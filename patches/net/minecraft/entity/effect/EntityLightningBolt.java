package net.minecraft.entity.effect;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityWeatherEffect {
   private int lightningState;
   public long boltVertex;
   private int boltLivingTime;
   private final boolean effectOnly;

   public EntityLightningBolt(World var1, double var2, double var4, double var6, boolean var8) {
      super(☃);
      this.setLocationAndAngles(☃, ☃, ☃, 0.0F, 0.0F);
      this.lightningState = 2;
      this.boltVertex = this.rand.nextLong();
      this.boltLivingTime = this.rand.nextInt(3) + 1;
      this.effectOnly = ☃;
      BlockPos ☃ = new BlockPos(this);
      if (!☃
         && !☃.isRemote
         && ☃.getGameRules().getBoolean("doFireTick")
         && (☃.getDifficulty() == EnumDifficulty.NORMAL || ☃.getDifficulty() == EnumDifficulty.HARD)
         && ☃.isAreaLoaded(☃, 10)) {
         if (☃.getBlockState(☃).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(☃, ☃)) {
            ☃.setBlockState(☃, Blocks.FIRE.getDefaultState());
         }

         for (int ☃x = 0; ☃x < 4; ☃x++) {
            BlockPos ☃xx = ☃.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
            if (☃.getBlockState(☃xx).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(☃, ☃xx)) {
               ☃.setBlockState(☃xx, Blocks.FIRE.getDefaultState());
            }
         }
      }
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.WEATHER;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.lightningState == 2) {
         this.world
            .playSound(
               null,
               this.posX,
               this.posY,
               this.posZ,
               SoundEvents.ENTITY_LIGHTNING_THUNDER,
               SoundCategory.WEATHER,
               10000.0F,
               0.8F + this.rand.nextFloat() * 0.2F
            );
         this.world
            .playSound(
               null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F
            );
      }

      this.lightningState--;
      if (this.lightningState < 0) {
         if (this.boltLivingTime == 0) {
            this.setDead();
         } else if (this.lightningState < -this.rand.nextInt(10)) {
            this.boltLivingTime--;
            this.lightningState = 1;
            if (!this.effectOnly && !this.world.isRemote) {
               this.boltVertex = this.rand.nextLong();
               BlockPos ☃ = new BlockPos(this);
               if (this.world.getGameRules().getBoolean("doFireTick")
                  && this.world.isAreaLoaded(☃, 10)
                  && this.world.getBlockState(☃).getMaterial() == Material.AIR
                  && Blocks.FIRE.canPlaceBlockAt(this.world, ☃)) {
                  this.world.setBlockState(☃, Blocks.FIRE.getDefaultState());
               }
            }
         }
      }

      if (this.lightningState >= 0) {
         if (this.world.isRemote) {
            this.world.setLastLightningBolt(2);
         } else if (!this.effectOnly) {
            double ☃ = 3.0;
            List<Entity> ☃x = this.world
               .getEntitiesWithinAABBExcludingEntity(
                  this, new AxisAlignedBB(this.posX - 3.0, this.posY - 3.0, this.posZ - 3.0, this.posX + 3.0, this.posY + 6.0 + 3.0, this.posZ + 3.0)
               );

            for (int ☃xx = 0; ☃xx < ☃x.size(); ☃xx++) {
               Entity ☃xxx = ☃x.get(☃xx);
               ☃xxx.onStruckByLightning(this);
            }
         }
      }
   }

   @Override
   protected void entityInit() {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
   }
}
