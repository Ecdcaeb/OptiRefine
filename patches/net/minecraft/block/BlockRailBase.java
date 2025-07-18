package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block {
   protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
   protected static final AxisAlignedBB ASCENDING_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
   protected final boolean isPowered;

   public static boolean isRailBlock(World var0, BlockPos var1) {
      return isRailBlock(☃.getBlockState(☃));
   }

   public static boolean isRailBlock(IBlockState var0) {
      Block ☃ = ☃.getBlock();
      return ☃ == Blocks.RAIL || ☃ == Blocks.GOLDEN_RAIL || ☃ == Blocks.DETECTOR_RAIL || ☃ == Blocks.ACTIVATOR_RAIL;
   }

   protected BlockRailBase(boolean var1) {
      super(Material.CIRCUITS);
      this.isPowered = ☃;
      this.setCreativeTab(CreativeTabs.TRANSPORTATION);
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
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      BlockRailBase.EnumRailDirection ☃ = ☃.getBlock() == this ? ☃.getValue(this.getShapeProperty()) : null;
      return ☃ != null && ☃.isAscending() ? ASCENDING_AABB : FLAT_AABB;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.down()).isTopSolid();
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         ☃ = this.updateDir(☃, ☃, ☃, true);
         if (this.isPowered) {
            ☃.neighborChanged(☃, ☃, this, ☃);
         }
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         BlockRailBase.EnumRailDirection ☃ = ☃.getValue(this.getShapeProperty());
         boolean ☃x = false;
         if (!☃.getBlockState(☃.down()).isTopSolid()) {
            ☃x = true;
         }

         if (☃ == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !☃.getBlockState(☃.east()).isTopSolid()) {
            ☃x = true;
         } else if (☃ == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !☃.getBlockState(☃.west()).isTopSolid()) {
            ☃x = true;
         } else if (☃ == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !☃.getBlockState(☃.north()).isTopSolid()) {
            ☃x = true;
         } else if (☃ == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !☃.getBlockState(☃.south()).isTopSolid()) {
            ☃x = true;
         }

         if (☃x && !☃.isAirBlock(☃)) {
            this.dropBlockAsItem(☃, ☃, ☃, 0);
            ☃.setBlockToAir(☃);
         } else {
            this.updateState(☃, ☃, ☃, ☃);
         }
      }
   }

   protected void updateState(IBlockState var1, World var2, BlockPos var3, Block var4) {
   }

   protected IBlockState updateDir(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return ☃.isRemote ? ☃ : new BlockRailBase.Rail(☃, ☃, ☃).place(☃.isBlockPowered(☃), ☃).getBlockState();
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.NORMAL;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(☃, ☃, ☃);
      if (☃.getValue(this.getShapeProperty()).isAscending()) {
         ☃.notifyNeighborsOfStateChange(☃.up(), this, false);
      }

      if (this.isPowered) {
         ☃.notifyNeighborsOfStateChange(☃, this, false);
         ☃.notifyNeighborsOfStateChange(☃.down(), this, false);
      }
   }

   public abstract IProperty<BlockRailBase.EnumRailDirection> getShapeProperty();

   public static enum EnumRailDirection implements IStringSerializable {
      NORTH_SOUTH(0, "north_south"),
      EAST_WEST(1, "east_west"),
      ASCENDING_EAST(2, "ascending_east"),
      ASCENDING_WEST(3, "ascending_west"),
      ASCENDING_NORTH(4, "ascending_north"),
      ASCENDING_SOUTH(5, "ascending_south"),
      SOUTH_EAST(6, "south_east"),
      SOUTH_WEST(7, "south_west"),
      NORTH_WEST(8, "north_west"),
      NORTH_EAST(9, "north_east");

      private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[values().length];
      private final int meta;
      private final String name;

      private EnumRailDirection(int var3, String var4) {
         this.meta = ☃;
         this.name = ☃;
      }

      public int getMetadata() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public boolean isAscending() {
         return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
      }

      public static BlockRailBase.EnumRailDirection byMetadata(int var0) {
         if (☃ < 0 || ☃ >= META_LOOKUP.length) {
            ☃ = 0;
         }

         return META_LOOKUP[☃];
      }

      @Override
      public String getName() {
         return this.name;
      }

      static {
         for (BlockRailBase.EnumRailDirection ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }

   public class Rail {
      private final World world;
      private final BlockPos pos;
      private final BlockRailBase block;
      private IBlockState state;
      private final boolean isPowered;
      private final List<BlockPos> connectedRails = Lists.newArrayList();

      public Rail(World var2, BlockPos var3, IBlockState var4) {
         this.world = ☃;
         this.pos = ☃;
         this.state = ☃;
         this.block = (BlockRailBase)☃.getBlock();
         BlockRailBase.EnumRailDirection ☃ = ☃.getValue(this.block.getShapeProperty());
         this.isPowered = this.block.isPowered;
         this.updateConnectedRails(☃);
      }

      public List<BlockPos> getConnectedRails() {
         return this.connectedRails;
      }

      private void updateConnectedRails(BlockRailBase.EnumRailDirection var1) {
         this.connectedRails.clear();
         switch (☃) {
            case NORTH_SOUTH:
               this.connectedRails.add(this.pos.north());
               this.connectedRails.add(this.pos.south());
               break;
            case EAST_WEST:
               this.connectedRails.add(this.pos.west());
               this.connectedRails.add(this.pos.east());
               break;
            case ASCENDING_EAST:
               this.connectedRails.add(this.pos.west());
               this.connectedRails.add(this.pos.east().up());
               break;
            case ASCENDING_WEST:
               this.connectedRails.add(this.pos.west().up());
               this.connectedRails.add(this.pos.east());
               break;
            case ASCENDING_NORTH:
               this.connectedRails.add(this.pos.north().up());
               this.connectedRails.add(this.pos.south());
               break;
            case ASCENDING_SOUTH:
               this.connectedRails.add(this.pos.north());
               this.connectedRails.add(this.pos.south().up());
               break;
            case SOUTH_EAST:
               this.connectedRails.add(this.pos.east());
               this.connectedRails.add(this.pos.south());
               break;
            case SOUTH_WEST:
               this.connectedRails.add(this.pos.west());
               this.connectedRails.add(this.pos.south());
               break;
            case NORTH_WEST:
               this.connectedRails.add(this.pos.west());
               this.connectedRails.add(this.pos.north());
               break;
            case NORTH_EAST:
               this.connectedRails.add(this.pos.east());
               this.connectedRails.add(this.pos.north());
         }
      }

      private void removeSoftConnections() {
         for (int ☃ = 0; ☃ < this.connectedRails.size(); ☃++) {
            BlockRailBase.Rail ☃x = this.findRailAt(this.connectedRails.get(☃));
            if (☃x != null && ☃x.isConnectedToRail(this)) {
               this.connectedRails.set(☃, ☃x.pos);
            } else {
               this.connectedRails.remove(☃--);
            }
         }
      }

      private boolean hasRailAt(BlockPos var1) {
         return BlockRailBase.isRailBlock(this.world, ☃) || BlockRailBase.isRailBlock(this.world, ☃.up()) || BlockRailBase.isRailBlock(this.world, ☃.down());
      }

      @Nullable
      private BlockRailBase.Rail findRailAt(BlockPos var1) {
         IBlockState ☃ = this.world.getBlockState(☃);
         if (BlockRailBase.isRailBlock(☃)) {
            return BlockRailBase.this.new Rail(this.world, ☃, ☃);
         } else {
            BlockPos var2 = ☃.up();
            ☃ = this.world.getBlockState(var2);
            if (BlockRailBase.isRailBlock(☃)) {
               return BlockRailBase.this.new Rail(this.world, var2, ☃);
            } else {
               var2 = ☃.down();
               ☃ = this.world.getBlockState(var2);
               return BlockRailBase.isRailBlock(☃) ? BlockRailBase.this.new Rail(this.world, var2, ☃) : null;
            }
         }
      }

      private boolean isConnectedToRail(BlockRailBase.Rail var1) {
         return this.isConnectedTo(☃.pos);
      }

      private boolean isConnectedTo(BlockPos var1) {
         for (int ☃ = 0; ☃ < this.connectedRails.size(); ☃++) {
            BlockPos ☃x = this.connectedRails.get(☃);
            if (☃x.getX() == ☃.getX() && ☃x.getZ() == ☃.getZ()) {
               return true;
            }
         }

         return false;
      }

      protected int countAdjacentRails() {
         int ☃ = 0;

         for (EnumFacing ☃x : EnumFacing.Plane.HORIZONTAL) {
            if (this.hasRailAt(this.pos.offset(☃x))) {
               ☃++;
            }
         }

         return ☃;
      }

      private boolean canConnectTo(BlockRailBase.Rail var1) {
         return this.isConnectedToRail(☃) || this.connectedRails.size() != 2;
      }

      private void connectTo(BlockRailBase.Rail var1) {
         this.connectedRails.add(☃.pos);
         BlockPos ☃ = this.pos.north();
         BlockPos ☃x = this.pos.south();
         BlockPos ☃xx = this.pos.west();
         BlockPos ☃xxx = this.pos.east();
         boolean ☃xxxx = this.isConnectedTo(☃);
         boolean ☃xxxxx = this.isConnectedTo(☃x);
         boolean ☃xxxxxx = this.isConnectedTo(☃xx);
         boolean ☃xxxxxxx = this.isConnectedTo(☃xxx);
         BlockRailBase.EnumRailDirection ☃xxxxxxxx = null;
         if (☃xxxx || ☃xxxxx) {
            ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         if (☃xxxxxx || ☃xxxxxxx) {
            ☃xxxxxxxx = BlockRailBase.EnumRailDirection.EAST_WEST;
         }

         if (!this.isPowered) {
            if (☃xxxxx && ☃xxxxxxx && !☃xxxx && !☃xxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_EAST;
            }

            if (☃xxxxx && ☃xxxxxx && !☃xxxx && !☃xxxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_WEST;
            }

            if (☃xxxx && ☃xxxxxx && !☃xxxxx && !☃xxxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_WEST;
            }

            if (☃xxxx && ☃xxxxxxx && !☃xxxxx && !☃xxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_EAST;
            }
         }

         if (☃xxxxxxxx == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (BlockRailBase.isRailBlock(this.world, ☃.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
            }

            if (BlockRailBase.isRailBlock(this.world, ☃x.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
            }
         }

         if (☃xxxxxxxx == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (BlockRailBase.isRailBlock(this.world, ☃xxx.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
            }

            if (BlockRailBase.isRailBlock(this.world, ☃xx.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
            }
         }

         if (☃xxxxxxxx == null) {
            ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         this.state = this.state.withProperty(this.block.getShapeProperty(), ☃xxxxxxxx);
         this.world.setBlockState(this.pos, this.state, 3);
      }

      private boolean hasNeighborRail(BlockPos var1) {
         BlockRailBase.Rail ☃ = this.findRailAt(☃);
         if (☃ == null) {
            return false;
         } else {
            ☃.removeSoftConnections();
            return ☃.canConnectTo(this);
         }
      }

      public BlockRailBase.Rail place(boolean var1, boolean var2) {
         BlockPos ☃ = this.pos.north();
         BlockPos ☃x = this.pos.south();
         BlockPos ☃xx = this.pos.west();
         BlockPos ☃xxx = this.pos.east();
         boolean ☃xxxx = this.hasNeighborRail(☃);
         boolean ☃xxxxx = this.hasNeighborRail(☃x);
         boolean ☃xxxxxx = this.hasNeighborRail(☃xx);
         boolean ☃xxxxxxx = this.hasNeighborRail(☃xxx);
         BlockRailBase.EnumRailDirection ☃xxxxxxxx = null;
         if ((☃xxxx || ☃xxxxx) && !☃xxxxxx && !☃xxxxxxx) {
            ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         if ((☃xxxxxx || ☃xxxxxxx) && !☃xxxx && !☃xxxxx) {
            ☃xxxxxxxx = BlockRailBase.EnumRailDirection.EAST_WEST;
         }

         if (!this.isPowered) {
            if (☃xxxxx && ☃xxxxxxx && !☃xxxx && !☃xxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_EAST;
            }

            if (☃xxxxx && ☃xxxxxx && !☃xxxx && !☃xxxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_WEST;
            }

            if (☃xxxx && ☃xxxxxx && !☃xxxxx && !☃xxxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_WEST;
            }

            if (☃xxxx && ☃xxxxxxx && !☃xxxxx && !☃xxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_EAST;
            }
         }

         if (☃xxxxxxxx == null) {
            if (☃xxxx || ☃xxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if (☃xxxxxx || ☃xxxxxxx) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered) {
               if (☃) {
                  if (☃xxxxx && ☃xxxxxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                  }

                  if (☃xxxxxx && ☃xxxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                  }

                  if (☃xxxxxxx && ☃xxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_EAST;
                  }

                  if (☃xxxx && ☃xxxxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_WEST;
                  }
               } else {
                  if (☃xxxx && ☃xxxxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_WEST;
                  }

                  if (☃xxxxxxx && ☃xxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_EAST;
                  }

                  if (☃xxxxxx && ☃xxxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                  }

                  if (☃xxxxx && ☃xxxxxxx) {
                     ☃xxxxxxxx = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                  }
               }
            }
         }

         if (☃xxxxxxxx == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (BlockRailBase.isRailBlock(this.world, ☃.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
            }

            if (BlockRailBase.isRailBlock(this.world, ☃x.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
            }
         }

         if (☃xxxxxxxx == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (BlockRailBase.isRailBlock(this.world, ☃xxx.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
            }

            if (BlockRailBase.isRailBlock(this.world, ☃xx.up())) {
               ☃xxxxxxxx = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
            }
         }

         if (☃xxxxxxxx == null) {
            ☃xxxxxxxx = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         this.updateConnectedRails(☃xxxxxxxx);
         this.state = this.state.withProperty(this.block.getShapeProperty(), ☃xxxxxxxx);
         if (☃ || this.world.getBlockState(this.pos) != this.state) {
            this.world.setBlockState(this.pos, this.state, 3);

            for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < this.connectedRails.size(); ☃xxxxxxxxx++) {
               BlockRailBase.Rail ☃xxxxxxxxxx = this.findRailAt(this.connectedRails.get(☃xxxxxxxxx));
               if (☃xxxxxxxxxx != null) {
                  ☃xxxxxxxxxx.removeSoftConnections();
                  if (☃xxxxxxxxxx.canConnectTo(this)) {
                     ☃xxxxxxxxxx.connectTo(this);
                  }
               }
            }
         }

         return this;
      }

      public IBlockState getBlockState() {
         return this.state;
      }
   }
}
