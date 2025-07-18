package net.minecraft.world.biome;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeDesert extends Biome {
   public BiomeDesert(Biome.BiomeProperties var1) {
      super(☃);
      this.spawnableCreatureList.clear();
      this.topBlock = Blocks.SAND.getDefaultState();
      this.fillerBlock = Blocks.SAND.getDefaultState();
      this.decorator.treesPerChunk = -999;
      this.decorator.deadBushPerChunk = 2;
      this.decorator.reedsPerChunk = 50;
      this.decorator.cactiPerChunk = 10;
      this.spawnableCreatureList.clear();
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
      Iterator<Biome.SpawnListEntry> ☃ = this.spawnableMonsterList.iterator();

      while (☃.hasNext()) {
         Biome.SpawnListEntry ☃x = ☃.next();
         if (☃x.entityClass == EntityZombie.class || ☃x.entityClass == EntityZombieVillager.class) {
            ☃.remove();
         }
      }

      this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombie.class, 19, 4, 4));
      this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 1, 1, 1));
      this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityHusk.class, 80, 4, 4));
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      super.decorate(☃, ☃, ☃);
      if (☃.nextInt(1000) == 0) {
         int ☃ = ☃.nextInt(16) + 8;
         int ☃x = ☃.nextInt(16) + 8;
         BlockPos ☃xx = ☃.getHeight(☃.add(☃, 0, ☃x)).up();
         new WorldGenDesertWells().generate(☃, ☃, ☃xx);
      }

      if (☃.nextInt(64) == 0) {
         new WorldGenFossils().generate(☃, ☃, ☃);
      }
   }
}
