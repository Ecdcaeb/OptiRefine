package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ModelPlayer.class)
public class MixinModelPlayer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] baseline member bipedCape exists in target class
    @Final
    @Shadow
    private ModelRenderer bipedCape;

// [AUDIT-OK] target setRotationAngles(FFFFFFFLnet/minecraft/entity/Entity;)V matches baseline; only bipedCape rotationPointY writes in method
    @WrapOperation(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelRenderer;rotationPointY:F", opcode = Opcodes.PUTFIELD))
    public void removeThebipedCape_rotationPointYSet(ModelRenderer instance, float value, Operation<Void> original){
        if (instance == this.bipedCape) {
            return;
        }
        original.call(instance, value);
    }

}
