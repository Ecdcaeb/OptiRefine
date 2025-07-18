package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
   private final EntityTameable tameable;

   public EntityAITargetNonTamed(EntityTameable var1, Class<T> var2, boolean var3, Predicate<? super T> var4) {
      super(☃, ☃, 10, ☃, false, ☃);
      this.tameable = ☃;
   }

   @Override
   public boolean shouldExecute() {
      return !this.tameable.isTamed() && super.shouldExecute();
   }
}
