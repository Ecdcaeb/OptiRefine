package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAreaEffectCloud extends Entity {
   private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.FLOAT);
   private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
   private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> PARTICLE = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> PARTICLE_PARAM_1 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> PARTICLE_PARAM_2 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
   private PotionType potion = PotionTypes.EMPTY;
   private final List<PotionEffect> effects = Lists.newArrayList();
   private final Map<Entity, Integer> reapplicationDelayMap = Maps.newHashMap();
   private int duration = 600;
   private int waitTime = 20;
   private int reapplicationDelay = 20;
   private boolean colorSet;
   private int durationOnUse;
   private float radiusOnUse;
   private float radiusPerTick;
   private EntityLivingBase owner;
   private UUID ownerUniqueId;

   public EntityAreaEffectCloud(World var1) {
      super(☃);
      this.noClip = true;
      this.isImmuneToFire = true;
      this.setRadius(3.0F);
   }

   public EntityAreaEffectCloud(World var1, double var2, double var4, double var6) {
      this(☃);
      this.setPosition(☃, ☃, ☃);
   }

   @Override
   protected void entityInit() {
      this.getDataManager().register(COLOR, 0);
      this.getDataManager().register(RADIUS, 0.5F);
      this.getDataManager().register(IGNORE_RADIUS, false);
      this.getDataManager().register(PARTICLE, EnumParticleTypes.SPELL_MOB.getParticleID());
      this.getDataManager().register(PARTICLE_PARAM_1, 0);
      this.getDataManager().register(PARTICLE_PARAM_2, 0);
   }

   public void setRadius(float var1) {
      double ☃ = this.posX;
      double ☃x = this.posY;
      double ☃xx = this.posZ;
      this.setSize(☃ * 2.0F, 0.5F);
      this.setPosition(☃, ☃x, ☃xx);
      if (!this.world.isRemote) {
         this.getDataManager().set(RADIUS, ☃);
      }
   }

   public float getRadius() {
      return this.getDataManager().get(RADIUS);
   }

   public void setPotion(PotionType var1) {
      this.potion = ☃;
      if (!this.colorSet) {
         this.updateFixedColor();
      }
   }

   private void updateFixedColor() {
      if (this.potion == PotionTypes.EMPTY && this.effects.isEmpty()) {
         this.getDataManager().set(COLOR, 0);
      } else {
         this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects)));
      }
   }

   public void addEffect(PotionEffect var1) {
      this.effects.add(☃);
      if (!this.colorSet) {
         this.updateFixedColor();
      }
   }

   public int getColor() {
      return this.getDataManager().get(COLOR);
   }

   public void setColor(int var1) {
      this.colorSet = true;
      this.getDataManager().set(COLOR, ☃);
   }

   public EnumParticleTypes getParticle() {
      return EnumParticleTypes.getParticleFromId(this.getDataManager().get(PARTICLE));
   }

   public void setParticle(EnumParticleTypes var1) {
      this.getDataManager().set(PARTICLE, ☃.getParticleID());
   }

   public int getParticleParam1() {
      return this.getDataManager().get(PARTICLE_PARAM_1);
   }

   public void setParticleParam1(int var1) {
      this.getDataManager().set(PARTICLE_PARAM_1, ☃);
   }

   public int getParticleParam2() {
      return this.getDataManager().get(PARTICLE_PARAM_2);
   }

   public void setParticleParam2(int var1) {
      this.getDataManager().set(PARTICLE_PARAM_2, ☃);
   }

   protected void setIgnoreRadius(boolean var1) {
      this.getDataManager().set(IGNORE_RADIUS, ☃);
   }

   public boolean shouldIgnoreRadius() {
      return this.getDataManager().get(IGNORE_RADIUS);
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int var1) {
      this.duration = ☃;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      boolean ☃ = this.shouldIgnoreRadius();
      float ☃x = this.getRadius();
      if (this.world.isRemote) {
         EnumParticleTypes ☃xx = this.getParticle();
         int[] ☃xxx = new int[☃xx.getArgumentCount()];
         if (☃xxx.length > 0) {
            ☃xxx[0] = this.getParticleParam1();
         }

         if (☃xxx.length > 1) {
            ☃xxx[1] = this.getParticleParam2();
         }

         if (☃) {
            if (this.rand.nextBoolean()) {
               for (int ☃xxxx = 0; ☃xxxx < 2; ☃xxxx++) {
                  float ☃xxxxx = this.rand.nextFloat() * (float) (Math.PI * 2);
                  float ☃xxxxxx = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                  float ☃xxxxxxx = MathHelper.cos(☃xxxxx) * ☃xxxxxx;
                  float ☃xxxxxxxx = MathHelper.sin(☃xxxxx) * ☃xxxxxx;
                  if (☃xx == EnumParticleTypes.SPELL_MOB) {
                     int ☃xxxxxxxxx = this.rand.nextBoolean() ? 16777215 : this.getColor();
                     int ☃xxxxxxxxxx = ☃xxxxxxxxx >> 16 & 0xFF;
                     int ☃xxxxxxxxxxx = ☃xxxxxxxxx >> 8 & 0xFF;
                     int ☃xxxxxxxxxxxx = ☃xxxxxxxxx & 0xFF;
                     this.world
                        .spawnAlwaysVisibleParticle(
                           EnumParticleTypes.SPELL_MOB.getParticleID(),
                           this.posX + ☃xxxxxxx,
                           this.posY,
                           this.posZ + ☃xxxxxxxx,
                           ☃xxxxxxxxxx / 255.0F,
                           ☃xxxxxxxxxxx / 255.0F,
                           ☃xxxxxxxxxxxx / 255.0F
                        );
                  } else {
                     this.world.spawnAlwaysVisibleParticle(☃xx.getParticleID(), this.posX + ☃xxxxxxx, this.posY, this.posZ + ☃xxxxxxxx, 0.0, 0.0, 0.0, ☃xxx);
                  }
               }
            }
         } else {
            float ☃xxxxx = (float) Math.PI * ☃x * ☃x;

            for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx; ☃xxxxxx++) {
               float ☃xxxxxxx = this.rand.nextFloat() * (float) (Math.PI * 2);
               float ☃xxxxxxxx = MathHelper.sqrt(this.rand.nextFloat()) * ☃x;
               float ☃xxxxxxxxx = MathHelper.cos(☃xxxxxxx) * ☃xxxxxxxx;
               float ☃xxxxxxxxxx = MathHelper.sin(☃xxxxxxx) * ☃xxxxxxxx;
               if (☃xx == EnumParticleTypes.SPELL_MOB) {
                  int ☃xxxxxxxxxxx = this.getColor();
                  int ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx >> 16 & 0xFF;
                  int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx >> 8 & 0xFF;
                  int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxx & 0xFF;
                  this.world
                     .spawnAlwaysVisibleParticle(
                        EnumParticleTypes.SPELL_MOB.getParticleID(),
                        this.posX + ☃xxxxxxxxx,
                        this.posY,
                        this.posZ + ☃xxxxxxxxxx,
                        ☃xxxxxxxxxxxx / 255.0F,
                        ☃xxxxxxxxxxxxx / 255.0F,
                        ☃xxxxxxxxxxxxxx / 255.0F
                     );
               } else {
                  this.world
                     .spawnAlwaysVisibleParticle(
                        ☃xx.getParticleID(),
                        this.posX + ☃xxxxxxxxx,
                        this.posY,
                        this.posZ + ☃xxxxxxxxxx,
                        (0.5 - this.rand.nextDouble()) * 0.15,
                        0.01F,
                        (0.5 - this.rand.nextDouble()) * 0.15,
                        ☃xxx
                     );
               }
            }
         }
      } else {
         if (this.ticksExisted >= this.waitTime + this.duration) {
            this.setDead();
            return;
         }

         boolean ☃xxxxx = this.ticksExisted < this.waitTime;
         if (☃ != ☃xxxxx) {
            this.setIgnoreRadius(☃xxxxx);
         }

         if (☃xxxxx) {
            return;
         }

         if (this.radiusPerTick != 0.0F) {
            ☃x += this.radiusPerTick;
            if (☃x < 0.5F) {
               this.setDead();
               return;
            }

            this.setRadius(☃x);
         }

         if (this.ticksExisted % 5 == 0) {
            Iterator<Entry<Entity, Integer>> ☃xxxxxxx = this.reapplicationDelayMap.entrySet().iterator();

            while (☃xxxxxxx.hasNext()) {
               Entry<Entity, Integer> ☃xxxxxxxx = ☃xxxxxxx.next();
               if (this.ticksExisted >= ☃xxxxxxxx.getValue()) {
                  ☃xxxxxxx.remove();
               }
            }

            List<PotionEffect> ☃xxxxxxxx = Lists.newArrayList();

            for (PotionEffect ☃xxxxxxxxx : this.potion.getEffects()) {
               ☃xxxxxxxx.add(
                  new PotionEffect(
                     ☃xxxxxxxxx.getPotion(), ☃xxxxxxxxx.getDuration() / 4, ☃xxxxxxxxx.getAmplifier(), ☃xxxxxxxxx.getIsAmbient(), ☃xxxxxxxxx.doesShowParticles()
                  )
               );
            }

            ☃xxxxxxxx.addAll(this.effects);
            if (☃xxxxxxxx.isEmpty()) {
               this.reapplicationDelayMap.clear();
            } else {
               List<EntityLivingBase> ☃xxxxxxxxx = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());
               if (!☃xxxxxxxxx.isEmpty()) {
                  for (EntityLivingBase ☃xxxxxxxxxx : ☃xxxxxxxxx) {
                     if (!this.reapplicationDelayMap.containsKey(☃xxxxxxxxxx) && ☃xxxxxxxxxx.canBeHitWithPotion()) {
                        double ☃xxxxxxxxxxx = ☃xxxxxxxxxx.posX - this.posX;
                        double ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.posZ - this.posZ;
                        double ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx;
                        if (☃xxxxxxxxxxxxx <= ☃x * ☃x) {
                           this.reapplicationDelayMap.put(☃xxxxxxxxxx, this.ticksExisted + this.reapplicationDelay);

                           for (PotionEffect ☃xxxxxxxxxxxxxx : ☃xxxxxxxx) {
                              if (☃xxxxxxxxxxxxxx.getPotion().isInstant()) {
                                 ☃xxxxxxxxxxxxxx.getPotion().affectEntity(this, this.getOwner(), ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx.getAmplifier(), 0.5);
                              } else {
                                 ☃xxxxxxxxxx.addPotionEffect(new PotionEffect(☃xxxxxxxxxxxxxx));
                              }
                           }

                           if (this.radiusOnUse != 0.0F) {
                              ☃x += this.radiusOnUse;
                              if (☃x < 0.5F) {
                                 this.setDead();
                                 return;
                              }

                              this.setRadius(☃x);
                           }

                           if (this.durationOnUse != 0) {
                              this.duration = this.duration + this.durationOnUse;
                              if (this.duration <= 0) {
                                 this.setDead();
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void setRadiusOnUse(float var1) {
      this.radiusOnUse = ☃;
   }

   public void setRadiusPerTick(float var1) {
      this.radiusPerTick = ☃;
   }

   public void setWaitTime(int var1) {
      this.waitTime = ☃;
   }

   public void setOwner(@Nullable EntityLivingBase var1) {
      this.owner = ☃;
      this.ownerUniqueId = ☃ == null ? null : ☃.getUniqueID();
   }

   @Nullable
   public EntityLivingBase getOwner() {
      if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer) {
         Entity ☃ = ((WorldServer)this.world).getEntityFromUuid(this.ownerUniqueId);
         if (☃ instanceof EntityLivingBase) {
            this.owner = (EntityLivingBase)☃;
         }
      }

      return this.owner;
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      this.ticksExisted = ☃.getInteger("Age");
      this.duration = ☃.getInteger("Duration");
      this.waitTime = ☃.getInteger("WaitTime");
      this.reapplicationDelay = ☃.getInteger("ReapplicationDelay");
      this.durationOnUse = ☃.getInteger("DurationOnUse");
      this.radiusOnUse = ☃.getFloat("RadiusOnUse");
      this.radiusPerTick = ☃.getFloat("RadiusPerTick");
      this.setRadius(☃.getFloat("Radius"));
      this.ownerUniqueId = ☃.getUniqueId("OwnerUUID");
      if (☃.hasKey("Particle", 8)) {
         EnumParticleTypes ☃ = EnumParticleTypes.getByName(☃.getString("Particle"));
         if (☃ != null) {
            this.setParticle(☃);
            this.setParticleParam1(☃.getInteger("ParticleParam1"));
            this.setParticleParam2(☃.getInteger("ParticleParam2"));
         }
      }

      if (☃.hasKey("Color", 99)) {
         this.setColor(☃.getInteger("Color"));
      }

      if (☃.hasKey("Potion", 8)) {
         this.setPotion(PotionUtils.getPotionTypeFromNBT(☃));
      }

      if (☃.hasKey("Effects", 9)) {
         NBTTagList ☃ = ☃.getTagList("Effects", 10);
         this.effects.clear();

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            PotionEffect ☃xx = PotionEffect.readCustomPotionEffectFromNBT(☃.getCompoundTagAt(☃x));
            if (☃xx != null) {
               this.addEffect(☃xx);
            }
         }
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setInteger("Age", this.ticksExisted);
      ☃.setInteger("Duration", this.duration);
      ☃.setInteger("WaitTime", this.waitTime);
      ☃.setInteger("ReapplicationDelay", this.reapplicationDelay);
      ☃.setInteger("DurationOnUse", this.durationOnUse);
      ☃.setFloat("RadiusOnUse", this.radiusOnUse);
      ☃.setFloat("RadiusPerTick", this.radiusPerTick);
      ☃.setFloat("Radius", this.getRadius());
      ☃.setString("Particle", this.getParticle().getParticleName());
      ☃.setInteger("ParticleParam1", this.getParticleParam1());
      ☃.setInteger("ParticleParam2", this.getParticleParam2());
      if (this.ownerUniqueId != null) {
         ☃.setUniqueId("OwnerUUID", this.ownerUniqueId);
      }

      if (this.colorSet) {
         ☃.setInteger("Color", this.getColor());
      }

      if (this.potion != PotionTypes.EMPTY && this.potion != null) {
         ☃.setString("Potion", PotionType.REGISTRY.getNameForObject(this.potion).toString());
      }

      if (!this.effects.isEmpty()) {
         NBTTagList ☃ = new NBTTagList();

         for (PotionEffect ☃x : this.effects) {
            ☃.appendTag(☃x.writeCustomPotionEffectToNBT(new NBTTagCompound()));
         }

         ☃.setTag("Effects", ☃);
      }
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (RADIUS.equals(☃)) {
         this.setRadius(this.getRadius());
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   public EnumPushReaction getPushReaction() {
      return EnumPushReaction.IGNORE;
   }
}
