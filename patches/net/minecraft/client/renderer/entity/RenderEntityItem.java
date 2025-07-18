package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderEntityItem extends Render<EntityItem> {
   private final RenderItem itemRenderer;
   private final Random random = new Random();

   public RenderEntityItem(RenderManager var1, RenderItem var2) {
      super(☃);
      this.itemRenderer = ☃;
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   private int transformModelCount(EntityItem var1, double var2, double var4, double var6, float var8, IBakedModel var9) {
      ItemStack ☃ = ☃.getItem();
      Item ☃x = ☃.getItem();
      if (☃x == null) {
         return 0;
      } else {
         boolean ☃xx = ☃.isGui3d();
         int ☃xxx = this.getModelCount(☃);
         float ☃xxxx = 0.25F;
         float ☃xxxxx = MathHelper.sin((☃.getAge() + ☃) / 10.0F + ☃.hoverStart) * 0.1F + 0.1F;
         float ☃xxxxxx = ☃.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
         GlStateManager.translate((float)☃, (float)☃ + ☃xxxxx + 0.25F * ☃xxxxxx, (float)☃);
         if (☃xx || this.renderManager.options != null) {
            float ☃xxxxxxx = ((☃.getAge() + ☃) / 20.0F + ☃.hoverStart) * (180.0F / (float)Math.PI);
            GlStateManager.rotate(☃xxxxxxx, 0.0F, 1.0F, 0.0F);
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         return ☃xxx;
      }
   }

   private int getModelCount(ItemStack var1) {
      int ☃ = 1;
      if (☃.getCount() > 48) {
         ☃ = 5;
      } else if (☃.getCount() > 32) {
         ☃ = 4;
      } else if (☃.getCount() > 16) {
         ☃ = 3;
      } else if (☃.getCount() > 1) {
         ☃ = 2;
      }

      return ☃;
   }

   public void doRender(EntityItem var1, double var2, double var4, double var6, float var8, float var9) {
      ItemStack ☃ = ☃.getItem();
      int ☃x = ☃.isEmpty() ? 187 : Item.getIdFromItem(☃.getItem()) + ☃.getMetadata();
      this.random.setSeed(☃x);
      boolean ☃xx = false;
      if (this.bindEntityTexture(☃)) {
         this.renderManager.renderEngine.getTexture(this.getEntityTexture(☃)).setBlurMipmap(false, false);
         ☃xx = true;
      }

      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.pushMatrix();
      IBakedModel ☃xxx = this.itemRenderer.getItemModelWithOverrides(☃, ☃.world, null);
      int ☃xxxx = this.transformModelCount(☃, ☃, ☃, ☃, ☃, ☃xxx);
      float ☃xxxxx = ☃xxx.getItemCameraTransforms().ground.scale.x;
      float ☃xxxxxx = ☃xxx.getItemCameraTransforms().ground.scale.y;
      float ☃xxxxxxx = ☃xxx.getItemCameraTransforms().ground.scale.z;
      boolean ☃xxxxxxxx = ☃xxx.isGui3d();
      if (!☃xxxxxxxx) {
         float ☃xxxxxxxxx = -0.0F * (☃xxxx - 1) * 0.5F * ☃xxxxx;
         float ☃xxxxxxxxxx = -0.0F * (☃xxxx - 1) * 0.5F * ☃xxxxxx;
         float ☃xxxxxxxxxxx = -0.09375F * (☃xxxx - 1) * 0.5F * ☃xxxxxxx;
         GlStateManager.translate(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
      }

      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃xxxx; ☃xxxxxxxxx++) {
         if (☃xxxxxxxx) {
            GlStateManager.pushMatrix();
            if (☃xxxxxxxxx > 0) {
               float ☃xxxxxxxxxx = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float ☃xxxxxxxxxxx = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float ☃xxxxxxxxxxxx = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               GlStateManager.translate(☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
            }

            ☃xxx.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
            this.itemRenderer.renderItem(☃, ☃xxx);
            GlStateManager.popMatrix();
         } else {
            GlStateManager.pushMatrix();
            if (☃xxxxxxxxx > 0) {
               float ☃xxxxxxxxxx = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
               float ☃xxxxxxxxxxx = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
               GlStateManager.translate(☃xxxxxxxxxx, ☃xxxxxxxxxxx, 0.0F);
            }

            ☃xxx.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
            this.itemRenderer.renderItem(☃, ☃xxx);
            GlStateManager.popMatrix();
            GlStateManager.translate(0.0F * ☃xxxxx, 0.0F * ☃xxxxxx, 0.09375F * ☃xxxxxxx);
         }
      }

      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.bindEntityTexture(☃);
      if (☃xx) {
         this.renderManager.renderEngine.getTexture(this.getEntityTexture(☃)).restoreLastBlurMipmap();
      }

      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityItem var1) {
      return TextureMap.LOCATION_BLOCKS_TEXTURE;
   }
}
