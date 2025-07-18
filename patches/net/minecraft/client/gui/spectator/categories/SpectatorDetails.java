package net.minecraft.client.gui.spectator.categories;

import com.google.common.base.MoreObjects;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;

public class SpectatorDetails {
   private final ISpectatorMenuView category;
   private final List<ISpectatorMenuObject> items;
   private final int selectedSlot;

   public SpectatorDetails(ISpectatorMenuView var1, List<ISpectatorMenuObject> var2, int var3) {
      this.category = ☃;
      this.items = ☃;
      this.selectedSlot = ☃;
   }

   public ISpectatorMenuObject getObject(int var1) {
      return ☃ >= 0 && ☃ < this.items.size()
         ? (ISpectatorMenuObject)MoreObjects.firstNonNull(this.items.get(☃), SpectatorMenu.EMPTY_SLOT)
         : SpectatorMenu.EMPTY_SLOT;
   }

   public int getSelectedSlot() {
      return this.selectedSlot;
   }
}
