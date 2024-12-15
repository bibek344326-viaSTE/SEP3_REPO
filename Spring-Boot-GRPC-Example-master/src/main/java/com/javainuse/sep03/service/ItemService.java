package com.javainuse.sep03.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@GrpcService
public class ItemService extends ItemServiceGrpc.ItemServiceImplBase {
    private final HttpClient client = HttpClients.createDefault();
    private final String baseUrl = "http://localhost:5203/Items";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Create a new item
     */
    @Override
    public void createItem(ItemDTO request, StreamObserver<Item> responseObserver) {
        try {
            String json = objectMapper.writeValueAsString(new SimpleItem(
                    request.getName(),
                    request.getDescription(),
                    request.getQuantityInStore()
            ));

            HttpPost httpPost = new HttpPost(baseUrl);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            Item response = client.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 201) {
                    String responseString = null;
                    try {
                        responseString = getResponseString(httpResponse.getEntity().getContent());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Response body from REST API: " + responseString);

                    SimpleItem simpleItem = objectMapper.readValue(responseString, SimpleItem.class);
                    return toGrpcItem(simpleItem);
                } else {
                    throw new RuntimeException("Failed to create item. Status code: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            logError("createItem", e);
            responseObserver.onError(e);
        }
    }

    /**
     * Get all items from the API
     */
    @Override
    public void getAllItems(Empty request, StreamObserver<ItemList> responseObserver) {
        try {
            System.out.println("Sending GET request to URL: " + baseUrl);
            HttpGet httpGet = new HttpGet(baseUrl);

            client.execute(httpGet, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    String responseString = null;
                    try {
                        responseString = getResponseString(httpResponse.getEntity().getContent());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Response body from REST API: " + responseString);

                    List<Item> simpleItems = objectMapper.readValue(responseString, new TypeReference<List<Item>>() {});

                    System.out.println("Parsed simple items: " + simpleItems);

                    ItemList.Builder itemListBuilder = ItemList.newBuilder();
                    itemListBuilder.addAllItems(simpleItems);


                    responseObserver.onNext(itemListBuilder.build());
                    responseObserver.onCompleted();
                } else {
                    throw new RuntimeException("Failed to get items. Status code: " + httpResponse.getCode());
                }
                return null;
            });

        } catch (Exception e) {
            logError("getAllItems", e);
            responseObserver.onError(e);
        }
    }

    /**
     * Edit an item
     */
    @Override
    public void editItem(Item request, StreamObserver<Empty> responseObserver) {
        try {
            String json = objectMapper.writeValueAsString(new SimpleItem(
                    request.getItemId(),
                    request.getItemName(),
                    request.getDescription(),
                    request.getQuantityInStore()
            ));

            HttpPut httpPut = new HttpPut(baseUrl + "/" + request.getItemId());
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            client.execute(httpPut, httpResponse -> {
                if (httpResponse.getCode() == 204) {
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                    return null;
                } else {
                    throw new RuntimeException("Failed to edit item. Status code: " + httpResponse.getCode());
                }
            });
        } catch (Exception e) {
            logError("editItem", e);
            responseObserver.onError(e);
        }
    }

    /**
     * Delete an item
     */
    @Override
    public void deleteItem(Item request, StreamObserver<Empty> responseObserver) {
        try {
            HttpDelete httpDelete = new HttpDelete(baseUrl + "/" + request.getItemId());

            client.execute(httpDelete, httpResponse -> {
                if (httpResponse.getCode() == 204) {
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                    return null;
                } else {
                    throw new RuntimeException("Failed to delete item. Status code: " + httpResponse.getCode());
                }
            });
        } catch (Exception e) {
            logError("deleteItem", e);
            responseObserver.onError(e);
        }
    }

    /**
     * Converts a SimpleItem (POJO) to gRPC Item (generated proto class)
     */
    private Item toGrpcItem(SimpleItem simpleItem) {
        return Item.newBuilder()
                .setItemId(simpleItem.itemId)
                .setItemName(simpleItem.itemName)
                .setDescription(simpleItem.description)
                .setQuantityInStore(simpleItem.quantityInStore)
                .build();
    }

    /**
     * Helper method to log errors
     */
    private void logError(String methodName, Exception e) {
        System.out.println("Error during " + methodName + " request: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Helper method to convert InputStream to String
     */
    private String getResponseString(InputStream responseStream) throws Exception {
        return new BufferedReader(new InputStreamReader(responseStream))
                .lines().collect(Collectors.joining("\n"));
    }

    /**
     * Helper class for constructing request payloads
     */
    @JsonIgnoreProperties(ignoreUnknown = true) // This will ignore unknown properties like "orderItems"
    public static class SimpleItem {
        public String itemId;
        public String itemName;
        public String description;
        public int quantityInStore;

        public SimpleItem() {}

        public SimpleItem(String itemName, String description, int quantityInStore) {
            this.itemName = itemName;
            this.description = description;
            this.quantityInStore = quantityInStore;
        }

        public SimpleItem(String itemId, String itemName, String description, int quantityInStore) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.description = description;
            this.quantityInStore = quantityInStore;
        }

        @Override
        public String toString() {
            return "SimpleItem{" +
                    "itemId='" + itemId + '\'' +
                    ", itemName='" + itemName + '\'' +
                    ", description='" + description + '\'' +
                    ", quantityInStore=" + quantityInStore +
                    '}';
        }
    }
}
