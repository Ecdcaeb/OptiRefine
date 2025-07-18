package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandSetBlock extends CommandBase {
   @Override
   public String getName() {
      return "setblock";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.setblock.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 4) {
         throw new WrongUsageException("commands.setblock.usage");
      } else {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos ☃ = parseBlockPos(☃, ☃, 0, false);
         Block ☃x = CommandBase.getBlockByText(☃, ☃[3]);
         IBlockState ☃xx;
         if (☃.length >= 5) {
            ☃xx = convertArgToBlockState(☃x, ☃[4]);
         } else {
            ☃xx = ☃x.getDefaultState();
         }

         World ☃xxx = ☃.getEntityWorld();
         if (!☃xxx.isBlockLoaded(☃)) {
            throw new CommandException("commands.setblock.outOfWorld");
         } else {
            NBTTagCompound ☃xxxx = new NBTTagCompound();
            boolean ☃xxxxx = false;
            if (☃.length >= 7 && ☃x.hasTileEntity()) {
               String ☃xxxxxx = buildString(☃, 6);

               try {
                  ☃xxxx = JsonToNBT.getTagFromJson(☃xxxxxx);
                  ☃xxxxx = true;
               } catch (NBTException var12) {
                  throw new CommandException("commands.setblock.tagError", var12.getMessage());
               }
            }

            if (☃.length >= 6) {
               if ("destroy".equals(☃[5])) {
                  ☃xxx.destroyBlock(☃, true);
                  if (☃x == Blocks.AIR) {
                     notifyCommandListener(☃, this, "commands.setblock.success", new Object[0]);
                     return;
                  }
               } else if ("keep".equals(☃[5]) && !☃xxx.isAirBlock(☃)) {
                  throw new CommandException("commands.setblock.noChange");
               }
            }

            TileEntity ☃xxxxxx = ☃xxx.getTileEntity(☃);
            if (☃xxxxxx != null && ☃xxxxxx instanceof IInventory) {
               ((IInventory)☃xxxxxx).clear();
            }

            if (!☃xxx.setBlockState(☃, ☃xx, 2)) {
               throw new CommandException("commands.setblock.noChange");
            } else {
               if (☃xxxxx) {
                  TileEntity ☃xxxxxxx = ☃xxx.getTileEntity(☃);
                  if (☃xxxxxxx != null) {
                     ☃xxxx.setInteger("x", ☃.getX());
                     ☃xxxx.setInteger("y", ☃.getY());
                     ☃xxxx.setInteger("z", ☃.getZ());
                     ☃xxxxxxx.readFromNBT(☃xxxx);
                  }
               }

               ☃xxx.notifyNeighborsRespectDebug(☃, ☃xx.getBlock(), false);
               ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
               notifyCommandListener(☃, this, "commands.setblock.success", new Object[0]);
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length > 0 && ☃.length <= 3) {
         return getTabCompletionCoordinate(☃, 0, ☃);
      } else if (☃.length == 4) {
         return getListOfStringsMatchingLastWord(☃, Block.REGISTRY.getKeys());
      } else {
         return ☃.length == 6 ? getListOfStringsMatchingLastWord(☃, new String[]{"replace", "destroy", "keep"}) : Collections.emptyList();
      }
   }
}
