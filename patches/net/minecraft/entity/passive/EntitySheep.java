package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySheep extends EntityAnimal {
   private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
   private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container() {
      @Override
      public boolean canInteractWith(EntityPlayer var1) {
         return false;
      }
   }, 2, 1);
   private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
   private int sheepTimer;
   private EntityAIEatGrass entityAIEatGrass;

   private static float[] createSheepColor(EnumDyeColor var0) {
      float[] ☃ = ☃.getColorComponentValues();
      float ☃x = 0.75F;
      return new float[]{☃[0] * 0.75F, ☃[1] * 0.75F, ☃[2] * 0.75F};
   }

   public static float[] getDyeRgb(EnumDyeColor var0) {
      return DYE_TO_RGB.get(☃);
   }

   public EntitySheep(World var1) {
      super(☃);
      this.setSize(0.9F, 1.3F);
      this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.DYE));
      this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.DYE));
   }

   @Override
   protected void initEntityAI() {
      this.entityAIEatGrass = new EntityAIEatGrass(this);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
      this.tasks.addTask(2, new EntityAIMate(this, 1.0));
      this.tasks.addTask(3, new EntityAITempt(this, 1.1, Items.WHEAT, false));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
      this.tasks.addTask(5, this.entityAIEatGrass);
      this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
   }

   @Override
   protected void updateAITasks() {
      this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
      super.updateAITasks();
   }

   @Override
   public void onLivingUpdate() {
      if (this.world.isRemote) {
         this.sheepTimer = Math.max(0, this.sheepTimer - 1);
      }

      super.onLivingUpdate();
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23F);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(DYE_COLOR, (byte)0);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      if (this.getSheared()) {
         return LootTableList.ENTITIES_SHEEP;
      } else {
         switch (this.getFleeceColor()) {
            case WHITE:
            default:
               return LootTableList.ENTITIES_SHEEP_WHITE;
            case ORANGE:
               return LootTableList.ENTITIES_SHEEP_ORANGE;
            case MAGENTA:
               return LootTableList.ENTITIES_SHEEP_MAGENTA;
            case LIGHT_BLUE:
               return LootTableList.ENTITIES_SHEEP_LIGHT_BLUE;
            case YELLOW:
               return LootTableList.ENTITIES_SHEEP_YELLOW;
            case LIME:
               return LootTableList.ENTITIES_SHEEP_LIME;
            case PINK:
               return LootTableList.ENTITIES_SHEEP_PINK;
            case GRAY:
               return LootTableList.ENTITIES_SHEEP_GRAY;
            case SILVER:
               return LootTableList.ENTITIES_SHEEP_SILVER;
            case CYAN:
               return LootTableList.ENTITIES_SHEEP_CYAN;
            case PURPLE:
               return LootTableList.ENTITIES_SHEEP_PURPLE;
            case BLUE:
               return LootTableList.ENTITIES_SHEEP_BLUE;
            case BROWN:
               return LootTableList.ENTITIES_SHEEP_BROWN;
            case GREEN:
               return LootTableList.ENTITIES_SHEEP_GREEN;
            case RED:
               return LootTableList.ENTITIES_SHEEP_RED;
            case BLACK:
               return LootTableList.ENTITIES_SHEEP_BLACK;
         }
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 10) {
         this.sheepTimer = 40;
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   public float getHeadRotationPointY(float var1) {
      if (this.sheepTimer <= 0) {
         return 0.0F;
      } else if (this.sheepTimer >= 4 && this.sheepTimer <= 36) {
         return 1.0F;
      } else {
         return this.sheepTimer < 4 ? (this.sheepTimer - ☃) / 4.0F : -(this.sheepTimer - 40 - ☃) / 4.0F;
      }
   }

   public float getHeadRotationAngleX(float var1) {
      if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
         float ☃ = (this.sheepTimer - 4 - ☃) / 32.0F;
         return (float) (Math.PI / 5) + 0.21991149F * MathHelper.sin(☃ * 28.7F);
      } else {
         return this.sheepTimer > 0 ? (float) (Math.PI / 5) : this.rotationPitch * (float) (Math.PI / 180.0);
      }
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.SHEARS && !this.getSheared() && !this.isChild()) {
         if (!this.world.isRemote) {
            this.setSheared(true);
            int ☃x = 1 + this.rand.nextInt(3);

            for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
               EntityItem ☃xxx = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, this.getFleeceColor().getMetadata()), 1.0F);
               ☃xxx.motionY = ☃xxx.motionY + this.rand.nextFloat() * 0.05F;
               ☃xxx.motionX = ☃xxx.motionX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
               ☃xxx.motionZ = ☃xxx.motionZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            }
         }

         ☃.damageItem(1, ☃);
         this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
      }

      return super.processInteract(☃, ☃);
   }

   public static void registerFixesSheep(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntitySheep.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("Sheared", this.getSheared());
      ☃.setByte("Color", (byte)this.getFleeceColor().getMetadata());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setSheared(☃.getBoolean("Sheared"));
      this.setFleeceColor(EnumDyeColor.byMetadata(☃.getByte("Color")));
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SHEEP_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_SHEEP_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SHEEP_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
   }

   public EnumDyeColor getFleeceColor() {
      return EnumDyeColor.byMetadata(this.dataManager.get(DYE_COLOR) & 15);
   }

   public void setFleeceColor(EnumDyeColor var1) {
      byte ☃ = this.dataManager.get(DYE_COLOR);
      this.dataManager.set(DYE_COLOR, (byte)(☃ & 240 | ☃.getMetadata() & 15));
   }

   public boolean getSheared() {
      return (this.dataManager.get(DYE_COLOR) & 16) != 0;
   }

   public void setSheared(boolean var1) {
      byte ☃ = this.dataManager.get(DYE_COLOR);
      if (☃) {
         this.dataManager.set(DYE_COLOR, (byte)(☃ | 16));
      } else {
         this.dataManager.set(DYE_COLOR, (byte)(☃ & -17));
      }
   }

   public static EnumDyeColor getRandomSheepColor(Random var0) {
      int ☃ = ☃.nextInt(100);
      if (☃ < 5) {
         return EnumDyeColor.BLACK;
      } else if (☃ < 10) {
         return EnumDyeColor.GRAY;
      } else if (☃ < 15) {
         return EnumDyeColor.SILVER;
      } else if (☃ < 18) {
         return EnumDyeColor.BROWN;
      } else {
         return ☃.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE;
      }
   }

   public EntitySheep createChild(EntityAgeable var1) {
      EntitySheep ☃ = (EntitySheep)☃;
      EntitySheep ☃x = new EntitySheep(this.world);
      ☃x.setFleeceColor(this.getDyeColorMixFromParents(this, ☃));
      return ☃x;
   }

   @Override
   public void eatGrassBonus() {
      this.setSheared(false);
      if (this.isChild()) {
         this.addGrowth(60);
      }
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      this.setFleeceColor(getRandomSheepColor(this.world.rand));
      return ☃;
   }

   private EnumDyeColor getDyeColorMixFromParents(EntityAnimal var1, EntityAnimal var2) {
      int ☃ = ((EntitySheep)☃).getFleeceColor().getDyeDamage();
      int ☃x = ((EntitySheep)☃).getFleeceColor().getDyeDamage();
      this.inventoryCrafting.getStackInSlot(0).setItemDamage(☃);
      this.inventoryCrafting.getStackInSlot(1).setItemDamage(☃x);
      ItemStack ☃xx = CraftingManager.findMatchingResult(this.inventoryCrafting, ((EntitySheep)☃).world);
      int ☃xxx;
      if (☃xx.getItem() == Items.DYE) {
         ☃xxx = ☃xx.getMetadata();
      } else {
         ☃xxx = this.world.rand.nextBoolean() ? ☃ : ☃x;
      }

      return EnumDyeColor.byDyeDamage(☃xxx);
   }

   @Override
   public float getEyeHeight() {
      return 0.95F * this.height;
   }

   static {
      for (EnumDyeColor ☃ : EnumDyeColor.values()) {
         DYE_TO_RGB.put(☃, createSheepColor(☃));
      }

      DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[]{0.9019608F, 0.9019608F, 0.9019608F});
   }
}
