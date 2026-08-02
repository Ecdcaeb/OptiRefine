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
@ChangeSuperClass(GuiScreenOF.class)
public abstract class MixinGuiVideoSettings extends GuiScreen {

    @Shadow @Final private GuiScreen parentGuiScreen;
    @Shadow protected String screenTitle;
    @Shadow @Final private GameSettings guiGameSettings;
    @Shadow @Final private GuiListExtended optionsRowList;

    @Unique
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

    @Unique
    private final TooltipManager optiRefine$tooltipManager =
            new TooltipManager(this, new TooltipProviderOptions());

    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    private void optiRefine$initGui(CallbackInfo ci) {
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

        // vanilla drawScreen/mouse handlers still touch optionsRowList; keep an empty list
        this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, new GameSettings.Options[0]);

        ci.cancel(); // stop vanilla initGui
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void optiRefine$actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button == null || !button.enabled) {
            ci.cancel();
            return;
        }

        optiRefine$actionPerformedImpl(button, 1);
        ci.cancel();
    }

    @Unique
    private void optiRefine$actionPerformedImpl(GuiButton button, int val) {
        int guiScaleBefore = this.guiGameSettings.guiScale;

        // Option buttons: ids < 200
        if (button.id < 200 && button instanceof GuiOptionButton) {
            this.guiGameSettings.setOptionValue(((GuiOptionButton) button).getOption(), val);
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

    @Unique
    protected void optiRefine$actionPerformedRightClick(GuiButton button) {
        if (button != null && button.id == GameSettings.Options.GUI_SCALE.ordinal()) {
            optiRefine$actionPerformedImpl(button, -1);
        }
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void optiRefine$drawScreenHead(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 15, 0xFFFFFF);

        final String ver = "OptiFine HD G5 Ultra + " + Reference.BRAND;
        this.drawString(this.fontRenderer, ver, 2, this.height - 10, 8421504);

        final String verMc = "Cleanroom " + CleanroomVersion.getVersion();
        int len = this.fontRenderer.getStringWidth(verMc);
        this.drawString(this.fontRenderer, verMc, this.width - len - 2, this.height - 10, 8421504);
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void optiRefine$drawScreenTail(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.optiRefine$tooltipManager.drawTooltips(mouseX, mouseY, this.buttonList);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static int getButtonWidth(GuiButton btn) { return btn.width; }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static int getButtonHeight(GuiButton btn) { return btn.height; }

    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL,
            desc = "net.minecraft.client.gui.GuiScreen func_73733_a (IIIIII)V", deobf = true)
    private static void _acc_GuiScreen_draw(GuiScreen guiScreen,
                                            int left, int top, int right, int bottom,
                                            int startColor, int endColor) {
        throw new AbstractMethodError();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void drawGradientRect(GuiScreen guiScreen,
                                         int left, int top, int right, int bottom,
                                         int startColor, int endColor) {
        _acc_GuiScreen_draw(guiScreen, left, top, right, bottom, startColor, endColor);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static String getGuiChatText(GuiChat guiChat) {
        return _acc_GuiChatAccessor_getInputField(guiChat).getText();
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD,
            desc = "net.minecraft.client.gui.GuiChat field_146415_a Lnet.minecraft.client.gui.GuiTextField;", deobf = true)
    private static native GuiTextField _acc_GuiChatAccessor_getInputField(GuiChat guiChat);
}
