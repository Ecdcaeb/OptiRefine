package net.minecraft.entity.item;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand extends EntityLivingBase {
   private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
   private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
   private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
   private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
   private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
   private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
   public static final DataParameter<Byte> STATUS = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.BYTE);
   public static final DataParameter<Rotations> HEAD_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
   public static final DataParameter<Rotations> BODY_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
   public static final DataParameter<Rotations> LEFT_ARM_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
   public static final DataParameter<Rotations> RIGHT_ARM_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
   public static final DataParameter<Rotations> LEFT_LEG_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
   public static final DataParameter<Rotations> RIGHT_LEG_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
   private static final Predicate<Entity> IS_RIDEABLE_MINECART = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃ instanceof EntityMinecart && ((EntityMinecart)☃).getType() == EntityMinecart.Type.RIDEABLE;
      }
   };
   private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
   private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
   private boolean canInteract;
   public long punchCooldown;
   private int disabledSlots;
   private boolean wasMarker;
   private Rotations headRotation = DEFAULT_HEAD_ROTATION;
   private Rotations bodyRotation = DEFAULT_BODY_ROTATION;
   private Rotations leftArmRotation = DEFAULT_LEFTARM_ROTATION;
   private Rotations rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
   private Rotations leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
   private Rotations rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;

   public EntityArmorStand(World var1) {
      super(☃);
      this.noClip = this.hasNoGravity();
      this.setSize(0.5F, 1.975F);
   }

   public EntityArmorStand(World var1, double var2, double var4, double var6) {
      this(☃);
      this.setPosition(☃, ☃, ☃);
   }

   @Override
   protected final void setSize(float var1, float var2) {
      double ☃ = this.posX;
      double ☃x = this.posY;
      double ☃xx = this.posZ;
      float ☃xxx = this.hasMarker() ? 0.0F : (this.isChild() ? 0.5F : 1.0F);
      super.setSize(☃ * ☃xxx, ☃ * ☃xxx);
      this.setPosition(☃, ☃x, ☃xx);
   }

   @Override
   public boolean isServerWorld() {
      return super.isServerWorld() && !this.hasNoGravity();
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(STATUS, (byte)0);
      this.dataManager.register(HEAD_ROTATION, DEFAULT_HEAD_ROTATION);
      this.dataManager.register(BODY_ROTATION, DEFAULT_BODY_ROTATION);
      this.dataManager.register(LEFT_ARM_ROTATION, DEFAULT_LEFTARM_ROTATION);
      this.dataManager.register(RIGHT_ARM_ROTATION, DEFAULT_RIGHTARM_ROTATION);
      this.dataManager.register(LEFT_LEG_ROTATION, DEFAULT_LEFTLEG_ROTATION);
      this.dataManager.register(RIGHT_LEG_ROTATION, DEFAULT_RIGHTLEG_ROTATION);
   }

   @Override
   public Iterable<ItemStack> getHeldEquipment() {
      return this.handItems;
   }

   @Override
   public Iterable<ItemStack> getArmorInventoryList() {
      return this.armorItems;
   }

   @Override
   public ItemStack getItemStackFromSlot(EntityEquipmentSlot var1) {
      switch (☃.getSlotType()) {
         case HAND:
            return this.handItems.get(☃.getIndex());
         case ARMOR:
            return this.armorItems.get(☃.getIndex());
         default:
            return ItemStack.EMPTY;
      }
   }

   @Override
   public void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2) {
      switch (☃.getSlotType()) {
         case HAND:
            this.playEquipSound(☃);
            this.handItems.set(☃.getIndex(), ☃);
            break;
         case ARMOR:
            this.playEquipSound(☃);
            this.armorItems.set(☃.getIndex(), ☃);
      }
   }

   @Override
   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      EntityEquipmentSlot ☃;
      if (☃ == 98) {
         ☃ = EntityEquipmentSlot.MAINHAND;
      } else if (☃ == 99) {
         ☃ = EntityEquipmentSlot.OFFHAND;
      } else if (☃ == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
         ☃ = EntityEquipmentSlot.HEAD;
      } else if (☃ == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
         ☃ = EntityEquipmentSlot.CHEST;
      } else if (☃ == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
         ☃ = EntityEquipmentSlot.LEGS;
      } else {
         if (☃ != 100 + EntityEquipmentSlot.FEET.getIndex()) {
            return false;
         }

         ☃ = EntityEquipmentSlot.FEET;
      }

      if (!☃.isEmpty() && !EntityLiving.isItemStackInSlot(☃, ☃) && ☃ != EntityEquipmentSlot.HEAD) {
         return false;
      } else {
         this.setItemStackToSlot(☃, ☃);
         return true;
      }
   }

   public static void registerFixesArmorStand(DataFixer var0) {
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityArmorStand.class, "ArmorItems", "HandItems"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      NBTTagList ☃ = new NBTTagList();

      for (ItemStack ☃x : this.armorItems) {
         NBTTagCompound ☃xx = new NBTTagCompound();
         if (!☃x.isEmpty()) {
            ☃x.writeToNBT(☃xx);
         }

         ☃.appendTag(☃xx);
      }

      ☃.setTag("ArmorItems", ☃);
      NBTTagList ☃x = new NBTTagList();

      for (ItemStack ☃xx : this.handItems) {
         NBTTagCompound ☃xxx = new NBTTagCompound();
         if (!☃xx.isEmpty()) {
            ☃xx.writeToNBT(☃xxx);
         }

         ☃x.appendTag(☃xxx);
      }

      ☃.setTag("HandItems", ☃x);
      ☃.setBoolean("Invisible", this.isInvisible());
      ☃.setBoolean("Small", this.isSmall());
      ☃.setBoolean("ShowArms", this.getShowArms());
      ☃.setInteger("DisabledSlots", this.disabledSlots);
      ☃.setBoolean("NoBasePlate", this.hasNoBasePlate());
      if (this.hasMarker()) {
         ☃.setBoolean("Marker", this.hasMarker());
      }

      ☃.setTag("Pose", this.readPoseFromNBT());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("ArmorItems", 9)) {
         NBTTagList ☃ = ☃.getTagList("ArmorItems", 10);

         for (int ☃x = 0; ☃x < this.armorItems.size(); ☃x++) {
            this.armorItems.set(☃x, new ItemStack(☃.getCompoundTagAt(☃x)));
         }
      }

      if (☃.hasKey("HandItems", 9)) {
         NBTTagList ☃ = ☃.getTagList("HandItems", 10);

         for (int ☃x = 0; ☃x < this.handItems.size(); ☃x++) {
            this.handItems.set(☃x, new ItemStack(☃.getCompoundTagAt(☃x)));
         }
      }

      this.setInvisible(☃.getBoolean("Invisible"));
      this.setSmall(☃.getBoolean("Small"));
      this.setShowArms(☃.getBoolean("ShowArms"));
      this.disabledSlots = ☃.getInteger("DisabledSlots");
      this.setNoBasePlate(☃.getBoolean("NoBasePlate"));
      this.setMarker(☃.getBoolean("Marker"));
      this.wasMarker = !this.hasMarker();
      this.noClip = this.hasNoGravity();
      NBTTagCompound ☃ = ☃.getCompoundTag("Pose");
      this.writePoseToNBT(☃);
   }

   private void writePoseToNBT(NBTTagCompound var1) {
      NBTTagList ☃ = ☃.getTagList("Head", 5);
      this.setHeadRotation(☃.isEmpty() ? DEFAULT_HEAD_ROTATION : new Rotations(☃));
      NBTTagList ☃x = ☃.getTagList("Body", 5);
      this.setBodyRotation(☃x.isEmpty() ? DEFAULT_BODY_ROTATION : new Rotations(☃x));
      NBTTagList ☃xx = ☃.getTagList("LeftArm", 5);
      this.setLeftArmRotation(☃xx.isEmpty() ? DEFAULT_LEFTARM_ROTATION : new Rotations(☃xx));
      NBTTagList ☃xxx = ☃.getTagList("RightArm", 5);
      this.setRightArmRotation(☃xxx.isEmpty() ? DEFAULT_RIGHTARM_ROTATION : new Rotations(☃xxx));
      NBTTagList ☃xxxx = ☃.getTagList("LeftLeg", 5);
      this.setLeftLegRotation(☃xxxx.isEmpty() ? DEFAULT_LEFTLEG_ROTATION : new Rotations(☃xxxx));
      NBTTagList ☃xxxxx = ☃.getTagList("RightLeg", 5);
      this.setRightLegRotation(☃xxxxx.isEmpty() ? DEFAULT_RIGHTLEG_ROTATION : new Rotations(☃xxxxx));
   }

   private NBTTagCompound readPoseFromNBT() {
      NBTTagCompound ☃ = new NBTTagCompound();
      if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
         ☃.setTag("Head", this.headRotation.writeToNBT());
      }

      if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
         ☃.setTag("Body", this.bodyRotation.writeToNBT());
      }

      if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
         ☃.setTag("LeftArm", this.leftArmRotation.writeToNBT());
      }

      if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
         ☃.setTag("RightArm", this.rightArmRotation.writeToNBT());
      }

      if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
         ☃.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
      }

      if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
         ☃.setTag("RightLeg", this.rightLegRotation.writeToNBT());
      }

      return ☃;
   }

   @Override
   public boolean canBePushed() {
      return false;
   }

   @Override
   protected void collideWithEntity(Entity var1) {
   }

   @Override
   protected void collideWithNearbyEntities() {
      List<Entity> ☃ = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_RIDEABLE_MINECART);

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         Entity ☃xx = ☃.get(☃x);
         if (this.getDistanceSq(☃xx) <= 0.2) {
            ☃xx.applyEntityCollision(this);
         }
      }
   }

   @Override
   public EnumActionResult applyPlayerInteraction(EntityPlayer var1, Vec3d var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (this.hasMarker() || ☃.getItem() == Items.NAME_TAG) {
         return EnumActionResult.PASS;
      } else if (!this.world.isRemote && !☃.isSpectator()) {
         EntityEquipmentSlot ☃x = EntityLiving.getSlotForItemStack(☃);
         if (☃.isEmpty()) {
            EntityEquipmentSlot ☃xx = this.getClickedSlot(☃);
            EntityEquipmentSlot ☃xxx = this.isDisabled(☃xx) ? ☃x : ☃xx;
            if (this.hasItemInSlot(☃xxx)) {
               this.swapItem(☃, ☃xxx, ☃, ☃);
            }
         } else {
            if (this.isDisabled(☃x)) {
               return EnumActionResult.FAIL;
            }

            if (☃x.getSlotType() == EntityEquipmentSlot.Type.HAND && !this.getShowArms()) {
               return EnumActionResult.FAIL;
            }

            this.swapItem(☃, ☃x, ☃, ☃);
         }

         return EnumActionResult.SUCCESS;
      } else {
         return EnumActionResult.SUCCESS;
      }
   }

   protected EntityEquipmentSlot getClickedSlot(Vec3d var1) {
      EntityEquipmentSlot ☃ = EntityEquipmentSlot.MAINHAND;
      boolean ☃x = this.isSmall();
      double ☃xx = ☃x ? ☃.y * 2.0 : ☃.y;
      EntityEquipmentSlot ☃xxx = EntityEquipmentSlot.FEET;
      if (☃xx >= 0.1 && ☃xx < 0.1 + (☃x ? 0.8 : 0.45) && this.hasItemInSlot(☃xxx)) {
         ☃ = EntityEquipmentSlot.FEET;
      } else if (☃xx >= 0.9 + (☃x ? 0.3 : 0.0) && ☃xx < 0.9 + (☃x ? 1.0 : 0.7) && this.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
         ☃ = EntityEquipmentSlot.CHEST;
      } else if (☃xx >= 0.4 && ☃xx < 0.4 + (☃x ? 1.0 : 0.8) && this.hasItemInSlot(EntityEquipmentSlot.LEGS)) {
         ☃ = EntityEquipmentSlot.LEGS;
      } else if (☃xx >= 1.6 && this.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
         ☃ = EntityEquipmentSlot.HEAD;
      }

      return ☃;
   }

   private boolean isDisabled(EntityEquipmentSlot var1) {
      return (this.disabledSlots & 1 << ☃.getSlotIndex()) != 0;
   }

   private void swapItem(EntityPlayer var1, EntityEquipmentSlot var2, ItemStack var3, EnumHand var4) {
      ItemStack ☃ = this.getItemStackFromSlot(☃);
      if (☃.isEmpty() || (this.disabledSlots & 1 << ☃.getSlotIndex() + 8) == 0) {
         if (!☃.isEmpty() || (this.disabledSlots & 1 << ☃.getSlotIndex() + 16) == 0) {
            if (☃.capabilities.isCreativeMode && ☃.isEmpty() && !☃.isEmpty()) {
               ItemStack ☃x = ☃.copy();
               ☃x.setCount(1);
               this.setItemStackToSlot(☃, ☃x);
            } else if (☃.isEmpty() || ☃.getCount() <= 1) {
               this.setItemStackToSlot(☃, ☃);
               ☃.setHeldItem(☃, ☃);
            } else if (☃.isEmpty()) {
               ItemStack ☃x = ☃.copy();
               ☃x.setCount(1);
               this.setItemStackToSlot(☃, ☃x);
               ☃.shrink(1);
            }
         }
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.world.isRemote || this.isDead) {
         return false;
      } else if (DamageSource.OUT_OF_WORLD.equals(☃)) {
         this.setDead();
         return false;
      } else if (this.isEntityInvulnerable(☃) || this.canInteract || this.hasMarker()) {
         return false;
      } else if (☃.isExplosion()) {
         this.dropContents();
         this.setDead();
         return false;
      } else if (DamageSource.IN_FIRE.equals(☃)) {
         if (this.isBurning()) {
            this.damageArmorStand(0.15F);
         } else {
            this.setFire(5);
         }

         return false;
      } else if (DamageSource.ON_FIRE.equals(☃) && this.getHealth() > 0.5F) {
         this.damageArmorStand(4.0F);
         return false;
      } else {
         boolean ☃ = "arrow".equals(☃.getDamageType());
         boolean ☃x = "player".equals(☃.getDamageType());
         if (!☃x && !☃) {
            return false;
         } else {
            if (☃.getImmediateSource() instanceof EntityArrow) {
               ☃.getImmediateSource().setDead();
            }

            if (☃.getTrueSource() instanceof EntityPlayer && !((EntityPlayer)☃.getTrueSource()).capabilities.allowEdit) {
               return false;
            } else if (☃.isCreativePlayer()) {
               this.playBrokenSound();
               this.playParticles();
               this.setDead();
               return false;
            } else {
               long ☃xx = this.world.getTotalWorldTime();
               if (☃xx - this.punchCooldown > 5L && !☃) {
                  this.world.setEntityState(this, (byte)32);
                  this.punchCooldown = ☃xx;
               } else {
                  this.dropBlock();
                  this.playParticles();
                  this.setDead();
               }

               return false;
            }
         }
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 32) {
         if (this.world.isRemote) {
            this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ARMORSTAND_HIT, this.getSoundCategory(), 0.3F, 1.0F, false);
            this.punchCooldown = this.world.getTotalWorldTime();
         }
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      double ☃ = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
      if (Double.isNaN(☃) || ☃ == 0.0) {
         ☃ = 4.0;
      }

      ☃ *= 64.0;
      return ☃ < ☃ * ☃;
   }

   private void playParticles() {
      if (this.world instanceof WorldServer) {
         ((WorldServer)this.world)
            .spawnParticle(
               EnumParticleTypes.BLOCK_DUST,
               this.posX,
               this.posY + this.height / 1.5,
               this.posZ,
               10,
               this.width / 4.0F,
               this.height / 4.0F,
               this.width / 4.0F,
               0.05,
               Block.getStateId(Blocks.PLANKS.getDefaultState())
            );
      }
   }

   private void damageArmorStand(float var1) {
      float ☃ = this.getHealth();
      ☃ -= ☃;
      if (☃ <= 0.5F) {
         this.dropContents();
         this.setDead();
      } else {
         this.setHealth(☃);
      }
   }

   private void dropBlock() {
      Block.spawnAsEntity(this.world, new BlockPos(this), new ItemStack(Items.ARMOR_STAND));
      this.dropContents();
   }

   private void dropContents() {
      this.playBrokenSound();

      for (int ☃ = 0; ☃ < this.handItems.size(); ☃++) {
         ItemStack ☃x = this.handItems.get(☃);
         if (!☃x.isEmpty()) {
            Block.spawnAsEntity(this.world, new BlockPos(this).up(), ☃x);
            this.handItems.set(☃, ItemStack.EMPTY);
         }
      }

      for (int ☃x = 0; ☃x < this.armorItems.size(); ☃x++) {
         ItemStack ☃xx = this.armorItems.get(☃x);
         if (!☃xx.isEmpty()) {
            Block.spawnAsEntity(this.world, new BlockPos(this).up(), ☃xx);
            this.armorItems.set(☃x, ItemStack.EMPTY);
         }
      }
   }

   private void playBrokenSound() {
      this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ARMORSTAND_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
   }

   @Override
   protected float updateDistance(float var1, float var2) {
      this.prevRenderYawOffset = this.prevRotationYaw;
      this.renderYawOffset = this.rotationYaw;
      return 0.0F;
   }

   @Override
   public float getEyeHeight() {
      return this.isChild() ? this.height * 0.5F : this.height * 0.9F;
   }

   @Override
   public double getYOffset() {
      return this.hasMarker() ? 0.0 : 0.1F;
   }

   @Override
   public void travel(float var1, float var2, float var3) {
      if (!this.hasNoGravity()) {
         super.travel(☃, ☃, ☃);
      }
   }

   @Override
   public void setRenderYawOffset(float var1) {
      this.prevRenderYawOffset = this.prevRotationYaw = ☃;
      this.prevRotationYawHead = this.rotationYawHead = ☃;
   }

   @Override
   public void setRotationYawHead(float var1) {
      this.prevRenderYawOffset = this.prevRotationYaw = ☃;
      this.prevRotationYawHead = this.rotationYawHead = ☃;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      Rotations ☃ = this.dataManager.get(HEAD_ROTATION);
      if (!this.headRotation.equals(☃)) {
         this.setHeadRotation(☃);
      }

      Rotations ☃x = this.dataManager.get(BODY_ROTATION);
      if (!this.bodyRotation.equals(☃x)) {
         this.setBodyRotation(☃x);
      }

      Rotations ☃xx = this.dataManager.get(LEFT_ARM_ROTATION);
      if (!this.leftArmRotation.equals(☃xx)) {
         this.setLeftArmRotation(☃xx);
      }

      Rotations ☃xxx = this.dataManager.get(RIGHT_ARM_ROTATION);
      if (!this.rightArmRotation.equals(☃xxx)) {
         this.setRightArmRotation(☃xxx);
      }

      Rotations ☃xxxx = this.dataManager.get(LEFT_LEG_ROTATION);
      if (!this.leftLegRotation.equals(☃xxxx)) {
         this.setLeftLegRotation(☃xxxx);
      }

      Rotations ☃xxxxx = this.dataManager.get(RIGHT_LEG_ROTATION);
      if (!this.rightLegRotation.equals(☃xxxxx)) {
         this.setRightLegRotation(☃xxxxx);
      }

      boolean ☃xxxxxx = this.hasMarker();
      if (this.wasMarker != ☃xxxxxx) {
         this.updateBoundingBox(☃xxxxxx);
         this.preventEntitySpawning = !☃xxxxxx;
         this.wasMarker = ☃xxxxxx;
      }
   }

   private void updateBoundingBox(boolean var1) {
      if (☃) {
         this.setSize(0.0F, 0.0F);
      } else {
         this.setSize(0.5F, 1.975F);
      }
   }

   @Override
   protected void updatePotionMetadata() {
      this.setInvisible(this.canInteract);
   }

   @Override
   public void setInvisible(boolean var1) {
      this.canInteract = ☃;
      super.setInvisible(☃);
   }

   @Override
   public boolean isChild() {
      return this.isSmall();
   }

   @Override
   public void onKillCommand() {
      this.setDead();
   }

   @Override
   public boolean isImmuneToExplosions() {
      return this.isInvisible();
   }

   @Override
   public EnumPushReaction getPushReaction() {
      return this.hasMarker() ? EnumPushReaction.IGNORE : super.getPushReaction();
   }

   private void setSmall(boolean var1) {
      this.dataManager.set(STATUS, this.setBit(this.dataManager.get(STATUS), 1, ☃));
      this.setSize(0.5F, 1.975F);
   }

   public boolean isSmall() {
      return (this.dataManager.get(STATUS) & 1) != 0;
   }

   private void setShowArms(boolean var1) {
      this.dataManager.set(STATUS, this.setBit(this.dataManager.get(STATUS), 4, ☃));
   }

   public boolean getShowArms() {
      return (this.dataManager.get(STATUS) & 4) != 0;
   }

   private void setNoBasePlate(boolean var1) {
      this.dataManager.set(STATUS, this.setBit(this.dataManager.get(STATUS), 8, ☃));
   }

   public boolean hasNoBasePlate() {
      return (this.dataManager.get(STATUS) & 8) != 0;
   }

   private void setMarker(boolean var1) {
      this.dataManager.set(STATUS, this.setBit(this.dataManager.get(STATUS), 16, ☃));
      this.setSize(0.5F, 1.975F);
   }

   public boolean hasMarker() {
      return (this.dataManager.get(STATUS) & 16) != 0;
   }

   private byte setBit(byte var1, int var2, boolean var3) {
      if (☃) {
         ☃ = (byte)(☃ | ☃);
      } else {
         ☃ = (byte)(☃ & ~☃);
      }

      return ☃;
   }

   public void setHeadRotation(Rotations var1) {
      this.headRotation = ☃;
      this.dataManager.set(HEAD_ROTATION, ☃);
   }

   public void setBodyRotation(Rotations var1) {
      this.bodyRotation = ☃;
      this.dataManager.set(BODY_ROTATION, ☃);
   }

   public void setLeftArmRotation(Rotations var1) {
      this.leftArmRotation = ☃;
      this.dataManager.set(LEFT_ARM_ROTATION, ☃);
   }

   public void setRightArmRotation(Rotations var1) {
      this.rightArmRotation = ☃;
      this.dataManager.set(RIGHT_ARM_ROTATION, ☃);
   }

   public void setLeftLegRotation(Rotations var1) {
      this.leftLegRotation = ☃;
      this.dataManager.set(LEFT_LEG_ROTATION, ☃);
   }

   public void setRightLegRotation(Rotations var1) {
      this.rightLegRotation = ☃;
      this.dataManager.set(RIGHT_LEG_ROTATION, ☃);
   }

   public Rotations getHeadRotation() {
      return this.headRotation;
   }

   public Rotations getBodyRotation() {
      return this.bodyRotation;
   }

   public Rotations getLeftArmRotation() {
      return this.leftArmRotation;
   }

   public Rotations getRightArmRotation() {
      return this.rightArmRotation;
   }

   public Rotations getLeftLegRotation() {
      return this.leftLegRotation;
   }

   public Rotations getRightLegRotation() {
      return this.rightLegRotation;
   }

   @Override
   public boolean canBeCollidedWith() {
      return super.canBeCollidedWith() && !this.hasMarker();
   }

   @Override
   public EnumHandSide getPrimaryHand() {
      return EnumHandSide.RIGHT;
   }

   @Override
   protected SoundEvent getFallSound(int var1) {
      return SoundEvents.ENTITY_ARMORSTAND_FALL;
   }

   @Nullable
   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_ARMORSTAND_HIT;
   }

   @Nullable
   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_ARMORSTAND_BREAK;
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt var1) {
   }

   @Override
   public boolean canBeHitWithPotion() {
      return false;
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (STATUS.equals(☃)) {
         this.setSize(0.5F, 1.975F);
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   public boolean attackable() {
      return false;
   }
}
