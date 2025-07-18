package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.boss.EntityDragon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private final EntityDragon dragon;
   private final IPhase[] phases = new IPhase[PhaseList.getTotalPhases()];
   private IPhase phase;

   public PhaseManager(EntityDragon var1) {
      this.dragon = ☃;
      this.setPhase(PhaseList.HOVER);
   }

   public void setPhase(PhaseList<?> var1) {
      if (this.phase == null || ☃ != this.phase.getType()) {
         if (this.phase != null) {
            this.phase.removeAreaEffect();
         }

         this.phase = this.getPhase((PhaseList<IPhase>)☃);
         if (!this.dragon.world.isRemote) {
            this.dragon.getDataManager().set(EntityDragon.PHASE, ☃.getId());
         }

         LOGGER.debug("Dragon is now in phase {} on the {}", ☃, this.dragon.world.isRemote ? "client" : "server");
         this.phase.initPhase();
      }
   }

   public IPhase getCurrentPhase() {
      return this.phase;
   }

   public <T extends IPhase> T getPhase(PhaseList<T> var1) {
      int ☃ = ☃.getId();
      if (this.phases[☃] == null) {
         this.phases[☃] = ☃.createPhase(this.dragon);
      }

      return (T)this.phases[☃];
   }
}
