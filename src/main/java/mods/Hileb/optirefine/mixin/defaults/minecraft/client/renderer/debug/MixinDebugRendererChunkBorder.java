package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.debug;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.debug.DebugRendererChunkBorder;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(DebugRendererChunkBorder.class)
public abstract class MixinDebugRendererChunkBorder {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @WrapMethod(method = "render")
    // [AUDIT-OK] render(float,long) baseline; shadow-pass guard + begin/endLeash match OF:20-111
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
