/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Queues
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayDeque
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Queue
 *  java.util.Random
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.particle.Barrier
 *  net.minecraft.client.particle.Barrier$Factory
 *  net.minecraft.client.particle.IParticleFactory
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleBlockDust$Factory
 *  net.minecraft.client.particle.ParticleBreaking$Factory
 *  net.minecraft.client.particle.ParticleBreaking$SlimeFactory
 *  net.minecraft.client.particle.ParticleBreaking$SnowballFactory
 *  net.minecraft.client.particle.ParticleBubble$Factory
 *  net.minecraft.client.particle.ParticleCloud$Factory
 *  net.minecraft.client.particle.ParticleCrit$DamageIndicatorFactory
 *  net.minecraft.client.particle.ParticleCrit$Factory
 *  net.minecraft.client.particle.ParticleCrit$MagicFactory
 *  net.minecraft.client.particle.ParticleDigging
 *  net.minecraft.client.particle.ParticleDigging$Factory
 *  net.minecraft.client.particle.ParticleDragonBreath$Factory
 *  net.minecraft.client.particle.ParticleDrip$LavaFactory
 *  net.minecraft.client.particle.ParticleDrip$WaterFactory
 *  net.minecraft.client.particle.ParticleEmitter
 *  net.minecraft.client.particle.ParticleEnchantmentTable$EnchantmentTable
 *  net.minecraft.client.particle.ParticleEndRod$Factory
 *  net.minecraft.client.particle.ParticleExplosion$Factory
 *  net.minecraft.client.particle.ParticleExplosionHuge$Factory
 *  net.minecraft.client.particle.ParticleExplosionLarge$Factory
 *  net.minecraft.client.particle.ParticleFallingDust$Factory
 *  net.minecraft.client.particle.ParticleFirework$Factory
 *  net.minecraft.client.particle.ParticleFirework$Spark
 *  net.minecraft.client.particle.ParticleFlame$Factory
 *  net.minecraft.client.particle.ParticleFootStep$Factory
 *  net.minecraft.client.particle.ParticleHeart$AngryVillagerFactory
 *  net.minecraft.client.particle.ParticleHeart$Factory
 *  net.minecraft.client.particle.ParticleLava$Factory
 *  net.minecraft.client.particle.ParticleMobAppearance$Factory
 *  net.minecraft.client.particle.ParticleNote$Factory
 *  net.minecraft.client.particle.ParticlePortal$Factory
 *  net.minecraft.client.particle.ParticleRain$Factory
 *  net.minecraft.client.particle.ParticleRedstone$Factory
 *  net.minecraft.client.particle.ParticleSmokeLarge$Factory
 *  net.minecraft.client.particle.ParticleSmokeNormal$Factory
 *  net.minecraft.client.particle.ParticleSnowShovel$Factory
 *  net.minecraft.client.particle.ParticleSpell$AmbientMobFactory
 *  net.minecraft.client.particle.ParticleSpell$Factory
 *  net.minecraft.client.particle.ParticleSpell$InstantFactory
 *  net.minecraft.client.particle.ParticleSpell$MobFactory
 *  net.minecraft.client.particle.ParticleSpell$WitchFactory
 *  net.minecraft.client.particle.ParticleSpit$Factory
 *  net.minecraft.client.particle.ParticleSplash$Factory
 *  net.minecraft.client.particle.ParticleSuspend
 *  net.minecraft.client.particle.ParticleSuspend$Factory
 *  net.minecraft.client.particle.ParticleSuspendedTown$Factory
 *  net.minecraft.client.particle.ParticleSuspendedTown$HappyVillagerFactory
 *  net.minecraft.client.particle.ParticleSweepAttack$Factory
 *  net.minecraft.client.particle.ParticleTotem$Factory
 *  net.minecraft.client.particle.ParticleWaterWake$Factory
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.client.particle.ParticleCrit;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleDragonBreath;
import net.minecraft.client.particle.ParticleDrip;
import net.minecraft.client.particle.ParticleEmitter;
import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.client.particle.ParticleExplosionHuge;
import net.minecraft.client.particle.ParticleExplosionLarge;
import net.minecraft.client.particle.ParticleFallingDust;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleFootStep;
import net.minecraft.client.particle.ParticleHeart;
import net.minecraft.client.particle.ParticleLava;
import net.minecraft.client.particle.ParticleMobAppearance;
import net.minecraft.client.particle.ParticleNote;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.client.particle.ParticleRain;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.particle.ParticleSnowShovel;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.client.particle.ParticleSpit;
import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.client.particle.ParticleSuspend;
import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.client.particle.ParticleSweepAttack;
import net.minecraft.client.particle.ParticleTotem;
import net.minecraft.client.particle.ParticleWaterWake;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public class ParticleManager {
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    protected World world;
    private final ArrayDeque<Particle>[][] fxLayers = new ArrayDeque[4][];
    private final Queue<ParticleEmitter> particleEmitters = Queues.newArrayDeque();
    private final TextureManager renderer;
    private final Random rand = new Random();
    private final Map<Integer, IParticleFactory> particleTypes = Maps.newHashMap();
    private final Queue<Particle> queue = Queues.newArrayDeque();

    public ParticleManager(World worldIn, TextureManager rendererIn) {
        this.world = worldIn;
        this.renderer = rendererIn;
        for (int i = 0; i < 4; ++i) {
            this.fxLayers[i] = new ArrayDeque[2];
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j] = Queues.newArrayDeque();
            }
        }
        this.registerVanillaParticles();
    }

    private void registerVanillaParticles() {
        this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), (IParticleFactory)new ParticleExplosion.Factory());
        this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), (IParticleFactory)new ParticleSpit.Factory());
        this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), (IParticleFactory)new ParticleBubble.Factory());
        this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), (IParticleFactory)new ParticleSplash.Factory());
        this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), (IParticleFactory)new ParticleWaterWake.Factory());
        this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), (IParticleFactory)new ParticleRain.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), (IParticleFactory)new ParticleSuspend.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), (IParticleFactory)new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), (IParticleFactory)new ParticleCrit.Factory());
        this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), (IParticleFactory)new ParticleCrit.MagicFactory());
        this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), (IParticleFactory)new ParticleSmokeNormal.Factory());
        this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), (IParticleFactory)new ParticleSmokeLarge.Factory());
        this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), (IParticleFactory)new ParticleSpell.Factory());
        this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), (IParticleFactory)new ParticleSpell.InstantFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), (IParticleFactory)new ParticleSpell.MobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), (IParticleFactory)new ParticleSpell.AmbientMobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), (IParticleFactory)new ParticleSpell.WitchFactory());
        this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), (IParticleFactory)new ParticleDrip.WaterFactory());
        this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), (IParticleFactory)new ParticleDrip.LavaFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), (IParticleFactory)new ParticleHeart.AngryVillagerFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), (IParticleFactory)new ParticleSuspendedTown.HappyVillagerFactory());
        this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), (IParticleFactory)new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), (IParticleFactory)new ParticleNote.Factory());
        this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), (IParticleFactory)new ParticlePortal.Factory());
        this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), (IParticleFactory)new ParticleEnchantmentTable.EnchantmentTable());
        this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), (IParticleFactory)new ParticleFlame.Factory());
        this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), (IParticleFactory)new ParticleLava.Factory());
        this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), (IParticleFactory)new ParticleFootStep.Factory());
        this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), (IParticleFactory)new ParticleCloud.Factory());
        this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), (IParticleFactory)new ParticleRedstone.Factory());
        this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), (IParticleFactory)new ParticleFallingDust.Factory());
        this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), (IParticleFactory)new ParticleBreaking.SnowballFactory());
        this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), (IParticleFactory)new ParticleSnowShovel.Factory());
        this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), (IParticleFactory)new ParticleBreaking.SlimeFactory());
        this.registerParticle(EnumParticleTypes.HEART.getParticleID(), (IParticleFactory)new ParticleHeart.Factory());
        this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), (IParticleFactory)new Barrier.Factory());
        this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), (IParticleFactory)new ParticleBreaking.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), (IParticleFactory)new ParticleDigging.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), (IParticleFactory)new ParticleBlockDust.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), (IParticleFactory)new ParticleExplosionHuge.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), (IParticleFactory)new ParticleExplosionLarge.Factory());
        this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), (IParticleFactory)new ParticleFirework.Factory());
        this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), (IParticleFactory)new ParticleMobAppearance.Factory());
        this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), (IParticleFactory)new ParticleDragonBreath.Factory());
        this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), (IParticleFactory)new ParticleEndRod.Factory());
        this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), (IParticleFactory)new ParticleCrit.DamageIndicatorFactory());
        this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), (IParticleFactory)new ParticleSweepAttack.Factory());
        this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), (IParticleFactory)new ParticleTotem.Factory());
    }

    public void registerParticle(int id, IParticleFactory particleFactory) {
        this.particleTypes.put((Object)id, (Object)particleFactory);
    }

    public void emitParticleAtEntity(Entity entityIn, EnumParticleTypes particleTypes) {
        this.particleEmitters.add((Object)new ParticleEmitter(this.world, entityIn, particleTypes));
    }

    public void emitParticleAtEntity(Entity p_191271_1_, EnumParticleTypes p_191271_2_, int p_191271_3_) {
        this.particleEmitters.add((Object)new ParticleEmitter(this.world, p_191271_1_, p_191271_2_, p_191271_3_));
    }

    @Nullable
    public Particle spawnEffectParticle(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
        Particle particle;
        IParticleFactory iparticlefactory = (IParticleFactory)this.particleTypes.get((Object)particleId);
        if (iparticlefactory != null && (particle = iparticlefactory.createParticle(particleId, this.world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters)) != null) {
            this.addEffect(particle);
            return particle;
        }
        return null;
    }

    public void addEffect(Particle effect) {
        if (effect == null) {
            return;
        }
        if (effect instanceof ParticleFirework.Spark && !Config.isFireworkParticles()) {
            return;
        }
        this.queue.add((Object)effect);
    }

    public void updateEffects() {
        for (int i = 0; i < 4; ++i) {
            this.updateEffectLayer(i);
        }
        if (!this.particleEmitters.isEmpty()) {
            ArrayList list = Lists.newArrayList();
            for (ParticleEmitter particleemitter : this.particleEmitters) {
                particleemitter.onUpdate();
                if (particleemitter.k()) continue;
                list.add((Object)particleemitter);
            }
            this.particleEmitters.removeAll((Collection)list);
        }
        if (!this.queue.isEmpty()) {
            Particle particle = (Particle)this.queue.poll();
            while (particle != null) {
                int k;
                int j = particle.getFXLayer();
                int n = k = particle.shouldDisableDepth() ? 0 : 1;
                if (this.fxLayers[j][k].size() >= 16384) {
                    this.fxLayers[j][k].removeFirst();
                }
                if (!(particle instanceof Barrier) || !this.reuseBarrierParticle(particle, this.fxLayers[j][k])) {
                    this.fxLayers[j][k].add((Object)particle);
                }
                particle = (Particle)this.queue.poll();
            }
        }
    }

    private void updateEffectLayer(int layer) {
        this.world.profiler.startSection(String.valueOf((int)layer));
        for (int i = 0; i < 2; ++i) {
            this.world.profiler.startSection(String.valueOf((int)i));
            this.tickParticleList((Queue<Particle>)this.fxLayers[layer][i]);
            this.world.profiler.endSection();
        }
        this.world.profiler.endSection();
    }

    private void tickParticleList(Queue<Particle> p_187240_1_) {
        if (!p_187240_1_.isEmpty()) {
            Queue<Particle> particlesIn = p_187240_1_;
            long timeStartMs = System.currentTimeMillis();
            int countLeft = particlesIn.size();
            Iterator iterator = p_187240_1_.iterator();
            while (iterator.hasNext()) {
                Particle particle = (Particle)iterator.next();
                this.tickParticle(particle);
                if (!particle.isAlive()) {
                    iterator.remove();
                }
                --countLeft;
                if (System.currentTimeMillis() <= timeStartMs + 20L) continue;
                break;
            }
            if (countLeft > 0) {
                Iterator it = particlesIn.iterator();
                for (int countToRemove = countLeft; it.hasNext() && countToRemove > 0; --countToRemove) {
                    Particle particle = (Particle)it.next();
                    particle.setExpired();
                    it.remove();
                }
            }
        }
    }

    private void tickParticle(Particle particle) {
        try {
            particle.onUpdate();
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            int i = particle.getFXLayer();
            crashreportcategory.addDetail("Particle", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
            crashreportcategory.addDetail("Particle Type", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
            throw new ReportedException(crashreport);
        }
    }

    public void renderParticles(Entity entityIn, float partialTicks) {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        Particle.cameraViewDir = entityIn.getLook(partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc((int)516, (float)0.003921569f);
        IBlockState cameraBlockState = ActiveRenderInfo.getBlockStateAtEntityViewpoint((World)this.world, (Entity)entityIn, (float)partialTicks);
        boolean isEyeInWater = cameraBlockState.a() == Material.WATER;
        for (int i_nf = 0; i_nf < 3; ++i_nf) {
            int i = i_nf;
            for (int j = 0; j < 2; ++j) {
                if (this.fxLayers[i][j].isEmpty()) continue;
                switch (j) {
                    case 0: {
                        GlStateManager.depthMask((boolean)false);
                        break;
                    }
                    case 1: {
                        GlStateManager.depthMask((boolean)true);
                    }
                }
                switch (i) {
                    default: {
                        this.renderer.bindTexture(PARTICLE_TEXTURES);
                        break;
                    }
                    case 1: {
                        this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    }
                }
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                for (Particle particle : this.fxLayers[i][j]) {
                    try {
                        if (!isEyeInWater && particle instanceof ParticleSuspend) continue;
                        particle.renderParticle(bufferbuilder, entityIn, partialTicks, f, f4, f1, f2, f3);
                    }
                    catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Rendering Particle");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                        crashreportcategory.addDetail("Particle", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                        crashreportcategory.addDetail("Particle Type", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
                        throw new ReportedException(crashreport);
                    }
                }
                tessellator.draw();
            }
        }
        GlStateManager.depthMask((boolean)true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
    }

    public void renderLitParticles(Entity entityIn, float partialTick) {
        float f = (float)Math.PI / 180;
        float f1 = MathHelper.cos((float)(entityIn.rotationYaw * ((float)Math.PI / 180)));
        float f2 = MathHelper.sin((float)(entityIn.rotationYaw * ((float)Math.PI / 180)));
        float f3 = -f2 * MathHelper.sin((float)(entityIn.rotationPitch * ((float)Math.PI / 180)));
        float f4 = f1 * MathHelper.sin((float)(entityIn.rotationPitch * ((float)Math.PI / 180)));
        float f5 = MathHelper.cos((float)(entityIn.rotationPitch * ((float)Math.PI / 180)));
        for (int i = 0; i < 2; ++i) {
            ArrayDeque<Particle> queue = this.fxLayers[3][i];
            if (queue.isEmpty()) continue;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            for (Particle particle : queue) {
                particle.renderParticle(bufferbuilder, entityIn, partialTick, f1, f5, f2, f3, f4);
            }
        }
    }

    public void clearEffects(@Nullable World worldIn) {
        this.world = worldIn;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j].clear();
            }
        }
        this.particleEmitters.clear();
    }

    public void addBlockDestroyEffects(BlockPos pos, IBlockState state) {
        boolean notAir;
        if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
            Block block = state.getBlock();
            notAir = !Reflector.callBoolean((Object)block, (ReflectorMethod)Reflector.ForgeBlock_isAir, (Object[])new Object[]{state, this.world, pos}) && !Reflector.callBoolean((Object)block, (ReflectorMethod)Reflector.ForgeBlock_addDestroyEffects, (Object[])new Object[]{this.world, pos, this});
        } else {
            boolean bl = notAir = state.a() != Material.AIR;
        }
        if (notAir) {
            state = state.c((IBlockAccess)this.world, pos);
            int i = 4;
            for (int j = 0; j < 4; ++j) {
                for (int k = 0; k < 4; ++k) {
                    for (int l = 0; l < 4; ++l) {
                        double d0 = ((double)j + 0.5) / 4.0;
                        double d1 = ((double)k + 0.5) / 4.0;
                        double d2 = ((double)l + 0.5) / 4.0;
                        this.addEffect((Particle)new ParticleDigging(this.world, (double)pos.p() + d0, (double)pos.q() + d1, (double)pos.r() + d2, d0 - 0.5, d1 - 0.5, d2 - 0.5, state).setBlockPos(pos));
                    }
                }
            }
        }
    }

    public void addBlockHitEffects(BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = this.world.getBlockState(pos);
        if (iblockstate.i() != EnumBlockRenderType.INVISIBLE) {
            int i = pos.p();
            int j = pos.q();
            int k = pos.r();
            float f = 0.1f;
            AxisAlignedBB axisalignedbb = iblockstate.e((IBlockAccess)this.world, pos);
            double d0 = (double)i + this.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - (double)0.2f) + (double)0.1f + axisalignedbb.minX;
            double d1 = (double)j + this.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - (double)0.2f) + (double)0.1f + axisalignedbb.minY;
            double d2 = (double)k + this.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - (double)0.2f) + (double)0.1f + axisalignedbb.minZ;
            if (side == EnumFacing.DOWN) {
                d1 = (double)j + axisalignedbb.minY - (double)0.1f;
            }
            if (side == EnumFacing.UP) {
                d1 = (double)j + axisalignedbb.maxY + (double)0.1f;
            }
            if (side == EnumFacing.NORTH) {
                d2 = (double)k + axisalignedbb.minZ - (double)0.1f;
            }
            if (side == EnumFacing.SOUTH) {
                d2 = (double)k + axisalignedbb.maxZ + (double)0.1f;
            }
            if (side == EnumFacing.WEST) {
                d0 = (double)i + axisalignedbb.minX - (double)0.1f;
            }
            if (side == EnumFacing.EAST) {
                d0 = (double)i + axisalignedbb.maxX + (double)0.1f;
            }
            this.addEffect(new ParticleDigging(this.world, d0, d1, d2, 0.0, 0.0, 0.0, iblockstate).setBlockPos(pos).c(0.2f).multipleParticleScaleBy(0.6f));
        }
    }

    public String getStatistics() {
        int i = 0;
        for (int j = 0; j < 4; ++j) {
            for (int k = 0; k < 2; ++k) {
                i += this.fxLayers[j][k].size();
            }
        }
        return "" + i;
    }

    private boolean reuseBarrierParticle(Particle entityfx, ArrayDeque<Particle> deque) {
        for (Particle efx : deque) {
            if (!(efx instanceof Barrier) || entityfx.prevPosX != efx.prevPosX || entityfx.prevPosY != efx.prevPosY || entityfx.prevPosZ != efx.prevPosZ) continue;
            efx.particleAge = 0;
            return true;
        }
        return false;
    }

    public void addBlockHitEffects(BlockPos pos, RayTraceResult target) {
        IBlockState state = this.world.getBlockState(pos);
        if (state == null) {
            return;
        }
        boolean blockAddHitEffects = Reflector.callBoolean((Object)state.getBlock(), (ReflectorMethod)Reflector.ForgeBlock_addHitEffects, (Object[])new Object[]{state, this.world, target, this});
        if (state != null && !blockAddHitEffects) {
            this.addBlockHitEffects(pos, target.sideHit);
        }
    }
}
