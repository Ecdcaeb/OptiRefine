package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKill extends CommandBase {
   @Override
   public String getName() {
      return "kill";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.kill.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length == 0) {
         EntityPlayer ☃ = getCommandSenderAsPlayer(☃);
         ☃.onKillCommand();
         notifyCommandListener(☃, this, "commands.kill.successful", new Object[]{☃.getDisplayName()});
      } else {
         Entity ☃ = getEntity(☃, ☃, ☃[0]);
         ☃.onKillCommand();
         notifyCommandListener(☃, this, "commands.kill.successful", new Object[]{☃.getDisplayName()});
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }
}
