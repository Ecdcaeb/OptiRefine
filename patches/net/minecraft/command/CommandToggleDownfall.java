package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall extends CommandBase {
   @Override
   public String getName() {
      return "toggledownfall";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.downfall.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      this.toggleRainfall(☃);
      notifyCommandListener(☃, this, "commands.downfall.success", new Object[0]);
   }

   protected void toggleRainfall(MinecraftServer var1) {
      WorldInfo ☃ = ☃.worlds[0].getWorldInfo();
      ☃.setRaining(!☃.isRaining());
   }
}
