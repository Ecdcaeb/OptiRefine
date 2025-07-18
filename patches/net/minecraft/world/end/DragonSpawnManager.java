package net.minecraft.world.end;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.gen.feature.WorldGenSpikes;

public enum DragonSpawnManager {
   START {
      @Override
      public void process(WorldServer var1, DragonFightManager var2, List<EntityEnderCrystal> var3, int var4, BlockPos var5) {
         BlockPos ☃ = new BlockPos(0, 128, 0);

         for (EntityEnderCrystal ☃x : ☃) {
            ☃x.setBeamTarget(☃);
         }

         ☃.setRespawnState(PREPARING_TO_SUMMON_PILLARS);
      }
   },
   PREPARING_TO_SUMMON_PILLARS {
      @Override
      public void process(WorldServer var1, DragonFightManager var2, List<EntityEnderCrystal> var3, int var4, BlockPos var5) {
         if (☃ < 100) {
            if (☃ == 0 || ☃ == 50 || ☃ == 51 || ☃ == 52 || ☃ >= 95) {
               ☃.playEvent(3001, new BlockPos(0, 128, 0), 0);
            }
         } else {
            ☃.setRespawnState(SUMMONING_PILLARS);
         }
      }
   },
   SUMMONING_PILLARS {
      @Override
      public void process(WorldServer var1, DragonFightManager var2, List<EntityEnderCrystal> var3, int var4, BlockPos var5) {
         int ☃ = 40;
         boolean ☃x = ☃ % 40 == 0;
         boolean ☃xx = ☃ % 40 == 39;
         if (☃x || ☃xx) {
            WorldGenSpikes.EndSpike[] ☃xxx = BiomeEndDecorator.getSpikesForWorld(☃);
            int ☃xxxx = ☃ / 40;
            if (☃xxxx < ☃xxx.length) {
               WorldGenSpikes.EndSpike ☃xxxxx = ☃xxx[☃xxxx];
               if (☃x) {
                  for (EntityEnderCrystal ☃xxxxxx : ☃) {
                     ☃xxxxxx.setBeamTarget(new BlockPos(☃xxxxx.getCenterX(), ☃xxxxx.getHeight() + 1, ☃xxxxx.getCenterZ()));
                  }
               } else {
                  int ☃xxxxxx = 10;

                  for (BlockPos.MutableBlockPos ☃xxxxxxx : BlockPos.getAllInBoxMutable(
                     new BlockPos(☃xxxxx.getCenterX() - 10, ☃xxxxx.getHeight() - 10, ☃xxxxx.getCenterZ() - 10),
                     new BlockPos(☃xxxxx.getCenterX() + 10, ☃xxxxx.getHeight() + 10, ☃xxxxx.getCenterZ() + 10)
                  )) {
                     ☃.setBlockToAir(☃xxxxxxx);
                  }

                  ☃.createExplosion(null, ☃xxxxx.getCenterX() + 0.5F, ☃xxxxx.getHeight(), ☃xxxxx.getCenterZ() + 0.5F, 5.0F, true);
                  WorldGenSpikes ☃xxxxxxx = new WorldGenSpikes();
                  ☃xxxxxxx.setSpike(☃xxxxx);
                  ☃xxxxxxx.setCrystalInvulnerable(true);
                  ☃xxxxxxx.setBeamTarget(new BlockPos(0, 128, 0));
                  ☃xxxxxxx.generate(☃, new Random(), new BlockPos(☃xxxxx.getCenterX(), 45, ☃xxxxx.getCenterZ()));
               }
            } else if (☃x) {
               ☃.setRespawnState(SUMMONING_DRAGON);
            }
         }
      }
   },
   SUMMONING_DRAGON {
      @Override
      public void process(WorldServer var1, DragonFightManager var2, List<EntityEnderCrystal> var3, int var4, BlockPos var5) {
         if (☃ >= 100) {
            ☃.setRespawnState(END);
            ☃.resetSpikeCrystals();

            for (EntityEnderCrystal ☃ : ☃) {
               ☃.setBeamTarget(null);
               ☃.createExplosion(☃, ☃.posX, ☃.posY, ☃.posZ, 6.0F, false);
               ☃.setDead();
            }
         } else if (☃ >= 80) {
            ☃.playEvent(3001, new BlockPos(0, 128, 0), 0);
         } else if (☃ == 0) {
            for (EntityEnderCrystal ☃ : ☃) {
               ☃.setBeamTarget(new BlockPos(0, 128, 0));
            }
         } else if (☃ < 5) {
            ☃.playEvent(3001, new BlockPos(0, 128, 0), 0);
         }
      }
   },
   END {
      @Override
      public void process(WorldServer var1, DragonFightManager var2, List<EntityEnderCrystal> var3, int var4, BlockPos var5) {
      }
   };

   private DragonSpawnManager() {
   }

   public abstract void process(WorldServer var1, DragonFightManager var2, List<EntityEnderCrystal> var3, int var4, BlockPos var5);
}
