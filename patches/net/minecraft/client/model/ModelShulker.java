package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.math.MathHelper;

public class ModelShulker extends ModelBase {
   public final ModelRenderer base;
   public final ModelRenderer lid;
   public ModelRenderer head;

   public ModelShulker() {
      this.textureHeight = 64;
      this.textureWidth = 64;
      this.lid = new ModelRenderer(this);
      this.base = new ModelRenderer(this);
      this.head = new ModelRenderer(this);
      this.lid.setTextureOffset(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16, 12, 16);
      this.lid.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.base.setTextureOffset(0, 28).addBox(-8.0F, -8.0F, -8.0F, 16, 8, 16);
      this.base.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.head.setTextureOffset(0, 52).addBox(-3.0F, 0.0F, -3.0F, 6, 6, 6);
      this.head.setRotationPoint(0.0F, 12.0F, 0.0F);
   }

   @Override
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      EntityShulker ☃ = (EntityShulker)☃;
      float ☃x = ☃ - ☃.ticksExisted;
      float ☃xx = (0.5F + ☃.getClientPeekAmount(☃x)) * (float) Math.PI;
      float ☃xxx = -1.0F + MathHelper.sin(☃xx);
      float ☃xxxx = 0.0F;
      if (☃xx > (float) Math.PI) {
         ☃xxxx = MathHelper.sin(☃ * 0.1F) * 0.7F;
      }

      this.lid.setRotationPoint(0.0F, 16.0F + MathHelper.sin(☃xx) * 8.0F + ☃xxxx, 0.0F);
      if (☃.getClientPeekAmount(☃x) > 0.3F) {
         this.lid.rotateAngleY = ☃xxx * ☃xxx * ☃xxx * ☃xxx * (float) Math.PI * 0.125F;
      } else {
         this.lid.rotateAngleY = 0.0F;
      }

      this.head.rotateAngleX = ☃ * (float) (Math.PI / 180.0);
      this.head.rotateAngleY = ☃ * (float) (Math.PI / 180.0);
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.base.render(☃);
      this.lid.render(☃);
   }
}
