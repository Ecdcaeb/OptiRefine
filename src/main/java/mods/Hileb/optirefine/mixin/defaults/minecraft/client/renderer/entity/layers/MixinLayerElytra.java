package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerElytra.class)
public abstract class MixinLayerElytra {
    @Shadow @Final
    private static ResourceLocation TEXTURE_ELYTRA;


    @Redirect(method = "doRenderLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/LayerElytra;TEXTURE_ELYTRA:Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation customTexture(@Local(argsOnly = true) EntityLivingBase entityLivingBaseIn){
        if (Config.isCustomItems()) {
            ItemStack itemStack = entityLivingBaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            return CustomItems.getCustomElytraTexture(itemStack, TEXTURE_ELYTRA);
        } else return TEXTURE_ELYTRA;
    }
}
/*
+++ net/minecraft/client/renderer/entity/layers/LayerElytra.java	Tue Aug 19 14:59:58 2025
@@ -7,12 +7,13 @@
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EnumPlayerModelParts;
 import net.minecraft.init.Items;
 import net.minecraft.inventory.EntityEquipmentSlot;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.CustomItems;

 public class LayerElytra implements LayerRenderer<EntityLivingBase> {
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    protected final RenderLivingBase<?> renderPlayer;
    private final ModelElytra modelElytra = new ModelElytra();

@@ -24,22 +25,32 @@
       ItemStack var9 = var1.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
       if (var9.getItem() == Items.ELYTRA) {
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.enableBlend();
          GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
          if (var1 instanceof AbstractClientPlayer) {
-            AbstractClientPlayer var10 = (AbstractClientPlayer)var1;
-            if (var10.isPlayerInfoSet() && var10.getLocationElytra() != null) {
-               this.renderPlayer.bindTexture(var10.getLocationElytra());
-            } else if (var10.hasPlayerInfo() && var10.getLocationCape() != null && var10.isWearing(EnumPlayerModelParts.CAPE)) {
-               this.renderPlayer.bindTexture(var10.getLocationCape());
+            AbstractClientPlayer var12 = (AbstractClientPlayer)var1;
+            if (var12.isPlayerInfoSet() && var12.getLocationElytra() != null) {
+               this.renderPlayer.bindTexture(var12.getLocationElytra());
+            } else if (var12.hasElytraCape() && var12.hasPlayerInfo() && var12.getLocationCape() != null && var12.isWearing(EnumPlayerModelParts.CAPE)) {
+               this.renderPlayer.bindTexture(var12.getLocationCape());
             } else {
-               this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
+               ResourceLocation var11 = TEXTURE_ELYTRA;
+               if (Config.isCustomItems()) {
+                  var11 = CustomItems.getCustomElytraTexture(var9, var11);
+               }
+
+               this.renderPlayer.bindTexture(var11);
             }
          } else {
-            this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
+            ResourceLocation var10 = TEXTURE_ELYTRA;
+            if (Config.isCustomItems()) {
+               var10 = CustomItems.getCustomElytraTexture(var9, var10);
+            }
+
+            this.renderPlayer.bindTexture(var10);
          }

          GlStateManager.pushMatrix();
          GlStateManager.translate(0.0F, 0.0F, 0.125F);
          this.modelElytra.setRotationAngles(var2, var3, var5, var6, var7, var8, var1);
          this.modelElytra.render(var1, var2, var3, var5, var6, var7, var8);
 */
