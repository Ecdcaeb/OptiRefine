package mods.Hileb.optirefine.mixin.minecraft.util;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Mixin(net.minecraft.util.Util.class)
public abstract class MixinUtil {
    /**
     * @author Hileb
     * @reason output error
     */
    @Nullable
    @Overwrite
    public static <V> V runTask(FutureTask<V> task, Logger logger) {
        try {
            task.run();
            return task.get();
        }
        catch (ExecutionException executionexception) {
            logger.fatal("Error executing task", executionexception);
            if (executionexception.getCause() instanceof OutOfMemoryError cause) {
                throw cause;
            }
        }
        catch (InterruptedException interruptedexception) {
            logger.fatal("Error executing task", interruptedexception);
        }
        return null;
    }
}
