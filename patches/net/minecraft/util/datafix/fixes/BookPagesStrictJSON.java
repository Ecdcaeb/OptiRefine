package net.minecraft.util.datafix.fixes;

import com.google.gson.JsonParseException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class BookPagesStrictJSON implements IFixableData {
   @Override
   public int getFixVersion() {
      return 165;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("minecraft:written_book".equals(☃.getString("id"))) {
         NBTTagCompound ☃ = ☃.getCompoundTag("tag");
         if (☃.hasKey("pages", 9)) {
            NBTTagList ☃x = ☃.getTagList("pages", 8);

            for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
               String ☃xxx = ☃x.getStringTagAt(☃xx);
               ITextComponent ☃xxxx = null;
               if (!"null".equals(☃xxx) && !StringUtils.isNullOrEmpty(☃xxx)) {
                  if (☃xxx.charAt(0) == '"' && ☃xxx.charAt(☃xxx.length() - 1) == '"' || ☃xxx.charAt(0) == '{' && ☃xxx.charAt(☃xxx.length() - 1) == '}') {
                     try {
                        ☃xxxx = JsonUtils.gsonDeserialize(SignStrictJSON.GSON_INSTANCE, ☃xxx, ITextComponent.class, true);
                        if (☃xxxx == null) {
                           ☃xxxx = new TextComponentString("");
                        }
                     } catch (JsonParseException var10) {
                     }

                     if (☃xxxx == null) {
                        try {
                           ☃xxxx = ITextComponent.Serializer.jsonToComponent(☃xxx);
                        } catch (JsonParseException var9) {
                        }
                     }

                     if (☃xxxx == null) {
                        try {
                           ☃xxxx = ITextComponent.Serializer.fromJsonLenient(☃xxx);
                        } catch (JsonParseException var8) {
                        }
                     }

                     if (☃xxxx == null) {
                        ☃xxxx = new TextComponentString(☃xxx);
                     }
                  } else {
                     ☃xxxx = new TextComponentString(☃xxx);
                  }
               } else {
                  ☃xxxx = new TextComponentString("");
               }

               ☃x.set(☃xx, new NBTTagString(ITextComponent.Serializer.componentToJson(☃xxxx)));
            }

            ☃.setTag("pages", ☃x);
         }
      }

      return ☃;
   }
}
