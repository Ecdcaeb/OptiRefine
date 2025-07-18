package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock {
   private final EntityVillager villager;
   private boolean hasFarmItem;
   private boolean wantsToReapStuff;
   private int currentTask;

   public EntityAIHarvestFarmland(EntityVillager var1, double var2) {
      super(☃, ☃, 16);
      this.villager = ☃;
   }

   @Override
   public boolean shouldExecute() {
      if (this.runDelay <= 0) {
         if (!this.villager.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
         }

         this.currentTask = -1;
         this.hasFarmItem = this.villager.isFarmItemInInventory();
         this.wantsToReapStuff = this.villager.wantsMoreFood();
      }

      return super.shouldExecute();
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.currentTask >= 0 && super.shouldContinueExecuting();
   }

   @Override
   public void updateTask() {
      super.updateTask();
      this.villager
         .getLookHelper()
         .setLookPosition(
            this.destinationBlock.getX() + 0.5,
            this.destinationBlock.getY() + 1,
            this.destinationBlock.getZ() + 0.5,
            10.0F,
            this.villager.getVerticalFaceSpeed()
         );
      if (this.getIsAboveDestination()) {
         World ☃ = this.villager.world;
         BlockPos ☃x = this.destinationBlock.up();
         IBlockState ☃xx = ☃.getBlockState(☃x);
         Block ☃xxx = ☃xx.getBlock();
         if (this.currentTask == 0 && ☃xxx instanceof BlockCrops && ((BlockCrops)☃xxx).isMaxAge(☃xx)) {
            ☃.destroyBlock(☃x, true);
         } else if (this.currentTask == 1 && ☃xx.getMaterial() == Material.AIR) {
            InventoryBasic ☃xxxx = this.villager.getVillagerInventory();

            for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.getSizeInventory(); ☃xxxxx++) {
               ItemStack ☃xxxxxx = ☃xxxx.getStackInSlot(☃xxxxx);
               boolean ☃xxxxxxx = false;
               if (!☃xxxxxx.isEmpty()) {
                  if (☃xxxxxx.getItem() == Items.WHEAT_SEEDS) {
                     ☃.setBlockState(☃x, Blocks.WHEAT.getDefaultState(), 3);
                     ☃xxxxxxx = true;
                  } else if (☃xxxxxx.getItem() == Items.POTATO) {
                     ☃.setBlockState(☃x, Blocks.POTATOES.getDefaultState(), 3);
                     ☃xxxxxxx = true;
                  } else if (☃xxxxxx.getItem() == Items.CARROT) {
                     ☃.setBlockState(☃x, Blocks.CARROTS.getDefaultState(), 3);
                     ☃xxxxxxx = true;
                  } else if (☃xxxxxx.getItem() == Items.BEETROOT_SEEDS) {
                     ☃.setBlockState(☃x, Blocks.BEETROOTS.getDefaultState(), 3);
                     ☃xxxxxxx = true;
                  }
               }

               if (☃xxxxxxx) {
                  ☃xxxxxx.shrink(1);
                  if (☃xxxxxx.isEmpty()) {
                     ☃xxxx.setInventorySlotContents(☃xxxxx, ItemStack.EMPTY);
                  }
                  break;
               }
            }
         }

         this.currentTask = -1;
         this.runDelay = 10;
      }
   }

   @Override
   protected boolean shouldMoveTo(World var1, BlockPos var2) {
      Block ☃ = ☃.getBlockState(☃).getBlock();
      if (☃ == Blocks.FARMLAND) {
         ☃ = ☃.up();
         IBlockState ☃x = ☃.getBlockState(☃);
         ☃ = ☃x.getBlock();
         if (☃ instanceof BlockCrops && ((BlockCrops)☃).isMaxAge(☃x) && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0)) {
            this.currentTask = 0;
            return true;
         }

         if (☃x.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0)) {
            this.currentTask = 1;
            return true;
         }
      }

      return false;
   }
}
