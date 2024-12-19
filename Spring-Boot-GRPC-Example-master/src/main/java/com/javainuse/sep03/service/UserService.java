package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.javainuse.user.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

// This class represents the user object returned from the REST API
class UserDTO {
    private int userId;
    private String userName;
    private String password;
    private String userRole; // Change to String if the API returns the role as a string
    private boolean isActive;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "RestUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userRole='" + userRole + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:5203/Users")
            .build();


    @Override
    public void addUser(UserCreateDTO request, StreamObserver<GetUserDTO> responseObserver) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(request.getUserName());
        userDTO.setPassword(request.getPassword());
        userDTO.setUserRole(request.getUserRole().name());

        UserDTO createdUser = webClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();

        if (createdUser == null) {
            responseObserver.onError(new RuntimeException("Failed to create user"));
            return;
        }

        GetUserDTO grpcUser = convertToGrpcGetUserDTO(createdUser);
        responseObserver.onNext(grpcUser);
        responseObserver.onCompleted();
    }

    @Override
    public void editUser(User request, StreamObserver<Empty> responseObserver) {
        int userId = Integer.parseInt(request.getUserid());
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setUserName(request.getUsername());
        userDTO.setPassword(request.getPassword());
        userDTO.setUserRole(request.getUserRole().name());
        userDTO.setIsActive(request.getIsActive());

        webClient.put()
                .uri("/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        emptyResponse -> {
                            responseObserver.onNext(Empty.getDefaultInstance());
                            responseObserver.onCompleted();
                        },
                        responseObserver::onError
                );
    }

    @Override
    public void deleteUser(User request, StreamObserver<Empty> responseObserver) {
        try {
            int userId = Integer.parseInt(request.getUserid());
            webClient.delete()
                    .uri("/{id}", userId)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(
                            emptyResponse -> {
                                responseObserver.onNext(Empty.getDefaultInstance());
                                responseObserver.onCompleted();
                            },
                            responseObserver::onError
                    );
        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserList> responseObserver) {
        webClient.get()
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .collectList()
                .subscribe(
                        restUsers -> {
                            UserList.Builder listBuilder = UserList.newBuilder();
                            for (UserDTO ru : restUsers) {
                                GetUserDTO grpcUser = convertToGrpcGetUserDTO(ru);
                                listBuilder.addUsers(grpcUser);
                            }
                            responseObserver.onNext(listBuilder.build());
                            responseObserver.onCompleted();
                        },
                        responseObserver::onError
                );
    }
    
    private GetUserDTO convertToGrpcGetUserDTO(UserDTO ru) {
        if (ru == null) {
            throw new NullPointerException("RestUser is null.");
        }

        UserRole userRole = UserRole.valueOf(ru.getUserRole().toUpperCase());

        GetUserDTO grpcUser = GetUserDTO.newBuilder()
                .setUserId(String.valueOf(ru.getUserId()))
                .setUserName(ru.getUserName() != null ? ru.getUserName() : "UNKNOWN_USER_NAME")
                .setUserRole(userRole)
                .setIsActive(ru.getIsActive())
                .build();

        System.out.println("Successfully converted RestUser to gRPC User: " + grpcUser);
        return grpcUser;
    }
}
