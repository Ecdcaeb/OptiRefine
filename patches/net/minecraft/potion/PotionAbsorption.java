package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;

public class PotionAbsorption extends Potion {
   protected PotionAbsorption(boolean var1, int var2) {
      super(☃, ☃);
   }

   @Override
   public void removeAttributesModifiersFromEntity(EntityLivingBase var1, AbstractAttributeMap var2, int var3) {
      ☃.setAbsorptionAmount(☃.getAbsorptionAmount() - 4 * (☃ + 1));
      super.removeAttributesModifiersFromEntity(☃, ☃, ☃);
   }

   @Override
   public void applyAttributesModifiersToEntity(EntityLivingBase var1, AbstractAttributeMap var2, int var3) {
      ☃.setAbsorptionAmount(☃.getAbsorptionAmount() + 4 * (☃ + 1));
      super.applyAttributesModifiersToEntity(☃, ☃, ☃);
   }
}
