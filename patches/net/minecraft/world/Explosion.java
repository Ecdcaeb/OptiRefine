package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Explosion {
   private final boolean causesFire;
   private final boolean damagesTerrain;
   private final Random random = new Random();
   private final World world;
   private final double x;
   private final double y;
   private final double z;
   private final Entity exploder;
   private final float size;
   private final List<BlockPos> affectedBlockPositions = Lists.newArrayList();
   private final Map<EntityPlayer, Vec3d> playerKnockbackMap = Maps.newHashMap();

   public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9, List<BlockPos> var10) {
      this(☃, ☃, ☃, ☃, ☃, ☃, false, true, ☃);
   }

   public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9, boolean var10, boolean var11, List<BlockPos> var12) {
      this(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.affectedBlockPositions.addAll(☃);
   }

   public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9, boolean var10, boolean var11) {
      this.world = ☃;
      this.exploder = ☃;
      this.size = ☃;
      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
      this.causesFire = ☃;
      this.damagesTerrain = ☃;
   }

   public void doExplosionA() {
      Set<BlockPos> ☃ = Sets.newHashSet();
      int ☃x = 16;

      for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
            for (int ☃xxxx = 0; ☃xxxx < 16; ☃xxxx++) {
               if (☃xx == 0 || ☃xx == 15 || ☃xxx == 0 || ☃xxx == 15 || ☃xxxx == 0 || ☃xxxx == 15) {
                  double ☃xxxxx = ☃xx / 15.0F * 2.0F - 1.0F;
                  double ☃xxxxxx = ☃xxx / 15.0F * 2.0F - 1.0F;
                  double ☃xxxxxxx = ☃xxxx / 15.0F * 2.0F - 1.0F;
                  double ☃xxxxxxxx = Math.sqrt(☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx + ☃xxxxxxx * ☃xxxxxxx);
                  ☃xxxxx /= ☃xxxxxxxx;
                  ☃xxxxxx /= ☃xxxxxxxx;
                  ☃xxxxxxx /= ☃xxxxxxxx;
                  float ☃xxxxxxxxx = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                  double ☃xxxxxxxxxx = this.x;
                  double ☃xxxxxxxxxxx = this.y;
                  double ☃xxxxxxxxxxxx = this.z;

                  for (float ☃xxxxxxxxxxxxx = 0.3F; ☃xxxxxxxxx > 0.0F; ☃xxxxxxxxx -= 0.22500001F) {
                     BlockPos ☃xxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
                     IBlockState ☃xxxxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxxxxx);
                     if (☃xxxxxxxxxxxxxxx.getMaterial() != Material.AIR) {
                        float ☃xxxxxxxxxxxxxxxx = this.exploder != null
                           ? this.exploder.getExplosionResistance(this, this.world, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx)
                           : ☃xxxxxxxxxxxxxxx.getBlock().getExplosionResistance(null);
                        ☃xxxxxxxxx -= (☃xxxxxxxxxxxxxxxx + 0.3F) * 0.3F;
                     }

                     if (☃xxxxxxxxx > 0.0F
                        && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxx))) {
                        ☃.add(☃xxxxxxxxxxxxxx);
                     }

                     ☃xxxxxxxxxx += ☃xxxxx * 0.3F;
                     ☃xxxxxxxxxxx += ☃xxxxxx * 0.3F;
                     ☃xxxxxxxxxxxx += ☃xxxxxxx * 0.3F;
                  }
               }
            }
         }
      }

      this.affectedBlockPositions.addAll(☃);
      float ☃xx = this.size * 2.0F;
      int ☃xxx = MathHelper.floor(this.x - ☃xx - 1.0);
      int ☃xxxxx = MathHelper.floor(this.x + ☃xx + 1.0);
      int ☃xxxxxx = MathHelper.floor(this.y - ☃xx - 1.0);
      int ☃xxxxxxx = MathHelper.floor(this.y + ☃xx + 1.0);
      int ☃xxxxxxxx = MathHelper.floor(this.z - ☃xx - 1.0);
      int ☃xxxxxxxxx = MathHelper.floor(this.z + ☃xx + 1.0);
      List<Entity> ☃xxxxxxxxxx = this.world
         .getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(☃xxx, ☃xxxxxx, ☃xxxxxxxx, ☃xxxxx, ☃xxxxxxx, ☃xxxxxxxxx));
      Vec3d ☃xxxxxxxxxxx = new Vec3d(this.x, this.y, this.z);

      for (int ☃xxxxxxxxxxxx = 0; ☃xxxxxxxxxxxx < ☃xxxxxxxxxx.size(); ☃xxxxxxxxxxxx++) {
         Entity ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx.get(☃xxxxxxxxxxxx);
         if (!☃xxxxxxxxxxxxx.isImmuneToExplosions()) {
            double ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.getDistance(this.x, this.y, this.z) / ☃xx;
            if (☃xxxxxxxxxxxxxxxx <= 1.0) {
               double ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.posX - this.x;
               double ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.posY + ☃xxxxxxxxxxxxx.getEyeHeight() - this.y;
               double ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx.posZ - this.z;
               double ☃xxxxxxxxxxxxxxxxxxxx = MathHelper.sqrt(
                  ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx
               );
               if (☃xxxxxxxxxxxxxxxxxxxx != 0.0) {
                  ☃xxxxxxxxxxxxxxxxx /= ☃xxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxx /= ☃xxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxxxxxxxx /= ☃xxxxxxxxxxxxxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxxx = this.world.getBlockDensity(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx.getEntityBoundingBox());
                  double ☃xxxxxxxxxxxxxxxxxxxxxx = (1.0 - ☃xxxxxxxxxxxxxxxx) * ☃xxxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxx.attackEntityFrom(
                     DamageSource.causeExplosionDamage(this),
                     (int)((☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxx) / 2.0 * 7.0 * ☃xx + 1.0)
                  );
                  double ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxx instanceof EntityLivingBase) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxx = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx);
                  }

                  ☃xxxxxxxxxxxxx.motionX += ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxx.motionY += ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxx;
                  ☃xxxxxxxxxxxxx.motionZ += ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxx instanceof EntityPlayer) {
                     EntityPlayer ☃xxxxxxxxxxxxxxxxxxxxxxxx = (EntityPlayer)☃xxxxxxxxxxxxx;
                     if (!☃xxxxxxxxxxxxxxxxxxxxxxxx.isSpectator()
                        && (!☃xxxxxxxxxxxxxxxxxxxxxxxx.isCreative() || !☃xxxxxxxxxxxxxxxxxxxxxxxx.capabilities.isFlying)) {
                        this.playerKnockbackMap
                           .put(
                              ☃xxxxxxxxxxxxxxxxxxxxxxxx,
                              new Vec3d(
                                 ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx,
                                 ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx,
                                 ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx
                              )
                           );
                     }
                  }
               }
            }
         }
      }
   }

   public void doExplosionB(boolean var1) {
      this.world
         .playSound(
            null,
            this.x,
            this.y,
            this.z,
            SoundEvents.ENTITY_GENERIC_EXPLODE,
            SoundCategory.BLOCKS,
            4.0F,
            (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F
         );
      if (!(this.size < 2.0F) && this.damagesTerrain) {
         this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.x, this.y, this.z, 1.0, 0.0, 0.0);
      } else {
         this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.x, this.y, this.z, 1.0, 0.0, 0.0);
      }

      if (this.damagesTerrain) {
         for (BlockPos ☃ : this.affectedBlockPositions) {
            IBlockState ☃x = this.world.getBlockState(☃);
            Block ☃xx = ☃x.getBlock();
            if (☃) {
               double ☃xxx = ☃.getX() + this.world.rand.nextFloat();
               double ☃xxxx = ☃.getY() + this.world.rand.nextFloat();
               double ☃xxxxx = ☃.getZ() + this.world.rand.nextFloat();
               double ☃xxxxxx = ☃xxx - this.x;
               double ☃xxxxxxx = ☃xxxx - this.y;
               double ☃xxxxxxxx = ☃xxxxx - this.z;
               double ☃xxxxxxxxx = MathHelper.sqrt(☃xxxxxx * ☃xxxxxx + ☃xxxxxxx * ☃xxxxxxx + ☃xxxxxxxx * ☃xxxxxxxx);
               ☃xxxxxx /= ☃xxxxxxxxx;
               ☃xxxxxxx /= ☃xxxxxxxxx;
               ☃xxxxxxxx /= ☃xxxxxxxxx;
               double ☃xxxxxxxxxx = 0.5 / (☃xxxxxxxxx / this.size + 0.1);
               ☃xxxxxxxxxx *= this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F;
               ☃xxxxxx *= ☃xxxxxxxxxx;
               ☃xxxxxxx *= ☃xxxxxxxxxx;
               ☃xxxxxxxx *= ☃xxxxxxxxxx;
               this.world
                  .spawnParticle(
                     EnumParticleTypes.EXPLOSION_NORMAL, (☃xxx + this.x) / 2.0, (☃xxxx + this.y) / 2.0, (☃xxxxx + this.z) / 2.0, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx
                  );
               this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
            }

            if (☃x.getMaterial() != Material.AIR) {
               if (☃xx.canDropFromExplosion(this)) {
                  ☃xx.dropBlockAsItemWithChance(this.world, ☃, this.world.getBlockState(☃), 1.0F / this.size, 0);
               }

               this.world.setBlockState(☃, Blocks.AIR.getDefaultState(), 3);
               ☃xx.onExplosionDestroy(this.world, ☃, this);
            }
         }
      }

      if (this.causesFire) {
         for (BlockPos ☃ : this.affectedBlockPositions) {
            if (this.world.getBlockState(☃).getMaterial() == Material.AIR && this.world.getBlockState(☃.down()).isFullBlock() && this.random.nextInt(3) == 0) {
               this.world.setBlockState(☃, Blocks.FIRE.getDefaultState());
            }
         }
      }
   }

   public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
      return this.playerKnockbackMap;
   }

   @Nullable
   public EntityLivingBase getExplosivePlacedBy() {
      if (this.exploder == null) {
         return null;
      } else if (this.exploder instanceof EntityTNTPrimed) {
         return ((EntityTNTPrimed)this.exploder).getTntPlacedBy();
      } else {
         return this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null;
      }
   }

   public void clearAffectedBlockPositions() {
      this.affectedBlockPositions.clear();
   }

   public List<BlockPos> getAffectedBlockPositions() {
      return this.affectedBlockPositions;
   }
}
