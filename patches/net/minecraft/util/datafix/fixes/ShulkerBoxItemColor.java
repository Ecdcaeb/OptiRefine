package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxItemColor implements IFixableData {
   public static final String[] NAMES_BY_COLOR = new String[]{
      "minecraft:white_shulker_box",
      "minecraft:orange_shulker_box",
      "minecraft:magenta_shulker_box",
      "minecraft:light_blue_shulker_box",
      "minecraft:yellow_shulker_box",
      "minecraft:lime_shulker_box",
      "minecraft:pink_shulker_box",
      "minecraft:gray_shulker_box",
      "minecraft:silver_shulker_box",
      "minecraft:cyan_shulker_box",
      "minecraft:purple_shulker_box",
      "minecraft:blue_shulker_box",
      "minecraft:brown_shulker_box",
      "minecraft:green_shulker_box",
      "minecraft:red_shulker_box",
      "minecraft:black_shulker_box"
   };

   @Override
   public int getFixVersion() {
      return 813;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("minecraft:shulker_box".equals(☃.getString("id")) && ☃.hasKey("tag", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("tag");
         if (☃.hasKey("BlockEntityTag", 10)) {
            NBTTagCompound ☃x = ☃.getCompoundTag("BlockEntityTag");
            if (☃x.getTagList("Items", 10).isEmpty()) {
               ☃x.removeTag("Items");
            }

            int ☃xx = ☃x.getInteger("Color");
            ☃x.removeTag("Color");
            if (☃x.isEmpty()) {
               ☃.removeTag("BlockEntityTag");
            }

            if (☃.isEmpty()) {
               ☃.removeTag("tag");
            }

            ☃.setString("id", NAMES_BY_COLOR[☃xx % 16]);
         }
      }

      return ☃;
   }
}
