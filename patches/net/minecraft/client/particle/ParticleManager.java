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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleBreaking.SlimeFactory;
import net.minecraft.client.particle.ParticleBreaking.SnowballFactory;
import net.minecraft.client.particle.ParticleCrit.DamageIndicatorFactory;
import net.minecraft.client.particle.ParticleCrit.MagicFactory;
import net.minecraft.client.particle.ParticleDrip.LavaFactory;
import net.minecraft.client.particle.ParticleDrip.WaterFactory;
import net.minecraft.client.particle.ParticleEnchantmentTable.EnchantmentTable;
import net.minecraft.client.particle.ParticleExplosion.Factory;
import net.minecraft.client.particle.ParticleFirework.Spark;
import net.minecraft.client.particle.ParticleHeart.AngryVillagerFactory;
import net.minecraft.client.particle.ParticleSpell.AmbientMobFactory;
import net.minecraft.client.particle.ParticleSpell.InstantFactory;
import net.minecraft.client.particle.ParticleSpell.MobFactory;
import net.minecraft.client.particle.ParticleSpell.WitchFactory;
import net.minecraft.client.particle.ParticleSuspendedTown.HappyVillagerFactory;
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
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;

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

      for (int i = 0; i < 4; i++) {
         this.fxLayers[i] = new ArrayDeque[2];

         for (int j = 0; j < 2; j++) {
            this.fxLayers[i][j] = Queues.newArrayDeque();
         }
      }

      this.registerVanillaParticles();
   }

   private void registerVanillaParticles() {
      this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new Factory());
      this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), new net.minecraft.client.particle.ParticleSpit.Factory());
      this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new net.minecraft.client.particle.ParticleBubble.Factory());
      this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new net.minecraft.client.particle.ParticleSplash.Factory());
      this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new net.minecraft.client.particle.ParticleWaterWake.Factory());
      this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new net.minecraft.client.particle.ParticleRain.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new net.minecraft.client.particle.ParticleSuspend.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new net.minecraft.client.particle.ParticleSuspendedTown.Factory());
      this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new net.minecraft.client.particle.ParticleCrit.Factory());
      this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new MagicFactory());
      this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new net.minecraft.client.particle.ParticleSmokeNormal.Factory());
      this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new net.minecraft.client.particle.ParticleSmokeLarge.Factory());
      this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new net.minecraft.client.particle.ParticleSpell.Factory());
      this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new InstantFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new MobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new AmbientMobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new WitchFactory());
      this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new WaterFactory());
      this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new LavaFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new AngryVillagerFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new HappyVillagerFactory());
      this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new net.minecraft.client.particle.ParticleSuspendedTown.Factory());
      this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new net.minecraft.client.particle.ParticleNote.Factory());
      this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new net.minecraft.client.particle.ParticlePortal.Factory());
      this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EnchantmentTable());
      this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new net.minecraft.client.particle.ParticleFlame.Factory());
      this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new net.minecraft.client.particle.ParticleLava.Factory());
      this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new net.minecraft.client.particle.ParticleFootStep.Factory());
      this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new net.minecraft.client.particle.ParticleCloud.Factory());
      this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new net.minecraft.client.particle.ParticleRedstone.Factory());
      this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), new net.minecraft.client.particle.ParticleFallingDust.Factory());
      this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new SnowballFactory());
      this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new net.minecraft.client.particle.ParticleSnowShovel.Factory());
      this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new SlimeFactory());
      this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new net.minecraft.client.particle.ParticleHeart.Factory());
      this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new net.minecraft.client.particle.Barrier.Factory());
      this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new net.minecraft.client.particle.ParticleBreaking.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new net.minecraft.client.particle.ParticleDigging.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new net.minecraft.client.particle.ParticleBlockDust.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new net.minecraft.client.particle.ParticleExplosionHuge.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new net.minecraft.client.particle.ParticleExplosionLarge.Factory());
      this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new net.minecraft.client.particle.ParticleFirework.Factory());
      this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new net.minecraft.client.particle.ParticleMobAppearance.Factory());
      this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), new net.minecraft.client.particle.ParticleDragonBreath.Factory());
      this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), new net.minecraft.client.particle.ParticleEndRod.Factory());
      this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), new DamageIndicatorFactory());
      this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), new net.minecraft.client.particle.ParticleSweepAttack.Factory());
      this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), new net.minecraft.client.particle.ParticleTotem.Factory());
   }

   public void registerParticle(int id, IParticleFactory particleFactory) {
      this.particleTypes.put(id, particleFactory);
   }

   public void emitParticleAtEntity(Entity entityIn, EnumParticleTypes particleTypes) {
      this.particleEmitters.add(new ParticleEmitter(this.world, entityIn, particleTypes));
   }

   public void emitParticleAtEntity(Entity p_191271_1_, EnumParticleTypes p_191271_2_, int p_191271_3_) {
      this.particleEmitters.add(new ParticleEmitter(this.world, p_191271_1_, p_191271_2_, p_191271_3_));
   }

   @Nullable
   public Particle spawnEffectParticle(
      int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters
   ) {
      IParticleFactory iparticlefactory = this.particleTypes.get(particleId);
      if (iparticlefactory != null) {
         Particle particle = iparticlefactory.createParticle(particleId, this.world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
         if (particle != null) {
            this.addEffect(particle);
            return particle;
         }
      }

      return null;
   }

   public void addEffect(Particle effect) {
      if (effect != null) {
         if (!(effect instanceof Spark) || Config.isFireworkParticles()) {
            this.queue.add(effect);
         }
      }
   }

   public void updateEffects() {
      for (int i = 0; i < 4; i++) {
         this.updateEffectLayer(i);
      }

      if (!this.particleEmitters.isEmpty()) {
         List<ParticleEmitter> list = Lists.newArrayList();

         for (ParticleEmitter particleemitter : this.particleEmitters) {
            particleemitter.onUpdate();
            if (!particleemitter.k()) {
               list.add(particleemitter);
            }
         }

         this.particleEmitters.removeAll(list);
      }

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

   private void updateEffectLayer(int layer) {
      this.world.profiler.startSection(String.valueOf(layer));

      for (int i = 0; i < 2; i++) {
         this.world.profiler.startSection(String.valueOf(i));
         this.tickParticleList(this.fxLayers[layer][i]);
         this.world.profiler.endSection();
      }

      this.world.profiler.endSection();
   }

   private void tickParticleList(Queue<Particle> p_187240_1_) {
      if (!p_187240_1_.isEmpty()) {
         long timeStartMs = System.currentTimeMillis();
         int countLeft = p_187240_1_.size();
         Iterator<Particle> iterator = p_187240_1_.iterator();

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

            for (Iterator it = p_187240_1_.iterator(); it.hasNext() && countToRemove > 0; countToRemove--) {
               Particle particlex = (Particle)it.next();
               particlex.setExpired();
               it.remove();
            }
         }
      }
   }

   private void tickParticle(final Particle particle) {
      try {
         particle.onUpdate();
      } catch (Throwable var6) {
         CrashReport crashreport = CrashReport.makeCrashReport(var6, "Ticking Particle");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
         final int i = particle.getFXLayer();
         crashreportcategory.addDetail("Particle", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return particle.toString();
            }
         });
         crashreportcategory.addDetail("Particle Type", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               if (i == 0) {
                  return "MISC_TEXTURE";
               } else if (i == 1) {
                  return "TERRAIN_TEXTURE";
               } else {
                  return i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
               }
            }
         });
         throw new ReportedException(crashreport);
      }
   }

   public void renderParticles(Entity entityIn, float partialTicks) {
      float f = ActiveRenderInfo.getRotationX();
      float f1 = ActiveRenderInfo.getRotationZ();
      float f2 = ActiveRenderInfo.getRotationYZ();
      float f3 = ActiveRenderInfo.getRotationXY();
      float f4 = ActiveRenderInfo.getRotationXZ();
      Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
      Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
      Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
      Particle.cameraViewDir = entityIn.getLook(partialTicks);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.alphaFunc(516, 0.003921569F);
      IBlockState cameraBlockState = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.world, entityIn, partialTicks);
      boolean isEyeInWater = cameraBlockState.a() == Material.WATER;

      for (int i_nf = 0; i_nf < 3; i_nf++) {
         final int i = i_nf;

         for (int j = 0; j < 2; j++) {
            if (!this.fxLayers[i][j].isEmpty()) {
               switch (j) {
                  case 0:
                     GlStateManager.depthMask(false);
                     break;
                  case 1:
                     GlStateManager.depthMask(true);
               }

               switch (i) {
                  case 0:
                  default:
                     this.renderer.bindTexture(PARTICLE_TEXTURES);
                     break;
                  case 1:
                     this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Tessellator tessellator = Tessellator.getInstance();
               BufferBuilder bufferbuilder = tessellator.getBuffer();
               bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

               for (final Particle particle : this.fxLayers[i][j]) {
                  try {
                     if (isEyeInWater || !(particle instanceof ParticleSuspend)) {
                        particle.renderParticle(bufferbuilder, entityIn, partialTicks, f, f4, f1, f2, f3);
                     }
                  } catch (Throwable var20) {
                     CrashReport crashreport = CrashReport.makeCrashReport(var20, "Rendering Particle");
                     CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                     crashreportcategory.addDetail("Particle", new ICrashReportDetail<String>() {
                        public String call() throws Exception {
                           return particle.toString();
                        }
                     });
                     crashreportcategory.addDetail("Particle Type", new ICrashReportDetail<String>() {
                        public String call() throws Exception {
                           if (i == 0) {
                              return "MISC_TEXTURE";
                           } else if (i == 1) {
                              return "TERRAIN_TEXTURE";
                           } else {
                              return i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
                           }
                        }
                     });
                     throw new ReportedException(crashreport);
                  }
               }

               tessellator.draw();
            }
         }
      }

      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
   }

   public void renderLitParticles(Entity entityIn, float partialTick) {
      float f = (float) (Math.PI / 180.0);
      float f1 = MathHelper.cos(entityIn.rotationYaw * (float) (Math.PI / 180.0));
      float f2 = MathHelper.sin(entityIn.rotationYaw * (float) (Math.PI / 180.0));
      float f3 = -f2 * MathHelper.sin(entityIn.rotationPitch * (float) (Math.PI / 180.0));
      float f4 = f1 * MathHelper.sin(entityIn.rotationPitch * (float) (Math.PI / 180.0));
      float f5 = MathHelper.cos(entityIn.rotationPitch * (float) (Math.PI / 180.0));

      for (int i = 0; i < 2; i++) {
         Queue<Particle> queue = this.fxLayers[3][i];
         if (!queue.isEmpty()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();

            for (Particle particle : queue) {
               particle.renderParticle(bufferbuilder, entityIn, partialTick, f1, f5, f2, f3, f4);
            }
         }
      }
   }

   public void clearEffects(@Nullable World worldIn) {
      this.world = worldIn;

      for (int i = 0; i < 4; i++) {
         for (int j = 0; j < 2; j++) {
            this.fxLayers[i][j].clear();
         }
      }

      this.particleEmitters.clear();
   }

   public void addBlockDestroyEffects(BlockPos pos, IBlockState state) {
      boolean notAir;
      if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
         Block block = state.getBlock();
         notAir = !Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, new Object[]{state, this.world, pos})
            && !Reflector.callBoolean(block, Reflector.ForgeBlock_addDestroyEffects, new Object[]{this.world, pos, this});
      } else {
         notAir = state.a() != Material.AIR;
      }

      if (notAir) {
         state = state.c(this.world, pos);
         int i = 4;

         for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
               for (int l = 0; l < 4; l++) {
                  double d0 = (j + 0.5) / 4.0;
                  double d1 = (k + 0.5) / 4.0;
                  double d2 = (l + 0.5) / 4.0;
                  this.addEffect(
                     new ParticleDigging(this.world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, d0 - 0.5, d1 - 0.5, d2 - 0.5, state).setBlockPos(pos)
                  );
               }
            }
         }
      }
   }

   public void addBlockHitEffects(BlockPos pos, EnumFacing side) {
      IBlockState iblockstate = this.world.getBlockState(pos);
      if (iblockstate.i() != EnumBlockRenderType.INVISIBLE) {
         int i = pos.getX();
         int j = pos.getY();
         int k = pos.getZ();
         float f = 0.1F;
         AxisAlignedBB axisalignedbb = iblockstate.e(this.world, pos);
         double d0 = i + this.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.2F) + 0.1F + axisalignedbb.minX;
         double d1 = j + this.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.2F) + 0.1F + axisalignedbb.minY;
         double d2 = k + this.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.2F) + 0.1F + axisalignedbb.minZ;
         if (side == EnumFacing.DOWN) {
            d1 = j + axisalignedbb.minY - 0.1F;
         }

         if (side == EnumFacing.UP) {
            d1 = j + axisalignedbb.maxY + 0.1F;
         }

         if (side == EnumFacing.NORTH) {
            d2 = k + axisalignedbb.minZ - 0.1F;
         }

         if (side == EnumFacing.SOUTH) {
            d2 = k + axisalignedbb.maxZ + 0.1F;
         }

         if (side == EnumFacing.WEST) {
            d0 = i + axisalignedbb.minX - 0.1F;
         }

         if (side == EnumFacing.EAST) {
            d0 = i + axisalignedbb.maxX + 0.1F;
         }

         this.addEffect(
            new ParticleDigging(this.world, d0, d1, d2, 0.0, 0.0, 0.0, iblockstate).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F)
         );
      }
   }

   public String getStatistics() {
      int i = 0;

      for (int j = 0; j < 4; j++) {
         for (int k = 0; k < 2; k++) {
            i += this.fxLayers[j][k].size();
         }
      }

      return "" + i;
   }

   private boolean reuseBarrierParticle(Particle entityfx, ArrayDeque<Particle> deque) {
      for (Particle efx : deque) {
         if (efx instanceof Barrier && entityfx.prevPosX == efx.prevPosX && entityfx.prevPosY == efx.prevPosY && entityfx.prevPosZ == efx.prevPosZ) {
            efx.particleAge = 0;
            return true;
         }
      }

      return false;
   }

   public void addBlockHitEffects(BlockPos pos, RayTraceResult target) {
      IBlockState state = this.world.getBlockState(pos);
      if (state != null) {
         boolean blockAddHitEffects = Reflector.callBoolean(state.getBlock(), Reflector.ForgeBlock_addHitEffects, new Object[]{state, this.world, target, this});
         if (state != null && !blockAddHitEffects) {
            this.addBlockHitEffects(pos, target.sideHit);
         }
      }
   }
}
