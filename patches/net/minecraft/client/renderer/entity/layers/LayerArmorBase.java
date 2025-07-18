package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class LayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {
   protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   protected T modelLeggings;
   protected T modelArmor;
   private final RenderLivingBase<?> renderer;
   private float alpha = 1.0F;
   private float colorR = 1.0F;
   private float colorG = 1.0F;
   private float colorB = 1.0F;
   private boolean skipRenderGlint;
   private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

   public LayerArmorBase(RenderLivingBase<?> var1) {
      this.renderer = ☃;
      this.initArmor();
   }

   @Override
   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.renderArmorLayer(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, EntityEquipmentSlot.CHEST);
      this.renderArmorLayer(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, EntityEquipmentSlot.LEGS);
      this.renderArmorLayer(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, EntityEquipmentSlot.FEET);
      this.renderArmorLayer(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, EntityEquipmentSlot.HEAD);
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }

   private void renderArmorLayer(
      EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, EntityEquipmentSlot var9
   ) {
      ItemStack ☃ = ☃.getItemStackFromSlot(☃);
      if (☃.getItem() instanceof ItemArmor) {
         ItemArmor ☃x = (ItemArmor)☃.getItem();
         if (☃x.getEquipmentSlot() == ☃) {
            T ☃xx = this.getModelFromSlot(☃);
            ☃xx.setModelAttributes(this.renderer.getMainModel());
            ☃xx.setLivingAnimations(☃, ☃, ☃, ☃);
            this.setModelSlotVisible(☃xx, ☃);
            boolean ☃xxx = this.isLegSlot(☃);
            this.renderer.bindTexture(this.getArmorResource(☃x, ☃xxx));
            switch (☃x.getArmorMaterial()) {
               case LEATHER:
                  int ☃xxxx = ☃x.getColor(☃);
                  float ☃xxxxx = (☃xxxx >> 16 & 0xFF) / 255.0F;
                  float ☃xxxxxx = (☃xxxx >> 8 & 0xFF) / 255.0F;
                  float ☃xxxxxxx = (☃xxxx & 0xFF) / 255.0F;
                  GlStateManager.color(this.colorR * ☃xxxxx, this.colorG * ☃xxxxxx, this.colorB * ☃xxxxxxx, this.alpha);
                  ☃xx.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
                  this.renderer.bindTexture(this.getArmorResource(☃x, ☃xxx, "overlay"));
               case CHAIN:
               case IRON:
               case GOLD:
               case DIAMOND:
                  GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                  ☃xx.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
               default:
                  if (!this.skipRenderGlint && ☃.isItemEnchanted()) {
                     renderEnchantedGlint(this.renderer, ☃, ☃xx, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
                  }
            }
         }
      }
   }

   public T getModelFromSlot(EntityEquipmentSlot var1) {
      return this.isLegSlot(☃) ? this.modelLeggings : this.modelArmor;
   }

   private boolean isLegSlot(EntityEquipmentSlot var1) {
      return ☃ == EntityEquipmentSlot.LEGS;
   }

   public static void renderEnchantedGlint(
      RenderLivingBase<?> var0, EntityLivingBase var1, ModelBase var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9
   ) {
      float ☃ = ☃.ticksExisted + ☃;
      ☃.bindTexture(ENCHANTED_ITEM_GLINT_RES);
      Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
      GlStateManager.enableBlend();
      GlStateManager.depthFunc(514);
      GlStateManager.depthMask(false);
      float ☃x = 0.5F;
      GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

      for (int ☃xx = 0; ☃xx < 2; ☃xx++) {
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
         float ☃xxx = 0.76F;
         GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         float ☃xxxx = 0.33333334F;
         GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
         GlStateManager.rotate(30.0F - ☃xx * 60.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.translate(0.0F, ☃ * (0.001F + ☃xx * 0.003F) * 20.0F, 0.0F);
         GlStateManager.matrixMode(5888);
         ☃.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      }

      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      GlStateManager.matrixMode(5888);
      GlStateManager.enableLighting();
      GlStateManager.depthMask(true);
      GlStateManager.depthFunc(515);
      GlStateManager.disableBlend();
      Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
   }

   private ResourceLocation getArmorResource(ItemArmor var1, boolean var2) {
      return this.getArmorResource(☃, ☃, null);
   }

   private ResourceLocation getArmorResource(ItemArmor var1, boolean var2, String var3) {
      String ☃ = String.format("textures/models/armor/%s_layer_%d%s.png", ☃.getArmorMaterial().getName(), ☃ ? 2 : 1, ☃ == null ? "" : String.format("_%s", ☃));
      ResourceLocation ☃x = ARMOR_TEXTURE_RES_MAP.get(☃);
      if (☃x == null) {
         ☃x = new ResourceLocation(☃);
         ARMOR_TEXTURE_RES_MAP.put(☃, ☃x);
      }

      return ☃x;
   }

   protected abstract void initArmor();

   protected abstract void setModelSlotVisible(T var1, EntityEquipmentSlot var2);
}
