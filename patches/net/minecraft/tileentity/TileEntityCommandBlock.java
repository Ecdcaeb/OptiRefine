package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity {
   private boolean powered;
   private boolean auto;
   private boolean conditionMet;
   private boolean sendToClient;
   private final CommandBlockBaseLogic commandBlockLogic = new CommandBlockBaseLogic() {
      @Override
      public BlockPos getPosition() {
         return TileEntityCommandBlock.this.pos;
      }

      @Override
      public Vec3d getPositionVector() {
         return new Vec3d(
            TileEntityCommandBlock.this.pos.getX() + 0.5, TileEntityCommandBlock.this.pos.getY() + 0.5, TileEntityCommandBlock.this.pos.getZ() + 0.5
         );
      }

      @Override
      public World getEntityWorld() {
         return TileEntityCommandBlock.this.getWorld();
      }

      @Override
      public void setCommand(String var1) {
         super.setCommand(☃);
         TileEntityCommandBlock.this.markDirty();
      }

      @Override
      public void updateCommand() {
         IBlockState ☃ = TileEntityCommandBlock.this.world.getBlockState(TileEntityCommandBlock.this.pos);
         TileEntityCommandBlock.this.getWorld().notifyBlockUpdate(TileEntityCommandBlock.this.pos, ☃, ☃, 3);
      }

      @Override
      public int getCommandBlockType() {
         return 0;
      }

      @Override
      public void fillInInfo(ByteBuf var1) {
         ☃.writeInt(TileEntityCommandBlock.this.pos.getX());
         ☃.writeInt(TileEntityCommandBlock.this.pos.getY());
         ☃.writeInt(TileEntityCommandBlock.this.pos.getZ());
      }

      @Override
      public MinecraftServer getServer() {
         return TileEntityCommandBlock.this.world.getMinecraftServer();
      }
   };

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      this.commandBlockLogic.writeToNBT(☃);
      ☃.setBoolean("powered", this.isPowered());
      ☃.setBoolean("conditionMet", this.isConditionMet());
      ☃.setBoolean("auto", this.isAuto());
      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.commandBlockLogic.readDataFromNBT(☃);
      this.powered = ☃.getBoolean("powered");
      this.conditionMet = ☃.getBoolean("conditionMet");
      this.setAuto(☃.getBoolean("auto"));
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      if (this.isSendToClient()) {
         this.setSendToClient(false);
         NBTTagCompound ☃ = this.writeToNBT(new NBTTagCompound());
         return new SPacketUpdateTileEntity(this.pos, 2, ☃);
      } else {
         return null;
      }
   }

   @Override
   public boolean onlyOpsCanSetNbt() {
      return true;
   }

   public CommandBlockBaseLogic getCommandBlockLogic() {
      return this.commandBlockLogic;
   }

   public CommandResultStats getCommandResultStats() {
      return this.commandBlockLogic.getCommandResultStats();
   }

   public void setPowered(boolean var1) {
      this.powered = ☃;
   }

   public boolean isPowered() {
      return this.powered;
   }

   public boolean isAuto() {
      return this.auto;
   }

   public void setAuto(boolean var1) {
      boolean ☃ = this.auto;
      this.auto = ☃;
      if (!☃ && ☃ && !this.powered && this.world != null && this.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
         Block ☃x = this.getBlockType();
         if (☃x instanceof BlockCommandBlock) {
            this.setConditionMet();
            this.world.scheduleUpdate(this.pos, ☃x, ☃x.tickRate(this.world));
         }
      }
   }

   public boolean isConditionMet() {
      return this.conditionMet;
   }

   public boolean setConditionMet() {
      this.conditionMet = true;
      if (this.isConditional()) {
         BlockPos ☃ = this.pos.offset(this.world.getBlockState(this.pos).getValue(BlockCommandBlock.FACING).getOpposite());
         if (this.world.getBlockState(☃).getBlock() instanceof BlockCommandBlock) {
            TileEntity ☃x = this.world.getTileEntity(☃);
            this.conditionMet = ☃x instanceof TileEntityCommandBlock && ((TileEntityCommandBlock)☃x).getCommandBlockLogic().getSuccessCount() > 0;
         } else {
            this.conditionMet = false;
         }
      }

      return this.conditionMet;
   }

   public boolean isSendToClient() {
      return this.sendToClient;
   }

   public void setSendToClient(boolean var1) {
      this.sendToClient = ☃;
   }

   public TileEntityCommandBlock.Mode getMode() {
      Block ☃ = this.getBlockType();
      if (☃ == Blocks.COMMAND_BLOCK) {
         return TileEntityCommandBlock.Mode.REDSTONE;
      } else if (☃ == Blocks.REPEATING_COMMAND_BLOCK) {
         return TileEntityCommandBlock.Mode.AUTO;
      } else {
         return ☃ == Blocks.CHAIN_COMMAND_BLOCK ? TileEntityCommandBlock.Mode.SEQUENCE : TileEntityCommandBlock.Mode.REDSTONE;
      }
   }

   public boolean isConditional() {
      IBlockState ☃ = this.world.getBlockState(this.getPos());
      return ☃.getBlock() instanceof BlockCommandBlock ? ☃.getValue(BlockCommandBlock.CONDITIONAL) : false;
   }

   @Override
   public void validate() {
      this.blockType = null;
      super.validate();
   }

   public static enum Mode {
      SEQUENCE,
      AUTO,
      REDSTONE;
   }
}
