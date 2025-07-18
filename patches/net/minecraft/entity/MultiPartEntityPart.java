package net.minecraft.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class MultiPartEntityPart extends Entity {
   public final IEntityMultiPart parent;
   public final String partName;

   public MultiPartEntityPart(IEntityMultiPart var1, String var2, float var3, float var4) {
      super(☃.getWorld());
      this.setSize(☃, ☃);
      this.parent = ☃;
      this.partName = ☃;
   }

   @Override
   protected void entityInit() {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return this.isEntityInvulnerable(☃) ? false : this.parent.attackEntityFromPart(this, ☃, ☃);
   }

   @Override
   public boolean isEntityEqual(Entity var1) {
      return this == ☃ || this.parent == ☃;
   }
}
