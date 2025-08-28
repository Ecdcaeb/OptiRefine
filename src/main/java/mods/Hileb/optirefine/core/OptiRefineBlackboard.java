package mods.Hileb.optirefine.core;

import com.google.common.collect.Sets;
import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import mods.Hileb.optirefine.library.common.utils.Lazy;
import net.minecraft.launchwrapper.Launch;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class OptiRefineBlackboard {



    public static final HashSet<String> CLASSES = Sets.newHashSet(
            "net.minecraft.block.material.MapColor", // Optifine -> make ‘colorValue’ not final -> accessTransformer
            "net.minecraft.block.state.BlockStateBase", // Optifine -> add new field and methods -> mixin
            "net.minecraft.block.state.BlockStateBase$1",
            "net.minecraft.block.state.BlockStateContainer", // Optifine -> redirected a construction -> skip
            "net.minecraft.block.state.BlockStateContainer$1",
            "net.minecraft.block.state.BlockStateContainer$Builder",
            "net.minecraft.block.state.BlockStateContainer$StateImplementation", // Optifine -> add new field and methods -> very bad -> skip
            "net.minecraft.block.BlockAir", // Optifine -> add new field and methods -> mixin
            "net.minecraft.client.LoadingScreenRenderer", // Optifine -> customGUI -> mixin FMLClientHandler
            "net.minecraft.client.entity.AbstractClientPlayer", // Optifine -> Capes -> mixin
            "net.minecraft.client.gui.FontRenderer", // Optifine -> custom font colors -> mixin
            "net.minecraft.client.gui.GuiCustomizeSkin", // Optifine -> add Capes button -> mixin
            "net.minecraft.client.gui.GuiCustomizeSkin$1",
            "net.minecraft.client.gui.GuiCustomizeSkin$ButtonPart",
            "net.minecraft.client.gui.GuiDownloadTerrain", // Optifine -> Custom Screen -> mixin
            "net.minecraft.client.gui.GuiIngame", // Optfine -> gui in game -> mixin GuiIngameForge
            "net.minecraft.client.gui.GuiIngame$1",
            "net.minecraft.client.gui.GuiMainMenu",
            "net.minecraft.client.gui.GuiOverlayDebug",
            "net.minecraft.client.gui.GuiOverlayDebug$1",
            "net.minecraft.client.gui.GuiScreenWorking",
            "net.minecraft.client.gui.GuiSlot",
            "net.minecraft.client.gui.GuiVideoSettings",
            "net.minecraft.client.model.ModelBox", // Optifine -> -this.bipedCape.rotationPointY -> skip
            "net.minecraft.client.model.ModelPlayer",
            "net.minecraft.client.model.ModelRenderer",
            "net.minecraft.client.model.TexturedQuad",
            "net.minecraft.client.multiplayer.WorldClient",
            "net.minecraft.client.multiplayer.WorldClient$1",
            "net.minecraft.client.multiplayer.WorldClient$2",
            "net.minecraft.client.multiplayer.WorldClient$3",
            "net.minecraft.client.multiplayer.WorldClient$4",
            "net.minecraft.client.particle.ParticleItemPickup",
            "net.minecraft.client.particle.ParticleManager",
            "net.minecraft.client.particle.ParticleManager$1",
            "net.minecraft.client.particle.ParticleManager$2",
            "net.minecraft.client.particle.ParticleManager$3",
            "net.minecraft.client.particle.ParticleManager$4",
            /*
            "net.minecraft.client.renderer.block.model.BakedQuad",
            "net.minecraft.client.renderer.block.model.BakedQuadRetextured",
            "net.minecraft.client.renderer.block.model.FaceBakery",
            "net.minecraft.client.renderer.block.model.FaceBakery$1",
            "net.minecraft.client.renderer.block.model.FaceBakery$2",
            "net.minecraft.client.renderer.block.model.FaceBakery$3",
            "net.minecraft.client.renderer.block.model.FaceBakery$4",
            "net.minecraft.client.renderer.block.model.FaceBakery$5",
            "net.minecraft.client.renderer.block.model.FaceBakery$Rotation",
            "net.minecraft.client.renderer.block.model.ItemOverrideList",

             */
            "net.minecraft.client.renderer.block.model.ModelBakery",
            "net.minecraft.client.renderer.block.model.ModelBakery$1",
            "net.minecraft.client.renderer.block.model.ModelBakery$2",
            "net.minecraft.client.renderer.block.model.ModelBakery$3",
            "net.minecraft.client.renderer.block.model.ModelRotation",
            "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher",
            "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$1",
            "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$2",
            "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$3",
            "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$PendingUpload",
            "net.minecraft.client.renderer.chunk.CompiledChunk",
            "net.minecraft.client.renderer.chunk.CompiledChunk$1",
            //"net.minecraft.client.renderer.chunk.RenderChunk",
            "net.minecraft.client.renderer.chunk.SetVisibility",
            "net.minecraft.client.renderer.chunk.VisGraph",
            "net.minecraft.client.renderer.chunk.VisGraph$1",
            "net.minecraft.client.renderer.culling.ClippingHelper",
            "net.minecraft.client.renderer.culling.Frustum",
            "net.minecraft.client.renderer.debug.DebugRendererChunkBorder",
            "net.minecraft.client.renderer.entity.layers.LayerArmorBase",
            "net.minecraft.client.renderer.entity.layers.LayerArmorBase$1",
            "net.minecraft.client.renderer.entity.layers.LayerCape",
            "net.minecraft.client.renderer.entity.layers.LayerElytra",
            "net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes",
            "net.minecraft.client.renderer.entity.layers.LayerEndermanEyes",
            "net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder",
            "net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom",
            "net.minecraft.client.renderer.entity.layers.LayerSheepWool",
            "net.minecraft.client.renderer.entity.layers.LayerSpiderEyes",
            "net.minecraft.client.renderer.entity.layers.LayerWolfCollar",
            "net.minecraft.client.renderer.entity.Render",
            "net.minecraft.client.renderer.entity.RenderItemFrame",
            "net.minecraft.client.renderer.entity.RenderLiving",
            "net.minecraft.client.renderer.entity.RenderLivingBase",
            "net.minecraft.client.renderer.entity.RenderLivingBase$1",
            "net.minecraft.client.renderer.entity.RenderManager",
            "net.minecraft.client.renderer.entity.RenderXPOrb",
            "net.minecraft.client.renderer.texture.AbstractTexture",
            "net.minecraft.client.renderer.texture.DynamicTexture",
            "net.minecraft.client.renderer.texture.ITextureObject",
            "net.minecraft.client.renderer.texture.LayeredColorMaskTexture",
            "net.minecraft.client.renderer.texture.LayeredTexture",
            "net.minecraft.client.renderer.texture.SimpleTexture",
            "net.minecraft.client.renderer.texture.Stitcher",
            "net.minecraft.client.renderer.texture.Stitcher$Holder",
            "net.minecraft.client.renderer.texture.Stitcher$Slot",

            "net.minecraft.client.renderer.texture.TextureManager",
            "net.minecraft.client.renderer.texture.TextureManager$1",

            "net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer",
            "net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer",
            "net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher",
            "net.minecraft.client.renderer.tileentity.TileEntitySignRenderer",
            "net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer",
            "net.minecraft.client.renderer.vertex.DefaultVertexFormats",
            "net.minecraft.client.renderer.vertex.VertexBuffer",
            "net.minecraft.client.renderer.BlockFluidRenderer",
//            "net.minecraft.client.renderer.BlockModelRender",
//            "net.minecraft.client.renderer.BlockModelRender$AmbientOcclusionFace", //TODO
            "net.minecraft.client.renderer.BlockModelRender$EnumNeighborInfo",
            "net.minecraft.client.renderer.BlockModelRender$Orientation",
            "net.minecraft.client.renderer.BlockModelRender$VertexTranslations",
            "net.minecraft.client.renderer.BufferBuilder",
            "net.minecraft.client.renderer.BufferBuilder$1",
            "net.minecraft.client.renderer.BufferBuilder$2",
            "net.minecraft.client.renderer.BufferBuilder$State",
            "net.minecraft.client.renderer.ChunkRenderContainer",

            "net.minecraft.client.renderer.ImageBufferDownload",

            "net.minecraft.client.renderer.Tessellator",
            "net.minecraft.client.renderer.ThreadDownloadImageData",
            "net.minecraft.client.renderer.ThreadDownloadImageData$1",
            "net.minecraft.client.renderer.VboRenderList",
            "net.minecraft.client.renderer.VertexBufferUploader",
            "net.minecraft.client.renderer.ViewFrustum",
            "net.minecraft.client.renderer.WorldVertexBufferUploader",
            "net.minecraft.client.renderer.WorldVertexBufferUploader$1",
            "net.minecraft.client.resources.AbstractResourcePack",
            "net.minecraft.client.resources.DefaultResourcePack",
            "net.minecraft.client.resources.I18n",
            "net.minecraft.client.resources.ResourcePackRepository$1",
            "net.minecraft.client.resources.ResourcePackRepository$2",
            "net.minecraft.client.resources.ResourcePackRepository$3",
            "net.minecraft.client.resources.ResourcePackRepository$Entry",
            "net.minecraft.client.settings.GameSettings",
            "net.minecraft.client.settings.GameSettings$1",
            "net.minecraft.client.settings.GameSettings$2",
            "net.minecraft.client.settings.GameSettings$Options",
            //"net.minecraft.client.LoadingScreenRenderer",
            "net.minecraft.crash.CrashReport",
            "net.minecraft.crash.CrashReport$1",
            "net.minecraft.crash.CrashReport$2",
            "net.minecraft.crash.CrashReport$3",
            "net.minecraft.crash.CrashReport$4",
            "net.minecraft.crash.CrashReport$5",
            "net.minecraft.crash.CrashReport$6",
            "net.minecraft.crash.CrashReport$7",
            "net.minecraft.entity.EntityLiving",
            "net.minecraft.entity.EntityLiving$1",
            "net.minecraft.entity.EntityLiving$SpawnPlacementType",
            "net.minecraft.network.datasync.EntityDataManager",
            "net.minecraft.network.datasync.EntityDataManager$1",
            "net.minecraft.network.PacketThreadUtil",
            "net.minecraft.network.PacketThreadUtil$1",
            "net.minecraft.potion.PotionUtils",
            "net.minecraft.profiler.Profiler",
            "net.minecraft.profiler.Profiler$Result",
            "net.minecraft.server.integrated.IntegratedServer",
            "net.minecraft.server.integrated.IntegratedServer$1",
            "net.minecraft.server.integrated.IntegratedServer$2",
            "net.minecraft.server.integrated.IntegratedServer$3",
            "net.minecraft.server.management.PlayerChunkMap",
            "net.minecraft.server.management.PlayerChunkMap$1",
            "net.minecraft.server.management.PlayerChunkMap$2",
            "net.minecraft.server.management.PlayerChunkMap$3",
            "net.minecraft.server.management.PlayerChunkMap$4",
            "net.minecraft.server.management.PlayerChunkMap$5",
            "net.minecraft.util.math.ChunkPos",
            "net.minecraft.util.math.MathHelper",
            "net.minecraft.util.text.translation.I18n",
            "net.minecraft.util.ClassInheritanceMultiMap",
            "net.minecraft.util.ClassInheritanceMultiMap$1",
            "net.minecraft.util.EnumFacing",
            "net.minecraft.util.EnumFacing$1",
            "net.minecraft.util.EnumFacing$Axis",
            "net.minecraft.util.EnumFacing$AxisDirection",
            "net.minecraft.util.EnumFacing$Plane",
            "net.minecraft.util.IntegerCache",
            "net.minecraft.util.ScreenShotHelper",
            "net.minecraft.util.Util",
            "net.minecraft.util.Util$EnumOS",
            "net.minecraft.world.GameRules",
            "net.minecraft.world.GameRules$Value",
            "net.minecraft.world.GameRules$ValueType",
            "net.minecraft.world.gen.layer.GenLayerZoom",
            "net.minecraft.world.chunk.BlockStateContainer",
            "net.minecraft.world.chunk.storage.ExtendedBlockStorage"
    );
    private String getTitleFromHitokoto() {
        CloseableHttpResponse.create();
        CloseableHttpClient.class.getDeclaredMethods();
        try {
            String response = EntityUtils.toString(HttpClients.createDefault().execute(new HttpGet("https://v1.hitokoto.cn/")).getEntity());
        } catch (Throwable var9) {
        }
    }


    public static final Lazy<Object2BooleanMap<String>> REPATCH_CONFIG = Lazy.of(()->{
//        ServiceLoader.load(IPlantable.class).stream()
//                .map(ServiceLoader.Provider::get)
                //.
        String[] str = CLASSES.toArray(String[]::new);
        boolean[] ab = new boolean[str.length];
        java.util.Arrays.fill(ab, true);
        var map = new Object2BooleanOpenHashMap<>(str, ab);
        File config = new File(new File(Launch.minecraftHome, "config"), "optirefine_patch_cfg.json");
        if (config.exists()) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(config.toPath(), StandardCharsets.UTF_8)) {
                JsonElement jsonElement = JsonParser.parseReader(bufferedReader);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isBoolean()) {
                            if(map.containsKey(entry.getKey())) {
                                map.put(entry.getKey(), entry.getValue().getAsBoolean());
                            }
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        } else {
            try (BufferedWriter writer = Files.newBufferedWriter(config.toPath(), StandardCharsets.UTF_8)) {
                JsonObject jsonObject = new JsonObject();
                for (var entry : map.object2BooleanEntrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
                    jsonObject.add(entry.getKey(), new JsonPrimitive(entry.getBooleanValue()));
                }
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
            } catch (IOException ignored) {
            }
        }
        return map;
    });

    public static boolean isOverwritePatches(String className) {
        return REPATCH_CONFIG.get().containsKey(className) && REPATCH_CONFIG.get().getBoolean(className);
    }

    /*
    net.minecraft.block.BlockAir
net.minecraft.block.material.MapColor
net.minecraft.block.state.BlockStateBase
net.minecraft.block.state.BlockStateContainer
net.minecraft.client.LoadingScreenRenderer
net.minecraft.client.entity.AbstractClientPlayer
net.minecraft.client.gui.FontRenderer
net.minecraft.client.gui.GuiCustomizeSkin
net.minecraft.client.gui.GuiDownloadTerrain
net.minecraft.client.gui.GuiIngame
net.minecraft.client.gui.GuiMainMenu
net.minecraft.client.gui.GuiOverlayDebug
net.minecraft.client.gui.GuiScreenWorking
net.minecraft.client.gui.GuiSlot
net.minecraft.client.gui.GuiVideoSettings
net.minecraft.client.model.ModelBox
net.minecraft.client.model.ModelPlayer
net.minecraft.client.model.ModelRenderer
net.minecraft.client.model.TexturedQuad
net.minecraft.client.multiplayer.WorldClient
net.minecraft.client.particle.ParticleItemPickup
net.minecraft.client.particle.ParticleManager
net.minecraft.client.renderer.BlockFluidRenderer
net.minecraft.client.renderer.BlockModelRenderer
net.minecraft.client.renderer.BufferBuilder
net.minecraft.client.renderer.ChunkRenderContainer
net.minecraft.client.renderer.EntityRenderer
net.minecraft.client.renderer.GlStateManager
net.minecraft.client.renderer.ImageBufferDownload
net.minecraft.client.renderer.ItemRenderer
net.minecraft.client.renderer.OpenGlHelper
net.minecraft.client.renderer.RenderGlobal
net.minecraft.client.renderer.RenderItem
net.minecraft.client.renderer.RenderList
net.minecraft.client.renderer.Tessellator
net.minecraft.client.renderer.ThreadDownloadImageData
net.minecraft.client.renderer.VboRenderList
net.minecraft.client.renderer.VertexBufferUploader
net.minecraft.client.renderer.ViewFrustum
net.minecraft.client.renderer.WorldVertexBufferUploader
net.minecraft.client.renderer.block.model.BakedQuad
net.minecraft.client.renderer.block.model.BakedQuadRetextured
net.minecraft.client.renderer.block.model.FaceBakery
net.minecraft.client.renderer.block.model.ItemOverrideList
net.minecraft.client.renderer.block.model.ModelBakery
net.minecraft.client.renderer.block.model.ModelRotation
net.minecraft.client.renderer.chunk.ChunkRenderDispatcher
net.minecraft.client.renderer.chunk.CompiledChunk
net.minecraft.client.renderer.chunk.RenderChunk
net.minecraft.client.renderer.chunk.SetVisibility
net.minecraft.client.renderer.chunk.VisGraph
net.minecraft.client.renderer.culling.ClippingHelper
net.minecraft.client.renderer.culling.Frustum
net.minecraft.client.renderer.debug.DebugRendererChunkBorder
net.minecraft.client.renderer.entity.Render
net.minecraft.client.renderer.entity.RenderItemFrame
net.minecraft.client.renderer.entity.RenderLiving
net.minecraft.client.renderer.entity.RenderLivingBase
net.minecraft.client.renderer.entity.RenderManager
net.minecraft.client.renderer.entity.RenderXPOrb
net.minecraft.client.renderer.entity.layers.LayerArmorBase
net.minecraft.client.renderer.entity.layers.LayerCape
net.minecraft.client.renderer.entity.layers.LayerElytra
net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes
net.minecraft.client.renderer.entity.layers.LayerEndermanEyes
net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder
net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom
net.minecraft.client.renderer.entity.layers.LayerSheepWool
net.minecraft.client.renderer.entity.layers.LayerSpiderEyes
net.minecraft.client.renderer.entity.layers.LayerWolfCollar
net.minecraft.client.renderer.texture.AbstractTexture
net.minecraft.client.renderer.texture.DynamicTexture
net.minecraft.client.renderer.texture.ITextureObject
net.minecraft.client.renderer.texture.LayeredColorMaskTexture
net.minecraft.client.renderer.texture.LayeredTexture
net.minecraft.client.renderer.texture.SimpleTexture
net.minecraft.client.renderer.texture.Stitcher
net.minecraft.client.renderer.texture.TextureAtlasSprite
net.minecraft.client.renderer.texture.TextureManager
net.minecraft.client.renderer.texture.TextureMap
net.minecraft.client.renderer.texture.TextureUtil
net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer
net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer
net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
net.minecraft.client.renderer.tileentity.TileEntitySignRenderer
net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
net.minecraft.client.renderer.vertex.DefaultVertexFormats
net.minecraft.client.renderer.vertex.VertexBuffer
net.minecraft.client.resources.AbstractResourcePack
net.minecraft.client.resources.DefaultResourcePack
net.minecraft.client.resources.I18n
net.minecraft.client.resources.ResourcePackRepository
net.minecraft.client.settings.GameSettings
net.minecraft.crash.CrashReport
net.minecraft.entity.EntityLiving
net.minecraft.network.PacketThreadUtil
net.minecraft.network.datasync.EntityDataManager
net.minecraft.potion.PotionUtils
net.minecraft.profiler.Profiler
net.minecraft.server.integrated.IntegratedServer
net.minecraft.server.management.PlayerChunkMap
net.minecraft.util.ClassInheritanceMultiMap
net.minecraft.util.EnumFacing
net.minecraft.util.IntegerCache
net.minecraft.util.ScreenShotHelper
net.minecraft.util.Util
net.minecraft.util.math.ChunkPos
net.minecraft.util.math.MathHelper
net.minecraft.world.GameRules
net.minecraft.world.WorldEntitySpawner
net.minecraft.world.chunk.storage.ExtendedBlockStorage
net.minecraft.world.gen.layer.GenLayerZoom
    */
}
