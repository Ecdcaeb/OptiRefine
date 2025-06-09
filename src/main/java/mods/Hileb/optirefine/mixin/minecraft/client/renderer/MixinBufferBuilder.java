package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.optifine.SmartAnimations;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.util.TextureUtils;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.*;
import java.util.Arrays;
import java.util.BitSet;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder {

    @SuppressWarnings("all")
    @AccessTransformer(name = "field_178999_b", deobf = true)
    public IntBuffer acc_rawIntBuffer;

    @SuppressWarnings("all")
    @AccessTransformer(name = "field_179000_c", deobf = true)
    public FloatBuffer acc_rawFloatBuffer;

    @SuppressWarnings("all")
    @AccessTransformer(name = "field_178997_d", deobf = true)
    public int acc_vertexCount;

    @SuppressWarnings("all")
    @AccessTransformer(name = "field_179006_k", deobf = true)
    public int acc_drawMode;

    @Unique
    private BlockRenderLayer blockLayer = null;
    @Unique
    private boolean[] drawnIcons = new boolean[256];
    @Unique
    private TextureAtlasSprite[] quadSprites = null;
    @Unique
    private TextureAtlasSprite[] quadSpritesPrev = null;
    @Unique
    private TextureAtlasSprite quadSprite = null;
    @Unique @Public
    private SVertexBuilder sVertexBuilder;
    @Unique @Public
    private RenderEnv renderEnv = null;
    @Unique @Public
    private BitSet animatedSprites = null;
    @Unique @Public
    private final BitSet animatedSpritesCached = new BitSet();
    @Unique
    private boolean modeTriangles = false;
    @Unique
    private ByteBuffer byteBufferTriangles;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(int p_i46275_1_, CallbackInfo ci){
        SVertexBuilder.initVertexBuilder((BufferBuilder)(Object)this);
    }
    /*
    ((Buffer)this.rawShortBuffer).position(k << 1);
         if (this.quadSprites != null) {
            TextureAtlasSprite[] sprites = this.quadSprites;
            int quadSize = this.getBufferQuadSize();
            this.quadSprites = new TextureAtlasSprite[quadSize];
            System.arraycopy(sprites, 0, this.quadSprites, 0, Math.min(sprites.length, this.quadSprites.length));
            this.quadSpritesPrev = null;
         }
    * */
    @Redirect(method = "growBuffer", at = @At(value = "INVOKE", target = "Ljava/nio/ShortBuffer;position(I)Ljava/nio/ShortBuffer;"))
    public ShortBuffer savequadSprites(ShortBuffer instance, int newPosition){
        ShortBuffer buffer = instance.position(newPosition);
        if (this.quadSprites != null) {
            TextureAtlasSprite[] sprites = this.quadSprites;
            int quadSize = this.getBufferQuadSize();
            this.quadSprites = new TextureAtlasSprite[quadSize];
            System.arraycopy(sprites, 0, this.quadSprites, 0, Math.min(sprites.length, this.quadSprites.length));
            this.quadSpritesPrev = null;
        }
        return buffer;
    }

    @Shadow
    private int vertexCount;
    @Shadow
    private VertexFormat vertexFormat;

    @Inject(method = "sortVertexData", at = @At("TAIL"))
    public void afterSortVertexData(float p_181674_1_, float p_181674_2_, float p_181674_3_, CallbackInfo ci, @Local Integer[] ainteger){
        if (this.quadSprites != null) {
            TextureAtlasSprite[] quadSpritesSorted = new TextureAtlasSprite[this.vertexCount / 4];
            int quadStep = this.vertexFormat.getSize() / 4 * 4;

            for (int ix = 0; ix < ainteger.length; ix++) {
                int indexQuad = ainteger[ix];
                quadSpritesSorted[ix] = this.quadSprites[indexQuad];
            }
            System.arraycopy(quadSpritesSorted, 0, this.quadSprites, 0, quadSpritesSorted.length);
        }
    }

    @Redirect(method = "getVertexState", at = @At(value = "NEW", target = "(Lnet/minecraft/client/renderer/BufferBuilder;[ILnet/minecraft/client/renderer/vertex/VertexFormat;)Lnet/minecraft/client/renderer/BufferBuilder$State;"))
    public BufferBuilder.State getVertexStateReturn(BufferBuilder p_i46453_1_, int[] p_i46453_2_, VertexFormat p_i46453_3_){
        return newBufferBuilder$State(p_i46453_2_, p_i46453_3_, this.quadSprites == null ? null : this.quadSprites.clone());
    }

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.client.renderer.BufferBuilder$State ([ILnet.minecraft.client.renderer.vertex.VertexFormat;[Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;)V")
    private static BufferBuilder.State newBufferBuilder$State(int[] p_i46453_2_, VertexFormat p_i46453_3_, TextureAtlasSprite[] textureAtlasSprites){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("all")
    @AccessTransformer(name = "func_181664_j()", deobf = true)
    public int acc_getBufferSize(){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("all")
    @AccessTransformer(name = "func_181665_a", deobf = true)
    public static float acc_getDistanceSq(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_) {
        throw new AbstractMethodError();
    }

    @Inject(method = "setVertexState", at = @At("TAIL"))
    public void cacheVertexState(BufferBuilder.State p_178993_1_, CallbackInfo ci){
        if (state.stateQuadSprites != null) {
            if (this.quadSprites == null) {
                this.quadSprites = this.quadSpritesPrev;
            }

            if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }

            TextureAtlasSprite[] src = state.stateQuadSprites;
            System.arraycopy(src, 0, this.quadSprites, 0, src.length);
        } else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }

            this.quadSprites = null;
        }
    }

    @Inject(method = "reset", at = @At("TAIL"))
    public void afterRest(CallbackInfo ci){
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

    @Inject(method = "begin", at = @At(value = "INVOKE", target = "Ljava/nio/ByteBuffer;limit(I)Ljava/nio/ByteBuffer;", shift = At.Shift.AFTER))
    public void afterBegin(int p_181668_1_, VertexFormat p_181668_2_, CallbackInfo ci){
        if (Config.isShaders()) {
            SVertexBuilder.endSetVertexFormat(this);
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

    @WrapMethod(method = "tex")
    public BufferBuilder beforeTex(double u, double v, Operation<BufferBuilder> original){
        if (this.quadSprite != null && this.quadSprites != null) {
            u = this.quadSprite.toSingleU(u);
            v = this.quadSprite.toSingleV(v);
            this.quadSprites[this.vertexCount / 4] = this.quadSprite;
        }
        return original.call(u, v);
    }

    @Inject(method = "addVertexData", at = @At("HEAD"))
    public void beforeAddVertexData(int[] vertexData, CallbackInfo ci){
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData((BufferBuilder)(Object) this, vertexData);
        }
    }

    @Inject(method = "addVertexData", at = @At("RETURN"))
    public void afterAddVertexData(int[] vertexData, CallbackInfo ci){
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData((BufferBuilder)(Object)this);
        }
    }

    @Shadow
    private VertexFormatElement vertexFormatElement;
    @Shadow
    private int vertexFormatIndex;

    @Inject(method = "endVertex", at = @At("RETURN"))
    public void inject_endVertex(CallbackInfo ci) {
        this.vertexFormatIndex = 0;
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertex((BufferBuilder)(Object)this);
        }
    }

    @Inject(method = "pos", at = @At("HEAD"))
    public void beforePos(double p_181662_1_, double p_181662_3_, double p_181662_5_, CallbackInfoReturnable<BufferBuilder> cir){
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertex((BufferBuilder)(Object)this);
        }
    }

    @ModifyReturnValue(method = "getByteBuffer", at = @At("RETURN"))
    public ByteBuffer returnGetByteBuffer(ByteBuffer original){
        return this.modeTriangles ? this.byteBufferTriangles : original;
    }

    @ModifyReturnValue(method = "getDrawMode", at = @At("RETURN"))
    public int returnGetDrawMode(int original){
        return this.modeTriangles ? 4 : original;
    }

    @Shadow
    private void putColor(int p_192836_1_, int p_192836_2_) {}

    @Shadow
    public void putColorRGB_F(float p_178994_1_, float p_178994_2_, float p_178994_3_, int p_178994_4_) {}

    @Unique
    public void putColorRGB_F4(float red, float green, float blue) {
        for (int i = 0; i < 4; i++) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }
    }

    @Unique
    public void putSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && sprite.getAnimationIndex() >= 0) {
            this.animatedSprites.set(sprite.getAnimationIndex());
        }

        if (this.quadSprites != null) {
            int countQuads = this.vertexCount / 4;
            this.quadSprites[countQuads - 1] = sprite;
        }
    }

    @Unique
    public void setSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && sprite.getAnimationIndex() >= 0) {
            this.animatedSprites.set(sprite.getAnimationIndex());
        }

        if (this.quadSprites != null) {
            this.quadSprite = sprite;
        }
    }

    @Unique
    public boolean isMultiTexture() {
        return this.quadSprites != null;
    }

    @Unique
    public void drawMultiTexture() {
        if (this.quadSprites != null) {
            int maxTextureIndex = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
            if (this.drawnIcons.length <= maxTextureIndex) {
                this.drawnIcons = new boolean[maxTextureIndex + 1];
            }

            Arrays.fill(this.drawnIcons, false);
            int texSwitch = 0;
            int grassOverlayIndex = -1;
            int countQuads = this.vertexCount / 4;

            for (int i = 0; i < countQuads; i++) {
                TextureAtlasSprite icon = this.quadSprites[i];
                if (icon != null) {
                    int iconIndex = icon.getIndexInMap();
                    if (!this.drawnIcons[iconIndex]) {
                        if (icon == TextureUtils.iconGrassSideOverlay) {
                            if (grassOverlayIndex < 0) {
                                grassOverlayIndex = i;
                            }
                        } else {
                            i = this.drawForIcon(icon, i) - 1;
                            texSwitch++;
                            if (this.blockLayer != BlockRenderLayer.TRANSLUCENT) {
                                this.drawnIcons[iconIndex] = true;
                            }
                        }
                    }
                }
            }

            if (grassOverlayIndex >= 0) {
                this.drawForIcon(TextureUtils.iconGrassSideOverlay, grassOverlayIndex);
                texSwitch++;
            }

            if (texSwitch > 0) {
            }
        }
    }

    @Unique
    private int drawForIcon(TextureAtlasSprite sprite, int startQuadPos) {
        GL11.glBindTexture(3553, sprite.glSpriteTextureId);
        int firstRegionEnd = -1;
        int lastPos = -1;
        int countQuads = this.vertexCount / 4;

        for (int i = startQuadPos; i < countQuads; i++) {
            TextureAtlasSprite ts = this.quadSprites[i];
            if (ts == sprite) {
                if (lastPos < 0) {
                    lastPos = i;
                }
            } else if (lastPos >= 0) {
                this.draw(lastPos, i);
                if (this.blockLayer == BlockRenderLayer.TRANSLUCENT) {
                    return i;
                }

                lastPos = -1;
                if (firstRegionEnd < 0) {
                    firstRegionEnd = i;
                }
            }
        }

        if (lastPos >= 0) {
            this.draw(lastPos, countQuads);
        }

        if (firstRegionEnd < 0) {
            firstRegionEnd = countQuads;
        }

        return firstRegionEnd;
    }

    @Shadow
    private int drawMode;

    @Unique
    private void draw(int startQuadVertex, int endQuadVertex) {
        int vxQuadCount = endQuadVertex - startQuadVertex;
        if (vxQuadCount > 0) {
            int startVertex = startQuadVertex * 4;
            int vxCount = vxQuadCount * 4;
            GL11.glDrawArrays(this.drawMode, startVertex, vxCount);
        }
    }

    @Unique
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

    @Shadow
    private IntBuffer rawIntBuffer;

    @Unique
    private int getBufferQuadSize() {
        return this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.getIntegerSize() * 4);
    }

    @Unique
    public RenderEnv getRenderEnv(IBlockState blockStateIn, BlockPos blockPosIn) {
        if (this.renderEnv == null) {
            this.renderEnv = new RenderEnv(blockStateIn, blockPosIn);
            return this.renderEnv;
        } else {
            this.renderEnv.reset(blockStateIn, blockPosIn);
            return this.renderEnv;
        }
    }

    @Shadow
    private double xOffset;
    @Shadow
    private double yOffset;
    @Shadow
    private double zOffset;
    @Shadow
    private boolean isDrawing;

    @Unique
    public boolean isDrawing() {
        return this.isDrawing;
    }

    @Unique
    public double getXOffset() {
        return this.xOffset;
    }

    @Unique
    public double getYOffset() {
        return this.yOffset;
    }

    @Unique
    public double getZOffset() {
        return this.zOffset;
    }

    @Unique
    public BlockRenderLayer getBlockLayer() {
        return this.blockLayer;
    }

    @Shadow
    private boolean noColor;

    @Shadow
    public int getColorIndex(int p_78909_1_) {return 0;}

    @Unique
    public void putColorMultiplierRgba(float red, float green, float blue, float alpha, int vertexIndex) {
        int index = this.getColorIndex(vertexIndex);
        int col = -1;
        if (!this.noColor) {
            col = this.rawIntBuffer.get(index);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                int r = (int)((col & 0xFF) * red);
                int g = (int)((col >> 8 & 0xFF) * green);
                int b = (int)((col >> 16 & 0xFF) * blue);
                int a = (int)((col >> 24 & 0xFF) * alpha);
                col = a << 24 | b << 16 | g << 8 | r;
            } else {
                int r = (int)((col >> 24 & 0xFF) * red);
                int g = (int)((col >> 16 & 0xFF) * green);
                int b = (int)((col >> 8 & 0xFF) * blue);
                int a = (int)((col & 0xFF) * alpha);
                col = r << 24 | g << 16 | b << 8 | a;
            }
        }

        this.rawIntBuffer.put(index, col);
    }

    @Shadow
    private ByteBuffer byteBuffer;

    @Unique
    public void quadsToTriangles() {
        if (this.drawMode == 7) {
            if (this.byteBufferTriangles == null) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
            }

            if (this.byteBufferTriangles.capacity() < this.byteBuffer.capacity() * 2) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
            }

            int vertexSize = this.vertexFormat.getSize();
            int limit = this.byteBuffer.limit();
            ((Buffer)this.byteBuffer).rewind();
            ((Buffer)this.byteBufferTriangles).clear();

            for (int v = 0; v < this.vertexCount; v += 4) {
                ((Buffer)this.byteBuffer).limit((v + 3) * vertexSize);
                ((Buffer)this.byteBuffer).position(v * vertexSize);
                this.byteBufferTriangles.put(this.byteBuffer);
                ((Buffer)this.byteBuffer).limit((v + 1) * vertexSize);
                ((Buffer)this.byteBuffer).position(v * vertexSize);
                this.byteBufferTriangles.put(this.byteBuffer);
                ((Buffer)this.byteBuffer).limit((v + 2 + 2) * vertexSize);
                ((Buffer)this.byteBuffer).position((v + 2) * vertexSize);
                this.byteBufferTriangles.put(this.byteBuffer);
            }

            ((Buffer)this.byteBuffer).limit(limit);
            ((Buffer)this.byteBuffer).rewind();
            ((Buffer)this.byteBufferTriangles).flip();
            this.modeTriangles = true;
        }
    }

    @Unique
    public void putColorRGBA(int index, int red, int green, int blue, int alpha) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, alpha << 24 | blue << 16 | green << 8 | red);
        } else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | alpha);
        }
    }

    @Unique
    public boolean isColorDisabled() {
        return this.noColor;
    }

    @Shadow
    private void growBuffer(int p_181670_1_) {}

    @Unique
    public void putBulkData(ByteBuffer buffer) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData((BufferBuilder) (Object)this, buffer);
        }

        this.growBuffer(buffer.limit() + this.vertexFormat.getSize());
        ((Buffer)this.byteBuffer).position(this.vertexCount * this.vertexFormat.getSize());
        this.byteBuffer.put(buffer);
        this.vertexCount = this.vertexCount + buffer.limit() / this.vertexFormat.getSize();
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData((BufferBuilder) (Object)this);
        }
    }



}
