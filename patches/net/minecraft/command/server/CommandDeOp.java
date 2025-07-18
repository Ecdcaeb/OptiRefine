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

public class CommandDeOp extends CommandBase {
   @Override
   public String getName() {
      return "deop";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.deop.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length == 1 && ☃[0].length() > 0) {
         GameProfile ☃ = ☃.getPlayerList().getOppedPlayers().getGameProfileFromName(☃[0]);
         if (☃ == null) {
            throw new CommandException("commands.deop.failed", ☃[0]);
         } else {
            ☃.getPlayerList().removeOp(☃);
            notifyCommandListener(☃, this, "commands.deop.success", new Object[]{☃[0]});
         }
      } else {
         throw new WrongUsageException("commands.deop.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, ☃.getPlayerList().getOppedPlayerNames()) : Collections.emptyList();
   }
}
