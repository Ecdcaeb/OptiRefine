/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.base.Splitter
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.gson.Gson
 *  java.io.BufferedReader
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.FileOutputStream
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.OutputStream
 *  java.io.OutputStreamWriter
 *  java.io.PrintWriter
 *  java.io.Reader
 *  java.io.Writer
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runtime
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Type
 *  java.nio.charset.Charset
 *  java.nio.charset.StandardCharsets
 *  java.util.Arrays
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.chat.NarratorChatListener
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.GameSettings$2
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.client.tutorial.TutorialSteps
 *  net.minecraft.entity.player.EntityPlayer$EnumChatVisibility
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClientSettings
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.util.JsonUtils
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.datafix.FixTypes
 *  net.minecraft.util.datafix.IFixType
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.EnumDifficulty
 *  net.minecraft.world.World
 *  net.minecraftforge.client.resource.IResourceType
 *  net.optifine.ClearWater
 *  net.optifine.CustomColors
 *  net.optifine.CustomGuis
 *  net.optifine.CustomSky
 *  net.optifine.DynamicLights
 *  net.optifine.Lang
 *  net.optifine.NaturalTextures
 *  net.optifine.RandomEntities
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorField
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.shaders.Shaders
 *  net.optifine.util.KeyUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.DisplayMode
 */
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
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
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
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
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorMethod;
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

/*
 * Exception performing whole class analysis ignored.
 */
public class GameSettings {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    private static final Type TYPE_LIST_STRING = new /* Unavailable Anonymous Inner Class!! */;
    public static final Splitter COLON_SPLITTER = Splitter.on((char)':');
    private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] PARTICLES = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] AMBIENT_OCCLUSIONS = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] CLOUDS_TYPES = new String[]{"options.off", "options.clouds.fast", "options.clouds.fancy"};
    private static final String[] ATTACK_INDICATORS = new String[]{"options.off", "options.attack.crosshair", "options.attack.hotbar"};
    public static final String[] NARRATOR_MODES = new String[]{"options.narrator.off", "options.narrator.all", "options.narrator.chat", "options.narrator.system"};
    public float mouseSensitivity = 0.5f;
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
    public float chatOpacity = 1.0f;
    public boolean snooperEnabled = true;
    public boolean fullScreen;
    public boolean enableVsync = true;
    public boolean useVbo = true;
    public boolean reducedDebugInfo;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus = true;
    private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
    public boolean touchscreen;
    public EnumHandSide mainHand = EnumHandSide.RIGHT;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips = true;
    public float chatScale = 1.0f;
    public float chatWidth = 1.0f;
    public float chatHeightUnfocused = 0.44366196f;
    public float chatHeightFocused = 1.0f;
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
    public KeyBinding[] keyBindsHotbar = new KeyBinding[]{new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
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
    public float ofFogStart = 0.8f;
    public int ofMipmapType = 0;
    public boolean ofOcclusionFancy = false;
    public boolean ofSmoothFps = false;
    public boolean ofSmoothWorld = Config.isSingleProcessor();
    public boolean ofLazyChunkLoading = Config.isSingleProcessor();
    public boolean ofRenderRegions = false;
    public boolean ofSmartAnimations = false;
    public float ofAoLevel = 1.0f;
    public int ofAaLevel = 0;
    public int ofAfLevel = 1;
    public int ofClouds = 0;
    public float ofCloudsHeight = 0.0f;
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
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindFullscreen, this.keyBindSpectatorOutlines, this.keyBindSwapHands, this.keyBindSaveToolbar, this.keyBindLoadToolbar, this.keyBindAdvancements}, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_us";
        this.mc = mcIn;
        this.optionsFile = new File(optionsFileIn, "options.txt");
        if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
            Options.RENDER_DISTANCE.setValueMax(32.0f);
            long MB = 1000000L;
            if (Runtime.getRuntime().maxMemory() >= 1500L * MB) {
                Options.RENDER_DISTANCE.setValueMax(48.0f);
            }
            if (Runtime.getRuntime().maxMemory() >= 2500L * MB) {
                Options.RENDER_DISTANCE.setValueMax(64.0f);
            }
        } else {
            Options.RENDER_DISTANCE.setValueMax(16.0f);
        }
        this.renderDistanceChunks = mcIn.isJava64bit() ? 12 : 8;
        this.optionsFileOF = new File(optionsFileIn, "optionsof.txt");
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
        this.keyBindings = (KeyBinding[])ArrayUtils.add((Object[])this.keyBindings, (Object)this.ofKeyBindZoom);
        KeyUtils.fixKeyConflicts((KeyBinding[])this.keyBindings, (KeyBinding[])new KeyBinding[]{this.ofKeyBindZoom});
        this.renderDistanceChunks = 8;
        this.loadOptions();
        Config.initGameSettings((GameSettings)this);
    }

    public GameSettings() {
        this.setForgeKeybindProperties();
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindFullscreen, this.keyBindSpectatorOutlines, this.keyBindSwapHands, this.keyBindSaveToolbar, this.keyBindLoadToolbar, this.keyBindAdvancements}, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_us";
    }

    public static String getKeyDisplayString(int key) {
        if (key < 0) {
            switch (key) {
                case -100: {
                    return I18n.format((String)"key.mouse.left", (Object[])new Object[0]);
                }
                case -99: {
                    return I18n.format((String)"key.mouse.right", (Object[])new Object[0]);
                }
                case -98: {
                    return I18n.format((String)"key.mouse.middle", (Object[])new Object[0]);
                }
            }
            return I18n.format((String)"key.mouseButton", (Object[])new Object[]{key + 101});
        }
        return key < 256 ? Keyboard.getKeyName((int)key) : String.format((String)"%c", (Object[])new Object[]{Character.valueOf((char)((char)(key - 256)))}).toUpperCase();
    }

    public static boolean isKeyDown(KeyBinding key) {
        int i = key.getKeyCode();
        if (i != 0 && i < 256) {
            return i < 0 ? Mouse.isButtonDown((int)(i + 100)) : Keyboard.isKeyDown((int)i);
        }
        return false;
    }

    public void setOptionKeyBinding(KeyBinding key, int keyCode) {
        key.setKeyCode(keyCode);
        this.saveOptions();
    }

    public void setOptionFloatValue(Options settingsOption, float value) {
        this.setOptionFloatValueOF(settingsOption, value);
        if (settingsOption == Options.SENSITIVITY) {
            this.mouseSensitivity = value;
        }
        if (settingsOption == Options.FOV) {
            this.fovSetting = value;
        }
        if (settingsOption == Options.GAMMA) {
            this.gammaSetting = value;
        }
        if (settingsOption == Options.FRAMERATE_LIMIT) {
            this.limitFramerate = (int)value;
            this.enableVsync = false;
            if (this.limitFramerate <= 0) {
                this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                this.enableVsync = true;
            }
            this.updateVSync();
        }
        if (settingsOption == Options.CHAT_OPACITY) {
            this.chatOpacity = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_HEIGHT_FOCUSED) {
            this.chatHeightFocused = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_HEIGHT_UNFOCUSED) {
            this.chatHeightUnfocused = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_WIDTH) {
            this.chatWidth = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.CHAT_SCALE) {
            this.chatScale = value;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }
        if (settingsOption == Options.MIPMAP_LEVELS) {
            int i = this.mipmapLevels;
            this.mipmapLevels = (int)value;
            if ((float)i != value) {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
                this.mc.scheduleResourcesRefresh();
            }
        }
        if (settingsOption == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)value;
            this.mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    public void setOptionValue(Options settingsOption, int value) {
        this.setOptionValueOF(settingsOption, value);
        if (settingsOption == Options.RENDER_DISTANCE) {
            this.setOptionFloatValue(settingsOption, MathHelper.clamp((float)(this.renderDistanceChunks + value), (float)settingsOption.getValueMin(), (float)settingsOption.getValueMax()));
        }
        if (settingsOption == Options.MAIN_HAND) {
            this.mainHand = this.mainHand.opposite();
        }
        if (settingsOption == Options.INVERT_MOUSE) {
            boolean bl = this.invertMouse = !this.invertMouse;
        }
        if (settingsOption == Options.GUI_SCALE) {
            this.guiScale += value;
            if (GuiScreen.isShiftKeyDown()) {
                this.guiScale = 0;
            }
            DisplayMode mode = Config.getLargestDisplayMode();
            int maxScaleWidth = mode.getWidth() / 320;
            int maxScaleHeight = mode.getHeight() / 240;
            int maxGuiScale = Math.min((int)maxScaleWidth, (int)maxScaleHeight);
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
        if (settingsOption == Options.PARTICLES) {
            this.particleSetting = (this.particleSetting + value) % 3;
        }
        if (settingsOption == Options.VIEW_BOBBING) {
            boolean bl = this.viewBobbing = !this.viewBobbing;
        }
        if (settingsOption == Options.RENDER_CLOUDS) {
            this.clouds = (this.clouds + value) % 3;
        }
        if (settingsOption == Options.FORCE_UNICODE_FONT) {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRenderer.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }
        if (settingsOption == Options.FBO_ENABLE) {
            boolean bl = this.fboEnable = !this.fboEnable;
        }
        if (settingsOption == Options.ANAGLYPH) {
            if (!this.anaglyph && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.an.shaders1"), (String)Lang.get((String)"of.message.an.shaders2"));
                return;
            }
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
            if (Reflector.FMLClientHandler_refreshResources.exists()) {
                Object instance = Reflector.call((ReflectorMethod)Reflector.FMLClientHandler_instance, (Object[])new Object[0]);
                IResourceType type = (IResourceType)Reflector.VanillaResourceType_TEXTURES.getValue();
                Reflector.call((Object)instance, (ReflectorMethod)Reflector.FMLClientHandler_refreshResources, (Object[])new IResourceType[]{type});
            }
        }
        if (settingsOption == Options.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.updateRenderClouds();
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = (this.ambientOcclusion + value) % 3;
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.CHAT_VISIBILITY) {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((int)((this.chatVisibility.getChatVisibility() + value) % 3));
        }
        if (settingsOption == Options.CHAT_COLOR) {
            boolean bl = this.chatColours = !this.chatColours;
        }
        if (settingsOption == Options.CHAT_LINKS) {
            boolean bl = this.chatLinks = !this.chatLinks;
        }
        if (settingsOption == Options.CHAT_LINKS_PROMPT) {
            boolean bl = this.chatLinksPrompt = !this.chatLinksPrompt;
        }
        if (settingsOption == Options.SNOOPER_ENABLED) {
            boolean bl = this.snooperEnabled = !this.snooperEnabled;
        }
        if (settingsOption == Options.TOUCHSCREEN) {
            boolean bl = this.touchscreen = !this.touchscreen;
        }
        if (settingsOption == Options.USE_FULLSCREEN) {
            boolean bl = this.fullScreen = !this.fullScreen;
            if (this.mc.isFullScreen() != this.fullScreen) {
                this.mc.toggleFullscreen();
            }
        }
        if (settingsOption == Options.ENABLE_VSYNC) {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled((boolean)this.enableVsync);
        }
        if (settingsOption == Options.USE_VBO) {
            this.useVbo = !this.useVbo;
            this.mc.renderGlobal.loadRenderers();
        }
        if (settingsOption == Options.REDUCED_DEBUG_INFO) {
            boolean bl = this.reducedDebugInfo = !this.reducedDebugInfo;
        }
        if (settingsOption == Options.ENTITY_SHADOWS) {
            boolean bl = this.entityShadows = !this.entityShadows;
        }
        if (settingsOption == Options.ATTACK_INDICATOR) {
            this.attackIndicator = (this.attackIndicator + value) % 3;
        }
        if (settingsOption == Options.SHOW_SUBTITLES) {
            boolean bl = this.showSubtitles = !this.showSubtitles;
        }
        if (settingsOption == Options.REALMS_NOTIFICATIONS) {
            boolean bl = this.realmsNotifications = !this.realmsNotifications;
        }
        if (settingsOption == Options.AUTO_JUMP) {
            boolean bl = this.autoJump = !this.autoJump;
        }
        if (settingsOption == Options.NARRATOR) {
            this.narrator = NarratorChatListener.INSTANCE.isActive() ? (this.narrator + value) % NARRATOR_MODES.length : 0;
            NarratorChatListener.INSTANCE.announceMode(this.narrator);
        }
        this.saveOptions();
    }

    public float getOptionFloatValue(Options settingOption) {
        float valOF = this.getOptionFloatValueOF(settingOption);
        if (valOF != Float.MAX_VALUE) {
            return valOF;
        }
        if (settingOption == Options.FOV) {
            return this.fovSetting;
        }
        if (settingOption == Options.GAMMA) {
            return this.gammaSetting;
        }
        if (settingOption == Options.SATURATION) {
            return this.saturation;
        }
        if (settingOption == Options.SENSITIVITY) {
            return this.mouseSensitivity;
        }
        if (settingOption == Options.CHAT_OPACITY) {
            return this.chatOpacity;
        }
        if (settingOption == Options.CHAT_HEIGHT_FOCUSED) {
            return this.chatHeightFocused;
        }
        if (settingOption == Options.CHAT_HEIGHT_UNFOCUSED) {
            return this.chatHeightUnfocused;
        }
        if (settingOption == Options.CHAT_SCALE) {
            return this.chatScale;
        }
        if (settingOption == Options.CHAT_WIDTH) {
            return this.chatWidth;
        }
        if (settingOption == Options.FRAMERATE_LIMIT) {
            return this.limitFramerate;
        }
        if (settingOption == Options.MIPMAP_LEVELS) {
            return this.mipmapLevels;
        }
        return settingOption == Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : 0.0f;
    }

    public boolean getOptionOrdinalValue(Options settingOption) {
        switch (2.$SwitchMap$net$minecraft$client$settings$GameSettings$Options[settingOption.ordinal()]) {
            case 1: {
                return this.invertMouse;
            }
            case 2: {
                return this.viewBobbing;
            }
            case 3: {
                return this.anaglyph;
            }
            case 4: {
                return this.fboEnable;
            }
            case 5: {
                return this.chatColours;
            }
            case 6: {
                return this.chatLinks;
            }
            case 7: {
                return this.chatLinksPrompt;
            }
            case 8: {
                return this.snooperEnabled;
            }
            case 9: {
                return this.fullScreen;
            }
            case 10: {
                return this.enableVsync;
            }
            case 11: {
                return this.useVbo;
            }
            case 12: {
                return this.touchscreen;
            }
            case 13: {
                return this.forceUnicodeFont;
            }
            case 14: {
                return this.reducedDebugInfo;
            }
            case 15: {
                return this.entityShadows;
            }
            case 16: {
                return this.showSubtitles;
            }
            case 17: {
                return this.realmsNotifications;
            }
            case 18: {
                return this.enableWeakAttacks;
            }
            case 19: {
                return this.autoJump;
            }
        }
        return false;
    }

    private static String getTranslation(String[] strArray, int index) {
        if (index < 0 || index >= strArray.length) {
            index = 0;
        }
        return I18n.format((String)strArray[index], (Object[])new Object[0]);
    }

    public String getKeyBinding(Options settingOption) {
        String strOF = this.getKeyBindingOF(settingOption);
        if (strOF != null) {
            return strOF;
        }
        String s = I18n.format((String)settingOption.getTranslation(), (Object[])new Object[0]) + ": ";
        if (settingOption.isFloat()) {
            float f1 = this.getOptionFloatValue(settingOption);
            float f = settingOption.normalizeValue(f1);
            if (settingOption == Options.SENSITIVITY) {
                if (f == 0.0f) {
                    return s + I18n.format((String)"options.sensitivity.min", (Object[])new Object[0]);
                }
                return f == 1.0f ? s + I18n.format((String)"options.sensitivity.max", (Object[])new Object[0]) : s + (int)(f * 200.0f) + "%";
            }
            if (settingOption == Options.FOV) {
                if (f1 == 70.0f) {
                    return s + I18n.format((String)"options.fov.min", (Object[])new Object[0]);
                }
                return f1 == 110.0f ? s + I18n.format((String)"options.fov.max", (Object[])new Object[0]) : s + (int)f1;
            }
            if (settingOption == Options.FRAMERATE_LIMIT) {
                return f1 == Options.access$000((Options)settingOption) ? s + I18n.format((String)"options.framerateLimit.max", (Object[])new Object[0]) : s + I18n.format((String)"options.framerate", (Object[])new Object[]{(int)f1});
            }
            if (settingOption == Options.RENDER_CLOUDS) {
                return f1 == Options.access$100((Options)settingOption) ? s + I18n.format((String)"options.cloudHeight.min", (Object[])new Object[0]) : s + ((int)f1 + 128);
            }
            if (settingOption == Options.GAMMA) {
                if (f == 0.0f) {
                    return s + I18n.format((String)"options.gamma.min", (Object[])new Object[0]);
                }
                return f == 1.0f ? s + I18n.format((String)"options.gamma.max", (Object[])new Object[0]) : s + "+" + (int)(f * 100.0f) + "%";
            }
            if (settingOption == Options.SATURATION) {
                return s + (int)(f * 400.0f) + "%";
            }
            if (settingOption == Options.CHAT_OPACITY) {
                return s + (int)(f * 90.0f + 10.0f) + "%";
            }
            if (settingOption == Options.CHAT_HEIGHT_UNFOCUSED) {
                return s + GuiNewChat.calculateChatboxHeight((float)f) + "px";
            }
            if (settingOption == Options.CHAT_HEIGHT_FOCUSED) {
                return s + GuiNewChat.calculateChatboxHeight((float)f) + "px";
            }
            if (settingOption == Options.CHAT_WIDTH) {
                return s + GuiNewChat.calculateChatboxWidth((float)f) + "px";
            }
            if (settingOption == Options.RENDER_DISTANCE) {
                return s + I18n.format((String)"options.chunks", (Object[])new Object[]{(int)f1});
            }
            if (settingOption == Options.MIPMAP_LEVELS) {
                if ((double)f1 >= 4.0) {
                    return s + Lang.get((String)"of.general.max");
                }
                return f1 == 0.0f ? s + I18n.format((String)"options.off", (Object[])new Object[0]) : s + (int)f1;
            }
            return f == 0.0f ? s + I18n.format((String)"options.off", (Object[])new Object[0]) : s + (int)(f * 100.0f) + "%";
        }
        if (settingOption.isBoolean()) {
            boolean flag = this.getOptionOrdinalValue(settingOption);
            return flag ? s + I18n.format((String)"options.on", (Object[])new Object[0]) : s + I18n.format((String)"options.off", (Object[])new Object[0]);
        }
        if (settingOption == Options.MAIN_HAND) {
            return s + this.mainHand;
        }
        if (settingOption == Options.GUI_SCALE) {
            if (this.guiScale >= GUISCALES.length) {
                return s + this.guiScale + "x";
            }
            return s + GameSettings.getTranslation(GUISCALES, this.guiScale);
        }
        if (settingOption == Options.CHAT_VISIBILITY) {
            return s + I18n.format((String)this.chatVisibility.getResourceKey(), (Object[])new Object[0]);
        }
        if (settingOption == Options.PARTICLES) {
            return s + GameSettings.getTranslation(PARTICLES, this.particleSetting);
        }
        if (settingOption == Options.AMBIENT_OCCLUSION) {
            return s + GameSettings.getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        if (settingOption == Options.RENDER_CLOUDS) {
            return s + GameSettings.getTranslation(CLOUDS_TYPES, this.clouds);
        }
        if (settingOption == Options.GRAPHICS) {
            if (this.fancyGraphics) {
                return s + I18n.format((String)"options.graphics.fancy", (Object[])new Object[0]);
            }
            String s1 = "options.graphics.fast";
            return s + I18n.format((String)"options.graphics.fast", (Object[])new Object[0]);
        }
        if (settingOption == Options.ATTACK_INDICATOR) {
            return s + GameSettings.getTranslation(ATTACK_INDICATORS, this.attackIndicator);
        }
        if (settingOption == Options.NARRATOR) {
            return NarratorChatListener.INSTANCE.isActive() ? s + GameSettings.getTranslation(NARRATOR_MODES, this.narrator) : s + I18n.format((String)"options.narrator.notavailable", (Object[])new Object[0]);
        }
        return s;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadOptions() {
        FileInputStream is = null;
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            this.soundLevels.clear();
            is = new FileInputStream(this.optionsFile);
            List list = IOUtils.readLines((InputStream)is, (Charset)StandardCharsets.UTF_8);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (String s : list) {
                try {
                    Iterator iterator = COLON_SPLITTER.omitEmptyStrings().limit(2).split((CharSequence)s).iterator();
                    nbttagcompound.setString((String)iterator.next(), (String)iterator.next());
                }
                catch (Exception var10) {
                    LOGGER.warn("Skipping bad option: {}", (Object)s);
                }
            }
            nbttagcompound = this.dataFix(nbttagcompound);
            for (String s1 : nbttagcompound.getKeySet()) {
                String s2 = nbttagcompound.getString(s1);
                try {
                    if ("mouseSensitivity".equals((Object)s1)) {
                        this.mouseSensitivity = this.parseFloat(s2);
                    }
                    if ("fov".equals((Object)s1)) {
                        this.fovSetting = this.parseFloat(s2) * 40.0f + 70.0f;
                    }
                    if ("gamma".equals((Object)s1)) {
                        this.gammaSetting = this.parseFloat(s2);
                    }
                    if ("saturation".equals((Object)s1)) {
                        this.saturation = this.parseFloat(s2);
                    }
                    if ("invertYMouse".equals((Object)s1)) {
                        this.invertMouse = "true".equals((Object)s2);
                    }
                    if ("renderDistance".equals((Object)s1)) {
                        this.renderDistanceChunks = Integer.parseInt((String)s2);
                    }
                    if ("guiScale".equals((Object)s1)) {
                        this.guiScale = Integer.parseInt((String)s2);
                    }
                    if ("particles".equals((Object)s1)) {
                        this.particleSetting = Integer.parseInt((String)s2);
                    }
                    if ("bobView".equals((Object)s1)) {
                        this.viewBobbing = "true".equals((Object)s2);
                    }
                    if ("anaglyph3d".equals((Object)s1)) {
                        this.anaglyph = "true".equals((Object)s2);
                    }
                    if ("maxFps".equals((Object)s1)) {
                        this.limitFramerate = Integer.parseInt((String)s2);
                        if (this.enableVsync) {
                            this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                        }
                        if (this.limitFramerate <= 0) {
                            this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                        }
                    }
                    if ("fboEnable".equals((Object)s1)) {
                        this.fboEnable = "true".equals((Object)s2);
                    }
                    if ("difficulty".equals((Object)s1)) {
                        this.difficulty = EnumDifficulty.byId((int)Integer.parseInt((String)s2));
                    }
                    if ("fancyGraphics".equals((Object)s1)) {
                        this.fancyGraphics = "true".equals((Object)s2);
                        this.updateRenderClouds();
                    }
                    if ("tutorialStep".equals((Object)s1)) {
                        this.tutorialStep = TutorialSteps.getTutorial((String)s2);
                    }
                    if ("ao".equals((Object)s1)) {
                        this.ambientOcclusion = "true".equals((Object)s2) ? 2 : ("false".equals((Object)s2) ? 0 : Integer.parseInt((String)s2));
                    }
                    if ("renderClouds".equals((Object)s1)) {
                        if ("true".equals((Object)s2)) {
                            this.clouds = 2;
                        } else if ("false".equals((Object)s2)) {
                            this.clouds = 0;
                        } else if ("fast".equals((Object)s2)) {
                            this.clouds = 1;
                        }
                    }
                    if ("attackIndicator".equals((Object)s1)) {
                        if ("0".equals((Object)s2)) {
                            this.attackIndicator = 0;
                        } else if ("1".equals((Object)s2)) {
                            this.attackIndicator = 1;
                        } else if ("2".equals((Object)s2)) {
                            this.attackIndicator = 2;
                        }
                    }
                    if ("resourcePacks".equals((Object)s1)) {
                        this.resourcePacks = (List)JsonUtils.gsonDeserialize((Gson)GSON, (String)s2, (Type)TYPE_LIST_STRING);
                        if (this.resourcePacks == null) {
                            this.resourcePacks = Lists.newArrayList();
                        }
                    }
                    if ("incompatibleResourcePacks".equals((Object)s1)) {
                        this.incompatibleResourcePacks = (List)JsonUtils.gsonDeserialize((Gson)GSON, (String)s2, (Type)TYPE_LIST_STRING);
                        if (this.incompatibleResourcePacks == null) {
                            this.incompatibleResourcePacks = Lists.newArrayList();
                        }
                    }
                    if ("lastServer".equals((Object)s1)) {
                        this.lastServer = s2;
                    }
                    if ("lang".equals((Object)s1)) {
                        this.language = s2;
                    }
                    if ("chatVisibility".equals((Object)s1)) {
                        this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((int)Integer.parseInt((String)s2));
                    }
                    if ("chatColors".equals((Object)s1)) {
                        this.chatColours = "true".equals((Object)s2);
                    }
                    if ("chatLinks".equals((Object)s1)) {
                        this.chatLinks = "true".equals((Object)s2);
                    }
                    if ("chatLinksPrompt".equals((Object)s1)) {
                        this.chatLinksPrompt = "true".equals((Object)s2);
                    }
                    if ("chatOpacity".equals((Object)s1)) {
                        this.chatOpacity = this.parseFloat(s2);
                    }
                    if ("snooperEnabled".equals((Object)s1)) {
                        this.snooperEnabled = "true".equals((Object)s2);
                    }
                    if ("fullscreen".equals((Object)s1)) {
                        this.fullScreen = "true".equals((Object)s2);
                    }
                    if ("enableVsync".equals((Object)s1)) {
                        this.enableVsync = "true".equals((Object)s2);
                        if (this.enableVsync) {
                            this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
                        }
                        this.updateVSync();
                    }
                    if ("useVbo".equals((Object)s1)) {
                        this.useVbo = "true".equals((Object)s2);
                    }
                    if ("hideServerAddress".equals((Object)s1)) {
                        this.hideServerAddress = "true".equals((Object)s2);
                    }
                    if ("advancedItemTooltips".equals((Object)s1)) {
                        this.advancedItemTooltips = "true".equals((Object)s2);
                    }
                    if ("pauseOnLostFocus".equals((Object)s1)) {
                        this.pauseOnLostFocus = "true".equals((Object)s2);
                    }
                    if ("touchscreen".equals((Object)s1)) {
                        this.touchscreen = "true".equals((Object)s2);
                    }
                    if ("overrideHeight".equals((Object)s1)) {
                        this.overrideHeight = Integer.parseInt((String)s2);
                    }
                    if ("overrideWidth".equals((Object)s1)) {
                        this.overrideWidth = Integer.parseInt((String)s2);
                    }
                    if ("heldItemTooltips".equals((Object)s1)) {
                        this.heldItemTooltips = "true".equals((Object)s2);
                    }
                    if ("chatHeightFocused".equals((Object)s1)) {
                        this.chatHeightFocused = this.parseFloat(s2);
                    }
                    if ("chatHeightUnfocused".equals((Object)s1)) {
                        this.chatHeightUnfocused = this.parseFloat(s2);
                    }
                    if ("chatScale".equals((Object)s1)) {
                        this.chatScale = this.parseFloat(s2);
                    }
                    if ("chatWidth".equals((Object)s1)) {
                        this.chatWidth = this.parseFloat(s2);
                    }
                    if ("mipmapLevels".equals((Object)s1)) {
                        this.mipmapLevels = Integer.parseInt((String)s2);
                    }
                    if ("forceUnicodeFont".equals((Object)s1)) {
                        this.forceUnicodeFont = "true".equals((Object)s2);
                    }
                    if ("reducedDebugInfo".equals((Object)s1)) {
                        this.reducedDebugInfo = "true".equals((Object)s2);
                    }
                    if ("useNativeTransport".equals((Object)s1)) {
                        this.useNativeTransport = "true".equals((Object)s2);
                    }
                    if ("entityShadows".equals((Object)s1)) {
                        this.entityShadows = "true".equals((Object)s2);
                    }
                    if ("mainHand".equals((Object)s1)) {
                        EnumHandSide enumHandSide = this.mainHand = "left".equals((Object)s2) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
                    }
                    if ("showSubtitles".equals((Object)s1)) {
                        this.showSubtitles = "true".equals((Object)s2);
                    }
                    if ("realmsNotifications".equals((Object)s1)) {
                        this.realmsNotifications = "true".equals((Object)s2);
                    }
                    if ("enableWeakAttacks".equals((Object)s1)) {
                        this.enableWeakAttacks = "true".equals((Object)s2);
                    }
                    if ("autoJump".equals((Object)s1)) {
                        this.autoJump = "true".equals((Object)s2);
                    }
                    if ("narrator".equals((Object)s1)) {
                        this.narrator = Integer.parseInt((String)s2);
                    }
                    for (KeyBinding keyBinding : this.keyBindings) {
                        if (!s1.equals((Object)("key_" + keyBinding.getKeyDescription()))) continue;
                        if (Reflector.KeyModifier_valueFromString.exists()) {
                            if (s2.indexOf(58) != -1) {
                                String[] t = s2.split(":");
                                Object keyModifier = Reflector.call((ReflectorMethod)Reflector.KeyModifier_valueFromString, (Object[])new Object[]{t[1]});
                                Reflector.call((Object)keyBinding, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyModifierAndCode, (Object[])new Object[]{keyModifier, Integer.parseInt((String)t[0])});
                                continue;
                            }
                            Object keyModifierNone = Reflector.getFieldValue((ReflectorField)Reflector.KeyModifier_NONE);
                            Reflector.call((Object)keyBinding, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyModifierAndCode, (Object[])new Object[]{keyModifierNone, Integer.parseInt((String)s2)});
                            continue;
                        }
                        keyBinding.setKeyCode(Integer.parseInt((String)s2));
                    }
                    for (KeyBinding keyBinding : SoundCategory.values()) {
                        if (!s1.equals((Object)("soundCategory_" + keyBinding.getName()))) continue;
                        this.soundLevels.put((Object)keyBinding, (Object)Float.valueOf((float)this.parseFloat(s2)));
                    }
                    for (KeyBinding keyBinding : EnumPlayerModelParts.values()) {
                        if (!s1.equals((Object)("modelPart_" + keyBinding.getPartName()))) continue;
                        this.setModelPartEnabled((EnumPlayerModelParts)keyBinding, "true".equals((Object)s2));
                    }
                }
                catch (Exception var11) {
                    LOGGER.warn("Skipping bad option: {}:{}", (Object)s1, (Object)s2);
                    var11.printStackTrace();
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            IOUtils.closeQuietly((InputStream)is);
        }
        catch (Exception exception) {
            LOGGER.error("Failed to load options", (Throwable)exception);
        }
        finally {
            IOUtils.closeQuietly(is);
        }
        this.loadOfOptions();
    }

    private NBTTagCompound dataFix(NBTTagCompound p_189988_1_) {
        int i = 0;
        try {
            i = Integer.parseInt((String)p_189988_1_.getString("version"));
        }
        catch (RuntimeException runtimeException) {
            // empty catch block
        }
        return this.mc.getDataFixer().process((IFixType)FixTypes.OPTIONS, p_189988_1_, i);
    }

    private float parseFloat(String str) {
        if ("true".equals((Object)str)) {
            return 1.0f;
        }
        return "false".equals((Object)str) ? 0.0f : Float.parseFloat((String)str);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void saveOptions() {
        Object fml;
        if (Reflector.FMLClientHandler.exists() && (fml = Reflector.call((ReflectorMethod)Reflector.FMLClientHandler_instance, (Object[])new Object[0])) != null && Reflector.callBoolean((Object)fml, (ReflectorMethod)Reflector.FMLClientHandler_isLoading, (Object[])new Object[0])) {
            return;
        }
        PrintWriter printwriter = null;
        try {
            printwriter = new PrintWriter((Writer)new OutputStreamWriter((OutputStream)new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
            printwriter.println("version:1343");
            printwriter.println("invertYMouse:" + this.invertMouse);
            printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printwriter.println("fov:" + (this.fovSetting - 70.0f) / 40.0f);
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
                case 0: {
                    printwriter.println("renderClouds:false");
                    break;
                }
                case 1: {
                    printwriter.println("renderClouds:fast");
                    break;
                }
                case 2: {
                    printwriter.println("renderClouds:true");
                }
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
            for (KeyBinding keyBinding : this.keyBindings) {
                if (Reflector.ForgeKeyBinding_getKeyModifier.exists()) {
                    Object keyModifierNone;
                    String keyString = "key_" + keyBinding.getKeyDescription() + ":" + keyBinding.getKeyCode();
                    Object keyModifier = Reflector.call((Object)keyBinding, (ReflectorMethod)Reflector.ForgeKeyBinding_getKeyModifier, (Object[])new Object[0]);
                    printwriter.println(keyModifier != (keyModifierNone = Reflector.getFieldValue((ReflectorField)Reflector.KeyModifier_NONE)) ? keyString + ":" + keyModifier : keyString);
                    continue;
                }
                printwriter.println("key_" + keyBinding.getKeyDescription() + ":" + keyBinding.getKeyCode());
            }
            for (KeyBinding keyBinding : SoundCategory.values()) {
                printwriter.println("soundCategory_" + keyBinding.getName() + ":" + this.getSoundLevel((SoundCategory)keyBinding));
            }
            for (KeyBinding keyBinding : EnumPlayerModelParts.values()) {
                printwriter.println("modelPart_" + keyBinding.getPartName() + ":" + this.setModelParts.contains((Object)keyBinding));
            }
            IOUtils.closeQuietly((Writer)printwriter);
        }
        catch (Exception exception) {
            LOGGER.error("Failed to save options", (Throwable)exception);
        }
        finally {
            IOUtils.closeQuietly(printwriter);
        }
        this.saveOfOptions();
        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory category) {
        return this.soundLevels.containsKey((Object)category) ? ((Float)this.soundLevels.get((Object)category)).floatValue() : 1.0f;
    }

    public void setSoundLevel(SoundCategory category, float volume) {
        this.mc.getSoundHandler().setSoundLevel(category, volume);
        this.soundLevels.put((Object)category, (Object)Float.valueOf((float)volume));
    }

    public void sendSettingsToServer() {
        if (this.mc.player != null) {
            int i = 0;
            for (EnumPlayerModelParts enumplayermodelparts : this.setModelParts) {
                i |= enumplayermodelparts.getPartMask();
            }
            this.mc.player.connection.sendPacket((Packet)new CPacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i, this.mainHand));
        }
    }

    public Set<EnumPlayerModelParts> getModelParts() {
        return ImmutableSet.copyOf(this.setModelParts);
    }

    public void setModelPartEnabled(EnumPlayerModelParts modelPart, boolean enable) {
        if (enable) {
            this.setModelParts.add((Object)modelPart);
        } else {
            this.setModelParts.remove((Object)modelPart);
        }
        this.sendSettingsToServer();
    }

    public void switchModelPartEnabled(EnumPlayerModelParts modelPart) {
        if (this.getModelParts().contains((Object)modelPart)) {
            this.setModelParts.remove((Object)modelPart);
        } else {
            this.setModelParts.add((Object)modelPart);
        }
        this.sendSettingsToServer();
    }

    public int shouldRenderClouds() {
        return this.renderDistanceChunks >= 4 ? this.clouds : 0;
    }

    public boolean isUsingNativeTransport() {
        return this.useNativeTransport;
    }

    private void setOptionFloatValueOF(Options option, float val) {
        int valInt;
        if (option == Options.CLOUD_HEIGHT) {
            this.ofCloudsHeight = val;
            this.mc.renderGlobal.resetClouds();
        }
        if (option == Options.AO_LEVEL) {
            this.ofAoLevel = val;
            this.mc.renderGlobal.loadRenderers();
        }
        if (option == Options.AA_LEVEL) {
            valInt = (int)val;
            if (valInt > 0 && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.aa.shaders1"), (String)Lang.get((String)"of.message.aa.shaders2"));
                return;
            }
            int[] aaLevels = new int[]{0, 2, 4, 6, 8, 12, 16};
            this.ofAaLevel = 0;
            for (int l = 0; l < aaLevels.length; ++l) {
                if (valInt < aaLevels[l]) continue;
                this.ofAaLevel = aaLevels[l];
            }
            this.ofAaLevel = Config.limit((int)this.ofAaLevel, (int)0, (int)16);
        }
        if (option == Options.AF_LEVEL) {
            valInt = (int)val;
            if (valInt > 1 && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.af.shaders1"), (String)Lang.get((String)"of.message.af.shaders2"));
                return;
            }
            this.ofAfLevel = 1;
            while (this.ofAfLevel * 2 <= valInt) {
                this.ofAfLevel *= 2;
            }
            this.ofAfLevel = Config.limit((int)this.ofAfLevel, (int)1, (int)16);
            this.mc.refreshResources();
        }
        if (option == Options.MIPMAP_TYPE) {
            valInt = (int)val;
            this.ofMipmapType = Config.limit((int)valInt, (int)0, (int)3);
            this.mc.refreshResources();
        }
        if (option == Options.FULLSCREEN_MODE) {
            int index = (int)val - 1;
            String[] modeNames = Config.getDisplayModeNames();
            if (index < 0 || index >= modeNames.length) {
                this.ofFullscreenMode = "Default";
                return;
            }
            this.ofFullscreenMode = modeNames[index];
        }
    }

    private float getOptionFloatValueOF(Options settingOption) {
        if (settingOption == Options.CLOUD_HEIGHT) {
            return this.ofCloudsHeight;
        }
        if (settingOption == Options.AO_LEVEL) {
            return this.ofAoLevel;
        }
        if (settingOption == Options.AA_LEVEL) {
            return this.ofAaLevel;
        }
        if (settingOption == Options.AF_LEVEL) {
            return this.ofAfLevel;
        }
        if (settingOption == Options.MIPMAP_TYPE) {
            return this.ofMipmapType;
        }
        if (settingOption == Options.FRAMERATE_LIMIT) {
            if ((float)this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync) {
                return 0.0f;
            }
            return this.limitFramerate;
        }
        if (settingOption == Options.FULLSCREEN_MODE) {
            if (this.ofFullscreenMode.equals((Object)"Default")) {
                return 0.0f;
            }
            List modeList = Arrays.asList((Object[])Config.getDisplayModeNames());
            int index = modeList.indexOf((Object)this.ofFullscreenMode);
            if (index < 0) {
                return 0.0f;
            }
            return index + 1;
        }
        return Float.MAX_VALUE;
    }

    private void setOptionValueOF(Options par1EnumOptions, int par2) {
        if (par1EnumOptions == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    this.ofFogType = 2;
                    if (Config.isFancyFogAvailable()) break;
                    this.ofFogType = 3;
                    break;
                }
                case 2: {
                    this.ofFogType = 3;
                    break;
                }
                case 3: {
                    this.ofFogType = 1;
                    break;
                }
                default: {
                    this.ofFogType = 1;
                }
            }
        }
        if (par1EnumOptions == Options.FOG_START) {
            this.ofFogStart += 0.2f;
            if (this.ofFogStart > 0.81f) {
                this.ofFogStart = 0.2f;
            }
        }
        if (par1EnumOptions == Options.SMOOTH_FPS) {
            boolean bl = this.ofSmoothFps = !this.ofSmoothFps;
        }
        if (par1EnumOptions == Options.SMOOTH_WORLD) {
            this.ofSmoothWorld = !this.ofSmoothWorld;
            Config.updateThreadPriorities();
        }
        if (par1EnumOptions == Options.CLOUDS) {
            ++this.ofClouds;
            if (this.ofClouds > 3) {
                this.ofClouds = 0;
            }
            this.updateRenderClouds();
            this.mc.renderGlobal.resetClouds();
        }
        if (par1EnumOptions == Options.TREES) {
            this.ofTrees = GameSettings.nextValue(this.ofTrees, OF_TREES_VALUES);
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.DROPPED_ITEMS) {
            ++this.ofDroppedItems;
            if (this.ofDroppedItems > 2) {
                this.ofDroppedItems = 0;
            }
        }
        if (par1EnumOptions == Options.RAIN) {
            ++this.ofRain;
            if (this.ofRain > 3) {
                this.ofRain = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_WATER) {
            ++this.ofAnimatedWater;
            if (this.ofAnimatedWater == 1) {
                ++this.ofAnimatedWater;
            }
            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_LAVA) {
            ++this.ofAnimatedLava;
            if (this.ofAnimatedLava == 1) {
                ++this.ofAnimatedLava;
            }
            if (this.ofAnimatedLava > 2) {
                this.ofAnimatedLava = 0;
            }
        }
        if (par1EnumOptions == Options.ANIMATED_FIRE) {
            boolean bl = this.ofAnimatedFire = !this.ofAnimatedFire;
        }
        if (par1EnumOptions == Options.ANIMATED_PORTAL) {
            boolean bl = this.ofAnimatedPortal = !this.ofAnimatedPortal;
        }
        if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
            boolean bl = this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }
        if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
            boolean bl = this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }
        if (par1EnumOptions == Options.ANIMATED_FLAME) {
            boolean bl = this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }
        if (par1EnumOptions == Options.ANIMATED_SMOKE) {
            boolean bl = this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }
        if (par1EnumOptions == Options.VOID_PARTICLES) {
            boolean bl = this.ofVoidParticles = !this.ofVoidParticles;
        }
        if (par1EnumOptions == Options.WATER_PARTICLES) {
            boolean bl = this.ofWaterParticles = !this.ofWaterParticles;
        }
        if (par1EnumOptions == Options.PORTAL_PARTICLES) {
            boolean bl = this.ofPortalParticles = !this.ofPortalParticles;
        }
        if (par1EnumOptions == Options.POTION_PARTICLES) {
            boolean bl = this.ofPotionParticles = !this.ofPotionParticles;
        }
        if (par1EnumOptions == Options.FIREWORK_PARTICLES) {
            boolean bl = this.ofFireworkParticles = !this.ofFireworkParticles;
        }
        if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
            boolean bl = this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
        }
        if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
            boolean bl = this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
        }
        if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
            boolean bl = this.ofAnimatedTextures = !this.ofAnimatedTextures;
        }
        if (par1EnumOptions == Options.RAIN_SPLASH) {
            boolean bl = this.ofRainSplash = !this.ofRainSplash;
        }
        if (par1EnumOptions == Options.LAGOMETER) {
            boolean bl = this.ofLagometer = !this.ofLagometer;
        }
        if (par1EnumOptions == Options.SHOW_FPS) {
            boolean bl = this.ofShowFps = !this.ofShowFps;
        }
        if (par1EnumOptions == Options.AUTOSAVE_TICKS) {
            int step = 900;
            this.ofAutoSaveTicks = Math.max((int)(this.ofAutoSaveTicks / step * step), (int)step);
            this.ofAutoSaveTicks *= 2;
            if (this.ofAutoSaveTicks > 32 * step) {
                this.ofAutoSaveTicks = step;
            }
        }
        if (par1EnumOptions == Options.BETTER_GRASS) {
            ++this.ofBetterGrass;
            if (this.ofBetterGrass > 3) {
                this.ofBetterGrass = 1;
            }
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CONNECTED_TEXTURES) {
            ++this.ofConnectedTextures;
            if (this.ofConnectedTextures > 3) {
                this.ofConnectedTextures = 1;
            }
            if (this.ofConnectedTextures == 2) {
                this.mc.renderGlobal.loadRenderers();
            } else {
                this.mc.refreshResources();
            }
        }
        if (par1EnumOptions == Options.WEATHER) {
            boolean bl = this.ofWeather = !this.ofWeather;
        }
        if (par1EnumOptions == Options.SKY) {
            boolean bl = this.ofSky = !this.ofSky;
        }
        if (par1EnumOptions == Options.STARS) {
            boolean bl = this.ofStars = !this.ofStars;
        }
        if (par1EnumOptions == Options.SUN_MOON) {
            boolean bl = this.ofSunMoon = !this.ofSunMoon;
        }
        if (par1EnumOptions == Options.VIGNETTE) {
            ++this.ofVignette;
            if (this.ofVignette > 2) {
                this.ofVignette = 0;
            }
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES) {
            ++this.ofChunkUpdates;
            if (this.ofChunkUpdates > 5) {
                this.ofChunkUpdates = 1;
            }
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
            boolean bl = this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }
        if (par1EnumOptions == Options.TIME) {
            ++this.ofTime;
            if (this.ofTime > 2) {
                this.ofTime = 0;
            }
        }
        if (par1EnumOptions == Options.CLEAR_WATER) {
            this.ofClearWater = !this.ofClearWater;
            this.updateWaterOpacity();
        }
        if (par1EnumOptions == Options.PROFILER) {
            boolean bl = this.ofProfiler = !this.ofProfiler;
        }
        if (par1EnumOptions == Options.BETTER_SNOW) {
            this.ofBetterSnow = !this.ofBetterSnow;
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.SWAMP_COLORS) {
            this.ofSwampColors = !this.ofSwampColors;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.RANDOM_ENTITIES) {
            this.ofRandomEntities = !this.ofRandomEntities;
            RandomEntities.update();
        }
        if (par1EnumOptions == Options.SMOOTH_BIOMES) {
            this.ofSmoothBiomes = !this.ofSmoothBiomes;
            CustomColors.updateUseDefaultGrassFoliageColors();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CUSTOM_FONTS) {
            this.ofCustomFonts = !this.ofCustomFonts;
            this.mc.fontRenderer.onResourceManagerReload(Config.getResourceManager());
            this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
        }
        if (par1EnumOptions == Options.CUSTOM_COLORS) {
            this.ofCustomColors = !this.ofCustomColors;
            CustomColors.update();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.CUSTOM_ITEMS) {
            this.ofCustomItems = !this.ofCustomItems;
            this.mc.refreshResources();
        }
        if (par1EnumOptions == Options.CUSTOM_SKY) {
            this.ofCustomSky = !this.ofCustomSky;
            CustomSky.update();
        }
        if (par1EnumOptions == Options.SHOW_CAPES) {
            boolean bl = this.ofShowCapes = !this.ofShowCapes;
        }
        if (par1EnumOptions == Options.NATURAL_TEXTURES) {
            this.ofNaturalTextures = !this.ofNaturalTextures;
            NaturalTextures.update();
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.EMISSIVE_TEXTURES) {
            this.ofEmissiveTextures = !this.ofEmissiveTextures;
            this.mc.refreshResources();
        }
        if (par1EnumOptions == Options.FAST_MATH) {
            MathHelper.fastMath = this.ofFastMath = !this.ofFastMath;
        }
        if (par1EnumOptions == Options.FAST_RENDER) {
            if (!this.ofFastRender && Config.isShaders()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.fr.shaders1"), (String)Lang.get((String)"of.message.fr.shaders2"));
                return;
            }
            boolean bl = this.ofFastRender = !this.ofFastRender;
            if (this.ofFastRender) {
                this.mc.entityRenderer.stopUseShader();
            }
            Config.updateFramebufferSize();
        }
        if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS) {
            this.ofTranslucentBlocks = this.ofTranslucentBlocks == 0 ? 1 : (this.ofTranslucentBlocks == 1 ? 2 : (this.ofTranslucentBlocks == 2 ? 0 : 0));
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.LAZY_CHUNK_LOADING) {
            boolean bl = this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
        }
        if (par1EnumOptions == Options.RENDER_REGIONS) {
            this.ofRenderRegions = !this.ofRenderRegions;
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.SMART_ANIMATIONS) {
            this.ofSmartAnimations = !this.ofSmartAnimations;
            this.mc.renderGlobal.loadRenderers();
        }
        if (par1EnumOptions == Options.DYNAMIC_FOV) {
            boolean bl = this.ofDynamicFov = !this.ofDynamicFov;
        }
        if (par1EnumOptions == Options.ALTERNATE_BLOCKS) {
            this.ofAlternateBlocks = !this.ofAlternateBlocks;
            this.mc.refreshResources();
        }
        if (par1EnumOptions == Options.DYNAMIC_LIGHTS) {
            this.ofDynamicLights = GameSettings.nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
            DynamicLights.removeLights((RenderGlobal)this.mc.renderGlobal);
        }
        if (par1EnumOptions == Options.SCREENSHOT_SIZE) {
            ++this.ofScreenshotSize;
            if (this.ofScreenshotSize > 4) {
                this.ofScreenshotSize = 1;
            }
            if (!OpenGlHelper.isFramebufferEnabled()) {
                this.ofScreenshotSize = 1;
            }
        }
        if (par1EnumOptions == Options.CUSTOM_ENTITY_MODELS) {
            this.ofCustomEntityModels = !this.ofCustomEntityModels;
            this.mc.refreshResources();
        }
        if (par1EnumOptions == Options.CUSTOM_GUIS) {
            this.ofCustomGuis = !this.ofCustomGuis;
            CustomGuis.update();
        }
        if (par1EnumOptions == Options.SHOW_GL_ERRORS) {
            boolean bl = this.ofShowGlErrors = !this.ofShowGlErrors;
        }
        if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
            boolean bl = this.heldItemTooltips = !this.heldItemTooltips;
        }
        if (par1EnumOptions == Options.ADVANCED_TOOLTIPS) {
            this.advancedItemTooltips = !this.advancedItemTooltips;
        }
    }

    private String getKeyBindingOF(Options par1EnumOptions) {
        String var2 = I18n.format((String)par1EnumOptions.getTranslation(), (Object[])new Object[0]) + ": ";
        if (var2 == null) {
            var2 = par1EnumOptions.getTranslation();
        }
        String s = var2;
        if (par1EnumOptions == Options.RENDER_DISTANCE) {
            int distChunks = (int)this.getOptionFloatValue(par1EnumOptions);
            String str = I18n.format((String)"of.options.renderDistance.tiny", (Object[])new Object[0]);
            int baseDist = 2;
            if (distChunks >= 4) {
                str = I18n.format((String)"of.options.renderDistance.short", (Object[])new Object[0]);
                baseDist = 4;
            }
            if (distChunks >= 8) {
                str = I18n.format((String)"of.options.renderDistance.normal", (Object[])new Object[0]);
                baseDist = 8;
            }
            if (distChunks >= 16) {
                str = I18n.format((String)"of.options.renderDistance.far", (Object[])new Object[0]);
                baseDist = 16;
            }
            if (distChunks >= 32) {
                str = Lang.get((String)"of.options.renderDistance.extreme");
                baseDist = 32;
            }
            if (distChunks >= 48) {
                str = Lang.get((String)"of.options.renderDistance.insane");
                baseDist = 48;
            }
            if (distChunks >= 64) {
                str = Lang.get((String)"of.options.renderDistance.ludicrous");
                baseDist = 64;
            }
            int diff = this.renderDistanceChunks - baseDist;
            String descr = str;
            if (diff > 0) {
                descr = descr + "+";
            }
            return var2 + distChunks + " " + descr + "";
        }
        if (par1EnumOptions == Options.FOG_FANCY) {
            switch (this.ofFogType) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
                case 3: {
                    return s + Lang.getOff();
                }
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.FOG_START) {
            return s + this.ofFogStart;
        }
        if (par1EnumOptions == Options.MIPMAP_TYPE) {
            switch (this.ofMipmapType) {
                case 0: {
                    return s + Lang.get((String)"of.options.mipmap.nearest");
                }
                case 1: {
                    return s + Lang.get((String)"of.options.mipmap.linear");
                }
                case 2: {
                    return s + Lang.get((String)"of.options.mipmap.bilinear");
                }
                case 3: {
                    return s + Lang.get((String)"of.options.mipmap.trilinear");
                }
            }
            return s + "of.options.mipmap.nearest";
        }
        if (par1EnumOptions == Options.SMOOTH_FPS) {
            if (this.ofSmoothFps) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SMOOTH_WORLD) {
            if (this.ofSmoothWorld) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.CLOUDS) {
            switch (this.ofClouds) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
                case 3: {
                    return s + Lang.getOff();
                }
            }
            return s + Lang.getDefault();
        }
        if (par1EnumOptions == Options.TREES) {
            switch (this.ofTrees) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
                case 4: {
                    return s + Lang.get((String)"of.general.smart");
                }
            }
            return s + Lang.getDefault();
        }
        if (par1EnumOptions == Options.DROPPED_ITEMS) {
            switch (this.ofDroppedItems) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getDefault();
        }
        if (par1EnumOptions == Options.RAIN) {
            switch (this.ofRain) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
                case 3: {
                    return s + Lang.getOff();
                }
            }
            return s + Lang.getDefault();
        }
        if (par1EnumOptions == Options.ANIMATED_WATER) {
            switch (this.ofAnimatedWater) {
                case 1: {
                    return s + Lang.get((String)"of.options.animation.dynamic");
                }
                case 2: {
                    return s + Lang.getOff();
                }
            }
            return s + Lang.getOn();
        }
        if (par1EnumOptions == Options.ANIMATED_LAVA) {
            switch (this.ofAnimatedLava) {
                case 1: {
                    return s + Lang.get((String)"of.options.animation.dynamic");
                }
                case 2: {
                    return s + Lang.getOff();
                }
            }
            return s + Lang.getOn();
        }
        if (par1EnumOptions == Options.ANIMATED_FIRE) {
            if (this.ofAnimatedFire) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ANIMATED_PORTAL) {
            if (this.ofAnimatedPortal) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ANIMATED_REDSTONE) {
            if (this.ofAnimatedRedstone) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ANIMATED_EXPLOSION) {
            if (this.ofAnimatedExplosion) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ANIMATED_FLAME) {
            if (this.ofAnimatedFlame) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ANIMATED_SMOKE) {
            if (this.ofAnimatedSmoke) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.VOID_PARTICLES) {
            if (this.ofVoidParticles) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.WATER_PARTICLES) {
            if (this.ofWaterParticles) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.PORTAL_PARTICLES) {
            if (this.ofPortalParticles) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.POTION_PARTICLES) {
            if (this.ofPotionParticles) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.FIREWORK_PARTICLES) {
            if (this.ofFireworkParticles) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.DRIPPING_WATER_LAVA) {
            if (this.ofDrippingWaterLava) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ANIMATED_TERRAIN) {
            if (this.ofAnimatedTerrain) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ANIMATED_TEXTURES) {
            if (this.ofAnimatedTextures) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.RAIN_SPLASH) {
            if (this.ofRainSplash) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.LAGOMETER) {
            if (this.ofLagometer) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SHOW_FPS) {
            if (this.ofShowFps) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.AUTOSAVE_TICKS) {
            int step = 900;
            if (this.ofAutoSaveTicks <= step) {
                return s + Lang.get((String)"of.options.save.45s");
            }
            if (this.ofAutoSaveTicks <= 2 * step) {
                return s + Lang.get((String)"of.options.save.90s");
            }
            if (this.ofAutoSaveTicks <= 4 * step) {
                return s + Lang.get((String)"of.options.save.3min");
            }
            if (this.ofAutoSaveTicks <= 8 * step) {
                return s + Lang.get((String)"of.options.save.6min");
            }
            if (this.ofAutoSaveTicks <= 16 * step) {
                return s + Lang.get((String)"of.options.save.12min");
            }
            return s + Lang.get((String)"of.options.save.24min");
        }
        if (par1EnumOptions == Options.BETTER_GRASS) {
            switch (this.ofBetterGrass) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.CONNECTED_TEXTURES) {
            switch (this.ofConnectedTextures) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.WEATHER) {
            if (this.ofWeather) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SKY) {
            if (this.ofSky) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.STARS) {
            if (this.ofStars) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SUN_MOON) {
            if (this.ofSunMoon) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.VIGNETTE) {
            switch (this.ofVignette) {
                case 1: {
                    return s + Lang.getFast();
                }
                case 2: {
                    return s + Lang.getFancy();
                }
            }
            return s + Lang.getDefault();
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES) {
            return s + this.ofChunkUpdates;
        }
        if (par1EnumOptions == Options.CHUNK_UPDATES_DYNAMIC) {
            if (this.ofChunkUpdatesDynamic) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.TIME) {
            if (this.ofTime == 1) {
                return s + Lang.get((String)"of.options.time.dayOnly");
            }
            if (this.ofTime == 2) {
                return s + Lang.get((String)"of.options.time.nightOnly");
            }
            return s + Lang.getDefault();
        }
        if (par1EnumOptions == Options.CLEAR_WATER) {
            if (this.ofClearWater) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.AA_LEVEL) {
            String suffix = "";
            if (this.ofAaLevel != Config.getAntialiasingLevel()) {
                suffix = " (" + Lang.get((String)"of.general.restart") + ")";
            }
            if (this.ofAaLevel == 0) {
                return s + Lang.getOff() + suffix;
            }
            return s + this.ofAaLevel + suffix;
        }
        if (par1EnumOptions == Options.AF_LEVEL) {
            if (this.ofAfLevel == 1) {
                return s + Lang.getOff();
            }
            return s + this.ofAfLevel;
        }
        if (par1EnumOptions == Options.PROFILER) {
            if (this.ofProfiler) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.BETTER_SNOW) {
            if (this.ofBetterSnow) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SWAMP_COLORS) {
            if (this.ofSwampColors) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.RANDOM_ENTITIES) {
            if (this.ofRandomEntities) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SMOOTH_BIOMES) {
            if (this.ofSmoothBiomes) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.CUSTOM_FONTS) {
            if (this.ofCustomFonts) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.CUSTOM_COLORS) {
            if (this.ofCustomColors) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.CUSTOM_SKY) {
            if (this.ofCustomSky) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SHOW_CAPES) {
            if (this.ofShowCapes) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.CUSTOM_ITEMS) {
            if (this.ofCustomItems) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.NATURAL_TEXTURES) {
            if (this.ofNaturalTextures) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.EMISSIVE_TEXTURES) {
            if (this.ofEmissiveTextures) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.FAST_MATH) {
            if (this.ofFastMath) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.FAST_RENDER) {
            if (this.ofFastRender) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.TRANSLUCENT_BLOCKS) {
            if (this.ofTranslucentBlocks == 1) {
                return s + Lang.getFast();
            }
            if (this.ofTranslucentBlocks == 2) {
                return s + Lang.getFancy();
            }
            return s + Lang.getDefault();
        }
        if (par1EnumOptions == Options.LAZY_CHUNK_LOADING) {
            if (this.ofLazyChunkLoading) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.RENDER_REGIONS) {
            if (this.ofRenderRegions) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SMART_ANIMATIONS) {
            if (this.ofSmartAnimations) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.DYNAMIC_FOV) {
            if (this.ofDynamicFov) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ALTERNATE_BLOCKS) {
            if (this.ofAlternateBlocks) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.DYNAMIC_LIGHTS) {
            int index = GameSettings.indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
            return s + GameSettings.getTranslation(KEYS_DYNAMIC_LIGHTS, index);
        }
        if (par1EnumOptions == Options.SCREENSHOT_SIZE) {
            if (this.ofScreenshotSize <= 1) {
                return s + Lang.getDefault();
            }
            return s + this.ofScreenshotSize + "x";
        }
        if (par1EnumOptions == Options.CUSTOM_ENTITY_MODELS) {
            if (this.ofCustomEntityModels) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.CUSTOM_GUIS) {
            if (this.ofCustomGuis) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.SHOW_GL_ERRORS) {
            if (this.ofShowGlErrors) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.FULLSCREEN_MODE) {
            if (this.ofFullscreenMode.equals((Object)"Default")) {
                return s + Lang.getDefault();
            }
            return s + this.ofFullscreenMode;
        }
        if (par1EnumOptions == Options.HELD_ITEM_TOOLTIPS) {
            if (this.heldItemTooltips) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.ADVANCED_TOOLTIPS) {
            if (this.advancedItemTooltips) {
                return s + Lang.getOn();
            }
            return s + Lang.getOff();
        }
        if (par1EnumOptions == Options.FRAMERATE_LIMIT) {
            float var6 = this.getOptionFloatValue(par1EnumOptions);
            if (var6 == 0.0f) {
                return var2 + Lang.get((String)"of.options.framerateLimit.vsync");
            }
            if (var6 == Options.access$000((Options)par1EnumOptions)) {
                return var2 + I18n.format((String)"options.framerateLimit.max", (Object[])new Object[0]);
            }
            return var2 + (int)var6 + " fps";
        }
        return null;
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
            BufferedReader bufferedreader = new BufferedReader((Reader)new InputStreamReader((InputStream)new FileInputStream(ofReadFile), StandardCharsets.UTF_8));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                try {
                    String[] as = s.split(":");
                    if (as[0].equals((Object)"ofRenderDistanceChunks") && as.length >= 2) {
                        this.renderDistanceChunks = Integer.valueOf((String)as[1]);
                        this.renderDistanceChunks = Config.limit((int)this.renderDistanceChunks, (int)2, (int)1024);
                    }
                    if (as[0].equals((Object)"ofFogType") && as.length >= 2) {
                        this.ofFogType = Integer.valueOf((String)as[1]);
                        this.ofFogType = Config.limit((int)this.ofFogType, (int)1, (int)3);
                    }
                    if (as[0].equals((Object)"ofFogStart") && as.length >= 2) {
                        this.ofFogStart = Float.valueOf((String)as[1]).floatValue();
                        if (this.ofFogStart < 0.2f) {
                            this.ofFogStart = 0.2f;
                        }
                        if (this.ofFogStart > 0.81f) {
                            this.ofFogStart = 0.8f;
                        }
                    }
                    if (as[0].equals((Object)"ofMipmapType") && as.length >= 2) {
                        this.ofMipmapType = Integer.valueOf((String)as[1]);
                        this.ofMipmapType = Config.limit((int)this.ofMipmapType, (int)0, (int)3);
                    }
                    if (as[0].equals((Object)"ofOcclusionFancy") && as.length >= 2) {
                        this.ofOcclusionFancy = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofSmoothFps") && as.length >= 2) {
                        this.ofSmoothFps = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofSmoothWorld") && as.length >= 2) {
                        this.ofSmoothWorld = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAoLevel") && as.length >= 2) {
                        this.ofAoLevel = Float.valueOf((String)as[1]).floatValue();
                        this.ofAoLevel = Config.limit((float)this.ofAoLevel, (float)0.0f, (float)1.0f);
                    }
                    if (as[0].equals((Object)"ofClouds") && as.length >= 2) {
                        this.ofClouds = Integer.valueOf((String)as[1]);
                        this.ofClouds = Config.limit((int)this.ofClouds, (int)0, (int)3);
                        this.updateRenderClouds();
                    }
                    if (as[0].equals((Object)"ofCloudsHeight") && as.length >= 2) {
                        this.ofCloudsHeight = Float.valueOf((String)as[1]).floatValue();
                        this.ofCloudsHeight = Config.limit((float)this.ofCloudsHeight, (float)0.0f, (float)1.0f);
                    }
                    if (as[0].equals((Object)"ofTrees") && as.length >= 2) {
                        this.ofTrees = Integer.valueOf((String)as[1]);
                        this.ofTrees = GameSettings.limit(this.ofTrees, OF_TREES_VALUES);
                    }
                    if (as[0].equals((Object)"ofDroppedItems") && as.length >= 2) {
                        this.ofDroppedItems = Integer.valueOf((String)as[1]);
                        this.ofDroppedItems = Config.limit((int)this.ofDroppedItems, (int)0, (int)2);
                    }
                    if (as[0].equals((Object)"ofRain") && as.length >= 2) {
                        this.ofRain = Integer.valueOf((String)as[1]);
                        this.ofRain = Config.limit((int)this.ofRain, (int)0, (int)3);
                    }
                    if (as[0].equals((Object)"ofAnimatedWater") && as.length >= 2) {
                        this.ofAnimatedWater = Integer.valueOf((String)as[1]);
                        this.ofAnimatedWater = Config.limit((int)this.ofAnimatedWater, (int)0, (int)2);
                    }
                    if (as[0].equals((Object)"ofAnimatedLava") && as.length >= 2) {
                        this.ofAnimatedLava = Integer.valueOf((String)as[1]);
                        this.ofAnimatedLava = Config.limit((int)this.ofAnimatedLava, (int)0, (int)2);
                    }
                    if (as[0].equals((Object)"ofAnimatedFire") && as.length >= 2) {
                        this.ofAnimatedFire = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAnimatedPortal") && as.length >= 2) {
                        this.ofAnimatedPortal = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAnimatedRedstone") && as.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAnimatedExplosion") && as.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAnimatedFlame") && as.length >= 2) {
                        this.ofAnimatedFlame = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAnimatedSmoke") && as.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofVoidParticles") && as.length >= 2) {
                        this.ofVoidParticles = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofWaterParticles") && as.length >= 2) {
                        this.ofWaterParticles = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofPortalParticles") && as.length >= 2) {
                        this.ofPortalParticles = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofPotionParticles") && as.length >= 2) {
                        this.ofPotionParticles = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofFireworkParticles") && as.length >= 2) {
                        this.ofFireworkParticles = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofDrippingWaterLava") && as.length >= 2) {
                        this.ofDrippingWaterLava = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAnimatedTerrain") && as.length >= 2) {
                        this.ofAnimatedTerrain = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAnimatedTextures") && as.length >= 2) {
                        this.ofAnimatedTextures = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofRainSplash") && as.length >= 2) {
                        this.ofRainSplash = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofLagometer") && as.length >= 2) {
                        this.ofLagometer = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofShowFps") && as.length >= 2) {
                        this.ofShowFps = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAutoSaveTicks") && as.length >= 2) {
                        this.ofAutoSaveTicks = Integer.valueOf((String)as[1]);
                        this.ofAutoSaveTicks = Config.limit((int)this.ofAutoSaveTicks, (int)40, (int)40000);
                    }
                    if (as[0].equals((Object)"ofBetterGrass") && as.length >= 2) {
                        this.ofBetterGrass = Integer.valueOf((String)as[1]);
                        this.ofBetterGrass = Config.limit((int)this.ofBetterGrass, (int)1, (int)3);
                    }
                    if (as[0].equals((Object)"ofConnectedTextures") && as.length >= 2) {
                        this.ofConnectedTextures = Integer.valueOf((String)as[1]);
                        this.ofConnectedTextures = Config.limit((int)this.ofConnectedTextures, (int)1, (int)3);
                    }
                    if (as[0].equals((Object)"ofWeather") && as.length >= 2) {
                        this.ofWeather = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofSky") && as.length >= 2) {
                        this.ofSky = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofStars") && as.length >= 2) {
                        this.ofStars = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofSunMoon") && as.length >= 2) {
                        this.ofSunMoon = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofVignette") && as.length >= 2) {
                        this.ofVignette = Integer.valueOf((String)as[1]);
                        this.ofVignette = Config.limit((int)this.ofVignette, (int)0, (int)2);
                    }
                    if (as[0].equals((Object)"ofChunkUpdates") && as.length >= 2) {
                        this.ofChunkUpdates = Integer.valueOf((String)as[1]);
                        this.ofChunkUpdates = Config.limit((int)this.ofChunkUpdates, (int)1, (int)5);
                    }
                    if (as[0].equals((Object)"ofChunkUpdatesDynamic") && as.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofTime") && as.length >= 2) {
                        this.ofTime = Integer.valueOf((String)as[1]);
                        this.ofTime = Config.limit((int)this.ofTime, (int)0, (int)2);
                    }
                    if (as[0].equals((Object)"ofClearWater") && as.length >= 2) {
                        this.ofClearWater = Boolean.valueOf((String)as[1]);
                        this.updateWaterOpacity();
                    }
                    if (as[0].equals((Object)"ofAaLevel") && as.length >= 2) {
                        this.ofAaLevel = Integer.valueOf((String)as[1]);
                        this.ofAaLevel = Config.limit((int)this.ofAaLevel, (int)0, (int)16);
                    }
                    if (as[0].equals((Object)"ofAfLevel") && as.length >= 2) {
                        this.ofAfLevel = Integer.valueOf((String)as[1]);
                        this.ofAfLevel = Config.limit((int)this.ofAfLevel, (int)1, (int)16);
                    }
                    if (as[0].equals((Object)"ofProfiler") && as.length >= 2) {
                        this.ofProfiler = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofBetterSnow") && as.length >= 2) {
                        this.ofBetterSnow = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofSwampColors") && as.length >= 2) {
                        this.ofSwampColors = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofRandomEntities") && as.length >= 2) {
                        this.ofRandomEntities = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofSmoothBiomes") && as.length >= 2) {
                        this.ofSmoothBiomes = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofCustomFonts") && as.length >= 2) {
                        this.ofCustomFonts = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofCustomColors") && as.length >= 2) {
                        this.ofCustomColors = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofCustomItems") && as.length >= 2) {
                        this.ofCustomItems = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofCustomSky") && as.length >= 2) {
                        this.ofCustomSky = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofShowCapes") && as.length >= 2) {
                        this.ofShowCapes = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofNaturalTextures") && as.length >= 2) {
                        this.ofNaturalTextures = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofEmissiveTextures") && as.length >= 2) {
                        this.ofEmissiveTextures = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofLazyChunkLoading") && as.length >= 2) {
                        this.ofLazyChunkLoading = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofRenderRegions") && as.length >= 2) {
                        this.ofRenderRegions = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofSmartAnimations") && as.length >= 2) {
                        this.ofSmartAnimations = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofDynamicFov") && as.length >= 2) {
                        this.ofDynamicFov = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofAlternateBlocks") && as.length >= 2) {
                        this.ofAlternateBlocks = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofDynamicLights") && as.length >= 2) {
                        this.ofDynamicLights = Integer.valueOf((String)as[1]);
                        this.ofDynamicLights = GameSettings.limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
                    }
                    if (as[0].equals((Object)"ofScreenshotSize") && as.length >= 2) {
                        this.ofScreenshotSize = Integer.valueOf((String)as[1]);
                        this.ofScreenshotSize = Config.limit((int)this.ofScreenshotSize, (int)1, (int)4);
                    }
                    if (as[0].equals((Object)"ofCustomEntityModels") && as.length >= 2) {
                        this.ofCustomEntityModels = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofCustomGuis") && as.length >= 2) {
                        this.ofCustomGuis = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofShowGlErrors") && as.length >= 2) {
                        this.ofShowGlErrors = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofFullscreenMode") && as.length >= 2) {
                        this.ofFullscreenMode = as[1];
                    }
                    if (as[0].equals((Object)"ofFastMath") && as.length >= 2) {
                        MathHelper.fastMath = this.ofFastMath = Boolean.valueOf((String)as[1]).booleanValue();
                    }
                    if (as[0].equals((Object)"ofFastRender") && as.length >= 2) {
                        this.ofFastRender = Boolean.valueOf((String)as[1]);
                    }
                    if (as[0].equals((Object)"ofTranslucentBlocks") && as.length >= 2) {
                        this.ofTranslucentBlocks = Integer.valueOf((String)as[1]);
                        this.ofTranslucentBlocks = Config.limit((int)this.ofTranslucentBlocks, (int)0, (int)2);
                    }
                    if (!as[0].equals((Object)("key_" + this.ofKeyBindZoom.getKeyDescription()))) continue;
                    this.ofKeyBindZoom.setKeyCode(Integer.parseInt((String)as[1]));
                }
                catch (Exception exception1) {
                    Config.dbg((String)("Skipping bad option: " + s));
                    exception1.printStackTrace();
                }
            }
            KeyUtils.fixKeyConflicts((KeyBinding[])this.keyBindings, (KeyBinding[])new KeyBinding[]{this.ofKeyBindZoom});
            KeyBinding.resetKeyBindingArrayAndHash();
            bufferedreader.close();
        }
        catch (Exception exception) {
            Config.warn((String)"Failed to load options");
            exception.printStackTrace();
        }
    }

    public void saveOfOptions() {
        try {
            PrintWriter printwriter = new PrintWriter((Writer)new OutputStreamWriter((OutputStream)new FileOutputStream(this.optionsFileOF), StandardCharsets.UTF_8));
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
        }
        catch (Exception exception) {
            Config.warn((String)"Failed to save options");
            exception.printStackTrace();
        }
    }

    private void updateRenderClouds() {
        switch (this.ofClouds) {
            case 3: {
                this.clouds = 0;
                break;
            }
            case 1: {
                this.clouds = 1;
                break;
            }
            case 2: {
                this.clouds = 2;
                break;
            }
            default: {
                this.clouds = this.fancyGraphics ? 2 : 1;
            }
        }
    }

    public void resetSettings() {
        this.renderDistanceChunks = 8;
        this.viewBobbing = true;
        this.anaglyph = false;
        this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
        this.enableVsync = false;
        this.updateVSync();
        this.mipmapLevels = 4;
        this.fancyGraphics = true;
        this.ambientOcclusion = 2;
        this.clouds = 2;
        this.fovSetting = 70.0f;
        this.gammaSetting = 0.0f;
        this.guiScale = 0;
        this.particleSetting = 0;
        this.heldItemTooltips = true;
        this.useVbo = false;
        this.forceUnicodeFont = false;
        this.ofFogType = 1;
        this.ofFogStart = 0.8f;
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
        this.ofAoLevel = 1.0f;
        this.ofAaLevel = 0;
        this.ofAfLevel = 1;
        this.ofClouds = 0;
        this.ofCloudsHeight = 0.0f;
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
        Shaders.setShaderPack((String)"OFF");
        Shaders.configAntialiasingLevel = 0;
        Shaders.uninit();
        Shaders.storeConfig();
        this.updateWaterOpacity();
        this.mc.refreshResources();
        this.saveOptions();
    }

    public void updateVSync() {
        Display.setVSyncEnabled((boolean)this.enableVsync);
    }

    private void updateWaterOpacity() {
        if (Config.isIntegratedServerRunning()) {
            Config.waterOpacityChanged = true;
        }
        ClearWater.updateWaterOpacity((GameSettings)this, (World)this.mc.world);
    }

    public void setAllAnimations(boolean flag) {
        int animVal;
        this.ofAnimatedWater = animVal = flag ? 0 : 2;
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
        int index = GameSettings.indexOf(val, vals);
        if (index < 0) {
            return vals[0];
        }
        if (++index >= vals.length) {
            index = 0;
        }
        return vals[index];
    }

    private static int limit(int val, int[] vals) {
        int index = GameSettings.indexOf(val, vals);
        if (index < 0) {
            return vals[0];
        }
        return val;
    }

    private static int indexOf(int val, int[] vals) {
        for (int i = 0; i < vals.length; ++i) {
            if (vals[i] != val) continue;
            return i;
        }
        return -1;
    }

    private void setForgeKeybindProperties() {
        if (!Reflector.KeyConflictContext_IN_GAME.exists()) {
            return;
        }
        if (!Reflector.ForgeKeyBinding_setKeyConflictContext.exists()) {
            return;
        }
        Object inGame = Reflector.getFieldValue((ReflectorField)Reflector.KeyConflictContext_IN_GAME);
        Reflector.call((Object)this.keyBindForward, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindLeft, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindBack, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindRight, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindJump, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindSneak, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindSprint, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindAttack, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindChat, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindPlayerList, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindCommand, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindTogglePerspective, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindSmoothCamera, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
        Reflector.call((Object)this.keyBindSwapHands, (ReflectorMethod)Reflector.ForgeKeyBinding_setKeyConflictContext, (Object[])new Object[]{inGame});
    }

    public void onGuiClosed() {
        if (this.needsResourceRefresh) {
            this.mc.scheduleResourcesRefresh();
            this.needsResourceRefresh = false;
        }
    }
}
