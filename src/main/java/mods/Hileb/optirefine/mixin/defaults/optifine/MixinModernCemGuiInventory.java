package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiInventory.class)
public abstract class MixinModernCemGuiInventory {
    @WrapOperation(method = "drawEntityOnScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V"))
    private static void optiRefine$guiContext(RenderManager manager, Entity entity, double x, double y, double z,
                                              float yaw, float partialTicks, boolean hideDebugBox, Operation<Void> original) {
        CemRenderContext.pushGui(entity, entity.world, partialTicks);
        try {
            original.call(manager, entity, x, y, z, yaw, partialTicks, hideDebugBox);
        } finally {
            CemRenderContext.pop();
        }
    }
}
