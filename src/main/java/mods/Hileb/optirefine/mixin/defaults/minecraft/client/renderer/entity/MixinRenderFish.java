package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderFish;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderFish.class)
public abstract class MixinRenderFish {
    @WrapOperation(method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/Tessellator;draw()V", ordinal = 1))
    private void optiRefine$drawFishingLine(Tessellator tessellator, Operation<Void> draw) {
        if (!Config.isShaders() || Shaders.isShadowPass) {
            draw.call(tessellator);
            return;
        }
        // The line has position and color only, so use the same untextured program as a leash.
        Program previous = Shaders.activeProgram;
        try {
            Shaders.useProgram(Shaders.ProgramBasic);
            draw.call(tessellator);
        } finally {
            Shaders.useProgram(previous);
        }
    }
}
