package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CommandPlaySound extends CommandBase {
   @Override
   public String getName() {
      return "playsound";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.playsound.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException(this.getUsage(☃));
      } else {
         int ☃ = 0;
         String ☃x = ☃[☃++];
         String ☃xx = ☃[☃++];
         SoundCategory ☃xxx = SoundCategory.getByName(☃xx);
         if (☃xxx == null) {
            throw new CommandException("commands.playsound.unknownSoundSource", ☃xx);
         } else {
            EntityPlayerMP ☃xxxx = getPlayer(☃, ☃, ☃[☃++]);
            Vec3d ☃xxxxx = ☃.getPositionVector();
            double ☃xxxxxx = ☃.length > ☃ ? parseDouble(☃xxxxx.x, ☃[☃++], true) : ☃xxxxx.x;
            double ☃xxxxxxx = ☃.length > ☃ ? parseDouble(☃xxxxx.y, ☃[☃++], 0, 0, false) : ☃xxxxx.y;
            double ☃xxxxxxxx = ☃.length > ☃ ? parseDouble(☃xxxxx.z, ☃[☃++], true) : ☃xxxxx.z;
            double ☃xxxxxxxxx = ☃.length > ☃ ? parseDouble(☃[☃++], 0.0, Float.MAX_VALUE) : 1.0;
            double ☃xxxxxxxxxx = ☃.length > ☃ ? parseDouble(☃[☃++], 0.0, 2.0) : 1.0;
            double ☃xxxxxxxxxxx = ☃.length > ☃ ? parseDouble(☃[☃], 0.0, 1.0) : 0.0;
            double ☃xxxxxxxxxxxx = ☃xxxxxxxxx > 1.0 ? ☃xxxxxxxxx * 16.0 : 16.0;
            double ☃xxxxxxxxxxxxx = ☃xxxx.getDistance(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
            if (☃xxxxxxxxxxxxx > ☃xxxxxxxxxxxx) {
               if (☃xxxxxxxxxxx <= 0.0) {
                  throw new CommandException("commands.playsound.playerTooFar", ☃xxxx.getName());
               }

               double ☃xxxxxxxxxxxxxx = ☃xxxxxx - ☃xxxx.posX;
               double ☃xxxxxxxxxxxxxxx = ☃xxxxxxx - ☃xxxx.posY;
               double ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxx - ☃xxxx.posZ;
               double ☃xxxxxxxxxxxxxxxxx = Math.sqrt(
                  ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx
               );
               if (☃xxxxxxxxxxxxxxxxx > 0.0) {
                  ☃xxxxxx = ☃xxxx.posX + ☃xxxxxxxxxxxxxx / ☃xxxxxxxxxxxxxxxxx * 2.0;
                  ☃xxxxxxx = ☃xxxx.posY + ☃xxxxxxxxxxxxxxx / ☃xxxxxxxxxxxxxxxxx * 2.0;
                  ☃xxxxxxxx = ☃xxxx.posZ + ☃xxxxxxxxxxxxxxxx / ☃xxxxxxxxxxxxxxxxx * 2.0;
               }

               ☃xxxxxxxxx = ☃xxxxxxxxxxx;
            }

            ☃xxxx.connection.sendPacket(new SPacketCustomSound(☃x, ☃xxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, (float)☃xxxxxxxxx, (float)☃xxxxxxxxxx));
            notifyCommandListener(☃, this, "commands.playsound.success", new Object[]{☃x, ☃xxxx.getName()});
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, SoundEvent.REGISTRY.getKeys());
      } else if (☃.length == 2) {
         return getListOfStringsMatchingLastWord(☃, SoundCategory.getSoundCategoryNames());
      } else if (☃.length == 3) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else {
         return ☃.length > 3 && ☃.length <= 6 ? getTabCompletionCoordinate(☃, 3, ☃) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 2;
   }
}
