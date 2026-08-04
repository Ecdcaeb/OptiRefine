package mods.Hileb.optirefine.core;

import com.google.common.eventbus.EventBus;
import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.OptiRefine;
import mods.Hileb.optirefine.core.transformer.OptiRefineConfigTransformer;
import mods.Hileb.optirefine.core.transformer.dev.OptifineDevTweakerTransformer;
import mods.Hileb.optirefine.core.transformer.dev.OptifineDevUtilTransformer;
import mods.Hileb.optirefine.core.transformer.OptifineTransformerTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.CursedMixinExtensions;
import mods.Hileb.optirefine.library.fmlmodhacker.MetaDataDecoder;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@IFMLLoadingPlugin.SortingIndex(1000)
@IFMLLoadingPlugin.Name(OptiRefine.NAME)
@IFMLLoadingPlugin.MCVersion(net.minecraftforge.common.ForgeVersion.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions({
        "mods.Hileb.optirefine.core.",
        "mods.Hileb.optirefine.library."
})
public class OptiRefineCore implements IFMLLoadingPlugin {

    private static final Logger LOGGER = LogManager.getLogger();

    static {
        setupTransformers();
    }

    public static void setupTransformers(){
        TransformerHelper.registerTransformer(new OptifineTransformerTransformer());
        TransformerHelper.registerTransformer(new OptiRefineConfigTransformer());

        if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            TransformerHelper.registerTransformer(new OptifineDevUtilTransformer());
            TransformerHelper.registerTransformer(new OptifineDevTweakerTransformer());
        }
    }

    @Nullable
    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getModContainerClass() {
        return "mods.Hileb.optirefine.core.OptiRefineCore$Container";
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    public static File coremodLocation = null;
    @Override
    public void injectData(Map<String, Object> map) {
        coremodLocation = (File) map.get("coremodLocation");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @SuppressWarnings("unused")
    public static class Container extends DummyModContainer{
        public static ModMetadata DATA = null;

        public Container(){
            super(DATA = decodeData());
            DATA.modProperties.put("iconItem", "minecraft:ender_eye");
        }

        private static ModMetadata decodeData() {

            File source = coremodLocation;

            if (source == null) {

                LOGGER.warn("coremodLocation is null, falling back to default metadata");

                return fallbackMetadata();

            }

            if (source.isFile()) {
                try (FileSystem fs = FileSystems.newFileSystem(source.toPath(), (ClassLoader)null)){
                    try (InputStream inputStream = Files.newInputStream(Objects.requireNonNull(fs.getPath("/mcmod.info")))) {
                        return MetaDataDecoder.decodeMcModInfo(inputStream).get("optirefine");
                    } catch (Throwable t) {
                        LOGGER.error("Error loading metadata from jar: ", t);
                    }
                } catch (IOException e) {
                    LOGGER.error("Error loading FileSystem from jar: ", e);
                }
            } else if (source.isDirectory()) {

                try (InputStream inputStream = Files.newInputStream(Objects.requireNonNull(source.toPath().resolve("mcmod.info")))) {

                    return MetaDataDecoder.decodeMcModInfo(inputStream).get("optirefine");

                } catch (Throwable t) {

                    LOGGER.error("Error loading metadata from jar: ", t);

                }

            }

            return fallbackMetadata();

        }



        private static ModMetadata fallbackMetadata() {

            ModMetadata modMetadata = new ModMetadata();

            modMetadata.name = "OptiRefine";

            modMetadata.modId = "optirefine";

            modMetadata.authorList.add("Hileb");

            modMetadata.version = "Unknown";

            return modMetadata;

        }

        @Override
        public File getSource() {
            return coremodLocation;
        }

        @Override
        public Class<?> getCustomResourcePackClass() {
            try {
                return this.getSource().isDirectory() ? Class.forName("net.minecraftforge.fml.client.FMLFolderResourcePack", true, this.getClass().getClassLoader()) : Class.forName("net.minecraftforge.fml.client.FMLFileResourcePack", true, this.getClass().getClassLoader());
            } catch (Throwable var2) {
                return null;
            }
        }

        // [DIAG-TEMP] every mixin target, eagerly loaded at startup (Class.forName initialize=false
        // triggers the mixin transform/apply without running <clinit>), so injection errors surface
        // here in one batch instead of mid-game. Remove once the shader/render crashes are resolved.
        private static final String[] EARLY_LOAD_TARGETS = {
            "net.minecraft.block.BlockAir", "net.minecraft.block.material.MapColor", "net.minecraft.block.state.BlockStateBase", "net.minecraft.block.state.BlockStateContainer",
            "net.minecraft.client.LoadingScreenRenderer", "net.minecraft.client.entity.AbstractClientPlayer", "net.minecraft.client.gui.FontRenderer", "net.minecraft.client.gui.GuiCustomizeSkin",
            "net.minecraft.client.gui.GuiDownloadTerrain", "net.minecraft.client.gui.GuiIngame", "net.minecraft.client.gui.GuiMainMenu", "net.minecraft.client.gui.GuiOverlayDebug",
            "net.minecraft.client.gui.GuiScreenWorking", "net.minecraft.client.gui.GuiSlot", "net.minecraft.client.gui.GuiVideoSettings", "net.minecraft.client.model.ModelBox",
            "net.minecraft.client.model.ModelPlayer", "net.minecraft.client.model.ModelRenderer", "net.minecraft.client.model.TexturedQuad", "net.minecraft.client.multiplayer.ChunkProviderClient",
            "net.minecraft.client.multiplayer.WorldClient", "net.minecraft.client.particle.ParticleItemPickup", "net.minecraft.client.particle.ParticleManager", "net.minecraft.client.renderer.BlockFluidRenderer",
            "net.minecraft.client.renderer.BlockModelRenderer", "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace", "net.minecraft.client.renderer.BufferBuilder", "net.minecraft.client.renderer.BufferBuilder$State",
            "net.minecraft.client.renderer.ChunkRenderContainer", "net.minecraft.client.renderer.EntityRenderer", "net.minecraft.client.renderer.GlStateManager", "net.minecraft.client.renderer.ImageBufferDownload",
            "net.minecraft.client.renderer.ItemRenderer", "net.minecraft.client.renderer.Matrix4f", "net.minecraft.client.renderer.OpenGlHelper", "net.minecraft.client.renderer.RenderGlobal",
            "net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation", "net.minecraft.client.renderer.RenderItem", "net.minecraft.client.renderer.RenderList", "net.minecraft.client.renderer.Tessellator",
            "net.minecraft.client.renderer.ThreadDownloadImageData", "net.minecraft.client.renderer.VboRenderList", "net.minecraft.client.renderer.VertexBufferUploader", "net.minecraft.client.renderer.ViewFrustum",
            "net.minecraft.client.renderer.WorldVertexBufferUploader", "net.minecraft.client.renderer.block.model.BakedQuad", "net.minecraft.client.renderer.block.model.BakedQuadRetextured", "net.minecraft.client.renderer.block.model.BlockPart$Deserializer",
            "net.minecraft.client.renderer.block.model.FaceBakery", "net.minecraft.client.renderer.block.model.ItemOverrideList", "net.minecraft.client.renderer.block.model.ModelBakery", "net.minecraft.client.renderer.block.model.ModelRotation",
            "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher", "net.minecraft.client.renderer.chunk.CompiledChunk", "net.minecraft.client.renderer.chunk.CompiledChunk$1", "net.minecraft.client.renderer.chunk.RenderChunk",
            "net.minecraft.client.renderer.chunk.SetVisibility", "net.minecraft.client.renderer.chunk.VisGraph", "net.minecraft.client.renderer.culling.ClippingHelper", "net.minecraft.client.renderer.culling.Frustum",
            "net.minecraft.client.renderer.debug.DebugRendererChunkBorder", "net.minecraft.client.renderer.entity.Render", "net.minecraft.client.renderer.entity.RenderItemFrame", "net.minecraft.client.renderer.entity.RenderLiving",
            "net.minecraft.client.renderer.entity.RenderLivingBase", "net.minecraft.client.renderer.entity.RenderManager", "net.minecraft.client.renderer.entity.RenderXPOrb", "net.minecraft.client.renderer.entity.layers.LayerArmorBase",
            "net.minecraft.client.renderer.entity.layers.LayerCape", "net.minecraft.client.renderer.entity.layers.LayerElytra", "net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes", "net.minecraft.client.renderer.entity.layers.LayerEndermanEyes",
            "net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder", "net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom", "net.minecraft.client.renderer.entity.layers.LayerSheepWool", "net.minecraft.client.renderer.entity.layers.LayerSpiderEyes",
            "net.minecraft.client.renderer.entity.layers.LayerWolfCollar", "net.minecraft.client.renderer.texture.AbstractTexture", "net.minecraft.client.renderer.texture.DynamicTexture", "net.minecraft.client.renderer.texture.ITextureObject",
            "net.minecraft.client.renderer.texture.LayeredColorMaskTexture", "net.minecraft.client.renderer.texture.LayeredTexture", "net.minecraft.client.renderer.texture.SimpleTexture", "net.minecraft.client.renderer.texture.Stitcher",
            "net.minecraft.client.renderer.texture.TextureAtlasSprite", "net.minecraft.client.renderer.texture.TextureManager", "net.minecraft.client.renderer.texture.TextureMap", "net.minecraft.client.renderer.texture.TextureUtil",
            "net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer", "net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer", "net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher", "net.minecraft.client.renderer.tileentity.TileEntitySignRenderer",
            "net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer", "net.minecraft.client.renderer.vertex.DefaultVertexFormats", "net.minecraft.client.renderer.vertex.VertexBuffer", "net.minecraft.client.resources.AbstractResourcePack",
            "net.minecraft.client.resources.DefaultResourcePack", "net.minecraft.client.resources.I18n", "net.minecraft.client.resources.ResourcePackRepository", "net.minecraft.client.settings.GameSettings",
            "net.minecraft.client.settings.GameSettings$Options", "net.minecraft.crash.CrashReport", "net.minecraft.entity.EntityLiving", "net.minecraft.network.PacketThreadUtil",
            "net.minecraft.network.datasync.EntityDataManager", "net.minecraft.potion.PotionUtils", "net.minecraft.profiler.Profiler", "net.minecraft.server.integrated.IntegratedServer",
            "net.minecraft.server.management.PlayerChunkMap", "net.minecraft.util.ClassInheritanceMultiMap", "net.minecraft.util.EnumFacing", "net.minecraft.util.ScreenShotHelper",
            "net.minecraft.util.Util", "net.minecraft.util.math.ChunkPos", "net.minecraft.util.math.MathHelper", "net.minecraft.util.text.translation.I18n",
            "net.minecraft.world.GameRules$Value", "net.minecraft.world.WorldEntitySpawner", "net.minecraft.world.chunk.BlockStateContainer", "net.minecraft.world.chunk.storage.ExtendedBlockStorage",
            "net.minecraft.world.gen.layer.GenLayerZoom", "net.minecraftforge.client.GuiIngameForge", "net.minecraftforge.fml.client.FMLClientHandler", "Config",
            "net.optifine.shaders.Shaders",
        };

        @Override
        public boolean registerBus(EventBus bus, LoadController controller) {
            // [DIAG-TEMP] eagerly load every mixin target (initialize=false: transform+apply only,
            // no <clinit> side effects) to surface all injection problems in one startup pass.
            for (String target : EARLY_LOAD_TARGETS) {
                try {
                    Class.forName(target, false, OptiRefineCore.class.getClassLoader());
                } catch (Throwable t) {
                    LOGGER.error("[DIAG] early-load {} failed: {}", target, t.toString());
                }
            }
            return true;
        }
    }

    public static class DefaultMixinPlugin implements IMixinConfigPlugin {
        @Override
        public void onLoad(String s) {

        }

        @Override
        public String getRefMapperConfig() {
            return null;
        }

        @Override
        public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
            targetClassName = targetClassName.replace('/', '.');

            boolean should = true;

            LOGGER.debug("OptiRefine ShouldApply For {} ? {}", targetClassName, should);

            return should;
        }

        @Override
        public void acceptTargets(Set<String> set, Set<String> set1) {

        }

        @Override
        public List<String> getMixins() {
            return null;
        }

        @Override
        public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

        }

        @Override
        public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
            CursedMixinExtensions.postApply(classNode);
            OptiRefineConfigTransformer.transform0(classNode);
        }
    }

    public static class ModMixinPlugin implements IMixinConfigPlugin {
        @Override
        public void onLoad(String s) {

        }

        @Override
        public String getRefMapperConfig() {
            return "";
        }

        @Override

        public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

            //mods.Hileb.optirefine.mixin.mods.<modId>.path.MixinClass

            String[] parts = mixinClassName.split("\\.");

            if (parts.length < 6 || !"mods".equals(parts[0]) || !"Hileb".equals(parts[1]) || !"optirefine".equals(parts[2]) || !"mixin".equals(parts[3]) || !"mods".equals(parts[4])) {

                return false;

            }

            String modId = parts[5];

            return Loader.isModLoaded(modId);

        }

        @Override
        public void acceptTargets(Set<String> set, Set<String> set1) {

        }

        @Override
        public List<String> getMixins() {
            return List.of();
        }

        @Override
        public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
        }

        @Override
        public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
            CursedMixinExtensions.postApply(classNode);
        }
    }
}
