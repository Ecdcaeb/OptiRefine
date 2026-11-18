package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.shaders.modern.ModernShaderPipeline;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.optifine.shaders.Shaders;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ShaderPassContracts {
    private static final String BASE = "shadow-pass-properties.zip";
    private static final String COMPLEMENT = "shadow-pass-complement.zip";
    private static final String DEFAULTS = "shadow-pass-defaults.zip";
    private static final String PROPERTIES = "shaders/shaders.properties";
    private static final int FIRST_ENTITY = Integer.MIN_VALUE + 7200;
    private static final Map<BlockPos, IBlockState> GLASS_SITE = new LinkedHashMap<>();
    private static final List<AxisAlignedBB> FAR_BOXES = List.of(
            box(1_000_000, 0, 0), box(-1_000_000, 0, 0), box(0, 1_000_000, 0),
            box(0, -1_000_000, 0), box(0, 0, 1_000_000), box(0, 0, -1_000_000));

    private ShaderPassContracts() {}

    static void prepare(Minecraft mc) throws IOException {
        Path packs = mc.gameDir.toPath().resolve("shaderpacks");
        createVariant(packs, COMPLEMENT, """
                shadowTerrain=true
                shadowEntities=true
                shadowBlockEntities=false
                shadow.culling=false
                particles.before.deferred=true
                shadowTranslucent=false
                """);
        createVariant(packs, DEFAULTS, null);
        BlockPos center = mc.player.getPosition().up(5);
        for (int z = -4; z <= 4; z++) {
            for (int x = -4; x <= 4; x++) {
                BlockPos pos = center.add(x, 0, z);
                GLASS_SITE.put(pos, mc.world.getBlockState(pos));
                mc.world.setBlockState(pos, Blocks.STAINED_GLASS.getDefaultState(), 3);
            }
        }
    }

    private static void createVariant(Path packs, String name, String properties) throws IOException {
        try (ZipInputStream source = new ZipInputStream(Files.newInputStream(packs.resolve(BASE)));
             ZipOutputStream destination = new ZipOutputStream(Files.newOutputStream(packs.resolve(name)))) {
            for (ZipEntry entry; (entry = source.getNextEntry()) != null;) {
                if (entry.isDirectory() || entry.getName().equals(PROPERTIES)) continue;
                destination.putNextEntry(new ZipEntry(entry.getName()));
                source.transferTo(destination);
                destination.closeEntry();
            }
            if (properties != null) {
                destination.putNextEntry(new ZipEntry(PROPERTIES));
                destination.write(properties.getBytes(StandardCharsets.US_ASCII));
                destination.closeEntry();
            }
        }
    }

    static boolean verify(Minecraft mc, String name) throws ReflectiveOperationException {
        Expected expected = switch (name) {
            case BASE -> new Expected(false, false, true, false, false, true);
            case COMPLEMENT -> new Expected(true, true, false, false, true, false);
            case DEFAULTS -> new Expected(true, true, true, true, true, true);
            default -> null;
        };
        if (expected == null) return false;
        try {
            verifyFrame(mc, name, expected);
            return true;
        } finally {
            if (name.equals(DEFAULTS)) {
                GLASS_SITE.forEach((pos, state) -> mc.world.setBlockState(pos, state, 3));
                GLASS_SITE.clear();
            }
        }
    }

    private static void verifyFrame(Minecraft mc, String name, Expected expected) throws ReflectiveOperationException {
        require(ModernShaderPipeline.isReady() && Shaders.ProgramShadow.getId() != 0
                && Shaders.ProgramsDeferred[0].getId() != 0, name, "expected active shadow and deferred programs");
        RenderManager manager = mc.getRenderManager();
        TileEntityRendererDispatcher dispatcher = TileEntityRendererDispatcher.instance;
        List<ProbeEntity> entities = List.of(new ProbeEntity(mc, 0, false), new ProbeEntity(mc, 1, false), new ProbeEntity(mc, 0, true));
        List<ProbeEntity> weather = List.of(new ProbeEntity(mc, 0, false), new ProbeEntity(mc, 1, false));
        List<ProbeTile> tiles = List.of(new ProbeTile(mc, 0), new ProbeTile(mc, 1));
        Render<? extends Entity> previousEntity = manager.entityRenderMap.put(ProbeEntity.class, new ProbeRenderer(manager));
        TileEntitySpecialRenderer<ProbeTile> tileRenderer = new TileEntitySpecialRenderer<>() {
            @Override
            public void render(ProbeTile tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
                tile.draws.record();
            }
        };
        tileRenderer.setRendererDispatcher(dispatcher);
        TileEntitySpecialRenderer<? extends TileEntity> previousTile = dispatcher.renderers.put(ProbeTile.class, tileRenderer);
        ProbeParticle lit = new ProbeParticle(mc, 3);
        ProbeParticle unlit = new ProbeParticle(mc, 0);
        FrameProbe frame = new FrameProbe();
        int previousPass = MinecraftForgeClient.getRenderPass();
        boolean previousAnaglyph = mc.gameSettings.anaglyph;
        try {
            for (int i = 0; i < entities.size(); i++) mc.world.addEntityToWorld(FIRST_ENTITY + i, entities.get(i));
            mc.world.weatherEffects.addAll(weather);
            mc.renderGlobal.updateTileEntities(List.of(), List.copyOf(tiles));
            mc.effectRenderer.addEffect(lit);
            mc.effectRenderer.addEffect(unlit);
            mc.effectRenderer.updateEffects();
            MinecraftForge.EVENT_BUS.register(frame);
            mc.gameSettings.anaglyph = false;
            mc.getFramebuffer().bindFramebuffer(true);
            mc.profiler.startSection("shaderPassContract");
            try {
                mc.entityRenderer.renderWorld(0.0F, 0L);
            } finally {
                mc.profiler.endSection();
            }
            require(!Shaders.isShadowPass && MinecraftForgeClient.getRenderPass() == -1, name, "render phase leaked");
            for (int pass = 0; pass < 2; pass++) {
                checkDraws(entities.get(pass).draws, pass, expected.entities, name, "entity");
                checkDraws(weather.get(pass).draws, pass, expected.entities, name, "weather entity");
                checkDraws(tiles.get(pass).draws, pass, expected.blockEntities, name, "block entity");
            }
            checkDraws(entities.get(2).draws, 0, expected.entities && !expected.culling, name, "out-of-frustum entity");
            Field shadowInfos = field(RenderGlobal.class, "optiRefine$shadowRenderInfos", "optiRefine$shadowRenderInfos");
            int casterChunks = ((List<?>) shadowInfos.get(mc.renderGlobal)).size();
            ViewFrustum frustum = (ViewFrustum) field(RenderGlobal.class, "viewFrustum", "field_175008_n").get(mc.renderGlobal);
            require(casterChunks > 0 && (casterChunks < frustum.renderChunks.length) == expected.culling,
                    name, "shadow caster chunk count " + casterChunks + " of " + frustum.renderChunks.length);
            int colors = shadowColors();
            require(((colors & 1) != 0) == expected.terrain, name, "solid shadow terrain colors " + colors);
            require(((colors & 2) != 0) == expected.translucent, name, "translucent shadow terrain colors " + colors);
            require(frame.draws == 1 && frame.image != 0, name, "expected one world render-last event");
            checkParticle(lit, frame, expected.beforeDeferred, name);
            checkParticle(unlit, frame, expected.beforeDeferred, name);
        } finally {
            mc.gameSettings.anaglyph = previousAnaglyph;
            ForgeHooksClient.setRenderPass(previousPass);
            MinecraftForge.EVENT_BUS.unregister(frame);
            lit.setExpired();
            unlit.setExpired();
            mc.renderGlobal.updateTileEntities(List.copyOf(tiles), List.of());
            mc.world.weatherEffects.removeAll(weather);
            for (int i = 0; i < entities.size(); i++) mc.world.removeEntityFromWorld(FIRST_ENTITY + i);
            if (previousEntity == null) manager.entityRenderMap.remove(ProbeEntity.class);
            else manager.entityRenderMap.put(ProbeEntity.class, previousEntity);
            if (previousTile == null) dispatcher.renderers.remove(ProbeTile.class);
            else dispatcher.renderers.put(ProbeTile.class, previousTile);
            mc.getFramebuffer().bindFramebuffer(true);
        }
    }

    private static int shadowColors() {
        int texture = ModernShaderPipeline.getShadowImageTexture(0);
        if (texture == 0) throw new AssertionError("Missing shadow fixture color texture");
        int previous = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GlStateManager.bindTexture(texture);
        try {
            int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
            int colors = 0;
            for (int i = 0; i < pixels.capacity(); i += 4) {
                int r = Byte.toUnsignedInt(pixels.get(i));
                int g = Byte.toUnsignedInt(pixels.get(i + 1));
                int b = Byte.toUnsignedInt(pixels.get(i + 2));
                if (r == 255 && g == 0 && b == 0) colors |= 1;
                if (r == 0 && g == 255 && b == 0) colors |= 2;
            }
            return colors;
        } finally {
            GlStateManager.bindTexture(previous);
        }
    }

    private static void checkDraws(Draws draws, int pass, boolean castsShadow, String name, String kind) {
        int mask = 1 << pass;
        require(draws.world == mask && draws.shadow == (castsShadow ? mask : 0), name,
                kind + " pass " + pass + " recorded world=" + draws.world + ", shadow=" + draws.shadow);
    }

    private static void checkParticle(ProbeParticle particle, FrameProbe frame, boolean beforeDeferred, String name) {
        require(particle.draws == 1 && particle.image != 0 && (particle.image != frame.image) == beforeDeferred,
                name, "particle layer " + particle.layer + " recorded draws=" + particle.draws
                        + ", color texture=" + particle.image + ", post-deferred texture=" + frame.image);
    }

    private static void require(boolean value, String name, String details) {
        if (!value) throw new AssertionError(name + ": " + details);
    }

    private static Field field(Class<?> type, String mcp, String srg) throws ReflectiveOperationException {
        Field field;
        try {
            field = type.getDeclaredField(mcp);
        } catch (NoSuchFieldException absentInNamespace) {
            field = type.getDeclaredField(srg);
        }
        field.setAccessible(true);
        return field;
    }

    private static AxisAlignedBB box(double x, double y, double z) {
        return new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
    }

    private record Expected(boolean terrain, boolean entities, boolean blockEntities, boolean culling,
                            boolean beforeDeferred, boolean translucent) {}

    private static final class Draws {
        int world;
        int shadow;

        void record() {
            int mask = 1 << MinecraftForgeClient.getRenderPass();
            if (Shaders.isShadowPass) shadow |= mask;
            else world |= mask;
        }
    }

    private static final class ProbeEntity extends EntityArmorStand {
        final int pass;
        final boolean testCulling;
        final Draws draws = new Draws();

        ProbeEntity(Minecraft mc, int pass, boolean testCulling) {
            super(mc.world, mc.player.posX, mc.player.posY, mc.player.posZ);
            this.pass = pass;
            this.testCulling = testCulling;
        }

        @Override public boolean shouldRenderInPass(int pass) { return this.pass == pass; }
    }

    private static final class ProbeRenderer extends Render<ProbeEntity> {
        ProbeRenderer(RenderManager manager) { super(manager); }

        @Override
        public boolean shouldRender(ProbeEntity entity, ICamera camera, double x, double y, double z) {
            return !entity.testCulling || !Shaders.isShadowPass || FAR_BOXES.stream().allMatch(camera::isBoundingBoxInFrustum);
        }

        @Override
        public void doRender(ProbeEntity entity, double x, double y, double z, float yaw, float partialTicks) {
            entity.draws.record();
        }

        @Override protected ResourceLocation getEntityTexture(ProbeEntity entity) { return null; }
    }

    private static final class ProbeTile extends TileEntity {
        final int pass;
        final Draws draws = new Draws();

        ProbeTile(Minecraft mc, int pass) {
            this.pass = pass;
            setWorld(mc.world);
            setPos(mc.player.getPosition());
        }

        @Override public boolean shouldRenderInPass(int pass) { return this.pass == pass; }
        @Override public AxisAlignedBB getRenderBoundingBox() { return null; }
    }

    private static final class ProbeParticle extends Particle {
        final int layer;
        int draws;
        int image;

        ProbeParticle(Minecraft mc, int layer) {
            super(mc.world, mc.player.posX, mc.player.posY, mc.player.posZ);
            this.layer = layer;
        }

        @Override public int getFXLayer() { return layer; }

        @Override
        public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX,
                                   float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
            draws++;
            image = ModernShaderPipeline.getMainImageTexture(0);
        }
    }

    public static final class FrameProbe {
        int draws;
        int image;

        @SubscribeEvent
        public void renderLast(RenderWorldLastEvent event) {
            draws++;
            image = ModernShaderPipeline.getMainImageTexture(0);
        }
    }
}
