// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: item-service.proto

package com.javainuse.item;

public final class ItemServiceOuterClass {
  private ItemServiceOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ItemDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ItemDTO_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Item_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Item_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ItemResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ItemResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022item-service.proto\"G\n\007ItemDTO\022\014\n\004name\030" +
      "\001 \001(\t\022\023\n\013description\030\002 \001(\t\022\031\n\021quantity_i" +
      "n_store\030\003 \001(\005\"P\n\004Item\022\n\n\002id\030\001 \001(\t\022\014\n\004nam" +
      "e\030\002 \001(\t\022\023\n\013description\030\003 \001(\t\022\031\n\021quantity" +
      "_in_store\030\004 \001(\005\"\032\n\014ItemResponse\022\n\n\002id\030\001 " +
      "\001(\t2z\n\013ItemService\022%\n\ncreateItem\022\010.ItemD" +
      "TO\032\r.ItemResponse\022 \n\010editItem\022\005.Item\032\r.I" +
      "temResponse\022\"\n\ndeleteItem\022\005.Item\032\r.ItemR" +
      "esponseB\026\n\022com.javainuse.itemP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_ItemDTO_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ItemDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ItemDTO_descriptor,
        new java.lang.String[] { "Name", "Description", "QuantityInStore", });
    internal_static_Item_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_Item_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Item_descriptor,
        new java.lang.String[] { "Id", "Name", "Description", "QuantityInStore", });
    internal_static_ItemResponse_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_ItemResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ItemResponse_descriptor,
        new java.lang.String[] { "Id", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}