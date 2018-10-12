// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basic/common.proto

package protobuf.clazz;

public final class Common {
  private Common() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PairIntOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional int32 v1 = 1;
    /**
     * <code>optional int32 v1 = 1;</code>
     */
    boolean hasV1();
    /**
     * <code>optional int32 v1 = 1;</code>
     */
    int getV1();

    // optional int32 v2 = 2;
    /**
     * <code>optional int32 v2 = 2;</code>
     */
    boolean hasV2();
    /**
     * <code>optional int32 v2 = 2;</code>
     */
    int getV2();
  }
  /**
   * Protobuf type {@code PairInt}
   */
  public static final class PairInt extends
      com.google.protobuf.GeneratedMessage
      implements PairIntOrBuilder {
    // Use PairInt.newBuilder() to construct.
    private PairInt(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private PairInt(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final PairInt defaultInstance;
    public static PairInt getDefaultInstance() {
      return defaultInstance;
    }

    public PairInt getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private PairInt(
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
              v1_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              v2_ = input.readInt32();
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
      return protobuf.clazz.Common.internal_static_PairInt_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protobuf.clazz.Common.internal_static_PairInt_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protobuf.clazz.Common.PairInt.class, protobuf.clazz.Common.PairInt.Builder.class);
    }

    public static com.google.protobuf.Parser<PairInt> PARSER =
        new com.google.protobuf.AbstractParser<PairInt>() {
      public PairInt parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new PairInt(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<PairInt> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional int32 v1 = 1;
    public static final int V1_FIELD_NUMBER = 1;
    private int v1_;
    /**
     * <code>optional int32 v1 = 1;</code>
     */
    public boolean hasV1() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional int32 v1 = 1;</code>
     */
    public int getV1() {
      return v1_;
    }

    // optional int32 v2 = 2;
    public static final int V2_FIELD_NUMBER = 2;
    private int v2_;
    /**
     * <code>optional int32 v2 = 2;</code>
     */
    public boolean hasV2() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 v2 = 2;</code>
     */
    public int getV2() {
      return v2_;
    }

    private void initFields() {
      v1_ = 0;
      v2_ = 0;
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
        output.writeInt32(1, v1_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, v2_);
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
          .computeInt32Size(1, v1_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, v2_);
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

    public static protobuf.clazz.Common.PairInt parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf.clazz.Common.PairInt parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf.clazz.Common.PairInt parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf.clazz.Common.PairInt parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf.clazz.Common.PairInt parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf.clazz.Common.PairInt parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static protobuf.clazz.Common.PairInt parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static protobuf.clazz.Common.PairInt parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static protobuf.clazz.Common.PairInt parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf.clazz.Common.PairInt parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(protobuf.clazz.Common.PairInt prototype) {
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
     * Protobuf type {@code PairInt}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements protobuf.clazz.Common.PairIntOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protobuf.clazz.Common.internal_static_PairInt_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protobuf.clazz.Common.internal_static_PairInt_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protobuf.clazz.Common.PairInt.class, protobuf.clazz.Common.PairInt.Builder.class);
      }

      // Construct using protobuf.clazz.Common.PairInt.newBuilder()
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
        v1_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        v2_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protobuf.clazz.Common.internal_static_PairInt_descriptor;
      }

      public protobuf.clazz.Common.PairInt getDefaultInstanceForType() {
        return protobuf.clazz.Common.PairInt.getDefaultInstance();
      }

      public protobuf.clazz.Common.PairInt build() {
        protobuf.clazz.Common.PairInt result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protobuf.clazz.Common.PairInt buildPartial() {
        protobuf.clazz.Common.PairInt result = new protobuf.clazz.Common.PairInt(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.v1_ = v1_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.v2_ = v2_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protobuf.clazz.Common.PairInt) {
          return mergeFrom((protobuf.clazz.Common.PairInt)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protobuf.clazz.Common.PairInt other) {
        if (other == protobuf.clazz.Common.PairInt.getDefaultInstance()) return this;
        if (other.hasV1()) {
          setV1(other.getV1());
        }
        if (other.hasV2()) {
          setV2(other.getV2());
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
        protobuf.clazz.Common.PairInt parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protobuf.clazz.Common.PairInt) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional int32 v1 = 1;
      private int v1_ ;
      /**
       * <code>optional int32 v1 = 1;</code>
       */
      public boolean hasV1() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional int32 v1 = 1;</code>
       */
      public int getV1() {
        return v1_;
      }
      /**
       * <code>optional int32 v1 = 1;</code>
       */
      public Builder setV1(int value) {
        bitField0_ |= 0x00000001;
        v1_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 v1 = 1;</code>
       */
      public Builder clearV1() {
        bitField0_ = (bitField0_ & ~0x00000001);
        v1_ = 0;
        onChanged();
        return this;
      }

      // optional int32 v2 = 2;
      private int v2_ ;
      /**
       * <code>optional int32 v2 = 2;</code>
       */
      public boolean hasV2() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int32 v2 = 2;</code>
       */
      public int getV2() {
        return v2_;
      }
      /**
       * <code>optional int32 v2 = 2;</code>
       */
      public Builder setV2(int value) {
        bitField0_ |= 0x00000002;
        v2_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 v2 = 2;</code>
       */
      public Builder clearV2() {
        bitField0_ = (bitField0_ & ~0x00000002);
        v2_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:PairInt)
    }

    static {
      defaultInstance = new PairInt(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:PairInt)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_PairInt_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PairInt_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022basic/common.proto\"!\n\007PairInt\022\n\n\002v1\030\001 " +
      "\001(\005\022\n\n\002v2\030\002 \001(\005B\030\n\016protobuf.clazzB\006Commo" +
      "n"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_PairInt_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_PairInt_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_PairInt_descriptor,
              new java.lang.String[] { "V1", "V2", });
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
