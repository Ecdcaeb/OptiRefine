package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandScoreboard extends CommandBase {
   @Override
   public String getName() {
      return "scoreboard";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.scoreboard.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (!this.handleUserWildcards(☃, ☃, ☃)) {
         if (☃.length < 1) {
            throw new WrongUsageException("commands.scoreboard.usage");
         } else {
            if ("objectives".equalsIgnoreCase(☃[0])) {
               if (☃.length == 1) {
                  throw new WrongUsageException("commands.scoreboard.objectives.usage");
               }

               if ("list".equalsIgnoreCase(☃[1])) {
                  this.listObjectives(☃, ☃);
               } else if ("add".equalsIgnoreCase(☃[1])) {
                  if (☃.length < 4) {
                     throw new WrongUsageException("commands.scoreboard.objectives.add.usage");
                  }

                  this.addObjective(☃, ☃, 2, ☃);
               } else if ("remove".equalsIgnoreCase(☃[1])) {
                  if (☃.length != 3) {
                     throw new WrongUsageException("commands.scoreboard.objectives.remove.usage");
                  }

                  this.removeObjective(☃, ☃[2], ☃);
               } else {
                  if (!"setdisplay".equalsIgnoreCase(☃[1])) {
                     throw new WrongUsageException("commands.scoreboard.objectives.usage");
                  }

                  if (☃.length != 3 && ☃.length != 4) {
                     throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage");
                  }

                  this.setDisplayObjective(☃, ☃, 2, ☃);
               }
            } else if ("players".equalsIgnoreCase(☃[0])) {
               if (☃.length == 1) {
                  throw new WrongUsageException("commands.scoreboard.players.usage");
               }

               if ("list".equalsIgnoreCase(☃[1])) {
                  if (☃.length > 3) {
                     throw new WrongUsageException("commands.scoreboard.players.list.usage");
                  }

                  this.listPlayers(☃, ☃, 2, ☃);
               } else if ("add".equalsIgnoreCase(☃[1])) {
                  if (☃.length < 5) {
                     throw new WrongUsageException("commands.scoreboard.players.add.usage");
                  }

                  this.addPlayerScore(☃, ☃, 2, ☃);
               } else if ("remove".equalsIgnoreCase(☃[1])) {
                  if (☃.length < 5) {
                     throw new WrongUsageException("commands.scoreboard.players.remove.usage");
                  }

                  this.addPlayerScore(☃, ☃, 2, ☃);
               } else if ("set".equalsIgnoreCase(☃[1])) {
                  if (☃.length < 5) {
                     throw new WrongUsageException("commands.scoreboard.players.set.usage");
                  }

                  this.addPlayerScore(☃, ☃, 2, ☃);
               } else if ("reset".equalsIgnoreCase(☃[1])) {
                  if (☃.length != 3 && ☃.length != 4) {
                     throw new WrongUsageException("commands.scoreboard.players.reset.usage");
                  }

                  this.resetPlayerScore(☃, ☃, 2, ☃);
               } else if ("enable".equalsIgnoreCase(☃[1])) {
                  if (☃.length != 4) {
                     throw new WrongUsageException("commands.scoreboard.players.enable.usage");
                  }

                  this.enablePlayerTrigger(☃, ☃, 2, ☃);
               } else if ("test".equalsIgnoreCase(☃[1])) {
                  if (☃.length != 5 && ☃.length != 6) {
                     throw new WrongUsageException("commands.scoreboard.players.test.usage");
                  }

                  this.testPlayerScore(☃, ☃, 2, ☃);
               } else if ("operation".equalsIgnoreCase(☃[1])) {
                  if (☃.length != 7) {
                     throw new WrongUsageException("commands.scoreboard.players.operation.usage");
                  }

                  this.applyPlayerOperation(☃, ☃, 2, ☃);
               } else {
                  if (!"tag".equalsIgnoreCase(☃[1])) {
                     throw new WrongUsageException("commands.scoreboard.players.usage");
                  }

                  if (☃.length < 4) {
                     throw new WrongUsageException("commands.scoreboard.players.tag.usage");
                  }

                  this.applyPlayerTag(☃, ☃, ☃, 2);
               }
            } else {
               if (!"teams".equalsIgnoreCase(☃[0])) {
                  throw new WrongUsageException("commands.scoreboard.usage");
               }

               if (☃.length == 1) {
                  throw new WrongUsageException("commands.scoreboard.teams.usage");
               }

               if ("list".equalsIgnoreCase(☃[1])) {
                  if (☃.length > 3) {
                     throw new WrongUsageException("commands.scoreboard.teams.list.usage");
                  }

                  this.listTeams(☃, ☃, 2, ☃);
               } else if ("add".equalsIgnoreCase(☃[1])) {
                  if (☃.length < 3) {
                     throw new WrongUsageException("commands.scoreboard.teams.add.usage");
                  }

                  this.addTeam(☃, ☃, 2, ☃);
               } else if ("remove".equalsIgnoreCase(☃[1])) {
                  if (☃.length != 3) {
                     throw new WrongUsageException("commands.scoreboard.teams.remove.usage");
                  }

                  this.removeTeam(☃, ☃, 2, ☃);
               } else if ("empty".equalsIgnoreCase(☃[1])) {
                  if (☃.length != 3) {
                     throw new WrongUsageException("commands.scoreboard.teams.empty.usage");
                  }

                  this.emptyTeam(☃, ☃, 2, ☃);
               } else if ("join".equalsIgnoreCase(☃[1])) {
                  if (☃.length < 4 && (☃.length != 3 || !(☃ instanceof EntityPlayer))) {
                     throw new WrongUsageException("commands.scoreboard.teams.join.usage");
                  }

                  this.joinTeam(☃, ☃, 2, ☃);
               } else if ("leave".equalsIgnoreCase(☃[1])) {
                  if (☃.length < 3 && !(☃ instanceof EntityPlayer)) {
                     throw new WrongUsageException("commands.scoreboard.teams.leave.usage");
                  }

                  this.leaveTeam(☃, ☃, 2, ☃);
               } else {
                  if (!"option".equalsIgnoreCase(☃[1])) {
                     throw new WrongUsageException("commands.scoreboard.teams.usage");
                  }

                  if (☃.length != 4 && ☃.length != 5) {
                     throw new WrongUsageException("commands.scoreboard.teams.option.usage");
                  }

                  this.setTeamOption(☃, ☃, 2, ☃);
               }
            }
         }
      }
   }

   private boolean handleUserWildcards(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      int ☃ = -1;

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         if (this.isUsernameIndex(☃, ☃x) && "*".equals(☃[☃x])) {
            if (☃ >= 0) {
               throw new CommandException("commands.scoreboard.noMultiWildcard");
            }

            ☃ = ☃x;
         }
      }

      if (☃ < 0) {
         return false;
      } else {
         List<String> ☃xx = Lists.newArrayList(this.getScoreboard(☃).getObjectiveNames());
         String ☃xxx = ☃[☃];
         List<String> ☃xxxx = Lists.newArrayList();

         for (String ☃xxxxx : ☃xx) {
            ☃[☃] = ☃xxxxx;

            try {
               this.execute(☃, ☃, ☃);
               ☃xxxx.add(☃xxxxx);
            } catch (CommandException var12) {
               TextComponentTranslation ☃xxxxxx = new TextComponentTranslation(var12.getMessage(), var12.getErrorObjects());
               ☃xxxxxx.getStyle().setColor(TextFormatting.RED);
               ☃.sendMessage(☃xxxxxx);
            }
         }

         ☃[☃] = ☃xxx;
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, ☃xxxx.size());
         if (☃xxxx.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.allMatchesFailed");
         } else {
            return true;
         }
      }
   }

   protected Scoreboard getScoreboard(MinecraftServer var1) {
      return ☃.getWorld(0).getScoreboard();
   }

   protected ScoreObjective convertToObjective(String var1, boolean var2, MinecraftServer var3) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      ScoreObjective ☃x = ☃.getObjective(☃);
      if (☃x == null) {
         throw new CommandException("commands.scoreboard.objectiveNotFound", ☃);
      } else if (☃ && ☃x.getCriteria().isReadOnly()) {
         throw new CommandException("commands.scoreboard.objectiveReadOnly", ☃);
      } else {
         return ☃x;
      }
   }

   protected ScorePlayerTeam convertToTeam(String var1, MinecraftServer var2) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      ScorePlayerTeam ☃x = ☃.getTeam(☃);
      if (☃x == null) {
         throw new CommandException("commands.scoreboard.teamNotFound", ☃);
      } else {
         return ☃x;
      }
   }

   protected void addObjective(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      String ☃ = ☃[☃++];
      String ☃x = ☃[☃++];
      Scoreboard ☃xx = this.getScoreboard(☃);
      IScoreCriteria ☃xxx = IScoreCriteria.INSTANCES.get(☃x);
      if (☃xxx == null) {
         throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", ☃x);
      } else if (☃xx.getObjective(☃) != null) {
         throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", ☃);
      } else if (☃.length() > 16) {
         throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", ☃, 16);
      } else if (☃.isEmpty()) {
         throw new WrongUsageException("commands.scoreboard.objectives.add.usage");
      } else {
         if (☃.length > ☃) {
            String ☃xxxx = getChatComponentFromNthArg(☃, ☃, ☃).getUnformattedText();
            if (☃xxxx.length() > 32) {
               throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", ☃xxxx, 32);
            }

            if (☃xxxx.isEmpty()) {
               ☃xx.addScoreObjective(☃, ☃xxx);
            } else {
               ☃xx.addScoreObjective(☃, ☃xxx).setDisplayName(☃xxxx);
            }
         } else {
            ☃xx.addScoreObjective(☃, ☃xxx);
         }

         notifyCommandListener(☃, this, "commands.scoreboard.objectives.add.success", new Object[]{☃});
      }
   }

   protected void addTeam(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      String ☃ = ☃[☃++];
      Scoreboard ☃x = this.getScoreboard(☃);
      if (☃x.getTeam(☃) != null) {
         throw new CommandException("commands.scoreboard.teams.add.alreadyExists", ☃);
      } else if (☃.length() > 16) {
         throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", ☃, 16);
      } else if (☃.isEmpty()) {
         throw new WrongUsageException("commands.scoreboard.teams.add.usage");
      } else {
         if (☃.length > ☃) {
            String ☃xx = getChatComponentFromNthArg(☃, ☃, ☃).getUnformattedText();
            if (☃xx.length() > 32) {
               throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", ☃xx, 32);
            }

            if (☃xx.isEmpty()) {
               ☃x.createTeam(☃);
            } else {
               ☃x.createTeam(☃).setDisplayName(☃xx);
            }
         } else {
            ☃x.createTeam(☃);
         }

         notifyCommandListener(☃, this, "commands.scoreboard.teams.add.success", new Object[]{☃});
      }
   }

   protected void setTeamOption(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      ScorePlayerTeam ☃ = this.convertToTeam(☃[☃++], ☃);
      if (☃ != null) {
         String ☃x = ☃[☃++].toLowerCase(Locale.ROOT);
         if (!"color".equalsIgnoreCase(☃x)
            && !"friendlyfire".equalsIgnoreCase(☃x)
            && !"seeFriendlyInvisibles".equalsIgnoreCase(☃x)
            && !"nametagVisibility".equalsIgnoreCase(☃x)
            && !"deathMessageVisibility".equalsIgnoreCase(☃x)
            && !"collisionRule".equalsIgnoreCase(☃x)) {
            throw new WrongUsageException("commands.scoreboard.teams.option.usage");
         } else if (☃.length == 4) {
            if ("color".equalsIgnoreCase(☃x)) {
               throw new WrongUsageException(
                  "commands.scoreboard.teams.option.noValue", ☃x, joinNiceStringFromCollection(TextFormatting.getValidValues(true, false))
               );
            } else if ("friendlyfire".equalsIgnoreCase(☃x) || "seeFriendlyInvisibles".equalsIgnoreCase(☃x)) {
               throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceStringFromCollection(Arrays.asList("true", "false")));
            } else if ("nametagVisibility".equalsIgnoreCase(☃x) || "deathMessageVisibility".equalsIgnoreCase(☃x)) {
               throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceString(Team.EnumVisible.getNames()));
            } else if ("collisionRule".equalsIgnoreCase(☃x)) {
               throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceString(Team.CollisionRule.getNames()));
            } else {
               throw new WrongUsageException("commands.scoreboard.teams.option.usage");
            }
         } else {
            String ☃xx = ☃[☃];
            if ("color".equalsIgnoreCase(☃x)) {
               TextFormatting ☃xxx = TextFormatting.getValueByName(☃xx);
               if (☃xxx == null || ☃xxx.isFancyStyling()) {
                  throw new WrongUsageException(
                     "commands.scoreboard.teams.option.noValue", ☃x, joinNiceStringFromCollection(TextFormatting.getValidValues(true, false))
                  );
               }

               ☃.setColor(☃xxx);
               ☃.setPrefix(☃xxx.toString());
               ☃.setSuffix(TextFormatting.RESET.toString());
            } else if ("friendlyfire".equalsIgnoreCase(☃x)) {
               if (!"true".equalsIgnoreCase(☃xx) && !"false".equalsIgnoreCase(☃xx)) {
                  throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceStringFromCollection(Arrays.asList("true", "false")));
               }

               ☃.setAllowFriendlyFire("true".equalsIgnoreCase(☃xx));
            } else if ("seeFriendlyInvisibles".equalsIgnoreCase(☃x)) {
               if (!"true".equalsIgnoreCase(☃xx) && !"false".equalsIgnoreCase(☃xx)) {
                  throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceStringFromCollection(Arrays.asList("true", "false")));
               }

               ☃.setSeeFriendlyInvisiblesEnabled("true".equalsIgnoreCase(☃xx));
            } else if ("nametagVisibility".equalsIgnoreCase(☃x)) {
               Team.EnumVisible ☃xxx = Team.EnumVisible.getByName(☃xx);
               if (☃xxx == null) {
                  throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceString(Team.EnumVisible.getNames()));
               }

               ☃.setNameTagVisibility(☃xxx);
            } else if ("deathMessageVisibility".equalsIgnoreCase(☃x)) {
               Team.EnumVisible ☃xxx = Team.EnumVisible.getByName(☃xx);
               if (☃xxx == null) {
                  throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceString(Team.EnumVisible.getNames()));
               }

               ☃.setDeathMessageVisibility(☃xxx);
            } else if ("collisionRule".equalsIgnoreCase(☃x)) {
               Team.CollisionRule ☃xxx = Team.CollisionRule.getByName(☃xx);
               if (☃xxx == null) {
                  throw new WrongUsageException("commands.scoreboard.teams.option.noValue", ☃x, joinNiceString(Team.CollisionRule.getNames()));
               }

               ☃.setCollisionRule(☃xxx);
            }

            notifyCommandListener(☃, this, "commands.scoreboard.teams.option.success", new Object[]{☃x, ☃.getName(), ☃xx});
         }
      }
   }

   protected void removeTeam(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      ScorePlayerTeam ☃x = this.convertToTeam(☃[☃], ☃);
      if (☃x != null) {
         ☃.removeTeam(☃x);
         notifyCommandListener(☃, this, "commands.scoreboard.teams.remove.success", new Object[]{☃x.getName()});
      }
   }

   protected void listTeams(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      if (☃.length > ☃) {
         ScorePlayerTeam ☃x = this.convertToTeam(☃[☃], ☃);
         if (☃x == null) {
            return;
         }

         Collection<String> ☃xx = ☃x.getMembershipCollection();
         ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃xx.size());
         if (☃xx.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.list.player.empty", ☃x.getName());
         }

         TextComponentTranslation ☃xxx = new TextComponentTranslation("commands.scoreboard.teams.list.player.count", ☃xx.size(), ☃x.getName());
         ☃xxx.getStyle().setColor(TextFormatting.DARK_GREEN);
         ☃.sendMessage(☃xxx);
         ☃.sendMessage(new TextComponentString(joinNiceString(☃xx.toArray())));
      } else {
         Collection<ScorePlayerTeam> ☃xx = ☃.getTeams();
         ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃xx.size());
         if (☃xx.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.list.empty");
         }

         TextComponentTranslation ☃xxx = new TextComponentTranslation("commands.scoreboard.teams.list.count", ☃xx.size());
         ☃xxx.getStyle().setColor(TextFormatting.DARK_GREEN);
         ☃.sendMessage(☃xxx);

         for (ScorePlayerTeam ☃xxxx : ☃xx) {
            ☃.sendMessage(
               new TextComponentTranslation(
                  "commands.scoreboard.teams.list.entry", ☃xxxx.getName(), ☃xxxx.getDisplayName(), ☃xxxx.getMembershipCollection().size()
               )
            );
         }
      }
   }

   protected void joinTeam(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      String ☃x = ☃[☃++];
      Set<String> ☃xx = Sets.newHashSet();
      Set<String> ☃xxx = Sets.newHashSet();
      if (☃ instanceof EntityPlayer && ☃ == ☃.length) {
         String ☃xxxx = getCommandSenderAsPlayer(☃).getName();
         if (☃.addPlayerToTeam(☃xxxx, ☃x)) {
            ☃xx.add(☃xxxx);
         } else {
            ☃xxx.add(☃xxxx);
         }
      } else {
         while (☃ < ☃.length) {
            String ☃xxxx = ☃[☃++];
            if (EntitySelector.isSelector(☃xxxx)) {
               for (Entity ☃xxxxx : getEntityList(☃, ☃, ☃xxxx)) {
                  String ☃xxxxxx = getEntityName(☃, ☃, ☃xxxxx.getCachedUniqueIdString());
                  if (☃.addPlayerToTeam(☃xxxxxx, ☃x)) {
                     ☃xx.add(☃xxxxxx);
                  } else {
                     ☃xxx.add(☃xxxxxx);
                  }
               }
            } else {
               String ☃xxxxxx = getEntityName(☃, ☃, ☃xxxx);
               if (☃.addPlayerToTeam(☃xxxxxx, ☃x)) {
                  ☃xx.add(☃xxxxxx);
               } else {
                  ☃xxx.add(☃xxxxxx);
               }
            }
         }
      }

      if (!☃xx.isEmpty()) {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, ☃xx.size());
         notifyCommandListener(
            ☃, this, "commands.scoreboard.teams.join.success", new Object[]{☃xx.size(), ☃x, joinNiceString(☃xx.toArray(new String[☃xx.size()]))}
         );
      }

      if (!☃xxx.isEmpty()) {
         throw new CommandException("commands.scoreboard.teams.join.failure", ☃xxx.size(), ☃x, joinNiceString(☃xxx.toArray(new String[☃xxx.size()])));
      }
   }

   protected void leaveTeam(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      Set<String> ☃x = Sets.newHashSet();
      Set<String> ☃xx = Sets.newHashSet();
      if (☃ instanceof EntityPlayer && ☃ == ☃.length) {
         String ☃xxx = getCommandSenderAsPlayer(☃).getName();
         if (☃.removePlayerFromTeams(☃xxx)) {
            ☃x.add(☃xxx);
         } else {
            ☃xx.add(☃xxx);
         }
      } else {
         while (☃ < ☃.length) {
            String ☃xxx = ☃[☃++];
            if (EntitySelector.isSelector(☃xxx)) {
               for (Entity ☃xxxx : getEntityList(☃, ☃, ☃xxx)) {
                  String ☃xxxxx = getEntityName(☃, ☃, ☃xxxx.getCachedUniqueIdString());
                  if (☃.removePlayerFromTeams(☃xxxxx)) {
                     ☃x.add(☃xxxxx);
                  } else {
                     ☃xx.add(☃xxxxx);
                  }
               }
            } else {
               String ☃xxxxx = getEntityName(☃, ☃, ☃xxx);
               if (☃.removePlayerFromTeams(☃xxxxx)) {
                  ☃x.add(☃xxxxx);
               } else {
                  ☃xx.add(☃xxxxx);
               }
            }
         }
      }

      if (!☃x.isEmpty()) {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, ☃x.size());
         notifyCommandListener(☃, this, "commands.scoreboard.teams.leave.success", new Object[]{☃x.size(), joinNiceString(☃x.toArray(new String[☃x.size()]))});
      }

      if (!☃xx.isEmpty()) {
         throw new CommandException("commands.scoreboard.teams.leave.failure", ☃xx.size(), joinNiceString(☃xx.toArray(new String[☃xx.size()])));
      }
   }

   protected void emptyTeam(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      ScorePlayerTeam ☃x = this.convertToTeam(☃[☃], ☃);
      if (☃x != null) {
         Collection<String> ☃xx = Lists.newArrayList(☃x.getMembershipCollection());
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, ☃xx.size());
         if (☃xx.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", ☃x.getName());
         } else {
            for (String ☃xxx : ☃xx) {
               ☃.removePlayerFromTeam(☃xxx, ☃x);
            }

            notifyCommandListener(☃, this, "commands.scoreboard.teams.empty.success", new Object[]{☃xx.size(), ☃x.getName()});
         }
      }
   }

   protected void removeObjective(ICommandSender var1, String var2, MinecraftServer var3) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      ScoreObjective ☃x = this.convertToObjective(☃, false, ☃);
      ☃.removeObjective(☃x);
      notifyCommandListener(☃, this, "commands.scoreboard.objectives.remove.success", new Object[]{☃});
   }

   protected void listObjectives(ICommandSender var1, MinecraftServer var2) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      Collection<ScoreObjective> ☃x = ☃.getScoreObjectives();
      if (☃x.isEmpty()) {
         throw new CommandException("commands.scoreboard.objectives.list.empty");
      } else {
         TextComponentTranslation ☃xx = new TextComponentTranslation("commands.scoreboard.objectives.list.count", ☃x.size());
         ☃xx.getStyle().setColor(TextFormatting.DARK_GREEN);
         ☃.sendMessage(☃xx);

         for (ScoreObjective ☃xxx : ☃x) {
            ☃.sendMessage(
               new TextComponentTranslation("commands.scoreboard.objectives.list.entry", ☃xxx.getName(), ☃xxx.getDisplayName(), ☃xxx.getCriteria().getName())
            );
         }
      }
   }

   protected void setDisplayObjective(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      String ☃x = ☃[☃++];
      int ☃xx = Scoreboard.getObjectiveDisplaySlotNumber(☃x);
      ScoreObjective ☃xxx = null;
      if (☃.length == 4) {
         ☃xxx = this.convertToObjective(☃[☃], false, ☃);
      }

      if (☃xx < 0) {
         throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", ☃x);
      } else {
         ☃.setObjectiveInDisplaySlot(☃xx, ☃xxx);
         if (☃xxx != null) {
            notifyCommandListener(
               ☃, this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[]{Scoreboard.getObjectiveDisplaySlot(☃xx), ☃xxx.getName()}
            );
         } else {
            notifyCommandListener(☃, this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[]{Scoreboard.getObjectiveDisplaySlot(☃xx)});
         }
      }
   }

   protected void listPlayers(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      if (☃.length > ☃) {
         String ☃x = getEntityName(☃, ☃, ☃[☃]);
         Map<ScoreObjective, Score> ☃xx = ☃.getObjectivesForEntity(☃x);
         ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃xx.size());
         if (☃xx.isEmpty()) {
            throw new CommandException("commands.scoreboard.players.list.player.empty", ☃x);
         }

         TextComponentTranslation ☃xxx = new TextComponentTranslation("commands.scoreboard.players.list.player.count", ☃xx.size(), ☃x);
         ☃xxx.getStyle().setColor(TextFormatting.DARK_GREEN);
         ☃.sendMessage(☃xxx);

         for (Score ☃xxxx : ☃xx.values()) {
            ☃.sendMessage(
               new TextComponentTranslation(
                  "commands.scoreboard.players.list.player.entry",
                  ☃xxxx.getScorePoints(),
                  ☃xxxx.getObjective().getDisplayName(),
                  ☃xxxx.getObjective().getName()
               )
            );
         }
      } else {
         Collection<String> ☃x = ☃.getObjectiveNames();
         ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃x.size());
         if (☃x.isEmpty()) {
            throw new CommandException("commands.scoreboard.players.list.empty");
         }

         TextComponentTranslation ☃xx = new TextComponentTranslation("commands.scoreboard.players.list.count", ☃x.size());
         ☃xx.getStyle().setColor(TextFormatting.DARK_GREEN);
         ☃.sendMessage(☃xx);
         ☃.sendMessage(new TextComponentString(joinNiceString(☃x.toArray())));
      }
   }

   protected void addPlayerScore(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      String ☃ = ☃[☃ - 1];
      int ☃x = ☃;
      String ☃xx = getEntityName(☃, ☃, ☃[☃++]);
      if (☃xx.length() > 40) {
         throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", ☃xx, 40);
      } else {
         ScoreObjective ☃xxx = this.convertToObjective(☃[☃++], true, ☃);
         int ☃xxxx = "set".equalsIgnoreCase(☃) ? parseInt(☃[☃++]) : parseInt(☃[☃++], 0);
         if (☃.length > ☃) {
            Entity ☃xxxxx = getEntity(☃, ☃, ☃[☃x]);

            try {
               NBTTagCompound ☃xxxxxx = JsonToNBT.getTagFromJson(buildString(☃, ☃));
               NBTTagCompound ☃xxxxxxx = entityToNBT(☃xxxxx);
               if (!NBTUtil.areNBTEquals(☃xxxxxx, ☃xxxxxxx, true)) {
                  throw new CommandException("commands.scoreboard.players.set.tagMismatch", ☃xx);
               }
            } catch (NBTException var13) {
               throw new CommandException("commands.scoreboard.players.set.tagError", var13.getMessage());
            }
         }

         Scoreboard ☃xxxxx = this.getScoreboard(☃);
         Score ☃xxxxxx = ☃xxxxx.getOrCreateScore(☃xx, ☃xxx);
         if ("set".equalsIgnoreCase(☃)) {
            ☃xxxxxx.setScorePoints(☃xxxx);
         } else if ("add".equalsIgnoreCase(☃)) {
            ☃xxxxxx.increaseScore(☃xxxx);
         } else {
            ☃xxxxxx.decreaseScore(☃xxxx);
         }

         notifyCommandListener(☃, this, "commands.scoreboard.players.set.success", new Object[]{☃xxx.getName(), ☃xx, ☃xxxxxx.getScorePoints()});
      }
   }

   protected void resetPlayerScore(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      String ☃x = getEntityName(☃, ☃, ☃[☃++]);
      if (☃.length > ☃) {
         ScoreObjective ☃xx = this.convertToObjective(☃[☃++], false, ☃);
         ☃.removeObjectiveFromEntity(☃x, ☃xx);
         notifyCommandListener(☃, this, "commands.scoreboard.players.resetscore.success", new Object[]{☃xx.getName(), ☃x});
      } else {
         ☃.removeObjectiveFromEntity(☃x, null);
         notifyCommandListener(☃, this, "commands.scoreboard.players.reset.success", new Object[]{☃x});
      }
   }

   protected void enablePlayerTrigger(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      String ☃x = getPlayerName(☃, ☃, ☃[☃++]);
      if (☃x.length() > 40) {
         throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", ☃x, 40);
      } else {
         ScoreObjective ☃xx = this.convertToObjective(☃[☃], false, ☃);
         if (☃xx.getCriteria() != IScoreCriteria.TRIGGER) {
            throw new CommandException("commands.scoreboard.players.enable.noTrigger", ☃xx.getName());
         } else {
            Score ☃xxx = ☃.getOrCreateScore(☃x, ☃xx);
            ☃xxx.setLocked(false);
            notifyCommandListener(☃, this, "commands.scoreboard.players.enable.success", new Object[]{☃xx.getName(), ☃x});
         }
      }
   }

   protected void testPlayerScore(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      String ☃x = getEntityName(☃, ☃, ☃[☃++]);
      if (☃x.length() > 40) {
         throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", ☃x, 40);
      } else {
         ScoreObjective ☃xx = this.convertToObjective(☃[☃++], false, ☃);
         if (!☃.entityHasObjective(☃x, ☃xx)) {
            throw new CommandException("commands.scoreboard.players.test.notFound", ☃xx.getName(), ☃x);
         } else {
            int ☃xxx = ☃[☃].equals("*") ? Integer.MIN_VALUE : parseInt(☃[☃]);
            ☃++;
            int ☃xxxx = ☃ < ☃.length && !☃[☃].equals("*") ? parseInt(☃[☃], ☃xxx) : Integer.MAX_VALUE;
            Score ☃xxxxx = ☃.getOrCreateScore(☃x, ☃xx);
            if (☃xxxxx.getScorePoints() >= ☃xxx && ☃xxxxx.getScorePoints() <= ☃xxxx) {
               notifyCommandListener(☃, this, "commands.scoreboard.players.test.success", new Object[]{☃xxxxx.getScorePoints(), ☃xxx, ☃xxxx});
            } else {
               throw new CommandException("commands.scoreboard.players.test.failed", ☃xxxxx.getScorePoints(), ☃xxx, ☃xxxx);
            }
         }
      }
   }

   protected void applyPlayerOperation(ICommandSender var1, String[] var2, int var3, MinecraftServer var4) throws CommandException {
      Scoreboard ☃ = this.getScoreboard(☃);
      String ☃x = getEntityName(☃, ☃, ☃[☃++]);
      ScoreObjective ☃xx = this.convertToObjective(☃[☃++], true, ☃);
      String ☃xxx = ☃[☃++];
      String ☃xxxx = getEntityName(☃, ☃, ☃[☃++]);
      ScoreObjective ☃xxxxx = this.convertToObjective(☃[☃], false, ☃);
      if (☃x.length() > 40) {
         throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", ☃x, 40);
      } else if (☃xxxx.length() > 40) {
         throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", ☃xxxx, 40);
      } else {
         Score ☃xxxxxx = ☃.getOrCreateScore(☃x, ☃xx);
         if (!☃.entityHasObjective(☃xxxx, ☃xxxxx)) {
            throw new CommandException("commands.scoreboard.players.operation.notFound", ☃xxxxx.getName(), ☃xxxx);
         } else {
            Score ☃xxxxxxx = ☃.getOrCreateScore(☃xxxx, ☃xxxxx);
            if ("+=".equals(☃xxx)) {
               ☃xxxxxx.setScorePoints(☃xxxxxx.getScorePoints() + ☃xxxxxxx.getScorePoints());
            } else if ("-=".equals(☃xxx)) {
               ☃xxxxxx.setScorePoints(☃xxxxxx.getScorePoints() - ☃xxxxxxx.getScorePoints());
            } else if ("*=".equals(☃xxx)) {
               ☃xxxxxx.setScorePoints(☃xxxxxx.getScorePoints() * ☃xxxxxxx.getScorePoints());
            } else if ("/=".equals(☃xxx)) {
               if (☃xxxxxxx.getScorePoints() != 0) {
                  ☃xxxxxx.setScorePoints(☃xxxxxx.getScorePoints() / ☃xxxxxxx.getScorePoints());
               }
            } else if ("%=".equals(☃xxx)) {
               if (☃xxxxxxx.getScorePoints() != 0) {
                  ☃xxxxxx.setScorePoints(☃xxxxxx.getScorePoints() % ☃xxxxxxx.getScorePoints());
               }
            } else if ("=".equals(☃xxx)) {
               ☃xxxxxx.setScorePoints(☃xxxxxxx.getScorePoints());
            } else if ("<".equals(☃xxx)) {
               ☃xxxxxx.setScorePoints(Math.min(☃xxxxxx.getScorePoints(), ☃xxxxxxx.getScorePoints()));
            } else if (">".equals(☃xxx)) {
               ☃xxxxxx.setScorePoints(Math.max(☃xxxxxx.getScorePoints(), ☃xxxxxxx.getScorePoints()));
            } else {
               if (!"><".equals(☃xxx)) {
                  throw new CommandException("commands.scoreboard.players.operation.invalidOperation", ☃xxx);
               }

               int ☃xxxxxxxx = ☃xxxxxx.getScorePoints();
               ☃xxxxxx.setScorePoints(☃xxxxxxx.getScorePoints());
               ☃xxxxxxx.setScorePoints(☃xxxxxxxx);
            }

            notifyCommandListener(☃, this, "commands.scoreboard.players.operation.success", new Object[0]);
         }
      }
   }

   protected void applyPlayerTag(MinecraftServer var1, ICommandSender var2, String[] var3, int var4) throws CommandException {
      String ☃ = getEntityName(☃, ☃, ☃[☃]);
      Entity ☃x = getEntity(☃, ☃, ☃[☃++]);
      String ☃xx = ☃[☃++];
      Set<String> ☃xxx = ☃x.getTags();
      if ("list".equals(☃xx)) {
         if (!☃xxx.isEmpty()) {
            TextComponentTranslation ☃xxxx = new TextComponentTranslation("commands.scoreboard.players.tag.list", ☃);
            ☃xxxx.getStyle().setColor(TextFormatting.DARK_GREEN);
            ☃.sendMessage(☃xxxx);
            ☃.sendMessage(new TextComponentString(joinNiceString(☃xxx.toArray())));
         }

         ☃.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ☃xxx.size());
      } else if (☃.length < 5) {
         throw new WrongUsageException("commands.scoreboard.players.tag.usage");
      } else {
         String ☃xxxx = ☃[☃++];
         if (☃.length > ☃) {
            try {
               NBTTagCompound ☃xxxxx = JsonToNBT.getTagFromJson(buildString(☃, ☃));
               NBTTagCompound ☃xxxxxx = entityToNBT(☃x);
               if (!NBTUtil.areNBTEquals(☃xxxxx, ☃xxxxxx, true)) {
                  throw new CommandException("commands.scoreboard.players.tag.tagMismatch", ☃);
               }
            } catch (NBTException var12) {
               throw new CommandException("commands.scoreboard.players.tag.tagError", var12.getMessage());
            }
         }

         if ("add".equals(☃xx)) {
            if (!☃x.addTag(☃xxxx)) {
               throw new CommandException("commands.scoreboard.players.tag.tooMany", 1024);
            }

            notifyCommandListener(☃, this, "commands.scoreboard.players.tag.success.add", new Object[]{☃xxxx});
         } else {
            if (!"remove".equals(☃xx)) {
               throw new WrongUsageException("commands.scoreboard.players.tag.usage");
            }

            if (!☃x.removeTag(☃xxxx)) {
               throw new CommandException("commands.scoreboard.players.tag.notFound", ☃xxxx);
            }

            notifyCommandListener(☃, this, "commands.scoreboard.players.tag.success.remove", new Object[]{☃xxxx});
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"objectives", "players", "teams"});
      } else {
         if ("objectives".equalsIgnoreCase(☃[0])) {
            if (☃.length == 2) {
               return getListOfStringsMatchingLastWord(☃, new String[]{"list", "add", "remove", "setdisplay"});
            }

            if ("add".equalsIgnoreCase(☃[1])) {
               if (☃.length == 4) {
                  Set<String> ☃ = IScoreCriteria.INSTANCES.keySet();
                  return getListOfStringsMatchingLastWord(☃, ☃);
               }
            } else if ("remove".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, this.getObjectiveNames(false, ☃));
               }
            } else if ("setdisplay".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, Scoreboard.getDisplaySlotStrings());
               }

               if (☃.length == 4) {
                  return getListOfStringsMatchingLastWord(☃, this.getObjectiveNames(false, ☃));
               }
            }
         } else if ("players".equalsIgnoreCase(☃[0])) {
            if (☃.length == 2) {
               return getListOfStringsMatchingLastWord(☃, new String[]{"set", "add", "remove", "reset", "list", "enable", "test", "operation", "tag"});
            }

            if ("set".equalsIgnoreCase(☃[1]) || "add".equalsIgnoreCase(☃[1]) || "remove".equalsIgnoreCase(☃[1]) || "reset".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
               }

               if (☃.length == 4) {
                  return getListOfStringsMatchingLastWord(☃, this.getObjectiveNames(true, ☃));
               }
            } else if ("enable".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
               }

               if (☃.length == 4) {
                  return getListOfStringsMatchingLastWord(☃, this.getTriggerNames(☃));
               }
            } else if ("list".equalsIgnoreCase(☃[1]) || "test".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, this.getScoreboard(☃).getObjectiveNames());
               }

               if (☃.length == 4 && "test".equalsIgnoreCase(☃[1])) {
                  return getListOfStringsMatchingLastWord(☃, this.getObjectiveNames(false, ☃));
               }
            } else if ("operation".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, this.getScoreboard(☃).getObjectiveNames());
               }

               if (☃.length == 4) {
                  return getListOfStringsMatchingLastWord(☃, this.getObjectiveNames(true, ☃));
               }

               if (☃.length == 5) {
                  return getListOfStringsMatchingLastWord(☃, new String[]{"+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><"});
               }

               if (☃.length == 6) {
                  return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
               }

               if (☃.length == 7) {
                  return getListOfStringsMatchingLastWord(☃, this.getObjectiveNames(false, ☃));
               }
            } else if ("tag".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, this.getScoreboard(☃).getObjectiveNames());
               }

               if (☃.length == 4) {
                  return getListOfStringsMatchingLastWord(☃, new String[]{"add", "remove", "list"});
               }
            }
         } else if ("teams".equalsIgnoreCase(☃[0])) {
            if (☃.length == 2) {
               return getListOfStringsMatchingLastWord(☃, new String[]{"add", "remove", "join", "leave", "empty", "list", "option"});
            }

            if ("join".equalsIgnoreCase(☃[1])) {
               if (☃.length == 3) {
                  return getListOfStringsMatchingLastWord(☃, this.getScoreboard(☃).getTeamNames());
               }

               if (☃.length >= 4) {
                  return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
               }
            } else {
               if ("leave".equalsIgnoreCase(☃[1])) {
                  return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
               }

               if ("empty".equalsIgnoreCase(☃[1]) || "list".equalsIgnoreCase(☃[1]) || "remove".equalsIgnoreCase(☃[1])) {
                  if (☃.length == 3) {
                     return getListOfStringsMatchingLastWord(☃, this.getScoreboard(☃).getTeamNames());
                  }
               } else if ("option".equalsIgnoreCase(☃[1])) {
                  if (☃.length == 3) {
                     return getListOfStringsMatchingLastWord(☃, this.getScoreboard(☃).getTeamNames());
                  }

                  if (☃.length == 4) {
                     return getListOfStringsMatchingLastWord(
                        ☃, new String[]{"color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility", "collisionRule"}
                     );
                  }

                  if (☃.length == 5) {
                     if ("color".equalsIgnoreCase(☃[3])) {
                        return getListOfStringsMatchingLastWord(☃, TextFormatting.getValidValues(true, false));
                     }

                     if ("nametagVisibility".equalsIgnoreCase(☃[3]) || "deathMessageVisibility".equalsIgnoreCase(☃[3])) {
                        return getListOfStringsMatchingLastWord(☃, Team.EnumVisible.getNames());
                     }

                     if ("collisionRule".equalsIgnoreCase(☃[3])) {
                        return getListOfStringsMatchingLastWord(☃, Team.CollisionRule.getNames());
                     }

                     if ("friendlyfire".equalsIgnoreCase(☃[3]) || "seeFriendlyInvisibles".equalsIgnoreCase(☃[3])) {
                        return getListOfStringsMatchingLastWord(☃, new String[]{"true", "false"});
                     }
                  }
               }
            }
         }

         return Collections.emptyList();
      }
   }

   protected List<String> getObjectiveNames(boolean var1, MinecraftServer var2) {
      Collection<ScoreObjective> ☃ = this.getScoreboard(☃).getScoreObjectives();
      List<String> ☃x = Lists.newArrayList();

      for (ScoreObjective ☃xx : ☃) {
         if (!☃ || !☃xx.getCriteria().isReadOnly()) {
            ☃x.add(☃xx.getName());
         }
      }

      return ☃x;
   }

   protected List<String> getTriggerNames(MinecraftServer var1) {
      Collection<ScoreObjective> ☃ = this.getScoreboard(☃).getScoreObjectives();
      List<String> ☃x = Lists.newArrayList();

      for (ScoreObjective ☃xx : ☃) {
         if (☃xx.getCriteria() == IScoreCriteria.TRIGGER) {
            ☃x.add(☃xx.getName());
         }
      }

      return ☃x;
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      if (!"players".equalsIgnoreCase(☃[0])) {
         return "teams".equalsIgnoreCase(☃[0]) ? ☃ == 2 : false;
      } else {
         return ☃.length > 1 && "operation".equalsIgnoreCase(☃[1]) ? ☃ == 2 || ☃ == 5 : ☃ == 2;
      }
   }
}
