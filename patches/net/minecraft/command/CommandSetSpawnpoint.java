package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetSpawnpoint extends CommandBase {
   @Override
   public String getName() {
      return "spawnpoint";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.spawnpoint.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length > 1 && ☃.length < 4) {
         throw new WrongUsageException("commands.spawnpoint.usage");
      } else {
         EntityPlayerMP ☃ = ☃.length > 0 ? getPlayer(☃, ☃, ☃[0]) : getCommandSenderAsPlayer(☃);
         BlockPos ☃x = ☃.length > 3 ? parseBlockPos(☃, ☃, 1, true) : ☃.getPosition();
         if (☃.world != null) {
            ☃.setSpawnPoint(☃x, true);
            notifyCommandListener(☃, this, "commands.spawnpoint.success", new Object[]{☃.getName(), ☃x.getX(), ☃x.getY(), ☃x.getZ()});
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else {
         return ☃.length > 1 && ☃.length <= 4 ? getTabCompletionCoordinate(☃, 1, ☃) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
