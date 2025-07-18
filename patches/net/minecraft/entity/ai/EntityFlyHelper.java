package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;

public class EntityFlyHelper extends EntityMoveHelper {
   public EntityFlyHelper(EntityLiving var1) {
      super(☃);
   }

   @Override
   public void onUpdateMoveHelper() {
      if (this.action == EntityMoveHelper.Action.MOVE_TO) {
         this.action = EntityMoveHelper.Action.WAIT;
         this.entity.setNoGravity(true);
         double ☃ = this.posX - this.entity.posX;
         double ☃x = this.posY - this.entity.posY;
         double ☃xx = this.posZ - this.entity.posZ;
         double ☃xxx = ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
         if (☃xxx < 2.5000003E-7F) {
            this.entity.setMoveVertical(0.0F);
            this.entity.setMoveForward(0.0F);
            return;
         }

         float ☃xxxx = (float)(MathHelper.atan2(☃xx, ☃) * 180.0F / (float)Math.PI) - 90.0F;
         this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, ☃xxxx, 10.0F);
         float ☃xxxxx;
         if (this.entity.onGround) {
            ☃xxxxx = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
         } else {
            ☃xxxxx = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue());
         }

         this.entity.setAIMoveSpeed(☃xxxxx);
         double ☃xxxxxx = MathHelper.sqrt(☃ * ☃ + ☃xx * ☃xx);
         float ☃xxxxxxx = (float)(-(MathHelper.atan2(☃x, ☃xxxxxx) * 180.0F / (float)Math.PI));
         this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, ☃xxxxxxx, 10.0F);
         this.entity.setMoveVertical(☃x > 0.0 ? ☃xxxxx : -☃xxxxx);
      } else {
         this.entity.setNoGravity(false);
         this.entity.setMoveVertical(0.0F);
         this.entity.setMoveForward(0.0F);
      }
   }
}
