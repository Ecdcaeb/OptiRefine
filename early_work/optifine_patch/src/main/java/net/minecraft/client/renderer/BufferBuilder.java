/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Float
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 *  java.nio.ByteBuffer
 *  java.nio.ByteOrder
 *  java.nio.FloatBuffer
 *  java.nio.IntBuffer
 *  java.nio.ShortBuffer
 *  java.util.Arrays
 *  java.util.BitSet
 *  java.util.Comparator
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.BufferBuilder$2
 *  net.minecraft.client.renderer.BufferBuilder$State
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.client.renderer.vertex.VertexFormatElement
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumUsage
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.optifine.SmartAnimations
 *  net.optifine.render.RenderEnv
 *  net.optifine.shaders.SVertexBuilder
 *  net.optifine.util.TextureUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.optifine.SmartAnimations;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.util.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

/*
 * Exception performing whole class analysis ignored.
 */
public class BufferBuilder {
    private static final Logger LOGGER = LogManager.getLogger();
    private ByteBuffer byteBuffer;
    public IntBuffer rawIntBuffer;
    private ShortBuffer rawShortBuffer;
    public FloatBuffer rawFloatBuffer;
    public int vertexCount;
    private VertexFormatElement vertexFormatElement;
    private int vertexFormatIndex;
    private boolean noColor;
    public int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    private BlockRenderLayer blockLayer = null;
    private boolean[] drawnIcons = new boolean[256];
    private TextureAtlasSprite[] quadSprites = null;
    private TextureAtlasSprite[] quadSpritesPrev = null;
    private TextureAtlasSprite quadSprite = null;
    public SVertexBuilder sVertexBuilder;
    public RenderEnv renderEnv = null;
    public BitSet animatedSprites = null;
    public BitSet animatedSpritesCached = new BitSet();
    private boolean modeTriangles = false;
    private ByteBuffer byteBufferTriangles;

    public BufferBuilder(int bufferSizeIn) {
        this.byteBuffer = GLAllocation.createDirectByteBuffer((int)(bufferSizeIn * 4));
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawShortBuffer = this.byteBuffer.asShortBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
        SVertexBuilder.initVertexBuilder((BufferBuilder)this);
    }

    private void growBuffer(int p_181670_1_) {
        if (MathHelper.roundUp((int)p_181670_1_, (int)4) / 4 > this.rawIntBuffer.remaining() || this.vertexCount * this.vertexFormat.getSize() + p_181670_1_ > this.byteBuffer.capacity()) {
            int i = this.byteBuffer.capacity();
            int j = i + MathHelper.roundUp((int)p_181670_1_, (int)0x200000);
            LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", (Object)i, (Object)j);
            int k = this.rawIntBuffer.position();
            ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer((int)j);
            this.byteBuffer.position(0);
            bytebuffer.put(this.byteBuffer);
            bytebuffer.rewind();
            this.byteBuffer = bytebuffer;
            this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
            this.rawIntBuffer = this.byteBuffer.asIntBuffer();
            this.rawIntBuffer.position(k);
            this.rawShortBuffer = this.byteBuffer.asShortBuffer();
            this.rawShortBuffer.position(k << 1);
            if (this.quadSprites != null) {
                TextureAtlasSprite[] sprites = this.quadSprites;
                int quadSize = this.getBufferQuadSize();
                this.quadSprites = new TextureAtlasSprite[quadSize];
                System.arraycopy((Object)sprites, (int)0, (Object)this.quadSprites, (int)0, (int)Math.min((int)sprites.length, (int)this.quadSprites.length));
                this.quadSpritesPrev = null;
            }
        }
    }

    public void sortVertexData(float p_181674_1_, float p_181674_2_, float p_181674_3_) {
        int i = this.vertexCount / 4;
        float[] afloat = new float[i];
        for (int j = 0; j < i; ++j) {
            afloat[j] = BufferBuilder.getDistanceSq(this.rawFloatBuffer, (float)((double)p_181674_1_ + this.xOffset), (float)((double)p_181674_2_ + this.yOffset), (float)((double)p_181674_3_ + this.zOffset), this.vertexFormat.getIntegerSize(), j * this.vertexFormat.getSize());
        }
        Object[] ainteger = new Integer[i];
        for (int k = 0; k < ainteger.length; ++k) {
            ainteger[k] = k;
        }
        Arrays.sort((Object[])ainteger, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        BitSet bitset = new BitSet();
        int l = this.vertexFormat.getSize();
        int[] aint = new int[l];
        int i1 = bitset.nextClearBit(0);
        while (i1 < ainteger.length) {
            int j1 = ainteger[i1].intValue();
            if (j1 != i1) {
                this.rawIntBuffer.limit(j1 * l + l);
                this.rawIntBuffer.position(j1 * l);
                this.rawIntBuffer.get(aint);
                int k1 = j1;
                int l1 = ainteger[j1].intValue();
                while (k1 != i1) {
                    this.rawIntBuffer.limit(l1 * l + l);
                    this.rawIntBuffer.position(l1 * l);
                    IntBuffer intbuffer = this.rawIntBuffer.slice();
                    this.rawIntBuffer.limit(k1 * l + l);
                    this.rawIntBuffer.position(k1 * l);
                    this.rawIntBuffer.put(intbuffer);
                    bitset.set(k1);
                    k1 = l1;
                    l1 = ainteger[l1].intValue();
                }
                this.rawIntBuffer.limit(i1 * l + l);
                this.rawIntBuffer.position(i1 * l);
                this.rawIntBuffer.put(aint);
            }
            bitset.set(i1);
            i1 = bitset.nextClearBit(i1 + 1);
        }
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(this.getBufferSize());
        if (this.quadSprites != null) {
            TextureAtlasSprite[] quadSpritesSorted = new TextureAtlasSprite[this.vertexCount / 4];
            int quadStep = this.vertexFormat.getSize() / 4 * 4;
            int ix = 0;
            while (ix < ainteger.length) {
                int indexQuad = ainteger[ix].intValue();
                int indexQuadSorted = ix++;
                quadSpritesSorted[indexQuadSorted] = this.quadSprites[indexQuad];
            }
            System.arraycopy((Object)quadSpritesSorted, (int)0, (Object)this.quadSprites, (int)0, (int)quadSpritesSorted.length);
        }
    }

    public State getVertexState() {
        this.rawIntBuffer.rewind();
        int i = this.getBufferSize();
        this.rawIntBuffer.limit(i);
        int[] aint = new int[i];
        this.rawIntBuffer.get(aint);
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(i);
        TextureAtlasSprite[] quadSpritesCopy = null;
        if (this.quadSprites != null) {
            int countQuads = this.vertexCount / 4;
            quadSpritesCopy = new TextureAtlasSprite[countQuads];
            System.arraycopy((Object)this.quadSprites, (int)0, (Object)quadSpritesCopy, (int)0, (int)countQuads);
        }
        return new State(this, aint, new VertexFormat(this.vertexFormat), quadSpritesCopy);
    }

    public int getBufferSize() {
        return this.vertexCount * this.vertexFormat.getIntegerSize();
    }

    private static float getDistanceSq(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_) {
        float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
        float f1 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
        float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
        float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
        float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
        float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
        float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
        float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
        float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
        float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
        float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
        float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
        float f12 = (f + f3 + f6 + f9) * 0.25f - p_181665_1_;
        float f13 = (f1 + f4 + f7 + f10) * 0.25f - p_181665_2_;
        float f14 = (f2 + f5 + f8 + f11) * 0.25f - p_181665_3_;
        return f12 * f12 + f13 * f13 + f14 * f14;
    }

    public void setVertexState(State state) {
        this.rawIntBuffer.clear();
        this.growBuffer(state.getRawBuffer().length * 4);
        this.rawIntBuffer.put(state.getRawBuffer());
        this.vertexCount = state.getVertexCount();
        this.vertexFormat = new VertexFormat(state.getVertexFormat());
        if (State.access$000((State)state) != null) {
            if (this.quadSprites == null) {
                this.quadSprites = this.quadSpritesPrev;
            }
            if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }
            TextureAtlasSprite[] src = State.access$000((State)state);
            System.arraycopy((Object)src, (int)0, (Object)this.quadSprites, (int)0, (int)src.length);
        } else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
        }
    }

    public void reset() {
        this.vertexCount = 0;
        this.vertexFormatElement = null;
        this.vertexFormatIndex = 0;
        this.quadSprite = null;
        if (SmartAnimations.isActive()) {
            if (this.animatedSprites == null) {
                this.animatedSprites = this.animatedSpritesCached;
            }
            this.animatedSprites.clear();
        } else if (this.animatedSprites != null) {
            this.animatedSprites = null;
        }
        this.modeTriangles = false;
    }

    public void begin(int glMode, VertexFormat format) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        }
        this.isDrawing = true;
        this.reset();
        this.drawMode = glMode;
        this.vertexFormat = format;
        this.vertexFormatElement = format.getElement(this.vertexFormatIndex);
        this.noColor = false;
        this.byteBuffer.limit(this.byteBuffer.capacity());
        if (Config.isShaders()) {
            SVertexBuilder.endSetVertexFormat((BufferBuilder)this);
        }
        if (Config.isMultiTexture()) {
            if (this.blockLayer != null) {
                if (this.quadSprites == null) {
                    this.quadSprites = this.quadSpritesPrev;
                }
                if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                    this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
                }
            }
        } else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
        }
    }

    public BufferBuilder tex(double u, double v) {
        if (this.quadSprite != null && this.quadSprites != null) {
            u = this.quadSprite.toSingleU((float)u);
            v = this.quadSprite.toSingleV((float)v);
            this.quadSprites[this.vertexCount / 4] = this.quadSprite;
        }
        int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)u);
                this.byteBuffer.putFloat(i + 4, (float)v);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, (int)u);
                this.byteBuffer.putInt(i + 4, (int)v);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)v);
                this.byteBuffer.putShort(i + 2, (short)u);
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)v);
                this.byteBuffer.put(i + 1, (byte)u);
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public BufferBuilder lightmap(int p_187314_1_, int p_187314_2_) {
        int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)p_187314_1_);
                this.byteBuffer.putFloat(i + 4, (float)p_187314_2_);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, p_187314_1_);
                this.byteBuffer.putInt(i + 4, p_187314_2_);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)p_187314_2_);
                this.byteBuffer.putShort(i + 2, (short)p_187314_1_);
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)p_187314_2_);
                this.byteBuffer.put(i + 1, (byte)p_187314_1_);
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_) {
        int i = (this.vertexCount - 4) * this.vertexFormat.getIntegerSize() + this.vertexFormat.getUvOffsetById(1) / 4;
        int j = this.vertexFormat.getSize() >> 2;
        this.rawIntBuffer.put(i, p_178962_1_);
        this.rawIntBuffer.put(i + j, p_178962_2_);
        this.rawIntBuffer.put(i + j * 2, p_178962_3_);
        this.rawIntBuffer.put(i + j * 3, p_178962_4_);
    }

    public void putPosition(double x, double y, double z) {
        int i = this.vertexFormat.getIntegerSize();
        int j = (this.vertexCount - 4) * i;
        for (int k = 0; k < 4; ++k) {
            int l = j + k * i;
            int i1 = l + 1;
            int j1 = i1 + 1;
            this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)((float)(x + this.xOffset) + Float.intBitsToFloat((int)this.rawIntBuffer.get(l)))));
            this.rawIntBuffer.put(i1, Float.floatToRawIntBits((float)((float)(y + this.yOffset) + Float.intBitsToFloat((int)this.rawIntBuffer.get(i1)))));
            this.rawIntBuffer.put(j1, Float.floatToRawIntBits((float)((float)(z + this.zOffset) + Float.intBitsToFloat((int)this.rawIntBuffer.get(j1)))));
        }
    }

    public int getColorIndex(int vertexIndex) {
        return ((this.vertexCount - vertexIndex) * this.vertexFormat.getSize() + this.vertexFormat.getColorOffset()) / 4;
    }

    public void putColorMultiplier(float red, float green, float blue, int vertexIndex) {
        int i = this.getColorIndex(vertexIndex);
        int j = -1;
        if (!this.noColor) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                int k = (int)((float)(j & 0xFF) * red);
                int l = (int)((float)(j >> 8 & 0xFF) * green);
                int i1 = (int)((float)(j >> 16 & 0xFF) * blue);
                j &= 0xFF000000;
                j = j | i1 << 16 | l << 8 | k;
            } else {
                int j1 = (int)((float)(j >> 24 & 0xFF) * red);
                int k1 = (int)((float)(j >> 16 & 0xFF) * green);
                int l1 = (int)((float)(j >> 8 & 0xFF) * blue);
                j &= 0xFF;
                j = j | j1 << 24 | k1 << 16 | l1 << 8;
            }
        }
        this.rawIntBuffer.put(i, j);
    }

    private void putColor(int argb, int vertexIndex) {
        int i = this.getColorIndex(vertexIndex);
        int j = argb >> 16 & 0xFF;
        int k = argb >> 8 & 0xFF;
        int l = argb & 0xFF;
        this.putColorRGBA(i, j, k, l);
    }

    public void putColorRGB_F(float red, float green, float blue, int vertexIndex) {
        int i = this.getColorIndex(vertexIndex);
        int j = MathHelper.clamp((int)((int)(red * 255.0f)), (int)0, (int)255);
        int k = MathHelper.clamp((int)((int)(green * 255.0f)), (int)0, (int)255);
        int l = MathHelper.clamp((int)((int)(blue * 255.0f)), (int)0, (int)255);
        this.putColorRGBA(i, j, k, l);
    }

    public void putColorRGBA(int index, int red, int green, int blue) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, 0xFF000000 | blue << 16 | green << 8 | red);
        } else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | 0xFF);
        }
    }

    public void noColor() {
        this.noColor = true;
    }

    public BufferBuilder color(float red, float green, float blue, float alpha) {
        return this.color((int)(red * 255.0f), (int)(green * 255.0f), (int)(blue * 255.0f), (int)(alpha * 255.0f));
    }

    public BufferBuilder color(int red, int green, int blue, int alpha) {
        if (this.noColor) {
            return this;
        }
        int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)red / 255.0f);
                this.byteBuffer.putFloat(i + 4, (float)green / 255.0f);
                this.byteBuffer.putFloat(i + 8, (float)blue / 255.0f);
                this.byteBuffer.putFloat(i + 12, (float)alpha / 255.0f);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putFloat(i, (float)red);
                this.byteBuffer.putFloat(i + 4, (float)green);
                this.byteBuffer.putFloat(i + 8, (float)blue);
                this.byteBuffer.putFloat(i + 12, (float)alpha);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)red);
                this.byteBuffer.putShort(i + 2, (short)green);
                this.byteBuffer.putShort(i + 4, (short)blue);
                this.byteBuffer.putShort(i + 6, (short)alpha);
                break;
            }
            case 6: 
            case 7: {
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    this.byteBuffer.put(i, (byte)red);
                    this.byteBuffer.put(i + 1, (byte)green);
                    this.byteBuffer.put(i + 2, (byte)blue);
                    this.byteBuffer.put(i + 3, (byte)alpha);
                    break;
                }
                this.byteBuffer.put(i, (byte)alpha);
                this.byteBuffer.put(i + 1, (byte)blue);
                this.byteBuffer.put(i + 2, (byte)green);
                this.byteBuffer.put(i + 3, (byte)red);
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void addVertexData(int[] vertexData) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData((BufferBuilder)this, (int[])vertexData);
        }
        this.growBuffer(vertexData.length * 4 + this.vertexFormat.getSize());
        this.rawIntBuffer.position(this.getBufferSize());
        this.rawIntBuffer.put(vertexData);
        this.vertexCount += vertexData.length / this.vertexFormat.getIntegerSize();
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData((BufferBuilder)this);
        }
    }

    public void endVertex() {
        ++this.vertexCount;
        this.growBuffer(this.vertexFormat.getSize());
        this.vertexFormatIndex = 0;
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertex((BufferBuilder)this);
        }
    }

    public BufferBuilder pos(double x, double y, double z) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertex((BufferBuilder)this);
        }
        int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)(x + this.xOffset));
                this.byteBuffer.putFloat(i + 4, (float)(y + this.yOffset));
                this.byteBuffer.putFloat(i + 8, (float)(z + this.zOffset));
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, Float.floatToRawIntBits((float)((float)(x + this.xOffset))));
                this.byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)((float)(y + this.yOffset))));
                this.byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)((float)(z + this.zOffset))));
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)(x + this.xOffset));
                this.byteBuffer.putShort(i + 2, (short)(y + this.yOffset));
                this.byteBuffer.putShort(i + 4, (short)(z + this.zOffset));
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)(x + this.xOffset));
                this.byteBuffer.put(i + 1, (byte)(y + this.yOffset));
                this.byteBuffer.put(i + 2, (byte)(z + this.zOffset));
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void putNormal(float x, float y, float z) {
        int i = (byte)(x * 127.0f) & 0xFF;
        int j = (byte)(y * 127.0f) & 0xFF;
        int k = (byte)(z * 127.0f) & 0xFF;
        int l = i | j << 8 | k << 16;
        int i1 = this.vertexFormat.getSize() >> 2;
        int j1 = (this.vertexCount - 4) * i1 + this.vertexFormat.getNormalOffset() / 4;
        this.rawIntBuffer.put(j1, l);
        this.rawIntBuffer.put(j1 + i1, l);
        this.rawIntBuffer.put(j1 + i1 * 2, l);
        this.rawIntBuffer.put(j1 + i1 * 3, l);
    }

    private void nextVertexFormatIndex() {
        ++this.vertexFormatIndex;
        this.vertexFormatIndex %= this.vertexFormat.getElementCount();
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
            this.nextVertexFormatIndex();
        }
    }

    public BufferBuilder normal(float x, float y, float z) {
        int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, x);
                this.byteBuffer.putFloat(i + 4, y);
                this.byteBuffer.putFloat(i + 8, z);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, (int)x);
                this.byteBuffer.putInt(i + 4, (int)y);
                this.byteBuffer.putInt(i + 8, (int)z);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)((int)(x * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 2, (short)((int)(y * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 4, (short)((int)(z * 32767.0f) & 0xFFFF));
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)((int)(x * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 1, (byte)((int)(y * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 2, (byte)((int)(z * 127.0f) & 0xFF));
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void setTranslation(double x, double y, double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }

    public void finishDrawing() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        }
        this.isDrawing = false;
        this.byteBuffer.position(0);
        this.byteBuffer.limit(this.getBufferSize() * 4);
    }

    public ByteBuffer getByteBuffer() {
        if (this.modeTriangles) {
            return this.byteBufferTriangles;
        }
        return this.byteBuffer;
    }

    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }

    public int getVertexCount() {
        if (this.modeTriangles) {
            return this.vertexCount / 4 * 6;
        }
        return this.vertexCount;
    }

    public int getDrawMode() {
        if (this.modeTriangles) {
            return 4;
        }
        return this.drawMode;
    }

    public void putColor4(int argb) {
        for (int i = 0; i < 4; ++i) {
            this.putColor(argb, i + 1);
        }
    }

    public void putColorRGB_F4(float red, float green, float blue) {
        for (int i = 0; i < 4; ++i) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }
    }

    public void putSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && sprite.getAnimationIndex() >= 0) {
            this.animatedSprites.set(sprite.getAnimationIndex());
        }
        if (this.quadSprites != null) {
            int countQuads = this.vertexCount / 4;
            this.quadSprites[countQuads - 1] = sprite;
        }
    }

    public void setSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && sprite.getAnimationIndex() >= 0) {
            this.animatedSprites.set(sprite.getAnimationIndex());
        }
        if (this.quadSprites != null) {
            this.quadSprite = sprite;
        }
    }

    public boolean isMultiTexture() {
        return this.quadSprites != null;
    }

    public void drawMultiTexture() {
        if (this.quadSprites == null) {
            return;
        }
        int maxTextureIndex = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
        if (this.drawnIcons.length <= maxTextureIndex) {
            this.drawnIcons = new boolean[maxTextureIndex + 1];
        }
        Arrays.fill((boolean[])this.drawnIcons, (boolean)false);
        int texSwitch = 0;
        int grassOverlayIndex = -1;
        int countQuads = this.vertexCount / 4;
        for (int i = 0; i < countQuads; ++i) {
            int iconIndex;
            TextureAtlasSprite icon = this.quadSprites[i];
            if (icon == null || this.drawnIcons[iconIndex = icon.getIndexInMap()]) continue;
            if (icon == TextureUtils.iconGrassSideOverlay) {
                if (grassOverlayIndex >= 0) continue;
                grassOverlayIndex = i;
                continue;
            }
            i = this.drawForIcon(icon, i) - 1;
            ++texSwitch;
            if (this.blockLayer == BlockRenderLayer.TRANSLUCENT) continue;
            this.drawnIcons[iconIndex] = true;
        }
        if (grassOverlayIndex >= 0) {
            this.drawForIcon(TextureUtils.iconGrassSideOverlay, grassOverlayIndex);
            ++texSwitch;
        }
        if (texSwitch > 0) {
            // empty if block
        }
    }

    private int drawForIcon(TextureAtlasSprite sprite, int startQuadPos) {
        GL11.glBindTexture((int)3553, (int)sprite.glSpriteTextureId);
        int firstRegionEnd = -1;
        int lastPos = -1;
        int countQuads = this.vertexCount / 4;
        for (int i = startQuadPos; i < countQuads; ++i) {
            TextureAtlasSprite ts = this.quadSprites[i];
            if (ts == sprite) {
                if (lastPos >= 0) continue;
                lastPos = i;
                continue;
            }
            if (lastPos < 0) continue;
            this.draw(lastPos, i);
            if (this.blockLayer == BlockRenderLayer.TRANSLUCENT) {
                return i;
            }
            lastPos = -1;
            if (firstRegionEnd >= 0) continue;
            firstRegionEnd = i;
        }
        if (lastPos >= 0) {
            this.draw(lastPos, countQuads);
        }
        if (firstRegionEnd < 0) {
            firstRegionEnd = countQuads;
        }
        return firstRegionEnd;
    }

    private void draw(int startQuadVertex, int endQuadVertex) {
        int vxQuadCount = endQuadVertex - startQuadVertex;
        if (vxQuadCount <= 0) {
            return;
        }
        int startVertex = startQuadVertex * 4;
        int vxCount = vxQuadCount * 4;
        GL11.glDrawArrays((int)this.drawMode, (int)startVertex, (int)vxCount);
    }

    public void setBlockLayer(BlockRenderLayer blockLayer) {
        this.blockLayer = blockLayer;
        if (blockLayer == null) {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
            this.quadSprite = null;
        }
    }

    private int getBufferQuadSize() {
        int quadSize = this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.getIntegerSize() * 4);
        return quadSize;
    }

    public RenderEnv getRenderEnv(IBlockState blockStateIn, BlockPos blockPosIn) {
        if (this.renderEnv == null) {
            this.renderEnv = new RenderEnv(blockStateIn, blockPosIn);
            return this.renderEnv;
        }
        this.renderEnv.reset(blockStateIn, blockPosIn);
        return this.renderEnv;
    }

    public boolean isDrawing() {
        return this.isDrawing;
    }

    public double getXOffset() {
        return this.xOffset;
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public double getZOffset() {
        return this.zOffset;
    }

    public BlockRenderLayer getBlockLayer() {
        return this.blockLayer;
    }

    public void putColorMultiplierRgba(float red, float green, float blue, float alpha, int vertexIndex) {
        int index = this.getColorIndex(vertexIndex);
        int col = -1;
        if (!this.noColor) {
            col = this.rawIntBuffer.get(index);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                int r = (int)((float)(col & 0xFF) * red);
                int g = (int)((float)(col >> 8 & 0xFF) * green);
                int b = (int)((float)(col >> 16 & 0xFF) * blue);
                int a = (int)((float)(col >> 24 & 0xFF) * alpha);
                col = a << 24 | b << 16 | g << 8 | r;
            } else {
                int r = (int)((float)(col >> 24 & 0xFF) * red);
                int g = (int)((float)(col >> 16 & 0xFF) * green);
                int b = (int)((float)(col >> 8 & 0xFF) * blue);
                int a = (int)((float)(col & 0xFF) * alpha);
                col = r << 24 | g << 16 | b << 8 | a;
            }
        }
        this.rawIntBuffer.put(index, col);
    }

    public void quadsToTriangles() {
        if (this.drawMode != 7) {
            return;
        }
        if (this.byteBufferTriangles == null) {
            this.byteBufferTriangles = GLAllocation.createDirectByteBuffer((int)(this.byteBuffer.capacity() * 2));
        }
        if (this.byteBufferTriangles.capacity() < this.byteBuffer.capacity() * 2) {
            this.byteBufferTriangles = GLAllocation.createDirectByteBuffer((int)(this.byteBuffer.capacity() * 2));
        }
        int vertexSize = this.vertexFormat.getSize();
        int limit = this.byteBuffer.limit();
        this.byteBuffer.rewind();
        this.byteBufferTriangles.clear();
        for (int v = 0; v < this.vertexCount; v += 4) {
            this.byteBuffer.limit((v + 3) * vertexSize);
            this.byteBuffer.position(v * vertexSize);
            this.byteBufferTriangles.put(this.byteBuffer);
            this.byteBuffer.limit((v + 1) * vertexSize);
            this.byteBuffer.position(v * vertexSize);
            this.byteBufferTriangles.put(this.byteBuffer);
            this.byteBuffer.limit((v + 2 + 2) * vertexSize);
            this.byteBuffer.position((v + 2) * vertexSize);
            this.byteBufferTriangles.put(this.byteBuffer);
        }
        this.byteBuffer.limit(limit);
        this.byteBuffer.rewind();
        this.byteBufferTriangles.flip();
        this.modeTriangles = true;
    }

    public void putColorRGBA(int index, int red, int green, int blue, int alpha) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, alpha << 24 | blue << 16 | green << 8 | red);
        } else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | alpha);
        }
    }

    public boolean isColorDisabled() {
        return this.noColor;
    }

    public void putBulkData(ByteBuffer buffer) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData((BufferBuilder)this, (ByteBuffer)buffer);
        }
        this.growBuffer(buffer.limit() + this.vertexFormat.getSize());
        this.byteBuffer.position(this.vertexCount * this.vertexFormat.getSize());
        this.byteBuffer.put(buffer);
        this.vertexCount += buffer.limit() / this.vertexFormat.getSize();
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData((BufferBuilder)this);
        }
    }
}
