package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(BakedQuadRetextured.class)
public abstract class MixinBakedQuadRetextured {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1

    // [AUDIT-OK] OF-added field (OF BakedQuadRetextured:8), not in baseline
    private TextureAtlasSprite spriteOld;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/BakedQuadRetextured;remapQuad()V"))
    // [AUDIT-FIXED] spriteOld must be set BEFORE remapQuad (OF ctor order); previously set at RETURN, so remapQuad
    // fell back to getSprite() = NEW texture -> two affine maps composed to identity -> UVs in old atlas coords
    public void initBeforeRemap(BakedQuad quad, TextureAtlasSprite p_i46217_2, CallbackInfo ci){
        this.spriteOld = quad.getSprite();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    // [AUDIT-OK] target <init>(BakedQuad,TextureAtlasSprite) baseline (deobf:14); fixVertexData call matches OF:23-24
    public void init(BakedQuad quad, TextureAtlasSprite p_i46217_2, CallbackInfo ci){
        // [AUDIT-OK] OF-added member BakedQuad.fixVertexData()V (provided by MixinBakedQuad @Unique); MCP name, no deobf correct
        BakedQuad_fixVertexData(this);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.block.model.BakedQuad fixVertexData ()V")
    private static native void BakedQuad_fixVertexData(Object bakedQuad);

    @Redirect(method = "remapQuad", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/block/model/BakedQuad;sprite:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    // [AUDIT-FIXED] fallback reads the REAL sprite field (old sprite from super), NOT getSprite() (= new texture)
    public TextureAtlasSprite applySpriteOld(BakedQuad instance){
        return spriteOld != null ? spriteOld : BakedQuad_sprite_get(instance);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.block.model.BakedQuad field_187509_d Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", deobf = true)
    private static native TextureAtlasSprite BakedQuad_sprite_get(BakedQuad instance);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] OF-added member (OF getSprite returns texture field)
    public TextureAtlasSprite getSprite() {
        // [AUDIT-ISSUE] vanilla SRG field_178218_d (=texture, tsrg) missing deobf=true — MCP (devrun) runtime would fail to resolve; add deobf=true per convention
        return BakedQuadRetextured_texture_get((BakedQuadRetextured) (Object) this);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.block.model.BakedQuadRetextured field_178218_d Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    private static native TextureAtlasSprite BakedQuadRetextured_texture_get(BakedQuadRetextured instance);

}
