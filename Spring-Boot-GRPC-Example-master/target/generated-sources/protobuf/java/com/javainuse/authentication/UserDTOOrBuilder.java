// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: auth-service.proto

package com.javainuse.authentication;

public interface UserDTOOrBuilder extends
    // @@protoc_insertion_point(interface_extends:UserDTO)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string username = 1;</code>
   */
  java.lang.String getUsername();
  /**
   * <code>string username = 1;</code>
   */
  com.google.protobuf.ByteString
      getUsernameBytes();

  /**
   * <code>string userid = 2;</code>
   */
  java.lang.String getUserid();
  /**
   * <code>string userid = 2;</code>
   */
  com.google.protobuf.ByteString
      getUseridBytes();

  /**
   * <code>.constants.Role role = 3;</code>
   */
  int getRoleValue();
  /**
   * <code>.constants.Role role = 3;</code>
   */
  com.javainuse.constants.Role getRole();
}