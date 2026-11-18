package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.resources.ForgeResourcePacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.GlVersion;
import net.optifine.reflect.Reflector;
import org.lwjgl.opengl.GL;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(targets = "Config", remap = false)
public abstract class MixinConfig {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1


    @Shadow(remap = false)
// [AUDIT-OK] Config.gameSettings static field exists in OF Config; remap=false correct (OF class, not in tsrg). needs-verification: runtime "Config" must be the OF jar real class (stub mods.Hileb.optirefine.optifine.Config declares neither gameSettings nor checkOpenGlCaps/getGlVersionLwjgl - if the stub were the target, this mixin would fail to apply)
    private static GameSettings gameSettings;

    @Inject(method = "checkInitialized", at = @At(value = "INVOKE", target = "LConfig;checkOpenGlCaps()V", remap = false), remap = false)
// [AUDIT-OK] injects into checkInitialized at the checkOpenGlCaps()V call (OF Config flow); remap=false OK. needs-verification: checkInitialized/checkOpenGlCaps not in patch trees (net.optifine absent)
    private static void optiRefine$initializeGameSettings(CallbackInfo ci){
        gameSettings = Minecraft.getMinecraft().gameSettings;
    }

    @WrapMethod(method = "getGlVersionLwjgl")
// [AUDIT-OK] wraps OF Config.getGlVersionLwjgl, adds OpenGL 4.6/4.5 detection via lwjglx GL.getCapabilities(). needs-verification: GL capabilities must be initialized before first call, else NPE
    private static GlVersion optiRefine$detectModernGlVersion(Operation<GlVersion> original){
        if (GL.getCapabilities().OpenGL46) {
            return new GlVersion(4, 6);
        } else if (GL.getCapabilities().OpenGL45) {
            return new GlVersion(4, 5);
        } else return original.call();
    }

    @ModifyReturnValue(method = "getResourcePacks", at = @At("RETURN"), remap = false)
    private static IResourcePack[] optiRefine$includeForgePacks(IResourcePack[] selected) {
        return ForgeResourcePacks.prependActivePacks(selected);
    }

    @Inject(method = "getDefiningResourcePack", at = @At(value = "INVOKE",
            target = "LConfig;getDefaultResourcePack()Lnet/minecraft/client/resources/DefaultResourcePack;", ordinal = 0,
            remap = false), cancellable = true, remap = false)
    private static void optiRefine$findForgePack(ResourceLocation location, CallbackInfoReturnable<IResourcePack> cir) {
        IResourcePack pack = ForgeResourcePacks.findDefiningPack(location);
        if (pack != null) {
            cir.setReturnValue(pack);
        }
    }
}
