package net.minecraft.block.state.pattern;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import javax.annotation.Nullable;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class BlockPattern {
   private final Predicate<BlockWorldState>[][][] blockMatches;
   private final int fingerLength;
   private final int thumbLength;
   private final int palmLength;

   public BlockPattern(Predicate<BlockWorldState>[][][] var1) {
      this.blockMatches = ☃;
      this.fingerLength = ☃.length;
      if (this.fingerLength > 0) {
         this.thumbLength = ☃[0].length;
         if (this.thumbLength > 0) {
            this.palmLength = ☃[0][0].length;
         } else {
            this.palmLength = 0;
         }
      } else {
         this.thumbLength = 0;
         this.palmLength = 0;
      }
   }

   public int getFingerLength() {
      return this.fingerLength;
   }

   public int getThumbLength() {
      return this.thumbLength;
   }

   public int getPalmLength() {
      return this.palmLength;
   }

   @Nullable
   private BlockPattern.PatternHelper checkPatternAt(BlockPos var1, EnumFacing var2, EnumFacing var3, LoadingCache<BlockPos, BlockWorldState> var4) {
      for (int ☃ = 0; ☃ < this.palmLength; ☃++) {
         for (int ☃x = 0; ☃x < this.thumbLength; ☃x++) {
            for (int ☃xx = 0; ☃xx < this.fingerLength; ☃xx++) {
               if (!this.blockMatches[☃xx][☃x][☃].apply(☃.getUnchecked(translateOffset(☃, ☃, ☃, ☃, ☃x, ☃xx)))) {
                  return null;
               }
            }
         }
      }

      return new BlockPattern.PatternHelper(☃, ☃, ☃, ☃, this.palmLength, this.thumbLength, this.fingerLength);
   }

   @Nullable
   public BlockPattern.PatternHelper match(World var1, BlockPos var2) {
      LoadingCache<BlockPos, BlockWorldState> ☃ = createLoadingCache(☃, false);
      int ☃x = Math.max(Math.max(this.palmLength, this.thumbLength), this.fingerLength);

      for (BlockPos ☃xx : BlockPos.getAllInBox(☃, ☃.add(☃x - 1, ☃x - 1, ☃x - 1))) {
         for (EnumFacing ☃xxx : EnumFacing.values()) {
            for (EnumFacing ☃xxxx : EnumFacing.values()) {
               if (☃xxxx != ☃xxx && ☃xxxx != ☃xxx.getOpposite()) {
                  BlockPattern.PatternHelper ☃xxxxx = this.checkPatternAt(☃xx, ☃xxx, ☃xxxx, ☃);
                  if (☃xxxxx != null) {
                     return ☃xxxxx;
                  }
               }
            }
         }
      }

      return null;
   }

   public static LoadingCache<BlockPos, BlockWorldState> createLoadingCache(World var0, boolean var1) {
      return CacheBuilder.newBuilder().build(new BlockPattern.CacheLoader(☃, ☃));
   }

   protected static BlockPos translateOffset(BlockPos var0, EnumFacing var1, EnumFacing var2, int var3, int var4, int var5) {
      if (☃ != ☃ && ☃ != ☃.getOpposite()) {
         Vec3i ☃ = new Vec3i(☃.getXOffset(), ☃.getYOffset(), ☃.getZOffset());
         Vec3i ☃x = new Vec3i(☃.getXOffset(), ☃.getYOffset(), ☃.getZOffset());
         Vec3i ☃xx = ☃.crossProduct(☃x);
         return ☃.add(
            ☃x.getX() * -☃ + ☃xx.getX() * ☃ + ☃.getX() * ☃, ☃x.getY() * -☃ + ☃xx.getY() * ☃ + ☃.getY() * ☃, ☃x.getZ() * -☃ + ☃xx.getZ() * ☃ + ☃.getZ() * ☃
         );
      } else {
         throw new IllegalArgumentException("Invalid forwards & up combination");
      }
   }

   static class CacheLoader extends com.google.common.cache.CacheLoader<BlockPos, BlockWorldState> {
      private final World world;
      private final boolean forceLoad;

      public CacheLoader(World var1, boolean var2) {
         this.world = ☃;
         this.forceLoad = ☃;
      }

      public BlockWorldState load(BlockPos var1) throws Exception {
         return new BlockWorldState(this.world, ☃, this.forceLoad);
      }
   }

   public static class PatternHelper {
      private final BlockPos frontTopLeft;
      private final EnumFacing forwards;
      private final EnumFacing up;
      private final LoadingCache<BlockPos, BlockWorldState> lcache;
      private final int width;
      private final int height;
      private final int depth;

      public PatternHelper(BlockPos var1, EnumFacing var2, EnumFacing var3, LoadingCache<BlockPos, BlockWorldState> var4, int var5, int var6, int var7) {
         this.frontTopLeft = ☃;
         this.forwards = ☃;
         this.up = ☃;
         this.lcache = ☃;
         this.width = ☃;
         this.height = ☃;
         this.depth = ☃;
      }

      public BlockPos getFrontTopLeft() {
         return this.frontTopLeft;
      }

      public EnumFacing getForwards() {
         return this.forwards;
      }

      public EnumFacing getUp() {
         return this.up;
      }

      public int getWidth() {
         return this.width;
      }

      public int getHeight() {
         return this.height;
      }

      public BlockWorldState translateOffset(int var1, int var2, int var3) {
         return (BlockWorldState)this.lcache.getUnchecked(BlockPattern.translateOffset(this.frontTopLeft, this.getForwards(), this.getUp(), ☃, ☃, ☃));
      }

      @Override
      public String toString() {
         return MoreObjects.toStringHelper(this).add("up", this.up).add("forwards", this.forwards).add("frontTopLeft", this.frontTopLeft).toString();
      }
   }
}
