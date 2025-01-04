package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.optifine.Lang;
import net.optifine.gui.*;
import net.optifine.shaders.gui.GuiShaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * {@link net.optifine.gui.GuiScreenOF}
 * **/
@Mixin(GuiVideoSettings.class)
public class MixinGuiVideoSettings extends GuiScreen {
    @Shadow
    @Final
    private GuiScreen parentGuiScreen;

    @Shadow
    protected String screenTitle;

    @Shadow
    @Final
    private GameSettings guiGameSettings;

    @Unique
    @SuppressWarnings("all")
    private static final String __OBFID = "CL_00000718";

    @Unique
    @SuppressWarnings("all")
    private static GameSettings.Options[] videoOptions = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettingsOptionOF.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.ATTACK_INDICATOR, GameSettingsOptionOF.DYNAMIC_LIGHTS, GameSettingsOptionOF.DYNAMIC_FOV};

    @Unique
    @SuppressWarnings("all")
    private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());

    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    public void overwriteInitGui(CallbackInfo ci) {
        this.screenTitle = I18n.format("options.videoTitle");
        this.buttonList.clear();

        for (int i = 0; i < videoOptions.length; i++) {
            GameSettings.Options opt = videoOptions[i];
            if (opt != null) {
                int x = this.width / 2 - 155 + i % 2 * 160;
                int y = this.height / 6 + 21 * (i / 2) - 12;
                if (opt.isFloat()) {
                    this.buttonList.add(new GuiOptionSliderOF(opt.getOrdinal(), x, y, opt));
                } else {
                    this.buttonList.add(new GuiOptionButtonOF(opt.getOrdinal(), x, y, opt, this.guiGameSettings.getKeyBinding(opt)));
                }
            }
        }

        int y = this.height / 6 + 21 * (videoOptions.length / 2) - 12;
        int x = 0;
        x = this.width / 2 - 155;
        this.buttonList.add(new GuiOptionButton(231, x, y, Lang.get("of.options.shaders")));
        x = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(202, x, y, Lang.get("of.options.quality")));
        y += 21;
        x = this.width / 2 - 155;
        this.buttonList.add(new GuiOptionButton(201, x, y, Lang.get("of.options.details")));
        x = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(212, x, y, Lang.get("of.options.performance")));
        y += 21;
        x = this.width / 2 - 155;
        this.buttonList.add(new GuiOptionButton(211, x, y, Lang.get("of.options.animations")));
        x = this.width / 2 - 155 + 160;
        this.buttonList.add(new GuiOptionButton(222, x, y, Lang.get("of.options.other")));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done")));
        ci.cancel();
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void injectActionPerformed(GuiButton button, CallbackInfo ci){
        if (button.enabled && button.id != 200) {
            actionPerformed(button, 1);
        }
    }

    @Unique
    @SuppressWarnings("all")
    public void actionPerformed(GuiButton button, int val) {
        if (button.enabled) {
            int guiScale = this.guiGameSettings.guiScale;
            if (button.id < 200 && button instanceof GuiOptionButton) {
                this.guiGameSettings.setOptionValue(((GuiOptionButton)button).getOption(), val);
                button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.byOrdinal(button.id));
            }

            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }

            if (this.guiGameSettings.guiScale != guiScale) {
                ScaledResolution var3 = new ScaledResolution(this.mc);
                int var4 = var3.getScaledWidth();
                int var5 = var3.getScaledHeight();
                this.setWorldAndResolution(this.mc, var4, var5);
            }

            if (button.id == 201) {
                this.mc.gameSettings.saveOptions();
                GuiDetailSettingsOF scr = new GuiDetailSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr);
            }

            if (button.id == 202) {
                this.mc.gameSettings.saveOptions();
                GuiQualitySettingsOF scr = new GuiQualitySettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr);
            }

            if (button.id == 211) {
                this.mc.gameSettings.saveOptions();
                GuiAnimationSettingsOF scr = new GuiAnimationSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr);
            }

            if (button.id == 212) {
                this.mc.gameSettings.saveOptions();
                GuiPerformanceSettingsOF scr = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr);
            }

            if (button.id == 222) {
                this.mc.gameSettings.saveOptions();
                GuiOtherSettingsOF scr = new GuiOtherSettingsOF(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr);
            }

            if (button.id == 231) {
                if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
                    return;
                }

                if (Config.isAnisotropicFiltering()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
                    return;
                }

                if (Config.isFastRender()) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.fr1"), Lang.get("of.message.shaders.fr2"));
                    return;
                }

                if (Config.getGameSettings().anaglyph) {
                    Config.showGuiMessage(Lang.get("of.message.shaders.an1"), Lang.get("of.message.shaders.an2"));
                    return;
                }

                this.mc.gameSettings.saveOptions();
                GuiShaders scr = new GuiShaders(this, this.guiGameSettings);
                this.mc.displayGuiScreen(scr);
            }
        }
    }

    @Unique
    @SuppressWarnings("all")
    protected void actionPerformedRightClick(GuiButton button) {
        if (button.id == GameSettings.Options.GUI_SCALE.ordinal()) {
            this.actionPerformed(button, -1);
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiListExtended;drawScreen(IIF)V"))
    public void injectDrawScreen(GuiListExtended instance, int x, int y, float v){
        this.a(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
        final String ver = "OptiFine HD G5 Ultra";
        this.drawCenteredString(this.fontRenderer, ver, 2, this.height - 10, 8421504);
        final String verMc = "Minecraft 1.12.2";
        int lenMc = this.fontRenderer.getStringWidth(verMc);
        this.drawCenteredString(this.fontRenderer, verMc, this.width - lenMc - 2, this.height - 10, 8421504);
    }

    @Unique
    @SuppressWarnings("all")
    public static int getButtonWidth(GuiButton btn) {
        return btn.width;
    }

    @Unique
    @SuppressWarnings("all")
    public static int getButtonHeight(GuiButton btn) {
        return btn.height;
    }

    @Unique
    @SuppressWarnings("all")
    public static void drawGradientRect(GuiScreen guiScreen, int left, int top, int right, int bottom, int startColor, int endColor) {
        ((GuiScreenAccessor)guiScreen).invokeDrawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Unique
    @SuppressWarnings("all")
    public static String getGuiChatText(GuiChat guiChat) {
        return ((GuiChatAccessor)guiChat).getInputField().getText();
    }

}
