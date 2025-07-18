package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerCapabilities {
   public boolean disableDamage;
   public boolean isFlying;
   public boolean allowFlying;
   public boolean isCreativeMode;
   public boolean allowEdit = true;
   private float flySpeed = 0.05F;
   private float walkSpeed = 0.1F;

   public void writeCapabilitiesToNBT(NBTTagCompound var1) {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setBoolean("invulnerable", this.disableDamage);
      ☃.setBoolean("flying", this.isFlying);
      ☃.setBoolean("mayfly", this.allowFlying);
      ☃.setBoolean("instabuild", this.isCreativeMode);
      ☃.setBoolean("mayBuild", this.allowEdit);
      ☃.setFloat("flySpeed", this.flySpeed);
      ☃.setFloat("walkSpeed", this.walkSpeed);
      ☃.setTag("abilities", ☃);
   }

   public void readCapabilitiesFromNBT(NBTTagCompound var1) {
      if (☃.hasKey("abilities", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("abilities");
         this.disableDamage = ☃.getBoolean("invulnerable");
         this.isFlying = ☃.getBoolean("flying");
         this.allowFlying = ☃.getBoolean("mayfly");
         this.isCreativeMode = ☃.getBoolean("instabuild");
         if (☃.hasKey("flySpeed", 99)) {
            this.flySpeed = ☃.getFloat("flySpeed");
            this.walkSpeed = ☃.getFloat("walkSpeed");
         }

         if (☃.hasKey("mayBuild", 1)) {
            this.allowEdit = ☃.getBoolean("mayBuild");
         }
      }
   }

   public float getFlySpeed() {
      return this.flySpeed;
   }

   public void setFlySpeed(float var1) {
      this.flySpeed = ☃;
   }

   public float getWalkSpeed() {
      return this.walkSpeed;
   }

   public void setPlayerWalkSpeed(float var1) {
      this.walkSpeed = ☃;
   }
}
