package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWritableBook extends Item {
   public ItemWritableBook() {
      this.setMaxStackSize(1);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      ☃.openBook(☃, ☃);
      ☃.addStat(StatList.getObjectUseStats(this));
      return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
   }

   public static boolean isNBTValid(NBTTagCompound var0) {
      if (☃ == null) {
         return false;
      } else if (!☃.hasKey("pages", 9)) {
         return false;
      } else {
         NBTTagList ☃ = ☃.getTagList("pages", 8);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            String ☃xx = ☃.getStringTagAt(☃x);
            if (☃xx.length() > 32767) {
               return false;
            }
         }

         return true;
      }
   }
}
