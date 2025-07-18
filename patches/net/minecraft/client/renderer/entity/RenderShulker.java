package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RenderShulker extends RenderLiving<EntityShulker> {
   public static final ResourceLocation[] SHULKER_ENDERGOLEM_TEXTURE = new ResourceLocation[]{
      new ResourceLocation("textures/entity/shulker/shulker_white.png"),
      new ResourceLocation("textures/entity/shulker/shulker_orange.png"),
      new ResourceLocation("textures/entity/shulker/shulker_magenta.png"),
      new ResourceLocation("textures/entity/shulker/shulker_light_blue.png"),
      new ResourceLocation("textures/entity/shulker/shulker_yellow.png"),
      new ResourceLocation("textures/entity/shulker/shulker_lime.png"),
      new ResourceLocation("textures/entity/shulker/shulker_pink.png"),
      new ResourceLocation("textures/entity/shulker/shulker_gray.png"),
      new ResourceLocation("textures/entity/shulker/shulker_silver.png"),
      new ResourceLocation("textures/entity/shulker/shulker_cyan.png"),
      new ResourceLocation("textures/entity/shulker/shulker_purple.png"),
      new ResourceLocation("textures/entity/shulker/shulker_blue.png"),
      new ResourceLocation("textures/entity/shulker/shulker_brown.png"),
      new ResourceLocation("textures/entity/shulker/shulker_green.png"),
      new ResourceLocation("textures/entity/shulker/shulker_red.png"),
      new ResourceLocation("textures/entity/shulker/shulker_black.png")
   };

   public RenderShulker(RenderManager var1) {
      super(☃, new ModelShulker(), 0.0F);
      this.addLayer(new RenderShulker.HeadLayer());
   }

   public ModelShulker getMainModel() {
      return (ModelShulker)super.getMainModel();
   }

   public void doRender(EntityShulker var1, double var2, double var4, double var6, float var8, float var9) {
      int ☃ = ☃.getClientTeleportInterp();
      if (☃ > 0 && ☃.isAttachedToBlock()) {
         BlockPos ☃x = ☃.getAttachmentPos();
         BlockPos ☃xx = ☃.getOldAttachPos();
         double ☃xxx = (☃ - ☃) / 6.0;
         ☃xxx *= ☃xxx;
         double ☃xxxx = (☃x.getX() - ☃xx.getX()) * ☃xxx;
         double ☃xxxxx = (☃x.getY() - ☃xx.getY()) * ☃xxx;
         double ☃xxxxxx = (☃x.getZ() - ☃xx.getZ()) * ☃xxx;
         super.doRender(☃, ☃ - ☃xxxx, ☃ - ☃xxxxx, ☃ - ☃xxxxxx, ☃, ☃);
      } else {
         super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public boolean shouldRender(EntityShulker var1, ICamera var2, double var3, double var5, double var7) {
      if (super.shouldRender(☃, ☃, ☃, ☃, ☃)) {
         return true;
      } else {
         if (☃.getClientTeleportInterp() > 0 && ☃.isAttachedToBlock()) {
            BlockPos ☃ = ☃.getOldAttachPos();
            BlockPos ☃x = ☃.getAttachmentPos();
            Vec3d ☃xx = new Vec3d(☃x.getX(), ☃x.getY(), ☃x.getZ());
            Vec3d ☃xxx = new Vec3d(☃.getX(), ☃.getY(), ☃.getZ());
            if (☃.isBoundingBoxInFrustum(new AxisAlignedBB(☃xxx.x, ☃xxx.y, ☃xxx.z, ☃xx.x, ☃xx.y, ☃xx.z))) {
               return true;
            }
         }

         return false;
      }
   }

   protected ResourceLocation getEntityTexture(EntityShulker var1) {
      return SHULKER_ENDERGOLEM_TEXTURE[☃.getColor().getMetadata()];
   }

   protected void applyRotations(EntityShulker var1, float var2, float var3, float var4) {
      super.applyRotations(☃, ☃, ☃, ☃);
      switch (☃.getAttachmentFacing()) {
         case DOWN:
         default:
            break;
         case EAST:
            GlStateManager.translate(0.5F, 0.5F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            break;
         case WEST:
            GlStateManager.translate(-0.5F, 0.5F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            break;
         case NORTH:
            GlStateManager.translate(0.0F, 0.5F, -0.5F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            break;
         case SOUTH:
            GlStateManager.translate(0.0F, 0.5F, 0.5F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            break;
         case UP:
            GlStateManager.translate(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      }
   }

   protected void preRenderCallback(EntityShulker var1, float var2) {
      float ☃ = 0.999F;
      GlStateManager.scale(0.999F, 0.999F, 0.999F);
   }

   class HeadLayer implements LayerRenderer<EntityShulker> {
      private HeadLayer() {
      }

      public void doRenderLayer(EntityShulker var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         GlStateManager.pushMatrix();
         switch (☃.getAttachmentFacing()) {
            case DOWN:
            default:
               break;
            case EAST:
               GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.translate(1.0F, -1.0F, 0.0F);
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
               break;
            case WEST:
               GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.translate(-1.0F, -1.0F, 0.0F);
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
               break;
            case NORTH:
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.translate(0.0F, -1.0F, -1.0F);
               break;
            case SOUTH:
               GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.translate(0.0F, -1.0F, 1.0F);
               break;
            case UP:
               GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.translate(0.0F, -2.0F, 0.0F);
         }

         ModelRenderer ☃ = RenderShulker.this.getMainModel().head;
         ☃.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
         ☃.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
         RenderShulker.this.bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[☃.getColor().getMetadata()]);
         ☃.render(☃);
         GlStateManager.popMatrix();
      }

      @Override
      public boolean shouldCombineTextures() {
         return false;
      }
   }
}
