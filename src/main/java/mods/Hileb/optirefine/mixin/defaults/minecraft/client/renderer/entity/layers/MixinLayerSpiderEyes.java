package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerSpiderEyes.class)
public abstract class MixinLayerSpiderEyes {

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntitySpider;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void wrap_render(ModelBase instance, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original){
        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }

        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), true);
        instance.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), false);
        if (Config.isShaders()) {
            Shaders.endSpiderEyes();
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native void RenderGlobal_renderOverlayEyes_set(RenderGlobal renderGlobal, boolean val);
}
