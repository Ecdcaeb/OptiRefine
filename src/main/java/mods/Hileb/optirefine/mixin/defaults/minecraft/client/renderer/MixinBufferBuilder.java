package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

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
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleU (F)F")
    private static native float TextureAtlasSprite_toSingleU(TextureAtlasSprite textureAtlasSprite, float arg1);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleV (F)F")
    private static native float TextureAtlasSprite_toSingleV(TextureAtlasSprite textureAtlasSprite, float arg1);


    @WrapMethod(method = "tex")
    public BufferBuilder beforeTex(double u, double v, Operation<BufferBuilder> original){
        if (this.quadSprite != null && this.quadSprites != null) {
            u = TextureAtlasSprite_toSingleU(this.quadSprite, (float) u);
            v = TextureAtlasSprite_toSingleV(this.quadSprite, (float) v);
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

/*
--- net/minecraft/client/renderer/BufferBuilder.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/BufferBuilder.java	Tue Aug 19 14:59:58 2025
@@ -7,40 +7,62 @@
 import java.nio.FloatBuffer;
 import java.nio.IntBuffer;
 import java.nio.ShortBuffer;
 import java.util.Arrays;
 import java.util.BitSet;
 import java.util.Comparator;
+import net.minecraft.block.state.IBlockState;
+import net.minecraft.client.renderer.texture.TextureAtlasSprite;
 import net.minecraft.client.renderer.vertex.VertexFormat;
 import net.minecraft.client.renderer.vertex.VertexFormatElement;
+import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
+import net.minecraft.util.BlockRenderLayer;
+import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
+import net.optifine.SmartAnimations;
+import net.optifine.render.RenderEnv;
+import net.optifine.shaders.SVertexBuilder;
+import net.optifine.util.TextureUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
+import org.lwjgl.opengl.GL11;

 public class BufferBuilder {
    private static final Logger LOGGER = LogManager.getLogger();
    private ByteBuffer byteBuffer;
-   private IntBuffer rawIntBuffer;
+   public IntBuffer rawIntBuffer;
    private ShortBuffer rawShortBuffer;
-   private FloatBuffer rawFloatBuffer;
-   private int vertexCount;
+   public FloatBuffer rawFloatBuffer;
+   public int vertexCount;
    private VertexFormatElement vertexFormatElement;
    private int vertexFormatIndex;
    private boolean noColor;
-   private int drawMode;
+   public int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
+   private BlockRenderLayer blockLayer = null;
+   private boolean[] drawnIcons = new boolean[256];
+   private TextureAtlasSprite[] quadSprites = null;
+   private TextureAtlasSprite[] quadSpritesPrev = null;
+   private TextureAtlasSprite quadSprite = null;
+   public SVertexBuilder sVertexBuilder;
+   public RenderEnv renderEnv = null;
+   public BitSet animatedSprites = null;
+   public BitSet animatedSpritesCached = new BitSet();
+   private boolean modeTriangles = false;
+   private ByteBuffer byteBufferTriangles;

    public BufferBuilder(int var1) {
       this.byteBuffer = GLAllocation.createDirectByteBuffer(var1 * 4);
       this.rawIntBuffer = this.byteBuffer.asIntBuffer();
       this.rawShortBuffer = this.byteBuffer.asShortBuffer();
       this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
+      SVertexBuilder.initVertexBuilder(this);
    }

    private void growBuffer(int var1) {
       if (MathHelper.roundUp(var1, 4) / 4 > this.rawIntBuffer.remaining() || this.vertexCount * this.vertexFormat.getSize() + var1 > this.byteBuffer.capacity()
          )
        {
@@ -50,17 +72,24 @@
          int var4 = this.rawIntBuffer.position();
          ByteBuffer var5 = GLAllocation.createDirectByteBuffer(var3);
          ((Buffer)this.byteBuffer).position(0);
          var5.put(this.byteBuffer);
          ((Buffer)var5).rewind();
          this.byteBuffer = var5;
-         this.rawFloatBuffer = this.byteBuffer.asFloatBuffer().asReadOnlyBuffer();
+         this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
          this.rawIntBuffer = this.byteBuffer.asIntBuffer();
          ((Buffer)this.rawIntBuffer).position(var4);
          this.rawShortBuffer = this.byteBuffer.asShortBuffer();
          ((Buffer)this.rawShortBuffer).position(var4 << 1);
+         if (this.quadSprites != null) {
+            TextureAtlasSprite[] var6 = this.quadSprites;
+            int var7 = this.getBufferQuadSize();
+            this.quadSprites = new TextureAtlasSprite[var7];
+            System.arraycopy(var6, 0, this.quadSprites, 0, Math.min(var6.length, this.quadSprites.length));
+            this.quadSpritesPrev = null;
+         }
       }
    }

    public void sortVertexData(float var1, float var2, float var3) {
       int var4 = this.vertexCount / 4;
       final float[] var5 = new float[var4];
@@ -114,26 +143,47 @@
             ((Buffer)this.rawIntBuffer).position(var10 * var8);
             this.rawIntBuffer.put(var9);
          }

          var16.set(var10);
       }
+
+      ((Buffer)this.rawIntBuffer).limit(this.rawIntBuffer.capacity());
+      ((Buffer)this.rawIntBuffer).position(this.getBufferSize());
+      if (this.quadSprites != null) {
+         TextureAtlasSprite[] var17 = new TextureAtlasSprite[this.vertexCount / 4];
+         int var18 = this.vertexFormat.getSize() / 4 * 4;
+
+         for (int var19 = 0; var19 < var15.length; var19++) {
+            int var20 = var15[var19];
+            var17[var19] = this.quadSprites[var20];
+         }
+
+         System.arraycopy(var17, 0, this.quadSprites, 0, var17.length);
+      }
    }

    public BufferBuilder.State getVertexState() {
       ((Buffer)this.rawIntBuffer).rewind();
       int var1 = this.getBufferSize();
       ((Buffer)this.rawIntBuffer).limit(var1);
       int[] var2 = new int[var1];
       this.rawIntBuffer.get(var2);
       ((Buffer)this.rawIntBuffer).limit(this.rawIntBuffer.capacity());
       ((Buffer)this.rawIntBuffer).position(var1);
-      return new BufferBuilder.State(var2, new VertexFormat(this.vertexFormat));
+      TextureAtlasSprite[] var3 = null;
+      if (this.quadSprites != null) {
+         int var4 = this.vertexCount / 4;
+         var3 = new TextureAtlasSprite[var4];
+         System.arraycopy(this.quadSprites, 0, var3, 0, var4);
+      }
+
+      return new BufferBuilder.State(var2, new VertexFormat(this.vertexFormat), var3);
    }

-   private int getBufferSize() {
+   public int getBufferSize() {
       return this.vertexCount * this.vertexFormat.getIntegerSize();
    }

    private static float getDistanceSq(FloatBuffer var0, float var1, float var2, float var3, int var4, int var5) {
       float var6 = var0.get(var5 + var4 * 0 + 0);
       float var7 = var0.get(var5 + var4 * 0 + 1);
@@ -156,18 +206,48 @@
    public void setVertexState(BufferBuilder.State var1) {
       ((Buffer)this.rawIntBuffer).clear();
       this.growBuffer(var1.getRawBuffer().length * 4);
       this.rawIntBuffer.put(var1.getRawBuffer());
       this.vertexCount = var1.getVertexCount();
       this.vertexFormat = new VertexFormat(var1.getVertexFormat());
+      if (var1.stateQuadSprites != null) {
+         if (this.quadSprites == null) {
+            this.quadSprites = this.quadSpritesPrev;
+         }
+
+         if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
+            this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
+         }
+
+         TextureAtlasSprite[] var2 = var1.stateQuadSprites;
+         System.arraycopy(var2, 0, this.quadSprites, 0, var2.length);
+      } else {
+         if (this.quadSprites != null) {
+            this.quadSpritesPrev = this.quadSprites;
+         }
+
+         this.quadSprites = null;
+      }
    }

    public void reset() {
       this.vertexCount = 0;
       this.vertexFormatElement = null;
       this.vertexFormatIndex = 0;
+      this.quadSprite = null;
+      if (SmartAnimations.isActive()) {
+         if (this.animatedSprites == null) {
+            this.animatedSprites = this.animatedSpritesCached;
+         }
+
+         this.animatedSprites.clear();
+      } else if (this.animatedSprites != null) {
+         this.animatedSprites = null;
+      }
+
+      this.modeTriangles = false;
    }

    public void begin(int var1, VertexFormat var2) {
       if (this.isDrawing) {
          throw new IllegalStateException("Already building!");
       } else {
@@ -175,16 +255,43 @@
          this.reset();
          this.drawMode = var1;
          this.vertexFormat = var2;
          this.vertexFormatElement = var2.getElement(this.vertexFormatIndex);
          this.noColor = false;
          ((Buffer)this.byteBuffer).limit(this.byteBuffer.capacity());
+         if (Config.isShaders()) {
+            SVertexBuilder.endSetVertexFormat(this);
+         }
+
+         if (Config.isMultiTexture()) {
+            if (this.blockLayer != null) {
+               if (this.quadSprites == null) {
+                  this.quadSprites = this.quadSpritesPrev;
+               }
+
+               if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
+                  this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
+               }
+            }
+         } else {
+            if (this.quadSprites != null) {
+               this.quadSpritesPrev = this.quadSprites;
+            }
+
+            this.quadSprites = null;
+         }
       }
    }

    public BufferBuilder tex(double var1, double var3) {
+      if (this.quadSprite != null && this.quadSprites != null) {
+         var1 = this.quadSprite.toSingleU((float)var1);
+         var3 = this.quadSprite.toSingleV((float)var3);
+         this.quadSprites[this.vertexCount / 4] = this.quadSprite;
+      }
+
       int var5 = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
       switch (this.vertexFormatElement.getType()) {
          case FLOAT:
             this.byteBuffer.putFloat(var5, (float)var1);
             this.byteBuffer.putFloat(var5 + 4, (float)var3);
             break;
@@ -255,13 +362,13 @@
          this.rawIntBuffer.put(var10, Float.floatToRawIntBits((float)(var1 + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var10))));
          this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float)(var3 + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var11))));
          this.rawIntBuffer.put(var12, Float.floatToRawIntBits((float)(var5 + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var12))));
       }
    }

-   private int getColorIndex(int var1) {
+   public int getColorIndex(int var1) {
       return ((this.vertexCount - var1) * this.vertexFormat.getSize() + this.vertexFormat.getColorOffset()) / 4;
    }

    public void putColorMultiplier(float var1, float var2, float var3, int var4) {
       int var5 = this.getColorIndex(var4);
       int var6 = -1;
@@ -269,19 +376,19 @@
          var6 = this.rawIntBuffer.get(var5);
          if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
             int var7 = (int)((var6 & 0xFF) * var1);
             int var8 = (int)((var6 >> 8 & 0xFF) * var2);
             int var9 = (int)((var6 >> 16 & 0xFF) * var3);
             var6 &= -16777216;
-            var6 |= var9 << 16 | var8 << 8 | var7;
+            var6 = var6 | var9 << 16 | var8 << 8 | var7;
          } else {
             int var13 = (int)((var6 >> 24 & 0xFF) * var1);
             int var14 = (int)((var6 >> 16 & 0xFF) * var2);
             int var15 = (int)((var6 >> 8 & 0xFF) * var3);
             var6 &= 255;
-            var6 |= var13 << 24 | var14 << 16 | var15 << 8;
+            var6 = var6 | var13 << 24 | var14 << 16 | var15 << 8;
          }
       }

       this.rawIntBuffer.put(var5, var6);
    }

@@ -298,13 +405,13 @@
       int var6 = MathHelper.clamp((int)(var1 * 255.0F), 0, 255);
       int var7 = MathHelper.clamp((int)(var2 * 255.0F), 0, 255);
       int var8 = MathHelper.clamp((int)(var3 * 255.0F), 0, 255);
       this.putColorRGBA(var5, var6, var7, var8);
    }

-   private void putColorRGBA(int var1, int var2, int var3, int var4) {
+   public void putColorRGBA(int var1, int var2, int var3, int var4) {
       if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
          this.rawIntBuffer.put(var1, 0xFF000000 | var4 << 16 | var3 << 8 | var2);
       } else {
          this.rawIntBuffer.put(var1, var2 << 24 | var3 << 16 | var4 << 8 | 0xFF);
       }
    }
@@ -361,24 +468,40 @@
          this.nextVertexFormatIndex();
          return this;
       }
    }

    public void addVertexData(int[] var1) {
-      this.growBuffer(var1.length * 4);
+      if (Config.isShaders()) {
+         SVertexBuilder.beginAddVertexData(this, var1);
+      }
+
+      this.growBuffer(var1.length * 4 + this.vertexFormat.getSize());
       ((Buffer)this.rawIntBuffer).position(this.getBufferSize());
       this.rawIntBuffer.put(var1);
       this.vertexCount = this.vertexCount + var1.length / this.vertexFormat.getIntegerSize();
+      if (Config.isShaders()) {
+         SVertexBuilder.endAddVertexData(this);
+      }
    }

    public void endVertex() {
       this.vertexCount++;
       this.growBuffer(this.vertexFormat.getSize());
+      this.vertexFormatIndex = 0;
+      this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
+      if (Config.isShaders()) {
+         SVertexBuilder.endAddVertex(this);
+      }
    }

    public BufferBuilder pos(double var1, double var3, double var5) {
+      if (Config.isShaders()) {
+         SVertexBuilder.beginAddVertex(this);
+      }
+
       int var7 = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
       switch (this.vertexFormatElement.getType()) {
          case FLOAT:
             this.byteBuffer.putFloat(var7, (float)(var1 + this.xOffset));
             this.byteBuffer.putFloat(var7 + 4, (float)(var3 + this.yOffset));
             this.byteBuffer.putFloat(var7 + 8, (float)(var5 + this.zOffset));
@@ -420,13 +543,13 @@
    }

    private void nextVertexFormatIndex() {
       this.vertexFormatIndex++;
       this.vertexFormatIndex = this.vertexFormatIndex % this.vertexFormat.getElementCount();
       this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
-      if (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
+      if (this.vertexFormatElement.getUsage() == EnumUsage.PADDING) {
          this.nextVertexFormatIndex();
       }
    }

    public BufferBuilder normal(float var1, float var2, float var3) {
       int var4 = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
@@ -441,21 +564,21 @@
             this.byteBuffer.putInt(var4, (int)var1);
             this.byteBuffer.putInt(var4 + 4, (int)var2);
             this.byteBuffer.putInt(var4 + 8, (int)var3);
             break;
          case USHORT:
          case SHORT:
-            this.byteBuffer.putShort(var4, (short)((int)var1 * 32767 & 65535));
-            this.byteBuffer.putShort(var4 + 2, (short)((int)var2 * 32767 & 65535));
-            this.byteBuffer.putShort(var4 + 4, (short)((int)var3 * 32767 & 65535));
+            this.byteBuffer.putShort(var4, (short)((int)(var1 * 32767.0F) & 65535));
+            this.byteBuffer.putShort(var4 + 2, (short)((int)(var2 * 32767.0F) & 65535));
+            this.byteBuffer.putShort(var4 + 4, (short)((int)(var3 * 32767.0F) & 65535));
             break;
          case UBYTE:
          case BYTE:
-            this.byteBuffer.put(var4, (byte)((int)var1 * 127 & 0xFF));
-            this.byteBuffer.put(var4 + 1, (byte)((int)var2 * 127 & 0xFF));
-            this.byteBuffer.put(var4 + 2, (byte)((int)var3 * 127 & 0xFF));
+            this.byteBuffer.put(var4, (byte)((int)(var1 * 127.0F) & 0xFF));
+            this.byteBuffer.put(var4 + 1, (byte)((int)(var2 * 127.0F) & 0xFF));
+            this.byteBuffer.put(var4 + 2, (byte)((int)(var3 * 127.0F) & 0xFF));
       }

       this.nextVertexFormatIndex();
       return this;
    }

@@ -473,25 +596,25 @@
          ((Buffer)this.byteBuffer).position(0);
          ((Buffer)this.byteBuffer).limit(this.getBufferSize() * 4);
       }
    }

    public ByteBuffer getByteBuffer() {
-      return this.byteBuffer;
+      return this.modeTriangles ? this.byteBufferTriangles : this.byteBuffer;
    }

    public VertexFormat getVertexFormat() {
       return this.vertexFormat;
    }

    public int getVertexCount() {
-      return this.vertexCount;
+      return this.modeTriangles ? this.vertexCount / 4 * 6 : this.vertexCount;
    }

    public int getDrawMode() {
-      return this.drawMode;
+      return this.modeTriangles ? 4 : this.drawMode;
    }

    public void putColor4(int var1) {
       for (int var2 = 0; var2 < 4; var2++) {
          this.putColor(var1, var2 + 1);
       }
@@ -500,15 +623,263 @@
    public void putColorRGB_F4(float var1, float var2, float var3) {
       for (int var4 = 0; var4 < 4; var4++) {
          this.putColorRGB_F(var1, var2, var3, var4 + 1);
       }
    }

+   public void putSprite(TextureAtlasSprite var1) {
+      if (this.animatedSprites != null && var1 != null && var1.getAnimationIndex() >= 0) {
+         this.animatedSprites.set(var1.getAnimationIndex());
+      }
+
+      if (this.quadSprites != null) {
+         int var2 = this.vertexCount / 4;
+         this.quadSprites[var2 - 1] = var1;
+      }
+   }
+
+   public void setSprite(TextureAtlasSprite var1) {
+      if (this.animatedSprites != null && var1 != null && var1.getAnimationIndex() >= 0) {
+         this.animatedSprites.set(var1.getAnimationIndex());
+      }
+
+      if (this.quadSprites != null) {
+         this.quadSprite = var1;
+      }
+   }
+
+   public boolean isMultiTexture() {
+      return this.quadSprites != null;
+   }
+
+   public void drawMultiTexture() {
+      if (this.quadSprites != null) {
+         int var1 = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
+         if (this.drawnIcons.length <= var1) {
+            this.drawnIcons = new boolean[var1 + 1];
+         }
+
+         Arrays.fill(this.drawnIcons, false);
+         int var2 = 0;
+         int var3 = -1;
+         int var4 = this.vertexCount / 4;
+
+         for (int var5 = 0; var5 < var4; var5++) {
+            TextureAtlasSprite var6 = this.quadSprites[var5];
+            if (var6 != null) {
+               int var7 = var6.getIndexInMap();
+               if (!this.drawnIcons[var7]) {
+                  if (var6 == TextureUtils.iconGrassSideOverlay) {
+                     if (var3 < 0) {
+                        var3 = var5;
+                     }
+                  } else {
+                     var5 = this.drawForIcon(var6, var5) - 1;
+                     var2++;
+                     if (this.blockLayer != BlockRenderLayer.TRANSLUCENT) {
+                        this.drawnIcons[var7] = true;
+                     }
+                  }
+               }
+            }
+         }
+
+         if (var3 >= 0) {
+            this.drawForIcon(TextureUtils.iconGrassSideOverlay, var3);
+            var2++;
+         }
+
+         if (var2 > 0) {
+         }
+      }
+   }
+
+   private int drawForIcon(TextureAtlasSprite var1, int var2) {
+      GL11.glBindTexture(3553, var1.glSpriteTextureId);
+      int var3 = -1;
+      int var4 = -1;
+      int var5 = this.vertexCount / 4;
+
+      for (int var6 = var2; var6 < var5; var6++) {
+         TextureAtlasSprite var7 = this.quadSprites[var6];
+         if (var7 == var1) {
+            if (var4 < 0) {
+               var4 = var6;
+            }
+         } else if (var4 >= 0) {
+            this.draw(var4, var6);
+            if (this.blockLayer == BlockRenderLayer.TRANSLUCENT) {
+               return var6;
+            }
+
+            var4 = -1;
+            if (var3 < 0) {
+               var3 = var6;
+            }
+         }
+      }
+
+      if (var4 >= 0) {
+         this.draw(var4, var5);
+      }
+
+      if (var3 < 0) {
+         var3 = var5;
+      }
+
+      return var3;
+   }
+
+   private void draw(int var1, int var2) {
+      int var3 = var2 - var1;
+      if (var3 > 0) {
+         int var4 = var1 * 4;
+         int var5 = var3 * 4;
+         GL11.glDrawArrays(this.drawMode, var4, var5);
+      }
+   }
+
+   public void setBlockLayer(BlockRenderLayer var1) {
+      this.blockLayer = var1;
+      if (var1 == null) {
+         if (this.quadSprites != null) {
+            this.quadSpritesPrev = this.quadSprites;
+         }
+
+         this.quadSprites = null;
+         this.quadSprite = null;
+      }
+   }
+
+   private int getBufferQuadSize() {
+      return this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.getIntegerSize() * 4);
+   }
+
+   public RenderEnv getRenderEnv(IBlockState var1, BlockPos var2) {
+      if (this.renderEnv == null) {
+         this.renderEnv = new RenderEnv(var1, var2);
+         return this.renderEnv;
+      } else {
+         this.renderEnv.reset(var1, var2);
+         return this.renderEnv;
+      }
+   }
+
+   public boolean isDrawing() {
+      return this.isDrawing;
+   }
+
+   public double getXOffset() {
+      return this.xOffset;
+   }
+
+   public double getYOffset() {
+      return this.yOffset;
+   }
+
+   public double getZOffset() {
+      return this.zOffset;
+   }
+
+   public BlockRenderLayer getBlockLayer() {
+      return this.blockLayer;
+   }
+
+   public void putColorMultiplierRgba(float var1, float var2, float var3, float var4, int var5) {
+      int var6 = this.getColorIndex(var5);
+      int var7 = -1;
+      if (!this.noColor) {
+         var7 = this.rawIntBuffer.get(var6);
+         if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
+            int var8 = (int)((var7 & 0xFF) * var1);
+            int var9 = (int)((var7 >> 8 & 0xFF) * var2);
+            int var10 = (int)((var7 >> 16 & 0xFF) * var3);
+            int var11 = (int)((var7 >> 24 & 0xFF) * var4);
+            var7 = var11 << 24 | var10 << 16 | var9 << 8 | var8;
+         } else {
+            int var13 = (int)((var7 >> 24 & 0xFF) * var1);
+            int var14 = (int)((var7 >> 16 & 0xFF) * var2);
+            int var15 = (int)((var7 >> 8 & 0xFF) * var3);
+            int var16 = (int)((var7 & 0xFF) * var4);
+            var7 = var13 << 24 | var14 << 16 | var15 << 8 | var16;
+         }
+      }
+
+      this.rawIntBuffer.put(var6, var7);
+   }
+
+   public void quadsToTriangles() {
+      if (this.drawMode == 7) {
+         if (this.byteBufferTriangles == null) {
+            this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
+         }
+
+         if (this.byteBufferTriangles.capacity() < this.byteBuffer.capacity() * 2) {
+            this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
+         }
+
+         int var1 = this.vertexFormat.getSize();
+         int var2 = this.byteBuffer.limit();
+         ((Buffer)this.byteBuffer).rewind();
+         ((Buffer)this.byteBufferTriangles).clear();
+
+         for (byte var3 = 0; var3 < this.vertexCount; var3 += 4) {
+            ((Buffer)this.byteBuffer).limit((var3 + 3) * var1);
+            ((Buffer)this.byteBuffer).position(var3 * var1);
+            this.byteBufferTriangles.put(this.byteBuffer);
+            ((Buffer)this.byteBuffer).limit((var3 + 1) * var1);
+            ((Buffer)this.byteBuffer).position(var3 * var1);
+            this.byteBufferTriangles.put(this.byteBuffer);
+            ((Buffer)this.byteBuffer).limit((var3 + 2 + 2) * var1);
+            ((Buffer)this.byteBuffer).position((var3 + 2) * var1);
+            this.byteBufferTriangles.put(this.byteBuffer);
+         }
+
+         ((Buffer)this.byteBuffer).limit(var2);
+         ((Buffer)this.byteBuffer).rewind();
+         ((Buffer)this.byteBufferTriangles).flip();
+         this.modeTriangles = true;
+      }
+   }
+
+   public void putColorRGBA(int var1, int var2, int var3, int var4, int var5) {
+      if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
+         this.rawIntBuffer.put(var1, var5 << 24 | var4 << 16 | var3 << 8 | var2);
+      } else {
+         this.rawIntBuffer.put(var1, var2 << 24 | var3 << 16 | var4 << 8 | var5);
+      }
+   }
+
+   public boolean isColorDisabled() {
+      return this.noColor;
+   }
+
+   public void putBulkData(ByteBuffer var1) {
+      if (Config.isShaders()) {
+         SVertexBuilder.beginAddVertexData(this, var1);
+      }
+
+      this.growBuffer(var1.limit() + this.vertexFormat.getSize());
+      ((Buffer)this.byteBuffer).position(this.vertexCount * this.vertexFormat.getSize());
+      this.byteBuffer.put(var1);
+      this.vertexCount = this.vertexCount + var1.limit() / this.vertexFormat.getSize();
+      if (Config.isShaders()) {
+         SVertexBuilder.endAddVertexData(this);
+      }
+   }
+
    public class State {
       private final int[] stateRawBuffer;
       private final VertexFormat stateVertexFormat;
+      private TextureAtlasSprite[] stateQuadSprites;
+
+      public State(int[] var2, VertexFormat var3, TextureAtlasSprite[] var4) {
+         this.stateRawBuffer = var2;
+         this.stateVertexFormat = var3;
+         this.stateQuadSprites = var4;
+      }

       public State(int[] var2, VertexFormat var3) {
          this.stateRawBuffer = var2;
          this.stateVertexFormat = var3;
       }

 */
