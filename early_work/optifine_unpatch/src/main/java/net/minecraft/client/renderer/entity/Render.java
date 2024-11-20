/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  javax.annotation.Nullable
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class Render<T extends Entity> {
    private static final ResourceLocation SHADOW_TEXTURES = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    protected float shadowSize;
    protected float shadowOpaque = 1.0f;
    protected boolean renderOutlines;

    protected Render(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public void setRenderOutlines(boolean bl) {
        this.renderOutlines = bl;
    }

    public boolean shouldRender(T t, ICamera iCamera, double d, double d2, double d3) {
        AxisAlignedBB axisAlignedBB = t.getRenderBoundingBox().grow(0.5);
        if (axisAlignedBB.hasNaN() || axisAlignedBB.getAverageEdgeLength() == 0.0) {
            axisAlignedBB = new AxisAlignedBB(((Entity)t).posX - 2.0, ((Entity)t).posY - 2.0, ((Entity)t).posZ - 2.0, ((Entity)t).posX + 2.0, ((Entity)t).posY + 2.0, ((Entity)t).posZ + 2.0);
        }
        return t.isInRangeToRender3d(d, d2, d3) && (((Entity)t).ignoreFrustumCheck || iCamera.isBoundingBoxInFrustum(axisAlignedBB));
    }

    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        if (!this.renderOutlines) {
            this.renderName(t, d, d2, d3);
        }
    }

    protected int getTeamColor(T t) {
        int n = 0xFFFFFF;
        ScorePlayerTeam \u26032 = (ScorePlayerTeam)t.getTeam();
        if (\u26032 != null && (\u2603 = FontRenderer.getFormatFromString((String)\u26032.getPrefix())).length() >= 2) {
            n = this.getFontRendererFromRenderManager().getColorCode(\u2603.charAt(1));
        }
        return n;
    }

    protected void renderName(T t, double d, double d2, double d3) {
        if (!this.canRenderName(t)) {
            return;
        }
        this.renderLivingLabel(t, t.getDisplayName().getFormattedText(), d, d2, d3, 64);
    }

    protected boolean canRenderName(T t) {
        return t.getAlwaysRenderNameTagForRender() && t.hasCustomName();
    }

    protected void renderEntityName(T t, double d, double d2, double d3, String string, double d4) {
        this.renderLivingLabel(t, string, d, d2, d3, 64);
    }

    @Nullable
    protected abstract ResourceLocation getEntityTexture(T var1);

    protected boolean bindEntityTexture(T t) {
        ResourceLocation resourceLocation = this.getEntityTexture(t);
        if (resourceLocation == null) {
            return false;
        }
        this.bindTexture(resourceLocation);
        return true;
    }

    public void bindTexture(ResourceLocation resourceLocation) {
        this.renderManager.renderEngine.bindTexture(resourceLocation);
    }

    private void renderEntityOnFire(Entity entity, double d, double d2, double d3, float f) {
        GlStateManager.disableLighting();
        TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite \u26032 = textureMap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite \u26033 = textureMap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)d), (float)((float)d2), (float)((float)d3));
        float \u26034 = entity.width * 1.4f;
        GlStateManager.scale((float)\u26034, (float)\u26034, (float)\u26034);
        Tessellator \u26035 = Tessellator.getInstance();
        BufferBuilder \u26036 = \u26035.getBuffer();
        float \u26037 = 0.5f;
        float \u26038 = 0.0f;
        float \u26039 = entity.height / \u26034;
        float \u260310 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate((float)(-this.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)(-0.3f + (float)((int)\u26039) * 0.02f));
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float \u260311 = 0.0f;
        int \u260312 = 0;
        \u26036.begin(7, DefaultVertexFormats.POSITION_TEX);
        while (\u26039 > 0.0f) {
            TextureAtlasSprite textureAtlasSprite = \u260312 % 2 == 0 ? \u26032 : \u26033;
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float \u260313 = textureAtlasSprite.getMinU();
            float \u260314 = textureAtlasSprite.getMinV();
            float \u260315 = textureAtlasSprite.getMaxU();
            float \u260316 = textureAtlasSprite.getMaxV();
            if (\u260312 / 2 % 2 == 0) {
                float f2 = \u260315;
                \u260315 = \u260313;
                \u260313 = f2;
            }
            \u26036.pos((double)(\u26037 - 0.0f), (double)(0.0f - \u260310), (double)\u260311).tex((double)\u260315, (double)\u260316).endVertex();
            \u26036.pos((double)(-\u26037 - 0.0f), (double)(0.0f - \u260310), (double)\u260311).tex((double)\u260313, (double)\u260316).endVertex();
            \u26036.pos((double)(-\u26037 - 0.0f), (double)(1.4f - \u260310), (double)\u260311).tex((double)\u260313, (double)\u260314).endVertex();
            \u26036.pos((double)(\u26037 - 0.0f), (double)(1.4f - \u260310), (double)\u260311).tex((double)\u260315, (double)\u260314).endVertex();
            \u26039 -= 0.45f;
            \u260310 -= 0.45f;
            \u26037 *= 0.9f;
            \u260311 += 0.03f;
            ++\u260312;
        }
        \u26035.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderShadow(Entity entity2, double d, double d2, double d3, float f, float f2) {
        Entity entity2;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.renderManager.renderEngine.bindTexture(SHADOW_TEXTURES);
        World world = this.getWorldFromRenderManager();
        GlStateManager.depthMask((boolean)false);
        float \u26032 = this.shadowSize;
        if (entity2 instanceof EntityLiving) {
            EntityLiving entityLiving = (EntityLiving)entity2;
            \u26032 *= entityLiving.getRenderSizeModifier();
            if (entityLiving.isChild()) {
                \u26032 *= 0.5f;
            }
        }
        double \u26033 = entity2.lastTickPosX + (entity2.posX - entity2.lastTickPosX) * (double)f2;
        double \u26034 = entity2.lastTickPosY + (entity2.posY - entity2.lastTickPosY) * (double)f2;
        double \u26035 = entity2.lastTickPosZ + (entity2.posZ - entity2.lastTickPosZ) * (double)f2;
        int \u26036 = MathHelper.floor((double)(\u26033 - (double)\u26032));
        int \u26037 = MathHelper.floor((double)(\u26033 + (double)\u26032));
        int \u26038 = MathHelper.floor((double)(\u26034 - (double)\u26032));
        int \u26039 = MathHelper.floor((double)\u26034);
        int \u260310 = MathHelper.floor((double)(\u26035 - (double)\u26032));
        int \u260311 = MathHelper.floor((double)(\u26035 + (double)\u26032));
        double \u260312 = d - \u26033;
        double \u260313 = d2 - \u26034;
        double \u260314 = d3 - \u26035;
        Tessellator \u260315 = Tessellator.getInstance();
        BufferBuilder \u260316 = \u260315.getBuffer();
        \u260316.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (BlockPos blockPos : BlockPos.getAllInBoxMutable((BlockPos)new BlockPos(\u26036, \u26038, \u260310), (BlockPos)new BlockPos(\u26037, \u26039, \u260311))) {
            IBlockState iBlockState = world.getBlockState(blockPos.down());
            if (iBlockState.i() == EnumBlockRenderType.INVISIBLE || world.getLightFromNeighbors(blockPos) <= 3) continue;
            this.renderShadowSingle(iBlockState, d, d2, d3, blockPos, f, \u26032, \u260312, \u260313, \u260314);
        }
        \u260315.draw();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask((boolean)true);
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.world;
    }

    private void renderShadowSingle(IBlockState iBlockState, double d, double d2, double d3, BlockPos blockPos, float f, float f2, double d4, double d5, double d6) {
        if (!iBlockState.g()) {
            return;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder \u26032 = tessellator.getBuffer();
        double \u26033 = ((double)f - (d2 - ((double)blockPos.q() + d5)) / 2.0) * 0.5 * (double)this.getWorldFromRenderManager().getLightBrightness(blockPos);
        if (\u26033 < 0.0) {
            return;
        }
        if (\u26033 > 1.0) {
            \u26033 = 1.0;
        }
        AxisAlignedBB \u26034 = iBlockState.e((IBlockAccess)this.getWorldFromRenderManager(), blockPos);
        double \u26035 = (double)blockPos.p() + \u26034.minX + d4;
        double \u26036 = (double)blockPos.p() + \u26034.maxX + d4;
        double \u26037 = (double)blockPos.q() + \u26034.minY + d5 + 0.015625;
        double \u26038 = (double)blockPos.r() + \u26034.minZ + d6;
        double \u26039 = (double)blockPos.r() + \u26034.maxZ + d6;
        float \u260310 = (float)((d - \u26035) / 2.0 / (double)f2 + 0.5);
        float \u260311 = (float)((d - \u26036) / 2.0 / (double)f2 + 0.5);
        float \u260312 = (float)((d3 - \u26038) / 2.0 / (double)f2 + 0.5);
        float \u260313 = (float)((d3 - \u26039) / 2.0 / (double)f2 + 0.5);
        \u26032.pos(\u26035, \u26037, \u26038).tex((double)\u260310, (double)\u260312).color(1.0f, 1.0f, 1.0f, (float)\u26033).endVertex();
        \u26032.pos(\u26035, \u26037, \u26039).tex((double)\u260310, (double)\u260313).color(1.0f, 1.0f, 1.0f, (float)\u26033).endVertex();
        \u26032.pos(\u26036, \u26037, \u26039).tex((double)\u260311, (double)\u260313).color(1.0f, 1.0f, 1.0f, (float)\u26033).endVertex();
        \u26032.pos(\u26036, \u26037, \u26038).tex((double)\u260311, (double)\u260312).color(1.0f, 1.0f, 1.0f, (float)\u26033).endVertex();
    }

    public static void renderOffsetAABB(AxisAlignedBB axisAlignedBB, double d, double d2, double d3) {
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder \u26032 = tessellator.getBuffer();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        \u26032.setTranslation(d, d2, d3);
        \u26032.begin(7, DefaultVertexFormats.POSITION_NORMAL);
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        \u26032.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        tessellator.draw();
        \u26032.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableTexture2D();
    }

    public void doRenderShadowAndFire(Entity entity, double d, double d2, double d3, float f, float f2) {
        if (this.renderManager.options == null) {
            return;
        }
        if (this.renderManager.options.entityShadows && this.shadowSize > 0.0f && !entity.isInvisible() && this.renderManager.isRenderShadow() && (\u2603 = (float)((1.0 - (\u2603 = this.renderManager.getDistanceToCamera(entity.posX, entity.posY, entity.posZ)) / 256.0) * (double)this.shadowOpaque)) > 0.0f) {
            this.renderShadow(entity, d, d2, d3, \u2603, f2);
        }
        if (!(!entity.canRenderOnFire() || entity instanceof EntityPlayer && ((EntityPlayer)entity).isSpectator())) {
            this.renderEntityOnFire(entity, d, d2, d3, f2);
        }
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }

    protected void renderLivingLabel(T t, String string, double d, double d2, double d3, int n) {
        double d4 = t.getDistanceSq(this.renderManager.renderViewEntity);
        if (d4 > (double)(n * n)) {
            return;
        }
        boolean \u26032 = t.isSneaking();
        float \u26033 = this.renderManager.playerViewY;
        float \u26034 = this.renderManager.playerViewX;
        boolean \u26035 = this.renderManager.options.thirdPersonView == 2;
        float \u26036 = ((Entity)t).height + 0.5f - (\u26032 ? 0.25f : 0.0f);
        int \u26037 = "deadmau5".equals((Object)string) ? -10 : 0;
        EntityRenderer.drawNameplate((FontRenderer)this.getFontRendererFromRenderManager(), (String)string, (float)((float)d), (float)((float)d2 + \u26036), (float)((float)d3), (int)\u26037, (float)\u26033, (float)\u26034, (boolean)\u26035, (boolean)\u26032);
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    public boolean isMultipass() {
        return false;
    }

    public void renderMultipass(T t, double d, double d2, double d3, float f, float f2) {
    }
}
