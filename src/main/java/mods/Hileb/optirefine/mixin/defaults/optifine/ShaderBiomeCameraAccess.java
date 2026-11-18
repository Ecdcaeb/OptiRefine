package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Shaders.class, remap = false)
public interface ShaderBiomeCameraAccess {
    @Accessor("cameraPositionX")
    static double optiRefine$getCameraX() {
        throw new AssertionError();
    }

    @Accessor("cameraPositionY")
    static double optiRefine$getCameraY() {
        throw new AssertionError();
    }

    @Accessor("cameraPositionZ")
    static double optiRefine$getCameraZ() {
        throw new AssertionError();
    }

    @Accessor("cameraOffsetX")
    static int optiRefine$getCameraOffsetX() {
        throw new AssertionError();
    }

    @Accessor("cameraOffsetZ")
    static int optiRefine$getCameraOffsetZ() {
        throw new AssertionError();
    }
}
