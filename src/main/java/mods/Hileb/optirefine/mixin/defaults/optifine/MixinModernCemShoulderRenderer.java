package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerEntityOnShoulder.class)
public abstract class MixinModernCemShoulderRenderer {
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.entity.AbstractClientPlayer entityShoulderLeft Lnet.minecraft.entity.passive.EntityShoulderRiding;")
    private static native EntityShoulderRiding optiRefine$leftShoulder(AbstractClientPlayer player);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.entity.AbstractClientPlayer entityShoulderRight Lnet.minecraft.entity.passive.EntityShoulderRiding;")
    private static native EntityShoulderRiding optiRefine$rightShoulder(AbstractClientPlayer player);

    @WrapOperation(method = "renderEntityOnShoulder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void optiRefine$shoulderContext(ModelBase model, Entity renderArgument, float limbSwing,
                                           float limbSpeed, float age, float headYaw, float headPitch,
                                           float scale, Operation<Void> original,
                                           @Local(argsOnly = true) EntityPlayer player,
                                           @Local(argsOnly = true) boolean leftShoulder) {
        Entity shoulder = null;
        if (player instanceof AbstractClientPlayer) {
            shoulder = leftShoulder ? optiRefine$leftShoulder((AbstractClientPlayer) player)
                    : optiRefine$rightShoulder((AbstractClientPlayer) player);
        }
        CemRenderContext.push(shoulder, player.world, CemRenderContext.partialTicks());
        try {
            original.call(model, renderArgument, limbSwing, limbSpeed, age, headYaw, headPitch, scale);
        } finally {
            CemRenderContext.pop();
        }
    }
}
