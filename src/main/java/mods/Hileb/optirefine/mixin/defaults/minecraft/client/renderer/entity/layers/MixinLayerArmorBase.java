package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {

    @WrapOperation(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;getArmorResource(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/EntityEquipmentSlot;Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation getResources(LayerArmorBase instance, Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type, Operation<ResourceLocation> original){
        if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(stack, slot, type)) {
            return original.call(instance, entity, stack, slot, type);
        }
        return null;
    }

    @WrapWithCondition(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public boolean replace_renderer_bindTexture_this_getArmorResource(RenderLivingBase instance, ResourceLocation location) {
        return location != null;
    }
}
/*
+++ net/minecraft/client/renderer/entity/layers/LayerArmorBase.java	Tue Aug 19 14:59:58 2025
@@ -3,17 +3,23 @@
 import com.google.common.collect.Maps;
 import java.util.Map;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderLivingBase;
+import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.inventory.EntityEquipmentSlot;
 import net.minecraft.item.ItemArmor;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.CustomItems;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.shaders.Shaders;
+import net.optifine.shaders.ShadersRender;

 public abstract class LayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected T modelLeggings;
    protected T modelArmor;
    private final RenderLivingBase<?> renderer;
@@ -45,36 +51,75 @@
    ) {
       ItemStack var10 = var1.getItemStackFromSlot(var9);
       if (var10.getItem() instanceof ItemArmor) {
          ItemArmor var11 = (ItemArmor)var10.getItem();
          if (var11.getEquipmentSlot() == var9) {
             ModelBase var12 = this.getModelFromSlot(var9);
+            if (Reflector.ForgeHooksClient.exists()) {
+               var12 = this.getArmorModelHook(var1, var10, var9, (T)var12);
+            }
+
             var12.setModelAttributes(this.renderer.getMainModel());
             var12.setLivingAnimations(var1, var2, var3, var4);
             this.setModelSlotVisible((T)var12, var9);
             boolean var13 = this.isLegSlot(var9);
-            this.renderer.bindTexture(this.getArmorResource(var11, var13));
+            if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(var10, var9, null)) {
+               if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
+                  this.renderer.bindTexture(this.getArmorResource(var1, var10, var9, null));
+               } else {
+                  this.renderer.bindTexture(this.getArmorResource(var11, var13));
+               }
+            }
+
+            if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
+               if (ReflectorForge.armorHasOverlay(var11, var10)) {
+                  int var18 = var11.getColor(var10);
+                  float var19 = (var18 >> 16 & 0xFF) / 255.0F;
+                  float var20 = (var18 >> 8 & 0xFF) / 255.0F;
+                  float var21 = (var18 & 0xFF) / 255.0F;
+                  GlStateManager.color(this.colorR * var19, this.colorG * var20, this.colorB * var21, this.alpha);
+                  var12.render(var1, var2, var3, var5, var6, var7, var8);
+                  if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(var10, var9, "overlay")) {
+                     this.renderer.bindTexture(this.getArmorResource(var1, var10, var9, "overlay"));
+                  }
+               }
+
+               GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
+               var12.render(var1, var2, var3, var5, var6, var7, var8);
+               if (!this.skipRenderGlint
+                  && var10.hasEffect()
+                  && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(var1, var10, var12, var2, var3, var4, var5, var6, var7, var8))) {
+                  renderEnchantedGlint(this.renderer, var1, var12, var2, var3, var4, var5, var6, var7, var8);
+               }
+
+               return;
+            }
+
             switch (var11.getArmorMaterial()) {
                case LEATHER:
                   int var14 = var11.getColor(var10);
                   float var15 = (var14 >> 16 & 0xFF) / 255.0F;
                   float var16 = (var14 >> 8 & 0xFF) / 255.0F;
                   float var17 = (var14 & 0xFF) / 255.0F;
                   GlStateManager.color(this.colorR * var15, this.colorG * var16, this.colorB * var17, this.alpha);
                   var12.render(var1, var2, var3, var5, var6, var7, var8);
-                  this.renderer.bindTexture(this.getArmorResource(var11, var13, "overlay"));
+                  if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(var10, var9, "overlay")) {
+                     this.renderer.bindTexture(this.getArmorResource(var11, var13, "overlay"));
+                  }
                case CHAIN:
                case IRON:
                case GOLD:
                case DIAMOND:
                   GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                   var12.render(var1, var2, var3, var5, var6, var7, var8);
-               default:
-                  if (!this.skipRenderGlint && var10.isItemEnchanted()) {
-                     renderEnchantedGlint(this.renderer, var1, var12, var2, var3, var4, var5, var6, var7, var8);
-                  }
+            }
+
+            if (!this.skipRenderGlint
+               && var10.isItemEnchanted()
+               && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(var1, var10, var12, var2, var3, var4, var5, var6, var7, var8))) {
+               renderEnchantedGlint(this.renderer, var1, var12, var2, var3, var4, var5, var6, var7, var8);
             }
          }
       }
    }

    public T getModelFromSlot(EntityEquipmentSlot var1) {
@@ -85,49 +130,58 @@
       return var1 == EntityEquipmentSlot.LEGS;
    }

    public static void renderEnchantedGlint(
       RenderLivingBase<?> var0, EntityLivingBase var1, ModelBase var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9
    ) {
-      float var10 = var1.ticksExisted + var5;
-      var0.bindTexture(ENCHANTED_ITEM_GLINT_RES);
-      Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
-      GlStateManager.enableBlend();
-      GlStateManager.depthFunc(514);
-      GlStateManager.depthMask(false);
-      float var11 = 0.5F;
-      GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
-
-      for (int var12 = 0; var12 < 2; var12++) {
-         GlStateManager.disableLighting();
-         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
-         float var13 = 0.76F;
-         GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
+      if (!Config.isShaders() || !Shaders.isShadowPass) {
+         float var10 = var1.ticksExisted + var5;
+         var0.bindTexture(ENCHANTED_ITEM_GLINT_RES);
+         if (Config.isShaders()) {
+            ShadersRender.renderEnchantedGlintBegin();
+         }
+
+         Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
+         GlStateManager.enableBlend();
+         GlStateManager.depthFunc(514);
+         GlStateManager.depthMask(false);
+         float var11 = 0.5F;
+         GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
+
+         for (int var12 = 0; var12 < 2; var12++) {
+            GlStateManager.disableLighting();
+            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
+            float var13 = 0.76F;
+            GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
+            GlStateManager.matrixMode(5890);
+            GlStateManager.loadIdentity();
+            float var14 = 0.33333334F;
+            GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
+            GlStateManager.rotate(30.0F - var12 * 60.0F, 0.0F, 0.0F, 1.0F);
+            GlStateManager.translate(0.0F, var10 * (0.001F + var12 * 0.003F) * 20.0F, 0.0F);
+            GlStateManager.matrixMode(5888);
+            var2.render(var1, var3, var4, var6, var7, var8, var9);
+            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
+         }
+
          GlStateManager.matrixMode(5890);
          GlStateManager.loadIdentity();
-         float var14 = 0.33333334F;
-         GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
-         GlStateManager.rotate(30.0F - var12 * 60.0F, 0.0F, 0.0F, 1.0F);
-         GlStateManager.translate(0.0F, var10 * (0.001F + var12 * 0.003F) * 20.0F, 0.0F);
          GlStateManager.matrixMode(5888);
-         var2.render(var1, var3, var4, var6, var7, var8, var9);
-         GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
+         GlStateManager.enableLighting();
+         GlStateManager.depthMask(true);
+         GlStateManager.depthFunc(515);
+         GlStateManager.disableBlend();
+         Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
+         if (Config.isShaders()) {
+            ShadersRender.renderEnchantedGlintEnd();
+         }
       }
-
-      GlStateManager.matrixMode(5890);
-      GlStateManager.loadIdentity();
-      GlStateManager.matrixMode(5888);
-      GlStateManager.enableLighting();
-      GlStateManager.depthMask(true);
-      GlStateManager.depthFunc(515);
-      GlStateManager.disableBlend();
-      Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
    }

    private ResourceLocation getArmorResource(ItemArmor var1, boolean var2) {
-      return this.getArmorResource(var1, var2, null);
+      return this.getArmorResource(var1, var2, (String)null);
    }

    private ResourceLocation getArmorResource(ItemArmor var1, boolean var2, String var3) {
       String var4 = String.format(
          "textures/models/armor/%s_layer_%d%s.png", var1.getArmorMaterial().getName(), var2 ? 2 : 1, var3 == null ? "" : String.format("_%s", var3)
       );
@@ -140,7 +194,34 @@
       return var5;
    }

    protected abstract void initArmor();

    protected abstract void setModelSlotVisible(T var1, EntityEquipmentSlot var2);
+
+   protected T getArmorModelHook(EntityLivingBase var1, ItemStack var2, EntityEquipmentSlot var3, T var4) {
+      return (T)var4;
+   }
+
+   public ResourceLocation getArmorResource(Entity var1, ItemStack var2, EntityEquipmentSlot var3, String var4) {
+      ItemArmor var5 = (ItemArmor)var2.getItem();
+      String var6 = var5.getArmorMaterial().getName();
+      String var7 = "minecraft";
+      int var8 = var6.indexOf(58);
+      if (var8 != -1) {
+         var7 = var6.substring(0, var8);
+         var6 = var6.substring(var8 + 1);
+      }
+
+      String var9 = String.format(
+         "%s:textures/models/armor/%s_layer_%d%s.png", var7, var6, this.isLegSlot(var3) ? 2 : 1, var4 == null ? "" : String.format("_%s", var4)
+      );
+      var9 = Reflector.callString(Reflector.ForgeHooksClient_getArmorTexture, new Object[]{var1, var2, var9, var3, var4});
+      ResourceLocation var10 = ARMOR_TEXTURE_RES_MAP.get(var9);
+      if (var10 == null) {
+         var10 = new ResourceLocation(var9);
+         ARMOR_TEXTURE_RES_MAP.put(var9, var10);
+      }
+
+      return var10;
+   }
 }
 */
