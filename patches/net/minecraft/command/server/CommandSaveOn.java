package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOn extends CommandBase {
   @Override
   public String getName() {
      return "save-on";
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.save-on.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      boolean ☃ = false;

      for (int ☃x = 0; ☃x < ☃.worlds.length; ☃x++) {
         if (☃.worlds[☃x] != null) {
            WorldServer ☃xx = ☃.worlds[☃x];
            if (☃xx.disableLevelSaving) {
               ☃xx.disableLevelSaving = false;
               ☃ = true;
            }
         }
      }

      if (☃) {
         notifyCommandListener(☃, this, "commands.save.enabled", new Object[0]);
      } else {
         throw new CommandException("commands.save-on.alreadyOn");
      }
   }
}
