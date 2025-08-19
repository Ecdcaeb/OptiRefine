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
public abstract class MixinAbstractTexture {

    @Shadow
    protected int glTextureId;

    @SuppressWarnings("AddedMixinMembersNamePattern")
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public MultiTexID getMultiTexID() {
        return ShadersTex.getMultiTexID((AbstractTexture) (Object)this);
    }
}

/*
@@ -1,16 +1,19 @@
 package net.minecraft.client.renderer.texture;

 import net.minecraft.client.renderer.GlStateManager;
+import net.optifine.shaders.MultiTexID;
+import net.optifine.shaders.ShadersTex;

 public abstract class AbstractTexture implements ITextureObject {
    protected int glTextureId = -1;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean blurLast;
    protected boolean mipmapLast;
+   public MultiTexID multiTex;

    public void setBlurMipmapDirect(boolean var1, boolean var2) {
       this.blur = var1;
       this.mipmap = var2;
       int var3;
       short var4;
@@ -19,12 +22,13 @@
          var4 = 9729;
       } else {
          var3 = var2 ? 9986 : 9728;
          var4 = 9728;
       }

+      GlStateManager.bindTexture(this.getGlTextureId());
       GlStateManager.glTexParameteri(3553, 10241, var3);
       GlStateManager.glTexParameteri(3553, 10240, var4);
    }

    public void setBlurMipmap(boolean var1, boolean var2) {
       this.blurLast = this.blur;
@@ -42,12 +46,17 @@
       }

       return this.glTextureId;
    }

    public void deleteGlTexture() {
+      ShadersTex.deleteTextures(this, this.glTextureId);
       if (this.glTextureId != -1) {
          TextureUtil.deleteTexture(this.glTextureId);
          this.glTextureId = -1;
       }
+   }
+
+   public MultiTexID getMultiTexID() {
+      return ShadersTex.getMultiTexID(this);
    }
 }
 */
