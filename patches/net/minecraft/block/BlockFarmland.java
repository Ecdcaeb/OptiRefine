package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFarmland extends Block {
   public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
   protected static final AxisAlignedBB FARMLAND_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.9375, 1.0);
   protected static final AxisAlignedBB field_194405_c = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);

   protected BlockFarmland() {
      super(Material.GROUND);
      this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, 0));
      this.setTickRandomly(true);
      this.setLightOpacity(255);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return FARMLAND_AABB;
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
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      int ☃ = ☃.getValue(MOISTURE);
      if (!this.hasWater(☃, ☃) && !☃.isRainingAt(☃.up())) {
         if (☃ > 0) {
            ☃.setBlockState(☃, ☃.withProperty(MOISTURE, ☃ - 1), 2);
         } else if (!this.hasCrops(☃, ☃)) {
            turnToDirt(☃, ☃);
         }
      } else if (☃ < 7) {
         ☃.setBlockState(☃, ☃.withProperty(MOISTURE, 7), 2);
      }
   }

   @Override
   public void onFallenUpon(World var1, BlockPos var2, Entity var3, float var4) {
      if (!☃.isRemote
         && ☃.rand.nextFloat() < ☃ - 0.5F
         && ☃ instanceof EntityLivingBase
         && (☃ instanceof EntityPlayer || ☃.getGameRules().getBoolean("mobGriefing"))
         && ☃.width * ☃.width * ☃.height > 0.512F) {
         turnToDirt(☃, ☃);
      }

      super.onFallenUpon(☃, ☃, ☃, ☃);
   }

   protected static void turnToDirt(World var0, BlockPos var1) {
      ☃.setBlockState(☃, Blocks.DIRT.getDefaultState());
      AxisAlignedBB ☃ = field_194405_c.offset(☃);

      for (Entity ☃x : ☃.getEntitiesWithinAABBExcludingEntity(null, ☃)) {
         double ☃xx = Math.min(☃.maxY - ☃.minY, ☃.maxY - ☃x.getEntityBoundingBox().minY);
         ☃x.setPositionAndUpdate(☃x.posX, ☃x.posY + ☃xx + 0.001, ☃x.posZ);
      }
   }

   private boolean hasCrops(World var1, BlockPos var2) {
      Block ☃ = ☃.getBlockState(☃.up()).getBlock();
      return ☃ instanceof BlockCrops || ☃ instanceof BlockStem;
   }

   private boolean hasWater(World var1, BlockPos var2) {
      for (BlockPos.MutableBlockPos ☃ : BlockPos.getAllInBoxMutable(☃.add(-4, 0, -4), ☃.add(4, 1, 4))) {
         if (☃.getBlockState(☃).getMaterial() == Material.WATER) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      super.neighborChanged(☃, ☃, ☃, ☃, ☃);
      if (☃.getBlockState(☃.up()).getMaterial().isSolid()) {
         turnToDirt(☃, ☃);
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(☃, ☃, ☃);
      if (☃.getBlockState(☃.up()).getMaterial().isSolid()) {
         turnToDirt(☃, ☃);
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      switch (☃) {
         case UP:
            return true;
         case NORTH:
         case SOUTH:
         case WEST:
         case EAST:
            IBlockState ☃ = ☃.getBlockState(☃.offset(☃));
            Block ☃x = ☃.getBlock();
            return !☃.isOpaqueCube() && ☃x != Blocks.FARMLAND && ☃x != Blocks.GRASS_PATH;
         default:
            return super.shouldSideBeRendered(☃, ☃, ☃, ☃);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), ☃, ☃);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(MOISTURE, ☃ & 7);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(MOISTURE);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, MOISTURE);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
