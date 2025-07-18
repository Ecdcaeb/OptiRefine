package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase {
   @Override
   public String getName() {
      return "time";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.time.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length > 1) {
         if ("set".equals(☃[0])) {
            int ☃;
            if ("day".equals(☃[1])) {
               ☃ = 1000;
            } else if ("night".equals(☃[1])) {
               ☃ = 13000;
            } else {
               ☃ = parseInt(☃[1], 0);
            }

            this.setAllWorldTimes(☃, ☃);
            notifyCommandListener(☃, this, "commands.time.set", new Object[]{☃});
            return;
         }

         if ("add".equals(☃[0])) {
            int ☃ = parseInt(☃[1], 0);
            this.incrementAllWorldTimes(☃, ☃);
            notifyCommandListener(☃, this, "commands.time.added", new Object[]{☃});
            return;
         }

         if ("query".equals(☃[0])) {
            if ("daytime".equals(☃[1])) {
               int ☃ = (int)(☃.getEntityWorld().getWorldTime() % 24000L);
               ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃);
               notifyCommandListener(☃, this, "commands.time.query", new Object[]{☃});
               return;
            }

            if ("day".equals(☃[1])) {
               int ☃ = (int)(☃.getEntityWorld().getWorldTime() / 24000L % 2147483647L);
               ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃);
               notifyCommandListener(☃, this, "commands.time.query", new Object[]{☃});
               return;
            }

            if ("gametime".equals(☃[1])) {
               int ☃ = (int)(☃.getEntityWorld().getTotalWorldTime() % 2147483647L);
               ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃);
               notifyCommandListener(☃, this, "commands.time.query", new Object[]{☃});
               return;
            }
         }
      }

      throw new WrongUsageException("commands.time.usage");
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"set", "add", "query"});
      } else if (☃.length == 2 && "set".equals(☃[0])) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"day", "night"});
      } else {
         return ☃.length == 2 && "query".equals(☃[0])
            ? getListOfStringsMatchingLastWord(☃, new String[]{"daytime", "gametime", "day"})
            : Collections.emptyList();
      }
   }

   protected void setAllWorldTimes(MinecraftServer var1, int var2) {
      for (int ☃ = 0; ☃ < ☃.worlds.length; ☃++) {
         ☃.worlds[☃].setWorldTime(☃);
      }
   }

   protected void incrementAllWorldTimes(MinecraftServer var1, int var2) {
      for (int ☃ = 0; ☃ < ☃.worlds.length; ☃++) {
         WorldServer ☃x = ☃.worlds[☃];
         ☃x.setWorldTime(☃x.getWorldTime() + ☃);
      }
   }
}
