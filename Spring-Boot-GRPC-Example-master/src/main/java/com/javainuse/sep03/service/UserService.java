package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.javainuse.user.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

 class UerDTO {

    // The field names here must match the expected JSON structure
    private String userName;

    private String password;

    private int userRole; // Assuming userRole is an integer (0, 1, 2, etc.)

    // Constructor
    public UerDTO() {}

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    // toString() for easy logging
    @Override
    public String toString() {
        return "UerDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}


class RestUser {
    private int userId;
    private String userName;
    private String password;
    private UserRole userRole;
    private boolean isActive;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }

    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "RestUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userRole=" + userRole +
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
    public void addUser(UserDTO request, StreamObserver<User> responseObserver) {

        UerDTO dto = new UerDTO();
        dto.setUserName(request.getUserName());
        dto.setPassword(request.getPassword());
        dto.setUserRole(request.getUserRoleValue());

        RestUser createdUser = webClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(RestUser.class)
                .block();
        if (createdUser == null) {
            responseObserver.onError(new RuntimeException("Failed to create item"));
            return;
        }

        com.javainuse.user.User grpcUser = com.javainuse.user.User.newBuilder()
                .setUserid(String.valueOf(createdUser.getUserId()))
                .setUsername(createdUser.getUserName())
                .setPassword(createdUser.getPassword())
                .setUserRole(createdUser.getUserRole())
                .setIsActive(createdUser.getIsActive())
                .build();

        responseObserver.onNext(grpcUser);
        responseObserver.onCompleted();

    }

    @Override
    public void editUser(User request, StreamObserver<Empty> responseObserver) {
        int userId = Integer.parseInt(request.getUserid());
        RestUser restUser = new RestUser();
        restUser.setUserId(userId);
        restUser.setUserName(request.getUsername());
        restUser.setPassword(request.getPassword());
        restUser.setUserRole(request.getUserRole());
        restUser.setIsActive(request.getIsActive());

        System.out.println("Editing user with ID: " + userId + ", Data: " + restUser);

        webClient.put()
                .uri("/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(restUser)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        emptyResponse -> {
                            System.out.println("User with ID " + userId + " edited successfully.");
                            responseObserver.onNext(Empty.getDefaultInstance());
                            responseObserver.onCompleted();
                        },
                        throwable -> {
                            System.err.println("Error while editing user: " + throwable.getMessage());
                            throwable.printStackTrace();
                            responseObserver.onError(throwable);
                        }
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
                            error -> {
                                responseObserver.onError(error);
                            }
                    );
        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserList> responseObserver) {
        webClient.get()
                .retrieve()
                .bodyToFlux(RestUser.class)
                .collectList()
                .subscribe(
                        restUsers -> {
                            try {
                                UserList.Builder listBuilder = UserList.newBuilder();
                                for (RestUser ru : restUsers) {
                                    User grpcUser = convertToGrpcUser(ru);
                                    listBuilder.addUsers(grpcUser);
                                }
                                responseObserver.onNext(listBuilder.build());
                                responseObserver.onCompleted();
                            } catch (Exception e) {
                                responseObserver.onError(e);
                            }
                        },
                        responseObserver::onError
                );
    }

    private User convertToGrpcUser(RestUser ru) {
        if (ru == null) {
            throw new NullPointerException("RestUser is null.");
        }

        User user = User.newBuilder()
                .setUserid(String.valueOf(ru.getUserId()))
                .setUsername(ru.getUserName() != null ? ru.getUserName() : "UNKNOWN_USER_NAME")
                .setPassword(ru.getPassword() != null ? ru.getPassword() : "UNKNOWN_PASSWORD")
                .setUserRole(ru.getUserRole() != null ? ru.getUserRole() : UserRole.INVENTORY_MANAGER)
                .setIsActive(ru.getIsActive())
                .build();

        System.out.println("Successfully converted RestUser to gRPC User: " + user);
        return user;
    }
}
