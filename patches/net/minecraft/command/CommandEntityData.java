package net.minecraft.command;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandEntityData extends CommandBase {
   @Override
   public String getName() {
      return "entitydata";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.entitydata.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.entitydata.usage");
      } else {
         Entity ☃ = getEntity(☃, ☃, ☃[0]);
         if (☃ instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", ☃.getDisplayName());
         } else {
            NBTTagCompound ☃x = entityToNBT(☃);
            NBTTagCompound ☃xx = ☃x.copy();

            NBTTagCompound ☃xxx;
            try {
               ☃xxx = JsonToNBT.getTagFromJson(buildString(☃, 1));
            } catch (NBTException var9) {
               throw new CommandException("commands.entitydata.tagError", var9.getMessage());
            }

            UUID ☃xxxx = ☃.getUniqueID();
            ☃x.merge(☃xxx);
            ☃.setUniqueId(☃xxxx);
            if (☃x.equals(☃xx)) {
               throw new CommandException("commands.entitydata.failed", ☃x.toString());
            } else {
               ☃.readFromNBT(☃x);
               notifyCommandListener(☃, this, "commands.entitydata.success", new Object[]{☃x.toString()});
            }
         }
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
