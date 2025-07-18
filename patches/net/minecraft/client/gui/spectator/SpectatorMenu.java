package net.minecraft.client.gui.spectator;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class SpectatorMenu {
   private static final ISpectatorMenuObject CLOSE_ITEM = new SpectatorMenu.EndSpectatorObject();
   private static final ISpectatorMenuObject SCROLL_LEFT = new SpectatorMenu.MoveMenuObject(-1, true);
   private static final ISpectatorMenuObject SCROLL_RIGHT_ENABLED = new SpectatorMenu.MoveMenuObject(1, true);
   private static final ISpectatorMenuObject SCROLL_RIGHT_DISABLED = new SpectatorMenu.MoveMenuObject(1, false);
   public static final ISpectatorMenuObject EMPTY_SLOT = new ISpectatorMenuObject() {
      @Override
      public void selectItem(SpectatorMenu var1) {
      }

      @Override
      public ITextComponent getSpectatorName() {
         return new TextComponentString("");
      }

      @Override
      public void renderIcon(float var1, int var2) {
      }

      @Override
      public boolean isEnabled() {
         return false;
      }
   };
   private final ISpectatorMenuRecipient listener;
   private final List<SpectatorDetails> previousCategories = Lists.newArrayList();
   private ISpectatorMenuView category;
   private int selectedSlot = -1;
   private int page;

   public SpectatorMenu(ISpectatorMenuRecipient var1) {
      this.category = new BaseSpectatorGroup();
      this.listener = ☃;
   }

   public ISpectatorMenuObject getItem(int var1) {
      int ☃ = ☃ + this.page * 6;
      if (this.page > 0 && ☃ == 0) {
         return SCROLL_LEFT;
      } else if (☃ == 7) {
         return ☃ < this.category.getItems().size() ? SCROLL_RIGHT_ENABLED : SCROLL_RIGHT_DISABLED;
      } else if (☃ == 8) {
         return CLOSE_ITEM;
      } else {
         return ☃ >= 0 && ☃ < this.category.getItems().size()
            ? (ISpectatorMenuObject)MoreObjects.firstNonNull(this.category.getItems().get(☃), EMPTY_SLOT)
            : EMPTY_SLOT;
      }
   }

   public List<ISpectatorMenuObject> getItems() {
      List<ISpectatorMenuObject> ☃ = Lists.newArrayList();

      for (int ☃x = 0; ☃x <= 8; ☃x++) {
         ☃.add(this.getItem(☃x));
      }

      return ☃;
   }

   public ISpectatorMenuObject getSelectedItem() {
      return this.getItem(this.selectedSlot);
   }

   public ISpectatorMenuView getSelectedCategory() {
      return this.category;
   }

   public void selectSlot(int var1) {
      ISpectatorMenuObject ☃ = this.getItem(☃);
      if (☃ != EMPTY_SLOT) {
         if (this.selectedSlot == ☃ && ☃.isEnabled()) {
            ☃.selectItem(this);
         } else {
            this.selectedSlot = ☃;
         }
      }
   }

   public void exit() {
      this.listener.onSpectatorMenuClosed(this);
   }

   public int getSelectedSlot() {
      return this.selectedSlot;
   }

   public void selectCategory(ISpectatorMenuView var1) {
      this.previousCategories.add(this.getCurrentPage());
      this.category = ☃;
      this.selectedSlot = -1;
      this.page = 0;
   }

   public SpectatorDetails getCurrentPage() {
      return new SpectatorDetails(this.category, this.getItems(), this.selectedSlot);
   }

   static class EndSpectatorObject implements ISpectatorMenuObject {
      private EndSpectatorObject() {
      }

      @Override
      public void selectItem(SpectatorMenu var1) {
         ☃.exit();
      }

      @Override
      public ITextComponent getSpectatorName() {
         return new TextComponentTranslation("spectatorMenu.close");
      }

      @Override
      public void renderIcon(float var1, int var2) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
         Gui.drawModalRectWithCustomSizedTexture(0, 0, 128.0F, 0.0F, 16, 16, 256.0F, 256.0F);
      }

      @Override
      public boolean isEnabled() {
         return true;
      }
   }

   static class MoveMenuObject implements ISpectatorMenuObject {
      private final int direction;
      private final boolean enabled;

      public MoveMenuObject(int var1, boolean var2) {
         this.direction = ☃;
         this.enabled = ☃;
      }

      @Override
      public void selectItem(SpectatorMenu var1) {
         ☃.page = ☃.page + this.direction;
      }

      @Override
      public ITextComponent getSpectatorName() {
         return this.direction < 0 ? new TextComponentTranslation("spectatorMenu.previous_page") : new TextComponentTranslation("spectatorMenu.next_page");
      }

      @Override
      public void renderIcon(float var1, int var2) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
         if (this.direction < 0) {
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 144.0F, 0.0F, 16, 16, 256.0F, 256.0F);
         } else {
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 160.0F, 0.0F, 16, 16, 256.0F, 256.0F);
         }
      }

      @Override
      public boolean isEnabled() {
         return this.enabled;
      }
   }
}
