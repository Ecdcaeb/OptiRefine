package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.resources.ModernResourcePaths;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;

/** Resolve aliases inside each pack so Minecraft's existing pack priority remains authoritative. */
@Mixin(AbstractResourcePack.class)
public abstract class MixinModernResourcePack {
    @Shadow
    protected abstract InputStream getInputStreamByName(String name) throws IOException;

    @Shadow
    protected abstract boolean hasResourceName(String name);

    @Inject(method = "getInputStream", at = @At("HEAD"), cancellable = true)
    private void optiRefine$modernInputStream(ResourceLocation location, CallbackInfoReturnable<InputStream> cir) throws IOException {
        ModernResourcePaths.PackPaths paths = ModernResourcePaths.forLocation(location);
        if (paths != null) {
            if (hasResourceName(paths.modern())) {
                cir.setReturnValue(getInputStreamByName(paths.modern()));
            } else if (hasResourceName(paths.legacy())) {
                cir.setReturnValue(getInputStreamByName(paths.legacy()));
            } else {
                // Keep the source pack's normal missing-resource exception and requested filename.
                cir.setReturnValue(getInputStreamByName(paths.requested()));
            }
        }
    }

    @Inject(method = "resourceExists", at = @At("HEAD"), cancellable = true)
    private void optiRefine$modernResourceExists(ResourceLocation location, CallbackInfoReturnable<Boolean> cir) {
        ModernResourcePaths.PackPaths paths = ModernResourcePaths.forLocation(location);
        if (paths != null) {
            cir.setReturnValue(hasResourceName(paths.modern()) || hasResourceName(paths.legacy()));
        }
    }
}
