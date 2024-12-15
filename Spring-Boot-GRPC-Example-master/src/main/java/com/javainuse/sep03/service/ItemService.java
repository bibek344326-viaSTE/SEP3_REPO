package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.javainuse.item.ItemDTO;
import com.javainuse.item.Item;
import com.javainuse.item.ItemList;
import com.javainuse.item.ItemServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.nio.charset.StandardCharsets;

@GrpcService
public class ItemService extends ItemServiceGrpc.ItemServiceImplBase {
    private final HttpClient client = HttpClients.createDefault();

    private final String baseUrl = "http://localhost:5000/api/items";

    @Override
    public void createItem(ItemDTO request, StreamObserver<Item> responseObserver) {
        try {
            HttpPost httpPost = new HttpPost(baseUrl);
            String json = "{" +
                    "\"ItemName\":\"" + request.getName() + "\", " +
                    "\"Description\":\"" + request.getDescription() + "\", " +
                    "\"QuantityInStore\": " + request.getQuantityInStore() +
                    "}";
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            Item response = client.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 201) {
                    byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    // Extract item details from the response
                    return Item.newBuilder()
                            .setId("extracted-id-here")
                            .setName(request.getName())
                            .setDescription(request.getDescription())
                            .setQuantityInStore(request.getQuantityInStore())
                            .build();
                } else {
                    throw new RuntimeException("Failed to create item: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void editItem(Item request, StreamObserver<Empty> responseObserver) {
        try {
            HttpPut httpPut = new HttpPut(baseUrl + "/" + request.getId());
            String json = "{" +
                    "\"ItemName\":\"" + request.getName() + "\", " +
                    "\"Description\":\"" + request.getDescription() + "\", " +
                    "\"QuantityInStore\": " + request.getQuantityInStore() +
                    "}";
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            client.execute(httpPut, httpResponse -> {
                if (httpResponse.getCode() == 204) {
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                    return null;
                } else {
                    throw new RuntimeException("Failed to edit item: " + httpResponse.getCode());
                }
            });
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void deleteItem(Item request, StreamObserver<Empty> responseObserver) {
        try {
            HttpDelete httpDelete = new HttpDelete(baseUrl + "/" + request.getId());

            client.execute(httpDelete, httpResponse -> {
                if (httpResponse.getCode() == 204) {
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                    return null;
                } else {
                    throw new RuntimeException("Failed to delete item: " + httpResponse.getCode());
                }
            });
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAllItems(Empty request, StreamObserver<ItemList> responseObserver) {
        try {
            HttpGet httpGet = new HttpGet(baseUrl);

            ItemList response = client.execute(httpGet, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    // Extract item list from responseString
                    ItemList.Builder itemListBuilder = ItemList.newBuilder();
                    // Populate the itemListBuilder with items extracted from the response
                    return itemListBuilder.build();
                } else {
                    throw new RuntimeException("Failed to get items: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
