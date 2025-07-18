package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.text.TextFormatting;

public abstract class Team {
   public boolean isSameTeam(@Nullable Team var1) {
      return ☃ == null ? false : this == ☃;
   }

   public abstract String getName();

   public abstract String formatString(String var1);

   public abstract boolean getSeeFriendlyInvisiblesEnabled();

   public abstract boolean getAllowFriendlyFire();

   public abstract Team.EnumVisible getNameTagVisibility();

   public abstract TextFormatting getColor();

   public abstract Collection<String> getMembershipCollection();

   public abstract Team.EnumVisible getDeathMessageVisibility();

   public abstract Team.CollisionRule getCollisionRule();

   public static enum CollisionRule {
      ALWAYS("always", 0),
      NEVER("never", 1),
      HIDE_FOR_OTHER_TEAMS("pushOtherTeams", 2),
      HIDE_FOR_OWN_TEAM("pushOwnTeam", 3);

      private static final Map<String, Team.CollisionRule> nameMap = Maps.newHashMap();
      public final String name;
      public final int id;

      public static String[] getNames() {
         return nameMap.keySet().toArray(new String[nameMap.size()]);
      }

      @Nullable
      public static Team.CollisionRule getByName(String var0) {
         return nameMap.get(☃);
      }

      private CollisionRule(String var3, int var4) {
         this.name = ☃;
         this.id = ☃;
      }

      static {
         for (Team.CollisionRule ☃ : values()) {
            nameMap.put(☃.name, ☃);
         }
      }
   }

   public static enum EnumVisible {
      ALWAYS("always", 0),
      NEVER("never", 1),
      HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
      HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

      private static final Map<String, Team.EnumVisible> nameMap = Maps.newHashMap();
      public final String internalName;
      public final int id;

      public static String[] getNames() {
         return nameMap.keySet().toArray(new String[nameMap.size()]);
      }

      @Nullable
      public static Team.EnumVisible getByName(String var0) {
         return nameMap.get(☃);
      }

      private EnumVisible(String var3, int var4) {
         this.internalName = ☃;
         this.id = ☃;
      }

      static {
         for (Team.EnumVisible ☃ : values()) {
            nameMap.put(☃.internalName, ☃);
         }
      }
   }
}
