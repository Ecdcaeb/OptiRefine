package net.minecraft.util.math;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Immutable
public class BlockPos extends Vec3i {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
   private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
   private static final int NUM_Z_BITS = NUM_X_BITS;
   private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
   private static final int Y_SHIFT = 0 + NUM_Z_BITS;
   private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
   private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
   private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
   private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

   public BlockPos(int var1, int var2, int var3) {
      super(☃, ☃, ☃);
   }

   public BlockPos(double var1, double var3, double var5) {
      super(☃, ☃, ☃);
   }

   public BlockPos(Entity var1) {
      this(☃.posX, ☃.posY, ☃.posZ);
   }

   public BlockPos(Vec3d var1) {
      this(☃.x, ☃.y, ☃.z);
   }

   public BlockPos(Vec3i var1) {
      this(☃.getX(), ☃.getY(), ☃.getZ());
   }

   public BlockPos add(double var1, double var3, double var5) {
      return ☃ == 0.0 && ☃ == 0.0 && ☃ == 0.0 ? this : new BlockPos(this.getX() + ☃, this.getY() + ☃, this.getZ() + ☃);
   }

   public BlockPos add(int var1, int var2, int var3) {
      return ☃ == 0 && ☃ == 0 && ☃ == 0 ? this : new BlockPos(this.getX() + ☃, this.getY() + ☃, this.getZ() + ☃);
   }

   public BlockPos add(Vec3i var1) {
      return this.add(☃.getX(), ☃.getY(), ☃.getZ());
   }

   public BlockPos subtract(Vec3i var1) {
      return this.add(-☃.getX(), -☃.getY(), -☃.getZ());
   }

   public BlockPos up() {
      return this.up(1);
   }

   public BlockPos up(int var1) {
      return this.offset(EnumFacing.UP, ☃);
   }

   public BlockPos down() {
      return this.down(1);
   }

   public BlockPos down(int var1) {
      return this.offset(EnumFacing.DOWN, ☃);
   }

   public BlockPos north() {
      return this.north(1);
   }

   public BlockPos north(int var1) {
      return this.offset(EnumFacing.NORTH, ☃);
   }

   public BlockPos south() {
      return this.south(1);
   }

   public BlockPos south(int var1) {
      return this.offset(EnumFacing.SOUTH, ☃);
   }

   public BlockPos west() {
      return this.west(1);
   }

   public BlockPos west(int var1) {
      return this.offset(EnumFacing.WEST, ☃);
   }

   public BlockPos east() {
      return this.east(1);
   }

   public BlockPos east(int var1) {
      return this.offset(EnumFacing.EAST, ☃);
   }

   public BlockPos offset(EnumFacing var1) {
      return this.offset(☃, 1);
   }

   public BlockPos offset(EnumFacing var1, int var2) {
      return ☃ == 0 ? this : new BlockPos(this.getX() + ☃.getXOffset() * ☃, this.getY() + ☃.getYOffset() * ☃, this.getZ() + ☃.getZOffset() * ☃);
   }

   public BlockPos rotate(Rotation var1) {
      switch (☃) {
         case NONE:
         default:
            return this;
         case CLOCKWISE_90:
            return new BlockPos(-this.getZ(), this.getY(), this.getX());
         case CLOCKWISE_180:
            return new BlockPos(-this.getX(), this.getY(), -this.getZ());
         case COUNTERCLOCKWISE_90:
            return new BlockPos(this.getZ(), this.getY(), -this.getX());
      }
   }

   public BlockPos crossProduct(Vec3i var1) {
      return new BlockPos(
         this.getY() * ☃.getZ() - this.getZ() * ☃.getY(), this.getZ() * ☃.getX() - this.getX() * ☃.getZ(), this.getX() * ☃.getY() - this.getY() * ☃.getX()
      );
   }

   public long toLong() {
      return (this.getX() & X_MASK) << X_SHIFT | (this.getY() & Y_MASK) << Y_SHIFT | (this.getZ() & Z_MASK) << 0;
   }

   public static BlockPos fromLong(long var0) {
      int ☃ = (int)(☃ << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
      int ☃x = (int)(☃ << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
      int ☃xx = (int)(☃ << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
      return new BlockPos(☃, ☃x, ☃xx);
   }

   public static Iterable<BlockPos> getAllInBox(BlockPos var0, BlockPos var1) {
      return getAllInBox(
         Math.min(☃.getX(), ☃.getX()),
         Math.min(☃.getY(), ☃.getY()),
         Math.min(☃.getZ(), ☃.getZ()),
         Math.max(☃.getX(), ☃.getX()),
         Math.max(☃.getY(), ☃.getY()),
         Math.max(☃.getZ(), ☃.getZ())
      );
   }

   public static Iterable<BlockPos> getAllInBox(final int var0, final int var1, final int var2, final int var3, final int var4, final int var5) {
      return new Iterable<BlockPos>() {
         @Override
         public Iterator<BlockPos> iterator() {
            return new AbstractIterator<BlockPos>() {
               private boolean first = true;
               private int lastPosX;
               private int lastPosY;
               private int lastPosZ;

               protected BlockPos computeNext() {
                  if (this.first) {
                     this.first = false;
                     this.lastPosX = ☃;
                     this.lastPosY = ☃;
                     this.lastPosZ = ☃;
                     return new BlockPos(☃, ☃, ☃);
                  } else if (this.lastPosX == ☃ && this.lastPosY == ☃ && this.lastPosZ == ☃) {
                     return (BlockPos)this.endOfData();
                  } else {
                     if (this.lastPosX < ☃) {
                        this.lastPosX++;
                     } else if (this.lastPosY < ☃) {
                        this.lastPosX = ☃;
                        this.lastPosY++;
                     } else if (this.lastPosZ < ☃) {
                        this.lastPosX = ☃;
                        this.lastPosY = ☃;
                        this.lastPosZ++;
                     }

                     return new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ);
                  }
               }
            };
         }
      };
   }

   public BlockPos toImmutable() {
      return this;
   }

   public static Iterable<BlockPos.MutableBlockPos> getAllInBoxMutable(BlockPos var0, BlockPos var1) {
      return getAllInBoxMutable(
         Math.min(☃.getX(), ☃.getX()),
         Math.min(☃.getY(), ☃.getY()),
         Math.min(☃.getZ(), ☃.getZ()),
         Math.max(☃.getX(), ☃.getX()),
         Math.max(☃.getY(), ☃.getY()),
         Math.max(☃.getZ(), ☃.getZ())
      );
   }

   public static Iterable<BlockPos.MutableBlockPos> getAllInBoxMutable(
      final int var0, final int var1, final int var2, final int var3, final int var4, final int var5
   ) {
      return new Iterable<BlockPos.MutableBlockPos>() {
         @Override
         public Iterator<BlockPos.MutableBlockPos> iterator() {
            return new AbstractIterator<BlockPos.MutableBlockPos>() {
               private BlockPos.MutableBlockPos pos;

               protected BlockPos.MutableBlockPos computeNext() {
                  if (this.pos == null) {
                     this.pos = new BlockPos.MutableBlockPos(☃, ☃, ☃);
                     return this.pos;
                  } else if (this.pos.x == ☃ && this.pos.y == ☃ && this.pos.z == ☃) {
                     return (BlockPos.MutableBlockPos)this.endOfData();
                  } else {
                     if (this.pos.x < ☃) {
                        this.pos.x++;
                     } else if (this.pos.y < ☃) {
                        this.pos.x = ☃;
                        this.pos.y++;
                     } else if (this.pos.z < ☃) {
                        this.pos.x = ☃;
                        this.pos.y = ☃;
                        this.pos.z++;
                     }

                     return this.pos;
                  }
               }
            };
         }
      };
   }

   public static class MutableBlockPos extends BlockPos {
      protected int x;
      protected int y;
      protected int z;

      public MutableBlockPos() {
         this(0, 0, 0);
      }

      public MutableBlockPos(BlockPos var1) {
         this(☃.getX(), ☃.getY(), ☃.getZ());
      }

      public MutableBlockPos(int var1, int var2, int var3) {
         super(0, 0, 0);
         this.x = ☃;
         this.y = ☃;
         this.z = ☃;
      }

      @Override
      public BlockPos add(double var1, double var3, double var5) {
         return super.add(☃, ☃, ☃).toImmutable();
      }

      @Override
      public BlockPos add(int var1, int var2, int var3) {
         return super.add(☃, ☃, ☃).toImmutable();
      }

      @Override
      public BlockPos offset(EnumFacing var1, int var2) {
         return super.offset(☃, ☃).toImmutable();
      }

      @Override
      public BlockPos rotate(Rotation var1) {
         return super.rotate(☃).toImmutable();
      }

      @Override
      public int getX() {
         return this.x;
      }

      @Override
      public int getY() {
         return this.y;
      }

      @Override
      public int getZ() {
         return this.z;
      }

      public BlockPos.MutableBlockPos setPos(int var1, int var2, int var3) {
         this.x = ☃;
         this.y = ☃;
         this.z = ☃;
         return this;
      }

      public BlockPos.MutableBlockPos setPos(Entity var1) {
         return this.setPos(☃.posX, ☃.posY, ☃.posZ);
      }

      public BlockPos.MutableBlockPos setPos(double var1, double var3, double var5) {
         return this.setPos(MathHelper.floor(☃), MathHelper.floor(☃), MathHelper.floor(☃));
      }

      public BlockPos.MutableBlockPos setPos(Vec3i var1) {
         return this.setPos(☃.getX(), ☃.getY(), ☃.getZ());
      }

      public BlockPos.MutableBlockPos move(EnumFacing var1) {
         return this.move(☃, 1);
      }

      public BlockPos.MutableBlockPos move(EnumFacing var1, int var2) {
         return this.setPos(this.x + ☃.getXOffset() * ☃, this.y + ☃.getYOffset() * ☃, this.z + ☃.getZOffset() * ☃);
      }

      public void setY(int var1) {
         this.y = ☃;
      }

      @Override
      public BlockPos toImmutable() {
         return new BlockPos(this);
      }
   }

   public static final class PooledMutableBlockPos extends BlockPos.MutableBlockPos {
      private boolean released;
      private static final List<BlockPos.PooledMutableBlockPos> POOL = Lists.newArrayList();

      private PooledMutableBlockPos(int var1, int var2, int var3) {
         super(☃, ☃, ☃);
      }

      public static BlockPos.PooledMutableBlockPos retain() {
         return retain(0, 0, 0);
      }

      public static BlockPos.PooledMutableBlockPos retain(double var0, double var2, double var4) {
         return retain(MathHelper.floor(☃), MathHelper.floor(☃), MathHelper.floor(☃));
      }

      public static BlockPos.PooledMutableBlockPos retain(Vec3i var0) {
         return retain(☃.getX(), ☃.getY(), ☃.getZ());
      }

      public static BlockPos.PooledMutableBlockPos retain(int var0, int var1, int var2) {
         synchronized (POOL) {
            if (!POOL.isEmpty()) {
               BlockPos.PooledMutableBlockPos ☃ = POOL.remove(POOL.size() - 1);
               if (☃ != null && ☃.released) {
                  ☃.released = false;
                  ☃.setPos(☃, ☃, ☃);
                  return ☃;
               }
            }
         }

         return new BlockPos.PooledMutableBlockPos(☃, ☃, ☃);
      }

      public void release() {
         synchronized (POOL) {
            if (POOL.size() < 100) {
               POOL.add(this);
            }

            this.released = true;
         }
      }

      public BlockPos.PooledMutableBlockPos setPos(int var1, int var2, int var3) {
         if (this.released) {
            BlockPos.LOGGER.error("PooledMutableBlockPosition modified after it was released.", new Throwable());
            this.released = false;
         }

         return (BlockPos.PooledMutableBlockPos)super.setPos(☃, ☃, ☃);
      }

      public BlockPos.PooledMutableBlockPos setPos(Entity var1) {
         return (BlockPos.PooledMutableBlockPos)super.setPos(☃);
      }

      public BlockPos.PooledMutableBlockPos setPos(double var1, double var3, double var5) {
         return (BlockPos.PooledMutableBlockPos)super.setPos(☃, ☃, ☃);
      }

      public BlockPos.PooledMutableBlockPos setPos(Vec3i var1) {
         return (BlockPos.PooledMutableBlockPos)super.setPos(☃);
      }

      public BlockPos.PooledMutableBlockPos move(EnumFacing var1) {
         return (BlockPos.PooledMutableBlockPos)super.move(☃);
      }

      public BlockPos.PooledMutableBlockPos move(EnumFacing var1, int var2) {
         return (BlockPos.PooledMutableBlockPos)super.move(☃, ☃);
      }
   }
}
