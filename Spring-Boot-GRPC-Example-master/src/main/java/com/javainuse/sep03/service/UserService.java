package com.javainuse.sep03.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.protobuf.Empty;
import com.javainuse.user.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.io.IOException;
import java.util.List;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    WebClient webClient = WebClient.builder()
            .baseUrl("https://localhost:7211/Users")
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().secure(sslContextSpec ->
                            sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
                    )
            ))
            .build();

    @Override
    public void addUser(CreateUser request, StreamObserver<GetUser> responseObserver) {
        logger.info("Starting addUser request for user: {}", request.getUserName());

        UnifiedUserDTO restRequest = new UnifiedUserDTO(
                request.getUserName(),
                request.getPassword(),
                request.getUserRole().getNumber()
        );

        webClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(restRequest)
                .retrieve()
                .bodyToMono(UnifiedUserDTO.class)
                .doOnSuccess(userResponse -> {
                    logger.info("Received successful response from API: {}", userResponse);
                    GetUser userDTO = GetUser.newBuilder()
                            .setUserId(userResponse.getUserId())
                            .setUserName(userResponse.getUserName())
                            .setUserRole(UserRole.forNumber(userResponse.getUserRole()))
                            .build();

                    responseObserver.onNext(userDTO);
                    responseObserver.onCompleted();
                })
                .doOnError(error -> {
                    logger.error("Failed to create user: ", error);
                    responseObserver.onError(new RuntimeException("Failed to create user: " + error.getMessage()));
                })
                .subscribe();
    }

    @Override
    public void editUser(User request, StreamObserver<Empty> responseObserver) {
        logger.info("Starting editUser request for userId: {}", request.getUserId());

        UnifiedUserDTO restRequest = new UnifiedUserDTO(
                request.getUsername(),
                request.getPassword(),
                request.getUserRole().getNumber()
        );

        webClient.put()
                .uri("/{id}", request.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(restRequest)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> {
                    logger.info("User successfully updated: {}", request.getUserId());
                    responseObserver.onNext(Empty.getDefaultInstance());
                    responseObserver.onCompleted();
                })
                .doOnError(error -> {
                    logger.error("Failed to update user: ", error);
                    responseObserver.onError(new RuntimeException("Failed to update user: " + error.getMessage()));
                })
                .subscribe();
    }

    @Override
    public void deleteUser(DeleteUser request, StreamObserver<Empty> responseObserver) {
        logger.info("Starting deleteUser request for userId: {}", request.getUserId());

        webClient.delete()
                .uri("/{id}", request.getUserId())
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> {
                    logger.info("User successfully deleted: {}", request.getUserId());
                    responseObserver.onNext(Empty.getDefaultInstance());
                    responseObserver.onCompleted();
                })
                .doOnError(error -> {
                    logger.error("Failed to delete user: ", error);
                    responseObserver.onError(new RuntimeException("Failed to delete user: " + error.getMessage()));
                })
                .subscribe();
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserList> responseObserver) {
        logger.info("Starting getAllUsers request...");

        webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(UnifiedUserDTO.class)
                .map(userResponse -> GetUser.newBuilder()
                        .setUserId(userResponse.getUserId())
                        .setUserName(userResponse.getUserName())
                        .setUserRole(UserRole.forNumber(userResponse.getUserRole()))
                        .build())
                .collectList()
                .doOnSuccess(userDTOList -> {
                    UserList.Builder userListBuilder = UserList.newBuilder();
                    userDTOList.forEach(userListBuilder::addUsers);
                    responseObserver.onNext(userListBuilder.build());
                    responseObserver.onCompleted();
                })
                .doOnError(error -> {
                    logger.error("Failed to fetch users: ", error);
                    responseObserver.onError(new RuntimeException("Failed to fetch users: " + error.getMessage()));
                })
                .subscribe();
    }

    // Custom DTO for Unified User
    public static class UnifiedUserDTO {
        private int userId;
        private String userName;
        private String password;

        @JsonDeserialize(using = UserRoleDeserializer.class)
        private int userRole; // Role stays an INT, but the deserializer will convert from string

        public UnifiedUserDTO() {}

        public UnifiedUserDTO(String userName, String password, int userRole) {
            this.userName = userName;
            this.password = password;
            this.userRole = userRole;
        }

        public int getUserId() { return userId; }
        public String getUserName() { return userName; }
        public String getPassword() { return password; }
        public int getUserRole() { return userRole; }
    }

    // Custom Deserializer for UserRole
    public static class UserRoleDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String roleName = p.getText(); // Get the string from the JSON response
            switch (roleName) {
                case "INVENTORY_MANAGER":
                    return 0;
                case "WAREHOUSE_WORKER":
                    return 1;
                default:
                    throw new IllegalArgumentException("Unknown role: " + roleName);
            }
        }
    }
}
