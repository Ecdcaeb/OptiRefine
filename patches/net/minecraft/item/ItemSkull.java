package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class ItemSkull extends Item {
   private static final String[] SKULL_TYPES = new String[]{"skeleton", "wither", "zombie", "char", "creeper", "dragon"};

   public ItemSkull() {
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      if (☃ == EnumFacing.DOWN) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃);
         Block ☃x = ☃.getBlock();
         boolean ☃xx = ☃x.isReplaceable(☃, ☃);
         if (!☃xx) {
            if (!☃.getBlockState(☃).getMaterial().isSolid()) {
               return EnumActionResult.FAIL;
            }

            ☃ = ☃.offset(☃);
         }

         ItemStack ☃xxx = ☃.getHeldItem(☃);
         if (!☃.canPlayerEdit(☃, ☃, ☃xxx) || !Blocks.SKULL.canPlaceBlockAt(☃, ☃)) {
            return EnumActionResult.FAIL;
         } else if (☃.isRemote) {
            return EnumActionResult.SUCCESS;
         } else {
            ☃.setBlockState(☃, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, ☃), 11);
            int ☃xxxx = 0;
            if (☃ == EnumFacing.UP) {
               ☃xxxx = MathHelper.floor(☃.rotationYaw * 16.0F / 360.0F + 0.5) & 15;
            }

            TileEntity ☃xxxxx = ☃.getTileEntity(☃);
            if (☃xxxxx instanceof TileEntitySkull) {
               TileEntitySkull ☃xxxxxx = (TileEntitySkull)☃xxxxx;
               if (☃xxx.getMetadata() == 3) {
                  GameProfile ☃xxxxxxx = null;
                  if (☃xxx.hasTagCompound()) {
                     NBTTagCompound ☃xxxxxxxx = ☃xxx.getTagCompound();
                     if (☃xxxxxxxx.hasKey("SkullOwner", 10)) {
                        ☃xxxxxxx = NBTUtil.readGameProfileFromNBT(☃xxxxxxxx.getCompoundTag("SkullOwner"));
                     } else if (☃xxxxxxxx.hasKey("SkullOwner", 8) && !StringUtils.isBlank(☃xxxxxxxx.getString("SkullOwner"))) {
                        ☃xxxxxxx = new GameProfile(null, ☃xxxxxxxx.getString("SkullOwner"));
                     }
                  }

                  ☃xxxxxx.setPlayerProfile(☃xxxxxxx);
               } else {
                  ☃xxxxxx.setType(☃xxx.getMetadata());
               }

               ☃xxxxxx.setSkullRotation(☃xxxx);
               Blocks.SKULL.checkWitherSpawn(☃, ☃, ☃xxxxxx);
            }

            if (☃ instanceof EntityPlayerMP) {
               CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃xxx);
            }

            ☃xxx.shrink(1);
            return EnumActionResult.SUCCESS;
         }
      }
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (int ☃ = 0; ☃ < SKULL_TYPES.length; ☃++) {
            ☃.add(new ItemStack(this, 1, ☃));
         }
      }
   }

   @Override
   public int getMetadata(int var1) {
      return ☃;
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      int ☃ = ☃.getMetadata();
      if (☃ < 0 || ☃ >= SKULL_TYPES.length) {
         ☃ = 0;
      }

      return super.getTranslationKey() + "." + SKULL_TYPES[☃];
   }

   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      if (☃.getMetadata() == 3 && ☃.hasTagCompound()) {
         if (☃.getTagCompound().hasKey("SkullOwner", 8)) {
            return I18n.translateToLocalFormatted("item.skull.player.name", ☃.getTagCompound().getString("SkullOwner"));
         }

         if (☃.getTagCompound().hasKey("SkullOwner", 10)) {
            NBTTagCompound ☃ = ☃.getTagCompound().getCompoundTag("SkullOwner");
            if (☃.hasKey("Name", 8)) {
               return I18n.translateToLocalFormatted("item.skull.player.name", ☃.getString("Name"));
            }
         }
      }

      return super.getItemStackDisplayName(☃);
   }

   @Override
   public boolean updateItemStackNBT(NBTTagCompound var1) {
      super.updateItemStackNBT(☃);
      if (☃.hasKey("SkullOwner", 8) && !StringUtils.isBlank(☃.getString("SkullOwner"))) {
         GameProfile ☃ = new GameProfile(null, ☃.getString("SkullOwner"));
         ☃ = TileEntitySkull.updateGameProfile(☃);
         ☃.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), ☃));
         return true;
      } else {
         return false;
      }
   }
}
