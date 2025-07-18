package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen {
   protected GuiScreen parentScreen;
   private GuiLanguage.List list;
   private final GameSettings game_settings_3;
   private final LanguageManager languageManager;
   private GuiOptionButton forceUnicodeFontBtn;
   private GuiOptionButton confirmSettingsBtn;

   public GuiLanguage(GuiScreen var1, GameSettings var2, LanguageManager var3) {
      this.parentScreen = ☃;
      this.game_settings_3 = ☃;
      this.languageManager = ☃;
   }

   @Override
   public void initGui() {
      this.forceUnicodeFontBtn = this.addButton(
         new GuiOptionButton(
            100,
            this.width / 2 - 155,
            this.height - 38,
            GameSettings.Options.FORCE_UNICODE_FONT,
            this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)
         )
      );
      this.confirmSettingsBtn = this.addButton(new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done")));
      this.list = new GuiLanguage.List(this.mc);
      this.list.registerScrollButtons(7, 8);
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.list.handleMouseInput();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         switch (☃.id) {
            case 5:
               break;
            case 6:
               this.mc.displayGuiScreen(this.parentScreen);
               break;
            case 100:
               if (☃ instanceof GuiOptionButton) {
                  this.game_settings_3.setOptionValue(((GuiOptionButton)☃).getOption(), 1);
                  ☃.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                  ScaledResolution ☃ = new ScaledResolution(this.mc);
                  int ☃x = ☃.getScaledWidth();
                  int ☃xx = ☃.getScaledHeight();
                  this.setWorldAndResolution(this.mc, ☃x, ☃xx);
               }
               break;
            default:
               this.list.actionPerformed(☃);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.list.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, I18n.format("options.language"), this.width / 2, 16, 16777215);
      this.drawCenteredString(this.fontRenderer, "(" + I18n.format("options.languageWarning") + ")", this.width / 2, this.height - 56, 8421504);
      super.drawScreen(☃, ☃, ☃);
   }

   class List extends GuiSlot {
      private final java.util.List<String> langCodeList = Lists.newArrayList();
      private final Map<String, Language> languageMap = Maps.newHashMap();

      public List(Minecraft var2) {
         super(☃, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);

         for (Language ☃ : GuiLanguage.this.languageManager.getLanguages()) {
            this.languageMap.put(☃.getLanguageCode(), ☃);
            this.langCodeList.add(☃.getLanguageCode());
         }
      }

      @Override
      protected int getSize() {
         return this.langCodeList.size();
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         Language ☃ = this.languageMap.get(this.langCodeList.get(☃));
         GuiLanguage.this.languageManager.setCurrentLanguage(☃);
         GuiLanguage.this.game_settings_3.language = ☃.getLanguageCode();
         this.mc.refreshResources();
         GuiLanguage.this.fontRenderer
            .setUnicodeFlag(GuiLanguage.this.languageManager.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont);
         GuiLanguage.this.fontRenderer.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
         GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done");
         GuiLanguage.this.forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
         GuiLanguage.this.game_settings_3.saveOptions();
      }

      @Override
      protected boolean isSelected(int var1) {
         return this.langCodeList.get(☃).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
      }

      @Override
      protected int getContentHeight() {
         return this.getSize() * 18;
      }

      @Override
      protected void drawBackground() {
         GuiLanguage.this.drawDefaultBackground();
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         GuiLanguage.this.fontRenderer.setBidiFlag(true);
         GuiLanguage.this.drawCenteredString(
            GuiLanguage.this.fontRenderer, this.languageMap.get(this.langCodeList.get(☃)).toString(), this.width / 2, ☃ + 1, 16777215
         );
         GuiLanguage.this.fontRenderer.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
      }
   }
}
