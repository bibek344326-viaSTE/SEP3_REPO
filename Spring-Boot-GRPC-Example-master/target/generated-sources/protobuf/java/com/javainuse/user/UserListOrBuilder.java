// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user-service.proto

package com.javainuse.user;

public interface UserListOrBuilder extends
    // @@protoc_insertion_point(interface_extends:UserList)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Contains a list of User objects
   * </pre>
   *
   * <code>repeated .User users = 1;</code>
   */
  java.util.List<com.javainuse.user.User> 
      getUsersList();
  /**
   * <pre>
   * Contains a list of User objects
   * </pre>
   *
   * <code>repeated .User users = 1;</code>
   */
  com.javainuse.user.User getUsers(int index);
  /**
   * <pre>
   * Contains a list of User objects
   * </pre>
   *
   * <code>repeated .User users = 1;</code>
   */
  int getUsersCount();
  /**
   * <pre>
   * Contains a list of User objects
   * </pre>
   *
   * <code>repeated .User users = 1;</code>
   */
  java.util.List<? extends com.javainuse.user.UserOrBuilder> 
      getUsersOrBuilderList();
  /**
   * <pre>
   * Contains a list of User objects
   * </pre>
   *
   * <code>repeated .User users = 1;</code>
   */
  com.javainuse.user.UserOrBuilder getUsersOrBuilder(
      int index);
}
