// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: constants/util.proto

package com.javainuse.constants;

public final class Util {
  private Util() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\024constants/util.proto\022\tconstants*3\n\004Typ" +
      "e\022\013\n\007FANTASY\020\000\022\021\n\rAUTOBIOGRAPHY\020\001\022\013\n\007HIS" +
      "TORY\020\002*1\n\004Role\022\024\n\020InventoryManager\020\000\022\023\n\017" +
      "WarehouseWorker\020\001B\033\n\027com.javainuse.const" +
      "antsP\001b\006proto3"
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
  }

  // @@protoc_insertion_point(outer_class_scope)
}
