package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * OF Util.runTask rethrows OutOfMemoryError nested in ExecutionException.
 * LoliASM {@code UtilMixin} {@code @Overwrite}s {@code runTask} and removes
 * {@code Logger.fatal}, so wrapping that INVOKE always fails. {@code FutureTask.get()}
 * exists in both vanilla and the overwrite — wrap it after LoliASM (priority 1100).
 */
@Mixin(value = net.minecraft.util.Util.class, priority = 1100)
public abstract class MixinUtil {

    @WrapOperation(method = "runTask", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/FutureTask;get()Ljava/lang/Object;"))
    private static <V> V optiRefine$rethrowOom(FutureTask<V> task, Operation<V> original) {
        try {
            return original.call(task);
        } catch (Throwable t) {
            Throwable cause = t;
            while (cause != null) {
                if (cause instanceof OutOfMemoryError oom) {
                    throw oom;
                }
                Throwable next = cause.getCause();
                if (next == null || next == cause) {
                    break;
                }
                cause = next;
            }
            if (t instanceof RuntimeException re) {
                throw re;
            }
            if (t instanceof Error err) {
                throw err;
            }
            throw new RuntimeException(t);
        }
    }
}
