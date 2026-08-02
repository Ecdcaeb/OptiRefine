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

    @Unique
    @AccessTransformer(name = "field_178215_a", deobf = true)
    // [AUDIT-OK] AT fields: field_178215_a vertexData / field_178213_b tintIndex / field_178214_c face / field_187509_d sprite (tsrg, SRG+deobf) and format / applyDiffuseLighting (Forge-added, tsrg-less, MCP) — all present in runtime (cleanroom BakedQuad patch)
    public int[] optirefine$acc_vertexData;

    @Unique
    @AccessTransformer(name = "field_178213_b", deobf = true)
    public int optirefine$acc_tintIndex;
    @Unique
    @AccessTransformer(name = "field_178214_c", deobf = true)
    public EnumFacing optirefine$acc_face;
    @Unique
    @AccessTransformer(name = "field_187509_d", deobf = true)
    public TextureAtlasSprite optirefine$acc_sprite;

    @Unique
    @AccessTransformer(name = "format")
    public VertexFormat  optirefine$acc_format;

    @Unique
    @AccessTransformer(name = "applyDiffuseLighting")
    public boolean  optirefine$acc_applyDiffuseLighting;


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
    private int[] vertexDataSingle = null;
    @Unique
    private QuadBounds quadBounds;
    @Unique
    private boolean quadEmissiveChecked;
    @Unique
    private BakedQuad quadEmissive;

    @Inject(method = "<init>([IILnet/minecraft/util/EnumFacing;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZLnet/minecraft/client/renderer/vertex/VertexFormat;)V", at = @At("RETURN"))
    // [AUDIT-OK] 6-arg ctor (int[],int,EnumFacing,TextureAtlasSprite,boolean,VertexFormat) added by cleanroom BakedQuad patch — target exists
    public void init(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn, boolean applyDiffuseLighting, VertexFormat format, CallbackInfo ci){
        this.fixVertexData();
    }

    @ModifyReturnValue(method = "getSprite", at = @At("RETURN"))
    // [AUDIT-OK] target getSprite() baseline; null->getSpriteByUv matches OF:43-45
    public TextureAtlasSprite $getSprite(TextureAtlasSprite orv) {
        if (orv == null) {
            return this.sprite = optiRefine$getSpriteByUv(this.getVertexData());
        }
        return orv;
    }

    @Inject(method = "getVertexData", at = @At("HEAD"))
    // [AUDIT-OK] target getVertexData() baseline (deobf:29)
    public void beforeGetVertexData(CallbackInfoReturnable<int[]> cir){
        this.fixVertexData();
    }

    @Shadow
    public abstract int[] getVertexData();

    @ModifyReturnValue(method = "getFace", at = @At("RETURN"))
    // [AUDIT-OK] target getFace() baseline (deobf:44); null->getFacingFromVertexData matches OF:64-66
    public EnumFacing $getSprite(EnumFacing original) {
        if (original == null) {
            return this.face = FaceBakery.getFacingFromVertexData(this.getVertexData());
        } else return original;
    }

    @Shadow
    public abstract TextureAtlasSprite getSprite();

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added member (OF:72); toSingleU/toSingleV/getIconByUV are OF members (MCP, no deobf) provided by MixinTextureAtlasSprite/MixinTextureMap
    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = optiRefine$makeVertexDataSingle(this.getVertexData(), this.getSprite());
        }

        return this.vertexDataSingle;
    }

    @Unique
    private static int[] optiRefine$makeVertexDataSingle(int[] vd, TextureAtlasSprite sprite) {
        int[] vdSingle = vd.clone();
        int step = vdSingle.length / 4;

        for (int i = 0; i < 4; i++) {
            int pos = i * step;
            float tu = Float.intBitsToFloat(vdSingle[pos + 4]);
            float tv = Float.intBitsToFloat(vdSingle[pos + 4 + 1]);
            float u = TextureAtlasSprite_toSingleU(sprite, tu);
            float v = TextureAtlasSprite_toSingleV(sprite, tv);
            vdSingle[pos + 4] = Float.floatToRawIntBits(u);
            vdSingle[pos + 4 + 1] = Float.floatToRawIntBits(v);
        }

        return vdSingle;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleU (F)F")
    private static native float TextureAtlasSprite_toSingleU(TextureAtlasSprite textureAtlasSprite, float arg1);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleV (F)F")
    private static native float TextureAtlasSprite_toSingleV(TextureAtlasSprite textureAtlasSprite, float arg1);

    @Unique
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
        return TextureMap_getIconByUV(Minecraft.getMinecraft().getTextureMapBlocks(), uMid, vMid);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getIconByUV (FF)Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
    private static native TextureAtlasSprite TextureMap_getIconByUV(TextureMap textureMap, float u, float v);

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added member (OF:131, protected); 28<->56 expand/compact matches OF shader formats
    protected void fixVertexData() {
        if (Config.isShaders()) {
            if (this.vertexData.length == 28) {
                this.vertexData = optiRefine$expandVertexData(this.vertexData);
            }
        } else if (this.vertexData.length == 56) {
            this.vertexData = optiRefine$compactVertexData(this.vertexData);
        }
    }

    @Unique
    private static int[] optiRefine$expandVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step * 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, step);
        }

        return vdNew;
    }

    @Unique
    private static int[] optiRefine$compactVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step / 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, stepNew);
        }

        return vdNew;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
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
    @Unique
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
    @Unique
    public boolean isFaceQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFaceQuad(this.face);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean isFullQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFullQuad(this.face);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
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
            if (this.quadEmissive == null && this.sprite != null && TextureAtlasSprite_spriteEmissive_get(this.sprite) != null) {
                this.quadEmissive = new BakedQuadRetextured(cast_BakedQuad(this), TextureAtlasSprite_spriteEmissive_get(this.sprite));
            }

            this.quadEmissiveChecked = true;
            return this.quadEmissive;
        }
    }

    @AccessibleOperation
    private static native BakedQuad cast_BakedQuad(Object obj);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteEmissive Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
    private static native TextureAtlasSprite TextureAtlasSprite_spriteEmissive_get(TextureAtlasSprite textureAtlasSprite);

    @Override
    // [AUDIT-OK] OF overrides toString (OF:215); nit: no @Unique — merged as added member (warns AddedMixinMembersNamePattern)
    public String toString() {
        return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }
}
