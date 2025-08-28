package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.Reference;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ChangeSuperClass;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.optifine.Lang;
import net.optifine.gui.GuiAnimationSettingsOF;
import net.optifine.gui.GuiDetailSettingsOF;
import net.optifine.gui.GuiOtherSettingsOF;
import net.optifine.gui.GuiPerformanceSettingsOF;
import net.optifine.gui.GuiQualitySettingsOF;
import net.optifine.gui.GuiScreenOF;
import net.optifine.gui.TooltipManager;
import net.optifine.gui.TooltipProviderOptions;
import net.optifine.shaders.gui.GuiShaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiVideoSettings.class)
@ChangeSuperClass(GuiScreenOF.class)
public abstract class MixinGuiVideoSettings extends GuiScreen {
    @Shadow
    @Final
    private GuiScreen parentGuiScreen;

    @Shadow
    protected String screenTitle;

    @Shadow
    @Final
    private GameSettings guiGameSettings;

    @SuppressWarnings("unused")
    @Unique
    private static final String __OBFID = "CL_00000718";

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static GameSettings.Options[] videoOptions = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettingsOptionOF.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.ATTACK_INDICATOR, GameSettingsOptionOF.DYNAMIC_LIGHTS, GameSettingsOptionOF.DYNAMIC_FOV};

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());

    @Redirect(method = "initGui", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiVideoSettings;VIDEO_OPTIONS:[Lnet/minecraft/client/settings/GameSettings$Options;"))
    private GameSettings.Options[] redirectUpdatedEnumVideoSettings(){
        return videoOptions;
    }

    @Redirect(method = "initGui", at = @At(value = "NEW", target = "(Lnet/minecraft/client/Minecraft;IIIII[Lnet/minecraft/client/settings/GameSettings$Options;)Lnet/minecraft/client/gui/GuiOptionsRowList;"))
    public GuiOptionsRowList adjustRollHight(Minecraft p_i45015_1, int p_i45015_2, int p_i45015_3, int p_i45015_4, int p_i45015_5, int p_i45015_6, GameSettings.Options[] p_i45015_7){
        return new GuiOptionsRowList(p_i45015_1, p_i45015_2, p_i45015_3, p_i45015_4, p_i45015_5 - 48, p_i45015_6, p_i45015_7);
    }

    @WrapOperation(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    public boolean addExtraInitGui(List<?> instance, Object e, Operation<Boolean> original) {
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

        return true;
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void injectActionPerformed(GuiButton button, CallbackInfo ci){
        if (button.enabled && button.id != 200) {
            actionPerformed(button, 1);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
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

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    protected void actionPerformedRightClick(GuiButton button) {
        if (button.id == GameSettings.Options.GUI_SCALE.ordinal()) {
            this.actionPerformed(button, -1);
        }
    }

    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiListExtended;drawScreen(IIF)V"))
    public void injectDrawScreen(GuiListExtended instance, int x, int y, float v, Operation<Void> original){
        this.drawString(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
        final String ver = "OptiFine HD G5 Ultra + " + Reference.BRAND;
        this.drawCenteredString(this.fontRenderer, ver, 2, this.height - 10, 8421504);
        final String verMc = "Minecraft 1.12.2";
        int lenMc = this.fontRenderer.getStringWidth(verMc);
        this.drawCenteredString(this.fontRenderer, verMc, this.width - lenMc - 2, this.height - 10, 8421504);
        original.call(instance, x, y, v);
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static int getButtonWidth(GuiButton btn) {
        return btn.width;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static int getButtonHeight(GuiButton btn) {
        return btn.height;
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.gui.GuiScreen func_73733_a (IIIIII)V", deobf = true)
    private static void _acc_GuiScreen_draw(GuiScreen guiScreen, int left, int top, int right, int bottom, int startColor, int endColor){
        throw new AbstractMethodError();
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static void drawGradientRect(GuiScreen guiScreen, int left, int top, int right, int bottom, int startColor, int endColor) {
        _acc_GuiScreen_draw(guiScreen, left, top, right, bottom, startColor, endColor);
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static String getGuiChatText(GuiChat guiChat) {
        return _acc_GuiChatAccessor_getInputField(guiChat).getText();
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.gui.GuiChat field_146415_a Lnet.minecraft.client.gui.GuiTextField;", deobf = true)
    private static native GuiTextField _acc_GuiChatAccessor_getInputField(GuiChat guiChat);

}

/*
+++ net/minecraft/client/gui/GuiVideoSettings.java	Tue Aug 19 14:59:58 2025
@@ -1,111 +1,209 @@
 package net.minecraft.client.gui;

-import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.resources.I18n;
 import net.minecraft.client.settings.GameSettings;
+import net.optifine.Lang;
+import net.optifine.gui.GuiAnimationSettingsOF;
+import net.optifine.gui.GuiDetailSettingsOF;
+import net.optifine.gui.GuiOptionButtonOF;
+import net.optifine.gui.GuiOptionSliderOF;
+import net.optifine.gui.GuiOtherSettingsOF;
+import net.optifine.gui.GuiPerformanceSettingsOF;
+import net.optifine.gui.GuiQualitySettingsOF;
+import net.optifine.gui.GuiScreenOF;
+import net.optifine.gui.TooltipManager;
+import net.optifine.gui.TooltipProviderOptions;
+import net.optifine.shaders.gui.GuiShaders;

-public class GuiVideoSettings extends GuiScreen {
-   private final GuiScreen parentGuiScreen;
+public class GuiVideoSettings extends GuiScreenOF {
+   private GuiScreen parentGuiScreen;
    protected String screenTitle = "Video Settings";
-   private final GameSettings guiGameSettings;
-   private GuiListExtended optionsRowList;
-   private static final GameSettings.Options[] VIDEO_OPTIONS = new GameSettings.Options[]{
+   private GameSettings guiGameSettings;
+   private static GameSettings.Options[] videoOptions = new GameSettings.Options[]{
       GameSettings.Options.GRAPHICS,
       GameSettings.Options.RENDER_DISTANCE,
       GameSettings.Options.AMBIENT_OCCLUSION,
       GameSettings.Options.FRAMERATE_LIMIT,
-      GameSettings.Options.ANAGLYPH,
+      GameSettings.Options.AO_LEVEL,
       GameSettings.Options.VIEW_BOBBING,
       GameSettings.Options.GUI_SCALE,
-      GameSettings.Options.ATTACK_INDICATOR,
-      GameSettings.Options.GAMMA,
-      GameSettings.Options.RENDER_CLOUDS,
-      GameSettings.Options.PARTICLES,
-      GameSettings.Options.USE_FULLSCREEN,
-      GameSettings.Options.ENABLE_VSYNC,
-      GameSettings.Options.MIPMAP_LEVELS,
       GameSettings.Options.USE_VBO,
-      GameSettings.Options.ENTITY_SHADOWS
+      GameSettings.Options.GAMMA,
+      GameSettings.Options.ATTACK_INDICATOR,
+      GameSettings.Options.DYNAMIC_LIGHTS,
+      GameSettings.Options.DYNAMIC_FOV
    };
+   private static final String __OBFID = "CL_00000718";
+   private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());

    public GuiVideoSettings(GuiScreen var1, GameSettings var2) {
       this.parentGuiScreen = var1;
       this.guiGameSettings = var2;
    }

    public void initGui() {
       this.screenTitle = I18n.format("options.videoTitle");
       this.buttonList.clear();
-      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 27, I18n.format("gui.done")));
-      if (OpenGlHelper.vboSupported) {
-         this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, VIDEO_OPTIONS);
-      } else {
-         GameSettings.Options[] var1 = new GameSettings.Options[VIDEO_OPTIONS.length - 1];
-         int var2 = 0;
-
-         for (GameSettings.Options var6 : VIDEO_OPTIONS) {
-            if (var6 == GameSettings.Options.USE_VBO) {
-               break;
-            }

-            var1[var2] = var6;
-            var2++;
+      for (int var1 = 0; var1 < videoOptions.length; var1++) {
+         GameSettings.Options var2 = videoOptions[var1];
+         if (var2 != null) {
+            int var3 = this.width / 2 - 155 + var1 % 2 * 160;
+            int var4 = this.height / 6 + 21 * (var1 / 2) - 12;
+            if (var2.isFloat()) {
+               this.buttonList.add(new GuiOptionSliderOF(var2.getOrdinal(), var3, var4, var2));
+            } else {
+               this.buttonList.add(new GuiOptionButtonOF(var2.getOrdinal(), var3, var4, var2, this.guiGameSettings.getKeyBinding(var2)));
+            }
          }
-
-         this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, var1);
       }
+
+      int var5 = this.height / 6 + 21 * (videoOptions.length / 2) - 12;
+      int var9 = 0;
+      var9 = this.width / 2 - 155 + 0;
+      this.buttonList.add(new GuiOptionButton(231, var9, var5, Lang.get("of.options.shaders")));
+      var9 = this.width / 2 - 155 + 160;
+      this.buttonList.add(new GuiOptionButton(202, var9, var5, Lang.get("of.options.quality")));
+      var5 += 21;
+      var9 = this.width / 2 - 155 + 0;
+      this.buttonList.add(new GuiOptionButton(201, var9, var5, Lang.get("of.options.details")));
+      var9 = this.width / 2 - 155 + 160;
+      this.buttonList.add(new GuiOptionButton(212, var9, var5, Lang.get("of.options.performance")));
+      var5 += 21;
+      var9 = this.width / 2 - 155 + 0;
+      this.buttonList.add(new GuiOptionButton(211, var9, var5, Lang.get("of.options.animations")));
+      var9 = this.width / 2 - 155 + 160;
+      this.buttonList.add(new GuiOptionButton(222, var9, var5, Lang.get("of.options.other")));
+      var5 += 21;
+      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done")));
    }

-   public void handleMouseInput() {
-      super.handleMouseInput();
-      this.optionsRowList.handleMouseInput();
+   protected void actionPerformed(GuiButton var1) {
+      this.actionPerformed(var1, 1);
    }

-   protected void keyTyped(char var1, int var2) {
-      if (var2 == 1) {
-         this.mc.gameSettings.saveOptions();
+   protected void actionPerformedRightClick(GuiButton var1) {
+      if (var1.id == GameSettings.Options.GUI_SCALE.ordinal()) {
+         this.actionPerformed(var1, -1);
       }
-
-      super.keyTyped(var1, var2);
    }

-   protected void actionPerformed(GuiButton var1) {
+   private void actionPerformed(GuiButton var1, int var2) {
       if (var1.enabled) {
+         int var3 = this.guiGameSettings.guiScale;
+         if (var1.id < 200 && var1 instanceof GuiOptionButton) {
+            this.guiGameSettings.setOptionValue(((GuiOptionButton)var1).getOption(), var2);
+            var1.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.byOrdinal(var1.id));
+         }
+
          if (var1.id == 200) {
             this.mc.gameSettings.saveOptions();
             this.mc.displayGuiScreen(this.parentGuiScreen);
          }
-      }
-   }

-   protected void mouseClicked(int var1, int var2, int var3) {
-      int var4 = this.guiGameSettings.guiScale;
-      super.mouseClicked(var1, var2, var3);
-      this.optionsRowList.mouseClicked(var1, var2, var3);
-      if (this.guiGameSettings.guiScale != var4) {
-         ScaledResolution var5 = new ScaledResolution(this.mc);
-         int var6 = var5.getScaledWidth();
-         int var7 = var5.getScaledHeight();
-         this.setWorldAndResolution(this.mc, var6, var7);
-      }
-   }
+         if (this.guiGameSettings.guiScale != var3) {
+            ScaledResolution var4 = new ScaledResolution(this.mc);
+            int var5 = var4.getScaledWidth();
+            int var6 = var4.getScaledHeight();
+            this.setWorldAndResolution(this.mc, var5, var6);
+         }
+
+         if (var1.id == 201) {
+            this.mc.gameSettings.saveOptions();
+            GuiDetailSettingsOF var7 = new GuiDetailSettingsOF(this, this.guiGameSettings);
+            this.mc.displayGuiScreen(var7);
+         }
+
+         if (var1.id == 202) {
+            this.mc.gameSettings.saveOptions();
+            GuiQualitySettingsOF var8 = new GuiQualitySettingsOF(this, this.guiGameSettings);
+            this.mc.displayGuiScreen(var8);
+         }

-   protected void mouseReleased(int var1, int var2, int var3) {
-      int var4 = this.guiGameSettings.guiScale;
-      super.mouseReleased(var1, var2, var3);
-      this.optionsRowList.mouseReleased(var1, var2, var3);
-      if (this.guiGameSettings.guiScale != var4) {
-         ScaledResolution var5 = new ScaledResolution(this.mc);
-         int var6 = var5.getScaledWidth();
-         int var7 = var5.getScaledHeight();
-         this.setWorldAndResolution(this.mc, var6, var7);
+         if (var1.id == 211) {
+            this.mc.gameSettings.saveOptions();
+            GuiAnimationSettingsOF var9 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
+            this.mc.displayGuiScreen(var9);
+         }
+
+         if (var1.id == 212) {
+            this.mc.gameSettings.saveOptions();
+            GuiPerformanceSettingsOF var10 = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
+            this.mc.displayGuiScreen(var10);
+         }
+
+         if (var1.id == 222) {
+            this.mc.gameSettings.saveOptions();
+            GuiOtherSettingsOF var11 = new GuiOtherSettingsOF(this, this.guiGameSettings);
+            this.mc.displayGuiScreen(var11);
+         }
+
+         if (var1.id == 231) {
+            if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
+               Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
+               return;
+            }
+
+            if (Config.isAnisotropicFiltering()) {
+               Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
+               return;
+            }
+
+            if (Config.isFastRender()) {
+               Config.showGuiMessage(Lang.get("of.message.shaders.fr1"), Lang.get("of.message.shaders.fr2"));
+               return;
+            }
+
+            if (Config.getGameSettings().anaglyph) {
+               Config.showGuiMessage(Lang.get("of.message.shaders.an1"), Lang.get("of.message.shaders.an2"));
+               return;
+            }
+
+            this.mc.gameSettings.saveOptions();
+            GuiShaders var12 = new GuiShaders(this, this.guiGameSettings);
+            this.mc.displayGuiScreen(var12);
+         }
       }
    }

    public void drawScreen(int var1, int var2, float var3) {
       this.drawDefaultBackground();
-      this.optionsRowList.drawScreen(var1, var2, var3);
-      this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 5, 16777215);
+      this.a(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
+      String var4 = Config.getVersion();
+      String var5 = "HD_U";
+      if (var5.equals("HD")) {
+         var4 = "OptiFine HD G5";
+      }
+
+      if (var5.equals("HD_U")) {
+         var4 = "OptiFine HD G5 Ultra";
+      }
+
+      if (var5.equals("L")) {
+         var4 = "OptiFine G5 Light";
+      }
+
+      this.c(this.fontRenderer, var4, 2, this.height - 10, 8421504);
+      String var6 = "Minecraft 1.12.2";
+      int var7 = this.fontRenderer.getStringWidth(var6);
+      this.c(this.fontRenderer, var6, this.width - var7 - 2, this.height - 10, 8421504);
       super.drawScreen(var1, var2, var3);
+      this.tooltipManager.drawTooltips(var1, var2, this.buttonList);
+   }
+
+   public static int getButtonWidth(GuiButton var0) {
+      return var0.width;
+   }
+
+   public static int getButtonHeight(GuiButton var0) {
+      return var0.height;
+   }
+
+   public static void drawGradientRect(GuiScreen var0, int var1, int var2, int var3, int var4, int var5, int var6) {
+      var0.a(var1, var2, var3, var4, var5, var6);
+   }
+
+   public static String getGuiChatText(GuiChat var0) {
+      return var0.inputField.getText();
    }
 }
 */
