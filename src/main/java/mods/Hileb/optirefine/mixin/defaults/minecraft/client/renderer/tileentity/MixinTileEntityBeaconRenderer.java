package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(TileEntityBeaconRenderer.class)
public abstract class MixinTileEntityBeaconRenderer {

    @WrapMethod(method = "renderBeacon")
    public void _renderBeacon(double x, double y, double z, double partialTicks, double textureScale, List<TileEntityBeacon.BeamSegment> beamSegments, double totalWorldTime, Operation<Void> original) {
        if (!(textureScale <= 0.0) && !beamSegments.isEmpty()) {
            if (Config.isShaders()) {
                Shaders.beginBeacon();
            }
            original.call(x, y, z, partialTicks, textureScale, beamSegments, totalWorldTime);
            if (Config.isShaders()) {
                Shaders.endBeacon();
            }

        }
    }

    @WrapOperation(method = "renderBeamSegment(DDDDDDII[FDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V"))
    public void depthMask(boolean flagIn, Operation<Void> original){
        if (!flagIn) {
            GlStateManager.depthMask(false);
            if (Config.isShaders()) {
                GlStateManager.depthMask(Shaders.isBeaconBeamDepth());
            }
        } else original.call(true);
    }


}
/*
+++ net/minecraft/client/renderer/tileentity/TileEntityBeaconRenderer.java	Tue Aug 19 14:59:58 2025
@@ -3,36 +3,48 @@
 import java.util.List;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.tileentity.TileEntityBeacon;
+import net.minecraft.tileentity.TileEntityBeacon.BeamSegment;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
+import net.optifine.shaders.Shaders;

 public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer<TileEntityBeacon> {
    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public void render(TileEntityBeacon var1, double var2, double var4, double var6, float var8, int var9, float var10) {
-      this.renderBeacon(var2, var4, var6, var8, var1.shouldBeamRender(), var1.getBeamSegments(), var1.getWorld().getTotalWorldTime());
+      this.renderBeacon(var2, var4, var6, var8, var1.shouldBeamRender(), var1.getBeamSegments(), var1.D().getTotalWorldTime());
    }

-   public void renderBeacon(double var1, double var3, double var5, double var7, double var9, List<TileEntityBeacon.BeamSegment> var11, double var12) {
-      GlStateManager.alphaFunc(516, 0.1F);
-      this.bindTexture(TEXTURE_BEACON_BEAM);
-      if (var9 > 0.0) {
-         GlStateManager.disableFog();
-         int var14 = 0;
-
-         for (int var15 = 0; var15 < var11.size(); var15++) {
-            TileEntityBeacon.BeamSegment var16 = (TileEntityBeacon.BeamSegment)var11.get(var15);
-            renderBeamSegment(var1, var3, var5, var7, var9, var12, var14, var16.getHeight(), var16.getColors());
-            var14 += var16.getHeight();
+   public void renderBeacon(double var1, double var3, double var5, double var7, double var9, List<BeamSegment> var11, double var12) {
+      if (!(var9 <= 0.0) && var11.size() > 0) {
+         if (Config.isShaders()) {
+            Shaders.beginBeacon();
          }

-         GlStateManager.enableFog();
+         GlStateManager.alphaFunc(516, 0.1F);
+         this.bindTexture(TEXTURE_BEACON_BEAM);
+         if (var9 > 0.0) {
+            GlStateManager.disableFog();
+            int var14 = 0;
+
+            for (int var15 = 0; var15 < var11.size(); var15++) {
+               BeamSegment var16 = (BeamSegment)var11.get(var15);
+               renderBeamSegment(var1, var3, var5, var7, var9, var12, var14, var16.getHeight(), var16.getColors());
+               var14 += var16.getHeight();
+            }
+
+            GlStateManager.enableFog();
+         }
+
+         if (Config.isShaders()) {
+            Shaders.endBeacon();
+         }
       }
    }

    public static void renderBeamSegment(double var0, double var2, double var4, double var6, double var8, double var10, int var12, int var13, float[] var14) {
       renderBeamSegment(var0, var2, var4, var6, var8, var10, var12, var13, var14, 0.2, 0.25);
    }
@@ -91,12 +103,16 @@
       var20.draw();
       GlStateManager.enableBlend();
       GlStateManager.tryBlendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
       );
       GlStateManager.depthMask(false);
+      if (Config.isShaders()) {
+         GlStateManager.depthMask(Shaders.isBeaconBeamDepth());
+      }
+
       var31 = 0.5 - var17;
       var33 = 0.5 - var17;
       var35 = 0.5 + var17;
       var37 = 0.5 - var17;
       var39 = 0.5 - var17;
       var41 = 0.5 + var17;
 */
