package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
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
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EntitySelectors;
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
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
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
   public final ItemRenderer itemRenderer;
   private final MapItemRenderer mapItemRenderer;
   private int rendererUpdateCount;
   private Entity pointedEntity;
   private final MouseFilter mouseFilterXAxis = new MouseFilter();
   private final MouseFilter mouseFilterYAxis = new MouseFilter();
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
   private float fogColorRed;
   private float fogColorGreen;
   private float fogColorBlue;
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
   private int shaderIndex = SHADER_COUNT;
   private boolean useShader;
   private int frameCount;

   public EntityRenderer(Minecraft var1, IResourceManager var2) {
      this.mc = ☃;
      this.resourceManager = ☃;
      this.itemRenderer = ☃.getItemRenderer();
      this.mapItemRenderer = new MapItemRenderer(☃.getTextureManager());
      this.lightmapTexture = new DynamicTexture(16, 16);
      this.locationLightMap = ☃.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
      this.lightmapColors = this.lightmapTexture.getTextureData();
      this.shaderGroup = null;

      for (int ☃ = 0; ☃ < 32; ☃++) {
         for (int ☃x = 0; ☃x < 32; ☃x++) {
            float ☃xx = ☃x - 16;
            float ☃xxx = ☃ - 16;
            float ☃xxxx = MathHelper.sqrt(☃xx * ☃xx + ☃xxx * ☃xxx);
            this.rainXCoords[☃ << 5 | ☃x] = -☃xxx / ☃xxxx;
            this.rainYCoords[☃ << 5 | ☃x] = ☃xx / ☃xxxx;
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

   public void loadEntityShader(@Nullable Entity var1) {
      if (OpenGlHelper.shadersSupported) {
         if (this.shaderGroup != null) {
            this.shaderGroup.deleteShaderGroup();
         }

         this.shaderGroup = null;
         if (☃ instanceof EntityCreeper) {
            this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
         } else if (☃ instanceof EntitySpider) {
            this.loadShader(new ResourceLocation("shaders/post/spider.json"));
         } else if (☃ instanceof EntityEnderman) {
            this.loadShader(new ResourceLocation("shaders/post/invert.json"));
         }
      }
   }

   private void loadShader(ResourceLocation var1) {
      try {
         this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), ☃);
         this.shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
         this.useShader = true;
      } catch (IOException var3) {
         LOGGER.warn("Failed to load shader: {}", ☃, var3);
         this.shaderIndex = SHADER_COUNT;
         this.useShader = false;
      } catch (JsonSyntaxException var4) {
         LOGGER.warn("Failed to load shader: {}", ☃, var4);
         this.shaderIndex = SHADER_COUNT;
         this.useShader = false;
      }
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
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
         float ☃ = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float ☃x = ☃ * ☃ * ☃ * 8.0F;
         this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * ☃x);
         this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * ☃x);
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

      float ☃ = this.mc.world.getLightBrightness(new BlockPos(this.mc.getRenderViewEntity()));
      float ☃x = this.mc.gameSettings.renderDistanceChunks / 32.0F;
      float ☃xx = ☃ * (1.0F - ☃x) + ☃x;
      this.fogColor1 = this.fogColor1 + (☃xx - this.fogColor1) * 0.1F;
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

   public void updateShaderGroupSize(int var1, int var2) {
      if (OpenGlHelper.shadersSupported) {
         if (this.shaderGroup != null) {
            this.shaderGroup.createBindFramebuffers(☃, ☃);
         }

         this.mc.renderGlobal.createBindEntityOutlineFbs(☃, ☃);
      }
   }

   public void getMouseOver(float var1) {
      Entity ☃ = this.mc.getRenderViewEntity();
      if (☃ != null) {
         if (this.mc.world != null) {
            this.mc.profiler.startSection("pick");
            this.mc.pointedEntity = null;
            double ☃x = this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = ☃.rayTrace(☃x, ☃);
            Vec3d ☃xx = ☃.getPositionEyes(☃);
            boolean ☃xxx = false;
            int ☃xxxx = 3;
            double ☃xxxxx = ☃x;
            if (this.mc.playerController.extendedReach()) {
               ☃xxxxx = 6.0;
               ☃x = ☃xxxxx;
            } else {
               if (☃x > 3.0) {
                  ☃xxx = true;
               }

               ☃x = ☃x;
            }

            if (this.mc.objectMouseOver != null) {
               ☃xxxxx = this.mc.objectMouseOver.hitVec.distanceTo(☃xx);
            }

            Vec3d ☃xxxxxx = ☃.getLook(1.0F);
            Vec3d ☃xxxxxxx = ☃xx.add(☃xxxxxx.x * ☃x, ☃xxxxxx.y * ☃x, ☃xxxxxx.z * ☃x);
            this.pointedEntity = null;
            Vec3d ☃xxxxxxxx = null;
            float ☃xxxxxxxxx = 1.0F;
            List<Entity> ☃xxxxxxxxxx = this.mc
               .world
               .getEntitiesInAABBexcluding(
                  ☃,
                  ☃.getEntityBoundingBox().expand(☃xxxxxx.x * ☃x, ☃xxxxxx.y * ☃x, ☃xxxxxx.z * ☃x).grow(1.0, 1.0, 1.0),
                  Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                     public boolean apply(@Nullable Entity var1) {
                        return ☃ != null && ☃.canBeCollidedWith();
                     }
                  })
               );
            double ☃xxxxxxxxxxx = ☃xxxxx;

            for (int ☃xxxxxxxxxxxx = 0; ☃xxxxxxxxxxxx < ☃xxxxxxxxxx.size(); ☃xxxxxxxxxxxx++) {
               Entity ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx.get(☃xxxxxxxxxxxx);
               AxisAlignedBB ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.getEntityBoundingBox().grow(☃xxxxxxxxxxxxx.getCollisionBorderSize());
               RayTraceResult ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.calculateIntercept(☃xx, ☃xxxxxxx);
               if (☃xxxxxxxxxxxxxx.contains(☃xx)) {
                  if (☃xxxxxxxxxxx >= 0.0) {
                     this.pointedEntity = ☃xxxxxxxxxxxxx;
                     ☃xxxxxxxx = ☃xxxxxxxxxxxxxxx == null ? ☃xx : ☃xxxxxxxxxxxxxxx.hitVec;
                     ☃xxxxxxxxxxx = 0.0;
                  }
               } else if (☃xxxxxxxxxxxxxxx != null) {
                  double ☃xxxxxxxxxxxxxxxx = ☃xx.distanceTo(☃xxxxxxxxxxxxxxx.hitVec);
                  if (☃xxxxxxxxxxxxxxxx < ☃xxxxxxxxxxx || ☃xxxxxxxxxxx == 0.0) {
                     if (☃xxxxxxxxxxxxx.getLowestRidingEntity() == ☃.getLowestRidingEntity()) {
                        if (☃xxxxxxxxxxx == 0.0) {
                           this.pointedEntity = ☃xxxxxxxxxxxxx;
                           ☃xxxxxxxx = ☃xxxxxxxxxxxxxxx.hitVec;
                        }
                     } else {
                        this.pointedEntity = ☃xxxxxxxxxxxxx;
                        ☃xxxxxxxx = ☃xxxxxxxxxxxxxxx.hitVec;
                        ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
                     }
                  }
               }
            }

            if (this.pointedEntity != null && ☃xxx && ☃xx.distanceTo(☃xxxxxxxx) > 3.0) {
               this.pointedEntity = null;
               this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, ☃xxxxxxxx, null, new BlockPos(☃xxxxxxxx));
            }

            if (this.pointedEntity != null && (☃xxxxxxxxxxx < ☃xxxxx || this.mc.objectMouseOver == null)) {
               this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, ☃xxxxxxxx);
               if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                  this.mc.pointedEntity = this.pointedEntity;
               }
            }

            this.mc.profiler.endSection();
         }
      }
   }

   private void updateFovModifierHand() {
      float ☃ = 1.0F;
      if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
         AbstractClientPlayer ☃x = (AbstractClientPlayer)this.mc.getRenderViewEntity();
         ☃ = ☃x.getFovModifier();
      }

      this.fovModifierHandPrev = this.fovModifierHand;
      this.fovModifierHand = this.fovModifierHand + (☃ - this.fovModifierHand) * 0.5F;
      if (this.fovModifierHand > 1.5F) {
         this.fovModifierHand = 1.5F;
      }

      if (this.fovModifierHand < 0.1F) {
         this.fovModifierHand = 0.1F;
      }
   }

   private float getFOVModifier(float var1, boolean var2) {
      if (this.debugView) {
         return 90.0F;
      } else {
         Entity ☃ = this.mc.getRenderViewEntity();
         float ☃x = 70.0F;
         if (☃) {
            ☃x = this.mc.gameSettings.fovSetting;
            ☃x *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * ☃;
         }

         if (☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).getHealth() <= 0.0F) {
            float ☃xx = ((EntityLivingBase)☃).deathTime + ☃;
            ☃x /= (1.0F - 500.0F / (☃xx + 500.0F)) * 2.0F + 1.0F;
         }

         IBlockState ☃xx = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, ☃, ☃);
         if (☃xx.getMaterial() == Material.WATER) {
            ☃x = ☃x * 60.0F / 70.0F;
         }

         return ☃x;
      }
   }

   private void hurtCameraEffect(float var1) {
      if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
         EntityLivingBase ☃ = (EntityLivingBase)this.mc.getRenderViewEntity();
         float ☃x = ☃.hurtTime - ☃;
         if (☃.getHealth() <= 0.0F) {
            float ☃xx = ☃.deathTime + ☃;
            GlStateManager.rotate(40.0F - 8000.0F / (☃xx + 200.0F), 0.0F, 0.0F, 1.0F);
         }

         if (☃x < 0.0F) {
            return;
         }

         ☃x /= ☃.maxHurtTime;
         ☃x = MathHelper.sin(☃x * ☃x * ☃x * ☃x * (float) Math.PI);
         float ☃xx = ☃.attackedAtYaw;
         GlStateManager.rotate(-☃xx, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-☃x * 14.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(☃xx, 0.0F, 1.0F, 0.0F);
      }
   }

   private void applyBobbing(float var1) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer ☃ = (EntityPlayer)this.mc.getRenderViewEntity();
         float ☃x = ☃.distanceWalkedModified - ☃.prevDistanceWalkedModified;
         float ☃xx = -(☃.distanceWalkedModified + ☃x * ☃);
         float ☃xxx = ☃.prevCameraYaw + (☃.cameraYaw - ☃.prevCameraYaw) * ☃;
         float ☃xxxx = ☃.prevCameraPitch + (☃.cameraPitch - ☃.prevCameraPitch) * ☃;
         GlStateManager.translate(MathHelper.sin(☃xx * (float) Math.PI) * ☃xxx * 0.5F, -Math.abs(MathHelper.cos(☃xx * (float) Math.PI) * ☃xxx), 0.0F);
         GlStateManager.rotate(MathHelper.sin(☃xx * (float) Math.PI) * ☃xxx * 3.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(Math.abs(MathHelper.cos(☃xx * (float) Math.PI - 0.2F) * ☃xxx) * 5.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(☃xxxx, 1.0F, 0.0F, 0.0F);
      }
   }

   private void orientCamera(float var1) {
      Entity ☃ = this.mc.getRenderViewEntity();
      float ☃x = ☃.getEyeHeight();
      double ☃xx = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃;
      double ☃xxx = ☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃ + ☃x;
      double ☃xxxx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃;
      if (☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).isPlayerSleeping()) {
         ☃x = (float)(☃x + 1.0);
         GlStateManager.translate(0.0F, 0.3F, 0.0F);
         if (!this.mc.gameSettings.debugCamEnable) {
            BlockPos ☃xxxxx = new BlockPos(☃);
            IBlockState ☃xxxxxx = this.mc.world.getBlockState(☃xxxxx);
            Block ☃xxxxxxx = ☃xxxxxx.getBlock();
            if (☃xxxxxxx == Blocks.BED) {
               int ☃xxxxxxxx = ☃xxxxxx.getValue(BlockBed.FACING).getHorizontalIndex();
               GlStateManager.rotate(☃xxxxxxxx * 90, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃ + 180.0F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃, -1.0F, 0.0F, 0.0F);
         }
      } else if (this.mc.gameSettings.thirdPersonView > 0) {
         double ☃xxxxx = this.thirdPersonDistancePrev + (4.0F - this.thirdPersonDistancePrev) * ☃;
         if (this.mc.gameSettings.debugCamEnable) {
            GlStateManager.translate(0.0F, 0.0F, (float)(-☃xxxxx));
         } else {
            float ☃xxxxxx = ☃.rotationYaw;
            float ☃xxxxxxx = ☃.rotationPitch;
            if (this.mc.gameSettings.thirdPersonView == 2) {
               ☃xxxxxxx += 180.0F;
            }

            double ☃xxxxxxxx = -MathHelper.sin(☃xxxxxx * (float) (Math.PI / 180.0)) * MathHelper.cos(☃xxxxxxx * (float) (Math.PI / 180.0)) * ☃xxxxx;
            double ☃xxxxxxxxx = MathHelper.cos(☃xxxxxx * (float) (Math.PI / 180.0)) * MathHelper.cos(☃xxxxxxx * (float) (Math.PI / 180.0)) * ☃xxxxx;
            double ☃xxxxxxxxxx = -MathHelper.sin(☃xxxxxxx * (float) (Math.PI / 180.0)) * ☃xxxxx;

            for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < 8; ☃xxxxxxxxxxx++) {
               float ☃xxxxxxxxxxxx = (☃xxxxxxxxxxx & 1) * 2 - 1;
               float ☃xxxxxxxxxxxxx = (☃xxxxxxxxxxx >> 1 & 1) * 2 - 1;
               float ☃xxxxxxxxxxxxxx = (☃xxxxxxxxxxx >> 2 & 1) * 2 - 1;
               ☃xxxxxxxxxxxx *= 0.1F;
               ☃xxxxxxxxxxxxx *= 0.1F;
               ☃xxxxxxxxxxxxxx *= 0.1F;
               RayTraceResult ☃xxxxxxxxxxxxxxx = this.mc
                  .world
                  .rayTraceBlocks(
                     new Vec3d(☃xx + ☃xxxxxxxxxxxx, ☃xxx + ☃xxxxxxxxxxxxx, ☃xxxx + ☃xxxxxxxxxxxxxx),
                     new Vec3d(☃xx - ☃xxxxxxxx + ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxx, ☃xxx - ☃xxxxxxxxxx + ☃xxxxxxxxxxxxx, ☃xxxx - ☃xxxxxxxxx + ☃xxxxxxxxxxxxxx)
                  );
               if (☃xxxxxxxxxxxxxxx != null) {
                  double ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.hitVec.distanceTo(new Vec3d(☃xx, ☃xxx, ☃xxxx));
                  if (☃xxxxxxxxxxxxxxxx < ☃xxxxx) {
                     ☃xxxxx = ☃xxxxxxxxxxxxxxxx;
                  }
               }
            }

            if (this.mc.gameSettings.thirdPersonView == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(☃.rotationPitch - ☃xxxxxxx, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(☃.rotationYaw - ☃xxxxxx, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, (float)(-☃xxxxx));
            GlStateManager.rotate(☃xxxxxx - ☃.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(☃xxxxxxx - ☃.rotationPitch, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GlStateManager.translate(0.0F, 0.0F, 0.05F);
      }

      if (!this.mc.gameSettings.debugCamEnable) {
         GlStateManager.rotate(☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃, 1.0F, 0.0F, 0.0F);
         if (☃ instanceof EntityAnimal) {
            EntityAnimal ☃xxxxx = (EntityAnimal)☃;
            GlStateManager.rotate(☃xxxxx.prevRotationYawHead + (☃xxxxx.rotationYawHead - ☃xxxxx.prevRotationYawHead) * ☃ + 180.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GlStateManager.rotate(☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃ + 180.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      GlStateManager.translate(0.0F, -☃x, 0.0F);
      ☃xx = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃;
      ☃xxx = ☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃ + ☃x;
      ☃xxxx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃;
      this.cloudFog = this.mc.renderGlobal.hasCloudFog(☃xx, ☃xxx, ☃xxxx, ☃);
   }

   private void setupCameraTransform(float var1, int var2) {
      this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      float ☃ = 0.07F;
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate(-(☃ * 2 - 1) * 0.07F, 0.0F, 0.0F);
      }

      if (this.cameraZoom != 1.0) {
         GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
         GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0);
      }

      Project.gluPerspective(
         this.getFOVModifier(☃, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2
      );
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate((☃ * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      this.hurtCameraEffect(☃);
      if (this.mc.gameSettings.viewBobbing) {
         this.applyBobbing(☃);
      }

      float ☃x = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * ☃;
      if (☃x > 0.0F) {
         int ☃xx = 20;
         if (this.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            ☃xx = 7;
         }

         float ☃xxx = 5.0F / (☃x * ☃x + 5.0F) - ☃x * 0.04F;
         ☃xxx *= ☃xxx;
         GlStateManager.rotate((this.rendererUpdateCount + ☃) * ☃xx, 0.0F, 1.0F, 1.0F);
         GlStateManager.scale(1.0F / ☃xxx, 1.0F, 1.0F);
         GlStateManager.rotate(-(this.rendererUpdateCount + ☃) * ☃xx, 0.0F, 1.0F, 1.0F);
      }

      this.orientCamera(☃);
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

   private void renderHand(float var1, int var2) {
      if (!this.debugView) {
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         float ☃ = 0.07F;
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate(-(☃ * 2 - 1) * 0.07F, 0.0F, 0.0F);
         }

         Project.gluPerspective(this.getFOVModifier(☃, false), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((☃ * 2 - 1) * 0.1F, 0.0F, 0.0F);
         }

         GlStateManager.pushMatrix();
         this.hurtCameraEffect(☃);
         if (this.mc.gameSettings.viewBobbing) {
            this.applyBobbing(☃);
         }

         boolean ☃x = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
         if (this.mc.gameSettings.thirdPersonView == 0 && !☃x && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
            this.enableLightmap();
            this.itemRenderer.renderItemInFirstPerson(☃);
            this.disableLightmap();
         }

         GlStateManager.popMatrix();
         if (this.mc.gameSettings.thirdPersonView == 0 && !☃x) {
            this.itemRenderer.renderOverlays(☃);
            this.hurtCameraEffect(☃);
         }

         if (this.mc.gameSettings.viewBobbing) {
            this.applyBobbing(☃);
         }
      }
   }

   public void disableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   public void enableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      float ☃ = 0.00390625F;
      GlStateManager.scale(0.00390625F, 0.00390625F, 0.00390625F);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.matrixMode(5888);
      this.mc.getTextureManager().bindTexture(this.locationLightMap);
      GlStateManager.glTexParameteri(3553, 10241, 9729);
      GlStateManager.glTexParameteri(3553, 10240, 9729);
      GlStateManager.glTexParameteri(3553, 10242, 10496);
      GlStateManager.glTexParameteri(3553, 10243, 10496);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   private void updateTorchFlicker() {
      this.torchFlickerDX = (float)(this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
      this.torchFlickerDX = (float)(this.torchFlickerDX * 0.9);
      this.torchFlickerX = this.torchFlickerX + (this.torchFlickerDX - this.torchFlickerX);
      this.lightmapUpdateNeeded = true;
   }

   private void updateLightmap(float var1) {
      if (this.lightmapUpdateNeeded) {
         this.mc.profiler.startSection("lightTex");
         World ☃ = this.mc.world;
         if (☃ != null) {
            float ☃x = ☃.getSunBrightness(1.0F);
            float ☃xx = ☃x * 0.95F + 0.05F;

            for (int ☃xxx = 0; ☃xxx < 256; ☃xxx++) {
               float ☃xxxx = ☃.provider.getLightBrightnessTable()[☃xxx / 16] * ☃xx;
               float ☃xxxxx = ☃.provider.getLightBrightnessTable()[☃xxx % 16] * (this.torchFlickerX * 0.1F + 1.5F);
               if (☃.getLastLightningBolt() > 0) {
                  ☃xxxx = ☃.provider.getLightBrightnessTable()[☃xxx / 16];
               }

               float ☃xxxxxx = ☃xxxx * (☃x * 0.65F + 0.35F);
               float ☃xxxxxxx = ☃xxxx * (☃x * 0.65F + 0.35F);
               float ☃xxxxxxxx = ☃xxxxx * ((☃xxxxx * 0.6F + 0.4F) * 0.6F + 0.4F);
               float ☃xxxxxxxxx = ☃xxxxx * (☃xxxxx * ☃xxxxx * 0.6F + 0.4F);
               float ☃xxxxxxxxxx = ☃xxxxxx + ☃xxxxx;
               float ☃xxxxxxxxxxx = ☃xxxxxxx + ☃xxxxxxxx;
               float ☃xxxxxxxxxxxx = ☃xxxx + ☃xxxxxxxxx;
               ☃xxxxxxxxxx = ☃xxxxxxxxxx * 0.96F + 0.03F;
               ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * 0.96F + 0.03F;
               ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * 0.96F + 0.03F;
               if (this.bossColorModifier > 0.0F) {
                  float ☃xxxxxxxxxxxxx = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * ☃;
                  ☃xxxxxxxxxx = ☃xxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxx) + ☃xxxxxxxxxx * 0.7F * ☃xxxxxxxxxxxxx;
                  ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxx) + ☃xxxxxxxxxxx * 0.6F * ☃xxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxx) + ☃xxxxxxxxxxxx * 0.6F * ☃xxxxxxxxxxxxx;
               }

               if (☃.provider.getDimensionType().getId() == 1) {
                  ☃xxxxxxxxxx = 0.22F + ☃xxxxx * 0.75F;
                  ☃xxxxxxxxxxx = 0.28F + ☃xxxxxxxx * 0.75F;
                  ☃xxxxxxxxxxxx = 0.25F + ☃xxxxxxxxx * 0.75F;
               }

               if (this.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                  float ☃xxxxxxxxxxxxx = this.getNightVisionBrightness(this.mc.player, ☃);
                  float ☃xxxxxxxxxxxxxx = 1.0F / ☃xxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxx > 1.0F / ☃xxxxxxxxxxx) {
                     ☃xxxxxxxxxxxxxx = 1.0F / ☃xxxxxxxxxxx;
                  }

                  if (☃xxxxxxxxxxxxxx > 1.0F / ☃xxxxxxxxxxxx) {
                     ☃xxxxxxxxxxxxxx = 1.0F / ☃xxxxxxxxxxxx;
                  }

                  ☃xxxxxxxxxx = ☃xxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxx) + ☃xxxxxxxxxx * ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxx;
                  ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxx) + ☃xxxxxxxxxxx * ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxx) + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxx;
               }

               if (☃xxxxxxxxxx > 1.0F) {
                  ☃xxxxxxxxxx = 1.0F;
               }

               if (☃xxxxxxxxxxx > 1.0F) {
                  ☃xxxxxxxxxxx = 1.0F;
               }

               if (☃xxxxxxxxxxxx > 1.0F) {
                  ☃xxxxxxxxxxxx = 1.0F;
               }

               float ☃xxxxxxxxxxxxxxx = this.mc.gameSettings.gammaSetting;
               float ☃xxxxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxx;
               float ☃xxxxxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxx;
               float ☃xxxxxxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxx = ☃xxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxxx) + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxxx) + ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxxx) + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
               ☃xxxxxxxxxx = ☃xxxxxxxxxx * 0.96F + 0.03F;
               ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * 0.96F + 0.03F;
               ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxx * 0.96F + 0.03F;
               if (☃xxxxxxxxxx > 1.0F) {
                  ☃xxxxxxxxxx = 1.0F;
               }

               if (☃xxxxxxxxxxx > 1.0F) {
                  ☃xxxxxxxxxxx = 1.0F;
               }

               if (☃xxxxxxxxxxxx > 1.0F) {
                  ☃xxxxxxxxxxxx = 1.0F;
               }

               if (☃xxxxxxxxxx < 0.0F) {
                  ☃xxxxxxxxxx = 0.0F;
               }

               if (☃xxxxxxxxxxx < 0.0F) {
                  ☃xxxxxxxxxxx = 0.0F;
               }

               if (☃xxxxxxxxxxxx < 0.0F) {
                  ☃xxxxxxxxxxxx = 0.0F;
               }

               int ☃xxxxxxxxxxxxxxxxxxx = 255;
               int ☃xxxxxxxxxxxxxxxxxxxx = (int)(☃xxxxxxxxxx * 255.0F);
               int ☃xxxxxxxxxxxxxxxxxxxxx = (int)(☃xxxxxxxxxxx * 255.0F);
               int ☃xxxxxxxxxxxxxxxxxxxxxx = (int)(☃xxxxxxxxxxxx * 255.0F);
               this.lightmapColors[☃xxx] = 0xFF000000 | ☃xxxxxxxxxxxxxxxxxxxx << 16 | ☃xxxxxxxxxxxxxxxxxxxxx << 8 | ☃xxxxxxxxxxxxxxxxxxxxxx;
            }

            this.lightmapTexture.updateDynamicTexture();
            this.lightmapUpdateNeeded = false;
            this.mc.profiler.endSection();
         }
      }
   }

   private float getNightVisionBrightness(EntityLivingBase var1, float var2) {
      int ☃ = ☃.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
      return ☃ > 200 ? 1.0F : 0.7F + MathHelper.sin((☃ - ☃) * (float) Math.PI * 0.2F) * 0.3F;
   }

   public void updateCameraAndRender(float var1, long var2) {
      boolean ☃ = Display.isActive();
      if (!☃ && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
         if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
            this.mc.displayInGameMenu();
         }
      } else {
         this.prevFrameTime = Minecraft.getSystemTime();
      }

      this.mc.profiler.startSection("mouse");
      if (☃ && Minecraft.IS_RUNNING_ON_MAC && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
         Mouse.setGrabbed(false);
         Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2 - 20);
         Mouse.setGrabbed(true);
      }

      if (this.mc.inGameHasFocus && ☃) {
         this.mc.mouseHelper.mouseXYChange();
         this.mc.getTutorial().handleMouse(this.mc.mouseHelper);
         float ☃x = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float ☃xx = ☃x * ☃x * ☃x * 8.0F;
         float ☃xxx = this.mc.mouseHelper.deltaX * ☃xx;
         float ☃xxxx = this.mc.mouseHelper.deltaY * ☃xx;
         int ☃xxxxx = 1;
         if (this.mc.gameSettings.invertMouse) {
            ☃xxxxx = -1;
         }

         if (this.mc.gameSettings.smoothCamera) {
            this.smoothCamYaw += ☃xxx;
            this.smoothCamPitch += ☃xxxx;
            float ☃xxxxxx = ☃ - this.smoothCamPartialTicks;
            this.smoothCamPartialTicks = ☃;
            ☃xxx = this.smoothCamFilterX * ☃xxxxxx;
            ☃xxxx = this.smoothCamFilterY * ☃xxxxxx;
            this.mc.player.turn(☃xxx, ☃xxxx * ☃xxxxx);
         } else {
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
            this.mc.player.turn(☃xxx, ☃xxxx * ☃xxxxx);
         }
      }

      this.mc.profiler.endSection();
      if (!this.mc.skipRenderWorld) {
         anaglyphEnable = this.mc.gameSettings.anaglyph;
         final ScaledResolution ☃xxxxxx = new ScaledResolution(this.mc);
         int ☃xxxxxxx = ☃xxxxxx.getScaledWidth();
         int ☃xxxxxxxx = ☃xxxxxx.getScaledHeight();
         final int ☃xxxxxxxxx = Mouse.getX() * ☃xxxxxxx / this.mc.displayWidth;
         final int ☃xxxxxxxxxx = ☃xxxxxxxx - Mouse.getY() * ☃xxxxxxxx / this.mc.displayHeight - 1;
         int ☃xxxxxxxxxxx = this.mc.gameSettings.limitFramerate;
         if (this.mc.world != null) {
            this.mc.profiler.startSection("level");
            int ☃xxxxxxxxxxxx = Math.min(Minecraft.getDebugFPS(), ☃xxxxxxxxxxx);
            ☃xxxxxxxxxxxx = Math.max(☃xxxxxxxxxxxx, 60);
            long ☃xxxxxxxxxxxxx = System.nanoTime() - ☃;
            long ☃xxxxxxxxxxxxxx = Math.max(1000000000 / ☃xxxxxxxxxxxx / 4 - ☃xxxxxxxxxxxxx, 0L);
            this.renderWorld(☃, System.nanoTime() + ☃xxxxxxxxxxxxxx);
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
                  this.shaderGroup.render(☃);
                  GlStateManager.popMatrix();
               }

               this.mc.getFramebuffer().bindFramebuffer(true);
            }

            this.renderEndNanoTime = System.nanoTime();
            this.mc.profiler.endStartSection("gui");
            if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
               GlStateManager.alphaFunc(516, 0.1F);
               this.setupOverlayRendering();
               this.renderItemActivation(☃xxxxxxx, ☃xxxxxxxx, ☃);
               this.mc.ingameGUI.renderGameOverlay(☃);
            }

            this.mc.profiler.endSection();
         } else {
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.setupOverlayRendering();
            this.renderEndNanoTime = System.nanoTime();
         }

         if (this.mc.currentScreen != null) {
            GlStateManager.clear(256);

            try {
               this.mc.currentScreen.drawScreen(☃xxxxxxxxx, ☃xxxxxxxxxx, this.mc.getTickLength());
            } catch (Throwable var16) {
               CrashReport ☃xxxxxxxxxxxxxxx = CrashReport.makeCrashReport(var16, "Rendering screen");
               CrashReportCategory ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.makeCategory("Screen render details");
               ☃xxxxxxxxxxxxxxxx.addDetail("Screen name", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                  }
               });
               ☃xxxxxxxxxxxxxxxx.addDetail("Mouse location", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", ☃, ☃, Mouse.getX(), Mouse.getY());
                  }
               });
               ☃xxxxxxxxxxxxxxxx.addDetail(
                  "Screen size",
                  new ICrashReportDetail<String>() {
                     public String call() throws Exception {
                        return String.format(
                           "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",
                           ☃.getScaledWidth(),
                           ☃.getScaledHeight(),
                           EntityRenderer.this.mc.displayWidth,
                           EntityRenderer.this.mc.displayHeight,
                           ☃.getScaleFactor()
                        );
                     }
                  }
               );
               throw new ReportedException(☃xxxxxxxxxxxxxxx);
            }
         }
      }
   }

   private void createWorldIcon() {
      if (this.mc.renderGlobal.getRenderedChunks() > 10 && this.mc.renderGlobal.hasNoChunkUpdates() && !this.mc.getIntegratedServer().isWorldIconSet()) {
         BufferedImage ☃ = ScreenShotHelper.createScreenshot(this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer());
         int ☃x = ☃.getWidth();
         int ☃xx = ☃.getHeight();
         int ☃xxx = 0;
         int ☃xxxx = 0;
         if (☃x > ☃xx) {
            ☃xxx = (☃x - ☃xx) / 2;
            ☃x = ☃xx;
         } else {
            ☃xxxx = (☃xx - ☃x) / 2;
         }

         try {
            BufferedImage ☃xxxxx = new BufferedImage(64, 64, 1);
            Graphics ☃xxxxxx = ☃xxxxx.createGraphics();
            ☃xxxxxx.drawImage(☃, 0, 0, 64, 64, ☃xxx, ☃xxxx, ☃xxx + ☃x, ☃xxxx + ☃x, null);
            ☃xxxxxx.dispose();
            ImageIO.write(☃xxxxx, "png", this.mc.getIntegratedServer().getWorldIconFile());
         } catch (IOException var8) {
            LOGGER.warn("Couldn't save auto screenshot", var8);
         }
      }
   }

   public void renderStreamIndicator(float var1) {
      this.setupOverlayRendering();
   }

   private boolean isDrawBlockOutline() {
      if (!this.drawBlockOutline) {
         return false;
      } else {
         Entity ☃ = this.mc.getRenderViewEntity();
         boolean ☃x = ☃ instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
         if (☃x && !((EntityPlayer)☃).capabilities.allowEdit) {
            ItemStack ☃xx = ((EntityPlayer)☃).getHeldItemMainhand();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
               BlockPos ☃xxx = this.mc.objectMouseOver.getBlockPos();
               Block ☃xxxx = this.mc.world.getBlockState(☃xxx).getBlock();
               if (this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
                  ☃x = ☃xxxx.hasTileEntity() && this.mc.world.getTileEntity(☃xxx) instanceof IInventory;
               } else {
                  ☃x = !☃xx.isEmpty() && (☃xx.canDestroy(☃xxxx) || ☃xx.canPlaceOn(☃xxxx));
               }
            }
         }

         return ☃x;
      }
   }

   public void renderWorld(float var1, long var2) {
      this.updateLightmap(☃);
      if (this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(this.mc.player);
      }

      this.getMouseOver(☃);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.5F);
      this.mc.profiler.startSection("center");
      if (this.mc.gameSettings.anaglyph) {
         anaglyphField = 0;
         GlStateManager.colorMask(false, true, true, false);
         this.renderWorldPass(0, ☃, ☃);
         anaglyphField = 1;
         GlStateManager.colorMask(true, false, false, false);
         this.renderWorldPass(1, ☃, ☃);
         GlStateManager.colorMask(true, true, true, false);
      } else {
         this.renderWorldPass(2, ☃, ☃);
      }

      this.mc.profiler.endSection();
   }

   private void renderWorldPass(int var1, float var2, long var3) {
      RenderGlobal ☃ = this.mc.renderGlobal;
      ParticleManager ☃x = this.mc.effectRenderer;
      boolean ☃xx = this.isDrawBlockOutline();
      GlStateManager.enableCull();
      this.mc.profiler.endStartSection("clear");
      GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      this.updateFogColor(☃);
      GlStateManager.clear(16640);
      this.mc.profiler.endStartSection("camera");
      this.setupCameraTransform(☃, ☃);
      ActiveRenderInfo.updateRenderInfo(this.mc.player, this.mc.gameSettings.thirdPersonView == 2);
      this.mc.profiler.endStartSection("frustum");
      ClippingHelperImpl.getInstance();
      this.mc.profiler.endStartSection("culling");
      ICamera ☃xxx = new Frustum();
      Entity ☃xxxx = this.mc.getRenderViewEntity();
      double ☃xxxxx = ☃xxxx.lastTickPosX + (☃xxxx.posX - ☃xxxx.lastTickPosX) * ☃;
      double ☃xxxxxx = ☃xxxx.lastTickPosY + (☃xxxx.posY - ☃xxxx.lastTickPosY) * ☃;
      double ☃xxxxxxx = ☃xxxx.lastTickPosZ + (☃xxxx.posZ - ☃xxxx.lastTickPosZ) * ☃;
      ☃xxx.setPosition(☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
      if (this.mc.gameSettings.renderDistanceChunks >= 4) {
         this.setupFog(-1, ☃);
         this.mc.profiler.endStartSection("sky");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(☃, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
         GlStateManager.matrixMode(5888);
         ☃.renderSky(☃, ☃);
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(
            this.getFOVModifier(☃, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2
         );
         GlStateManager.matrixMode(5888);
      }

      this.setupFog(0, ☃);
      GlStateManager.shadeModel(7425);
      if (☃xxxx.posY + ☃xxxx.getEyeHeight() < 128.0) {
         this.renderCloudsCheck(☃, ☃, ☃, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
      }

      this.mc.profiler.endStartSection("prepareterrain");
      this.setupFog(0, ☃);
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      RenderHelper.disableStandardItemLighting();
      this.mc.profiler.endStartSection("terrain_setup");
      ☃.setupTerrain(☃xxxx, ☃, ☃xxx, this.frameCount++, this.mc.player.isSpectator());
      if (☃ == 0 || ☃ == 2) {
         this.mc.profiler.endStartSection("updatechunks");
         this.mc.renderGlobal.updateChunks(☃);
      }

      this.mc.profiler.endStartSection("terrain");
      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      GlStateManager.disableAlpha();
      ☃.renderBlockLayer(BlockRenderLayer.SOLID, ☃, ☃, ☃xxxx);
      GlStateManager.enableAlpha();
      ☃.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, ☃, ☃, ☃xxxx);
      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
      ☃.renderBlockLayer(BlockRenderLayer.CUTOUT, ☃, ☃, ☃xxxx);
      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
      GlStateManager.shadeModel(7424);
      GlStateManager.alphaFunc(516, 0.1F);
      if (!this.debugView) {
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         RenderHelper.enableStandardItemLighting();
         this.mc.profiler.endStartSection("entities");
         ☃.renderEntities(☃xxxx, ☃xxx, ☃);
         RenderHelper.disableStandardItemLighting();
         this.disableLightmap();
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      if (☃xx && this.mc.objectMouseOver != null && !☃xxxx.isInsideOfMaterial(Material.WATER)) {
         EntityPlayer ☃xxxxxxxx = (EntityPlayer)☃xxxx;
         GlStateManager.disableAlpha();
         this.mc.profiler.endStartSection("outline");
         ☃.drawSelectionBox(☃xxxxxxxx, this.mc.objectMouseOver, 0, ☃);
         GlStateManager.enableAlpha();
      }

      if (this.mc.debugRenderer.shouldRender()) {
         this.mc.debugRenderer.renderDebug(☃, ☃);
      }

      this.mc.profiler.endStartSection("destroyProgress");
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
      ☃.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), ☃xxxx, ☃);
      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
      GlStateManager.disableBlend();
      if (!this.debugView) {
         this.enableLightmap();
         this.mc.profiler.endStartSection("litParticles");
         ☃x.renderLitParticles(☃xxxx, ☃);
         RenderHelper.disableStandardItemLighting();
         this.setupFog(0, ☃);
         this.mc.profiler.endStartSection("particles");
         ☃x.renderParticles(☃xxxx, ☃);
         this.disableLightmap();
      }

      GlStateManager.depthMask(false);
      GlStateManager.enableCull();
      this.mc.profiler.endStartSection("weather");
      this.renderRainSnow(☃);
      GlStateManager.depthMask(true);
      ☃.renderWorldBorder(☃xxxx, ☃);
      GlStateManager.disableBlend();
      GlStateManager.enableCull();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.alphaFunc(516, 0.1F);
      this.setupFog(0, ☃);
      GlStateManager.enableBlend();
      GlStateManager.depthMask(false);
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      GlStateManager.shadeModel(7425);
      this.mc.profiler.endStartSection("translucent");
      ☃.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, ☃, ☃, ☃xxxx);
      GlStateManager.shadeModel(7424);
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.disableFog();
      if (☃xxxx.posY + ☃xxxx.getEyeHeight() >= 128.0) {
         this.mc.profiler.endStartSection("aboveClouds");
         this.renderCloudsCheck(☃, ☃, ☃, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
      }

      this.mc.profiler.endStartSection("hand");
      if (this.renderHand) {
         GlStateManager.clear(256);
         this.renderHand(☃, ☃);
      }
   }

   private void renderCloudsCheck(RenderGlobal var1, float var2, int var3, double var4, double var6, double var8) {
      if (this.mc.gameSettings.shouldRenderClouds() != 0) {
         this.mc.profiler.endStartSection("clouds");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(☃, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         this.setupFog(0, ☃);
         ☃.renderClouds(☃, ☃, ☃, ☃, ☃);
         GlStateManager.disableFog();
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(
            this.getFOVModifier(☃, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2
         );
         GlStateManager.matrixMode(5888);
      }
   }

   private void addRainParticles() {
      float ☃ = this.mc.world.getRainStrength(1.0F);
      if (!this.mc.gameSettings.fancyGraphics) {
         ☃ /= 2.0F;
      }

      if (☃ != 0.0F) {
         this.random.setSeed(this.rendererUpdateCount * 312987231L);
         Entity ☃x = this.mc.getRenderViewEntity();
         World ☃xx = this.mc.world;
         BlockPos ☃xxx = new BlockPos(☃x);
         int ☃xxxx = 10;
         double ☃xxxxx = 0.0;
         double ☃xxxxxx = 0.0;
         double ☃xxxxxxx = 0.0;
         int ☃xxxxxxxx = 0;
         int ☃xxxxxxxxx = (int)(100.0F * ☃ * ☃);
         if (this.mc.gameSettings.particleSetting == 1) {
            ☃xxxxxxxxx >>= 1;
         } else if (this.mc.gameSettings.particleSetting == 2) {
            ☃xxxxxxxxx = 0;
         }

         for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < ☃xxxxxxxxx; ☃xxxxxxxxxx++) {
            BlockPos ☃xxxxxxxxxxx = ☃xx.getPrecipitationHeight(
               ☃xxx.add(this.random.nextInt(10) - this.random.nextInt(10), 0, this.random.nextInt(10) - this.random.nextInt(10))
            );
            Biome ☃xxxxxxxxxxxx = ☃xx.getBiome(☃xxxxxxxxxxx);
            BlockPos ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx.down();
            IBlockState ☃xxxxxxxxxxxxxx = ☃xx.getBlockState(☃xxxxxxxxxxxxx);
            if (☃xxxxxxxxxxx.getY() <= ☃xxx.getY() + 10
               && ☃xxxxxxxxxxx.getY() >= ☃xxx.getY() - 10
               && ☃xxxxxxxxxxxx.canRain()
               && ☃xxxxxxxxxxxx.getTemperature(☃xxxxxxxxxxx) >= 0.15F) {
               double ☃xxxxxxxxxxxxxxx = this.random.nextDouble();
               double ☃xxxxxxxxxxxxxxxx = this.random.nextDouble();
               AxisAlignedBB ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.getBoundingBox(☃xx, ☃xxxxxxxxxxxxx);
               if (☃xxxxxxxxxxxxxx.getMaterial() == Material.LAVA || ☃xxxxxxxxxxxxxx.getBlock() == Blocks.MAGMA) {
                  this.mc
                     .world
                     .spawnParticle(
                        EnumParticleTypes.SMOKE_NORMAL,
                        ☃xxxxxxxxxxx.getX() + ☃xxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxx.getY() + 0.1F - ☃xxxxxxxxxxxxxxxxx.minY,
                        ☃xxxxxxxxxxx.getZ() + ☃xxxxxxxxxxxxxxxx,
                        0.0,
                        0.0,
                        0.0,
                        new int[0]
                     );
               } else if (☃xxxxxxxxxxxxxx.getMaterial() != Material.AIR) {
                  if (this.random.nextInt(++☃xxxxxxxx) == 0) {
                     ☃xxxxx = ☃xxxxxxxxxxxxx.getX() + ☃xxxxxxxxxxxxxxx;
                     ☃xxxxxx = ☃xxxxxxxxxxxxx.getY() + 0.1F + ☃xxxxxxxxxxxxxxxxx.maxY - 1.0;
                     ☃xxxxxxx = ☃xxxxxxxxxxxxx.getZ() + ☃xxxxxxxxxxxxxxxx;
                  }

                  this.mc
                     .world
                     .spawnParticle(
                        EnumParticleTypes.WATER_DROP,
                        ☃xxxxxxxxxxxxx.getX() + ☃xxxxxxxxxxxxxxx,
                        ☃xxxxxxxxxxxxx.getY() + 0.1F + ☃xxxxxxxxxxxxxxxxx.maxY,
                        ☃xxxxxxxxxxxxx.getZ() + ☃xxxxxxxxxxxxxxxx,
                        0.0,
                        0.0,
                        0.0,
                        new int[0]
                     );
               }
            }
         }

         if (☃xxxxxxxx > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
            this.rainSoundCounter = 0;
            if (☃xxxxxx > ☃xxx.getY() + 1 && ☃xx.getPrecipitationHeight(☃xxx).getY() > MathHelper.floor((float)☃xxx.getY())) {
               this.mc.world.playSound(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
            } else {
               this.mc.world.playSound(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
            }
         }
      }
   }

   protected void renderRainSnow(float var1) {
      float ☃ = this.mc.world.getRainStrength(☃);
      if (!(☃ <= 0.0F)) {
         this.enableLightmap();
         Entity ☃x = this.mc.getRenderViewEntity();
         World ☃xx = this.mc.world;
         int ☃xxx = MathHelper.floor(☃x.posX);
         int ☃xxxx = MathHelper.floor(☃x.posY);
         int ☃xxxxx = MathHelper.floor(☃x.posZ);
         Tessellator ☃xxxxxx = Tessellator.getInstance();
         BufferBuilder ☃xxxxxxx = ☃xxxxxx.getBuffer();
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
         double ☃xxxxxxxx = ☃x.lastTickPosX + (☃x.posX - ☃x.lastTickPosX) * ☃;
         double ☃xxxxxxxxx = ☃x.lastTickPosY + (☃x.posY - ☃x.lastTickPosY) * ☃;
         double ☃xxxxxxxxxx = ☃x.lastTickPosZ + (☃x.posZ - ☃x.lastTickPosZ) * ☃;
         int ☃xxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxx);
         int ☃xxxxxxxxxxxx = 5;
         if (this.mc.gameSettings.fancyGraphics) {
            ☃xxxxxxxxxxxx = 10;
         }

         int ☃xxxxxxxxxxxxx = -1;
         float ☃xxxxxxxxxxxxxx = this.rendererUpdateCount + ☃;
         ☃xxxxxxx.setTranslation(-☃xxxxxxxx, -☃xxxxxxxxx, -☃xxxxxxxxxx);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         BlockPos.MutableBlockPos ☃xxxxxxxxxxxxxxx = new BlockPos.MutableBlockPos();

         for (int ☃xxxxxxxxxxxxxxxx = ☃xxxxx - ☃xxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxx <= ☃xxxxx + ☃xxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxxxxxx = ☃xxx - ☃xxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxx <= ☃xxx + ☃xxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxx - ☃xxxxx + 16) * 32 + ☃xxxxxxxxxxxxxxxxx - ☃xxx + 16;
               double ☃xxxxxxxxxxxxxxxxxxx = this.rainXCoords[☃xxxxxxxxxxxxxxxxxx] * 0.5;
               double ☃xxxxxxxxxxxxxxxxxxxx = this.rainYCoords[☃xxxxxxxxxxxxxxxxxx] * 0.5;
               ☃xxxxxxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxx, 0, ☃xxxxxxxxxxxxxxxx);
               Biome ☃xxxxxxxxxxxxxxxxxxxxx = ☃xx.getBiome(☃xxxxxxxxxxxxxxx);
               if (☃xxxxxxxxxxxxxxxxxxxxx.canRain() || ☃xxxxxxxxxxxxxxxxxxxxx.getEnableSnow()) {
                  int ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xx.getPrecipitationHeight(☃xxxxxxxxxxxxxxx).getY();
                  int ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxx - ☃xxxxxxxxxxxx;
                  int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxx + ☃xxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxx) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx;
                  }

                  if (☃xxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxx) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx;
                  }

                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxx) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
                  }

                  if (☃xxxxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxxxxxxxxx) {
                     this.random
                        .setSeed(
                           ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx * 3121 + ☃xxxxxxxxxxxxxxxxx * 45238971
                              ^ ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx * 418711 + ☃xxxxxxxxxxxxxxxx * 13761
                        );
                     ☃xxxxxxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
                     float ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx.getTemperature(☃xxxxxxxxxxxxxxx);
                     if (☃xx.getBiomeProvider().getTemperatureAtHeight(☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx) >= 0.15F) {
                        if (☃xxxxxxxxxxxxx != 0) {
                           if (☃xxxxxxxxxxxxx >= 0) {
                              ☃xxxxxx.draw();
                           }

                           ☃xxxxxxxxxxxxx = 0;
                           this.mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                           ☃xxxxxxx.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = -(
                              (double)(
                                    this.rendererUpdateCount
                                          + ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx * 3121
                                          + ☃xxxxxxxxxxxxxxxxx * 45238971
                                          + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx * 418711
                                          + ☃xxxxxxxxxxxxxxxx * 13761
                                       & 31
                                 )
                                 + ☃
                           )
                           / 32.0
                           * (3.0 + this.random.nextDouble());
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx + 0.5F - ☃x.posX;
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx + 0.5F - ☃x.posZ;
                        float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.sqrt(
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                           )
                           / ☃xxxxxxxxxxxx;
                        float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ((1.0F - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.5F + 0.5F) * ☃;
                        ☃xxxxxxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xx.getCombinedLight(☃xxxxxxxxxxxxxxx, 0);
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx >> 16 & 65535;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx & 65535;
                        ☃xxxxxxx.pos(
                              ☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxx + 0.5
                           )
                           .tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                        ☃xxxxxxx.pos(
                              ☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx + 0.5
                           )
                           .tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                        ☃xxxxxxx.pos(☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx + 0.5)
                           .tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                        ☃xxxxxxx.pos(☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxx + 0.5)
                           .tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                     } else {
                        if (☃xxxxxxxxxxxxx != 1) {
                           if (☃xxxxxxxxxxxxx >= 0) {
                              ☃xxxxxx.draw();
                           }

                           ☃xxxxxxxxxxxxx = 1;
                           this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                           ☃xxxxxxx.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = -((this.rendererUpdateCount & 511) + ☃) / 512.0F;
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.random.nextDouble() + ☃xxxxxxxxxxxxxx * 0.01 * (float)this.random.nextGaussian();
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.random.nextDouble() + ☃xxxxxxxxxxxxxx * (float)this.random.nextGaussian() * 0.001;
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx + 0.5F - ☃x.posX;
                        double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx + 0.5F - ☃x.posZ;
                        float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.sqrt(
                              ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                           )
                           / ☃xxxxxxxxxxxx;
                        float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (
                              (1.0F - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) * 0.3F + 0.5F
                           )
                           * ☃;
                        ☃xxxxxxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = (☃xx.getCombinedLight(☃xxxxxxxxxxxxxxx, 0) * 3 + 15728880) / 4;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx >> 16 & 65535;
                        int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx & 65535;
                        ☃xxxxxxx.pos(
                              ☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxx + 0.5
                           )
                           .tex(
                              0.0 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                           )
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                        ☃xxxxxxx.pos(
                              ☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx + 0.5
                           )
                           .tex(
                              1.0 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                           )
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                        ☃xxxxxxx.pos(☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx + 0.5)
                           .tex(
                              1.0 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                           )
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                        ☃xxxxxxx.pos(☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx + 0.5, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxx + 0.5)
                           .tex(
                              0.0 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                              ☃xxxxxxxxxxxxxxxxxxxxxxxx * 0.25 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                           )
                           .color(1.0F, 1.0F, 1.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .lightmap(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                           .endVertex();
                     }
                  }
               }
            }
         }

         if (☃xxxxxxxxxxxxx >= 0) {
            ☃xxxxxx.draw();
         }

         ☃xxxxxxx.setTranslation(0.0, 0.0, 0.0);
         GlStateManager.enableCull();
         GlStateManager.disableBlend();
         GlStateManager.alphaFunc(516, 0.1F);
         this.disableLightmap();
      }
   }

   public void setupOverlayRendering() {
      ScaledResolution ☃ = new ScaledResolution(this.mc);
      GlStateManager.clear(256);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0, ☃.getScaledWidth_double(), ☃.getScaledHeight_double(), 0.0, 1000.0, 3000.0);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
   }

   private void updateFogColor(float var1) {
      World ☃ = this.mc.world;
      Entity ☃x = this.mc.getRenderViewEntity();
      float ☃xx = 0.25F + 0.75F * this.mc.gameSettings.renderDistanceChunks / 32.0F;
      ☃xx = 1.0F - (float)Math.pow(☃xx, 0.25);
      Vec3d ☃xxx = ☃.getSkyColor(this.mc.getRenderViewEntity(), ☃);
      float ☃xxxx = (float)☃xxx.x;
      float ☃xxxxx = (float)☃xxx.y;
      float ☃xxxxxx = (float)☃xxx.z;
      Vec3d ☃xxxxxxx = ☃.getFogColor(☃);
      this.fogColorRed = (float)☃xxxxxxx.x;
      this.fogColorGreen = (float)☃xxxxxxx.y;
      this.fogColorBlue = (float)☃xxxxxxx.z;
      if (this.mc.gameSettings.renderDistanceChunks >= 4) {
         double ☃xxxxxxxx = MathHelper.sin(☃.getCelestialAngleRadians(☃)) > 0.0F ? -1.0 : 1.0;
         Vec3d ☃xxxxxxxxx = new Vec3d(☃xxxxxxxx, 0.0, 0.0);
         float ☃xxxxxxxxxx = (float)☃x.getLook(☃).dotProduct(☃xxxxxxxxx);
         if (☃xxxxxxxxxx < 0.0F) {
            ☃xxxxxxxxxx = 0.0F;
         }

         if (☃xxxxxxxxxx > 0.0F) {
            float[] ☃xxxxxxxxxxx = ☃.provider.calcSunriseSunsetColors(☃.getCelestialAngle(☃), ☃);
            if (☃xxxxxxxxxxx != null) {
               ☃xxxxxxxxxx *= ☃xxxxxxxxxxx[3];
               this.fogColorRed = this.fogColorRed * (1.0F - ☃xxxxxxxxxx) + ☃xxxxxxxxxxx[0] * ☃xxxxxxxxxx;
               this.fogColorGreen = this.fogColorGreen * (1.0F - ☃xxxxxxxxxx) + ☃xxxxxxxxxxx[1] * ☃xxxxxxxxxx;
               this.fogColorBlue = this.fogColorBlue * (1.0F - ☃xxxxxxxxxx) + ☃xxxxxxxxxxx[2] * ☃xxxxxxxxxx;
            }
         }
      }

      this.fogColorRed = this.fogColorRed + (☃xxxx - this.fogColorRed) * ☃xx;
      this.fogColorGreen = this.fogColorGreen + (☃xxxxx - this.fogColorGreen) * ☃xx;
      this.fogColorBlue = this.fogColorBlue + (☃xxxxxx - this.fogColorBlue) * ☃xx;
      float ☃xxxxxxxxxxx = ☃.getRainStrength(☃);
      if (☃xxxxxxxxxxx > 0.0F) {
         float ☃xxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxx * 0.5F;
         float ☃xxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxx * 0.4F;
         this.fogColorRed *= ☃xxxxxxxxxxxx;
         this.fogColorGreen *= ☃xxxxxxxxxxxx;
         this.fogColorBlue *= ☃xxxxxxxxxxxxx;
      }

      float ☃xxxxxxxxxxxx = ☃.getThunderStrength(☃);
      if (☃xxxxxxxxxxxx > 0.0F) {
         float ☃xxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxxx * 0.5F;
         this.fogColorRed *= ☃xxxxxxxxxxxxx;
         this.fogColorGreen *= ☃xxxxxxxxxxxxx;
         this.fogColorBlue *= ☃xxxxxxxxxxxxx;
      }

      IBlockState ☃xxxxxxxxxxxxx = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, ☃x, ☃);
      if (this.cloudFog) {
         Vec3d ☃xxxxxxxxxxxxxx = ☃.getCloudColour(☃);
         this.fogColorRed = (float)☃xxxxxxxxxxxxxx.x;
         this.fogColorGreen = (float)☃xxxxxxxxxxxxxx.y;
         this.fogColorBlue = (float)☃xxxxxxxxxxxxxx.z;
      } else if (☃xxxxxxxxxxxxx.getMaterial() == Material.WATER) {
         float ☃xxxxxxxxxxxxxx = 0.0F;
         if (☃x instanceof EntityLivingBase) {
            ☃xxxxxxxxxxxxxx = EnchantmentHelper.getRespirationModifier((EntityLivingBase)☃x) * 0.2F;
            if (((EntityLivingBase)☃x).isPotionActive(MobEffects.WATER_BREATHING)) {
               ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx * 0.3F + 0.6F;
            }
         }

         this.fogColorRed = 0.02F + ☃xxxxxxxxxxxxxx;
         this.fogColorGreen = 0.02F + ☃xxxxxxxxxxxxxx;
         this.fogColorBlue = 0.2F + ☃xxxxxxxxxxxxxx;
      } else if (☃xxxxxxxxxxxxx.getMaterial() == Material.LAVA) {
         this.fogColorRed = 0.6F;
         this.fogColorGreen = 0.1F;
         this.fogColorBlue = 0.0F;
      }

      float ☃xxxxxxxxxxxxxx = this.fogColor2 + (this.fogColor1 - this.fogColor2) * ☃;
      this.fogColorRed *= ☃xxxxxxxxxxxxxx;
      this.fogColorGreen *= ☃xxxxxxxxxxxxxx;
      this.fogColorBlue *= ☃xxxxxxxxxxxxxx;
      double ☃xxxxxxxxxxxxxxx = (☃x.lastTickPosY + (☃x.posY - ☃x.lastTickPosY) * ☃) * ☃.provider.getVoidFogYFactor();
      if (☃x instanceof EntityLivingBase && ((EntityLivingBase)☃x).isPotionActive(MobEffects.BLINDNESS)) {
         int ☃xxxxxxxxxxxxxxxx = ((EntityLivingBase)☃x).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
         if (☃xxxxxxxxxxxxxxxx < 20) {
            ☃xxxxxxxxxxxxxxx *= 1.0F - ☃xxxxxxxxxxxxxxxx / 20.0F;
         } else {
            ☃xxxxxxxxxxxxxxx = 0.0;
         }
      }

      if (☃xxxxxxxxxxxxxxx < 1.0) {
         if (☃xxxxxxxxxxxxxxx < 0.0) {
            ☃xxxxxxxxxxxxxxx = 0.0;
         }

         ☃xxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxxx;
         this.fogColorRed = (float)(this.fogColorRed * ☃xxxxxxxxxxxxxxx);
         this.fogColorGreen = (float)(this.fogColorGreen * ☃xxxxxxxxxxxxxxx);
         this.fogColorBlue = (float)(this.fogColorBlue * ☃xxxxxxxxxxxxxxx);
      }

      if (this.bossColorModifier > 0.0F) {
         float ☃xxxxxxxxxxxxxxxx = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * ☃;
         this.fogColorRed = this.fogColorRed * (1.0F - ☃xxxxxxxxxxxxxxxx) + this.fogColorRed * 0.7F * ☃xxxxxxxxxxxxxxxx;
         this.fogColorGreen = this.fogColorGreen * (1.0F - ☃xxxxxxxxxxxxxxxx) + this.fogColorGreen * 0.6F * ☃xxxxxxxxxxxxxxxx;
         this.fogColorBlue = this.fogColorBlue * (1.0F - ☃xxxxxxxxxxxxxxxx) + this.fogColorBlue * 0.6F * ☃xxxxxxxxxxxxxxxx;
      }

      if (☃x instanceof EntityLivingBase && ((EntityLivingBase)☃x).isPotionActive(MobEffects.NIGHT_VISION)) {
         float ☃xxxxxxxxxxxxxxxx = this.getNightVisionBrightness((EntityLivingBase)☃x, ☃);
         float ☃xxxxxxxxxxxxxxxxx = 1.0F / this.fogColorRed;
         if (☃xxxxxxxxxxxxxxxxx > 1.0F / this.fogColorGreen) {
            ☃xxxxxxxxxxxxxxxxx = 1.0F / this.fogColorGreen;
         }

         if (☃xxxxxxxxxxxxxxxxx > 1.0F / this.fogColorBlue) {
            ☃xxxxxxxxxxxxxxxxx = 1.0F / this.fogColorBlue;
         }

         this.fogColorRed = this.fogColorRed * (1.0F - ☃xxxxxxxxxxxxxxxx) + this.fogColorRed * ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
         this.fogColorGreen = this.fogColorGreen * (1.0F - ☃xxxxxxxxxxxxxxxx) + this.fogColorGreen * ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
         this.fogColorBlue = this.fogColorBlue * (1.0F - ☃xxxxxxxxxxxxxxxx) + this.fogColorBlue * ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
      }

      if (this.mc.gameSettings.anaglyph) {
         float ☃xxxxxxxxxxxxxxxxxx = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
         float ☃xxxxxxxxxxxxxxxxxxx = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
         float ☃xxxxxxxxxxxxxxxxxxxx = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
         this.fogColorRed = ☃xxxxxxxxxxxxxxxxxx;
         this.fogColorGreen = ☃xxxxxxxxxxxxxxxxxxx;
         this.fogColorBlue = ☃xxxxxxxxxxxxxxxxxxxx;
      }

      GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
   }

   private void setupFog(int var1, float var2) {
      Entity ☃ = this.mc.getRenderViewEntity();
      this.setupFogColor(false);
      GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      IBlockState ☃x = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, ☃, ☃);
      if (☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).isPotionActive(MobEffects.BLINDNESS)) {
         float ☃xx = 5.0F;
         int ☃xxx = ((EntityLivingBase)☃).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
         if (☃xxx < 20) {
            ☃xx = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - ☃xxx / 20.0F);
         }

         GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
         if (☃ == -1) {
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(☃xx * 0.8F);
         } else {
            GlStateManager.setFogStart(☃xx * 0.25F);
            GlStateManager.setFogEnd(☃xx);
         }

         if (GLContext.getCapabilities().GL_NV_fog_distance) {
            GlStateManager.glFogi(34138, 34139);
         }
      } else if (this.cloudFog) {
         GlStateManager.setFog(GlStateManager.FogMode.EXP);
         GlStateManager.setFogDensity(0.1F);
      } else if (☃x.getMaterial() == Material.WATER) {
         GlStateManager.setFog(GlStateManager.FogMode.EXP);
         if (☃ instanceof EntityLivingBase) {
            if (((EntityLivingBase)☃).isPotionActive(MobEffects.WATER_BREATHING)) {
               GlStateManager.setFogDensity(0.01F);
            } else {
               GlStateManager.setFogDensity(0.1F - EnchantmentHelper.getRespirationModifier((EntityLivingBase)☃) * 0.03F);
            }
         } else {
            GlStateManager.setFogDensity(0.1F);
         }
      } else if (☃x.getMaterial() == Material.LAVA) {
         GlStateManager.setFog(GlStateManager.FogMode.EXP);
         GlStateManager.setFogDensity(2.0F);
      } else {
         float ☃xxxx = this.farPlaneDistance;
         GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
         if (☃ == -1) {
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(☃xxxx);
         } else {
            GlStateManager.setFogStart(☃xxxx * 0.75F);
            GlStateManager.setFogEnd(☃xxxx);
         }

         if (GLContext.getCapabilities().GL_NV_fog_distance) {
            GlStateManager.glFogi(34138, 34139);
         }

         if (this.mc.world.provider.doesXZShowFog((int)☃.posX, (int)☃.posZ) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog()) {
            GlStateManager.setFogStart(☃xxxx * 0.05F);
            GlStateManager.setFogEnd(Math.min(☃xxxx, 192.0F) * 0.5F);
         }
      }

      GlStateManager.enableColorMaterial();
      GlStateManager.enableFog();
      GlStateManager.colorMaterial(1028, 4608);
   }

   public void setupFogColor(boolean var1) {
      if (☃) {
         GlStateManager.glFog(2918, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
      } else {
         GlStateManager.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
      }
   }

   private FloatBuffer setFogColorBuffer(float var1, float var2, float var3, float var4) {
      ((Buffer)this.fogColorBuffer).clear();
      this.fogColorBuffer.put(☃).put(☃).put(☃).put(☃);
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

   public static void drawNameplate(
      FontRenderer var0, String var1, float var2, float var3, float var4, int var5, float var6, float var7, boolean var8, boolean var9
   ) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(☃, ☃, ☃);
      GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-☃, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate((☃ ? -1 : 1) * ☃, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(-0.025F, -0.025F, 0.025F);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(false);
      if (!☃) {
         GlStateManager.disableDepth();
      }

      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      int ☃ = ☃.getStringWidth(☃) / 2;
      GlStateManager.disableTexture2D();
      Tessellator ☃x = Tessellator.getInstance();
      BufferBuilder ☃xx = ☃x.getBuffer();
      ☃xx.begin(7, DefaultVertexFormats.POSITION_COLOR);
      ☃xx.pos(-☃ - 1, -1 + ☃, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      ☃xx.pos(-☃ - 1, 8 + ☃, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      ☃xx.pos(☃ + 1, 8 + ☃, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      ☃xx.pos(☃ + 1, -1 + ☃, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
      ☃x.draw();
      GlStateManager.enableTexture2D();
      if (!☃) {
         ☃.drawString(☃, -☃.getStringWidth(☃) / 2, ☃, 553648127);
         GlStateManager.enableDepth();
      }

      GlStateManager.depthMask(true);
      ☃.drawString(☃, -☃.getStringWidth(☃) / 2, ☃, ☃ ? 553648127 : -1);
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }

   public void displayItemActivation(ItemStack var1) {
      this.itemActivationItem = ☃;
      this.itemActivationTicks = 40;
      this.itemActivationOffX = this.random.nextFloat() * 2.0F - 1.0F;
      this.itemActivationOffY = this.random.nextFloat() * 2.0F - 1.0F;
   }

   private void renderItemActivation(int var1, int var2, float var3) {
      if (this.itemActivationItem != null && this.itemActivationTicks > 0) {
         int ☃ = 40 - this.itemActivationTicks;
         float ☃x = (☃ + ☃) / 40.0F;
         float ☃xx = ☃x * ☃x;
         float ☃xxx = ☃x * ☃xx;
         float ☃xxxx = 10.25F * ☃xxx * ☃xx + -24.95F * ☃xx * ☃xx + 25.5F * ☃xxx + -13.8F * ☃xx + 4.0F * ☃x;
         float ☃xxxxx = ☃xxxx * (float) Math.PI;
         float ☃xxxxxx = this.itemActivationOffX * (☃ / 4);
         float ☃xxxxxxx = this.itemActivationOffY * (☃ / 4);
         GlStateManager.enableAlpha();
         GlStateManager.pushMatrix();
         GlStateManager.pushAttrib();
         GlStateManager.enableDepth();
         GlStateManager.disableCull();
         RenderHelper.enableStandardItemLighting();
         GlStateManager.translate(
            ☃ / 2 + ☃xxxxxx * MathHelper.abs(MathHelper.sin(☃xxxxx * 2.0F)), ☃ / 2 + ☃xxxxxxx * MathHelper.abs(MathHelper.sin(☃xxxxx * 2.0F)), -50.0F
         );
         float ☃xxxxxxxx = 50.0F + 175.0F * MathHelper.sin(☃xxxxx);
         GlStateManager.scale(☃xxxxxxxx, -☃xxxxxxxx, ☃xxxxxxxx);
         GlStateManager.rotate(900.0F * MathHelper.abs(MathHelper.sin(☃xxxxx)), 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(6.0F * MathHelper.cos(☃x * 8.0F), 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(6.0F * MathHelper.cos(☃x * 8.0F), 0.0F, 0.0F, 1.0F);
         this.mc.getRenderItem().renderItem(this.itemActivationItem, ItemCameraTransforms.TransformType.FIXED);
         GlStateManager.popAttrib();
         GlStateManager.popMatrix();
         RenderHelper.disableStandardItemLighting();
         GlStateManager.enableCull();
         GlStateManager.disableDepth();
      }
   }
}
