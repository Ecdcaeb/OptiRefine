package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.debug;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.debug.DebugRendererChunkBorder;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DebugRendererChunkBorder.class)
public abstract class MixinDebugRendererChunkBorder {
    @WrapMethod(method = "render")
    public void isShadowPas_render(float partialTicks, long finishTimeNano, Operation<Void> original){
        if (!Shaders.isShadowPass) {
            if (Config.isShaders()) {
                Shaders.beginLeash();
            }
            original.call(partialTicks, finishTimeNano);
            if (Config.isShaders()) {
                Shaders.endLeash();
            }
        }
    }
}
