package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityStructureRenderer extends TileEntitySpecialRenderer<TileEntityStructure> {
   public void render(TileEntityStructure var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      if (Minecraft.getMinecraft().player.canUseCommandBlock() || Minecraft.getMinecraft().player.isSpectator()) {
         super.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         BlockPos ☃ = ☃.getPosition();
         BlockPos ☃x = ☃.getStructureSize();
         if (☃x.getX() >= 1 && ☃x.getY() >= 1 && ☃x.getZ() >= 1) {
            if (☃.getMode() == TileEntityStructure.Mode.SAVE || ☃.getMode() == TileEntityStructure.Mode.LOAD) {
               double ☃xx = 0.01;
               double ☃xxx = ☃.getX();
               double ☃xxxx = ☃.getZ();
               double ☃xxxxx = ☃ + ☃.getY() - 0.01;
               double ☃xxxxxx = ☃xxxxx + ☃x.getY() + 0.02;
               double ☃xxxxxxx;
               double ☃xxxxxxxx;
               switch (☃.getMirror()) {
                  case LEFT_RIGHT:
                     ☃xxxxxxx = ☃x.getX() + 0.02;
                     ☃xxxxxxxx = -(☃x.getZ() + 0.02);
                     break;
                  case FRONT_BACK:
                     ☃xxxxxxx = -(☃x.getX() + 0.02);
                     ☃xxxxxxxx = ☃x.getZ() + 0.02;
                     break;
                  default:
                     ☃xxxxxxx = ☃x.getX() + 0.02;
                     ☃xxxxxxxx = ☃x.getZ() + 0.02;
               }

               double ☃xx;
               double ☃xxx;
               double ☃xxxx;
               double ☃xxxxx;
               switch (☃.getRotation()) {
                  case CLOCKWISE_90:
                     ☃xx = ☃ + (☃xxxxxxxx < 0.0 ? ☃xxx - 0.01 : ☃xxx + 1.0 + 0.01);
                     ☃xxx = ☃ + (☃xxxxxxx < 0.0 ? ☃xxxx + 1.0 + 0.01 : ☃xxxx - 0.01);
                     ☃xxxx = ☃xx - ☃xxxxxxxx;
                     ☃xxxxx = ☃xxx + ☃xxxxxxx;
                     break;
                  case CLOCKWISE_180:
                     ☃xx = ☃ + (☃xxxxxxx < 0.0 ? ☃xxx - 0.01 : ☃xxx + 1.0 + 0.01);
                     ☃xxx = ☃ + (☃xxxxxxxx < 0.0 ? ☃xxxx - 0.01 : ☃xxxx + 1.0 + 0.01);
                     ☃xxxx = ☃xx - ☃xxxxxxx;
                     ☃xxxxx = ☃xxx - ☃xxxxxxxx;
                     break;
                  case COUNTERCLOCKWISE_90:
                     ☃xx = ☃ + (☃xxxxxxxx < 0.0 ? ☃xxx + 1.0 + 0.01 : ☃xxx - 0.01);
                     ☃xxx = ☃ + (☃xxxxxxx < 0.0 ? ☃xxxx - 0.01 : ☃xxxx + 1.0 + 0.01);
                     ☃xxxx = ☃xx + ☃xxxxxxxx;
                     ☃xxxxx = ☃xxx - ☃xxxxxxx;
                     break;
                  default:
                     ☃xx = ☃ + (☃xxxxxxx < 0.0 ? ☃xxx + 1.0 + 0.01 : ☃xxx - 0.01);
                     ☃xxx = ☃ + (☃xxxxxxxx < 0.0 ? ☃xxxx + 1.0 + 0.01 : ☃xxxx - 0.01);
                     ☃xxxx = ☃xx + ☃xxxxxxx;
                     ☃xxxxx = ☃xxx + ☃xxxxxxxx;
               }

               int ☃xx = 255;
               int ☃xxx = 223;
               int ☃xxxx = 127;
               Tessellator ☃xxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxx = ☃xxxxx.getBuffer();
               GlStateManager.disableFog();
               GlStateManager.disableLighting();
               GlStateManager.disableTexture2D();
               GlStateManager.enableBlend();
               GlStateManager.tryBlendFuncSeparate(
                  GlStateManager.SourceFactor.SRC_ALPHA,
                  GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                  GlStateManager.SourceFactor.ONE,
                  GlStateManager.DestFactor.ZERO
               );
               this.setLightmapDisabled(true);
               if (☃.getMode() == TileEntityStructure.Mode.SAVE || ☃.showsBoundingBox()) {
                  this.renderBox(☃xxxxx, ☃xxxxxx, ☃xx, ☃xxxxx, ☃xxx, ☃xxxx, ☃xxxxxx, ☃xxxxx, 255, 223, 127);
               }

               if (☃.getMode() == TileEntityStructure.Mode.SAVE && ☃.showsAir()) {
                  this.renderInvisibleBlocks(☃, ☃, ☃, ☃, ☃, ☃xxxxx, ☃xxxxxx, true);
                  this.renderInvisibleBlocks(☃, ☃, ☃, ☃, ☃, ☃xxxxx, ☃xxxxxx, false);
               }

               this.setLightmapDisabled(false);
               GlStateManager.glLineWidth(1.0F);
               GlStateManager.enableLighting();
               GlStateManager.enableTexture2D();
               GlStateManager.enableDepth();
               GlStateManager.depthMask(true);
               GlStateManager.enableFog();
            }
         }
      }
   }

   private void renderInvisibleBlocks(
      TileEntityStructure var1, double var2, double var4, double var6, BlockPos var8, Tessellator var9, BufferBuilder var10, boolean var11
   ) {
      GlStateManager.glLineWidth(☃ ? 3.0F : 1.0F);
      ☃.begin(3, DefaultVertexFormats.POSITION_COLOR);
      World ☃ = ☃.getWorld();
      BlockPos ☃x = ☃.getPos();
      BlockPos ☃xx = ☃x.add(☃);

      for (BlockPos ☃xxx : BlockPos.getAllInBox(☃xx, ☃xx.add(☃.getStructureSize()).add(-1, -1, -1))) {
         IBlockState ☃xxxx = ☃.getBlockState(☃xxx);
         boolean ☃xxxxx = ☃xxxx == Blocks.AIR.getDefaultState();
         boolean ☃xxxxxx = ☃xxxx == Blocks.STRUCTURE_VOID.getDefaultState();
         if (☃xxxxx || ☃xxxxxx) {
            float ☃xxxxxxx = ☃xxxxx ? 0.05F : 0.0F;
            double ☃xxxxxxxx = ☃xxx.getX() - ☃x.getX() + 0.45F + ☃ - ☃xxxxxxx;
            double ☃xxxxxxxxx = ☃xxx.getY() - ☃x.getY() + 0.45F + ☃ - ☃xxxxxxx;
            double ☃xxxxxxxxxx = ☃xxx.getZ() - ☃x.getZ() + 0.45F + ☃ - ☃xxxxxxx;
            double ☃xxxxxxxxxxx = ☃xxx.getX() - ☃x.getX() + 0.55F + ☃ + ☃xxxxxxx;
            double ☃xxxxxxxxxxxx = ☃xxx.getY() - ☃x.getY() + 0.55F + ☃ + ☃xxxxxxx;
            double ☃xxxxxxxxxxxxx = ☃xxx.getZ() - ☃x.getZ() + 0.55F + ☃ + ☃xxxxxxx;
            if (☃) {
               RenderGlobal.drawBoundingBox(☃, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 0.0F, 0.0F, 0.0F, 1.0F);
            } else if (☃xxxxx) {
               RenderGlobal.drawBoundingBox(☃, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 0.5F, 0.5F, 1.0F, 1.0F);
            } else {
               RenderGlobal.drawBoundingBox(☃, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 1.0F, 0.25F, 0.25F, 1.0F);
            }
         }
      }

      ☃.draw();
   }

   private void renderBox(
      Tessellator var1, BufferBuilder var2, double var3, double var5, double var7, double var9, double var11, double var13, int var15, int var16, int var17
   ) {
      GlStateManager.glLineWidth(2.0F);
      ☃.begin(3, DefaultVertexFormats.POSITION_COLOR);
      ☃.pos(☃, ☃, ☃).color((float)☃, (float)☃, (float)☃, 0.0F).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color((float)☃, (float)☃, (float)☃, 0.0F).endVertex();
      ☃.draw();
      GlStateManager.glLineWidth(1.0F);
   }

   public boolean isGlobalRenderer(TileEntityStructure var1) {
      return true;
   }
}
