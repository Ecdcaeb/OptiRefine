package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemElytra extends Item {
   public ItemElytra() {
      this.maxStackSize = 1;
      this.setMaxDamage(432);
      this.setCreativeTab(CreativeTabs.TRANSPORTATION);
      this.addPropertyOverride(new ResourceLocation("broken"), new IItemPropertyGetter() {
         @Override
         public float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
            return ItemElytra.isUsable(☃) ? 0.0F : 1.0F;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
   }

   public static boolean isUsable(ItemStack var0) {
      return ☃.getItemDamage() < ☃.getMaxDamage() - 1;
   }

   @Override
   public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
      return ☃.getItem() == Items.LEATHER;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      EntityEquipmentSlot ☃x = EntityLiving.getSlotForItemStack(☃);
      ItemStack ☃xx = ☃.getItemStackFromSlot(☃x);
      if (☃xx.isEmpty()) {
         ☃.setItemStackToSlot(☃x, ☃.copy());
         ☃.setCount(0);
         return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
      } else {
         return new ActionResult<>(EnumActionResult.FAIL, ☃);
      }
   }
}
