package mods.Hileb.optirefine.mixin.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.optifine.EmissiveTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

    @WrapOperation(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V"))
    public void render$(TileEntityRendererDispatcher instance, TileEntity v0, double v1, double v2, double v3, float v4, int v5, float v6, Operation<Void> original){
        if (EmissiveTextures.isActive()) {
            EmissiveTextures.beginRender();
        }

        original.call(instance, v0, v1, v2, v3, v4, v5, v6);
        if (EmissiveTextures.isActive()) {
            if (EmissiveTextures.hasEmissive()) {
                EmissiveTextures.beginRenderEmissive();
                original.call(instance, v0, v1, v2, v3, v4, v5, v6);
                EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
        }
    }




}
