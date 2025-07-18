package net.minecraft.entity.item;

import com.google.common.base.Optional;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class EntityEnderCrystal extends Entity {
   private static final DataParameter<Optional<BlockPos>> BEAM_TARGET = EntityDataManager.createKey(
      EntityEnderCrystal.class, DataSerializers.OPTIONAL_BLOCK_POS
   );
   private static final DataParameter<Boolean> SHOW_BOTTOM = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.BOOLEAN);
   public int innerRotation;

   public EntityEnderCrystal(World var1) {
      super(☃);
      this.preventEntitySpawning = true;
      this.setSize(2.0F, 2.0F);
      this.innerRotation = this.rand.nextInt(100000);
   }

   public EntityEnderCrystal(World var1, double var2, double var4, double var6) {
      this(☃);
      this.setPosition(☃, ☃, ☃);
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
      this.getDataManager().register(BEAM_TARGET, Optional.absent());
      this.getDataManager().register(SHOW_BOTTOM, true);
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.innerRotation++;
      if (!this.world.isRemote) {
         BlockPos ☃ = new BlockPos(this);
         if (this.world.provider instanceof WorldProviderEnd && this.world.getBlockState(☃).getBlock() != Blocks.FIRE) {
            this.world.setBlockState(☃, Blocks.FIRE.getDefaultState());
         }
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      if (this.getBeamTarget() != null) {
         ☃.setTag("BeamTarget", NBTUtil.createPosTag(this.getBeamTarget()));
      }

      ☃.setBoolean("ShowBottom", this.shouldShowBottom());
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      if (☃.hasKey("BeamTarget", 10)) {
         this.setBeamTarget(NBTUtil.getPosFromTag(☃.getCompoundTag("BeamTarget")));
      }

      if (☃.hasKey("ShowBottom", 1)) {
         this.setShowBottom(☃.getBoolean("ShowBottom"));
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (☃.getTrueSource() instanceof EntityDragon) {
         return false;
      } else {
         if (!this.isDead && !this.world.isRemote) {
            this.setDead();
            if (!this.world.isRemote) {
               if (!☃.isExplosion()) {
                  this.world.createExplosion(null, this.posX, this.posY, this.posZ, 6.0F, true);
               }

               this.onCrystalDestroyed(☃);
            }
         }

         return true;
      }
   }

   @Override
   public void onKillCommand() {
      this.onCrystalDestroyed(DamageSource.GENERIC);
      super.onKillCommand();
   }

   private void onCrystalDestroyed(DamageSource var1) {
      if (this.world.provider instanceof WorldProviderEnd) {
         WorldProviderEnd ☃ = (WorldProviderEnd)this.world.provider;
         DragonFightManager ☃x = ☃.getDragonFightManager();
         if (☃x != null) {
            ☃x.onCrystalDestroyed(this, ☃);
         }
      }
   }

   public void setBeamTarget(@Nullable BlockPos var1) {
      this.getDataManager().set(BEAM_TARGET, Optional.fromNullable(☃));
   }

   @Nullable
   public BlockPos getBeamTarget() {
      return (BlockPos)this.getDataManager().get(BEAM_TARGET).orNull();
   }

   public void setShowBottom(boolean var1) {
      this.getDataManager().set(SHOW_BOTTOM, ☃);
   }

   public boolean shouldShowBottom() {
      return this.getDataManager().get(SHOW_BOTTOM);
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      return super.isInRangeToRenderDist(☃) || this.getBeamTarget() != null;
   }
}
