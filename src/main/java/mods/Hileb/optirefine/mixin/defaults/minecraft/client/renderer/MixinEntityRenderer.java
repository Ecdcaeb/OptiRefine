package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

public class MixinEntityRenderer {
}
/*
--- net/minecraft/client/renderer/EntityRenderer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/EntityRenderer.java	Tue Aug 19 14:59:58 2025
@@ -2,38 +2,51 @@

 import com.google.common.base.Predicate;
 import com.google.common.base.Predicates;
 import com.google.gson.JsonSyntaxException;
 import java.awt.Graphics2D;
 import java.awt.image.BufferedImage;
+import java.awt.image.ImageObserver;
 import java.io.IOException;
 import java.nio.Buffer;
 import java.nio.FloatBuffer;
+import java.util.Calendar;
+import java.util.Date;
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
+import net.minecraft.client.gui.GuiChat;
+import net.minecraft.client.gui.GuiDownloadTerrain;
+import net.minecraft.client.gui.GuiMainMenu;
 import net.minecraft.client.gui.MapItemRenderer;
 import net.minecraft.client.gui.ScaledResolution;
+import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
 import net.minecraft.client.multiplayer.WorldClient;
 import net.minecraft.client.particle.ParticleManager;
-import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
+import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
+import net.minecraft.client.renderer.chunk.RenderChunk;
+import net.minecraft.client.renderer.culling.ClippingHelper;
 import net.minecraft.client.renderer.culling.ClippingHelperImpl;
 import net.minecraft.client.renderer.culling.Frustum;
+import net.minecraft.client.renderer.culling.ICamera;
 import net.minecraft.client.renderer.texture.DynamicTexture;
 import net.minecraft.client.renderer.texture.TextureMap;
+import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
+import net.minecraft.client.resources.I18n;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.client.resources.IResourceManagerReloadListener;
+import net.minecraft.client.settings.GameSettings;
 import net.minecraft.client.shader.ShaderGroup;
 import net.minecraft.client.shader.ShaderLinkHelper;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.CrashReportCategory;
 import net.minecraft.crash.ICrashReportDetail;
 import net.minecraft.enchantment.EnchantmentHelper;
@@ -47,31 +60,56 @@
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.MobEffects;
 import net.minecraft.init.SoundEvents;
 import net.minecraft.inventory.IInventory;
 import net.minecraft.item.ItemStack;
+import net.minecraft.server.integrated.IntegratedServer;
 import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EntitySelectors;
+import net.minecraft.util.EnumFacing;
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
+import net.minecraft.util.math.BlockPos.MutableBlockPos;
+import net.minecraft.util.math.RayTraceResult.Type;
+import net.minecraft.util.text.Style;
+import net.minecraft.util.text.TextComponentString;
+import net.minecraft.util.text.event.ClickEvent;
+import net.minecraft.util.text.event.ClickEvent.Action;
 import net.minecraft.world.GameType;
+import net.minecraft.world.World;
+import net.minecraft.world.WorldProvider;
 import net.minecraft.world.biome.Biome;
+import net.optifine.CustomColors;
+import net.optifine.GlErrors;
+import net.optifine.Lagometer;
+import net.optifine.RandomEntities;
+import net.optifine.gui.GuiChatOF;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.reflect.ReflectorResolver;
+import net.optifine.shaders.Shaders;
+import net.optifine.shaders.ShadersRender;
+import net.optifine.util.MemoryMonitor;
+import net.optifine.util.TextureUtils;
+import net.optifine.util.TimedEvent;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
+import org.lwjgl.input.Keyboard;
 import org.lwjgl.input.Mouse;
 import org.lwjgl.opengl.Display;
+import org.lwjgl.opengl.GL11;
 import org.lwjgl.opengl.GLContext;
 import org.lwjgl.util.glu.Project;

 public class EntityRenderer implements IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
@@ -79,18 +117,18 @@
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    private final Minecraft mc;
    private final IResourceManager resourceManager;
    private final Random random = new Random();
    private float farPlaneDistance;
-   public final ItemRenderer itemRenderer;
+   public ItemRenderer itemRenderer;
    private final MapItemRenderer mapItemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
-   private final MouseFilter mouseFilterXAxis = new MouseFilter();
-   private final MouseFilter mouseFilterYAxis = new MouseFilter();
+   private MouseFilter mouseFilterXAxis = new MouseFilter();
+   private MouseFilter mouseFilterYAxis = new MouseFilter();
    private final float thirdPersonDistance = 4.0F;
    private float thirdPersonDistancePrev = 4.0F;
    private float smoothCamYaw;
    private float smoothCamPitch;
    private float smoothCamFilterX;
    private float smoothCamFilterY;
@@ -112,15 +150,15 @@
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];
    private final FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
-   private float fogColorRed;
-   private float fogColorGreen;
-   private float fogColorBlue;
+   public float fogColorRed;
+   public float fogColorGreen;
+   public float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection;
    private boolean debugView;
    private double cameraZoom = 1.0;
    private double cameraYaw;
@@ -154,17 +192,30 @@
       new ResourceLocation("shaders/post/blobs.json"),
       new ResourceLocation("shaders/post/antialias.json"),
       new ResourceLocation("shaders/post/creeper.json"),
       new ResourceLocation("shaders/post/spider.json")
    };
    public static final int SHADER_COUNT = SHADERS_TEXTURES.length;
-   private int shaderIndex = SHADER_COUNT;
+   private int shaderIndex;
    private boolean useShader;
-   private int frameCount;
+   public int frameCount;
+   private boolean initialized = false;
+   private World updatedWorld = null;
+   public boolean fogStandard = false;
+   private float clipDistance = 128.0F;
+   private long lastServerTime = 0L;
+   private int lastServerTicks = 0;
+   private int serverWaitTime = 0;
+   private int serverWaitTimeCurrent = 0;
+   private float avgServerTimeDiff = 0.0F;
+   private float avgServerTickDiff = 0.0F;
+   private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
+   private boolean loadVisibleChunks = false;

    public EntityRenderer(Minecraft var1, IResourceManager var2) {
+      this.shaderIndex = SHADER_COUNT;
       this.mc = var1;
       this.resourceManager = var2;
       this.itemRenderer = var1.getItemRenderer();
       this.mapItemRenderer = new MapItemRenderer(var1.getTextureManager());
       this.lightmapTexture = new DynamicTexture(16, 16);
       this.locationLightMap = var1.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
@@ -209,29 +260,33 @@
          if (var1 instanceof EntityCreeper) {
             this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
          } else if (var1 instanceof EntitySpider) {
             this.loadShader(new ResourceLocation("shaders/post/spider.json"));
          } else if (var1 instanceof EntityEnderman) {
             this.loadShader(new ResourceLocation("shaders/post/invert.json"));
+         } else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
+            Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, new Object[]{var1, this});
          }
       }
    }

    private void loadShader(ResourceLocation var1) {
-      try {
-         this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), var1);
-         this.shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
-         this.useShader = true;
-      } catch (IOException var3) {
-         LOGGER.warn("Failed to load shader: {}", var1, var3);
-         this.shaderIndex = SHADER_COUNT;
-         this.useShader = false;
-      } catch (JsonSyntaxException var4) {
-         LOGGER.warn("Failed to load shader: {}", var1, var4);
-         this.shaderIndex = SHADER_COUNT;
-         this.useShader = false;
+      if (OpenGlHelper.isFramebufferEnabled()) {
+         try {
+            this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), var1);
+            this.shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
+            this.useShader = true;
+         } catch (IOException var3) {
+            LOGGER.warn("Failed to load shader: {}", var1, var3);
+            this.shaderIndex = SHADER_COUNT;
+            this.useShader = false;
+         } catch (JsonSyntaxException var4) {
+            LOGGER.warn("Failed to load shader: {}", var1, var4);
+            this.shaderIndex = SHADER_COUNT;
+            this.useShader = false;
+         }
       }
    }

    public void onResourceManagerReload(IResourceManager var1) {
       if (this.shaderGroup != null) {
          this.shaderGroup.deleteShaderGroup();
@@ -270,16 +325,21 @@
       }

       if (this.mc.getRenderViewEntity() == null) {
          this.mc.setRenderViewEntity(this.mc.player);
       }

-      float var4 = this.mc.world.getLightBrightness(new BlockPos(this.mc.getRenderViewEntity()));
-      float var5 = this.mc.gameSettings.renderDistanceChunks / 32.0F;
-      float var3 = var4 * (1.0F - var5) + var5;
-      this.fogColor1 = this.fogColor1 + (var3 - this.fogColor1) * 0.1F;
+      Entity var11 = this.mc.getRenderViewEntity();
+      double var12 = var11.posX;
+      double var4 = var11.posY + var11.getEyeHeight();
+      double var6 = var11.posZ;
+      float var8 = this.mc.world.getLightBrightness(new BlockPos(var12, var4, var6));
+      float var9 = this.mc.gameSettings.renderDistanceChunks / 16.0F;
+      var9 = MathHelper.clamp(var9, 0.0F, 1.0F);
+      float var10 = var8 * (1.0F - var9) + var9;
+      this.fogColor1 = this.fogColor1 + (var10 - this.fogColor1) * 0.1F;
       this.rendererUpdateCount++;
       this.itemRenderer.updateEquippedItem();
       this.addRainParticles();
       this.bossColorModifierPrev = this.bossColorModifier;
       if (this.mc.ingameGUI.getBossOverlay().shouldDarkenSky()) {
          this.bossColorModifier += 0.05F;
@@ -311,96 +371,93 @@
          this.mc.renderGlobal.createBindEntityOutlineFbs(var1, var2);
       }
    }

    public void getMouseOver(float var1) {
       Entity var2 = this.mc.getRenderViewEntity();
-      if (var2 != null) {
-         if (this.mc.world != null) {
-            this.mc.profiler.startSection("pick");
-            this.mc.pointedEntity = null;
-            double var3 = this.mc.playerController.getBlockReachDistance();
-            this.mc.objectMouseOver = var2.rayTrace(var3, var1);
-            Vec3d var5 = var2.getPositionEyes(var1);
-            boolean var6 = false;
-            byte var7 = 3;
-            double var8 = var3;
-            if (this.mc.playerController.extendedReach()) {
-               var8 = 6.0;
-               var3 = var8;
-            } else {
-               if (var3 > 3.0) {
-                  var6 = true;
-               }
-
-               var3 = var3;
-            }
-
-            if (this.mc.objectMouseOver != null) {
-               var8 = this.mc.objectMouseOver.hitVec.distanceTo(var5);
-            }
+      if (var2 != null && this.mc.world != null) {
+         this.mc.profiler.startSection("pick");
+         this.mc.pointedEntity = null;
+         double var3 = this.mc.playerController.getBlockReachDistance();
+         this.mc.objectMouseOver = var2.rayTrace(var3, var1);
+         Vec3d var5 = var2.getPositionEyes(var1);
+         boolean var6 = false;
+         byte var7 = 3;
+         double var8 = var3;
+         if (this.mc.playerController.extendedReach()) {
+            var8 = 6.0;
+            var3 = var8;
+         } else if (var3 > 3.0) {
+            var6 = true;
+         }
+
+         if (this.mc.objectMouseOver != null) {
+            var8 = this.mc.objectMouseOver.hitVec.distanceTo(var5);
+         }
+
+         Vec3d var10 = var2.getLook(1.0F);
+         Vec3d var11 = var5.add(var10.x * var3, var10.y * var3, var10.z * var3);
+         this.pointedEntity = null;
+         Vec3d var12 = null;
+         float var13 = 1.0F;
+         List var14 = this.mc
+            .world
+            .getEntitiesInAABBexcluding(
+               var2,
+               var2.getEntityBoundingBox().expand(var10.x * var3, var10.y * var3, var10.z * var3).grow(1.0, 1.0, 1.0),
+               Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
+                  public boolean apply(@Nullable Entity var1) {
+                     return var1 != null && var1.canBeCollidedWith();
+                  }
+               })
+            );
+         double var15 = var8;

-            Vec3d var10 = var2.getLook(1.0F);
-            Vec3d var11 = var5.add(var10.x * var3, var10.y * var3, var10.z * var3);
-            this.pointedEntity = null;
-            Vec3d var12 = null;
-            float var13 = 1.0F;
-            List var14 = this.mc
-               .world
-               .getEntitiesInAABBexcluding(
-                  var2,
-                  var2.getEntityBoundingBox().expand(var10.x * var3, var10.y * var3, var10.z * var3).grow(1.0, 1.0, 1.0),
-                  Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
-                     public boolean apply(@Nullable Entity var1) {
-                        return var1 != null && var1.canBeCollidedWith();
-                     }
-                  })
-               );
-            double var15 = var8;
+         for (int var17 = 0; var17 < var14.size(); var17++) {
+            Entity var18 = (Entity)var14.get(var17);
+            AxisAlignedBB var19 = var18.getEntityBoundingBox().grow(var18.getCollisionBorderSize());
+            RayTraceResult var20 = var19.calculateIntercept(var5, var11);
+            if (var19.contains(var5)) {
+               if (var15 >= 0.0) {
+                  this.pointedEntity = var18;
+                  var12 = var20 == null ? var5 : var20.hitVec;
+                  var15 = 0.0;
+               }
+            } else if (var20 != null) {
+               double var21 = var5.distanceTo(var20.hitVec);
+               if (var21 < var15 || var15 == 0.0) {
+                  boolean var23 = false;
+                  if (Reflector.ForgeEntity_canRiderInteract.exists()) {
+                     var23 = Reflector.callBoolean(var18, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
+                  }

-            for (int var17 = 0; var17 < var14.size(); var17++) {
-               Entity var18 = (Entity)var14.get(var17);
-               AxisAlignedBB var19 = var18.getEntityBoundingBox().grow(var18.getCollisionBorderSize());
-               RayTraceResult var20 = var19.calculateIntercept(var5, var11);
-               if (var19.contains(var5)) {
-                  if (var15 >= 0.0) {
+                  if (var23 || var18.getLowestRidingEntity() != var2.getLowestRidingEntity()) {
                      this.pointedEntity = var18;
-                     var12 = var20 == null ? var5 : var20.hitVec;
-                     var15 = 0.0;
-                  }
-               } else if (var20 != null) {
-                  double var21 = var5.distanceTo(var20.hitVec);
-                  if (var21 < var15 || var15 == 0.0) {
-                     if (var18.getLowestRidingEntity() == var2.getLowestRidingEntity()) {
-                        if (var15 == 0.0) {
-                           this.pointedEntity = var18;
-                           var12 = var20.hitVec;
-                        }
-                     } else {
-                        this.pointedEntity = var18;
-                        var12 = var20.hitVec;
-                        var15 = var21;
-                     }
+                     var12 = var20.hitVec;
+                     var15 = var21;
+                  } else if (var15 == 0.0) {
+                     this.pointedEntity = var18;
+                     var12 = var20.hitVec;
                   }
                }
             }
+         }

-            if (this.pointedEntity != null && var6 && var5.distanceTo(var12) > 3.0) {
-               this.pointedEntity = null;
-               this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, var12, null, new BlockPos(var12));
-            }
+         if (this.pointedEntity != null && var6 && var5.distanceTo(var12) > 3.0) {
+            this.pointedEntity = null;
+            this.mc.objectMouseOver = new RayTraceResult(Type.MISS, var12, (EnumFacing)null, new BlockPos(var12));
+         }

-            if (this.pointedEntity != null && (var15 < var8 || this.mc.objectMouseOver == null)) {
-               this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, var12);
-               if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
-                  this.mc.pointedEntity = this.pointedEntity;
-               }
+         if (this.pointedEntity != null && (var15 < var8 || this.mc.objectMouseOver == null)) {
+            this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, var12);
+            if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
+               this.mc.pointedEntity = this.pointedEntity;
             }
-
-            this.mc.profiler.endSection();
          }
+
+         this.mc.profiler.endSection();
       }
    }

    private void updateFovModifierHand() {
       float var1 = 1.0F;
       if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
@@ -424,26 +481,54 @@
          return 90.0F;
       } else {
          Entity var3 = this.mc.getRenderViewEntity();
          float var4 = 70.0F;
          if (var2) {
             var4 = this.mc.gameSettings.fovSetting;
-            var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * var1;
+            if (Config.isDynamicFov()) {
+               var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * var1;
+            }
+         }
+
+         boolean var5 = false;
+         if (this.mc.currentScreen == null) {
+            var5 = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
+         }
+
+         if (var5) {
+            if (!Config.zoomMode) {
+               Config.zoomMode = true;
+               Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
+               this.mc.gameSettings.smoothCamera = true;
+               this.mc.renderGlobal.displayListEntitiesDirty = true;
+            }
+
+            if (Config.zoomMode) {
+               var4 /= 4.0F;
+            }
+         } else if (Config.zoomMode) {
+            Config.zoomMode = false;
+            this.mc.gameSettings.smoothCamera = Config.zoomSmoothCamera;
+            this.mouseFilterXAxis = new MouseFilter();
+            this.mouseFilterYAxis = new MouseFilter();
+            this.mc.renderGlobal.displayListEntitiesDirty = true;
          }

          if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).getHealth() <= 0.0F) {
-            float var5 = ((EntityLivingBase)var3).deathTime + var1;
-            var4 /= (1.0F - 500.0F / (var5 + 500.0F)) * 2.0F + 1.0F;
+            float var6 = ((EntityLivingBase)var3).deathTime + var1;
+            var4 /= (1.0F - 500.0F / (var6 + 500.0F)) * 2.0F + 1.0F;
          }

          IBlockState var7 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, var3, var1);
-         if (var7.getMaterial() == Material.WATER) {
+         if (var7.a() == Material.WATER) {
             var4 = var4 * 60.0F / 70.0F;
          }

-         return var4;
+         return Reflector.ForgeHooksClient_getFOVModifier.exists()
+            ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, new Object[]{this, var3, var7, var1, var4})
+            : var4;
       }
    }

    private void hurtCameraEffect(float var1) {
       if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
          EntityLivingBase var2 = (EntityLivingBase)this.mc.getRenderViewEntity();
@@ -489,16 +574,18 @@
       if (var2 instanceof EntityLivingBase && ((EntityLivingBase)var2).isPlayerSleeping()) {
          var3 = (float)(var3 + 1.0);
          GlStateManager.translate(0.0F, 0.3F, 0.0F);
          if (!this.mc.gameSettings.debugCamEnable) {
             BlockPos var30 = new BlockPos(var2);
             IBlockState var11 = this.mc.world.getBlockState(var30);
-            Block var32 = var11.getBlock();
-            if (var32 == Blocks.BED) {
-               int var33 = var11.getValue(BlockBed.FACING).getHorizontalIndex();
-               GlStateManager.rotate(var33 * 90, 0.0F, 1.0F, 0.0F);
+            Block var36 = var11.getBlock();
+            if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
+               Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[]{this.mc.world, var30, var11, var2});
+            } else if (var36 == Blocks.BED) {
+               int var39 = ((EnumFacing)var11.getValue(BlockBed.D)).getHorizontalIndex();
+               GlStateManager.rotate(var39 * 90, 0.0F, 1.0F, 0.0F);
             }

             GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
             GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, -1.0F, 0.0F, 0.0F);
          }
       } else if (this.mc.gameSettings.thirdPersonView > 0) {
@@ -547,46 +634,79 @@
             GlStateManager.rotate(var13 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
          }
       } else {
          GlStateManager.translate(0.0F, 0.0F, 0.05F);
       }

-      if (!this.mc.gameSettings.debugCamEnable) {
+      if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
+         if (!this.mc.gameSettings.debugCamEnable) {
+            float var31 = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F;
+            float var34 = var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1;
+            float var37 = 0.0F;
+            if (var2 instanceof EntityAnimal) {
+               EntityAnimal var40 = (EntityAnimal)var2;
+               var31 = var40.aQ + (var40.aP - var40.aQ) * var1 + 180.0F;
+            }
+
+            IBlockState var41 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, var2, var1);
+            Object var42 = Reflector.newInstance(
+               Reflector.EntityViewRenderEvent_CameraSetup_Constructor, new Object[]{this, var2, var41, var1, var31, var34, var37}
+            );
+            Reflector.postForgeBusEvent(var42);
+            var37 = Reflector.callFloat(var42, Reflector.EntityViewRenderEvent_CameraSetup_getRoll, new Object[0]);
+            var34 = Reflector.callFloat(var42, Reflector.EntityViewRenderEvent_CameraSetup_getPitch, new Object[0]);
+            var31 = Reflector.callFloat(var42, Reflector.EntityViewRenderEvent_CameraSetup_getYaw, new Object[0]);
+            GlStateManager.rotate(var37, 0.0F, 0.0F, 1.0F);
+            GlStateManager.rotate(var34, 1.0F, 0.0F, 0.0F);
+            GlStateManager.rotate(var31, 0.0F, 1.0F, 0.0F);
+         }
+      } else if (!this.mc.gameSettings.debugCamEnable) {
          GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
          if (var2 instanceof EntityAnimal) {
-            EntityAnimal var31 = (EntityAnimal)var2;
-            GlStateManager.rotate(var31.prevRotationYawHead + (var31.rotationYawHead - var31.prevRotationYawHead) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
+            EntityAnimal var33 = (EntityAnimal)var2;
+            GlStateManager.rotate(var33.aQ + (var33.aP - var33.aQ) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
          } else {
             GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
          }
       }

       GlStateManager.translate(0.0F, -var3, 0.0F);
       var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * var1;
       var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * var1 + var3;
       var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * var1;
       this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, var1);
    }

-   private void setupCameraTransform(float var1, int var2) {
+   public void setupCameraTransform(float var1, int var2) {
       this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
+      if (Config.isFogFancy()) {
+         this.farPlaneDistance *= 0.95F;
+      }
+
+      if (Config.isFogFast()) {
+         this.farPlaneDistance *= 0.83F;
+      }
+
       GlStateManager.matrixMode(5889);
       GlStateManager.loadIdentity();
       float var3 = 0.07F;
       if (this.mc.gameSettings.anaglyph) {
          GlStateManager.translate(-(var2 * 2 - 1) * 0.07F, 0.0F, 0.0F);
       }

+      this.clipDistance = this.farPlaneDistance * 2.0F;
+      if (this.clipDistance < 173.0F) {
+         this.clipDistance = 173.0F;
+      }
+
       if (this.cameraZoom != 1.0) {
          GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
          GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0);
       }

-      Project.gluPerspective(
-         this.getFOVModifier(var1, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2
-      );
+      Project.gluPerspective(this.getFOVModifier(var1, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
       GlStateManager.matrixMode(5888);
       GlStateManager.loadIdentity();
       if (this.mc.gameSettings.anaglyph) {
          GlStateManager.translate((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
       }

@@ -628,42 +748,65 @@
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
          }
       }
    }

    private void renderHand(float var1, int var2) {
+      this.renderHand(var1, var2, true, true, false);
+   }
+
+   public void renderHand(float var1, int var2, boolean var3, boolean var4, boolean var5) {
       if (!this.debugView) {
          GlStateManager.matrixMode(5889);
          GlStateManager.loadIdentity();
-         float var3 = 0.07F;
+         float var6 = 0.07F;
          if (this.mc.gameSettings.anaglyph) {
             GlStateManager.translate(-(var2 * 2 - 1) * 0.07F, 0.0F, 0.0F);
          }

+         if (Config.isShaders()) {
+            Shaders.applyHandDepth();
+         }
+
          Project.gluPerspective(this.getFOVModifier(var1, false), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
          GlStateManager.matrixMode(5888);
          GlStateManager.loadIdentity();
          if (this.mc.gameSettings.anaglyph) {
             GlStateManager.translate((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
          }

-         GlStateManager.pushMatrix();
-         this.hurtCameraEffect(var1);
-         if (this.mc.gameSettings.viewBobbing) {
-            this.applyBobbing(var1);
+         boolean var7 = false;
+         if (var3) {
+            GlStateManager.pushMatrix();
+            this.hurtCameraEffect(var1);
+            if (this.mc.gameSettings.viewBobbing) {
+               this.applyBobbing(var1);
+            }
+
+            var7 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
+            boolean var8 = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, var1, var2);
+            if (var8 && this.mc.gameSettings.thirdPersonView == 0 && !var7 && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
+               this.enableLightmap();
+               if (Config.isShaders()) {
+                  ShadersRender.renderItemFP(this.itemRenderer, var1, var5);
+               } else {
+                  this.itemRenderer.renderItemInFirstPerson(var1);
+               }
+
+               this.disableLightmap();
+            }
+
+            GlStateManager.popMatrix();
          }

-         boolean var4 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
-         if (this.mc.gameSettings.thirdPersonView == 0 && !var4 && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
-            this.enableLightmap();
-            this.itemRenderer.renderItemInFirstPerson(var1);
-            this.disableLightmap();
+         if (!var4) {
+            return;
          }

-         GlStateManager.popMatrix();
-         if (this.mc.gameSettings.thirdPersonView == 0 && !var4) {
+         this.disableLightmap();
+         if (this.mc.gameSettings.thirdPersonView == 0 && !var7) {
             this.itemRenderer.renderOverlays(var1);
             this.hurtCameraEffect(var1);
          }

          if (this.mc.gameSettings.viewBobbing) {
             this.applyBobbing(var1);
@@ -672,12 +815,15 @@
    }

    public void disableLightmap() {
       GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
       GlStateManager.disableTexture2D();
       GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+      if (Config.isShaders()) {
+         Shaders.disableLightmap();
+      }
    }

    public void enableLightmap() {
       GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
       GlStateManager.matrixMode(5890);
       GlStateManager.loadIdentity();
@@ -685,17 +831,20 @@
       GlStateManager.scale(0.00390625F, 0.00390625F, 0.00390625F);
       GlStateManager.translate(8.0F, 8.0F, 8.0F);
       GlStateManager.matrixMode(5888);
       this.mc.getTextureManager().bindTexture(this.locationLightMap);
       GlStateManager.glTexParameteri(3553, 10241, 9729);
       GlStateManager.glTexParameteri(3553, 10240, 9729);
-      GlStateManager.glTexParameteri(3553, 10242, 10496);
-      GlStateManager.glTexParameteri(3553, 10243, 10496);
+      GlStateManager.glTexParameteri(3553, 10242, 33071);
+      GlStateManager.glTexParameteri(3553, 10243, 33071);
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       GlStateManager.enableTexture2D();
       GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+      if (Config.isShaders()) {
+         Shaders.enableLightmap();
+      }
    }

    private void updateTorchFlicker() {
       this.torchFlickerDX = (float)(this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
       this.torchFlickerDX = (float)(this.torchFlickerDX * 0.9);
       this.torchFlickerX = this.torchFlickerX + (this.torchFlickerDX - this.torchFlickerX);
@@ -704,130 +853,150 @@

    private void updateLightmap(float var1) {
       if (this.lightmapUpdateNeeded) {
          this.mc.profiler.startSection("lightTex");
          WorldClient var2 = this.mc.world;
          if (var2 != null) {
+            if (Config.isCustomColors()
+               && CustomColors.updateLightmap(var2, this.torchFlickerX, this.lightmapColors, this.mc.player.isPotionActive(MobEffects.NIGHT_VISION), var1)) {
+               this.lightmapTexture.updateDynamicTexture();
+               this.lightmapUpdateNeeded = false;
+               this.mc.profiler.endSection();
+               return;
+            }
+
             float var3 = var2.getSunBrightness(1.0F);
             float var4 = var3 * 0.95F + 0.05F;

             for (int var5 = 0; var5 < 256; var5++) {
                float var6 = var2.provider.getLightBrightnessTable()[var5 / 16] * var4;
                float var7 = var2.provider.getLightBrightnessTable()[var5 % 16] * (this.torchFlickerX * 0.1F + 1.5F);
                if (var2.getLastLightningBolt() > 0) {
                   var6 = var2.provider.getLightBrightnessTable()[var5 / 16];
                }

                float var8 = var6 * (var3 * 0.65F + 0.35F);
                float var9 = var6 * (var3 * 0.65F + 0.35F);
-               float var12 = var7 * ((var7 * 0.6F + 0.4F) * 0.6F + 0.4F);
-               float var13 = var7 * (var7 * var7 * 0.6F + 0.4F);
-               float var14 = var8 + var7;
-               float var15 = var9 + var12;
-               float var16 = var6 + var13;
+               float var10 = var7 * ((var7 * 0.6F + 0.4F) * 0.6F + 0.4F);
+               float var11 = var7 * (var7 * var7 * 0.6F + 0.4F);
+               float var12 = var8 + var7;
+               float var13 = var9 + var10;
+               float var14 = var6 + var11;
+               var12 = var12 * 0.96F + 0.03F;
+               var13 = var13 * 0.96F + 0.03F;
                var14 = var14 * 0.96F + 0.03F;
-               var15 = var15 * 0.96F + 0.03F;
-               var16 = var16 * 0.96F + 0.03F;
                if (this.bossColorModifier > 0.0F) {
-                  float var17 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
-                  var14 = var14 * (1.0F - var17) + var14 * 0.7F * var17;
-                  var15 = var15 * (1.0F - var17) + var15 * 0.6F * var17;
-                  var16 = var16 * (1.0F - var17) + var16 * 0.6F * var17;
+                  float var15 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
+                  var12 = var12 * (1.0F - var15) + var12 * 0.7F * var15;
+                  var13 = var13 * (1.0F - var15) + var13 * 0.6F * var15;
+                  var14 = var14 * (1.0F - var15) + var14 * 0.6F * var15;
                }

                if (var2.provider.getDimensionType().getId() == 1) {
-                  var14 = 0.22F + var7 * 0.75F;
-                  var15 = 0.28F + var12 * 0.75F;
-                  var16 = 0.25F + var13 * 0.75F;
-               }
-
+                  var12 = 0.22F + var7 * 0.75F;
+                  var13 = 0.28F + var10 * 0.75F;
+                  var14 = 0.25F + var11 * 0.75F;
+               }
+
+               if (Reflector.ForgeWorldProvider_getLightmapColors.exists()) {
+                  float[] var35 = new float[]{var12, var13, var14};
+                  Reflector.call(var2.provider, Reflector.ForgeWorldProvider_getLightmapColors, new Object[]{var1, var3, var6, var7, var35});
+                  var12 = var35[0];
+                  var13 = var35[1];
+                  var14 = var35[2];
+               }
+
+               var12 = MathHelper.clamp(var12, 0.0F, 1.0F);
+               var13 = MathHelper.clamp(var13, 0.0F, 1.0F);
+               var14 = MathHelper.clamp(var14, 0.0F, 1.0F);
                if (this.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
-                  float var34 = this.getNightVisionBrightness(this.mc.player, var1);
-                  float var18 = 1.0F / var14;
-                  if (var18 > 1.0F / var15) {
-                     var18 = 1.0F / var15;
+                  float var36 = this.getNightVisionBrightness(this.mc.player, var1);
+                  float var16 = 1.0F / var12;
+                  if (var16 > 1.0F / var13) {
+                     var16 = 1.0F / var13;
                   }

-                  if (var18 > 1.0F / var16) {
-                     var18 = 1.0F / var16;
+                  if (var16 > 1.0F / var14) {
+                     var16 = 1.0F / var14;
                   }

-                  var14 = var14 * (1.0F - var34) + var14 * var18 * var34;
-                  var15 = var15 * (1.0F - var34) + var15 * var18 * var34;
-                  var16 = var16 * (1.0F - var34) + var16 * var18 * var34;
+                  var12 = var12 * (1.0F - var36) + var12 * var16 * var36;
+                  var13 = var13 * (1.0F - var36) + var13 * var16 * var36;
+                  var14 = var14 * (1.0F - var36) + var14 * var16 * var36;
+               }
+
+               if (var12 > 1.0F) {
+                  var12 = 1.0F;
+               }
+
+               if (var13 > 1.0F) {
+                  var13 = 1.0F;
                }

                if (var14 > 1.0F) {
                   var14 = 1.0F;
                }

-               if (var15 > 1.0F) {
-                  var15 = 1.0F;
+               float var37 = this.mc.gameSettings.gammaSetting;
+               float var38 = 1.0F - var12;
+               float var17 = 1.0F - var13;
+               float var18 = 1.0F - var14;
+               var38 = 1.0F - var38 * var38 * var38 * var38;
+               var17 = 1.0F - var17 * var17 * var17 * var17;
+               var18 = 1.0F - var18 * var18 * var18 * var18;
+               var12 = var12 * (1.0F - var37) + var38 * var37;
+               var13 = var13 * (1.0F - var37) + var17 * var37;
+               var14 = var14 * (1.0F - var37) + var18 * var37;
+               var12 = var12 * 0.96F + 0.03F;
+               var13 = var13 * 0.96F + 0.03F;
+               var14 = var14 * 0.96F + 0.03F;
+               if (var12 > 1.0F) {
+                  var12 = 1.0F;
                }

-               if (var16 > 1.0F) {
-                  var16 = 1.0F;
+               if (var13 > 1.0F) {
+                  var13 = 1.0F;
                }

-               float var35 = this.mc.gameSettings.gammaSetting;
-               float var36 = 1.0F - var14;
-               float var19 = 1.0F - var15;
-               float var20 = 1.0F - var16;
-               var36 = 1.0F - var36 * var36 * var36 * var36;
-               var19 = 1.0F - var19 * var19 * var19 * var19;
-               var20 = 1.0F - var20 * var20 * var20 * var20;
-               var14 = var14 * (1.0F - var35) + var36 * var35;
-               var15 = var15 * (1.0F - var35) + var19 * var35;
-               var16 = var16 * (1.0F - var35) + var20 * var35;
-               var14 = var14 * 0.96F + 0.03F;
-               var15 = var15 * 0.96F + 0.03F;
-               var16 = var16 * 0.96F + 0.03F;
                if (var14 > 1.0F) {
                   var14 = 1.0F;
                }

-               if (var15 > 1.0F) {
-                  var15 = 1.0F;
+               if (var12 < 0.0F) {
+                  var12 = 0.0F;
                }

-               if (var16 > 1.0F) {
-                  var16 = 1.0F;
+               if (var13 < 0.0F) {
+                  var13 = 0.0F;
                }

                if (var14 < 0.0F) {
                   var14 = 0.0F;
                }

-               if (var15 < 0.0F) {
-                  var15 = 0.0F;
-               }
-
-               if (var16 < 0.0F) {
-                  var16 = 0.0F;
-               }
-
-               short var21 = 255;
+               short var19 = 255;
+               int var20 = (int)(var12 * 255.0F);
+               int var21 = (int)(var13 * 255.0F);
                int var22 = (int)(var14 * 255.0F);
-               int var23 = (int)(var15 * 255.0F);
-               int var24 = (int)(var16 * 255.0F);
-               this.lightmapColors[var5] = 0xFF000000 | var22 << 16 | var23 << 8 | var24;
+               this.lightmapColors[var5] = 0xFF000000 | var20 << 16 | var21 << 8 | var22;
             }

             this.lightmapTexture.updateDynamicTexture();
             this.lightmapUpdateNeeded = false;
             this.mc.profiler.endSection();
          }
       }
    }

-   private float getNightVisionBrightness(EntityLivingBase var1, float var2) {
+   public float getNightVisionBrightness(EntityLivingBase var1, float var2) {
       int var3 = var1.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
       return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin((var3 - var2) * (float) Math.PI * 0.2F) * 0.3F;
    }

    public void updateCameraAndRender(float var1, long var2) {
+      this.frameInit();
       boolean var4 = Display.isActive();
       if (!var4 && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
          if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
             this.mc.displayInGameMenu();
          }
       } else {
@@ -874,13 +1043,23 @@
          final ScaledResolution var17 = new ScaledResolution(this.mc);
          int var18 = var17.getScaledWidth();
          int var20 = var17.getScaledHeight();
          final int var22 = Mouse.getX() * var18 / this.mc.displayWidth;
          final int var23 = var20 - Mouse.getY() * var20 / this.mc.displayHeight - 1;
          int var24 = this.mc.gameSettings.limitFramerate;
-         if (this.mc.world != null) {
+         if (this.mc.world == null) {
+            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
+            GlStateManager.matrixMode(5889);
+            GlStateManager.loadIdentity();
+            GlStateManager.matrixMode(5888);
+            GlStateManager.loadIdentity();
+            this.setupOverlayRendering();
+            this.renderEndNanoTime = System.nanoTime();
+            TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
+            TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRenderer;
+         } else {
             this.mc.profiler.startSection("level");
             int var11 = Math.min(Minecraft.getDebugFPS(), var24);
             var11 = Math.max(var11, 60);
             long var12 = System.nanoTime() - var2;
             long var14 = Math.max(1000000000 / var11 / 4 - var12, 0L);
             this.renderWorld(var1, System.nanoTime() + var14);
@@ -908,30 +1087,33 @@
             this.mc.profiler.endStartSection("gui");
             if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                GlStateManager.alphaFunc(516, 0.1F);
                this.setupOverlayRendering();
                this.renderItemActivation(var18, var20, var1);
                this.mc.ingameGUI.renderGameOverlay(var1);
+               if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
+                  Config.drawFps();
+               }
+
+               if (this.mc.gameSettings.showDebugInfo) {
+                  Lagometer.showLagometer(var17);
+               }
             }

             this.mc.profiler.endSection();
-         } else {
-            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
-            GlStateManager.matrixMode(5889);
-            GlStateManager.loadIdentity();
-            GlStateManager.matrixMode(5888);
-            GlStateManager.loadIdentity();
-            this.setupOverlayRendering();
-            this.renderEndNanoTime = System.nanoTime();
          }

          if (this.mc.currentScreen != null) {
             GlStateManager.clear(256);

             try {
-               this.mc.currentScreen.drawScreen(var22, var23, this.mc.getTickLength());
+               if (Reflector.ForgeHooksClient_drawScreen.exists()) {
+                  Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, new Object[]{this.mc.currentScreen, var22, var23, this.mc.getTickLength()});
+               } else {
+                  this.mc.currentScreen.drawScreen(var22, var23, this.mc.getTickLength());
+               }
             } catch (Throwable var16) {
                CrashReport var26 = CrashReport.makeCrashReport(var16, "Rendering screen");
                CrashReportCategory var13 = var26.makeCategory("Screen render details");
                var13.addDetail("Screen name", new ICrashReportDetail<String>() {
                   public String call() throws Exception {
                      return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
@@ -958,12 +1140,20 @@
                   }
                );
                throw new ReportedException(var26);
             }
          }
       }
+
+      this.frameFinish();
+      this.waitForServerThread();
+      MemoryMonitor.update();
+      Lagometer.updateLagometer();
+      if (this.mc.gameSettings.ofProfiler) {
+         this.mc.gameSettings.showDebugProfilerChart = true;
+      }
    }

    private void createWorldIcon() {
       if (this.mc.renderGlobal.getRenderedChunks() > 10 && this.mc.renderGlobal.hasNoChunkUpdates() && !this.mc.getIntegratedServer().isWorldIconSet()) {
          BufferedImage var1 = ScreenShotHelper.createScreenshot(this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer());
          int var2 = var1.getWidth();
@@ -977,13 +1167,13 @@
             var5 = (var3 - var2) / 2;
          }

          try {
             BufferedImage var6 = new BufferedImage(64, 64, 1);
             Graphics2D var7 = var6.createGraphics();
-            var7.drawImage(var1, 0, 0, 64, 64, var4, var5, var4 + var2, var5 + var2, null);
+            var7.drawImage(var1, 0, 0, 64, 64, var4, var5, var4 + var2, var5 + var2, (ImageObserver)null);
             var7.dispose();
             ImageIO.write(var6, "png", this.mc.getIntegratedServer().getWorldIconFile());
          } catch (IOException var8) {
             LOGGER.warn("Couldn't save auto screenshot", var8);
          }
       }
@@ -998,19 +1188,20 @@
          return false;
       } else {
          Entity var1 = this.mc.getRenderViewEntity();
          boolean var2 = var1 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
          if (var2 && !((EntityPlayer)var1).capabilities.allowEdit) {
             ItemStack var3 = ((EntityPlayer)var1).getHeldItemMainhand();
-            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
+            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == Type.BLOCK) {
                BlockPos var4 = this.mc.objectMouseOver.getBlockPos();
-               Block var5 = this.mc.world.getBlockState(var4).getBlock();
+               IBlockState var5 = this.mc.world.getBlockState(var4);
+               Block var6 = var5.getBlock();
                if (this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
-                  var2 = var5.hasTileEntity() && this.mc.world.getTileEntity(var4) instanceof IInventory;
+                  var2 = ReflectorForge.blockHasTileEntity(var5) && this.mc.world.getTileEntity(var4) instanceof IInventory;
                } else {
-                  var2 = !var3.isEmpty() && (var3.canDestroy(var5) || var3.canPlaceOn(var5));
+                  var2 = !var3.isEmpty() && (var3.canDestroy(var6) || var3.canPlaceOn(var6));
                }
             }
          }

          return var2;
       }
@@ -1020,15 +1211,19 @@
       this.updateLightmap(var1);
       if (this.mc.getRenderViewEntity() == null) {
          this.mc.setRenderViewEntity(this.mc.player);
       }

       this.getMouseOver(var1);
+      if (Config.isShaders()) {
+         Shaders.beginRender(this.mc, var1, var2);
+      }
+
       GlStateManager.enableDepth();
       GlStateManager.enableAlpha();
-      GlStateManager.alphaFunc(516, 0.5F);
+      GlStateManager.alphaFunc(516, 0.1F);
       this.mc.profiler.startSection("center");
       if (this.mc.gameSettings.anaglyph) {
          anaglyphField = 0;
          GlStateManager.colorMask(false, true, true, false);
          this.renderWorldPass(0, var1, var2);
          anaglyphField = 1;
@@ -1040,186 +1235,345 @@
       }

       this.mc.profiler.endSection();
    }

    private void renderWorldPass(int var1, float var2, long var3) {
-      RenderGlobal var5 = this.mc.renderGlobal;
-      ParticleManager var6 = this.mc.effectRenderer;
-      boolean var7 = this.isDrawBlockOutline();
+      boolean var5 = Config.isShaders();
+      if (var5) {
+         Shaders.beginRenderPass(var1, var2, var3);
+      }
+
+      RenderGlobal var6 = this.mc.renderGlobal;
+      ParticleManager var7 = this.mc.effectRenderer;
+      boolean var8 = this.isDrawBlockOutline();
       GlStateManager.enableCull();
       this.mc.profiler.endStartSection("clear");
-      GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
+      if (var5) {
+         Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
+      } else {
+         GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
+      }
+
       this.updateFogColor(var2);
       GlStateManager.clear(16640);
+      if (var5) {
+         Shaders.clearRenderBuffer();
+      }
+
       this.mc.profiler.endStartSection("camera");
       this.setupCameraTransform(var2, var1);
-      ActiveRenderInfo.updateRenderInfo(this.mc.player, this.mc.gameSettings.thirdPersonView == 2);
+      if (var5) {
+         Shaders.setCamera(var2);
+      }
+
+      if (Reflector.ActiveRenderInfo_updateRenderInfo2.exists()) {
+         Reflector.call(Reflector.ActiveRenderInfo_updateRenderInfo2, new Object[]{this.mc.getRenderViewEntity(), this.mc.gameSettings.thirdPersonView == 2});
+      } else {
+         ActiveRenderInfo.updateRenderInfo(this.mc.player, this.mc.gameSettings.thirdPersonView == 2);
+      }
+
       this.mc.profiler.endStartSection("frustum");
-      ClippingHelperImpl.getInstance();
+      ClippingHelper var9 = ClippingHelperImpl.getInstance();
       this.mc.profiler.endStartSection("culling");
-      Frustum var8 = new Frustum();
-      Entity var9 = this.mc.getRenderViewEntity();
-      double var10 = var9.lastTickPosX + (var9.posX - var9.lastTickPosX) * var2;
-      double var12 = var9.lastTickPosY + (var9.posY - var9.lastTickPosY) * var2;
-      double var14 = var9.lastTickPosZ + (var9.posZ - var9.lastTickPosZ) * var2;
-      var8.setPosition(var10, var12, var14);
-      if (this.mc.gameSettings.renderDistanceChunks >= 4) {
+      var9.disabled = Config.isShaders() && !Shaders.isFrustumCulling();
+      Frustum var10 = new Frustum(var9);
+      Entity var11 = this.mc.getRenderViewEntity();
+      double var12 = var11.lastTickPosX + (var11.posX - var11.lastTickPosX) * var2;
+      double var14 = var11.lastTickPosY + (var11.posY - var11.lastTickPosY) * var2;
+      double var16 = var11.lastTickPosZ + (var11.posZ - var11.lastTickPosZ) * var2;
+      if (var5) {
+         ShadersRender.setFrustrumPosition(var10, var12, var14, var16);
+      } else {
+         var10.setPosition(var12, var14, var16);
+      }
+
+      if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
          this.setupFog(-1, var2);
          this.mc.profiler.endStartSection("sky");
          GlStateManager.matrixMode(5889);
          GlStateManager.loadIdentity();
-         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
+         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
          GlStateManager.matrixMode(5888);
-         var5.renderSky(var2, var1);
+         if (var5) {
+            Shaders.beginSky();
+         }
+
+         var6.renderSky(var2, var1);
+         if (var5) {
+            Shaders.endSky();
+         }
+
          GlStateManager.matrixMode(5889);
          GlStateManager.loadIdentity();
-         Project.gluPerspective(
-            this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2
-         );
+         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
          GlStateManager.matrixMode(5888);
+      } else {
+         GlStateManager.disableBlend();
       }

       this.setupFog(0, var2);
       GlStateManager.shadeModel(7425);
-      if (var9.posY + var9.getEyeHeight() < 128.0) {
-         this.renderCloudsCheck(var5, var2, var1, var10, var12, var14);
+      if (var11.posY + var11.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0F) {
+         this.renderCloudsCheck(var6, var2, var1, var12, var14, var16);
       }

       this.mc.profiler.endStartSection("prepareterrain");
       this.setupFog(0, var2);
       this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
       RenderHelper.disableStandardItemLighting();
       this.mc.profiler.endStartSection("terrain_setup");
-      var5.setupTerrain(var9, var2, var8, this.frameCount++, this.mc.player.isSpectator());
+      this.checkLoadVisibleChunks(var11, var2, var10, this.mc.player.isSpectator());
+      if (var5) {
+         ShadersRender.setupTerrain(var6, var11, var2, var10, this.frameCount++, this.mc.player.isSpectator());
+      } else {
+         var6.setupTerrain(var11, var2, var10, this.frameCount++, this.mc.player.isSpectator());
+      }
+
       if (var1 == 0 || var1 == 2) {
          this.mc.profiler.endStartSection("updatechunks");
+         Lagometer.timerChunkUpload.start();
          this.mc.renderGlobal.updateChunks(var3);
+         Lagometer.timerChunkUpload.end();
       }

       this.mc.profiler.endStartSection("terrain");
+      Lagometer.timerTerrain.start();
+      if (this.mc.gameSettings.ofSmoothFps && var1 > 0) {
+         this.mc.profiler.endStartSection("finish");
+         GL11.glFinish();
+         this.mc.profiler.endStartSection("terrain");
+      }
+
       GlStateManager.matrixMode(5888);
       GlStateManager.pushMatrix();
       GlStateManager.disableAlpha();
-      var5.renderBlockLayer(BlockRenderLayer.SOLID, var2, var1, var9);
+      if (var5) {
+         ShadersRender.beginTerrainSolid();
+      }
+
+      var6.renderBlockLayer(BlockRenderLayer.SOLID, var2, var1, var11);
       GlStateManager.enableAlpha();
-      var5.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, var2, var1, var9);
+      if (var5) {
+         ShadersRender.beginTerrainCutoutMipped();
+      }
+
+      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0);
+      var6.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, var2, var1, var11);
+      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
       this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
-      var5.renderBlockLayer(BlockRenderLayer.CUTOUT, var2, var1, var9);
+      if (var5) {
+         ShadersRender.beginTerrainCutout();
+      }
+
+      var6.renderBlockLayer(BlockRenderLayer.CUTOUT, var2, var1, var11);
       this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
+      if (var5) {
+         ShadersRender.endTerrain();
+      }
+
+      Lagometer.timerTerrain.end();
       GlStateManager.shadeModel(7424);
       GlStateManager.alphaFunc(516, 0.1F);
       if (!this.debugView) {
          GlStateManager.matrixMode(5888);
          GlStateManager.popMatrix();
          GlStateManager.pushMatrix();
          RenderHelper.enableStandardItemLighting();
          this.mc.profiler.endStartSection("entities");
-         var5.renderEntities(var9, var8, var2);
+         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
+            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{0});
+         }
+
+         var6.renderEntities(var11, var10, var2);
+         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
+            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{-1});
+         }
+
          RenderHelper.disableStandardItemLighting();
          this.disableLightmap();
       }

       GlStateManager.matrixMode(5888);
       GlStateManager.popMatrix();
-      if (var7 && this.mc.objectMouseOver != null && !var9.isInsideOfMaterial(Material.WATER)) {
-         EntityPlayer var16 = (EntityPlayer)var9;
+      if (var8 && this.mc.objectMouseOver != null && !var11.isInsideOfMaterial(Material.WATER)) {
+         EntityPlayer var18 = (EntityPlayer)var11;
          GlStateManager.disableAlpha();
          this.mc.profiler.endStartSection("outline");
-         var5.drawSelectionBox(var16, this.mc.objectMouseOver, 0, var2);
+         if (!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists()
+            || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[]{var6, var18, this.mc.objectMouseOver, 0, var2})) {
+            var6.drawSelectionBox(var18, this.mc.objectMouseOver, 0, var2);
+         }
+
          GlStateManager.enableAlpha();
       }

       if (this.mc.debugRenderer.shouldRender()) {
+         boolean var19 = GlStateManager.isFogEnabled();
+         GlStateManager.disableFog();
          this.mc.debugRenderer.renderDebug(var2, var3);
+         GlStateManager.setFogEnabled(var19);
       }

-      this.mc.profiler.endStartSection("destroyProgress");
-      GlStateManager.enableBlend();
-      GlStateManager.tryBlendFuncSeparate(
-         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
-      );
-      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
-      var5.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), var9, var2);
-      this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
+      if (!var6.damagedBlocks.isEmpty()) {
+         this.mc.profiler.endStartSection("destroyProgress");
+         GlStateManager.enableBlend();
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
+         );
+         this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
+         var6.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), var11, var2);
+         this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
+         GlStateManager.disableBlend();
+      }
+
+      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
       GlStateManager.disableBlend();
       if (!this.debugView) {
          this.enableLightmap();
          this.mc.profiler.endStartSection("litParticles");
-         var6.renderLitParticles(var9, var2);
+         if (var5) {
+            Shaders.beginLitParticles();
+         }
+
+         var7.renderLitParticles(var11, var2);
          RenderHelper.disableStandardItemLighting();
          this.setupFog(0, var2);
          this.mc.profiler.endStartSection("particles");
-         var6.renderParticles(var9, var2);
+         if (var5) {
+            Shaders.beginParticles();
+         }
+
+         var7.renderParticles(var11, var2);
+         if (var5) {
+            Shaders.endParticles();
+         }
+
          this.disableLightmap();
       }

       GlStateManager.depthMask(false);
+      if (Config.isShaders()) {
+         GlStateManager.depthMask(Shaders.isRainDepth());
+      }
+
       GlStateManager.enableCull();
       this.mc.profiler.endStartSection("weather");
+      if (var5) {
+         Shaders.beginWeather();
+      }
+
       this.renderRainSnow(var2);
+      if (var5) {
+         Shaders.endWeather();
+      }
+
       GlStateManager.depthMask(true);
-      var5.renderWorldBorder(var9, var2);
+      var6.renderWorldBorder(var11, var2);
+      if (var5) {
+         ShadersRender.renderHand0(this, var2, var1);
+         Shaders.preWater();
+      }
+
       GlStateManager.disableBlend();
       GlStateManager.enableCull();
       GlStateManager.tryBlendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
       );
       GlStateManager.alphaFunc(516, 0.1F);
       this.setupFog(0, var2);
       GlStateManager.enableBlend();
       GlStateManager.depthMask(false);
       this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
       GlStateManager.shadeModel(7425);
       this.mc.profiler.endStartSection("translucent");
-      var5.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, var2, var1, var9);
+      if (var5) {
+         Shaders.beginWater();
+      }
+
+      var6.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, var2, var1, var11);
+      if (var5) {
+         Shaders.endWater();
+      }
+
+      if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
+         RenderHelper.enableStandardItemLighting();
+         this.mc.profiler.endStartSection("entities");
+         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{1});
+         this.mc.renderGlobal.renderEntities(var11, var10, var2);
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.SRC_ALPHA,
+            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+            GlStateManager.SourceFactor.ONE,
+            GlStateManager.DestFactor.ZERO
+         );
+         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{-1});
+         RenderHelper.disableStandardItemLighting();
+      }
+
       GlStateManager.shadeModel(7424);
       GlStateManager.depthMask(true);
       GlStateManager.enableCull();
       GlStateManager.disableBlend();
       GlStateManager.disableFog();
-      if (var9.posY + var9.getEyeHeight() >= 128.0) {
+      if (var11.posY + var11.getEyeHeight() >= 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0F) {
          this.mc.profiler.endStartSection("aboveClouds");
-         this.renderCloudsCheck(var5, var2, var1, var10, var12, var14);
+         this.renderCloudsCheck(var6, var2, var1, var12, var14, var16);
+      }
+
+      if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
+         this.mc.profiler.endStartSection("forge_render_last");
+         Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[]{var6, var2});
       }

       this.mc.profiler.endStartSection("hand");
-      if (this.renderHand) {
+      if (this.renderHand && !Shaders.isShadowPass) {
+         if (var5) {
+            ShadersRender.renderHand1(this, var2, var1);
+            Shaders.renderCompositeFinal();
+         }
+
          GlStateManager.clear(256);
-         this.renderHand(var2, var1);
+         if (var5) {
+            ShadersRender.renderFPOverlay(this, var2, var1);
+         } else {
+            this.renderHand(var2, var1);
+         }
+      }
+
+      if (var5) {
+         Shaders.endRender();
       }
    }

    private void renderCloudsCheck(RenderGlobal var1, float var2, int var3, double var4, double var6, double var8) {
-      if (this.mc.gameSettings.shouldRenderClouds() != 0) {
+      if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
          this.mc.profiler.endStartSection("clouds");
          GlStateManager.matrixMode(5889);
          GlStateManager.loadIdentity();
-         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);
+         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
          GlStateManager.matrixMode(5888);
          GlStateManager.pushMatrix();
          this.setupFog(0, var2);
          var1.renderClouds(var2, var3, var4, var6, var8);
          GlStateManager.disableFog();
          GlStateManager.popMatrix();
          GlStateManager.matrixMode(5889);
          GlStateManager.loadIdentity();
-         Project.gluPerspective(
-            this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2
-         );
+         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
          GlStateManager.matrixMode(5888);
       }
    }

    private void addRainParticles() {
       float var1 = this.mc.world.getRainStrength(1.0F);
-      if (!this.mc.gameSettings.fancyGraphics) {
+      if (!Config.isRainFancy()) {
          var1 /= 2.0F;
       }

-      if (var1 != 0.0F) {
+      if (var1 != 0.0F && Config.isRainSplash()) {
          this.random.setSeed(this.rendererUpdateCount * 312987231L);
          Entity var2 = this.mc.getRenderViewEntity();
          WorldClient var3 = this.mc.world;
          BlockPos var4 = new BlockPos(var2);
          byte var5 = 10;
          double var6 = 0.0;
@@ -1240,20 +1594,20 @@
             Biome var16 = var3.getBiome(var15);
             BlockPos var17 = var15.down();
             IBlockState var18 = var3.getBlockState(var17);
             if (var15.getY() <= var4.getY() + 10 && var15.getY() >= var4.getY() - 10 && var16.canRain() && var16.getTemperature(var15) >= 0.15F) {
                double var19 = this.random.nextDouble();
                double var21 = this.random.nextDouble();
-               AxisAlignedBB var23 = var18.getBoundingBox(var3, var17);
-               if (var18.getMaterial() == Material.LAVA || var18.getBlock() == Blocks.MAGMA) {
+               AxisAlignedBB var23 = var18.e(var3, var17);
+               if (var18.a() == Material.LAVA || var18.getBlock() == Blocks.MAGMA) {
                   this.mc
                      .world
                      .spawnParticle(
                         EnumParticleTypes.SMOKE_NORMAL, var15.getX() + var19, var15.getY() + 0.1F - var23.minY, var15.getZ() + var21, 0.0, 0.0, 0.0, new int[0]
                      );
-               } else if (var18.getMaterial() != Material.AIR) {
+               } else if (var18.a() != Material.AIR) {
                   if (this.random.nextInt(++var12) == 0) {
                      var6 = var17.getX() + var19;
                      var8 = var17.getY() + 0.1F + var23.maxY - 1.0;
                      var10 = var17.getZ() + var21;
                   }

@@ -1275,46 +1629,59 @@
             }
          }
       }
    }

    protected void renderRainSnow(float var1) {
-      float var2 = this.mc.world.getRainStrength(var1);
-      if (!(var2 <= 0.0F)) {
+      if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
+         WorldProvider var2 = this.mc.world.provider;
+         Object var3 = Reflector.call(var2, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);
+         if (var3 != null) {
+            Reflector.callVoid(var3, Reflector.IRenderHandler_render, new Object[]{var1, this.mc.world, this.mc});
+            return;
+         }
+      }
+
+      float var49 = this.mc.world.getRainStrength(var1);
+      if (var49 > 0.0F) {
+         if (Config.isRainOff()) {
+            return;
+         }
+
          this.enableLightmap();
-         Entity var3 = this.mc.getRenderViewEntity();
+         Entity var50 = this.mc.getRenderViewEntity();
          WorldClient var4 = this.mc.world;
-         int var5 = MathHelper.floor(var3.posX);
-         int var6 = MathHelper.floor(var3.posY);
-         int var7 = MathHelper.floor(var3.posZ);
+         int var5 = MathHelper.floor(var50.posX);
+         int var6 = MathHelper.floor(var50.posY);
+         int var7 = MathHelper.floor(var50.posZ);
          Tessellator var8 = Tessellator.getInstance();
          BufferBuilder var9 = var8.getBuffer();
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
-         double var10 = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) * var1;
-         double var12 = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * var1;
-         double var14 = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) * var1;
+         double var10 = var50.lastTickPosX + (var50.posX - var50.lastTickPosX) * var1;
+         double var12 = var50.lastTickPosY + (var50.posY - var50.lastTickPosY) * var1;
+         double var14 = var50.lastTickPosZ + (var50.posZ - var50.lastTickPosZ) * var1;
          int var16 = MathHelper.floor(var12);
          byte var17 = 5;
-         if (this.mc.gameSettings.fancyGraphics) {
+         if (Config.isRainFancy()) {
             var17 = 10;
          }

          byte var18 = -1;
          float var19 = this.rendererUpdateCount + var1;
          var9.setTranslation(-var10, -var12, -var14);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-         BlockPos.MutableBlockPos var20 = new BlockPos.MutableBlockPos();
+         MutableBlockPos var20 = new MutableBlockPos();

          for (int var21 = var7 - var17; var21 <= var7 + var17; var21++) {
             for (int var22 = var5 - var17; var22 <= var5 + var17; var22++) {
                int var23 = (var21 - var7 + 16) * 32 + var22 - var5 + 16;
                double var24 = this.rainXCoords[var23] * 0.5;
                double var26 = this.rainYCoords[var23] * 0.5;
@@ -1354,16 +1721,16 @@

                         double var34 = -(
                               (double)(this.rendererUpdateCount + var22 * var22 * 3121 + var22 * 45238971 + var21 * var21 * 418711 + var21 * 13761 & 31) + var1
                            )
                            / 32.0
                            * (3.0 + this.random.nextDouble());
-                        double var36 = var22 + 0.5F - var3.posX;
-                        double var38 = var21 + 0.5F - var3.posZ;
+                        double var36 = var22 + 0.5F - var50.posX;
+                        double var38 = var21 + 0.5F - var50.posZ;
                         float var40 = MathHelper.sqrt(var36 * var36 + var38 * var38) / var17;
-                        float var41 = ((1.0F - var40 * var40) * 0.5F + 0.5F) * var2;
+                        float var41 = ((1.0F - var40 * var40) * 0.5F + 0.5F) * var49;
                         var20.setPos(var22, var32, var21);
                         int var42 = var4.getCombinedLight(var20, 0);
                         int var43 = var42 >> 16 & 65535;
                         int var44 = var42 & 65535;
                         var9.pos(var22 - var24 + 0.5, var31, var21 - var26 + 0.5)
                            .tex(0.0, var30 * 0.25 + var34)
@@ -1393,40 +1760,40 @@

                            var18 = 1;
                            this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                            var9.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                         }

-                        double var49 = -((this.rendererUpdateCount & 511) + var1) / 512.0F;
-                        double var50 = this.random.nextDouble() + var19 * 0.01 * (float)this.random.nextGaussian();
-                        double var51 = this.random.nextDouble() + var19 * (float)this.random.nextGaussian() * 0.001;
-                        double var52 = var22 + 0.5F - var3.posX;
-                        double var53 = var21 + 0.5F - var3.posZ;
-                        float var54 = MathHelper.sqrt(var52 * var52 + var53 * var53) / var17;
-                        float var45 = ((1.0F - var54 * var54) * 0.3F + 0.5F) * var2;
+                        double var51 = -((this.rendererUpdateCount & 511) + var1) / 512.0F;
+                        double var52 = this.random.nextDouble() + var19 * 0.01 * (float)this.random.nextGaussian();
+                        double var53 = this.random.nextDouble() + var19 * (float)this.random.nextGaussian() * 0.001;
+                        double var54 = var22 + 0.5F - var50.posX;
+                        double var55 = var21 + 0.5F - var50.posZ;
+                        float var56 = MathHelper.sqrt(var54 * var54 + var55 * var55) / var17;
+                        float var45 = ((1.0F - var56 * var56) * 0.3F + 0.5F) * var49;
                         var20.setPos(var22, var32, var21);
                         int var46 = (var4.getCombinedLight(var20, 0) * 3 + 15728880) / 4;
                         int var47 = var46 >> 16 & 65535;
                         int var48 = var46 & 65535;
                         var9.pos(var22 - var24 + 0.5, var31, var21 - var26 + 0.5)
-                           .tex(0.0 + var50, var30 * 0.25 + var49 + var51)
+                           .tex(0.0 + var52, var30 * 0.25 + var51 + var53)
                            .color(1.0F, 1.0F, 1.0F, var45)
                            .lightmap(var47, var48)
                            .endVertex();
                         var9.pos(var22 + var24 + 0.5, var31, var21 + var26 + 0.5)
-                           .tex(1.0 + var50, var30 * 0.25 + var49 + var51)
+                           .tex(1.0 + var52, var30 * 0.25 + var51 + var53)
                            .color(1.0F, 1.0F, 1.0F, var45)
                            .lightmap(var47, var48)
                            .endVertex();
                         var9.pos(var22 + var24 + 0.5, var30, var21 + var26 + 0.5)
-                           .tex(1.0 + var50, var31 * 0.25 + var49 + var51)
+                           .tex(1.0 + var52, var31 * 0.25 + var51 + var53)
                            .color(1.0F, 1.0F, 1.0F, var45)
                            .lightmap(var47, var48)
                            .endVertex();
                         var9.pos(var22 - var24 + 0.5, var30, var21 - var26 + 0.5)
-                           .tex(0.0 + var50, var31 * 0.25 + var49 + var51)
+                           .tex(0.0 + var52, var31 * 0.25 + var51 + var53)
                            .color(1.0F, 1.0F, 1.0F, var45)
                            .lightmap(var47, var48)
                            .endVertex();
                      }
                   }
                }
@@ -1459,16 +1826,18 @@
    private void updateFogColor(float var1) {
       WorldClient var2 = this.mc.world;
       Entity var3 = this.mc.getRenderViewEntity();
       float var4 = 0.25F + 0.75F * this.mc.gameSettings.renderDistanceChunks / 32.0F;
       var4 = 1.0F - (float)Math.pow(var4, 0.25);
       Vec3d var5 = var2.getSkyColor(this.mc.getRenderViewEntity(), var1);
+      var5 = CustomColors.getWorldSkyColor(var5, var2, this.mc.getRenderViewEntity(), var1);
       float var6 = (float)var5.x;
       float var7 = (float)var5.y;
       float var8 = (float)var5.z;
       Vec3d var9 = var2.getFogColor(var1);
+      var9 = CustomColors.getWorldFogColor(var9, var2, this.mc.getRenderViewEntity(), var1);
       this.fogColorRed = (float)var9.x;
       this.fogColorGreen = (float)var9.y;
       this.fogColorBlue = (float)var9.z;
       if (this.mc.gameSettings.renderDistanceChunks >= 4) {
          double var10 = MathHelper.sin(var2.getCelestialAngleRadians(var1)) > 0.0F ? -1.0 : 1.0;
          Vec3d var12 = new Vec3d(var10, 0.0, 0.0);
@@ -1488,173 +1857,242 @@
          }
       }

       this.fogColorRed = this.fogColorRed + (var6 - this.fogColorRed) * var4;
       this.fogColorGreen = this.fogColorGreen + (var7 - this.fogColorGreen) * var4;
       this.fogColorBlue = this.fogColorBlue + (var8 - this.fogColorBlue) * var4;
-      float var20 = var2.getRainStrength(var1);
-      if (var20 > 0.0F) {
-         float var11 = 1.0F - var20 * 0.5F;
-         float var22 = 1.0F - var20 * 0.4F;
+      float var22 = var2.getRainStrength(var1);
+      if (var22 > 0.0F) {
+         float var11 = 1.0F - var22 * 0.5F;
+         float var24 = 1.0F - var22 * 0.4F;
          this.fogColorRed *= var11;
          this.fogColorGreen *= var11;
-         this.fogColorBlue *= var22;
+         this.fogColorBlue *= var24;
       }

-      float var21 = var2.getThunderStrength(var1);
-      if (var21 > 0.0F) {
-         float var23 = 1.0F - var21 * 0.5F;
-         this.fogColorRed *= var23;
-         this.fogColorGreen *= var23;
-         this.fogColorBlue *= var23;
+      float var23 = var2.getThunderStrength(var1);
+      if (var23 > 0.0F) {
+         float var25 = 1.0F - var23 * 0.5F;
+         this.fogColorRed *= var25;
+         this.fogColorGreen *= var25;
+         this.fogColorBlue *= var25;
       }

-      IBlockState var24 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, var3, var1);
+      IBlockState var26 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, var3, var1);
       if (this.cloudFog) {
-         Vec3d var26 = var2.getCloudColour(var1);
-         this.fogColorRed = (float)var26.x;
-         this.fogColorGreen = (float)var26.y;
-         this.fogColorBlue = (float)var26.z;
-      } else if (var24.getMaterial() == Material.WATER) {
-         float var27 = 0.0F;
+         Vec3d var28 = var2.getCloudColour(var1);
+         this.fogColorRed = (float)var28.x;
+         this.fogColorGreen = (float)var28.y;
+         this.fogColorBlue = (float)var28.z;
+      } else if (Reflector.ForgeBlock_getFogColor.exists()) {
+         Vec3d var29 = ActiveRenderInfo.projectViewFromEntity(var3, var1);
+         BlockPos var35 = new BlockPos(var29);
+         IBlockState var15 = this.mc.world.getBlockState(var35);
+         Vec3d var16 = (Vec3d)Reflector.call(
+            var15.getBlock(),
+            Reflector.ForgeBlock_getFogColor,
+            new Object[]{this.mc.world, var35, var15, var3, new Vec3d(this.fogColorRed, this.fogColorGreen, this.fogColorBlue), var1}
+         );
+         this.fogColorRed = (float)var16.x;
+         this.fogColorGreen = (float)var16.y;
+         this.fogColorBlue = (float)var16.z;
+      } else if (var26.a() == Material.WATER) {
+         float var30 = 0.0F;
          if (var3 instanceof EntityLivingBase) {
-            var27 = EnchantmentHelper.getRespirationModifier((EntityLivingBase)var3) * 0.2F;
+            var30 = EnchantmentHelper.getRespirationModifier((EntityLivingBase)var3) * 0.2F;
+            var30 = Config.limit(var30, 0.0F, 0.6F);
             if (((EntityLivingBase)var3).isPotionActive(MobEffects.WATER_BREATHING)) {
-               var27 = var27 * 0.3F + 0.6F;
+               var30 = var30 * 0.3F + 0.6F;
             }
          }

-         this.fogColorRed = 0.02F + var27;
-         this.fogColorGreen = 0.02F + var27;
-         this.fogColorBlue = 0.2F + var27;
-      } else if (var24.getMaterial() == Material.LAVA) {
+         this.fogColorRed = 0.02F + var30;
+         this.fogColorGreen = 0.02F + var30;
+         this.fogColorBlue = 0.2F + var30;
+      } else if (var26.a() == Material.LAVA) {
          this.fogColorRed = 0.6F;
          this.fogColorGreen = 0.1F;
          this.fogColorBlue = 0.0F;
       }

-      float var28 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * var1;
-      this.fogColorRed *= var28;
-      this.fogColorGreen *= var28;
-      this.fogColorBlue *= var28;
-      double var29 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * var1) * var2.provider.getVoidFogYFactor();
+      if (var26.a() == Material.WATER) {
+         Vec3d var32 = CustomColors.getUnderwaterColor(
+            this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ
+         );
+         if (var32 != null) {
+            this.fogColorRed = (float)var32.x;
+            this.fogColorGreen = (float)var32.y;
+            this.fogColorBlue = (float)var32.z;
+         }
+      } else if (var26.a() == Material.LAVA) {
+         Vec3d var33 = CustomColors.getUnderlavaColor(
+            this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ
+         );
+         if (var33 != null) {
+            this.fogColorRed = (float)var33.x;
+            this.fogColorGreen = (float)var33.y;
+            this.fogColorBlue = (float)var33.z;
+         }
+      }
+
+      float var34 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * var1;
+      this.fogColorRed *= var34;
+      this.fogColorGreen *= var34;
+      this.fogColorBlue *= var34;
+      double var36 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * var1) * var2.provider.getVoidFogYFactor();
       if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(MobEffects.BLINDNESS)) {
-         int var16 = ((EntityLivingBase)var3).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
-         if (var16 < 20) {
-            var29 *= 1.0F - var16 / 20.0F;
+         int var38 = ((EntityLivingBase)var3).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
+         if (var38 < 20) {
+            var36 *= 1.0F - var38 / 20.0F;
          } else {
-            var29 = 0.0;
+            var36 = 0.0;
          }
       }

-      if (var29 < 1.0) {
-         if (var29 < 0.0) {
-            var29 = 0.0;
+      if (var36 < 1.0) {
+         if (var36 < 0.0) {
+            var36 = 0.0;
          }

-         var29 *= var29;
-         this.fogColorRed = (float)(this.fogColorRed * var29);
-         this.fogColorGreen = (float)(this.fogColorGreen * var29);
-         this.fogColorBlue = (float)(this.fogColorBlue * var29);
+         var36 *= var36;
+         this.fogColorRed = (float)(this.fogColorRed * var36);
+         this.fogColorGreen = (float)(this.fogColorGreen * var36);
+         this.fogColorBlue = (float)(this.fogColorBlue * var36);
       }

       if (this.bossColorModifier > 0.0F) {
-         float var31 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
-         this.fogColorRed = this.fogColorRed * (1.0F - var31) + this.fogColorRed * 0.7F * var31;
-         this.fogColorGreen = this.fogColorGreen * (1.0F - var31) + this.fogColorGreen * 0.6F * var31;
-         this.fogColorBlue = this.fogColorBlue * (1.0F - var31) + this.fogColorBlue * 0.6F * var31;
+         float var39 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
+         this.fogColorRed = this.fogColorRed * (1.0F - var39) + this.fogColorRed * 0.7F * var39;
+         this.fogColorGreen = this.fogColorGreen * (1.0F - var39) + this.fogColorGreen * 0.6F * var39;
+         this.fogColorBlue = this.fogColorBlue * (1.0F - var39) + this.fogColorBlue * 0.6F * var39;
       }

       if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(MobEffects.NIGHT_VISION)) {
-         float var32 = this.getNightVisionBrightness((EntityLivingBase)var3, var1);
+         float var40 = this.getNightVisionBrightness((EntityLivingBase)var3, var1);
          float var17 = 1.0F / this.fogColorRed;
          if (var17 > 1.0F / this.fogColorGreen) {
             var17 = 1.0F / this.fogColorGreen;
          }

          if (var17 > 1.0F / this.fogColorBlue) {
             var17 = 1.0F / this.fogColorBlue;
          }

-         this.fogColorRed = this.fogColorRed * (1.0F - var32) + this.fogColorRed * var17 * var32;
-         this.fogColorGreen = this.fogColorGreen * (1.0F - var32) + this.fogColorGreen * var17 * var32;
-         this.fogColorBlue = this.fogColorBlue * (1.0F - var32) + this.fogColorBlue * var17 * var32;
+         if (Float.isInfinite(var17)) {
+            var17 = Math.nextAfter(var17, 0.0);
+         }
+
+         this.fogColorRed = this.fogColorRed * (1.0F - var40) + this.fogColorRed * var17 * var40;
+         this.fogColorGreen = this.fogColorGreen * (1.0F - var40) + this.fogColorGreen * var17 * var40;
+         this.fogColorBlue = this.fogColorBlue * (1.0F - var40) + this.fogColorBlue * var17 * var40;
       }

       if (this.mc.gameSettings.anaglyph) {
-         float var33 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
-         float var34 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
+         float var41 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
+         float var43 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
          float var18 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
-         this.fogColorRed = var33;
-         this.fogColorGreen = var34;
+         this.fogColorRed = var41;
+         this.fogColorGreen = var43;
          this.fogColorBlue = var18;
       }

-      GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
+      if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
+         Object var42 = Reflector.newInstance(
+            Reflector.EntityViewRenderEvent_FogColors_Constructor,
+            new Object[]{this, var3, var26, var1, this.fogColorRed, this.fogColorGreen, this.fogColorBlue}
+         );
+         Reflector.postForgeBusEvent(var42);
+         this.fogColorRed = Reflector.callFloat(var42, Reflector.EntityViewRenderEvent_FogColors_getRed, new Object[0]);
+         this.fogColorGreen = Reflector.callFloat(var42, Reflector.EntityViewRenderEvent_FogColors_getGreen, new Object[0]);
+         this.fogColorBlue = Reflector.callFloat(var42, Reflector.EntityViewRenderEvent_FogColors_getBlue, new Object[0]);
+      }
+
+      Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    private void setupFog(int var1, float var2) {
+      this.fogStandard = false;
       Entity var3 = this.mc.getRenderViewEntity();
       this.setupFogColor(false);
       GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       IBlockState var4 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, var3, var2);
-      if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(MobEffects.BLINDNESS)) {
-         float var7 = 5.0F;
-         int var6 = ((EntityLivingBase)var3).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
-         if (var6 < 20) {
-            var7 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - var6 / 20.0F);
+      float var5 = -1.0F;
+      if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
+         var5 = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, new Object[]{this, var3, var4, var2, 0.1F});
+      }
+
+      if (var5 >= 0.0F) {
+         GlStateManager.setFogDensity(var5);
+      } else if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(MobEffects.BLINDNESS)) {
+         float var9 = 5.0F;
+         int var10 = ((EntityLivingBase)var3).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
+         if (var10 < 20) {
+            var9 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - var10 / 20.0F);
          }

          GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
          if (var1 == -1) {
             GlStateManager.setFogStart(0.0F);
-            GlStateManager.setFogEnd(var7 * 0.8F);
+            GlStateManager.setFogEnd(var9 * 0.8F);
          } else {
-            GlStateManager.setFogStart(var7 * 0.25F);
-            GlStateManager.setFogEnd(var7);
+            GlStateManager.setFogStart(var9 * 0.25F);
+            GlStateManager.setFogEnd(var9);
          }

-         if (GLContext.getCapabilities().GL_NV_fog_distance) {
+         if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
             GlStateManager.glFogi(34138, 34139);
          }
       } else if (this.cloudFog) {
          GlStateManager.setFog(GlStateManager.FogMode.EXP);
          GlStateManager.setFogDensity(0.1F);
-      } else if (var4.getMaterial() == Material.WATER) {
+      } else if (var4.a() == Material.WATER) {
          GlStateManager.setFog(GlStateManager.FogMode.EXP);
+         float var6 = Config.isClearWater() ? 0.02F : 0.1F;
          if (var3 instanceof EntityLivingBase) {
             if (((EntityLivingBase)var3).isPotionActive(MobEffects.WATER_BREATHING)) {
                GlStateManager.setFogDensity(0.01F);
             } else {
-               GlStateManager.setFogDensity(0.1F - EnchantmentHelper.getRespirationModifier((EntityLivingBase)var3) * 0.03F);
+               float var7 = 0.1F - EnchantmentHelper.getRespirationModifier((EntityLivingBase)var3) * 0.03F;
+               GlStateManager.setFogDensity(Config.limit(var7, 0.0F, var6));
             }
          } else {
-            GlStateManager.setFogDensity(0.1F);
+            GlStateManager.setFogDensity(var6);
          }
-      } else if (var4.getMaterial() == Material.LAVA) {
+      } else if (var4.a() == Material.LAVA) {
          GlStateManager.setFog(GlStateManager.FogMode.EXP);
          GlStateManager.setFogDensity(2.0F);
       } else {
-         float var5 = this.farPlaneDistance;
+         float var8 = this.farPlaneDistance;
+         this.fogStandard = true;
          GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
          if (var1 == -1) {
             GlStateManager.setFogStart(0.0F);
-            GlStateManager.setFogEnd(var5);
+            GlStateManager.setFogEnd(var8);
          } else {
-            GlStateManager.setFogStart(var5 * 0.75F);
-            GlStateManager.setFogEnd(var5);
+            GlStateManager.setFogStart(var8 * Config.getFogStart());
+            GlStateManager.setFogEnd(var8);
          }

          if (GLContext.getCapabilities().GL_NV_fog_distance) {
-            GlStateManager.glFogi(34138, 34139);
+            if (Config.isFogFancy()) {
+               GlStateManager.glFogi(34138, 34139);
+            }
+
+            if (Config.isFogFast()) {
+               GlStateManager.glFogi(34138, 34140);
+            }
          }

          if (this.mc.world.provider.doesXZShowFog((int)var3.posX, (int)var3.posZ) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog()) {
-            GlStateManager.setFogStart(var5 * 0.05F);
-            GlStateManager.setFogEnd(Math.min(var5, 192.0F) * 0.5F);
+            GlStateManager.setFogStart(var8 * 0.05F);
+            GlStateManager.setFogEnd(var8);
+         }
+
+         if (Reflector.ForgeHooksClient_onFogRender.exists()) {
+            Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, new Object[]{this, var3, var4, var2, var1, var8});
          }
       }

       GlStateManager.enableColorMaterial();
       GlStateManager.enableFog();
       GlStateManager.colorMaterial(1028, 4608);
@@ -1666,12 +2104,16 @@
       } else {
          GlStateManager.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
       }
    }

    private FloatBuffer setFogColorBuffer(float var1, float var2, float var3, float var4) {
+      if (Config.isShaders()) {
+         Shaders.setFogColor(var1, var2, var3);
+      }
+
       ((Buffer)this.fogColorBuffer).clear();
       this.fogColorBuffer.put(var1).put(var2).put(var3).put(var4);
       ((Buffer)this.fogColorBuffer).flip();
       return this.fogColorBuffer;
    }

@@ -1681,12 +2123,271 @@
    }

    public MapItemRenderer getMapItemRenderer() {
       return this.mapItemRenderer;
    }

+   private void waitForServerThread() {
+      this.serverWaitTimeCurrent = 0;
+      if (!Config.isSmoothWorld() || !Config.isSingleProcessor()) {
+         this.lastServerTime = 0L;
+         this.lastServerTicks = 0;
+      } else if (this.mc.isIntegratedServerRunning()) {
+         IntegratedServer var1 = this.mc.getIntegratedServer();
+         if (var1 != null) {
+            boolean var2 = this.mc.isGamePaused();
+            if (!var2 && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
+               if (this.serverWaitTime > 0) {
+                  Lagometer.timerServer.start();
+                  Config.sleep(this.serverWaitTime);
+                  Lagometer.timerServer.end();
+                  this.serverWaitTimeCurrent = this.serverWaitTime;
+               }
+
+               long var3 = System.nanoTime() / 1000000L;
+               if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
+                  long var5 = var3 - this.lastServerTime;
+                  if (var5 < 0L) {
+                     this.lastServerTime = var3;
+                     var5 = 0L;
+                  }
+
+                  if (var5 >= 50L) {
+                     this.lastServerTime = var3;
+                     int var7 = var1.getTickCounter();
+                     int var8 = var7 - this.lastServerTicks;
+                     if (var8 < 0) {
+                        this.lastServerTicks = var7;
+                        var8 = 0;
+                     }
+
+                     if (var8 < 1 && this.serverWaitTime < 100) {
+                        this.serverWaitTime += 2;
+                     }
+
+                     if (var8 > 1 && this.serverWaitTime > 0) {
+                        this.serverWaitTime--;
+                     }
+
+                     this.lastServerTicks = var7;
+                  }
+               } else {
+                  this.lastServerTime = var3;
+                  this.lastServerTicks = var1.getTickCounter();
+                  this.avgServerTickDiff = 1.0F;
+                  this.avgServerTimeDiff = 50.0F;
+               }
+            } else {
+               if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
+                  Config.sleep(20L);
+               }
+
+               this.lastServerTime = 0L;
+               this.lastServerTicks = 0;
+            }
+         }
+      }
+   }
+
+   private void frameInit() {
+      GlErrors.frameStart();
+      if (!this.initialized) {
+         ReflectorResolver.resolve();
+         TextureUtils.registerResourceListener();
+         if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
+            Config.setNotify64BitJava(true);
+         }
+
+         this.initialized = true;
+      }
+
+      Config.checkDisplayMode();
+      WorldClient var1 = this.mc.world;
+      if (var1 != null) {
+         if (Config.getNewRelease() != null) {
+            String var2 = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
+            String var3 = var2 + " " + Config.getNewRelease();
+            TextComponentString var4 = new TextComponentString(I18n.format("of.message.newVersion", "§n" + var3 + "§r"));
+            var4.setStyle(new Style().setClickEvent(new ClickEvent(Action.OPEN_URL, "https://optifine.net/downloads")));
+            this.mc.ingameGUI.getChatGUI().printChatMessage(var4);
+            Config.setNewRelease(null);
+         }
+
+         if (Config.isNotify64BitJava()) {
+            Config.setNotify64BitJava(false);
+            TextComponentString var5 = new TextComponentString(I18n.format("of.message.java64Bit"));
+            this.mc.ingameGUI.getChatGUI().printChatMessage(var5);
+         }
+      }
+
+      if (this.mc.currentScreen instanceof GuiMainMenu) {
+         this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
+      }
+
+      if (this.updatedWorld != var1) {
+         RandomEntities.worldChanged(this.updatedWorld, var1);
+         Config.updateThreadPriorities();
+         this.lastServerTime = 0L;
+         this.lastServerTicks = 0;
+         this.updatedWorld = var1;
+      }
+
+      if (!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
+         Shaders.configAntialiasingLevel = 0;
+      }
+
+      if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == GuiChat.class) {
+         this.mc.displayGuiScreen(new GuiChatOF((GuiChat)this.mc.currentScreen));
+      }
+   }
+
+   private void frameFinish() {
+      if (this.mc.world != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L)) {
+         int var1 = GlStateManager.glGetError();
+         if (var1 != 0 && GlErrors.isEnabled(var1)) {
+            String var2 = Config.getGlErrorString(var1);
+            TextComponentString var3 = new TextComponentString(I18n.format("of.message.openglError", var1, var2));
+            this.mc.ingameGUI.getChatGUI().printChatMessage(var3);
+         }
+      }
+   }
+
+   private void updateMainMenu(GuiMainMenu var1) {
+      try {
+         String var2 = null;
+         Calendar var3 = Calendar.getInstance();
+         var3.setTime(new Date());
+         int var4 = var3.get(5);
+         int var5 = var3.get(2) + 1;
+         if (var4 == 8 && var5 == 4) {
+            var2 = "Happy birthday, OptiFine!";
+         }
+
+         if (var4 == 14 && var5 == 8) {
+            var2 = "Happy birthday, sp614x!";
+         }
+
+         if (var2 == null) {
+            return;
+         }
+
+         Reflector.setFieldValue(var1, Reflector.GuiMainMenu_splashText, var2);
+      } catch (Throwable var6) {
+      }
+   }
+
+   public boolean setFxaaShader(int var1) {
+      if (!OpenGlHelper.isFramebufferEnabled()) {
+         return false;
+      } else if (this.shaderGroup != null && this.shaderGroup != this.fxaaShaders[2] && this.shaderGroup != this.fxaaShaders[4]) {
+         return true;
+      } else if (var1 != 2 && var1 != 4) {
+         if (this.shaderGroup == null) {
+            return true;
+         } else {
+            this.shaderGroup.deleteShaderGroup();
+            this.shaderGroup = null;
+            return true;
+         }
+      } else if (this.shaderGroup != null && this.shaderGroup == this.fxaaShaders[var1]) {
+         return true;
+      } else if (this.mc.world == null) {
+         return true;
+      } else {
+         this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + var1 + "x.json"));
+         this.fxaaShaders[var1] = this.shaderGroup;
+         return this.useShader;
+      }
+   }
+
+   private void checkLoadVisibleChunks(Entity var1, float var2, ICamera var3, boolean var4) {
+      int var5 = 201435902;
+      if (this.loadVisibleChunks) {
+         this.loadVisibleChunks = false;
+         this.loadAllVisibleChunks(var1, var2, var3, var4);
+         this.mc.ingameGUI.getChatGUI().deleteChatLine(var5);
+      }
+
+      if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
+         if (this.mc.gameSettings.keyBindAdvancements.getKeyCode() == 38) {
+            if (this.mc.currentScreen instanceof GuiScreenAdvancements) {
+               this.mc.displayGuiScreen(null);
+            }
+
+            while (Keyboard.next()) {
+            }
+         }
+
+         if (this.mc.currentScreen != null) {
+            return;
+         }
+
+         this.loadVisibleChunks = true;
+         TextComponentString var6 = new TextComponentString(I18n.format("of.message.loadingVisibleChunks"));
+         this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(var6, var5);
+         Reflector.Minecraft_actionKeyF3.setValue(this.mc, Boolean.TRUE);
+      }
+   }
+
+   private void loadAllVisibleChunks(Entity var1, double var2, ICamera var4, boolean var5) {
+      int var6 = this.mc.gameSettings.ofChunkUpdates;
+      boolean var7 = this.mc.gameSettings.ofLazyChunkLoading;
+
+      try {
+         this.mc.gameSettings.ofChunkUpdates = 1000;
+         this.mc.gameSettings.ofLazyChunkLoading = false;
+         RenderGlobal var8 = Config.getRenderGlobal();
+         int var9 = var8.getCountLoadedChunks();
+         long var10 = System.currentTimeMillis();
+         Config.dbg("Loading visible chunks");
+         long var12 = System.currentTimeMillis() + 5000L;
+         int var14 = 0;
+         boolean var15 = false;
+
+         do {
+            var15 = false;
+
+            for (int var16 = 0; var16 < 100; var16++) {
+               var8.displayListEntitiesDirty = true;
+               var8.setupTerrain(var1, var2, var4, this.frameCount++, var5);
+               if (!var8.hasNoChunkUpdates()) {
+                  var15 = true;
+               }
+
+               var14 += var8.getCountChunksToUpdate();
+
+               while (!var8.hasNoChunkUpdates()) {
+                  var8.updateChunks(System.nanoTime() + 1000000000L);
+               }
+
+               var14 -= var8.getCountChunksToUpdate();
+               if (!var15) {
+                  break;
+               }
+            }
+
+            if (var8.getCountLoadedChunks() != var9) {
+               var15 = true;
+               var9 = var8.getCountLoadedChunks();
+            }
+
+            if (System.currentTimeMillis() > var12) {
+               Config.log("Chunks loaded: " + var14);
+               var12 = System.currentTimeMillis() + 5000L;
+            }
+         } while (var15);
+
+         Config.log("Chunks loaded: " + var14);
+         Config.log("Finished loading visible chunks");
+         RenderChunk.renderChunksUpdated = 0;
+      } finally {
+         this.mc.gameSettings.ofChunkUpdates = var6;
+         this.mc.gameSettings.ofLazyChunkLoading = var7;
+      }
+   }
+
    public static void drawNameplate(
       FontRenderer var0, String var1, float var2, float var3, float var4, int var5, float var6, float var7, boolean var8, boolean var9
    ) {
       GlStateManager.pushMatrix();
       GlStateManager.translate(var2, var3, var4);
       GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
@@ -1755,13 +2456,13 @@
          );
          float var12 = 50.0F + 175.0F * MathHelper.sin(var9);
          GlStateManager.scale(var12, -var12, var12);
          GlStateManager.rotate(900.0F * MathHelper.abs(MathHelper.sin(var9)), 0.0F, 1.0F, 0.0F);
          GlStateManager.rotate(6.0F * MathHelper.cos(var5 * 8.0F), 1.0F, 0.0F, 0.0F);
          GlStateManager.rotate(6.0F * MathHelper.cos(var5 * 8.0F), 0.0F, 0.0F, 1.0F);
-         this.mc.getRenderItem().renderItem(this.itemActivationItem, ItemCameraTransforms.TransformType.FIXED);
+         this.mc.getRenderItem().renderItem(this.itemActivationItem, TransformType.FIXED);
          GlStateManager.popAttrib();
          GlStateManager.popMatrix();
          RenderHelper.disableStandardItemLighting();
          GlStateManager.enableCull();
          GlStateManager.disableDepth();
       }
 */