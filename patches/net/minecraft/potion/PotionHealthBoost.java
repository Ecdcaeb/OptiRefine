package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;

public class PotionHealthBoost extends Potion {
   public PotionHealthBoost(boolean var1, int var2) {
      super(☃, ☃);
   }

   @Override
   public void removeAttributesModifiersFromEntity(EntityLivingBase var1, AbstractAttributeMap var2, int var3) {
      super.removeAttributesModifiersFromEntity(☃, ☃, ☃);
      if (☃.getHealth() > ☃.getMaxHealth()) {
         ☃.setHealth(☃.getMaxHealth());
      }
   }
}
