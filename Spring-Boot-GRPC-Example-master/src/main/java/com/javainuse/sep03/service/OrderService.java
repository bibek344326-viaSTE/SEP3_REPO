package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.javainuse.orders.Order;
import com.javainuse.orders.OrderItem;
import com.javainuse.orders.OrderList;
import com.javainuse.orders.OrderRequest;
import com.javainuse.orders.OrderServiceGrpc;
import com.javainuse.user.Role;
import com.javainuse.user.User; // Import the gRPC User type
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

class RestUser {
    public int userId;
    public String userName;
    public String password; // Might be empty as per the C# code
    public String userRole;
}

class RestOrderItem {
    public int orderItemId;
    public int orderId;
    public int itemId;
    public int quantityToPick;
}

class RestOrder {
    public int orderId;
    public String orderStatus;
    public String deliveryDate; // ISO-8601 or string representation
    public List<RestOrderItem> orderItems;

    public RestUser createdBy;      // Detailed user who created the order
    public RestUser assignedUser;   // Detailed user to whom the order is assigned, can be null
    public String createdAt;        // ISO-8601 string, parseable as OffsetDateTime
}

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final WebClient webClient;
    private final String baseUrl = "http://localhost:5203/Orders"; // Adjust to your actual OrdersController endpoint

    public OrderService() {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public void createOrder(OrderRequest request, StreamObserver<Order> responseObserver) {
        // Create RestOrder from the request
        RestOrder restOrder = new RestOrder();

        // The proto has user_id and createdBy separately
        // We map:
        // - createdBy to restOrder.createdBy.userId
        // - user_id to restOrder.assignedUser.userId (the user who will handle this order)
        restOrder.createdBy = new RestUser();
        restOrder.createdBy.userId = request.getCreatedBy();

        restOrder.assignedUser = new RestUser();
        restOrder.assignedUser.userId = request.getUserId();

        // Set the order status and delivery date - as per your logic
        restOrder.orderStatus = "IN_PROGRESS";
        restOrder.deliveryDate = OffsetDateTime.now().plusDays(7).toString();

        // Convert order items
        restOrder.orderItems = request.getOrderItemsList().stream().map(oi -> {
            RestOrderItem item = new RestOrderItem();
            item.itemId = oi.getItemId();
            item.quantityToPick = oi.getQuantityToPick();
            return item;
        }).collect(Collectors.toList());

        webClient.post()
                .uri("") // baseUrl is already set
                .bodyValue(restOrder)
                .retrieve()
                .bodyToMono(RestOrder.class)
                .subscribe(
                        createdRestOrder -> {
                            Order response = convertRestOrderToProto(createdRestOrder);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        responseObserver::onError
                );
    }

    @Override
    public void getAllOrders(Empty request, StreamObserver<OrderList> responseObserver) {
        System.out.println("Starting getAllOrders() request...");

        webClient.get()
                .uri("")
                .retrieve()
                .toEntityList(RestOrder.class)
                .doOnNext(response -> {
                    System.out.println("HTTP Status: " + response.getStatusCode());
                    System.out.println("Response Headers: " + response.getHeaders());
                })
                .map(response -> response.getBody())
                .doOnNext(restOrders -> {
                    if (restOrders == null) {
                        System.out.println("Error: Response body is null.");
                    } else {
                        System.out.println("Number of orders received: " + restOrders.size());
                        for (int i = 0; i < restOrders.size(); i++) {
                            System.out.println("Order " + (i + 1) + ": " + restOrders.get(i));
                        }
                    }
                })
                .map(restOrders -> {
                    try {
                        if (restOrders == null) {
                            throw new RuntimeException("Response from REST API is null.");
                        }

                        // Convert List<RestOrder> to OrderList
                        List<Order> protoOrders = restOrders.stream()
                                .map(this::convertRestOrderToProto)
                                .collect(Collectors.toList());
                        System.out.println("Mapped " + protoOrders.size() + " orders to gRPC OrderList.");
                        return OrderList.newBuilder().addAllOrders(protoOrders).build();
                    } catch (Exception e) {
                        System.out.println("Error mapping RestOrders to OrderList: " + e.getMessage());
                        e.printStackTrace();
                        throw e; // Re-throw so it gets caught downstream
                    }
                })
                .doOnNext(orderList -> {
                    System.out.println("OrderList ready to be sent to the client: " + orderList);
                })
                .subscribe(
                        orderList -> {
                            System.out.println("Sending OrderList to gRPC client...");
                            responseObserver.onNext(orderList);
                            responseObserver.onCompleted();
                        },
                        throwable -> {
                            System.out.println("Error occurred during getAllOrders(): " + throwable.getMessage());
                            throwable.printStackTrace();
                            responseObserver.onError(throwable);
                        }
                );
    }



    // Utility method to convert from RestOrder to the proto Order message
    private Order convertRestOrderToProto(RestOrder ro) {
        if (ro == null) {
            System.out.println("RestOrder is null.");
            return Order.newBuilder().build();
        }

        try {
            Order.Builder builder = Order.newBuilder()
                    .setOrderId(ro.orderId)
                    .setOrderStatus(Integer.parseInt(ro.orderStatus));

            // Handle null assignedUser and createdBy
            if (ro.assignedUser != null) {
                User assignedUserProto = convertRestUserToProto(ro.assignedUser);
                builder.setAssignedUser(assignedUserProto); // Only set if assignedUser is not null
            } else {
                System.out.println("Warning: assignedUser is null for order " + ro.orderId);
                // Do not set assignedUser at all, let protobuf handle it as "not set"
            }

            if (ro.createdBy != null) {
                User createdByProto = convertRestUserToProto(ro.createdBy);
                builder.setCreatedByUser(createdByProto);
            } else {
                System.out.println("Warning: createdBy is null for order " + ro.orderId);
                builder.setCreatedByUser(User.newBuilder().build()); // Set default empty User
            }

            if (ro.orderItems != null) {
                for (RestOrderItem roi : ro.orderItems) {
                    OrderItem protoItem = OrderItem.newBuilder()
                            .setOrderItemId(roi.orderItemId)
                            .setItemId(roi.itemId)
                            .setQuantityToPick(roi.quantityToPick)
                            .build();
                    builder.addOrderItems(protoItem);
                }
            }

            if (ro.createdAt != null) {
                OffsetDateTime odt = OffsetDateTime.parse(ro.createdAt);
                long seconds = odt.toEpochSecond();
                int nanos = odt.getNano();
                builder.setCreatedAt(Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos));
            }

            if (ro.deliveryDate != null) {
                OffsetDateTime odt = OffsetDateTime.parse(ro.deliveryDate);
                long seconds = odt.toEpochSecond();
                int nanos = odt.getNano();
                builder.setDeliveryDate(Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos));
            }

            return builder.build();
        } catch (Exception e) {
            System.out.println("Error in convertRestOrderToProto for order: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }



    private User convertRestUserToProto(RestUser restUser) {
        if (restUser == null) {
            System.out.println("Warning: RestUser is null.");
            return User.newBuilder().build();
        }

        return User.newBuilder()
                .setUserid(restUser.userId > 0 ? String.valueOf(restUser.userId) : "0")
                .setUsername(restUser.userName != null ? restUser.userName : "unknown")
                .setPassword(restUser.password != null ? restUser.password : "")
                .setRole(Role.INVENTORY_MANAGER) // Default role
                .build();
    }

}
