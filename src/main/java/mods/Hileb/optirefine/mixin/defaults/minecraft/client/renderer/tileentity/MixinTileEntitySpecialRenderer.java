package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Implements;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.IEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Implements(IEntityRenderer.class)
@Mixin(TileEntitySpecialRenderer.class)
public abstract class MixinTileEntitySpecialRenderer{
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private Class<?> tileEntityClass = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private ResourceLocation locationTextureCustom = null;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public Class<?> getEntityClass() {
        return this.tileEntityClass;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setEntityClass(Class<?> tileEntityClass) {
        this.tileEntityClass = tileEntityClass;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public ResourceLocation getLocationTextureCustom() {
        return this.locationTextureCustom;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setLocationTextureCustom(ResourceLocation locationTextureCustom) {
        this.locationTextureCustom = locationTextureCustom;
    }

}
/*
+++ net/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer.java	Tue Aug 19 14:59:58 2025
@@ -1,20 +1,22 @@
 package net.minecraft.client.renderer.tileentity;

 import net.minecraft.client.gui.FontRenderer;
+import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.EntityRenderer;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.texture.TextureManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.world.World;
+import net.optifine.entity.model.IEntityRenderer;

-public abstract class TileEntitySpecialRenderer<T extends TileEntity> {
+public abstract class TileEntitySpecialRenderer<T extends TileEntity> implements IEntityRenderer {
    protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{
       new ResourceLocation("textures/blocks/destroy_stage_0.png"),
       new ResourceLocation("textures/blocks/destroy_stage_1.png"),
       new ResourceLocation("textures/blocks/destroy_stage_2.png"),
       new ResourceLocation("textures/blocks/destroy_stage_3.png"),
       new ResourceLocation("textures/blocks/destroy_stage_4.png"),
@@ -22,12 +24,14 @@
       new ResourceLocation("textures/blocks/destroy_stage_6.png"),
       new ResourceLocation("textures/blocks/destroy_stage_7.png"),
       new ResourceLocation("textures/blocks/destroy_stage_8.png"),
       new ResourceLocation("textures/blocks/destroy_stage_9.png")
    };
    protected TileEntityRendererDispatcher rendererDispatcher;
+   private Class tileEntityClass = null;
+   private ResourceLocation locationTextureCustom = null;

    public void render(T var1, double var2, double var4, double var6, float var8, int var9, float var10) {
       ITextComponent var11 = var1.getDisplayName();
       if (var11 != null && this.rendererDispatcher.cameraHitResult != null && var1.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
          this.setLightmapDisabled(true);
          this.drawNameplate((T)var1, var11.getFormattedText(), var2, var4, var6, 12);
@@ -69,14 +73,33 @@
       return false;
    }

    protected void drawNameplate(T var1, String var2, double var3, double var5, double var7, int var9) {
       Entity var10 = this.rendererDispatcher.entity;
       double var11 = var1.getDistanceSq(var10.posX, var10.posY, var10.posZ);
-      if (!(var11 > var9 * var9)) {
+      if (var11 <= var9 * var9) {
          float var13 = this.rendererDispatcher.entityYaw;
          float var14 = this.rendererDispatcher.entityPitch;
          boolean var15 = false;
          EntityRenderer.drawNameplate(this.getFontRenderer(), var2, (float)var3 + 0.5F, (float)var5 + 1.5F, (float)var7 + 0.5F, 0, var13, var14, false, false);
       }
+   }
+
+   public void renderTileEntityFast(T var1, double var2, double var4, double var6, float var8, int var9, float var10, BufferBuilder var11) {
+   }
+
+   public Class getEntityClass() {
+      return this.tileEntityClass;
+   }
+
+   public void setEntityClass(Class var1) {
+      this.tileEntityClass = var1;
+   }
+
+   public ResourceLocation getLocationTextureCustom() {
+      return this.locationTextureCustom;
+   }
+
+   public void setLocationTextureCustom(ResourceLocation var1) {
+      this.locationTextureCustom = var1;
    }
 }
 */
