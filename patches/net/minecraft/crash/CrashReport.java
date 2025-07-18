package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrashReport {
   private static final Logger LOGGER = LogManager.getLogger();
   private final String description;
   private final Throwable cause;
   private final CrashReportCategory systemDetailsCategory = new CrashReportCategory(this, "System Details");
   private final List<CrashReportCategory> crashReportSections = Lists.newArrayList();
   private File crashReportFile;
   private boolean firstCategoryInCrashReport = true;
   private StackTraceElement[] stacktrace = new StackTraceElement[0];

   public CrashReport(String var1, Throwable var2) {
      this.description = ☃;
      this.cause = ☃;
      this.populateEnvironment();
   }

   private void populateEnvironment() {
      this.systemDetailsCategory.addDetail("Minecraft Version", new ICrashReportDetail<String>() {
         public String call() {
            return "1.12.2";
         }
      });
      this.systemDetailsCategory.addDetail("Operating System", new ICrashReportDetail<String>() {
         public String call() {
            return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
         }
      });
      this.systemDetailsCategory.addDetail("Java Version", new ICrashReportDetail<String>() {
         public String call() {
            return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
         }
      });
      this.systemDetailsCategory.addDetail("Java VM Version", new ICrashReportDetail<String>() {
         public String call() {
            return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
         }
      });
      this.systemDetailsCategory.addDetail("Memory", new ICrashReportDetail<String>() {
         public String call() {
            Runtime ☃ = Runtime.getRuntime();
            long ☃x = ☃.maxMemory();
            long ☃xx = ☃.totalMemory();
            long ☃xxx = ☃.freeMemory();
            long ☃xxxx = ☃x / 1024L / 1024L;
            long ☃xxxxx = ☃xx / 1024L / 1024L;
            long ☃xxxxxx = ☃xxx / 1024L / 1024L;
            return ☃xxx + " bytes (" + ☃xxxxxx + " MB) / " + ☃xx + " bytes (" + ☃xxxxx + " MB) up to " + ☃x + " bytes (" + ☃xxxx + " MB)";
         }
      });
      this.systemDetailsCategory.addDetail("JVM Flags", new ICrashReportDetail<String>() {
         public String call() {
            RuntimeMXBean ☃ = ManagementFactory.getRuntimeMXBean();
            List<String> ☃x = ☃.getInputArguments();
            int ☃xx = 0;
            StringBuilder ☃xxx = new StringBuilder();

            for (String ☃xxxx : ☃x) {
               if (☃xxxx.startsWith("-X")) {
                  if (☃xx++ > 0) {
                     ☃xxx.append(" ");
                  }

                  ☃xxx.append(☃xxxx);
               }
            }

            return String.format("%d total; %s", ☃xx, ☃xxx.toString());
         }
      });
      this.systemDetailsCategory.addDetail("IntCache", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return IntCache.getCacheSizes();
         }
      });
   }

   public String getDescription() {
      return this.description;
   }

   public Throwable getCrashCause() {
      return this.cause;
   }

   public void getSectionsInStringBuilder(StringBuilder var1) {
      if ((this.stacktrace == null || this.stacktrace.length <= 0) && !this.crashReportSections.isEmpty()) {
         this.stacktrace = (StackTraceElement[])ArrayUtils.subarray(this.crashReportSections.get(0).getStackTrace(), 0, 1);
      }

      if (this.stacktrace != null && this.stacktrace.length > 0) {
         ☃.append("-- Head --\n");
         ☃.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
         ☃.append("Stacktrace:\n");

         for (StackTraceElement ☃ : this.stacktrace) {
            ☃.append("\t").append("at ").append(☃);
            ☃.append("\n");
         }

         ☃.append("\n");
      }

      for (CrashReportCategory ☃ : this.crashReportSections) {
         ☃.appendToStringBuilder(☃);
         ☃.append("\n\n");
      }

      this.systemDetailsCategory.appendToStringBuilder(☃);
   }

   public String getCauseStackTraceOrString() {
      StringWriter ☃ = null;
      PrintWriter ☃x = null;
      Throwable ☃xx = this.cause;
      if (☃xx.getMessage() == null) {
         if (☃xx instanceof NullPointerException) {
            ☃xx = new NullPointerException(this.description);
         } else if (☃xx instanceof StackOverflowError) {
            ☃xx = new StackOverflowError(this.description);
         } else if (☃xx instanceof OutOfMemoryError) {
            ☃xx = new OutOfMemoryError(this.description);
         }

         ☃xx.setStackTrace(this.cause.getStackTrace());
      }

      String ☃xxx = ☃xx.toString();

      try {
         ☃ = new StringWriter();
         ☃x = new PrintWriter(☃);
         ☃xx.printStackTrace(☃x);
         ☃xxx = ☃.toString();
      } finally {
         IOUtils.closeQuietly(☃);
         IOUtils.closeQuietly(☃x);
      }

      return ☃xxx;
   }

   public String getCompleteReport() {
      StringBuilder ☃ = new StringBuilder();
      ☃.append("---- Minecraft Crash Report ----\n");
      ☃.append("// ");
      ☃.append(getWittyComment());
      ☃.append("\n\n");
      ☃.append("Time: ");
      ☃.append(new SimpleDateFormat().format(new Date()));
      ☃.append("\n");
      ☃.append("Description: ");
      ☃.append(this.description);
      ☃.append("\n\n");
      ☃.append(this.getCauseStackTraceOrString());
      ☃.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

      for (int ☃x = 0; ☃x < 87; ☃x++) {
         ☃.append("-");
      }

      ☃.append("\n\n");
      this.getSectionsInStringBuilder(☃);
      return ☃.toString();
   }

   public File getFile() {
      return this.crashReportFile;
   }

   public boolean saveToFile(File var1) {
      if (this.crashReportFile != null) {
         return false;
      } else {
         if (☃.getParentFile() != null) {
            ☃.getParentFile().mkdirs();
         }

         Writer ☃ = null;

         boolean var4;
         try {
            ☃ = new OutputStreamWriter(new FileOutputStream(☃), StandardCharsets.UTF_8);
            ☃.write(this.getCompleteReport());
            this.crashReportFile = ☃;
            return true;
         } catch (Throwable var8) {
            LOGGER.error("Could not save crash report to {}", ☃, var8);
            var4 = false;
         } finally {
            IOUtils.closeQuietly(☃);
         }

         return var4;
      }
   }

   public CrashReportCategory getCategory() {
      return this.systemDetailsCategory;
   }

   public CrashReportCategory makeCategory(String var1) {
      return this.makeCategoryDepth(☃, 1);
   }

   public CrashReportCategory makeCategoryDepth(String var1, int var2) {
      CrashReportCategory ☃ = new CrashReportCategory(this, ☃);
      if (this.firstCategoryInCrashReport) {
         int ☃x = ☃.getPrunedStackTrace(☃);
         StackTraceElement[] ☃xx = this.cause.getStackTrace();
         StackTraceElement ☃xxx = null;
         StackTraceElement ☃xxxx = null;
         int ☃xxxxx = ☃xx.length - ☃x;
         if (☃xxxxx < 0) {
            System.out.println("Negative index in crash report handler (" + ☃xx.length + "/" + ☃x + ")");
         }

         if (☃xx != null && 0 <= ☃xxxxx && ☃xxxxx < ☃xx.length) {
            ☃xxx = ☃xx[☃xxxxx];
            if (☃xx.length + 1 - ☃x < ☃xx.length) {
               ☃xxxx = ☃xx[☃xx.length + 1 - ☃x];
            }
         }

         this.firstCategoryInCrashReport = ☃.firstTwoElementsOfStackTraceMatch(☃xxx, ☃xxxx);
         if (☃x > 0 && !this.crashReportSections.isEmpty()) {
            CrashReportCategory ☃xxxxxx = this.crashReportSections.get(this.crashReportSections.size() - 1);
            ☃xxxxxx.trimStackTraceEntriesFromBottom(☃x);
         } else if (☃xx != null && ☃xx.length >= ☃x && 0 <= ☃xxxxx && ☃xxxxx < ☃xx.length) {
            this.stacktrace = new StackTraceElement[☃xxxxx];
            System.arraycopy(☃xx, 0, this.stacktrace, 0, this.stacktrace.length);
         } else {
            this.firstCategoryInCrashReport = false;
         }
      }

      this.crashReportSections.add(☃);
      return ☃;
   }

   private static String getWittyComment() {
      String[] ☃ = new String[]{
         "Who set us up the TNT?",
         "Everything's going to plan. No, really, that was supposed to happen.",
         "Uh... Did I do that?",
         "Oops.",
         "Why did you do that?",
         "I feel sad now :(",
         "My bad.",
         "I'm sorry, Dave.",
         "I let you down. Sorry :(",
         "On the bright side, I bought you a teddy bear!",
         "Daisy, daisy...",
         "Oh - I know what I did wrong!",
         "Hey, that tickles! Hehehe!",
         "I blame Dinnerbone.",
         "You should try our sister game, Minceraft!",
         "Don't be sad. I'll do better next time, I promise!",
         "Don't be sad, have a hug! <3",
         "I just don't know what went wrong :(",
         "Shall we play a game?",
         "Quite honestly, I wouldn't worry myself about that.",
         "I bet Cylons wouldn't have this problem.",
         "Sorry :(",
         "Surprise! Haha. Well, this is awkward.",
         "Would you like a cupcake?",
         "Hi. I'm Minecraft, and I'm a crashaholic.",
         "Ooh. Shiny.",
         "This doesn't make any sense!",
         "Why is it breaking :(",
         "Don't do that.",
         "Ouch. That hurt :(",
         "You're mean.",
         "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]",
         "There are four lights!",
         "But it works on my machine."
      };

      try {
         return ☃[(int)(System.nanoTime() % ☃.length)];
      } catch (Throwable var2) {
         return "Witty comment unavailable :(";
      }
   }

   public static CrashReport makeCrashReport(Throwable var0, String var1) {
      CrashReport ☃;
      if (☃ instanceof ReportedException) {
         ☃ = ((ReportedException)☃).getCrashReport();
      } else {
         ☃ = new CrashReport(☃, ☃);
      }

      return ☃;
   }
}
