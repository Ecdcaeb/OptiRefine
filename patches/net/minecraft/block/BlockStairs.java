package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static final PropertyEnum<BlockStairs.EnumHalf> HALF = PropertyEnum.create("half", BlockStairs.EnumHalf.class);
   public static final PropertyEnum<BlockStairs.EnumShape> SHAPE = PropertyEnum.create("shape", BlockStairs.EnumShape.class);
   protected static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0, 0.5, 0.0, 0.5, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_QTR_TOP_EAST = new AxisAlignedBB(0.5, 0.5, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 0.5);
   protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0, 0.5, 0.5, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_OCT_TOP_NW = new AxisAlignedBB(0.0, 0.5, 0.0, 0.5, 1.0, 0.5);
   protected static final AxisAlignedBB AABB_OCT_TOP_NE = new AxisAlignedBB(0.5, 0.5, 0.0, 1.0, 1.0, 0.5);
   protected static final AxisAlignedBB AABB_OCT_TOP_SW = new AxisAlignedBB(0.0, 0.5, 0.5, 0.5, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_OCT_TOP_SE = new AxisAlignedBB(0.5, 0.5, 0.5, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
   protected static final AxisAlignedBB AABB_QTR_BOT_WEST = new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.5, 1.0);
   protected static final AxisAlignedBB AABB_QTR_BOT_EAST = new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.5, 1.0);
   protected static final AxisAlignedBB AABB_QTR_BOT_NORTH = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 0.5);
   protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH = new AxisAlignedBB(0.0, 0.0, 0.5, 1.0, 0.5, 1.0);
   protected static final AxisAlignedBB AABB_OCT_BOT_NW = new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 0.5, 0.5);
   protected static final AxisAlignedBB AABB_OCT_BOT_NE = new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 0.5, 0.5);
   protected static final AxisAlignedBB AABB_OCT_BOT_SW = new AxisAlignedBB(0.0, 0.0, 0.5, 0.5, 0.5, 1.0);
   protected static final AxisAlignedBB AABB_OCT_BOT_SE = new AxisAlignedBB(0.5, 0.0, 0.5, 1.0, 0.5, 1.0);
   private final Block modelBlock;
   private final IBlockState modelState;

   protected BlockStairs(IBlockState var1) {
      super(☃.getBlock().material);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(HALF, BlockStairs.EnumHalf.BOTTOM)
            .withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT)
      );
      this.modelBlock = ☃.getBlock();
      this.modelState = ☃;
      this.setHardness(this.modelBlock.blockHardness);
      this.setResistance(this.modelBlock.blockResistance / 3.0F);
      this.setSoundType(this.modelBlock.blockSoundType);
      this.setLightOpacity(255);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      if (!☃) {
         ☃ = this.getActualState(☃, ☃, ☃);
      }

      for (AxisAlignedBB ☃ : getCollisionBoxList(☃)) {
         addCollisionBoxToList(☃, ☃, ☃, ☃);
      }
   }

   private static List<AxisAlignedBB> getCollisionBoxList(IBlockState var0) {
      List<AxisAlignedBB> ☃ = Lists.newArrayList();
      boolean ☃x = ☃.getValue(HALF) == BlockStairs.EnumHalf.TOP;
      ☃.add(☃x ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
      BlockStairs.EnumShape ☃xx = ☃.getValue(SHAPE);
      if (☃xx == BlockStairs.EnumShape.STRAIGHT || ☃xx == BlockStairs.EnumShape.INNER_LEFT || ☃xx == BlockStairs.EnumShape.INNER_RIGHT) {
         ☃.add(getCollQuarterBlock(☃));
      }

      if (☃xx != BlockStairs.EnumShape.STRAIGHT) {
         ☃.add(getCollEighthBlock(☃));
      }

      return ☃;
   }

   private static AxisAlignedBB getCollQuarterBlock(IBlockState var0) {
      boolean ☃ = ☃.getValue(HALF) == BlockStairs.EnumHalf.TOP;
      switch ((EnumFacing)☃.getValue(FACING)) {
         case NORTH:
         default:
            return ☃ ? AABB_QTR_BOT_NORTH : AABB_QTR_TOP_NORTH;
         case SOUTH:
            return ☃ ? AABB_QTR_BOT_SOUTH : AABB_QTR_TOP_SOUTH;
         case WEST:
            return ☃ ? AABB_QTR_BOT_WEST : AABB_QTR_TOP_WEST;
         case EAST:
            return ☃ ? AABB_QTR_BOT_EAST : AABB_QTR_TOP_EAST;
      }
   }

   private static AxisAlignedBB getCollEighthBlock(IBlockState var0) {
      EnumFacing ☃ = ☃.getValue(FACING);
      EnumFacing ☃x;
      switch ((BlockStairs.EnumShape)☃.getValue(SHAPE)) {
         case OUTER_LEFT:
         default:
            ☃x = ☃;
            break;
         case OUTER_RIGHT:
            ☃x = ☃.rotateY();
            break;
         case INNER_RIGHT:
            ☃x = ☃.getOpposite();
            break;
         case INNER_LEFT:
            ☃x = ☃.rotateYCCW();
      }

      boolean ☃ = ☃.getValue(HALF) == BlockStairs.EnumHalf.TOP;
      switch (☃x) {
         case NORTH:
         default:
            return ☃ ? AABB_OCT_BOT_NW : AABB_OCT_TOP_NW;
         case SOUTH:
            return ☃ ? AABB_OCT_BOT_SE : AABB_OCT_TOP_SE;
         case WEST:
            return ☃ ? AABB_OCT_BOT_SW : AABB_OCT_TOP_SW;
         case EAST:
            return ☃ ? AABB_OCT_BOT_NE : AABB_OCT_TOP_NE;
      }
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      ☃ = this.getActualState(☃, ☃, ☃);
      if (☃.getAxis() == EnumFacing.Axis.Y) {
         return ☃ == EnumFacing.UP == (☃.getValue(HALF) == BlockStairs.EnumHalf.TOP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
      } else {
         BlockStairs.EnumShape ☃ = ☃.getValue(SHAPE);
         if (☃ != BlockStairs.EnumShape.OUTER_LEFT && ☃ != BlockStairs.EnumShape.OUTER_RIGHT) {
            EnumFacing ☃x = ☃.getValue(FACING);
            switch (☃) {
               case INNER_RIGHT:
                  return ☃x != ☃ && ☃x != ☃.rotateYCCW() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
               case INNER_LEFT:
                  return ☃x != ☃ && ☃x != ☃.rotateY() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
               case STRAIGHT:
                  return ☃x == ☃ ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
               default:
                  return BlockFaceShape.UNDEFINED;
            }
         } else {
            return BlockFaceShape.UNDEFINED;
         }
      }
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
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      this.modelBlock.randomDisplayTick(☃, ☃, ☃, ☃);
   }

   @Override
   public void onBlockClicked(World var1, BlockPos var2, EntityPlayer var3) {
      this.modelBlock.onBlockClicked(☃, ☃, ☃);
   }

   @Override
   public void onPlayerDestroy(World var1, BlockPos var2, IBlockState var3) {
      this.modelBlock.onPlayerDestroy(☃, ☃, ☃);
   }

   @Override
   public int getPackedLightmapCoords(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return this.modelState.getPackedLightmapCoords(☃, ☃);
   }

   @Override
   public float getExplosionResistance(Entity var1) {
      return this.modelBlock.getExplosionResistance(☃);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return this.modelBlock.getRenderLayer();
   }

   @Override
   public int tickRate(World var1) {
      return this.modelBlock.tickRate(☃);
   }

   @Override
   public AxisAlignedBB getSelectedBoundingBox(IBlockState var1, World var2, BlockPos var3) {
      return this.modelState.getSelectedBoundingBox(☃, ☃);
   }

   @Override
   public Vec3d modifyAcceleration(World var1, BlockPos var2, Entity var3, Vec3d var4) {
      return this.modelBlock.modifyAcceleration(☃, ☃, ☃, ☃);
   }

   @Override
   public boolean isCollidable() {
      return this.modelBlock.isCollidable();
   }

   @Override
   public boolean canCollideCheck(IBlockState var1, boolean var2) {
      return this.modelBlock.canCollideCheck(☃, ☃);
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return this.modelBlock.canPlaceBlockAt(☃, ☃);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.modelState.neighborChanged(☃, ☃, Blocks.AIR, ☃);
      this.modelBlock.onBlockAdded(☃, ☃, this.modelState);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      this.modelBlock.breakBlock(☃, ☃, this.modelState);
   }

   @Override
   public void onEntityWalk(World var1, BlockPos var2, Entity var3) {
      this.modelBlock.onEntityWalk(☃, ☃, ☃);
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      this.modelBlock.updateTick(☃, ☃, ☃, ☃);
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      return this.modelBlock.onBlockActivated(☃, ☃, this.modelState, ☃, ☃, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
   }

   @Override
   public void onExplosionDestroy(World var1, BlockPos var2, Explosion var3) {
      this.modelBlock.onExplosionDestroy(☃, ☃, ☃);
   }

   @Override
   public boolean isTopSolid(IBlockState var1) {
      return ☃.getValue(HALF) == BlockStairs.EnumHalf.TOP;
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return this.modelBlock.getMapColor(this.modelState, ☃, ☃);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState ☃ = super.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      ☃ = ☃.withProperty(FACING, ☃.getHorizontalFacing()).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
      return ☃ != EnumFacing.DOWN && (☃ == EnumFacing.UP || !(☃ > 0.5))
         ? ☃.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM)
         : ☃.withProperty(HALF, BlockStairs.EnumHalf.TOP);
   }

   @Nullable
   @Override
   public RayTraceResult collisionRayTrace(IBlockState var1, World var2, BlockPos var3, Vec3d var4, Vec3d var5) {
      List<RayTraceResult> ☃ = Lists.newArrayList();

      for (AxisAlignedBB ☃x : getCollisionBoxList(this.getActualState(☃, ☃, ☃))) {
         ☃.add(this.rayTrace(☃, ☃, ☃, ☃x));
      }

      RayTraceResult ☃x = null;
      double ☃xx = 0.0;

      for (RayTraceResult ☃xxx : ☃) {
         if (☃xxx != null) {
            double ☃xxxx = ☃xxx.hitVec.squareDistanceTo(☃);
            if (☃xxxx > ☃xx) {
               ☃x = ☃xxx;
               ☃xx = ☃xxxx;
            }
         }
      }

      return ☃x;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState().withProperty(HALF, (☃ & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
      return ☃.withProperty(FACING, EnumFacing.byIndex(5 - (☃ & 3)));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      if (☃.getValue(HALF) == BlockStairs.EnumHalf.TOP) {
         ☃ |= 4;
      }

      return ☃ | 5 - ☃.getValue(FACING).getIndex();
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.withProperty(SHAPE, getStairsShape(☃, ☃, ☃));
   }

   private static BlockStairs.EnumShape getStairsShape(IBlockState var0, IBlockAccess var1, BlockPos var2) {
      EnumFacing ☃ = ☃.getValue(FACING);
      IBlockState ☃x = ☃.getBlockState(☃.offset(☃));
      if (isBlockStairs(☃x) && ☃.getValue(HALF) == ☃x.getValue(HALF)) {
         EnumFacing ☃xx = ☃x.getValue(FACING);
         if (☃xx.getAxis() != ☃.getValue(FACING).getAxis() && isDifferentStairs(☃, ☃, ☃, ☃xx.getOpposite())) {
            if (☃xx == ☃.rotateYCCW()) {
               return BlockStairs.EnumShape.OUTER_LEFT;
            }

            return BlockStairs.EnumShape.OUTER_RIGHT;
         }
      }

      IBlockState ☃xx = ☃.getBlockState(☃.offset(☃.getOpposite()));
      if (isBlockStairs(☃xx) && ☃.getValue(HALF) == ☃xx.getValue(HALF)) {
         EnumFacing ☃xxx = ☃xx.getValue(FACING);
         if (☃xxx.getAxis() != ☃.getValue(FACING).getAxis() && isDifferentStairs(☃, ☃, ☃, ☃xxx)) {
            if (☃xxx == ☃.rotateYCCW()) {
               return BlockStairs.EnumShape.INNER_LEFT;
            }

            return BlockStairs.EnumShape.INNER_RIGHT;
         }
      }

      return BlockStairs.EnumShape.STRAIGHT;
   }

   private static boolean isDifferentStairs(IBlockState var0, IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      IBlockState ☃ = ☃.getBlockState(☃.offset(☃));
      return !isBlockStairs(☃) || ☃.getValue(FACING) != ☃.getValue(FACING) || ☃.getValue(HALF) != ☃.getValue(HALF);
   }

   public static boolean isBlockStairs(IBlockState var0) {
      return ☃.getBlock() instanceof BlockStairs;
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      EnumFacing ☃ = ☃.getValue(FACING);
      BlockStairs.EnumShape ☃x = ☃.getValue(SHAPE);
      switch (☃) {
         case LEFT_RIGHT:
            if (☃.getAxis() == EnumFacing.Axis.Z) {
               switch (☃x) {
                  case OUTER_LEFT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
                  case OUTER_RIGHT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
                  case INNER_RIGHT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                  case INNER_LEFT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                  default:
                     return ☃.withRotation(Rotation.CLOCKWISE_180);
               }
            }
            break;
         case FRONT_BACK:
            if (☃.getAxis() == EnumFacing.Axis.X) {
               switch (☃x) {
                  case OUTER_LEFT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
                  case OUTER_RIGHT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
                  case INNER_RIGHT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                  case INNER_LEFT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                  case STRAIGHT:
                     return ☃.withRotation(Rotation.CLOCKWISE_180);
               }
            }
      }

      return super.withMirror(☃, ☃);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, HALF, SHAPE);
   }

   public static enum EnumHalf implements IStringSerializable {
      TOP("top"),
      BOTTOM("bottom");

      private final String name;

      private EnumHalf(String var3) {
         this.name = ☃;
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }
   }

   public static enum EnumShape implements IStringSerializable {
      STRAIGHT("straight"),
      INNER_LEFT("inner_left"),
      INNER_RIGHT("inner_right"),
      OUTER_LEFT("outer_left"),
      OUTER_RIGHT("outer_right");

      private final String name;

      private EnumShape(String var3) {
         this.name = ☃;
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }
   }
}
