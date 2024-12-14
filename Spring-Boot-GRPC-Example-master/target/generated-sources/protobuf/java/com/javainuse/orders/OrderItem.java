// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: order-service.proto

package com.javainuse.orders;

/**
 * Protobuf type {@code OrderItem}
 */
public  final class OrderItem extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:OrderItem)
    OrderItemOrBuilder {
private static final long serialVersionUID = 0L;
  // Use OrderItem.newBuilder() to construct.
  private OrderItem(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private OrderItem() {
    orderItemId_ = 0;
    itemId_ = 0;
    quantityToPick_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private OrderItem(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            orderItemId_ = input.readInt32();
            break;
          }
          case 16: {

            itemId_ = input.readInt32();
            break;
          }
          case 24: {

            quantityToPick_ = input.readInt32();
            break;
          }
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderItem_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderItem_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.javainuse.orders.OrderItem.class, com.javainuse.orders.OrderItem.Builder.class);
  }

  public static final int ORDER_ITEM_ID_FIELD_NUMBER = 1;
  private int orderItemId_;
  /**
   * <code>int32 order_item_id = 1;</code>
   */
  public int getOrderItemId() {
    return orderItemId_;
  }

  public static final int ITEM_ID_FIELD_NUMBER = 2;
  private int itemId_;
  /**
   * <code>int32 item_id = 2;</code>
   */
  public int getItemId() {
    return itemId_;
  }

  public static final int QUANTITY_TO_PICK_FIELD_NUMBER = 3;
  private int quantityToPick_;
  /**
   * <code>int32 quantity_to_pick = 3;</code>
   */
  public int getQuantityToPick() {
    return quantityToPick_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (orderItemId_ != 0) {
      output.writeInt32(1, orderItemId_);
    }
    if (itemId_ != 0) {
      output.writeInt32(2, itemId_);
    }
    if (quantityToPick_ != 0) {
      output.writeInt32(3, quantityToPick_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (orderItemId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, orderItemId_);
    }
    if (itemId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, itemId_);
    }
    if (quantityToPick_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, quantityToPick_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.javainuse.orders.OrderItem)) {
      return super.equals(obj);
    }
    com.javainuse.orders.OrderItem other = (com.javainuse.orders.OrderItem) obj;

    boolean result = true;
    result = result && (getOrderItemId()
        == other.getOrderItemId());
    result = result && (getItemId()
        == other.getItemId());
    result = result && (getQuantityToPick()
        == other.getQuantityToPick());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ORDER_ITEM_ID_FIELD_NUMBER;
    hash = (53 * hash) + getOrderItemId();
    hash = (37 * hash) + ITEM_ID_FIELD_NUMBER;
    hash = (53 * hash) + getItemId();
    hash = (37 * hash) + QUANTITY_TO_PICK_FIELD_NUMBER;
    hash = (53 * hash) + getQuantityToPick();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.javainuse.orders.OrderItem parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.javainuse.orders.OrderItem parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.javainuse.orders.OrderItem parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.javainuse.orders.OrderItem parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.javainuse.orders.OrderItem parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.javainuse.orders.OrderItem parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.javainuse.orders.OrderItem parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.javainuse.orders.OrderItem parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.javainuse.orders.OrderItem parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.javainuse.orders.OrderItem parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.javainuse.orders.OrderItem parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.javainuse.orders.OrderItem parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.javainuse.orders.OrderItem prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code OrderItem}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:OrderItem)
      com.javainuse.orders.OrderItemOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderItem_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderItem_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.javainuse.orders.OrderItem.class, com.javainuse.orders.OrderItem.Builder.class);
    }

    // Construct using com.javainuse.orders.OrderItem.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      orderItemId_ = 0;

      itemId_ = 0;

      quantityToPick_ = 0;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderItem_descriptor;
    }

    @java.lang.Override
    public com.javainuse.orders.OrderItem getDefaultInstanceForType() {
      return com.javainuse.orders.OrderItem.getDefaultInstance();
    }

    @java.lang.Override
    public com.javainuse.orders.OrderItem build() {
      com.javainuse.orders.OrderItem result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.javainuse.orders.OrderItem buildPartial() {
      com.javainuse.orders.OrderItem result = new com.javainuse.orders.OrderItem(this);
      result.orderItemId_ = orderItemId_;
      result.itemId_ = itemId_;
      result.quantityToPick_ = quantityToPick_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return (Builder) super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.javainuse.orders.OrderItem) {
        return mergeFrom((com.javainuse.orders.OrderItem)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.javainuse.orders.OrderItem other) {
      if (other == com.javainuse.orders.OrderItem.getDefaultInstance()) return this;
      if (other.getOrderItemId() != 0) {
        setOrderItemId(other.getOrderItemId());
      }
      if (other.getItemId() != 0) {
        setItemId(other.getItemId());
      }
      if (other.getQuantityToPick() != 0) {
        setQuantityToPick(other.getQuantityToPick());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.javainuse.orders.OrderItem parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.javainuse.orders.OrderItem) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int orderItemId_ ;
    /**
     * <code>int32 order_item_id = 1;</code>
     */
    public int getOrderItemId() {
      return orderItemId_;
    }
    /**
     * <code>int32 order_item_id = 1;</code>
     */
    public Builder setOrderItemId(int value) {
      
      orderItemId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 order_item_id = 1;</code>
     */
    public Builder clearOrderItemId() {
      
      orderItemId_ = 0;
      onChanged();
      return this;
    }

    private int itemId_ ;
    /**
     * <code>int32 item_id = 2;</code>
     */
    public int getItemId() {
      return itemId_;
    }
    /**
     * <code>int32 item_id = 2;</code>
     */
    public Builder setItemId(int value) {
      
      itemId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 item_id = 2;</code>
     */
    public Builder clearItemId() {
      
      itemId_ = 0;
      onChanged();
      return this;
    }

    private int quantityToPick_ ;
    /**
     * <code>int32 quantity_to_pick = 3;</code>
     */
    public int getQuantityToPick() {
      return quantityToPick_;
    }
    /**
     * <code>int32 quantity_to_pick = 3;</code>
     */
    public Builder setQuantityToPick(int value) {
      
      quantityToPick_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 quantity_to_pick = 3;</code>
     */
    public Builder clearQuantityToPick() {
      
      quantityToPick_ = 0;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:OrderItem)
  }

  // @@protoc_insertion_point(class_scope:OrderItem)
  private static final com.javainuse.orders.OrderItem DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.javainuse.orders.OrderItem();
  }

  public static com.javainuse.orders.OrderItem getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<OrderItem>
      PARSER = new com.google.protobuf.AbstractParser<OrderItem>() {
    @java.lang.Override
    public OrderItem parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new OrderItem(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<OrderItem> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<OrderItem> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.javainuse.orders.OrderItem getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

