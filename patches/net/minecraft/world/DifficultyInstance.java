package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.util.math.MathHelper;

@Immutable
public class DifficultyInstance {
   private final EnumDifficulty worldDifficulty;
   private final float additionalDifficulty;

   public DifficultyInstance(EnumDifficulty var1, long var2, long var4, float var6) {
      this.worldDifficulty = ☃;
      this.additionalDifficulty = this.calculateAdditionalDifficulty(☃, ☃, ☃, ☃);
   }

   public float getAdditionalDifficulty() {
      return this.additionalDifficulty;
   }

   public boolean isHarderThan(float var1) {
      return this.additionalDifficulty > ☃;
   }

   public float getClampedAdditionalDifficulty() {
      if (this.additionalDifficulty < 2.0F) {
         return 0.0F;
      } else {
         return this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F;
      }
   }

   private float calculateAdditionalDifficulty(EnumDifficulty var1, long var2, long var4, float var6) {
      if (☃ == EnumDifficulty.PEACEFUL) {
         return 0.0F;
      } else {
         boolean ☃ = ☃ == EnumDifficulty.HARD;
         float ☃x = 0.75F;
         float ☃xx = MathHelper.clamp(((float)☃ + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
         ☃x += ☃xx;
         float ☃xxx = 0.0F;
         ☃xxx += MathHelper.clamp((float)☃ / 3600000.0F, 0.0F, 1.0F) * (☃ ? 1.0F : 0.75F);
         ☃xxx += MathHelper.clamp(☃ * 0.25F, 0.0F, ☃xx);
         if (☃ == EnumDifficulty.EASY) {
            ☃xxx *= 0.5F;
         }

         ☃x += ☃xxx;
         return ☃.getId() * ☃x;
      }
   }
}
