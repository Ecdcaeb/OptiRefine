package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving {
   public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
   public static final AttributeModifier FLEEING_SPEED_MODIFIER = new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0, 2)
      .setSaved(false);
   private BlockPos homePosition = BlockPos.ORIGIN;
   private float maximumHomeDistance = -1.0F;
   private final float restoreWaterCost = PathNodeType.WATER.getPriority();

   public EntityCreature(World var1) {
      super(☃);
   }

   public float getBlockPathWeight(BlockPos var1) {
      return 0.0F;
   }

   @Override
   public boolean getCanSpawnHere() {
      return super.getCanSpawnHere() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F;
   }

   public boolean hasPath() {
      return !this.navigator.noPath();
   }

   public boolean isWithinHomeDistanceCurrentPosition() {
      return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
   }

   public boolean isWithinHomeDistanceFromPosition(BlockPos var1) {
      return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(☃) < this.maximumHomeDistance * this.maximumHomeDistance;
   }

   public void setHomePosAndDistance(BlockPos var1, int var2) {
      this.homePosition = ☃;
      this.maximumHomeDistance = ☃;
   }

   public BlockPos getHomePosition() {
      return this.homePosition;
   }

   public float getMaximumHomeDistance() {
      return this.maximumHomeDistance;
   }

   public void detachHome() {
      this.maximumHomeDistance = -1.0F;
   }

   public boolean hasHome() {
      return this.maximumHomeDistance != -1.0F;
   }

   @Override
   protected void updateLeashedState() {
      super.updateLeashedState();
      if (this.getLeashed() && this.getLeashHolder() != null && this.getLeashHolder().world == this.world) {
         Entity ☃ = this.getLeashHolder();
         this.setHomePosAndDistance(new BlockPos((int)☃.posX, (int)☃.posY, (int)☃.posZ), 5);
         float ☃x = this.getDistance(☃);
         if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
            if (☃x > 10.0F) {
               this.clearLeashed(true, true);
            }

            return;
         }

         this.onLeashDistance(☃x);
         if (☃x > 10.0F) {
            this.clearLeashed(true, true);
            this.tasks.disableControlFlag(1);
         } else if (☃x > 6.0F) {
            double ☃xx = (☃.posX - this.posX) / ☃x;
            double ☃xxx = (☃.posY - this.posY) / ☃x;
            double ☃xxxx = (☃.posZ - this.posZ) / ☃x;
            this.motionX = this.motionX + ☃xx * Math.abs(☃xx) * 0.4;
            this.motionY = this.motionY + ☃xxx * Math.abs(☃xxx) * 0.4;
            this.motionZ = this.motionZ + ☃xxxx * Math.abs(☃xxxx) * 0.4;
         } else {
            this.tasks.enableControlFlag(1);
            float ☃xx = 2.0F;
            Vec3d ☃xxx = new Vec3d(☃.posX - this.posX, ☃.posY - this.posY, ☃.posZ - this.posZ).normalize().scale(Math.max(☃x - 2.0F, 0.0F));
            this.getNavigator().tryMoveToXYZ(this.posX + ☃xxx.x, this.posY + ☃xxx.y, this.posZ + ☃xxx.z, this.followLeashSpeed());
         }
      }
   }

   protected double followLeashSpeed() {
      return 1.0;
   }

   protected void onLeashDistance(float var1) {
   }
}
