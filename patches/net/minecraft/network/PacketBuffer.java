package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ByteProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class PacketBuffer extends ByteBuf {
   private final ByteBuf buf;

   public PacketBuffer(ByteBuf var1) {
      this.buf = ☃;
   }

   public static int getVarIntSize(int var0) {
      for (int ☃ = 1; ☃ < 5; ☃++) {
         if ((☃ & -1 << ☃ * 7) == 0) {
            return ☃;
         }
      }

      return 5;
   }

   public PacketBuffer writeByteArray(byte[] var1) {
      this.writeVarInt(☃.length);
      this.writeBytes(☃);
      return this;
   }

   public byte[] readByteArray() {
      return this.readByteArray(this.readableBytes());
   }

   public byte[] readByteArray(int var1) {
      int ☃ = this.readVarInt();
      if (☃ > ☃) {
         throw new DecoderException("ByteArray with size " + ☃ + " is bigger than allowed " + ☃);
      } else {
         byte[] ☃x = new byte[☃];
         this.readBytes(☃x);
         return ☃x;
      }
   }

   public PacketBuffer writeVarIntArray(int[] var1) {
      this.writeVarInt(☃.length);

      for (int ☃ : ☃) {
         this.writeVarInt(☃);
      }

      return this;
   }

   public int[] readVarIntArray() {
      return this.readVarIntArray(this.readableBytes());
   }

   public int[] readVarIntArray(int var1) {
      int ☃ = this.readVarInt();
      if (☃ > ☃) {
         throw new DecoderException("VarIntArray with size " + ☃ + " is bigger than allowed " + ☃);
      } else {
         int[] ☃x = new int[☃];

         for (int ☃xx = 0; ☃xx < ☃x.length; ☃xx++) {
            ☃x[☃xx] = this.readVarInt();
         }

         return ☃x;
      }
   }

   public PacketBuffer writeLongArray(long[] var1) {
      this.writeVarInt(☃.length);

      for (long ☃ : ☃) {
         this.writeLong(☃);
      }

      return this;
   }

   public long[] readLongArray(@Nullable long[] var1) {
      return this.readLongArray(☃, this.readableBytes() / 8);
   }

   public long[] readLongArray(@Nullable long[] var1, int var2) {
      int ☃ = this.readVarInt();
      if (☃ == null || ☃.length != ☃) {
         if (☃ > ☃) {
            throw new DecoderException("LongArray with size " + ☃ + " is bigger than allowed " + ☃);
         }

         ☃ = new long[☃];
      }

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         ☃[☃x] = this.readLong();
      }

      return ☃;
   }

   public BlockPos readBlockPos() {
      return BlockPos.fromLong(this.readLong());
   }

   public PacketBuffer writeBlockPos(BlockPos var1) {
      this.writeLong(☃.toLong());
      return this;
   }

   public ITextComponent readTextComponent() {
      return ITextComponent.Serializer.jsonToComponent(this.readString(32767));
   }

   public PacketBuffer writeTextComponent(ITextComponent var1) {
      return this.writeString(ITextComponent.Serializer.componentToJson(☃));
   }

   public <T extends Enum<T>> T readEnumValue(Class<T> var1) {
      return ☃.getEnumConstants()[this.readVarInt()];
   }

   public PacketBuffer writeEnumValue(Enum<?> var1) {
      return this.writeVarInt(☃.ordinal());
   }

   public int readVarInt() {
      int ☃ = 0;
      int ☃x = 0;

      byte ☃xx;
      do {
         ☃xx = this.readByte();
         ☃ |= (☃xx & 127) << ☃x++ * 7;
         if (☃x > 5) {
            throw new RuntimeException("VarInt too big");
         }
      } while ((☃xx & 128) == 128);

      return ☃;
   }

   public long readVarLong() {
      long ☃ = 0L;
      int ☃x = 0;

      byte ☃xx;
      do {
         ☃xx = this.readByte();
         ☃ |= (long)(☃xx & 127) << ☃x++ * 7;
         if (☃x > 10) {
            throw new RuntimeException("VarLong too big");
         }
      } while ((☃xx & 128) == 128);

      return ☃;
   }

   public PacketBuffer writeUniqueId(UUID var1) {
      this.writeLong(☃.getMostSignificantBits());
      this.writeLong(☃.getLeastSignificantBits());
      return this;
   }

   public UUID readUniqueId() {
      return new UUID(this.readLong(), this.readLong());
   }

   public PacketBuffer writeVarInt(int var1) {
      while ((☃ & -128) != 0) {
         this.writeByte(☃ & 127 | 128);
         ☃ >>>= 7;
      }

      this.writeByte(☃);
      return this;
   }

   public PacketBuffer writeVarLong(long var1) {
      while ((☃ & -128L) != 0L) {
         this.writeByte((int)(☃ & 127L) | 128);
         ☃ >>>= 7;
      }

      this.writeByte((int)☃);
      return this;
   }

   public PacketBuffer writeCompoundTag(@Nullable NBTTagCompound var1) {
      if (☃ == null) {
         this.writeByte(0);
      } else {
         try {
            CompressedStreamTools.write(☃, new ByteBufOutputStream(this));
         } catch (IOException var3) {
            throw new EncoderException(var3);
         }
      }

      return this;
   }

   @Nullable
   public NBTTagCompound readCompoundTag() {
      int ☃ = this.readerIndex();
      byte ☃x = this.readByte();
      if (☃x == 0) {
         return null;
      } else {
         this.readerIndex(☃);

         try {
            return CompressedStreamTools.read(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
         } catch (IOException var4) {
            throw new EncoderException(var4);
         }
      }
   }

   public PacketBuffer writeItemStack(ItemStack var1) {
      if (☃.isEmpty()) {
         this.writeShort(-1);
      } else {
         this.writeShort(Item.getIdFromItem(☃.getItem()));
         this.writeByte(☃.getCount());
         this.writeShort(☃.getMetadata());
         NBTTagCompound ☃ = null;
         if (☃.getItem().isDamageable() || ☃.getItem().getShareTag()) {
            ☃ = ☃.getTagCompound();
         }

         this.writeCompoundTag(☃);
      }

      return this;
   }

   public ItemStack readItemStack() {
      int ☃ = this.readShort();
      if (☃ < 0) {
         return ItemStack.EMPTY;
      } else {
         int ☃x = this.readByte();
         int ☃xx = this.readShort();
         ItemStack ☃xxx = new ItemStack(Item.getItemById(☃), ☃x, ☃xx);
         ☃xxx.setTagCompound(this.readCompoundTag());
         return ☃xxx;
      }
   }

   public String readString(int var1) {
      int ☃ = this.readVarInt();
      if (☃ > ☃ * 4) {
         throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + ☃ + " > " + ☃ * 4 + ")");
      } else if (☃ < 0) {
         throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
      } else {
         String ☃x = this.toString(this.readerIndex(), ☃, StandardCharsets.UTF_8);
         this.readerIndex(this.readerIndex() + ☃);
         if (☃x.length() > ☃) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + ☃ + " > " + ☃ + ")");
         } else {
            return ☃x;
         }
      }
   }

   public PacketBuffer writeString(String var1) {
      byte[] ☃ = ☃.getBytes(StandardCharsets.UTF_8);
      if (☃.length > 32767) {
         throw new EncoderException("String too big (was " + ☃.length + " bytes encoded, max " + 32767 + ")");
      } else {
         this.writeVarInt(☃.length);
         this.writeBytes(☃);
         return this;
      }
   }

   public ResourceLocation readResourceLocation() {
      return new ResourceLocation(this.readString(32767));
   }

   public PacketBuffer writeResourceLocation(ResourceLocation var1) {
      this.writeString(☃.toString());
      return this;
   }

   public Date readTime() {
      return new Date(this.readLong());
   }

   public PacketBuffer writeTime(Date var1) {
      this.writeLong(☃.getTime());
      return this;
   }

   public int capacity() {
      return this.buf.capacity();
   }

   public ByteBuf capacity(int var1) {
      return this.buf.capacity(☃);
   }

   public int maxCapacity() {
      return this.buf.maxCapacity();
   }

   public ByteBufAllocator alloc() {
      return this.buf.alloc();
   }

   public ByteOrder order() {
      return this.buf.order();
   }

   public ByteBuf order(ByteOrder var1) {
      return this.buf.order(☃);
   }

   public ByteBuf unwrap() {
      return this.buf.unwrap();
   }

   public boolean isDirect() {
      return this.buf.isDirect();
   }

   public boolean isReadOnly() {
      return this.buf.isReadOnly();
   }

   public ByteBuf asReadOnly() {
      return this.buf.asReadOnly();
   }

   public int readerIndex() {
      return this.buf.readerIndex();
   }

   public ByteBuf readerIndex(int var1) {
      return this.buf.readerIndex(☃);
   }

   public int writerIndex() {
      return this.buf.writerIndex();
   }

   public ByteBuf writerIndex(int var1) {
      return this.buf.writerIndex(☃);
   }

   public ByteBuf setIndex(int var1, int var2) {
      return this.buf.setIndex(☃, ☃);
   }

   public int readableBytes() {
      return this.buf.readableBytes();
   }

   public int writableBytes() {
      return this.buf.writableBytes();
   }

   public int maxWritableBytes() {
      return this.buf.maxWritableBytes();
   }

   public boolean isReadable() {
      return this.buf.isReadable();
   }

   public boolean isReadable(int var1) {
      return this.buf.isReadable(☃);
   }

   public boolean isWritable() {
      return this.buf.isWritable();
   }

   public boolean isWritable(int var1) {
      return this.buf.isWritable(☃);
   }

   public ByteBuf clear() {
      return this.buf.clear();
   }

   public ByteBuf markReaderIndex() {
      return this.buf.markReaderIndex();
   }

   public ByteBuf resetReaderIndex() {
      return this.buf.resetReaderIndex();
   }

   public ByteBuf markWriterIndex() {
      return this.buf.markWriterIndex();
   }

   public ByteBuf resetWriterIndex() {
      return this.buf.resetWriterIndex();
   }

   public ByteBuf discardReadBytes() {
      return this.buf.discardReadBytes();
   }

   public ByteBuf discardSomeReadBytes() {
      return this.buf.discardSomeReadBytes();
   }

   public ByteBuf ensureWritable(int var1) {
      return this.buf.ensureWritable(☃);
   }

   public int ensureWritable(int var1, boolean var2) {
      return this.buf.ensureWritable(☃, ☃);
   }

   public boolean getBoolean(int var1) {
      return this.buf.getBoolean(☃);
   }

   public byte getByte(int var1) {
      return this.buf.getByte(☃);
   }

   public short getUnsignedByte(int var1) {
      return this.buf.getUnsignedByte(☃);
   }

   public short getShort(int var1) {
      return this.buf.getShort(☃);
   }

   public short getShortLE(int var1) {
      return this.buf.getShortLE(☃);
   }

   public int getUnsignedShort(int var1) {
      return this.buf.getUnsignedShort(☃);
   }

   public int getUnsignedShortLE(int var1) {
      return this.buf.getUnsignedShortLE(☃);
   }

   public int getMedium(int var1) {
      return this.buf.getMedium(☃);
   }

   public int getMediumLE(int var1) {
      return this.buf.getMediumLE(☃);
   }

   public int getUnsignedMedium(int var1) {
      return this.buf.getUnsignedMedium(☃);
   }

   public int getUnsignedMediumLE(int var1) {
      return this.buf.getUnsignedMediumLE(☃);
   }

   public int getInt(int var1) {
      return this.buf.getInt(☃);
   }

   public int getIntLE(int var1) {
      return this.buf.getIntLE(☃);
   }

   public long getUnsignedInt(int var1) {
      return this.buf.getUnsignedInt(☃);
   }

   public long getUnsignedIntLE(int var1) {
      return this.buf.getUnsignedIntLE(☃);
   }

   public long getLong(int var1) {
      return this.buf.getLong(☃);
   }

   public long getLongLE(int var1) {
      return this.buf.getLongLE(☃);
   }

   public char getChar(int var1) {
      return this.buf.getChar(☃);
   }

   public float getFloat(int var1) {
      return this.buf.getFloat(☃);
   }

   public double getDouble(int var1) {
      return this.buf.getDouble(☃);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2) {
      return this.buf.getBytes(☃, ☃);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3) {
      return this.buf.getBytes(☃, ☃, ☃);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.buf.getBytes(☃, ☃, ☃, ☃);
   }

   public ByteBuf getBytes(int var1, byte[] var2) {
      return this.buf.getBytes(☃, ☃);
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      return this.buf.getBytes(☃, ☃, ☃, ☃);
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      return this.buf.getBytes(☃, ☃);
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) throws IOException {
      return this.buf.getBytes(☃, ☃, ☃);
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) throws IOException {
      return this.buf.getBytes(☃, ☃, ☃);
   }

   public int getBytes(int var1, FileChannel var2, long var3, int var5) throws IOException {
      return this.buf.getBytes(☃, ☃, ☃, ☃);
   }

   public CharSequence getCharSequence(int var1, int var2, Charset var3) {
      return this.buf.getCharSequence(☃, ☃, ☃);
   }

   public ByteBuf setBoolean(int var1, boolean var2) {
      return this.buf.setBoolean(☃, ☃);
   }

   public ByteBuf setByte(int var1, int var2) {
      return this.buf.setByte(☃, ☃);
   }

   public ByteBuf setShort(int var1, int var2) {
      return this.buf.setShort(☃, ☃);
   }

   public ByteBuf setShortLE(int var1, int var2) {
      return this.buf.setShortLE(☃, ☃);
   }

   public ByteBuf setMedium(int var1, int var2) {
      return this.buf.setMedium(☃, ☃);
   }

   public ByteBuf setMediumLE(int var1, int var2) {
      return this.buf.setMediumLE(☃, ☃);
   }

   public ByteBuf setInt(int var1, int var2) {
      return this.buf.setInt(☃, ☃);
   }

   public ByteBuf setIntLE(int var1, int var2) {
      return this.buf.setIntLE(☃, ☃);
   }

   public ByteBuf setLong(int var1, long var2) {
      return this.buf.setLong(☃, ☃);
   }

   public ByteBuf setLongLE(int var1, long var2) {
      return this.buf.setLongLE(☃, ☃);
   }

   public ByteBuf setChar(int var1, int var2) {
      return this.buf.setChar(☃, ☃);
   }

   public ByteBuf setFloat(int var1, float var2) {
      return this.buf.setFloat(☃, ☃);
   }

   public ByteBuf setDouble(int var1, double var2) {
      return this.buf.setDouble(☃, ☃);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2) {
      return this.buf.setBytes(☃, ☃);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3) {
      return this.buf.setBytes(☃, ☃, ☃);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.buf.setBytes(☃, ☃, ☃, ☃);
   }

   public ByteBuf setBytes(int var1, byte[] var2) {
      return this.buf.setBytes(☃, ☃);
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      return this.buf.setBytes(☃, ☃, ☃, ☃);
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      return this.buf.setBytes(☃, ☃);
   }

   public int setBytes(int var1, InputStream var2, int var3) throws IOException {
      return this.buf.setBytes(☃, ☃, ☃);
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) throws IOException {
      return this.buf.setBytes(☃, ☃, ☃);
   }

   public int setBytes(int var1, FileChannel var2, long var3, int var5) throws IOException {
      return this.buf.setBytes(☃, ☃, ☃, ☃);
   }

   public ByteBuf setZero(int var1, int var2) {
      return this.buf.setZero(☃, ☃);
   }

   public int setCharSequence(int var1, CharSequence var2, Charset var3) {
      return this.buf.setCharSequence(☃, ☃, ☃);
   }

   public boolean readBoolean() {
      return this.buf.readBoolean();
   }

   public byte readByte() {
      return this.buf.readByte();
   }

   public short readUnsignedByte() {
      return this.buf.readUnsignedByte();
   }

   public short readShort() {
      return this.buf.readShort();
   }

   public short readShortLE() {
      return this.buf.readShortLE();
   }

   public int readUnsignedShort() {
      return this.buf.readUnsignedShort();
   }

   public int readUnsignedShortLE() {
      return this.buf.readUnsignedShortLE();
   }

   public int readMedium() {
      return this.buf.readMedium();
   }

   public int readMediumLE() {
      return this.buf.readMediumLE();
   }

   public int readUnsignedMedium() {
      return this.buf.readUnsignedMedium();
   }

   public int readUnsignedMediumLE() {
      return this.buf.readUnsignedMediumLE();
   }

   public int readInt() {
      return this.buf.readInt();
   }

   public int readIntLE() {
      return this.buf.readIntLE();
   }

   public long readUnsignedInt() {
      return this.buf.readUnsignedInt();
   }

   public long readUnsignedIntLE() {
      return this.buf.readUnsignedIntLE();
   }

   public long readLong() {
      return this.buf.readLong();
   }

   public long readLongLE() {
      return this.buf.readLongLE();
   }

   public char readChar() {
      return this.buf.readChar();
   }

   public float readFloat() {
      return this.buf.readFloat();
   }

   public double readDouble() {
      return this.buf.readDouble();
   }

   public ByteBuf readBytes(int var1) {
      return this.buf.readBytes(☃);
   }

   public ByteBuf readSlice(int var1) {
      return this.buf.readSlice(☃);
   }

   public ByteBuf readRetainedSlice(int var1) {
      return this.buf.readRetainedSlice(☃);
   }

   public ByteBuf readBytes(ByteBuf var1) {
      return this.buf.readBytes(☃);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2) {
      return this.buf.readBytes(☃, ☃);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2, int var3) {
      return this.buf.readBytes(☃, ☃, ☃);
   }

   public ByteBuf readBytes(byte[] var1) {
      return this.buf.readBytes(☃);
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      return this.buf.readBytes(☃, ☃, ☃);
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      return this.buf.readBytes(☃);
   }

   public ByteBuf readBytes(OutputStream var1, int var2) throws IOException {
      return this.buf.readBytes(☃, ☃);
   }

   public int readBytes(GatheringByteChannel var1, int var2) throws IOException {
      return this.buf.readBytes(☃, ☃);
   }

   public CharSequence readCharSequence(int var1, Charset var2) {
      return this.buf.readCharSequence(☃, ☃);
   }

   public int readBytes(FileChannel var1, long var2, int var4) throws IOException {
      return this.buf.readBytes(☃, ☃, ☃);
   }

   public ByteBuf skipBytes(int var1) {
      return this.buf.skipBytes(☃);
   }

   public ByteBuf writeBoolean(boolean var1) {
      return this.buf.writeBoolean(☃);
   }

   public ByteBuf writeByte(int var1) {
      return this.buf.writeByte(☃);
   }

   public ByteBuf writeShort(int var1) {
      return this.buf.writeShort(☃);
   }

   public ByteBuf writeShortLE(int var1) {
      return this.buf.writeShortLE(☃);
   }

   public ByteBuf writeMedium(int var1) {
      return this.buf.writeMedium(☃);
   }

   public ByteBuf writeMediumLE(int var1) {
      return this.buf.writeMediumLE(☃);
   }

   public ByteBuf writeInt(int var1) {
      return this.buf.writeInt(☃);
   }

   public ByteBuf writeIntLE(int var1) {
      return this.buf.writeIntLE(☃);
   }

   public ByteBuf writeLong(long var1) {
      return this.buf.writeLong(☃);
   }

   public ByteBuf writeLongLE(long var1) {
      return this.buf.writeLongLE(☃);
   }

   public ByteBuf writeChar(int var1) {
      return this.buf.writeChar(☃);
   }

   public ByteBuf writeFloat(float var1) {
      return this.buf.writeFloat(☃);
   }

   public ByteBuf writeDouble(double var1) {
      return this.buf.writeDouble(☃);
   }

   public ByteBuf writeBytes(ByteBuf var1) {
      return this.buf.writeBytes(☃);
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2) {
      return this.buf.writeBytes(☃, ☃);
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2, int var3) {
      return this.buf.writeBytes(☃, ☃, ☃);
   }

   public ByteBuf writeBytes(byte[] var1) {
      return this.buf.writeBytes(☃);
   }

   public ByteBuf writeBytes(byte[] var1, int var2, int var3) {
      return this.buf.writeBytes(☃, ☃, ☃);
   }

   public ByteBuf writeBytes(ByteBuffer var1) {
      return this.buf.writeBytes(☃);
   }

   public int writeBytes(InputStream var1, int var2) throws IOException {
      return this.buf.writeBytes(☃, ☃);
   }

   public int writeBytes(ScatteringByteChannel var1, int var2) throws IOException {
      return this.buf.writeBytes(☃, ☃);
   }

   public int writeBytes(FileChannel var1, long var2, int var4) throws IOException {
      return this.buf.writeBytes(☃, ☃, ☃);
   }

   public ByteBuf writeZero(int var1) {
      return this.buf.writeZero(☃);
   }

   public int writeCharSequence(CharSequence var1, Charset var2) {
      return this.buf.writeCharSequence(☃, ☃);
   }

   public int indexOf(int var1, int var2, byte var3) {
      return this.buf.indexOf(☃, ☃, ☃);
   }

   public int bytesBefore(byte var1) {
      return this.buf.bytesBefore(☃);
   }

   public int bytesBefore(int var1, byte var2) {
      return this.buf.bytesBefore(☃, ☃);
   }

   public int bytesBefore(int var1, int var2, byte var3) {
      return this.buf.bytesBefore(☃, ☃, ☃);
   }

   public int forEachByte(ByteProcessor var1) {
      return this.buf.forEachByte(☃);
   }

   public int forEachByte(int var1, int var2, ByteProcessor var3) {
      return this.buf.forEachByte(☃, ☃, ☃);
   }

   public int forEachByteDesc(ByteProcessor var1) {
      return this.buf.forEachByteDesc(☃);
   }

   public int forEachByteDesc(int var1, int var2, ByteProcessor var3) {
      return this.buf.forEachByteDesc(☃, ☃, ☃);
   }

   public ByteBuf copy() {
      return this.buf.copy();
   }

   public ByteBuf copy(int var1, int var2) {
      return this.buf.copy(☃, ☃);
   }

   public ByteBuf slice() {
      return this.buf.slice();
   }

   public ByteBuf retainedSlice() {
      return this.buf.retainedSlice();
   }

   public ByteBuf slice(int var1, int var2) {
      return this.buf.slice(☃, ☃);
   }

   public ByteBuf retainedSlice(int var1, int var2) {
      return this.buf.retainedSlice(☃, ☃);
   }

   public ByteBuf duplicate() {
      return this.buf.duplicate();
   }

   public ByteBuf retainedDuplicate() {
      return this.buf.retainedDuplicate();
   }

   public int nioBufferCount() {
      return this.buf.nioBufferCount();
   }

   public ByteBuffer nioBuffer() {
      return this.buf.nioBuffer();
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return this.buf.nioBuffer(☃, ☃);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return this.buf.internalNioBuffer(☃, ☃);
   }

   public ByteBuffer[] nioBuffers() {
      return this.buf.nioBuffers();
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return this.buf.nioBuffers(☃, ☃);
   }

   public boolean hasArray() {
      return this.buf.hasArray();
   }

   public byte[] array() {
      return this.buf.array();
   }

   public int arrayOffset() {
      return this.buf.arrayOffset();
   }

   public boolean hasMemoryAddress() {
      return this.buf.hasMemoryAddress();
   }

   public long memoryAddress() {
      return this.buf.memoryAddress();
   }

   public String toString(Charset var1) {
      return this.buf.toString(☃);
   }

   public String toString(int var1, int var2, Charset var3) {
      return this.buf.toString(☃, ☃, ☃);
   }

   public int hashCode() {
      return this.buf.hashCode();
   }

   public boolean equals(Object var1) {
      return this.buf.equals(☃);
   }

   public int compareTo(ByteBuf var1) {
      return this.buf.compareTo(☃);
   }

   public String toString() {
      return this.buf.toString();
   }

   public ByteBuf retain(int var1) {
      return this.buf.retain(☃);
   }

   public ByteBuf retain() {
      return this.buf.retain();
   }

   public ByteBuf touch() {
      return this.buf.touch();
   }

   public ByteBuf touch(Object var1) {
      return this.buf.touch(☃);
   }

   public int refCnt() {
      return this.buf.refCnt();
   }

   public boolean release() {
      return this.buf.release();
   }

   public boolean release(int var1) {
      return this.buf.release(☃);
   }
}
