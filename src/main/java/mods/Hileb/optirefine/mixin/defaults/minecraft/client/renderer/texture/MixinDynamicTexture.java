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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ALL")
@Mixin(DynamicTexture.class)
public abstract class MixinDynamicTexture extends AbstractTexture {

    @Shadow @Final
    private int[] dynamicTextureData;
    @Shadow @Final
    private int width;
    @Shadow @Final
    private int height;

    @Unique
    private boolean shadersInitialized = false;

    @Redirect(method = "<init>(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;allocateTexture(III)V"))
    public void afterConstructed(int glTextureId, int textureWidth, int textureHeight){
        if (Config.isShaders()) {
            ShadersTex.initDynamicTexture(glTextureId, textureWidth, textureHeight, (DynamicTexture)(Object)this);
            this.shadersInitialized = true;
        } else {
            TextureUtil.allocateTexture(glTextureId, textureWidth, textureHeight);
        }
    }

    @Inject(method = "updateDynamicTexture", at = @At("RETURN"))
    public void onUpdate(CallbackInfo ci){
        if (Config.isShaders()) {
            if (!this.shadersInitialized) {
                ShadersTex.initDynamicTexture(this.getGlTextureId(), this.width, this.height, DynamicTexture_cast(this));
                this.shadersInitialized = true;
            }

            ShadersTex.updateDynamicTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height, DynamicTexture_cast(this));
        } else {
            TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
        }
    }

    @AccessibleOperation(opcode = Opcodes.NOP)
    private native static DynamicTexture DynamicTexture_cast(MixinDynamicTexture obj);
}
