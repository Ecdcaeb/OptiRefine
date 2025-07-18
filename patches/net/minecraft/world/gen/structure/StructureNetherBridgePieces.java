package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureNetherBridgePieces {
   private static final StructureNetherBridgePieces.PieceWeight[] PRIMARY_COMPONENTS = new StructureNetherBridgePieces.PieceWeight[]{
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Straight.class, 30, 0, true),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing3.class, 10, 4),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing.class, 10, 4),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Stairs.class, 10, 3),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Throne.class, 5, 2),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Entrance.class, 5, 1)
   };
   private static final StructureNetherBridgePieces.PieceWeight[] SECONDARY_COMPONENTS = new StructureNetherBridgePieces.PieceWeight[]{
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor5.class, 25, 0, true),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing2.class, 15, 5),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor2.class, 5, 10),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor.class, 5, 10),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor3.class, 10, 3, true),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor4.class, 7, 2),
      new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.NetherStalkRoom.class, 5, 2)
   };

   public static void registerNetherFortressPieces() {
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing3.class, "NeBCr");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.End.class, "NeBEF");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Straight.class, "NeBS");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor3.class, "NeCCS");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor4.class, "NeCTB");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Entrance.class, "NeCE");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing2.class, "NeSCSC");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor.class, "NeSCLT");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor5.class, "NeSC");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor2.class, "NeSCRT");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.NetherStalkRoom.class, "NeCSR");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Throne.class, "NeMT");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing.class, "NeRC");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Stairs.class, "NeSR");
      MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Start.class, "NeStart");
   }

   private static StructureNetherBridgePieces.Piece findAndCreateBridgePieceFactory(
      StructureNetherBridgePieces.PieceWeight var0, List<StructureComponent> var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7
   ) {
      Class<? extends StructureNetherBridgePieces.Piece> ☃ = ☃.weightClass;
      StructureNetherBridgePieces.Piece ☃x = null;
      if (☃ == StructureNetherBridgePieces.Straight.class) {
         ☃x = StructureNetherBridgePieces.Straight.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Crossing3.class) {
         ☃x = StructureNetherBridgePieces.Crossing3.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Crossing.class) {
         ☃x = StructureNetherBridgePieces.Crossing.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Stairs.class) {
         ☃x = StructureNetherBridgePieces.Stairs.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Throne.class) {
         ☃x = StructureNetherBridgePieces.Throne.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Entrance.class) {
         ☃x = StructureNetherBridgePieces.Entrance.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Corridor5.class) {
         ☃x = StructureNetherBridgePieces.Corridor5.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Corridor2.class) {
         ☃x = StructureNetherBridgePieces.Corridor2.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Corridor.class) {
         ☃x = StructureNetherBridgePieces.Corridor.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Corridor3.class) {
         ☃x = StructureNetherBridgePieces.Corridor3.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Corridor4.class) {
         ☃x = StructureNetherBridgePieces.Corridor4.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.Crossing2.class) {
         ☃x = StructureNetherBridgePieces.Crossing2.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else if (☃ == StructureNetherBridgePieces.NetherStalkRoom.class) {
         ☃x = StructureNetherBridgePieces.NetherStalkRoom.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }

      return ☃x;
   }

   public static class Corridor extends StructureNetherBridgePieces.Piece {
      private boolean chest;

      public Corridor() {
      }

      public Corridor(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.chest = ☃.nextInt(3) == 0;
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.chest = ☃.getBoolean("Chest");
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Chest", this.chest);
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentX((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, 1, true);
      }

      public static StructureNetherBridgePieces.Corridor createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Corridor(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         if (this.chest && ☃.isVecInside(new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
            this.chest = false;
            this.generateChest(☃, ☃, ☃, 3, 2, 3, LootTableList.CHESTS_NETHER_BRIDGE);
         }

         this.fillWithBlocks(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 4; ☃++) {
            for (int ☃x = 0; ☃x <= 4; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Corridor2 extends StructureNetherBridgePieces.Piece {
      private boolean chest;

      public Corridor2() {
      }

      public Corridor2(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.chest = ☃.nextInt(3) == 0;
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.chest = ☃.getBoolean("Chest");
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Chest", this.chest);
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentZ((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, 1, true);
      }

      public static StructureNetherBridgePieces.Corridor2 createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Corridor2(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         if (this.chest && ☃.isVecInside(new BlockPos(this.getXWithOffset(1, 3), this.getYWithOffset(2), this.getZWithOffset(1, 3)))) {
            this.chest = false;
            this.generateChest(☃, ☃, ☃, 1, 2, 3, LootTableList.CHESTS_NETHER_BRIDGE);
         }

         this.fillWithBlocks(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 4; ☃++) {
            for (int ☃x = 0; ☃x <= 4; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Corridor3 extends StructureNetherBridgePieces.Piece {
      public Corridor3() {
      }

      public Corridor3(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 1, 0, true);
      }

      public static StructureNetherBridgePieces.Corridor3 createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -7, 0, 5, 14, 10, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Corridor3(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         IBlockState ☃ = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

         for (int ☃x = 0; ☃x <= 9; ☃x++) {
            int ☃xx = Math.max(1, 7 - ☃x);
            int ☃xxx = Math.min(Math.max(☃xx + 5, 14 - ☃x), 13);
            int ☃xxxx = ☃x;
            this.fillWithBlocks(☃, ☃, 0, 0, ☃x, 4, ☃xx, ☃x, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 1, ☃xx + 1, ☃x, 3, ☃xxx - 1, ☃x, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            if (☃x <= 6) {
               this.setBlockState(☃, ☃, 1, ☃xx + 1, ☃x, ☃);
               this.setBlockState(☃, ☃, 2, ☃xx + 1, ☃x, ☃);
               this.setBlockState(☃, ☃, 3, ☃xx + 1, ☃x, ☃);
            }

            this.fillWithBlocks(☃, ☃, 0, ☃xxx, ☃x, 4, ☃xxx, ☃x, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 0, ☃xx + 1, ☃x, 0, ☃xxx - 1, ☃x, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 4, ☃xx + 1, ☃x, 4, ☃xxx - 1, ☃x, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            if ((☃x & 1) == 0) {
               this.fillWithBlocks(
                  ☃, ☃, 0, ☃xx + 2, ☃x, 0, ☃xx + 3, ☃x, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false
               );
               this.fillWithBlocks(
                  ☃, ☃, 4, ☃xx + 2, ☃x, 4, ☃xx + 3, ☃x, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false
               );
            }

            for (int ☃xxxxx = 0; ☃xxxxx <= 4; ☃xxxxx++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃xxxxx, -1, ☃xxxx, ☃);
            }
         }

         return true;
      }
   }

   public static class Corridor4 extends StructureNetherBridgePieces.Piece {
      public Corridor4() {
      }

      public Corridor4(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         int ☃ = 1;
         EnumFacing ☃x = this.getCoordBaseMode();
         if (☃x == EnumFacing.WEST || ☃x == EnumFacing.NORTH) {
            ☃ = 5;
         }

         this.getNextComponentX((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, ☃, ☃.nextInt(8) > 0);
         this.getNextComponentZ((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, ☃, ☃.nextInt(8) > 0);
      }

      public static StructureNetherBridgePieces.Corridor4 createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -3, 0, 0, 9, 7, 9, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Corridor4(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 8, 5, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 3, 0, 1, 4, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 3, 0, 7, 4, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 1, 4, 2, 2, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 1, 4, 7, 2, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 8, 8, 3, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 6, 0, 3, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 3, 6, 8, 3, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 4, 5, 1, 5, 5, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 4, 5, 7, 5, 5, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 5; ☃++) {
            for (int ☃x = 0; ☃x <= 8; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃x, -1, ☃, ☃);
            }
         }

         return true;
      }
   }

   public static class Corridor5 extends StructureNetherBridgePieces.Piece {
      public Corridor5() {
      }

      public Corridor5(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 1, 0, true);
      }

      public static StructureNetherBridgePieces.Corridor5 createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Corridor5(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 4; ☃++) {
            for (int ☃x = 0; ☃x <= 4; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Crossing extends StructureNetherBridgePieces.Piece {
      public Crossing() {
      }

      public Crossing(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 2, 0, false);
         this.getNextComponentX((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, 2, false);
         this.getNextComponentZ((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, 2, false);
      }

      public static StructureNetherBridgePieces.Crossing createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -2, 0, 0, 7, 9, 7, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Crossing(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 6, 7, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 6, 4, 5, 6, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 5, 2, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 6; ☃++) {
            for (int ☃x = 0; ☃x <= 6; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Crossing2 extends StructureNetherBridgePieces.Piece {
      public Crossing2() {
      }

      public Crossing2(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 1, 0, true);
         this.getNextComponentX((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, 1, true);
         this.getNextComponentZ((StructureNetherBridgePieces.Start)☃, ☃, ☃, 0, 1, true);
      }

      public static StructureNetherBridgePieces.Crossing2 createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Crossing2(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 4; ☃++) {
            for (int ☃x = 0; ☃x <= 4; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Crossing3 extends StructureNetherBridgePieces.Piece {
      public Crossing3() {
      }

      public Crossing3(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      protected Crossing3(Random var1, int var2, int var3) {
         super(0);
         this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(☃));
         if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
            this.boundingBox = new StructureBoundingBox(☃, 64, ☃, ☃ + 19 - 1, 73, ☃ + 19 - 1);
         } else {
            this.boundingBox = new StructureBoundingBox(☃, 64, ☃, ☃ + 19 - 1, 73, ☃ + 19 - 1);
         }
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 8, 3, false);
         this.getNextComponentX((StructureNetherBridgePieces.Start)☃, ☃, ☃, 3, 8, false);
         this.getNextComponentZ((StructureNetherBridgePieces.Start)☃, ☃, ☃, 3, 8, false);
      }

      public static StructureNetherBridgePieces.Crossing3 createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -8, -3, 0, 19, 10, 19, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Crossing3(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 5, 0, 10, 7, 18, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 8, 18, 7, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 7; ☃ <= 11; ☃++) {
            for (int ☃x = 0; ☃x <= 2; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, 18 - ☃x, ☃);
            }
         }

         this.fillWithBlocks(☃, ☃, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 2; ☃++) {
            for (int ☃x = 7; ☃x <= 11; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), 18 - ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class End extends StructureNetherBridgePieces.Piece {
      private int fillSeed;

      public End() {
      }

      public End(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
         this.fillSeed = ☃.nextInt();
      }

      public static StructureNetherBridgePieces.End createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -3, 0, 5, 10, 8, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.End(☃, ☃, ☃, ☃) : null;
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.fillSeed = ☃.getInteger("Seed");
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setInteger("Seed", this.fillSeed);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         Random ☃ = new Random(this.fillSeed);

         for (int ☃x = 0; ☃x <= 4; ☃x++) {
            for (int ☃xx = 3; ☃xx <= 4; ☃xx++) {
               int ☃xxx = ☃.nextInt(8);
               this.fillWithBlocks(☃, ☃, ☃x, ☃xx, 0, ☃x, ☃xx, ☃xxx, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            }
         }

         int ☃x = ☃.nextInt(8);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 0, 5, ☃x, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         ☃x = ☃.nextInt(8);
         this.fillWithBlocks(☃, ☃, 4, 5, 0, 4, 5, ☃x, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃xx = 0; ☃xx <= 4; ☃xx++) {
            int ☃xxx = ☃.nextInt(5);
            this.fillWithBlocks(☃, ☃, ☃xx, 2, 0, ☃xx, 2, ☃xxx, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         }

         for (int ☃xx = 0; ☃xx <= 4; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx <= 1; ☃xxx++) {
               int ☃xxxx = ☃.nextInt(3);
               this.fillWithBlocks(☃, ☃, ☃xx, ☃xxx, 0, ☃xx, ☃xxx, ☃xxxx, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            }
         }

         return true;
      }
   }

   public static class Entrance extends StructureNetherBridgePieces.Piece {
      public Entrance() {
      }

      public Entrance(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 5, 3, true);
      }

      public static StructureNetherBridgePieces.Entrance createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -5, -3, 0, 13, 14, 13, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Entrance(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 12, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

         for (int ☃ = 1; ☃ <= 11; ☃ += 2) {
            this.fillWithBlocks(☃, ☃, ☃, 10, 0, ☃, 11, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, ☃, 10, 12, ☃, 11, 12, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 0, 10, ☃, 0, 11, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 12, 10, ☃, 12, 11, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, 13, 0, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, 13, 12, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), 0, 13, ☃, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), 12, 13, ☃, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), ☃ + 1, 13, 0, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), ☃ + 1, 13, 12, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, ☃ + 1, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, ☃ + 1, ☃);
         }

         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, ☃);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 12, ☃);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, ☃);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, 0, ☃);

         for (int ☃ = 3; ☃ <= 9; ☃ += 2) {
            this.fillWithBlocks(☃, ☃, 1, 7, ☃, 1, 8, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 11, 7, ☃, 11, 8, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         }

         this.fillWithBlocks(☃, ☃, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 4; ☃ <= 8; ☃++) {
            for (int ☃x = 0; ☃x <= 2; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, 12 - ☃x, ☃);
            }
         }

         for (int ☃ = 0; ☃ <= 2; ☃++) {
            for (int ☃x = 4; ☃x <= 8; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), 12 - ☃, -1, ☃x, ☃);
            }
         }

         this.fillWithBlocks(☃, ☃, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 1, 6, 6, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), 6, 0, 6, ☃);
         IBlockState ☃ = Blocks.FLOWING_LAVA.getDefaultState();
         this.setBlockState(☃, ☃, 6, 5, 6, ☃);
         BlockPos ☃x = new BlockPos(this.getXWithOffset(6, 6), this.getYWithOffset(5), this.getZWithOffset(6, 6));
         if (☃.isVecInside(☃x)) {
            ☃.immediateBlockTick(☃x, ☃, ☃);
         }

         return true;
      }
   }

   public static class NetherStalkRoom extends StructureNetherBridgePieces.Piece {
      public NetherStalkRoom() {
      }

      public NetherStalkRoom(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 5, 3, true);
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 5, 11, true);
      }

      public static StructureNetherBridgePieces.NetherStalkRoom createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -5, -3, 0, 13, 14, 13, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.NetherStalkRoom(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 12, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 1; ☃ <= 11; ☃ += 2) {
            this.fillWithBlocks(☃, ☃, ☃, 10, 0, ☃, 11, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, ☃, 10, 12, ☃, 11, 12, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 0, 10, ☃, 0, 11, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 12, 10, ☃, 12, 11, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, 13, 0, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, 13, 12, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), 0, 13, ☃, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), 12, 13, ☃, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), ☃ + 1, 13, 0, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), ☃ + 1, 13, 12, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, ☃ + 1, ☃);
            this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, ☃ + 1, ☃);
         }

         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, ☃);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 12, ☃);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, ☃);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, 0, ☃);

         for (int ☃ = 3; ☃ <= 9; ☃ += 2) {
            this.fillWithBlocks(☃, ☃, 1, 7, ☃, 1, 8, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 11, 7, ☃, 11, 8, ☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         }

         IBlockState ☃ = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);

         for (int ☃x = 0; ☃x <= 6; ☃x++) {
            int ☃xx = ☃x + 4;

            for (int ☃xxx = 5; ☃xxx <= 7; ☃xxx++) {
               this.setBlockState(☃, ☃, ☃xxx, 5 + ☃x, ☃xx, ☃);
            }

            if (☃xx >= 5 && ☃xx <= 8) {
               this.fillWithBlocks(☃, ☃, 5, 5, ☃xx, 7, ☃x + 4, ☃xx, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            } else if (☃xx >= 9 && ☃xx <= 10) {
               this.fillWithBlocks(☃, ☃, 5, 8, ☃xx, 7, ☃x + 4, ☃xx, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            }

            if (☃x >= 1) {
               this.fillWithBlocks(☃, ☃, 5, 6 + ☃x, ☃xx, 7, 9 + ☃x, ☃xx, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }
         }

         for (int ☃x = 5; ☃x <= 7; ☃x++) {
            this.setBlockState(☃, ☃, ☃x, 12, 11, ☃);
         }

         this.fillWithBlocks(☃, ☃, 5, 6, 7, 5, 7, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 7, 6, 7, 7, 7, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 13, 12, 7, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         IBlockState ☃x = ☃.withProperty(BlockStairs.FACING, EnumFacing.EAST);
         IBlockState ☃xx = ☃.withProperty(BlockStairs.FACING, EnumFacing.WEST);
         this.setBlockState(☃, ☃xx, 4, 5, 2, ☃);
         this.setBlockState(☃, ☃xx, 4, 5, 3, ☃);
         this.setBlockState(☃, ☃xx, 4, 5, 9, ☃);
         this.setBlockState(☃, ☃xx, 4, 5, 10, ☃);
         this.setBlockState(☃, ☃x, 8, 5, 2, ☃);
         this.setBlockState(☃, ☃x, 8, 5, 3, ☃);
         this.setBlockState(☃, ☃x, 8, 5, 9, ☃);
         this.setBlockState(☃, ☃x, 8, 5, 10, ☃);
         this.fillWithBlocks(☃, ☃, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.getDefaultState(), Blocks.SOUL_SAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.getDefaultState(), Blocks.SOUL_SAND.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.getDefaultState(), Blocks.NETHER_WART.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.getDefaultState(), Blocks.NETHER_WART.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃xxx = 4; ☃xxx <= 8; ☃xxx++) {
            for (int ☃xxxx = 0; ☃xxxx <= 2; ☃xxxx++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃xxx, -1, ☃xxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃xxx, -1, 12 - ☃xxxx, ☃);
            }
         }

         for (int ☃xxx = 0; ☃xxx <= 2; ☃xxx++) {
            for (int ☃xxxx = 4; ☃xxxx <= 8; ☃xxxx++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃xxx, -1, ☃xxxx, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), 12 - ☃xxx, -1, ☃xxxx, ☃);
            }
         }

         return true;
      }
   }

   abstract static class Piece extends StructureComponent {
      public Piece() {
      }

      protected Piece(int var1) {
         super(☃);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
      }

      private int getTotalWeight(List<StructureNetherBridgePieces.PieceWeight> var1) {
         boolean ☃ = false;
         int ☃x = 0;

         for (StructureNetherBridgePieces.PieceWeight ☃xx : ☃) {
            if (☃xx.maxPlaceCount > 0 && ☃xx.placeCount < ☃xx.maxPlaceCount) {
               ☃ = true;
            }

            ☃x += ☃xx.weight;
         }

         return ☃ ? ☃x : -1;
      }

      private StructureNetherBridgePieces.Piece generatePiece(
         StructureNetherBridgePieces.Start var1,
         List<StructureNetherBridgePieces.PieceWeight> var2,
         List<StructureComponent> var3,
         Random var4,
         int var5,
         int var6,
         int var7,
         EnumFacing var8,
         int var9
      ) {
         int ☃ = this.getTotalWeight(☃);
         boolean ☃x = ☃ > 0 && ☃ <= 30;
         int ☃xx = 0;

         while (☃xx < 5 && ☃x) {
            ☃xx++;
            int ☃xxx = ☃.nextInt(☃);

            for (StructureNetherBridgePieces.PieceWeight ☃xxxx : ☃) {
               ☃xxx -= ☃xxxx.weight;
               if (☃xxx < 0) {
                  if (!☃xxxx.doPlace(☃) || ☃xxxx == ☃.lastPlaced && !☃xxxx.allowInRow) {
                     break;
                  }

                  StructureNetherBridgePieces.Piece ☃xxxxx = StructureNetherBridgePieces.findAndCreateBridgePieceFactory(☃xxxx, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
                  if (☃xxxxx != null) {
                     ☃xxxx.placeCount++;
                     ☃.lastPlaced = ☃xxxx;
                     if (!☃xxxx.isValid()) {
                        ☃.remove(☃xxxx);
                     }

                     return ☃xxxxx;
                  }
               }
            }
         }

         return StructureNetherBridgePieces.End.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }

      private StructureComponent generateAndAddPiece(
         StructureNetherBridgePieces.Start var1,
         List<StructureComponent> var2,
         Random var3,
         int var4,
         int var5,
         int var6,
         @Nullable EnumFacing var7,
         int var8,
         boolean var9
      ) {
         if (Math.abs(☃ - ☃.getBoundingBox().minX) <= 112 && Math.abs(☃ - ☃.getBoundingBox().minZ) <= 112) {
            List<StructureNetherBridgePieces.PieceWeight> ☃ = ☃.primaryWeights;
            if (☃) {
               ☃ = ☃.secondaryWeights;
            }

            StructureComponent ☃x = this.generatePiece(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃ + 1);
            if (☃x != null) {
               ☃.add(☃x);
               ☃.pendingChildren.add(☃x);
            }

            return ☃x;
         } else {
            return StructureNetherBridgePieces.End.createPiece(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         }
      }

      @Nullable
      protected StructureComponent getNextComponentNormal(
         StructureNetherBridgePieces.Start var1, List<StructureComponent> var2, Random var3, int var4, int var5, boolean var6
      ) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, ☃, this.getComponentType(), ☃
                  );
               case SOUTH:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, ☃, this.getComponentType(), ☃
                  );
               case WEST:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, ☃, this.getComponentType(), ☃
                  );
               case EAST:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, ☃, this.getComponentType(), ☃
                  );
            }
         }

         return null;
      }

      @Nullable
      protected StructureComponent getNextComponentX(
         StructureNetherBridgePieces.Start var1, List<StructureComponent> var2, Random var3, int var4, int var5, boolean var6
      ) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.WEST, this.getComponentType(), ☃
                  );
               case SOUTH:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX - 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.WEST, this.getComponentType(), ☃
                  );
               case WEST:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), ☃
                  );
               case EAST:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), ☃
                  );
            }
         }

         return null;
      }

      @Nullable
      protected StructureComponent getNextComponentZ(
         StructureNetherBridgePieces.Start var1, List<StructureComponent> var2, Random var3, int var4, int var5, boolean var6
      ) {
         EnumFacing ☃ = this.getCoordBaseMode();
         if (☃ != null) {
            switch (☃) {
               case NORTH:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.EAST, this.getComponentType(), ☃
                  );
               case SOUTH:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.maxX + 1, this.boundingBox.minY + ☃, this.boundingBox.minZ + ☃, EnumFacing.EAST, this.getComponentType(), ☃
                  );
               case WEST:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), ☃
                  );
               case EAST:
                  return this.generateAndAddPiece(
                     ☃, ☃, ☃, this.boundingBox.minX + ☃, this.boundingBox.minY + ☃, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), ☃
                  );
            }
         }

         return null;
      }

      protected static boolean isAboveGround(StructureBoundingBox var0) {
         return ☃ != null && ☃.minY > 10;
      }
   }

   static class PieceWeight {
      public Class<? extends StructureNetherBridgePieces.Piece> weightClass;
      public final int weight;
      public int placeCount;
      public int maxPlaceCount;
      public boolean allowInRow;

      public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> var1, int var2, int var3, boolean var4) {
         this.weightClass = ☃;
         this.weight = ☃;
         this.maxPlaceCount = ☃;
         this.allowInRow = ☃;
      }

      public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> var1, int var2, int var3) {
         this(☃, ☃, ☃, false);
      }

      public boolean doPlace(int var1) {
         return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
      }

      public boolean isValid() {
         return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
      }
   }

   public static class Stairs extends StructureNetherBridgePieces.Piece {
      public Stairs() {
      }

      public Stairs(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentZ((StructureNetherBridgePieces.Start)☃, ☃, ☃, 6, 2, false);
      }

      public static StructureNetherBridgePieces.Stairs createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, int var5, EnumFacing var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -2, 0, 0, 7, 11, 7, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Stairs(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 6, 10, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 3, 2, 6, 5, 2, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 3, 4, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.setBlockState(☃, Blocks.NETHER_BRICK.getDefaultState(), 5, 2, 5, ☃);
         this.fillWithBlocks(☃, ☃, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 8, 2, 6, 8, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 6; ☃++) {
            for (int ☃x = 0; ☃x <= 6; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }

   public static class Start extends StructureNetherBridgePieces.Crossing3 {
      public StructureNetherBridgePieces.PieceWeight lastPlaced;
      public List<StructureNetherBridgePieces.PieceWeight> primaryWeights;
      public List<StructureNetherBridgePieces.PieceWeight> secondaryWeights;
      public List<StructureComponent> pendingChildren = Lists.newArrayList();

      public Start() {
      }

      public Start(Random var1, int var2, int var3) {
         super(☃, ☃, ☃);
         this.primaryWeights = Lists.newArrayList();

         for (StructureNetherBridgePieces.PieceWeight ☃ : StructureNetherBridgePieces.PRIMARY_COMPONENTS) {
            ☃.placeCount = 0;
            this.primaryWeights.add(☃);
         }

         this.secondaryWeights = Lists.newArrayList();

         for (StructureNetherBridgePieces.PieceWeight ☃ : StructureNetherBridgePieces.SECONDARY_COMPONENTS) {
            ☃.placeCount = 0;
            this.secondaryWeights.add(☃);
         }
      }
   }

   public static class Straight extends StructureNetherBridgePieces.Piece {
      public Straight() {
      }

      public Straight(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
         this.getNextComponentNormal((StructureNetherBridgePieces.Start)☃, ☃, ☃, 1, 3, false);
      }

      public static StructureNetherBridgePieces.Straight createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -1, -3, 0, 5, 10, 19, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Straight(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 5, 0, 3, 7, 18, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

         for (int ☃ = 0; ☃ <= 4; ☃++) {
            for (int ☃x = 0; ☃x <= 2; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, 18 - ☃x, ☃);
            }
         }

         this.fillWithBlocks(☃, ☃, 0, 1, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 4, 0, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 3, 14, 0, 4, 14, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 1, 17, 0, 4, 17, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 1, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 3, 4, 4, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 3, 14, 4, 4, 14, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 1, 17, 4, 4, 17, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         return true;
      }
   }

   public static class Throne extends StructureNetherBridgePieces.Piece {
      private boolean hasSpawner;

      public Throne() {
      }

      public Throne(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(☃);
         this.setCoordBaseMode(☃);
         this.boundingBox = ☃;
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.hasSpawner = ☃.getBoolean("Mob");
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Mob", this.hasSpawner);
      }

      public static StructureNetherBridgePieces.Throne createPiece(
         List<StructureComponent> var0, Random var1, int var2, int var3, int var4, int var5, EnumFacing var6
      ) {
         StructureBoundingBox ☃ = StructureBoundingBox.getComponentToAddBoundingBox(☃, ☃, ☃, -2, 0, 0, 7, 8, 9, ☃);
         return isAboveGround(☃) && StructureComponent.findIntersecting(☃, ☃) == null ? new StructureNetherBridgePieces.Throne(☃, ☃, ☃, ☃) : null;
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, 2, 0, 6, 7, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 1, 6, 3, ☃);
         this.setBlockState(☃, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 5, 6, 3, ☃);
         this.fillWithBlocks(☃, ☃, 0, 6, 3, 0, 6, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 6, 3, 6, 6, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 6, 8, 5, 7, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 2, 8, 8, 4, 8, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
         if (!this.hasSpawner) {
            BlockPos ☃ = new BlockPos(this.getXWithOffset(3, 5), this.getYWithOffset(5), this.getZWithOffset(3, 5));
            if (☃.isVecInside(☃)) {
               this.hasSpawner = true;
               ☃.setBlockState(☃, Blocks.MOB_SPAWNER.getDefaultState(), 2);
               TileEntity ☃x = ☃.getTileEntity(☃);
               if (☃x instanceof TileEntityMobSpawner) {
                  ((TileEntityMobSpawner)☃x).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityBlaze.class));
               }
            }
         }

         for (int ☃ = 0; ☃ <= 6; ☃++) {
            for (int ☃x = 0; ☃x <= 6; ☃x++) {
               this.replaceAirAndLiquidDownwards(☃, Blocks.NETHER_BRICK.getDefaultState(), ☃, -1, ☃x, ☃);
            }
         }

         return true;
      }
   }
}
