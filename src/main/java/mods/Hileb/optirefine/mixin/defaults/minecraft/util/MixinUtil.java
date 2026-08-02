package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.ExecutionException;
@Mixin(net.minecraft.util.Util.class)
public abstract class MixinUtil {

    @WrapOperation(method = "runTask", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;Ljava/lang/Throwable;)V"))
    private static <V> void runTask$logActualCause(Logger instance, String s, Throwable throwable, Operation<Void> original) {
        original.call(instance, s ,throwable);
        if (throwable instanceof ExecutionException executionException && executionException.getCause() instanceof  OutOfMemoryError outOfMemoryError) {
            throw outOfMemoryError;
        }
    }
}
