package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CommandHandler implements ICommandManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<String, ICommand> commandMap = Maps.newHashMap();
   private final Set<ICommand> commandSet = Sets.newHashSet();

   @Override
   public int executeCommand(ICommandSender var1, String var2) {
      ☃ = ☃.trim();
      if (☃.startsWith("/")) {
         ☃ = ☃.substring(1);
      }

      String[] ☃ = ☃.split(" ");
      String ☃x = ☃[0];
      ☃ = dropFirstString(☃);
      ICommand ☃xx = this.commandMap.get(☃x);
      int ☃xxx = 0;

      try {
         int ☃xxxx = this.getUsernameIndex(☃xx, ☃);
         if (☃xx == null) {
            TextComponentTranslation ☃xxxxx = new TextComponentTranslation("commands.generic.notFound");
            ☃xxxxx.getStyle().setColor(TextFormatting.RED);
            ☃.sendMessage(☃xxxxx);
         } else if (☃xx.checkPermission(this.getServer(), ☃)) {
            if (☃xxxx > -1) {
               List<Entity> ☃xxxxx = EntitySelector.matchEntities(☃, ☃[☃xxxx], Entity.class);
               String ☃xxxxxx = ☃[☃xxxx];
               ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, ☃xxxxx.size());
               if (☃xxxxx.isEmpty()) {
                  throw new PlayerNotFoundException("commands.generic.selector.notFound", ☃[☃xxxx]);
               }

               for (Entity ☃xxxxxxx : ☃xxxxx) {
                  ☃[☃xxxx] = ☃xxxxxxx.getCachedUniqueIdString();
                  if (this.tryExecute(☃, ☃, ☃xx, ☃)) {
                     ☃xxx++;
                  }
               }

               ☃[☃xxxx] = ☃xxxxxx;
            } else {
               ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
               if (this.tryExecute(☃, ☃, ☃xx, ☃)) {
                  ☃xxx++;
               }
            }
         } else {
            TextComponentTranslation ☃xxxxx = new TextComponentTranslation("commands.generic.permission");
            ☃xxxxx.getStyle().setColor(TextFormatting.RED);
            ☃.sendMessage(☃xxxxx);
         }
      } catch (CommandException var12) {
         TextComponentTranslation ☃xxxxx = new TextComponentTranslation(var12.getMessage(), var12.getErrorObjects());
         ☃xxxxx.getStyle().setColor(TextFormatting.RED);
         ☃.sendMessage(☃xxxxx);
      }

      ☃.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, ☃xxx);
      return ☃xxx;
   }

   protected boolean tryExecute(ICommandSender var1, String[] var2, ICommand var3, String var4) {
      try {
         ☃.execute(this.getServer(), ☃, ☃);
         return true;
      } catch (WrongUsageException var7) {
         TextComponentTranslation ☃ = new TextComponentTranslation(
            "commands.generic.usage", new TextComponentTranslation(var7.getMessage(), var7.getErrorObjects())
         );
         ☃.getStyle().setColor(TextFormatting.RED);
         ☃.sendMessage(☃);
      } catch (CommandException var8) {
         TextComponentTranslation ☃x = new TextComponentTranslation(var8.getMessage(), var8.getErrorObjects());
         ☃x.getStyle().setColor(TextFormatting.RED);
         ☃.sendMessage(☃x);
      } catch (Throwable var9) {
         TextComponentTranslation ☃xx = new TextComponentTranslation("commands.generic.exception");
         ☃xx.getStyle().setColor(TextFormatting.RED);
         ☃.sendMessage(☃xx);
         LOGGER.warn("Couldn't process command: " + ☃, var9);
      }

      return false;
   }

   protected abstract MinecraftServer getServer();

   public ICommand registerCommand(ICommand var1) {
      this.commandMap.put(☃.getName(), ☃);
      this.commandSet.add(☃);

      for (String ☃ : ☃.getAliases()) {
         ICommand ☃x = this.commandMap.get(☃);
         if (☃x == null || !☃x.getName().equals(☃)) {
            this.commandMap.put(☃, ☃);
         }
      }

      return ☃;
   }

   private static String[] dropFirstString(String[] var0) {
      String[] ☃ = new String[☃.length - 1];
      System.arraycopy(☃, 1, ☃, 0, ☃.length - 1);
      return ☃;
   }

   @Override
   public List<String> getTabCompletions(ICommandSender var1, String var2, @Nullable BlockPos var3) {
      String[] ☃ = ☃.split(" ", -1);
      String ☃x = ☃[0];
      if (☃.length == 1) {
         List<String> ☃xx = Lists.newArrayList();

         for (Entry<String, ICommand> ☃xxx : this.commandMap.entrySet()) {
            if (CommandBase.doesStringStartWith(☃x, ☃xxx.getKey()) && ☃xxx.getValue().checkPermission(this.getServer(), ☃)) {
               ☃xx.add(☃xxx.getKey());
            }
         }

         return ☃xx;
      } else {
         if (☃.length > 1) {
            ICommand ☃xx = this.commandMap.get(☃x);
            if (☃xx != null && ☃xx.checkPermission(this.getServer(), ☃)) {
               return ☃xx.getTabCompletions(this.getServer(), ☃, dropFirstString(☃), ☃);
            }
         }

         return Collections.emptyList();
      }
   }

   @Override
   public List<ICommand> getPossibleCommands(ICommandSender var1) {
      List<ICommand> ☃ = Lists.newArrayList();

      for (ICommand ☃x : this.commandSet) {
         if (☃x.checkPermission(this.getServer(), ☃)) {
            ☃.add(☃x);
         }
      }

      return ☃;
   }

   @Override
   public Map<String, ICommand> getCommands() {
      return this.commandMap;
   }

   private int getUsernameIndex(ICommand var1, String[] var2) throws CommandException {
      if (☃ == null) {
         return -1;
      } else {
         for (int ☃ = 0; ☃ < ☃.length; ☃++) {
            if (☃.isUsernameIndex(☃, ☃) && EntitySelector.matchesMultiplePlayers(☃[☃])) {
               return ☃;
            }
         }

         return -1;
      }
   }
}
