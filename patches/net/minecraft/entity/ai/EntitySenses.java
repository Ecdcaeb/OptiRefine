package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitySenses {
   EntityLiving entity;
   List<Entity> seenEntities = Lists.newArrayList();
   List<Entity> unseenEntities = Lists.newArrayList();

   public EntitySenses(EntityLiving var1) {
      this.entity = ☃;
   }

   public void clearSensingCache() {
      this.seenEntities.clear();
      this.unseenEntities.clear();
   }

   public boolean canSee(Entity var1) {
      if (this.seenEntities.contains(☃)) {
         return true;
      } else if (this.unseenEntities.contains(☃)) {
         return false;
      } else {
         this.entity.world.profiler.startSection("canSee");
         boolean ☃ = this.entity.canEntityBeSeen(☃);
         this.entity.world.profiler.endSection();
         if (☃) {
            this.seenEntities.add(☃);
         } else {
            this.unseenEntities.add(☃);
         }

         return ☃;
      }
   }
}
