package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AdvancementCommand extends CommandBase {
   @Override
   public String getName() {
      return "advancement";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.advancement.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.advancement.usage");
      } else {
         AdvancementCommand.ActionType ☃ = AdvancementCommand.ActionType.byName(☃[0]);
         if (☃ != null) {
            if (☃.length < 3) {
               throw ☃.wrongUsage();
            }

            EntityPlayerMP ☃x = getPlayer(☃, ☃, ☃[1]);
            AdvancementCommand.Mode ☃xx = AdvancementCommand.Mode.byName(☃[2]);
            if (☃xx == null) {
               throw ☃.wrongUsage();
            }

            this.perform(☃, ☃, ☃, ☃x, ☃, ☃xx);
         } else {
            if (!"test".equals(☃[0])) {
               throw new WrongUsageException("commands.advancement.usage");
            }

            if (☃.length == 3) {
               this.testAdvancement(☃, getPlayer(☃, ☃, ☃[1]), findAdvancement(☃, ☃[2]));
            } else {
               if (☃.length != 4) {
                  throw new WrongUsageException("commands.advancement.test.usage");
               }

               this.testCriterion(☃, getPlayer(☃, ☃, ☃[1]), findAdvancement(☃, ☃[2]), ☃[3]);
            }
         }
      }
   }

   private void perform(
      MinecraftServer var1, ICommandSender var2, String[] var3, EntityPlayerMP var4, AdvancementCommand.ActionType var5, AdvancementCommand.Mode var6
   ) throws CommandException {
      if (☃ == AdvancementCommand.Mode.EVERYTHING) {
         if (☃.length == 3) {
            int ☃ = ☃.perform(☃, ☃.getAdvancementManager().getAdvancements());
            if (☃ == 0) {
               throw ☃.fail(☃, ☃.getName());
            } else {
               ☃.success(☃, this, ☃, ☃.getName(), ☃);
            }
         } else {
            throw ☃.usage(☃);
         }
      } else if (☃.length < 4) {
         throw ☃.usage(☃);
      } else {
         Advancement ☃ = findAdvancement(☃, ☃[3]);
         if (☃ == AdvancementCommand.Mode.ONLY && ☃.length == 5) {
            String ☃x = ☃[4];
            if (!☃.getCriteria().keySet().contains(☃x)) {
               throw new CommandException("commands.advancement.criterionNotFound", ☃.getId(), ☃[4]);
            }

            if (!☃.performCriterion(☃, ☃, ☃x)) {
               throw new CommandException(☃.baseTranslationKey + ".criterion.failed", ☃.getId(), ☃.getName(), ☃x);
            }

            notifyCommandListener(☃, this, ☃.baseTranslationKey + ".criterion.success", new Object[]{☃.getId(), ☃.getName(), ☃x});
         } else {
            if (☃.length != 4) {
               throw ☃.usage(☃);
            }

            List<Advancement> ☃xx = this.getAdvancements(☃, ☃);
            int ☃xxx = ☃.perform(☃, ☃xx);
            if (☃xxx == 0) {
               throw ☃.fail(☃, ☃.getId(), ☃.getName());
            }

            ☃.success(☃, this, ☃, ☃.getId(), ☃.getName(), ☃xxx);
         }
      }
   }

   private void addChildren(Advancement var1, List<Advancement> var2) {
      for (Advancement ☃ : ☃.getChildren()) {
         ☃.add(☃);
         this.addChildren(☃, ☃);
      }
   }

   private List<Advancement> getAdvancements(Advancement var1, AdvancementCommand.Mode var2) {
      List<Advancement> ☃ = Lists.newArrayList();
      if (☃.parents) {
         for (Advancement ☃x = ☃.getParent(); ☃x != null; ☃x = ☃x.getParent()) {
            ☃.add(☃x);
         }
      }

      ☃.add(☃);
      if (☃.children) {
         this.addChildren(☃, ☃);
      }

      return ☃;
   }

   private void testCriterion(ICommandSender var1, EntityPlayerMP var2, Advancement var3, String var4) throws CommandException {
      PlayerAdvancements ☃ = ☃.getAdvancements();
      CriterionProgress ☃x = ☃.getProgress(☃).getCriterionProgress(☃);
      if (☃x == null) {
         throw new CommandException("commands.advancement.criterionNotFound", ☃.getId(), ☃);
      } else if (!☃x.isObtained()) {
         throw new CommandException("commands.advancement.test.criterion.notDone", ☃.getName(), ☃.getId(), ☃);
      } else {
         notifyCommandListener(☃, this, "commands.advancement.test.criterion.success", new Object[]{☃.getName(), ☃.getId(), ☃});
      }
   }

   private void testAdvancement(ICommandSender var1, EntityPlayerMP var2, Advancement var3) throws CommandException {
      AdvancementProgress ☃ = ☃.getAdvancements().getProgress(☃);
      if (!☃.isDone()) {
         throw new CommandException("commands.advancement.test.advancement.notDone", ☃.getName(), ☃.getId());
      } else {
         notifyCommandListener(☃, this, "commands.advancement.test.advancement.success", new Object[]{☃.getName(), ☃.getId()});
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"grant", "revoke", "test"});
      } else {
         AdvancementCommand.ActionType ☃ = AdvancementCommand.ActionType.byName(☃[0]);
         if (☃ != null) {
            if (☃.length == 2) {
               return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
            }

            if (☃.length == 3) {
               return getListOfStringsMatchingLastWord(☃, AdvancementCommand.Mode.NAMES);
            }

            AdvancementCommand.Mode ☃x = AdvancementCommand.Mode.byName(☃[2]);
            if (☃x != null && ☃x != AdvancementCommand.Mode.EVERYTHING) {
               if (☃.length == 4) {
                  return getListOfStringsMatchingLastWord(☃, this.getAdvancementNames(☃));
               }

               if (☃.length == 5 && ☃x == AdvancementCommand.Mode.ONLY) {
                  Advancement ☃xx = ☃.getAdvancementManager().getAdvancement(new ResourceLocation(☃[3]));
                  if (☃xx != null) {
                     return getListOfStringsMatchingLastWord(☃, ☃xx.getCriteria().keySet());
                  }
               }
            }
         }

         if ("test".equals(☃[0])) {
            if (☃.length == 2) {
               return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
            }

            if (☃.length == 3) {
               return getListOfStringsMatchingLastWord(☃, this.getAdvancementNames(☃));
            }

            if (☃.length == 4) {
               Advancement ☃x = ☃.getAdvancementManager().getAdvancement(new ResourceLocation(☃[2]));
               if (☃x != null) {
                  return getListOfStringsMatchingLastWord(☃, ☃x.getCriteria().keySet());
               }
            }
         }

         return Collections.emptyList();
      }
   }

   private List<ResourceLocation> getAdvancementNames(MinecraftServer var1) {
      List<ResourceLocation> ☃ = Lists.newArrayList();

      for (Advancement ☃x : ☃.getAdvancementManager().getAdvancements()) {
         ☃.add(☃x.getId());
      }

      return ☃;
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃.length > 1 && ("grant".equals(☃[0]) || "revoke".equals(☃[0]) || "test".equals(☃[0])) && ☃ == 1;
   }

   public static Advancement findAdvancement(MinecraftServer var0, String var1) throws CommandException {
      Advancement ☃ = ☃.getAdvancementManager().getAdvancement(new ResourceLocation(☃));
      if (☃ == null) {
         throw new CommandException("commands.advancement.advancementNotFound", ☃);
      } else {
         return ☃;
      }
   }

   static enum ActionType {
      GRANT("grant") {
         @Override
         protected boolean perform(EntityPlayerMP var1, Advancement var2) {
            AdvancementProgress ☃ = ☃.getAdvancements().getProgress(☃);
            if (☃.isDone()) {
               return false;
            } else {
               for (String ☃x : ☃.getRemaningCriteria()) {
                  ☃.getAdvancements().grantCriterion(☃, ☃x);
               }

               return true;
            }
         }

         @Override
         protected boolean performCriterion(EntityPlayerMP var1, Advancement var2, String var3) {
            return ☃.getAdvancements().grantCriterion(☃, ☃);
         }
      },
      REVOKE("revoke") {
         @Override
         protected boolean perform(EntityPlayerMP var1, Advancement var2) {
            AdvancementProgress ☃ = ☃.getAdvancements().getProgress(☃);
            if (!☃.hasProgress()) {
               return false;
            } else {
               for (String ☃x : ☃.getCompletedCriteria()) {
                  ☃.getAdvancements().revokeCriterion(☃, ☃x);
               }

               return true;
            }
         }

         @Override
         protected boolean performCriterion(EntityPlayerMP var1, Advancement var2, String var3) {
            return ☃.getAdvancements().revokeCriterion(☃, ☃);
         }
      };

      final String name;
      final String baseTranslationKey;

      private ActionType(String var3) {
         this.name = ☃;
         this.baseTranslationKey = "commands.advancement." + ☃;
      }

      @Nullable
      static AdvancementCommand.ActionType byName(String var0) {
         for (AdvancementCommand.ActionType ☃ : values()) {
            if (☃.name.equals(☃)) {
               return ☃;
            }
         }

         return null;
      }

      CommandException wrongUsage() {
         return new CommandException(this.baseTranslationKey + ".usage");
      }

      public int perform(EntityPlayerMP var1, Iterable<Advancement> var2) {
         int ☃ = 0;

         for (Advancement ☃x : ☃) {
            if (this.perform(☃, ☃x)) {
               ☃++;
            }
         }

         return ☃;
      }

      protected abstract boolean perform(EntityPlayerMP var1, Advancement var2);

      protected abstract boolean performCriterion(EntityPlayerMP var1, Advancement var2, String var3);
   }

   static enum Mode {
      ONLY("only", false, false),
      THROUGH("through", true, true),
      FROM("from", false, true),
      UNTIL("until", true, false),
      EVERYTHING("everything", true, true);

      static final String[] NAMES = new String[values().length];
      final String name;
      final boolean parents;
      final boolean children;

      private Mode(String var3, boolean var4, boolean var5) {
         this.name = ☃;
         this.parents = ☃;
         this.children = ☃;
      }

      CommandException fail(AdvancementCommand.ActionType var1, Object... var2) {
         return new CommandException(☃.baseTranslationKey + "." + this.name + ".failed", ☃);
      }

      CommandException usage(AdvancementCommand.ActionType var1) {
         return new CommandException(☃.baseTranslationKey + "." + this.name + ".usage");
      }

      void success(ICommandSender var1, AdvancementCommand var2, AdvancementCommand.ActionType var3, Object... var4) {
         CommandBase.notifyCommandListener(☃, ☃, ☃.baseTranslationKey + "." + this.name + ".success", ☃);
      }

      @Nullable
      static AdvancementCommand.Mode byName(String var0) {
         for (AdvancementCommand.Mode ☃ : values()) {
            if (☃.name.equals(☃)) {
               return ☃;
            }
         }

         return null;
      }

      static {
         for (int ☃ = 0; ☃ < values().length; ☃++) {
            NAMES[☃] = values()[☃].name;
         }
      }
   }
}
