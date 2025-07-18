package net.minecraft.entity.item;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMinecartTNT extends EntityMinecart {
   private int minecartTNTFuse = -1;

   public EntityMinecartTNT(World var1) {
      super(☃);
   }

   public EntityMinecartTNT(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesMinecartTNT(DataFixer var0) {
      EntityMinecart.registerFixesMinecart(☃, EntityMinecartTNT.class);
   }

   @Override
   public EntityMinecart.Type getType() {
      return EntityMinecart.Type.TNT;
   }

   @Override
   public IBlockState getDefaultDisplayTile() {
      return Blocks.TNT.getDefaultState();
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.minecartTNTFuse > 0) {
         this.minecartTNTFuse--;
         this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
      } else if (this.minecartTNTFuse == 0) {
         this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
      }

      if (this.collidedHorizontally) {
         double ☃ = this.motionX * this.motionX + this.motionZ * this.motionZ;
         if (☃ >= 0.01F) {
            this.explodeCart(☃);
         }
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      Entity ☃ = ☃.getImmediateSource();
      if (☃ instanceof EntityArrow) {
         EntityArrow ☃x = (EntityArrow)☃;
         if (☃x.isBurning()) {
            this.explodeCart(☃x.motionX * ☃x.motionX + ☃x.motionY * ☃x.motionY + ☃x.motionZ * ☃x.motionZ);
         }
      }

      return super.attackEntityFrom(☃, ☃);
   }

   @Override
   public void killMinecart(DamageSource var1) {
      double ☃ = this.motionX * this.motionX + this.motionZ * this.motionZ;
      if (!☃.isFireDamage() && !☃.isExplosion() && !(☃ >= 0.01F)) {
         super.killMinecart(☃);
         if (!☃.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(Blocks.TNT, 1), 0.0F);
         }
      } else {
         if (this.minecartTNTFuse < 0) {
            this.ignite();
            this.minecartTNTFuse = this.rand.nextInt(20) + this.rand.nextInt(20);
         }
      }
   }

   protected void explodeCart(double var1) {
      if (!this.world.isRemote) {
         double ☃ = Math.sqrt(☃);
         if (☃ > 5.0) {
            ☃ = 5.0;
         }

         this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0 + this.rand.nextDouble() * 1.5 * ☃), true);
         this.setDead();
      }
   }

   @Override
   public void fall(float var1, float var2) {
      if (☃ >= 3.0F) {
         float ☃ = ☃ / 10.0F;
         this.explodeCart(☃ * ☃);
      }

      super.fall(☃, ☃);
   }

   @Override
   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      if (☃ && this.minecartTNTFuse < 0) {
         this.ignite();
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 10) {
         this.ignite();
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   public void ignite() {
      this.minecartTNTFuse = 80;
      if (!this.world.isRemote) {
         this.world.setEntityState(this, (byte)10);
         if (!this.isSilent()) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }
      }
   }

   public int getFuseTicks() {
      return this.minecartTNTFuse;
   }

   public boolean isIgnited() {
      return this.minecartTNTFuse > -1;
   }

   @Override
   public float getExplosionResistance(Explosion var1, World var2, BlockPos var3, IBlockState var4) {
      return !this.isIgnited() || !BlockRailBase.isRailBlock(☃) && !BlockRailBase.isRailBlock(☃, ☃.up()) ? super.getExplosionResistance(☃, ☃, ☃, ☃) : 0.0F;
   }

   @Override
   public boolean canExplosionDestroyBlock(Explosion var1, World var2, BlockPos var3, IBlockState var4, float var5) {
      return !this.isIgnited() || !BlockRailBase.isRailBlock(☃) && !BlockRailBase.isRailBlock(☃, ☃.up())
         ? super.canExplosionDestroyBlock(☃, ☃, ☃, ☃, ☃)
         : false;
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("TNTFuse", 99)) {
         this.minecartTNTFuse = ☃.getInteger("TNTFuse");
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("TNTFuse", this.minecartTNTFuse);
   }
}
