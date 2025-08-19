package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLiving.class)
public abstract class MixinRenderLiving {
    @WrapMethod(method = "renderLeash")
    public void blockRenderLeashForConfig(EntityLiving entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            original.call(entityLivingIn, x, y, z, entityYaw, partialTicks);
        }
    }

    @Inject(method = "renderLeash", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableCull()V"))
    public void beforeRenderLeashForConfig(EntityLiving entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci){
        if (Config.isShaders()) {
            Shaders.beginLeash();
        }
    }


    @WrapOperation(method = "renderLeash", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableLighting()V"))
    public void afterRenderLeashForConfig(Operation<Void> original){
        if (Config.isShaders()) {
            Shaders.endLeash();
        }
        original.call();
    }
}

/*
+++ net/minecraft/client/renderer/entity/RenderLiving.java	Tue Aug 19 14:59:58 2025
@@ -7,12 +7,13 @@
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.culling.ICamera;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityHanging;
 import net.minecraft.entity.EntityLiving;
+import net.optifine.shaders.Shaders;

 public abstract class RenderLiving<T extends EntityLiving> extends RenderLivingBase<T> {
    public RenderLiving(RenderManager var1, ModelBase var2, float var3) {
       super(var1, var2, var3);
    }

@@ -47,98 +48,116 @@

    private double interpolateValue(double var1, double var3, double var5) {
       return var1 + (var3 - var1) * var5;
    }

    protected void renderLeash(T var1, double var2, double var4, double var6, float var8, float var9) {
-      Entity var10 = var1.getLeashHolder();
-      if (var10 != null) {
-         var4 -= (1.6 - var1.height) * 0.5;
-         Tessellator var11 = Tessellator.getInstance();
-         BufferBuilder var12 = var11.getBuffer();
-         double var13 = this.interpolateValue(var10.prevRotationYaw, var10.rotationYaw, var9 * 0.5F) * (float) (Math.PI / 180.0);
-         double var15 = this.interpolateValue(var10.prevRotationPitch, var10.rotationPitch, var9 * 0.5F) * (float) (Math.PI / 180.0);
-         double var17 = Math.cos(var13);
-         double var19 = Math.sin(var13);
-         double var21 = Math.sin(var15);
-         if (var10 instanceof EntityHanging) {
-            var17 = 0.0;
-            var19 = 0.0;
-            var21 = -1.0;
-         }
+      if (!Config.isShaders() || !Shaders.isShadowPass) {
+         Entity var10 = var1.getLeashHolder();
+         if (var10 != null) {
+            var4 -= (1.6 - var1.height) * 0.5;
+            Tessellator var11 = Tessellator.getInstance();
+            BufferBuilder var12 = var11.getBuffer();
+            double var13 = this.interpolateValue(var10.prevRotationYaw, var10.rotationYaw, var9 * 0.5F) * (float) (Math.PI / 180.0);
+            double var15 = this.interpolateValue(var10.prevRotationPitch, var10.rotationPitch, var9 * 0.5F) * (float) (Math.PI / 180.0);
+            double var17 = Math.cos(var13);
+            double var19 = Math.sin(var13);
+            double var21 = Math.sin(var15);
+            if (var10 instanceof EntityHanging) {
+               var17 = 0.0;
+               var19 = 0.0;
+               var21 = -1.0;
+            }

-         double var23 = Math.cos(var15);
-         double var25 = this.interpolateValue(var10.prevPosX, var10.posX, var9) - var17 * 0.7 - var19 * 0.5 * var23;
-         double var27 = this.interpolateValue(var10.prevPosY + var10.getEyeHeight() * 0.7, var10.posY + var10.getEyeHeight() * 0.7, var9) - var21 * 0.5 - 0.25;
-         double var29 = this.interpolateValue(var10.prevPosZ, var10.posZ, var9) - var19 * 0.7 + var17 * 0.5 * var23;
-         double var31 = this.interpolateValue(var1.prevRenderYawOffset, var1.renderYawOffset, var9) * (float) (Math.PI / 180.0) + (Math.PI / 2);
-         var17 = Math.cos(var31) * var1.width * 0.4;
-         var19 = Math.sin(var31) * var1.width * 0.4;
-         double var33 = this.interpolateValue(var1.prevPosX, var1.posX, var9) + var17;
-         double var35 = this.interpolateValue(var1.prevPosY, var1.posY, var9);
-         double var37 = this.interpolateValue(var1.prevPosZ, var1.posZ, var9) + var19;
-         var2 += var17;
-         var6 += var19;
-         double var39 = (float)(var25 - var33);
-         double var41 = (float)(var27 - var35);
-         double var43 = (float)(var29 - var37);
-         GlStateManager.disableTexture2D();
-         GlStateManager.disableLighting();
-         GlStateManager.disableCull();
-         byte var45 = 24;
-         double var46 = 0.025;
-         var12.begin(5, DefaultVertexFormats.POSITION_COLOR);
-
-         for (int var48 = 0; var48 <= 24; var48++) {
-            float var49 = 0.5F;
-            float var50 = 0.4F;
-            float var51 = 0.3F;
-            if (var48 % 2 == 0) {
-               var49 *= 0.7F;
-               var50 *= 0.7F;
-               var51 *= 0.7F;
+            double var23 = Math.cos(var15);
+            double var25 = this.interpolateValue(var10.prevPosX, var10.posX, var9) - var17 * 0.7 - var19 * 0.5 * var23;
+            double var27 = this.interpolateValue(var10.prevPosY + var10.getEyeHeight() * 0.7, var10.posY + var10.getEyeHeight() * 0.7, var9)
+               - var21 * 0.5
+               - 0.25;
+            double var29 = this.interpolateValue(var10.prevPosZ, var10.posZ, var9) - var19 * 0.7 + var17 * 0.5 * var23;
+            double var31 = this.interpolateValue(var1.prevRenderYawOffset, var1.renderYawOffset, var9) * (float) (Math.PI / 180.0) + (Math.PI / 2);
+            var17 = Math.cos(var31) * var1.width * 0.4;
+            var19 = Math.sin(var31) * var1.width * 0.4;
+            double var33 = this.interpolateValue(var1.prevPosX, var1.posX, var9) + var17;
+            double var35 = this.interpolateValue(var1.prevPosY, var1.posY, var9);
+            double var37 = this.interpolateValue(var1.prevPosZ, var1.posZ, var9) + var19;
+            var2 += var17;
+            var6 += var19;
+            double var39 = (float)(var25 - var33);
+            double var41 = (float)(var27 - var35);
+            double var43 = (float)(var29 - var37);
+            GlStateManager.disableTexture2D();
+            GlStateManager.disableLighting();
+            GlStateManager.disableCull();
+            if (Config.isShaders()) {
+               Shaders.beginLeash();
             }

-            float var52 = var48 / 24.0F;
-            var12.pos(var2 + var39 * var52 + 0.0, var4 + var41 * (var52 * var52 + var52) * 0.5 + ((24.0F - var48) / 18.0F + 0.125F), var6 + var43 * var52)
-               .color(var49, var50, var51, 1.0F)
-               .endVertex();
-            var12.pos(
-                  var2 + var39 * var52 + 0.025, var4 + var41 * (var52 * var52 + var52) * 0.5 + ((24.0F - var48) / 18.0F + 0.125F) + 0.025, var6 + var43 * var52
-               )
-               .color(var49, var50, var51, 1.0F)
-               .endVertex();
-         }
+            byte var45 = 24;
+            double var46 = 0.025;
+            var12.begin(5, DefaultVertexFormats.POSITION_COLOR);
+
+            for (int var48 = 0; var48 <= 24; var48++) {
+               float var49 = 0.5F;
+               float var50 = 0.4F;
+               float var51 = 0.3F;
+               if (var48 % 2 == 0) {
+                  var49 *= 0.7F;
+                  var50 *= 0.7F;
+                  var51 *= 0.7F;
+               }
+
+               float var52 = var48 / 24.0F;
+               var12.pos(var2 + var39 * var52 + 0.0, var4 + var41 * (var52 * var52 + var52) * 0.5 + ((24.0F - var48) / 18.0F + 0.125F), var6 + var43 * var52)
+                  .color(var49, var50, var51, 1.0F)
+                  .endVertex();
+               var12.pos(
+                     var2 + var39 * var52 + 0.025,
+                     var4 + var41 * (var52 * var52 + var52) * 0.5 + ((24.0F - var48) / 18.0F + 0.125F) + 0.025,
+                     var6 + var43 * var52
+                  )
+                  .color(var49, var50, var51, 1.0F)
+                  .endVertex();
+            }

-         var11.draw();
-         var12.begin(5, DefaultVertexFormats.POSITION_COLOR);
+            var11.draw();
+            var12.begin(5, DefaultVertexFormats.POSITION_COLOR);

-         for (int var58 = 0; var58 <= 24; var58++) {
-            float var59 = 0.5F;
-            float var60 = 0.4F;
-            float var61 = 0.3F;
-            if (var58 % 2 == 0) {
-               var59 *= 0.7F;
-               var60 *= 0.7F;
-               var61 *= 0.7F;
+            for (int var58 = 0; var58 <= 24; var58++) {
+               float var59 = 0.5F;
+               float var60 = 0.4F;
+               float var61 = 0.3F;
+               if (var58 % 2 == 0) {
+                  var59 *= 0.7F;
+                  var60 *= 0.7F;
+                  var61 *= 0.7F;
+               }
+
+               float var62 = var58 / 24.0F;
+               var12.pos(
+                     var2 + var39 * var62 + 0.0,
+                     var4 + var41 * (var62 * var62 + var62) * 0.5 + ((24.0F - var58) / 18.0F + 0.125F) + 0.025,
+                     var6 + var43 * var62
+                  )
+                  .color(var59, var60, var61, 1.0F)
+                  .endVertex();
+               var12.pos(
+                     var2 + var39 * var62 + 0.025,
+                     var4 + var41 * (var62 * var62 + var62) * 0.5 + ((24.0F - var58) / 18.0F + 0.125F),
+                     var6 + var43 * var62 + 0.025
+                  )
+                  .color(var59, var60, var61, 1.0F)
+                  .endVertex();
             }

-            float var62 = var58 / 24.0F;
-            var12.pos(
-                  var2 + var39 * var62 + 0.0, var4 + var41 * (var62 * var62 + var62) * 0.5 + ((24.0F - var58) / 18.0F + 0.125F) + 0.025, var6 + var43 * var62
-               )
-               .color(var59, var60, var61, 1.0F)
-               .endVertex();
-            var12.pos(
-                  var2 + var39 * var62 + 0.025, var4 + var41 * (var62 * var62 + var62) * 0.5 + ((24.0F - var58) / 18.0F + 0.125F), var6 + var43 * var62 + 0.025
-               )
-               .color(var59, var60, var61, 1.0F)
-               .endVertex();
-         }
+            var11.draw();
+            if (Config.isShaders()) {
+               Shaders.endLeash();
+            }

-         var11.draw();
-         GlStateManager.enableLighting();
-         GlStateManager.enableTexture2D();
-         GlStateManager.enableCull();
+            GlStateManager.enableLighting();
+            GlStateManager.enableTexture2D();
+            GlStateManager.enableCull();
+         }
       }
    }
 }
 */
