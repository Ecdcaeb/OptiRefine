package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityPiston extends TileEntity implements ITickable {
   private IBlockState pistonState;
   private EnumFacing pistonFacing;
   private boolean extending;
   private boolean shouldHeadBeRendered;
   private static final ThreadLocal<EnumFacing> MOVING_ENTITY = new ThreadLocal<EnumFacing>() {
      protected EnumFacing initialValue() {
         return null;
      }
   };
   private float progress;
   private float lastProgress;

   public TileEntityPiston() {
   }

   public TileEntityPiston(IBlockState var1, EnumFacing var2, boolean var3, boolean var4) {
      this.pistonState = ☃;
      this.pistonFacing = ☃;
      this.extending = ☃;
      this.shouldHeadBeRendered = ☃;
   }

   public IBlockState getPistonState() {
      return this.pistonState;
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   @Override
   public int getBlockMetadata() {
      return 0;
   }

   public boolean isExtending() {
      return this.extending;
   }

   public EnumFacing getFacing() {
      return this.pistonFacing;
   }

   public boolean shouldPistonHeadBeRendered() {
      return this.shouldHeadBeRendered;
   }

   public float getProgress(float var1) {
      if (☃ > 1.0F) {
         ☃ = 1.0F;
      }

      return this.lastProgress + (this.progress - this.lastProgress) * ☃;
   }

   public float getOffsetX(float var1) {
      return this.pistonFacing.getXOffset() * this.getExtendedProgress(this.getProgress(☃));
   }

   public float getOffsetY(float var1) {
      return this.pistonFacing.getYOffset() * this.getExtendedProgress(this.getProgress(☃));
   }

   public float getOffsetZ(float var1) {
      return this.pistonFacing.getZOffset() * this.getExtendedProgress(this.getProgress(☃));
   }

   private float getExtendedProgress(float var1) {
      return this.extending ? ☃ - 1.0F : 1.0F - ☃;
   }

   public AxisAlignedBB getAABB(IBlockAccess var1, BlockPos var2) {
      return this.getAABB(☃, ☃, this.progress).union(this.getAABB(☃, ☃, this.lastProgress));
   }

   public AxisAlignedBB getAABB(IBlockAccess var1, BlockPos var2, float var3) {
      ☃ = this.getExtendedProgress(☃);
      IBlockState ☃ = this.getCollisionRelatedBlockState();
      return ☃.getBoundingBox(☃, ☃).offset(☃ * this.pistonFacing.getXOffset(), ☃ * this.pistonFacing.getYOffset(), ☃ * this.pistonFacing.getZOffset());
   }

   private IBlockState getCollisionRelatedBlockState() {
      return !this.isExtending() && this.shouldPistonHeadBeRendered()
         ? Blocks.PISTON_HEAD
            .getDefaultState()
            .withProperty(
               BlockPistonExtension.TYPE,
               this.pistonState.getBlock() == Blocks.STICKY_PISTON ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT
            )
            .withProperty(BlockPistonExtension.FACING, this.pistonState.getValue(BlockPistonBase.FACING))
         : this.pistonState;
   }

   private void moveCollidedEntities(float var1) {
      EnumFacing ☃ = this.extending ? this.pistonFacing : this.pistonFacing.getOpposite();
      double ☃x = ☃ - this.progress;
      List<AxisAlignedBB> ☃xx = Lists.newArrayList();
      this.getCollisionRelatedBlockState().addCollisionBoxToList(this.world, BlockPos.ORIGIN, new AxisAlignedBB(BlockPos.ORIGIN), ☃xx, null, true);
      if (!☃xx.isEmpty()) {
         AxisAlignedBB ☃xxx = this.moveByPositionAndProgress(this.getMinMaxPiecesAABB(☃xx));
         List<Entity> ☃xxxx = this.world.getEntitiesWithinAABBExcludingEntity(null, this.getMovementArea(☃xxx, ☃, ☃x).union(☃xxx));
         if (!☃xxxx.isEmpty()) {
            boolean ☃xxxxx = this.pistonState.getBlock() == Blocks.SLIME_BLOCK;

            for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxx.size(); ☃xxxxxx++) {
               Entity ☃xxxxxxx = ☃xxxx.get(☃xxxxxx);
               if (☃xxxxxxx.getPushReaction() != EnumPushReaction.IGNORE) {
                  if (☃xxxxx) {
                     switch (☃.getAxis()) {
                        case X:
                           ☃xxxxxxx.motionX = ☃.getXOffset();
                           break;
                        case Y:
                           ☃xxxxxxx.motionY = ☃.getYOffset();
                           break;
                        case Z:
                           ☃xxxxxxx.motionZ = ☃.getZOffset();
                     }
                  }

                  double ☃xxxxxxxx = 0.0;

                  for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃xx.size(); ☃xxxxxxxxx++) {
                     AxisAlignedBB ☃xxxxxxxxxx = this.getMovementArea(this.moveByPositionAndProgress(☃xx.get(☃xxxxxxxxx)), ☃, ☃x);
                     AxisAlignedBB ☃xxxxxxxxxxx = ☃xxxxxxx.getEntityBoundingBox();
                     if (☃xxxxxxxxxx.intersects(☃xxxxxxxxxxx)) {
                        ☃xxxxxxxx = Math.max(☃xxxxxxxx, this.getMovement(☃xxxxxxxxxx, ☃, ☃xxxxxxxxxxx));
                        if (☃xxxxxxxx >= ☃x) {
                           break;
                        }
                     }
                  }

                  if (!(☃xxxxxxxx <= 0.0)) {
                     ☃xxxxxxxx = Math.min(☃xxxxxxxx, ☃x) + 0.01;
                     MOVING_ENTITY.set(☃);
                     ☃xxxxxxx.move(MoverType.PISTON, ☃xxxxxxxx * ☃.getXOffset(), ☃xxxxxxxx * ☃.getYOffset(), ☃xxxxxxxx * ☃.getZOffset());
                     MOVING_ENTITY.set(null);
                     if (!this.extending && this.shouldHeadBeRendered) {
                        this.fixEntityWithinPistonBase(☃xxxxxxx, ☃, ☃x);
                     }
                  }
               }
            }
         }
      }
   }

   private AxisAlignedBB getMinMaxPiecesAABB(List<AxisAlignedBB> var1) {
      double ☃ = 0.0;
      double ☃x = 0.0;
      double ☃xx = 0.0;
      double ☃xxx = 1.0;
      double ☃xxxx = 1.0;
      double ☃xxxxx = 1.0;

      for (AxisAlignedBB ☃xxxxxx : ☃) {
         ☃ = Math.min(☃xxxxxx.minX, ☃);
         ☃x = Math.min(☃xxxxxx.minY, ☃x);
         ☃xx = Math.min(☃xxxxxx.minZ, ☃xx);
         ☃xxx = Math.max(☃xxxxxx.maxX, ☃xxx);
         ☃xxxx = Math.max(☃xxxxxx.maxY, ☃xxxx);
         ☃xxxxx = Math.max(☃xxxxxx.maxZ, ☃xxxxx);
      }

      return new AxisAlignedBB(☃, ☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
   }

   private double getMovement(AxisAlignedBB var1, EnumFacing var2, AxisAlignedBB var3) {
      switch (☃.getAxis()) {
         case X:
            return getDeltaX(☃, ☃, ☃);
         case Y:
         default:
            return getDeltaY(☃, ☃, ☃);
         case Z:
            return getDeltaZ(☃, ☃, ☃);
      }
   }

   private AxisAlignedBB moveByPositionAndProgress(AxisAlignedBB var1) {
      double ☃ = this.getExtendedProgress(this.progress);
      return ☃.offset(
         this.pos.getX() + ☃ * this.pistonFacing.getXOffset(),
         this.pos.getY() + ☃ * this.pistonFacing.getYOffset(),
         this.pos.getZ() + ☃ * this.pistonFacing.getZOffset()
      );
   }

   private AxisAlignedBB getMovementArea(AxisAlignedBB var1, EnumFacing var2, double var3) {
      double ☃ = ☃ * ☃.getAxisDirection().getOffset();
      double ☃x = Math.min(☃, 0.0);
      double ☃xx = Math.max(☃, 0.0);
      switch (☃) {
         case WEST:
            return new AxisAlignedBB(☃.minX + ☃x, ☃.minY, ☃.minZ, ☃.minX + ☃xx, ☃.maxY, ☃.maxZ);
         case EAST:
            return new AxisAlignedBB(☃.maxX + ☃x, ☃.minY, ☃.minZ, ☃.maxX + ☃xx, ☃.maxY, ☃.maxZ);
         case DOWN:
            return new AxisAlignedBB(☃.minX, ☃.minY + ☃x, ☃.minZ, ☃.maxX, ☃.minY + ☃xx, ☃.maxZ);
         case UP:
         default:
            return new AxisAlignedBB(☃.minX, ☃.maxY + ☃x, ☃.minZ, ☃.maxX, ☃.maxY + ☃xx, ☃.maxZ);
         case NORTH:
            return new AxisAlignedBB(☃.minX, ☃.minY, ☃.minZ + ☃x, ☃.maxX, ☃.maxY, ☃.minZ + ☃xx);
         case SOUTH:
            return new AxisAlignedBB(☃.minX, ☃.minY, ☃.maxZ + ☃x, ☃.maxX, ☃.maxY, ☃.maxZ + ☃xx);
      }
   }

   private void fixEntityWithinPistonBase(Entity var1, EnumFacing var2, double var3) {
      AxisAlignedBB ☃ = ☃.getEntityBoundingBox();
      AxisAlignedBB ☃x = Block.FULL_BLOCK_AABB.offset(this.pos);
      if (☃.intersects(☃x)) {
         EnumFacing ☃xx = ☃.getOpposite();
         double ☃xxx = this.getMovement(☃x, ☃xx, ☃) + 0.01;
         double ☃xxxx = this.getMovement(☃x, ☃xx, ☃.intersect(☃x)) + 0.01;
         if (Math.abs(☃xxx - ☃xxxx) < 0.01) {
            ☃xxx = Math.min(☃xxx, ☃) + 0.01;
            MOVING_ENTITY.set(☃);
            ☃.move(MoverType.PISTON, ☃xxx * ☃xx.getXOffset(), ☃xxx * ☃xx.getYOffset(), ☃xxx * ☃xx.getZOffset());
            MOVING_ENTITY.set(null);
         }
      }
   }

   private static double getDeltaX(AxisAlignedBB var0, EnumFacing var1, AxisAlignedBB var2) {
      return ☃.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? ☃.maxX - ☃.minX : ☃.maxX - ☃.minX;
   }

   private static double getDeltaY(AxisAlignedBB var0, EnumFacing var1, AxisAlignedBB var2) {
      return ☃.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? ☃.maxY - ☃.minY : ☃.maxY - ☃.minY;
   }

   private static double getDeltaZ(AxisAlignedBB var0, EnumFacing var1, AxisAlignedBB var2) {
      return ☃.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? ☃.maxZ - ☃.minZ : ☃.maxZ - ☃.minZ;
   }

   public void clearPistonTileEntity() {
      if (this.lastProgress < 1.0F && this.world != null) {
         this.progress = 1.0F;
         this.lastProgress = this.progress;
         this.world.removeTileEntity(this.pos);
         this.invalidate();
         if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
            this.world.setBlockState(this.pos, this.pistonState, 3);
            this.world.neighborChanged(this.pos, this.pistonState.getBlock(), this.pos);
         }
      }
   }

   @Override
   public void update() {
      this.lastProgress = this.progress;
      if (this.lastProgress >= 1.0F) {
         this.world.removeTileEntity(this.pos);
         this.invalidate();
         if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
            this.world.setBlockState(this.pos, this.pistonState, 3);
            this.world.neighborChanged(this.pos, this.pistonState.getBlock(), this.pos);
         }
      } else {
         float ☃ = this.progress + 0.5F;
         this.moveCollidedEntities(☃);
         this.progress = ☃;
         if (this.progress >= 1.0F) {
            this.progress = 1.0F;
         }
      }
   }

   public static void registerFixesPiston(DataFixer var0) {
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.pistonState = Block.getBlockById(☃.getInteger("blockId")).getStateFromMeta(☃.getInteger("blockData"));
      this.pistonFacing = EnumFacing.byIndex(☃.getInteger("facing"));
      this.progress = ☃.getFloat("progress");
      this.lastProgress = this.progress;
      this.extending = ☃.getBoolean("extending");
      this.shouldHeadBeRendered = ☃.getBoolean("source");
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
      ☃.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
      ☃.setInteger("facing", this.pistonFacing.getIndex());
      ☃.setFloat("progress", this.lastProgress);
      ☃.setBoolean("extending", this.extending);
      ☃.setBoolean("source", this.shouldHeadBeRendered);
      return ☃;
   }

   public void addCollissionAABBs(World var1, BlockPos var2, AxisAlignedBB var3, List<AxisAlignedBB> var4, @Nullable Entity var5) {
      if (!this.extending && this.shouldHeadBeRendered) {
         this.pistonState.withProperty(BlockPistonBase.EXTENDED, true).addCollisionBoxToList(☃, ☃, ☃, ☃, ☃, false);
      }

      EnumFacing ☃ = MOVING_ENTITY.get();
      if (!(this.progress < 1.0) || ☃ != (this.extending ? this.pistonFacing : this.pistonFacing.getOpposite())) {
         int ☃x = ☃.size();
         IBlockState ☃xx;
         if (this.shouldPistonHeadBeRendered()) {
            ☃xx = Blocks.PISTON_HEAD
               .getDefaultState()
               .withProperty(BlockPistonExtension.FACING, this.pistonFacing)
               .withProperty(BlockPistonExtension.SHORT, this.extending != 1.0F - this.progress < 0.25F);
         } else {
            ☃xx = this.pistonState;
         }

         float ☃xxx = this.getExtendedProgress(this.progress);
         double ☃xxxx = this.pistonFacing.getXOffset() * ☃xxx;
         double ☃xxxxx = this.pistonFacing.getYOffset() * ☃xxx;
         double ☃xxxxxx = this.pistonFacing.getZOffset() * ☃xxx;
         ☃xx.addCollisionBoxToList(☃, ☃, ☃.offset(-☃xxxx, -☃xxxxx, -☃xxxxxx), ☃, ☃, true);

         for (int ☃xxxxxxx = ☃x; ☃xxxxxxx < ☃.size(); ☃xxxxxxx++) {
            ☃.set(☃xxxxxxx, ☃.get(☃xxxxxxx).offset(☃xxxx, ☃xxxxx, ☃xxxxxx));
         }
      }
   }
}
