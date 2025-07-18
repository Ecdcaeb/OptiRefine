package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal extends BlockBreakable {
   public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z);
   protected static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 0.625);
   protected static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 1.0);
   protected static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);

   public BlockPortal() {
      super(Material.PORTAL, false);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((EnumFacing.Axis)☃.getValue(AXIS)) {
         case X:
            return X_AABB;
         case Y:
         default:
            return Y_AABB;
         case Z:
            return Z_AABB;
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      super.updateTick(☃, ☃, ☃, ☃);
      if (☃.provider.isSurfaceWorld() && ☃.getGameRules().getBoolean("doMobSpawning") && ☃.nextInt(2000) < ☃.getDifficulty().getId()) {
         int ☃ = ☃.getY();
         BlockPos ☃x = ☃;

         while (!☃.getBlockState(☃x).isTopSolid() && ☃x.getY() > 0) {
            ☃x = ☃x.down();
         }

         if (☃ > 0 && !☃.getBlockState(☃x.up()).isNormalCube()) {
            Entity ☃xx = ItemMonsterPlacer.spawnCreature(☃, EntityList.getKey(EntityPigZombie.class), ☃x.getX() + 0.5, ☃x.getY() + 1.1, ☃x.getZ() + 0.5);
            if (☃xx != null) {
               ☃xx.timeUntilPortal = ☃xx.getPortalCooldown();
            }
         }
      }
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   public static int getMetaForAxis(EnumFacing.Axis var0) {
      if (☃ == EnumFacing.Axis.X) {
         return 1;
      } else {
         return ☃ == EnumFacing.Axis.Z ? 2 : 0;
      }
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   public boolean trySpawnPortal(World var1, BlockPos var2) {
      BlockPortal.Size ☃ = new BlockPortal.Size(☃, ☃, EnumFacing.Axis.X);
      if (☃.isValid() && ☃.portalBlockCount == 0) {
         ☃.placePortalBlocks();
         return true;
      } else {
         BlockPortal.Size ☃x = new BlockPortal.Size(☃, ☃, EnumFacing.Axis.Z);
         if (☃x.isValid() && ☃x.portalBlockCount == 0) {
            ☃x.placePortalBlocks();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      EnumFacing.Axis ☃ = ☃.getValue(AXIS);
      if (☃ == EnumFacing.Axis.X) {
         BlockPortal.Size ☃x = new BlockPortal.Size(☃, ☃, EnumFacing.Axis.X);
         if (!☃x.isValid() || ☃x.portalBlockCount < ☃x.width * ☃x.height) {
            ☃.setBlockState(☃, Blocks.AIR.getDefaultState());
         }
      } else if (☃ == EnumFacing.Axis.Z) {
         BlockPortal.Size ☃x = new BlockPortal.Size(☃, ☃, EnumFacing.Axis.Z);
         if (!☃x.isValid() || ☃x.portalBlockCount < ☃x.width * ☃x.height) {
            ☃.setBlockState(☃, Blocks.AIR.getDefaultState());
         }
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      ☃ = ☃.offset(☃);
      EnumFacing.Axis ☃ = null;
      if (☃.getBlock() == this) {
         ☃ = ☃.getValue(AXIS);
         if (☃ == null) {
            return false;
         }

         if (☃ == EnumFacing.Axis.Z && ☃ != EnumFacing.EAST && ☃ != EnumFacing.WEST) {
            return false;
         }

         if (☃ == EnumFacing.Axis.X && ☃ != EnumFacing.SOUTH && ☃ != EnumFacing.NORTH) {
            return false;
         }
      }

      boolean ☃x = ☃.getBlockState(☃.west()).getBlock() == this && ☃.getBlockState(☃.west(2)).getBlock() != this;
      boolean ☃xx = ☃.getBlockState(☃.east()).getBlock() == this && ☃.getBlockState(☃.east(2)).getBlock() != this;
      boolean ☃xxx = ☃.getBlockState(☃.north()).getBlock() == this && ☃.getBlockState(☃.north(2)).getBlock() != this;
      boolean ☃xxxx = ☃.getBlockState(☃.south()).getBlock() == this && ☃.getBlockState(☃.south(2)).getBlock() != this;
      boolean ☃xxxxx = ☃x || ☃xx || ☃ == EnumFacing.Axis.X;
      boolean ☃xxxxxx = ☃xxx || ☃xxxx || ☃ == EnumFacing.Axis.Z;
      if (☃xxxxx && ☃ == EnumFacing.WEST) {
         return true;
      } else if (☃xxxxx && ☃ == EnumFacing.EAST) {
         return true;
      } else {
         return ☃xxxxxx && ☃ == EnumFacing.NORTH ? true : ☃xxxxxx && ☃ == EnumFacing.SOUTH;
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!☃.isRiding() && !☃.isBeingRidden() && ☃.isNonBoss()) {
         ☃.setPortal(☃);
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (☃.nextInt(100) == 0) {
         ☃.playSound(
            ☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, ☃.nextFloat() * 0.4F + 0.8F, false
         );
      }

      for (int ☃ = 0; ☃ < 4; ☃++) {
         double ☃x = ☃.getX() + ☃.nextFloat();
         double ☃xx = ☃.getY() + ☃.nextFloat();
         double ☃xxx = ☃.getZ() + ☃.nextFloat();
         double ☃xxxx = (☃.nextFloat() - 0.5) * 0.5;
         double ☃xxxxx = (☃.nextFloat() - 0.5) * 0.5;
         double ☃xxxxxx = (☃.nextFloat() - 0.5) * 0.5;
         int ☃xxxxxxx = ☃.nextInt(2) * 2 - 1;
         if (☃.getBlockState(☃.west()).getBlock() != this && ☃.getBlockState(☃.east()).getBlock() != this) {
            ☃x = ☃.getX() + 0.5 + 0.25 * ☃xxxxxxx;
            ☃xxxx = ☃.nextFloat() * 2.0F * ☃xxxxxxx;
         } else {
            ☃xxx = ☃.getZ() + 0.5 + 0.25 * ☃xxxxxxx;
            ☃xxxxxx = ☃.nextFloat() * 2.0F * ☃xxxxxxx;
         }

         ☃.spawnParticle(EnumParticleTypes.PORTAL, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx);
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return ItemStack.EMPTY;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(AXIS, (☃ & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return getMetaForAxis(☃.getValue(AXIS));
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case COUNTERCLOCKWISE_90:
         case CLOCKWISE_90:
            switch ((EnumFacing.Axis)☃.getValue(AXIS)) {
               case X:
                  return ☃.withProperty(AXIS, EnumFacing.Axis.Z);
               case Z:
                  return ☃.withProperty(AXIS, EnumFacing.Axis.X);
               default:
                  return ☃;
            }
         default:
            return ☃;
      }
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, AXIS);
   }

   public BlockPattern.PatternHelper createPatternHelper(World var1, BlockPos var2) {
      EnumFacing.Axis ☃ = EnumFacing.Axis.Z;
      BlockPortal.Size ☃x = new BlockPortal.Size(☃, ☃, EnumFacing.Axis.X);
      LoadingCache<BlockPos, BlockWorldState> ☃xx = BlockPattern.createLoadingCache(☃, true);
      if (!☃x.isValid()) {
         ☃ = EnumFacing.Axis.X;
         ☃x = new BlockPortal.Size(☃, ☃, EnumFacing.Axis.Z);
      }

      if (!☃x.isValid()) {
         return new BlockPattern.PatternHelper(☃, EnumFacing.NORTH, EnumFacing.UP, ☃xx, 1, 1, 1);
      } else {
         int[] ☃xxx = new int[EnumFacing.AxisDirection.values().length];
         EnumFacing ☃xxxx = ☃x.rightDir.rotateYCCW();
         BlockPos ☃xxxxx = ☃x.bottomLeft.up(☃x.getHeight() - 1);

         for (EnumFacing.AxisDirection ☃xxxxxx : EnumFacing.AxisDirection.values()) {
            BlockPattern.PatternHelper ☃xxxxxxx = new BlockPattern.PatternHelper(
               ☃xxxx.getAxisDirection() == ☃xxxxxx ? ☃xxxxx : ☃xxxxx.offset(☃x.rightDir, ☃x.getWidth() - 1),
               EnumFacing.getFacingFromAxis(☃xxxxxx, ☃),
               EnumFacing.UP,
               ☃xx,
               ☃x.getWidth(),
               ☃x.getHeight(),
               1
            );

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃x.getWidth(); ☃xxxxxxxx++) {
               for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃x.getHeight(); ☃xxxxxxxxx++) {
                  BlockWorldState ☃xxxxxxxxxx = ☃xxxxxxx.translateOffset(☃xxxxxxxx, ☃xxxxxxxxx, 1);
                  if (☃xxxxxxxxxx.getBlockState() != null && ☃xxxxxxxxxx.getBlockState().getMaterial() != Material.AIR) {
                     ☃xxx[☃xxxxxx.ordinal()]++;
                  }
               }
            }
         }

         EnumFacing.AxisDirection ☃xxxxxx = EnumFacing.AxisDirection.POSITIVE;

         for (EnumFacing.AxisDirection ☃xxxxxxx : EnumFacing.AxisDirection.values()) {
            if (☃xxx[☃xxxxxxx.ordinal()] < ☃xxx[☃xxxxxx.ordinal()]) {
               ☃xxxxxx = ☃xxxxxxx;
            }
         }

         return new BlockPattern.PatternHelper(
            ☃xxxx.getAxisDirection() == ☃xxxxxx ? ☃xxxxx : ☃xxxxx.offset(☃x.rightDir, ☃x.getWidth() - 1),
            EnumFacing.getFacingFromAxis(☃xxxxxx, ☃),
            EnumFacing.UP,
            ☃xx,
            ☃x.getWidth(),
            ☃x.getHeight(),
            1
         );
      }
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   public static class Size {
      private final World world;
      private final EnumFacing.Axis axis;
      private final EnumFacing rightDir;
      private final EnumFacing leftDir;
      private int portalBlockCount;
      private BlockPos bottomLeft;
      private int height;
      private int width;

      public Size(World var1, BlockPos var2, EnumFacing.Axis var3) {
         this.world = ☃;
         this.axis = ☃;
         if (☃ == EnumFacing.Axis.X) {
            this.leftDir = EnumFacing.EAST;
            this.rightDir = EnumFacing.WEST;
         } else {
            this.leftDir = EnumFacing.NORTH;
            this.rightDir = EnumFacing.SOUTH;
         }

         BlockPos ☃ = ☃;

         while (☃.getY() > ☃.getY() - 21 && ☃.getY() > 0 && this.isEmptyBlock(☃.getBlockState(☃.down()).getBlock())) {
            ☃ = ☃.down();
         }

         int ☃x = this.getDistanceUntilEdge(☃, this.leftDir) - 1;
         if (☃x >= 0) {
            this.bottomLeft = ☃.offset(this.leftDir, ☃x);
            this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
            if (this.width < 2 || this.width > 21) {
               this.bottomLeft = null;
               this.width = 0;
            }
         }

         if (this.bottomLeft != null) {
            this.height = this.calculatePortalHeight();
         }
      }

      protected int getDistanceUntilEdge(BlockPos var1, EnumFacing var2) {
         int ☃;
         for (☃ = 0; ☃ < 22; ☃++) {
            BlockPos ☃x = ☃.offset(☃, ☃);
            if (!this.isEmptyBlock(this.world.getBlockState(☃x).getBlock()) || this.world.getBlockState(☃x.down()).getBlock() != Blocks.OBSIDIAN) {
               break;
            }
         }

         Block ☃x = this.world.getBlockState(☃.offset(☃, ☃)).getBlock();
         return ☃x == Blocks.OBSIDIAN ? ☃ : 0;
      }

      public int getHeight() {
         return this.height;
      }

      public int getWidth() {
         return this.width;
      }

      protected int calculatePortalHeight() {
         label56:
         for (this.height = 0; this.height < 21; this.height++) {
            for (int ☃ = 0; ☃ < this.width; ☃++) {
               BlockPos ☃x = this.bottomLeft.offset(this.rightDir, ☃).up(this.height);
               Block ☃xx = this.world.getBlockState(☃x).getBlock();
               if (!this.isEmptyBlock(☃xx)) {
                  break label56;
               }

               if (☃xx == Blocks.PORTAL) {
                  this.portalBlockCount++;
               }

               if (☃ == 0) {
                  ☃xx = this.world.getBlockState(☃x.offset(this.leftDir)).getBlock();
                  if (☃xx != Blocks.OBSIDIAN) {
                     break label56;
                  }
               } else if (☃ == this.width - 1) {
                  ☃xx = this.world.getBlockState(☃x.offset(this.rightDir)).getBlock();
                  if (☃xx != Blocks.OBSIDIAN) {
                     break label56;
                  }
               }
            }
         }

         for (int ☃ = 0; ☃ < this.width; ☃++) {
            if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, ☃).up(this.height)).getBlock() != Blocks.OBSIDIAN) {
               this.height = 0;
               break;
            }
         }

         if (this.height <= 21 && this.height >= 3) {
            return this.height;
         } else {
            this.bottomLeft = null;
            this.width = 0;
            this.height = 0;
            return 0;
         }
      }

      protected boolean isEmptyBlock(Block var1) {
         return ☃.material == Material.AIR || ☃ == Blocks.FIRE || ☃ == Blocks.PORTAL;
      }

      public boolean isValid() {
         return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
      }

      public void placePortalBlocks() {
         for (int ☃ = 0; ☃ < this.width; ☃++) {
            BlockPos ☃x = this.bottomLeft.offset(this.rightDir, ☃);

            for (int ☃xx = 0; ☃xx < this.height; ☃xx++) {
               this.world.setBlockState(☃x.up(☃xx), Blocks.PORTAL.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
            }
         }
      }
   }
}
