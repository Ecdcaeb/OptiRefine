package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

public class LayerBipedArmor extends LayerArmorBase<ModelBiped> {
   public LayerBipedArmor(RenderLivingBase<?> var1) {
      super(☃);
   }

   @Override
   protected void initArmor() {
      this.modelLeggings = new ModelBiped(0.5F);
      this.modelArmor = new ModelBiped(1.0F);
   }

   protected void setModelSlotVisible(ModelBiped var1, EntityEquipmentSlot var2) {
      this.setModelVisible(☃);
      switch (☃) {
         case HEAD:
            ☃.bipedHead.showModel = true;
            ☃.bipedHeadwear.showModel = true;
            break;
         case CHEST:
            ☃.bipedBody.showModel = true;
            ☃.bipedRightArm.showModel = true;
            ☃.bipedLeftArm.showModel = true;
            break;
         case LEGS:
            ☃.bipedBody.showModel = true;
            ☃.bipedRightLeg.showModel = true;
            ☃.bipedLeftLeg.showModel = true;
            break;
         case FEET:
            ☃.bipedRightLeg.showModel = true;
            ☃.bipedLeftLeg.showModel = true;
      }
   }

   protected void setModelVisible(ModelBiped var1) {
      ☃.setVisible(false);
   }
}
