package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyBool ATTACHED = PropertyBool.create("attached");
   public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0625, 0.0, 1.0, 0.15625, 1.0);
   protected static final AxisAlignedBB TRIP_WRITE_ATTACHED_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);

   public BlockTripWire() {
      super(Material.CIRCUITS);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(POWERED, false)
            .withProperty(ATTACHED, false)
            .withProperty(DISARMED, false)
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
      );
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return !☃.getValue(ATTACHED) ? TRIP_WRITE_ATTACHED_AABB : AABB;
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.withProperty(NORTH, isConnectedTo(☃, ☃, ☃, EnumFacing.NORTH))
         .withProperty(EAST, isConnectedTo(☃, ☃, ☃, EnumFacing.EAST))
         .withProperty(SOUTH, isConnectedTo(☃, ☃, ☃, EnumFacing.SOUTH))
         .withProperty(WEST, isConnectedTo(☃, ☃, ☃, EnumFacing.WEST));
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
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.STRING;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.STRING);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      ☃.setBlockState(☃, ☃, 3);
      this.notifyHook(☃, ☃, ☃);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      this.notifyHook(☃, ☃, ☃.withProperty(POWERED, true));
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (!☃.isRemote) {
         if (!☃.getHeldItemMainhand().isEmpty() && ☃.getHeldItemMainhand().getItem() == Items.SHEARS) {
            ☃.setBlockState(☃, ☃.withProperty(DISARMED, true), 4);
         }
      }
   }

   private void notifyHook(World var1, BlockPos var2, IBlockState var3) {
      for (EnumFacing ☃ : new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST}) {
         for (int ☃x = 1; ☃x < 42; ☃x++) {
            BlockPos ☃xx = ☃.offset(☃, ☃x);
            IBlockState ☃xxx = ☃.getBlockState(☃xx);
            if (☃xxx.getBlock() == Blocks.TRIPWIRE_HOOK) {
               if (☃xxx.getValue(BlockTripWireHook.FACING) == ☃.getOpposite()) {
                  Blocks.TRIPWIRE_HOOK.calculateState(☃, ☃xx, ☃xxx, false, true, ☃x, ☃);
               }
               break;
            }

            if (☃xxx.getBlock() != Blocks.TRIPWIRE) {
               break;
            }
         }
      }
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!☃.isRemote) {
         if (!☃.getValue(POWERED)) {
            this.updateState(☃, ☃);
         }
      }
   }

   @Override
   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         if (☃.getBlockState(☃).getValue(POWERED)) {
            this.updateState(☃, ☃);
         }
      }
   }

   private void updateState(World var1, BlockPos var2) {
      IBlockState ☃ = ☃.getBlockState(☃);
      boolean ☃x = ☃.getValue(POWERED);
      boolean ☃xx = false;
      List<? extends Entity> ☃xxx = ☃.getEntitiesWithinAABBExcludingEntity(null, ☃.getBoundingBox(☃, ☃).offset(☃));
      if (!☃xxx.isEmpty()) {
         for (Entity ☃xxxx : ☃xxx) {
            if (!☃xxxx.doesEntityNotTriggerPressurePlate()) {
               ☃xx = true;
               break;
            }
         }
      }

      if (☃xx != ☃x) {
         ☃ = ☃.withProperty(POWERED, ☃xx);
         ☃.setBlockState(☃, ☃, 3);
         this.notifyHook(☃, ☃, ☃);
      }

      if (☃xx) {
         ☃.scheduleUpdate(new BlockPos(☃), this, this.tickRate(☃));
      }
   }

   public static boolean isConnectedTo(IBlockAccess var0, BlockPos var1, IBlockState var2, EnumFacing var3) {
      BlockPos ☃ = ☃.offset(☃);
      IBlockState ☃x = ☃.getBlockState(☃);
      Block ☃xx = ☃x.getBlock();
      if (☃xx == Blocks.TRIPWIRE_HOOK) {
         EnumFacing ☃xxx = ☃.getOpposite();
         return ☃x.getValue(BlockTripWireHook.FACING) == ☃xxx;
      } else {
         return ☃xx == Blocks.TRIPWIRE;
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWERED, (☃ & 1) > 0).withProperty(ATTACHED, (☃ & 4) > 0).withProperty(DISARMED, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      if (☃.getValue(POWERED)) {
         ☃ |= 1;
      }

      if (☃.getValue(ATTACHED)) {
         ☃ |= 4;
      }

      if (☃.getValue(DISARMED)) {
         ☃ |= 8;
      }

      return ☃;
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
      return new BlockStateContainer(this, POWERED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
