package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CommandFunction extends CommandBase {
   @Override
   public String getName() {
      return "function";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.function.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length != 1 && ☃.length != 3) {
         throw new WrongUsageException("commands.function.usage");
      } else {
         ResourceLocation ☃ = new ResourceLocation(☃[0]);
         FunctionObject ☃x = ☃.getFunctionManager().getFunction(☃);
         if (☃x == null) {
            throw new CommandException("commands.function.unknown", ☃);
         } else {
            if (☃.length == 3) {
               String ☃xx = ☃[1];
               boolean ☃xxx;
               if ("if".equals(☃xx)) {
                  ☃xxx = true;
               } else {
                  if (!"unless".equals(☃xx)) {
                     throw new WrongUsageException("commands.function.usage");
                  }

                  ☃xxx = false;
               }

               boolean ☃xxxx = false;

               try {
                  ☃xxxx = !getEntityList(☃, ☃, ☃[2]).isEmpty();
               } catch (EntityNotFoundException var10) {
               }

               if (☃xxx != ☃xxxx) {
                  throw new CommandException("commands.function.skipped", ☃);
               }
            }

            int ☃xxxx = ☃.getFunctionManager()
               .execute(☃x, CommandSenderWrapper.create(☃).computePositionVector().withPermissionLevel(2).withSendCommandFeedback(false));
            notifyCommandListener(☃, this, "commands.function.success", new Object[]{☃, ☃xxxx});
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getFunctionManager().getFunctions().keySet());
      } else if (☃.length == 2) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"if", "unless"});
      } else {
         return ☃.length == 3 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
      }
   }
}
