package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable extends TileEntity implements ILockableContainer {
   private LockCode code = LockCode.EMPTY_CODE;

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.code = LockCode.fromNBT(☃);
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      if (this.code != null) {
         this.code.toNBT(☃);
      }

      return ☃;
   }

   @Override
   public boolean isLocked() {
      return this.code != null && !this.code.isEmpty();
   }

   @Override
   public LockCode getLockCode() {
      return this.code;
   }

   @Override
   public void setLockCode(LockCode var1) {
      this.code = ☃;
   }

   @Override
   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName()));
   }
}
