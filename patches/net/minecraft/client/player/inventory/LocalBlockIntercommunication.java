package net.minecraft.client.player.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;

public class LocalBlockIntercommunication implements IInteractionObject {
   private final String guiID;
   private final ITextComponent displayName;

   public LocalBlockIntercommunication(String var1, ITextComponent var2) {
      this.guiID = ☃;
      this.displayName = ☃;
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      throw new UnsupportedOperationException();
   }

   @Override
   public String getName() {
      return this.displayName.getUnformattedText();
   }

   @Override
   public boolean hasCustomName() {
      return true;
   }

   @Override
   public String getGuiID() {
      return this.guiID;
   }

   @Override
   public ITextComponent getDisplayName() {
      return this.displayName;
   }
}
