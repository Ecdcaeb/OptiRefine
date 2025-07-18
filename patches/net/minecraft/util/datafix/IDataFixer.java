package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;

public interface IDataFixer {
   NBTTagCompound process(IFixType var1, NBTTagCompound var2, int var3);
}
