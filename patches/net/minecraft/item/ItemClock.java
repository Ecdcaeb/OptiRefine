package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemClock extends Item {
   public ItemClock() {
      this.addPropertyOverride(new ResourceLocation("time"), new IItemPropertyGetter() {
         double rotation;
         double rota;
         long lastUpdateTick;

         @Override
         public float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
            boolean ☃ = ☃ != null;
            Entity ☃x = (Entity)(☃ ? ☃ : ☃.getItemFrame());
            if (☃ == null && ☃x != null) {
               ☃ = ☃x.world;
            }

            if (☃ == null) {
               return 0.0F;
            } else {
               double ☃xx;
               if (☃.provider.isSurfaceWorld()) {
                  ☃xx = ☃.getCelestialAngle(1.0F);
               } else {
                  ☃xx = Math.random();
               }

               ☃xx = this.wobble(☃, ☃xx);
               return (float)☃xx;
            }
         }

         private double wobble(World var1, double var2) {
            if (☃.getTotalWorldTime() != this.lastUpdateTick) {
               this.lastUpdateTick = ☃.getTotalWorldTime();
               double ☃ = ☃ - this.rotation;
               ☃ = MathHelper.positiveModulo(☃ + 0.5, 1.0) - 0.5;
               this.rota += ☃ * 0.1;
               this.rota *= 0.9;
               this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0);
            }

            return this.rotation;
         }
      });
   }
}
