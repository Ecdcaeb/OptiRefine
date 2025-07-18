package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase {
   @Override
   public String getName() {
      return "save-all";
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.save.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      ☃.sendMessage(new TextComponentTranslation("commands.save.start"));
      if (☃.getPlayerList() != null) {
         ☃.getPlayerList().saveAllPlayerData();
      }

      try {
         for (int ☃ = 0; ☃ < ☃.worlds.length; ☃++) {
            if (☃.worlds[☃] != null) {
               WorldServer ☃x = ☃.worlds[☃];
               boolean ☃xx = ☃x.disableLevelSaving;
               ☃x.disableLevelSaving = false;
               ☃x.saveAllChunks(true, null);
               ☃x.disableLevelSaving = ☃xx;
            }
         }

         if (☃.length > 0 && "flush".equals(☃[0])) {
            ☃.sendMessage(new TextComponentTranslation("commands.save.flushStart"));

            for (int ☃x = 0; ☃x < ☃.worlds.length; ☃x++) {
               if (☃.worlds[☃x] != null) {
                  WorldServer ☃xx = ☃.worlds[☃x];
                  boolean ☃xxx = ☃xx.disableLevelSaving;
                  ☃xx.disableLevelSaving = false;
                  ☃xx.flushToDisk();
                  ☃xx.disableLevelSaving = ☃xxx;
               }
            }

            ☃.sendMessage(new TextComponentTranslation("commands.save.flushEnd"));
         }
      } catch (MinecraftException var7) {
         notifyCommandListener(☃, this, "commands.save.failed", new Object[]{var7.getMessage()});
         return;
      }

      notifyCommandListener(☃, this, "commands.save.success", new Object[0]);
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, new String[]{"flush"}) : Collections.emptyList();
   }
}
