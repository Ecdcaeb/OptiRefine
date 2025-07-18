package net.minecraft.client.renderer.block.model;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.multipart.Multipart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistrySimple;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBakery {
   private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet(
      new ResourceLocation[]{
         new ResourceLocation("blocks/water_flow"),
         new ResourceLocation("blocks/water_still"),
         new ResourceLocation("blocks/lava_flow"),
         new ResourceLocation("blocks/lava_still"),
         new ResourceLocation("blocks/water_overlay"),
         new ResourceLocation("blocks/destroy_stage_0"),
         new ResourceLocation("blocks/destroy_stage_1"),
         new ResourceLocation("blocks/destroy_stage_2"),
         new ResourceLocation("blocks/destroy_stage_3"),
         new ResourceLocation("blocks/destroy_stage_4"),
         new ResourceLocation("blocks/destroy_stage_5"),
         new ResourceLocation("blocks/destroy_stage_6"),
         new ResourceLocation("blocks/destroy_stage_7"),
         new ResourceLocation("blocks/destroy_stage_8"),
         new ResourceLocation("blocks/destroy_stage_9"),
         new ResourceLocation("items/empty_armor_slot_helmet"),
         new ResourceLocation("items/empty_armor_slot_chestplate"),
         new ResourceLocation("items/empty_armor_slot_leggings"),
         new ResourceLocation("items/empty_armor_slot_boots"),
         new ResourceLocation("items/empty_armor_slot_shield"),
         new ResourceLocation("blocks/shulker_top_white"),
         new ResourceLocation("blocks/shulker_top_orange"),
         new ResourceLocation("blocks/shulker_top_magenta"),
         new ResourceLocation("blocks/shulker_top_light_blue"),
         new ResourceLocation("blocks/shulker_top_yellow"),
         new ResourceLocation("blocks/shulker_top_lime"),
         new ResourceLocation("blocks/shulker_top_pink"),
         new ResourceLocation("blocks/shulker_top_gray"),
         new ResourceLocation("blocks/shulker_top_silver"),
         new ResourceLocation("blocks/shulker_top_cyan"),
         new ResourceLocation("blocks/shulker_top_purple"),
         new ResourceLocation("blocks/shulker_top_blue"),
         new ResourceLocation("blocks/shulker_top_brown"),
         new ResourceLocation("blocks/shulker_top_green"),
         new ResourceLocation("blocks/shulker_top_red"),
         new ResourceLocation("blocks/shulker_top_black")
      }
   );
   private static final Logger LOGGER = LogManager.getLogger();
   protected static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
   private static final String MISSING_MODEL_MESH = "{    'textures': {       'particle': 'missingno',       'missingno': 'missingno'    },    'elements': [         {  'from': [ 0, 0, 0 ],            'to': [ 16, 16, 16 ],            'faces': {                'down':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'down',  'texture': '#missingno' },                'up':    { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'up',    'texture': '#missingno' },                'north': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'north', 'texture': '#missingno' },                'south': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'south', 'texture': '#missingno' },                'west':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'west',  'texture': '#missingno' },                'east':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'east',  'texture': '#missingno' }            }        }    ]}"
      .replaceAll("'", "\"");
   private static final Map<String, String> BUILT_IN_MODELS = Maps.newHashMap();
   private static final Joiner JOINER = Joiner.on(" -> ");
   private final IResourceManager resourceManager;
   private final Map<ResourceLocation, TextureAtlasSprite> sprites = Maps.newHashMap();
   private final Map<ResourceLocation, ModelBlock> models = Maps.newLinkedHashMap();
   private final Map<ModelResourceLocation, VariantList> variants = Maps.newLinkedHashMap();
   private final Map<ModelBlockDefinition, Collection<ModelResourceLocation>> multipartVariantMap = Maps.newLinkedHashMap();
   private final TextureMap textureMap;
   private final BlockModelShapes blockModelShapes;
   private final FaceBakery faceBakery = new FaceBakery();
   private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
   private final RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple<>();
   private static final String EMPTY_MODEL_RAW = "{    'elements': [        {   'from': [0, 0, 0],            'to': [16, 16, 16],            'faces': {                'down': {'uv': [0, 0, 16, 16], 'texture': '' }            }        }    ]}"
      .replaceAll("'", "\"");
   private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize(EMPTY_MODEL_RAW);
   private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize(EMPTY_MODEL_RAW);
   private final Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
   private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
   private final Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();

   public ModelBakery(IResourceManager var1, TextureMap var2, BlockModelShapes var3) {
      this.resourceManager = ☃;
      this.textureMap = ☃;
      this.blockModelShapes = ☃;
   }

   public IRegistry<ModelResourceLocation, IBakedModel> setupModelRegistry() {
      this.loadBlocks();
      this.loadVariantItemModels();
      this.loadModelsCheck();
      this.loadSprites();
      this.makeItemModels();
      this.bakeBlockModels();
      this.bakeItemModels();
      return this.bakedRegistry;
   }

   private void loadBlocks() {
      BlockStateMapper ☃ = this.blockModelShapes.getBlockStateMapper();

      for (Block ☃x : Block.REGISTRY) {
         for (final ResourceLocation ☃xx : ☃.getBlockstateLocations(☃x)) {
            try {
               ModelBlockDefinition ☃xxx = this.getModelBlockDefinition(☃xx);
               Map<IBlockState, ModelResourceLocation> ☃xxxx = ☃.getVariants(☃x);
               if (☃xxx.hasMultipartData()) {
                  Collection<ModelResourceLocation> ☃xxxxx = Sets.newHashSet(☃xxxx.values());
                  ☃xxx.getMultipartData().setStateContainer(☃x.getBlockState());
                  Collection<ModelResourceLocation> ☃xxxxxx = this.multipartVariantMap.get(☃xxx);
                  if (☃xxxxxx == null) {
                     ☃xxxxxx = Lists.newArrayList();
                     this.multipartVariantMap.put(☃xxx, ☃xxxxxx);
                  }

                  ☃xxxxxx.addAll(Lists.newArrayList(Iterables.filter(☃xxxxx, new Predicate<ModelResourceLocation>() {
                     public boolean apply(@Nullable ModelResourceLocation var1) {
                        return ☃.equals(☃);
                     }
                  })));
               }

               for (Entry<IBlockState, ModelResourceLocation> ☃xxxxx : ☃xxxx.entrySet()) {
                  ModelResourceLocation ☃xxxxxx = ☃xxxxx.getValue();
                  if (☃xx.equals(☃xxxxxx)) {
                     try {
                        this.variants.put(☃xxxxxx, ☃xxx.getVariant(☃xxxxxx.getVariant()));
                     } catch (RuntimeException var12) {
                        if (!☃xxx.hasMultipartData()) {
                           LOGGER.warn("Unable to load variant: {} from {}", ☃xxxxxx.getVariant(), ☃xxxxxx);
                        }
                     }
                  }
               }
            } catch (Exception var13) {
               LOGGER.warn("Unable to load definition {}", ☃xx, var13);
            }
         }
      }
   }

   private void loadVariantItemModels() {
      this.variants
         .put(
            MODEL_MISSING,
            new VariantList(Lists.newArrayList(new Variant[]{new Variant(new ResourceLocation(MODEL_MISSING.getPath()), ModelRotation.X0_Y0, false, 1)}))
         );
      this.loadStaticModels();
      this.loadVariantModels();
      this.loadMultipartVariantModels();
      this.loadItemModels();
   }

   private void loadStaticModels() {
      ResourceLocation ☃ = new ResourceLocation("item_frame");
      ModelBlockDefinition ☃x = this.getModelBlockDefinition(☃);
      this.registerVariant(☃x, new ModelResourceLocation(☃, "normal"));
      this.registerVariant(☃x, new ModelResourceLocation(☃, "map"));
   }

   private void registerVariant(ModelBlockDefinition var1, ModelResourceLocation var2) {
      try {
         this.variants.put(☃, ☃.getVariant(☃.getVariant()));
      } catch (RuntimeException var4) {
         if (!☃.hasMultipartData()) {
            LOGGER.warn("Unable to load variant: {} from {}", ☃.getVariant(), ☃);
         }
      }
   }

   private ModelBlockDefinition getModelBlockDefinition(ResourceLocation var1) {
      ResourceLocation ☃ = this.getBlockstateLocation(☃);
      ModelBlockDefinition ☃x = this.blockDefinitions.get(☃);
      if (☃x == null) {
         ☃x = this.loadMultipartMBD(☃, ☃);
         this.blockDefinitions.put(☃, ☃x);
      }

      return ☃x;
   }

   private ModelBlockDefinition loadMultipartMBD(ResourceLocation var1, ResourceLocation var2) {
      List<ModelBlockDefinition> ☃ = Lists.newArrayList();

      try {
         for (IResource ☃x : this.resourceManager.getAllResources(☃)) {
            ☃.add(this.loadModelBlockDefinition(☃, ☃x));
         }
      } catch (IOException var6) {
         throw new RuntimeException("Encountered an exception when loading model definition of model " + ☃, var6);
      }

      return new ModelBlockDefinition(☃);
   }

   private ModelBlockDefinition loadModelBlockDefinition(ResourceLocation var1, IResource var2) {
      InputStream ☃ = null;

      ModelBlockDefinition var4;
      try {
         ☃ = ☃.getInputStream();
         var4 = ModelBlockDefinition.parseFromReader(new InputStreamReader(☃, StandardCharsets.UTF_8));
      } catch (Exception var8) {
         throw new RuntimeException(
            "Encountered an exception when loading model definition of '"
               + ☃
               + "' from: '"
               + ☃.getResourceLocation()
               + "' in resourcepack: '"
               + ☃.getResourcePackName()
               + "'",
            var8
         );
      } finally {
         IOUtils.closeQuietly(☃);
      }

      return var4;
   }

   private ResourceLocation getBlockstateLocation(ResourceLocation var1) {
      return new ResourceLocation(☃.getNamespace(), "blockstates/" + ☃.getPath() + ".json");
   }

   private void loadVariantModels() {
      for (Entry<ModelResourceLocation, VariantList> ☃ : this.variants.entrySet()) {
         this.loadVariantList(☃.getKey(), ☃.getValue());
      }
   }

   private void loadMultipartVariantModels() {
      for (Entry<ModelBlockDefinition, Collection<ModelResourceLocation>> ☃ : this.multipartVariantMap.entrySet()) {
         ModelResourceLocation ☃x = ☃.getValue().iterator().next();

         for (VariantList ☃xx : ☃.getKey().getMultipartVariants()) {
            this.loadVariantList(☃x, ☃xx);
         }
      }
   }

   private void loadVariantList(ModelResourceLocation var1, VariantList var2) {
      for (Variant ☃ : ☃.getVariantList()) {
         ResourceLocation ☃x = ☃.getModelLocation();
         if (this.models.get(☃x) == null) {
            try {
               this.models.put(☃x, this.loadModel(☃x));
            } catch (Exception var7) {
               LOGGER.warn("Unable to load block model: '{}' for variant: '{}': {} ", ☃x, ☃, var7);
            }
         }
      }
   }

   private ModelBlock loadModel(ResourceLocation var1) throws IOException {
      Reader ☃ = null;
      IResource ☃x = null;

      ModelBlock ☃xx;
      try {
         String ☃xxx = ☃.getPath();
         if ("builtin/generated".equals(☃xxx)) {
            return MODEL_GENERATED;
         }

         if (!"builtin/entity".equals(☃xxx)) {
            if (☃xxx.startsWith("builtin/")) {
               String ☃xxxx = ☃xxx.substring("builtin/".length());
               String ☃xxxxx = BUILT_IN_MODELS.get(☃xxxx);
               if (☃xxxxx == null) {
                  throw new FileNotFoundException(☃.toString());
               }

               ☃ = new StringReader(☃xxxxx);
            } else {
               ☃x = this.resourceManager.getResource(this.getModelLocation(☃));
               ☃ = new InputStreamReader(☃x.getInputStream(), StandardCharsets.UTF_8);
            }

            ☃xx = ModelBlock.deserialize(☃);
            ☃xx.name = ☃.toString();
            return ☃xx;
         }

         ☃xx = MODEL_ENTITY;
      } finally {
         IOUtils.closeQuietly(☃);
         IOUtils.closeQuietly(☃x);
      }

      return ☃xx;
   }

   private ResourceLocation getModelLocation(ResourceLocation var1) {
      return new ResourceLocation(☃.getNamespace(), "models/" + ☃.getPath() + ".json");
   }

   private void loadItemModels() {
      this.registerVariantNames();

      for (Item ☃ : Item.REGISTRY) {
         for (String ☃x : this.getVariantNames(☃)) {
            ResourceLocation ☃xx = this.getItemLocation(☃x);
            ResourceLocation ☃xxx = Item.REGISTRY.getNameForObject(☃);
            this.loadItemModel(☃x, ☃xx, ☃xxx);
            if (☃.hasCustomProperties()) {
               ModelBlock ☃xxxx = this.models.get(☃xx);
               if (☃xxxx != null) {
                  for (ResourceLocation ☃xxxxx : ☃xxxx.getOverrideLocations()) {
                     this.loadItemModel(☃xxxxx.toString(), ☃xxxxx, ☃xxx);
                  }
               }
            }
         }
      }
   }

   private void loadItemModel(String var1, ResourceLocation var2, ResourceLocation var3) {
      this.itemLocations.put(☃, ☃);
      if (this.models.get(☃) == null) {
         try {
            ModelBlock ☃ = this.loadModel(☃);
            this.models.put(☃, ☃);
         } catch (Exception var5) {
            LOGGER.warn("Unable to load item model: '{}' for item: '{}'", ☃, ☃, var5);
         }
      }
   }

   private void registerVariantNames() {
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.STONE),
            Lists.newArrayList(new String[]{"stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth"})
         );
      this.variantNames.put(Item.getItemFromBlock(Blocks.DIRT), Lists.newArrayList(new String[]{"dirt", "coarse_dirt", "podzol"}));
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.PLANKS),
            Lists.newArrayList(new String[]{"oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks"})
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.SAPLING),
            Lists.newArrayList(new String[]{"oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling"})
         );
      this.variantNames.put(Item.getItemFromBlock(Blocks.SAND), Lists.newArrayList(new String[]{"sand", "red_sand"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.LOG), Lists.newArrayList(new String[]{"oak_log", "spruce_log", "birch_log", "jungle_log"}));
      this.variantNames
         .put(Item.getItemFromBlock(Blocks.LEAVES), Lists.newArrayList(new String[]{"oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.SPONGE), Lists.newArrayList(new String[]{"sponge", "sponge_wet"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.SANDSTONE), Lists.newArrayList(new String[]{"sandstone", "chiseled_sandstone", "smooth_sandstone"}));
      this.variantNames
         .put(Item.getItemFromBlock(Blocks.RED_SANDSTONE), Lists.newArrayList(new String[]{"red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.TALLGRASS), Lists.newArrayList(new String[]{"dead_bush", "tall_grass", "fern"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.DEADBUSH), Lists.newArrayList(new String[]{"dead_bush"}));
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.WOOL),
            Lists.newArrayList(
               new String[]{
                  "black_wool",
                  "red_wool",
                  "green_wool",
                  "brown_wool",
                  "blue_wool",
                  "purple_wool",
                  "cyan_wool",
                  "silver_wool",
                  "gray_wool",
                  "pink_wool",
                  "lime_wool",
                  "yellow_wool",
                  "light_blue_wool",
                  "magenta_wool",
                  "orange_wool",
                  "white_wool"
               }
            )
         );
      this.variantNames.put(Item.getItemFromBlock(Blocks.YELLOW_FLOWER), Lists.newArrayList(new String[]{"dandelion"}));
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.RED_FLOWER),
            Lists.newArrayList(
               new String[]{"poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy"}
            )
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.STONE_SLAB),
            Lists.newArrayList(
               new String[]{"stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab"}
            )
         );
      this.variantNames.put(Item.getItemFromBlock(Blocks.STONE_SLAB2), Lists.newArrayList(new String[]{"red_sandstone_slab"}));
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.STAINED_GLASS),
            Lists.newArrayList(
               new String[]{
                  "black_stained_glass",
                  "red_stained_glass",
                  "green_stained_glass",
                  "brown_stained_glass",
                  "blue_stained_glass",
                  "purple_stained_glass",
                  "cyan_stained_glass",
                  "silver_stained_glass",
                  "gray_stained_glass",
                  "pink_stained_glass",
                  "lime_stained_glass",
                  "yellow_stained_glass",
                  "light_blue_stained_glass",
                  "magenta_stained_glass",
                  "orange_stained_glass",
                  "white_stained_glass"
               }
            )
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.MONSTER_EGG),
            Lists.newArrayList(
               new String[]{
                  "stone_monster_egg",
                  "cobblestone_monster_egg",
                  "stone_brick_monster_egg",
                  "mossy_brick_monster_egg",
                  "cracked_brick_monster_egg",
                  "chiseled_brick_monster_egg"
               }
            )
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.STONEBRICK),
            Lists.newArrayList(new String[]{"stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick"})
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.WOODEN_SLAB),
            Lists.newArrayList(new String[]{"oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab"})
         );
      this.variantNames.put(Item.getItemFromBlock(Blocks.COBBLESTONE_WALL), Lists.newArrayList(new String[]{"cobblestone_wall", "mossy_cobblestone_wall"}));
      this.variantNames
         .put(Item.getItemFromBlock(Blocks.ANVIL), Lists.newArrayList(new String[]{"anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged"}));
      this.variantNames
         .put(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), Lists.newArrayList(new String[]{"quartz_block", "chiseled_quartz_block", "quartz_column"}));
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY),
            Lists.newArrayList(
               new String[]{
                  "black_stained_hardened_clay",
                  "red_stained_hardened_clay",
                  "green_stained_hardened_clay",
                  "brown_stained_hardened_clay",
                  "blue_stained_hardened_clay",
                  "purple_stained_hardened_clay",
                  "cyan_stained_hardened_clay",
                  "silver_stained_hardened_clay",
                  "gray_stained_hardened_clay",
                  "pink_stained_hardened_clay",
                  "lime_stained_hardened_clay",
                  "yellow_stained_hardened_clay",
                  "light_blue_stained_hardened_clay",
                  "magenta_stained_hardened_clay",
                  "orange_stained_hardened_clay",
                  "white_stained_hardened_clay"
               }
            )
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE),
            Lists.newArrayList(
               new String[]{
                  "black_stained_glass_pane",
                  "red_stained_glass_pane",
                  "green_stained_glass_pane",
                  "brown_stained_glass_pane",
                  "blue_stained_glass_pane",
                  "purple_stained_glass_pane",
                  "cyan_stained_glass_pane",
                  "silver_stained_glass_pane",
                  "gray_stained_glass_pane",
                  "pink_stained_glass_pane",
                  "lime_stained_glass_pane",
                  "yellow_stained_glass_pane",
                  "light_blue_stained_glass_pane",
                  "magenta_stained_glass_pane",
                  "orange_stained_glass_pane",
                  "white_stained_glass_pane"
               }
            )
         );
      this.variantNames.put(Item.getItemFromBlock(Blocks.LEAVES2), Lists.newArrayList(new String[]{"acacia_leaves", "dark_oak_leaves"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.LOG2), Lists.newArrayList(new String[]{"acacia_log", "dark_oak_log"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.PRISMARINE), Lists.newArrayList(new String[]{"prismarine", "prismarine_bricks", "dark_prismarine"}));
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.CARPET),
            Lists.newArrayList(
               new String[]{
                  "black_carpet",
                  "red_carpet",
                  "green_carpet",
                  "brown_carpet",
                  "blue_carpet",
                  "purple_carpet",
                  "cyan_carpet",
                  "silver_carpet",
                  "gray_carpet",
                  "pink_carpet",
                  "lime_carpet",
                  "yellow_carpet",
                  "light_blue_carpet",
                  "magenta_carpet",
                  "orange_carpet",
                  "white_carpet"
               }
            )
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.DOUBLE_PLANT),
            Lists.newArrayList(new String[]{"sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia"})
         );
      this.variantNames.put(Items.COAL, Lists.newArrayList(new String[]{"coal", "charcoal"}));
      this.variantNames.put(Items.FISH, Lists.newArrayList(new String[]{"cod", "salmon", "clownfish", "pufferfish"}));
      this.variantNames.put(Items.COOKED_FISH, Lists.newArrayList(new String[]{"cooked_cod", "cooked_salmon"}));
      this.variantNames
         .put(
            Items.DYE,
            Lists.newArrayList(
               new String[]{
                  "dye_black",
                  "dye_red",
                  "dye_green",
                  "dye_brown",
                  "dye_blue",
                  "dye_purple",
                  "dye_cyan",
                  "dye_silver",
                  "dye_gray",
                  "dye_pink",
                  "dye_lime",
                  "dye_yellow",
                  "dye_light_blue",
                  "dye_magenta",
                  "dye_orange",
                  "dye_white"
               }
            )
         );
      this.variantNames.put(Items.POTIONITEM, Lists.newArrayList(new String[]{"bottle_drinkable"}));
      this.variantNames
         .put(Items.SKULL, Lists.newArrayList(new String[]{"skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper", "skull_dragon"}));
      this.variantNames.put(Items.SPLASH_POTION, Lists.newArrayList(new String[]{"bottle_splash"}));
      this.variantNames.put(Items.LINGERING_POTION, Lists.newArrayList(new String[]{"bottle_lingering"}));
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.CONCRETE),
            Lists.newArrayList(
               new String[]{
                  "black_concrete",
                  "red_concrete",
                  "green_concrete",
                  "brown_concrete",
                  "blue_concrete",
                  "purple_concrete",
                  "cyan_concrete",
                  "silver_concrete",
                  "gray_concrete",
                  "pink_concrete",
                  "lime_concrete",
                  "yellow_concrete",
                  "light_blue_concrete",
                  "magenta_concrete",
                  "orange_concrete",
                  "white_concrete"
               }
            )
         );
      this.variantNames
         .put(
            Item.getItemFromBlock(Blocks.CONCRETE_POWDER),
            Lists.newArrayList(
               new String[]{
                  "black_concrete_powder",
                  "red_concrete_powder",
                  "green_concrete_powder",
                  "brown_concrete_powder",
                  "blue_concrete_powder",
                  "purple_concrete_powder",
                  "cyan_concrete_powder",
                  "silver_concrete_powder",
                  "gray_concrete_powder",
                  "pink_concrete_powder",
                  "lime_concrete_powder",
                  "yellow_concrete_powder",
                  "light_blue_concrete_powder",
                  "magenta_concrete_powder",
                  "orange_concrete_powder",
                  "white_concrete_powder"
               }
            )
         );
      this.variantNames.put(Item.getItemFromBlock(Blocks.AIR), Collections.emptyList());
      this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE_GATE), Lists.newArrayList(new String[]{"oak_fence_gate"}));
      this.variantNames.put(Item.getItemFromBlock(Blocks.OAK_FENCE), Lists.newArrayList(new String[]{"oak_fence"}));
      this.variantNames.put(Items.OAK_DOOR, Lists.newArrayList(new String[]{"oak_door"}));
      this.variantNames.put(Items.BOAT, Lists.newArrayList(new String[]{"oak_boat"}));
      this.variantNames.put(Items.TOTEM_OF_UNDYING, Lists.newArrayList(new String[]{"totem"}));
   }

   private List<String> getVariantNames(Item var1) {
      List<String> ☃ = this.variantNames.get(☃);
      if (☃ == null) {
         ☃ = Collections.singletonList(Item.REGISTRY.getNameForObject(☃).toString());
      }

      return ☃;
   }

   private ResourceLocation getItemLocation(String var1) {
      ResourceLocation ☃ = new ResourceLocation(☃);
      return new ResourceLocation(☃.getNamespace(), "item/" + ☃.getPath());
   }

   private void bakeBlockModels() {
      for (ModelResourceLocation ☃ : this.variants.keySet()) {
         IBakedModel ☃x = this.createRandomModelForVariantList(this.variants.get(☃), ☃.toString());
         if (☃x != null) {
            this.bakedRegistry.putObject(☃, ☃x);
         }
      }

      for (Entry<ModelBlockDefinition, Collection<ModelResourceLocation>> ☃x : this.multipartVariantMap.entrySet()) {
         ModelBlockDefinition ☃xx = ☃x.getKey();
         Multipart ☃xxx = ☃xx.getMultipartData();
         String ☃xxxx = Block.REGISTRY.getNameForObject(☃xxx.getStateContainer().getBlock()).toString();
         MultipartBakedModel.Builder ☃xxxxx = new MultipartBakedModel.Builder();

         for (Selector ☃xxxxxx : ☃xxx.getSelectors()) {
            IBakedModel ☃xxxxxxx = this.createRandomModelForVariantList(☃xxxxxx.getVariantList(), "selector of " + ☃xxxx);
            if (☃xxxxxxx != null) {
               ☃xxxxx.putModel(☃xxxxxx.getPredicate(☃xxx.getStateContainer()), ☃xxxxxxx);
            }
         }

         IBakedModel ☃xxxxxxx = ☃xxxxx.makeMultipartModel();

         for (ModelResourceLocation ☃xxxxxxxx : ☃x.getValue()) {
            if (!☃xx.hasVariant(☃xxxxxxxx.getVariant())) {
               this.bakedRegistry.putObject(☃xxxxxxxx, ☃xxxxxxx);
            }
         }
      }
   }

   @Nullable
   private IBakedModel createRandomModelForVariantList(VariantList var1, String var2) {
      if (☃.getVariantList().isEmpty()) {
         return null;
      } else {
         WeightedBakedModel.Builder ☃ = new WeightedBakedModel.Builder();
         int ☃x = 0;

         for (Variant ☃xx : ☃.getVariantList()) {
            ModelBlock ☃xxx = this.models.get(☃xx.getModelLocation());
            if (☃xxx == null || !☃xxx.isResolved()) {
               LOGGER.warn("Missing model for: {}", ☃);
            } else if (☃xxx.getElements().isEmpty()) {
               LOGGER.warn("Missing elements for: {}", ☃);
            } else {
               IBakedModel ☃xxxx = this.bakeModel(☃xxx, ☃xx.getRotation(), ☃xx.isUvLock());
               if (☃xxxx != null) {
                  ☃x++;
                  ☃.add(☃xxxx, ☃xx.getWeight());
               }
            }
         }

         IBakedModel ☃xxx = null;
         if (☃x == 0) {
            LOGGER.warn("No weighted models for: {}", ☃);
         } else if (☃x == 1) {
            ☃xxx = ☃.first();
         } else {
            ☃xxx = ☃.build();
         }

         return ☃xxx;
      }
   }

   private void bakeItemModels() {
      for (Entry<String, ResourceLocation> ☃ : this.itemLocations.entrySet()) {
         ResourceLocation ☃x = ☃.getValue();
         ModelResourceLocation ☃xx = new ModelResourceLocation(☃.getKey(), "inventory");
         ModelBlock ☃xxx = this.models.get(☃x);
         if (☃xxx == null || !☃xxx.isResolved()) {
            LOGGER.warn("Missing model for: {}", ☃x);
         } else if (☃xxx.getElements().isEmpty()) {
            LOGGER.warn("Missing elements for: {}", ☃x);
         } else if (this.isCustomRenderer(☃xxx)) {
            this.bakedRegistry.putObject(☃xx, new BuiltInModel(☃xxx.getAllTransforms(), ☃xxx.createOverrides()));
         } else {
            IBakedModel ☃xxxx = this.bakeModel(☃xxx, ModelRotation.X0_Y0, false);
            if (☃xxxx != null) {
               this.bakedRegistry.putObject(☃xx, ☃xxxx);
            }
         }
      }
   }

   private Set<ResourceLocation> getVariantsTextureLocations() {
      Set<ResourceLocation> ☃ = Sets.newHashSet();
      List<ModelResourceLocation> ☃x = Lists.newArrayList(this.variants.keySet());
      Collections.sort(☃x, new Comparator<ModelResourceLocation>() {
         public int compare(ModelResourceLocation var1, ModelResourceLocation var2) {
            return ☃.toString().compareTo(☃.toString());
         }
      });

      for (ModelResourceLocation ☃xx : ☃x) {
         VariantList ☃xxx = this.variants.get(☃xx);

         for (Variant ☃xxxx : ☃xxx.getVariantList()) {
            ModelBlock ☃xxxxx = this.models.get(☃xxxx.getModelLocation());
            if (☃xxxxx == null) {
               LOGGER.warn("Missing model for: {}", ☃xx);
            } else {
               ☃.addAll(this.getTextureLocations(☃xxxxx));
            }
         }
      }

      for (ModelBlockDefinition ☃xx : this.multipartVariantMap.keySet()) {
         for (VariantList ☃xxx : ☃xx.getMultipartData().getVariants()) {
            for (Variant ☃xxxxx : ☃xxx.getVariantList()) {
               ModelBlock ☃xxxxxx = this.models.get(☃xxxxx.getModelLocation());
               if (☃xxxxxx == null) {
                  LOGGER.warn("Missing model for: {}", Block.REGISTRY.getNameForObject(☃xx.getMultipartData().getStateContainer().getBlock()));
               } else {
                  ☃.addAll(this.getTextureLocations(☃xxxxxx));
               }
            }
         }
      }

      ☃.addAll(LOCATIONS_BUILTIN_TEXTURES);
      return ☃;
   }

   @Nullable
   private IBakedModel bakeModel(ModelBlock var1, ModelRotation var2, boolean var3) {
      TextureAtlasSprite ☃ = this.sprites.get(new ResourceLocation(☃.resolveTextureName("particle")));
      SimpleBakedModel.Builder ☃x = new SimpleBakedModel.Builder(☃, ☃.createOverrides()).setTexture(☃);
      if (☃.getElements().isEmpty()) {
         return null;
      } else {
         for (BlockPart ☃xx : ☃.getElements()) {
            for (EnumFacing ☃xxx : ☃xx.mapFaces.keySet()) {
               BlockPartFace ☃xxxx = ☃xx.mapFaces.get(☃xxx);
               TextureAtlasSprite ☃xxxxx = this.sprites.get(new ResourceLocation(☃.resolveTextureName(☃xxxx.texture)));
               if (☃xxxx.cullFace == null) {
                  ☃x.addGeneralQuad(this.makeBakedQuad(☃xx, ☃xxxx, ☃xxxxx, ☃xxx, ☃, ☃));
               } else {
                  ☃x.addFaceQuad(☃.rotateFace(☃xxxx.cullFace), this.makeBakedQuad(☃xx, ☃xxxx, ☃xxxxx, ☃xxx, ☃, ☃));
               }
            }
         }

         return ☃x.makeBakedModel();
      }
   }

   private BakedQuad makeBakedQuad(BlockPart var1, BlockPartFace var2, TextureAtlasSprite var3, EnumFacing var4, ModelRotation var5, boolean var6) {
      return this.faceBakery.makeBakedQuad(☃.positionFrom, ☃.positionTo, ☃, ☃, ☃, ☃, ☃.partRotation, ☃, ☃.shade);
   }

   private void loadModelsCheck() {
      this.loadModels();

      for (ModelBlock ☃ : this.models.values()) {
         ☃.getParentFromMap(this.models);
      }

      ModelBlock.checkModelHierarchy(this.models);
   }

   private void loadModels() {
      Deque<ResourceLocation> ☃ = Queues.newArrayDeque();
      Set<ResourceLocation> ☃x = Sets.newHashSet();

      for (ResourceLocation ☃xx : this.models.keySet()) {
         ☃x.add(☃xx);
         this.addModelParentLocation(☃, ☃x, this.models.get(☃xx));
      }

      while (!☃.isEmpty()) {
         ResourceLocation ☃xx = ☃.pop();

         try {
            if (this.models.get(☃xx) != null) {
               continue;
            }

            ModelBlock ☃xxx = this.loadModel(☃xx);
            this.models.put(☃xx, ☃xxx);
            this.addModelParentLocation(☃, ☃x, ☃xxx);
         } catch (Exception var5) {
            LOGGER.warn("In parent chain: {}; unable to load model: '{}'", JOINER.join(this.getParentPath(☃xx)), ☃xx, var5);
         }

         ☃x.add(☃xx);
      }
   }

   private void addModelParentLocation(Deque<ResourceLocation> var1, Set<ResourceLocation> var2, ModelBlock var3) {
      ResourceLocation ☃ = ☃.getParentLocation();
      if (☃ != null && !☃.contains(☃)) {
         ☃.add(☃);
      }
   }

   private List<ResourceLocation> getParentPath(ResourceLocation var1) {
      List<ResourceLocation> ☃ = Lists.newArrayList(new ResourceLocation[]{☃});
      ResourceLocation ☃x = ☃;

      while ((☃x = this.getParentLocation(☃x)) != null) {
         ☃.add(0, ☃x);
      }

      return ☃;
   }

   @Nullable
   private ResourceLocation getParentLocation(ResourceLocation var1) {
      for (Entry<ResourceLocation, ModelBlock> ☃ : this.models.entrySet()) {
         ModelBlock ☃x = ☃.getValue();
         if (☃x != null && ☃.equals(☃x.getParentLocation())) {
            return ☃.getKey();
         }
      }

      return null;
   }

   private Set<ResourceLocation> getTextureLocations(ModelBlock var1) {
      Set<ResourceLocation> ☃ = Sets.newHashSet();

      for (BlockPart ☃x : ☃.getElements()) {
         for (BlockPartFace ☃xx : ☃x.mapFaces.values()) {
            ResourceLocation ☃xxx = new ResourceLocation(☃.resolveTextureName(☃xx.texture));
            ☃.add(☃xxx);
         }
      }

      ☃.add(new ResourceLocation(☃.resolveTextureName("particle")));
      return ☃;
   }

   private void loadSprites() {
      final Set<ResourceLocation> ☃ = this.getVariantsTextureLocations();
      ☃.addAll(this.getItemsTextureLocations());
      ☃.remove(TextureMap.LOCATION_MISSING_TEXTURE);
      ITextureMapPopulator ☃x = new ITextureMapPopulator() {
         @Override
         public void registerSprites(TextureMap var1x) {
            for (ResourceLocation ☃xx : ☃) {
               TextureAtlasSprite ☃x = ☃.registerSprite(☃xx);
               ModelBakery.this.sprites.put(☃xx, ☃x);
            }
         }
      };
      this.textureMap.loadSprites(this.resourceManager, ☃x);
      this.sprites.put(new ResourceLocation("missingno"), this.textureMap.getMissingSprite());
   }

   private Set<ResourceLocation> getItemsTextureLocations() {
      Set<ResourceLocation> ☃ = Sets.newHashSet();

      for (ResourceLocation ☃x : this.itemLocations.values()) {
         ModelBlock ☃xx = this.models.get(☃x);
         if (☃xx != null) {
            ☃.add(new ResourceLocation(☃xx.resolveTextureName("particle")));
            if (this.hasItemModel(☃xx)) {
               for (String ☃xxx : ItemModelGenerator.LAYERS) {
                  ☃.add(new ResourceLocation(☃xx.resolveTextureName(☃xxx)));
               }
            } else if (!this.isCustomRenderer(☃xx)) {
               for (BlockPart ☃xxx : ☃xx.getElements()) {
                  for (BlockPartFace ☃xxxx : ☃xxx.mapFaces.values()) {
                     ResourceLocation ☃xxxxx = new ResourceLocation(☃xx.resolveTextureName(☃xxxx.texture));
                     ☃.add(☃xxxxx);
                  }
               }
            }
         }
      }

      return ☃;
   }

   private boolean hasItemModel(@Nullable ModelBlock var1) {
      return ☃ == null ? false : ☃.getRootModel() == MODEL_GENERATED;
   }

   private boolean isCustomRenderer(@Nullable ModelBlock var1) {
      if (☃ == null) {
         return false;
      } else {
         ModelBlock ☃ = ☃.getRootModel();
         return ☃ == MODEL_ENTITY;
      }
   }

   private void makeItemModels() {
      for (ResourceLocation ☃ : this.itemLocations.values()) {
         ModelBlock ☃x = this.models.get(☃);
         if (this.hasItemModel(☃x)) {
            ModelBlock ☃xx = this.makeItemModel(☃x);
            if (☃xx != null) {
               ☃xx.name = ☃.toString();
            }

            this.models.put(☃, ☃xx);
         } else if (this.isCustomRenderer(☃x)) {
            this.models.put(☃, ☃x);
         }
      }

      for (TextureAtlasSprite ☃x : this.sprites.values()) {
         if (!☃x.hasAnimationMetadata()) {
            ☃x.clearFramesTextureData();
         }
      }
   }

   private ModelBlock makeItemModel(ModelBlock var1) {
      return this.itemModelGenerator.makeItemModel(this.textureMap, ☃);
   }

   static {
      BUILT_IN_MODELS.put("missing", MISSING_MODEL_MESH);
      MODEL_GENERATED.name = "generation marker";
      MODEL_ENTITY.name = "block entity marker";
   }
}
