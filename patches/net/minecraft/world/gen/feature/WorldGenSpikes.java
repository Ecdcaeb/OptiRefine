package net.minecraft.world.gen.feature;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator {
   private boolean crystalInvulnerable;
   private WorldGenSpikes.EndSpike spike;
   private BlockPos beamTarget;

   public void setSpike(WorldGenSpikes.EndSpike var1) {
      this.spike = ☃;
   }

   public void setCrystalInvulnerable(boolean var1) {
      this.crystalInvulnerable = ☃;
   }

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (this.spike == null) {
         throw new IllegalStateException("Decoration requires priming with a spike");
      } else {
         int ☃ = this.spike.getRadius();

         for (BlockPos.MutableBlockPos ☃x : BlockPos.getAllInBoxMutable(
            new BlockPos(☃.getX() - ☃, 0, ☃.getZ() - ☃), new BlockPos(☃.getX() + ☃, this.spike.getHeight() + 10, ☃.getZ() + ☃)
         )) {
            if (☃x.distanceSq(☃.getX(), ☃x.getY(), ☃.getZ()) <= ☃ * ☃ + 1 && ☃x.getY() < this.spike.getHeight()) {
               this.setBlockAndNotifyAdequately(☃, ☃x, Blocks.OBSIDIAN.getDefaultState());
            } else if (☃x.getY() > 65) {
               this.setBlockAndNotifyAdequately(☃, ☃x, Blocks.AIR.getDefaultState());
            }
         }

         if (this.spike.isGuarded()) {
            for (int ☃xx = -2; ☃xx <= 2; ☃xx++) {
               for (int ☃xxx = -2; ☃xxx <= 2; ☃xxx++) {
                  if (MathHelper.abs(☃xx) == 2 || MathHelper.abs(☃xxx) == 2) {
                     this.setBlockAndNotifyAdequately(
                        ☃, new BlockPos(☃.getX() + ☃xx, this.spike.getHeight(), ☃.getZ() + ☃xxx), Blocks.IRON_BARS.getDefaultState()
                     );
                     this.setBlockAndNotifyAdequately(
                        ☃, new BlockPos(☃.getX() + ☃xx, this.spike.getHeight() + 1, ☃.getZ() + ☃xxx), Blocks.IRON_BARS.getDefaultState()
                     );
                     this.setBlockAndNotifyAdequately(
                        ☃, new BlockPos(☃.getX() + ☃xx, this.spike.getHeight() + 2, ☃.getZ() + ☃xxx), Blocks.IRON_BARS.getDefaultState()
                     );
                  }

                  this.setBlockAndNotifyAdequately(
                     ☃, new BlockPos(☃.getX() + ☃xx, this.spike.getHeight() + 3, ☃.getZ() + ☃xxx), Blocks.IRON_BARS.getDefaultState()
                  );
               }
            }
         }

         EntityEnderCrystal ☃xx = new EntityEnderCrystal(☃);
         ☃xx.setBeamTarget(this.beamTarget);
         ☃xx.setEntityInvulnerable(this.crystalInvulnerable);
         ☃xx.setLocationAndAngles(☃.getX() + 0.5F, this.spike.getHeight() + 1, ☃.getZ() + 0.5F, ☃.nextFloat() * 360.0F, 0.0F);
         ☃.spawnEntity(☃xx);
         this.setBlockAndNotifyAdequately(☃, new BlockPos(☃.getX(), this.spike.getHeight(), ☃.getZ()), Blocks.BEDROCK.getDefaultState());
         return true;
      }
   }

   public void setBeamTarget(@Nullable BlockPos var1) {
      this.beamTarget = ☃;
   }

   public static class EndSpike {
      private final int centerX;
      private final int centerZ;
      private final int radius;
      private final int height;
      private final boolean guarded;
      private final AxisAlignedBB topBoundingBox;

      public EndSpike(int var1, int var2, int var3, int var4, boolean var5) {
         this.centerX = ☃;
         this.centerZ = ☃;
         this.radius = ☃;
         this.height = ☃;
         this.guarded = ☃;
         this.topBoundingBox = new AxisAlignedBB(☃ - ☃, 0.0, ☃ - ☃, ☃ + ☃, 256.0, ☃ + ☃);
      }

      public boolean doesStartInChunk(BlockPos var1) {
         int ☃ = this.centerX - this.radius;
         int ☃x = this.centerZ - this.radius;
         return ☃.getX() == (☃ & -16) && ☃.getZ() == (☃x & -16);
      }

      public int getCenterX() {
         return this.centerX;
      }

      public int getCenterZ() {
         return this.centerZ;
      }

      public int getRadius() {
         return this.radius;
      }

      public int getHeight() {
         return this.height;
      }

      public boolean isGuarded() {
         return this.guarded;
      }

      public AxisAlignedBB getTopBoundingBox() {
         return this.topBoundingBox;
      }
   }
}
