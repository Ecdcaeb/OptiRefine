/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Queues
 *  com.google.common.collect.Sets
 *  java.io.Closeable
 *  java.io.FileNotFoundException
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.io.StringReader
 *  java.lang.Exception
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.nio.charset.StandardCharsets
 *  java.util.ArrayDeque
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.Deque
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  javax.annotation.Nullable
 *  javax.vecmath.Matrix4f
 *  net.minecraft.block.Block
 *  net.minecraft.client.renderer.BlockModelShapes
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.BlockPart
 *  net.minecraft.client.renderer.block.model.BlockPartFace
 *  net.minecraft.client.renderer.block.model.BuiltInModel
 *  net.minecraft.client.renderer.block.model.FaceBakery
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemModelGenerator
 *  net.minecraft.client.renderer.block.model.ModelBakery$3
 *  net.minecraft.client.renderer.block.model.ModelBlock
 *  net.minecraft.client.renderer.block.model.ModelBlockDefinition
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.client.renderer.block.model.ModelRotation
 *  net.minecraft.client.renderer.block.model.MultipartBakedModel$Builder
 *  net.minecraft.client.renderer.block.model.SimpleBakedModel$Builder
 *  net.minecraft.client.renderer.block.model.Variant
 *  net.minecraft.client.renderer.block.model.VariantList
 *  net.minecraft.client.renderer.block.model.WeightedBakedModel$Builder
 *  net.minecraft.client.renderer.block.model.multipart.Multipart
 *  net.minecraft.client.renderer.block.model.multipart.Selector
 *  net.minecraft.client.renderer.block.statemap.BlockStateMapper
 *  net.minecraft.client.renderer.texture.ITextureMapPopulator
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.registry.IRegistry
 *  net.minecraft.util.registry.RegistrySimple
 *  net.minecraftforge.common.model.ITransformation
 *  net.minecraftforge.common.model.TRSRTransformation
 *  net.minecraftforge.registries.IRegistryDelegate
 *  net.optifine.CustomItems
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorField
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.util.StrUtils
 *  net.optifine.util.TextureUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BuiltInModel;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.MultipartBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.client.renderer.block.model.WeightedBakedModel;
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
import net.minecraftforge.common.model.ITransformation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.registries.IRegistryDelegate;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.util.StrUtils;
import net.optifine.util.TextureUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBakery {
    private static final Set<ResourceLocation> LOCATIONS_BUILTIN_TEXTURES = Sets.newHashSet((Object[])new ResourceLocation[]{new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/lava_flow"), new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/water_overlay"), new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9"), new ResourceLocation("items/empty_armor_slot_helmet"), new ResourceLocation("items/empty_armor_slot_chestplate"), new ResourceLocation("items/empty_armor_slot_leggings"), new ResourceLocation("items/empty_armor_slot_boots"), new ResourceLocation("items/empty_armor_slot_shield"), new ResourceLocation("blocks/shulker_top_white"), new ResourceLocation("blocks/shulker_top_orange"), new ResourceLocation("blocks/shulker_top_magenta"), new ResourceLocation("blocks/shulker_top_light_blue"), new ResourceLocation("blocks/shulker_top_yellow"), new ResourceLocation("blocks/shulker_top_lime"), new ResourceLocation("blocks/shulker_top_pink"), new ResourceLocation("blocks/shulker_top_gray"), new ResourceLocation("blocks/shulker_top_silver"), new ResourceLocation("blocks/shulker_top_cyan"), new ResourceLocation("blocks/shulker_top_purple"), new ResourceLocation("blocks/shulker_top_blue"), new ResourceLocation("blocks/shulker_top_brown"), new ResourceLocation("blocks/shulker_top_green"), new ResourceLocation("blocks/shulker_top_red"), new ResourceLocation("blocks/shulker_top_black")});
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final ModelResourceLocation MODEL_MISSING = new ModelResourceLocation("builtin/missing", "missing");
    private static final String MISSING_MODEL_MESH = "{    'textures': {       'particle': 'missingno',       'missingno': 'missingno'    },    'elements': [         {  'from': [ 0, 0, 0 ],            'to': [ 16, 16, 16 ],            'faces': {                'down':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'down',  'texture': '#missingno' },                'up':    { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'up',    'texture': '#missingno' },                'north': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'north', 'texture': '#missingno' },                'south': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'south', 'texture': '#missingno' },                'west':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'west',  'texture': '#missingno' },                'east':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'east',  'texture': '#missingno' }            }        }    ]}".replaceAll("'", "\"");
    private static final Map<String, String> BUILT_IN_MODELS = Maps.newHashMap();
    private static final Joiner JOINER = Joiner.on((String)" -> ");
    private final IResourceManager resourceManager;
    private final Map<ResourceLocation, TextureAtlasSprite> sprites = Maps.newHashMap();
    private final Map<ResourceLocation, ModelBlock> models = Maps.newLinkedHashMap();
    private final Map<ModelResourceLocation, VariantList> variants = Maps.newLinkedHashMap();
    private final Map<ModelBlockDefinition, Collection<ModelResourceLocation>> multipartVariantMap = Maps.newLinkedHashMap();
    private final TextureMap textureMap;
    private final BlockModelShapes blockModelShapes;
    private final FaceBakery faceBakery = new FaceBakery();
    private final ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private final RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple();
    private static final String EMPTY_MODEL_RAW = "{    'elements': [        {   'from': [0, 0, 0],            'to': [16, 16, 16],            'faces': {                'down': {'uv': [0, 0, 16, 16], 'texture': '' }            }        }    ]}".replaceAll("'", "\"");
    private static final ModelBlock MODEL_GENERATED = ModelBlock.deserialize((String)EMPTY_MODEL_RAW);
    private static final ModelBlock MODEL_ENTITY = ModelBlock.deserialize((String)EMPTY_MODEL_RAW);
    private final Map<String, ResourceLocation> itemLocations = Maps.newLinkedHashMap();
    private final Map<ResourceLocation, ModelBlockDefinition> blockDefinitions = Maps.newHashMap();
    private final Map<Item, List<String>> variantNames = Maps.newIdentityHashMap();
    private static Map<IRegistryDelegate<Item>, Set<String>> customVariantNames;

    public ModelBakery(IResourceManager resourceManagerIn, TextureMap textureMapIn, BlockModelShapes blockModelShapesIn) {
        this.resourceManager = resourceManagerIn;
        this.textureMap = textureMapIn;
        this.blockModelShapes = blockModelShapesIn;
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
        BlockStateMapper blockstatemapper = this.blockModelShapes.getBlockStateMapper();
        for (Block block : Block.REGISTRY) {
            for (ResourceLocation resourcelocation : blockstatemapper.getBlockstateLocations(block)) {
                try {
                    this.loadBlock(blockstatemapper, block, resourcelocation);
                }
                catch (Exception exception) {
                    LOGGER.warn("Unable to load definition " + resourcelocation, (Throwable)exception);
                }
            }
        }
    }

    protected void loadBlock(BlockStateMapper blockstatemapper, Block block, ResourceLocation resourcelocation) {
        ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(resourcelocation);
        Map map = blockstatemapper.getVariants(block);
        if (modelblockdefinition.hasMultipartData()) {
            HashSet collection = Sets.newHashSet((Iterable)map.values());
            modelblockdefinition.getMultipartData().setStateContainer(block.getBlockState());
            Collection collection1 = (Collection)this.multipartVariantMap.get((Object)modelblockdefinition);
            if (collection1 == null) {
                collection1 = Lists.newArrayList();
            }
            collection1.addAll((Collection)Lists.newArrayList((Iterable)Iterables.filter((Iterable)collection, (Predicate)new /* Unavailable Anonymous Inner Class!! */)));
            this.registerMultipartVariant(modelblockdefinition, (Collection<ModelResourceLocation>)collection1);
        }
        for (Map.Entry entry : map.entrySet()) {
            ModelResourceLocation modelresourcelocation = (ModelResourceLocation)entry.getValue();
            if (!resourcelocation.equals((Object)modelresourcelocation)) continue;
            try {
                if (Reflector.ForgeItem_delegate.exists()) {
                    this.registerVariant(modelblockdefinition, modelresourcelocation);
                    continue;
                }
                this.variants.put((Object)modelresourcelocation, (Object)modelblockdefinition.getVariant(modelresourcelocation.getVariant()));
            }
            catch (RuntimeException var12) {
                if (modelblockdefinition.hasMultipartData()) continue;
                LOGGER.warn("Unable to load variant: " + modelresourcelocation.getVariant() + " from " + modelresourcelocation, (Throwable)var12);
            }
        }
    }

    private void loadVariantItemModels() {
        this.variants.put((Object)MODEL_MISSING, (Object)new VariantList((List)Lists.newArrayList((Object[])new Variant[]{new Variant(new ResourceLocation(MODEL_MISSING.a()), ModelRotation.X0_Y0, false, 1)})));
        this.loadStaticModels();
        this.loadVariantModels();
        this.loadMultipartVariantModels();
        this.loadItemModels();
        CustomItems.update();
        CustomItems.loadModels((ModelBakery)this);
    }

    private void loadStaticModels() {
        ResourceLocation resourcelocation = new ResourceLocation("item_frame");
        ModelBlockDefinition modelblockdefinition = this.getModelBlockDefinition(resourcelocation);
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "normal"));
        this.registerVariant(modelblockdefinition, new ModelResourceLocation(resourcelocation, "map"));
    }

    private void registerVariant(ModelBlockDefinition blockstateDefinition, ModelResourceLocation location) {
        block2: {
            try {
                this.variants.put((Object)location, (Object)blockstateDefinition.getVariant(location.getVariant()));
            }
            catch (RuntimeException var4) {
                if (blockstateDefinition.hasMultipartData()) break block2;
                LOGGER.warn("Unable to load variant: {} from {}", (Object)location.getVariant(), (Object)location);
            }
        }
    }

    private ModelBlockDefinition getModelBlockDefinition(ResourceLocation location) {
        ResourceLocation resourcelocation = this.getBlockstateLocation(location);
        ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)this.blockDefinitions.get((Object)resourcelocation);
        if (modelblockdefinition == null) {
            modelblockdefinition = this.loadMultipartMBD(location, resourcelocation);
            this.blockDefinitions.put((Object)resourcelocation, (Object)modelblockdefinition);
        }
        return modelblockdefinition;
    }

    private ModelBlockDefinition loadMultipartMBD(ResourceLocation location, ResourceLocation fileIn) {
        ArrayList list = Lists.newArrayList();
        try {
            for (IResource iresource : this.resourceManager.getAllResources(fileIn)) {
                list.add((Object)this.loadModelBlockDefinition(location, iresource));
            }
        }
        catch (IOException ioexception) {
            throw new RuntimeException("Encountered an exception when loading model definition of model " + fileIn, (Throwable)ioexception);
        }
        return new ModelBlockDefinition((List)list);
    }

    private ModelBlockDefinition loadModelBlockDefinition(ResourceLocation location, IResource resource) {
        ModelBlockDefinition lvt_4_1_;
        InputStream inputstream = null;
        try {
            inputstream = resource.getInputStream();
            lvt_4_1_ = Reflector.ForgeModelBlockDefinition_parseFromReader2.exists() ? (ModelBlockDefinition)Reflector.call((ReflectorMethod)Reflector.ForgeModelBlockDefinition_parseFromReader2, (Object[])new Object[]{new InputStreamReader(inputstream, StandardCharsets.UTF_8), location}) : ModelBlockDefinition.parseFromReader((Reader)new InputStreamReader(inputstream, StandardCharsets.UTF_8));
        }
        catch (Exception exception) {
            throw new RuntimeException("Encountered an exception when loading model definition of '" + location + "' from: '" + resource.getResourceLocation() + "' in resourcepack: '" + resource.getResourcePackName() + "'", (Throwable)exception);
        }
        finally {
            IOUtils.closeQuietly((InputStream)inputstream);
        }
        return lvt_4_1_;
    }

    private ResourceLocation getBlockstateLocation(ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), "blockstates/" + location.getPath() + ".json");
    }

    private void loadVariantModels() {
        for (Map.Entry entry : this.variants.entrySet()) {
            this.loadVariantList((ModelResourceLocation)entry.getKey(), (VariantList)entry.getValue());
        }
    }

    private void loadMultipartVariantModels() {
        for (Map.Entry entry : this.multipartVariantMap.entrySet()) {
            ModelResourceLocation modelresourcelocation = (ModelResourceLocation)((Collection)entry.getValue()).iterator().next();
            for (VariantList variantlist : ((ModelBlockDefinition)entry.getKey()).getMultipartVariants()) {
                this.loadVariantList(modelresourcelocation, variantlist);
            }
        }
    }

    private void loadVariantList(ModelResourceLocation p_188638_1_, VariantList p_188638_2_) {
        for (Variant variant : p_188638_2_.getVariantList()) {
            ResourceLocation resourcelocation = variant.getModelLocation();
            if (this.models.get((Object)resourcelocation) != null) continue;
            try {
                this.models.put((Object)resourcelocation, (Object)this.loadModel(resourcelocation));
            }
            catch (Exception exception) {
                LOGGER.warn("Unable to load block model: '{}' for variant: '{}': {} ", (Object)resourcelocation, (Object)p_188638_1_, (Object)exception);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ModelBlock loadModel(ResourceLocation location) throws IOException {
        ModelBlock lvt_5_2_;
        IResource iresource;
        InputStreamReader reader;
        block8: {
            String s;
            block9: {
                ModelBlock lvt_5_2_2;
                reader = null;
                iresource = null;
                s = location.getPath();
                if ("builtin/generated".equals((Object)s)) break block8;
                if (!"builtin/entity".equals((Object)s)) break block9;
                ModelBlock modelBlock = lvt_5_2_2 = MODEL_ENTITY;
                IOUtils.closeQuietly(reader);
                IOUtils.closeQuietly(iresource);
                return modelBlock;
            }
            if (s.startsWith("builtin/")) {
                String s2 = s.substring("builtin/".length());
                String s1 = (String)BUILT_IN_MODELS.get((Object)s2);
                if (s1 == null) {
                    throw new FileNotFoundException(location.toString());
                }
                reader = new StringReader(s1);
            } else {
                location = this.getModelLocation(location);
                iresource = this.resourceManager.getResource(location);
                reader = new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8);
            }
            ModelBlock lvt_5_2_3 = ModelBlock.deserialize((Reader)reader);
            lvt_5_2_3.name = location.toString();
            ModelBlock modelblock1 = lvt_5_2_3;
            String basePath = TextureUtils.getBasePath((String)location.getPath());
            ModelBakery.fixModelLocations(modelblock1, basePath);
            ModelBlock modelBlock = modelblock1;
            IOUtils.closeQuietly((Reader)reader);
            IOUtils.closeQuietly((Closeable)iresource);
            return modelBlock;
        }
        try {
            lvt_5_2_ = MODEL_GENERATED;
        }
        finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(iresource);
        }
        return lvt_5_2_;
    }

    private ResourceLocation getModelLocation(ResourceLocation location) {
        String path = location.getPath();
        if (path.startsWith("mcpatcher") || path.startsWith("optifine")) {
            if (!path.endsWith(".json")) {
                location = new ResourceLocation(location.getNamespace(), path + ".json");
            }
            return location;
        }
        return new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
    }

    private void loadItemModels() {
        this.registerVariantNames();
        for (Item item : Item.REGISTRY) {
            for (String s : this.getVariantNames(item)) {
                ModelBlock modelblock;
                ResourceLocation resourcelocation = this.getItemLocation(s);
                ResourceLocation resourcelocation1 = (ResourceLocation)Item.REGISTRY.getNameForObject((Object)item);
                this.loadItemModel(s, resourcelocation, resourcelocation1);
                if (!item.hasCustomProperties() || (modelblock = (ModelBlock)this.models.get((Object)resourcelocation)) == null) continue;
                for (ResourceLocation resourcelocation2 : modelblock.getOverrideLocations()) {
                    this.loadItemModel(resourcelocation2.toString(), resourcelocation2, resourcelocation1);
                }
            }
        }
    }

    public void loadItemModel(String variantName, ResourceLocation location, ResourceLocation itemName) {
        this.itemLocations.put((Object)variantName, (Object)location);
        if (this.models.get((Object)location) == null) {
            try {
                ModelBlock modelblock = this.loadModel(location);
                this.models.put((Object)location, (Object)modelblock);
            }
            catch (Exception exception) {
                LOGGER.warn("Unable to load item model: '{}' for item: '{}'", new Object[]{location, itemName});
                LOGGER.warn(exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }

    private void registerVariantNames() {
        this.variantNames.clear();
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.STONE), (Object)Lists.newArrayList((Object[])new String[]{"stone", "granite", "granite_smooth", "diorite", "diorite_smooth", "andesite", "andesite_smooth"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.DIRT), (Object)Lists.newArrayList((Object[])new String[]{"dirt", "coarse_dirt", "podzol"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.PLANKS), (Object)Lists.newArrayList((Object[])new String[]{"oak_planks", "spruce_planks", "birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.SAPLING), (Object)Lists.newArrayList((Object[])new String[]{"oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.SAND), (Object)Lists.newArrayList((Object[])new String[]{"sand", "red_sand"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.LOG), (Object)Lists.newArrayList((Object[])new String[]{"oak_log", "spruce_log", "birch_log", "jungle_log"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.LEAVES), (Object)Lists.newArrayList((Object[])new String[]{"oak_leaves", "spruce_leaves", "birch_leaves", "jungle_leaves"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.SPONGE), (Object)Lists.newArrayList((Object[])new String[]{"sponge", "sponge_wet"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.SANDSTONE), (Object)Lists.newArrayList((Object[])new String[]{"sandstone", "chiseled_sandstone", "smooth_sandstone"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.RED_SANDSTONE), (Object)Lists.newArrayList((Object[])new String[]{"red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.TALLGRASS), (Object)Lists.newArrayList((Object[])new String[]{"dead_bush", "tall_grass", "fern"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.DEADBUSH), (Object)Lists.newArrayList((Object[])new String[]{"dead_bush"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.WOOL), (Object)Lists.newArrayList((Object[])new String[]{"black_wool", "red_wool", "green_wool", "brown_wool", "blue_wool", "purple_wool", "cyan_wool", "silver_wool", "gray_wool", "pink_wool", "lime_wool", "yellow_wool", "light_blue_wool", "magenta_wool", "orange_wool", "white_wool"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.YELLOW_FLOWER), (Object)Lists.newArrayList((Object[])new String[]{"dandelion"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.RED_FLOWER), (Object)Lists.newArrayList((Object[])new String[]{"poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.STONE_SLAB), (Object)Lists.newArrayList((Object[])new String[]{"stone_slab", "sandstone_slab", "cobblestone_slab", "brick_slab", "stone_brick_slab", "nether_brick_slab", "quartz_slab"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.STONE_SLAB2), (Object)Lists.newArrayList((Object[])new String[]{"red_sandstone_slab"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.STAINED_GLASS), (Object)Lists.newArrayList((Object[])new String[]{"black_stained_glass", "red_stained_glass", "green_stained_glass", "brown_stained_glass", "blue_stained_glass", "purple_stained_glass", "cyan_stained_glass", "silver_stained_glass", "gray_stained_glass", "pink_stained_glass", "lime_stained_glass", "yellow_stained_glass", "light_blue_stained_glass", "magenta_stained_glass", "orange_stained_glass", "white_stained_glass"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.MONSTER_EGG), (Object)Lists.newArrayList((Object[])new String[]{"stone_monster_egg", "cobblestone_monster_egg", "stone_brick_monster_egg", "mossy_brick_monster_egg", "cracked_brick_monster_egg", "chiseled_brick_monster_egg"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.STONEBRICK), (Object)Lists.newArrayList((Object[])new String[]{"stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.WOODEN_SLAB), (Object)Lists.newArrayList((Object[])new String[]{"oak_slab", "spruce_slab", "birch_slab", "jungle_slab", "acacia_slab", "dark_oak_slab"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.COBBLESTONE_WALL), (Object)Lists.newArrayList((Object[])new String[]{"cobblestone_wall", "mossy_cobblestone_wall"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.ANVIL), (Object)Lists.newArrayList((Object[])new String[]{"anvil_intact", "anvil_slightly_damaged", "anvil_very_damaged"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.QUARTZ_BLOCK), (Object)Lists.newArrayList((Object[])new String[]{"quartz_block", "chiseled_quartz_block", "quartz_column"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.STAINED_HARDENED_CLAY), (Object)Lists.newArrayList((Object[])new String[]{"black_stained_hardened_clay", "red_stained_hardened_clay", "green_stained_hardened_clay", "brown_stained_hardened_clay", "blue_stained_hardened_clay", "purple_stained_hardened_clay", "cyan_stained_hardened_clay", "silver_stained_hardened_clay", "gray_stained_hardened_clay", "pink_stained_hardened_clay", "lime_stained_hardened_clay", "yellow_stained_hardened_clay", "light_blue_stained_hardened_clay", "magenta_stained_hardened_clay", "orange_stained_hardened_clay", "white_stained_hardened_clay"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.STAINED_GLASS_PANE), (Object)Lists.newArrayList((Object[])new String[]{"black_stained_glass_pane", "red_stained_glass_pane", "green_stained_glass_pane", "brown_stained_glass_pane", "blue_stained_glass_pane", "purple_stained_glass_pane", "cyan_stained_glass_pane", "silver_stained_glass_pane", "gray_stained_glass_pane", "pink_stained_glass_pane", "lime_stained_glass_pane", "yellow_stained_glass_pane", "light_blue_stained_glass_pane", "magenta_stained_glass_pane", "orange_stained_glass_pane", "white_stained_glass_pane"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.LEAVES2), (Object)Lists.newArrayList((Object[])new String[]{"acacia_leaves", "dark_oak_leaves"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.LOG2), (Object)Lists.newArrayList((Object[])new String[]{"acacia_log", "dark_oak_log"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.PRISMARINE), (Object)Lists.newArrayList((Object[])new String[]{"prismarine", "prismarine_bricks", "dark_prismarine"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.CARPET), (Object)Lists.newArrayList((Object[])new String[]{"black_carpet", "red_carpet", "green_carpet", "brown_carpet", "blue_carpet", "purple_carpet", "cyan_carpet", "silver_carpet", "gray_carpet", "pink_carpet", "lime_carpet", "yellow_carpet", "light_blue_carpet", "magenta_carpet", "orange_carpet", "white_carpet"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.DOUBLE_PLANT), (Object)Lists.newArrayList((Object[])new String[]{"sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia"}));
        this.variantNames.put((Object)Items.COAL, (Object)Lists.newArrayList((Object[])new String[]{"coal", "charcoal"}));
        this.variantNames.put((Object)Items.FISH, (Object)Lists.newArrayList((Object[])new String[]{"cod", "salmon", "clownfish", "pufferfish"}));
        this.variantNames.put((Object)Items.COOKED_FISH, (Object)Lists.newArrayList((Object[])new String[]{"cooked_cod", "cooked_salmon"}));
        this.variantNames.put((Object)Items.DYE, (Object)Lists.newArrayList((Object[])new String[]{"dye_black", "dye_red", "dye_green", "dye_brown", "dye_blue", "dye_purple", "dye_cyan", "dye_silver", "dye_gray", "dye_pink", "dye_lime", "dye_yellow", "dye_light_blue", "dye_magenta", "dye_orange", "dye_white"}));
        this.variantNames.put((Object)Items.POTIONITEM, (Object)Lists.newArrayList((Object[])new String[]{"bottle_drinkable"}));
        this.variantNames.put((Object)Items.SKULL, (Object)Lists.newArrayList((Object[])new String[]{"skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper", "skull_dragon"}));
        this.variantNames.put((Object)Items.SPLASH_POTION, (Object)Lists.newArrayList((Object[])new String[]{"bottle_splash"}));
        this.variantNames.put((Object)Items.LINGERING_POTION, (Object)Lists.newArrayList((Object[])new String[]{"bottle_lingering"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.CONCRETE), (Object)Lists.newArrayList((Object[])new String[]{"black_concrete", "red_concrete", "green_concrete", "brown_concrete", "blue_concrete", "purple_concrete", "cyan_concrete", "silver_concrete", "gray_concrete", "pink_concrete", "lime_concrete", "yellow_concrete", "light_blue_concrete", "magenta_concrete", "orange_concrete", "white_concrete"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.CONCRETE_POWDER), (Object)Lists.newArrayList((Object[])new String[]{"black_concrete_powder", "red_concrete_powder", "green_concrete_powder", "brown_concrete_powder", "blue_concrete_powder", "purple_concrete_powder", "cyan_concrete_powder", "silver_concrete_powder", "gray_concrete_powder", "pink_concrete_powder", "lime_concrete_powder", "yellow_concrete_powder", "light_blue_concrete_powder", "magenta_concrete_powder", "orange_concrete_powder", "white_concrete_powder"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.AIR), (Object)Collections.emptyList());
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.OAK_FENCE_GATE), (Object)Lists.newArrayList((Object[])new String[]{"oak_fence_gate"}));
        this.variantNames.put((Object)Item.getItemFromBlock((Block)Blocks.OAK_FENCE), (Object)Lists.newArrayList((Object[])new String[]{"oak_fence"}));
        this.variantNames.put((Object)Items.OAK_DOOR, (Object)Lists.newArrayList((Object[])new String[]{"oak_door"}));
        this.variantNames.put((Object)Items.BOAT, (Object)Lists.newArrayList((Object[])new String[]{"oak_boat"}));
        this.variantNames.put((Object)Items.TOTEM_OF_UNDYING, (Object)Lists.newArrayList((Object[])new String[]{"totem"}));
        for (Map.Entry e : customVariantNames.entrySet()) {
            this.variantNames.put(((IRegistryDelegate)e.getKey()).get(), (Object)Lists.newArrayList((Iterator)((Set)e.getValue()).iterator()));
        }
    }

    private List<String> getVariantNames(Item stack) {
        List list = (List)this.variantNames.get((Object)stack);
        if (list == null) {
            list = Collections.singletonList((Object)((ResourceLocation)Item.REGISTRY.getNameForObject((Object)stack)).toString());
        }
        return list;
    }

    private ResourceLocation getItemLocation(String location) {
        ResourceLocation resourcelocation = new ResourceLocation(location);
        if (Reflector.ForgeHooksClient.exists()) {
            resourcelocation = new ResourceLocation(location.replaceAll("#.*", ""));
        }
        return new ResourceLocation(resourcelocation.getNamespace(), "item/" + resourcelocation.getPath());
    }

    private void bakeBlockModels() {
        for (ModelResourceLocation modelresourcelocation : this.variants.keySet()) {
            IBakedModel ibakedmodel = this.createRandomModelForVariantList((VariantList)this.variants.get((Object)modelresourcelocation), modelresourcelocation.toString());
            if (ibakedmodel == null) continue;
            this.bakedRegistry.putObject((Object)modelresourcelocation, (Object)ibakedmodel);
        }
        for (Map.Entry entry : this.multipartVariantMap.entrySet()) {
            ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)entry.getKey();
            Multipart multipart = modelblockdefinition.getMultipartData();
            String s = ((ResourceLocation)Block.REGISTRY.getNameForObject((Object)multipart.getStateContainer().getBlock())).toString();
            MultipartBakedModel.Builder multipartbakedmodel$builder = new MultipartBakedModel.Builder();
            for (Selector selector : multipart.getSelectors()) {
                IBakedModel ibakedmodel1 = this.createRandomModelForVariantList(selector.getVariantList(), "selector of " + s);
                if (ibakedmodel1 == null) continue;
                multipartbakedmodel$builder.putModel(selector.getPredicate(multipart.getStateContainer()), ibakedmodel1);
            }
            IBakedModel ibakedmodel2 = multipartbakedmodel$builder.makeMultipartModel();
            for (ModelResourceLocation modelresourcelocation1 : (Collection)entry.getValue()) {
                if (modelblockdefinition.hasVariant(modelresourcelocation1.getVariant())) continue;
                this.bakedRegistry.putObject((Object)modelresourcelocation1, (Object)ibakedmodel2);
            }
        }
    }

    @Nullable
    private IBakedModel createRandomModelForVariantList(VariantList variantsIn, String modelLocation) {
        if (variantsIn.getVariantList().isEmpty()) {
            return null;
        }
        WeightedBakedModel.Builder weightedbakedmodel$builder = new WeightedBakedModel.Builder();
        int i = 0;
        for (Variant variant : variantsIn.getVariantList()) {
            ModelBlock modelblock = (ModelBlock)this.models.get((Object)variant.getModelLocation());
            if (modelblock != null && modelblock.isResolved()) {
                if (modelblock.getElements().isEmpty()) {
                    LOGGER.warn("Missing elements for: {}", (Object)modelLocation);
                    continue;
                }
                IBakedModel ibakedmodel = this.bakeModel(modelblock, variant.getRotation(), variant.isUvLock());
                if (ibakedmodel == null) continue;
                ++i;
                weightedbakedmodel$builder.add(ibakedmodel, variant.getWeight());
                continue;
            }
            LOGGER.warn("Missing model for: {}", (Object)modelLocation);
        }
        Object ibakedmodel1 = null;
        if (i == 0) {
            LOGGER.warn("No weighted models for: {}", (Object)modelLocation);
        } else {
            ibakedmodel1 = i == 1 ? weightedbakedmodel$builder.first() : weightedbakedmodel$builder.build();
        }
        return ibakedmodel1;
    }

    private void bakeItemModels() {
        for (Map.Entry entry : this.itemLocations.entrySet()) {
            ModelBlock modelblock;
            ResourceLocation resourcelocation = (ResourceLocation)entry.getValue();
            ModelResourceLocation modelresourcelocation = new ModelResourceLocation((String)entry.getKey(), "inventory");
            if (Reflector.ForgeHooksClient.exists()) {
                modelresourcelocation = (ModelResourceLocation)Reflector.call((ReflectorMethod)Reflector.ModelLoader_getInventoryVariant, (Object[])new Object[]{entry.getKey()});
            }
            if ((modelblock = (ModelBlock)this.models.get((Object)resourcelocation)) != null && modelblock.isResolved()) {
                if (modelblock.getElements().isEmpty()) {
                    LOGGER.warn("Missing elements for: {}", (Object)resourcelocation);
                    continue;
                }
                if (this.isCustomRenderer(modelblock)) {
                    this.bakedRegistry.putObject((Object)modelresourcelocation, (Object)new BuiltInModel(modelblock.getAllTransforms(), modelblock.createOverrides()));
                    continue;
                }
                IBakedModel ibakedmodel = this.bakeModel(modelblock, ModelRotation.X0_Y0, false);
                if (ibakedmodel == null) continue;
                this.bakedRegistry.putObject((Object)modelresourcelocation, (Object)ibakedmodel);
                continue;
            }
            LOGGER.warn("Missing model for: {}", (Object)resourcelocation);
        }
    }

    private Set<ResourceLocation> getVariantsTextureLocations() {
        HashSet set = Sets.newHashSet();
        ArrayList list = Lists.newArrayList((Iterable)this.variants.keySet());
        Collections.sort((List)list, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        for (ModelResourceLocation modelresourcelocation : list) {
            VariantList variantlist = (VariantList)this.variants.get((Object)modelresourcelocation);
            for (Variant variant : variantlist.getVariantList()) {
                ModelBlock modelblock = (ModelBlock)this.models.get((Object)variant.getModelLocation());
                if (modelblock == null) {
                    LOGGER.warn("Missing model for: {}", (Object)modelresourcelocation);
                    continue;
                }
                set.addAll(this.getTextureLocations(modelblock));
            }
        }
        for (ModelBlockDefinition modelblockdefinition : this.multipartVariantMap.keySet()) {
            for (VariantList variantlist1 : modelblockdefinition.getMultipartData().getVariants()) {
                for (Variant variant1 : variantlist1.getVariantList()) {
                    ModelBlock modelblock1 = (ModelBlock)this.models.get((Object)variant1.getModelLocation());
                    if (modelblock1 == null) {
                        LOGGER.warn("Missing model for: {}", Block.REGISTRY.getNameForObject((Object)modelblockdefinition.getMultipartData().getStateContainer().getBlock()));
                        continue;
                    }
                    set.addAll(this.getTextureLocations(modelblock1));
                }
            }
        }
        set.addAll(LOCATIONS_BUILTIN_TEXTURES);
        return set;
    }

    @Nullable
    public IBakedModel bakeModel(ModelBlock modelBlockIn, ModelRotation modelRotationIn, boolean uvLocked) {
        return this.bakeModel(modelBlockIn, (ITransformation)modelRotationIn, uvLocked);
    }

    protected IBakedModel bakeModel(ModelBlock modelBlockIn, ITransformation modelRotationIn, boolean uvLocked) {
        TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.sprites.get((Object)new ResourceLocation(modelBlockIn.resolveTextureName("particle")));
        SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(modelBlockIn, modelBlockIn.createOverrides()).setTexture(textureatlassprite);
        if (modelBlockIn.getElements().isEmpty()) {
            return null;
        }
        for (BlockPart blockpart : modelBlockIn.getElements()) {
            for (EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                BlockPartFace blockpartface = (BlockPartFace)blockpart.mapFaces.get((Object)enumfacing);
                TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)this.sprites.get((Object)new ResourceLocation(modelBlockIn.resolveTextureName(blockpartface.texture)));
                boolean isMatrixInteger = true;
                if (Reflector.ForgeHooksClient.exists()) {
                    isMatrixInteger = TRSRTransformation.isInteger((Matrix4f)modelRotationIn.getMatrix());
                }
                if (blockpartface.cullFace == null || !isMatrixInteger) {
                    simplebakedmodel$builder.addGeneralQuad(this.makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, modelRotationIn, uvLocked));
                    continue;
                }
                simplebakedmodel$builder.addFaceQuad(modelRotationIn.rotate(blockpartface.cullFace), this.makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, modelRotationIn, uvLocked));
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }

    private BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, ModelRotation p_177589_5_, boolean p_177589_6_) {
        if (Reflector.ForgeHooksClient.exists()) {
            return this.makeBakedQuad(p_177589_1_, p_177589_2_, p_177589_3_, p_177589_4_, (ITransformation)p_177589_5_, p_177589_6_);
        }
        return this.faceBakery.makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
    }

    protected BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, ITransformation p_177589_5_, boolean p_177589_6_) {
        return this.faceBakery.makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
    }

    private void loadModelsCheck() {
        this.loadModels();
        for (ModelBlock modelblock : this.models.values()) {
            modelblock.getParentFromMap(this.models);
        }
        ModelBlock.checkModelHierarchy(this.models);
    }

    private void loadModels() {
        ArrayDeque deque = Queues.newArrayDeque();
        HashSet set = Sets.newHashSet();
        for (ResourceLocation resourcelocation : this.models.keySet()) {
            set.add((Object)resourcelocation);
            this.addModelParentLocation((Deque<ResourceLocation>)deque, (Set<ResourceLocation>)set, (ModelBlock)this.models.get((Object)resourcelocation));
        }
        while (!deque.isEmpty()) {
            ResourceLocation resourcelocation1 = (ResourceLocation)deque.pop();
            try {
                if (this.models.get((Object)resourcelocation1) != null) continue;
                ModelBlock modelblock = this.loadModel(resourcelocation1);
                this.models.put((Object)resourcelocation1, (Object)modelblock);
                this.addModelParentLocation((Deque<ResourceLocation>)deque, (Set<ResourceLocation>)set, modelblock);
            }
            catch (Exception exception) {
                LOGGER.warn("In parent chain: {}; unable to load model: '{}'", new Object[]{JOINER.join(this.getParentPath(resourcelocation1)), resourcelocation1});
            }
            set.add((Object)resourcelocation1);
        }
    }

    private void addModelParentLocation(Deque<ResourceLocation> p_188633_1_, Set<ResourceLocation> p_188633_2_, ModelBlock p_188633_3_) {
        ResourceLocation resourcelocation = p_188633_3_.getParentLocation();
        if (resourcelocation != null && !p_188633_2_.contains((Object)resourcelocation)) {
            p_188633_1_.add((Object)resourcelocation);
        }
    }

    private List<ResourceLocation> getParentPath(ResourceLocation p_177573_1_) {
        ArrayList list = Lists.newArrayList((Object[])new ResourceLocation[]{p_177573_1_});
        ResourceLocation resourcelocation = p_177573_1_;
        while ((resourcelocation = this.getParentLocation(resourcelocation)) != null) {
            list.add(0, (Object)resourcelocation);
        }
        return list;
    }

    @Nullable
    private ResourceLocation getParentLocation(ResourceLocation p_177576_1_) {
        for (Map.Entry entry : this.models.entrySet()) {
            ModelBlock modelblock = (ModelBlock)entry.getValue();
            if (modelblock == null || !p_177576_1_.equals((Object)modelblock.getParentLocation())) continue;
            return (ResourceLocation)entry.getKey();
        }
        return null;
    }

    private Set<ResourceLocation> getTextureLocations(ModelBlock p_177585_1_) {
        HashSet set = Sets.newHashSet();
        for (BlockPart blockpart : p_177585_1_.getElements()) {
            for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                ResourceLocation resourcelocation = new ResourceLocation(p_177585_1_.resolveTextureName(blockpartface.texture));
                set.add((Object)resourcelocation);
            }
        }
        set.add((Object)new ResourceLocation(p_177585_1_.resolveTextureName("particle")));
        return set;
    }

    private void loadSprites() {
        Set<ResourceLocation> set = this.getVariantsTextureLocations();
        set.addAll(this.getItemsTextureLocations());
        set.remove((Object)TextureMap.LOCATION_MISSING_TEXTURE);
        3 itexturemappopulator = new /* Unavailable Anonymous Inner Class!! */;
        this.textureMap.loadSprites(this.resourceManager, (ITextureMapPopulator)itexturemappopulator);
        this.sprites.put((Object)new ResourceLocation("missingno"), (Object)this.textureMap.getMissingSprite());
    }

    private Set<ResourceLocation> getItemsTextureLocations() {
        HashSet set = Sets.newHashSet();
        for (ResourceLocation resourcelocation : this.itemLocations.values()) {
            ModelBlock modelblock = (ModelBlock)this.models.get((Object)resourcelocation);
            if (modelblock == null) continue;
            set.add((Object)new ResourceLocation(modelblock.resolveTextureName("particle")));
            if (this.hasItemModel(modelblock)) {
                for (String s : ItemModelGenerator.LAYERS) {
                    set.add((Object)new ResourceLocation(modelblock.resolveTextureName(s)));
                }
                continue;
            }
            if (this.isCustomRenderer(modelblock)) continue;
            for (BlockPart blockpart : modelblock.getElements()) {
                for (BlockPartFace blockpartface : blockpart.mapFaces.values()) {
                    ResourceLocation resourcelocation1 = new ResourceLocation(modelblock.resolveTextureName(blockpartface.texture));
                    set.add((Object)resourcelocation1);
                }
            }
        }
        return set;
    }

    private boolean hasItemModel(@Nullable ModelBlock p_177581_1_) {
        if (p_177581_1_ == null) {
            return false;
        }
        return p_177581_1_.getRootModel() == MODEL_GENERATED;
    }

    private boolean isCustomRenderer(@Nullable ModelBlock p_177587_1_) {
        if (p_177587_1_ == null) {
            return false;
        }
        ModelBlock modelblock = p_177587_1_.getRootModel();
        return modelblock == MODEL_ENTITY;
    }

    private void makeItemModels() {
        for (ResourceLocation resourcelocation : this.itemLocations.values()) {
            ModelBlock modelblock = (ModelBlock)this.models.get((Object)resourcelocation);
            if (this.hasItemModel(modelblock)) {
                ModelBlock modelblock1 = this.makeItemModel(modelblock);
                if (modelblock1 != null) {
                    modelblock1.name = resourcelocation.toString();
                }
                this.models.put((Object)resourcelocation, (Object)modelblock1);
                continue;
            }
            if (!this.isCustomRenderer(modelblock)) continue;
            this.models.put((Object)resourcelocation, (Object)modelblock);
        }
        for (TextureAtlasSprite textureatlassprite : this.sprites.values()) {
            if (textureatlassprite.hasAnimationMetadata()) continue;
            textureatlassprite.clearFramesTextureData();
        }
    }

    private ModelBlock makeItemModel(ModelBlock p_177582_1_) {
        return this.itemModelGenerator.makeItemModel(this.textureMap, p_177582_1_);
    }

    public ModelBlock getModelBlock(ResourceLocation resourceLocation) {
        ModelBlock modelblock = (ModelBlock)this.models.get((Object)resourceLocation);
        return modelblock;
    }

    public static void fixModelLocations(ModelBlock modelBlock, String basePath) {
        ResourceLocation parentLocFixed = ModelBakery.fixModelLocation(modelBlock.parentLocation, basePath);
        if (parentLocFixed != modelBlock.parentLocation) {
            modelBlock.parentLocation = parentLocFixed;
        }
        if (modelBlock.textures != null) {
            for (Map.Entry entry : modelBlock.textures.entrySet()) {
                String path = (String)entry.getValue();
                String pathFixed = ModelBakery.fixResourcePath(path, basePath);
                if (pathFixed == path) continue;
                entry.setValue((Object)pathFixed);
            }
        }
    }

    public static ResourceLocation fixModelLocation(ResourceLocation loc, String basePath) {
        if (loc == null || basePath == null) {
            return loc;
        }
        if (!loc.getNamespace().equals((Object)"minecraft")) {
            return loc;
        }
        String path = loc.getPath();
        String pathFixed = ModelBakery.fixResourcePath(path, basePath);
        if (pathFixed != path) {
            loc = new ResourceLocation(loc.getNamespace(), pathFixed);
        }
        return loc;
    }

    private static String fixResourcePath(String path, String basePath) {
        path = TextureUtils.fixResourcePath((String)path, (String)basePath);
        path = StrUtils.removeSuffix((String)path, (String)".json");
        path = StrUtils.removeSuffix((String)path, (String)".png");
        return path;
    }

    protected void registerMultipartVariant(ModelBlockDefinition definition, Collection<ModelResourceLocation> locations) {
        this.multipartVariantMap.put((Object)definition, locations);
    }

    public static void registerItemVariants(Item item, ResourceLocation ... names) {
        IRegistryDelegate delegate = (IRegistryDelegate)Reflector.getFieldValue((Object)item, (ReflectorField)Reflector.ForgeItem_delegate);
        if (!customVariantNames.containsKey((Object)delegate)) {
            customVariantNames.put((Object)delegate, (Object)Sets.newHashSet());
        }
        for (ResourceLocation name : names) {
            ((Set)customVariantNames.get((Object)delegate)).add((Object)name.toString());
        }
    }

    static /* synthetic */ Map access$000(ModelBakery x0) {
        return x0.sprites;
    }

    static {
        BUILT_IN_MODELS.put((Object)"missing", (Object)MISSING_MODEL_MESH);
        ModelBakery.MODEL_GENERATED.name = "generation marker";
        ModelBakery.MODEL_ENTITY.name = "block entity marker";
        customVariantNames = Maps.newHashMap();
    }
}
