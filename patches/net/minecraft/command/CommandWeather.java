package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase {
   @Override
   public String getName() {
      return "weather";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.weather.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length >= 1 && ☃.length <= 2) {
         int ☃ = (300 + new Random().nextInt(600)) * 20;
         if (☃.length >= 2) {
            ☃ = parseInt(☃[1], 1, 1000000) * 20;
         }

         World ☃x = ☃.worlds[0];
         WorldInfo ☃xx = ☃x.getWorldInfo();
         if ("clear".equalsIgnoreCase(☃[0])) {
            ☃xx.setCleanWeatherTime(☃);
            ☃xx.setRainTime(0);
            ☃xx.setThunderTime(0);
            ☃xx.setRaining(false);
            ☃xx.setThundering(false);
            notifyCommandListener(☃, this, "commands.weather.clear", new Object[0]);
         } else if ("rain".equalsIgnoreCase(☃[0])) {
            ☃xx.setCleanWeatherTime(0);
            ☃xx.setRainTime(☃);
            ☃xx.setThunderTime(☃);
            ☃xx.setRaining(true);
            ☃xx.setThundering(false);
            notifyCommandListener(☃, this, "commands.weather.rain", new Object[0]);
         } else {
            if (!"thunder".equalsIgnoreCase(☃[0])) {
               throw new WrongUsageException("commands.weather.usage");
            }

            ☃xx.setCleanWeatherTime(0);
            ☃xx.setRainTime(☃);
            ☃xx.setThunderTime(☃);
            ☃xx.setRaining(true);
            ☃xx.setThundering(true);
            notifyCommandListener(☃, this, "commands.weather.thunder", new Object[0]);
         }
      } else {
         throw new WrongUsageException("commands.weather.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, new String[]{"clear", "rain", "thunder"}) : Collections.emptyList();
   }
}
