package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class StructureOceanMonumentPieces {
   public static void registerOceanMonumentPieces() {
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.MonumentBuilding.class, "OMB");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.MonumentCoreRoom.class, "OMCR");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleXRoom.class, "OMDXR");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleXYRoom.class, "OMDXYR");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleYRoom.class, "OMDYR");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleYZRoom.class, "OMDYZR");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleZRoom.class, "OMDZR");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.EntryRoom.class, "OMEntry");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.Penthouse.class, "OMPenthouse");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.SimpleRoom.class, "OMSimple");
      MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.SimpleTopRoom.class, "OMSimpleT");
   }

   public static class DoubleXRoom extends StructureOceanMonumentPieces.Piece {
      public DoubleXRoom() {
      }

      public DoubleXRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 2, 1, 1);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         StructureOceanMonumentPieces.RoomDefinition ☃ = this.roomDefinition.connections[EnumFacing.EAST.getIndex()];
         StructureOceanMonumentPieces.RoomDefinition ☃x = this.roomDefinition;
         if (this.roomDefinition.index / 25 > 0) {
            this.generateDefaultFloor(☃, ☃, 8, 0, ☃.hasOpening[EnumFacing.DOWN.getIndex()]);
            this.generateDefaultFloor(☃, ☃, 0, 0, ☃x.hasOpening[EnumFacing.DOWN.getIndex()]);
         }

         if (☃x.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 4, 1, 7, 4, 6, ROUGH_PRISMARINE);
         }

         if (☃.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 8, 4, 1, 14, 4, 6, ROUGH_PRISMARINE);
         }

         this.fillWithBlocks(☃, ☃, 0, 3, 0, 0, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 15, 3, 0, 15, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 0, 15, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 7, 14, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 15, 2, 0, 15, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 0, 15, 2, 0, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 7, 14, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 15, 1, 0, 15, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 0, 15, 1, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 7, 14, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 1, 0, 10, 1, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 2, 0, 9, 2, 3, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 3, 0, 10, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.setBlockState(☃, SEA_LANTERN, 6, 2, 3, ☃);
         this.setBlockState(☃, SEA_LANTERN, 9, 2, 3, ☃);
         if (☃x.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 0, 4, 2, 0, false);
         }

         if (☃x.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 7, 4, 2, 7, false);
         }

         if (☃x.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 1, 3, 0, 2, 4, false);
         }

         if (☃.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 11, 1, 0, 12, 2, 0, false);
         }

         if (☃.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 11, 1, 7, 12, 2, 7, false);
         }

         if (☃.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 15, 1, 3, 15, 2, 4, false);
         }

         return true;
      }
   }

   public static class DoubleXYRoom extends StructureOceanMonumentPieces.Piece {
      public DoubleXYRoom() {
      }

      public DoubleXYRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 2, 2, 1);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         StructureOceanMonumentPieces.RoomDefinition ☃ = this.roomDefinition.connections[EnumFacing.EAST.getIndex()];
         StructureOceanMonumentPieces.RoomDefinition ☃x = this.roomDefinition;
         StructureOceanMonumentPieces.RoomDefinition ☃xx = ☃x.connections[EnumFacing.UP.getIndex()];
         StructureOceanMonumentPieces.RoomDefinition ☃xxx = ☃.connections[EnumFacing.UP.getIndex()];
         if (this.roomDefinition.index / 25 > 0) {
            this.generateDefaultFloor(☃, ☃, 8, 0, ☃.hasOpening[EnumFacing.DOWN.getIndex()]);
            this.generateDefaultFloor(☃, ☃, 0, 0, ☃x.hasOpening[EnumFacing.DOWN.getIndex()]);
         }

         if (☃xx.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 8, 1, 7, 8, 6, ROUGH_PRISMARINE);
         }

         if (☃xxx.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 8, 8, 1, 14, 8, 6, ROUGH_PRISMARINE);
         }

         for (int ☃xxxx = 1; ☃xxxx <= 7; ☃xxxx++) {
            IBlockState ☃xxxxx = BRICKS_PRISMARINE;
            if (☃xxxx == 2 || ☃xxxx == 6) {
               ☃xxxxx = ROUGH_PRISMARINE;
            }

            this.fillWithBlocks(☃, ☃, 0, ☃xxxx, 0, 0, ☃xxxx, 7, ☃xxxxx, ☃xxxxx, false);
            this.fillWithBlocks(☃, ☃, 15, ☃xxxx, 0, 15, ☃xxxx, 7, ☃xxxxx, ☃xxxxx, false);
            this.fillWithBlocks(☃, ☃, 1, ☃xxxx, 0, 15, ☃xxxx, 0, ☃xxxxx, ☃xxxxx, false);
            this.fillWithBlocks(☃, ☃, 1, ☃xxxx, 7, 14, ☃xxxx, 7, ☃xxxxx, ☃xxxxx, false);
         }

         this.fillWithBlocks(☃, ☃, 2, 1, 3, 2, 7, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 2, 4, 7, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 5, 4, 7, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 13, 1, 3, 13, 7, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 11, 1, 2, 12, 7, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 11, 1, 5, 12, 7, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 1, 3, 5, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 1, 3, 10, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 7, 2, 10, 7, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 5, 2, 5, 7, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 5, 2, 10, 7, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 5, 5, 5, 7, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 5, 5, 10, 7, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.setBlockState(☃, BRICKS_PRISMARINE, 6, 6, 2, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 9, 6, 2, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 6, 6, 5, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 9, 6, 5, ☃);
         this.fillWithBlocks(☃, ☃, 5, 4, 3, 6, 4, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 9, 4, 3, 10, 4, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.setBlockState(☃, SEA_LANTERN, 5, 4, 2, ☃);
         this.setBlockState(☃, SEA_LANTERN, 5, 4, 5, ☃);
         this.setBlockState(☃, SEA_LANTERN, 10, 4, 2, ☃);
         this.setBlockState(☃, SEA_LANTERN, 10, 4, 5, ☃);
         if (☃x.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 0, 4, 2, 0, false);
         }

         if (☃x.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 7, 4, 2, 7, false);
         }

         if (☃x.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 1, 3, 0, 2, 4, false);
         }

         if (☃.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 11, 1, 0, 12, 2, 0, false);
         }

         if (☃.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 11, 1, 7, 12, 2, 7, false);
         }

         if (☃.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 15, 1, 3, 15, 2, 4, false);
         }

         if (☃xx.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 5, 0, 4, 6, 0, false);
         }

         if (☃xx.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 5, 7, 4, 6, 7, false);
         }

         if (☃xx.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 5, 3, 0, 6, 4, false);
         }

         if (☃xxx.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 11, 5, 0, 12, 6, 0, false);
         }

         if (☃xxx.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 11, 5, 7, 12, 6, 7, false);
         }

         if (☃xxx.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 15, 5, 3, 15, 6, 4, false);
         }

         return true;
      }
   }

   public static class DoubleYRoom extends StructureOceanMonumentPieces.Piece {
      public DoubleYRoom() {
      }

      public DoubleYRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 1, 2, 1);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.roomDefinition.index / 25 > 0) {
            this.generateDefaultFloor(☃, ☃, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
         }

         StructureOceanMonumentPieces.RoomDefinition ☃ = this.roomDefinition.connections[EnumFacing.UP.getIndex()];
         if (☃.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 8, 1, 6, 8, 6, ROUGH_PRISMARINE);
         }

         this.fillWithBlocks(☃, ☃, 0, 4, 0, 0, 4, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 4, 0, 7, 4, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 4, 0, 6, 4, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 4, 7, 6, 4, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, 4, 1, 2, 4, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 4, 2, 1, 4, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 4, 1, 5, 4, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 4, 2, 6, 4, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, 4, 5, 2, 4, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 4, 5, 1, 4, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 4, 5, 5, 4, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 4, 5, 6, 4, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         StructureOceanMonumentPieces.RoomDefinition ☃x = this.roomDefinition;

         for (int ☃xx = 1; ☃xx <= 5; ☃xx += 4) {
            int ☃xxx = 0;
            if (☃x.hasOpening[EnumFacing.SOUTH.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 2, ☃xx, ☃xxx, 2, ☃xx + 2, ☃xxx, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 5, ☃xx, ☃xxx, 5, ☃xx + 2, ☃xxx, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 3, ☃xx + 2, ☃xxx, 4, ☃xx + 2, ☃xxx, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, 0, ☃xx, ☃xxx, 7, ☃xx + 2, ☃xxx, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 0, ☃xx + 1, ☃xxx, 7, ☃xx + 1, ☃xxx, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            }

            int var9 = 7;
            if (☃x.hasOpening[EnumFacing.NORTH.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 2, ☃xx, var9, 2, ☃xx + 2, var9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 5, ☃xx, var9, 5, ☃xx + 2, var9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 3, ☃xx + 2, var9, 4, ☃xx + 2, var9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, 0, ☃xx, var9, 7, ☃xx + 2, var9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 0, ☃xx + 1, var9, 7, ☃xx + 1, var9, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            }

            int ☃xxxx = 0;
            if (☃x.hasOpening[EnumFacing.WEST.getIndex()]) {
               this.fillWithBlocks(☃, ☃, ☃xxxx, ☃xx, 2, ☃xxxx, ☃xx + 2, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃xxxx, ☃xx, 5, ☃xxxx, ☃xx + 2, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃xxxx, ☃xx + 2, 3, ☃xxxx, ☃xx + 2, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, ☃xxxx, ☃xx, 0, ☃xxxx, ☃xx + 2, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃xxxx, ☃xx + 1, 0, ☃xxxx, ☃xx + 1, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            }

            int var10 = 7;
            if (☃x.hasOpening[EnumFacing.EAST.getIndex()]) {
               this.fillWithBlocks(☃, ☃, var10, ☃xx, 2, var10, ☃xx + 2, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, var10, ☃xx, 5, var10, ☃xx + 2, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, var10, ☃xx + 2, 3, var10, ☃xx + 2, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, var10, ☃xx, 0, var10, ☃xx + 2, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, var10, ☃xx + 1, 0, var10, ☃xx + 1, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            }

            ☃x = ☃;
         }

         return true;
      }
   }

   public static class DoubleYZRoom extends StructureOceanMonumentPieces.Piece {
      public DoubleYZRoom() {
      }

      public DoubleYZRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 1, 2, 2);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         StructureOceanMonumentPieces.RoomDefinition ☃ = this.roomDefinition.connections[EnumFacing.NORTH.getIndex()];
         StructureOceanMonumentPieces.RoomDefinition ☃x = this.roomDefinition;
         StructureOceanMonumentPieces.RoomDefinition ☃xx = ☃.connections[EnumFacing.UP.getIndex()];
         StructureOceanMonumentPieces.RoomDefinition ☃xxx = ☃x.connections[EnumFacing.UP.getIndex()];
         if (this.roomDefinition.index / 25 > 0) {
            this.generateDefaultFloor(☃, ☃, 0, 8, ☃.hasOpening[EnumFacing.DOWN.getIndex()]);
            this.generateDefaultFloor(☃, ☃, 0, 0, ☃x.hasOpening[EnumFacing.DOWN.getIndex()]);
         }

         if (☃xxx.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 8, 1, 6, 8, 7, ROUGH_PRISMARINE);
         }

         if (☃xx.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 8, 8, 6, 8, 14, ROUGH_PRISMARINE);
         }

         for (int ☃xxxx = 1; ☃xxxx <= 7; ☃xxxx++) {
            IBlockState ☃xxxxx = BRICKS_PRISMARINE;
            if (☃xxxx == 2 || ☃xxxx == 6) {
               ☃xxxxx = ROUGH_PRISMARINE;
            }

            this.fillWithBlocks(☃, ☃, 0, ☃xxxx, 0, 0, ☃xxxx, 15, ☃xxxxx, ☃xxxxx, false);
            this.fillWithBlocks(☃, ☃, 7, ☃xxxx, 0, 7, ☃xxxx, 15, ☃xxxxx, ☃xxxxx, false);
            this.fillWithBlocks(☃, ☃, 1, ☃xxxx, 0, 6, ☃xxxx, 0, ☃xxxxx, ☃xxxxx, false);
            this.fillWithBlocks(☃, ☃, 1, ☃xxxx, 15, 6, ☃xxxx, 15, ☃xxxxx, ☃xxxxx, false);
         }

         for (int ☃xxxx = 1; ☃xxxx <= 7; ☃xxxx++) {
            IBlockState ☃xxxxx = DARK_PRISMARINE;
            if (☃xxxx == 2 || ☃xxxx == 6) {
               ☃xxxxx = SEA_LANTERN;
            }

            this.fillWithBlocks(☃, ☃, 3, ☃xxxx, 7, 4, ☃xxxx, 8, ☃xxxxx, ☃xxxxx, false);
         }

         if (☃x.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 0, 4, 2, 0, false);
         }

         if (☃x.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 7, 1, 3, 7, 2, 4, false);
         }

         if (☃x.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 1, 3, 0, 2, 4, false);
         }

         if (☃.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 15, 4, 2, 15, false);
         }

         if (☃.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 1, 11, 0, 2, 12, false);
         }

         if (☃.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 7, 1, 11, 7, 2, 12, false);
         }

         if (☃xxx.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 5, 0, 4, 6, 0, false);
         }

         if (☃xxx.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 7, 5, 3, 7, 6, 4, false);
            this.fillWithBlocks(☃, ☃, 5, 4, 2, 6, 4, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 6, 1, 2, 6, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 6, 1, 5, 6, 3, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         }

         if (☃xxx.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 5, 3, 0, 6, 4, false);
            this.fillWithBlocks(☃, ☃, 1, 4, 2, 2, 4, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 1, 2, 1, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 1, 5, 1, 3, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         }

         if (☃xx.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 5, 15, 4, 6, 15, false);
         }

         if (☃xx.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 5, 11, 0, 6, 12, false);
            this.fillWithBlocks(☃, ☃, 1, 4, 10, 2, 4, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 1, 10, 1, 3, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 1, 13, 1, 3, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         }

         if (☃xx.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 7, 5, 11, 7, 6, 12, false);
            this.fillWithBlocks(☃, ☃, 5, 4, 10, 6, 4, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 6, 1, 10, 6, 3, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 6, 1, 13, 6, 3, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         }

         return true;
      }
   }

   public static class DoubleZRoom extends StructureOceanMonumentPieces.Piece {
      public DoubleZRoom() {
      }

      public DoubleZRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 1, 1, 2);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         StructureOceanMonumentPieces.RoomDefinition ☃ = this.roomDefinition.connections[EnumFacing.NORTH.getIndex()];
         StructureOceanMonumentPieces.RoomDefinition ☃x = this.roomDefinition;
         if (this.roomDefinition.index / 25 > 0) {
            this.generateDefaultFloor(☃, ☃, 0, 8, ☃.hasOpening[EnumFacing.DOWN.getIndex()]);
            this.generateDefaultFloor(☃, ☃, 0, 0, ☃x.hasOpening[EnumFacing.DOWN.getIndex()]);
         }

         if (☃x.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 4, 1, 6, 4, 7, ROUGH_PRISMARINE);
         }

         if (☃.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 4, 8, 6, 4, 14, ROUGH_PRISMARINE);
         }

         this.fillWithBlocks(☃, ☃, 0, 3, 0, 0, 3, 15, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 3, 0, 7, 3, 15, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 0, 7, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 15, 6, 3, 15, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 2, 15, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 2, 0, 7, 2, 15, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 0, 7, 2, 0, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 15, 6, 2, 15, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 1, 15, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 1, 0, 7, 1, 15, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 0, 7, 1, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 15, 6, 1, 15, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 1, 1, 1, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 1, 1, 6, 1, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 1, 1, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 3, 1, 6, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 13, 1, 1, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 1, 13, 6, 1, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 13, 1, 3, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 3, 13, 6, 3, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, 1, 6, 2, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 1, 6, 5, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, 1, 9, 2, 3, 9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 1, 9, 5, 3, 9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 2, 6, 4, 2, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 2, 9, 4, 2, 9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, 2, 7, 2, 2, 8, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 2, 7, 5, 2, 8, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.setBlockState(☃, SEA_LANTERN, 2, 2, 5, ☃);
         this.setBlockState(☃, SEA_LANTERN, 5, 2, 5, ☃);
         this.setBlockState(☃, SEA_LANTERN, 2, 2, 10, ☃);
         this.setBlockState(☃, SEA_LANTERN, 5, 2, 10, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 2, 3, 5, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 5, 3, 5, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 2, 3, 10, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 5, 3, 10, ☃);
         if (☃x.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 0, 4, 2, 0, false);
         }

         if (☃x.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 7, 1, 3, 7, 2, 4, false);
         }

         if (☃x.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 1, 3, 0, 2, 4, false);
         }

         if (☃.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 15, 4, 2, 15, false);
         }

         if (☃.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 1, 11, 0, 2, 12, false);
         }

         if (☃.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 7, 1, 11, 7, 2, 12, false);
         }

         return true;
      }
   }

   public static class EntryRoom extends StructureOceanMonumentPieces.Piece {
      public EntryRoom() {
      }

      public EntryRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2) {
         super(1, ☃, ☃, 1, 1, 1);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 3, 0, 2, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 3, 0, 7, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 1, 2, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 2, 0, 7, 2, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 1, 0, 7, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 7, 7, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 0, 2, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 1, 0, 6, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 7, 4, 2, 7, false);
         }

         if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 0, 1, 3, 1, 2, 4, false);
         }

         if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
            this.generateWaterBox(☃, ☃, 6, 1, 3, 7, 2, 4, false);
         }

         return true;
      }
   }

   static class FitSimpleRoomHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
      private FitSimpleRoomHelper() {
      }

      @Override
      public boolean fits(StructureOceanMonumentPieces.RoomDefinition var1) {
         return true;
      }

      @Override
      public StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         ☃.claimed = true;
         return new StructureOceanMonumentPieces.SimpleRoom(☃, ☃, ☃);
      }
   }

   static class FitSimpleRoomTopHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
      private FitSimpleRoomTopHelper() {
      }

      @Override
      public boolean fits(StructureOceanMonumentPieces.RoomDefinition var1) {
         return !☃.hasOpening[EnumFacing.WEST.getIndex()]
            && !☃.hasOpening[EnumFacing.EAST.getIndex()]
            && !☃.hasOpening[EnumFacing.NORTH.getIndex()]
            && !☃.hasOpening[EnumFacing.SOUTH.getIndex()]
            && !☃.hasOpening[EnumFacing.UP.getIndex()];
      }

      @Override
      public StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         ☃.claimed = true;
         return new StructureOceanMonumentPieces.SimpleTopRoom(☃, ☃, ☃);
      }
   }

   public static class MonumentBuilding extends StructureOceanMonumentPieces.Piece {
      private StructureOceanMonumentPieces.RoomDefinition sourceRoom;
      private StructureOceanMonumentPieces.RoomDefinition coreRoom;
      private final List<StructureOceanMonumentPieces.Piece> childPieces = Lists.newArrayList();

      public MonumentBuilding() {
      }

      public MonumentBuilding(Random var1, int var2, int var3, EnumFacing var4) {
         super(0);
         this.setCoordBaseMode(☃);
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃.getAxis() == EnumFacing.Axis.Z) {
            this.boundingBox = new StructureBoundingBox(☃, 39, ☃, ☃ + 58 - 1, 61, ☃ + 58 - 1);
         } else {
            this.boundingBox = new StructureBoundingBox(☃, 39, ☃, ☃ + 58 - 1, 61, ☃ + 58 - 1);
         }

         List<StructureOceanMonumentPieces.RoomDefinition> ☃x = this.generateRoomGraph(☃);
         this.sourceRoom.claimed = true;
         this.childPieces.add(new StructureOceanMonumentPieces.EntryRoom(☃, this.sourceRoom));
         this.childPieces.add(new StructureOceanMonumentPieces.MonumentCoreRoom(☃, this.coreRoom, ☃));
         List<StructureOceanMonumentPieces.MonumentRoomFitHelper> ☃xx = Lists.newArrayList();
         ☃xx.add(new StructureOceanMonumentPieces.XYDoubleRoomFitHelper());
         ☃xx.add(new StructureOceanMonumentPieces.YZDoubleRoomFitHelper());
         ☃xx.add(new StructureOceanMonumentPieces.ZDoubleRoomFitHelper());
         ☃xx.add(new StructureOceanMonumentPieces.XDoubleRoomFitHelper());
         ☃xx.add(new StructureOceanMonumentPieces.YDoubleRoomFitHelper());
         ☃xx.add(new StructureOceanMonumentPieces.FitSimpleRoomTopHelper());
         ☃xx.add(new StructureOceanMonumentPieces.FitSimpleRoomHelper());

         for (StructureOceanMonumentPieces.RoomDefinition ☃xxx : ☃x) {
            if (!☃xxx.claimed && !☃xxx.isSpecial()) {
               for (StructureOceanMonumentPieces.MonumentRoomFitHelper ☃xxxx : ☃xx) {
                  if (☃xxxx.fits(☃xxx)) {
                     this.childPieces.add(☃xxxx.create(☃, ☃xxx, ☃));
                     break;
                  }
               }
            }
         }

         int ☃xxxxx = this.boundingBox.minY;
         int ☃xxxxxx = this.getXWithOffset(9, 22);
         int ☃xxxxxxx = this.getZWithOffset(9, 22);

         for (StructureOceanMonumentPieces.Piece ☃xxxxxxxx : this.childPieces) {
            ☃xxxxxxxx.getBoundingBox().offset(☃xxxxxx, ☃xxxxx, ☃xxxxxxx);
         }

         StructureBoundingBox ☃xxxxxxxx = StructureBoundingBox.createProper(
            this.getXWithOffset(1, 1),
            this.getYWithOffset(1),
            this.getZWithOffset(1, 1),
            this.getXWithOffset(23, 21),
            this.getYWithOffset(8),
            this.getZWithOffset(23, 21)
         );
         StructureBoundingBox ☃xxxxxxxxx = StructureBoundingBox.createProper(
            this.getXWithOffset(34, 1),
            this.getYWithOffset(1),
            this.getZWithOffset(34, 1),
            this.getXWithOffset(56, 21),
            this.getYWithOffset(8),
            this.getZWithOffset(56, 21)
         );
         StructureBoundingBox ☃xxxxxxxxxx = StructureBoundingBox.createProper(
            this.getXWithOffset(22, 22),
            this.getYWithOffset(13),
            this.getZWithOffset(22, 22),
            this.getXWithOffset(35, 35),
            this.getYWithOffset(17),
            this.getZWithOffset(35, 35)
         );
         int ☃xxxxxxxxxxx = ☃.nextInt();
         this.childPieces.add(new StructureOceanMonumentPieces.WingRoom(☃, ☃xxxxxxxx, ☃xxxxxxxxxxx++));
         this.childPieces.add(new StructureOceanMonumentPieces.WingRoom(☃, ☃xxxxxxxxx, ☃xxxxxxxxxxx++));
         this.childPieces.add(new StructureOceanMonumentPieces.Penthouse(☃, ☃xxxxxxxxxx));
      }

      private List<StructureOceanMonumentPieces.RoomDefinition> generateRoomGraph(Random var1) {
         StructureOceanMonumentPieces.RoomDefinition[] ☃ = new StructureOceanMonumentPieces.RoomDefinition[75];

         for (int ☃x = 0; ☃x < 5; ☃x++) {
            for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
               int ☃xxx = 0;
               int ☃xxxx = getRoomIndex(☃x, 0, ☃xx);
               ☃[☃xxxx] = new StructureOceanMonumentPieces.RoomDefinition(☃xxxx);
            }
         }

         for (int ☃x = 0; ☃x < 5; ☃x++) {
            for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
               int ☃xxx = 1;
               int ☃xxxx = getRoomIndex(☃x, 1, ☃xx);
               ☃[☃xxxx] = new StructureOceanMonumentPieces.RoomDefinition(☃xxxx);
            }
         }

         for (int ☃x = 1; ☃x < 4; ☃x++) {
            for (int ☃xx = 0; ☃xx < 2; ☃xx++) {
               int ☃xxx = 2;
               int ☃xxxx = getRoomIndex(☃x, 2, ☃xx);
               ☃[☃xxxx] = new StructureOceanMonumentPieces.RoomDefinition(☃xxxx);
            }
         }

         this.sourceRoom = ☃[GRIDROOM_SOURCE_INDEX];

         for (int ☃x = 0; ☃x < 5; ☃x++) {
            for (int ☃xx = 0; ☃xx < 5; ☃xx++) {
               for (int ☃xxx = 0; ☃xxx < 3; ☃xxx++) {
                  int ☃xxxx = getRoomIndex(☃x, ☃xxx, ☃xx);
                  if (☃[☃xxxx] != null) {
                     for (EnumFacing ☃xxxxx : EnumFacing.values()) {
                        int ☃xxxxxx = ☃x + ☃xxxxx.getXOffset();
                        int ☃xxxxxxx = ☃xxx + ☃xxxxx.getYOffset();
                        int ☃xxxxxxxx = ☃xx + ☃xxxxx.getZOffset();
                        if (☃xxxxxx >= 0 && ☃xxxxxx < 5 && ☃xxxxxxxx >= 0 && ☃xxxxxxxx < 5 && ☃xxxxxxx >= 0 && ☃xxxxxxx < 3) {
                           int ☃xxxxxxxxx = getRoomIndex(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
                           if (☃[☃xxxxxxxxx] != null) {
                              if (☃xxxxxxxx == ☃xx) {
                                 ☃[☃xxxx].setConnection(☃xxxxx, ☃[☃xxxxxxxxx]);
                              } else {
                                 ☃[☃xxxx].setConnection(☃xxxxx.getOpposite(), ☃[☃xxxxxxxxx]);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         StructureOceanMonumentPieces.RoomDefinition ☃x = new StructureOceanMonumentPieces.RoomDefinition(1003);
         StructureOceanMonumentPieces.RoomDefinition ☃xx = new StructureOceanMonumentPieces.RoomDefinition(1001);
         StructureOceanMonumentPieces.RoomDefinition ☃xxxx = new StructureOceanMonumentPieces.RoomDefinition(1002);
         ☃[GRIDROOM_TOP_CONNECT_INDEX].setConnection(EnumFacing.UP, ☃x);
         ☃[GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(EnumFacing.SOUTH, ☃xx);
         ☃[GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(EnumFacing.SOUTH, ☃xxxx);
         ☃x.claimed = true;
         ☃xx.claimed = true;
         ☃xxxx.claimed = true;
         this.sourceRoom.isSource = true;
         this.coreRoom = ☃[getRoomIndex(☃.nextInt(4), 0, 2)];
         this.coreRoom.claimed = true;
         this.coreRoom.connections[EnumFacing.EAST.getIndex()].claimed = true;
         this.coreRoom.connections[EnumFacing.NORTH.getIndex()].claimed = true;
         this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.NORTH.getIndex()].claimed = true;
         this.coreRoom.connections[EnumFacing.UP.getIndex()].claimed = true;
         this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
         this.coreRoom.connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
         this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
         List<StructureOceanMonumentPieces.RoomDefinition> ☃xxxxxx = Lists.newArrayList();

         for (StructureOceanMonumentPieces.RoomDefinition ☃xxxxxxx : ☃) {
            if (☃xxxxxxx != null) {
               ☃xxxxxxx.updateOpenings();
               ☃xxxxxx.add(☃xxxxxxx);
            }
         }

         ☃x.updateOpenings();
         Collections.shuffle(☃xxxxxx, ☃);
         int ☃xxxxxxxx = 1;

         for (StructureOceanMonumentPieces.RoomDefinition ☃xxxxxxxxx : ☃xxxxxx) {
            int ☃xxxxxxxxxx = 0;
            int ☃xxxxxxxxxxx = 0;

            while (☃xxxxxxxxxx < 2 && ☃xxxxxxxxxxx < 5) {
               ☃xxxxxxxxxxx++;
               int ☃xxxxxxxxxxxx = ☃.nextInt(6);
               if (☃xxxxxxxxx.hasOpening[☃xxxxxxxxxxxx]) {
                  int ☃xxxxxxxxxxxxx = EnumFacing.byIndex(☃xxxxxxxxxxxx).getOpposite().getIndex();
                  ☃xxxxxxxxx.hasOpening[☃xxxxxxxxxxxx] = false;
                  ☃xxxxxxxxx.connections[☃xxxxxxxxxxxx].hasOpening[☃xxxxxxxxxxxxx] = false;
                  if (☃xxxxxxxxx.findSource(☃xxxxxxxx++) && ☃xxxxxxxxx.connections[☃xxxxxxxxxxxx].findSource(☃xxxxxxxx++)) {
                     ☃xxxxxxxxxx++;
                  } else {
                     ☃xxxxxxxxx.hasOpening[☃xxxxxxxxxxxx] = true;
                     ☃xxxxxxxxx.connections[☃xxxxxxxxxxxx].hasOpening[☃xxxxxxxxxxxxx] = true;
                  }
               }
            }
         }

         ☃xxxxxx.add(☃x);
         ☃xxxxxx.add(☃xx);
         ☃xxxxxx.add(☃xxxx);
         return ☃xxxxxx;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         int ☃ = Math.max(☃.getSeaLevel(), 64) - this.boundingBox.minY;
         this.generateWaterBox(☃, ☃, 0, 0, 0, 58, ☃, 58, false);
         this.generateWing(false, 0, ☃, ☃, ☃);
         this.generateWing(true, 33, ☃, ☃, ☃);
         this.generateEntranceArchs(☃, ☃, ☃);
         this.generateEntranceWall(☃, ☃, ☃);
         this.generateRoofPiece(☃, ☃, ☃);
         this.generateLowerWall(☃, ☃, ☃);
         this.generateMiddleWall(☃, ☃, ☃);
         this.generateUpperWall(☃, ☃, ☃);

         for (int ☃x = 0; ☃x < 7; ☃x++) {
            int ☃xx = 0;

            while (☃xx < 7) {
               if (☃xx == 0 && ☃x == 3) {
                  ☃xx = 6;
               }

               int ☃xxx = ☃x * 9;
               int ☃xxxx = ☃xx * 9;

               for (int ☃xxxxx = 0; ☃xxxxx < 4; ☃xxxxx++) {
                  for (int ☃xxxxxx = 0; ☃xxxxxx < 4; ☃xxxxxx++) {
                     this.setBlockState(☃, BRICKS_PRISMARINE, ☃xxx + ☃xxxxx, 0, ☃xxxx + ☃xxxxxx, ☃);
                     this.replaceAirAndLiquidDownwards(☃, BRICKS_PRISMARINE, ☃xxx + ☃xxxxx, -1, ☃xxxx + ☃xxxxxx, ☃);
                  }
               }

               if (☃x != 0 && ☃x != 6) {
                  ☃xx += 6;
               } else {
                  ☃xx++;
               }
            }
         }

         for (int ☃x = 0; ☃x < 5; ☃x++) {
            this.generateWaterBox(☃, ☃, -1 - ☃x, 0 + ☃x * 2, -1 - ☃x, -1 - ☃x, 23, 58 + ☃x, false);
            this.generateWaterBox(☃, ☃, 58 + ☃x, 0 + ☃x * 2, -1 - ☃x, 58 + ☃x, 23, 58 + ☃x, false);
            this.generateWaterBox(☃, ☃, 0 - ☃x, 0 + ☃x * 2, -1 - ☃x, 57 + ☃x, 23, -1 - ☃x, false);
            this.generateWaterBox(☃, ☃, 0 - ☃x, 0 + ☃x * 2, 58 + ☃x, 57 + ☃x, 23, 58 + ☃x, false);
         }

         for (StructureOceanMonumentPieces.Piece ☃x : this.childPieces) {
            if (☃x.getBoundingBox().intersectsWith(☃)) {
               ☃x.addComponentParts(☃, ☃, ☃);
            }
         }

         return true;
      }

      private void generateWing(boolean var1, int var2, World var3, Random var4, StructureBoundingBox var5) {
         int ☃ = 24;
         if (this.doesChunkIntersect(☃, ☃, 0, ☃ + 23, 20)) {
            this.fillWithBlocks(☃, ☃, ☃ + 0, 0, 0, ☃ + 24, 0, 20, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, ☃ + 0, 1, 0, ☃ + 24, 10, 20, false);

            for (int ☃x = 0; ☃x < 4; ☃x++) {
               this.fillWithBlocks(☃, ☃, ☃ + ☃x, ☃x + 1, ☃x, ☃ + ☃x, ☃x + 1, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃ + ☃x + 7, ☃x + 5, ☃x + 7, ☃ + ☃x + 7, ☃x + 5, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃ + 17 - ☃x, ☃x + 5, ☃x + 7, ☃ + 17 - ☃x, ☃x + 5, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃ + 24 - ☃x, ☃x + 1, ☃x, ☃ + 24 - ☃x, ☃x + 1, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃ + ☃x + 1, ☃x + 1, ☃x, ☃ + 23 - ☃x, ☃x + 1, ☃x, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, ☃ + ☃x + 8, ☃x + 5, ☃x + 7, ☃ + 16 - ☃x, ☃x + 5, ☃x + 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            this.fillWithBlocks(☃, ☃, ☃ + 4, 4, 4, ☃ + 6, 4, 20, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 7, 4, 4, ☃ + 17, 4, 6, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 18, 4, 4, ☃ + 20, 4, 20, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 11, 8, 11, ☃ + 13, 8, 20, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.setBlockState(☃, DOT_DECO_DATA, ☃ + 12, 9, 12, ☃);
            this.setBlockState(☃, DOT_DECO_DATA, ☃ + 12, 9, 15, ☃);
            this.setBlockState(☃, DOT_DECO_DATA, ☃ + 12, 9, 18, ☃);
            int ☃x = ☃ + (☃ ? 19 : 5);
            int ☃xx = ☃ + (☃ ? 5 : 19);

            for (int ☃xxx = 20; ☃xxx >= 5; ☃xxx -= 3) {
               this.setBlockState(☃, DOT_DECO_DATA, ☃x, 5, ☃xxx, ☃);
            }

            for (int ☃xxx = 19; ☃xxx >= 7; ☃xxx -= 3) {
               this.setBlockState(☃, DOT_DECO_DATA, ☃xx, 5, ☃xxx, ☃);
            }

            for (int ☃xxx = 0; ☃xxx < 4; ☃xxx++) {
               int ☃xxxx = ☃ ? ☃ + (24 - (17 - ☃xxx * 3)) : ☃ + 17 - ☃xxx * 3;
               this.setBlockState(☃, DOT_DECO_DATA, ☃xxxx, 5, 5, ☃);
            }

            this.setBlockState(☃, DOT_DECO_DATA, ☃xx, 5, 5, ☃);
            this.fillWithBlocks(☃, ☃, ☃ + 11, 1, 12, ☃ + 13, 7, 12, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 12, 1, 11, ☃ + 12, 7, 13, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         }
      }

      private void generateEntranceArchs(World var1, Random var2, StructureBoundingBox var3) {
         if (this.doesChunkIntersect(☃, 22, 5, 35, 17)) {
            this.generateWaterBox(☃, ☃, 25, 0, 0, 32, 8, 20, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, 24, 2, 5 + ☃ * 4, 24, 4, 5 + ☃ * 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 22, 4, 5 + ☃ * 4, 23, 4, 5 + ☃ * 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.setBlockState(☃, BRICKS_PRISMARINE, 25, 5, 5 + ☃ * 4, ☃);
               this.setBlockState(☃, BRICKS_PRISMARINE, 26, 6, 5 + ☃ * 4, ☃);
               this.setBlockState(☃, SEA_LANTERN, 26, 5, 5 + ☃ * 4, ☃);
               this.fillWithBlocks(☃, ☃, 33, 2, 5 + ☃ * 4, 33, 4, 5 + ☃ * 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 34, 4, 5 + ☃ * 4, 35, 4, 5 + ☃ * 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.setBlockState(☃, BRICKS_PRISMARINE, 32, 5, 5 + ☃ * 4, ☃);
               this.setBlockState(☃, BRICKS_PRISMARINE, 31, 6, 5 + ☃ * 4, ☃);
               this.setBlockState(☃, SEA_LANTERN, 31, 5, 5 + ☃ * 4, ☃);
               this.fillWithBlocks(☃, ☃, 27, 6, 5 + ☃ * 4, 30, 6, 5 + ☃ * 4, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            }
         }
      }

      private void generateEntranceWall(World var1, Random var2, StructureBoundingBox var3) {
         if (this.doesChunkIntersect(☃, 15, 20, 42, 21)) {
            this.fillWithBlocks(☃, ☃, 15, 0, 21, 42, 0, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 26, 1, 21, 31, 3, 21, false);
            this.fillWithBlocks(☃, ☃, 21, 12, 21, 36, 12, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 17, 11, 21, 40, 11, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 16, 10, 21, 41, 10, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 15, 7, 21, 42, 9, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 16, 6, 21, 41, 6, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 17, 5, 21, 40, 5, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 21, 4, 21, 36, 4, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 22, 3, 21, 26, 3, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 31, 3, 21, 35, 3, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 23, 2, 21, 25, 2, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 32, 2, 21, 34, 2, 21, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 28, 4, 20, 29, 4, 21, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.setBlockState(☃, BRICKS_PRISMARINE, 27, 3, 21, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 30, 3, 21, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 26, 2, 21, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 31, 2, 21, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 25, 1, 21, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 32, 1, 21, ☃);

            for (int ☃ = 0; ☃ < 7; ☃++) {
               this.setBlockState(☃, DARK_PRISMARINE, 28 - ☃, 6 + ☃, 21, ☃);
               this.setBlockState(☃, DARK_PRISMARINE, 29 + ☃, 6 + ☃, 21, ☃);
            }

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.setBlockState(☃, DARK_PRISMARINE, 28 - ☃, 9 + ☃, 21, ☃);
               this.setBlockState(☃, DARK_PRISMARINE, 29 + ☃, 9 + ☃, 21, ☃);
            }

            this.setBlockState(☃, DARK_PRISMARINE, 28, 12, 21, ☃);
            this.setBlockState(☃, DARK_PRISMARINE, 29, 12, 21, ☃);

            for (int ☃ = 0; ☃ < 3; ☃++) {
               this.setBlockState(☃, DARK_PRISMARINE, 22 - ☃ * 2, 8, 21, ☃);
               this.setBlockState(☃, DARK_PRISMARINE, 22 - ☃ * 2, 9, 21, ☃);
               this.setBlockState(☃, DARK_PRISMARINE, 35 + ☃ * 2, 8, 21, ☃);
               this.setBlockState(☃, DARK_PRISMARINE, 35 + ☃ * 2, 9, 21, ☃);
            }

            this.generateWaterBox(☃, ☃, 15, 13, 21, 42, 15, 21, false);
            this.generateWaterBox(☃, ☃, 15, 1, 21, 15, 6, 21, false);
            this.generateWaterBox(☃, ☃, 16, 1, 21, 16, 5, 21, false);
            this.generateWaterBox(☃, ☃, 17, 1, 21, 20, 4, 21, false);
            this.generateWaterBox(☃, ☃, 21, 1, 21, 21, 3, 21, false);
            this.generateWaterBox(☃, ☃, 22, 1, 21, 22, 2, 21, false);
            this.generateWaterBox(☃, ☃, 23, 1, 21, 24, 1, 21, false);
            this.generateWaterBox(☃, ☃, 42, 1, 21, 42, 6, 21, false);
            this.generateWaterBox(☃, ☃, 41, 1, 21, 41, 5, 21, false);
            this.generateWaterBox(☃, ☃, 37, 1, 21, 40, 4, 21, false);
            this.generateWaterBox(☃, ☃, 36, 1, 21, 36, 3, 21, false);
            this.generateWaterBox(☃, ☃, 33, 1, 21, 34, 1, 21, false);
            this.generateWaterBox(☃, ☃, 35, 1, 21, 35, 2, 21, false);
         }
      }

      private void generateRoofPiece(World var1, Random var2, StructureBoundingBox var3) {
         if (this.doesChunkIntersect(☃, 21, 21, 36, 36)) {
            this.fillWithBlocks(☃, ☃, 21, 0, 22, 36, 0, 36, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 21, 1, 22, 36, 23, 36, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, 21 + ☃, 13 + ☃, 21 + ☃, 36 - ☃, 13 + ☃, 21 + ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 21 + ☃, 13 + ☃, 36 - ☃, 36 - ☃, 13 + ☃, 36 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 21 + ☃, 13 + ☃, 22 + ☃, 21 + ☃, 13 + ☃, 35 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 36 - ☃, 13 + ☃, 22 + ☃, 36 - ☃, 13 + ☃, 35 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            this.fillWithBlocks(☃, ☃, 25, 16, 25, 32, 16, 32, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 25, 17, 25, 25, 19, 25, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 32, 17, 25, 32, 19, 25, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 25, 17, 32, 25, 19, 32, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 32, 17, 32, 32, 19, 32, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.setBlockState(☃, BRICKS_PRISMARINE, 26, 20, 26, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 27, 21, 27, ☃);
            this.setBlockState(☃, SEA_LANTERN, 27, 20, 27, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 26, 20, 31, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 27, 21, 30, ☃);
            this.setBlockState(☃, SEA_LANTERN, 27, 20, 30, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 31, 20, 31, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 30, 21, 30, ☃);
            this.setBlockState(☃, SEA_LANTERN, 30, 20, 30, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 31, 20, 26, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 30, 21, 27, ☃);
            this.setBlockState(☃, SEA_LANTERN, 30, 20, 27, ☃);
            this.fillWithBlocks(☃, ☃, 28, 21, 27, 29, 21, 27, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 27, 21, 28, 27, 21, 29, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 28, 21, 30, 29, 21, 30, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 30, 21, 28, 30, 21, 29, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         }
      }

      private void generateLowerWall(World var1, Random var2, StructureBoundingBox var3) {
         if (this.doesChunkIntersect(☃, 0, 21, 6, 58)) {
            this.fillWithBlocks(☃, ☃, 0, 0, 21, 6, 0, 57, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 0, 1, 21, 6, 7, 57, false);
            this.fillWithBlocks(☃, ☃, 4, 4, 21, 6, 4, 53, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, ☃, ☃ + 1, 21, ☃, ☃ + 1, 57 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            for (int ☃ = 23; ☃ < 53; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, 5, 5, ☃, ☃);
            }

            this.setBlockState(☃, DOT_DECO_DATA, 5, 5, 52, ☃);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, ☃, ☃ + 1, 21, ☃, ☃ + 1, 57 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            this.fillWithBlocks(☃, ☃, 4, 1, 52, 6, 3, 52, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 5, 1, 51, 5, 3, 53, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         }

         if (this.doesChunkIntersect(☃, 51, 21, 58, 58)) {
            this.fillWithBlocks(☃, ☃, 51, 0, 21, 57, 0, 57, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 51, 1, 21, 57, 7, 57, false);
            this.fillWithBlocks(☃, ☃, 51, 4, 21, 53, 4, 53, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, 57 - ☃, ☃ + 1, 21, 57 - ☃, ☃ + 1, 57 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            for (int ☃ = 23; ☃ < 53; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, 52, 5, ☃, ☃);
            }

            this.setBlockState(☃, DOT_DECO_DATA, 52, 5, 52, ☃);
            this.fillWithBlocks(☃, ☃, 51, 1, 52, 53, 3, 52, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 52, 1, 51, 52, 3, 53, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         }

         if (this.doesChunkIntersect(☃, 0, 51, 57, 57)) {
            this.fillWithBlocks(☃, ☃, 7, 0, 51, 50, 0, 57, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 7, 1, 51, 50, 10, 57, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, ☃ + 1, ☃ + 1, 57 - ☃, 56 - ☃, ☃ + 1, 57 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }
         }
      }

      private void generateMiddleWall(World var1, Random var2, StructureBoundingBox var3) {
         if (this.doesChunkIntersect(☃, 7, 21, 13, 50)) {
            this.fillWithBlocks(☃, ☃, 7, 0, 21, 13, 0, 50, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 7, 1, 21, 13, 10, 50, false);
            this.fillWithBlocks(☃, ☃, 11, 8, 21, 13, 8, 53, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, ☃ + 7, ☃ + 5, 21, ☃ + 7, ☃ + 5, 54, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            for (int ☃ = 21; ☃ <= 45; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, 12, 9, ☃, ☃);
            }
         }

         if (this.doesChunkIntersect(☃, 44, 21, 50, 54)) {
            this.fillWithBlocks(☃, ☃, 44, 0, 21, 50, 0, 50, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 44, 1, 21, 50, 10, 50, false);
            this.fillWithBlocks(☃, ☃, 44, 8, 21, 46, 8, 53, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, 50 - ☃, ☃ + 5, 21, 50 - ☃, ☃ + 5, 54, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            for (int ☃ = 21; ☃ <= 45; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, 45, 9, ☃, ☃);
            }
         }

         if (this.doesChunkIntersect(☃, 8, 44, 49, 54)) {
            this.fillWithBlocks(☃, ☃, 14, 0, 44, 43, 0, 50, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 14, 1, 44, 43, 10, 50, false);

            for (int ☃ = 12; ☃ <= 45; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, ☃, 9, 45, ☃);
               this.setBlockState(☃, DOT_DECO_DATA, ☃, 9, 52, ☃);
               if (☃ == 12 || ☃ == 18 || ☃ == 24 || ☃ == 33 || ☃ == 39 || ☃ == 45) {
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 9, 47, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 9, 50, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 10, 45, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 10, 46, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 10, 51, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 10, 52, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 11, 47, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 11, 50, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 12, 48, ☃);
                  this.setBlockState(☃, DOT_DECO_DATA, ☃, 12, 49, ☃);
               }
            }

            for (int ☃x = 0; ☃x < 3; ☃x++) {
               this.fillWithBlocks(☃, ☃, 8 + ☃x, 5 + ☃x, 54, 49 - ☃x, 5 + ☃x, 54, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            }

            this.fillWithBlocks(☃, ☃, 11, 8, 54, 46, 8, 54, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 14, 8, 44, 43, 8, 53, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         }
      }

      private void generateUpperWall(World var1, Random var2, StructureBoundingBox var3) {
         if (this.doesChunkIntersect(☃, 14, 21, 20, 43)) {
            this.fillWithBlocks(☃, ☃, 14, 0, 21, 20, 0, 43, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 14, 1, 22, 20, 14, 43, false);
            this.fillWithBlocks(☃, ☃, 18, 12, 22, 20, 12, 39, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 18, 12, 21, 20, 12, 21, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, ☃ + 14, ☃ + 9, 21, ☃ + 14, ☃ + 9, 43 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            for (int ☃ = 23; ☃ <= 39; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, 19, 13, ☃, ☃);
            }
         }

         if (this.doesChunkIntersect(☃, 37, 21, 43, 43)) {
            this.fillWithBlocks(☃, ☃, 37, 0, 21, 43, 0, 43, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 37, 1, 22, 43, 14, 43, false);
            this.fillWithBlocks(☃, ☃, 37, 12, 22, 39, 12, 39, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 37, 12, 21, 39, 12, 21, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, 43 - ☃, ☃ + 9, 21, 43 - ☃, ☃ + 9, 43 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            for (int ☃ = 23; ☃ <= 39; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, 38, 13, ☃, ☃);
            }
         }

         if (this.doesChunkIntersect(☃, 15, 37, 42, 43)) {
            this.fillWithBlocks(☃, ☃, 21, 0, 37, 36, 0, 43, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.generateWaterBox(☃, ☃, 21, 1, 37, 36, 14, 43, false);
            this.fillWithBlocks(☃, ☃, 21, 12, 37, 36, 12, 39, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);

            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, 15 + ☃, ☃ + 9, 43 - ☃, 42 - ☃, ☃ + 9, 43 - ☃, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            for (int ☃ = 21; ☃ <= 36; ☃ += 3) {
               this.setBlockState(☃, DOT_DECO_DATA, ☃, 13, 38, ☃);
            }
         }
      }
   }

   public static class MonumentCoreRoom extends StructureOceanMonumentPieces.Piece {
      public MonumentCoreRoom() {
      }

      public MonumentCoreRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 2, 2, 2);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.generateBoxOnFillOnly(☃, ☃, 1, 8, 0, 14, 8, 14, ROUGH_PRISMARINE);
         int ☃ = 7;
         IBlockState ☃x = BRICKS_PRISMARINE;
         this.fillWithBlocks(☃, ☃, 0, 7, 0, 0, 7, 15, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 15, 7, 0, 15, 7, 15, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 1, 7, 0, 15, 7, 0, ☃x, ☃x, false);
         this.fillWithBlocks(☃, ☃, 1, 7, 15, 14, 7, 15, ☃x, ☃x, false);

         for (int ☃xx = 1; ☃xx <= 6; ☃xx++) {
            ☃x = BRICKS_PRISMARINE;
            if (☃xx == 2 || ☃xx == 6) {
               ☃x = ROUGH_PRISMARINE;
            }

            for (int ☃xxx = 0; ☃xxx <= 15; ☃xxx += 15) {
               this.fillWithBlocks(☃, ☃, ☃xxx, ☃xx, 0, ☃xxx, ☃xx, 1, ☃x, ☃x, false);
               this.fillWithBlocks(☃, ☃, ☃xxx, ☃xx, 6, ☃xxx, ☃xx, 9, ☃x, ☃x, false);
               this.fillWithBlocks(☃, ☃, ☃xxx, ☃xx, 14, ☃xxx, ☃xx, 15, ☃x, ☃x, false);
            }

            this.fillWithBlocks(☃, ☃, 1, ☃xx, 0, 1, ☃xx, 0, ☃x, ☃x, false);
            this.fillWithBlocks(☃, ☃, 6, ☃xx, 0, 9, ☃xx, 0, ☃x, ☃x, false);
            this.fillWithBlocks(☃, ☃, 14, ☃xx, 0, 14, ☃xx, 0, ☃x, ☃x, false);
            this.fillWithBlocks(☃, ☃, 1, ☃xx, 15, 14, ☃xx, 15, ☃x, ☃x, false);
         }

         this.fillWithBlocks(☃, ☃, 6, 3, 6, 9, 6, 9, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.GOLD_BLOCK.getDefaultState(), false);

         for (int ☃xx = 3; ☃xx <= 6; ☃xx += 3) {
            for (int ☃xxx = 6; ☃xxx <= 9; ☃xxx += 3) {
               this.setBlockState(☃, SEA_LANTERN, ☃xxx, ☃xx, 6, ☃);
               this.setBlockState(☃, SEA_LANTERN, ☃xxx, ☃xx, 9, ☃);
            }
         }

         this.fillWithBlocks(☃, ☃, 5, 1, 6, 5, 2, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 1, 9, 5, 2, 9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 1, 6, 10, 2, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 1, 9, 10, 2, 9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 1, 5, 6, 2, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 9, 1, 5, 9, 2, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 1, 10, 6, 2, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 9, 1, 10, 9, 2, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 2, 5, 5, 6, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 2, 10, 5, 6, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 2, 5, 10, 6, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 2, 10, 10, 6, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 7, 1, 5, 7, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 7, 1, 10, 7, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 5, 7, 9, 5, 7, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 7, 9, 10, 7, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 7, 5, 6, 7, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 7, 10, 6, 7, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 9, 7, 5, 14, 7, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 9, 7, 10, 14, 7, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, 1, 2, 2, 1, 3, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 2, 3, 1, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 13, 1, 2, 13, 1, 3, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 12, 1, 2, 12, 1, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, 1, 12, 2, 1, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 13, 3, 1, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 13, 1, 12, 13, 1, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 12, 1, 13, 12, 1, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         return true;
      }
   }

   interface MonumentRoomFitHelper {
      boolean fits(StructureOceanMonumentPieces.RoomDefinition var1);

      StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3);
   }

   public static class Penthouse extends StructureOceanMonumentPieces.Piece {
      public Penthouse() {
      }

      public Penthouse(EnumFacing var1, StructureBoundingBox var2) {
         super(☃, ☃);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 2, -1, 2, 11, -1, 11, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, -1, 0, 1, -1, 11, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 12, -1, 0, 13, -1, 11, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, -1, 0, 11, -1, 1, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 2, -1, 12, 11, -1, 13, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 0, 0, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 13, 0, 0, 13, 0, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 0, 12, 0, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 0, 13, 12, 0, 13, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);

         for (int ☃ = 2; ☃ <= 11; ☃ += 3) {
            this.setBlockState(☃, SEA_LANTERN, 0, 0, ☃, ☃);
            this.setBlockState(☃, SEA_LANTERN, 13, 0, ☃, ☃);
            this.setBlockState(☃, SEA_LANTERN, ☃, 0, 0, ☃);
         }

         this.fillWithBlocks(☃, ☃, 2, 0, 3, 4, 0, 9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 9, 0, 3, 11, 0, 9, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 4, 0, 9, 9, 0, 11, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.setBlockState(☃, BRICKS_PRISMARINE, 5, 0, 8, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 8, 0, 8, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 10, 0, 10, ☃);
         this.setBlockState(☃, BRICKS_PRISMARINE, 3, 0, 10, ☃);
         this.fillWithBlocks(☃, ☃, 3, 0, 3, 3, 0, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 10, 0, 3, 10, 0, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, 0, 10, 7, 0, 10, DARK_PRISMARINE, DARK_PRISMARINE, false);
         int ☃ = 3;

         for (int ☃x = 0; ☃x < 2; ☃x++) {
            for (int ☃xx = 2; ☃xx <= 8; ☃xx += 3) {
               this.fillWithBlocks(☃, ☃, ☃, 0, ☃xx, ☃, 2, ☃xx, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            ☃ = 10;
         }

         this.fillWithBlocks(☃, ☃, 5, 0, 10, 5, 2, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 8, 0, 10, 8, 2, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 6, -1, 7, 7, -1, 8, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.generateWaterBox(☃, ☃, 6, -1, 3, 7, -1, 4, false);
         this.spawnElder(☃, ☃, 6, 1, 6);
         return true;
      }
   }

   public abstract static class Piece extends StructureComponent {
      protected static final IBlockState ROUGH_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.ROUGH_META);
      protected static final IBlockState BRICKS_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.BRICKS_META);
      protected static final IBlockState DARK_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.DARK_META);
      protected static final IBlockState DOT_DECO_DATA = BRICKS_PRISMARINE;
      protected static final IBlockState SEA_LANTERN = Blocks.SEA_LANTERN.getDefaultState();
      protected static final IBlockState WATER = Blocks.WATER.getDefaultState();
      protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
      protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
      protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
      protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
      protected StructureOceanMonumentPieces.RoomDefinition roomDefinition;

      protected static final int getRoomIndex(int var0, int var1, int var2) {
         return ☃ * 25 + ☃ * 5 + ☃;
      }

      public Piece() {
         super(0);
      }

      public Piece(int var1) {
         super(☃);
      }

      public Piece(EnumFacing var1, StructureBoundingBox var2) {
         super(1);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      protected Piece(int var1, EnumFacing var2, StructureOceanMonumentPieces.RoomDefinition var3, int var4, int var5, int var6) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.roomDefinition = ☃;
         int ☃ = ☃.index;
         int ☃x = ☃ % 5;
         int ☃xx = ☃ / 5 % 5;
         int ☃xxx = ☃ / 25;
         if (☃ != EnumFacing.NORTH && ☃ != EnumFacing.SOUTH) {
            this.boundingBox = new StructureBoundingBox(0, 0, 0, ☃ * 8 - 1, ☃ * 4 - 1, ☃ * 8 - 1);
         } else {
            this.boundingBox = new StructureBoundingBox(0, 0, 0, ☃ * 8 - 1, ☃ * 4 - 1, ☃ * 8 - 1);
         }

         switch (☃) {
            case NORTH:
               this.boundingBox.offset(☃x * 8, ☃xxx * 4, -(☃xx + ☃) * 8 + 1);
               break;
            case SOUTH:
               this.boundingBox.offset(☃x * 8, ☃xxx * 4, ☃xx * 8);
               break;
            case WEST:
               this.boundingBox.offset(-(☃xx + ☃) * 8 + 1, ☃xxx * 4, ☃x * 8);
               break;
            default:
               this.boundingBox.offset(☃xx * 8, ☃xxx * 4, ☃x * 8);
         }
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
      }

      protected void generateWaterBox(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
         for (int ☃ = ☃; ☃ <= ☃; ☃++) {
            for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
               for (int ☃xx = ☃; ☃xx <= ☃; ☃xx++) {
                  if (!☃ || this.getBlockStateFromPos(☃, ☃x, ☃, ☃xx, ☃).getMaterial() != Material.AIR) {
                     if (this.getYWithOffset(☃) >= ☃.getSeaLevel()) {
                        this.setBlockState(☃, Blocks.AIR.getDefaultState(), ☃x, ☃, ☃xx, ☃);
                     } else {
                        this.setBlockState(☃, WATER, ☃x, ☃, ☃xx, ☃);
                     }
                  }
               }
            }
         }
      }

      protected void generateDefaultFloor(World var1, StructureBoundingBox var2, int var3, int var4, boolean var5) {
         if (☃) {
            this.fillWithBlocks(☃, ☃, ☃ + 0, 0, ☃ + 0, ☃ + 2, 0, ☃ + 8 - 1, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 5, 0, ☃ + 0, ☃ + 8 - 1, 0, ☃ + 8 - 1, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 3, 0, ☃ + 0, ☃ + 4, 0, ☃ + 2, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 3, 0, ☃ + 5, ☃ + 4, 0, ☃ + 8 - 1, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 3, 0, ☃ + 2, ☃ + 4, 0, ☃ + 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 3, 0, ☃ + 5, ☃ + 4, 0, ☃ + 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 2, 0, ☃ + 3, ☃ + 2, 0, ☃ + 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, ☃ + 5, 0, ☃ + 3, ☃ + 5, 0, ☃ + 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         } else {
            this.fillWithBlocks(☃, ☃, ☃ + 0, 0, ☃ + 0, ☃ + 8 - 1, 0, ☃ + 8 - 1, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
         }
      }

      protected void generateBoxOnFillOnly(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockState var9) {
         for (int ☃ = ☃; ☃ <= ☃; ☃++) {
            for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
               for (int ☃xx = ☃; ☃xx <= ☃; ☃xx++) {
                  if (this.getBlockStateFromPos(☃, ☃x, ☃, ☃xx, ☃) == WATER) {
                     this.setBlockState(☃, ☃, ☃x, ☃, ☃xx, ☃);
                  }
               }
            }
         }
      }

      protected boolean doesChunkIntersect(StructureBoundingBox var1, int var2, int var3, int var4, int var5) {
         int ☃ = this.getXWithOffset(☃, ☃);
         int ☃x = this.getZWithOffset(☃, ☃);
         int ☃xx = this.getXWithOffset(☃, ☃);
         int ☃xxx = this.getZWithOffset(☃, ☃);
         return ☃.intersectsWith(Math.min(☃, ☃xx), Math.min(☃x, ☃xxx), Math.max(☃, ☃xx), Math.max(☃x, ☃xxx));
      }

      protected boolean spawnElder(World var1, StructureBoundingBox var2, int var3, int var4, int var5) {
         int ☃ = this.getXWithOffset(☃, ☃);
         int ☃x = this.getYWithOffset(☃);
         int ☃xx = this.getZWithOffset(☃, ☃);
         if (☃.isVecInside(new BlockPos(☃, ☃x, ☃xx))) {
            EntityElderGuardian ☃xxx = new EntityElderGuardian(☃);
            ☃xxx.heal(☃xxx.getMaxHealth());
            ☃xxx.setLocationAndAngles(☃ + 0.5, ☃x, ☃xx + 0.5, 0.0F, 0.0F);
            ☃xxx.onInitialSpawn(☃.getDifficultyForLocation(new BlockPos(☃xxx)), null);
            ☃.spawnEntity(☃xxx);
            return true;
         } else {
            return false;
         }
      }
   }

   static class RoomDefinition {
      int index;
      StructureOceanMonumentPieces.RoomDefinition[] connections = new StructureOceanMonumentPieces.RoomDefinition[6];
      boolean[] hasOpening = new boolean[6];
      boolean claimed;
      boolean isSource;
      int scanIndex;

      public RoomDefinition(int var1) {
         this.index = ☃;
      }

      public void setConnection(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2) {
         this.connections[☃.getIndex()] = ☃;
         ☃.connections[☃.getOpposite().getIndex()] = this;
      }

      public void updateOpenings() {
         for (int ☃ = 0; ☃ < 6; ☃++) {
            this.hasOpening[☃] = this.connections[☃] != null;
         }
      }

      public boolean findSource(int var1) {
         if (this.isSource) {
            return true;
         } else {
            this.scanIndex = ☃;

            for (int ☃ = 0; ☃ < 6; ☃++) {
               if (this.connections[☃] != null && this.hasOpening[☃] && this.connections[☃].scanIndex != ☃ && this.connections[☃].findSource(☃)) {
                  return true;
               }
            }

            return false;
         }
      }

      public boolean isSpecial() {
         return this.index >= 75;
      }

      public int countOpenings() {
         int ☃ = 0;

         for (int ☃x = 0; ☃x < 6; ☃x++) {
            if (this.hasOpening[☃x]) {
               ☃++;
            }
         }

         return ☃;
      }
   }

   public static class SimpleRoom extends StructureOceanMonumentPieces.Piece {
      private int mainDesign;

      public SimpleRoom() {
      }

      public SimpleRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 1, 1, 1);
         this.mainDesign = ☃.nextInt(3);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.roomDefinition.index / 25 > 0) {
            this.generateDefaultFloor(☃, ☃, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
         }

         if (this.roomDefinition.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 4, 1, 6, 4, 6, ROUGH_PRISMARINE);
         }

         boolean ☃ = this.mainDesign != 0
            && ☃.nextBoolean()
            && !this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]
            && !this.roomDefinition.hasOpening[EnumFacing.UP.getIndex()]
            && this.roomDefinition.countOpenings() > 1;
         if (this.mainDesign == 0) {
            this.fillWithBlocks(☃, ☃, 0, 1, 0, 2, 1, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 3, 0, 2, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 2, 2, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 2, 0, 2, 2, 0, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.setBlockState(☃, SEA_LANTERN, 1, 2, 1, ☃);
            this.fillWithBlocks(☃, ☃, 5, 1, 0, 7, 1, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 5, 3, 0, 7, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 2, 0, 7, 2, 2, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 5, 2, 0, 6, 2, 0, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.setBlockState(☃, SEA_LANTERN, 6, 2, 1, ☃);
            this.fillWithBlocks(☃, ☃, 0, 1, 5, 2, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 3, 5, 2, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 2, 5, 0, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 2, 7, 2, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.setBlockState(☃, SEA_LANTERN, 1, 2, 6, ☃);
            this.fillWithBlocks(☃, ☃, 5, 1, 5, 7, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 5, 3, 5, 7, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 2, 5, 7, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 5, 2, 7, 6, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.setBlockState(☃, SEA_LANTERN, 6, 2, 6, ☃);
            if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 3, 3, 0, 4, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, 3, 3, 0, 4, 3, 1, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 3, 2, 0, 4, 2, 0, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 3, 1, 0, 4, 1, 1, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 3, 3, 7, 4, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, 3, 3, 6, 4, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 3, 2, 7, 4, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 3, 1, 6, 4, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 0, 3, 3, 0, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, 0, 3, 3, 1, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 0, 2, 3, 0, 2, 4, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 0, 1, 3, 1, 1, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 7, 3, 3, 7, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            } else {
               this.fillWithBlocks(☃, ☃, 6, 3, 3, 7, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 7, 2, 3, 7, 2, 4, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 6, 1, 3, 7, 1, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }
         } else if (this.mainDesign == 1) {
            this.fillWithBlocks(☃, ☃, 2, 1, 2, 2, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 2, 1, 5, 2, 3, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 5, 1, 5, 5, 3, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 5, 1, 2, 5, 3, 2, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.setBlockState(☃, SEA_LANTERN, 2, 2, 2, ☃);
            this.setBlockState(☃, SEA_LANTERN, 2, 2, 5, ☃);
            this.setBlockState(☃, SEA_LANTERN, 5, 2, 5, ☃);
            this.setBlockState(☃, SEA_LANTERN, 5, 2, 2, ☃);
            this.fillWithBlocks(☃, ☃, 0, 1, 0, 1, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 3, 1, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 1, 7, 1, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 1, 6, 0, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 6, 1, 7, 7, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 1, 6, 7, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 6, 1, 0, 7, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 1, 1, 7, 3, 1, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.setBlockState(☃, ROUGH_PRISMARINE, 1, 2, 0, ☃);
            this.setBlockState(☃, ROUGH_PRISMARINE, 0, 2, 1, ☃);
            this.setBlockState(☃, ROUGH_PRISMARINE, 1, 2, 7, ☃);
            this.setBlockState(☃, ROUGH_PRISMARINE, 0, 2, 6, ☃);
            this.setBlockState(☃, ROUGH_PRISMARINE, 6, 2, 7, ☃);
            this.setBlockState(☃, ROUGH_PRISMARINE, 7, 2, 6, ☃);
            this.setBlockState(☃, ROUGH_PRISMARINE, 6, 2, 0, ☃);
            this.setBlockState(☃, ROUGH_PRISMARINE, 7, 2, 1, ☃);
            if (!this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 1, 3, 0, 6, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 1, 2, 0, 6, 2, 0, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 1, 1, 0, 6, 1, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            if (!this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 1, 3, 7, 6, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 1, 2, 7, 6, 2, 7, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 1, 1, 7, 6, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            if (!this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 0, 3, 1, 0, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 0, 2, 1, 0, 2, 6, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 1, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            if (!this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
               this.fillWithBlocks(☃, ☃, 7, 3, 1, 7, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 7, 2, 1, 7, 2, 6, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, 7, 1, 1, 7, 1, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }
         } else if (this.mainDesign == 2) {
            this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 1, 0, 7, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 1, 0, 6, 1, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 1, 7, 6, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 2, 0, 7, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 2, 0, 6, 2, 0, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 2, 7, 6, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 3, 0, 0, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 3, 0, 7, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 3, 0, 6, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 1, 3, 7, 6, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 0, 1, 3, 0, 2, 4, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 1, 3, 7, 2, 4, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 3, 1, 0, 4, 2, 0, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 3, 1, 7, 4, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
            if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
               this.generateWaterBox(☃, ☃, 3, 1, 0, 4, 2, 0, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
               this.generateWaterBox(☃, ☃, 3, 1, 7, 4, 2, 7, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
               this.generateWaterBox(☃, ☃, 0, 1, 3, 0, 2, 4, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
               this.generateWaterBox(☃, ☃, 7, 1, 3, 7, 2, 4, false);
            }
         }

         if (☃) {
            this.fillWithBlocks(☃, ☃, 3, 1, 3, 4, 1, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 3, 2, 3, 4, 2, 4, ROUGH_PRISMARINE, ROUGH_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 3, 3, 3, 4, 3, 4, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         }

         return true;
      }
   }

   public static class SimpleTopRoom extends StructureOceanMonumentPieces.Piece {
      public SimpleTopRoom() {
      }

      public SimpleTopRoom(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         super(1, ☃, ☃, 1, 1, 1);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.roomDefinition.index / 25 > 0) {
            this.generateDefaultFloor(☃, ☃, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
         }

         if (this.roomDefinition.connections[EnumFacing.UP.getIndex()] == null) {
            this.generateBoxOnFillOnly(☃, ☃, 1, 4, 1, 6, 4, 6, ROUGH_PRISMARINE);
         }

         for (int ☃ = 1; ☃ <= 6; ☃++) {
            for (int ☃x = 1; ☃x <= 6; ☃x++) {
               if (☃.nextInt(3) != 0) {
                  int ☃xx = 2 + (☃.nextInt(4) == 0 ? 0 : 1);
                  this.fillWithBlocks(☃, ☃, ☃, ☃xx, ☃x, ☃, 3, ☃x, Blocks.SPONGE.getStateFromMeta(1), Blocks.SPONGE.getStateFromMeta(1), false);
               }
            }
         }

         this.fillWithBlocks(☃, ☃, 0, 1, 0, 0, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 1, 0, 7, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 0, 6, 1, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 1, 7, 6, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 2, 0, 7, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 0, 6, 2, 0, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 2, 7, 6, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 3, 0, 0, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 3, 0, 7, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 0, 6, 3, 0, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 1, 3, 7, 6, 3, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 0, 1, 3, 0, 2, 4, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 7, 1, 3, 7, 2, 4, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 0, 4, 2, 0, DARK_PRISMARINE, DARK_PRISMARINE, false);
         this.fillWithBlocks(☃, ☃, 3, 1, 7, 4, 2, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
         if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
            this.generateWaterBox(☃, ☃, 3, 1, 0, 4, 2, 0, false);
         }

         return true;
      }
   }

   public static class WingRoom extends StructureOceanMonumentPieces.Piece {
      private int mainDesign;

      public WingRoom() {
      }

      public WingRoom(EnumFacing var1, StructureBoundingBox var2, int var3) {
         super(☃, ☃);
         this.mainDesign = ☃ & 1;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.mainDesign == 0) {
            for (int ☃ = 0; ☃ < 4; ☃++) {
               this.fillWithBlocks(☃, ☃, 10 - ☃, 3 - ☃, 20 - ☃, 12 + ☃, 3 - ☃, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            }

            this.fillWithBlocks(☃, ☃, 7, 0, 6, 15, 0, 16, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 6, 0, 6, 6, 3, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 16, 0, 6, 16, 3, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 1, 7, 7, 1, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 15, 1, 7, 15, 1, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 7, 1, 6, 9, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 13, 1, 6, 15, 3, 6, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 8, 1, 7, 9, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 13, 1, 7, 14, 1, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 9, 0, 5, 13, 0, 5, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 10, 0, 7, 12, 0, 7, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 8, 0, 10, 8, 0, 12, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 14, 0, 10, 14, 0, 12, DARK_PRISMARINE, DARK_PRISMARINE, false);

            for (int ☃ = 18; ☃ >= 7; ☃ -= 3) {
               this.setBlockState(☃, SEA_LANTERN, 6, 3, ☃, ☃);
               this.setBlockState(☃, SEA_LANTERN, 16, 3, ☃, ☃);
            }

            this.setBlockState(☃, SEA_LANTERN, 10, 0, 10, ☃);
            this.setBlockState(☃, SEA_LANTERN, 12, 0, 10, ☃);
            this.setBlockState(☃, SEA_LANTERN, 10, 0, 12, ☃);
            this.setBlockState(☃, SEA_LANTERN, 12, 0, 12, ☃);
            this.setBlockState(☃, SEA_LANTERN, 8, 3, 6, ☃);
            this.setBlockState(☃, SEA_LANTERN, 14, 3, 6, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 4, 2, 4, ☃);
            this.setBlockState(☃, SEA_LANTERN, 4, 1, 4, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 4, 0, 4, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 18, 2, 4, ☃);
            this.setBlockState(☃, SEA_LANTERN, 18, 1, 4, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 18, 0, 4, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 4, 2, 18, ☃);
            this.setBlockState(☃, SEA_LANTERN, 4, 1, 18, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 4, 0, 18, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 18, 2, 18, ☃);
            this.setBlockState(☃, SEA_LANTERN, 18, 1, 18, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 18, 0, 18, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 9, 7, 20, ☃);
            this.setBlockState(☃, BRICKS_PRISMARINE, 13, 7, 20, ☃);
            this.fillWithBlocks(☃, ☃, 6, 0, 21, 7, 4, 21, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 15, 0, 21, 16, 4, 21, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.spawnElder(☃, ☃, 11, 2, 16);
         } else if (this.mainDesign == 1) {
            this.fillWithBlocks(☃, ☃, 9, 3, 18, 13, 3, 20, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 9, 0, 18, 9, 2, 18, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 13, 0, 18, 13, 2, 18, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            int ☃ = 9;
            int ☃x = 20;
            int ☃xx = 5;

            for (int ☃xxx = 0; ☃xxx < 2; ☃xxx++) {
               this.setBlockState(☃, BRICKS_PRISMARINE, ☃, 6, 20, ☃);
               this.setBlockState(☃, SEA_LANTERN, ☃, 5, 20, ☃);
               this.setBlockState(☃, BRICKS_PRISMARINE, ☃, 4, 20, ☃);
               ☃ = 13;
            }

            this.fillWithBlocks(☃, ☃, 7, 3, 7, 15, 3, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
            int var10 = 10;

            for (int ☃xxx = 0; ☃xxx < 2; ☃xxx++) {
               this.fillWithBlocks(☃, ☃, var10, 0, 10, var10, 6, 10, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, var10, 0, 12, var10, 6, 12, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.setBlockState(☃, SEA_LANTERN, var10, 0, 10, ☃);
               this.setBlockState(☃, SEA_LANTERN, var10, 0, 12, ☃);
               this.setBlockState(☃, SEA_LANTERN, var10, 4, 10, ☃);
               this.setBlockState(☃, SEA_LANTERN, var10, 4, 12, ☃);
               var10 = 12;
            }

            var10 = 8;

            for (int ☃xxx = 0; ☃xxx < 2; ☃xxx++) {
               this.fillWithBlocks(☃, ☃, var10, 0, 7, var10, 2, 7, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               this.fillWithBlocks(☃, ☃, var10, 0, 14, var10, 2, 14, BRICKS_PRISMARINE, BRICKS_PRISMARINE, false);
               var10 = 14;
            }

            this.fillWithBlocks(☃, ☃, 8, 3, 8, 8, 3, 13, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.fillWithBlocks(☃, ☃, 14, 3, 8, 14, 3, 13, DARK_PRISMARINE, DARK_PRISMARINE, false);
            this.spawnElder(☃, ☃, 11, 5, 13);
         }

         return true;
      }
   }

   static class XDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
      private XDoubleRoomFitHelper() {
      }

      @Override
      public boolean fits(StructureOceanMonumentPieces.RoomDefinition var1) {
         return ☃.hasOpening[EnumFacing.EAST.getIndex()] && !☃.connections[EnumFacing.EAST.getIndex()].claimed;
      }

      @Override
      public StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         ☃.claimed = true;
         ☃.connections[EnumFacing.EAST.getIndex()].claimed = true;
         return new StructureOceanMonumentPieces.DoubleXRoom(☃, ☃, ☃);
      }
   }

   static class XYDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
      private XYDoubleRoomFitHelper() {
      }

      @Override
      public boolean fits(StructureOceanMonumentPieces.RoomDefinition var1) {
         if (☃.hasOpening[EnumFacing.EAST.getIndex()]
            && !☃.connections[EnumFacing.EAST.getIndex()].claimed
            && ☃.hasOpening[EnumFacing.UP.getIndex()]
            && !☃.connections[EnumFacing.UP.getIndex()].claimed) {
            StructureOceanMonumentPieces.RoomDefinition ☃ = ☃.connections[EnumFacing.EAST.getIndex()];
            return ☃.hasOpening[EnumFacing.UP.getIndex()] && !☃.connections[EnumFacing.UP.getIndex()].claimed;
         } else {
            return false;
         }
      }

      @Override
      public StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         ☃.claimed = true;
         ☃.connections[EnumFacing.EAST.getIndex()].claimed = true;
         ☃.connections[EnumFacing.UP.getIndex()].claimed = true;
         ☃.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
         return new StructureOceanMonumentPieces.DoubleXYRoom(☃, ☃, ☃);
      }
   }

   static class YDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
      private YDoubleRoomFitHelper() {
      }

      @Override
      public boolean fits(StructureOceanMonumentPieces.RoomDefinition var1) {
         return ☃.hasOpening[EnumFacing.UP.getIndex()] && !☃.connections[EnumFacing.UP.getIndex()].claimed;
      }

      @Override
      public StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         ☃.claimed = true;
         ☃.connections[EnumFacing.UP.getIndex()].claimed = true;
         return new StructureOceanMonumentPieces.DoubleYRoom(☃, ☃, ☃);
      }
   }

   static class YZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
      private YZDoubleRoomFitHelper() {
      }

      @Override
      public boolean fits(StructureOceanMonumentPieces.RoomDefinition var1) {
         if (☃.hasOpening[EnumFacing.NORTH.getIndex()]
            && !☃.connections[EnumFacing.NORTH.getIndex()].claimed
            && ☃.hasOpening[EnumFacing.UP.getIndex()]
            && !☃.connections[EnumFacing.UP.getIndex()].claimed) {
            StructureOceanMonumentPieces.RoomDefinition ☃ = ☃.connections[EnumFacing.NORTH.getIndex()];
            return ☃.hasOpening[EnumFacing.UP.getIndex()] && !☃.connections[EnumFacing.UP.getIndex()].claimed;
         } else {
            return false;
         }
      }

      @Override
      public StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         ☃.claimed = true;
         ☃.connections[EnumFacing.NORTH.getIndex()].claimed = true;
         ☃.connections[EnumFacing.UP.getIndex()].claimed = true;
         ☃.connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
         return new StructureOceanMonumentPieces.DoubleYZRoom(☃, ☃, ☃);
      }
   }

   static class ZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
      private ZDoubleRoomFitHelper() {
      }

      @Override
      public boolean fits(StructureOceanMonumentPieces.RoomDefinition var1) {
         return ☃.hasOpening[EnumFacing.NORTH.getIndex()] && !☃.connections[EnumFacing.NORTH.getIndex()].claimed;
      }

      @Override
      public StructureOceanMonumentPieces.Piece create(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3) {
         StructureOceanMonumentPieces.RoomDefinition ☃ = ☃;
         if (!☃.hasOpening[EnumFacing.NORTH.getIndex()] || ☃.connections[EnumFacing.NORTH.getIndex()].claimed) {
            ☃ = ☃.connections[EnumFacing.SOUTH.getIndex()];
         }

         ☃.claimed = true;
         ☃.connections[EnumFacing.NORTH.getIndex()].claimed = true;
         return new StructureOceanMonumentPieces.DoubleZRoom(☃, ☃, ☃);
      }
   }
}
