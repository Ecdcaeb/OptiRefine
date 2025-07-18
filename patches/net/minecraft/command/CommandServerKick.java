package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandServerKick extends CommandBase {
   @Override
   public String getName() {
      return "kick";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.kick.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length > 0 && ☃[0].length() > 1) {
         EntityPlayerMP ☃ = ☃.getPlayerList().getPlayerByUsername(☃[0]);
         if (☃ == null) {
            throw new PlayerNotFoundException("commands.generic.player.notFound", ☃[0]);
         } else {
            if (☃.length >= 2) {
               ITextComponent ☃x = getChatComponentFromNthArg(☃, ☃, 1);
               ☃.connection.disconnect(☃x);
               notifyCommandListener(☃, this, "commands.kick.success.reason", new Object[]{☃.getName(), ☃x.getUnformattedText()});
            } else {
               ☃.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.kicked"));
               notifyCommandListener(☃, this, "commands.kick.success", new Object[]{☃.getName()});
            }
         }
      } else {
         throw new WrongUsageException("commands.kick.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length >= 1 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }
}
