package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandReload extends CommandBase {
   @Override
   public String getName() {
      return "reload";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.reload.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length > 0) {
         throw new WrongUsageException("commands.reload.usage");
      } else {
         ☃.reload();
         notifyCommandListener(☃, this, "commands.reload.success", new Object[0]);
      }
   }
}
