package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnderChest extends BlockContainer {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   protected static final AxisAlignedBB ENDER_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);

   protected BlockEnderChest() {
      super(Material.ROCK);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ENDER_CHEST_AABB;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean hasCustomBreakingProgress(IBlockState var1) {
      return true;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.OBSIDIAN);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 8;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing().getOpposite());
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      ☃.setBlockState(☃, ☃.withProperty(FACING, ☃.getHorizontalFacing().getOpposite()), 2);
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      InventoryEnderChest ☃ = ☃.getInventoryEnderChest();
      TileEntity ☃x = ☃.getTileEntity(☃);
      if (☃ == null || !(☃x instanceof TileEntityEnderChest)) {
         return true;
      } else if (☃.getBlockState(☃.up()).isNormalCube()) {
         return true;
      } else if (☃.isRemote) {
         return true;
      } else {
         ☃.setChestTileEntity((TileEntityEnderChest)☃x);
         ☃.displayGUIChest(☃);
         ☃.addStat(StatList.ENDERCHEST_OPENED);
         return true;
      }
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityEnderChest();
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      for (int ☃ = 0; ☃ < 3; ☃++) {
         int ☃x = ☃.nextInt(2) * 2 - 1;
         int ☃xx = ☃.nextInt(2) * 2 - 1;
         double ☃xxx = ☃.getX() + 0.5 + 0.25 * ☃x;
         double ☃xxxx = ☃.getY() + ☃.nextFloat();
         double ☃xxxxx = ☃.getZ() + 0.5 + 0.25 * ☃xx;
         double ☃xxxxxx = ☃.nextFloat() * ☃x;
         double ☃xxxxxxx = (☃.nextFloat() - 0.5) * 0.125;
         double ☃xxxxxxxx = ☃.nextFloat() * ☃xx;
         ☃.spawnParticle(EnumParticleTypes.PORTAL, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      EnumFacing ☃ = EnumFacing.byIndex(☃);
      if (☃.getAxis() == EnumFacing.Axis.Y) {
         ☃ = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(FACING).getIndex();
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING)));
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
