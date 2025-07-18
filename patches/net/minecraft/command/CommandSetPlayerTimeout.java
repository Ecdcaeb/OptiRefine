package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase {
   @Override
   public String getName() {
      return "setidletimeout";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.setidletimeout.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length != 1) {
         throw new WrongUsageException("commands.setidletimeout.usage");
      } else {
         int ☃ = parseInt(☃[0], 0);
         ☃.setPlayerIdleTimeout(☃);
         notifyCommandListener(☃, this, "commands.setidletimeout.success", new Object[]{☃});
      }
   }
}
