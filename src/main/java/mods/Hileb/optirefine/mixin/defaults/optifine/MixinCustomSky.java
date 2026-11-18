package mods.Hileb.optirefine.mixin.defaults.optifine;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.CustomSky;
import net.optifine.CustomSkyLayer;
import net.optifine.render.Blender;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;
import net.optifine.util.TextureUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Mixin(value = CustomSky.class, remap = false)
public abstract class MixinCustomSky {
    @Shadow private static CustomSkyLayer[][] worldSkyLayers;
    @Unique private static final String optiRefine$WORLD_PREFIX = "mcpatcher/sky/world";
    @Unique private static final String optiRefine$FIRST_LAYER = "/sky1.properties";
    @Unique private static Int2ObjectMap<CustomSkyLayer[]> optiRefine$dimensionLayers;

    @Inject(method = "reset", at = @At("HEAD"))
    private static void optiRefine$resetDimensionLayers(CallbackInfo ci) {
        optiRefine$dimensionLayers = null;
    }

    @Inject(method = "update", at = @At("TAIL"))
    private static void optiRefine$loadDimensionLayers(CallbackInfo ci) {
        // G5 already loaded IDs 0–9, including resources in packs without file discovery.
        // Discover other IDs from the packs so even dimensions registered after reload are available.
        Int2ObjectMap<CustomSkyLayer[]> layersById = null;
        for (String file : ResUtils.collectFiles(optiRefine$WORLD_PREFIX, optiRefine$FIRST_LAYER)) {
            int dimension;
            try {
                dimension = Integer.parseInt(file, optiRefine$WORLD_PREFIX.length(),
                        file.length() - optiRefine$FIRST_LAYER.length(), 10);
            } catch (NumberFormatException ignored) {
                continue;
            }
            if (dimension >= 0 && dimension < 10) {
                continue;
            }
            CustomSkyLayer[] layers = optiRefine$readDimensionLayers(dimension);
            if (layers != null) {
                if (layersById == null) {
                    layersById = new Int2ObjectOpenHashMap<>();
                }
                layersById.put(dimension, layers);
            }
        }
        optiRefine$dimensionLayers = layersById;
    }

    @Unique
    private static CustomSkyLayer[] optiRefine$readDimensionLayers(int dimension) {
        String prefix = optiRefine$WORLD_PREFIX + dimension + "/sky";
        ArrayList<CustomSkyLayer> layers = new ArrayList<>();
        // Preserve G5's numbering: the first missing properties file ends the sequence.
        for (int index = 1; index < 1000; ++index) {
            String path = prefix + index + ".properties";
            PropertiesOrdered properties;
            try (InputStream stream = Config.getResourceStream(new ResourceLocation(path))) {
                if (stream == null) {
                    break;
                }
                properties = new PropertiesOrdered();
                properties.load(stream);
            } catch (FileNotFoundException missing) {
                break;
            } catch (IOException failure) {
                Config.warn("CustomSky: Error reading " + path + ": " + failure);
                continue;
            }
            Config.dbg("CustomSky properties: " + path);
            CustomSkyLayer layer = new CustomSkyLayer(properties, prefix + index + ".png");
            if (!layer.isValid(path)) {
                continue;
            }
            ResourceLocation source = new ResourceLocation(layer.source);
            ITextureObject texture = TextureUtils.getTexture(source);
            if (texture == null) {
                Config.log("CustomSky: Texture not found: " + source);
                continue;
            }
            layer.textureId = texture.getGlTextureId();
            layers.add(layer);
        }
        return layers.isEmpty() ? null : layers.toArray(CustomSkyLayer[]::new);
    }

    @Unique
    private static CustomSkyLayer[] optiRefine$getLayers(World world) {
        if (worldSkyLayers == null && optiRefine$dimensionLayers == null) {
            return null;
        }
        int dimension = world.provider.getDimension();
        if (dimension >= 0 && worldSkyLayers != null && dimension < worldSkyLayers.length) {
            return worldSkyLayers[dimension];
        }
        return optiRefine$dimensionLayers == null ? null : optiRefine$dimensionLayers.get(dimension);
    }

    /**
     * @author OptiRefine
     * @reason Select sky layers by the provider's actual dimension ID.
     */
    @Overwrite
    public static boolean hasSkyLayers(World world) {
        CustomSkyLayer[] layers = optiRefine$getLayers(world);
        return layers != null && layers.length > 0;
    }

    /**
     * @author OptiRefine
     * @reason Select sky layers by the provider's actual dimension ID before running G5's layer renderer.
     */
    @Overwrite
    public static void renderSky(World world, TextureManager textureManager, float partialTicks) {
        CustomSkyLayer[] layers = optiRefine$getLayers(world);
        if (layers == null) {
            return;
        }
        int time = (int) (world.getWorldTime() % 24000L);
        float angle = world.getCelestialAngle(partialTicks);
        float rain = world.getRainStrength(partialTicks);
        float thunder = world.getThunderStrength(partialTicks);
        if (rain > 0.0F) {
            thunder /= rain;
        }
        for (CustomSkyLayer layer : layers) {
            if (layer.isActive(world, time)) {
                layer.render(world, time, angle, rain, thunder);
            }
        }
        Blender.clearBlend(1.0F - rain);
    }
}
