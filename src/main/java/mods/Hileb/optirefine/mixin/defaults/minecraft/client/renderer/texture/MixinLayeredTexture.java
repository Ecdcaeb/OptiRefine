package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;

@SuppressWarnings("MissingUnique")
@Mixin(LayeredTexture.class)
public abstract class MixinLayeredTexture {

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private ResourceLocation textureLocation;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(String[] textureNames, CallbackInfo ci){
        if (textureNames.length > 0 && textureNames[0] != null) {
            this.textureLocation = new ResourceLocation(textureNames[0]);
        }
    }

    @WrapOperation(method = "loadTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I"))
    public int onImageLoad(int textureId, BufferedImage texture, Operation<Integer> original, @Local(argsOnly = true)IResourceManager resourceManager ){
        if (Config.isShaders()) {
            return ShadersTex.loadSimpleTexture(textureId, texture, false, false, resourceManager, this.textureLocation, this.getMultiTexID());
        } else {
            return original.call(textureId, texture);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.AbstractTexture getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    public native MultiTexID getMultiTexID();
}

/*
--- net/minecraft/client/renderer/texture/LayeredTexture.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/texture/LayeredTexture.java	Tue Aug 19 14:59:58 2025
@@ -1,25 +1,31 @@
 package net.minecraft.client.renderer.texture;

 import com.google.common.collect.Lists;
 import java.awt.image.BufferedImage;
+import java.awt.image.ImageObserver;
 import java.io.IOException;
 import java.util.List;
 import net.minecraft.client.resources.IResource;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.shaders.ShadersTex;
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class LayeredTexture extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;
+   private ResourceLocation textureLocation;

    public LayeredTexture(String... var1) {
       this.layeredTextureNames = Lists.newArrayList(var1);
+      if (var1.length > 0 && var1[0] != null) {
+         this.textureLocation = new ResourceLocation(var1[0]);
+      }
    }

    public void loadTexture(IResourceManager var1) throws IOException {
       this.deleteGlTexture();
       BufferedImage var2 = null;

@@ -31,21 +37,25 @@
                var5 = var1.getResource(new ResourceLocation(var4));
                BufferedImage var6 = TextureUtil.readBufferedImage(var5.getInputStream());
                if (var2 == null) {
                   var2 = new BufferedImage(var6.getWidth(), var6.getHeight(), 2);
                }

-               var2.getGraphics().drawImage(var6, 0, 0, null);
+               var2.getGraphics().drawImage(var6, 0, 0, (ImageObserver)null);
             }
             continue;
          } catch (IOException var10) {
             LOGGER.error("Couldn't load layered image", var10);
          } finally {
             IOUtils.closeQuietly(var5);
          }

          return;
       }

-      TextureUtil.uploadTextureImage(this.getGlTextureId(), var2);
+      if (Config.isShaders()) {
+         ShadersTex.loadSimpleTexture(this.getGlTextureId(), var2, false, false, var1, this.textureLocation, this.getMultiTexID());
+      } else {
+         TextureUtil.uploadTextureImage(this.getGlTextureId(), var2);
+      }
    }
 }
 */
