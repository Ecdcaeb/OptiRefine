package net.minecraft.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandDebug extends CommandBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private long profileStartTime;
   private int profileStartTick;

   @Override
   public String getName() {
      return "debug";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.debug.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.debug.usage");
      } else {
         if ("start".equals(☃[0])) {
            if (☃.length != 1) {
               throw new WrongUsageException("commands.debug.usage");
            }

            notifyCommandListener(☃, this, "commands.debug.start", new Object[0]);
            ☃.enableProfiling();
            this.profileStartTime = MinecraftServer.getCurrentTimeMillis();
            this.profileStartTick = ☃.getTickCounter();
         } else {
            if (!"stop".equals(☃[0])) {
               throw new WrongUsageException("commands.debug.usage");
            }

            if (☃.length != 1) {
               throw new WrongUsageException("commands.debug.usage");
            }

            if (!☃.profiler.profilingEnabled) {
               throw new CommandException("commands.debug.notStarted");
            }

            long ☃ = MinecraftServer.getCurrentTimeMillis();
            int ☃x = ☃.getTickCounter();
            long ☃xx = ☃ - this.profileStartTime;
            int ☃xxx = ☃x - this.profileStartTick;
            this.saveProfilerResults(☃xx, ☃xxx, ☃);
            ☃.profiler.profilingEnabled = false;
            notifyCommandListener(☃, this, "commands.debug.stop", new Object[]{String.format("%.2f", (float)☃xx / 1000.0F), ☃xxx});
         }
      }
   }

   private void saveProfilerResults(long var1, int var3, MinecraftServer var4) {
      File ☃ = new File(☃.getFile("debug"), "profile-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
      ☃.getParentFile().mkdirs();
      Writer ☃x = null;

      try {
         ☃x = new OutputStreamWriter(new FileOutputStream(☃), StandardCharsets.UTF_8);
         ☃x.write(this.getProfilerResults(☃, ☃, ☃));
      } catch (Throwable var11) {
         LOGGER.error("Could not save profiler results to {}", ☃, var11);
      } finally {
         IOUtils.closeQuietly(☃x);
      }
   }

   private String getProfilerResults(long var1, int var3, MinecraftServer var4) {
      StringBuilder ☃ = new StringBuilder();
      ☃.append("---- Minecraft Profiler Results ----\n");
      ☃.append("// ");
      ☃.append(getWittyComment());
      ☃.append("\n\n");
      ☃.append("Time span: ").append(☃).append(" ms\n");
      ☃.append("Tick span: ").append(☃).append(" ticks\n");
      ☃.append("// This is approximately ")
         .append(String.format("%.2f", ☃ / ((float)☃ / 1000.0F)))
         .append(" ticks per second. It should be ")
         .append(20)
         .append(" ticks per second\n\n");
      ☃.append("--- BEGIN PROFILE DUMP ---\n\n");
      this.appendProfilerResults(0, "root", ☃, ☃);
      ☃.append("--- END PROFILE DUMP ---\n\n");
      return ☃.toString();
   }

   private void appendProfilerResults(int var1, String var2, StringBuilder var3, MinecraftServer var4) {
      List<Profiler.Result> ☃ = ☃.profiler.getProfilingData(☃);
      if (☃ != null && ☃.size() >= 3) {
         for (int ☃x = 1; ☃x < ☃.size(); ☃x++) {
            Profiler.Result ☃xx = ☃.get(☃x);
            ☃.append(String.format("[%02d] ", ☃));

            for (int ☃xxx = 0; ☃xxx < ☃; ☃xxx++) {
               ☃.append("|   ");
            }

            ☃.append(☃xx.profilerName)
               .append(" - ")
               .append(String.format("%.2f", ☃xx.usePercentage))
               .append("%/")
               .append(String.format("%.2f", ☃xx.totalUsePercentage))
               .append("%\n");
            if (!"unspecified".equals(☃xx.profilerName)) {
               try {
                  this.appendProfilerResults(☃ + 1, ☃ + "." + ☃xx.profilerName, ☃, ☃);
               } catch (Exception var9) {
                  ☃.append("[[ EXCEPTION ").append(var9).append(" ]]");
               }
            }
         }
      }
   }

   private static String getWittyComment() {
      String[] ☃ = new String[]{
         "Shiny numbers!",
         "Am I not running fast enough? :(",
         "I'm working as hard as I can!",
         "Will I ever be good enough for you? :(",
         "Speedy. Zoooooom!",
         "Hello world",
         "40% better than a crash report.",
         "Now with extra numbers",
         "Now with less numbers",
         "Now with the same numbers",
         "You should add flames to things, it makes them go faster!",
         "Do you feel the need for... optimization?",
         "*cracks redstone whip*",
         "Maybe if you treated it better then it'll have more motivation to work faster! Poor server."
      };

      try {
         return ☃[(int)(System.nanoTime() % ☃.length)];
      } catch (Throwable var2) {
         return "Witty comment unavailable :(";
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, new String[]{"start", "stop"}) : Collections.emptyList();
   }
}
