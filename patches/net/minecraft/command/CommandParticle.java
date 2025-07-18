package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle extends CommandBase {
   @Override
   public String getName() {
      return "particle";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.particle.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 8) {
         throw new WrongUsageException("commands.particle.usage");
      } else {
         boolean ☃ = false;
         EnumParticleTypes ☃x = EnumParticleTypes.getByName(☃[0]);
         if (☃x == null) {
            throw new CommandException("commands.particle.notFound", ☃[0]);
         } else {
            String ☃xx = ☃[0];
            Vec3d ☃xxx = ☃.getPositionVector();
            double ☃xxxx = (float)parseDouble(☃xxx.x, ☃[1], true);
            double ☃xxxxx = (float)parseDouble(☃xxx.y, ☃[2], true);
            double ☃xxxxxx = (float)parseDouble(☃xxx.z, ☃[3], true);
            double ☃xxxxxxx = (float)parseDouble(☃[4]);
            double ☃xxxxxxxx = (float)parseDouble(☃[5]);
            double ☃xxxxxxxxx = (float)parseDouble(☃[6]);
            double ☃xxxxxxxxxx = (float)parseDouble(☃[7]);
            int ☃xxxxxxxxxxx = 0;
            if (☃.length > 8) {
               ☃xxxxxxxxxxx = parseInt(☃[8], 0);
            }

            boolean ☃xxxxxxxxxxxx = false;
            if (☃.length > 9 && "force".equals(☃[9])) {
               ☃xxxxxxxxxxxx = true;
            }

            EntityPlayerMP ☃xxxxxxxxxxxxx;
            if (☃.length > 10) {
               ☃xxxxxxxxxxxxx = getPlayer(☃, ☃, ☃[10]);
            } else {
               ☃xxxxxxxxxxxxx = null;
            }

            int[] ☃xxxxxxxxxxxxxx = new int[☃x.getArgumentCount()];

            for (int ☃xxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxx.length; ☃xxxxxxxxxxxxxxx++) {
               if (☃.length > 11 + ☃xxxxxxxxxxxxxxx) {
                  try {
                     ☃xxxxxxxxxxxxxx[☃xxxxxxxxxxxxxxx] = Integer.parseInt(☃[11 + ☃xxxxxxxxxxxxxxx]);
                  } catch (NumberFormatException var28) {
                     throw new CommandException("commands.particle.invalidParam", ☃[11 + ☃xxxxxxxxxxxxxxx]);
                  }
               }
            }

            World ☃xxxxxxxxxxxxxxxx = ☃.getEntityWorld();
            if (☃xxxxxxxxxxxxxxxx instanceof WorldServer) {
               WorldServer ☃xxxxxxxxxxxxxxxxx = (WorldServer)☃xxxxxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxxx == null) {
                  ☃xxxxxxxxxxxxxxxxx.spawnParticle(
                     ☃x, ☃xxxxxxxxxxxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx
                  );
               } else {
                  ☃xxxxxxxxxxxxxxxxx.spawnParticle(
                     ☃xxxxxxxxxxxxx, ☃x, ☃xxxxxxxxxxxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxx
                  );
               }

               notifyCommandListener(☃, this, "commands.particle.success", new Object[]{☃xx, Math.max(☃xxxxxxxxxxx, 1)});
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, EnumParticleTypes.getParticleNames());
      } else if (☃.length > 1 && ☃.length <= 4) {
         return getTabCompletionCoordinate(☃, 1, ☃);
      } else if (☃.length == 10) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"normal", "force"});
      } else {
         return ☃.length == 11 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 10;
   }
}
