package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.text.ITextComponent;

public class HoverEvent {
   private final HoverEvent.Action action;
   private final ITextComponent value;

   public HoverEvent(HoverEvent.Action var1, ITextComponent var2) {
      this.action = ☃;
      this.value = ☃;
   }

   public HoverEvent.Action getAction() {
      return this.action;
   }

   public ITextComponent getValue() {
      return this.value;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ != null && this.getClass() == ☃.getClass()) {
         HoverEvent ☃ = (HoverEvent)☃;
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
      return "HoverEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
   }

   @Override
   public int hashCode() {
      int ☃ = this.action.hashCode();
      return 31 * ☃ + (this.value != null ? this.value.hashCode() : 0);
   }

   public static enum Action {
      SHOW_TEXT("show_text", true),
      SHOW_ITEM("show_item", true),
      SHOW_ENTITY("show_entity", true);

      private static final Map<String, HoverEvent.Action> NAME_MAPPING = Maps.newHashMap();
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

      public static HoverEvent.Action getValueByCanonicalName(String var0) {
         return NAME_MAPPING.get(☃);
      }

      static {
         for (HoverEvent.Action ☃ : values()) {
            NAME_MAPPING.put(☃.getCanonicalName(), ☃);
         }
      }
   }
}
