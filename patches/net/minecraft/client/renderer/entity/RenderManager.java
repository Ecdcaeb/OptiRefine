package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RenderManager {
   private final Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap = Maps.newHashMap();
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

   public RenderManager(TextureManager var1, RenderItem var2) {
      this.renderEngine = ☃;
      this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
      this.entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
      this.entityRenderMap.put(EntityPig.class, new RenderPig(this));
      this.entityRenderMap.put(EntitySheep.class, new RenderSheep(this));
      this.entityRenderMap.put(EntityCow.class, new RenderCow(this));
      this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this));
      this.entityRenderMap.put(EntityWolf.class, new RenderWolf(this));
      this.entityRenderMap.put(EntityChicken.class, new RenderChicken(this));
      this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this));
      this.entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this));
      this.entityRenderMap.put(EntityParrot.class, new RenderParrot(this));
      this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
      this.entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
      this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
      this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
      this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
      this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
      this.entityRenderMap.put(EntityWitherSkeleton.class, new RenderWitherSkeleton(this));
      this.entityRenderMap.put(EntityStray.class, new RenderStray(this));
      this.entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
      this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
      this.entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
      this.entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
      this.entityRenderMap.put(EntityZombieVillager.class, new RenderZombieVillager(this));
      this.entityRenderMap.put(EntityHusk.class, new RenderHusk(this));
      this.entityRenderMap.put(EntitySlime.class, new RenderSlime(this));
      this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
      this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, 6.0F));
      this.entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
      this.entityRenderMap.put(EntitySquid.class, new RenderSquid(this));
      this.entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
      this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
      this.entityRenderMap.put(EntityBat.class, new RenderBat(this));
      this.entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
      this.entityRenderMap.put(EntityElderGuardian.class, new RenderElderGuardian(this));
      this.entityRenderMap.put(EntityShulker.class, new RenderShulker(this));
      this.entityRenderMap.put(EntityPolarBear.class, new RenderPolarBear(this));
      this.entityRenderMap.put(EntityEvoker.class, new RenderEvoker(this));
      this.entityRenderMap.put(EntityVindicator.class, new RenderVindicator(this));
      this.entityRenderMap.put(EntityVex.class, new RenderVex(this));
      this.entityRenderMap.put(EntityIllusionIllager.class, new RenderIllusionIllager(this));
      this.entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
      this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
      this.entityRenderMap.put(EntityWither.class, new RenderWither(this));
      this.entityRenderMap.put(Entity.class, new RenderEntity(this));
      this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
      this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, ☃));
      this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
      this.entityRenderMap.put(EntityTippedArrow.class, new RenderTippedArrow(this));
      this.entityRenderMap.put(EntitySpectralArrow.class, new RenderSpectralArrow(this));
      this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball<>(this, Items.SNOWBALL, ☃));
      this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball<>(this, Items.ENDER_PEARL, ☃));
      this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball<>(this, Items.ENDER_EYE, ☃));
      this.entityRenderMap.put(EntityEgg.class, new RenderSnowball<>(this, Items.EGG, ☃));
      this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, ☃));
      this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball<>(this, Items.EXPERIENCE_BOTTLE, ☃));
      this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball<>(this, Items.FIREWORKS, ☃));
      this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
      this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
      this.entityRenderMap.put(EntityDragonFireball.class, new RenderDragonFireball(this));
      this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
      this.entityRenderMap.put(EntityShulkerBullet.class, new RenderShulkerBullet(this));
      this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, ☃));
      this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
      this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
      this.entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
      this.entityRenderMap.put(EntityArmorStand.class, new RenderArmorStand(this));
      this.entityRenderMap.put(EntityEvokerFangs.class, new RenderEvokerFangs(this));
      this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
      this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
      this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart(this));
      this.entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
      this.entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
      this.entityRenderMap.put(EntityAreaEffectCloud.class, new RenderAreaEffectCloud(this));
      this.entityRenderMap.put(EntityHorse.class, new RenderHorse(this));
      this.entityRenderMap.put(EntitySkeletonHorse.class, new RenderAbstractHorse(this));
      this.entityRenderMap.put(EntityZombieHorse.class, new RenderAbstractHorse(this));
      this.entityRenderMap.put(EntityMule.class, new RenderAbstractHorse(this, 0.92F));
      this.entityRenderMap.put(EntityDonkey.class, new RenderAbstractHorse(this, 0.87F));
      this.entityRenderMap.put(EntityLlama.class, new RenderLlama(this));
      this.entityRenderMap.put(EntityLlamaSpit.class, new RenderLlamaSpit(this));
      this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
      this.playerRenderer = new RenderPlayer(this);
      this.skinMap.put("default", this.playerRenderer);
      this.skinMap.put("slim", new RenderPlayer(this, true));
   }

   public void setRenderPosition(double var1, double var3, double var5) {
      this.renderPosX = ☃;
      this.renderPosY = ☃;
      this.renderPosZ = ☃;
   }

   public <T extends Entity> Render<T> getEntityClassRenderObject(Class<? extends Entity> var1) {
      Render<? extends Entity> ☃ = this.entityRenderMap.get(☃);
      if (☃ == null && ☃ != Entity.class) {
         ☃ = this.getEntityClassRenderObject((Class<? extends Entity>)☃.getSuperclass());
         this.entityRenderMap.put(☃, ☃);
      }

      return (Render<T>)☃;
   }

   @Nullable
   public <T extends Entity> Render<T> getEntityRenderObject(Entity var1) {
      if (☃ instanceof AbstractClientPlayer) {
         String ☃ = ((AbstractClientPlayer)☃).getSkinType();
         RenderPlayer ☃x = this.skinMap.get(☃);
         return ☃x != null ? ☃x : this.playerRenderer;
      } else {
         return this.getEntityClassRenderObject((Class<? extends Entity>)☃.getClass());
      }
   }

   public void cacheActiveRenderInfo(World var1, FontRenderer var2, Entity var3, Entity var4, GameSettings var5, float var6) {
      this.world = ☃;
      this.options = ☃;
      this.renderViewEntity = ☃;
      this.pointedEntity = ☃;
      this.textRenderer = ☃;
      if (☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).isPlayerSleeping()) {
         IBlockState ☃ = ☃.getBlockState(new BlockPos(☃));
         Block ☃x = ☃.getBlock();
         if (☃x == Blocks.BED) {
            int ☃xx = ☃.getValue(BlockBed.FACING).getHorizontalIndex();
            this.playerViewY = ☃xx * 90 + 180;
            this.playerViewX = 0.0F;
         }
      } else {
         this.playerViewY = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃;
         this.playerViewX = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      }

      if (☃.thirdPersonView == 2) {
         this.playerViewY += 180.0F;
      }

      this.viewerPosX = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      this.viewerPosY = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      this.viewerPosZ = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
   }

   public void setPlayerViewY(float var1) {
      this.playerViewY = ☃;
   }

   public boolean isRenderShadow() {
      return this.renderShadow;
   }

   public void setRenderShadow(boolean var1) {
      this.renderShadow = ☃;
   }

   public void setDebugBoundingBox(boolean var1) {
      this.debugBoundingBox = ☃;
   }

   public boolean isDebugBoundingBox() {
      return this.debugBoundingBox;
   }

   public boolean isRenderMultipass(Entity var1) {
      return this.getEntityRenderObject(☃).isMultipass();
   }

   public boolean shouldRender(Entity var1, ICamera var2, double var3, double var5, double var7) {
      Render<Entity> ☃ = this.getEntityRenderObject(☃);
      return ☃ != null && ☃.shouldRender(☃, ☃, ☃, ☃, ☃);
   }

   public void renderEntityStatic(Entity var1, float var2, boolean var3) {
      if (☃.ticksExisted == 0) {
         ☃.lastTickPosX = ☃.posX;
         ☃.lastTickPosY = ☃.posY;
         ☃.lastTickPosZ = ☃.posZ;
      }

      double ☃ = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃x = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      float ☃xxx = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃;
      int ☃xxxx = ☃.getBrightnessForRender();
      if (☃.isBurning()) {
         ☃xxxx = 15728880;
      }

      int ☃xxxxx = ☃xxxx % 65536;
      int ☃xxxxxx = ☃xxxx / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃xxxxx, ☃xxxxxx);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.renderEntity(☃, ☃ - this.renderPosX, ☃x - this.renderPosY, ☃xx - this.renderPosZ, ☃xxx, ☃, ☃);
   }

   public void renderEntity(Entity var1, double var2, double var4, double var6, float var8, float var9, boolean var10) {
      Render<Entity> ☃ = null;

      try {
         ☃ = this.getEntityRenderObject(☃);
         if (☃ != null && this.renderEngine != null) {
            try {
               ☃.setRenderOutlines(this.renderOutlines);
               ☃.doRender(☃, ☃, ☃, ☃, ☃, ☃);
            } catch (Throwable var17) {
               throw new ReportedException(CrashReport.makeCrashReport(var17, "Rendering entity in world"));
            }

            try {
               if (!this.renderOutlines) {
                  ☃.doRenderShadowAndFire(☃, ☃, ☃, ☃, ☃, ☃);
               }
            } catch (Throwable var18) {
               throw new ReportedException(CrashReport.makeCrashReport(var18, "Post-rendering entity in world"));
            }

            if (this.debugBoundingBox && !☃.isInvisible() && !☃ && !Minecraft.getMinecraft().isReducedDebug()) {
               try {
                  this.renderDebugBoundingBox(☃, ☃, ☃, ☃, ☃, ☃);
               } catch (Throwable var16) {
                  throw new ReportedException(CrashReport.makeCrashReport(var16, "Rendering entity hitbox in world"));
               }
            }
         }
      } catch (Throwable var19) {
         CrashReport ☃x = CrashReport.makeCrashReport(var19, "Rendering entity in world");
         CrashReportCategory ☃xx = ☃x.makeCategory("Entity being rendered");
         ☃.addEntityCrashInfo(☃xx);
         CrashReportCategory ☃xxx = ☃x.makeCategory("Renderer details");
         ☃xxx.addCrashSection("Assigned renderer", ☃);
         ☃xxx.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(☃, ☃, ☃));
         ☃xxx.addCrashSection("Rotation", ☃);
         ☃xxx.addCrashSection("Delta", ☃);
         throw new ReportedException(☃x);
      }
   }

   public void renderMultipass(Entity var1, float var2) {
      if (☃.ticksExisted == 0) {
         ☃.lastTickPosX = ☃.posX;
         ☃.lastTickPosY = ☃.posY;
         ☃.lastTickPosZ = ☃.posZ;
      }

      double ☃ = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃x = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      float ☃xxx = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃;
      int ☃xxxx = ☃.getBrightnessForRender();
      if (☃.isBurning()) {
         ☃xxxx = 15728880;
      }

      int ☃xxxxx = ☃xxxx % 65536;
      int ☃xxxxxx = ☃xxxx / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃xxxxx, ☃xxxxxx);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Render<Entity> ☃xxxxxxx = this.getEntityRenderObject(☃);
      if (☃xxxxxxx != null && this.renderEngine != null) {
         ☃xxxxxxx.renderMultipass(☃, ☃ - this.renderPosX, ☃x - this.renderPosY, ☃xx - this.renderPosZ, ☃xxx, ☃);
      }
   }

   private void renderDebugBoundingBox(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.depthMask(false);
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.disableBlend();
      float ☃ = ☃.width / 2.0F;
      AxisAlignedBB ☃x = ☃.getEntityBoundingBox();
      RenderGlobal.drawBoundingBox(
         ☃x.minX - ☃.posX + ☃,
         ☃x.minY - ☃.posY + ☃,
         ☃x.minZ - ☃.posZ + ☃,
         ☃x.maxX - ☃.posX + ☃,
         ☃x.maxY - ☃.posY + ☃,
         ☃x.maxZ - ☃.posZ + ☃,
         1.0F,
         1.0F,
         1.0F,
         1.0F
      );
      Entity[] ☃xx = ☃.getParts();
      if (☃xx != null) {
         for (Entity ☃xxx : ☃xx) {
            double ☃xxxx = (☃xxx.posX - ☃xxx.prevPosX) * ☃;
            double ☃xxxxx = (☃xxx.posY - ☃xxx.prevPosY) * ☃;
            double ☃xxxxxx = (☃xxx.posZ - ☃xxx.prevPosZ) * ☃;
            AxisAlignedBB ☃xxxxxxx = ☃xxx.getEntityBoundingBox();
            RenderGlobal.drawBoundingBox(
               ☃xxxxxxx.minX - this.renderPosX + ☃xxxx,
               ☃xxxxxxx.minY - this.renderPosY + ☃xxxxx,
               ☃xxxxxxx.minZ - this.renderPosZ + ☃xxxxxx,
               ☃xxxxxxx.maxX - this.renderPosX + ☃xxxx,
               ☃xxxxxxx.maxY - this.renderPosY + ☃xxxxx,
               ☃xxxxxxx.maxZ - this.renderPosZ + ☃xxxxxx,
               0.25F,
               1.0F,
               0.0F,
               1.0F
            );
         }
      }

      if (☃ instanceof EntityLivingBase) {
         float ☃xxx = 0.01F;
         RenderGlobal.drawBoundingBox(☃ - ☃, ☃ + ☃.getEyeHeight() - 0.01F, ☃ - ☃, ☃ + ☃, ☃ + ☃.getEyeHeight() + 0.01F, ☃ + ☃, 1.0F, 0.0F, 0.0F, 1.0F);
      }

      Tessellator ☃xxx = Tessellator.getInstance();
      BufferBuilder ☃xxxx = ☃xxx.getBuffer();
      Vec3d ☃xxxxx = ☃.getLook(☃);
      ☃xxxx.begin(3, DefaultVertexFormats.POSITION_COLOR);
      ☃xxxx.pos(☃, ☃ + ☃.getEyeHeight(), ☃).color(0, 0, 255, 255).endVertex();
      ☃xxxx.pos(☃ + ☃xxxxx.x * 2.0, ☃ + ☃.getEyeHeight() + ☃xxxxx.y * 2.0, ☃ + ☃xxxxx.z * 2.0).color(0, 0, 255, 255).endVertex();
      ☃xxx.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
   }

   public void setWorld(@Nullable World var1) {
      this.world = ☃;
      if (☃ == null) {
         this.renderViewEntity = null;
      }
   }

   public double getDistanceToCamera(double var1, double var3, double var5) {
      double ☃ = ☃ - this.viewerPosX;
      double ☃x = ☃ - this.viewerPosY;
      double ☃xx = ☃ - this.viewerPosZ;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public FontRenderer getFontRenderer() {
      return this.textRenderer;
   }

   public void setRenderOutlines(boolean var1) {
      this.renderOutlines = ☃;
   }
}
