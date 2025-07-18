package net.minecraft.client.gui.advancements;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiScreenAdvancements extends GuiScreen implements ClientAdvancementManager.IListener {
   private static final ResourceLocation WINDOW = new ResourceLocation("textures/gui/advancements/window.png");
   private static final ResourceLocation TABS = new ResourceLocation("textures/gui/advancements/tabs.png");
   private final ClientAdvancementManager clientAdvancementManager;
   private final Map<Advancement, GuiAdvancementTab> tabs = Maps.newLinkedHashMap();
   private GuiAdvancementTab selectedTab;
   private int scrollMouseX;
   private int scrollMouseY;
   private boolean isScrolling;

   public GuiScreenAdvancements(ClientAdvancementManager var1) {
      this.clientAdvancementManager = ☃;
   }

   @Override
   public void initGui() {
      this.tabs.clear();
      this.selectedTab = null;
      this.clientAdvancementManager.setListener(this);
      if (this.selectedTab == null && !this.tabs.isEmpty()) {
         this.clientAdvancementManager.setSelectedTab(this.tabs.values().iterator().next().getAdvancement(), true);
      } else {
         this.clientAdvancementManager.setSelectedTab(this.selectedTab == null ? null : this.selectedTab.getAdvancement(), true);
      }
   }

   @Override
   public void onGuiClosed() {
      this.clientAdvancementManager.setListener(null);
      NetHandlerPlayClient ☃ = this.mc.getConnection();
      if (☃ != null) {
         ☃.sendPacket(CPacketSeenAdvancements.closedScreen());
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (☃ == 0) {
         int ☃ = (this.width - 252) / 2;
         int ☃x = (this.height - 140) / 2;

         for (GuiAdvancementTab ☃xx : this.tabs.values()) {
            if (☃xx.isMouseOver(☃, ☃x, ☃, ☃)) {
               this.clientAdvancementManager.setSelectedTab(☃xx.getAdvancement(), true);
               break;
            }
         }
      }

      super.mouseClicked(☃, ☃, ☃);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (☃ == this.mc.gameSettings.keyBindAdvancements.getKeyCode()) {
         this.mc.displayGuiScreen(null);
         this.mc.setIngameFocus();
      } else {
         super.keyTyped(☃, ☃);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      int ☃ = (this.width - 252) / 2;
      int ☃x = (this.height - 140) / 2;
      if (Mouse.isButtonDown(0)) {
         if (!this.isScrolling) {
            this.isScrolling = true;
         } else if (this.selectedTab != null) {
            this.selectedTab.scroll(☃ - this.scrollMouseX, ☃ - this.scrollMouseY);
         }

         this.scrollMouseX = ☃;
         this.scrollMouseY = ☃;
      } else {
         this.isScrolling = false;
      }

      this.drawDefaultBackground();
      this.renderInside(☃, ☃, ☃, ☃x);
      this.renderWindow(☃, ☃x);
      this.renderToolTips(☃, ☃, ☃, ☃x);
   }

   private void renderInside(int var1, int var2, int var3, int var4) {
      GuiAdvancementTab ☃ = this.selectedTab;
      if (☃ == null) {
         drawRect(☃ + 9, ☃ + 18, ☃ + 9 + 234, ☃ + 18 + 113, -16777216);
         String ☃x = I18n.format("advancements.empty");
         int ☃xx = this.fontRenderer.getStringWidth(☃x);
         this.fontRenderer.drawString(☃x, ☃ + 9 + 117 - ☃xx / 2, ☃ + 18 + 56 - this.fontRenderer.FONT_HEIGHT / 2, -1);
         this.fontRenderer.drawString(":(", ☃ + 9 + 117 - this.fontRenderer.getStringWidth(":(") / 2, ☃ + 18 + 113 - this.fontRenderer.FONT_HEIGHT, -1);
      } else {
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)(☃ + 9), (float)(☃ + 18), -400.0F);
         GlStateManager.enableDepth();
         ☃.drawContents();
         GlStateManager.popMatrix();
         GlStateManager.depthFunc(515);
         GlStateManager.disableDepth();
      }
   }

   public void renderWindow(int var1, int var2) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableBlend();
      RenderHelper.disableStandardItemLighting();
      this.mc.getTextureManager().bindTexture(WINDOW);
      this.drawTexturedModalRect(☃, ☃, 0, 0, 252, 140);
      if (this.tabs.size() > 1) {
         this.mc.getTextureManager().bindTexture(TABS);

         for (GuiAdvancementTab ☃ : this.tabs.values()) {
            ☃.drawTab(☃, ☃, ☃ == this.selectedTab);
         }

         GlStateManager.enableRescaleNormal();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         RenderHelper.enableGUIStandardItemLighting();

         for (GuiAdvancementTab ☃ : this.tabs.values()) {
            ☃.drawIcon(☃, ☃, this.itemRender);
         }

         GlStateManager.disableBlend();
      }

      this.fontRenderer.drawString(I18n.format("gui.advancements"), ☃ + 8, ☃ + 6, 4210752);
   }

   private void renderToolTips(int var1, int var2, int var3, int var4) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.selectedTab != null) {
         GlStateManager.pushMatrix();
         GlStateManager.enableDepth();
         GlStateManager.translate((float)(☃ + 9), (float)(☃ + 18), 400.0F);
         this.selectedTab.drawToolTips(☃ - ☃ - 9, ☃ - ☃ - 18, ☃, ☃);
         GlStateManager.disableDepth();
         GlStateManager.popMatrix();
      }

      if (this.tabs.size() > 1) {
         for (GuiAdvancementTab ☃ : this.tabs.values()) {
            if (☃.isMouseOver(☃, ☃, ☃, ☃)) {
               this.drawHoveringText(☃.getTitle(), ☃, ☃);
            }
         }
      }
   }

   @Override
   public void rootAdvancementAdded(Advancement var1) {
      GuiAdvancementTab ☃ = GuiAdvancementTab.create(this.mc, this, this.tabs.size(), ☃);
      if (☃ != null) {
         this.tabs.put(☃, ☃);
      }
   }

   @Override
   public void rootAdvancementRemoved(Advancement var1) {
   }

   @Override
   public void nonRootAdvancementAdded(Advancement var1) {
      GuiAdvancementTab ☃ = this.getTab(☃);
      if (☃ != null) {
         ☃.addAdvancement(☃);
      }
   }

   @Override
   public void nonRootAdvancementRemoved(Advancement var1) {
   }

   @Override
   public void onUpdateAdvancementProgress(Advancement var1, AdvancementProgress var2) {
      GuiAdvancement ☃ = this.getAdvancementGui(☃);
      if (☃ != null) {
         ☃.setAdvancementProgress(☃);
      }
   }

   @Override
   public void setSelectedTab(@Nullable Advancement var1) {
      this.selectedTab = this.tabs.get(☃);
   }

   @Override
   public void advancementsCleared() {
      this.tabs.clear();
      this.selectedTab = null;
   }

   @Nullable
   public GuiAdvancement getAdvancementGui(Advancement var1) {
      GuiAdvancementTab ☃ = this.getTab(☃);
      return ☃ == null ? null : ☃.getAdvancementGui(☃);
   }

   @Nullable
   private GuiAdvancementTab getTab(Advancement var1) {
      while (☃.getParent() != null) {
         ☃ = ☃.getParent();
      }

      return this.tabs.get(☃);
   }
}
