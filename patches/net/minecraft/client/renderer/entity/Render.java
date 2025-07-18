package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.shaders.Shaders;

public abstract class Render<T extends Entity> implements IEntityRenderer {
   private static final ResourceLocation SHADOW_TEXTURES = new ResourceLocation("textures/misc/shadow.png");
   protected final RenderManager renderManager;
   public float shadowSize;
   protected float shadowOpaque = 1.0F;
   protected boolean renderOutlines;
   private Class entityClass = null;
   private ResourceLocation locationTextureCustom = null;

   protected Render(RenderManager renderManager) {
      this.renderManager = renderManager;
   }

   public void setRenderOutlines(boolean renderOutlinesIn) {
      this.renderOutlines = renderOutlinesIn;
   }

   public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
      AxisAlignedBB axisalignedbb = livingEntity.getRenderBoundingBox().grow(0.5);
      if (axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0) {
         axisalignedbb = new AxisAlignedBB(
            livingEntity.posX - 2.0,
            livingEntity.posY - 2.0,
            livingEntity.posZ - 2.0,
            livingEntity.posX + 2.0,
            livingEntity.posY + 2.0,
            livingEntity.posZ + 2.0
         );
      }

      return livingEntity.isInRangeToRender3d(camX, camY, camZ) && (livingEntity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
   }

   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (!this.renderOutlines) {
         this.renderName(entity, x, y, z);
      }
   }

   protected int getTeamColor(T entityIn) {
      int i = 16777215;
      ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityIn.getTeam();
      if (scoreplayerteam != null) {
         String s = FontRenderer.getFormatFromString(scoreplayerteam.getPrefix());
         if (s.length() >= 2) {
            i = this.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
         }
      }

      return i;
   }

   protected void renderName(T entity, double x, double y, double z) {
      if (this.canRenderName(entity)) {
         this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
      }
   }

   protected boolean canRenderName(T entity) {
      return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
   }

   protected void renderEntityName(T entityIn, double x, double y, double z, String name, double distanceSq) {
      this.renderLivingLabel(entityIn, name, x, y, z, 64);
   }

   @Nullable
   protected abstract ResourceLocation getEntityTexture(T var1);

   protected boolean bindEntityTexture(T entity) {
      ResourceLocation resourcelocation = this.getEntityTexture(entity);
      if (this.locationTextureCustom != null) {
         resourcelocation = this.locationTextureCustom;
      }

      if (resourcelocation == null) {
         return false;
      } else {
         this.bindTexture(resourcelocation);
         return true;
      }
   }

   public void bindTexture(ResourceLocation location) {
      this.renderManager.renderEngine.bindTexture(location);
   }

   private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks) {
      GlStateManager.disableLighting();
      TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
      TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)x, (float)y, (float)z);
      float f = entity.width * 1.4F;
      GlStateManager.scale(f, f, f);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      float f1 = 0.5F;
      float f2 = 0.0F;
      float f3 = entity.height / f;
      float f4 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
      GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)f3 * 0.02F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float f5 = 0.0F;
      int i = 0;
      boolean multitexture = Config.isMultiTexture();
      if (multitexture) {
         bufferbuilder.setBlockLayer(BlockRenderLayer.SOLID);
      }

      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

      while (f3 > 0.0F) {
         TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
         bufferbuilder.setSprite(textureatlassprite2);
         this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
         float f6 = textureatlassprite2.getMinU();
         float f7 = textureatlassprite2.getMinV();
         float f8 = textureatlassprite2.getMaxU();
         float f9 = textureatlassprite2.getMaxV();
         if (i / 2 % 2 == 0) {
            float f10 = f8;
            f8 = f6;
            f6 = f10;
         }

         bufferbuilder.pos(f1 - 0.0F, 0.0F - f4, f5).tex(f8, f9).endVertex();
         bufferbuilder.pos(-f1 - 0.0F, 0.0F - f4, f5).tex(f6, f9).endVertex();
         bufferbuilder.pos(-f1 - 0.0F, 1.4F - f4, f5).tex(f6, f7).endVertex();
         bufferbuilder.pos(f1 - 0.0F, 1.4F - f4, f5).tex(f8, f7).endVertex();
         f3 -= 0.45F;
         f4 -= 0.45F;
         f1 *= 0.9F;
         f5 += 0.03F;
         i++;
      }

      tessellator.draw();
      if (multitexture) {
         bufferbuilder.setBlockLayer(null);
         GlStateManager.bindCurrentTexture();
      }

      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
   }

   private void renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks) {
      if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         this.renderManager.renderEngine.bindTexture(SHADOW_TEXTURES);
         World world = this.getWorldFromRenderManager();
         GlStateManager.depthMask(false);
         float f = this.shadowSize;
         if (entityIn instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)entityIn;
            f *= entityliving.getRenderSizeModifier();
            if (entityliving.isChild()) {
               f *= 0.5F;
            }
         }

         double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
         double d0 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
         double d1 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
         int i = MathHelper.floor(d5 - f);
         int j = MathHelper.floor(d5 + f);
         int k = MathHelper.floor(d0 - f);
         int l = MathHelper.floor(d0);
         int i1 = MathHelper.floor(d1 - f);
         int j1 = MathHelper.floor(d1 + f);
         double d2 = x - d5;
         double d3 = y - d0;
         double d4 = z - d1;
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

         for (BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(i, k, i1), new BlockPos(j, l, j1))) {
            IBlockState iblockstate = world.getBlockState(blockpos.down());
            if (iblockstate.i() != EnumBlockRenderType.INVISIBLE && world.getLightFromNeighbors(blockpos) > 3) {
               this.renderShadowSingle(iblockstate, x, y, z, blockpos, shadowAlpha, f, d2, d3, d4);
            }
         }

         tessellator.draw();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
      }
   }

   private World getWorldFromRenderManager() {
      return this.renderManager.world;
   }

   private void renderShadowSingle(
      IBlockState state,
      double p_188299_2_,
      double p_188299_4_,
      double p_188299_6_,
      BlockPos p_188299_8_,
      float p_188299_9_,
      float p_188299_10_,
      double p_188299_11_,
      double p_188299_13_,
      double p_188299_15_
   ) {
      if (state.g()) {
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         double d0 = (p_188299_9_ - (p_188299_4_ - (p_188299_8_.getY() + p_188299_13_)) / 2.0)
            * 0.5
            * this.getWorldFromRenderManager().getLightBrightness(p_188299_8_);
         if (d0 >= 0.0) {
            if (d0 > 1.0) {
               d0 = 1.0;
            }

            AxisAlignedBB axisalignedbb = state.e(this.getWorldFromRenderManager(), p_188299_8_);
            double d1 = p_188299_8_.getX() + axisalignedbb.minX + p_188299_11_;
            double d2 = p_188299_8_.getX() + axisalignedbb.maxX + p_188299_11_;
            double d3 = p_188299_8_.getY() + axisalignedbb.minY + p_188299_13_ + 0.015625;
            double d4 = p_188299_8_.getZ() + axisalignedbb.minZ + p_188299_15_;
            double d5 = p_188299_8_.getZ() + axisalignedbb.maxZ + p_188299_15_;
            float f = (float)((p_188299_2_ - d1) / 2.0 / p_188299_10_ + 0.5);
            float f1 = (float)((p_188299_2_ - d2) / 2.0 / p_188299_10_ + 0.5);
            float f2 = (float)((p_188299_6_ - d4) / 2.0 / p_188299_10_ + 0.5);
            float f3 = (float)((p_188299_6_ - d5) / 2.0 / p_188299_10_ + 0.5);
            bufferbuilder.pos(d1, d3, d4).tex(f, f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
            bufferbuilder.pos(d1, d3, d5).tex(f, f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
            bufferbuilder.pos(d2, d3, d5).tex(f1, f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
            bufferbuilder.pos(d2, d3, d4).tex(f1, f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
         }
      }
   }

   public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z) {
      GlStateManager.disableTexture2D();
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      bufferbuilder.setTranslation(x, y, z);
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_NORMAL);
      bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      tessellator.draw();
      bufferbuilder.setTranslation(0.0, 0.0, 0.0);
      GlStateManager.enableTexture2D();
   }

   public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
      if (this.renderManager.options != null) {
         if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {
            double d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
            float f = (float)((1.0 - d0 / 256.0) * this.shadowOpaque);
            if (f > 0.0F) {
               this.renderShadow(entityIn, x, y, z, f, partialTicks);
            }
         }

         if (entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).isSpectator())) {
            this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
         }
      }
   }

   public FontRenderer getFontRendererFromRenderManager() {
      return this.renderManager.getFontRenderer();
   }

   protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
      double d0 = entityIn.getDistanceSq(this.renderManager.renderViewEntity);
      if (d0 <= maxDistance * maxDistance) {
         boolean flag = entityIn.isSneaking();
         float f = this.renderManager.playerViewY;
         float f1 = this.renderManager.playerViewX;
         boolean flag1 = this.renderManager.options.thirdPersonView == 2;
         float f2 = entityIn.height + 0.5F - (flag ? 0.25F : 0.0F);
         int i = "deadmau5".equals(str) ? -10 : 0;
         EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float)x, (float)y + f2, (float)z, i, f, f1, flag1, flag);
      }
   }

   public RenderManager getRenderManager() {
      return this.renderManager;
   }

   public boolean isMultipass() {
      return false;
   }

   public void renderMultipass(T p_188300_1_, double p_188300_2_, double p_188300_4_, double p_188300_6_, float p_188300_8_, float p_188300_9_) {
   }

   public Class getEntityClass() {
      return this.entityClass;
   }

   public void setEntityClass(Class entityClass) {
      this.entityClass = entityClass;
   }

   public ResourceLocation getLocationTextureCustom() {
      return this.locationTextureCustom;
   }

   public void setLocationTextureCustom(ResourceLocation locationTextureCustom) {
      this.locationTextureCustom = locationTextureCustom;
   }
}
