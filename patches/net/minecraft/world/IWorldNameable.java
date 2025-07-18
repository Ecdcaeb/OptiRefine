package net.minecraft.world;

import net.minecraft.util.text.ITextComponent;

public interface IWorldNameable {
   String getName();

   boolean hasCustomName();

   ITextComponent getDisplayName();
}
