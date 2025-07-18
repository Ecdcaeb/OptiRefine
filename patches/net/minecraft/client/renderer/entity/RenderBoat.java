package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.IMultipassModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderBoat extends Render<EntityBoat> {
   private static final ResourceLocation[] BOAT_TEXTURES = new ResourceLocation[]{
      new ResourceLocation("textures/entity/boat/boat_oak.png"),
      new ResourceLocation("textures/entity/boat/boat_spruce.png"),
      new ResourceLocation("textures/entity/boat/boat_birch.png"),
      new ResourceLocation("textures/entity/boat/boat_jungle.png"),
      new ResourceLocation("textures/entity/boat/boat_acacia.png"),
      new ResourceLocation("textures/entity/boat/boat_darkoak.png")
   };
   protected ModelBase modelBoat = new ModelBoat();

   public RenderBoat(RenderManager var1) {
      super(☃);
      this.shadowSize = 0.5F;
   }

   public void doRender(EntityBoat var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      this.setupTranslation(☃, ☃, ☃);
      this.setupRotation(☃, ☃, ☃);
      this.bindEntityTexture(☃);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      this.modelBoat.render(☃, ☃, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public void setupRotation(EntityBoat var1, float var2, float var3) {
      GlStateManager.rotate(180.0F - ☃, 0.0F, 1.0F, 0.0F);
      float ☃ = ☃.getTimeSinceHit() - ☃;
      float ☃x = ☃.getDamageTaken() - ☃;
      if (☃x < 0.0F) {
         ☃x = 0.0F;
      }

      if (☃ > 0.0F) {
         GlStateManager.rotate(MathHelper.sin(☃) * ☃ * ☃x / 10.0F * ☃.getForwardDirection(), 1.0F, 0.0F, 0.0F);
      }

      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
   }

   public void setupTranslation(double var1, double var3, double var5) {
      GlStateManager.translate((float)☃, (float)☃ + 0.375F, (float)☃);
   }

   protected ResourceLocation getEntityTexture(EntityBoat var1) {
      return BOAT_TEXTURES[☃.getBoatType().ordinal()];
   }

   @Override
   public boolean isMultipass() {
      return true;
   }

   public void renderMultipass(EntityBoat var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      this.setupTranslation(☃, ☃, ☃);
      this.setupRotation(☃, ☃, ☃);
      this.bindEntityTexture(☃);
      ((IMultipassModel)this.modelBoat).renderMultipass(☃, ☃, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
   }
}
