// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: order-service.proto

package com.javainuse.orders;

public interface OrderItemDTOOrBuilder extends
    // @@protoc_insertion_point(interface_extends:orders.OrderItemDTO)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.items.Item item = 1;</code>
   */
  boolean hasItem();
  /**
   * <code>.items.Item item = 1;</code>
   */
  com.javainuse.item.Item getItem();
  /**
   * <code>.items.Item item = 1;</code>
   */
  com.javainuse.item.ItemOrBuilder getItemOrBuilder();

  /**
   * <code>int32 quantity_to_pick = 2;</code>
   */
  int getQuantityToPick();
}
