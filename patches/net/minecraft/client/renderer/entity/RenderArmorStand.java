package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderArmorStand extends RenderLivingBase<EntityArmorStand> {
   public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");

   public RenderArmorStand(RenderManager var1) {
      super(☃, new ModelArmorStand(), 0.0F);
      LayerBipedArmor ☃ = new LayerBipedArmor(this) {
         @Override
         protected void initArmor() {
            this.modelLeggings = new ModelArmorStandArmor(0.5F);
            this.modelArmor = new ModelArmorStandArmor(1.0F);
         }
      };
      this.addLayer(☃);
      this.addLayer(new LayerHeldItem(this));
      this.addLayer(new LayerElytra(this));
      this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
   }

   protected ResourceLocation getEntityTexture(EntityArmorStand var1) {
      return TEXTURE_ARMOR_STAND;
   }

   public ModelArmorStand getMainModel() {
      return (ModelArmorStand)super.getMainModel();
   }

   protected void applyRotations(EntityArmorStand var1, float var2, float var3, float var4) {
      GlStateManager.rotate(180.0F - ☃, 0.0F, 1.0F, 0.0F);
      float ☃ = (float)(☃.world.getTotalWorldTime() - ☃.punchCooldown) + ☃;
      if (☃ < 5.0F) {
         GlStateManager.rotate(MathHelper.sin(☃ / 1.5F * (float) Math.PI) * 3.0F, 0.0F, 1.0F, 0.0F);
      }
   }

   protected boolean canRenderName(EntityArmorStand var1) {
      return ☃.getAlwaysRenderNameTag();
   }

   public void doRender(EntityArmorStand var1, double var2, double var4, double var6, float var8, float var9) {
      if (☃.hasMarker()) {
         this.renderMarker = true;
      }

      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
      if (☃.hasMarker()) {
         this.renderMarker = false;
      }
   }
}
