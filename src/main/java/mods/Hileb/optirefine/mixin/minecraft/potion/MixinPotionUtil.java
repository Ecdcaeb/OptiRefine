package mods.Hileb.optirefine.mixin.minecraft.potion;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(PotionUtils.class)
public abstract class MixinPotionUtil {

    @Redirect(method = "getPotionColorFromEffectList", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getLiquidColor()I"))
    private static int injectGetPotionColorFromEffectList(Potion instance){
        int k = instance.getLiquidColor();
        if (Config.isCustomColors()) {
            return CustomColors.getPotionColor(instance, k);
        }
        return k;
    }

    @WrapMethod(method = "getPotionColorFromEffectList")
    private static int wrapGetPotionColorFromEffectList(Collection<PotionEffect> p_185181_0_, Operation<Integer> original){
        if (p_185181_0_.isEmpty()) {
            return Config.isCustomColors() ? CustomColors.getPotionColor(null, 3694022) : 3694022;
        } else return original.call(p_185181_0_);
    }



}
