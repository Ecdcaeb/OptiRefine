package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class EntityDamageSourceIndirect extends EntityDamageSource {
   private final Entity indirectEntity;

   public EntityDamageSourceIndirect(String var1, Entity var2, @Nullable Entity var3) {
      super(☃, ☃);
      this.indirectEntity = ☃;
   }

   @Nullable
   @Override
   public Entity getImmediateSource() {
      return this.damageSourceEntity;
   }

   @Nullable
   @Override
   public Entity getTrueSource() {
      return this.indirectEntity;
   }

   @Override
   public ITextComponent getDeathMessage(EntityLivingBase var1) {
      ITextComponent ☃ = this.indirectEntity == null ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
      ItemStack ☃x = this.indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.indirectEntity).getHeldItemMainhand() : ItemStack.EMPTY;
      String ☃xx = "death.attack." + this.damageType;
      String ☃xxx = ☃xx + ".item";
      return !☃x.isEmpty() && ☃x.hasDisplayName() && I18n.canTranslate(☃xxx)
         ? new TextComponentTranslation(☃xxx, ☃.getDisplayName(), ☃, ☃x.getTextComponent())
         : new TextComponentTranslation(☃xx, ☃.getDisplayName(), ☃);
   }
}
