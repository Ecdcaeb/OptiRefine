package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureVillagePieces {
   public static void registerVillagePieces() {
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House1.class, "ViBH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field1.class, "ViDF");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field2.class, "ViF");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Torch.class, "ViL");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Hall.class, "ViPH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House4Garden.class, "ViSH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.WoodHut.class, "ViSmH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Church.class, "ViST");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House2.class, "ViS");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Start.class, "ViStart");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Path.class, "ViSR");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House3.class, "ViTRH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Well.class, "ViW");
   }

   public static List<StructureVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(Random var0, int var1) {
      List<StructureVillagePieces.PieceWeight> ☃ = Lists.newArrayList();
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4, MathHelper.getInt(☃, 2 + ☃, 4 + ☃ * 2)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.getInt(☃, 0 + ☃, 1 + ☃)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20, MathHelper.getInt(☃, 0 + ☃, 2 + ☃)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3, MathHelper.getInt(☃, 2 + ☃, 5 + ☃ * 3)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15, MathHelper.getInt(☃, 0 + ☃, 2 + ☃)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3, MathHelper.getInt(☃, 1 + ☃, 4 + ☃)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3, MathHelper.getInt(☃, 2 + ☃, 4 + ☃ * 2)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15, MathHelper.getInt(☃, 0, 1 + ☃)));
      ☃.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8, MathHelper.getInt(☃, 0 + ☃, 3 + ☃ * 2)));
      Iterator<StructureVillagePieces.PieceWeight> ☃x = ☃.iterator();

      while (☃x.hasNext()) {
         if (☃x.next().villagePiecesLimit == 0) {
            ☃x.remove();
         }
      }

      return ☃;
   }

   private static int updatePieceWeight(List<StructureVillagePieces.PieceWeight> var0) {
      boolean ☃ = false;
      int ☃x = 0;

      for (StructureVillagePieces.PieceWeight ☃xx : ☃) {
         if (☃xx.villagePiecesLimit > 0 && ☃xx.villagePiecesSpawned < ☃xx.villagePiecesLimit) {
            ☃ = true;
         }

         ☃x += ☃xx.villagePieceWeight;
      }

      return ☃ ? ☃x : -1;
   }

   private static StructureVillagePieces.Village findAndCreateComponentFactory(
      StructureVillagePieces.Start var0,
      StructureVillagePieces.PieceWeight var1,
      List<StructureComponent> var2,
      Random var3,
      int var4,
      int var5,
      int var6,
      EnumFacing var7,
      int var8
   ) {
      Class<? extends StructureVillagePieces.Village> ☃ = ☃.villagePieceClass;
      StructureVillagePieces.Village ☃x = null;
      if (☃ == StructureVillagePieces.House4Garden.class) {
         ☃x = StructureVillagePieces.House4Garden.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.Church.class) {
         ☃x = StructureVillagePieces.Church.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.House1.class) {
         ☃x = StructureVillagePieces.House1.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.WoodHut.class) {
         ☃x = StructureVillagePieces.WoodHut.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.Hall.class) {
         ☃x = StructureVillagePieces.Hall.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.Field1.class) {
         ☃x = StructureVillagePieces.Field1.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.Field2.class) {
         ☃x = StructureVillagePieces.Field2.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.House2.class) {
         ☃x = StructureVillagePieces.House2.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureVillagePieces.House3.class) {
         ☃x = StructureVillagePieces.House3.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }

      return ☃x;
   }

   private static StructureVillagePieces.Village generateComponent(
      StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
   ) {
      int ☃ = updatePieceWeight(☃.structureVillageWeightedPieceList);
      if (☃ <= 0) {
         return null;
      } else {
         int ☃x = 0;

         while (☃x < 5) {
            ☃x++;
            int ☃xx = ☃.nextInt(☃);

            for (StructureVillagePieces.PieceWeight ☃xxx : ☃.structureVillageWeightedPieceList) {
               ☃xx -= ☃xxx.villagePieceWeight;
               if (☃xx < 0) {
                  if (!☃xxx.canSpawnMoreVillagePiecesOfType(☃) || ☃xxx == ☃.lastPlaced && ☃.structureVillageWeightedPieceList.size() > 1) {
                     break;
                  }

                  StructureVillagePieces.Village ☃xxxx = findAndCreateComponentFactory(☃, ☃xxx, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
                  if (☃xxxx != null) {
                     ☃xxx.villagePiecesSpawned++;
                     ☃.lastPlaced = ☃xxx;
                     if (!☃xxx.canSpawnMoreVillagePieces()) {
                        ☃.structureVillageWeightedPieceList.remove(☃xxx);
                     }

                     return ☃xxxx;
                  }
               }
            }
         }

         StructureBoundingBox ☃xx = StructureVillagePieces.Torch.findPieceBox(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         return ☃xx != null ? new StructureVillagePieces.Torch(☃, ☃, ☃, ☃xx, ☃) : null;
      }
   }

   private static StructureComponent generateAndAddComponent(
      StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
   ) {
      if (☃ > 50) {
         return null;
      } else if (Math.abs(☃ - ☃.getBoundingBox().minX) <= 112 && Math.abs(☃ - ☃.getBoundingBox().minZ) <= 112) {
         StructureComponent ☃ = generateComponent(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃ + 1);
         if (☃ != null) {
            ☃.add(☃);
            ☃.pendingHouses.add(☃);
            return ☃;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static StructureComponent generateAndAddRoadPiece(
      StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
   ) {
      if (☃ > 3 + ☃.terrainType) {
         return null;
      } else if (Math.abs(☃ - ☃.getBoundingBox().minX) <= 112 && Math.abs(☃ - ☃.getBoundingBox().minZ) <= 112) {
         StructureBoundingBox ☃ = StructureVillagePieces.Path.findPieceBox(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         if (☃ != null && ☃.minY > 10) {
            StructureComponent ☃x = new StructureVillagePieces.Path(☃, ☃, ☃, ☃, ☃);
            ☃.add(☃x);
            ☃.pendingRoads.add(☃x);
            return ☃x;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static class Church extends StructureVillagePieces.Village {
      public Church() {
      }

      public Church(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      public static StructureVillagePieces.Church createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 5, 12, 9, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.Church(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
         }

         IBlockState ☃ = Blocks.COBBLESTONE.getDefaultState();
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
         this.fillWithBlocks(☃, ☃, 1, 1, 1, 3, 3, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 5, 1, 3, 9, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 0, 0, 3, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 0, 3, 10, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 10, 3, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 4, 1, 1, 4, 10, 3, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 0, 4, 0, 4, 7, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 4, 0, 4, 4, 4, 7, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 8, 3, 4, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 5, 4, 3, 10, 4, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 5, 5, 3, 5, 7, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 9, 0, 4, 9, 4, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 4, 0, 4, 4, 4, ☃, ☃, false);
         this.setBlockState(☃, ☃, 0, 11, 2, ☃);
         this.setBlockState(☃, ☃, 4, 11, 2, ☃);
         this.setBlockState(☃, ☃, 2, 11, 0, ☃);
         this.setBlockState(☃, ☃, 2, 11, 4, ☃);
         this.setBlockState(☃, ☃, 1, 1, 6, ☃);
         this.setBlockState(☃, ☃, 1, 1, 7, ☃);
         this.setBlockState(☃, ☃, 2, 1, 7, ☃);
         this.setBlockState(☃, ☃, 3, 1, 6, ☃);
         this.setBlockState(☃, ☃, 3, 1, 7, ☃);
         this.setBlockState(☃, ☃x, 1, 1, 5, ☃);
         this.setBlockState(☃, ☃x, 2, 1, 6, ☃);
         this.setBlockState(☃, ☃x, 3, 1, 5, ☃);
         this.setBlockState(☃, ☃xx, 1, 2, 7, ☃);
         this.setBlockState(☃, ☃xxx, 3, 2, 7, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 6, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 7, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 6, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 7, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 4, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 4, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 6, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 6, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 8, ☃);
         this.placeTorch(☃, EnumFacing.SOUTH, 2, 4, 7, ☃);
         this.placeTorch(☃, EnumFacing.EAST, 1, 4, 6, ☃);
         this.placeTorch(☃, EnumFacing.WEST, 3, 4, 6, ☃);
         this.placeTorch(☃, EnumFacing.NORTH, 2, 4, 5, ☃);
         IBlockState ☃xxxx = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST);

         for (int ☃xxxxx = 1; ☃xxxxx <= 9; ☃xxxxx++) {
            this.setBlockState(☃, ☃xxxx, 3, ☃xxxxx, 3, ☃);
         }

         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 1, 0, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 2, 0, ☃);
         this.createVillageDoor(☃, ☃, ☃, 2, 1, 0, EnumFacing.NORTH);
         if (this.getBlockStateFromPos(☃, 2, 0, -1, ☃).getMaterial() == Material.AIR
            && this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getMaterial() != Material.AIR) {
            this.setBlockState(☃, ☃x, 2, 0, -1, ☃);
            if (this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getBlock() == Blocks.GRASS_PATH) {
               this.setBlockState(☃, Blocks.GRASS.getDefaultState(), 2, -1, -1, ☃);
            }
         }

         for (int ☃xxxxx = 0; ☃xxxxx < 9; ☃xxxxx++) {
            for (int ☃xxxxxx = 0; ☃xxxxxx < 5; ☃xxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxx, 12, ☃xxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxx, -1, ☃xxxxx, ☃);
            }
         }

         this.spawnVillagers(☃, ☃, 2, 1, 2, 1);
         return true;
      }

      @Override
      protected int chooseProfession(int var1, int var2) {
         return 2;
      }
   }

   public static class Field1 extends StructureVillagePieces.Village {
      private Block cropTypeA;
      private Block cropTypeB;
      private Block cropTypeC;
      private Block cropTypeD;

      public Field1() {
      }

      public Field1(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.cropTypeA = this.getRandomCropType(☃);
         this.cropTypeB = this.getRandomCropType(☃);
         this.cropTypeC = this.getRandomCropType(☃);
         this.cropTypeD = this.getRandomCropType(☃);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
         ☃.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
         ☃.setInteger("CC", Block.REGISTRY.getIDForObject(this.cropTypeC));
         ☃.setInteger("CD", Block.REGISTRY.getIDForObject(this.cropTypeD));
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.cropTypeA = Block.getBlockById(☃.getInteger("CA"));
         this.cropTypeB = Block.getBlockById(☃.getInteger("CB"));
         this.cropTypeC = Block.getBlockById(☃.getInteger("CC"));
         this.cropTypeD = Block.getBlockById(☃.getInteger("CD"));
         if (!(this.cropTypeA instanceof BlockCrops)) {
            this.cropTypeA = Blocks.WHEAT;
         }

         if (!(this.cropTypeB instanceof BlockCrops)) {
            this.cropTypeB = Blocks.CARROTS;
         }

         if (!(this.cropTypeC instanceof BlockCrops)) {
            this.cropTypeC = Blocks.POTATOES;
         }

         if (!(this.cropTypeD instanceof BlockCrops)) {
            this.cropTypeD = Blocks.BEETROOTS;
         }
      }

      private Block getRandomCropType(Random var1) {
         switch (☃.nextInt(10)) {
            case 0:
            case 1:
               return Blocks.CARROTS;
            case 2:
            case 3:
               return Blocks.POTATOES;
            case 4:
               return Blocks.BEETROOTS;
            default:
               return Blocks.WHEAT;
         }
      }

      public static StructureVillagePieces.Field1 createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 13, 4, 9, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.Field1(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 0, 1, 8, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 10, 0, 1, 11, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 0, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 6, 0, 0, 6, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 12, 0, 0, 12, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 0, 11, 0, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 8, 11, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 0, 1, 9, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

         for (int ☃x = 1; ☃x <= 7; ☃x++) {
            int ☃xx = ((BlockCrops)this.cropTypeA).getMaxAge();
            int ☃xxx = ☃xx / 3;
            this.setBlockState(☃, this.cropTypeA.getStateFromMeta(MathHelper.getInt(☃, ☃xxx, ☃xx)), 1, 1, ☃x, ☃);
            this.setBlockState(☃, this.cropTypeA.getStateFromMeta(MathHelper.getInt(☃, ☃xxx, ☃xx)), 2, 1, ☃x, ☃);
            int ☃xxxx = ((BlockCrops)this.cropTypeB).getMaxAge();
            int ☃xxxxx = ☃xxxx / 3;
            this.setBlockState(☃, this.cropTypeB.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxx, ☃xxxx)), 4, 1, ☃x, ☃);
            this.setBlockState(☃, this.cropTypeB.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxx, ☃xxxx)), 5, 1, ☃x, ☃);
            int ☃xxxxxx = ((BlockCrops)this.cropTypeC).getMaxAge();
            int ☃xxxxxxx = ☃xxxxxx / 3;
            this.setBlockState(☃, this.cropTypeC.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxxxx, ☃xxxxxx)), 7, 1, ☃x, ☃);
            this.setBlockState(☃, this.cropTypeC.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxxxx, ☃xxxxxx)), 8, 1, ☃x, ☃);
            int ☃xxxxxxxx = ((BlockCrops)this.cropTypeD).getMaxAge();
            int ☃xxxxxxxxx = ☃xxxxxxxx / 3;
            this.setBlockState(☃, this.cropTypeD.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxxxxxx, ☃xxxxxxxx)), 10, 1, ☃x, ☃);
            this.setBlockState(☃, this.cropTypeD.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxxxxxx, ☃xxxxxxxx)), 11, 1, ☃x, ☃);
         }

         for (int ☃x = 0; ☃x < 9; ☃x++) {
            for (int ☃xx = 0; ☃xx < 13; ☃xx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xx, 4, ☃x, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.DIRT.getDefaultState(), ☃xx, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Field2 extends StructureVillagePieces.Village {
      private Block cropTypeA;
      private Block cropTypeB;

      public Field2() {
      }

      public Field2(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.cropTypeA = this.getRandomCropType(☃);
         this.cropTypeB = this.getRandomCropType(☃);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
         ☃.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.cropTypeA = Block.getBlockById(☃.getInteger("CA"));
         this.cropTypeB = Block.getBlockById(☃.getInteger("CB"));
      }

      private Block getRandomCropType(Random var1) {
         switch (☃.nextInt(10)) {
            case 0:
            case 1:
               return Blocks.CARROTS;
            case 2:
            case 3:
               return Blocks.POTATOES;
            case 4:
               return Blocks.BEETROOTS;
            default:
               return Blocks.WHEAT;
         }
      }

      public static StructureVillagePieces.Field2 createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 7, 4, 9, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.Field2(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 6, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 0, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 6, 0, 0, 6, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 0, 5, 0, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 8, 5, 0, 8, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

         for (int ☃x = 1; ☃x <= 7; ☃x++) {
            int ☃xx = ((BlockCrops)this.cropTypeA).getMaxAge();
            int ☃xxx = ☃xx / 3;
            this.setBlockState(☃, this.cropTypeA.getStateFromMeta(MathHelper.getInt(☃, ☃xxx, ☃xx)), 1, 1, ☃x, ☃);
            this.setBlockState(☃, this.cropTypeA.getStateFromMeta(MathHelper.getInt(☃, ☃xxx, ☃xx)), 2, 1, ☃x, ☃);
            int ☃xxxx = ((BlockCrops)this.cropTypeB).getMaxAge();
            int ☃xxxxx = ☃xxxx / 3;
            this.setBlockState(☃, this.cropTypeB.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxx, ☃xxxx)), 4, 1, ☃x, ☃);
            this.setBlockState(☃, this.cropTypeB.getStateFromMeta(MathHelper.getInt(☃, ☃xxxxx, ☃xxxx)), 5, 1, ☃x, ☃);
         }

         for (int ☃x = 0; ☃x < 9; ☃x++) {
            for (int ☃xx = 0; ☃xx < 7; ☃xx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xx, 4, ☃x, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.DIRT.getDefaultState(), ☃xx, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Hall extends StructureVillagePieces.Village {
      public Hall() {
      }

      public Hall(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      public static StructureVillagePieces.Hall createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 9, 7, 11, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.Hall(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
         IBlockState ☃xxxx = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
         IBlockState ☃xxxxx = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
         IBlockState ☃xxxxxx = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
         this.fillWithBlocks(☃, ☃, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 0, 6, 8, 0, 10, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
         this.setBlockState(☃, ☃, 6, 0, 6, ☃);
         this.fillWithBlocks(☃, ☃, 2, 1, 6, 2, 1, 10, ☃xxxxxx, ☃xxxxxx, false);
         this.fillWithBlocks(☃, ☃, 8, 1, 6, 8, 1, 10, ☃xxxxxx, ☃xxxxxx, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 10, 7, 1, 10, ☃xxxxxx, ☃xxxxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 1, 7, 0, 4, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 0, 3, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 8, 0, 0, 8, 3, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 0, 7, 1, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 5, 7, 1, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 0, 7, 3, 0, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 5, 7, 3, 5, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 4, 1, 8, 4, 1, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 4, 4, 8, 4, 4, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 5, 2, 8, 5, 3, ☃xxxx, ☃xxxx, false);
         this.setBlockState(☃, ☃xxxx, 0, 4, 2, ☃);
         this.setBlockState(☃, ☃xxxx, 0, 4, 3, ☃);
         this.setBlockState(☃, ☃xxxx, 8, 4, 2, ☃);
         this.setBlockState(☃, ☃xxxx, 8, 4, 3, ☃);
         IBlockState ☃xxxxxxx = ☃x;
         IBlockState ☃xxxxxxxx = ☃xx;

         for (int ☃xxxxxxxxx = -1; ☃xxxxxxxxx <= 2; ☃xxxxxxxxx++) {
            for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx <= 8; ☃xxxxxxxxxx++) {
               this.setBlockState(☃, ☃xxxxxxx, ☃xxxxxxxxxx, 4 + ☃xxxxxxxxx, ☃xxxxxxxxx, ☃);
               this.setBlockState(☃, ☃xxxxxxxx, ☃xxxxxxxxxx, 4 + ☃xxxxxxxxx, 5 - ☃xxxxxxxxx, ☃);
            }
         }

         this.setBlockState(☃, ☃xxxxx, 0, 2, 1, ☃);
         this.setBlockState(☃, ☃xxxxx, 0, 2, 4, ☃);
         this.setBlockState(☃, ☃xxxxx, 8, 2, 1, ☃);
         this.setBlockState(☃, ☃xxxxx, 8, 2, 4, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, ☃);
         this.setBlockState(☃, ☃xxxxxx, 2, 1, 3, ☃);
         this.setBlockState(☃, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 2, 2, 3, ☃);
         this.setBlockState(☃, ☃xxxx, 1, 1, 4, ☃);
         this.setBlockState(☃, ☃xxxxxxx, 2, 1, 4, ☃);
         this.setBlockState(☃, ☃xxx, 1, 1, 3, ☃);
         this.fillWithBlocks(☃, ☃, 5, 0, 1, 7, 0, 3, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
         this.setBlockState(☃, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 6, 1, 1, ☃);
         this.setBlockState(☃, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 6, 1, 2, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 1, 0, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 2, 0, ☃);
         this.placeTorch(☃, EnumFacing.NORTH, 2, 3, 1, ☃);
         this.createVillageDoor(☃, ☃, ☃, 2, 1, 0, EnumFacing.NORTH);
         if (this.getBlockStateFromPos(☃, 2, 0, -1, ☃).getMaterial() == Material.AIR
            && this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getMaterial() != Material.AIR) {
            this.setBlockState(☃, ☃xxxxxxx, 2, 0, -1, ☃);
            if (this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getBlock() == Blocks.GRASS_PATH) {
               this.setBlockState(☃, Blocks.GRASS.getDefaultState(), 2, -1, -1, ☃);
            }
         }

         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 6, 1, 5, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 6, 2, 5, ☃);
         this.placeTorch(☃, EnumFacing.SOUTH, 6, 3, 4, ☃);
         this.createVillageDoor(☃, ☃, ☃, 6, 1, 5, EnumFacing.SOUTH);

         for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 5; ☃xxxxxxxxx++) {
            for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < 9; ☃xxxxxxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxxxxxx, 7, ☃xxxxxxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxxxxxx, -1, ☃xxxxxxxxx, ☃);
            }
         }

         this.spawnVillagers(☃, ☃, 4, 1, 2, 2);
         return true;
      }

      @Override
      protected int chooseProfession(int var1, int var2) {
         return ☃ == 0 ? 4 : super.chooseProfession(☃, ☃);
      }
   }

   public static class House1 extends StructureVillagePieces.Village {
      public House1() {
      }

      public House1(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      public static StructureVillagePieces.House1 createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 9, 9, 6, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.House1(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
         IBlockState ☃xxxx = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
         IBlockState ☃xxxxx = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xxxxxx = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
         this.fillWithBlocks(☃, ☃, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 8, 0, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 8, 5, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 6, 1, 8, 6, 4, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 7, 2, 8, 7, 3, ☃, ☃, false);

         for (int ☃xxxxxxx = -1; ☃xxxxxxx <= 2; ☃xxxxxxx++) {
            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx <= 8; ☃xxxxxxxx++) {
               this.setBlockState(☃, ☃x, ☃xxxxxxxx, 6 + ☃xxxxxxx, ☃xxxxxxx, ☃);
               this.setBlockState(☃, ☃xx, ☃xxxxxxxx, 6 + ☃xxxxxxx, 5 - ☃xxxxxxx, ☃);
            }
         }

         this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 1, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 5, 8, 1, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 8, 1, 0, 8, 1, 4, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 2, 1, 0, 7, 1, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 4, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 2, 5, 0, 4, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 8, 2, 5, 8, 4, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 8, 2, 0, 8, 4, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 2, 1, 0, 4, 4, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 5, 7, 4, 5, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 8, 2, 1, 8, 4, 4, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 0, 7, 4, 0, ☃xxxx, ☃xxxx, false);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 5, 3, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 6, 3, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 3, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 3, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 5, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, ☃);
         this.fillWithBlocks(☃, ☃, 1, 4, 1, 7, 4, 1, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 4, 4, 7, 4, 4, ☃xxxx, ☃xxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 4, 7, 3, 4, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
         this.setBlockState(☃, ☃xxxx, 7, 1, 4, ☃);
         this.setBlockState(☃, ☃xxx, 7, 1, 3, ☃);
         this.setBlockState(☃, ☃x, 6, 1, 4, ☃);
         this.setBlockState(☃, ☃x, 5, 1, 4, ☃);
         this.setBlockState(☃, ☃x, 4, 1, 4, ☃);
         this.setBlockState(☃, ☃x, 3, 1, 4, ☃);
         this.setBlockState(☃, ☃xxxxxx, 6, 1, 3, ☃);
         this.setBlockState(☃, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 6, 2, 3, ☃);
         this.setBlockState(☃, ☃xxxxxx, 4, 1, 3, ☃);
         this.setBlockState(☃, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 4, 2, 3, ☃);
         this.setBlockState(☃, Blocks.CRAFTING_TABLE.getDefaultState(), 7, 1, 1, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, 1, 0, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, 2, 0, ☃);
         this.createVillageDoor(☃, ☃, ☃, 1, 1, 0, EnumFacing.NORTH);
         if (this.getBlockStateFromPos(☃, 1, 0, -1, ☃).getMaterial() == Material.AIR
            && this.getBlockStateFromPos(☃, 1, -1, -1, ☃).getMaterial() != Material.AIR) {
            this.setBlockState(☃, ☃xxxxx, 1, 0, -1, ☃);
            if (this.getBlockStateFromPos(☃, 1, -1, -1, ☃).getBlock() == Blocks.GRASS_PATH) {
               this.setBlockState(☃, Blocks.GRASS.getDefaultState(), 1, -1, -1, ☃);
            }
         }

         for (int ☃xxxxxxx = 0; ☃xxxxxxx < 6; ☃xxxxxxx++) {
            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 9; ☃xxxxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxxxx, 9, ☃xxxxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxxxx, -1, ☃xxxxxxx, ☃);
            }
         }

         this.spawnVillagers(☃, ☃, 2, 1, 2, 1);
         return true;
      }

      @Override
      protected int chooseProfession(int var1, int var2) {
         return 1;
      }
   }

   public static class House2 extends StructureVillagePieces.Village {
      private boolean hasMadeChest;

      public House2() {
      }

      public House2(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      public static StructureVillagePieces.House2 createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 10, 6, 7, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.House2(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Chest", this.hasMadeChest);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.hasMadeChest = ☃.getBoolean("Chest");
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
         }

         IBlockState ☃ = Blocks.COBBLESTONE.getDefaultState();
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
         IBlockState ☃xxxx = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xxxxx = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
         IBlockState ☃xxxxxx = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 9, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 9, 0, 6, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 4, 0, 9, 4, 6, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 9, 5, 6, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 5, 1, 8, 5, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 1, 0, 2, 3, 0, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 4, 0, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 0, 3, 4, 0, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 6, 0, 4, 6, ☃xxxxx, ☃xxxxx, false);
         this.setBlockState(☃, ☃xxx, 3, 3, 1, ☃);
         this.fillWithBlocks(☃, ☃, 3, 1, 2, 3, 3, 2, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 4, 1, 3, 5, 3, 3, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 3, 5, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 6, 5, 3, 6, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 5, 1, 0, 5, 3, 0, ☃xxxxxx, ☃xxxxxx, false);
         this.fillWithBlocks(☃, ☃, 9, 1, 0, 9, 3, 0, ☃xxxxxx, ☃xxxxxx, false);
         this.fillWithBlocks(☃, ☃, 6, 1, 4, 9, 4, 6, ☃, ☃, false);
         this.setBlockState(☃, Blocks.FLOWING_LAVA.getDefaultState(), 7, 1, 5, ☃);
         this.setBlockState(☃, Blocks.FLOWING_LAVA.getDefaultState(), 8, 1, 5, ☃);
         this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), 9, 2, 5, ☃);
         this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), 9, 2, 4, ☃);
         this.fillWithBlocks(☃, ☃, 7, 2, 4, 8, 2, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.setBlockState(☃, ☃, 6, 1, 3, ☃);
         this.setBlockState(☃, Blocks.FURNACE.getDefaultState(), 6, 2, 3, ☃);
         this.setBlockState(☃, Blocks.FURNACE.getDefaultState(), 6, 3, 3, ☃);
         this.setBlockState(☃, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 8, 1, 1, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 4, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 6, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 6, ☃);
         this.setBlockState(☃, ☃xxxxxx, 2, 1, 4, ☃);
         this.setBlockState(☃, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 2, 2, 4, ☃);
         this.setBlockState(☃, ☃xxx, 1, 1, 5, ☃);
         this.setBlockState(☃, ☃x, 2, 1, 5, ☃);
         this.setBlockState(☃, ☃xx, 1, 1, 4, ☃);
         if (!this.hasMadeChest && ☃.isVecInside(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5)))) {
            this.hasMadeChest = true;
            this.generateChest(☃, ☃, ☃, 5, 1, 5, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
         }

         for (int ☃xxxxxxx = 6; ☃xxxxxxx <= 8; ☃xxxxxxx++) {
            if (this.getBlockStateFromPos(☃, ☃xxxxxxx, 0, -1, ☃).getMaterial() == Material.AIR
               && this.getBlockStateFromPos(☃, ☃xxxxxxx, -1, -1, ☃).getMaterial() != Material.AIR) {
               this.setBlockState(☃, ☃xxxx, ☃xxxxxxx, 0, -1, ☃);
               if (this.getBlockStateFromPos(☃, ☃xxxxxxx, -1, -1, ☃).getBlock() == Blocks.GRASS_PATH) {
                  this.setBlockState(☃, Blocks.GRASS.getDefaultState(), ☃xxxxxxx, -1, -1, ☃);
               }
            }
         }

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 7; ☃xxxxxxxx++) {
            for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 10; ☃xxxxxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxxxxx, 6, ☃xxxxxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxxxxx, -1, ☃xxxxxxxx, ☃);
            }
         }

         this.spawnVillagers(☃, ☃, 7, 1, 1, 1);
         return true;
      }

      @Override
      protected int chooseProfession(int var1, int var2) {
         return 3;
      }
   }

   public static class House3 extends StructureVillagePieces.Village {
      public House3() {
      }

      public House3(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      public static StructureVillagePieces.House3 createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 9, 7, 12, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.House3(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
         IBlockState ☃xxxx = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
         IBlockState ☃xxxxx = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
         IBlockState ☃xxxxxx = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
         this.fillWithBlocks(☃, ☃, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 0, 5, 8, 0, 10, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 1, 7, 0, 4, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 0, 3, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 8, 0, 0, 8, 3, 10, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 0, 7, 2, 0, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 5, 2, 1, 5, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 2, 0, 6, 2, 3, 10, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 3, 0, 10, 7, 3, 10, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 0, 7, 3, 0, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 5, 2, 3, 5, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 4, 1, 8, 4, 1, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 4, 4, 3, 4, 4, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 0, 5, 2, 8, 5, 3, ☃xxxxx, ☃xxxxx, false);
         this.setBlockState(☃, ☃xxxxx, 0, 4, 2, ☃);
         this.setBlockState(☃, ☃xxxxx, 0, 4, 3, ☃);
         this.setBlockState(☃, ☃xxxxx, 8, 4, 2, ☃);
         this.setBlockState(☃, ☃xxxxx, 8, 4, 3, ☃);
         this.setBlockState(☃, ☃xxxxx, 8, 4, 4, ☃);
         IBlockState ☃xxxxxxx = ☃x;
         IBlockState ☃xxxxxxxx = ☃xx;
         IBlockState ☃xxxxxxxxx = ☃xxxx;
         IBlockState ☃xxxxxxxxxx = ☃xxx;

         for (int ☃xxxxxxxxxxx = -1; ☃xxxxxxxxxxx <= 2; ☃xxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxx = 0; ☃xxxxxxxxxxxx <= 8; ☃xxxxxxxxxxxx++) {
               this.setBlockState(☃, ☃xxxxxxx, ☃xxxxxxxxxxxx, 4 + ☃xxxxxxxxxxx, ☃xxxxxxxxxxx, ☃);
               if ((☃xxxxxxxxxxx > -1 || ☃xxxxxxxxxxxx <= 1)
                  && (☃xxxxxxxxxxx > 0 || ☃xxxxxxxxxxxx <= 3)
                  && (☃xxxxxxxxxxx > 1 || ☃xxxxxxxxxxxx <= 4 || ☃xxxxxxxxxxxx >= 6)) {
                  this.setBlockState(☃, ☃xxxxxxxx, ☃xxxxxxxxxxxx, 4 + ☃xxxxxxxxxxx, 5 - ☃xxxxxxxxxxx, ☃);
               }
            }
         }

         this.fillWithBlocks(☃, ☃, 3, 4, 5, 3, 4, 10, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 7, 4, 2, 7, 4, 10, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 4, 5, 4, 4, 5, 10, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 6, 5, 4, 6, 5, 10, ☃xxxxx, ☃xxxxx, false);
         this.fillWithBlocks(☃, ☃, 5, 6, 3, 5, 6, 10, ☃xxxxx, ☃xxxxx, false);

         for (int ☃xxxxxxxxxxx = 4; ☃xxxxxxxxxxx >= 1; ☃xxxxxxxxxxx--) {
            this.setBlockState(☃, ☃xxxxx, ☃xxxxxxxxxxx, 2 + ☃xxxxxxxxxxx, 7 - ☃xxxxxxxxxxx, ☃);

            for (int ☃xxxxxxxxxxxxx = 8 - ☃xxxxxxxxxxx; ☃xxxxxxxxxxxxx <= 10; ☃xxxxxxxxxxxxx++) {
               this.setBlockState(☃, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 2 + ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃);
            }
         }

         this.setBlockState(☃, ☃xxxxx, 6, 6, 3, ☃);
         this.setBlockState(☃, ☃xxxxx, 7, 5, 4, ☃);
         this.setBlockState(☃, ☃xxxx, 6, 6, 4, ☃);

         for (int ☃xxxxxxxxxxx = 6; ☃xxxxxxxxxxx <= 8; ☃xxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxx = 5; ☃xxxxxxxxxxxxx <= 10; ☃xxxxxxxxxxxxx++) {
               this.setBlockState(☃, ☃xxxxxxxxx, ☃xxxxxxxxxxx, 12 - ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃);
            }
         }

         this.setBlockState(☃, ☃xxxxxx, 0, 2, 1, ☃);
         this.setBlockState(☃, ☃xxxxxx, 0, 2, 4, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, ☃);
         this.setBlockState(☃, ☃xxxxxx, 4, 2, 0, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, ☃);
         this.setBlockState(☃, ☃xxxxxx, 6, 2, 0, ☃);
         this.setBlockState(☃, ☃xxxxxx, 8, 2, 1, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, ☃);
         this.setBlockState(☃, ☃xxxxxx, 8, 2, 4, ☃);
         this.setBlockState(☃, ☃xxxxx, 8, 2, 5, ☃);
         this.setBlockState(☃, ☃xxxxxx, 8, 2, 6, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 7, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 8, ☃);
         this.setBlockState(☃, ☃xxxxxx, 8, 2, 9, ☃);
         this.setBlockState(☃, ☃xxxxxx, 2, 2, 6, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 7, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 8, ☃);
         this.setBlockState(☃, ☃xxxxxx, 2, 2, 9, ☃);
         this.setBlockState(☃, ☃xxxxxx, 4, 4, 10, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 5, 4, 10, ☃);
         this.setBlockState(☃, ☃xxxxxx, 6, 4, 10, ☃);
         this.setBlockState(☃, ☃xxxxx, 5, 5, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 1, 0, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 2, 0, ☃);
         this.placeTorch(☃, EnumFacing.NORTH, 2, 3, 1, ☃);
         this.createVillageDoor(☃, ☃, ☃, 2, 1, 0, EnumFacing.NORTH);
         this.fillWithBlocks(☃, ☃, 1, 0, -1, 3, 2, -1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         if (this.getBlockStateFromPos(☃, 2, 0, -1, ☃).getMaterial() == Material.AIR
            && this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getMaterial() != Material.AIR) {
            this.setBlockState(☃, ☃xxxxxxx, 2, 0, -1, ☃);
            if (this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getBlock() == Blocks.GRASS_PATH) {
               this.setBlockState(☃, Blocks.GRASS.getDefaultState(), 2, -1, -1, ☃);
            }
         }

         for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < 5; ☃xxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < 9; ☃xxxxxxxxxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxxxxxxxxx, 7, ☃xxxxxxxxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxxxxxxxxx, -1, ☃xxxxxxxxxxx, ☃);
            }
         }

         for (int ☃xxxxxxxxxxx = 5; ☃xxxxxxxxxxx < 11; ☃xxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxx = 2; ☃xxxxxxxxxxxxx < 9; ☃xxxxxxxxxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxxxxxxxxx, 7, ☃xxxxxxxxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxxxxxxxxx, -1, ☃xxxxxxxxxxx, ☃);
            }
         }

         this.spawnVillagers(☃, ☃, 4, 1, 2, 2);
         return true;
      }
   }

   public static class House4Garden extends StructureVillagePieces.Village {
      private boolean isRoofAccessible;

      public House4Garden() {
      }

      public House4Garden(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.isRoofAccessible = ☃.nextBoolean();
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Terrace", this.isRoofAccessible);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.isRoofAccessible = ☃.getBoolean("Terrace");
      }

      public static StructureVillagePieces.House4Garden createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 5, 6, 5, ☃);
         return StructureComponent.findIntersecting(☃, ☃) != null ? null : new StructureVillagePieces.House4Garden(☃, ☃, ☃, ☃, ☃);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
         IBlockState ☃xxxx = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 4, 0, 4, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 0, 4, 0, 4, 4, 4, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 1, 4, 1, 3, 4, 3, ☃x, ☃x, false);
         this.setBlockState(☃, ☃, 0, 1, 0, ☃);
         this.setBlockState(☃, ☃, 0, 2, 0, ☃);
         this.setBlockState(☃, ☃, 0, 3, 0, ☃);
         this.setBlockState(☃, ☃, 4, 1, 0, ☃);
         this.setBlockState(☃, ☃, 4, 2, 0, ☃);
         this.setBlockState(☃, ☃, 4, 3, 0, ☃);
         this.setBlockState(☃, ☃, 0, 1, 4, ☃);
         this.setBlockState(☃, ☃, 0, 2, 4, ☃);
         this.setBlockState(☃, ☃, 0, 3, 4, ☃);
         this.setBlockState(☃, ☃, 4, 1, 4, ☃);
         this.setBlockState(☃, ☃, 4, 2, 4, ☃);
         this.setBlockState(☃, ☃, 4, 3, 4, ☃);
         this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 3, 3, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 4, 1, 1, 4, 3, 3, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 4, 3, 3, 4, ☃x, ☃x, false);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 4, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, ☃);
         this.setBlockState(☃, ☃x, 1, 1, 0, ☃);
         this.setBlockState(☃, ☃x, 1, 2, 0, ☃);
         this.setBlockState(☃, ☃x, 1, 3, 0, ☃);
         this.setBlockState(☃, ☃x, 2, 3, 0, ☃);
         this.setBlockState(☃, ☃x, 3, 3, 0, ☃);
         this.setBlockState(☃, ☃x, 3, 2, 0, ☃);
         this.setBlockState(☃, ☃x, 3, 1, 0, ☃);
         if (this.getBlockStateFromPos(☃, 2, 0, -1, ☃).getMaterial() == Material.AIR
            && this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getMaterial() != Material.AIR) {
            this.setBlockState(☃, ☃xx, 2, 0, -1, ☃);
            if (this.getBlockStateFromPos(☃, 2, -1, -1, ☃).getBlock() == Blocks.GRASS_PATH) {
               this.setBlockState(☃, Blocks.GRASS.getDefaultState(), 2, -1, -1, ☃);
            }
         }

         this.fillWithBlocks(☃, ☃, 1, 1, 1, 3, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         if (this.isRoofAccessible) {
            this.setBlockState(☃, ☃xxxx, 0, 5, 0, ☃);
            this.setBlockState(☃, ☃xxxx, 1, 5, 0, ☃);
            this.setBlockState(☃, ☃xxxx, 2, 5, 0, ☃);
            this.setBlockState(☃, ☃xxxx, 3, 5, 0, ☃);
            this.setBlockState(☃, ☃xxxx, 4, 5, 0, ☃);
            this.setBlockState(☃, ☃xxxx, 0, 5, 4, ☃);
            this.setBlockState(☃, ☃xxxx, 1, 5, 4, ☃);
            this.setBlockState(☃, ☃xxxx, 2, 5, 4, ☃);
            this.setBlockState(☃, ☃xxxx, 3, 5, 4, ☃);
            this.setBlockState(☃, ☃xxxx, 4, 5, 4, ☃);
            this.setBlockState(☃, ☃xxxx, 4, 5, 1, ☃);
            this.setBlockState(☃, ☃xxxx, 4, 5, 2, ☃);
            this.setBlockState(☃, ☃xxxx, 4, 5, 3, ☃);
            this.setBlockState(☃, ☃xxxx, 0, 5, 1, ☃);
            this.setBlockState(☃, ☃xxxx, 0, 5, 2, ☃);
            this.setBlockState(☃, ☃xxxx, 0, 5, 3, ☃);
         }

         if (this.isRoofAccessible) {
            IBlockState ☃xxxxx = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.SOUTH);
            this.setBlockState(☃, ☃xxxxx, 3, 1, 3, ☃);
            this.setBlockState(☃, ☃xxxxx, 3, 2, 3, ☃);
            this.setBlockState(☃, ☃xxxxx, 3, 3, 3, ☃);
            this.setBlockState(☃, ☃xxxxx, 3, 4, 3, ☃);
         }

         this.placeTorch(☃, EnumFacing.NORTH, 2, 3, 1, ☃);

         for (int ☃xxxxx = 0; ☃xxxxx < 5; ☃xxxxx++) {
            for (int ☃xxxxxx = 0; ☃xxxxxx < 5; ☃xxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxx, 6, ☃xxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxx, -1, ☃xxxxx, ☃);
            }
         }

         this.spawnVillagers(☃, ☃, 1, 1, 2, 1);
         return true;
      }
   }

   public static class Path extends StructureVillagePieces.Road {
      private int length;

      public Path() {
      }

      public Path(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.length = Math.max(☃.getXSize(), ☃.getZSize());
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setInteger("Length", this.length);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.length = ☃.getInteger("Length");
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         boolean ☃ = false;

         for (int ☃x = ☃.nextInt(5); ☃x < this.length - 8; ☃x += 2 + ☃.nextInt(5)) {
            StructureComponent ☃xx = this.getNextComponentNN((StructureVillagePieces.Start)☃, ☃, ☃, 0, ☃x);
            if (☃xx != null) {
               ☃x += Math.max(☃xx.boundingBox.getXSize(), ☃xx.boundingBox.getZSize());
               ☃ = true;
            }
         }

         for (int var7 = ☃.nextInt(5); var7 < this.length - 8; var7 += 2 + ☃.nextInt(5)) {
            StructureComponent ☃xx = this.getNextComponentPP((StructureVillagePieces.Start)☃, ☃, ☃, 0, var7);
            if (☃xx != null) {
               var7 += Math.max(☃xx.boundingBox.getXSize(), ☃xx.boundingBox.getZSize());
               ☃ = true;
            }
         }

         EnumFacing ☃xx = this.getCoordBaseMode();
         if (☃ && ☃.nextInt(3) > 0 && ☃xx != null) {
            switch (☃xx) {
               case NORTH:
               default:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.minX - 1,
                     this.boundingBox.minY,
                     this.boundingBox.minZ,
                     EnumFacing.WEST,
                     this.getComponentType()
                  );
                  break;
               case SOUTH:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.minX - 1,
                     this.boundingBox.minY,
                     this.boundingBox.maxZ - 2,
                     EnumFacing.WEST,
                     this.getComponentType()
                  );
                  break;
               case WEST:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.minX,
                     this.boundingBox.minY,
                     this.boundingBox.minZ - 1,
                     EnumFacing.NORTH,
                     this.getComponentType()
                  );
                  break;
               case EAST:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.maxX - 2,
                     this.boundingBox.minY,
                     this.boundingBox.minZ - 1,
                     EnumFacing.NORTH,
                     this.getComponentType()
                  );
            }
         }

         if (☃ && ☃.nextInt(3) > 0 && ☃xx != null) {
            switch (☃xx) {
               case NORTH:
               default:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.maxX + 1,
                     this.boundingBox.minY,
                     this.boundingBox.minZ,
                     EnumFacing.EAST,
                     this.getComponentType()
                  );
                  break;
               case SOUTH:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.maxX + 1,
                     this.boundingBox.minY,
                     this.boundingBox.maxZ - 2,
                     EnumFacing.EAST,
                     this.getComponentType()
                  );
                  break;
               case WEST:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.minX,
                     this.boundingBox.minY,
                     this.boundingBox.maxZ + 1,
                     EnumFacing.SOUTH,
                     this.getComponentType()
                  );
                  break;
               case EAST:
                  StructureVillagePieces.generateAndAddRoadPiece(
                     (StructureVillagePieces.Start)☃,
                     ☃,
                     ☃,
                     this.boundingBox.maxX - 2,
                     this.boundingBox.minY,
                     this.boundingBox.maxZ + 1,
                     EnumFacing.SOUTH,
                     this.getComponentType()
                  );
            }
         }
      }

      public static StructureBoundingBox findPieceBox(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6
      ) {
         for (int ☃ = 7 * MathHelper.getInt(☃, 3, 5); ☃ >= 7; ☃ -= 7) {
            StructureBoundingBox ☃x = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 3, 3, ☃, ☃);
            if (StructureComponent.findIntersecting(☃, ☃x) == null) {
               return ☃x;
            }
         }

         return null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.GRASS_PATH.getDefaultState());
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());

         for (int ☃xxxx = this.boundingBox.minX; ☃xxxx <= this.boundingBox.maxX; ☃xxxx++) {
            for (int ☃xxxxx = this.boundingBox.minZ; ☃xxxxx <= this.boundingBox.maxZ; ☃xxxxx++) {
               BlockPos ☃xxxxxx = new BlockPos(☃xxxx, 64, ☃xxxxx);
               if (☃.isVecInside(☃xxxxxx)) {
                  ☃xxxxxx = ☃.getTopSolidOrLiquidBlock(☃xxxxxx).down();
                  if (☃xxxxxx.getY() < ☃.getSeaLevel()) {
                     ☃xxxxxx = new BlockPos(☃xxxxxx.getX(), ☃.getSeaLevel() - 1, ☃xxxxxx.getZ());
                  }

                  while (☃xxxxxx.getY() >= ☃.getSeaLevel() - 1) {
                     IBlockState ☃xxxxxxx = ☃.getBlockState(☃xxxxxx);
                     if (☃xxxxxxx.getBlock() == Blocks.GRASS && ☃.isAirBlock(☃xxxxxx.up())) {
                        ☃.setBlockState(☃xxxxxx, ☃, 2);
                        break;
                     }

                     if (☃xxxxxxx.getMaterial().isLiquid()) {
                        ☃.setBlockState(☃xxxxxx, ☃x, 2);
                        break;
                     }

                     if (☃xxxxxxx.getBlock() == Blocks.SAND || ☃xxxxxxx.getBlock() == Blocks.SANDSTONE || ☃xxxxxxx.getBlock() == Blocks.RED_SANDSTONE) {
                        ☃.setBlockState(☃xxxxxx, ☃xx, 2);
                        ☃.setBlockState(☃xxxxxx.down(), ☃xxx, 2);
                        break;
                     }

                     ☃xxxxxx = ☃xxxxxx.down();
                  }
               }
            }
         }

         return true;
      }
   }

   public static class PieceWeight {
      public Class<? extends StructureVillagePieces.Village> villagePieceClass;
      public final int villagePieceWeight;
      public int villagePiecesSpawned;
      public int villagePiecesLimit;

      public PieceWeight(Class<? extends StructureVillagePieces.Village> var1, int var2, int var3) {
         this.villagePieceClass = ☃;
         this.villagePieceWeight = ☃;
         this.villagePiecesLimit = ☃;
      }

      public boolean canSpawnMoreVillagePiecesOfType(int var1) {
         return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
      }

      public boolean canSpawnMoreVillagePieces() {
         return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
      }
   }

   public abstract static class Road extends StructureVillagePieces.Village {
      public Road() {
      }

      protected Road(StructureVillagePieces.Start var1, int var2) {
         super(☃, ☃);
      }
   }

   public static class Start extends StructureVillagePieces.Well {
      public BiomeProvider biomeProvider;
      public int terrainType;
      public StructureVillagePieces.PieceWeight lastPlaced;
      public List<StructureVillagePieces.PieceWeight> structureVillageWeightedPieceList;
      public List<StructureComponent> pendingHouses = Lists.newArrayList();
      public List<StructureComponent> pendingRoads = Lists.newArrayList();

      public Start() {
      }

      public Start(BiomeProvider var1, int var2, Random var3, int var4, int var5, List<StructureVillagePieces.PieceWeight> var6, int var7) {
         super(null, 0, ☃, ☃, ☃);
         this.biomeProvider = ☃;
         this.structureVillageWeightedPieceList = ☃;
         this.terrainType = ☃;
         Biome ☃ = ☃.getBiome(new BlockPos(☃, 0, ☃), Biomes.DEFAULT);
         if (☃ instanceof BiomeDesert) {
            this.structureType = 1;
         } else if (☃ instanceof BiomeSavanna) {
            this.structureType = 2;
         } else if (☃ instanceof BiomeTaiga) {
            this.structureType = 3;
         }

         this.setStructureType(this.structureType);
         this.isZombieInfested = ☃.nextInt(50) == 0;
      }
   }

   public static class Torch extends StructureVillagePieces.Village {
      public Torch() {
      }

      public Torch(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      public static StructureBoundingBox findPieceBox(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 3, 4, 2, ☃);
         return StructureComponent.findIntersecting(☃, ☃) != null ? null : ☃;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.setBlockState(☃, ☃, 1, 0, 0, ☃);
         this.setBlockState(☃, ☃, 1, 1, 0, ☃);
         this.setBlockState(☃, ☃, 1, 2, 0, ☃);
         this.setBlockState(☃, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, ☃);
         this.placeTorch(☃, EnumFacing.EAST, 2, 3, 0, ☃);
         this.placeTorch(☃, EnumFacing.NORTH, 1, 3, 1, ☃);
         this.placeTorch(☃, EnumFacing.WEST, 0, 3, 0, ☃);
         this.placeTorch(☃, EnumFacing.SOUTH, 1, 3, -1, ☃);
         return true;
      }
   }

   abstract static class Village extends StructureComponent {
      protected int averageGroundLvl = -1;
      private int villagersSpawned;
      protected int structureType;
      protected boolean isZombieInfested;

      public Village() {
      }

      protected Village(StructureVillagePieces.Start var1, int var2) {
         super(☃);
         if (☃ != null) {
            this.structureType = ☃.structureType;
            this.isZombieInfested = ☃.isZombieInfested;
         }
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         ☃.setInteger("HPos", this.averageGroundLvl);
         ☃.setInteger("VCount", this.villagersSpawned);
         ☃.setByte("Type", (byte)this.structureType);
         ☃.setBoolean("Zombie", this.isZombieInfested);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         this.averageGroundLvl = ☃.getInteger("HPos");
         this.villagersSpawned = ☃.getInteger("VCount");
         this.structureType = ☃.getByte("Type");
         if (☃.getBoolean("Desert")) {
            this.structureType = 1;
         }

         this.isZombieInfested = ☃.getBoolean("Zombie");
      }

      @Nullable
      protected StructureComponent getNextComponentNN(StructureVillagePieces.Start var1, List<StructureComponent> var2, Random var3, int var4, int var5) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
               default:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.WEST, this.getComponentType()
                  );
               case SOUTH:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.WEST, this.getComponentType()
                  );
               case WEST:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType()
                  );
               case EAST:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType()
                  );
            }
         } else {
            return null;
         }
      }

      @Nullable
      protected StructureComponent getNextComponentPP(StructureVillagePieces.Start var1, List<StructureComponent> var2, Random var3, int var4, int var5) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
               default:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.EAST, this.getComponentType()
                  );
               case SOUTH:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.EAST, this.getComponentType()
                  );
               case WEST:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType()
                  );
               case EAST:
                  return StructureVillagePieces.generateAndAddComponent(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType()
                  );
            }
         } else {
            return null;
         }
      }

      protected int getAverageGroundLevel(World var1, StructureBoundingBox var2) {
         int ☃ = 0;
         int ☃x = 0;
         BlockPos.MutableBlockPos ☃xx = new BlockPos.MutableBlockPos();

         for (int ☃xxx = this.boundingBox.minZ; ☃xxx <= this.boundingBox.maxZ; ☃xxx++) {
            for (int ☃xxxx = this.boundingBox.minX; ☃xxxx <= this.boundingBox.maxX; ☃xxxx++) {
               ☃xx.setPos(☃xxxx, 64, ☃xxx);
               if (☃.isVecInside(☃xx)) {
                  ☃ += Math.max(☃.getTopSolidOrLiquidBlock(☃xx).getY(), ☃.provider.getAverageGroundLevel() - 1);
                  ☃x++;
               }
            }
         }

         return ☃x == 0 ? -1 : ☃ / ☃x;
      }

      protected static boolean canVillageGoDeeper(StructureBoundingBox var0) {
         return ☃ != null && ☃.minY > 10;
      }

      protected void spawnVillagers(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6) {
         if (this.villagersSpawned < ☃) {
            for (int ☃ = this.villagersSpawned; ☃ < ☃; ☃++) {
               int ☃x = this.getXWithOffset(☃ + ☃, ☃);
               int ☃xx = this.getYWithOffset(☃);
               int ☃xxx = this.getZWithOffset(☃ + ☃, ☃);
               if (!☃.isVecInside(new BlockPos(☃x, ☃xx, ☃xxx))) {
                  break;
               }

               this.villagersSpawned++;
               if (this.isZombieInfested) {
                  EntityZombieVillager ☃xxxx = new EntityZombieVillager(☃);
                  ☃xxxx.setLocationAndAngles(☃x + 0.5, ☃xx, ☃xxx + 0.5, 0.0F, 0.0F);
                  ☃xxxx.onInitialSpawn(☃.getDifficultyForLocation(new BlockPos(☃xxxx)), null);
                  ☃xxxx.setProfession(this.chooseProfession(☃, 0));
                  ☃xxxx.enablePersistence();
                  ☃.spawnEntity(☃xxxx);
               } else {
                  EntityVillager ☃xxxx = new EntityVillager(☃);
                  ☃xxxx.setLocationAndAngles(☃x + 0.5, ☃xx, ☃xxx + 0.5, 0.0F, 0.0F);
                  ☃xxxx.setProfession(this.chooseProfession(☃, ☃.rand.nextInt(6)));
                  ☃xxxx.finalizeMobSpawn(☃.getDifficultyForLocation(new BlockPos(☃xxxx)), null, false);
                  ☃.spawnEntity(☃xxxx);
               }
            }
         }
      }

      protected int chooseProfession(int var1, int var2) {
         return ☃;
      }

      protected IBlockState getBiomeSpecificBlockState(IBlockState var1) {
         if (this.structureType == 1) {
            if (☃.getBlock() == Blocks.LOG || ☃.getBlock() == Blocks.LOG2) {
               return Blocks.SANDSTONE.getDefaultState();
            }

            if (☃.getBlock() == Blocks.COBBLESTONE) {
               return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
            }

            if (☃.getBlock() == Blocks.PLANKS) {
               return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
            }

            if (☃.getBlock() == Blocks.OAK_STAIRS) {
               return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, ☃.getValue(BlockStairs.FACING));
            }

            if (☃.getBlock() == Blocks.STONE_STAIRS) {
               return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, ☃.getValue(BlockStairs.FACING));
            }

            if (☃.getBlock() == Blocks.GRAVEL) {
               return Blocks.SANDSTONE.getDefaultState();
            }
         } else if (this.structureType == 3) {
            if (☃.getBlock() == Blocks.LOG || ☃.getBlock() == Blocks.LOG2) {
               return Blocks.LOG
                  .getDefaultState()
                  .withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE)
                  .withProperty(BlockLog.LOG_AXIS, ☃.getValue(BlockLog.LOG_AXIS));
            }

            if (☃.getBlock() == Blocks.PLANKS) {
               return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
            }

            if (☃.getBlock() == Blocks.OAK_STAIRS) {
               return Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, ☃.getValue(BlockStairs.FACING));
            }

            if (☃.getBlock() == Blocks.OAK_FENCE) {
               return Blocks.SPRUCE_FENCE.getDefaultState();
            }
         } else if (this.structureType == 2) {
            if (☃.getBlock() == Blocks.LOG || ☃.getBlock() == Blocks.LOG2) {
               return Blocks.LOG2
                  .getDefaultState()
                  .withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA)
                  .withProperty(BlockLog.LOG_AXIS, ☃.getValue(BlockLog.LOG_AXIS));
            }

            if (☃.getBlock() == Blocks.PLANKS) {
               return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
            }

            if (☃.getBlock() == Blocks.OAK_STAIRS) {
               return Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, ☃.getValue(BlockStairs.FACING));
            }

            if (☃.getBlock() == Blocks.COBBLESTONE) {
               return Blocks.LOG2
                  .getDefaultState()
                  .withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA)
                  .withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
            }

            if (☃.getBlock() == Blocks.OAK_FENCE) {
               return Blocks.ACACIA_FENCE.getDefaultState();
            }
         }

         return ☃;
      }

      protected BlockDoor biomeDoor() {
         switch (this.structureType) {
            case 2:
               return Blocks.ACACIA_DOOR;
            case 3:
               return Blocks.SPRUCE_DOOR;
            default:
               return Blocks.OAK_DOOR;
         }
      }

      protected void createVillageDoor(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, EnumFacing var7) {
         if (!this.isZombieInfested) {
            this.generateDoor(☃, ☃, ☃, ☃, ☃, ☃, EnumFacing.NORTH, this.biomeDoor());
         }
      }

      protected void placeTorch(World var1, EnumFacing var2, int var3, int var4, int var5, StructureBoundingBox var6) {
         if (!this.isZombieInfested) {
            this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, ☃), ☃, ☃, ☃, ☃);
         }
      }

      @Override
      protected void replaceAirAndLiquidDownwards(World var1, IBlockState var2, int var3, int var4, int var5, StructureBoundingBox var6) {
         IBlockState ☃ = this.getBiomeSpecificBlockState(☃);
         super.replaceAirAndLiquidDownwards(☃, ☃, ☃, ☃, ☃, ☃);
      }

      protected void setStructureType(int var1) {
         this.structureType = ☃;
      }
   }

   public static class Well extends StructureVillagePieces.Village {
      public Well() {
      }

      public Well(StructureVillagePieces.Start var1, int var2, Random var3, int var4, int var5) {
         super(☃, ☃);
         this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(☃));
         if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
            this.boundingBox = new StructureBoundingBox(☃, 64, ☃, ☃ + 6 - 1, 78, ☃ + 6 - 1);
         } else {
            this.boundingBox = new StructureBoundingBox(☃, 64, ☃, ☃ + 6 - 1, 78, ☃ + 6 - 1);
         }
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         StructureVillagePieces.generateAndAddRoadPiece(
            (StructureVillagePieces.Start)☃,
            ☃,
            ☃,
            this.boundingBox.minX - 1,
            this.boundingBox.maxY - 4,
            this.boundingBox.minZ + 1,
            EnumFacing.WEST,
            this.getComponentType()
         );
         StructureVillagePieces.generateAndAddRoadPiece(
            (StructureVillagePieces.Start)☃,
            ☃,
            ☃,
            this.boundingBox.maxX + 1,
            this.boundingBox.maxY - 4,
            this.boundingBox.minZ + 1,
            EnumFacing.EAST,
            this.getComponentType()
         );
         StructureVillagePieces.generateAndAddRoadPiece(
            (StructureVillagePieces.Start)☃,
            ☃,
            ☃,
            this.boundingBox.minX + 1,
            this.boundingBox.maxY - 4,
            this.boundingBox.minZ - 1,
            EnumFacing.NORTH,
            this.getComponentType()
         );
         StructureVillagePieces.generateAndAddRoadPiece(
            (StructureVillagePieces.Start)☃,
            ☃,
            ☃,
            this.boundingBox.minX + 1,
            this.boundingBox.maxY - 4,
            this.boundingBox.maxZ + 1,
            EnumFacing.SOUTH,
            this.getComponentType()
         );
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
         this.fillWithBlocks(☃, ☃, 1, 0, 1, 4, 12, 4, ☃, Blocks.FLOWING_WATER.getDefaultState(), false);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 12, 2, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 3, 12, 2, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, 12, 3, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 3, 12, 3, ☃);
         this.setBlockState(☃, ☃x, 1, 13, 1, ☃);
         this.setBlockState(☃, ☃x, 1, 14, 1, ☃);
         this.setBlockState(☃, ☃x, 4, 13, 1, ☃);
         this.setBlockState(☃, ☃x, 4, 14, 1, ☃);
         this.setBlockState(☃, ☃x, 1, 13, 4, ☃);
         this.setBlockState(☃, ☃x, 1, 14, 4, ☃);
         this.setBlockState(☃, ☃x, 4, 13, 4, ☃);
         this.setBlockState(☃, ☃x, 4, 14, 4, ☃);
         this.fillWithBlocks(☃, ☃, 1, 15, 1, 4, 15, 4, ☃, ☃, false);

         for (int ☃xx = 0; ☃xx <= 5; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx <= 5; ☃xxx++) {
               if (☃xxx == 0 || ☃xxx == 5 || ☃xx == 0 || ☃xx == 5) {
                  this.setBlockState(☃, ☃, ☃xxx, 11, ☃xx, ☃);
                  this.clearCurrentPositionBlocksUpwards(☃, ☃xxx, 12, ☃xx, ☃);
               }
            }
         }

         return true;
      }
   }

   public static class WoodHut extends StructureVillagePieces.Village {
      private boolean isTallHouse;
      private int tablePosition;

      public WoodHut() {
      }

      public WoodHut(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(☃, ☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.isTallHouse = ☃.nextBoolean();
         this.tablePosition = ☃.nextInt(3);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setInteger("T", this.tablePosition);
         ☃.setBoolean("C", this.isTallHouse);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.tablePosition = ☃.getInteger("T");
         this.isTallHouse = ☃.getBoolean("C");
      }

      public static StructureVillagePieces.WoodHut createPiece(
         StructureVillagePieces.Start var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, 0, 0, 0, 4, 6, 5, ☃);
         return canVillageGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureVillagePieces.WoodHut(☃, ☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(☃, ☃);
            if (this.averageGroundLvl < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
         }

         IBlockState ☃ = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
         IBlockState ☃x = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
         IBlockState ☃xx = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
         IBlockState ☃xxx = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
         IBlockState ☃xxxx = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
         this.fillWithBlocks(☃, ☃, 1, 1, 1, 3, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 3, 0, 4, ☃, ☃, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 1, 2, 0, 3, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
         if (this.isTallHouse) {
            this.fillWithBlocks(☃, ☃, 1, 4, 1, 2, 4, 3, ☃xxx, ☃xxx, false);
         } else {
            this.fillWithBlocks(☃, ☃, 1, 5, 1, 2, 5, 3, ☃xxx, ☃xxx, false);
         }

         this.setBlockState(☃, ☃xxx, 1, 4, 0, ☃);
         this.setBlockState(☃, ☃xxx, 2, 4, 0, ☃);
         this.setBlockState(☃, ☃xxx, 1, 4, 4, ☃);
         this.setBlockState(☃, ☃xxx, 2, 4, 4, ☃);
         this.setBlockState(☃, ☃xxx, 0, 4, 1, ☃);
         this.setBlockState(☃, ☃xxx, 0, 4, 2, ☃);
         this.setBlockState(☃, ☃xxx, 0, 4, 3, ☃);
         this.setBlockState(☃, ☃xxx, 3, 4, 1, ☃);
         this.setBlockState(☃, ☃xxx, 3, 4, 2, ☃);
         this.setBlockState(☃, ☃xxx, 3, 4, 3, ☃);
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 3, 0, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 0, 3, 3, 0, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 4, 0, 3, 4, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 4, 3, 3, 4, ☃xxx, ☃xxx, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 3, 3, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 1, 3, 3, 3, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 0, 2, 3, 0, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 4, 2, 3, 4, ☃x, ☃x, false);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, ☃);
         this.setBlockState(☃, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 2, ☃);
         if (this.tablePosition > 0) {
            this.setBlockState(☃, ☃xxxx, this.tablePosition, 1, 3, ☃);
            this.setBlockState(☃, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.tablePosition, 2, 3, ☃);
         }

         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, 1, 0, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, 2, 0, ☃);
         this.createVillageDoor(☃, ☃, ☃, 1, 1, 0, EnumFacing.NORTH);
         if (this.getBlockStateFromPos(☃, 1, 0, -1, ☃).getMaterial() == Material.AIR
            && this.getBlockStateFromPos(☃, 1, -1, -1, ☃).getMaterial() != Material.AIR) {
            this.setBlockState(☃, ☃xx, 1, 0, -1, ☃);
            if (this.getBlockStateFromPos(☃, 1, -1, -1, ☃).getBlock() == Blocks.GRASS_PATH) {
               this.setBlockState(☃, Blocks.GRASS.getDefaultState(), 1, -1, -1, ☃);
            }
         }

         for (int ☃xxxxx = 0; ☃xxxxx < 5; ☃xxxxx++) {
            for (int ☃xxxxxx = 0; ☃xxxxxx < 4; ☃xxxxxx++) {
               this.clearCurrentPositionBlocksUpwards(☃, ☃xxxxxx, 6, ☃xxxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, ☃, ☃xxxxxx, -1, ☃xxxxx, ☃);
            }
         }

         this.spawnVillagers(☃, ☃, 1, 1, 2, 1);
         return true;
      }
   }
}
