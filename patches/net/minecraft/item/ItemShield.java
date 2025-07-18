package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemShield extends Item {
   public ItemShield() {
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.COMBAT);
      this.setMaxDamage(336);
      this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
         @Override
         public float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
            return ☃ != null && ☃.isHandActive() && ☃.getActiveItemStack() == ☃ ? 1.0F : 0.0F;
         }
      });
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
   }

   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      if (☃.getSubCompound("BlockEntityTag") != null) {
         EnumDyeColor ☃ = TileEntityBanner.getColor(☃);
         return I18n.translateToLocal("item.shield." + ☃.getTranslationKey() + ".name");
      } else {
         return I18n.translateToLocal("item.shield.name");
      }
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      ItemBanner.appendHoverTextFromTileEntityTag(☃, ☃);
   }

   @Override
   public EnumAction getItemUseAction(ItemStack var1) {
      return EnumAction.BLOCK;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack var1) {
      return 72000;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      ☃.setActiveHand(☃);
      return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
   }

   @Override
   public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
      return ☃.getItem() == Item.getItemFromBlock(Blocks.PLANKS) ? true : super.getIsRepairable(☃, ☃);
   }
}
