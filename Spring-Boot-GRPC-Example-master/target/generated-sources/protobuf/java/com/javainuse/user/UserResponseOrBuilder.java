// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user-service.proto

package com.javainuse.user;

public interface UserResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:UserResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string userid = 1;</code>
   */
  java.lang.String getUserid();
  /**
   * <code>string userid = 1;</code>
   */
  com.google.protobuf.ByteString
      getUseridBytes();

  /**
   * <code>string username = 2;</code>
   */
  java.lang.String getUsername();
  /**
   * <code>string username = 2;</code>
   */
  com.google.protobuf.ByteString
      getUsernameBytes();

  /**
   * <pre>
   * Role is now an enum
   * </pre>
   *
   * <code>.Role role = 4;</code>
   */
  int getRoleValue();
  /**
   * <pre>
   * Role is now an enum
   * </pre>
   *
   * <code>.Role role = 4;</code>
   */
  com.javainuse.user.Role getRole();
}