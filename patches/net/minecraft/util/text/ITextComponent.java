package net.minecraft.util.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;

public interface ITextComponent extends Iterable<ITextComponent> {
   ITextComponent setStyle(Style var1);

   Style getStyle();

   ITextComponent appendText(String var1);

   ITextComponent appendSibling(ITextComponent var1);

   String getUnformattedComponentText();

   String getUnformattedText();

   String getFormattedText();

   List<ITextComponent> getSiblings();

   ITextComponent createCopy();

   public static class Serializer implements JsonDeserializer<ITextComponent>, JsonSerializer<ITextComponent> {
      private static final Gson GSON;

      public ITextComponent deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (☃.isJsonPrimitive()) {
            return new TextComponentString(☃.getAsString());
         } else if (!☃.isJsonObject()) {
            if (☃.isJsonArray()) {
               JsonArray ☃ = ☃.getAsJsonArray();
               ITextComponent ☃x = null;

               for (JsonElement ☃xx : ☃) {
                  ITextComponent ☃xxx = this.deserialize(☃xx, ☃xx.getClass(), ☃);
                  if (☃x == null) {
                     ☃x = ☃xxx;
                  } else {
                     ☃x.appendSibling(☃xxx);
                  }
               }

               return ☃x;
            } else {
               throw new JsonParseException("Don't know how to turn " + ☃ + " into a Component");
            }
         } else {
            JsonObject ☃ = ☃.getAsJsonObject();
            ITextComponent ☃x;
            if (☃.has("text")) {
               ☃x = new TextComponentString(☃.get("text").getAsString());
            } else if (☃.has("translate")) {
               String ☃xxx = ☃.get("translate").getAsString();
               if (☃.has("with")) {
                  JsonArray ☃xxxx = ☃.getAsJsonArray("with");
                  Object[] ☃xxxxx = new Object[☃xxxx.size()];

                  for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx.length; ☃xxxxxx++) {
                     ☃xxxxx[☃xxxxxx] = this.deserialize(☃xxxx.get(☃xxxxxx), ☃, ☃);
                     if (☃xxxxx[☃xxxxxx] instanceof TextComponentString) {
                        TextComponentString ☃xxxxxxx = (TextComponentString)☃xxxxx[☃xxxxxx];
                        if (☃xxxxxxx.getStyle().isEmpty() && ☃xxxxxxx.getSiblings().isEmpty()) {
                           ☃xxxxx[☃xxxxxx] = ☃xxxxxxx.getText();
                        }
                     }
                  }

                  ☃x = new TextComponentTranslation(☃xxx, ☃xxxxx);
               } else {
                  ☃x = new TextComponentTranslation(☃xxx);
               }
            } else if (☃.has("score")) {
               JsonObject ☃xxx = ☃.getAsJsonObject("score");
               if (!☃xxx.has("name") || !☃xxx.has("objective")) {
                  throw new JsonParseException("A score component needs a least a name and an objective");
               }

               ☃x = new TextComponentScore(JsonUtils.getString(☃xxx, "name"), JsonUtils.getString(☃xxx, "objective"));
               if (☃xxx.has("value")) {
                  ((TextComponentScore)☃x).setValue(JsonUtils.getString(☃xxx, "value"));
               }
            } else if (☃.has("selector")) {
               ☃x = new TextComponentSelector(JsonUtils.getString(☃, "selector"));
            } else {
               if (!☃.has("keybind")) {
                  throw new JsonParseException("Don't know how to turn " + ☃ + " into a Component");
               }

               ☃x = new TextComponentKeybind(JsonUtils.getString(☃, "keybind"));
            }

            if (☃.has("extra")) {
               JsonArray ☃xxxx = ☃.getAsJsonArray("extra");
               if (☃xxxx.size() <= 0) {
                  throw new JsonParseException("Unexpected empty array of components");
               }

               for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.size(); ☃xxxxx++) {
                  ☃x.appendSibling(this.deserialize(☃xxxx.get(☃xxxxx), ☃, ☃));
               }
            }

            ☃x.setStyle((Style)☃.deserialize(☃, Style.class));
            return ☃x;
         }
      }

      private void serializeChatStyle(Style var1, JsonObject var2, JsonSerializationContext var3) {
         JsonElement ☃ = ☃.serialize(☃);
         if (☃.isJsonObject()) {
            JsonObject ☃x = (JsonObject)☃;

            for (Entry<String, JsonElement> ☃xx : ☃x.entrySet()) {
               ☃.add(☃xx.getKey(), ☃xx.getValue());
            }
         }
      }

      public JsonElement serialize(ITextComponent var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         if (!☃.getStyle().isEmpty()) {
            this.serializeChatStyle(☃.getStyle(), ☃, ☃);
         }

         if (!☃.getSiblings().isEmpty()) {
            JsonArray ☃x = new JsonArray();

            for (ITextComponent ☃xx : ☃.getSiblings()) {
               ☃x.add(this.serialize(☃xx, ☃xx.getClass(), ☃));
            }

            ☃.add("extra", ☃x);
         }

         if (☃ instanceof TextComponentString) {
            ☃.addProperty("text", ((TextComponentString)☃).getText());
         } else if (☃ instanceof TextComponentTranslation) {
            TextComponentTranslation ☃x = (TextComponentTranslation)☃;
            ☃.addProperty("translate", ☃x.getKey());
            if (☃x.getFormatArgs() != null && ☃x.getFormatArgs().length > 0) {
               JsonArray ☃xx = new JsonArray();

               for (Object ☃xxx : ☃x.getFormatArgs()) {
                  if (☃xxx instanceof ITextComponent) {
                     ☃xx.add(this.serialize((ITextComponent)☃xxx, ☃xxx.getClass(), ☃));
                  } else {
                     ☃xx.add(new JsonPrimitive(String.valueOf(☃xxx)));
                  }
               }

               ☃.add("with", ☃xx);
            }
         } else if (☃ instanceof TextComponentScore) {
            TextComponentScore ☃x = (TextComponentScore)☃;
            JsonObject ☃xx = new JsonObject();
            ☃xx.addProperty("name", ☃x.getName());
            ☃xx.addProperty("objective", ☃x.getObjective());
            ☃xx.addProperty("value", ☃x.getUnformattedComponentText());
            ☃.add("score", ☃xx);
         } else if (☃ instanceof TextComponentSelector) {
            TextComponentSelector ☃x = (TextComponentSelector)☃;
            ☃.addProperty("selector", ☃x.getSelector());
         } else {
            if (!(☃ instanceof TextComponentKeybind)) {
               throw new IllegalArgumentException("Don't know how to serialize " + ☃ + " as a Component");
            }

            TextComponentKeybind ☃x = (TextComponentKeybind)☃;
            ☃.addProperty("keybind", ☃x.getKeybind());
         }

         return ☃;
      }

      public static String componentToJson(ITextComponent var0) {
         return GSON.toJson(☃);
      }

      @Nullable
      public static ITextComponent jsonToComponent(String var0) {
         return JsonUtils.gsonDeserialize(GSON, ☃, ITextComponent.class, false);
      }

      @Nullable
      public static ITextComponent fromJsonLenient(String var0) {
         return JsonUtils.gsonDeserialize(GSON, ☃, ITextComponent.class, true);
      }

      static {
         GsonBuilder ☃ = new GsonBuilder();
         ☃.registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer());
         ☃.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
         ☃.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
         GSON = ☃.create();
      }
   }
}
