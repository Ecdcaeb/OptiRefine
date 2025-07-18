package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum HorseArmorType {
   NONE(0),
   IRON(5, "iron", "meo"),
   GOLD(7, "gold", "goo"),
   DIAMOND(11, "diamond", "dio");

   private final String textureName;
   private final String hash;
   private final int protection;

   private HorseArmorType(int var3) {
      this.protection = ☃;
      this.textureName = null;
      this.hash = "";
   }

   private HorseArmorType(int var3, String var4, String var5) {
      this.protection = ☃;
      this.textureName = "textures/entity/horse/armor/horse_armor_" + ☃ + ".png";
      this.hash = ☃;
   }

   public int getOrdinal() {
      return this.ordinal();
   }

   public String getHash() {
      return this.hash;
   }

   public int getProtection() {
      return this.protection;
   }

   @Nullable
   public String getTextureName() {
      return this.textureName;
   }

   public static HorseArmorType getByOrdinal(int var0) {
      return values()[☃];
   }

   public static HorseArmorType getByItemStack(ItemStack var0) {
      return ☃.isEmpty() ? NONE : getByItem(☃.getItem());
   }

   public static HorseArmorType getByItem(Item var0) {
      if (☃ == Items.IRON_HORSE_ARMOR) {
         return IRON;
      } else if (☃ == Items.GOLDEN_HORSE_ARMOR) {
         return GOLD;
      } else {
         return ☃ == Items.DIAMOND_HORSE_ARMOR ? DIAMOND : NONE;
      }
   }

   public static boolean isHorseArmor(Item var0) {
      return getByItem(☃) != NONE;
   }
}
