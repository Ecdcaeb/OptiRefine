package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerEndermanEyes.class)
public class MixinLayerEndermanEyes {

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntityEnderman;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelEnderman;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void wrap_render(ModelEnderman instance, Entity entity, float v0, float v1, float v2, float v3, float v4, float v5, Operation<Void> original){
        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }

        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), true);
        instance.render(entity, v0, v1, v2, v3, v4, v5);
        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), false);
        if (Config.isShaders()) {
            // SpiderEyes?
            Shaders.endSpiderEyes();
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native void RenderGlobal_renderOverlayEyes_set(RenderGlobal renderGlobal, boolean val);

}
