package com.javainuse.sep03.service;

import com.javainuse.item.Item;
import com.javainuse.item.ItemDTO;

import com.javainuse.item.ItemList;
import com.javainuse.item.ItemServiceGrpc;
import com.google.protobuf.Empty;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import io.grpc.stub.StreamObserver;
import reactor.netty.http.client.HttpClient;

import java.util.List;

// The REST API returns/accepts JSON objects with fields matching these:
class RestItem {
    private int itemId;
    private String itemName;
    private String description;
    private int quantityInStore;

    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getQuantityInStore() {
        return quantityInStore;
    }
    public void setQuantityInStore(int quantityInStore) {
        this.quantityInStore = quantityInStore;
    }
}

class RestItemDto {
    private String itemName;
    private String description;
    private int quantityInStore;

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getQuantityInStore() {
        return quantityInStore;
    }
    public void setQuantityInStore(int quantityInStore) {
        this.quantityInStore = quantityInStore;
    }
}

@GrpcService
public class ItemService extends ItemServiceGrpc.ItemServiceImplBase {

    WebClient webClient = WebClient.builder()
            .baseUrl("https://localhost:7211/Items")
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().secure(sslContextSpec ->
                            sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
                    )
            ))
            .build();

    @Override
    public void editItem(Item request, StreamObserver<Empty> responseObserver) {
        System.out.println("Received edit request: " + request);

        // To edit an item, we do a PUT /Items/{id}
        String id = request.getItemId();
        System.out.println("Item ID: " + id);

        if (id == null || id.isEmpty()) {
            System.err.println("Error: Item ID is required");
            responseObserver.onError(new IllegalArgumentException("Item ID is required"));
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            System.err.println("Error: Invalid Item ID");
            responseObserver.onError(new IllegalArgumentException("Invalid Item ID"));
            return;
        }

        RestItem restItem = new RestItem();
        restItem.setItemId(itemId);
        restItem.setItemName(request.getItemName());
        restItem.setDescription(request.getDescription());
        restItem.setQuantityInStore(request.getQuantityInStore());

        System.out.println("Calling REST API with RestItem: " + restItem);

        try {
            webClient.put()
                    .uri("/{id}", itemId)
                    .bodyValue(restItem)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // Blocking call

            System.out.println("Item updated successfully: " + restItem);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("Error occurred during REST API call: " + e.getMessage());
            responseObserver.onError(e);
        }
    }


    @Override
    public void deleteItem(Item request, StreamObserver<Empty> responseObserver) {
        // DELETE /Items/{id}
        String id = request.getItemId();
        if (id == null || id.isEmpty()) {
            responseObserver.onError(new IllegalArgumentException("Item ID is required"));
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            responseObserver.onError(new IllegalArgumentException("Invalid Item ID"));
            return;
        }

        try {
            webClient.delete()
                    .uri("/{id}", itemId)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // Blocking

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAllItems(Empty request, StreamObserver<ItemList> responseObserver) {
        // GET /Items
        try {
            List<RestItem> restItems = webClient.get()
                    .uri("")
                    .retrieve()
                    .bodyToFlux(RestItem.class)
                    .collectList()
                    .block(); // Blocking call

            if (restItems == null) {
                responseObserver.onError(new RuntimeException("No items returned"));
                return;
            }

            ItemList.Builder itemListBuilder = ItemList.newBuilder();
            for (RestItem ri : restItems) {
                Item grpcItem = Item.newBuilder()
                        .setItemId(String.valueOf(ri.getItemId()))
                        .setItemName(ri.getItemName())
                        .setDescription(ri.getDescription() == null ? "" : ri.getDescription())
                        .setQuantityInStore(ri.getQuantityInStore())
                        .build();
                itemListBuilder.addItems(grpcItem);
            }

            responseObserver.onNext(itemListBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void createItem(ItemDTO request,
                           StreamObserver<Item> responseObserver) {
        RestItemDto dto = new RestItemDto();
        dto.setItemName(request.getName());
        dto.setDescription(request.getDescription());
        dto.setQuantityInStore(request.getQuantityInStore());

        try {
            RestItem createdItem = webClient.post()
                    .uri("")
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(RestItem.class)
                    .block();

            if (createdItem == null) {
                responseObserver.onError(new RuntimeException("Failed to create item"));
                return;
            }

            com.javainuse.item.Item grpcItem = com.javainuse.item.Item.newBuilder()
                    .setItemId(String.valueOf(createdItem.getItemId()))
                    .setItemName(createdItem.getItemName())
                    .setDescription(createdItem.getDescription() == null ? "" : createdItem.getDescription())
                    .setQuantityInStore(createdItem.getQuantityInStore())
                    .build();

            responseObserver.onNext(grpcItem);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

}
