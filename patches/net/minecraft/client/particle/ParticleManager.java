package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.world.World;

public class ParticleManager {
   private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
   protected World world;
   private final ArrayDeque<Particle>[][] fxLayers = new ArrayDeque[4][];
   private final Queue<ParticleEmitter> particleEmitters = Queues.newArrayDeque();
   private final TextureManager renderer;
   private final Random rand = new Random();
   private final Map<Integer, IParticleFactory> particleTypes = Maps.newHashMap();
   private final Queue<Particle> queue = Queues.newArrayDeque();

   public ParticleManager(World var1, TextureManager var2) {
      this.world = ☃;
      this.renderer = ☃;

      for (int ☃ = 0; ☃ < 4; ☃++) {
         this.fxLayers[☃] = new ArrayDeque[2];

         for (int ☃x = 0; ☃x < 2; ☃x++) {
            this.fxLayers[☃][☃x] = Queues.newArrayDeque();
         }
      }

      this.registerVanillaParticles();
   }

   private void registerVanillaParticles() {
      this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new ParticleExplosion.Factory());
      this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), new ParticleSpit.Factory());
      this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new ParticleBubble.Factory());
      this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleSplash.Factory());
      this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleWaterWake.Factory());
      this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleRain.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new ParticleSuspend.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new ParticleSuspendedTown.Factory());
      this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new ParticleCrit.Factory());
      this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new ParticleCrit.MagicFactory());
      this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new ParticleSmokeNormal.Factory());
      this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new ParticleSmokeLarge.Factory());
      this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new ParticleSpell.Factory());
      this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new ParticleSpell.InstantFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new ParticleSpell.MobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new ParticleSpell.AmbientMobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new ParticleSpell.WitchFactory());
      this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleDrip.WaterFactory());
      this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new ParticleDrip.LavaFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new ParticleHeart.AngryVillagerFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new ParticleSuspendedTown.HappyVillagerFactory());
      this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new ParticleSuspendedTown.Factory());
      this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new ParticleNote.Factory());
      this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new ParticlePortal.Factory());
      this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new ParticleEnchantmentTable.EnchantmentTable());
      this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new ParticleFlame.Factory());
      this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new ParticleLava.Factory());
      this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new ParticleFootStep.Factory());
      this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new ParticleCloud.Factory());
      this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new ParticleRedstone.Factory());
      this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), new ParticleFallingDust.Factory());
      this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new ParticleBreaking.SnowballFactory());
      this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new ParticleSnowShovel.Factory());
      this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new ParticleBreaking.SlimeFactory());
      this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new ParticleHeart.Factory());
      this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
      this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new ParticleBreaking.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new ParticleDigging.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new ParticleBlockDust.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new ParticleExplosionHuge.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new ParticleExplosionLarge.Factory());
      this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new ParticleFirework.Factory());
      this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new ParticleMobAppearance.Factory());
      this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), new ParticleDragonBreath.Factory());
      this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), new ParticleEndRod.Factory());
      this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), new ParticleCrit.DamageIndicatorFactory());
      this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), new ParticleSweepAttack.Factory());
      this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), new ParticleTotem.Factory());
   }

   public void registerParticle(int var1, IParticleFactory var2) {
      this.particleTypes.put(☃, ☃);
   }

   public void emitParticleAtEntity(Entity var1, EnumParticleTypes var2) {
      this.particleEmitters.add(new ParticleEmitter(this.world, ☃, ☃));
   }

   public void emitParticleAtEntity(Entity var1, EnumParticleTypes var2, int var3) {
      this.particleEmitters.add(new ParticleEmitter(this.world, ☃, ☃, ☃));
   }

   @Nullable
   public Particle spawnEffectParticle(int var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      IParticleFactory ☃ = this.particleTypes.get(☃);
      if (☃ != null) {
         Particle ☃x = ☃.createParticle(☃, this.world, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
         if (☃x != null) {
            this.addEffect(☃x);
            return ☃x;
         }
      }

      return null;
   }

   public void addEffect(Particle var1) {
      this.queue.add(☃);
   }

   public void updateEffects() {
      for (int ☃ = 0; ☃ < 4; ☃++) {
         this.updateEffectLayer(☃);
      }

      if (!this.particleEmitters.isEmpty()) {
         List<ParticleEmitter> ☃ = Lists.newArrayList();

         for (ParticleEmitter ☃x : this.particleEmitters) {
            ☃x.onUpdate();
            if (!☃x.isAlive()) {
               ☃.add(☃x);
            }
         }

         this.particleEmitters.removeAll(☃);
      }

      if (!this.queue.isEmpty()) {
         for (Particle ☃ = this.queue.poll(); ☃ != null; ☃ = this.queue.poll()) {
            int ☃xx = ☃.getFXLayer();
            int ☃xxx = ☃.shouldDisableDepth() ? 0 : 1;
            if (this.fxLayers[☃xx][☃xxx].size() >= 16384) {
               this.fxLayers[☃xx][☃xxx].removeFirst();
            }

            this.fxLayers[☃xx][☃xxx].add(☃);
         }
      }
   }

   private void updateEffectLayer(int var1) {
      this.world.profiler.startSection(String.valueOf(☃));

      for (int ☃ = 0; ☃ < 2; ☃++) {
         this.world.profiler.startSection(String.valueOf(☃));
         this.tickParticleList(this.fxLayers[☃][☃]);
         this.world.profiler.endSection();
      }

      this.world.profiler.endSection();
   }

   private void tickParticleList(Queue<Particle> var1) {
      if (!☃.isEmpty()) {
         Iterator<Particle> ☃ = ☃.iterator();

         while (☃.hasNext()) {
            Particle ☃x = ☃.next();
            this.tickParticle(☃x);
            if (!☃x.isAlive()) {
               ☃.remove();
            }
         }
      }
   }

   private void tickParticle(final Particle var1) {
      try {
         ☃.onUpdate();
      } catch (Throwable var6) {
         CrashReport ☃ = CrashReport.makeCrashReport(var6, "Ticking Particle");
         CrashReportCategory ☃x = ☃.makeCategory("Particle being ticked");
         final int ☃xx = ☃.getFXLayer();
         ☃x.addDetail("Particle", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return ☃.toString();
            }
         });
         ☃x.addDetail("Particle Type", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               if (☃ == 0) {
                  return "MISC_TEXTURE";
               } else if (☃ == 1) {
                  return "TERRAIN_TEXTURE";
               } else {
                  return ☃ == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + ☃;
               }
            }
         });
         throw new ReportedException(☃);
      }
   }

   public void renderParticles(Entity var1, float var2) {
      float ☃ = ActiveRenderInfo.getRotationX();
      float ☃x = ActiveRenderInfo.getRotationZ();
      float ☃xx = ActiveRenderInfo.getRotationYZ();
      float ☃xxx = ActiveRenderInfo.getRotationXY();
      float ☃xxxx = ActiveRenderInfo.getRotationXZ();
      Particle.interpPosX = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      Particle.interpPosY = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      Particle.interpPosZ = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      Particle.cameraViewDir = ☃.getLook(☃);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.alphaFunc(516, 0.003921569F);

      for (final int ☃xxxxx = 0; ☃xxxxx < 3; ☃xxxxx++) {
         for (int ☃xxxxxx = 0; ☃xxxxxx < 2; ☃xxxxxx++) {
            if (!this.fxLayers[☃xxxxx][☃xxxxxx].isEmpty()) {
               switch (☃xxxxxx) {
                  case 0:
                     GlStateManager.depthMask(false);
                     break;
                  case 1:
                     GlStateManager.depthMask(true);
               }

               switch (☃xxxxx) {
                  case 0:
                  default:
                     this.renderer.bindTexture(PARTICLE_TEXTURES);
                     break;
                  case 1:
                     this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Tessellator ☃xxxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxxx = ☃xxxxxxx.getBuffer();
               ☃xxxxxxxx.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

               for (final Particle ☃xxxxxxxxx : this.fxLayers[☃xxxxx][☃xxxxxx]) {
                  try {
                     ☃xxxxxxxxx.renderParticle(☃xxxxxxxx, ☃, ☃, ☃, ☃xxxx, ☃x, ☃xx, ☃xxx);
                  } catch (Throwable var18) {
                     CrashReport ☃xxxxxxxxxx = CrashReport.makeCrashReport(var18, "Rendering Particle");
                     CrashReportCategory ☃xxxxxxxxxxx = ☃xxxxxxxxxx.makeCategory("Particle being rendered");
                     ☃xxxxxxxxxxx.addDetail("Particle", new ICrashReportDetail<String>() {
                        public String call() throws Exception {
                           return ☃.toString();
                        }
                     });
                     ☃xxxxxxxxxxx.addDetail("Particle Type", new ICrashReportDetail<String>() {
                        public String call() throws Exception {
                           if (☃ == 0) {
                              return "MISC_TEXTURE";
                           } else if (☃ == 1) {
                              return "TERRAIN_TEXTURE";
                           } else {
                              return ☃ == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + ☃;
                           }
                        }
                     });
                     throw new ReportedException(☃xxxxxxxxxx);
                  }
               }

               ☃xxxxxxx.draw();
            }
         }
      }

      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
   }

   public void renderLitParticles(Entity var1, float var2) {
      float ☃ = (float) (Math.PI / 180.0);
      float ☃x = MathHelper.cos(☃.rotationYaw * (float) (Math.PI / 180.0));
      float ☃xx = MathHelper.sin(☃.rotationYaw * (float) (Math.PI / 180.0));
      float ☃xxx = -☃xx * MathHelper.sin(☃.rotationPitch * (float) (Math.PI / 180.0));
      float ☃xxxx = ☃x * MathHelper.sin(☃.rotationPitch * (float) (Math.PI / 180.0));
      float ☃xxxxx = MathHelper.cos(☃.rotationPitch * (float) (Math.PI / 180.0));

      for (int ☃xxxxxx = 0; ☃xxxxxx < 2; ☃xxxxxx++) {
         Queue<Particle> ☃xxxxxxx = this.fxLayers[3][☃xxxxxx];
         if (!☃xxxxxxx.isEmpty()) {
            Tessellator ☃xxxxxxxx = Tessellator.getInstance();
            BufferBuilder ☃xxxxxxxxx = ☃xxxxxxxx.getBuffer();

            for (Particle ☃xxxxxxxxxx : ☃xxxxxxx) {
               ☃xxxxxxxxxx.renderParticle(☃xxxxxxxxx, ☃, ☃, ☃x, ☃xxxxx, ☃xx, ☃xxx, ☃xxxx);
            }
         }
      }
   }

   public void clearEffects(@Nullable World var1) {
      this.world = ☃;

      for (int ☃ = 0; ☃ < 4; ☃++) {
         for (int ☃x = 0; ☃x < 2; ☃x++) {
            this.fxLayers[☃][☃x].clear();
         }
      }

      this.particleEmitters.clear();
   }

   public void addBlockDestroyEffects(BlockPos var1, IBlockState var2) {
      if (☃.getMaterial() != Material.AIR) {
         ☃ = ☃.getActualState(this.world, ☃);
         int ☃ = 4;

         for (int ☃x = 0; ☃x < 4; ☃x++) {
            for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
               for (int ☃xxx = 0; ☃xxx < 4; ☃xxx++) {
                  double ☃xxxx = (☃x + 0.5) / 4.0;
                  double ☃xxxxx = (☃xx + 0.5) / 4.0;
                  double ☃xxxxxx = (☃xxx + 0.5) / 4.0;
                  this.addEffect(
                     new ParticleDigging(this.world, ☃.getX() + ☃xxxx, ☃.getY() + ☃xxxxx, ☃.getZ() + ☃xxxxxx, ☃xxxx - 0.5, ☃xxxxx - 0.5, ☃xxxxxx - 0.5, ☃)
                        .setBlockPos(☃)
                  );
               }
            }
         }
      }
   }

   public void addBlockHitEffects(BlockPos var1, EnumFacing var2) {
      IBlockState ☃ = this.world.getBlockState(☃);
      if (☃.getRenderType() != EnumBlockRenderType.INVISIBLE) {
         int ☃x = ☃.getX();
         int ☃xx = ☃.getY();
         int ☃xxx = ☃.getZ();
         float ☃xxxx = 0.1F;
         AxisAlignedBB ☃xxxxx = ☃.getBoundingBox(this.world, ☃);
         double ☃xxxxxx = ☃x + this.rand.nextDouble() * (☃xxxxx.maxX - ☃xxxxx.minX - 0.2F) + 0.1F + ☃xxxxx.minX;
         double ☃xxxxxxx = ☃xx + this.rand.nextDouble() * (☃xxxxx.maxY - ☃xxxxx.minY - 0.2F) + 0.1F + ☃xxxxx.minY;
         double ☃xxxxxxxx = ☃xxx + this.rand.nextDouble() * (☃xxxxx.maxZ - ☃xxxxx.minZ - 0.2F) + 0.1F + ☃xxxxx.minZ;
         if (☃ == EnumFacing.DOWN) {
            ☃xxxxxxx = ☃xx + ☃xxxxx.minY - 0.1F;
         }

         if (☃ == EnumFacing.UP) {
            ☃xxxxxxx = ☃xx + ☃xxxxx.maxY + 0.1F;
         }

         if (☃ == EnumFacing.NORTH) {
            ☃xxxxxxxx = ☃xxx + ☃xxxxx.minZ - 0.1F;
         }

         if (☃ == EnumFacing.SOUTH) {
            ☃xxxxxxxx = ☃xxx + ☃xxxxx.maxZ + 0.1F;
         }

         if (☃ == EnumFacing.WEST) {
            ☃xxxxxx = ☃x + ☃xxxxx.minX - 0.1F;
         }

         if (☃ == EnumFacing.EAST) {
            ☃xxxxxx = ☃x + ☃xxxxx.maxX + 0.1F;
         }

         this.addEffect(
            new ParticleDigging(this.world, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.0, 0.0, 0.0, ☃).setBlockPos(☃).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F)
         );
      }
   }

   public String getStatistics() {
      int ☃ = 0;

      for (int ☃x = 0; ☃x < 4; ☃x++) {
         for (int ☃xx = 0; ☃xx < 2; ☃xx++) {
            ☃ += this.fxLayers[☃x][☃xx].size();
         }
      }

      return "" + ☃;
   }
}
