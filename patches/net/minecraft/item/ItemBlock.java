package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlock extends Item {
   protected final Block block;

   public ItemBlock(Block var1) {
      this.block = ☃;
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      if (!☃x.isReplaceable(☃, ☃)) {
         ☃ = ☃.offset(☃);
      }

      ItemStack ☃xx = ☃.getHeldItem(☃);
      if (!☃xx.isEmpty() && ☃.canPlayerEdit(☃, ☃, ☃xx) && ☃.mayPlace(this.block, ☃, false, ☃, null)) {
         int ☃xxx = this.getMetadata(☃xx.getMetadata());
         IBlockState ☃xxxx = this.block.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, ☃xxx, ☃);
         if (☃.setBlockState(☃, ☃xxxx, 11)) {
            ☃xxxx = ☃.getBlockState(☃);
            if (☃xxxx.getBlock() == this.block) {
               setTileEntityNBT(☃, ☃, ☃, ☃xx);
               this.block.onBlockPlacedBy(☃, ☃, ☃xxxx, ☃, ☃xx);
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃xx);
               }
            }

            SoundType ☃xxxxx = this.block.getSoundType();
            ☃.playSound(☃, ☃, ☃xxxxx.getPlaceSound(), SoundCategory.BLOCKS, (☃xxxxx.getVolume() + 1.0F) / 2.0F, ☃xxxxx.getPitch() * 0.8F);
            ☃xx.shrink(1);
         }

         return EnumActionResult.SUCCESS;
      } else {
         return EnumActionResult.FAIL;
      }
   }

   public static boolean setTileEntityNBT(World var0, @Nullable EntityPlayer var1, BlockPos var2, ItemStack var3) {
      MinecraftServer ☃ = ☃.getMinecraftServer();
      if (☃ == null) {
         return false;
      } else {
         NBTTagCompound ☃x = ☃.getSubCompound("BlockEntityTag");
         if (☃x != null) {
            TileEntity ☃xx = ☃.getTileEntity(☃);
            if (☃xx != null) {
               if (!☃.isRemote && ☃xx.onlyOpsCanSetNbt() && (☃ == null || !☃.canUseCommandBlock())) {
                  return false;
               }

               NBTTagCompound ☃xxx = ☃xx.writeToNBT(new NBTTagCompound());
               NBTTagCompound ☃xxxx = ☃xxx.copy();
               ☃xxx.merge(☃x);
               ☃xxx.setInteger("x", ☃.getX());
               ☃xxx.setInteger("y", ☃.getY());
               ☃xxx.setInteger("z", ☃.getZ());
               if (!☃xxx.equals(☃xxxx)) {
                  ☃xx.readFromNBT(☃xxx);
                  ☃xx.markDirty();
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3, EntityPlayer var4, ItemStack var5) {
      Block ☃ = ☃.getBlockState(☃).getBlock();
      if (☃ == Blocks.SNOW_LAYER) {
         ☃ = EnumFacing.UP;
      } else if (!☃.isReplaceable(☃, ☃)) {
         ☃ = ☃.offset(☃);
      }

      return ☃.mayPlace(this.block, ☃, false, ☃, null);
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return this.block.getTranslationKey();
   }

   @Override
   public String getTranslationKey() {
      return this.block.getTranslationKey();
   }

   @Override
   public CreativeTabs getCreativeTab() {
      return this.block.getCreativeTab();
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         this.block.getSubBlocks(☃, ☃);
      }
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      super.addInformation(☃, ☃, ☃, ☃);
      this.block.addInformation(☃, ☃, ☃, ☃);
   }

   public Block getBlock() {
      return this.block;
   }
}
