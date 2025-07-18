package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RecipeCommand extends CommandBase {
   @Override
   public String getName() {
      return "recipe";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.recipe.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.recipe.usage");
      } else {
         boolean ☃ = "give".equalsIgnoreCase(☃[0]);
         boolean ☃x = "take".equalsIgnoreCase(☃[0]);
         if (!☃ && !☃x) {
            throw new WrongUsageException("commands.recipe.usage");
         } else {
            for (EntityPlayerMP ☃xx : getPlayers(☃, ☃, ☃[1])) {
               if ("*".equals(☃[2])) {
                  if (☃) {
                     ☃xx.unlockRecipes(this.getRecipes());
                     notifyCommandListener(☃, this, "commands.recipe.give.success.all", new Object[]{☃xx.getName()});
                  } else {
                     ☃xx.resetRecipes(this.getRecipes());
                     notifyCommandListener(☃, this, "commands.recipe.take.success.all", new Object[]{☃xx.getName()});
                  }
               } else {
                  IRecipe ☃xxx = CraftingManager.getRecipe(new ResourceLocation(☃[2]));
                  if (☃xxx == null) {
                     throw new CommandException("commands.recipe.unknownrecipe", ☃[2]);
                  }

                  if (☃xxx.isDynamic()) {
                     throw new CommandException("commands.recipe.unsupported", ☃[2]);
                  }

                  List<IRecipe> ☃xxxx = Lists.newArrayList(new IRecipe[]{☃xxx});
                  if (☃ == ☃xx.getRecipeBook().isUnlocked(☃xxx)) {
                     String ☃xxxxx = ☃ ? "commands.recipe.alreadyHave" : "commands.recipe.dontHave";
                     throw new CommandException(☃xxxxx, ☃xx.getName(), ☃xxx.getRecipeOutput().getDisplayName());
                  }

                  if (☃) {
                     ☃xx.unlockRecipes(☃xxxx);
                     notifyCommandListener(☃, this, "commands.recipe.give.success.one", new Object[]{☃xx.getName(), ☃xxx.getRecipeOutput().getDisplayName()});
                  } else {
                     ☃xx.resetRecipes(☃xxxx);
                     notifyCommandListener(☃, this, "commands.recipe.take.success.one", new Object[]{☃xxx.getRecipeOutput().getDisplayName(), ☃xx.getName()});
                  }
               }
            }
         }
      }
   }

   private List<IRecipe> getRecipes() {
      return Lists.newArrayList(CraftingManager.REGISTRY);
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"give", "take"});
      } else if (☃.length == 2) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else {
         return ☃.length == 3 ? getListOfStringsMatchingLastWord(☃, CraftingManager.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }
}
