package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityEnderChest extends TileEntity implements ITickable {
   public float lidAngle;
   public float prevLidAngle;
   public int numPlayersUsing;
   private int ticksSinceSync;

   @Override
   public void update() {
      if (++this.ticksSinceSync % 20 * 4 == 0) {
         this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
      }

      this.prevLidAngle = this.lidAngle;
      int ☃ = this.pos.getX();
      int ☃x = this.pos.getY();
      int ☃xx = this.pos.getZ();
      float ☃xxx = 0.1F;
      if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
         double ☃xxxx = ☃ + 0.5;
         double ☃xxxxx = ☃xx + 0.5;
         this.world
            .playSound(null, ☃xxxx, ☃x + 0.5, ☃xxxxx, SoundEvents.BLOCK_ENDERCHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
      }

      if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
         float ☃xxxx = this.lidAngle;
         if (this.numPlayersUsing > 0) {
            this.lidAngle += 0.1F;
         } else {
            this.lidAngle -= 0.1F;
         }

         if (this.lidAngle > 1.0F) {
            this.lidAngle = 1.0F;
         }

         float ☃xxxxx = 0.5F;
         if (this.lidAngle < 0.5F && ☃xxxx >= 0.5F) {
            double ☃xxxxxx = ☃ + 0.5;
            double ☃xxxxxxx = ☃xx + 0.5;
            this.world
               .playSound(
                  null, ☃xxxxxx, ☃x + 0.5, ☃xxxxxxx, SoundEvents.BLOCK_ENDERCHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F
               );
         }

         if (this.lidAngle < 0.0F) {
            this.lidAngle = 0.0F;
         }
      }
   }

   @Override
   public boolean receiveClientEvent(int var1, int var2) {
      if (☃ == 1) {
         this.numPlayersUsing = ☃;
         return true;
      } else {
         return super.receiveClientEvent(☃, ☃);
      }
   }

   @Override
   public void invalidate() {
      this.updateContainingBlockInfo();
      super.invalidate();
   }

   public void openChest() {
      this.numPlayersUsing++;
      this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
   }

   public void closeChest() {
      this.numPlayersUsing--;
      this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
   }

   public boolean canBeUsed(EntityPlayer var1) {
      return this.world.getTileEntity(this.pos) != this
         ? false
         : !(☃.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) > 64.0);
   }
}
