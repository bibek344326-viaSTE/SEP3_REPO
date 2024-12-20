package com.javainuse.sep03.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        RestUserCreateDTO restRequest = new RestUserCreateDTO(
                request.getUserName(),
                request.getPassword(),
                request.getUserRoleValue()
        );

        webClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(restRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnSuccess(userResponse -> {
                    GetUser userDTO = GetUser.newBuilder()
                            .setUserId(userResponse.getUserId())
                            .setUserName(userResponse.getUserName())
                            .setUserRole(userResponse.getUserRole())
                            .build();

                    logger.info("User successfully created: {}", userDTO);
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

        RestUserUpdateDTO restRequest = new RestUserUpdateDTO(
                request.getUserId(),
                request.getUsername(),
                request.getPassword(),
                request.getUserRoleValue()
        );

        webClient.put()
                .uri("/{id}", restRequest.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(restRequest)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> {
                    logger.info("User successfully updated: {}", restRequest.getUserId());
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
                .bodyToMono(String.class)
                .flatMapMany(responseBody -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<UserResponse> userResponses = mapper.readValue(responseBody, new TypeReference<List<UserResponse>>() {});
                        return Flux.fromIterable(userResponses);
                    } catch (Exception e) {
                        logger.error("Failed to parse JSON into UserResponse: ", e);
                        return Flux.error(new RuntimeException("Failed to parse JSON: " + e.getMessage()));
                    }
                })
                .map(userResponse -> {
                    return GetUser.newBuilder()
                            .setUserId(userResponse.getUserId())
                            .setUserName(userResponse.getUserName())
                            .setUserRole(userResponse.getUserRole())
                            .build();
                })
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

    public static class RestUserCreateDTO {
        private String userName;
        private String password;
        private int userRole;

        public RestUserCreateDTO(String userName, String password, int userRole) {
            this.userName = userName;
            this.password = password;
            this.userRole = userRole;
        }

        // Getter and Setter for userName
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        // Getter and Setter for password
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        // Getter and Setter for userRole
        public int getUserRole() {
            return userRole;
        }

        public void setUserRole(int userRole) {
            this.userRole = userRole;
        }
    }


    public static class RestUserUpdateDTO {
        private int userId;
        private String username;
        private String password;
        private int userRole;

        public RestUserUpdateDTO(int userId, String username, String password, int userRole) {
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.userRole = userRole;
        }

        // Getter and Setter for userId
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        // Getter and Setter for username
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        // Getter and Setter for password
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        // Getter and Setter for userRole
        public int getUserRole() {
            return userRole;
        }

        public void setUserRole(int userRole) {
            this.userRole = userRole;
        }
    }


    public static class UserResponse {
        @JsonProperty("userId")
        private int userId;

        @JsonProperty("userName")
        private String userName;

        @JsonProperty("userRole")
        private UserRole userRole;

        @JsonProperty("isActive")
        private boolean isActive;

        // Default constructor
        public UserResponse() {}

        // Getter and Setter for userId
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        // Getter and Setter for userName
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        // Getter and Setter for userRole
        public UserRole getUserRole() {
            return userRole;
        }

        public void setUserRole(UserRole userRole) {
            this.userRole = userRole;
        }

        // Getter and Setter for isActive
        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }


}
