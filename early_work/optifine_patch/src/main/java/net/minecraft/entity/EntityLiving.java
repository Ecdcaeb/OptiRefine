/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Maps
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Float
 *  java.lang.Iterable
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.Map
 *  java.util.Random
 *  java.util.UUID
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityBodyHelper
 *  net.minecraft.entity.EntityHanging
 *  net.minecraft.entity.EntityLeashKnot
 *  net.minecraft.entity.EntityLiving$1
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.EntityJumpHelper
 *  net.minecraft.entity.ai.EntityLookHelper
 *  net.minecraft.entity.ai.EntityMoveHelper
 *  net.minecraft.entity.ai.EntitySenses
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.IMob
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.inventory.EntityEquipmentSlot$Type
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagFloat
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.network.Packet
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.DataSerializer
 *  net.minecraft.network.datasync.DataSerializers
 *  net.minecraft.network.datasync.EntityDataManager
 *  net.minecraft.network.play.server.SPacketEntityAttach
 *  net.minecraft.pathfinding.PathNavigate
 *  net.minecraft.pathfinding.PathNavigateGround
 *  net.minecraft.pathfinding.PathNodeType
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.datafix.DataFixer
 *  net.minecraft.util.datafix.FixTypes
 *  net.minecraft.util.datafix.IDataWalker
 *  net.minecraft.util.datafix.walkers.ItemStackDataLists
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.EnumDifficulty
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldServer
 *  net.minecraft.world.storage.loot.LootContext$Builder
 *  net.minecraft.world.storage.loot.LootTable
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorField
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorMethod;

public abstract class EntityLiving
extends EntityLivingBase {
    private static final DataParameter<Byte> AI_FLAGS = EntityDataManager.createKey(EntityLiving.class, (DataSerializer)DataSerializers.BYTE);
    public int livingSoundTime;
    protected int experienceValue;
    private final EntityLookHelper lookHelper;
    protected EntityMoveHelper moveHelper;
    protected EntityJumpHelper jumpHelper;
    private final EntityBodyHelper bodyHelper;
    protected PathNavigate navigator;
    protected final EntityAITasks tasks;
    protected final EntityAITasks targetTasks;
    private EntityLivingBase attackTarget;
    private final EntitySenses senses;
    private final NonNullList<ItemStack> inventoryHands = NonNullList.withSize((int)2, (Object)ItemStack.EMPTY);
    protected float[] inventoryHandsDropChances = new float[2];
    private final NonNullList<ItemStack> inventoryArmor = NonNullList.withSize((int)4, (Object)ItemStack.EMPTY);
    protected float[] inventoryArmorDropChances = new float[4];
    private boolean canPickUpLoot;
    private boolean persistenceRequired;
    private final Map<PathNodeType, Float> mapPathPriority = Maps.newEnumMap(PathNodeType.class);
    private ResourceLocation deathLootTable;
    private long deathLootTableSeed;
    private boolean isLeashed;
    private Entity leashHolder;
    private NBTTagCompound leashNBTTag;
    private UUID teamUuid = null;
    private String teamUuidString = null;

    public EntityLiving(World worldIn) {
        super(worldIn);
        this.tasks = new EntityAITasks(worldIn != null && worldIn.profiler != null ? worldIn.profiler : null);
        this.targetTasks = new EntityAITasks(worldIn != null && worldIn.profiler != null ? worldIn.profiler : null);
        this.lookHelper = new EntityLookHelper(this);
        this.moveHelper = new EntityMoveHelper(this);
        this.jumpHelper = new EntityJumpHelper(this);
        this.bodyHelper = this.createBodyHelper();
        this.navigator = this.createNavigator(worldIn);
        this.senses = new EntitySenses(this);
        Arrays.fill((float[])this.inventoryArmorDropChances, (float)0.085f);
        Arrays.fill((float[])this.inventoryHandsDropChances, (float)0.085f);
        if (worldIn != null && !worldIn.isRemote) {
            this.initEntityAI();
        }
    }

    protected void initEntityAI() {
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateGround(this, worldIn);
    }

    public float getPathPriority(PathNodeType nodeType) {
        Float f = (Float)this.mapPathPriority.get((Object)nodeType);
        return f == null ? nodeType.getPriority() : f.floatValue();
    }

    public void setPathPriority(PathNodeType nodeType, float priority) {
        this.mapPathPriority.put((Object)nodeType, (Object)Float.valueOf((float)priority));
    }

    protected EntityBodyHelper createBodyHelper() {
        return new EntityBodyHelper((EntityLivingBase)this);
    }

    public EntityLookHelper getLookHelper() {
        return this.lookHelper;
    }

    public EntityMoveHelper getMoveHelper() {
        return this.moveHelper;
    }

    public EntityJumpHelper getJumpHelper() {
        return this.jumpHelper;
    }

    public PathNavigate getNavigator() {
        return this.navigator;
    }

    public EntitySenses getEntitySenses() {
        return this.senses;
    }

    @Nullable
    public EntityLivingBase getAttackTarget() {
        return this.attackTarget;
    }

    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        this.attackTarget = entitylivingbaseIn;
        Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooks_onLivingSetAttackTarget, (Object[])new Object[]{this, entitylivingbaseIn});
    }

    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
        return cls != EntityGhast.class;
    }

    public void eatGrassBonus() {
    }

    protected void entityInit() {
        super.entityInit();
        this.Y.register(AI_FLAGS, (Object)0);
    }

    public int getTalkInterval() {
        return 80;
    }

    public void playLivingSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.a(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();
        this.l.profiler.startSection("mobBaseTick");
        if (this.isEntityAlive() && this.S.nextInt(1000) < this.livingSoundTime++) {
            this.applyEntityAI();
            this.playLivingSound();
        }
        this.l.profiler.endSection();
    }

    protected void playHurtSound(DamageSource source) {
        this.applyEntityAI();
        super.playHurtSound(source);
    }

    private void applyEntityAI() {
        this.livingSoundTime = -this.getTalkInterval();
    }

    protected int getExperiencePoints(EntityPlayer player) {
        if (this.experienceValue > 0) {
            int i = this.experienceValue;
            for (int j = 0; j < this.inventoryArmor.size(); ++j) {
                if (((ItemStack)this.inventoryArmor.get(j)).isEmpty() || !(this.inventoryArmorDropChances[j] <= 1.0f)) continue;
                i += 1 + this.S.nextInt(3);
            }
            for (int k = 0; k < this.inventoryHands.size(); ++k) {
                if (((ItemStack)this.inventoryHands.get(k)).isEmpty() || !(this.inventoryHandsDropChances[k] <= 1.0f)) continue;
                i += 1 + this.S.nextInt(3);
            }
            return i;
        }
        return this.experienceValue;
    }

    public void spawnExplosionParticle() {
        if (this.l.isRemote) {
            for (int i = 0; i < 20; ++i) {
                double d0 = this.S.nextGaussian() * 0.02;
                double d1 = this.S.nextGaussian() * 0.02;
                double d2 = this.S.nextGaussian() * 0.02;
                double d3 = 10.0;
                this.l.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.p + (double)(this.S.nextFloat() * this.G * 2.0f) - (double)this.G - d0 * 10.0, this.q + (double)(this.S.nextFloat() * this.H) - d1 * 10.0, this.r + (double)(this.S.nextFloat() * this.G * 2.0f) - (double)this.G - d2 * 10.0, d0, d1, d2, new int[0]);
            }
        } else {
            this.l.setEntityState((Entity)this, (byte)20);
        }
    }

    public void handleStatusUpdate(byte id) {
        if (id == 20) {
            this.spawnExplosionParticle();
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public void onUpdate() {
        if (Config.isSmoothWorld() && this.canSkipUpdate()) {
            this.onUpdateMinimal();
            return;
        }
        super.onUpdate();
        if (!this.l.isRemote) {
            this.updateLeashedState();
            if (this.T % 5 == 0) {
                boolean flag = !(this.bE() instanceof EntityLiving);
                boolean flag1 = !(this.bJ() instanceof EntityBoat);
                this.tasks.setControlFlag(1, flag);
                this.tasks.setControlFlag(4, flag && flag1);
                this.tasks.setControlFlag(2, flag);
            }
        }
    }

    protected float updateDistance(float p_110146_1_, float p_110146_2_) {
        this.bodyHelper.updateRenderAngles();
        return p_110146_2_;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    protected Item getDropItem() {
        return null;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        Item item = this.getDropItem();
        if (item != null) {
            int i = this.S.nextInt(3);
            if (lootingModifier > 0) {
                i += this.S.nextInt(lootingModifier + 1);
            }
            for (int j = 0; j < i; ++j) {
                this.a(item, 1);
            }
        }
    }

    public static void registerFixesMob(DataFixer fixer, Class<?> name) {
        fixer.registerWalker(FixTypes.ENTITY, (IDataWalker)new ItemStackDataLists(name, new String[]{"ArmorItems", "HandItems"}));
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
        compound.setBoolean("PersistenceRequired", this.persistenceRequired);
        NBTTagList nbttaglist = new NBTTagList();
        for (ItemStack itemstack : this.inventoryArmor) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            if (!itemstack.isEmpty()) {
                itemstack.writeToNBT(nbttagcompound);
            }
            nbttaglist.appendTag((NBTBase)nbttagcompound);
        }
        compound.setTag("ArmorItems", (NBTBase)nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();
        for (Object itemstack1 : this.inventoryHands) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            if (!itemstack1.isEmpty()) {
                itemstack1.writeToNBT(nbttagcompound1);
            }
            nbttaglist1.appendTag((NBTBase)nbttagcompound1);
        }
        compound.setTag("HandItems", (NBTBase)nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();
        for (ItemStack f : (Object)this.inventoryArmorDropChances) {
            nbttaglist2.appendTag((NBTBase)new NBTTagFloat((float)f));
        }
        compound.setTag("ArmorDropChances", (NBTBase)nbttaglist2);
        NBTTagList nbttaglist3 = new NBTTagList();
        for (float f1 : this.inventoryHandsDropChances) {
            nbttaglist3.appendTag((NBTBase)new NBTTagFloat(f1));
        }
        compound.setTag("HandDropChances", (NBTBase)nbttaglist3);
        compound.setBoolean("Leashed", this.isLeashed);
        if (this.leashHolder != null) {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            if (this.leashHolder instanceof EntityLivingBase) {
                UUID uuid = this.leashHolder.getUniqueID();
                nbttagcompound2.setUniqueId("UUID", uuid);
            } else if (this.leashHolder instanceof EntityHanging) {
                BlockPos blockpos = ((EntityHanging)this.leashHolder).getHangingPosition();
                nbttagcompound2.setInteger("X", blockpos.p());
                nbttagcompound2.setInteger("Y", blockpos.q());
                nbttagcompound2.setInteger("Z", blockpos.r());
            }
            compound.setTag("Leash", (NBTBase)nbttagcompound2);
        }
        compound.setBoolean("LeftHanded", this.isLeftHanded());
        if (this.deathLootTable != null) {
            compound.setString("DeathLootTable", this.deathLootTable.toString());
            if (this.deathLootTableSeed != 0L) {
                compound.setLong("DeathLootTableSeed", this.deathLootTableSeed);
            }
        }
        if (this.isAIDisabled()) {
            compound.setBoolean("NoAI", this.isAIDisabled());
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("CanPickUpLoot", 1)) {
            this.setCanPickUpLoot(compound.getBoolean("CanPickUpLoot"));
        }
        this.persistenceRequired = compound.getBoolean("PersistenceRequired");
        if (compound.hasKey("ArmorItems", 9)) {
            NBTTagList nbttaglist = compound.getTagList("ArmorItems", 10);
            for (int i = 0; i < this.inventoryArmor.size(); ++i) {
                this.inventoryArmor.set(i, (Object)new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }
        if (compound.hasKey("HandItems", 9)) {
            NBTTagList nbttaglist1 = compound.getTagList("HandItems", 10);
            for (int j = 0; j < this.inventoryHands.size(); ++j) {
                this.inventoryHands.set(j, (Object)new ItemStack(nbttaglist1.getCompoundTagAt(j)));
            }
        }
        if (compound.hasKey("ArmorDropChances", 9)) {
            NBTTagList nbttaglist2 = compound.getTagList("ArmorDropChances", 5);
            for (int k = 0; k < nbttaglist2.tagCount(); ++k) {
                this.inventoryArmorDropChances[k] = nbttaglist2.getFloatAt(k);
            }
        }
        if (compound.hasKey("HandDropChances", 9)) {
            NBTTagList nbttaglist3 = compound.getTagList("HandDropChances", 5);
            for (int l = 0; l < nbttaglist3.tagCount(); ++l) {
                this.inventoryHandsDropChances[l] = nbttaglist3.getFloatAt(l);
            }
        }
        this.isLeashed = compound.getBoolean("Leashed");
        if (this.isLeashed && compound.hasKey("Leash", 10)) {
            this.leashNBTTag = compound.getCompoundTag("Leash");
        }
        this.setLeftHanded(compound.getBoolean("LeftHanded"));
        if (compound.hasKey("DeathLootTable", 8)) {
            this.deathLootTable = new ResourceLocation(compound.getString("DeathLootTable"));
            this.deathLootTableSeed = compound.getLong("DeathLootTableSeed");
        }
        this.setNoAI(compound.getBoolean("NoAI"));
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return null;
    }

    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        ResourceLocation resourcelocation = this.deathLootTable;
        if (resourcelocation == null) {
            resourcelocation = this.getLootTable();
        }
        if (resourcelocation != null) {
            LootTable loottable = this.l.getLootTableManager().getLootTableFromLocation(resourcelocation);
            this.deathLootTable = null;
            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.l).withLootedEntity((Entity)this).withDamageSource(source);
            if (wasRecentlyHit && this.attackingPlayer != null) {
                lootcontext$builder = lootcontext$builder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
            }
            for (ItemStack itemstack : loottable.generateLootForPools(this.deathLootTableSeed == 0L ? this.S : new Random(this.deathLootTableSeed), lootcontext$builder.build())) {
                this.a(itemstack, 0.0f);
            }
            this.dropEquipment(wasRecentlyHit, lootingModifier);
        } else {
            super.dropLoot(wasRecentlyHit, lootingModifier, source);
        }
    }

    public void setMoveForward(float p_191989_1_) {
        this.moveForward = p_191989_1_;
    }

    public void setMoveVertical(float amount) {
        this.moveVertical = amount;
    }

    public void setMoveStrafing(float amount) {
        this.moveStrafing = amount;
    }

    public void setAIMoveSpeed(float speedIn) {
        super.setAIMoveSpeed(speedIn);
        this.setMoveForward(speedIn);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.l.profiler.startSection("looting");
        boolean mobGriefing = this.l.getGameRules().getBoolean("mobGriefing");
        if (Reflector.ForgeEventFactory_getMobGriefingEvent.exists()) {
            mobGriefing = Reflector.callBoolean((ReflectorMethod)Reflector.ForgeEventFactory_getMobGriefingEvent, (Object[])new Object[]{this.l, this});
        }
        if (!this.l.isRemote && this.canPickUpLoot() && !this.dead && mobGriefing) {
            for (EntityItem entityitem : this.l.getEntitiesWithinAABB(EntityItem.class, this.bw().grow(1.0, 0.0, 1.0))) {
                if (entityitem.F || entityitem.getItem().isEmpty() || entityitem.cannotPickup()) continue;
                this.updateEquipmentIfNeeded(entityitem);
            }
        }
        this.l.profiler.endSection();
    }

    protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        boolean flag = true;
        ItemStack itemstack1 = this.getItemStackFromSlot(entityequipmentslot);
        if (!itemstack1.isEmpty()) {
            if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
                if (itemstack.getItem() instanceof ItemSword && !(itemstack1.getItem() instanceof ItemSword)) {
                    flag = true;
                } else if (itemstack.getItem() instanceof ItemSword && itemstack1.getItem() instanceof ItemSword) {
                    ItemSword itemsword = (ItemSword)itemstack.getItem();
                    ItemSword itemsword1 = (ItemSword)itemstack1.getItem();
                    flag = itemsword.getAttackDamage() == itemsword1.getAttackDamage() ? itemstack.getMetadata() > itemstack1.getMetadata() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound() : itemsword.getAttackDamage() > itemsword1.getAttackDamage();
                } else {
                    flag = itemstack.getItem() instanceof ItemBow && itemstack1.getItem() instanceof ItemBow ? itemstack.hasTagCompound() && !itemstack1.hasTagCompound() : false;
                }
            } else if (itemstack.getItem() instanceof ItemArmor && !(itemstack1.getItem() instanceof ItemArmor)) {
                flag = true;
            } else if (itemstack.getItem() instanceof ItemArmor && itemstack1.getItem() instanceof ItemArmor && !EnchantmentHelper.hasBindingCurse((ItemStack)itemstack1)) {
                ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                ItemArmor itemarmor1 = (ItemArmor)itemstack1.getItem();
                flag = itemarmor.damageReduceAmount == itemarmor1.damageReduceAmount ? itemstack.getMetadata() > itemstack1.getMetadata() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound() : itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
            } else {
                flag = false;
            }
        }
        if (flag && this.canEquipItem(itemstack)) {
            double d0;
            switch (1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[entityequipmentslot.getSlotType().ordinal()]) {
                case 1: {
                    d0 = this.inventoryHandsDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                case 2: {
                    d0 = this.inventoryArmorDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                default: {
                    d0 = 0.0;
                }
            }
            if (!itemstack1.isEmpty() && (double)(this.S.nextFloat() - 0.1f) < d0) {
                this.a(itemstack1, 0.0f);
            }
            this.setItemStackToSlot(entityequipmentslot, itemstack);
            switch (1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[entityequipmentslot.getSlotType().ordinal()]) {
                case 1: {
                    this.inventoryHandsDropChances[entityequipmentslot.getIndex()] = 2.0f;
                    break;
                }
                case 2: {
                    this.inventoryArmorDropChances[entityequipmentslot.getIndex()] = 2.0f;
                }
            }
            this.persistenceRequired = true;
            this.onItemPickup((Entity)itemEntity, itemstack.getCount());
            itemEntity.X();
        }
    }

    protected boolean canEquipItem(ItemStack stack) {
        return true;
    }

    protected boolean canDespawn() {
        return true;
    }

    protected void despawnEntity() {
        Object result = null;
        Object Result_DEFAULT = Reflector.getFieldValue((ReflectorField)Reflector.Event_Result_DEFAULT);
        Object Result_DENY = Reflector.getFieldValue((ReflectorField)Reflector.Event_Result_DENY);
        if (this.persistenceRequired) {
            this.idleTime = 0;
        } else if ((this.idleTime & 0x1F) == 31 && (result = Reflector.call((ReflectorMethod)Reflector.ForgeEventFactory_canEntityDespawn, (Object[])new Object[]{this})) != Result_DEFAULT) {
            if (result == Result_DENY) {
                this.idleTime = 0;
            } else {
                this.X();
            }
        } else {
            EntityPlayer entity = this.l.getClosestPlayerToEntity((Entity)this, -1.0);
            if (entity != null) {
                double d0 = entity.posX - this.p;
                double d1 = entity.posY - this.q;
                double d2 = entity.posZ - this.r;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (this.canDespawn() && d3 > 16384.0) {
                    this.X();
                }
                if (this.idleTime > 600 && this.S.nextInt(800) == 0 && d3 > 1024.0 && this.canDespawn()) {
                    this.X();
                } else if (d3 < 1024.0) {
                    this.idleTime = 0;
                }
            }
        }
    }

    protected final void updateEntityActionState() {
        ++this.idleTime;
        this.l.profiler.startSection("checkDespawn");
        this.despawnEntity();
        this.l.profiler.endSection();
        this.l.profiler.startSection("sensing");
        this.senses.clearSensingCache();
        this.l.profiler.endSection();
        this.l.profiler.startSection("targetSelector");
        this.targetTasks.onUpdateTasks();
        this.l.profiler.endSection();
        this.l.profiler.startSection("goalSelector");
        this.tasks.onUpdateTasks();
        this.l.profiler.endSection();
        this.l.profiler.startSection("navigation");
        this.navigator.onUpdateNavigation();
        this.l.profiler.endSection();
        this.l.profiler.startSection("mob tick");
        this.updateAITasks();
        this.l.profiler.endSection();
        if (this.aS() && this.bJ() instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)this.bJ();
            entityliving.getNavigator().setPath(this.getNavigator().getPath(), 1.5);
            entityliving.getMoveHelper().read(this.getMoveHelper());
        }
        this.l.profiler.startSection("controls");
        this.l.profiler.startSection("move");
        this.moveHelper.onUpdateMoveHelper();
        this.l.profiler.endStartSection("look");
        this.lookHelper.onUpdateLook();
        this.l.profiler.endStartSection("jump");
        this.jumpHelper.doJump();
        this.l.profiler.endSection();
        this.l.profiler.endSection();
    }

    protected void updateAITasks() {
    }

    public int getVerticalFaceSpeed() {
        return 40;
    }

    public int getHorizontalFaceSpeed() {
        return 10;
    }

    public void faceEntity(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
        double d1;
        double d0 = entityIn.posX - this.p;
        double d2 = entityIn.posZ - this.r;
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            d1 = entitylivingbase.q + (double)entitylivingbase.by() - (this.q + (double)this.by());
        } else {
            d1 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (this.q + (double)this.by());
        }
        double d3 = MathHelper.sqrt((double)(d0 * d0 + d2 * d2));
        float f = (float)(MathHelper.atan2((double)d2, (double)d0) * 57.29577951308232) - 90.0f;
        float f1 = (float)(-(MathHelper.atan2((double)d1, (double)d3) * 57.29577951308232));
        this.w = this.updateRotation(this.w, f1, maxPitchIncrease);
        this.v = this.updateRotation(this.v, f, maxYawIncrease);
    }

    private float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees((float)(targetAngle - angle));
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
    }

    public boolean getCanSpawnHere() {
        IBlockState iblockstate = this.l.getBlockState(new BlockPos((Entity)this).down());
        return iblockstate.a((Entity)this);
    }

    public boolean isNotColliding() {
        return !this.l.containsAnyLiquid(this.bw()) && this.l.getCollisionBoxes((Entity)this, this.bw()).isEmpty() && this.l.checkNoEntityCollision(this.bw(), (Entity)this);
    }

    public float getRenderSizeModifier() {
        return 1.0f;
    }

    public int getMaxSpawnedInChunk() {
        return 4;
    }

    public int getMaxFallHeight() {
        if (this.getAttackTarget() == null) {
            return 3;
        }
        int i = (int)(this.getHealth() - this.getMaxHealth() * 0.33f);
        if ((i -= (3 - this.l.getDifficulty().getId()) * 4) < 0) {
            i = 0;
        }
        return i + 3;
    }

    public Iterable<ItemStack> getHeldEquipment() {
        return this.inventoryHands;
    }

    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventoryArmor;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        switch (1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[slotIn.getSlotType().ordinal()]) {
            case 1: {
                return (ItemStack)this.inventoryHands.get(slotIn.getIndex());
            }
            case 2: {
                return (ItemStack)this.inventoryArmor.get(slotIn.getIndex());
            }
        }
        return ItemStack.EMPTY;
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        switch (1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[slotIn.getSlotType().ordinal()]) {
            case 1: {
                this.inventoryHands.set(slotIn.getIndex(), (Object)stack);
                break;
            }
            case 2: {
                this.inventoryArmor.set(slotIn.getIndex(), (Object)stack);
            }
        }
    }

    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            boolean flag;
            double d0;
            ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
            switch (1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[entityequipmentslot.getSlotType().ordinal()]) {
                case 1: {
                    d0 = this.inventoryHandsDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                case 2: {
                    d0 = this.inventoryArmorDropChances[entityequipmentslot.getIndex()];
                    break;
                }
                default: {
                    d0 = 0.0;
                }
            }
            boolean bl = flag = d0 > 1.0;
            if (itemstack.isEmpty() || EnchantmentHelper.hasVanishingCurse((ItemStack)itemstack) || !wasRecentlyHit && !flag || !((double)(this.S.nextFloat() - (float)lootingModifier * 0.01f) < d0)) continue;
            if (!flag && itemstack.isItemStackDamageable()) {
                itemstack.setItemDamage(itemstack.getMaxDamage() - this.S.nextInt(1 + this.S.nextInt(Math.max((int)(itemstack.getMaxDamage() - 3), (int)1))));
            }
            this.a(itemstack, 0.0f);
        }
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        if (this.S.nextFloat() < 0.15f * difficulty.getClampedAdditionalDifficulty()) {
            float f;
            int i = this.S.nextInt(2);
            float f2 = f = this.l.getDifficulty() == EnumDifficulty.HARD ? 0.1f : 0.25f;
            if (this.S.nextFloat() < 0.095f) {
                ++i;
            }
            if (this.S.nextFloat() < 0.095f) {
                ++i;
            }
            if (this.S.nextFloat() < 0.095f) {
                ++i;
            }
            boolean flag = true;
            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
                Item item;
                if (entityequipmentslot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) continue;
                ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
                if (!flag && this.S.nextFloat() < f) break;
                flag = false;
                if (!itemstack.isEmpty() || (item = EntityLiving.getArmorByChance(entityequipmentslot, i)) == null) continue;
                this.setItemStackToSlot(entityequipmentslot, new ItemStack(item));
            }
        }
    }

    public static EntityEquipmentSlot getSlotForItemStack(ItemStack stack) {
        EntityEquipmentSlot slot;
        if (Reflector.ForgeItem_getEquipmentSlot.exists() && (slot = (EntityEquipmentSlot)Reflector.call((Object)stack.getItem(), (ReflectorMethod)Reflector.ForgeItem_getEquipmentSlot, (Object[])new Object[]{stack})) != null) {
            return slot;
        }
        if (stack.getItem() != Item.getItemFromBlock((Block)Blocks.PUMPKIN) && stack.getItem() != Items.SKULL) {
            boolean isShield;
            if (stack.getItem() instanceof ItemArmor) {
                return ((ItemArmor)stack.getItem()).armorType;
            }
            if (stack.getItem() == Items.ELYTRA) {
                return EntityEquipmentSlot.CHEST;
            }
            boolean bl = isShield = stack.getItem() == Items.SHIELD;
            if (Reflector.ForgeItem_isShield.exists()) {
                isShield = Reflector.callBoolean((Object)stack.getItem(), (ReflectorMethod)Reflector.ForgeItem_isShield, (Object[])new Object[]{stack, null});
            }
            return isShield ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND;
        }
        return EntityEquipmentSlot.HEAD;
    }

    @Nullable
    public static Item getArmorByChance(EntityEquipmentSlot slotIn, int chance) {
        switch (1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[slotIn.ordinal()]) {
            case 1: {
                if (chance == 0) {
                    return Items.LEATHER_HELMET;
                }
                if (chance == 1) {
                    return Items.GOLDEN_HELMET;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_HELMET;
                }
                if (chance == 3) {
                    return Items.IRON_HELMET;
                }
                if (chance == 4) {
                    return Items.DIAMOND_HELMET;
                }
            }
            case 2: {
                if (chance == 0) {
                    return Items.LEATHER_CHESTPLATE;
                }
                if (chance == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                }
                if (chance == 3) {
                    return Items.IRON_CHESTPLATE;
                }
                if (chance == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            }
            case 3: {
                if (chance == 0) {
                    return Items.LEATHER_LEGGINGS;
                }
                if (chance == 1) {
                    return Items.GOLDEN_LEGGINGS;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                }
                if (chance == 3) {
                    return Items.IRON_LEGGINGS;
                }
                if (chance == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            }
            case 4: {
                if (chance == 0) {
                    return Items.LEATHER_BOOTS;
                }
                if (chance == 1) {
                    return Items.GOLDEN_BOOTS;
                }
                if (chance == 2) {
                    return Items.CHAINMAIL_BOOTS;
                }
                if (chance == 3) {
                    return Items.IRON_BOOTS;
                }
                if (chance != 4) break;
                return Items.DIAMOND_BOOTS;
            }
        }
        return null;
    }

    protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty) {
        float f = difficulty.getClampedAdditionalDifficulty();
        if (!this.getHeldItemMainhand().isEmpty() && this.S.nextFloat() < 0.25f * f) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment((Random)this.S, (ItemStack)this.getHeldItemMainhand(), (int)((int)(5.0f + f * (float)this.S.nextInt(18))), (boolean)false));
        }
        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            ItemStack itemstack;
            if (entityequipmentslot.getSlotType() != EntityEquipmentSlot.Type.ARMOR || (itemstack = this.getItemStackFromSlot(entityequipmentslot)).isEmpty() || !(this.S.nextFloat() < 0.5f * f)) continue;
            this.setItemStackToSlot(entityequipmentslot, EnchantmentHelper.addRandomEnchantment((Random)this.S, (ItemStack)itemstack, (int)((int)(5.0f + f * (float)this.S.nextInt(18))), (boolean)false));
        }
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.S.nextGaussian() * 0.05, 1));
        if (this.S.nextFloat() < 0.05f) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }
        return livingdata;
    }

    public boolean canBeSteered() {
        return false;
    }

    public void enablePersistence() {
        this.persistenceRequired = true;
    }

    public void setDropChance(EntityEquipmentSlot slotIn, float chance) {
        switch (1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot$Type[slotIn.getSlotType().ordinal()]) {
            case 1: {
                this.inventoryHandsDropChances[slotIn.getIndex()] = chance;
                break;
            }
            case 2: {
                this.inventoryArmorDropChances[slotIn.getIndex()] = chance;
            }
        }
    }

    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }

    public void setCanPickUpLoot(boolean canPickup) {
        this.canPickUpLoot = canPickup;
    }

    public boolean isNoDespawnRequired() {
        return this.persistenceRequired;
    }

    public final boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (this.getLeashed() && this.getLeashHolder() == player) {
            this.clearLeashed(true, !player.capabilities.isCreativeMode);
            return true;
        }
        ItemStack itemstack = player.b(hand);
        if (itemstack.getItem() == Items.LEAD && this.canBeLeashedTo(player)) {
            this.setLeashHolder((Entity)player, true);
            itemstack.shrink(1);
            return true;
        }
        return this.processInteract(player, hand) ? true : super.b(player, hand);
    }

    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        return false;
    }

    protected void updateLeashedState() {
        if (this.leashNBTTag != null) {
            this.recreateLeash();
        }
        if (this.isLeashed) {
            if (!this.isEntityAlive()) {
                this.clearLeashed(true, true);
            }
            if (this.leashHolder == null || this.leashHolder.isDead) {
                this.clearLeashed(true, true);
            }
        }
    }

    public void clearLeashed(boolean sendPacket, boolean dropLead) {
        if (this.isLeashed) {
            this.isLeashed = false;
            this.leashHolder = null;
            if (!this.l.isRemote && dropLead) {
                this.a(Items.LEAD, 1);
            }
            if (!this.l.isRemote && sendPacket && this.l instanceof WorldServer) {
                ((WorldServer)this.l).getEntityTracker().sendToTracking((Entity)this, (Packet)new SPacketEntityAttach((Entity)this, (Entity)null));
            }
        }
    }

    public boolean canBeLeashedTo(EntityPlayer player) {
        return !this.getLeashed() && !(this instanceof IMob);
    }

    public boolean getLeashed() {
        return this.isLeashed;
    }

    public Entity getLeashHolder() {
        return this.leashHolder;
    }

    public void setLeashHolder(Entity entityIn, boolean sendAttachNotification) {
        this.isLeashed = true;
        this.leashHolder = entityIn;
        if (!this.l.isRemote && sendAttachNotification && this.l instanceof WorldServer) {
            ((WorldServer)this.l).getEntityTracker().sendToTracking((Entity)this, (Packet)new SPacketEntityAttach((Entity)this, this.leashHolder));
        }
        if (this.aS()) {
            this.dismountRidingEntity();
        }
    }

    public boolean startRiding(Entity entityIn, boolean force) {
        boolean flag = super.a(entityIn, force);
        if (flag && this.getLeashed()) {
            this.clearLeashed(true, true);
        }
        return flag;
    }

    private void recreateLeash() {
        if (this.isLeashed && this.leashNBTTag != null) {
            if (this.leashNBTTag.hasUniqueId("UUID")) {
                UUID uuid = this.leashNBTTag.getUniqueId("UUID");
                for (EntityLivingBase entitylivingbase : this.l.getEntitiesWithinAABB(EntityLivingBase.class, this.bw().grow(10.0))) {
                    if (!entitylivingbase.bm().equals((Object)uuid)) continue;
                    this.setLeashHolder((Entity)entitylivingbase, true);
                    break;
                }
            } else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
                BlockPos blockpos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
                EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition((World)this.l, (BlockPos)blockpos);
                if (entityleashknot == null) {
                    entityleashknot = EntityLeashKnot.createKnot((World)this.l, (BlockPos)blockpos);
                }
                this.setLeashHolder((Entity)entityleashknot, true);
            } else {
                this.clearLeashed(false, true);
            }
        }
        this.leashNBTTag = null;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        EntityEquipmentSlot entityequipmentslot;
        if (inventorySlot == 98) {
            entityequipmentslot = EntityEquipmentSlot.MAINHAND;
        } else if (inventorySlot == 99) {
            entityequipmentslot = EntityEquipmentSlot.OFFHAND;
        } else if (inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.HEAD;
        } else if (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.CHEST;
        } else if (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.LEGS;
        } else {
            if (inventorySlot != 100 + EntityEquipmentSlot.FEET.getIndex()) {
                return false;
            }
            entityequipmentslot = EntityEquipmentSlot.FEET;
        }
        if (!itemStackIn.isEmpty() && !EntityLiving.isItemStackInSlot(entityequipmentslot, itemStackIn) && entityequipmentslot != EntityEquipmentSlot.HEAD) {
            return false;
        }
        this.setItemStackToSlot(entityequipmentslot, itemStackIn);
        return true;
    }

    public boolean canPassengerSteer() {
        return this.canBeSteered() && super.bI();
    }

    public static boolean isItemStackInSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(stack);
        return entityequipmentslot == slotIn || entityequipmentslot == EntityEquipmentSlot.MAINHAND && slotIn == EntityEquipmentSlot.OFFHAND || entityequipmentslot == EntityEquipmentSlot.OFFHAND && slotIn == EntityEquipmentSlot.MAINHAND;
    }

    public boolean isServerWorld() {
        return super.isServerWorld() && !this.isAIDisabled();
    }

    public void setNoAI(boolean disable) {
        byte b0 = (Byte)this.Y.get(AI_FLAGS);
        this.Y.set(AI_FLAGS, (Object)(disable ? (byte)(b0 | 1) : (byte)(b0 & 0xFFFFFFFE)));
    }

    public void setLeftHanded(boolean leftHanded) {
        byte b0 = (Byte)this.Y.get(AI_FLAGS);
        this.Y.set(AI_FLAGS, (Object)(leftHanded ? (byte)(b0 | 2) : (byte)(b0 & 0xFFFFFFFD)));
    }

    public boolean isAIDisabled() {
        return ((Byte)this.Y.get(AI_FLAGS) & 1) != 0;
    }

    public boolean isLeftHanded() {
        return ((Byte)this.Y.get(AI_FLAGS) & 2) != 0;
    }

    public EnumHandSide getPrimaryHand() {
        return this.isLeftHanded() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    private boolean canSkipUpdate() {
        double dz;
        if (this.isChild()) {
            return false;
        }
        if (this.hurtTime > 0) {
            return false;
        }
        if (this.T < 20) {
            return false;
        }
        World world = this.e();
        if (world == null) {
            return false;
        }
        if (world.playerEntities.size() != 1) {
            return false;
        }
        Entity player = (Entity)world.playerEntities.get(0);
        double dx = Math.max((double)(Math.abs((double)(this.p - player.posX)) - 16.0), (double)0.0);
        double distSq = dx * dx + (dz = Math.max((double)(Math.abs((double)(this.r - player.posZ)) - 16.0), (double)0.0)) * dz;
        return !this.a(distSq);
    }

    private void onUpdateMinimal() {
        float brightness;
        ++this.idleTime;
        if (this instanceof EntityMob && (brightness = this.aw()) > 0.5f) {
            this.idleTime += 2;
        }
        this.despawnEntity();
    }

    public Team aY() {
        UUID uuid = this.bm();
        if (this.teamUuid != uuid) {
            this.teamUuid = uuid;
            this.teamUuidString = uuid.toString();
        }
        return this.l.getScoreboard().getPlayersTeam(this.teamUuidString);
    }
}
