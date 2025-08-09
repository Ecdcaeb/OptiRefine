package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity.layers;

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
