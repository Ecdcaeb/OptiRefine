package net.minecraft.command;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ICommand extends Comparable<ICommand> {
   String getName();

   String getUsage(ICommandSender var1);

   List<String> getAliases();

   void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException;

   boolean checkPermission(MinecraftServer var1, ICommandSender var2);

   List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4);

   boolean isUsernameIndex(String[] var1, int var2);
}
