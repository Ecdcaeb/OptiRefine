package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListIPBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanIp extends CommandBase {
   public static final Pattern IP_PATTERN = Pattern.compile(
      "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
   );

   @Override
   public String getName() {
      return "ban-ip";
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
      return "commands.banip.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length >= 1 && ☃[0].length() > 1) {
         ITextComponent ☃ = ☃.length >= 2 ? getChatComponentFromNthArg(☃, ☃, 1) : null;
         Matcher ☃x = IP_PATTERN.matcher(☃[0]);
         if (☃x.matches()) {
            this.banIp(☃, ☃, ☃[0], ☃ == null ? null : ☃.getUnformattedText());
         } else {
            EntityPlayerMP ☃xx = ☃.getPlayerList().getPlayerByUsername(☃[0]);
            if (☃xx == null) {
               throw new PlayerNotFoundException("commands.banip.invalid");
            }

            this.banIp(☃, ☃, ☃xx.getPlayerIP(), ☃ == null ? null : ☃.getUnformattedText());
         }
      } else {
         throw new WrongUsageException("commands.banip.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }

   protected void banIp(MinecraftServer var1, ICommandSender var2, String var3, @Nullable String var4) {
      UserListIPBansEntry ☃ = new UserListIPBansEntry(☃, null, ☃.getName(), null, ☃);
      ☃.getPlayerList().getBannedIPs().addEntry(☃);
      List<EntityPlayerMP> ☃x = ☃.getPlayerList().getPlayersMatchingAddress(☃);
      String[] ☃xx = new String[☃x.size()];
      int ☃xxx = 0;

      for (EntityPlayerMP ☃xxxx : ☃x) {
         ☃xxxx.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.ip_banned"));
         ☃xx[☃xxx++] = ☃xxxx.getName();
      }

      if (☃x.isEmpty()) {
         notifyCommandListener(☃, this, "commands.banip.success", new Object[]{☃});
      } else {
         notifyCommandListener(☃, this, "commands.banip.success.players", new Object[]{☃, joinNiceString(☃xx)});
      }
   }
}
