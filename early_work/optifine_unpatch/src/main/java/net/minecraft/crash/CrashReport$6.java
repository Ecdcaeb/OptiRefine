/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Runtime
 *  java.lang.String
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.ICrashReportDetail
 */
package net.minecraft.crash;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;

class CrashReport.6
implements ICrashReportDetail<String> {
    CrashReport.6(CrashReport this$0) {
    }

    public String call() {
        Runtime runtime = Runtime.getRuntime();
        long i = runtime.maxMemory();
        long j = runtime.totalMemory();
        long k = runtime.freeMemory();
        long l = i / 1024L / 1024L;
        long i1 = j / 1024L / 1024L;
        long j1 = k / 1024L / 1024L;
        return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
    }
}
