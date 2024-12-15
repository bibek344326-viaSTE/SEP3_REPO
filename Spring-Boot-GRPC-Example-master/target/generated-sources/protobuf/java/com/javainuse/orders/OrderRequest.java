// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: order-service.proto

package com.javainuse.orders;

/**
 * Protobuf type {@code OrderRequest}
 */
public  final class OrderRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:OrderRequest)
    OrderRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use OrderRequest.newBuilder() to construct.
  private OrderRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private OrderRequest() {
    orderItems_ = java.util.Collections.emptyList();
    userId_ = 0;
    createdBy_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private OrderRequest(
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
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              orderItems_ = new java.util.ArrayList<com.javainuse.orders.OrderItem>();
              mutable_bitField0_ |= 0x00000001;
            }
            orderItems_.add(
                input.readMessage(com.javainuse.orders.OrderItem.parser(), extensionRegistry));
            break;
          }
          case 16: {

            userId_ = input.readInt32();
            break;
          }
          case 24: {

            createdBy_ = input.readInt32();
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
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        orderItems_ = java.util.Collections.unmodifiableList(orderItems_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.javainuse.orders.OrderRequest.class, com.javainuse.orders.OrderRequest.Builder.class);
  }

  private int bitField0_;
  public static final int ORDER_ITEMS_FIELD_NUMBER = 1;
  private java.util.List<com.javainuse.orders.OrderItem> orderItems_;
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  public java.util.List<com.javainuse.orders.OrderItem> getOrderItemsList() {
    return orderItems_;
  }
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  public java.util.List<? extends com.javainuse.orders.OrderItemOrBuilder> 
      getOrderItemsOrBuilderList() {
    return orderItems_;
  }
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  public int getOrderItemsCount() {
    return orderItems_.size();
  }
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  public com.javainuse.orders.OrderItem getOrderItems(int index) {
    return orderItems_.get(index);
  }
  /**
   * <code>repeated .OrderItem order_items = 1;</code>
   */
  public com.javainuse.orders.OrderItemOrBuilder getOrderItemsOrBuilder(
      int index) {
    return orderItems_.get(index);
  }

  public static final int USER_ID_FIELD_NUMBER = 2;
  private int userId_;
  /**
   * <code>int32 user_id = 2;</code>
   */
  public int getUserId() {
    return userId_;
  }

  public static final int CREATEDBY_FIELD_NUMBER = 3;
  private int createdBy_;
  /**
   * <code>int32 createdBy = 3;</code>
   */
  public int getCreatedBy() {
    return createdBy_;
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
    for (int i = 0; i < orderItems_.size(); i++) {
      output.writeMessage(1, orderItems_.get(i));
    }
    if (userId_ != 0) {
      output.writeInt32(2, userId_);
    }
    if (createdBy_ != 0) {
      output.writeInt32(3, createdBy_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < orderItems_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, orderItems_.get(i));
    }
    if (userId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, userId_);
    }
    if (createdBy_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, createdBy_);
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
    if (!(obj instanceof com.javainuse.orders.OrderRequest)) {
      return super.equals(obj);
    }
    com.javainuse.orders.OrderRequest other = (com.javainuse.orders.OrderRequest) obj;

    boolean result = true;
    result = result && getOrderItemsList()
        .equals(other.getOrderItemsList());
    result = result && (getUserId()
        == other.getUserId());
    result = result && (getCreatedBy()
        == other.getCreatedBy());
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
    if (getOrderItemsCount() > 0) {
      hash = (37 * hash) + ORDER_ITEMS_FIELD_NUMBER;
      hash = (53 * hash) + getOrderItemsList().hashCode();
    }
    hash = (37 * hash) + USER_ID_FIELD_NUMBER;
    hash = (53 * hash) + getUserId();
    hash = (37 * hash) + CREATEDBY_FIELD_NUMBER;
    hash = (53 * hash) + getCreatedBy();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.javainuse.orders.OrderRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.javainuse.orders.OrderRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.javainuse.orders.OrderRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.javainuse.orders.OrderRequest parseFrom(
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
  public static Builder newBuilder(com.javainuse.orders.OrderRequest prototype) {
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
   * Protobuf type {@code OrderRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:OrderRequest)
      com.javainuse.orders.OrderRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.javainuse.orders.OrderRequest.class, com.javainuse.orders.OrderRequest.Builder.class);
    }

    // Construct using com.javainuse.orders.OrderRequest.newBuilder()
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
        getOrderItemsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (orderItemsBuilder_ == null) {
        orderItems_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        orderItemsBuilder_.clear();
      }
      userId_ = 0;

      createdBy_ = 0;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.javainuse.orders.OrderServiceOuterClass.internal_static_OrderRequest_descriptor;
    }

    @java.lang.Override
    public com.javainuse.orders.OrderRequest getDefaultInstanceForType() {
      return com.javainuse.orders.OrderRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.javainuse.orders.OrderRequest build() {
      com.javainuse.orders.OrderRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.javainuse.orders.OrderRequest buildPartial() {
      com.javainuse.orders.OrderRequest result = new com.javainuse.orders.OrderRequest(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (orderItemsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          orderItems_ = java.util.Collections.unmodifiableList(orderItems_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.orderItems_ = orderItems_;
      } else {
        result.orderItems_ = orderItemsBuilder_.build();
      }
      result.userId_ = userId_;
      result.createdBy_ = createdBy_;
      result.bitField0_ = to_bitField0_;
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
      if (other instanceof com.javainuse.orders.OrderRequest) {
        return mergeFrom((com.javainuse.orders.OrderRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.javainuse.orders.OrderRequest other) {
      if (other == com.javainuse.orders.OrderRequest.getDefaultInstance()) return this;
      if (orderItemsBuilder_ == null) {
        if (!other.orderItems_.isEmpty()) {
          if (orderItems_.isEmpty()) {
            orderItems_ = other.orderItems_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureOrderItemsIsMutable();
            orderItems_.addAll(other.orderItems_);
          }
          onChanged();
        }
      } else {
        if (!other.orderItems_.isEmpty()) {
          if (orderItemsBuilder_.isEmpty()) {
            orderItemsBuilder_.dispose();
            orderItemsBuilder_ = null;
            orderItems_ = other.orderItems_;
            bitField0_ = (bitField0_ & ~0x00000001);
            orderItemsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getOrderItemsFieldBuilder() : null;
          } else {
            orderItemsBuilder_.addAllMessages(other.orderItems_);
          }
        }
      }
      if (other.getUserId() != 0) {
        setUserId(other.getUserId());
      }
      if (other.getCreatedBy() != 0) {
        setCreatedBy(other.getCreatedBy());
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
      com.javainuse.orders.OrderRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.javainuse.orders.OrderRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<com.javainuse.orders.OrderItem> orderItems_ =
      java.util.Collections.emptyList();
    private void ensureOrderItemsIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        orderItems_ = new java.util.ArrayList<com.javainuse.orders.OrderItem>(orderItems_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.javainuse.orders.OrderItem, com.javainuse.orders.OrderItem.Builder, com.javainuse.orders.OrderItemOrBuilder> orderItemsBuilder_;

    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public java.util.List<com.javainuse.orders.OrderItem> getOrderItemsList() {
      if (orderItemsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(orderItems_);
      } else {
        return orderItemsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public int getOrderItemsCount() {
      if (orderItemsBuilder_ == null) {
        return orderItems_.size();
      } else {
        return orderItemsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public com.javainuse.orders.OrderItem getOrderItems(int index) {
      if (orderItemsBuilder_ == null) {
        return orderItems_.get(index);
      } else {
        return orderItemsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder setOrderItems(
        int index, com.javainuse.orders.OrderItem value) {
      if (orderItemsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureOrderItemsIsMutable();
        orderItems_.set(index, value);
        onChanged();
      } else {
        orderItemsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder setOrderItems(
        int index, com.javainuse.orders.OrderItem.Builder builderForValue) {
      if (orderItemsBuilder_ == null) {
        ensureOrderItemsIsMutable();
        orderItems_.set(index, builderForValue.build());
        onChanged();
      } else {
        orderItemsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder addOrderItems(com.javainuse.orders.OrderItem value) {
      if (orderItemsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureOrderItemsIsMutable();
        orderItems_.add(value);
        onChanged();
      } else {
        orderItemsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder addOrderItems(
        int index, com.javainuse.orders.OrderItem value) {
      if (orderItemsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureOrderItemsIsMutable();
        orderItems_.add(index, value);
        onChanged();
      } else {
        orderItemsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder addOrderItems(
        com.javainuse.orders.OrderItem.Builder builderForValue) {
      if (orderItemsBuilder_ == null) {
        ensureOrderItemsIsMutable();
        orderItems_.add(builderForValue.build());
        onChanged();
      } else {
        orderItemsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder addOrderItems(
        int index, com.javainuse.orders.OrderItem.Builder builderForValue) {
      if (orderItemsBuilder_ == null) {
        ensureOrderItemsIsMutable();
        orderItems_.add(index, builderForValue.build());
        onChanged();
      } else {
        orderItemsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder addAllOrderItems(
        java.lang.Iterable<? extends com.javainuse.orders.OrderItem> values) {
      if (orderItemsBuilder_ == null) {
        ensureOrderItemsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, orderItems_);
        onChanged();
      } else {
        orderItemsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder clearOrderItems() {
      if (orderItemsBuilder_ == null) {
        orderItems_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        orderItemsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public Builder removeOrderItems(int index) {
      if (orderItemsBuilder_ == null) {
        ensureOrderItemsIsMutable();
        orderItems_.remove(index);
        onChanged();
      } else {
        orderItemsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public com.javainuse.orders.OrderItem.Builder getOrderItemsBuilder(
        int index) {
      return getOrderItemsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public com.javainuse.orders.OrderItemOrBuilder getOrderItemsOrBuilder(
        int index) {
      if (orderItemsBuilder_ == null) {
        return orderItems_.get(index);  } else {
        return orderItemsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public java.util.List<? extends com.javainuse.orders.OrderItemOrBuilder> 
         getOrderItemsOrBuilderList() {
      if (orderItemsBuilder_ != null) {
        return orderItemsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(orderItems_);
      }
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public com.javainuse.orders.OrderItem.Builder addOrderItemsBuilder() {
      return getOrderItemsFieldBuilder().addBuilder(
          com.javainuse.orders.OrderItem.getDefaultInstance());
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public com.javainuse.orders.OrderItem.Builder addOrderItemsBuilder(
        int index) {
      return getOrderItemsFieldBuilder().addBuilder(
          index, com.javainuse.orders.OrderItem.getDefaultInstance());
    }
    /**
     * <code>repeated .OrderItem order_items = 1;</code>
     */
    public java.util.List<com.javainuse.orders.OrderItem.Builder> 
         getOrderItemsBuilderList() {
      return getOrderItemsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.javainuse.orders.OrderItem, com.javainuse.orders.OrderItem.Builder, com.javainuse.orders.OrderItemOrBuilder> 
        getOrderItemsFieldBuilder() {
      if (orderItemsBuilder_ == null) {
        orderItemsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.javainuse.orders.OrderItem, com.javainuse.orders.OrderItem.Builder, com.javainuse.orders.OrderItemOrBuilder>(
                orderItems_,
                ((bitField0_ & 0x00000001) == 0x00000001),
                getParentForChildren(),
                isClean());
        orderItems_ = null;
      }
      return orderItemsBuilder_;
    }

    private int userId_ ;
    /**
     * <code>int32 user_id = 2;</code>
     */
    public int getUserId() {
      return userId_;
    }
    /**
     * <code>int32 user_id = 2;</code>
     */
    public Builder setUserId(int value) {
      
      userId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 user_id = 2;</code>
     */
    public Builder clearUserId() {
      
      userId_ = 0;
      onChanged();
      return this;
    }

    private int createdBy_ ;
    /**
     * <code>int32 createdBy = 3;</code>
     */
    public int getCreatedBy() {
      return createdBy_;
    }
    /**
     * <code>int32 createdBy = 3;</code>
     */
    public Builder setCreatedBy(int value) {
      
      createdBy_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 createdBy = 3;</code>
     */
    public Builder clearCreatedBy() {
      
      createdBy_ = 0;
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


    // @@protoc_insertion_point(builder_scope:OrderRequest)
  }

  // @@protoc_insertion_point(class_scope:OrderRequest)
  private static final com.javainuse.orders.OrderRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.javainuse.orders.OrderRequest();
  }

  public static com.javainuse.orders.OrderRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<OrderRequest>
      PARSER = new com.google.protobuf.AbstractParser<OrderRequest>() {
    @java.lang.Override
    public OrderRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new OrderRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<OrderRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<OrderRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.javainuse.orders.OrderRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
