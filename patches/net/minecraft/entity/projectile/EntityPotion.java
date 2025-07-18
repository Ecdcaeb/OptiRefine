package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPotion extends EntityThrowable {
   private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityPotion.class, DataSerializers.ITEM_STACK);
   private static final Logger LOGGER = LogManager.getLogger();
   public static final Predicate<EntityLivingBase> WATER_SENSITIVE = new Predicate<EntityLivingBase>() {
      public boolean apply(@Nullable EntityLivingBase var1) {
         return EntityPotion.isWaterSensitiveEntity(☃);
      }
   };

   public EntityPotion(World var1) {
      super(☃);
   }

   public EntityPotion(World var1, EntityLivingBase var2, ItemStack var3) {
      super(☃, ☃);
      this.setItem(☃);
   }

   public EntityPotion(World var1, double var2, double var4, double var6, ItemStack var8) {
      super(☃, ☃, ☃, ☃);
      if (!☃.isEmpty()) {
         this.setItem(☃);
      }
   }

   @Override
   protected void entityInit() {
      this.getDataManager().register(ITEM, ItemStack.EMPTY);
   }

   public ItemStack getPotion() {
      ItemStack ☃ = this.getDataManager().get(ITEM);
      if (☃.getItem() != Items.SPLASH_POTION && ☃.getItem() != Items.LINGERING_POTION) {
         if (this.world != null) {
            LOGGER.error("ThrownPotion entity {} has no item?!", this.getEntityId());
         }

         return new ItemStack(Items.SPLASH_POTION);
      } else {
         return ☃;
      }
   }

   public void setItem(ItemStack var1) {
      this.getDataManager().set(ITEM, ☃);
      this.getDataManager().setDirty(ITEM);
   }

   @Override
   protected float getGravityVelocity() {
      return 0.05F;
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (!this.world.isRemote) {
         ItemStack ☃ = this.getPotion();
         PotionType ☃x = PotionUtils.getPotionFromItem(☃);
         List<PotionEffect> ☃xx = PotionUtils.getEffectsFromStack(☃);
         boolean ☃xxx = ☃x == PotionTypes.WATER && ☃xx.isEmpty();
         if (☃.typeOfHit == RayTraceResult.Type.BLOCK && ☃xxx) {
            BlockPos ☃xxxx = ☃.getBlockPos().offset(☃.sideHit);
            this.extinguishFires(☃xxxx, ☃.sideHit);

            for (EnumFacing ☃xxxxx : EnumFacing.Plane.HORIZONTAL) {
               this.extinguishFires(☃xxxx.offset(☃xxxxx), ☃xxxxx);
            }
         }

         if (☃xxx) {
            this.applyWater();
         } else if (!☃xx.isEmpty()) {
            if (this.isLingering()) {
               this.makeAreaOfEffectCloud(☃, ☃x);
            } else {
               this.applySplash(☃, ☃xx);
            }
         }

         int ☃xxxx = ☃x.hasInstantEffect() ? 2007 : 2002;
         this.world.playEvent(☃xxxx, new BlockPos(this), PotionUtils.getColor(☃));
         this.setDead();
      }
   }

   private void applyWater() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox().grow(4.0, 2.0, 4.0);
      List<EntityLivingBase> ☃x = this.world.getEntitiesWithinAABB(EntityLivingBase.class, ☃, WATER_SENSITIVE);
      if (!☃x.isEmpty()) {
         for (EntityLivingBase ☃xx : ☃x) {
            double ☃xxx = this.getDistanceSq(☃xx);
            if (☃xxx < 16.0 && isWaterSensitiveEntity(☃xx)) {
               ☃xx.attackEntityFrom(DamageSource.DROWN, 1.0F);
            }
         }
      }
   }

   private void applySplash(RayTraceResult var1, List<PotionEffect> var2) {
      AxisAlignedBB ☃ = this.getEntityBoundingBox().grow(4.0, 2.0, 4.0);
      List<EntityLivingBase> ☃x = this.world.getEntitiesWithinAABB(EntityLivingBase.class, ☃);
      if (!☃x.isEmpty()) {
         for (EntityLivingBase ☃xx : ☃x) {
            if (☃xx.canBeHitWithPotion()) {
               double ☃xxx = this.getDistanceSq(☃xx);
               if (☃xxx < 16.0) {
                  double ☃xxxx = 1.0 - Math.sqrt(☃xxx) / 4.0;
                  if (☃xx == ☃.entityHit) {
                     ☃xxxx = 1.0;
                  }

                  for (PotionEffect ☃xxxxx : ☃) {
                     Potion ☃xxxxxx = ☃xxxxx.getPotion();
                     if (☃xxxxxx.isInstant()) {
                        ☃xxxxxx.affectEntity(this, this.getThrower(), ☃xx, ☃xxxxx.getAmplifier(), ☃xxxx);
                     } else {
                        int ☃xxxxxxx = (int)(☃xxxx * ☃xxxxx.getDuration() + 0.5);
                        if (☃xxxxxxx > 20) {
                           ☃xx.addPotionEffect(new PotionEffect(☃xxxxxx, ☃xxxxxxx, ☃xxxxx.getAmplifier(), ☃xxxxx.getIsAmbient(), ☃xxxxx.doesShowParticles()));
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void makeAreaOfEffectCloud(ItemStack var1, PotionType var2) {
      EntityAreaEffectCloud ☃ = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
      ☃.setOwner(this.getThrower());
      ☃.setRadius(3.0F);
      ☃.setRadiusOnUse(-0.5F);
      ☃.setWaitTime(10);
      ☃.setRadiusPerTick(-☃.getRadius() / ☃.getDuration());
      ☃.setPotion(☃);

      for (PotionEffect ☃x : PotionUtils.getFullEffectsFromItem(☃)) {
         ☃.addEffect(new PotionEffect(☃x));
      }

      NBTTagCompound ☃x = ☃.getTagCompound();
      if (☃x != null && ☃x.hasKey("CustomPotionColor", 99)) {
         ☃.setColor(☃x.getInteger("CustomPotionColor"));
      }

      this.world.spawnEntity(☃);
   }

   private boolean isLingering() {
      return this.getPotion().getItem() == Items.LINGERING_POTION;
   }

   private void extinguishFires(BlockPos var1, EnumFacing var2) {
      if (this.world.getBlockState(☃).getBlock() == Blocks.FIRE) {
         this.world.extinguishFire(null, ☃.offset(☃), ☃.getOpposite());
      }
   }

   public static void registerFixesPotion(DataFixer var0) {
      EntityThrowable.registerFixesThrowable(☃, "ThrownPotion");
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityPotion.class, "Potion"));
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      ItemStack ☃ = new ItemStack(☃.getCompoundTag("Potion"));
      if (☃.isEmpty()) {
         this.setDead();
      } else {
         this.setItem(☃);
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ItemStack ☃ = this.getPotion();
      if (!☃.isEmpty()) {
         ☃.setTag("Potion", ☃.writeToNBT(new NBTTagCompound()));
      }
   }

   private static boolean isWaterSensitiveEntity(EntityLivingBase var0) {
      return ☃ instanceof EntityEnderman || ☃ instanceof EntityBlaze;
   }
}
