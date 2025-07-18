package net.minecraft.command.server;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEmote extends CommandBase {
   @Override
   public String getName() {
      return "me";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.me.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length <= 0) {
         throw new WrongUsageException("commands.me.usage");
      } else {
         ITextComponent ☃ = getChatComponentFromNthArg(☃, ☃, 0, !(☃ instanceof EntityPlayer));
         ☃.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.emote", ☃.getDisplayName(), ☃));
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
   }
}
