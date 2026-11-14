package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(RenderLiving.class)
public abstract class MixinRenderLiving {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @WrapMethod(method = "renderLeash")
    // [AUDIT-OK] target renderLeash baseline; shader/shadow-pass guard matches OF renderLeash
    public void blockRenderLeashForConfig(EntityLiving entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (Config.isShaders() && Shaders.isShadowPass) {
            return;
        }
        boolean shaders = Config.isShaders() && entityLivingIn.getLeashHolder() != null;
        if (shaders) {
            Shaders.beginLeash();
        }
        try {
            original.call(entityLivingIn, x, y, z, entityYaw, partialTicks);
        } finally {
            if (shaders) {
                Shaders.endLeash();
            }
        }
    }

}
