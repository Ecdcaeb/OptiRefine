package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.InputStream;

@Mixin(value = DefaultResourcePack.class, priority = 1100)
public abstract class MixinDefaultResourcePackProbe {
    // VintageFix's resourceExists overwrite still probes the OptiFine stream.
    @WrapOperation(method = "resourceExists", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/DefaultResourcePack;getResourceStream(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;"))
    private InputStream optiRefine$closeResourceProbe(DefaultResourcePack pack, ResourceLocation location, Operation<InputStream> original) {
        InputStream input = original.call(pack, location);
        // resourceExists only tests this result for null; it never reads the stream.
        IOUtils.closeQuietly(input);
        return input;
    }
}
