package net.minecraft.entity.passive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityShoulderRiding extends EntityTameable {
   private int rideCooldownCounter;

   public EntityShoulderRiding(World var1) {
      super(☃);
   }

   public boolean setEntityOnShoulder(EntityPlayer var1) {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setString("id", this.getEntityString());
      this.writeToNBT(☃);
      if (☃.addShoulderEntity(☃)) {
         this.world.removeEntity(this);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void onUpdate() {
      this.rideCooldownCounter++;
      super.onUpdate();
   }

   public boolean canSitOnShoulder() {
      return this.rideCooldownCounter > 100;
   }
}
