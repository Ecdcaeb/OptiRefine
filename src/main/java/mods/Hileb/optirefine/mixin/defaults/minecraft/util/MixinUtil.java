package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.ExecutionException;

@Checked
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
