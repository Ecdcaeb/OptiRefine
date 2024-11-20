/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Class
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
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.optifine.entity.model.IEntityRenderer
 *  net.optifine.shaders.Shaders
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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.shaders.Shaders;

public abstract class Render<T extends Entity>
implements IEntityRenderer {
    private static final ResourceLocation SHADOW_TEXTURES = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    public float shadowSize;
    protected float shadowOpaque = 1.0f;
    protected boolean renderOutlines;
    private Class entityClass = null;
    private ResourceLocation locationTextureCustom = null;

    protected Render(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public void setRenderOutlines(boolean renderOutlinesIn) {
        this.renderOutlines = renderOutlinesIn;
    }

    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        AxisAlignedBB axisalignedbb = livingEntity.getRenderBoundingBox().grow(0.5);
        if (axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0) {
            axisalignedbb = new AxisAlignedBB(((Entity)livingEntity).posX - 2.0, ((Entity)livingEntity).posY - 2.0, ((Entity)livingEntity).posZ - 2.0, ((Entity)livingEntity).posX + 2.0, ((Entity)livingEntity).posY + 2.0, ((Entity)livingEntity).posZ + 2.0);
        }
        return livingEntity.isInRangeToRender3d(camX, camY, camZ) && (((Entity)livingEntity).ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            this.renderName(entity, x, y, z);
        }
    }

    protected int getTeamColor(T entityIn) {
        String s;
        int i = 0xFFFFFF;
        ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityIn.getTeam();
        if (scoreplayerteam != null && (s = FontRenderer.getFormatFromString((String)scoreplayerteam.getPrefix())).length() >= 2) {
            i = this.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
        }
        return i;
    }

    protected void renderName(T entity, double x, double y, double z) {
        if (this.canRenderName(entity)) {
            this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
        }
    }

    protected boolean canRenderName(T entity) {
        return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
    }

    protected void renderEntityName(T entityIn, double x, double y, double z, String name, double distanceSq) {
        this.renderLivingLabel(entityIn, name, x, y, z, 64);
    }

    @Nullable
    protected abstract ResourceLocation getEntityTexture(T var1);

    protected boolean bindEntityTexture(T entity) {
        ResourceLocation resourcelocation = this.getEntityTexture(entity);
        if (this.locationTextureCustom != null) {
            resourcelocation = this.locationTextureCustom;
        }
        if (resourcelocation == null) {
            return false;
        }
        this.bindTexture(resourcelocation);
        return true;
    }

    public void bindTexture(ResourceLocation location) {
        this.renderManager.renderEngine.bindTexture(location);
    }

    private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks) {
        GlStateManager.disableLighting();
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
        float f = entity.width * 1.4f;
        GlStateManager.scale((float)f, (float)f, (float)f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f1 = 0.5f;
        float f2 = 0.0f;
        float f3 = entity.height / f;
        float f4 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate((float)(-this.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)(-0.3f + (float)((int)f3) * 0.02f));
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f5 = 0.0f;
        int i = 0;
        boolean multitexture = Config.isMultiTexture();
        if (multitexture) {
            bufferbuilder.setBlockLayer(BlockRenderLayer.SOLID);
        }
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        while (f3 > 0.0f) {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            bufferbuilder.setSprite(textureatlassprite2);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f6 = textureatlassprite2.getMinU();
            float f7 = textureatlassprite2.getMinV();
            float f8 = textureatlassprite2.getMaxU();
            float f9 = textureatlassprite2.getMaxV();
            if (i / 2 % 2 == 0) {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }
            bufferbuilder.pos((double)(f1 - 0.0f), (double)(0.0f - f4), (double)f5).tex((double)f8, (double)f9).endVertex();
            bufferbuilder.pos((double)(-f1 - 0.0f), (double)(0.0f - f4), (double)f5).tex((double)f6, (double)f9).endVertex();
            bufferbuilder.pos((double)(-f1 - 0.0f), (double)(1.4f - f4), (double)f5).tex((double)f6, (double)f7).endVertex();
            bufferbuilder.pos((double)(f1 - 0.0f), (double)(1.4f - f4), (double)f5).tex((double)f8, (double)f7).endVertex();
            f3 -= 0.45f;
            f4 -= 0.45f;
            f1 *= 0.9f;
            f5 += 0.03f;
            ++i;
        }
        tessellator.draw();
        if (multitexture) {
            bufferbuilder.setBlockLayer(null);
            GlStateManager.bindCurrentTexture();
        }
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    private void renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks) {
        if (Config.isShaders() && Shaders.shouldSkipDefaultShadow) {
            return;
        }
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.renderManager.renderEngine.bindTexture(SHADOW_TEXTURES);
        World world = this.getWorldFromRenderManager();
        GlStateManager.depthMask((boolean)false);
        float f = this.shadowSize;
        if (entityIn instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)entityIn;
            f *= entityliving.getRenderSizeModifier();
            if (entityliving.isChild()) {
                f *= 0.5f;
            }
        }
        double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d0 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d1 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        int i = MathHelper.floor((double)(d5 - (double)f));
        int j = MathHelper.floor((double)(d5 + (double)f));
        int k = MathHelper.floor((double)(d0 - (double)f));
        int l = MathHelper.floor((double)d0);
        int i1 = MathHelper.floor((double)(d1 - (double)f));
        int j1 = MathHelper.floor((double)(d1 + (double)f));
        double d2 = x - d5;
        double d3 = y - d0;
        double d4 = z - d1;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (BlockPos blockpos : BlockPos.getAllInBoxMutable((BlockPos)new BlockPos(i, k, i1), (BlockPos)new BlockPos(j, l, j1))) {
            IBlockState iblockstate = world.getBlockState(blockpos.down());
            if (iblockstate.i() == EnumBlockRenderType.INVISIBLE || world.getLightFromNeighbors(blockpos) <= 3) continue;
            this.renderShadowSingle(iblockstate, x, y, z, blockpos, shadowAlpha, f, d2, d3, d4);
        }
        tessellator.draw();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask((boolean)true);
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.world;
    }

    private void renderShadowSingle(IBlockState state, double p_188299_2_, double p_188299_4_, double p_188299_6_, BlockPos p_188299_8_, float p_188299_9_, float p_188299_10_, double p_188299_11_, double p_188299_13_, double p_188299_15_) {
        if (state.g()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            double d0 = ((double)p_188299_9_ - (p_188299_4_ - ((double)p_188299_8_.q() + p_188299_13_)) / 2.0) * 0.5 * (double)this.getWorldFromRenderManager().getLightBrightness(p_188299_8_);
            if (d0 >= 0.0) {
                if (d0 > 1.0) {
                    d0 = 1.0;
                }
                AxisAlignedBB axisalignedbb = state.e((IBlockAccess)this.getWorldFromRenderManager(), p_188299_8_);
                double d1 = (double)p_188299_8_.p() + axisalignedbb.minX + p_188299_11_;
                double d2 = (double)p_188299_8_.p() + axisalignedbb.maxX + p_188299_11_;
                double d3 = (double)p_188299_8_.q() + axisalignedbb.minY + p_188299_13_ + 0.015625;
                double d4 = (double)p_188299_8_.r() + axisalignedbb.minZ + p_188299_15_;
                double d5 = (double)p_188299_8_.r() + axisalignedbb.maxZ + p_188299_15_;
                float f = (float)((p_188299_2_ - d1) / 2.0 / (double)p_188299_10_ + 0.5);
                float f1 = (float)((p_188299_2_ - d2) / 2.0 / (double)p_188299_10_ + 0.5);
                float f2 = (float)((p_188299_6_ - d4) / 2.0 / (double)p_188299_10_ + 0.5);
                float f3 = (float)((p_188299_6_ - d5) / 2.0 / (double)p_188299_10_ + 0.5);
                bufferbuilder.pos(d1, d3, d4).tex((double)f, (double)f2).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                bufferbuilder.pos(d1, d3, d5).tex((double)f, (double)f3).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                bufferbuilder.pos(d2, d3, d5).tex((double)f1, (double)f3).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
                bufferbuilder.pos(d2, d3, d4).tex((double)f1, (double)f2).color(1.0f, 1.0f, 1.0f, (float)d0).endVertex();
            }
        }
    }

    public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z) {
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        bufferbuilder.setTranslation(x, y, z);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_NORMAL);
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0f, 0.0f, -1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 0.0f, 1.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0f, -1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        bufferbuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0f, 0.0f, 0.0f).endVertex();
        tessellator.draw();
        bufferbuilder.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableTexture2D();
    }

    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        if (this.renderManager.options != null) {
            double d0;
            float f;
            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0f && !entityIn.isInvisible() && this.renderManager.isRenderShadow() && (f = (float)((1.0 - (d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ)) / 256.0) * (double)this.shadowOpaque)) > 0.0f) {
                this.renderShadow(entityIn, x, y, z, f, partialTicks);
            }
            if (!(!entityIn.canRenderOnFire() || entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).isSpectator())) {
                this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
            }
        }
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }

    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        double d0 = entityIn.getDistanceSq(this.renderManager.renderViewEntity);
        if (d0 <= (double)(maxDistance * maxDistance)) {
            boolean flag = entityIn.isSneaking();
            float f = this.renderManager.playerViewY;
            float f1 = this.renderManager.playerViewX;
            boolean flag1 = this.renderManager.options.thirdPersonView == 2;
            float f2 = ((Entity)entityIn).height + 0.5f - (flag ? 0.25f : 0.0f);
            int i = "deadmau5".equals((Object)str) ? -10 : 0;
            EntityRenderer.drawNameplate((FontRenderer)this.getFontRendererFromRenderManager(), (String)str, (float)((float)x), (float)((float)y + f2), (float)((float)z), (int)i, (float)f, (float)f1, (boolean)flag1, (boolean)flag);
        }
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    public boolean isMultipass() {
        return false;
    }

    public void renderMultipass(T p_188300_1_, double p_188300_2_, double p_188300_4_, double p_188300_6_, float p_188300_8_, float p_188300_9_) {
    }

    public Class getEntityClass() {
        return this.entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public ResourceLocation getLocationTextureCustom() {
        return this.locationTextureCustom;
    }

    public void setLocationTextureCustom(ResourceLocation locationTextureCustom) {
        this.locationTextureCustom = locationTextureCustom;
    }
}
