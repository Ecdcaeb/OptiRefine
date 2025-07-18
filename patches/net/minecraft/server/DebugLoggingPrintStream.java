package net.minecraft.server;

import java.io.OutputStream;
import net.minecraft.util.LoggingPrintStream;

public class DebugLoggingPrintStream extends LoggingPrintStream {
   public DebugLoggingPrintStream(String var1, OutputStream var2) {
      super(☃, ☃);
   }

   @Override
   protected void logString(String var1) {
      StackTraceElement[] ☃ = Thread.currentThread().getStackTrace();
      StackTraceElement ☃x = ☃[Math.min(3, ☃.length)];
      LOGGER.info("[{}]@.({}:{}): {}", this.domain, ☃x.getFileName(), ☃x.getLineNumber(), ☃);
   }
}
