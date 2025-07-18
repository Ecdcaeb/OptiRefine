package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenNetherBridge extends MapGenStructure {
   private final List<Biome.SpawnListEntry> spawnList = Lists.newArrayList();

   public MapGenNetherBridge() {
      this.spawnList.add(new Biome.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
      this.spawnList.add(new Biome.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
      this.spawnList.add(new Biome.SpawnListEntry(EntityWitherSkeleton.class, 8, 5, 5));
      this.spawnList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 2, 5, 5));
      this.spawnList.add(new Biome.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
   }

   @Override
   public String getStructureName() {
      return "Fortress";
   }

   public List<Biome.SpawnListEntry> getSpawnList() {
      return this.spawnList;
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int ☃ = ☃ >> 4;
      int ☃x = ☃ >> 4;
      this.rand.setSeed(☃ ^ ☃x << 4 ^ this.world.getSeed());
      this.rand.nextInt();
      if (this.rand.nextInt(3) != 0) {
         return false;
      } else {
         return ☃ != (☃ << 4) + 4 + this.rand.nextInt(8) ? false : ☃ == (☃x << 4) + 4 + this.rand.nextInt(8);
      }
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      return new MapGenNetherBridge.Start(this.world, this.rand, ☃, ☃);
   }

   @Override
   public BlockPos getNearestStructurePos(World var1, BlockPos var2, boolean var3) {
      int ☃ = 1000;
      int ☃x = ☃.getX() >> 4;
      int ☃xx = ☃.getZ() >> 4;

      for (int ☃xxx = 0; ☃xxx <= 1000; ☃xxx++) {
         for (int ☃xxxx = -☃xxx; ☃xxxx <= ☃xxx; ☃xxxx++) {
            boolean ☃xxxxx = ☃xxxx == -☃xxx || ☃xxxx == ☃xxx;

            for (int ☃xxxxxx = -☃xxx; ☃xxxxxx <= ☃xxx; ☃xxxxxx++) {
               boolean ☃xxxxxxx = ☃xxxxxx == -☃xxx || ☃xxxxxx == ☃xxx;
               if (☃xxxxx || ☃xxxxxxx) {
                  int ☃xxxxxxxx = ☃x + ☃xxxx;
                  int ☃xxxxxxxxx = ☃xx + ☃xxxxxx;
                  if (this.canSpawnStructureAtCoords(☃xxxxxxxx, ☃xxxxxxxxx) && (!☃ || !☃.isChunkGeneratedAt(☃xxxxxxxx, ☃xxxxxxxxx))) {
                     return new BlockPos((☃xxxxxxxx << 4) + 8, 64, (☃xxxxxxxxx << 4) + 8);
                  }
               }
            }
         }
      }

      return null;
   }

   public static class Start extends StructureStart {
      public Start() {
      }

      public Start(World var1, Random var2, int var3, int var4) {
         super(☃, ☃);
         StructureNetherBridgePieces.Start ☃ = new StructureNetherBridgePieces.Start(☃, (☃ << 4) + 2, (☃ << 4) + 2);
         this.components.add(☃);
         ☃.buildComponent(☃, this.components, ☃);
         List<StructureComponent> ☃x = ☃.pendingChildren;

         while (!☃x.isEmpty()) {
            int ☃xx = ☃.nextInt(☃x.size());
            StructureComponent ☃xxx = ☃x.remove(☃xx);
            ☃xxx.buildComponent(☃, this.components, ☃);
         }

         this.updateBoundingBox();
         this.setRandomHeight(☃, ☃, 48, 70);
      }
   }
}
