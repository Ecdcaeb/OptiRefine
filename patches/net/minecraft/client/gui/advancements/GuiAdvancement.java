package net.minecraft.client.gui.advancements;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiAdvancement extends Gui {
   private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/advancements/widgets.png");
   private static final Pattern PATTERN = Pattern.compile("(.+) \\S+");
   private final GuiAdvancementTab guiAdvancementTab;
   private final Advancement advancement;
   private final DisplayInfo displayInfo;
   private final String title;
   private final int width;
   private final List<String> description;
   private final Minecraft minecraft;
   private GuiAdvancement parent;
   private final List<GuiAdvancement> children = Lists.newArrayList();
   private AdvancementProgress advancementProgress;
   private final int x;
   private final int y;

   public GuiAdvancement(GuiAdvancementTab var1, Minecraft var2, Advancement var3, DisplayInfo var4) {
      this.guiAdvancementTab = ☃;
      this.advancement = ☃;
      this.displayInfo = ☃;
      this.minecraft = ☃;
      this.title = ☃.fontRenderer.trimStringToWidth(☃.getTitle().getFormattedText(), 163);
      this.x = MathHelper.floor(☃.getX() * 28.0F);
      this.y = MathHelper.floor(☃.getY() * 27.0F);
      int ☃ = ☃.getRequirementCount();
      int ☃x = String.valueOf(☃).length();
      int ☃xx = ☃ > 1 ? ☃.fontRenderer.getStringWidth("  ") + ☃.fontRenderer.getStringWidth("0") * ☃x * 2 + ☃.fontRenderer.getStringWidth("/") : 0;
      int ☃xxx = 29 + ☃.fontRenderer.getStringWidth(this.title) + ☃xx;
      String ☃xxxx = ☃.getDescription().getFormattedText();
      this.description = this.findOptimalLines(☃xxxx, ☃xxx);

      for (String ☃xxxxx : this.description) {
         ☃xxx = Math.max(☃xxx, ☃.fontRenderer.getStringWidth(☃xxxxx));
      }

      this.width = ☃xxx + 3 + 5;
   }

   private List<String> findOptimalLines(String var1, int var2) {
      if (☃.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<String> ☃ = this.minecraft.fontRenderer.listFormattedStringToWidth(☃, ☃);
         if (☃.size() < 2) {
            return ☃;
         } else {
            String ☃x = ☃.get(0);
            String ☃xx = ☃.get(1);
            int ☃xxx = this.minecraft.fontRenderer.getStringWidth(☃x + ' ' + ☃xx.split(" ")[0]);
            if (☃xxx - ☃ <= 10) {
               return this.minecraft.fontRenderer.listFormattedStringToWidth(☃, ☃xxx);
            } else {
               Matcher ☃xxxx = PATTERN.matcher(☃x);
               if (☃xxxx.matches()) {
                  int ☃xxxxx = this.minecraft.fontRenderer.getStringWidth(☃xxxx.group(1));
                  if (☃ - ☃xxxxx <= 10) {
                     return this.minecraft.fontRenderer.listFormattedStringToWidth(☃, ☃xxxxx);
                  }
               }

               return ☃;
            }
         }
      }
   }

   @Nullable
   private GuiAdvancement getFirstVisibleParent(Advancement var1) {
      do {
         ☃ = ☃.getParent();
      } while (☃ != null && ☃.getDisplay() == null);

      return ☃ != null && ☃.getDisplay() != null ? this.guiAdvancementTab.getAdvancementGui(☃) : null;
   }

   public void drawConnectivity(int var1, int var2, boolean var3) {
      if (this.parent != null) {
         int ☃ = ☃ + this.parent.x + 13;
         int ☃x = ☃ + this.parent.x + 26 + 4;
         int ☃xx = ☃ + this.parent.y + 13;
         int ☃xxx = ☃ + this.x + 13;
         int ☃xxxx = ☃ + this.y + 13;
         int ☃xxxxx = ☃ ? -16777216 : -1;
         if (☃) {
            this.drawHorizontalLine(☃x, ☃, ☃xx - 1, ☃xxxxx);
            this.drawHorizontalLine(☃x + 1, ☃, ☃xx, ☃xxxxx);
            this.drawHorizontalLine(☃x, ☃, ☃xx + 1, ☃xxxxx);
            this.drawHorizontalLine(☃xxx, ☃x - 1, ☃xxxx - 1, ☃xxxxx);
            this.drawHorizontalLine(☃xxx, ☃x - 1, ☃xxxx, ☃xxxxx);
            this.drawHorizontalLine(☃xxx, ☃x - 1, ☃xxxx + 1, ☃xxxxx);
            this.drawVerticalLine(☃x - 1, ☃xxxx, ☃xx, ☃xxxxx);
            this.drawVerticalLine(☃x + 1, ☃xxxx, ☃xx, ☃xxxxx);
         } else {
            this.drawHorizontalLine(☃x, ☃, ☃xx, ☃xxxxx);
            this.drawHorizontalLine(☃xxx, ☃x, ☃xxxx, ☃xxxxx);
            this.drawVerticalLine(☃x, ☃xxxx, ☃xx, ☃xxxxx);
         }
      }

      for (GuiAdvancement ☃ : this.children) {
         ☃.drawConnectivity(☃, ☃, ☃);
      }
   }

   public void draw(int var1, int var2) {
      if (!this.displayInfo.isHidden() || this.advancementProgress != null && this.advancementProgress.isDone()) {
         float ☃ = this.advancementProgress == null ? 0.0F : this.advancementProgress.getPercent();
         AdvancementState ☃x;
         if (☃ >= 1.0F) {
            ☃x = AdvancementState.OBTAINED;
         } else {
            ☃x = AdvancementState.UNOBTAINED;
         }

         this.minecraft.getTextureManager().bindTexture(WIDGETS);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableBlend();
         this.drawTexturedModalRect(☃ + this.x + 3, ☃ + this.y, this.displayInfo.getFrame().getIcon(), 128 + ☃x.getId() * 26, 26, 26);
         RenderHelper.enableGUIStandardItemLighting();
         this.minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, this.displayInfo.getIcon(), ☃ + this.x + 8, ☃ + this.y + 5);
      }

      for (GuiAdvancement ☃ : this.children) {
         ☃.draw(☃, ☃);
      }
   }

   public void setAdvancementProgress(AdvancementProgress var1) {
      this.advancementProgress = ☃;
   }

   public void addGuiAdvancement(GuiAdvancement var1) {
      this.children.add(☃);
   }

   public void drawHover(int var1, int var2, float var3, int var4, int var5) {
      boolean ☃ = ☃ + ☃ + this.x + this.width + 26 >= this.guiAdvancementTab.getScreen().width;
      String ☃x = this.advancementProgress == null ? null : this.advancementProgress.getProgressText();
      int ☃xx = ☃x == null ? 0 : this.minecraft.fontRenderer.getStringWidth(☃x);
      boolean ☃xxx = 113 - ☃ - this.y - 26 <= 6 + this.description.size() * this.minecraft.fontRenderer.FONT_HEIGHT;
      float ☃xxxx = this.advancementProgress == null ? 0.0F : this.advancementProgress.getPercent();
      int ☃xxxxx = MathHelper.floor(☃xxxx * this.width);
      AdvancementState ☃xxxxxx;
      AdvancementState ☃xxxxxxx;
      AdvancementState ☃xxxxxxxx;
      if (☃xxxx >= 1.0F) {
         ☃xxxxx = this.width / 2;
         ☃xxxxxx = AdvancementState.OBTAINED;
         ☃xxxxxxx = AdvancementState.OBTAINED;
         ☃xxxxxxxx = AdvancementState.OBTAINED;
      } else if (☃xxxxx < 2) {
         ☃xxxxx = this.width / 2;
         ☃xxxxxx = AdvancementState.UNOBTAINED;
         ☃xxxxxxx = AdvancementState.UNOBTAINED;
         ☃xxxxxxxx = AdvancementState.UNOBTAINED;
      } else if (☃xxxxx > this.width - 2) {
         ☃xxxxx = this.width / 2;
         ☃xxxxxx = AdvancementState.OBTAINED;
         ☃xxxxxxx = AdvancementState.OBTAINED;
         ☃xxxxxxxx = AdvancementState.UNOBTAINED;
      } else {
         ☃xxxxxx = AdvancementState.OBTAINED;
         ☃xxxxxxx = AdvancementState.UNOBTAINED;
         ☃xxxxxxxx = AdvancementState.UNOBTAINED;
      }

      int ☃xxxxxxxxx = this.width - ☃xxxxx;
      this.minecraft.getTextureManager().bindTexture(WIDGETS);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableBlend();
      int ☃xxxxxxxxxx = ☃ + this.y;
      int ☃xxxxxxxxxxx;
      if (☃) {
         ☃xxxxxxxxxxx = ☃ + this.x - this.width + 26 + 6;
      } else {
         ☃xxxxxxxxxxx = ☃ + this.x;
      }

      int ☃xxxxxxxxxxxx = 32 + this.description.size() * this.minecraft.fontRenderer.FONT_HEIGHT;
      if (!this.description.isEmpty()) {
         if (☃xxx) {
            this.render9Sprite(☃xxxxxxxxxxx, ☃xxxxxxxxxx + 26 - ☃xxxxxxxxxxxx, this.width, ☃xxxxxxxxxxxx, 10, 200, 26, 0, 52);
         } else {
            this.render9Sprite(☃xxxxxxxxxxx, ☃xxxxxxxxxx, this.width, ☃xxxxxxxxxxxx, 10, 200, 26, 0, 52);
         }
      }

      this.drawTexturedModalRect(☃xxxxxxxxxxx, ☃xxxxxxxxxx, 0, ☃xxxxxx.getId() * 26, ☃xxxxx, 26);
      this.drawTexturedModalRect(☃xxxxxxxxxxx + ☃xxxxx, ☃xxxxxxxxxx, 200 - ☃xxxxxxxxx, ☃xxxxxxx.getId() * 26, ☃xxxxxxxxx, 26);
      this.drawTexturedModalRect(☃ + this.x + 3, ☃ + this.y, this.displayInfo.getFrame().getIcon(), 128 + ☃xxxxxxxx.getId() * 26, 26, 26);
      if (☃) {
         this.minecraft.fontRenderer.drawString(this.title, ☃xxxxxxxxxxx + 5, ☃ + this.y + 9, -1, true);
         if (☃x != null) {
            this.minecraft.fontRenderer.drawString(☃x, ☃ + this.x - ☃xx, ☃ + this.y + 9, -1, true);
         }
      } else {
         this.minecraft.fontRenderer.drawString(this.title, ☃ + this.x + 32, ☃ + this.y + 9, -1, true);
         if (☃x != null) {
            this.minecraft.fontRenderer.drawString(☃x, ☃ + this.x + this.width - ☃xx - 5, ☃ + this.y + 9, -1, true);
         }
      }

      if (☃xxx) {
         for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < this.description.size(); ☃xxxxxxxxxxxxx++) {
            this.minecraft
               .fontRenderer
               .drawString(
                  this.description.get(☃xxxxxxxxxxxxx),
                  ☃xxxxxxxxxxx + 5,
                  ☃xxxxxxxxxx + 26 - ☃xxxxxxxxxxxx + 7 + ☃xxxxxxxxxxxxx * this.minecraft.fontRenderer.FONT_HEIGHT,
                  -5592406,
                  false
               );
         }
      } else {
         for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < this.description.size(); ☃xxxxxxxxxxxxx++) {
            this.minecraft
               .fontRenderer
               .drawString(
                  this.description.get(☃xxxxxxxxxxxxx),
                  ☃xxxxxxxxxxx + 5,
                  ☃ + this.y + 9 + 17 + ☃xxxxxxxxxxxxx * this.minecraft.fontRenderer.FONT_HEIGHT,
                  -5592406,
                  false
               );
         }
      }

      RenderHelper.enableGUIStandardItemLighting();
      this.minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, this.displayInfo.getIcon(), ☃ + this.x + 8, ☃ + this.y + 5);
   }

   protected void render9Sprite(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      this.drawTexturedModalRect(☃, ☃, ☃, ☃, ☃, ☃);
      this.renderRepeating(☃ + ☃, ☃, ☃ - ☃ - ☃, ☃, ☃ + ☃, ☃, ☃ - ☃ - ☃, ☃);
      this.drawTexturedModalRect(☃ + ☃ - ☃, ☃, ☃ + ☃ - ☃, ☃, ☃, ☃);
      this.drawTexturedModalRect(☃, ☃ + ☃ - ☃, ☃, ☃ + ☃ - ☃, ☃, ☃);
      this.renderRepeating(☃ + ☃, ☃ + ☃ - ☃, ☃ - ☃ - ☃, ☃, ☃ + ☃, ☃ + ☃ - ☃, ☃ - ☃ - ☃, ☃);
      this.drawTexturedModalRect(☃ + ☃ - ☃, ☃ + ☃ - ☃, ☃ + ☃ - ☃, ☃ + ☃ - ☃, ☃, ☃);
      this.renderRepeating(☃, ☃ + ☃, ☃, ☃ - ☃ - ☃, ☃, ☃ + ☃, ☃, ☃ - ☃ - ☃);
      this.renderRepeating(☃ + ☃, ☃ + ☃, ☃ - ☃ - ☃, ☃ - ☃ - ☃, ☃ + ☃, ☃ + ☃, ☃ - ☃ - ☃, ☃ - ☃ - ☃);
      this.renderRepeating(☃ + ☃ - ☃, ☃ + ☃, ☃, ☃ - ☃ - ☃, ☃ + ☃ - ☃, ☃ + ☃, ☃, ☃ - ☃ - ☃);
   }

   protected void renderRepeating(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int ☃ = 0;

      while (☃ < ☃) {
         int ☃x = ☃ + ☃;
         int ☃xx = Math.min(☃, ☃ - ☃);
         int ☃xxx = 0;

         while (☃xxx < ☃) {
            int ☃xxxx = ☃ + ☃xxx;
            int ☃xxxxx = Math.min(☃, ☃ - ☃xxx);
            this.drawTexturedModalRect(☃x, ☃xxxx, ☃, ☃, ☃xx, ☃xxxxx);
            ☃xxx += ☃;
         }

         ☃ += ☃;
      }
   }

   public boolean isMouseOver(int var1, int var2, int var3, int var4) {
      if (!this.displayInfo.isHidden() || this.advancementProgress != null && this.advancementProgress.isDone()) {
         int ☃ = ☃ + this.x;
         int ☃x = ☃ + 26;
         int ☃xx = ☃ + this.y;
         int ☃xxx = ☃xx + 26;
         return ☃ >= ☃ && ☃ <= ☃x && ☃ >= ☃xx && ☃ <= ☃xxx;
      } else {
         return false;
      }
   }

   public void attachToParent() {
      if (this.parent == null && this.advancement.getParent() != null) {
         this.parent = this.getFirstVisibleParent(this.advancement);
         if (this.parent != null) {
            this.parent.addGuiAdvancement(this);
         }
      }
   }

   public int getY() {
      return this.y;
   }

   public int getX() {
      return this.x;
   }
}
