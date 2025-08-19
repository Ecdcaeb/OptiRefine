package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.image.BufferedImage;

@Mixin(LayeredColorMaskTexture.class)
public abstract class MixinLayeredColorMaskTexture {

    @Shadow @Final
    private ResourceLocation textureLocation;

    @WrapOperation(method = "loadTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I"))
    public int onImageLoad(int textureId, BufferedImage texture, Operation<Integer> original, @Local(argsOnly = true) IResourceManager resourceManager ){
        if (Config.isShaders()) {
            return ShadersTex.loadSimpleTexture(textureId, texture, false, false, resourceManager, this.textureLocation, this.getMultiTexID());
        } else {
            return original.call(textureId, texture);
        }
    }

    @SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.AbstractTexture getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    public native MultiTexID getMultiTexID();
}

/*
@@ -1,17 +1,20 @@
 package net.minecraft.client.renderer.texture;

 import java.awt.Graphics;
 import java.awt.image.BufferedImage;
+import java.awt.image.ImageObserver;
 import java.io.IOException;
 import java.util.List;
 import net.minecraft.client.resources.IResource;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.item.EnumDyeColor;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
+import net.optifine.reflect.Reflector;
+import net.optifine.shaders.ShadersTex;
 import org.apache.commons.io.IOUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class LayeredColorMaskTexture extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
@@ -24,70 +27,76 @@
       this.listTextures = var2;
       this.listDyeColors = var3;
    }

    public void loadTexture(IResourceManager var1) throws IOException {
       this.deleteGlTexture();
-      IResource var3 = null;
+      IResource var2 = null;

-      BufferedImage var2;
-      label198: {
+      BufferedImage var3;
+      label201: {
          try {
-            var3 = var1.getResource(this.textureLocation);
-            BufferedImage var4 = TextureUtil.readBufferedImage(var3.getInputStream());
+            var2 = var1.getResource(this.textureLocation);
+            BufferedImage var4 = TextureUtil.readBufferedImage(var2.getInputStream());
             int var5 = var4.getType();
             if (var5 == 0) {
                var5 = 6;
             }

-            var2 = new BufferedImage(var4.getWidth(), var4.getHeight(), var5);
-            Graphics var6 = var2.getGraphics();
-            var6.drawImage(var4, 0, 0, null);
+            var3 = new BufferedImage(var4.getWidth(), var4.getHeight(), var5);
+            Graphics var6 = var3.getGraphics();
+            var6.drawImage(var4, 0, 0, (ImageObserver)null);
             int var7 = 0;

             while (true) {
                if (var7 >= 17 || var7 >= this.listTextures.size() || var7 >= this.listDyeColors.size()) {
-                  break label198;
+                  break label201;
                }

                IResource var8 = null;

                try {
                   String var9 = this.listTextures.get(var7);
                   int var10 = this.listDyeColors.get(var7).getColorValue();
                   if (var9 != null) {
                      var8 = var1.getResource(new ResourceLocation(var9));
-                     BufferedImage var11 = TextureUtil.readBufferedImage(var8.getInputStream());
-                     if (var11.getWidth() == var2.getWidth() && var11.getHeight() == var2.getHeight() && var11.getType() == 6) {
+                     BufferedImage var11 = Reflector.MinecraftForgeClient_getImageLayer.exists()
+                        ? (BufferedImage)Reflector.call(Reflector.MinecraftForgeClient_getImageLayer, new Object[]{new ResourceLocation(var9), var1})
+                        : TextureUtil.readBufferedImage(var8.getInputStream());
+                     if (var11.getWidth() == var3.getWidth() && var11.getHeight() == var3.getHeight() && var11.getType() == 6) {
                         for (int var12 = 0; var12 < var11.getHeight(); var12++) {
                            for (int var13 = 0; var13 < var11.getWidth(); var13++) {
                               int var14 = var11.getRGB(var13, var12);
                               if ((var14 & 0xFF000000) != 0) {
                                  int var15 = (var14 & 0xFF0000) << 8 & 0xFF000000;
                                  int var16 = var4.getRGB(var13, var12);
                                  int var17 = MathHelper.multiplyColor(var16, var10) & 16777215;
                                  var11.setRGB(var13, var12, var15 | var17);
                               }
                            }
                         }

-                        var2.getGraphics().drawImage(var11, 0, 0, null);
+                        var3.getGraphics().drawImage(var11, 0, 0, (ImageObserver)null);
                      }
                   }
                } finally {
                   IOUtils.closeQuietly(var8);
                }

                var7++;
             }
          } catch (IOException var27) {
             LOGGER.error("Couldn't load layered image", var27);
          } finally {
-            IOUtils.closeQuietly(var3);
+            IOUtils.closeQuietly(var2);
          }

          return;
       }

-      TextureUtil.uploadTextureImage(this.getGlTextureId(), var2);
+      if (Config.isShaders()) {
+         ShadersTex.loadSimpleTexture(this.getGlTextureId(), var3, false, false, var1, this.textureLocation, this.getMultiTexID());
+      } else {
+         TextureUtil.uploadTextureImage(this.getGlTextureId(), var3);
+      }
    }
 }
 */
