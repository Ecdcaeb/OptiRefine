package net.minecraft.util;

import net.minecraft.util.math.MathHelper;

public class CombatRules {
   public static float getDamageAfterAbsorb(float var0, float var1, float var2) {
      float ☃ = 2.0F + ☃ / 4.0F;
      float ☃x = MathHelper.clamp(☃ - ☃ / ☃, ☃ * 0.2F, 20.0F);
      return ☃ * (1.0F - ☃x / 25.0F);
   }

   public static float getDamageAfterMagicAbsorb(float var0, float var1) {
      float ☃ = MathHelper.clamp(☃, 0.0F, 20.0F);
      return ☃ * (1.0F - ☃ / 25.0F);
   }
}
