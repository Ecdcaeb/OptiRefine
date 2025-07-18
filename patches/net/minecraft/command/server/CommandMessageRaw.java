package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;

public class CommandMessageRaw extends CommandBase {
   @Override
   public String getName() {
      return "tellraw";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.tellraw.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.tellraw.usage");
      } else {
         EntityPlayer ☃ = getPlayer(☃, ☃, ☃[0]);
         String ☃x = buildString(☃, 1);

         try {
            ITextComponent ☃xx = ITextComponent.Serializer.jsonToComponent(☃x);
            ☃.sendMessage(TextComponentUtils.processComponent(☃, ☃xx, ☃));
         } catch (JsonParseException var7) {
            throw toSyntaxException(var7);
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
