package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;

public enum GameType {
   NOT_SET(-1, "", ""),
   SURVIVAL(0, "survival", "s"),
   CREATIVE(1, "creative", "c"),
   ADVENTURE(2, "adventure", "a"),
   SPECTATOR(3, "spectator", "sp");

   int id;
   String name;
   String shortName;

   private GameType(int var3, String var4, String var5) {
      this.id = ☃;
      this.name = ☃;
      this.shortName = ☃;
   }

   public int getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public void configurePlayerCapabilities(PlayerCapabilities var1) {
      if (this == CREATIVE) {
         ☃.allowFlying = true;
         ☃.isCreativeMode = true;
         ☃.disableDamage = true;
      } else if (this == SPECTATOR) {
         ☃.allowFlying = true;
         ☃.isCreativeMode = false;
         ☃.disableDamage = true;
         ☃.isFlying = true;
      } else {
         ☃.allowFlying = false;
         ☃.isCreativeMode = false;
         ☃.disableDamage = false;
         ☃.isFlying = false;
      }

      ☃.allowEdit = !this.hasLimitedInteractions();
   }

   public boolean hasLimitedInteractions() {
      return this == ADVENTURE || this == SPECTATOR;
   }

   public boolean isCreative() {
      return this == CREATIVE;
   }

   public boolean isSurvivalOrAdventure() {
      return this == SURVIVAL || this == ADVENTURE;
   }

   public static GameType getByID(int var0) {
      return parseGameTypeWithDefault(☃, SURVIVAL);
   }

   public static GameType parseGameTypeWithDefault(int var0, GameType var1) {
      for (GameType ☃ : values()) {
         if (☃.id == ☃) {
            return ☃;
         }
      }

      return ☃;
   }

   public static GameType getByName(String var0) {
      return parseGameTypeWithDefault(☃, SURVIVAL);
   }

   public static GameType parseGameTypeWithDefault(String var0, GameType var1) {
      for (GameType ☃ : values()) {
         if (☃.name.equals(☃) || ☃.shortName.equals(☃)) {
            return ☃;
         }
      }

      return ☃;
   }
}
