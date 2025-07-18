package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandEnchant extends CommandBase {
   @Override
   public String getName() {
      return "enchant";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.enchant.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.enchant.usage");
      } else {
         EntityLivingBase ☃ = getEntity(☃, ☃, ☃[0], EntityLivingBase.class);
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);

         Enchantment ☃x;
         try {
            ☃x = Enchantment.getEnchantmentByID(parseInt(☃[1], 0));
         } catch (NumberInvalidException var12) {
            ☃x = Enchantment.getEnchantmentByLocation(☃[1]);
         }

         if (☃x == null) {
            throw new NumberInvalidException("commands.enchant.notFound", ☃[1]);
         } else {
            int ☃xx = 1;
            ItemStack ☃xxx = ☃.getHeldItemMainhand();
            if (☃xxx.isEmpty()) {
               throw new CommandException("commands.enchant.noItem");
            } else if (!☃x.canApply(☃xxx)) {
               throw new CommandException("commands.enchant.cantEnchant");
            } else {
               if (☃.length >= 3) {
                  ☃xx = parseInt(☃[2], ☃x.getMinLevel(), ☃x.getMaxLevel());
               }

               if (☃xxx.hasTagCompound()) {
                  NBTTagList ☃xxxx = ☃xxx.getEnchantmentTagList();

                  for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.tagCount(); ☃xxxxx++) {
                     int ☃xxxxxx = ☃xxxx.getCompoundTagAt(☃xxxxx).getShort("id");
                     if (Enchantment.getEnchantmentByID(☃xxxxxx) != null) {
                        Enchantment ☃xxxxxxx = Enchantment.getEnchantmentByID(☃xxxxxx);
                        if (!☃x.isCompatibleWith(☃xxxxxxx)) {
                           throw new CommandException(
                              "commands.enchant.cantCombine",
                              ☃x.getTranslatedName(☃xx),
                              ☃xxxxxxx.getTranslatedName(☃xxxx.getCompoundTagAt(☃xxxxx).getShort("lvl"))
                           );
                        }
                     }
                  }
               }

               ☃xxx.addEnchantment(☃x, ☃xx);
               notifyCommandListener(☃, this, "commands.enchant.success", new Object[0]);
               ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else {
         return ☃.length == 2 ? getListOfStringsMatchingLastWord(☃, Enchantment.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
