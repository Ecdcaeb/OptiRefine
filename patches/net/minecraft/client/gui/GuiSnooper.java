package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper extends GuiScreen {
   private final GuiScreen lastScreen;
   private final GameSettings game_settings_2;
   private final java.util.List<String> keys = Lists.newArrayList();
   private final java.util.List<String> values = Lists.newArrayList();
   private String title;
   private String[] desc;
   private GuiSnooper.List list;
   private GuiButton toggleButton;

   public GuiSnooper(GuiScreen var1, GameSettings var2) {
      this.lastScreen = ☃;
      this.game_settings_2 = ☃;
   }

   @Override
   public void initGui() {
      this.title = I18n.format("options.snooper.title");
      String ☃ = I18n.format("options.snooper.desc");
      java.util.List<String> ☃x = Lists.newArrayList();

      for (String ☃xx : this.fontRenderer.listFormattedStringToWidth(☃, this.width - 30)) {
         ☃x.add(☃xx);
      }

      this.desc = ☃x.toArray(new String[☃x.size()]);
      this.keys.clear();
      this.values.clear();
      this.toggleButton = this.addButton(
         new GuiButton(1, this.width / 2 - 152, this.height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED))
      );
      this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, I18n.format("gui.done")));
      boolean ☃xx = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;

      for (Entry<String, String> ☃xxx : new TreeMap<>(this.mc.getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
         this.keys.add((☃xx ? "C " : "") + ☃xxx.getKey());
         this.values.add(this.fontRenderer.trimStringToWidth(☃xxx.getValue(), this.width - 220));
      }

      if (☃xx) {
         for (Entry<String, String> ☃xxx : new TreeMap<>(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
            this.keys.add("S " + ☃xxx.getKey());
            this.values.add(this.fontRenderer.trimStringToWidth(☃xxx.getValue(), this.width - 220));
         }
      }

      this.list = new GuiSnooper.List();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.list.handleMouseInput();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 2) {
            this.game_settings_2.saveOptions();
            this.game_settings_2.saveOptions();
            this.mc.displayGuiScreen(this.lastScreen);
         }

         if (☃.id == 1) {
            this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
            this.toggleButton.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.list.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 8, 16777215);
      int ☃ = 22;

      for (String ☃x : this.desc) {
         this.drawCenteredString(this.fontRenderer, ☃x, this.width / 2, ☃, 8421504);
         ☃ += this.fontRenderer.FONT_HEIGHT;
      }

      super.drawScreen(☃, ☃, ☃);
   }

   class List extends GuiSlot {
      public List() {
         super(GuiSnooper.this.mc, GuiSnooper.this.width, GuiSnooper.this.height, 80, GuiSnooper.this.height - 40, GuiSnooper.this.fontRenderer.FONT_HEIGHT + 1);
      }

      @Override
      protected int getSize() {
         return GuiSnooper.this.keys.size();
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      @Override
      protected boolean isSelected(int var1) {
         return false;
      }

      @Override
      protected void drawBackground() {
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         GuiSnooper.this.fontRenderer.drawString(GuiSnooper.this.keys.get(☃), 10, ☃, 16777215);
         GuiSnooper.this.fontRenderer.drawString(GuiSnooper.this.values.get(☃), 230, ☃, 16777215);
      }

      @Override
      protected int getScrollBarX() {
         return this.width - 10;
      }
   }
}
