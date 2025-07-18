package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor {
   protected float avoidsWater;

   @Override
   public void init(IBlockAccess var1, EntityLiving var2) {
      super.init(☃, ☃);
      this.avoidsWater = ☃.getPathPriority(PathNodeType.WATER);
   }

   @Override
   public void postProcess() {
      this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
      super.postProcess();
   }

   @Override
   public PathPoint getStart() {
      int ☃;
      if (this.getCanSwim() && this.entity.isInWater()) {
         ☃ = (int)this.entity.getEntityBoundingBox().minY;
         BlockPos.MutableBlockPos ☃x = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), ☃, MathHelper.floor(this.entity.posZ));

         for (Block ☃xx = this.blockaccess.getBlockState(☃x).getBlock();
            ☃xx == Blocks.FLOWING_WATER || ☃xx == Blocks.WATER;
            ☃xx = this.blockaccess.getBlockState(☃x).getBlock()
         ) {
            ☃x.setPos(MathHelper.floor(this.entity.posX), ++☃, MathHelper.floor(this.entity.posZ));
         }
      } else if (this.entity.onGround) {
         ☃ = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5);
      } else {
         BlockPos ☃x = new BlockPos(this.entity);

         while (
            (this.blockaccess.getBlockState(☃x).getMaterial() == Material.AIR || this.blockaccess.getBlockState(☃x).getBlock().isPassable(this.blockaccess, ☃x))
               && ☃x.getY() > 0
         ) {
            ☃x = ☃x.down();
         }

         ☃ = ☃x.up().getY();
      }

      BlockPos ☃x = new BlockPos(this.entity);
      PathNodeType ☃xx = this.getPathNodeType(this.entity, ☃x.getX(), ☃, ☃x.getZ());
      if (this.entity.getPathPriority(☃xx) < 0.0F) {
         Set<BlockPos> ☃xxx = Sets.newHashSet();
         ☃xxx.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)☃, this.entity.getEntityBoundingBox().minZ));
         ☃xxx.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)☃, this.entity.getEntityBoundingBox().maxZ));
         ☃xxx.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)☃, this.entity.getEntityBoundingBox().minZ));
         ☃xxx.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)☃, this.entity.getEntityBoundingBox().maxZ));

         for (BlockPos ☃xxxx : ☃xxx) {
            PathNodeType ☃xxxxx = this.getPathNodeType(this.entity, ☃xxxx);
            if (this.entity.getPathPriority(☃xxxxx) >= 0.0F) {
               return this.openPoint(☃xxxx.getX(), ☃xxxx.getY(), ☃xxxx.getZ());
            }
         }
      }

      return this.openPoint(☃x.getX(), ☃, ☃x.getZ());
   }

   @Override
   public PathPoint getPathPointToCoords(double var1, double var3, double var5) {
      return this.openPoint(MathHelper.floor(☃), MathHelper.floor(☃), MathHelper.floor(☃));
   }

   @Override
   public int findPathOptions(PathPoint[] var1, PathPoint var2, PathPoint var3, float var4) {
      int ☃ = 0;
      int ☃x = 0;
      PathNodeType ☃xx = this.getPathNodeType(this.entity, ☃.x, ☃.y + 1, ☃.z);
      if (this.entity.getPathPriority(☃xx) >= 0.0F) {
         ☃x = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
      }

      BlockPos ☃xxx = new BlockPos(☃.x, ☃.y, ☃.z).down();
      double ☃xxxx = ☃.y - (1.0 - this.blockaccess.getBlockState(☃xxx).getBoundingBox(this.blockaccess, ☃xxx).maxY);
      PathPoint ☃xxxxx = this.getSafePoint(☃.x, ☃.y, ☃.z + 1, ☃x, ☃xxxx, EnumFacing.SOUTH);
      PathPoint ☃xxxxxx = this.getSafePoint(☃.x - 1, ☃.y, ☃.z, ☃x, ☃xxxx, EnumFacing.WEST);
      PathPoint ☃xxxxxxx = this.getSafePoint(☃.x + 1, ☃.y, ☃.z, ☃x, ☃xxxx, EnumFacing.EAST);
      PathPoint ☃xxxxxxxx = this.getSafePoint(☃.x, ☃.y, ☃.z - 1, ☃x, ☃xxxx, EnumFacing.NORTH);
      if (☃xxxxx != null && !☃xxxxx.visited && ☃xxxxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxxxx;
      }

      if (☃xxxxxx != null && !☃xxxxxx.visited && ☃xxxxxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxxxxx;
      }

      if (☃xxxxxxx != null && !☃xxxxxxx.visited && ☃xxxxxxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxxxxxx;
      }

      if (☃xxxxxxxx != null && !☃xxxxxxxx.visited && ☃xxxxxxxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxxxxxxx;
      }

      boolean ☃xxxxxxxxx = ☃xxxxxxxx == null || ☃xxxxxxxx.nodeType == PathNodeType.OPEN || ☃xxxxxxxx.costMalus != 0.0F;
      boolean ☃xxxxxxxxxx = ☃xxxxx == null || ☃xxxxx.nodeType == PathNodeType.OPEN || ☃xxxxx.costMalus != 0.0F;
      boolean ☃xxxxxxxxxxx = ☃xxxxxxx == null || ☃xxxxxxx.nodeType == PathNodeType.OPEN || ☃xxxxxxx.costMalus != 0.0F;
      boolean ☃xxxxxxxxxxxx = ☃xxxxxx == null || ☃xxxxxx.nodeType == PathNodeType.OPEN || ☃xxxxxx.costMalus != 0.0F;
      if (☃xxxxxxxxx && ☃xxxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.getSafePoint(☃.x - 1, ☃.y, ☃.z - 1, ☃x, ☃xxxx, EnumFacing.NORTH);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxxx && ☃xxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.getSafePoint(☃.x + 1, ☃.y, ☃.z - 1, ☃x, ☃xxxx, EnumFacing.NORTH);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxxxx && ☃xxxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.getSafePoint(☃.x - 1, ☃.y, ☃.z + 1, ☃x, ☃xxxx, EnumFacing.SOUTH);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxxxx && ☃xxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.getSafePoint(☃.x + 1, ☃.y, ☃.z + 1, ☃x, ☃xxxx, EnumFacing.SOUTH);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      return ☃;
   }

   @Nullable
   private PathPoint getSafePoint(int var1, int var2, int var3, int var4, double var5, EnumFacing var7) {
      PathPoint ☃ = null;
      BlockPos ☃x = new BlockPos(☃, ☃, ☃);
      BlockPos ☃xx = ☃x.down();
      double ☃xxx = ☃ - (1.0 - this.blockaccess.getBlockState(☃xx).getBoundingBox(this.blockaccess, ☃xx).maxY);
      if (☃xxx - ☃ > 1.125) {
         return null;
      } else {
         PathNodeType ☃xxxx = this.getPathNodeType(this.entity, ☃, ☃, ☃);
         float ☃xxxxx = this.entity.getPathPriority(☃xxxx);
         double ☃xxxxxx = this.entity.width / 2.0;
         if (☃xxxxx >= 0.0F) {
            ☃ = this.openPoint(☃, ☃, ☃);
            ☃.nodeType = ☃xxxx;
            ☃.costMalus = Math.max(☃.costMalus, ☃xxxxx);
         }

         if (☃xxxx == PathNodeType.WALKABLE) {
            return ☃;
         } else {
            if (☃ == null && ☃ > 0 && ☃xxxx != PathNodeType.FENCE && ☃xxxx != PathNodeType.TRAPDOOR) {
               ☃ = this.getSafePoint(☃, ☃ + 1, ☃, ☃ - 1, ☃, ☃);
               if (☃ != null && (☃.nodeType == PathNodeType.OPEN || ☃.nodeType == PathNodeType.WALKABLE) && this.entity.width < 1.0F) {
                  double ☃xxxxxxx = ☃ - ☃.getXOffset() + 0.5;
                  double ☃xxxxxxxx = ☃ - ☃.getZOffset() + 0.5;
                  AxisAlignedBB ☃xxxxxxxxx = new AxisAlignedBB(
                     ☃xxxxxxx - ☃xxxxxx, ☃ + 0.001, ☃xxxxxxxx - ☃xxxxxx, ☃xxxxxxx + ☃xxxxxx, ☃ + this.entity.height, ☃xxxxxxxx + ☃xxxxxx
                  );
                  AxisAlignedBB ☃xxxxxxxxxx = this.blockaccess.getBlockState(☃x).getBoundingBox(this.blockaccess, ☃x);
                  AxisAlignedBB ☃xxxxxxxxxxx = ☃xxxxxxxxx.expand(0.0, ☃xxxxxxxxxx.maxY - 0.002, 0.0);
                  if (this.entity.world.collidesWithAnyBlock(☃xxxxxxxxxxx)) {
                     ☃ = null;
                  }
               }
            }

            if (☃xxxx == PathNodeType.OPEN) {
               AxisAlignedBB ☃xxxxxxx = new AxisAlignedBB(
                  ☃ - ☃xxxxxx + 0.5, ☃ + 0.001, ☃ - ☃xxxxxx + 0.5, ☃ + ☃xxxxxx + 0.5, ☃ + this.entity.height, ☃ + ☃xxxxxx + 0.5
               );
               if (this.entity.world.collidesWithAnyBlock(☃xxxxxxx)) {
                  return null;
               }

               if (this.entity.width >= 1.0F) {
                  PathNodeType ☃xxxxxxxx = this.getPathNodeType(this.entity, ☃, ☃ - 1, ☃);
                  if (☃xxxxxxxx == PathNodeType.BLOCKED) {
                     ☃ = this.openPoint(☃, ☃, ☃);
                     ☃.nodeType = PathNodeType.WALKABLE;
                     ☃.costMalus = Math.max(☃.costMalus, ☃xxxxx);
                     return ☃;
                  }
               }

               int ☃xxxxxxxx = 0;

               while (☃ > 0 && ☃xxxx == PathNodeType.OPEN) {
                  ☃--;
                  if (☃xxxxxxxx++ >= this.entity.getMaxFallHeight()) {
                     return null;
                  }

                  ☃xxxx = this.getPathNodeType(this.entity, ☃, ☃, ☃);
                  ☃xxxxx = this.entity.getPathPriority(☃xxxx);
                  if (☃xxxx != PathNodeType.OPEN && ☃xxxxx >= 0.0F) {
                     ☃ = this.openPoint(☃, ☃, ☃);
                     ☃.nodeType = ☃xxxx;
                     ☃.costMalus = Math.max(☃.costMalus, ☃xxxxx);
                     break;
                  }

                  if (☃xxxxx < 0.0F) {
                     return null;
                  }
               }
            }

            return ☃;
         }
      }
   }

   @Override
   public PathNodeType getPathNodeType(
      IBlockAccess var1, int var2, int var3, int var4, EntityLiving var5, int var6, int var7, int var8, boolean var9, boolean var10
   ) {
      EnumSet<PathNodeType> ☃ = EnumSet.noneOf(PathNodeType.class);
      PathNodeType ☃x = PathNodeType.BLOCKED;
      double ☃xx = ☃.width / 2.0;
      BlockPos ☃xxx = new BlockPos(☃);
      ☃x = this.getPathNodeType(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃x, ☃xxx);
      if (☃.contains(PathNodeType.FENCE)) {
         return PathNodeType.FENCE;
      } else {
         PathNodeType ☃xxxx = PathNodeType.BLOCKED;

         for (PathNodeType ☃xxxxx : ☃) {
            if (☃.getPathPriority(☃xxxxx) < 0.0F) {
               return ☃xxxxx;
            }

            if (☃.getPathPriority(☃xxxxx) >= ☃.getPathPriority(☃xxxx)) {
               ☃xxxx = ☃xxxxx;
            }
         }

         return ☃x == PathNodeType.OPEN && ☃.getPathPriority(☃xxxx) == 0.0F ? PathNodeType.OPEN : ☃xxxx;
      }
   }

   public PathNodeType getPathNodeType(
      IBlockAccess var1,
      int var2,
      int var3,
      int var4,
      int var5,
      int var6,
      int var7,
      boolean var8,
      boolean var9,
      EnumSet<PathNodeType> var10,
      PathNodeType var11,
      BlockPos var12
   ) {
      for (int ☃ = 0; ☃ < ☃; ☃++) {
         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            for (int ☃xx = 0; ☃xx < ☃; ☃xx++) {
               int ☃xxx = ☃ + ☃;
               int ☃xxxx = ☃x + ☃;
               int ☃xxxxx = ☃xx + ☃;
               PathNodeType ☃xxxxxx = this.getPathNodeType(☃, ☃xxx, ☃xxxx, ☃xxxxx);
               if (☃xxxxxx == PathNodeType.DOOR_WOOD_CLOSED && ☃ && ☃) {
                  ☃xxxxxx = PathNodeType.WALKABLE;
               }

               if (☃xxxxxx == PathNodeType.DOOR_OPEN && !☃) {
                  ☃xxxxxx = PathNodeType.BLOCKED;
               }

               if (☃xxxxxx == PathNodeType.RAIL
                  && !(☃.getBlockState(☃).getBlock() instanceof BlockRailBase)
                  && !(☃.getBlockState(☃.down()).getBlock() instanceof BlockRailBase)) {
                  ☃xxxxxx = PathNodeType.FENCE;
               }

               if (☃ == 0 && ☃x == 0 && ☃xx == 0) {
                  ☃ = ☃xxxxxx;
               }

               ☃.add(☃xxxxxx);
            }
         }
      }

      return ☃;
   }

   private PathNodeType getPathNodeType(EntityLiving var1, BlockPos var2) {
      return this.getPathNodeType(☃, ☃.getX(), ☃.getY(), ☃.getZ());
   }

   private PathNodeType getPathNodeType(EntityLiving var1, int var2, int var3, int var4) {
      return this.getPathNodeType(
         this.blockaccess, ☃, ☃, ☃, ☃, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors()
      );
   }

   @Override
   public PathNodeType getPathNodeType(IBlockAccess var1, int var2, int var3, int var4) {
      PathNodeType ☃ = this.getPathNodeTypeRaw(☃, ☃, ☃, ☃);
      if (☃ == PathNodeType.OPEN && ☃ >= 1) {
         Block ☃x = ☃.getBlockState(new BlockPos(☃, ☃ - 1, ☃)).getBlock();
         PathNodeType ☃xx = this.getPathNodeTypeRaw(☃, ☃, ☃ - 1, ☃);
         ☃ = ☃xx != PathNodeType.WALKABLE && ☃xx != PathNodeType.OPEN && ☃xx != PathNodeType.WATER && ☃xx != PathNodeType.LAVA
            ? PathNodeType.WALKABLE
            : PathNodeType.OPEN;
         if (☃xx == PathNodeType.DAMAGE_FIRE || ☃x == Blocks.MAGMA) {
            ☃ = PathNodeType.DAMAGE_FIRE;
         }

         if (☃xx == PathNodeType.DAMAGE_CACTUS) {
            ☃ = PathNodeType.DAMAGE_CACTUS;
         }
      }

      return this.checkNeighborBlocks(☃, ☃, ☃, ☃, ☃);
   }

   public PathNodeType checkNeighborBlocks(IBlockAccess var1, int var2, int var3, int var4, PathNodeType var5) {
      BlockPos.PooledMutableBlockPos ☃ = BlockPos.PooledMutableBlockPos.retain();
      if (☃ == PathNodeType.WALKABLE) {
         for (int ☃x = -1; ☃x <= 1; ☃x++) {
            for (int ☃xx = -1; ☃xx <= 1; ☃xx++) {
               if (☃x != 0 || ☃xx != 0) {
                  Block ☃xxx = ☃.getBlockState(☃.setPos(☃x + ☃, ☃, ☃xx + ☃)).getBlock();
                  if (☃xxx == Blocks.CACTUS) {
                     ☃ = PathNodeType.DANGER_CACTUS;
                  } else if (☃xxx == Blocks.FIRE) {
                     ☃ = PathNodeType.DANGER_FIRE;
                  }
               }
            }
         }
      }

      ☃.release();
      return ☃;
   }

   protected PathNodeType getPathNodeTypeRaw(IBlockAccess var1, int var2, int var3, int var4) {
      BlockPos ☃ = new BlockPos(☃, ☃, ☃);
      IBlockState ☃x = ☃.getBlockState(☃);
      Block ☃xx = ☃x.getBlock();
      Material ☃xxx = ☃x.getMaterial();
      if (☃xxx == Material.AIR) {
         return PathNodeType.OPEN;
      } else if (☃xx == Blocks.TRAPDOOR || ☃xx == Blocks.IRON_TRAPDOOR || ☃xx == Blocks.WATERLILY) {
         return PathNodeType.TRAPDOOR;
      } else if (☃xx == Blocks.FIRE) {
         return PathNodeType.DAMAGE_FIRE;
      } else if (☃xx == Blocks.CACTUS) {
         return PathNodeType.DAMAGE_CACTUS;
      } else if (☃xx instanceof BlockDoor && ☃xxx == Material.WOOD && !☃x.getValue(BlockDoor.OPEN)) {
         return PathNodeType.DOOR_WOOD_CLOSED;
      } else if (☃xx instanceof BlockDoor && ☃xxx == Material.IRON && !☃x.getValue(BlockDoor.OPEN)) {
         return PathNodeType.DOOR_IRON_CLOSED;
      } else if (☃xx instanceof BlockDoor && ☃x.getValue(BlockDoor.OPEN)) {
         return PathNodeType.DOOR_OPEN;
      } else if (☃xx instanceof BlockRailBase) {
         return PathNodeType.RAIL;
      } else if (!(☃xx instanceof BlockFence) && !(☃xx instanceof BlockWall) && (!(☃xx instanceof BlockFenceGate) || ☃x.getValue(BlockFenceGate.OPEN))) {
         if (☃xxx == Material.WATER) {
            return PathNodeType.WATER;
         } else if (☃xxx == Material.LAVA) {
            return PathNodeType.LAVA;
         } else {
            return ☃xx.isPassable(☃, ☃) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
         }
      } else {
         return PathNodeType.FENCE;
      }
   }
}
