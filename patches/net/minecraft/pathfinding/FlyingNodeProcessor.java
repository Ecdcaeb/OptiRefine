package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class FlyingNodeProcessor extends WalkNodeProcessor {
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
      } else {
         ☃ = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5);
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
               return super.openPoint(☃xxxx.getX(), ☃xxxx.getY(), ☃xxxx.getZ());
            }
         }
      }

      return super.openPoint(☃x.getX(), ☃, ☃x.getZ());
   }

   @Override
   public PathPoint getPathPointToCoords(double var1, double var3, double var5) {
      return super.openPoint(MathHelper.floor(☃), MathHelper.floor(☃), MathHelper.floor(☃));
   }

   @Override
   public int findPathOptions(PathPoint[] var1, PathPoint var2, PathPoint var3, float var4) {
      int ☃ = 0;
      PathPoint ☃x = this.openPoint(☃.x, ☃.y, ☃.z + 1);
      PathPoint ☃xx = this.openPoint(☃.x - 1, ☃.y, ☃.z);
      PathPoint ☃xxx = this.openPoint(☃.x + 1, ☃.y, ☃.z);
      PathPoint ☃xxxx = this.openPoint(☃.x, ☃.y, ☃.z - 1);
      PathPoint ☃xxxxx = this.openPoint(☃.x, ☃.y + 1, ☃.z);
      PathPoint ☃xxxxxx = this.openPoint(☃.x, ☃.y - 1, ☃.z);
      if (☃x != null && !☃x.visited && ☃x.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃x;
      }

      if (☃xx != null && !☃xx.visited && ☃xx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xx;
      }

      if (☃xxx != null && !☃xxx.visited && ☃xxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxx;
      }

      if (☃xxxx != null && !☃xxxx.visited && ☃xxxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxxx;
      }

      if (☃xxxxx != null && !☃xxxxx.visited && ☃xxxxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxxxx;
      }

      if (☃xxxxxx != null && !☃xxxxxx.visited && ☃xxxxxx.distanceTo(☃) < ☃) {
         ☃[☃++] = ☃xxxxxx;
      }

      boolean ☃xxxxxxx = ☃xxxx == null || ☃xxxx.costMalus != 0.0F;
      boolean ☃xxxxxxxx = ☃x == null || ☃x.costMalus != 0.0F;
      boolean ☃xxxxxxxxx = ☃xxx == null || ☃xxx.costMalus != 0.0F;
      boolean ☃xxxxxxxxxx = ☃xx == null || ☃xx.costMalus != 0.0F;
      boolean ☃xxxxxxxxxxx = ☃xxxxx == null || ☃xxxxx.costMalus != 0.0F;
      boolean ☃xxxxxxxxxxxx = ☃xxxxxx == null || ☃xxxxxx.costMalus != 0.0F;
      if (☃xxxxxxx && ☃xxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x - 1, ☃.y, ☃.z - 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxx && ☃xxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x + 1, ☃.y, ☃.z - 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxx && ☃xxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x - 1, ☃.y, ☃.z + 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxx && ☃xxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x + 1, ☃.y, ☃.z + 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxx && ☃xxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x, ☃.y + 1, ☃.z - 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxx && ☃xxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x, ☃.y + 1, ☃.z + 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxxx && ☃xxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x + 1, ☃.y + 1, ☃.z);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxxxx && ☃xxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x - 1, ☃.y + 1, ☃.z);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxx && ☃xxxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x, ☃.y - 1, ☃.z - 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxx && ☃xxxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x, ☃.y - 1, ☃.z + 1);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxxx && ☃xxxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x + 1, ☃.y - 1, ☃.z);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      if (☃xxxxxxxxxx && ☃xxxxxxxxxxxx) {
         PathPoint ☃xxxxxxxxxxxxx = this.openPoint(☃.x - 1, ☃.y - 1, ☃.z);
         if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.visited && ☃xxxxxxxxxxxxx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xxxxxxxxxxxxx;
         }
      }

      return ☃;
   }

   @Nullable
   @Override
   protected PathPoint openPoint(int var1, int var2, int var3) {
      PathPoint ☃ = null;
      PathNodeType ☃x = this.getPathNodeType(this.entity, ☃, ☃, ☃);
      float ☃xx = this.entity.getPathPriority(☃x);
      if (☃xx >= 0.0F) {
         ☃ = super.openPoint(☃, ☃, ☃);
         ☃.nodeType = ☃x;
         ☃.costMalus = Math.max(☃.costMalus, ☃xx);
         if (☃x == PathNodeType.WALKABLE) {
            ☃.costMalus++;
         }
      }

      return ☃x != PathNodeType.OPEN && ☃x != PathNodeType.WALKABLE ? ☃ : ☃;
   }

   @Override
   public PathNodeType getPathNodeType(
      IBlockAccess var1, int var2, int var3, int var4, EntityLiving var5, int var6, int var7, int var8, boolean var9, boolean var10
   ) {
      EnumSet<PathNodeType> ☃ = EnumSet.noneOf(PathNodeType.class);
      PathNodeType ☃x = PathNodeType.BLOCKED;
      BlockPos ☃xx = new BlockPos(☃);
      ☃x = this.getPathNodeType(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃x, ☃xx);
      if (☃.contains(PathNodeType.FENCE)) {
         return PathNodeType.FENCE;
      } else {
         PathNodeType ☃xxx = PathNodeType.BLOCKED;

         for (PathNodeType ☃xxxx : ☃) {
            if (☃.getPathPriority(☃xxxx) < 0.0F) {
               return ☃xxxx;
            }

            if (☃.getPathPriority(☃xxxx) >= ☃.getPathPriority(☃xxx)) {
               ☃xxx = ☃xxxx;
            }
         }

         return ☃x == PathNodeType.OPEN && ☃.getPathPriority(☃xxx) == 0.0F ? PathNodeType.OPEN : ☃xxx;
      }
   }

   @Override
   public PathNodeType getPathNodeType(IBlockAccess var1, int var2, int var3, int var4) {
      PathNodeType ☃ = this.getPathNodeTypeRaw(☃, ☃, ☃, ☃);
      if (☃ == PathNodeType.OPEN && ☃ >= 1) {
         Block ☃x = ☃.getBlockState(new BlockPos(☃, ☃ - 1, ☃)).getBlock();
         PathNodeType ☃xx = this.getPathNodeTypeRaw(☃, ☃, ☃ - 1, ☃);
         if (☃xx == PathNodeType.DAMAGE_FIRE || ☃x == Blocks.MAGMA || ☃xx == PathNodeType.LAVA) {
            ☃ = PathNodeType.DAMAGE_FIRE;
         } else if (☃xx == PathNodeType.DAMAGE_CACTUS) {
            ☃ = PathNodeType.DAMAGE_CACTUS;
         } else {
            ☃ = ☃xx != PathNodeType.WALKABLE && ☃xx != PathNodeType.OPEN && ☃xx != PathNodeType.WATER ? PathNodeType.WALKABLE : PathNodeType.OPEN;
         }
      }

      return this.checkNeighborBlocks(☃, ☃, ☃, ☃, ☃);
   }

   private PathNodeType getPathNodeType(EntityLiving var1, BlockPos var2) {
      return this.getPathNodeType(☃, ☃.getX(), ☃.getY(), ☃.getZ());
   }

   private PathNodeType getPathNodeType(EntityLiving var1, int var2, int var3, int var4) {
      return this.getPathNodeType(
         this.blockaccess, ☃, ☃, ☃, ☃, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors()
      );
   }
}
