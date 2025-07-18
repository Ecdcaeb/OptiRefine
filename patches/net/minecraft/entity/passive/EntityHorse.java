package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
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
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHorse extends AbstractHorse {
   private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
   private static final DataParameter<Integer> HORSE_VARIANT = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> HORSE_ARMOR = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
   private static final String[] HORSE_TEXTURES = new String[]{
      "textures/entity/horse/horse_white.png",
      "textures/entity/horse/horse_creamy.png",
      "textures/entity/horse/horse_chestnut.png",
      "textures/entity/horse/horse_brown.png",
      "textures/entity/horse/horse_black.png",
      "textures/entity/horse/horse_gray.png",
      "textures/entity/horse/horse_darkbrown.png"
   };
   private static final String[] HORSE_TEXTURES_ABBR = new String[]{"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
   private static final String[] HORSE_MARKING_TEXTURES = new String[]{
      null,
      "textures/entity/horse/horse_markings_white.png",
      "textures/entity/horse/horse_markings_whitefield.png",
      "textures/entity/horse/horse_markings_whitedots.png",
      "textures/entity/horse/horse_markings_blackdots.png"
   };
   private static final String[] HORSE_MARKING_TEXTURES_ABBR = new String[]{"", "wo_", "wmo", "wdo", "bdo"};
   private String texturePrefix;
   private final String[] horseTexturesArray = new String[3];

   public EntityHorse(World var1) {
      super(☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(HORSE_VARIANT, 0);
      this.dataManager.register(HORSE_ARMOR, HorseArmorType.NONE.getOrdinal());
   }

   public static void registerFixesHorse(DataFixer var0) {
      AbstractHorse.registerFixesAbstractHorse(☃, EntityHorse.class);
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityHorse.class, "ArmorItem"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Variant", this.getHorseVariant());
      if (!this.horseChest.getStackInSlot(1).isEmpty()) {
         ☃.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setHorseVariant(☃.getInteger("Variant"));
      if (☃.hasKey("ArmorItem", 10)) {
         ItemStack ☃ = new ItemStack(☃.getCompoundTag("ArmorItem"));
         if (!☃.isEmpty() && HorseArmorType.isHorseArmor(☃.getItem())) {
            this.horseChest.setInventorySlotContents(1, ☃);
         }
      }

      this.updateHorseSlots();
   }

   public void setHorseVariant(int var1) {
      this.dataManager.set(HORSE_VARIANT, ☃);
      this.resetTexturePrefix();
   }

   public int getHorseVariant() {
      return this.dataManager.get(HORSE_VARIANT);
   }

   private void resetTexturePrefix() {
      this.texturePrefix = null;
   }

   private void setHorseTexturePaths() {
      int ☃ = this.getHorseVariant();
      int ☃x = (☃ & 0xFF) % 7;
      int ☃xx = ((☃ & 0xFF00) >> 8) % 5;
      HorseArmorType ☃xxx = this.getHorseArmorType();
      this.horseTexturesArray[0] = HORSE_TEXTURES[☃x];
      this.horseTexturesArray[1] = HORSE_MARKING_TEXTURES[☃xx];
      this.horseTexturesArray[2] = ☃xxx.getTextureName();
      this.texturePrefix = "horse/" + HORSE_TEXTURES_ABBR[☃x] + HORSE_MARKING_TEXTURES_ABBR[☃xx] + ☃xxx.getHash();
   }

   public String getHorseTexture() {
      if (this.texturePrefix == null) {
         this.setHorseTexturePaths();
      }

      return this.texturePrefix;
   }

   public String[] getVariantTexturePaths() {
      if (this.texturePrefix == null) {
         this.setHorseTexturePaths();
      }

      return this.horseTexturesArray;
   }

   @Override
   protected void updateHorseSlots() {
      super.updateHorseSlots();
      this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
   }

   public void setHorseArmorStack(ItemStack var1) {
      HorseArmorType ☃ = HorseArmorType.getByItemStack(☃);
      this.dataManager.set(HORSE_ARMOR, ☃.getOrdinal());
      this.resetTexturePrefix();
      if (!this.world.isRemote) {
         this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
         int ☃x = ☃.getProtection();
         if (☃x != 0) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR)
               .applyModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", ☃x, 0).setSaved(false));
         }
      }
   }

   public HorseArmorType getHorseArmorType() {
      return HorseArmorType.getByOrdinal(this.dataManager.get(HORSE_ARMOR));
   }

   @Override
   public void onInventoryChanged(IInventory var1) {
      HorseArmorType ☃ = this.getHorseArmorType();
      super.onInventoryChanged(☃);
      HorseArmorType ☃x = this.getHorseArmorType();
      if (this.ticksExisted > 20 && ☃ != ☃x && ☃x != HorseArmorType.NONE) {
         this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
      }
   }

   @Override
   protected void playGallopSound(SoundType var1) {
      super.playGallopSound(☃);
      if (this.rand.nextInt(10) == 0) {
         this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, ☃.getVolume() * 0.6F, ☃.getPitch());
      }
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getModifiedMaxHealth());
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getModifiedMovementSpeed());
      this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.world.isRemote && this.dataManager.isDirty()) {
         this.dataManager.setClean();
         this.resetTexturePrefix();
      }
   }

   @Override
   protected SoundEvent getAmbientSound() {
      super.getAmbientSound();
      return SoundEvents.ENTITY_HORSE_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      super.getDeathSound();
      return SoundEvents.ENTITY_HORSE_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      super.getHurtSound(☃);
      return SoundEvents.ENTITY_HORSE_HURT;
   }

   @Override
   protected SoundEvent getAngrySound() {
      super.getAngrySound();
      return SoundEvents.ENTITY_HORSE_ANGRY;
   }

   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_HORSE;
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      boolean ☃x = !☃.isEmpty();
      if (☃x && ☃.getItem() == Items.SPAWN_EGG) {
         return super.processInteract(☃, ☃);
      } else {
         if (!this.isChild()) {
            if (this.isTame() && ☃.isSneaking()) {
               this.openGUI(☃);
               return true;
            }

            if (this.isBeingRidden()) {
               return super.processInteract(☃, ☃);
            }
         }

         if (☃x) {
            if (this.handleEating(☃, ☃)) {
               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
               }

               return true;
            }

            if (☃.interactWithEntity(☃, this, ☃)) {
               return true;
            }

            if (!this.isTame()) {
               this.makeMad();
               return true;
            }

            boolean ☃xx = HorseArmorType.getByItemStack(☃) != HorseArmorType.NONE;
            boolean ☃xxx = !this.isChild() && !this.isHorseSaddled() && ☃.getItem() == Items.SADDLE;
            if (☃xx || ☃xxx) {
               this.openGUI(☃);
               return true;
            }
         }

         if (this.isChild()) {
            return super.processInteract(☃, ☃);
         } else {
            this.mountTo(☃);
            return true;
         }
      }
   }

   @Override
   public boolean canMateWith(EntityAnimal var1) {
      if (☃ == this) {
         return false;
      } else {
         return !(☃ instanceof EntityDonkey) && !(☃ instanceof EntityHorse) ? false : this.canMate() && ((AbstractHorse)☃).canMate();
      }
   }

   @Override
   public EntityAgeable createChild(EntityAgeable var1) {
      AbstractHorse ☃;
      if (☃ instanceof EntityDonkey) {
         ☃ = new EntityMule(this.world);
      } else {
         EntityHorse ☃x = (EntityHorse)☃;
         ☃ = new EntityHorse(this.world);
         int ☃xx = this.rand.nextInt(9);
         int ☃xxx;
         if (☃xx < 4) {
            ☃xxx = this.getHorseVariant() & 0xFF;
         } else if (☃xx < 8) {
            ☃xxx = ☃x.getHorseVariant() & 0xFF;
         } else {
            ☃xxx = this.rand.nextInt(7);
         }

         int ☃xxxx = this.rand.nextInt(5);
         if (☃xxxx < 2) {
            ☃xxx |= this.getHorseVariant() & 0xFF00;
         } else if (☃xxxx < 4) {
            ☃xxx |= ☃x.getHorseVariant() & 0xFF00;
         } else {
            ☃xxx |= this.rand.nextInt(5) << 8 & 0xFF00;
         }

         ((EntityHorse)☃).setHorseVariant(☃xxx);
      }

      this.setOffspringAttributes(☃, ☃);
      return ☃;
   }

   @Override
   public boolean wearsArmor() {
      return true;
   }

   @Override
   public boolean isArmor(ItemStack var1) {
      return HorseArmorType.isHorseArmor(☃.getItem());
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      ☃ = super.onInitialSpawn(☃, ☃);
      int ☃;
      if (☃ instanceof EntityHorse.GroupData) {
         ☃ = ((EntityHorse.GroupData)☃).variant;
      } else {
         ☃ = this.rand.nextInt(7);
         ☃ = new EntityHorse.GroupData(☃);
      }

      this.setHorseVariant(☃ | this.rand.nextInt(5) << 8);
      return ☃;
   }

   public static class GroupData implements IEntityLivingData {
      public int variant;

      public GroupData(int var1) {
         this.variant = ☃;
      }
   }
}
