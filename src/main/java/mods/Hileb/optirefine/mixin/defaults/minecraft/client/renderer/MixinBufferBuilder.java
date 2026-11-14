package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
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
import java.util.Comparator;
@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 3

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_178999_b", deobf = true)
// [AUDIT-OK] vanilla SRG field_178999_b = rawIntBuffer, deobf=true
    public IntBuffer optiRefine$rawIntBufferAccess;

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_179000_c", deobf = true)
// [AUDIT-OK] vanilla SRG field_179000_c = rawFloatBuffer, deobf=true
    public FloatBuffer optiRefine$rawFloatBufferAccess;

    @Redirect(method = "growBuffer", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;asReadOnlyBuffer()Ljava/nio/FloatBuffer;", remap = false))
    // Keep the writable view created after growth rewinds the new buffer to byte offset 0.
    // Reusing it also avoids rebuilding a shifted view on every endVertex() call (#6).
    private FloatBuffer optiRefine$writableFloatBuffer(FloatBuffer buffer) {
        return buffer;
    }

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_178997_d", deobf = true)
// [AUDIT-OK] vanilla SRG field_178997_d = vertexCount, deobf=true
    public int optiRefine$vertexCountAccess;

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "field_179006_k", deobf = true)
// [AUDIT-OK] vanilla SRG field_179006_k = drawMode, deobf=true
    public int optiRefine$drawModeAccess;



    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    @Unique
    private BlockRenderLayer blockLayer;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    @Unique
    private boolean[] drawnIcons;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    @Unique
    private TextureAtlasSprite[] quadSprites;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    @Unique
    private TextureAtlasSprite[] quadSpritesPrev;
    @Unique
    private TextureAtlasSprite[] optiRefine$quadSpritesScratch;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    @Unique
    private TextureAtlasSprite quadSprite;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public)
    private SVertexBuilder sVertexBuilder;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public)
    @Unique
    private RenderEnv renderEnv;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public)
    @Unique
    private BitSet animatedSprites;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public)
    @Unique
    private BitSet animatedSpritesCached;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    @Unique
    private boolean modeTriangles;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added field, not in baseline
    private ByteBuffer byteBufferTriangles;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void optiRefine$initShaderVertexBuilder(int bufferSize, CallbackInfo ci){
// [AUDIT-OK] target BufferBuilder.<init>(I)V matches baseline; SVertexBuilder.initVertexBuilder matches OF ctor
        SVertexBuilder.initVertexBuilder((BufferBuilder)(Object)this);
    }
    @Inject(method = "growBuffer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/BufferBuilder;rawShortBuffer:Ljava/nio/ShortBuffer;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void optiRefine$growQuadSprites(int increaseAmount, CallbackInfo ci) {
        if (this.quadSprites != null) {
            this.quadSprites = Arrays.copyOf(this.quadSprites, this.getBufferQuadSize());
            this.quadSpritesPrev = null;
        }
    }

    @Shadow
// [AUDIT-OK] baseline member vertexCount (SRG field_178997_d)
    private int vertexCount;
    @Shadow
// [AUDIT-OK] baseline member vertexFormat (SRG field_179011_q)
    private VertexFormat vertexFormat;

    @SuppressWarnings("unused")
    @WrapOperation(method = "sortVertexData", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;sort([Ljava/lang/Object;Ljava/util/Comparator;)V", remap = false))
    private void optiRefine$sortQuadSprites(Object[] order, Comparator<?> comparator, Operation<Void> original) {
        original.call(order, comparator);
        if (this.quadSprites != null) {
            int quadCount = this.vertexCount / 4;
            TextureAtlasSprite[] sorted = this.optiRefine$quadSpritesScratch;
            if (sorted == null || sorted.length < quadCount) {
                sorted = new TextureAtlasSprite[quadCount];
                this.optiRefine$quadSpritesScratch = sorted;
            }
            try {
                for (int index = 0; index < order.length; index++) {
                    int sourceQuad = (Integer) order[index];
                    sorted[index] = this.quadSprites[sourceQuad];
                }
                System.arraycopy(sorted, 0, this.quadSprites, 0, quadCount);
            } finally {
                Arrays.fill(sorted, 0, quadCount, null);
            }
        }
    }

    @Shadow
// [AUDIT-OK] baseline member getBufferSize (SRG func_181664_j)
    protected abstract int getBufferSize();

    @WrapMethod(method = "getVertexState")
    // [AUDIT-FIXED 2026-08-09] crash: setVertexState(State) NPE'd on State.getRawBuffer()==null in
    // RenderChunk.resortTransparency. The old @Redirect NEW -> @NewConstructor 5-arg State chain
    // (static native newBufferBuilder$State renamed to <init>, colliding desc with
    // MixinBufferBuilderState's @NewConstructor) intermittently produced a State whose stateRawBuffer
    // was never assigned. Replaced with a plain wrap building the vanilla 2-arg State and setting
    // stateQuadSprites via a PUTFIELD bridge — same OF semantics, no cursed ctor codegen.
    public BufferBuilder.State optiRefine$getVertexState(Operation<BufferBuilder.State> original) {
        BufferBuilder.State state = original.call();
        optiRefine$setVertexStateQuadSprites(state, this.quadSprites == null ? null : Arrays.copyOf(this.quadSprites, this.vertexCount / 4));
        return state;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.BufferBuilder$State stateQuadSprites [Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
// [AUDIT-OK] stateQuadSprites is @Public (cursed postApply widens) — cross-class PUTFIELD legal
    private native static void optiRefine$setVertexStateQuadSprites(BufferBuilder.State state, TextureAtlasSprite[] sprites);

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "func_181664_j", deobf = true, access = Opcodes.ACC_PUBLIC)
// [AUDIT-OK] vanilla SRG func_181664_j = getBufferSize, deobf=true (OF: public)
    private static native int optiRefine$exposeBufferSize();

    
    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessTransformer(name = "func_181665_a", deobf = true, access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
// [AUDIT-OK] vanilla SRG func_181665_a = getDistanceSq, deobf=true (OF keeps private; ACC_PUBLIC extra visibility harmless)
    private static native float optiRefine$exposeVertexDistanceCalculation(FloatBuffer vertexData, float cameraX, float cameraY, float cameraZ, int vertexStride, int vertexOffset);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.BufferBuilder$State stateQuadSprites [Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
// [AUDIT-OK] stateQuadSprites is @Public (cursed postApply widens) — cross-class GETFIELD legal, verified 2026-08-05
    private native static TextureAtlasSprite[] optiRefine$getVertexStateQuadSprites(BufferBuilder.State state) ;

    @Inject(method = "setVertexState", at = @At("TAIL"))
    public void optiRefine$restoreQuadSprites(BufferBuilder.State state, CallbackInfo ci){
// [AUDIT-OK] target setVertexState(Lnet/minecraft/client/renderer/BufferBuilder$State;)V matches baseline
        if (optiRefine$getVertexStateQuadSprites(state) != null) {
            if (this.quadSprites == null) {
                this.quadSprites = this.quadSpritesPrev;
            }

            if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }

            TextureAtlasSprite[] src = optiRefine$getVertexStateQuadSprites(state);
            System.arraycopy(src, 0, this.quadSprites, 0, src.length);
        } else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }

            this.quadSprites = null;
        }
    }

    @Inject(method = "reset", at = @At("TAIL"))
    public void optiRefine$resetSpriteAndTriangleState(CallbackInfo ci){
// [AUDIT-OK] target reset()V matches baseline
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

    @Inject(method = "begin", at = @At("RETURN"))
    public void optiRefine$prepareVertexFormatAndSprites(int drawMode, VertexFormat format, CallbackInfo ci){
// [AUDIT-OK] target begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V matches baseline
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
// [AUDIT-OK] OF member toSingleU(F)F, not in baseline (MixinTextureAtlasSprite provides)
    private static native float optiRefine$toSpriteSingleU(TextureAtlasSprite textureAtlasSprite, float u);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleV (F)F")
// [AUDIT-OK] OF member toSingleV(F)F, not in baseline
    private static native float optiRefine$toSpriteSingleV(TextureAtlasSprite textureAtlasSprite, float v);


    @WrapMethod(method = "tex")
    public BufferBuilder optiRefine$mapSpriteTextureCoordinates(double u, double v, Operation<BufferBuilder> original){
// [AUDIT-OK] target tex(DD)Lnet/minecraft/client/renderer/BufferBuilder; matches baseline (OF tex body)
        if (this.quadSprite != null && this.quadSprites != null) {
            u = optiRefine$toSpriteSingleU(this.quadSprite, (float) u);
            v = optiRefine$toSpriteSingleV(this.quadSprite, (float) v);
            this.quadSprites[this.vertexCount / 4] = this.quadSprite;
        }
        return original.call(u, v);
    }

    @Inject(method = "addVertexData", at = @At("HEAD"))
    public void optiRefine$beginShaderVertexData(int[] vertexData, CallbackInfo ci){
// [AUDIT-OK] target addVertexData([I)V matches baseline
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData((BufferBuilder)(Object) this, vertexData);
        }
    }

    @Inject(method = "addVertexData", at = @At("RETURN"))
    public void optiRefine$finishShaderVertexData(int[] vertexData, CallbackInfo ci){
// [AUDIT-OK] target addVertexData([I)V matches baseline
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData((BufferBuilder)(Object)this);
        }
    }

    @SuppressWarnings("unused")
    @Shadow
// [AUDIT-OK] baseline member vertexFormatElement (SRG field_178998_a)
    private VertexFormatElement vertexFormatElement;
    @Shadow
// [AUDIT-OK] baseline member vertexFormatIndex (SRG field_178999_c)
    private int vertexFormatIndex;

    @Inject(method = "endVertex", at = @At("RETURN"))
    public void optiRefine$finishShaderVertex(CallbackInfo ci) {
// [AUDIT-OK] target endVertex()V matches baseline (OF resets format index)
        this.vertexFormatIndex = 0;
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertex((BufferBuilder)(Object)this);
        }
    }

    @Inject(method = "pos", at = @At("HEAD"))
    public void optiRefine$beginShaderVertex(double x, double y, double z, CallbackInfoReturnable<BufferBuilder> cir){
// [AUDIT-OK] target pos(DDD)Lnet/minecraft/client/renderer/BufferBuilder; matches baseline
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertex((BufferBuilder)(Object)this);
        }
    }

    @ModifyReturnValue(method = "getByteBuffer", at = @At("RETURN"))
    public ByteBuffer optiRefine$selectTriangleBuffer(ByteBuffer original){
// [AUDIT-OK] target getByteBuffer()Ljava/nio/ByteBuffer; matches baseline
        return this.modeTriangles ? this.byteBufferTriangles : original;
    }

    @WrapMethod(method = "getVertexCount")
    // [AUDIT-FIXED] OF overrides getVertexCount for triangle mode (6 verts/quad); missing before -> glDrawArrays got quad counts
    private int optiRefine$getVertexCount(Operation<Integer> original) {
        return this.modeTriangles ? this.vertexCount / 4 * 6 : original.call();
    }

    @ModifyReturnValue(method = "getDrawMode", at = @At("RETURN"))
    public int optiRefine$selectTriangleDrawMode(int original){
// [AUDIT-OK] target getDrawMode()I matches baseline (OF: modeTriangles -> 4)
        return this.modeTriangles ? 4 : original;
    }

    @SuppressWarnings("unused")
    @Shadow
// [AUDIT-OK] baseline member putColor(II)V (SRG func_192836_a)
    private void putColor(int p_192836_1_, int p_192836_2_) {}

    @Shadow
// [AUDIT-OK] baseline member putColorRGB_F(FFFFI)V (SRG func_178994_a)
    public void putColorRGB_F(float p_178994_1_, float p_178994_2_, float p_178994_3_, int p_178994_4_) {}

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getAnimationIndex ()I")
// [AUDIT-OK] OF member getAnimationIndex()I, not in baseline
    private static native int optiRefine$getSpriteAnimationIndex(TextureAtlasSprite textureAtlasSprite);


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
    public void putSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && optiRefine$getSpriteAnimationIndex(sprite) >= 0) {
            this.animatedSprites.set(optiRefine$getSpriteAnimationIndex(sprite));
        }

        if (this.quadSprites != null) {
            int countQuads = this.vertexCount / 4;
            this.quadSprites[countQuads - 1] = sprite;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public void setSprite(TextureAtlasSprite sprite) {
        if (this.animatedSprites != null && sprite != null && optiRefine$getSpriteAnimationIndex(sprite) >= 0) {
            this.animatedSprites.set(optiRefine$getSpriteAnimationIndex(sprite));
        }

        if (this.quadSprites != null) {
            this.quadSprite = sprite;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
    public boolean isMultiTexture() {
        return this.quadSprites != null;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountRegisteredSprites ()I")
// [AUDIT-OK] OF member getCountRegisteredSprites()I, not in baseline (MixinTextureMap provides)
    private static native int optiRefine$getTextureMapCountRegisteredSprites(TextureMap textureMap) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite getIndexInMap ()I")
// [AUDIT-OK] OF member getIndexInMap()I, not in baseline
    private static native int optiRefine$getSpriteIndexInMap(TextureAtlasSprite textureAtlasSprite);


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public void drawMultiTexture() {
        if (this.quadSprites == null) {
            return;
        }
        int previousTexture = optiRefine$getGlStateBoundTexture();
        try {
            int maxTextureIndex = optiRefine$getTextureMapCountRegisteredSprites(Config.getMinecraft().getTextureMapBlocks());
            if (this.drawnIcons.length <= maxTextureIndex) {
                this.drawnIcons = new boolean[maxTextureIndex + 1];
            }

            Arrays.fill(this.drawnIcons, false);
            int grassOverlayIndex = -1;
            int countQuads = this.vertexCount / 4;

            for (int i = 0; i < countQuads; i++) {
                TextureAtlasSprite icon = this.quadSprites[i];
                if (icon != null) {
                    int iconIndex = optiRefine$getSpriteIndexInMap(icon);
                    if (!this.drawnIcons[iconIndex]) {
                        if (icon == TextureUtils.iconGrassSideOverlay) {
                            if (grassOverlayIndex < 0) {
                                grassOverlayIndex = i;
                            }
                        } else {
                            i = this.drawForIcon(icon, i) - 1;
                            if (this.blockLayer != BlockRenderLayer.TRANSLUCENT) {
                                this.drawnIcons[iconIndex] = true;
                            }
                        }
                    }
                }
            }

            if (grassOverlayIndex >= 0) {
                this.drawForIcon(TextureUtils.iconGrassSideOverlay, grassOverlayIndex);
            }

        } finally {
            GlStateManager.bindTexture(previousTexture);
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager getBoundTexture ()I")
    private static native int optiRefine$getGlStateBoundTexture();

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite glSpriteTextureId I")
// [AUDIT-OK] OF field glSpriteTextureId, not in baseline (MixinTextureAtlasSprite @Public)
    private static native int optiRefine$getSpriteTextureId(TextureAtlasSprite textureAtlasSprite);

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added method, not in baseline
    private int drawForIcon(TextureAtlasSprite sprite, int startQuadPos) {
        GlStateManager.bindTexture(optiRefine$getSpriteTextureId(sprite));
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
// [AUDIT-OK] baseline member drawMode (SRG field_179006_k)
    private int drawMode;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    private void draw(int startQuadVertex, int endQuadVertex) {
        int vxQuadCount = endQuadVertex - startQuadVertex;
        if (vxQuadCount > 0) {
            int verticesPerQuad = this.modeTriangles ? 6 : 4;
            GlStateManager.glDrawArrays(this.modeTriangles ? GL11.GL_TRIANGLES : this.drawMode,
                    startQuadVertex * verticesPerQuad, vxQuadCount * verticesPerQuad);
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
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
// [AUDIT-OK] baseline member rawIntBuffer (SRG field_178999_b)
    private IntBuffer rawIntBuffer;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    private int getBufferQuadSize() {
        return this.rawIntBuffer.capacity() / (this.vertexFormat.getIntegerSize() * 4);
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
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
// [AUDIT-OK] baseline member xOffset (SRG field_179004_l)
    private double xOffset;
    @Shadow
// [AUDIT-OK] baseline member yOffset (SRG field_179005_m)
    private double yOffset;
    @Shadow
// [AUDIT-OK] baseline member zOffset (SRG field_179002_n)
    private double zOffset;
    @Shadow
// [AUDIT-OK] baseline member isDrawing (SRG field_179010_r)
    private boolean isDrawing;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public boolean isDrawing() {
        return this.isDrawing;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
    public double getXOffset() {
        return this.xOffset;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public double getYOffset() {
        return this.yOffset;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
    public double getZOffset() {
        return this.zOffset;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public BlockRenderLayer getBlockLayer() {
        return this.blockLayer;
    }

    @Shadow
// [AUDIT-OK] baseline member noColor (SRG field_178995_e)
    private boolean noColor;

    @Shadow
// [AUDIT-OK] baseline member getColorIndex(I)I (SRG func_78909_a)
    public int getColorIndex(int p_78909_1_) {return 0;}

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
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
// [AUDIT-OK] baseline member byteBuffer (SRG field_179001_a)
    private ByteBuffer byteBuffer;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public void quadsToTriangles() {
        if (this.drawMode == GL11.GL_QUADS && !this.modeTriangles) {
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
// [AUDIT-OK] member provided by Cleanroom patch (patch/patches adds 5-arg putColorRGBA/isColorDisabled/putBulkData); no @Unique here so the member silently resolves to the runtime-provided one (no warning)
    public void putColorRGBA(int index, int red, int green, int blue, int alpha) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, alpha << 24 | blue << 16 | green << 8 | red);
        } else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | alpha);
        }
    }

    @Shadow
// [AUDIT-OK] baseline member growBuffer(I)V (SRG func_181670_b)
    private void growBuffer(int p_181670_1_) {}

    @WrapMethod(method = "putBulkData")
    // [AUDIT-FIXED 2026-08-05, reworked 2026-08-09] the runtime putBulkData (Cleanroom patch version) has
    // no SVertexBuilder hooks -> Forge LightUtil item rendering wrote 56-byte vertices without
    // midTexCoord/tangent/entity data -> items corrupted under shaders. WrapMethod replicates OF's
    // beginAddVertexData/endAddVertexData around the original body (was an @Inject HEAD/RETURN pair;
    // the @Unique copy of putBulkData is discarded at apply, runtime version is Cleanroom's).
    private void optiRefine$putBulkData(java.nio.ByteBuffer buffer, Operation<Void> original) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData((BufferBuilder) (Object)this, buffer);
        }
        original.call(buffer);
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData((BufferBuilder) (Object)this);
        }
    }




    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initializeVertexBufferState(CallbackInfo ci) {
        this.blockLayer = null;
        this.drawnIcons = new boolean[256];
        this.quadSprites = null;
        this.quadSpritesPrev = null;
        this.optiRefine$quadSpritesScratch = null;
        this.quadSprite = null;
        this.renderEnv = null;
        this.animatedSprites = null;
        this.animatedSpritesCached = new BitSet();
        this.modeTriangles = false;
    }
}
