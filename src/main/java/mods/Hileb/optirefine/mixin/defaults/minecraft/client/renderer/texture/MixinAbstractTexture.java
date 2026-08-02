package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;


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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinAbstractTexture {

    @Shadow
    // [AUDIT-OK] baseline member glTextureId exists in AbstractTexture
    protected int glTextureId;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    // [AUDIT-OK] OF-added field multiTex (@Public), not in baseline
    public MultiTexID multiTex;

    @Inject(method = "deleteGlTexture", at = @At("HEAD"))
    // [AUDIT-OK] target deleteGlTexture()V matches baseline; HEAD inject replicates OF ShadersTex.deleteTextures
    public void ideleteGlTexture(CallbackInfo ci){
        ShadersTex.deleteTextures((AbstractTexture)(Object)this, this.glTextureId);
    }

    @Shadow
    // [AUDIT-OK] baseline member getGlTextureId exists in AbstractTexture
    public abstract int getGlTextureId();

    @Inject(method = "setBlurMipmapDirect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glTexParameteri(III)V", ordinal = 0))
    // [AUDIT-OK] target setBlurMipmapDirect(ZZ)V matches baseline; OF adds GlStateManager.bindTexture before params
    public void bindTextureBefore(boolean p_174937_1_, boolean p_174937_2_, CallbackInfo ci){
        GlStateManager.bindTexture(this.getGlTextureId());
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] OF-added @Unique method getMultiTexID, not in baseline
    public MultiTexID getMultiTexID() {
        return ShadersTex.getMultiTexID((AbstractTexture) (Object)this);
    }
}
