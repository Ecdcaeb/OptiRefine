package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenFossils extends WorldGenerator {
   private static final ResourceLocation STRUCTURE_SPINE_01 = new ResourceLocation("fossils/fossil_spine_01");
   private static final ResourceLocation STRUCTURE_SPINE_02 = new ResourceLocation("fossils/fossil_spine_02");
   private static final ResourceLocation STRUCTURE_SPINE_03 = new ResourceLocation("fossils/fossil_spine_03");
   private static final ResourceLocation STRUCTURE_SPINE_04 = new ResourceLocation("fossils/fossil_spine_04");
   private static final ResourceLocation STRUCTURE_SPINE_01_COAL = new ResourceLocation("fossils/fossil_spine_01_coal");
   private static final ResourceLocation STRUCTURE_SPINE_02_COAL = new ResourceLocation("fossils/fossil_spine_02_coal");
   private static final ResourceLocation STRUCTURE_SPINE_03_COAL = new ResourceLocation("fossils/fossil_spine_03_coal");
   private static final ResourceLocation STRUCTURE_SPINE_04_COAL = new ResourceLocation("fossils/fossil_spine_04_coal");
   private static final ResourceLocation STRUCTURE_SKULL_01 = new ResourceLocation("fossils/fossil_skull_01");
   private static final ResourceLocation STRUCTURE_SKULL_02 = new ResourceLocation("fossils/fossil_skull_02");
   private static final ResourceLocation STRUCTURE_SKULL_03 = new ResourceLocation("fossils/fossil_skull_03");
   private static final ResourceLocation STRUCTURE_SKULL_04 = new ResourceLocation("fossils/fossil_skull_04");
   private static final ResourceLocation STRUCTURE_SKULL_01_COAL = new ResourceLocation("fossils/fossil_skull_01_coal");
   private static final ResourceLocation STRUCTURE_SKULL_02_COAL = new ResourceLocation("fossils/fossil_skull_02_coal");
   private static final ResourceLocation STRUCTURE_SKULL_03_COAL = new ResourceLocation("fossils/fossil_skull_03_coal");
   private static final ResourceLocation STRUCTURE_SKULL_04_COAL = new ResourceLocation("fossils/fossil_skull_04_coal");
   private static final ResourceLocation[] FOSSILS = new ResourceLocation[]{
      STRUCTURE_SPINE_01,
      STRUCTURE_SPINE_02,
      STRUCTURE_SPINE_03,
      STRUCTURE_SPINE_04,
      STRUCTURE_SKULL_01,
      STRUCTURE_SKULL_02,
      STRUCTURE_SKULL_03,
      STRUCTURE_SKULL_04
   };
   private static final ResourceLocation[] FOSSILS_COAL = new ResourceLocation[]{
      STRUCTURE_SPINE_01_COAL,
      STRUCTURE_SPINE_02_COAL,
      STRUCTURE_SPINE_03_COAL,
      STRUCTURE_SPINE_04_COAL,
      STRUCTURE_SKULL_01_COAL,
      STRUCTURE_SKULL_02_COAL,
      STRUCTURE_SKULL_03_COAL,
      STRUCTURE_SKULL_04_COAL
   };

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      Random ☃ = ☃.getChunk(☃).getRandomWithSeed(987234911L);
      MinecraftServer ☃x = ☃.getMinecraftServer();
      Rotation[] ☃xx = Rotation.values();
      Rotation ☃xxx = ☃xx[☃.nextInt(☃xx.length)];
      int ☃xxxx = ☃.nextInt(FOSSILS.length);
      TemplateManager ☃xxxxx = ☃.getSaveHandler().getStructureTemplateManager();
      Template ☃xxxxxx = ☃xxxxx.getTemplate(☃x, FOSSILS[☃xxxx]);
      Template ☃xxxxxxx = ☃xxxxx.getTemplate(☃x, FOSSILS_COAL[☃xxxx]);
      ChunkPos ☃xxxxxxxx = new ChunkPos(☃);
      StructureBoundingBox ☃xxxxxxxxx = new StructureBoundingBox(☃xxxxxxxx.getXStart(), 0, ☃xxxxxxxx.getZStart(), ☃xxxxxxxx.getXEnd(), 256, ☃xxxxxxxx.getZEnd());
      PlacementSettings ☃xxxxxxxxxx = new PlacementSettings().setRotation(☃xxx).setBoundingBox(☃xxxxxxxxx).setRandom(☃);
      BlockPos ☃xxxxxxxxxxx = ☃xxxxxx.transformedSize(☃xxx);
      int ☃xxxxxxxxxxxx = ☃.nextInt(16 - ☃xxxxxxxxxxx.getX());
      int ☃xxxxxxxxxxxxx = ☃.nextInt(16 - ☃xxxxxxxxxxx.getZ());
      int ☃xxxxxxxxxxxxxx = 256;

      for (int ☃xxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxx < ☃xxxxxxxxxxx.getX(); ☃xxxxxxxxxxxxxxx++) {
         for (int ☃xxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxx < ☃xxxxxxxxxxx.getX(); ☃xxxxxxxxxxxxxxxx++) {
            ☃xxxxxxxxxxxxxx = Math.min(☃xxxxxxxxxxxxxx, ☃.getHeight(☃.getX() + ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxx, ☃.getZ() + ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxx));
         }
      }

      int ☃xxxxxxxxxxxxxxx = Math.max(☃xxxxxxxxxxxxxx - 15 - ☃.nextInt(10), 10);
      BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxx.getZeroPositionWithTransform(☃.add(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx), Mirror.NONE, ☃xxx);
      ☃xxxxxxxxxx.setIntegrity(0.9F);
      ☃xxxxxx.addBlocksToWorld(☃, ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxx, 20);
      ☃xxxxxxxxxx.setIntegrity(0.1F);
      ☃xxxxxxx.addBlocksToWorld(☃, ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxx, 20);
      return true;
   }
}
