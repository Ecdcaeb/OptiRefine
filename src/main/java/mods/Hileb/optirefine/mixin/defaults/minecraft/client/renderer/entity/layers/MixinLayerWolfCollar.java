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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @Redirect(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntityWolf;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/EnumDyeColor;getColorComponentValues()[F"))
    // [AUDIT-OK] getColorComponentValues INVOKE unique (deobf:26); CustomColors.getWolfCollarColors matches OF:20-22
    public float[] optiRefine$applyCustomCollarColor(EnumDyeColor dyeColor) {
        if (Config.isCustomColors()) {
            return CustomColors.getWolfCollarColors(dyeColor, dyeColor.getColorComponentValues());
        } else return dyeColor.getColorComponentValues();
    }
}
