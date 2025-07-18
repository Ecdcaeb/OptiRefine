package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class RenderGlobal implements IWorldEventListener, IResourceManagerReloadListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
   private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
   private static final ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");
   private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");
   private static final ResourceLocation FORCEFIELD_TEXTURES = new ResourceLocation("textures/misc/forcefield.png");
   private final Minecraft mc;
   private final TextureManager renderEngine;
   private final RenderManager renderManager;
   private WorldClient world;
   private Set<RenderChunk> chunksToUpdate = Sets.newLinkedHashSet();
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
   private final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.newHashMap();
   private final Map<BlockPos, ISound> mapSoundPositions = Maps.newHashMap();
   private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
   private Framebuffer entityOutlineFramebuffer;
   private ShaderGroup entityOutlineShader;
   private double frustumUpdatePosX = Double.MIN_VALUE;
   private double frustumUpdatePosY = Double.MIN_VALUE;
   private double frustumUpdatePosZ = Double.MIN_VALUE;
   private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
   private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
   private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
   private double lastViewEntityX = Double.MIN_VALUE;
   private double lastViewEntityY = Double.MIN_VALUE;
   private double lastViewEntityZ = Double.MIN_VALUE;
   private double lastViewEntityPitch = Double.MIN_VALUE;
   private double lastViewEntityYaw = Double.MIN_VALUE;
   private ChunkRenderDispatcher renderDispatcher;
   private ChunkRenderContainer renderContainer;
   private int renderDistanceChunks = -1;
   private int renderEntitiesStartupCounter = 2;
   private int countEntitiesTotal;
   private int countEntitiesRendered;
   private int countEntitiesHidden;
   private boolean debugFixTerrainFrustum;
   private ClippingHelper debugFixedClippingHelper;
   private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
   private final Vector3d debugTerrainFrustumPosition = new Vector3d();
   private boolean vboEnabled;
   IRenderChunkFactory renderChunkFactory;
   private double prevRenderSortX;
   private double prevRenderSortY;
   private double prevRenderSortZ;
   private boolean displayListEntitiesDirty = true;
   private boolean entityOutlinesRendered;
   private final Set<BlockPos> setLightUpdates = Sets.newHashSet();

   public RenderGlobal(Minecraft var1) {
      this.mc = ☃;
      this.renderManager = ☃.getRenderManager();
      this.renderEngine = ☃.getTextureManager();
      this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
      GlStateManager.glTexParameteri(3553, 10242, 10497);
      GlStateManager.glTexParameteri(3553, 10243, 10497);
      GlStateManager.bindTexture(0);
      this.updateDestroyBlockIcons();
      this.vboEnabled = OpenGlHelper.useVbo();
      if (this.vboEnabled) {
         this.renderContainer = new VboRenderList();
         this.renderChunkFactory = new VboChunkFactory();
      } else {
         this.renderContainer = new RenderList();
         this.renderChunkFactory = new ListChunkFactory();
      }

      this.vertexBufferFormat = new VertexFormat();
      this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
      this.generateStars();
      this.generateSky();
      this.generateSky2();
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      this.updateDestroyBlockIcons();
   }

   private void updateDestroyBlockIcons() {
      TextureMap ☃ = this.mc.getTextureMapBlocks();

      for (int ☃x = 0; ☃x < this.destroyBlockIcons.length; ☃x++) {
         this.destroyBlockIcons[☃x] = ☃.getAtlasSprite("minecraft:blocks/destroy_stage_" + ☃x);
      }
   }

   public void makeEntityOutlineShader() {
      if (OpenGlHelper.shadersSupported) {
         if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
         }

         ResourceLocation ☃ = new ResourceLocation("shaders/post/entity_outline.json");

         try {
            this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), ☃);
            this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
         } catch (IOException var3) {
            LOGGER.warn("Failed to load shader: {}", ☃, var3);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
         } catch (JsonSyntaxException var4) {
            LOGGER.warn("Failed to load shader: {}", ☃, var4);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
         }
      } else {
         this.entityOutlineShader = null;
         this.entityOutlineFramebuffer = null;
      }
   }

   public void renderEntityOutlineFramebuffer() {
      if (this.isRenderEntityOutlines()) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ZERO,
            GlStateManager.DestFactor.ONE
         );
         this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
         GlStateManager.disableBlend();
      }
   }

   protected boolean isRenderEntityOutlines() {
      return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.player != null;
   }

   private void generateSky2() {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      if (this.sky2VBO != null) {
         this.sky2VBO.deleteGlBuffers();
      }

      if (this.glSkyList2 >= 0) {
         GLAllocation.deleteDisplayLists(this.glSkyList2);
         this.glSkyList2 = -1;
      }

      if (this.vboEnabled) {
         this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderSky(☃x, -16.0F, true);
         ☃x.finishDrawing();
         ☃x.reset();
         this.sky2VBO.bufferData(☃x.getByteBuffer());
      } else {
         this.glSkyList2 = GLAllocation.generateDisplayLists(1);
         GlStateManager.glNewList(this.glSkyList2, 4864);
         this.renderSky(☃x, -16.0F, true);
         ☃.draw();
         GlStateManager.glEndList();
      }
   }

   private void generateSky() {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      if (this.skyVBO != null) {
         this.skyVBO.deleteGlBuffers();
      }

      if (this.glSkyList >= 0) {
         GLAllocation.deleteDisplayLists(this.glSkyList);
         this.glSkyList = -1;
      }

      if (this.vboEnabled) {
         this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderSky(☃x, 16.0F, false);
         ☃x.finishDrawing();
         ☃x.reset();
         this.skyVBO.bufferData(☃x.getByteBuffer());
      } else {
         this.glSkyList = GLAllocation.generateDisplayLists(1);
         GlStateManager.glNewList(this.glSkyList, 4864);
         this.renderSky(☃x, 16.0F, false);
         ☃.draw();
         GlStateManager.glEndList();
      }
   }

   private void renderSky(BufferBuilder var1, float var2, boolean var3) {
      int ☃ = 64;
      int ☃x = 6;
      ☃.begin(7, DefaultVertexFormats.POSITION);

      for (int ☃xx = -384; ☃xx <= 384; ☃xx += 64) {
         for (int ☃xxx = -384; ☃xxx <= 384; ☃xxx += 64) {
            float ☃xxxx = ☃xx;
            float ☃xxxxx = ☃xx + 64;
            if (☃) {
               ☃xxxxx = ☃xx;
               ☃xxxx = ☃xx + 64;
            }

            ☃.pos(☃xxxx, ☃, ☃xxx).endVertex();
            ☃.pos(☃xxxxx, ☃, ☃xxx).endVertex();
            ☃.pos(☃xxxxx, ☃, ☃xxx + 64).endVertex();
            ☃.pos(☃xxxx, ☃, ☃xxx + 64).endVertex();
         }
      }
   }

   private void generateStars() {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      if (this.starVBO != null) {
         this.starVBO.deleteGlBuffers();
      }

      if (this.starGLCallList >= 0) {
         GLAllocation.deleteDisplayLists(this.starGLCallList);
         this.starGLCallList = -1;
      }

      if (this.vboEnabled) {
         this.starVBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderStars(☃x);
         ☃x.finishDrawing();
         ☃x.reset();
         this.starVBO.bufferData(☃x.getByteBuffer());
      } else {
         this.starGLCallList = GLAllocation.generateDisplayLists(1);
         GlStateManager.pushMatrix();
         GlStateManager.glNewList(this.starGLCallList, 4864);
         this.renderStars(☃x);
         ☃.draw();
         GlStateManager.glEndList();
         GlStateManager.popMatrix();
      }
   }

   private void renderStars(BufferBuilder var1) {
      Random ☃ = new Random(10842L);
      ☃.begin(7, DefaultVertexFormats.POSITION);

      for (int ☃x = 0; ☃x < 1500; ☃x++) {
         double ☃xx = ☃.nextFloat() * 2.0F - 1.0F;
         double ☃xxx = ☃.nextFloat() * 2.0F - 1.0F;
         double ☃xxxx = ☃.nextFloat() * 2.0F - 1.0F;
         double ☃xxxxx = 0.15F + ☃.nextFloat() * 0.1F;
         double ☃xxxxxx = ☃xx * ☃xx + ☃xxx * ☃xxx + ☃xxxx * ☃xxxx;
         if (☃xxxxxx < 1.0 && ☃xxxxxx > 0.01) {
            ☃xxxxxx = 1.0 / Math.sqrt(☃xxxxxx);
            ☃xx *= ☃xxxxxx;
            ☃xxx *= ☃xxxxxx;
            ☃xxxx *= ☃xxxxxx;
            double ☃xxxxxxx = ☃xx * 100.0;
            double ☃xxxxxxxx = ☃xxx * 100.0;
            double ☃xxxxxxxxx = ☃xxxx * 100.0;
            double ☃xxxxxxxxxx = Math.atan2(☃xx, ☃xxxx);
            double ☃xxxxxxxxxxx = Math.sin(☃xxxxxxxxxx);
            double ☃xxxxxxxxxxxx = Math.cos(☃xxxxxxxxxx);
            double ☃xxxxxxxxxxxxx = Math.atan2(Math.sqrt(☃xx * ☃xx + ☃xxxx * ☃xxxx), ☃xxx);
            double ☃xxxxxxxxxxxxxx = Math.sin(☃xxxxxxxxxxxxx);
            double ☃xxxxxxxxxxxxxxx = Math.cos(☃xxxxxxxxxxxxx);
            double ☃xxxxxxxxxxxxxxxx = ☃.nextDouble() * Math.PI * 2.0;
            double ☃xxxxxxxxxxxxxxxxx = Math.sin(☃xxxxxxxxxxxxxxxx);
            double ☃xxxxxxxxxxxxxxxxxx = Math.cos(☃xxxxxxxxxxxxxxxx);

            for (int ☃xxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxxx++) {
               double ☃xxxxxxxxxxxxxxxxxxxx = 0.0;
               double ☃xxxxxxxxxxxxxxxxxxxxx = ((☃xxxxxxxxxxxxxxxxxxx & 2) - 1) * ☃xxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxx = ((☃xxxxxxxxxxxxxxxxxxx + 1 & 2) - 1) * ☃xxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.0;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx + 0.0 * ☃xxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 0.0 * ☃xxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxx;
               ☃.pos(☃xxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .endVertex();
            }
         }
      }
   }

   public void setWorldAndLoadRenderers(@Nullable WorldClient var1) {
      if (this.world != null) {
         this.world.removeEventListener(this);
      }

      this.frustumUpdatePosX = Double.MIN_VALUE;
      this.frustumUpdatePosY = Double.MIN_VALUE;
      this.frustumUpdatePosZ = Double.MIN_VALUE;
      this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
      this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
      this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
      this.renderManager.setWorld(☃);
      this.world = ☃;
      if (☃ != null) {
         ☃.addEventListener(this);
         this.loadRenderers();
      } else {
         this.chunksToUpdate.clear();
         this.renderInfos.clear();
         if (this.viewFrustum != null) {
            this.viewFrustum.deleteGlResources();
            this.viewFrustum = null;
         }

         if (this.renderDispatcher != null) {
            this.renderDispatcher.stopWorkerThreads();
         }

         this.renderDispatcher = null;
      }
   }

   public void loadRenderers() {
      if (this.world != null) {
         if (this.renderDispatcher == null) {
            this.renderDispatcher = new ChunkRenderDispatcher();
         }

         this.displayListEntitiesDirty = true;
         Blocks.LEAVES.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
         Blocks.LEAVES2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
         this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
         boolean ☃ = this.vboEnabled;
         this.vboEnabled = OpenGlHelper.useVbo();
         if (☃ && !this.vboEnabled) {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
         } else if (!☃ && this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
         }

         if (☃ != this.vboEnabled) {
            this.generateStars();
            this.generateSky();
            this.generateSky2();
         }

         if (this.viewFrustum != null) {
            this.viewFrustum.deleteGlResources();
         }

         this.stopChunkUpdates();
         synchronized (this.setTileEntities) {
            this.setTileEntities.clear();
         }

         this.viewFrustum = new ViewFrustum(this.world, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
         if (this.world != null) {
            Entity ☃x = this.mc.getRenderViewEntity();
            if (☃x != null) {
               this.viewFrustum.updateChunkPositions(☃x.posX, ☃x.posZ);
            }
         }

         this.renderEntitiesStartupCounter = 2;
      }
   }

   protected void stopChunkUpdates() {
      this.chunksToUpdate.clear();
      this.renderDispatcher.stopChunkUpdates();
   }

   public void createBindEntityOutlineFbs(int var1, int var2) {
      if (OpenGlHelper.shadersSupported) {
         if (this.entityOutlineShader != null) {
            this.entityOutlineShader.createBindFramebuffers(☃, ☃);
         }
      }
   }

   public void renderEntities(Entity var1, ICamera var2, float var3) {
      if (this.renderEntitiesStartupCounter > 0) {
         this.renderEntitiesStartupCounter--;
      } else {
         double ☃ = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃;
         double ☃x = ☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃;
         double ☃xx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃;
         this.world.profiler.startSection("prepare");
         TileEntityRendererDispatcher.instance
            .prepare(this.world, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.objectMouseOver, ☃);
         this.renderManager
            .cacheActiveRenderInfo(this.world, this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, ☃);
         this.countEntitiesTotal = 0;
         this.countEntitiesRendered = 0;
         this.countEntitiesHidden = 0;
         Entity ☃xxx = this.mc.getRenderViewEntity();
         double ☃xxxx = ☃xxx.lastTickPosX + (☃xxx.posX - ☃xxx.lastTickPosX) * ☃;
         double ☃xxxxx = ☃xxx.lastTickPosY + (☃xxx.posY - ☃xxx.lastTickPosY) * ☃;
         double ☃xxxxxx = ☃xxx.lastTickPosZ + (☃xxx.posZ - ☃xxx.lastTickPosZ) * ☃;
         TileEntityRendererDispatcher.staticPlayerX = ☃xxxx;
         TileEntityRendererDispatcher.staticPlayerY = ☃xxxxx;
         TileEntityRendererDispatcher.staticPlayerZ = ☃xxxxxx;
         this.renderManager.setRenderPosition(☃xxxx, ☃xxxxx, ☃xxxxxx);
         this.mc.entityRenderer.enableLightmap();
         this.world.profiler.endStartSection("global");
         List<Entity> ☃xxxxxxx = this.world.getLoadedEntityList();
         this.countEntitiesTotal = ☃xxxxxxx.size();

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < this.world.weatherEffects.size(); ☃xxxxxxxx++) {
            Entity ☃xxxxxxxxx = this.world.weatherEffects.get(☃xxxxxxxx);
            this.countEntitiesRendered++;
            if (☃xxxxxxxxx.isInRangeToRender3d(☃, ☃x, ☃xx)) {
               this.renderManager.renderEntityStatic(☃xxxxxxxxx, ☃, false);
            }
         }

         this.world.profiler.endStartSection("entities");
         List<Entity> ☃xxxxxxxxx = Lists.newArrayList();
         List<Entity> ☃xxxxxxxxxx = Lists.newArrayList();
         BlockPos.PooledMutableBlockPos ☃xxxxxxxxxxx = BlockPos.PooledMutableBlockPos.retain();

         for (RenderGlobal.ContainerLocalRenderInformation ☃xxxxxxxxxxxx : this.renderInfos) {
            Chunk ☃xxxxxxxxxxxxx = this.world.getChunk(☃xxxxxxxxxxxx.renderChunk.getPosition());
            ClassInheritanceMultiMap<Entity> ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.getEntityLists()[☃xxxxxxxxxxxx.renderChunk.getPosition().getY() / 16];
            if (!☃xxxxxxxxxxxxxx.isEmpty()) {
               for (Entity ☃xxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxx) {
                  boolean ☃xxxxxxxxxxxxxxxx = this.renderManager.shouldRender(☃xxxxxxxxxxxxxxx, ☃, ☃, ☃x, ☃xx)
                     || ☃xxxxxxxxxxxxxxx.isRidingOrBeingRiddenBy(this.mc.player);
                  if (☃xxxxxxxxxxxxxxxx) {
                     boolean ☃xxxxxxxxxxxxxxxxx = this.mc.getRenderViewEntity() instanceof EntityLivingBase
                        ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping()
                        : false;
                     if ((☃xxxxxxxxxxxxxxx != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || ☃xxxxxxxxxxxxxxxxx)
                        && (
                           !(☃xxxxxxxxxxxxxxx.posY >= 0.0)
                              || !(☃xxxxxxxxxxxxxxx.posY < 256.0)
                              || this.world.isBlockLoaded(☃xxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxx))
                        )) {
                        this.countEntitiesRendered++;
                        this.renderManager.renderEntityStatic(☃xxxxxxxxxxxxxxx, ☃, false);
                        if (this.isOutlineActive(☃xxxxxxxxxxxxxxx, ☃xxx, ☃)) {
                           ☃xxxxxxxxx.add(☃xxxxxxxxxxxxxxx);
                        }

                        if (this.renderManager.isRenderMultipass(☃xxxxxxxxxxxxxxx)) {
                           ☃xxxxxxxxxx.add(☃xxxxxxxxxxxxxxx);
                        }
                     }
                  }
               }
            }
         }

         ☃xxxxxxxxxxx.release();
         if (!☃xxxxxxxxxx.isEmpty()) {
            for (Entity ☃xxxxxxxxxxxxx : ☃xxxxxxxxxx) {
               this.renderManager.renderMultipass(☃xxxxxxxxxxxxx, ☃);
            }
         }

         if (this.isRenderEntityOutlines() && (!☃xxxxxxxxx.isEmpty() || this.entityOutlinesRendered)) {
            this.world.profiler.endStartSection("entityOutlines");
            this.entityOutlineFramebuffer.framebufferClear();
            this.entityOutlinesRendered = !☃xxxxxxxxx.isEmpty();
            if (!☃xxxxxxxxx.isEmpty()) {
               GlStateManager.depthFunc(519);
               GlStateManager.disableFog();
               this.entityOutlineFramebuffer.bindFramebuffer(false);
               RenderHelper.disableStandardItemLighting();
               this.renderManager.setRenderOutlines(true);

               for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < ☃xxxxxxxxx.size(); ☃xxxxxxxxxxxxx++) {
                  this.renderManager.renderEntityStatic(☃xxxxxxxxx.get(☃xxxxxxxxxxxxx), ☃, false);
               }

               this.renderManager.setRenderOutlines(false);
               RenderHelper.enableStandardItemLighting();
               GlStateManager.depthMask(false);
               this.entityOutlineShader.render(☃);
               GlStateManager.enableLighting();
               GlStateManager.depthMask(true);
               GlStateManager.enableFog();
               GlStateManager.enableBlend();
               GlStateManager.enableColorMaterial();
               GlStateManager.depthFunc(515);
               GlStateManager.enableDepth();
               GlStateManager.enableAlpha();
            }

            this.mc.getFramebuffer().bindFramebuffer(false);
         }

         this.world.profiler.endStartSection("blockentities");
         RenderHelper.enableStandardItemLighting();

         for (RenderGlobal.ContainerLocalRenderInformation ☃xxxxxxxxxxxxx : this.renderInfos) {
            List<TileEntity> ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.renderChunk.getCompiledChunk().getTileEntities();
            if (!☃xxxxxxxxxxxxxx.isEmpty()) {
               for (TileEntity ☃xxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxx) {
                  TileEntityRendererDispatcher.instance.render(☃xxxxxxxxxxxxxxxx, ☃, -1);
               }
            }
         }

         synchronized (this.setTileEntities) {
            for (TileEntity ☃xxxxxxxxxxxxxx : this.setTileEntities) {
               TileEntityRendererDispatcher.instance.render(☃xxxxxxxxxxxxxx, ☃, -1);
            }
         }

         this.preRenderDamagedBlocks();

         for (DestroyBlockProgress ☃xxxxxxxxxxxxxx : this.damagedBlocks.values()) {
            BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.getPosition();
            if (this.world.getBlockState(☃xxxxxxxxxxxxxxxx).getBlock().hasTileEntity()) {
               TileEntity ☃xxxxxxxxxxxxxxxxx = this.world.getTileEntity(☃xxxxxxxxxxxxxxxx);
               if (☃xxxxxxxxxxxxxxxxx instanceof TileEntityChest) {
                  TileEntityChest ☃xxxxxxxxxxxxxxxxxx = (TileEntityChest)☃xxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxxxxxx.adjacentChestXNeg != null) {
                     ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.offset(EnumFacing.WEST);
                     ☃xxxxxxxxxxxxxxxxx = this.world.getTileEntity(☃xxxxxxxxxxxxxxxx);
                  } else if (☃xxxxxxxxxxxxxxxxxx.adjacentChestZNeg != null) {
                     ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.offset(EnumFacing.NORTH);
                     ☃xxxxxxxxxxxxxxxxx = this.world.getTileEntity(☃xxxxxxxxxxxxxxxx);
                  }
               }

               IBlockState ☃xxxxxxxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxxxxxxx);
               if (☃xxxxxxxxxxxxxxxxx != null && ☃xxxxxxxxxxxxxxxxxx.hasCustomBreakingProgress()) {
                  TileEntityRendererDispatcher.instance.render(☃xxxxxxxxxxxxxxxxx, ☃, ☃xxxxxxxxxxxxxx.getPartialBlockDamage());
               }
            }
         }

         this.postRenderDamagedBlocks();
         this.mc.entityRenderer.disableLightmap();
         this.mc.profiler.endSection();
      }
   }

   private boolean isOutlineActive(Entity var1, Entity var2, ICamera var3) {
      boolean ☃ = ☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).isPlayerSleeping();
      if (☃ == ☃ && this.mc.gameSettings.thirdPersonView == 0 && !☃) {
         return false;
      } else if (☃.isGlowing()) {
         return true;
      } else {
         return this.mc.player.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown() && ☃ instanceof EntityPlayer
            ? ☃.ignoreFrustumCheck || ☃.isBoundingBoxInFrustum(☃.getEntityBoundingBox()) || ☃.isRidingOrBeingRiddenBy(this.mc.player)
            : false;
      }
   }

   public String getDebugInfoRenders() {
      int ☃ = this.viewFrustum.renderChunks.length;
      int ☃x = this.getRenderedChunks();
      return String.format(
         "C: %d/%d %sD: %d, L: %d, %s",
         ☃x,
         ☃,
         this.mc.renderChunksMany ? "(s) " : "",
         this.renderDistanceChunks,
         this.setLightUpdates.size(),
         this.renderDispatcher == null ? "null" : this.renderDispatcher.getDebugInfo()
      );
   }

   protected int getRenderedChunks() {
      int ☃ = 0;

      for (RenderGlobal.ContainerLocalRenderInformation ☃x : this.renderInfos) {
         CompiledChunk ☃xx = ☃x.renderChunk.compiledChunk;
         if (☃xx != CompiledChunk.DUMMY && !☃xx.isEmpty()) {
            ☃++;
         }
      }

      return ☃;
   }

   public String getDebugInfoEntities() {
      return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden;
   }

   public void setupTerrain(Entity var1, double var2, ICamera var4, int var5, boolean var6) {
      if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
         this.loadRenderers();
      }

      this.world.profiler.startSection("camera");
      double ☃ = ☃.posX - this.frustumUpdatePosX;
      double ☃x = ☃.posY - this.frustumUpdatePosY;
      double ☃xx = ☃.posZ - this.frustumUpdatePosZ;
      if (this.frustumUpdatePosChunkX != ☃.chunkCoordX
         || this.frustumUpdatePosChunkY != ☃.chunkCoordY
         || this.frustumUpdatePosChunkZ != ☃.chunkCoordZ
         || ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx > 16.0) {
         this.frustumUpdatePosX = ☃.posX;
         this.frustumUpdatePosY = ☃.posY;
         this.frustumUpdatePosZ = ☃.posZ;
         this.frustumUpdatePosChunkX = ☃.chunkCoordX;
         this.frustumUpdatePosChunkY = ☃.chunkCoordY;
         this.frustumUpdatePosChunkZ = ☃.chunkCoordZ;
         this.viewFrustum.updateChunkPositions(☃.posX, ☃.posZ);
      }

      this.world.profiler.endStartSection("renderlistcamera");
      double ☃xxx = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃xxxx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xxxxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      this.renderContainer.initialize(☃xxx, ☃xxxx, ☃xxxxx);
      this.world.profiler.endStartSection("cull");
      if (this.debugFixedClippingHelper != null) {
         Frustum ☃xxxxxx = new Frustum(this.debugFixedClippingHelper);
         ☃xxxxxx.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
         ☃ = ☃xxxxxx;
      }

      this.mc.profiler.endStartSection("culling");
      BlockPos ☃xxxxxx = new BlockPos(☃xxx, ☃xxxx + ☃.getEyeHeight(), ☃xxxxx);
      RenderChunk ☃xxxxxxx = this.viewFrustum.getRenderChunk(☃xxxxxx);
      BlockPos ☃xxxxxxxx = new BlockPos(MathHelper.floor(☃xxx / 16.0) * 16, MathHelper.floor(☃xxxx / 16.0) * 16, MathHelper.floor(☃xxxxx / 16.0) * 16);
      this.displayListEntitiesDirty = this.displayListEntitiesDirty
         || !this.chunksToUpdate.isEmpty()
         || ☃.posX != this.lastViewEntityX
         || ☃.posY != this.lastViewEntityY
         || ☃.posZ != this.lastViewEntityZ
         || ☃.rotationPitch != this.lastViewEntityPitch
         || ☃.rotationYaw != this.lastViewEntityYaw;
      this.lastViewEntityX = ☃.posX;
      this.lastViewEntityY = ☃.posY;
      this.lastViewEntityZ = ☃.posZ;
      this.lastViewEntityPitch = ☃.rotationPitch;
      this.lastViewEntityYaw = ☃.rotationYaw;
      boolean ☃xxxxxxxxx = this.debugFixedClippingHelper != null;
      this.mc.profiler.endStartSection("update");
      if (!☃xxxxxxxxx && this.displayListEntitiesDirty) {
         this.displayListEntitiesDirty = false;
         this.renderInfos = Lists.newArrayList();
         Queue<RenderGlobal.ContainerLocalRenderInformation> ☃xxxxxxxxxx = Queues.newArrayDeque();
         Entity.setRenderDistanceWeight(MathHelper.clamp(this.mc.gameSettings.renderDistanceChunks / 8.0, 1.0, 2.5));
         boolean ☃xxxxxxxxxxx = this.mc.renderChunksMany;
         if (☃xxxxxxx != null) {
            boolean ☃xxxxxxxxxxxx = false;
            RenderGlobal.ContainerLocalRenderInformation ☃xxxxxxxxxxxxx = new RenderGlobal.ContainerLocalRenderInformation(☃xxxxxxx, null, 0);
            Set<EnumFacing> ☃xxxxxxxxxxxxxx = this.getVisibleFacings(☃xxxxxx);
            if (☃xxxxxxxxxxxxxx.size() == 1) {
               Vector3f ☃xxxxxxxxxxxxxxx = this.getViewVector(☃, ☃);
               EnumFacing ☃xxxxxxxxxxxxxxxx = EnumFacing.getFacingFromVector(☃xxxxxxxxxxxxxxx.x, ☃xxxxxxxxxxxxxxx.y, ☃xxxxxxxxxxxxxxx.z).getOpposite();
               ☃xxxxxxxxxxxxxx.remove(☃xxxxxxxxxxxxxxxx);
            }

            if (☃xxxxxxxxxxxxxx.isEmpty()) {
               ☃xxxxxxxxxxxx = true;
            }

            if (☃xxxxxxxxxxxx && !☃) {
               this.renderInfos.add(☃xxxxxxxxxxxxx);
            } else {
               if (☃ && this.world.getBlockState(☃xxxxxx).isOpaqueCube()) {
                  ☃xxxxxxxxxxx = false;
               }

               ☃xxxxxxx.setFrameIndex(☃);
               ☃xxxxxxxxxx.add(☃xxxxxxxxxxxxx);
            }
         } else {
            int ☃xxxxxxxxxxxxxxx = ☃xxxxxx.getY() > 0 ? 248 : 8;

            for (int ☃xxxxxxxxxxxxxxxx = -this.renderDistanceChunks; ☃xxxxxxxxxxxxxxxx <= this.renderDistanceChunks; ☃xxxxxxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxxxxxx = -this.renderDistanceChunks; ☃xxxxxxxxxxxxxxxxx <= this.renderDistanceChunks; ☃xxxxxxxxxxxxxxxxx++) {
                  RenderChunk ☃xxxxxxxxxxxxxxxxxx = this.viewFrustum
                     .getRenderChunk(new BlockPos((☃xxxxxxxxxxxxxxxx << 4) + 8, ☃xxxxxxxxxxxxxxx, (☃xxxxxxxxxxxxxxxxx << 4) + 8));
                  if (☃xxxxxxxxxxxxxxxxxx != null && ☃.isBoundingBoxInFrustum(☃xxxxxxxxxxxxxxxxxx.boundingBox)) {
                     ☃xxxxxxxxxxxxxxxxxx.setFrameIndex(☃);
                     ☃xxxxxxxxxx.add(new RenderGlobal.ContainerLocalRenderInformation(☃xxxxxxxxxxxxxxxxxx, null, 0));
                  }
               }
            }
         }

         this.mc.profiler.startSection("iteration");

         while (!☃xxxxxxxxxx.isEmpty()) {
            RenderGlobal.ContainerLocalRenderInformation ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxx.poll();
            RenderChunk ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.renderChunk;
            EnumFacing ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.facing;
            this.renderInfos.add(☃xxxxxxxxxxxxxxx);

            for (EnumFacing ☃xxxxxxxxxxxxxxxxxxx : EnumFacing.values()) {
               RenderChunk ☃xxxxxxxxxxxxxxxxxxxx = this.getRenderChunkOffset(☃xxxxxxxx, ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx);
               if ((!☃xxxxxxxxxxx || !☃xxxxxxxxxxxxxxx.hasDirection(☃xxxxxxxxxxxxxxxxxxx.getOpposite()))
                  && (
                     !☃xxxxxxxxxxx
                        || ☃xxxxxxxxxxxxxxxxxx == null
                        || ☃xxxxxxxxxxxxxxxx.getCompiledChunk().isVisible(☃xxxxxxxxxxxxxxxxxx.getOpposite(), ☃xxxxxxxxxxxxxxxxxxx)
                  )
                  && ☃xxxxxxxxxxxxxxxxxxxx != null
                  && ☃xxxxxxxxxxxxxxxxxxxx.setFrameIndex(☃)
                  && ☃.isBoundingBoxInFrustum(☃xxxxxxxxxxxxxxxxxxxx.boundingBox)) {
                  RenderGlobal.ContainerLocalRenderInformation ☃xxxxxxxxxxxxxxxxxxxxx = new RenderGlobal.ContainerLocalRenderInformation(
                     ☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx.counter + 1
                  );
                  ☃xxxxxxxxxxxxxxxxxxxxx.setDirection(☃xxxxxxxxxxxxxxx.setFacing, ☃xxxxxxxxxxxxxxxxxxx);
                  ☃xxxxxxxxxx.add(☃xxxxxxxxxxxxxxxxxxxxx);
               }
            }
         }

         this.mc.profiler.endSection();
      }

      this.mc.profiler.endStartSection("captureFrustum");
      if (this.debugFixTerrainFrustum) {
         this.fixTerrainFrustum(☃xxx, ☃xxxx, ☃xxxxx);
         this.debugFixTerrainFrustum = false;
      }

      this.mc.profiler.endStartSection("rebuildNear");
      Set<RenderChunk> ☃xxxxxxxxxx = this.chunksToUpdate;
      this.chunksToUpdate = Sets.newLinkedHashSet();

      for (RenderGlobal.ContainerLocalRenderInformation ☃xxxxxxxxxxx : this.renderInfos) {
         RenderChunk ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxx.renderChunk;
         if (☃xxxxxxxxxxxxxxx.needsUpdate() || ☃xxxxxxxxxx.contains(☃xxxxxxxxxxxxxxx)) {
            this.displayListEntitiesDirty = true;
            BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.getPosition().add(8, 8, 8);
            boolean ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.distanceSq(☃xxxxxx) < 768.0;
            if (!☃xxxxxxxxxxxxxxx.needsImmediateUpdate() && !☃xxxxxxxxxxxxxxxxxx) {
               this.chunksToUpdate.add(☃xxxxxxxxxxxxxxx);
            } else {
               this.mc.profiler.startSection("build near");
               this.renderDispatcher.updateChunkNow(☃xxxxxxxxxxxxxxx);
               ☃xxxxxxxxxxxxxxx.clearNeedsUpdate();
               this.mc.profiler.endSection();
            }
         }
      }

      this.chunksToUpdate.addAll(☃xxxxxxxxxx);
      this.mc.profiler.endSection();
   }

   private Set<EnumFacing> getVisibleFacings(BlockPos var1) {
      VisGraph ☃ = new VisGraph();
      BlockPos ☃x = new BlockPos(☃.getX() >> 4 << 4, ☃.getY() >> 4 << 4, ☃.getZ() >> 4 << 4);
      Chunk ☃xx = this.world.getChunk(☃x);

      for (BlockPos.MutableBlockPos ☃xxx : BlockPos.getAllInBoxMutable(☃x, ☃x.add(15, 15, 15))) {
         if (☃xx.getBlockState(☃xxx).isOpaqueCube()) {
            ☃.setOpaqueCube(☃xxx);
         }
      }

      return ☃.getVisibleFacings(☃);
   }

   @Nullable
   private RenderChunk getRenderChunkOffset(BlockPos var1, RenderChunk var2, EnumFacing var3) {
      BlockPos ☃ = ☃.getBlockPosOffset16(☃);
      if (MathHelper.abs(☃.getX() - ☃.getX()) > this.renderDistanceChunks * 16) {
         return null;
      } else if (☃.getY() < 0 || ☃.getY() >= 256) {
         return null;
      } else {
         return MathHelper.abs(☃.getZ() - ☃.getZ()) > this.renderDistanceChunks * 16 ? null : this.viewFrustum.getRenderChunk(☃);
      }
   }

   private void fixTerrainFrustum(double var1, double var3, double var5) {
      this.debugFixedClippingHelper = new ClippingHelperImpl();
      ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
      Matrix4f ☃ = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
      ☃.transpose();
      Matrix4f ☃x = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
      ☃x.transpose();
      Matrix4f ☃xx = new Matrix4f();
      Matrix4f.mul(☃x, ☃, ☃xx);
      ☃xx.invert();
      this.debugTerrainFrustumPosition.x = ☃;
      this.debugTerrainFrustumPosition.y = ☃;
      this.debugTerrainFrustumPosition.z = ☃;
      this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);

      for (int ☃xxx = 0; ☃xxx < 8; ☃xxx++) {
         Matrix4f.transform(☃xx, this.debugTerrainMatrix[☃xxx], this.debugTerrainMatrix[☃xxx]);
         this.debugTerrainMatrix[☃xxx].x = this.debugTerrainMatrix[☃xxx].x / this.debugTerrainMatrix[☃xxx].w;
         this.debugTerrainMatrix[☃xxx].y = this.debugTerrainMatrix[☃xxx].y / this.debugTerrainMatrix[☃xxx].w;
         this.debugTerrainMatrix[☃xxx].z = this.debugTerrainMatrix[☃xxx].z / this.debugTerrainMatrix[☃xxx].w;
         this.debugTerrainMatrix[☃xxx].w = 1.0F;
      }
   }

   protected Vector3f getViewVector(Entity var1, double var2) {
      float ☃ = (float)(☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃);
      float ☃x = (float)(☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃);
      if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
         ☃ += 180.0F;
      }

      float ☃xx = MathHelper.cos(-☃x * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xxx = MathHelper.sin(-☃x * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xxxx = -MathHelper.cos(-☃ * (float) (Math.PI / 180.0));
      float ☃xxxxx = MathHelper.sin(-☃ * (float) (Math.PI / 180.0));
      return new Vector3f(☃xxx * ☃xxxx, ☃xxxxx, ☃xx * ☃xxxx);
   }

   public int renderBlockLayer(BlockRenderLayer var1, double var2, int var4, Entity var5) {
      RenderHelper.disableStandardItemLighting();
      if (☃ == BlockRenderLayer.TRANSLUCENT) {
         this.mc.profiler.startSection("translucent_sort");
         double ☃ = ☃.posX - this.prevRenderSortX;
         double ☃x = ☃.posY - this.prevRenderSortY;
         double ☃xx = ☃.posZ - this.prevRenderSortZ;
         if (☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx > 1.0) {
            this.prevRenderSortX = ☃.posX;
            this.prevRenderSortY = ☃.posY;
            this.prevRenderSortZ = ☃.posZ;
            int ☃xxx = 0;

            for (RenderGlobal.ContainerLocalRenderInformation ☃xxxx : this.renderInfos) {
               if (☃xxxx.renderChunk.compiledChunk.isLayerStarted(☃) && ☃xxx++ < 15) {
                  this.renderDispatcher.updateTransparencyLater(☃xxxx.renderChunk);
               }
            }
         }

         this.mc.profiler.endSection();
      }

      this.mc.profiler.startSection("filterempty");
      int ☃ = 0;
      boolean ☃x = ☃ == BlockRenderLayer.TRANSLUCENT;
      int ☃xx = ☃x ? this.renderInfos.size() - 1 : 0;
      int ☃xxx = ☃x ? -1 : this.renderInfos.size();
      int ☃xxxxx = ☃x ? -1 : 1;

      for (int ☃xxxxxx = ☃xx; ☃xxxxxx != ☃xxx; ☃xxxxxx += ☃xxxxx) {
         RenderChunk ☃xxxxxxx = this.renderInfos.get(☃xxxxxx).renderChunk;
         if (!☃xxxxxxx.getCompiledChunk().isLayerEmpty(☃)) {
            ☃++;
            this.renderContainer.addRenderChunk(☃xxxxxxx, ☃);
         }
      }

      this.mc.profiler.func_194339_b(() -> "render_" + ☃);
      this.renderBlockLayer(☃);
      this.mc.profiler.endSection();
      return ☃;
   }

   private void renderBlockLayer(BlockRenderLayer var1) {
      this.mc.entityRenderer.enableLightmap();
      if (OpenGlHelper.useVbo()) {
         GlStateManager.glEnableClientState(32884);
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.glEnableClientState(32888);
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.glEnableClientState(32888);
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.glEnableClientState(32886);
      }

      this.renderContainer.renderChunkLayer(☃);
      if (OpenGlHelper.useVbo()) {
         for (VertexFormatElement ☃ : DefaultVertexFormats.BLOCK.getElements()) {
            VertexFormatElement.EnumUsage ☃x = ☃.getUsage();
            int ☃xx = ☃.getIndex();
            switch (☃x) {
               case POSITION:
                  GlStateManager.glDisableClientState(32884);
                  break;
               case UV:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + ☃xx);
                  GlStateManager.glDisableClientState(32888);
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case COLOR:
                  GlStateManager.glDisableClientState(32886);
                  GlStateManager.resetColor();
            }
         }
      }

      this.mc.entityRenderer.disableLightmap();
   }

   private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> var1) {
      while (☃.hasNext()) {
         DestroyBlockProgress ☃ = ☃.next();
         int ☃x = ☃.getCreationCloudUpdateTick();
         if (this.cloudTickCounter - ☃x > 400) {
            ☃.remove();
         }
      }
   }

   public void updateClouds() {
      this.cloudTickCounter++;
      if (this.cloudTickCounter % 20 == 0) {
         this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
      }

      if (!this.setLightUpdates.isEmpty() && !this.renderDispatcher.hasNoFreeRenderBuilders() && this.chunksToUpdate.isEmpty()) {
         Iterator<BlockPos> ☃ = this.setLightUpdates.iterator();

         while (☃.hasNext()) {
            BlockPos ☃x = ☃.next();
            ☃.remove();
            int ☃xx = ☃x.getX();
            int ☃xxx = ☃x.getY();
            int ☃xxxx = ☃x.getZ();
            this.markBlocksForUpdate(☃xx - 1, ☃xxx - 1, ☃xxxx - 1, ☃xx + 1, ☃xxx + 1, ☃xxxx + 1, false);
         }
      }
   }

   private void renderSkyEnd() {
      GlStateManager.disableFog();
      GlStateManager.disableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      RenderHelper.disableStandardItemLighting();
      GlStateManager.depthMask(false);
      this.renderEngine.bindTexture(END_SKY_TEXTURES);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();

      for (int ☃xx = 0; ☃xx < 6; ☃xx++) {
         GlStateManager.pushMatrix();
         if (☃xx == 1) {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
         }

         if (☃xx == 2) {
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
         }

         if (☃xx == 3) {
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
         }

         if (☃xx == 4) {
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
         }

         if (☃xx == 5) {
            GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
         }

         ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         ☃x.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(40, 40, 40, 255).endVertex();
         ☃x.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(40, 40, 40, 255).endVertex();
         ☃x.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(40, 40, 40, 255).endVertex();
         ☃x.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(40, 40, 40, 255).endVertex();
         ☃.draw();
         GlStateManager.popMatrix();
      }

      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.enableAlpha();
   }

   public void renderSky(float var1, int var2) {
      if (this.mc.world.provider.getDimensionType().getId() == 1) {
         this.renderSkyEnd();
      } else if (this.mc.world.provider.isSurfaceWorld()) {
         GlStateManager.disableTexture2D();
         Vec3d ☃ = this.world.getSkyColor(this.mc.getRenderViewEntity(), ☃);
         float ☃x = (float)☃.x;
         float ☃xx = (float)☃.y;
         float ☃xxx = (float)☃.z;
         if (☃ != 2) {
            float ☃xxxx = (☃x * 30.0F + ☃xx * 59.0F + ☃xxx * 11.0F) / 100.0F;
            float ☃xxxxx = (☃x * 30.0F + ☃xx * 70.0F) / 100.0F;
            float ☃xxxxxx = (☃x * 30.0F + ☃xxx * 70.0F) / 100.0F;
            ☃x = ☃xxxx;
            ☃xx = ☃xxxxx;
            ☃xxx = ☃xxxxxx;
         }

         GlStateManager.color(☃x, ☃xx, ☃xxx);
         Tessellator ☃xxxx = Tessellator.getInstance();
         BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
         GlStateManager.depthMask(false);
         GlStateManager.enableFog();
         GlStateManager.color(☃x, ☃xx, ☃xxx);
         if (this.vboEnabled) {
            this.skyVBO.bindBuffer();
            GlStateManager.glEnableClientState(32884);
            GlStateManager.glVertexPointer(3, 5126, 12, 0);
            this.skyVBO.drawArrays(7);
            this.skyVBO.unbindBuffer();
            GlStateManager.glDisableClientState(32884);
         } else {
            GlStateManager.callList(this.glSkyList);
         }

         GlStateManager.disableFog();
         GlStateManager.disableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         RenderHelper.disableStandardItemLighting();
         float[] ☃xxxxxx = this.world.provider.calcSunriseSunsetColors(this.world.getCelestialAngle(☃), ☃);
         if (☃xxxxxx != null) {
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(MathHelper.sin(this.world.getCelestialAngleRadians(☃)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            float ☃xxxxxxx = ☃xxxxxx[0];
            float ☃xxxxxxxx = ☃xxxxxx[1];
            float ☃xxxxxxxxx = ☃xxxxxx[2];
            if (☃ != 2) {
               float ☃xxxxxxxxxx = (☃xxxxxxx * 30.0F + ☃xxxxxxxx * 59.0F + ☃xxxxxxxxx * 11.0F) / 100.0F;
               float ☃xxxxxxxxxxx = (☃xxxxxxx * 30.0F + ☃xxxxxxxx * 70.0F) / 100.0F;
               float ☃xxxxxxxxxxxx = (☃xxxxxxx * 30.0F + ☃xxxxxxxxx * 70.0F) / 100.0F;
               ☃xxxxxxx = ☃xxxxxxxxxx;
               ☃xxxxxxxx = ☃xxxxxxxxxxx;
               ☃xxxxxxxxx = ☃xxxxxxxxxxxx;
            }

            ☃xxxxx.begin(6, DefaultVertexFormats.POSITION_COLOR);
            ☃xxxxx.pos(0.0, 100.0, 0.0).color(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxx[3]).endVertex();
            int ☃xxxxxxxxxx = 16;

            for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx <= 16; ☃xxxxxxxxxxx++) {
               float ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx * (float) (Math.PI * 2) / 16.0F;
               float ☃xxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxx);
               float ☃xxxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxx);
               ☃xxxxx.pos(☃xxxxxxxxxxxxx * 120.0F, ☃xxxxxxxxxxxxxx * 120.0F, -☃xxxxxxxxxxxxxx * 40.0F * ☃xxxxxx[3])
                  .color(☃xxxxxx[0], ☃xxxxxx[1], ☃xxxxxx[2], 0.0F)
                  .endVertex();
            }

            ☃xxxx.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
         }

         GlStateManager.enableTexture2D();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
         );
         GlStateManager.pushMatrix();
         float ☃xxxxxxx = 1.0F - this.world.getRainStrength(☃);
         GlStateManager.color(1.0F, 1.0F, 1.0F, ☃xxxxxxx);
         GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(this.world.getCelestialAngle(☃) * 360.0F, 1.0F, 0.0F, 0.0F);
         float ☃xxxxxxxx = 30.0F;
         this.renderEngine.bindTexture(SUN_TEXTURES);
         ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃xxxxx.pos(-☃xxxxxxxx, 100.0, -☃xxxxxxxx).tex(0.0, 0.0).endVertex();
         ☃xxxxx.pos(☃xxxxxxxx, 100.0, -☃xxxxxxxx).tex(1.0, 0.0).endVertex();
         ☃xxxxx.pos(☃xxxxxxxx, 100.0, ☃xxxxxxxx).tex(1.0, 1.0).endVertex();
         ☃xxxxx.pos(-☃xxxxxxxx, 100.0, ☃xxxxxxxx).tex(0.0, 1.0).endVertex();
         ☃xxxx.draw();
         ☃xxxxxxxx = 20.0F;
         this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
         int ☃xxxxxxxxx = this.world.getMoonPhase();
         int ☃xxxxxxxxxx = ☃xxxxxxxxx % 4;
         int ☃xxxxxxxxxxx = ☃xxxxxxxxx / 4 % 2;
         float ☃xxxxxxxxxxxx = (☃xxxxxxxxxx + 0) / 4.0F;
         float ☃xxxxxxxxxxxxx = (☃xxxxxxxxxxx + 0) / 2.0F;
         float ☃xxxxxxxxxxxxxx = (☃xxxxxxxxxx + 1) / 4.0F;
         float ☃xxxxxxxxxxxxxxx = (☃xxxxxxxxxxx + 1) / 2.0F;
         ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃xxxxx.pos(-☃xxxxxxxx, -100.0, ☃xxxxxxxx).tex(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx).endVertex();
         ☃xxxxx.pos(☃xxxxxxxx, -100.0, ☃xxxxxxxx).tex(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx).endVertex();
         ☃xxxxx.pos(☃xxxxxxxx, -100.0, -☃xxxxxxxx).tex(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx).endVertex();
         ☃xxxxx.pos(-☃xxxxxxxx, -100.0, -☃xxxxxxxx).tex(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx).endVertex();
         ☃xxxx.draw();
         GlStateManager.disableTexture2D();
         float ☃xxxxxxxxxxxxxxxx = this.world.getStarBrightness(☃) * ☃xxxxxxx;
         if (☃xxxxxxxxxxxxxxxx > 0.0F) {
            GlStateManager.color(☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
            if (this.vboEnabled) {
               this.starVBO.bindBuffer();
               GlStateManager.glEnableClientState(32884);
               GlStateManager.glVertexPointer(3, 5126, 12, 0);
               this.starVBO.drawArrays(7);
               this.starVBO.unbindBuffer();
               GlStateManager.glDisableClientState(32884);
            } else {
               GlStateManager.callList(this.starGLCallList);
            }
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.enableAlpha();
         GlStateManager.enableFog();
         GlStateManager.popMatrix();
         GlStateManager.disableTexture2D();
         GlStateManager.color(0.0F, 0.0F, 0.0F);
         double ☃xxxxxxxxxxxxxxxxx = this.mc.player.getPositionEyes(☃).y - this.world.getHorizon();
         if (☃xxxxxxxxxxxxxxxxx < 0.0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 12.0F, 0.0F);
            if (this.vboEnabled) {
               this.sky2VBO.bindBuffer();
               GlStateManager.glEnableClientState(32884);
               GlStateManager.glVertexPointer(3, 5126, 12, 0);
               this.sky2VBO.drawArrays(7);
               this.sky2VBO.unbindBuffer();
               GlStateManager.glDisableClientState(32884);
            } else {
               GlStateManager.callList(this.glSkyList2);
            }

            GlStateManager.popMatrix();
            float ☃xxxxxxxxxxxxxxxxxx = 1.0F;
            float ☃xxxxxxxxxxxxxxxxxxx = -((float)(☃xxxxxxxxxxxxxxxxx + 65.0));
            float ☃xxxxxxxxxxxxxxxxxxxx = -1.0F;
            ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_COLOR);
            ☃xxxxx.pos(-1.0, ☃xxxxxxxxxxxxxxxxxxx, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, ☃xxxxxxxxxxxxxxxxxxx, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, ☃xxxxxxxxxxxxxxxxxxx, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, ☃xxxxxxxxxxxxxxxxxxx, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, ☃xxxxxxxxxxxxxxxxxxx, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, ☃xxxxxxxxxxxxxxxxxxx, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, ☃xxxxxxxxxxxxxxxxxxx, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, ☃xxxxxxxxxxxxxxxxxxx, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxxx.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
            ☃xxxx.draw();
         }

         if (this.world.provider.isSkyColored()) {
            GlStateManager.color(☃x * 0.2F + 0.04F, ☃xx * 0.2F + 0.04F, ☃xxx * 0.6F + 0.1F);
         } else {
            GlStateManager.color(☃x, ☃xx, ☃xxx);
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, -((float)(☃xxxxxxxxxxxxxxxxx - 16.0)), 0.0F);
         GlStateManager.callList(this.glSkyList2);
         GlStateManager.popMatrix();
         GlStateManager.enableTexture2D();
         GlStateManager.depthMask(true);
      }
   }

   public void renderClouds(float var1, int var2, double var3, double var5, double var7) {
      if (this.mc.world.provider.isSurfaceWorld()) {
         if (this.mc.gameSettings.shouldRenderClouds() == 2) {
            this.renderCloudsFancy(☃, ☃, ☃, ☃, ☃);
         } else {
            GlStateManager.disableCull();
            int ☃ = 32;
            int ☃x = 8;
            Tessellator ☃xx = Tessellator.getInstance();
            BufferBuilder ☃xxx = ☃xx.getBuffer();
            this.renderEngine.bindTexture(CLOUDS_TEXTURES);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            Vec3d ☃xxxx = this.world.getCloudColour(☃);
            float ☃xxxxx = (float)☃xxxx.x;
            float ☃xxxxxx = (float)☃xxxx.y;
            float ☃xxxxxxx = (float)☃xxxx.z;
            if (☃ != 2) {
               float ☃xxxxxxxx = (☃xxxxx * 30.0F + ☃xxxxxx * 59.0F + ☃xxxxxxx * 11.0F) / 100.0F;
               float ☃xxxxxxxxx = (☃xxxxx * 30.0F + ☃xxxxxx * 70.0F) / 100.0F;
               float ☃xxxxxxxxxx = (☃xxxxx * 30.0F + ☃xxxxxxx * 70.0F) / 100.0F;
               ☃xxxxx = ☃xxxxxxxx;
               ☃xxxxxx = ☃xxxxxxxxx;
               ☃xxxxxxx = ☃xxxxxxxxxx;
            }

            float ☃xxxxxxxx = 4.8828125E-4F;
            double ☃xxxxxxxxx = this.cloudTickCounter + ☃;
            double ☃xxxxxxxxxx = ☃ + ☃xxxxxxxxx * 0.03F;
            int ☃xxxxxxxxxxx = MathHelper.floor(☃xxxxxxxxxx / 2048.0);
            int ☃xxxxxxxxxxxx = MathHelper.floor(☃ / 2048.0);
            ☃xxxxxxxxxx -= ☃xxxxxxxxxxx * 2048;
            double var22 = ☃ - ☃xxxxxxxxxxxx * 2048;
            float ☃xxxxxxxxxxxxx = this.world.provider.getCloudHeight() - (float)☃ + 0.33F;
            float ☃xxxxxxxxxxxxxx = (float)(☃xxxxxxxxxx * 4.8828125E-4);
            float ☃xxxxxxxxxxxxxxx = (float)(var22 * 4.8828125E-4);
            ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

            for (int ☃xxxxxxxxxxxxxxxx = -256; ☃xxxxxxxxxxxxxxxx < 256; ☃xxxxxxxxxxxxxxxx += 32) {
               for (int ☃xxxxxxxxxxxxxxxxx = -256; ☃xxxxxxxxxxxxxxxxx < 256; ☃xxxxxxxxxxxxxxxxx += 32) {
                  ☃xxx.pos(☃xxxxxxxxxxxxxxxx + 0, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx + 32)
                     .tex((☃xxxxxxxxxxxxxxxx + 0) * 4.8828125E-4F + ☃xxxxxxxxxxxxxx, (☃xxxxxxxxxxxxxxxxx + 32) * 4.8828125E-4F + ☃xxxxxxxxxxxxxxx)
                     .color(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, 0.8F)
                     .endVertex();
                  ☃xxx.pos(☃xxxxxxxxxxxxxxxx + 32, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx + 32)
                     .tex((☃xxxxxxxxxxxxxxxx + 32) * 4.8828125E-4F + ☃xxxxxxxxxxxxxx, (☃xxxxxxxxxxxxxxxxx + 32) * 4.8828125E-4F + ☃xxxxxxxxxxxxxxx)
                     .color(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, 0.8F)
                     .endVertex();
                  ☃xxx.pos(☃xxxxxxxxxxxxxxxx + 32, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx + 0)
                     .tex((☃xxxxxxxxxxxxxxxx + 32) * 4.8828125E-4F + ☃xxxxxxxxxxxxxx, (☃xxxxxxxxxxxxxxxxx + 0) * 4.8828125E-4F + ☃xxxxxxxxxxxxxxx)
                     .color(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, 0.8F)
                     .endVertex();
                  ☃xxx.pos(☃xxxxxxxxxxxxxxxx + 0, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx + 0)
                     .tex((☃xxxxxxxxxxxxxxxx + 0) * 4.8828125E-4F + ☃xxxxxxxxxxxxxx, (☃xxxxxxxxxxxxxxxxx + 0) * 4.8828125E-4F + ☃xxxxxxxxxxxxxxx)
                     .color(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, 0.8F)
                     .endVertex();
               }
            }

            ☃xx.draw();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableCull();
         }
      }
   }

   public boolean hasCloudFog(double var1, double var3, double var5, float var7) {
      return false;
   }

   private void renderCloudsFancy(float var1, int var2, double var3, double var5, double var7) {
      GlStateManager.disableCull();
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      float ☃xx = 12.0F;
      float ☃xxx = 4.0F;
      double ☃xxxx = this.cloudTickCounter + ☃;
      double ☃xxxxx = (☃ + ☃xxxx * 0.03F) / 12.0;
      double ☃xxxxxx = ☃ / 12.0 + 0.33F;
      float ☃xxxxxxx = this.world.provider.getCloudHeight() - (float)☃ + 0.33F;
      int ☃xxxxxxxx = MathHelper.floor(☃xxxxx / 2048.0);
      int ☃xxxxxxxxx = MathHelper.floor(☃xxxxxx / 2048.0);
      ☃xxxxx -= ☃xxxxxxxx * 2048;
      ☃xxxxxx -= ☃xxxxxxxxx * 2048;
      this.renderEngine.bindTexture(CLOUDS_TEXTURES);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      Vec3d ☃xxxxxxxxxx = this.world.getCloudColour(☃);
      float ☃xxxxxxxxxxx = (float)☃xxxxxxxxxx.x;
      float ☃xxxxxxxxxxxx = (float)☃xxxxxxxxxx.y;
      float ☃xxxxxxxxxxxxx = (float)☃xxxxxxxxxx.z;
      if (☃ != 2) {
         float ☃xxxxxxxxxxxxxx = (☃xxxxxxxxxxx * 30.0F + ☃xxxxxxxxxxxx * 59.0F + ☃xxxxxxxxxxxxx * 11.0F) / 100.0F;
         float ☃xxxxxxxxxxxxxxx = (☃xxxxxxxxxxx * 30.0F + ☃xxxxxxxxxxxx * 70.0F) / 100.0F;
         float ☃xxxxxxxxxxxxxxxx = (☃xxxxxxxxxxx * 30.0F + ☃xxxxxxxxxxxxx * 70.0F) / 100.0F;
         ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxxx;
         ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx;
         ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
      }

      float ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxx * 0.9F;
      float ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * 0.9F;
      float ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx * 0.9F;
      float ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx * 0.7F;
      float ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * 0.7F;
      float ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx * 0.7F;
      float ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx * 0.8F;
      float ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * 0.8F;
      float ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx * 0.8F;
      float ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.00390625F;
      float ☃xxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxx) * 0.00390625F;
      float ☃xxxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.floor(☃xxxxxx) * 0.00390625F;
      float ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = (float)(☃xxxxx - MathHelper.floor(☃xxxxx));
      float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = (float)(☃xxxxxx - MathHelper.floor(☃xxxxxx));
      int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = 8;
      int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 4;
      float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 9.765625E-4F;
      GlStateManager.scale(12.0F, 1.0F, 12.0F);

      for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 2; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
         if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == 0) {
            GlStateManager.colorMask(false, false, false, false);
         } else {
            switch (☃) {
               case 0:
                  GlStateManager.colorMask(false, true, true, true);
                  break;
               case 1:
                  GlStateManager.colorMask(true, false, false, true);
                  break;
               case 2:
                  GlStateManager.colorMask(true, true, true, true);
            }
         }

         for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = -3; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx <= 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = -3; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx <= 4; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 8;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 8;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
               float ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx;
               if (☃xxxxxxx > -5.0F) {
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F, ☃xxxxxxx + 0.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, -1.0F, 0.0F)
                     .endVertex();
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F, ☃xxxxxxx + 0.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, -1.0F, 0.0F)
                     .endVertex();
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F, ☃xxxxxxx + 0.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, -1.0F, 0.0F)
                     .endVertex();
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F, ☃xxxxxxx + 0.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, -1.0F, 0.0F)
                     .endVertex();
               }

               if (☃xxxxxxx <= 5.0F) {
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F, ☃xxxxxxx + 4.0F - 9.765625E-4F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, 1.0F, 0.0F)
                     .endVertex();
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F, ☃xxxxxxx + 4.0F - 9.765625E-4F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, 1.0F, 0.0F)
                     .endVertex();
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F, ☃xxxxxxx + 4.0F - 9.765625E-4F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, 1.0F, 0.0F)
                     .endVertex();
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F, ☃xxxxxxx + 4.0F - 9.765625E-4F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F)
                     .tex(
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                        (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                     )
                     .color(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 0.8F)
                     .normal(0.0F, 1.0F, 0.0F)
                     .endVertex();
               }

               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > -1) {
                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(-1.0F, 0.0F, 0.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(-1.0F, 0.0F, 0.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(-1.0F, 0.0F, 0.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(-1.0F, 0.0F, 0.0F)
                        .endVertex();
                  }
               }

               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx <= 1) {
                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(1.0F, 0.0F, 0.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(1.0F, 0.0F, 0.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(1.0F, 0.0F, 0.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx, 0.8F)
                        .normal(1.0F, 0.0F, 0.0F)
                        .endVertex();
                  }
               }

               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > -1) {
                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, -1.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, -1.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, -1.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, -1.0F)
                        .endVertex();
                  }
               }

               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx <= 1) {
                  for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < 8; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F,
                           ☃xxxxxxx + 4.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 8.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
                     ☃x.pos(
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F,
                           ☃xxxxxxx + 0.0F,
                           ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 1.0F - 9.765625E-4F
                        )
                        .tex(
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.0F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                           (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 0.5F) * 0.00390625F + ☃xxxxxxxxxxxxxxxxxxxxxxxxx
                        )
                        .color(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, 0.8F)
                        .normal(0.0F, 0.0F, 1.0F)
                        .endVertex();
                  }
               }

               ☃.draw();
            }
         }
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.enableCull();
   }

   public void updateChunks(long var1) {
      this.displayListEntitiesDirty = this.displayListEntitiesDirty | this.renderDispatcher.runChunkUploads(☃);
      if (!this.chunksToUpdate.isEmpty()) {
         Iterator<RenderChunk> ☃ = this.chunksToUpdate.iterator();

         while (☃.hasNext()) {
            RenderChunk ☃x = ☃.next();
            boolean ☃xx;
            if (☃x.needsImmediateUpdate()) {
               ☃xx = this.renderDispatcher.updateChunkNow(☃x);
            } else {
               ☃xx = this.renderDispatcher.updateChunkLater(☃x);
            }

            if (!☃xx) {
               break;
            }

            ☃x.clearNeedsUpdate();
            ☃.remove();
            long ☃xxx = ☃ - System.nanoTime();
            if (☃xxx < 0L) {
               break;
            }
         }
      }
   }

   public void renderWorldBorder(Entity var1, float var2) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      WorldBorder ☃xx = this.world.getWorldBorder();
      double ☃xxx = this.mc.gameSettings.renderDistanceChunks * 16;
      if (!(☃.posX < ☃xx.maxX() - ☃xxx) || !(☃.posX > ☃xx.minX() + ☃xxx) || !(☃.posZ < ☃xx.maxZ() - ☃xxx) || !(☃.posZ > ☃xx.minZ() + ☃xxx)) {
         double ☃xxxx = 1.0 - ☃xx.getClosestDistance(☃) / ☃xxx;
         ☃xxxx = Math.pow(☃xxxx, 4.0);
         double ☃xxxxx = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
         double ☃xxxxxx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
         double ☃xxxxxxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
         );
         this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         int ☃xxxxxxxx = ☃xx.getStatus().getColor();
         float ☃xxxxxxxxx = (☃xxxxxxxx >> 16 & 0xFF) / 255.0F;
         float ☃xxxxxxxxxx = (☃xxxxxxxx >> 8 & 0xFF) / 255.0F;
         float ☃xxxxxxxxxxx = (☃xxxxxxxx & 0xFF) / 255.0F;
         GlStateManager.color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, (float)☃xxxx);
         GlStateManager.doPolygonOffset(-3.0F, -3.0F);
         GlStateManager.enablePolygonOffset();
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.enableAlpha();
         GlStateManager.disableCull();
         float ☃xxxxxxxxxxxx = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
         float ☃xxxxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxxxxx = 128.0F;
         ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃x.setTranslation(-☃xxxxx, -☃xxxxxx, -☃xxxxxxx);
         double ☃xxxxxxxxxxxxxxxx = Math.max((double)MathHelper.floor(☃xxxxxxx - ☃xxx), ☃xx.minZ());
         double ☃xxxxxxxxxxxxxxxxx = Math.min((double)MathHelper.ceil(☃xxxxxxx + ☃xxx), ☃xx.maxZ());
         if (☃xxxxx > ☃xx.maxX() - ☃xxx) {
            float ☃xxxxxxxxxxxxxxxxxx = 0.0F;

            for (double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxx += 0.5F) {
               double ☃xxxxxxxxxxxxxxxxxxxx = Math.min(1.0, ☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx);
               float ☃xxxxxxxxxxxxxxxxxxxxx = (float)☃xxxxxxxxxxxxxxxxxxxx * 0.5F;
               ☃x.pos(☃xx.maxX(), 256.0, ☃xxxxxxxxxxxxxxxxxxx).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F).endVertex();
               ☃x.pos(☃xx.maxX(), 256.0, ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx)
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F)
                  .endVertex();
               ☃x.pos(☃xx.maxX(), 0.0, ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx)
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F)
                  .endVertex();
               ☃x.pos(☃xx.maxX(), 0.0, ☃xxxxxxxxxxxxxxxxxxx).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx++;
            }
         }

         if (☃xxxxx < ☃xx.minX() + ☃xxx) {
            float ☃xxxxxxxxxxxxxxxxxx = 0.0F;

            for (double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxx += 0.5F) {
               double ☃xxxxxxxxxxxxxxxxxxxx = Math.min(1.0, ☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx);
               float ☃xxxxxxxxxxxxxxxxxxxxx = (float)☃xxxxxxxxxxxxxxxxxxxx * 0.5F;
               ☃x.pos(☃xx.minX(), 256.0, ☃xxxxxxxxxxxxxxxxxxx).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F).endVertex();
               ☃x.pos(☃xx.minX(), 256.0, ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx)
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F)
                  .endVertex();
               ☃x.pos(☃xx.minX(), 0.0, ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx)
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F)
                  .endVertex();
               ☃x.pos(☃xx.minX(), 0.0, ☃xxxxxxxxxxxxxxxxxxx).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx++;
            }
         }

         ☃xxxxxxxxxxxxxxxx = Math.max((double)MathHelper.floor(☃xxxxx - ☃xxx), ☃xx.minX());
         ☃xxxxxxxxxxxxxxxxx = Math.min((double)MathHelper.ceil(☃xxxxx + ☃xxx), ☃xx.maxX());
         if (☃xxxxxxx > ☃xx.maxZ() - ☃xxx) {
            float ☃xxxxxxxxxxxxxxxxxx = 0.0F;

            for (double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxx += 0.5F) {
               double ☃xxxxxxxxxxxxxxxxxxxx = Math.min(1.0, ☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx);
               float ☃xxxxxxxxxxxxxxxxxxxxx = (float)☃xxxxxxxxxxxxxxxxxxxx * 0.5F;
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx, 256.0, ☃xx.maxZ()).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F).endVertex();
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx, 256.0, ☃xx.maxZ())
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F)
                  .endVertex();
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx, 0.0, ☃xx.maxZ())
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F)
                  .endVertex();
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx, 0.0, ☃xx.maxZ()).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx++;
            }
         }

         if (☃xxxxxxx < ☃xx.minZ() + ☃xxx) {
            float ☃xxxxxxxxxxxxxxxxxx = 0.0F;

            for (double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxx += 0.5F) {
               double ☃xxxxxxxxxxxxxxxxxxxx = Math.min(1.0, ☃xxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxx);
               float ☃xxxxxxxxxxxxxxxxxxxxx = (float)☃xxxxxxxxxxxxxxxxxxxx * 0.5F;
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx, 256.0, ☃xx.minZ()).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F).endVertex();
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx, 256.0, ☃xx.minZ())
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 0.0F)
                  .endVertex();
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx, 0.0, ☃xx.minZ())
                  .tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F)
                  .endVertex();
               ☃x.pos(☃xxxxxxxxxxxxxxxxxxx, 0.0, ☃xx.minZ()).tex(☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx + 128.0F).endVertex();
               ☃xxxxxxxxxxxxxxxxxxx++;
            }
         }

         ☃.draw();
         ☃x.setTranslation(0.0, 0.0, 0.0);
         GlStateManager.enableCull();
         GlStateManager.disableAlpha();
         GlStateManager.doPolygonOffset(0.0F, 0.0F);
         GlStateManager.disablePolygonOffset();
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
      }
   }

   private void preRenderDamagedBlocks() {
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.enableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
      GlStateManager.doPolygonOffset(-3.0F, -3.0F);
      GlStateManager.enablePolygonOffset();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableAlpha();
      GlStateManager.pushMatrix();
   }

   private void postRenderDamagedBlocks() {
      GlStateManager.disableAlpha();
      GlStateManager.doPolygonOffset(0.0F, 0.0F);
      GlStateManager.disablePolygonOffset();
      GlStateManager.enableAlpha();
      GlStateManager.depthMask(true);
      GlStateManager.popMatrix();
   }

   public void drawBlockDamageTexture(Tessellator var1, BufferBuilder var2, Entity var3, float var4) {
      double ☃ = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃x = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      if (!this.damagedBlocks.isEmpty()) {
         this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
         this.preRenderDamagedBlocks();
         ☃.begin(7, DefaultVertexFormats.BLOCK);
         ☃.setTranslation(-☃, -☃x, -☃xx);
         ☃.noColor();
         Iterator<DestroyBlockProgress> ☃xxx = this.damagedBlocks.values().iterator();

         while (☃xxx.hasNext()) {
            DestroyBlockProgress ☃xxxx = ☃xxx.next();
            BlockPos ☃xxxxx = ☃xxxx.getPosition();
            double ☃xxxxxx = ☃xxxxx.getX() - ☃;
            double ☃xxxxxxx = ☃xxxxx.getY() - ☃x;
            double ☃xxxxxxxx = ☃xxxxx.getZ() - ☃xx;
            Block ☃xxxxxxxxx = this.world.getBlockState(☃xxxxx).getBlock();
            if (!(☃xxxxxxxxx instanceof BlockChest)
               && !(☃xxxxxxxxx instanceof BlockEnderChest)
               && !(☃xxxxxxxxx instanceof BlockSign)
               && !(☃xxxxxxxxx instanceof BlockSkull)) {
               if (☃xxxxxx * ☃xxxxxx + ☃xxxxxxx * ☃xxxxxxx + ☃xxxxxxxx * ☃xxxxxxxx > 1024.0) {
                  ☃xxx.remove();
               } else {
                  IBlockState ☃xxxxxxxxxx = this.world.getBlockState(☃xxxxx);
                  if (☃xxxxxxxxxx.getMaterial() != Material.AIR) {
                     int ☃xxxxxxxxxxx = ☃xxxx.getPartialBlockDamage();
                     TextureAtlasSprite ☃xxxxxxxxxxxx = this.destroyBlockIcons[☃xxxxxxxxxxx];
                     BlockRendererDispatcher ☃xxxxxxxxxxxxx = this.mc.getBlockRendererDispatcher();
                     ☃xxxxxxxxxxxxx.renderBlockDamage(☃xxxxxxxxxx, ☃xxxxx, ☃xxxxxxxxxxxx, this.world);
                  }
               }
            }
         }

         ☃.draw();
         ☃.setTranslation(0.0, 0.0, 0.0);
         this.postRenderDamagedBlocks();
      }
   }

   public void drawSelectionBox(EntityPlayer var1, RayTraceResult var2, int var3, float var4) {
      if (☃ == 0 && ☃.typeOfHit == RayTraceResult.Type.BLOCK) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         GlStateManager.glLineWidth(2.0F);
         GlStateManager.disableTexture2D();
         GlStateManager.depthMask(false);
         BlockPos ☃ = ☃.getBlockPos();
         IBlockState ☃x = this.world.getBlockState(☃);
         if (☃x.getMaterial() != Material.AIR && this.world.getWorldBorder().contains(☃)) {
            double ☃xx = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
            double ☃xxx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
            double ☃xxxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
            drawSelectionBoundingBox(☃x.getSelectedBoundingBox(this.world, ☃).grow(0.002F).offset(-☃xx, -☃xxx, -☃xxxx), 0.0F, 0.0F, 0.0F, 0.4F);
         }

         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
      }
   }

   public static void drawSelectionBoundingBox(AxisAlignedBB var0, float var1, float var2, float var3, float var4) {
      drawBoundingBox(☃.minX, ☃.minY, ☃.minZ, ☃.maxX, ☃.maxY, ☃.maxZ, ☃, ☃, ☃, ☃);
   }

   public static void drawBoundingBox(
      double var0, double var2, double var4, double var6, double var8, double var10, float var12, float var13, float var14, float var15
   ) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      ☃x.begin(3, DefaultVertexFormats.POSITION_COLOR);
      drawBoundingBox(☃x, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      ☃.draw();
   }

   public static void drawBoundingBox(
      BufferBuilder var0, double var1, double var3, double var5, double var7, double var9, double var11, float var13, float var14, float var15, float var16
   ) {
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, 0.0F).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, 0.0F).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, 0.0F).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, 0.0F).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, 0.0F).endVertex();
   }

   public static void renderFilledBox(AxisAlignedBB var0, float var1, float var2, float var3, float var4) {
      renderFilledBox(☃.minX, ☃.minY, ☃.minZ, ☃.maxX, ☃.maxY, ☃.maxZ, ☃, ☃, ☃, ☃);
   }

   public static void renderFilledBox(
      double var0, double var2, double var4, double var6, double var8, double var10, float var12, float var13, float var14, float var15
   ) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      ☃x.begin(5, DefaultVertexFormats.POSITION_COLOR);
      addChainedFilledBoxVertices(☃x, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      ☃.draw();
   }

   public static void addChainedFilledBoxVertices(
      BufferBuilder var0, double var1, double var3, double var5, double var7, double var9, double var11, float var13, float var14, float var15, float var16
   ) {
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, ☃).color(☃, ☃, ☃, ☃).endVertex();
   }

   private void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      this.viewFrustum.markBlocksForUpdate(☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void notifyBlockUpdate(World var1, BlockPos var2, IBlockState var3, IBlockState var4, int var5) {
      int ☃ = ☃.getX();
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ();
      this.markBlocksForUpdate(☃ - 1, ☃x - 1, ☃xx - 1, ☃ + 1, ☃x + 1, ☃xx + 1, (☃ & 8) != 0);
   }

   @Override
   public void notifyLightSet(BlockPos var1) {
      this.setLightUpdates.add(☃.toImmutable());
   }

   @Override
   public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.markBlocksForUpdate(☃ - 1, ☃ - 1, ☃ - 1, ☃ + 1, ☃ + 1, ☃ + 1, false);
   }

   @Override
   public void playRecord(@Nullable SoundEvent var1, BlockPos var2) {
      ISound ☃ = this.mapSoundPositions.get(☃);
      if (☃ != null) {
         this.mc.getSoundHandler().stopSound(☃);
         this.mapSoundPositions.remove(☃);
      }

      if (☃ != null) {
         ItemRecord ☃x = ItemRecord.getBySound(☃);
         if (☃x != null) {
            this.mc.ingameGUI.setRecordPlayingMessage(☃x.getRecordNameLocal());
         }

         ISound var5 = PositionedSoundRecord.getRecordSoundRecord(☃, ☃.getX(), ☃.getY(), ☃.getZ());
         this.mapSoundPositions.put(☃, var5);
         this.mc.getSoundHandler().playSound(var5);
      }

      this.setPartying(this.world, ☃, ☃ != null);
   }

   private void setPartying(World var1, BlockPos var2, boolean var3) {
      for (EntityLivingBase ☃ : ☃.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(☃).grow(3.0))) {
         ☃.setPartying(☃, ☃);
      }
   }

   @Override
   public void playSoundToAllNearExcept(
      @Nullable EntityPlayer var1, SoundEvent var2, SoundCategory var3, double var4, double var6, double var8, float var10, float var11
   ) {
   }

   @Override
   public void spawnParticle(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      this.spawnParticle(☃, ☃, false, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void spawnParticle(
      int var1, boolean var2, boolean var3, final double var4, final double var6, final double var8, double var10, double var12, double var14, int... var16
   ) {
      try {
         this.spawnParticle0(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } catch (Throwable var20) {
         CrashReport ☃ = CrashReport.makeCrashReport(var20, "Exception while adding particle");
         CrashReportCategory ☃x = ☃.makeCategory("Particle being added");
         ☃x.addCrashSection("ID", ☃);
         if (☃ != null) {
            ☃x.addCrashSection("Parameters", ☃);
         }

         ☃x.addDetail("Position", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return CrashReportCategory.getCoordinateInfo(☃, ☃, ☃);
            }
         });
         throw new ReportedException(☃);
      }
   }

   private void spawnParticle(EnumParticleTypes var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      this.spawnParticle(☃.getParticleID(), ☃.getShouldIgnoreRange(), ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Nullable
   private Particle spawnParticle0(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      return this.spawnParticle0(☃, ☃, false, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Nullable
   private Particle spawnParticle0(
      int var1, boolean var2, boolean var3, double var4, double var6, double var8, double var10, double var12, double var14, int... var16
   ) {
      Entity ☃ = this.mc.getRenderViewEntity();
      if (this.mc != null && ☃ != null && this.mc.effectRenderer != null) {
         int ☃x = this.calculateParticleLevel(☃);
         double ☃xx = ☃.posX - ☃;
         double ☃xxx = ☃.posY - ☃;
         double ☃xxxx = ☃.posZ - ☃;
         if (☃) {
            return this.mc.effectRenderer.spawnEffectParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
         } else if (☃xx * ☃xx + ☃xxx * ☃xxx + ☃xxxx * ☃xxxx > 1024.0) {
            return null;
         } else {
            return ☃x > 1 ? null : this.mc.effectRenderer.spawnEffectParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
         }
      } else {
         return null;
      }
   }

   private int calculateParticleLevel(boolean var1) {
      int ☃ = this.mc.gameSettings.particleSetting;
      if (☃ && ☃ == 2 && this.world.rand.nextInt(10) == 0) {
         ☃ = 1;
      }

      if (☃ == 1 && this.world.rand.nextInt(3) == 0) {
         ☃ = 2;
      }

      return ☃;
   }

   @Override
   public void onEntityAdded(Entity var1) {
   }

   @Override
   public void onEntityRemoved(Entity var1) {
   }

   public void deleteAllDisplayLists() {
   }

   @Override
   public void broadcastSound(int var1, BlockPos var2, int var3) {
      switch (☃) {
         case 1023:
         case 1028:
         case 1038:
            Entity ☃ = this.mc.getRenderViewEntity();
            if (☃ != null) {
               double ☃x = ☃.getX() - ☃.posX;
               double ☃xx = ☃.getY() - ☃.posY;
               double ☃xxx = ☃.getZ() - ☃.posZ;
               double ☃xxxx = Math.sqrt(☃x * ☃x + ☃xx * ☃xx + ☃xxx * ☃xxx);
               double ☃xxxxx = ☃.posX;
               double ☃xxxxxx = ☃.posY;
               double ☃xxxxxxx = ☃.posZ;
               if (☃xxxx > 0.0) {
                  ☃xxxxx += ☃x / ☃xxxx * 2.0;
                  ☃xxxxxx += ☃xx / ☃xxxx * 2.0;
                  ☃xxxxxxx += ☃xxx / ☃xxxx * 2.0;
               }

               if (☃ == 1023) {
                  this.world.playSound(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
               } else if (☃ == 1038) {
                  this.world.playSound(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
               } else {
                  this.world.playSound(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, SoundEvents.ENTITY_ENDERDRAGON_DEATH, SoundCategory.HOSTILE, 5.0F, 1.0F, false);
               }
            }
      }
   }

   @Override
   public void playEvent(EntityPlayer var1, int var2, BlockPos var3, int var4) {
      Random ☃ = this.world.rand;
      switch (☃) {
         case 1000:
            this.world.playSound(☃, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            break;
         case 1001:
            this.world.playSound(☃, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0F, 1.2F, false);
            break;
         case 1002:
            this.world.playSound(☃, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.2F, false);
            break;
         case 1003:
            this.world.playSound(☃, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 1.0F, 1.2F, false);
            break;
         case 1004:
            this.world.playSound(☃, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.2F, false);
            break;
         case 1005:
            this.world.playSound(☃, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1006:
            this.world.playSound(☃, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1007:
            this.world.playSound(☃, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1008:
            this.world.playSound(☃, SoundEvents.BLOCK_FENCE_GATE_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1009:
            this.world.playSound(☃, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (☃.nextFloat() - ☃.nextFloat()) * 0.8F, false);
            break;
         case 1010:
            if (Item.getItemById(☃) instanceof ItemRecord) {
               this.world.playRecord(☃, ((ItemRecord)Item.getItemById(☃)).getSound());
            } else {
               this.world.playRecord(☃, null);
            }
            break;
         case 1011:
            this.world.playSound(☃, SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1012:
            this.world.playSound(☃, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1013:
            this.world.playSound(☃, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1014:
            this.world.playSound(☃, SoundEvents.BLOCK_FENCE_GATE_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1015:
            this.world.playSound(☃, SoundEvents.ENTITY_GHAST_WARN, SoundCategory.HOSTILE, 10.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1016:
            this.world.playSound(☃, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 10.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1017:
            this.world.playSound(☃, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.HOSTILE, 10.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1018:
            this.world.playSound(☃, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1019:
            this.world
               .playSound(☃, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.HOSTILE, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1020:
            this.world
               .playSound(☃, SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.HOSTILE, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1021:
            this.world
               .playSound(☃, SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.HOSTILE, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1022:
            this.world.playSound(☃, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1024:
            this.world.playSound(☃, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1025:
            this.world.playSound(☃, SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 0.05F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1026:
            this.world.playSound(☃, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1027:
            this.world
               .playSound(☃, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 2.0F, (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F, false);
            break;
         case 1029:
            this.world.playSound(☃, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1030:
            this.world.playSound(☃, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1031:
            this.world.playSound(☃, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1032:
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRAVEL, ☃.nextFloat() * 0.4F + 0.8F));
            break;
         case 1033:
            this.world.playSound(☃, SoundEvents.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            break;
         case 1034:
            this.world.playSound(☃, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            break;
         case 1035:
            this.world.playSound(☃, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            break;
         case 1036:
            this.world.playSound(☃, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 1037:
            this.world.playSound(☃, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 2000:
            int ☃xx = ☃ % 3 - 1;
            int ☃xxx = ☃ / 3 % 3 - 1;
            double ☃xxxx = ☃.getX() + ☃xx * 0.6 + 0.5;
            double ☃xxxxx = ☃.getY() + 0.5;
            double ☃xxxxxx = ☃.getZ() + ☃xxx * 0.6 + 0.5;

            for (int ☃xxxxxxx = 0; ☃xxxxxxx < 10; ☃xxxxxxx++) {
               double ☃xxxxxxxx = ☃.nextDouble() * 0.2 + 0.01;
               double ☃xxxxxxxxxx = ☃xxxx + ☃xx * 0.01 + (☃.nextDouble() - 0.5) * ☃xxx * 0.5;
               double ☃xxxxxxxxxxx = ☃xxxxx + (☃.nextDouble() - 0.5) * 0.5;
               double ☃xxxxxxxxxxxx = ☃xxxxxx + ☃xxx * 0.01 + (☃.nextDouble() - 0.5) * ☃xx * 0.5;
               double ☃xxxxxxxxxxxxx = ☃xx * ☃xxxxxxxx + ☃.nextGaussian() * 0.01;
               double ☃xxxxxxxxxxxxxx = -0.03 + ☃.nextGaussian() * 0.01;
               double ☃xxxxxxxxxxxxxxx = ☃xxx * ☃xxxxxxxx + ☃.nextGaussian() * 0.01;
               this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx);
            }
            break;
         case 2001:
            Block ☃x = Block.getBlockById(☃ & 4095);
            if (☃x.getDefaultState().getMaterial() != Material.AIR) {
               SoundType ☃xx = ☃x.getSoundType();
               this.world.playSound(☃, ☃xx.getBreakSound(), SoundCategory.BLOCKS, (☃xx.getVolume() + 1.0F) / 2.0F, ☃xx.getPitch() * 0.8F, false);
            }

            this.mc.effectRenderer.addBlockDestroyEffects(☃, ☃x.getStateFromMeta(☃ >> 12 & 0xFF));
            break;
         case 2002:
         case 2007:
            double ☃xx = ☃.getX();
            double ☃xxx = ☃.getY();
            double ☃xxxx = ☃.getZ();

            for (int ☃xxxxx = 0; ☃xxxxx < 8; ☃xxxxx++) {
               this.spawnParticle(
                  EnumParticleTypes.ITEM_CRACK,
                  ☃xx,
                  ☃xxx,
                  ☃xxxx,
                  ☃.nextGaussian() * 0.15,
                  ☃.nextDouble() * 0.2,
                  ☃.nextGaussian() * 0.15,
                  Item.getIdFromItem(Items.SPLASH_POTION)
               );
            }

            float ☃xxxxx = (☃ >> 16 & 0xFF) / 255.0F;
            float ☃xxxxxx = (☃ >> 8 & 0xFF) / 255.0F;
            float ☃xxxxxxx = (☃ >> 0 & 0xFF) / 255.0F;
            EnumParticleTypes ☃xxxxxxxx = ☃ == 2007 ? EnumParticleTypes.SPELL_INSTANT : EnumParticleTypes.SPELL;

            for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 100; ☃xxxxxxxxx++) {
               double ☃xxxxxxxxxx = ☃.nextDouble() * 4.0;
               double ☃xxxxxxxxxxx = ☃.nextDouble() * Math.PI * 2.0;
               double ☃xxxxxxxxxxxx = Math.cos(☃xxxxxxxxxxx) * ☃xxxxxxxxxx;
               double ☃xxxxxxxxxxxxx = 0.01 + ☃.nextDouble() * 0.5;
               double ☃xxxxxxxxxxxxxx = Math.sin(☃xxxxxxxxxxx) * ☃xxxxxxxxxx;
               Particle ☃xxxxxxxxxxxxxxx = this.spawnParticle0(
                  ☃xxxxxxxx.getParticleID(),
                  ☃xxxxxxxx.getShouldIgnoreRange(),
                  ☃xx + ☃xxxxxxxxxxxx * 0.1,
                  ☃xxx + 0.3,
                  ☃xxxx + ☃xxxxxxxxxxxxxx * 0.1,
                  ☃xxxxxxxxxxxx,
                  ☃xxxxxxxxxxxxx,
                  ☃xxxxxxxxxxxxxx
               );
               if (☃xxxxxxxxxxxxxxx != null) {
                  float ☃xxxxxxxxxxxxxxxx = 0.75F + ☃.nextFloat() * 0.25F;
                  ☃xxxxxxxxxxxxxxx.setRBGColorF(☃xxxxx * ☃xxxxxxxxxxxxxxxx, ☃xxxxxx * ☃xxxxxxxxxxxxxxxx, ☃xxxxxxx * ☃xxxxxxxxxxxxxxxx);
                  ☃xxxxxxxxxxxxxxx.multiplyVelocity((float)☃xxxxxxxxxx);
               }
            }

            this.world.playSound(☃, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.NEUTRAL, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 2003:
            double ☃xx = ☃.getX() + 0.5;
            double ☃xxx = ☃.getY();
            double ☃xxxx = ☃.getZ() + 0.5;

            for (int ☃xxxxx = 0; ☃xxxxx < 8; ☃xxxxx++) {
               this.spawnParticle(
                  EnumParticleTypes.ITEM_CRACK,
                  ☃xx,
                  ☃xxx,
                  ☃xxxx,
                  ☃.nextGaussian() * 0.15,
                  ☃.nextDouble() * 0.2,
                  ☃.nextGaussian() * 0.15,
                  Item.getIdFromItem(Items.ENDER_EYE)
               );
            }

            for (double ☃xxxxx = 0.0; ☃xxxxx < Math.PI * 2; ☃xxxxx += Math.PI / 20) {
               this.spawnParticle(
                  EnumParticleTypes.PORTAL,
                  ☃xx + Math.cos(☃xxxxx) * 5.0,
                  ☃xxx - 0.4,
                  ☃xxxx + Math.sin(☃xxxxx) * 5.0,
                  Math.cos(☃xxxxx) * -5.0,
                  0.0,
                  Math.sin(☃xxxxx) * -5.0
               );
               this.spawnParticle(
                  EnumParticleTypes.PORTAL,
                  ☃xx + Math.cos(☃xxxxx) * 5.0,
                  ☃xxx - 0.4,
                  ☃xxxx + Math.sin(☃xxxxx) * 5.0,
                  Math.cos(☃xxxxx) * -7.0,
                  0.0,
                  Math.sin(☃xxxxx) * -7.0
               );
            }
            break;
         case 2004:
            for (int ☃xx = 0; ☃xx < 20; ☃xx++) {
               double ☃xxx = ☃.getX() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
               double ☃xxxx = ☃.getY() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
               double ☃xxxxx = ☃.getZ() + 0.5 + (this.world.rand.nextFloat() - 0.5) * 2.0;
               this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃xxx, ☃xxxx, ☃xxxxx, 0.0, 0.0, 0.0, new int[0]);
               this.world.spawnParticle(EnumParticleTypes.FLAME, ☃xxx, ☃xxxx, ☃xxxxx, 0.0, 0.0, 0.0, new int[0]);
            }
            break;
         case 2005:
            ItemDye.spawnBonemealParticles(this.world, ☃, ☃);
            break;
         case 2006:
            for (int ☃x = 0; ☃x < 200; ☃x++) {
               float ☃xx = ☃.nextFloat() * 4.0F;
               float ☃xxx = ☃.nextFloat() * (float) (Math.PI * 2);
               double ☃xxxx = MathHelper.cos(☃xxx) * ☃xx;
               double ☃xxxxx = 0.01 + ☃.nextDouble() * 0.5;
               double ☃xxxxxx = MathHelper.sin(☃xxx) * ☃xx;
               Particle ☃xxxxxxx = this.spawnParticle0(
                  EnumParticleTypes.DRAGON_BREATH.getParticleID(),
                  false,
                  ☃.getX() + ☃xxxx * 0.1,
                  ☃.getY() + 0.3,
                  ☃.getZ() + ☃xxxxxx * 0.1,
                  ☃xxxx,
                  ☃xxxxx,
                  ☃xxxxxx
               );
               if (☃xxxxxxx != null) {
                  ☃xxxxxxx.multiplyVelocity(☃xx);
               }
            }

            this.world
               .playSound(☃, SoundEvents.ENTITY_ENDERDRAGON_FIREBALL_EPLD, SoundCategory.HOSTILE, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
            break;
         case 3000:
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, ☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5, 0.0, 0.0, 0.0, new int[0]);
            this.world
               .playSound(
                  ☃,
                  SoundEvents.BLOCK_END_GATEWAY_SPAWN,
                  SoundCategory.BLOCKS,
                  10.0F,
                  (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F,
                  false
               );
            break;
         case 3001:
            this.world.playSound(☃, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.HOSTILE, 64.0F, 0.8F + this.world.rand.nextFloat() * 0.3F, false);
      }
   }

   @Override
   public void sendBlockBreakProgress(int var1, BlockPos var2, int var3) {
      if (☃ >= 0 && ☃ < 10) {
         DestroyBlockProgress ☃ = this.damagedBlocks.get(☃);
         if (☃ == null || ☃.getPosition().getX() != ☃.getX() || ☃.getPosition().getY() != ☃.getY() || ☃.getPosition().getZ() != ☃.getZ()) {
            ☃ = new DestroyBlockProgress(☃, ☃);
            this.damagedBlocks.put(☃, ☃);
         }

         ☃.setPartialBlockDamage(☃);
         ☃.setCloudUpdateTick(this.cloudTickCounter);
      } else {
         this.damagedBlocks.remove(☃);
      }
   }

   public boolean hasNoChunkUpdates() {
      return this.chunksToUpdate.isEmpty() && this.renderDispatcher.hasNoChunkUpdates();
   }

   public void setDisplayListEntitiesDirty() {
      this.displayListEntitiesDirty = true;
   }

   public void updateTileEntities(Collection<TileEntity> var1, Collection<TileEntity> var2) {
      synchronized (this.setTileEntities) {
         this.setTileEntities.removeAll(☃);
         this.setTileEntities.addAll(☃);
      }
   }

   class ContainerLocalRenderInformation {
      final RenderChunk renderChunk;
      final EnumFacing facing;
      byte setFacing;
      final int counter;

      private ContainerLocalRenderInformation(RenderChunk var2, EnumFacing var3, @Nullable int var4) {
         this.renderChunk = ☃;
         this.facing = ☃;
         this.counter = ☃;
      }

      public void setDirection(byte var1, EnumFacing var2) {
         this.setFacing = (byte)(this.setFacing | ☃ | 1 << ☃.ordinal());
      }

      public boolean hasDirection(EnumFacing var1) {
         return (this.setFacing & 1 << ☃.ordinal()) > 0;
      }
   }
}
