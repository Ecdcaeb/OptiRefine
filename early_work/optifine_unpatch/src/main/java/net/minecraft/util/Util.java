/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.InterruptedException
 *  java.lang.Object
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
        String string = System.getProperty((String)"os.name").toLowerCase(Locale.ROOT);
        if (string.contains((CharSequence)"win")) {
            return EnumOS.WINDOWS;
        }
        if (string.contains((CharSequence)"mac")) {
            return EnumOS.OSX;
        }
        if (string.contains((CharSequence)"solaris")) {
            return EnumOS.SOLARIS;
        }
        if (string.contains((CharSequence)"sunos")) {
            return EnumOS.SOLARIS;
        }
        if (string.contains((CharSequence)"linux")) {
            return EnumOS.LINUX;
        }
        if (string.contains((CharSequence)"unix")) {
            return EnumOS.LINUX;
        }
        return EnumOS.UNKNOWN;
    }

    @Nullable
    public static <V> V runTask(FutureTask<V> futureTask, Logger logger) {
        try {
            futureTask.run();
            return (V)futureTask.get();
        }
        catch (ExecutionException executionException) {
            logger.fatal("Error executing task", (Throwable)executionException);
        }
        catch (InterruptedException interruptedException) {
            logger.fatal("Error executing task", (Throwable)interruptedException);
        }
        return null;
    }

    public static <T> T getLastElement(List<T> list) {
        return (T)list.get(list.size() - 1);
    }
}
