package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class EntityMoveHelper {
   protected final EntityLiving entity;
   protected double posX;
   protected double posY;
   protected double posZ;
   protected double speed;
   protected float moveForward;
   protected float moveStrafe;
   public EntityMoveHelper.Action action = EntityMoveHelper.Action.WAIT;

   public EntityMoveHelper(EntityLiving var1) {
      this.entity = ☃;
   }

   public boolean isUpdating() {
      return this.action == EntityMoveHelper.Action.MOVE_TO;
   }

   public double getSpeed() {
      return this.speed;
   }

   public void setMoveTo(double var1, double var3, double var5, double var7) {
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      this.speed = ☃;
      this.action = EntityMoveHelper.Action.MOVE_TO;
   }

   public void strafe(float var1, float var2) {
      this.action = EntityMoveHelper.Action.STRAFE;
      this.moveForward = ☃;
      this.moveStrafe = ☃;
      this.speed = 0.25;
   }

   public void read(EntityMoveHelper var1) {
      this.action = ☃.action;
      this.posX = ☃.posX;
      this.posY = ☃.posY;
      this.posZ = ☃.posZ;
      this.speed = Math.max(☃.speed, 1.0);
      this.moveForward = ☃.moveForward;
      this.moveStrafe = ☃.moveStrafe;
   }

   public void onUpdateMoveHelper() {
      if (this.action == EntityMoveHelper.Action.STRAFE) {
         float ☃ = (float)this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
         float ☃x = (float)this.speed * ☃;
         float ☃xx = this.moveForward;
         float ☃xxx = this.moveStrafe;
         float ☃xxxx = MathHelper.sqrt(☃xx * ☃xx + ☃xxx * ☃xxx);
         if (☃xxxx < 1.0F) {
            ☃xxxx = 1.0F;
         }

         ☃xxxx = ☃x / ☃xxxx;
         ☃xx *= ☃xxxx;
         ☃xxx *= ☃xxxx;
         float ☃xxxxx = MathHelper.sin(this.entity.rotationYaw * (float) (Math.PI / 180.0));
         float ☃xxxxxx = MathHelper.cos(this.entity.rotationYaw * (float) (Math.PI / 180.0));
         float ☃xxxxxxx = ☃xx * ☃xxxxxx - ☃xxx * ☃xxxxx;
         float ☃xxxxxxxx = ☃xxx * ☃xxxxxx + ☃xx * ☃xxxxx;
         PathNavigate ☃xxxxxxxxx = this.entity.getNavigator();
         if (☃xxxxxxxxx != null) {
            NodeProcessor ☃xxxxxxxxxx = ☃xxxxxxxxx.getNodeProcessor();
            if (☃xxxxxxxxxx != null
               && ☃xxxxxxxxxx.getPathNodeType(
                     this.entity.world,
                     MathHelper.floor(this.entity.posX + ☃xxxxxxx),
                     MathHelper.floor(this.entity.posY),
                     MathHelper.floor(this.entity.posZ + ☃xxxxxxxx)
                  )
                  != PathNodeType.WALKABLE) {
               this.moveForward = 1.0F;
               this.moveStrafe = 0.0F;
               ☃x = ☃;
            }
         }

         this.entity.setAIMoveSpeed(☃x);
         this.entity.setMoveForward(this.moveForward);
         this.entity.setMoveStrafing(this.moveStrafe);
         this.action = EntityMoveHelper.Action.WAIT;
      } else if (this.action == EntityMoveHelper.Action.MOVE_TO) {
         this.action = EntityMoveHelper.Action.WAIT;
         double ☃xxxxx = this.posX - this.entity.posX;
         double ☃xxxxxx = this.posZ - this.entity.posZ;
         double ☃xxxxxxx = this.posY - this.entity.posY;
         double ☃xxxxxxxx = ☃xxxxx * ☃xxxxx + ☃xxxxxxx * ☃xxxxxxx + ☃xxxxxx * ☃xxxxxx;
         if (☃xxxxxxxx < 2.5000003E-7F) {
            this.entity.setMoveForward(0.0F);
            return;
         }

         float ☃xxxxxxxxx = (float)(MathHelper.atan2(☃xxxxxx, ☃xxxxx) * 180.0F / (float)Math.PI) - 90.0F;
         this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, ☃xxxxxxxxx, 90.0F);
         this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
         if (☃xxxxxxx > this.entity.stepHeight && ☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx < Math.max(1.0F, this.entity.width)) {
            this.entity.getJumpHelper().setJumping();
            this.action = EntityMoveHelper.Action.JUMPING;
         }
      } else if (this.action == EntityMoveHelper.Action.JUMPING) {
         this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
         if (this.entity.onGround) {
            this.action = EntityMoveHelper.Action.WAIT;
         }
      } else {
         this.entity.setMoveForward(0.0F);
      }
   }

   protected float limitAngle(float var1, float var2, float var3) {
      float ☃ = MathHelper.wrapDegrees(☃ - ☃);
      if (☃ > ☃) {
         ☃ = ☃;
      }

      if (☃ < -☃) {
         ☃ = -☃;
      }

      float ☃x = ☃ + ☃;
      if (☃x < 0.0F) {
         ☃x += 360.0F;
      } else if (☃x > 360.0F) {
         ☃x -= 360.0F;
      }

      return ☃x;
   }

   public double getX() {
      return this.posX;
   }

   public double getY() {
      return this.posY;
   }

   public double getZ() {
      return this.posZ;
   }

   public static enum Action {
      WAIT,
      MOVE_TO,
      STRAFE,
      JUMPING;
   }
}
