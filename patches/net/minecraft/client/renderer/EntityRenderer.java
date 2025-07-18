package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.Buffer;
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
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
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.optifine.CustomColors;
import net.optifine.GlErrors;
import net.optifine.Lagometer;
import net.optifine.RandomEntities;
import net.optifine.gui.GuiChatOF;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
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

public class EntityRenderer implements IResourceManagerReloadListener {
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
   private final float thirdPersonDistance = 4.0F;
   private float thirdPersonDistancePrev = 4.0F;
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
   private final FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
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
   private static final ResourceLocation[] SHADERS_TEXTURES = new ResourceLocation[]{
      new ResourceLocation("shaders/post/notch.json"),
      new ResourceLocation("shaders/post/fxaa.json"),
      new ResourceLocation("shaders/post/art.json"),
      new ResourceLocation("shaders/post/bumpy.json"),
      new ResourceLocation("shaders/post/blobs2.json"),
      new ResourceLocation("shaders/post/pencil.json"),
      new ResourceLocation("shaders/post/color_convolve.json"),
      new ResourceLocation("shaders/post/deconverge.json"),
      new ResourceLocation("shaders/post/flip.json"),
      new ResourceLocation("shaders/post/invert.json"),
      new ResourceLocation("shaders/post/ntsc.json"),
      new ResourceLocation("shaders/post/outline.json"),
      new ResourceLocation("shaders/post/phosphor.json"),
      new ResourceLocation("shaders/post/scan_pincushion.json"),
      new ResourceLocation("shaders/post/sobel.json"),
      new ResourceLocation("shaders/post/bits.json"),
      new ResourceLocation("shaders/post/desaturate.json"),
      new ResourceLocation("shaders/post/green.json"),
      new ResourceLocation("shaders/post/blur.json"),
      new ResourceLocation("shaders/post/wobble.json"),
      new ResourceLocation("shaders/post/blobs.json"),
      new ResourceLocation("shaders/post/antialias.json"),
      new ResourceLocation("shaders/post/creeper.json"),
      new ResourceLocation("shaders/post/spider.json")
   };
   public static final int SHADER_COUNT = SHADERS_TEXTURES.length;
   private int shaderIndex;
   private boolean useShader;
   public int frameCount;
   private boolean initialized = false;
   private World updatedWorld = null;
   public boolean fogStandard = false;
   private float clipDistance = 128.0F;
   private long lastServerTime = 0L;
   private int lastServerTicks = 0;
   private int serverWaitTime = 0;
   private int serverWaitTimeCurrent = 0;
   private float avgServerTimeDiff = 0.0F;
   private float avgServerTickDiff = 0.0F;
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

      for (int i = 0; i < 32; i++) {
         for (int j = 0; j < 32; j++) {
            float f = j - 16;
            float f1 = i - 16;
            float f2 = MathHelper.sqrt(f * f + f1 * f1);
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
            Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, new Object[]{entityIn, this});
         }
      }
   }

   private void loadShader(ResourceLocation resourceLocationIn) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         try {
            this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
            this.shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
         } catch (IOException var3) {
            LOGGER.warn("Failed to load shader: {}", resourceLocationIn, var3);
            this.shaderIndex = SHADER_COUNT;
            this.useShader = false;
         } catch (JsonSyntaxException var4) {
            LOGGER.warn("Failed to load shader: {}", resourceLocationIn, var4);
            this.shaderIndex = SHADER_COUNT;
            this.useShader = false;
         }
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
      this.thirdPersonDistancePrev = 4.0F;
      if (this.mc.gameSettings.smoothCamera) {
         float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float f1 = f * f * f * 8.0F;
         this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f1);
         this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f1);
         this.smoothCamPartialTicks = 0.0F;
         this.smoothCamYaw = 0.0F;
         this.smoothCamPitch = 0.0F;
      } else {
         this.smoothCamFilterX = 0.0F;
         this.smoothCamFilterY = 0.0F;
         this.mouseFilterXAxis.reset();
         this.mouseFilterYAxis.reset();
      }

      if (this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(this.mc.player);
      }

      Entity viewEntity = this.mc.getRenderViewEntity();
      double vx = viewEntity.posX;
      double vy = viewEntity.posY + viewEntity.getEyeHeight();
      double vz = viewEntity.posZ;
      float f3 = this.mc.world.getLightBrightness(new BlockPos(vx, vy, vz));
      float f4 = this.mc.gameSettings.renderDistanceChunks / 16.0F;
      f4 = MathHelper.clamp(f4, 0.0F, 1.0F);
      float f2 = f3 * (1.0F - f4) + f4;
      this.fogColor1 = this.fogColor1 + (f2 - this.fogColor1) * 0.1F;
      this.rendererUpdateCount++;
      this.itemRenderer.updateEquippedItem();
      this.addRainParticles();
      this.bossColorModifierPrev = this.bossColorModifier;
      if (this.mc.ingameGUI.getBossOverlay().shouldDarkenSky()) {
         this.bossColorModifier += 0.05F;
         if (this.bossColorModifier > 1.0F) {
            this.bossColorModifier = 1.0F;
         }
      } else if (this.bossColorModifier > 0.0F) {
         this.bossColorModifier -= 0.0125F;
      }

      if (this.itemActivationTicks > 0) {
         this.itemActivationTicks--;
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
            d1 = 6.0;
            d0 = d1;
         } else if (d0 > 3.0) {
            flag = true;
         }

         if (this.mc.objectMouseOver != null) {
            d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
         }

         Vec3d vec3d1 = entity.getLook(1.0F);
         Vec3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
         this.pointedEntity = null;
         Vec3d vec3d3 = null;
         float f = 1.0F;
         List<Entity> list = this.mc
            .world
            .getEntitiesInAABBexcluding(
               entity,
               entity.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0, 1.0, 1.0),
               Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                  public boolean apply(@Nullable Entity p_apply_1_) {
                     return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                  }
               })
            );
         double d2 = d1;

         for (int j = 0; j < list.size(); j++) {
            Entity entity1 = list.get(j);
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
            if (axisalignedbb.contains(vec3d)) {
               if (d2 >= 0.0) {
                  this.pointedEntity = entity1;
                  vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                  d2 = 0.0;
               }
            } else if (raytraceresult != null) {
               double d3 = vec3d.distanceTo(raytraceresult.hitVec);
               if (d3 < d2 || d2 == 0.0) {
                  boolean canRiderInteract = false;
                  if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                     canRiderInteract = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                  }

                  if (canRiderInteract || entity1.getLowestRidingEntity() != entity.getLowestRidingEntity()) {
                     this.pointedEntity = entity1;
                     vec3d3 = raytraceresult.hitVec;
                     d2 = d3;
                  } else if (d2 == 0.0) {
                     this.pointedEntity = entity1;
                     vec3d3 = raytraceresult.hitVec;
                  }
               }
            }
         }

         if (this.pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > 3.0) {
            this.pointedEntity = null;
            this.mc.objectMouseOver = new RayTraceResult(Type.MISS, vec3d3, (EnumFacing)null, new BlockPos(vec3d3));
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
      float f = 1.0F;
      if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
         AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
         f = abstractclientplayer.getFovModifier();
      }

      this.fovModifierHandPrev = this.fovModifierHand;
      this.fovModifierHand = this.fovModifierHand + (f - this.fovModifierHand) * 0.5F;
      if (this.fovModifierHand > 1.5F) {
         this.fovModifierHand = 1.5F;
      }

      if (this.fovModifierHand < 0.1F) {
         this.fovModifierHand = 0.1F;
      }
   }

   private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
      if (this.debugView) {
         return 90.0F;
      } else {
         Entity entity = this.mc.getRenderViewEntity();
         float f = 70.0F;
         if (useFOVSetting) {
            f = this.mc.gameSettings.fovSetting;
            if (Config.isDynamicFov()) {
               f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
            }
         }

         boolean zoomActive = false;
         if (this.mc.currentScreen == null) {
            zoomActive = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
         }

         if (zoomActive) {
            if (!Config.zoomMode) {
               Config.zoomMode = true;
               Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
               this.mc.gameSettings.smoothCamera = true;
               this.mc.renderGlobal.displayListEntitiesDirty = true;
            }

            if (Config.zoomMode) {
               f /= 4.0F;
            }
         } else if (Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = Config.zoomSmoothCamera;
            this.mouseFilterXAxis = new MouseFilter();
            this.mouseFilterYAxis = new MouseFilter();
            this.mc.renderGlobal.displayListEntitiesDirty = true;
         }

         if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0F) {
            float f1 = ((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
         }

         IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
         if (iblockstate.a() == Material.WATER) {
            f = f * 60.0F / 70.0F;
         }

         return Reflector.ForgeHooksClient_getFOVModifier.exists()
            ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, new Object[]{this, entity, iblockstate, partialTicks, f})
            : f;
      }
   }

   private void hurtCameraEffect(float partialTicks) {
      if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
         EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
         float f = entitylivingbase.hurtTime - partialTicks;
         if (entitylivingbase.getHealth() <= 0.0F) {
            float f1 = entitylivingbase.deathTime + partialTicks;
            GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
         }

         if (f < 0.0F) {
            return;
         }

         f /= entitylivingbase.maxHurtTime;
         f = MathHelper.sin(f * f * f * f * (float) Math.PI);
         float f2 = entitylivingbase.attackedAtYaw;
         GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
      }
   }

   private void applyBobbing(float partialTicks) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
         float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
         float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
         float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
         float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
         GlStateManager.translate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
         GlStateManager.rotate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
      }
   }

   private void orientCamera(float partialTicks) {
      Entity entity = this.mc.getRenderViewEntity();
      float f = entity.getEyeHeight();
      double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
      double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
      double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
      if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
         f = (float)(f + 1.0);
         GlStateManager.translate(0.0F, 0.3F, 0.0F);
         if (!this.mc.gameSettings.debugCamEnable) {
            BlockPos blockpos = new BlockPos(entity);
            IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
               Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[]{this.mc.world, blockpos, iblockstate, entity});
            } else if (block == Blocks.BED) {
               int j = ((EnumFacing)iblockstate.getValue(BlockBed.D)).getHorizontalIndex();
               GlStateManager.rotate(j * 90, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
         }
      } else if (this.mc.gameSettings.thirdPersonView > 0) {
         double d3 = this.thirdPersonDistancePrev + (4.0F - this.thirdPersonDistancePrev) * partialTicks;
         if (this.mc.gameSettings.debugCamEnable) {
            GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
         } else {
            float f1 = entity.rotationYaw;
            float f2 = entity.rotationPitch;
            if (this.mc.gameSettings.thirdPersonView == 2) {
               f2 += 180.0F;
            }

            double d4 = -MathHelper.sin(f1 * (float) (Math.PI / 180.0)) * MathHelper.cos(f2 * (float) (Math.PI / 180.0)) * d3;
            double d5 = MathHelper.cos(f1 * (float) (Math.PI / 180.0)) * MathHelper.cos(f2 * (float) (Math.PI / 180.0)) * d3;
            double d6 = -MathHelper.sin(f2 * (float) (Math.PI / 180.0)) * d3;

            for (int i = 0; i < 8; i++) {
               float f3 = (i & 1) * 2 - 1;
               float f4 = (i >> 1 & 1) * 2 - 1;
               float f5 = (i >> 2 & 1) * 2 - 1;
               f3 *= 0.1F;
               f4 *= 0.1F;
               f5 *= 0.1F;
               RayTraceResult raytraceresult = this.mc
                  .world
                  .rayTraceBlocks(new Vec3d(d0 + f3, d1 + f4, d2 + f5), new Vec3d(d0 - d4 + f3 + f5, d1 - d6 + f4, d2 - d5 + f5));
               if (raytraceresult != null) {
                  double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));
                  if (d7 < d3) {
                     d3 = d7;
                  }
               }
            }

            if (this.mc.gameSettings.thirdPersonView == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
            GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GlStateManager.translate(0.0F, 0.0F, 0.05F);
      }

      if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
         if (!this.mc.gameSettings.debugCamEnable) {
            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
            float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            float roll = 0.0F;
            if (entity instanceof EntityAnimal) {
               EntityAnimal entityanimal = (EntityAnimal)entity;
               yaw = entityanimal.aQ + (entityanimal.aP - entityanimal.aQ) * partialTicks + 180.0F;
            }

            IBlockState state = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
            Object event = Reflector.newInstance(
               Reflector.EntityViewRenderEvent_CameraSetup_Constructor, new Object[]{this, entity, state, partialTicks, yaw, pitch, roll}
            );
            Reflector.postForgeBusEvent(event);
            roll = Reflector.callFloat(event, Reflector.EntityViewRenderEvent_CameraSetup_getRoll, new Object[0]);
            pitch = Reflector.callFloat(event, Reflector.EntityViewRenderEvent_CameraSetup_getPitch, new Object[0]);
            yaw = Reflector.callFloat(event, Reflector.EntityViewRenderEvent_CameraSetup_getYaw, new Object[0]);
            GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
         }
      } else if (!this.mc.gameSettings.debugCamEnable) {
         GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
         if (entity instanceof EntityAnimal) {
            EntityAnimal entityanimal = (EntityAnimal)entity;
            GlStateManager.rotate(entityanimal.aQ + (entityanimal.aP - entityanimal.aQ) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      GlStateManager.translate(0.0F, -f, 0.0F);
      d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
      d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
      d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
      this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
   }

   public void setupCameraTransform(float partialTicks, int pass) {
      this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
      if (Config.isFogFancy()) {
         this.farPlaneDistance *= 0.95F;
      }

      if (Config.isFogFast()) {
         this.farPlaneDistance *= 0.83F;
      }

      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      float f = 0.07F;
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate(-(pass * 2 - 1) * 0.07F, 0.0F, 0.0F);
      }

      this.clipDistance = this.farPlaneDistance * 2.0F;
      if (this.clipDistance < 173.0F) {
         this.clipDistance = 173.0F;
      }

      if (this.cameraZoom != 1.0) {
         GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
         GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0);
      }

      Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      this.hurtCameraEffect(partialTicks);
      if (this.mc.gameSettings.viewBobbing) {
         this.applyBobbing(partialTicks);
      }

      float f1 = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * partialTicks;
      if (f1 > 0.0F) {
         int i = 20;
         if (this.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            i = 7;
         }

         float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
         f2 *= f2;
         GlStateManager.rotate((this.rendererUpdateCount + partialTicks) * i, 0.0F, 1.0F, 1.0F);
         GlStateManager.scale(1.0F / f2, 1.0F, 1.0F);
         GlStateManager.rotate(-(this.rendererUpdateCount + partialTicks) * i, 0.0F, 1.0F, 1.0F);
      }

      this.orientCamera(partialTicks);
      if (this.debugView) {
         switch (this.debugViewDirection) {
            case 0:
               GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 1:
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 2:
               GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 3:
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
               break;
            case 4:
               GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
         }
      }
   }

   private void renderHand(float partialTicks, int pass) {
      this.renderHand(partialTicks, pass, true, true, false);
   }

   public void renderHand(float partialTicks, int pass, boolean renderItem, boolean renderOverlay, boolean renderTranslucent) {
      if (!this.debugView) {
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         float f = 0.07F;
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate(-(pass * 2 - 1) * 0.07F, 0.0F, 0.0F);
         }

         if (Config.isShaders()) {
            Shaders.applyHandDepth();
         }

         Project.gluPerspective(
            this.getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F
         );
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
         }

         boolean flag = false;
         if (renderItem) {
            GlStateManager.pushMatrix();
            this.hurtCameraEffect(partialTicks);
            if (this.mc.gameSettings.viewBobbing) {
               this.applyBobbing(partialTicks);
            }

            flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
            boolean shouldRenderHand = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, partialTicks, pass);
            if (shouldRenderHand
               && this.mc.gameSettings.thirdPersonView == 0
               && !flag
               && !this.mc.gameSettings.hideGUI
               && !this.mc.playerController.isSpectator()) {
               this.enableLightmap();
               if (Config.isShaders()) {
                  ShadersRender.renderItemFP(this.itemRenderer, partialTicks, renderTranslucent);
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
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if (Config.isShaders()) {
         Shaders.disableLightmap();
      }
   }

   public void enableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      float f = 0.00390625F;
      GlStateManager.scale(0.00390625F, 0.00390625F, 0.00390625F);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.matrixMode(5888);
      this.mc.getTextureManager().bindTexture(this.locationLightMap);
      GlStateManager.glTexParameteri(3553, 10241, 9729);
      GlStateManager.glTexParameteri(3553, 10240, 9729);
      GlStateManager.glTexParameteri(3553, 10242, 33071);
      GlStateManager.glTexParameteri(3553, 10243, 33071);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if (Config.isShaders()) {
         Shaders.enableLightmap();
      }
   }

   private void updateTorchFlicker() {
      this.torchFlickerDX = (float)(this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
      this.torchFlickerDX = (float)(this.torchFlickerDX * 0.9);
      this.torchFlickerX = this.torchFlickerX + (this.torchFlickerDX - this.torchFlickerX);
      this.lightmapUpdateNeeded = true;
   }

   private void updateLightmap(float partialTicks) {
      if (this.lightmapUpdateNeeded) {
         this.mc.profiler.startSection("lightTex");
         World world = this.mc.world;
         if (world != null) {
            if (Config.isCustomColors()
               && CustomColors.updateLightmap(
                  world, this.torchFlickerX, this.lightmapColors, this.mc.player.isPotionActive(MobEffects.NIGHT_VISION), partialTicks
               )) {
               this.lightmapTexture.updateDynamicTexture();
               this.lightmapUpdateNeeded = false;
               this.mc.profiler.endSection();
               return;
            }

            float f = world.getSunBrightness(1.0F);
            float f1 = f * 0.95F + 0.05F;

            for (int i = 0; i < 256; i++) {
               float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
               float f3 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1F + 1.5F);
               if (world.getLastLightningBolt() > 0) {
                  f2 = world.provider.getLightBrightnessTable()[i / 16];
               }

               float f4 = f2 * (f * 0.65F + 0.35F);
               float f5 = f2 * (f * 0.65F + 0.35F);
               float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
               float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
               float f8 = f4 + f3;
               float f9 = f5 + f6;
               float f10 = f2 + f7;
               f8 = f8 * 0.96F + 0.03F;
               f9 = f9 * 0.96F + 0.03F;
               f10 = f10 * 0.96F + 0.03F;
               if (this.bossColorModifier > 0.0F) {
                  float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                  f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
                  f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
                  f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
               }

               if (world.provider.getDimensionType().getId() == 1) {
                  f8 = 0.22F + f3 * 0.75F;
                  f9 = 0.28F + f6 * 0.75F;
                  f10 = 0.25F + f7 * 0.75F;
               }

               if (Reflector.ForgeWorldProvider_getLightmapColors.exists()) {
                  float[] colors = new float[]{f8, f9, f10};
                  Reflector.call(world.provider, Reflector.ForgeWorldProvider_getLightmapColors, new Object[]{partialTicks, f, f2, f3, colors});
                  f8 = colors[0];
                  f9 = colors[1];
                  f10 = colors[2];
               }

               f8 = MathHelper.clamp(f8, 0.0F, 1.0F);
               f9 = MathHelper.clamp(f9, 0.0F, 1.0F);
               f10 = MathHelper.clamp(f10, 0.0F, 1.0F);
               if (this.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                  float f15 = this.getNightVisionBrightness(this.mc.player, partialTicks);
                  float f12 = 1.0F / f8;
                  if (f12 > 1.0F / f9) {
                     f12 = 1.0F / f9;
                  }

                  if (f12 > 1.0F / f10) {
                     f12 = 1.0F / f10;
                  }

                  f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
                  f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
                  f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
               }

               if (f8 > 1.0F) {
                  f8 = 1.0F;
               }

               if (f9 > 1.0F) {
                  f9 = 1.0F;
               }

               if (f10 > 1.0F) {
                  f10 = 1.0F;
               }

               float f16 = this.mc.gameSettings.gammaSetting;
               float f17 = 1.0F - f8;
               float f13 = 1.0F - f9;
               float f14 = 1.0F - f10;
               f17 = 1.0F - f17 * f17 * f17 * f17;
               f13 = 1.0F - f13 * f13 * f13 * f13;
               f14 = 1.0F - f14 * f14 * f14 * f14;
               f8 = f8 * (1.0F - f16) + f17 * f16;
               f9 = f9 * (1.0F - f16) + f13 * f16;
               f10 = f10 * (1.0F - f16) + f14 * f16;
               f8 = f8 * 0.96F + 0.03F;
               f9 = f9 * 0.96F + 0.03F;
               f10 = f10 * 0.96F + 0.03F;
               if (f8 > 1.0F) {
                  f8 = 1.0F;
               }

               if (f9 > 1.0F) {
                  f9 = 1.0F;
               }

               if (f10 > 1.0F) {
                  f10 = 1.0F;
               }

               if (f8 < 0.0F) {
                  f8 = 0.0F;
               }

               if (f9 < 0.0F) {
                  f9 = 0.0F;
               }

               if (f10 < 0.0F) {
                  f10 = 0.0F;
               }

               int j = 255;
               int k = (int)(f8 * 255.0F);
               int l = (int)(f9 * 255.0F);
               int i1 = (int)(f10 * 255.0F);
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
      return i > 200 ? 1.0F : 0.7F + MathHelper.sin((i - partialTicks) * (float) Math.PI * 0.2F) * 0.3F;
   }

   public void updateCameraAndRender(float partialTicks, long nanoTime) {
      this.frameInit();
      boolean flag = Display.isActive();
      if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
         if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
            this.mc.displayInGameMenu();
         }
      } else {
         this.prevFrameTime = Minecraft.getSystemTime();
      }

      this.mc.profiler.startSection("mouse");
      if (flag && Minecraft.IS_RUNNING_ON_MAC && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
         Mouse.setGrabbed(false);
         Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2 - 20);
         Mouse.setGrabbed(true);
      }

      if (this.mc.inGameHasFocus && flag) {
         this.mc.mouseHelper.mouseXYChange();
         this.mc.getTutorial().handleMouse(this.mc.mouseHelper);
         float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float f1 = f * f * f * 8.0F;
         float f2 = this.mc.mouseHelper.deltaX * f1;
         float f3 = this.mc.mouseHelper.deltaY * f1;
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
            this.mc.player.turn(f2, f3 * i);
         } else {
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
            this.mc.player.turn(f2, f3 * i);
         }
      }

      this.mc.profiler.endSection();
      if (!this.mc.skipRenderWorld) {
         anaglyphEnable = this.mc.gameSettings.anaglyph;
         final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
         int i1 = scaledresolution.getScaledWidth();
         int j1 = scaledresolution.getScaledHeight();
         final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
         final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
         int i2 = this.mc.gameSettings.limitFramerate;
         if (this.mc.world == null) {
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.setupOverlayRendering();
            this.renderEndNanoTime = System.nanoTime();
            TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
            TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRenderer;
         } else {
            this.mc.profiler.startSection("level");
            int j = Math.min(Minecraft.getDebugFPS(), i2);
            j = Math.max(j, 60);
            long k = System.nanoTime() - nanoTime;
            long l = Math.max(1000000000 / j / 4 - k, 0L);
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
                  GlStateManager.matrixMode(5890);
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
               GlStateManager.alphaFunc(516, 0.1F);
               this.setupOverlayRendering();
               this.renderItemActivation(i1, j1, partialTicks);
               this.mc.ingameGUI.renderGameOverlay(partialTicks);
               if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                  Config.drawFps();
               }

               if (this.mc.gameSettings.showDebugInfo) {
                  Lagometer.showLagometer(scaledresolution);
               }
            }

            this.mc.profiler.endSection();
         }

         if (this.mc.currentScreen != null) {
            GlStateManager.clear(256);

            try {
               if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, new Object[]{this.mc.currentScreen, k1, l1, this.mc.getTickLength()});
               } else {
                  this.mc.currentScreen.drawScreen(k1, l1, this.mc.getTickLength());
               }
            } catch (Throwable var16) {
               CrashReport crashreport = CrashReport.makeCrashReport(var16, "Rendering screen");
               CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
               crashreportcategory.addDetail("Screen name", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                  }
               });
               crashreportcategory.addDetail("Mouse location", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY());
                  }
               });
               crashreportcategory.addDetail(
                  "Screen size",
                  new ICrashReportDetail<String>() {
                     public String call() throws Exception {
                        return String.format(
                           "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",
                           scaledresolution.getScaledWidth(),
                           scaledresolution.getScaledHeight(),
                           EntityRenderer.this.mc.displayWidth,
                           EntityRenderer.this.mc.displayHeight,
                           scaledresolution.getScaleFactor()
                        );
                     }
                  }
               );
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
         BufferedImage bufferedimage = ScreenShotHelper.createScreenshot(this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer());
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
            Graphics graphics = bufferedimage1.createGraphics();
            graphics.drawImage(bufferedimage, 0, 0, 64, 64, k, l, k + i, l + i, (ImageObserver)null);
            graphics.dispose();
            ImageIO.write(bufferedimage1, "png", this.mc.getIntegratedServer().getWorldIconFile());
         } catch (IOException var8) {
            LOGGER.warn("Couldn't save auto screenshot", var8);
         }
      }
   }

   public void renderStreamIndicator(float partialTicks) {
      this.setupOverlayRendering();
   }

   private boolean isDrawBlockOutline() {
      if (!this.drawBlockOutline) {
         return false;
      } else {
         Entity entity = this.mc.getRenderViewEntity();
         boolean flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
         if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
            ItemStack itemstack = ((EntityPlayer)entity).getHeldItemMainhand();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == Type.BLOCK) {
               BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
               IBlockState state = this.mc.world.getBlockState(blockpos);
               Block block = state.getBlock();
               if (this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
                  flag = ReflectorForge.blockHasTileEntity(state) && this.mc.world.getTileEntity(blockpos) instanceof IInventory;
               } else {
                  flag = !itemstack.isEmpty() && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
               }
            }
         }

         return flag;
      }
   }

   public void renderWorld(float partialTicks, long finishTimeNano) {
      this.updateLightmap(partialTicks);
      if (this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(this.mc.player);
      }

      this.getMouseOver(partialTicks);
      if (Config.isShaders()) {
         Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
      }

      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.mc.profiler.startSection("center");
      if (this.mc.gameSettings.anaglyph) {
         anaglyphField = 0;
         GlStateManager.colorMask(false, true, true, false);
         this.renderWorldPass(0, partialTicks, finishTimeNano);
         anaglyphField = 1;
         GlStateManager.colorMask(true, false, false, false);
         this.renderWorldPass(1, partialTicks, finishTimeNano);
         GlStateManager.colorMask(true, true, true, false);
      } else {
         this.renderWorldPass(2, partialTicks, finishTimeNano);
      }

      this.mc.profiler.endSection();
   }

   private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
      boolean isShaders = Config.isShaders();
      if (isShaders) {
         Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
      }

      RenderGlobal renderglobal = this.mc.renderGlobal;
      ParticleManager particlemanager = this.mc.effectRenderer;
      boolean flag = this.isDrawBlockOutline();
      GlStateManager.enableCull();
      this.mc.profiler.endStartSection("clear");
      if (isShaders) {
         Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      } else {
         GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      }

      this.updateFogColor(partialTicks);
      GlStateManager.clear(16640);
      if (isShaders) {
         Shaders.clearRenderBuffer();
      }

      this.mc.profiler.endStartSection("camera");
      this.setupCameraTransform(partialTicks, pass);
      if (isShaders) {
         Shaders.setCamera(partialTicks);
      }

      if (Reflector.ActiveRenderInfo_updateRenderInfo2.exists()) {
         Reflector.call(Reflector.ActiveRenderInfo_updateRenderInfo2, new Object[]{this.mc.getRenderViewEntity(), this.mc.gameSettings.thirdPersonView == 2});
      } else {
         ActiveRenderInfo.updateRenderInfo(this.mc.player, this.mc.gameSettings.thirdPersonView == 2);
      }

      this.mc.profiler.endStartSection("frustum");
      ClippingHelper clippingHelper = ClippingHelperImpl.getInstance();
      this.mc.profiler.endStartSection("culling");
      clippingHelper.disabled = Config.isShaders() && !Shaders.isFrustumCulling();
      ICamera icamera = new Frustum(clippingHelper);
      Entity entity = this.mc.getRenderViewEntity();
      double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
      double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
      double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
      if (isShaders) {
         ShadersRender.setFrustrumPosition(icamera, d0, d1, d2);
      } else {
         icamera.setPosition(d0, d1, d2);
      }

      if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
         this.setupFog(-1, partialTicks);
         this.mc.profiler.endStartSection("sky");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
         if (isShaders) {
            Shaders.beginSky();
         }

         renderglobal.renderSky(partialTicks, pass);
         if (isShaders) {
            Shaders.endSky();
         }

         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      } else {
         GlStateManager.disableBlend();
      }

      this.setupFog(0, partialTicks);
      GlStateManager.shadeModel(7425);
      if (entity.posY + entity.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0F) {
         this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
      }

      this.mc.profiler.endStartSection("prepareterrain");
      this.setupFog(0, partialTicks);
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      RenderHelper.disableStandardItemLighting();
      this.mc.profiler.endStartSection("terrain_setup");
      this.checkLoadVisibleChunks(entity, partialTicks, icamera, this.mc.player.isSpectator());
      if (isShaders) {
         ShadersRender.setupTerrain(renderglobal, entity, partialTicks, icamera, this.frameCount++, this.mc.player.isSpectator());
      } else {
         renderglobal.setupTerrain(entity, partialTicks, icamera, this.frameCount++, this.mc.player.isSpectator());
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

      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      GlStateManager.disableAlpha();
      if (isShaders) {
         ShadersRender.beginTerrainSolid();
      }

      renderglobal.renderBlockLayer(BlockRenderLayer.SOLID, partialTicks, pass, entity);
      GlStateManager.enableAlpha();
      if (isShaders) {
         ShadersRender.beginTerrainCutoutMipped();
      }

      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0);
      renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
      if (isShaders) {
         ShadersRender.beginTerrainCutout();
      }

      renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT, partialTicks, pass, entity);
      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
      if (isShaders) {
         ShadersRender.endTerrain();
      }

      Lagometer.timerTerrain.end();
      GlStateManager.shadeModel(7424);
      GlStateManager.alphaFunc(516, 0.1F);
      if (!this.debugView) {
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         RenderHelper.enableStandardItemLighting();
         this.mc.profiler.endStartSection("entities");
         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{0});
         }

         renderglobal.renderEntities(entity, icamera, partialTicks);
         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{-1});
         }

         RenderHelper.disableStandardItemLighting();
         this.disableLightmap();
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      if (flag && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.WATER)) {
         EntityPlayer entityplayer = (EntityPlayer)entity;
         GlStateManager.disableAlpha();
         this.mc.profiler.endStartSection("outline");
         if (!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists()
            || !Reflector.callBoolean(
               Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[]{renderglobal, entityplayer, this.mc.objectMouseOver, 0, partialTicks}
            )) {
            renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
         }

         GlStateManager.enableAlpha();
      }

      if (this.mc.debugRenderer.shouldRender()) {
         boolean preDebugFog = GlStateManager.isFogEnabled();
         GlStateManager.disableFog();
         this.mc.debugRenderer.renderDebug(partialTicks, finishTimeNano);
         GlStateManager.setFogEnabled(preDebugFog);
      }

      if (!renderglobal.damagedBlocks.isEmpty()) {
         this.mc.profiler.endStartSection("destroyProgress");
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
         );
         this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
         renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity, partialTicks);
         this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
         GlStateManager.disableBlend();
      }

      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
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

      GlStateManager.depthMask(false);
      if (Config.isShaders()) {
         GlStateManager.depthMask(Shaders.isRainDepth());
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

      GlStateManager.depthMask(true);
      renderglobal.renderWorldBorder(entity, partialTicks);
      if (isShaders) {
         ShadersRender.renderHand0(this, partialTicks, pass);
         Shaders.preWater();
      }

      GlStateManager.disableBlend();
      GlStateManager.enableCull();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.alphaFunc(516, 0.1F);
      this.setupFog(0, partialTicks);
      GlStateManager.enableBlend();
      GlStateManager.depthMask(false);
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      GlStateManager.shadeModel(7425);
      this.mc.profiler.endStartSection("translucent");
      if (isShaders) {
         Shaders.beginWater();
      }

      renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, partialTicks, pass, entity);
      if (isShaders) {
         Shaders.endWater();
      }

      if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
         RenderHelper.enableStandardItemLighting();
         this.mc.profiler.endStartSection("entities");
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{1});
         this.mc.renderGlobal.renderEntities(entity, icamera, partialTicks);
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{-1});
         RenderHelper.disableStandardItemLighting();
      }

      GlStateManager.shadeModel(7424);
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.disableFog();
      if (entity.posY + entity.getEyeHeight() >= 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0F) {
         this.mc.profiler.endStartSection("aboveClouds");
         this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
      }

      if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
         this.mc.profiler.endStartSection("forge_render_last");
         Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[]{renderglobal, partialTicks});
      }

      this.mc.profiler.endStartSection("hand");
      if (this.renderHand && !Shaders.isShadowPass) {
         if (isShaders) {
            ShadersRender.renderHand1(this, partialTicks, pass);
            Shaders.renderCompositeFinal();
         }

         GlStateManager.clear(256);
         if (isShaders) {
            ShadersRender.renderFPOverlay(this, partialTicks, pass);
         } else {
            this.renderHand(partialTicks, pass);
         }
      }

      if (isShaders) {
         Shaders.endRender();
      }
   }

   private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass, double x, double y, double z) {
      if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
         this.mc.profiler.endStartSection("clouds");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         this.setupFog(0, partialTicks);
         renderGlobalIn.renderClouds(partialTicks, pass, x, y, z);
         GlStateManager.disableFog();
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      }
   }

   private void addRainParticles() {
      float f = this.mc.world.getRainStrength(1.0F);
      if (!Config.isRainFancy()) {
         f /= 2.0F;
      }

      if (f != 0.0F && Config.isRainSplash()) {
         this.random.setSeed(this.rendererUpdateCount * 312987231L);
         Entity entity = this.mc.getRenderViewEntity();
         World world = this.mc.world;
         BlockPos blockpos = new BlockPos(entity);
         int i = 10;
         double d0 = 0.0;
         double d1 = 0.0;
         double d2 = 0.0;
         int j = 0;
         int k = (int)(100.0F * f * f);
         if (this.mc.gameSettings.particleSetting == 1) {
            k >>= 1;
         } else if (this.mc.gameSettings.particleSetting == 2) {
            k = 0;
         }

         for (int l = 0; l < k; l++) {
            BlockPos blockpos1 = world.getPrecipitationHeight(
               blockpos.add(this.random.nextInt(10) - this.random.nextInt(10), 0, this.random.nextInt(10) - this.random.nextInt(10))
            );
            Biome biome = world.getBiome(blockpos1);
            BlockPos blockpos2 = blockpos1.down();
            IBlockState iblockstate = world.getBlockState(blockpos2);
            if (blockpos1.getY() <= blockpos.getY() + 10
               && blockpos1.getY() >= blockpos.getY() - 10
               && biome.canRain()
               && biome.getTemperature(blockpos1) >= 0.15F) {
               double d3 = this.random.nextDouble();
               double d4 = this.random.nextDouble();
               AxisAlignedBB axisalignedbb = iblockstate.e(world, blockpos2);
               if (iblockstate.a() == Material.LAVA || iblockstate.getBlock() == Blocks.MAGMA) {
                  this.mc
                     .world
                     .spawnParticle(
                        EnumParticleTypes.SMOKE_NORMAL,
                        blockpos1.getX() + d3,
                        blockpos1.getY() + 0.1F - axisalignedbb.minY,
                        blockpos1.getZ() + d4,
                        0.0,
                        0.0,
                        0.0,
                        new int[0]
                     );
               } else if (iblockstate.a() != Material.AIR) {
                  if (this.random.nextInt(++j) == 0) {
                     d0 = blockpos2.getX() + d3;
                     d1 = blockpos2.getY() + 0.1F + axisalignedbb.maxY - 1.0;
                     d2 = blockpos2.getZ() + d4;
                  }

                  this.mc
                     .world
                     .spawnParticle(
                        EnumParticleTypes.WATER_DROP,
                        blockpos2.getX() + d3,
                        blockpos2.getY() + 0.1F + axisalignedbb.maxY,
                        blockpos2.getZ() + d4,
                        0.0,
                        0.0,
                        0.0,
                        new int[0]
                     );
               }
            }
         }

         if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
            this.rainSoundCounter = 0;
            if (d1 > blockpos.getY() + 1 && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor((float)blockpos.getY())) {
               this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
            } else {
               this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
            }
         }
      }
   }

   protected void renderRainSnow(float partialTicks) {
      if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
         WorldProvider worldProvider = this.mc.world.provider;
         Object weatherRenderer = Reflector.call(worldProvider, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);
         if (weatherRenderer != null) {
            Reflector.callVoid(weatherRenderer, Reflector.IRenderHandler_render, new Object[]{partialTicks, this.mc.world, this.mc});
            return;
         }
      }

      float f = this.mc.world.getRainStrength(partialTicks);
      if (f > 0.0F) {
         if (Config.isRainOff()) {
            return;
         }

         this.enableLightmap();
         Entity entity = this.mc.getRenderViewEntity();
         World world = this.mc.world;
         int i = MathHelper.floor(entity.posX);
         int j = MathHelper.floor(entity.posY);
         int k = MathHelper.floor(entity.posZ);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         GlStateManager.disableCull();
         GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         GlStateManager.alphaFunc(516, 0.1F);
         double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
         double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
         double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
         int l = MathHelper.floor(d1);
         int i1 = 5;
         if (Config.isRainFancy()) {
            i1 = 10;
         }

         int j1 = -1;
         float f1 = this.rendererUpdateCount + partialTicks;
         bufferbuilder.setTranslation(-d0, -d1, -d2);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

         for (int k1 = k - i1; k1 <= k + i1; k1++) {
            for (int l1 = i - i1; l1 <= i + i1; l1++) {
               int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
               double d3 = this.rainXCoords[i2] * 0.5;
               double d4 = this.rainYCoords[i2] * 0.5;
               blockpos$mutableblockpos.setPos(l1, 0, k1);
               Biome biome = world.getBiome(blockpos$mutableblockpos);
               if (biome.canRain() || biome.getEnableSnow()) {
                  int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
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

                  if (k2 != l2) {
                     this.random.setSeed(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761);
                     blockpos$mutableblockpos.setPos(l1, k2, k1);
                     float f2 = biome.getTemperature(blockpos$mutableblockpos);
                     if (world.getBiomeProvider().getTemperatureAtHeight(f2, j2) >= 0.15F) {
                        if (j1 != 0) {
                           if (j1 >= 0) {
                              tessellator.draw();
                           }

                           j1 = 0;
                           this.mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                           bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        double d5 = -((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + partialTicks)
                           / 32.0
                           * (3.0 + this.random.nextDouble());
                        double d6 = l1 + 0.5F - entity.posX;
                        double d7 = k1 + 0.5F - entity.posZ;
                        float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / i1;
                        float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                        blockpos$mutableblockpos.setPos(l1, i3, k1);
                        int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                        int k3 = j3 >> 16 & 65535;
                        int l3 = j3 & 65535;
                        bufferbuilder.pos(l1 - d3 + 0.5, l2, k1 - d4 + 0.5).tex(0.0, k2 * 0.25 + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                        bufferbuilder.pos(l1 + d3 + 0.5, l2, k1 + d4 + 0.5).tex(1.0, k2 * 0.25 + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                        bufferbuilder.pos(l1 + d3 + 0.5, k2, k1 + d4 + 0.5).tex(1.0, l2 * 0.25 + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                        bufferbuilder.pos(l1 - d3 + 0.5, k2, k1 - d4 + 0.5).tex(0.0, l2 * 0.25 + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                     } else {
                        if (j1 != 1) {
                           if (j1 >= 0) {
                              tessellator.draw();
                           }

                           j1 = 1;
                           this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                           bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        double d8 = -((this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
                        double d9 = this.random.nextDouble() + f1 * 0.01 * (float)this.random.nextGaussian();
                        double d10 = this.random.nextDouble() + f1 * (float)this.random.nextGaussian() * 0.001;
                        double d11 = l1 + 0.5F - entity.posX;
                        double d12 = k1 + 0.5F - entity.posZ;
                        float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / i1;
                        float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
                        blockpos$mutableblockpos.setPos(l1, i3, k1);
                        int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                        int j4 = i4 >> 16 & 65535;
                        int k4 = i4 & 65535;
                        bufferbuilder.pos(l1 - d3 + 0.5, l2, k1 - d4 + 0.5)
                           .tex(0.0 + d9, k2 * 0.25 + d8 + d10)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .lightmap(j4, k4)
                           .endVertex();
                        bufferbuilder.pos(l1 + d3 + 0.5, l2, k1 + d4 + 0.5)
                           .tex(1.0 + d9, k2 * 0.25 + d8 + d10)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .lightmap(j4, k4)
                           .endVertex();
                        bufferbuilder.pos(l1 + d3 + 0.5, k2, k1 + d4 + 0.5)
                           .tex(1.0 + d9, l2 * 0.25 + d8 + d10)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .lightmap(j4, k4)
                           .endVertex();
                        bufferbuilder.pos(l1 - d3 + 0.5, k2, k1 - d4 + 0.5)
                           .tex(0.0 + d9, l2 * 0.25 + d8 + d10)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .lightmap(j4, k4)
                           .endVertex();
                     }
                  }
               }
            }
         }

         if (j1 >= 0) {
            tessellator.draw();
         }

         bufferbuilder.setTranslation(0.0, 0.0, 0.0);
         GlStateManager.enableCull();
         GlStateManager.disableBlend();
         GlStateManager.alphaFunc(516, 0.1F);
         this.disableLightmap();
      }
   }

   public void setupOverlayRendering() {
      ScaledResolution scaledresolution = new ScaledResolution(this.mc);
      GlStateManager.clear(256);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 1000.0, 3000.0);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
   }

   private void updateFogColor(float partialTicks) {
      World world = this.mc.world;
      Entity entity = this.mc.getRenderViewEntity();
      float f = 0.25F + 0.75F * this.mc.gameSettings.renderDistanceChunks / 32.0F;
      f = 1.0F - (float)Math.pow(f, 0.25);
      Vec3d vec3d = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
      vec3d = CustomColors.getWorldSkyColor(vec3d, world, this.mc.getRenderViewEntity(), partialTicks);
      float f1 = (float)vec3d.x;
      float f2 = (float)vec3d.y;
      float f3 = (float)vec3d.z;
      Vec3d vec3d1 = world.getFogColor(partialTicks);
      vec3d1 = CustomColors.getWorldFogColor(vec3d1, world, this.mc.getRenderViewEntity(), partialTicks);
      this.fogColorRed = (float)vec3d1.x;
      this.fogColorGreen = (float)vec3d1.y;
      this.fogColorBlue = (float)vec3d1.z;
      if (this.mc.gameSettings.renderDistanceChunks >= 4) {
         double d0 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0F ? -1.0 : 1.0;
         Vec3d vec3d2 = new Vec3d(d0, 0.0, 0.0);
         float f5 = (float)entity.getLook(partialTicks).dotProduct(vec3d2);
         if (f5 < 0.0F) {
            f5 = 0.0F;
         }

         if (f5 > 0.0F) {
            float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
            if (afloat != null) {
               f5 *= afloat[3];
               this.fogColorRed = this.fogColorRed * (1.0F - f5) + afloat[0] * f5;
               this.fogColorGreen = this.fogColorGreen * (1.0F - f5) + afloat[1] * f5;
               this.fogColorBlue = this.fogColorBlue * (1.0F - f5) + afloat[2] * f5;
            }
         }
      }

      this.fogColorRed = this.fogColorRed + (f1 - this.fogColorRed) * f;
      this.fogColorGreen = this.fogColorGreen + (f2 - this.fogColorGreen) * f;
      this.fogColorBlue = this.fogColorBlue + (f3 - this.fogColorBlue) * f;
      float f8 = world.getRainStrength(partialTicks);
      if (f8 > 0.0F) {
         float f4 = 1.0F - f8 * 0.5F;
         float f10 = 1.0F - f8 * 0.4F;
         this.fogColorRed *= f4;
         this.fogColorGreen *= f4;
         this.fogColorBlue *= f10;
      }

      float f9 = world.getThunderStrength(partialTicks);
      if (f9 > 0.0F) {
         float f11 = 1.0F - f9 * 0.5F;
         this.fogColorRed *= f11;
         this.fogColorGreen *= f11;
         this.fogColorBlue *= f11;
      }

      IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
      if (this.cloudFog) {
         Vec3d vec3d3 = world.getCloudColour(partialTicks);
         this.fogColorRed = (float)vec3d3.x;
         this.fogColorGreen = (float)vec3d3.y;
         this.fogColorBlue = (float)vec3d3.z;
      } else if (Reflector.ForgeBlock_getFogColor.exists()) {
         Vec3d viewport = ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);
         BlockPos viewportPos = new BlockPos(viewport);
         IBlockState viewportState = this.mc.world.getBlockState(viewportPos);
         Vec3d inMaterialColor = (Vec3d)Reflector.call(
            viewportState.getBlock(),
            Reflector.ForgeBlock_getFogColor,
            new Object[]{this.mc.world, viewportPos, viewportState, entity, new Vec3d(this.fogColorRed, this.fogColorGreen, this.fogColorBlue), partialTicks}
         );
         this.fogColorRed = (float)inMaterialColor.x;
         this.fogColorGreen = (float)inMaterialColor.y;
         this.fogColorBlue = (float)inMaterialColor.z;
      } else if (iblockstate.a() == Material.WATER) {
         float f12 = 0.0F;
         if (entity instanceof EntityLivingBase) {
            f12 = EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.2F;
            f12 = Config.limit(f12, 0.0F, 0.6F);
            if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
               f12 = f12 * 0.3F + 0.6F;
            }
         }

         this.fogColorRed = 0.02F + f12;
         this.fogColorGreen = 0.02F + f12;
         this.fogColorBlue = 0.2F + f12;
      } else if (iblockstate.a() == Material.LAVA) {
         this.fogColorRed = 0.6F;
         this.fogColorGreen = 0.1F;
         this.fogColorBlue = 0.0F;
      }

      if (iblockstate.a() == Material.WATER) {
         Vec3d colUnderwater = CustomColors.getUnderwaterColor(
            this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ
         );
         if (colUnderwater != null) {
            this.fogColorRed = (float)colUnderwater.x;
            this.fogColorGreen = (float)colUnderwater.y;
            this.fogColorBlue = (float)colUnderwater.z;
         }
      } else if (iblockstate.a() == Material.LAVA) {
         Vec3d colUnderlava = CustomColors.getUnderlavaColor(
            this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ
         );
         if (colUnderlava != null) {
            this.fogColorRed = (float)colUnderlava.x;
            this.fogColorGreen = (float)colUnderlava.y;
            this.fogColorBlue = (float)colUnderlava.z;
         }
      }

      float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
      this.fogColorRed *= f13;
      this.fogColorGreen *= f13;
      this.fogColorBlue *= f13;
      double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) * world.provider.getVoidFogYFactor();
      if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
         int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
         if (i < 20) {
            d1 *= 1.0F - i / 20.0F;
         } else {
            d1 = 0.0;
         }
      }

      if (d1 < 1.0) {
         if (d1 < 0.0) {
            d1 = 0.0;
         }

         d1 *= d1;
         this.fogColorRed = (float)(this.fogColorRed * d1);
         this.fogColorGreen = (float)(this.fogColorGreen * d1);
         this.fogColorBlue = (float)(this.fogColorBlue * d1);
      }

      if (this.bossColorModifier > 0.0F) {
         float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
         this.fogColorRed = this.fogColorRed * (1.0F - f14) + this.fogColorRed * 0.7F * f14;
         this.fogColorGreen = this.fogColorGreen * (1.0F - f14) + this.fogColorGreen * 0.6F * f14;
         this.fogColorBlue = this.fogColorBlue * (1.0F - f14) + this.fogColorBlue * 0.6F * f14;
      }

      if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.NIGHT_VISION)) {
         float f15 = this.getNightVisionBrightness((EntityLivingBase)entity, partialTicks);
         float f6 = 1.0F / this.fogColorRed;
         if (f6 > 1.0F / this.fogColorGreen) {
            f6 = 1.0F / this.fogColorGreen;
         }

         if (f6 > 1.0F / this.fogColorBlue) {
            f6 = 1.0F / this.fogColorBlue;
         }

         if (Float.isInfinite(f6)) {
            f6 = Math.nextAfter(f6, 0.0);
         }

         this.fogColorRed = this.fogColorRed * (1.0F - f15) + this.fogColorRed * f6 * f15;
         this.fogColorGreen = this.fogColorGreen * (1.0F - f15) + this.fogColorGreen * f6 * f15;
         this.fogColorBlue = this.fogColorBlue * (1.0F - f15) + this.fogColorBlue * f6 * f15;
      }

      if (this.mc.gameSettings.anaglyph) {
         float f16 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
         float f17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
         float f7 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
         this.fogColorRed = f16;
         this.fogColorGreen = f17;
         this.fogColorBlue = f7;
      }

      if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
         Object event = Reflector.newInstance(
            Reflector.EntityViewRenderEvent_FogColors_Constructor,
            new Object[]{this, entity, iblockstate, partialTicks, this.fogColorRed, this.fogColorGreen, this.fogColorBlue}
         );
         Reflector.postForgeBusEvent(event);
         this.fogColorRed = Reflector.callFloat(event, Reflector.EntityViewRenderEvent_FogColors_getRed, new Object[0]);
         this.fogColorGreen = Reflector.callFloat(event, Reflector.EntityViewRenderEvent_FogColors_getGreen, new Object[0]);
         this.fogColorBlue = Reflector.callFloat(event, Reflector.EntityViewRenderEvent_FogColors_getBlue, new Object[0]);
      }

      Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
   }

   private void setupFog(int startCoords, float partialTicks) {
      this.fogStandard = false;
      Entity entity = this.mc.getRenderViewEntity();
      this.setupFogColor(false);
      GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
      float forgeFogDensity = -1.0F;
      if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
         forgeFogDensity = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, new Object[]{this, entity, iblockstate, partialTicks, 0.1F});
      }

      if (forgeFogDensity >= 0.0F) {
         GlStateManager.setFogDensity(forgeFogDensity);
      } else if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
         float f1 = 5.0F;
         int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
         if (i < 20) {
            f1 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - i / 20.0F);
         }

         GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
         if (startCoords == -1) {
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(f1 * 0.8F);
         } else {
            GlStateManager.setFogStart(f1 * 0.25F);
            GlStateManager.setFogEnd(f1);
         }

         if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
            GlStateManager.glFogi(34138, 34139);
         }
      } else if (this.cloudFog) {
         GlStateManager.setFog(GlStateManager.FogMode.EXP);
         GlStateManager.setFogDensity(0.1F);
      } else if (iblockstate.a() == Material.WATER) {
         GlStateManager.setFog(GlStateManager.FogMode.EXP);
         float waterFogDensityMax = Config.isClearWater() ? 0.02F : 0.1F;
         if (entity instanceof EntityLivingBase) {
            if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
               GlStateManager.setFogDensity(0.01F);
            } else {
               float waterFogDensity = 0.1F - EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03F;
               GlStateManager.setFogDensity(Config.limit(waterFogDensity, 0.0F, waterFogDensityMax));
            }
         } else {
            GlStateManager.setFogDensity(waterFogDensityMax);
         }
      } else if (iblockstate.a() == Material.LAVA) {
         GlStateManager.setFog(GlStateManager.FogMode.EXP);
         GlStateManager.setFogDensity(2.0F);
      } else {
         float f = this.farPlaneDistance;
         this.fogStandard = true;
         GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
         if (startCoords == -1) {
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(f);
         } else {
            GlStateManager.setFogStart(f * Config.getFogStart());
            GlStateManager.setFogEnd(f);
         }

         if (GLContext.getCapabilities().GL_NV_fog_distance) {
            if (Config.isFogFancy()) {
               GlStateManager.glFogi(34138, 34139);
            }

            if (Config.isFogFast()) {
               GlStateManager.glFogi(34138, 34140);
            }
         }

         if (this.mc.world.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog()) {
            GlStateManager.setFogStart(f * 0.05F);
            GlStateManager.setFogEnd(f);
         }

         if (Reflector.ForgeHooksClient_onFogRender.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, new Object[]{this, entity, iblockstate, partialTicks, startCoords, f});
         }
      }

      GlStateManager.enableColorMaterial();
      GlStateManager.enableFog();
      GlStateManager.colorMaterial(1028, 4608);
   }

   public void setupFogColor(boolean black) {
      if (black) {
         GlStateManager.glFog(2918, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
      } else {
         GlStateManager.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
      }
   }

   private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
      if (Config.isShaders()) {
         Shaders.setFogColor(red, green, blue);
      }

      ((Buffer)this.fogColorBuffer).clear();
      this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
      ((Buffer)this.fogColorBuffer).flip();
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
      } else if (this.mc.isIntegratedServerRunning()) {
         IntegratedServer srv = this.mc.getIntegratedServer();
         if (srv != null) {
            boolean paused = this.mc.isGamePaused();
            if (!paused && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
               if (this.serverWaitTime > 0) {
                  Lagometer.timerServer.start();
                  Config.sleep(this.serverWaitTime);
                  Lagometer.timerServer.end();
                  this.serverWaitTimeCurrent = this.serverWaitTime;
               }

               long timeNow = System.nanoTime() / 1000000L;
               if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                  long timeDiff = timeNow - this.lastServerTime;
                  if (timeDiff < 0L) {
                     this.lastServerTime = timeNow;
                     timeDiff = 0L;
                  }

                  if (timeDiff >= 50L) {
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
                        this.serverWaitTime--;
                     }

                     this.lastServerTicks = ticks;
                  }
               } else {
                  this.lastServerTime = timeNow;
                  this.lastServerTicks = srv.getTickCounter();
                  this.avgServerTickDiff = 1.0F;
                  this.avgServerTimeDiff = 50.0F;
               }
            } else {
               if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                  Config.sleep(20L);
               }

               this.lastServerTime = 0L;
               this.lastServerTicks = 0;
            }
         }
      }
   }

   private void frameInit() {
      GlErrors.frameStart();
      if (!this.initialized) {
         ReflectorResolver.resolve();
         TextureUtils.registerResourceListener();
         if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
            Config.setNotify64BitJava(true);
         }

         this.initialized = true;
      }

      Config.checkDisplayMode();
      World world = this.mc.world;
      if (world != null) {
         if (Config.getNewRelease() != null) {
            String userEdition = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
            String fullNewVer = userEdition + " " + Config.getNewRelease();
            TextComponentString msg = new TextComponentString(I18n.format("of.message.newVersion", "§n" + fullNewVer + "§r"));
            msg.setStyle(new Style().setClickEvent(new ClickEvent(Action.OPEN_URL, "https://optifine.net/downloads")));
            this.mc.ingameGUI.getChatGUI().printChatMessage(msg);
            Config.setNewRelease(null);
         }

         if (Config.isNotify64BitJava()) {
            Config.setNotify64BitJava(false);
            TextComponentString msg = new TextComponentString(I18n.format("of.message.java64Bit"));
            this.mc.ingameGUI.getChatGUI().printChatMessage(msg);
         }
      }

      if (this.mc.currentScreen instanceof GuiMainMenu) {
         this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
      }

      if (this.updatedWorld != world) {
         RandomEntities.worldChanged(this.updatedWorld, world);
         Config.updateThreadPriorities();
         this.lastServerTime = 0L;
         this.lastServerTicks = 0;
         this.updatedWorld = world;
      }

      if (!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
         Shaders.configAntialiasingLevel = 0;
      }

      if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == GuiChat.class) {
         this.mc.displayGuiScreen(new GuiChatOF((GuiChat)this.mc.currentScreen));
      }
   }

   private void frameFinish() {
      if (this.mc.world != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L)) {
         int err = GlStateManager.glGetError();
         if (err != 0 && GlErrors.isEnabled(err)) {
            String text = Config.getGlErrorString(err);
            TextComponentString msg = new TextComponentString(I18n.format("of.message.openglError", err, text));
            this.mc.ingameGUI.getChatGUI().printChatMessage(msg);
         }
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

         Reflector.setFieldValue(mainGui, Reflector.GuiMainMenu_splashText, str);
      } catch (Throwable var6) {
      }
   }

   public boolean setFxaaShader(int fxaaLevel) {
      if (!OpenGlHelper.isFramebufferEnabled()) {
         return false;
      } else if (this.shaderGroup != null && this.shaderGroup != this.fxaaShaders[2] && this.shaderGroup != this.fxaaShaders[4]) {
         return true;
      } else if (fxaaLevel != 2 && fxaaLevel != 4) {
         if (this.shaderGroup == null) {
            return true;
         } else {
            this.shaderGroup.deleteShaderGroup();
            this.shaderGroup = null;
            return true;
         }
      } else if (this.shaderGroup != null && this.shaderGroup == this.fxaaShaders[fxaaLevel]) {
         return true;
      } else if (this.mc.world == null) {
         return true;
      } else {
         this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + fxaaLevel + "x.json"));
         this.fxaaShaders[fxaaLevel] = this.shaderGroup;
         return this.useShader;
      }
   }

   private void checkLoadVisibleChunks(Entity entity, float partialTicks, ICamera icamera, boolean spectator) {
      int messageId = 201435902;
      if (this.loadVisibleChunks) {
         this.loadVisibleChunks = false;
         this.loadAllVisibleChunks(entity, partialTicks, icamera, spectator);
         this.mc.ingameGUI.getChatGUI().deleteChatLine(messageId);
      }

      if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
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
         TextComponentString msg = new TextComponentString(I18n.format("of.message.loadingVisibleChunks"));
         this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(msg, messageId);
         Reflector.Minecraft_actionKeyF3.setValue(this.mc, Boolean.TRUE);
      }
   }

   private void loadAllVisibleChunks(Entity entity, double partialTicks, ICamera icamera, boolean spectator) {
      int chunkUpdatesConfig = this.mc.gameSettings.ofChunkUpdates;
      boolean lazyChunkLoadingConfig = this.mc.gameSettings.ofLazyChunkLoading;

      try {
         this.mc.gameSettings.ofChunkUpdates = 1000;
         this.mc.gameSettings.ofLazyChunkLoading = false;
         RenderGlobal renderGlobal = Config.getRenderGlobal();
         int countLoadedChunks = renderGlobal.getCountLoadedChunks();
         long timeStart = System.currentTimeMillis();
         Config.dbg("Loading visible chunks");
         long timeLog = System.currentTimeMillis() + 5000L;
         int chunksUpdated = 0;
         boolean hasUpdates = false;

         do {
            hasUpdates = false;

            for (int i = 0; i < 100; i++) {
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
               if (!hasUpdates) {
                  break;
               }
            }

            if (renderGlobal.getCountLoadedChunks() != countLoadedChunks) {
               hasUpdates = true;
               countLoadedChunks = renderGlobal.getCountLoadedChunks();
            }

            if (System.currentTimeMillis() > timeLog) {
               Config.log("Chunks loaded: " + chunksUpdated);
               timeLog = System.currentTimeMillis() + 5000L;
            }
         } while (hasUpdates);

         Config.log("Chunks loaded: " + chunksUpdated);
         Config.log("Finished loading visible chunks");
         RenderChunk.renderChunksUpdated = 0;
      } finally {
         this.mc.gameSettings.ofChunkUpdates = chunkUpdatesConfig;
         this.mc.gameSettings.ofLazyChunkLoading = lazyChunkLoadingConfig;
      }
   }

   public static void drawNameplate(
      FontRenderer fontRendererIn,
      String str,
      float x,
      float y,
      float z,
      int verticalShift,
      float viewerYaw,
      float viewerPitch,
      boolean isThirdPersonFrontal,
      boolean isSneaking
   ) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(-0.025F, -0.025F, 0.025F);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(false);
      if (!isSneaking) {
         GlStateManager.disableDepth();
      }

      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      int i = fontRendererIn.getStringWidth(str) / 2;
      GlStateManager.disableTexture2D();
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
      bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      if (!isSneaking) {
         fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 553648127);
         GlStateManager.enableDepth();
      }

      GlStateManager.depthMask(true);
      fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }

   public void displayItemActivation(ItemStack p_190565_1_) {
      this.itemActivationItem = p_190565_1_;
      this.itemActivationTicks = 40;
      this.itemActivationOffX = this.random.nextFloat() * 2.0F - 1.0F;
      this.itemActivationOffY = this.random.nextFloat() * 2.0F - 1.0F;
   }

   private void renderItemActivation(int p_190563_1_, int p_190563_2_, float p_190563_3_) {
      if (this.itemActivationItem != null && this.itemActivationTicks > 0) {
         int i = 40 - this.itemActivationTicks;
         float f = (i + p_190563_3_) / 40.0F;
         float f1 = f * f;
         float f2 = f * f1;
         float f3 = 10.25F * f2 * f1 + -24.95F * f1 * f1 + 25.5F * f2 + -13.8F * f1 + 4.0F * f;
         float f4 = f3 * (float) Math.PI;
         float f5 = this.itemActivationOffX * (p_190563_1_ / 4);
         float f6 = this.itemActivationOffY * (p_190563_2_ / 4);
         GlStateManager.enableAlpha();
         GlStateManager.pushMatrix();
         GlStateManager.pushAttrib();
         GlStateManager.enableDepth();
         GlStateManager.disableCull();
         RenderHelper.enableStandardItemLighting();
         GlStateManager.translate(
            p_190563_1_ / 2 + f5 * MathHelper.abs(MathHelper.sin(f4 * 2.0F)), p_190563_2_ / 2 + f6 * MathHelper.abs(MathHelper.sin(f4 * 2.0F)), -50.0F
         );
         float f7 = 50.0F + 175.0F * MathHelper.sin(f4);
         GlStateManager.scale(f7, -f7, f7);
         GlStateManager.rotate(900.0F * MathHelper.abs(MathHelper.sin(f4)), 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(6.0F * MathHelper.cos(f * 8.0F), 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(6.0F * MathHelper.cos(f * 8.0F), 0.0F, 0.0F, 1.0F);
         this.mc.getRenderItem().renderItem(this.itemActivationItem, TransformType.FIXED);
         GlStateManager.popAttrib();
         GlStateManager.popMatrix();
         RenderHelper.disableStandardItemLighting();
         GlStateManager.enableCull();
         GlStateManager.disableDepth();
      }
   }
}
