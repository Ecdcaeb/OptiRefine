package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class BlockModelShapes {
   private final Map<IBlockState, IBakedModel> bakedModelStore = Maps.newIdentityHashMap();
   private final BlockStateMapper blockStateMapper = new BlockStateMapper();
   private final ModelManager modelManager;

   public BlockModelShapes(ModelManager var1) {
      this.modelManager = ☃;
      this.registerAllBlocks();
   }

   public BlockStateMapper getBlockStateMapper() {
      return this.blockStateMapper;
   }

   public TextureAtlasSprite getTexture(IBlockState var1) {
      Block ☃ = ☃.getBlock();
      IBakedModel ☃x = this.getModelForState(☃);
      if (☃x == null || ☃x == this.modelManager.getMissingModel()) {
         if (☃ == Blocks.WALL_SIGN
            || ☃ == Blocks.STANDING_SIGN
            || ☃ == Blocks.CHEST
            || ☃ == Blocks.TRAPPED_CHEST
            || ☃ == Blocks.STANDING_BANNER
            || ☃ == Blocks.WALL_BANNER
            || ☃ == Blocks.BED) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak");
         }

         if (☃ == Blocks.ENDER_CHEST) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/obsidian");
         }

         if (☃ == Blocks.FLOWING_LAVA || ☃ == Blocks.LAVA) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/lava_still");
         }

         if (☃ == Blocks.FLOWING_WATER || ☃ == Blocks.WATER) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/water_still");
         }

         if (☃ == Blocks.SKULL) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/soul_sand");
         }

         if (☃ == Blocks.BARRIER) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/barrier");
         }

         if (☃ == Blocks.STRUCTURE_VOID) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/structure_void");
         }

         if (☃ == Blocks.WHITE_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_white");
         }

         if (☃ == Blocks.ORANGE_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_orange");
         }

         if (☃ == Blocks.MAGENTA_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_magenta");
         }

         if (☃ == Blocks.LIGHT_BLUE_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_light_blue");
         }

         if (☃ == Blocks.YELLOW_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_yellow");
         }

         if (☃ == Blocks.LIME_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_lime");
         }

         if (☃ == Blocks.PINK_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_pink");
         }

         if (☃ == Blocks.GRAY_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_gray");
         }

         if (☃ == Blocks.SILVER_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_silver");
         }

         if (☃ == Blocks.CYAN_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_cyan");
         }

         if (☃ == Blocks.PURPLE_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_purple");
         }

         if (☃ == Blocks.BLUE_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_blue");
         }

         if (☃ == Blocks.BROWN_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_brown");
         }

         if (☃ == Blocks.GREEN_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_green");
         }

         if (☃ == Blocks.RED_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_red");
         }

         if (☃ == Blocks.BLACK_SHULKER_BOX) {
            return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_black");
         }
      }

      if (☃x == null) {
         ☃x = this.modelManager.getMissingModel();
      }

      return ☃x.getParticleTexture();
   }

   public IBakedModel getModelForState(IBlockState var1) {
      IBakedModel ☃ = this.bakedModelStore.get(☃);
      if (☃ == null) {
         ☃ = this.modelManager.getMissingModel();
      }

      return ☃;
   }

   public ModelManager getModelManager() {
      return this.modelManager;
   }

   public void reloadModels() {
      this.bakedModelStore.clear();

      for (Entry<IBlockState, ModelResourceLocation> ☃ : this.blockStateMapper.putAllStateModelLocations().entrySet()) {
         this.bakedModelStore.put(☃.getKey(), this.modelManager.getModel(☃.getValue()));
      }
   }

   public void registerBlockWithStateMapper(Block var1, IStateMapper var2) {
      this.blockStateMapper.registerBlockStateMapper(☃, ☃);
   }

   public void registerBuiltInBlocks(Block... var1) {
      this.blockStateMapper.registerBuiltInBlocks(☃);
   }

   private void registerAllBlocks() {
      this.registerBuiltInBlocks(
         Blocks.AIR,
         Blocks.FLOWING_WATER,
         Blocks.WATER,
         Blocks.FLOWING_LAVA,
         Blocks.LAVA,
         Blocks.PISTON_EXTENSION,
         Blocks.CHEST,
         Blocks.ENDER_CHEST,
         Blocks.TRAPPED_CHEST,
         Blocks.STANDING_SIGN,
         Blocks.SKULL,
         Blocks.END_PORTAL,
         Blocks.BARRIER,
         Blocks.WALL_SIGN,
         Blocks.WALL_BANNER,
         Blocks.STANDING_BANNER,
         Blocks.END_GATEWAY,
         Blocks.STRUCTURE_VOID,
         Blocks.WHITE_SHULKER_BOX,
         Blocks.ORANGE_SHULKER_BOX,
         Blocks.MAGENTA_SHULKER_BOX,
         Blocks.LIGHT_BLUE_SHULKER_BOX,
         Blocks.YELLOW_SHULKER_BOX,
         Blocks.LIME_SHULKER_BOX,
         Blocks.PINK_SHULKER_BOX,
         Blocks.GRAY_SHULKER_BOX,
         Blocks.SILVER_SHULKER_BOX,
         Blocks.CYAN_SHULKER_BOX,
         Blocks.PURPLE_SHULKER_BOX,
         Blocks.BLUE_SHULKER_BOX,
         Blocks.BROWN_SHULKER_BOX,
         Blocks.GREEN_SHULKER_BOX,
         Blocks.RED_SHULKER_BOX,
         Blocks.BLACK_SHULKER_BOX,
         Blocks.BED
      );
      this.registerBlockWithStateMapper(Blocks.STONE, new StateMap.Builder().withName(BlockStone.VARIANT).build());
      this.registerBlockWithStateMapper(Blocks.PRISMARINE, new StateMap.Builder().withName(BlockPrismarine.VARIANT).build());
      this.registerBlockWithStateMapper(
         Blocks.LEAVES,
         new StateMap.Builder().withName(BlockOldLeaf.VARIANT).withSuffix("_leaves").ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build()
      );
      this.registerBlockWithStateMapper(
         Blocks.LEAVES2,
         new StateMap.Builder().withName(BlockNewLeaf.VARIANT).withSuffix("_leaves").ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE).build()
      );
      this.registerBlockWithStateMapper(Blocks.CACTUS, new StateMap.Builder().ignore(BlockCactus.AGE).build());
      this.registerBlockWithStateMapper(Blocks.REEDS, new StateMap.Builder().ignore(BlockReed.AGE).build());
      this.registerBlockWithStateMapper(Blocks.JUKEBOX, new StateMap.Builder().ignore(BlockJukebox.HAS_RECORD).build());
      this.registerBlockWithStateMapper(Blocks.COBBLESTONE_WALL, new StateMap.Builder().withName(BlockWall.VARIANT).withSuffix("_wall").build());
      this.registerBlockWithStateMapper(Blocks.DOUBLE_PLANT, new StateMap.Builder().withName(BlockDoublePlant.VARIANT).ignore(BlockDoublePlant.FACING).build());
      this.registerBlockWithStateMapper(Blocks.OAK_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.SPRUCE_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.BIRCH_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.JUNGLE_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.DARK_OAK_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.ACACIA_FENCE_GATE, new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.TRIPWIRE, new StateMap.Builder().ignore(BlockTripWire.DISARMED, BlockTripWire.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.DOUBLE_WOODEN_SLAB, new StateMap.Builder().withName(BlockPlanks.VARIANT).withSuffix("_double_slab").build());
      this.registerBlockWithStateMapper(Blocks.WOODEN_SLAB, new StateMap.Builder().withName(BlockPlanks.VARIANT).withSuffix("_slab").build());
      this.registerBlockWithStateMapper(Blocks.TNT, new StateMap.Builder().ignore(BlockTNT.EXPLODE).build());
      this.registerBlockWithStateMapper(Blocks.FIRE, new StateMap.Builder().ignore(BlockFire.AGE).build());
      this.registerBlockWithStateMapper(Blocks.REDSTONE_WIRE, new StateMap.Builder().ignore(BlockRedstoneWire.POWER).build());
      this.registerBlockWithStateMapper(Blocks.OAK_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.SPRUCE_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.BIRCH_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.JUNGLE_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.ACACIA_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.DARK_OAK_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.IRON_DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
      this.registerBlockWithStateMapper(Blocks.WOOL, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_wool").build());
      this.registerBlockWithStateMapper(Blocks.CARPET, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_carpet").build());
      this.registerBlockWithStateMapper(
         Blocks.STAINED_HARDENED_CLAY, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_stained_hardened_clay").build()
      );
      this.registerBlockWithStateMapper(
         Blocks.STAINED_GLASS_PANE, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_stained_glass_pane").build()
      );
      this.registerBlockWithStateMapper(Blocks.STAINED_GLASS, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_stained_glass").build());
      this.registerBlockWithStateMapper(Blocks.SANDSTONE, new StateMap.Builder().withName(BlockSandStone.TYPE).build());
      this.registerBlockWithStateMapper(Blocks.RED_SANDSTONE, new StateMap.Builder().withName(BlockRedSandstone.TYPE).build());
      this.registerBlockWithStateMapper(Blocks.TALLGRASS, new StateMap.Builder().withName(BlockTallGrass.TYPE).build());
      this.registerBlockWithStateMapper(Blocks.YELLOW_FLOWER, new StateMap.Builder().withName(Blocks.YELLOW_FLOWER.getTypeProperty()).build());
      this.registerBlockWithStateMapper(Blocks.RED_FLOWER, new StateMap.Builder().withName(Blocks.RED_FLOWER.getTypeProperty()).build());
      this.registerBlockWithStateMapper(Blocks.STONE_SLAB, new StateMap.Builder().withName(BlockStoneSlab.VARIANT).withSuffix("_slab").build());
      this.registerBlockWithStateMapper(Blocks.STONE_SLAB2, new StateMap.Builder().withName(BlockStoneSlabNew.VARIANT).withSuffix("_slab").build());
      this.registerBlockWithStateMapper(Blocks.MONSTER_EGG, new StateMap.Builder().withName(BlockSilverfish.VARIANT).withSuffix("_monster_egg").build());
      this.registerBlockWithStateMapper(Blocks.STONEBRICK, new StateMap.Builder().withName(BlockStoneBrick.VARIANT).build());
      this.registerBlockWithStateMapper(Blocks.DISPENSER, new StateMap.Builder().ignore(BlockDispenser.TRIGGERED).build());
      this.registerBlockWithStateMapper(Blocks.DROPPER, new StateMap.Builder().ignore(BlockDropper.TRIGGERED).build());
      this.registerBlockWithStateMapper(Blocks.LOG, new StateMap.Builder().withName(BlockOldLog.VARIANT).withSuffix("_log").build());
      this.registerBlockWithStateMapper(Blocks.LOG2, new StateMap.Builder().withName(BlockNewLog.VARIANT).withSuffix("_log").build());
      this.registerBlockWithStateMapper(Blocks.PLANKS, new StateMap.Builder().withName(BlockPlanks.VARIANT).withSuffix("_planks").build());
      this.registerBlockWithStateMapper(Blocks.SAPLING, new StateMap.Builder().withName(BlockSapling.TYPE).withSuffix("_sapling").build());
      this.registerBlockWithStateMapper(Blocks.SAND, new StateMap.Builder().withName(BlockSand.VARIANT).build());
      this.registerBlockWithStateMapper(Blocks.HOPPER, new StateMap.Builder().ignore(BlockHopper.ENABLED).build());
      this.registerBlockWithStateMapper(Blocks.FLOWER_POT, new StateMap.Builder().ignore(BlockFlowerPot.LEGACY_DATA).build());
      this.registerBlockWithStateMapper(Blocks.CONCRETE, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_concrete").build());
      this.registerBlockWithStateMapper(Blocks.CONCRETE_POWDER, new StateMap.Builder().withName(BlockColored.COLOR).withSuffix("_concrete_powder").build());
      this.registerBlockWithStateMapper(Blocks.QUARTZ_BLOCK, new StateMapperBase() {
         @Override
         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            BlockQuartz.EnumType ☃ = ☃.getValue(BlockQuartz.VARIANT);
            switch (☃) {
               case DEFAULT:
               default:
                  return new ModelResourceLocation("quartz_block", "normal");
               case CHISELED:
                  return new ModelResourceLocation("chiseled_quartz_block", "normal");
               case LINES_Y:
                  return new ModelResourceLocation("quartz_column", "axis=y");
               case LINES_X:
                  return new ModelResourceLocation("quartz_column", "axis=x");
               case LINES_Z:
                  return new ModelResourceLocation("quartz_column", "axis=z");
            }
         }
      });
      this.registerBlockWithStateMapper(Blocks.DEADBUSH, new StateMapperBase() {
         @Override
         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            return new ModelResourceLocation("dead_bush", "normal");
         }
      });
      this.registerBlockWithStateMapper(Blocks.PUMPKIN_STEM, new StateMapperBase() {
         @Override
         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            Map<IProperty<?>, Comparable<?>> ☃ = Maps.newLinkedHashMap(☃.getProperties());
            if (☃.getValue(BlockStem.FACING) != EnumFacing.UP) {
               ☃.remove(BlockStem.AGE);
            }

            return new ModelResourceLocation(Block.REGISTRY.getNameForObject(☃.getBlock()), this.getPropertyString(☃));
         }
      });
      this.registerBlockWithStateMapper(Blocks.MELON_STEM, new StateMapperBase() {
         @Override
         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            Map<IProperty<?>, Comparable<?>> ☃ = Maps.newLinkedHashMap(☃.getProperties());
            if (☃.getValue(BlockStem.FACING) != EnumFacing.UP) {
               ☃.remove(BlockStem.AGE);
            }

            return new ModelResourceLocation(Block.REGISTRY.getNameForObject(☃.getBlock()), this.getPropertyString(☃));
         }
      });
      this.registerBlockWithStateMapper(Blocks.DIRT, new StateMapperBase() {
         @Override
         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            Map<IProperty<?>, Comparable<?>> ☃ = Maps.newLinkedHashMap(☃.getProperties());
            String ☃x = BlockDirt.VARIANT.getName((BlockDirt.DirtType)☃.remove(BlockDirt.VARIANT));
            if (BlockDirt.DirtType.PODZOL != ☃.getValue(BlockDirt.VARIANT)) {
               ☃.remove(BlockDirt.SNOWY);
            }

            return new ModelResourceLocation(☃x, this.getPropertyString(☃));
         }
      });
      this.registerBlockWithStateMapper(Blocks.DOUBLE_STONE_SLAB, new StateMapperBase() {
         @Override
         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            Map<IProperty<?>, Comparable<?>> ☃ = Maps.newLinkedHashMap(☃.getProperties());
            String ☃x = BlockStoneSlab.VARIANT.getName((BlockStoneSlab.EnumType)☃.remove(BlockStoneSlab.VARIANT));
            ☃.remove(BlockStoneSlab.SEAMLESS);
            String ☃xx = ☃.getValue(BlockStoneSlab.SEAMLESS) ? "all" : "normal";
            return new ModelResourceLocation(☃x + "_double_slab", ☃xx);
         }
      });
      this.registerBlockWithStateMapper(Blocks.DOUBLE_STONE_SLAB2, new StateMapperBase() {
         @Override
         protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
            Map<IProperty<?>, Comparable<?>> ☃ = Maps.newLinkedHashMap(☃.getProperties());
            String ☃x = BlockStoneSlabNew.VARIANT.getName((BlockStoneSlabNew.EnumType)☃.remove(BlockStoneSlabNew.VARIANT));
            ☃.remove(BlockStoneSlab.SEAMLESS);
            String ☃xx = ☃.getValue(BlockStoneSlabNew.SEAMLESS) ? "all" : "normal";
            return new ModelResourceLocation(☃x + "_double_slab", ☃xx);
         }
      });
   }
}
