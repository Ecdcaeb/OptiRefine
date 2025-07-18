package net.minecraft.client.model;

public class ModelPig extends ModelQuadruped {
   public ModelPig() {
      this(0.0F);
   }

   public ModelPig(float var1) {
      super(6, ☃);
      this.head.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4, 3, 1, ☃);
      this.childYOffset = 4.0F;
   }
}
