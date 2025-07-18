package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry {
   private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
   private static final ITextComponent INCOMPATIBLE = new TextComponentTranslation("resourcePack.incompatible");
   private static final ITextComponent INCOMPATIBLE_OLD = new TextComponentTranslation("resourcePack.incompatible.old");
   private static final ITextComponent INCOMPATIBLE_NEW = new TextComponentTranslation("resourcePack.incompatible.new");
   protected final Minecraft mc;
   protected final GuiScreenResourcePacks resourcePacksGUI;

   public ResourcePackListEntry(GuiScreenResourcePacks var1) {
      this.resourcePacksGUI = ☃;
      this.mc = Minecraft.getMinecraft();
   }

   @Override
   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
      int ☃ = this.getResourcePackFormat();
      if (☃ != 3) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         Gui.drawRect(☃ - 1, ☃ - 1, ☃ + ☃ - 9, ☃ + ☃ + 1, -8978432);
      }

      this.bindResourcePackIcon();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
      String ☃x = this.getResourcePackName();
      String ☃xx = this.getResourcePackDescription();
      if (this.showHoverOverlay() && (this.mc.gameSettings.touchscreen || ☃)) {
         this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
         Gui.drawRect(☃, ☃, ☃ + 32, ☃ + 32, -1601138544);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         int ☃xxx = ☃ - ☃;
         int ☃xxxx = ☃ - ☃;
         if (☃ < 3) {
            ☃x = INCOMPATIBLE.getFormattedText();
            ☃xx = INCOMPATIBLE_OLD.getFormattedText();
         } else if (☃ > 3) {
            ☃x = INCOMPATIBLE.getFormattedText();
            ☃xx = INCOMPATIBLE_NEW.getFormattedText();
         }

         if (this.canMoveRight()) {
            if (☃xxx < 32) {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
            } else {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
            }
         } else {
            if (this.canMoveLeft()) {
               if (☃xxx < 16) {
                  Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
               } else {
                  Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
               }
            }

            if (this.canMoveUp()) {
               if (☃xxx < 32 && ☃xxx > 16 && ☃xxxx < 16) {
                  Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
               } else {
                  Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
               }
            }

            if (this.canMoveDown()) {
               if (☃xxx < 32 && ☃xxx > 16 && ☃xxxx > 16) {
                  Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
               } else {
                  Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
               }
            }
         }
      }

      int ☃xxxxx = this.mc.fontRenderer.getStringWidth(☃x);
      if (☃xxxxx > 157) {
         ☃x = this.mc.fontRenderer.trimStringToWidth(☃x, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
      }

      this.mc.fontRenderer.drawStringWithShadow(☃x, ☃ + 32 + 2, ☃ + 1, 16777215);
      List<String> ☃xxxxxx = this.mc.fontRenderer.listFormattedStringToWidth(☃xx, 157);

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < 2 && ☃xxxxxxx < ☃xxxxxx.size(); ☃xxxxxxx++) {
         this.mc.fontRenderer.drawStringWithShadow(☃xxxxxx.get(☃xxxxxxx), ☃ + 32 + 2, ☃ + 12 + 10 * ☃xxxxxxx, 8421504);
      }
   }

   protected abstract int getResourcePackFormat();

   protected abstract String getResourcePackDescription();

   protected abstract String getResourcePackName();

   protected abstract void bindResourcePackIcon();

   protected boolean showHoverOverlay() {
      return true;
   }

   protected boolean canMoveRight() {
      return !this.resourcePacksGUI.hasResourcePackEntry(this);
   }

   protected boolean canMoveLeft() {
      return this.resourcePacksGUI.hasResourcePackEntry(this);
   }

   protected boolean canMoveUp() {
      List<ResourcePackListEntry> ☃ = this.resourcePacksGUI.getListContaining(this);
      int ☃x = ☃.indexOf(this);
      return ☃x > 0 && ☃.get(☃x - 1).showHoverOverlay();
   }

   protected boolean canMoveDown() {
      List<ResourcePackListEntry> ☃ = this.resourcePacksGUI.getListContaining(this);
      int ☃x = ☃.indexOf(this);
      return ☃x >= 0 && ☃x < ☃.size() - 1 && ☃.get(☃x + 1).showHoverOverlay();
   }

   @Override
   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (this.showHoverOverlay() && ☃ <= 32) {
         if (this.canMoveRight()) {
            this.resourcePacksGUI.markChanged();
            final int ☃ = this.resourcePacksGUI.getSelectedResourcePacks().get(0).isServerPack() ? 1 : 0;
            int ☃x = this.getResourcePackFormat();
            if (☃x == 3) {
               this.resourcePacksGUI.getListContaining(this).remove(this);
               this.resourcePacksGUI.getSelectedResourcePacks().add(☃, this);
            } else {
               String ☃xx = I18n.format("resourcePack.incompatible.confirm.title");
               String ☃xxx = I18n.format("resourcePack.incompatible.confirm." + (☃x > 3 ? "new" : "old"));
               this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                  @Override
                  public void confirmClicked(boolean var1, int var2x) {
                     List<ResourcePackListEntry> ☃xxxx = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
                     ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);
                     if (☃) {
                        ☃xxxx.remove(ResourcePackListEntry.this);
                        ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(☃, ResourcePackListEntry.this);
                     }
                  }
               }, ☃xx, ☃xxx, 0));
            }

            return true;
         }

         if (☃ < 16 && this.canMoveLeft()) {
            this.resourcePacksGUI.getListContaining(this).remove(this);
            this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
            this.resourcePacksGUI.markChanged();
            return true;
         }

         if (☃ > 16 && ☃ < 16 && this.canMoveUp()) {
            List<ResourcePackListEntry> ☃ = this.resourcePacksGUI.getListContaining(this);
            int ☃x = ☃.indexOf(this);
            ☃.remove(this);
            ☃.add(☃x - 1, this);
            this.resourcePacksGUI.markChanged();
            return true;
         }

         if (☃ > 16 && ☃ > 16 && this.canMoveDown()) {
            List<ResourcePackListEntry> ☃ = this.resourcePacksGUI.getListContaining(this);
            int ☃x = ☃.indexOf(this);
            ☃.remove(this);
            ☃.add(☃x + 1, this);
            this.resourcePacksGUI.markChanged();
            return true;
         }
      }

      return false;
   }

   @Override
   public void updatePosition(int var1, int var2, int var3, float var4) {
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public boolean isServerPack() {
      return false;
   }
}
