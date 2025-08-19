package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.debug;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.debug.DebugRendererChunkBorder;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DebugRendererChunkBorder.class)
public abstract class MixinDebugRendererChunkBorder {
    @WrapMethod(method = "render")
    public void isShadowPas_render(float partialTicks, long finishTimeNano, Operation<Void> original){
        if (!Shaders.isShadowPass) {
            if (Config.isShaders()) {
                Shaders.beginLeash();
            }
            original.call(partialTicks, finishTimeNano);
            if (Config.isShaders()) {
                Shaders.endLeash();
            }
        }
    }
}
/*
+++ net/minecraft/client/renderer/debug/DebugRendererChunkBorder.java	Tue Aug 19 14:59:58 2025
@@ -2,103 +2,114 @@

 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
+import net.minecraft.client.renderer.debug.DebugRenderer.IDebugRenderer;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
+import net.optifine.shaders.Shaders;

-public class DebugRendererChunkBorder implements DebugRenderer.IDebugRenderer {
+public class DebugRendererChunkBorder implements IDebugRenderer {
    private final Minecraft minecraft;

    public DebugRendererChunkBorder(Minecraft var1) {
       this.minecraft = var1;
    }

    public void render(float var1, long var2) {
-      EntityPlayerSP var4 = this.minecraft.player;
-      Tessellator var5 = Tessellator.getInstance();
-      BufferBuilder var6 = var5.getBuffer();
-      double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * var1;
-      double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * var1;
-      double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * var1;
-      double var13 = 0.0 - var9;
-      double var15 = 256.0 - var9;
-      GlStateManager.disableTexture2D();
-      GlStateManager.disableBlend();
-      double var17 = (var4.chunkCoordX << 4) - var7;
-      double var19 = (var4.chunkCoordZ << 4) - var11;
-      GlStateManager.glLineWidth(1.0F);
-      var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
-
-      for (byte var21 = -16; var21 <= 32; var21 += 16) {
-         for (byte var22 = -16; var22 <= 32; var22 += 16) {
-            var6.pos(var17 + var21, var13, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
-            var6.pos(var17 + var21, var13, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
-            var6.pos(var17 + var21, var15, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
-            var6.pos(var17 + var21, var15, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
+      if (!Shaders.isShadowPass) {
+         if (Config.isShaders()) {
+            Shaders.beginLeash();
          }
-      }

-      for (byte var24 = 2; var24 < 16; var24 += 2) {
-         var6.pos(var17 + var24, var13, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-         var6.pos(var17 + var24, var13, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + var24, var15, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + var24, var15, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-         var6.pos(var17 + var24, var13, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-         var6.pos(var17 + var24, var13, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + var24, var15, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + var24, var15, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-      }
+         EntityPlayerSP var4 = this.minecraft.player;
+         Tessellator var5 = Tessellator.getInstance();
+         BufferBuilder var6 = var5.getBuffer();
+         double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * var1;
+         double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * var1;
+         double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * var1;
+         double var13 = 0.0 - var9;
+         double var15 = 256.0 - var9;
+         GlStateManager.disableTexture2D();
+         GlStateManager.disableBlend();
+         double var17 = (var4.chunkCoordX << 4) - var7;
+         double var19 = (var4.chunkCoordZ << 4) - var11;
+         GlStateManager.glLineWidth(1.0F);
+         var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
+
+         for (byte var21 = -16; var21 <= 32; var21 += 16) {
+            for (byte var22 = -16; var22 <= 32; var22 += 16) {
+               var6.pos(var17 + var21, var13, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
+               var6.pos(var17 + var21, var13, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
+               var6.pos(var17 + var21, var15, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
+               var6.pos(var17 + var21, var15, var19 + var22).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
+            }
+         }

-      for (byte var25 = 2; var25 < 16; var25 += 2) {
-         var6.pos(var17, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-         var6.pos(var17, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-         var6.pos(var17 + 16.0, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-         var6.pos(var17 + 16.0, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + 16.0, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + 16.0, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-      }
+         for (byte var24 = 2; var24 < 16; var24 += 2) {
+            var6.pos(var17 + var24, var13, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+            var6.pos(var17 + var24, var13, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + var24, var15, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + var24, var15, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+            var6.pos(var17 + var24, var13, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+            var6.pos(var17 + var24, var13, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + var24, var15, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + var24, var15, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+         }

-      for (byte var26 = 0; var26 <= 256; var26 += 2) {
-         double var29 = var26 - var9;
-         var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-         var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17, var29, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + 16.0, var29, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17 + 16.0, var29, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
-         var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
-      }
+         for (byte var25 = 2; var25 < 16; var25 += 2) {
+            var6.pos(var17, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+            var6.pos(var17, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+            var6.pos(var17 + 16.0, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+            var6.pos(var17 + 16.0, var13, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + 16.0, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + 16.0, var15, var19 + var25).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+         }

-      var5.draw();
-      GlStateManager.glLineWidth(2.0F);
-      var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
-
-      for (byte var27 = 0; var27 <= 16; var27 += 16) {
-         for (byte var30 = 0; var30 <= 16; var30 += 16) {
-            var6.pos(var17 + var27, var13, var19 + var30).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
-            var6.pos(var17 + var27, var13, var19 + var30).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
-            var6.pos(var17 + var27, var15, var19 + var30).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
-            var6.pos(var17 + var27, var15, var19 + var30).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
+         for (byte var26 = 0; var26 <= 256; var26 += 2) {
+            double var29 = var26 - var9;
+            var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
+            var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17, var29, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + 16.0, var29, var19 + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17 + 16.0, var29, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
+            var6.pos(var17, var29, var19).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
          }
-      }

-      for (byte var28 = 0; var28 <= 256; var28 += 16) {
-         double var31 = var28 - var9;
-         var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
-         var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
-         var6.pos(var17, var31, var19 + 16.0).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
-         var6.pos(var17 + 16.0, var31, var19 + 16.0).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
-         var6.pos(var17 + 16.0, var31, var19).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
-         var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
-         var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
-      }
+         var5.draw();
+         GlStateManager.glLineWidth(2.0F);
+         var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
+
+         for (byte var27 = 0; var27 <= 16; var27 += 16) {
+            for (byte var30 = 0; var30 <= 16; var30 += 16) {
+               var6.pos(var17 + var27, var13, var19 + var30).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
+               var6.pos(var17 + var27, var13, var19 + var30).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
+               var6.pos(var17 + var27, var15, var19 + var30).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
+               var6.pos(var17 + var27, var15, var19 + var30).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
+            }
+         }

-      var5.draw();
-      GlStateManager.glLineWidth(1.0F);
-      GlStateManager.enableBlend();
-      GlStateManager.enableTexture2D();
+         for (byte var28 = 0; var28 <= 256; var28 += 16) {
+            double var31 = var28 - var9;
+            var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
+            var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
+            var6.pos(var17, var31, var19 + 16.0).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
+            var6.pos(var17 + 16.0, var31, var19 + 16.0).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
+            var6.pos(var17 + 16.0, var31, var19).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
+            var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
+            var6.pos(var17, var31, var19).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
+         }
+
+         var5.draw();
+         GlStateManager.glLineWidth(1.0F);
+         GlStateManager.enableBlend();
+         GlStateManager.enableTexture2D();
+         if (Config.isShaders()) {
+            Shaders.endLeash();
+         }
+      }
    }
 }
 */
