package com.javainuse.sep03.service;

import com.javainuse.user.UserServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
}
