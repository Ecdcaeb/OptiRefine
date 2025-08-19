package mods.Hileb.optirefine.mixin.defaults.minecraft.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Checked
@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private UUID teamUuid = null;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private String teamUuidString = null;

    @SuppressWarnings("unused")
    public MixinEntityLiving(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @WrapMethod(method = "onUpdate")
    public void injectOnUpdate(Operation<Void> original){
        if (Config.isSmoothWorld() && this.canSkipUpdate()) {
            this.onUpdateMinimal();
        } else {
            original.call();
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean canSkipUpdate() {
        if (this.isChild()) {
            return false;
        } else if (this.hurtTime > 0) {
            return false;
        } else if (this.ticksExisted < 20) {
            return false;
        } else {
            World world = this.getEntityWorld();
            if (world.playerEntities.size() != 1) {
                return false;
            } else {
                Entity player = world.playerEntities.getFirst();
                double dx = Math.max(Math.abs(this.posX - player.posX) - 16.0, 0.0);
                double dz = Math.max(Math.abs(this.posZ - player.posZ) - 16.0, 0.0);
                double distSq = dx * dx + dz * dz;
                return !this.isInRangeToRenderDist(distSq);
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private void onUpdateMinimal() {
        ++this.idleTime;
        if (_cast_EntityLiving() instanceof EntityMob && this.getBrightness() > 0.5f) {
            this.idleTime += 2;
        }
        this.despawnEntity();
    }

    @Shadow
    protected abstract void despawnEntity();

    @Override
    public Team getTeam() {
        UUID uuid = _cast_EntityLiving().getUniqueID();
        if (this.teamUuid != uuid) {
            this.teamUuid = uuid;
            this.teamUuidString = String.valueOf(uuid);
        }

        return this.world.getScoreboard().getPlayersTeam(this.teamUuidString);
    }

    @Unique
    @AccessibleOperation
    private EntityLiving _cast_EntityLiving() {throw new AbstractMethodError();}


}
/*
+++ net/minecraft/entity/EntityLiving.java	Tue Aug 19 14:59:58 2025
@@ -2,29 +2,32 @@

 import com.google.common.collect.Maps;
 import java.util.Arrays;
 import java.util.Map;
 import java.util.Random;
 import java.util.UUID;
+import java.util.function.BiPredicate;
 import javax.annotation.Nullable;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.enchantment.EnchantmentHelper;
 import net.minecraft.entity.ai.EntityAITasks;
 import net.minecraft.entity.ai.EntityJumpHelper;
 import net.minecraft.entity.ai.EntityLookHelper;
 import net.minecraft.entity.ai.EntityMoveHelper;
 import net.minecraft.entity.ai.EntitySenses;
 import net.minecraft.entity.ai.attributes.AttributeModifier;
 import net.minecraft.entity.item.EntityBoat;
 import net.minecraft.entity.item.EntityItem;
 import net.minecraft.entity.monster.EntityGhast;
+import net.minecraft.entity.monster.EntityMob;
 import net.minecraft.entity.monster.IMob;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.inventory.EntityEquipmentSlot;
+import net.minecraft.inventory.EntityEquipmentSlot.Type;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemArmor;
 import net.minecraft.item.ItemBow;
 import net.minecraft.item.ItemStack;
 import net.minecraft.item.ItemSword;
 import net.minecraft.nbt.NBTTagCompound;
@@ -34,12 +37,13 @@
 import net.minecraft.network.datasync.DataSerializers;
 import net.minecraft.network.datasync.EntityDataManager;
 import net.minecraft.network.play.server.SPacketEntityAttach;
 import net.minecraft.pathfinding.PathNavigate;
 import net.minecraft.pathfinding.PathNavigateGround;
 import net.minecraft.pathfinding.PathNodeType;
+import net.minecraft.scoreboard.Team;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.EnumHandSide;
 import net.minecraft.util.EnumParticleTypes;
 import net.minecraft.util.NonNullList;
 import net.minecraft.util.ResourceLocation;
@@ -48,16 +52,19 @@
 import net.minecraft.util.datafix.FixTypes;
 import net.minecraft.util.datafix.walkers.ItemStackDataLists;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.DifficultyInstance;
 import net.minecraft.world.EnumDifficulty;
+import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.World;
+import net.minecraft.world.WorldEntitySpawner;
 import net.minecraft.world.WorldServer;
-import net.minecraft.world.storage.loot.LootContext;
 import net.minecraft.world.storage.loot.LootTable;
+import net.minecraft.world.storage.loot.LootContext.Builder;
+import net.optifine.reflect.Reflector;

 public abstract class EntityLiving extends EntityLivingBase {
    private static final DataParameter<Byte> AI_FLAGS = EntityDataManager.createKey(EntityLiving.class, DataSerializers.BYTE);
    public int livingSoundTime;
    protected int experienceValue;
    private final EntityLookHelper lookHelper;
@@ -78,12 +85,14 @@
    private final Map<PathNodeType, Float> mapPathPriority = Maps.newEnumMap(PathNodeType.class);
    private ResourceLocation deathLootTable;
    private long deathLootTableSeed;
    private boolean isLeashed;
    private Entity leashHolder;
    private NBTTagCompound leashNBTTag;
+   private UUID teamUuid = null;
+   private String teamUuidString = null;

    public EntityLiving(World var1) {
       super(var1);
       this.tasks = new EntityAITasks(var1 != null && var1.profiler != null ? var1.profiler : null);
       this.targetTasks = new EntityAITasks(var1 != null && var1.profiler != null ? var1.profiler : null);
       this.lookHelper = new EntityLookHelper(this);
@@ -148,12 +157,13 @@
    public EntityLivingBase getAttackTarget() {
       return this.attackTarget;
    }

    public void setAttackTarget(@Nullable EntityLivingBase var1) {
       this.attackTarget = var1;
+      Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, new Object[]{this, var1});
    }

    public boolean canAttackClass(Class<? extends EntityLivingBase> var1) {
       return var1 != EntityGhast.class;
    }

@@ -198,19 +208,19 @@

    protected int getExperiencePoints(EntityPlayer var1) {
       if (this.experienceValue > 0) {
          int var2 = this.experienceValue;

          for (int var3 = 0; var3 < this.inventoryArmor.size(); var3++) {
-            if (!this.inventoryArmor.get(var3).isEmpty() && this.inventoryArmorDropChances[var3] <= 1.0F) {
+            if (!((ItemStack)this.inventoryArmor.get(var3)).isEmpty() && this.inventoryArmorDropChances[var3] <= 1.0F) {
                var2 += 1 + this.rand.nextInt(3);
             }
          }

          for (int var4 = 0; var4 < this.inventoryHands.size(); var4++) {
-            if (!this.inventoryHands.get(var4).isEmpty() && this.inventoryHandsDropChances[var4] <= 1.0F) {
+            if (!((ItemStack)this.inventoryHands.get(var4)).isEmpty() && this.inventoryHandsDropChances[var4] <= 1.0F) {
                var2 += 1 + this.rand.nextInt(3);
             }
          }

          return var2;
       } else {
@@ -230,13 +240,14 @@
                   EnumParticleTypes.EXPLOSION_NORMAL,
                   this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width - var2 * 10.0,
                   this.posY + this.rand.nextFloat() * this.height - var4 * 10.0,
                   this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width - var6 * 10.0,
                   var2,
                   var4,
-                  var6
+                  var6,
+                  new int[0]
                );
          }
       } else {
          this.world.setEntityState(this, (byte)20);
       }
    }
@@ -247,21 +258,25 @@
       } else {
          super.handleStatusUpdate(var1);
       }
    }

    public void onUpdate() {
-      super.onUpdate();
-      if (!this.world.isRemote) {
-         this.updateLeashedState();
-         if (this.ticksExisted % 5 == 0) {
-            boolean var1 = !(this.getControllingPassenger() instanceof EntityLiving);
-            boolean var2 = !(this.getRidingEntity() instanceof EntityBoat);
-            this.tasks.setControlFlag(1, var1);
-            this.tasks.setControlFlag(4, var1 && var2);
-            this.tasks.setControlFlag(2, var1);
+      if (Config.isSmoothWorld() && this.canSkipUpdate()) {
+         this.onUpdateMinimal();
+      } else {
+         super.onUpdate();
+         if (!this.world.isRemote) {
+            this.updateLeashedState();
+            if (this.ticksExisted % 5 == 0) {
+               boolean var1 = !(this.getControllingPassenger() instanceof EntityLiving);
+               boolean var2 = !(this.getRidingEntity() instanceof EntityBoat);
+               this.tasks.setControlFlag(1, var1);
+               this.tasks.setControlFlag(4, var1 && var2);
+               this.tasks.setControlFlag(2, var1);
+            }
          }
       }
    }

    protected float updateDistance(float var1, float var2) {
       this.bodyHelper.updateRenderAngles();
@@ -290,13 +305,13 @@
             this.dropItem(var3, 1);
          }
       }
    }

    public static void registerFixesMob(DataFixer var0, Class<?> var1) {
-      var0.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(var1, "ArmorItems", "HandItems"));
+      var0.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(var1, new String[]{"ArmorItems", "HandItems"}));
    }

    public void writeEntityToNBT(NBTTagCompound var1) {
       super.writeEntityToNBT(var1);
       var1.setBoolean("CanPickUpLoot", this.canPickUpLoot());
       var1.setBoolean("PersistenceRequired", this.persistenceRequired);
@@ -431,19 +446,19 @@
          var4 = this.getLootTable();
       }

       if (var4 != null) {
          LootTable var5 = this.world.getLootTableManager().getLootTableFromLocation(var4);
          this.deathLootTable = null;
-         LootContext.Builder var6 = new LootContext.Builder((WorldServer)this.world).withLootedEntity(this).withDamageSource(var3);
+         Builder var6 = new Builder((WorldServer)this.world).withLootedEntity(this).withDamageSource(var3);
          if (var1 && this.attackingPlayer != null) {
             var6 = var6.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
          }

-         for (ItemStack var9 : var5.generateLootForPools(this.deathLootTableSeed == 0L ? this.rand : new Random(this.deathLootTableSeed), var6.build())) {
-            this.entityDropItem(var9, 0.0F);
+         for (ItemStack var8 : var5.generateLootForPools(this.deathLootTableSeed == 0L ? this.rand : new Random(this.deathLootTableSeed), var6.build())) {
+            this.entityDropItem(var8, 0.0F);
          }

          this.dropEquipment(var1, var2);
       } else {
          super.dropLoot(var1, var2, var3);
       }
@@ -466,13 +481,18 @@
       this.setMoveForward(var1);
    }

    public void onLivingUpdate() {
       super.onLivingUpdate();
       this.world.profiler.startSection("looting");
-      if (!this.world.isRemote && this.canPickUpLoot() && !this.dead && this.world.getGameRules().getBoolean("mobGriefing")) {
+      boolean var1 = this.world.getGameRules().getBoolean("mobGriefing");
+      if (Reflector.ForgeEventFactory_getMobGriefingEvent.exists()) {
+         var1 = Reflector.callBoolean(Reflector.ForgeEventFactory_getMobGriefingEvent, new Object[]{this.world, this});
+      }
+
+      if (!this.world.isRemote && this.canPickUpLoot() && !this.dead && var1) {
          for (EntityItem var3 : this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(1.0, 0.0, 1.0))) {
             if (!var3.isDead && !var3.getItem().isEmpty() && !var3.cannotPickup()) {
                this.updateEquipmentIfNeeded(var3);
             }
          }
       }
@@ -483,13 +503,13 @@
    protected void updateEquipmentIfNeeded(EntityItem var1) {
       ItemStack var2 = var1.getItem();
       EntityEquipmentSlot var3 = getSlotForItemStack(var2);
       boolean var4 = true;
       ItemStack var5 = this.getItemStackFromSlot(var3);
       if (!var5.isEmpty()) {
-         if (var3.getSlotType() == EntityEquipmentSlot.Type.HAND) {
+         if (var3.getSlotType() == Type.HAND) {
             if (var2.getItem() instanceof ItemSword && !(var5.getItem() instanceof ItemSword)) {
                var4 = true;
             } else if (var2.getItem() instanceof ItemSword && var5.getItem() instanceof ItemSword) {
                ItemSword var6 = (ItemSword)var2.getItem();
                ItemSword var7 = (ItemSword)var5.getItem();
                if (var6.getAttackDamage() == var7.getAttackDamage()) {
@@ -555,31 +575,38 @@

    protected boolean canDespawn() {
       return true;
    }

    protected void despawnEntity() {
+      Object var1 = null;
+      Object var2 = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
+      Object var3 = Reflector.getFieldValue(Reflector.Event_Result_DENY);
       if (this.persistenceRequired) {
          this.idleTime = 0;
-      } else {
-         EntityPlayer var1 = this.world.getClosestPlayerToEntity(this, -1.0);
-         if (var1 != null) {
-            double var2 = var1.posX - this.posX;
-            double var4 = var1.posY - this.posY;
-            double var6 = var1.posZ - this.posZ;
-            double var8 = var2 * var2 + var4 * var4 + var6 * var6;
-            if (this.canDespawn() && var8 > 16384.0) {
+      } else if ((this.idleTime & 31) != 31 || (var1 = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, new Object[]{this})) == var2) {
+         EntityPlayer var4 = this.world.getClosestPlayerToEntity(this, -1.0);
+         if (var4 != null) {
+            double var5 = var4.posX - this.posX;
+            double var7 = var4.posY - this.posY;
+            double var9 = var4.posZ - this.posZ;
+            double var11 = var5 * var5 + var7 * var7 + var9 * var9;
+            if (this.canDespawn() && var11 > 16384.0) {
                this.setDead();
             }

-            if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && var8 > 1024.0 && this.canDespawn()) {
+            if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && var11 > 1024.0 && this.canDespawn()) {
                this.setDead();
-            } else if (var8 < 1024.0) {
+            } else if (var11 < 1024.0) {
                this.idleTime = 0;
             }
          }
+      } else if (var1 == var3) {
+         this.idleTime = 0;
+      } else {
+         this.setDead();
       }
    }

    protected final void updateEntityActionState() {
       this.idleTime++;
       this.world.profiler.startSection("checkDespawn");
@@ -627,24 +654,24 @@
    public int getHorizontalFaceSpeed() {
       return 10;
    }

    public void faceEntity(Entity var1, float var2, float var3) {
       double var4 = var1.posX - this.posX;
-      double var8 = var1.posZ - this.posZ;
-      double var6;
+      double var6 = var1.posZ - this.posZ;
+      double var8;
       if (var1 instanceof EntityLivingBase) {
          EntityLivingBase var10 = (EntityLivingBase)var1;
-         var6 = var10.posY + var10.getEyeHeight() - (this.posY + this.getEyeHeight());
+         var8 = var10.posY + var10.getEyeHeight() - (this.posY + this.getEyeHeight());
       } else {
-         var6 = (var1.getEntityBoundingBox().minY + var1.getEntityBoundingBox().maxY) / 2.0 - (this.posY + this.getEyeHeight());
+         var8 = (var1.getEntityBoundingBox().minY + var1.getEntityBoundingBox().maxY) / 2.0 - (this.posY + this.getEyeHeight());
       }

-      double var14 = MathHelper.sqrt(var4 * var4 + var8 * var8);
-      float var12 = (float)(MathHelper.atan2(var8, var4) * 180.0F / (float)Math.PI) - 90.0F;
-      float var13 = (float)(-(MathHelper.atan2(var6, var14) * 180.0F / (float)Math.PI));
+      double var14 = MathHelper.sqrt(var4 * var4 + var6 * var6);
+      float var12 = (float)(MathHelper.atan2(var6, var4) * (180.0 / Math.PI)) - 90.0F;
+      float var13 = (float)(-(MathHelper.atan2(var8, var14) * (180.0 / Math.PI)));
       this.rotationPitch = this.updateRotation(this.rotationPitch, var13, var3);
       this.rotationYaw = this.updateRotation(this.rotationYaw, var12, var2);
    }

    private float updateRotation(float var1, float var2, float var3) {
       float var4 = MathHelper.wrapDegrees(var2 - var1);
@@ -658,13 +685,13 @@

       return var1 + var4;
    }

    public boolean getCanSpawnHere() {
       IBlockState var1 = this.world.getBlockState(new BlockPos(this).down());
-      return var1.canEntitySpawn(this);
+      return var1.a(this);
    }

    public boolean isNotColliding() {
       return !this.world.containsAnyLiquid(this.getEntityBoundingBox())
          && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
          && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
@@ -700,15 +727,15 @@
       return this.inventoryArmor;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot var1) {
       switch (var1.getSlotType()) {
          case HAND:
-            return this.inventoryHands.get(var1.getIndex());
+            return (ItemStack)this.inventoryHands.get(var1.getIndex());
          case ARMOR:
-            return this.inventoryArmor.get(var1.getIndex());
+            return (ItemStack)this.inventoryArmor.get(var1.getIndex());
          default:
             return ItemStack.EMPTY;
       }
    }

    public void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2) {
@@ -763,13 +790,13 @@
             var2++;
          }

          boolean var4 = true;

          for (EntityEquipmentSlot var8 : EntityEquipmentSlot.values()) {
-            if (var8.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
+            if (var8.getSlotType() == Type.ARMOR) {
                ItemStack var9 = this.getItemStackFromSlot(var8);
                if (!var4 && this.rand.nextFloat() < var3) {
                   break;
                }

                var4 = false;
@@ -782,20 +809,32 @@
             }
          }
       }
    }

    public static EntityEquipmentSlot getSlotForItemStack(ItemStack var0) {
+      if (Reflector.ForgeItem_getEquipmentSlot.exists()) {
+         EntityEquipmentSlot var1 = (EntityEquipmentSlot)Reflector.call(var0.getItem(), Reflector.ForgeItem_getEquipmentSlot, new Object[]{var0});
+         if (var1 != null) {
+            return var1;
+         }
+      }
+
       if (var0.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN) || var0.getItem() == Items.SKULL) {
          return EntityEquipmentSlot.HEAD;
       } else if (var0.getItem() instanceof ItemArmor) {
          return ((ItemArmor)var0.getItem()).armorType;
       } else if (var0.getItem() == Items.ELYTRA) {
          return EntityEquipmentSlot.CHEST;
       } else {
-         return var0.getItem() == Items.SHIELD ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND;
+         boolean var2 = var0.getItem() == Items.SHIELD;
+         if (Reflector.ForgeItem_isShield.exists()) {
+            var2 = Reflector.callBoolean(var0.getItem(), Reflector.ForgeItem_isShield, new Object[]{var0, null});
+         }
+
+         return var2 ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND;
       }
    }

    @Nullable
    public static Item getArmorByChance(EntityEquipmentSlot var0, int var1) {
       switch (var0) {
@@ -859,13 +898,13 @@
             EntityEquipmentSlot.MAINHAND,
             EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItemMainhand(), (int)(5.0F + var2 * this.rand.nextInt(18)), false)
          );
       }

       for (EntityEquipmentSlot var6 : EntityEquipmentSlot.values()) {
-         if (var6.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
+         if (var6.getSlotType() == Type.ARMOR) {
             ItemStack var7 = this.getItemStackFromSlot(var6);
             if (!var7.isEmpty() && this.rand.nextFloat() < 0.5F * var2) {
                this.setItemStackToSlot(var6, EnchantmentHelper.addRandomEnchantment(this.rand, var7, (int)(5.0F + var2 * this.rand.nextInt(18)), false));
             }
          }
       }
@@ -956,13 +995,13 @@
          this.leashHolder = null;
          if (!this.world.isRemote && var2) {
             this.dropItem(Items.LEAD, 1);
          }

          if (!this.world.isRemote && var1 && this.world instanceof WorldServer) {
-            ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityAttach(this, null));
+            ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityAttach(this, (Entity)null));
          }
       }
    }

    public boolean canBeLeashedTo(EntityPlayer var1) {
       return !this.getLeashed() && !(this instanceof IMob);
@@ -999,26 +1038,26 @@

    private void recreateLeash() {
       if (this.isLeashed && this.leashNBTTag != null) {
          if (this.leashNBTTag.hasUniqueId("UUID")) {
             UUID var1 = this.leashNBTTag.getUniqueId("UUID");

-            for (EntityLivingBase var4 : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0))) {
-               if (var4.getUniqueID().equals(var1)) {
-                  this.setLeashHolder(var4, true);
+            for (EntityLivingBase var3 : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0))) {
+               if (var3.getUniqueID().equals(var1)) {
+                  this.setLeashHolder(var3, true);
                   break;
                }
             }
          } else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
-            BlockPos var5 = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
-            EntityLeashKnot var6 = EntityLeashKnot.getKnotForPosition(this.world, var5);
-            if (var6 == null) {
-               var6 = EntityLeashKnot.createKnot(this.world, var5);
+            BlockPos var4 = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
+            EntityLeashKnot var5 = EntityLeashKnot.getKnotForPosition(this.world, var4);
+            if (var5 == null) {
+               var5 = EntityLeashKnot.createKnot(this.world, var4);
             }

-            this.setLeashHolder(var6, true);
+            this.setLeashHolder(var5, true);
          } else {
             this.clearLeashed(false, true);
          }
       }

       this.leashNBTTag = null;
@@ -1065,33 +1104,92 @@

    public boolean isServerWorld() {
       return super.isServerWorld() && !this.isAIDisabled();
    }

    public void setNoAI(boolean var1) {
-      byte var2 = this.dataManager.get(AI_FLAGS);
+      byte var2 = this.dataManager.<Byte>get(AI_FLAGS);
       this.dataManager.set(AI_FLAGS, var1 ? (byte)(var2 | 1) : (byte)(var2 & -2));
    }

    public void setLeftHanded(boolean var1) {
-      byte var2 = this.dataManager.get(AI_FLAGS);
+      byte var2 = this.dataManager.<Byte>get(AI_FLAGS);
       this.dataManager.set(AI_FLAGS, var1 ? (byte)(var2 | 2) : (byte)(var2 & -3));
    }

    public boolean isAIDisabled() {
-      return (this.dataManager.get(AI_FLAGS) & 1) != 0;
+      return (this.dataManager.<Byte>get(AI_FLAGS) & 1) != 0;
    }

    public boolean isLeftHanded() {
-      return (this.dataManager.get(AI_FLAGS) & 2) != 0;
+      return (this.dataManager.<Byte>get(AI_FLAGS) & 2) != 0;
    }

    public EnumHandSide getPrimaryHand() {
       return this.isLeftHanded() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

+   private boolean canSkipUpdate() {
+      if (this.isChild()) {
+         return false;
+      } else if (this.hurtTime > 0) {
+         return false;
+      } else if (this.ticksExisted < 20) {
+         return false;
+      } else {
+         World var1 = this.getEntityWorld();
+         if (var1 == null) {
+            return false;
+         } else if (var1.playerEntities.size() != 1) {
+            return false;
+         } else {
+            Entity var2 = (Entity)var1.playerEntities.get(0);
+            double var3 = Math.max(Math.abs(this.posX - var2.posX) - 16.0, 0.0);
+            double var5 = Math.max(Math.abs(this.posZ - var2.posZ) - 16.0, 0.0);
+            double var7 = var3 * var3 + var5 * var5;
+            return !this.isInRangeToRenderDist(var7);
+         }
+      }
+   }
+
+   private void onUpdateMinimal() {
+      this.idleTime++;
+      if (this instanceof EntityMob) {
+         float var1 = this.getBrightness();
+         if (var1 > 0.5F) {
+            this.idleTime += 2;
+         }
+      }
+
+      this.despawnEntity();
+   }
+
+   public Team getTeam() {
+      UUID var1 = this.getUniqueID();
+      if (this.teamUuid != var1) {
+         this.teamUuid = var1;
+         this.teamUuidString = var1.toString();
+      }
+
+      return this.world.getScoreboard().getPlayersTeam(this.teamUuidString);
+   }
+
    public static enum SpawnPlacementType {
       ON_GROUND,
       IN_AIR,
       IN_WATER;
+
+      private final BiPredicate<IBlockAccess, BlockPos> spawnPredicate;
+
+      private SpawnPlacementType() {
+         this.spawnPredicate = null;
+      }
+
+      private SpawnPlacementType(BiPredicate<IBlockAccess, BlockPos> var3) {
+         this.spawnPredicate = var3;
+      }
+
+      public boolean canSpawnAt(World var1, BlockPos var2) {
+         return this.spawnPredicate != null ? this.spawnPredicate.test(var1, var2) : WorldEntitySpawner.canCreatureTypeSpawnBody(this, var1, var2);
+      }
    }
 }
 */
