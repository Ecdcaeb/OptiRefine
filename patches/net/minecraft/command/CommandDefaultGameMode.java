package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class CommandDefaultGameMode extends CommandGameMode {
   @Override
   public String getName() {
      return "defaultgamemode";
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.defaultgamemode.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length <= 0) {
         throw new WrongUsageException("commands.defaultgamemode.usage");
      } else {
         GameType ☃ = this.getGameModeFromCommand(☃, ☃[0]);
         this.setDefaultGameType(☃, ☃);
         notifyCommandListener(☃, this, "commands.defaultgamemode.success", new Object[]{new TextComponentTranslation("gameMode." + ☃.getName())});
      }
   }

   protected void setDefaultGameType(GameType var1, MinecraftServer var2) {
      ☃.setGameType(☃);
      if (☃.getForceGamemode()) {
         for (EntityPlayerMP ☃ : ☃.getPlayerList().getPlayers()) {
            ☃.setGameType(☃);
         }
      }
   }
}
