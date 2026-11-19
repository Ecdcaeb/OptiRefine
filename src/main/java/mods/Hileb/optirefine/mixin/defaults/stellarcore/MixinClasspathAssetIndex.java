package mods.Hileb.optirefine.mixin.defaults.stellarcore;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mods.Hileb.optirefine.optifine.OptifineHelper;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "github.kasuminova.stellarcore.client.resource.ClasspathAssetIndex", remap = false)
public abstract class MixinClasspathAssetIndex {
    @Dynamic("StellarCore's default resource pack existence cache")
    @ModifyReturnValue(method = "tryContains(Lnet/minecraft/util/ResourceLocation;)Ljava/lang/Boolean;",
            at = @At("RETURN"), remap = false)
    private static Boolean optiRefine$includeOptifineAssets(Boolean result, ResourceLocation location) {
        // G5 can reconstruct resources from patches the classpath index cannot see.
        if (Boolean.FALSE.equals(result) && location != null && OptifineHelper.hasOptifineResource(location)) {
            return Boolean.TRUE;
        }
        return result;
    }
}
