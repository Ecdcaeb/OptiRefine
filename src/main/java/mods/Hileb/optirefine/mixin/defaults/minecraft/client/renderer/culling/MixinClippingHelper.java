package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.culling;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.culling.ClippingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClippingHelper.class)
public abstract class MixinClippingHelper {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean disabled = false;

    @Shadow
    public float[][] frustum;

    @WrapMethod(method = "isBoxInFrustum")
    public boolean optional_isBoxInFrustum(double d, double e, double f, double g, double h, double i, Operation<Boolean> original){
        if (disabled) {
            return true;
        } else  return original.call(d, e, f, g, h, i);
    }


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (this.disabled) {
            return true;
        } else {
            float minXf = (float)minX;
            float minYf = (float)minY;
            float minZf = (float)minZ;
            float maxXf = (float)maxX;
            float maxYf = (float)maxY;
            float maxZf = (float)maxZ;

            for (int i = 0; i < 6; i++) {
                float[] frustumi = this.frustum[i];
                float frustumi0 = frustumi[0];
                float frustumi1 = frustumi[1];
                float frustumi2 = frustumi[2];
                float frustumi3 = frustumi[3];
                if (i < 4) {
                    if (frustumi0 * minXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                            || frustumi0 * minXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                            || frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                            || frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F) {
                        return false;
                    }
                } else if (frustumi0 * minXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0F
                        && frustumi0 * minXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                        && frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F
                        && frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0F) {
                    return false;
                }
            }

            return true;
        }
    }

}
/*
+++ net/minecraft/client/renderer/culling/ClippingHelper.java	Tue Aug 19 14:59:58 2025
@@ -2,29 +2,89 @@

 public class ClippingHelper {
    public float[][] frustum = new float[6][4];
    public float[] projectionMatrix = new float[16];
    public float[] modelviewMatrix = new float[16];
    public float[] clippingMatrix = new float[16];
+   public boolean disabled = false;

-   private double dot(float[] var1, double var2, double var4, double var6) {
-      return var1[0] * var2 + var1[1] * var4 + var1[2] * var6 + var1[3];
+   private float dot(float[] var1, float var2, float var3, float var4) {
+      return var1[0] * var2 + var1[1] * var3 + var1[2] * var4 + var1[3];
    }

    public boolean isBoxInFrustum(double var1, double var3, double var5, double var7, double var9, double var11) {
-      for (int var13 = 0; var13 < 6; var13++) {
-         float[] var14 = this.frustum[var13];
-         if (!(this.dot(var14, var1, var3, var5) > 0.0)
-            && !(this.dot(var14, var7, var3, var5) > 0.0)
-            && !(this.dot(var14, var1, var9, var5) > 0.0)
-            && !(this.dot(var14, var7, var9, var5) > 0.0)
-            && !(this.dot(var14, var1, var3, var11) > 0.0)
-            && !(this.dot(var14, var7, var3, var11) > 0.0)
-            && !(this.dot(var14, var1, var9, var11) > 0.0)
-            && !(this.dot(var14, var7, var9, var11) > 0.0)) {
-            return false;
+      if (this.disabled) {
+         return true;
+      } else {
+         float var13 = (float)var1;
+         float var14 = (float)var3;
+         float var15 = (float)var5;
+         float var16 = (float)var7;
+         float var17 = (float)var9;
+         float var18 = (float)var11;
+
+         for (int var19 = 0; var19 < 6; var19++) {
+            float[] var20 = this.frustum[var19];
+            float var21 = var20[0];
+            float var22 = var20[1];
+            float var23 = var20[2];
+            float var24 = var20[3];
+            if (var21 * var13 + var22 * var14 + var23 * var15 + var24 <= 0.0F
+               && var21 * var16 + var22 * var14 + var23 * var15 + var24 <= 0.0F
+               && var21 * var13 + var22 * var17 + var23 * var15 + var24 <= 0.0F
+               && var21 * var16 + var22 * var17 + var23 * var15 + var24 <= 0.0F
+               && var21 * var13 + var22 * var14 + var23 * var18 + var24 <= 0.0F
+               && var21 * var16 + var22 * var14 + var23 * var18 + var24 <= 0.0F
+               && var21 * var13 + var22 * var17 + var23 * var18 + var24 <= 0.0F
+               && var21 * var16 + var22 * var17 + var23 * var18 + var24 <= 0.0F) {
+               return false;
+            }
          }
+
+         return true;
       }
+   }
+
+   public boolean isBoxInFrustumFully(double var1, double var3, double var5, double var7, double var9, double var11) {
+      if (this.disabled) {
+         return true;
+      } else {
+         float var13 = (float)var1;
+         float var14 = (float)var3;
+         float var15 = (float)var5;
+         float var16 = (float)var7;
+         float var17 = (float)var9;
+         float var18 = (float)var11;

-      return true;
+         for (int var19 = 0; var19 < 6; var19++) {
+            float[] var20 = this.frustum[var19];
+            float var21 = var20[0];
+            float var22 = var20[1];
+            float var23 = var20[2];
+            float var24 = var20[3];
+            if (var19 < 4) {
+               if (var21 * var13 + var22 * var14 + var23 * var15 + var24 <= 0.0F
+                  || var21 * var16 + var22 * var14 + var23 * var15 + var24 <= 0.0F
+                  || var21 * var13 + var22 * var17 + var23 * var15 + var24 <= 0.0F
+                  || var21 * var16 + var22 * var17 + var23 * var15 + var24 <= 0.0F
+                  || var21 * var13 + var22 * var14 + var23 * var18 + var24 <= 0.0F
+                  || var21 * var16 + var22 * var14 + var23 * var18 + var24 <= 0.0F
+                  || var21 * var13 + var22 * var17 + var23 * var18 + var24 <= 0.0F
+                  || var21 * var16 + var22 * var17 + var23 * var18 + var24 <= 0.0F) {
+                  return false;
+               }
+            } else if (var21 * var13 + var22 * var14 + var23 * var15 + var24 <= 0.0F
+               && var21 * var16 + var22 * var14 + var23 * var15 + var24 <= 0.0F
+               && var21 * var13 + var22 * var17 + var23 * var15 + var24 <= 0.0F
+               && var21 * var16 + var22 * var17 + var23 * var15 + var24 <= 0.0F
+               && var21 * var13 + var22 * var14 + var23 * var18 + var24 <= 0.0F
+               && var21 * var16 + var22 * var14 + var23 * var18 + var24 <= 0.0F
+               && var21 * var13 + var22 * var17 + var23 * var18 + var24 <= 0.0F
+               && var21 * var16 + var22 * var17 + var23 * var18 + var24 <= 0.0F) {
+               return false;
+            }
+         }
+
+         return true;
+      }
    }
 }
 */
