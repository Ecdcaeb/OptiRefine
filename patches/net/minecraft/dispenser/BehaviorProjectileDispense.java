package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {
   @Override
   public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
      World ☃ = ☃.getWorld();
      IPosition ☃x = BlockDispenser.getDispensePosition(☃);
      EnumFacing ☃xx = ☃.getBlockState().getValue(BlockDispenser.FACING);
      IProjectile ☃xxx = this.getProjectileEntity(☃, ☃x, ☃);
      ☃xxx.shoot(☃xx.getXOffset(), ☃xx.getYOffset() + 0.1F, ☃xx.getZOffset(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
      ☃.spawnEntity((Entity)☃xxx);
      ☃.shrink(1);
      return ☃;
   }

   @Override
   protected void playDispenseSound(IBlockSource var1) {
      ☃.getWorld().playEvent(1002, ☃.getBlockPos(), 0);
   }

   protected abstract IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3);

   protected float getProjectileInaccuracy() {
      return 6.0F;
   }

   protected float getProjectileVelocity() {
      return 1.1F;
   }
}
