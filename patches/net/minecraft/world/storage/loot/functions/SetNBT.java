package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetNBT extends LootFunction {
   private final NBTTagCompound tag;

   public SetNBT(LootCondition[] var1, NBTTagCompound var2) {
      super(☃);
      this.tag = ☃;
   }

   @Override
   public ItemStack apply(ItemStack var1, Random var2, LootContext var3) {
      NBTTagCompound ☃ = ☃.getTagCompound();
      if (☃ == null) {
         ☃ = this.tag.copy();
      } else {
         ☃.merge(this.tag);
      }

      ☃.setTagCompound(☃);
      return ☃;
   }

   public static class Serializer extends LootFunction.Serializer<SetNBT> {
      public Serializer() {
         super(new ResourceLocation("set_nbt"), SetNBT.class);
      }

      public void serialize(JsonObject var1, SetNBT var2, JsonSerializationContext var3) {
         ☃.addProperty("tag", ☃.tag.toString());
      }

      public SetNBT deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3) {
         try {
            NBTTagCompound ☃ = JsonToNBT.getTagFromJson(JsonUtils.getString(☃, "tag"));
            return new SetNBT(☃, ☃);
         } catch (NBTException var5) {
            throw new JsonSyntaxException(var5);
         }
      }
   }
}
