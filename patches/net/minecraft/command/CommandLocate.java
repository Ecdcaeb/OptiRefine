package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandLocate extends CommandBase {
   @Override
   public String getName() {
      return "locate";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.locate.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length != 1) {
         throw new WrongUsageException("commands.locate.usage");
      } else {
         String ☃ = ☃[0];
         BlockPos ☃x = ☃.getEntityWorld().findNearestStructure(☃, ☃.getPosition(), false);
         if (☃x != null) {
            ☃.sendMessage(new TextComponentTranslation("commands.locate.success", ☃, ☃x.getX(), ☃x.getZ()));
         } else {
            throw new CommandException("commands.locate.failure", ☃);
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1
         ? getListOfStringsMatchingLastWord(☃, new String[]{"Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple", "Mineshaft"})
         : Collections.emptyList();
   }
}
