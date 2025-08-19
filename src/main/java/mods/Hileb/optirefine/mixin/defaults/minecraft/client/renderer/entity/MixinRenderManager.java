package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.player.PlayerItemsLayer;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager {

    @Unique
    public Render<?> renderRender = null;

    @Shadow
    @Final
    private Map<String, RenderPlayer> skinMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(TextureManager p_i46180_1, RenderItem p_i46180_2, CallbackInfo ci){
        PlayerItemsLayer.register(this.skinMap);
    }

    @WrapOperation(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;doRender(Lnet/minecraft/entity/Entity;DDDFF)V"))
    public void beforeDoRender(Render<Entity> instance, Entity entity, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (CustomEntityModels.isActive()) {
            this.renderRender = instance;
        }
        original.call(instance, entity, x, y, z, entityYaw, partialTicks);
    }

    @WrapMethod(method = "renderDebugBoundingBox")
    public void blockRenderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (!Shaders.isShadowPass) {
            original.call(entityIn, x, y, z, entityYaw, partialTicks);
        }
    }

    @Shadow @Final
    public Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap;

    @Unique
    public Map<Class<? extends Entity>, Render<? extends Entity>> getEntityRenderMap() {
        return this.entityRenderMap;
    }
}
/*
+++ net/minecraft/client/renderer/entity/RenderManager.java	Tue Aug 19 14:59:58 2025
@@ -1,9 +1,10 @@
 package net.minecraft.client.renderer.entity;

 import com.google.common.collect.Maps;
+import java.util.Collections;
 import java.util.Map;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
 import net.minecraft.block.BlockBed;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
@@ -103,17 +104,22 @@
 import net.minecraft.entity.projectile.EntitySnowball;
 import net.minecraft.entity.projectile.EntitySpectralArrow;
 import net.minecraft.entity.projectile.EntityTippedArrow;
 import net.minecraft.entity.projectile.EntityWitherSkull;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
+import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ReportedException;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Vec3d;
 import net.minecraft.world.World;
+import net.optifine.entity.model.CustomEntityModels;
+import net.optifine.player.PlayerItemsLayer;
+import net.optifine.reflect.Reflector;
+import net.optifine.shaders.Shaders;

 public class RenderManager {
    private final Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap = Maps.newHashMap();
    private final Map<String, RenderPlayer> skinMap = Maps.newHashMap();
    private final RenderPlayer playerRenderer;
    private FontRenderer textRenderer;
@@ -130,12 +136,13 @@
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean renderOutlines;
    private boolean renderShadow = true;
    private boolean debugBoundingBox;
+   public Render renderRender = null;

    public RenderManager(TextureManager var1, RenderItem var2) {
       this.renderEngine = var1;
       this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
       this.entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
       this.entityRenderMap.put(EntityPig.class, new RenderPig(this));
@@ -183,19 +190,19 @@
       this.entityRenderMap.put(Entity.class, new RenderEntity(this));
       this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
       this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, var2));
       this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
       this.entityRenderMap.put(EntityTippedArrow.class, new RenderTippedArrow(this));
       this.entityRenderMap.put(EntitySpectralArrow.class, new RenderSpectralArrow(this));
-      this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball<>(this, Items.SNOWBALL, var2));
-      this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball<>(this, Items.ENDER_PEARL, var2));
-      this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball<>(this, Items.ENDER_EYE, var2));
-      this.entityRenderMap.put(EntityEgg.class, new RenderSnowball<>(this, Items.EGG, var2));
+      this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(this, Items.SNOWBALL, var2));
+      this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ENDER_PEARL, var2));
+      this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(this, Items.ENDER_EYE, var2));
+      this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(this, Items.EGG, var2));
       this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, var2));
-      this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball<>(this, Items.EXPERIENCE_BOTTLE, var2));
-      this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball<>(this, Items.FIREWORKS, var2));
+      this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(this, Items.EXPERIENCE_BOTTLE, var2));
+      this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.FIREWORKS, var2));
       this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
       this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
       this.entityRenderMap.put(EntityDragonFireball.class, new RenderDragonFireball(this));
       this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
       this.entityRenderMap.put(EntityShulkerBullet.class, new RenderShulkerBullet(this));
       this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, var2));
@@ -218,12 +225,16 @@
       this.entityRenderMap.put(EntityLlama.class, new RenderLlama(this));
       this.entityRenderMap.put(EntityLlamaSpit.class, new RenderLlamaSpit(this));
       this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
       this.playerRenderer = new RenderPlayer(this);
       this.skinMap.put("default", this.playerRenderer);
       this.skinMap.put("slim", new RenderPlayer(this, true));
+      PlayerItemsLayer.register(this.skinMap);
+      if (Reflector.RenderingRegistry_loadEntityRenderers.exists()) {
+         Reflector.call(Reflector.RenderingRegistry_loadEntityRenderers, new Object[]{this, this.entityRenderMap});
+      }
    }

    public void setRenderPosition(double var1, double var3, double var5) {
       this.renderPosX = var1;
       this.renderPosY = var3;
       this.renderPosZ = var5;
@@ -256,15 +267,20 @@
       this.renderViewEntity = var3;
       this.pointedEntity = var4;
       this.textRenderer = var2;
       if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPlayerSleeping()) {
          IBlockState var7 = var1.getBlockState(new BlockPos(var3));
          Block var8 = var7.getBlock();
-         if (var8 == Blocks.BED) {
-            int var9 = var7.getValue(BlockBed.FACING).getHorizontalIndex();
-            this.playerViewY = var9 * 90 + 180;
+         if (Reflector.callBoolean(var8, Reflector.ForgeBlock_isBed, new Object[]{var7, var1, new BlockPos(var3), (EntityLivingBase)var3})) {
+            EnumFacing var9 = (EnumFacing)Reflector.call(var8, Reflector.ForgeBlock_getBedDirection, new Object[]{var7, var1, new BlockPos(var3)});
+            int var10 = var9.getHorizontalIndex();
+            this.playerViewY = var10 * 90 + 180;
+            this.playerViewX = 0.0F;
+         } else if (var8 == Blocks.BED) {
+            int var11 = ((EnumFacing)var7.getValue(BlockBed.D)).getHorizontalIndex();
+            this.playerViewY = var11 * 90 + 180;
             this.playerViewX = 0.0F;
          }
       } else {
          this.playerViewY = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var6;
          this.playerViewX = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var6;
       }
@@ -335,23 +351,27 @@

       try {
          var11 = this.getEntityRenderObject(var1);
          if (var11 != null && this.renderEngine != null) {
             try {
                var11.setRenderOutlines(this.renderOutlines);
+               if (CustomEntityModels.isActive()) {
+                  this.renderRender = var11;
+               }
+
                var11.doRender(var1, var2, var4, var6, var8, var9);
-            } catch (Throwable var17) {
-               throw new ReportedException(CrashReport.makeCrashReport(var17, "Rendering entity in world"));
+            } catch (Throwable var18) {
+               throw new ReportedException(CrashReport.makeCrashReport(var18, "Rendering entity in world"));
             }

             try {
                if (!this.renderOutlines) {
                   var11.doRenderShadowAndFire(var1, var2, var4, var6, var8, var9);
                }
-            } catch (Throwable var18) {
-               throw new ReportedException(CrashReport.makeCrashReport(var18, "Post-rendering entity in world"));
+            } catch (Throwable var17) {
+               throw new ReportedException(CrashReport.makeCrashReport(var17, "Post-rendering entity in world"));
             }

             if (this.debugBoundingBox && !var1.isInvisible() && !var10 && !Minecraft.getMinecraft().isReducedDebug()) {
                try {
                   this.renderDebugBoundingBox(var1, var2, var4, var6, var8, var9);
                } catch (Throwable var16) {
@@ -396,81 +416,83 @@
       if (var13 != null && this.renderEngine != null) {
          var13.renderMultipass(var1, var3 - this.renderPosX, var5 - this.renderPosY, var7 - this.renderPosZ, var9, var2);
       }
    }

    private void renderDebugBoundingBox(Entity var1, double var2, double var4, double var6, float var8, float var9) {
-      GlStateManager.depthMask(false);
-      GlStateManager.disableTexture2D();
-      GlStateManager.disableLighting();
-      GlStateManager.disableCull();
-      GlStateManager.disableBlend();
-      float var10 = var1.width / 2.0F;
-      AxisAlignedBB var11 = var1.getEntityBoundingBox();
-      RenderGlobal.drawBoundingBox(
-         var11.minX - var1.posX + var2,
-         var11.minY - var1.posY + var4,
-         var11.minZ - var1.posZ + var6,
-         var11.maxX - var1.posX + var2,
-         var11.maxY - var1.posY + var4,
-         var11.maxZ - var1.posZ + var6,
-         1.0F,
-         1.0F,
-         1.0F,
-         1.0F
-      );
-      Entity[] var12 = var1.getParts();
-      if (var12 != null) {
-         for (Entity var16 : var12) {
-            double var17 = (var16.posX - var16.prevPosX) * var9;
-            double var19 = (var16.posY - var16.prevPosY) * var9;
-            double var21 = (var16.posZ - var16.prevPosZ) * var9;
-            AxisAlignedBB var23 = var16.getEntityBoundingBox();
+      if (!Shaders.isShadowPass) {
+         GlStateManager.depthMask(false);
+         GlStateManager.disableTexture2D();
+         GlStateManager.disableLighting();
+         GlStateManager.disableCull();
+         GlStateManager.disableBlend();
+         float var10 = var1.width / 2.0F;
+         AxisAlignedBB var11 = var1.getEntityBoundingBox();
+         RenderGlobal.drawBoundingBox(
+            var11.minX - var1.posX + var2,
+            var11.minY - var1.posY + var4,
+            var11.minZ - var1.posZ + var6,
+            var11.maxX - var1.posX + var2,
+            var11.maxY - var1.posY + var4,
+            var11.maxZ - var1.posZ + var6,
+            1.0F,
+            1.0F,
+            1.0F,
+            1.0F
+         );
+         Entity[] var12 = var1.getParts();
+         if (var12 != null) {
+            for (Entity var16 : var12) {
+               double var17 = (var16.posX - var16.prevPosX) * var9;
+               double var19 = (var16.posY - var16.prevPosY) * var9;
+               double var21 = (var16.posZ - var16.prevPosZ) * var9;
+               AxisAlignedBB var23 = var16.getEntityBoundingBox();
+               RenderGlobal.drawBoundingBox(
+                  var23.minX - this.renderPosX + var17,
+                  var23.minY - this.renderPosY + var19,
+                  var23.minZ - this.renderPosZ + var21,
+                  var23.maxX - this.renderPosX + var17,
+                  var23.maxY - this.renderPosY + var19,
+                  var23.maxZ - this.renderPosZ + var21,
+                  0.25F,
+                  1.0F,
+                  0.0F,
+                  1.0F
+               );
+            }
+         }
+
+         if (var1 instanceof EntityLivingBase) {
+            float var24 = 0.01F;
             RenderGlobal.drawBoundingBox(
-               var23.minX - this.renderPosX + var17,
-               var23.minY - this.renderPosY + var19,
-               var23.minZ - this.renderPosZ + var21,
-               var23.maxX - this.renderPosX + var17,
-               var23.maxY - this.renderPosY + var19,
-               var23.maxZ - this.renderPosZ + var21,
-               0.25F,
+               var2 - var10,
+               var4 + var1.getEyeHeight() - 0.01F,
+               var6 - var10,
+               var2 + var10,
+               var4 + var1.getEyeHeight() + 0.01F,
+               var6 + var10,
                1.0F,
                0.0F,
+               0.0F,
                1.0F
             );
          }
-      }

-      if (var1 instanceof EntityLivingBase) {
-         float var24 = 0.01F;
-         RenderGlobal.drawBoundingBox(
-            var2 - var10,
-            var4 + var1.getEyeHeight() - 0.01F,
-            var6 - var10,
-            var2 + var10,
-            var4 + var1.getEyeHeight() + 0.01F,
-            var6 + var10,
-            1.0F,
-            0.0F,
-            0.0F,
-            1.0F
-         );
+         Tessellator var25 = Tessellator.getInstance();
+         BufferBuilder var26 = var25.getBuffer();
+         Vec3d var27 = var1.getLook(var9);
+         var26.begin(3, DefaultVertexFormats.POSITION_COLOR);
+         var26.pos(var2, var4 + var1.getEyeHeight(), var6).color(0, 0, 255, 255).endVertex();
+         var26.pos(var2 + var27.x * 2.0, var4 + var1.getEyeHeight() + var27.y * 2.0, var6 + var27.z * 2.0).color(0, 0, 255, 255).endVertex();
+         var25.draw();
+         GlStateManager.enableTexture2D();
+         GlStateManager.enableLighting();
+         GlStateManager.enableCull();
+         GlStateManager.disableBlend();
+         GlStateManager.depthMask(true);
       }
-
-      Tessellator var25 = Tessellator.getInstance();
-      BufferBuilder var26 = var25.getBuffer();
-      Vec3d var27 = var1.getLook(var9);
-      var26.begin(3, DefaultVertexFormats.POSITION_COLOR);
-      var26.pos(var2, var4 + var1.getEyeHeight(), var6).color(0, 0, 255, 255).endVertex();
-      var26.pos(var2 + var27.x * 2.0, var4 + var1.getEyeHeight() + var27.y * 2.0, var6 + var27.z * 2.0).color(0, 0, 255, 255).endVertex();
-      var25.draw();
-      GlStateManager.enableTexture2D();
-      GlStateManager.enableLighting();
-      GlStateManager.enableCull();
-      GlStateManager.disableBlend();
-      GlStateManager.depthMask(true);
    }

    public void setWorld(@Nullable World var1) {
       this.world = var1;
       if (var1 == null) {
          this.renderViewEntity = null;
@@ -487,8 +509,16 @@
    public FontRenderer getFontRenderer() {
       return this.textRenderer;
    }

    public void setRenderOutlines(boolean var1) {
       this.renderOutlines = var1;
+   }
+
+   public Map<Class, Render> getEntityRenderMap() {
+      return this.entityRenderMap;
+   }
+
+   public Map<String, RenderPlayer> getSkinMap() {
+      return Collections.unmodifiableMap(this.skinMap);
    }
 }
 */
