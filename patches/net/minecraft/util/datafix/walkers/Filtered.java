package net.minecraft.util.datafix.walkers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

public abstract class Filtered implements IDataWalker {
   private final ResourceLocation key;

   public Filtered(Class<?> var1) {
      if (Entity.class.isAssignableFrom(☃)) {
         this.key = EntityList.getKey((Class<? extends Entity>)☃);
      } else if (TileEntity.class.isAssignableFrom(☃)) {
         this.key = TileEntity.getKey((Class<? extends TileEntity>)☃);
      } else {
         this.key = null;
      }
   }

   @Override
   public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
      if (new ResourceLocation(☃.getString("id")).equals(this.key)) {
         ☃ = this.filteredProcess(☃, ☃, ☃);
      }

      return ☃;
   }

   abstract NBTTagCompound filteredProcess(IDataFixer var1, NBTTagCompound var2, int var3);
}
