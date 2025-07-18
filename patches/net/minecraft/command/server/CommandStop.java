package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStop extends CommandBase {
   @Override
   public String getName() {
      return "stop";
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.stop.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.worlds != null) {
         notifyCommandListener(☃, this, "commands.stop.start", new Object[0]);
      }

      ☃.initiateShutdown();
   }
}
