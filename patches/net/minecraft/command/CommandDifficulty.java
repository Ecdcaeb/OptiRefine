package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty extends CommandBase {
   @Override
   public String getName() {
      return "difficulty";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.difficulty.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length <= 0) {
         throw new WrongUsageException("commands.difficulty.usage");
      } else {
         EnumDifficulty ☃ = this.getDifficultyFromCommand(☃[0]);
         ☃.setDifficultyForAllWorlds(☃);
         notifyCommandListener(☃, this, "commands.difficulty.success", new Object[]{new TextComponentTranslation(☃.getTranslationKey())});
      }
   }

   protected EnumDifficulty getDifficultyFromCommand(String var1) throws NumberInvalidException {
      if ("peaceful".equalsIgnoreCase(☃) || "p".equalsIgnoreCase(☃)) {
         return EnumDifficulty.PEACEFUL;
      } else if ("easy".equalsIgnoreCase(☃) || "e".equalsIgnoreCase(☃)) {
         return EnumDifficulty.EASY;
      } else if ("normal".equalsIgnoreCase(☃) || "n".equalsIgnoreCase(☃)) {
         return EnumDifficulty.NORMAL;
      } else {
         return !"hard".equalsIgnoreCase(☃) && !"h".equalsIgnoreCase(☃) ? EnumDifficulty.byId(parseInt(☃, 0, 3)) : EnumDifficulty.HARD;
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, new String[]{"peaceful", "easy", "normal", "hard"}) : Collections.emptyList();
   }
}
