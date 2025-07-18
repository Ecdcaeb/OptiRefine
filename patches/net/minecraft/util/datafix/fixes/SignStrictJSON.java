package net.minecraft.util.datafix.fixes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SignStrictJSON implements IFixableData {
   public static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(ITextComponent.class, new JsonDeserializer<ITextComponent>() {
      public ITextComponent deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (☃.isJsonPrimitive()) {
            return new TextComponentString(☃.getAsString());
         } else if (☃.isJsonArray()) {
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
      }
   }).create();

   @Override
   public int getFixVersion() {
      return 101;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("Sign".equals(☃.getString("id"))) {
         this.updateLine(☃, "Text1");
         this.updateLine(☃, "Text2");
         this.updateLine(☃, "Text3");
         this.updateLine(☃, "Text4");
      }

      return ☃;
   }

   private void updateLine(NBTTagCompound var1, String var2) {
      String ☃ = ☃.getString(☃);
      ITextComponent ☃x = null;
      if (!"null".equals(☃) && !StringUtils.isNullOrEmpty(☃)) {
         if (☃.charAt(0) == '"' && ☃.charAt(☃.length() - 1) == '"' || ☃.charAt(0) == '{' && ☃.charAt(☃.length() - 1) == '}') {
            try {
               ☃x = JsonUtils.gsonDeserialize(GSON_INSTANCE, ☃, ITextComponent.class, true);
               if (☃x == null) {
                  ☃x = new TextComponentString("");
               }
            } catch (JsonParseException var8) {
            }

            if (☃x == null) {
               try {
                  ☃x = ITextComponent.Serializer.jsonToComponent(☃);
               } catch (JsonParseException var7) {
               }
            }

            if (☃x == null) {
               try {
                  ☃x = ITextComponent.Serializer.fromJsonLenient(☃);
               } catch (JsonParseException var6) {
               }
            }

            if (☃x == null) {
               ☃x = new TextComponentString(☃);
            }
         } else {
            ☃x = new TextComponentString(☃);
         }
      } else {
         ☃x = new TextComponentString("");
      }

      ☃.setString(☃, ITextComponent.Serializer.componentToJson(☃x));
   }
}
