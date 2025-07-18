package net.minecraft.client.settings;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

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
   public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
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
   public KeyBinding[] keyBindings = (KeyBinding[])ArrayUtils.addAll(
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
   protected Minecraft mc;
   private File optionsFile;
   public EnumDifficulty difficulty = EnumDifficulty.NORMAL;
   public boolean hideGUI;
   public int thirdPersonView;
   public boolean showDebugInfo;
   public boolean showDebugProfilerChart;
   public boolean showLagometer;
   public String lastServer = "";
   public boolean smoothCamera;
   public boolean debugCamEnable;
   public float fovSetting = 70.0F;
   public float gammaSetting;
   public float saturation;
   public int guiScale;
   public int particleSetting;
   public int narrator;
   public String language = "en_us";
   public boolean forceUnicodeFont;

   public GameSettings(Minecraft var1, File var2) {
      this.mc = ☃;
      this.optionsFile = new File(☃, "options.txt");
      if (☃.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
         GameSettings.Options.RENDER_DISTANCE.setValueMax(32.0F);
      } else {
         GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
      }

      this.renderDistanceChunks = ☃.isJava64bit() ? 12 : 8;
      this.loadOptions();
   }

   public GameSettings() {
   }

   public static String getKeyDisplayString(int var0) {
      if (☃ < 0) {
         switch (☃) {
            case -100:
               return I18n.format("key.mouse.left");
            case -99:
               return I18n.format("key.mouse.right");
            case -98:
               return I18n.format("key.mouse.middle");
            default:
               return I18n.format("key.mouseButton", ☃ + 101);
         }
      } else {
         return ☃ < 256 ? Keyboard.getKeyName(☃) : String.format("%c", (char)(☃ - 256)).toUpperCase();
      }
   }

   public static boolean isKeyDown(KeyBinding var0) {
      int ☃ = ☃.getKeyCode();
      if (☃ == 0 || ☃ >= 256) {
         return false;
      } else {
         return ☃ < 0 ? Mouse.isButtonDown(☃ + 100) : Keyboard.isKeyDown(☃);
      }
   }

   public void setOptionKeyBinding(KeyBinding var1, int var2) {
      ☃.setKeyCode(☃);
      this.saveOptions();
   }

   public void setOptionFloatValue(GameSettings.Options var1, float var2) {
      if (☃ == GameSettings.Options.SENSITIVITY) {
         this.mouseSensitivity = ☃;
      }

      if (☃ == GameSettings.Options.FOV) {
         this.fovSetting = ☃;
      }

      if (☃ == GameSettings.Options.GAMMA) {
         this.gammaSetting = ☃;
      }

      if (☃ == GameSettings.Options.FRAMERATE_LIMIT) {
         this.limitFramerate = (int)☃;
      }

      if (☃ == GameSettings.Options.CHAT_OPACITY) {
         this.chatOpacity = ☃;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (☃ == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
         this.chatHeightFocused = ☃;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (☃ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
         this.chatHeightUnfocused = ☃;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (☃ == GameSettings.Options.CHAT_WIDTH) {
         this.chatWidth = ☃;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (☃ == GameSettings.Options.CHAT_SCALE) {
         this.chatScale = ☃;
         this.mc.ingameGUI.getChatGUI().refreshChat();
      }

      if (☃ == GameSettings.Options.MIPMAP_LEVELS) {
         int ☃ = this.mipmapLevels;
         this.mipmapLevels = (int)☃;
         if (☃ != ☃) {
            this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
            this.mc.scheduleResourcesRefresh();
         }
      }

      if (☃ == GameSettings.Options.RENDER_DISTANCE) {
         this.renderDistanceChunks = (int)☃;
         this.mc.renderGlobal.setDisplayListEntitiesDirty();
      }
   }

   public void setOptionValue(GameSettings.Options var1, int var2) {
      if (☃ == GameSettings.Options.RENDER_DISTANCE) {
         this.setOptionFloatValue(☃, MathHelper.clamp((float)(this.renderDistanceChunks + ☃), ☃.getValueMin(), ☃.getValueMax()));
      }

      if (☃ == GameSettings.Options.MAIN_HAND) {
         this.mainHand = this.mainHand.opposite();
      }

      if (☃ == GameSettings.Options.INVERT_MOUSE) {
         this.invertMouse = !this.invertMouse;
      }

      if (☃ == GameSettings.Options.GUI_SCALE) {
         this.guiScale = this.guiScale + ☃ & 3;
      }

      if (☃ == GameSettings.Options.PARTICLES) {
         this.particleSetting = (this.particleSetting + ☃) % 3;
      }

      if (☃ == GameSettings.Options.VIEW_BOBBING) {
         this.viewBobbing = !this.viewBobbing;
      }

      if (☃ == GameSettings.Options.RENDER_CLOUDS) {
         this.clouds = (this.clouds + ☃) % 3;
      }

      if (☃ == GameSettings.Options.FORCE_UNICODE_FONT) {
         this.forceUnicodeFont = !this.forceUnicodeFont;
         this.mc.fontRenderer.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
      }

      if (☃ == GameSettings.Options.FBO_ENABLE) {
         this.fboEnable = !this.fboEnable;
      }

      if (☃ == GameSettings.Options.ANAGLYPH) {
         this.anaglyph = !this.anaglyph;
         this.mc.refreshResources();
      }

      if (☃ == GameSettings.Options.GRAPHICS) {
         this.fancyGraphics = !this.fancyGraphics;
         this.mc.renderGlobal.loadRenderers();
      }

      if (☃ == GameSettings.Options.AMBIENT_OCCLUSION) {
         this.ambientOcclusion = (this.ambientOcclusion + ☃) % 3;
         this.mc.renderGlobal.loadRenderers();
      }

      if (☃ == GameSettings.Options.CHAT_VISIBILITY) {
         this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + ☃) % 3);
      }

      if (☃ == GameSettings.Options.CHAT_COLOR) {
         this.chatColours = !this.chatColours;
      }

      if (☃ == GameSettings.Options.CHAT_LINKS) {
         this.chatLinks = !this.chatLinks;
      }

      if (☃ == GameSettings.Options.CHAT_LINKS_PROMPT) {
         this.chatLinksPrompt = !this.chatLinksPrompt;
      }

      if (☃ == GameSettings.Options.SNOOPER_ENABLED) {
         this.snooperEnabled = !this.snooperEnabled;
      }

      if (☃ == GameSettings.Options.TOUCHSCREEN) {
         this.touchscreen = !this.touchscreen;
      }

      if (☃ == GameSettings.Options.USE_FULLSCREEN) {
         this.fullScreen = !this.fullScreen;
         if (this.mc.isFullScreen() != this.fullScreen) {
            this.mc.toggleFullscreen();
         }
      }

      if (☃ == GameSettings.Options.ENABLE_VSYNC) {
         this.enableVsync = !this.enableVsync;
         Display.setVSyncEnabled(this.enableVsync);
      }

      if (☃ == GameSettings.Options.USE_VBO) {
         this.useVbo = !this.useVbo;
         this.mc.renderGlobal.loadRenderers();
      }

      if (☃ == GameSettings.Options.REDUCED_DEBUG_INFO) {
         this.reducedDebugInfo = !this.reducedDebugInfo;
      }

      if (☃ == GameSettings.Options.ENTITY_SHADOWS) {
         this.entityShadows = !this.entityShadows;
      }

      if (☃ == GameSettings.Options.ATTACK_INDICATOR) {
         this.attackIndicator = (this.attackIndicator + ☃) % 3;
      }

      if (☃ == GameSettings.Options.SHOW_SUBTITLES) {
         this.showSubtitles = !this.showSubtitles;
      }

      if (☃ == GameSettings.Options.REALMS_NOTIFICATIONS) {
         this.realmsNotifications = !this.realmsNotifications;
      }

      if (☃ == GameSettings.Options.AUTO_JUMP) {
         this.autoJump = !this.autoJump;
      }

      if (☃ == GameSettings.Options.NARRATOR) {
         if (NarratorChatListener.INSTANCE.isActive()) {
            this.narrator = (this.narrator + ☃) % NARRATOR_MODES.length;
         } else {
            this.narrator = 0;
         }

         NarratorChatListener.INSTANCE.announceMode(this.narrator);
      }

      this.saveOptions();
   }

   public float getOptionFloatValue(GameSettings.Options var1) {
      if (☃ == GameSettings.Options.FOV) {
         return this.fovSetting;
      } else if (☃ == GameSettings.Options.GAMMA) {
         return this.gammaSetting;
      } else if (☃ == GameSettings.Options.SATURATION) {
         return this.saturation;
      } else if (☃ == GameSettings.Options.SENSITIVITY) {
         return this.mouseSensitivity;
      } else if (☃ == GameSettings.Options.CHAT_OPACITY) {
         return this.chatOpacity;
      } else if (☃ == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
         return this.chatHeightFocused;
      } else if (☃ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
         return this.chatHeightUnfocused;
      } else if (☃ == GameSettings.Options.CHAT_SCALE) {
         return this.chatScale;
      } else if (☃ == GameSettings.Options.CHAT_WIDTH) {
         return this.chatWidth;
      } else if (☃ == GameSettings.Options.FRAMERATE_LIMIT) {
         return this.limitFramerate;
      } else if (☃ == GameSettings.Options.MIPMAP_LEVELS) {
         return this.mipmapLevels;
      } else {
         return ☃ == GameSettings.Options.RENDER_DISTANCE ? this.renderDistanceChunks : 0.0F;
      }
   }

   public boolean getOptionOrdinalValue(GameSettings.Options var1) {
      switch (☃) {
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

   private static String getTranslation(String[] var0, int var1) {
      if (☃ < 0 || ☃ >= ☃.length) {
         ☃ = 0;
      }

      return I18n.format(☃[☃]);
   }

   public String getKeyBinding(GameSettings.Options var1) {
      String ☃ = I18n.format(☃.getTranslation()) + ": ";
      if (☃.isFloat()) {
         float ☃x = this.getOptionFloatValue(☃);
         float ☃xx = ☃.normalizeValue(☃x);
         if (☃ == GameSettings.Options.SENSITIVITY) {
            if (☃xx == 0.0F) {
               return ☃ + I18n.format("options.sensitivity.min");
            } else {
               return ☃xx == 1.0F ? ☃ + I18n.format("options.sensitivity.max") : ☃ + (int)(☃xx * 200.0F) + "%";
            }
         } else if (☃ == GameSettings.Options.FOV) {
            if (☃x == 70.0F) {
               return ☃ + I18n.format("options.fov.min");
            } else {
               return ☃x == 110.0F ? ☃ + I18n.format("options.fov.max") : ☃ + (int)☃x;
            }
         } else if (☃ == GameSettings.Options.FRAMERATE_LIMIT) {
            return ☃x == ☃.valueMax ? ☃ + I18n.format("options.framerateLimit.max") : ☃ + I18n.format("options.framerate", (int)☃x);
         } else if (☃ == GameSettings.Options.RENDER_CLOUDS) {
            return ☃x == ☃.valueMin ? ☃ + I18n.format("options.cloudHeight.min") : ☃ + ((int)☃x + 128);
         } else if (☃ == GameSettings.Options.GAMMA) {
            if (☃xx == 0.0F) {
               return ☃ + I18n.format("options.gamma.min");
            } else {
               return ☃xx == 1.0F ? ☃ + I18n.format("options.gamma.max") : ☃ + "+" + (int)(☃xx * 100.0F) + "%";
            }
         } else if (☃ == GameSettings.Options.SATURATION) {
            return ☃ + (int)(☃xx * 400.0F) + "%";
         } else if (☃ == GameSettings.Options.CHAT_OPACITY) {
            return ☃ + (int)(☃xx * 90.0F + 10.0F) + "%";
         } else if (☃ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
            return ☃ + GuiNewChat.calculateChatboxHeight(☃xx) + "px";
         } else if (☃ == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
            return ☃ + GuiNewChat.calculateChatboxHeight(☃xx) + "px";
         } else if (☃ == GameSettings.Options.CHAT_WIDTH) {
            return ☃ + GuiNewChat.calculateChatboxWidth(☃xx) + "px";
         } else if (☃ == GameSettings.Options.RENDER_DISTANCE) {
            return ☃ + I18n.format("options.chunks", (int)☃x);
         } else if (☃ == GameSettings.Options.MIPMAP_LEVELS) {
            return ☃x == 0.0F ? ☃ + I18n.format("options.off") : ☃ + (int)☃x;
         } else {
            return ☃xx == 0.0F ? ☃ + I18n.format("options.off") : ☃ + (int)(☃xx * 100.0F) + "%";
         }
      } else if (☃.isBoolean()) {
         boolean ☃x = this.getOptionOrdinalValue(☃);
         return ☃x ? ☃ + I18n.format("options.on") : ☃ + I18n.format("options.off");
      } else if (☃ == GameSettings.Options.MAIN_HAND) {
         return ☃ + this.mainHand;
      } else if (☃ == GameSettings.Options.GUI_SCALE) {
         return ☃ + getTranslation(GUISCALES, this.guiScale);
      } else if (☃ == GameSettings.Options.CHAT_VISIBILITY) {
         return ☃ + I18n.format(this.chatVisibility.getResourceKey());
      } else if (☃ == GameSettings.Options.PARTICLES) {
         return ☃ + getTranslation(PARTICLES, this.particleSetting);
      } else if (☃ == GameSettings.Options.AMBIENT_OCCLUSION) {
         return ☃ + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
      } else if (☃ == GameSettings.Options.RENDER_CLOUDS) {
         return ☃ + getTranslation(CLOUDS_TYPES, this.clouds);
      } else if (☃ == GameSettings.Options.GRAPHICS) {
         if (this.fancyGraphics) {
            return ☃ + I18n.format("options.graphics.fancy");
         } else {
            String ☃x = "options.graphics.fast";
            return ☃ + I18n.format("options.graphics.fast");
         }
      } else if (☃ == GameSettings.Options.ATTACK_INDICATOR) {
         return ☃ + getTranslation(ATTACK_INDICATORS, this.attackIndicator);
      } else if (☃ == GameSettings.Options.NARRATOR) {
         return NarratorChatListener.INSTANCE.isActive() ? ☃ + getTranslation(NARRATOR_MODES, this.narrator) : ☃ + I18n.format("options.narrator.notavailable");
      } else {
         return ☃;
      }
   }

   public void loadOptions() {
      try {
         if (!this.optionsFile.exists()) {
            return;
         }

         this.soundLevels.clear();
         List<String> ☃ = IOUtils.readLines(new FileInputStream(this.optionsFile));
         NBTTagCompound ☃x = new NBTTagCompound();

         for (String ☃xx : ☃) {
            try {
               Iterator<String> ☃xxx = COLON_SPLITTER.omitEmptyStrings().limit(2).split(☃xx).iterator();
               ☃x.setString(☃xxx.next(), ☃xxx.next());
            } catch (Exception var10) {
               LOGGER.warn("Skipping bad option: {}", ☃xx);
            }
         }

         ☃x = this.dataFix(☃x);

         for (String ☃xx : ☃x.getKeySet()) {
            String ☃xxx = ☃x.getString(☃xx);

            try {
               if ("mouseSensitivity".equals(☃xx)) {
                  this.mouseSensitivity = this.parseFloat(☃xxx);
               }

               if ("fov".equals(☃xx)) {
                  this.fovSetting = this.parseFloat(☃xxx) * 40.0F + 70.0F;
               }

               if ("gamma".equals(☃xx)) {
                  this.gammaSetting = this.parseFloat(☃xxx);
               }

               if ("saturation".equals(☃xx)) {
                  this.saturation = this.parseFloat(☃xxx);
               }

               if ("invertYMouse".equals(☃xx)) {
                  this.invertMouse = "true".equals(☃xxx);
               }

               if ("renderDistance".equals(☃xx)) {
                  this.renderDistanceChunks = Integer.parseInt(☃xxx);
               }

               if ("guiScale".equals(☃xx)) {
                  this.guiScale = Integer.parseInt(☃xxx);
               }

               if ("particles".equals(☃xx)) {
                  this.particleSetting = Integer.parseInt(☃xxx);
               }

               if ("bobView".equals(☃xx)) {
                  this.viewBobbing = "true".equals(☃xxx);
               }

               if ("anaglyph3d".equals(☃xx)) {
                  this.anaglyph = "true".equals(☃xxx);
               }

               if ("maxFps".equals(☃xx)) {
                  this.limitFramerate = Integer.parseInt(☃xxx);
               }

               if ("fboEnable".equals(☃xx)) {
                  this.fboEnable = "true".equals(☃xxx);
               }

               if ("difficulty".equals(☃xx)) {
                  this.difficulty = EnumDifficulty.byId(Integer.parseInt(☃xxx));
               }

               if ("fancyGraphics".equals(☃xx)) {
                  this.fancyGraphics = "true".equals(☃xxx);
               }

               if ("tutorialStep".equals(☃xx)) {
                  this.tutorialStep = TutorialSteps.getTutorial(☃xxx);
               }

               if ("ao".equals(☃xx)) {
                  if ("true".equals(☃xxx)) {
                     this.ambientOcclusion = 2;
                  } else if ("false".equals(☃xxx)) {
                     this.ambientOcclusion = 0;
                  } else {
                     this.ambientOcclusion = Integer.parseInt(☃xxx);
                  }
               }

               if ("renderClouds".equals(☃xx)) {
                  if ("true".equals(☃xxx)) {
                     this.clouds = 2;
                  } else if ("false".equals(☃xxx)) {
                     this.clouds = 0;
                  } else if ("fast".equals(☃xxx)) {
                     this.clouds = 1;
                  }
               }

               if ("attackIndicator".equals(☃xx)) {
                  if ("0".equals(☃xxx)) {
                     this.attackIndicator = 0;
                  } else if ("1".equals(☃xxx)) {
                     this.attackIndicator = 1;
                  } else if ("2".equals(☃xxx)) {
                     this.attackIndicator = 2;
                  }
               }

               if ("resourcePacks".equals(☃xx)) {
                  this.resourcePacks = JsonUtils.gsonDeserialize(GSON, ☃xxx, TYPE_LIST_STRING);
                  if (this.resourcePacks == null) {
                     this.resourcePacks = Lists.newArrayList();
                  }
               }

               if ("incompatibleResourcePacks".equals(☃xx)) {
                  this.incompatibleResourcePacks = JsonUtils.gsonDeserialize(GSON, ☃xxx, TYPE_LIST_STRING);
                  if (this.incompatibleResourcePacks == null) {
                     this.incompatibleResourcePacks = Lists.newArrayList();
                  }
               }

               if ("lastServer".equals(☃xx)) {
                  this.lastServer = ☃xxx;
               }

               if ("lang".equals(☃xx)) {
                  this.language = ☃xxx;
               }

               if ("chatVisibility".equals(☃xx)) {
                  this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(☃xxx));
               }

               if ("chatColors".equals(☃xx)) {
                  this.chatColours = "true".equals(☃xxx);
               }

               if ("chatLinks".equals(☃xx)) {
                  this.chatLinks = "true".equals(☃xxx);
               }

               if ("chatLinksPrompt".equals(☃xx)) {
                  this.chatLinksPrompt = "true".equals(☃xxx);
               }

               if ("chatOpacity".equals(☃xx)) {
                  this.chatOpacity = this.parseFloat(☃xxx);
               }

               if ("snooperEnabled".equals(☃xx)) {
                  this.snooperEnabled = "true".equals(☃xxx);
               }

               if ("fullscreen".equals(☃xx)) {
                  this.fullScreen = "true".equals(☃xxx);
               }

               if ("enableVsync".equals(☃xx)) {
                  this.enableVsync = "true".equals(☃xxx);
               }

               if ("useVbo".equals(☃xx)) {
                  this.useVbo = "true".equals(☃xxx);
               }

               if ("hideServerAddress".equals(☃xx)) {
                  this.hideServerAddress = "true".equals(☃xxx);
               }

               if ("advancedItemTooltips".equals(☃xx)) {
                  this.advancedItemTooltips = "true".equals(☃xxx);
               }

               if ("pauseOnLostFocus".equals(☃xx)) {
                  this.pauseOnLostFocus = "true".equals(☃xxx);
               }

               if ("touchscreen".equals(☃xx)) {
                  this.touchscreen = "true".equals(☃xxx);
               }

               if ("overrideHeight".equals(☃xx)) {
                  this.overrideHeight = Integer.parseInt(☃xxx);
               }

               if ("overrideWidth".equals(☃xx)) {
                  this.overrideWidth = Integer.parseInt(☃xxx);
               }

               if ("heldItemTooltips".equals(☃xx)) {
                  this.heldItemTooltips = "true".equals(☃xxx);
               }

               if ("chatHeightFocused".equals(☃xx)) {
                  this.chatHeightFocused = this.parseFloat(☃xxx);
               }

               if ("chatHeightUnfocused".equals(☃xx)) {
                  this.chatHeightUnfocused = this.parseFloat(☃xxx);
               }

               if ("chatScale".equals(☃xx)) {
                  this.chatScale = this.parseFloat(☃xxx);
               }

               if ("chatWidth".equals(☃xx)) {
                  this.chatWidth = this.parseFloat(☃xxx);
               }

               if ("mipmapLevels".equals(☃xx)) {
                  this.mipmapLevels = Integer.parseInt(☃xxx);
               }

               if ("forceUnicodeFont".equals(☃xx)) {
                  this.forceUnicodeFont = "true".equals(☃xxx);
               }

               if ("reducedDebugInfo".equals(☃xx)) {
                  this.reducedDebugInfo = "true".equals(☃xxx);
               }

               if ("useNativeTransport".equals(☃xx)) {
                  this.useNativeTransport = "true".equals(☃xxx);
               }

               if ("entityShadows".equals(☃xx)) {
                  this.entityShadows = "true".equals(☃xxx);
               }

               if ("mainHand".equals(☃xx)) {
                  this.mainHand = "left".equals(☃xxx) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
               }

               if ("showSubtitles".equals(☃xx)) {
                  this.showSubtitles = "true".equals(☃xxx);
               }

               if ("realmsNotifications".equals(☃xx)) {
                  this.realmsNotifications = "true".equals(☃xxx);
               }

               if ("enableWeakAttacks".equals(☃xx)) {
                  this.enableWeakAttacks = "true".equals(☃xxx);
               }

               if ("autoJump".equals(☃xx)) {
                  this.autoJump = "true".equals(☃xxx);
               }

               if ("narrator".equals(☃xx)) {
                  this.narrator = Integer.parseInt(☃xxx);
               }

               for (KeyBinding ☃xxxx : this.keyBindings) {
                  if (☃xx.equals("key_" + ☃xxxx.getKeyDescription())) {
                     ☃xxxx.setKeyCode(Integer.parseInt(☃xxx));
                  }
               }

               for (SoundCategory ☃xxxxx : SoundCategory.values()) {
                  if (☃xx.equals("soundCategory_" + ☃xxxxx.getName())) {
                     this.soundLevels.put(☃xxxxx, this.parseFloat(☃xxx));
                  }
               }

               for (EnumPlayerModelParts ☃xxxxxx : EnumPlayerModelParts.values()) {
                  if (☃xx.equals("modelPart_" + ☃xxxxxx.getPartName())) {
                     this.setModelPartEnabled(☃xxxxxx, "true".equals(☃xxx));
                  }
               }
            } catch (Exception var11) {
               LOGGER.warn("Skipping bad option: {}:{}", ☃xx, ☃xxx);
            }
         }

         KeyBinding.resetKeyBindingArrayAndHash();
      } catch (Exception var12) {
         LOGGER.error("Failed to load options", var12);
      }
   }

   private NBTTagCompound dataFix(NBTTagCompound var1) {
      int ☃ = 0;

      try {
         ☃ = Integer.parseInt(☃.getString("version"));
      } catch (RuntimeException var4) {
      }

      return this.mc.getDataFixer().process(FixTypes.OPTIONS, ☃, ☃);
   }

   private float parseFloat(String var1) {
      if ("true".equals(☃)) {
         return 1.0F;
      } else {
         return "false".equals(☃) ? 0.0F : Float.parseFloat(☃);
      }
   }

   public void saveOptions() {
      PrintWriter ☃ = null;

      try {
         ☃ = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
         ☃.println("version:1343");
         ☃.println("invertYMouse:" + this.invertMouse);
         ☃.println("mouseSensitivity:" + this.mouseSensitivity);
         ☃.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
         ☃.println("gamma:" + this.gammaSetting);
         ☃.println("saturation:" + this.saturation);
         ☃.println("renderDistance:" + this.renderDistanceChunks);
         ☃.println("guiScale:" + this.guiScale);
         ☃.println("particles:" + this.particleSetting);
         ☃.println("bobView:" + this.viewBobbing);
         ☃.println("anaglyph3d:" + this.anaglyph);
         ☃.println("maxFps:" + this.limitFramerate);
         ☃.println("fboEnable:" + this.fboEnable);
         ☃.println("difficulty:" + this.difficulty.getId());
         ☃.println("fancyGraphics:" + this.fancyGraphics);
         ☃.println("ao:" + this.ambientOcclusion);
         switch (this.clouds) {
            case 0:
               ☃.println("renderClouds:false");
               break;
            case 1:
               ☃.println("renderClouds:fast");
               break;
            case 2:
               ☃.println("renderClouds:true");
         }

         ☃.println("resourcePacks:" + GSON.toJson(this.resourcePacks));
         ☃.println("incompatibleResourcePacks:" + GSON.toJson(this.incompatibleResourcePacks));
         ☃.println("lastServer:" + this.lastServer);
         ☃.println("lang:" + this.language);
         ☃.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
         ☃.println("chatColors:" + this.chatColours);
         ☃.println("chatLinks:" + this.chatLinks);
         ☃.println("chatLinksPrompt:" + this.chatLinksPrompt);
         ☃.println("chatOpacity:" + this.chatOpacity);
         ☃.println("snooperEnabled:" + this.snooperEnabled);
         ☃.println("fullscreen:" + this.fullScreen);
         ☃.println("enableVsync:" + this.enableVsync);
         ☃.println("useVbo:" + this.useVbo);
         ☃.println("hideServerAddress:" + this.hideServerAddress);
         ☃.println("advancedItemTooltips:" + this.advancedItemTooltips);
         ☃.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
         ☃.println("touchscreen:" + this.touchscreen);
         ☃.println("overrideWidth:" + this.overrideWidth);
         ☃.println("overrideHeight:" + this.overrideHeight);
         ☃.println("heldItemTooltips:" + this.heldItemTooltips);
         ☃.println("chatHeightFocused:" + this.chatHeightFocused);
         ☃.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
         ☃.println("chatScale:" + this.chatScale);
         ☃.println("chatWidth:" + this.chatWidth);
         ☃.println("mipmapLevels:" + this.mipmapLevels);
         ☃.println("forceUnicodeFont:" + this.forceUnicodeFont);
         ☃.println("reducedDebugInfo:" + this.reducedDebugInfo);
         ☃.println("useNativeTransport:" + this.useNativeTransport);
         ☃.println("entityShadows:" + this.entityShadows);
         ☃.println("mainHand:" + (this.mainHand == EnumHandSide.LEFT ? "left" : "right"));
         ☃.println("attackIndicator:" + this.attackIndicator);
         ☃.println("showSubtitles:" + this.showSubtitles);
         ☃.println("realmsNotifications:" + this.realmsNotifications);
         ☃.println("enableWeakAttacks:" + this.enableWeakAttacks);
         ☃.println("autoJump:" + this.autoJump);
         ☃.println("narrator:" + this.narrator);
         ☃.println("tutorialStep:" + this.tutorialStep.getName());

         for (KeyBinding ☃x : this.keyBindings) {
            ☃.println("key_" + ☃x.getKeyDescription() + ":" + ☃x.getKeyCode());
         }

         for (SoundCategory ☃x : SoundCategory.values()) {
            ☃.println("soundCategory_" + ☃x.getName() + ":" + this.getSoundLevel(☃x));
         }

         for (EnumPlayerModelParts ☃x : EnumPlayerModelParts.values()) {
            ☃.println("modelPart_" + ☃x.getPartName() + ":" + this.setModelParts.contains(☃x));
         }
      } catch (Exception var9) {
         LOGGER.error("Failed to save options", var9);
      } finally {
         IOUtils.closeQuietly(☃);
      }

      this.sendSettingsToServer();
   }

   public float getSoundLevel(SoundCategory var1) {
      return this.soundLevels.containsKey(☃) ? this.soundLevels.get(☃) : 1.0F;
   }

   public void setSoundLevel(SoundCategory var1, float var2) {
      this.mc.getSoundHandler().setSoundLevel(☃, ☃);
      this.soundLevels.put(☃, ☃);
   }

   public void sendSettingsToServer() {
      if (this.mc.player != null) {
         int ☃ = 0;

         for (EnumPlayerModelParts ☃x : this.setModelParts) {
            ☃ |= ☃x.getPartMask();
         }

         this.mc
            .player
            .connection
            .sendPacket(new CPacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, ☃, this.mainHand));
      }
   }

   public Set<EnumPlayerModelParts> getModelParts() {
      return ImmutableSet.copyOf(this.setModelParts);
   }

   public void setModelPartEnabled(EnumPlayerModelParts var1, boolean var2) {
      if (☃) {
         this.setModelParts.add(☃);
      } else {
         this.setModelParts.remove(☃);
      }

      this.sendSettingsToServer();
   }

   public void switchModelPartEnabled(EnumPlayerModelParts var1) {
      if (this.getModelParts().contains(☃)) {
         this.setModelParts.remove(☃);
      } else {
         this.setModelParts.add(☃);
      }

      this.sendSettingsToServer();
   }

   public int shouldRenderClouds() {
      return this.renderDistanceChunks >= 4 ? this.clouds : 0;
   }

   public boolean isUsingNativeTransport() {
      return this.useNativeTransport;
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
      FRAMERATE_LIMIT("options.framerateLimit", true, false, 10.0F, 260.0F, 10.0F),
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
      NARRATOR("options.narrator", false, false);

      private final boolean isFloat;
      private final boolean isBoolean;
      private final String translation;
      private final float valueStep;
      private float valueMin;
      private float valueMax;

      public static GameSettings.Options byOrdinal(int var0) {
         for (GameSettings.Options ☃ : values()) {
            if (☃.getOrdinal() == ☃) {
               return ☃;
            }
         }

         return null;
      }

      private Options(String var3, boolean var4, boolean var5) {
         this(☃, ☃, ☃, 0.0F, 1.0F, 0.0F);
      }

      private Options(String var3, boolean var4, boolean var5, float var6, float var7, float var8) {
         this.translation = ☃;
         this.isFloat = ☃;
         this.isBoolean = ☃;
         this.valueMin = ☃;
         this.valueMax = ☃;
         this.valueStep = ☃;
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

      public void setValueMax(float var1) {
         this.valueMax = ☃;
      }

      public float normalizeValue(float var1) {
         return MathHelper.clamp((this.snapToStepClamp(☃) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
      }

      public float denormalizeValue(float var1) {
         return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(☃, 0.0F, 1.0F));
      }

      public float snapToStepClamp(float var1) {
         ☃ = this.snapToStep(☃);
         return MathHelper.clamp(☃, this.valueMin, this.valueMax);
      }

      private float snapToStep(float var1) {
         if (this.valueStep > 0.0F) {
            ☃ = this.valueStep * Math.round(☃ / this.valueStep);
         }

         return ☃;
      }
   }
}
