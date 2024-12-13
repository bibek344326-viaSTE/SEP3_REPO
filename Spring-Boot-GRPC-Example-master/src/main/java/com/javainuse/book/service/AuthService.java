package com.javainuse.book.service;

import com.javainuse.authentication.AuthServiceGrpc;
import com.javainuse.authentication.LoginRequest;
import com.javainuse.authentication.RegisterRequest;
import com.javainuse.authentication.UserDTO;
import com.javainuse.authentication.LoginResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.nio.charset.StandardCharsets;

@GrpcService
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    private final HttpClient client = HttpClients.createDefault();

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Login attempt: Username - " + request.getUsername());

        try {
            HttpPost httpPost = new HttpPost("http://localhost:5000/api/auth/login");
            String json = "{\"UserName\":\"" + request.getUsername() + "\", \"Password\":\"" + request.getPassword() + "\"}";
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            LoginResponse response = client.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                    String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                    // Extract token from responseString appropriately
                    return LoginResponse.newBuilder().setToken("extracted-token-here").build();
                } else {
                    throw new RuntimeException("Failed to log in: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<UserDTO> responseObserver) {
        try {
            HttpPost httpPost = new HttpPost("http://localhost:5000/api/auth/register");
            String json = "{\"UserName\":\"" + request.getUsername() + "\", \"Password\":\"" + request.getPassword() + "\", \"UserRole\":\"" + request.getRole() + "\"}";
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            UserDTO user = client.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    return UserDTO.newBuilder().setUsername(request.getUsername()).build();
                } else {
                    throw new RuntimeException("Registration failed: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(user);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
