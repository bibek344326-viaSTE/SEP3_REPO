// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user-service.proto

package com.javainuse.user;

public final class UserServiceOuterClass {
  private UserServiceOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_UserRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_UserRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_EditResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_EditResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_DeleteResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_DeleteResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022user-service.proto\032\024constants/util.pro" +
      "to\"=\n\013UserRequest\022\016\n\006userid\030\001 \001(\t\022\020\n\010use" +
      "rname\030\002 \001(\t\022\014\n\004role\030\003 \001(\t\"\037\n\014EditRespons" +
      "e\022\017\n\007success\030\001 \001(\010\"!\n\016DeleteResponse\022\017\n\007" +
      "success\030\001 \001(\0102e\n\013UserService\022)\n\010editUser" +
      "\022\014.UserRequest\032\017.DeleteResponse\022+\n\ndelet" +
      "eUser\022\014.UserRequest\032\017.DeleteResponseB\026\n\022" +
      "com.javainuse.userP\001b\006proto3"
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
          com.javainuse.constants.Util.getDescriptor(),
        }, assigner);
    internal_static_UserRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_UserRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_UserRequest_descriptor,
        new java.lang.String[] { "Userid", "Username", "Role", });
    internal_static_EditResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_EditResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_EditResponse_descriptor,
        new java.lang.String[] { "Success", });
    internal_static_DeleteResponse_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_DeleteResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_DeleteResponse_descriptor,
        new java.lang.String[] { "Success", });
    com.javainuse.constants.Util.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
