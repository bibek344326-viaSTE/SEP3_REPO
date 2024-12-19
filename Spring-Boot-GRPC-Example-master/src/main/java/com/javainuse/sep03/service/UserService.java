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
    public void getAllUsers(Empty request, StreamObserver<UserList> responseObserver) {
        logger.info("Starting getAllUsers request...");

        webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(responseBody -> logger.info("Raw API Response: {}", responseBody))
                .flatMapMany(responseBody -> {
                    try {
                        logger.info("Parsing JSON response into List<UserResponse>...");
                        ObjectMapper mapper = new ObjectMapper();
                        List<UserResponse> userResponses = mapper.readValue(responseBody, new TypeReference<List<UserResponse>>() {});
                        logger.info("Successfully parsed {} users from the API response.", userResponses.size());
                        return Flux.fromIterable(userResponses);
                    } catch (Exception e) {
                        logger.error("Failed to parse JSON into UserResponse: ", e);
                        return Flux.error(new RuntimeException("Failed to parse JSON: " + e.getMessage()));
                    }
                })
                .<GetUserDTO>handle((userResponse, sink) -> {
                    try {
                        logger.info("Mapping UserResponse to GetUserDTO: {}", userResponse);

                        UserRole grpcRole;
                        if (userResponse.getUserRole() != null) {
                            try {
                                grpcRole = UserRole.valueOf(userResponse.getUserRole().name());
                            } catch (Exception e) {
                                logger.error("Failed to map userRole for userId {}. UserRole from REST: {}", userResponse.getUserId(), userResponse.getUserRole(), e);
                                return;
                            }
                        } else {
                            logger.error("userRole is NULL for userId {}.", userResponse.getUserId());
                            return;
                        }

                        GetUserDTO userDTO = GetUserDTO.newBuilder()
                                .setUserId(userResponse.getUserId())
                                .setUserName(userResponse.getUserName())
                                .setUserRole(grpcRole)
                                .setIsActive(userResponse.isActive())
                                .build();

                        logger.info("Mapped GetUserDTO: userId: {}, userName: {}, userRole: {}, isActive: {}",
                                userDTO.getUserId(),
                                userDTO.getUserName(),
                                userDTO.getUserRole(),
                                userDTO.getIsActive());

                        sink.next(userDTO);
                    } catch (Exception e) {
                        logger.error("Failed to map UserResponse to GetUserDTO for userId {}: {}", userResponse.getUserId(), e.getMessage());
                        sink.error(e);
                    }
                })
                .collectList()
                .doOnSuccess(userDTOList -> {
                    UserList.Builder userListBuilder = UserList.newBuilder();
                    userDTOList.forEach(userListBuilder::addUsers);
                    UserList userList = userListBuilder.build();
                    logger.info("UserList successfully created with {} users.", userDTOList.size());
                    responseObserver.onNext(userList);
                    responseObserver.onCompleted();
                })
                .doOnError(error -> {
                    logger.error("Failed to fetch users: ", error);
                    responseObserver.onError(new RuntimeException("Failed to fetch users: " + error.getMessage()));
                })
                .subscribe();
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

        public int getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public UserRole getUserRole() {
            return userRole;
        }

        public boolean isActive() {
            return isActive;
        }
    }
}
