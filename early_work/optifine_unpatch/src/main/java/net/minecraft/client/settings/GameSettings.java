/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.gson.Gson
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.FileOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.io.OutputStreamWriter
 *  java.io.PrintWriter
 *  java.io.Writer
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Runtime
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Type
 *  java.nio.charset.Charset
 *  java.nio.charset.StandardCharsets
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.gui.chat.NarratorChatListener
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.resources.I18n
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
 *  net.minecraftforge.client.resource.IResourceType
 *  net.minecraftforge.client.resource.VanillaResourceType
 *  net.minecraftforge.client.settings.IKeyConflictContext
 *  net.minecraftforge.client.settings.KeyConflictContext
 *  net.minecraftforge.client.settings.KeyModifier
 *  net.minecraftforge.fml.client.FMLClientHandler
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
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
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

@SideOnly(value=Side.CLIENT)
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
    private boolean needsBlockModelRefresh = false;

    public GameSettings(Minecraft mcIn, File mcDataDir) {
        this.setForgeKeybindProperties();
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[]{this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindFullscreen, this.keyBindSpectatorOutlines, this.keyBindSwapHands, this.keyBindSaveToolbar, this.keyBindLoadToolbar, this.keyBindAdvancements}, (Object[])this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fovSetting = 70.0f;
        this.language = "en_us";
        this.mc = mcIn;
        this.optionsFile = new File(mcDataDir, "options.txt");
        if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
            Options.RENDER_DISTANCE.setValueMax(32.0f);
        } else {
            Options.RENDER_DISTANCE.setValueMax(16.0f);
        }
        this.renderDistanceChunks = mcIn.isJava64bit() ? 12 : 8;
        this.loadOptions();
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
                this.needsBlockModelRefresh = true;
            }
        }
        if (settingsOption == Options.RENDER_DISTANCE) {
            this.renderDistanceChunks = (int)value;
            this.mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    public void setOptionValue(Options settingsOption, int value) {
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
            this.guiScale = this.guiScale + value & 3;
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
            this.anaglyph = !this.anaglyph;
            FMLClientHandler.instance().refreshResources(new IResourceType[]{VanillaResourceType.TEXTURES});
        }
        if (settingsOption == Options.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
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
        switch (settingOption.ordinal()) {
            case 0: {
                return this.invertMouse;
            }
            case 6: {
                return this.viewBobbing;
            }
            case 7: {
                return this.anaglyph;
            }
            case 9: {
                return this.fboEnable;
            }
            case 16: {
                return this.chatColours;
            }
            case 17: {
                return this.chatLinks;
            }
            case 19: {
                return this.chatLinksPrompt;
            }
            case 20: {
                return this.snooperEnabled;
            }
            case 21: {
                return this.fullScreen;
            }
            case 22: {
                return this.enableVsync;
            }
            case 23: {
                return this.useVbo;
            }
            case 24: {
                return this.touchscreen;
            }
            case 30: {
                return this.forceUnicodeFont;
            }
            case 31: {
                return this.reducedDebugInfo;
            }
            case 32: {
                return this.entityShadows;
            }
            case 36: {
                return this.showSubtitles;
            }
            case 37: {
                return this.realmsNotifications;
            }
            case 35: {
                return this.enableWeakAttacks;
            }
            case 38: {
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
                return f1 == settingOption.valueMax ? s + I18n.format((String)"options.framerateLimit.max", (Object[])new Object[0]) : s + I18n.format((String)"options.framerate", (Object[])new Object[]{(int)f1});
            }
            if (settingOption == Options.RENDER_CLOUDS) {
                return f1 == settingOption.valueMin ? s + I18n.format((String)"options.cloudHeight.min", (Object[])new Object[0]) : s + ((int)f1 + 128);
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
                return f1 == 0.0f ? s + I18n.format((String)"options.off", (Object[])new Object[0]) : s + (int)f1;
            }
            return f == 0.0f ? s + I18n.format((String)"options.off", (Object[])new Object[0]) : s + (int)(f * 100.0f) + "%";
        }
        if (settingOption.isBoolean()) {
            boolean flag = this.getOptionOrdinalValue(settingOption);
            return flag ? s + I18n.format((String)"options.on", (Object[])new Object[0]) : s + I18n.format((String)"options.off", (Object[])new Object[0]);
        }
        if (settingOption == Options.MAIN_HAND) {
            return s + String.valueOf((Object)this.mainHand);
        }
        if (settingOption == Options.GUI_SCALE) {
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
        FileInputStream fileInputStream = null;
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            this.soundLevels.clear();
            fileInputStream = new FileInputStream(this.optionsFile);
            List list = IOUtils.readLines((InputStream)fileInputStream, (Charset)StandardCharsets.UTF_8);
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
                    }
                    if ("fboEnable".equals((Object)s1)) {
                        this.fboEnable = "true".equals((Object)s2);
                    }
                    if ("difficulty".equals((Object)s1)) {
                        this.difficulty = EnumDifficulty.byId((int)Integer.parseInt((String)s2));
                    }
                    if ("fancyGraphics".equals((Object)s1)) {
                        this.fancyGraphics = "true".equals((Object)s2);
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
                        if (s2.indexOf(58) != -1) {
                            String[] t = s2.split(":");
                            keyBinding.setKeyModifierAndCode(KeyModifier.valueFromString((String)t[1]), Integer.parseInt((String)t[0]));
                            continue;
                        }
                        keyBinding.setKeyModifierAndCode(KeyModifier.NONE, Integer.parseInt((String)s2));
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
                }
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            IOUtils.closeQuietly((InputStream)fileInputStream);
        }
        catch (Exception exception) {
            LOGGER.error("Failed to load options", (Throwable)exception);
        }
        finally {
            IOUtils.closeQuietly(fileInputStream);
        }
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
        if (FMLClientHandler.instance().isLoading()) {
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
                String keyString = "key_" + keyBinding.getKeyDescription() + ":" + keyBinding.getKeyCode();
                printwriter.println(keyBinding.getKeyModifier() != KeyModifier.NONE ? keyString + ":" + String.valueOf((Object)keyBinding.getKeyModifier()) : keyString);
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

    private void setForgeKeybindProperties() {
        KeyConflictContext inGame = KeyConflictContext.IN_GAME;
        this.keyBindForward.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindLeft.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindBack.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindRight.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindJump.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindSneak.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindSprint.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindAttack.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindChat.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindPlayerList.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindCommand.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindTogglePerspective.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindSmoothCamera.setKeyConflictContext((IKeyConflictContext)inGame);
        this.keyBindSwapHands.setKeyConflictContext((IKeyConflictContext)inGame);
    }

    public void onGuiClosed() {
        if (this.needsBlockModelRefresh) {
            FMLClientHandler.instance().scheduleResourcesRefresh(new IResourceType[]{VanillaResourceType.MODELS});
            this.needsBlockModelRefresh = false;
        }
    }
}
