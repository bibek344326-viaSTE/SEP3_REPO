package com.javainuse.sep03.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.javainuse.item.Item;
import com.javainuse.orders.*;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import io.grpc.stub.StreamObserver;
import reactor.netty.http.client.HttpClient;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields
class RestOrder {
    private int orderId;
    private OrderStatus orderStatus;
    private String deliveryDate;
    private List<RestOrderItem> orderItems;
    private String assignedUser;
    private String createdBy;
    private String createdAt;

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }

    public List<RestOrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<RestOrderItem> orderItems) { this.orderItems = orderItems; }

    public String getAssignedUser() { return assignedUser; }
    public void setAssignedUser(String assignedUser) { this.assignedUser = assignedUser; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields
class RestOrderItem {
    private RestItem item;
    private int quantityToPick;

    public RestItem getItem() { return item; }
    public void setItem(RestItem item) { this.item = item; }

    public int getQuantityToPick() { return quantityToPick; }
    public void setQuantityToPick(int quantityToPick) { this.quantityToPick = quantityToPick; }
}

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {


    WebClient webClient = WebClient.builder()
            .baseUrl("https://localhost:7211/Orders")
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().secure(sslContextSpec ->
                            sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
                    )
            ))
            .build();


    @Override
    public void getAllOrders(Empty request, StreamObserver<OrderList> responseObserver) {
        System.out.println("[INFO] Calling REST API to fetch all orders");

        webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(responseBody -> System.out.println("[INFO] Raw REST response: " + responseBody))
                .map(responseBody -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<RestOrder> orderList = mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, RestOrder.class));
                        System.out.println("[INFO] Successfully deserialized response with size: " + orderList.size());

                        OrderList.Builder responseBuilder = OrderList.newBuilder();
                        for (RestOrder o : orderList) {
                            OrderDTO.Builder orderBuilder = OrderDTO.newBuilder()
                                    .setOrderId(o.getOrderId())
                                    .setOrderStatus(o.getOrderStatus())
                                    .setAssignedUser(o.getAssignedUser() != null ? o.getAssignedUser() : "Unassigned")
                                    .setCreatedByUser(o.getCreatedBy() != null ? o.getCreatedBy() : "Unknown Creator");

                            orderBuilder.setDeliveryDate(parseTimestamp(o.getDeliveryDate(), "deliveryDate", o.getOrderId()));
                            orderBuilder.setCreatedAt(parseTimestamp(o.getCreatedAt(), "createdAt", o.getOrderId()));

                            if (o.getOrderItems() != null) {
                                for (RestOrderItem item : o.getOrderItems()) {
                                    if (item.getItem() != null) {
                                        RestItem restItem = item.getItem();
                                        Item grpcItem = Item.newBuilder()
                                                .setItemId(String.valueOf(restItem.getItemId()))
                                                .setItemName(restItem.getItemName() != null ? restItem.getItemName() : "Unknown Product")
                                                .setQuantityInStore(restItem.getQuantityInStore())
                                                .build();

                                        OrderItemDTO itemDto = OrderItemDTO.newBuilder()
                                                .setItem(grpcItem)
                                                .setQuantityToPick(item.getQuantityToPick())
                                                .build();

                                        orderBuilder.addOrderItems(itemDto);
                                    }
                                }
                            }
                            responseBuilder.addOrders(orderBuilder.build());
                        }
                        return responseBuilder.build();
                    } catch (Exception e) {
                        System.err.println("[ERROR] Failed to deserialize REST API response.");
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .subscribe(
                        responseObserver::onNext, // onNext (only when successful)
                        error -> {
                            System.err.println("[ERROR] Error occurred during gRPC response: " + error.getMessage());
                                responseObserver.onError(error); // Send an error back to the client

                        },
                        () -> {
                            System.out.println("[INFO] Completed sending all orders to gRPC client.");
                                responseObserver.onCompleted(); // ✅ Mark the stream as completed

                        }
                );
    }

    private Timestamp parseTimestamp(String dateTime, String fieldName, int orderId) {
        try {
            Instant instant = Instant.parse(dateTime);
            return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
        } catch (Exception e) {
            System.err.println("[ERROR] Error parsing " + fieldName + " for Order ID: " + orderId);
            return Timestamp.newBuilder().setSeconds(0).setNanos(0).build();
        }
    }

    private OrderStatus mapOrderStatus(String status) {
        return "COMPLETED".equals(status) ? OrderStatus.COMPLETED : OrderStatus.IN_PROGRESS;
    }
}
