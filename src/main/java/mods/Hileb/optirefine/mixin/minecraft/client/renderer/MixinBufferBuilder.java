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
import net.minecraft.client.renderer.texture.TextureMap;
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

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_178999_b", deobf = true)
    public IntBuffer acc_rawIntBuffer;

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_179000_c", deobf = true)
    public FloatBuffer acc_rawFloatBuffer;

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_178997_d", deobf = true)
    public int acc_vertexCount;

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_179006_k", deobf = true)
    public int acc_drawMode;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private BlockRenderLayer blockLayer = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean[] drawnIcons = new boolean[256];
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private TextureAtlasSprite[] quadSprites = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private TextureAtlasSprite[] quadSpritesPrev = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private TextureAtlasSprite quadSprite = null;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private SVertexBuilder sVertexBuilder;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private RenderEnv renderEnv = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private BitSet animatedSprites = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private final BitSet animatedSpritesCached = new BitSet();
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean modeTriangles = false;
    @SuppressWarnings("AddedMixinMembersNamePattern")
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

    @SuppressWarnings("unused")
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
        return newBufferBuilder$State(AccessibleOperation.Construction.construction(), p_i46453_2_, p_i46453_3_, this.quadSprites == null ? null : this.quadSprites.clone());
    }

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.client.renderer.BufferBuilder$State ([ILnet.minecraft.client.renderer.vertex.VertexFormat;[Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;)V")
    private static native BufferBuilder.State newBufferBuilder$State(AccessibleOperation.Construction construction, int[] p_i46453_2_, VertexFormat p_i46453_3_, TextureAtlasSprite[] textureAtlasSprites);

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "func_181664_j", deobf = true, access = Opcodes.ACC_PUBLIC)
    private static native int acc_getBufferSize();

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "func_181665_a", deobf = true, access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    private static native float acc_getDistanceSq(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.BufferBuilder$State stateQuadSprites [Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
    private native static TextureAtlasSprite[] BufferBuilder$State_stateQuadSprites_get(BufferBuilder.State ins) ;

    @Inject(method = "setVertexState", at = @At("TAIL"))
    public void cacheVertexState(BufferBuilder.State state, CallbackInfo ci){
        if (BufferBuilder$State_stateQuadSprites_get(state) != null) {
            if (this.quadSprites == null) {
                this.quadSprites = this.quadSpritesPrev;
            }

            if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }

            TextureAtlasSprite[] src = BufferBuilder$State_stateQuadSprites_get(state);
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
            SVertexBuilder.endSetVertexFormat((BufferBuilder) (Object)this);
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

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleU (D)D")
    private static native double TextureAtlasSprite_toSingleU(TextureAtlasSprite textureAtlasSprite, double arg1);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleV (D)D")
    private static native double TextureAtlasSprite_toSingleV(TextureAtlasSprite textureAtlasSprite, double arg1);


    @WrapMethod(method = "tex")
    public BufferBuilder beforeTex(double u, double v, Operation<BufferBuilder> original){
        if (this.quadSprite != null && this.quadSprites != null) {
            u = TextureAtlasSprite_toSingleU(this.quadSprite, u);
            v = TextureAtlasSprite_toSingleV(this.quadSprite, v);
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    @Shadow
    private void putColor(int p_192836_1_, int p_192836_2_) {}

    @Shadow
    public void putColorRGB_F(float p_178994_1_, float p_178994_2_, float p_178994_3_, int p_178994_4_) {}

    @SuppressWarnings("unused")
    @Unique
    public void putColorRGB_F4(float red, float green, float blue) {
        for (int i = 0; i < 4; i++) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationIndex ()I")
    private static native int TextureAtlasSprite_getAnimationIndex(TextureAtlasSprite textureAtlasSprite);


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void putSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && TextureAtlasSprite_getAnimationIndex(sprite) >= 0) {
            this.animatedSprites.set(TextureAtlasSprite_getAnimationIndex(sprite));
        }

        if (this.quadSprites != null) {
            int countQuads = this.vertexCount / 4;
            this.quadSprites[countQuads - 1] = sprite;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && TextureAtlasSprite_getAnimationIndex(sprite) >= 0) {
            this.animatedSprites.set(TextureAtlasSprite_getAnimationIndex(sprite));
        }

        if (this.quadSprites != null) {
            this.quadSprite = sprite;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public boolean isMultiTexture() {
        return this.quadSprites != null;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountRegisteredSprites ()I")
    private static native int TextureMap_getCountRegisteredSprites(TextureMap textureMap) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getIndexInMap ()I")
    private static native int TextureAtlasSprite_getIndexInMap(TextureAtlasSprite textureAtlasSprite);


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void drawMultiTexture() {
        if (this.quadSprites != null) {
            int maxTextureIndex = TextureMap_getCountRegisteredSprites(Config.getMinecraft().getTextureMapBlocks());
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
                    int iconIndex = TextureAtlasSprite_getIndexInMap(icon);
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

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite glSpriteTextureId I")
    private static native int TextureAtlasSprite_glSpriteTextureId_get(TextureAtlasSprite textureAtlasSprite);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private int drawForIcon(TextureAtlasSprite sprite, int startQuadPos) {
        GL11.glBindTexture(3553, TextureAtlasSprite_glSpriteTextureId_get(sprite));
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private void draw(int startQuadVertex, int endQuadVertex) {
        int vxQuadCount = endQuadVertex - startQuadVertex;
        if (vxQuadCount > 0) {
            int startVertex = startQuadVertex * 4;
            int vxCount = vxQuadCount * 4;
            GL11.glDrawArrays(this.drawMode, startVertex, vxCount);
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private int getBufferQuadSize() {
        return this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.getIntegerSize() * 4);
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
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

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public boolean isDrawing() {
        return this.isDrawing;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public double getXOffset() {
        return this.xOffset;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public double getYOffset() {
        return this.yOffset;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public double getZOffset() {
        return this.zOffset;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public BlockRenderLayer getBlockLayer() {
        return this.blockLayer;
    }

    @Shadow
    private boolean noColor;

    @Shadow
    public int getColorIndex(int p_78909_1_) {return 0;}

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
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

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
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

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void putColorRGBA(int index, int red, int green, int blue, int alpha) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, alpha << 24 | blue << 16 | green << 8 | red);
        } else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | alpha);
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public boolean isColorDisabled() {
        return this.noColor;
    }

    @Shadow
    private void growBuffer(int p_181670_1_) {}

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
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
