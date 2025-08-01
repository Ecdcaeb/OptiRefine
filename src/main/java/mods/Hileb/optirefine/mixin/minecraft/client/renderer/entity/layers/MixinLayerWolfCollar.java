package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity.layers;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.item.EnumDyeColor;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerWolfCollar.class)
public class MixinLayerWolfCollar {
    @Redirect(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntityWolf;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/EnumDyeColor;getColorComponentValues()[F"))
    public float[] customColor(EnumDyeColor instance) {
        if (Config.isCustomColors()) {
            return CustomColors.getWolfCollarColors(instance, instance.getColorComponentValues());
        } else return instance.getColorComponentValues();
    }
}
