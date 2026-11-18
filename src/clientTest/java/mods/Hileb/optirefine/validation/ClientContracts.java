package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.resources.ModernResourcePaths;
import mods.Hileb.optirefine.optifine.shaders.modern.ModernShaderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.world.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.optifine.shaders.Programs;
import net.optifine.shaders.Shaders;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.*;

@Mod(modid = "optirefine_client_contracts", name = "OptiRefine Client Contracts", version = "1", clientSideOnly = true)
public final class ClientContracts {
    private static final String[] PACKS = {
            "modern-stages.zip", "modern-compute.zip", "modern-compute-only.zip", "modern-compute-half.zip",
            "modern-deferred.zip", "modern-graphics-images.zip", "modern-disabled.zip", "modern-zero.zip",
            "modern-resize-failure.zip", "modern-failure.zip", "documented-color-targets.zip",
            "shadow-pass-properties.zip", "shadow-pass-complement.zip", "shadow-pass-defaults.zip", "modern-compute.zip"
    };
    private final Path output = Path.of(System.getProperty("optirefine.contract.output"));
    private final long deadline = System.nanoTime() + 300_000_000_000L;
    private final StringBuilder evidence = new StringBuilder();
    private boolean launched, finished;
    private int ticks, pack = -1, packTicks;
    private boolean resized;
    private boolean resizeFailureRequested;
    private float originalRenderQuality;

    @Mod.EventHandler public void initialize(FMLInitializationEvent event) { MinecraftForge.EVENT_BUS.register(this); }

    @SubscribeEvent public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || finished) return;
        Minecraft mc = Minecraft.getMinecraft();
        try {
            if (System.nanoTime() > deadline) throw new AssertionError("Client contracts exceeded deadline");
            if (!launched && mc.currentScreen instanceof GuiMainMenu) {
                verifyEnvironment(mc);
                launched = true;
                mc.gameSettings.renderDistanceChunks = 4; mc.gameSettings.limitFramerate = 60;
                mc.gameSettings.enableVsync = false; mc.gameSettings.pauseOnLostFocus = false;
                mc.launchIntegratedServer("ClientContracts-" + System.currentTimeMillis(), "OptiRefine Client Contracts", new WorldSettings(1337L, GameType.CREATIVE, false, false, WorldType.FLAT));
            }
            if (mc.world == null || mc.player == null) return;
            ++ticks;
            if (ticks == 40) {
                mc.displayGuiScreen(new GuiVideoSettings(new GuiIngameMenu(), mc.gameSettings));
            } else if (ticks == 44) {
                screenshot(mc, "01-video-settings"); mc.displayGuiScreen(null);
                evidence.append("Real video settings initialize ordinary Config-facade helper classes\n");
            } else if (ticks == 70) {
                evidence.append("Actual OpenGL: ").append(GL11.glGetString(GL11.GL_VERSION)).append('\n');
                if (!Shaders.capabilities.OpenGL43) throw new AssertionError("Client contracts require OpenGL 4.3");
                resources(mc); sortedSprites(mc);
                ForgeModResourceContracts.verify(mc);
                evidence.append("Forge JAR resource packs supply working CIT and CTM rules with user-pack priority\n");
                PackSelectorContracts.verify(mc);
                evidence.append("CIT and Random Entities honor documented selectors, variant names and pack priority\n");
                SelectiveResourceContracts.verify(mc);
                evidence.append("AF, custom-items and alternate-block settings retain selective texture/model reloads and live sound/language consumers\n");
                NpotAtlasContracts.verify(mc);
                evidence.append("Rectangular sprites retain atlas mip levels, content and animation uploads\n");
                NpotAtlasContracts.verifyTextureProperties(mc);
                evidence.append("Texture alpha cutout follows the active pack and resets on reload\n");
                DimensionSkyContracts.verify(mc);
                evidence.append("Native sky rendering selects signed and sparse Forge dimensions across reloads\n");
                ShoulderTextureContracts.verify(mc);
                evidence.append("Native shoulder rendering retains random parrot texture rules across seating and reload\n");
                ShaderBiomeContracts.verify(mc);
                evidence.append("G5 custom uniforms use biome category and precipitation at the camera\n");
                CrashReportContracts.verify(mc);
                evidence.append("Chunk workers preserve crash details without a current OpenGL context\n");
                CemContextContracts.verify(mc);
                evidence.append("Worldless item chest CEM uses client/enclosing world values and restores nested and exceptional contexts\n");
                CemExpressionContracts.verify(mc);
                CemPartContracts.verify();
                evidence.append("G5 CEM evaluates documented predicates, visibility, box sizes and attachment transforms\n");
                GeometryContracts.verify();
                evidence.append("Quad facing and packed normals preserve compact/expanded layouts, winding ties and non-finite inputs\n");
                ChunkCacheContracts.verifyLightFloor(mc);
                evidence.append("Cached block light honors both orders of default and raised light-floor queries\n");
                FishingCullingContracts.verify(mc);
                evidence.append("Fishing line draws once when visible and its hook section is culled\n");
                ShaderLifecycleContracts.createResizeFailurePack(mc);
                ShaderPassContracts.prepare(mc);
                mc.gameSettings.showDebugInfo = true;
                nextPack();
            } else if (pack >= 0 && ++packTicks == 45) {
                if (PACKS[pack].equals("modern-resize-failure.zip") && !resizeFailureRequested) {
                    ShaderLifecycleContracts.requestInvalidResize();
                    resizeFailureRequested = true;
                    packTicks = 0;
                    return;
                }
                if (pack == 2 && !resized) {
                    originalRenderQuality = Shaders.configRenderResMul;
                    Shaders.configRenderResMul = 0.5F;
                    Shaders.scheduleResize();
                    resized = true;
                    packTicks = 0;
                    return;
                }
                if (pack == 2) {
                    if (Shaders.renderWidth != Math.round(mc.displayWidth * 0.5F)
                            || Shaders.renderHeight != Math.round(mc.displayHeight * 0.5F)) {
                        throw new AssertionError("Shader pack resize did not update borrowed targets");
                    }
                    Shaders.configRenderResMul = originalRenderQuality;
                    Shaders.scheduleResize();
                    evidence.append("Active compute-only pack survives render-quality resize\n");
                }
                checkPack(mc);
                if (pack + 1 == PACKS.length) {
                    NpotAtlasContracts.verifyShaderSidecars(mc);
                    evidence.append("Rectangular atlas uploads preserve shader normal and specular mipmaps\n");
                    NpotAtlasContracts.verifyLabPbr(mc);
                    evidence.append("LabPBR shader maps preserve channel filtering and format macros across reloads\n");
                    Shaders.setShaderPack("OFF");
                    ClientBehaviorContracts.verify(mc);
                    evidence.append("Native chat replacement, local reload commands, completions, notices and native debug-key handling passed\n");
                    mc.world.sendQuittingDisconnectingPacket(); mc.loadWorld(null); mc.displayGuiScreen(new GuiMainMenu());
                    finish(mc, "PASS\n" + evidence);
                } else nextPack();
            }
        } catch (Throwable error) { error.printStackTrace(); finish(mc, "FAIL\n" + evidence + error); }
    }

    private void verifyEnvironment(Minecraft mc) throws Exception {
        boolean runtime = Boolean.getBoolean("optirefine.contract.runtime");
        if (FMLLaunchHandler.isDeobfuscatedEnvironment() == runtime) {
            throw new AssertionError("Client contracts use the wrong Minecraft namespace");
        }
        ModContainer mod = Loader.instance().getIndexedModList().get("optirefine");
        File source = mod == null ? null : mod.getSource();
        File expected = new File(System.getProperty("optirefine.contract.artifact")).getCanonicalFile();
        File modsDirectory = new File(mc.gameDir, "mods").getCanonicalFile();
        if (source == null || !expected.equals(source.getCanonicalFile())
                || !expected.toPath().startsWith(modsDirectory.toPath())) {
            throw new AssertionError("Expected OptiRefine from " + expected + ", loaded from " + source);
        }
        if (runtime) {
            Minecraft.class.getDeclaredMethod("func_71410_x");
            evidence.append("Production SRG Minecraft loads final OptiRefine Jar and official G5 through runtime discovery\n");
        } else {
            evidence.append("MCP development client loads the OptiRefine development Jar through Forge discovery\n");
        }
    }

    private void resources(Minecraft mc) throws Exception {
        ResourcePackRepository repository = mc.getResourcePackRepository(); repository.updateRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> selected = new ArrayList<>();
        for (ResourcePackRepository.Entry entry : repository.getRepositoryEntriesAll()) if (entry.getResourcePackName().equals("modern-base.zip")) selected.add(entry);
        if (selected.size() != 1) throw new AssertionError("Modern CIT/CEM fixture is unavailable");
        repository.setRepositories(selected);
        ResourceLocation stale = new ResourceLocation("mcpatcher/cit/__client_contract_stale__.properties");
        ModernResourcePaths.forLocation(stale);
        Field paths = ModernResourcePaths.class.getDeclaredField("PACK_PATHS"); paths.setAccessible(true);
        mc.refreshResources();
        if (((Map<?, ?>)paths.get(null)).containsKey(stale)) throw new AssertionError("Resource reload retained stale translations");
        ResourceLocation rule = new ResourceLocation("mcpatcher/cit/modern.json");
        byte[] modern;
        try (IResource resource = mc.getResourceManager().getResource(rule)) { modern = resource.getInputStream().readAllBytes(); }
        FileResourcePack higher = new FileResourcePack(mc.gameDir.toPath().resolve("resourcepacks/higher-legacy.zip").toFile());
        FileResourcePack base = new FileResourcePack(mc.gameDir.toPath().resolve("resourcepacks/modern-base.zip").toFile());
        FallbackResourceManager priority = new FallbackResourceManager(new MetadataSerializer()); priority.addResourcePack(base); priority.addResourcePack(higher);
        try (IResource resource = priority.getResource(rule); InputStream expected = higher.getInputStream(rule)) {
            byte[] actual = resource.getInputStream().readAllBytes();
            if (!Arrays.equals(expected.readAllBytes(), actual) || Arrays.equals(modern, actual)) throw new AssertionError("Cross-pack precedence changed");
        }
        evidence.append("Modern CIT/CEM pack loads; legacy aliases, cross-pack priority and cache reload passed\n");
    }

    private void sortedSprites(Minecraft mc) throws Exception {
        TextureAtlasSprite near = mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone"), far = mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
        if (near == far) throw new AssertionError("Sprite fixtures are not distinct");
        BufferBuilder builder = new BufferBuilder(1024); builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        for (int z : new int[]{1, 5}) for (int vertex = 0; vertex < 4; vertex++) builder.pos(0, 0, z).tex(0, 0).endVertex();
        Field sprites = BufferBuilder.class.getDeclaredField("quadSprites"); sprites.setAccessible(true); sprites.set(builder, new TextureAtlasSprite[]{near, far});
        builder.sortVertexData(0, 0, 0); BufferBuilder.State snapshot = builder.getVertexState();
        builder.sortVertexData(0, 0, 6);
        Field saved = BufferBuilder.State.class.getDeclaredField("stateQuadSprites"); saved.setAccessible(true);
        if (((TextureAtlasSprite[])saved.get(snapshot))[0] != far || ((TextureAtlasSprite[])sprites.get(builder))[0] != near || builder.getByteBuffer().getFloat(8) != 1) throw new AssertionError("Quad/sprite ordering or snapshot ownership changed");
        builder.finishDrawing(); builder.reset(); evidence.append("Actual vertex sorting retains independent snapshots and sprite alignment\n");
    }

    private void nextPack() { ++pack; packTicks = 0; Shaders.setShaderPack(PACKS[pack]); }

    private void checkPack(Minecraft mc) throws Exception {
        String name = PACKS[pack];
        if (ShaderPassContracts.verify(mc, name)) {
            evidence.append("Native shadow caster properties and particle ordering passed for ").append(name).append('\n');
            return;
        }
        if (name.equals("documented-color-targets.zip")) {
            if (!ModernShaderPipeline.isReady()) throw new AssertionError("Extended color target pack did not initialize");
            int sizedTexture = ModernShaderPipeline.getMainImageTexture(5);
            if (sizedTexture == 0) throw new AssertionError("Missing sized colortex5");
            mc.getFramebuffer().bindFramebuffer(true);
            mc.profiler.startSection("colorTargetContract");
            try {
                mc.entityRenderer.renderWorld(0.0F, 0L);
                pixelTexture(mc.getFramebuffer().framebufferTexture, mc.displayWidth / 2, mc.displayHeight / 4,
                        new int[]{64, 128, 192}, "Final color output");
            } finally {
                mc.profiler.endSection();
            }
            verifyRelease(sizedTexture);
            evidence.append("Named outputs, legacy target 8, render-relative and fixed color targets pass pixel and cleanup checks\n");
            return;
        }
        boolean shaders = (Boolean)Class.forName("Config").getMethod("isShaders").invoke(null);
        if (name.equals("modern-resize-failure.zip")) {
            ShaderLifecycleContracts.verifyResizeRecovery(mc);
            screenshot(mc, "04-resize-off-recovery");
            evidence.append("Active compute pack resize failure switches OFF, releases owned programs and retains the live world/framebuffer\n");
            return;
        }
        if (name.equals("modern-failure.zip")) {
            if (shaders || ModernShaderPipeline.isReady()) throw new AssertionError("Invalid pack did not recover through OFF");
            evidence.append("Invalid compute compilation recovers through OFF\n"); return;
        }
        if (!shaders || !ModernShaderPipeline.isReady()) throw new AssertionError("Shader pack not ready: " + name);
        if (name.equals("modern-disabled.zip")) {
            if (ModernShaderPipeline.getDispatchCount() != 0) throw new AssertionError("Disabled stage dispatched");
        } else {
            if (ModernShaderPipeline.getDispatchCount() == 0) throw new AssertionError("Compute-only stages were lost");
            pixel(name.equals("modern-graphics-images.zip") ? 5 : 0, name.equals("modern-stages.zip") ? new int[]{160,128,192} : new int[]{128,128,224});
        }
        if (name.equals("modern-stages.zip")) {
            ShaderLifecycleContracts.verifySamplers();
            ShaderVertexContracts.verify();
            evidence.append("Shader vertex submission retains shared sources and applies distinct entity tags to appended quads\n");
            evidence.append("Actual GLSL samplers see prepare/shadowcomp flips and preserve custom sampler overrides\n");
            EntityRenderContracts.verify(mc);
            evidence.append("Faulting entity renderer restores the incoming entity, counter, profiler and Forge pass\n");
            TntFlashContracts.verify(mc);
            evidence.append("TNT flash and quiet phases produce the expected entityColor pixels\n");
            FishingShaderContracts.verify(mc);
            evidence.append("Fishing line and bobber draw with separate basic and entity shader programs\n");
        }
        if (pack == 0) screenshot(mc, "02-prepare-shadowcomp-debug");
        if (pack == 5) screenshot(mc, "03-graphics-images");
        verifyRelease();
        evidence.append(name).append(": pixels, stage selection, owned deletion and borrowed texture survival passed\n");
    }

    private void verifyRelease(int... ownedTargets) throws Exception {
        int borrowed = ModernShaderPipeline.getMainImageTexture(0);
        List<Integer> programs = new ArrayList<>(), alternates = new ArrayList<>();
        Field registry = Shaders.class.getDeclaredField("programs"); registry.setAccessible(true);
        int count = ((Programs)registry.get(null)).getCount();
        for (int index = 0; index < count; index++) for (int stage = 0; stage < ModernShaderPipeline.getComputeProgramCount(index); stage++) programs.add(ModernShaderPipeline.getComputeProgramId(index, stage));
        for (int index = 0; index < 8; index++) if (ModernShaderPipeline.getShadowAlternateTexture(index) != 0) alternates.add(ModernShaderPipeline.getShadowAlternateTexture(index));
        ModernShaderPipeline.close(); ModernShaderPipeline.close();
        if (ModernShaderPipeline.isReady()) throw new AssertionError("Closed pack remains ready");
        for (int id : programs) if (GL20.glIsProgram(id)) throw new AssertionError("Owned compute program survives close");
        for (int id : alternates) if (GL11.glIsTexture(id)) throw new AssertionError("Owned shadow alternate survives close");
        for (int id : ownedTargets) if (GL11.glIsTexture(id)) throw new AssertionError("Owned color target survives close");
        if (borrowed != 0 && !GL11.glIsTexture(borrowed)) throw new AssertionError("Pack state deleted a borrowed G5 texture");
        Shaders.uninit();
    }

    private static void pixel(int index, int[] expected) {
        int texture = ModernShaderPipeline.getMainImageTexture(index);
        if (texture == 0) throw new AssertionError("Missing image texture " + index);
        pixelTexture(texture, 2, 2, expected, "Image " + index);
    }

    private static void pixelTexture(int texture, int x, int y, int[] expected, String name) {
        int previous = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING), framebuffer = GL30.glGenFramebuffers();
        ByteBuffer sample = BufferUtils.createByteBuffer(4);
        try {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebuffer);
            GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
            GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
            GL11.glReadPixels(x, y, 1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, sample);
            for (int channel = 0; channel < 3; channel++) {
                if (Math.abs((sample.get(channel) & 255) - expected[channel]) > 3) {
                    throw new AssertionError(name + " expected " + Arrays.toString(expected) + " got "
                            + (sample.get(0) & 255) + "," + (sample.get(1) & 255) + "," + (sample.get(2) & 255));
                }
            }
        } finally { GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, previous); GL30.glDeleteFramebuffers(framebuffer); }
    }

    private void screenshot(Minecraft mc, String name) throws IOException { Files.createDirectories(output); ScreenShotHelper.saveScreenshot(output.toFile(), name + ".png", mc.displayWidth, mc.displayHeight, mc.getFramebuffer()); }
    private void finish(Minecraft mc, String result) {
        finished = true;
        try { Files.createDirectories(output); Files.writeString(output.resolve("result.txt"), result); } catch (IOException error) { error.printStackTrace(); }
        System.out.println("OPTIRE FINE CLIENT CONTRACTS: " + result); mc.shutdown();
    }
}
