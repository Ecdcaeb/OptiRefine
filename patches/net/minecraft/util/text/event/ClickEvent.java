package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent {
   private final ClickEvent.Action action;
   private final String value;

   public ClickEvent(ClickEvent.Action var1, String var2) {
      this.action = ☃;
      this.value = ☃;
   }

   public ClickEvent.Action getAction() {
      return this.action;
   }

   public String getValue() {
      return this.value;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ != null && this.getClass() == ☃.getClass()) {
         ClickEvent ☃ = (ClickEvent)☃;
         if (this.action != ☃.action) {
            return false;
         } else {
            return this.value != null ? this.value.equals(☃.value) : ☃.value == null;
         }
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
   }

   @Override
   public int hashCode() {
      int ☃ = this.action.hashCode();
      return 31 * ☃ + (this.value != null ? this.value.hashCode() : 0);
   }

   public static enum Action {
      OPEN_URL("open_url", true),
      OPEN_FILE("open_file", false),
      RUN_COMMAND("run_command", true),
      SUGGEST_COMMAND("suggest_command", true),
      CHANGE_PAGE("change_page", true);

      private static final Map<String, ClickEvent.Action> NAME_MAPPING = Maps.newHashMap();
      private final boolean allowedInChat;
      private final String canonicalName;

      private Action(String var3, boolean var4) {
         this.canonicalName = ☃;
         this.allowedInChat = ☃;
      }

      public boolean shouldAllowInChat() {
         return this.allowedInChat;
      }

      public String getCanonicalName() {
         return this.canonicalName;
      }

      public static ClickEvent.Action getValueByCanonicalName(String var0) {
         return NAME_MAPPING.get(☃);
      }

      static {
         for (ClickEvent.Action ☃ : values()) {
            NAME_MAPPING.put(☃.getCanonicalName(), ☃);
         }
      }
   }
}
