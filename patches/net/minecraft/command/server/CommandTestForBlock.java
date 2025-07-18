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
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandTestForBlock extends CommandBase {
   @Override
   public String getName() {
      return "testforblock";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.testforblock.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 4) {
         throw new WrongUsageException("commands.testforblock.usage");
      } else {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos ☃ = parseBlockPos(☃, ☃, 0, false);
         Block ☃x = getBlockByText(☃, ☃[3]);
         if (☃x == null) {
            throw new NumberInvalidException("commands.setblock.notFound", ☃[3]);
         } else {
            World ☃xx = ☃.getEntityWorld();
            if (!☃xx.isBlockLoaded(☃)) {
               throw new CommandException("commands.testforblock.outOfWorld");
            } else {
               NBTTagCompound ☃xxx = new NBTTagCompound();
               boolean ☃xxxx = false;
               if (☃.length >= 6 && ☃x.hasTileEntity()) {
                  String ☃xxxxx = buildString(☃, 5);

                  try {
                     ☃xxx = JsonToNBT.getTagFromJson(☃xxxxx);
                     ☃xxxx = true;
                  } catch (NBTException var14) {
                     throw new CommandException("commands.setblock.tagError", var14.getMessage());
                  }
               }

               IBlockState ☃xxxxx = ☃xx.getBlockState(☃);
               Block ☃xxxxxx = ☃xxxxx.getBlock();
               if (☃xxxxxx != ☃x) {
                  throw new CommandException(
                     "commands.testforblock.failed.tile", ☃.getX(), ☃.getY(), ☃.getZ(), ☃xxxxxx.getLocalizedName(), ☃x.getLocalizedName()
                  );
               } else if (☃.length >= 5 && !CommandBase.convertArgToBlockStatePredicate(☃x, ☃[4]).apply(☃xxxxx)) {
                  try {
                     int ☃xxxxxxx = ☃xxxxx.getBlock().getMetaFromState(☃xxxxx);
                     throw new CommandException("commands.testforblock.failed.data", ☃.getX(), ☃.getY(), ☃.getZ(), ☃xxxxxxx, Integer.parseInt(☃[4]));
                  } catch (NumberFormatException var13) {
                     throw new CommandException("commands.testforblock.failed.data", ☃.getX(), ☃.getY(), ☃.getZ(), ☃xxxxx.toString(), ☃[4]);
                  }
               } else {
                  if (☃xxxx) {
                     TileEntity ☃xxxxxxx = ☃xx.getTileEntity(☃);
                     if (☃xxxxxxx == null) {
                        throw new CommandException("commands.testforblock.failed.tileEntity", ☃.getX(), ☃.getY(), ☃.getZ());
                     }

                     NBTTagCompound ☃xxxxxxxx = ☃xxxxxxx.writeToNBT(new NBTTagCompound());
                     if (!NBTUtil.areNBTEquals(☃xxx, ☃xxxxxxxx, true)) {
                        throw new CommandException("commands.testforblock.failed.nbt", ☃.getX(), ☃.getY(), ☃.getZ());
                     }
                  }

                  ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                  notifyCommandListener(☃, this, "commands.testforblock.success", new Object[]{☃.getX(), ☃.getY(), ☃.getZ()});
               }
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length > 0 && ☃.length <= 3) {
         return getTabCompletionCoordinate(☃, 0, ☃);
      } else {
         return ☃.length == 4 ? getListOfStringsMatchingLastWord(☃, Block.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }
}
