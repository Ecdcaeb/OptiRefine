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
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class Render<T extends Entity> {
   private static final ResourceLocation SHADOW_TEXTURES = new ResourceLocation("textures/misc/shadow.png");
   protected final RenderManager renderManager;
   protected float shadowSize;
   protected float shadowOpaque = 1.0F;
   protected boolean renderOutlines;

   protected Render(RenderManager var1) {
      this.renderManager = ☃;
   }

   public void setRenderOutlines(boolean var1) {
      this.renderOutlines = ☃;
   }

   public boolean shouldRender(T var1, ICamera var2, double var3, double var5, double var7) {
      AxisAlignedBB ☃ = ☃.getRenderBoundingBox().grow(0.5);
      if (☃.hasNaN() || ☃.getAverageEdgeLength() == 0.0) {
         ☃ = new AxisAlignedBB(☃.posX - 2.0, ☃.posY - 2.0, ☃.posZ - 2.0, ☃.posX + 2.0, ☃.posY + 2.0, ☃.posZ + 2.0);
      }

      return ☃.isInRangeToRender3d(☃, ☃, ☃) && (☃.ignoreFrustumCheck || ☃.isBoundingBoxInFrustum(☃));
   }

   public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
      if (!this.renderOutlines) {
         this.renderName(☃, ☃, ☃, ☃);
      }
   }

   protected int getTeamColor(T var1) {
      int ☃ = 16777215;
      ScorePlayerTeam ☃x = (ScorePlayerTeam)☃.getTeam();
      if (☃x != null) {
         String ☃xx = FontRenderer.getFormatFromString(☃x.getPrefix());
         if (☃xx.length() >= 2) {
            ☃ = this.getFontRendererFromRenderManager().getColorCode(☃xx.charAt(1));
         }
      }

      return ☃;
   }

   protected void renderName(T var1, double var2, double var4, double var6) {
      if (this.canRenderName(☃)) {
         this.renderLivingLabel(☃, ☃.getDisplayName().getFormattedText(), ☃, ☃, ☃, 64);
      }
   }

   protected boolean canRenderName(T var1) {
      return ☃.getAlwaysRenderNameTagForRender() && ☃.hasCustomName();
   }

   protected void renderEntityName(T var1, double var2, double var4, double var6, String var8, double var9) {
      this.renderLivingLabel(☃, ☃, ☃, ☃, ☃, 64);
   }

   @Nullable
   protected abstract ResourceLocation getEntityTexture(T var1);

   protected boolean bindEntityTexture(T var1) {
      ResourceLocation ☃ = this.getEntityTexture(☃);
      if (☃ == null) {
         return false;
      } else {
         this.bindTexture(☃);
         return true;
      }
   }

   public void bindTexture(ResourceLocation var1) {
      this.renderManager.renderEngine.bindTexture(☃);
   }

   private void renderEntityOnFire(Entity var1, double var2, double var4, double var6, float var8) {
      GlStateManager.disableLighting();
      TextureMap ☃ = Minecraft.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite ☃x = ☃.getAtlasSprite("minecraft:blocks/fire_layer_0");
      TextureAtlasSprite ☃xx = ☃.getAtlasSprite("minecraft:blocks/fire_layer_1");
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃, (float)☃, (float)☃);
      float ☃xxx = ☃.width * 1.4F;
      GlStateManager.scale(☃xxx, ☃xxx, ☃xxx);
      Tessellator ☃xxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
      float ☃xxxxxx = 0.5F;
      float ☃xxxxxxx = 0.0F;
      float ☃xxxxxxxx = ☃.height / ☃xxx;
      float ☃xxxxxxxxx = (float)(☃.posY - ☃.getEntityBoundingBox().minY);
      GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)☃xxxxxxxx * 0.02F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float ☃xxxxxxxxxx = 0.0F;
      int ☃xxxxxxxxxxx = 0;
      ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);

      while (☃xxxxxxxx > 0.0F) {
         TextureAtlasSprite ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx % 2 == 0 ? ☃x : ☃xx;
         this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
         float ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getMinU();
         float ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getMinV();
         float ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getMaxU();
         float ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getMaxV();
         if (☃xxxxxxxxxxx / 2 % 2 == 0) {
            float ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx;
            ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx;
            ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx;
         }

         ☃xxxxx.pos(☃xxxxxx - 0.0F, 0.0F - ☃xxxxxxxxx, ☃xxxxxxxxxx).tex(☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx).endVertex();
         ☃xxxxx.pos(-☃xxxxxx - 0.0F, 0.0F - ☃xxxxxxxxx, ☃xxxxxxxxxx).tex(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx).endVertex();
         ☃xxxxx.pos(-☃xxxxxx - 0.0F, 1.4F - ☃xxxxxxxxx, ☃xxxxxxxxxx).tex(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).endVertex();
         ☃xxxxx.pos(☃xxxxxx - 0.0F, 1.4F - ☃xxxxxxxxx, ☃xxxxxxxxxx).tex(☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).endVertex();
         ☃xxxxxxxx -= 0.45F;
         ☃xxxxxxxxx -= 0.45F;
         ☃xxxxxx *= 0.9F;
         ☃xxxxxxxxxx += 0.03F;
         ☃xxxxxxxxxxx++;
      }

      ☃xxxx.draw();
      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
   }

   private void renderShadow(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      this.renderManager.renderEngine.bindTexture(SHADOW_TEXTURES);
      World ☃ = this.getWorldFromRenderManager();
      GlStateManager.depthMask(false);
      float ☃x = this.shadowSize;
      if (☃ instanceof EntityLiving) {
         EntityLiving ☃xx = (EntityLiving)☃;
         ☃x *= ☃xx.getRenderSizeModifier();
         if (☃xx.isChild()) {
            ☃x *= 0.5F;
         }
      }

      double ☃xx = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃xxx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xxxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      int ☃xxxxx = MathHelper.floor(☃xx - ☃x);
      int ☃xxxxxx = MathHelper.floor(☃xx + ☃x);
      int ☃xxxxxxx = MathHelper.floor(☃xxx - ☃x);
      int ☃xxxxxxxx = MathHelper.floor(☃xxx);
      int ☃xxxxxxxxx = MathHelper.floor(☃xxxx - ☃x);
      int ☃xxxxxxxxxx = MathHelper.floor(☃xxxx + ☃x);
      double ☃xxxxxxxxxxx = ☃ - ☃xx;
      double ☃xxxxxxxxxxxx = ☃ - ☃xxx;
      double ☃xxxxxxxxxxxxx = ☃ - ☃xxxx;
      Tessellator ☃xxxxxxxxxxxxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.getBuffer();
      ☃xxxxxxxxxxxxxxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

      for (BlockPos ☃xxxxxxxxxxxxxxxx : BlockPos.getAllInBoxMutable(new BlockPos(☃xxxxx, ☃xxxxxxx, ☃xxxxxxxxx), new BlockPos(☃xxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxx))) {
         IBlockState ☃xxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxx.down());
         if (☃xxxxxxxxxxxxxxxxx.getRenderType() != EnumBlockRenderType.INVISIBLE && ☃.getLightFromNeighbors(☃xxxxxxxxxxxxxxxx) > 3) {
            this.renderShadowSingle(☃xxxxxxxxxxxxxxxxx, ☃, ☃, ☃, ☃xxxxxxxxxxxxxxxx, ☃, ☃x, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx);
         }
      }

      ☃xxxxxxxxxxxxxx.draw();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
   }

   private World getWorldFromRenderManager() {
      return this.renderManager.world;
   }

   private void renderShadowSingle(
      IBlockState var1, double var2, double var4, double var6, BlockPos var8, float var9, float var10, double var11, double var13, double var15
   ) {
      if (☃.isFullCube()) {
         Tessellator ☃ = Tessellator.getInstance();
         BufferBuilder ☃x = ☃.getBuffer();
         double ☃xx = (☃ - (☃ - (☃.getY() + ☃)) / 2.0) * 0.5 * this.getWorldFromRenderManager().getLightBrightness(☃);
         if (!(☃xx < 0.0)) {
            if (☃xx > 1.0) {
               ☃xx = 1.0;
            }

            AxisAlignedBB ☃xxx = ☃.getBoundingBox(this.getWorldFromRenderManager(), ☃);
            double ☃xxxx = ☃.getX() + ☃xxx.minX + ☃;
            double ☃xxxxx = ☃.getX() + ☃xxx.maxX + ☃;
            double ☃xxxxxx = ☃.getY() + ☃xxx.minY + ☃ + 0.015625;
            double ☃xxxxxxx = ☃.getZ() + ☃xxx.minZ + ☃;
            double ☃xxxxxxxx = ☃.getZ() + ☃xxx.maxZ + ☃;
            float ☃xxxxxxxxx = (float)((☃ - ☃xxxx) / 2.0 / ☃ + 0.5);
            float ☃xxxxxxxxxx = (float)((☃ - ☃xxxxx) / 2.0 / ☃ + 0.5);
            float ☃xxxxxxxxxxx = (float)((☃ - ☃xxxxxxx) / 2.0 / ☃ + 0.5);
            float ☃xxxxxxxxxxxx = (float)((☃ - ☃xxxxxxxx) / 2.0 / ☃ + 0.5);
            ☃x.pos(☃xxxx, ☃xxxxxx, ☃xxxxxxx).tex(☃xxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 1.0F, 1.0F, (float)☃xx).endVertex();
            ☃x.pos(☃xxxx, ☃xxxxxx, ☃xxxxxxxx).tex(☃xxxxxxxxx, ☃xxxxxxxxxxxx).color(1.0F, 1.0F, 1.0F, (float)☃xx).endVertex();
            ☃x.pos(☃xxxxx, ☃xxxxxx, ☃xxxxxxxx).tex(☃xxxxxxxxxx, ☃xxxxxxxxxxxx).color(1.0F, 1.0F, 1.0F, (float)☃xx).endVertex();
            ☃x.pos(☃xxxxx, ☃xxxxxx, ☃xxxxxxx).tex(☃xxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 1.0F, 1.0F, (float)☃xx).endVertex();
         }
      }
   }

   public static void renderOffsetAABB(AxisAlignedBB var0, double var1, double var3, double var5) {
      GlStateManager.disableTexture2D();
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      ☃x.setTranslation(☃, ☃, ☃);
      ☃x.begin(7, DefaultVertexFormats.POSITION_NORMAL);
      ☃x.pos(☃.minX, ☃.maxY, ☃.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.maxY, ☃.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.minY, ☃.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      ☃x.pos(☃.minX, ☃.minY, ☃.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
      ☃x.pos(☃.minX, ☃.minY, ☃.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.minY, ☃.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.maxY, ☃.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      ☃x.pos(☃.minX, ☃.maxY, ☃.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
      ☃x.pos(☃.minX, ☃.minY, ☃.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.minY, ☃.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.minY, ☃.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      ☃x.pos(☃.minX, ☃.minY, ☃.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
      ☃x.pos(☃.minX, ☃.maxY, ☃.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.maxY, ☃.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.maxY, ☃.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.pos(☃.minX, ☃.maxY, ☃.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.pos(☃.minX, ☃.minY, ☃.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      ☃x.pos(☃.minX, ☃.maxY, ☃.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      ☃x.pos(☃.minX, ☃.maxY, ☃.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      ☃x.pos(☃.minX, ☃.minY, ☃.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.minY, ☃.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.maxY, ☃.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.maxY, ☃.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      ☃x.pos(☃.maxX, ☃.minY, ☃.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
      ☃.draw();
      ☃x.setTranslation(0.0, 0.0, 0.0);
      GlStateManager.enableTexture2D();
   }

   public void doRenderShadowAndFire(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      if (this.renderManager.options != null) {
         if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !☃.isInvisible() && this.renderManager.isRenderShadow()) {
            double ☃ = this.renderManager.getDistanceToCamera(☃.posX, ☃.posY, ☃.posZ);
            float ☃x = (float)((1.0 - ☃ / 256.0) * this.shadowOpaque);
            if (☃x > 0.0F) {
               this.renderShadow(☃, ☃, ☃, ☃, ☃x, ☃);
            }
         }

         if (☃.canRenderOnFire() && (!(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).isSpectator())) {
            this.renderEntityOnFire(☃, ☃, ☃, ☃, ☃);
         }
      }
   }

   public FontRenderer getFontRendererFromRenderManager() {
      return this.renderManager.getFontRenderer();
   }

   protected void renderLivingLabel(T var1, String var2, double var3, double var5, double var7, int var9) {
      double ☃ = ☃.getDistanceSq(this.renderManager.renderViewEntity);
      if (!(☃ > ☃ * ☃)) {
         boolean ☃x = ☃.isSneaking();
         float ☃xx = this.renderManager.playerViewY;
         float ☃xxx = this.renderManager.playerViewX;
         boolean ☃xxxx = this.renderManager.options.thirdPersonView == 2;
         float ☃xxxxx = ☃.height + 0.5F - (☃x ? 0.25F : 0.0F);
         int ☃xxxxxx = "deadmau5".equals(☃) ? -10 : 0;
         EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), ☃, (float)☃, (float)☃ + ☃xxxxx, (float)☃, ☃xxxxxx, ☃xx, ☃xxx, ☃xxxx, ☃x);
      }
   }

   public RenderManager getRenderManager() {
      return this.renderManager;
   }

   public boolean isMultipass() {
      return false;
   }

   public void renderMultipass(T var1, double var2, double var4, double var6, float var8, float var9) {
   }
}
