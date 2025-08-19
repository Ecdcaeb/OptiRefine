package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.EmissiveTextures;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.image.BufferedImage;

@Mixin(SimpleTexture.class)
public abstract class MixinSimpleTexture {

    @Shadow @Final
    protected ResourceLocation textureLocation;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public ResourceLocation locationEmissive;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public boolean isEmissive;

    @WrapOperation(method = "loadTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImageAllocate(ILjava/awt/image/BufferedImage;ZZ)I"))
    public int _uploadTextureImageAllocate(int textureId, BufferedImage texture, boolean blur, boolean clamp, Operation<Integer> original, @Local(argsOnly = true)IResourceManager resourceManager){
        if (Config.isShaders()) {
            ShadersTex.loadSimpleTexture(textureId, texture, blur, clamp, resourceManager, this.textureLocation, this.getMultiTexID());
        } else {
            return original.call(textureId, texture, blur, clamp);
        }

        if (EmissiveTextures.isActive()) {
            EmissiveTextures.loadTexture(this.textureLocation, (SimpleTexture)(Object) this);
        }
        return 0;
    }

    @SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.AbstractTexture getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    public native MultiTexID getMultiTexID();
}
/*
--- net/minecraft/client/renderer/texture/SimpleTexture.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/texture/SimpleTexture.java	Tue Aug 19 14:59:58 2025
@@ -3,19 +3,23 @@
 import java.awt.image.BufferedImage;
 import java.io.IOException;
 import net.minecraft.client.resources.IResource;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.client.resources.data.TextureMetadataSection;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.EmissiveTextures;
+import net.optifine.shaders.ShadersTex;
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class SimpleTexture extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final ResourceLocation textureLocation;
+   public ResourceLocation locationEmissive;
+   public boolean isEmissive;

    public SimpleTexture(ResourceLocation var1) {
       this.textureLocation = var1;
    }

    public void loadTexture(IResourceManager var1) throws IOException {
@@ -26,22 +30,30 @@
          var2 = var1.getResource(this.textureLocation);
          BufferedImage var3 = TextureUtil.readBufferedImage(var2.getInputStream());
          boolean var4 = false;
          boolean var5 = false;
          if (var2.hasMetadata()) {
             try {
-               TextureMetadataSection var6 = var2.getMetadata("texture");
+               TextureMetadataSection var6 = (TextureMetadataSection)var2.getMetadata("texture");
                if (var6 != null) {
                   var4 = var6.getTextureBlur();
                   var5 = var6.getTextureClamp();
                }
             } catch (RuntimeException var10) {
                LOGGER.warn("Failed reading metadata of: {}", this.textureLocation, var10);
             }
          }

-         TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), var3, var4, var5);
+         if (Config.isShaders()) {
+            ShadersTex.loadSimpleTexture(this.getGlTextureId(), var3, var4, var5, var1, this.textureLocation, this.getMultiTexID());
+         } else {
+            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), var3, var4, var5);
+         }
+
+         if (EmissiveTextures.isActive()) {
+            EmissiveTextures.loadTexture(this.textureLocation, this);
+         }
       } finally {
          IOUtils.closeQuietly(var2);
       }
    }
 }
 */
