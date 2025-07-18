package net.minecraft.entity.ai;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2 {
   private int interactionDelay;
   private final EntityVillager villager;

   public EntityAIVillagerInteract(EntityVillager var1) {
      super(☃, EntityVillager.class, 3.0F, 0.02F);
      this.villager = ☃;
   }

   @Override
   public void startExecuting() {
      super.startExecuting();
      if (this.villager.canAbondonItems() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).wantsMoreFood()) {
         this.interactionDelay = 10;
      } else {
         this.interactionDelay = 0;
      }
   }

   @Override
   public void updateTask() {
      super.updateTask();
      if (this.interactionDelay > 0) {
         this.interactionDelay--;
         if (this.interactionDelay == 0) {
            InventoryBasic ☃ = this.villager.getVillagerInventory();

            for (int ☃x = 0; ☃x < ☃.getSizeInventory(); ☃x++) {
               ItemStack ☃xx = ☃.getStackInSlot(☃x);
               ItemStack ☃xxx = ItemStack.EMPTY;
               if (!☃xx.isEmpty()) {
                  Item ☃xxxx = ☃xx.getItem();
                  if ((☃xxxx == Items.BREAD || ☃xxxx == Items.POTATO || ☃xxxx == Items.CARROT || ☃xxxx == Items.BEETROOT) && ☃xx.getCount() > 3) {
                     int ☃xxxxx = ☃xx.getCount() / 2;
                     ☃xx.shrink(☃xxxxx);
                     ☃xxx = new ItemStack(☃xxxx, ☃xxxxx, ☃xx.getMetadata());
                  } else if (☃xxxx == Items.WHEAT && ☃xx.getCount() > 5) {
                     int ☃xxxxx = ☃xx.getCount() / 2 / 3 * 3;
                     int ☃xxxxxx = ☃xxxxx / 3;
                     ☃xx.shrink(☃xxxxx);
                     ☃xxx = new ItemStack(Items.BREAD, ☃xxxxxx, 0);
                  }

                  if (☃xx.isEmpty()) {
                     ☃.setInventorySlotContents(☃x, ItemStack.EMPTY);
                  }
               }

               if (!☃xxx.isEmpty()) {
                  double ☃xxxxx = this.villager.posY - 0.3F + this.villager.getEyeHeight();
                  EntityItem ☃xxxxxx = new EntityItem(this.villager.world, this.villager.posX, ☃xxxxx, this.villager.posZ, ☃xxx);
                  float ☃xxxxxxx = 0.3F;
                  float ☃xxxxxxxx = this.villager.rotationYawHead;
                  float ☃xxxxxxxxx = this.villager.rotationPitch;
                  ☃xxxxxx.motionX = -MathHelper.sin(☃xxxxxxxx * (float) (Math.PI / 180.0)) * MathHelper.cos(☃xxxxxxxxx * (float) (Math.PI / 180.0)) * 0.3F;
                  ☃xxxxxx.motionZ = MathHelper.cos(☃xxxxxxxx * (float) (Math.PI / 180.0)) * MathHelper.cos(☃xxxxxxxxx * (float) (Math.PI / 180.0)) * 0.3F;
                  ☃xxxxxx.motionY = -MathHelper.sin(☃xxxxxxxxx * (float) (Math.PI / 180.0)) * 0.3F + 0.1F;
                  ☃xxxxxx.setDefaultPickupDelay();
                  this.villager.world.spawnEntity(☃xxxxxx);
                  break;
               }
            }
         }
      }
   }
}
