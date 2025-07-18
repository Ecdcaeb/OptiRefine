package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CommandExecuteAt extends CommandBase {
   @Override
   public String getName() {
      return "execute";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.execute.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 5) {
         throw new WrongUsageException("commands.execute.usage");
      } else {
         Entity ☃ = getEntity(☃, ☃, ☃[0], Entity.class);
         double ☃x = parseDouble(☃.posX, ☃[1], false);
         double ☃xx = parseDouble(☃.posY, ☃[2], false);
         double ☃xxx = parseDouble(☃.posZ, ☃[3], false);
         new BlockPos(☃x, ☃xx, ☃xxx);
         int ☃xxxx = 4;
         if ("detect".equals(☃[4]) && ☃.length > 10) {
            World ☃xxxxx = ☃.getEntityWorld();
            double ☃xxxxxx = parseDouble(☃x, ☃[5], false);
            double ☃xxxxxxx = parseDouble(☃xx, ☃[6], false);
            double ☃xxxxxxxx = parseDouble(☃xxx, ☃[7], false);
            Block ☃xxxxxxxxx = getBlockByText(☃, ☃[8]);
            BlockPos ☃xxxxxxxxxx = new BlockPos(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
            if (!☃xxxxx.isBlockLoaded(☃xxxxxxxxxx)) {
               throw new CommandException("commands.execute.failed", "detect", ☃.getName());
            }

            IBlockState ☃xxxxxxxxxxx = ☃xxxxx.getBlockState(☃xxxxxxxxxx);
            if (☃xxxxxxxxxxx.getBlock() != ☃xxxxxxxxx) {
               throw new CommandException("commands.execute.failed", "detect", ☃.getName());
            }

            if (!CommandBase.convertArgToBlockStatePredicate(☃xxxxxxxxx, ☃[9]).apply(☃xxxxxxxxxxx)) {
               throw new CommandException("commands.execute.failed", "detect", ☃.getName());
            }

            ☃xxxx = 10;
         }

         String ☃xxxxxxxxxxxx = buildString(☃, ☃xxxx);
         ICommandSender ☃xxxxxxxxxxxxx = CommandSenderWrapper.create(☃)
            .withEntity(☃, new Vec3d(☃x, ☃xx, ☃xxx))
            .withSendCommandFeedback(☃.worlds[0].getGameRules().getBoolean("commandBlockOutput"));
         ICommandManager ☃xxxxxxxxxxxxxx = ☃.getCommandManager();

         try {
            int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.executeCommand(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxx);
            if (☃xxxxxxxxxxxxxxx < 1) {
               throw new CommandException("commands.execute.allInvocationsFailed", ☃xxxxxxxxxxxx);
            }
         } catch (Throwable var23) {
            throw new CommandException("commands.execute.failed", ☃xxxxxxxxxxxx, ☃.getName());
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else if (☃.length > 1 && ☃.length <= 4) {
         return getTabCompletionCoordinate(☃, 1, ☃);
      } else if (☃.length > 5 && ☃.length <= 8 && "detect".equals(☃[4])) {
         return getTabCompletionCoordinate(☃, 5, ☃);
      } else {
         return ☃.length == 9 && "detect".equals(☃[4]) ? getListOfStringsMatchingLastWord(☃, Block.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
