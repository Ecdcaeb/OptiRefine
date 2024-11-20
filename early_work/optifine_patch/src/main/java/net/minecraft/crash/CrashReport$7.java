/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.world.gen.layer.IntCache
 */
package net.minecraft.crash;

import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.world.gen.layer.IntCache;

class CrashReport.7
implements ICrashReportDetail<String> {
    CrashReport.7() {
    }

    public String call() throws Exception {
        return IntCache.getCacheSizes();
    }
}
