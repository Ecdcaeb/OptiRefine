package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

public class MixinRenderGlobal {
}
/*
--- net/minecraft/client/renderer/RenderGlobal.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/RenderGlobal.java	Tue Aug 19 14:59:58 2025
@@ -1,18 +1,24 @@
 package net.minecraft.client.renderer;

 import com.google.common.collect.Lists;
 import com.google.common.collect.Maps;
-import com.google.common.collect.Queues;
 import com.google.common.collect.Sets;
 import com.google.gson.JsonSyntaxException;
+import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
+import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
 import java.io.IOException;
 import java.util.ArrayDeque;
 import java.util.ArrayList;
+import java.util.Arrays;
 import java.util.Collection;
+import java.util.Collections;
+import java.util.Deque;
+import java.util.HashSet;
 import java.util.Iterator;
+import java.util.LinkedHashSet;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import java.util.Set;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
@@ -23,12 +29,13 @@
 import net.minecraft.block.SoundType;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.audio.ISound;
 import net.minecraft.client.audio.PositionedSoundRecord;
+import net.minecraft.client.multiplayer.ChunkProviderClient;
 import net.minecraft.client.multiplayer.WorldClient;
 import net.minecraft.client.particle.Particle;
 import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
 import net.minecraft.client.renderer.chunk.CompiledChunk;
 import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
 import net.minecraft.client.renderer.chunk.ListChunkFactory;
@@ -36,21 +43,25 @@
 import net.minecraft.client.renderer.chunk.VboChunkFactory;
 import net.minecraft.client.renderer.chunk.VisGraph;
 import net.minecraft.client.renderer.culling.ClippingHelper;
 import net.minecraft.client.renderer.culling.ClippingHelperImpl;
 import net.minecraft.client.renderer.culling.Frustum;
 import net.minecraft.client.renderer.culling.ICamera;
+import net.minecraft.client.renderer.entity.RenderItemFrame;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;
 import net.minecraft.client.renderer.texture.TextureManager;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
+import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.client.renderer.vertex.VertexBuffer;
 import net.minecraft.client.renderer.vertex.VertexFormat;
 import net.minecraft.client.renderer.vertex.VertexFormatElement;
+import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
+import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.client.resources.IResourceManagerReloadListener;
 import net.minecraft.client.shader.Framebuffer;
 import net.minecraft.client.shader.ShaderGroup;
 import net.minecraft.client.shader.ShaderLinkHelper;
 import net.minecraft.crash.CrashReport;
@@ -77,45 +88,68 @@
 import net.minecraft.util.SoundEvent;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.util.math.Vec3d;
+import net.minecraft.util.math.BlockPos.MutableBlockPos;
+import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
+import net.minecraft.util.math.RayTraceResult.Type;
+import net.minecraft.world.DimensionType;
 import net.minecraft.world.IWorldEventListener;
 import net.minecraft.world.World;
+import net.minecraft.world.WorldProvider;
 import net.minecraft.world.border.WorldBorder;
 import net.minecraft.world.chunk.Chunk;
+import net.minecraft.world.chunk.IChunkProvider;
+import net.optifine.CustomColors;
+import net.optifine.CustomSky;
+import net.optifine.DynamicLights;
+import net.optifine.Lagometer;
+import net.optifine.RandomEntities;
+import net.optifine.SmartAnimations;
+import net.optifine.reflect.Reflector;
+import net.optifine.render.ChunkVisibility;
+import net.optifine.render.CloudRenderer;
+import net.optifine.render.RenderEnv;
+import net.optifine.shaders.Shaders;
+import net.optifine.shaders.ShadersRender;
+import net.optifine.shaders.ShadowUtils;
+import net.optifine.shaders.gui.GuiShaderOptions;
+import net.optifine.util.ChunkUtils;
+import net.optifine.util.RenderChunkUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
+import org.lwjgl.input.Keyboard;
 import org.lwjgl.util.vector.Vector3f;
 import org.lwjgl.util.vector.Vector4f;

 public class RenderGlobal implements IWorldEventListener, IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation FORCEFIELD_TEXTURES = new ResourceLocation("textures/misc/forcefield.png");
-   private final Minecraft mc;
+   public final Minecraft mc;
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient world;
-   private Set<RenderChunk> chunksToUpdate = Sets.newLinkedHashSet();
+   private Set<RenderChunk> chunksToUpdate = new ObjectLinkedOpenHashSet();
    private List<RenderGlobal.ContainerLocalRenderInformation> renderInfos = Lists.newArrayListWithCapacity(69696);
    private final Set<TileEntity> setTileEntities = Sets.newHashSet();
    private ViewFrustum viewFrustum;
    private int starGLCallList = -1;
    private int glSkyList = -1;
    private int glSkyList2 = -1;
    private final VertexFormat vertexBufferFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private int cloudTickCounter;
-   private final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.newHashMap();
+   public final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.newHashMap();
    private final Map<BlockPos, ISound> mapSoundPositions = Maps.newHashMap();
    private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
    private Framebuffer entityOutlineFramebuffer;
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX = Double.MIN_VALUE;
    private double frustumUpdatePosY = Double.MIN_VALUE;
@@ -141,17 +175,44 @@
    private final Vector3d debugTerrainFrustumPosition = new Vector3d();
    private boolean vboEnabled;
    IRenderChunkFactory renderChunkFactory;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
-   private boolean displayListEntitiesDirty = true;
+   public boolean displayListEntitiesDirty = true;
    private boolean entityOutlinesRendered;
    private final Set<BlockPos> setLightUpdates = Sets.newHashSet();
+   private CloudRenderer cloudRenderer;
+   public Entity renderedEntity;
+   public Set chunksToResortTransparency = new LinkedHashSet();
+   public Set chunksToUpdateForced = new LinkedHashSet();
+   private Set<RenderChunk> chunksToUpdatePrev = new ObjectLinkedOpenHashSet();
+   private Deque visibilityDeque = new ArrayDeque();
+   private List renderInfosEntities = new ArrayList(1024);
+   private List renderInfosTileEntities = new ArrayList(1024);
+   private List renderInfosNormal = new ArrayList(1024);
+   private List renderInfosEntitiesNormal = new ArrayList(1024);
+   private List renderInfosTileEntitiesNormal = new ArrayList(1024);
+   private List renderInfosShadow = new ArrayList(1024);
+   private List renderInfosEntitiesShadow = new ArrayList(1024);
+   private List renderInfosTileEntitiesShadow = new ArrayList(1024);
+   private int renderDistance = 0;
+   private int renderDistanceSq = 0;
+   private static final Set SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(EnumFacing.VALUES)));
+   private int countTileEntitiesRendered;
+   private IChunkProvider worldChunkProvider = null;
+   private Long2ObjectMap<Chunk> worldChunkProviderMap = null;
+   private int countLoadedChunksPrev = 0;
+   private RenderEnv renderEnv = new RenderEnv(Blocks.AIR.getDefaultState(), new BlockPos(0, 0, 0));
+   public boolean renderOverlayDamaged = false;
+   public boolean renderOverlayEyes = false;
+   private boolean firstWorldLoad = false;
+   private static int renderEntitiesCounter = 0;

    public RenderGlobal(Minecraft var1) {
+      this.cloudRenderer = new CloudRenderer(var1);
       this.mc = var1;
       this.renderManager = var1.getRenderManager();
       this.renderEngine = var1.getTextureManager();
       this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
       GlStateManager.glTexParameteri(3553, 10242, 10497);
       GlStateManager.glTexParameteri(3553, 10243, 10497);
@@ -164,13 +225,13 @@
       } else {
          this.renderContainer = new RenderList();
          this.renderChunkFactory = new ListChunkFactory();
       }

       this.vertexBufferFormat = new VertexFormat();
-      this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
+      this.vertexBufferFormat.addElement(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.POSITION, 3));
       this.generateStars();
       this.generateSky();
       this.generateSky2();
    }

    public void onResourceManagerReload(IResourceManager var1) {
@@ -224,13 +285,15 @@
          this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
          GlStateManager.disableBlend();
       }
    }

    protected boolean isRenderEntityOutlines() {
-      return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.player != null;
+      return !Config.isFastRender() && !Config.isShaders() && !Config.isAntialiasing()
+         ? this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.player != null
+         : false;
    }

    private void generateSky2() {
       Tessellator var1 = Tessellator.getInstance();
       BufferBuilder var2 = var1.getBuffer();
       if (this.sky2VBO != null) {
@@ -285,26 +348,27 @@
    }

    private void renderSky(BufferBuilder var1, float var2, boolean var3) {
       byte var4 = 64;
       byte var5 = 6;
       var1.begin(7, DefaultVertexFormats.POSITION);
+      int var6 = (this.renderDistance / 64 + 1) * 64 + 64;

-      for (short var6 = -384; var6 <= 384; var6 += 64) {
-         for (short var7 = -384; var7 <= 384; var7 += 64) {
-            float var8 = var6;
-            float var9 = var6 + 64;
+      for (int var7 = -var6; var7 <= var6; var7 += 64) {
+         for (int var8 = -var6; var8 <= var6; var8 += 64) {
+            float var9 = var7;
+            float var10 = var7 + 64;
             if (var3) {
-               var9 = var6;
-               var8 = var6 + 64;
+               var10 = var7;
+               var9 = var7 + 64;
             }

-            var1.pos(var8, var2, var7).endVertex();
-            var1.pos(var9, var2, var7).endVertex();
-            var1.pos(var9, var2, var7 + 64).endVertex();
-            var1.pos(var8, var2, var7 + 64).endVertex();
+            var1.pos(var9, var2, var8).endVertex();
+            var1.pos(var10, var2, var8).endVertex();
+            var1.pos(var10, var2, var8 + 64).endVertex();
+            var1.pos(var9, var2, var8 + 64).endVertex();
          }
       }
    }

    private void generateStars() {
       Tessellator var1 = Tessellator.getInstance();
@@ -367,17 +431,17 @@
                double var39 = 0.0;
                double var41 = ((var38 & 2) - 1) * var10;
                double var43 = ((var38 + 1 & 2) - 1) * var10;
                double var45 = 0.0;
                double var47 = var41 * var36 - var43 * var34;
                double var49 = var43 * var36 + var41 * var34;
-               double var53 = var47 * var28 + 0.0 * var30;
-               double var55 = 0.0 * var28 - var47 * var30;
-               double var57 = var55 * var22 - var49 * var24;
-               double var61 = var49 * var22 + var55 * var24;
-               var1.pos(var14 + var57, var16 + var53, var18 + var61).endVertex();
+               double var51 = var47 * var28 + 0.0 * var30;
+               double var53 = 0.0 * var28 - var47 * var30;
+               double var55 = var53 * var22 - var49 * var24;
+               double var57 = var49 * var22 + var53 * var24;
+               var1.pos(var14 + var55, var16 + var51, var18 + var57).endVertex();
             }
          }
       }
    }

    public void setWorldAndLoadRenderers(@Nullable WorldClient var1) {
@@ -390,18 +454,28 @@
       this.frustumUpdatePosZ = Double.MIN_VALUE;
       this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
       this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
       this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
       this.renderManager.setWorld(var1);
       this.world = var1;
+      if (Config.isDynamicLights()) {
+         DynamicLights.clear();
+      }
+
+      ChunkVisibility.reset();
+      this.worldChunkProvider = null;
+      this.worldChunkProviderMap = null;
+      this.renderEnv.reset(null, null);
+      Shaders.checkWorldChanged(this.world);
       if (var1 != null) {
          var1.addEventListener(this);
          this.loadRenderers();
       } else {
          this.chunksToUpdate.clear();
-         this.renderInfos.clear();
+         this.chunksToUpdatePrev.clear();
+         this.clearRenderInfos();
          if (this.viewFrustum != null) {
             this.viewFrustum.deleteGlResources();
             this.viewFrustum = null;
          }

          if (this.renderDispatcher != null) {
@@ -416,31 +490,36 @@
       if (this.world != null) {
          if (this.renderDispatcher == null) {
             this.renderDispatcher = new ChunkRenderDispatcher();
          }

          this.displayListEntitiesDirty = true;
-         Blocks.LEAVES.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
-         Blocks.LEAVES2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
+         Blocks.LEAVES.setGraphicsLevel(Config.isTreesFancy());
+         Blocks.LEAVES2.setGraphicsLevel(Config.isTreesFancy());
+         BlockModelRenderer.updateAoLightValue();
+         if (Config.isDynamicLights()) {
+            DynamicLights.clear();
+         }
+
+         SmartAnimations.update();
          this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
+         this.renderDistance = this.renderDistanceChunks * 16;
+         this.renderDistanceSq = this.renderDistance * this.renderDistance;
          boolean var1 = this.vboEnabled;
          this.vboEnabled = OpenGlHelper.useVbo();
          if (var1 && !this.vboEnabled) {
             this.renderContainer = new RenderList();
             this.renderChunkFactory = new ListChunkFactory();
          } else if (!var1 && this.vboEnabled) {
             this.renderContainer = new VboRenderList();
             this.renderChunkFactory = new VboChunkFactory();
          }

-         if (var1 != this.vboEnabled) {
-            this.generateStars();
-            this.generateSky();
-            this.generateSky2();
-         }
-
+         this.generateStars();
+         this.generateSky();
+         this.generateSky2();
          if (this.viewFrustum != null) {
             this.viewFrustum.deleteGlResources();
          }

          this.stopChunkUpdates();
          synchronized (this.setTileEntities) {
@@ -454,115 +533,170 @@
                this.viewFrustum.updateChunkPositions(var5.posX, var5.posZ);
             }
          }

          this.renderEntitiesStartupCounter = 2;
       }
+
+      if (this.mc.player == null) {
+         this.firstWorldLoad = true;
+      }
    }

    protected void stopChunkUpdates() {
       this.chunksToUpdate.clear();
       this.renderDispatcher.stopChunkUpdates();
    }

    public void createBindEntityOutlineFbs(int var1, int var2) {
-      if (OpenGlHelper.shadersSupported) {
-         if (this.entityOutlineShader != null) {
-            this.entityOutlineShader.createBindFramebuffers(var1, var2);
-         }
+      if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null) {
+         this.entityOutlineShader.createBindFramebuffers(var1, var2);
       }
    }

    public void renderEntities(Entity var1, ICamera var2, float var3) {
+      int var4 = 0;
+      if (Reflector.MinecraftForgeClient_getRenderPass.exists()) {
+         var4 = Reflector.callInt(Reflector.MinecraftForgeClient_getRenderPass, new Object[0]);
+      }
+
       if (this.renderEntitiesStartupCounter > 0) {
+         if (var4 > 0) {
+            return;
+         }
+
          this.renderEntitiesStartupCounter--;
       } else {
-         double var4 = var1.prevPosX + (var1.posX - var1.prevPosX) * var3;
-         double var6 = var1.prevPosY + (var1.posY - var1.prevPosY) * var3;
-         double var8 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * var3;
+         double var5 = var1.prevPosX + (var1.posX - var1.prevPosX) * var3;
+         double var7 = var1.prevPosY + (var1.posY - var1.prevPosY) * var3;
+         double var9 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * var3;
          this.world.profiler.startSection("prepare");
          TileEntityRendererDispatcher.instance
             .prepare(this.world, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.objectMouseOver, var3);
          this.renderManager
             .cacheActiveRenderInfo(this.world, this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, var3);
-         this.countEntitiesTotal = 0;
-         this.countEntitiesRendered = 0;
-         this.countEntitiesHidden = 0;
-         Entity var10 = this.mc.getRenderViewEntity();
-         double var11 = var10.lastTickPosX + (var10.posX - var10.lastTickPosX) * var3;
-         double var13 = var10.lastTickPosY + (var10.posY - var10.lastTickPosY) * var3;
-         double var15 = var10.lastTickPosZ + (var10.posZ - var10.lastTickPosZ) * var3;
-         TileEntityRendererDispatcher.staticPlayerX = var11;
-         TileEntityRendererDispatcher.staticPlayerY = var13;
-         TileEntityRendererDispatcher.staticPlayerZ = var15;
-         this.renderManager.setRenderPosition(var11, var13, var15);
+         renderEntitiesCounter++;
+         if (var4 == 0) {
+            this.countEntitiesTotal = 0;
+            this.countEntitiesRendered = 0;
+            this.countEntitiesHidden = 0;
+            this.countTileEntitiesRendered = 0;
+         }
+
+         Entity var11 = this.mc.getRenderViewEntity();
+         double var12 = var11.lastTickPosX + (var11.posX - var11.lastTickPosX) * var3;
+         double var14 = var11.lastTickPosY + (var11.posY - var11.lastTickPosY) * var3;
+         double var16 = var11.lastTickPosZ + (var11.posZ - var11.lastTickPosZ) * var3;
+         TileEntityRendererDispatcher.staticPlayerX = var12;
+         TileEntityRendererDispatcher.staticPlayerY = var14;
+         TileEntityRendererDispatcher.staticPlayerZ = var16;
+         this.renderManager.setRenderPosition(var12, var14, var16);
          this.mc.entityRenderer.enableLightmap();
          this.world.profiler.endStartSection("global");
-         List var17 = this.world.getLoadedEntityList();
-         this.countEntitiesTotal = var17.size();
+         List var18 = this.world.getLoadedEntityList();
+         if (var4 == 0) {
+            this.countEntitiesTotal = var18.size();
+         }
+
+         if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
+            GlStateManager.disableFog();
+         }
+
+         boolean var19 = Reflector.ForgeEntity_shouldRenderInPass.exists();
+         boolean var20 = Reflector.ForgeTileEntity_shouldRenderInPass.exists();

-         for (int var18 = 0; var18 < this.world.weatherEffects.size(); var18++) {
-            Entity var19 = this.world.weatherEffects.get(var18);
-            this.countEntitiesRendered++;
-            if (var19.isInRangeToRender3d(var4, var6, var8)) {
-               this.renderManager.renderEntityStatic(var19, var3, false);
+         for (int var21 = 0; var21 < this.world.weatherEffects.size(); var21++) {
+            Entity var22 = (Entity)this.world.weatherEffects.get(var21);
+            if (!var19 || Reflector.callBoolean(var22, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{var4})) {
+               this.countEntitiesRendered++;
+               if (var22.isInRangeToRender3d(var5, var7, var9)) {
+                  this.renderManager.renderEntityStatic(var22, var3, false);
+               }
             }
          }

          this.world.profiler.endStartSection("entities");
-         ArrayList var31 = Lists.newArrayList();
-         ArrayList var32 = Lists.newArrayList();
-         BlockPos.PooledMutableBlockPos var20 = BlockPos.PooledMutableBlockPos.retain();
-
-         for (RenderGlobal.ContainerLocalRenderInformation var22 : this.renderInfos) {
-            Chunk var23 = this.world.getChunk(var22.renderChunk.getPosition());
-            ClassInheritanceMultiMap var24 = var23.getEntityLists()[var22.renderChunk.getPosition().getY() / 16];
-            if (!var24.isEmpty()) {
-               for (Entity var26 : var24) {
-                  boolean var27 = this.renderManager.shouldRender(var26, var2, var4, var6, var8) || var26.isRidingOrBeingRiddenBy(this.mc.player);
-                  if (var27) {
-                     boolean var28 = this.mc.getRenderViewEntity() instanceof EntityLivingBase
-                        ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping()
-                        : false;
-                     if ((var26 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || var28)
-                        && (!(var26.posY >= 0.0) || !(var26.posY < 256.0) || this.world.isBlockLoaded(var20.setPos(var26)))) {
-                        this.countEntitiesRendered++;
-                        this.renderManager.renderEntityStatic(var26, var3, false);
-                        if (this.isOutlineActive(var26, var10, var2)) {
-                           var31.add(var26);
-                        }
-
-                        if (this.renderManager.isRenderMultipass(var26)) {
-                           var32.add(var26);
+         boolean var36 = Config.isShaders();
+         if (var36) {
+            Shaders.beginEntities();
+         }
+
+         RenderItemFrame.updateItemRenderDistance();
+         ArrayList var37 = Lists.newArrayList();
+         ArrayList var23 = Lists.newArrayList();
+         PooledMutableBlockPos var24 = PooledMutableBlockPos.retain();
+         boolean var25 = Shaders.isShadowPass && !this.mc.player.isSpectator();
+
+         for (RenderGlobal.ContainerLocalRenderInformation var27 : this.renderInfosEntities) {
+            Chunk var28 = var27.renderChunk.getChunk();
+            ClassInheritanceMultiMap var29 = var28.getEntityLists()[var27.renderChunk.getPosition().getY() / 16];
+            if (!var29.isEmpty()) {
+               for (Entity var31 : var29) {
+                  if (!var19 || Reflector.callBoolean(var31, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{var4})) {
+                     boolean var32 = this.renderManager.shouldRender(var31, var2, var5, var7, var9) || var31.isRidingOrBeingRiddenBy(this.mc.player);
+                     if (var32) {
+                        boolean var33 = this.mc.getRenderViewEntity() instanceof EntityLivingBase
+                           ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping()
+                           : false;
+                        if ((var31 != this.mc.getRenderViewEntity() || var25 || this.mc.gameSettings.thirdPersonView != 0 || var33)
+                           && (var31.posY < 0.0 || var31.posY >= 256.0 || this.world.isBlockLoaded(var24.setPos(var31)))) {
+                           this.countEntitiesRendered++;
+                           this.renderedEntity = var31;
+                           if (var36) {
+                              Shaders.nextEntity(var31);
+                           }
+
+                           this.renderManager.renderEntityStatic(var31, var3, false);
+                           this.renderedEntity = null;
+                           if (this.isOutlineActive(var31, var11, var2)) {
+                              var37.add(var31);
+                           }
+
+                           if (this.renderManager.isRenderMultipass(var31)) {
+                              var23.add(var31);
+                           }
                         }
                      }
                   }
                }
             }
          }

-         var20.release();
-         if (!var32.isEmpty()) {
-            for (Entity var38 : var32) {
-               this.renderManager.renderMultipass(var38, var3);
+         var24.release();
+         if (!var23.isEmpty()) {
+            for (Entity var42 : var23) {
+               if (!var19 || Reflector.callBoolean(var42, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{var4})) {
+                  if (var36) {
+                     Shaders.nextEntity(var42);
+                  }
+
+                  this.renderManager.renderMultipass(var42, var3);
+               }
             }
          }

-         if (this.isRenderEntityOutlines() && (!var31.isEmpty() || this.entityOutlinesRendered)) {
+         if (var4 == 0 && this.isRenderEntityOutlines() && (!var37.isEmpty() || this.entityOutlinesRendered)) {
             this.world.profiler.endStartSection("entityOutlines");
             this.entityOutlineFramebuffer.framebufferClear();
-            this.entityOutlinesRendered = !var31.isEmpty();
-            if (!var31.isEmpty()) {
+            this.entityOutlinesRendered = !var37.isEmpty();
+            if (!var37.isEmpty()) {
                GlStateManager.depthFunc(519);
                GlStateManager.disableFog();
                this.entityOutlineFramebuffer.bindFramebuffer(false);
                RenderHelper.disableStandardItemLighting();
                this.renderManager.setRenderOutlines(true);

-               for (int var34 = 0; var34 < var31.size(); var34++) {
-                  this.renderManager.renderEntityStatic((Entity)var31.get(var34), var3, false);
+               for (int var39 = 0; var39 < var37.size(); var39++) {
+                  Entity var43 = (Entity)var37.get(var39);
+                  if (!var19 || Reflector.callBoolean(var43, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{var4})) {
+                     if (var36) {
+                        Shaders.nextEntity(var43);
+                     }
+
+                     this.renderManager.renderEntityStatic(var43, var3, false);
+                  }
                }

                this.renderManager.setRenderOutlines(false);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.depthMask(false);
                this.entityOutlineShader.render(var3);
@@ -576,55 +710,138 @@
                GlStateManager.enableAlpha();
             }

             this.mc.getFramebuffer().bindFramebuffer(false);
          }

+         if (!this.isRenderEntityOutlines() && (!var37.isEmpty() || this.entityOutlinesRendered)) {
+            this.world.profiler.endStartSection("entityOutlines");
+            this.entityOutlinesRendered = !var37.isEmpty();
+            if (!var37.isEmpty()) {
+               if (var36) {
+                  Shaders.beginEntitiesGlowing();
+               }
+
+               GlStateManager.disableFog();
+               GlStateManager.disableDepth();
+               this.mc.entityRenderer.disableLightmap();
+               RenderHelper.disableStandardItemLighting();
+               this.renderManager.setRenderOutlines(true);
+
+               for (int var40 = 0; var40 < var37.size(); var40++) {
+                  Entity var44 = (Entity)var37.get(var40);
+                  if (!var19 || Reflector.callBoolean(var44, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{var4})) {
+                     if (var36) {
+                        Shaders.nextEntity(var44);
+                     }
+
+                     this.renderManager.renderEntityStatic(var44, var3, false);
+                  }
+               }
+
+               this.renderManager.setRenderOutlines(false);
+               RenderHelper.enableStandardItemLighting();
+               this.mc.entityRenderer.enableLightmap();
+               GlStateManager.enableDepth();
+               GlStateManager.enableFog();
+               if (var36) {
+                  Shaders.endEntitiesGlowing();
+               }
+            }
+         }
+
+         if (var36) {
+            Shaders.endEntities();
+            Shaders.beginBlockEntities();
+         }
+
          this.world.profiler.endStartSection("blockentities");
          RenderHelper.enableStandardItemLighting();
+         if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
+            TileEntityRendererDispatcher.instance.preDrawBatch();
+         }
+
+         TileEntitySignRenderer.updateTextRenderDistance();
+
+         for (RenderGlobal.ContainerLocalRenderInformation var45 : this.renderInfosTileEntities) {
+            List var48 = var45.renderChunk.getCompiledChunk().getTileEntities();
+            if (!var48.isEmpty()) {
+               for (TileEntity var54 : var48) {
+                  if (var20) {
+                     if (!Reflector.callBoolean(var54, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[]{var4})) {
+                        continue;
+                     }
+
+                     AxisAlignedBB var56 = (AxisAlignedBB)Reflector.call(var54, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
+                     if (var56 != null && !var2.isBoundingBoxInFrustum(var56)) {
+                        continue;
+                     }
+                  }
+
+                  if (var36) {
+                     Shaders.nextBlockEntity(var54);
+                  }

-         for (RenderGlobal.ContainerLocalRenderInformation var39 : this.renderInfos) {
-            List var42 = var39.renderChunk.getCompiledChunk().getTileEntities();
-            if (!var42.isEmpty()) {
-               for (TileEntity var47 : var42) {
-                  TileEntityRendererDispatcher.instance.render(var47, var3, -1);
+                  TileEntityRendererDispatcher.instance.render(var54, var3, -1);
+                  this.countTileEntitiesRendered++;
                }
             }
          }

          synchronized (this.setTileEntities) {
-            for (TileEntity var43 : this.setTileEntities) {
-               TileEntityRendererDispatcher.instance.render(var43, var3, -1);
+            for (TileEntity var52 : this.setTileEntities) {
+               if (!var20 || Reflector.callBoolean(var52, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[]{var4})) {
+                  if (var36) {
+                     Shaders.nextBlockEntity(var52);
+                  }
+
+                  TileEntityRendererDispatcher.instance.render(var52, var3, -1);
+               }
             }
          }

+         if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
+            TileEntityRendererDispatcher.instance.drawBatch(var4);
+         }
+
+         this.renderOverlayDamaged = true;
          this.preRenderDamagedBlocks();

-         for (DestroyBlockProgress var41 : this.damagedBlocks.values()) {
-            BlockPos var44 = var41.getPosition();
-            if (this.world.getBlockState(var44).getBlock().hasTileEntity()) {
-               TileEntity var46 = this.world.getTileEntity(var44);
-               if (var46 instanceof TileEntityChest) {
-                  TileEntityChest var48 = (TileEntityChest)var46;
-                  if (var48.adjacentChestXNeg != null) {
-                     var44 = var44.offset(EnumFacing.WEST);
-                     var46 = this.world.getTileEntity(var44);
-                  } else if (var48.adjacentChestZNeg != null) {
-                     var44 = var44.offset(EnumFacing.NORTH);
-                     var46 = this.world.getTileEntity(var44);
+         for (DestroyBlockProgress var50 : this.damagedBlocks.values()) {
+            BlockPos var53 = var50.getPosition();
+            if (this.world.getBlockState(var53).getBlock().hasTileEntity()) {
+               TileEntity var55 = this.world.getTileEntity(var53);
+               if (var55 instanceof TileEntityChest) {
+                  TileEntityChest var57 = (TileEntityChest)var55;
+                  if (var57.adjacentChestXNeg != null) {
+                     var53 = var53.offset(EnumFacing.WEST);
+                     var55 = this.world.getTileEntity(var53);
+                  } else if (var57.adjacentChestZNeg != null) {
+                     var53 = var53.offset(EnumFacing.NORTH);
+                     var55 = this.world.getTileEntity(var53);
                   }
                }

-               IBlockState var49 = this.world.getBlockState(var44);
-               if (var46 != null && var49.hasCustomBreakingProgress()) {
-                  TileEntityRendererDispatcher.instance.render(var46, var3, var41.getPartialBlockDamage());
+               IBlockState var58 = this.world.getBlockState(var53);
+               if (var55 != null && var58.h()) {
+                  if (var36) {
+                     Shaders.nextBlockEntity(var55);
+                  }
+
+                  TileEntityRendererDispatcher.instance.render(var55, var3, var50.getPartialBlockDamage());
                }
             }
          }

          this.postRenderDamagedBlocks();
+         this.renderOverlayDamaged = false;
+         if (var36) {
+            Shaders.endBlockEntities();
+         }
+
+         renderEntitiesCounter--;
          this.mc.entityRenderer.disableLightmap();
          this.mc.profiler.endSection();
       }
    }

    private boolean isOutlineActive(Entity var1, Entity var2, ICamera var3) {
@@ -665,13 +882,13 @@
       }

       return var1;
    }

    public String getDebugInfoEntities() {
-      return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden;
+      return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", " + Config.getVersionDebug();
    }

    public void setupTerrain(Entity var1, double var2, ICamera var4, int var5, boolean var6) {
       if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
          this.loadRenderers();
       }
@@ -690,12 +907,16 @@
          this.frustumUpdatePosChunkX = var1.chunkCoordX;
          this.frustumUpdatePosChunkY = var1.chunkCoordY;
          this.frustumUpdatePosChunkZ = var1.chunkCoordZ;
          this.viewFrustum.updateChunkPositions(var1.posX, var1.posZ);
       }

+      if (Config.isDynamicLights()) {
+         DynamicLights.update(this);
+      }
+
       this.world.profiler.endStartSection("renderlistcamera");
       double var13 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var2;
       double var15 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var2;
       double var17 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var2;
       this.renderContainer.initialize(var13, var15, var17);
       this.world.profiler.endStartSection("cull");
@@ -703,15 +924,15 @@
          Frustum var19 = new Frustum(this.debugFixedClippingHelper);
          var19.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
          var4 = var19;
       }

       this.mc.profiler.endStartSection("culling");
-      BlockPos var34 = new BlockPos(var13, var15 + var1.getEyeHeight(), var17);
-      RenderChunk var20 = this.viewFrustum.getRenderChunk(var34);
-      BlockPos var21 = new BlockPos(MathHelper.floor(var13 / 16.0) * 16, MathHelper.floor(var15 / 16.0) * 16, MathHelper.floor(var17 / 16.0) * 16);
+      BlockPos var40 = new BlockPos(var13, var15 + var1.getEyeHeight(), var17);
+      RenderChunk var20 = this.viewFrustum.getRenderChunk(var40);
+      new BlockPos(MathHelper.floor(var13 / 16.0) * 16, MathHelper.floor(var15 / 16.0) * 16, MathHelper.floor(var17 / 16.0) * 16);
       this.displayListEntitiesDirty = this.displayListEntitiesDirty
          || !this.chunksToUpdate.isEmpty()
          || var1.posX != this.lastViewEntityX
          || var1.posY != this.lastViewEntityY
          || var1.posZ != this.lastViewEntityZ
          || var1.rotationPitch != this.lastViewEntityPitch
@@ -720,74 +941,144 @@
       this.lastViewEntityY = var1.posY;
       this.lastViewEntityZ = var1.posZ;
       this.lastViewEntityPitch = var1.rotationPitch;
       this.lastViewEntityYaw = var1.rotationYaw;
       boolean var22 = this.debugFixedClippingHelper != null;
       this.mc.profiler.endStartSection("update");
-      if (!var22 && this.displayListEntitiesDirty) {
+      Lagometer.timerVisibility.start();
+      int var23 = this.getCountLoadedChunks();
+      if (var23 != this.countLoadedChunksPrev) {
+         this.countLoadedChunksPrev = var23;
+         this.displayListEntitiesDirty = true;
+      }
+
+      int var24 = 256;
+      if (!ChunkVisibility.isFinished()) {
+         this.displayListEntitiesDirty = true;
+      }
+
+      if (!var22 && this.displayListEntitiesDirty && Config.isIntegratedServerRunning()) {
+         var24 = ChunkVisibility.getMaxChunkY(this.world, var1, this.renderDistanceChunks);
+      }
+
+      RenderChunk var25 = this.viewFrustum.getRenderChunk(new BlockPos(var1.posX, var1.posY, var1.posZ));
+      if (Shaders.isShadowPass) {
+         this.renderInfos = this.renderInfosShadow;
+         this.renderInfosEntities = this.renderInfosEntitiesShadow;
+         this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
+         if (!var22 && this.displayListEntitiesDirty) {
+            this.clearRenderInfos();
+            if (var25 != null && var25.getPosition().getY() > var24) {
+               this.renderInfosEntities.add(var25.getRenderInfo());
+            }
+
+            Iterator var26 = ShadowUtils.makeShadowChunkIterator(this.world, var2, var1, this.renderDistanceChunks, this.viewFrustum);
+
+            while (var26.hasNext()) {
+               RenderChunk var27 = (RenderChunk)var26.next();
+               if (var27 != null && var27.getPosition().getY() <= var24) {
+                  RenderGlobal.ContainerLocalRenderInformation var28 = var27.getRenderInfo();
+                  if (!var27.compiledChunk.isEmpty()) {
+                     this.renderInfos.add(var28);
+                  }
+
+                  if (ChunkUtils.hasEntities(var27.getChunk())) {
+                     this.renderInfosEntities.add(var28);
+                  }
+
+                  if (var27.getCompiledChunk().getTileEntities().size() > 0) {
+                     this.renderInfosTileEntities.add(var28);
+                  }
+               }
+            }
+         }
+      } else {
+         this.renderInfos = this.renderInfosNormal;
+         this.renderInfosEntities = this.renderInfosEntitiesNormal;
+         this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
+      }
+
+      if (!var22 && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
          this.displayListEntitiesDirty = false;
-         this.renderInfos = Lists.newArrayList();
-         ArrayDeque var23 = Queues.newArrayDeque();
+         this.clearRenderInfos();
+         this.visibilityDeque.clear();
+         Deque var41 = this.visibilityDeque;
          Entity.setRenderDistanceWeight(MathHelper.clamp(this.mc.gameSettings.renderDistanceChunks / 8.0, 1.0, 2.5));
-         boolean var24 = this.mc.renderChunksMany;
-         if (var20 != null) {
-            boolean var37 = false;
-            RenderGlobal.ContainerLocalRenderInformation var40 = new RenderGlobal.ContainerLocalRenderInformation(var20, null, 0);
-            Set var43 = this.getVisibleFacings(var34);
-            if (var43.size() == 1) {
-               Vector3f var46 = this.getViewVector(var1, var2);
-               EnumFacing var29 = EnumFacing.getFacingFromVector(var46.x, var46.y, var46.z).getOpposite();
-               var43.remove(var29);
+         boolean var43 = this.mc.renderChunksMany;
+         if (var20 != null && var20.getPosition().getY() <= var24) {
+            boolean var46 = false;
+            RenderGlobal.ContainerLocalRenderInformation var49 = new RenderGlobal.ContainerLocalRenderInformation(var20, (EnumFacing)null, 0);
+            Set var52 = SET_ALL_FACINGS;
+            if (var52.size() == 1) {
+               Vector3f var55 = this.getViewVector(var1, var2);
+               EnumFacing var58 = EnumFacing.getFacingFromVector(var55.x, var55.y, var55.z).getOpposite();
+               var52.remove(var58);
             }

-            if (var43.isEmpty()) {
-               var37 = true;
+            if (var52.isEmpty()) {
+               var46 = true;
             }

-            if (var37 && !var6) {
-               this.renderInfos.add(var40);
+            if (var46 && !var6) {
+               this.renderInfos.add(var49);
             } else {
-               if (var6 && this.world.getBlockState(var34).isOpaqueCube()) {
-                  var24 = false;
+               if (var6 && this.world.getBlockState(var40).p()) {
+                  var43 = false;
                }

                var20.setFrameIndex(var5);
-               var23.add(var40);
+               var41.add(var49);
             }
          } else {
-            int var25 = var34.getY() > 0 ? 248 : 8;
+            int var45 = var40.getY() > 0 ? Math.min(var24, 248) : 8;
+            if (var25 != null) {
+               this.renderInfosEntities.add(var25.getRenderInfo());
+            }

-            for (int var26 = -this.renderDistanceChunks; var26 <= this.renderDistanceChunks; var26++) {
-               for (int var27 = -this.renderDistanceChunks; var27 <= this.renderDistanceChunks; var27++) {
-                  RenderChunk var28 = this.viewFrustum.getRenderChunk(new BlockPos((var26 << 4) + 8, var25, (var27 << 4) + 8));
-                  if (var28 != null && ((ICamera)var4).isBoundingBoxInFrustum(var28.boundingBox)) {
-                     var28.setFrameIndex(var5);
-                     var23.add(new RenderGlobal.ContainerLocalRenderInformation(var28, null, 0));
+            for (int var29 = -this.renderDistanceChunks; var29 <= this.renderDistanceChunks; var29++) {
+               for (int var30 = -this.renderDistanceChunks; var30 <= this.renderDistanceChunks; var30++) {
+                  RenderChunk var31 = this.viewFrustum.getRenderChunk(new BlockPos((var29 << 4) + 8, var45, (var30 << 4) + 8));
+                  if (var31 != null && var31.isBoundingBoxInFrustum((ICamera)var4, var5)) {
+                     var31.setFrameIndex(var5);
+                     RenderGlobal.ContainerLocalRenderInformation var32 = var31.getRenderInfo();
+                     var32.initialize(null, 0);
+                     var41.add(var32);
                   }
                }
             }
          }

          this.mc.profiler.startSection("iteration");
+         boolean var47 = Config.isFogOn();
+
+         while (!var41.isEmpty()) {
+            RenderGlobal.ContainerLocalRenderInformation var50 = (RenderGlobal.ContainerLocalRenderInformation)var41.poll();
+            RenderChunk var53 = var50.renderChunk;
+            EnumFacing var56 = var50.facing;
+            CompiledChunk var59 = var53.compiledChunk;
+            if (!var59.isEmpty() || var53.needsUpdate()) {
+               this.renderInfos.add(var50);
+            }
+
+            if (ChunkUtils.hasEntities(var53.getChunk())) {
+               this.renderInfosEntities.add(var50);
+            }

-         while (!var23.isEmpty()) {
-            RenderGlobal.ContainerLocalRenderInformation var38 = (RenderGlobal.ContainerLocalRenderInformation)var23.poll();
-            RenderChunk var41 = var38.renderChunk;
-            EnumFacing var44 = var38.facing;
-            this.renderInfos.add(var38);
-
-            for (EnumFacing var31 : EnumFacing.values()) {
-               RenderChunk var32 = this.getRenderChunkOffset(var21, var41, var31);
-               if ((!var24 || !var38.hasDirection(var31.getOpposite()))
-                  && (!var24 || var44 == null || var41.getCompiledChunk().isVisible(var44.getOpposite(), var31))
-                  && var32 != null
-                  && var32.setFrameIndex(var5)
-                  && ((ICamera)var4).isBoundingBoxInFrustum(var32.boundingBox)) {
-                  RenderGlobal.ContainerLocalRenderInformation var33 = new RenderGlobal.ContainerLocalRenderInformation(var32, var31, var38.counter + 1);
-                  var33.setDirection(var38.setFacing, var31);
-                  var23.add(var33);
+            if (var59.getTileEntities().size() > 0) {
+               this.renderInfosTileEntities.add(var50);
+            }
+
+            for (EnumFacing var36 : var43 ? ChunkVisibility.getFacingsNotOpposite(var50.setFacing) : EnumFacing.VALUES) {
+               if (!var43 || var56 == null || var59.isVisible(var56.getOpposite(), var36)) {
+                  RenderChunk var37 = this.getRenderChunkOffset(var40, var53, var36, var47, var24);
+                  if (var37 != null && var37.setFrameIndex(var5) && var37.isBoundingBoxInFrustum((ICamera)var4, var5)) {
+                     int var38 = var50.setFacing | 1 << var36.ordinal();
+                     RenderGlobal.ContainerLocalRenderInformation var39 = var37.getRenderInfo();
+                     var39.initialize(var36, var38);
+                     var41.add(var39);
+                  }
                }
             }
          }

          this.mc.profiler.endSection();
       }
@@ -795,60 +1086,81 @@
       this.mc.profiler.endStartSection("captureFrustum");
       if (this.debugFixTerrainFrustum) {
          this.fixTerrainFrustum(var13, var15, var17);
          this.debugFixTerrainFrustum = false;
       }

-      this.mc.profiler.endStartSection("rebuildNear");
-      Set var35 = this.chunksToUpdate;
-      this.chunksToUpdate = Sets.newLinkedHashSet();
-
-      for (RenderGlobal.ContainerLocalRenderInformation var39 : this.renderInfos) {
-         RenderChunk var42 = var39.renderChunk;
-         if (var42.needsUpdate() || var35.contains(var42)) {
-            this.displayListEntitiesDirty = true;
-            BlockPos var45 = var42.getPosition().add(8, 8, 8);
-            boolean var48 = var45.distanceSq(var34) < 768.0;
-            if (!var42.needsImmediateUpdate() && !var48) {
-               this.chunksToUpdate.add(var42);
-            } else {
-               this.mc.profiler.startSection("build near");
-               this.renderDispatcher.updateChunkNow(var42);
-               var42.clearNeedsUpdate();
-               this.mc.profiler.endSection();
+      Lagometer.timerVisibility.end();
+      if (Shaders.isShadowPass) {
+         Shaders.mcProfilerEndSection();
+      } else {
+         this.mc.profiler.endStartSection("rebuildNear");
+         Set var42 = this.chunksToUpdate;
+         this.chunksToUpdate = this.chunksToUpdatePrev;
+         this.chunksToUpdatePrev = var42;
+         this.chunksToUpdate.clear();
+         Lagometer.timerChunkUpdate.start();
+
+         for (RenderGlobal.ContainerLocalRenderInformation var48 : this.renderInfos) {
+            RenderChunk var51 = var48.renderChunk;
+            if (var51.needsUpdate() || var42.contains(var51)) {
+               this.displayListEntitiesDirty = true;
+               BlockPos var54 = var51.getPosition();
+               boolean var57 = var40.distanceSq(var54.getX() + 8, var54.getY() + 8, var54.getZ() + 8) < 768.0;
+               if (!var57) {
+                  this.chunksToUpdate.add(var51);
+               } else if (!var51.isPlayerUpdate()) {
+                  this.chunksToUpdateForced.add(var51);
+               } else {
+                  this.mc.profiler.startSection("build near");
+                  this.renderDispatcher.updateChunkNow(var51);
+                  var51.clearNeedsUpdate();
+                  this.mc.profiler.endSection();
+               }
             }
          }
-      }

-      this.chunksToUpdate.addAll(var35);
-      this.mc.profiler.endSection();
+         Lagometer.timerChunkUpdate.end();
+         this.chunksToUpdate.addAll(var42);
+         this.mc.profiler.endSection();
+      }
    }

    private Set<EnumFacing> getVisibleFacings(BlockPos var1) {
       VisGraph var2 = new VisGraph();
       BlockPos var3 = new BlockPos(var1.getX() >> 4 << 4, var1.getY() >> 4 << 4, var1.getZ() >> 4 << 4);
       Chunk var4 = this.world.getChunk(var3);

-      for (BlockPos.MutableBlockPos var6 : BlockPos.getAllInBoxMutable(var3, var3.add(15, 15, 15))) {
-         if (var4.getBlockState(var6).isOpaqueCube()) {
+      for (MutableBlockPos var6 : BlockPos.getAllInBoxMutable(var3, var3.add(15, 15, 15))) {
+         if (var4.getBlockState(var6).p()) {
             var2.setOpaqueCube(var6);
          }
       }

       return var2.getVisibleFacings(var1);
    }

    @Nullable
-   private RenderChunk getRenderChunkOffset(BlockPos var1, RenderChunk var2, EnumFacing var3) {
-      BlockPos var4 = var2.getBlockPosOffset16(var3);
-      if (MathHelper.abs(var1.getX() - var4.getX()) > this.renderDistanceChunks * 16) {
+   private RenderChunk getRenderChunkOffset(BlockPos var1, RenderChunk var2, EnumFacing var3, boolean var4, int var5) {
+      RenderChunk var6 = var2.getRenderChunkNeighbour(var3);
+      if (var6 == null) {
          return null;
-      } else if (var4.getY() < 0 || var4.getY() >= 256) {
+      } else if (var6.getPosition().getY() > var5) {
          return null;
       } else {
-         return MathHelper.abs(var1.getZ() - var4.getZ()) > this.renderDistanceChunks * 16 ? null : this.viewFrustum.getRenderChunk(var4);
+         if (var4) {
+            BlockPos var7 = var6.getPosition();
+            int var8 = var1.getX() - var7.getX();
+            int var9 = var1.getZ() - var7.getZ();
+            int var10 = var8 * var8 + var9 * var9;
+            if (var10 > this.renderDistanceSq) {
+               return null;
+            }
+         }
+
+         return var6;
       }
    }

    private void fixTerrainFrustum(double var1, double var3, double var5) {
       this.debugFixedClippingHelper = new ClippingHelperImpl();
       ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
@@ -893,26 +1205,27 @@
       float var9 = MathHelper.sin(-var4 * (float) (Math.PI / 180.0));
       return new Vector3f(var7 * var8, var9, var6 * var8);
    }

    public int renderBlockLayer(BlockRenderLayer var1, double var2, int var4, Entity var5) {
       RenderHelper.disableStandardItemLighting();
-      if (var1 == BlockRenderLayer.TRANSLUCENT) {
+      if (var1 == BlockRenderLayer.TRANSLUCENT && !Shaders.isShadowPass) {
          this.mc.profiler.startSection("translucent_sort");
          double var6 = var5.posX - this.prevRenderSortX;
          double var8 = var5.posY - this.prevRenderSortY;
          double var10 = var5.posZ - this.prevRenderSortZ;
          if (var6 * var6 + var8 * var8 + var10 * var10 > 1.0) {
             this.prevRenderSortX = var5.posX;
             this.prevRenderSortY = var5.posY;
             this.prevRenderSortZ = var5.posZ;
             int var12 = 0;
+            this.chunksToResortTransparency.clear();

             for (RenderGlobal.ContainerLocalRenderInformation var14 : this.renderInfos) {
                if (var14.renderChunk.compiledChunk.isLayerStarted(var1) && var12++ < 15) {
-                  this.renderDispatcher.updateTransparencyLater(var14.renderChunk);
+                  this.chunksToResortTransparency.add(var14.renderChunk);
                }
             }
          }

          this.mc.profiler.endSection();
       }
@@ -929,16 +1242,25 @@
          if (!var18.getCompiledChunk().isLayerEmpty(var1)) {
             var15++;
             this.renderContainer.addRenderChunk(var18, var1);
          }
       }

-      this.mc.profiler.func_194339_b(() -> "render_" + var1);
-      this.renderBlockLayer(var1);
-      this.mc.profiler.endSection();
-      return var15;
+      if (var15 == 0) {
+         this.mc.profiler.endSection();
+         return var15;
+      } else {
+         if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
+            GlStateManager.disableFog();
+         }
+
+         this.mc.profiler.func_194339_b(() -> "render_" + var1);
+         this.renderBlockLayer(var1);
+         this.mc.profiler.endSection();
+         return var15;
+      }
    }

    private void renderBlockLayer(BlockRenderLayer var1) {
       this.mc.entityRenderer.enableLightmap();
       if (OpenGlHelper.useVbo()) {
          GlStateManager.glEnableClientState(32884);
@@ -947,23 +1269,31 @@
          OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
          GlStateManager.glEnableClientState(32888);
          OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
          GlStateManager.glEnableClientState(32886);
       }

+      if (Config.isShaders()) {
+         ShadersRender.preRenderChunkLayer(var1);
+      }
+
       this.renderContainer.renderChunkLayer(var1);
+      if (Config.isShaders()) {
+         ShadersRender.postRenderChunkLayer(var1);
+      }
+
       if (OpenGlHelper.useVbo()) {
-         for (VertexFormatElement var4 : DefaultVertexFormats.BLOCK.getElements()) {
-            VertexFormatElement.EnumUsage var5 = var4.getUsage();
-            int var6 = var4.getIndex();
-            switch (var5) {
+         for (VertexFormatElement var3 : DefaultVertexFormats.BLOCK.getElements()) {
+            EnumUsage var4 = var3.getUsage();
+            int var5 = var3.getIndex();
+            switch (var4) {
                case POSITION:
                   GlStateManager.glDisableClientState(32884);
                   break;
                case UV:
-                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var6);
+                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var5);
                   GlStateManager.glDisableClientState(32888);
                   OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                   break;
                case COLOR:
                   GlStateManager.glDisableClientState(32886);
                   GlStateManager.resetColor();
@@ -982,197 +1312,289 @@
             var1.remove();
          }
       }
    }

    public void updateClouds() {
+      if (Config.isShaders()) {
+         if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(24)) {
+            GuiShaderOptions var1 = new GuiShaderOptions(null, Config.getGameSettings());
+            Config.getMinecraft().displayGuiScreen(var1);
+         }
+
+         if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
+            Shaders.uninit();
+            Shaders.loadShaderPack();
+            Reflector.Minecraft_actionKeyF3.setValue(this.mc, Boolean.TRUE);
+         }
+      }
+
       this.cloudTickCounter++;
       if (this.cloudTickCounter % 20 == 0) {
          this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
       }

       if (!this.setLightUpdates.isEmpty() && !this.renderDispatcher.hasNoFreeRenderBuilders() && this.chunksToUpdate.isEmpty()) {
-         Iterator var1 = this.setLightUpdates.iterator();
+         Iterator var6 = this.setLightUpdates.iterator();

-         while (var1.hasNext()) {
-            BlockPos var2 = (BlockPos)var1.next();
-            var1.remove();
+         while (var6.hasNext()) {
+            BlockPos var2 = (BlockPos)var6.next();
+            var6.remove();
             int var3 = var2.getX();
             int var4 = var2.getY();
             int var5 = var2.getZ();
             this.markBlocksForUpdate(var3 - 1, var4 - 1, var5 - 1, var3 + 1, var4 + 1, var5 + 1, false);
          }
       }
    }

    private void renderSkyEnd() {
-      GlStateManager.disableFog();
-      GlStateManager.disableAlpha();
-      GlStateManager.enableBlend();
-      GlStateManager.tryBlendFuncSeparate(
-         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
-      );
-      RenderHelper.disableStandardItemLighting();
-      GlStateManager.depthMask(false);
-      this.renderEngine.bindTexture(END_SKY_TEXTURES);
-      Tessellator var1 = Tessellator.getInstance();
-      BufferBuilder var2 = var1.getBuffer();
+      if (Config.isSkyEnabled()) {
+         GlStateManager.disableFog();
+         GlStateManager.disableAlpha();
+         GlStateManager.enableBlend();
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.SRC_ALPHA,
+            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+            GlStateManager.SourceFactor.ONE,
+            GlStateManager.DestFactor.ZERO
+         );
+         RenderHelper.disableStandardItemLighting();
+         GlStateManager.depthMask(false);
+         this.renderEngine.bindTexture(END_SKY_TEXTURES);
+         Tessellator var1 = Tessellator.getInstance();
+         BufferBuilder var2 = var1.getBuffer();

-      for (int var3 = 0; var3 < 6; var3++) {
-         GlStateManager.pushMatrix();
-         if (var3 == 1) {
-            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
-         }
+         for (int var3 = 0; var3 < 6; var3++) {
+            GlStateManager.pushMatrix();
+            if (var3 == 1) {
+               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
+            }

-         if (var3 == 2) {
-            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
-         }
+            if (var3 == 2) {
+               GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
+            }

-         if (var3 == 3) {
-            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
-         }
+            if (var3 == 3) {
+               GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
+            }

-         if (var3 == 4) {
-            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
-         }
+            if (var3 == 4) {
+               GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
+            }
+
+            if (var3 == 5) {
+               GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
+            }
+
+            var2.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
+            int var4 = 40;
+            int var5 = 40;
+            int var6 = 40;
+            if (Config.isCustomColors()) {
+               Vec3d var7 = new Vec3d(var4 / 255.0, var5 / 255.0, var6 / 255.0);
+               var7 = CustomColors.getWorldSkyColor(var7, this.world, this.mc.getRenderViewEntity(), 0.0F);
+               var4 = (int)(var7.x * 255.0);
+               var5 = (int)(var7.y * 255.0);
+               var6 = (int)(var7.z * 255.0);
+            }

-         if (var3 == 5) {
-            GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
+            var2.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(var4, var5, var6, 255).endVertex();
+            var2.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(var4, var5, var6, 255).endVertex();
+            var2.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(var4, var5, var6, 255).endVertex();
+            var2.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(var4, var5, var6, 255).endVertex();
+            var1.draw();
+            GlStateManager.popMatrix();
          }

-         var2.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-         var2.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(40, 40, 40, 255).endVertex();
-         var2.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(40, 40, 40, 255).endVertex();
-         var2.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(40, 40, 40, 255).endVertex();
-         var2.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(40, 40, 40, 255).endVertex();
-         var1.draw();
-         GlStateManager.popMatrix();
+         GlStateManager.depthMask(true);
+         GlStateManager.enableTexture2D();
+         GlStateManager.enableAlpha();
+         GlStateManager.disableBlend();
       }
-
-      GlStateManager.depthMask(true);
-      GlStateManager.enableTexture2D();
-      GlStateManager.enableAlpha();
    }

    public void renderSky(float var1, int var2) {
-      if (this.mc.world.provider.getDimensionType().getId() == 1) {
+      if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
+         WorldProvider var3 = this.mc.world.provider;
+         Object var4 = Reflector.call(var3, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
+         if (var4 != null) {
+            Reflector.callVoid(var4, Reflector.IRenderHandler_render, new Object[]{var1, this.world, this.mc});
+            return;
+         }
+      }
+
+      if (this.mc.world.provider.getDimensionType() == DimensionType.THE_END) {
          this.renderSkyEnd();
       } else if (this.mc.world.provider.isSurfaceWorld()) {
          GlStateManager.disableTexture2D();
-         Vec3d var3 = this.world.getSkyColor(this.mc.getRenderViewEntity(), var1);
-         float var4 = (float)var3.x;
-         float var5 = (float)var3.y;
-         float var6 = (float)var3.z;
+         boolean var20 = Config.isShaders();
+         if (var20) {
+            Shaders.disableTexture2D();
+         }
+
+         Vec3d var21 = this.world.getSkyColor(this.mc.getRenderViewEntity(), var1);
+         var21 = CustomColors.getSkyColor(
+            var21, this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ
+         );
+         if (var20) {
+            Shaders.setSkyColor(var21);
+         }
+
+         float var5 = (float)var21.x;
+         float var6 = (float)var21.y;
+         float var7 = (float)var21.z;
          if (var2 != 2) {
-            float var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
-            float var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
-            float var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
-            var4 = var7;
+            float var8 = (var5 * 30.0F + var6 * 59.0F + var7 * 11.0F) / 100.0F;
+            float var9 = (var5 * 30.0F + var6 * 70.0F) / 100.0F;
+            float var10 = (var5 * 30.0F + var7 * 70.0F) / 100.0F;
             var5 = var8;
             var6 = var9;
+            var7 = var10;
          }

-         GlStateManager.color(var4, var5, var6);
-         Tessellator var20 = Tessellator.getInstance();
-         BufferBuilder var21 = var20.getBuffer();
+         GlStateManager.color(var5, var6, var7);
+         Tessellator var23 = Tessellator.getInstance();
+         BufferBuilder var24 = var23.getBuffer();
          GlStateManager.depthMask(false);
          GlStateManager.enableFog();
-         GlStateManager.color(var4, var5, var6);
-         if (this.vboEnabled) {
-            this.skyVBO.bindBuffer();
-            GlStateManager.glEnableClientState(32884);
-            GlStateManager.glVertexPointer(3, 5126, 12, 0);
-            this.skyVBO.drawArrays(7);
-            this.skyVBO.unbindBuffer();
-            GlStateManager.glDisableClientState(32884);
-         } else {
-            GlStateManager.callList(this.glSkyList);
+         if (var20) {
+            Shaders.enableFog();
+         }
+
+         GlStateManager.color(var5, var6, var7);
+         if (var20) {
+            Shaders.preSkyList();
+         }
+
+         if (Config.isSkyEnabled()) {
+            if (this.vboEnabled) {
+               this.skyVBO.bindBuffer();
+               GlStateManager.glEnableClientState(32884);
+               GlStateManager.glVertexPointer(3, 5126, 12, 0);
+               this.skyVBO.drawArrays(7);
+               this.skyVBO.unbindBuffer();
+               GlStateManager.glDisableClientState(32884);
+            } else {
+               GlStateManager.callList(this.glSkyList);
+            }
          }

          GlStateManager.disableFog();
+         if (var20) {
+            Shaders.disableFog();
+         }
+
          GlStateManager.disableAlpha();
          GlStateManager.enableBlend();
          GlStateManager.tryBlendFuncSeparate(
             GlStateManager.SourceFactor.SRC_ALPHA,
             GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
             GlStateManager.SourceFactor.ONE,
             GlStateManager.DestFactor.ZERO
          );
          RenderHelper.disableStandardItemLighting();
-         float[] var22 = this.world.provider.calcSunriseSunsetColors(this.world.getCelestialAngle(var1), var1);
-         if (var22 != null) {
+         float[] var25 = this.world.provider.calcSunriseSunsetColors(this.world.getCelestialAngle(var1), var1);
+         if (var25 != null && Config.isSunMoonEnabled()) {
             GlStateManager.disableTexture2D();
+            if (var20) {
+               Shaders.disableTexture2D();
+            }
+
             GlStateManager.shadeModel(7425);
             GlStateManager.pushMatrix();
             GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
             GlStateManager.rotate(MathHelper.sin(this.world.getCelestialAngleRadians(var1)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
             GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
-            float var10 = var22[0];
-            float var11 = var22[1];
-            float var12 = var22[2];
+            float var11 = var25[0];
+            float var12 = var25[1];
+            float var13 = var25[2];
             if (var2 != 2) {
-               float var13 = (var10 * 30.0F + var11 * 59.0F + var12 * 11.0F) / 100.0F;
-               float var14 = (var10 * 30.0F + var11 * 70.0F) / 100.0F;
-               float var15 = (var10 * 30.0F + var12 * 70.0F) / 100.0F;
-               var10 = var13;
+               float var14 = (var11 * 30.0F + var12 * 59.0F + var13 * 11.0F) / 100.0F;
+               float var15 = (var11 * 30.0F + var12 * 70.0F) / 100.0F;
+               float var16 = (var11 * 30.0F + var13 * 70.0F) / 100.0F;
                var11 = var14;
                var12 = var15;
+               var13 = var16;
             }

-            var21.begin(6, DefaultVertexFormats.POSITION_COLOR);
-            var21.pos(0.0, 100.0, 0.0).color(var10, var11, var12, var22[3]).endVertex();
-            byte var29 = 16;
-
-            for (int var32 = 0; var32 <= 16; var32++) {
-               float var35 = var32 * (float) (Math.PI * 2) / 16.0F;
-               float var16 = MathHelper.sin(var35);
-               float var17 = MathHelper.cos(var35);
-               var21.pos(var16 * 120.0F, var17 * 120.0F, -var17 * 40.0F * var22[3]).color(var22[0], var22[1], var22[2], 0.0F).endVertex();
+            var24.begin(6, DefaultVertexFormats.POSITION_COLOR);
+            var24.pos(0.0, 100.0, 0.0).color(var11, var12, var13, var25[3]).endVertex();
+            byte var31 = 16;
+
+            for (int var34 = 0; var34 <= 16; var34++) {
+               float var36 = var34 * (float) (Math.PI * 2) / 16.0F;
+               float var17 = MathHelper.sin(var36);
+               float var18 = MathHelper.cos(var36);
+               var24.pos(var17 * 120.0F, var18 * 120.0F, -var18 * 40.0F * var25[3]).color(var25[0], var25[1], var25[2], 0.0F).endVertex();
             }

-            var20.draw();
+            var23.draw();
             GlStateManager.popMatrix();
             GlStateManager.shadeModel(7424);
          }

          GlStateManager.enableTexture2D();
+         if (var20) {
+            Shaders.enableTexture2D();
+         }
+
          GlStateManager.tryBlendFuncSeparate(
             GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
          );
          GlStateManager.pushMatrix();
-         float var23 = 1.0F - this.world.getRainStrength(var1);
-         GlStateManager.color(1.0F, 1.0F, 1.0F, var23);
+         float var26 = 1.0F - this.world.getRainStrength(var1);
+         GlStateManager.color(1.0F, 1.0F, 1.0F, var26);
          GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
+         CustomSky.renderSky(this.world, this.renderEngine, var1);
+         if (var20) {
+            Shaders.preCelestialRotate();
+         }
+
          GlStateManager.rotate(this.world.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
-         float var25 = 30.0F;
-         this.renderEngine.bindTexture(SUN_TEXTURES);
-         var21.begin(7, DefaultVertexFormats.POSITION_TEX);
-         var21.pos(-var25, 100.0, -var25).tex(0.0, 0.0).endVertex();
-         var21.pos(var25, 100.0, -var25).tex(1.0, 0.0).endVertex();
-         var21.pos(var25, 100.0, var25).tex(1.0, 1.0).endVertex();
-         var21.pos(-var25, 100.0, var25).tex(0.0, 1.0).endVertex();
-         var20.draw();
-         var25 = 20.0F;
-         this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
-         int var27 = this.world.getMoonPhase();
-         int var30 = var27 % 4;
-         int var33 = var27 / 4 % 2;
-         float var36 = (var30 + 0) / 4.0F;
-         float var37 = (var33 + 0) / 2.0F;
-         float var38 = (var30 + 1) / 4.0F;
-         float var18 = (var33 + 1) / 2.0F;
-         var21.begin(7, DefaultVertexFormats.POSITION_TEX);
-         var21.pos(-var25, -100.0, var25).tex(var38, var18).endVertex();
-         var21.pos(var25, -100.0, var25).tex(var36, var18).endVertex();
-         var21.pos(var25, -100.0, -var25).tex(var36, var37).endVertex();
-         var21.pos(-var25, -100.0, -var25).tex(var38, var37).endVertex();
-         var20.draw();
+         if (var20) {
+            Shaders.postCelestialRotate();
+         }
+
+         float var27 = 30.0F;
+         if (Config.isSunTexture()) {
+            this.renderEngine.bindTexture(SUN_TEXTURES);
+            var24.begin(7, DefaultVertexFormats.POSITION_TEX);
+            var24.pos(-var27, 100.0, -var27).tex(0.0, 0.0).endVertex();
+            var24.pos(var27, 100.0, -var27).tex(1.0, 0.0).endVertex();
+            var24.pos(var27, 100.0, var27).tex(1.0, 1.0).endVertex();
+            var24.pos(-var27, 100.0, var27).tex(0.0, 1.0).endVertex();
+            var23.draw();
+         }
+
+         var27 = 20.0F;
+         if (Config.isMoonTexture()) {
+            this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
+            int var29 = this.world.getMoonPhase();
+            int var32 = var29 % 4;
+            int var35 = var29 / 4 % 2;
+            float var37 = (var32 + 0) / 4.0F;
+            float var39 = (var35 + 0) / 2.0F;
+            float var41 = (var32 + 1) / 4.0F;
+            float var19 = (var35 + 1) / 2.0F;
+            var24.begin(7, DefaultVertexFormats.POSITION_TEX);
+            var24.pos(-var27, -100.0, var27).tex(var41, var19).endVertex();
+            var24.pos(var27, -100.0, var27).tex(var37, var19).endVertex();
+            var24.pos(var27, -100.0, -var27).tex(var37, var39).endVertex();
+            var24.pos(-var27, -100.0, -var27).tex(var41, var39).endVertex();
+            var23.draw();
+         }
+
          GlStateManager.disableTexture2D();
-         float var19 = this.world.getStarBrightness(var1) * var23;
-         if (var19 > 0.0F) {
-            GlStateManager.color(var19, var19, var19, var19);
+         if (var20) {
+            Shaders.disableTexture2D();
+         }
+
+         float var30 = this.world.getStarBrightness(var1) * var26;
+         if (var30 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.world)) {
+            GlStateManager.color(var30, var30, var30, var30);
             if (this.vboEnabled) {
                this.starVBO.bindBuffer();
                GlStateManager.glEnableClientState(32884);
                GlStateManager.glVertexPointer(3, 5126, 12, 0);
                this.starVBO.drawArrays(7);
                this.starVBO.unbindBuffer();
@@ -1183,17 +1605,25 @@
          }

          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.disableBlend();
          GlStateManager.enableAlpha();
          GlStateManager.enableFog();
+         if (var20) {
+            Shaders.enableFog();
+         }
+
          GlStateManager.popMatrix();
          GlStateManager.disableTexture2D();
+         if (var20) {
+            Shaders.disableTexture2D();
+         }
+
          GlStateManager.color(0.0F, 0.0F, 0.0F);
-         double var24 = this.mc.player.getPositionEyes(var1).y - this.world.getHorizon();
-         if (var24 < 0.0) {
+         double var33 = this.mc.player.getPositionEyes(var1).y - this.world.getHorizon();
+         if (var33 < 0.0) {
             GlStateManager.pushMatrix();
             GlStateManager.translate(0.0F, 12.0F, 0.0F);
             if (this.vboEnabled) {
                this.sky2VBO.bindBuffer();
                GlStateManager.glEnableClientState(32884);
                GlStateManager.glVertexPointer(3, 5126, 12, 0);
@@ -1202,183 +1632,234 @@
                GlStateManager.glDisableClientState(32884);
             } else {
                GlStateManager.callList(this.glSkyList2);
             }

             GlStateManager.popMatrix();
-            float var28 = 1.0F;
-            float var31 = -((float)(var24 + 65.0));
-            float var34 = -1.0F;
-            var21.begin(7, DefaultVertexFormats.POSITION_COLOR);
-            var21.pos(-1.0, var31, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, var31, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, var31, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, var31, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, var31, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, var31, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, var31, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, var31, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
-            var21.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
-            var20.draw();
+            float var38 = 1.0F;
+            float var40 = -((float)(var33 + 65.0));
+            float var42 = -1.0F;
+            var24.begin(7, DefaultVertexFormats.POSITION_COLOR);
+            var24.pos(-1.0, var40, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, var40, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, var40, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, var40, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, var40, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, var40, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, var40, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, var40, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
+            var24.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
+            var23.draw();
          }

          if (this.world.provider.isSkyColored()) {
-            GlStateManager.color(var4 * 0.2F + 0.04F, var5 * 0.2F + 0.04F, var6 * 0.6F + 0.1F);
+            GlStateManager.color(var5 * 0.2F + 0.04F, var6 * 0.2F + 0.04F, var7 * 0.6F + 0.1F);
          } else {
-            GlStateManager.color(var4, var5, var6);
+            GlStateManager.color(var5, var6, var7);
+         }
+
+         if (this.mc.gameSettings.renderDistanceChunks <= 4) {
+            GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
          }

          GlStateManager.pushMatrix();
-         GlStateManager.translate(0.0F, -((float)(var24 - 16.0)), 0.0F);
-         GlStateManager.callList(this.glSkyList2);
+         GlStateManager.translate(0.0F, -((float)(var33 - 16.0)), 0.0F);
+         if (Config.isSkyEnabled()) {
+            if (this.vboEnabled) {
+               this.sky2VBO.bindBuffer();
+               GlStateManager.glEnableClientState(32884);
+               GlStateManager.glVertexPointer(3, 5126, 12, 0);
+               this.sky2VBO.drawArrays(7);
+               this.sky2VBO.unbindBuffer();
+               GlStateManager.glDisableClientState(32884);
+            } else {
+               GlStateManager.callList(this.glSkyList2);
+            }
+         }
+
          GlStateManager.popMatrix();
          GlStateManager.enableTexture2D();
+         if (var20) {
+            Shaders.enableTexture2D();
+         }
+
          GlStateManager.depthMask(true);
       }
    }

    public void renderClouds(float var1, int var2, double var3, double var5, double var7) {
-      if (this.mc.world.provider.isSurfaceWorld()) {
-         if (this.mc.gameSettings.shouldRenderClouds() == 2) {
-            this.renderCloudsFancy(var1, var2, var3, var5, var7);
-         } else {
-            GlStateManager.disableCull();
-            byte var9 = 32;
-            byte var10 = 8;
-            Tessellator var11 = Tessellator.getInstance();
-            BufferBuilder var12 = var11.getBuffer();
-            this.renderEngine.bindTexture(CLOUDS_TEXTURES);
-            GlStateManager.enableBlend();
-            GlStateManager.tryBlendFuncSeparate(
-               GlStateManager.SourceFactor.SRC_ALPHA,
-               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
-               GlStateManager.SourceFactor.ONE,
-               GlStateManager.DestFactor.ZERO
-            );
-            Vec3d var13 = this.world.getCloudColour(var1);
-            float var14 = (float)var13.x;
-            float var15 = (float)var13.y;
-            float var16 = (float)var13.z;
-            if (var2 != 2) {
-               float var17 = (var14 * 30.0F + var15 * 59.0F + var16 * 11.0F) / 100.0F;
-               float var18 = (var14 * 30.0F + var15 * 70.0F) / 100.0F;
-               float var19 = (var14 * 30.0F + var16 * 70.0F) / 100.0F;
-               var14 = var17;
-               var15 = var18;
-               var16 = var19;
-            }
-
-            float var31 = 4.8828125E-4F;
-            double var32 = this.cloudTickCounter + var1;
-            double var20 = var3 + var32 * 0.03F;
-            int var24 = MathHelper.floor(var20 / 2048.0);
-            int var25 = MathHelper.floor(var7 / 2048.0);
-            var20 -= var24 * 2048;
-            double var22 = var7 - var25 * 2048;
-            float var26 = this.world.provider.getCloudHeight() - (float)var5 + 0.33F;
-            float var27 = (float)(var20 * 4.8828125E-4);
-            float var28 = (float)(var22 * 4.8828125E-4);
-            var12.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-
-            for (short var29 = -256; var29 < 256; var29 += 32) {
-               for (short var30 = -256; var30 < 256; var30 += 32) {
-                  var12.pos(var29 + 0, var26, var30 + 32)
-                     .tex((var29 + 0) * 4.8828125E-4F + var27, (var30 + 32) * 4.8828125E-4F + var28)
-                     .color(var14, var15, var16, 0.8F)
-                     .endVertex();
-                  var12.pos(var29 + 32, var26, var30 + 32)
-                     .tex((var29 + 32) * 4.8828125E-4F + var27, (var30 + 32) * 4.8828125E-4F + var28)
-                     .color(var14, var15, var16, 0.8F)
-                     .endVertex();
-                  var12.pos(var29 + 32, var26, var30 + 0)
-                     .tex((var29 + 32) * 4.8828125E-4F + var27, (var30 + 0) * 4.8828125E-4F + var28)
-                     .color(var14, var15, var16, 0.8F)
-                     .endVertex();
-                  var12.pos(var29 + 0, var26, var30 + 0)
-                     .tex((var29 + 0) * 4.8828125E-4F + var27, (var30 + 0) * 4.8828125E-4F + var28)
-                     .color(var14, var15, var16, 0.8F)
-                     .endVertex();
+      if (!Config.isCloudsOff()) {
+         if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
+            WorldProvider var9 = this.mc.world.provider;
+            Object var10 = Reflector.call(var9, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
+            if (var10 != null) {
+               Reflector.callVoid(var10, Reflector.IRenderHandler_render, new Object[]{var1, this.world, this.mc});
+               return;
+            }
+         }
+
+         if (this.mc.world.provider.isSurfaceWorld()) {
+            if (Config.isShaders()) {
+               Shaders.beginClouds();
+            }
+
+            if (Config.isCloudsFancy()) {
+               this.renderCloudsFancy(var1, var2, var3, var5, var7);
+            } else {
+               float var32 = 0.0F;
+               GlStateManager.disableCull();
+               byte var34 = 32;
+               byte var11 = 8;
+               Tessellator var12 = Tessellator.getInstance();
+               BufferBuilder var13 = var12.getBuffer();
+               this.renderEngine.bindTexture(CLOUDS_TEXTURES);
+               GlStateManager.enableBlend();
+               GlStateManager.tryBlendFuncSeparate(
+                  GlStateManager.SourceFactor.SRC_ALPHA,
+                  GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+                  GlStateManager.SourceFactor.ONE,
+                  GlStateManager.DestFactor.ZERO
+               );
+               Vec3d var14 = this.world.getCloudColour(var32);
+               float var15 = (float)var14.x;
+               float var16 = (float)var14.y;
+               float var17 = (float)var14.z;
+               this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, var1, var14);
+               if (this.cloudRenderer.shouldUpdateGlList()) {
+                  this.cloudRenderer.startUpdateGlList();
+                  if (var2 != 2) {
+                     float var18 = (var15 * 30.0F + var16 * 59.0F + var17 * 11.0F) / 100.0F;
+                     float var19 = (var15 * 30.0F + var16 * 70.0F) / 100.0F;
+                     float var20 = (var15 * 30.0F + var17 * 70.0F) / 100.0F;
+                     var15 = var18;
+                     var16 = var19;
+                     var17 = var20;
+                  }
+
+                  float var35 = 4.8828125E-4F;
+                  double var36 = this.cloudTickCounter + var32;
+                  double var21 = var3 + var36 * 0.03F;
+                  int var23 = MathHelper.floor(var21 / 2048.0);
+                  int var24 = MathHelper.floor(var7 / 2048.0);
+                  var21 -= var23 * 2048;
+                  double var25 = var7 - var24 * 2048;
+                  float var27 = this.world.provider.getCloudHeight() - (float)var5 + 0.33F;
+                  var27 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
+                  float var28 = (float)(var21 * 4.8828125E-4);
+                  float var29 = (float)(var25 * 4.8828125E-4);
+                  var13.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
+
+                  for (short var30 = -256; var30 < 256; var30 += 32) {
+                     for (short var31 = -256; var31 < 256; var31 += 32) {
+                        var13.pos(var30 + 0, var27, var31 + 32)
+                           .tex((var30 + 0) * 4.8828125E-4F + var28, (var31 + 32) * 4.8828125E-4F + var29)
+                           .color(var15, var16, var17, 0.8F)
+                           .endVertex();
+                        var13.pos(var30 + 32, var27, var31 + 32)
+                           .tex((var30 + 32) * 4.8828125E-4F + var28, (var31 + 32) * 4.8828125E-4F + var29)
+                           .color(var15, var16, var17, 0.8F)
+                           .endVertex();
+                        var13.pos(var30 + 32, var27, var31 + 0)
+                           .tex((var30 + 32) * 4.8828125E-4F + var28, (var31 + 0) * 4.8828125E-4F + var29)
+                           .color(var15, var16, var17, 0.8F)
+                           .endVertex();
+                        var13.pos(var30 + 0, var27, var31 + 0)
+                           .tex((var30 + 0) * 4.8828125E-4F + var28, (var31 + 0) * 4.8828125E-4F + var29)
+                           .color(var15, var16, var17, 0.8F)
+                           .endVertex();
+                     }
+                  }
+
+                  var12.draw();
+                  this.cloudRenderer.endUpdateGlList();
                }
+
+               this.cloudRenderer.renderGlList();
+               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+               GlStateManager.disableBlend();
+               GlStateManager.enableCull();
             }

-            var11.draw();
-            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-            GlStateManager.disableBlend();
-            GlStateManager.enableCull();
+            if (Config.isShaders()) {
+               Shaders.endClouds();
+            }
          }
       }
    }

    public boolean hasCloudFog(double var1, double var3, double var5, float var7) {
       return false;
    }

    private void renderCloudsFancy(float var1, int var2, double var3, double var5, double var7) {
+      float var51 = 0.0F;
       GlStateManager.disableCull();
-      Tessellator var9 = Tessellator.getInstance();
-      BufferBuilder var10 = var9.getBuffer();
-      float var11 = 12.0F;
-      float var12 = 4.0F;
-      double var13 = this.cloudTickCounter + var1;
-      double var15 = (var3 + var13 * 0.03F) / 12.0;
-      double var17 = var7 / 12.0 + 0.33F;
-      float var19 = this.world.provider.getCloudHeight() - (float)var5 + 0.33F;
-      int var20 = MathHelper.floor(var15 / 2048.0);
-      int var21 = MathHelper.floor(var17 / 2048.0);
-      var15 -= var20 * 2048;
-      var17 -= var21 * 2048;
+      Tessellator var10 = Tessellator.getInstance();
+      BufferBuilder var11 = var10.getBuffer();
+      float var12 = 12.0F;
+      float var13 = 4.0F;
+      double var14 = this.cloudTickCounter + var51;
+      double var16 = (var3 + var14 * 0.03F) / 12.0;
+      double var18 = var7 / 12.0 + 0.33F;
+      float var20 = this.world.provider.getCloudHeight() - (float)var5 + 0.33F;
+      var20 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
+      int var21 = MathHelper.floor(var16 / 2048.0);
+      int var22 = MathHelper.floor(var18 / 2048.0);
+      var16 -= var21 * 2048;
+      var18 -= var22 * 2048;
       this.renderEngine.bindTexture(CLOUDS_TEXTURES);
       GlStateManager.enableBlend();
       GlStateManager.tryBlendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
       );
-      Vec3d var22 = this.world.getCloudColour(var1);
-      float var23 = (float)var22.x;
-      float var24 = (float)var22.y;
-      float var25 = (float)var22.z;
+      Vec3d var23 = this.world.getCloudColour(var51);
+      float var24 = (float)var23.x;
+      float var25 = (float)var23.y;
+      float var26 = (float)var23.z;
+      this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, var1, var23);
       if (var2 != 2) {
-         float var26 = (var23 * 30.0F + var24 * 59.0F + var25 * 11.0F) / 100.0F;
-         float var27 = (var23 * 30.0F + var24 * 70.0F) / 100.0F;
-         float var28 = (var23 * 30.0F + var25 * 70.0F) / 100.0F;
-         var23 = var26;
+         float var27 = (var24 * 30.0F + var25 * 59.0F + var26 * 11.0F) / 100.0F;
+         float var28 = (var24 * 30.0F + var25 * 70.0F) / 100.0F;
+         float var29 = (var24 * 30.0F + var26 * 70.0F) / 100.0F;
          var24 = var27;
          var25 = var28;
+         var26 = var29;
       }

-      float var53 = var23 * 0.9F;
-      float var54 = var24 * 0.9F;
-      float var55 = var25 * 0.9F;
-      float var29 = var23 * 0.7F;
+      float var55 = var24 * 0.9F;
+      float var56 = var25 * 0.9F;
+      float var57 = var26 * 0.9F;
       float var30 = var24 * 0.7F;
       float var31 = var25 * 0.7F;
-      float var32 = var23 * 0.8F;
+      float var32 = var26 * 0.7F;
       float var33 = var24 * 0.8F;
       float var34 = var25 * 0.8F;
-      float var35 = 0.00390625F;
-      float var36 = MathHelper.floor(var15) * 0.00390625F;
-      float var37 = MathHelper.floor(var17) * 0.00390625F;
-      float var38 = (float)(var15 - MathHelper.floor(var15));
-      float var39 = (float)(var17 - MathHelper.floor(var17));
-      byte var40 = 8;
-      byte var41 = 4;
-      float var42 = 9.765625E-4F;
+      float var35 = var26 * 0.8F;
+      float var36 = 0.00390625F;
+      float var37 = MathHelper.floor(var16) * 0.00390625F;
+      float var38 = MathHelper.floor(var18) * 0.00390625F;
+      float var39 = (float)(var16 - MathHelper.floor(var16));
+      float var40 = (float)(var18 - MathHelper.floor(var18));
+      byte var41 = 8;
+      byte var42 = 4;
+      float var43 = 9.765625E-4F;
       GlStateManager.scale(12.0F, 1.0F, 12.0F);

-      for (int var43 = 0; var43 < 2; var43++) {
-         if (var43 == 0) {
+      for (int var44 = 0; var44 < 2; var44++) {
+         if (var44 == 0) {
             GlStateManager.colorMask(false, false, false, false);
          } else {
             switch (var2) {
                case 0:
                   GlStateManager.colorMask(false, true, true, true);
                   break;
@@ -1387,209 +1868,255 @@
                   break;
                case 2:
                   GlStateManager.colorMask(true, true, true, true);
             }
          }

-         for (int var44 = -3; var44 <= 4; var44++) {
+         this.cloudRenderer.renderGlList();
+      }
+
+      if (this.cloudRenderer.shouldUpdateGlList()) {
+         this.cloudRenderer.startUpdateGlList();
+
+         for (int var58 = -3; var58 <= 4; var58++) {
             for (int var45 = -3; var45 <= 4; var45++) {
-               var10.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
-               float var46 = var44 * 8;
+               var11.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
+               float var46 = var58 * 8;
                float var47 = var45 * 8;
-               float var48 = var46 - var38;
-               float var49 = var47 - var39;
-               if (var19 > -5.0F) {
-                  var10.pos(var48 + 0.0F, var19 + 0.0F, var49 + 8.0F)
-                     .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                     .color(var29, var30, var31, 0.8F)
+               float var48 = var46 - var39;
+               float var49 = var47 - var40;
+               if (var20 > -5.0F) {
+                  var11.pos(var48 + 0.0F, var20 + 0.0F, var49 + 8.0F)
+                     .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                     .color(var30, var31, var32, 0.8F)
                      .normal(0.0F, -1.0F, 0.0F)
                      .endVertex();
-                  var10.pos(var48 + 8.0F, var19 + 0.0F, var49 + 8.0F)
-                     .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                     .color(var29, var30, var31, 0.8F)
+                  var11.pos(var48 + 8.0F, var20 + 0.0F, var49 + 8.0F)
+                     .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                     .color(var30, var31, var32, 0.8F)
                      .normal(0.0F, -1.0F, 0.0F)
                      .endVertex();
-                  var10.pos(var48 + 8.0F, var19 + 0.0F, var49 + 0.0F)
-                     .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                     .color(var29, var30, var31, 0.8F)
+                  var11.pos(var48 + 8.0F, var20 + 0.0F, var49 + 0.0F)
+                     .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                     .color(var30, var31, var32, 0.8F)
                      .normal(0.0F, -1.0F, 0.0F)
                      .endVertex();
-                  var10.pos(var48 + 0.0F, var19 + 0.0F, var49 + 0.0F)
-                     .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                     .color(var29, var30, var31, 0.8F)
+                  var11.pos(var48 + 0.0F, var20 + 0.0F, var49 + 0.0F)
+                     .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                     .color(var30, var31, var32, 0.8F)
                      .normal(0.0F, -1.0F, 0.0F)
                      .endVertex();
                }

-               if (var19 <= 5.0F) {
-                  var10.pos(var48 + 0.0F, var19 + 4.0F - 9.765625E-4F, var49 + 8.0F)
-                     .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                     .color(var23, var24, var25, 0.8F)
+               if (var20 <= 5.0F) {
+                  var11.pos(var48 + 0.0F, var20 + 4.0F - 9.765625E-4F, var49 + 8.0F)
+                     .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                     .color(var24, var25, var26, 0.8F)
                      .normal(0.0F, 1.0F, 0.0F)
                      .endVertex();
-                  var10.pos(var48 + 8.0F, var19 + 4.0F - 9.765625E-4F, var49 + 8.0F)
-                     .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                     .color(var23, var24, var25, 0.8F)
+                  var11.pos(var48 + 8.0F, var20 + 4.0F - 9.765625E-4F, var49 + 8.0F)
+                     .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                     .color(var24, var25, var26, 0.8F)
                      .normal(0.0F, 1.0F, 0.0F)
                      .endVertex();
-                  var10.pos(var48 + 8.0F, var19 + 4.0F - 9.765625E-4F, var49 + 0.0F)
-                     .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                     .color(var23, var24, var25, 0.8F)
+                  var11.pos(var48 + 8.0F, var20 + 4.0F - 9.765625E-4F, var49 + 0.0F)
+                     .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                     .color(var24, var25, var26, 0.8F)
                      .normal(0.0F, 1.0F, 0.0F)
                      .endVertex();
-                  var10.pos(var48 + 0.0F, var19 + 4.0F - 9.765625E-4F, var49 + 0.0F)
-                     .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                     .color(var23, var24, var25, 0.8F)
+                  var11.pos(var48 + 0.0F, var20 + 4.0F - 9.765625E-4F, var49 + 0.0F)
+                     .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                     .color(var24, var25, var26, 0.8F)
                      .normal(0.0F, 1.0F, 0.0F)
                      .endVertex();
                }

-               if (var44 > -1) {
+               if (var58 > -1) {
                   for (int var50 = 0; var50 < 8; var50++) {
-                     var10.pos(var48 + var50 + 0.0F, var19 + 0.0F, var49 + 8.0F)
-                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+                     var11.pos(var48 + var50 + 0.0F, var20 + 0.0F, var49 + 8.0F)
+                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(-1.0F, 0.0F, 0.0F)
                         .endVertex();
-                     var10.pos(var48 + var50 + 0.0F, var19 + 4.0F, var49 + 8.0F)
-                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+                     var11.pos(var48 + var50 + 0.0F, var20 + 4.0F, var49 + 8.0F)
+                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(-1.0F, 0.0F, 0.0F)
                         .endVertex();
-                     var10.pos(var48 + var50 + 0.0F, var19 + 4.0F, var49 + 0.0F)
-                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+                     var11.pos(var48 + var50 + 0.0F, var20 + 4.0F, var49 + 0.0F)
+                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(-1.0F, 0.0F, 0.0F)
                         .endVertex();
-                     var10.pos(var48 + var50 + 0.0F, var19 + 0.0F, var49 + 0.0F)
-                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+                     var11.pos(var48 + var50 + 0.0F, var20 + 0.0F, var49 + 0.0F)
+                        .tex((var46 + var50 + 0.5F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(-1.0F, 0.0F, 0.0F)
                         .endVertex();
                   }
                }

-               if (var44 <= 1) {
-                  for (int var56 = 0; var56 < 8; var56++) {
-                     var10.pos(var48 + var56 + 1.0F - 9.765625E-4F, var19 + 0.0F, var49 + 8.0F)
-                        .tex((var46 + var56 + 0.5F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+               if (var58 <= 1) {
+                  for (int var59 = 0; var59 < 8; var59++) {
+                     var11.pos(var48 + var59 + 1.0F - 9.765625E-4F, var20 + 0.0F, var49 + 8.0F)
+                        .tex((var46 + var59 + 0.5F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(1.0F, 0.0F, 0.0F)
                         .endVertex();
-                     var10.pos(var48 + var56 + 1.0F - 9.765625E-4F, var19 + 4.0F, var49 + 8.0F)
-                        .tex((var46 + var56 + 0.5F) * 0.00390625F + var36, (var47 + 8.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+                     var11.pos(var48 + var59 + 1.0F - 9.765625E-4F, var20 + 4.0F, var49 + 8.0F)
+                        .tex((var46 + var59 + 0.5F) * 0.00390625F + var37, (var47 + 8.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(1.0F, 0.0F, 0.0F)
                         .endVertex();
-                     var10.pos(var48 + var56 + 1.0F - 9.765625E-4F, var19 + 4.0F, var49 + 0.0F)
-                        .tex((var46 + var56 + 0.5F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+                     var11.pos(var48 + var59 + 1.0F - 9.765625E-4F, var20 + 4.0F, var49 + 0.0F)
+                        .tex((var46 + var59 + 0.5F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(1.0F, 0.0F, 0.0F)
                         .endVertex();
-                     var10.pos(var48 + var56 + 1.0F - 9.765625E-4F, var19 + 0.0F, var49 + 0.0F)
-                        .tex((var46 + var56 + 0.5F) * 0.00390625F + var36, (var47 + 0.0F) * 0.00390625F + var37)
-                        .color(var53, var54, var55, 0.8F)
+                     var11.pos(var48 + var59 + 1.0F - 9.765625E-4F, var20 + 0.0F, var49 + 0.0F)
+                        .tex((var46 + var59 + 0.5F) * 0.00390625F + var37, (var47 + 0.0F) * 0.00390625F + var38)
+                        .color(var55, var56, var57, 0.8F)
                         .normal(1.0F, 0.0F, 0.0F)
                         .endVertex();
                   }
                }

                if (var45 > -1) {
-                  for (int var57 = 0; var57 < 8; var57++) {
-                     var10.pos(var48 + 0.0F, var19 + 4.0F, var49 + var57 + 0.0F)
-                        .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + var57 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                  for (int var60 = 0; var60 < 8; var60++) {
+                     var11.pos(var48 + 0.0F, var20 + 4.0F, var49 + var60 + 0.0F)
+                        .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + var60 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, -1.0F)
                         .endVertex();
-                     var10.pos(var48 + 8.0F, var19 + 4.0F, var49 + var57 + 0.0F)
-                        .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + var57 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                     var11.pos(var48 + 8.0F, var20 + 4.0F, var49 + var60 + 0.0F)
+                        .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + var60 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, -1.0F)
                         .endVertex();
-                     var10.pos(var48 + 8.0F, var19 + 0.0F, var49 + var57 + 0.0F)
-                        .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + var57 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                     var11.pos(var48 + 8.0F, var20 + 0.0F, var49 + var60 + 0.0F)
+                        .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + var60 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, -1.0F)
                         .endVertex();
-                     var10.pos(var48 + 0.0F, var19 + 0.0F, var49 + var57 + 0.0F)
-                        .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + var57 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                     var11.pos(var48 + 0.0F, var20 + 0.0F, var49 + var60 + 0.0F)
+                        .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + var60 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, -1.0F)
                         .endVertex();
                   }
                }

                if (var45 <= 1) {
-                  for (int var58 = 0; var58 < 8; var58++) {
-                     var10.pos(var48 + 0.0F, var19 + 4.0F, var49 + var58 + 1.0F - 9.765625E-4F)
-                        .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + var58 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                  for (int var61 = 0; var61 < 8; var61++) {
+                     var11.pos(var48 + 0.0F, var20 + 4.0F, var49 + var61 + 1.0F - 9.765625E-4F)
+                        .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + var61 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, 1.0F)
                         .endVertex();
-                     var10.pos(var48 + 8.0F, var19 + 4.0F, var49 + var58 + 1.0F - 9.765625E-4F)
-                        .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + var58 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                     var11.pos(var48 + 8.0F, var20 + 4.0F, var49 + var61 + 1.0F - 9.765625E-4F)
+                        .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + var61 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, 1.0F)
                         .endVertex();
-                     var10.pos(var48 + 8.0F, var19 + 0.0F, var49 + var58 + 1.0F - 9.765625E-4F)
-                        .tex((var46 + 8.0F) * 0.00390625F + var36, (var47 + var58 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                     var11.pos(var48 + 8.0F, var20 + 0.0F, var49 + var61 + 1.0F - 9.765625E-4F)
+                        .tex((var46 + 8.0F) * 0.00390625F + var37, (var47 + var61 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, 1.0F)
                         .endVertex();
-                     var10.pos(var48 + 0.0F, var19 + 0.0F, var49 + var58 + 1.0F - 9.765625E-4F)
-                        .tex((var46 + 0.0F) * 0.00390625F + var36, (var47 + var58 + 0.5F) * 0.00390625F + var37)
-                        .color(var32, var33, var34, 0.8F)
+                     var11.pos(var48 + 0.0F, var20 + 0.0F, var49 + var61 + 1.0F - 9.765625E-4F)
+                        .tex((var46 + 0.0F) * 0.00390625F + var37, (var47 + var61 + 0.5F) * 0.00390625F + var38)
+                        .color(var33, var34, var35, 0.8F)
                         .normal(0.0F, 0.0F, 1.0F)
                         .endVertex();
                   }
                }

-               var9.draw();
+               var10.draw();
             }
          }
+
+         this.cloudRenderer.endUpdateGlList();
       }

       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       GlStateManager.disableBlend();
       GlStateManager.enableCull();
    }

    public void updateChunks(long var1) {
+      var1 = (long)(var1 + 1.0E8);
       this.displayListEntitiesDirty = this.displayListEntitiesDirty | this.renderDispatcher.runChunkUploads(var1);
-      if (!this.chunksToUpdate.isEmpty()) {
-         Iterator var3 = this.chunksToUpdate.iterator();
+      if (this.chunksToUpdateForced.size() > 0) {
+         Iterator var3 = this.chunksToUpdateForced.iterator();

          while (var3.hasNext()) {
             RenderChunk var4 = (RenderChunk)var3.next();
-            boolean var5;
-            if (var4.needsImmediateUpdate()) {
-               var5 = this.renderDispatcher.updateChunkNow(var4);
-            } else {
-               var5 = this.renderDispatcher.updateChunkLater(var4);
-            }
-
-            if (!var5) {
+            if (!this.renderDispatcher.updateChunkLater(var4)) {
                break;
             }

             var4.clearNeedsUpdate();
             var3.remove();
-            long var6 = var1 - System.nanoTime();
-            if (var6 < 0L) {
+            this.chunksToUpdate.remove(var4);
+            this.chunksToResortTransparency.remove(var4);
+         }
+      }
+
+      if (this.chunksToResortTransparency.size() > 0) {
+         Iterator var13 = this.chunksToResortTransparency.iterator();
+         if (var13.hasNext()) {
+            RenderChunk var15 = (RenderChunk)var13.next();
+            if (this.renderDispatcher.updateTransparencyLater(var15)) {
+               var13.remove();
+            }
+         }
+      }
+
+      double var14 = 0.0;
+      int var5 = Config.getUpdatesPerFrame();
+      if (!this.chunksToUpdate.isEmpty()) {
+         Iterator var6 = this.chunksToUpdate.iterator();
+
+         while (var6.hasNext()) {
+            RenderChunk var7 = (RenderChunk)var6.next();
+            boolean var9 = var7.isChunkRegionEmpty();
+            boolean var8;
+            if (!var7.needsImmediateUpdate() && !var9) {
+               var8 = this.renderDispatcher.updateChunkLater(var7);
+            } else {
+               var8 = this.renderDispatcher.updateChunkNow(var7);
+            }
+
+            if (!var8) {
                break;
             }
+
+            var7.clearNeedsUpdate();
+            var6.remove();
+            if (!var9) {
+               double var10 = 2.0 * RenderChunkUtils.getRelativeBufferSize(var7);
+               var14 += var10;
+               if (var14 > var5) {
+                  break;
+               }
+            }
          }
       }
    }

    public void renderWorldBorder(Entity var1, float var2) {
       Tessellator var3 = Tessellator.getInstance();
       BufferBuilder var4 = var3.getBuffer();
       WorldBorder var5 = this.world.getWorldBorder();
       double var6 = this.mc.gameSettings.renderDistanceChunks * 16;
-      if (!(var1.posX < var5.maxX() - var6) || !(var1.posX > var5.minX() + var6) || !(var1.posZ < var5.maxZ() - var6) || !(var1.posZ > var5.minZ() + var6)) {
+      if (var1.posX >= var5.maxX() - var6 || var1.posX <= var5.minX() + var6 || var1.posZ >= var5.maxZ() - var6 || var1.posZ <= var5.minZ() + var6) {
+         if (Config.isShaders()) {
+            Shaders.pushProgram();
+            Shaders.useProgram(Shaders.ProgramTexturedLit);
+         }
+
          double var8 = 1.0 - var5.getClosestDistance(var1) / var6;
          var8 = Math.pow(var8, 4.0);
          double var10 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var2;
          double var12 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var2;
          double var14 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var2;
          GlStateManager.enableBlend();
@@ -1679,38 +2206,53 @@
          var4.setTranslation(0.0, 0.0, 0.0);
          GlStateManager.enableCull();
          GlStateManager.disableAlpha();
          GlStateManager.doPolygonOffset(0.0F, 0.0F);
          GlStateManager.disablePolygonOffset();
          GlStateManager.enableAlpha();
+         GlStateManager.tryBlendFuncSeparate(
+            GlStateManager.SourceFactor.SRC_ALPHA,
+            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+            GlStateManager.SourceFactor.ONE,
+            GlStateManager.DestFactor.ZERO
+         );
          GlStateManager.disableBlend();
          GlStateManager.popMatrix();
          GlStateManager.depthMask(true);
+         if (Config.isShaders()) {
+            Shaders.popProgram();
+         }
       }
    }

    private void preRenderDamagedBlocks() {
       GlStateManager.tryBlendFuncSeparate(
          GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
       );
       GlStateManager.enableBlend();
       GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
-      GlStateManager.doPolygonOffset(-3.0F, -3.0F);
+      GlStateManager.doPolygonOffset(-1.0F, -10.0F);
       GlStateManager.enablePolygonOffset();
       GlStateManager.alphaFunc(516, 0.1F);
       GlStateManager.enableAlpha();
       GlStateManager.pushMatrix();
+      if (Config.isShaders()) {
+         ShadersRender.beginBlockDamage();
+      }
    }

    private void postRenderDamagedBlocks() {
       GlStateManager.disableAlpha();
       GlStateManager.doPolygonOffset(0.0F, 0.0F);
       GlStateManager.disablePolygonOffset();
       GlStateManager.enableAlpha();
       GlStateManager.depthMask(true);
       GlStateManager.popMatrix();
+      if (Config.isShaders()) {
+         ShadersRender.endBlockDamage();
+      }
    }

    public void drawBlockDamageTexture(Tessellator var1, BufferBuilder var2, Entity var3, float var4) {
       double var5 = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) * var4;
       double var7 = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * var4;
       double var9 = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) * var4;
@@ -1726,56 +2268,79 @@
             DestroyBlockProgress var12 = (DestroyBlockProgress)var11.next();
             BlockPos var13 = var12.getPosition();
             double var14 = var13.getX() - var5;
             double var16 = var13.getY() - var7;
             double var18 = var13.getZ() - var9;
             Block var20 = this.world.getBlockState(var13).getBlock();
-            if (!(var20 instanceof BlockChest) && !(var20 instanceof BlockEnderChest) && !(var20 instanceof BlockSign) && !(var20 instanceof BlockSkull)) {
+            boolean var21;
+            if (Reflector.ForgeTileEntity_canRenderBreaking.exists()) {
+               boolean var22 = var20 instanceof BlockChest || var20 instanceof BlockEnderChest || var20 instanceof BlockSign || var20 instanceof BlockSkull;
+               if (!var22) {
+                  TileEntity var23 = this.world.getTileEntity(var13);
+                  if (var23 != null) {
+                     var22 = Reflector.callBoolean(var23, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]);
+                  }
+               }
+
+               var21 = !var22;
+            } else {
+               var21 = !(var20 instanceof BlockChest) && !(var20 instanceof BlockEnderChest) && !(var20 instanceof BlockSign) && !(var20 instanceof BlockSkull);
+            }
+
+            if (var21) {
                if (var14 * var14 + var16 * var16 + var18 * var18 > 1024.0) {
                   var11.remove();
                } else {
-                  IBlockState var21 = this.world.getBlockState(var13);
-                  if (var21.getMaterial() != Material.AIR) {
-                     int var22 = var12.getPartialBlockDamage();
-                     TextureAtlasSprite var23 = this.destroyBlockIcons[var22];
-                     BlockRendererDispatcher var24 = this.mc.getBlockRendererDispatcher();
-                     var24.renderBlockDamage(var21, var13, var23, this.world);
+                  IBlockState var26 = this.world.getBlockState(var13);
+                  if (var26.a() != Material.AIR) {
+                     int var27 = var12.getPartialBlockDamage();
+                     TextureAtlasSprite var24 = this.destroyBlockIcons[var27];
+                     BlockRendererDispatcher var25 = this.mc.getBlockRendererDispatcher();
+                     var25.renderBlockDamage(var26, var13, var24, this.world);
                   }
                }
             }
          }

          var1.draw();
          var2.setTranslation(0.0, 0.0, 0.0);
          this.postRenderDamagedBlocks();
       }
    }

    public void drawSelectionBox(EntityPlayer var1, RayTraceResult var2, int var3, float var4) {
-      if (var3 == 0 && var2.typeOfHit == RayTraceResult.Type.BLOCK) {
+      if (var3 == 0 && var2.typeOfHit == Type.BLOCK) {
          GlStateManager.enableBlend();
          GlStateManager.tryBlendFuncSeparate(
             GlStateManager.SourceFactor.SRC_ALPHA,
             GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
             GlStateManager.SourceFactor.ONE,
             GlStateManager.DestFactor.ZERO
          );
          GlStateManager.glLineWidth(2.0F);
          GlStateManager.disableTexture2D();
+         if (Config.isShaders()) {
+            Shaders.disableTexture2D();
+         }
+
          GlStateManager.depthMask(false);
          BlockPos var5 = var2.getBlockPos();
          IBlockState var6 = this.world.getBlockState(var5);
-         if (var6.getMaterial() != Material.AIR && this.world.getWorldBorder().contains(var5)) {
+         if (var6.a() != Material.AIR && this.world.getWorldBorder().contains(var5)) {
             double var7 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var4;
             double var9 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var4;
             double var11 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var4;
-            drawSelectionBoundingBox(var6.getSelectedBoundingBox(this.world, var5).grow(0.002F).offset(-var7, -var9, -var11), 0.0F, 0.0F, 0.0F, 0.4F);
+            drawSelectionBoundingBox(var6.c(this.world, var5).grow(0.002F).offset(-var7, -var9, -var11), 0.0F, 0.0F, 0.0F, 0.4F);
          }

          GlStateManager.depthMask(true);
          GlStateManager.enableTexture2D();
+         if (Config.isShaders()) {
+            Shaders.enableTexture2D();
+         }
+
          GlStateManager.disableBlend();
       }
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB var0, float var1, float var2, float var3, float var4) {
       drawBoundingBox(var0.minX, var0.minY, var0.minZ, var0.maxX, var0.maxY, var0.maxZ, var1, var2, var3, var4);
@@ -1801,20 +2366,19 @@
       var0.pos(var1, var3, var11).color(var13, var14, var15, var16).endVertex();
       var0.pos(var1, var3, var5).color(var13, var14, var15, var16).endVertex();
       var0.pos(var1, var9, var5).color(var13, var14, var15, var16).endVertex();
       var0.pos(var7, var9, var5).color(var13, var14, var15, var16).endVertex();
       var0.pos(var7, var9, var11).color(var13, var14, var15, var16).endVertex();
       var0.pos(var1, var9, var11).color(var13, var14, var15, var16).endVertex();
-      var0.pos(var1, var9, var5).color(var13, var14, var15, var16).endVertex();
-      var0.pos(var1, var9, var11).color(var13, var14, var15, 0.0F).endVertex();
+      var0.pos(var1, var9, var5).color(var13, var14, var15, 0.0F).endVertex();
+      var0.pos(var1, var9, var11).color(var13, var14, var15, var16).endVertex();
       var0.pos(var1, var3, var11).color(var13, var14, var15, var16).endVertex();
-      var0.pos(var7, var9, var11).color(var13, var14, var15, 0.0F).endVertex();
-      var0.pos(var7, var3, var11).color(var13, var14, var15, var16).endVertex();
+      var0.pos(var7, var3, var11).color(var13, var14, var15, 0.0F).endVertex();
+      var0.pos(var7, var9, var11).color(var13, var14, var15, var16).endVertex();
       var0.pos(var7, var9, var5).color(var13, var14, var15, 0.0F).endVertex();
       var0.pos(var7, var3, var5).color(var13, var14, var15, var16).endVertex();
-      var0.pos(var7, var3, var5).color(var13, var14, var15, 0.0F).endVertex();
    }

    public static void renderFilledBox(AxisAlignedBB var0, float var1, float var2, float var3, float var4) {
       renderFilledBox(var0.minX, var0.minY, var0.minZ, var0.maxX, var0.maxY, var0.maxZ, var1, var2, var3, var4);
    }

@@ -1901,14 +2465,14 @@
       }

       this.setPartying(this.world, var2, var1 != null);
    }

    private void setPartying(World var1, BlockPos var2, boolean var3) {
-      for (EntityLivingBase var6 : var1.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(var2).grow(3.0))) {
-         var6.setPartying(var2, var3);
+      for (EntityLivingBase var5 : var1.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(var2).grow(3.0))) {
+         var5.setPartying(var2, var3);
       }
    }

    public void playSoundToAllNearExcept(
       @Nullable EntityPlayer var1, SoundEvent var2, SoundCategory var3, double var4, double var6, double var8, float var10, float var11
    ) {
@@ -1956,18 +2520,90 @@
       Entity var17 = this.mc.getRenderViewEntity();
       if (this.mc != null && var17 != null && this.mc.effectRenderer != null) {
          int var18 = this.calculateParticleLevel(var3);
          double var19 = var17.posX - var4;
          double var21 = var17.posY - var6;
          double var23 = var17.posZ - var8;
-         if (var2) {
-            return this.mc.effectRenderer.spawnEffectParticle(var1, var4, var6, var8, var10, var12, var14, var16);
-         } else if (var19 * var19 + var21 * var21 + var23 * var23 > 1024.0) {
+         if (var1 == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.PORTAL.getParticleID() && !Config.isPortalParticles()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava()) {
+            return null;
+         } else if (var1 == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles()) {
             return null;
          } else {
-            return var18 > 1 ? null : this.mc.effectRenderer.spawnEffectParticle(var1, var4, var6, var8, var10, var12, var14, var16);
+            if (!var2) {
+               double var26 = 1024.0;
+               if (var1 == EnumParticleTypes.CRIT.getParticleID()) {
+                  var26 = 38416.0;
+               }
+
+               if (var19 * var19 + var21 * var21 + var23 * var23 > var26) {
+                  return null;
+               }
+
+               if (var18 > 1) {
+                  return null;
+               }
+            }
+
+            Particle var28 = this.mc.effectRenderer.spawnEffectParticle(var1, var4, var6, var8, var10, var12, var14, var16);
+            if (var1 == EnumParticleTypes.WATER_BUBBLE.getParticleID()) {
+               CustomColors.updateWaterFX(var28, this.world, var4, var6, var8, this.renderEnv);
+            }
+
+            if (var1 == EnumParticleTypes.WATER_SPLASH.getParticleID()) {
+               CustomColors.updateWaterFX(var28, this.world, var4, var6, var8, this.renderEnv);
+            }
+
+            if (var1 == EnumParticleTypes.WATER_DROP.getParticleID()) {
+               CustomColors.updateWaterFX(var28, this.world, var4, var6, var8, this.renderEnv);
+            }
+
+            if (var1 == EnumParticleTypes.TOWN_AURA.getParticleID()) {
+               CustomColors.updateMyceliumFX(var28);
+            }
+
+            if (var1 == EnumParticleTypes.PORTAL.getParticleID()) {
+               CustomColors.updatePortalFX(var28);
+            }
+
+            if (var1 == EnumParticleTypes.REDSTONE.getParticleID()) {
+               CustomColors.updateReddustFX(var28, this.world, var4, var6, var8);
+            }
+
+            return var28;
          }
       } else {
          return null;
       }
    }

@@ -1982,15 +2618,23 @@
       }

       return var2;
    }

    public void onEntityAdded(Entity var1) {
+      RandomEntities.entityLoaded(var1, this.world);
+      if (Config.isDynamicLights()) {
+         DynamicLights.entityAdded(var1, this);
+      }
    }

    public void onEntityRemoved(Entity var1) {
+      RandomEntities.entityUnloaded(var1, this.world);
+      if (Config.isDynamicLights()) {
+         DynamicLights.entityRemoved(var1, this);
+      }
    }

    public void deleteAllDisplayLists() {
    }

    public void broadcastSound(int var1, BlockPos var2, int var3) {
@@ -2059,13 +2703,13 @@
                .playSound(var3, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (var5.nextFloat() - var5.nextFloat()) * 0.8F, false);
             break;
          case 1010:
             if (Item.getItemById(var4) instanceof ItemRecord) {
                this.world.playRecord(var3, ((ItemRecord)Item.getItemById(var4)).getSound());
             } else {
-               this.world.playRecord(var3, null);
+               this.world.playRecord(var3, (SoundEvent)null);
             }
             break;
          case 1011:
             this.world.playSound(var3, SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
             break;
          case 1012:
@@ -2156,150 +2800,157 @@
             this.world.playSound(var3, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
             break;
          case 1037:
             this.world.playSound(var3, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
             break;
          case 2000:
-            int var31 = var4 % 3 - 1;
-            int var8 = var4 / 3 % 3 - 1;
-            double var33 = var3.getX() + var31 * 0.6 + 0.5;
-            double var35 = var3.getY() + 0.5;
-            double var39 = var3.getZ() + var8 * 0.6 + 0.5;
-
-            for (int var40 = 0; var40 < 10; var40++) {
-               double var41 = var5.nextDouble() * 0.2 + 0.01;
-               double var47 = var33 + var31 * 0.01 + (var5.nextDouble() - 0.5) * var8 * 0.5;
-               double var50 = var35 + (var5.nextDouble() - 0.5) * 0.5;
-               double var53 = var39 + var8 * 0.01 + (var5.nextDouble() - 0.5) * var31 * 0.5;
-               double var55 = var31 * var41 + var5.nextGaussian() * 0.01;
-               double var57 = -0.03 + var5.nextGaussian() * 0.01;
-               double var58 = var8 * var41 + var5.nextGaussian() * 0.01;
-               this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var47, var50, var53, var55, var57, var58);
+            int var6 = var4 % 3 - 1;
+            int var7 = var4 / 3 % 3 - 1;
+            double var8 = var3.getX() + var6 * 0.6 + 0.5;
+            double var10 = var3.getY() + 0.5;
+            double var12 = var3.getZ() + var7 * 0.6 + 0.5;
+
+            for (int var41 = 0; var41 < 10; var41++) {
+               double var43 = var5.nextDouble() * 0.2 + 0.01;
+               double var44 = var8 + var6 * 0.01 + (var5.nextDouble() - 0.5) * var7 * 0.5;
+               double var45 = var10 + (var5.nextDouble() - 0.5) * 0.5;
+               double var47 = var12 + var7 * 0.01 + (var5.nextDouble() - 0.5) * var6 * 0.5;
+               double var48 = var6 * var43 + var5.nextGaussian() * 0.01;
+               double var50 = -0.03 + var5.nextGaussian() * 0.01;
+               double var51 = var7 * var43 + var5.nextGaussian() * 0.01;
+               this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var44, var45, var47, var48, var50, var51);
             }
-            break;
+
+            return;
          case 2001:
-            Block var6 = Block.getBlockById(var4 & 4095);
-            if (var6.getDefaultState().getMaterial() != Material.AIR) {
-               SoundType var44 = var6.getSoundType();
-               this.world.playSound(var3, var44.getBreakSound(), SoundCategory.BLOCKS, (var44.getVolume() + 1.0F) / 2.0F, var44.getPitch() * 0.8F, false);
+            Block var14 = Block.getBlockById(var4 & 4095);
+            if (var14.getDefaultState().a() != Material.AIR) {
+               SoundType var42 = var14.getSoundType();
+               if (Reflector.ForgeBlock_getSoundType.exists()) {
+                  var42 = (SoundType)Reflector.call(var14, Reflector.ForgeBlock_getSoundType, new Object[]{Block.getStateById(var4), this.world, var3, null});
+               }
+
+               this.world.playSound(var3, var42.getBreakSound(), SoundCategory.BLOCKS, (var42.getVolume() + 1.0F) / 2.0F, var42.getPitch() * 0.8F, false);
             }

-            this.mc.effectRenderer.addBlockDestroyEffects(var3, var6.getStateFromMeta(var4 >> 12 & 0xFF));
+            this.mc.effectRenderer.addBlockDestroyEffects(var3, var14.getStateFromMeta(var4 >> 12 & 0xFF));
             break;
          case 2002:
          case 2007:
-            double var30 = var3.getX();
-            double var32 = var3.getY();
-            double var34 = var3.getZ();
+            double var15 = var3.getX();
+            double var17 = var3.getY();
+            double var19 = var3.getZ();

-            for (int var37 = 0; var37 < 8; var37++) {
+            for (int var21 = 0; var21 < 8; var21++) {
                this.spawnParticle(
                   EnumParticleTypes.ITEM_CRACK,
-                  var30,
-                  var32,
-                  var34,
+                  var15,
+                  var17,
+                  var19,
                   var5.nextGaussian() * 0.15,
                   var5.nextDouble() * 0.2,
                   var5.nextGaussian() * 0.15,
                   Item.getIdFromItem(Items.SPLASH_POTION)
                );
             }

-            float var38 = (var4 >> 16 & 0xFF) / 255.0F;
-            float var14 = (var4 >> 8 & 0xFF) / 255.0F;
-            float var15 = (var4 >> 0 & 0xFF) / 255.0F;
-            EnumParticleTypes var16 = var2 == 2007 ? EnumParticleTypes.SPELL_INSTANT : EnumParticleTypes.SPELL;
-
-            for (int var43 = 0; var43 < 100; var43++) {
-               double var46 = var5.nextDouble() * 4.0;
-               double var49 = var5.nextDouble() * Math.PI * 2.0;
-               double var52 = Math.cos(var49) * var46;
-               double var54 = 0.01 + var5.nextDouble() * 0.5;
-               double var56 = Math.sin(var49) * var46;
-               Particle var28 = this.spawnParticle0(
-                  var16.getParticleID(), var16.getShouldIgnoreRange(), var30 + var52 * 0.1, var32 + 0.3, var34 + var56 * 0.1, var52, var54, var56
+            float var46 = (var4 >> 16 & 0xFF) / 255.0F;
+            float var22 = (var4 >> 8 & 0xFF) / 255.0F;
+            float var23 = (var4 >> 0 & 0xFF) / 255.0F;
+            EnumParticleTypes var24 = var2 == 2007 ? EnumParticleTypes.SPELL_INSTANT : EnumParticleTypes.SPELL;
+
+            for (int var49 = 0; var49 < 100; var49++) {
+               double var26 = var5.nextDouble() * 4.0;
+               double var28 = var5.nextDouble() * Math.PI * 2.0;
+               double var30 = Math.cos(var28) * var26;
+               double var56 = 0.01 + var5.nextDouble() * 0.5;
+               double var58 = Math.sin(var28) * var26;
+               Particle var60 = this.spawnParticle0(
+                  var24.getParticleID(), var24.getShouldIgnoreRange(), var15 + var30 * 0.1, var17 + 0.3, var19 + var58 * 0.1, var30, var56, var58
                );
-               if (var28 != null) {
-                  float var29 = 0.75F + var5.nextFloat() * 0.25F;
-                  var28.setRBGColorF(var38 * var29, var14 * var29, var15 * var29);
-                  var28.multiplyVelocity((float)var46);
+               if (var60 != null) {
+                  float var37 = 0.75F + var5.nextFloat() * 0.25F;
+                  var60.setRBGColorF(var46 * var37, var22 * var37, var23 * var37);
+                  var60.multiplyVelocity((float)var26);
                }
             }

             this.world.playSound(var3, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.NEUTRAL, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
             break;
          case 2003:
-            double var7 = var3.getX() + 0.5;
-            double var9 = var3.getY();
-            double var11 = var3.getZ() + 0.5;
+            double var25 = var3.getX() + 0.5;
+            double var27 = var3.getY();
+            double var29 = var3.getZ() + 0.5;

-            for (int var13 = 0; var13 < 8; var13++) {
+            for (int var53 = 0; var53 < 8; var53++) {
                this.spawnParticle(
                   EnumParticleTypes.ITEM_CRACK,
-                  var7,
-                  var9,
-                  var11,
+                  var25,
+                  var27,
+                  var29,
                   var5.nextGaussian() * 0.15,
                   var5.nextDouble() * 0.2,
                   var5.nextGaussian() * 0.15,
                   Item.getIdFromItem(Items.ENDER_EYE)
                );
             }

-            for (double var36 = 0.0; var36 < Math.PI * 2; var36 += Math.PI / 20) {
+            for (double var54 = 0.0; var54 < Math.PI * 2; var54 += Math.PI / 20) {
                this.spawnParticle(
                   EnumParticleTypes.PORTAL,
-                  var7 + Math.cos(var36) * 5.0,
-                  var9 - 0.4,
-                  var11 + Math.sin(var36) * 5.0,
-                  Math.cos(var36) * -5.0,
+                  var25 + Math.cos(var54) * 5.0,
+                  var27 - 0.4,
+                  var29 + Math.sin(var54) * 5.0,
+                  Math.cos(var54) * -5.0,
                   0.0,
-                  Math.sin(var36) * -5.0
+                  Math.sin(var54) * -5.0
                );
                this.spawnParticle(
                   EnumParticleTypes.PORTAL,
-                  var7 + Math.cos(var36) * 5.0,
-                  var9 - 0.4,
-                  var11 + Math.sin(var36) * 5.0,
-                  Math.cos(var36) * -7.0,
+                  var25 + Math.cos(var54) * 5.0,
+                  var27 - 0.4,
+                  var29 + Math.sin(var54) * 5.0,
+                  Math.cos(var54) * -7.0,
                   0.0,
-                  Math.sin(var36) * -7.0
+                  Math.sin(var54) * -7.0
                );
             }
-            break;
+
+            return;
          case 2004:
-            for (int var42 = 0; var42 < 20; var42++) {
-               double var45 = var3.getX() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
-               double var48 = var3.getY() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
-               double var51 = var3.getZ() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
-               this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var45, var48, var51, 0.0, 0.0, 0.0, new int[0]);
-               this.world.spawnParticle(EnumParticleTypes.FLAME, var45, var48, var51, 0.0, 0.0, 0.0, new int[0]);
+            for (int var52 = 0; var52 < 20; var52++) {
+               double var55 = var3.getX() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
+               double var57 = var3.getY() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
+               double var59 = var3.getZ() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
+               this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var55, var57, var59, 0.0, 0.0, 0.0, new int[0]);
+               this.world.spawnParticle(EnumParticleTypes.FLAME, var55, var57, var59, 0.0, 0.0, 0.0, new int[0]);
             }
-            break;
+
+            return;
          case 2005:
             ItemDye.spawnBonemealParticles(this.world, var3, var4);
             break;
          case 2006:
-            for (int var17 = 0; var17 < 200; var17++) {
-               float var18 = var5.nextFloat() * 4.0F;
-               float var19 = var5.nextFloat() * (float) (Math.PI * 2);
-               double var20 = MathHelper.cos(var19) * var18;
-               double var22 = 0.01 + var5.nextDouble() * 0.5;
-               double var24 = MathHelper.sin(var19) * var18;
-               Particle var26 = this.spawnParticle0(
+            for (int var31 = 0; var31 < 200; var31++) {
+               float var32 = var5.nextFloat() * 4.0F;
+               float var33 = var5.nextFloat() * (float) (Math.PI * 2);
+               double var34 = MathHelper.cos(var33) * var32;
+               double var36 = 0.01 + var5.nextDouble() * 0.5;
+               double var38 = MathHelper.sin(var33) * var32;
+               Particle var40 = this.spawnParticle0(
                   EnumParticleTypes.DRAGON_BREATH.getParticleID(),
                   false,
-                  var3.getX() + var20 * 0.1,
+                  var3.getX() + var34 * 0.1,
                   var3.getY() + 0.3,
-                  var3.getZ() + var24 * 0.1,
-                  var20,
-                  var22,
-                  var24
+                  var3.getZ() + var38 * 0.1,
+                  var34,
+                  var36,
+                  var38
                );
-               if (var26 != null) {
-                  var26.multiplyVelocity(var18);
+               if (var40 != null) {
+                  var40.multiplyVelocity(var32);
                }
             }

             this.world
                .playSound(var3, SoundEvents.ENTITY_ENDERDRAGON_FIREBALL_EPLD, SoundCategory.HOSTILE, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
             break;
@@ -2341,34 +2992,119 @@
    }

    public void setDisplayListEntitiesDirty() {
       this.displayListEntitiesDirty = true;
    }

+   public void resetClouds() {
+      this.cloudRenderer.reset();
+   }
+
+   public int getCountRenderers() {
+      return this.viewFrustum.renderChunks.length;
+   }
+
+   public int getCountActiveRenderers() {
+      return this.renderInfos.size();
+   }
+
+   public int getCountEntitiesRendered() {
+      return this.countEntitiesRendered;
+   }
+
+   public int getCountTileEntitiesRendered() {
+      return this.countTileEntitiesRendered;
+   }
+
+   public int getCountLoadedChunks() {
+      if (this.world == null) {
+         return 0;
+      } else {
+         ChunkProviderClient var1 = this.world.getChunkProvider();
+         if (var1 == null) {
+            return 0;
+         } else {
+            if (var1 != this.worldChunkProvider) {
+               this.worldChunkProvider = var1;
+               this.worldChunkProviderMap = (Long2ObjectMap<Chunk>)Reflector.getFieldValue(var1, Reflector.ChunkProviderClient_chunkMapping);
+            }
+
+            return this.worldChunkProviderMap == null ? 0 : this.worldChunkProviderMap.size();
+         }
+      }
+   }
+
+   public int getCountChunksToUpdate() {
+      return this.chunksToUpdate.size();
+   }
+
+   public RenderChunk getRenderChunk(BlockPos var1) {
+      return this.viewFrustum.getRenderChunk(var1);
+   }
+
+   public WorldClient getWorld() {
+      return this.world;
+   }
+
+   private void clearRenderInfos() {
+      if (renderEntitiesCounter > 0) {
+         this.renderInfos = new ArrayList<>(this.renderInfos.size() + 16);
+         this.renderInfosEntities = new ArrayList(this.renderInfosEntities.size() + 16);
+         this.renderInfosTileEntities = new ArrayList(this.renderInfosTileEntities.size() + 16);
+      } else {
+         this.renderInfos.clear();
+         this.renderInfosEntities.clear();
+         this.renderInfosTileEntities.clear();
+      }
+   }
+
+   public void onPlayerPositionSet() {
+      if (this.firstWorldLoad) {
+         this.loadRenderers();
+         this.firstWorldLoad = false;
+      }
+   }
+
+   public void pauseChunkUpdates() {
+      if (this.renderDispatcher != null) {
+         this.renderDispatcher.pauseChunkUpdates();
+      }
+   }
+
+   public void resumeChunkUpdates() {
+      if (this.renderDispatcher != null) {
+         this.renderDispatcher.resumeChunkUpdates();
+      }
+   }
+
    public void updateTileEntities(Collection<TileEntity> var1, Collection<TileEntity> var2) {
       synchronized (this.setTileEntities) {
          this.setTileEntities.removeAll(var1);
          this.setTileEntities.addAll(var2);
       }
    }

-   class ContainerLocalRenderInformation {
+   public static class ContainerLocalRenderInformation {
       final RenderChunk renderChunk;
-      final EnumFacing facing;
-      byte setFacing;
-      final int counter;
-
-      private ContainerLocalRenderInformation(RenderChunk var2, EnumFacing var3, @Nullable int var4) {
-         this.renderChunk = var2;
-         this.facing = var3;
-         this.counter = var4;
+      EnumFacing facing;
+      int setFacing;
+
+      public ContainerLocalRenderInformation(RenderChunk var1, EnumFacing var2, int var3) {
+         this.renderChunk = var1;
+         this.facing = var2;
+         this.setFacing = var3;
       }

       public void setDirection(byte var1, EnumFacing var2) {
-         this.setFacing = (byte)(this.setFacing | var1 | 1 << var2.ordinal());
+         this.setFacing = this.setFacing | var1 | 1 << var2.ordinal();
       }

       public boolean hasDirection(EnumFacing var1) {
          return (this.setFacing & 1 << var1.ordinal()) > 0;
+      }
+
+      private void initialize(EnumFacing var1, int var2) {
+         this.facing = var1;
+         this.setFacing = var2;
       }
    }
 }
 */
