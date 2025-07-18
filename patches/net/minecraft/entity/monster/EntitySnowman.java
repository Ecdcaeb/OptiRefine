package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
   private static final DataParameter<Byte> PUMPKIN_EQUIPPED = EntityDataManager.createKey(EntitySnowman.class, DataSerializers.BYTE);

   public EntitySnowman(World var1) {
      super(☃);
      this.setSize(0.7F, 1.9F);
   }

   public static void registerFixesSnowman(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntitySnowman.class);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25, 20, 10.0F));
      this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0, 1.0000001E-5F));
      this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(4, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(PUMPKIN_EQUIPPED, (byte)16);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("Pumpkin", this.isPumpkinEquipped());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("Pumpkin")) {
         this.setPumpkinEquipped(☃.getBoolean("Pumpkin"));
      }
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (!this.world.isRemote) {
         int ☃ = MathHelper.floor(this.posX);
         int ☃x = MathHelper.floor(this.posY);
         int ☃xx = MathHelper.floor(this.posZ);
         if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
         }

         if (this.world.getBiome(new BlockPos(☃, 0, ☃xx)).getTemperature(new BlockPos(☃, ☃x, ☃xx)) > 1.0F) {
            this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
         }

         if (!this.world.getGameRules().getBoolean("mobGriefing")) {
            return;
         }

         for (int ☃xxx = 0; ☃xxx < 4; ☃xxx++) {
            ☃ = MathHelper.floor(this.posX + (☃xxx % 2 * 2 - 1) * 0.25F);
            ☃x = MathHelper.floor(this.posY);
            ☃xx = MathHelper.floor(this.posZ + (☃xxx / 2 % 2 * 2 - 1) * 0.25F);
            BlockPos ☃xxxx = new BlockPos(☃, ☃x, ☃xx);
            if (this.world.getBlockState(☃xxxx).getMaterial() == Material.AIR
               && this.world.getBiome(☃xxxx).getTemperature(☃xxxx) < 0.8F
               && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, ☃xxxx)) {
               this.world.setBlockState(☃xxxx, Blocks.SNOW_LAYER.getDefaultState());
            }
         }
      }
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_SNOWMAN;
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      EntitySnowball ☃ = new EntitySnowball(this.world, this);
      double ☃x = ☃.posY + ☃.getEyeHeight() - 1.1F;
      double ☃xx = ☃.posX - this.posX;
      double ☃xxx = ☃x - ☃.posY;
      double ☃xxxx = ☃.posZ - this.posZ;
      float ☃xxxxx = MathHelper.sqrt(☃xx * ☃xx + ☃xxxx * ☃xxxx) * 0.2F;
      ☃.shoot(☃xx, ☃xxx + ☃xxxxx, ☃xxxx, 1.6F, 12.0F);
      this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
      this.world.spawnEntity(☃);
   }

   @Override
   public float getEyeHeight() {
      return 1.7F;
   }

   @Override
   protected boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.SHEARS && this.isPumpkinEquipped() && !this.world.isRemote) {
         this.setPumpkinEquipped(false);
         ☃.damageItem(1, ☃);
      }

      return super.processInteract(☃, ☃);
   }

   public boolean isPumpkinEquipped() {
      return (this.dataManager.get(PUMPKIN_EQUIPPED) & 16) != 0;
   }

   public void setPumpkinEquipped(boolean var1) {
      byte ☃ = this.dataManager.get(PUMPKIN_EQUIPPED);
      if (☃) {
         this.dataManager.set(PUMPKIN_EQUIPPED, (byte)(☃ | 16));
      } else {
         this.dataManager.set(PUMPKIN_EQUIPPED, (byte)(☃ & -17));
      }
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
   }

   @Nullable
   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_SNOWMAN_HURT;
   }

   @Nullable
   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SNOWMAN_DEATH;
   }

   @Override
   public void setSwingingArms(boolean var1) {
   }
}
