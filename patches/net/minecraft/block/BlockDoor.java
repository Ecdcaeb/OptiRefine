package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor extends Block {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static final PropertyBool OPEN = PropertyBool.create("open");
   public static final PropertyEnum<BlockDoor.EnumHingePosition> HINGE = PropertyEnum.create("hinge", BlockDoor.EnumHingePosition.class);
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyEnum<BlockDoor.EnumDoorHalf> HALF = PropertyEnum.create("half", BlockDoor.EnumDoorHalf.class);
   protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
   protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);

   protected BlockDoor(Material var1) {
      super(☃);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(OPEN, false)
            .withProperty(HINGE, BlockDoor.EnumHingePosition.LEFT)
            .withProperty(POWERED, false)
            .withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER)
      );
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = ☃.getActualState(☃, ☃);
      EnumFacing ☃ = ☃.getValue(FACING);
      boolean ☃x = !☃.getValue(OPEN);
      boolean ☃xx = ☃.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT;
      switch (☃) {
         case EAST:
         default:
            return ☃x ? EAST_AABB : (☃xx ? NORTH_AABB : SOUTH_AABB);
         case SOUTH:
            return ☃x ? SOUTH_AABB : (☃xx ? EAST_AABB : WEST_AABB);
         case WEST:
            return ☃x ? WEST_AABB : (☃xx ? SOUTH_AABB : NORTH_AABB);
         case NORTH:
            return ☃x ? NORTH_AABB : (☃xx ? WEST_AABB : EAST_AABB);
      }
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal((this.getTranslationKey() + ".name").replaceAll("tile", "item"));
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return isOpen(combineMetadata(☃, ☃));
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   private int getCloseSound() {
      return this.material == Material.IRON ? 1011 : 1012;
   }

   private int getOpenSound() {
      return this.material == Material.IRON ? 1005 : 1006;
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getBlock() == Blocks.IRON_DOOR) {
         return MapColor.IRON;
      } else if (☃.getBlock() == Blocks.OAK_DOOR) {
         return BlockPlanks.EnumType.OAK.getMapColor();
      } else if (☃.getBlock() == Blocks.SPRUCE_DOOR) {
         return BlockPlanks.EnumType.SPRUCE.getMapColor();
      } else if (☃.getBlock() == Blocks.BIRCH_DOOR) {
         return BlockPlanks.EnumType.BIRCH.getMapColor();
      } else if (☃.getBlock() == Blocks.JUNGLE_DOOR) {
         return BlockPlanks.EnumType.JUNGLE.getMapColor();
      } else if (☃.getBlock() == Blocks.ACACIA_DOOR) {
         return BlockPlanks.EnumType.ACACIA.getMapColor();
      } else {
         return ☃.getBlock() == Blocks.DARK_OAK_DOOR ? BlockPlanks.EnumType.DARK_OAK.getMapColor() : super.getMapColor(☃, ☃, ☃);
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (this.material == Material.IRON) {
         return false;
      } else {
         BlockPos ☃ = ☃.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? ☃ : ☃.down();
         IBlockState ☃x = ☃.equals(☃) ? ☃ : ☃.getBlockState(☃);
         if (☃x.getBlock() != this) {
            return false;
         } else {
            ☃ = ☃x.cycleProperty(OPEN);
            ☃.setBlockState(☃, ☃, 10);
            ☃.markBlockRangeForRenderUpdate(☃, ☃);
            ☃.playEvent(☃, ☃.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), ☃, 0);
            return true;
         }
      }
   }

   public void toggleDoor(World var1, BlockPos var2, boolean var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() == this) {
         BlockPos ☃x = ☃.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? ☃ : ☃.down();
         IBlockState ☃xx = ☃ == ☃x ? ☃ : ☃.getBlockState(☃x);
         if (☃xx.getBlock() == this && ☃xx.getValue(OPEN) != ☃) {
            ☃.setBlockState(☃x, ☃xx.withProperty(OPEN, ☃), 10);
            ☃.markBlockRangeForRenderUpdate(☃x, ☃);
            ☃.playEvent(null, ☃ ? this.getOpenSound() : this.getCloseSound(), ☃, 0);
         }
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (☃.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
         BlockPos ☃ = ☃.down();
         IBlockState ☃x = ☃.getBlockState(☃);
         if (☃x.getBlock() != this) {
            ☃.setBlockToAir(☃);
         } else if (☃ != this) {
            ☃x.neighborChanged(☃, ☃, ☃, ☃);
         }
      } else {
         boolean ☃ = false;
         BlockPos ☃x = ☃.up();
         IBlockState ☃xx = ☃.getBlockState(☃x);
         if (☃xx.getBlock() != this) {
            ☃.setBlockToAir(☃);
            ☃ = true;
         }

         if (!☃.getBlockState(☃.down()).isTopSolid()) {
            ☃.setBlockToAir(☃);
            ☃ = true;
            if (☃xx.getBlock() == this) {
               ☃.setBlockToAir(☃x);
            }
         }

         if (☃) {
            if (!☃.isRemote) {
               this.dropBlockAsItem(☃, ☃, ☃, 0);
            }
         } else {
            boolean ☃xxx = ☃.isBlockPowered(☃) || ☃.isBlockPowered(☃x);
            if (☃ != this && (☃xxx || ☃.getDefaultState().canProvidePower()) && ☃xxx != ☃xx.getValue(POWERED)) {
               ☃.setBlockState(☃x, ☃xx.withProperty(POWERED, ☃xxx), 2);
               if (☃xxx != ☃.getValue(OPEN)) {
                  ☃.setBlockState(☃, ☃.withProperty(OPEN, ☃xxx), 2);
                  ☃.markBlockRangeForRenderUpdate(☃, ☃);
                  ☃.playEvent(null, ☃xxx ? this.getOpenSound() : this.getCloseSound(), ☃, 0);
               }
            }
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return ☃.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : this.getItem();
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getY() >= 255 ? false : ☃.getBlockState(☃.down()).isTopSolid() && super.canPlaceBlockAt(☃, ☃) && super.canPlaceBlockAt(☃, ☃.up());
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.DESTROY;
   }

   public static int combineMetadata(IBlockAccess var0, BlockPos var1) {
      IBlockState ☃ = ☃.getBlockState(☃);
      int ☃x = ☃.getBlock().getMetaFromState(☃);
      boolean ☃xx = isTop(☃x);
      IBlockState ☃xxx = ☃.getBlockState(☃.down());
      int ☃xxxx = ☃xxx.getBlock().getMetaFromState(☃xxx);
      int ☃xxxxx = ☃xx ? ☃xxxx : ☃x;
      IBlockState ☃xxxxxx = ☃.getBlockState(☃.up());
      int ☃xxxxxxx = ☃xxxxxx.getBlock().getMetaFromState(☃xxxxxx);
      int ☃xxxxxxxx = ☃xx ? ☃x : ☃xxxxxxx;
      boolean ☃xxxxxxxxx = (☃xxxxxxxx & 1) != 0;
      boolean ☃xxxxxxxxxx = (☃xxxxxxxx & 2) != 0;
      return removeHalfBit(☃xxxxx) | (☃xx ? 8 : 0) | (☃xxxxxxxxx ? 16 : 0) | (☃xxxxxxxxxx ? 32 : 0);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this.getItem());
   }

   private Item getItem() {
      if (this == Blocks.IRON_DOOR) {
         return Items.IRON_DOOR;
      } else if (this == Blocks.SPRUCE_DOOR) {
         return Items.SPRUCE_DOOR;
      } else if (this == Blocks.BIRCH_DOOR) {
         return Items.BIRCH_DOOR;
      } else if (this == Blocks.JUNGLE_DOOR) {
         return Items.JUNGLE_DOOR;
      } else if (this == Blocks.ACACIA_DOOR) {
         return Items.ACACIA_DOOR;
      } else {
         return this == Blocks.DARK_OAK_DOOR ? Items.DARK_OAK_DOOR : Items.OAK_DOOR;
      }
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      BlockPos ☃ = ☃.down();
      BlockPos ☃x = ☃.up();
      if (☃.capabilities.isCreativeMode && ☃.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER && ☃.getBlockState(☃).getBlock() == this) {
         ☃.setBlockToAir(☃);
      }

      if (☃.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER && ☃.getBlockState(☃x).getBlock() == this) {
         if (☃.capabilities.isCreativeMode) {
            ☃.setBlockToAir(☃);
         }

         ☃.setBlockToAir(☃x);
      }
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER) {
         IBlockState ☃ = ☃.getBlockState(☃.up());
         if (☃.getBlock() == this) {
            ☃ = ☃.withProperty(HINGE, ☃.getValue(HINGE)).withProperty(POWERED, ☃.getValue(POWERED));
         }
      } else {
         IBlockState ☃ = ☃.getBlockState(☃.down());
         if (☃.getBlock() == this) {
            ☃ = ☃.withProperty(FACING, ☃.getValue(FACING)).withProperty(OPEN, ☃.getValue(OPEN));
         }
      }

      return ☃;
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.getValue(HALF) != BlockDoor.EnumDoorHalf.LOWER ? ☃ : ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃ == Mirror.NONE ? ☃ : ☃.withRotation(☃.toRotation(☃.getValue(FACING))).cycleProperty(HINGE);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return (☃ & 8) > 0
         ? this.getDefaultState()
            .withProperty(HALF, BlockDoor.EnumDoorHalf.UPPER)
            .withProperty(HINGE, (☃ & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT)
            .withProperty(POWERED, (☃ & 2) > 0)
         : this.getDefaultState()
            .withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER)
            .withProperty(FACING, EnumFacing.byHorizontalIndex(☃ & 3).rotateYCCW())
            .withProperty(OPEN, (☃ & 4) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      if (☃.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
         ☃ |= 8;
         if (☃.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT) {
            ☃ |= 1;
         }

         if (☃.getValue(POWERED)) {
            ☃ |= 2;
         }
      } else {
         ☃ |= ☃.getValue(FACING).rotateY().getHorizontalIndex();
         if (☃.getValue(OPEN)) {
            ☃ |= 4;
         }
      }

      return ☃;
   }

   protected static int removeHalfBit(int var0) {
      return ☃ & 7;
   }

   public static boolean isOpen(IBlockAccess var0, BlockPos var1) {
      return isOpen(combineMetadata(☃, ☃));
   }

   public static EnumFacing getFacing(IBlockAccess var0, BlockPos var1) {
      return getFacing(combineMetadata(☃, ☃));
   }

   public static EnumFacing getFacing(int var0) {
      return EnumFacing.byHorizontalIndex(☃ & 3).rotateYCCW();
   }

   protected static boolean isOpen(int var0) {
      return (☃ & 4) != 0;
   }

   protected static boolean isTop(int var0) {
      return (☃ & 8) != 0;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, HALF, FACING, OPEN, HINGE, POWERED);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   public static enum EnumDoorHalf implements IStringSerializable {
      UPPER,
      LOWER;

      @Override
      public String toString() {
         return this.getName();
      }

      @Override
      public String getName() {
         return this == UPPER ? "upper" : "lower";
      }
   }

   public static enum EnumHingePosition implements IStringSerializable {
      LEFT,
      RIGHT;

      @Override
      public String toString() {
         return this.getName();
      }

      @Override
      public String getName() {
         return this == LEFT ? "left" : "right";
      }
   }
}
