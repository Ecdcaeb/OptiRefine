package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandBlockData extends CommandBase {
   @Override
   public String getName() {
      return "blockdata";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.blockdata.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 4) {
         throw new WrongUsageException("commands.blockdata.usage");
      } else {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos ☃ = parseBlockPos(☃, ☃, 0, false);
         World ☃x = ☃.getEntityWorld();
         if (!☃x.isBlockLoaded(☃)) {
            throw new CommandException("commands.blockdata.outOfWorld");
         } else {
            IBlockState ☃xx = ☃x.getBlockState(☃);
            TileEntity ☃xxx = ☃x.getTileEntity(☃);
            if (☃xxx == null) {
               throw new CommandException("commands.blockdata.notValid");
            } else {
               NBTTagCompound ☃xxxx = ☃xxx.writeToNBT(new NBTTagCompound());
               NBTTagCompound ☃xxxxx = ☃xxxx.copy();

               NBTTagCompound ☃xxxxxx;
               try {
                  ☃xxxxxx = JsonToNBT.getTagFromJson(buildString(☃, 3));
               } catch (NBTException var12) {
                  throw new CommandException("commands.blockdata.tagError", var12.getMessage());
               }

               ☃xxxx.merge(☃xxxxxx);
               ☃xxxx.setInteger("x", ☃.getX());
               ☃xxxx.setInteger("y", ☃.getY());
               ☃xxxx.setInteger("z", ☃.getZ());
               if (☃xxxx.equals(☃xxxxx)) {
                  throw new CommandException("commands.blockdata.failed", ☃xxxx.toString());
               } else {
                  ☃xxx.readFromNBT(☃xxxx);
                  ☃xxx.markDirty();
                  ☃x.notifyBlockUpdate(☃, ☃xx, ☃xx, 3);
                  ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                  notifyCommandListener(☃, this, "commands.blockdata.success", new Object[]{☃xxxx.toString()});
               }
            }
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      return ☃.length > 0 && ☃.length <= 3 ? getTabCompletionCoordinate(☃, 0, ☃) : Collections.emptyList();
   }
}
