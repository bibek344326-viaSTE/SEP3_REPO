package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.javainuse.user.UserServiceGrpc;
import com.javainuse.user.User;
import com.javainuse.user.UserDTO;
import com.javainuse.user.UserList;
import com.javainuse.user.Role;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

// REST DTO classes need to match your REST API
class UerDTO {
    private String userName;
    private String password;
    private UserRole userRole;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }
}

enum UserRole {
    INVENTORY_MANAGER,
    WAREHOUSE_WORKER
}

class RestUser {
    private int userId;
    private String userName;
    private String password;
    private UserRole userRole;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }
}

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:5203/Users")
            .build();

    @Override
    public void addUser(UserDTO request, StreamObserver<User> responseObserver) {
        UerDTO dto = new UerDTO();
        dto.setUserName(request.getUsername());
        dto.setPassword(request.getPassword());
        dto.setUserRole(convertRole(request.getRole()));

        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(RestUser.class)
                .subscribe(
                        restUser -> {
                            User response = convertToGrpcUser(restUser);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        throwable -> responseObserver.onError(throwable)
                );
    }

    @Override
    public void editUser(User request, StreamObserver<Empty> responseObserver) {
        // We assume the userId is a numeric value stored as string
        int userId = Integer.parseInt(request.getUserid());
        RestUser restUser = new RestUser();
        restUser.setUserId(userId);
        restUser.setUserName(request.getUsername());
        restUser.setPassword(request.getPassword());
        restUser.setUserRole(convertRole(request.getRole()));

        webClient.put()
                .uri("/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(restUser)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        emptyResponse -> {
                            responseObserver.onNext(Empty.getDefaultInstance());
                            responseObserver.onCompleted();
                        },
                        throwable -> responseObserver.onError(throwable)
                );
    }

    @Override
    public void deleteUser(User request, StreamObserver<Empty> responseObserver) {
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
                        throwable -> responseObserver.onError(throwable)
                );
    }

    @Override
    public void getAllUsers(com.google.protobuf.Empty request, StreamObserver<UserList> responseObserver) {
        System.out.println("Starting getAllUsers request...");

        webClient.get()
                .retrieve()
                .bodyToFlux(RestUser.class)
                .collectList()
                .subscribe(
                        restUsers -> {
                            try {
                                if (restUsers == null) {
                                    System.err.println("REST API returned a null user list");
                                    responseObserver.onError(new NullPointerException("REST API returned null user list"));
                                    return;
                                }

                                System.out.println("Successfully received REST response. Number of users: " + restUsers.size());
                                UserList.Builder listBuilder = UserList.newBuilder();
                                for (RestUser ru : restUsers) {
                                    if (ru == null) {
                                        System.err.println("Encountered null RestUser object in the list");
                                        continue;
                                    }
                                    System.out.println("Converting RestUser: " + ru.toString());

                                    // Check for null properties on RestUser before accessing them
                                    if (ru.getUserName() == null) {
                                        System.err.println("RestUser with userId " + ru.getUserId() + " has a null username");
                                    }
                                    if (ru.getPassword() == null) {
                                        System.err.println("RestUser with userId " + ru.getUserId() + " has a null password");
                                    }
                                    if (ru.getUserRole() == null) {
                                        System.err.println("RestUser with userId " + ru.getUserId() + " has a null userRole");
                                    }

                                    try {
                                        User grpcUser = convertToGrpcUser(ru);
                                        listBuilder.addUsers(grpcUser);
                                    } catch (Exception e) {
                                        System.err.println("Error converting RestUser to gRPC User: " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                                UserList userList = listBuilder.build();
                                System.out.println("Successfully built UserList with " + userList.getUsersCount() + " users.");
                                responseObserver.onNext(userList);
                                responseObserver.onCompleted();
                            } catch (Exception e) {
                                System.err.println("Error during processing user list: " + e.getMessage());
                                e.printStackTrace();
                                responseObserver.onError(e);
                            }
                        },
                        throwable -> {
                            System.err.println("Error occurred while calling REST API: " + throwable.getMessage());
                            throwable.printStackTrace(); // Log full error stack trace for debugging
                            responseObserver.onError(throwable);
                        }
                );
    }


    // Helper method to convert REST UserRole to gRPC Role
    private Role convertRole(UserRole userRole) {
        switch (userRole) {
            case INVENTORY_MANAGER:
                return Role.INVENTORY_MANAGER;
            case WAREHOUSE_WORKER:
                return Role.WAREHOUSE_WORKER;
            default:
                return Role.INVENTORY_MANAGER; // default fallback
        }
    }

    // Helper method to convert gRPC Role to REST UserRole
    private UserRole convertRole(Role role) {
        switch (role) {
            case INVENTORY_MANAGER:
                return UserRole.INVENTORY_MANAGER;
            case WAREHOUSE_WORKER:
                return UserRole.WAREHOUSE_WORKER;
            default:
                return UserRole.INVENTORY_MANAGER; // default fallback
        }
    }

    // Convert REST user to gRPC user
    private User convertToGrpcUser(RestUser ru) {
        if (ru == null) {
            System.err.println("RestUser is null. Cannot convert to gRPC User.");
            throw new NullPointerException("RestUser is null.");
        }

        System.out.println("Starting conversion of RestUser to gRPC User for RestUser with ID: " + ru.getUserId());

        String userId = String.valueOf(ru.getUserId());
        if (userId == null) {
            System.err.println("RestUser has a null userId");
        } else {
            System.out.println("UserId: " + userId);
        }

        String userName = ru.getUserName();
        if (userName == null) {
            System.err.println("RestUser has a null userName");
        } else {
            System.out.println("Username: " + userName);
        }

        String password = ru.getPassword();
        if (password == null) {
            System.err.println("RestUser has a null password");
        } else {
            System.out.println("Password: " + password);
        }

        UserRole userRole = ru.getUserRole();
        if (userRole == null) {
            System.err.println("RestUser has a null role");
        } else {
            System.out.println("UserRole: " + userRole);
        }

        try {
            User user = User.newBuilder()
                    .setUserid(userId != null ? userId : "UNKNOWN_USER_ID")
                    .setUsername(userName != null ? userName : "UNKNOWN_USER_NAME")
                    .setPassword(password != null ? password : "UNKNOWN_PASSWORD")
                    .setRole(userRole != null ? convertRole(userRole) : Role.INVENTORY_MANAGER) // Default to INVENTORY_MANAGER
                    .build();

            System.out.println("Successfully converted RestUser to gRPC User: " + user);
            return user;
        } catch (Exception e) {
            System.err.println("Exception occurred while building gRPC User: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}
