package net.minecraft.client.gui.spectator;

import net.minecraft.util.text.ITextComponent;

public interface ISpectatorMenuObject {
   void selectItem(SpectatorMenu var1);

   ITextComponent getSpectatorName();

   void renderIcon(float var1, int var2);

   boolean isEnabled();
}
