package net.minecraft.potion;

import com.google.common.collect.ComparisonChain;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect implements Comparable<PotionEffect> {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Potion potion;
   private int duration;
   private int amplifier;
   private boolean isSplashPotion;
   private boolean isAmbient;
   private boolean isPotionDurationMax;
   private boolean showParticles;

   public PotionEffect(Potion var1) {
      this(☃, 0, 0);
   }

   public PotionEffect(Potion var1, int var2) {
      this(☃, ☃, 0);
   }

   public PotionEffect(Potion var1, int var2, int var3) {
      this(☃, ☃, ☃, false, true);
   }

   public PotionEffect(Potion var1, int var2, int var3, boolean var4, boolean var5) {
      this.potion = ☃;
      this.duration = ☃;
      this.amplifier = ☃;
      this.isAmbient = ☃;
      this.showParticles = ☃;
   }

   public PotionEffect(PotionEffect var1) {
      this.potion = ☃.potion;
      this.duration = ☃.duration;
      this.amplifier = ☃.amplifier;
      this.isAmbient = ☃.isAmbient;
      this.showParticles = ☃.showParticles;
   }

   public void combine(PotionEffect var1) {
      if (this.potion != ☃.potion) {
         LOGGER.warn("This method should only be called for matching effects!");
      }

      if (☃.amplifier > this.amplifier) {
         this.amplifier = ☃.amplifier;
         this.duration = ☃.duration;
      } else if (☃.amplifier == this.amplifier && this.duration < ☃.duration) {
         this.duration = ☃.duration;
      } else if (!☃.isAmbient && this.isAmbient) {
         this.isAmbient = ☃.isAmbient;
      }

      this.showParticles = ☃.showParticles;
   }

   public Potion getPotion() {
      return this.potion;
   }

   public int getDuration() {
      return this.duration;
   }

   public int getAmplifier() {
      return this.amplifier;
   }

   public boolean getIsAmbient() {
      return this.isAmbient;
   }

   public boolean doesShowParticles() {
      return this.showParticles;
   }

   public boolean onUpdate(EntityLivingBase var1) {
      if (this.duration > 0) {
         if (this.potion.isReady(this.duration, this.amplifier)) {
            this.performEffect(☃);
         }

         this.deincrementDuration();
      }

      return this.duration > 0;
   }

   private int deincrementDuration() {
      return --this.duration;
   }

   public void performEffect(EntityLivingBase var1) {
      if (this.duration > 0) {
         this.potion.performEffect(☃, this.amplifier);
      }
   }

   public String getEffectName() {
      return this.potion.getName();
   }

   @Override
   public String toString() {
      String ☃;
      if (this.amplifier > 0) {
         ☃ = this.getEffectName() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration;
      } else {
         ☃ = this.getEffectName() + ", Duration: " + this.duration;
      }

      if (this.isSplashPotion) {
         ☃ = ☃ + ", Splash: true";
      }

      if (!this.showParticles) {
         ☃ = ☃ + ", Particles: false";
      }

      return ☃;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof PotionEffect)) {
         return false;
      } else {
         PotionEffect ☃ = (PotionEffect)☃;
         return this.duration == ☃.duration
            && this.amplifier == ☃.amplifier
            && this.isSplashPotion == ☃.isSplashPotion
            && this.isAmbient == ☃.isAmbient
            && this.potion.equals(☃.potion);
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.potion.hashCode();
      ☃ = 31 * ☃ + this.duration;
      ☃ = 31 * ☃ + this.amplifier;
      ☃ = 31 * ☃ + (this.isSplashPotion ? 1 : 0);
      return 31 * ☃ + (this.isAmbient ? 1 : 0);
   }

   public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound var1) {
      ☃.setByte("Id", (byte)Potion.getIdFromPotion(this.getPotion()));
      ☃.setByte("Amplifier", (byte)this.getAmplifier());
      ☃.setInteger("Duration", this.getDuration());
      ☃.setBoolean("Ambient", this.getIsAmbient());
      ☃.setBoolean("ShowParticles", this.doesShowParticles());
      return ☃;
   }

   public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound var0) {
      int ☃ = ☃.getByte("Id");
      Potion ☃x = Potion.getPotionById(☃);
      if (☃x == null) {
         return null;
      } else {
         int ☃xx = ☃.getByte("Amplifier");
         int ☃xxx = ☃.getInteger("Duration");
         boolean ☃xxxx = ☃.getBoolean("Ambient");
         boolean ☃xxxxx = true;
         if (☃.hasKey("ShowParticles", 1)) {
            ☃xxxxx = ☃.getBoolean("ShowParticles");
         }

         return new PotionEffect(☃x, ☃xxx, ☃xx < 0 ? 0 : ☃xx, ☃xxxx, ☃xxxxx);
      }
   }

   public void setPotionDurationMax(boolean var1) {
      this.isPotionDurationMax = ☃;
   }

   public boolean getIsPotionDurationMax() {
      return this.isPotionDurationMax;
   }

   public int compareTo(PotionEffect var1) {
      int ☃ = 32147;
      return (this.getDuration() <= 32147 || ☃.getDuration() <= 32147) && (!this.getIsAmbient() || !☃.getIsAmbient())
         ? ComparisonChain.start()
            .compare(this.getIsAmbient(), ☃.getIsAmbient())
            .compare(this.getDuration(), ☃.getDuration())
            .compare(this.getPotion().getLiquidColor(), ☃.getPotion().getLiquidColor())
            .result()
         : ComparisonChain.start()
            .compare(this.getIsAmbient(), ☃.getIsAmbient())
            .compare(this.getPotion().getLiquidColor(), ☃.getPotion().getLiquidColor())
            .result();
   }
}
