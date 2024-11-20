/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Lists
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.nio.FloatBuffer
 *  java.util.List
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelSpider
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$Profile
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderLivingBase$1
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.scoreboard.Team$EnumVisible
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.TextFormatting
 *  net.optifine.EmissiveTextures
 *  net.optifine.entity.model.CustomEntityModels
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorConstructor
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.shaders.Shaders
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.optifine.EmissiveTextures;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorConstructor;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.Shaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);
    public ModelBase mainModel;
    protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer((int)4);
    protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
    protected boolean renderMarker;
    public static float NAME_TAG_RANGE = 64.0f;
    public static float NAME_TAG_RANGE_SNEAK = 32.0f;
    public EntityLivingBase renderEntity;
    public float renderLimbSwing;
    public float renderLimbSwingAmount;
    public float renderAgeInTicks;
    public float renderHeadYaw;
    public float renderHeadPitch;
    public float renderScaleFactor;
    public float renderPartialTicks;
    private boolean renderModelPushMatrix;
    private boolean renderLayersPushMatrix;
    public static final boolean animateModelLiving = Boolean.getBoolean((String)"animate.model.living");

    public RenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
        this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
        this.renderModelPushMatrix = this.mainModel instanceof ModelSpider;
    }

    public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
        return this.layerRenderers.add(layer);
    }

    public ModelBase getMainModel() {
        return this.mainModel;
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {
        }
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }

    public void transformHeldFull3DItemLayer() {
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (Reflector.RenderLivingEvent_Pre_Constructor.exists() && Reflector.postForgeBusEvent((ReflectorConstructor)Reflector.RenderLivingEvent_Pre_Constructor, (Object[])new Object[]{entity, this, Float.valueOf((float)partialTicks), x, y, z})) {
            return;
        }
        if (animateModelLiving) {
            ((EntityLivingBase)entity).limbSwingAmount = 1.0f;
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        this.mainModel.isRiding = entity.aS();
        if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
            this.mainModel.isRiding = entity.aS() && entity.bJ() != null && Reflector.callBoolean((Object)entity.bJ(), (ReflectorMethod)Reflector.ForgeEntity_shouldRiderSit, (Object[])new Object[0]);
        }
        this.mainModel.isChild = entity.isChild();
        try {
            float f = this.interpolateRotation(((EntityLivingBase)entity).prevRenderYawOffset, ((EntityLivingBase)entity).renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(((EntityLivingBase)entity).prevRotationYawHead, ((EntityLivingBase)entity).rotationYawHead, partialTicks);
            float f2 = f1 - f;
            if (this.mainModel.isRiding && entity.bJ() instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity.bJ();
                f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees((float)f2);
                if (f3 < -85.0f) {
                    f3 = -85.0f;
                }
                if (f3 >= 85.0f) {
                    f3 = 85.0f;
                }
                f = f1 - f3;
                if (f3 * f3 > 2500.0f) {
                    f += f3 * 0.2f;
                }
                f2 = f1 - f;
            }
            float f7 = ((EntityLivingBase)entity).y + (((EntityLivingBase)entity).w - ((EntityLivingBase)entity).y) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            float f8 = this.handleRotationFloat(entity, partialTicks);
            this.applyRotations(entity, f8, f, partialTicks);
            float f4 = this.prepareScale(entity, partialTicks);
            float f5 = 0.0f;
            float f6 = 0.0f;
            if (!entity.aS()) {
                f5 = ((EntityLivingBase)entity).prevLimbSwingAmount + (((EntityLivingBase)entity).limbSwingAmount - ((EntityLivingBase)entity).prevLimbSwingAmount) * partialTicks;
                f6 = ((EntityLivingBase)entity).limbSwing - ((EntityLivingBase)entity).limbSwingAmount * (1.0f - partialTicks);
                if (entity.isChild()) {
                    f6 *= 3.0f;
                }
                if (f5 > 1.0f) {
                    f5 = 1.0f;
                }
            }
            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
            this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);
            if (CustomEntityModels.isActive()) {
                this.renderEntity = entity;
                this.renderLimbSwing = f6;
                this.renderLimbSwingAmount = f5;
                this.renderAgeInTicks = f8;
                this.renderHeadYaw = f2;
                this.renderHeadPitch = f7;
                this.renderScaleFactor = f4;
                this.renderPartialTicks = partialTicks;
            }
            if (this.renderOutlines) {
                boolean flag1 = this.setScoreTeamColor(entity);
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode((int)this.getTeamColor((Entity)entity));
                if (!this.renderMarker) {
                    this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                }
                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                }
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
                if (flag1) {
                    this.unsetScoreTeamColor();
                }
            } else {
                boolean flag = this.setDoRenderBrightness(entity, partialTicks);
                if (EmissiveTextures.isActive()) {
                    EmissiveTextures.beginRender();
                }
                if (this.renderModelPushMatrix) {
                    GlStateManager.pushMatrix();
                }
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                if (this.renderModelPushMatrix) {
                    GlStateManager.popMatrix();
                }
                if (EmissiveTextures.isActive()) {
                    if (EmissiveTextures.hasEmissive()) {
                        this.renderModelPushMatrix = true;
                        EmissiveTextures.beginRenderEmissive();
                        GlStateManager.pushMatrix();
                        this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                        GlStateManager.popMatrix();
                        EmissiveTextures.endRenderEmissive();
                    }
                    EmissiveTextures.endRender();
                }
                if (flag) {
                    this.unsetBrightness();
                }
                GlStateManager.depthMask((boolean)true);
                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                }
            }
            if (CustomEntityModels.isActive()) {
                this.renderEntity = null;
            }
            GlStateManager.disableRescaleNormal();
        }
        catch (Exception exception) {
            LOGGER.error("Couldn't render entity", (Throwable)exception);
        }
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
            Reflector.postForgeBusEvent((ReflectorConstructor)Reflector.RenderLivingEvent_Post_Constructor, (Object[])new Object[]{entity, this, Float.valueOf((float)partialTicks), x, y, z});
        }
    }

    public float prepareScale(T entitylivingbaseIn, float partialTicks) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale((float)-1.0f, (float)-1.0f, (float)1.0f);
        this.preRenderCallback(entitylivingbaseIn, partialTicks);
        float f = 0.0625f;
        GlStateManager.translate((float)0.0f, (float)-1.501f, (float)0.0f);
        return 0.0625f;
    }

    protected boolean setScoreTeamColor(T entityLivingBaseIn) {
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected void unsetScoreTeamColor() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
    }

    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        boolean flag1;
        boolean flag = this.isVisible(entitylivingbaseIn);
        boolean bl = flag1 = !flag && !entitylivingbaseIn.e((EntityPlayer)Minecraft.getMinecraft().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture((Entity)entitylivingbaseIn)) {
                return;
            }
            if (flag1) {
                GlStateManager.enableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (flag1) {
                GlStateManager.disableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    protected boolean isVisible(T p_193115_1_) {
        return !p_193115_1_.aX() || this.renderOutlines;
    }

    protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
        return this.setBrightness(entityLivingBaseIn, partialTicks, true);
    }

    protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
        boolean flag1;
        float f = entitylivingbaseIn.aw();
        int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        boolean flag = (i >> 24 & 0xFF) > 0;
        boolean bl = flag1 = ((EntityLivingBase)entitylivingbaseIn).hurtTime > 0 || ((EntityLivingBase)entitylivingbaseIn).deathTime > 0;
        if (!flag && !flag1) {
            return false;
        }
        if (!flag && !combineTextures) {
            return false;
        }
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)7681);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)OpenGlHelper.GL_INTERPOLATE);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.GL_CONSTANT);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE2_RGB, (int)OpenGlHelper.GL_CONSTANT);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND2_RGB, (int)770);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)7681);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        this.brightnessBuffer.position(0);
        if (flag1) {
            this.brightnessBuffer.put(1.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.3f);
            if (Config.isShaders()) {
                Shaders.setEntityColor((float)1.0f, (float)0.0f, (float)0.0f, (float)0.3f);
            }
        } else {
            float f1 = (float)(i >> 24 & 0xFF) / 255.0f;
            float f2 = (float)(i >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(i >> 8 & 0xFF) / 255.0f;
            float f4 = (float)(i & 0xFF) / 255.0f;
            this.brightnessBuffer.put(f2);
            this.brightnessBuffer.put(f3);
            this.brightnessBuffer.put(f4);
            this.brightnessBuffer.put(1.0f - f1);
            if (Config.isShaders()) {
                Shaders.setEntityColor((float)f2, (float)f3, (float)f4, (float)(1.0f - f1));
            }
        }
        this.brightnessBuffer.flip();
        GlStateManager.glTexEnv((int)8960, (int)8705, (FloatBuffer)this.brightnessBuffer);
        GlStateManager.setActiveTexture((int)OpenGlHelper.GL_TEXTURE2);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture((int)TEXTURE_BRIGHTNESS.getGlTextureId());
        GlStateManager.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)7681);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected void unsetBrightness() {
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_ALPHA, (int)OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_ALPHA, (int)770);
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)5890);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)5890);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.setActiveTexture((int)OpenGlHelper.GL_TEXTURE2);
        GlStateManager.disableTexture2D();
        GlStateManager.bindTexture((int)0);
        GlStateManager.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)5890);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)8448);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GlStateManager.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)5890);
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.setEntityColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        }
    }

    protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
    }

    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        GlStateManager.rotate((float)(180.0f - rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        if (((EntityLivingBase)entityLiving).deathTime > 0) {
            float f = ((float)((EntityLivingBase)entityLiving).deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            if ((f = MathHelper.sqrt((float)f)) > 1.0f) {
                f = 1.0f;
            }
            GlStateManager.rotate((float)(f * this.getDeathMaxRotation(entityLiving)), (float)0.0f, (float)0.0f, (float)1.0f);
        } else {
            String s = TextFormatting.getTextWithoutFormattingCodes((String)entityLiving.h_());
            if (s != null && ("Dinnerbone".equals((Object)s) || "Grumm".equals((Object)s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate((float)0.0f, (float)(((EntityLivingBase)entityLiving).H + 0.1f), (float)0.0f);
                GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
        }
    }

    protected float getSwingProgress(T livingBase, float partialTickTime) {
        return livingBase.getSwingProgress(partialTickTime);
    }

    protected float handleRotationFloat(T livingBase, float partialTicks) {
        return (float)((EntityLivingBase)livingBase).T + partialTicks;
    }

    protected void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        for (LayerRenderer layerrenderer : this.layerRenderers) {
            boolean flag = this.setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.beginRender();
            }
            if (this.renderLayersPushMatrix) {
                GlStateManager.pushMatrix();
            }
            layerrenderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
            if (this.renderLayersPushMatrix) {
                GlStateManager.popMatrix();
            }
            if (EmissiveTextures.isActive()) {
                if (EmissiveTextures.hasEmissive()) {
                    this.renderLayersPushMatrix = true;
                    EmissiveTextures.beginRenderEmissive();
                    GlStateManager.pushMatrix();
                    layerrenderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
                    GlStateManager.popMatrix();
                    EmissiveTextures.endRenderEmissive();
                }
                EmissiveTextures.endRender();
            }
            if (!flag) continue;
            this.unsetBrightness();
        }
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 90.0f;
    }

    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        return 0;
    }

    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
    }

    public void renderName(T entity, double x, double y, double z) {
        if (Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() && Reflector.postForgeBusEvent((ReflectorConstructor)Reflector.RenderLivingEvent_Specials_Pre_Constructor, (Object[])new Object[]{entity, this, x, y, z})) {
            return;
        }
        if (this.canRenderName(entity)) {
            float f;
            double d0 = entity.h(this.renderManager.renderViewEntity);
            float f2 = f = entity.aU() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
            if (d0 < (double)(f * f)) {
                String s = entity.i_().getFormattedText();
                GlStateManager.alphaFunc((int)516, (float)0.1f);
                this.renderEntityName((Entity)entity, x, y, z, s, d0);
            }
        }
        if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
            Reflector.postForgeBusEvent((ReflectorConstructor)Reflector.RenderLivingEvent_Specials_Post_Constructor, (Object[])new Object[]{entity, this, x, y, z});
        }
    }

    protected boolean canRenderName(T entity) {
        boolean flag;
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
        boolean bl = flag = !entity.e((EntityPlayer)entityplayersp);
        if (entity != entityplayersp) {
            Team team = entity.aY();
            Team team1 = entityplayersp.aY();
            if (team != null) {
                Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
                switch (1.$SwitchMap$net$minecraft$scoreboard$Team$EnumVisible[team$enumvisible.ordinal()]) {
                    case 1: {
                        return flag;
                    }
                    case 2: {
                        return false;
                    }
                    case 3: {
                        return team1 == null ? flag : team.isSameTeam(team1) && (team.getSeeFriendlyInvisiblesEnabled() || flag);
                    }
                    case 4: {
                        return team1 == null ? flag : !team.isSameTeam(team1) && flag;
                    }
                }
                return true;
            }
        }
        return Minecraft.isGuiEnabled() && entity != this.renderManager.renderViewEntity && flag && !entity.aT();
    }

    public List<LayerRenderer<T>> getLayerRenderers() {
        return this.layerRenderers;
    }

    static {
        int[] aint = TEXTURE_BRIGHTNESS.getTextureData();
        for (int i = 0; i < 256; ++i) {
            aint[i] = -1;
        }
        TEXTURE_BRIGHTNESS.updateDynamicTexture();
    }
}
