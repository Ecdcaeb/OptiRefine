package net.minecraft.block;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDropper extends BlockDispenser {
   private final IBehaviorDispenseItem dropBehavior = new BehaviorDefaultDispenseItem();

   @Override
   protected IBehaviorDispenseItem getBehavior(ItemStack var1) {
      return this.dropBehavior;
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityDropper();
   }

   @Override
   protected void dispense(World var1, BlockPos var2) {
      BlockSourceImpl ☃ = new BlockSourceImpl(☃, ☃);
      TileEntityDispenser ☃x = ☃.getBlockTileEntity();
      if (☃x != null) {
         int ☃xx = ☃x.getDispenseSlot();
         if (☃xx < 0) {
            ☃.playEvent(1001, ☃, 0);
         } else {
            ItemStack ☃xxx = ☃x.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               EnumFacing ☃xxxx = ☃.getBlockState(☃).getValue(FACING);
               BlockPos ☃xxxxx = ☃.offset(☃xxxx);
               IInventory ☃xxxxxx = TileEntityHopper.getInventoryAtPosition(☃, ☃xxxxx.getX(), ☃xxxxx.getY(), ☃xxxxx.getZ());
               ItemStack ☃xxxxxxx;
               if (☃xxxxxx == null) {
                  ☃xxxxxxx = this.dropBehavior.dispense(☃, ☃xxx);
               } else {
                  ☃xxxxxxx = TileEntityHopper.putStackInInventoryAllSlots(☃x, ☃xxxxxx, ☃xxx.copy().splitStack(1), ☃xxxx.getOpposite());
                  if (☃xxxxxxx.isEmpty()) {
                     ☃xxxxxxx = ☃xxx.copy();
                     ☃xxxxxxx.shrink(1);
                  } else {
                     ☃xxxxxxx = ☃xxx.copy();
                  }
               }

               ☃x.setInventorySlotContents(☃xx, ☃xxxxxxx);
            }
         }
      }
   }
}
