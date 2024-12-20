package com.javainuse.sep03.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.Empty;
import com.javainuse.orders.*;
import io.grpc.stub.StreamObserver;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://localhost:7211/Orders")
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().secure(sslContextSpec ->
                            sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
                    )
            ))
            .build();

    @Override
    public void createOrder(CreateOrder request, StreamObserver<CreateOrderResponse> responseObserver) {
        logger.info("Received request to create order");

        try {
            OrderData createOrderDTO = new OrderData(
                    Instant.ofEpochSecond(request.getDeliveryDate().getSeconds()).toString(),
                    request.getOrderItemsList().stream().map(orderItem ->
                            new OrderItemData(orderItem.getItemId(), orderItem.getQuantityToPick())
                    ).collect(Collectors.toList()),
                    request.getCreatedBy(),
                    Instant.now().toString()
            );

            webClient.post()
                    .uri("/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(createOrderDTO), OrderData.class)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        logger.error("Error from REST API with status: {}", response.statusCode());
                        return Mono.error(new RuntimeException("Error calling REST API"));
                    })
                    .bodyToMono(Boolean.class)
                    .subscribe(
                            success -> {
                                logger.info("Order created successfully");
                                CreateOrderResponse response = CreateOrderResponse.newBuilder().setSuccess(success).build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            },
                            error -> {
                                logger.error("Error occurred in createOrder: ", error);
                                responseObserver.onError(error);
                            }
                    );
        } catch (Exception e) {
            logger.error("Exception occurred while processing createOrder", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAllOrders(Empty request, StreamObserver<OrderList> responseObserver) {
        logger.info("Fetching all orders from REST API");

        webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(List.class)
                .subscribe(
                        orders -> {
                            try {
                                List<Order> grpcOrderList = ((List<?>) orders).stream().map(order -> {
                                    try {
                                        Map<String, Object> orderMap = (Map<String, Object>) order;

                                        // Safely extract order items
                                        List<GetOrderItem> orderItems = ((List<?>) orderMap.getOrDefault("orderItems", List.of()))
                                                .stream().map(orderItem -> {
                                                    try {
                                                        Map<String, Object> orderItemMap = (Map<String, Object>) orderItem;
                                                        String itemName = Optional.ofNullable((String) orderItemMap.get("itemName")).orElse("Unknown");
                                                        int quantityToPick = ((Number) orderItemMap.getOrDefault("quantityToPick", 0)).intValue();
                                                        return GetOrderItem.newBuilder()
                                                                .setItemName(itemName)
                                                                .setQuantityToPick(quantityToPick)
                                                                .build();
                                                    } catch (Exception e) {
                                                        logger.error("Error while processing order item: {}", orderItem, e);
                                                        throw e;
                                                    }
                                                }).collect(Collectors.toList());

                                        // Safely extract order-level fields
                                        int orderId = ((Number) orderMap.getOrDefault("orderId", 0)).intValue();
                                        String assignedUser = Optional.ofNullable((String) orderMap.get("assignedUser")).orElse("Unassigned");
                                        String createdBy = Optional.ofNullable((String) orderMap.get("createdBy")).orElse("Unknown");
                                        String orderStatusStr = Optional.ofNullable((String) orderMap.get("orderStatus")).orElse("IN_PROGRESS");

                                        // Handle date parsing with null checks
                                        Instant deliveryDate = Optional.ofNullable(orderMap.get("deliveryDate"))
                                                .map(dateStr -> {
                                                    try {
                                                        return Instant.parse((String) dateStr);
                                                    } catch (Exception e) {
                                                        logger.warn("Invalid delivery date format: {}", dateStr, e);
                                                        return Instant.now(); // Default to now if the date is invalid
                                                    }
                                                }).orElse(Instant.now());

                                        Instant createdAt = Optional.ofNullable(orderMap.get("createdAt"))
                                                .map(dateStr -> {
                                                    try {
                                                        return Instant.parse((String) dateStr);
                                                    } catch (Exception e) {
                                                        logger.warn("Invalid created at date format: {}", dateStr, e);
                                                        return Instant.now(); // Default to now if the date is invalid
                                                    }
                                                }).orElse(Instant.now());

                                        // Safely build the Order object
                                        return Order.newBuilder()
                                                .setOrderId(orderId)
                                                .addAllOrderItems(orderItems)
                                                .setCreatedAt(com.google.protobuf.Timestamp.newBuilder()
                                                        .setSeconds(createdAt.getEpochSecond())
                                                        .setNanos(createdAt.getNano())
                                                        .build())
                                                .setAssignedUser(assignedUser)
                                                .setCreatedByUser(createdBy)
                                                .setOrderStatus(OrderStatus.valueOf(orderStatusStr))
                                                .setDeliveryDate(com.google.protobuf.Timestamp.newBuilder()
                                                        .setSeconds(deliveryDate.getEpochSecond())
                                                        .setNanos(deliveryDate.getNano())
                                                        .build())
                                                .build();
                                    } catch (Exception e) {
                                        logger.error("Error while processing order: {}", order, e);
                                        throw e;
                                    }
                                }).collect(Collectors.toList());

                                logger.info("Successfully fetched {} orders", grpcOrderList.size());

                                OrderList orderList = OrderList.newBuilder().addAllOrders(grpcOrderList).build();
                                responseObserver.onNext(orderList);
                                responseObserver.onCompleted();
                            } catch (Exception e) {
                                logger.error("Error processing orders from REST API response", e);
                                responseObserver.onError(e);
                            }
                        },
                        error -> {
                            logger.error("Error occurred while fetching orders from REST API", error);
                            responseObserver.onError(error);
                        }
                );
    }


    public static class OrderData {
        @JsonProperty("deliveryDate")
        private String deliveryDate;

        @JsonProperty("orderItems")
        private List<OrderItemData> orderItems;

        @JsonProperty("createdBy")
        private int createdBy;

        @JsonProperty("createdAt")
        private String createdAt;

        public OrderData(String deliveryDate, List<OrderItemData> orderItems, int createdBy, String createdAt) {
            this.deliveryDate = deliveryDate;
            this.orderItems = orderItems;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
        }
    }

    public static class OrderItemData {
        @JsonProperty("itemName")
        private int itemName;

        @JsonProperty("quantityToPick")
        private int quantityToPick;

        public OrderItemData(int itemId, int quantityToPick) {
            this.itemName = itemId;
            this.quantityToPick = quantityToPick;
        }
    }
}
