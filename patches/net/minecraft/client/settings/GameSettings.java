package net.minecraft.client.settings;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.client.resource.IResourceType;
import net.optifine.ClearWater;
import net.optifine.CustomColors;
import net.optifine.CustomGuis;
import net.optifine.CustomSky;
import net.optifine.DynamicLights;
import net.optifine.Lang;
import net.optifine.NaturalTextures;
import net.optifine.RandomEntities;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;
import net.optifine.util.KeyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GameSettings {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Gson GSON = new Gson();
   private static final Type TYPE_LIST_STRING = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
         return new Type[]{String.class};
      }

      @Override
      public Type getRawType() {
         return List.class;
      }

      @Override
      public Type getOwnerType() {
         return null;
      }
   };
   public static final Splitter COLON_SPLITTER = Splitter.on(':');
   private static final String[] GUISCALES = new String[]{
      "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"
   };
   private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
   private static final String[] AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
   private static final String[] CLOUDS_TYPES = new String[]{"options.off", "options.clouds.fast", "options.clouds.fancy"};
   private static final String[] ATTACK_INDICATORS = new String[]{"options.off", "options.attack.crosshair", "options.attack.hotbar"};
   public static final String[] NARRATOR_MODES = new String[]{
      "options.narrator.off", "options.narrator.all", "options.narrator.chat", "options.narrator.system"
   };
   public float mouseSensitivity = 0.5F;
   public boolean invertMouse;
   public int renderDistanceChunks = -1;
   public boolean viewBobbing = true;
   public boolean anaglyph;
   public boolean fboEnable = true;
   public int limitFramerate = 120;
   public int clouds = 2;
   public boolean fancyGraphics = true;
   public int ambientOcclusion = 2;
   public List<String> resourcePacks = Lists.newArrayList();
   public List<String> incompatibleResourcePacks = Lists.newArrayList();
   public EnumChatVisibility chatVisibility = EnumChatVisibility.FULL;
   public boolean chatColours = true;
   public boolean chatLinks = true;
   public boolean chatLinksPrompt = true;
   public float chatOpacity = 1.0F;
   public boolean snooperEnabled = true;
   public boolean fullScreen;
   public boolean enableVsync = true;
   public boolean useVbo = true;
   public boolean reducedDebugInfo;
   public boolean hideServerAddress;
   public boolean advancedItemTooltips;
   public boolean pauseOnLostFocus = true;
   private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet(EnumPlayerModelParts.values());
   public boolean touchscreen;
   public EnumHandSide mainHand = EnumHandSide.RIGHT;
   public int overrideWidth;
   public int overrideHeight;
   public boolean heldItemTooltips = true;
   public float chatScale = 1.0F;
   public float chatWidth = 1.0F;
   public float chatHeightUnfocused = 0.44366196F;
   public float chatHeightFocused = 1.0F;
   public int mipmapLevels = 4;
   private final Map<SoundCategory, Float> soundLevels = Maps.newEnumMap(SoundCategory.class);
   public boolean useNativeTransport = true;
   public boolean entityShadows = true;
   public int attackIndicator = 1;
   public boolean enableWeakAttacks;
   public boolean showSubtitles;
   public boolean realmsNotifications = true;
   public boolean autoJump = true;
   public TutorialSteps tutorialStep = TutorialSteps.MOVEMENT;
   public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
   public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
   public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
   public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
   public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
   public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
   public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
   public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
   public KeyBinding keyBindSwapHands = new KeyBinding("key.swapHands", 33, "key.categories.inventory");
   public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.inventory");
   public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
   public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
   public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
   public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
   public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
   public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
   public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
   public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
   public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
   public KeyBinding keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
   public KeyBinding keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
   public KeyBinding keyBindAdvancements = new KeyBinding("key.advancements", 38, "key.categories.misc");
   public KeyBinding[] keyBindsHotbar = new KeyBinding[]{
      new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"),
      new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"),
      new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"),
      new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"),
      new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"),
      new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"),
      new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"),
      new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"),
      new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")
   };
   public KeyBinding keyBindSaveToolbar = new KeyBinding("key.saveToolbarActivator", 46, "key.categories.creative");
   public KeyBinding keyBindLoadToolbar = new KeyBinding("key.loadToolbarActivator", 45, "key.categories.creative");
   public KeyBinding[] keyBindings;
   protected Minecraft mc;
   private File optionsFile;
   public EnumDifficulty difficulty;
   public boolean hideGUI;
   public int thirdPersonView;
   public boolean showDebugInfo;
   public boolean showDebugProfilerChart;
   public boolean showLagometer;
   public String lastServer;
   public boolean smoothCamera;
   public boolean debugCamEnable;
   public float fovSetting;
   public float gammaSetting;
   public float saturation;
   public int guiScale;
   public int particleSetting;
   public int narrator;
   public String language;
   public boolean forceUnicodeFont;
   public int ofFogType = 1;
   public float ofFogStart = 0.8F;
   public int ofMipmapType = 0;
   public boolean ofOcclusionFancy = false;
   public boolean ofSmoothFps = false;
   public boolean ofSmoothWorld = Config.isSingleProcessor();
   public boolean ofLazyChunkLoading = Config.isSingleProcessor();
   public boolean ofRenderRegions = false;
   public boolean ofSmartAnimations = false;
   public float ofAoLevel = 1.0F;
   public int ofAaLevel = 0;
   public int ofAfLevel = 1;
   public int ofClouds = 0;
   public float ofCloudsHeight = 0.0F;
   public int ofTrees = 0;
   public int ofRain = 0;
   public int ofDroppedItems = 0;
   public int ofBetterGrass = 3;
   public int ofAutoSaveTicks = 4000;
   public boolean ofLagometer = false;
   public boolean ofProfiler = false;
   public boolean ofShowFps = false;
   public boolean ofWeather = true;
   public boolean ofSky = true;
   public boolean ofStars = true;
   public boolean ofSunMoon = true;
   public int ofVignette = 0;
   public int ofChunkUpdates = 1;
   public boolean ofChunkUpdatesDynamic = false;
   public int ofTime = 0;
   public boolean ofClearWater = false;
   public boolean ofBetterSnow = false;
   public String ofFullscreenMode = "Default";
   public boolean ofSwampColors = true;
   public boolean ofRandomEntities = true;
   public boolean ofSmoothBiomes = true;
   public boolean ofCustomFonts = true;
   public boolean ofCustomColors = true;
   public boolean ofCustomSky = true;
   public boolean ofShowCapes = true;
   public int ofConnectedTextures = 2;
   public boolean ofCustomItems = true;
   public boolean ofNaturalTextures = false;
   public boolean ofEmissiveTextures = true;
   public boolean ofFastMath = false;
   public boolean ofFastRender = false;
   public int ofTranslucentBlocks = 0;
   public boolean ofDynamicFov = true;
   public boolean ofAlternateBlocks = true;
   public int ofDynamicLights = 3;
   public boolean ofCustomEntityModels = true;
   public boolean ofCustomGuis = true;
   public boolean ofShowGlErrors = true;
   public int ofScreenshotSize = 1;
   public int ofAnimatedWater = 0;
   public int ofAnimatedLava = 0;
   public boolean ofAnimatedFire = true;
   public boolean ofAnimatedPortal = true;
   public boolean ofAnimatedRedstone = true;
   public boolean ofAnimatedExplosion = true;
   public boolean ofAnimatedFlame = true;
   public boolean ofAnimatedSmoke = true;
   public boolean ofVoidParticles = true;
   public boolean ofWaterParticles = true;
   public boolean ofRainSplash = true;
   public boolean ofPortalParticles = true;
   public boolean ofPotionParticles = true;
   public boolean ofFireworkParticles = true;
   public boolean ofDrippingWaterLava = true;
   public boolean ofAnimatedTerrain = true;
   public boolean ofAnimatedTextures = true;
   public static final int DEFAULT = 0;
   public static final int FAST = 1;
   public static final int FANCY = 2;
   public static final int OFF = 3;
   public static final int SMART = 4;
   public static final int ANIM_ON = 0;
   public static final int ANIM_GENERATED = 1;
   public static final int ANIM_OFF = 2;
   public static final String DEFAULT_STR = "Default";
   private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
   private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
   private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
   public KeyBinding ofKeyBindZoom;
   private File optionsFileOF;
   private boolean needsResourceRefresh = false;

   public GameSettings(Minecraft mcIn, File optionsFileIn) {
      this.setForgeKeybindProperties();
      this.keyBindings = (KeyBinding[])ArrayUtils.addAll(
         new KeyBinding[]{
            this.keyBindAttack,
            this.keyBindUseItem,
            this.keyBindForward,
            this.keyBindLeft,
            this.keyBindBack,
            this.keyBindRight,
            this.keyBindJump,
            this.keyBindSneak,
            this.keyBindSprint,
            this.keyBindDrop,
            this.keyBindInventory,
            this.keyBindChat,
            this.keyBindPlayerList,
            this.keyBindPickBlock,
            this.keyBindCommand,
            this.keyBindScreenshot,
            this.keyBindTogglePerspective,
            this.keyBindSmoothCamera,
            this.keyBindFullscreen,
            this.keyBindSpectatorOutlines,
            this.keyBindSwapHands,
            this.keyBindSaveToolbar,
            this.keyBindLoadToolbar,
            this.keyBindAdvancements
         },
         this.keyBindsHotbar
      );
      this.difficulty = EnumDifficulty.NORMAL;
      this.lastServer = "";
      this.fovSetting = 70.0F;
      this.language = "en_us";
      this.mc = mcIn;
      this.optionsFile = new File(optionsFileIn, "options.txt");
      if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
         GameSettings.Options.RENDER_DISTANCE.setValueMax(32.0F);
         long MB = 1000000L;
         if (Runtime.getRuntime().maxMemory() >= 1500L * MB) {
            GameSettings.Options.RENDER_DISTANCE.setValueMax(48.0F);
         }

         if (Runtime.getRuntime().maxMemory() >= 2500L * MB) {
            GameSettings.Options.RENDER_DISTANCE.setValueMax(64.0F);
         }
      } else {
         GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
      }

      this.renderDistanceChunks = mcIn.isJava64bit() ? 12 : 8;
      this.optionsFileOF = new File(optionsFileIn, "optionsof.txt");
      this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
      this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
      this.keyBindings = (KeyBinding[])ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom);
      KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[]{this.ofKeyBindZoom});
      this.renderDistanceChunks = 8;
      this.loadOptions();
      Config.initGameSettings(this);
   }

   public GameSettings() {
      this.setForgeKeybindProperties();
      this.keyBindings = (KeyBinding[])ArrayUtils.addAll(
         new KeyBinding[]{
            this.keyBindAttack,
            this.keyBindUseItem,
            this.keyBindForward,
            this.keyBindLeft,
            this.keyBindBack,
            this.keyBindRight,
            this.keyBindJump,
            this.keyBindSneak,
            this.keyBindSprint,
            this.keyBindDrop,
            this.keyBindInventory,
            this.keyBindChat,
            this.keyBindPlayerList,
            this.keyBindPickBlock,
            this.keyBindCommand,
            this.keyBindScreenshot,
            this.keyBindTogglePerspective,
            this.keyBindSmoothCamera,
            this.keyBindFullscreen,
            this.keyBindSpectatorOutlines,
            this.keyBindSwapHands,
            this.keyBindSaveToolbar,
            this.keyBindLoadToolbar,
            this.keyBindAdvancements
         },
         this.keyBindsHotbar
      );
      this.difficulty = EnumDifficulty.NORMAL;
      this.lastServer = "";
      this.fovSetting = 70.0F;
      this.language = "en_us";
   }

   public static String getKeyDisplayString(int key) {
      if (key < 0) {
         switch (key) {
            case -100:
               return I18n.format("key.mouse.left");
            case -99:
               return I18n.format("key.mouse.right");
            case -98:
               return I18n.format("key.mouse.middle");
            default:
               return I18n.format("key.mouseButton", key + 101);
         }
      } else {
         return key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char)(key - 256)).toUpperCase();
      }
   }

   public static boolean isKeyDown(KeyBinding key) {
      int i = key.getKeyCode();
      if (i != 0 && i < 256) {
         return i < 0 ? Mouse.isButtonDown(i + 100) : Keyboard.isKeyDown(i);
      } else {
         return false;
      }
   }

   public void setOptionKeyBinding(KeyBinding key, int keyCode) {
      key.setKeyCode(keyCode);
      this.saveOptions();
   }

   public void setOptionFloatValue(GameSettings.Options settingsOption, float value) {
      this.setOptionFloatValueOF(settingsOption, value);
      if (settingsOption == GameSettings.Options.SENSITIVITY) {
         this.mouseSensitivity = value;
      }

      if (settingsOption == GameSettings.Options.FOV) {
         this.fovSetting = value;
      }

      if (settingsOption == GameSettings.Options.GAMMA) {
         this.gammaSetting = value;
      }

      if (settingsOption == GameSettings.Options.FRAMERATE_LIMIT) {
         this.limitFramerate = (int)value;
         this.enableVsync = false;
         if (this.limitFramerate <= 0) {
            this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
            this.enableVsync = true;
         }

         this.updateVSync();
      }

      if (settingsOption == GameSettings.Options.CHAT_OPACITY) {
         this.chatOpacity = value;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (settingsOption == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
         this.chatHeightFocused = value;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (settingsOption == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
         this.chatHeightUnfocused = value;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (settingsOption == GameSettings.Options.CHAT_WIDTH) {
         this.chatWidth = value;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (settingsOption == GameSettings.Options.CHAT_SCALE) {
         this.chatScale = value;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (settingsOption == GameSettings.Options.MIPMAP_LEVELS) {
         int i = this.mipmapLevels;
         this.mipmapLevels = (int)value;
         if (i != value) {
            this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
            this.mc.scheduleResourcesRefresh();
         }
      }

      if (settingsOption == GameSettings.Options.RENDER_DISTANCE) {
         this.renderDistanceChunks = (int)value;
         this.mc.renderGlobal.setDisplayListEntitiesDirty();
      }
   }

   public void setOptionValue(GameSettings.Options settingsOption, int value) {
      this.setOptionValueOF(settingsOption, value);
      if (settingsOption == GameSettings.Options.RENDER_DISTANCE) {
         this.setOptionFloatValue(
            settingsOption, MathHelper.clamp((float)(this.renderDistanceChunks + value), settingsOption.getValueMin(), settingsOption.getValueMax())
         );
      }

      if (settingsOption == GameSettings.Options.MAIN_HAND) {
         this.mainHand = this.mainHand.opposite();
      }

      if (settingsOption == GameSettings.Options.INVERT_MOUSE) {
         this.invertMouse = !this.invertMouse;
      }

      if (settingsOption == GameSettings.Options.GUI_SCALE) {
         this.guiScale += value;
         if (GuiScreen.isShiftKeyDown()) {
            this.guiScale = 0;
         }

         DisplayMode mode = Config.getLargestDisplayMode();
         int maxScaleWidth = mode.getWidth() / 320;
         int maxScaleHeight = mode.getHeight() / 240;
         int maxGuiScale = Math.min(maxScaleWidth, maxScaleHeight);
         if (this.guiScale < 0) {
            this.guiScale = maxGuiScale - 1;
         }

         if (this.mc.isUnicode() && this.guiScale % 2 != 0) {
            this.guiScale += value;
         }

         if (this.guiScale < 0 || this.guiScale >= maxGuiScale) {
            this.guiScale = 0;
         }
      }

      if (settingsOption == GameSettings.Options.PARTICLES) {
         this.particleSetting = (this.particleSetting + value) % 3;
      }

      if (settingsOption == GameSettings.Options.VIEW_BOBBING) {
         this.viewBobbing = !this.viewBobbing;
      }

      if (settingsOption == GameSettings.Options.RENDER_CLOUDS) {
         this.clouds = (this.clouds + value) % 3;
      }

      if (settingsOption == GameSettings.Options.FORCE_UNICODE_FONT) {
         this.forceUnicodeFont = !this.forceUnicodeFont;
         this.mc.fontRenderer.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
      }

      if (settingsOption == GameSettings.Options.FBO_ENABLE) {
         this.fboEnable = !this.fboEnable;
      }

      if (settingsOption == GameSettings.Options.ANAGLYPH) {
         if (!this.anaglyph && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
            return;
         }

         this.anaglyph = !this.anaglyph;
         this.mc.refreshResources();
         if (Reflector.FMLClientHandler_refreshResources.exists()) {
            Object instance = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
            IResourceType type = (IResourceType)Reflector.VanillaResourceType_TEXTURES.getValue();
            Reflector.call(instance, Reflector.FMLClientHandler_refreshResources, new IResourceType[]{type});
         }
      }

      if (settingsOption == GameSettings.Options.GRAPHICS) {
         this.fancyGraphics = !this.fancyGraphics;
         this.updateRenderClouds();
         this.mc.renderGlobal.loadRenderers();
      }

      if (settingsOption == GameSettings.Options.AMBIENT_OCCLUSION) {
         this.ambientOcclusion = (this.ambientOcclusion + value) % 3;
         this.mc.renderGlobal.loadRenderers();
      }

      if (settingsOption == GameSettings.Options.CHAT_VISIBILITY) {
         this.chatVisibility = EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + value) % 3);
      }

      if (settingsOption == GameSettings.Options.CHAT_COLOR) {
         this.chatColours = !this.chatColours;
      }

      if (settingsOption == GameSettings.Options.CHAT_LINKS) {
         this.chatLinks = !this.chatLinks;
      }

      if (settingsOption == GameSettings.Options.CHAT_LINKS_PROMPT) {
         this.chatLinksPrompt = !this.chatLinksPrompt;
      }

      if (settingsOption == GameSettings.Options.SNOOPER_ENABLED) {
         this.snooperEnabled = !this.snooperEnabled;
      }

      if (settingsOption == GameSettings.Options.TOUCHSCREEN) {
         this.touchscreen = !this.touchscreen;
      }

      if (settingsOption == GameSettings.Options.USE_FULLSCREEN) {
         this.fullScreen = !this.fullScreen;
         if (this.mc.isFullScreen() != this.fullScreen) {
            this.mc.toggleFullscreen();
         }
      }

      if (settingsOption == GameSettings.Options.ENABLE_VSYNC) {
         this.enableVsync = !this.enableVsync;
         Display.setVSyncEnabled(this.enableVsync);
      }

      if (settingsOption == GameSettings.Options.USE_VBO) {
         this.useVbo = !this.useVbo;
         this.mc.renderGlobal.loadRenderers();
      }

      if (settingsOption == GameSettings.Options.REDUCED_DEBUG_INFO) {
         this.reducedDebugInfo = !this.reducedDebugInfo;
      }

      if (settingsOption == GameSettings.Options.ENTITY_SHADOWS) {
         this.entityShadows = !this.entityShadows;
      }

      if (settingsOption == GameSettings.Options.ATTACK_INDICATOR) {
         this.attackIndicator = (this.attackIndicator + value) % 3;
      }

      if (settingsOption == GameSettings.Options.SHOW_SUBTITLES) {
         this.showSubtitles = !this.showSubtitles;
      }

      if (settingsOption == GameSettings.Options.REALMS_NOTIFICATIONS) {
         this.realmsNotifications = !this.realmsNotifications;
      }

      if (settingsOption == GameSettings.Options.AUTO_JUMP) {
         this.autoJump = !this.autoJump;
      }

      if (settingsOption == GameSettings.Options.NARRATOR) {
         if (NarratorChatListener.INSTANCE.isActive()) {
            this.narrator = (this.narrator + value) % NARRATOR_MODES.length;
         } else {
            this.narrator = 0;
         }

         NarratorChatListener.INSTANCE.announceMode(this.narrator);
      }

      this.saveOptions();
   }

   public float getOptionFloatValue(GameSettings.Options settingOption) {
      float valOF = this.getOptionFloatValueOF(settingOption);
      if (valOF != Float.MAX_VALUE) {
         return valOF;
      } else if (settingOption == GameSettings.Options.FOV) {
         return this.fovSetting;
      } else if (settingOption == GameSettings.Options.GAMMA) {
         return this.gammaSetting;
      } else if (settingOption == GameSettings.Options.SATURATION) {
         return this.saturation;
      } else if (settingOption == GameSettings.Options.SENSITIVITY) {
         return this.mouseSensitivity;
      } else if (settingOption == GameSettings.Options.CHAT_OPACITY) {
         return this.chatOpacity;
      } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
         return this.chatHeightFocused;
      } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
         return this.chatHeightUnfocused;
      } else if (settingOption == GameSettings.Options.CHAT_SCALE) {
         return this.chatScale;
      } else if (settingOption == GameSettings.Options.CHAT_WIDTH) {
         return this.chatWidth;
      } else if (settingOption == GameSettings.Options.FRAMERATE_LIMIT) {
         return this.limitFramerate;
      } else if (settingOption == GameSettings.Options.MIPMAP_LEVELS) {
         return this.mipmapLevels;
      } else {
         return settingOption == GameSettings.Options.RENDER_DISTANCE ? this.renderDistanceChunks : 0.0F;
      }
   }

   public boolean getOptionOrdinalValue(GameSettings.Options settingOption) {
      switch (settingOption) {
         case INVERT_MOUSE:
            return this.invertMouse;
         case VIEW_BOBBING:
            return this.viewBobbing;
         case ANAGLYPH:
            return this.anaglyph;
         case FBO_ENABLE:
            return this.fboEnable;
         case CHAT_COLOR:
            return this.chatColours;
         case CHAT_LINKS:
            return this.chatLinks;
         case CHAT_LINKS_PROMPT:
            return this.chatLinksPrompt;
         case SNOOPER_ENABLED:
            return this.snooperEnabled;
         case USE_FULLSCREEN:
            return this.fullScreen;
         case ENABLE_VSYNC:
            return this.enableVsync;
         case USE_VBO:
            return this.useVbo;
         case TOUCHSCREEN:
            return this.touchscreen;
         case FORCE_UNICODE_FONT:
            return this.forceUnicodeFont;
         case REDUCED_DEBUG_INFO:
            return this.reducedDebugInfo;
         case ENTITY_SHADOWS:
            return this.entityShadows;
         case SHOW_SUBTITLES:
            return this.showSubtitles;
         case REALMS_NOTIFICATIONS:
            return this.realmsNotifications;
         case ENABLE_WEAK_ATTACKS:
            return this.enableWeakAttacks;
         case AUTO_JUMP:
            return this.autoJump;
         default:
            return false;
      }
   }

   private static String getTranslation(String[] strArray, int index) {
      if (index < 0 || index >= strArray.length) {
         index = 0;
      }

      return I18n.format(strArray[index]);
   }

   public String getKeyBinding(GameSettings.Options settingOption) {
      String strOF = this.getKeyBindingOF(settingOption);
      if (strOF != null) {
         return strOF;
      } else {
         String s = I18n.format(settingOption.getTranslation()) + ": ";
         if (settingOption.isFloat()) {
            float f1 = this.getOptionFloatValue(settingOption);
            float f = settingOption.normalizeValue(f1);
            if (settingOption == GameSettings.Options.SENSITIVITY) {
               if (f == 0.0F) {
                  return s + I18n.format("options.sensitivity.min");
               } else {
                  return f == 1.0F ? s + I18n.format("options.sensitivity.max") : s + (int)(f * 200.0F) + "%";
               }
            } else if (settingOption == GameSettings.Options.FOV) {
               if (f1 == 70.0F) {
                  return s + I18n.format("options.fov.min");
               } else {
                  return f1 == 110.0F ? s + I18n.format("options.fov.max") : s + (int)f1;
               }
            } else if (settingOption == GameSettings.Options.FRAMERATE_LIMIT) {
               return f1 == settingOption.valueMax ? s + I18n.format("options.framerateLimit.max") : s + I18n.format("options.framerate", (int)f1);
            } else if (settingOption == GameSettings.Options.RENDER_CLOUDS) {
               return f1 == settingOption.valueMin ? s + I18n.format("options.cloudHeight.min") : s + ((int)f1 + 128);
            } else if (settingOption == GameSettings.Options.GAMMA) {
               if (f == 0.0F) {
                  return s + I18n.format("options.gamma.min");
               } else {
                  return f == 1.0F ? s + I18n.format("options.gamma.max") : s + "+" + (int)(f * 100.0F) + "%";
               }
            } else if (settingOption == GameSettings.Options.SATURATION) {
               return s + (int)(f * 400.0F) + "%";
            } else if (settingOption == GameSettings.Options.CHAT_OPACITY) {
               return s + (int)(f * 90.0F + 10.0F) + "%";
            } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
               return s + GuiNewChat.calculateChatboxHeight(f) + "px";
            } else if (settingOption == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
               return s + GuiNewChat.calculateChatboxHeight(f) + "px";
            } else if (settingOption == GameSettings.Options.CHAT_WIDTH) {
               return s + GuiNewChat.calculateChatboxWidth(f) + "px";
            } else if (settingOption == GameSettings.Options.RENDER_DISTANCE) {
               return s + I18n.format("options.chunks", (int)f1);
            } else if (settingOption == GameSettings.Options.MIPMAP_LEVELS) {
               if (f1 >= 4.0) {
                  return s + Lang.get("of.general.max");
               } else {
                  return f1 == 0.0F ? s + I18n.format("options.off") : s + (int)f1;
               }
            } else {
               return f == 0.0F ? s + I18n.format("options.off") : s + (int)(f * 100.0F) + "%";
            }
         } else if (settingOption.isBoolean()) {
            boolean flag = this.getOptionOrdinalValue(settingOption);
            return flag ? s + I18n.format("options.on") : s + I18n.format("options.off");
         } else if (settingOption == GameSettings.Options.MAIN_HAND) {
            return s + this.mainHand;
         } else if (settingOption == GameSettings.Options.GUI_SCALE) {
            return this.guiScale >= GUISCALES.length ? s + this.guiScale + "x" : s + getTranslation(GUISCALES, this.guiScale);
         } else if (settingOption == GameSettings.Options.CHAT_VISIBILITY) {
            return s + I18n.format(this.chatVisibility.getResourceKey());
         } else if (settingOption == GameSettings.Options.PARTICLES) {
            return s + getTranslation(PARTICLES, this.particleSetting);
         } else if (settingOption == GameSettings.Options.AMBIENT_OCCLUSION) {
            return s + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
         } else if (settingOption == GameSettings.Options.RENDER_CLOUDS) {
            return s + getTranslation(CLOUDS_TYPES, this.clouds);
         } else if (settingOption == GameSettings.Options.GRAPHICS) {
            if (this.fancyGraphics) {
               return s + I18n.format("options.graphics.fancy");
            } else {
               String s1 = "options.graphics.fast";
               return s + I18n.format("options.graphics.fast");
            }
         } else if (settingOption == GameSettings.Options.ATTACK_INDICATOR) {
            return s + getTranslation(ATTACK_INDICATORS, this.attackIndicator);
         } else if (settingOption == GameSettings.Options.NARRATOR) {
            return NarratorChatListener.INSTANCE.isActive()
               ? s + getTranslation(NARRATOR_MODES, this.narrator)
               : s + I18n.format("options.narrator.notavailable");
         } else {
            return s;
         }
      }
   }

   public void loadOptions() {
      FileInputStream is = null;

      label542: {
         try {
            if (this.optionsFile.exists()) {
               this.soundLevels.clear();
               List<String> list = IOUtils.readLines(is = new FileInputStream(this.optionsFile), StandardCharsets.UTF_8);
               NBTTagCompound nbttagcompound = new NBTTagCompound();

               for (String s : list) {
                  try {
                     Iterator<String> iterator = COLON_SPLITTER.omitEmptyStrings().limit(2).split(s).iterator();
                     nbttagcompound.setString(iterator.next(), iterator.next());
                  } catch (Exception var18) {
                     LOGGER.warn("Skipping bad option: {}", s);
                  }
               }

               nbttagcompound = this.dataFix(nbttagcompound);

               for (String s1 : nbttagcompound.getKeySet()) {
                  String s2 = nbttagcompound.getString(s1);

                  try {
                     if ("mouseSensitivity".equals(s1)) {
                        this.mouseSensitivity = this.parseFloat(s2);
                     }

                     if ("fov".equals(s1)) {
                        this.fovSetting = this.parseFloat(s2) * 40.0F + 70.0F;
                     }

                     if ("gamma".equals(s1)) {
                        this.gammaSetting = this.parseFloat(s2);
                     }

                     if ("saturation".equals(s1)) {
                        this.saturation = this.parseFloat(s2);
                     }

                     if ("invertYMouse".equals(s1)) {
                        this.invertMouse = "true".equals(s2);
                     }

                     if ("renderDistance".equals(s1)) {
                        this.renderDistanceChunks = Integer.parseInt(s2);
                     }

                     if ("guiScale".equals(s1)) {
                        this.guiScale = Integer.parseInt(s2);
                     }

                     if ("particles".equals(s1)) {
                        this.particleSetting = Integer.parseInt(s2);
                     }

                     if ("bobView".equals(s1)) {
                        this.viewBobbing = "true".equals(s2);
                     }

                     if ("anaglyph3d".equals(s1)) {
                        this.anaglyph = "true".equals(s2);
                     }

                     if ("maxFps".equals(s1)) {
                        this.limitFramerate = Integer.parseInt(s2);
                        if (this.enableVsync) {
                           this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                        }

                        if (this.limitFramerate <= 0) {
                           this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                        }
                     }

                     if ("fboEnable".equals(s1)) {
                        this.fboEnable = "true".equals(s2);
                     }

                     if ("difficulty".equals(s1)) {
                        this.difficulty = EnumDifficulty.byId(Integer.parseInt(s2));
                     }

                     if ("fancyGraphics".equals(s1)) {
                        this.fancyGraphics = "true".equals(s2);
                        this.updateRenderClouds();
                     }

                     if ("tutorialStep".equals(s1)) {
                        this.tutorialStep = TutorialSteps.getTutorial(s2);
                     }

                     if ("ao".equals(s1)) {
                        if ("true".equals(s2)) {
                           this.ambientOcclusion = 2;
                        } else if ("false".equals(s2)) {
                           this.ambientOcclusion = 0;
                        } else {
                           this.ambientOcclusion = Integer.parseInt(s2);
                        }
                     }

                     if ("renderClouds".equals(s1)) {
                        if ("true".equals(s2)) {
                           this.clouds = 2;
                        } else if ("false".equals(s2)) {
                           this.clouds = 0;
                        } else if ("fast".equals(s2)) {
                           this.clouds = 1;
                        }
                     }

                     if ("attackIndicator".equals(s1)) {
                        if ("0".equals(s2)) {
                           this.attackIndicator = 0;
                        } else if ("1".equals(s2)) {
                           this.attackIndicator = 1;
                        } else if ("2".equals(s2)) {
                           this.attackIndicator = 2;
                        }
                     }

                     if ("resourcePacks".equals(s1)) {
                        this.resourcePacks = (List<String>)JsonUtils.gsonDeserialize(GSON, s2, TYPE_LIST_STRING);
                        if (this.resourcePacks == null) {
                           this.resourcePacks = Lists.newArrayList();
                        }
                     }

                     if ("incompatibleResourcePacks".equals(s1)) {
                        this.incompatibleResourcePacks = (List<String>)JsonUtils.gsonDeserialize(GSON, s2, TYPE_LIST_STRING);
                        if (this.incompatibleResourcePacks == null) {
                           this.incompatibleResourcePacks = Lists.newArrayList();
                        }
                     }

                     if ("lastServer".equals(s1)) {
                        this.lastServer = s2;
                     }

                     if ("lang".equals(s1)) {
                        this.language = s2;
                     }

                     if ("chatVisibility".equals(s1)) {
                        this.chatVisibility = EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(s2));
                     }

                     if ("chatColors".equals(s1)) {
                        this.chatColours = "true".equals(s2);
                     }

                     if ("chatLinks".equals(s1)) {
                        this.chatLinks = "true".equals(s2);
                     }

                     if ("chatLinksPrompt".equals(s1)) {
                        this.chatLinksPrompt = "true".equals(s2);
                     }

                     if ("chatOpacity".equals(s1)) {
                        this.chatOpacity = this.parseFloat(s2);
                     }

                     if ("snooperEnabled".equals(s1)) {
                        this.snooperEnabled = "true".equals(s2);
                     }

                     if ("fullscreen".equals(s1)) {
                        this.fullScreen = "true".equals(s2);
                     }

                     if ("enableVsync".equals(s1)) {
                        this.enableVsync = "true".equals(s2);
                        if (this.enableVsync) {
                           this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
                        }

                        this.updateVSync();
                     }

                     if ("useVbo".equals(s1)) {
                        this.useVbo = "true".equals(s2);
                     }

                     if ("hideServerAddress".equals(s1)) {
                        this.hideServerAddress = "true".equals(s2);
                     }

                     if ("advancedItemTooltips".equals(s1)) {
                        this.advancedItemTooltips = "true".equals(s2);
                     }

                     if ("pauseOnLostFocus".equals(s1)) {
                        this.pauseOnLostFocus = "true".equals(s2);
                     }

                     if ("touchscreen".equals(s1)) {
                        this.touchscreen = "true".equals(s2);
                     }

                     if ("overrideHeight".equals(s1)) {
                        this.overrideHeight = Integer.parseInt(s2);
                     }

                     if ("overrideWidth".equals(s1)) {
                        this.overrideWidth = Integer.parseInt(s2);
                     }

                     if ("heldItemTooltips".equals(s1)) {
                        this.heldItemTooltips = "true".equals(s2);
                     }

                     if ("chatHeightFocused".equals(s1)) {
                        this.chatHeightFocused = this.parseFloat(s2);
                     }

                     if ("chatHeightUnfocused".equals(s1)) {
                        this.chatHeightUnfocused = this.parseFloat(s2);
                     }

                     if ("chatScale".equals(s1)) {
                        this.chatScale = this.parseFloat(s2);
                     }

                     if ("chatWidth".equals(s1)) {
                        this.chatWidth = this.parseFloat(s2);
                     }

                     if ("mipmapLevels".equals(s1)) {
                        this.mipmapLevels = Integer.parseInt(s2);
                     }

                     if ("forceUnicodeFont".equals(s1)) {
                        this.forceUnicodeFont = "true".equals(s2);
                     }

                     if ("reducedDebugInfo".equals(s1)) {
                        this.reducedDebugInfo = "true".equals(s2);
                     }

                     if ("useNativeTransport".equals(s1)) {
                        this.useNativeTransport = "true".equals(s2);
                     }

                     if ("entityShadows".equals(s1)) {
                        this.entityShadows = "true".equals(s2);
                     }

                     if ("mainHand".equals(s1)) {
                        this.mainHand = "left".equals(s2) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
                     }

                     if ("showSubtitles".equals(s1)) {
                        this.showSubtitles = "true".equals(s2);
                     }

                     if ("realmsNotifications".equals(s1)) {
                        this.realmsNotifications = "true".equals(s2);
                     }

                     if ("enableWeakAttacks".equals(s1)) {
                        this.enableWeakAttacks = "true".equals(s2);
                     }

                     if ("autoJump".equals(s1)) {
                        this.autoJump = "true".equals(s2);
                     }

                     if ("narrator".equals(s1)) {
                        this.narrator = Integer.parseInt(s2);
                     }

                     for (KeyBinding keybinding : this.keyBindings) {
                        if (s1.equals("key_" + keybinding.getKeyDescription())) {
                           if (Reflector.KeyModifier_valueFromString.exists()) {
                              if (s2.indexOf(58) != -1) {
                                 String[] t = s2.split(":");
                                 Object keyModifier = Reflector.call(Reflector.KeyModifier_valueFromString, new Object[]{t[1]});
                                 Reflector.call(keybinding, Reflector.ForgeKeyBinding_setKeyModifierAndCode, new Object[]{keyModifier, Integer.parseInt(t[0])});
                              } else {
                                 Object keyModifierNone = Reflector.getFieldValue(Reflector.KeyModifier_NONE);
                                 Reflector.call(
                                    keybinding, Reflector.ForgeKeyBinding_setKeyModifierAndCode, new Object[]{keyModifierNone, Integer.parseInt(s2)}
                                 );
                              }
                           } else {
                              keybinding.setKeyCode(Integer.parseInt(s2));
                           }
                        }
                     }

                     for (SoundCategory soundcategory : SoundCategory.values()) {
                        if (s1.equals("soundCategory_" + soundcategory.getName())) {
                           this.soundLevels.put(soundcategory, this.parseFloat(s2));
                        }
                     }

                     for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                        if (s1.equals("modelPart_" + enumplayermodelparts.getPartName())) {
                           this.setModelPartEnabled(enumplayermodelparts, "true".equals(s2));
                        }
                     }
                  } catch (Exception var19) {
                     LOGGER.warn("Skipping bad option: {}:{}", s1, s2);
                     var19.printStackTrace();
                  }
               }

               KeyBinding.resetKeyBindingArrayAndHash();
               break label542;
            }
         } catch (Exception var20) {
            LOGGER.error("Failed to load options", var20);
            break label542;
         } finally {
            IOUtils.closeQuietly(is);
         }

         return;
      }

      this.loadOfOptions();
   }

   private NBTTagCompound dataFix(NBTTagCompound p_189988_1_) {
      int i = 0;

      try {
         i = Integer.parseInt(p_189988_1_.getString("version"));
      } catch (RuntimeException var4) {
      }

      return this.mc.getDataFixer().process(FixTypes.OPTIONS, p_189988_1_, i);
   }

   private float parseFloat(String str) {
      if ("true".equals(str)) {
         return 1.0F;
      } else {
         return "false".equals(str) ? 0.0F : Float.parseFloat(str);
      }
   }

   public void saveOptions() {
      if (Reflector.FMLClientHandler.exists()) {
         Object fml = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
         if (fml != null && Reflector.callBoolean(fml, Reflector.FMLClientHandler_isLoading, new Object[0])) {
            return;
         }
      }

      PrintWriter printwriter = null;

      try {
         printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
         printwriter.println("version:1343");
         printwriter.println("invertYMouse:" + this.invertMouse);
         printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
         printwriter.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
         printwriter.println("gamma:" + this.gammaSetting);
         printwriter.println("saturation:" + this.saturation);
         printwriter.println("renderDistance:" + this.renderDistanceChunks);
         printwriter.println("guiScale:" + this.guiScale);
         printwriter.println("particles:" + this.particleSetting);
         printwriter.println("bobView:" + this.viewBobbing);
         printwriter.println("anaglyph3d:" + this.anaglyph);
         printwriter.println("maxFps:" + this.limitFramerate);
         printwriter.println("fboEnable:" + this.fboEnable);
         printwriter.println("difficulty:" + this.difficulty.getId());
         printwriter.println("fancyGraphics:" + this.fancyGraphics);
         printwriter.println("ao:" + this.ambientOcclusion);
         switch (this.clouds) {
            case 0:
               printwriter.println("renderClouds:false");
               break;
            case 1:
               printwriter.println("renderClouds:fast");
               break;
            case 2:
               printwriter.println("renderClouds:true");
         }

         printwriter.println("resourcePacks:" + GSON.toJson(this.resourcePacks));
         printwriter.println("incompatibleResourcePacks:" + GSON.toJson(this.incompatibleResourcePacks));
         printwriter.println("lastServer:" + this.lastServer);
         printwriter.println("lang:" + this.language);
         printwriter.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
         printwriter.println("chatColors:" + this.chatColours);
         printwriter.println("chatLinks:" + this.chatLinks);
         printwriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
         printwriter.println("chatOpacity:" + this.chatOpacity);
         printwriter.println("snooperEnabled:" + this.snooperEnabled);
         printwriter.println("fullscreen:" + this.fullScreen);
         printwriter.println("enableVsync:" + this.enableVsync);
         printwriter.println("useVbo:" + this.useVbo);
         printwriter.println("hideServerAddress:" + this.hideServerAddress);
         printwriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
         printwriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
         printwriter.println("touchscreen:" + this.touchscreen);
         printwriter.println("overrideWidth:" + this.overrideWidth);
         printwriter.println("overrideHeight:" + this.overrideHeight);
         printwriter.println("heldItemTooltips:" + this.heldItemTooltips);
         printwriter.println("chatHeightFocused:" + this.chatHeightFocused);
         printwriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
         printwriter.println("chatScale:" + this.chatScale);
         printwriter.println("chatWidth:" + this.chatWidth);
         printwriter.println("mipmapLevels:" + this.mipmapLevels);
         printwriter.println("forceUnicodeFont:" + this.forceUnicodeFont);
         printwriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
         printwriter.println("useNativeTransport:" + this.useNativeTransport);
         printwriter.println("entityShadows:" + this.entityShadows);
         printwriter.println("mainHand:" + (this.mainHand == EnumHandSide.LEFT ? "left" : "right"));
         printwriter.println("attackIndicator:" + this.attackIndicator);
         printwriter.println("showSubtitles:" + this.showSubtitles);
         printwriter.println("realmsNotifications:" + this.realmsNotifications);
         printwriter.println("enableWeakAttacks:" + this.enableWeakAttacks);
         printwriter.println("autoJump:" + this.autoJump);
         printwriter.println("narrator:" + this.narrator);
         printwriter.println("tutorialStep:" + this.tutorialStep.getName());

         for (KeyBinding keybinding : this.keyBindings) {
            if (Reflector.ForgeKeyBinding_getKeyModifier.exists()) {
               String keyString = "key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode();
               Object keyModifier = Reflector.call(keybinding, Reflector.ForgeKeyBinding_getKeyModifier, new Object[0]);
               Object keyModifierNone = Reflector.getFieldValue(Reflector.KeyModifier_NONE);
               printwriter.println(keyModifier != keyModifierNone ? keyString + ":" + keyModifier : keyString);
            } else {
               printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
            }
         }

         for (SoundCategory soundcategory : SoundCategory.values()) {
            printwriter.println("soundCategory_" + soundcategory.getName() + ":" + this.getSoundLevel(soundcategory));
         }

         for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
            printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + this.setModelParts.contains(enumplayermodelparts));
         }
      } catch (Exception var12) {
         LOGGER.error("Failed to save options", var12);
      } finally {
         IOUtils.closeQuietly(printwriter);
      }

      this.saveOfOptions();
      this.sendSettingsToServer();
   }

   public float getSoundLevel(SoundCategory category) {
      return this.soundLevels.containsKey(category) ? this.soundLevels.get(category) : 1.0F;
   }

   public void setSoundLevel(SoundCategory category, float volume) {
      this.mc.getSoundHandler().setSoundLevel(category, volume);
      this.soundLevels.put(category, volume);
   }

   public void sendSettingsToServer() {
      if (this.mc.player != null) {
         int i = 0;

         for (EnumPlayerModelParts enumplayermodelparts : this.setModelParts) {
            i |= enumplayermodelparts.getPartMask();
         }

         this.mc
            .player
            .connection
            .sendPacket(new CPacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i, this.mainHand));
      }
   }

   public Set<EnumPlayerModelParts> getModelParts() {
      return ImmutableSet.copyOf(this.setModelParts);
   }

   public void setModelPartEnabled(EnumPlayerModelParts modelPart, boolean enable) {
      if (enable) {
         this.setModelParts.add(modelPart);
      } else {
         this.setModelParts.remove(modelPart);
      }

      this.sendSettingsToServer();
   }

   public void switchModelPartEnabled(EnumPlayerModelParts modelPart) {
      if (this.getModelParts().contains(modelPart)) {
         this.setModelParts.remove(modelPart);
      } else {
         this.setModelParts.add(modelPart);
      }

      this.sendSettingsToServer();
   }

   public int shouldRenderClouds() {
      return this.renderDistanceChunks >= 4 ? this.clouds : 0;
   }

   public boolean isUsingNativeTransport() {
      return this.useNativeTransport;
   }

   private void setOptionFloatValueOF(GameSettings.Options option, float val) {
      if (option == GameSettings.Options.CLOUD_HEIGHT) {
         this.ofCloudsHeight = val;
         this.mc.renderGlobal.resetClouds();
      }

      if (option == GameSettings.Options.AO_LEVEL) {
         this.ofAoLevel = val;
         this.mc.renderGlobal.loadRenderers();
      }

      if (option == GameSettings.Options.AA_LEVEL) {
         int valInt = (int)val;
         if (valInt > 0 && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
            return;
         }

         int[] aaLevels = new int[]{0, 2, 4, 6, 8, 12, 16};
         this.ofAaLevel = 0;

         for (int l = 0; l < aaLevels.length; l++) {
            if (valInt >= aaLevels[l]) {
               this.ofAaLevel = aaLevels[l];
            }
         }

         this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
      }

      if (option == GameSettings.Options.AF_LEVEL) {
         int valInt = (int)val;
         if (valInt > 1 && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
            return;
         }

         this.ofAfLevel = 1;

         while (this.ofAfLevel * 2 <= valInt) {
            this.ofAfLevel *= 2;
         }

         this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
         this.mc.refreshResources();
      }

      if (option == GameSettings.Options.MIPMAP_TYPE) {
         int valInt = (int)val;
         this.ofMipmapType = Config.limit(valInt, 0, 3);
         this.mc.refreshResources();
      }

      if (option == GameSettings.Options.FULLSCREEN_MODE) {
         int index = (int)val - 1;
         String[] modeNames = Config.getDisplayModeNames();
         if (index < 0 || index >= modeNames.length) {
            this.ofFullscreenMode = "Default";
            return;
         }

         this.ofFullscreenMode = modeNames[index];
      }
   }

   private float getOptionFloatValueOF(GameSettings.Options settingOption) {
      if (settingOption == GameSettings.Options.CLOUD_HEIGHT) {
         return this.ofCloudsHeight;
      } else if (settingOption == GameSettings.Options.AO_LEVEL) {
         return this.ofAoLevel;
      } else if (settingOption == GameSettings.Options.AA_LEVEL) {
         return this.ofAaLevel;
      } else if (settingOption == GameSettings.Options.AF_LEVEL) {
         return this.ofAfLevel;
      } else if (settingOption == GameSettings.Options.MIPMAP_TYPE) {
         return this.ofMipmapType;
      } else if (settingOption == GameSettings.Options.FRAMERATE_LIMIT) {
         return this.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync ? 0.0F : this.limitFramerate;
      } else if (settingOption == GameSettings.Options.FULLSCREEN_MODE) {
         if (this.ofFullscreenMode.equals("Default")) {
            return 0.0F;
         } else {
            List modeList = Arrays.asList(Config.getDisplayModeNames());
            int index = modeList.indexOf(this.ofFullscreenMode);
            return index < 0 ? 0.0F : index + 1;
         }
      } else {
         return Float.MAX_VALUE;
      }
   }

   private void setOptionValueOF(GameSettings.Options par1EnumOptions, int par2) {
      if (par1EnumOptions == GameSettings.Options.FOG_FANCY) {
         switch (this.ofFogType) {
            case 1:
               this.ofFogType = 2;
               if (!Config.isFancyFogAvailable()) {
                  this.ofFogType = 3;
               }
               break;
            case 2:
               this.ofFogType = 3;
               break;
            case 3:
               this.ofFogType = 1;
               break;
            default:
               this.ofFogType = 1;
         }
      }

      if (par1EnumOptions == GameSettings.Options.FOG_START) {
         this.ofFogStart += 0.2F;
         if (this.ofFogStart > 0.81F) {
            this.ofFogStart = 0.2F;
         }
      }

      if (par1EnumOptions == GameSettings.Options.SMOOTH_FPS) {
         this.ofSmoothFps = !this.ofSmoothFps;
      }

      if (par1EnumOptions == GameSettings.Options.SMOOTH_WORLD) {
         this.ofSmoothWorld = !this.ofSmoothWorld;
         Config.updateThreadPriorities();
      }

      if (par1EnumOptions == GameSettings.Options.CLOUDS) {
         this.ofClouds++;
         if (this.ofClouds > 3) {
            this.ofClouds = 0;
         }

         this.updateRenderClouds();
         this.mc.renderGlobal.resetClouds();
      }

      if (par1EnumOptions == GameSettings.Options.TREES) {
         this.ofTrees = nextValue(this.ofTrees, OF_TREES_VALUES);
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.DROPPED_ITEMS) {
         this.ofDroppedItems++;
         if (this.ofDroppedItems > 2) {
            this.ofDroppedItems = 0;
         }
      }

      if (par1EnumOptions == GameSettings.Options.RAIN) {
         this.ofRain++;
         if (this.ofRain > 3) {
            this.ofRain = 0;
         }
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_WATER) {
         this.ofAnimatedWater++;
         if (this.ofAnimatedWater == 1) {
            this.ofAnimatedWater++;
         }

         if (this.ofAnimatedWater > 2) {
            this.ofAnimatedWater = 0;
         }
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_LAVA) {
         this.ofAnimatedLava++;
         if (this.ofAnimatedLava == 1) {
            this.ofAnimatedLava++;
         }

         if (this.ofAnimatedLava > 2) {
            this.ofAnimatedLava = 0;
         }
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_FIRE) {
         this.ofAnimatedFire = !this.ofAnimatedFire;
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_PORTAL) {
         this.ofAnimatedPortal = !this.ofAnimatedPortal;
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_REDSTONE) {
         this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_EXPLOSION) {
         this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_FLAME) {
         this.ofAnimatedFlame = !this.ofAnimatedFlame;
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_SMOKE) {
         this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
      }

      if (par1EnumOptions == GameSettings.Options.VOID_PARTICLES) {
         this.ofVoidParticles = !this.ofVoidParticles;
      }

      if (par1EnumOptions == GameSettings.Options.WATER_PARTICLES) {
         this.ofWaterParticles = !this.ofWaterParticles;
      }

      if (par1EnumOptions == GameSettings.Options.PORTAL_PARTICLES) {
         this.ofPortalParticles = !this.ofPortalParticles;
      }

      if (par1EnumOptions == GameSettings.Options.POTION_PARTICLES) {
         this.ofPotionParticles = !this.ofPotionParticles;
      }

      if (par1EnumOptions == GameSettings.Options.FIREWORK_PARTICLES) {
         this.ofFireworkParticles = !this.ofFireworkParticles;
      }

      if (par1EnumOptions == GameSettings.Options.DRIPPING_WATER_LAVA) {
         this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_TERRAIN) {
         this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
      }

      if (par1EnumOptions == GameSettings.Options.ANIMATED_TEXTURES) {
         this.ofAnimatedTextures = !this.ofAnimatedTextures;
      }

      if (par1EnumOptions == GameSettings.Options.RAIN_SPLASH) {
         this.ofRainSplash = !this.ofRainSplash;
      }

      if (par1EnumOptions == GameSettings.Options.LAGOMETER) {
         this.ofLagometer = !this.ofLagometer;
      }

      if (par1EnumOptions == GameSettings.Options.SHOW_FPS) {
         this.ofShowFps = !this.ofShowFps;
      }

      if (par1EnumOptions == GameSettings.Options.AUTOSAVE_TICKS) {
         int step = 900;
         this.ofAutoSaveTicks = Math.max(this.ofAutoSaveTicks / step * step, step);
         this.ofAutoSaveTicks *= 2;
         if (this.ofAutoSaveTicks > 32 * step) {
            this.ofAutoSaveTicks = step;
         }
      }

      if (par1EnumOptions == GameSettings.Options.BETTER_GRASS) {
         this.ofBetterGrass++;
         if (this.ofBetterGrass > 3) {
            this.ofBetterGrass = 1;
         }

         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.CONNECTED_TEXTURES) {
         this.ofConnectedTextures++;
         if (this.ofConnectedTextures > 3) {
            this.ofConnectedTextures = 1;
         }

         if (this.ofConnectedTextures == 2) {
            this.mc.renderGlobal.loadRenderers();
         } else {
            this.mc.refreshResources();
         }
      }

      if (par1EnumOptions == GameSettings.Options.WEATHER) {
         this.ofWeather = !this.ofWeather;
      }

      if (par1EnumOptions == GameSettings.Options.SKY) {
         this.ofSky = !this.ofSky;
      }

      if (par1EnumOptions == GameSettings.Options.STARS) {
         this.ofStars = !this.ofStars;
      }

      if (par1EnumOptions == GameSettings.Options.SUN_MOON) {
         this.ofSunMoon = !this.ofSunMoon;
      }

      if (par1EnumOptions == GameSettings.Options.VIGNETTE) {
         this.ofVignette++;
         if (this.ofVignette > 2) {
            this.ofVignette = 0;
         }
      }

      if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES) {
         this.ofChunkUpdates++;
         if (this.ofChunkUpdates > 5) {
            this.ofChunkUpdates = 1;
         }
      }

      if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
         this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
      }

      if (par1EnumOptions == GameSettings.Options.TIME) {
         this.ofTime++;
         if (this.ofTime > 2) {
            this.ofTime = 0;
         }
      }

      if (par1EnumOptions == GameSettings.Options.CLEAR_WATER) {
         this.ofClearWater = !this.ofClearWater;
         this.updateWaterOpacity();
      }

      if (par1EnumOptions == GameSettings.Options.PROFILER) {
         this.ofProfiler = !this.ofProfiler;
      }

      if (par1EnumOptions == GameSettings.Options.BETTER_SNOW) {
         this.ofBetterSnow = !this.ofBetterSnow;
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.SWAMP_COLORS) {
         this.ofSwampColors = !this.ofSwampColors;
         CustomColors.updateUseDefaultGrassFoliageColors();
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.RANDOM_ENTITIES) {
         this.ofRandomEntities = !this.ofRandomEntities;
         RandomEntities.update();
      }

      if (par1EnumOptions == GameSettings.Options.SMOOTH_BIOMES) {
         this.ofSmoothBiomes = !this.ofSmoothBiomes;
         CustomColors.updateUseDefaultGrassFoliageColors();
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.CUSTOM_FONTS) {
         this.ofCustomFonts = !this.ofCustomFonts;
         this.mc.fontRenderer.onResourceManagerReload(Config.getResourceManager());
         this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
      }

      if (par1EnumOptions == GameSettings.Options.CUSTOM_COLORS) {
         this.ofCustomColors = !this.ofCustomColors;
         CustomColors.update();
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.CUSTOM_ITEMS) {
         this.ofCustomItems = !this.ofCustomItems;
         this.mc.refreshResources();
      }

      if (par1EnumOptions == GameSettings.Options.CUSTOM_SKY) {
         this.ofCustomSky = !this.ofCustomSky;
         CustomSky.update();
      }

      if (par1EnumOptions == GameSettings.Options.SHOW_CAPES) {
         this.ofShowCapes = !this.ofShowCapes;
      }

      if (par1EnumOptions == GameSettings.Options.NATURAL_TEXTURES) {
         this.ofNaturalTextures = !this.ofNaturalTextures;
         NaturalTextures.update();
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.EMISSIVE_TEXTURES) {
         this.ofEmissiveTextures = !this.ofEmissiveTextures;
         this.mc.refreshResources();
      }

      if (par1EnumOptions == GameSettings.Options.FAST_MATH) {
         this.ofFastMath = !this.ofFastMath;
         MathHelper.fastMath = this.ofFastMath;
      }

      if (par1EnumOptions == GameSettings.Options.FAST_RENDER) {
         if (!this.ofFastRender && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
            return;
         }

         this.ofFastRender = !this.ofFastRender;
         if (this.ofFastRender) {
            this.mc.entityRenderer.stopUseShader();
         }

         Config.updateFramebufferSize();
      }

      if (par1EnumOptions == GameSettings.Options.TRANSLUCENT_BLOCKS) {
         if (this.ofTranslucentBlocks == 0) {
            this.ofTranslucentBlocks = 1;
         } else if (this.ofTranslucentBlocks == 1) {
            this.ofTranslucentBlocks = 2;
         } else if (this.ofTranslucentBlocks == 2) {
            this.ofTranslucentBlocks = 0;
         } else {
            this.ofTranslucentBlocks = 0;
         }

         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.LAZY_CHUNK_LOADING) {
         this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
      }

      if (par1EnumOptions == GameSettings.Options.RENDER_REGIONS) {
         this.ofRenderRegions = !this.ofRenderRegions;
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.SMART_ANIMATIONS) {
         this.ofSmartAnimations = !this.ofSmartAnimations;
         this.mc.renderGlobal.loadRenderers();
      }

      if (par1EnumOptions == GameSettings.Options.DYNAMIC_FOV) {
         this.ofDynamicFov = !this.ofDynamicFov;
      }

      if (par1EnumOptions == GameSettings.Options.ALTERNATE_BLOCKS) {
         this.ofAlternateBlocks = !this.ofAlternateBlocks;
         this.mc.refreshResources();
      }

      if (par1EnumOptions == GameSettings.Options.DYNAMIC_LIGHTS) {
         this.ofDynamicLights = nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
         DynamicLights.removeLights(this.mc.renderGlobal);
      }

      if (par1EnumOptions == GameSettings.Options.SCREENSHOT_SIZE) {
         this.ofScreenshotSize++;
         if (this.ofScreenshotSize > 4) {
            this.ofScreenshotSize = 1;
         }

         if (!OpenGlHelper.isFramebufferEnabled()) {
            this.ofScreenshotSize = 1;
         }
      }

      if (par1EnumOptions == GameSettings.Options.CUSTOM_ENTITY_MODELS) {
         this.ofCustomEntityModels = !this.ofCustomEntityModels;
         this.mc.refreshResources();
      }

      if (par1EnumOptions == GameSettings.Options.CUSTOM_GUIS) {
         this.ofCustomGuis = !this.ofCustomGuis;
         CustomGuis.update();
      }

      if (par1EnumOptions == GameSettings.Options.SHOW_GL_ERRORS) {
         this.ofShowGlErrors = !this.ofShowGlErrors;
      }

      if (par1EnumOptions == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
         this.heldItemTooltips = !this.heldItemTooltips;
      }

      if (par1EnumOptions == GameSettings.Options.ADVANCED_TOOLTIPS) {
         this.advancedItemTooltips = !this.advancedItemTooltips;
      }
   }

   private String getKeyBindingOF(GameSettings.Options par1EnumOptions) {
      String var2 = I18n.format(par1EnumOptions.getTranslation()) + ": ";
      if (var2 == null) {
         var2 = par1EnumOptions.getTranslation();
      }

      if (par1EnumOptions == GameSettings.Options.RENDER_DISTANCE) {
         int distChunks = (int)this.getOptionFloatValue(par1EnumOptions);
         String str = I18n.format("of.options.renderDistance.tiny");
         int baseDist = 2;
         if (distChunks >= 4) {
            str = I18n.format("of.options.renderDistance.short");
            baseDist = 4;
         }

         if (distChunks >= 8) {
            str = I18n.format("of.options.renderDistance.normal");
            baseDist = 8;
         }

         if (distChunks >= 16) {
            str = I18n.format("of.options.renderDistance.far");
            baseDist = 16;
         }

         if (distChunks >= 32) {
            str = Lang.get("of.options.renderDistance.extreme");
            baseDist = 32;
         }

         if (distChunks >= 48) {
            str = Lang.get("of.options.renderDistance.insane");
            baseDist = 48;
         }

         if (distChunks >= 64) {
            str = Lang.get("of.options.renderDistance.ludicrous");
            baseDist = 64;
         }

         int diff = this.renderDistanceChunks - baseDist;
         String descr = str;
         if (diff > 0) {
            descr = str + "+";
         }

         return var2 + distChunks + " " + descr + "";
      } else if (par1EnumOptions == GameSettings.Options.FOG_FANCY) {
         switch (this.ofFogType) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            case 3:
               return var2 + Lang.getOff();
            default:
               return var2 + Lang.getOff();
         }
      } else if (par1EnumOptions == GameSettings.Options.FOG_START) {
         return var2 + this.ofFogStart;
      } else if (par1EnumOptions == GameSettings.Options.MIPMAP_TYPE) {
         switch (this.ofMipmapType) {
            case 0:
               return var2 + Lang.get("of.options.mipmap.nearest");
            case 1:
               return var2 + Lang.get("of.options.mipmap.linear");
            case 2:
               return var2 + Lang.get("of.options.mipmap.bilinear");
            case 3:
               return var2 + Lang.get("of.options.mipmap.trilinear");
            default:
               return var2 + "of.options.mipmap.nearest";
         }
      } else if (par1EnumOptions == GameSettings.Options.SMOOTH_FPS) {
         return this.ofSmoothFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SMOOTH_WORLD) {
         return this.ofSmoothWorld ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.CLOUDS) {
         switch (this.ofClouds) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            case 3:
               return var2 + Lang.getOff();
            default:
               return var2 + Lang.getDefault();
         }
      } else if (par1EnumOptions == GameSettings.Options.TREES) {
         switch (this.ofTrees) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            case 3:
            default:
               return var2 + Lang.getDefault();
            case 4:
               return var2 + Lang.get("of.general.smart");
         }
      } else if (par1EnumOptions == GameSettings.Options.DROPPED_ITEMS) {
         switch (this.ofDroppedItems) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            default:
               return var2 + Lang.getDefault();
         }
      } else if (par1EnumOptions == GameSettings.Options.RAIN) {
         switch (this.ofRain) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            case 3:
               return var2 + Lang.getOff();
            default:
               return var2 + Lang.getDefault();
         }
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_WATER) {
         switch (this.ofAnimatedWater) {
            case 1:
               return var2 + Lang.get("of.options.animation.dynamic");
            case 2:
               return var2 + Lang.getOff();
            default:
               return var2 + Lang.getOn();
         }
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_LAVA) {
         switch (this.ofAnimatedLava) {
            case 1:
               return var2 + Lang.get("of.options.animation.dynamic");
            case 2:
               return var2 + Lang.getOff();
            default:
               return var2 + Lang.getOn();
         }
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_FIRE) {
         return this.ofAnimatedFire ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_PORTAL) {
         return this.ofAnimatedPortal ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_REDSTONE) {
         return this.ofAnimatedRedstone ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_EXPLOSION) {
         return this.ofAnimatedExplosion ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_FLAME) {
         return this.ofAnimatedFlame ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_SMOKE) {
         return this.ofAnimatedSmoke ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.VOID_PARTICLES) {
         return this.ofVoidParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.WATER_PARTICLES) {
         return this.ofWaterParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.PORTAL_PARTICLES) {
         return this.ofPortalParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.POTION_PARTICLES) {
         return this.ofPotionParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.FIREWORK_PARTICLES) {
         return this.ofFireworkParticles ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.DRIPPING_WATER_LAVA) {
         return this.ofDrippingWaterLava ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_TERRAIN) {
         return this.ofAnimatedTerrain ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ANIMATED_TEXTURES) {
         return this.ofAnimatedTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.RAIN_SPLASH) {
         return this.ofRainSplash ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.LAGOMETER) {
         return this.ofLagometer ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SHOW_FPS) {
         return this.ofShowFps ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.AUTOSAVE_TICKS) {
         int step = 900;
         if (this.ofAutoSaveTicks <= step) {
            return var2 + Lang.get("of.options.save.45s");
         } else if (this.ofAutoSaveTicks <= 2 * step) {
            return var2 + Lang.get("of.options.save.90s");
         } else if (this.ofAutoSaveTicks <= 4 * step) {
            return var2 + Lang.get("of.options.save.3min");
         } else if (this.ofAutoSaveTicks <= 8 * step) {
            return var2 + Lang.get("of.options.save.6min");
         } else {
            return this.ofAutoSaveTicks <= 16 * step ? var2 + Lang.get("of.options.save.12min") : var2 + Lang.get("of.options.save.24min");
         }
      } else if (par1EnumOptions == GameSettings.Options.BETTER_GRASS) {
         switch (this.ofBetterGrass) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            default:
               return var2 + Lang.getOff();
         }
      } else if (par1EnumOptions == GameSettings.Options.CONNECTED_TEXTURES) {
         switch (this.ofConnectedTextures) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            default:
               return var2 + Lang.getOff();
         }
      } else if (par1EnumOptions == GameSettings.Options.WEATHER) {
         return this.ofWeather ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SKY) {
         return this.ofSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.STARS) {
         return this.ofStars ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SUN_MOON) {
         return this.ofSunMoon ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.VIGNETTE) {
         switch (this.ofVignette) {
            case 1:
               return var2 + Lang.getFast();
            case 2:
               return var2 + Lang.getFancy();
            default:
               return var2 + Lang.getDefault();
         }
      } else if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES) {
         return var2 + this.ofChunkUpdates;
      } else if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
         return this.ofChunkUpdatesDynamic ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.TIME) {
         if (this.ofTime == 1) {
            return var2 + Lang.get("of.options.time.dayOnly");
         } else {
            return this.ofTime == 2 ? var2 + Lang.get("of.options.time.nightOnly") : var2 + Lang.getDefault();
         }
      } else if (par1EnumOptions == GameSettings.Options.CLEAR_WATER) {
         return this.ofClearWater ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.AA_LEVEL) {
         String suffix = "";
         if (this.ofAaLevel != Config.getAntialiasingLevel()) {
            suffix = " (" + Lang.get("of.general.restart") + ")";
         }

         return this.ofAaLevel == 0 ? var2 + Lang.getOff() + suffix : var2 + this.ofAaLevel + suffix;
      } else if (par1EnumOptions == GameSettings.Options.AF_LEVEL) {
         return this.ofAfLevel == 1 ? var2 + Lang.getOff() : var2 + this.ofAfLevel;
      } else if (par1EnumOptions == GameSettings.Options.PROFILER) {
         return this.ofProfiler ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.BETTER_SNOW) {
         return this.ofBetterSnow ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SWAMP_COLORS) {
         return this.ofSwampColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.RANDOM_ENTITIES) {
         return this.ofRandomEntities ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SMOOTH_BIOMES) {
         return this.ofSmoothBiomes ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.CUSTOM_FONTS) {
         return this.ofCustomFonts ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.CUSTOM_COLORS) {
         return this.ofCustomColors ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.CUSTOM_SKY) {
         return this.ofCustomSky ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SHOW_CAPES) {
         return this.ofShowCapes ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.CUSTOM_ITEMS) {
         return this.ofCustomItems ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.NATURAL_TEXTURES) {
         return this.ofNaturalTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.EMISSIVE_TEXTURES) {
         return this.ofEmissiveTextures ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.FAST_MATH) {
         return this.ofFastMath ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.FAST_RENDER) {
         return this.ofFastRender ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.TRANSLUCENT_BLOCKS) {
         if (this.ofTranslucentBlocks == 1) {
            return var2 + Lang.getFast();
         } else {
            return this.ofTranslucentBlocks == 2 ? var2 + Lang.getFancy() : var2 + Lang.getDefault();
         }
      } else if (par1EnumOptions == GameSettings.Options.LAZY_CHUNK_LOADING) {
         return this.ofLazyChunkLoading ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.RENDER_REGIONS) {
         return this.ofRenderRegions ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SMART_ANIMATIONS) {
         return this.ofSmartAnimations ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.DYNAMIC_FOV) {
         return this.ofDynamicFov ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ALTERNATE_BLOCKS) {
         return this.ofAlternateBlocks ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.DYNAMIC_LIGHTS) {
         int index = indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
         return var2 + getTranslation(KEYS_DYNAMIC_LIGHTS, index);
      } else if (par1EnumOptions == GameSettings.Options.SCREENSHOT_SIZE) {
         return this.ofScreenshotSize <= 1 ? var2 + Lang.getDefault() : var2 + this.ofScreenshotSize + "x";
      } else if (par1EnumOptions == GameSettings.Options.CUSTOM_ENTITY_MODELS) {
         return this.ofCustomEntityModels ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.CUSTOM_GUIS) {
         return this.ofCustomGuis ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.SHOW_GL_ERRORS) {
         return this.ofShowGlErrors ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.FULLSCREEN_MODE) {
         return this.ofFullscreenMode.equals("Default") ? var2 + Lang.getDefault() : var2 + this.ofFullscreenMode;
      } else if (par1EnumOptions == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
         return this.heldItemTooltips ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.ADVANCED_TOOLTIPS) {
         return this.advancedItemTooltips ? var2 + Lang.getOn() : var2 + Lang.getOff();
      } else if (par1EnumOptions == GameSettings.Options.FRAMERATE_LIMIT) {
         float var6 = this.getOptionFloatValue(par1EnumOptions);
         if (var6 == 0.0F) {
            return var2 + Lang.get("of.options.framerateLimit.vsync");
         } else {
            return var6 == par1EnumOptions.valueMax ? var2 + I18n.format("options.framerateLimit.max") : var2 + (int)var6 + " fps";
         }
      } else {
         return null;
      }
   }

   public void loadOfOptions() {
      try {
         File ofReadFile = this.optionsFileOF;
         if (!ofReadFile.exists()) {
            ofReadFile = this.optionsFile;
         }

         if (!ofReadFile.exists()) {
            return;
         }

         BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(ofReadFile), StandardCharsets.UTF_8));
         String s = "";

         while ((s = bufferedreader.readLine()) != null) {
            try {
               String[] as = s.split(":");
               if (as[0].equals("ofRenderDistanceChunks") && as.length >= 2) {
                  this.renderDistanceChunks = Integer.valueOf(as[1]);
                  this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 1024);
               }

               if (as[0].equals("ofFogType") && as.length >= 2) {
                  this.ofFogType = Integer.valueOf(as[1]);
                  this.ofFogType = Config.limit(this.ofFogType, 1, 3);
               }

               if (as[0].equals("ofFogStart") && as.length >= 2) {
                  this.ofFogStart = Float.valueOf(as[1]);
                  if (this.ofFogStart < 0.2F) {
                     this.ofFogStart = 0.2F;
                  }

                  if (this.ofFogStart > 0.81F) {
                     this.ofFogStart = 0.8F;
                  }
               }

               if (as[0].equals("ofMipmapType") && as.length >= 2) {
                  this.ofMipmapType = Integer.valueOf(as[1]);
                  this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
               }

               if (as[0].equals("ofOcclusionFancy") && as.length >= 2) {
                  this.ofOcclusionFancy = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofSmoothFps") && as.length >= 2) {
                  this.ofSmoothFps = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofSmoothWorld") && as.length >= 2) {
                  this.ofSmoothWorld = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAoLevel") && as.length >= 2) {
                  this.ofAoLevel = Float.valueOf(as[1]);
                  this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
               }

               if (as[0].equals("ofClouds") && as.length >= 2) {
                  this.ofClouds = Integer.valueOf(as[1]);
                  this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                  this.updateRenderClouds();
               }

               if (as[0].equals("ofCloudsHeight") && as.length >= 2) {
                  this.ofCloudsHeight = Float.valueOf(as[1]);
                  this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
               }

               if (as[0].equals("ofTrees") && as.length >= 2) {
                  this.ofTrees = Integer.valueOf(as[1]);
                  this.ofTrees = limit(this.ofTrees, OF_TREES_VALUES);
               }

               if (as[0].equals("ofDroppedItems") && as.length >= 2) {
                  this.ofDroppedItems = Integer.valueOf(as[1]);
                  this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
               }

               if (as[0].equals("ofRain") && as.length >= 2) {
                  this.ofRain = Integer.valueOf(as[1]);
                  this.ofRain = Config.limit(this.ofRain, 0, 3);
               }

               if (as[0].equals("ofAnimatedWater") && as.length >= 2) {
                  this.ofAnimatedWater = Integer.valueOf(as[1]);
                  this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
               }

               if (as[0].equals("ofAnimatedLava") && as.length >= 2) {
                  this.ofAnimatedLava = Integer.valueOf(as[1]);
                  this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
               }

               if (as[0].equals("ofAnimatedFire") && as.length >= 2) {
                  this.ofAnimatedFire = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAnimatedPortal") && as.length >= 2) {
                  this.ofAnimatedPortal = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAnimatedRedstone") && as.length >= 2) {
                  this.ofAnimatedRedstone = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAnimatedExplosion") && as.length >= 2) {
                  this.ofAnimatedExplosion = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAnimatedFlame") && as.length >= 2) {
                  this.ofAnimatedFlame = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAnimatedSmoke") && as.length >= 2) {
                  this.ofAnimatedSmoke = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofVoidParticles") && as.length >= 2) {
                  this.ofVoidParticles = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofWaterParticles") && as.length >= 2) {
                  this.ofWaterParticles = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofPortalParticles") && as.length >= 2) {
                  this.ofPortalParticles = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofPotionParticles") && as.length >= 2) {
                  this.ofPotionParticles = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofFireworkParticles") && as.length >= 2) {
                  this.ofFireworkParticles = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofDrippingWaterLava") && as.length >= 2) {
                  this.ofDrippingWaterLava = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAnimatedTerrain") && as.length >= 2) {
                  this.ofAnimatedTerrain = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAnimatedTextures") && as.length >= 2) {
                  this.ofAnimatedTextures = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofRainSplash") && as.length >= 2) {
                  this.ofRainSplash = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofLagometer") && as.length >= 2) {
                  this.ofLagometer = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofShowFps") && as.length >= 2) {
                  this.ofShowFps = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAutoSaveTicks") && as.length >= 2) {
                  this.ofAutoSaveTicks = Integer.valueOf(as[1]);
                  this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
               }

               if (as[0].equals("ofBetterGrass") && as.length >= 2) {
                  this.ofBetterGrass = Integer.valueOf(as[1]);
                  this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
               }

               if (as[0].equals("ofConnectedTextures") && as.length >= 2) {
                  this.ofConnectedTextures = Integer.valueOf(as[1]);
                  this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
               }

               if (as[0].equals("ofWeather") && as.length >= 2) {
                  this.ofWeather = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofSky") && as.length >= 2) {
                  this.ofSky = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofStars") && as.length >= 2) {
                  this.ofStars = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofSunMoon") && as.length >= 2) {
                  this.ofSunMoon = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofVignette") && as.length >= 2) {
                  this.ofVignette = Integer.valueOf(as[1]);
                  this.ofVignette = Config.limit(this.ofVignette, 0, 2);
               }

               if (as[0].equals("ofChunkUpdates") && as.length >= 2) {
                  this.ofChunkUpdates = Integer.valueOf(as[1]);
                  this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
               }

               if (as[0].equals("ofChunkUpdatesDynamic") && as.length >= 2) {
                  this.ofChunkUpdatesDynamic = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofTime") && as.length >= 2) {
                  this.ofTime = Integer.valueOf(as[1]);
                  this.ofTime = Config.limit(this.ofTime, 0, 2);
               }

               if (as[0].equals("ofClearWater") && as.length >= 2) {
                  this.ofClearWater = Boolean.valueOf(as[1]);
                  this.updateWaterOpacity();
               }

               if (as[0].equals("ofAaLevel") && as.length >= 2) {
                  this.ofAaLevel = Integer.valueOf(as[1]);
                  this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
               }

               if (as[0].equals("ofAfLevel") && as.length >= 2) {
                  this.ofAfLevel = Integer.valueOf(as[1]);
                  this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
               }

               if (as[0].equals("ofProfiler") && as.length >= 2) {
                  this.ofProfiler = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofBetterSnow") && as.length >= 2) {
                  this.ofBetterSnow = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofSwampColors") && as.length >= 2) {
                  this.ofSwampColors = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofRandomEntities") && as.length >= 2) {
                  this.ofRandomEntities = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofSmoothBiomes") && as.length >= 2) {
                  this.ofSmoothBiomes = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofCustomFonts") && as.length >= 2) {
                  this.ofCustomFonts = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofCustomColors") && as.length >= 2) {
                  this.ofCustomColors = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofCustomItems") && as.length >= 2) {
                  this.ofCustomItems = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofCustomSky") && as.length >= 2) {
                  this.ofCustomSky = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofShowCapes") && as.length >= 2) {
                  this.ofShowCapes = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofNaturalTextures") && as.length >= 2) {
                  this.ofNaturalTextures = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofEmissiveTextures") && as.length >= 2) {
                  this.ofEmissiveTextures = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofLazyChunkLoading") && as.length >= 2) {
                  this.ofLazyChunkLoading = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofRenderRegions") && as.length >= 2) {
                  this.ofRenderRegions = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofSmartAnimations") && as.length >= 2) {
                  this.ofSmartAnimations = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofDynamicFov") && as.length >= 2) {
                  this.ofDynamicFov = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofAlternateBlocks") && as.length >= 2) {
                  this.ofAlternateBlocks = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofDynamicLights") && as.length >= 2) {
                  this.ofDynamicLights = Integer.valueOf(as[1]);
                  this.ofDynamicLights = limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
               }

               if (as[0].equals("ofScreenshotSize") && as.length >= 2) {
                  this.ofScreenshotSize = Integer.valueOf(as[1]);
                  this.ofScreenshotSize = Config.limit(this.ofScreenshotSize, 1, 4);
               }

               if (as[0].equals("ofCustomEntityModels") && as.length >= 2) {
                  this.ofCustomEntityModels = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofCustomGuis") && as.length >= 2) {
                  this.ofCustomGuis = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofShowGlErrors") && as.length >= 2) {
                  this.ofShowGlErrors = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofFullscreenMode") && as.length >= 2) {
                  this.ofFullscreenMode = as[1];
               }

               if (as[0].equals("ofFastMath") && as.length >= 2) {
                  this.ofFastMath = Boolean.valueOf(as[1]);
                  MathHelper.fastMath = this.ofFastMath;
               }

               if (as[0].equals("ofFastRender") && as.length >= 2) {
                  this.ofFastRender = Boolean.valueOf(as[1]);
               }

               if (as[0].equals("ofTranslucentBlocks") && as.length >= 2) {
                  this.ofTranslucentBlocks = Integer.valueOf(as[1]);
                  this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
               }

               if (as[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription())) {
                  this.ofKeyBindZoom.setKeyCode(Integer.parseInt(as[1]));
               }
            } catch (Exception var5) {
               Config.dbg("Skipping bad option: " + s);
               var5.printStackTrace();
            }
         }

         KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[]{this.ofKeyBindZoom});
         KeyBinding.resetKeyBindingArrayAndHash();
         bufferedreader.close();
      } catch (Exception var6) {
         Config.warn("Failed to load options");
         var6.printStackTrace();
      }
   }

   public void saveOfOptions() {
      try {
         PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFileOF), StandardCharsets.UTF_8));
         printwriter.println("ofFogType:" + this.ofFogType);
         printwriter.println("ofFogStart:" + this.ofFogStart);
         printwriter.println("ofMipmapType:" + this.ofMipmapType);
         printwriter.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
         printwriter.println("ofSmoothFps:" + this.ofSmoothFps);
         printwriter.println("ofSmoothWorld:" + this.ofSmoothWorld);
         printwriter.println("ofAoLevel:" + this.ofAoLevel);
         printwriter.println("ofClouds:" + this.ofClouds);
         printwriter.println("ofCloudsHeight:" + this.ofCloudsHeight);
         printwriter.println("ofTrees:" + this.ofTrees);
         printwriter.println("ofDroppedItems:" + this.ofDroppedItems);
         printwriter.println("ofRain:" + this.ofRain);
         printwriter.println("ofAnimatedWater:" + this.ofAnimatedWater);
         printwriter.println("ofAnimatedLava:" + this.ofAnimatedLava);
         printwriter.println("ofAnimatedFire:" + this.ofAnimatedFire);
         printwriter.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
         printwriter.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
         printwriter.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
         printwriter.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
         printwriter.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
         printwriter.println("ofVoidParticles:" + this.ofVoidParticles);
         printwriter.println("ofWaterParticles:" + this.ofWaterParticles);
         printwriter.println("ofPortalParticles:" + this.ofPortalParticles);
         printwriter.println("ofPotionParticles:" + this.ofPotionParticles);
         printwriter.println("ofFireworkParticles:" + this.ofFireworkParticles);
         printwriter.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
         printwriter.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
         printwriter.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
         printwriter.println("ofRainSplash:" + this.ofRainSplash);
         printwriter.println("ofLagometer:" + this.ofLagometer);
         printwriter.println("ofShowFps:" + this.ofShowFps);
         printwriter.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
         printwriter.println("ofBetterGrass:" + this.ofBetterGrass);
         printwriter.println("ofConnectedTextures:" + this.ofConnectedTextures);
         printwriter.println("ofWeather:" + this.ofWeather);
         printwriter.println("ofSky:" + this.ofSky);
         printwriter.println("ofStars:" + this.ofStars);
         printwriter.println("ofSunMoon:" + this.ofSunMoon);
         printwriter.println("ofVignette:" + this.ofVignette);
         printwriter.println("ofChunkUpdates:" + this.ofChunkUpdates);
         printwriter.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
         printwriter.println("ofTime:" + this.ofTime);
         printwriter.println("ofClearWater:" + this.ofClearWater);
         printwriter.println("ofAaLevel:" + this.ofAaLevel);
         printwriter.println("ofAfLevel:" + this.ofAfLevel);
         printwriter.println("ofProfiler:" + this.ofProfiler);
         printwriter.println("ofBetterSnow:" + this.ofBetterSnow);
         printwriter.println("ofSwampColors:" + this.ofSwampColors);
         printwriter.println("ofRandomEntities:" + this.ofRandomEntities);
         printwriter.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
         printwriter.println("ofCustomFonts:" + this.ofCustomFonts);
         printwriter.println("ofCustomColors:" + this.ofCustomColors);
         printwriter.println("ofCustomItems:" + this.ofCustomItems);
         printwriter.println("ofCustomSky:" + this.ofCustomSky);
         printwriter.println("ofShowCapes:" + this.ofShowCapes);
         printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
         printwriter.println("ofEmissiveTextures:" + this.ofEmissiveTextures);
         printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
         printwriter.println("ofRenderRegions:" + this.ofRenderRegions);
         printwriter.println("ofSmartAnimations:" + this.ofSmartAnimations);
         printwriter.println("ofDynamicFov:" + this.ofDynamicFov);
         printwriter.println("ofAlternateBlocks:" + this.ofAlternateBlocks);
         printwriter.println("ofDynamicLights:" + this.ofDynamicLights);
         printwriter.println("ofScreenshotSize:" + this.ofScreenshotSize);
         printwriter.println("ofCustomEntityModels:" + this.ofCustomEntityModels);
         printwriter.println("ofCustomGuis:" + this.ofCustomGuis);
         printwriter.println("ofShowGlErrors:" + this.ofShowGlErrors);
         printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
         printwriter.println("ofFastMath:" + this.ofFastMath);
         printwriter.println("ofFastRender:" + this.ofFastRender);
         printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
         printwriter.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
         printwriter.close();
      } catch (Exception var2) {
         Config.warn("Failed to save options");
         var2.printStackTrace();
      }
   }

   private void updateRenderClouds() {
      switch (this.ofClouds) {
         case 1:
            this.clouds = 1;
            break;
         case 2:
            this.clouds = 2;
            break;
         case 3:
            this.clouds = 0;
            break;
         default:
            if (this.fancyGraphics) {
               this.clouds = 2;
            } else {
               this.clouds = 1;
            }
      }
   }

   public void resetSettings() {
      this.renderDistanceChunks = 8;
      this.viewBobbing = true;
      this.anaglyph = false;
      this.limitFramerate = (int)GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
      this.enableVsync = false;
      this.updateVSync();
      this.mipmapLevels = 4;
      this.fancyGraphics = true;
      this.ambientOcclusion = 2;
      this.clouds = 2;
      this.fovSetting = 70.0F;
      this.gammaSetting = 0.0F;
      this.guiScale = 0;
      this.particleSetting = 0;
      this.heldItemTooltips = true;
      this.useVbo = false;
      this.forceUnicodeFont = false;
      this.ofFogType = 1;
      this.ofFogStart = 0.8F;
      this.ofMipmapType = 0;
      this.ofOcclusionFancy = false;
      this.ofSmartAnimations = false;
      this.ofSmoothFps = false;
      Config.updateAvailableProcessors();
      this.ofSmoothWorld = Config.isSingleProcessor();
      this.ofLazyChunkLoading = false;
      this.ofRenderRegions = false;
      this.ofFastMath = false;
      this.ofFastRender = false;
      this.ofTranslucentBlocks = 0;
      this.ofDynamicFov = true;
      this.ofAlternateBlocks = true;
      this.ofDynamicLights = 3;
      this.ofScreenshotSize = 1;
      this.ofCustomEntityModels = true;
      this.ofCustomGuis = true;
      this.ofShowGlErrors = true;
      this.ofAoLevel = 1.0F;
      this.ofAaLevel = 0;
      this.ofAfLevel = 1;
      this.ofClouds = 0;
      this.ofCloudsHeight = 0.0F;
      this.ofTrees = 0;
      this.ofRain = 0;
      this.ofBetterGrass = 3;
      this.ofAutoSaveTicks = 4000;
      this.ofLagometer = false;
      this.ofShowFps = false;
      this.ofProfiler = false;
      this.ofWeather = true;
      this.ofSky = true;
      this.ofStars = true;
      this.ofSunMoon = true;
      this.ofVignette = 0;
      this.ofChunkUpdates = 1;
      this.ofChunkUpdatesDynamic = false;
      this.ofTime = 0;
      this.ofClearWater = false;
      this.ofBetterSnow = false;
      this.ofFullscreenMode = "Default";
      this.ofSwampColors = true;
      this.ofRandomEntities = true;
      this.ofSmoothBiomes = true;
      this.ofCustomFonts = true;
      this.ofCustomColors = true;
      this.ofCustomItems = true;
      this.ofCustomSky = true;
      this.ofShowCapes = true;
      this.ofConnectedTextures = 2;
      this.ofNaturalTextures = false;
      this.ofEmissiveTextures = true;
      this.ofAnimatedWater = 0;
      this.ofAnimatedLava = 0;
      this.ofAnimatedFire = true;
      this.ofAnimatedPortal = true;
      this.ofAnimatedRedstone = true;
      this.ofAnimatedExplosion = true;
      this.ofAnimatedFlame = true;
      this.ofAnimatedSmoke = true;
      this.ofVoidParticles = true;
      this.ofWaterParticles = true;
      this.ofRainSplash = true;
      this.ofPortalParticles = true;
      this.ofPotionParticles = true;
      this.ofFireworkParticles = true;
      this.ofDrippingWaterLava = true;
      this.ofAnimatedTerrain = true;
      this.ofAnimatedTextures = true;
      Shaders.setShaderPack("OFF");
      Shaders.configAntialiasingLevel = 0;
      Shaders.uninit();
      Shaders.storeConfig();
      this.updateWaterOpacity();
      this.mc.refreshResources();
      this.saveOptions();
   }

   public void updateVSync() {
      Display.setVSyncEnabled(this.enableVsync);
   }

   private void updateWaterOpacity() {
      if (Config.isIntegratedServerRunning()) {
         Config.waterOpacityChanged = true;
      }

      ClearWater.updateWaterOpacity(this, this.mc.world);
   }

   public void setAllAnimations(boolean flag) {
      int animVal = flag ? 0 : 2;
      this.ofAnimatedWater = animVal;
      this.ofAnimatedLava = animVal;
      this.ofAnimatedFire = flag;
      this.ofAnimatedPortal = flag;
      this.ofAnimatedRedstone = flag;
      this.ofAnimatedExplosion = flag;
      this.ofAnimatedFlame = flag;
      this.ofAnimatedSmoke = flag;
      this.ofVoidParticles = flag;
      this.ofWaterParticles = flag;
      this.ofRainSplash = flag;
      this.ofPortalParticles = flag;
      this.ofPotionParticles = flag;
      this.ofFireworkParticles = flag;
      this.particleSetting = flag ? 0 : 2;
      this.ofDrippingWaterLava = flag;
      this.ofAnimatedTerrain = flag;
      this.ofAnimatedTextures = flag;
   }

   private static int nextValue(int val, int[] vals) {
      int index = indexOf(val, vals);
      if (index < 0) {
         return vals[0];
      } else {
         if (++index >= vals.length) {
            index = 0;
         }

         return vals[index];
      }
   }

   private static int limit(int val, int[] vals) {
      int index = indexOf(val, vals);
      return index < 0 ? vals[0] : val;
   }

   private static int indexOf(int val, int[] vals) {
      for (int i = 0; i < vals.length; i++) {
         if (vals[i] == val) {
            return i;
         }
      }

      return -1;
   }

   private void setForgeKeybindProperties() {
      if (Reflector.KeyConflictContext_IN_GAME.exists()) {
         if (Reflector.ForgeKeyBinding_setKeyConflictContext.exists()) {
            Object inGame = Reflector.getFieldValue(Reflector.KeyConflictContext_IN_GAME);
            Reflector.call(this.keyBindForward, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindLeft, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindBack, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindRight, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindJump, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindSneak, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindSprint, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindAttack, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindChat, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindPlayerList, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindCommand, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindTogglePerspective, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindSmoothCamera, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
            Reflector.call(this.keyBindSwapHands, Reflector.ForgeKeyBinding_setKeyConflictContext, new Object[]{inGame});
         }
      }
   }

   public void onGuiClosed() {
      if (this.needsResourceRefresh) {
         this.mc.scheduleResourcesRefresh();
         this.needsResourceRefresh = false;
      }
   }

   public static enum Options {
      INVERT_MOUSE("options.invertMouse", false, true),
      SENSITIVITY("options.sensitivity", true, false),
      FOV("options.fov", true, false, 30.0F, 110.0F, 1.0F),
      GAMMA("options.gamma", true, false),
      SATURATION("options.saturation", true, false),
      RENDER_DISTANCE("options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
      VIEW_BOBBING("options.viewBobbing", false, true),
      ANAGLYPH("options.anaglyph", false, true),
      FRAMERATE_LIMIT("options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F),
      FBO_ENABLE("options.fboEnable", false, true),
      RENDER_CLOUDS("options.renderClouds", false, false),
      GRAPHICS("options.graphics", false, false),
      AMBIENT_OCCLUSION("options.ao", false, false),
      GUI_SCALE("options.guiScale", false, false),
      PARTICLES("options.particles", false, false),
      CHAT_VISIBILITY("options.chat.visibility", false, false),
      CHAT_COLOR("options.chat.color", false, true),
      CHAT_LINKS("options.chat.links", false, true),
      CHAT_OPACITY("options.chat.opacity", true, false),
      CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true),
      SNOOPER_ENABLED("options.snooper", false, true),
      USE_FULLSCREEN("options.fullscreen", false, true),
      ENABLE_VSYNC("options.vsync", false, true),
      USE_VBO("options.vbo", false, true),
      TOUCHSCREEN("options.touchscreen", false, true),
      CHAT_SCALE("options.chat.scale", true, false),
      CHAT_WIDTH("options.chat.width", true, false),
      CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),
      CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false),
      MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
      FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true),
      REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
      ENTITY_SHADOWS("options.entityShadows", false, true),
      MAIN_HAND("options.mainHand", false, false),
      ATTACK_INDICATOR("options.attackIndicator", false, false),
      ENABLE_WEAK_ATTACKS("options.enableWeakAttacks", false, true),
      SHOW_SUBTITLES("options.showSubtitles", false, true),
      REALMS_NOTIFICATIONS("options.realmsNotifications", false, true),
      AUTO_JUMP("options.autoJump", false, true),
      NARRATOR("options.narrator", false, false),
      FOG_FANCY("of.options.FOG_FANCY", false, false),
      FOG_START("of.options.FOG_START", false, false),
      MIPMAP_TYPE("of.options.MIPMAP_TYPE", true, false, 0.0F, 3.0F, 1.0F),
      SMOOTH_FPS("of.options.SMOOTH_FPS", false, false),
      CLOUDS("of.options.CLOUDS", false, false),
      CLOUD_HEIGHT("of.options.CLOUD_HEIGHT", true, false),
      TREES("of.options.TREES", false, false),
      RAIN("of.options.RAIN", false, false),
      ANIMATED_WATER("of.options.ANIMATED_WATER", false, false),
      ANIMATED_LAVA("of.options.ANIMATED_LAVA", false, false),
      ANIMATED_FIRE("of.options.ANIMATED_FIRE", false, false),
      ANIMATED_PORTAL("of.options.ANIMATED_PORTAL", false, false),
      AO_LEVEL("of.options.AO_LEVEL", true, false),
      LAGOMETER("of.options.LAGOMETER", false, false),
      SHOW_FPS("of.options.SHOW_FPS", false, false),
      AUTOSAVE_TICKS("of.options.AUTOSAVE_TICKS", false, false),
      BETTER_GRASS("of.options.BETTER_GRASS", false, false),
      ANIMATED_REDSTONE("of.options.ANIMATED_REDSTONE", false, false),
      ANIMATED_EXPLOSION("of.options.ANIMATED_EXPLOSION", false, false),
      ANIMATED_FLAME("of.options.ANIMATED_FLAME", false, false),
      ANIMATED_SMOKE("of.options.ANIMATED_SMOKE", false, false),
      WEATHER("of.options.WEATHER", false, false),
      SKY("of.options.SKY", false, false),
      STARS("of.options.STARS", false, false),
      SUN_MOON("of.options.SUN_MOON", false, false),
      VIGNETTE("of.options.VIGNETTE", false, false),
      CHUNK_UPDATES("of.options.CHUNK_UPDATES", false, false),
      CHUNK_UPDATES_DYNAMIC("of.options.CHUNK_UPDATES_DYNAMIC", false, false),
      TIME("of.options.TIME", false, false),
      CLEAR_WATER("of.options.CLEAR_WATER", false, false),
      SMOOTH_WORLD("of.options.SMOOTH_WORLD", false, false),
      VOID_PARTICLES("of.options.VOID_PARTICLES", false, false),
      WATER_PARTICLES("of.options.WATER_PARTICLES", false, false),
      RAIN_SPLASH("of.options.RAIN_SPLASH", false, false),
      PORTAL_PARTICLES("of.options.PORTAL_PARTICLES", false, false),
      POTION_PARTICLES("of.options.POTION_PARTICLES", false, false),
      FIREWORK_PARTICLES("of.options.FIREWORK_PARTICLES", false, false),
      PROFILER("of.options.PROFILER", false, false),
      DRIPPING_WATER_LAVA("of.options.DRIPPING_WATER_LAVA", false, false),
      BETTER_SNOW("of.options.BETTER_SNOW", false, false),
      FULLSCREEN_MODE("of.options.FULLSCREEN_MODE", true, false, 0.0F, Config.getDisplayModes().length, 1.0F),
      ANIMATED_TERRAIN("of.options.ANIMATED_TERRAIN", false, false),
      SWAMP_COLORS("of.options.SWAMP_COLORS", false, false),
      RANDOM_ENTITIES("of.options.RANDOM_ENTITIES", false, false),
      SMOOTH_BIOMES("of.options.SMOOTH_BIOMES", false, false),
      CUSTOM_FONTS("of.options.CUSTOM_FONTS", false, false),
      CUSTOM_COLORS("of.options.CUSTOM_COLORS", false, false),
      SHOW_CAPES("of.options.SHOW_CAPES", false, false),
      CONNECTED_TEXTURES("of.options.CONNECTED_TEXTURES", false, false),
      CUSTOM_ITEMS("of.options.CUSTOM_ITEMS", false, false),
      AA_LEVEL("of.options.AA_LEVEL", true, false, 0.0F, 16.0F, 1.0F),
      AF_LEVEL("of.options.AF_LEVEL", true, false, 1.0F, 16.0F, 1.0F),
      ANIMATED_TEXTURES("of.options.ANIMATED_TEXTURES", false, false),
      NATURAL_TEXTURES("of.options.NATURAL_TEXTURES", false, false),
      EMISSIVE_TEXTURES("of.options.EMISSIVE_TEXTURES", false, false),
      HELD_ITEM_TOOLTIPS("of.options.HELD_ITEM_TOOLTIPS", false, false),
      DROPPED_ITEMS("of.options.DROPPED_ITEMS", false, false),
      LAZY_CHUNK_LOADING("of.options.LAZY_CHUNK_LOADING", false, false),
      CUSTOM_SKY("of.options.CUSTOM_SKY", false, false),
      FAST_MATH("of.options.FAST_MATH", false, false),
      FAST_RENDER("of.options.FAST_RENDER", false, false),
      TRANSLUCENT_BLOCKS("of.options.TRANSLUCENT_BLOCKS", false, false),
      DYNAMIC_FOV("of.options.DYNAMIC_FOV", false, false),
      DYNAMIC_LIGHTS("of.options.DYNAMIC_LIGHTS", false, false),
      ALTERNATE_BLOCKS("of.options.ALTERNATE_BLOCKS", false, false),
      CUSTOM_ENTITY_MODELS("of.options.CUSTOM_ENTITY_MODELS", false, false),
      ADVANCED_TOOLTIPS("of.options.ADVANCED_TOOLTIPS", false, false),
      SCREENSHOT_SIZE("of.options.SCREENSHOT_SIZE", false, false),
      CUSTOM_GUIS("of.options.CUSTOM_GUIS", false, false),
      RENDER_REGIONS("of.options.RENDER_REGIONS", false, false),
      SHOW_GL_ERRORS("of.options.SHOW_GL_ERRORS", false, false),
      SMART_ANIMATIONS("of.options.SMART_ANIMATIONS", false, false);

      private final boolean isFloat;
      private final boolean isBoolean;
      private final String translation;
      private final float valueStep;
      private float valueMin;
      private float valueMax;

      public static GameSettings.Options byOrdinal(int ordinal) {
         for (GameSettings.Options gamesettings$options : values()) {
            if (gamesettings$options.getOrdinal() == ordinal) {
               return gamesettings$options;
            }
         }

         return null;
      }

      private Options(String str, boolean isFloat, boolean isBoolean) {
         this(str, isFloat, isBoolean, 0.0F, 1.0F, 0.0F);
      }

      private Options(String str, boolean isFloat, boolean isBoolean, float valMin, float valMax, float valStep) {
         this.translation = str;
         this.isFloat = isFloat;
         this.isBoolean = isBoolean;
         this.valueMin = valMin;
         this.valueMax = valMax;
         this.valueStep = valStep;
      }

      public boolean isFloat() {
         return this.isFloat;
      }

      public boolean isBoolean() {
         return this.isBoolean;
      }

      public int getOrdinal() {
         return this.ordinal();
      }

      public String getTranslation() {
         return this.translation;
      }

      public float getValueMin() {
         return this.valueMin;
      }

      public float getValueMax() {
         return this.valueMax;
      }

      public void setValueMax(float value) {
         this.valueMax = value;
      }

      public float normalizeValue(float value) {
         return MathHelper.clamp((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
      }

      public float denormalizeValue(float value) {
         return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(value, 0.0F, 1.0F));
      }

      public float snapToStepClamp(float value) {
         value = this.snapToStep(value);
         return MathHelper.clamp(value, this.valueMin, this.valueMax);
      }

      private float snapToStep(float value) {
         if (this.valueStep > 0.0F) {
            value = this.valueStep * Math.round(value / this.valueStep);
         }

         return value;
      }
   }
}
