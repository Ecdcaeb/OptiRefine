package net.minecraft.pathfinding;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;

public class PathWorldListener implements IWorldEventListener {
   private final List<PathNavigate> navigations = Lists.newArrayList();

   @Override
   public void notifyBlockUpdate(World var1, BlockPos var2, IBlockState var3, IBlockState var4, int var5) {
      if (this.didBlockChange(☃, ☃, ☃, ☃)) {
         int ☃ = 0;

         for (int ☃x = this.navigations.size(); ☃ < ☃x; ☃++) {
            PathNavigate ☃xx = this.navigations.get(☃);
            if (☃xx != null && !☃xx.canUpdatePathOnTimeout()) {
               Path ☃xxx = ☃xx.getPath();
               if (☃xxx != null && !☃xxx.isFinished() && ☃xxx.getCurrentPathLength() != 0) {
                  PathPoint ☃xxxx = ☃xx.currentPath.getFinalPathPoint();
                  double ☃xxxxx = ☃.distanceSq((☃xxxx.x + ☃xx.entity.posX) / 2.0, (☃xxxx.y + ☃xx.entity.posY) / 2.0, (☃xxxx.z + ☃xx.entity.posZ) / 2.0);
                  int ☃xxxxxx = (☃xxx.getCurrentPathLength() - ☃xxx.getCurrentPathIndex()) * (☃xxx.getCurrentPathLength() - ☃xxx.getCurrentPathIndex());
                  if (☃xxxxx < ☃xxxxxx) {
                     ☃xx.updatePath();
                  }
               }
            }
         }
      }
   }

   protected boolean didBlockChange(World var1, BlockPos var2, IBlockState var3, IBlockState var4) {
      AxisAlignedBB ☃ = ☃.getCollisionBoundingBox(☃, ☃);
      AxisAlignedBB ☃x = ☃.getCollisionBoundingBox(☃, ☃);
      return ☃ != ☃x && (☃ == null || !☃.equals(☃x));
   }

   @Override
   public void notifyLightSet(BlockPos var1) {
   }

   @Override
   public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   @Override
   public void playSoundToAllNearExcept(
      @Nullable EntityPlayer var1, SoundEvent var2, SoundCategory var3, double var4, double var6, double var8, float var10, float var11
   ) {
   }

   @Override
   public void spawnParticle(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
   }

   @Override
   public void spawnParticle(
      int var1, boolean var2, boolean var3, double var4, double var6, double var8, double var10, double var12, double var14, int... var16
   ) {
   }

   @Override
   public void onEntityAdded(Entity var1) {
      if (☃ instanceof EntityLiving) {
         this.navigations.add(((EntityLiving)☃).getNavigator());
      }
   }

   @Override
   public void onEntityRemoved(Entity var1) {
      if (☃ instanceof EntityLiving) {
         this.navigations.remove(((EntityLiving)☃).getNavigator());
      }
   }

   @Override
   public void playRecord(SoundEvent var1, BlockPos var2) {
   }

   @Override
   public void broadcastSound(int var1, BlockPos var2, int var3) {
   }

   @Override
   public void playEvent(EntityPlayer var1, int var2, BlockPos var3, int var4) {
   }

   @Override
   public void sendBlockBreakProgress(int var1, BlockPos var2, int var3) {
   }
}
