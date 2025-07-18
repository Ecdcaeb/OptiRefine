package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderMinecart<T extends EntityMinecart> extends Render<T> {
   private static final ResourceLocation MINECART_TEXTURES = new ResourceLocation("textures/entity/minecart.png");
   protected ModelBase modelMinecart = new ModelMinecart();

   public RenderMinecart(RenderManager var1) {
      super(☃);
      this.shadowSize = 0.5F;
   }

   public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      this.bindEntityTexture(☃);
      long ☃ = ☃.getEntityId() * 493286711L;
      ☃ = ☃ * ☃ * 4392167121L + ☃ * 98761L;
      float ☃x = (((float)(☃ >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      float ☃xx = (((float)(☃ >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      float ☃xxx = (((float)(☃ >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      GlStateManager.translate(☃x, ☃xx, ☃xxx);
      double ☃xxxx = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃xxxxx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xxxxxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      double ☃xxxxxxx = 0.3F;
      Vec3d ☃xxxxxxxx = ☃.getPos(☃xxxx, ☃xxxxx, ☃xxxxxx);
      float ☃xxxxxxxxx = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      if (☃xxxxxxxx != null) {
         Vec3d ☃xxxxxxxxxx = ☃.getPosOffset(☃xxxx, ☃xxxxx, ☃xxxxxx, 0.3F);
         Vec3d ☃xxxxxxxxxxx = ☃.getPosOffset(☃xxxx, ☃xxxxx, ☃xxxxxx, -0.3F);
         if (☃xxxxxxxxxx == null) {
            ☃xxxxxxxxxx = ☃xxxxxxxx;
         }

         if (☃xxxxxxxxxxx == null) {
            ☃xxxxxxxxxxx = ☃xxxxxxxx;
         }

         ☃ += ☃xxxxxxxx.x - ☃xxxx;
         ☃ += (☃xxxxxxxxxx.y + ☃xxxxxxxxxxx.y) / 2.0 - ☃xxxxx;
         ☃ += ☃xxxxxxxx.z - ☃xxxxxx;
         Vec3d ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx.add(-☃xxxxxxxxxx.x, -☃xxxxxxxxxx.y, -☃xxxxxxxxxx.z);
         if (☃xxxxxxxxxxxx.length() != 0.0) {
            ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx.normalize();
            ☃ = (float)(Math.atan2(☃xxxxxxxxxxxx.z, ☃xxxxxxxxxxxx.x) * 180.0 / Math.PI);
            ☃xxxxxxxxx = (float)(Math.atan(☃xxxxxxxxxxxx.y) * 73.0);
         }
      }

      GlStateManager.translate((float)☃, (float)☃ + 0.375F, (float)☃);
      GlStateManager.rotate(180.0F - ☃, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-☃xxxxxxxxx, 0.0F, 0.0F, 1.0F);
      float ☃xxxxxxxxxxxx = ☃.getRollingAmplitude() - ☃;
      float ☃xxxxxxxxxxxxx = ☃.getDamage() - ☃;
      if (☃xxxxxxxxxxxxx < 0.0F) {
         ☃xxxxxxxxxxxxx = 0.0F;
      }

      if (☃xxxxxxxxxxxx > 0.0F) {
         GlStateManager.rotate(MathHelper.sin(☃xxxxxxxxxxxx) * ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxxx / 10.0F * ☃.getRollingDirection(), 1.0F, 0.0F, 0.0F);
      }

      int ☃xxxxxxxxxxxxxx = ☃.getDisplayTileOffset();
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      IBlockState ☃xxxxxxxxxxxxxxx = ☃.getDisplayTile();
      if (☃xxxxxxxxxxxxxxx.getRenderType() != EnumBlockRenderType.INVISIBLE) {
         GlStateManager.pushMatrix();
         this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
         float ☃xxxxxxxxxxxxxxxx = 0.75F;
         GlStateManager.scale(0.75F, 0.75F, 0.75F);
         GlStateManager.translate(-0.5F, (☃xxxxxxxxxxxxxx - 8) / 16.0F, 0.5F);
         this.renderCartContents(☃, ☃, ☃xxxxxxxxxxxxxxx);
         GlStateManager.popMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.bindEntityTexture(☃);
      }

      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
      this.modelMinecart.render(☃, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(T var1) {
      return MINECART_TEXTURES;
   }

   protected void renderCartContents(T var1, float var2, IBlockState var3) {
      GlStateManager.pushMatrix();
      Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(☃, ☃.getBrightness());
      GlStateManager.popMatrix();
   }
}
