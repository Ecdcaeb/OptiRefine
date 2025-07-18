package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTestFor extends CommandBase {
   @Override
   public String getName() {
      return "testfor";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.testfor.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.testfor.usage");
      } else {
         Entity ☃ = getEntity(☃, ☃, ☃[0]);
         NBTTagCompound ☃x = null;
         if (☃.length >= 2) {
            try {
               ☃x = JsonToNBT.getTagFromJson(buildString(☃, 1));
            } catch (NBTException var7) {
               throw new CommandException("commands.testfor.tagError", var7.getMessage());
            }
         }

         if (☃x != null) {
            NBTTagCompound ☃xx = entityToNBT(☃);
            if (!NBTUtil.areNBTEquals(☃x, ☃xx, true)) {
               throw new CommandException("commands.testfor.failure", ☃.getName());
            }
         }

         notifyCommandListener(☃, this, "commands.testfor.success", new Object[]{☃.getName()});
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length == 1 ? getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames()) : Collections.emptyList();
   }
}
