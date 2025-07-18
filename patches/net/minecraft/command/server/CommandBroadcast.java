package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBroadcast extends CommandBase {
   @Override
   public String getName() {
      return "say";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 1;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.say.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length > 0 && ☃[0].length() > 0) {
         ITextComponent ☃ = getChatComponentFromNthArg(☃, ☃, 0, true);
         ☃.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.announcement", ☃.getDisplayName(), ☃));
      } else {
         throw new WrongUsageException("commands.say.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length >= 1 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }
}
