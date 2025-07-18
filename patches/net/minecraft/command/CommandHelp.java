package net.minecraft.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class CommandHelp extends CommandBase {
   private static final String[] SEARGE_SAYS = new String[]{
      "Yolo",
      "Ask for help on twitter",
      "/deop @p",
      "Scoreboard deleted, commands blocked",
      "Contact helpdesk for help",
      "/testfornoob @p",
      "/trigger warning",
      "Oh my god, it's full of stats",
      "/kill @p[name=!Searge]",
      "Have you tried turning it off and on again?",
      "Sorry, no help today"
   };
   private final Random rand = new Random();

   @Override
   public String getName() {
      return "help";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.help.usage";
   }

   @Override
   public List<String> getAliases() {
      return Arrays.asList("?");
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃ instanceof CommandBlockBaseLogic) {
         ☃.sendMessage(new TextComponentString("Searge says: ").appendText(SEARGE_SAYS[this.rand.nextInt(SEARGE_SAYS.length) % SEARGE_SAYS.length]));
      } else {
         List<ICommand> ☃ = this.getSortedPossibleCommands(☃, ☃);
         int ☃x = 7;
         int ☃xx = (☃.size() - 1) / 7;
         int ☃xxx = 0;

         try {
            ☃xxx = ☃.length == 0 ? 0 : parseInt(☃[0], 1, ☃xx + 1) - 1;
         } catch (NumberInvalidException var13) {
            Map<String, ICommand> ☃xxxx = this.getCommandMap(☃);
            ICommand ☃xxxxx = ☃xxxx.get(☃[0]);
            if (☃xxxxx != null) {
               throw new WrongUsageException(☃xxxxx.getUsage(☃));
            }

            if (MathHelper.getInt(☃[0], -1) == -1 && MathHelper.getInt(☃[0], -2) == -2) {
               throw new CommandNotFoundException();
            }

            throw var13;
         }

         int ☃xxxxxx = Math.min((☃xxx + 1) * 7, ☃.size());
         TextComponentTranslation ☃xxxxxxx = new TextComponentTranslation("commands.help.header", ☃xxx + 1, ☃xx + 1);
         ☃xxxxxxx.getStyle().setColor(TextFormatting.DARK_GREEN);
         ☃.sendMessage(☃xxxxxxx);

         for (int ☃xxxxxxxx = ☃xxx * 7; ☃xxxxxxxx < ☃xxxxxx; ☃xxxxxxxx++) {
            ICommand ☃xxxxxxxxx = ☃.get(☃xxxxxxxx);
            TextComponentTranslation ☃xxxxxxxxxx = new TextComponentTranslation(☃xxxxxxxxx.getUsage(☃));
            ☃xxxxxxxxxx.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + ☃xxxxxxxxx.getName() + " "));
            ☃.sendMessage(☃xxxxxxxxxx);
         }

         if (☃xxx == 0) {
            TextComponentTranslation ☃xxxxxxxx = new TextComponentTranslation("commands.help.footer");
            ☃xxxxxxxx.getStyle().setColor(TextFormatting.GREEN);
            ☃.sendMessage(☃xxxxxxxx);
         }
      }
   }

   protected List<ICommand> getSortedPossibleCommands(ICommandSender var1, MinecraftServer var2) {
      List<ICommand> ☃ = ☃.getCommandManager().getPossibleCommands(☃);
      Collections.sort(☃);
      return ☃;
   }

   protected Map<String, ICommand> getCommandMap(MinecraftServer var1) {
      return ☃.getCommandManager().getCommands();
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         Set<String> ☃ = this.getCommandMap(☃).keySet();
         return getListOfStringsMatchingLastWord(☃, ☃.toArray(new String[☃.size()]));
      } else {
         return Collections.emptyList();
      }
   }
}
