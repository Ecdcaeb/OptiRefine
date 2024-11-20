/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Queues
 *  com.google.common.collect.Sets
 *  com.google.gson.JsonSyntaxException
 *  java.io.IOException
 *  java.lang.Double
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayDeque
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  java.util.Set
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockChest
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockSign
 *  net.minecraft.block.BlockSkull
 *  net.minecraft.block.SoundType
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.renderer.BlockRendererDispatcher
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.ChunkRenderContainer
 *  net.minecraft.client.renderer.DestroyBlockProgress
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Matrix4f
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderGlobal$2
 *  net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderList
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.VboRenderList
 *  net.minecraft.client.renderer.Vector3d
 *  net.minecraft.client.renderer.ViewFrustum
 *  net.minecraft.client.renderer.chunk.ChunkRenderDispatcher
 *  net.minecraft.client.renderer.chunk.CompiledChunk
 *  net.minecraft.client.renderer.chunk.IRenderChunkFactory
 *  net.minecraft.client.renderer.chunk.ListChunkFactory
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.client.renderer.chunk.VboChunkFactory
 *  net.minecraft.client.renderer.chunk.VisGraph
 *  net.minecraft.client.renderer.culling.ClippingHelper
 *  net.minecraft.client.renderer.culling.ClippingHelperImpl
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.client.renderer.vertex.VertexFormatElement
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumType
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumUsage
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.client.shader.ShaderLinkHelper
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemDye
 *  net.minecraft.item.ItemRecord
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.ClassInheritanceMultiMap
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.BlockPos$PooledMutableBlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.IWorldEventListener
 *  net.minecraft.world.World
 *  net.minecraft.world.border.WorldBorder
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraftforge.client.IRenderHandler
 *  net.minecraftforge.client.MinecraftForgeClient
 *  net.minecraftforge.common.ForgeModContainer
 *  net.minecraftforge.fml.client.FMLClientHandler
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector3f
 *  org.lwjgl.util.vector.Vector4f
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.ViewFrustum;
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
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

@SideOnly(value=Side.CLIENT)
public class RenderGlobal
implements IWorldEventListener,
IResourceManagerReloadListener {
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
    private List<ContainerLocalRenderInformation> renderInfos = Lists.newArrayListWithCapacity((int)69696);
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

    public RenderGlobal(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.renderEngine = mcIn.getTextureManager();
        this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
        GlStateManager.glTexParameteri((int)3553, (int)10242, (int)10497);
        GlStateManager.glTexParameteri((int)3553, (int)10243, (int)10497);
        GlStateManager.bindTexture((int)0);
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

    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.updateDestroyBlockIcons();
    }

    private void updateDestroyBlockIcons() {
        TextureMap texturemap = this.mc.getTextureMapBlocks();
        for (int i = 0; i < this.destroyBlockIcons.length; ++i) {
            this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
        }
    }

    public void makeEntityOutlineShader() {
        if (OpenGlHelper.shadersSupported) {
            if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
                ShaderLinkHelper.setNewStaticShaderLinkHelper();
            }
            ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
            try {
                this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
                this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            }
            catch (IOException ioexception) {
                LOGGER.warn("Failed to load shader: {}", (Object)resourcelocation, (Object)ioexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
            catch (JsonSyntaxException jsonsyntaxexception) {
                LOGGER.warn("Failed to load shader: {}", (Object)resourcelocation, (Object)jsonsyntaxexception);
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
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ZERO, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
            this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }

    protected boolean isRenderEntityOutlines() {
        return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.player != null;
    }

    private void generateSky2() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.sky2VBO != null) {
            this.sky2VBO.deleteGlBuffers();
        }
        if (this.glSkyList2 >= 0) {
            GLAllocation.deleteDisplayLists((int)this.glSkyList2);
            this.glSkyList2 = -1;
        }
        if (this.vboEnabled) {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(bufferbuilder, -16.0f, true);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.sky2VBO.bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.glSkyList2 = GLAllocation.generateDisplayLists((int)1);
            GlStateManager.glNewList((int)this.glSkyList2, (int)4864);
            this.renderSky(bufferbuilder, -16.0f, true);
            tessellator.draw();
            GlStateManager.glEndList();
        }
    }

    private void generateSky() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.skyVBO != null) {
            this.skyVBO.deleteGlBuffers();
        }
        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists((int)this.glSkyList);
            this.glSkyList = -1;
        }
        if (this.vboEnabled) {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(bufferbuilder, 16.0f, false);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.skyVBO.bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.glSkyList = GLAllocation.generateDisplayLists((int)1);
            GlStateManager.glNewList((int)this.glSkyList, (int)4864);
            this.renderSky(bufferbuilder, 16.0f, false);
            tessellator.draw();
            GlStateManager.glEndList();
        }
    }

    private void renderSky(BufferBuilder bufferBuilderIn, float posY, boolean reverseX) {
        int i = 64;
        int j = 6;
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);
        for (int k = -384; k <= 384; k += 64) {
            for (int l = -384; l <= 384; l += 64) {
                float f = k;
                float f1 = k + 64;
                if (reverseX) {
                    f1 = k;
                    f = k + 64;
                }
                bufferBuilderIn.pos((double)f, (double)posY, (double)l).endVertex();
                bufferBuilderIn.pos((double)f1, (double)posY, (double)l).endVertex();
                bufferBuilderIn.pos((double)f1, (double)posY, (double)(l + 64)).endVertex();
                bufferBuilderIn.pos((double)f, (double)posY, (double)(l + 64)).endVertex();
            }
        }
    }

    private void generateStars() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.starVBO != null) {
            this.starVBO.deleteGlBuffers();
        }
        if (this.starGLCallList >= 0) {
            GLAllocation.deleteDisplayLists((int)this.starGLCallList);
            this.starGLCallList = -1;
        }
        if (this.vboEnabled) {
            this.starVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderStars(bufferbuilder);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.starVBO.bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.starGLCallList = GLAllocation.generateDisplayLists((int)1);
            GlStateManager.pushMatrix();
            GlStateManager.glNewList((int)this.starGLCallList, (int)4864);
            this.renderStars(bufferbuilder);
            tessellator.draw();
            GlStateManager.glEndList();
            GlStateManager.popMatrix();
        }
    }

    private void renderStars(BufferBuilder bufferBuilderIn) {
        Random random = new Random(10842L);
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);
        for (int i = 0; i < 1500; ++i) {
            double d0 = random.nextFloat() * 2.0f - 1.0f;
            double d1 = random.nextFloat() * 2.0f - 1.0f;
            double d2 = random.nextFloat() * 2.0f - 1.0f;
            double d3 = 0.15f + random.nextFloat() * 0.1f;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (!(d4 < 1.0) || !(d4 > 0.01)) continue;
            d4 = 1.0 / Math.sqrt((double)d4);
            double d5 = (d0 *= d4) * 100.0;
            double d6 = (d1 *= d4) * 100.0;
            double d7 = (d2 *= d4) * 100.0;
            double d8 = Math.atan2((double)d0, (double)d2);
            double d9 = Math.sin((double)d8);
            double d10 = Math.cos((double)d8);
            double d11 = Math.atan2((double)Math.sqrt((double)(d0 * d0 + d2 * d2)), (double)d1);
            double d12 = Math.sin((double)d11);
            double d13 = Math.cos((double)d11);
            double d14 = random.nextDouble() * Math.PI * 2.0;
            double d15 = Math.sin((double)d14);
            double d16 = Math.cos((double)d14);
            for (int j = 0; j < 4; ++j) {
                double d17 = 0.0;
                double d18 = (double)((j & 2) - 1) * d3;
                double d19 = (double)((j + 1 & 2) - 1) * d3;
                double d20 = 0.0;
                double d21 = d18 * d16 - d19 * d15;
                double d22 = d19 * d16 + d18 * d15;
                double d23 = d21 * d12 + 0.0 * d13;
                double d24 = 0.0 * d12 - d21 * d13;
                double d25 = d24 * d9 - d22 * d10;
                double d26 = d22 * d9 + d24 * d10;
                bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
            }
        }
    }

    public void setWorldAndLoadRenderers(@Nullable WorldClient worldClientIn) {
        if (this.world != null) {
            this.world.removeEventListener((IWorldEventListener)this);
        }
        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.renderManager.setWorld((World)worldClientIn);
        this.world = worldClientIn;
        if (worldClientIn != null) {
            worldClientIn.addEventListener((IWorldEventListener)this);
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadRenderers() {
        if (this.world != null) {
            Entity entity;
            if (this.renderDispatcher == null) {
                this.renderDispatcher = new ChunkRenderDispatcher();
            }
            this.displayListEntitiesDirty = true;
            Blocks.LEAVES.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            Blocks.LEAVES2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            boolean flag = this.vboEnabled;
            this.vboEnabled = OpenGlHelper.useVbo();
            if (flag && !this.vboEnabled) {
                this.renderContainer = new RenderList();
                this.renderChunkFactory = new ListChunkFactory();
            } else if (!flag && this.vboEnabled) {
                this.renderContainer = new VboRenderList();
                this.renderChunkFactory = new VboChunkFactory();
            }
            if (flag != this.vboEnabled) {
                this.generateStars();
                this.generateSky();
                this.generateSky2();
            }
            if (this.viewFrustum != null) {
                this.viewFrustum.deleteGlResources();
            }
            this.stopChunkUpdates();
            Set<TileEntity> set = this.setTileEntities;
            synchronized (set) {
                this.setTileEntities.clear();
            }
            this.viewFrustum = new ViewFrustum((World)this.world, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
            if (this.world != null && (entity = this.mc.getRenderViewEntity()) != null) {
                this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
            }
            this.renderEntitiesStartupCounter = 2;
        }
    }

    protected void stopChunkUpdates() {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }

    public void createBindEntityOutlineFbs(int width, int height) {
        if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null) {
            this.entityOutlineShader.createBindFramebuffers(width, height);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void renderEntities(Entity renderViewEntity, ICamera camera, float partialTicks) {
        int pass = MinecraftForgeClient.getRenderPass();
        if (this.renderEntitiesStartupCounter > 0) {
            if (pass > 0) {
                return;
            }
            --this.renderEntitiesStartupCounter;
        } else {
            double d0 = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * (double)partialTicks;
            double d1 = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * (double)partialTicks;
            double d2 = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * (double)partialTicks;
            this.world.profiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.prepare((World)this.world, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.objectMouseOver, partialTicks);
            this.renderManager.cacheActiveRenderInfo((World)this.world, this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
            if (pass == 0) {
                this.countEntitiesTotal = 0;
                this.countEntitiesRendered = 0;
                this.countEntitiesHidden = 0;
            }
            Entity entity = this.mc.getRenderViewEntity();
            double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            TileEntityRendererDispatcher.staticPlayerX = d3;
            TileEntityRendererDispatcher.staticPlayerY = d4;
            TileEntityRendererDispatcher.staticPlayerZ = d5;
            this.renderManager.setRenderPosition(d3, d4, d5);
            this.mc.entityRenderer.enableLightmap();
            this.world.profiler.endStartSection("global");
            List list = this.world.getLoadedEntityList();
            if (pass == 0) {
                this.countEntitiesTotal = list.size();
            }
            for (int i = 0; i < this.world.weatherEffects.size(); ++i) {
                Entity entity1 = (Entity)this.world.weatherEffects.get(i);
                if (!entity1.shouldRenderInPass(pass)) continue;
                ++this.countEntitiesRendered;
                if (!entity1.isInRangeToRender3d(d0, d1, d2)) continue;
                this.renderManager.renderEntityStatic(entity1, partialTicks, false);
            }
            this.world.profiler.endStartSection("entities");
            ArrayList list1 = Lists.newArrayList();
            ArrayList list2 = Lists.newArrayList();
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
            for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
                Chunk chunk = this.world.getChunk(renderglobal$containerlocalrenderinformation.renderChunk.getPosition());
                ClassInheritanceMultiMap classinheritancemultimap = chunk.getEntityLists()[renderglobal$containerlocalrenderinformation.renderChunk.getPosition().q() / 16];
                if (classinheritancemultimap.isEmpty()) continue;
                for (Entity entity2 : classinheritancemultimap) {
                    boolean flag1;
                    boolean flag;
                    if (!entity2.shouldRenderInPass(pass) || !(flag = this.renderManager.shouldRender(entity2, camera, d0, d1, d2) || entity2.isRidingOrBeingRiddenBy((Entity)this.mc.player))) continue;
                    boolean bl = flag1 = this.mc.getRenderViewEntity() instanceof EntityLivingBase ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() : false;
                    if (entity2 == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0 && !flag1 || !(entity2.posY < 0.0) && !(entity2.posY >= 256.0) && !this.world.isBlockLoaded((BlockPos)blockpos$pooledmutableblockpos.setPos(entity2))) continue;
                    ++this.countEntitiesRendered;
                    this.renderManager.renderEntityStatic(entity2, partialTicks, false);
                    if (this.isOutlineActive(entity2, entity, camera)) {
                        list1.add((Object)entity2);
                    }
                    if (!this.renderManager.isRenderMultipass(entity2)) continue;
                    list2.add((Object)entity2);
                }
            }
            blockpos$pooledmutableblockpos.release();
            if (!list2.isEmpty()) {
                for (Entity entity3 : list2) {
                    this.renderManager.renderMultipass(entity3, partialTicks);
                }
            }
            if (pass == 0 && this.isRenderEntityOutlines() && (!list1.isEmpty() || this.entityOutlinesRendered)) {
                this.world.profiler.endStartSection("entityOutlines");
                this.entityOutlineFramebuffer.framebufferClear();
                boolean bl = this.entityOutlinesRendered = !list1.isEmpty();
                if (!list1.isEmpty()) {
                    GlStateManager.depthFunc((int)519);
                    GlStateManager.disableFog();
                    this.entityOutlineFramebuffer.bindFramebuffer(false);
                    RenderHelper.disableStandardItemLighting();
                    this.renderManager.setRenderOutlines(true);
                    for (int j = 0; j < list1.size(); ++j) {
                        this.renderManager.renderEntityStatic((Entity)list1.get(j), partialTicks, false);
                    }
                    this.renderManager.setRenderOutlines(false);
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.depthMask((boolean)false);
                    this.entityOutlineShader.render(partialTicks);
                    GlStateManager.enableLighting();
                    GlStateManager.depthMask((boolean)true);
                    GlStateManager.enableFog();
                    GlStateManager.enableBlend();
                    GlStateManager.enableColorMaterial();
                    GlStateManager.depthFunc((int)515);
                    GlStateManager.enableDepth();
                    GlStateManager.enableAlpha();
                }
                this.mc.getFramebuffer().bindFramebuffer(false);
            }
            this.world.profiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();
            TileEntityRendererDispatcher.instance.preDrawBatch();
            for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 : this.renderInfos) {
                List list3 = renderglobal$containerlocalrenderinformation1.renderChunk.getCompiledChunk().getTileEntities();
                if (list3.isEmpty()) continue;
                for (TileEntity tileentity2 : list3) {
                    if (!tileentity2.shouldRenderInPass(pass) || !camera.isBoundingBoxInFrustum(tileentity2.getRenderBoundingBox())) continue;
                    TileEntityRendererDispatcher.instance.render(tileentity2, partialTicks, -1);
                }
            }
            Iterator iterator = this.setTileEntities;
            synchronized (iterator) {
                for (TileEntity tileentity : this.setTileEntities) {
                    if (!tileentity.shouldRenderInPass(pass) || !camera.isBoundingBoxInFrustum(tileentity.getRenderBoundingBox())) continue;
                    TileEntityRendererDispatcher.instance.render(tileentity, partialTicks, -1);
                }
            }
            TileEntityRendererDispatcher.instance.drawBatch(pass);
            this.preRenderDamagedBlocks();
            for (DestroyBlockProgress destroyblockprogress : this.damagedBlocks.values()) {
                BlockPos blockpos = destroyblockprogress.getPosition();
                if (!this.world.getBlockState(blockpos).getBlock().hasTileEntity()) continue;
                TileEntity tileentity1 = this.world.getTileEntity(blockpos);
                if (tileentity1 instanceof TileEntityChest) {
                    TileEntityChest tileentitychest = (TileEntityChest)tileentity1;
                    if (tileentitychest.adjacentChestXNeg != null) {
                        blockpos = blockpos.offset(EnumFacing.WEST);
                        tileentity1 = this.world.getTileEntity(blockpos);
                    } else if (tileentitychest.adjacentChestZNeg != null) {
                        blockpos = blockpos.offset(EnumFacing.NORTH);
                        tileentity1 = this.world.getTileEntity(blockpos);
                    }
                }
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                if (tileentity1 == null || !iblockstate.h()) continue;
                TileEntityRendererDispatcher.instance.render(tileentity1, partialTicks, destroyblockprogress.getPartialBlockDamage());
            }
            this.postRenderDamagedBlocks();
            this.mc.entityRenderer.disableLightmap();
            this.mc.profiler.endSection();
        }
    }

    private boolean isOutlineActive(Entity entityIn, Entity viewer, ICamera camera) {
        boolean flag;
        boolean bl = flag = viewer instanceof EntityLivingBase && ((EntityLivingBase)viewer).isPlayerSleeping();
        if (entityIn == viewer && this.mc.gameSettings.thirdPersonView == 0 && !flag) {
            return false;
        }
        if (entityIn.isGlowing()) {
            return true;
        }
        if (this.mc.player.y() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown() && entityIn instanceof EntityPlayer) {
            return entityIn.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entityIn.getEntityBoundingBox()) || entityIn.isRidingOrBeingRiddenBy((Entity)this.mc.player);
        }
        return false;
    }

    public String getDebugInfoRenders() {
        int i = this.viewFrustum.renderChunks.length;
        int j = this.getRenderedChunks();
        return String.format((String)"C: %d/%d %sD: %d, L: %d, %s", (Object[])new Object[]{j, i, this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.setLightUpdates.size(), this.renderDispatcher == null ? "null" : this.renderDispatcher.getDebugInfo()});
    }

    protected int getRenderedChunks() {
        int i = 0;
        for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
            CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
            if (compiledchunk == CompiledChunk.DUMMY || compiledchunk.isEmpty()) continue;
            ++i;
        }
        return i;
    }

    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden;
    }

    public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
            this.loadRenderers();
        }
        this.world.profiler.startSection("camera");
        double d0 = viewEntity.posX - this.frustumUpdatePosX;
        double d1 = viewEntity.posY - this.frustumUpdatePosY;
        double d2 = viewEntity.posZ - this.frustumUpdatePosZ;
        if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d0 * d0 + d1 * d1 + d2 * d2 > 16.0) {
            this.frustumUpdatePosX = viewEntity.posX;
            this.frustumUpdatePosY = viewEntity.posY;
            this.frustumUpdatePosZ = viewEntity.posZ;
            this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
            this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
            this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
        }
        this.world.profiler.endStartSection("renderlistcamera");
        double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
        this.renderContainer.initialize(d3, d4, d5);
        this.world.profiler.endStartSection("cull");
        if (this.debugFixedClippingHelper != null) {
            Frustum frustum = new Frustum(this.debugFixedClippingHelper);
            frustum.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
            camera = frustum;
        }
        this.mc.profiler.endStartSection("culling");
        BlockPos blockpos1 = new BlockPos(d3, d4 + (double)viewEntity.getEyeHeight(), d5);
        RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos1);
        BlockPos blockpos = new BlockPos(MathHelper.floor((double)(d3 / 16.0)) * 16, MathHelper.floor((double)(d4 / 16.0)) * 16, MathHelper.floor((double)(d5 / 16.0)) * 16);
        this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || (double)viewEntity.rotationPitch != this.lastViewEntityPitch || (double)viewEntity.rotationYaw != this.lastViewEntityYaw;
        this.lastViewEntityX = viewEntity.posX;
        this.lastViewEntityY = viewEntity.posY;
        this.lastViewEntityZ = viewEntity.posZ;
        this.lastViewEntityPitch = viewEntity.rotationPitch;
        this.lastViewEntityYaw = viewEntity.rotationYaw;
        boolean flag = this.debugFixedClippingHelper != null;
        this.mc.profiler.endStartSection("update");
        if (!flag && this.displayListEntitiesDirty) {
            this.displayListEntitiesDirty = false;
            this.renderInfos = Lists.newArrayList();
            ArrayDeque queue = Queues.newArrayDeque();
            Entity.setRenderDistanceWeight((double)MathHelper.clamp((double)((double)this.mc.gameSettings.renderDistanceChunks / 8.0), (double)1.0, (double)2.5));
            boolean flag1 = this.mc.renderChunksMany;
            if (renderchunk != null) {
                boolean flag2 = false;
                ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = new ContainerLocalRenderInformation(this, renderchunk, (EnumFacing)null, 0);
                Set<EnumFacing> set1 = this.getVisibleFacings(blockpos1);
                if (set1.size() == 1) {
                    Vector3f vector3f = this.getViewVector(viewEntity, partialTicks);
                    EnumFacing enumfacing = EnumFacing.getFacingFromVector((float)vector3f.x, (float)vector3f.y, (float)vector3f.z).getOpposite();
                    set1.remove((Object)enumfacing);
                }
                if (set1.isEmpty()) {
                    flag2 = true;
                }
                if (flag2 && !playerSpectator) {
                    this.renderInfos.add((Object)renderglobal$containerlocalrenderinformation3);
                } else {
                    if (playerSpectator && this.world.getBlockState(blockpos1).p()) {
                        flag1 = false;
                    }
                    renderchunk.setFrameIndex(frameCount);
                    queue.add((Object)renderglobal$containerlocalrenderinformation3);
                }
            } else {
                int i = blockpos1.q() > 0 ? 248 : 8;
                for (int j = -this.renderDistanceChunks; j <= this.renderDistanceChunks; ++j) {
                    for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; ++k) {
                        RenderChunk renderchunk1 = this.viewFrustum.getRenderChunk(new BlockPos((j << 4) + 8, i, (k << 4) + 8));
                        if (renderchunk1 == null || !camera.isBoundingBoxInFrustum(renderchunk1.boundingBox.expand(0.0, blockpos1.q() > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, 0.0))) continue;
                        renderchunk1.setFrameIndex(frameCount);
                        queue.add((Object)new ContainerLocalRenderInformation(this, renderchunk1, (EnumFacing)null, 0));
                    }
                }
            }
            this.mc.profiler.startSection("iteration");
            while (!queue.isEmpty()) {
                ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = (ContainerLocalRenderInformation)queue.poll();
                RenderChunk renderchunk3 = renderglobal$containerlocalrenderinformation1.renderChunk;
                EnumFacing enumfacing2 = renderglobal$containerlocalrenderinformation1.facing;
                this.renderInfos.add((Object)renderglobal$containerlocalrenderinformation1);
                for (RenderChunk enumfacing1 : EnumFacing.values()) {
                    RenderChunk renderchunk2 = this.getRenderChunkOffset(blockpos, renderchunk3, (EnumFacing)enumfacing1);
                    if (flag1 && renderglobal$containerlocalrenderinformation1.hasDirection(enumfacing1.getOpposite()) || flag1 && enumfacing2 != null && !renderchunk3.getCompiledChunk().isVisible(enumfacing2.getOpposite(), (EnumFacing)enumfacing1) || renderchunk2 == null || !renderchunk2.setFrameIndex(frameCount) || !camera.isBoundingBoxInFrustum(renderchunk2.boundingBox)) continue;
                    ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = new ContainerLocalRenderInformation(this, renderchunk2, (EnumFacing)enumfacing1, renderglobal$containerlocalrenderinformation1.counter + 1);
                    renderglobal$containerlocalrenderinformation.setDirection(renderglobal$containerlocalrenderinformation1.setFacing, (EnumFacing)enumfacing1);
                    queue.add((Object)renderglobal$containerlocalrenderinformation);
                }
            }
            this.mc.profiler.endSection();
        }
        this.mc.profiler.endStartSection("captureFrustum");
        if (this.debugFixTerrainFrustum) {
            this.fixTerrainFrustum(d3, d4, d5);
            this.debugFixTerrainFrustum = false;
        }
        this.mc.profiler.endStartSection("rebuildNear");
        Set<RenderChunk> set = this.chunksToUpdate;
        this.chunksToUpdate = Sets.newLinkedHashSet();
        for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 : this.renderInfos) {
            boolean flag3;
            RenderChunk renderchunk4 = renderglobal$containerlocalrenderinformation2.renderChunk;
            if (!renderchunk4.needsUpdate() && !set.contains((Object)renderchunk4)) continue;
            this.displayListEntitiesDirty = true;
            BlockPos blockpos2 = renderchunk4.getPosition().add(8, 8, 8);
            boolean bl = flag3 = blockpos2.n((Vec3i)blockpos1) < 768.0;
            if (ForgeModContainer.alwaysSetupTerrainOffThread || !renderchunk4.needsImmediateUpdate() && !flag3) {
                this.chunksToUpdate.add((Object)renderchunk4);
                continue;
            }
            this.mc.profiler.startSection("build near");
            this.renderDispatcher.updateChunkNow(renderchunk4);
            renderchunk4.clearNeedsUpdate();
            this.mc.profiler.endSection();
        }
        this.chunksToUpdate.addAll(set);
        this.mc.profiler.endSection();
    }

    private Set<EnumFacing> getVisibleFacings(BlockPos pos) {
        VisGraph visgraph = new VisGraph();
        BlockPos blockpos = new BlockPos(pos.p() >> 4 << 4, pos.q() >> 4 << 4, pos.r() >> 4 << 4);
        Chunk chunk = this.world.getChunk(blockpos);
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable((BlockPos)blockpos, (BlockPos)blockpos.add(15, 15, 15))) {
            if (!chunk.getBlockState((BlockPos)blockpos$mutableblockpos).p()) continue;
            visgraph.setOpaqueCube((BlockPos)blockpos$mutableblockpos);
        }
        return visgraph.getVisibleFacings(pos);
    }

    @Nullable
    private RenderChunk getRenderChunkOffset(BlockPos playerPos, RenderChunk renderChunkBase, EnumFacing facing) {
        BlockPos blockpos = renderChunkBase.getBlockPosOffset16(facing);
        if (MathHelper.abs((int)(playerPos.p() - blockpos.p())) > this.renderDistanceChunks * 16) {
            return null;
        }
        if (blockpos.q() >= 0 && blockpos.q() < 256) {
            return MathHelper.abs((int)(playerPos.r() - blockpos.r())) > this.renderDistanceChunks * 16 ? null : this.viewFrustum.getRenderChunk(blockpos);
        }
        return null;
    }

    private void fixTerrainFrustum(double x, double y, double z) {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
        Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
        matrix4f.transpose();
        Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
        matrix4f1.transpose();
        Matrix4f matrix4f2 = new Matrix4f();
        Matrix4f.mul((org.lwjgl.util.vector.Matrix4f)matrix4f1, (org.lwjgl.util.vector.Matrix4f)matrix4f, (org.lwjgl.util.vector.Matrix4f)matrix4f2);
        matrix4f2.invert();
        this.debugTerrainFrustumPosition.x = x;
        this.debugTerrainFrustumPosition.y = y;
        this.debugTerrainFrustumPosition.z = z;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[1] = new Vector4f(1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[2] = new Vector4f(1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[5] = new Vector4f(1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[6] = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0f, 1.0f, 1.0f, 1.0f);
        for (int i = 0; i < 8; ++i) {
            Matrix4f.transform((org.lwjgl.util.vector.Matrix4f)matrix4f2, (Vector4f)this.debugTerrainMatrix[i], (Vector4f)this.debugTerrainMatrix[i]);
            this.debugTerrainMatrix[i].x /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].y /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].z /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].w = 1.0f;
        }
    }

    protected Vector3f getViewVector(Entity entityIn, double partialTicks) {
        float f = (float)((double)entityIn.prevRotationPitch + (double)(entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
        float f1 = (float)((double)entityIn.prevRotationYaw + (double)(entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
            f += 180.0f;
        }
        float f2 = MathHelper.cos((float)(-f1 * ((float)Math.PI / 180) - (float)Math.PI));
        float f3 = MathHelper.sin((float)(-f1 * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = -MathHelper.cos((float)(-f * ((float)Math.PI / 180)));
        float f5 = MathHelper.sin((float)(-f * ((float)Math.PI / 180)));
        return new Vector3f(f3 * f4, f5, f2 * f4);
    }

    public int renderBlockLayer(BlockRenderLayer blockLayerIn, double partialTicks, int pass, Entity entityIn) {
        RenderHelper.disableStandardItemLighting();
        if (blockLayerIn == BlockRenderLayer.TRANSLUCENT) {
            this.mc.profiler.startSection("translucent_sort");
            double d0 = entityIn.posX - this.prevRenderSortX;
            double d1 = entityIn.posY - this.prevRenderSortY;
            double d2 = entityIn.posZ - this.prevRenderSortZ;
            if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0) {
                this.prevRenderSortX = entityIn.posX;
                this.prevRenderSortY = entityIn.posY;
                this.prevRenderSortZ = entityIn.posZ;
                int k = 0;
                for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
                    if (!renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) || k++ >= 15) continue;
                    this.renderDispatcher.updateTransparencyLater(renderglobal$containerlocalrenderinformation.renderChunk);
                }
            }
            this.mc.profiler.endSection();
        }
        this.mc.profiler.startSection("filterempty");
        int l = 0;
        boolean flag = blockLayerIn == BlockRenderLayer.TRANSLUCENT;
        int i1 = flag ? this.renderInfos.size() - 1 : 0;
        int i = flag ? -1 : this.renderInfos.size();
        int j1 = flag ? -1 : 1;
        for (int j = i1; j != i; j += j1) {
            RenderChunk renderchunk = ((ContainerLocalRenderInformation)this.renderInfos.get((int)j)).renderChunk;
            if (renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn)) continue;
            ++l;
            this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
        }
        this.mc.profiler.func_194339_b(() -> "render_" + String.valueOf((Object)blockLayerIn));
        this.renderBlockLayer(blockLayerIn);
        this.mc.profiler.endSection();
        return l;
    }

    private void renderBlockLayer(BlockRenderLayer blockLayerIn) {
        this.mc.entityRenderer.enableLightmap();
        if (OpenGlHelper.useVbo()) {
            GlStateManager.glEnableClientState((int)32884);
            OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
            GlStateManager.glEnableClientState((int)32888);
            OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.lightmapTexUnit);
            GlStateManager.glEnableClientState((int)32888);
            OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
            GlStateManager.glEnableClientState((int)32886);
        }
        this.renderContainer.renderChunkLayer(blockLayerIn);
        if (OpenGlHelper.useVbo()) {
            for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements()) {
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int k1 = vertexformatelement.getIndex();
                switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[vertexformatelement$enumusage.ordinal()]) {
                    case 1: {
                        GlStateManager.glDisableClientState((int)32884);
                        break;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture((int)(OpenGlHelper.defaultTexUnit + k1));
                        GlStateManager.glDisableClientState((int)32888);
                        OpenGlHelper.setClientActiveTexture((int)OpenGlHelper.defaultTexUnit);
                        break;
                    }
                    case 3: {
                        GlStateManager.glDisableClientState((int)32886);
                        GlStateManager.resetColor();
                    }
                }
            }
        }
        this.mc.entityRenderer.disableLightmap();
    }

    private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iteratorIn) {
        while (iteratorIn.hasNext()) {
            DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iteratorIn.next();
            int k1 = destroyblockprogress.getCreationCloudUpdateTick();
            if (this.cloudTickCounter - k1 <= 400) continue;
            iteratorIn.remove();
        }
    }

    public void updateClouds() {
        ++this.cloudTickCounter;
        if (this.cloudTickCounter % 20 == 0) {
            this.cleanupDamagedBlocks((Iterator<DestroyBlockProgress>)this.damagedBlocks.values().iterator());
        }
        if (!this.setLightUpdates.isEmpty() && !this.renderDispatcher.hasNoFreeRenderBuilders() && this.chunksToUpdate.isEmpty()) {
            Iterator iterator = this.setLightUpdates.iterator();
            while (iterator.hasNext()) {
                BlockPos blockpos = (BlockPos)iterator.next();
                iterator.remove();
                int k1 = blockpos.p();
                int l1 = blockpos.q();
                int i2 = blockpos.r();
                this.markBlocksForUpdate(k1 - 1, l1 - 1, i2 - 1, k1 + 1, l1 + 1, i2 + 1, false);
            }
        }
    }

    private void renderSkyEnd() {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask((boolean)false);
        this.renderEngine.bindTexture(END_SKY_TEXTURES);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        for (int k1 = 0; k1 < 6; ++k1) {
            GlStateManager.pushMatrix();
            if (k1 == 1) {
                GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (k1 == 2) {
                GlStateManager.rotate((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (k1 == 3) {
                GlStateManager.rotate((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (k1 == 4) {
                GlStateManager.rotate((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            if (k1 == 5) {
                GlStateManager.rotate((float)-90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(40, 40, 40, 255).endVertex();
            bufferbuilder.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(40, 40, 40, 255).endVertex();
            bufferbuilder.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(40, 40, 40, 255).endVertex();
            bufferbuilder.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(40, 40, 40, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    public void renderSky(float partialTicks, int pass) {
        IRenderHandler renderer = this.world.provider.getSkyRenderer();
        if (renderer != null) {
            renderer.render(partialTicks, this.world, this.mc);
            return;
        }
        if (this.mc.world.provider.getDimensionType().getId() == 1) {
            this.renderSkyEnd();
        } else if (this.mc.world.provider.isSurfaceWorld()) {
            GlStateManager.disableTexture2D();
            Vec3d vec3d = this.world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
            float f = (float)vec3d.x;
            float f1 = (float)vec3d.y;
            float f2 = (float)vec3d.z;
            if (pass != 2) {
                float f3 = (f * 30.0f + f1 * 59.0f + f2 * 11.0f) / 100.0f;
                float f4 = (f * 30.0f + f1 * 70.0f) / 100.0f;
                float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
                f = f3;
                f1 = f4;
                f2 = f5;
            }
            GlStateManager.color((float)f, (float)f1, (float)f2);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.depthMask((boolean)false);
            GlStateManager.enableFog();
            GlStateManager.color((float)f, (float)f1, (float)f2);
            if (this.vboEnabled) {
                this.skyVBO.bindBuffer();
                GlStateManager.glEnableClientState((int)32884);
                GlStateManager.glVertexPointer((int)3, (int)5126, (int)12, (int)0);
                this.skyVBO.drawArrays(7);
                this.skyVBO.unbindBuffer();
                GlStateManager.glDisableClientState((int)32884);
            } else {
                GlStateManager.callList((int)this.glSkyList);
            }
            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            RenderHelper.disableStandardItemLighting();
            float[] afloat = this.world.provider.calcSunriseSunsetColors(this.world.getCelestialAngle(partialTicks), partialTicks);
            if (afloat != null) {
                GlStateManager.disableTexture2D();
                GlStateManager.shadeModel((int)7425);
                GlStateManager.pushMatrix();
                GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)(MathHelper.sin((float)this.world.getCelestialAngleRadians(partialTicks)) < 0.0f ? 180.0f : 0.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.rotate((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                float f6 = afloat[0];
                float f7 = afloat[1];
                float f8 = afloat[2];
                if (pass != 2) {
                    float f9 = (f6 * 30.0f + f7 * 59.0f + f8 * 11.0f) / 100.0f;
                    float f10 = (f6 * 30.0f + f7 * 70.0f) / 100.0f;
                    float f11 = (f6 * 30.0f + f8 * 70.0f) / 100.0f;
                    f6 = f9;
                    f7 = f10;
                    f8 = f11;
                }
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.pos(0.0, 100.0, 0.0).color(f6, f7, f8, afloat[3]).endVertex();
                int l1 = 16;
                for (int j2 = 0; j2 <= 16; ++j2) {
                    float f21 = (float)j2 * ((float)Math.PI * 2) / 16.0f;
                    float f12 = MathHelper.sin((float)f21);
                    float f13 = MathHelper.cos((float)f21);
                    bufferbuilder.pos((double)(f12 * 120.0f), (double)(f13 * 120.0f), (double)(-f13 * 40.0f * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0f).endVertex();
                }
                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.shadeModel((int)7424);
            }
            GlStateManager.enableTexture2D();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            float f16 = 1.0f - this.world.getRainStrength(partialTicks);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)f16);
            GlStateManager.rotate((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(this.world.getCelestialAngle(partialTicks) * 360.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            float f17 = 30.0f;
            this.renderEngine.bindTexture(SUN_TEXTURES);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos((double)(-f17), 100.0, (double)(-f17)).tex(0.0, 0.0).endVertex();
            bufferbuilder.pos((double)f17, 100.0, (double)(-f17)).tex(1.0, 0.0).endVertex();
            bufferbuilder.pos((double)f17, 100.0, (double)f17).tex(1.0, 1.0).endVertex();
            bufferbuilder.pos((double)(-f17), 100.0, (double)f17).tex(0.0, 1.0).endVertex();
            tessellator.draw();
            f17 = 20.0f;
            this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
            int k1 = this.world.getMoonPhase();
            int i2 = k1 % 4;
            int k2 = k1 / 4 % 2;
            float f22 = (float)(i2 + 0) / 4.0f;
            float f23 = (float)(k2 + 0) / 2.0f;
            float f24 = (float)(i2 + 1) / 4.0f;
            float f14 = (float)(k2 + 1) / 2.0f;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos((double)(-f17), -100.0, (double)f17).tex((double)f24, (double)f14).endVertex();
            bufferbuilder.pos((double)f17, -100.0, (double)f17).tex((double)f22, (double)f14).endVertex();
            bufferbuilder.pos((double)f17, -100.0, (double)(-f17)).tex((double)f22, (double)f23).endVertex();
            bufferbuilder.pos((double)(-f17), -100.0, (double)(-f17)).tex((double)f24, (double)f23).endVertex();
            tessellator.draw();
            GlStateManager.disableTexture2D();
            float f15 = this.world.getStarBrightness(partialTicks) * f16;
            if (f15 > 0.0f) {
                GlStateManager.color((float)f15, (float)f15, (float)f15, (float)f15);
                if (this.vboEnabled) {
                    this.starVBO.bindBuffer();
                    GlStateManager.glEnableClientState((int)32884);
                    GlStateManager.glVertexPointer((int)3, (int)5126, (int)12, (int)0);
                    this.starVBO.drawArrays(7);
                    this.starVBO.unbindBuffer();
                    GlStateManager.glDisableClientState((int)32884);
                } else {
                    GlStateManager.callList((int)this.starGLCallList);
                }
            }
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableFog();
            GlStateManager.popMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.color((float)0.0f, (float)0.0f, (float)0.0f);
            double d3 = this.mc.player.f((float)partialTicks).y - this.world.getHorizon();
            if (d3 < 0.0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)0.0f, (float)12.0f, (float)0.0f);
                if (this.vboEnabled) {
                    this.sky2VBO.bindBuffer();
                    GlStateManager.glEnableClientState((int)32884);
                    GlStateManager.glVertexPointer((int)3, (int)5126, (int)12, (int)0);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GlStateManager.glDisableClientState((int)32884);
                } else {
                    GlStateManager.callList((int)this.glSkyList2);
                }
                GlStateManager.popMatrix();
                float f18 = 1.0f;
                float f19 = -((float)(d3 + 65.0));
                float f20 = -1.0f;
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.pos(-1.0, (double)f19, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, (double)f19, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, (double)f19, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, (double)f19, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, (double)f19, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, (double)f19, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, (double)f19, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, (double)f19, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
            }
            if (this.world.provider.isSkyColored()) {
                GlStateManager.color((float)(f * 0.2f + 0.04f), (float)(f1 * 0.2f + 0.04f), (float)(f2 * 0.6f + 0.1f));
            } else {
                GlStateManager.color((float)f, (float)f1, (float)f2);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)0.0f, (float)(-((float)(d3 - 16.0))), (float)0.0f);
            GlStateManager.callList((int)this.glSkyList2);
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask((boolean)true);
        }
    }

    public void renderClouds(float partialTicks, int pass, double x, double y, double z) {
        if (FMLClientHandler.instance().renderClouds(this.cloudTickCounter, partialTicks)) {
            return;
        }
        if (this.mc.world.provider.isSurfaceWorld()) {
            if (this.mc.gameSettings.shouldRenderClouds() == 2) {
                this.renderCloudsFancy(partialTicks, pass, x, y, z);
            } else {
                GlStateManager.disableCull();
                int k1 = 32;
                int l1 = 8;
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                this.renderEngine.bindTexture(CLOUDS_TEXTURES);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
                Vec3d vec3d = this.world.getCloudColour(partialTicks);
                float f = (float)vec3d.x;
                float f1 = (float)vec3d.y;
                float f2 = (float)vec3d.z;
                if (pass != 2) {
                    float f3 = (f * 30.0f + f1 * 59.0f + f2 * 11.0f) / 100.0f;
                    float f4 = (f * 30.0f + f1 * 70.0f) / 100.0f;
                    float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
                    f = f3;
                    f1 = f4;
                    f2 = f5;
                }
                float f9 = 4.8828125E-4f;
                double d5 = (float)this.cloudTickCounter + partialTicks;
                double d3 = x + d5 * (double)0.03f;
                int i2 = MathHelper.floor((double)(d3 / 2048.0));
                int j2 = MathHelper.floor((double)(z / 2048.0));
                double lvt_22_1_ = z - (double)(j2 * 2048);
                float f6 = this.world.provider.getCloudHeight() - (float)y + 0.33f;
                float f7 = (float)((d3 -= (double)(i2 * 2048)) * 4.8828125E-4);
                float f8 = (float)(lvt_22_1_ * 4.8828125E-4);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                for (int k2 = -256; k2 < 256; k2 += 32) {
                    for (int l2 = -256; l2 < 256; l2 += 32) {
                        bufferbuilder.pos((double)(k2 + 0), (double)f6, (double)(l2 + 32)).tex((double)((float)(k2 + 0) * 4.8828125E-4f + f7), (double)((float)(l2 + 32) * 4.8828125E-4f + f8)).color(f, f1, f2, 0.8f).endVertex();
                        bufferbuilder.pos((double)(k2 + 32), (double)f6, (double)(l2 + 32)).tex((double)((float)(k2 + 32) * 4.8828125E-4f + f7), (double)((float)(l2 + 32) * 4.8828125E-4f + f8)).color(f, f1, f2, 0.8f).endVertex();
                        bufferbuilder.pos((double)(k2 + 32), (double)f6, (double)(l2 + 0)).tex((double)((float)(k2 + 32) * 4.8828125E-4f + f7), (double)((float)(l2 + 0) * 4.8828125E-4f + f8)).color(f, f1, f2, 0.8f).endVertex();
                        bufferbuilder.pos((double)(k2 + 0), (double)f6, (double)(l2 + 0)).tex((double)((float)(k2 + 0) * 4.8828125E-4f + f7), (double)((float)(l2 + 0) * 4.8828125E-4f + f8)).color(f, f1, f2, 0.8f).endVertex();
                    }
                }
                tessellator.draw();
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
            }
        }
    }

    public boolean hasCloudFog(double x, double y, double z, float partialTicks) {
        return false;
    }

    private void renderCloudsFancy(float partialTicks, int pass, double x, double y, double z) {
        GlStateManager.disableCull();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = 12.0f;
        float f1 = 4.0f;
        double d3 = (float)this.cloudTickCounter + partialTicks;
        double d4 = (x + d3 * (double)0.03f) / 12.0;
        double d5 = z / 12.0 + (double)0.33f;
        float f2 = this.world.provider.getCloudHeight() - (float)y + 0.33f;
        int k1 = MathHelper.floor((double)(d4 / 2048.0));
        int l1 = MathHelper.floor((double)(d5 / 2048.0));
        d4 -= (double)(k1 * 2048);
        d5 -= (double)(l1 * 2048);
        this.renderEngine.bindTexture(CLOUDS_TEXTURES);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        Vec3d vec3d = this.world.getCloudColour(partialTicks);
        float f3 = (float)vec3d.x;
        float f4 = (float)vec3d.y;
        float f5 = (float)vec3d.z;
        if (pass != 2) {
            float f6 = (f3 * 30.0f + f4 * 59.0f + f5 * 11.0f) / 100.0f;
            float f7 = (f3 * 30.0f + f4 * 70.0f) / 100.0f;
            float f8 = (f3 * 30.0f + f5 * 70.0f) / 100.0f;
            f3 = f6;
            f4 = f7;
            f5 = f8;
        }
        float f25 = f3 * 0.9f;
        float f26 = f4 * 0.9f;
        float f27 = f5 * 0.9f;
        float f9 = f3 * 0.7f;
        float f10 = f4 * 0.7f;
        float f11 = f5 * 0.7f;
        float f12 = f3 * 0.8f;
        float f13 = f4 * 0.8f;
        float f14 = f5 * 0.8f;
        float f15 = 0.00390625f;
        float f16 = (float)MathHelper.floor((double)d4) * 0.00390625f;
        float f17 = (float)MathHelper.floor((double)d5) * 0.00390625f;
        float f18 = (float)(d4 - (double)MathHelper.floor((double)d4));
        float f19 = (float)(d5 - (double)MathHelper.floor((double)d5));
        int i2 = 8;
        int j2 = 4;
        float f20 = 9.765625E-4f;
        GlStateManager.scale((float)12.0f, (float)1.0f, (float)12.0f);
        for (int k2 = 0; k2 < 2; ++k2) {
            if (k2 == 0) {
                GlStateManager.colorMask((boolean)false, (boolean)false, (boolean)false, (boolean)false);
            } else {
                switch (pass) {
                    case 0: {
                        GlStateManager.colorMask((boolean)false, (boolean)true, (boolean)true, (boolean)true);
                        break;
                    }
                    case 1: {
                        GlStateManager.colorMask((boolean)true, (boolean)false, (boolean)false, (boolean)true);
                        break;
                    }
                    case 2: {
                        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                    }
                }
            }
            for (int l2 = -3; l2 <= 4; ++l2) {
                for (int i3 = -3; i3 <= 4; ++i3) {
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    float f21 = l2 * 8;
                    float f22 = i3 * 8;
                    float f23 = f21 - f18;
                    float f24 = f22 - f19;
                    if (f2 > -5.0f) {
                        bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 0.0f), (double)(f24 + 8.0f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f9, f10, f11, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 0.0f), (double)(f24 + 8.0f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f9, f10, f11, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 0.0f), (double)(f24 + 0.0f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f9, f10, f11, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 0.0f), (double)(f24 + 0.0f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f9, f10, f11, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    }
                    if (f2 <= 5.0f) {
                        bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 4.0f - 9.765625E-4f), (double)(f24 + 8.0f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f3, f4, f5, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 4.0f - 9.765625E-4f), (double)(f24 + 8.0f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f3, f4, f5, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 4.0f - 9.765625E-4f), (double)(f24 + 0.0f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f3, f4, f5, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 4.0f - 9.765625E-4f), (double)(f24 + 0.0f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f3, f4, f5, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                    }
                    if (l2 > -1) {
                        for (int j3 = 0; j3 < 8; ++j3) {
                            bufferbuilder.pos((double)(f23 + (float)j3 + 0.0f), (double)(f2 + 0.0f), (double)(f24 + 8.0f)).tex((double)((f21 + (float)j3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + (float)j3 + 0.0f), (double)(f2 + 4.0f), (double)(f24 + 8.0f)).tex((double)((f21 + (float)j3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + (float)j3 + 0.0f), (double)(f2 + 4.0f), (double)(f24 + 0.0f)).tex((double)((f21 + (float)j3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + (float)j3 + 0.0f), (double)(f2 + 0.0f), (double)(f24 + 0.0f)).tex((double)((f21 + (float)j3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (l2 <= 1) {
                        for (int k3 = 0; k3 < 8; ++k3) {
                            bufferbuilder.pos((double)(f23 + (float)k3 + 1.0f - 9.765625E-4f), (double)(f2 + 0.0f), (double)(f24 + 8.0f)).tex((double)((f21 + (float)k3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + (float)k3 + 1.0f - 9.765625E-4f), (double)(f2 + 4.0f), (double)(f24 + 8.0f)).tex((double)((f21 + (float)k3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 8.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + (float)k3 + 1.0f - 9.765625E-4f), (double)(f2 + 4.0f), (double)(f24 + 0.0f)).tex((double)((f21 + (float)k3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + (float)k3 + 1.0f - 9.765625E-4f), (double)(f2 + 0.0f), (double)(f24 + 0.0f)).tex((double)((f21 + (float)k3 + 0.5f) * 0.00390625f + f16), (double)((f22 + 0.0f) * 0.00390625f + f17)).color(f25, f26, f27, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (i3 > -1) {
                        for (int l3 = 0; l3 < 8; ++l3) {
                            bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 4.0f), (double)(f24 + (float)l3 + 0.0f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + (float)l3 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 4.0f), (double)(f24 + (float)l3 + 0.0f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + (float)l3 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 0.0f), (double)(f24 + (float)l3 + 0.0f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + (float)l3 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 0.0f), (double)(f24 + (float)l3 + 0.0f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + (float)l3 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                        }
                    }
                    if (i3 <= 1) {
                        for (int i4 = 0; i4 < 8; ++i4) {
                            bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 4.0f), (double)(f24 + (float)i4 + 1.0f - 9.765625E-4f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + (float)i4 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 4.0f), (double)(f24 + (float)i4 + 1.0f - 9.765625E-4f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + (float)i4 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + 8.0f), (double)(f2 + 0.0f), (double)(f24 + (float)i4 + 1.0f - 9.765625E-4f)).tex((double)((f21 + 8.0f) * 0.00390625f + f16), (double)((f22 + (float)i4 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            bufferbuilder.pos((double)(f23 + 0.0f), (double)(f2 + 0.0f), (double)(f24 + (float)i4 + 1.0f - 9.765625E-4f)).tex((double)((f21 + 0.0f) * 0.00390625f + f16), (double)((f22 + (float)i4 + 0.5f) * 0.00390625f + f17)).color(f12, f13, f14, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                        }
                    }
                    tessellator.draw();
                }
            }
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    public void updateChunks(long finishTimeNano) {
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
        if (!this.chunksToUpdate.isEmpty()) {
            RenderChunk renderchunk1;
            boolean flag1;
            Iterator iterator = this.chunksToUpdate.iterator();
            while (iterator.hasNext() && (flag1 = (renderchunk1 = (RenderChunk)iterator.next()).needsImmediateUpdate() ? this.renderDispatcher.updateChunkNow(renderchunk1) : this.renderDispatcher.updateChunkLater(renderchunk1))) {
                renderchunk1.clearNeedsUpdate();
                iterator.remove();
                long k1 = finishTimeNano - System.nanoTime();
                if (k1 >= 0L) continue;
                break;
            }
        }
    }

    public void renderWorldBorder(Entity entityIn, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        WorldBorder worldborder = this.world.getWorldBorder();
        double d3 = this.mc.gameSettings.renderDistanceChunks * 16;
        if (entityIn.posX >= worldborder.maxX() - d3 || entityIn.posX <= worldborder.minX() + d3 || entityIn.posZ >= worldborder.maxZ() - d3 || entityIn.posZ <= worldborder.minZ() + d3) {
            double d4 = 1.0 - worldborder.getClosestDistance(entityIn) / d3;
            d4 = Math.pow((double)d4, (double)4.0);
            double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
            double d6 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
            double d7 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
            GlStateManager.depthMask((boolean)false);
            GlStateManager.pushMatrix();
            int k1 = worldborder.getStatus().getColor();
            float f = (float)(k1 >> 16 & 0xFF) / 255.0f;
            float f1 = (float)(k1 >> 8 & 0xFF) / 255.0f;
            float f2 = (float)(k1 & 0xFF) / 255.0f;
            GlStateManager.color((float)f, (float)f1, (float)f2, (float)((float)d4));
            GlStateManager.doPolygonOffset((float)-3.0f, (float)-3.0f);
            GlStateManager.enablePolygonOffset();
            GlStateManager.alphaFunc((int)516, (float)0.1f);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            float f3 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            float f6 = 128.0f;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.setTranslation(-d5, -d6, -d7);
            double d8 = Math.max((double)MathHelper.floor((double)(d7 - d3)), (double)worldborder.minZ());
            double d9 = Math.min((double)MathHelper.ceil((double)(d7 + d3)), (double)worldborder.maxZ());
            if (d5 > worldborder.maxX() - d3) {
                float f7 = 0.0f;
                double d10 = d8;
                while (d10 < d9) {
                    double d11 = Math.min((double)1.0, (double)(d9 - d10));
                    float f8 = (float)d11 * 0.5f;
                    bufferbuilder.pos(worldborder.maxX(), 256.0, d10).tex((double)(f3 + f7), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(worldborder.maxX(), 256.0, d10 + d11).tex((double)(f3 + f8 + f7), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(worldborder.maxX(), 0.0, d10 + d11).tex((double)(f3 + f8 + f7), (double)(f3 + 128.0f)).endVertex();
                    bufferbuilder.pos(worldborder.maxX(), 0.0, d10).tex((double)(f3 + f7), (double)(f3 + 128.0f)).endVertex();
                    d10 += 1.0;
                    f7 += 0.5f;
                }
            }
            if (d5 < worldborder.minX() + d3) {
                float f9 = 0.0f;
                double d12 = d8;
                while (d12 < d9) {
                    double d15 = Math.min((double)1.0, (double)(d9 - d12));
                    float f12 = (float)d15 * 0.5f;
                    bufferbuilder.pos(worldborder.minX(), 256.0, d12).tex((double)(f3 + f9), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(worldborder.minX(), 256.0, d12 + d15).tex((double)(f3 + f12 + f9), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(worldborder.minX(), 0.0, d12 + d15).tex((double)(f3 + f12 + f9), (double)(f3 + 128.0f)).endVertex();
                    bufferbuilder.pos(worldborder.minX(), 0.0, d12).tex((double)(f3 + f9), (double)(f3 + 128.0f)).endVertex();
                    d12 += 1.0;
                    f9 += 0.5f;
                }
            }
            d8 = Math.max((double)MathHelper.floor((double)(d5 - d3)), (double)worldborder.minX());
            d9 = Math.min((double)MathHelper.ceil((double)(d5 + d3)), (double)worldborder.maxX());
            if (d7 > worldborder.maxZ() - d3) {
                float f10 = 0.0f;
                double d13 = d8;
                while (d13 < d9) {
                    double d16 = Math.min((double)1.0, (double)(d9 - d13));
                    float f13 = (float)d16 * 0.5f;
                    bufferbuilder.pos(d13, 256.0, worldborder.maxZ()).tex((double)(f3 + f10), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(d13 + d16, 256.0, worldborder.maxZ()).tex((double)(f3 + f13 + f10), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(d13 + d16, 0.0, worldborder.maxZ()).tex((double)(f3 + f13 + f10), (double)(f3 + 128.0f)).endVertex();
                    bufferbuilder.pos(d13, 0.0, worldborder.maxZ()).tex((double)(f3 + f10), (double)(f3 + 128.0f)).endVertex();
                    d13 += 1.0;
                    f10 += 0.5f;
                }
            }
            if (d7 < worldborder.minZ() + d3) {
                float f11 = 0.0f;
                double d14 = d8;
                while (d14 < d9) {
                    double d17 = Math.min((double)1.0, (double)(d9 - d14));
                    float f14 = (float)d17 * 0.5f;
                    bufferbuilder.pos(d14, 256.0, worldborder.minZ()).tex((double)(f3 + f11), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(d14 + d17, 256.0, worldborder.minZ()).tex((double)(f3 + f14 + f11), (double)(f3 + 0.0f)).endVertex();
                    bufferbuilder.pos(d14 + d17, 0.0, worldborder.minZ()).tex((double)(f3 + f14 + f11), (double)(f3 + 128.0f)).endVertex();
                    bufferbuilder.pos(d14, 0.0, worldborder.minZ()).tex((double)(f3 + f11), (double)(f3 + 128.0f)).endVertex();
                    d14 += 1.0;
                    f11 += 0.5f;
                }
            }
            tessellator.draw();
            bufferbuilder.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableAlpha();
            GlStateManager.doPolygonOffset((float)0.0f, (float)0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.depthMask((boolean)true);
        }
    }

    private void preRenderDamagedBlocks() {
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.DST_COLOR, (GlStateManager.DestFactor)GlStateManager.DestFactor.SRC_COLOR, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.enableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)0.5f);
        GlStateManager.doPolygonOffset((float)-1.0f, (float)-10.0f);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }

    private void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset((float)0.0f, (float)0.0f);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.popMatrix();
    }

    public void drawBlockDamageTexture(Tessellator tessellatorIn, BufferBuilder bufferBuilderIn, Entity entityIn, float partialTicks) {
        double d3 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d4 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d5 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        if (!this.damagedBlocks.isEmpty()) {
            this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.preRenderDamagedBlocks();
            bufferBuilderIn.begin(7, DefaultVertexFormats.BLOCK);
            bufferBuilderIn.setTranslation(-d3, -d4, -d5);
            bufferBuilderIn.noColor();
            Iterator iterator = this.damagedBlocks.values().iterator();
            while (iterator.hasNext()) {
                boolean hasBreak;
                DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iterator.next();
                BlockPos blockpos = destroyblockprogress.getPosition();
                double d6 = (double)blockpos.p() - d3;
                double d7 = (double)blockpos.q() - d4;
                double d8 = (double)blockpos.r() - d5;
                Block block = this.world.getBlockState(blockpos).getBlock();
                TileEntity te = this.world.getTileEntity(blockpos);
                boolean bl = hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull;
                if (!hasBreak) {
                    boolean bl2 = hasBreak = te != null && te.canRenderBreaking();
                }
                if (hasBreak) continue;
                if (d6 * d6 + d7 * d7 + d8 * d8 > 1024.0) {
                    iterator.remove();
                    continue;
                }
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                if (iblockstate.a() == Material.AIR) continue;
                int k1 = destroyblockprogress.getPartialBlockDamage();
                TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[k1];
                BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
                blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, (IBlockAccess)this.world);
            }
            tessellatorIn.draw();
            bufferBuilderIn.setTranslation(0.0, 0.0, 0.0);
            this.postRenderDamagedBlocks();
        }
    }

    public void drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks) {
        if (execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth((float)2.0f);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.a() != Material.AIR && this.world.getWorldBorder().contains(blockpos)) {
                double d3 = player.M + (player.p - player.M) * (double)partialTicks;
                double d4 = player.N + (player.q - player.N) * (double)partialTicks;
                double d5 = player.O + (player.r - player.O) * (double)partialTicks;
                RenderGlobal.drawSelectionBoundingBox(iblockstate.c((World)this.world, blockpos).grow((double)0.002f).offset(-d3, -d4, -d5), 0.0f, 0.0f, 0.0f, 0.4f);
            }
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
        RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
    }

    public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderGlobal.drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void drawBoundingBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        buffer.pos(minX, minY, minZ).color(red, green, blue, 0.0f).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, 0.0f).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0f).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0f).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, 0.0f).endVertex();
    }

    public static void renderFilledBox(AxisAlignedBB aabb, float red, float green, float blue, float alpha) {
        RenderGlobal.renderFilledBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, red, green, blue, alpha);
    }

    public static void renderFilledBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        RenderGlobal.addChainedFilledBoxVertices(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void addChainedFilledBoxVertices(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
    }

    private void markBlocksForUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean updateImmediately) {
        this.viewFrustum.markBlocksForUpdate(minX, minY, minZ, maxX, maxY, maxZ, updateImmediately);
    }

    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
        int k1 = pos.p();
        int l1 = pos.q();
        int i2 = pos.r();
        this.markBlocksForUpdate(k1 - 1, l1 - 1, i2 - 1, k1 + 1, l1 + 1, i2 + 1, (flags & 8) != 0);
    }

    public void notifyLightSet(BlockPos pos) {
        this.setLightUpdates.add((Object)pos.toImmutable());
    }

    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1, false);
    }

    public void playRecord(@Nullable SoundEvent soundIn, BlockPos pos) {
        ISound isound = (ISound)this.mapSoundPositions.get((Object)pos);
        if (isound != null) {
            this.mc.getSoundHandler().stopSound(isound);
            this.mapSoundPositions.remove((Object)pos);
        }
        if (soundIn != null) {
            ItemRecord itemrecord = ItemRecord.getBySound((SoundEvent)soundIn);
            if (itemrecord != null) {
                this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
            }
            PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.getRecordSoundRecord((SoundEvent)soundIn, (float)pos.p(), (float)pos.q(), (float)pos.r());
            this.mapSoundPositions.put((Object)pos, (Object)positionedsoundrecord);
            this.mc.getSoundHandler().playSound((ISound)positionedsoundrecord);
        }
        this.setPartying((World)this.world, pos, soundIn != null);
    }

    private void setPartying(World worldIn, BlockPos pos, boolean isPartying) {
        for (EntityLivingBase entitylivingbase : worldIn.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(3.0))) {
            entitylivingbase.setPartying(pos, isPartying);
        }
    }

    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {
    }

    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
        this.spawnParticle(particleID, ignoreRange, false, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }

    public void spawnParticle(int id, boolean ignoreRange, boolean minimiseParticleLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
        try {
            this.spawnParticle0(id, ignoreRange, minimiseParticleLevel, x, y, z, xSpeed, ySpeed, zSpeed, parameters);
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Exception while adding particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("ID", (Object)id);
            if (parameters != null) {
                crashreportcategory.addCrashSection("Parameters", (Object)parameters);
            }
            crashreportcategory.addDetail("Position", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
            throw new ReportedException(crashreport);
        }
    }

    private void spawnParticle(EnumParticleTypes particleIn, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
        this.spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }

    @Nullable
    private Particle spawnParticle0(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
        return this.spawnParticle0(particleID, ignoreRange, false, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }

    @Nullable
    private Particle spawnParticle0(int particleID, boolean ignoreRange, boolean minParticles, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
        Entity entity = this.mc.getRenderViewEntity();
        if (this.mc != null && entity != null && this.mc.effectRenderer != null) {
            int k1 = this.calculateParticleLevel(minParticles);
            double d3 = entity.posX - xCoord;
            double d4 = entity.posY - yCoord;
            double d5 = entity.posZ - zCoord;
            if (ignoreRange) {
                return this.mc.effectRenderer.spawnEffectParticle(particleID, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            }
            if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0) {
                return null;
            }
            return k1 > 1 ? null : this.mc.effectRenderer.spawnEffectParticle(particleID, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
        }
        return null;
    }

    private int calculateParticleLevel(boolean minimiseLevel) {
        int k1 = this.mc.gameSettings.particleSetting;
        if (minimiseLevel && k1 == 2 && this.world.rand.nextInt(10) == 0) {
            k1 = 1;
        }
        if (k1 == 1 && this.world.rand.nextInt(3) == 0) {
            k1 = 2;
        }
        return k1;
    }

    public void onEntityAdded(Entity entityIn) {
    }

    public void onEntityRemoved(Entity entityIn) {
    }

    public void deleteAllDisplayLists() {
    }

    public void broadcastSound(int soundID, BlockPos pos, int data) {
        switch (soundID) {
            case 1023: 
            case 1028: 
            case 1038: {
                Entity entity = this.mc.getRenderViewEntity();
                if (entity == null) break;
                double d3 = (double)pos.p() - entity.posX;
                double d4 = (double)pos.q() - entity.posY;
                double d5 = (double)pos.r() - entity.posZ;
                double d6 = Math.sqrt((double)(d3 * d3 + d4 * d4 + d5 * d5));
                double d7 = entity.posX;
                double d8 = entity.posY;
                double d9 = entity.posZ;
                if (d6 > 0.0) {
                    d7 += d3 / d6 * 2.0;
                    d8 += d4 / d6 * 2.0;
                    d9 += d5 / d6 * 2.0;
                }
                if (soundID == 1023) {
                    this.world.playSound(d7, d8, d9, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                if (soundID == 1038) {
                    this.world.playSound(d7, d8, d9, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.HOSTILE, 1.0f, 1.0f, false);
                    break;
                }
                this.world.playSound(d7, d8, d9, SoundEvents.ENTITY_ENDERDRAGON_DEATH, SoundCategory.HOSTILE, 5.0f, 1.0f, false);
            }
        }
    }

    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
        Random random = this.world.rand;
        switch (type) {
            case 1000: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1001: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0f, 1.2f, false);
                break;
            }
            case 1002: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 1.0f, 1.2f, false);
                break;
            }
            case 1003: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 1.0f, 1.2f, false);
                break;
            }
            case 1004: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.NEUTRAL, 1.0f, 1.2f, false);
                break;
            }
            case 1005: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1006: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1007: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1008: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_FENCE_GATE_OPEN, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1009: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f, false);
                break;
            }
            case 1010: {
                if (Item.getItemById((int)data) instanceof ItemRecord) {
                    this.world.playRecord(blockPosIn, ((ItemRecord)Item.getItemById((int)data)).getSound());
                    break;
                }
                this.world.playRecord(blockPosIn, (SoundEvent)null);
                break;
            }
            case 1011: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1012: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1013: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1014: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_FENCE_GATE_CLOSE, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1015: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_GHAST_WARN, SoundCategory.HOSTILE, 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1016: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1017: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.HOSTILE, 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1018: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1019: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1020: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1021: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1022: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1024: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1025: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 0.05f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1026: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1027: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1029: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1030: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1031: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1032: {
                this.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.BLOCK_PORTAL_TRAVEL, (float)(random.nextFloat() * 0.4f + 0.8f)));
                break;
            }
            case 1033: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1034: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1035: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                break;
            }
            case 1036: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1037: {
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2000: {
                int j2 = data % 3 - 1;
                int k1 = data / 3 % 3 - 1;
                double d11 = (double)blockPosIn.p() + (double)j2 * 0.6 + 0.5;
                double d13 = (double)blockPosIn.q() + 0.5;
                double d15 = (double)blockPosIn.r() + (double)k1 * 0.6 + 0.5;
                for (int l2 = 0; l2 < 10; ++l2) {
                    double d16 = random.nextDouble() * 0.2 + 0.01;
                    double d19 = d11 + (double)j2 * 0.01 + (random.nextDouble() - 0.5) * (double)k1 * 0.5;
                    double d22 = d13 + (random.nextDouble() - 0.5) * 0.5;
                    double d25 = d15 + (double)k1 * 0.01 + (random.nextDouble() - 0.5) * (double)j2 * 0.5;
                    double d27 = (double)j2 * d16 + random.nextGaussian() * 0.01;
                    double d29 = -0.03 + random.nextGaussian() * 0.01;
                    double d30 = (double)k1 * d16 + random.nextGaussian() * 0.01;
                    this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d19, d22, d25, d27, d29, d30, new int[0]);
                }
                return;
            }
            case 2001: {
                Block block = Block.getBlockById((int)(data & 0xFFF));
                if (block.getDefaultState().a() != Material.AIR) {
                    SoundType soundtype = block.getSoundType(Block.getStateById((int)data), (World)this.world, blockPosIn, null);
                    this.world.playSound(blockPosIn, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f, false);
                }
                this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(data >> 12 & 0xFF));
                break;
            }
            case 2002: 
            case 2007: {
                double d9 = blockPosIn.p();
                double d10 = blockPosIn.q();
                double d12 = blockPosIn.r();
                for (int k2 = 0; k2 < 8; ++k2) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d9, d10, d12, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem((Item)Items.SPLASH_POTION));
                }
                float f5 = (float)(data >> 16 & 0xFF) / 255.0f;
                float f = (float)(data >> 8 & 0xFF) / 255.0f;
                float f1 = (float)(data >> 0 & 0xFF) / 255.0f;
                EnumParticleTypes enumparticletypes = type == 2007 ? EnumParticleTypes.SPELL_INSTANT : EnumParticleTypes.SPELL;
                for (int j3 = 0; j3 < 100; ++j3) {
                    double d18 = random.nextDouble() * 4.0;
                    double d21 = random.nextDouble() * Math.PI * 2.0;
                    double d24 = Math.cos((double)d21) * d18;
                    double d26 = 0.01 + random.nextDouble() * 0.5;
                    double d28 = Math.sin((double)d21) * d18;
                    Particle particle1 = this.spawnParticle0(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d9 + d24 * 0.1, d10 + 0.3, d12 + d28 * 0.1, d24, d26, d28, new int[0]);
                    if (particle1 == null) continue;
                    float f4 = 0.75f + random.nextFloat() * 0.25f;
                    particle1.setRBGColorF(f5 * f4, f * f4, f1 * f4);
                    particle1.multiplyVelocity((float)d18);
                }
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.NEUTRAL, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2003: {
                double d3 = (double)blockPosIn.p() + 0.5;
                double d4 = blockPosIn.q();
                double d5 = (double)blockPosIn.r() + 0.5;
                for (int l1 = 0; l1 < 8; ++l1) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d3, d4, d5, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem((Item)Items.ENDER_EYE));
                }
                for (double d14 = 0.0; d14 < Math.PI * 2; d14 += 0.15707963267948966) {
                    this.spawnParticle(EnumParticleTypes.PORTAL, d3 + Math.cos((double)d14) * 5.0, d4 - 0.4, d5 + Math.sin((double)d14) * 5.0, Math.cos((double)d14) * -5.0, 0.0, Math.sin((double)d14) * -5.0, new int[0]);
                    this.spawnParticle(EnumParticleTypes.PORTAL, d3 + Math.cos((double)d14) * 5.0, d4 - 0.4, d5 + Math.sin((double)d14) * 5.0, Math.cos((double)d14) * -7.0, 0.0, Math.sin((double)d14) * -7.0, new int[0]);
                }
                return;
            }
            case 2004: {
                for (int i3 = 0; i3 < 20; ++i3) {
                    double d17 = (double)blockPosIn.p() + 0.5 + ((double)this.world.rand.nextFloat() - 0.5) * 2.0;
                    double d20 = (double)blockPosIn.q() + 0.5 + ((double)this.world.rand.nextFloat() - 0.5) * 2.0;
                    double d23 = (double)blockPosIn.r() + 0.5 + ((double)this.world.rand.nextFloat() - 0.5) * 2.0;
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d17, d20, d23, 0.0, 0.0, 0.0, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.FLAME, d17, d20, d23, 0.0, 0.0, 0.0, new int[0]);
                }
                return;
            }
            case 2005: {
                ItemDye.spawnBonemealParticles((World)this.world, (BlockPos)blockPosIn, (int)data);
                break;
            }
            case 2006: {
                for (int i2 = 0; i2 < 200; ++i2) {
                    float f2 = random.nextFloat() * 4.0f;
                    float f3 = random.nextFloat() * ((float)Math.PI * 2);
                    double d6 = MathHelper.cos((float)f3) * f2;
                    double d7 = 0.01 + random.nextDouble() * 0.5;
                    double d8 = MathHelper.sin((float)f3) * f2;
                    Particle particle = this.spawnParticle0(EnumParticleTypes.DRAGON_BREATH.getParticleID(), false, (double)blockPosIn.p() + d6 * 0.1, (double)blockPosIn.q() + 0.3, (double)blockPosIn.r() + d8 * 0.1, d6, d7, d8, new int[0]);
                    if (particle == null) continue;
                    particle.multiplyVelocity(f2);
                }
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ENDERDRAGON_FIREBALL_EPLD, SoundCategory.HOSTILE, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 3000: {
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, (double)blockPosIn.p() + 0.5, (double)blockPosIn.q() + 0.5, (double)blockPosIn.r() + 0.5, 0.0, 0.0, 0.0, new int[0]);
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.BLOCKS, 10.0f, (1.0f + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2f) * 0.7f, false);
                break;
            }
            case 3001: {
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.HOSTILE, 64.0f, 0.8f + this.world.rand.nextFloat() * 0.3f, false);
            }
        }
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
        if (progress >= 0 && progress < 10) {
            DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)this.damagedBlocks.get((Object)breakerId);
            if (destroyblockprogress == null || destroyblockprogress.getPosition().p() != pos.p() || destroyblockprogress.getPosition().q() != pos.q() || destroyblockprogress.getPosition().r() != pos.r()) {
                destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put((Object)breakerId, (Object)destroyblockprogress);
            }
            destroyblockprogress.setPartialBlockDamage(progress);
            destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
        } else {
            this.damagedBlocks.remove((Object)breakerId);
        }
    }

    public boolean hasNoChunkUpdates() {
        return this.chunksToUpdate.isEmpty() && this.renderDispatcher.hasNoChunkUpdates();
    }

    public void setDisplayListEntitiesDirty() {
        this.displayListEntitiesDirty = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd) {
        Set<TileEntity> set = this.setTileEntities;
        synchronized (set) {
            this.setTileEntities.removeAll(tileEntitiesToRemove);
            this.setTileEntities.addAll(tileEntitiesToAdd);
        }
    }
}
