package com.javainuse.sep03.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.Empty;
import com.javainuse.item.Item;
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
    public void createOrder(OrderRequest request, StreamObserver<CreateOrderResponse> responseObserver) {
        logger.info("Received request to create order: {}", request);

        try {
            // Map gRPC OrderRequest to REST API CreateOrderDTO
            CreateOrderDTO createOrderDTO = new CreateOrderDTO(
                    Instant.ofEpochSecond(request.getDeliveryDate().getSeconds()).toString(), // Convert gRPC timestamp to ISO-8601 date string
                    request.getOrderItemsList().stream().map(orderItemDTO -> {
                        try {
                            logger.info("Processing order item: {}", orderItemDTO);
                            return new RestOrderItemDTO(
                                    orderItemDTO.getItemId(), // Send only itemId for create
                                    orderItemDTO.getQuantityToPick()
                            );
                        } catch (Exception e) {
                            logger.error("Failed to map order item: {}", orderItemDTO, e);
                            throw new RuntimeException("Error while processing order item", e);
                        }
                    }).collect(Collectors.toList()),
                    request.getCreatedBy(),
                    Instant.now().toString() // Set createdAt as current timestamp in ISO-8601 format
            );

            logger.info("Mapped CreateOrderDTO to send to REST API: {}", createOrderDTO);

            webClient.post()
                    .uri("/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(createOrderDTO), CreateOrderDTO.class)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        logger.error("Error from REST API: {}", response.statusCode());
                        return Mono.error(new RuntimeException("Error calling REST API"));
                    })
                    .bodyToMono(Boolean.class)
                    .doOnSuccess(success -> logger.info("Order created successfully: {}", success))
                    .doOnError(error -> logger.error("Failed to create order", error))
                    .subscribe(
                            success -> {
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
        logger.info("Received request to get all orders from REST API");

        webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(List.class)
                .subscribe(
                        orders -> {
                            try {
                                List<OrderDTO> grpcOrderList = ((List<?>) orders).stream().map(order -> {
                                    try {
                                        Map<String, Object> orderMap = (Map<String, Object>) order;
                                        List<OrderItemDTO> orderItems = ((List<?>) orderMap.getOrDefault("orderItems", List.of()))
                                                .stream().map(orderItem -> {
                                                    Map<String, Object> orderItemMap = (Map<String, Object>) orderItem;
                                                    Map<String, Object> itemMap = (Map<String, Object>) orderItemMap.get("item");

                                                    return OrderItemDTO.newBuilder()
                                                            .setItem(Item.newBuilder()
                                                                    .setItemId((Integer) itemMap.getOrDefault("itemId", 0))
                                                                    .setItemName((String) itemMap.getOrDefault("itemName", "Unknown"))
                                                                    .setDescription(itemMap.get("description") != null ? (String) itemMap.get("description") : "") // Null check for description
                                                                    .setQuantityInStore((Integer) itemMap.getOrDefault("quantityInStore", 0))
                                                                    .build())
                                                            .setQuantityToPick((Integer) orderItemMap.getOrDefault("quantityToPick", 0))
                                                            .build();
                                                }).collect(Collectors.toList());

                                        return OrderDTO.newBuilder()
                                                .setOrderId((Integer) orderMap.getOrDefault("orderId", 0))
                                                .addAllOrderItems(orderItems)
                                                .setCreatedAt(com.google.protobuf.Timestamp.newBuilder()
                                                        .setSeconds(((String) orderMap.getOrDefault("createdAt", "1970-01-01T00:00:00Z")).hashCode())
                                                        .build())
                                                .setAssignedUser(orderMap.get("assignedUser") != null ? orderMap.get("assignedUser").toString() : "Unassigned") // Null check for assignedUser
                                                .setCreatedByUser(orderMap.getOrDefault("createdBy", "Unknown").toString()) // Null check for createdBy
                                                .setOrderStatus(OrderStatus.valueOf((String) orderMap.getOrDefault("orderStatus", "IN_PROGRESS"))) // Null check for orderStatus
                                                .setDeliveryDate(com.google.protobuf.Timestamp.newBuilder()
                                                        .setSeconds(((String) orderMap.getOrDefault("deliveryDate", "1970-01-01T00:00:00Z")).hashCode())
                                                        .build())
                                                .build();
                                    } catch (Exception e) {
                                        logger.error("Failed to process order: {}", order, e);
                                        throw new RuntimeException(e);
                                    }
                                }).collect(Collectors.toList());

                                OrderList orderList = OrderList.newBuilder().addAllOrders(grpcOrderList).build();
                                responseObserver.onNext(orderList);
                                responseObserver.onCompleted();
                            } catch (Exception e) {
                                logger.error("Exception while processing orders", e);
                                responseObserver.onError(e);
                            }
                        },
                        error -> {
                            logger.error("Error occurred while fetching orders", error);
                            responseObserver.onError(error);
                        }
                );


    }
    public static class CreateOrderDTO {

        @JsonProperty("deliveryDate")
        private String deliveryDate;

        @JsonProperty("orderItems")
        private List<RestOrderItemDTO> orderItems;

        @JsonProperty("createdBy")
        private int createdBy;

        @JsonProperty("createdAt")
        private String createdAt;

        public CreateOrderDTO(String deliveryDate, List<RestOrderItemDTO> orderItems, int createdBy, String createdAt) {
            this.deliveryDate = deliveryDate; // ISO-8601 formatted string
            this.orderItems = orderItems;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public List<RestOrderItemDTO> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<RestOrderItemDTO> orderItems) {
            this.orderItems = orderItems;
        }

        public int getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(int createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        @Override
        public String toString() {
            return "CreateOrderDTO{" +
                    "deliveryDate='" + deliveryDate + '\'' +
                    ", orderItems=" + orderItems +
                    ", createdBy=" + createdBy +
                    ", createdAt='" + createdAt + '\'' +
                    '}';
        }
    }
    public static class RestOrderItemDTO {

        @JsonProperty("itemId")
        private int itemId;

        @JsonProperty("quantityToPick")
        private int quantityToPick;

        public RestOrderItemDTO(int itemId, int quantityToPick) {
            this.itemId = itemId; // Send only the item ID
            this.quantityToPick = quantityToPick;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getQuantityToPick() {
            return quantityToPick;
        }

        public void setQuantityToPick(int quantityToPick) {
            this.quantityToPick = quantityToPick;
        }

        @Override
        public String toString() {
            return "RestOrderItemDTO{" +
                    "itemId=" + itemId +
                    ", quantityToPick=" + quantityToPick +
                    '}';
        }
    }

}
