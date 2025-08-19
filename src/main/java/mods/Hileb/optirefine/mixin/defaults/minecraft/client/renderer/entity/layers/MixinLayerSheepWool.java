package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.item.EnumDyeColor;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerSheepWool.class)
public abstract class MixinLayerSheepWool {

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntitySheep;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/EntitySheep;getDyeRgb(Lnet/minecraft/item/EnumDyeColor;)[F"))
    public float[] custom_color(EnumDyeColor dyeColor, Operation<float[]> original) {
        float[] floats = original.call(dyeColor);
        if (Config.isCustomColors()) {
            return CustomColors.getSheepColors(dyeColor, floats);
        } else return floats;
    }

}
/*
+++ net/minecraft/client/renderer/entity/layers/LayerSheepWool.java	Tue Aug 19 14:59:58 2025
@@ -3,17 +3,18 @@
 import net.minecraft.client.model.ModelSheep1;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderSheep;
 import net.minecraft.entity.passive.EntitySheep;
 import net.minecraft.item.EnumDyeColor;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.CustomColors;

 public class LayerSheepWool implements LayerRenderer<EntitySheep> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderSheep sheepRenderer;
-   private final ModelSheep1 sheepModel = new ModelSheep1();
+   public ModelSheep1 sheepModel = new ModelSheep1();

    public LayerSheepWool(RenderSheep var1) {
       this.sheepRenderer = var1;
    }

    public void doRenderLayer(EntitySheep var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
@@ -25,21 +26,30 @@
             int var11 = EnumDyeColor.values().length;
             int var12 = var10 % var11;
             int var13 = (var10 + 1) % var11;
             float var14 = (var1.ticksExisted % 25 + var4) / 25.0F;
             float[] var15 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(var12));
             float[] var16 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(var13));
+            if (Config.isCustomColors()) {
+               var15 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(var12), var15);
+               var16 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(var13), var16);
+            }
+
             GlStateManager.color(
                var15[0] * (1.0F - var14) + var16[0] * var14, var15[1] * (1.0F - var14) + var16[1] * var14, var15[2] * (1.0F - var14) + var16[2] * var14
             );
          } else {
             float[] var9 = EntitySheep.getDyeRgb(var1.getFleeceColor());
+            if (Config.isCustomColors()) {
+               var9 = CustomColors.getSheepColors(var1.getFleeceColor(), var9);
+            }
+
             GlStateManager.color(var9[0], var9[1], var9[2]);
          }

-         this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
+         this.sheepModel.setModelAttributes(this.sheepRenderer.b());
          this.sheepModel.setLivingAnimations(var1, var2, var3, var4);
          this.sheepModel.render(var1, var2, var3, var5, var6, var7, var8);
       }
    }

    public boolean shouldCombineTextures() {
 */
