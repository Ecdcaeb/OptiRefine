package net.minecraft.client.renderer.debug;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererWater implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;
   private EntityPlayer player;
   private double xo;
   private double yo;
   private double zo;

   public DebugRendererWater(Minecraft var1) {
      this.minecraft = ☃;
   }

   @Override
   public void render(float var1, long var2) {
      this.player = this.minecraft.player;
      this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * ☃;
      this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * ☃;
      this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * ☃;
      BlockPos ☃ = this.minecraft.player.getPosition();
      World ☃x = this.minecraft.player.world;
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.color(0.0F, 1.0F, 0.0F, 0.75F);
      GlStateManager.disableTexture2D();
      GlStateManager.glLineWidth(6.0F);

      for (BlockPos ☃xx : BlockPos.getAllInBox(☃.add(-10, -10, -10), ☃.add(10, 10, 10))) {
         IBlockState ☃xxx = ☃x.getBlockState(☃xx);
         if (☃xxx.getBlock() == Blocks.WATER || ☃xxx.getBlock() == Blocks.FLOWING_WATER) {
            double ☃xxxx = BlockLiquid.getLiquidHeight(☃xxx, ☃x, ☃xx);
            RenderGlobal.renderFilledBox(
               new AxisAlignedBB(☃xx.getX() + 0.01F, ☃xx.getY() + 0.01F, ☃xx.getZ() + 0.01F, ☃xx.getX() + 0.99F, ☃xxxx, ☃xx.getZ() + 0.99F)
                  .offset(-this.xo, -this.yo, -this.zo),
               1.0F,
               1.0F,
               1.0F,
               0.2F
            );
         }
      }

      for (BlockPos ☃xxx : BlockPos.getAllInBox(☃.add(-10, -10, -10), ☃.add(10, 10, 10))) {
         IBlockState ☃xxxx = ☃x.getBlockState(☃xxx);
         if (☃xxxx.getBlock() == Blocks.WATER || ☃xxxx.getBlock() == Blocks.FLOWING_WATER) {
            Integer ☃xxxxx = ☃xxxx.getValue(BlockLiquid.LEVEL);
            double ☃xxxxxx = ☃xxxxx > 7 ? 0.9 : 1.0 - 0.11 * ☃xxxxx.intValue();
            String ☃xxxxxxx = ☃xxxx.getBlock() == Blocks.FLOWING_WATER ? "f" : "s";
            DebugRenderer.renderDebugText(☃xxxxxxx + " " + ☃xxxxx, ☃xxx.getX() + 0.5, ☃xxx.getY() + ☃xxxxxx, ☃xxx.getZ() + 0.5, ☃, -16777216);
         }
      }

      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
