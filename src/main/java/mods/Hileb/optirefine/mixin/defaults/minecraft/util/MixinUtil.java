package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * OF Util.runTask rethrows OutOfMemoryError nested in ExecutionException.
 * LoliASM {@code UtilMixin} {@code @Overwrite}s {@code runTask} (priority 1000) and
 * replaces {@code Logger.fatal} with {@code CrashUtils.notify}. Same-priority inject
 * then fails ({@code InvalidInjectionException}). Apply first (900) against vanilla;
 * LoliASM overwrite then replaces the method (crash GUI wins, no apply crash).
 */
@Mixin(value = net.minecraft.util.Util.class, priority = 900)
public abstract class MixinUtil {

    @WrapOperation(method = "runTask", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;Ljava/lang/Throwable;)V"))
    private static void optiRefine$logActualCause(Logger instance, String s, Throwable throwable, Operation<Void> original) {
        original.call(instance, s, throwable);
        if (throwable instanceof ExecutionException executionException && executionException.getCause() instanceof OutOfMemoryError outOfMemoryError) {
            throw outOfMemoryError;
        }
    }
}
