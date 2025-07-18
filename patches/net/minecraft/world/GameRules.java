package net.minecraft.world;

import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules {
   private final TreeMap<String, GameRules.Value> rules = new TreeMap<>();

   public GameRules() {
      this.addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doEntityDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
      this.addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("spectatorsGenerateChunks", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("spawnRadius", "10", GameRules.ValueType.NUMERICAL_VALUE);
      this.addGameRule("disableElytraMovementCheck", "false", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("maxEntityCramming", "24", GameRules.ValueType.NUMERICAL_VALUE);
      this.addGameRule("doWeatherCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doLimitedCrafting", "false", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("maxCommandChainLength", "65536", GameRules.ValueType.NUMERICAL_VALUE);
      this.addGameRule("announceAdvancements", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("gameLoopFunction", "-", GameRules.ValueType.FUNCTION);
   }

   public void addGameRule(String var1, String var2, GameRules.ValueType var3) {
      this.rules.put(☃, new GameRules.Value(☃, ☃));
   }

   public void setOrCreateGameRule(String var1, String var2) {
      GameRules.Value ☃ = this.rules.get(☃);
      if (☃ != null) {
         ☃.setValue(☃);
      } else {
         this.addGameRule(☃, ☃, GameRules.ValueType.ANY_VALUE);
      }
   }

   public String getString(String var1) {
      GameRules.Value ☃ = this.rules.get(☃);
      return ☃ != null ? ☃.getString() : "";
   }

   public boolean getBoolean(String var1) {
      GameRules.Value ☃ = this.rules.get(☃);
      return ☃ != null ? ☃.getBoolean() : false;
   }

   public int getInt(String var1) {
      GameRules.Value ☃ = this.rules.get(☃);
      return ☃ != null ? ☃.getInt() : 0;
   }

   public NBTTagCompound writeToNBT() {
      NBTTagCompound ☃ = new NBTTagCompound();

      for (String ☃x : this.rules.keySet()) {
         GameRules.Value ☃xx = this.rules.get(☃x);
         ☃.setString(☃x, ☃xx.getString());
      }

      return ☃;
   }

   public void readFromNBT(NBTTagCompound var1) {
      for (String ☃ : ☃.getKeySet()) {
         this.setOrCreateGameRule(☃, ☃.getString(☃));
      }
   }

   public String[] getRules() {
      Set<String> ☃ = this.rules.keySet();
      return ☃.toArray(new String[☃.size()]);
   }

   public boolean hasRule(String var1) {
      return this.rules.containsKey(☃);
   }

   public boolean areSameType(String var1, GameRules.ValueType var2) {
      GameRules.Value ☃ = this.rules.get(☃);
      return ☃ != null && (☃.getType() == ☃ || ☃ == GameRules.ValueType.ANY_VALUE);
   }

   static class Value {
      private String valueString;
      private boolean valueBoolean;
      private int valueInteger;
      private double valueDouble;
      private final GameRules.ValueType type;

      public Value(String var1, GameRules.ValueType var2) {
         this.type = ☃;
         this.setValue(☃);
      }

      public void setValue(String var1) {
         this.valueString = ☃;
         this.valueBoolean = Boolean.parseBoolean(☃);
         this.valueInteger = this.valueBoolean ? 1 : 0;

         try {
            this.valueInteger = Integer.parseInt(☃);
         } catch (NumberFormatException var4) {
         }

         try {
            this.valueDouble = Double.parseDouble(☃);
         } catch (NumberFormatException var3) {
         }
      }

      public String getString() {
         return this.valueString;
      }

      public boolean getBoolean() {
         return this.valueBoolean;
      }

      public int getInt() {
         return this.valueInteger;
      }

      public GameRules.ValueType getType() {
         return this.type;
      }
   }

   public static enum ValueType {
      ANY_VALUE,
      BOOLEAN_VALUE,
      NUMERICAL_VALUE,
      FUNCTION;
   }
}
