package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetDefaultSpawnpoint extends CommandBase {
   @Override
   public String getName() {
      return "setworldspawn";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.setworldspawn.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      BlockPos ☃;
      if (☃.length == 0) {
         ☃ = getCommandSenderAsPlayer(☃).getPosition();
      } else {
         if (☃.length != 3 || ☃.getEntityWorld() == null) {
            throw new WrongUsageException("commands.setworldspawn.usage");
         }

         ☃ = parseBlockPos(☃, ☃, 0, true);
      }

      ☃.getEntityWorld().setSpawnPoint(☃);
      ☃.getPlayerList().sendPacketToAllPlayers(new SPacketSpawnPosition(☃));
      notifyCommandListener(☃, this, "commands.setworldspawn.success", new Object[]{☃.getX(), ☃.getY(), ☃.getZ()});
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length > 0 && ☃.length <= 3 ? getTabCompletionCoordinate(☃, 0, ☃) : Collections.emptyList();
   }
}
