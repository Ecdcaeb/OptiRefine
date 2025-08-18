package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.optifine.config.GlVersion;
import net.optifine.reflect.Reflector;
import org.lwjgl.opengl.GL;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "Config")
public abstract class MixinConfig {

    @Shadow(remap = false)
    private static GameSettings gameSettings;

    @Inject(method = "checkInitialized", at = @At(value = "INVOKE", target = "LConfig;checkOpenGlCaps()V", remap = false), remap = false)
    private static void ensureGamesettingSetup(CallbackInfo ci){
        gameSettings = Minecraft.getMinecraft().gameSettings;
    }

    @WrapMethod(method = "getGlVersionLwjgl")
    private static GlVersion getGlVersionLwjgl$Updated(Operation<GlVersion> original){
        if (GL.getCapabilities().OpenGL46) {
            return new GlVersion(4, 6);
        } else if (GL.getCapabilities().OpenGL45) {
            return new GlVersion(4, 5);
        } else return original.call();
    }
}
