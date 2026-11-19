package mods.Hileb.optirefine.mixin.defaults.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraftFrameLighting {
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/shader/Framebuffer;framebufferRender(II)V", shift = At.Shift.AFTER))
    private void optiRefine$clearFrameLighting(CallbackInfo ci) {
        // Tooltips enable lighting. Hidden HUD skips the toast cleanup, and a disabled framebuffer skips its own cleanup.
        GlStateManager.disableLighting();
    }
}
