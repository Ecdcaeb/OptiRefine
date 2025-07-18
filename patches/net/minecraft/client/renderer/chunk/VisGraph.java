package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Queues;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntegerCache;
import net.minecraft.util.math.BlockPos;

public class VisGraph {
   private static final int DX = (int)Math.pow(16.0, 0.0);
   private static final int DZ = (int)Math.pow(16.0, 1.0);
   private static final int DY = (int)Math.pow(16.0, 2.0);
   private final BitSet bitSet = new BitSet(4096);
   private static final int[] INDEX_OF_EDGES = new int[1352];
   private int empty = 4096;

   public void setOpaqueCube(BlockPos var1) {
      this.bitSet.set(getIndex(☃), true);
      this.empty--;
   }

   private static int getIndex(BlockPos var0) {
      return getIndex(☃.getX() & 15, ☃.getY() & 15, ☃.getZ() & 15);
   }

   private static int getIndex(int var0, int var1, int var2) {
      return ☃ << 0 | ☃ << 8 | ☃ << 4;
   }

   public SetVisibility computeVisibility() {
      SetVisibility ☃ = new SetVisibility();
      if (4096 - this.empty < 256) {
         ☃.setAllVisible(true);
      } else if (this.empty == 0) {
         ☃.setAllVisible(false);
      } else {
         for (int ☃x : INDEX_OF_EDGES) {
            if (!this.bitSet.get(☃x)) {
               ☃.setManyVisible(this.floodFill(☃x));
            }
         }
      }

      return ☃;
   }

   public Set<EnumFacing> getVisibleFacings(BlockPos var1) {
      return this.floodFill(getIndex(☃));
   }

   private Set<EnumFacing> floodFill(int var1) {
      Set<EnumFacing> ☃ = EnumSet.noneOf(EnumFacing.class);
      Queue<Integer> ☃x = Queues.newArrayDeque();
      ☃x.add(IntegerCache.getInteger(☃));
      this.bitSet.set(☃, true);

      while (!☃x.isEmpty()) {
         int ☃xx = ☃x.poll();
         this.addEdges(☃xx, ☃);

         for (EnumFacing ☃xxx : EnumFacing.values()) {
            int ☃xxxx = this.getNeighborIndexAtFace(☃xx, ☃xxx);
            if (☃xxxx >= 0 && !this.bitSet.get(☃xxxx)) {
               this.bitSet.set(☃xxxx, true);
               ☃x.add(IntegerCache.getInteger(☃xxxx));
            }
         }
      }

      return ☃;
   }

   private void addEdges(int var1, Set<EnumFacing> var2) {
      int ☃ = ☃ >> 0 & 15;
      if (☃ == 0) {
         ☃.add(EnumFacing.WEST);
      } else if (☃ == 15) {
         ☃.add(EnumFacing.EAST);
      }

      int ☃x = ☃ >> 8 & 15;
      if (☃x == 0) {
         ☃.add(EnumFacing.DOWN);
      } else if (☃x == 15) {
         ☃.add(EnumFacing.UP);
      }

      int ☃xx = ☃ >> 4 & 15;
      if (☃xx == 0) {
         ☃.add(EnumFacing.NORTH);
      } else if (☃xx == 15) {
         ☃.add(EnumFacing.SOUTH);
      }
   }

   private int getNeighborIndexAtFace(int var1, EnumFacing var2) {
      switch (☃) {
         case DOWN:
            if ((☃ >> 8 & 15) == 0) {
               return -1;
            }

            return ☃ - DY;
         case UP:
            if ((☃ >> 8 & 15) == 15) {
               return -1;
            }

            return ☃ + DY;
         case NORTH:
            if ((☃ >> 4 & 15) == 0) {
               return -1;
            }

            return ☃ - DZ;
         case SOUTH:
            if ((☃ >> 4 & 15) == 15) {
               return -1;
            }

            return ☃ + DZ;
         case WEST:
            if ((☃ >> 0 & 15) == 0) {
               return -1;
            }

            return ☃ - DX;
         case EAST:
            if ((☃ >> 0 & 15) == 15) {
               return -1;
            }

            return ☃ + DX;
         default:
            return -1;
      }
   }

   static {
      int ☃ = 0;
      int ☃x = 15;
      int ☃xx = 0;

      for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
         for (int ☃xxxx = 0; ☃xxxx < 16; ☃xxxx++) {
            for (int ☃xxxxx = 0; ☃xxxxx < 16; ☃xxxxx++) {
               if (☃xxx == 0 || ☃xxx == 15 || ☃xxxx == 0 || ☃xxxx == 15 || ☃xxxxx == 0 || ☃xxxxx == 15) {
                  INDEX_OF_EDGES[☃xx++] = getIndex(☃xxx, ☃xxxx, ☃xxxxx);
               }
            }
         }
      }
   }
}
