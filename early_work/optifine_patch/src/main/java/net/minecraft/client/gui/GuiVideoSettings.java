/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiOptionButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.optifine.Lang
 *  net.optifine.gui.GuiAnimationSettingsOF
 *  net.optifine.gui.GuiDetailSettingsOF
 *  net.optifine.gui.GuiOptionButtonOF
 *  net.optifine.gui.GuiOptionSliderOF
 *  net.optifine.gui.GuiOtherSettingsOF
 *  net.optifine.gui.GuiPerformanceSettingsOF
 *  net.optifine.gui.GuiQualitySettingsOF
 *  net.optifine.gui.GuiScreenOF
 *  net.optifine.gui.TooltipManager
 *  net.optifine.gui.TooltipProvider
 *  net.optifine.gui.TooltipProviderOptions
 *  net.optifine.shaders.gui.GuiShaders
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.optifine.Lang;
import net.optifine.gui.GuiAnimationSettingsOF;
import net.optifine.gui.GuiDetailSettingsOF;
import net.optifine.gui.GuiOptionButtonOF;
import net.optifine.gui.GuiOptionSliderOF;
import net.optifine.gui.GuiOtherSettingsOF;
import net.optifine.gui.GuiPerformanceSettingsOF;
import net.optifine.gui.GuiQualitySettingsOF;
import net.optifine.gui.GuiScreenOF;
import net.optifine.gui.TooltipManager;
import net.optifine.gui.TooltipProvider;
import net.optifine.gui.TooltipProviderOptions;
import net.optifine.shaders.gui.GuiShaders;

public class GuiVideoSettings
extends GuiScreenOF {
    private GuiScreen parentGuiScreen;
    protected String screenTitle = "Video Settings";
    private GameSettings guiGameSettings;
    private static GameSettings.Options[] videoOptions = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.ATTACK_INDICATOR, GameSettings.Options.DYNAMIC_LIGHTS, GameSettings.Options.DYNAMIC_FOV};
    private static final String __OBFID = "CL_00000718";
    private TooltipManager tooltipManager = new TooltipManager((GuiScreen)this, (TooltipProvider)new TooltipProviderOptions());

    public GuiVideoSettings(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
        this.parentGuiScreen = par1GuiScreen;
        this.guiGameSettings = par2GameSettings;
    }

    public void initGui() {
        this.screenTitle = I18n.format((String)"options.videoTitle", (Object[])new Object[0]);
        this.n.clear();
        for (int i = 0; i < videoOptions.length; ++i) {
            GameSettings.Options opt = videoOptions[i];
            if (opt == null) continue;
            int x = this.l / 2 - 155 + i % 2 * 160;
            int y = this.m / 6 + 21 * (i / 2) - 12;
            if (opt.isFloat()) {
                this.n.add((Object)new GuiOptionSliderOF(opt.getOrdinal(), x, y, opt));
                continue;
            }
            this.n.add((Object)new GuiOptionButtonOF(opt.getOrdinal(), x, y, opt, this.guiGameSettings.getKeyBinding(opt)));
        }
        int y = this.m / 6 + 21 * (videoOptions.length / 2) - 12;
        int x = 0;
        x = this.l / 2 - 155 + 0;
        this.n.add((Object)new GuiOptionButton(231, x, y, Lang.get((String)"of.options.shaders")));
        x = this.l / 2 - 155 + 160;
        this.n.add((Object)new GuiOptionButton(202, x, y, Lang.get((String)"of.options.quality")));
        x = this.l / 2 - 155 + 0;
        this.n.add((Object)new GuiOptionButton(201, x, y += 21, Lang.get((String)"of.options.details")));
        x = this.l / 2 - 155 + 160;
        this.n.add((Object)new GuiOptionButton(212, x, y, Lang.get((String)"of.options.performance")));
        x = this.l / 2 - 155 + 0;
        this.n.add((Object)new GuiOptionButton(211, x, y += 21, Lang.get((String)"of.options.animations")));
        x = this.l / 2 - 155 + 160;
        this.n.add((Object)new GuiOptionButton(222, x, y, Lang.get((String)"of.options.other")));
        y += 21;
        this.n.add((Object)new GuiButton(200, this.l / 2 - 100, this.m / 6 + 168 + 11, I18n.format((String)"gui.done", (Object[])new Object[0])));
    }

    protected void actionPerformed(GuiButton button) {
        this.actionPerformed(button, 1);
    }

    protected void actionPerformedRightClick(GuiButton button) {
        if (button.id == GameSettings.Options.GUI_SCALE.ordinal()) {
            this.actionPerformed(button, -1);
        }
    }

    private void actionPerformed(GuiButton button, int val) {
        GuiDetailSettingsOF scr;
        if (!button.enabled) {
            return;
        }
        int guiScale = this.guiGameSettings.guiScale;
        if (button.id < 200 && button instanceof GuiOptionButton) {
            this.guiGameSettings.setOptionValue(((GuiOptionButton)button).getOption(), val);
            button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.byOrdinal((int)button.id));
        }
        if (button.id == 200) {
            this.j.gameSettings.saveOptions();
            this.j.displayGuiScreen(this.parentGuiScreen);
        }
        if (this.guiGameSettings.guiScale != guiScale) {
            ScaledResolution var3 = new ScaledResolution(this.j);
            int var4 = var3.getScaledWidth();
            int var5 = var3.getScaledHeight();
            this.a(this.j, var4, var5);
        }
        if (button.id == 201) {
            this.j.gameSettings.saveOptions();
            scr = new GuiDetailSettingsOF((GuiScreen)this, this.guiGameSettings);
            this.j.displayGuiScreen((GuiScreen)scr);
        }
        if (button.id == 202) {
            this.j.gameSettings.saveOptions();
            scr = new GuiQualitySettingsOF((GuiScreen)this, this.guiGameSettings);
            this.j.displayGuiScreen((GuiScreen)scr);
        }
        if (button.id == 211) {
            this.j.gameSettings.saveOptions();
            scr = new GuiAnimationSettingsOF((GuiScreen)this, this.guiGameSettings);
            this.j.displayGuiScreen((GuiScreen)scr);
        }
        if (button.id == 212) {
            this.j.gameSettings.saveOptions();
            scr = new GuiPerformanceSettingsOF((GuiScreen)this, this.guiGameSettings);
            this.j.displayGuiScreen((GuiScreen)scr);
        }
        if (button.id == 222) {
            this.j.gameSettings.saveOptions();
            scr = new GuiOtherSettingsOF((GuiScreen)this, this.guiGameSettings);
            this.j.displayGuiScreen((GuiScreen)scr);
        }
        if (button.id == 231) {
            if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.shaders.aa1"), (String)Lang.get((String)"of.message.shaders.aa2"));
                return;
            }
            if (Config.isAnisotropicFiltering()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.shaders.af1"), (String)Lang.get((String)"of.message.shaders.af2"));
                return;
            }
            if (Config.isFastRender()) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.shaders.fr1"), (String)Lang.get((String)"of.message.shaders.fr2"));
                return;
            }
            if (Config.getGameSettings().anaglyph) {
                Config.showGuiMessage((String)Lang.get((String)"of.message.shaders.an1"), (String)Lang.get((String)"of.message.shaders.an2"));
                return;
            }
            this.j.gameSettings.saveOptions();
            scr = new GuiShaders((GuiScreen)this, this.guiGameSettings);
            this.j.displayGuiScreen((GuiScreen)scr);
        }
    }

    public void drawScreen(int x, int y, float z) {
        this.c();
        this.a(this.q, this.screenTitle, this.l / 2, 15, 0xFFFFFF);
        String ver = Config.getVersion();
        String ed = "HD_U";
        if (ed.equals((Object)"HD")) {
            ver = "OptiFine HD G5";
        }
        if (ed.equals((Object)"HD_U")) {
            ver = "OptiFine HD G5 Ultra";
        }
        if (ed.equals((Object)"L")) {
            ver = "OptiFine G5 Light";
        }
        this.c(this.q, ver, 2, this.m - 10, 0x808080);
        String verMc = "Minecraft 1.12.2";
        int lenMc = this.q.getStringWidth(verMc);
        this.c(this.q, verMc, this.l - lenMc - 2, this.m - 10, 0x808080);
        super.a(x, y, z);
        this.tooltipManager.drawTooltips(x, y, this.n);
    }

    public static int getButtonWidth(GuiButton btn) {
        return btn.width;
    }

    public static int getButtonHeight(GuiButton btn) {
        return btn.height;
    }

    public static void drawGradientRect(GuiScreen guiScreen, int left, int top, int right, int bottom, int startColor, int endColor) {
        guiScreen.a(left, top, right, bottom, startColor, endColor);
    }

    public static String getGuiChatText(GuiChat guiChat) {
        return guiChat.inputField.getText();
    }
}
