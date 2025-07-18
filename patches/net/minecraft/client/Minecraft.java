package net.minecraft.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.ScreenChatOptions;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.util.ISearchTree;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.util.SearchTree;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Minecraft implements IThreadListener, ISnooperInfo {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation LOCATION_MOJANG_PNG = new ResourceLocation("textures/gui/title/mojang.png");
   public static final boolean IS_RUNNING_ON_MAC = Util.getOSType() == Util.EnumOS.OSX;
   public static byte[] memoryReserve = new byte[10485760];
   private static final List<DisplayMode> MAC_DISPLAY_MODES = Lists.newArrayList(new DisplayMode[]{new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)});
   private final File fileResourcepacks;
   private final PropertyMap twitchDetails;
   private final PropertyMap profileProperties;
   private ServerData currentServerData;
   private TextureManager renderEngine;
   private static Minecraft instance;
   private final DataFixer dataFixer;
   public PlayerControllerMP playerController;
   private boolean fullscreen;
   private final boolean enableGLErrorChecking = true;
   private boolean hasCrashed;
   private CrashReport crashReporter;
   public int displayWidth;
   public int displayHeight;
   private boolean connectedToRealms;
   private final Timer timer = new Timer(20.0F);
   private final Snooper usageSnooper = new Snooper("client", this, MinecraftServer.getCurrentTimeMillis());
   public WorldClient world;
   public RenderGlobal renderGlobal;
   private RenderManager renderManager;
   private RenderItem renderItem;
   private ItemRenderer itemRenderer;
   public EntityPlayerSP player;
   @Nullable
   private Entity renderViewEntity;
   public Entity pointedEntity;
   public ParticleManager effectRenderer;
   private SearchTreeManager searchTreeManager = new SearchTreeManager();
   private final Session session;
   private boolean isGamePaused;
   private float renderPartialTicksPaused;
   public FontRenderer fontRenderer;
   public FontRenderer standardGalacticFontRenderer;
   @Nullable
   public GuiScreen currentScreen;
   public LoadingScreenRenderer loadingScreen;
   public EntityRenderer entityRenderer;
   public DebugRenderer debugRenderer;
   private int leftClickCounter;
   private final int tempDisplayWidth;
   private final int tempDisplayHeight;
   @Nullable
   private IntegratedServer integratedServer;
   public GuiIngame ingameGUI;
   public boolean skipRenderWorld;
   public RayTraceResult objectMouseOver;
   public GameSettings gameSettings;
   public CreativeSettings creativeSettings;
   public MouseHelper mouseHelper;
   public final File gameDir;
   private final File fileAssets;
   private final String launchedVersion;
   private final String versionType;
   private final Proxy proxy;
   private ISaveFormat saveLoader;
   private static int debugFPS;
   private int rightClickDelayTimer;
   private String serverName;
   private int serverPort;
   public boolean inGameHasFocus;
   long systemTime = getSystemTime();
   private int joinPlayerCounter;
   public final FrameTimer frameTimer = new FrameTimer();
   long startNanoTime = System.nanoTime();
   private final boolean jvm64bit;
   private final boolean isDemo;
   @Nullable
   private NetworkManager networkManager;
   private boolean integratedServerIsRunning;
   public final Profiler profiler = new Profiler();
   private long debugCrashKeyPressTime = -1L;
   private IReloadableResourceManager resourceManager;
   private final MetadataSerializer metadataSerializer = new MetadataSerializer();
   private final List<IResourcePack> defaultResourcePacks = Lists.newArrayList();
   private final DefaultResourcePack defaultResourcePack;
   private ResourcePackRepository resourcePackRepository;
   private LanguageManager languageManager;
   private BlockColors blockColors;
   private ItemColors itemColors;
   private Framebuffer framebuffer;
   private TextureMap textureMapBlocks;
   private SoundHandler soundHandler;
   private MusicTicker musicTicker;
   private ResourceLocation mojangLogo;
   private final MinecraftSessionService sessionService;
   private SkinManager skinManager;
   private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
   private final Thread thread = Thread.currentThread();
   private ModelManager modelManager;
   private BlockRendererDispatcher blockRenderDispatcher;
   private final GuiToast toastGui;
   volatile boolean running = true;
   public String debug = "";
   public boolean renderChunksMany = true;
   private long debugUpdateTime = getSystemTime();
   private int fpsCounter;
   private boolean actionKeyF3;
   private final Tutorial tutorial;
   long prevFrameTime = -1L;
   private String debugProfilerName = "root";

   public Minecraft(GameConfiguration var1) {
      instance = this;
      this.gameDir = ☃.folderInfo.gameDir;
      this.fileAssets = ☃.folderInfo.assetsDir;
      this.fileResourcepacks = ☃.folderInfo.resourcePacksDir;
      this.launchedVersion = ☃.gameInfo.version;
      this.versionType = ☃.gameInfo.versionType;
      this.twitchDetails = ☃.userInfo.userProperties;
      this.profileProperties = ☃.userInfo.profileProperties;
      this.defaultResourcePack = new DefaultResourcePack(☃.folderInfo.getAssetsIndex());
      this.proxy = ☃.userInfo.proxy == null ? Proxy.NO_PROXY : ☃.userInfo.proxy;
      this.sessionService = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
      this.session = ☃.userInfo.session;
      LOGGER.info("Setting user: {}", this.session.getUsername());
      LOGGER.debug("(Session ID is {})", this.session.getSessionID());
      this.isDemo = ☃.gameInfo.isDemo;
      this.displayWidth = ☃.displayInfo.width > 0 ? ☃.displayInfo.width : 1;
      this.displayHeight = ☃.displayInfo.height > 0 ? ☃.displayInfo.height : 1;
      this.tempDisplayWidth = ☃.displayInfo.width;
      this.tempDisplayHeight = ☃.displayInfo.height;
      this.fullscreen = ☃.displayInfo.fullscreen;
      this.jvm64bit = isJvm64bit();
      this.integratedServer = null;
      if (☃.serverInfo.serverName != null) {
         this.serverName = ☃.serverInfo.serverName;
         this.serverPort = ☃.serverInfo.serverPort;
      }

      ImageIO.setUseCache(false);
      Locale.setDefault(Locale.ROOT);
      Bootstrap.register();
      TextComponentKeybind.displaySupplierFunction = KeyBinding::getDisplayString;
      this.dataFixer = DataFixesManager.createFixer();
      this.toastGui = new GuiToast(this);
      this.tutorial = new Tutorial(this);
   }

   public void run() {
      this.running = true;

      try {
         this.init();
      } catch (Throwable var11) {
         CrashReport ☃ = CrashReport.makeCrashReport(var11, "Initializing game");
         ☃.makeCategory("Initialization");
         this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(☃));
         return;
      }

      try {
         try {
            while (this.running) {
               if (this.hasCrashed && this.crashReporter != null) {
                  this.displayCrashReport(this.crashReporter);
                  return;
               } else {
                  try {
                     this.runGameLoop();
                  } catch (OutOfMemoryError var10) {
                     this.freeMemory();
                     this.displayGuiScreen(new GuiMemoryErrorScreen());
                     System.gc();
                  }
               }
            }

            return;
         } catch (MinecraftError var12) {
         } catch (ReportedException var13) {
            this.addGraphicsAndWorldToCrashReport(var13.getCrashReport());
            this.freeMemory();
            LOGGER.fatal("Reported exception thrown!", var13);
            this.displayCrashReport(var13.getCrashReport());
         } catch (Throwable var14) {
            CrashReport ☃ = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", var14));
            this.freeMemory();
            LOGGER.fatal("Unreported exception thrown!", var14);
            this.displayCrashReport(☃);
         }
      } finally {
         this.shutdownMinecraftApplet();
      }
   }

   private void init() throws LWJGLException {
      this.gameSettings = new GameSettings(this, this.gameDir);
      this.creativeSettings = new CreativeSettings(this, this.gameDir);
      this.defaultResourcePacks.add(this.defaultResourcePack);
      this.startTimerHackThread();
      if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
         this.displayWidth = this.gameSettings.overrideWidth;
         this.displayHeight = this.gameSettings.overrideHeight;
      }

      LOGGER.info("LWJGL Version: {}", Sys.getVersion());
      this.setWindowIcon();
      this.setInitialDisplayMode();
      this.createDisplay();
      OpenGlHelper.initializeTextures();
      this.framebuffer = new Framebuffer(this.displayWidth, this.displayHeight, true);
      this.framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      this.registerMetadataSerializers();
      this.resourcePackRepository = new ResourcePackRepository(
         this.fileResourcepacks, new File(this.gameDir, "server-resource-packs"), this.defaultResourcePack, this.metadataSerializer, this.gameSettings
      );
      this.resourceManager = new SimpleReloadableResourceManager(this.metadataSerializer);
      this.languageManager = new LanguageManager(this.metadataSerializer, this.gameSettings.language);
      this.resourceManager.registerReloadListener(this.languageManager);
      this.refreshResources();
      this.renderEngine = new TextureManager(this.resourceManager);
      this.resourceManager.registerReloadListener(this.renderEngine);
      this.drawSplashScreen(this.renderEngine);
      this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
      this.saveLoader = new AnvilSaveConverter(new File(this.gameDir, "saves"), this.dataFixer);
      this.soundHandler = new SoundHandler(this.resourceManager, this.gameSettings);
      this.resourceManager.registerReloadListener(this.soundHandler);
      this.musicTicker = new MusicTicker(this);
      this.fontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
      if (this.gameSettings.language != null) {
         this.fontRenderer.setUnicodeFlag(this.isUnicode());
         this.fontRenderer.setBidiFlag(this.languageManager.isCurrentLanguageBidirectional());
      }

      this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
      this.resourceManager.registerReloadListener(this.fontRenderer);
      this.resourceManager.registerReloadListener(this.standardGalacticFontRenderer);
      this.resourceManager.registerReloadListener(new GrassColorReloadListener());
      this.resourceManager.registerReloadListener(new FoliageColorReloadListener());
      this.mouseHelper = new MouseHelper();
      this.checkGLError("Pre startup");
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7425);
      GlStateManager.clearDepth(1.0);
      GlStateManager.enableDepth();
      GlStateManager.depthFunc(515);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.cullFace(GlStateManager.CullFace.BACK);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.matrixMode(5888);
      this.checkGLError("Startup");
      this.textureMapBlocks = new TextureMap("textures");
      this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
      this.renderEngine.loadTickableTexture(TextureMap.LOCATION_BLOCKS_TEXTURE, this.textureMapBlocks);
      this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
      this.modelManager = new ModelManager(this.textureMapBlocks);
      this.resourceManager.registerReloadListener(this.modelManager);
      this.blockColors = BlockColors.init();
      this.itemColors = ItemColors.init(this.blockColors);
      this.renderItem = new RenderItem(this.renderEngine, this.modelManager, this.itemColors);
      this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
      this.itemRenderer = new ItemRenderer(this);
      this.resourceManager.registerReloadListener(this.renderItem);
      this.entityRenderer = new EntityRenderer(this, this.resourceManager);
      this.resourceManager.registerReloadListener(this.entityRenderer);
      this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.blockColors);
      this.resourceManager.registerReloadListener(this.blockRenderDispatcher);
      this.renderGlobal = new RenderGlobal(this);
      this.resourceManager.registerReloadListener(this.renderGlobal);
      this.populateSearchTreeManager();
      this.resourceManager.registerReloadListener(this.searchTreeManager);
      GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
      this.effectRenderer = new ParticleManager(this.world, this.renderEngine);
      this.checkGLError("Post startup");
      this.ingameGUI = new GuiIngame(this);
      if (this.serverName != null) {
         this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
      } else {
         this.displayGuiScreen(new GuiMainMenu());
      }

      this.renderEngine.deleteTexture(this.mojangLogo);
      this.mojangLogo = null;
      this.loadingScreen = new LoadingScreenRenderer(this);
      this.debugRenderer = new DebugRenderer(this);
      if (this.gameSettings.fullScreen && !this.fullscreen) {
         this.toggleFullscreen();
      }

      try {
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
      } catch (OpenGLException var2) {
         this.gameSettings.enableVsync = false;
         this.gameSettings.saveOptions();
      }

      this.renderGlobal.makeEntityOutlineShader();
   }

   private void populateSearchTreeManager() {
      SearchTree<ItemStack> ☃ = new SearchTree<>(
         var0 -> var0.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL)
            .stream()
            .map(TextFormatting::getTextWithoutFormattingCodes)
            .map(String::trim)
            .filter(var0x -> !var0x.isEmpty())
            .collect(Collectors.toList()),
         var0 -> Collections.singleton(Item.REGISTRY.getNameForObject(var0.getItem()))
      );
      NonNullList<ItemStack> ☃x = NonNullList.create();

      for (Item ☃xx : Item.REGISTRY) {
         ☃xx.getSubItems(CreativeTabs.SEARCH, ☃x);
      }

      ☃x.forEach(☃::add);
      SearchTree<RecipeList> ☃xx = new SearchTree<>(
         var0 -> var0.getRecipes()
            .stream()
            .flatMap(var0x -> var0x.getRecipeOutput().getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL).stream())
            .map(TextFormatting::getTextWithoutFormattingCodes)
            .map(String::trim)
            .filter(var0x -> !var0x.isEmpty())
            .collect(Collectors.toList()),
         var0 -> var0.getRecipes().stream().map(var0x -> Item.REGISTRY.getNameForObject(var0x.getRecipeOutput().getItem())).collect(Collectors.toList())
      );
      RecipeBookClient.ALL_RECIPES.forEach(☃xx::add);
      this.searchTreeManager.register(SearchTreeManager.ITEMS, ☃);
      this.searchTreeManager.register(SearchTreeManager.RECIPES, ☃xx);
   }

   private void registerMetadataSerializers() {
      this.metadataSerializer.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
      this.metadataSerializer.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
      this.metadataSerializer.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
      this.metadataSerializer.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
      this.metadataSerializer.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
   }

   private void createDisplay() throws LWJGLException {
      Display.setResizable(true);
      Display.setTitle("Minecraft 1.12.2");

      try {
         Display.create(new PixelFormat().withDepthBits(24));
      } catch (LWJGLException var4) {
         LOGGER.error("Couldn't set pixel format", var4);

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var3) {
         }

         if (this.fullscreen) {
            this.updateDisplayMode();
         }

         Display.create();
      }
   }

   private void setInitialDisplayMode() throws LWJGLException {
      if (this.fullscreen) {
         Display.setFullscreen(true);
         DisplayMode ☃ = Display.getDisplayMode();
         this.displayWidth = Math.max(1, ☃.getWidth());
         this.displayHeight = Math.max(1, ☃.getHeight());
      } else {
         Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
      }
   }

   private void setWindowIcon() {
      Util.EnumOS ☃ = Util.getOSType();
      if (☃ != Util.EnumOS.OSX) {
         InputStream ☃x = null;
         InputStream ☃xx = null;

         try {
            ☃x = this.defaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
            ☃xx = this.defaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
            if (☃x != null && ☃xx != null) {
               Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(☃x), this.readImageToBuffer(☃xx)});
            }
         } catch (IOException var8) {
            LOGGER.error("Couldn't set icon", var8);
         } finally {
            IOUtils.closeQuietly(☃x);
            IOUtils.closeQuietly(☃xx);
         }
      }
   }

   private static boolean isJvm64bit() {
      String[] ☃ = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

      for (String ☃x : ☃) {
         String ☃xx = System.getProperty(☃x);
         if (☃xx != null && ☃xx.contains("64")) {
            return true;
         }
      }

      return false;
   }

   public Framebuffer getFramebuffer() {
      return this.framebuffer;
   }

   public String getVersion() {
      return this.launchedVersion;
   }

   public String getVersionType() {
      return this.versionType;
   }

   private void startTimerHackThread() {
      Thread ☃ = new Thread("Timer hack thread") {
         @Override
         public void run() {
            while (Minecraft.this.running) {
               try {
                  Thread.sleep(2147483647L);
               } catch (InterruptedException var2) {
               }
            }
         }
      };
      ☃.setDaemon(true);
      ☃.start();
   }

   public void crashed(CrashReport var1) {
      this.hasCrashed = true;
      this.crashReporter = ☃;
   }

   public void displayCrashReport(CrashReport var1) {
      File ☃ = new File(getMinecraft().gameDir, "crash-reports");
      File ☃x = new File(☃, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
      Bootstrap.printToSYSOUT(☃.getCompleteReport());
      if (☃.getFile() != null) {
         Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + ☃.getFile());
         System.exit(-1);
      } else if (☃.saveToFile(☃x)) {
         Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + ☃x.getAbsolutePath());
         System.exit(-1);
      } else {
         Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
         System.exit(-2);
      }
   }

   public boolean isUnicode() {
      return this.languageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
   }

   public void refreshResources() {
      List<IResourcePack> ☃ = Lists.newArrayList(this.defaultResourcePacks);
      if (this.integratedServer != null) {
         this.integratedServer.reload();
      }

      for (ResourcePackRepository.Entry ☃x : this.resourcePackRepository.getRepositoryEntries()) {
         ☃.add(☃x.getResourcePack());
      }

      if (this.resourcePackRepository.getServerResourcePack() != null) {
         ☃.add(this.resourcePackRepository.getServerResourcePack());
      }

      try {
         this.resourceManager.reloadResources(☃);
      } catch (RuntimeException var4) {
         LOGGER.info("Caught error stitching, removing all assigned resourcepacks", var4);
         ☃.clear();
         ☃.addAll(this.defaultResourcePacks);
         this.resourcePackRepository.setRepositories(Collections.emptyList());
         this.resourceManager.reloadResources(☃);
         this.gameSettings.resourcePacks.clear();
         this.gameSettings.incompatibleResourcePacks.clear();
         this.gameSettings.saveOptions();
      }

      this.languageManager.parseLanguageMetadata(☃);
      if (this.renderGlobal != null) {
         this.renderGlobal.loadRenderers();
      }
   }

   private ByteBuffer readImageToBuffer(InputStream var1) throws IOException {
      BufferedImage ☃ = ImageIO.read(☃);
      int[] ☃x = ☃.getRGB(0, 0, ☃.getWidth(), ☃.getHeight(), null, 0, ☃.getWidth());
      ByteBuffer ☃xx = ByteBuffer.allocate(4 * ☃x.length);

      for (int ☃xxx : ☃x) {
         ☃xx.putInt(☃xxx << 8 | ☃xxx >> 24 & 0xFF);
      }

      ((Buffer)☃xx).flip();
      return ☃xx;
   }

   private void updateDisplayMode() throws LWJGLException {
      Set<DisplayMode> ☃ = Sets.newHashSet();
      Collections.addAll(☃, Display.getAvailableDisplayModes());
      DisplayMode ☃x = Display.getDesktopDisplayMode();
      if (!☃.contains(☃x) && Util.getOSType() == Util.EnumOS.OSX) {
         for (DisplayMode ☃xx : MAC_DISPLAY_MODES) {
            boolean ☃xxx = true;

            for (DisplayMode ☃xxxx : ☃) {
               if (☃xxxx.getBitsPerPixel() == 32 && ☃xxxx.getWidth() == ☃xx.getWidth() && ☃xxxx.getHeight() == ☃xx.getHeight()) {
                  ☃xxx = false;
                  break;
               }
            }

            if (!☃xxx) {
               for (DisplayMode ☃xxxxx : ☃) {
                  if (☃xxxxx.getBitsPerPixel() == 32 && ☃xxxxx.getWidth() == ☃xx.getWidth() / 2 && ☃xxxxx.getHeight() == ☃xx.getHeight() / 2) {
                     ☃x = ☃xxxxx;
                     break;
                  }
               }
            }
         }
      }

      Display.setDisplayMode(☃x);
      this.displayWidth = ☃x.getWidth();
      this.displayHeight = ☃x.getHeight();
   }

   private void drawSplashScreen(TextureManager var1) {
      ScaledResolution ☃ = new ScaledResolution(this);
      int ☃x = ☃.getScaleFactor();
      Framebuffer ☃xx = new Framebuffer(☃.getScaledWidth() * ☃x, ☃.getScaledHeight() * ☃x, true);
      ☃xx.bindFramebuffer(false);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0, ☃.getScaledWidth(), ☃.getScaledHeight(), 0.0, 1000.0, 3000.0);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      GlStateManager.disableDepth();
      GlStateManager.enableTexture2D();
      InputStream ☃xxx = null;

      try {
         ☃xxx = this.defaultResourcePack.getInputStream(LOCATION_MOJANG_PNG);
         this.mojangLogo = ☃.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(☃xxx)));
         ☃.bindTexture(this.mojangLogo);
      } catch (IOException var12) {
         LOGGER.error("Unable to load logo: {}", LOCATION_MOJANG_PNG, var12);
      } finally {
         IOUtils.closeQuietly(☃xxx);
      }

      Tessellator ☃xxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
      ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃xxxxx.pos(0.0, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      ☃xxxxx.pos(this.displayWidth, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      ☃xxxxx.pos(this.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      ☃xxxxx.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      ☃xxxx.draw();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int ☃xxxxxx = 256;
      int ☃xxxxxxx = 256;
      this.draw((☃.getScaledWidth() - 256) / 2, (☃.getScaledHeight() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      ☃xx.unbindFramebuffer();
      ☃xx.framebufferRender(☃.getScaledWidth() * ☃x, ☃.getScaledHeight() * ☃x);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.updateDisplay();
   }

   public void draw(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      BufferBuilder ☃ = Tessellator.getInstance().getBuffer();
      ☃.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      float ☃x = 0.00390625F;
      float ☃xx = 0.00390625F;
      ☃.pos(☃, ☃ + ☃, 0.0).tex(☃ * 0.00390625F, (☃ + ☃) * 0.00390625F).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃ + ☃, ☃ + ☃, 0.0).tex((☃ + ☃) * 0.00390625F, (☃ + ☃) * 0.00390625F).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃ + ☃, ☃, 0.0).tex((☃ + ☃) * 0.00390625F, ☃ * 0.00390625F).color(☃, ☃, ☃, ☃).endVertex();
      ☃.pos(☃, ☃, 0.0).tex(☃ * 0.00390625F, ☃ * 0.00390625F).color(☃, ☃, ☃, ☃).endVertex();
      Tessellator.getInstance().draw();
   }

   public ISaveFormat getSaveLoader() {
      return this.saveLoader;
   }

   public void displayGuiScreen(@Nullable GuiScreen var1) {
      if (this.currentScreen != null) {
         this.currentScreen.onGuiClosed();
      }

      if (☃ == null && this.world == null) {
         ☃ = new GuiMainMenu();
      } else if (☃ == null && this.player.getHealth() <= 0.0F) {
         ☃ = new GuiGameOver(null);
      }

      if (☃ instanceof GuiMainMenu || ☃ instanceof GuiMultiplayer) {
         this.gameSettings.showDebugInfo = false;
         this.ingameGUI.getChatGUI().clearChatMessages(true);
      }

      this.currentScreen = ☃;
      if (☃ != null) {
         this.setIngameNotInFocus();
         KeyBinding.unPressAllKeys();

         while (Mouse.next()) {
         }

         while (Keyboard.next()) {
         }

         ScaledResolution ☃ = new ScaledResolution(this);
         int ☃x = ☃.getScaledWidth();
         int ☃xx = ☃.getScaledHeight();
         ☃.setWorldAndResolution(this, ☃x, ☃xx);
         this.skipRenderWorld = false;
      } else {
         this.soundHandler.resumeSounds();
         this.setIngameFocus();
      }
   }

   private void checkGLError(String var1) {
      int ☃ = GlStateManager.glGetError();
      if (☃ != 0) {
         String ☃x = GLU.gluErrorString(☃);
         LOGGER.error("########## GL ERROR ##########");
         LOGGER.error("@ {}", ☃);
         LOGGER.error("{}: {}", ☃, ☃x);
      }
   }

   public void shutdownMinecraftApplet() {
      try {
         LOGGER.info("Stopping!");

         try {
            this.loadWorld(null);
         } catch (Throwable var5) {
         }

         this.soundHandler.unloadSounds();
      } finally {
         Display.destroy();
         if (!this.hasCrashed) {
            System.exit(0);
         }
      }

      System.gc();
   }

   private void runGameLoop() {
      long ☃ = System.nanoTime();
      this.profiler.startSection("root");
      if (Display.isCreated() && Display.isCloseRequested()) {
         this.shutdown();
      }

      this.timer.updateTimer();
      this.profiler.startSection("scheduledExecutables");
      synchronized (this.scheduledTasks) {
         while (!this.scheduledTasks.isEmpty()) {
            Util.runTask(this.scheduledTasks.poll(), LOGGER);
         }
      }

      this.profiler.endSection();
      long ☃x = System.nanoTime();
      this.profiler.startSection("tick");

      for (int ☃xx = 0; ☃xx < Math.min(10, this.timer.elapsedTicks); ☃xx++) {
         this.runTick();
      }

      this.profiler.endStartSection("preRenderErrors");
      long ☃xx = System.nanoTime() - ☃x;
      this.checkGLError("Pre render");
      this.profiler.endStartSection("sound");
      this.soundHandler.setListener(this.player, this.timer.renderPartialTicks);
      this.profiler.endSection();
      this.profiler.startSection("render");
      GlStateManager.pushMatrix();
      GlStateManager.clear(16640);
      this.framebuffer.bindFramebuffer(true);
      this.profiler.startSection("display");
      GlStateManager.enableTexture2D();
      this.profiler.endSection();
      if (!this.skipRenderWorld) {
         this.profiler.endStartSection("gameRenderer");
         this.entityRenderer.updateCameraAndRender(this.isGamePaused ? this.renderPartialTicksPaused : this.timer.renderPartialTicks, ☃);
         this.profiler.endStartSection("toasts");
         this.toastGui.drawToast(new ScaledResolution(this));
         this.profiler.endSection();
      }

      this.profiler.endSection();
      if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
         if (!this.profiler.profilingEnabled) {
            this.profiler.clearProfiling();
         }

         this.profiler.profilingEnabled = true;
         this.displayDebugInfo(☃xx);
      } else {
         this.profiler.profilingEnabled = false;
         this.prevFrameTime = System.nanoTime();
      }

      this.framebuffer.unbindFramebuffer();
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      this.framebuffer.framebufferRender(this.displayWidth, this.displayHeight);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      this.entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
      GlStateManager.popMatrix();
      this.profiler.startSection("root");
      this.updateDisplay();
      Thread.yield();
      this.checkGLError("Post render");
      this.fpsCounter++;
      boolean ☃xxx = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.integratedServer.getPublic();
      if (this.isGamePaused != ☃xxx) {
         if (this.isGamePaused) {
            this.renderPartialTicksPaused = this.timer.renderPartialTicks;
         } else {
            this.timer.renderPartialTicks = this.renderPartialTicksPaused;
         }

         this.isGamePaused = ☃xxx;
      }

      long ☃xxxx = System.nanoTime();
      this.frameTimer.addFrame(☃xxxx - this.startNanoTime);
      this.startNanoTime = ☃xxxx;

      while (getSystemTime() >= this.debugUpdateTime + 1000L) {
         debugFPS = this.fpsCounter;
         this.debug = String.format(
            "%d fps (%d chunk update%s) T: %s%s%s%s%s",
            debugFPS,
            RenderChunk.renderChunksUpdated,
            RenderChunk.renderChunksUpdated == 1 ? "" : "s",
            this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : this.gameSettings.limitFramerate,
            this.gameSettings.enableVsync ? " vsync" : "",
            this.gameSettings.fancyGraphics ? "" : " fast",
            this.gameSettings.clouds == 0 ? "" : (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds"),
            OpenGlHelper.useVbo() ? " vbo" : ""
         );
         RenderChunk.renderChunksUpdated = 0;
         this.debugUpdateTime += 1000L;
         this.fpsCounter = 0;
         this.usageSnooper.addMemoryStatsToSnooper();
         if (!this.usageSnooper.isSnooperRunning()) {
            this.usageSnooper.startSnooper();
         }
      }

      if (this.isFramerateLimitBelowMax()) {
         this.profiler.startSection("fpslimit_wait");
         Display.sync(this.getLimitFramerate());
         this.profiler.endSection();
      }

      this.profiler.endSection();
   }

   public void updateDisplay() {
      this.profiler.startSection("display_update");
      Display.update();
      this.profiler.endSection();
      this.checkWindowResize();
   }

   protected void checkWindowResize() {
      if (!this.fullscreen && Display.wasResized()) {
         int ☃ = this.displayWidth;
         int ☃x = this.displayHeight;
         this.displayWidth = Display.getWidth();
         this.displayHeight = Display.getHeight();
         if (this.displayWidth != ☃ || this.displayHeight != ☃x) {
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }

            this.resize(this.displayWidth, this.displayHeight);
         }
      }
   }

   public int getLimitFramerate() {
      return this.world == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
   }

   public boolean isFramerateLimitBelowMax() {
      return this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
   }

   public void freeMemory() {
      try {
         memoryReserve = new byte[0];
         this.renderGlobal.deleteAllDisplayLists();
      } catch (Throwable var3) {
      }

      try {
         System.gc();
         this.loadWorld(null);
      } catch (Throwable var2) {
      }

      System.gc();
   }

   private void updateDebugProfilerName(int var1) {
      List<Profiler.Result> ☃ = this.profiler.getProfilingData(this.debugProfilerName);
      if (!☃.isEmpty()) {
         Profiler.Result ☃x = ☃.remove(0);
         if (☃ == 0) {
            if (!☃x.profilerName.isEmpty()) {
               int ☃xx = this.debugProfilerName.lastIndexOf(46);
               if (☃xx >= 0) {
                  this.debugProfilerName = this.debugProfilerName.substring(0, ☃xx);
               }
            }
         } else {
            ☃--;
            if (☃ < ☃.size() && !"unspecified".equals(☃.get(☃).profilerName)) {
               if (!this.debugProfilerName.isEmpty()) {
                  this.debugProfilerName = this.debugProfilerName + ".";
               }

               this.debugProfilerName = this.debugProfilerName + ☃.get(☃).profilerName;
            }
         }
      }
   }

   private void displayDebugInfo(long var1) {
      if (this.profiler.profilingEnabled) {
         List<Profiler.Result> ☃ = this.profiler.getProfilingData(this.debugProfilerName);
         Profiler.Result ☃x = ☃.remove(0);
         GlStateManager.clear(256);
         GlStateManager.matrixMode(5889);
         GlStateManager.enableColorMaterial();
         GlStateManager.loadIdentity();
         GlStateManager.ortho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -2000.0F);
         GlStateManager.glLineWidth(1.0F);
         GlStateManager.disableTexture2D();
         Tessellator ☃xx = Tessellator.getInstance();
         BufferBuilder ☃xxx = ☃xx.getBuffer();
         int ☃xxxx = 160;
         int ☃xxxxx = this.displayWidth - 160 - 10;
         int ☃xxxxxx = this.displayHeight - 320;
         GlStateManager.enableBlend();
         ☃xxx.begin(7, DefaultVertexFormats.POSITION_COLOR);
         ☃xxx.pos(☃xxxxx - 176.0F, ☃xxxxxx - 96.0F - 16.0F, 0.0).color(200, 0, 0, 0).endVertex();
         ☃xxx.pos(☃xxxxx - 176.0F, ☃xxxxxx + 320, 0.0).color(200, 0, 0, 0).endVertex();
         ☃xxx.pos(☃xxxxx + 176.0F, ☃xxxxxx + 320, 0.0).color(200, 0, 0, 0).endVertex();
         ☃xxx.pos(☃xxxxx + 176.0F, ☃xxxxxx - 96.0F - 16.0F, 0.0).color(200, 0, 0, 0).endVertex();
         ☃xx.draw();
         GlStateManager.disableBlend();
         double ☃xxxxxxx = 0.0;

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃.size(); ☃xxxxxxxx++) {
            Profiler.Result ☃xxxxxxxxx = ☃.get(☃xxxxxxxx);
            int ☃xxxxxxxxxx = MathHelper.floor(☃xxxxxxxxx.usePercentage / 4.0) + 1;
            ☃xxx.begin(6, DefaultVertexFormats.POSITION_COLOR);
            int ☃xxxxxxxxxxx = ☃xxxxxxxxx.getColor();
            int ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx >> 16 & 0xFF;
            int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx >> 8 & 0xFF;
            int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxx & 0xFF;
            ☃xxx.pos(☃xxxxx, ☃xxxxxx, 0.0).color(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, 255).endVertex();

            for (int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxx; ☃xxxxxxxxxxxxxxx >= 0; ☃xxxxxxxxxxxxxxx--) {
               float ☃xxxxxxxxxxxxxxxx = (float)((☃xxxxxxx + ☃xxxxxxxxx.usePercentage * ☃xxxxxxxxxxxxxxx / ☃xxxxxxxxxx) * (float) (Math.PI * 2) / 100.0);
               float ☃xxxxxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxxxxxx) * 160.0F;
               float ☃xxxxxxxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxxxxxx) * 160.0F * 0.5F;
               ☃xxx.pos(☃xxxxx + ☃xxxxxxxxxxxxxxxxx, ☃xxxxxx - ☃xxxxxxxxxxxxxxxxxx, 0.0).color(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, 255).endVertex();
            }

            ☃xx.draw();
            ☃xxx.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for (int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxx; ☃xxxxxxxxxxxxxxx >= 0; ☃xxxxxxxxxxxxxxx--) {
               float ☃xxxxxxxxxxxxxxxx = (float)((☃xxxxxxx + ☃xxxxxxxxx.usePercentage * ☃xxxxxxxxxxxxxxx / ☃xxxxxxxxxx) * (float) (Math.PI * 2) / 100.0);
               float ☃xxxxxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxxxxxx) * 160.0F;
               float ☃xxxxxxxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxxxxxx) * 160.0F * 0.5F;
               ☃xxx.pos(☃xxxxx + ☃xxxxxxxxxxxxxxxxx, ☃xxxxxx - ☃xxxxxxxxxxxxxxxxxx, 0.0)
                  .color(☃xxxxxxxxxxxx >> 1, ☃xxxxxxxxxxxxx >> 1, ☃xxxxxxxxxxxxxx >> 1, 255)
                  .endVertex();
               ☃xxx.pos(☃xxxxx + ☃xxxxxxxxxxxxxxxxx, ☃xxxxxx - ☃xxxxxxxxxxxxxxxxxx + 10.0F, 0.0)
                  .color(☃xxxxxxxxxxxx >> 1, ☃xxxxxxxxxxxxx >> 1, ☃xxxxxxxxxxxxxx >> 1, 255)
                  .endVertex();
            }

            ☃xx.draw();
            ☃xxxxxxx += ☃xxxxxxxxx.usePercentage;
         }

         DecimalFormat ☃xxxxxxxx = new DecimalFormat("##0.00");
         GlStateManager.enableTexture2D();
         String ☃xxxxxxxxx = "";
         if (!"unspecified".equals(☃x.profilerName)) {
            ☃xxxxxxxxx = ☃xxxxxxxxx + "[0] ";
         }

         if (☃x.profilerName.isEmpty()) {
            ☃xxxxxxxxx = ☃xxxxxxxxx + "ROOT ";
         } else {
            ☃xxxxxxxxx = ☃xxxxxxxxx + ☃x.profilerName + ' ';
         }

         int ☃xxxxxxxxxx = 16777215;
         this.fontRenderer.drawStringWithShadow(☃xxxxxxxxx, ☃xxxxx - 160, ☃xxxxxx - 80 - 16, 16777215);
         ☃xxxxxxxxx = ☃xxxxxxxx.format(☃x.totalUsePercentage) + "%";
         this.fontRenderer.drawStringWithShadow(☃xxxxxxxxx, ☃xxxxx + 160 - this.fontRenderer.getStringWidth(☃xxxxxxxxx), ☃xxxxxx - 80 - 16, 16777215);

         for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃.size(); ☃xxxxxxxxxxx++) {
            Profiler.Result ☃xxxxxxxxxxxx = ☃.get(☃xxxxxxxxxxx);
            StringBuilder ☃xxxxxxxxxxxxx = new StringBuilder();
            if ("unspecified".equals(☃xxxxxxxxxxxx.profilerName)) {
               ☃xxxxxxxxxxxxx.append("[?] ");
            } else {
               ☃xxxxxxxxxxxxx.append("[").append(☃xxxxxxxxxxx + 1).append("] ");
            }

            String ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.append(☃xxxxxxxxxxxx.profilerName).toString();
            this.fontRenderer.drawStringWithShadow(☃xxxxxxxxxxxxxx, ☃xxxxx - 160, ☃xxxxxx + 80 + ☃xxxxxxxxxxx * 8 + 20, ☃xxxxxxxxxxxx.getColor());
            ☃xxxxxxxxxxxxxx = ☃xxxxxxxx.format(☃xxxxxxxxxxxx.usePercentage) + "%";
            this.fontRenderer
               .drawStringWithShadow(
                  ☃xxxxxxxxxxxxxx,
                  ☃xxxxx + 160 - 50 - this.fontRenderer.getStringWidth(☃xxxxxxxxxxxxxx),
                  ☃xxxxxx + 80 + ☃xxxxxxxxxxx * 8 + 20,
                  ☃xxxxxxxxxxxx.getColor()
               );
            ☃xxxxxxxxxxxxxx = ☃xxxxxxxx.format(☃xxxxxxxxxxxx.totalUsePercentage) + "%";
            this.fontRenderer
               .drawStringWithShadow(
                  ☃xxxxxxxxxxxxxx,
                  ☃xxxxx + 160 - this.fontRenderer.getStringWidth(☃xxxxxxxxxxxxxx),
                  ☃xxxxxx + 80 + ☃xxxxxxxxxxx * 8 + 20,
                  ☃xxxxxxxxxxxx.getColor()
               );
         }
      }
   }

   public void shutdown() {
      this.running = false;
   }

   public void setIngameFocus() {
      if (Display.isActive()) {
         if (!this.inGameHasFocus) {
            if (!IS_RUNNING_ON_MAC) {
               KeyBinding.updateKeyBindState();
            }

            this.inGameHasFocus = true;
            this.mouseHelper.grabMouseCursor();
            this.displayGuiScreen(null);
            this.leftClickCounter = 10000;
         }
      }
   }

   public void setIngameNotInFocus() {
      if (this.inGameHasFocus) {
         this.inGameHasFocus = false;
         this.mouseHelper.ungrabMouseCursor();
      }
   }

   public void displayInGameMenu() {
      if (this.currentScreen == null) {
         this.displayGuiScreen(new GuiIngameMenu());
         if (this.isSingleplayer() && !this.integratedServer.getPublic()) {
            this.soundHandler.pauseSounds();
         }
      }
   }

   private void sendClickBlockToController(boolean var1) {
      if (!☃) {
         this.leftClickCounter = 0;
      }

      if (this.leftClickCounter <= 0 && !this.player.isHandActive()) {
         if (☃ && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos ☃ = this.objectMouseOver.getBlockPos();
            if (this.world.getBlockState(☃).getMaterial() != Material.AIR && this.playerController.onPlayerDamageBlock(☃, this.objectMouseOver.sideHit)) {
               this.effectRenderer.addBlockHitEffects(☃, this.objectMouseOver.sideHit);
               this.player.swingArm(EnumHand.MAIN_HAND);
            }
         } else {
            this.playerController.resetBlockRemoving();
         }
      }
   }

   private void clickMouse() {
      if (this.leftClickCounter <= 0) {
         if (this.objectMouseOver == null) {
            LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
            if (this.playerController.isNotCreative()) {
               this.leftClickCounter = 10;
            }
         } else if (!this.player.isRowingBoat()) {
            switch (this.objectMouseOver.typeOfHit) {
               case ENTITY:
                  this.playerController.attackEntity(this.player, this.objectMouseOver.entityHit);
                  break;
               case BLOCK:
                  BlockPos ☃ = this.objectMouseOver.getBlockPos();
                  if (this.world.getBlockState(☃).getMaterial() != Material.AIR) {
                     this.playerController.clickBlock(☃, this.objectMouseOver.sideHit);
                     break;
                  }
               case MISS:
                  if (this.playerController.isNotCreative()) {
                     this.leftClickCounter = 10;
                  }

                  this.player.resetCooldown();
            }

            this.player.swingArm(EnumHand.MAIN_HAND);
         }
      }
   }

   private void rightClickMouse() {
      if (!this.playerController.getIsHittingBlock()) {
         this.rightClickDelayTimer = 4;
         if (!this.player.isRowingBoat()) {
            if (this.objectMouseOver == null) {
               LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
            }

            for (EnumHand ☃ : EnumHand.values()) {
               ItemStack ☃x = this.player.getHeldItem(☃);
               if (this.objectMouseOver != null) {
                  switch (this.objectMouseOver.typeOfHit) {
                     case ENTITY:
                        if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.entityHit, this.objectMouseOver, ☃)
                           == EnumActionResult.SUCCESS) {
                           return;
                        }

                        if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.entityHit, ☃) == EnumActionResult.SUCCESS) {
                           return;
                        }
                        break;
                     case BLOCK:
                        BlockPos ☃xx = this.objectMouseOver.getBlockPos();
                        if (this.world.getBlockState(☃xx).getMaterial() != Material.AIR) {
                           int ☃xxx = ☃x.getCount();
                           EnumActionResult ☃xxxx = this.playerController
                              .processRightClickBlock(this.player, this.world, ☃xx, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec, ☃);
                           if (☃xxxx == EnumActionResult.SUCCESS) {
                              this.player.swingArm(☃);
                              if (!☃x.isEmpty() && (☃x.getCount() != ☃xxx || this.playerController.isInCreativeMode())) {
                                 this.entityRenderer.itemRenderer.resetEquippedProgress(☃);
                              }

                              return;
                           }
                        }
                  }
               }

               if (!☃x.isEmpty() && this.playerController.processRightClick(this.player, this.world, ☃) == EnumActionResult.SUCCESS) {
                  this.entityRenderer.itemRenderer.resetEquippedProgress(☃);
                  return;
               }
            }
         }
      }
   }

   public void toggleFullscreen() {
      try {
         this.fullscreen = !this.fullscreen;
         this.gameSettings.fullScreen = this.fullscreen;
         if (this.fullscreen) {
            this.updateDisplayMode();
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         } else {
            Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
            this.displayWidth = this.tempDisplayWidth;
            this.displayHeight = this.tempDisplayHeight;
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         }

         if (this.currentScreen != null) {
            this.resize(this.displayWidth, this.displayHeight);
         } else {
            this.updateFramebufferSize();
         }

         Display.setFullscreen(this.fullscreen);
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
         this.updateDisplay();
      } catch (Exception var2) {
         LOGGER.error("Couldn't toggle fullscreen", var2);
      }
   }

   private void resize(int var1, int var2) {
      this.displayWidth = Math.max(1, ☃);
      this.displayHeight = Math.max(1, ☃);
      if (this.currentScreen != null) {
         ScaledResolution ☃ = new ScaledResolution(this);
         this.currentScreen.onResize(this, ☃.getScaledWidth(), ☃.getScaledHeight());
      }

      this.loadingScreen = new LoadingScreenRenderer(this);
      this.updateFramebufferSize();
   }

   private void updateFramebufferSize() {
      this.framebuffer.createBindFramebuffer(this.displayWidth, this.displayHeight);
      if (this.entityRenderer != null) {
         this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
      }
   }

   public MusicTicker getMusicTicker() {
      return this.musicTicker;
   }

   public void runTick() {
      if (this.rightClickDelayTimer > 0) {
         this.rightClickDelayTimer--;
      }

      this.profiler.startSection("gui");
      if (!this.isGamePaused) {
         this.ingameGUI.updateTick();
      }

      this.profiler.endSection();
      this.entityRenderer.getMouseOver(1.0F);
      this.tutorial.onMouseHover(this.world, this.objectMouseOver);
      this.profiler.startSection("gameMode");
      if (!this.isGamePaused && this.world != null) {
         this.playerController.updateController();
      }

      this.profiler.endStartSection("textures");
      if (this.world != null) {
         this.renderEngine.tick();
      }

      if (this.currentScreen == null && this.player != null) {
         if (this.player.getHealth() <= 0.0F && !(this.currentScreen instanceof GuiGameOver)) {
            this.displayGuiScreen(null);
         } else if (this.player.isPlayerSleeping() && this.world != null) {
            this.displayGuiScreen(new GuiSleepMP());
         }
      } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.player.isPlayerSleeping()) {
         this.displayGuiScreen(null);
      }

      if (this.currentScreen != null) {
         this.leftClickCounter = 10000;
      }

      if (this.currentScreen != null) {
         try {
            this.currentScreen.handleInput();
         } catch (Throwable var5) {
            CrashReport ☃ = CrashReport.makeCrashReport(var5, "Updating screen events");
            CrashReportCategory ☃x = ☃.makeCategory("Affected screen");
            ☃x.addDetail("Screen name", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return Minecraft.this.currentScreen.getClass().getCanonicalName();
               }
            });
            throw new ReportedException(☃);
         }

         if (this.currentScreen != null) {
            try {
               this.currentScreen.updateScreen();
            } catch (Throwable var4) {
               CrashReport ☃ = CrashReport.makeCrashReport(var4, "Ticking screen");
               CrashReportCategory ☃x = ☃.makeCategory("Affected screen");
               ☃x.addDetail("Screen name", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     return Minecraft.this.currentScreen.getClass().getCanonicalName();
                  }
               });
               throw new ReportedException(☃);
            }
         }
      }

      if (this.currentScreen == null || this.currentScreen.allowUserInput) {
         this.profiler.endStartSection("mouse");
         this.runTickMouse();
         if (this.leftClickCounter > 0) {
            this.leftClickCounter--;
         }

         this.profiler.endStartSection("keyboard");
         this.runTickKeyboard();
      }

      if (this.world != null) {
         if (this.player != null) {
            this.joinPlayerCounter++;
            if (this.joinPlayerCounter == 30) {
               this.joinPlayerCounter = 0;
               this.world.joinEntityInSurroundings(this.player);
            }
         }

         this.profiler.endStartSection("gameRenderer");
         if (!this.isGamePaused) {
            this.entityRenderer.updateRenderer();
         }

         this.profiler.endStartSection("levelRenderer");
         if (!this.isGamePaused) {
            this.renderGlobal.updateClouds();
         }

         this.profiler.endStartSection("level");
         if (!this.isGamePaused) {
            if (this.world.getLastLightningBolt() > 0) {
               this.world.setLastLightningBolt(this.world.getLastLightningBolt() - 1);
            }

            this.world.updateEntities();
         }
      } else if (this.entityRenderer.isShaderActive()) {
         this.entityRenderer.stopUseShader();
      }

      if (!this.isGamePaused) {
         this.musicTicker.update();
         this.soundHandler.update();
      }

      if (this.world != null) {
         if (!this.isGamePaused) {
            this.world.setAllowedSpawnTypes(this.world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
            this.tutorial.update();

            try {
               this.world.tick();
            } catch (Throwable var6) {
               CrashReport ☃ = CrashReport.makeCrashReport(var6, "Exception in world tick");
               if (this.world == null) {
                  CrashReportCategory ☃x = ☃.makeCategory("Affected level");
                  ☃x.addCrashSection("Problem", "Level is null!");
               } else {
                  this.world.addWorldInfoToCrashReport(☃);
               }

               throw new ReportedException(☃);
            }
         }

         this.profiler.endStartSection("animateTick");
         if (!this.isGamePaused && this.world != null) {
            this.world.doVoidFogParticles(MathHelper.floor(this.player.posX), MathHelper.floor(this.player.posY), MathHelper.floor(this.player.posZ));
         }

         this.profiler.endStartSection("particles");
         if (!this.isGamePaused) {
            this.effectRenderer.updateEffects();
         }
      } else if (this.networkManager != null) {
         this.profiler.endStartSection("pendingConnection");
         this.networkManager.processReceivedPackets();
      }

      this.profiler.endSection();
      this.systemTime = getSystemTime();
   }

   private void runTickKeyboard() {
      while (Keyboard.next()) {
         int ☃ = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
         if (this.debugCrashKeyPressTime > 0L) {
            if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
               throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
            }

            if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
               this.debugCrashKeyPressTime = -1L;
            }
         } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
            this.actionKeyF3 = true;
            this.debugCrashKeyPressTime = getSystemTime();
         }

         this.dispatchKeypresses();
         if (this.currentScreen != null) {
            this.currentScreen.handleKeyboardInput();
         }

         boolean ☃x = Keyboard.getEventKeyState();
         if (☃x) {
            if (☃ == 62 && this.entityRenderer != null) {
               this.entityRenderer.switchUseShader();
            }

            boolean ☃xx = false;
            if (this.currentScreen == null) {
               if (☃ == 1) {
                  this.displayInGameMenu();
               }

               ☃xx = Keyboard.isKeyDown(61) && this.processKeyF3(☃);
               this.actionKeyF3 |= ☃xx;
               if (☃ == 59) {
                  this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
               }
            }

            if (☃xx) {
               KeyBinding.setKeyBindState(☃, false);
            } else {
               KeyBinding.setKeyBindState(☃, true);
               KeyBinding.onTick(☃);
            }

            if (this.gameSettings.showDebugProfilerChart) {
               if (☃ == 11) {
                  this.updateDebugProfilerName(0);
               }

               for (int ☃xxx = 0; ☃xxx < 9; ☃xxx++) {
                  if (☃ == 2 + ☃xxx) {
                     this.updateDebugProfilerName(☃xxx + 1);
                  }
               }
            }
         } else {
            KeyBinding.setKeyBindState(☃, false);
            if (☃ == 61) {
               if (this.actionKeyF3) {
                  this.actionKeyF3 = false;
               } else {
                  this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                  this.gameSettings.showDebugProfilerChart = this.gameSettings.showDebugInfo && GuiScreen.isShiftKeyDown();
                  this.gameSettings.showLagometer = this.gameSettings.showDebugInfo && GuiScreen.isAltKeyDown();
               }
            }
         }
      }

      this.processKeyBinds();
   }

   private boolean processKeyF3(int var1) {
      if (☃ == 30) {
         this.renderGlobal.loadRenderers();
         this.debugFeedbackTranslated("debug.reload_chunks.message");
         return true;
      } else if (☃ == 48) {
         boolean ☃ = !this.renderManager.isDebugBoundingBox();
         this.renderManager.setDebugBoundingBox(☃);
         this.debugFeedbackTranslated(☃ ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
         return true;
      } else if (☃ == 32) {
         if (this.ingameGUI != null) {
            this.ingameGUI.getChatGUI().clearChatMessages(false);
         }

         return true;
      } else if (☃ == 33) {
         this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
         this.debugFeedbackTranslated("debug.cycle_renderdistance.message", this.gameSettings.renderDistanceChunks);
         return true;
      } else if (☃ == 34) {
         boolean ☃ = this.debugRenderer.toggleChunkBorders();
         this.debugFeedbackTranslated(☃ ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
         return true;
      } else if (☃ == 35) {
         this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
         this.debugFeedbackTranslated(this.gameSettings.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
         this.gameSettings.saveOptions();
         return true;
      } else if (☃ == 49) {
         if (!this.player.canUseCommand(2, "")) {
            this.debugFeedbackTranslated("debug.creative_spectator.error");
         } else if (this.player.isCreative()) {
            this.player.sendChatMessage("/gamemode spectator");
         } else if (this.player.isSpectator()) {
            this.player.sendChatMessage("/gamemode creative");
         }

         return true;
      } else if (☃ == 25) {
         this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
         this.gameSettings.saveOptions();
         this.debugFeedbackTranslated(this.gameSettings.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
         return true;
      } else if (☃ == 16) {
         this.debugFeedbackTranslated("debug.help.message");
         GuiNewChat ☃ = this.ingameGUI.getChatGUI();
         ☃.printChatMessage(new TextComponentTranslation("debug.reload_chunks.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.show_hitboxes.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.clear_chat.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.cycle_renderdistance.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.chunk_boundaries.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.advanced_tooltips.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.creative_spectator.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.pause_focus.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.help.help"));
         ☃.printChatMessage(new TextComponentTranslation("debug.reload_resourcepacks.help"));
         return true;
      } else if (☃ == 20) {
         this.debugFeedbackTranslated("debug.reload_resourcepacks.message");
         this.refreshResources();
         return true;
      } else {
         return false;
      }
   }

   private void processKeyBinds() {
      while (this.gameSettings.keyBindTogglePerspective.isPressed()) {
         this.gameSettings.thirdPersonView++;
         if (this.gameSettings.thirdPersonView > 2) {
            this.gameSettings.thirdPersonView = 0;
         }

         if (this.gameSettings.thirdPersonView == 0) {
            this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
         } else if (this.gameSettings.thirdPersonView == 1) {
            this.entityRenderer.loadEntityShader(null);
         }

         this.renderGlobal.setDisplayListEntitiesDirty();
      }

      while (this.gameSettings.keyBindSmoothCamera.isPressed()) {
         this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         boolean ☃x = this.gameSettings.keyBindSaveToolbar.isKeyDown();
         boolean ☃xx = this.gameSettings.keyBindLoadToolbar.isKeyDown();
         if (this.gameSettings.keyBindsHotbar[☃].isPressed()) {
            if (this.player.isSpectator()) {
               this.ingameGUI.getSpectatorGui().onHotbarSelected(☃);
            } else if (!this.player.isCreative() || this.currentScreen != null || !☃xx && !☃x) {
               this.player.inventory.currentItem = ☃;
            } else {
               GuiContainerCreative.handleHotbarSnapshots(this, ☃, ☃xx, ☃x);
            }
         }
      }

      while (this.gameSettings.keyBindInventory.isPressed()) {
         if (this.playerController.isRidingHorse()) {
            this.player.sendHorseInventory();
         } else {
            this.tutorial.openInventory();
            this.displayGuiScreen(new GuiInventory(this.player));
         }
      }

      while (this.gameSettings.keyBindAdvancements.isPressed()) {
         this.displayGuiScreen(new GuiScreenAdvancements(this.player.connection.getAdvancementManager()));
      }

      while (this.gameSettings.keyBindSwapHands.isPressed()) {
         if (!this.player.isSpectator()) {
            this.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
         }
      }

      while (this.gameSettings.keyBindDrop.isPressed()) {
         if (!this.player.isSpectator()) {
            this.player.dropItem(GuiScreen.isCtrlKeyDown());
         }
      }

      boolean ☃x = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;
      if (☃x) {
         while (this.gameSettings.keyBindChat.isPressed()) {
            this.displayGuiScreen(new GuiChat());
         }

         if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed()) {
            this.displayGuiScreen(new GuiChat("/"));
         }
      }

      if (this.player.isHandActive()) {
         if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
            this.playerController.onStoppedUsingItem(this.player);
         }

         while (this.gameSettings.keyBindAttack.isPressed()) {
         }

         while (this.gameSettings.keyBindUseItem.isPressed()) {
         }

         while (this.gameSettings.keyBindPickBlock.isPressed()) {
         }
      } else {
         while (this.gameSettings.keyBindAttack.isPressed()) {
            this.clickMouse();
         }

         while (this.gameSettings.keyBindUseItem.isPressed()) {
            this.rightClickMouse();
         }

         while (this.gameSettings.keyBindPickBlock.isPressed()) {
            this.middleClickMouse();
         }
      }

      if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !this.player.isHandActive()) {
         this.rightClickMouse();
      }

      this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
   }

   private void runTickMouse() {
      while (Mouse.next()) {
         int ☃ = Mouse.getEventButton();
         KeyBinding.setKeyBindState(☃ - 100, Mouse.getEventButtonState());
         if (Mouse.getEventButtonState()) {
            if (this.player.isSpectator() && ☃ == 2) {
               this.ingameGUI.getSpectatorGui().onMiddleClick();
            } else {
               KeyBinding.onTick(☃ - 100);
            }
         }

         long ☃x = getSystemTime() - this.systemTime;
         if (☃x <= 200L) {
            int ☃xx = Mouse.getEventDWheel();
            if (☃xx != 0) {
               if (this.player.isSpectator()) {
                  ☃xx = ☃xx < 0 ? -1 : 1;
                  if (this.ingameGUI.getSpectatorGui().isMenuActive()) {
                     this.ingameGUI.getSpectatorGui().onMouseScroll(-☃xx);
                  } else {
                     float ☃xxx = MathHelper.clamp(this.player.capabilities.getFlySpeed() + ☃xx * 0.005F, 0.0F, 0.2F);
                     this.player.capabilities.setFlySpeed(☃xxx);
                  }
               } else {
                  this.player.inventory.changeCurrentItem(☃xx);
               }
            }

            if (this.currentScreen == null) {
               if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                  this.setIngameFocus();
               }
            } else if (this.currentScreen != null) {
               this.currentScreen.handleMouseInput();
            }
         }
      }
   }

   private void debugFeedbackTranslated(String var1, Object... var2) {
      this.ingameGUI
         .getChatGUI()
         .printChatMessage(
            new TextComponentString("")
               .appendSibling(new TextComponentTranslation("debug.prefix").setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true)))
               .appendText(" ")
               .appendSibling(new TextComponentTranslation(☃, ☃))
         );
   }

   public void launchIntegratedServer(String var1, String var2, @Nullable WorldSettings var3) {
      this.loadWorld(null);
      System.gc();
      ISaveHandler ☃ = this.saveLoader.getSaveLoader(☃, false);
      WorldInfo ☃x = ☃.loadWorldInfo();
      if (☃x == null && ☃ != null) {
         ☃x = new WorldInfo(☃, ☃);
         ☃.saveWorldInfo(☃x);
      }

      if (☃ == null) {
         ☃ = new WorldSettings(☃x);
      }

      try {
         YggdrasilAuthenticationService ☃xx = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
         MinecraftSessionService ☃xxx = ☃xx.createMinecraftSessionService();
         GameProfileRepository ☃xxxx = ☃xx.createProfileRepository();
         PlayerProfileCache ☃xxxxx = new PlayerProfileCache(☃xxxx, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
         TileEntitySkull.setProfileCache(☃xxxxx);
         TileEntitySkull.setSessionService(☃xxx);
         PlayerProfileCache.setOnlineMode(false);
         this.integratedServer = new IntegratedServer(this, ☃, ☃, ☃, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
         this.integratedServer.startServerThread();
         this.integratedServerIsRunning = true;
      } catch (Throwable var11) {
         CrashReport ☃xxxxxx = CrashReport.makeCrashReport(var11, "Starting integrated server");
         CrashReportCategory ☃xxxxxxx = ☃xxxxxx.makeCategory("Starting integrated server");
         ☃xxxxxxx.addCrashSection("Level ID", ☃);
         ☃xxxxxxx.addCrashSection("Level Name", ☃);
         throw new ReportedException(☃xxxxxx);
      }

      this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel"));

      while (!this.integratedServer.serverIsInRunLoop()) {
         String ☃xx = this.integratedServer.getUserMessage();
         if (☃xx != null) {
            this.loadingScreen.displayLoadingString(I18n.format(☃xx));
         } else {
            this.loadingScreen.displayLoadingString("");
         }

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var10) {
         }
      }

      this.displayGuiScreen(new GuiScreenWorking());
      SocketAddress ☃xx = this.integratedServer.getNetworkSystem().addLocalEndpoint();
      NetworkManager ☃xxx = NetworkManager.provideLocalClient(☃xx);
      ☃xxx.setNetHandler(new NetHandlerLoginClient(☃xxx, this, null));
      ☃xxx.sendPacket(new C00Handshake(☃xx.toString(), 0, EnumConnectionState.LOGIN));
      ☃xxx.sendPacket(new CPacketLoginStart(this.getSession().getProfile()));
      this.networkManager = ☃xxx;
   }

   public void loadWorld(@Nullable WorldClient var1) {
      this.loadWorld(☃, "");
   }

   public void loadWorld(@Nullable WorldClient var1, String var2) {
      if (☃ == null) {
         NetHandlerPlayClient ☃ = this.getConnection();
         if (☃ != null) {
            ☃.cleanup();
         }

         if (this.integratedServer != null && this.integratedServer.isAnvilFileSet()) {
            this.integratedServer.initiateShutdown();
         }

         this.integratedServer = null;
         this.entityRenderer.resetData();
         this.playerController = null;
         NarratorChatListener.INSTANCE.clear();
      }

      this.renderViewEntity = null;
      this.networkManager = null;
      if (this.loadingScreen != null) {
         this.loadingScreen.resetProgressAndMessage(☃);
         this.loadingScreen.displayLoadingString("");
      }

      if (☃ == null && this.world != null) {
         this.resourcePackRepository.clearResourcePack();
         this.ingameGUI.resetPlayersOverlayFooterHeader();
         this.setServerData(null);
         this.integratedServerIsRunning = false;
      }

      this.soundHandler.stopSounds();
      this.world = ☃;
      if (this.renderGlobal != null) {
         this.renderGlobal.setWorldAndLoadRenderers(☃);
      }

      if (this.effectRenderer != null) {
         this.effectRenderer.clearEffects(☃);
      }

      TileEntityRendererDispatcher.instance.setWorld(☃);
      if (☃ != null) {
         if (!this.integratedServerIsRunning) {
            AuthenticationService ☃x = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
            MinecraftSessionService ☃xx = ☃x.createMinecraftSessionService();
            GameProfileRepository ☃xxx = ☃x.createProfileRepository();
            PlayerProfileCache ☃xxxx = new PlayerProfileCache(☃xxx, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(☃xxxx);
            TileEntitySkull.setSessionService(☃xx);
            PlayerProfileCache.setOnlineMode(false);
         }

         if (this.player == null) {
            this.player = this.playerController.createPlayer(☃, new StatisticsManager(), new RecipeBookClient());
            this.playerController.flipPlayer(this.player);
         }

         this.player.preparePlayerToSpawn();
         ☃.spawnEntity(this.player);
         this.player.movementInput = new MovementInputFromOptions(this.gameSettings);
         this.playerController.setPlayerCapabilities(this.player);
         this.renderViewEntity = this.player;
      } else {
         this.saveLoader.flushCache();
         this.player = null;
      }

      System.gc();
      this.systemTime = 0L;
   }

   public void setDimensionAndSpawnPlayer(int var1) {
      this.world.setInitialSpawnLocation();
      this.world.removeAllEntities();
      int ☃ = 0;
      String ☃x = null;
      if (this.player != null) {
         ☃ = this.player.getEntityId();
         this.world.removeEntity(this.player);
         ☃x = this.player.getServerBrand();
      }

      this.renderViewEntity = null;
      EntityPlayerSP ☃xx = this.player;
      this.player = this.playerController
         .createPlayer(
            this.world,
            this.player == null ? new StatisticsManager() : this.player.getStatFileWriter(),
            this.player == null ? new RecipeBook() : this.player.getRecipeBook()
         );
      this.player.getDataManager().setEntryValues(☃xx.getDataManager().getAll());
      this.player.dimension = ☃;
      this.renderViewEntity = this.player;
      this.player.preparePlayerToSpawn();
      this.player.setServerBrand(☃x);
      this.world.spawnEntity(this.player);
      this.playerController.flipPlayer(this.player);
      this.player.movementInput = new MovementInputFromOptions(this.gameSettings);
      this.player.setEntityId(☃);
      this.playerController.setPlayerCapabilities(this.player);
      this.player.setReducedDebug(☃xx.hasReducedDebug());
      if (this.currentScreen instanceof GuiGameOver) {
         this.displayGuiScreen(null);
      }
   }

   public final boolean isDemo() {
      return this.isDemo;
   }

   @Nullable
   public NetHandlerPlayClient getConnection() {
      return this.player == null ? null : this.player.connection;
   }

   public static boolean isGuiEnabled() {
      return instance == null || !instance.gameSettings.hideGUI;
   }

   public static boolean isFancyGraphicsEnabled() {
      return instance != null && instance.gameSettings.fancyGraphics;
   }

   public static boolean isAmbientOcclusionEnabled() {
      return instance != null && instance.gameSettings.ambientOcclusion != 0;
   }

   private void middleClickMouse() {
      if (this.objectMouseOver != null && this.objectMouseOver.typeOfHit != RayTraceResult.Type.MISS) {
         boolean ☃ = this.player.capabilities.isCreativeMode;
         TileEntity ☃x = null;
         ItemStack ☃xx;
         if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos ☃xxx = this.objectMouseOver.getBlockPos();
            IBlockState ☃xxxx = this.world.getBlockState(☃xxx);
            Block ☃xxxxx = ☃xxxx.getBlock();
            if (☃xxxx.getMaterial() == Material.AIR) {
               return;
            }

            ☃xx = ☃xxxxx.getItem(this.world, ☃xxx, ☃xxxx);
            if (☃xx.isEmpty()) {
               return;
            }

            if (☃ && GuiScreen.isCtrlKeyDown() && ☃xxxxx.hasTileEntity()) {
               ☃x = this.world.getTileEntity(☃xxx);
            }
         } else {
            if (this.objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY || this.objectMouseOver.entityHit == null || !☃) {
               return;
            }

            if (this.objectMouseOver.entityHit instanceof EntityPainting) {
               ☃xx = new ItemStack(Items.PAINTING);
            } else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
               ☃xx = new ItemStack(Items.LEAD);
            } else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
               EntityItemFrame ☃xxxxxx = (EntityItemFrame)this.objectMouseOver.entityHit;
               ItemStack ☃xxxxxxx = ☃xxxxxx.getDisplayedItem();
               if (☃xxxxxxx.isEmpty()) {
                  ☃xx = new ItemStack(Items.ITEM_FRAME);
               } else {
                  ☃xx = ☃xxxxxxx.copy();
               }
            } else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
               EntityMinecart ☃xxxxxx = (EntityMinecart)this.objectMouseOver.entityHit;
               Item ☃xxxxxxx;
               switch (☃xxxxxx.getType()) {
                  case FURNACE:
                     ☃xxxxxxx = Items.FURNACE_MINECART;
                     break;
                  case CHEST:
                     ☃xxxxxxx = Items.CHEST_MINECART;
                     break;
                  case TNT:
                     ☃xxxxxxx = Items.TNT_MINECART;
                     break;
                  case HOPPER:
                     ☃xxxxxxx = Items.HOPPER_MINECART;
                     break;
                  case COMMAND_BLOCK:
                     ☃xxxxxxx = Items.COMMAND_BLOCK_MINECART;
                     break;
                  default:
                     ☃xxxxxxx = Items.MINECART;
               }

               ☃xx = new ItemStack(☃xxxxxxx);
            } else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
               ☃xx = new ItemStack(((EntityBoat)this.objectMouseOver.entityHit).getItemBoat());
            } else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
               ☃xx = new ItemStack(Items.ARMOR_STAND);
            } else if (this.objectMouseOver.entityHit instanceof EntityEnderCrystal) {
               ☃xx = new ItemStack(Items.END_CRYSTAL);
            } else {
               ResourceLocation ☃xxxxxx = EntityList.getKey(this.objectMouseOver.entityHit);
               if (☃xxxxxx == null || !EntityList.ENTITY_EGGS.containsKey(☃xxxxxx)) {
                  return;
               }

               ☃xx = new ItemStack(Items.SPAWN_EGG);
               ItemMonsterPlacer.applyEntityIdToItemStack(☃xx, ☃xxxxxx);
            }
         }

         if (☃xx.isEmpty()) {
            String ☃xxxxxx = "";
            if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
               ☃xxxxxx = Block.REGISTRY.getNameForObject(this.world.getBlockState(this.objectMouseOver.getBlockPos()).getBlock()).toString();
            } else if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
               ☃xxxxxx = EntityList.getKey(this.objectMouseOver.entityHit).toString();
            }

            LOGGER.warn("Picking on: [{}] {} gave null item", this.objectMouseOver.typeOfHit, ☃xxxxxx);
         } else {
            InventoryPlayer ☃xxxxxx = this.player.inventory;
            if (☃x != null) {
               this.storeTEInStack(☃xx, ☃x);
            }

            int ☃xxxxxxx = ☃xxxxxx.getSlotFor(☃xx);
            if (☃) {
               ☃xxxxxx.setPickedItemStack(☃xx);
               this.playerController.sendSlotPacket(this.player.getHeldItem(EnumHand.MAIN_HAND), 36 + ☃xxxxxx.currentItem);
            } else if (☃xxxxxxx != -1) {
               if (InventoryPlayer.isHotbar(☃xxxxxxx)) {
                  ☃xxxxxx.currentItem = ☃xxxxxxx;
               } else {
                  this.playerController.pickItem(☃xxxxxxx);
               }
            }
         }
      }
   }

   private ItemStack storeTEInStack(ItemStack var1, TileEntity var2) {
      NBTTagCompound ☃ = ☃.writeToNBT(new NBTTagCompound());
      if (☃.getItem() == Items.SKULL && ☃.hasKey("Owner")) {
         NBTTagCompound ☃x = ☃.getCompoundTag("Owner");
         NBTTagCompound ☃xx = new NBTTagCompound();
         ☃xx.setTag("SkullOwner", ☃x);
         ☃.setTagCompound(☃xx);
         return ☃;
      } else {
         ☃.setTagInfo("BlockEntityTag", ☃);
         NBTTagCompound ☃x = new NBTTagCompound();
         NBTTagList ☃xx = new NBTTagList();
         ☃xx.appendTag(new NBTTagString("(+NBT)"));
         ☃x.setTag("Lore", ☃xx);
         ☃.setTagInfo("display", ☃x);
         return ☃;
      }
   }

   public CrashReport addGraphicsAndWorldToCrashReport(CrashReport var1) {
      ☃.getCategory().addDetail("Launched Version", new ICrashReportDetail<String>() {
         public String call() {
            return Minecraft.this.launchedVersion;
         }
      });
      ☃.getCategory().addDetail("LWJGL", new ICrashReportDetail<String>() {
         public String call() {
            return Sys.getVersion();
         }
      });
      ☃.getCategory().addDetail("OpenGL", new ICrashReportDetail<String>() {
         public String call() {
            return GlStateManager.glGetString(7937) + " GL version " + GlStateManager.glGetString(7938) + ", " + GlStateManager.glGetString(7936);
         }
      });
      ☃.getCategory().addDetail("GL Caps", new ICrashReportDetail<String>() {
         public String call() {
            return OpenGlHelper.getLogText();
         }
      });
      ☃.getCategory().addDetail("Using VBOs", new ICrashReportDetail<String>() {
         public String call() {
            return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
         }
      });
      ☃.getCategory()
         .addDetail(
            "Is Modded",
            new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  String ☃ = ClientBrandRetriever.getClientModName();
                  if (!"vanilla".equals(☃)) {
                     return "Definitely; Client brand changed to '" + ☃ + "'";
                  } else {
                     return Minecraft.class.getSigners() == null
                        ? "Very likely; Jar signature invalidated"
                        : "Probably not. Jar signature remains and client brand is untouched.";
                  }
               }
            }
         );
      ☃.getCategory().addDetail("Type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return "Client (map_client.txt)";
         }
      });
      ☃.getCategory().addDetail("Resource Packs", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            StringBuilder ☃ = new StringBuilder();

            for (String ☃x : Minecraft.this.gameSettings.resourcePacks) {
               if (☃.length() > 0) {
                  ☃.append(", ");
               }

               ☃.append(☃x);
               if (Minecraft.this.gameSettings.incompatibleResourcePacks.contains(☃x)) {
                  ☃.append(" (incompatible)");
               }
            }

            return ☃.toString();
         }
      });
      ☃.getCategory().addDetail("Current Language", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return Minecraft.this.languageManager.getCurrentLanguage().toString();
         }
      });
      ☃.getCategory().addDetail("Profiler Position", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return Minecraft.this.profiler.profilingEnabled ? Minecraft.this.profiler.getNameOfLastSection() : "N/A (disabled)";
         }
      });
      ☃.getCategory().addDetail("CPU", new ICrashReportDetail<String>() {
         public String call() {
            return OpenGlHelper.getCpu();
         }
      });
      if (this.world != null) {
         this.world.addWorldInfoToCrashReport(☃);
      }

      return ☃;
   }

   public static Minecraft getMinecraft() {
      return instance;
   }

   public ListenableFuture<Object> scheduleResourcesRefresh() {
      return this.addScheduledTask(new Runnable() {
         @Override
         public void run() {
            Minecraft.this.refreshResources();
         }
      });
   }

   @Override
   public void addServerStatsToSnooper(Snooper var1) {
      ☃.addClientStat("fps", debugFPS);
      ☃.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
      ☃.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
      ☃.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
      ☃.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - ☃.getMinecraftStartTimeMillis()) / 60L * 1000L);
      ☃.addClientStat("current_action", this.getCurrentAction());
      ☃.addClientStat("language", this.gameSettings.language == null ? "en_us" : this.gameSettings.language);
      String ☃ = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
      ☃.addClientStat("endianness", ☃);
      ☃.addClientStat("subtitles", this.gameSettings.showSubtitles);
      ☃.addClientStat("touch", this.gameSettings.touchscreen ? "touch" : "mouse");
      ☃.addClientStat("resource_packs", this.resourcePackRepository.getRepositoryEntries().size());
      int ☃x = 0;

      for (ResourcePackRepository.Entry ☃xx : this.resourcePackRepository.getRepositoryEntries()) {
         ☃.addClientStat("resource_pack[" + ☃x++ + "]", ☃xx.getResourcePackName());
      }

      if (this.integratedServer != null && this.integratedServer.getPlayerUsageSnooper() != null) {
         ☃.addClientStat("snooper_partner", this.integratedServer.getPlayerUsageSnooper().getUniqueID());
      }
   }

   private String getCurrentAction() {
      if (this.integratedServer != null) {
         return this.integratedServer.getPublic() ? "hosting_lan" : "singleplayer";
      } else if (this.currentServerData != null) {
         return this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer";
      } else {
         return "out_of_game";
      }
   }

   @Override
   public void addServerTypeToSnooper(Snooper var1) {
      ☃.addStatToSnooper("opengl_version", GlStateManager.glGetString(7938));
      ☃.addStatToSnooper("opengl_vendor", GlStateManager.glGetString(7936));
      ☃.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
      ☃.addStatToSnooper("launched_version", this.launchedVersion);
      ContextCapabilities ☃ = GLContext.getCapabilities();
      ☃.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", ☃.GL_ARB_arrays_of_arrays);
      ☃.addStatToSnooper("gl_caps[ARB_base_instance]", ☃.GL_ARB_base_instance);
      ☃.addStatToSnooper("gl_caps[ARB_blend_func_extended]", ☃.GL_ARB_blend_func_extended);
      ☃.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", ☃.GL_ARB_clear_buffer_object);
      ☃.addStatToSnooper("gl_caps[ARB_color_buffer_float]", ☃.GL_ARB_color_buffer_float);
      ☃.addStatToSnooper("gl_caps[ARB_compatibility]", ☃.GL_ARB_compatibility);
      ☃.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", ☃.GL_ARB_compressed_texture_pixel_storage);
      ☃.addStatToSnooper("gl_caps[ARB_compute_shader]", ☃.GL_ARB_compute_shader);
      ☃.addStatToSnooper("gl_caps[ARB_copy_buffer]", ☃.GL_ARB_copy_buffer);
      ☃.addStatToSnooper("gl_caps[ARB_copy_image]", ☃.GL_ARB_copy_image);
      ☃.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", ☃.GL_ARB_depth_buffer_float);
      ☃.addStatToSnooper("gl_caps[ARB_compute_shader]", ☃.GL_ARB_compute_shader);
      ☃.addStatToSnooper("gl_caps[ARB_copy_buffer]", ☃.GL_ARB_copy_buffer);
      ☃.addStatToSnooper("gl_caps[ARB_copy_image]", ☃.GL_ARB_copy_image);
      ☃.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", ☃.GL_ARB_depth_buffer_float);
      ☃.addStatToSnooper("gl_caps[ARB_depth_clamp]", ☃.GL_ARB_depth_clamp);
      ☃.addStatToSnooper("gl_caps[ARB_depth_texture]", ☃.GL_ARB_depth_texture);
      ☃.addStatToSnooper("gl_caps[ARB_draw_buffers]", ☃.GL_ARB_draw_buffers);
      ☃.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", ☃.GL_ARB_draw_buffers_blend);
      ☃.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", ☃.GL_ARB_draw_elements_base_vertex);
      ☃.addStatToSnooper("gl_caps[ARB_draw_indirect]", ☃.GL_ARB_draw_indirect);
      ☃.addStatToSnooper("gl_caps[ARB_draw_instanced]", ☃.GL_ARB_draw_instanced);
      ☃.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", ☃.GL_ARB_explicit_attrib_location);
      ☃.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", ☃.GL_ARB_explicit_uniform_location);
      ☃.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", ☃.GL_ARB_fragment_layer_viewport);
      ☃.addStatToSnooper("gl_caps[ARB_fragment_program]", ☃.GL_ARB_fragment_program);
      ☃.addStatToSnooper("gl_caps[ARB_fragment_shader]", ☃.GL_ARB_fragment_shader);
      ☃.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", ☃.GL_ARB_fragment_program_shadow);
      ☃.addStatToSnooper("gl_caps[ARB_framebuffer_object]", ☃.GL_ARB_framebuffer_object);
      ☃.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", ☃.GL_ARB_framebuffer_sRGB);
      ☃.addStatToSnooper("gl_caps[ARB_geometry_shader4]", ☃.GL_ARB_geometry_shader4);
      ☃.addStatToSnooper("gl_caps[ARB_gpu_shader5]", ☃.GL_ARB_gpu_shader5);
      ☃.addStatToSnooper("gl_caps[ARB_half_float_pixel]", ☃.GL_ARB_half_float_pixel);
      ☃.addStatToSnooper("gl_caps[ARB_half_float_vertex]", ☃.GL_ARB_half_float_vertex);
      ☃.addStatToSnooper("gl_caps[ARB_instanced_arrays]", ☃.GL_ARB_instanced_arrays);
      ☃.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", ☃.GL_ARB_map_buffer_alignment);
      ☃.addStatToSnooper("gl_caps[ARB_map_buffer_range]", ☃.GL_ARB_map_buffer_range);
      ☃.addStatToSnooper("gl_caps[ARB_multisample]", ☃.GL_ARB_multisample);
      ☃.addStatToSnooper("gl_caps[ARB_multitexture]", ☃.GL_ARB_multitexture);
      ☃.addStatToSnooper("gl_caps[ARB_occlusion_query2]", ☃.GL_ARB_occlusion_query2);
      ☃.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", ☃.GL_ARB_pixel_buffer_object);
      ☃.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", ☃.GL_ARB_seamless_cube_map);
      ☃.addStatToSnooper("gl_caps[ARB_shader_objects]", ☃.GL_ARB_shader_objects);
      ☃.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", ☃.GL_ARB_shader_stencil_export);
      ☃.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", ☃.GL_ARB_shader_texture_lod);
      ☃.addStatToSnooper("gl_caps[ARB_shadow]", ☃.GL_ARB_shadow);
      ☃.addStatToSnooper("gl_caps[ARB_shadow_ambient]", ☃.GL_ARB_shadow_ambient);
      ☃.addStatToSnooper("gl_caps[ARB_stencil_texturing]", ☃.GL_ARB_stencil_texturing);
      ☃.addStatToSnooper("gl_caps[ARB_sync]", ☃.GL_ARB_sync);
      ☃.addStatToSnooper("gl_caps[ARB_tessellation_shader]", ☃.GL_ARB_tessellation_shader);
      ☃.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", ☃.GL_ARB_texture_border_clamp);
      ☃.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", ☃.GL_ARB_texture_buffer_object);
      ☃.addStatToSnooper("gl_caps[ARB_texture_cube_map]", ☃.GL_ARB_texture_cube_map);
      ☃.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", ☃.GL_ARB_texture_cube_map_array);
      ☃.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", ☃.GL_ARB_texture_non_power_of_two);
      ☃.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", ☃.GL_ARB_uniform_buffer_object);
      ☃.addStatToSnooper("gl_caps[ARB_vertex_blend]", ☃.GL_ARB_vertex_blend);
      ☃.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", ☃.GL_ARB_vertex_buffer_object);
      ☃.addStatToSnooper("gl_caps[ARB_vertex_program]", ☃.GL_ARB_vertex_program);
      ☃.addStatToSnooper("gl_caps[ARB_vertex_shader]", ☃.GL_ARB_vertex_shader);
      ☃.addStatToSnooper("gl_caps[EXT_bindable_uniform]", ☃.GL_EXT_bindable_uniform);
      ☃.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", ☃.GL_EXT_blend_equation_separate);
      ☃.addStatToSnooper("gl_caps[EXT_blend_func_separate]", ☃.GL_EXT_blend_func_separate);
      ☃.addStatToSnooper("gl_caps[EXT_blend_minmax]", ☃.GL_EXT_blend_minmax);
      ☃.addStatToSnooper("gl_caps[EXT_blend_subtract]", ☃.GL_EXT_blend_subtract);
      ☃.addStatToSnooper("gl_caps[EXT_draw_instanced]", ☃.GL_EXT_draw_instanced);
      ☃.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", ☃.GL_EXT_framebuffer_multisample);
      ☃.addStatToSnooper("gl_caps[EXT_framebuffer_object]", ☃.GL_EXT_framebuffer_object);
      ☃.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", ☃.GL_EXT_framebuffer_sRGB);
      ☃.addStatToSnooper("gl_caps[EXT_geometry_shader4]", ☃.GL_EXT_geometry_shader4);
      ☃.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", ☃.GL_EXT_gpu_program_parameters);
      ☃.addStatToSnooper("gl_caps[EXT_gpu_shader4]", ☃.GL_EXT_gpu_shader4);
      ☃.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", ☃.GL_EXT_multi_draw_arrays);
      ☃.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", ☃.GL_EXT_packed_depth_stencil);
      ☃.addStatToSnooper("gl_caps[EXT_paletted_texture]", ☃.GL_EXT_paletted_texture);
      ☃.addStatToSnooper("gl_caps[EXT_rescale_normal]", ☃.GL_EXT_rescale_normal);
      ☃.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", ☃.GL_EXT_separate_shader_objects);
      ☃.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", ☃.GL_EXT_shader_image_load_store);
      ☃.addStatToSnooper("gl_caps[EXT_shadow_funcs]", ☃.GL_EXT_shadow_funcs);
      ☃.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", ☃.GL_EXT_shared_texture_palette);
      ☃.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", ☃.GL_EXT_stencil_clear_tag);
      ☃.addStatToSnooper("gl_caps[EXT_stencil_two_side]", ☃.GL_EXT_stencil_two_side);
      ☃.addStatToSnooper("gl_caps[EXT_stencil_wrap]", ☃.GL_EXT_stencil_wrap);
      ☃.addStatToSnooper("gl_caps[EXT_texture_3d]", ☃.GL_EXT_texture_3d);
      ☃.addStatToSnooper("gl_caps[EXT_texture_array]", ☃.GL_EXT_texture_array);
      ☃.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", ☃.GL_EXT_texture_buffer_object);
      ☃.addStatToSnooper("gl_caps[EXT_texture_integer]", ☃.GL_EXT_texture_integer);
      ☃.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", ☃.GL_EXT_texture_lod_bias);
      ☃.addStatToSnooper("gl_caps[EXT_texture_sRGB]", ☃.GL_EXT_texture_sRGB);
      ☃.addStatToSnooper("gl_caps[EXT_vertex_shader]", ☃.GL_EXT_vertex_shader);
      ☃.addStatToSnooper("gl_caps[EXT_vertex_weighting]", ☃.GL_EXT_vertex_weighting);
      ☃.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GlStateManager.glGetInteger(35658));
      GlStateManager.glGetError();
      ☃.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GlStateManager.glGetInteger(35657));
      GlStateManager.glGetError();
      ☃.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GlStateManager.glGetInteger(34921));
      GlStateManager.glGetError();
      ☃.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GlStateManager.glGetInteger(35660));
      GlStateManager.glGetError();
      ☃.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GlStateManager.glGetInteger(34930));
      GlStateManager.glGetError();
      ☃.addStatToSnooper("gl_caps[gl_max_array_texture_layers]", GlStateManager.glGetInteger(35071));
      GlStateManager.glGetError();
      ☃.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
      GameProfile ☃x = this.session.getProfile();
      if (☃x != null && ☃x.getId() != null) {
         ☃.addStatToSnooper("uuid", Hashing.sha1().hashBytes(☃x.getId().toString().getBytes(Charsets.ISO_8859_1)).toString());
      }
   }

   public static int getGLMaximumTextureSize() {
      for (int ☃ = 16384; ☃ > 0; ☃ >>= 1) {
         GlStateManager.glTexImage2D(32868, 0, 6408, ☃, ☃, 0, 6408, 5121, null);
         int ☃x = GlStateManager.glGetTexLevelParameteri(32868, 0, 4096);
         if (☃x != 0) {
            return ☃;
         }
      }

      return -1;
   }

   @Override
   public boolean isSnooperEnabled() {
      return this.gameSettings.snooperEnabled;
   }

   public void setServerData(ServerData var1) {
      this.currentServerData = ☃;
   }

   @Nullable
   public ServerData getCurrentServerData() {
      return this.currentServerData;
   }

   public boolean isIntegratedServerRunning() {
      return this.integratedServerIsRunning;
   }

   public boolean isSingleplayer() {
      return this.integratedServerIsRunning && this.integratedServer != null;
   }

   @Nullable
   public IntegratedServer getIntegratedServer() {
      return this.integratedServer;
   }

   public static void stopIntegratedServer() {
      if (instance != null) {
         IntegratedServer ☃ = instance.getIntegratedServer();
         if (☃ != null) {
            ☃.stopServer();
         }
      }
   }

   public Snooper getPlayerUsageSnooper() {
      return this.usageSnooper;
   }

   public static long getSystemTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public boolean isFullScreen() {
      return this.fullscreen;
   }

   public Session getSession() {
      return this.session;
   }

   public PropertyMap getProfileProperties() {
      if (this.profileProperties.isEmpty()) {
         GameProfile ☃ = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
         this.profileProperties.putAll(☃.getProperties());
      }

      return this.profileProperties;
   }

   public Proxy getProxy() {
      return this.proxy;
   }

   public TextureManager getTextureManager() {
      return this.renderEngine;
   }

   public IResourceManager getResourceManager() {
      return this.resourceManager;
   }

   public ResourcePackRepository getResourcePackRepository() {
      return this.resourcePackRepository;
   }

   public LanguageManager getLanguageManager() {
      return this.languageManager;
   }

   public TextureMap getTextureMapBlocks() {
      return this.textureMapBlocks;
   }

   public boolean isJava64bit() {
      return this.jvm64bit;
   }

   public boolean isGamePaused() {
      return this.isGamePaused;
   }

   public SoundHandler getSoundHandler() {
      return this.soundHandler;
   }

   public MusicTicker.MusicType getAmbientMusicType() {
      if (this.currentScreen instanceof GuiWinGame) {
         return MusicTicker.MusicType.CREDITS;
      } else if (this.player != null) {
         if (this.player.world.provider instanceof WorldProviderHell) {
            return MusicTicker.MusicType.NETHER;
         } else if (this.player.world.provider instanceof WorldProviderEnd) {
            return this.ingameGUI.getBossOverlay().shouldPlayEndBossMusic() ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END;
         } else {
            return this.player.capabilities.isCreativeMode && this.player.capabilities.allowFlying
               ? MusicTicker.MusicType.CREATIVE
               : MusicTicker.MusicType.GAME;
         }
      } else {
         return MusicTicker.MusicType.MENU;
      }
   }

   public void dispatchKeypresses() {
      int ☃ = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
      if (☃ != 0 && !Keyboard.isRepeatEvent()) {
         if (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L) {
            if (Keyboard.getEventKeyState()) {
               if (☃ == this.gameSettings.keyBindFullscreen.getKeyCode()) {
                  this.toggleFullscreen();
               } else if (☃ == this.gameSettings.keyBindScreenshot.getKeyCode()) {
                  this.ingameGUI
                     .getChatGUI()
                     .printChatMessage(ScreenShotHelper.saveScreenshot(this.gameDir, this.displayWidth, this.displayHeight, this.framebuffer));
               } else if (☃ == 48 && GuiScreen.isCtrlKeyDown() && (this.currentScreen == null || this.currentScreen != null && !this.currentScreen.isFocused())
                  )
                {
                  this.gameSettings.setOptionValue(GameSettings.Options.NARRATOR, 1);
                  if (this.currentScreen instanceof ScreenChatOptions) {
                     ((ScreenChatOptions)this.currentScreen).updateNarratorButton();
                  }
               }
            }
         }
      }
   }

   public MinecraftSessionService getSessionService() {
      return this.sessionService;
   }

   public SkinManager getSkinManager() {
      return this.skinManager;
   }

   @Nullable
   public Entity getRenderViewEntity() {
      return this.renderViewEntity;
   }

   public void setRenderViewEntity(Entity var1) {
      this.renderViewEntity = ☃;
      this.entityRenderer.loadEntityShader(☃);
   }

   public <V> ListenableFuture<V> addScheduledTask(Callable<V> var1) {
      Validate.notNull(☃);
      if (this.isCallingFromMinecraftThread()) {
         try {
            return Futures.immediateFuture(☃.call());
         } catch (Exception var5) {
            return Futures.immediateFailedCheckedFuture(var5);
         }
      } else {
         ListenableFutureTask<V> ☃ = ListenableFutureTask.create(☃);
         synchronized (this.scheduledTasks) {
            this.scheduledTasks.add(☃);
            return ☃;
         }
      }
   }

   @Override
   public ListenableFuture<Object> addScheduledTask(Runnable var1) {
      Validate.notNull(☃);
      return this.addScheduledTask(Executors.callable(☃));
   }

   @Override
   public boolean isCallingFromMinecraftThread() {
      return Thread.currentThread() == this.thread;
   }

   public BlockRendererDispatcher getBlockRendererDispatcher() {
      return this.blockRenderDispatcher;
   }

   public RenderManager getRenderManager() {
      return this.renderManager;
   }

   public RenderItem getRenderItem() {
      return this.renderItem;
   }

   public ItemRenderer getItemRenderer() {
      return this.itemRenderer;
   }

   public <T> ISearchTree<T> getSearchTree(SearchTreeManager.Key<T> var1) {
      return this.searchTreeManager.get(☃);
   }

   public static int getDebugFPS() {
      return debugFPS;
   }

   public FrameTimer getFrameTimer() {
      return this.frameTimer;
   }

   public boolean isConnectedToRealms() {
      return this.connectedToRealms;
   }

   public void setConnectedToRealms(boolean var1) {
      this.connectedToRealms = ☃;
   }

   public DataFixer getDataFixer() {
      return this.dataFixer;
   }

   public float getRenderPartialTicks() {
      return this.timer.renderPartialTicks;
   }

   public float getTickLength() {
      return this.timer.elapsedPartialTicks;
   }

   public BlockColors getBlockColors() {
      return this.blockColors;
   }

   public boolean isReducedDebug() {
      return this.player != null && this.player.hasReducedDebug() || this.gameSettings.reducedDebugInfo;
   }

   public GuiToast getToastGui() {
      return this.toastGui;
   }

   public Tutorial getTutorial() {
      return this.tutorial;
   }
}
