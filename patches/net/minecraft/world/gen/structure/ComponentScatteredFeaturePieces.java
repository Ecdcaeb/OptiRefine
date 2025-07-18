package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class ComponentScatteredFeaturePieces {
   public static void registerScatteredFeaturePieces() {
      MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.DesertPyramid.class, "TeDP");
      MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.JunglePyramid.class, "TeJP");
      MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.SwampHut.class, "TeSH");
      MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.Igloo.class, "Iglu");
   }

   public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature {
      private final boolean[] hasPlacedChest = new boolean[4];

      public DesertPyramid() {
      }

      public DesertPyramid(Random var1, int var2, int var3) {
         super(☃, ☃, 64, ☃, 21, 15, 21);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("hasPlacedChest0", this.hasPlacedChest[0]);
         ☃.setBoolean("hasPlacedChest1", this.hasPlacedChest[1]);
         ☃.setBoolean("hasPlacedChest2", this.hasPlacedChest[2]);
         ☃.setBoolean("hasPlacedChest3", this.hasPlacedChest[3]);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.hasPlacedChest[0] = ☃.getBoolean("hasPlacedChest0");
         this.hasPlacedChest[1] = ☃.getBoolean("hasPlacedChest1");
         this.hasPlacedChest[2] = ☃.getBoolean("hasPlacedChest2");
         this.hasPlacedChest[3] = ☃.getBoolean("hasPlacedChest3");
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithBlocks(☃, ☃, 0, -4, 0, this.width - 1, 0, this.depth - 1, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);

         for (int ☃ = 1; ☃ <= 9; ☃++) {
            this.fillWithBlocks(
               ☃, ☃, ☃, ☃, ☃, this.width - 1 - ☃, ☃, this.depth - 1 - ☃, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false
            );
            this.fillWithBlocks(
               ☃, ☃, ☃ + 1, ☃, ☃ + 1, this.width - 2 - ☃, ☃, this.depth - 2 - ☃, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false
            );
         }

         for (int ☃ = 0; ☃ < this.width; ☃++) {
            for (int ☃x = 0; ☃x < this.depth; ☃x++) {
               int ☃xx = -5;
               this.replaceAirAndLiquidDownwards(☃, Blocks.SANDSTONE.getDefaultState(), ☃, -5, ☃x, ☃);
            }
         }

         IBlockState ☃ = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
         IBlockState ☃x = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
         IBlockState ☃xx = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
         IBlockState ☃xxx = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
         int ☃xxxx = ~EnumDyeColor.ORANGE.getDyeDamage() & 15;
         int ☃xxxxx = ~EnumDyeColor.BLUE.getDyeDamage() & 15;
         this.fillWithBlocks(☃, ☃, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.setBlockState(☃, ☃, 2, 10, 0, ☃);
         this.setBlockState(☃, ☃x, 2, 10, 4, ☃);
         this.setBlockState(☃, ☃xx, 0, 10, 2, ☃);
         this.setBlockState(☃, ☃xxx, 4, 10, 2, ☃);
         this.fillWithBlocks(☃, ☃, this.width - 5, 0, 0, this.width - 1, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, this.width - 4, 10, 1, this.width - 2, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.setBlockState(☃, ☃, this.width - 3, 10, 0, ☃);
         this.setBlockState(☃, ☃x, this.width - 3, 10, 4, ☃);
         this.setBlockState(☃, ☃xx, this.width - 5, 10, 2, ☃);
         this.setBlockState(☃, ☃xxx, this.width - 1, 10, 2, ☃);
         this.fillWithBlocks(☃, ☃, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 1, 0, 11, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, ☃);
         this.fillWithBlocks(☃, ☃, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 4, 1, 2, 8, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 12, 1, 2, 16, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 5, 4, 5, this.width - 6, 4, this.depth - 6, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, 4, 9, 11, 4, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(
            ☃,
            ☃,
            8,
            1,
            8,
            8,
            3,
            8,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.fillWithBlocks(
            ☃,
            ☃,
            12,
            1,
            8,
            12,
            3,
            8,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.fillWithBlocks(
            ☃,
            ☃,
            8,
            1,
            12,
            8,
            3,
            12,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.fillWithBlocks(
            ☃,
            ☃,
            12,
            1,
            12,
            12,
            3,
            12,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.fillWithBlocks(☃, ☃, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, this.width - 5, 1, 5, this.width - 2, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, this.width - 7, 7, 9, this.width - 7, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(
            ☃,
            ☃,
            5,
            5,
            9,
            5,
            7,
            11,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.fillWithBlocks(
            ☃,
            ☃,
            this.width - 6,
            5,
            9,
            this.width - 6,
            7,
            11,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 5, 5, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 5, 6, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 6, 6, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), this.width - 6, 5, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), this.width - 6, 6, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), this.width - 7, 6, 10, ☃);
         this.fillWithBlocks(☃, ☃, 2, 4, 4, 2, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, this.width - 3, 4, 4, this.width - 3, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.setBlockState(☃, ☃, 2, 4, 5, ☃);
         this.setBlockState(☃, ☃, 2, 3, 4, ☃);
         this.setBlockState(☃, ☃, this.width - 3, 4, 5, ☃);
         this.setBlockState(☃, ☃, this.width - 3, 3, 4, ☃);
         this.fillWithBlocks(☃, ☃, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, this.width - 3, 1, 3, this.width - 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.setBlockState(☃, Blocks.SANDSTONE.getDefaultState(), 1, 1, 2, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getDefaultState(), this.width - 2, 1, 2, ☃);
         this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, ☃);
         this.setBlockState(☃, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), this.width - 2, 2, 2, ☃);
         this.setBlockState(☃, ☃xxx, 2, 1, 2, ☃);
         this.setBlockState(☃, ☃xx, this.width - 3, 1, 2, ☃);
         this.fillWithBlocks(☃, ☃, 4, 3, 5, 4, 3, 18, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, this.width - 5, 3, 5, this.width - 5, 3, 17, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 3, 1, 5, 4, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, this.width - 6, 1, 5, this.width - 5, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

         for (int ☃xxxxxx = 5; ☃xxxxxx <= 17; ☃xxxxxx += 2) {
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, ☃xxxxxx, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, ☃xxxxxx, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), this.width - 5, 1, ☃xxxxxx, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), this.width - 5, 2, ☃xxxxxx, ☃);
         }

         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 10, 0, 7, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 10, 0, 8, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 9, 0, 9, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 11, 0, 9, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 8, 0, 10, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 12, 0, 10, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 7, 0, 10, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 13, 0, 10, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 9, 0, 11, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 11, 0, 11, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 10, 0, 12, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 10, 0, 13, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxxx), 10, 0, 10, ☃);

         for (int ☃xxxxxx = 0; ☃xxxxxx <= this.width - 1; ☃xxxxxx += this.width - 1) {
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 2, 1, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 2, 2, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 2, 3, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 3, 1, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 3, 2, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 3, 3, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 4, 1, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), ☃xxxxxx, 4, 2, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 4, 3, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 5, 1, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 5, 2, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 5, 3, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 6, 1, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), ☃xxxxxx, 6, 2, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 6, 3, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 7, 1, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 7, 2, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 7, 3, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 8, 1, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 8, 2, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 8, 3, ☃);
         }

         for (int ☃xxxxxx = 2; ☃xxxxxx <= this.width - 3; ☃xxxxxx += this.width - 3 - 2) {
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx - 1, 2, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 2, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx + 1, 2, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx - 1, 3, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 3, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx + 1, 3, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx - 1, 4, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), ☃xxxxxx, 4, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx + 1, 4, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx - 1, 5, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 5, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx + 1, 5, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx - 1, 6, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), ☃xxxxxx, 6, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx + 1, 6, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx - 1, 7, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx, 7, 0, ☃);
            this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), ☃xxxxxx + 1, 7, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx - 1, 8, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx, 8, 0, ☃);
            this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), ☃xxxxxx + 1, 8, 0, ☃);
         }

         this.fillWithBlocks(
            ☃,
            ☃,
            8,
            4,
            0,
            12,
            6,
            0,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 8, 6, 0, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 12, 6, 0, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 9, 5, 0, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, ☃);
         this.setBlockState(☃, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(☃xxxx), 11, 5, 0, ☃);
         this.fillWithBlocks(
            ☃,
            ☃,
            8,
            -14,
            8,
            12,
            -11,
            12,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.fillWithBlocks(
            ☃,
            ☃,
            8,
            -10,
            8,
            12,
            -10,
            12,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()),
            false
         );
         this.fillWithBlocks(
            ☃,
            ☃,
            8,
            -9,
            8,
            12,
            -9,
            12,
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()),
            false
         );
         this.fillWithBlocks(☃, ☃, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
         this.fillWithBlocks(☃, ☃, 9, -11, 9, 11, -1, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.setBlockState(☃, Blocks.STONE_PRESSURE_PLATE.getDefaultState(), 10, -11, 10, ☃);
         this.fillWithBlocks(☃, ☃, 9, -13, 9, 11, -13, 11, Blocks.TNT.getDefaultState(), Blocks.AIR.getDefaultState(), false);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 8, -11, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 8, -10, 10, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 12, -11, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 12, -10, 10, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 10, -11, 8, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 10, -10, 8, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 10, -11, 12, ☃);
         this.setBlockState(☃, Blocks.AIR.getDefaultState(), 10, -10, 12, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, ☃);
         this.setBlockState(☃, Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, ☃);

         for (EnumFacing ☃xxxxxx : EnumFacing.Plane.HORIZONTAL) {
            if (!this.hasPlacedChest[☃xxxxxx.getHorizontalIndex()]) {
               int ☃xxxxxxx = ☃xxxxxx.getXOffset() * 2;
               int ☃xxxxxxxx = ☃xxxxxx.getZOffset() * 2;
               this.hasPlacedChest[☃xxxxxx.getHorizontalIndex()] = this.generateChest(
                  ☃, ☃, ☃, 10 + ☃xxxxxxx, -11, 10 + ☃xxxxxxxx, LootTableList.CHESTS_DESERT_PYRAMID
               );
            }
         }

         return true;
      }
   }

   abstract static class Feature extends StructureComponent {
      protected int width;
      protected int height;
      protected int depth;
      protected int horizontalPos = -1;

      public Feature() {
      }

      protected Feature(Random var1, int var2, int var3, int var4, int var5, int var6, int var7) {
         super(0);
         this.width = ☃;
         this.height = ☃;
         this.depth = ☃;
         this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(☃));
         if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
            this.boundingBox = new StructureBoundingBox(☃, ☃, ☃, ☃ + ☃ - 1, ☃ + ☃ - 1, ☃ + ☃ - 1);
         } else {
            this.boundingBox = new StructureBoundingBox(☃, ☃, ☃, ☃ + ☃ - 1, ☃ + ☃ - 1, ☃ + ☃ - 1);
         }
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         ☃.setInteger("Width", this.width);
         ☃.setInteger("Height", this.height);
         ☃.setInteger("Depth", this.depth);
         ☃.setInteger("HPos", this.horizontalPos);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         this.width = ☃.getInteger("Width");
         this.height = ☃.getInteger("Height");
         this.depth = ☃.getInteger("Depth");
         this.horizontalPos = ☃.getInteger("HPos");
      }

      protected boolean offsetToAverageGroundLevel(World var1, StructureBoundingBox var2, int var3) {
         if (this.horizontalPos >= 0) {
            return true;
         } else {
            int ☃ = 0;
            int ☃x = 0;
            BlockPos.MutableBlockPos ☃xx = new BlockPos.MutableBlockPos();

            for (int ☃xxx = this.boundingBox.minZ; ☃xxx <= this.boundingBox.maxZ; ☃xxx++) {
               for (int ☃xxxx = this.boundingBox.minX; ☃xxxx <= this.boundingBox.maxX; ☃xxxx++) {
                  ☃xx.setPos(☃xxxx, 64, ☃xxx);
                  if (☃.isVecInside(☃xx)) {
                     ☃ += Math.max(☃.getTopSolidOrLiquidBlock(☃xx).getY(), ☃.provider.getAverageGroundLevel());
                     ☃x++;
                  }
               }
            }

            if (☃x == 0) {
               return false;
            } else {
               this.horizontalPos = ☃ / ☃x;
               this.boundingBox.offset(0, this.horizontalPos - this.boundingBox.minY + ☃, 0);
               return true;
            }
         }
      }
   }

   public static class Igloo extends ComponentScatteredFeaturePieces.Feature {
      private static final ResourceLocation IGLOO_TOP_ID = new ResourceLocation("igloo/igloo_top");
      private static final ResourceLocation IGLOO_MIDDLE_ID = new ResourceLocation("igloo/igloo_middle");
      private static final ResourceLocation IGLOO_BOTTOM_ID = new ResourceLocation("igloo/igloo_bottom");

      public Igloo() {
      }

      public Igloo(Random var1, int var2, int var3) {
         super(☃, ☃, 64, ☃, 7, 5, 8);
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (!this.offsetToAverageGroundLevel(☃, ☃, -1)) {
            return false;
         } else {
            StructureBoundingBox ☃ = this.getBoundingBox();
            BlockPos ☃x = new BlockPos(☃.minX, ☃.minY, ☃.minZ);
            Rotation[] ☃xx = Rotation.values();
            MinecraftServer ☃xxx = ☃.getMinecraftServer();
            TemplateManager ☃xxxx = ☃.getSaveHandler().getStructureTemplateManager();
            PlacementSettings ☃xxxxx = new PlacementSettings()
               .setRotation(☃xx[☃.nextInt(☃xx.length)])
               .setReplacedBlock(Blocks.STRUCTURE_VOID)
               .setBoundingBox(☃);
            Template ☃xxxxxx = ☃xxxx.getTemplate(☃xxx, IGLOO_TOP_ID);
            ☃xxxxxx.addBlocksToWorldChunk(☃, ☃x, ☃xxxxx);
            if (☃.nextDouble() < 0.5) {
               Template ☃xxxxxxx = ☃xxxx.getTemplate(☃xxx, IGLOO_MIDDLE_ID);
               Template ☃xxxxxxxx = ☃xxxx.getTemplate(☃xxx, IGLOO_BOTTOM_ID);
               int ☃xxxxxxxxx = ☃.nextInt(8) + 4;

               for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < ☃xxxxxxxxx; ☃xxxxxxxxxx++) {
                  BlockPos ☃xxxxxxxxxxx = ☃xxxxxx.calculateConnectedPos(☃xxxxx, new BlockPos(3, -1 - ☃xxxxxxxxxx * 3, 5), ☃xxxxx, new BlockPos(1, 2, 1));
                  ☃xxxxxxx.addBlocksToWorldChunk(☃, ☃x.add(☃xxxxxxxxxxx), ☃xxxxx);
               }

               BlockPos ☃xxxxxxxxxx = ☃x.add(☃xxxxxx.calculateConnectedPos(☃xxxxx, new BlockPos(3, -1 - ☃xxxxxxxxx * 3, 5), ☃xxxxx, new BlockPos(3, 5, 7)));
               ☃xxxxxxxx.addBlocksToWorldChunk(☃, ☃xxxxxxxxxx, ☃xxxxx);
               Map<BlockPos, String> ☃xxxxxxxxxxx = ☃xxxxxxxx.getDataBlocks(☃xxxxxxxxxx, ☃xxxxx);

               for (Entry<BlockPos, String> ☃xxxxxxxxxxxx : ☃xxxxxxxxxxx.entrySet()) {
                  if ("chest".equals(☃xxxxxxxxxxxx.getValue())) {
                     BlockPos ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getKey();
                     ☃.setBlockState(☃xxxxxxxxxxxxx, Blocks.AIR.getDefaultState(), 3);
                     TileEntity ☃xxxxxxxxxxxxxx = ☃.getTileEntity(☃xxxxxxxxxxxxx.down());
                     if (☃xxxxxxxxxxxxxx instanceof TileEntityChest) {
                        ((TileEntityChest)☃xxxxxxxxxxxxxx).setLootTable(LootTableList.CHESTS_IGLOO_CHEST, ☃.nextLong());
                     }
                  }
               }
            } else {
               BlockPos ☃xxxxxxx = Template.transformedBlockPos(☃xxxxx, new BlockPos(3, 0, 5));
               ☃.setBlockState(☃x.add(☃xxxxxxx), Blocks.SNOW.getDefaultState(), 3);
            }

            return true;
         }
      }
   }

   public static class JunglePyramid extends ComponentScatteredFeaturePieces.Feature {
      private boolean placedMainChest;
      private boolean placedHiddenChest;
      private boolean placedTrap1;
      private boolean placedTrap2;
      private static final ComponentScatteredFeaturePieces.JunglePyramid.Stones cobblestoneSelector = new ComponentScatteredFeaturePieces.JunglePyramid.Stones();

      public JunglePyramid() {
      }

      public JunglePyramid(Random var1, int var2, int var3) {
         super(☃, ☃, 64, ☃, 12, 10, 15);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("placedMainChest", this.placedMainChest);
         ☃.setBoolean("placedHiddenChest", this.placedHiddenChest);
         ☃.setBoolean("placedTrap1", this.placedTrap1);
         ☃.setBoolean("placedTrap2", this.placedTrap2);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.placedMainChest = ☃.getBoolean("placedMainChest");
         this.placedHiddenChest = ☃.getBoolean("placedHiddenChest");
         this.placedTrap1 = ☃.getBoolean("placedTrap1");
         this.placedTrap2 = ☃.getBoolean("placedTrap2");
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (!this.offsetToAverageGroundLevel(☃, ☃, 0)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(☃, ☃, 0, -4, 0, this.width - 1, 0, this.depth - 1, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 2, 1, 2, 9, 2, 2, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 2, 1, 12, 9, 2, 12, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 2, 1, 3, 2, 2, 11, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 9, 1, 3, 9, 2, 11, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 1, 3, 1, 10, 6, 1, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 1, 3, 13, 10, 6, 13, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 1, 3, 2, 1, 6, 12, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 10, 3, 2, 10, 6, 12, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 2, 3, 2, 9, 3, 12, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 2, 6, 2, 9, 6, 12, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 3, 7, 3, 8, 7, 11, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 8, 4, 7, 8, 10, false, ☃, cobblestoneSelector);
            this.fillWithAir(☃, ☃, 3, 1, 3, 8, 2, 11);
            this.fillWithAir(☃, ☃, 4, 3, 6, 7, 3, 9);
            this.fillWithAir(☃, ☃, 2, 4, 2, 9, 5, 12);
            this.fillWithAir(☃, ☃, 4, 6, 5, 7, 6, 9);
            this.fillWithAir(☃, ☃, 5, 7, 6, 6, 7, 8);
            this.fillWithAir(☃, ☃, 5, 1, 2, 6, 2, 2);
            this.fillWithAir(☃, ☃, 5, 2, 12, 6, 2, 12);
            this.fillWithAir(☃, ☃, 5, 5, 1, 6, 5, 1);
            this.fillWithAir(☃, ☃, 5, 5, 13, 6, 5, 13);
            this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, 5, 5, ☃);
            this.setBlockState(☃, Blocks.AIR.getDefaultState(), 10, 5, 5, ☃);
            this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, 5, 9, ☃);
            this.setBlockState(☃, Blocks.AIR.getDefaultState(), 10, 5, 9, ☃);

            for (int ☃ = 0; ☃ <= 14; ☃ += 14) {
               this.fillWithRandomizedBlocks(☃, ☃, 2, 4, ☃, 2, 5, ☃, false, ☃, cobblestoneSelector);
               this.fillWithRandomizedBlocks(☃, ☃, 4, 4, ☃, 4, 5, ☃, false, ☃, cobblestoneSelector);
               this.fillWithRandomizedBlocks(☃, ☃, 7, 4, ☃, 7, 5, ☃, false, ☃, cobblestoneSelector);
               this.fillWithRandomizedBlocks(☃, ☃, 9, 4, ☃, 9, 5, ☃, false, ☃, cobblestoneSelector);
            }

            this.fillWithRandomizedBlocks(☃, ☃, 5, 6, 0, 6, 6, 0, false, ☃, cobblestoneSelector);

            for (int ☃ = 0; ☃ <= 11; ☃ += 11) {
               for (int ☃x = 2; ☃x <= 12; ☃x += 2) {
                  this.fillWithRandomizedBlocks(☃, ☃, ☃, 4, ☃x, ☃, 5, ☃x, false, ☃, cobblestoneSelector);
               }

               this.fillWithRandomizedBlocks(☃, ☃, ☃, 6, 5, ☃, 6, 5, false, ☃, cobblestoneSelector);
               this.fillWithRandomizedBlocks(☃, ☃, ☃, 6, 9, ☃, 6, 9, false, ☃, cobblestoneSelector);
            }

            this.fillWithRandomizedBlocks(☃, ☃, 2, 7, 2, 2, 9, 2, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 9, 7, 2, 9, 9, 2, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 2, 7, 12, 2, 9, 12, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 9, 7, 12, 9, 9, 12, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 9, 4, 4, 9, 4, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 7, 9, 4, 7, 9, 4, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 9, 10, 4, 9, 10, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 7, 9, 10, 7, 9, 10, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 5, 9, 7, 6, 9, 7, false, ☃, cobblestoneSelector);
            IBlockState ☃ = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
            IBlockState ☃x = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
            IBlockState ☃xx = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
            IBlockState ☃xxx = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
            this.setBlockState(☃, ☃xxx, 5, 9, 6, ☃);
            this.setBlockState(☃, ☃xxx, 6, 9, 6, ☃);
            this.setBlockState(☃, ☃xx, 5, 9, 8, ☃);
            this.setBlockState(☃, ☃xx, 6, 9, 8, ☃);
            this.setBlockState(☃, ☃xxx, 4, 0, 0, ☃);
            this.setBlockState(☃, ☃xxx, 5, 0, 0, ☃);
            this.setBlockState(☃, ☃xxx, 6, 0, 0, ☃);
            this.setBlockState(☃, ☃xxx, 7, 0, 0, ☃);
            this.setBlockState(☃, ☃xxx, 4, 1, 8, ☃);
            this.setBlockState(☃, ☃xxx, 4, 2, 9, ☃);
            this.setBlockState(☃, ☃xxx, 4, 3, 10, ☃);
            this.setBlockState(☃, ☃xxx, 7, 1, 8, ☃);
            this.setBlockState(☃, ☃xxx, 7, 2, 9, ☃);
            this.setBlockState(☃, ☃xxx, 7, 3, 10, ☃);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 9, 4, 1, 9, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 7, 1, 9, 7, 1, 9, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 4, 1, 10, 7, 2, 10, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 5, 4, 5, 6, 4, 5, false, ☃, cobblestoneSelector);
            this.setBlockState(☃, ☃, 4, 4, 5, ☃);
            this.setBlockState(☃, ☃x, 7, 4, 5, ☃);

            for (int ☃xxxx = 0; ☃xxxx < 4; ☃xxxx++) {
               this.setBlockState(☃, ☃xx, 5, 0 - ☃xxxx, 6 + ☃xxxx, ☃);
               this.setBlockState(☃, ☃xx, 6, 0 - ☃xxxx, 6 + ☃xxxx, ☃);
               this.fillWithAir(☃, ☃, 5, 0 - ☃xxxx, 7 + ☃xxxx, 6, 0 - ☃xxxx, 9 + ☃xxxx);
            }

            this.fillWithAir(☃, ☃, 1, -3, 12, 10, -1, 13);
            this.fillWithAir(☃, ☃, 1, -3, 1, 3, -1, 13);
            this.fillWithAir(☃, ☃, 1, -3, 1, 9, -1, 5);

            for (int ☃xxxx = 1; ☃xxxx <= 13; ☃xxxx += 2) {
               this.fillWithRandomizedBlocks(☃, ☃, 1, -3, ☃xxxx, 1, -2, ☃xxxx, false, ☃, cobblestoneSelector);
            }

            for (int ☃xxxx = 2; ☃xxxx <= 12; ☃xxxx += 2) {
               this.fillWithRandomizedBlocks(☃, ☃, 1, -1, ☃xxxx, 3, -1, ☃xxxx, false, ☃, cobblestoneSelector);
            }

            this.fillWithRandomizedBlocks(☃, ☃, 2, -2, 1, 5, -2, 1, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 7, -2, 1, 9, -2, 1, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 6, -3, 1, 6, -3, 1, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 6, -1, 1, 6, -1, 1, false, ☃, cobblestoneSelector);
            this.setBlockState(
               ☃,
               Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.EAST).withProperty(BlockTripWireHook.ATTACHED, true),
               1,
               -3,
               8,
               ☃
            );
            this.setBlockState(
               ☃,
               Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.WEST).withProperty(BlockTripWireHook.ATTACHED, true),
               4,
               -3,
               8,
               ☃
            );
            this.setBlockState(☃, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 2, -3, 8, ☃);
            this.setBlockState(☃, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 3, -3, 8, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 7, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 6, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 5, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 4, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 3, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 2, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 1, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 4, -3, 1, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 3, -3, 1, ☃);
            if (!this.placedTrap1) {
               this.placedTrap1 = this.createDispenser(☃, ☃, ☃, 3, -2, 1, EnumFacing.NORTH, LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
            }

            this.setBlockState(☃, Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH, true), 3, -2, 2, ☃);
            this.setBlockState(
               ☃,
               Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.NORTH).withProperty(BlockTripWireHook.ATTACHED, true),
               7,
               -3,
               1,
               ☃
            );
            this.setBlockState(
               ☃,
               Blocks.TRIPWIRE_HOOK.getDefaultState().withProperty(BlockTripWireHook.FACING, EnumFacing.SOUTH).withProperty(BlockTripWireHook.ATTACHED, true),
               7,
               -3,
               5,
               ☃
            );
            this.setBlockState(☃, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 2, ☃);
            this.setBlockState(☃, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 3, ☃);
            this.setBlockState(☃, Blocks.TRIPWIRE.getDefaultState().withProperty(BlockTripWire.ATTACHED, true), 7, -3, 4, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -3, 6, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -3, 6, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -3, 5, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 4, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -2, 4, ☃);
            if (!this.placedTrap2) {
               this.placedTrap2 = this.createDispenser(☃, ☃, ☃, 9, -2, 3, EnumFacing.WEST, LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
            }

            this.setBlockState(☃, Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, true), 8, -1, 3, ☃);
            this.setBlockState(☃, Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, true), 8, -2, 3, ☃);
            if (!this.placedMainChest) {
               this.placedMainChest = this.generateChest(☃, ☃, ☃, 8, -3, 3, LootTableList.CHESTS_JUNGLE_TEMPLE);
            }

            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 2, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 1, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 4, -3, 5, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -2, 5, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -1, 5, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 6, -3, 5, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -2, 5, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -1, 5, ☃);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 5, ☃);
            this.fillWithRandomizedBlocks(☃, ☃, 9, -1, 1, 9, -1, 5, false, ☃, cobblestoneSelector);
            this.fillWithAir(☃, ☃, 8, -3, 8, 10, -1, 10);
            this.setBlockState(☃, Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, ☃);
            this.setBlockState(☃, Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, ☃);
            IBlockState ☃xxxx = Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH);
            this.setBlockState(☃, ☃xxxx, 8, -2, 12, ☃);
            this.setBlockState(☃, ☃xxxx, 9, -2, 12, ☃);
            this.setBlockState(☃, ☃xxxx, 10, -2, 12, ☃);
            this.fillWithRandomizedBlocks(☃, ☃, 8, -3, 8, 8, -3, 10, false, ☃, cobblestoneSelector);
            this.fillWithRandomizedBlocks(☃, ☃, 10, -3, 8, 10, -3, 10, false, ☃, cobblestoneSelector);
            this.setBlockState(☃, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 10, -2, 9, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -2, 9, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -2, 10, ☃);
            this.setBlockState(☃, Blocks.REDSTONE_WIRE.getDefaultState(), 10, -1, 9, ☃);
            this.setBlockState(☃, Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.UP), 9, -2, 8, ☃);
            this.setBlockState(☃, Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.WEST), 10, -2, 8, ☃);
            this.setBlockState(☃, Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.WEST), 10, -1, 8, ☃);
            this.setBlockState(☃, Blocks.UNPOWERED_REPEATER.getDefaultState().withProperty(BlockRedstoneRepeater.FACING, EnumFacing.NORTH), 10, -2, 10, ☃);
            if (!this.placedHiddenChest) {
               this.placedHiddenChest = this.generateChest(☃, ☃, ☃, 9, -3, 10, LootTableList.CHESTS_JUNGLE_TEMPLE);
            }

            return true;
         }
      }

      static class Stones extends StructureComponent.BlockSelector {
         private Stones() {
         }

         @Override
         public void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5) {
            if (☃.nextFloat() < 0.4F) {
               this.blockstate = Blocks.COBBLESTONE.getDefaultState();
            } else {
               this.blockstate = Blocks.MOSSY_COBBLESTONE.getDefaultState();
            }
         }
      }
   }

   public static class SwampHut extends ComponentScatteredFeaturePieces.Feature {
      private boolean hasWitch;

      public SwampHut() {
      }

      public SwampHut(Random var1, int var2, int var3) {
         super(☃, ☃, 64, ☃, 7, 7, 9);
      }

      @Override
      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(☃);
         ☃.setBoolean("Witch", this.hasWitch);
      }

      @Override
      protected void readStructureFromNBT(NBTTagCompound var1, TemplateManager var2) {
         super.readStructureFromNBT(☃, ☃);
         this.hasWitch = ☃.getBoolean("Witch");
      }

      @Override
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (!this.offsetToAverageGroundLevel(☃, ☃, 0)) {
            return false;
         } else {
            this.fillWithBlocks(
               ☃,
               ☃,
               1,
               1,
               1,
               5,
               1,
               7,
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               false
            );
            this.fillWithBlocks(
               ☃,
               ☃,
               1,
               4,
               2,
               5,
               4,
               7,
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               false
            );
            this.fillWithBlocks(
               ☃,
               ☃,
               2,
               1,
               0,
               4,
               1,
               0,
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               false
            );
            this.fillWithBlocks(
               ☃,
               ☃,
               2,
               2,
               2,
               3,
               3,
               2,
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               false
            );
            this.fillWithBlocks(
               ☃,
               ☃,
               1,
               2,
               3,
               1,
               3,
               6,
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               false
            );
            this.fillWithBlocks(
               ☃,
               ☃,
               5,
               2,
               3,
               5,
               3,
               6,
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               false
            );
            this.fillWithBlocks(
               ☃,
               ☃,
               2,
               2,
               7,
               4,
               3,
               7,
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()),
               false
            );
            this.fillWithBlocks(☃, ☃, 1, 0, 2, 1, 3, 2, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 5, 0, 2, 5, 3, 2, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 1, 0, 7, 1, 3, 7, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
            this.fillWithBlocks(☃, ☃, 5, 0, 7, 5, 3, 7, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
            this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 2, 3, 2, ☃);
            this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 3, 3, 7, ☃);
            this.setBlockState(☃, Blocks.AIR.getDefaultState(), 1, 3, 4, ☃);
            this.setBlockState(☃, Blocks.AIR.getDefaultState(), 5, 3, 4, ☃);
            this.setBlockState(☃, Blocks.AIR.getDefaultState(), 5, 3, 5, ☃);
            this.setBlockState(
               ☃, Blocks.FLOWER_POT.getDefaultState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, ☃
            );
            this.setBlockState(☃, Blocks.CRAFTING_TABLE.getDefaultState(), 3, 2, 6, ☃);
            this.setBlockState(☃, Blocks.CAULDRON.getDefaultState(), 4, 2, 6, ☃);
            this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 1, 2, 1, ☃);
            this.setBlockState(☃, Blocks.OAK_FENCE.getDefaultState(), 5, 2, 1, ☃);
            IBlockState ☃ = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
            IBlockState ☃x = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
            IBlockState ☃xx = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
            IBlockState ☃xxx = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
            this.fillWithBlocks(☃, ☃, 0, 4, 1, 6, 4, 1, ☃, ☃, false);
            this.fillWithBlocks(☃, ☃, 0, 4, 2, 0, 4, 7, ☃x, ☃x, false);
            this.fillWithBlocks(☃, ☃, 6, 4, 2, 6, 4, 7, ☃xx, ☃xx, false);
            this.fillWithBlocks(☃, ☃, 0, 4, 8, 6, 4, 8, ☃xxx, ☃xxx, false);

            for (int ☃xxxx = 2; ☃xxxx <= 7; ☃xxxx += 5) {
               for (int ☃xxxxx = 1; ☃xxxxx <= 5; ☃xxxxx += 4) {
                  this.replaceAirAndLiquidDownwards(☃, Blocks.LOG.getDefaultState(), ☃xxxxx, -1, ☃xxxx, ☃);
               }
            }

            if (!this.hasWitch) {
               int ☃xxxx = this.getXWithOffset(2, 5);
               int ☃xxxxx = this.getYWithOffset(2);
               int ☃xxxxxx = this.getZWithOffset(2, 5);
               if (☃.isVecInside(new BlockPos(☃xxxx, ☃xxxxx, ☃xxxxxx))) {
                  this.hasWitch = true;
                  EntityWitch ☃xxxxxxx = new EntityWitch(☃);
                  ☃xxxxxxx.enablePersistence();
                  ☃xxxxxxx.setLocationAndAngles(☃xxxx + 0.5, ☃xxxxx, ☃xxxxxx + 0.5, 0.0F, 0.0F);
                  ☃xxxxxxx.onInitialSpawn(☃.getDifficultyForLocation(new BlockPos(☃xxxx, ☃xxxxx, ☃xxxxxx)), null);
                  ☃.spawnEntity(☃xxxxxxx);
               }
            }

            return true;
         }
      }
   }
}
