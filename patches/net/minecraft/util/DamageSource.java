package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;

public class DamageSource {
   public static final DamageSource IN_FIRE = new DamageSource("inFire").setFireDamage();
   public static final DamageSource LIGHTNING_BOLT = new DamageSource("lightningBolt");
   public static final DamageSource ON_FIRE = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
   public static final DamageSource LAVA = new DamageSource("lava").setFireDamage();
   public static final DamageSource HOT_FLOOR = new DamageSource("hotFloor").setFireDamage();
   public static final DamageSource IN_WALL = new DamageSource("inWall").setDamageBypassesArmor();
   public static final DamageSource CRAMMING = new DamageSource("cramming").setDamageBypassesArmor();
   public static final DamageSource DROWN = new DamageSource("drown").setDamageBypassesArmor();
   public static final DamageSource STARVE = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
   public static final DamageSource CACTUS = new DamageSource("cactus");
   public static final DamageSource FALL = new DamageSource("fall").setDamageBypassesArmor();
   public static final DamageSource FLY_INTO_WALL = new DamageSource("flyIntoWall").setDamageBypassesArmor();
   public static final DamageSource OUT_OF_WORLD = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
   public static final DamageSource GENERIC = new DamageSource("generic").setDamageBypassesArmor();
   public static final DamageSource MAGIC = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
   public static final DamageSource WITHER = new DamageSource("wither").setDamageBypassesArmor();
   public static final DamageSource ANVIL = new DamageSource("anvil");
   public static final DamageSource FALLING_BLOCK = new DamageSource("fallingBlock");
   public static final DamageSource DRAGON_BREATH = new DamageSource("dragonBreath").setDamageBypassesArmor();
   public static final DamageSource FIREWORKS = new DamageSource("fireworks").setExplosion();
   private boolean isUnblockable;
   private boolean isDamageAllowedInCreativeMode;
   private boolean damageIsAbsolute;
   private float hungerDamage = 0.1F;
   private boolean fireDamage;
   private boolean projectile;
   private boolean difficultyScaled;
   private boolean magicDamage;
   private boolean explosion;
   public String damageType;

   public static DamageSource causeMobDamage(EntityLivingBase var0) {
      return new EntityDamageSource("mob", ☃);
   }

   public static DamageSource causeIndirectDamage(Entity var0, EntityLivingBase var1) {
      return new EntityDamageSourceIndirect("mob", ☃, ☃);
   }

   public static DamageSource causePlayerDamage(EntityPlayer var0) {
      return new EntityDamageSource("player", ☃);
   }

   public static DamageSource causeArrowDamage(EntityArrow var0, @Nullable Entity var1) {
      return new EntityDamageSourceIndirect("arrow", ☃, ☃).setProjectile();
   }

   public static DamageSource causeFireballDamage(EntityFireball var0, @Nullable Entity var1) {
      return ☃ == null
         ? new EntityDamageSourceIndirect("onFire", ☃, ☃).setFireDamage().setProjectile()
         : new EntityDamageSourceIndirect("fireball", ☃, ☃).setFireDamage().setProjectile();
   }

   public static DamageSource causeThrownDamage(Entity var0, @Nullable Entity var1) {
      return new EntityDamageSourceIndirect("thrown", ☃, ☃).setProjectile();
   }

   public static DamageSource causeIndirectMagicDamage(Entity var0, @Nullable Entity var1) {
      return new EntityDamageSourceIndirect("indirectMagic", ☃, ☃).setDamageBypassesArmor().setMagicDamage();
   }

   public static DamageSource causeThornsDamage(Entity var0) {
      return new EntityDamageSource("thorns", ☃).setIsThornsDamage().setMagicDamage();
   }

   public static DamageSource causeExplosionDamage(@Nullable Explosion var0) {
      return ☃ != null && ☃.getExplosivePlacedBy() != null
         ? new EntityDamageSource("explosion.player", ☃.getExplosivePlacedBy()).setDifficultyScaled().setExplosion()
         : new DamageSource("explosion").setDifficultyScaled().setExplosion();
   }

   public static DamageSource causeExplosionDamage(@Nullable EntityLivingBase var0) {
      return ☃ != null
         ? new EntityDamageSource("explosion.player", ☃).setDifficultyScaled().setExplosion()
         : new DamageSource("explosion").setDifficultyScaled().setExplosion();
   }

   public boolean isProjectile() {
      return this.projectile;
   }

   public DamageSource setProjectile() {
      this.projectile = true;
      return this;
   }

   public boolean isExplosion() {
      return this.explosion;
   }

   public DamageSource setExplosion() {
      this.explosion = true;
      return this;
   }

   public boolean isUnblockable() {
      return this.isUnblockable;
   }

   public float getHungerDamage() {
      return this.hungerDamage;
   }

   public boolean canHarmInCreative() {
      return this.isDamageAllowedInCreativeMode;
   }

   public boolean isDamageAbsolute() {
      return this.damageIsAbsolute;
   }

   protected DamageSource(String var1) {
      this.damageType = ☃;
   }

   @Nullable
   public Entity getImmediateSource() {
      return this.getTrueSource();
   }

   @Nullable
   public Entity getTrueSource() {
      return null;
   }

   protected DamageSource setDamageBypassesArmor() {
      this.isUnblockable = true;
      this.hungerDamage = 0.0F;
      return this;
   }

   protected DamageSource setDamageAllowedInCreativeMode() {
      this.isDamageAllowedInCreativeMode = true;
      return this;
   }

   protected DamageSource setDamageIsAbsolute() {
      this.damageIsAbsolute = true;
      this.hungerDamage = 0.0F;
      return this;
   }

   protected DamageSource setFireDamage() {
      this.fireDamage = true;
      return this;
   }

   public ITextComponent getDeathMessage(EntityLivingBase var1) {
      EntityLivingBase ☃ = ☃.getAttackingEntity();
      String ☃x = "death.attack." + this.damageType;
      String ☃xx = ☃x + ".player";
      return ☃ != null && I18n.canTranslate(☃xx)
         ? new TextComponentTranslation(☃xx, ☃.getDisplayName(), ☃.getDisplayName())
         : new TextComponentTranslation(☃x, ☃.getDisplayName());
   }

   public boolean isFireDamage() {
      return this.fireDamage;
   }

   public String getDamageType() {
      return this.damageType;
   }

   public DamageSource setDifficultyScaled() {
      this.difficultyScaled = true;
      return this;
   }

   public boolean isDifficultyScaled() {
      return this.difficultyScaled;
   }

   public boolean isMagicDamage() {
      return this.magicDamage;
   }

   public DamageSource setMagicDamage() {
      this.magicDamage = true;
      return this;
   }

   public boolean isCreativePlayer() {
      Entity ☃ = this.getTrueSource();
      return ☃ instanceof EntityPlayer && ((EntityPlayer)☃).capabilities.isCreativeMode;
   }

   @Nullable
   public Vec3d getDamageLocation() {
      return null;
   }
}
