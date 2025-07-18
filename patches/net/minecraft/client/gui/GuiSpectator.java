package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiSpectator extends Gui implements ISpectatorMenuRecipient {
   private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
   public static final ResourceLocation SPECTATOR_WIDGETS = new ResourceLocation("textures/gui/spectator_widgets.png");
   private final Minecraft mc;
   private long lastSelectionTime;
   private SpectatorMenu menu;

   public GuiSpectator(Minecraft var1) {
      this.mc = ☃;
   }

   public void onHotbarSelected(int var1) {
      this.lastSelectionTime = Minecraft.getSystemTime();
      if (this.menu != null) {
         this.menu.selectSlot(☃);
      } else {
         this.menu = new SpectatorMenu(this);
      }
   }

   private float getHotbarAlpha() {
      long ☃ = this.lastSelectionTime - Minecraft.getSystemTime() + 5000L;
      return MathHelper.clamp((float)☃ / 2000.0F, 0.0F, 1.0F);
   }

   public void renderTooltip(ScaledResolution var1, float var2) {
      if (this.menu != null) {
         float ☃ = this.getHotbarAlpha();
         if (☃ <= 0.0F) {
            this.menu.exit();
         } else {
            int ☃x = ☃.getScaledWidth() / 2;
            float ☃xx = this.zLevel;
            this.zLevel = -90.0F;
            float ☃xxx = ☃.getScaledHeight() - 22.0F * ☃;
            SpectatorDetails ☃xxxx = this.menu.getCurrentPage();
            this.renderPage(☃, ☃, ☃x, ☃xxx, ☃xxxx);
            this.zLevel = ☃xx;
         }
      }
   }

   protected void renderPage(ScaledResolution var1, float var2, int var3, float var4, SpectatorDetails var5) {
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.color(1.0F, 1.0F, 1.0F, ☃);
      this.mc.getTextureManager().bindTexture(WIDGETS);
      this.drawTexturedModalRect(☃ - 91, ☃, 0, 0, 182, 22);
      if (☃.getSelectedSlot() >= 0) {
         this.drawTexturedModalRect(☃ - 91 - 1 + ☃.getSelectedSlot() * 20, ☃ - 1.0F, 0, 22, 24, 22);
      }

      RenderHelper.enableGUIStandardItemLighting();

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.renderSlot(☃, ☃.getScaledWidth() / 2 - 90 + ☃ * 20 + 2, ☃ + 3.0F, ☃, ☃.getObject(☃));
      }

      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
   }

   private void renderSlot(int var1, int var2, float var3, float var4, ISpectatorMenuObject var5) {
      this.mc.getTextureManager().bindTexture(SPECTATOR_WIDGETS);
      if (☃ != SpectatorMenu.EMPTY_SLOT) {
         int ☃ = (int)(☃ * 255.0F);
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)☃, ☃, 0.0F);
         float ☃x = ☃.isEnabled() ? 1.0F : 0.25F;
         GlStateManager.color(☃x, ☃x, ☃x, ☃);
         ☃.renderIcon(☃x, ☃);
         GlStateManager.popMatrix();
         String ☃xx = String.valueOf(GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[☃].getKeyCode()));
         if (☃ > 3 && ☃.isEnabled()) {
            this.mc.fontRenderer.drawStringWithShadow(☃xx, ☃ + 19 - 2 - this.mc.fontRenderer.getStringWidth(☃xx), ☃ + 6.0F + 3.0F, 16777215 + (☃ << 24));
         }
      }
   }

   public void renderSelectedItem(ScaledResolution var1) {
      int ☃ = (int)(this.getHotbarAlpha() * 255.0F);
      if (☃ > 3 && this.menu != null) {
         ISpectatorMenuObject ☃x = this.menu.getSelectedItem();
         String ☃xx = ☃x == SpectatorMenu.EMPTY_SLOT
            ? this.menu.getSelectedCategory().getPrompt().getFormattedText()
            : ☃x.getSpectatorName().getFormattedText();
         if (☃xx != null) {
            int ☃xxx = (☃.getScaledWidth() - this.mc.fontRenderer.getStringWidth(☃xx)) / 2;
            int ☃xxxx = ☃.getScaledHeight() - 35;
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            this.mc.fontRenderer.drawStringWithShadow(☃xx, ☃xxx, ☃xxxx, 16777215 + (☃ << 24));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }
      }
   }

   @Override
   public void onSpectatorMenuClosed(SpectatorMenu var1) {
      this.menu = null;
      this.lastSelectionTime = 0L;
   }

   public boolean isMenuActive() {
      return this.menu != null;
   }

   public void onMouseScroll(int var1) {
      int ☃ = this.menu.getSelectedSlot() + ☃;

      while (☃ >= 0 && ☃ <= 8 && (this.menu.getItem(☃) == SpectatorMenu.EMPTY_SLOT || !this.menu.getItem(☃).isEnabled())) {
         ☃ += ☃;
      }

      if (☃ >= 0 && ☃ <= 8) {
         this.menu.selectSlot(☃);
         this.lastSelectionTime = Minecraft.getSystemTime();
      }
   }

   public void onMiddleClick() {
      this.lastSelectionTime = Minecraft.getSystemTime();
      if (this.isMenuActive()) {
         int ☃ = this.menu.getSelectedSlot();
         if (☃ != -1) {
            this.menu.selectSlot(☃);
         }
      } else {
         this.menu = new SpectatorMenu(this);
      }
   }
}
