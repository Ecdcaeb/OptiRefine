package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFurnace extends BlockContainer {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   private final boolean isBurning;
   private static boolean keepInventory;

   protected BlockFurnace(boolean var1) {
      super(Material.ROCK);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.isBurning = ☃;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.FURNACE);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.setDefaultFacing(☃, ☃, ☃);
   }

   private void setDefaultFacing(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         IBlockState ☃ = ☃.getBlockState(☃.north());
         IBlockState ☃x = ☃.getBlockState(☃.south());
         IBlockState ☃xx = ☃.getBlockState(☃.west());
         IBlockState ☃xxx = ☃.getBlockState(☃.east());
         EnumFacing ☃xxxx = ☃.getValue(FACING);
         if (☃xxxx == EnumFacing.NORTH && ☃.isFullBlock() && !☃x.isFullBlock()) {
            ☃xxxx = EnumFacing.SOUTH;
         } else if (☃xxxx == EnumFacing.SOUTH && ☃x.isFullBlock() && !☃.isFullBlock()) {
            ☃xxxx = EnumFacing.NORTH;
         } else if (☃xxxx == EnumFacing.WEST && ☃xx.isFullBlock() && !☃xxx.isFullBlock()) {
            ☃xxxx = EnumFacing.EAST;
         } else if (☃xxxx == EnumFacing.EAST && ☃xxx.isFullBlock() && !☃xx.isFullBlock()) {
            ☃xxxx = EnumFacing.WEST;
         }

         ☃.setBlockState(☃, ☃.withProperty(FACING, ☃xxxx), 2);
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (this.isBurning) {
         EnumFacing ☃ = ☃.getValue(FACING);
         double ☃x = ☃.getX() + 0.5;
         double ☃xx = ☃.getY() + ☃.nextDouble() * 6.0 / 16.0;
         double ☃xxx = ☃.getZ() + 0.5;
         double ☃xxxx = 0.52;
         double ☃xxxxx = ☃.nextDouble() * 0.6 - 0.3;
         if (☃.nextDouble() < 0.1) {
            ☃.playSound(☃.getX() + 0.5, ☃.getY(), ☃.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
         }

         switch (☃) {
            case WEST:
               ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃x - 0.52, ☃xx, ☃xxx + ☃xxxxx, 0.0, 0.0, 0.0);
               ☃.spawnParticle(EnumParticleTypes.FLAME, ☃x - 0.52, ☃xx, ☃xxx + ☃xxxxx, 0.0, 0.0, 0.0);
               break;
            case EAST:
               ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃x + 0.52, ☃xx, ☃xxx + ☃xxxxx, 0.0, 0.0, 0.0);
               ☃.spawnParticle(EnumParticleTypes.FLAME, ☃x + 0.52, ☃xx, ☃xxx + ☃xxxxx, 0.0, 0.0, 0.0);
               break;
            case NORTH:
               ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃x + ☃xxxxx, ☃xx, ☃xxx - 0.52, 0.0, 0.0, 0.0);
               ☃.spawnParticle(EnumParticleTypes.FLAME, ☃x + ☃xxxxx, ☃xx, ☃xxx - 0.52, 0.0, 0.0, 0.0);
               break;
            case SOUTH:
               ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃x + ☃xxxxx, ☃xx, ☃xxx + 0.52, 0.0, 0.0, 0.0);
               ☃.spawnParticle(EnumParticleTypes.FLAME, ☃x + ☃xxxxx, ☃xx, ☃xxx + 0.52, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityFurnace) {
            ☃.displayGUIChest((TileEntityFurnace)☃);
            ☃.addStat(StatList.FURNACE_INTERACTION);
         }

         return true;
      }
   }

   public static void setState(boolean var0, World var1, BlockPos var2) {
      IBlockState ☃ = ☃.getBlockState(☃);
      TileEntity ☃x = ☃.getTileEntity(☃);
      keepInventory = true;
      if (☃) {
         ☃.setBlockState(☃, Blocks.LIT_FURNACE.getDefaultState().withProperty(FACING, ☃.getValue(FACING)), 3);
         ☃.setBlockState(☃, Blocks.LIT_FURNACE.getDefaultState().withProperty(FACING, ☃.getValue(FACING)), 3);
      } else {
         ☃.setBlockState(☃, Blocks.FURNACE.getDefaultState().withProperty(FACING, ☃.getValue(FACING)), 3);
         ☃.setBlockState(☃, Blocks.FURNACE.getDefaultState().withProperty(FACING, ☃.getValue(FACING)), 3);
      }

      keepInventory = false;
      if (☃x != null) {
         ☃x.validate();
         ☃.setTileEntity(☃, ☃x);
      }
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityFurnace();
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing().getOpposite());
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      ☃.setBlockState(☃, ☃.withProperty(FACING, ☃.getHorizontalFacing().getOpposite()), 2);
      if (☃.hasDisplayName()) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityFurnace) {
            ((TileEntityFurnace)☃).setCustomInventoryName(☃.getDisplayName());
         }
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!keepInventory) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityFurnace) {
            InventoryHelper.dropInventoryItems(☃, ☃, (TileEntityFurnace)☃);
            ☃.updateComparatorOutputLevel(☃, this);
         }
      }

      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      return Container.calcRedstone(☃.getTileEntity(☃));
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Blocks.FURNACE);
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
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
}
