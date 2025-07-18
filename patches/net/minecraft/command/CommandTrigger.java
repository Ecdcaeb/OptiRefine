package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTrigger extends CommandBase {
   @Override
   public String getName() {
      return "trigger";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.trigger.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 3) {
         throw new WrongUsageException("commands.trigger.usage");
      } else {
         EntityPlayerMP ☃;
         if (☃ instanceof EntityPlayerMP) {
            ☃ = (EntityPlayerMP)☃;
         } else {
            Entity ☃x = ☃.getCommandSenderEntity();
            if (!(☃x instanceof EntityPlayerMP)) {
               throw new CommandException("commands.trigger.invalidPlayer");
            }

            ☃ = (EntityPlayerMP)☃x;
         }

         Scoreboard ☃x = ☃.getWorld(0).getScoreboard();
         ScoreObjective ☃xx = ☃x.getObjective(☃[0]);
         if (☃xx != null && ☃xx.getCriteria() == IScoreCriteria.TRIGGER) {
            int ☃xxx = parseInt(☃[2]);
            if (!☃x.entityHasObjective(☃.getName(), ☃xx)) {
               throw new CommandException("commands.trigger.invalidObjective", ☃[0]);
            } else {
               Score ☃xxxx = ☃x.getOrCreateScore(☃.getName(), ☃xx);
               if (☃xxxx.isLocked()) {
                  throw new CommandException("commands.trigger.disabled", ☃[0]);
               } else {
                  if ("set".equals(☃[1])) {
                     ☃xxxx.setScorePoints(☃xxx);
                  } else {
                     if (!"add".equals(☃[1])) {
                        throw new CommandException("commands.trigger.invalidMode", ☃[1]);
                     }

                     ☃xxxx.increaseScore(☃xxx);
                  }

                  ☃xxxx.setLocked(true);
                  if (☃.interactionManager.isCreative()) {
                     notifyCommandListener(☃, this, "commands.trigger.success", new Object[]{☃[0], ☃[1], ☃[2]});
                  }
               }
            }
         } else {
            throw new CommandException("commands.trigger.invalidObjective", ☃[0]);
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         Scoreboard ☃ = ☃.getWorld(0).getScoreboard();
         List<String> ☃x = Lists.newArrayList();

         for (ScoreObjective ☃xx : ☃.getScoreObjectives()) {
            if (☃xx.getCriteria() == IScoreCriteria.TRIGGER) {
               ☃x.add(☃xx.getName());
            }
         }

         return getListOfStringsMatchingLastWord(☃, ☃x.toArray(new String[☃x.size()]));
      } else {
         return ☃.length == 2 ? getListOfStringsMatchingLastWord(☃, new String[]{"add", "set"}) : Collections.emptyList();
      }
   }
}
