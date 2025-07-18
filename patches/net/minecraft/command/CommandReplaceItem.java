package net.minecraft.command;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase {
   private static final Map<String, Integer> SHORTCUTS = Maps.newHashMap();

   @Override
   public String getName() {
      return "replaceitem";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.replaceitem.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 1) {
         throw new WrongUsageException("commands.replaceitem.usage");
      } else {
         boolean ☃;
         if ("entity".equals(☃[0])) {
            ☃ = false;
         } else {
            if (!"block".equals(☃[0])) {
               throw new WrongUsageException("commands.replaceitem.usage");
            }

            ☃ = true;
         }

         int ☃x;
         if (☃) {
            if (☃.length < 6) {
               throw new WrongUsageException("commands.replaceitem.block.usage");
            }

            ☃x = 4;
         } else {
            if (☃.length < 4) {
               throw new WrongUsageException("commands.replaceitem.entity.usage");
            }

            ☃x = 2;
         }

         String ☃xx = ☃[☃x];
         int ☃xxx = this.getSlotForShortcut(☃[☃x++]);

         Item ☃xxxx;
         try {
            ☃xxxx = getItemByText(☃, ☃[☃x]);
         } catch (NumberInvalidException var17) {
            if (Block.getBlockFromName(☃[☃x]) != Blocks.AIR) {
               throw var17;
            }

            ☃xxxx = null;
         }

         ☃x++;
         int ☃xxxxx = ☃.length > ☃x ? parseInt(☃[☃x++], 1, ☃xxxx.getItemStackLimit()) : 1;
         int ☃xxxxxx = ☃.length > ☃x ? parseInt(☃[☃x++]) : 0;
         ItemStack ☃xxxxxxx = new ItemStack(☃xxxx, ☃xxxxx, ☃xxxxxx);
         if (☃.length > ☃x) {
            String ☃xxxxxxxx = buildString(☃, ☃x);

            try {
               ☃xxxxxxx.setTagCompound(JsonToNBT.getTagFromJson(☃xxxxxxxx));
            } catch (NBTException var16) {
               throw new CommandException("commands.replaceitem.tagError", var16.getMessage());
            }
         }

         if (☃) {
            ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            BlockPos ☃xxxxxxxx = parseBlockPos(☃, ☃, 1, false);
            World ☃xxxxxxxxx = ☃.getEntityWorld();
            TileEntity ☃xxxxxxxxxx = ☃xxxxxxxxx.getTileEntity(☃xxxxxxxx);
            if (☃xxxxxxxxxx == null || !(☃xxxxxxxxxx instanceof IInventory)) {
               throw new CommandException("commands.replaceitem.noContainer", ☃xxxxxxxx.getX(), ☃xxxxxxxx.getY(), ☃xxxxxxxx.getZ());
            }

            IInventory ☃xxxxxxxxxxx = (IInventory)☃xxxxxxxxxx;
            if (☃xxx >= 0 && ☃xxx < ☃xxxxxxxxxxx.getSizeInventory()) {
               ☃xxxxxxxxxxx.setInventorySlotContents(☃xxx, ☃xxxxxxx);
            }
         } else {
            Entity ☃xxxxxxxxxxx = getEntity(☃, ☃, ☃[1]);
            ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (☃xxxxxxxxxxx instanceof EntityPlayer) {
               ((EntityPlayer)☃xxxxxxxxxxx).inventoryContainer.detectAndSendChanges();
            }

            if (!☃xxxxxxxxxxx.replaceItemInInventory(☃xxx, ☃xxxxxxx)) {
               throw new CommandException("commands.replaceitem.failed", ☃xx, ☃xxxxx, ☃xxxxxxx.isEmpty() ? "Air" : ☃xxxxxxx.getTextComponent());
            }

            if (☃xxxxxxxxxxx instanceof EntityPlayer) {
               ((EntityPlayer)☃xxxxxxxxxxx).inventoryContainer.detectAndSendChanges();
            }
         }

         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, ☃xxxxx);
         notifyCommandListener(☃, this, "commands.replaceitem.success", new Object[]{☃xx, ☃xxxxx, ☃xxxxxxx.isEmpty() ? "Air" : ☃xxxxxxx.getTextComponent()});
      }
   }

   private int getSlotForShortcut(String var1) throws CommandException {
      if (!SHORTCUTS.containsKey(☃)) {
         throw new CommandException("commands.generic.parameter.invalid", ☃);
      } else {
         return SHORTCUTS.get(☃);
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length == 1) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"entity", "block"});
      } else if (☃.length == 2 && "entity".equals(☃[0])) {
         return getListOfStringsMatchingLastWord(☃, ☃.getOnlinePlayerNames());
      } else if (☃.length >= 2 && ☃.length <= 4 && "block".equals(☃[0])) {
         return getTabCompletionCoordinate(☃, 1, ☃);
      } else if ((☃.length != 3 || !"entity".equals(☃[0])) && (☃.length != 5 || !"block".equals(☃[0]))) {
         return (☃.length != 4 || !"entity".equals(☃[0])) && (☃.length != 6 || !"block".equals(☃[0]))
            ? Collections.emptyList()
            : getListOfStringsMatchingLastWord(☃, Item.REGISTRY.getKeys());
      } else {
         return getListOfStringsMatchingLastWord(☃, SHORTCUTS.keySet());
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return ☃.length > 0 && "entity".equals(☃[0]) && ☃ == 1;
   }

   static {
      for (int ☃ = 0; ☃ < 54; ☃++) {
         SHORTCUTS.put("slot.container." + ☃, ☃);
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         SHORTCUTS.put("slot.hotbar." + ☃, ☃);
      }

      for (int ☃ = 0; ☃ < 27; ☃++) {
         SHORTCUTS.put("slot.inventory." + ☃, 9 + ☃);
      }

      for (int ☃ = 0; ☃ < 27; ☃++) {
         SHORTCUTS.put("slot.enderchest." + ☃, 200 + ☃);
      }

      for (int ☃ = 0; ☃ < 8; ☃++) {
         SHORTCUTS.put("slot.villager." + ☃, 300 + ☃);
      }

      for (int ☃ = 0; ☃ < 15; ☃++) {
         SHORTCUTS.put("slot.horse." + ☃, 500 + ☃);
      }

      SHORTCUTS.put("slot.weapon", 98);
      SHORTCUTS.put("slot.weapon.mainhand", 98);
      SHORTCUTS.put("slot.weapon.offhand", 99);
      SHORTCUTS.put("slot.armor.head", 100 + EntityEquipmentSlot.HEAD.getIndex());
      SHORTCUTS.put("slot.armor.chest", 100 + EntityEquipmentSlot.CHEST.getIndex());
      SHORTCUTS.put("slot.armor.legs", 100 + EntityEquipmentSlot.LEGS.getIndex());
      SHORTCUTS.put("slot.armor.feet", 100 + EntityEquipmentSlot.FEET.getIndex());
      SHORTCUTS.put("slot.horse.saddle", 400);
      SHORTCUTS.put("slot.horse.armor", 401);
      SHORTCUTS.put("slot.horse.chest", 499);
   }
}
