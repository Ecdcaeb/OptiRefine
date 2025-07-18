package net.minecraft.util;

import net.minecraft.nbt.NBTTagCompound;

public class WeightedSpawnerEntity extends WeightedRandom.Item {
   private final NBTTagCompound nbt;

   public WeightedSpawnerEntity() {
      super(1);
      this.nbt = new NBTTagCompound();
      this.nbt.setString("id", "minecraft:pig");
   }

   public WeightedSpawnerEntity(NBTTagCompound var1) {
      this(☃.hasKey("Weight", 99) ? ☃.getInteger("Weight") : 1, ☃.getCompoundTag("Entity"));
   }

   public WeightedSpawnerEntity(int var1, NBTTagCompound var2) {
      super(☃);
      this.nbt = ☃;
   }

   public NBTTagCompound toCompoundTag() {
      NBTTagCompound ☃ = new NBTTagCompound();
      if (!this.nbt.hasKey("id", 8)) {
         this.nbt.setString("id", "minecraft:pig");
      } else if (!this.nbt.getString("id").contains(":")) {
         this.nbt.setString("id", new ResourceLocation(this.nbt.getString("id")).toString());
      }

      ☃.setTag("Entity", this.nbt);
      ☃.setInteger("Weight", this.itemWeight);
      return ☃;
   }

   public NBTTagCompound getNbt() {
      return this.nbt;
   }
}
