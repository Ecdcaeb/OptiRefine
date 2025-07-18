package net.minecraft.command;

import io.netty.buffer.Unpooled;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class CommandStopSound extends CommandBase {
   @Override
   public String getName() {
      return "stopsound";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.stopsound.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length >= 1 && ☃.length <= 3) {
         int ☃ = 0;
         EntityPlayerMP ☃x = getPlayer(☃, ☃, ☃[☃++]);
         String ☃xx = "";
         String ☃xxx = "";
         if (☃.length >= 2) {
            String ☃xxxx = ☃[☃++];
            SoundCategory ☃xxxxx = SoundCategory.getByName(☃xxxx);
            if (☃xxxxx == null) {
               throw new CommandException("commands.stopsound.unknownSoundSource", ☃xxxx);
            }

            ☃xx = ☃xxxxx.getName();
         }

         if (☃.length == 3) {
            ☃xxx = ☃[☃++];
         }

         PacketBuffer ☃xxxx = new PacketBuffer(Unpooled.buffer());
         ☃xxxx.writeString(☃xx);
         ☃xxxx.writeString(☃xxx);
         ☃x.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", ☃xxxx));
         if (☃xx.isEmpty() && ☃xxx.isEmpty()) {
            notifyCommandListener(☃, this, "commands.stopsound.success.all", new Object[]{☃x.getName()});
         } else if (☃xxx.isEmpty()) {
            notifyCommandListener(☃, this, "commands.stopsound.success.soundSource", new Object[]{☃xx, ☃x.getName()});
         } else {
            notifyCommandListener(☃, this, "commands.stopsound.success.individualSound", new Object[]{☃xxx, ☃xx, ☃x.getName()});
         }
      } else {
         throw new WrongUsageException(this.getUsage(☃));
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else if (☃.length == 2) {
         return getListOfStringsMatchingLastWord(☃, SoundCategory.getSoundCategoryNames());
      } else {
         return ☃.length == 3 ? getListOfStringsMatchingLastWord(☃, SoundEvent.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
