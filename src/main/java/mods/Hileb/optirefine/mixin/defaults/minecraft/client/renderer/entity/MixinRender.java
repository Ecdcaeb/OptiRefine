package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public abstract class MixinRender {
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private final Class<? extends Entity> entityClass = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private final ResourceLocation locationTextureCustom = null;

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    public void before_renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci, @Share(namespace = "optirefine", value = "multitexture")LocalBooleanRef multitextureRef, @Local(ordinal = 0) BufferBuilder builder){
        boolean multitexture = Config.isMultiTexture();
        if (multitexture) {
            BufferBuilder_setBlockLayer(builder, BlockRenderLayer.SOLID);
        }
        multitextureRef.set(multitexture);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder setBlockLayer (Lnet.minecraft.util.BlockRenderLayer;)V")
    private static native void BufferBuilder_setBlockLayer(BufferBuilder builder, BlockRenderLayer layer);

    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    public void after_renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci, @Share(namespace = "optirefine", value = "multitexture")LocalBooleanRef multitextureRef, @Local(ordinal = 0) BufferBuilder builder){
        if (multitextureRef.get()) {
            BufferBuilder_setBlockLayer(builder, null);
            GlStateManager_bindCurrentTexture();
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void GlStateManager_bindCurrentTexture();

    @WrapMethod(method = "renderShadow")
    public void is_renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks, Operation<Void> original){
        if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
            original.call(entityIn, x, y, z, shadowAlpha, partialTicks);
        }
    }

}

/*
+++ net/minecraft/client/renderer/entity/Render.java	Tue Aug 19 14:59:58 2025
@@ -13,25 +13,30 @@
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLiving;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.scoreboard.ScorePlayerTeam;
+import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EnumBlockRenderType;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.World;
+import net.optifine.entity.model.IEntityRenderer;
+import net.optifine.shaders.Shaders;

-public abstract class Render<T extends Entity> {
+public abstract class Render<T extends Entity> implements IEntityRenderer {
    private static final ResourceLocation SHADOW_TEXTURES = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
-   protected float shadowSize;
+   public float shadowSize;
    protected float shadowOpaque = 1.0F;
    protected boolean renderOutlines;
+   private Class entityClass = null;
+   private ResourceLocation locationTextureCustom = null;

    protected Render(RenderManager var1) {
       this.renderManager = var1;
    }

    public void setRenderOutlines(boolean var1) {
@@ -64,13 +69,13 @@
       }

       return var2;
    }

    protected void renderName(T var1, double var2, double var4, double var6) {
-      if (this.canRenderName(var1)) {
+      if (this.canRenderName((T)var1)) {
          this.renderLivingLabel((T)var1, var1.getDisplayName().getFormattedText(), var2, var4, var6, 64);
       }
    }

    protected boolean canRenderName(T var1) {
       return var1.getAlwaysRenderNameTagForRender() && var1.hasCustomName();
@@ -82,12 +87,16 @@

    @Nullable
    protected abstract ResourceLocation getEntityTexture(T var1);

    protected boolean bindEntityTexture(T var1) {
       ResourceLocation var2 = this.getEntityTexture((T)var1);
+      if (this.locationTextureCustom != null) {
+         var2 = this.locationTextureCustom;
+      }
+
       if (var2 == null) {
          return false;
       } else {
          this.bindTexture(var2);
          return true;
       }
@@ -114,104 +123,117 @@
       float var18 = (float)(var1.posY - var1.getEntityBoundingBox().minY);
       GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
       GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)var17 * 0.02F);
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       float var19 = 0.0F;
       int var20 = 0;
+      boolean var21 = Config.isMultiTexture();
+      if (var21) {
+         var14.setBlockLayer(BlockRenderLayer.SOLID);
+      }
+
       var14.begin(7, DefaultVertexFormats.POSITION_TEX);

       while (var17 > 0.0F) {
-         TextureAtlasSprite var21 = var20 % 2 == 0 ? var10 : var11;
+         TextureAtlasSprite var22 = var20 % 2 == 0 ? var10 : var11;
+         var14.setSprite(var22);
          this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
-         float var22 = var21.getMinU();
-         float var23 = var21.getMinV();
-         float var24 = var21.getMaxU();
-         float var25 = var21.getMaxV();
+         float var23 = var22.getMinU();
+         float var24 = var22.getMinV();
+         float var25 = var22.getMaxU();
+         float var26 = var22.getMaxV();
          if (var20 / 2 % 2 == 0) {
-            float var26 = var24;
-            var24 = var22;
-            var22 = var26;
+            float var27 = var25;
+            var25 = var23;
+            var23 = var27;
          }

-         var14.pos(var15 - 0.0F, 0.0F - var18, var19).tex(var24, var25).endVertex();
-         var14.pos(-var15 - 0.0F, 0.0F - var18, var19).tex(var22, var25).endVertex();
-         var14.pos(-var15 - 0.0F, 1.4F - var18, var19).tex(var22, var23).endVertex();
-         var14.pos(var15 - 0.0F, 1.4F - var18, var19).tex(var24, var23).endVertex();
+         var14.pos(var15 - 0.0F, 0.0F - var18, var19).tex(var25, var26).endVertex();
+         var14.pos(-var15 - 0.0F, 0.0F - var18, var19).tex(var23, var26).endVertex();
+         var14.pos(-var15 - 0.0F, 1.4F - var18, var19).tex(var23, var24).endVertex();
+         var14.pos(var15 - 0.0F, 1.4F - var18, var19).tex(var25, var24).endVertex();
          var17 -= 0.45F;
          var18 -= 0.45F;
          var15 *= 0.9F;
          var19 += 0.03F;
          var20++;
       }

       var13.draw();
+      if (var21) {
+         var14.setBlockLayer(null);
+         GlStateManager.bindCurrentTexture();
+      }
+
       GlStateManager.popMatrix();
       GlStateManager.enableLighting();
    }

    private void renderShadow(Entity var1, double var2, double var4, double var6, float var8, float var9) {
-      GlStateManager.enableBlend();
-      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
-      this.renderManager.renderEngine.bindTexture(SHADOW_TEXTURES);
-      World var10 = this.getWorldFromRenderManager();
-      GlStateManager.depthMask(false);
-      float var11 = this.shadowSize;
-      if (var1 instanceof EntityLiving) {
-         EntityLiving var12 = (EntityLiving)var1;
-         var11 *= var12.getRenderSizeModifier();
-         if (var12.isChild()) {
-            var11 *= 0.5F;
+      if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
+         GlStateManager.enableBlend();
+         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
+         this.renderManager.renderEngine.bindTexture(SHADOW_TEXTURES);
+         World var10 = this.getWorldFromRenderManager();
+         GlStateManager.depthMask(false);
+         float var11 = this.shadowSize;
+         if (var1 instanceof EntityLiving) {
+            EntityLiving var12 = (EntityLiving)var1;
+            var11 *= var12.getRenderSizeModifier();
+            if (var12.isChild()) {
+               var11 *= 0.5F;
+            }
          }
-      }

-      double var35 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var9;
-      double var14 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var9;
-      double var16 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var9;
-      int var18 = MathHelper.floor(var35 - var11);
-      int var19 = MathHelper.floor(var35 + var11);
-      int var20 = MathHelper.floor(var14 - var11);
-      int var21 = MathHelper.floor(var14);
-      int var22 = MathHelper.floor(var16 - var11);
-      int var23 = MathHelper.floor(var16 + var11);
-      double var24 = var2 - var35;
-      double var26 = var4 - var14;
-      double var28 = var6 - var16;
-      Tessellator var30 = Tessellator.getInstance();
-      BufferBuilder var31 = var30.getBuffer();
-      var31.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-
-      for (BlockPos var33 : BlockPos.getAllInBoxMutable(new BlockPos(var18, var20, var22), new BlockPos(var19, var21, var23))) {
-         IBlockState var34 = var10.getBlockState(var33.down());
-         if (var34.getRenderType() != EnumBlockRenderType.INVISIBLE && var10.getLightFromNeighbors(var33) > 3) {
-            this.renderShadowSingle(var34, var2, var4, var6, var33, var8, var11, var24, var26, var28);
+         double var35 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var9;
+         double var14 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var9;
+         double var16 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var9;
+         int var18 = MathHelper.floor(var35 - var11);
+         int var19 = MathHelper.floor(var35 + var11);
+         int var20 = MathHelper.floor(var14 - var11);
+         int var21 = MathHelper.floor(var14);
+         int var22 = MathHelper.floor(var16 - var11);
+         int var23 = MathHelper.floor(var16 + var11);
+         double var24 = var2 - var35;
+         double var26 = var4 - var14;
+         double var28 = var6 - var16;
+         Tessellator var30 = Tessellator.getInstance();
+         BufferBuilder var31 = var30.getBuffer();
+         var31.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
+
+         for (BlockPos var33 : BlockPos.getAllInBoxMutable(new BlockPos(var18, var20, var22), new BlockPos(var19, var21, var23))) {
+            IBlockState var34 = var10.getBlockState(var33.down());
+            if (var34.i() != EnumBlockRenderType.INVISIBLE && var10.getLightFromNeighbors(var33) > 3) {
+               this.renderShadowSingle(var34, var2, var4, var6, var33, var8, var11, var24, var26, var28);
+            }
          }
-      }

-      var30.draw();
-      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-      GlStateManager.disableBlend();
-      GlStateManager.depthMask(true);
+         var30.draw();
+         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+         GlStateManager.disableBlend();
+         GlStateManager.depthMask(true);
+      }
    }

    private World getWorldFromRenderManager() {
       return this.renderManager.world;
    }

    private void renderShadowSingle(
       IBlockState var1, double var2, double var4, double var6, BlockPos var8, float var9, float var10, double var11, double var13, double var15
    ) {
-      if (var1.isFullCube()) {
+      if (var1.g()) {
          Tessellator var17 = Tessellator.getInstance();
          BufferBuilder var18 = var17.getBuffer();
          double var19 = (var9 - (var4 - (var8.getY() + var13)) / 2.0) * 0.5 * this.getWorldFromRenderManager().getLightBrightness(var8);
-         if (!(var19 < 0.0)) {
+         if (var19 >= 0.0) {
             if (var19 > 1.0) {
                var19 = 1.0;
             }

-            AxisAlignedBB var21 = var1.getBoundingBox(this.getWorldFromRenderManager(), var8);
+            AxisAlignedBB var21 = var1.e(this.getWorldFromRenderManager(), var8);
             double var22 = var8.getX() + var21.minX + var11;
             double var24 = var8.getX() + var21.maxX + var11;
             double var26 = var8.getY() + var21.minY + var13 + 0.015625;
             double var28 = var8.getZ() + var21.minZ + var15;
             double var30 = var8.getZ() + var21.maxZ + var15;
             float var32 = (float)((var2 - var22) / 2.0 / var10 + 0.5);
@@ -281,13 +303,13 @@
    public FontRenderer getFontRendererFromRenderManager() {
       return this.renderManager.getFontRenderer();
    }

    protected void renderLivingLabel(T var1, String var2, double var3, double var5, double var7, int var9) {
       double var10 = var1.getDistanceSq(this.renderManager.renderViewEntity);
-      if (!(var10 > var9 * var9)) {
+      if (var10 <= var9 * var9) {
          boolean var12 = var1.isSneaking();
          float var13 = this.renderManager.playerViewY;
          float var14 = this.renderManager.playerViewX;
          boolean var15 = this.renderManager.options.thirdPersonView == 2;
          float var16 = var1.height + 0.5F - (var12 ? 0.25F : 0.0F);
          int var17 = "deadmau5".equals(var2) ? -10 : 0;
@@ -303,8 +325,24 @@

    public boolean isMultipass() {
       return false;
    }

    public void renderMultipass(T var1, double var2, double var4, double var6, float var8, float var9) {
+   }
+
+   public Class getEntityClass() {
+      return this.entityClass;
+   }
+
+   public void setEntityClass(Class var1) {
+      this.entityClass = var1;
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
