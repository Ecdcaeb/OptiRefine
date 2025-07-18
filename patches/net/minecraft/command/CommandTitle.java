package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle extends CommandBase {
   private static final Logger LOGGER = LogManager.getLogger();

   @Override
   public String getName() {
      return "title";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.title.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.title.usage");
      } else {
         if (☃.length < 3) {
            if ("title".equals(☃[1]) || "subtitle".equals(☃[1]) || "actionbar".equals(☃[1])) {
               throw new WrongUsageException("commands.title.usage.title");
            }

            if ("times".equals(☃[1])) {
               throw new WrongUsageException("commands.title.usage.times");
            }
         }

         EntityPlayerMP ☃ = getPlayer(☃, ☃, ☃[0]);
         SPacketTitle.Type ☃x = SPacketTitle.Type.byName(☃[1]);
         if (☃x != SPacketTitle.Type.CLEAR && ☃x != SPacketTitle.Type.RESET) {
            if (☃x == SPacketTitle.Type.TIMES) {
               if (☃.length != 5) {
                  throw new WrongUsageException("commands.title.usage");
               } else {
                  int ☃xx = parseInt(☃[2]);
                  int ☃xxx = parseInt(☃[3]);
                  int ☃xxxx = parseInt(☃[4]);
                  SPacketTitle ☃xxxxx = new SPacketTitle(☃xx, ☃xxx, ☃xxxx);
                  ☃.connection.sendPacket(☃xxxxx);
                  notifyCommandListener(☃, this, "commands.title.success", new Object[0]);
               }
            } else if (☃.length < 3) {
               throw new WrongUsageException("commands.title.usage");
            } else {
               String ☃xx = buildString(☃, 2);

               ITextComponent ☃xxx;
               try {
                  ☃xxx = ITextComponent.Serializer.jsonToComponent(☃xx);
               } catch (JsonParseException var10) {
                  throw toSyntaxException(var10);
               }

               SPacketTitle ☃xxxx = new SPacketTitle(☃x, TextComponentUtils.processComponent(☃, ☃xxx, ☃));
               ☃.connection.sendPacket(☃xxxx);
               notifyCommandListener(☃, this, "commands.title.success", new Object[0]);
            }
         } else if (☃.length != 2) {
            throw new WrongUsageException("commands.title.usage");
         } else {
            SPacketTitle ☃xx = new SPacketTitle(☃x, null);
            ☃.connection.sendPacket(☃xx);
            notifyCommandListener(☃, this, "commands.title.success", new Object[0]);
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else {
         return ☃.length == 2 ? getListOfStringsMatchingLastWord(☃, SPacketTitle.Type.getNames()) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
