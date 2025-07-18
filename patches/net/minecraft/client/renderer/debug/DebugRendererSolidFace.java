package net.minecraft.client.renderer.debug;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DebugRendererSolidFace implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;

   public DebugRendererSolidFace(Minecraft var1) {
      this.minecraft = ☃;
   }

   @Override
   public void render(float var1, long var2) {
      EntityPlayer ☃ = this.minecraft.player;
      double ☃x = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃xx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      World ☃xxxx = this.minecraft.player.world;
      Iterable<BlockPos> ☃xxxxx = BlockPos.getAllInBox(
         MathHelper.floor(☃.posX - 6.0),
         MathHelper.floor(☃.posY - 6.0),
         MathHelper.floor(☃.posZ - 6.0),
         MathHelper.floor(☃.posX + 6.0),
         MathHelper.floor(☃.posY + 6.0),
         MathHelper.floor(☃.posZ + 6.0)
      );
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);

      for (BlockPos ☃xxxxxx : ☃xxxxx) {
         IBlockState ☃xxxxxxx = ☃xxxx.getBlockState(☃xxxxxx);
         if (☃xxxxxxx.getBlock() != Blocks.AIR) {
            AxisAlignedBB ☃xxxxxxxx = ☃xxxxxxx.getSelectedBoundingBox(☃xxxx, ☃xxxxxx).grow(0.002).offset(-☃x, -☃xx, -☃xxx);
            double ☃xxxxxxxxx = ☃xxxxxxxx.minX;
            double ☃xxxxxxxxxx = ☃xxxxxxxx.minY;
            double ☃xxxxxxxxxxx = ☃xxxxxxxx.minZ;
            double ☃xxxxxxxxxxxx = ☃xxxxxxxx.maxX;
            double ☃xxxxxxxxxxxxx = ☃xxxxxxxx.maxY;
            double ☃xxxxxxxxxxxxxx = ☃xxxxxxxx.maxZ;
            float ☃xxxxxxxxxxxxxxx = 1.0F;
            float ☃xxxxxxxxxxxxxxxx = 0.0F;
            float ☃xxxxxxxxxxxxxxxxx = 0.0F;
            float ☃xxxxxxxxxxxxxxxxxx = 0.5F;
            if (☃xxxxxxx.getBlockFaceShape(☃xxxx, ☃xxxxxx, EnumFacing.WEST) == BlockFaceShape.SOLID) {
               Tessellator ☃xxxxxxxxxxxxxxxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.getBuffer();
               ☃xxxxxxxxxxxxxxxxxxxx.begin(5, DefaultVertexFormats.POSITION_COLOR);
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx.draw();
            }

            if (☃xxxxxxx.getBlockFaceShape(☃xxxx, ☃xxxxxx, EnumFacing.SOUTH) == BlockFaceShape.SOLID) {
               Tessellator ☃xxxxxxxxxxxxxxxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.getBuffer();
               ☃xxxxxxxxxxxxxxxxxxxx.begin(5, DefaultVertexFormats.POSITION_COLOR);
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx.draw();
            }

            if (☃xxxxxxx.getBlockFaceShape(☃xxxx, ☃xxxxxx, EnumFacing.EAST) == BlockFaceShape.SOLID) {
               Tessellator ☃xxxxxxxxxxxxxxxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.getBuffer();
               ☃xxxxxxxxxxxxxxxxxxxx.begin(5, DefaultVertexFormats.POSITION_COLOR);
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx.draw();
            }

            if (☃xxxxxxx.getBlockFaceShape(☃xxxx, ☃xxxxxx, EnumFacing.NORTH) == BlockFaceShape.SOLID) {
               Tessellator ☃xxxxxxxxxxxxxxxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.getBuffer();
               ☃xxxxxxxxxxxxxxxxxxxx.begin(5, DefaultVertexFormats.POSITION_COLOR);
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx.draw();
            }

            if (☃xxxxxxx.getBlockFaceShape(☃xxxx, ☃xxxxxx, EnumFacing.DOWN) == BlockFaceShape.SOLID) {
               Tessellator ☃xxxxxxxxxxxxxxxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.getBuffer();
               ☃xxxxxxxxxxxxxxxxxxxx.begin(5, DefaultVertexFormats.POSITION_COLOR);
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx.draw();
            }

            if (☃xxxxxxx.getBlockFaceShape(☃xxxx, ☃xxxxxx, EnumFacing.UP) == BlockFaceShape.SOLID) {
               Tessellator ☃xxxxxxxxxxxxxxxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.getBuffer();
               ☃xxxxxxxxxxxxxxxxxxxx.begin(5, DefaultVertexFormats.POSITION_COLOR);
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxxx.pos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx.draw();
            }
         }
      }

      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
