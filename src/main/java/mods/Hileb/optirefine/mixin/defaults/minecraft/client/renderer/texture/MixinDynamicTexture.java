package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.optifine.shaders.ShadersTex;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@SuppressWarnings("ALL")
@Mixin(DynamicTexture.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinDynamicTexture extends AbstractTexture {

    @Mutable
    @Shadow @Final
    // [AUDIT-OK] baseline member dynamicTextureData exists in DynamicTexture (@Mutable permits 3x realloc)
    private int[] dynamicTextureData;
    @Shadow @Final
    // [AUDIT-OK] baseline members width/height exist in DynamicTexture
    private int width;
    @Shadow @Final
    private int height;

    // [AUDIT-OK] OF-added field shadersInitialized (@Unique), not in baseline
    @Unique
    private boolean shadersInitialized;

    @Redirect(method = "<init>(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;allocateTexture(III)V"))
    // [AUDIT-OK] target <init>(II)V matches baseline; redirect of allocateTexture replicates OF ctor (dynamicTextureData expanded to width*height*3 + ShadersTex.initDynamicTexture)
    public void optiRefine$allocateShaderTextureData(int glTextureId, int textureWidth, int textureHeight){
        // Keep the array stable when shaders are enabled after callers cache getTextureData().
        this.dynamicTextureData = new int[textureWidth * textureHeight * 3];
        if (Config.isShaders()) {

            ShadersTex.initDynamicTexture(glTextureId, textureWidth, textureHeight, (DynamicTexture)(Object)this);
            this.shadersInitialized = true;
        } else {
            TextureUtil.allocateTexture(glTextureId, textureWidth, textureHeight);
        }
    }

    @Redirect(method = "updateDynamicTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTexture(I[III)V"))
    // [AUDIT-OK] target updateDynamicTexture()V matches baseline; redirect of uploadTexture mirrors OF (lazy shaders init + ShadersTex.updateDynamicTexture)
    public void optiRefine$uploadShaderTextureData(int glTextureId, int[] textureData, int width, int height){
        if (Config.isShaders()) {
            if (!this.shadersInitialized) {
                ShadersTex.initDynamicTexture(this.getGlTextureId(), this.width, this.height, optiRefine$castDynamicTexture(this));
                this.shadersInitialized = true;
            }

            ShadersTex.updateDynamicTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height, optiRefine$castDynamicTexture(this));
        } else {
            TextureUtil.uploadTexture(glTextureId, textureData, width, height);
        }
    }

    @AccessibleOperation(opcode = Opcodes.NOP)
    // [AUDIT-OK] NOP cast helper: call removed, preceding aload(this) remains as arg (semantically identical)
    private native static DynamicTexture optiRefine$castDynamicTexture(MixinDynamicTexture obj);

}

