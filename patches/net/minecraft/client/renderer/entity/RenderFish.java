package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderFish extends Render<EntityFishHook> {
   private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

   public RenderFish(RenderManager var1) {
      super(☃);
   }

   public void doRender(EntityFishHook var1, double var2, double var4, double var6, float var8, float var9) {
      EntityPlayer ☃ = ☃.getAngler();
      if (☃ != null && !this.renderOutlines) {
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)☃, (float)☃, (float)☃);
         GlStateManager.enableRescaleNormal();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         this.bindEntityTexture(☃);
         Tessellator ☃x = Tessellator.getInstance();
         BufferBuilder ☃xx = ☃x.getBuffer();
         int ☃xxx = 1;
         int ☃xxxx = 2;
         float ☃xxxxx = 0.0625F;
         float ☃xxxxxx = 0.125F;
         float ☃xxxxxxx = 0.125F;
         float ☃xxxxxxxx = 0.1875F;
         float ☃xxxxxxxxx = 1.0F;
         float ☃xxxxxxxxxx = 0.5F;
         float ☃xxxxxxxxxxx = 0.5F;
         GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
         if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(☃));
         }

         ☃xx.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
         ☃xx.pos(-0.5, -0.5, 0.0).tex(0.0625, 0.1875).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃xx.pos(0.5, -0.5, 0.0).tex(0.125, 0.1875).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃xx.pos(0.5, 0.5, 0.0).tex(0.125, 0.125).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃xx.pos(-0.5, 0.5, 0.0).tex(0.0625, 0.125).normal(0.0F, 1.0F, 0.0F).endVertex();
         ☃x.draw();
         if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
         }

         GlStateManager.disableRescaleNormal();
         GlStateManager.popMatrix();
         int ☃xxxxxxxxxxxx = ☃.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
         ItemStack ☃xxxxxxxxxxxxx = ☃.getHeldItemMainhand();
         if (☃xxxxxxxxxxxxx.getItem() != Items.FISHING_ROD) {
            ☃xxxxxxxxxxxx = -☃xxxxxxxxxxxx;
         }

         float ☃xxxxxxxxxxxxxx = ☃.getSwingProgress(☃);
         float ☃xxxxxxxxxxxxxxx = MathHelper.sin(MathHelper.sqrt(☃xxxxxxxxxxxxxx) * (float) Math.PI);
         float ☃xxxxxxxxxxxxxxxx = (☃.prevRenderYawOffset + (☃.renderYawOffset - ☃.prevRenderYawOffset) * ☃) * (float) (Math.PI / 180.0);
         double ☃xxxxxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxxxxxx);
         double ☃xxxxxxxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxxxxxx);
         double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * 0.35;
         double ☃xxxxxxxxxxxxxxxxxxxx = 0.8;
         double ☃xxxxxxxxxxxxxxxxxxxxx;
         double ☃xxxxxxxxxxxxxxxxxxxxxx;
         double ☃xxxxxxxxxxxxxxxxxxxxxxx;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxx;
         if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && ☃ == Minecraft.getMinecraft().player) {
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxx = this.renderManager.options.fovSetting;
            ☃xxxxxxxxxxxxxxxxxxxxxxxxx /= 100.0F;
            Vec3d ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = new Vec3d(☃xxxxxxxxxxxx * -0.36 * ☃xxxxxxxxxxxxxxxxxxxxxxxxx, -0.045 * ☃xxxxxxxxxxxxxxxxxxxxxxxxx, 0.4);
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.rotatePitch(
               -(☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃) * (float) (Math.PI / 180.0)
            );
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.rotateYaw(
               -(☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃) * (float) (Math.PI / 180.0)
            );
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.rotateYaw(☃xxxxxxxxxxxxxxx * 0.5F);
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.rotatePitch(-☃xxxxxxxxxxxxxxx * 0.7F);
            ☃xxxxxxxxxxxxxxxxxxxxx = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃ + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.x;
            ☃xxxxxxxxxxxxxxxxxxxxxx = ☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃ + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.y;
            ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃ + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx.z;
            ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃.getEyeHeight();
         } else {
            ☃xxxxxxxxxxxxxxxxxxxxx = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃ - ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxx * 0.8;
            ☃xxxxxxxxxxxxxxxxxxxxxx = ☃.prevPosY + ☃.getEyeHeight() + (☃.posY - ☃.prevPosY) * ☃ - 0.45;
            ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃ - ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx * 0.8;
            ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃.isSneaking() ? -0.1875 : 0.0;
         }

         double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃ + 0.25;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = (float)(☃xxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxx);
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (float)(☃xxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxx) + ☃xxxxxxxxxxxxxxxxxxxxxxxx;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (float)(☃xxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx);
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         ☃xx.begin(3, DefaultVertexFormats.POSITION_COLOR);
         int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 16;

         for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx <= 16; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
            float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx / 16.0F;
            ☃xx.pos(
                  ☃ + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                  ☃
                     + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        * (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                        * 0.5
                     + 0.25,
                  ☃ + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
               )
               .color(0, 0, 0, 255)
               .endVertex();
         }

         ☃x.draw();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
         super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   protected ResourceLocation getEntityTexture(EntityFishHook var1) {
      return FISH_PARTICLES;
   }
}
