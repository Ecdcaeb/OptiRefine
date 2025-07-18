package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMooshroom extends EntityCow {
   public EntityMooshroom(World var1) {
      super(☃);
      this.setSize(0.9F, 1.4F);
      this.spawnableBlock = Blocks.MYCELIUM;
   }

   public static void registerFixesMooshroom(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityMooshroom.class);
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.BOWL && this.getGrowingAge() >= 0 && !☃.capabilities.isCreativeMode) {
         ☃.shrink(1);
         if (☃.isEmpty()) {
            ☃.setHeldItem(☃, new ItemStack(Items.MUSHROOM_STEW));
         } else if (!☃.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW))) {
            ☃.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
         }

         return true;
      } else if (☃.getItem() == Items.SHEARS && this.getGrowingAge() >= 0) {
         this.setDead();
         this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + this.height / 2.0F, this.posZ, 0.0, 0.0, 0.0);
         if (!this.world.isRemote) {
            EntityCow ☃x = new EntityCow(this.world);
            ☃x.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            ☃x.setHealth(this.getHealth());
            ☃x.renderYawOffset = this.renderYawOffset;
            if (this.hasCustomName()) {
               ☃x.setCustomNameTag(this.getCustomNameTag());
            }

            this.world.spawnEntity(☃x);

            for (int ☃xx = 0; ☃xx < 5; ☃xx++) {
               this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY + this.height, this.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
            }

            ☃.damageItem(1, ☃);
            this.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);
         }

         return true;
      } else {
         return super.processInteract(☃, ☃);
      }
   }

   public EntityMooshroom createChild(EntityAgeable var1) {
      return new EntityMooshroom(this.world);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_MUSHROOM_COW;
   }
}
