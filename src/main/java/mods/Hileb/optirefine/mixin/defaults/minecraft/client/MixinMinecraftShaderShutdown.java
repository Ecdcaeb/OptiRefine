package mods.Hileb.optirefine.mixin.defaults.minecraft.client;

import mods.Hileb.optirefine.optifine.resources.ModernResourcePaths;
import net.minecraft.client.Minecraft;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keep resource caches and shader GL resources inside Minecraft's lifecycle. */
@Mixin(Minecraft.class)
public abstract class MixinMinecraftShaderShutdown {
    @Inject(method = "refreshResources", at = @At("HEAD"))
    private void optiRefine$clearModernResourcePaths(CallbackInfo ci) {
        ModernResourcePaths.clearCache();
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;destroy()V", remap = false))
    private void optiRefine$releaseShaderResources(CallbackInfo ci) {
        if (Display.isCreated()) {
            Shaders.uninit();
        }
    }
}
