package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelLlamaSpit extends ModelBase {
   private final ModelRenderer main = new ModelRenderer(this);

   public ModelLlamaSpit() {
      this(0.0F);
   }

   public ModelLlamaSpit(float var1) {
      int ☃ = 2;
      this.main.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, 0.0F, 2, 2, 2, ☃);
      this.main.setTextureOffset(0, 0).addBox(0.0F, -4.0F, 0.0F, 2, 2, 2, ☃);
      this.main.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -4.0F, 2, 2, 2, ☃);
      this.main.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, ☃);
      this.main.setTextureOffset(0, 0).addBox(2.0F, 0.0F, 0.0F, 2, 2, 2, ☃);
      this.main.setTextureOffset(0, 0).addBox(0.0F, 2.0F, 0.0F, 2, 2, 2, ☃);
      this.main.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 2.0F, 2, 2, 2, ☃);
      this.main.setRotationPoint(0.0F, 0.0F, 0.0F);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.main.render(☃);
   }
}
