// @generated
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: installer/proto/install.proto

package com.facebook.buck.install.model;

@javax.annotation.Generated(value="protoc", comments="annotations:FileResponseOrBuilder.java.pb.meta")
public interface FileResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:install.FileResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string install_id = 1;</code>
   */
  java.lang.String getInstallId();
  /**
   * <code>string install_id = 1;</code>
   */
  com.google.protobuf.ByteString
      getInstallIdBytes();

  /**
   * <code>string name = 2;</code>
   */
  java.lang.String getName();
  /**
   * <code>string name = 2;</code>
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>string path = 3;</code>
   */
  java.lang.String getPath();
  /**
   * <code>string path = 3;</code>
   */
  com.google.protobuf.ByteString
      getPathBytes();

  /**
   * <code>.install.ErrorDetail error_detail = 4;</code>
   */
  boolean hasErrorDetail();
  /**
   * <code>.install.ErrorDetail error_detail = 4;</code>
   */
  com.facebook.buck.install.model.ErrorDetail getErrorDetail();
  /**
   * <code>.install.ErrorDetail error_detail = 4;</code>
   */
  com.facebook.buck.install.model.ErrorDetailOrBuilder getErrorDetailOrBuilder();
}
