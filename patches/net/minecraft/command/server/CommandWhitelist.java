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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandWhitelist extends CommandBase {
   @Override
   public String getName() {
      return "whitelist";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.whitelist.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.whitelist.usage");
      } else {
         if ("on".equals(☃[0])) {
            ☃.getPlayerList().setWhiteListEnabled(true);
            notifyCommandListener(☃, this, "commands.whitelist.enabled", new Object[0]);
         } else if ("off".equals(☃[0])) {
            ☃.getPlayerList().setWhiteListEnabled(false);
            notifyCommandListener(☃, this, "commands.whitelist.disabled", new Object[0]);
         } else if ("list".equals(☃[0])) {
            ☃.sendMessage(
               new TextComponentTranslation(
                  "commands.whitelist.list", ☃.getPlayerList().getWhitelistedPlayerNames().length, ☃.getPlayerList().getAvailablePlayerDat().length
               )
            );
            String[] ☃ = ☃.getPlayerList().getWhitelistedPlayerNames();
            ☃.sendMessage(new TextComponentString(joinNiceString(☃)));
         } else if ("add".equals(☃[0])) {
            if (☃.length < 2) {
               throw new WrongUsageException("commands.whitelist.add.usage");
            }

            GameProfile ☃ = ☃.getPlayerProfileCache().getGameProfileForUsername(☃[1]);
            if (☃ == null) {
               throw new CommandException("commands.whitelist.add.failed", ☃[1]);
            }

            ☃.getPlayerList().addWhitelistedPlayer(☃);
            notifyCommandListener(☃, this, "commands.whitelist.add.success", new Object[]{☃[1]});
         } else if ("remove".equals(☃[0])) {
            if (☃.length < 2) {
               throw new WrongUsageException("commands.whitelist.remove.usage");
            }

            GameProfile ☃ = ☃.getPlayerList().getWhitelistedPlayers().getByName(☃[1]);
            if (☃ == null) {
               throw new CommandException("commands.whitelist.remove.failed", ☃[1]);
            }

            ☃.getPlayerList().removePlayerFromWhitelist(☃);
            notifyCommandListener(☃, this, "commands.whitelist.remove.success", new Object[]{☃[1]});
         } else if ("reload".equals(☃[0])) {
            ☃.getPlayerList().reloadWhitelist();
            notifyCommandListener(☃, this, "commands.whitelist.reloaded", new Object[0]);
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"on", "off", "list", "add", "remove", "reload"});
      } else {
         if (☃.length == 2) {
            if ("remove".equals(☃[0])) {
               return getListOfStringsMatchingLastWord(☃, ☃.getPlayerList().getWhitelistedPlayerNames());
            }

            if ("add".equals(☃[0])) {
               return getListOfStringsMatchingLastWord(☃, ☃.getPlayerProfileCache().getUsernames());
            }
         }

         return Collections.emptyList();
      }
   }
}
