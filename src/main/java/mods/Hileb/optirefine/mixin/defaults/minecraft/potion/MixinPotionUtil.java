package mods.Hileb.optirefine.mixin.defaults.minecraft.potion;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @WrapOperation(method = "getPotionColorFromEffectList", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getLiquidColor()I"))
// [AUDIT-OK] getLiquidColor custom-color wrap matches OF PotionUtils (CustomColors.getPotionColor per effect)
    private static int optiRefine$applyCustomPotionColor(Potion potion, Operation<Integer> original){
        int k = original.call(potion);
        if (Config.isCustomColors()) {
            return CustomColors.getPotionColor(potion, k);
        }
        return k;
    }

    @WrapMethod(method = "getPotionColorFromEffectList")
// [AUDIT-OK] empty-list branch matches OF (3694022 / CustomColors.getPotionColor(null, 3694022))
    private static int optiRefine$colorEmptyPotionEffects(Collection<PotionEffect> effects, Operation<Integer> original){
        if (effects.isEmpty()) {
            return Config.isCustomColors() ? CustomColors.getPotionColor(null, 3694022) : 3694022;
        } else return original.call(effects);
    }
}
