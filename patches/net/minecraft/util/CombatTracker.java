package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CombatTracker {
   private final List<CombatEntry> combatEntries = Lists.newArrayList();
   private final EntityLivingBase fighter;
   private int lastDamageTime;
   private int combatStartTime;
   private int combatEndTime;
   private boolean inCombat;
   private boolean takingDamage;
   private String fallSuffix;

   public CombatTracker(EntityLivingBase var1) {
      this.fighter = ☃;
   }

   public void calculateFallSuffix() {
      this.resetFallSuffix();
      if (this.fighter.isOnLadder()) {
         Block ☃ = this.fighter.world.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();
         if (☃ == Blocks.LADDER) {
            this.fallSuffix = "ladder";
         } else if (☃ == Blocks.VINE) {
            this.fallSuffix = "vines";
         }
      } else if (this.fighter.isInWater()) {
         this.fallSuffix = "water";
      }
   }

   public void trackDamage(DamageSource var1, float var2, float var3) {
      this.reset();
      this.calculateFallSuffix();
      CombatEntry ☃ = new CombatEntry(☃, this.fighter.ticksExisted, ☃, ☃, this.fallSuffix, this.fighter.fallDistance);
      this.combatEntries.add(☃);
      this.lastDamageTime = this.fighter.ticksExisted;
      this.takingDamage = true;
      if (☃.isLivingDamageSrc() && !this.inCombat && this.fighter.isEntityAlive()) {
         this.inCombat = true;
         this.combatStartTime = this.fighter.ticksExisted;
         this.combatEndTime = this.combatStartTime;
         this.fighter.sendEnterCombat();
      }
   }

   public ITextComponent getDeathMessage() {
      if (this.combatEntries.isEmpty()) {
         return new TextComponentTranslation("death.attack.generic", this.fighter.getDisplayName());
      } else {
         CombatEntry ☃ = this.getBestCombatEntry();
         CombatEntry ☃x = this.combatEntries.get(this.combatEntries.size() - 1);
         ITextComponent ☃xx = ☃x.getDamageSrcDisplayName();
         Entity ☃xxx = ☃x.getDamageSrc().getTrueSource();
         ITextComponent ☃xxxx;
         if (☃ != null && ☃x.getDamageSrc() == DamageSource.FALL) {
            ITextComponent ☃xxxxx = ☃.getDamageSrcDisplayName();
            if (☃.getDamageSrc() == DamageSource.FALL || ☃.getDamageSrc() == DamageSource.OUT_OF_WORLD) {
               ☃xxxx = new TextComponentTranslation("death.fell.accident." + this.getFallSuffix(☃), this.fighter.getDisplayName());
            } else if (☃xxxxx != null && (☃xx == null || !☃xxxxx.equals(☃xx))) {
               Entity ☃xxxxxx = ☃.getDamageSrc().getTrueSource();
               ItemStack ☃xxxxxxx = ☃xxxxxx instanceof EntityLivingBase ? ((EntityLivingBase)☃xxxxxx).getHeldItemMainhand() : ItemStack.EMPTY;
               if (!☃xxxxxxx.isEmpty() && ☃xxxxxxx.hasDisplayName()) {
                  ☃xxxx = new TextComponentTranslation("death.fell.assist.item", this.fighter.getDisplayName(), ☃xxxxx, ☃xxxxxxx.getTextComponent());
               } else {
                  ☃xxxx = new TextComponentTranslation("death.fell.assist", this.fighter.getDisplayName(), ☃xxxxx);
               }
            } else if (☃xx != null) {
               ItemStack ☃xxxxxx = ☃xxx instanceof EntityLivingBase ? ((EntityLivingBase)☃xxx).getHeldItemMainhand() : ItemStack.EMPTY;
               if (!☃xxxxxx.isEmpty() && ☃xxxxxx.hasDisplayName()) {
                  ☃xxxx = new TextComponentTranslation("death.fell.finish.item", this.fighter.getDisplayName(), ☃xx, ☃xxxxxx.getTextComponent());
               } else {
                  ☃xxxx = new TextComponentTranslation("death.fell.finish", this.fighter.getDisplayName(), ☃xx);
               }
            } else {
               ☃xxxx = new TextComponentTranslation("death.fell.killer", this.fighter.getDisplayName());
            }
         } else {
            ☃xxxx = ☃x.getDamageSrc().getDeathMessage(this.fighter);
         }

         return ☃xxxx;
      }
   }

   @Nullable
   public EntityLivingBase getBestAttacker() {
      EntityLivingBase ☃ = null;
      EntityPlayer ☃x = null;
      float ☃xx = 0.0F;
      float ☃xxx = 0.0F;

      for (CombatEntry ☃xxxx : this.combatEntries) {
         if (☃xxxx.getDamageSrc().getTrueSource() instanceof EntityPlayer && (☃x == null || ☃xxxx.getDamage() > ☃xxx)) {
            ☃xxx = ☃xxxx.getDamage();
            ☃x = (EntityPlayer)☃xxxx.getDamageSrc().getTrueSource();
         }

         if (☃xxxx.getDamageSrc().getTrueSource() instanceof EntityLivingBase && (☃ == null || ☃xxxx.getDamage() > ☃xx)) {
            ☃xx = ☃xxxx.getDamage();
            ☃ = (EntityLivingBase)☃xxxx.getDamageSrc().getTrueSource();
         }
      }

      return (EntityLivingBase)(☃x != null && ☃xxx >= ☃xx / 3.0F ? ☃x : ☃);
   }

   @Nullable
   private CombatEntry getBestCombatEntry() {
      CombatEntry ☃ = null;
      CombatEntry ☃x = null;
      float ☃xx = 0.0F;
      float ☃xxx = 0.0F;

      for (int ☃xxxx = 0; ☃xxxx < this.combatEntries.size(); ☃xxxx++) {
         CombatEntry ☃xxxxx = this.combatEntries.get(☃xxxx);
         CombatEntry ☃xxxxxx = ☃xxxx > 0 ? this.combatEntries.get(☃xxxx - 1) : null;
         if ((☃xxxxx.getDamageSrc() == DamageSource.FALL || ☃xxxxx.getDamageSrc() == DamageSource.OUT_OF_WORLD)
            && ☃xxxxx.getDamageAmount() > 0.0F
            && (☃ == null || ☃xxxxx.getDamageAmount() > ☃xxx)) {
            if (☃xxxx > 0) {
               ☃ = ☃xxxxxx;
            } else {
               ☃ = ☃xxxxx;
            }

            ☃xxx = ☃xxxxx.getDamageAmount();
         }

         if (☃xxxxx.getFallSuffix() != null && (☃x == null || ☃xxxxx.getDamage() > ☃xx)) {
            ☃x = ☃xxxxx;
            ☃xx = ☃xxxxx.getDamage();
         }
      }

      if (☃xxx > 5.0F && ☃ != null) {
         return ☃;
      } else {
         return ☃xx > 5.0F && ☃x != null ? ☃x : null;
      }
   }

   private String getFallSuffix(CombatEntry var1) {
      return ☃.getFallSuffix() == null ? "generic" : ☃.getFallSuffix();
   }

   public int getCombatDuration() {
      return this.inCombat ? this.fighter.ticksExisted - this.combatStartTime : this.combatEndTime - this.combatStartTime;
   }

   private void resetFallSuffix() {
      this.fallSuffix = null;
   }

   public void reset() {
      int ☃ = this.inCombat ? 300 : 100;
      if (this.takingDamage && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.lastDamageTime > ☃)) {
         boolean ☃x = this.inCombat;
         this.takingDamage = false;
         this.inCombat = false;
         this.combatEndTime = this.fighter.ticksExisted;
         if (☃x) {
            this.fighter.sendEndCombat();
         }

         this.combatEntries.clear();
      }
   }

   public EntityLivingBase getFighter() {
      return this.fighter;
   }
}
