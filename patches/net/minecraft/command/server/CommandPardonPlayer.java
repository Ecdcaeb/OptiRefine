package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPardonPlayer extends CommandBase {
   @Override
   public String getName() {
      return "pardon";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.unban.usage";
   }

   @Override
   public boolean checkPermission(MinecraftServer var1, ICommandSender var2) {
      return ☃.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(☃, ☃);
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length == 1 && ☃[0].length() > 0) {
         GameProfile ☃ = ☃.getPlayerList().getBannedPlayers().getBannedProfile(☃[0]);
         if (☃ == null) {
            throw new CommandException("commands.unban.failed", ☃[0]);
         } else {
            ☃.getPlayerList().getBannedPlayers().removeEntry(☃);
            notifyCommandListener(☃, this, "commands.unban.success", new Object[]{☃[0]});
         }
      } else {
         throw new WrongUsageException("commands.unban.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, ☃.getPlayerList().getBannedPlayers().getKeys()) : Collections.emptyList();
   }
}
