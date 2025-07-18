package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;

public class CommandGameMode extends CommandBase {
   @Override
   public String getName() {
      return "gamemode";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.gamemode.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length <= 0) {
         throw new WrongUsageException("commands.gamemode.usage");
      } else {
         GameType ☃ = this.getGameModeFromCommand(☃, ☃[0]);
         EntityPlayer ☃x = ☃.length >= 2 ? getPlayer(☃, ☃, ☃[1]) : getCommandSenderAsPlayer(☃);
         ☃x.setGameType(☃);
         ITextComponent ☃xx = new TextComponentTranslation("gameMode." + ☃.getName());
         if (☃.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            ☃x.sendMessage(new TextComponentTranslation("gameMode.changed", ☃xx));
         }

         if (☃x == ☃) {
            notifyCommandListener(☃, this, 1, "commands.gamemode.success.self", new Object[]{☃xx});
         } else {
            notifyCommandListener(☃, this, 1, "commands.gamemode.success.other", new Object[]{☃x.getName(), ☃xx});
         }
      }
   }

   protected GameType getGameModeFromCommand(ICommandSender var1, String var2) throws NumberInvalidException {
      GameType ☃ = GameType.parseGameTypeWithDefault(☃, GameType.NOT_SET);
      return ☃ == GameType.NOT_SET ? WorldSettings.getGameTypeById(parseInt(☃, 0, GameType.values().length - 2)) : ☃;
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"survival", "creative", "adventure", "spectator"});
      } else {
         return ☃.length == 2 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 1;
   }
}
