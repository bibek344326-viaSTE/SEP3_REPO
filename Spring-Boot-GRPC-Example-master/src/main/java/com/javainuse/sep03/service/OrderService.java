package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.javainuse.orders.Order;
import com.javainuse.orders.OrderItem;
import com.javainuse.orders.OrderList;
import com.javainuse.orders.OrderRequest;
import com.javainuse.orders.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.nio.charset.StandardCharsets;

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {
    private final HttpClient client = HttpClients.createDefault();

    private final String baseUrl = "http://localhost:5203/api/orders";

    @Override
    public void createOrder(OrderRequest request, StreamObserver<Order> responseObserver) {
        try {
            HttpPost httpPost = new HttpPost(baseUrl);
            String json = "{" +
                    "\"UserId\": " + request.getUserId() + ", " +
                    "\"CreatedBy\": " + request.getCreatedBy() + ", " +
                    "\"OrderItems\": [" + request.getOrderItemsList().stream().map(item ->
                            "{" +
                                    "\"ItemId\": " + item.getItemId() + ", " +
                                    "\"QuantityToPick\": " + item.getQuantityToPick() +
                                    "}")
                    .reduce((a, b) -> a + "," + b).orElse("") +
                    "]" +
                    "}";
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            Order response = client.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 201) {
                    byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    // Extract order details from the response
                    return Order.newBuilder()
                            .setOrderId(123) // Extract actual order ID
                            .addAllOrderItems(request.getOrderItemsList())
                            .setUserId(request.getUserId())
                            .setCreatedBy(request.getCreatedBy())
                            .setCreatedAt(Timestamp.newBuilder().build()) // Extract timestamp if available
                            .build();
                } else {
                    throw new RuntimeException("Failed to create order: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAllOrders(Empty request, StreamObserver<OrderList> responseObserver) {
        try {
            HttpGet httpGet = new HttpGet(baseUrl);

            OrderList response = client.execute(httpGet, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    // Extract order list from responseString
                    OrderList.Builder orderListBuilder = OrderList.newBuilder();
                    // Populate the orderListBuilder with orders extracted from the response
                    return orderListBuilder.build();
                } else {
                    throw new RuntimeException("Failed to get orders: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
