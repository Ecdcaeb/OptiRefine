package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSit extends EntityAIMoveToBlock {
   private final EntityOcelot ocelot;

   public EntityAIOcelotSit(EntityOcelot var1, double var2) {
      super(☃, ☃, 8);
      this.ocelot = ☃;
   }

   @Override
   public boolean shouldExecute() {
      return this.ocelot.isTamed() && !this.ocelot.isSitting() && super.shouldExecute();
   }

   @Override
   public void startExecuting() {
      super.startExecuting();
      this.ocelot.getAISit().setSitting(false);
   }

   @Override
   public void resetTask() {
      super.resetTask();
      this.ocelot.setSitting(false);
   }

   @Override
   public void updateTask() {
      super.updateTask();
      this.ocelot.getAISit().setSitting(false);
      if (!this.getIsAboveDestination()) {
         this.ocelot.setSitting(false);
      } else if (!this.ocelot.isSitting()) {
         this.ocelot.setSitting(true);
      }
   }

   @Override
   protected boolean shouldMoveTo(World var1, BlockPos var2) {
      if (!☃.isAirBlock(☃.up())) {
         return false;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃);
         Block ☃x = ☃.getBlock();
         if (☃x == Blocks.CHEST) {
            TileEntity ☃xx = ☃.getTileEntity(☃);
            if (☃xx instanceof TileEntityChest && ((TileEntityChest)☃xx).numPlayersUsing < 1) {
               return true;
            }
         } else {
            if (☃x == Blocks.LIT_FURNACE) {
               return true;
            }

            if (☃x == Blocks.BED && ☃.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD) {
               return true;
            }
         }

         return false;
      }
   }
}
