package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEnderPearl extends EntityThrowable {
   private EntityLivingBase perlThrower;

   public EntityEnderPearl(World var1) {
      super(☃);
   }

   public EntityEnderPearl(World var1, EntityLivingBase var2) {
      super(☃, ☃);
      this.perlThrower = ☃;
   }

   public EntityEnderPearl(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesEnderPearl(DataFixer var0) {
      EntityThrowable.registerFixesThrowable(☃, "ThrownEnderpearl");
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      EntityLivingBase ☃ = this.getThrower();
      if (☃.entityHit != null) {
         if (☃.entityHit == this.perlThrower) {
            return;
         }

         ☃.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, ☃), 0.0F);
      }

      if (☃.typeOfHit == RayTraceResult.Type.BLOCK) {
         BlockPos ☃x = ☃.getBlockPos();
         TileEntity ☃xx = this.world.getTileEntity(☃x);
         if (☃xx instanceof TileEntityEndGateway) {
            TileEntityEndGateway ☃xxx = (TileEntityEndGateway)☃xx;
            if (☃ != null) {
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP)☃, this.world.getBlockState(☃x));
               }

               ☃xxx.teleportEntity(☃);
               this.setDead();
               return;
            }

            ☃xxx.teleportEntity(this);
            return;
         }
      }

      for (int ☃x = 0; ☃x < 32; ☃x++) {
         this.world
            .spawnParticle(
               EnumParticleTypes.PORTAL,
               this.posX,
               this.posY + this.rand.nextDouble() * 2.0,
               this.posZ,
               this.rand.nextGaussian(),
               0.0,
               this.rand.nextGaussian()
            );
      }

      if (!this.world.isRemote) {
         if (☃ instanceof EntityPlayerMP) {
            EntityPlayerMP ☃x = (EntityPlayerMP)☃;
            if (☃x.connection.getNetworkManager().isChannelOpen() && ☃x.world == this.world && !☃x.isPlayerSleeping()) {
               if (this.rand.nextFloat() < 0.05F && this.world.getGameRules().getBoolean("doMobSpawning")) {
                  EntityEndermite ☃xx = new EntityEndermite(this.world);
                  ☃xx.setSpawnedByPlayer(true);
                  ☃xx.setLocationAndAngles(☃.posX, ☃.posY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch);
                  this.world.spawnEntity(☃xx);
               }

               if (☃.isRiding()) {
                  ☃.dismountRidingEntity();
               }

               ☃.setPositionAndUpdate(this.posX, this.posY, this.posZ);
               ☃.fallDistance = 0.0F;
               ☃.attackEntityFrom(DamageSource.FALL, 5.0F);
            }
         } else if (☃ != null) {
            ☃.setPositionAndUpdate(this.posX, this.posY, this.posZ);
            ☃.fallDistance = 0.0F;
         }

         this.setDead();
      }
   }

   @Override
   public void onUpdate() {
      EntityLivingBase ☃ = this.getThrower();
      if (☃ != null && ☃ instanceof EntityPlayer && !☃.isEntityAlive()) {
         this.setDead();
      } else {
         super.onUpdate();
      }
   }

   @Nullable
   @Override
   public Entity changeDimension(int var1) {
      if (this.thrower.dimension != ☃) {
         this.thrower = null;
      }

      return super.changeDimension(☃);
   }
}
