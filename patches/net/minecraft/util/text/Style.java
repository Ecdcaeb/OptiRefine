package net.minecraft.util.text;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class Style {
   private Style parentStyle;
   private TextFormatting color;
   private Boolean bold;
   private Boolean italic;
   private Boolean underlined;
   private Boolean strikethrough;
   private Boolean obfuscated;
   private ClickEvent clickEvent;
   private HoverEvent hoverEvent;
   private String insertion;
   private static final Style ROOT = new Style() {
      @Nullable
      @Override
      public TextFormatting getColor() {
         return null;
      }

      @Override
      public boolean getBold() {
         return false;
      }

      @Override
      public boolean getItalic() {
         return false;
      }

      @Override
      public boolean getStrikethrough() {
         return false;
      }

      @Override
      public boolean getUnderlined() {
         return false;
      }

      @Override
      public boolean getObfuscated() {
         return false;
      }

      @Nullable
      @Override
      public ClickEvent getClickEvent() {
         return null;
      }

      @Nullable
      @Override
      public HoverEvent getHoverEvent() {
         return null;
      }

      @Nullable
      @Override
      public String getInsertion() {
         return null;
      }

      @Override
      public Style setColor(TextFormatting var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setBold(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setItalic(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setStrikethrough(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setUnderlined(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setObfuscated(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setClickEvent(ClickEvent var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setHoverEvent(HoverEvent var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Style setParentStyle(Style var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public String toString() {
         return "Style.ROOT";
      }

      @Override
      public Style createShallowCopy() {
         return this;
      }

      @Override
      public Style createDeepCopy() {
         return this;
      }

      @Override
      public String getFormattingCode() {
         return "";
      }
   };

   @Nullable
   public TextFormatting getColor() {
      return this.color == null ? this.getParent().getColor() : this.color;
   }

   public boolean getBold() {
      return this.bold == null ? this.getParent().getBold() : this.bold;
   }

   public boolean getItalic() {
      return this.italic == null ? this.getParent().getItalic() : this.italic;
   }

   public boolean getStrikethrough() {
      return this.strikethrough == null ? this.getParent().getStrikethrough() : this.strikethrough;
   }

   public boolean getUnderlined() {
      return this.underlined == null ? this.getParent().getUnderlined() : this.underlined;
   }

   public boolean getObfuscated() {
      return this.obfuscated == null ? this.getParent().getObfuscated() : this.obfuscated;
   }

   public boolean isEmpty() {
      return this.bold == null
         && this.italic == null
         && this.strikethrough == null
         && this.underlined == null
         && this.obfuscated == null
         && this.color == null
         && this.clickEvent == null
         && this.hoverEvent == null
         && this.insertion == null;
   }

   @Nullable
   public ClickEvent getClickEvent() {
      return this.clickEvent == null ? this.getParent().getClickEvent() : this.clickEvent;
   }

   @Nullable
   public HoverEvent getHoverEvent() {
      return this.hoverEvent == null ? this.getParent().getHoverEvent() : this.hoverEvent;
   }

   @Nullable
   public String getInsertion() {
      return this.insertion == null ? this.getParent().getInsertion() : this.insertion;
   }

   public Style setColor(TextFormatting var1) {
      this.color = ☃;
      return this;
   }

   public Style setBold(Boolean var1) {
      this.bold = ☃;
      return this;
   }

   public Style setItalic(Boolean var1) {
      this.italic = ☃;
      return this;
   }

   public Style setStrikethrough(Boolean var1) {
      this.strikethrough = ☃;
      return this;
   }

   public Style setUnderlined(Boolean var1) {
      this.underlined = ☃;
      return this;
   }

   public Style setObfuscated(Boolean var1) {
      this.obfuscated = ☃;
      return this;
   }

   public Style setClickEvent(ClickEvent var1) {
      this.clickEvent = ☃;
      return this;
   }

   public Style setHoverEvent(HoverEvent var1) {
      this.hoverEvent = ☃;
      return this;
   }

   public Style setInsertion(String var1) {
      this.insertion = ☃;
      return this;
   }

   public Style setParentStyle(Style var1) {
      this.parentStyle = ☃;
      return this;
   }

   public String getFormattingCode() {
      if (this.isEmpty()) {
         return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
      } else {
         StringBuilder ☃ = new StringBuilder();
         if (this.getColor() != null) {
            ☃.append(this.getColor());
         }

         if (this.getBold()) {
            ☃.append(TextFormatting.BOLD);
         }

         if (this.getItalic()) {
            ☃.append(TextFormatting.ITALIC);
         }

         if (this.getUnderlined()) {
            ☃.append(TextFormatting.UNDERLINE);
         }

         if (this.getObfuscated()) {
            ☃.append(TextFormatting.OBFUSCATED);
         }

         if (this.getStrikethrough()) {
            ☃.append(TextFormatting.STRIKETHROUGH);
         }

         return ☃.toString();
      }
   }

   private Style getParent() {
      return this.parentStyle == null ? ROOT : this.parentStyle;
   }

   @Override
   public String toString() {
      return "Style{hasParent="
         + (this.parentStyle != null)
         + ", color="
         + this.color
         + ", bold="
         + this.bold
         + ", italic="
         + this.italic
         + ", underlined="
         + this.underlined
         + ", obfuscated="
         + this.obfuscated
         + ", clickEvent="
         + this.getClickEvent()
         + ", hoverEvent="
         + this.getHoverEvent()
         + ", insertion="
         + this.getInsertion()
         + '}';
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof Style)) {
         return false;
      } else {
         Style ☃ = (Style)☃;
         return this.getBold() == ☃.getBold()
            && this.getColor() == ☃.getColor()
            && this.getItalic() == ☃.getItalic()
            && this.getObfuscated() == ☃.getObfuscated()
            && this.getStrikethrough() == ☃.getStrikethrough()
            && this.getUnderlined() == ☃.getUnderlined()
            && (this.getClickEvent() != null ? this.getClickEvent().equals(☃.getClickEvent()) : ☃.getClickEvent() == null)
            && (this.getHoverEvent() != null ? this.getHoverEvent().equals(☃.getHoverEvent()) : ☃.getHoverEvent() == null)
            && (this.getInsertion() != null ? this.getInsertion().equals(☃.getInsertion()) : ☃.getInsertion() == null);
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.color.hashCode();
      ☃ = 31 * ☃ + this.bold.hashCode();
      ☃ = 31 * ☃ + this.italic.hashCode();
      ☃ = 31 * ☃ + this.underlined.hashCode();
      ☃ = 31 * ☃ + this.strikethrough.hashCode();
      ☃ = 31 * ☃ + this.obfuscated.hashCode();
      ☃ = 31 * ☃ + this.clickEvent.hashCode();
      ☃ = 31 * ☃ + this.hoverEvent.hashCode();
      return 31 * ☃ + this.insertion.hashCode();
   }

   public Style createShallowCopy() {
      Style ☃ = new Style();
      ☃.bold = this.bold;
      ☃.italic = this.italic;
      ☃.strikethrough = this.strikethrough;
      ☃.underlined = this.underlined;
      ☃.obfuscated = this.obfuscated;
      ☃.color = this.color;
      ☃.clickEvent = this.clickEvent;
      ☃.hoverEvent = this.hoverEvent;
      ☃.parentStyle = this.parentStyle;
      ☃.insertion = this.insertion;
      return ☃;
   }

   public Style createDeepCopy() {
      Style ☃ = new Style();
      ☃.setBold(this.getBold());
      ☃.setItalic(this.getItalic());
      ☃.setStrikethrough(this.getStrikethrough());
      ☃.setUnderlined(this.getUnderlined());
      ☃.setObfuscated(this.getObfuscated());
      ☃.setColor(this.getColor());
      ☃.setClickEvent(this.getClickEvent());
      ☃.setHoverEvent(this.getHoverEvent());
      ☃.setInsertion(this.getInsertion());
      return ☃;
   }

   public static class Serializer implements JsonDeserializer<Style>, JsonSerializer<Style> {
      @Nullable
      public Style deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (☃.isJsonObject()) {
            Style ☃ = new Style();
            JsonObject ☃x = ☃.getAsJsonObject();
            if (☃x == null) {
               return null;
            } else {
               if (☃x.has("bold")) {
                  ☃.bold = ☃x.get("bold").getAsBoolean();
               }

               if (☃x.has("italic")) {
                  ☃.italic = ☃x.get("italic").getAsBoolean();
               }

               if (☃x.has("underlined")) {
                  ☃.underlined = ☃x.get("underlined").getAsBoolean();
               }

               if (☃x.has("strikethrough")) {
                  ☃.strikethrough = ☃x.get("strikethrough").getAsBoolean();
               }

               if (☃x.has("obfuscated")) {
                  ☃.obfuscated = ☃x.get("obfuscated").getAsBoolean();
               }

               if (☃x.has("color")) {
                  ☃.color = (TextFormatting)☃.deserialize(☃x.get("color"), TextFormatting.class);
               }

               if (☃x.has("insertion")) {
                  ☃.insertion = ☃x.get("insertion").getAsString();
               }

               if (☃x.has("clickEvent")) {
                  JsonObject ☃xx = ☃x.getAsJsonObject("clickEvent");
                  if (☃xx != null) {
                     JsonPrimitive ☃xxx = ☃xx.getAsJsonPrimitive("action");
                     ClickEvent.Action ☃xxxx = ☃xxx == null ? null : ClickEvent.Action.getValueByCanonicalName(☃xxx.getAsString());
                     JsonPrimitive ☃xxxxx = ☃xx.getAsJsonPrimitive("value");
                     String ☃xxxxxx = ☃xxxxx == null ? null : ☃xxxxx.getAsString();
                     if (☃xxxx != null && ☃xxxxxx != null && ☃xxxx.shouldAllowInChat()) {
                        ☃.clickEvent = new ClickEvent(☃xxxx, ☃xxxxxx);
                     }
                  }
               }

               if (☃x.has("hoverEvent")) {
                  JsonObject ☃xx = ☃x.getAsJsonObject("hoverEvent");
                  if (☃xx != null) {
                     JsonPrimitive ☃xxx = ☃xx.getAsJsonPrimitive("action");
                     HoverEvent.Action ☃xxxx = ☃xxx == null ? null : HoverEvent.Action.getValueByCanonicalName(☃xxx.getAsString());
                     ITextComponent ☃xxxxx = (ITextComponent)☃.deserialize(☃xx.get("value"), ITextComponent.class);
                     if (☃xxxx != null && ☃xxxxx != null && ☃xxxx.shouldAllowInChat()) {
                        ☃.hoverEvent = new HoverEvent(☃xxxx, ☃xxxxx);
                     }
                  }
               }

               return ☃;
            }
         } else {
            return null;
         }
      }

      @Nullable
      public JsonElement serialize(Style var1, Type var2, JsonSerializationContext var3) {
         if (☃.isEmpty()) {
            return null;
         } else {
            JsonObject ☃ = new JsonObject();
            if (☃.bold != null) {
               ☃.addProperty("bold", ☃.bold);
            }

            if (☃.italic != null) {
               ☃.addProperty("italic", ☃.italic);
            }

            if (☃.underlined != null) {
               ☃.addProperty("underlined", ☃.underlined);
            }

            if (☃.strikethrough != null) {
               ☃.addProperty("strikethrough", ☃.strikethrough);
            }

            if (☃.obfuscated != null) {
               ☃.addProperty("obfuscated", ☃.obfuscated);
            }

            if (☃.color != null) {
               ☃.add("color", ☃.serialize(☃.color));
            }

            if (☃.insertion != null) {
               ☃.add("insertion", ☃.serialize(☃.insertion));
            }

            if (☃.clickEvent != null) {
               JsonObject ☃x = new JsonObject();
               ☃x.addProperty("action", ☃.clickEvent.getAction().getCanonicalName());
               ☃x.addProperty("value", ☃.clickEvent.getValue());
               ☃.add("clickEvent", ☃x);
            }

            if (☃.hoverEvent != null) {
               JsonObject ☃x = new JsonObject();
               ☃x.addProperty("action", ☃.hoverEvent.getAction().getCanonicalName());
               ☃x.add("value", ☃.serialize(☃.hoverEvent.getValue()));
               ☃.add("hoverEvent", ☃x);
            }

            return ☃;
         }
      }
   }
}
