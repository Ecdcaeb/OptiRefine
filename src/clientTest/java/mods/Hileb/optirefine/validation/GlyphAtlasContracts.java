package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class GlyphAtlasContracts {
    private static final int SIZE = 16;
    private static final int RED = 0xFFFF0000;
    private static final int GREEN = 0xFF00FF00;
    private static final int BLUE = 0xFF0000FF;
    private static final int MAGENTA = 0xFFFF00FF;
    private static final ResourceLocation DYNAMIC = new ResourceLocation("optirefine_contracts", "live_atlas_sprite");
    private static final ResourceLocation MIRRORED = new ResourceLocation("optirefine_contracts", "mirrored_atlas_sprite");

    private GlyphAtlasContracts() {}

    public static void verify(Minecraft mc) throws Exception {
        TextureMap atlas = mc.getTextureMapBlocks();
        Field afLevel = GameSettings.class.getField("ofAfLevel");
        int previousAf = afLevel.getInt(mc.gameSettings);
        int previousBinding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        boolean atlasWasBound = previousBinding == atlas.getGlTextureId();
        Registration registration = new Registration(atlas);
        MinecraftForge.EVENT_BUS.register(registration);
        try {
            afLevel.setInt(mc.gameSettings, 4);
            mc.refreshResources();
            require(atlas.getAtlasSprite(DYNAMIC.toString()) == registration.dynamic, "Custom sprite was not stitched");
            require(atlas.getAtlasSprite(MIRRORED.toString()) == registration.mirrored, "Mirrored sprite was not stitched");
            Field textureId = TextureAtlasSprite.class.getField("glSpriteTextureId");
            require(textureId.getInt(registration.dynamic) < 0, "Atlas-only sprite acquired a standalone texture");
            require(textureId.getInt(registration.mirrored) > 0, "Ordinary sprite lost its standalone texture");
            TextureAtlasSprite clipped = new ClippedSprite(registration.dynamic);
            require((int) TextureAtlasSprite.class.getMethod("getIndexInMap").invoke(clipped) == -1,
                    "Clipped view unexpectedly has an atlas index");
            verifyBakedCoordinates(clipped);
            verifyDraw(atlas, registration, clipped, GREEN);
            GlStateManager.bindTexture(atlas.getGlTextureId());
            registration.dynamic.uploadRightHalf(atlas.getMipmapLevels(), BLUE);
            verifyDraw(atlas, registration, clipped, BLUE);
        } finally {
            MinecraftForge.EVENT_BUS.unregister(registration);
            afLevel.setInt(mc.gameSettings, previousAf);
            try {
                mc.refreshResources();
            } finally {
                GlStateManager.bindTexture(atlasWasBound ? atlas.getGlTextureId()
                        : GL11.glIsTexture(previousBinding) ? previousBinding : 0);
            }
        }
    }

    private static void verifyBakedCoordinates(TextureAtlasSprite clipped) throws ReflectiveOperationException {
        int stride = DefaultVertexFormats.ITEM.getIntegerSize();
        int uv = DefaultVertexFormats.ITEM.getUvOffsetById(0) / Integer.BYTES;
        int[] packed = new int[4 * stride];
        for (int vertex = 0; vertex < 4; vertex++) {
            packed[vertex * stride + uv] = Float.floatToRawIntBits(vertex < 2 ? clipped.getMinU() : clipped.getMaxU());
            packed[vertex * stride + uv + 1] = Float.floatToRawIntBits(vertex % 2 == 0 ? clipped.getMinV() : clipped.getMaxV());
        }
        BakedQuad quad = new BakedQuad(packed, -1, EnumFacing.SOUTH, clipped, false, DefaultVertexFormats.ITEM);
        int[] converted = (int[]) BakedQuad.class.getMethod("getVertexDataSingle").invoke(quad);
        require(Arrays.equals(packed, converted), "Baked glyph quad lost its atlas coordinates");
    }

    private static void verifyDraw(TextureMap atlas, Registration registration, TextureAtlasSprite clipped, int expectedClipped)
            throws ReflectiveOperationException {
        Method setLayer = BufferBuilder.class.getMethod("setBlockLayer", BlockRenderLayer.class);
        Method setSprite = BufferBuilder.class.getMethod("setSprite", TextureAtlasSprite.class);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int previousRead = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING);
        int previousDraw = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        int previousMatrix = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        int previousTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, SIZE, 0, SIZE, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Framebuffer framebuffer = null;
        try {
            framebuffer = new Framebuffer(SIZE, SIZE, false);
            framebuffer.bindFramebuffer(true);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1, 1, 1, 1);
            GlStateManager.bindTexture(atlas.getGlTextureId());
            setLayer.invoke(buffer, BlockRenderLayer.TRANSLUCENT);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            require((boolean) BufferBuilder.class.getMethod("isMultiTexture").invoke(buffer), "AF 4× did not enable multi-texture");
            quad(buffer, setSprite, registration.dynamic, 0.25F, 0, SIZE);
            quad(buffer, setSprite, clipped, 0.5F, 0, 12);
            quad(buffer, setSprite, registration.mirrored, 0.5F, 4, 12);
            quad(buffer, setSprite, clipped, 0.5F, 8, 12);
            tessellator.draw();
            require(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) == atlas.getGlTextureId(), "Draw lost its incoming atlas binding");
            ByteBuffer pixels = BufferUtils.createByteBuffer(SIZE * SIZE * 4);
            GL11.glReadPixels(0, 0, SIZE, SIZE, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
            require(pixel(pixels, 2) == expectedClipped, "Clipped view sampled the wrong atlas region");
            require(pixel(pixels, 6) == MAGENTA, "Standalone quad did not draw between clipped quads");
            require(pixel(pixels, 10) == expectedClipped, "Disjoint clipped run was dropped or drawn out of order");
            require(pixel(pixels, 14) == RED, "Atlas-only sprite did not draw from its uploaded frame");
        } finally {
            setLayer.invoke(buffer, (Object) null);
            if (framebuffer != null) framebuffer.deleteFramebuffer();
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, previousRead);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previousDraw);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(previousMatrix);
            GlStateManager.bindTexture(previousTexture);
            GL11.glPopAttrib();
        }
    }

    private static void quad(BufferBuilder buffer, Method setSprite, TextureAtlasSprite sprite, float sample, int left, int right)
            throws ReflectiveOperationException {
        setSprite.invoke(buffer, sprite);
        double u = sprite.getMinU() + (sprite.getMaxU() - sprite.getMinU()) * sample;
        double v = (sprite.getMinV() + sprite.getMaxV()) / 2;
        buffer.pos(left, 0, 0).tex(u, v).endVertex();
        buffer.pos(right, 0, 0).tex(u, v).endVertex();
        buffer.pos(right, SIZE, 0).tex(u, v).endVertex();
        buffer.pos(left, SIZE, 0).tex(u, v).endVertex();
    }

    private static int pixel(ByteBuffer pixels, int x) {
        int index = (SIZE / 2 * SIZE + x) * 4;
        return (pixels.get(index + 3) & 255) << 24 | (pixels.get(index) & 255) << 16
                | (pixels.get(index + 1) & 255) << 8 | (pixels.get(index + 2) & 255);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static final class Registration {
        private final TextureMap atlas;
        private AtlasSprite dynamic;
        private AtlasSprite mirrored;

        private Registration(TextureMap atlas) {
            this.atlas = atlas;
        }

        @SubscribeEvent
        public void before(TextureStitchEvent.Pre event) {
            if (event.getMap() == atlas) {
                dynamic = new AtlasSprite(DYNAMIC, false);
                mirrored = new AtlasSprite(MIRRORED, true);
                require(atlas.setTextureEntry(dynamic), "Forge rejected atlas-only sprite");
                require(atlas.setTextureEntry(mirrored), "Forge rejected mirrored sprite");
            }
        }
    }

    private static final class AtlasSprite extends TextureAtlasSprite {
        private final boolean mirrorFrames;

        private AtlasSprite(ResourceLocation name, boolean mirrorFrames) {
            super(name.toString());
            this.mirrorFrames = mirrorFrames;
            setIconWidth(SIZE);
            setIconHeight(SIZE);
        }

        @Override
        public boolean hasCustomLoader(IResourceManager manager, ResourceLocation name) {
            return true;
        }

        @Override
        public boolean load(IResourceManager manager, ResourceLocation name, Function<ResourceLocation, TextureAtlasSprite> sprites) {
            return false;
        }

        @Override
        public void generateMipmaps(int levels) {
            int[][] frame = mipmaps(levels, mirrorFrames ? MAGENTA : RED, mirrorFrames ? MAGENTA : GREEN);
            if (mirrorFrames) {
                setFramesTextureData(List.<int[][]>of(frame));
            } else {
                clearFramesTextureData();
                framesTextureData.add(frame);
            }
        }

        private void uploadRightHalf(int levels, int color) {
            int[][] frame = mipmaps(levels, RED, color);
            framesTextureData.set(0, frame);
            TextureUtil.uploadTextureMipmap(frame, SIZE, SIZE, getOriginX(), getOriginY(), false, false);
        }
    }

    private static int[][] mipmaps(int levels, int left, int right) {
        int[][] frame = new int[levels + 1][];
        frame[0] = new int[SIZE * SIZE];
        for (int y = 0; y < SIZE; y++) {
            Arrays.fill(frame[0], y * SIZE, y * SIZE + SIZE / 2, left);
            Arrays.fill(frame[0], y * SIZE + SIZE / 2, (y + 1) * SIZE, right);
        }
        return TextureUtil.generateMipmapData(levels, SIZE, frame);
    }

    private static final class ClippedSprite extends TextureAtlasSprite {
        private final TextureAtlasSprite parent;

        private ClippedSprite(TextureAtlasSprite parent) {
            super("optirefine_contracts:unregistered_clip");
            this.parent = parent;
            setIconWidth(SIZE / 2);
            setIconHeight(SIZE);
        }

        @Override
        public float getMinU() {
            return (parent.getMinU() + parent.getMaxU()) / 2;
        }

        @Override
        public float getMaxU() {
            return parent.getMaxU();
        }

        @Override
        public float getMinV() {
            return parent.getMinV();
        }

        @Override
        public float getMaxV() {
            return parent.getMaxV();
        }
    }
}
