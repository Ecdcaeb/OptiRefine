package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandOp extends CommandBase {
   @Override
   public String getName() {
      return "op";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.op.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length == 1 && ☃[0].length() > 0) {
         GameProfile ☃ = ☃.getPlayerProfileCache().getGameProfileForUsername(☃[0]);
         if (☃ == null) {
            throw new CommandException("commands.op.failed", ☃[0]);
         } else {
            ☃.getPlayerList().addOp(☃);
            notifyCommandListener(☃, this, "commands.op.success", new Object[]{☃[0]});
         }
      } else {
         throw new WrongUsageException("commands.op.usage");
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         String ☃ = ☃[☃.length - 1];
         List<String> ☃x = Lists.newArrayList();

         for (GameProfile ☃xx : ☃.getOnlinePlayerProfiles()) {
            if (!☃.getPlayerList().canSendCommands(☃xx) && doesStringStartWith(☃, ☃xx.getName())) {
               ☃x.add(☃xx.getName());
            }
         }

         return ☃x;
      } else {
         return Collections.emptyList();
      }
   }
}
