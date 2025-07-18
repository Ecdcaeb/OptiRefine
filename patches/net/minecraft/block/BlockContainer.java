package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

public abstract class BlockContainer extends Block implements ITileEntityProvider {
   protected BlockContainer(Material var1) {
      this(☃, ☃.getMaterialMapColor());
   }

   protected BlockContainer(Material var1, MapColor var2) {
      super(☃, ☃);
      this.hasTileEntity = true;
   }

   protected boolean isInvalidNeighbor(World var1, BlockPos var2, EnumFacing var3) {
      return ☃.getBlockState(☃.offset(☃)).getMaterial() == Material.CACTUS;
   }

   protected boolean hasInvalidNeighbor(World var1, BlockPos var2) {
      return this.isInvalidNeighbor(☃, ☃, EnumFacing.NORTH)
         || this.isInvalidNeighbor(☃, ☃, EnumFacing.SOUTH)
         || this.isInvalidNeighbor(☃, ☃, EnumFacing.WEST)
         || this.isInvalidNeighbor(☃, ☃, EnumFacing.EAST);
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.INVISIBLE;
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(☃, ☃, ☃);
      ☃.removeTileEntity(☃);
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      if (☃ instanceof IWorldNameable && ((IWorldNameable)☃).hasCustomName()) {
         ☃.addStat(StatList.getBlockStats(this));
         ☃.addExhaustion(0.005F);
         if (☃.isRemote) {
            return;
         }

         int ☃ = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, ☃);
         Item ☃x = this.getItemDropped(☃, ☃.rand, ☃);
         if (☃x == Items.AIR) {
            return;
         }

         ItemStack ☃xx = new ItemStack(☃x, this.quantityDropped(☃.rand));
         ☃xx.setStackDisplayName(((IWorldNameable)☃).getName());
         spawnAsEntity(☃, ☃, ☃xx);
      } else {
         super.harvestBlock(☃, ☃, ☃, ☃, null, ☃);
      }
   }

   @Override
   public boolean eventReceived(IBlockState var1, World var2, BlockPos var3, int var4, int var5) {
      super.eventReceived(☃, ☃, ☃, ☃, ☃);
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ == null ? false : ☃.receiveClientEvent(☃, ☃);
   }
}
