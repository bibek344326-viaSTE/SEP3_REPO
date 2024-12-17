package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.javainuse.orders.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import java.util.List;

class RestOrder {
    private int orderId;
    private String orderStatus;
    private String deliveryDate;
    private List<OrderItem> orderItems;
    private RestUserDTO assignedUser;
    private RestUserDTO createdBy;
    private Integer userId;
    private Integer createdById;
    private String createdAt;

    int getOrderId() { return orderId; }
    void setOrderId(int orderId) { this.orderId = orderId; }
    String getOrderStatus() { return orderStatus; }
    void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    String getDeliveryDate() { return deliveryDate; }
    void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
    List<OrderItem> getOrderItems() { return orderItems; }
    void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    RestUserDTO getAssignedUser() { return assignedUser; }
    void setAssignedUser(RestUserDTO assignedUser) { this.assignedUser = assignedUser; }
    RestUserDTO getCreatedBy() { return createdBy; }
    void setCreatedBy(RestUserDTO createdBy) { this.createdBy = createdBy; }
    Integer getUserId() { return userId; }
    void setUserId(Integer userId) { this.userId = userId; }
    Integer getCreatedById() { return createdById; }
    void setCreatedById(Integer createdById) { this.createdById = createdById; }
    String getCreatedAt() { return createdAt; }
    void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

class OrderItem {
    private int orderItemId;
    private String productName;
    private int quantityToPick;
    private int itemId;

    int getOrderItemId() { return orderItemId; }
    void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }
    String getProductName() { return productName; }
    void setProductName(String productName) { this.productName = productName; }
    int getQuantityToPick() { return quantityToPick; }
    void setQuantityToPick(int quantityToPick) { this.quantityToPick = quantityToPick; }
    int getItemId() { return itemId; }
    void setItemId(int itemId) { this.itemId = itemId; }
}

class RestUserDTO {
    private String userName;

    String getUserName() { return userName; }
    void setUserName(String userName) { this.userName = userName; }
}

class CreateOrderRequest {
    private String orderStatus;
    private String deliveryDate;
    private List<CreateOrderItemRequest> orderItems;
    private Integer userId;
    private Integer createdById;

    String getOrderStatus() { return orderStatus; }
    void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    String getDeliveryDate() { return deliveryDate; }
    void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
    List<CreateOrderItemRequest> getOrderItems() { return orderItems; }
    void setOrderItems(List<CreateOrderItemRequest> orderItems) { this.orderItems = orderItems; }
    Integer getUserId() { return userId; }
    void setUserId(Integer userId) { this.userId = userId; }
    Integer getCreatedById() { return createdById; }
    void setCreatedById(Integer createdById) { this.createdById = createdById; }
}

class CreateOrderItemRequest {
    private int itemId;
    private int quantityToPick;

    int getItemId() { return itemId; }
    void setItemId(int itemId) { this.itemId = itemId; }
    int getQuantityToPick() { return quantityToPick; }
    void setQuantityToPick(int quantityToPick) { this.quantityToPick = quantityToPick; }
}

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:5203/Orders").build();

    @Override
    public void getAllOrders(Empty request, StreamObserver<OrderList> responseObserver) {
        webClient.get().uri("/").retrieve().bodyToFlux(RestOrder.class)
                .collectList()
                .map(orderList -> {
                    System.out.println("Received response from REST API: " + orderList);
                    OrderList.Builder responseBuilder = OrderList.newBuilder();
                    for (RestOrder o : orderList) {
                        try {
                            System.out.println("Processing order with ID: " + o.getOrderId());
                            OrderDTO.Builder orderBuilder = OrderDTO.newBuilder()
                                    .setOrderId(o.getOrderId())
                                    .setOrderStatus(mapOrderStatus(o.getOrderStatus()));

                            // Parse deliveryDate safely
                            Timestamp deliveryTimestamp = parseTimestamp(o.getDeliveryDate(), "deliveryDate", o.getOrderId());
                            orderBuilder.setDeliveryDate(deliveryTimestamp);

                            // Parse createdAt safely
                            Timestamp createdAtTimestamp = parseTimestamp(o.getCreatedAt(), "createdAt", o.getOrderId());
                            orderBuilder.setCreatedAt(createdAtTimestamp);

                            orderBuilder.setAssignedUser(o.getAssignedUser() != null ? o.getAssignedUser().getUserName() : "");
                            orderBuilder.setCreatedByUser(o.getCreatedBy() != null ? o.getCreatedBy().getUserName() : "");

                            if (o.getOrderItems() != null) {
                                for (OrderItem item : o.getOrderItems()) {
                                    System.out.println("Processing item: " + item);
                                    OrderItemDTO itemDto = OrderItemDTO.newBuilder()
                                            .setProductName(item.getProductName() != null ? item.getProductName() : "")
                                            .setQuantityToPick(item.getQuantityToPick())
                                            .build();
                                    orderBuilder.addOrderItems(itemDto);
                                }
                            }
                            responseBuilder.addOrders(orderBuilder.build());
                        } catch (Exception e) {
                            System.err.println("Error processing order with ID: " + o.getOrderId());
                            e.printStackTrace();
                            throw new RuntimeException("Error processing order with ID: " + o.getOrderId(), e);
                        }
                    }
                    return responseBuilder.build();
                })
                .subscribe(
                        response -> {
                            System.out.println("Sending response to gRPC client: " + response);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        error -> {
                            System.err.println("Error during WebClient call or processing: " + error.getMessage());
                            error.printStackTrace();
                            responseObserver.onError(error);
                        }
                );
    }

    private Timestamp parseTimestamp(String dateTime, String fieldName, int orderId) {
        try {
            if (dateTime == null || dateTime.trim().isEmpty()) {
                System.err.println("Warning: " + fieldName + " is null or empty for Order ID: " + orderId);
                return Timestamp.newBuilder().setSeconds(0).setNanos(0).build(); // Default timestamp
            }
            System.out.println("Parsing " + fieldName + " for Order ID " + orderId + ": " + dateTime);
            Instant instant = Instant.parse(dateTime);
            return Timestamp.newBuilder()
                    .setSeconds(instant.getEpochSecond())
                    .setNanos(instant.getNano())
                    .build();
        } catch (Exception e) {
            System.err.println("Error parsing " + fieldName + " for Order ID: " + orderId + ". Value: " + dateTime);
            e.printStackTrace();
            return Timestamp.newBuilder().setSeconds(0).setNanos(0).build(); // Default timestamp
        }
    }

    private OrderStatus mapOrderStatus(String status) {
        return "COMPLETED".equals(status) ? OrderStatus.COMPLETED : OrderStatus.IN_PROGRESS;
    }
}
