// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: order-service.proto

package com.javainuse.orders;

public interface OrderRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:OrderRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  java.util.List<com.javainuse.orders.OrderItem> 
      getOrderItemsList();
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  com.javainuse.orders.OrderItem getOrderItems(int index);
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  int getOrderItemsCount();
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  java.util.List<? extends com.javainuse.orders.OrderItemOrBuilder> 
      getOrderItemsOrBuilderList();
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  com.javainuse.orders.OrderItemOrBuilder getOrderItemsOrBuilder(
      int index);

  /**
   * <code>int32 user_id = 2;</code>
   */
  int getUserId();

  /**
   * <code>int32 createdBy = 3;</code>
   */
  int getCreatedBy();
}