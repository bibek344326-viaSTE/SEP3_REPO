package com.javainuse.sep03.service;

import com.javainuse.orders.OrderServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {
}
