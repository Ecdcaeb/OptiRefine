package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class PhaseBase implements IPhase {
   protected final EntityDragon dragon;

   public PhaseBase(EntityDragon var1) {
      this.dragon = ☃;
   }

   @Override
   public boolean getIsStationary() {
      return false;
   }

   @Override
   public void doClientRenderEffects() {
   }

   @Override
   public void doLocalUpdate() {
   }

   @Override
   public void onCrystalDestroyed(EntityEnderCrystal var1, BlockPos var2, DamageSource var3, @Nullable EntityPlayer var4) {
   }

   @Override
   public void initPhase() {
   }

   @Override
   public void removeAreaEffect() {
   }

   @Override
   public float getMaxRiseOrFall() {
      return 0.6F;
   }

   @Nullable
   @Override
   public Vec3d getTargetLocation() {
      return null;
   }

   @Override
   public float getAdjustedDamage(MultiPartEntityPart var1, DamageSource var2, float var3) {
      return ☃;
   }

   @Override
   public float getYawFactor() {
      float ☃ = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0F;
      float ☃x = Math.min(☃, 40.0F);
      return 0.7F / ☃x / ☃;
   }
}
