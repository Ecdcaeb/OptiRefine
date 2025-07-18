package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase {
   @Override
   public String getName() {
      return "testforblocks";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.compare.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 9) {
         throw new WrongUsageException("commands.compare.usage");
      } else {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos ☃ = parseBlockPos(☃, ☃, 0, false);
         BlockPos ☃x = parseBlockPos(☃, ☃, 3, false);
         BlockPos ☃xx = parseBlockPos(☃, ☃, 6, false);
         StructureBoundingBox ☃xxx = new StructureBoundingBox(☃, ☃x);
         StructureBoundingBox ☃xxxx = new StructureBoundingBox(☃xx, ☃xx.add(☃xxx.getLength()));
         int ☃xxxxx = ☃xxx.getXSize() * ☃xxx.getYSize() * ☃xxx.getZSize();
         if (☃xxxxx > 524288) {
            throw new CommandException("commands.compare.tooManyBlocks", ☃xxxxx, 524288);
         } else if (☃xxx.minY >= 0 && ☃xxx.maxY < 256 && ☃xxxx.minY >= 0 && ☃xxxx.maxY < 256) {
            World ☃xxxxxx = ☃.getEntityWorld();
            if (☃xxxxxx.isAreaLoaded(☃xxx) && ☃xxxxxx.isAreaLoaded(☃xxxx)) {
               boolean ☃xxxxxxx = false;
               if (☃.length > 9 && "masked".equals(☃[9])) {
                  ☃xxxxxxx = true;
               }

               ☃xxxxx = 0;
               BlockPos ☃xxxxxxxx = new BlockPos(☃xxxx.minX - ☃xxx.minX, ☃xxxx.minY - ☃xxx.minY, ☃xxxx.minZ - ☃xxx.minZ);
               BlockPos.MutableBlockPos ☃xxxxxxxxx = new BlockPos.MutableBlockPos();
               BlockPos.MutableBlockPos ☃xxxxxxxxxx = new BlockPos.MutableBlockPos();

               for (int ☃xxxxxxxxxxx = ☃xxx.minZ; ☃xxxxxxxxxxx <= ☃xxx.maxZ; ☃xxxxxxxxxxx++) {
                  for (int ☃xxxxxxxxxxxx = ☃xxx.minY; ☃xxxxxxxxxxxx <= ☃xxx.maxY; ☃xxxxxxxxxxxx++) {
                     for (int ☃xxxxxxxxxxxxx = ☃xxx.minX; ☃xxxxxxxxxxxxx <= ☃xxx.maxX; ☃xxxxxxxxxxxxx++) {
                        ☃xxxxxxxxx.setPos(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxx);
                        ☃xxxxxxxxxx.setPos(☃xxxxxxxxxxxxx + ☃xxxxxxxx.getX(), ☃xxxxxxxxxxxx + ☃xxxxxxxx.getY(), ☃xxxxxxxxxxx + ☃xxxxxxxx.getZ());
                        boolean ☃xxxxxxxxxxxxxx = false;
                        IBlockState ☃xxxxxxxxxxxxxxx = ☃xxxxxx.getBlockState(☃xxxxxxxxx);
                        if (!☃xxxxxxx || ☃xxxxxxxxxxxxxxx.getBlock() != Blocks.AIR) {
                           if (☃xxxxxxxxxxxxxxx == ☃xxxxxx.getBlockState(☃xxxxxxxxxx)) {
                              TileEntity ☃xxxxxxxxxxxxxxxx = ☃xxxxxx.getTileEntity(☃xxxxxxxxx);
                              TileEntity ☃xxxxxxxxxxxxxxxxx = ☃xxxxxx.getTileEntity(☃xxxxxxxxxx);
                              if (☃xxxxxxxxxxxxxxxx != null && ☃xxxxxxxxxxxxxxxxx != null) {
                                 NBTTagCompound ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.writeToNBT(new NBTTagCompound());
                                 ☃xxxxxxxxxxxxxxxxxx.removeTag("x");
                                 ☃xxxxxxxxxxxxxxxxxx.removeTag("y");
                                 ☃xxxxxxxxxxxxxxxxxx.removeTag("z");
                                 NBTTagCompound ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.writeToNBT(new NBTTagCompound());
                                 ☃xxxxxxxxxxxxxxxxxxx.removeTag("x");
                                 ☃xxxxxxxxxxxxxxxxxxx.removeTag("y");
                                 ☃xxxxxxxxxxxxxxxxxxx.removeTag("z");
                                 if (!☃xxxxxxxxxxxxxxxxxx.equals(☃xxxxxxxxxxxxxxxxxxx)) {
                                    ☃xxxxxxxxxxxxxx = true;
                                 }
                              } else if (☃xxxxxxxxxxxxxxxx != null) {
                                 ☃xxxxxxxxxxxxxx = true;
                              }
                           } else {
                              ☃xxxxxxxxxxxxxx = true;
                           }

                           ☃xxxxx++;
                           if (☃xxxxxxxxxxxxxx) {
                              throw new CommandException("commands.compare.failed");
                           }
                        }
                     }
                  }
               }

               ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, ☃xxxxx);
               notifyCommandListener(☃, this, "commands.compare.success", new Object[]{☃xxxxx});
            } else {
               throw new CommandException("commands.compare.outOfWorld");
            }
         } else {
            throw new CommandException("commands.compare.outOfWorld");
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length > 0 && ☃.length <= 3) {
         return getTabCompletionCoordinate(☃, 0, ☃);
      } else if (☃.length > 3 && ☃.length <= 6) {
         return getTabCompletionCoordinate(☃, 3, ☃);
      } else if (☃.length > 6 && ☃.length <= 9) {
         return getTabCompletionCoordinate(☃, 6, ☃);
      } else {
         return ☃.length == 10 ? getListOfStringsMatchingLastWord(☃, new String[]{"masked", "all"}) : Collections.emptyList();
      }
   }
}
