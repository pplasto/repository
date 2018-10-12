// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ddz/ddzProto.proto

package protobuf.clazz.ddz;

public final class DdzRsp {
  private DdzRsp() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface DdzCallRspOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional int32 id = 1;
    /**
     * <code>optional int32 id = 1;</code>
     */
    boolean hasId();
    /**
     * <code>optional int32 id = 1;</code>
     */
    int getId();

    // optional int32 result = 2;
    /**
     * <code>optional int32 result = 2;</code>
     */
    boolean hasResult();
    /**
     * <code>optional int32 result = 2;</code>
     */
    int getResult();
  }
  /**
   * Protobuf type {@code ddz.DdzCallRsp}
   */
  public static final class DdzCallRsp extends
      com.google.protobuf.GeneratedMessage
      implements DdzCallRspOrBuilder {
    // Use DdzCallRsp.newBuilder() to construct.
    private DdzCallRsp(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private DdzCallRsp(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final DdzCallRsp defaultInstance;
    public static DdzCallRsp getDefaultInstance() {
      return defaultInstance;
    }

    public DdzCallRsp getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private DdzCallRsp(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              id_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              result_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallRsp_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallRsp_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protobuf.clazz.ddz.DdzRsp.DdzCallRsp.class, protobuf.clazz.ddz.DdzRsp.DdzCallRsp.Builder.class);
    }

    public static com.google.protobuf.Parser<DdzCallRsp> PARSER =
        new com.google.protobuf.AbstractParser<DdzCallRsp>() {
      public DdzCallRsp parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new DdzCallRsp(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<DdzCallRsp> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional int32 id = 1;
    public static final int ID_FIELD_NUMBER = 1;
    private int id_;
    /**
     * <code>optional int32 id = 1;</code>
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional int32 id = 1;</code>
     */
    public int getId() {
      return id_;
    }

    // optional int32 result = 2;
    public static final int RESULT_FIELD_NUMBER = 2;
    private int result_;
    /**
     * <code>optional int32 result = 2;</code>
     */
    public boolean hasResult() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 result = 2;</code>
     */
    public int getResult() {
      return result_;
    }

    private void initFields() {
      id_ = 0;
      result_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, result_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, result_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallRsp parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(protobuf.clazz.ddz.DdzRsp.DdzCallRsp prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ddz.DdzCallRsp}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements protobuf.clazz.ddz.DdzRsp.DdzCallRspOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallRsp_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallRsp_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protobuf.clazz.ddz.DdzRsp.DdzCallRsp.class, protobuf.clazz.ddz.DdzRsp.DdzCallRsp.Builder.class);
      }

      // Construct using protobuf.clazz.ddz.DdzRsp.DdzCallRsp.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        id_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        result_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallRsp_descriptor;
      }

      public protobuf.clazz.ddz.DdzRsp.DdzCallRsp getDefaultInstanceForType() {
        return protobuf.clazz.ddz.DdzRsp.DdzCallRsp.getDefaultInstance();
      }

      public protobuf.clazz.ddz.DdzRsp.DdzCallRsp build() {
        protobuf.clazz.ddz.DdzRsp.DdzCallRsp result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protobuf.clazz.ddz.DdzRsp.DdzCallRsp buildPartial() {
        protobuf.clazz.ddz.DdzRsp.DdzCallRsp result = new protobuf.clazz.ddz.DdzRsp.DdzCallRsp(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.result_ = result_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protobuf.clazz.ddz.DdzRsp.DdzCallRsp) {
          return mergeFrom((protobuf.clazz.ddz.DdzRsp.DdzCallRsp)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protobuf.clazz.ddz.DdzRsp.DdzCallRsp other) {
        if (other == protobuf.clazz.ddz.DdzRsp.DdzCallRsp.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasResult()) {
          setResult(other.getResult());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        protobuf.clazz.ddz.DdzRsp.DdzCallRsp parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protobuf.clazz.ddz.DdzRsp.DdzCallRsp) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional int32 id = 1;
      private int id_ ;
      /**
       * <code>optional int32 id = 1;</code>
       */
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public int getId() {
        return id_;
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public Builder setId(int value) {
        bitField0_ |= 0x00000001;
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0;
        onChanged();
        return this;
      }

      // optional int32 result = 2;
      private int result_ ;
      /**
       * <code>optional int32 result = 2;</code>
       */
      public boolean hasResult() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int32 result = 2;</code>
       */
      public int getResult() {
        return result_;
      }
      /**
       * <code>optional int32 result = 2;</code>
       */
      public Builder setResult(int value) {
        bitField0_ |= 0x00000002;
        result_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 result = 2;</code>
       */
      public Builder clearResult() {
        bitField0_ = (bitField0_ & ~0x00000002);
        result_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:ddz.DdzCallRsp)
    }

    static {
      defaultInstance = new DdzCallRsp(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:ddz.DdzCallRsp)
  }

  public interface DdzCallReqOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional int32 id = 1;
    /**
     * <code>optional int32 id = 1;</code>
     */
    boolean hasId();
    /**
     * <code>optional int32 id = 1;</code>
     */
    int getId();

    // optional int32 result = 2;
    /**
     * <code>optional int32 result = 2;</code>
     */
    boolean hasResult();
    /**
     * <code>optional int32 result = 2;</code>
     */
    int getResult();
  }
  /**
   * Protobuf type {@code ddz.DdzCallReq}
   */
  public static final class DdzCallReq extends
      com.google.protobuf.GeneratedMessage
      implements DdzCallReqOrBuilder {
    // Use DdzCallReq.newBuilder() to construct.
    private DdzCallReq(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private DdzCallReq(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final DdzCallReq defaultInstance;
    public static DdzCallReq getDefaultInstance() {
      return defaultInstance;
    }

    public DdzCallReq getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private DdzCallReq(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              id_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              result_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallReq_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallReq_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protobuf.clazz.ddz.DdzRsp.DdzCallReq.class, protobuf.clazz.ddz.DdzRsp.DdzCallReq.Builder.class);
    }

    public static com.google.protobuf.Parser<DdzCallReq> PARSER =
        new com.google.protobuf.AbstractParser<DdzCallReq>() {
      public DdzCallReq parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new DdzCallReq(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<DdzCallReq> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional int32 id = 1;
    public static final int ID_FIELD_NUMBER = 1;
    private int id_;
    /**
     * <code>optional int32 id = 1;</code>
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional int32 id = 1;</code>
     */
    public int getId() {
      return id_;
    }

    // optional int32 result = 2;
    public static final int RESULT_FIELD_NUMBER = 2;
    private int result_;
    /**
     * <code>optional int32 result = 2;</code>
     */
    public boolean hasResult() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 result = 2;</code>
     */
    public int getResult() {
      return result_;
    }

    private void initFields() {
      id_ = 0;
      result_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, result_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, result_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf.clazz.ddz.DdzRsp.DdzCallReq parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(protobuf.clazz.ddz.DdzRsp.DdzCallReq prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ddz.DdzCallReq}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements protobuf.clazz.ddz.DdzRsp.DdzCallReqOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallReq_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallReq_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protobuf.clazz.ddz.DdzRsp.DdzCallReq.class, protobuf.clazz.ddz.DdzRsp.DdzCallReq.Builder.class);
      }

      // Construct using protobuf.clazz.ddz.DdzRsp.DdzCallReq.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        id_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        result_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protobuf.clazz.ddz.DdzRsp.internal_static_ddz_DdzCallReq_descriptor;
      }

      public protobuf.clazz.ddz.DdzRsp.DdzCallReq getDefaultInstanceForType() {
        return protobuf.clazz.ddz.DdzRsp.DdzCallReq.getDefaultInstance();
      }

      public protobuf.clazz.ddz.DdzRsp.DdzCallReq build() {
        protobuf.clazz.ddz.DdzRsp.DdzCallReq result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protobuf.clazz.ddz.DdzRsp.DdzCallReq buildPartial() {
        protobuf.clazz.ddz.DdzRsp.DdzCallReq result = new protobuf.clazz.ddz.DdzRsp.DdzCallReq(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.result_ = result_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protobuf.clazz.ddz.DdzRsp.DdzCallReq) {
          return mergeFrom((protobuf.clazz.ddz.DdzRsp.DdzCallReq)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protobuf.clazz.ddz.DdzRsp.DdzCallReq other) {
        if (other == protobuf.clazz.ddz.DdzRsp.DdzCallReq.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasResult()) {
          setResult(other.getResult());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        protobuf.clazz.ddz.DdzRsp.DdzCallReq parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protobuf.clazz.ddz.DdzRsp.DdzCallReq) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional int32 id = 1;
      private int id_ ;
      /**
       * <code>optional int32 id = 1;</code>
       */
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public int getId() {
        return id_;
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public Builder setId(int value) {
        bitField0_ |= 0x00000001;
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0;
        onChanged();
        return this;
      }

      // optional int32 result = 2;
      private int result_ ;
      /**
       * <code>optional int32 result = 2;</code>
       */
      public boolean hasResult() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int32 result = 2;</code>
       */
      public int getResult() {
        return result_;
      }
      /**
       * <code>optional int32 result = 2;</code>
       */
      public Builder setResult(int value) {
        bitField0_ |= 0x00000002;
        result_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 result = 2;</code>
       */
      public Builder clearResult() {
        bitField0_ = (bitField0_ & ~0x00000002);
        result_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:ddz.DdzCallReq)
    }

    static {
      defaultInstance = new DdzCallReq(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:ddz.DdzCallReq)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ddz_DdzCallRsp_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ddz_DdzCallRsp_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ddz_DdzCallReq_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ddz_DdzCallReq_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022ddz/ddzProto.proto\022\003ddz\"(\n\nDdzCallRsp\022" +
      "\n\n\002id\030\001 \001(\005\022\016\n\006result\030\002 \001(\005\"(\n\nDdzCallRe" +
      "q\022\n\n\002id\030\001 \001(\005\022\016\n\006result\030\002 \001(\005B\034\n\022protobu" +
      "f.clazz.ddzB\006DdzRsp"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ddz_DdzCallRsp_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ddz_DdzCallRsp_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ddz_DdzCallRsp_descriptor,
              new java.lang.String[] { "Id", "Result", });
          internal_static_ddz_DdzCallReq_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_ddz_DdzCallReq_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ddz_DdzCallReq_descriptor,
              new java.lang.String[] { "Id", "Result", });
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
