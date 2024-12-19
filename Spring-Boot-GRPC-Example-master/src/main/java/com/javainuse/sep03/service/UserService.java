package com.javainuse.sep03.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Empty;
import com.javainuse.user.*;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {


    WebClient webClient = WebClient.builder()
            .baseUrl("https://localhost:7211")
            .defaultHeader("Accept", "application/json")
            .defaultHeader("Content-Type", "application/json")
            .clientConnector(
                    new ReactorClientHttpConnector(
                            HttpClient.create().secure(sslContextSpec ->
                                    sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
                            )
                    )
            )
            .build();



    @Override
    public void addUser(UserCreateDTO request, StreamObserver<GetUserDTO> responseObserver) {
        webClient.post()
                .uri("/")
                .bodyValue(new UserCreateDto(request.getUserName(), request.getPassword(), request.getUserRole().name()))
                .retrieve()
                .bodyToMono(GetUserDTO.class)
                .doOnSuccess(responseObserver::onNext)
                .doOnError(error -> responseObserver.onError(new RuntimeException("Failed to add user: " + error.getMessage())))
                .doFinally(signal -> responseObserver.onCompleted())
                .subscribe();
    }

    @Override
    public void editUser(User request, StreamObserver<Empty> responseObserver) {
        webClient.put()
                .uri("/{id}", request.getUserid())
                .bodyValue(new UserDto(request.getUserid(), request.getUsername(), request.getPassword(), request.getUserRole().name(), request.getIsActive()))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> responseObserver.onNext(Empty.getDefaultInstance()))
                .doOnError(error -> responseObserver.onError(new RuntimeException("Failed to update user: " + error.getMessage())))
                .doFinally(signal -> responseObserver.onCompleted())
                .subscribe();
    }

    @Override
    public void deleteUser(User request, StreamObserver<Empty> responseObserver) {
        webClient.delete()
                .uri("/{id}", request.getUserid())
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> responseObserver.onNext(Empty.getDefaultInstance()))
                .doOnError(error -> responseObserver.onError(new RuntimeException("Failed to delete user: " + error.getMessage())))
                .doFinally(signal -> responseObserver.onCompleted())
                .subscribe();
    }
    @Override
    public void getAllUsers(Empty request, StreamObserver<UserList> responseObserver) {
        System.out.println("Calling URL: http://localhost:5203/Users");

        String rawResponse = webClient.get()
                .uri("/Users/")
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        response -> {
                            System.out.println("Failed with status: " + response.statusCode());
                            return response.bodyToMono(String.class).flatMap(body -> {
                                System.out.println("Error body: " + body);
                                return Mono.error(new RuntimeException("API returned status: " + response.statusCode()));
                            });
                        }
                )
                .bodyToMono(String.class)
                .doOnNext(rawResponseStr -> System.out.println("Raw API Response: " + rawResponseStr))
                .block();

        System.out.println("Final Raw Response: " + rawResponse);
        UserList.Builder builder = UserList.newBuilder();
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }





    // DTO classes used in WebClient
    private static class UserCreateDto {
        private String userName;
        private String password;
        private String userRole;

        public UserCreateDto(String userName, String password, String userRole) {
            this.userName = userName;
            this.password = password;
            this.userRole = userRole;
        }

        // Getters and Setters
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
    }

    private static class UserDto {
        private String userid;
        private String username;
        private String password;
        private String userRole;
        private boolean isActive;

        public UserDto(String userid, String username, String password, String userRole, boolean isActive) {
            this.userid = userid;
            this.username = username;
            this.password = password;
            this.userRole = userRole;
            this.isActive = isActive;
        }

        // Getters and Setters
        public String getUserid() { return userid; }
        public void setUserid(String userid) { this.userid = userid; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
        public boolean getIsActive() { return isActive; }
        public void setIsActive(boolean isActive) { this.isActive = isActive; }
    }
}
