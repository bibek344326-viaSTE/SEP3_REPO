package com.javainuse.orders;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.22.1)",
    comments = "Source: order-service.proto")
public final class OrderServiceGrpc {

  private OrderServiceGrpc() {}

  public static final String SERVICE_NAME = "OrderService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.javainuse.orders.OrderRequest,
      com.javainuse.orders.Order> getCreateOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateOrder",
      requestType = com.javainuse.orders.OrderRequest.class,
      responseType = com.javainuse.orders.Order.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.javainuse.orders.OrderRequest,
      com.javainuse.orders.Order> getCreateOrderMethod() {
    io.grpc.MethodDescriptor<com.javainuse.orders.OrderRequest, com.javainuse.orders.Order> getCreateOrderMethod;
    if ((getCreateOrderMethod = OrderServiceGrpc.getCreateOrderMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getCreateOrderMethod = OrderServiceGrpc.getCreateOrderMethod) == null) {
          OrderServiceGrpc.getCreateOrderMethod = getCreateOrderMethod = 
              io.grpc.MethodDescriptor.<com.javainuse.orders.OrderRequest, com.javainuse.orders.Order>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "OrderService", "CreateOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.javainuse.orders.OrderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.javainuse.orders.Order.getDefaultInstance()))
                  .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("CreateOrder"))
                  .build();
          }
        }
     }
     return getCreateOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.javainuse.orders.OrderList> getGetAllOrdersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllOrders",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.javainuse.orders.OrderList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.javainuse.orders.OrderList> getGetAllOrdersMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.javainuse.orders.OrderList> getGetAllOrdersMethod;
    if ((getGetAllOrdersMethod = OrderServiceGrpc.getGetAllOrdersMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getGetAllOrdersMethod = OrderServiceGrpc.getGetAllOrdersMethod) == null) {
          OrderServiceGrpc.getGetAllOrdersMethod = getGetAllOrdersMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.javainuse.orders.OrderList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "OrderService", "GetAllOrders"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.javainuse.orders.OrderList.getDefaultInstance()))
                  .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("GetAllOrders"))
                  .build();
          }
        }
     }
     return getGetAllOrdersMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OrderServiceStub newStub(io.grpc.Channel channel) {
    return new OrderServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OrderServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new OrderServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OrderServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new OrderServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class OrderServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void createOrder(com.javainuse.orders.OrderRequest request,
        io.grpc.stub.StreamObserver<com.javainuse.orders.Order> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateOrderMethod(), responseObserver);
    }

    /**
     */
    public void getAllOrders(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.javainuse.orders.OrderList> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAllOrdersMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreateOrderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.javainuse.orders.OrderRequest,
                com.javainuse.orders.Order>(
                  this, METHODID_CREATE_ORDER)))
          .addMethod(
            getGetAllOrdersMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.javainuse.orders.OrderList>(
                  this, METHODID_GET_ALL_ORDERS)))
          .build();
    }
  }

  /**
   */
  public static final class OrderServiceStub extends io.grpc.stub.AbstractStub<OrderServiceStub> {
    private OrderServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OrderServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OrderServiceStub(channel, callOptions);
    }

    /**
     */
    public void createOrder(com.javainuse.orders.OrderRequest request,
        io.grpc.stub.StreamObserver<com.javainuse.orders.Order> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllOrders(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.javainuse.orders.OrderList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAllOrdersMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class OrderServiceBlockingStub extends io.grpc.stub.AbstractStub<OrderServiceBlockingStub> {
    private OrderServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OrderServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OrderServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.javainuse.orders.Order createOrder(com.javainuse.orders.OrderRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.javainuse.orders.OrderList getAllOrders(com.google.protobuf.Empty request) {
      return blockingUnaryCall(
          getChannel(), getGetAllOrdersMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class OrderServiceFutureStub extends io.grpc.stub.AbstractStub<OrderServiceFutureStub> {
    private OrderServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private OrderServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new OrderServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.javainuse.orders.Order> createOrder(
        com.javainuse.orders.OrderRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateOrderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.javainuse.orders.OrderList> getAllOrders(
        com.google.protobuf.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAllOrdersMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_ORDER = 0;
  private static final int METHODID_GET_ALL_ORDERS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final OrderServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(OrderServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_ORDER:
          serviceImpl.createOrder((com.javainuse.orders.OrderRequest) request,
              (io.grpc.stub.StreamObserver<com.javainuse.orders.Order>) responseObserver);
          break;
        case METHODID_GET_ALL_ORDERS:
          serviceImpl.getAllOrders((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.javainuse.orders.OrderList>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class OrderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OrderServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.javainuse.orders.OrderServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OrderService");
    }
  }

  private static final class OrderServiceFileDescriptorSupplier
      extends OrderServiceBaseDescriptorSupplier {
    OrderServiceFileDescriptorSupplier() {}
  }

  private static final class OrderServiceMethodDescriptorSupplier
      extends OrderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    OrderServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (OrderServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OrderServiceFileDescriptorSupplier())
              .addMethod(getCreateOrderMethod())
              .addMethod(getGetAllOrdersMethod())
              .build();
        }
      }
    }
    return result;
  }
}
