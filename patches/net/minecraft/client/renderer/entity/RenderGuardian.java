package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class RenderGuardian extends RenderLiving<EntityGuardian> {
   private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
   private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");

   public RenderGuardian(RenderManager var1) {
      super(☃, new ModelGuardian(), 0.5F);
   }

   public boolean shouldRender(EntityGuardian var1, ICamera var2, double var3, double var5, double var7) {
      if (super.shouldRender(☃, ☃, ☃, ☃, ☃)) {
         return true;
      } else {
         if (☃.hasTargetedEntity()) {
            EntityLivingBase ☃ = ☃.getTargetedEntity();
            if (☃ != null) {
               Vec3d ☃x = this.getPosition(☃, ☃.height * 0.5, 1.0F);
               Vec3d ☃xx = this.getPosition(☃, ☃.getEyeHeight(), 1.0F);
               if (☃.isBoundingBoxInFrustum(new AxisAlignedBB(☃xx.x, ☃xx.y, ☃xx.z, ☃x.x, ☃x.y, ☃x.z))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private Vec3d getPosition(EntityLivingBase var1, double var2, float var4) {
      double ☃ = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃x = ☃ + ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      return new Vec3d(☃, ☃x, ☃xx);
   }

   public void doRender(EntityGuardian var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      EntityLivingBase ☃ = ☃.getTargetedEntity();
      if (☃ != null) {
         float ☃x = ☃.getAttackAnimationScale(☃);
         Tessellator ☃xx = Tessellator.getInstance();
         BufferBuilder ☃xxx = ☃xx.getBuffer();
         this.bindTexture(GUARDIAN_BEAM_TEXTURE);
         GlStateManager.glTexParameteri(3553, 10242, 10497);
         GlStateManager.glTexParameteri(3553, 10243, 10497);
         GlStateManager.disableLighting();
         GlStateManager.disableCull();
         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
         float ☃xxxx = 240.0F;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
         );
         float ☃xxxxx = (float)☃.world.getTotalWorldTime() + ☃;
         float ☃xxxxxx = ☃xxxxx * 0.5F % 1.0F;
         float ☃xxxxxxx = ☃.getEyeHeight();
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)☃, (float)☃ + ☃xxxxxxx, (float)☃);
         Vec3d ☃xxxxxxxx = this.getPosition(☃, ☃.height * 0.5, ☃);
         Vec3d ☃xxxxxxxxx = this.getPosition(☃, ☃xxxxxxx, ☃);
         Vec3d ☃xxxxxxxxxx = ☃xxxxxxxx.subtract(☃xxxxxxxxx);
         double ☃xxxxxxxxxxx = ☃xxxxxxxxxx.length() + 1.0;
         ☃xxxxxxxxxx = ☃xxxxxxxxxx.normalize();
         float ☃xxxxxxxxxxxx = (float)Math.acos(☃xxxxxxxxxx.y);
         float ☃xxxxxxxxxxxxx = (float)Math.atan2(☃xxxxxxxxxx.z, ☃xxxxxxxxxx.x);
         GlStateManager.rotate(((float) (Math.PI / 2) + -☃xxxxxxxxxxxxx) * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(☃xxxxxxxxxxxx * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
         int ☃xxxxxxxxxxxxxx = 1;
         double ☃xxxxxxxxxxxxxxx = ☃xxxxx * 0.05 * -1.5;
         ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         float ☃xxxxxxxxxxxxxxxx = ☃x * ☃x;
         int ☃xxxxxxxxxxxxxxxxx = 64 + (int)(☃xxxxxxxxxxxxxxxx * 191.0F);
         int ☃xxxxxxxxxxxxxxxxxx = 32 + (int)(☃xxxxxxxxxxxxxxxx * 191.0F);
         int ☃xxxxxxxxxxxxxxxxxxx = 128 - (int)(☃xxxxxxxxxxxxxxxx * 64.0F);
         double ☃xxxxxxxxxxxxxxxxxxxx = 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxx = 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + (Math.PI * 3.0 / 4.0)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + (Math.PI * 3.0 / 4.0)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + (Math.PI / 4)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + (Math.PI / 4)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + (Math.PI * 5.0 / 4.0)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + (Math.PI * 5.0 / 4.0)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + (Math.PI * 7.0 / 4.0)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + (Math.PI * 7.0 / 4.0)) * 0.282;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + Math.PI) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + Math.PI) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + 0.0) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + 0.0) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + (Math.PI / 2)) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + (Math.PI / 2)) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.cos(☃xxxxxxxxxxxxxxx + (Math.PI * 3.0 / 2.0)) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 + Math.sin(☃xxxxxxxxxxxxxxx + (Math.PI * 3.0 / 2.0)) * 0.2;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.4999;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = -1.0F + ☃xxxxxx;
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx * 2.5 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.4999, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.4999, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.4999, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.4999, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0;
         if (☃.ticksExisted % 2 == 0) {
            ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.5;
         }

         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.5, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xxx.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .tex(0.5, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
            .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 255)
            .endVertex();
         ☃xx.draw();
         GlStateManager.popMatrix();
      }
   }

   protected ResourceLocation getEntityTexture(EntityGuardian var1) {
      return GUARDIAN_TEXTURE;
   }
}
