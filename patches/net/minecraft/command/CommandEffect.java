package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEffect extends CommandBase {
   @Override
   public String getName() {
      return "effect";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.effect.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.effect.usage");
      } else {
         EntityLivingBase ☃ = getEntity(☃, ☃, ☃[0], EntityLivingBase.class);
         if ("clear".equals(☃[1])) {
            if (☃.getActivePotionEffects().isEmpty()) {
               throw new CommandException("commands.effect.failure.notActive.all", ☃.getName());
            } else {
               ☃.clearActivePotions();
               notifyCommandListener(☃, this, "commands.effect.success.removed.all", new Object[]{☃.getName()});
            }
         } else {
            Potion ☃x;
            try {
               ☃x = Potion.getPotionById(parseInt(☃[1], 1));
            } catch (NumberInvalidException var11) {
               ☃x = Potion.getPotionFromResourceLocation(☃[1]);
            }

            if (☃x == null) {
               throw new NumberInvalidException("commands.effect.notFound", ☃[1]);
            } else {
               int ☃xx = 600;
               int ☃xxx = 30;
               int ☃xxxx = 0;
               if (☃.length >= 3) {
                  ☃xxx = parseInt(☃[2], 0, 1000000);
                  if (☃x.isInstant()) {
                     ☃xx = ☃xxx;
                  } else {
                     ☃xx = ☃xxx * 20;
                  }
               } else if (☃x.isInstant()) {
                  ☃xx = 1;
               }

               if (☃.length >= 4) {
                  ☃xxxx = parseInt(☃[3], 0, 255);
               }

               boolean ☃xxxxx = true;
               if (☃.length >= 5 && "true".equalsIgnoreCase(☃[4])) {
                  ☃xxxxx = false;
               }

               if (☃xxx > 0) {
                  PotionEffect ☃xxxxxx = new PotionEffect(☃x, ☃xx, ☃xxxx, false, ☃xxxxx);
                  ☃.addPotionEffect(☃xxxxxx);
                  notifyCommandListener(
                     ☃,
                     this,
                     "commands.effect.success",
                     new Object[]{new TextComponentTranslation(☃xxxxxx.getEffectName()), Potion.getIdFromPotion(☃x), ☃xxxx, ☃.getName(), ☃xxx}
                  );
               } else if (☃.isPotionActive(☃x)) {
                  ☃.removePotionEffect(☃x);
                  notifyCommandListener(☃, this, "commands.effect.success.removed", new Object[]{new TextComponentTranslation(☃x.getName()), ☃.getName()});
               } else {
                  throw new CommandException("commands.effect.failure.notActive", new TextComponentTranslation(☃x.getName()), ☃.getName());
               }
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else if (☃.length == 2) {
         return getListOfStringsMatchingLastWord(☃, Potion.REGISTRY.getKeys());
      } else {
         return ☃.length == 5 ? getListOfStringsMatchingLastWord(☃, new String[]{"true", "false"}) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
