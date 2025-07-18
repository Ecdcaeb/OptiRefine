package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;

public class RenderEnderman extends RenderLiving<EntityEnderman> {
   private static final ResourceLocation ENDERMAN_TEXTURES = new ResourceLocation("textures/entity/enderman/enderman.png");
   private final Random rnd = new Random();

   public RenderEnderman(RenderManager var1) {
      super(☃, new ModelEnderman(0.0F), 0.5F);
      this.addLayer(new LayerEndermanEyes(this));
      this.addLayer(new LayerHeldBlock(this));
   }

   public ModelEnderman getMainModel() {
      return (ModelEnderman)super.getMainModel();
   }

   public void doRender(EntityEnderman var1, double var2, double var4, double var6, float var8, float var9) {
      IBlockState ☃ = ☃.getHeldBlockState();
      ModelEnderman ☃x = this.getMainModel();
      ☃x.isCarrying = ☃ != null;
      ☃x.isAttacking = ☃.isScreaming();
      if (☃.isScreaming()) {
         double ☃xx = 0.02;
         ☃ += this.rnd.nextGaussian() * 0.02;
         ☃ += this.rnd.nextGaussian() * 0.02;
      }

      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityEnderman var1) {
      return ENDERMAN_TEXTURES;
   }
}
