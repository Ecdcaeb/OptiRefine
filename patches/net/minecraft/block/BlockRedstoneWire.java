package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block {
   public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> NORTH = PropertyEnum.create("north", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> EAST = PropertyEnum.create("east", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> SOUTH = PropertyEnum.create("south", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> WEST = PropertyEnum.create("west", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
   protected static final AxisAlignedBB[] REDSTONE_WIRE_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.0625, 0.8125),
      new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.0625, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.1875, 0.8125, 0.0625, 0.8125),
      new AxisAlignedBB(0.0, 0.0, 0.1875, 0.8125, 0.0625, 1.0),
      new AxisAlignedBB(0.1875, 0.0, 0.0, 0.8125, 0.0625, 0.8125),
      new AxisAlignedBB(0.1875, 0.0, 0.0, 0.8125, 0.0625, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.8125, 0.0625, 0.8125),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.8125, 0.0625, 1.0),
      new AxisAlignedBB(0.1875, 0.0, 0.1875, 1.0, 0.0625, 0.8125),
      new AxisAlignedBB(0.1875, 0.0, 0.1875, 1.0, 0.0625, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.1875, 1.0, 0.0625, 0.8125),
      new AxisAlignedBB(0.0, 0.0, 0.1875, 1.0, 0.0625, 1.0),
      new AxisAlignedBB(0.1875, 0.0, 0.0, 1.0, 0.0625, 0.8125),
      new AxisAlignedBB(0.1875, 0.0, 0.0, 1.0, 0.0625, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 0.8125),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0)
   };
   private boolean canProvidePower = true;
   private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

   public BlockRedstoneWire() {
      super(Material.CIRCUITS);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE)
            .withProperty(EAST, BlockRedstoneWire.EnumAttachPosition.NONE)
            .withProperty(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE)
            .withProperty(WEST, BlockRedstoneWire.EnumAttachPosition.NONE)
            .withProperty(POWER, 0)
      );
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return REDSTONE_WIRE_AABB[getAABBIndex(☃.getActualState(☃, ☃))];
   }

   private static int getAABBIndex(IBlockState var0) {
      int ☃ = 0;
      boolean ☃x = ☃.getValue(NORTH) != BlockRedstoneWire.EnumAttachPosition.NONE;
      boolean ☃xx = ☃.getValue(EAST) != BlockRedstoneWire.EnumAttachPosition.NONE;
      boolean ☃xxx = ☃.getValue(SOUTH) != BlockRedstoneWire.EnumAttachPosition.NONE;
      boolean ☃xxxx = ☃.getValue(WEST) != BlockRedstoneWire.EnumAttachPosition.NONE;
      if (☃x || ☃xxx && !☃x && !☃xx && !☃xxxx) {
         ☃ |= 1 << EnumFacing.NORTH.getHorizontalIndex();
      }

      if (☃xx || ☃xxxx && !☃x && !☃xx && !☃xxx) {
         ☃ |= 1 << EnumFacing.EAST.getHorizontalIndex();
      }

      if (☃xxx || ☃x && !☃xx && !☃xxx && !☃xxxx) {
         ☃ |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
      }

      if (☃xxxx || ☃xx && !☃x && !☃xxx && !☃xxxx) {
         ☃ |= 1 << EnumFacing.WEST.getHorizontalIndex();
      }

      return ☃;
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = ☃.withProperty(WEST, this.getAttachPosition(☃, ☃, EnumFacing.WEST));
      ☃ = ☃.withProperty(EAST, this.getAttachPosition(☃, ☃, EnumFacing.EAST));
      ☃ = ☃.withProperty(NORTH, this.getAttachPosition(☃, ☃, EnumFacing.NORTH));
      return ☃.withProperty(SOUTH, this.getAttachPosition(☃, ☃, EnumFacing.SOUTH));
   }

   private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      BlockPos ☃ = ☃.offset(☃);
      IBlockState ☃x = ☃.getBlockState(☃.offset(☃));
      if (!canConnectTo(☃.getBlockState(☃), ☃) && (☃x.isNormalCube() || !canConnectUpwardsTo(☃.getBlockState(☃.down())))) {
         IBlockState ☃xx = ☃.getBlockState(☃.up());
         if (!☃xx.isNormalCube()) {
            boolean ☃xxx = ☃.getBlockState(☃).isTopSolid() || ☃.getBlockState(☃).getBlock() == Blocks.GLOWSTONE;
            if (☃xxx && canConnectUpwardsTo(☃.getBlockState(☃.up()))) {
               if (☃x.isBlockNormalCube()) {
                  return BlockRedstoneWire.EnumAttachPosition.UP;
               }

               return BlockRedstoneWire.EnumAttachPosition.SIDE;
            }
         }

         return BlockRedstoneWire.EnumAttachPosition.NONE;
      } else {
         return BlockRedstoneWire.EnumAttachPosition.SIDE;
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
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.down()).isTopSolid() || ☃.getBlockState(☃.down()).getBlock() == Blocks.GLOWSTONE;
   }

   private IBlockState updateSurroundingRedstone(World var1, BlockPos var2, IBlockState var3) {
      ☃ = this.calculateCurrentChanges(☃, ☃, ☃, ☃);
      List<BlockPos> ☃ = Lists.newArrayList(this.blocksNeedingUpdate);
      this.blocksNeedingUpdate.clear();

      for (BlockPos ☃x : ☃) {
         ☃.notifyNeighborsOfStateChange(☃x, this, false);
      }

      return ☃;
   }

   private IBlockState calculateCurrentChanges(World var1, BlockPos var2, BlockPos var3, IBlockState var4) {
      IBlockState ☃ = ☃;
      int ☃x = ☃.getValue(POWER);
      int ☃xx = 0;
      ☃xx = this.getMaxCurrentStrength(☃, ☃, ☃xx);
      this.canProvidePower = false;
      int ☃xxx = ☃.getRedstonePowerFromNeighbors(☃);
      this.canProvidePower = true;
      if (☃xxx > 0 && ☃xxx > ☃xx - 1) {
         ☃xx = ☃xxx;
      }

      int ☃xxxx = 0;

      for (EnumFacing ☃xxxxx : EnumFacing.Plane.HORIZONTAL) {
         BlockPos ☃xxxxxx = ☃.offset(☃xxxxx);
         boolean ☃xxxxxxx = ☃xxxxxx.getX() != ☃.getX() || ☃xxxxxx.getZ() != ☃.getZ();
         if (☃xxxxxxx) {
            ☃xxxx = this.getMaxCurrentStrength(☃, ☃xxxxxx, ☃xxxx);
         }

         if (☃.getBlockState(☃xxxxxx).isNormalCube() && !☃.getBlockState(☃.up()).isNormalCube()) {
            if (☃xxxxxxx && ☃.getY() >= ☃.getY()) {
               ☃xxxx = this.getMaxCurrentStrength(☃, ☃xxxxxx.up(), ☃xxxx);
            }
         } else if (!☃.getBlockState(☃xxxxxx).isNormalCube() && ☃xxxxxxx && ☃.getY() <= ☃.getY()) {
            ☃xxxx = this.getMaxCurrentStrength(☃, ☃xxxxxx.down(), ☃xxxx);
         }
      }

      if (☃xxxx > ☃xx) {
         ☃xx = ☃xxxx - 1;
      } else if (☃xx > 0) {
         ☃xx--;
      } else {
         ☃xx = 0;
      }

      if (☃xxx > ☃xx - 1) {
         ☃xx = ☃xxx;
      }

      if (☃x != ☃xx) {
         ☃ = ☃.withProperty(POWER, ☃xx);
         if (☃.getBlockState(☃) == ☃) {
            ☃.setBlockState(☃, ☃, 2);
         }

         this.blocksNeedingUpdate.add(☃);

         for (EnumFacing ☃xxxxx : EnumFacing.values()) {
            this.blocksNeedingUpdate.add(☃.offset(☃xxxxx));
         }
      }

      return ☃;
   }

   private void notifyWireNeighborsOfStateChange(World var1, BlockPos var2) {
      if (☃.getBlockState(☃).getBlock() == this) {
         ☃.notifyNeighborsOfStateChange(☃, this, false);

         for (EnumFacing ☃ : EnumFacing.values()) {
            ☃.notifyNeighborsOfStateChange(☃.offset(☃), this, false);
         }
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         this.updateSurroundingRedstone(☃, ☃, ☃);

         for (EnumFacing ☃ : EnumFacing.Plane.VERTICAL) {
            ☃.notifyNeighborsOfStateChange(☃.offset(☃), this, false);
         }

         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            this.notifyWireNeighborsOfStateChange(☃, ☃.offset(☃));
         }

         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            BlockPos ☃x = ☃.offset(☃);
            if (☃.getBlockState(☃x).isNormalCube()) {
               this.notifyWireNeighborsOfStateChange(☃, ☃x.up());
            } else {
               this.notifyWireNeighborsOfStateChange(☃, ☃x.down());
            }
         }
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(☃, ☃, ☃);
      if (!☃.isRemote) {
         for (EnumFacing ☃ : EnumFacing.values()) {
            ☃.notifyNeighborsOfStateChange(☃.offset(☃), this, false);
         }

         this.updateSurroundingRedstone(☃, ☃, ☃);

         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            this.notifyWireNeighborsOfStateChange(☃, ☃.offset(☃));
         }

         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            BlockPos ☃x = ☃.offset(☃);
            if (☃.getBlockState(☃x).isNormalCube()) {
               this.notifyWireNeighborsOfStateChange(☃, ☃x.up());
            } else {
               this.notifyWireNeighborsOfStateChange(☃, ☃x.down());
            }
         }
      }
   }

   private int getMaxCurrentStrength(World var1, BlockPos var2, int var3) {
      if (☃.getBlockState(☃).getBlock() != this) {
         return ☃;
      } else {
         int ☃ = ☃.getBlockState(☃).getValue(POWER);
         return ☃ > ☃ ? ☃ : ☃;
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         if (this.canPlaceBlockAt(☃, ☃)) {
            this.updateSurroundingRedstone(☃, ☃, ☃);
         } else {
            this.dropBlockAsItem(☃, ☃, ☃, 0);
            ☃.setBlockToAir(☃);
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.REDSTONE;
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return !this.canProvidePower ? 0 : ☃.getWeakPower(☃, ☃, ☃);
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (!this.canProvidePower) {
         return 0;
      } else {
         int ☃ = ☃.getValue(POWER);
         if (☃ == 0) {
            return 0;
         } else if (☃ == EnumFacing.UP) {
            return ☃;
         } else {
            EnumSet<EnumFacing> ☃x = EnumSet.noneOf(EnumFacing.class);

            for (EnumFacing ☃xx : EnumFacing.Plane.HORIZONTAL) {
               if (this.isPowerSourceAt(☃, ☃, ☃xx)) {
                  ☃x.add(☃xx);
               }
            }

            if (☃.getAxis().isHorizontal() && ☃x.isEmpty()) {
               return ☃;
            } else {
               return ☃x.contains(☃) && !☃x.contains(☃.rotateYCCW()) && !☃x.contains(☃.rotateY()) ? ☃ : 0;
            }
         }
      }
   }

   private boolean isPowerSourceAt(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      BlockPos ☃ = ☃.offset(☃);
      IBlockState ☃x = ☃.getBlockState(☃);
      boolean ☃xx = ☃x.isNormalCube();
      boolean ☃xxx = ☃.getBlockState(☃.up()).isNormalCube();
      if (!☃xxx && ☃xx && canConnectUpwardsTo(☃, ☃.up())) {
         return true;
      } else if (canConnectTo(☃x, ☃)) {
         return true;
      } else {
         return ☃x.getBlock() == Blocks.POWERED_REPEATER && ☃x.getValue(BlockRedstoneDiode.FACING) == ☃ ? true : !☃xx && canConnectUpwardsTo(☃, ☃.down());
      }
   }

   protected static boolean canConnectUpwardsTo(IBlockAccess var0, BlockPos var1) {
      return canConnectUpwardsTo(☃.getBlockState(☃));
   }

   protected static boolean canConnectUpwardsTo(IBlockState var0) {
      return canConnectTo(☃, null);
   }

   protected static boolean canConnectTo(IBlockState var0, @Nullable EnumFacing var1) {
      Block ☃ = ☃.getBlock();
      if (☃ == Blocks.REDSTONE_WIRE) {
         return true;
      } else if (Blocks.UNPOWERED_REPEATER.isSameDiode(☃)) {
         EnumFacing ☃x = ☃.getValue(BlockRedstoneRepeater.FACING);
         return ☃x == ☃ || ☃x.getOpposite() == ☃;
      } else {
         return Blocks.OBSERVER == ☃.getBlock() ? ☃ == ☃.getValue(BlockObserver.FACING) : ☃.canProvidePower() && ☃ != null;
      }
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return this.canProvidePower;
   }

   public static int colorMultiplier(int var0) {
      float ☃ = ☃ / 15.0F;
      float ☃x = ☃ * 0.6F + 0.4F;
      if (☃ == 0) {
         ☃x = 0.3F;
      }

      float ☃xx = ☃ * ☃ * 0.7F - 0.5F;
      float ☃xxx = ☃ * ☃ * 0.6F - 0.7F;
      if (☃xx < 0.0F) {
         ☃xx = 0.0F;
      }

      if (☃xxx < 0.0F) {
         ☃xxx = 0.0F;
      }

      int ☃xxxx = MathHelper.clamp((int)(☃x * 255.0F), 0, 255);
      int ☃xxxxx = MathHelper.clamp((int)(☃xx * 255.0F), 0, 255);
      int ☃xxxxxx = MathHelper.clamp((int)(☃xxx * 255.0F), 0, 255);
      return 0xFF000000 | ☃xxxx << 16 | ☃xxxxx << 8 | ☃xxxxxx;
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      int ☃ = ☃.getValue(POWER);
      if (☃ != 0) {
         double ☃x = ☃.getX() + 0.5 + (☃.nextFloat() - 0.5) * 0.2;
         double ☃xx = ☃.getY() + 0.0625F;
         double ☃xxx = ☃.getZ() + 0.5 + (☃.nextFloat() - 0.5) * 0.2;
         float ☃xxxx = ☃ / 15.0F;
         float ☃xxxxx = ☃xxxx * 0.6F + 0.4F;
         float ☃xxxxxx = Math.max(0.0F, ☃xxxx * ☃xxxx * 0.7F - 0.5F);
         float ☃xxxxxxx = Math.max(0.0F, ☃xxxx * ☃xxxx * 0.6F - 0.7F);
         ☃.spawnParticle(EnumParticleTypes.REDSTONE, ☃x, ☃xx, ☃xxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.REDSTONE);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWER, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(POWER);
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case CLOCKWISE_180:
            return ☃.withProperty(NORTH, ☃.getValue(SOUTH))
               .withProperty(EAST, ☃.getValue(WEST))
               .withProperty(SOUTH, ☃.getValue(NORTH))
               .withProperty(WEST, ☃.getValue(EAST));
         case COUNTERCLOCKWISE_90:
            return ☃.withProperty(NORTH, ☃.getValue(EAST))
               .withProperty(EAST, ☃.getValue(SOUTH))
               .withProperty(SOUTH, ☃.getValue(WEST))
               .withProperty(WEST, ☃.getValue(NORTH));
         case CLOCKWISE_90:
            return ☃.withProperty(NORTH, ☃.getValue(WEST))
               .withProperty(EAST, ☃.getValue(NORTH))
               .withProperty(SOUTH, ☃.getValue(EAST))
               .withProperty(WEST, ☃.getValue(SOUTH));
         default:
            return ☃;
      }
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      switch (☃) {
         case LEFT_RIGHT:
            return ☃.withProperty(NORTH, ☃.getValue(SOUTH)).withProperty(SOUTH, ☃.getValue(NORTH));
         case FRONT_BACK:
            return ☃.withProperty(EAST, ☃.getValue(WEST)).withProperty(WEST, ☃.getValue(EAST));
         default:
            return super.withMirror(☃, ☃);
      }
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, POWER);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   static enum EnumAttachPosition implements IStringSerializable {
      UP("up"),
      SIDE("side"),
      NONE("none");

      private final String name;

      private EnumAttachPosition(String var3) {
         this.name = ☃;
      }

      @Override
      public String toString() {
         return this.getName();
      }

      @Override
      public String getName() {
         return this.name;
      }
   }
}
