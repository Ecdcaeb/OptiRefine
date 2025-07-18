package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandStats extends CommandBase {
   @Override
   public String getName() {
      return "stats";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.stats.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.stats.usage");
      } else {
         boolean ☃;
         if ("entity".equals(☃[0])) {
            ☃ = false;
         } else {
            if (!"block".equals(☃[0])) {
               throw new WrongUsageException("commands.stats.usage");
            }

            ☃ = true;
         }

         int ☃x;
         if (☃) {
            if (☃.length < 5) {
               throw new WrongUsageException("commands.stats.block.usage");
            }

            ☃x = 4;
         } else {
            if (☃.length < 3) {
               throw new WrongUsageException("commands.stats.entity.usage");
            }

            ☃x = 2;
         }

         String ☃xx = ☃[☃x++];
         if ("set".equals(☃xx)) {
            if (☃.length < ☃x + 3) {
               if (☃x == 5) {
                  throw new WrongUsageException("commands.stats.block.set.usage");
               }

               throw new WrongUsageException("commands.stats.entity.set.usage");
            }
         } else {
            if (!"clear".equals(☃xx)) {
               throw new WrongUsageException("commands.stats.usage");
            }

            if (☃.length < ☃x + 1) {
               if (☃x == 5) {
                  throw new WrongUsageException("commands.stats.block.clear.usage");
               }

               throw new WrongUsageException("commands.stats.entity.clear.usage");
            }
         }

         CommandResultStats.Type ☃xxx = CommandResultStats.Type.getTypeByName(☃[☃x++]);
         if (☃xxx == null) {
            throw new CommandException("commands.stats.failed");
         } else {
            World ☃xxxx = ☃.getEntityWorld();
            CommandResultStats ☃xxxxx;
            if (☃) {
               BlockPos ☃xxxxxx = parseBlockPos(☃, ☃, 1, false);
               TileEntity ☃xxxxxxx = ☃xxxx.getTileEntity(☃xxxxxx);
               if (☃xxxxxxx == null) {
                  throw new CommandException("commands.stats.noCompatibleBlock", ☃xxxxxx.getX(), ☃xxxxxx.getY(), ☃xxxxxx.getZ());
               }

               if (☃xxxxxxx instanceof TileEntityCommandBlock) {
                  ☃xxxxx = ((TileEntityCommandBlock)☃xxxxxxx).getCommandResultStats();
               } else {
                  if (!(☃xxxxxxx instanceof TileEntitySign)) {
                     throw new CommandException("commands.stats.noCompatibleBlock", ☃xxxxxx.getX(), ☃xxxxxx.getY(), ☃xxxxxx.getZ());
                  }

                  ☃xxxxx = ((TileEntitySign)☃xxxxxxx).getStats();
               }
            } else {
               Entity ☃xxxxxxxx = getEntity(☃, ☃, ☃[1]);
               ☃xxxxx = ☃xxxxxxxx.getCommandStats();
            }

            if ("set".equals(☃xx)) {
               String ☃xxxxxxxx = ☃[☃x++];
               String ☃xxxxxxxxx = ☃[☃x];
               if (☃xxxxxxxx.isEmpty() || ☃xxxxxxxxx.isEmpty()) {
                  throw new CommandException("commands.stats.failed");
               }

               CommandResultStats.setScoreBoardStat(☃xxxxx, ☃xxx, ☃xxxxxxxx, ☃xxxxxxxxx);
               notifyCommandListener(☃, this, "commands.stats.success", new Object[]{☃xxx.getTypeName(), ☃xxxxxxxxx, ☃xxxxxxxx});
            } else if ("clear".equals(☃xx)) {
               CommandResultStats.setScoreBoardStat(☃xxxxx, ☃xxx, null, null);
               notifyCommandListener(☃, this, "commands.stats.cleared", new Object[]{☃xxx.getTypeName()});
            }

            if (☃) {
               BlockPos ☃xxxxxxxx = parseBlockPos(☃, ☃, 1, false);
               TileEntity ☃xxxxxxxxx = ☃xxxx.getTileEntity(☃xxxxxxxx);
               ☃xxxxxxxxx.markDirty();
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"entity", "block"});
      } else if (☃.length == 2 && "entity".equals(☃[0])) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else if (☃.length >= 2 && ☃.length <= 4 && "block".equals(☃[0])) {
         return getTabCompletionCoordinate(☃, 1, ☃);
      } else if ((☃.length != 3 || !"entity".equals(☃[0])) && (☃.length != 5 || !"block".equals(☃[0]))) {
         if ((☃.length != 4 || !"entity".equals(☃[0])) && (☃.length != 6 || !"block".equals(☃[0]))) {
            return (☃.length != 6 || !"entity".equals(☃[0])) && (☃.length != 8 || !"block".equals(☃[0]))
               ? Collections.emptyList()
               : getListOfStringsMatchingLastWord(☃, this.getObjectiveNames(☃));
         } else {
            return getListOfStringsMatchingLastWord(☃, CommandResultStats.Type.getTypeNames());
         }
      } else {
         return getListOfStringsMatchingLastWord(☃, new String[]{"set", "clear"});
      }
   }

   protected List<String> getObjectiveNames(MinecraftServer var1) {
      Collection<ScoreObjective> ☃ = ☃.getWorld(0).getScoreboard().getScoreObjectives();
      List<String> ☃x = Lists.newArrayList();

      for (ScoreObjective ☃xx : ☃) {
         if (!☃xx.getCriteria().isReadOnly()) {
            ☃x.add(☃xx.getName());
         }
      }

      return ☃x;
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃.length > 0 && "entity".equals(☃[0]) && ☃ == 1;
   }
}
