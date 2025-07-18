package net.minecraft.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Random;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

public class Teleporter {
   private final WorldServer world;
   private final Random random;
   private final Long2ObjectMap<Teleporter.PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap(4096);

   public Teleporter(WorldServer var1) {
      this.world = ☃;
      this.random = new Random(☃.getSeed());
   }

   public void placeInPortal(Entity var1, float var2) {
      if (this.world.provider.getDimensionType().getId() != 1) {
         if (!this.placeInExistingPortal(☃, ☃)) {
            this.makePortal(☃);
            this.placeInExistingPortal(☃, ☃);
         }
      } else {
         int ☃ = MathHelper.floor(☃.posX);
         int ☃x = MathHelper.floor(☃.posY) - 1;
         int ☃xx = MathHelper.floor(☃.posZ);
         int ☃xxx = 1;
         int ☃xxxx = 0;

         for (int ☃xxxxx = -2; ☃xxxxx <= 2; ☃xxxxx++) {
            for (int ☃xxxxxx = -2; ☃xxxxxx <= 2; ☃xxxxxx++) {
               for (int ☃xxxxxxx = -1; ☃xxxxxxx < 3; ☃xxxxxxx++) {
                  int ☃xxxxxxxx = ☃ + ☃xxxxxx * 1 + ☃xxxxx * 0;
                  int ☃xxxxxxxxx = ☃x + ☃xxxxxxx;
                  int ☃xxxxxxxxxx = ☃xx + ☃xxxxxx * 0 - ☃xxxxx * 1;
                  boolean ☃xxxxxxxxxxx = ☃xxxxxxx < 0;
                  this.world
                     .setBlockState(
                        new BlockPos(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx), ☃xxxxxxxxxxx ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState()
                     );
               }
            }
         }

         ☃.setLocationAndAngles(☃, ☃x, ☃xx, ☃.rotationYaw, 0.0F);
         ☃.motionX = 0.0;
         ☃.motionY = 0.0;
         ☃.motionZ = 0.0;
      }
   }

   public boolean placeInExistingPortal(Entity var1, float var2) {
      int ☃ = 128;
      double ☃x = -1.0;
      int ☃xx = MathHelper.floor(☃.posX);
      int ☃xxx = MathHelper.floor(☃.posZ);
      boolean ☃xxxx = true;
      BlockPos ☃xxxxx = BlockPos.ORIGIN;
      long ☃xxxxxx = ChunkPos.asLong(☃xx, ☃xxx);
      if (this.destinationCoordinateCache.containsKey(☃xxxxxx)) {
         Teleporter.PortalPosition ☃xxxxxxx = (Teleporter.PortalPosition)this.destinationCoordinateCache.get(☃xxxxxx);
         ☃x = 0.0;
         ☃xxxxx = ☃xxxxxxx;
         ☃xxxxxxx.lastUpdateTime = this.world.getTotalWorldTime();
         ☃xxxx = false;
      } else {
         BlockPos ☃xxxxxxx = new BlockPos(☃);

         for (int ☃xxxxxxxx = -128; ☃xxxxxxxx <= 128; ☃xxxxxxxx++) {
            for (int ☃xxxxxxxxx = -128; ☃xxxxxxxxx <= 128; ☃xxxxxxxxx++) {
               BlockPos ☃xxxxxxxxxx = ☃xxxxxxx.add(☃xxxxxxxx, this.world.getActualHeight() - 1 - ☃xxxxxxx.getY(), ☃xxxxxxxxx);

               while (☃xxxxxxxxxx.getY() >= 0) {
                  BlockPos ☃xxxxxxxxxxx = ☃xxxxxxxxxx.down();
                  if (this.world.getBlockState(☃xxxxxxxxxx).getBlock() == Blocks.PORTAL) {
                     for (☃xxxxxxxxxxx = ☃xxxxxxxxxx.down();
                        this.world.getBlockState(☃xxxxxxxxxxx).getBlock() == Blocks.PORTAL;
                        ☃xxxxxxxxxxx = ☃xxxxxxxxxxx.down()
                     ) {
                        ☃xxxxxxxxxx = ☃xxxxxxxxxxx;
                     }

                     double ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.distanceSq(☃xxxxxxx);
                     if (☃x < 0.0 || ☃xxxxxxxxxxxx < ☃x) {
                        ☃x = ☃xxxxxxxxxxxx;
                        ☃xxxxx = ☃xxxxxxxxxx;
                     }
                  }

                  ☃xxxxxxxxxx = ☃xxxxxxxxxxx;
               }
            }
         }
      }

      if (☃x >= 0.0) {
         if (☃xxxx) {
            this.destinationCoordinateCache.put(☃xxxxxx, new Teleporter.PortalPosition(☃xxxxx, this.world.getTotalWorldTime()));
         }

         double ☃xxxxxxx = ☃xxxxx.getX() + 0.5;
         double ☃xxxxxxxx = ☃xxxxx.getZ() + 0.5;
         BlockPattern.PatternHelper ☃xxxxxxxxx = Blocks.PORTAL.createPatternHelper(this.world, ☃xxxxx);
         boolean ☃xxxxxxxxxx = ☃xxxxxxxxx.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
         double ☃xxxxxxxxxxx = ☃xxxxxxxxx.getForwards().getAxis() == EnumFacing.Axis.X
            ? ☃xxxxxxxxx.getFrontTopLeft().getZ()
            : ☃xxxxxxxxx.getFrontTopLeft().getX();
         double ☃xxxxxxxxxxxx = ☃xxxxxxxxx.getFrontTopLeft().getY() + 1 - ☃.getLastPortalVec().y * ☃xxxxxxxxx.getHeight();
         if (☃xxxxxxxxxx) {
            ☃xxxxxxxxxxx++;
         }

         if (☃xxxxxxxxx.getForwards().getAxis() == EnumFacing.Axis.X) {
            ☃xxxxxxxx = ☃xxxxxxxxxxx
               + (1.0 - ☃.getLastPortalVec().x) * ☃xxxxxxxxx.getWidth() * ☃xxxxxxxxx.getForwards().rotateY().getAxisDirection().getOffset();
         } else {
            ☃xxxxxxx = ☃xxxxxxxxxxx
               + (1.0 - ☃.getLastPortalVec().x) * ☃xxxxxxxxx.getWidth() * ☃xxxxxxxxx.getForwards().rotateY().getAxisDirection().getOffset();
         }

         float ☃xxxxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxxxxxx = 0.0F;
         if (☃xxxxxxxxx.getForwards().getOpposite() == ☃.getTeleportDirection()) {
            ☃xxxxxxxxxxxxx = 1.0F;
            ☃xxxxxxxxxxxxxx = 1.0F;
         } else if (☃xxxxxxxxx.getForwards().getOpposite() == ☃.getTeleportDirection().getOpposite()) {
            ☃xxxxxxxxxxxxx = -1.0F;
            ☃xxxxxxxxxxxxxx = -1.0F;
         } else if (☃xxxxxxxxx.getForwards().getOpposite() == ☃.getTeleportDirection().rotateY()) {
            ☃xxxxxxxxxxxxxxx = 1.0F;
            ☃xxxxxxxxxxxxxxxx = -1.0F;
         } else {
            ☃xxxxxxxxxxxxxxx = -1.0F;
            ☃xxxxxxxxxxxxxxxx = 1.0F;
         }

         double ☃xxxxxxxxxxxxxxxxx = ☃.motionX;
         double ☃xxxxxxxxxxxxxxxxxx = ☃.motionZ;
         ☃.motionX = ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
         ☃.motionZ = ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx;
         ☃.rotationYaw = ☃ - ☃.getTeleportDirection().getOpposite().getHorizontalIndex() * 90 + ☃xxxxxxxxx.getForwards().getHorizontalIndex() * 90;
         if (☃ instanceof EntityPlayerMP) {
            ((EntityPlayerMP)☃).connection.setPlayerLocation(☃xxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxx, ☃.rotationYaw, ☃.rotationPitch);
         } else {
            ☃.setLocationAndAngles(☃xxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxx, ☃.rotationYaw, ☃.rotationPitch);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean makePortal(Entity var1) {
      int ☃ = 16;
      double ☃x = -1.0;
      int ☃xx = MathHelper.floor(☃.posX);
      int ☃xxx = MathHelper.floor(☃.posY);
      int ☃xxxx = MathHelper.floor(☃.posZ);
      int ☃xxxxx = ☃xx;
      int ☃xxxxxx = ☃xxx;
      int ☃xxxxxxx = ☃xxxx;
      int ☃xxxxxxxx = 0;
      int ☃xxxxxxxxx = this.random.nextInt(4);
      BlockPos.MutableBlockPos ☃xxxxxxxxxx = new BlockPos.MutableBlockPos();

      for (int ☃xxxxxxxxxxx = ☃xx - 16; ☃xxxxxxxxxxx <= ☃xx + 16; ☃xxxxxxxxxxx++) {
         double ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx + 0.5 - ☃.posX;

         for (int ☃xxxxxxxxxxxxx = ☃xxxx - 16; ☃xxxxxxxxxxxxx <= ☃xxxx + 16; ☃xxxxxxxxxxxxx++) {
            double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx + 0.5 - ☃.posZ;

            label296:
            for (int ☃xxxxxxxxxxxxxxx = this.world.getActualHeight() - 1; ☃xxxxxxxxxxxxxxx >= 0; ☃xxxxxxxxxxxxxxx--) {
               if (this.world.isAirBlock(☃xxxxxxxxxx.setPos(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx))) {
                  while (☃xxxxxxxxxxxxxxx > 0 && this.world.isAirBlock(☃xxxxxxxxxx.setPos(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxx))) {
                     ☃xxxxxxxxxxxxxxx--;
                  }

                  for (int ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxx; ☃xxxxxxxxxxxxxxxx < ☃xxxxxxxxx + 4; ☃xxxxxxxxxxxxxxxx++) {
                     int ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx % 2;
                     int ☃xxxxxxxxxxxxxxxxxx = 1 - ☃xxxxxxxxxxxxxxxxx;
                     if (☃xxxxxxxxxxxxxxxx % 4 >= 2) {
                        ☃xxxxxxxxxxxxxxxxx = -☃xxxxxxxxxxxxxxxxx;
                        ☃xxxxxxxxxxxxxxxxxx = -☃xxxxxxxxxxxxxxxxxx;
                     }

                     for (int ☃xxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxx < 3; ☃xxxxxxxxxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxx++) {
                           for (int ☃xxxxxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxx++) {
                              int ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx
                                 + (☃xxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxxxxx
                                 + ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx;
                              int ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx;
                              int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx
                                 + (☃xxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxxxxxx
                                 - ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx;
                              ☃xxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxx);
                              if (☃xxxxxxxxxxxxxxxxxxxxx < 0 && !this.world.getBlockState(☃xxxxxxxxxx).getMaterial().isSolid()
                                 || ☃xxxxxxxxxxxxxxxxxxxxx >= 0 && !this.world.isAirBlock(☃xxxxxxxxxx)) {
                                 continue label296;
                              }
                           }
                        }
                     }

                     double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx + 0.5 - ☃.posY;
                     double ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx
                        + ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx
                        + ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx;
                     if (☃x < 0.0 || ☃xxxxxxxxxxxxxxxxxxxx < ☃x) {
                        ☃x = ☃xxxxxxxxxxxxxxxxxxxx;
                        ☃xxxxx = ☃xxxxxxxxxxx;
                        ☃xxxxxx = ☃xxxxxxxxxxxxxxx;
                        ☃xxxxxxx = ☃xxxxxxxxxxxxx;
                        ☃xxxxxxxx = ☃xxxxxxxxxxxxxxxx % 4;
                     }
                  }
               }
            }
         }
      }

      if (☃x < 0.0) {
         for (int ☃xxxxxxxxxxx = ☃xx - 16; ☃xxxxxxxxxxx <= ☃xx + 16; ☃xxxxxxxxxxx++) {
            double ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx + 0.5 - ☃.posX;

            for (int ☃xxxxxxxxxxxxx = ☃xxxx - 16; ☃xxxxxxxxxxxxx <= ☃xxxx + 16; ☃xxxxxxxxxxxxx++) {
               double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx + 0.5 - ☃.posZ;

               label233:
               for (int ☃xxxxxxxxxxxxxxxx = this.world.getActualHeight() - 1; ☃xxxxxxxxxxxxxxxx >= 0; ☃xxxxxxxxxxxxxxxx--) {
                  if (this.world.isAirBlock(☃xxxxxxxxxx.setPos(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx))) {
                     while (☃xxxxxxxxxxxxxxxx > 0 && this.world.isAirBlock(☃xxxxxxxxxx.setPos(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxx))) {
                        ☃xxxxxxxxxxxxxxxx--;
                     }

                     for (int ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxx + 2; ☃xxxxxxxxxxxxxxxxxxx++) {
                        int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx % 2;
                        int ☃xxxxxxxxxxxxxxxxxxxxxx = 1 - ☃xxxxxxxxxxxxxxxxxxxx;

                        for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                           for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxx++) {
                              int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx + (☃xxxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxxxxxxxx;
                              int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxx;
                              int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx + (☃xxxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxxxxxxxxxx;
                              ☃xxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx);
                              if (☃xxxxxxxxxxxxxxxxxxxxxxxx < 0 && !this.world.getBlockState(☃xxxxxxxxxx).getMaterial().isSolid()
                                 || ☃xxxxxxxxxxxxxxxxxxxxxxxx >= 0 && !this.world.isAirBlock(☃xxxxxxxxxx)) {
                                 continue label233;
                              }
                           }
                        }

                        double ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx + 0.5 - ☃.posY;
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx
                           + ☃xxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxx
                           + ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx;
                        if (☃x < 0.0 || ☃xxxxxxxxxxxxxxxxxxxxxxxxx < ☃x) {
                           ☃x = ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
                           ☃xxxxx = ☃xxxxxxxxxxx;
                           ☃xxxxxx = ☃xxxxxxxxxxxxxxxx;
                           ☃xxxxxxx = ☃xxxxxxxxxxxxx;
                           ☃xxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      int ☃xxxxxxxxxxx = ☃xxxxx;
      int ☃xxxxxxxxxxxx = ☃xxxxxx;
      int ☃xxxxxxxxxxxxx = ☃xxxxxxx;
      int ☃xxxxxxxxxxxxxx = ☃xxxxxxxx % 2;
      int ☃xxxxxxxxxxxxxxxxxxx = 1 - ☃xxxxxxxxxxxxxx;
      if (☃xxxxxxxx % 4 >= 2) {
         ☃xxxxxxxxxxxxxx = -☃xxxxxxxxxxxxxx;
         ☃xxxxxxxxxxxxxxxxxxx = -☃xxxxxxxxxxxxxxxxxxx;
      }

      if (☃x < 0.0) {
         ☃xxxxxx = MathHelper.clamp(☃xxxxxx, 70, this.world.getActualHeight() - 10);
         ☃xxxxxxxxxxxx = ☃xxxxxx;

         for (int ☃xxxxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxxxx <= 1; ☃xxxxxxxxxxxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxxxxxxxxxxx = 1; ☃xxxxxxxxxxxxxxxxxxxxxx < 3; ☃xxxxxxxxxxxxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxxxxxxx < 3; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx
                     + (☃xxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxx
                     + ☃xxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx;
                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxx;
                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx
                     + (☃xxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxxxxxxx
                     - ☃xxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx;
                  boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxx < 0;
                  this.world
                     .setBlockState(
                        new BlockPos(☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx),
                        ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState()
                     );
               }
            }
         }
      }

      IBlockState ☃xxxxxxxxxxxxxxxxxxxx = Blocks.PORTAL
         .getDefaultState()
         .withProperty(BlockPortal.AXIS, ☃xxxxxxxxxxxxxx == 0 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);

      for (int ☃xxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxx++) {
         for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx + (☃xxxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxx;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx + (☃xxxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxxxxxxx;
               boolean ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxx == 0
                  || ☃xxxxxxxxxxxxxxxxxxxxxxx == 3
                  || ☃xxxxxxxxxxxxxxxxxxxxxxxxx == -1
                  || ☃xxxxxxxxxxxxxxxxxxxxxxxxx == 3;
               this.world
                  .setBlockState(
                     new BlockPos(☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx),
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx ? Blocks.OBSIDIAN.getDefaultState() : ☃xxxxxxxxxxxxxxxxxxxx,
                     2
                  );
            }
         }

         for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = -1; ☃xxxxxxxxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx + (☃xxxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxx;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxx;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx + (☃xxxxxxxxxxxxxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxxxxxxxx;
               BlockPos ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx);
               this.world
                  .notifyNeighborsOfStateChange(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, this.world.getBlockState(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx).getBlock(), false);
            }
         }
      }

      return true;
   }

   public void removeStalePortalLocations(long var1) {
      if (☃ % 100L == 0L) {
         long ☃ = ☃ - 300L;
         ObjectIterator<Teleporter.PortalPosition> ☃x = this.destinationCoordinateCache.values().iterator();

         while (☃x.hasNext()) {
            Teleporter.PortalPosition ☃xx = (Teleporter.PortalPosition)☃x.next();
            if (☃xx == null || ☃xx.lastUpdateTime < ☃) {
               ☃x.remove();
            }
         }
      }
   }

   public class PortalPosition extends BlockPos {
      public long lastUpdateTime;

      public PortalPosition(BlockPos var2, long var3) {
         super(☃.getX(), ☃.getY(), ☃.getZ());
         this.lastUpdateTime = ☃;
      }
   }
}
