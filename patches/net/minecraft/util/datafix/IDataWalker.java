package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;

public interface IDataWalker {
   NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3);
}
