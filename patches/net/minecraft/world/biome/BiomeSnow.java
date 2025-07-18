package net.minecraft.world.biome;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeSnow extends Biome {
   private final boolean superIcy;
   private final WorldGenIceSpike iceSpike = new WorldGenIceSpike();
   private final WorldGenIcePath icePatch = new WorldGenIcePath(4);

   public BiomeSnow(boolean var1, Biome.BiomeProperties var2) {
      super(☃);
      this.superIcy = ☃;
      if (☃) {
         this.topBlock = Blocks.SNOW.getDefaultState();
      }

      this.spawnableCreatureList.clear();
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 10, 2, 3));
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
      Iterator<Biome.SpawnListEntry> ☃ = this.spawnableMonsterList.iterator();

      while (☃.hasNext()) {
         Biome.SpawnListEntry ☃x = ☃.next();
         if (☃x.entityClass == EntitySkeleton.class) {
            ☃.remove();
         }
      }

      this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 20, 4, 4));
      this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityStray.class, 80, 4, 4));
   }

   @Override
   public float getSpawningChance() {
      return 0.07F;
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      if (this.superIcy) {
         for (int ☃ = 0; ☃ < 3; ☃++) {
            int ☃x = ☃.nextInt(16) + 8;
            int ☃xx = ☃.nextInt(16) + 8;
            this.iceSpike.generate(☃, ☃, ☃.getHeight(☃.add(☃x, 0, ☃xx)));
         }

         for (int ☃ = 0; ☃ < 2; ☃++) {
            int ☃x = ☃.nextInt(16) + 8;
            int ☃xx = ☃.nextInt(16) + 8;
            this.icePatch.generate(☃, ☃, ☃.getHeight(☃.add(☃x, 0, ☃xx)));
         }
      }

      super.decorate(☃, ☃, ☃);
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      return new WorldGenTaiga2(false);
   }
}
