package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.optifine.EmissiveTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class MixinTileEntityRendererDispatcher {

    @WrapOperation(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V"))
    public void render$(TileEntityRendererDispatcher instance, TileEntity v0, double v1, double v2, double v3, float v4, int v5, float v6, Operation<Void> original){
        if (EmissiveTextures.isActive()) {
            EmissiveTextures.beginRender();
        }

        original.call(instance, v0, v1, v2, v3, v4, v5, v6);
        if (EmissiveTextures.isActive()) {
            if (EmissiveTextures.hasEmissive()) {
                EmissiveTextures.beginRenderEmissive();
                original.call(instance, v0, v1, v2, v3, v4, v5, v6);
                EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
        }
    }
}

/*
+++ net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.java	Tue Aug 19 14:59:58 2025
@@ -1,17 +1,21 @@
 package net.minecraft.client.renderer.tileentity;

 import com.google.common.collect.Maps;
 import java.util.Map;
 import javax.annotation.Nullable;
+import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.FontRenderer;
 import net.minecraft.client.model.ModelShulker;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.RenderHelper;
+import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.texture.TextureManager;
+import net.minecraft.client.renderer.texture.TextureMap;
+import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.CrashReportCategory;
 import net.minecraft.entity.Entity;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.tileentity.TileEntityBanner;
 import net.minecraft.tileentity.TileEntityBeacon;
@@ -27,30 +31,36 @@
 import net.minecraft.tileentity.TileEntitySign;
 import net.minecraft.tileentity.TileEntitySkull;
 import net.minecraft.tileentity.TileEntityStructure;
 import net.minecraft.util.ReportedException;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.RayTraceResult;
+import net.minecraft.util.math.Vec3d;
 import net.minecraft.world.World;
+import net.optifine.EmissiveTextures;
+import net.optifine.reflect.Reflector;

 public class TileEntityRendererDispatcher {
-   private final Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> renderers = Maps.newHashMap();
+   public final Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> renderers = Maps.newHashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
-   private FontRenderer fontRenderer;
+   public FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World world;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public RayTraceResult cameraHitResult;
    public double entityX;
    public double entityY;
    public double entityZ;
+   public TileEntity tileEntityRendered;
+   private Tessellator batchBuffer = new Tessellator(2097152);
+   private boolean drawingBatch = false;

    private TileEntityRendererDispatcher() {
       this.renderers.put(TileEntitySign.class, new TileEntitySignRenderer());
       this.renderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
       this.renderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
       this.renderers.put(TileEntityChest.class, new TileEntityChestRenderer());
@@ -79,13 +89,13 @@

       return var2;
    }

    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(@Nullable TileEntity var1) {
-      return var1 == null ? null : this.getRenderer((Class<? extends TileEntity>)var1.getClass());
+      return var1 != null && !var1.isInvalid() ? this.getRenderer((Class<? extends TileEntity>)var1.getClass()) : null;
    }

    public void prepare(World var1, TextureManager var2, FontRenderer var3, Entity var4, RayTraceResult var5, float var6) {
       if (this.world != var1) {
          this.setWorld(var1);
       }
@@ -100,20 +110,45 @@
       this.entityY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * var6;
       this.entityZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * var6;
    }

    public void render(TileEntity var1, float var2, int var3) {
       if (var1.getDistanceSq(this.entityX, this.entityY, this.entityZ) < var1.getMaxRenderDistanceSquared()) {
-         RenderHelper.enableStandardItemLighting();
-         int var4 = this.world.getCombinedLight(var1.getPos(), 0);
-         int var5 = var4 % 65536;
-         int var6 = var4 / 65536;
-         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var5, var6);
-         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-         BlockPos var7 = var1.getPos();
-         this.render(var1, var7.getX() - staticPlayerX, var7.getY() - staticPlayerY, var7.getZ() - staticPlayerZ, var2, var3, 1.0F);
+         boolean var4 = true;
+         if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
+            var4 = !this.drawingBatch || !Reflector.callBoolean(var1, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0]);
+         }
+
+         if (var4) {
+            RenderHelper.enableStandardItemLighting();
+            int var5 = this.world.getCombinedLight(var1.getPos(), 0);
+            int var6 = var5 % 65536;
+            int var7 = var5 / 65536;
+            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6, var7);
+            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+         }
+
+         BlockPos var8 = var1.getPos();
+         if (!this.world.isBlockLoaded(var8, false)) {
+            return;
+         }
+
+         if (EmissiveTextures.isActive()) {
+            EmissiveTextures.beginRender();
+         }
+
+         this.render(var1, var8.getX() - staticPlayerX, var8.getY() - staticPlayerY, var8.getZ() - staticPlayerZ, var2, var3, 1.0F);
+         if (EmissiveTextures.isActive()) {
+            if (EmissiveTextures.hasEmissive()) {
+               EmissiveTextures.beginRenderEmissive();
+               this.render(var1, var8.getX() - staticPlayerX, var8.getY() - staticPlayerY, var8.getZ() - staticPlayerZ, var2, var3, 1.0F);
+               EmissiveTextures.endRenderEmissive();
+            }
+
+            EmissiveTextures.endRender();
+         }
       }
    }

    public void render(TileEntity var1, double var2, double var4, double var6, float var8) {
       this.render(var1, var2, var4, var6, var8, 1.0F);
    }
@@ -123,13 +158,20 @@
    }

    public void render(TileEntity var1, double var2, double var4, double var6, float var8, int var9, float var10) {
       TileEntitySpecialRenderer var11 = this.getRenderer(var1);
       if (var11 != null) {
          try {
-            var11.render(var1, var2, var4, var6, var8, var9, var10);
+            this.tileEntityRendered = var1;
+            if (this.drawingBatch && Reflector.callBoolean(var1, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0])) {
+               var11.renderTileEntityFast(var1, var2, var4, var6, var8, var9, var10, this.batchBuffer.getBuffer());
+            } else {
+               var11.render(var1, var2, var4, var6, var8, var9, var10);
+            }
+
+            this.tileEntityRendered = null;
          } catch (Throwable var15) {
             CrashReport var13 = CrashReport.makeCrashReport(var15, "Rendering Block Entity");
             CrashReportCategory var14 = var13.makeCategory("Block Entity Details");
             var1.addInfoToCrashReport(var14);
             throw new ReportedException(var13);
          }
@@ -142,8 +184,39 @@
          this.entity = null;
       }
    }

    public FontRenderer getFontRenderer() {
       return this.fontRenderer;
+   }
+
+   public void preDrawBatch() {
+      this.batchBuffer.getBuffer().begin(7, DefaultVertexFormats.BLOCK);
+      this.drawingBatch = true;
+   }
+
+   public void drawBatch(int var1) {
+      this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
+      RenderHelper.disableStandardItemLighting();
+      GlStateManager.blendFunc(770, 771);
+      GlStateManager.enableBlend();
+      GlStateManager.disableCull();
+      if (Minecraft.isAmbientOcclusionEnabled()) {
+         GlStateManager.shadeModel(7425);
+      } else {
+         GlStateManager.shadeModel(7424);
+      }
+
+      if (var1 > 0) {
+         Vec3d var2 = (Vec3d)Reflector.call(Reflector.ActiveRenderInfo_getCameraPosition, new Object[0]);
+         if (var2 != null) {
+            this.batchBuffer.getBuffer().sortVertexData((float)var2.x, (float)var2.y, (float)var2.z);
+         } else {
+            this.batchBuffer.getBuffer().sortVertexData(0.0F, 0.0F, 0.0F);
+         }
+      }
+
+      this.batchBuffer.draw();
+      RenderHelper.enableStandardItemLighting();
+      this.drawingBatch = false;
    }
 }
 */
