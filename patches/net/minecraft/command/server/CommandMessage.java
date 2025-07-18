package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage extends CommandBase {
   @Override
   public List<String> getAliases() {
      return Arrays.asList("w", "msg");
   }

   @Override
   public String getName() {
      return "tell";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.message.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.message.usage");
      } else {
         EntityPlayer ☃ = getPlayer(☃, ☃, ☃[0]);
         if (☃ == ☃) {
            throw new PlayerNotFoundException("commands.message.sameTarget");
         } else {
            ITextComponent ☃x = getChatComponentFromNthArg(☃, ☃, 1, !(☃ instanceof EntityPlayer));
            TextComponentTranslation ☃xx = new TextComponentTranslation("commands.message.display.incoming", ☃.getDisplayName(), ☃x.createCopy());
            TextComponentTranslation ☃xxx = new TextComponentTranslation("commands.message.display.outgoing", ☃.getDisplayName(), ☃x.createCopy());
            ☃xx.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
            ☃xxx.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
            ☃.sendMessage(☃xx);
            ☃.sendMessage(☃xxx);
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
