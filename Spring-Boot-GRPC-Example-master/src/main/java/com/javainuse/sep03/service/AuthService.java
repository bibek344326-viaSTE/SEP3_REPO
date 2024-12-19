package com.javainuse.sep03.service;

import com.javainuse.authentication.AuthServiceGrpc;
import com.javainuse.authentication.LoginRequest;
import com.javainuse.authentication.LoginResponse;
import io.grpc.stub.StreamObserver;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

@GrpcService
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    WebClient webClient = WebClient.builder()
            .baseUrl("https://localhost:7211/api/auth")
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().secure(sslContextSpec ->
                            sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
                    )
            ))
            .build();

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Login attempt: Username - " + request.getUsername() + " Password - " + request.getPassword());

        Map<String, String> requestBody = Map.of(
                "username", request.getUsername(),
                "password", request.getPassword()
        );

        try {
            webClient.post()
                    .uri("/login")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class) // Assuming the response is a String JSON response
                    .doOnNext(responseString -> {
                        System.out.println("Response body from REST API: " + responseString);
                    })
                    .map(this::extractTokenFromResponse) // Extract the token from the response
                    .doOnNext(token -> {
                        System.out.println("Extracted Token: " + token);
                    })
                    .doOnError(throwable -> {
                        System.err.println("Error during login request: " + throwable.getMessage());
                        responseObserver.onError(throwable);
                    })
                    .subscribe(token -> {
                        LoginResponse response = LoginResponse.newBuilder()
                                .setToken(token)
                                .build();

                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    });
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
