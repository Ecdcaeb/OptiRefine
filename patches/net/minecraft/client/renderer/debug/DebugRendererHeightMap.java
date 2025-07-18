package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererHeightMap implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;

   public DebugRendererHeightMap(Minecraft var1) {
      this.minecraft = ☃;
   }

   @Override
   public void render(float var1, long var2) {
      EntityPlayer ☃ = this.minecraft.player;
      World ☃x = this.minecraft.world;
      double ☃xx = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃xxx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xxxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.disableTexture2D();
      BlockPos ☃xxxxx = new BlockPos(☃.posX, 0.0, ☃.posZ);
      Iterable<BlockPos> ☃xxxxxx = BlockPos.getAllInBox(☃xxxxx.add(-40, 0, -40), ☃xxxxx.add(40, 0, 40));
      Tessellator ☃xxxxxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxxxxx = ☃xxxxxxx.getBuffer();
      ☃xxxxxxxx.begin(5, DefaultVertexFormats.POSITION_COLOR);

      for (BlockPos ☃xxxxxxxxx : ☃xxxxxx) {
         int ☃xxxxxxxxxx = ☃x.getHeight(☃xxxxxxxxx.getX(), ☃xxxxxxxxx.getZ());
         if (☃x.getBlockState(☃xxxxxxxxx.add(0, ☃xxxxxxxxxx, 0).down()) == Blocks.AIR.getDefaultState()) {
            RenderGlobal.addChainedFilledBoxVertices(
               ☃xxxxxxxx,
               ☃xxxxxxxxx.getX() + 0.25F - ☃xx,
               ☃xxxxxxxxxx - ☃xxx,
               ☃xxxxxxxxx.getZ() + 0.25F - ☃xxxx,
               ☃xxxxxxxxx.getX() + 0.75F - ☃xx,
               ☃xxxxxxxxxx + 0.09375 - ☃xxx,
               ☃xxxxxxxxx.getZ() + 0.75F - ☃xxxx,
               0.0F,
               0.0F,
               1.0F,
               0.5F
            );
         } else {
            RenderGlobal.addChainedFilledBoxVertices(
               ☃xxxxxxxx,
               ☃xxxxxxxxx.getX() + 0.25F - ☃xx,
               ☃xxxxxxxxxx - ☃xxx,
               ☃xxxxxxxxx.getZ() + 0.25F - ☃xxxx,
               ☃xxxxxxxxx.getX() + 0.75F - ☃xx,
               ☃xxxxxxxxxx + 0.09375 - ☃xxx,
               ☃xxxxxxxxx.getZ() + 0.75F - ☃xxxx,
               0.0F,
               1.0F,
               0.0F,
               0.5F
            );
         }
      }

      ☃xxxxxxx.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.popMatrix();
   }
}
