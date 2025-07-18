package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   protected static final AxisAlignedBB NORTH_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0, 0.9375, 0.875, 0.9375);
   protected static final AxisAlignedBB SOUTH_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 1.0);
   protected static final AxisAlignedBB WEST_CHEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
   protected static final AxisAlignedBB EAST_CHEST_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 1.0, 0.875, 0.9375);
   protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
   public final BlockChest.Type chestType;

   protected BlockChest(BlockChest.Type var1) {
      super(Material.WOOD);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.chestType = ☃;
      this.setCreativeTab(☃ == BlockChest.Type.TRAP ? CreativeTabs.REDSTONE : CreativeTabs.DECORATIONS);
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
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getBlockState(☃.north()).getBlock() == this) {
         return NORTH_CHEST_AABB;
      } else if (☃.getBlockState(☃.south()).getBlock() == this) {
         return SOUTH_CHEST_AABB;
      } else if (☃.getBlockState(☃.west()).getBlock() == this) {
         return WEST_CHEST_AABB;
      } else {
         return ☃.getBlockState(☃.east()).getBlock() == this ? EAST_CHEST_AABB : NOT_CONNECTED_AABB;
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.checkForSurroundingChests(☃, ☃, ☃);

      for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
         BlockPos ☃x = ☃.offset(☃);
         IBlockState ☃xx = ☃.getBlockState(☃x);
         if (☃xx.getBlock() == this) {
            this.checkForSurroundingChests(☃, ☃x, ☃xx);
         }
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing());
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      EnumFacing ☃ = EnumFacing.byHorizontalIndex(MathHelper.floor(☃.rotationYaw * 4.0F / 360.0F + 0.5) & 3).getOpposite();
      ☃ = ☃.withProperty(FACING, ☃);
      BlockPos ☃x = ☃.north();
      BlockPos ☃xx = ☃.south();
      BlockPos ☃xxx = ☃.west();
      BlockPos ☃xxxx = ☃.east();
      boolean ☃xxxxx = this == ☃.getBlockState(☃x).getBlock();
      boolean ☃xxxxxx = this == ☃.getBlockState(☃xx).getBlock();
      boolean ☃xxxxxxx = this == ☃.getBlockState(☃xxx).getBlock();
      boolean ☃xxxxxxxx = this == ☃.getBlockState(☃xxxx).getBlock();
      if (!☃xxxxx && !☃xxxxxx && !☃xxxxxxx && !☃xxxxxxxx) {
         ☃.setBlockState(☃, ☃, 3);
      } else if (☃.getAxis() != EnumFacing.Axis.X || !☃xxxxx && !☃xxxxxx) {
         if (☃.getAxis() == EnumFacing.Axis.Z && (☃xxxxxxx || ☃xxxxxxxx)) {
            if (☃xxxxxxx) {
               ☃.setBlockState(☃xxx, ☃, 3);
            } else {
               ☃.setBlockState(☃xxxx, ☃, 3);
            }

            ☃.setBlockState(☃, ☃, 3);
         }
      } else {
         if (☃xxxxx) {
            ☃.setBlockState(☃x, ☃, 3);
         } else {
            ☃.setBlockState(☃xx, ☃, 3);
         }

         ☃.setBlockState(☃, ☃, 3);
      }

      if (☃.hasDisplayName()) {
         TileEntity ☃xxxxxxxxx = ☃.getTileEntity(☃);
         if (☃xxxxxxxxx instanceof TileEntityChest) {
            ((TileEntityChest)☃xxxxxxxxx).setCustomName(☃.getDisplayName());
         }
      }
   }

   public IBlockState checkForSurroundingChests(World var1, BlockPos var2, IBlockState var3) {
      if (☃.isRemote) {
         return ☃;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃.north());
         IBlockState ☃x = ☃.getBlockState(☃.south());
         IBlockState ☃xx = ☃.getBlockState(☃.west());
         IBlockState ☃xxx = ☃.getBlockState(☃.east());
         EnumFacing ☃xxxx = ☃.getValue(FACING);
         if (☃.getBlock() != this && ☃x.getBlock() != this) {
            boolean ☃xxxxx = ☃.isFullBlock();
            boolean ☃xxxxxx = ☃x.isFullBlock();
            if (☃xx.getBlock() == this || ☃xxx.getBlock() == this) {
               BlockPos ☃xxxxxxx = ☃xx.getBlock() == this ? ☃.west() : ☃.east();
               IBlockState ☃xxxxxxxx = ☃.getBlockState(☃xxxxxxx.north());
               IBlockState ☃xxxxxxxxx = ☃.getBlockState(☃xxxxxxx.south());
               ☃xxxx = EnumFacing.SOUTH;
               EnumFacing ☃xxxxxxxxxx;
               if (☃xx.getBlock() == this) {
                  ☃xxxxxxxxxx = ☃xx.getValue(FACING);
               } else {
                  ☃xxxxxxxxxx = ☃xxx.getValue(FACING);
               }

               if (☃xxxxxxxxxx == EnumFacing.NORTH) {
                  ☃xxxx = EnumFacing.NORTH;
               }

               if ((☃xxxxx || ☃xxxxxxxx.isFullBlock()) && !☃xxxxxx && !☃xxxxxxxxx.isFullBlock()) {
                  ☃xxxx = EnumFacing.SOUTH;
               }

               if ((☃xxxxxx || ☃xxxxxxxxx.isFullBlock()) && !☃xxxxx && !☃xxxxxxxx.isFullBlock()) {
                  ☃xxxx = EnumFacing.NORTH;
               }
            }
         } else {
            BlockPos ☃xxxxx = ☃.getBlock() == this ? ☃.north() : ☃.south();
            IBlockState ☃xxxxxx = ☃.getBlockState(☃xxxxx.west());
            IBlockState ☃xxxxxxxxxxx = ☃.getBlockState(☃xxxxx.east());
            ☃xxxx = EnumFacing.EAST;
            EnumFacing ☃xxxxxxxxxxxx;
            if (☃.getBlock() == this) {
               ☃xxxxxxxxxxxx = ☃.getValue(FACING);
            } else {
               ☃xxxxxxxxxxxx = ☃x.getValue(FACING);
            }

            if (☃xxxxxxxxxxxx == EnumFacing.WEST) {
               ☃xxxx = EnumFacing.WEST;
            }

            if ((☃xx.isFullBlock() || ☃xxxxxx.isFullBlock()) && !☃xxx.isFullBlock() && !☃xxxxxxxxxxx.isFullBlock()) {
               ☃xxxx = EnumFacing.EAST;
            }

            if ((☃xxx.isFullBlock() || ☃xxxxxxxxxxx.isFullBlock()) && !☃xx.isFullBlock() && !☃xxxxxx.isFullBlock()) {
               ☃xxxx = EnumFacing.WEST;
            }
         }

         ☃ = ☃.withProperty(FACING, ☃xxxx);
         ☃.setBlockState(☃, ☃, 3);
         return ☃;
      }
   }

   public IBlockState correctFacing(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = null;

      for (EnumFacing ☃x : EnumFacing.Plane.HORIZONTAL) {
         IBlockState ☃xx = ☃.getBlockState(☃.offset(☃x));
         if (☃xx.getBlock() == this) {
            return ☃;
         }

         if (☃xx.isFullBlock()) {
            if (☃ != null) {
               ☃ = null;
               break;
            }

            ☃ = ☃x;
         }
      }

      if (☃ != null) {
         return ☃.withProperty(FACING, ☃.getOpposite());
      } else {
         EnumFacing ☃x = ☃.getValue(FACING);
         if (☃.getBlockState(☃.offset(☃x)).isFullBlock()) {
            ☃x = ☃x.getOpposite();
         }

         if (☃.getBlockState(☃.offset(☃x)).isFullBlock()) {
            ☃x = ☃x.rotateY();
         }

         if (☃.getBlockState(☃.offset(☃x)).isFullBlock()) {
            ☃x = ☃x.getOpposite();
         }

         return ☃.withProperty(FACING, ☃x);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      int ☃ = 0;
      BlockPos ☃x = ☃.west();
      BlockPos ☃xx = ☃.east();
      BlockPos ☃xxx = ☃.north();
      BlockPos ☃xxxx = ☃.south();
      if (☃.getBlockState(☃x).getBlock() == this) {
         if (this.isDoubleChest(☃, ☃x)) {
            return false;
         }

         ☃++;
      }

      if (☃.getBlockState(☃xx).getBlock() == this) {
         if (this.isDoubleChest(☃, ☃xx)) {
            return false;
         }

         ☃++;
      }

      if (☃.getBlockState(☃xxx).getBlock() == this) {
         if (this.isDoubleChest(☃, ☃xxx)) {
            return false;
         }

         ☃++;
      }

      if (☃.getBlockState(☃xxxx).getBlock() == this) {
         if (this.isDoubleChest(☃, ☃xxxx)) {
            return false;
         }

         ☃++;
      }

      return ☃ <= 1;
   }

   private boolean isDoubleChest(World var1, BlockPos var2) {
      if (☃.getBlockState(☃).getBlock() != this) {
         return false;
      } else {
         for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
            if (☃.getBlockState(☃.offset(☃)).getBlock() == this) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      super.neighborChanged(☃, ☃, ☃, ☃, ☃);
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityChest) {
         ☃.updateContainingBlockInfo();
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof IInventory) {
         InventoryHelper.dropInventoryItems(☃, ☃, (IInventory)☃);
         ☃.updateComparatorOutputLevel(☃, this);
      }

      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         ILockableContainer ☃ = this.getLockableContainer(☃, ☃);
         if (☃ != null) {
            ☃.displayGUIChest(☃);
            if (this.chestType == BlockChest.Type.BASIC) {
               ☃.addStat(StatList.CHEST_OPENED);
            } else if (this.chestType == BlockChest.Type.TRAP) {
               ☃.addStat(StatList.TRAPPED_CHEST_TRIGGERED);
            }
         }

         return true;
      }
   }

   @Nullable
   public ILockableContainer getLockableContainer(World var1, BlockPos var2) {
      return this.getContainer(☃, ☃, false);
   }

   @Nullable
   public ILockableContainer getContainer(World var1, BlockPos var2, boolean var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (!(☃ instanceof TileEntityChest)) {
         return null;
      } else {
         ILockableContainer ☃x = (TileEntityChest)☃;
         if (!☃ && this.isBlocked(☃, ☃)) {
            return null;
         } else {
            for (EnumFacing ☃xx : EnumFacing.Plane.HORIZONTAL) {
               BlockPos ☃xxx = ☃.offset(☃xx);
               Block ☃xxxx = ☃.getBlockState(☃xxx).getBlock();
               if (☃xxxx == this) {
                  if (this.isBlocked(☃, ☃xxx)) {
                     return null;
                  }

                  TileEntity ☃xxxxx = ☃.getTileEntity(☃xxx);
                  if (☃xxxxx instanceof TileEntityChest) {
                     if (☃xx != EnumFacing.WEST && ☃xx != EnumFacing.NORTH) {
                        ☃x = new InventoryLargeChest("container.chestDouble", ☃x, (TileEntityChest)☃xxxxx);
                     } else {
                        ☃x = new InventoryLargeChest("container.chestDouble", (TileEntityChest)☃xxxxx, ☃x);
                     }
                  }
               }
            }

            return ☃x;
         }
      }
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityChest();
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return this.chestType == BlockChest.Type.TRAP;
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (!☃.canProvidePower()) {
         return 0;
      } else {
         int ☃ = 0;
         TileEntity ☃x = ☃.getTileEntity(☃);
         if (☃x instanceof TileEntityChest) {
            ☃ = ((TileEntityChest)☃x).numPlayersUsing;
         }

         return MathHelper.clamp(☃, 0, 15);
      }
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.UP ? ☃.getWeakPower(☃, ☃, ☃) : 0;
   }

   private boolean isBlocked(World var1, BlockPos var2) {
      return this.isBelowSolidBlock(☃, ☃) || this.isOcelotSittingOnChest(☃, ☃);
   }

   private boolean isBelowSolidBlock(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.up()).isNormalCube();
   }

   private boolean isOcelotSittingOnChest(World var1, BlockPos var2) {
      for (Entity ☃ : ☃.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(☃.getX(), ☃.getY() + 1, ☃.getZ(), ☃.getX() + 1, ☃.getY() + 2, ☃.getZ() + 1))) {
         EntityOcelot ☃x = (EntityOcelot)☃;
         if (☃x.isSitting()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      return Container.calcRedstoneFromInventory(this.getLockableContainer(☃, ☃));
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

   public static enum Type {
      BASIC,
      TRAP;
   }
}
