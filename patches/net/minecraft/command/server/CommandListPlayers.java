package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListPlayers extends CommandBase {
   @Override
   public String getName() {
      return "list";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.players.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      int ☃ = ☃.getCurrentPlayerCount();
      ☃.sendMessage(new TextComponentTranslation("commands.players.list", ☃, ☃.getMaxPlayers()));
      ☃.sendMessage(new TextComponentString(☃.getPlayerList().getFormattedListOfPlayers(☃.length > 0 && "uuids".equalsIgnoreCase(☃[0]))));
      ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃);
   }
}
