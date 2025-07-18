package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;

public class CommandPublishLocalServer extends CommandBase {
   @Override
   public String getName() {
      return "publish";
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.publish.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      String ☃ = ☃.shareToLAN(GameType.SURVIVAL, false);
      if (☃ != null) {
         notifyCommandListener(☃, this, "commands.publish.started", new Object[]{☃});
      } else {
         notifyCommandListener(☃, this, "commands.publish.failed", new Object[0]);
      }
   }
}
