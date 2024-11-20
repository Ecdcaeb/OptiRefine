/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.OutOfMemoryError
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.List
 *  java.util.Locale
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.FutureTask
 *  javax.annotation.Nullable
 *  net.minecraft.util.Util$EnumOS
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.util;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Logger;

public class Util {
    public static EnumOS getOSType() {
        String s = System.getProperty((String)"os.name").toLowerCase(Locale.ROOT);
        if (s.contains((CharSequence)"win")) {
            return EnumOS.WINDOWS;
        }
        if (s.contains((CharSequence)"mac")) {
            return EnumOS.OSX;
        }
        if (s.contains((CharSequence)"solaris")) {
            return EnumOS.SOLARIS;
        }
        if (s.contains((CharSequence)"sunos")) {
            return EnumOS.SOLARIS;
        }
        if (s.contains((CharSequence)"linux")) {
            return EnumOS.LINUX;
        }
        return s.contains((CharSequence)"unix") ? EnumOS.LINUX : EnumOS.UNKNOWN;
    }

    @Nullable
    public static <V> V runTask(FutureTask<V> task, Logger logger) {
        try {
            task.run();
            return (V)task.get();
        }
        catch (ExecutionException executionexception) {
            logger.fatal("Error executing task", (Throwable)executionexception);
            if (executionexception.getCause() instanceof OutOfMemoryError) {
                OutOfMemoryError oome = (OutOfMemoryError)executionexception.getCause();
                throw oome;
            }
        }
        catch (InterruptedException interruptedexception) {
            logger.fatal("Error executing task", (Throwable)interruptedexception);
        }
        return null;
    }

    public static <T> T getLastElement(List<T> list) {
        return (T)list.get(list.size() - 1);
    }
}
