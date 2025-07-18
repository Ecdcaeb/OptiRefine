package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball {
   public int explosionPower = 1;

   public EntityLargeFireball(World var1) {
      super(☃);
   }

   public EntityLargeFireball(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public EntityLargeFireball(World var1, EntityLivingBase var2, double var3, double var5, double var7) {
      super(☃, ☃, ☃, ☃, ☃);
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (!this.world.isRemote) {
         if (☃.entityHit != null) {
            ☃.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
            this.applyEnchantments(this.shootingEntity, ☃.entityHit);
         }

         boolean ☃ = this.world.getGameRules().getBoolean("mobGriefing");
         this.world.newExplosion(null, this.posX, this.posY, this.posZ, this.explosionPower, ☃, ☃);
         this.setDead();
      }
   }

   public static void registerFixesLargeFireball(DataFixer var0) {
      EntityFireball.registerFixesFireball(☃, "Fireball");
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("ExplosionPower", this.explosionPower);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("ExplosionPower", 99)) {
         this.explosionPower = ☃.getInteger("ExplosionPower");
      }
   }
}
