package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem {
   @Override
   public final ItemStack dispense(IBlockSource var1, ItemStack var2) {
      ItemStack ☃ = this.dispenseStack(☃, ☃);
      this.playDispenseSound(☃);
      this.spawnDispenseParticles(☃, ☃.getBlockState().getValue(BlockDispenser.FACING));
      return ☃;
   }

   protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
      EnumFacing ☃ = ☃.getBlockState().getValue(BlockDispenser.FACING);
      IPosition ☃x = BlockDispenser.getDispensePosition(☃);
      ItemStack ☃xx = ☃.splitStack(1);
      doDispense(☃.getWorld(), ☃xx, 6, ☃, ☃x);
      return ☃;
   }

   public static void doDispense(World var0, ItemStack var1, int var2, EnumFacing var3, IPosition var4) {
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      if (☃.getAxis() == EnumFacing.Axis.Y) {
         ☃x -= 0.125;
      } else {
         ☃x -= 0.15625;
      }

      EntityItem ☃xxx = new EntityItem(☃, ☃, ☃x, ☃xx, ☃);
      double ☃xxxx = ☃.rand.nextDouble() * 0.1 + 0.2;
      ☃xxx.motionX = ☃.getXOffset() * ☃xxxx;
      ☃xxx.motionY = 0.2F;
      ☃xxx.motionZ = ☃.getZOffset() * ☃xxxx;
      ☃xxx.motionX = ☃xxx.motionX + ☃.rand.nextGaussian() * 0.0075F * ☃;
      ☃xxx.motionY = ☃xxx.motionY + ☃.rand.nextGaussian() * 0.0075F * ☃;
      ☃xxx.motionZ = ☃xxx.motionZ + ☃.rand.nextGaussian() * 0.0075F * ☃;
      ☃.spawnEntity(☃xxx);
   }

   protected void playDispenseSound(IBlockSource var1) {
      ☃.getWorld().playEvent(1000, ☃.getBlockPos(), 0);
   }

   protected void spawnDispenseParticles(IBlockSource var1, EnumFacing var2) {
      ☃.getWorld().playEvent(2000, ☃.getBlockPos(), this.getWorldEventDataFrom(☃));
   }

   private int getWorldEventDataFrom(EnumFacing var1) {
      return ☃.getXOffset() + 1 + (☃.getZOffset() + 1) * 3;
   }
}
