package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream extends PrintStream {
   protected static final Logger LOGGER = LogManager.getLogger();
   protected final String domain;

   public LoggingPrintStream(String var1, OutputStream var2) {
      super(☃);
      this.domain = ☃;
   }

   @Override
   public void println(String var1) {
      this.logString(☃);
   }

   @Override
   public void println(Object var1) {
      this.logString(String.valueOf(☃));
   }

   protected void logString(String var1) {
      LOGGER.info("[{}]: {}", this.domain, ☃);
   }
}
