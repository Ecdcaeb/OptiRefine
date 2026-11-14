package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.optifine.model.QuadBounds;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(BakedQuad.class)
public abstract class MixinBakedQuad {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @AccessTransformer(name = "field_178215_a", deobf = true)
    // [AUDIT-OK] AT fields: field_178215_a vertexData / field_178213_b tintIndex / field_178214_c face / field_187509_d sprite (tsrg, SRG+deobf) and format / applyDiffuseLighting (Forge-added, tsrg-less, MCP) — all present in runtime (cleanroom BakedQuad patch)
    public int[] optiRefine$vertexDataAccess;

    @Unique
    @AccessTransformer(name = "field_178213_b", deobf = true)
    public int optiRefine$tintIndexAccess;
    @AccessTransformer(name = "field_178214_c", deobf = true)
    public EnumFacing optiRefine$faceAccess;
    @Unique
    @AccessTransformer(name = "field_187509_d", deobf = true)
    public TextureAtlasSprite optiRefine$spriteAccess;

    @AccessTransformer(name = "format")
    public VertexFormat  optiRefine$formatAccess;

    @Unique
    @AccessTransformer(name = "applyDiffuseLighting")
    public boolean  optiRefine$diffuseLightingAccess;


    @Shadow @Final @Mutable
    // [AUDIT-OK] baseline members vertexData/tintIndex/face/sprite declared in BakedQuad (protected final, deobf:11-14); @Mutable for fixVertexData rewrite
    protected int[] vertexData;
    @Shadow @Final @Mutable
    protected int tintIndex;
    @Shadow @Final @Mutable
    protected EnumFacing face;
    @Shadow @Final @Mutable
    protected TextureAtlasSprite sprite;

    @Unique
    private int[] vertexDataSingle;
    @Unique
    private QuadBounds quadBounds;
    private boolean quadEmissiveChecked;
    @Unique
    private BakedQuad quadEmissive;

    @Shadow
    public abstract EnumFacing getFace();

    @Unique
    private VertexFormat optiRefine$compactFormat;
    @Unique
    private VertexFormat optiRefine$expandedFormat;

    @ModifyReturnValue(method = "getFormat", at = @At("RETURN"))
    private VertexFormat optiRefine$getMatchingFormat(VertexFormat original) {
        this.fixVertexData();
        // Four vertices of four-byte ints: array length equals bytes per vertex.
        int vertexBytes = this.vertexData.length;
        if (original.getSize() == vertexBytes) {
            return original;
        }
        VertexFormat cached = vertexBytes == 28 ? this.optiRefine$compactFormat : this.optiRefine$expandedFormat;
        if (cached == null) {
            cached = new VertexFormat();
            for (VertexFormatElement element : original.getElements()) {
                if (cached.getSize() + element.getSize() > vertexBytes) {
                    break;
                }
                cached.addElement(element);
            }
            if (cached.getSize() < vertexBytes) {
                cached.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE,
                        VertexFormatElement.EnumUsage.PADDING, vertexBytes - cached.getSize()));
            }
            if (vertexBytes == 28) {
                this.optiRefine$compactFormat = cached;
            } else {
                this.optiRefine$expandedFormat = cached;
            }
        }
        return cached;
    }
    @Inject(method = "<init>([IILnet/minecraft/util/EnumFacing;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZLnet/minecraft/client/renderer/vertex/VertexFormat;)V", at = @At("RETURN"))
    // [AUDIT-OK] 6-arg ctor (int[],int,EnumFacing,TextureAtlasSprite,boolean,VertexFormat) added by cleanroom BakedQuad patch — target exists
    public void optiRefine$initVertexData(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn, boolean applyDiffuseLighting, VertexFormat format, CallbackInfo ci){
        this.fixVertexData();
    }

    @ModifyReturnValue(method = "getSprite", at = @At("RETURN"))
    // [AUDIT-OK] target getSprite() baseline; null->getSpriteByUv matches OF:43-45
    public TextureAtlasSprite optiRefine$resolveMissingSprite(TextureAtlasSprite original) {
        if (original == null) {
            return this.sprite = optiRefine$getSpriteByUv(this.getVertexData());
        }
        return original;
    }

    @Inject(method = "getVertexData", at = @At("HEAD"))
    // [AUDIT-OK] target getVertexData() baseline (deobf:29)
    public void optiRefine$updateVertexData(CallbackInfoReturnable<int[]> cir){
        this.fixVertexData();
    }

    @Shadow
    public abstract int[] getVertexData();

    @ModifyReturnValue(method = "getFace", at = @At("RETURN"))
    // [AUDIT-OK] target getFace() baseline (deobf:44); null->getFacingFromVertexData matches OF:64-66
    public EnumFacing optiRefine$resolveMissingFace(EnumFacing original) {
        if (original == null) {
            return this.face = FaceBakery.getFacingFromVertexData(this.getVertexData());
        } else return original;
    }

    @Shadow
    public abstract TextureAtlasSprite getSprite();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added member (OF:72); toSingleU/toSingleV/getIconByUV are OF members (MCP, no deobf) provided by MixinTextureAtlasSprite/MixinTextureMap
    public int[] getVertexDataSingle() {
        this.fixVertexData();
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = optiRefine$makeVertexDataSingle(this.getVertexData(), this.getSprite());
        }

        return this.vertexDataSingle;
    }

    @Unique
    private static int[] optiRefine$makeVertexDataSingle(int[] vertexData, TextureAtlasSprite sprite) {
        int[] vdSingle = vertexData.clone();
        int step = vdSingle.length / 4;

        for (int i = 0; i < 4; i++) {
            int pos = i * step;
            float tu = Float.intBitsToFloat(vdSingle[pos + 4]);
            float tv = Float.intBitsToFloat(vdSingle[pos + 4 + 1]);
            float u = optiRefine$toSpriteSingleU(sprite, tu);
            float v = optiRefine$toSpriteSingleV(sprite, tv);
            vdSingle[pos + 4] = Float.floatToRawIntBits(u);
            vdSingle[pos + 4 + 1] = Float.floatToRawIntBits(v);
        }

        return vdSingle;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleU (F)F")
    private static native float optiRefine$toSpriteSingleU(TextureAtlasSprite textureAtlasSprite, float u);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleV (F)F")
    private static native float optiRefine$toSpriteSingleV(TextureAtlasSprite textureAtlasSprite, float v);

    private static TextureAtlasSprite optiRefine$getSpriteByUv(int[] vertexData) {
        float uMin = 1.0F;
        float vMin = 1.0F;
        float uMax = 0.0F;
        float vMax = 0.0F;
        int step = vertexData.length / 4;

        for (int i = 0; i < 4; i++) {
            int pos = i * step;
            float tu = Float.intBitsToFloat(vertexData[pos + 4]);
            float tv = Float.intBitsToFloat(vertexData[pos + 4 + 1]);
            uMin = Math.min(uMin, tu);
            vMin = Math.min(vMin, tv);
            uMax = Math.max(uMax, tu);
            vMax = Math.max(vMax, tv);
        }

        float uMid = (uMin + uMax) / 2.0F;
        float vMid = (vMin + vMax) / 2.0F;
        return optiRefine$getTextureMapIconByUV(Minecraft.getMinecraft().getTextureMapBlocks(), uMid, vMid);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getIconByUV (FF)Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
    private static native TextureAtlasSprite optiRefine$getTextureMapIconByUV(TextureMap textureMap, float u, float v);

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added member (OF:131, protected); 28<->56 expand/compact matches OF shader formats
    protected void fixVertexData() {
        if (Config.isShaders()) {
            if (this.vertexData.length == 28) {
                this.vertexData = optiRefine$expandVertexData(this.vertexData);
                this.vertexDataSingle = null;
                this.quadBounds = null;
            }
        } else if (this.vertexData.length == 56) {
            this.vertexData = optiRefine$compactVertexData(this.vertexData);
            this.vertexDataSingle = null;
            this.quadBounds = null;
        }
    }

    private static int[] optiRefine$expandVertexData(int[] vertexData) {
        int step = vertexData.length / 4;
        int stepNew = step * 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(vertexData, i * step, vdNew, i * stepNew, step);
        }

        return vdNew;
    }

    @Unique
    private static int[] optiRefine$compactVertexData(int[] vertexData) {
        int step = vertexData.length / 4;
        int stepNew = step / 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(vertexData, i * step, vdNew, i * stepNew, stepNew);
        }

        return vdNew;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added members getQuadBounds/getMidX/getMidY/getMidZ/isFaceQuad/isFullQuad/isFullFaceQuad (OF:165-199)
    public QuadBounds getQuadBounds() {
        if (this.quadBounds == null) {
            this.quadBounds = new QuadBounds(this.getVertexData());
        }

        return this.quadBounds;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public float getMidX() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxX() + qb.getMinX()) / 2.0F;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    public double getMidY() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxY() + qb.getMinY()) / 2.0F;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public double getMidZ() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxZ() + qb.getMinZ()) / 2.0F;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    public boolean isFaceQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFaceQuad(this.getFace());
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean isFullQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFullQuad(this.getFace());
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    public boolean isFullFaceQuad() {
        return this.isFullQuad() && this.isFaceQuad();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] OF-added member (OF:202); TextureAtlasSprite.spriteEmissive OF-added (provided by MixinTextureAtlasSprite)
    public BakedQuad getQuadEmissive() {
        if (this.quadEmissiveChecked) {
            return this.quadEmissive;
        } else {
            TextureAtlasSprite sprite = this.getSprite();
            if (this.quadEmissive == null && sprite != null && optiRefine$getEmissiveSprite(sprite) != null) {
                this.quadEmissive = new BakedQuadRetextured(optiRefine$asBakedQuad(this), optiRefine$getEmissiveSprite(sprite));
            }

            this.quadEmissiveChecked = true;
            return this.quadEmissive;
        }
    }

    @AccessibleOperation
    private static native BakedQuad optiRefine$asBakedQuad(Object instance);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteEmissive Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
    private static native TextureAtlasSprite optiRefine$getEmissiveSprite(TextureAtlasSprite textureAtlasSprite);

    @Override
    // [AUDIT-OK] OF overrides toString (OF:215); nit: no @Unique — merged as added member (warns AddedMixinMembersNamePattern)
    public String toString() {
        return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initializeSingleTextureVertexCache(CallbackInfo ci) {
        this.vertexDataSingle = null;
    }
}
