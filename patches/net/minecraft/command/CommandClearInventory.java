package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClearInventory extends CommandBase {
   @Override
   public String getName() {
      return "clear";
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.clear.usage";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      EntityPlayerMP ☃ = ☃.length == 0 ? getCommandSenderAsPlayer(☃) : getPlayer(☃, ☃, ☃[0]);
      Item ☃x = ☃.length >= 2 ? getItemByText(☃, ☃[1]) : null;
      int ☃xx = ☃.length >= 3 ? parseInt(☃[2], -1) : -1;
      int ☃xxx = ☃.length >= 4 ? parseInt(☃[3], -1) : -1;
      NBTTagCompound ☃xxxx = null;
      if (☃.length >= 5) {
         try {
            ☃xxxx = JsonToNBT.getTagFromJson(buildString(☃, 4));
         } catch (NBTException var10) {
            throw new CommandException("commands.clear.tagError", var10.getMessage());
         }
      }

      if (☃.length >= 2 && ☃x == null) {
         throw new CommandException("commands.clear.failure", ☃.getName());
      } else {
         int ☃xxxxx = ☃.inventory.clearMatchingItems(☃x, ☃xx, ☃xxx, ☃xxxx);
         ☃.inventoryContainer.detectAndSendChanges();
         if (!☃.capabilities.isCreativeMode) {
            ☃.updateHeldItem();
         }

         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, ☃xxxxx);
         if (☃xxxxx == 0) {
            throw new CommandException("commands.clear.failure", ☃.getName());
         } else {
            if (☃xxx == 0) {
               ☃.sendMessage(new TextComponentTranslation("commands.clear.testing", ☃.getName(), ☃xxxxx));
            } else {
               notifyCommandListener(☃, this, "commands.clear.success", new Object[]{☃.getName(), ☃xxxxx});
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else {
         return ☃.length == 2 ? getListOfStringsMatchingLastWord(☃, Item.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃ == 0;
   }
}
