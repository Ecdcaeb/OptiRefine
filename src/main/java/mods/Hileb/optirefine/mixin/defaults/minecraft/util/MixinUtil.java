package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

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
    public static <V> @org.jetbrains.annotations.Nullable V runTask(FutureTask<V> task, Logger logger) {
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
/*
--- net/minecraft/util/Util.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/util/Util.java	Tue Aug 19 14:59:58 2025
@@ -27,16 +27,20 @@

    @Nullable
    public static <V> V runTask(FutureTask<V> var0, Logger var1) {
       try {
          var0.run();
          return (V)var0.get();
-      } catch (ExecutionException var3) {
-         var1.fatal("Error executing task", var3);
-      } catch (InterruptedException var4) {
+      } catch (ExecutionException var4) {
          var1.fatal("Error executing task", var4);
+         if (var4.getCause() instanceof OutOfMemoryError) {
+            OutOfMemoryError var3 = (OutOfMemoryError)var4.getCause();
+            throw var3;
+         }
+      } catch (InterruptedException var5) {
+         var1.fatal("Error executing task", var5);
       }

       return null;
    }

    public static <T> T getLastElement(List<T> var0) {
 */
