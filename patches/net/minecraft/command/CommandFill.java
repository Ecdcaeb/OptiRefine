package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandFill extends CommandBase {
   @Override
   public String getName() {
      return "fill";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getUsage(ICommandSender var1) {
      return "commands.fill.usage";
   }

   @Override
   public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
      if (☃.length < 7) {
         throw new WrongUsageException("commands.fill.usage");
      } else {
         ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos ☃ = parseBlockPos(☃, ☃, 0, false);
         BlockPos ☃x = parseBlockPos(☃, ☃, 3, false);
         Block ☃xx = CommandBase.getBlockByText(☃, ☃[6]);
         IBlockState ☃xxx;
         if (☃.length >= 8) {
            ☃xxx = convertArgToBlockState(☃xx, ☃[7]);
         } else {
            ☃xxx = ☃xx.getDefaultState();
         }

         BlockPos ☃xxxx = new BlockPos(Math.min(☃.getX(), ☃x.getX()), Math.min(☃.getY(), ☃x.getY()), Math.min(☃.getZ(), ☃x.getZ()));
         BlockPos ☃xxxxx = new BlockPos(Math.max(☃.getX(), ☃x.getX()), Math.max(☃.getY(), ☃x.getY()), Math.max(☃.getZ(), ☃x.getZ()));
         int ☃xxxxxx = (☃xxxxx.getX() - ☃xxxx.getX() + 1) * (☃xxxxx.getY() - ☃xxxx.getY() + 1) * (☃xxxxx.getZ() - ☃xxxx.getZ() + 1);
         if (☃xxxxxx > 32768) {
            throw new CommandException("commands.fill.tooManyBlocks", ☃xxxxxx, 32768);
         } else if (☃xxxx.getY() >= 0 && ☃xxxxx.getY() < 256) {
            World ☃xxxxxxx = ☃.getEntityWorld();

            for (int ☃xxxxxxxx = ☃xxxx.getZ(); ☃xxxxxxxx <= ☃xxxxx.getZ(); ☃xxxxxxxx += 16) {
               for (int ☃xxxxxxxxx = ☃xxxx.getX(); ☃xxxxxxxxx <= ☃xxxxx.getX(); ☃xxxxxxxxx += 16) {
                  if (!☃xxxxxxx.isBlockLoaded(new BlockPos(☃xxxxxxxxx, ☃xxxxx.getY() - ☃xxxx.getY(), ☃xxxxxxxx))) {
                     throw new CommandException("commands.fill.outOfWorld");
                  }
               }
            }

            NBTTagCompound ☃xxxxxxxx = new NBTTagCompound();
            boolean ☃xxxxxxxxxx = false;
            if (☃.length >= 10 && ☃xx.hasTileEntity()) {
               String ☃xxxxxxxxxxx = buildString(☃, 9);

               try {
                  ☃xxxxxxxx = JsonToNBT.getTagFromJson(☃xxxxxxxxxxx);
                  ☃xxxxxxxxxx = true;
               } catch (NBTException var21) {
                  throw new CommandException("commands.fill.tagError", var21.getMessage());
               }
            }

            List<BlockPos> ☃xxxxxxxxxxx = Lists.newArrayList();
            ☃xxxxxx = 0;

            for (int ☃xxxxxxxxxxxx = ☃xxxx.getZ(); ☃xxxxxxxxxxxx <= ☃xxxxx.getZ(); ☃xxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxx = ☃xxxx.getY(); ☃xxxxxxxxxxxxx <= ☃xxxxx.getY(); ☃xxxxxxxxxxxxx++) {
                  for (int ☃xxxxxxxxxxxxxx = ☃xxxx.getX(); ☃xxxxxxxxxxxxxx <= ☃xxxxx.getX(); ☃xxxxxxxxxxxxxx++) {
                     BlockPos ☃xxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxx);
                     if (☃.length >= 9) {
                        if (!"outline".equals(☃[8]) && !"hollow".equals(☃[8])) {
                           if ("destroy".equals(☃[8])) {
                              ☃xxxxxxx.destroyBlock(☃xxxxxxxxxxxxxxx, true);
                           } else if ("keep".equals(☃[8])) {
                              if (!☃xxxxxxx.isAirBlock(☃xxxxxxxxxxxxxxx)) {
                                 continue;
                              }
                           } else if ("replace".equals(☃[8]) && !☃xx.hasTileEntity() && ☃.length > 9) {
                              Block ☃xxxxxxxxxxxxxxxx = CommandBase.getBlockByText(☃, ☃[9]);
                              if (☃xxxxxxx.getBlockState(☃xxxxxxxxxxxxxxx).getBlock() != ☃xxxxxxxxxxxxxxxx
                                 || ☃.length > 10
                                    && !"-1".equals(☃[10])
                                    && !"*".equals(☃[10])
                                    && !CommandBase.convertArgToBlockStatePredicate(☃xxxxxxxxxxxxxxxx, ☃[10]).apply(☃xxxxxxx.getBlockState(☃xxxxxxxxxxxxxxx))) {
                                 continue;
                              }
                           }
                        } else if (☃xxxxxxxxxxxxxx != ☃xxxx.getX()
                           && ☃xxxxxxxxxxxxxx != ☃xxxxx.getX()
                           && ☃xxxxxxxxxxxxx != ☃xxxx.getY()
                           && ☃xxxxxxxxxxxxx != ☃xxxxx.getY()
                           && ☃xxxxxxxxxxxx != ☃xxxx.getZ()
                           && ☃xxxxxxxxxxxx != ☃xxxxx.getZ()) {
                           if ("hollow".equals(☃[8])) {
                              ☃xxxxxxx.setBlockState(☃xxxxxxxxxxxxxxx, Blocks.AIR.getDefaultState(), 2);
                              ☃xxxxxxxxxxx.add(☃xxxxxxxxxxxxxxx);
                           }
                           continue;
                        }
                     }

                     TileEntity ☃xxxxxxxxxxxxxxxx = ☃xxxxxxx.getTileEntity(☃xxxxxxxxxxxxxxx);
                     if (☃xxxxxxxxxxxxxxxx != null && ☃xxxxxxxxxxxxxxxx instanceof IInventory) {
                        ((IInventory)☃xxxxxxxxxxxxxxxx).clear();
                     }

                     if (☃xxxxxxx.setBlockState(☃xxxxxxxxxxxxxxx, ☃xxx, 2)) {
                        ☃xxxxxxxxxxx.add(☃xxxxxxxxxxxxxxx);
                        ☃xxxxxx++;
                        if (☃xxxxxxxxxx) {
                           TileEntity ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxx.getTileEntity(☃xxxxxxxxxxxxxxx);
                           if (☃xxxxxxxxxxxxxxxxx != null) {
                              ☃xxxxxxxx.setInteger("x", ☃xxxxxxxxxxxxxxx.getX());
                              ☃xxxxxxxx.setInteger("y", ☃xxxxxxxxxxxxxxx.getY());
                              ☃xxxxxxxx.setInteger("z", ☃xxxxxxxxxxxxxxx.getZ());
                              ☃xxxxxxxxxxxxxxxxx.readFromNBT(☃xxxxxxxx);
                           }
                        }
                     }
                  }
               }
            }

            for (BlockPos ☃xxxxxxxxxxxx : ☃xxxxxxxxxxx) {
               Block ☃xxxxxxxxxxxxx = ☃xxxxxxx.getBlockState(☃xxxxxxxxxxxx).getBlock();
               ☃xxxxxxx.notifyNeighborsRespectDebug(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, false);
            }

            if (☃xxxxxx <= 0) {
               throw new CommandException("commands.fill.failed");
            } else {
               ☃.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, ☃xxxxxx);
               notifyCommandListener(☃, this, "commands.fill.success", new Object[]{☃xxxxxx});
            }
         } else {
            throw new CommandException("commands.fill.outOfWorld");
         }
      }
   }

   @Override
   public List<String> getTabCompletions(MinecraftServer var1, ICommandSender var2, String[] var3, @Nullable BlockPos var4) {
      if (☃.length > 0 && ☃.length <= 3) {
         return getTabCompletionCoordinate(☃, 0, ☃);
      } else if (☃.length > 3 && ☃.length <= 6) {
         return getTabCompletionCoordinate(☃, 3, ☃);
      } else if (☃.length == 7) {
         return getListOfStringsMatchingLastWord(☃, Block.REGISTRY.getKeys());
      } else if (☃.length == 9) {
         return getListOfStringsMatchingLastWord(☃, new String[]{"replace", "destroy", "keep", "hollow", "outline"});
      } else {
         return ☃.length == 10 && "replace".equals(☃[8]) ? getListOfStringsMatchingLastWord(☃, Block.REGISTRY.getKeys()) : Collections.emptyList();
      }
   }
}
