package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JsonUtils;

public class NBTPredicate {
   public static final NBTPredicate ANY = new NBTPredicate(null);
   @Nullable
   private final NBTTagCompound tag;

   public NBTPredicate(@Nullable NBTTagCompound var1) {
      this.tag = ☃;
   }

   public boolean test(ItemStack var1) {
      return this == ANY ? true : this.test(☃.getTagCompound());
   }

   public boolean test(Entity var1) {
      return this == ANY ? true : this.test(CommandBase.entityToNBT(☃));
   }

   public boolean test(@Nullable NBTBase var1) {
      return ☃ == null ? this == ANY : this.tag == null || NBTUtil.areNBTEquals(this.tag, ☃, true);
   }

   public static NBTPredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         NBTTagCompound ☃;
         try {
            ☃ = JsonToNBT.getTagFromJson(JsonUtils.getString(☃, "nbt"));
         } catch (NBTException var3) {
            throw new JsonSyntaxException("Invalid nbt tag: " + var3.getMessage());
         }

         return new NBTPredicate(☃);
      } else {
         return ANY;
      }
   }
}
