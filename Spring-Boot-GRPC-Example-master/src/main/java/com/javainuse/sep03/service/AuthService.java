package com.javainuse.sep03.service;

import com.javainuse.authentication.AuthServiceGrpc;
import com.javainuse.authentication.LoginRequest;
import com.javainuse.authentication.LoginResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.nio.charset.StandardCharsets;

@GrpcService
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    private final HttpClient client = HttpClients.createDefault();

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Login attempt: Username - " + request.getUsername() + " Password - " + request.getPassword());

        try {
            HttpPost httpPost = new HttpPost("http://localhost:5203/api/auth/login");
            String json = "{\"UserName\":\"" + request.getUsername() + "\", \"Password\":\"" + request.getPassword() + "\"}";
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            LoginResponse response = client.execute(httpPost, httpResponse -> {
                System.out.println("Status line from REST API: " + httpResponse.getReasonPhrase());
                System.out.println("Status code from REST API: " + httpResponse.getCode());

                System.out.println("Headers from REST API:");
                for (Header header : httpResponse.getHeaders()) {
                    System.out.println(header.getName() + ": " + header.getValue());
                }

                byte[] responseBytes = httpResponse.getEntity().getContent().readAllBytes();
                String responseString = new String(responseBytes, StandardCharsets.UTF_8);
                System.out.println("Response body from REST API: " + responseString);

                if (httpResponse.getCode() == 200) {
                    // Extract the token from the response string
                    String token = extractTokenFromResponse(responseString);

                    System.out.println("Extracted Token: " + token);

                    return LoginResponse.newBuilder()
                            .setToken(token)
                            .build();
                } else {
                    throw new RuntimeException("Failed to log in: " + httpResponse.getCode());
                }
            });

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    /**
     * Extracts the token from the response JSON.
     * Example response: {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxOSIsInVuaXF1ZV9uYW1lIjoiYWRtaW4iLCJyb2xlIjoiSU5WRU5UT1JZX01BTkFHRVIiLCJqdGkiOiJlMzM5N2UwNS0yOTM0LTQ0ODMtYWFiMy03ODA0ZTM1N2EwYjMiLCJuYmYiOjE3MzQyNDkwODQsImV4cCI6MTczNDI1MjY4NCwiaWF0IjoxNzM0MjQ5MDg0fQ.7JuGwezrmbMgTfECJRaw2xKcimHJegPxPyKwR6HVM7c"}
     */
    private String extractTokenFromResponse(String response) {
        try {
            int tokenStartIndex = response.indexOf("\"token\":\"") + 9; // Position after "token":"
            int tokenEndIndex = response.indexOf("\"", tokenStartIndex);
            if (tokenStartIndex != -1 && tokenEndIndex != -1) {
                return response.substring(tokenStartIndex, tokenEndIndex);
            }
            throw new RuntimeException("Failed to extract token from response");
        } catch (Exception e) {
            throw new RuntimeException("Error while extracting token from response: " + e.getMessage());
        }
    }
}
