package net.minecraft.world.biome;

public class BiomeVoid extends Biome {
   public BiomeVoid(Biome.BiomeProperties var1) {
      super(☃);
      this.spawnableMonsterList.clear();
      this.spawnableCreatureList.clear();
      this.spawnableWaterCreatureList.clear();
      this.spawnableCaveCreatureList.clear();
      this.decorator = new BiomeVoidDecorator();
   }

   @Override
   public boolean ignorePlayerSpawnSuitability() {
      return true;
   }
}
