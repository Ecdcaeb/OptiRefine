package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException {
   private final CrashReport crashReport;

   public ReportedException(CrashReport var1) {
      this.crashReport = ☃;
   }

   public CrashReport getCrashReport() {
      return this.crashReport;
   }

   @Override
   public Throwable getCause() {
      return this.crashReport.getCrashCause();
   }

   @Override
   public String getMessage() {
      return this.crashReport.getDescription();
   }
}
