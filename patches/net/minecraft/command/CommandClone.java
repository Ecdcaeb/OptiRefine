package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone extends CommandBase {
   @Override
   public String getName() {
      return "clone";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.clone.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 9) {
         throw new WrongUsageException("commands.clone.usage");
      } else {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos ☃ = parseBlockPos(☃, ☃, 0, false);
         BlockPos ☃x = parseBlockPos(☃, ☃, 3, false);
         BlockPos ☃xx = parseBlockPos(☃, ☃, 6, false);
         StructureBoundingBox ☃xxx = new StructureBoundingBox(☃, ☃x);
         StructureBoundingBox ☃xxxx = new StructureBoundingBox(☃xx, ☃xx.add(☃xxx.getLength()));
         int ☃xxxxx = ☃xxx.getXSize() * ☃xxx.getYSize() * ☃xxx.getZSize();
         if (☃xxxxx > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", ☃xxxxx, 32768);
         } else {
            boolean ☃xxxxxx = false;
            Block ☃xxxxxxx = null;
            Predicate<IBlockState> ☃xxxxxxxx = null;
            if ((☃.length < 11 || !"force".equals(☃[10]) && !"move".equals(☃[10])) && ☃xxx.intersectsWith(☃xxxx)) {
               throw new CommandException("commands.clone.noOverlap");
            } else {
               if (☃.length >= 11 && "move".equals(☃[10])) {
                  ☃xxxxxx = true;
               }

               if (☃xxx.minY >= 0 && ☃xxx.maxY < 256 && ☃xxxx.minY >= 0 && ☃xxxx.maxY < 256) {
                  World ☃xxxxxxxxx = ☃.getEntityWorld();
                  if (☃xxxxxxxxx.isAreaLoaded(☃xxx) && ☃xxxxxxxxx.isAreaLoaded(☃xxxx)) {
                     boolean ☃xxxxxxxxxx = false;
                     if (☃.length >= 10) {
                        if ("masked".equals(☃[9])) {
                           ☃xxxxxxxxxx = true;
                        } else if ("filtered".equals(☃[9])) {
                           if (☃.length < 12) {
                              throw new WrongUsageException("commands.clone.usage");
                           }

                           ☃xxxxxxx = getBlockByText(☃, ☃[11]);
                           if (☃.length >= 13) {
                              ☃xxxxxxxx = convertArgToBlockStatePredicate(☃xxxxxxx, ☃[12]);
                           }
                        }
                     }

                     List<CommandClone.StaticCloneData> ☃xxxxxxxxxxx = Lists.newArrayList();
                     List<CommandClone.StaticCloneData> ☃xxxxxxxxxxxx = Lists.newArrayList();
                     List<CommandClone.StaticCloneData> ☃xxxxxxxxxxxxx = Lists.newArrayList();
                     Deque<BlockPos> ☃xxxxxxxxxxxxxx = Lists.newLinkedList();
                     BlockPos ☃xxxxxxxxxxxxxxx = new BlockPos(☃xxxx.minX - ☃xxx.minX, ☃xxxx.minY - ☃xxx.minY, ☃xxxx.minZ - ☃xxx.minZ);

                     for (int ☃xxxxxxxxxxxxxxxx = ☃xxx.minZ; ☃xxxxxxxxxxxxxxxx <= ☃xxx.maxZ; ☃xxxxxxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxxxxxx = ☃xxx.minY; ☃xxxxxxxxxxxxxxxxx <= ☃xxx.maxY; ☃xxxxxxxxxxxxxxxxx++) {
                           for (int ☃xxxxxxxxxxxxxxxxxx = ☃xxx.minX; ☃xxxxxxxxxxxxxxxxxx <= ☃xxx.maxX; ☃xxxxxxxxxxxxxxxxxx++) {
                              BlockPos ☃xxxxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
                              BlockPos ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.add(☃xxxxxxxxxxxxxxx);
                              IBlockState ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx.getBlockState(☃xxxxxxxxxxxxxxxxxxx);
                              if ((!☃xxxxxxxxxx || ☃xxxxxxxxxxxxxxxxxxxxx.getBlock() != Blocks.AIR)
                                 && (
                                    ☃xxxxxxx == null
                                       || ☃xxxxxxxxxxxxxxxxxxxxx.getBlock() == ☃xxxxxxx && (☃xxxxxxxx == null || ☃xxxxxxxx.apply(☃xxxxxxxxxxxxxxxxxxxxx))
                                 )) {
                                 TileEntity ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx.getTileEntity(☃xxxxxxxxxxxxxxxxxxx);
                                 if (☃xxxxxxxxxxxxxxxxxxxxxx != null) {
                                    NBTTagCompound ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxx.writeToNBT(new NBTTagCompound());
                                    ☃xxxxxxxxxxxx.add(new CommandClone.StaticCloneData(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx));
                                    ☃xxxxxxxxxxxxxx.addLast(☃xxxxxxxxxxxxxxxxxxx);
                                 } else if (!☃xxxxxxxxxxxxxxxxxxxxx.isFullBlock() && !☃xxxxxxxxxxxxxxxxxxxxx.isFullCube()) {
                                    ☃xxxxxxxxxxxxx.add(new CommandClone.StaticCloneData(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, null));
                                    ☃xxxxxxxxxxxxxx.addFirst(☃xxxxxxxxxxxxxxxxxxx);
                                 } else {
                                    ☃xxxxxxxxxxx.add(new CommandClone.StaticCloneData(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx, null));
                                    ☃xxxxxxxxxxxxxx.addLast(☃xxxxxxxxxxxxxxxxxxx);
                                 }
                              }
                           }
                        }
                     }

                     if (☃xxxxxx) {
                        for (BlockPos ☃xxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxx) {
                           TileEntity ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxx.getTileEntity(☃xxxxxxxxxxxxxxxx);
                           if (☃xxxxxxxxxxxxxxxxx instanceof IInventory) {
                              ((IInventory)☃xxxxxxxxxxxxxxxxx).clear();
                           }

                           ☃xxxxxxxxx.setBlockState(☃xxxxxxxxxxxxxxxx, Blocks.BARRIER.getDefaultState(), 2);
                        }

                        for (BlockPos ☃xxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxx) {
                           ☃xxxxxxxxx.setBlockState(☃xxxxxxxxxxxxxxxx, Blocks.AIR.getDefaultState(), 3);
                        }
                     }

                     List<CommandClone.StaticCloneData> ☃xxxxxxxxxxxxxxxx = Lists.newArrayList();
                     ☃xxxxxxxxxxxxxxxx.addAll(☃xxxxxxxxxxx);
                     ☃xxxxxxxxxxxxxxxx.addAll(☃xxxxxxxxxxxx);
                     ☃xxxxxxxxxxxxxxxx.addAll(☃xxxxxxxxxxxxx);
                     List<CommandClone.StaticCloneData> ☃xxxxxxxxxxxxxxxxx = Lists.reverse(☃xxxxxxxxxxxxxxxx);

                     for (CommandClone.StaticCloneData ☃xxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxxx) {
                        TileEntity ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx.getTileEntity(☃xxxxxxxxxxxxxxxxxxx.pos);
                        if (☃xxxxxxxxxxxxxxxxxxxx instanceof IInventory) {
                           ((IInventory)☃xxxxxxxxxxxxxxxxxxxx).clear();
                        }

                        ☃xxxxxxxxx.setBlockState(☃xxxxxxxxxxxxxxxxxxx.pos, Blocks.BARRIER.getDefaultState(), 2);
                     }

                     ☃xxxxx = 0;

                     for (CommandClone.StaticCloneData ☃xxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxx) {
                        if (☃xxxxxxxxx.setBlockState(☃xxxxxxxxxxxxxxxxxxx.pos, ☃xxxxxxxxxxxxxxxxxxx.blockState, 2)) {
                           ☃xxxxx++;
                        }
                     }

                     for (CommandClone.StaticCloneData ☃xxxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxx) {
                        TileEntity ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx.getTileEntity(☃xxxxxxxxxxxxxxxxxxxx.pos);
                        if (☃xxxxxxxxxxxxxxxxxxxx.nbt != null && ☃xxxxxxxxxxxxxxxxxxxxx != null) {
                           ☃xxxxxxxxxxxxxxxxxxxx.nbt.setInteger("x", ☃xxxxxxxxxxxxxxxxxxxx.pos.getX());
                           ☃xxxxxxxxxxxxxxxxxxxx.nbt.setInteger("y", ☃xxxxxxxxxxxxxxxxxxxx.pos.getY());
                           ☃xxxxxxxxxxxxxxxxxxxx.nbt.setInteger("z", ☃xxxxxxxxxxxxxxxxxxxx.pos.getZ());
                           ☃xxxxxxxxxxxxxxxxxxxxx.readFromNBT(☃xxxxxxxxxxxxxxxxxxxx.nbt);
                           ☃xxxxxxxxxxxxxxxxxxxxx.markDirty();
                        }

                        ☃xxxxxxxxx.setBlockState(☃xxxxxxxxxxxxxxxxxxxx.pos, ☃xxxxxxxxxxxxxxxxxxxx.blockState, 2);
                     }

                     for (CommandClone.StaticCloneData ☃xxxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxxx) {
                        ☃xxxxxxxxx.notifyNeighborsRespectDebug(☃xxxxxxxxxxxxxxxxxxxx.pos, ☃xxxxxxxxxxxxxxxxxxxx.blockState.getBlock(), false);
                     }

                     List<NextTickListEntry> ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx.getPendingBlockUpdates(☃xxx, false);
                     if (☃xxxxxxxxxxxxxxxxxxxx != null) {
                        for (NextTickListEntry ☃xxxxxxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxxxxxxx) {
                           if (☃xxx.isVecInside(☃xxxxxxxxxxxxxxxxxxxxx.position)) {
                              BlockPos ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx.position.add(☃xxxxxxxxxxxxxxx);
                              ☃xxxxxxxxx.scheduleBlockUpdate(
                                 ☃xxxxxxxxxxxxxxxxxxxxxx,
                                 ☃xxxxxxxxxxxxxxxxxxxxx.getBlock(),
                                 (int)(☃xxxxxxxxxxxxxxxxxxxxx.scheduledTime - ☃xxxxxxxxx.getWorldInfo().getWorldTotalTime()),
                                 ☃xxxxxxxxxxxxxxxxxxxxx.priority
                              );
                           }
                        }
                     }

                     if (☃xxxxx <= 0) {
                        throw new CommandException("commands.clone.failed");
                     } else {
                        ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, ☃xxxxx);
                        notifyCommandListener(☃, this, "commands.clone.success", new Object[]{☃xxxxx});
                     }
                  } else {
                     throw new CommandException("commands.clone.outOfWorld");
                  }
               } else {
                  throw new CommandException("commands.clone.outOfWorld");
               }
            }
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
      } else if (☃.length == 10) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"replace", "masked", "filtered"});
      } else if (☃.length == 11) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"normal", "force", "move"});
      } else {
         return ☃.length == 12 && "filtered".equals(☃[9]) ? getListOfStringsMatchingLastWord(☃, Block.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }

   static class StaticCloneData {
      public final BlockPos pos;
      public final IBlockState blockState;
      public final NBTTagCompound nbt;

      public StaticCloneData(BlockPos var1, IBlockState var2, NBTTagCompound var3) {
         this.pos = ☃;
         this.blockState = ☃;
         this.nbt = ☃;
      }
   }
}
