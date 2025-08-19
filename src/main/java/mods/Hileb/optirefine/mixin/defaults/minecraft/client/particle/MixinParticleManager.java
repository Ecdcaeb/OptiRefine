package mods.Hileb.optirefine.mixin.defaults.minecraft.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {
    @Shadow
    @Final
    private ArrayDeque<Particle>[][] fxLayers;

    @Shadow
    @Final
    private Queue<Particle> queue;

    @WrapWithCondition(method = "addEffect", at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z"))
    public boolean injectAddEffect(Queue<?> instance, Object effect) {
        return !(effect instanceof ParticleFirework.Spark) || Config.isFireworkParticles();
    }

    @Shadow
    protected abstract void tickParticle(Particle particle);

    @WrapMethod(method = "tickParticleList")
    private void tickParticleList(Queue<Particle> particlesToTick, Operation<Void> original) {
        if (!particlesToTick.isEmpty()) {
            long timeStartMs = System.currentTimeMillis();
            int countLeft = particlesToTick.size();
            Iterator<Particle> iterator = particlesToTick.iterator();

            while (iterator.hasNext()) {
                Particle particle = iterator.next();
                this.tickParticle(particle);
                if (!particle.isAlive()) {
                    iterator.remove();
                }

                countLeft--;
                if (System.currentTimeMillis() > timeStartMs + 20L) {
                    break;
                }
            }

            if (countLeft > 0) {
                int countToRemove = countLeft;

                for (Iterator<Particle> it = particlesToTick.iterator(); it.hasNext() && countToRemove > 0; countToRemove--) {
                    Particle particlex = it.next();
                    particlex.setExpired();
                    it.remove();
                }
            }
        }
    }

    @Inject(method = "updateEffects", at = @At("RETURN"))
    public void injectUpdateEffects(CallbackInfo ci){
        if (!this.queue.isEmpty()) {
            for (Particle particle = this.queue.poll(); particle != null; particle = this.queue.poll()) {
                int j = particle.getFXLayer();
                int k = particle.shouldDisableDepth() ? 0 : 1;
                if (this.fxLayers[j][k].size() >= 16384) {
                    this.fxLayers[j][k].removeFirst();
                }

                if (!(particle instanceof Barrier) || !this.reuseBarrierParticle(particle, this.fxLayers[j][k])) {
                    this.fxLayers[j][k].add(particle);
                }
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean reuseBarrierParticle(Particle entityfx, ArrayDeque<Particle> deque) {
        for (Particle efx : deque) {
            if (efx instanceof Barrier && entityfx.prevPosX == efx.prevPosX && entityfx.prevPosY == efx.prevPosY && entityfx.prevPosZ == efx.prevPosZ) {
                efx.particleAge = 0;
                return true;
            }
        }

        return false;
    }

}
/*
+++ net/minecraft/client/particle/ParticleManager.java	Tue Aug 19 14:59:58 2025
@@ -7,14 +7,30 @@
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Queue;
 import java.util.Random;
 import javax.annotation.Nullable;
+import net.minecraft.block.Block;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
+import net.minecraft.client.particle.ParticleBreaking.SlimeFactory;
+import net.minecraft.client.particle.ParticleBreaking.SnowballFactory;
+import net.minecraft.client.particle.ParticleCrit.DamageIndicatorFactory;
+import net.minecraft.client.particle.ParticleCrit.MagicFactory;
+import net.minecraft.client.particle.ParticleDrip.LavaFactory;
+import net.minecraft.client.particle.ParticleDrip.WaterFactory;
+import net.minecraft.client.particle.ParticleEnchantmentTable.EnchantmentTable;
+import net.minecraft.client.particle.ParticleExplosion.Factory;
+import net.minecraft.client.particle.ParticleFirework.Spark;
+import net.minecraft.client.particle.ParticleHeart.AngryVillagerFactory;
+import net.minecraft.client.particle.ParticleSpell.AmbientMobFactory;
+import net.minecraft.client.particle.ParticleSpell.InstantFactory;
+import net.minecraft.client.particle.ParticleSpell.MobFactory;
+import net.minecraft.client.particle.ParticleSpell.WitchFactory;
+import net.minecraft.client.particle.ParticleSuspendedTown.HappyVillagerFactory;
 import net.minecraft.client.renderer.ActiveRenderInfo;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.texture.TextureManager;
 import net.minecraft.client.renderer.texture.TextureMap;
@@ -28,13 +44,15 @@
 import net.minecraft.util.EnumParticleTypes;
 import net.minecraft.util.ReportedException;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
+import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.world.World;
+import net.optifine.reflect.Reflector;

 public class ParticleManager {
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    protected World world;
    private final ArrayDeque<Particle>[][] fxLayers = new ArrayDeque[4][];
    private final Queue<ParticleEmitter> particleEmitters = Queues.newArrayDeque();
@@ -56,60 +74,60 @@
       }

       this.registerVanillaParticles();
    }

    private void registerVanillaParticles() {
-      this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new ParticleExplosion.Factory());
-      this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), new ParticleSpit.Factory());
-      this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new ParticleBubble.Factory());
-      this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleSplash.Factory());
-      this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleWaterWake.Factory());
-      this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleRain.Factory());
-      this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new ParticleSuspend.Factory());
-      this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new ParticleSuspendedTown.Factory());
-      this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new ParticleCrit.Factory());
-      this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new ParticleCrit.MagicFactory());
-      this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new ParticleSmokeNormal.Factory());
-      this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new ParticleSmokeLarge.Factory());
-      this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new ParticleSpell.Factory());
-      this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new ParticleSpell.InstantFactory());
-      this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new ParticleSpell.MobFactory());
-      this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new ParticleSpell.AmbientMobFactory());
-      this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new ParticleSpell.WitchFactory());
-      this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleDrip.WaterFactory());
-      this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new ParticleDrip.LavaFactory());
-      this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new ParticleHeart.AngryVillagerFactory());
-      this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new ParticleSuspendedTown.HappyVillagerFactory());
-      this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new ParticleSuspendedTown.Factory());
-      this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new ParticleNote.Factory());
-      this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new ParticlePortal.Factory());
-      this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new ParticleEnchantmentTable.EnchantmentTable());
-      this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new ParticleFlame.Factory());
-      this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new ParticleLava.Factory());
-      this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new ParticleFootStep.Factory());
-      this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new ParticleCloud.Factory());
-      this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new ParticleRedstone.Factory());
-      this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), new ParticleFallingDust.Factory());
-      this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new ParticleBreaking.SnowballFactory());
-      this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new ParticleSnowShovel.Factory());
-      this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new ParticleBreaking.SlimeFactory());
-      this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new ParticleHeart.Factory());
-      this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
-      this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new ParticleBreaking.Factory());
-      this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new ParticleDigging.Factory());
-      this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new ParticleBlockDust.Factory());
-      this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new ParticleExplosionHuge.Factory());
-      this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new ParticleExplosionLarge.Factory());
-      this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new ParticleFirework.Factory());
-      this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new ParticleMobAppearance.Factory());
-      this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), new ParticleDragonBreath.Factory());
-      this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), new ParticleEndRod.Factory());
-      this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), new ParticleCrit.DamageIndicatorFactory());
-      this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), new ParticleSweepAttack.Factory());
-      this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), new ParticleTotem.Factory());
+      this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new Factory());
+      this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), new net.minecraft.client.particle.ParticleSpit.Factory());
+      this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new net.minecraft.client.particle.ParticleBubble.Factory());
+      this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new net.minecraft.client.particle.ParticleSplash.Factory());
+      this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new net.minecraft.client.particle.ParticleWaterWake.Factory());
+      this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new net.minecraft.client.particle.ParticleRain.Factory());
+      this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new net.minecraft.client.particle.ParticleSuspend.Factory());
+      this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new net.minecraft.client.particle.ParticleSuspendedTown.Factory());
+      this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new net.minecraft.client.particle.ParticleCrit.Factory());
+      this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new MagicFactory());
+      this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new net.minecraft.client.particle.ParticleSmokeNormal.Factory());
+      this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new net.minecraft.client.particle.ParticleSmokeLarge.Factory());
+      this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new net.minecraft.client.particle.ParticleSpell.Factory());
+      this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new InstantFactory());
+      this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new MobFactory());
+      this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new AmbientMobFactory());
+      this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new WitchFactory());
+      this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new WaterFactory());
+      this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new LavaFactory());
+      this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new AngryVillagerFactory());
+      this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new HappyVillagerFactory());
+      this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new net.minecraft.client.particle.ParticleSuspendedTown.Factory());
+      this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new net.minecraft.client.particle.ParticleNote.Factory());
+      this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new net.minecraft.client.particle.ParticlePortal.Factory());
+      this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EnchantmentTable());
+      this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new net.minecraft.client.particle.ParticleFlame.Factory());
+      this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new net.minecraft.client.particle.ParticleLava.Factory());
+      this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new net.minecraft.client.particle.ParticleFootStep.Factory());
+      this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new net.minecraft.client.particle.ParticleCloud.Factory());
+      this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new net.minecraft.client.particle.ParticleRedstone.Factory());
+      this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), new net.minecraft.client.particle.ParticleFallingDust.Factory());
+      this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new SnowballFactory());
+      this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new net.minecraft.client.particle.ParticleSnowShovel.Factory());
+      this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new SlimeFactory());
+      this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new net.minecraft.client.particle.ParticleHeart.Factory());
+      this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new net.minecraft.client.particle.Barrier.Factory());
+      this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new net.minecraft.client.particle.ParticleBreaking.Factory());
+      this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new net.minecraft.client.particle.ParticleDigging.Factory());
+      this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new net.minecraft.client.particle.ParticleBlockDust.Factory());
+      this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new net.minecraft.client.particle.ParticleExplosionHuge.Factory());
+      this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new net.minecraft.client.particle.ParticleExplosionLarge.Factory());
+      this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new net.minecraft.client.particle.ParticleFirework.Factory());
+      this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new net.minecraft.client.particle.ParticleMobAppearance.Factory());
+      this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), new net.minecraft.client.particle.ParticleDragonBreath.Factory());
+      this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), new net.minecraft.client.particle.ParticleEndRod.Factory());
+      this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), new DamageIndicatorFactory());
+      this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), new net.minecraft.client.particle.ParticleSweepAttack.Factory());
+      this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), new net.minecraft.client.particle.ParticleTotem.Factory());
    }

    public void registerParticle(int var1, IParticleFactory var2) {
       this.particleTypes.put(var1, var2);
    }

@@ -133,26 +151,30 @@
       }

       return null;
    }

    public void addEffect(Particle var1) {
-      this.queue.add(var1);
+      if (var1 != null) {
+         if (!(var1 instanceof Spark) || Config.isFireworkParticles()) {
+            this.queue.add(var1);
+         }
+      }
    }

    public void updateEffects() {
       for (int var1 = 0; var1 < 4; var1++) {
          this.updateEffectLayer(var1);
       }

       if (!this.particleEmitters.isEmpty()) {
          ArrayList var4 = Lists.newArrayList();

          for (ParticleEmitter var3 : this.particleEmitters) {
             var3.onUpdate();
-            if (!var3.isAlive()) {
+            if (!var3.k()) {
                var4.add(var3);
             }
          }

          this.particleEmitters.removeAll(var4);
       }
@@ -162,13 +184,15 @@
             int var6 = var5.getFXLayer();
             int var7 = var5.shouldDisableDepth() ? 0 : 1;
             if (this.fxLayers[var6][var7].size() >= 16384) {
                this.fxLayers[var6][var7].removeFirst();
             }

-            this.fxLayers[var6][var7].add(var5);
+            if (!(var5 instanceof Barrier) || !this.reuseBarrierParticle(var5, this.fxLayers[var6][var7])) {
+               this.fxLayers[var6][var7].add(var5);
+            }
          }
       }
    }

    private void updateEffectLayer(int var1) {
       this.world.profiler.startSection(String.valueOf(var1));
@@ -181,19 +205,36 @@

       this.world.profiler.endSection();
    }

    private void tickParticleList(Queue<Particle> var1) {
       if (!var1.isEmpty()) {
-         Iterator var2 = var1.iterator();
+         long var3 = System.currentTimeMillis();
+         int var5 = var1.size();
+         Iterator var6 = var1.iterator();
+
+         while (var6.hasNext()) {
+            Particle var7 = (Particle)var6.next();
+            this.tickParticle(var7);
+            if (!var7.isAlive()) {
+               var6.remove();
+            }
+
+            var5--;
+            if (System.currentTimeMillis() > var3 + 20L) {
+               break;
+            }
+         }
+
+         if (var5 > 0) {
+            int var10 = var5;

-         while (var2.hasNext()) {
-            Particle var3 = (Particle)var2.next();
-            this.tickParticle(var3);
-            if (!var3.isAlive()) {
-               var2.remove();
+            for (Iterator var8 = var1.iterator(); var8.hasNext() && var10 > 0; var10--) {
+               Particle var9 = (Particle)var8.next();
+               var9.setExpired();
+               var8.remove();
             }
          }
       }
    }

    private void tickParticle(final Particle var1) {
@@ -233,65 +274,71 @@
       Particle.interpPosY = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var2;
       Particle.interpPosZ = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var2;
       Particle.cameraViewDir = var1.getLook(var2);
       GlStateManager.enableBlend();
       GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
       GlStateManager.alphaFunc(516, 0.003921569F);
+      IBlockState var8 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.world, var1, var2);
+      boolean var9 = var8.a() == Material.WATER;

-      for (final int var8 = 0; var8 < 3; var8++) {
-         for (int var9 = 0; var9 < 2; var9++) {
-            if (!this.fxLayers[var8][var9].isEmpty()) {
-               switch (var9) {
+      for (int var10 = 0; var10 < 3; var10++) {
+         final int var11 = var10;
+
+         for (int var12 = 0; var12 < 2; var12++) {
+            if (!this.fxLayers[var11][var12].isEmpty()) {
+               switch (var12) {
                   case 0:
                      GlStateManager.depthMask(false);
                      break;
                   case 1:
                      GlStateManager.depthMask(true);
                }

-               switch (var8) {
+               switch (var11) {
                   case 0:
                   default:
                      this.renderer.bindTexture(PARTICLE_TEXTURES);
                      break;
                   case 1:
                      this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-               Tessellator var10 = Tessellator.getInstance();
-               BufferBuilder var11 = var10.getBuffer();
-               var11.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
+               Tessellator var13 = Tessellator.getInstance();
+               BufferBuilder var14 = var13.getBuffer();
+               var14.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

-               for (final Particle var13 : this.fxLayers[var8][var9]) {
+               for (final Particle var16 : this.fxLayers[var11][var12]) {
                   try {
-                     var13.renderParticle(var11, var1, var2, var3, var7, var4, var5, var6);
-                  } catch (Throwable var18) {
-                     CrashReport var15 = CrashReport.makeCrashReport(var18, "Rendering Particle");
-                     CrashReportCategory var16 = var15.makeCategory("Particle being rendered");
-                     var16.addDetail("Particle", new ICrashReportDetail<String>() {
+                     if (var9 || !(var16 instanceof ParticleSuspend)) {
+                        var16.renderParticle(var14, var1, var2, var3, var7, var4, var5, var6);
+                     }
+                  } catch (Throwable var20) {
+                     CrashReport var18 = CrashReport.makeCrashReport(var20, "Rendering Particle");
+                     CrashReportCategory var19 = var18.makeCategory("Particle being rendered");
+                     var19.addDetail("Particle", new ICrashReportDetail<String>() {
                         public String call() throws Exception {
-                           return var13.toString();
+                           return var16.toString();
                         }
                      });
-                     var16.addDetail("Particle Type", new ICrashReportDetail<String>() {
+                     var19.addDetail("Particle Type", new ICrashReportDetail<String>() {
                         public String call() throws Exception {
-                           if (var8 == 0) {
+                           if (var11 == 0) {
                               return "MISC_TEXTURE";
-                           } else if (var8 == 1) {
+                           } else if (var11 == 1) {
                               return "TERRAIN_TEXTURE";
                            } else {
-                              return var8 == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + var8;
+                              return var11 == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + var11;
                            }
                         }
                      });
-                     throw new ReportedException(var15);
+                     throw new ReportedException(var18);
                   }
                }

-               var10.draw();
+               var13.draw();
             }
          }
       }

       GlStateManager.depthMask(true);
       GlStateManager.disableBlend();
@@ -329,40 +376,49 @@
       }

       this.particleEmitters.clear();
    }

    public void addBlockDestroyEffects(BlockPos var1, IBlockState var2) {
-      if (var2.getMaterial() != Material.AIR) {
-         var2 = var2.getActualState(this.world, var1);
-         byte var3 = 4;
-
-         for (int var4 = 0; var4 < 4; var4++) {
-            for (int var5 = 0; var5 < 4; var5++) {
-               for (int var6 = 0; var6 < 4; var6++) {
-                  double var7 = (var4 + 0.5) / 4.0;
-                  double var9 = (var5 + 0.5) / 4.0;
-                  double var11 = (var6 + 0.5) / 4.0;
+      boolean var3;
+      if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
+         Block var4 = var2.getBlock();
+         var3 = !Reflector.callBoolean(var4, Reflector.ForgeBlock_isAir, new Object[]{var2, this.world, var1})
+            && !Reflector.callBoolean(var4, Reflector.ForgeBlock_addDestroyEffects, new Object[]{this.world, var1, this});
+      } else {
+         var3 = var2.a() != Material.AIR;
+      }
+
+      if (var3) {
+         var2 = var2.c(this.world, var1);
+         byte var15 = 4;
+
+         for (int var5 = 0; var5 < 4; var5++) {
+            for (int var6 = 0; var6 < 4; var6++) {
+               for (int var7 = 0; var7 < 4; var7++) {
+                  double var8 = (var5 + 0.5) / 4.0;
+                  double var10 = (var6 + 0.5) / 4.0;
+                  double var12 = (var7 + 0.5) / 4.0;
                   this.addEffect(
-                     new ParticleDigging(this.world, var1.getX() + var7, var1.getY() + var9, var1.getZ() + var11, var7 - 0.5, var9 - 0.5, var11 - 0.5, var2)
+                     new ParticleDigging(this.world, var1.getX() + var8, var1.getY() + var10, var1.getZ() + var12, var8 - 0.5, var10 - 0.5, var12 - 0.5, var2)
                         .setBlockPos(var1)
                   );
                }
             }
          }
       }
    }

    public void addBlockHitEffects(BlockPos var1, EnumFacing var2) {
       IBlockState var3 = this.world.getBlockState(var1);
-      if (var3.getRenderType() != EnumBlockRenderType.INVISIBLE) {
+      if (var3.i() != EnumBlockRenderType.INVISIBLE) {
          int var4 = var1.getX();
          int var5 = var1.getY();
          int var6 = var1.getZ();
          float var7 = 0.1F;
-         AxisAlignedBB var8 = var3.getBoundingBox(this.world, var1);
+         AxisAlignedBB var8 = var3.e(this.world, var1);
          double var9 = var4 + this.rand.nextDouble() * (var8.maxX - var8.minX - 0.2F) + 0.1F + var8.minX;
          double var11 = var5 + this.rand.nextDouble() * (var8.maxY - var8.minY - 0.2F) + 0.1F + var8.minY;
          double var13 = var6 + this.rand.nextDouble() * (var8.maxZ - var8.minZ - 0.2F) + 0.1F + var8.minZ;
          if (var2 == EnumFacing.DOWN) {
             var11 = var5 + var8.minY - 0.1F;
          }
@@ -400,8 +456,29 @@
          for (int var3 = 0; var3 < 2; var3++) {
             var1 += this.fxLayers[var2][var3].size();
          }
       }

       return "" + var1;
+   }
+
+   private boolean reuseBarrierParticle(Particle var1, ArrayDeque<Particle> var2) {
+      for (Particle var4 : var2) {
+         if (var4 instanceof Barrier && var1.prevPosX == var4.prevPosX && var1.prevPosY == var4.prevPosY && var1.prevPosZ == var4.prevPosZ) {
+            var4.particleAge = 0;
+            return true;
+         }
+      }
+
+      return false;
+   }
+
+   public void addBlockHitEffects(BlockPos var1, RayTraceResult var2) {
+      IBlockState var3 = this.world.getBlockState(var1);
+      if (var3 != null) {
+         boolean var4 = Reflector.callBoolean(var3.getBlock(), Reflector.ForgeBlock_addHitEffects, new Object[]{var3, this.world, var2, this});
+         if (var3 != null && !var4) {
+            this.addBlockHitEffects(var1, var2.sideHit);
+         }
+      }
    }
 }
 */
