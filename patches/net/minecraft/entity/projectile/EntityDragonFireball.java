package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDragonFireball extends EntityFireball {
   public EntityDragonFireball(World var1) {
      super(☃);
      this.setSize(1.0F, 1.0F);
   }

   public EntityDragonFireball(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.setSize(1.0F, 1.0F);
   }

   public EntityDragonFireball(World var1, EntityLivingBase var2, double var3, double var5, double var7) {
      super(☃, ☃, ☃, ☃, ☃);
      this.setSize(1.0F, 1.0F);
   }

   public static void registerFixesDragonFireball(DataFixer var0) {
      EntityFireball.registerFixesFireball(☃, "DragonFireball");
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (☃.entityHit == null || !☃.entityHit.isEntityEqual(this.shootingEntity)) {
         if (!this.world.isRemote) {
            List<EntityLivingBase> ☃ = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(4.0, 2.0, 4.0));
            EntityAreaEffectCloud ☃x = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
            ☃x.setOwner(this.shootingEntity);
            ☃x.setParticle(EnumParticleTypes.DRAGON_BREATH);
            ☃x.setRadius(3.0F);
            ☃x.setDuration(600);
            ☃x.setRadiusPerTick((7.0F - ☃x.getRadius()) / ☃x.getDuration());
            ☃x.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
            if (!☃.isEmpty()) {
               for (EntityLivingBase ☃xx : ☃) {
                  double ☃xxx = this.getDistanceSq(☃xx);
                  if (☃xxx < 16.0) {
                     ☃x.setPosition(☃xx.posX, ☃xx.posY, ☃xx.posZ);
                     break;
                  }
               }
            }

            this.world.playEvent(2006, new BlockPos(this.posX, this.posY, this.posZ), 0);
            this.world.spawnEntity(☃x);
            this.setDead();
         }
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return false;
   }

   @Override
   protected EnumParticleTypes getParticleType() {
      return EnumParticleTypes.DRAGON_BREATH;
   }

   @Override
   protected boolean isFireballFiery() {
      return false;
   }
}
