package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class CommandGive extends CommandBase {
   @Override
   public String getName() {
      return "give";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.give.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 2) {
         throw new WrongUsageException("commands.give.usage");
      } else {
         EntityPlayer ☃ = getPlayer(☃, ☃, ☃[0]);
         Item ☃x = getItemByText(☃, ☃[1]);
         int ☃xx = ☃.length >= 3 ? parseInt(☃[2], 1, ☃x.getItemStackLimit()) : 1;
         int ☃xxx = ☃.length >= 4 ? parseInt(☃[3]) : 0;
         ItemStack ☃xxxx = new ItemStack(☃x, ☃xx, ☃xxx);
         if (☃.length >= 5) {
            String ☃xxxxx = buildString(☃, 4);

            try {
               ☃xxxx.setTagCompound(JsonToNBT.getTagFromJson(☃xxxxx));
            } catch (NBTException var11) {
               throw new CommandException("commands.give.tagError", var11.getMessage());
            }
         }

         boolean ☃xxxxx = ☃.inventory.addItemStackToInventory(☃xxxx);
         if (☃xxxxx) {
            ☃.world
               .playSound(
                  null,
                  ☃.posX,
                  ☃.posY,
                  ☃.posZ,
                  SoundEvents.ENTITY_ITEM_PICKUP,
                  SoundCategory.PLAYERS,
                  0.2F,
                  ((☃.getRNG().nextFloat() - ☃.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F
               );
            ☃.inventoryContainer.detectAndSendChanges();
         }

         if (☃xxxxx && ☃xxxx.isEmpty()) {
            ☃xxxx.setCount(1);
            ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, ☃xx);
            EntityItem ☃xxxxxx = ☃.dropItem(☃xxxx, false);
            if (☃xxxxxx != null) {
               ☃xxxxxx.makeFakeItem();
            }
         } else {
            ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, ☃xx - ☃xxxx.getCount());
            EntityItem ☃xxxxxx = ☃.dropItem(☃xxxx, false);
            if (☃xxxxxx != null) {
               ☃xxxxxx.setNoPickupDelay();
               ☃xxxxxx.setOwner(☃.getName());
            }
         }

         notifyCommandListener(☃, this, "commands.give.success", new Object[]{☃xxxx.getTextComponent(), ☃xx, ☃.getName()});
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
