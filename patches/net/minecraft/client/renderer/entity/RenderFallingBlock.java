package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RenderFallingBlock extends Render<EntityFallingBlock> {
   public RenderFallingBlock(RenderManager var1) {
      super(☃);
      this.shadowSize = 0.5F;
   }

   public void doRender(EntityFallingBlock var1, double var2, double var4, double var6, float var8, float var9) {
      if (☃.getBlock() != null) {
         IBlockState ☃ = ☃.getBlock();
         if (☃.getRenderType() == EnumBlockRenderType.MODEL) {
            World ☃x = ☃.getWorldObj();
            if (☃ != ☃x.getBlockState(new BlockPos(☃)) && ☃.getRenderType() != EnumBlockRenderType.INVISIBLE) {
               this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
               GlStateManager.pushMatrix();
               GlStateManager.disableLighting();
               Tessellator ☃xx = Tessellator.getInstance();
               BufferBuilder ☃xxx = ☃xx.getBuffer();
               if (this.renderOutlines) {
                  GlStateManager.enableColorMaterial();
                  GlStateManager.enableOutlineMode(this.getTeamColor(☃));
               }

               ☃xxx.begin(7, DefaultVertexFormats.BLOCK);
               BlockPos ☃xxxx = new BlockPos(☃.posX, ☃.getEntityBoundingBox().maxY, ☃.posZ);
               GlStateManager.translate((float)(☃ - ☃xxxx.getX() - 0.5), (float)(☃ - ☃xxxx.getY()), (float)(☃ - ☃xxxx.getZ() - 0.5));
               BlockRendererDispatcher ☃xxxxx = Minecraft.getMinecraft().getBlockRendererDispatcher();
               ☃xxxxx.getBlockModelRenderer().renderModel(☃x, ☃xxxxx.getModelForState(☃), ☃, ☃xxxx, ☃xxx, false, MathHelper.getPositionRandom(☃.getOrigin()));
               ☃xx.draw();
               if (this.renderOutlines) {
                  GlStateManager.disableOutlineMode();
                  GlStateManager.disableColorMaterial();
               }

               GlStateManager.enableLighting();
               GlStateManager.popMatrix();
               super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
            }
         }
      }
   }

   protected ResourceLocation getEntityTexture(EntityFallingBlock var1) {
      return TextureMap.LOCATION_BLOCKS_TEXTURE;
   }
}
