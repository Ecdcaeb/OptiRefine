/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.gson.JsonSyntaxException
 *  java.awt.Graphics2D
 *  java.awt.Image
 *  java.awt.image.BufferedImage
 *  java.awt.image.ImageObserver
 *  java.awt.image.RenderedImage
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Float
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.nio.FloatBuffer
 *  java.util.Calendar
 *  java.util.Date
 *  java.util.List
 *  java.util.Random
 *  javax.annotation.Nullable
 *  javax.imageio.ImageIO
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockBed
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.MapItemRenderer
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.gui.advancements.GuiScreenAdvancements
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$FogMode
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.client.renderer.culling.ClippingHelper
 *  net.minecraft.client.renderer.culling.ClippingHelperImpl
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.client.shader.ShaderLinkHelper
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.monster.EntityCreeper
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.monster.EntitySpider
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.server.integrated.IntegratedServer
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.EntitySelectors
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.MouseFilter
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.ScreenShotHelper
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.Style
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.event.ClickEvent
 *  net.minecraft.util.text.event.ClickEvent$Action
 *  net.minecraft.world.GameType
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldProvider
 *  net.minecraft.world.biome.Biome
 *  net.optifine.CustomColors
 *  net.optifine.GlErrors
 *  net.optifine.Lagometer
 *  net.optifine.RandomEntities
 *  net.optifine.gui.GuiChatOF
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorConstructor
 *  net.optifine.reflect.ReflectorField
 *  net.optifine.reflect.ReflectorForge
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.reflect.ReflectorResolver
 *  net.optifine.shaders.Shaders
 *  net.optifine.shaders.ShadersRender
 *  net.optifine.util.MemoryMonitor
 *  net.optifine.util.TextureUtils
 *  net.optifine.util.TimedEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.util.glu.Project
 */
package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.optifine.CustomColors;
import net.optifine.GlErrors;
import net.optifine.Lagometer;
import net.optifine.RandomEntities;
import net.optifine.gui.GuiChatOF;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorConstructor;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.reflect.ReflectorResolver;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.util.MemoryMonitor;
import net.optifine.util.TextureUtils;
import net.optifine.util.TimedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class EntityRenderer
implements IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    private final Minecraft mc;
    private final IResourceManager resourceManager;
    private final Random random = new Random();
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private final MapItemRenderer mapItemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private final float thirdPersonDistance = 4.0f;
    private float thirdPersonDistancePrev = 4.0f;
    private float smoothCamYaw;
    private float smoothCamPitch;
    private float smoothCamFilterX;
    private float smoothCamFilterY;
    private float smoothCamPartialTicks;
    private float fovModifierHand;
    private float fovModifierHandPrev;
    private float bossColorModifier;
    private float bossColorModifierPrev;
    private boolean cloudFog;
    private boolean renderHand = true;
    private boolean drawBlockOutline = true;
    private long timeWorldIcon;
    private long prevFrameTime = Minecraft.getSystemTime();
    private long renderEndNanoTime;
    private final DynamicTexture lightmapTexture;
    private final int[] lightmapColors;
    private final ResourceLocation locationLightMap;
    private boolean lightmapUpdateNeeded;
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];
    private final FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer((int)16);
    public float fogColorRed;
    public float fogColorGreen;
    public float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection;
    private boolean debugView;
    private double cameraZoom = 1.0;
    private double cameraYaw;
    private double cameraPitch;
    private ItemStack itemActivationItem;
    private int itemActivationTicks;
    private float itemActivationOffX;
    private float itemActivationOffY;
    private ShaderGroup shaderGroup;
    private static final ResourceLocation[] SHADERS_TEXTURES;
    public static final int SHADER_COUNT;
    private int shaderIndex;
    private boolean useShader;
    public int frameCount;
    private boolean initialized = false;
    private World updatedWorld = null;
    public boolean fogStandard = false;
    private float clipDistance = 128.0f;
    private long lastServerTime = 0L;
    private int lastServerTicks = 0;
    private int serverWaitTime = 0;
    private int serverWaitTimeCurrent = 0;
    private float avgServerTimeDiff = 0.0f;
    private float avgServerTickDiff = 0.0f;
    private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
    private boolean loadVisibleChunks = false;

    public EntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
        this.shaderIndex = SHADER_COUNT;
        this.mc = mcIn;
        this.resourceManager = resourceManagerIn;
        this.itemRenderer = mcIn.getItemRenderer();
        this.mapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.shaderGroup = null;
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = j - 16;
                float f1 = i - 16;
                float f2 = MathHelper.sqrt((float)(f * f + f1 * f1));
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && this.shaderGroup != null;
    }

    public void stopUseShader() {
        if (this.shaderGroup != null) {
            this.shaderGroup.deleteShaderGroup();
        }
        this.shaderGroup = null;
        this.shaderIndex = SHADER_COUNT;
    }

    public void switchUseShader() {
        this.useShader = !this.useShader;
    }

    public void loadEntityShader(@Nullable Entity entityIn) {
        if (OpenGlHelper.shadersSupported) {
            if (this.shaderGroup != null) {
                this.shaderGroup.deleteShaderGroup();
            }
            this.shaderGroup = null;
            if (entityIn instanceof EntityCreeper) {
                this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
            } else if (entityIn instanceof EntitySpider) {
                this.loadShader(new ResourceLocation("shaders/post/spider.json"));
            } else if (entityIn instanceof EntityEnderman) {
                this.loadShader(new ResourceLocation("shaders/post/invert.json"));
            } else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
                Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_loadEntityShader, (Object[])new Object[]{entityIn, this});
            }
        }
    }

    private void loadShader(ResourceLocation resourceLocationIn) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }
        try {
            this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
            this.shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
        }
        catch (IOException ioexception) {
            LOGGER.warn("Failed to load shader: {}", (Object)resourceLocationIn, (Object)ioexception);
            this.shaderIndex = SHADER_COUNT;
            this.useShader = false;
        }
        catch (JsonSyntaxException jsonsyntaxexception) {
            LOGGER.warn("Failed to load shader: {}", (Object)resourceLocationIn, (Object)jsonsyntaxexception);
            this.shaderIndex = SHADER_COUNT;
            this.useShader = false;
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        if (this.shaderGroup != null) {
            this.shaderGroup.deleteShaderGroup();
        }
        this.shaderGroup = null;
        if (this.shaderIndex == SHADER_COUNT) {
            this.loadEntityShader(this.mc.getRenderViewEntity());
        } else {
            this.loadShader(SHADERS_TEXTURES[this.shaderIndex]);
        }
    }

    public void updateRenderer() {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistancePrev = 4.0f;
        if (this.mc.gameSettings.smoothCamera) {
            float f = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f * f * f * 8.0f;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05f * f1);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05f * f1);
            this.smoothCamPartialTicks = 0.0f;
            this.smoothCamYaw = 0.0f;
            this.smoothCamPitch = 0.0f;
        } else {
            this.smoothCamFilterX = 0.0f;
            this.smoothCamFilterY = 0.0f;
            this.mouseFilterXAxis.reset();
            this.mouseFilterYAxis.reset();
        }
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity((Entity)this.mc.player);
        }
        Entity viewEntity = this.mc.getRenderViewEntity();
        double vx = viewEntity.posX;
        double vy = viewEntity.posY + (double)viewEntity.getEyeHeight();
        double vz = viewEntity.posZ;
        float f3 = this.mc.world.getLightBrightness(new BlockPos(vx, vy, vz));
        float f4 = (float)this.mc.gameSettings.renderDistanceChunks / 16.0f;
        f4 = MathHelper.clamp((float)f4, (float)0.0f, (float)1.0f);
        float f2 = f3 * (1.0f - f4) + f4;
        this.fogColor1 += (f2 - this.fogColor1) * 0.1f;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;
        if (this.mc.ingameGUI.getBossOverlay().shouldDarkenSky()) {
            this.bossColorModifier += 0.05f;
            if (this.bossColorModifier > 1.0f) {
                this.bossColorModifier = 1.0f;
            }
        } else if (this.bossColorModifier > 0.0f) {
            this.bossColorModifier -= 0.0125f;
        }
        if (this.itemActivationTicks > 0) {
            --this.itemActivationTicks;
            if (this.itemActivationTicks == 0) {
                this.itemActivationItem = null;
            }
        }
    }

    public ShaderGroup getShaderGroup() {
        return this.shaderGroup;
    }

    public void updateShaderGroupSize(int width, int height) {
        if (OpenGlHelper.shadersSupported) {
            if (this.shaderGroup != null) {
                this.shaderGroup.createBindFramebuffers(width, height);
            }
            this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
        }
    }

    public void getMouseOver(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity != null && this.mc.world != null) {
            this.mc.profiler.startSection("pick");
            this.mc.pointedEntity = null;
            double d0 = this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
            Vec3d vec3d = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            int i = 3;
            double d1 = d0;
            if (this.mc.playerController.extendedReach()) {
                d0 = d1 = 6.0;
            } else if (d0 > 3.0) {
                flag = true;
            }
            if (this.mc.objectMouseOver != null) {
                d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
            }
            Vec3d vec3d1 = entity.getLook(1.0f);
            Vec3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
            this.pointedEntity = null;
            Vec3d vec3d3 = null;
            float f = 1.0f;
            List list = this.mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0, 1.0, 1.0), Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, (Predicate)new /* Unavailable Anonymous Inner Class!! */));
            double d2 = d1;
            for (int j = 0; j < list.size(); ++j) {
                double d3;
                Entity entity1 = (Entity)list.get(j);
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)entity1.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                if (axisalignedbb.contains(vec3d)) {
                    if (!(d2 >= 0.0)) continue;
                    this.pointedEntity = entity1;
                    vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (raytraceresult == null || !((d3 = vec3d.distanceTo(raytraceresult.hitVec)) < d2) && d2 != 0.0) continue;
                boolean canRiderInteract = false;
                if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                    canRiderInteract = Reflector.callBoolean((Object)entity1, (ReflectorMethod)Reflector.ForgeEntity_canRiderInteract, (Object[])new Object[0]);
                }
                if (!canRiderInteract && entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                    if (d2 != 0.0) continue;
                    this.pointedEntity = entity1;
                    vec3d3 = raytraceresult.hitVec;
                    continue;
                }
                this.pointedEntity = entity1;
                vec3d3 = raytraceresult.hitVec;
                d2 = d3;
            }
            if (this.pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > 3.0) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing)null, new BlockPos(vec3d3));
            }
            if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, vec3d3);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.profiler.endSection();
        }
    }

    private void updateFovModifierHand() {
        float f = 1.0f;
        if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
            f = abstractclientplayer.getFovModifier();
        }
        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (f - this.fovModifierHand) * 0.5f;
        if (this.fovModifierHand > 1.5f) {
            this.fovModifierHand = 1.5f;
        }
        if (this.fovModifierHand < 0.1f) {
            this.fovModifierHand = 0.1f;
        }
    }

    private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
        IBlockState iblockstate;
        if (this.debugView) {
            return 90.0f;
        }
        Entity entity = this.mc.getRenderViewEntity();
        float f = 70.0f;
        if (useFOVSetting) {
            f = this.mc.gameSettings.fovSetting;
            if (Config.isDynamicFov()) {
                f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
            }
        }
        boolean zoomActive = false;
        if (this.mc.currentScreen == null) {
            GameSettings cfr_ignored_0 = this.mc.gameSettings;
            zoomActive = GameSettings.isKeyDown((KeyBinding)this.mc.gameSettings.ofKeyBindZoom);
        }
        if (zoomActive) {
            if (!Config.zoomMode) {
                Config.zoomMode = true;
                Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
                this.mc.gameSettings.smoothCamera = true;
                this.mc.renderGlobal.displayListEntitiesDirty = true;
            }
            if (Config.zoomMode) {
                f /= 4.0f;
            }
        } else if (Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = Config.zoomSmoothCamera;
            this.mouseFilterXAxis = new MouseFilter();
            this.mouseFilterYAxis = new MouseFilter();
            this.mc.renderGlobal.displayListEntitiesDirty = true;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            float f1 = (float)((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0f - 500.0f / (f1 + 500.0f)) * 2.0f + 1.0f;
        }
        if ((iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint((World)this.mc.world, (Entity)entity, (float)partialTicks)).a() == Material.WATER) {
            f = f * 60.0f / 70.0f;
        }
        if (Reflector.ForgeHooksClient_getFOVModifier.exists()) {
            return Reflector.callFloat((ReflectorMethod)Reflector.ForgeHooksClient_getFOVModifier, (Object[])new Object[]{this, entity, iblockstate, Float.valueOf((float)partialTicks), Float.valueOf((float)f)});
        }
        return f;
    }

    private void hurtCameraEffect(float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
            float f = (float)entitylivingbase.hurtTime - partialTicks;
            if (entitylivingbase.getHealth() <= 0.0f) {
                float f1 = (float)entitylivingbase.deathTime + partialTicks;
                GlStateManager.rotate((float)(40.0f - 8000.0f / (f1 + 200.0f)), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            if (f < 0.0f) {
                return;
            }
            f /= (float)entitylivingbase.maxHurtTime;
            f = MathHelper.sin((float)(f * f * f * f * (float)Math.PI));
            float f2 = entitylivingbase.attackedAtYaw;
            GlStateManager.rotate((float)(-f2), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(-f * 14.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.rotate((float)f2, (float)0.0f, (float)1.0f, (float)0.0f);
        }
    }

    private void applyBobbing(float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            float f = entityplayer.J - entityplayer.I;
            float f1 = -(entityplayer.J + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.aJ + (entityplayer.aK - entityplayer.aJ) * partialTicks;
            GlStateManager.translate((float)(MathHelper.sin((float)(f1 * (float)Math.PI)) * f2 * 0.5f), (float)(-Math.abs((float)(MathHelper.cos((float)(f1 * (float)Math.PI)) * f2))), (float)0.0f);
            GlStateManager.rotate((float)(MathHelper.sin((float)(f1 * (float)Math.PI)) * f2 * 3.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.rotate((float)(Math.abs((float)(MathHelper.cos((float)(f1 * (float)Math.PI - 0.2f)) * f2)) * 5.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)f3, (float)1.0f, (float)0.0f, (float)0.0f);
        }
    }

    private void orientCamera(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
            f = (float)((double)f + 1.0);
            GlStateManager.translate((float)0.0f, (float)0.3f, (float)0.0f);
            if (!this.mc.gameSettings.debugCamEnable) {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
                    Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_orientBedCamera, (Object[])new Object[]{this.mc.world, blockpos, iblockstate, entity});
                } else if (block == Blocks.BED) {
                    int j = ((EnumFacing)iblockstate.getValue((IProperty)BlockBed.D)).getHorizontalIndex();
                    GlStateManager.rotate((float)(j * 90), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                GlStateManager.rotate((float)(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
                GlStateManager.rotate((float)(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks), (float)-1.0f, (float)0.0f, (float)0.0f);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            double d3 = this.thirdPersonDistancePrev + (4.0f - this.thirdPersonDistancePrev) * partialTicks;
            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate((float)0.0f, (float)0.0f, (float)((float)(-d3)));
            } else {
                float f1 = entity.rotationYaw;
                float f2 = entity.rotationPitch;
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f2 += 180.0f;
                }
                double d4 = (double)(-MathHelper.sin((float)(f1 * ((float)Math.PI / 180))) * MathHelper.cos((float)(f2 * ((float)Math.PI / 180)))) * d3;
                double d5 = (double)(MathHelper.cos((float)(f1 * ((float)Math.PI / 180))) * MathHelper.cos((float)(f2 * ((float)Math.PI / 180)))) * d3;
                double d6 = (double)(-MathHelper.sin((float)(f2 * ((float)Math.PI / 180)))) * d3;
                for (int i = 0; i < 8; ++i) {
                    double d7;
                    RayTraceResult raytraceresult;
                    float f3 = (i & 1) * 2 - 1;
                    float f4 = (i >> 1 & 1) * 2 - 1;
                    float f5 = (i >> 2 & 1) * 2 - 1;
                    if ((raytraceresult = this.mc.world.rayTraceBlocks(new Vec3d(d0 + (double)(f3 *= 0.1f), d1 + (double)(f4 *= 0.1f), d2 + (double)(f5 *= 0.1f)), new Vec3d(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5))) == null || !((d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2))) < d3)) continue;
                    d3 = d7;
                }
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                GlStateManager.rotate((float)(entity.rotationPitch - f2), (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)(entity.rotationYaw - f1), (float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.translate((float)0.0f, (float)0.0f, (float)((float)(-d3)));
                GlStateManager.rotate((float)(f1 - entity.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.rotate((float)(f2 - entity.rotationPitch), (float)1.0f, (float)0.0f, (float)0.0f);
            }
        } else {
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.05f);
        }
        if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
            if (!this.mc.gameSettings.debugCamEnable) {
                float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
                float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float roll = 0.0f;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityanimal = (EntityAnimal)entity;
                    yaw = entityanimal.aQ + (entityanimal.aP - entityanimal.aQ) * partialTicks + 180.0f;
                }
                IBlockState state = ActiveRenderInfo.getBlockStateAtEntityViewpoint((World)this.mc.world, (Entity)entity, (float)partialTicks);
                Object event = Reflector.newInstance((ReflectorConstructor)Reflector.EntityViewRenderEvent_CameraSetup_Constructor, (Object[])new Object[]{this, entity, state, Float.valueOf((float)partialTicks), Float.valueOf((float)yaw), Float.valueOf((float)pitch), Float.valueOf((float)roll)});
                Reflector.postForgeBusEvent((Object)event);
                roll = Reflector.callFloat((Object)event, (ReflectorMethod)Reflector.EntityViewRenderEvent_CameraSetup_getRoll, (Object[])new Object[0]);
                pitch = Reflector.callFloat((Object)event, (ReflectorMethod)Reflector.EntityViewRenderEvent_CameraSetup_getPitch, (Object[])new Object[0]);
                yaw = Reflector.callFloat((Object)event, (ReflectorMethod)Reflector.EntityViewRenderEvent_CameraSetup_getYaw, (Object[])new Object[0]);
                GlStateManager.rotate((float)roll, (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.rotate((float)pitch, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)yaw, (float)0.0f, (float)1.0f, (float)0.0f);
            }
        } else if (!this.mc.gameSettings.debugCamEnable) {
            GlStateManager.rotate((float)(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks), (float)1.0f, (float)0.0f, (float)0.0f);
            if (entity instanceof EntityAnimal) {
                EntityAnimal entityanimal = (EntityAnimal)entity;
                GlStateManager.rotate((float)(entityanimal.aQ + (entityanimal.aP - entityanimal.aQ) * partialTicks + 180.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            } else {
                GlStateManager.rotate((float)(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
        }
        GlStateManager.translate((float)0.0f, (float)(-f), (float)0.0f);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }

    public void setupCameraTransform(float partialTicks, int pass) {
        float f1;
        this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
        if (Config.isFogFancy()) {
            this.farPlaneDistance *= 0.95f;
        }
        if (Config.isFogFast()) {
            this.farPlaneDistance *= 0.83f;
        }
        GlStateManager.matrixMode((int)5889);
        GlStateManager.loadIdentity();
        float f = 0.07f;
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)((float)(-(pass * 2 - 1)) * 0.07f), (float)0.0f, (float)0.0f);
        }
        this.clipDistance = this.farPlaneDistance * 2.0f;
        if (this.clipDistance < 173.0f) {
            this.clipDistance = 173.0f;
        }
        if (this.cameraZoom != 1.0) {
            GlStateManager.translate((float)((float)this.cameraYaw), (float)((float)(-this.cameraPitch)), (float)0.0f);
            GlStateManager.scale((double)this.cameraZoom, (double)this.cameraZoom, (double)1.0);
        }
        Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)this.clipDistance);
        GlStateManager.matrixMode((int)5888);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)((float)(pass * 2 - 1) * 0.1f), (float)0.0f, (float)0.0f);
        }
        this.hurtCameraEffect(partialTicks);
        if (this.mc.gameSettings.viewBobbing) {
            this.applyBobbing(partialTicks);
        }
        if ((f1 = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * partialTicks) > 0.0f) {
            int i = 20;
            if (this.mc.player.a(MobEffects.NAUSEA)) {
                i = 7;
            }
            float f2 = 5.0f / (f1 * f1 + 5.0f) - f1 * 0.04f;
            f2 *= f2;
            GlStateManager.rotate((float)(((float)this.rendererUpdateCount + partialTicks) * (float)i), (float)0.0f, (float)1.0f, (float)1.0f);
            GlStateManager.scale((float)(1.0f / f2), (float)1.0f, (float)1.0f);
            GlStateManager.rotate((float)(-((float)this.rendererUpdateCount + partialTicks) * (float)i), (float)0.0f, (float)1.0f, (float)1.0f);
        }
        this.orientCamera(partialTicks);
        if (this.debugView) {
            switch (this.debugViewDirection) {
                case 0: {
                    GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                    break;
                }
                case 1: {
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                    break;
                }
                case 2: {
                    GlStateManager.rotate((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                    break;
                }
                case 3: {
                    GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                    break;
                }
                case 4: {
                    GlStateManager.rotate((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
            }
        }
    }

    private void renderHand(float partialTicks, int pass) {
        this.renderHand(partialTicks, pass, true, true, false);
    }

    public void renderHand(float partialTicks, int pass, boolean renderItem, boolean renderOverlay, boolean renderTranslucent) {
        if (!this.debugView) {
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            float f = 0.07f;
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float)((float)(-(pass * 2 - 1)) * 0.07f), (float)0.0f, (float)0.0f);
            }
            if (Config.isShaders()) {
                Shaders.applyHandDepth();
            }
            Project.gluPerspective((float)this.getFOVModifier(partialTicks, false), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * 2.0f));
            GlStateManager.matrixMode((int)5888);
            GlStateManager.loadIdentity();
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float)((float)(pass * 2 - 1) * 0.1f), (float)0.0f, (float)0.0f);
            }
            boolean flag = false;
            if (renderItem) {
                boolean shouldRenderHand;
                GlStateManager.pushMatrix();
                this.hurtCameraEffect(partialTicks);
                if (this.mc.gameSettings.viewBobbing) {
                    this.applyBobbing(partialTicks);
                }
                flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                boolean bl = shouldRenderHand = !ReflectorForge.renderFirstPersonHand((RenderGlobal)this.mc.renderGlobal, (float)partialTicks, (int)pass);
                if (shouldRenderHand && this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
                    this.enableLightmap();
                    if (Config.isShaders()) {
                        ShadersRender.renderItemFP((ItemRenderer)this.itemRenderer, (float)partialTicks, (boolean)renderTranslucent);
                    } else {
                        this.itemRenderer.renderItemInFirstPerson(partialTicks);
                    }
                    this.disableLightmap();
                }
                GlStateManager.popMatrix();
            }
            if (!renderOverlay) {
                return;
            }
            this.disableLightmap();
            if (this.mc.gameSettings.thirdPersonView == 0 && !flag) {
                this.itemRenderer.renderOverlays(partialTicks);
                this.hurtCameraEffect(partialTicks);
            }
            if (this.mc.gameSettings.viewBobbing) {
                this.applyBobbing(partialTicks);
            }
        }
    }

    public void disableLightmap() {
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.disableLightmap();
        }
    }

    public void enableLightmap() {
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode((int)5890);
        GlStateManager.loadIdentity();
        float f = 0.00390625f;
        GlStateManager.scale((float)0.00390625f, (float)0.00390625f, (float)0.00390625f);
        GlStateManager.translate((float)8.0f, (float)8.0f, (float)8.0f);
        GlStateManager.matrixMode((int)5888);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GlStateManager.glTexParameteri((int)3553, (int)10241, (int)9729);
        GlStateManager.glTexParameteri((int)3553, (int)10240, (int)9729);
        GlStateManager.glTexParameteri((int)3553, (int)10242, (int)33071);
        GlStateManager.glTexParameteri((int)3553, (int)10243, (int)33071);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.enableLightmap();
        }
    }

    private void updateTorchFlicker() {
        this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9);
        this.torchFlickerX += this.torchFlickerDX - this.torchFlickerX;
        this.lightmapUpdateNeeded = true;
    }

    private void updateLightmap(float partialTicks) {
        if (this.lightmapUpdateNeeded) {
            this.mc.profiler.startSection("lightTex");
            WorldClient world = this.mc.world;
            if (world != null) {
                if (Config.isCustomColors() && CustomColors.updateLightmap((World)world, (float)this.torchFlickerX, (int[])this.lightmapColors, (boolean)this.mc.player.a(MobEffects.NIGHT_VISION), (float)partialTicks)) {
                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.profiler.endSection();
                    return;
                }
                float f = world.getSunBrightness(1.0f);
                float f1 = f * 0.95f + 0.05f;
                for (int i = 0; i < 256; ++i) {
                    float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
                    float f3 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1f + 1.5f);
                    if (world.getLastLightningBolt() > 0) {
                        f2 = world.provider.getLightBrightnessTable()[i / 16];
                    }
                    float f4 = f2 * (f * 0.65f + 0.35f);
                    float f5 = f2 * (f * 0.65f + 0.35f);
                    float f6 = f3 * ((f3 * 0.6f + 0.4f) * 0.6f + 0.4f);
                    float f7 = f3 * (f3 * f3 * 0.6f + 0.4f);
                    float f8 = f4 + f3;
                    float f9 = f5 + f6;
                    float f10 = f2 + f7;
                    f8 = f8 * 0.96f + 0.03f;
                    f9 = f9 * 0.96f + 0.03f;
                    f10 = f10 * 0.96f + 0.03f;
                    if (this.bossColorModifier > 0.0f) {
                        float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                        f8 = f8 * (1.0f - f11) + f8 * 0.7f * f11;
                        f9 = f9 * (1.0f - f11) + f9 * 0.6f * f11;
                        f10 = f10 * (1.0f - f11) + f10 * 0.6f * f11;
                    }
                    if (world.provider.getDimensionType().getId() == 1) {
                        f8 = 0.22f + f3 * 0.75f;
                        f9 = 0.28f + f6 * 0.75f;
                        f10 = 0.25f + f7 * 0.75f;
                    }
                    if (Reflector.ForgeWorldProvider_getLightmapColors.exists()) {
                        float[] colors = new float[]{f8, f9, f10};
                        Reflector.call((Object)world.provider, (ReflectorMethod)Reflector.ForgeWorldProvider_getLightmapColors, (Object[])new Object[]{Float.valueOf((float)partialTicks), Float.valueOf((float)f), Float.valueOf((float)f2), Float.valueOf((float)f3), colors});
                        f8 = colors[0];
                        f9 = colors[1];
                        f10 = colors[2];
                    }
                    f8 = MathHelper.clamp((float)f8, (float)0.0f, (float)1.0f);
                    f9 = MathHelper.clamp((float)f9, (float)0.0f, (float)1.0f);
                    f10 = MathHelper.clamp((float)f10, (float)0.0f, (float)1.0f);
                    if (this.mc.player.a(MobEffects.NIGHT_VISION)) {
                        float f15 = this.getNightVisionBrightness((EntityLivingBase)this.mc.player, partialTicks);
                        float f12 = 1.0f / f8;
                        if (f12 > 1.0f / f9) {
                            f12 = 1.0f / f9;
                        }
                        if (f12 > 1.0f / f10) {
                            f12 = 1.0f / f10;
                        }
                        f8 = f8 * (1.0f - f15) + f8 * f12 * f15;
                        f9 = f9 * (1.0f - f15) + f9 * f12 * f15;
                        f10 = f10 * (1.0f - f15) + f10 * f12 * f15;
                    }
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                    if (f9 > 1.0f) {
                        f9 = 1.0f;
                    }
                    if (f10 > 1.0f) {
                        f10 = 1.0f;
                    }
                    float f16 = this.mc.gameSettings.gammaSetting;
                    float f17 = 1.0f - f8;
                    float f13 = 1.0f - f9;
                    float f14 = 1.0f - f10;
                    f17 = 1.0f - f17 * f17 * f17 * f17;
                    f13 = 1.0f - f13 * f13 * f13 * f13;
                    f14 = 1.0f - f14 * f14 * f14 * f14;
                    f8 = f8 * (1.0f - f16) + f17 * f16;
                    f9 = f9 * (1.0f - f16) + f13 * f16;
                    f10 = f10 * (1.0f - f16) + f14 * f16;
                    f8 = f8 * 0.96f + 0.03f;
                    f9 = f9 * 0.96f + 0.03f;
                    f10 = f10 * 0.96f + 0.03f;
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                    if (f9 > 1.0f) {
                        f9 = 1.0f;
                    }
                    if (f10 > 1.0f) {
                        f10 = 1.0f;
                    }
                    if (f8 < 0.0f) {
                        f8 = 0.0f;
                    }
                    if (f9 < 0.0f) {
                        f9 = 0.0f;
                    }
                    if (f10 < 0.0f) {
                        f10 = 0.0f;
                    }
                    int j = 255;
                    int k = (int)(f8 * 255.0f);
                    int l = (int)(f9 * 255.0f);
                    int i1 = (int)(f10 * 255.0f);
                    this.lightmapColors[i] = 0xFF000000 | k << 16 | l << 8 | i1;
                }
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.profiler.endSection();
            }
        }
    }

    public float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
        int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
        return i > 200 ? 1.0f : 0.7f + MathHelper.sin((float)(((float)i - partialTicks) * (float)Math.PI * 0.2f)) * 0.3f;
    }

    public void updateCameraAndRender(float partialTicks, long nanoTime) {
        this.frameInit();
        boolean flag = Display.isActive();
        if (!(flag || !this.mc.gameSettings.pauseOnLostFocus || this.mc.gameSettings.touchscreen && Mouse.isButtonDown((int)1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }
        this.mc.profiler.startSection("mouse");
        if (flag && Minecraft.IS_RUNNING_ON_MAC && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed((boolean)false);
            Mouse.setCursorPosition((int)(Display.getWidth() / 2), (int)(Display.getHeight() / 2 - 20));
            Mouse.setGrabbed((boolean)true);
        }
        if (this.mc.inGameHasFocus && flag) {
            this.mc.mouseHelper.mouseXYChange();
            this.mc.getTutorial().handleMouse(this.mc.mouseHelper);
            float f = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f * f * f * 8.0f;
            float f2 = (float)this.mc.mouseHelper.deltaX * f1;
            float f3 = (float)this.mc.mouseHelper.deltaY * f1;
            int i = 1;
            if (this.mc.gameSettings.invertMouse) {
                i = -1;
            }
            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += f2;
                this.smoothCamPitch += f3;
                float f4 = partialTicks - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = partialTicks;
                f2 = this.smoothCamFilterX * f4;
                f3 = this.smoothCamFilterY * f4;
                this.mc.player.c(f2, f3 * (float)i);
            } else {
                this.smoothCamYaw = 0.0f;
                this.smoothCamPitch = 0.0f;
                this.mc.player.c(f2, f3 * (float)i);
            }
        }
        this.mc.profiler.endSection();
        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i1 = scaledresolution.getScaledWidth();
            int j1 = scaledresolution.getScaledHeight();
            int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
            int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
            int i2 = this.mc.gameSettings.limitFramerate;
            if (this.mc.world != null) {
                this.mc.profiler.startSection("level");
                int j = Math.min((int)Minecraft.getDebugFPS(), (int)i2);
                j = Math.max((int)j, (int)60);
                long k = System.nanoTime() - nanoTime;
                long l = Math.max((long)((long)(1000000000 / j / 4) - k), (long)0L);
                this.renderWorld(partialTicks, System.nanoTime() + l);
                if (this.mc.isSingleplayer() && this.timeWorldIcon < Minecraft.getSystemTime() - 1000L) {
                    this.timeWorldIcon = Minecraft.getSystemTime();
                    if (!this.mc.getIntegratedServer().isWorldIconSet()) {
                        this.createWorldIcon();
                    }
                }
                if (OpenGlHelper.shadersSupported) {
                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();
                    if (this.shaderGroup != null && this.useShader) {
                        GlStateManager.matrixMode((int)5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.shaderGroup.render(partialTicks);
                        GlStateManager.popMatrix();
                    }
                    this.mc.getFramebuffer().bindFramebuffer(true);
                }
                this.renderEndNanoTime = System.nanoTime();
                this.mc.profiler.endStartSection("gui");
                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GlStateManager.alphaFunc((int)516, (float)0.1f);
                    this.setupOverlayRendering();
                    this.renderItemActivation(i1, j1, partialTicks);
                    this.mc.ingameGUI.renderGameOverlay(partialTicks);
                    if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                        Config.drawFps();
                    }
                    if (this.mc.gameSettings.showDebugInfo) {
                        Lagometer.showLagometer((ScaledResolution)scaledresolution);
                    }
                }
                this.mc.profiler.endSection();
            } else {
                GlStateManager.viewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
                GlStateManager.matrixMode((int)5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode((int)5888);
                GlStateManager.loadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
                TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
                TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRenderer;
            }
            if (this.mc.currentScreen != null) {
                GlStateManager.clear((int)256);
                try {
                    if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                        Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_drawScreen, (Object[])new Object[]{this.mc.currentScreen, k1, l1, Float.valueOf((float)this.mc.getTickLength())});
                    } else {
                        this.mc.currentScreen.drawScreen(k1, l1, this.mc.getTickLength());
                    }
                }
                catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addDetail("Screen name", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                    crashreportcategory.addDetail("Mouse location", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                    crashreportcategory.addDetail("Screen size", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                    throw new ReportedException(crashreport);
                }
            }
        }
        this.frameFinish();
        this.waitForServerThread();
        MemoryMonitor.update();
        Lagometer.updateLagometer();
        if (this.mc.gameSettings.ofProfiler) {
            this.mc.gameSettings.showDebugProfilerChart = true;
        }
    }

    private void createWorldIcon() {
        if (this.mc.renderGlobal.getRenderedChunks() > 10 && this.mc.renderGlobal.hasNoChunkUpdates() && !this.mc.getIntegratedServer().isWorldIconSet()) {
            BufferedImage bufferedimage = ScreenShotHelper.createScreenshot((int)this.mc.displayWidth, (int)this.mc.displayHeight, (Framebuffer)this.mc.getFramebuffer());
            int i = bufferedimage.getWidth();
            int j = bufferedimage.getHeight();
            int k = 0;
            int l = 0;
            if (i > j) {
                k = (i - j) / 2;
                i = j;
            } else {
                l = (j - i) / 2;
            }
            try {
                BufferedImage bufferedimage1 = new BufferedImage(64, 64, 1);
                Graphics2D graphics = bufferedimage1.createGraphics();
                graphics.drawImage((Image)bufferedimage, 0, 0, 64, 64, k, l, k + i, l + i, (ImageObserver)null);
                graphics.dispose();
                ImageIO.write((RenderedImage)bufferedimage1, (String)"png", (File)this.mc.getIntegratedServer().getWorldIconFile());
            }
            catch (IOException ioexception) {
                LOGGER.warn("Couldn't save auto screenshot", (Throwable)ioexception);
            }
        }
    }

    public void renderStreamIndicator(float partialTicks) {
        this.setupOverlayRendering();
    }

    private boolean isDrawBlockOutline() {
        boolean flag;
        if (!this.drawBlockOutline) {
            return false;
        }
        Entity entity = this.mc.getRenderViewEntity();
        boolean bl = flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
        if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
            ItemStack itemstack = ((EntityPlayer)entity).co();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
                IBlockState state = this.mc.world.getBlockState(blockpos);
                Block block = state.getBlock();
                flag = this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR ? ReflectorForge.blockHasTileEntity((IBlockState)state) && this.mc.world.getTileEntity(blockpos) instanceof IInventory : !itemstack.isEmpty() && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
            }
        }
        return flag;
    }

    public void renderWorld(float partialTicks, long finishTimeNano) {
        this.updateLightmap(partialTicks);
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity((Entity)this.mc.player);
        }
        this.getMouseOver(partialTicks);
        if (Config.isShaders()) {
            Shaders.beginRender((Minecraft)this.mc, (float)partialTicks, (long)finishTimeNano);
        }
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        this.mc.profiler.startSection("center");
        if (this.mc.gameSettings.anaglyph) {
            anaglyphField = 0;
            GlStateManager.colorMask((boolean)false, (boolean)true, (boolean)true, (boolean)false);
            this.renderWorldPass(0, partialTicks, finishTimeNano);
            anaglyphField = 1;
            GlStateManager.colorMask((boolean)true, (boolean)false, (boolean)false, (boolean)false);
            this.renderWorldPass(1, partialTicks, finishTimeNano);
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        } else {
            this.renderWorldPass(2, partialTicks, finishTimeNano);
        }
        this.mc.profiler.endSection();
    }

    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
        boolean isShaders = Config.isShaders();
        if (isShaders) {
            Shaders.beginRenderPass((int)pass, (float)partialTicks, (long)finishTimeNano);
        }
        RenderGlobal renderglobal = this.mc.renderGlobal;
        ParticleManager particlemanager = this.mc.effectRenderer;
        boolean flag = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        this.mc.profiler.endStartSection("clear");
        if (isShaders) {
            Shaders.setViewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
        } else {
            GlStateManager.viewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
        }
        this.updateFogColor(partialTicks);
        GlStateManager.clear((int)16640);
        if (isShaders) {
            Shaders.clearRenderBuffer();
        }
        this.mc.profiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);
        if (isShaders) {
            Shaders.setCamera((float)partialTicks);
        }
        if (Reflector.ActiveRenderInfo_updateRenderInfo2.exists()) {
            Reflector.call((ReflectorMethod)Reflector.ActiveRenderInfo_updateRenderInfo2, (Object[])new Object[]{this.mc.getRenderViewEntity(), this.mc.gameSettings.thirdPersonView == 2});
        } else {
            ActiveRenderInfo.updateRenderInfo((EntityPlayer)this.mc.player, (this.mc.gameSettings.thirdPersonView == 2 ? 1 : 0) != 0);
        }
        this.mc.profiler.endStartSection("frustum");
        ClippingHelper clippingHelper = ClippingHelperImpl.getInstance();
        this.mc.profiler.endStartSection("culling");
        clippingHelper.disabled = Config.isShaders() && !Shaders.isFrustumCulling();
        Frustum icamera = new Frustum(clippingHelper);
        Entity entity = this.mc.getRenderViewEntity();
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        if (isShaders) {
            ShadersRender.setFrustrumPosition((ICamera)icamera, (double)d0, (double)d1, (double)d2);
        } else {
            icamera.setPosition(d0, d1, d2);
        }
        if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
            this.setupFog(-1, partialTicks);
            this.mc.profiler.endStartSection("sky");
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)this.clipDistance);
            GlStateManager.matrixMode((int)5888);
            if (isShaders) {
                Shaders.beginSky();
            }
            renderglobal.renderSky(partialTicks, pass);
            if (isShaders) {
                Shaders.endSky();
            }
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)this.clipDistance);
            GlStateManager.matrixMode((int)5888);
        } else {
            GlStateManager.disableBlend();
        }
        this.setupFog(0, partialTicks);
        GlStateManager.shadeModel((int)7425);
        if (entity.posY + (double)entity.getEyeHeight() < 128.0 + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0f)) {
            this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
        }
        this.mc.profiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        this.mc.profiler.endStartSection("terrain_setup");
        this.checkLoadVisibleChunks(entity, partialTicks, (ICamera)icamera, this.mc.player.y());
        if (isShaders) {
            ShadersRender.setupTerrain((RenderGlobal)renderglobal, (Entity)entity, (double)partialTicks, (ICamera)icamera, (int)this.frameCount++, (boolean)this.mc.player.y());
        } else {
            renderglobal.setupTerrain(entity, (double)partialTicks, (ICamera)icamera, this.frameCount++, this.mc.player.y());
        }
        if (pass == 0 || pass == 2) {
            this.mc.profiler.endStartSection("updatechunks");
            Lagometer.timerChunkUpload.start();
            this.mc.renderGlobal.updateChunks(finishTimeNano);
            Lagometer.timerChunkUpload.end();
        }
        this.mc.profiler.endStartSection("terrain");
        Lagometer.timerTerrain.start();
        if (this.mc.gameSettings.ofSmoothFps && pass > 0) {
            this.mc.profiler.endStartSection("finish");
            GL11.glFinish();
            this.mc.profiler.endStartSection("terrain");
        }
        GlStateManager.matrixMode((int)5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        if (isShaders) {
            ShadersRender.beginTerrainSolid();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.SOLID, (double)partialTicks, pass, entity);
        GlStateManager.enableAlpha();
        if (isShaders) {
            ShadersRender.beginTerrainCutoutMipped();
        }
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0);
        renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, (double)partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        if (isShaders) {
            ShadersRender.beginTerrainCutout();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT, (double)partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        if (isShaders) {
            ShadersRender.endTerrain();
        }
        Lagometer.timerTerrain.end();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        if (!this.debugView) {
            GlStateManager.matrixMode((int)5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            this.mc.profiler.endStartSection("entities");
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_setRenderPass, (Object[])new Object[]{0});
            }
            renderglobal.renderEntities(entity, (ICamera)icamera, partialTicks);
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_setRenderPass, (Object[])new Object[]{-1});
            }
            RenderHelper.disableStandardItemLighting();
            this.disableLightmap();
        }
        GlStateManager.matrixMode((int)5888);
        GlStateManager.popMatrix();
        if (flag && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.WATER)) {
            EntityPlayer entityplayer = (EntityPlayer)entity;
            GlStateManager.disableAlpha();
            this.mc.profiler.endStartSection("outline");
            if (!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean((ReflectorMethod)Reflector.ForgeHooksClient_onDrawBlockHighlight, (Object[])new Object[]{renderglobal, entityplayer, this.mc.objectMouseOver, 0, Float.valueOf((float)partialTicks)})) {
                renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
            }
            GlStateManager.enableAlpha();
        }
        if (this.mc.debugRenderer.shouldRender()) {
            boolean preDebugFog = GlStateManager.isFogEnabled();
            GlStateManager.disableFog();
            this.mc.debugRenderer.renderDebug(partialTicks, finishTimeNano);
            GlStateManager.setFogEnabled((boolean)preDebugFog);
        }
        if (!renderglobal.damagedBlocks.isEmpty()) {
            this.mc.profiler.endStartSection("destroyProgress");
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity, partialTicks);
            this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            GlStateManager.disableBlend();
        }
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.disableBlend();
        if (!this.debugView) {
            this.enableLightmap();
            this.mc.profiler.endStartSection("litParticles");
            if (isShaders) {
                Shaders.beginLitParticles();
            }
            particlemanager.renderLitParticles(entity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.profiler.endStartSection("particles");
            if (isShaders) {
                Shaders.beginParticles();
            }
            particlemanager.renderParticles(entity, partialTicks);
            if (isShaders) {
                Shaders.endParticles();
            }
            this.disableLightmap();
        }
        GlStateManager.depthMask((boolean)false);
        if (Config.isShaders()) {
            GlStateManager.depthMask((boolean)Shaders.isRainDepth());
        }
        GlStateManager.enableCull();
        this.mc.profiler.endStartSection("weather");
        if (isShaders) {
            Shaders.beginWeather();
        }
        this.renderRainSnow(partialTicks);
        if (isShaders) {
            Shaders.endWeather();
        }
        GlStateManager.depthMask((boolean)true);
        renderglobal.renderWorldBorder(entity, partialTicks);
        if (isShaders) {
            ShadersRender.renderHand0((EntityRenderer)this, (float)partialTicks, (int)pass);
            Shaders.preWater();
        }
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        this.setupFog(0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask((boolean)false);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.shadeModel((int)7425);
        this.mc.profiler.endStartSection("translucent");
        if (isShaders) {
            Shaders.beginWater();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, (double)partialTicks, pass, entity);
        if (isShaders) {
            Shaders.endWater();
        }
        if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
            RenderHelper.enableStandardItemLighting();
            this.mc.profiler.endStartSection("entities");
            Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_setRenderPass, (Object[])new Object[]{1});
            this.mc.renderGlobal.renderEntities(entity, (ICamera)icamera, partialTicks);
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_setRenderPass, (Object[])new Object[]{-1});
            RenderHelper.disableStandardItemLighting();
        }
        GlStateManager.shadeModel((int)7424);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (entity.posY + (double)entity.getEyeHeight() >= 128.0 + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0f)) {
            this.mc.profiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
        }
        if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
            this.mc.profiler.endStartSection("forge_render_last");
            Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_dispatchRenderLast, (Object[])new Object[]{renderglobal, Float.valueOf((float)partialTicks)});
        }
        this.mc.profiler.endStartSection("hand");
        if (this.renderHand && !Shaders.isShadowPass) {
            if (isShaders) {
                ShadersRender.renderHand1((EntityRenderer)this, (float)partialTicks, (int)pass);
                Shaders.renderCompositeFinal();
            }
            GlStateManager.clear((int)256);
            if (isShaders) {
                ShadersRender.renderFPOverlay((EntityRenderer)this, (float)partialTicks, (int)pass);
            } else {
                this.renderHand(partialTicks, pass);
            }
        }
        if (isShaders) {
            Shaders.endRender();
        }
    }

    private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass, double x, double y, double z) {
        if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds((GameSettings)this.mc.gameSettings)) {
            this.mc.profiler.endStartSection("clouds");
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.clipDistance * 4.0f));
            GlStateManager.matrixMode((int)5888);
            GlStateManager.pushMatrix();
            this.setupFog(0, partialTicks);
            renderGlobalIn.renderClouds(partialTicks, pass, x, y, z);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)this.clipDistance);
            GlStateManager.matrixMode((int)5888);
        }
    }

    private void addRainParticles() {
        float f = this.mc.world.getRainStrength(1.0f);
        if (!Config.isRainFancy()) {
            f /= 2.0f;
        }
        if (f != 0.0f && Config.isRainSplash()) {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            Entity entity = this.mc.getRenderViewEntity();
            WorldClient world = this.mc.world;
            BlockPos blockpos = new BlockPos(entity);
            int i = 10;
            double d0 = 0.0;
            double d1 = 0.0;
            double d2 = 0.0;
            int j = 0;
            int k = (int)(100.0f * f * f);
            if (this.mc.gameSettings.particleSetting == 1) {
                k >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
                k = 0;
            }
            for (int l = 0; l < k; ++l) {
                BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(this.random.nextInt(10) - this.random.nextInt(10), 0, this.random.nextInt(10) - this.random.nextInt(10)));
                Biome biome = world.getBiome(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                IBlockState iblockstate = world.getBlockState(blockpos2);
                if (blockpos1.q() > blockpos.q() + 10 || blockpos1.q() < blockpos.q() - 10 || !biome.canRain() || !(biome.getTemperature(blockpos1) >= 0.15f)) continue;
                double d3 = this.random.nextDouble();
                double d4 = this.random.nextDouble();
                AxisAlignedBB axisalignedbb = iblockstate.e((IBlockAccess)world, blockpos2);
                if (iblockstate.a() != Material.LAVA && iblockstate.getBlock() != Blocks.MAGMA) {
                    if (iblockstate.a() == Material.AIR) continue;
                    if (this.random.nextInt(++j) == 0) {
                        d0 = (double)blockpos2.p() + d3;
                        d1 = (double)((float)blockpos2.q() + 0.1f) + axisalignedbb.maxY - 1.0;
                        d2 = (double)blockpos2.r() + d4;
                    }
                    this.mc.world.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockpos2.p() + d3, (double)((float)blockpos2.q() + 0.1f) + axisalignedbb.maxY, (double)blockpos2.r() + d4, 0.0, 0.0, 0.0, new int[0]);
                    continue;
                }
                this.mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.p() + d3, (double)((float)blockpos1.q() + 0.1f) - axisalignedbb.minY, (double)blockpos1.r() + d4, 0.0, 0.0, 0.0, new int[0]);
            }
            if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (d1 > (double)(blockpos.q() + 1) && world.getPrecipitationHeight(blockpos).q() > MathHelper.floor((float)blockpos.q())) {
                    this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1f, 0.5f, false);
                } else {
                    this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2f, 1.0f, false);
                }
            }
        }
    }

    protected void renderRainSnow(float partialTicks) {
        WorldProvider worldProvider;
        Object weatherRenderer;
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists() && (weatherRenderer = Reflector.call((Object)(worldProvider = this.mc.world.provider), (ReflectorMethod)Reflector.ForgeWorldProvider_getWeatherRenderer, (Object[])new Object[0])) != null) {
            Reflector.callVoid((Object)weatherRenderer, (ReflectorMethod)Reflector.IRenderHandler_render, (Object[])new Object[]{Float.valueOf((float)partialTicks), this.mc.world, this.mc});
            return;
        }
        float f = this.mc.world.getRainStrength(partialTicks);
        if (f > 0.0f) {
            if (Config.isRainOff()) {
                return;
            }
            this.enableLightmap();
            Entity entity = this.mc.getRenderViewEntity();
            WorldClient world = this.mc.world;
            int i = MathHelper.floor((double)entity.posX);
            int j = MathHelper.floor((double)entity.posY);
            int k = MathHelper.floor((double)entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.disableCull();
            GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GlStateManager.alphaFunc((int)516, (float)0.1f);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            int l = MathHelper.floor((double)d1);
            int i1 = 5;
            if (Config.isRainFancy()) {
                i1 = 10;
            }
            int j1 = -1;
            float f1 = (float)this.rendererUpdateCount + partialTicks;
            bufferbuilder.setTranslation(-d0, -d1, -d2);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k1 = k - i1; k1 <= k + i1; ++k1) {
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                    double d3 = (double)this.rainXCoords[i2] * 0.5;
                    double d4 = (double)this.rainYCoords[i2] * 0.5;
                    blockpos$mutableblockpos.setPos(l1, 0, k1);
                    Biome biome = world.getBiome((BlockPos)blockpos$mutableblockpos);
                    if (!biome.canRain() && !biome.getEnableSnow()) continue;
                    int j2 = world.getPrecipitationHeight((BlockPos)blockpos$mutableblockpos).q();
                    int k2 = j - i1;
                    int l2 = j + i1;
                    if (k2 < j2) {
                        k2 = j2;
                    }
                    if (l2 < j2) {
                        l2 = j2;
                    }
                    int i3 = j2;
                    if (j2 < l) {
                        i3 = l;
                    }
                    if (k2 == l2) continue;
                    this.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                    blockpos$mutableblockpos.setPos(l1, k2, k1);
                    float f2 = biome.getTemperature((BlockPos)blockpos$mutableblockpos);
                    if (world.getBiomeProvider().getTemperatureAtHeight(f2, j2) >= 0.15f) {
                        if (j1 != 0) {
                            if (j1 >= 0) {
                                tessellator.draw();
                            }
                            j1 = 0;
                            this.mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        double d5 = -((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + this.random.nextDouble());
                        double d6 = (double)((float)l1 + 0.5f) - entity.posX;
                        double d7 = (double)((float)k1 + 0.5f) - entity.posZ;
                        float f3 = MathHelper.sqrt((double)(d6 * d6 + d7 * d7)) / (float)i1;
                        float f4 = ((1.0f - f3 * f3) * 0.5f + 0.5f) * f;
                        blockpos$mutableblockpos.setPos(l1, i3, k1);
                        int j3 = world.getCombinedLight((BlockPos)blockpos$mutableblockpos, 0);
                        int k3 = j3 >> 16 & 0xFFFF;
                        int l3 = j3 & 0xFFFF;
                        bufferbuilder.pos((double)l1 - d3 + 0.5, (double)l2, (double)k1 - d4 + 0.5).tex(0.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, (double)l2, (double)k1 + d4 + 0.5).tex(1.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, (double)k2, (double)k1 + d4 + 0.5).tex(1.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                        bufferbuilder.pos((double)l1 - d3 + 0.5, (double)k2, (double)k1 - d4 + 0.5).tex(0.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                        continue;
                    }
                    if (j1 != 1) {
                        if (j1 >= 0) {
                            tessellator.draw();
                        }
                        j1 = 1;
                        this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                        bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    }
                    double d8 = -((float)(this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                    double d9 = this.random.nextDouble() + (double)f1 * 0.01 * (double)((float)this.random.nextGaussian());
                    double d10 = this.random.nextDouble() + (double)(f1 * (float)this.random.nextGaussian()) * 0.001;
                    double d11 = (double)((float)l1 + 0.5f) - entity.posX;
                    double d12 = (double)((float)k1 + 0.5f) - entity.posZ;
                    float f6 = MathHelper.sqrt((double)(d11 * d11 + d12 * d12)) / (float)i1;
                    float f5 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f;
                    blockpos$mutableblockpos.setPos(l1, i3, k1);
                    int i4 = (world.getCombinedLight((BlockPos)blockpos$mutableblockpos, 0) * 3 + 0xF000F0) / 4;
                    int j4 = i4 >> 16 & 0xFFFF;
                    int k4 = i4 & 0xFFFF;
                    bufferbuilder.pos((double)l1 - d3 + 0.5, (double)l2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
                    bufferbuilder.pos((double)l1 + d3 + 0.5, (double)l2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
                    bufferbuilder.pos((double)l1 + d3 + 0.5, (double)k2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
                    bufferbuilder.pos((double)l1 - d3 + 0.5, (double)k2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
                }
            }
            if (j1 >= 0) {
                tessellator.draw();
            }
            bufferbuilder.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc((int)516, (float)0.1f);
            this.disableLightmap();
        }
    }

    public void setupOverlayRendering() {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.clear((int)256);
        GlStateManager.matrixMode((int)5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho((double)0.0, (double)scaledresolution.getScaledWidth_double(), (double)scaledresolution.getScaledHeight_double(), (double)0.0, (double)1000.0, (double)3000.0);
        GlStateManager.matrixMode((int)5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)-2000.0f);
    }

    private void updateFogColor(float partialTicks) {
        Vec3d colUnderlava;
        float f9;
        WorldClient world = this.mc.world;
        Entity entity = this.mc.getRenderViewEntity();
        float f = 0.25f + 0.75f * (float)this.mc.gameSettings.renderDistanceChunks / 32.0f;
        f = 1.0f - (float)Math.pow((double)f, (double)0.25);
        Vec3d vec3d = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        vec3d = CustomColors.getWorldSkyColor((Vec3d)vec3d, (World)world, (Entity)this.mc.getRenderViewEntity(), (float)partialTicks);
        float f1 = (float)vec3d.x;
        float f2 = (float)vec3d.y;
        float f3 = (float)vec3d.z;
        Vec3d vec3d1 = world.getFogColor(partialTicks);
        vec3d1 = CustomColors.getWorldFogColor((Vec3d)vec3d1, (World)world, (Entity)this.mc.getRenderViewEntity(), (float)partialTicks);
        this.fogColorRed = (float)vec3d1.x;
        this.fogColorGreen = (float)vec3d1.y;
        this.fogColorBlue = (float)vec3d1.z;
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            float[] afloat;
            double d0 = MathHelper.sin((float)world.getCelestialAngleRadians(partialTicks)) > 0.0f ? -1.0 : 1.0;
            Vec3d vec3d2 = new Vec3d(d0, 0.0, 0.0);
            float f5 = (float)entity.getLook(partialTicks).dotProduct(vec3d2);
            if (f5 < 0.0f) {
                f5 = 0.0f;
            }
            if (f5 > 0.0f && (afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks)) != null) {
                this.fogColorRed = this.fogColorRed * (1.0f - (f5 *= afloat[3])) + afloat[0] * f5;
                this.fogColorGreen = this.fogColorGreen * (1.0f - f5) + afloat[1] * f5;
                this.fogColorBlue = this.fogColorBlue * (1.0f - f5) + afloat[2] * f5;
            }
        }
        this.fogColorRed += (f1 - this.fogColorRed) * f;
        this.fogColorGreen += (f2 - this.fogColorGreen) * f;
        this.fogColorBlue += (f3 - this.fogColorBlue) * f;
        float f8 = world.getRainStrength(partialTicks);
        if (f8 > 0.0f) {
            float f4 = 1.0f - f8 * 0.5f;
            float f10 = 1.0f - f8 * 0.4f;
            this.fogColorRed *= f4;
            this.fogColorGreen *= f4;
            this.fogColorBlue *= f10;
        }
        if ((f9 = world.getThunderStrength(partialTicks)) > 0.0f) {
            float f11 = 1.0f - f9 * 0.5f;
            this.fogColorRed *= f11;
            this.fogColorGreen *= f11;
            this.fogColorBlue *= f11;
        }
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint((World)this.mc.world, (Entity)entity, (float)partialTicks);
        if (this.cloudFog) {
            Vec3d vec3d3 = world.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec3d3.x;
            this.fogColorGreen = (float)vec3d3.y;
            this.fogColorBlue = (float)vec3d3.z;
        } else if (Reflector.ForgeBlock_getFogColor.exists()) {
            Vec3d viewport = ActiveRenderInfo.projectViewFromEntity((Entity)entity, (double)partialTicks);
            BlockPos viewportPos = new BlockPos(viewport);
            IBlockState viewportState = this.mc.world.getBlockState(viewportPos);
            Vec3d inMaterialColor = (Vec3d)Reflector.call((Object)viewportState.getBlock(), (ReflectorMethod)Reflector.ForgeBlock_getFogColor, (Object[])new Object[]{this.mc.world, viewportPos, viewportState, entity, new Vec3d((double)this.fogColorRed, (double)this.fogColorGreen, (double)this.fogColorBlue), Float.valueOf((float)partialTicks)});
            this.fogColorRed = (float)inMaterialColor.x;
            this.fogColorGreen = (float)inMaterialColor.y;
            this.fogColorBlue = (float)inMaterialColor.z;
        } else if (iblockstate.a() == Material.WATER) {
            float f12 = 0.0f;
            if (entity instanceof EntityLivingBase) {
                f12 = (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)((EntityLivingBase)entity)) * 0.2f;
                f12 = Config.limit((float)f12, (float)0.0f, (float)0.6f);
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    f12 = f12 * 0.3f + 0.6f;
                }
            }
            this.fogColorRed = 0.02f + f12;
            this.fogColorGreen = 0.02f + f12;
            this.fogColorBlue = 0.2f + f12;
        } else if (iblockstate.a() == Material.LAVA) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
        }
        if (iblockstate.a() == Material.WATER) {
            Vec3d colUnderwater = CustomColors.getUnderwaterColor((IBlockAccess)this.mc.world, (double)this.mc.getRenderViewEntity().posX, (double)(this.mc.getRenderViewEntity().posY + 1.0), (double)this.mc.getRenderViewEntity().posZ);
            if (colUnderwater != null) {
                this.fogColorRed = (float)colUnderwater.x;
                this.fogColorGreen = (float)colUnderwater.y;
                this.fogColorBlue = (float)colUnderwater.z;
            }
        } else if (iblockstate.a() == Material.LAVA && (colUnderlava = CustomColors.getUnderlavaColor((IBlockAccess)this.mc.world, (double)this.mc.getRenderViewEntity().posX, (double)(this.mc.getRenderViewEntity().posY + 1.0), (double)this.mc.getRenderViewEntity().posZ)) != null) {
            this.fogColorRed = (float)colUnderlava.x;
            this.fogColorGreen = (float)colUnderlava.y;
            this.fogColorBlue = (float)colUnderlava.z;
        }
        float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f13;
        this.fogColorGreen *= f13;
        this.fogColorBlue *= f13;
        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks) * world.provider.getVoidFogYFactor();
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            d1 = i < 20 ? (d1 *= (double)(1.0f - (float)i / 20.0f)) : 0.0;
        }
        if (d1 < 1.0) {
            if (d1 < 0.0) {
                d1 = 0.0;
            }
            d1 *= d1;
            this.fogColorRed = (float)((double)this.fogColorRed * d1);
            this.fogColorGreen = (float)((double)this.fogColorGreen * d1);
            this.fogColorBlue = (float)((double)this.fogColorBlue * d1);
        }
        if (this.bossColorModifier > 0.0f) {
            float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
            this.fogColorRed = this.fogColorRed * (1.0f - f14) + this.fogColorRed * 0.7f * f14;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f14) + this.fogColorGreen * 0.6f * f14;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f14) + this.fogColorBlue * 0.6f * f14;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.NIGHT_VISION)) {
            float f15 = this.getNightVisionBrightness((EntityLivingBase)entity, partialTicks);
            float f6 = 1.0f / this.fogColorRed;
            if (f6 > 1.0f / this.fogColorGreen) {
                f6 = 1.0f / this.fogColorGreen;
            }
            if (f6 > 1.0f / this.fogColorBlue) {
                f6 = 1.0f / this.fogColorBlue;
            }
            if (Float.isInfinite((float)f6)) {
                f6 = Math.nextAfter((float)f6, (double)0.0);
            }
            this.fogColorRed = this.fogColorRed * (1.0f - f15) + this.fogColorRed * f6 * f15;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f15) + this.fogColorGreen * f6 * f15;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f15) + this.fogColorBlue * f6 * f15;
        }
        if (this.mc.gameSettings.anaglyph) {
            float f16 = (this.fogColorRed * 30.0f + this.fogColorGreen * 59.0f + this.fogColorBlue * 11.0f) / 100.0f;
            float f17 = (this.fogColorRed * 30.0f + this.fogColorGreen * 70.0f) / 100.0f;
            float f7 = (this.fogColorRed * 30.0f + this.fogColorBlue * 70.0f) / 100.0f;
            this.fogColorRed = f16;
            this.fogColorGreen = f17;
            this.fogColorBlue = f7;
        }
        if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
            Object event = Reflector.newInstance((ReflectorConstructor)Reflector.EntityViewRenderEvent_FogColors_Constructor, (Object[])new Object[]{this, entity, iblockstate, Float.valueOf((float)partialTicks), Float.valueOf((float)this.fogColorRed), Float.valueOf((float)this.fogColorGreen), Float.valueOf((float)this.fogColorBlue)});
            Reflector.postForgeBusEvent((Object)event);
            this.fogColorRed = Reflector.callFloat((Object)event, (ReflectorMethod)Reflector.EntityViewRenderEvent_FogColors_getRed, (Object[])new Object[0]);
            this.fogColorGreen = Reflector.callFloat((Object)event, (ReflectorMethod)Reflector.EntityViewRenderEvent_FogColors_getGreen, (Object[])new Object[0]);
            this.fogColorBlue = Reflector.callFloat((Object)event, (ReflectorMethod)Reflector.EntityViewRenderEvent_FogColors_getBlue, (Object[])new Object[0]);
        }
        Shaders.setClearColor((float)this.fogColorRed, (float)this.fogColorGreen, (float)this.fogColorBlue, (float)0.0f);
    }

    private void setupFog(int startCoords, float partialTicks) {
        this.fogStandard = false;
        Entity entity = this.mc.getRenderViewEntity();
        this.setupFogColor(false);
        GlStateManager.glNormal3f((float)0.0f, (float)-1.0f, (float)0.0f);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint((World)this.mc.world, (Entity)entity, (float)partialTicks);
        float forgeFogDensity = -1.0f;
        if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
            forgeFogDensity = Reflector.callFloat((ReflectorMethod)Reflector.ForgeHooksClient_getFogDensity, (Object[])new Object[]{this, entity, iblockstate, Float.valueOf((float)partialTicks), Float.valueOf((float)0.1f)});
        }
        if (forgeFogDensity >= 0.0f) {
            GlStateManager.setFogDensity((float)forgeFogDensity);
        } else if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
            float f1 = 5.0f;
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            if (i < 20) {
                f1 = 5.0f + (this.farPlaneDistance - 5.0f) * (1.0f - (float)i / 20.0f);
            }
            GlStateManager.setFog((GlStateManager.FogMode)GlStateManager.FogMode.LINEAR);
            if (startCoords == -1) {
                GlStateManager.setFogStart((float)0.0f);
                GlStateManager.setFogEnd((float)(f1 * 0.8f));
            } else {
                GlStateManager.setFogStart((float)(f1 * 0.25f));
                GlStateManager.setFogEnd((float)f1);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                GlStateManager.glFogi((int)34138, (int)34139);
            }
        } else if (this.cloudFog) {
            GlStateManager.setFog((GlStateManager.FogMode)GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity((float)0.1f);
        } else if (iblockstate.a() == Material.WATER) {
            float waterFogDensityMax;
            GlStateManager.setFog((GlStateManager.FogMode)GlStateManager.FogMode.EXP);
            float f = waterFogDensityMax = Config.isClearWater() ? 0.02f : 0.1f;
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    GlStateManager.setFogDensity((float)0.01f);
                } else {
                    float waterFogDensity = 0.1f - (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)((EntityLivingBase)entity)) * 0.03f;
                    GlStateManager.setFogDensity((float)Config.limit((float)waterFogDensity, (float)0.0f, (float)waterFogDensityMax));
                }
            } else {
                GlStateManager.setFogDensity((float)waterFogDensityMax);
            }
        } else if (iblockstate.a() == Material.LAVA) {
            GlStateManager.setFog((GlStateManager.FogMode)GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity((float)2.0f);
        } else {
            float f = this.farPlaneDistance;
            this.fogStandard = true;
            GlStateManager.setFog((GlStateManager.FogMode)GlStateManager.FogMode.LINEAR);
            if (startCoords == -1) {
                GlStateManager.setFogStart((float)0.0f);
                GlStateManager.setFogEnd((float)f);
            } else {
                GlStateManager.setFogStart((float)(f * Config.getFogStart()));
                GlStateManager.setFogEnd((float)f);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                if (Config.isFogFancy()) {
                    GlStateManager.glFogi((int)34138, (int)34139);
                }
                if (Config.isFogFast()) {
                    GlStateManager.glFogi((int)34138, (int)34140);
                }
            }
            if (this.mc.world.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog()) {
                GlStateManager.setFogStart((float)(f * 0.05f));
                GlStateManager.setFogEnd((float)f);
            }
            if (Reflector.ForgeHooksClient_onFogRender.exists()) {
                Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_onFogRender, (Object[])new Object[]{this, entity, iblockstate, Float.valueOf((float)partialTicks), startCoords, Float.valueOf((float)f)});
            }
        }
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial((int)1028, (int)4608);
    }

    public void setupFogColor(boolean black) {
        if (black) {
            GlStateManager.glFog((int)2918, (FloatBuffer)this.setFogColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        } else {
            GlStateManager.glFog((int)2918, (FloatBuffer)this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        }
    }

    private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
        if (Config.isShaders()) {
            Shaders.setFogColor((float)red, (float)green, (float)blue);
        }
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }

    public void resetData() {
        this.itemActivationItem = null;
        this.mapItemRenderer.clearLoadedMaps();
    }

    public MapItemRenderer getMapItemRenderer() {
        return this.mapItemRenderer;
    }

    private void waitForServerThread() {
        this.serverWaitTimeCurrent = 0;
        if (!Config.isSmoothWorld() || !Config.isSingleProcessor()) {
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            return;
        }
        if (!this.mc.isIntegratedServerRunning()) {
            return;
        }
        IntegratedServer srv = this.mc.getIntegratedServer();
        if (srv == null) {
            return;
        }
        boolean paused = this.mc.isGamePaused();
        if (paused || this.mc.currentScreen instanceof GuiDownloadTerrain) {
            if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                Config.sleep((long)20L);
            }
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            return;
        }
        if (this.serverWaitTime > 0) {
            Lagometer.timerServer.start();
            Config.sleep((long)this.serverWaitTime);
            Lagometer.timerServer.end();
            this.serverWaitTimeCurrent = this.serverWaitTime;
        }
        long timeNow = System.nanoTime() / 1000000L;
        if (this.lastServerTime == 0L || this.lastServerTicks == 0) {
            this.lastServerTime = timeNow;
            this.lastServerTicks = srv.getTickCounter();
            this.avgServerTickDiff = 1.0f;
            this.avgServerTimeDiff = 50.0f;
            return;
        }
        long timeDiff = timeNow - this.lastServerTime;
        if (timeDiff < 0L) {
            this.lastServerTime = timeNow;
            timeDiff = 0L;
        }
        if (timeDiff < 50L) {
            return;
        }
        this.lastServerTime = timeNow;
        int ticks = srv.getTickCounter();
        int tickDiff = ticks - this.lastServerTicks;
        if (tickDiff < 0) {
            this.lastServerTicks = ticks;
            tickDiff = 0;
        }
        if (tickDiff < 1 && this.serverWaitTime < 100) {
            this.serverWaitTime += 2;
        }
        if (tickDiff > 1 && this.serverWaitTime > 0) {
            --this.serverWaitTime;
        }
        this.lastServerTicks = ticks;
    }

    private void frameInit() {
        GlErrors.frameStart();
        if (!this.initialized) {
            ReflectorResolver.resolve();
            TextureUtils.registerResourceListener();
            if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
                Config.setNotify64BitJava((boolean)true);
            }
            this.initialized = true;
        }
        Config.checkDisplayMode();
        WorldClient world = this.mc.world;
        if (world != null) {
            if (Config.getNewRelease() != null) {
                String userEdition = "HD_U".replace((CharSequence)"HD_U", (CharSequence)"HD Ultra").replace((CharSequence)"L", (CharSequence)"Light");
                String fullNewVer = userEdition + " " + Config.getNewRelease();
                TextComponentString msg = new TextComponentString(I18n.format((String)"of.message.newVersion", (Object[])new Object[]{"\u00a7n" + fullNewVer + "\u00a7r"}));
                msg.a(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://optifine.net/downloads")));
                this.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)msg);
                Config.setNewRelease(null);
            }
            if (Config.isNotify64BitJava()) {
                Config.setNotify64BitJava((boolean)false);
                TextComponentString msg = new TextComponentString(I18n.format((String)"of.message.java64Bit", (Object[])new Object[0]));
                this.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)msg);
            }
        }
        if (this.mc.currentScreen instanceof GuiMainMenu) {
            this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
        }
        if (this.updatedWorld != world) {
            RandomEntities.worldChanged((World)this.updatedWorld, (World)world);
            Config.updateThreadPriorities();
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            this.updatedWorld = world;
        }
        if (!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
            Shaders.configAntialiasingLevel = 0;
        }
        if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == GuiChat.class) {
            this.mc.displayGuiScreen((GuiScreen)new GuiChatOF((GuiChat)this.mc.currentScreen));
        }
    }

    private void frameFinish() {
        int err;
        if (this.mc.world != null && Config.isShowGlErrors() && TimedEvent.isActive((String)"CheckGlErrorFrameFinish", (long)10000L) && (err = GlStateManager.glGetError()) != 0 && GlErrors.isEnabled((int)err)) {
            String text = Config.getGlErrorString((int)err);
            TextComponentString msg = new TextComponentString(I18n.format((String)"of.message.openglError", (Object[])new Object[]{err, text}));
            this.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)msg);
        }
    }

    private void updateMainMenu(GuiMainMenu mainGui) {
        try {
            String str = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(5);
            int month = calendar.get(2) + 1;
            if (day == 8 && month == 4) {
                str = "Happy birthday, OptiFine!";
            }
            if (day == 14 && month == 8) {
                str = "Happy birthday, sp614x!";
            }
            if (str == null) {
                return;
            }
            Reflector.setFieldValue((Object)mainGui, (ReflectorField)Reflector.GuiMainMenu_splashText, (Object)str);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public boolean setFxaaShader(int fxaaLevel) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return false;
        }
        if (this.shaderGroup != null && this.shaderGroup != this.fxaaShaders[2] && this.shaderGroup != this.fxaaShaders[4]) {
            return true;
        }
        if (fxaaLevel == 2 || fxaaLevel == 4) {
            if (this.shaderGroup != null && this.shaderGroup == this.fxaaShaders[fxaaLevel]) {
                return true;
            }
            if (this.mc.world == null) {
                return true;
            }
            this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + fxaaLevel + "x.json"));
            this.fxaaShaders[fxaaLevel] = this.shaderGroup;
            return this.useShader;
        }
        if (this.shaderGroup == null) {
            return true;
        }
        this.shaderGroup.deleteShaderGroup();
        this.shaderGroup = null;
        return true;
    }

    private void checkLoadVisibleChunks(Entity entity, float partialTicks, ICamera icamera, boolean spectator) {
        int messageId = 201435902;
        if (this.loadVisibleChunks) {
            this.loadVisibleChunks = false;
            this.loadAllVisibleChunks(entity, partialTicks, icamera, spectator);
            this.mc.ingameGUI.getChatGUI().deleteChatLine(messageId);
        }
        if (Keyboard.isKeyDown((int)61) && Keyboard.isKeyDown((int)38)) {
            if (this.mc.gameSettings.keyBindAdvancements.getKeyCode() == 38) {
                if (this.mc.currentScreen instanceof GuiScreenAdvancements) {
                    this.mc.displayGuiScreen(null);
                }
                while (Keyboard.next()) {
                }
            }
            if (this.mc.currentScreen != null) {
                return;
            }
            this.loadVisibleChunks = true;
            TextComponentString msg = new TextComponentString(I18n.format((String)"of.message.loadingVisibleChunks", (Object[])new Object[0]));
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)msg, messageId);
            Reflector.Minecraft_actionKeyF3.setValue((Object)this.mc, (Object)Boolean.TRUE);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadAllVisibleChunks(Entity entity, double partialTicks, ICamera icamera, boolean spectator) {
        int chunkUpdatesConfig = this.mc.gameSettings.ofChunkUpdates;
        boolean lazyChunkLoadingConfig = this.mc.gameSettings.ofLazyChunkLoading;
        try {
            this.mc.gameSettings.ofChunkUpdates = 1000;
            this.mc.gameSettings.ofLazyChunkLoading = false;
            RenderGlobal renderGlobal = Config.getRenderGlobal();
            int countLoadedChunks = renderGlobal.getCountLoadedChunks();
            long timeStart = System.currentTimeMillis();
            Config.dbg((String)"Loading visible chunks");
            long timeLog = System.currentTimeMillis() + 5000L;
            int chunksUpdated = 0;
            boolean hasUpdates = false;
            do {
                hasUpdates = false;
                for (int i = 0; i < 100; ++i) {
                    renderGlobal.displayListEntitiesDirty = true;
                    renderGlobal.setupTerrain(entity, partialTicks, icamera, this.frameCount++, spectator);
                    if (!renderGlobal.hasNoChunkUpdates()) {
                        hasUpdates = true;
                    }
                    chunksUpdated += renderGlobal.getCountChunksToUpdate();
                    while (!renderGlobal.hasNoChunkUpdates()) {
                        renderGlobal.updateChunks(System.nanoTime() + 1000000000L);
                    }
                    chunksUpdated -= renderGlobal.getCountChunksToUpdate();
                    if (!hasUpdates) break;
                }
                if (renderGlobal.getCountLoadedChunks() != countLoadedChunks) {
                    hasUpdates = true;
                    countLoadedChunks = renderGlobal.getCountLoadedChunks();
                }
                if (System.currentTimeMillis() <= timeLog) continue;
                Config.log((String)("Chunks loaded: " + chunksUpdated));
                timeLog = System.currentTimeMillis() + 5000L;
            } while (hasUpdates);
            Config.log((String)("Chunks loaded: " + chunksUpdated));
            Config.log((String)"Finished loading visible chunks");
            RenderChunk.renderChunksUpdated = 0;
        }
        finally {
            this.mc.gameSettings.ofChunkUpdates = chunkUpdatesConfig;
            this.mc.gameSettings.ofLazyChunkLoading = lazyChunkLoadingConfig;
        }
    }

    public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-viewerYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)-0.025f, (float)-0.025f, (float)0.025f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask((boolean)false);
        if (!isSneaking) {
            GlStateManager.disableDepth();
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)(-i - 1), (double)(-1 + verticalShift), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos((double)(-i - 1), (double)(8 + verticalShift), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos((double)(i + 1), (double)(8 + verticalShift), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos((double)(i + 1), (double)(-1 + verticalShift), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 0x20FFFFFF);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask((boolean)true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 0x20FFFFFF : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
    }

    public void displayItemActivation(ItemStack p_190565_1_) {
        this.itemActivationItem = p_190565_1_;
        this.itemActivationTicks = 40;
        this.itemActivationOffX = this.random.nextFloat() * 2.0f - 1.0f;
        this.itemActivationOffY = this.random.nextFloat() * 2.0f - 1.0f;
    }

    private void renderItemActivation(int p_190563_1_, int p_190563_2_, float p_190563_3_) {
        if (this.itemActivationItem != null && this.itemActivationTicks > 0) {
            int i = 40 - this.itemActivationTicks;
            float f = ((float)i + p_190563_3_) / 40.0f;
            float f1 = f * f;
            float f2 = f * f1;
            float f3 = 10.25f * f2 * f1 + -24.95f * f1 * f1 + 25.5f * f2 + -13.8f * f1 + 4.0f * f;
            float f4 = f3 * (float)Math.PI;
            float f5 = this.itemActivationOffX * (float)(p_190563_1_ / 4);
            float f6 = this.itemActivationOffY * (float)(p_190563_2_ / 4);
            GlStateManager.enableAlpha();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.enableDepth();
            GlStateManager.disableCull();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.translate((float)((float)(p_190563_1_ / 2) + f5 * MathHelper.abs((float)MathHelper.sin((float)(f4 * 2.0f)))), (float)((float)(p_190563_2_ / 2) + f6 * MathHelper.abs((float)MathHelper.sin((float)(f4 * 2.0f)))), (float)-50.0f);
            float f7 = 50.0f + 175.0f * MathHelper.sin((float)f4);
            GlStateManager.scale((float)f7, (float)(-f7), (float)f7);
            GlStateManager.rotate((float)(900.0f * MathHelper.abs((float)MathHelper.sin((float)f4))), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(6.0f * MathHelper.cos((float)(f * 8.0f))), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(6.0f * MathHelper.cos((float)(f * 8.0f))), (float)0.0f, (float)0.0f, (float)1.0f);
            this.mc.getRenderItem().renderItem(this.itemActivationItem, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableCull();
            GlStateManager.disableDepth();
        }
    }

    static /* synthetic */ Minecraft access$000(EntityRenderer x0) {
        return x0.mc;
    }

    static {
        SHADERS_TEXTURES = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
        SHADER_COUNT = SHADERS_TEXTURES.length;
    }
}
