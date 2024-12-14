package com.javainuse.sep03.service;

import com.javainuse.item.ItemServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ItemService extends ItemServiceGrpc.ItemServiceImplBase {
}
