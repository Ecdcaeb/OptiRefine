package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDeadBush extends BlockBush {
   protected static final AxisAlignedBB DEAD_BUSH_AABB = new AxisAlignedBB(0.099999994F, 0.0, 0.099999994F, 0.9F, 0.8F, 0.9F);

   protected BlockDeadBush() {
      super(Material.VINE);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return DEAD_BUSH_AABB;
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.WOOD;
   }

   @Override
   protected boolean canSustainBush(IBlockState var1) {
      return ☃.getBlock() == Blocks.SAND || ☃.getBlock() == Blocks.HARDENED_CLAY || ☃.getBlock() == Blocks.STAINED_HARDENED_CLAY || ☃.getBlock() == Blocks.DIRT;
   }

   @Override
   public boolean isReplaceable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   @Override
   public int quantityDropped(Random var1) {
      return ☃.nextInt(3);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.STICK;
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      if (!☃.isRemote && ☃.getItem() == Items.SHEARS) {
         ☃.addStat(StatList.getBlockStats(this));
         spawnAsEntity(☃, ☃, new ItemStack(Blocks.DEADBUSH, 1, 0));
      } else {
         super.harvestBlock(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
