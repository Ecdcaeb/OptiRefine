package net.minecraft.block;

import com.google.common.base.MoreObjects;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook extends Block {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyBool ATTACHED = PropertyBool.create("attached");
   protected static final AxisAlignedBB HOOK_NORTH_AABB = new AxisAlignedBB(0.3125, 0.0, 0.625, 0.6875, 0.625, 1.0);
   protected static final AxisAlignedBB HOOK_SOUTH_AABB = new AxisAlignedBB(0.3125, 0.0, 0.0, 0.6875, 0.625, 0.375);
   protected static final AxisAlignedBB HOOK_WEST_AABB = new AxisAlignedBB(0.625, 0.0, 0.3125, 1.0, 0.625, 0.6875);
   protected static final AxisAlignedBB HOOK_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.3125, 0.375, 0.625, 0.6875);

   public BlockTripWireHook() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false).withProperty(ATTACHED, false));
      this.setCreativeTab(CreativeTabs.REDSTONE);
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((EnumFacing)☃.getValue(FACING)) {
         case EAST:
         default:
            return HOOK_EAST_AABB;
         case WEST:
            return HOOK_WEST_AABB;
         case SOUTH:
            return HOOK_SOUTH_AABB;
         case NORTH:
            return HOOK_NORTH_AABB;
      }
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
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
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      EnumFacing ☃ = ☃.getOpposite();
      BlockPos ☃x = ☃.offset(☃);
      IBlockState ☃xx = ☃.getBlockState(☃x);
      boolean ☃xxx = isExceptBlockForAttachWithPiston(☃xx.getBlock());
      return !☃xxx && ☃.getAxis().isHorizontal() && ☃xx.getBlockFaceShape(☃, ☃x, ☃) == BlockFaceShape.SOLID && !☃xx.canProvidePower();
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
         if (this.canPlaceBlockOnSide(☃, ☃, ☃)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState ☃ = this.getDefaultState().withProperty(POWERED, false).withProperty(ATTACHED, false);
      if (☃.getAxis().isHorizontal()) {
         ☃ = ☃.withProperty(FACING, ☃);
      }

      return ☃;
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      this.calculateState(☃, ☃, ☃, false, false, -1, null);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (☃ != this) {
         if (this.checkForDrop(☃, ☃, ☃)) {
            EnumFacing ☃ = ☃.getValue(FACING);
            if (!this.canPlaceBlockOnSide(☃, ☃, ☃)) {
               this.dropBlockAsItem(☃, ☃, ☃, 0);
               ☃.setBlockToAir(☃);
            }
         }
      }
   }

   public void calculateState(World var1, BlockPos var2, IBlockState var3, boolean var4, boolean var5, int var6, @Nullable IBlockState var7) {
      EnumFacing ☃ = ☃.getValue(FACING);
      boolean ☃x = ☃.getValue(ATTACHED);
      boolean ☃xx = ☃.getValue(POWERED);
      boolean ☃xxx = !☃;
      boolean ☃xxxx = false;
      int ☃xxxxx = 0;
      IBlockState[] ☃xxxxxx = new IBlockState[42];

      for (int ☃xxxxxxx = 1; ☃xxxxxxx < 42; ☃xxxxxxx++) {
         BlockPos ☃xxxxxxxx = ☃.offset(☃, ☃xxxxxxx);
         IBlockState ☃xxxxxxxxx = ☃.getBlockState(☃xxxxxxxx);
         if (☃xxxxxxxxx.getBlock() == Blocks.TRIPWIRE_HOOK) {
            if (☃xxxxxxxxx.getValue(FACING) == ☃.getOpposite()) {
               ☃xxxxx = ☃xxxxxxx;
            }
            break;
         }

         if (☃xxxxxxxxx.getBlock() != Blocks.TRIPWIRE && ☃xxxxxxx != ☃) {
            ☃xxxxxx[☃xxxxxxx] = null;
            ☃xxx = false;
         } else {
            if (☃xxxxxxx == ☃) {
               ☃xxxxxxxxx = (IBlockState)MoreObjects.firstNonNull(☃, ☃xxxxxxxxx);
            }

            boolean ☃xxxxxxxxxx = !☃xxxxxxxxx.getValue(BlockTripWire.DISARMED);
            boolean ☃xxxxxxxxxxx = ☃xxxxxxxxx.getValue(BlockTripWire.POWERED);
            ☃xxxx |= ☃xxxxxxxxxx && ☃xxxxxxxxxxx;
            ☃xxxxxx[☃xxxxxxx] = ☃xxxxxxxxx;
            if (☃xxxxxxx == ☃) {
               ☃.scheduleUpdate(☃, this, this.tickRate(☃));
               ☃xxx &= ☃xxxxxxxxxx;
            }
         }
      }

      ☃xxx &= ☃xxxxx > 1;
      ☃xxxx &= ☃xxx;
      IBlockState ☃xxxxxxx = this.getDefaultState().withProperty(ATTACHED, ☃xxx).withProperty(POWERED, ☃xxxx);
      if (☃xxxxx > 0) {
         BlockPos ☃xxxxxxxxxx = ☃.offset(☃, ☃xxxxx);
         EnumFacing ☃xxxxxxxxxxx = ☃.getOpposite();
         ☃.setBlockState(☃xxxxxxxxxx, ☃xxxxxxx.withProperty(FACING, ☃xxxxxxxxxxx), 3);
         this.notifyNeighbors(☃, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
         this.playSound(☃, ☃xxxxxxxxxx, ☃xxx, ☃xxxx, ☃x, ☃xx);
      }

      this.playSound(☃, ☃, ☃xxx, ☃xxxx, ☃x, ☃xx);
      if (!☃) {
         ☃.setBlockState(☃, ☃xxxxxxx.withProperty(FACING, ☃), 3);
         if (☃) {
            this.notifyNeighbors(☃, ☃, ☃);
         }
      }

      if (☃x != ☃xxx) {
         for (int ☃xxxxxxxxxx = 1; ☃xxxxxxxxxx < ☃xxxxx; ☃xxxxxxxxxx++) {
            BlockPos ☃xxxxxxxxxxx = ☃.offset(☃, ☃xxxxxxxxxx);
            IBlockState ☃xxxxxxxxxxxx = ☃xxxxxx[☃xxxxxxxxxx];
            if (☃xxxxxxxxxxxx != null && ☃.getBlockState(☃xxxxxxxxxxx).getMaterial() != Material.AIR) {
               ☃.setBlockState(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx.withProperty(ATTACHED, ☃xxx), 3);
            }
         }
      }
   }

   @Override
   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      this.calculateState(☃, ☃, ☃, false, true, -1, null);
   }

   private void playSound(World var1, BlockPos var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      if (☃ && !☃) {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.BLOCKS, 0.4F, 0.6F);
      } else if (!☃ && ☃) {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.BLOCKS, 0.4F, 0.5F);
      } else if (☃ && !☃) {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_TRIPWIRE_ATTACH, SoundCategory.BLOCKS, 0.4F, 0.7F);
      } else if (!☃ && ☃) {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_TRIPWIRE_DETACH, SoundCategory.BLOCKS, 0.4F, 1.2F / (☃.rand.nextFloat() * 0.2F + 0.9F));
      }
   }

   private void notifyNeighbors(World var1, BlockPos var2, EnumFacing var3) {
      ☃.notifyNeighborsOfStateChange(☃, this, false);
      ☃.notifyNeighborsOfStateChange(☃.offset(☃.getOpposite()), this, false);
   }

   private boolean checkForDrop(World var1, BlockPos var2, IBlockState var3) {
      if (!this.canPlaceBlockAt(☃, ☃)) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
         return false;
      } else {
         return true;
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      boolean ☃ = ☃.getValue(ATTACHED);
      boolean ☃x = ☃.getValue(POWERED);
      if (☃ || ☃x) {
         this.calculateState(☃, ☃, ☃, true, false, -1, null);
      }

      if (☃x) {
         ☃.notifyNeighborsOfStateChange(☃, this, false);
         ☃.notifyNeighborsOfStateChange(☃.offset(☃.getValue(FACING).getOpposite()), this, false);
      }

      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getValue(POWERED) ? 15 : 0;
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (!☃.getValue(POWERED)) {
         return 0;
      } else {
         return ☃.getValue(FACING) == ☃ ? 15 : 0;
      }
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT_MIPPED;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState()
         .withProperty(FACING, EnumFacing.byHorizontalIndex(☃ & 3))
         .withProperty(POWERED, (☃ & 8) > 0)
         .withProperty(ATTACHED, (☃ & 4) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getHorizontalIndex();
      if (☃.getValue(POWERED)) {
         ☃ |= 8;
      }

      if (☃.getValue(ATTACHED)) {
         ☃ |= 4;
      }

      return ☃;
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
      return new BlockStateContainer(this, FACING, POWERED, ATTACHED);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
