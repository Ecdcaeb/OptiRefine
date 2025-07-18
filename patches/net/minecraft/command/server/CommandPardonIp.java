package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPardonIp extends CommandBase {
   @Override
   public String getName() {
      return "pardon-ip";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public boolean checkPermission(MinecraftServer var1, ICommandSender var2) {
      return ☃.getPlayerList().getBannedIPs().isLanServer() && super.checkPermission(☃, ☃);
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.unbanip.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length == 1 && ☃[0].length() > 1) {
         Matcher ☃ = CommandBanIp.IP_PATTERN.matcher(☃[0]);
         if (☃.matches()) {
            ☃.getPlayerList().getBannedIPs().removeEntry(☃[0]);
            notifyCommandListener(☃, this, "commands.unbanip.success", new Object[]{☃[0]});
         } else {
            throw new SyntaxErrorException("commands.unbanip.invalid");
         }
      } else {
         throw new WrongUsageException("commands.unbanip.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, ☃.getPlayerList().getBannedIPs().getKeys()) : Collections.emptyList();
   }
}
