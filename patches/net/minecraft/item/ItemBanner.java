package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemBanner extends ItemBlock {
   public ItemBanner() {
      super(Blocks.STANDING_BANNER);
      this.maxStackSize = 16;
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      boolean ☃x = ☃.getBlock().isReplaceable(☃, ☃);
      if (☃ != EnumFacing.DOWN && (☃.getMaterial().isSolid() || ☃x) && (!☃x || ☃ == EnumFacing.UP)) {
         ☃ = ☃.offset(☃);
         ItemStack ☃xx = ☃.getHeldItem(☃);
         if (!☃.canPlayerEdit(☃, ☃, ☃xx) || !Blocks.STANDING_BANNER.canPlaceBlockAt(☃, ☃)) {
            return EnumActionResult.FAIL;
         } else if (☃.isRemote) {
            return EnumActionResult.SUCCESS;
         } else {
            ☃ = ☃x ? ☃.down() : ☃;
            if (☃ == EnumFacing.UP) {
               int ☃xxx = MathHelper.floor((☃.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5) & 15;
               ☃.setBlockState(☃, Blocks.STANDING_BANNER.getDefaultState().withProperty(BlockStandingSign.ROTATION, ☃xxx), 3);
            } else {
               ☃.setBlockState(☃, Blocks.WALL_BANNER.getDefaultState().withProperty(BlockWallSign.FACING, ☃), 3);
            }

            TileEntity ☃xxx = ☃.getTileEntity(☃);
            if (☃xxx instanceof TileEntityBanner) {
               ((TileEntityBanner)☃xxx).setItemValues(☃xx, false);
            }

            if (☃ instanceof EntityPlayerMP) {
               CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃xx);
            }

            ☃xx.shrink(1);
            return EnumActionResult.SUCCESS;
         }
      } else {
         return EnumActionResult.FAIL;
      }
   }

   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      String ☃ = "item.banner.";
      EnumDyeColor ☃x = getBaseColor(☃);
      ☃ = ☃ + ☃x.getTranslationKey() + ".name";
      return I18n.translateToLocal(☃);
   }

   public static void appendHoverTextFromTileEntityTag(ItemStack var0, List<String> var1) {
      NBTTagCompound ☃ = ☃.getSubCompound("BlockEntityTag");
      if (☃ != null && ☃.hasKey("Patterns")) {
         NBTTagList ☃x = ☃.getTagList("Patterns", 10);

         for (int ☃xx = 0; ☃xx < ☃x.tagCount() && ☃xx < 6; ☃xx++) {
            NBTTagCompound ☃xxx = ☃x.getCompoundTagAt(☃xx);
            EnumDyeColor ☃xxxx = EnumDyeColor.byDyeDamage(☃xxx.getInteger("Color"));
            BannerPattern ☃xxxxx = BannerPattern.byHash(☃xxx.getString("Pattern"));
            if (☃xxxxx != null) {
               ☃.add(I18n.translateToLocal("item.banner." + ☃xxxxx.getFileName() + "." + ☃xxxx.getTranslationKey()));
            }
         }
      }
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      appendHoverTextFromTileEntityTag(☃, ☃);
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (EnumDyeColor ☃ : EnumDyeColor.values()) {
            ☃.add(makeBanner(☃, null));
         }
      }
   }

   public static ItemStack makeBanner(EnumDyeColor var0, @Nullable NBTTagList var1) {
      ItemStack ☃ = new ItemStack(Items.BANNER, 1, ☃.getDyeDamage());
      if (☃ != null && !☃.isEmpty()) {
         ☃.getOrCreateSubCompound("BlockEntityTag").setTag("Patterns", ☃.copy());
      }

      return ☃;
   }

   @Override
   public CreativeTabs getCreativeTab() {
      return CreativeTabs.DECORATIONS;
   }

   public static EnumDyeColor getBaseColor(ItemStack var0) {
      return EnumDyeColor.byDyeDamage(☃.getMetadata() & 15);
   }
}
