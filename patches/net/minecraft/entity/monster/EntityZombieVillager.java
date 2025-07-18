package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieVillager extends EntityZombie {
   private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.VARINT);
   private int conversionTime;
   private UUID converstionStarter;

   public EntityZombieVillager(World var1) {
      super(☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(CONVERTING, false);
      this.dataManager.register(PROFESSION, 0);
   }

   public void setProfession(int var1) {
      this.dataManager.set(PROFESSION, ☃);
   }

   public int getProfession() {
      return Math.max(this.dataManager.get(PROFESSION) % 6, 0);
   }

   public static void registerFixesZombieVillager(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityZombieVillager.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Profession", this.getProfession());
      ☃.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
      if (this.converstionStarter != null) {
         ☃.setUniqueId("ConversionPlayer", this.converstionStarter);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setProfession(☃.getInteger("Profession"));
      if (☃.hasKey("ConversionTime", 99) && ☃.getInteger("ConversionTime") > -1) {
         this.startConverting(☃.hasUniqueId("ConversionPlayer") ? ☃.getUniqueId("ConversionPlayer") : null, ☃.getInteger("ConversionTime"));
      }
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      this.setProfession(this.world.rand.nextInt(6));
      return super.onInitialSpawn(☃, ☃);
   }

   @Override
   public void onUpdate() {
      if (!this.world.isRemote && this.isConverting()) {
         int ☃ = this.getConversionProgress();
         this.conversionTime -= ☃;
         if (this.conversionTime <= 0) {
            this.finishConversion();
         }
      }

      super.onUpdate();
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.GOLDEN_APPLE && ☃.getMetadata() == 0 && this.isPotionActive(MobEffects.WEAKNESS)) {
         if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }

         if (!this.world.isRemote) {
            this.startConverting(☃.getUniqueID(), this.rand.nextInt(2401) + 3600);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   protected boolean canDespawn() {
      return !this.isConverting();
   }

   public boolean isConverting() {
      return this.getDataManager().get(CONVERTING);
   }

   protected void startConverting(@Nullable UUID var1, int var2) {
      this.converstionStarter = ☃;
      this.conversionTime = ☃;
      this.getDataManager().set(CONVERTING, true);
      this.removePotionEffect(MobEffects.WEAKNESS);
      this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, ☃, Math.min(this.world.getDifficulty().getId() - 1, 0)));
      this.world.setEntityState(this, (byte)16);
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 16) {
         if (!this.isSilent()) {
            this.world
               .playSound(
                  this.posX + 0.5,
                  this.posY + 0.5,
                  this.posZ + 0.5,
                  SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE,
                  this.getSoundCategory(),
                  1.0F + this.rand.nextFloat(),
                  this.rand.nextFloat() * 0.7F + 0.3F,
                  false
               );
         }
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   protected void finishConversion() {
      EntityVillager ☃ = new EntityVillager(this.world);
      ☃.copyLocationAndAnglesFrom(this);
      ☃.setProfession(this.getProfession());
      ☃.finalizeMobSpawn(this.world.getDifficultyForLocation(new BlockPos(☃)), null, false);
      ☃.setLookingForHome();
      if (this.isChild()) {
         ☃.setGrowingAge(-24000);
      }

      this.world.removeEntity(this);
      ☃.setNoAI(this.isAIDisabled());
      if (this.hasCustomName()) {
         ☃.setCustomNameTag(this.getCustomNameTag());
         ☃.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
      }

      this.world.spawnEntity(☃);
      if (this.converstionStarter != null) {
         EntityPlayer ☃x = this.world.getPlayerEntityByUUID(this.converstionStarter);
         if (☃x instanceof EntityPlayerMP) {
            CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((EntityPlayerMP)☃x, this, ☃);
         }
      }

      ☃.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
      this.world.playEvent(null, 1027, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
   }

   protected int getConversionProgress() {
      int ☃ = 1;
      if (this.rand.nextFloat() < 0.01F) {
         int ☃x = 0;
         BlockPos.MutableBlockPos ☃xx = new BlockPos.MutableBlockPos();

         for (int ☃xxx = (int)this.posX - 4; ☃xxx < (int)this.posX + 4 && ☃x < 14; ☃xxx++) {
            for (int ☃xxxx = (int)this.posY - 4; ☃xxxx < (int)this.posY + 4 && ☃x < 14; ☃xxxx++) {
               for (int ☃xxxxx = (int)this.posZ - 4; ☃xxxxx < (int)this.posZ + 4 && ☃x < 14; ☃xxxxx++) {
                  Block ☃xxxxxx = this.world.getBlockState(☃xx.setPos(☃xxx, ☃xxxx, ☃xxxxx)).getBlock();
                  if (☃xxxxxx == Blocks.IRON_BARS || ☃xxxxxx == Blocks.BED) {
                     if (this.rand.nextFloat() < 0.3F) {
                        ☃++;
                     }

                     ☃x++;
                  }
               }
            }
         }
      }

      return ☃;
   }

   @Override
   protected float getSoundPitch() {
      return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 2.0F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
   }

   @Override
   public SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
   }

   @Override
   public SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT;
   }

   @Override
   public SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH;
   }

   @Override
   public SoundEvent getStepSound() {
      return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_ZOMBIE_VILLAGER;
   }

   @Override
   protected ItemStack getSkullDrop() {
      return ItemStack.EMPTY;
   }
}
