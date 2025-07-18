package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLead extends Item {
   public ItemLead() {
      this.setCreativeTab(CreativeTabs.TOOLS);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      Block ☃ = ☃.getBlockState(☃).getBlock();
      if (!(☃ instanceof BlockFence)) {
         return EnumActionResult.PASS;
      } else {
         if (!☃.isRemote) {
            attachToFence(☃, ☃, ☃);
         }

         return EnumActionResult.SUCCESS;
      }
   }

   public static boolean attachToFence(EntityPlayer var0, World var1, BlockPos var2) {
      EntityLeashKnot ☃ = EntityLeashKnot.getKnotForPosition(☃, ☃);
      boolean ☃x = false;
      double ☃xx = 7.0;
      int ☃xxx = ☃.getX();
      int ☃xxxx = ☃.getY();
      int ☃xxxxx = ☃.getZ();

      for (EntityLiving ☃xxxxxx : ☃.getEntitiesWithinAABB(
         EntityLiving.class, new AxisAlignedBB(☃xxx - 7.0, ☃xxxx - 7.0, ☃xxxxx - 7.0, ☃xxx + 7.0, ☃xxxx + 7.0, ☃xxxxx + 7.0)
      )) {
         if (☃xxxxxx.getLeashed() && ☃xxxxxx.getLeashHolder() == ☃) {
            if (☃ == null) {
               ☃ = EntityLeashKnot.createKnot(☃, ☃);
            }

            ☃xxxxxx.setLeashHolder(☃, true);
            ☃x = true;
         }
      }

      return ☃x;
   }
}
