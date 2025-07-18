package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListBans extends CommandBase {
   @Override
   public String getName() {
      return "banlist";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public boolean checkPermission(MinecraftServer var1, ICommandSender var2) {
      return (☃.getPlayerList().getBannedIPs().isLanServer() || ☃.getPlayerList().getBannedPlayers().isLanServer()) && super.checkPermission(☃, ☃);
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.banlist.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length >= 1 && "ips".equalsIgnoreCase(☃[0])) {
         ☃.sendMessage(new TextComponentTranslation("commands.banlist.ips", ☃.getPlayerList().getBannedIPs().getKeys().length));
         ☃.sendMessage(new TextComponentString(joinNiceString(☃.getPlayerList().getBannedIPs().getKeys())));
      } else {
         ☃.sendMessage(new TextComponentTranslation("commands.banlist.players", ☃.getPlayerList().getBannedPlayers().getKeys().length));
         ☃.sendMessage(new TextComponentString(joinNiceString(☃.getPlayerList().getBannedPlayers().getKeys())));
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, new String[]{"players", "ips"}) : Collections.emptyList();
   }
}
