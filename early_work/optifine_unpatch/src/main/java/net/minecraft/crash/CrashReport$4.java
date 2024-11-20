/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.ICrashReportDetail
 */
package net.minecraft.crash;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;

class CrashReport.4
implements ICrashReportDetail<String> {
    CrashReport.4(CrashReport this$0) {
    }

    public String call() {
        return System.getProperty((String)"java.version") + ", " + System.getProperty((String)"java.vendor");
    }
}
