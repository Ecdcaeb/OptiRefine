package net.minecraft.tileentity;

import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDropper extends TileEntityDispenser {
   public static void registerFixesDropper(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDropper.class, "Items"));
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.dropper";
   }

   @Override
   public String getGuiID() {
      return "minecraft:dropper";
   }
}
