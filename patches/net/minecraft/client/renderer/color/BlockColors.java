package net.minecraft.client.renderer.color;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockColors {
   private final ObjectIntIdentityMap<IBlockColor> mapBlockColors = new ObjectIntIdentityMap<>(32);

   public static BlockColors init() {
      final BlockColors ☃ = new BlockColors();
      ☃.registerBlockColorHandler(
         new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
               BlockDoublePlant.EnumPlantType ☃x = ☃.getValue(BlockDoublePlant.VARIANT);
               return ☃ != null && ☃ != null && (☃x == BlockDoublePlant.EnumPlantType.GRASS || ☃x == BlockDoublePlant.EnumPlantType.FERN)
                  ? BiomeColorHelper.getGrassColorAtPos(☃, ☃.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER ? ☃.down() : ☃)
                  : -1;
            }
         },
         Blocks.DOUBLE_PLANT
      );
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            if (☃ != null && ☃ != null) {
               TileEntity ☃ = ☃.getTileEntity(☃);
               if (☃ instanceof TileEntityFlowerPot) {
                  Item ☃x = ((TileEntityFlowerPot)☃).getFlowerPotItem();
                  IBlockState ☃xx = Block.getBlockFromItem(☃x).getDefaultState();
                  return ☃.colorMultiplier(☃xx, ☃, ☃, ☃);
               } else {
                  return -1;
               }
            } else {
               return -1;
            }
         }
      }, Blocks.FLOWER_POT);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            return ☃ != null && ☃ != null ? BiomeColorHelper.getGrassColorAtPos(☃, ☃) : ColorizerGrass.getGrassColor(0.5, 1.0);
         }
      }, Blocks.GRASS);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            BlockPlanks.EnumType ☃ = ☃.getValue(BlockOldLeaf.VARIANT);
            if (☃ == BlockPlanks.EnumType.SPRUCE) {
               return ColorizerFoliage.getFoliageColorPine();
            } else if (☃ == BlockPlanks.EnumType.BIRCH) {
               return ColorizerFoliage.getFoliageColorBirch();
            } else {
               return ☃ != null && ☃ != null ? BiomeColorHelper.getFoliageColorAtPos(☃, ☃) : ColorizerFoliage.getFoliageColorBasic();
            }
         }
      }, Blocks.LEAVES);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            return ☃ != null && ☃ != null ? BiomeColorHelper.getFoliageColorAtPos(☃, ☃) : ColorizerFoliage.getFoliageColorBasic();
         }
      }, Blocks.LEAVES2);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            return ☃ != null && ☃ != null ? BiomeColorHelper.getWaterColorAtPos(☃, ☃) : -1;
         }
      }, Blocks.WATER, Blocks.FLOWING_WATER);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            return BlockRedstoneWire.colorMultiplier(☃.getValue(BlockRedstoneWire.POWER));
         }
      }, Blocks.REDSTONE_WIRE);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            return ☃ != null && ☃ != null ? BiomeColorHelper.getGrassColorAtPos(☃, ☃) : -1;
         }
      }, Blocks.REEDS);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            int ☃ = ☃.getValue(BlockStem.AGE);
            int ☃x = ☃ * 32;
            int ☃xx = 255 - ☃ * 8;
            int ☃xxx = ☃ * 4;
            return ☃x << 16 | ☃xx << 8 | ☃xxx;
         }
      }, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            if (☃ != null && ☃ != null) {
               return BiomeColorHelper.getGrassColorAtPos(☃, ☃);
            } else {
               return ☃.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.DEAD_BUSH ? 16777215 : ColorizerGrass.getGrassColor(0.5, 1.0);
            }
         }
      }, Blocks.TALLGRASS);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            return ☃ != null && ☃ != null ? BiomeColorHelper.getFoliageColorAtPos(☃, ☃) : ColorizerFoliage.getFoliageColorBasic();
         }
      }, Blocks.VINE);
      ☃.registerBlockColorHandler(new IBlockColor() {
         @Override
         public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
            return ☃ != null && ☃ != null ? 2129968 : 7455580;
         }
      }, Blocks.WATERLILY);
      return ☃;
   }

   public int getColor(IBlockState var1, World var2, BlockPos var3) {
      IBlockColor ☃ = this.mapBlockColors.getByValue(Block.getIdFromBlock(☃.getBlock()));
      if (☃ != null) {
         return ☃.colorMultiplier(☃, null, null, 0);
      } else {
         MapColor ☃x = ☃.getMapColor(☃, ☃);
         return ☃x != null ? ☃x.colorValue : -1;
      }
   }

   public int colorMultiplier(IBlockState var1, @Nullable IBlockAccess var2, @Nullable BlockPos var3, int var4) {
      IBlockColor ☃ = this.mapBlockColors.getByValue(Block.getIdFromBlock(☃.getBlock()));
      return ☃ == null ? -1 : ☃.colorMultiplier(☃, ☃, ☃, ☃);
   }

   public void registerBlockColorHandler(IBlockColor var1, Block... var2) {
      for (Block ☃ : ☃) {
         this.mapBlockColors.put(☃, Block.getIdFromBlock(☃));
      }
   }
}
