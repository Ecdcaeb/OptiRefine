package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.item.EnumDyeColor;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerWolfCollar.class)
public abstract class MixinLayerWolfCollar {
    @Redirect(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntityWolf;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/EnumDyeColor;getColorComponentValues()[F"))
    public float[] customColor(EnumDyeColor instance) {
        if (Config.isCustomColors()) {
            return CustomColors.getWolfCollarColors(instance, instance.getColorComponentValues());
        } else return instance.getColorComponentValues();
    }
}
/*
+++ net/minecraft/client/renderer/entity/layers/LayerWolfCollar.java	Tue Aug 19 14:59:58 2025
@@ -1,27 +1,32 @@
 package net.minecraft.client.renderer.entity.layers;

 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderWolf;
 import net.minecraft.entity.passive.EntityWolf;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.CustomColors;

 public class LayerWolfCollar implements LayerRenderer<EntityWolf> {
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;

    public LayerWolfCollar(RenderWolf var1) {
       this.wolfRenderer = var1;
    }

    public void doRenderLayer(EntityWolf var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
-      if (var1.isTamed() && !var1.isInvisible()) {
-         this.wolfRenderer.bindTexture(WOLF_COLLAR);
+      if (var1.dl() && !var1.isInvisible()) {
+         this.wolfRenderer.a(WOLF_COLLAR);
          float[] var9 = var1.getCollarColor().getColorComponentValues();
+         if (Config.isCustomColors()) {
+            var9 = CustomColors.getWolfCollarColors(var1.getCollarColor(), var9);
+         }
+
          GlStateManager.color(var9[0], var9[1], var9[2]);
-         this.wolfRenderer.getMainModel().render(var1, var2, var3, var5, var6, var7, var8);
+         this.wolfRenderer.b().render(var1, var2, var3, var5, var6, var7, var8);
       }
    }

    public boolean shouldCombineTextures() {
       return true;
    }
 */
