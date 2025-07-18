package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;

public class RenderItemFrame extends Render<EntityItemFrame> {
   private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
   private final Minecraft mc = Minecraft.getMinecraft();
   private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
   private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
   private final RenderItem itemRenderer;

   public RenderItemFrame(RenderManager var1, RenderItem var2) {
      super(☃);
      this.itemRenderer = ☃;
   }

   public void doRender(EntityItemFrame var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      BlockPos ☃ = ☃.getHangingPosition();
      double ☃x = ☃.getX() - ☃.posX + ☃;
      double ☃xx = ☃.getY() - ☃.posY + ☃;
      double ☃xxx = ☃.getZ() - ☃.posZ + ☃;
      GlStateManager.translate(☃x + 0.5, ☃xx + 0.5, ☃xxx + 0.5);
      GlStateManager.rotate(180.0F - ☃.rotationYaw, 0.0F, 1.0F, 0.0F);
      this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      BlockRendererDispatcher ☃xxxx = this.mc.getBlockRendererDispatcher();
      ModelManager ☃xxxxx = ☃xxxx.getBlockModelShapes().getModelManager();
      IBakedModel ☃xxxxxx;
      if (☃.getDisplayedItem().getItem() == Items.FILLED_MAP) {
         ☃xxxxxx = ☃xxxxx.getModel(this.mapModel);
      } else {
         ☃xxxxxx = ☃xxxxx.getModel(this.itemFrameModel);
      }

      GlStateManager.pushMatrix();
      GlStateManager.translate(-0.5F, -0.5F, -0.5F);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      ☃xxxx.getBlockModelRenderer().renderModelBrightnessColor(☃xxxxxx, 1.0F, 1.0F, 1.0F, 1.0F);
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.popMatrix();
      GlStateManager.translate(0.0F, 0.0F, 0.4375F);
      this.renderItem(☃);
      GlStateManager.popMatrix();
      this.renderName(☃, ☃ + ☃.facingDirection.getXOffset() * 0.3F, ☃ - 0.25, ☃ + ☃.facingDirection.getZOffset() * 0.3F);
   }

   @Nullable
   protected ResourceLocation getEntityTexture(EntityItemFrame var1) {
      return null;
   }

   private void renderItem(EntityItemFrame var1) {
      ItemStack ☃ = ☃.getDisplayedItem();
      if (!☃.isEmpty()) {
         GlStateManager.pushMatrix();
         GlStateManager.disableLighting();
         boolean ☃x = ☃.getItem() == Items.FILLED_MAP;
         int ☃xx = ☃x ? ☃.getRotation() % 4 * 2 : ☃.getRotation();
         GlStateManager.rotate(☃xx * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
         if (☃x) {
            this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            float ☃xxx = 0.0078125F;
            GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
            GlStateManager.translate(-64.0F, -64.0F, 0.0F);
            MapData ☃xxxx = Items.FILLED_MAP.getMapData(☃, ☃.world);
            GlStateManager.translate(0.0F, 0.0F, -1.0F);
            if (☃xxxx != null) {
               this.mc.entityRenderer.getMapItemRenderer().renderMap(☃xxxx, true);
            }
         } else {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            this.itemRenderer.renderItem(☃, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
         }

         GlStateManager.enableLighting();
         GlStateManager.popMatrix();
      }
   }

   protected void renderName(EntityItemFrame var1, double var2, double var4, double var6) {
      if (Minecraft.isGuiEnabled() && !☃.getDisplayedItem().isEmpty() && ☃.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == ☃) {
         double ☃ = ☃.getDistanceSq(this.renderManager.renderViewEntity);
         float ☃x = ☃.isSneaking() ? 32.0F : 64.0F;
         if (!(☃ >= ☃x * ☃x)) {
            String ☃xx = ☃.getDisplayedItem().getDisplayName();
            this.renderLivingLabel(☃, ☃xx, ☃, ☃, ☃, 64);
         }
      }
   }
}
