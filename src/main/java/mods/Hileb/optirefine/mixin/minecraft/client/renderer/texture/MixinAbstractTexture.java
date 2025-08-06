package mods.Hileb.optirefine.mixin.minecraft.client.renderer.texture;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(AbstractTexture.class)
public abstract class MixinAbstractTexture {

    @Shadow
    protected int glTextureId;

    @Unique @Public
    public MultiTexID multiTex;

    @Inject(method = "deleteGlTexture", at = @At("HEAD"))
    public void ideleteGlTexture(CallbackInfo ci){
        ShadersTex.deleteTextures((AbstractTexture)(Object)this, this.glTextureId);
    }

    @Shadow
    public abstract int getGlTextureId();

    @Inject(method = "setBlurMipmapDirect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glTexParameteri(III)V", ordinal = 0))
    public void bindTextureBefore(boolean p_174937_1_, boolean p_174937_2_, CallbackInfo ci){
        GlStateManager.bindTexture(this.getGlTextureId());
    }

    @Unique
    public MultiTexID getMultiTexID() {
        return ShadersTex.getMultiTexID((AbstractTexture) (Object)this);
    }
}
