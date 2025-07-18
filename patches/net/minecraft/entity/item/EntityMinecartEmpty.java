package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart {
   public EntityMinecartEmpty(World var1) {
      super(☃);
   }

   public EntityMinecartEmpty(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesMinecartEmpty(DataFixer var0) {
      EntityMinecart.registerFixesMinecart(☃, EntityMinecartEmpty.class);
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      if (☃.isSneaking()) {
         return false;
      } else if (this.isBeingRidden()) {
         return true;
      } else {
         if (!this.world.isRemote) {
            ☃.startRiding(this);
         }

         return true;
      }
   }

   @Override
   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      if (☃) {
         if (this.isBeingRidden()) {
            this.removePassengers();
         }

         if (this.getRollingAmplitude() == 0) {
            this.setRollingDirection(-this.getRollingDirection());
            this.setRollingAmplitude(10);
            this.setDamage(50.0F);
            this.markVelocityChanged();
         }
      }
   }

   @Override
   public EntityMinecart.Type getType() {
      return EntityMinecart.Type.RIDEABLE;
   }
}
