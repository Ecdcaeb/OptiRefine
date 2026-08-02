package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.ExecutionException;
@Mixin(net.minecraft.util.Util.class)
public abstract class MixinUtil {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @WrapOperation(method = "runTask", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;Ljava/lang/Throwable;)V"))
// [AUDIT-OK] Logger.fatal INVOKE within Util.runTask matches baseline (both catch sites); OOM rethrow matches OF runTask; InterruptedException branch unaffected
    private static <V> void runTask$logActualCause(Logger instance, String s, Throwable throwable, Operation<Void> original) {
        original.call(instance, s ,throwable);
        if (throwable instanceof ExecutionException executionException && executionException.getCause() instanceof  OutOfMemoryError outOfMemoryError) {
            throw outOfMemoryError;
        }
    }
}
