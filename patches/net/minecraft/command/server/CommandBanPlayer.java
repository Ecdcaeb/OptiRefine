package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanPlayer extends CommandBase {
   @Override
   public String getName() {
      return "ban";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.ban.usage";
   }

   @Override
   public boolean checkPermission(MinecraftServer var1, ICommandSender var2) {
      return ☃.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(☃, ☃);
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length >= 1 && ☃[0].length() > 0) {
         GameProfile ☃ = ☃.getPlayerProfileCache().getGameProfileForUsername(☃[0]);
         if (☃ == null) {
            throw new CommandException("commands.ban.failed", ☃[0]);
         } else {
            String ☃x = null;
            if (☃.length >= 2) {
               ☃x = getChatComponentFromNthArg(☃, ☃, 1).getUnformattedText();
            }

            UserListBansEntry ☃xx = new UserListBansEntry(☃, null, ☃.getName(), null, ☃x);
            ☃.getPlayerList().getBannedPlayers().addEntry(☃xx);
            EntityPlayerMP ☃xxx = ☃.getPlayerList().getPlayerByUsername(☃[0]);
            if (☃xxx != null) {
               ☃xxx.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.banned"));
            }

            notifyCommandListener(☃, this, "commands.ban.success", new Object[]{☃[0]});
         }
      } else {
         throw new WrongUsageException("commands.ban.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length >= 1 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }
}
