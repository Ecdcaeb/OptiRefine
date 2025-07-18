package net.minecraft.potion;

public class PotionHealth extends Potion {
   public PotionHealth(boolean var1, int var2) {
      super(☃, ☃);
   }

   @Override
   public boolean isInstant() {
      return true;
   }

   @Override
   public boolean isReady(int var1, int var2) {
      return ☃ >= 1;
   }
}
