package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemWrittenBook extends Item {
   public ItemWrittenBook() {
      this.setMaxStackSize(1);
   }

   public static boolean validBookTagContents(NBTTagCompound var0) {
      if (!ItemWritableBook.isNBTValid(☃)) {
         return false;
      } else if (!☃.hasKey("title", 8)) {
         return false;
      } else {
         String ☃ = ☃.getString("title");
         return ☃ != null && ☃.length() <= 32 ? ☃.hasKey("author", 8) : false;
      }
   }

   public static int getGeneration(ItemStack var0) {
      return ☃.getTagCompound().getInteger("generation");
   }

   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      if (☃.hasTagCompound()) {
         NBTTagCompound ☃ = ☃.getTagCompound();
         String ☃x = ☃.getString("title");
         if (!StringUtils.isNullOrEmpty(☃x)) {
            return ☃x;
         }
      }

      return super.getItemStackDisplayName(☃);
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      if (☃.hasTagCompound()) {
         NBTTagCompound ☃ = ☃.getTagCompound();
         String ☃x = ☃.getString("author");
         if (!StringUtils.isNullOrEmpty(☃x)) {
            ☃.add(TextFormatting.GRAY + I18n.translateToLocalFormatted("book.byAuthor", ☃x));
         }

         ☃.add(TextFormatting.GRAY + I18n.translateToLocal("book.generation." + ☃.getInteger("generation")));
      }
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.isRemote) {
         this.resolveContents(☃, ☃);
      }

      ☃.openBook(☃, ☃);
      ☃.addStat(StatList.getObjectUseStats(this));
      return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
   }

   private void resolveContents(ItemStack var1, EntityPlayer var2) {
      if (☃.getTagCompound() != null) {
         NBTTagCompound ☃ = ☃.getTagCompound();
         if (!☃.getBoolean("resolved")) {
            ☃.setBoolean("resolved", true);
            if (validBookTagContents(☃)) {
               NBTTagList ☃x = ☃.getTagList("pages", 8);

               for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
                  String ☃xxx = ☃x.getStringTagAt(☃xx);

                  ITextComponent ☃xxxx;
                  try {
                     ☃xxxx = ITextComponent.Serializer.fromJsonLenient(☃xxx);
                     ☃xxxx = TextComponentUtils.processComponent(☃, ☃xxxx, ☃);
                  } catch (Exception var9) {
                     ☃xxxx = new TextComponentString(☃xxx);
                  }

                  ☃x.set(☃xx, new NBTTagString(ITextComponent.Serializer.componentToJson(☃xxxx)));
               }

               ☃.setTag("pages", ☃x);
               if (☃ instanceof EntityPlayerMP && ☃.getHeldItemMainhand() == ☃) {
                  Slot ☃xx = ☃.openContainer.getSlotFromInventory(☃.inventory, ☃.inventory.currentItem);
                  ((EntityPlayerMP)☃).connection.sendPacket(new SPacketSetSlot(0, ☃xx.slotNumber, ☃));
               }
            }
         }
      }
   }

   @Override
   public boolean hasEffect(ItemStack var1) {
      return true;
   }
}
