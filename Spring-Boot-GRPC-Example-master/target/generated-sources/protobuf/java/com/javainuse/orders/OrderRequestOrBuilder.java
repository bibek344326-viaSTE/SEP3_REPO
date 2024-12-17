// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: order-service.proto

package com.javainuse.orders;

public interface OrderRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:orders.OrderRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .orders.OrderItemDTO order_items = 1;</code>
   */
  java.util.List<com.javainuse.orders.OrderItemDTO> 
      getOrderItemsList();
  /**
   * <code>repeated .orders.OrderItemDTO order_items = 1;</code>
   */
  com.javainuse.orders.OrderItemDTO getOrderItems(int index);
  /**
   * <code>repeated .orders.OrderItemDTO order_items = 1;</code>
   */
  int getOrderItemsCount();
  /**
   * <code>repeated .orders.OrderItemDTO order_items = 1;</code>
   */
  java.util.List<? extends com.javainuse.orders.OrderItemDTOOrBuilder> 
      getOrderItemsOrBuilderList();
  /**
   * <code>repeated .orders.OrderItemDTO order_items = 1;</code>
   */
  com.javainuse.orders.OrderItemDTOOrBuilder getOrderItemsOrBuilder(
      int index);

  /**
   * <code>.google.protobuf.Timestamp delivery_date = 2;</code>
   */
  boolean hasDeliveryDate();
  /**
   * <code>.google.protobuf.Timestamp delivery_date = 2;</code>
   */
  com.google.protobuf.Timestamp getDeliveryDate();
  /**
   * <code>.google.protobuf.Timestamp delivery_date = 2;</code>
   */
  com.google.protobuf.TimestampOrBuilder getDeliveryDateOrBuilder();

  /**
   * <code>int32 created_by = 3;</code>
   */
  int getCreatedBy();
}
