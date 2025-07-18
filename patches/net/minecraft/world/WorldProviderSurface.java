package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider {
   @Override
   public DimensionType getDimensionType() {
      return DimensionType.OVERWORLD;
   }

   @Override
   public boolean canDropChunk(int var1, int var2) {
      return !this.world.isSpawnChunk(☃, ☃);
   }
}
