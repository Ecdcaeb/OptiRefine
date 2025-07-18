package net.minecraft.world.biome;

public class BiomeOcean extends Biome {
   public BiomeOcean(Biome.BiomeProperties var1) {
      super(☃);
      this.spawnableCreatureList.clear();
   }

   @Override
   public Biome.TempCategory getTempCategory() {
      return Biome.TempCategory.OCEAN;
   }
}
