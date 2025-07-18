package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EmptyChunk extends Chunk {
   public EmptyChunk(World var1, int var2, int var3) {
      super(☃, ☃, ☃);
   }

   @Override
   public boolean isAtLocation(int var1, int var2) {
      return ☃ == this.x && ☃ == this.z;
   }

   @Override
   public int getHeightValue(int var1, int var2) {
      return 0;
   }

   @Override
   public void generateHeightMap() {
   }

   @Override
   public void generateSkylightMap() {
   }

   @Override
   public IBlockState getBlockState(BlockPos var1) {
      return Blocks.AIR.getDefaultState();
   }

   @Override
   public int getBlockLightOpacity(BlockPos var1) {
      return 255;
   }

   @Override
   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      return ☃.defaultLightValue;
   }

   @Override
   public void setLightFor(EnumSkyBlock var1, BlockPos var2, int var3) {
   }

   @Override
   public int getLightSubtracted(BlockPos var1, int var2) {
      return 0;
   }

   @Override
   public void addEntity(Entity var1) {
   }

   @Override
   public void removeEntity(Entity var1) {
   }

   @Override
   public void removeEntityAtIndex(Entity var1, int var2) {
   }

   @Override
   public boolean canSeeSky(BlockPos var1) {
      return false;
   }

   @Nullable
   @Override
   public TileEntity getTileEntity(BlockPos var1, Chunk.EnumCreateEntityType var2) {
      return null;
   }

   @Override
   public void addTileEntity(TileEntity var1) {
   }

   @Override
   public void addTileEntity(BlockPos var1, TileEntity var2) {
   }

   @Override
   public void removeTileEntity(BlockPos var1) {
   }

   @Override
   public void onLoad() {
   }

   @Override
   public void onUnload() {
   }

   @Override
   public void markDirty() {
   }

   @Override
   public void getEntitiesWithinAABBForEntity(@Nullable Entity var1, AxisAlignedBB var2, List<Entity> var3, Predicate<? super Entity> var4) {
   }

   @Override
   public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class<? extends T> var1, AxisAlignedBB var2, List<T> var3, Predicate<? super T> var4) {
   }

   @Override
   public boolean needsSaving(boolean var1) {
      return false;
   }

   @Override
   public Random getRandomWithSeed(long var1) {
      return new Random(this.getWorld().getSeed() + this.x * this.x * 4987142 + this.x * 5947611 + this.z * this.z * 4392871L + this.z * 389711 ^ ☃);
   }

   @Override
   public boolean isEmpty() {
      return true;
   }

   @Override
   public boolean isEmptyBetween(int var1, int var2) {
      return true;
   }
}
