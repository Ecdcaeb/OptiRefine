package net.minecraft.client.renderer;

import com.google.common.primitives.Floats;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BufferBuilder {
   private static final Logger LOGGER = LogManager.getLogger();
   private ByteBuffer byteBuffer;
   private IntBuffer rawIntBuffer;
   private ShortBuffer rawShortBuffer;
   private FloatBuffer rawFloatBuffer;
   private int vertexCount;
   private VertexFormatElement vertexFormatElement;
   private int vertexFormatIndex;
   private boolean noColor;
   private int drawMode;
   private double xOffset;
   private double yOffset;
   private double zOffset;
   private VertexFormat vertexFormat;
   private boolean isDrawing;

   public BufferBuilder(int var1) {
      this.byteBuffer = GLAllocation.createDirectByteBuffer(☃ * 4);
      this.rawIntBuffer = this.byteBuffer.asIntBuffer();
      this.rawShortBuffer = this.byteBuffer.asShortBuffer();
      this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
   }

   private void growBuffer(int var1) {
      if (MathHelper.roundUp(☃, 4) / 4 > this.rawIntBuffer.remaining() || this.vertexCount * this.vertexFormat.getSize() + ☃ > this.byteBuffer.capacity()) {
         int ☃ = this.byteBuffer.capacity();
         int ☃x = ☃ + MathHelper.roundUp(☃, 2097152);
         LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", ☃, ☃x);
         int ☃xx = this.rawIntBuffer.position();
         ByteBuffer ☃xxx = GLAllocation.createDirectByteBuffer(☃x);
         ((Buffer)this.byteBuffer).position(0);
         ☃xxx.put(this.byteBuffer);
         ((Buffer)☃xxx).rewind();
         this.byteBuffer = ☃xxx;
         this.rawFloatBuffer = this.byteBuffer.asFloatBuffer().asReadOnlyBuffer();
         this.rawIntBuffer = this.byteBuffer.asIntBuffer();
         ((Buffer)this.rawIntBuffer).position(☃xx);
         this.rawShortBuffer = this.byteBuffer.asShortBuffer();
         ((Buffer)this.rawShortBuffer).position(☃xx << 1);
      }
   }

   public void sortVertexData(float var1, float var2, float var3) {
      int ☃ = this.vertexCount / 4;
      final float[] ☃x = new float[☃];

      for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
         ☃x[☃xx] = getDistanceSq(
            this.rawFloatBuffer,
            (float)(☃ + this.xOffset),
            (float)(☃ + this.yOffset),
            (float)(☃ + this.zOffset),
            this.vertexFormat.getIntegerSize(),
            ☃xx * this.vertexFormat.getSize()
         );
      }

      Integer[] ☃xx = new Integer[☃];

      for (int ☃xxx = 0; ☃xxx < ☃xx.length; ☃xxx++) {
         ☃xx[☃xxx] = ☃xxx;
      }

      Arrays.sort(☃xx, new Comparator<Integer>() {
         public int compare(Integer var1, Integer var2x) {
            return Floats.compare(☃[var2x], ☃[☃]);
         }
      });
      BitSet ☃xxx = new BitSet();
      int ☃xxxx = this.vertexFormat.getSize();
      int[] ☃xxxxx = new int[☃xxxx];

      for (int ☃xxxxxx = ☃xxx.nextClearBit(0); ☃xxxxxx < ☃xx.length; ☃xxxxxx = ☃xxx.nextClearBit(☃xxxxxx + 1)) {
         int ☃xxxxxxx = ☃xx[☃xxxxxx];
         if (☃xxxxxxx != ☃xxxxxx) {
            ((Buffer)this.rawIntBuffer).limit(☃xxxxxxx * ☃xxxx + ☃xxxx);
            ((Buffer)this.rawIntBuffer).position(☃xxxxxxx * ☃xxxx);
            this.rawIntBuffer.get(☃xxxxx);
            int ☃xxxxxxxx = ☃xxxxxxx;

            for (int ☃xxxxxxxxx = ☃xx[☃xxxxxxx]; ☃xxxxxxxx != ☃xxxxxx; ☃xxxxxxxxx = ☃xx[☃xxxxxxxxx]) {
               ((Buffer)this.rawIntBuffer).limit(☃xxxxxxxxx * ☃xxxx + ☃xxxx);
               ((Buffer)this.rawIntBuffer).position(☃xxxxxxxxx * ☃xxxx);
               IntBuffer ☃xxxxxxxxxx = this.rawIntBuffer.slice();
               ((Buffer)this.rawIntBuffer).limit(☃xxxxxxxx * ☃xxxx + ☃xxxx);
               ((Buffer)this.rawIntBuffer).position(☃xxxxxxxx * ☃xxxx);
               this.rawIntBuffer.put(☃xxxxxxxxxx);
               ☃xxx.set(☃xxxxxxxx);
               ☃xxxxxxxx = ☃xxxxxxxxx;
            }

            ((Buffer)this.rawIntBuffer).limit(☃xxxxxx * ☃xxxx + ☃xxxx);
            ((Buffer)this.rawIntBuffer).position(☃xxxxxx * ☃xxxx);
            this.rawIntBuffer.put(☃xxxxx);
         }

         ☃xxx.set(☃xxxxxx);
      }
   }

   public BufferBuilder.State getVertexState() {
      ((Buffer)this.rawIntBuffer).rewind();
      int ☃ = this.getBufferSize();
      ((Buffer)this.rawIntBuffer).limit(☃);
      int[] ☃x = new int[☃];
      this.rawIntBuffer.get(☃x);
      ((Buffer)this.rawIntBuffer).limit(this.rawIntBuffer.capacity());
      ((Buffer)this.rawIntBuffer).position(☃);
      return new BufferBuilder.State(☃x, new VertexFormat(this.vertexFormat));
   }

   private int getBufferSize() {
      return this.vertexCount * this.vertexFormat.getIntegerSize();
   }

   private static float getDistanceSq(FloatBuffer var0, float var1, float var2, float var3, int var4, int var5) {
      float ☃ = ☃.get(☃ + ☃ * 0 + 0);
      float ☃x = ☃.get(☃ + ☃ * 0 + 1);
      float ☃xx = ☃.get(☃ + ☃ * 0 + 2);
      float ☃xxx = ☃.get(☃ + ☃ * 1 + 0);
      float ☃xxxx = ☃.get(☃ + ☃ * 1 + 1);
      float ☃xxxxx = ☃.get(☃ + ☃ * 1 + 2);
      float ☃xxxxxx = ☃.get(☃ + ☃ * 2 + 0);
      float ☃xxxxxxx = ☃.get(☃ + ☃ * 2 + 1);
      float ☃xxxxxxxx = ☃.get(☃ + ☃ * 2 + 2);
      float ☃xxxxxxxxx = ☃.get(☃ + ☃ * 3 + 0);
      float ☃xxxxxxxxxx = ☃.get(☃ + ☃ * 3 + 1);
      float ☃xxxxxxxxxxx = ☃.get(☃ + ☃ * 3 + 2);
      float ☃xxxxxxxxxxxx = (☃ + ☃xxx + ☃xxxxxx + ☃xxxxxxxxx) * 0.25F - ☃;
      float ☃xxxxxxxxxxxxx = (☃x + ☃xxxx + ☃xxxxxxx + ☃xxxxxxxxxx) * 0.25F - ☃;
      float ☃xxxxxxxxxxxxxx = (☃xx + ☃xxxxx + ☃xxxxxxxx + ☃xxxxxxxxxxx) * 0.25F - ☃;
      return ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxx + ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx;
   }

   public void setVertexState(BufferBuilder.State var1) {
      ((Buffer)this.rawIntBuffer).clear();
      this.growBuffer(☃.getRawBuffer().length * 4);
      this.rawIntBuffer.put(☃.getRawBuffer());
      this.vertexCount = ☃.getVertexCount();
      this.vertexFormat = new VertexFormat(☃.getVertexFormat());
   }

   public void reset() {
      this.vertexCount = 0;
      this.vertexFormatElement = null;
      this.vertexFormatIndex = 0;
   }

   public void begin(int var1, VertexFormat var2) {
      if (this.isDrawing) {
         throw new IllegalStateException("Already building!");
      } else {
         this.isDrawing = true;
         this.reset();
         this.drawMode = ☃;
         this.vertexFormat = ☃;
         this.vertexFormatElement = ☃.getElement(this.vertexFormatIndex);
         this.noColor = false;
         ((Buffer)this.byteBuffer).limit(this.byteBuffer.capacity());
      }
   }

   public BufferBuilder tex(double var1, double var3) {
      int ☃ = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch (this.vertexFormatElement.getType()) {
         case FLOAT:
            this.byteBuffer.putFloat(☃, (float)☃);
            this.byteBuffer.putFloat(☃ + 4, (float)☃);
            break;
         case UINT:
         case INT:
            this.byteBuffer.putInt(☃, (int)☃);
            this.byteBuffer.putInt(☃ + 4, (int)☃);
            break;
         case USHORT:
         case SHORT:
            this.byteBuffer.putShort(☃, (short)☃);
            this.byteBuffer.putShort(☃ + 2, (short)☃);
            break;
         case UBYTE:
         case BYTE:
            this.byteBuffer.put(☃, (byte)☃);
            this.byteBuffer.put(☃ + 1, (byte)☃);
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public BufferBuilder lightmap(int var1, int var2) {
      int ☃ = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch (this.vertexFormatElement.getType()) {
         case FLOAT:
            this.byteBuffer.putFloat(☃, ☃);
            this.byteBuffer.putFloat(☃ + 4, ☃);
            break;
         case UINT:
         case INT:
            this.byteBuffer.putInt(☃, ☃);
            this.byteBuffer.putInt(☃ + 4, ☃);
            break;
         case USHORT:
         case SHORT:
            this.byteBuffer.putShort(☃, (short)☃);
            this.byteBuffer.putShort(☃ + 2, (short)☃);
            break;
         case UBYTE:
         case BYTE:
            this.byteBuffer.put(☃, (byte)☃);
            this.byteBuffer.put(☃ + 1, (byte)☃);
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public void putBrightness4(int var1, int var2, int var3, int var4) {
      int ☃ = (this.vertexCount - 4) * this.vertexFormat.getIntegerSize() + this.vertexFormat.getUvOffsetById(1) / 4;
      int ☃x = this.vertexFormat.getSize() >> 2;
      this.rawIntBuffer.put(☃, ☃);
      this.rawIntBuffer.put(☃ + ☃x, ☃);
      this.rawIntBuffer.put(☃ + ☃x * 2, ☃);
      this.rawIntBuffer.put(☃ + ☃x * 3, ☃);
   }

   public void putPosition(double var1, double var3, double var5) {
      int ☃ = this.vertexFormat.getIntegerSize();
      int ☃x = (this.vertexCount - 4) * ☃;

      for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
         int ☃xxx = ☃x + ☃xx * ☃;
         int ☃xxxx = ☃xxx + 1;
         int ☃xxxxx = ☃xxxx + 1;
         this.rawIntBuffer.put(☃xxx, Float.floatToRawIntBits((float)(☃ + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(☃xxx))));
         this.rawIntBuffer.put(☃xxxx, Float.floatToRawIntBits((float)(☃ + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(☃xxxx))));
         this.rawIntBuffer.put(☃xxxxx, Float.floatToRawIntBits((float)(☃ + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(☃xxxxx))));
      }
   }

   private int getColorIndex(int var1) {
      return ((this.vertexCount - ☃) * this.vertexFormat.getSize() + this.vertexFormat.getColorOffset()) / 4;
   }

   public void putColorMultiplier(float var1, float var2, float var3, int var4) {
      int ☃ = this.getColorIndex(☃);
      int ☃x = -1;
      if (!this.noColor) {
         ☃x = this.rawIntBuffer.get(☃);
         if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            int ☃xx = (int)((☃x & 0xFF) * ☃);
            int ☃xxx = (int)((☃x >> 8 & 0xFF) * ☃);
            int ☃xxxx = (int)((☃x >> 16 & 0xFF) * ☃);
            ☃x &= -16777216;
            ☃x |= ☃xxxx << 16 | ☃xxx << 8 | ☃xx;
         } else {
            int ☃xx = (int)((☃x >> 24 & 0xFF) * ☃);
            int ☃xxx = (int)((☃x >> 16 & 0xFF) * ☃);
            int ☃xxxx = (int)((☃x >> 8 & 0xFF) * ☃);
            ☃x &= 255;
            ☃x |= ☃xx << 24 | ☃xxx << 16 | ☃xxxx << 8;
         }
      }

      this.rawIntBuffer.put(☃, ☃x);
   }

   private void putColor(int var1, int var2) {
      int ☃ = this.getColorIndex(☃);
      int ☃x = ☃ >> 16 & 0xFF;
      int ☃xx = ☃ >> 8 & 0xFF;
      int ☃xxx = ☃ & 0xFF;
      this.putColorRGBA(☃, ☃x, ☃xx, ☃xxx);
   }

   public void putColorRGB_F(float var1, float var2, float var3, int var4) {
      int ☃ = this.getColorIndex(☃);
      int ☃x = MathHelper.clamp((int)(☃ * 255.0F), 0, 255);
      int ☃xx = MathHelper.clamp((int)(☃ * 255.0F), 0, 255);
      int ☃xxx = MathHelper.clamp((int)(☃ * 255.0F), 0, 255);
      this.putColorRGBA(☃, ☃x, ☃xx, ☃xxx);
   }

   private void putColorRGBA(int var1, int var2, int var3, int var4) {
      if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
         this.rawIntBuffer.put(☃, 0xFF000000 | ☃ << 16 | ☃ << 8 | ☃);
      } else {
         this.rawIntBuffer.put(☃, ☃ << 24 | ☃ << 16 | ☃ << 8 | 0xFF);
      }
   }

   public void noColor() {
      this.noColor = true;
   }

   public BufferBuilder color(float var1, float var2, float var3, float var4) {
      return this.color((int)(☃ * 255.0F), (int)(☃ * 255.0F), (int)(☃ * 255.0F), (int)(☃ * 255.0F));
   }

   public BufferBuilder color(int var1, int var2, int var3, int var4) {
      if (this.noColor) {
         return this;
      } else {
         int ☃ = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
         switch (this.vertexFormatElement.getType()) {
            case FLOAT:
               this.byteBuffer.putFloat(☃, ☃ / 255.0F);
               this.byteBuffer.putFloat(☃ + 4, ☃ / 255.0F);
               this.byteBuffer.putFloat(☃ + 8, ☃ / 255.0F);
               this.byteBuffer.putFloat(☃ + 12, ☃ / 255.0F);
               break;
            case UINT:
            case INT:
               this.byteBuffer.putFloat(☃, ☃);
               this.byteBuffer.putFloat(☃ + 4, ☃);
               this.byteBuffer.putFloat(☃ + 8, ☃);
               this.byteBuffer.putFloat(☃ + 12, ☃);
               break;
            case USHORT:
            case SHORT:
               this.byteBuffer.putShort(☃, (short)☃);
               this.byteBuffer.putShort(☃ + 2, (short)☃);
               this.byteBuffer.putShort(☃ + 4, (short)☃);
               this.byteBuffer.putShort(☃ + 6, (short)☃);
               break;
            case UBYTE:
            case BYTE:
               if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                  this.byteBuffer.put(☃, (byte)☃);
                  this.byteBuffer.put(☃ + 1, (byte)☃);
                  this.byteBuffer.put(☃ + 2, (byte)☃);
                  this.byteBuffer.put(☃ + 3, (byte)☃);
               } else {
                  this.byteBuffer.put(☃, (byte)☃);
                  this.byteBuffer.put(☃ + 1, (byte)☃);
                  this.byteBuffer.put(☃ + 2, (byte)☃);
                  this.byteBuffer.put(☃ + 3, (byte)☃);
               }
         }

         this.nextVertexFormatIndex();
         return this;
      }
   }

   public void addVertexData(int[] var1) {
      this.growBuffer(☃.length * 4);
      ((Buffer)this.rawIntBuffer).position(this.getBufferSize());
      this.rawIntBuffer.put(☃);
      this.vertexCount = this.vertexCount + ☃.length / this.vertexFormat.getIntegerSize();
   }

   public void endVertex() {
      this.vertexCount++;
      this.growBuffer(this.vertexFormat.getSize());
   }

   public BufferBuilder pos(double var1, double var3, double var5) {
      int ☃ = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch (this.vertexFormatElement.getType()) {
         case FLOAT:
            this.byteBuffer.putFloat(☃, (float)(☃ + this.xOffset));
            this.byteBuffer.putFloat(☃ + 4, (float)(☃ + this.yOffset));
            this.byteBuffer.putFloat(☃ + 8, (float)(☃ + this.zOffset));
            break;
         case UINT:
         case INT:
            this.byteBuffer.putInt(☃, Float.floatToRawIntBits((float)(☃ + this.xOffset)));
            this.byteBuffer.putInt(☃ + 4, Float.floatToRawIntBits((float)(☃ + this.yOffset)));
            this.byteBuffer.putInt(☃ + 8, Float.floatToRawIntBits((float)(☃ + this.zOffset)));
            break;
         case USHORT:
         case SHORT:
            this.byteBuffer.putShort(☃, (short)(☃ + this.xOffset));
            this.byteBuffer.putShort(☃ + 2, (short)(☃ + this.yOffset));
            this.byteBuffer.putShort(☃ + 4, (short)(☃ + this.zOffset));
            break;
         case UBYTE:
         case BYTE:
            this.byteBuffer.put(☃, (byte)(☃ + this.xOffset));
            this.byteBuffer.put(☃ + 1, (byte)(☃ + this.yOffset));
            this.byteBuffer.put(☃ + 2, (byte)(☃ + this.zOffset));
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public void putNormal(float var1, float var2, float var3) {
      int ☃ = (byte)(☃ * 127.0F) & 255;
      int ☃x = (byte)(☃ * 127.0F) & 255;
      int ☃xx = (byte)(☃ * 127.0F) & 255;
      int ☃xxx = ☃ | ☃x << 8 | ☃xx << 16;
      int ☃xxxx = this.vertexFormat.getSize() >> 2;
      int ☃xxxxx = (this.vertexCount - 4) * ☃xxxx + this.vertexFormat.getNormalOffset() / 4;
      this.rawIntBuffer.put(☃xxxxx, ☃xxx);
      this.rawIntBuffer.put(☃xxxxx + ☃xxxx, ☃xxx);
      this.rawIntBuffer.put(☃xxxxx + ☃xxxx * 2, ☃xxx);
      this.rawIntBuffer.put(☃xxxxx + ☃xxxx * 3, ☃xxx);
   }

   private void nextVertexFormatIndex() {
      this.vertexFormatIndex++;
      this.vertexFormatIndex = this.vertexFormatIndex % this.vertexFormat.getElementCount();
      this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
      if (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
         this.nextVertexFormatIndex();
      }
   }

   public BufferBuilder normal(float var1, float var2, float var3) {
      int ☃ = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch (this.vertexFormatElement.getType()) {
         case FLOAT:
            this.byteBuffer.putFloat(☃, ☃);
            this.byteBuffer.putFloat(☃ + 4, ☃);
            this.byteBuffer.putFloat(☃ + 8, ☃);
            break;
         case UINT:
         case INT:
            this.byteBuffer.putInt(☃, (int)☃);
            this.byteBuffer.putInt(☃ + 4, (int)☃);
            this.byteBuffer.putInt(☃ + 8, (int)☃);
            break;
         case USHORT:
         case SHORT:
            this.byteBuffer.putShort(☃, (short)((int)☃ * 32767 & 65535));
            this.byteBuffer.putShort(☃ + 2, (short)((int)☃ * 32767 & 65535));
            this.byteBuffer.putShort(☃ + 4, (short)((int)☃ * 32767 & 65535));
            break;
         case UBYTE:
         case BYTE:
            this.byteBuffer.put(☃, (byte)((int)☃ * 127 & 0xFF));
            this.byteBuffer.put(☃ + 1, (byte)((int)☃ * 127 & 0xFF));
            this.byteBuffer.put(☃ + 2, (byte)((int)☃ * 127 & 0xFF));
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public void setTranslation(double var1, double var3, double var5) {
      this.xOffset = ☃;
      this.yOffset = ☃;
      this.zOffset = ☃;
   }

   public void finishDrawing() {
      if (!this.isDrawing) {
         throw new IllegalStateException("Not building!");
      } else {
         this.isDrawing = false;
         ((Buffer)this.byteBuffer).position(0);
         ((Buffer)this.byteBuffer).limit(this.getBufferSize() * 4);
      }
   }

   public ByteBuffer getByteBuffer() {
      return this.byteBuffer;
   }

   public VertexFormat getVertexFormat() {
      return this.vertexFormat;
   }

   public int getVertexCount() {
      return this.vertexCount;
   }

   public int getDrawMode() {
      return this.drawMode;
   }

   public void putColor4(int var1) {
      for (int ☃ = 0; ☃ < 4; ☃++) {
         this.putColor(☃, ☃ + 1);
      }
   }

   public void putColorRGB_F4(float var1, float var2, float var3) {
      for (int ☃ = 0; ☃ < 4; ☃++) {
         this.putColorRGB_F(☃, ☃, ☃, ☃ + 1);
      }
   }

   public class State {
      private final int[] stateRawBuffer;
      private final VertexFormat stateVertexFormat;

      public State(int[] var2, VertexFormat var3) {
         this.stateRawBuffer = ☃;
         this.stateVertexFormat = ☃;
      }

      public int[] getRawBuffer() {
         return this.stateRawBuffer;
      }

      public int getVertexCount() {
         return this.stateRawBuffer.length / this.stateVertexFormat.getIntegerSize();
      }

      public VertexFormat getVertexFormat() {
         return this.stateVertexFormat;
      }
   }
}
