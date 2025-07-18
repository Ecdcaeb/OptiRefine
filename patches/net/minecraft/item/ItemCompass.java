package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemCompass extends Item {
   public ItemCompass() {
      this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
         double rotation;
         double rota;
         long lastUpdateTick;

         @Override
         public float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
            if (☃ == null && !☃.isOnItemFrame()) {
               return 0.0F;
            } else {
               boolean ☃ = ☃ != null;
               Entity ☃x = (Entity)(☃ ? ☃ : ☃.getItemFrame());
               if (☃ == null) {
                  ☃ = ☃x.world;
               }

               double ☃xx;
               if (☃.provider.isSurfaceWorld()) {
                  double ☃xxx = ☃ ? ☃x.rotationYaw : this.getFrameRotation((EntityItemFrame)☃x);
                  ☃xxx = MathHelper.positiveModulo(☃xxx / 360.0, 1.0);
                  double ☃xxxx = this.getSpawnToAngle(☃, ☃x) / (float) (Math.PI * 2);
                  ☃xx = 0.5 - (☃xxx - 0.25 - ☃xxxx);
               } else {
                  ☃xx = Math.random();
               }

               if (☃) {
                  ☃xx = this.wobble(☃, ☃xx);
               }

               return MathHelper.positiveModulo((float)☃xx, 1.0F);
            }
         }

         private double wobble(World var1, double var2) {
            if (☃.getTotalWorldTime() != this.lastUpdateTick) {
               this.lastUpdateTick = ☃.getTotalWorldTime();
               double ☃ = ☃ - this.rotation;
               ☃ = MathHelper.positiveModulo(☃ + 0.5, 1.0) - 0.5;
               this.rota += ☃ * 0.1;
               this.rota *= 0.8;
               this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0);
            }

            return this.rotation;
         }

         private double getFrameRotation(EntityItemFrame var1) {
            return MathHelper.wrapDegrees(180 + ☃.facingDirection.getHorizontalIndex() * 90);
         }

         private double getSpawnToAngle(World var1, Entity var2) {
            BlockPos ☃ = ☃.getSpawnPoint();
            return Math.atan2(☃.getZ() - ☃.posZ, ☃.getX() - ☃.posX);
         }
      });
   }
}
