package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandXP extends CommandBase {
   @Override
   public String getName() {
      return "xp";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.xp.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length <= 0) {
         throw new WrongUsageException("commands.xp.usage");
      } else {
         String ☃ = ☃[0];
         boolean ☃x = ☃.endsWith("l") || ☃.endsWith("L");
         if (☃x && ☃.length() > 1) {
            ☃ = ☃.substring(0, ☃.length() - 1);
         }

         int ☃xx = parseInt(☃);
         boolean ☃xxx = ☃xx < 0;
         if (☃xxx) {
            ☃xx *= -1;
         }

         EntityPlayer ☃xxxx = ☃.length > 1 ? getPlayer(☃, ☃, ☃[1]) : getCommandSenderAsPlayer(☃);
         if (☃x) {
            ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃xxxx.experienceLevel);
            if (☃xxx) {
               ☃xxxx.addExperienceLevel(-☃xx);
               notifyCommandListener(☃, this, "commands.xp.success.negative.levels", new Object[]{☃xx, ☃xxxx.getName()});
            } else {
               ☃xxxx.addExperienceLevel(☃xx);
               notifyCommandListener(☃, this, "commands.xp.success.levels", new Object[]{☃xx, ☃xxxx.getName()});
            }
         } else {
            ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃xxxx.experienceTotal);
            if (☃xxx) {
               throw new CommandException("commands.xp.failure.widthdrawXp");
            }

            ☃xxxx.addExperience(☃xx);
            notifyCommandListener(☃, this, "commands.xp.success", new Object[]{☃xx, ☃xxxx.getName()});
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 2 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 1;
   }
}
