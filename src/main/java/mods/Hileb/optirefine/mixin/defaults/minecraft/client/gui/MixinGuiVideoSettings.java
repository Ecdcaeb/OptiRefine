package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.cleanroommc.common.CleanroomVersion;
import mods.Hileb.optirefine.Reference;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ChangeSuperClass;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.optifine.Lang;
import net.optifine.gui.*;
import net.optifine.shaders.gui.GuiShaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
@Mixin(GuiVideoSettings.class)
// GuiScreenOF supplies the right-click option dispatch and a no-argument constructor.
@ChangeSuperClass(GuiScreenOF.class)
public abstract class MixinGuiVideoSettings extends GuiScreen {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] baseline member parentGuiScreen exists in target class
    @Shadow @Final private GuiScreen parentGuiScreen;
// [AUDIT-OK] baseline member screenTitle exists in target class
    @Shadow protected String screenTitle;
// [AUDIT-OK] baseline member guiGameSettings exists in target class
    @Shadow @Final private GameSettings guiGameSettings;
// [AUDIT-OK] baseline member optionsRowList exists in target class
    @Shadow private GuiListExtended optionsRowList;

// [AUDIT-OK] OF-added static field, not in baseline
    private static final GameSettings.Options[] optiRefine$videoOptions = new GameSettings.Options[]{
            GameSettings.Options.GRAPHICS,
            GameSettings.Options.RENDER_DISTANCE,
            GameSettings.Options.AMBIENT_OCCLUSION,
            GameSettings.Options.FRAMERATE_LIMIT,
            GameSettingsOptionOF.AO_LEVEL,              // OF
            GameSettings.Options.VIEW_BOBBING,
            GameSettings.Options.GUI_SCALE,
            GameSettings.Options.USE_VBO,
            GameSettings.Options.GAMMA,
            GameSettings.Options.ATTACK_INDICATOR,
            GameSettingsOptionOF.DYNAMIC_LIGHTS,        // OF
            GameSettingsOptionOF.DYNAMIC_FOV            // OF
    };

// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private TooltipManager optiRefine$tooltipManager;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void optiRefine$initTooltipManager(CallbackInfo ci) {
        this.optiRefine$tooltipManager = new TooltipManager(this, new TooltipProviderOptions());
    }

// [AUDIT-OK] target initGui() matches baseline; HEAD cancel replicates OF buildVideoSettingsButtons
    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    private void optiRefine$buildVideoOptionButtons(CallbackInfo ci) {
        this.screenTitle = I18n.format("options.videoTitle");
        this.buttonList.clear();

        // OF patch: build options as individual buttons/sliders (2 columns)
        for (int i = 0; i < optiRefine$videoOptions.length; i++) {
            GameSettings.Options opt = optiRefine$videoOptions[i];
            if (opt == null) continue;

            int x = this.width / 2 - 155 + (i % 2) * 160;
            int y = this.height / 6 + 21 * (i / 2) - 12;

            if (opt.isFloat()) {
                // OptiFine style slider
                this.buttonList.add(new GuiOptionSliderOF(opt.getOrdinal(), x, y, opt));
            } else {
                // OptiFine style option button
                this.buttonList.add(new GuiOptionButtonOF(opt.getOrdinal(), x, y, opt, this.guiGameSettings.getKeyBinding(opt)));
            }
        }

        // OF patch: extra sub-menus (layout matches your earlier code)
        int y = this.height / 6 + 21 * (optiRefine$videoOptions.length / 2) - 12;
        int x = this.width / 2 - 155;

        this.buttonList.add(new GuiOptionButton(231, x, y, Lang.get("of.options.shaders")));
        this.buttonList.add(new GuiOptionButton(202, x + 160, y, Lang.get("of.options.quality")));

        y += 21;
        this.buttonList.add(new GuiOptionButton(201, x, y, Lang.get("of.options.details")));
        this.buttonList.add(new GuiOptionButton(212, x + 160, y, Lang.get("of.options.performance")));

        y += 21;
        this.buttonList.add(new GuiOptionButton(211, x, y, Lang.get("of.options.animations")));
        this.buttonList.add(new GuiOptionButton(222, x + 160, y, Lang.get("of.options.other")));

        y += 21;
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done")));

        // Vanilla mouse handlers still touch optionsRowList; keep an empty list.
        this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, new GameSettings.Options[0]);

        ci.cancel(); // stop vanilla initGui
    }

// [AUDIT-OK] target actionPerformed(GuiButton) matches baseline; HEAD cancel replicates OF actionPerformed(button,1) dispatch
    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void optiRefine$handleVideoOptionClick(GuiButton button, CallbackInfo ci) {
        if (button == null || !button.enabled) {
            ci.cancel();
            return;
        }

        optiRefine$dispatchVideoOptionAction(button, 1);
        ci.cancel();
    }

// [AUDIT-OK] OF-added helper, not in baseline
    private void optiRefine$dispatchVideoOptionAction(GuiButton button, int optionStep) {
        int guiScaleBefore = this.guiGameSettings.guiScale;

        // Option buttons: ids < 200
        if (button.id < 200 && button instanceof GuiOptionButton) {
            this.guiGameSettings.setOptionValue(((GuiOptionButton) button).getOption(), optionStep);
            button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.byOrdinal(button.id));
        }

        // Done
        if (button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentGuiScreen);
            return;
        }

        // guiScale change triggers resize
        if (this.guiGameSettings.guiScale != guiScaleBefore) {
            ScaledResolution sr = new ScaledResolution(this.mc);
            this.setWorldAndResolution(this.mc, sr.getScaledWidth(), sr.getScaledHeight());
        }

        // OF sub-screens
        if (button.id == 201) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiDetailSettingsOF(this, this.guiGameSettings));
            return;
        }
        if (button.id == 202) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiQualitySettingsOF(this, this.guiGameSettings));
            return;
        }
        if (button.id == 211) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiAnimationSettingsOF(this, this.guiGameSettings));
            return;
        }
        if (button.id == 212) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiPerformanceSettingsOF(this, this.guiGameSettings));
            return;
        }
        if (button.id == 222) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiOtherSettingsOF(this, this.guiGameSettings));
            return;
        }

        // Shaders button: keep your AA/AF/FastRender/anaglyph guards
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
            this.mc.displayGuiScreen(new GuiShaders(this, this.guiGameSettings));
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    protected void actionPerformedRightClick(GuiButton button) {
        if (button != null && button.id == GameSettings.Options.GUI_SCALE.ordinal()) {
            optiRefine$dispatchVideoOptionAction(button, -1);
        }
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void optiRefine$drawVideoSettingsWithTooltips(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 15, 0xFFFFFF);

        final String ver = "OptiFine HD G5 Ultra + " + Reference.BRAND;
        this.drawString(this.fontRenderer, ver, 2, this.height - 10, 8421504);

        final String verMc = "Cleanroom " + CleanroomVersion.getVersion();
        int len = this.fontRenderer.getStringWidth(verMc);
        this.drawString(this.fontRenderer, verMc, this.width - len - 2, this.height - 10, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.optiRefine$tooltipManager.drawTooltips(mouseX, mouseY, this.buttonList);
        ci.cancel();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added API; private static + @Public matches OF getButtonWidth
    @Public
    private static int getButtonWidth(GuiButton button) { return button.width; }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added API; private static + @Public matches OF getButtonHeight
    @Public
    private static int getButtonHeight(GuiButton button) { return button.height; }

// [AUDIT-OK] vanilla SRG name func_73733_a = GuiScreen.drawGradientRect, deobf=true
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL,
            desc = "net.minecraft.client.gui.GuiScreen func_73733_a (IIIIII)V", deobf = true)
    private static void optiRefine$drawScreenGradient(GuiScreen guiScreen,
                                            int left, int top, int right, int bottom,
                                            int startColor, int endColor) {
        throw new AbstractMethodError();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added API; private static + @Public matches OF drawGradientRect(GuiScreen,...)
    @Public
    private static void drawGradientRect(GuiScreen guiScreen,
                                         int left, int top, int right, int bottom,
                                         int startColor, int endColor) {
        optiRefine$drawScreenGradient(guiScreen, left, top, right, bottom, startColor, endColor);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added API; private static + @Public matches OF getGuiChatText
    @Public
    private static String getGuiChatText(GuiChat guiChat) {
        return optiRefine$getChatInputField(guiChat).getText();
    }

    @SuppressWarnings({"unused", "MissingUnique"})
// [AUDIT-OK] vanilla SRG name field_146415_a = GuiChat.inputField (protected; same-package access OK), deobf=true
    @AccessibleOperation(opcode = Opcodes.GETFIELD,
            desc = "net.minecraft.client.gui.GuiChat field_146415_a Lnet.minecraft.client.gui.GuiTextField;", deobf = true)
    private static native GuiTextField optiRefine$getChatInputField(GuiChat guiChat);
}
