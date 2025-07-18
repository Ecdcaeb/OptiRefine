package net.minecraft.client.gui.advancements;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiAdvancementTab extends Gui {
   private final Minecraft minecraft;
   private final GuiScreenAdvancements screen;
   private final AdvancementTabType type;
   private final int index;
   private final Advancement advancement;
   private final DisplayInfo display;
   private final ItemStack icon;
   private final String title;
   private final GuiAdvancement root;
   private final Map<Advancement, GuiAdvancement> guis = Maps.newLinkedHashMap();
   private int scrollX;
   private int scrollY;
   private int minX = Integer.MAX_VALUE;
   private int minY = Integer.MAX_VALUE;
   private int maxX = Integer.MIN_VALUE;
   private int maxY = Integer.MIN_VALUE;
   private float fade;
   private boolean centered;

   public GuiAdvancementTab(Minecraft var1, GuiScreenAdvancements var2, AdvancementTabType var3, int var4, Advancement var5, DisplayInfo var6) {
      this.minecraft = ☃;
      this.screen = ☃;
      this.type = ☃;
      this.index = ☃;
      this.advancement = ☃;
      this.display = ☃;
      this.icon = ☃.getIcon();
      this.title = ☃.getTitle().getFormattedText();
      this.root = new GuiAdvancement(this, ☃, ☃, ☃);
      this.addGuiAdvancement(this.root, ☃);
   }

   public Advancement getAdvancement() {
      return this.advancement;
   }

   public String getTitle() {
      return this.title;
   }

   public void drawTab(int var1, int var2, boolean var3) {
      this.type.draw(this, ☃, ☃, ☃, this.index);
   }

   public void drawIcon(int var1, int var2, RenderItem var3) {
      this.type.drawIcon(☃, ☃, this.index, ☃, this.icon);
   }

   public void drawContents() {
      if (!this.centered) {
         this.scrollX = 117 - (this.maxX + this.minX) / 2;
         this.scrollY = 56 - (this.maxY + this.minY) / 2;
         this.centered = true;
      }

      GlStateManager.depthFunc(518);
      drawRect(0, 0, 234, 113, -16777216);
      GlStateManager.depthFunc(515);
      ResourceLocation ☃ = this.display.getBackground();
      if (☃ != null) {
         this.minecraft.getTextureManager().bindTexture(☃);
      } else {
         this.minecraft.getTextureManager().bindTexture(TextureManager.RESOURCE_LOCATION_EMPTY);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int ☃x = this.scrollX % 16;
      int ☃xx = this.scrollY % 16;

      for (int ☃xxx = -1; ☃xxx <= 15; ☃xxx++) {
         for (int ☃xxxx = -1; ☃xxxx <= 8; ☃xxxx++) {
            drawModalRectWithCustomSizedTexture(☃x + 16 * ☃xxx, ☃xx + 16 * ☃xxxx, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
         }
      }

      this.root.drawConnectivity(this.scrollX, this.scrollY, true);
      this.root.drawConnectivity(this.scrollX, this.scrollY, false);
      this.root.draw(this.scrollX, this.scrollY);
   }

   public void drawToolTips(int var1, int var2, int var3, int var4) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, 0.0F, 200.0F);
      drawRect(0, 0, 234, 113, MathHelper.floor(this.fade * 255.0F) << 24);
      boolean ☃ = false;
      if (☃ > 0 && ☃ < 234 && ☃ > 0 && ☃ < 113) {
         for (GuiAdvancement ☃x : this.guis.values()) {
            if (☃x.isMouseOver(this.scrollX, this.scrollY, ☃, ☃)) {
               ☃ = true;
               ☃x.drawHover(this.scrollX, this.scrollY, this.fade, ☃, ☃);
               break;
            }
         }
      }

      GlStateManager.popMatrix();
      if (☃) {
         this.fade = MathHelper.clamp(this.fade + 0.02F, 0.0F, 0.3F);
      } else {
         this.fade = MathHelper.clamp(this.fade - 0.04F, 0.0F, 1.0F);
      }
   }

   public boolean isMouseOver(int var1, int var2, int var3, int var4) {
      return this.type.isMouseOver(☃, ☃, this.index, ☃, ☃);
   }

   @Nullable
   public static GuiAdvancementTab create(Minecraft var0, GuiScreenAdvancements var1, int var2, Advancement var3) {
      if (☃.getDisplay() == null) {
         return null;
      } else {
         for (AdvancementTabType ☃ : AdvancementTabType.values()) {
            if (☃ < ☃.getMax()) {
               return new GuiAdvancementTab(☃, ☃, ☃, ☃, ☃, ☃.getDisplay());
            }

            ☃ -= ☃.getMax();
         }

         return null;
      }
   }

   public void scroll(int var1, int var2) {
      if (this.maxX - this.minX > 234) {
         this.scrollX = MathHelper.clamp(this.scrollX + ☃, -(this.maxX - 234), 0);
      }

      if (this.maxY - this.minY > 113) {
         this.scrollY = MathHelper.clamp(this.scrollY + ☃, -(this.maxY - 113), 0);
      }
   }

   public void addAdvancement(Advancement var1) {
      if (☃.getDisplay() != null) {
         GuiAdvancement ☃ = new GuiAdvancement(this, this.minecraft, ☃, ☃.getDisplay());
         this.addGuiAdvancement(☃, ☃);
      }
   }

   private void addGuiAdvancement(GuiAdvancement var1, Advancement var2) {
      this.guis.put(☃, ☃);
      int ☃ = ☃.getX();
      int ☃x = ☃ + 28;
      int ☃xx = ☃.getY();
      int ☃xxx = ☃xx + 27;
      this.minX = Math.min(this.minX, ☃);
      this.maxX = Math.max(this.maxX, ☃x);
      this.minY = Math.min(this.minY, ☃xx);
      this.maxY = Math.max(this.maxY, ☃xxx);

      for (GuiAdvancement ☃xxxx : this.guis.values()) {
         ☃xxxx.attachToParent();
      }
   }

   @Nullable
   public GuiAdvancement getAdvancementGui(Advancement var1) {
      return this.guis.get(☃);
   }

   public GuiScreenAdvancements getScreen() {
      return this.screen;
   }
}
