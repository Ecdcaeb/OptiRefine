package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Checked
@Mixin(ModelPlayer.class)
public class MixinModelPlayer {

    @Final
    @Shadow
    private ModelRenderer bipedCape;

    @WrapOperation(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelRenderer;rotationPointY:F", opcode = Opcodes.PUTFIELD))
    public void removeThebipedCape_rotationPointYSet(ModelRenderer instance, float value, Operation<Void> original){
        if (instance == this.bipedCape) {
            return;
        }
        original.call(instance, value);
    }

}
