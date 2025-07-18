package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerSheepWool implements LayerRenderer<EntitySheep> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
   private final RenderSheep sheepRenderer;
   private final ModelSheep1 sheepModel = new ModelSheep1();

   public LayerSheepWool(RenderSheep var1) {
      this.sheepRenderer = ☃;
   }

   public void doRenderLayer(EntitySheep var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (!☃.getSheared() && !☃.isInvisible()) {
         this.sheepRenderer.bindTexture(TEXTURE);
         if (☃.hasCustomName() && "jeb_".equals(☃.getCustomNameTag())) {
            int ☃ = 25;
            int ☃x = ☃.ticksExisted / 25 + ☃.getEntityId();
            int ☃xx = EnumDyeColor.values().length;
            int ☃xxx = ☃x % ☃xx;
            int ☃xxxx = (☃x + 1) % ☃xx;
            float ☃xxxxx = (☃.ticksExisted % 25 + ☃) / 25.0F;
            float[] ☃xxxxxx = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(☃xxx));
            float[] ☃xxxxxxx = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(☃xxxx));
            GlStateManager.color(
               ☃xxxxxx[0] * (1.0F - ☃xxxxx) + ☃xxxxxxx[0] * ☃xxxxx,
               ☃xxxxxx[1] * (1.0F - ☃xxxxx) + ☃xxxxxxx[1] * ☃xxxxx,
               ☃xxxxxx[2] * (1.0F - ☃xxxxx) + ☃xxxxxxx[2] * ☃xxxxx
            );
         } else {
            float[] ☃ = EntitySheep.getDyeRgb(☃.getFleeceColor());
            GlStateManager.color(☃[0], ☃[1], ☃[2]);
         }

         this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
         this.sheepModel.setLivingAnimations(☃, ☃, ☃, ☃);
         this.sheepModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return true;
   }
}
