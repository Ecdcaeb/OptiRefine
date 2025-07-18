package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class EntityDamageSource extends DamageSource {
   @Nullable
   protected Entity damageSourceEntity;
   private boolean isThornsDamage;

   public EntityDamageSource(String var1, @Nullable Entity var2) {
      super(☃);
      this.damageSourceEntity = ☃;
   }

   public EntityDamageSource setIsThornsDamage() {
      this.isThornsDamage = true;
      return this;
   }

   public boolean getIsThornsDamage() {
      return this.isThornsDamage;
   }

   @Nullable
   @Override
   public Entity getTrueSource() {
      return this.damageSourceEntity;
   }

   @Override
   public ITextComponent getDeathMessage(EntityLivingBase var1) {
      ItemStack ☃ = this.damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.damageSourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
      String ☃x = "death.attack." + this.damageType;
      String ☃xx = ☃x + ".item";
      return !☃.isEmpty() && ☃.hasDisplayName() && I18n.canTranslate(☃xx)
         ? new TextComponentTranslation(☃xx, ☃.getDisplayName(), this.damageSourceEntity.getDisplayName(), ☃.getTextComponent())
         : new TextComponentTranslation(☃x, ☃.getDisplayName(), this.damageSourceEntity.getDisplayName());
   }

   @Override
   public boolean isDifficultyScaled() {
      return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
   }

   @Nullable
   @Override
   public Vec3d getDamageLocation() {
      return new Vec3d(this.damageSourceEntity.posX, this.damageSourceEntity.posY, this.damageSourceEntity.posZ);
   }
}
