package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder extends CommandBase {
   @Override
   public String getName() {
      return "worldborder";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.worldborder.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.worldborder.usage");
      } else {
         WorldBorder ☃ = this.getWorldBorder(☃);
         if ("set".equals(☃[0])) {
            if (☃.length != 2 && ☃.length != 3) {
               throw new WrongUsageException("commands.worldborder.set.usage");
            }

            double ☃x = ☃.getTargetSize();
            double ☃xx = parseDouble(☃[1], 1.0, 6.0E7);
            long ☃xxx = ☃.length > 2 ? parseLong(☃[2], 0L, 9223372036854775L) * 1000L : 0L;
            if (☃xxx > 0L) {
               ☃.setTransition(☃x, ☃xx, ☃xxx);
               if (☃x > ☃xx) {
                  notifyCommandListener(
                     ☃,
                     this,
                     "commands.worldborder.setSlowly.shrink.success",
                     new Object[]{String.format("%.1f", ☃xx), String.format("%.1f", ☃x), Long.toString(☃xxx / 1000L)}
                  );
               } else {
                  notifyCommandListener(
                     ☃,
                     this,
                     "commands.worldborder.setSlowly.grow.success",
                     new Object[]{String.format("%.1f", ☃xx), String.format("%.1f", ☃x), Long.toString(☃xxx / 1000L)}
                  );
               }
            } else {
               ☃.setTransition(☃xx);
               notifyCommandListener(☃, this, "commands.worldborder.set.success", new Object[]{String.format("%.1f", ☃xx), String.format("%.1f", ☃x)});
            }
         } else if ("add".equals(☃[0])) {
            if (☃.length != 2 && ☃.length != 3) {
               throw new WrongUsageException("commands.worldborder.add.usage");
            }

            double ☃x = ☃.getDiameter();
            double ☃xx = ☃x + parseDouble(☃[1], -☃x, 6.0E7 - ☃x);
            long ☃xxx = ☃.getTimeUntilTarget() + (☃.length > 2 ? parseLong(☃[2], 0L, 9223372036854775L) * 1000L : 0L);
            if (☃xxx > 0L) {
               ☃.setTransition(☃x, ☃xx, ☃xxx);
               if (☃x > ☃xx) {
                  notifyCommandListener(
                     ☃,
                     this,
                     "commands.worldborder.setSlowly.shrink.success",
                     new Object[]{String.format("%.1f", ☃xx), String.format("%.1f", ☃x), Long.toString(☃xxx / 1000L)}
                  );
               } else {
                  notifyCommandListener(
                     ☃,
                     this,
                     "commands.worldborder.setSlowly.grow.success",
                     new Object[]{String.format("%.1f", ☃xx), String.format("%.1f", ☃x), Long.toString(☃xxx / 1000L)}
                  );
               }
            } else {
               ☃.setTransition(☃xx);
               notifyCommandListener(☃, this, "commands.worldborder.set.success", new Object[]{String.format("%.1f", ☃xx), String.format("%.1f", ☃x)});
            }
         } else if ("center".equals(☃[0])) {
            if (☃.length != 3) {
               throw new WrongUsageException("commands.worldborder.center.usage");
            }

            BlockPos ☃x = ☃.getPosition();
            double ☃xx = parseDouble(☃x.getX() + 0.5, ☃[1], true);
            double ☃xxx = parseDouble(☃x.getZ() + 0.5, ☃[2], true);
            ☃.setCenter(☃xx, ☃xxx);
            notifyCommandListener(☃, this, "commands.worldborder.center.success", new Object[]{☃xx, ☃xxx});
         } else if ("damage".equals(☃[0])) {
            if (☃.length < 2) {
               throw new WrongUsageException("commands.worldborder.damage.usage");
            }

            if ("buffer".equals(☃[1])) {
               if (☃.length != 3) {
                  throw new WrongUsageException("commands.worldborder.damage.buffer.usage");
               }

               double ☃x = parseDouble(☃[2], 0.0);
               double ☃xx = ☃.getDamageBuffer();
               ☃.setDamageBuffer(☃x);
               notifyCommandListener(☃, this, "commands.worldborder.damage.buffer.success", new Object[]{String.format("%.1f", ☃x), String.format("%.1f", ☃xx)});
            } else if ("amount".equals(☃[1])) {
               if (☃.length != 3) {
                  throw new WrongUsageException("commands.worldborder.damage.amount.usage");
               }

               double ☃x = parseDouble(☃[2], 0.0);
               double ☃xx = ☃.getDamageAmount();
               ☃.setDamageAmount(☃x);
               notifyCommandListener(☃, this, "commands.worldborder.damage.amount.success", new Object[]{String.format("%.2f", ☃x), String.format("%.2f", ☃xx)});
            }
         } else if ("warning".equals(☃[0])) {
            if (☃.length < 2) {
               throw new WrongUsageException("commands.worldborder.warning.usage");
            }

            if ("time".equals(☃[1])) {
               if (☃.length != 3) {
                  throw new WrongUsageException("commands.worldborder.warning.time.usage");
               }

               int ☃x = parseInt(☃[2], 0);
               int ☃xx = ☃.getWarningTime();
               ☃.setWarningTime(☃x);
               notifyCommandListener(☃, this, "commands.worldborder.warning.time.success", new Object[]{☃x, ☃xx});
            } else if ("distance".equals(☃[1])) {
               if (☃.length != 3) {
                  throw new WrongUsageException("commands.worldborder.warning.distance.usage");
               }

               int ☃x = parseInt(☃[2], 0);
               int ☃xx = ☃.getWarningDistance();
               ☃.setWarningDistance(☃x);
               notifyCommandListener(☃, this, "commands.worldborder.warning.distance.success", new Object[]{☃x, ☃xx});
            }
         } else {
            if (!"get".equals(☃[0])) {
               throw new WrongUsageException("commands.worldborder.usage");
            }

            double ☃x = ☃.getDiameter();
            ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor(☃x + 0.5));
            ☃.sendMessage(new TextComponentTranslation("commands.worldborder.get.success", String.format("%.0f", ☃x)));
         }
      }
   }

   protected WorldBorder getWorldBorder(MinecraftServer var1) {
      return ☃.worlds[0].getWorldBorder();
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"set", "center", "damage", "warning", "add", "get"});
      } else if (☃.length == 2 && "damage".equals(☃[0])) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"buffer", "amount"});
      } else if (☃.length >= 2 && ☃.length <= 3 && "center".equals(☃[0])) {
         return getTabCompletionCoordinateXZ(☃, 1, ☃);
      } else {
         return ☃.length == 2 && "warning".equals(☃[0]) ? getListOfStringsMatchingLastWord(☃, new String[]{"time", "distance"}) : Collections.emptyList();
      }
   }
}
