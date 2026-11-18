package mods.Hileb.optirefine.optifine.shaders.modern;

import mods.Hileb.optirefine.mixin.defaults.optifine.ShaderBiomeCameraAccess;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.optifine.expr.ConstantFloat;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionFloat;
import net.optifine.shaders.Shaders;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public final class ShaderBiomeExpressions {
    // The ordinals are shader pack API values from OptiFine's shaders.properties.
    private enum Category {
        NONE, TAIGA, EXTREME_HILLS, JUNGLE, MESA, PLAINS, SAVANNA, ICY,
        THE_END, BEACH, FOREST, OCEAN, DESERT, RIVER, SWAMP, MUSHROOM, NETHER
    }

    private enum Precipitation {
        NONE, RAIN, SNOW
    }

    private record ForgeCategory(Type type, Category category) {}

    private static final Map<Biome, Category> VANILLA_CATEGORIES = vanillaCategories();
    // Explicit Forge tags can overlap. Prefer specific biome families over terrain and climate tags.
    private static final ForgeCategory[] FORGE_CATEGORIES = {
            new ForgeCategory(Type.NETHER, Category.NETHER),
            new ForgeCategory(Type.END, Category.THE_END),
            new ForgeCategory(Type.VOID, Category.NONE),
            new ForgeCategory(Type.OCEAN, Category.OCEAN),
            new ForgeCategory(Type.RIVER, Category.RIVER),
            new ForgeCategory(Type.MUSHROOM, Category.MUSHROOM),
            new ForgeCategory(Type.BEACH, Category.BEACH),
            new ForgeCategory(Type.MESA, Category.MESA),
            new ForgeCategory(Type.SWAMP, Category.SWAMP),
            new ForgeCategory(Type.JUNGLE, Category.JUNGLE),
            new ForgeCategory(Type.SAVANNA, Category.SAVANNA),
            new ForgeCategory(Type.CONIFEROUS, Category.TAIGA),
            new ForgeCategory(Type.FOREST, Category.FOREST),
            new ForgeCategory(Type.SNOWY, Category.ICY),
            new ForgeCategory(Type.SANDY, Category.DESERT),
            new ForgeCategory(Type.PLAINS, Category.PLAINS),
            new ForgeCategory(Type.MOUNTAIN, Category.EXTREME_HILLS),
            new ForgeCategory(Type.HILLS, Category.EXTREME_HILLS)
    };
    private static final Map<String, IExpression> EXPRESSIONS = expressions();

    private ShaderBiomeExpressions() {}

    public static void register(Map<String, IExpression> expressions) {
        // Keep expressions already supplied by G5 and by packs with their own 1.12 definitions.
        EXPRESSIONS.forEach(expressions::putIfAbsent);
    }

    private static Map<String, IExpression> expressions() {
        Map<String, IExpression> expressions = new HashMap<>();
        expressions.put("biome_category", (IExpressionFloat) () -> category(cameraBiome()).ordinal());
        expressions.put("biome_precipitation", (IExpressionFloat) () -> precipitation(cameraBiome()).ordinal());
        for (Category category : Category.values()) {
            expressions.put("CAT_" + category.name(), new ConstantFloat(category.ordinal()));
        }
        for (Precipitation precipitation : Precipitation.values()) {
            expressions.put("PPT_" + precipitation.name(), new ConstantFloat(precipitation.ordinal()));
        }
        return expressions;
    }

    private static Biome cameraBiome() {
        World world = Shaders.getCurrentWorld();
        if (world == null) return null;

        // G5's camera coordinates are relative to its rendering origin. Biome lookup uses world coordinates.
        PooledMutableBlockPos pos = PooledMutableBlockPos.retain(
                ShaderBiomeCameraAccess.optiRefine$getCameraX() + ShaderBiomeCameraAccess.optiRefine$getCameraOffsetX(),
                ShaderBiomeCameraAccess.optiRefine$getCameraY(),
                ShaderBiomeCameraAccess.optiRefine$getCameraZ() + ShaderBiomeCameraAccess.optiRefine$getCameraOffsetZ());
        try {
            return world.getBiome(pos);
        } finally {
            pos.release();
        }
    }

    private static Category category(Biome biome) {
        if (biome == null) return Category.NONE;
        Category vanillaCategory = VANILLA_CATEGORIES.get(biome);
        if (vanillaCategory != null) return vanillaCategory;

        for (ForgeCategory mapping : FORGE_CATEGORIES) {
            if (BiomeDictionary.getBiomes(mapping.type()).contains(biome)) return mapping.category();
        }
        return Category.NONE;
    }

    private static Precipitation precipitation(Biome biome) {
        // This is the biome's precipitation setting. Packs use temperature for altitude-dependent snow.
        if (biome == null) return Precipitation.NONE;
        if (biome.getEnableSnow()) return Precipitation.SNOW;
        return biome.canRain() ? Precipitation.RAIN : Precipitation.NONE;
    }

    private static Map<Biome, Category> vanillaCategories() {
        Map<Biome, Category> categories = new IdentityHashMap<>();
        add(categories, Category.NONE, Biomes.STONE_BEACH, Biomes.VOID);
        add(categories, Category.TAIGA, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS,
                Biomes.REDWOOD_TAIGA, Biomes.REDWOOD_TAIGA_HILLS, Biomes.MUTATED_TAIGA, Biomes.MUTATED_TAIGA_COLD,
                Biomes.MUTATED_REDWOOD_TAIGA, Biomes.MUTATED_REDWOOD_TAIGA_HILLS);
        add(categories, Category.EXTREME_HILLS, Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES,
                Biomes.MUTATED_EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES);
        add(categories, Category.JUNGLE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE,
                Biomes.MUTATED_JUNGLE, Biomes.MUTATED_JUNGLE_EDGE);
        add(categories, Category.MESA, Biomes.MESA, Biomes.MESA_ROCK, Biomes.MESA_CLEAR_ROCK,
                Biomes.MUTATED_MESA, Biomes.MUTATED_MESA_ROCK, Biomes.MUTATED_MESA_CLEAR_ROCK);
        add(categories, Category.PLAINS, Biomes.PLAINS, Biomes.MUTATED_PLAINS);
        add(categories, Category.SAVANNA, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.MUTATED_SAVANNA, Biomes.MUTATED_SAVANNA_ROCK);
        add(categories, Category.ICY, Biomes.ICE_PLAINS, Biomes.ICE_MOUNTAINS, Biomes.MUTATED_ICE_FLATS);
        add(categories, Category.THE_END, Biomes.SKY);
        add(categories, Category.BEACH, Biomes.BEACH, Biomes.COLD_BEACH);
        add(categories, Category.FOREST, Biomes.FOREST, Biomes.FOREST_HILLS, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS,
                Biomes.ROOFED_FOREST, Biomes.MUTATED_FOREST, Biomes.MUTATED_BIRCH_FOREST, Biomes.MUTATED_BIRCH_FOREST_HILLS,
                Biomes.MUTATED_ROOFED_FOREST);
        add(categories, Category.OCEAN, Biomes.OCEAN, Biomes.FROZEN_OCEAN, Biomes.DEEP_OCEAN);
        add(categories, Category.DESERT, Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.MUTATED_DESERT);
        add(categories, Category.RIVER, Biomes.RIVER, Biomes.FROZEN_RIVER);
        add(categories, Category.SWAMP, Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND);
        add(categories, Category.MUSHROOM, Biomes.MUSHROOM_ISLAND, Biomes.MUSHROOM_ISLAND_SHORE);
        add(categories, Category.NETHER, Biomes.HELL);
        return categories;
    }

    private static void add(Map<Biome, Category> categories, Category category, Biome... biomes) {
        for (Biome biome : biomes) categories.put(biome, category);
    }
}
