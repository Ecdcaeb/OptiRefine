package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureStrongholdPieces {
   private static final StructureStrongholdPieces.PieceWeight[] PIECE_WEIGHTS = new StructureStrongholdPieces.PieceWeight[]{
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Straight.class, 40, 0),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Prison.class, 5, 5),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.LeftTurn.class, 20, 0),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RightTurn.class, 20, 0),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RoomCrossing.class, 10, 6),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.StairsStraight.class, 5, 5),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Stairs.class, 5, 5),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Crossing.class, 5, 4),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.ChestCorridor.class, 5, 4),
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Library.class, 10, 2) {
         @Override
         public boolean canSpawnMoreStructuresOfType(int var1) {
            return super.canSpawnMoreStructuresOfType(☃) && ☃ > 4;
         }
      },
      new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.PortalRoom.class, 20, 1) {
         @Override
         public boolean canSpawnMoreStructuresOfType(int var1) {
            return super.canSpawnMoreStructuresOfType(☃) && ☃ > 5;
         }
      }
   };
   private static List<StructureStrongholdPieces.PieceWeight> structurePieceList;
   private static Class<? extends StructureStrongholdPieces.Stronghold> strongComponentType;
   static int totalWeight;
   private static final StructureStrongholdPieces.Stones STRONGHOLD_STONES = new StructureStrongholdPieces.Stones();

   public static void registerStrongholdPieces() {
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.ChestCorridor.class, "SHCC");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Corridor.class, "SHFC");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Crossing.class, "SH5C");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.LeftTurn.class, "SHLT");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Library.class, "SHLi");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.PortalRoom.class, "SHPR");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Prison.class, "SHPH");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RightTurn.class, "SHRT");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RoomCrossing.class, "SHRC");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs.class, "SHSD");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs2.class, "SHStart");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Straight.class, "SHS");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.StairsStraight.class, "SHSSD");
   }

   public static void prepareStructurePieces() {
      structurePieceList = Lists.newArrayList();

      for (StructureStrongholdPieces.PieceWeight ☃ : PIECE_WEIGHTS) {
         ☃.instancesSpawned = 0;
         structurePieceList.add(☃);
      }

      strongComponentType = null;
   }

   private static boolean canAddStructurePieces() {
      boolean ☃ = false;
      totalWeight = 0;

      for (StructureStrongholdPieces.PieceWeight ☃x : structurePieceList) {
         if (☃x.instancesLimit > 0 && ☃x.instancesSpawned < ☃x.instancesLimit) {
            ☃ = true;
         }

         totalWeight = totalWeight + ☃x.pieceWeight;
      }

      return ☃;
   }

   private static StructureStrongholdPieces.Stronghold findAndCreatePieceFactory(
      Class<? extends StructureStrongholdPieces.Stronghold> var0,
      List<StructureComponent> var1,
      Random var2,
      int var3,
      int var4,
      int var5,
      @Nullable EnumFacing var6,
      int var7
   ) {
      StructureStrongholdPieces.Stronghold ☃ = null;
      if (☃ == StructureStrongholdPieces.Straight.class) {
         ☃ = StructureStrongholdPieces.Straight.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.Prison.class) {
         ☃ = StructureStrongholdPieces.Prison.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.LeftTurn.class) {
         ☃ = StructureStrongholdPieces.LeftTurn.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.RightTurn.class) {
         ☃ = StructureStrongholdPieces.RightTurn.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.RoomCrossing.class) {
         ☃ = StructureStrongholdPieces.RoomCrossing.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.StairsStraight.class) {
         ☃ = StructureStrongholdPieces.StairsStraight.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.Stairs.class) {
         ☃ = StructureStrongholdPieces.Stairs.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.Crossing.class) {
         ☃ = StructureStrongholdPieces.Crossing.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.ChestCorridor.class) {
         ☃ = StructureStrongholdPieces.ChestCorridor.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.Library.class) {
         ☃ = StructureStrongholdPieces.Library.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureStrongholdPieces.PortalRoom.class) {
         ☃ = StructureStrongholdPieces.PortalRoom.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }

      return ☃;
   }

   private static StructureStrongholdPieces.Stronghold generatePieceFromSmallDoor(
      StructureStrongholdPieces.Stairs2 var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
   ) {
      if (!canAddStructurePieces()) {
         return null;
      } else {
         if (strongComponentType != null) {
            StructureStrongholdPieces.Stronghold ☃ = findAndCreatePieceFactory(strongComponentType, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
            strongComponentType = null;
            if (☃ != null) {
               return ☃;
            }
         }

         int ☃ = 0;

         while (☃ < 5) {
            ☃++;
            int ☃x = ☃.nextInt(totalWeight);

            for (StructureStrongholdPieces.PieceWeight ☃xx : structurePieceList) {
               ☃x -= ☃xx.pieceWeight;
               if (☃x < 0) {
                  if (!☃xx.canSpawnMoreStructuresOfType(☃) || ☃xx == ☃.lastPlaced) {
                     break;
                  }

                  StructureStrongholdPieces.Stronghold ☃xxx = findAndCreatePieceFactory(☃xx.pieceClass, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
                  if (☃xxx != null) {
                     ☃xx.instancesSpawned++;
                     ☃.lastPlaced = ☃xx;
                     if (!☃xx.canSpawnMoreStructures()) {
                        structurePieceList.remove(☃xx);
                     }

                     return ☃xxx;
                  }
               }
            }
         }

         StructureBoundingBox ☃x = StructureStrongholdPieces.Corridor.findPieceBox(☃, ☃, ☃, ☃, ☃, ☃);
         return ☃x != null && ☃x.minY > 1 ? new StructureStrongholdPieces.Corridor(☃, ☃, ☃x, ☃) : null;
      }
   }

   private static StructureComponent generateAndAddPiece(
      StructureStrongholdPieces.Stairs2 var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, @Nullable EnumFacing var6, int var7
   ) {
      if (☃ > 50) {
         return null;
      } else if (Math.abs(☃ - ☃.getBoundingBox().minX) <= 112 && Math.abs(☃ - ☃.getBoundingBox().minZ) <= 112) {
         StructureComponent ☃ = generatePieceFromSmallDoor(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃ + 1);
         if (☃ != null) {
            ☃.add(☃);
            ☃.pendingChildren.add(☃);
         }

         return ☃;
      } else {
         return null;
      }
   }

   public static class ChestCorridor extends StructureStrongholdPieces.Stronghold {
      private boolean hasMadeChest;

      public ChestCorridor() {
      }

      public ChestCorridor(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
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
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
      }

      public static StructureStrongholdPieces.ChestCorridor createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -1, 0, 5, 5, 7, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.ChestCorridor(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 4, 4, 6, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 1, 1, 0);
            this.placeDoor(☃, ☃, ☃, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
            this.fillWithBlocks(☃, ☃, 3, 1, 2, 3, 1, 4, Blocks.STONEBRICK.getDefaultState(), Blocks.STONEBRICK.getDefaultState(), false);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, ☃);

            for (int ☃ = 2; ☃ <= 4; ☃++) {
               this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, ☃, ☃);
            }

            if (!this.hasMadeChest && ☃.isVecInside(new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
               this.hasMadeChest = true;
               this.generateChest(☃, ☃, ☃, 3, 2, 3, LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
            }

            return true;
         }
      }
   }

   public static class Corridor extends StructureStrongholdPieces.Stronghold {
      private int steps;

      public Corridor() {
      }

      public Corridor(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.steps = ☃ != EnumFacing.NORTH && ☃ != EnumFacing.SOUTH ? ☃.getXSize() : ☃.getZSize();
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setInteger("Steps", this.steps);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.steps = ☃.getInteger("Steps");
      }

      public static StructureBoundingBox findPieceBox(List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5) {
         int ☃ = 3;
         StructureBoundingBox ☃x = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -1, 0, 5, 5, 4, ☃);
         StructureComponent ☃xx = StructureComponent.findIntersecting(☃, ☃x);
         if (☃xx == null) {
            return null;
         } else {
            if (☃xx.getBoundingBox().minY == ☃x.minY) {
               for (int ☃xxx = 3; ☃xxx >= 1; ☃xxx--) {
                  ☃x = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -1, 0, 5, 5, ☃xxx - 1, ☃);
                  if (!☃xx.getBoundingBox().intersectsWith(☃x)) {
                     return StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -1, 0, 5, 5, ☃xxx, ☃);
                  }
               }
            }

            return null;
         }
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            for (int ☃ = 0; ☃ < this.steps; ☃++) {
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 0, 0, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 0, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 2, 0, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3, 0, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 4, 0, ☃, ☃);

               for (int ☃x = 1; ☃x <= 3; ☃x++) {
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 0, ☃x, ☃, ☃);
                  this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, ☃x, ☃, ☃);
                  this.setBlockState(☃, Blocks.AIR.getDefaultState(), 2, ☃x, ☃, ☃);
                  this.setBlockState(☃, Blocks.AIR.getDefaultState(), 3, ☃x, ☃, ☃);
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 4, ☃x, ☃, ☃);
               }

               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 0, 4, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 4, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 2, 4, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3, 4, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 4, 4, ☃, ☃);
            }

            return true;
         }
      }
   }

   public static class Crossing extends StructureStrongholdPieces.Stronghold {
      private boolean leftLow;
      private boolean leftHigh;
      private boolean rightLow;
      private boolean rightHigh;

      public Crossing() {
      }

      public Crossing(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
         this.leftLow = ☃.nextBoolean();
         this.leftHigh = ☃.nextBoolean();
         this.rightLow = ☃.nextBoolean();
         this.rightHigh = ☃.nextInt(3) > 0;
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("leftLow", this.leftLow);
         ☃.setBoolean("leftHigh", this.leftHigh);
         ☃.setBoolean("rightLow", this.rightLow);
         ☃.setBoolean("rightHigh", this.rightHigh);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.leftLow = ☃.getBoolean("leftLow");
         this.leftHigh = ☃.getBoolean("leftHigh");
         this.rightLow = ☃.getBoolean("rightLow");
         this.rightHigh = ☃.getBoolean("rightHigh");
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         int ☃ = 3;
         int ☃x = 5;
         EnumFacing ☃xx = this.getCoordBaseMode();
         if (☃xx == EnumFacing.WEST || ☃xx == EnumFacing.NORTH) {
            ☃ = 8 - ☃;
            ☃x = 8 - ☃x;
         }

         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 5, 1);
         if (this.leftLow) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, ☃, 1);
         }

         if (this.leftHigh) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, ☃x, 7);
         }

         if (this.rightLow) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, ☃, 1);
         }

         if (this.rightHigh) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, ☃x, 7);
         }
      }

      public static StructureStrongholdPieces.Crossing createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -4, -3, 0, 10, 9, 11, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.Crossing(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 9, 8, 10, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 4, 3, 0);
            if (this.leftLow) {
               this.fillWithBlocks(☃, ☃, 0, 3, 1, 0, 5, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            if (this.rightLow) {
               this.fillWithBlocks(☃, ☃, 9, 3, 1, 9, 5, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            if (this.leftHigh) {
               this.fillWithBlocks(☃, ☃, 0, 5, 7, 0, 7, 9, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            if (this.rightHigh) {
               this.fillWithBlocks(☃, ☃, 9, 5, 7, 9, 7, 9, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            this.fillWithBlocks(☃, ☃, 5, 1, 10, 7, 3, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(☃, ☃, 1, 2, 1, 8, 2, 6, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 5, 4, 4, 9, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(☃, ☃, 8, 1, 5, 8, 4, 9, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(☃, ☃, 1, 4, 7, 3, 4, 9, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(☃, ☃, 1, 3, 5, 3, 3, 6, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithBlocks(☃, ☃, 1, 3, 4, 3, 3, 4, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 1, 4, 6, 3, 4, 6, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithRandomizedBlocks(☃, ☃, 5, 1, 7, 7, 1, 8, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithBlocks(☃, ☃, 5, 1, 9, 7, 1, 9, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 5, 2, 7, 7, 2, 7, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 4, 5, 7, 4, 5, 9, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 8, 5, 7, 8, 5, 9, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 5, 5, 7, 7, 5, 9, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
            this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 6, 5, 6, ☃);
            return true;
         }
      }
   }

   public static class LeftTurn extends StructureStrongholdPieces.Stronghold {
      public LeftTurn() {
      }

      public LeftTurn(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != EnumFacing.NORTH && ☃ != EnumFacing.EAST) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
         } else {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
         }
      }

      public static StructureStrongholdPieces.LeftTurn createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -1, 0, 5, 5, 5, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.LeftTurn(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 4, 4, 4, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 1, 1, 0);
            EnumFacing ☃ = this.getCoordBaseMode();
            if (☃ != EnumFacing.NORTH && ☃ != EnumFacing.EAST) {
               this.fillWithBlocks(☃, ☃, 4, 1, 1, 4, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            } else {
               this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            return true;
         }
      }
   }

   public static class Library extends StructureStrongholdPieces.Stronghold {
      private boolean isLargeRoom;

      public Library() {
      }

      public Library(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
         this.isLargeRoom = ☃.getYSize() > 6;
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Tall", this.isLargeRoom);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.isLargeRoom = ☃.getBoolean("Tall");
      }

      public static StructureStrongholdPieces.Library createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -4, -1, 0, 14, 11, 15, ☃);
         if (!canStrongholdGoDeeper(☃) || StructureComponent.findIntersecting(☃, ☃) != null) {
            ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -4, -1, 0, 14, 6, 15, ☃);
            if (!canStrongholdGoDeeper(☃) || StructureComponent.findIntersecting(☃, ☃) != null) {
               return null;
            }
         }

         return new StructureStrongholdPieces.Library(☃, ☃, ☃, ☃);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            int ☃ = 11;
            if (!this.isLargeRoom) {
               ☃ = 6;
            }

            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 13, ☃ - 1, 14, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 4, 1, 0);
            this.generateMaybeBox(☃, ☃, ☃, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.WEB.getDefaultState(), Blocks.WEB.getDefaultState(), false, 0);
            int ☃x = 1;
            int ☃xx = 12;

            for (int ☃xxx = 1; ☃xxx <= 13; ☃xxx++) {
               if ((☃xxx - 1) % 4 == 0) {
                  this.fillWithBlocks(☃, ☃, 1, 1, ☃xxx, 1, 4, ☃xxx, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                  this.fillWithBlocks(☃, ☃, 12, 1, ☃xxx, 12, 4, ☃xxx, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                  this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST), 2, 3, ☃xxx, ☃);
                  this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST), 11, 3, ☃xxx, ☃);
                  if (this.isLargeRoom) {
                     this.fillWithBlocks(☃, ☃, 1, 6, ☃xxx, 1, 9, ☃xxx, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                     this.fillWithBlocks(☃, ☃, 12, 6, ☃xxx, 12, 9, ☃xxx, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                  }
               } else {
                  this.fillWithBlocks(☃, ☃, 1, 1, ☃xxx, 1, 4, ☃xxx, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                  this.fillWithBlocks(☃, ☃, 12, 1, ☃xxx, 12, 4, ☃xxx, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                  if (this.isLargeRoom) {
                     this.fillWithBlocks(☃, ☃, 1, 6, ☃xxx, 1, 9, ☃xxx, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                     this.fillWithBlocks(☃, ☃, 12, 6, ☃xxx, 12, 9, ☃xxx, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                  }
               }
            }

            for (int ☃xxxx = 3; ☃xxxx < 12; ☃xxxx += 2) {
               this.fillWithBlocks(☃, ☃, 3, 1, ☃xxxx, 4, 3, ☃xxxx, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 6, 1, ☃xxxx, 7, 3, ☃xxxx, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 9, 1, ☃xxxx, 10, 3, ☃xxxx, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }

            if (this.isLargeRoom) {
               this.fillWithBlocks(☃, ☃, 1, 5, 1, 3, 5, 13, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 10, 5, 1, 12, 5, 13, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 4, 5, 1, 9, 5, 2, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 4, 5, 12, 9, 5, 13, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
               this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 9, 5, 11, ☃);
               this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 8, 5, 11, ☃);
               this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 9, 5, 10, ☃);
               this.fillWithBlocks(☃, ☃, 3, 6, 2, 3, 6, 12, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 10, 6, 2, 10, 6, 10, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 4, 6, 2, 9, 6, 2, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
               this.fillWithBlocks(☃, ☃, 4, 6, 12, 8, 6, 12, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 9, 6, 11, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 8, 6, 11, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 9, 6, 10, ☃);
               IBlockState ☃xxxx = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.SOUTH);
               this.setBlockState(☃, ☃xxxx, 10, 1, 13, ☃);
               this.setBlockState(☃, ☃xxxx, 10, 2, 13, ☃);
               this.setBlockState(☃, ☃xxxx, 10, 3, 13, ☃);
               this.setBlockState(☃, ☃xxxx, 10, 4, 13, ☃);
               this.setBlockState(☃, ☃xxxx, 10, 5, 13, ☃);
               this.setBlockState(☃, ☃xxxx, 10, 6, 13, ☃);
               this.setBlockState(☃, ☃xxxx, 10, 7, 13, ☃);
               int ☃xxxxx = 7;
               int ☃xxxxxx = 7;
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 6, 9, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 7, 9, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 6, 8, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 7, 8, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 6, 7, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 7, 7, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 5, 7, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 8, 7, 7, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 6, 7, 6, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 6, 7, 8, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 7, 7, 6, ☃);
               this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 7, 7, 8, ☃);
               IBlockState ☃xxxxxxx = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.UP);
               this.setBlockState(☃, ☃xxxxxxx, 5, 8, 7, ☃);
               this.setBlockState(☃, ☃xxxxxxx, 8, 8, 7, ☃);
               this.setBlockState(☃, ☃xxxxxxx, 6, 8, 6, ☃);
               this.setBlockState(☃, ☃xxxxxxx, 6, 8, 8, ☃);
               this.setBlockState(☃, ☃xxxxxxx, 7, 8, 6, ☃);
               this.setBlockState(☃, ☃xxxxxxx, 7, 8, 8, ☃);
            }

            this.generateChest(☃, ☃, ☃, 3, 3, 5, LootTableList.CHESTS_STRONGHOLD_LIBRARY);
            if (this.isLargeRoom) {
               this.setBlockState(☃, Blocks.AIR.getDefaultState(), 12, 9, 1, ☃);
               this.generateChest(☃, ☃, ☃, 12, 8, 1, LootTableList.CHESTS_STRONGHOLD_LIBRARY);
            }

            return true;
         }
      }
   }

   static class PieceWeight {
      public Class<? extends StructureStrongholdPieces.Stronghold> pieceClass;
      public final int pieceWeight;
      public int instancesSpawned;
      public int instancesLimit;

      public PieceWeight(Class<? extends StructureStrongholdPieces.Stronghold> var1, int var2, int var3) {
         this.pieceClass = ☃;
         this.pieceWeight = ☃;
         this.instancesLimit = ☃;
      }

      public boolean canSpawnMoreStructuresOfType(int var1) {
         return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
      }

      public boolean canSpawnMoreStructures() {
         return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
      }
   }

   public static class PortalRoom extends StructureStrongholdPieces.Stronghold {
      private boolean hasSpawner;

      public PortalRoom() {
      }

      public PortalRoom(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Mob", this.hasSpawner);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.hasSpawner = ☃.getBoolean("Mob");
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         if (☃ != null) {
            ((StructureStrongholdPieces.Stairs2)☃).strongholdPortalRoom = this;
         }
      }

      public static StructureStrongholdPieces.PortalRoom createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -4, -1, 0, 11, 8, 16, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.PortalRoom(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 10, 7, 15, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.placeDoor(☃, ☃, ☃, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
         int ☃ = 6;
         this.fillWithRandomizedBlocks(☃, ☃, 1, ☃, 1, 1, ☃, 14, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithRandomizedBlocks(☃, ☃, 9, ☃, 1, 9, ☃, 14, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithRandomizedBlocks(☃, ☃, 2, ☃, 1, 8, ☃, 2, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithRandomizedBlocks(☃, ☃, 2, ☃, 14, 8, ☃, 14, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithRandomizedBlocks(☃, ☃, 1, 1, 1, 2, 1, 4, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithRandomizedBlocks(☃, ☃, 8, 1, 1, 9, 1, 4, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithBlocks(☃, ☃, 1, 1, 1, 1, 1, 3, Blocks.FLOWING_LAVA.getDefaultState(), Blocks.FLOWING_LAVA.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 1, 1, 9, 1, 3, Blocks.FLOWING_LAVA.getDefaultState(), Blocks.FLOWING_LAVA.getDefaultState(), false);
         this.fillWithRandomizedBlocks(☃, ☃, 3, 1, 8, 7, 1, 12, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithBlocks(☃, ☃, 4, 1, 9, 6, 1, 11, Blocks.FLOWING_LAVA.getDefaultState(), Blocks.FLOWING_LAVA.getDefaultState(), false);

         for (int ☃x = 3; ☃x < 14; ☃x += 2) {
            this.fillWithBlocks(☃, ☃, 0, 3, ☃x, 0, 4, ☃x, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 10, 3, ☃x, 10, 4, ☃x, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
         }

         for (int ☃x = 2; ☃x < 9; ☃x += 2) {
            this.fillWithBlocks(☃, ☃, ☃x, 3, 15, ☃x, 4, 15, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
         }

         IBlockState ☃x = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
         this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 5, 6, 1, 7, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithRandomizedBlocks(☃, ☃, 4, 2, 6, 6, 2, 7, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
         this.fillWithRandomizedBlocks(☃, ☃, 4, 3, 7, 6, 3, 7, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);

         for (int ☃xx = 4; ☃xx <= 6; ☃xx++) {
            this.setBlockState(☃, ☃x, ☃xx, 1, 4, ☃);
            this.setBlockState(☃, ☃x, ☃xx, 2, 5, ☃);
            this.setBlockState(☃, ☃x, ☃xx, 3, 6, ☃);
         }

         IBlockState ☃xx = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.NORTH);
         IBlockState ☃xxx = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.SOUTH);
         IBlockState ☃xxxx = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.EAST);
         IBlockState ☃xxxxx = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.WEST);
         boolean ☃xxxxxx = true;
         boolean[] ☃xxxxxxx = new boolean[12];

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxxxxxx.length; ☃xxxxxxxx++) {
            ☃xxxxxxx[☃xxxxxxxx] = ☃.nextFloat() > 0.9F;
            ☃xxxxxx &= ☃xxxxxxx[☃xxxxxxxx];
         }

         this.setBlockState(☃, ☃xx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[0]), 4, 3, 8, ☃);
         this.setBlockState(☃, ☃xx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[1]), 5, 3, 8, ☃);
         this.setBlockState(☃, ☃xx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[2]), 6, 3, 8, ☃);
         this.setBlockState(☃, ☃xxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[3]), 4, 3, 12, ☃);
         this.setBlockState(☃, ☃xxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[4]), 5, 3, 12, ☃);
         this.setBlockState(☃, ☃xxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[5]), 6, 3, 12, ☃);
         this.setBlockState(☃, ☃xxxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[6]), 3, 3, 9, ☃);
         this.setBlockState(☃, ☃xxxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[7]), 3, 3, 10, ☃);
         this.setBlockState(☃, ☃xxxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[8]), 3, 3, 11, ☃);
         this.setBlockState(☃, ☃xxxxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[9]), 7, 3, 9, ☃);
         this.setBlockState(☃, ☃xxxxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[10]), 7, 3, 10, ☃);
         this.setBlockState(☃, ☃xxxxx.withProperty(BlockEndPortalFrame.EYE, ☃xxxxxxx[11]), 7, 3, 11, ☃);
         if (☃xxxxxx) {
            IBlockState ☃xxxxxxxx = Blocks.END_PORTAL.getDefaultState();
            this.setBlockState(☃, ☃xxxxxxxx, 4, 3, 9, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 5, 3, 9, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 6, 3, 9, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 4, 3, 10, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 5, 3, 10, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 6, 3, 10, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 4, 3, 11, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 5, 3, 11, ☃);
            this.setBlockState(☃, ☃xxxxxxxx, 6, 3, 11, ☃);
         }

         if (!this.hasSpawner) {
            ☃ = this.getYWithOffset(3);
            BlockPos ☃xxxxxxxx = new BlockPos(this.getXWithOffset(5, 6), ☃, this.getZWithOffset(5, 6));
            if (☃.isVecInside(☃xxxxxxxx)) {
               this.hasSpawner = true;
               ☃.setBlockState(☃xxxxxxxx, Blocks.MOB_SPAWNER.getDefaultState(), 2);
               TileEntity ☃xxxxxxxxx = ☃.getTileEntity(☃xxxxxxxx);
               if (☃xxxxxxxxx instanceof TileEntityMobSpawner) {
                  ((TileEntityMobSpawner)☃xxxxxxxxx).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntitySilverfish.class));
               }
            }
         }

         return true;
      }
   }

   public static class Prison extends StructureStrongholdPieces.Stronghold {
      public Prison() {
      }

      public Prison(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
      }

      public static StructureStrongholdPieces.Prison createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -1, 0, 9, 5, 11, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.Prison(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 8, 4, 10, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 1, 1, 0);
            this.fillWithBlocks(☃, ☃, 1, 1, 10, 3, 3, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 1, 4, 3, 1, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 3, 4, 3, 3, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 7, 4, 3, 7, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 9, 4, 3, 9, false, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithBlocks(☃, ☃, 4, 1, 4, 4, 3, 6, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 5, 1, 5, 7, 3, 5, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), 4, 3, 2, ☃);
            this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), 4, 3, 8, ☃);
            IBlockState ☃ = Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST);
            IBlockState ☃x = Blocks.IRON_DOOR
               .getDefaultState()
               .withProperty(BlockDoor.FACING, EnumFacing.WEST)
               .withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER);
            this.setBlockState(☃, ☃, 4, 1, 2, ☃);
            this.setBlockState(☃, ☃x, 4, 2, 2, ☃);
            this.setBlockState(☃, ☃, 4, 1, 8, ☃);
            this.setBlockState(☃, ☃x, 4, 2, 8, ☃);
            return true;
         }
      }
   }

   public static class RightTurn extends StructureStrongholdPieces.LeftTurn {
      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != EnumFacing.NORTH && ☃ != EnumFacing.EAST) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
         } else {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
         }
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 4, 4, 4, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 1, 1, 0);
            EnumFacing ☃ = this.getCoordBaseMode();
            if (☃ != EnumFacing.NORTH && ☃ != EnumFacing.EAST) {
               this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            } else {
               this.fillWithBlocks(☃, ☃, 4, 1, 1, 4, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            return true;
         }
      }
   }

   public static class RoomCrossing extends StructureStrongholdPieces.Stronghold {
      protected int roomType;

      public RoomCrossing() {
      }

      public RoomCrossing(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
         this.roomType = ☃.nextInt(5);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setInteger("Type", this.roomType);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.roomType = ☃.getInteger("Type");
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 4, 1);
         this.getNextComponentX((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 4);
         this.getNextComponentZ((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 4);
      }

      public static StructureStrongholdPieces.RoomCrossing createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -4, -1, 0, 11, 7, 11, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.RoomCrossing(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 10, 6, 10, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 4, 1, 0);
            this.fillWithBlocks(☃, ☃, 4, 1, 10, 6, 3, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 0, 1, 4, 0, 3, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 10, 1, 4, 10, 3, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            switch (this.roomType) {
               case 0:
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 5, 1, 5, ☃);
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 5, 2, 5, ☃);
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 5, 3, 5, ☃);
                  this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST), 4, 3, 5, ☃);
                  this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST), 6, 3, 5, ☃);
                  this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 5, 3, 4, ☃);
                  this.setBlockState(☃, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH), 5, 3, 6, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 4, 1, 4, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 4, 1, 5, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 4, 1, 6, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 6, 1, 4, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 6, 1, 5, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 6, 1, 6, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 5, 1, 4, ☃);
                  this.setBlockState(☃, Blocks.STONE_SLAB.getDefaultState(), 5, 1, 6, ☃);
                  break;
               case 1:
                  for (int ☃ = 0; ☃ < 5; ☃++) {
                     this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3, 1, 3 + ☃, ☃);
                     this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 7, 1, 3 + ☃, ☃);
                     this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3 + ☃, 1, 3, ☃);
                     this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3 + ☃, 1, 7, ☃);
                  }

                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 5, 1, 5, ☃);
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 5, 2, 5, ☃);
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 5, 3, 5, ☃);
                  this.setBlockState(☃, Blocks.FLOWING_WATER.getDefaultState(), 5, 4, 5, ☃);
                  break;
               case 2:
                  for (int ☃ = 1; ☃ <= 9; ☃++) {
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 1, 3, ☃, ☃);
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 9, 3, ☃, ☃);
                  }

                  for (int ☃ = 1; ☃ <= 9; ☃++) {
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), ☃, 3, 1, ☃);
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), ☃, 3, 9, ☃);
                  }

                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 5, 1, 4, ☃);
                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 5, 1, 6, ☃);
                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 5, 3, 4, ☃);
                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 5, 3, 6, ☃);
                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 4, 1, 5, ☃);
                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 6, 1, 5, ☃);
                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 4, 3, 5, ☃);
                  this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 6, 3, 5, ☃);

                  for (int ☃ = 1; ☃ <= 3; ☃++) {
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 4, ☃, 4, ☃);
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 6, ☃, 4, ☃);
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 4, ☃, 6, ☃);
                     this.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState(), 6, ☃, 6, ☃);
                  }

                  this.setBlockState(☃, Blocks.TORCH.getDefaultState(), 5, 3, 5, ☃);

                  for (int ☃ = 2; ☃ <= 8; ☃++) {
                     this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 2, 3, ☃, ☃);
                     this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 3, 3, ☃, ☃);
                     if (☃ <= 3 || ☃ >= 7) {
                        this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 4, 3, ☃, ☃);
                        this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 5, 3, ☃, ☃);
                        this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 6, 3, ☃, ☃);
                     }

                     this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 7, 3, ☃, ☃);
                     this.setBlockState(☃, Blocks.PLANKS.getDefaultState(), 8, 3, ☃, ☃);
                  }

                  IBlockState ☃ = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST);
                  this.setBlockState(☃, ☃, 9, 1, 3, ☃);
                  this.setBlockState(☃, ☃, 9, 2, 3, ☃);
                  this.setBlockState(☃, ☃, 9, 3, 3, ☃);
                  this.generateChest(☃, ☃, ☃, 3, 4, 8, LootTableList.CHESTS_STRONGHOLD_CROSSING);
            }

            return true;
         }
      }
   }

   public static class Stairs extends StructureStrongholdPieces.Stronghold {
      private boolean source;

      public Stairs() {
      }

      public Stairs(int var1, Random var2, int var3, int var4) {
         super(☃);
         this.source = true;
         this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(☃));
         this.entryDoor = StructureStrongholdPieces.Stronghold.Door.OPENING;
         if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
            this.boundingBox = new StructureBoundingBox(☃, 64, ☃, ☃ + 5 - 1, 74, ☃ + 5 - 1);
         } else {
            this.boundingBox = new StructureBoundingBox(☃, 64, ☃, ☃ + 5 - 1, 74, ☃ + 5 - 1);
         }
      }

      public Stairs(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.source = false;
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Source", this.source);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.source = ☃.getBoolean("Source");
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         if (this.source) {
            StructureStrongholdPieces.strongComponentType = StructureStrongholdPieces.Crossing.class;
         }

         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
      }

      public static StructureStrongholdPieces.Stairs createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -7, 0, 5, 11, 5, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.Stairs(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 4, 10, 4, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 1, 7, 0);
            this.placeDoor(☃, ☃, ☃, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 2, 6, 1, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 5, 1, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 5, 2, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 4, 3, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 2, 4, 3, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3, 3, 3, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3, 3, 2, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3, 2, 1, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 2, 2, 1, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 1, 1, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 1, 2, ☃);
            this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, ☃);
            return true;
         }
      }
   }

   public static class Stairs2 extends StructureStrongholdPieces.Stairs {
      public StructureStrongholdPieces.PieceWeight lastPlaced;
      public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;
      public List<StructureComponent> pendingChildren = Lists.newArrayList();

      public Stairs2() {
      }

      public Stairs2(int var1, Random var2, int var3, int var4) {
         super(0, ☃, ☃, ☃);
      }
   }

   public static class StairsStraight extends StructureStrongholdPieces.Stronghold {
      public StairsStraight() {
      }

      public StairsStraight(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
      }

      public static StructureStrongholdPieces.StairsStraight createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -7, 0, 5, 11, 8, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.StairsStraight(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 4, 10, 7, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 1, 7, 0);
            this.placeDoor(☃, ☃, ☃, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
            IBlockState ☃ = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

            for (int ☃x = 0; ☃x < 6; ☃x++) {
               this.setBlockState(☃, ☃, 1, 6 - ☃x, 1 + ☃x, ☃);
               this.setBlockState(☃, ☃, 2, 6 - ☃x, 1 + ☃x, ☃);
               this.setBlockState(☃, ☃, 3, 6 - ☃x, 1 + ☃x, ☃);
               if (☃x < 5) {
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 1, 5 - ☃x, 1 + ☃x, ☃);
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 2, 5 - ☃x, 1 + ☃x, ☃);
                  this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), 3, 5 - ☃x, 1 + ☃x, ☃);
               }
            }

            return true;
         }
      }
   }

   static class Stones extends StructureComponent.BlockSelector {
      private Stones() {
      }

      @Override
      public void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5) {
         if (☃) {
            float ☃ = ☃.nextFloat();
            if (☃ < 0.2F) {
               this.blockstate = Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CRACKED_META);
            } else if (☃ < 0.5F) {
               this.blockstate = Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.MOSSY_META);
            } else if (☃ < 0.55F) {
               this.blockstate = Blocks.MONSTER_EGG.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata());
            } else {
               this.blockstate = Blocks.STONEBRICK.getDefaultState();
            }
         } else {
            this.blockstate = Blocks.AIR.getDefaultState();
         }
      }
   }

   public static class Straight extends StructureStrongholdPieces.Stronghold {
      private boolean expandsX;
      private boolean expandsZ;

      public Straight() {
      }

      public Straight(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.entryDoor = this.getRandomDoor(☃);
         this.boundingBox = ☃;
         this.expandsX = ☃.nextInt(2) == 0;
         this.expandsZ = ☃.nextInt(2) == 0;
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Left", this.expandsX);
         ☃.setBoolean("Right", this.expandsZ);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.expandsX = ☃.getBoolean("Left");
         this.expandsZ = ☃.getBoolean("Right");
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 1);
         if (this.expandsX) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 2);
         }

         if (this.expandsZ) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)☃, ☃, ☃, 1, 2);
         }
      }

      public static StructureStrongholdPieces.Straight createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -1, 0, 5, 5, 7, ☃);
         return canStrongholdGoDeeper(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureStrongholdPieces.Straight(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(☃, ☃)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, 0, 0, 4, 4, 6, true, ☃, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(☃, ☃, ☃, this.entryDoor, 1, 1, 0);
            this.placeDoor(☃, ☃, ☃, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
            IBlockState ☃ = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST);
            IBlockState ☃x = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST);
            this.randomlyPlaceBlock(☃, ☃, ☃, 0.1F, 1, 2, 1, ☃);
            this.randomlyPlaceBlock(☃, ☃, ☃, 0.1F, 3, 2, 1, ☃x);
            this.randomlyPlaceBlock(☃, ☃, ☃, 0.1F, 1, 2, 5, ☃);
            this.randomlyPlaceBlock(☃, ☃, ☃, 0.1F, 3, 2, 5, ☃x);
            if (this.expandsX) {
               this.fillWithBlocks(☃, ☃, 0, 1, 2, 0, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            if (this.expandsZ) {
               this.fillWithBlocks(☃, ☃, 4, 1, 2, 4, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            return true;
         }
      }
   }

   abstract static class Stronghold extends StructureComponent {
      protected StructureStrongholdPieces.Stronghold.Door entryDoor = StructureStrongholdPieces.Stronghold.Door.OPENING;

      public Stronghold() {
      }

      protected Stronghold(int var1) {
         super(☃);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         ☃.setString("EntryDoor", this.entryDoor.name());
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         this.entryDoor = StructureStrongholdPieces.Stronghold.Door.valueOf(☃.getString("EntryDoor"));
      }

      protected void placeDoor(World var1, Random var2, StructureBoundingBox var3, StructureStrongholdPieces.Stronghold.Door var4, int var5, int var6, int var7) {
         switch (☃) {
            case OPENING:
               this.fillWithBlocks(☃, ☃, ☃, ☃, ☃, ☃ + 3 - 1, ☃ + 3 - 1, ☃, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
               break;
            case WOOD_DOOR:
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 1, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 2, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 2, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 2, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.OAK_DOOR.getDefaultState(), ☃ + 1, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), ☃ + 1, ☃ + 1, ☃, ☃);
               break;
            case GRATES:
               this.setBlockState(☃, Blocks.AIR.getDefaultState(), ☃ + 1, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.AIR.getDefaultState(), ☃ + 1, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), ☃, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), ☃, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), ☃, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), ☃ + 1, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), ☃ + 2, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), ☃ + 2, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_BARS.getDefaultState(), ☃ + 2, ☃, ☃, ☃);
               break;
            case IRON_DOOR:
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 1, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 2, ☃ + 2, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 2, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.STONEBRICK.getDefaultState(), ☃ + 2, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_DOOR.getDefaultState(), ☃ + 1, ☃, ☃, ☃);
               this.setBlockState(☃, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), ☃ + 1, ☃ + 1, ☃, ☃);
               this.setBlockState(☃, Blocks.STONE_BUTTON.getDefaultState().withProperty(BlockButton.FACING, EnumFacing.NORTH), ☃ + 2, ☃ + 1, ☃ + 1, ☃);
               this.setBlockState(☃, Blocks.STONE_BUTTON.getDefaultState().withProperty(BlockButton.FACING, EnumFacing.SOUTH), ☃ + 2, ☃ + 1, ☃ - 1, ☃);
         }
      }

      protected StructureStrongholdPieces.Stronghold.Door getRandomDoor(Random var1) {
         int ☃ = ☃.nextInt(5);
         switch (☃) {
            case 0:
            case 1:
            default:
               return StructureStrongholdPieces.Stronghold.Door.OPENING;
            case 2:
               return StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR;
            case 3:
               return StructureStrongholdPieces.Stronghold.Door.GRATES;
            case 4:
               return StructureStrongholdPieces.Stronghold.Door.IRON_DOOR;
         }
      }

      @Nullable
      protected StructureComponent getNextComponentNormal(
         StructureStrongholdPieces.Stairs2 var1, List<StructureComponent> var2, Random var3, int var4, int var5
      ) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, ☃, this.getComponentType()
                  );
               case SOUTH:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, ☃, this.getComponentType()
                  );
               case WEST:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, ☃, this.getComponentType()
                  );
               case EAST:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, ☃, this.getComponentType()
                  );
            }
         }

         return null;
      }

      @Nullable
      protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 var1, List<StructureComponent> var2, Random var3, int var4, int var5) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.WEST, this.getComponentType()
                  );
               case SOUTH:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.WEST, this.getComponentType()
                  );
               case WEST:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType()
                  );
               case EAST:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType()
                  );
            }
         }

         return null;
      }

      @Nullable
      protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 var1, List<StructureComponent> var2, Random var3, int var4, int var5) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.EAST, this.getComponentType()
                  );
               case SOUTH:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.EAST, this.getComponentType()
                  );
               case WEST:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType()
                  );
               case EAST:
                  return StructureStrongholdPieces.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType()
                  );
            }
         }

         return null;
      }

      protected static boolean canStrongholdGoDeeper(StructureBoundingBox var0) {
         return ☃ != null && ☃.minY > 10;
      }

      public static enum Door {
         OPENING,
         WOOD_DOOR,
         GRATES,
         IRON_DOOR;
      }
   }
}
