package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;

public class ModelDragon extends ModelBase {
   private final ModelRenderer head;
   private final ModelRenderer spine;
   private final ModelRenderer jaw;
   private final ModelRenderer body;
   private final ModelRenderer rearLeg;
   private final ModelRenderer frontLeg;
   private final ModelRenderer rearLegTip;
   private final ModelRenderer frontLegTip;
   private final ModelRenderer rearFoot;
   private final ModelRenderer frontFoot;
   private final ModelRenderer wing;
   private final ModelRenderer wingTip;
   private float partialTicks;

   public ModelDragon(float var1) {
      this.textureWidth = 256;
      this.textureHeight = 256;
      this.setTextureOffset("body.body", 0, 0);
      this.setTextureOffset("wing.skin", -56, 88);
      this.setTextureOffset("wingtip.skin", -56, 144);
      this.setTextureOffset("rearleg.main", 0, 0);
      this.setTextureOffset("rearfoot.main", 112, 0);
      this.setTextureOffset("rearlegtip.main", 196, 0);
      this.setTextureOffset("head.upperhead", 112, 30);
      this.setTextureOffset("wing.bone", 112, 88);
      this.setTextureOffset("head.upperlip", 176, 44);
      this.setTextureOffset("jaw.jaw", 176, 65);
      this.setTextureOffset("frontleg.main", 112, 104);
      this.setTextureOffset("wingtip.bone", 112, 136);
      this.setTextureOffset("frontfoot.main", 144, 104);
      this.setTextureOffset("neck.box", 192, 104);
      this.setTextureOffset("frontlegtip.main", 226, 138);
      this.setTextureOffset("body.scale", 220, 53);
      this.setTextureOffset("head.scale", 0, 0);
      this.setTextureOffset("neck.scale", 48, 0);
      this.setTextureOffset("head.nostril", 112, 0);
      float ☃ = -16.0F;
      this.head = new ModelRenderer(this, "head");
      this.head.addBox("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16);
      this.head.addBox("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16);
      this.head.mirror = true;
      this.head.addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6);
      this.head.addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4);
      this.head.mirror = false;
      this.head.addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6);
      this.head.addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4);
      this.jaw = new ModelRenderer(this, "jaw");
      this.jaw.setRotationPoint(0.0F, 4.0F, -8.0F);
      this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
      this.head.addChild(this.jaw);
      this.spine = new ModelRenderer(this, "neck");
      this.spine.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
      this.spine.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
      this.body = new ModelRenderer(this, "body");
      this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
      this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
      this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
      this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
      this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
      this.wing = new ModelRenderer(this, "wing");
      this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
      this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
      this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
      this.wingTip = new ModelRenderer(this, "wingtip");
      this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
      this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
      this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
      this.wing.addChild(this.wingTip);
      this.frontLeg = new ModelRenderer(this, "frontleg");
      this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
      this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
      this.frontLegTip = new ModelRenderer(this, "frontlegtip");
      this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
      this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
      this.frontLeg.addChild(this.frontLegTip);
      this.frontFoot = new ModelRenderer(this, "frontfoot");
      this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
      this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
      this.frontLegTip.addChild(this.frontFoot);
      this.rearLeg = new ModelRenderer(this, "rearleg");
      this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
      this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
      this.rearLegTip = new ModelRenderer(this, "rearlegtip");
      this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
      this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
      this.rearLeg.addChild(this.rearLegTip);
      this.rearFoot = new ModelRenderer(this, "rearfoot");
      this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
      this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
      this.rearLegTip.addChild(this.rearFoot);
   }

   @Override
   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      this.partialTicks = ☃;
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      GlStateManager.pushMatrix();
      EntityDragon ☃ = (EntityDragon)☃;
      float ☃x = ☃.prevAnimTime + (☃.animTime - ☃.prevAnimTime) * this.partialTicks;
      this.jaw.rotateAngleX = (float)(Math.sin(☃x * (float) (Math.PI * 2)) + 1.0) * 0.2F;
      float ☃xx = (float)(Math.sin(☃x * (float) (Math.PI * 2) - 1.0F) + 1.0);
      ☃xx = (☃xx * ☃xx + ☃xx * 2.0F) * 0.05F;
      GlStateManager.translate(0.0F, ☃xx - 2.0F, -3.0F);
      GlStateManager.rotate(☃xx * 2.0F, 1.0F, 0.0F, 0.0F);
      float ☃xxx = -30.0F;
      float ☃xxxx = 0.0F;
      float ☃xxxxx = 1.5F;
      double[] ☃xxxxxx = ☃.getMovementOffsets(6, this.partialTicks);
      float ☃xxxxxxx = this.updateRotations(☃.getMovementOffsets(5, this.partialTicks)[0] - ☃.getMovementOffsets(10, this.partialTicks)[0]);
      float ☃xxxxxxxx = this.updateRotations(☃.getMovementOffsets(5, this.partialTicks)[0] + ☃xxxxxxx / 2.0F);
      float ☃xxxxxxxxx = ☃x * (float) (Math.PI * 2);
      ☃xxx = 20.0F;
      float ☃xxxxxxxxxx = -12.0F;

      for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < 5; ☃xxxxxxxxxxx++) {
         double[] ☃xxxxxxxxxxxx = ☃.getMovementOffsets(5 - ☃xxxxxxxxxxx, this.partialTicks);
         float ☃xxxxxxxxxxxxx = (float)Math.cos(☃xxxxxxxxxxx * 0.45F + ☃xxxxxxxxx) * 0.15F;
         this.spine.rotateAngleY = this.updateRotations(☃xxxxxxxxxxxx[0] - ☃xxxxxx[0]) * (float) (Math.PI / 180.0) * 1.5F;
         this.spine.rotateAngleX = ☃xxxxxxxxxxxxx + ☃.getHeadPartYOffset(☃xxxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxxxxx) * (float) (Math.PI / 180.0) * 1.5F * 5.0F;
         this.spine.rotateAngleZ = -this.updateRotations(☃xxxxxxxxxxxx[0] - ☃xxxxxxxx) * (float) (Math.PI / 180.0) * 1.5F;
         this.spine.rotationPointY = ☃xxx;
         this.spine.rotationPointZ = ☃xxxxxxxxxx;
         this.spine.rotationPointX = ☃xxxx;
         ☃xxx = (float)(☃xxx + Math.sin(this.spine.rotateAngleX) * 10.0);
         ☃xxxxxxxxxx = (float)(☃xxxxxxxxxx - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
         ☃xxxx = (float)(☃xxxx - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
         this.spine.render(☃);
      }

      this.head.rotationPointY = ☃xxx;
      this.head.rotationPointZ = ☃xxxxxxxxxx;
      this.head.rotationPointX = ☃xxxx;
      double[] ☃xxxxxxxxxxx = ☃.getMovementOffsets(0, this.partialTicks);
      this.head.rotateAngleY = this.updateRotations(☃xxxxxxxxxxx[0] - ☃xxxxxx[0]) * (float) (Math.PI / 180.0);
      this.head.rotateAngleX = this.updateRotations(☃.getHeadPartYOffset(6, ☃xxxxxx, ☃xxxxxxxxxxx)) * (float) (Math.PI / 180.0) * 1.5F * 5.0F;
      this.head.rotateAngleZ = -this.updateRotations(☃xxxxxxxxxxx[0] - ☃xxxxxxxx) * (float) (Math.PI / 180.0);
      this.head.render(☃);
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-☃xxxxxxx * 1.5F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(0.0F, -1.0F, 0.0F);
      this.body.rotateAngleZ = 0.0F;
      this.body.render(☃);

      for (int ☃xxxxxxxxxxxx = 0; ☃xxxxxxxxxxxx < 2; ☃xxxxxxxxxxxx++) {
         GlStateManager.enableCull();
         float ☃xxxxxxxxxxxxx = ☃x * (float) (Math.PI * 2);
         this.wing.rotateAngleX = 0.125F - (float)Math.cos(☃xxxxxxxxxxxxx) * 0.2F;
         this.wing.rotateAngleY = 0.25F;
         this.wing.rotateAngleZ = (float)(Math.sin(☃xxxxxxxxxxxxx) + 0.125) * 0.8F;
         this.wingTip.rotateAngleZ = -((float)(Math.sin(☃xxxxxxxxxxxxx + 2.0F) + 0.5)) * 0.75F;
         this.rearLeg.rotateAngleX = 1.0F + ☃xx * 0.1F;
         this.rearLegTip.rotateAngleX = 0.5F + ☃xx * 0.1F;
         this.rearFoot.rotateAngleX = 0.75F + ☃xx * 0.1F;
         this.frontLeg.rotateAngleX = 1.3F + ☃xx * 0.1F;
         this.frontLegTip.rotateAngleX = -0.5F - ☃xx * 0.1F;
         this.frontFoot.rotateAngleX = 0.75F + ☃xx * 0.1F;
         this.wing.render(☃);
         this.frontLeg.render(☃);
         this.rearLeg.render(☃);
         GlStateManager.scale(-1.0F, 1.0F, 1.0F);
         if (☃xxxxxxxxxxxx == 0) {
            GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
         }
      }

      GlStateManager.popMatrix();
      GlStateManager.cullFace(GlStateManager.CullFace.BACK);
      GlStateManager.disableCull();
      float ☃xxxxxxxxxxxxx = -((float)Math.sin(☃x * (float) (Math.PI * 2))) * 0.0F;
      ☃xxxxxxxxx = ☃x * (float) (Math.PI * 2);
      ☃xxx = 10.0F;
      ☃xxxxxxxxxx = 60.0F;
      ☃xxxx = 0.0F;
      ☃xxxxxx = ☃.getMovementOffsets(11, this.partialTicks);

      for (int ☃xxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxx < 12; ☃xxxxxxxxxxxxxx++) {
         ☃xxxxxxxxxxx = ☃.getMovementOffsets(12 + ☃xxxxxxxxxxxxxx, this.partialTicks);
         ☃xxxxxxxxxxxxx = (float)(☃xxxxxxxxxxxxx + Math.sin(☃xxxxxxxxxxxxxx * 0.45F + ☃xxxxxxxxx) * 0.05F);
         this.spine.rotateAngleY = (this.updateRotations(☃xxxxxxxxxxx[0] - ☃xxxxxx[0]) * 1.5F + 180.0F) * (float) (Math.PI / 180.0);
         this.spine.rotateAngleX = ☃xxxxxxxxxxxxx + (float)(☃xxxxxxxxxxx[1] - ☃xxxxxx[1]) * (float) (Math.PI / 180.0) * 1.5F * 5.0F;
         this.spine.rotateAngleZ = this.updateRotations(☃xxxxxxxxxxx[0] - ☃xxxxxxxx) * (float) (Math.PI / 180.0) * 1.5F;
         this.spine.rotationPointY = ☃xxx;
         this.spine.rotationPointZ = ☃xxxxxxxxxx;
         this.spine.rotationPointX = ☃xxxx;
         ☃xxx = (float)(☃xxx + Math.sin(this.spine.rotateAngleX) * 10.0);
         ☃xxxxxxxxxx = (float)(☃xxxxxxxxxx - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
         ☃xxxx = (float)(☃xxxx - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
         this.spine.render(☃);
      }

      GlStateManager.popMatrix();
   }

   private float updateRotations(double var1) {
      while (☃ >= 180.0) {
         ☃ -= 360.0;
      }

      while (☃ < -180.0) {
         ☃ += 360.0;
      }

      return (float)☃;
   }
}
