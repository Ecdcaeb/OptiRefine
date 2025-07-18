package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWater extends EntityAIWander {
   protected final float probability;

   public EntityAIWanderAvoidWater(EntityCreature var1, double var2) {
      this(☃, ☃, 0.001F);
   }

   public EntityAIWanderAvoidWater(EntityCreature var1, double var2, float var4) {
      super(☃, ☃);
      this.probability = ☃;
   }

   @Nullable
   @Override
   protected Vec3d getPosition() {
      if (this.entity.isInWater()) {
         Vec3d ☃ = RandomPositionGenerator.getLandPos(this.entity, 15, 7);
         return ☃ == null ? super.getPosition() : ☃;
      } else {
         return this.entity.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.entity, 10, 7) : super.getPosition();
      }
   }
}
