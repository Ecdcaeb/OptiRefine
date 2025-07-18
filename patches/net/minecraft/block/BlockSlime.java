package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSlime extends BlockBreakable {
   public BlockSlime() {
      super(Material.CLAY, false, MapColor.GRASS);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.slipperiness = 0.8F;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   @Override
   public void onFallenUpon(World var1, BlockPos var2, Entity var3, float var4) {
      if (☃.isSneaking()) {
         super.onFallenUpon(☃, ☃, ☃, ☃);
      } else {
         ☃.fall(☃, 0.0F);
      }
   }

   @Override
   public void onLanded(World var1, Entity var2) {
      if (☃.isSneaking()) {
         super.onLanded(☃, ☃);
      } else if (☃.motionY < 0.0) {
         ☃.motionY = -☃.motionY;
         if (!(☃ instanceof EntityLivingBase)) {
            ☃.motionY *= 0.8;
         }
      }
   }

   @Override
   public void onEntityWalk(World var1, BlockPos var2, Entity var3) {
      if (Math.abs(☃.motionY) < 0.1 && !☃.isSneaking()) {
         double ☃ = 0.4 + Math.abs(☃.motionY) * 0.2;
         ☃.motionX *= ☃;
         ☃.motionZ *= ☃;
      }

      super.onEntityWalk(☃, ☃, ☃);
   }
}
