/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  java.lang.Class
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Collections
 *  java.util.Map
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderAbstractHorse
 *  net.minecraft.client.renderer.entity.RenderAreaEffectCloud
 *  net.minecraft.client.renderer.entity.RenderArmorStand
 *  net.minecraft.client.renderer.entity.RenderBat
 *  net.minecraft.client.renderer.entity.RenderBlaze
 *  net.minecraft.client.renderer.entity.RenderBoat
 *  net.minecraft.client.renderer.entity.RenderCaveSpider
 *  net.minecraft.client.renderer.entity.RenderChicken
 *  net.minecraft.client.renderer.entity.RenderCow
 *  net.minecraft.client.renderer.entity.RenderCreeper
 *  net.minecraft.client.renderer.entity.RenderDragon
 *  net.minecraft.client.renderer.entity.RenderDragonFireball
 *  net.minecraft.client.renderer.entity.RenderElderGuardian
 *  net.minecraft.client.renderer.entity.RenderEnderCrystal
 *  net.minecraft.client.renderer.entity.RenderEnderman
 *  net.minecraft.client.renderer.entity.RenderEndermite
 *  net.minecraft.client.renderer.entity.RenderEntity
 *  net.minecraft.client.renderer.entity.RenderEntityItem
 *  net.minecraft.client.renderer.entity.RenderEvoker
 *  net.minecraft.client.renderer.entity.RenderEvokerFangs
 *  net.minecraft.client.renderer.entity.RenderFallingBlock
 *  net.minecraft.client.renderer.entity.RenderFireball
 *  net.minecraft.client.renderer.entity.RenderFish
 *  net.minecraft.client.renderer.entity.RenderGhast
 *  net.minecraft.client.renderer.entity.RenderGiantZombie
 *  net.minecraft.client.renderer.entity.RenderGuardian
 *  net.minecraft.client.renderer.entity.RenderHorse
 *  net.minecraft.client.renderer.entity.RenderHusk
 *  net.minecraft.client.renderer.entity.RenderIllusionIllager
 *  net.minecraft.client.renderer.entity.RenderIronGolem
 *  net.minecraft.client.renderer.entity.RenderItemFrame
 *  net.minecraft.client.renderer.entity.RenderLeashKnot
 *  net.minecraft.client.renderer.entity.RenderLightningBolt
 *  net.minecraft.client.renderer.entity.RenderLlama
 *  net.minecraft.client.renderer.entity.RenderLlamaSpit
 *  net.minecraft.client.renderer.entity.RenderMagmaCube
 *  net.minecraft.client.renderer.entity.RenderMinecart
 *  net.minecraft.client.renderer.entity.RenderMinecartMobSpawner
 *  net.minecraft.client.renderer.entity.RenderMooshroom
 *  net.minecraft.client.renderer.entity.RenderOcelot
 *  net.minecraft.client.renderer.entity.RenderPainting
 *  net.minecraft.client.renderer.entity.RenderParrot
 *  net.minecraft.client.renderer.entity.RenderPig
 *  net.minecraft.client.renderer.entity.RenderPigZombie
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.client.renderer.entity.RenderPolarBear
 *  net.minecraft.client.renderer.entity.RenderPotion
 *  net.minecraft.client.renderer.entity.RenderRabbit
 *  net.minecraft.client.renderer.entity.RenderSheep
 *  net.minecraft.client.renderer.entity.RenderShulker
 *  net.minecraft.client.renderer.entity.RenderShulkerBullet
 *  net.minecraft.client.renderer.entity.RenderSilverfish
 *  net.minecraft.client.renderer.entity.RenderSkeleton
 *  net.minecraft.client.renderer.entity.RenderSlime
 *  net.minecraft.client.renderer.entity.RenderSnowMan
 *  net.minecraft.client.renderer.entity.RenderSnowball
 *  net.minecraft.client.renderer.entity.RenderSpectralArrow
 *  net.minecraft.client.renderer.entity.RenderSpider
 *  net.minecraft.client.renderer.entity.RenderSquid
 *  net.minecraft.client.renderer.entity.RenderStray
 *  net.minecraft.client.renderer.entity.RenderTNTPrimed
 *  net.minecraft.client.renderer.entity.RenderTippedArrow
 *  net.minecraft.client.renderer.entity.RenderTntMinecart
 *  net.minecraft.client.renderer.entity.RenderVex
 *  net.minecraft.client.renderer.entity.RenderVillager
 *  net.minecraft.client.renderer.entity.RenderVindicator
 *  net.minecraft.client.renderer.entity.RenderWitch
 *  net.minecraft.client.renderer.entity.RenderWither
 *  net.minecraft.client.renderer.entity.RenderWitherSkeleton
 *  net.minecraft.client.renderer.entity.RenderWitherSkull
 *  net.minecraft.client.renderer.entity.RenderWolf
 *  net.minecraft.client.renderer.entity.RenderXPOrb
 *  net.minecraft.client.renderer.entity.RenderZombie
 *  net.minecraft.client.renderer.entity.RenderZombieVillager
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityAreaEffectCloud
 *  net.minecraft.entity.EntityLeashKnot
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.boss.EntityDragon
 *  net.minecraft.entity.boss.EntityWither
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityEnderEye
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityFallingBlock
 *  net.minecraft.entity.item.EntityFireworkRocket
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.item.EntityMinecartMobSpawner
 *  net.minecraft.entity.item.EntityMinecartTNT
 *  net.minecraft.entity.item.EntityPainting
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.monster.EntityBlaze
 *  net.minecraft.entity.monster.EntityCaveSpider
 *  net.minecraft.entity.monster.EntityCreeper
 *  net.minecraft.entity.monster.EntityElderGuardian
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.monster.EntityEndermite
 *  net.minecraft.entity.monster.EntityEvoker
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.monster.EntityGiantZombie
 *  net.minecraft.entity.monster.EntityGuardian
 *  net.minecraft.entity.monster.EntityHusk
 *  net.minecraft.entity.monster.EntityIllusionIllager
 *  net.minecraft.entity.monster.EntityIronGolem
 *  net.minecraft.entity.monster.EntityMagmaCube
 *  net.minecraft.entity.monster.EntityPigZombie
 *  net.minecraft.entity.monster.EntityPolarBear
 *  net.minecraft.entity.monster.EntityShulker
 *  net.minecraft.entity.monster.EntitySilverfish
 *  net.minecraft.entity.monster.EntitySkeleton
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.monster.EntitySnowman
 *  net.minecraft.entity.monster.EntitySpider
 *  net.minecraft.entity.monster.EntityStray
 *  net.minecraft.entity.monster.EntityVex
 *  net.minecraft.entity.monster.EntityVindicator
 *  net.minecraft.entity.monster.EntityWitch
 *  net.minecraft.entity.monster.EntityWitherSkeleton
 *  net.minecraft.entity.monster.EntityZombie
 *  net.minecraft.entity.monster.EntityZombieVillager
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.passive.EntityChicken
 *  net.minecraft.entity.passive.EntityCow
 *  net.minecraft.entity.passive.EntityDonkey
 *  net.minecraft.entity.passive.EntityHorse
 *  net.minecraft.entity.passive.EntityLlama
 *  net.minecraft.entity.passive.EntityMooshroom
 *  net.minecraft.entity.passive.EntityMule
 *  net.minecraft.entity.passive.EntityOcelot
 *  net.minecraft.entity.passive.EntityParrot
 *  net.minecraft.entity.passive.EntityPig
 *  net.minecraft.entity.passive.EntityRabbit
 *  net.minecraft.entity.passive.EntitySheep
 *  net.minecraft.entity.passive.EntitySkeletonHorse
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.passive.EntityZombieHorse
 *  net.minecraft.entity.projectile.EntityDragonFireball
 *  net.minecraft.entity.projectile.EntityEgg
 *  net.minecraft.entity.projectile.EntityEvokerFangs
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.entity.projectile.EntityLargeFireball
 *  net.minecraft.entity.projectile.EntityLlamaSpit
 *  net.minecraft.entity.projectile.EntityPotion
 *  net.minecraft.entity.projectile.EntityShulkerBullet
 *  net.minecraft.entity.projectile.EntitySmallFireball
 *  net.minecraft.entity.projectile.EntitySnowball
 *  net.minecraft.entity.projectile.EntitySpectralArrow
 *  net.minecraft.entity.projectile.EntityTippedArrow
 *  net.minecraft.entity.projectile.EntityWitherSkull
 *  net.minecraft.init.Items
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.client.registry.RenderingRegistry
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderAbstractHorse;
import net.minecraft.client.renderer.entity.RenderAreaEffectCloud;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.renderer.entity.RenderBlaze;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderCaveSpider;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderDragonFireball;
import net.minecraft.client.renderer.entity.RenderElderGuardian;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderEndermite;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderEvoker;
import net.minecraft.client.renderer.entity.RenderEvokerFangs;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderGiantZombie;
import net.minecraft.client.renderer.entity.RenderGuardian;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderHusk;
import net.minecraft.client.renderer.entity.RenderIllusionIllager;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.renderer.entity.RenderLightningBolt;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.RenderLlamaSpit;
import net.minecraft.client.renderer.entity.RenderMagmaCube;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.entity.RenderMinecartMobSpawner;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.client.renderer.entity.RenderPainting;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.client.renderer.entity.RenderPigZombie;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderPolarBear;
import net.minecraft.client.renderer.entity.RenderPotion;
import net.minecraft.client.renderer.entity.RenderRabbit;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.client.renderer.entity.RenderShulkerBullet;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.client.renderer.entity.RenderSnowMan;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.RenderSpectralArrow;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.RenderSquid;
import net.minecraft.client.renderer.entity.RenderStray;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.client.renderer.entity.RenderTntMinecart;
import net.minecraft.client.renderer.entity.RenderVex;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.client.renderer.entity.RenderVindicator;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.client.renderer.entity.RenderWitherSkull;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.client.renderer.entity.RenderZombieVillager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class RenderManager {
    public final Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap = Maps.newHashMap();
    private final Map<String, RenderPlayer> skinMap = Maps.newHashMap();
    private final RenderPlayer playerRenderer;
    private FontRenderer textRenderer;
    private double renderPosX;
    private double renderPosY;
    private double renderPosZ;
    public TextureManager renderEngine;
    public World world;
    public Entity renderViewEntity;
    public Entity pointedEntity;
    public float playerViewY;
    public float playerViewX;
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean renderOutlines;
    private boolean renderShadow = true;
    private boolean debugBoundingBox;

    public RenderManager(TextureManager renderEngineIn, RenderItem itemRendererIn) {
        this.renderEngine = renderEngineIn;
        this.entityRenderMap.put(EntityCaveSpider.class, (Object)new RenderCaveSpider(this));
        this.entityRenderMap.put(EntitySpider.class, (Object)new RenderSpider(this));
        this.entityRenderMap.put(EntityPig.class, (Object)new RenderPig(this));
        this.entityRenderMap.put(EntitySheep.class, (Object)new RenderSheep(this));
        this.entityRenderMap.put(EntityCow.class, (Object)new RenderCow(this));
        this.entityRenderMap.put(EntityMooshroom.class, (Object)new RenderMooshroom(this));
        this.entityRenderMap.put(EntityWolf.class, (Object)new RenderWolf(this));
        this.entityRenderMap.put(EntityChicken.class, (Object)new RenderChicken(this));
        this.entityRenderMap.put(EntityOcelot.class, (Object)new RenderOcelot(this));
        this.entityRenderMap.put(EntityRabbit.class, (Object)new RenderRabbit(this));
        this.entityRenderMap.put(EntityParrot.class, (Object)new RenderParrot(this));
        this.entityRenderMap.put(EntitySilverfish.class, (Object)new RenderSilverfish(this));
        this.entityRenderMap.put(EntityEndermite.class, (Object)new RenderEndermite(this));
        this.entityRenderMap.put(EntityCreeper.class, (Object)new RenderCreeper(this));
        this.entityRenderMap.put(EntityEnderman.class, (Object)new RenderEnderman(this));
        this.entityRenderMap.put(EntitySnowman.class, (Object)new RenderSnowMan(this));
        this.entityRenderMap.put(EntitySkeleton.class, (Object)new RenderSkeleton(this));
        this.entityRenderMap.put(EntityWitherSkeleton.class, (Object)new RenderWitherSkeleton(this));
        this.entityRenderMap.put(EntityStray.class, (Object)new RenderStray(this));
        this.entityRenderMap.put(EntityWitch.class, (Object)new RenderWitch(this));
        this.entityRenderMap.put(EntityBlaze.class, (Object)new RenderBlaze(this));
        this.entityRenderMap.put(EntityPigZombie.class, (Object)new RenderPigZombie(this));
        this.entityRenderMap.put(EntityZombie.class, (Object)new RenderZombie(this));
        this.entityRenderMap.put(EntityZombieVillager.class, (Object)new RenderZombieVillager(this));
        this.entityRenderMap.put(EntityHusk.class, (Object)new RenderHusk(this));
        this.entityRenderMap.put(EntitySlime.class, (Object)new RenderSlime(this));
        this.entityRenderMap.put(EntityMagmaCube.class, (Object)new RenderMagmaCube(this));
        this.entityRenderMap.put(EntityGiantZombie.class, (Object)new RenderGiantZombie(this, 6.0f));
        this.entityRenderMap.put(EntityGhast.class, (Object)new RenderGhast(this));
        this.entityRenderMap.put(EntitySquid.class, (Object)new RenderSquid(this));
        this.entityRenderMap.put(EntityVillager.class, (Object)new RenderVillager(this));
        this.entityRenderMap.put(EntityIronGolem.class, (Object)new RenderIronGolem(this));
        this.entityRenderMap.put(EntityBat.class, (Object)new RenderBat(this));
        this.entityRenderMap.put(EntityGuardian.class, (Object)new RenderGuardian(this));
        this.entityRenderMap.put(EntityElderGuardian.class, (Object)new RenderElderGuardian(this));
        this.entityRenderMap.put(EntityShulker.class, (Object)new RenderShulker(this));
        this.entityRenderMap.put(EntityPolarBear.class, (Object)new RenderPolarBear(this));
        this.entityRenderMap.put(EntityEvoker.class, (Object)new RenderEvoker(this));
        this.entityRenderMap.put(EntityVindicator.class, (Object)new RenderVindicator(this));
        this.entityRenderMap.put(EntityVex.class, (Object)new RenderVex(this));
        this.entityRenderMap.put(EntityIllusionIllager.class, (Object)new RenderIllusionIllager(this));
        this.entityRenderMap.put(EntityDragon.class, (Object)new RenderDragon(this));
        this.entityRenderMap.put(EntityEnderCrystal.class, (Object)new RenderEnderCrystal(this));
        this.entityRenderMap.put(EntityWither.class, (Object)new RenderWither(this));
        this.entityRenderMap.put(Entity.class, (Object)new RenderEntity(this));
        this.entityRenderMap.put(EntityPainting.class, (Object)new RenderPainting(this));
        this.entityRenderMap.put(EntityItemFrame.class, (Object)new RenderItemFrame(this, itemRendererIn));
        this.entityRenderMap.put(EntityLeashKnot.class, (Object)new RenderLeashKnot(this));
        this.entityRenderMap.put(EntityTippedArrow.class, (Object)new RenderTippedArrow(this));
        this.entityRenderMap.put(EntitySpectralArrow.class, (Object)new RenderSpectralArrow(this));
        this.entityRenderMap.put(EntitySnowball.class, (Object)new RenderSnowball(this, Items.SNOWBALL, itemRendererIn));
        this.entityRenderMap.put(EntityEnderPearl.class, (Object)new RenderSnowball(this, Items.ENDER_PEARL, itemRendererIn));
        this.entityRenderMap.put(EntityEnderEye.class, (Object)new RenderSnowball(this, Items.ENDER_EYE, itemRendererIn));
        this.entityRenderMap.put(EntityEgg.class, (Object)new RenderSnowball(this, Items.EGG, itemRendererIn));
        this.entityRenderMap.put(EntityPotion.class, (Object)new RenderPotion(this, itemRendererIn));
        this.entityRenderMap.put(EntityExpBottle.class, (Object)new RenderSnowball(this, Items.EXPERIENCE_BOTTLE, itemRendererIn));
        this.entityRenderMap.put(EntityFireworkRocket.class, (Object)new RenderSnowball(this, Items.FIREWORKS, itemRendererIn));
        this.entityRenderMap.put(EntityLargeFireball.class, (Object)new RenderFireball(this, 2.0f));
        this.entityRenderMap.put(EntitySmallFireball.class, (Object)new RenderFireball(this, 0.5f));
        this.entityRenderMap.put(EntityDragonFireball.class, (Object)new RenderDragonFireball(this));
        this.entityRenderMap.put(EntityWitherSkull.class, (Object)new RenderWitherSkull(this));
        this.entityRenderMap.put(EntityShulkerBullet.class, (Object)new RenderShulkerBullet(this));
        this.entityRenderMap.put(EntityItem.class, (Object)new RenderEntityItem(this, itemRendererIn));
        this.entityRenderMap.put(EntityXPOrb.class, (Object)new RenderXPOrb(this));
        this.entityRenderMap.put(EntityTNTPrimed.class, (Object)new RenderTNTPrimed(this));
        this.entityRenderMap.put(EntityFallingBlock.class, (Object)new RenderFallingBlock(this));
        this.entityRenderMap.put(EntityArmorStand.class, (Object)new RenderArmorStand(this));
        this.entityRenderMap.put(EntityEvokerFangs.class, (Object)new RenderEvokerFangs(this));
        this.entityRenderMap.put(EntityMinecartTNT.class, (Object)new RenderTntMinecart(this));
        this.entityRenderMap.put(EntityMinecartMobSpawner.class, (Object)new RenderMinecartMobSpawner(this));
        this.entityRenderMap.put(EntityMinecart.class, (Object)new RenderMinecart(this));
        this.entityRenderMap.put(EntityBoat.class, (Object)new RenderBoat(this));
        this.entityRenderMap.put(EntityFishHook.class, (Object)new RenderFish(this));
        this.entityRenderMap.put(EntityAreaEffectCloud.class, (Object)new RenderAreaEffectCloud(this));
        this.entityRenderMap.put(EntityHorse.class, (Object)new RenderHorse(this));
        this.entityRenderMap.put(EntitySkeletonHorse.class, (Object)new RenderAbstractHorse(this));
        this.entityRenderMap.put(EntityZombieHorse.class, (Object)new RenderAbstractHorse(this));
        this.entityRenderMap.put(EntityMule.class, (Object)new RenderAbstractHorse(this, 0.92f));
        this.entityRenderMap.put(EntityDonkey.class, (Object)new RenderAbstractHorse(this, 0.87f));
        this.entityRenderMap.put(EntityLlama.class, (Object)new RenderLlama(this));
        this.entityRenderMap.put(EntityLlamaSpit.class, (Object)new RenderLlamaSpit(this));
        this.entityRenderMap.put(EntityLightningBolt.class, (Object)new RenderLightningBolt(this));
        this.playerRenderer = new RenderPlayer(this);
        this.skinMap.put((Object)"default", (Object)this.playerRenderer);
        this.skinMap.put((Object)"slim", (Object)new RenderPlayer(this, true));
        RenderingRegistry.loadEntityRenderers((RenderManager)this, this.entityRenderMap);
    }

    public Map<String, RenderPlayer> getSkinMap() {
        return Collections.unmodifiableMap(this.skinMap);
    }

    public void setRenderPosition(double renderPosXIn, double renderPosYIn, double renderPosZIn) {
        this.renderPosX = renderPosXIn;
        this.renderPosY = renderPosYIn;
        this.renderPosZ = renderPosZIn;
    }

    public <T extends Entity> Render<T> getEntityClassRenderObject(Class<? extends Entity> entityClass) {
        Render<T> render = (Render<T>)this.entityRenderMap.get(entityClass);
        if (render == null && entityClass != Entity.class) {
            render = this.getEntityClassRenderObject((Class<? extends Entity>)entityClass.getSuperclass());
            this.entityRenderMap.put(entityClass, render);
        }
        return render;
    }

    @Nullable
    public <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn) {
        if (entityIn instanceof AbstractClientPlayer) {
            String s = ((AbstractClientPlayer)entityIn).getSkinType();
            RenderPlayer renderplayer = (RenderPlayer)this.skinMap.get((Object)s);
            return renderplayer != null ? renderplayer : this.playerRenderer;
        }
        return this.getEntityClassRenderObject((Class<? extends Entity>)entityIn.getClass());
    }

    public void cacheActiveRenderInfo(World worldIn, FontRenderer textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks) {
        this.world = worldIn;
        this.options = optionsIn;
        this.renderViewEntity = livingPlayerIn;
        this.pointedEntity = pointedEntityIn;
        this.textRenderer = textRendererIn;
        if (livingPlayerIn instanceof EntityLivingBase && ((EntityLivingBase)livingPlayerIn).isPlayerSleeping()) {
            IBlockState iblockstate = worldIn.getBlockState(new BlockPos(livingPlayerIn));
            Block block = iblockstate.getBlock();
            if (block.isBed(iblockstate, (IBlockAccess)worldIn, new BlockPos(livingPlayerIn), (Entity)((EntityLivingBase)livingPlayerIn))) {
                int i = block.getBedDirection(iblockstate, (IBlockAccess)worldIn, new BlockPos(livingPlayerIn)).getHorizontalIndex();
                this.playerViewY = i * 90 + 180;
                this.playerViewX = 0.0f;
            }
        } else {
            this.playerViewY = livingPlayerIn.prevRotationYaw + (livingPlayerIn.rotationYaw - livingPlayerIn.prevRotationYaw) * partialTicks;
            this.playerViewX = livingPlayerIn.prevRotationPitch + (livingPlayerIn.rotationPitch - livingPlayerIn.prevRotationPitch) * partialTicks;
        }
        if (optionsIn.thirdPersonView == 2) {
            this.playerViewY += 180.0f;
        }
        this.viewerPosX = livingPlayerIn.lastTickPosX + (livingPlayerIn.posX - livingPlayerIn.lastTickPosX) * (double)partialTicks;
        this.viewerPosY = livingPlayerIn.lastTickPosY + (livingPlayerIn.posY - livingPlayerIn.lastTickPosY) * (double)partialTicks;
        this.viewerPosZ = livingPlayerIn.lastTickPosZ + (livingPlayerIn.posZ - livingPlayerIn.lastTickPosZ) * (double)partialTicks;
    }

    public void setPlayerViewY(float playerViewYIn) {
        this.playerViewY = playerViewYIn;
    }

    public boolean isRenderShadow() {
        return this.renderShadow;
    }

    public void setRenderShadow(boolean renderShadowIn) {
        this.renderShadow = renderShadowIn;
    }

    public void setDebugBoundingBox(boolean debugBoundingBoxIn) {
        this.debugBoundingBox = debugBoundingBoxIn;
    }

    public boolean isDebugBoundingBox() {
        return this.debugBoundingBox;
    }

    public boolean isRenderMultipass(Entity entityIn) {
        return this.getEntityRenderObject(entityIn).isMultipass();
    }

    public boolean shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ) {
        Render render = this.getEntityRenderObject(entityIn);
        return render != null && render.shouldRender(entityIn, camera, camX, camY, camZ);
    }

    public void renderEntityStatic(Entity entityIn, float partialTicks, boolean p_188388_3_) {
        if (entityIn.ticksExisted == 0) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();
        if (entityIn.isBurning()) {
            i = 0xF000F0;
        }
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.renderEntity(entityIn, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ, f, partialTicks, p_188388_3_);
    }

    public void renderEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_) {
        block9: {
            Render render = null;
            try {
                render = this.getEntityRenderObject(entityIn);
                if (render == null || this.renderEngine == null) break block9;
                try {
                    render.setRenderOutlines(this.renderOutlines);
                    render.doRender(entityIn, x, y, z, yaw, partialTicks);
                }
                catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.makeCrashReport((Throwable)throwable1, (String)"Rendering entity in world"));
                }
                try {
                    if (!this.renderOutlines) {
                        render.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
                    }
                }
                catch (Throwable throwable2) {
                    throw new ReportedException(CrashReport.makeCrashReport((Throwable)throwable2, (String)"Post-rendering entity in world"));
                }
                if (!this.debugBoundingBox || entityIn.isInvisible() || p_188391_10_ || Minecraft.getMinecraft().isReducedDebug()) break block9;
                try {
                    this.renderDebugBoundingBox(entityIn, x, y, z, yaw, partialTicks);
                }
                catch (Throwable throwable) {
                    throw new ReportedException(CrashReport.makeCrashReport((Throwable)throwable, (String)"Rendering entity hitbox in world"));
                }
            }
            catch (Throwable throwable3) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable3, (String)"Rendering entity in world");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
                entityIn.addEntityCrashInfo(crashreportcategory);
                CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
                crashreportcategory1.addCrashSection("Assigned renderer", render);
                crashreportcategory1.addCrashSection("Location", (Object)CrashReportCategory.getCoordinateInfo((double)x, (double)y, (double)z));
                crashreportcategory1.addCrashSection("Rotation", (Object)Float.valueOf((float)yaw));
                crashreportcategory1.addCrashSection("Delta", (Object)Float.valueOf((float)partialTicks));
                throw new ReportedException(crashreport);
            }
        }
    }

    public void renderMultipass(Entity entityIn, float partialTicks) {
        if (entityIn.ticksExisted == 0) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();
        if (entityIn.isBurning()) {
            i = 0xF000F0;
        }
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Render render = this.getEntityRenderObject(entityIn);
        if (render != null && this.renderEngine != null) {
            render.renderMultipass(entityIn, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ, f, partialTicks);
        }
    }

    private void renderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.depthMask((boolean)false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        float f = entityIn.width / 2.0f;
        AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
        RenderGlobal.drawBoundingBox((double)(axisalignedbb.minX - entityIn.posX + x), (double)(axisalignedbb.minY - entityIn.posY + y), (double)(axisalignedbb.minZ - entityIn.posZ + z), (double)(axisalignedbb.maxX - entityIn.posX + x), (double)(axisalignedbb.maxY - entityIn.posY + y), (double)(axisalignedbb.maxZ - entityIn.posZ + z), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Entity[] aentity = entityIn.getParts();
        if (aentity != null) {
            for (Entity entity : aentity) {
                double d0 = (entity.posX - entity.prevPosX) * (double)partialTicks;
                double d1 = (entity.posY - entity.prevPosY) * (double)partialTicks;
                double d2 = (entity.posZ - entity.prevPosZ) * (double)partialTicks;
                AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();
                RenderGlobal.drawBoundingBox((double)(axisalignedbb1.minX - this.renderPosX + d0), (double)(axisalignedbb1.minY - this.renderPosY + d1), (double)(axisalignedbb1.minZ - this.renderPosZ + d2), (double)(axisalignedbb1.maxX - this.renderPosX + d0), (double)(axisalignedbb1.maxY - this.renderPosY + d1), (double)(axisalignedbb1.maxZ - this.renderPosZ + d2), (float)0.25f, (float)1.0f, (float)0.0f, (float)1.0f);
            }
        }
        if (entityIn instanceof EntityLivingBase) {
            float f1 = 0.01f;
            RenderGlobal.drawBoundingBox((double)(x - (double)f), (double)(y + (double)entityIn.getEyeHeight() - (double)0.01f), (double)(z - (double)f), (double)(x + (double)f), (double)(y + (double)entityIn.getEyeHeight() + (double)0.01f), (double)(z + (double)f), (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Vec3d vec3d = entityIn.getLook(partialTicks);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y + (double)entityIn.getEyeHeight(), z).color(0, 0, 255, 255).endVertex();
        bufferbuilder.pos(x + vec3d.x * 2.0, y + (double)entityIn.getEyeHeight() + vec3d.y * 2.0, z + vec3d.z * 2.0).color(0, 0, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask((boolean)true);
    }

    public void setWorld(@Nullable World worldIn) {
        this.world = worldIn;
        if (worldIn == null) {
            this.renderViewEntity = null;
        }
    }

    public double getDistanceToCamera(double x, double y, double z) {
        double d0 = x - this.viewerPosX;
        double d1 = y - this.viewerPosY;
        double d2 = z - this.viewerPosZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public FontRenderer getFontRenderer() {
        return this.textRenderer;
    }

    public void setRenderOutlines(boolean renderOutlinesIn) {
        this.renderOutlines = renderOutlinesIn;
    }
}
