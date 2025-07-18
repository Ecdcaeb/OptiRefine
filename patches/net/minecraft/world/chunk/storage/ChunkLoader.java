package net.minecraft.world.chunk.storage;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.NibbleArray;

public class ChunkLoader {
   public static ChunkLoader.AnvilConverterData load(NBTTagCompound var0) {
      int ☃ = ☃.getInteger("xPos");
      int ☃x = ☃.getInteger("zPos");
      ChunkLoader.AnvilConverterData ☃xx = new ChunkLoader.AnvilConverterData(☃, ☃x);
      ☃xx.blocks = ☃.getByteArray("Blocks");
      ☃xx.data = new NibbleArrayReader(☃.getByteArray("Data"), 7);
      ☃xx.skyLight = new NibbleArrayReader(☃.getByteArray("SkyLight"), 7);
      ☃xx.blockLight = new NibbleArrayReader(☃.getByteArray("BlockLight"), 7);
      ☃xx.heightmap = ☃.getByteArray("HeightMap");
      ☃xx.terrainPopulated = ☃.getBoolean("TerrainPopulated");
      ☃xx.entities = ☃.getTagList("Entities", 10);
      ☃xx.tileEntities = ☃.getTagList("TileEntities", 10);
      ☃xx.tileTicks = ☃.getTagList("TileTicks", 10);

      try {
         ☃xx.lastUpdated = ☃.getLong("LastUpdate");
      } catch (ClassCastException var5) {
         ☃xx.lastUpdated = ☃.getInteger("LastUpdate");
      }

      return ☃xx;
   }

   public static void convertToAnvilFormat(ChunkLoader.AnvilConverterData var0, NBTTagCompound var1, BiomeProvider var2) {
      ☃.setInteger("xPos", ☃.x);
      ☃.setInteger("zPos", ☃.z);
      ☃.setLong("LastUpdate", ☃.lastUpdated);
      int[] ☃ = new int[☃.heightmap.length];

      for (int ☃x = 0; ☃x < ☃.heightmap.length; ☃x++) {
         ☃[☃x] = ☃.heightmap[☃x];
      }

      ☃.setIntArray("HeightMap", ☃);
      ☃.setBoolean("TerrainPopulated", ☃.terrainPopulated);
      NBTTagList ☃x = new NBTTagList();

      for (int ☃xx = 0; ☃xx < 8; ☃xx++) {
         boolean ☃xxx = true;

         for (int ☃xxxx = 0; ☃xxxx < 16 && ☃xxx; ☃xxxx++) {
            for (int ☃xxxxx = 0; ☃xxxxx < 16 && ☃xxx; ☃xxxxx++) {
               for (int ☃xxxxxx = 0; ☃xxxxxx < 16; ☃xxxxxx++) {
                  int ☃xxxxxxx = ☃xxxx << 11 | ☃xxxxxx << 7 | ☃xxxxx + (☃xx << 4);
                  int ☃xxxxxxxx = ☃.blocks[☃xxxxxxx];
                  if (☃xxxxxxxx != 0) {
                     ☃xxx = false;
                     break;
                  }
               }
            }
         }

         if (!☃xxx) {
            byte[] ☃xxxx = new byte[4096];
            NibbleArray ☃xxxxx = new NibbleArray();
            NibbleArray ☃xxxxxxx = new NibbleArray();
            NibbleArray ☃xxxxxxxx = new NibbleArray();

            for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 16; ☃xxxxxxxxx++) {
               for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < 16; ☃xxxxxxxxxx++) {
                  for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < 16; ☃xxxxxxxxxxx++) {
                     int ☃xxxxxxxxxxxx = ☃xxxxxxxxx << 11 | ☃xxxxxxxxxxx << 7 | ☃xxxxxxxxxx + (☃xx << 4);
                     int ☃xxxxxxxxxxxxx = ☃.blocks[☃xxxxxxxxxxxx];
                     ☃xxxx[☃xxxxxxxxxx << 8 | ☃xxxxxxxxxxx << 4 | ☃xxxxxxxxx] = (byte)(☃xxxxxxxxxxxxx & 0xFF);
                     ☃xxxxx.set(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃.data.get(☃xxxxxxxxx, ☃xxxxxxxxxx + (☃xx << 4), ☃xxxxxxxxxxx));
                     ☃xxxxxxx.set(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃.skyLight.get(☃xxxxxxxxx, ☃xxxxxxxxxx + (☃xx << 4), ☃xxxxxxxxxxx));
                     ☃xxxxxxxx.set(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃.blockLight.get(☃xxxxxxxxx, ☃xxxxxxxxxx + (☃xx << 4), ☃xxxxxxxxxxx));
                  }
               }
            }

            NBTTagCompound ☃xxxxxxxxx = new NBTTagCompound();
            ☃xxxxxxxxx.setByte("Y", (byte)(☃xx & 0xFF));
            ☃xxxxxxxxx.setByteArray("Blocks", ☃xxxx);
            ☃xxxxxxxxx.setByteArray("Data", ☃xxxxx.getData());
            ☃xxxxxxxxx.setByteArray("SkyLight", ☃xxxxxxx.getData());
            ☃xxxxxxxxx.setByteArray("BlockLight", ☃xxxxxxxx.getData());
            ☃x.appendTag(☃xxxxxxxxx);
         }
      }

      ☃.setTag("Sections", ☃x);
      byte[] ☃xx = new byte[256];
      BlockPos.MutableBlockPos ☃xxx = new BlockPos.MutableBlockPos();

      for (int ☃xxxx = 0; ☃xxxx < 16; ☃xxxx++) {
         for (int ☃xxxxx = 0; ☃xxxxx < 16; ☃xxxxx++) {
            ☃xxx.setPos(☃.x << 4 | ☃xxxx, 0, ☃.z << 4 | ☃xxxxx);
            ☃xx[☃xxxxx << 4 | ☃xxxx] = (byte)(Biome.getIdForBiome(☃.getBiome(☃xxx, Biomes.DEFAULT)) & 0xFF);
         }
      }

      ☃.setByteArray("Biomes", ☃xx);
      ☃.setTag("Entities", ☃.entities);
      ☃.setTag("TileEntities", ☃.tileEntities);
      if (☃.tileTicks != null) {
         ☃.setTag("TileTicks", ☃.tileTicks);
      }
   }

   public static class AnvilConverterData {
      public long lastUpdated;
      public boolean terrainPopulated;
      public byte[] heightmap;
      public NibbleArrayReader blockLight;
      public NibbleArrayReader skyLight;
      public NibbleArrayReader data;
      public byte[] blocks;
      public NBTTagList entities;
      public NBTTagList tileEntities;
      public NBTTagList tileTicks;
      public final int x;
      public final int z;

      public AnvilConverterData(int var1, int var2) {
         this.x = ☃;
         this.z = ☃;
      }
   }
}
