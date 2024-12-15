package com.javainuse.sep03.service;

import com.google.protobuf.Empty;
import com.javainuse.user.User;
import com.javainuse.user.UserDTO;
import com.javainuse.user.UserList;
import com.javainuse.user.UserResponse;
import com.javainuse.user.UserServiceGrpc;
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
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final HttpClient client = HttpClients.createDefault();

    private final String baseUrl = "http://localhost:5203/api/users";

    @Override
    public void addUser(UserDTO request, StreamObserver<UserResponse> responseObserver) {
        try {
            HttpPost httpPost = new HttpPost(baseUrl);
            String json = "{" +
                    "\"UserName\":\"" + request.getUsername() + "\", " +
                    "\"Password\":\"" + request.getPassword() + "\", " +
                    "\"UserRole\":\"" + request.getRole().name() + "\"" +
                    "}";
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            UserResponse response = client.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 201) {
                    byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    // Extract user details from the response
                    return UserResponse.newBuilder()
                            .setUserid("extracted-user-id") // Replace with actual extracted user ID
                            .setUsername(request.getUsername())
                            .setRole(request.getRole())
                            .build();
                } else {
                    throw new RuntimeException("Failed to add user: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void editUser(User request, StreamObserver<Empty> responseObserver) {
        try {
            HttpPut httpPut = new HttpPut(baseUrl + "/" + request.getUserid());
            String json = "{" +
                    "\"UserName\":\"" + request.getUsername() + "\", " +
                    "\"Password\":\"" + request.getPassword() + "\", " +
                    "\"UserRole\":\"" + request.getRole().name() + "\"" +
                    "}";
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            client.execute(httpPut, httpResponse -> {
                if (httpResponse.getCode() == 204) {
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                    return null;
                } else {
                    throw new RuntimeException("Failed to edit user: " + httpResponse.getCode());
                }
            });
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void deleteUser(User request, StreamObserver<Empty> responseObserver) {
        try {
            HttpDelete httpDelete = new HttpDelete(baseUrl + "/" + request.getUserid());

            client.execute(httpDelete, httpResponse -> {
                if (httpResponse.getCode() == 204) {
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                    return null;
                } else {
                    throw new RuntimeException("Failed to delete user: " + httpResponse.getCode());
                }
            });
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserList> responseObserver) {
        try {
            HttpGet httpGet = new HttpGet(baseUrl);

            UserList response = client.execute(httpGet, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    // Extract user list from responseString
                    UserList.Builder userListBuilder = UserList.newBuilder();
                    // Populate the userListBuilder with users extracted from the response
                    return userListBuilder.build();
                } else {
                    throw new RuntimeException("Failed to get users: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
