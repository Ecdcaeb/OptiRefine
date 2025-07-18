package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block {
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool[] ALL_FACES = new PropertyBool[]{UP, NORTH, SOUTH, WEST, EAST};
   protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0625, 1.0, 1.0);
   protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375, 0.0, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.0625);
   protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.9375, 1.0, 1.0, 1.0);

   public BlockVine() {
      super(Material.VINE);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(UP, false)
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
      );
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = ☃.getActualState(☃, ☃);
      int ☃ = 0;
      AxisAlignedBB ☃x = FULL_BLOCK_AABB;
      if (☃.getValue(UP)) {
         ☃x = UP_AABB;
         ☃++;
      }

      if (☃.getValue(NORTH)) {
         ☃x = NORTH_AABB;
         ☃++;
      }

      if (☃.getValue(EAST)) {
         ☃x = EAST_AABB;
         ☃++;
      }

      if (☃.getValue(SOUTH)) {
         ☃x = SOUTH_AABB;
         ☃++;
      }

      if (☃.getValue(WEST)) {
         ☃x = WEST_AABB;
         ☃++;
      }

      return ☃ == 1 ? ☃x : FULL_BLOCK_AABB;
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      BlockPos ☃ = ☃.up();
      return ☃.withProperty(UP, ☃.getBlockState(☃).getBlockFaceShape(☃, ☃, EnumFacing.DOWN) == BlockFaceShape.SOLID);
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
   public boolean isReplaceable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return ☃ != EnumFacing.DOWN && ☃ != EnumFacing.UP && this.canAttachTo(☃, ☃, ☃);
   }

   public boolean canAttachTo(World var1, BlockPos var2, EnumFacing var3) {
      Block ☃ = ☃.getBlockState(☃.up()).getBlock();
      return this.isAcceptableNeighbor(☃, ☃.offset(☃.getOpposite()), ☃)
         && (☃ == Blocks.AIR || ☃ == Blocks.VINE || this.isAcceptableNeighbor(☃, ☃.up(), EnumFacing.UP));
   }

   private boolean isAcceptableNeighbor(World var1, BlockPos var2, EnumFacing var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      return ☃.getBlockFaceShape(☃, ☃, ☃) == BlockFaceShape.SOLID && !isExceptBlockForAttaching(☃.getBlock());
   }

   protected static boolean isExceptBlockForAttaching(Block var0) {
      return ☃ instanceof BlockShulkerBox
         || ☃ == Blocks.BEACON
         || ☃ == Blocks.CAULDRON
         || ☃ == Blocks.GLASS
         || ☃ == Blocks.STAINED_GLASS
         || ☃ == Blocks.PISTON
         || ☃ == Blocks.STICKY_PISTON
         || ☃ == Blocks.PISTON_HEAD
         || ☃ == Blocks.TRAPDOOR;
   }

   private boolean recheckGrownSides(World var1, BlockPos var2, IBlockState var3) {
      IBlockState ☃ = ☃;

      for (EnumFacing ☃x : EnumFacing.Plane.HORIZONTAL) {
         PropertyBool ☃xx = getPropertyFor(☃x);
         if (☃.getValue(☃xx) && !this.canAttachTo(☃, ☃, ☃x.getOpposite())) {
            IBlockState ☃xxx = ☃.getBlockState(☃.up());
            if (☃xxx.getBlock() != this || !☃xxx.getValue(☃xx)) {
               ☃ = ☃.withProperty(☃xx, false);
            }
         }
      }

      if (getNumGrownFaces(☃) == 0) {
         return false;
      } else {
         if (☃ != ☃) {
            ☃.setBlockState(☃, ☃, 2);
         }

         return true;
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote && !this.recheckGrownSides(☃, ☃, ☃)) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         if (☃.rand.nextInt(4) == 0) {
            int ☃ = 4;
            int ☃x = 5;
            boolean ☃xx = false;

            label179:
            for (int ☃xxx = -4; ☃xxx <= 4; ☃xxx++) {
               for (int ☃xxxx = -4; ☃xxxx <= 4; ☃xxxx++) {
                  for (int ☃xxxxx = -1; ☃xxxxx <= 1; ☃xxxxx++) {
                     if (☃.getBlockState(☃.add(☃xxx, ☃xxxxx, ☃xxxx)).getBlock() == this) {
                        if (--☃x <= 0) {
                           ☃xx = true;
                           break label179;
                        }
                     }
                  }
               }
            }

            EnumFacing ☃xxx = EnumFacing.random(☃);
            BlockPos ☃xxxx = ☃.up();
            if (☃xxx == EnumFacing.UP && ☃.getY() < 255 && ☃.isAirBlock(☃xxxx)) {
               IBlockState ☃xxxxxx = ☃;

               for (EnumFacing ☃xxxxxxx : EnumFacing.Plane.HORIZONTAL) {
                  if (☃.nextBoolean() && this.canAttachTo(☃, ☃xxxx, ☃xxxxxxx.getOpposite())) {
                     ☃xxxxxx = ☃xxxxxx.withProperty(getPropertyFor(☃xxxxxxx), true);
                  } else {
                     ☃xxxxxx = ☃xxxxxx.withProperty(getPropertyFor(☃xxxxxxx), false);
                  }
               }

               if (☃xxxxxx.getValue(NORTH) || ☃xxxxxx.getValue(EAST) || ☃xxxxxx.getValue(SOUTH) || ☃xxxxxx.getValue(WEST)) {
                  ☃.setBlockState(☃xxxx, ☃xxxxxx, 2);
               }
            } else if (!☃xxx.getAxis().isHorizontal() || ☃.getValue(getPropertyFor(☃xxx))) {
               if (☃.getY() > 1) {
                  BlockPos ☃xxxxxx = ☃.down();
                  IBlockState ☃xxxxxxxx = ☃.getBlockState(☃xxxxxx);
                  Block ☃xxxxxxxxx = ☃xxxxxxxx.getBlock();
                  if (☃xxxxxxxxx.material == Material.AIR) {
                     IBlockState ☃xxxxxxxxxx = ☃;

                     for (EnumFacing ☃xxxxxxxxxxx : EnumFacing.Plane.HORIZONTAL) {
                        if (☃.nextBoolean()) {
                           ☃xxxxxxxxxx = ☃xxxxxxxxxx.withProperty(getPropertyFor(☃xxxxxxxxxxx), false);
                        }
                     }

                     if (☃xxxxxxxxxx.getValue(NORTH) || ☃xxxxxxxxxx.getValue(EAST) || ☃xxxxxxxxxx.getValue(SOUTH) || ☃xxxxxxxxxx.getValue(WEST)) {
                        ☃.setBlockState(☃xxxxxx, ☃xxxxxxxxxx, 2);
                     }
                  } else if (☃xxxxxxxxx == this) {
                     IBlockState ☃xxxxxxxxxx = ☃xxxxxxxx;

                     for (EnumFacing ☃xxxxxxxxxxxx : EnumFacing.Plane.HORIZONTAL) {
                        PropertyBool ☃xxxxxxxxxxxxx = getPropertyFor(☃xxxxxxxxxxxx);
                        if (☃.nextBoolean() && ☃.getValue(☃xxxxxxxxxxxxx)) {
                           ☃xxxxxxxxxx = ☃xxxxxxxxxx.withProperty(☃xxxxxxxxxxxxx, true);
                        }
                     }

                     if (☃xxxxxxxxxx.getValue(NORTH) || ☃xxxxxxxxxx.getValue(EAST) || ☃xxxxxxxxxx.getValue(SOUTH) || ☃xxxxxxxxxx.getValue(WEST)) {
                        ☃.setBlockState(☃xxxxxx, ☃xxxxxxxxxx, 2);
                     }
                  }
               }
            } else if (!☃xx) {
               BlockPos ☃xxxxxx = ☃.offset(☃xxx);
               IBlockState ☃xxxxxxxx = ☃.getBlockState(☃xxxxxx);
               Block ☃xxxxxxxxx = ☃xxxxxxxx.getBlock();
               if (☃xxxxxxxxx.material == Material.AIR) {
                  EnumFacing ☃xxxxxxxxxx = ☃xxx.rotateY();
                  EnumFacing ☃xxxxxxxxxxxxx = ☃xxx.rotateYCCW();
                  boolean ☃xxxxxxxxxxxxxx = ☃.getValue(getPropertyFor(☃xxxxxxxxxx));
                  boolean ☃xxxxxxxxxxxxxxx = ☃.getValue(getPropertyFor(☃xxxxxxxxxxxxx));
                  BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxx.offset(☃xxxxxxxxxx);
                  BlockPos ☃xxxxxxxxxxxxxxxxx = ☃xxxxxx.offset(☃xxxxxxxxxxxxx);
                  if (☃xxxxxxxxxxxxxx && this.canAttachTo(☃, ☃xxxxxxxxxxxxxxxx.offset(☃xxxxxxxxxx), ☃xxxxxxxxxx)) {
                     ☃.setBlockState(☃xxxxxx, this.getDefaultState().withProperty(getPropertyFor(☃xxxxxxxxxx), true), 2);
                  } else if (☃xxxxxxxxxxxxxxx && this.canAttachTo(☃, ☃xxxxxxxxxxxxxxxxx.offset(☃xxxxxxxxxxxxx), ☃xxxxxxxxxxxxx)) {
                     ☃.setBlockState(☃xxxxxx, this.getDefaultState().withProperty(getPropertyFor(☃xxxxxxxxxxxxx), true), 2);
                  } else if (☃xxxxxxxxxxxxxx && ☃.isAirBlock(☃xxxxxxxxxxxxxxxx) && this.canAttachTo(☃, ☃xxxxxxxxxxxxxxxx, ☃xxx)) {
                     ☃.setBlockState(☃xxxxxxxxxxxxxxxx, this.getDefaultState().withProperty(getPropertyFor(☃xxx.getOpposite()), true), 2);
                  } else if (☃xxxxxxxxxxxxxxx && ☃.isAirBlock(☃xxxxxxxxxxxxxxxxx) && this.canAttachTo(☃, ☃xxxxxxxxxxxxxxxxx, ☃xxx)) {
                     ☃.setBlockState(☃xxxxxxxxxxxxxxxxx, this.getDefaultState().withProperty(getPropertyFor(☃xxx.getOpposite()), true), 2);
                  }
               } else if (☃xxxxxxxx.getBlockFaceShape(☃, ☃xxxxxx, ☃xxx) == BlockFaceShape.SOLID) {
                  ☃.setBlockState(☃, ☃.withProperty(getPropertyFor(☃xxx), true), 2);
               }
            }
         }
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState ☃ = this.getDefaultState()
         .withProperty(UP, false)
         .withProperty(NORTH, false)
         .withProperty(EAST, false)
         .withProperty(SOUTH, false)
         .withProperty(WEST, false);
      return ☃.getAxis().isHorizontal() ? ☃.withProperty(getPropertyFor(☃.getOpposite()), true) : ☃;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.AIR;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      if (!☃.isRemote && ☃.getItem() == Items.SHEARS) {
         ☃.addStat(StatList.getBlockStats(this));
         spawnAsEntity(☃, ☃, new ItemStack(Blocks.VINE, 1, 0));
      } else {
         super.harvestBlock(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState()
         .withProperty(SOUTH, (☃ & 1) > 0)
         .withProperty(WEST, (☃ & 2) > 0)
         .withProperty(NORTH, (☃ & 4) > 0)
         .withProperty(EAST, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      if (☃.getValue(SOUTH)) {
         ☃ |= 1;
      }

      if (☃.getValue(WEST)) {
         ☃ |= 2;
      }

      if (☃.getValue(NORTH)) {
         ☃ |= 4;
      }

      if (☃.getValue(EAST)) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, UP, NORTH, EAST, SOUTH, WEST);
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

   public static PropertyBool getPropertyFor(EnumFacing var0) {
      switch (☃) {
         case UP:
            return UP;
         case NORTH:
            return NORTH;
         case SOUTH:
            return SOUTH;
         case WEST:
            return WEST;
         case EAST:
            return EAST;
         default:
            throw new IllegalArgumentException(☃ + " is an invalid choice");
      }
   }

   public static int getNumGrownFaces(IBlockState var0) {
      int ☃ = 0;

      for (PropertyBool ☃x : ALL_FACES) {
         if (☃.getValue(☃x)) {
            ☃++;
         }
      }

      return ☃;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
