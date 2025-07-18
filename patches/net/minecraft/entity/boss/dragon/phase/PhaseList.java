package net.minecraft.entity.boss.dragon.phase;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import net.minecraft.entity.boss.EntityDragon;

public class PhaseList<T extends IPhase> {
   private static PhaseList<?>[] phases = new PhaseList[0];
   public static final PhaseList<PhaseHoldingPattern> HOLDING_PATTERN = create(PhaseHoldingPattern.class, "HoldingPattern");
   public static final PhaseList<PhaseStrafePlayer> STRAFE_PLAYER = create(PhaseStrafePlayer.class, "StrafePlayer");
   public static final PhaseList<PhaseLandingApproach> LANDING_APPROACH = create(PhaseLandingApproach.class, "LandingApproach");
   public static final PhaseList<PhaseLanding> LANDING = create(PhaseLanding.class, "Landing");
   public static final PhaseList<PhaseTakeoff> TAKEOFF = create(PhaseTakeoff.class, "Takeoff");
   public static final PhaseList<PhaseSittingFlaming> SITTING_FLAMING = create(PhaseSittingFlaming.class, "SittingFlaming");
   public static final PhaseList<PhaseSittingScanning> SITTING_SCANNING = create(PhaseSittingScanning.class, "SittingScanning");
   public static final PhaseList<PhaseSittingAttacking> SITTING_ATTACKING = create(PhaseSittingAttacking.class, "SittingAttacking");
   public static final PhaseList<PhaseChargingPlayer> CHARGING_PLAYER = create(PhaseChargingPlayer.class, "ChargingPlayer");
   public static final PhaseList<PhaseDying> DYING = create(PhaseDying.class, "Dying");
   public static final PhaseList<PhaseHover> HOVER = create(PhaseHover.class, "Hover");
   private final Class<? extends IPhase> clazz;
   private final int id;
   private final String name;

   private PhaseList(int var1, Class<? extends IPhase> var2, String var3) {
      this.id = ☃;
      this.clazz = ☃;
      this.name = ☃;
   }

   public IPhase createPhase(EntityDragon var1) {
      try {
         Constructor<? extends IPhase> ☃ = this.getConstructor();
         return ☃.newInstance(☃);
      } catch (Exception var3) {
         throw new Error(var3);
      }
   }

   protected Constructor<? extends IPhase> getConstructor() throws NoSuchMethodException {
      return this.clazz.getConstructor(EntityDragon.class);
   }

   public int getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return this.name + " (#" + this.id + ")";
   }

   public static PhaseList<?> getById(int var0) {
      return ☃ >= 0 && ☃ < phases.length ? phases[☃] : HOLDING_PATTERN;
   }

   public static int getTotalPhases() {
      return phases.length;
   }

   private static <T extends IPhase> PhaseList<T> create(Class<T> var0, String var1) {
      PhaseList<T> ☃ = new PhaseList<>(phases.length, ☃, ☃);
      phases = Arrays.copyOf(phases, phases.length + 1);
      phases[☃.getId()] = ☃;
      return ☃;
   }
}
