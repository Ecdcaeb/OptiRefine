package net.minecraft.util.datafix.walkers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTag implements IDataWalker {
   private static final Logger LOGGER = LogManager.getLogger();

   @Override
   public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
      NBTTagCompound ☃ = ☃.getCompoundTag("tag");
      if (☃.hasKey("EntityTag", 10)) {
         NBTTagCompound ☃x = ☃.getCompoundTag("EntityTag");
         String ☃xx = ☃.getString("id");
         String ☃xxx;
         if ("minecraft:armor_stand".equals(☃xx)) {
            ☃xxx = ☃ < 515 ? "ArmorStand" : "minecraft:armor_stand";
         } else {
            if (!"minecraft:spawn_egg".equals(☃xx)) {
               return ☃;
            }

            ☃xxx = ☃x.getString("id");
         }

         boolean ☃xxxx;
         if (☃xxx == null) {
            LOGGER.warn("Unable to resolve Entity for ItemInstance: {}", ☃xx);
            ☃xxxx = false;
         } else {
            ☃xxxx = !☃x.hasKey("id", 8);
            ☃x.setString("id", ☃xxx);
         }

         ☃.process(FixTypes.ENTITY, ☃x, ☃);
         if (☃xxxx) {
            ☃x.removeTag("id");
         }
      }

      return ☃;
   }
}
