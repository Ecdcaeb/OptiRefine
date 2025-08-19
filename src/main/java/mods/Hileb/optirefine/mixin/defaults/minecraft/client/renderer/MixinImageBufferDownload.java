package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.renderer.ImageBufferDownload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.image.BufferedImage;

@Mixin(ImageBufferDownload.class)
public abstract class MixinImageBufferDownload {
    @Shadow
    private int imageWidth;

    @Shadow
    private int imageHeight;

    @Redirect(method = "parseUserSkin", at = @At(value = "NEW", target = "(III)Ljava/awt/image/BufferedImage;"))
    public BufferedImage beforeBufferedImage(int nBits, int bOffs, int cs, @Local(argsOnly = true) BufferedImage image, @Share(namespace = "optirefine", value = "k") LocalIntRef intRef){
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        int k;
        for (k = 1; this.imageWidth < srcWidth || this.imageHeight < srcHeight; k *= 2) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
        }
        intRef.set(k);
        return new BufferedImage(this.imageWidth, this.imageHeight, 2);
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 32))
    public int const32(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 64))
    public int const64(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 20))
    public int const20(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 24))
    public int const24(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 28))
    public int const28(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 40))
    public int const40(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 44))
    public int const44(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 48))
    public int const48(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 52))
    public int const52(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 16))
    public int const16(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 36))
    public int const36(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 4))
    public int const4(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 8))
    public int const8(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 12))
    public int const12(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }
}
/*
+++ net/minecraft/client/renderer/ImageBufferDownload.java	Tue Aug 19 14:59:58 2025
@@ -1,12 +1,13 @@
 package net.minecraft.client.renderer;

 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.image.BufferedImage;
 import java.awt.image.DataBufferInt;
+import java.awt.image.ImageObserver;
 import javax.annotation.Nullable;

 public class ImageBufferDownload implements IImageBuffer {
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;
@@ -15,43 +16,52 @@
    public BufferedImage parseUserSkin(BufferedImage var1) {
       if (var1 == null) {
          return null;
       } else {
          this.imageWidth = 64;
          this.imageHeight = 64;
-         BufferedImage var2 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
-         Graphics var3 = var2.getGraphics();
-         var3.drawImage(var1, 0, 0, null);
-         boolean var4 = var1.getHeight() == 32;
-         if (var4) {
-            var3.setColor(new Color(0, 0, 0, 0));
-            var3.fillRect(0, 32, 64, 32);
-            var3.drawImage(var2, 24, 48, 20, 52, 4, 16, 8, 20, null);
-            var3.drawImage(var2, 28, 48, 24, 52, 8, 16, 12, 20, null);
-            var3.drawImage(var2, 20, 52, 16, 64, 8, 20, 12, 32, null);
-            var3.drawImage(var2, 24, 52, 20, 64, 4, 20, 8, 32, null);
-            var3.drawImage(var2, 28, 52, 24, 64, 0, 20, 4, 32, null);
-            var3.drawImage(var2, 32, 52, 28, 64, 12, 20, 16, 32, null);
-            var3.drawImage(var2, 40, 48, 36, 52, 44, 16, 48, 20, null);
-            var3.drawImage(var2, 44, 48, 40, 52, 48, 16, 52, 20, null);
-            var3.drawImage(var2, 36, 52, 32, 64, 48, 20, 52, 32, null);
-            var3.drawImage(var2, 40, 52, 36, 64, 44, 20, 48, 32, null);
-            var3.drawImage(var2, 44, 52, 40, 64, 40, 20, 44, 32, null);
-            var3.drawImage(var2, 48, 52, 44, 64, 52, 20, 56, 32, null);
+         int var3 = var1.getWidth();
+         int var4 = var1.getHeight();
+
+         byte var5;
+         for (var5 = 1; this.imageWidth < var3 || this.imageHeight < var4; var5 *= 2) {
+            this.imageWidth *= 2;
+            this.imageHeight *= 2;
+         }
+
+         BufferedImage var6 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
+         Graphics var7 = var6.getGraphics();
+         var7.drawImage(var1, 0, 0, (ImageObserver)null);
+         boolean var8 = var1.getHeight() == 32 * var5;
+         if (var8) {
+            var7.setColor(new Color(0, 0, 0, 0));
+            var7.fillRect(0 * var5, 32 * var5, 64 * var5, 32 * var5);
+            var7.drawImage(var6, 24 * var5, 48 * var5, 20 * var5, 52 * var5, 4 * var5, 16 * var5, 8 * var5, 20 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 28 * var5, 48 * var5, 24 * var5, 52 * var5, 8 * var5, 16 * var5, 12 * var5, 20 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 20 * var5, 52 * var5, 16 * var5, 64 * var5, 8 * var5, 20 * var5, 12 * var5, 32 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 24 * var5, 52 * var5, 20 * var5, 64 * var5, 4 * var5, 20 * var5, 8 * var5, 32 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 28 * var5, 52 * var5, 24 * var5, 64 * var5, 0 * var5, 20 * var5, 4 * var5, 32 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 32 * var5, 52 * var5, 28 * var5, 64 * var5, 12 * var5, 20 * var5, 16 * var5, 32 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 40 * var5, 48 * var5, 36 * var5, 52 * var5, 44 * var5, 16 * var5, 48 * var5, 20 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 44 * var5, 48 * var5, 40 * var5, 52 * var5, 48 * var5, 16 * var5, 52 * var5, 20 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 36 * var5, 52 * var5, 32 * var5, 64 * var5, 48 * var5, 20 * var5, 52 * var5, 32 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 40 * var5, 52 * var5, 36 * var5, 64 * var5, 44 * var5, 20 * var5, 48 * var5, 32 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 44 * var5, 52 * var5, 40 * var5, 64 * var5, 40 * var5, 20 * var5, 44 * var5, 32 * var5, (ImageObserver)null);
+            var7.drawImage(var6, 48 * var5, 52 * var5, 44 * var5, 64 * var5, 52 * var5, 20 * var5, 56 * var5, 32 * var5, (ImageObserver)null);
          }

-         var3.dispose();
-         this.imageData = ((DataBufferInt)var2.getRaster().getDataBuffer()).getData();
-         this.setAreaOpaque(0, 0, 32, 16);
-         if (var4) {
-            this.setAreaTransparent(32, 0, 64, 32);
+         var7.dispose();
+         this.imageData = ((DataBufferInt)var6.getRaster().getDataBuffer()).getData();
+         this.setAreaOpaque(0 * var5, 0 * var5, 32 * var5, 16 * var5);
+         if (var8) {
+            this.setAreaTransparent(32 * var5, 0 * var5, 64 * var5, 32 * var5);
          }

-         this.setAreaOpaque(0, 16, 64, 32);
-         this.setAreaOpaque(16, 48, 48, 64);
-         return var2;
+         this.setAreaOpaque(0 * var5, 16 * var5, 64 * var5, 32 * var5);
+         this.setAreaOpaque(16 * var5, 48 * var5, 48 * var5, 64 * var5);
+         return var6;
       }
    }

    public void skinAvailable() {
    }
 */
