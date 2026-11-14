package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.optifine.EmissiveTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = EmissiveTextures.class, remap = false)
public abstract class MixinEmissiveTextures {
    @Shadow(remap = false)
    private static boolean render;

    @Unique
    @Public
    private static boolean optiRefine$isRendering() {
        return render;
    }
}
