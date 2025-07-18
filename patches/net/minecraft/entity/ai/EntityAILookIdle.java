package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAILookIdle extends EntityAIBase {
   private final EntityLiving idleEntity;
   private double lookX;
   private double lookZ;
   private int idleTime;

   public EntityAILookIdle(EntityLiving var1) {
      this.idleEntity = ☃;
      this.setMutexBits(3);
   }

   @Override
   public boolean shouldExecute() {
      return this.idleEntity.getRNG().nextFloat() < 0.02F;
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.idleTime >= 0;
   }

   @Override
   public void startExecuting() {
      double ☃ = (Math.PI * 2) * this.idleEntity.getRNG().nextDouble();
      this.lookX = Math.cos(☃);
      this.lookZ = Math.sin(☃);
      this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
   }

   @Override
   public void updateTask() {
      this.idleTime--;
      this.idleEntity
         .getLookHelper()
         .setLookPosition(
            this.idleEntity.posX + this.lookX,
            this.idleEntity.posY + this.idleEntity.getEyeHeight(),
            this.idleEntity.posZ + this.lookZ,
            this.idleEntity.getHorizontalFaceSpeed(),
            this.idleEntity.getVerticalFaceSpeed()
         );
   }
}
