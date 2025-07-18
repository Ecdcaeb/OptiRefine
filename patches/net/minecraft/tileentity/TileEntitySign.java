package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity {
   public final ITextComponent[] signText = new ITextComponent[]{
      new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("")
   };
   public int lineBeingEdited = -1;
   private boolean isEditable = true;
   private EntityPlayer player;
   private final CommandResultStats stats = new CommandResultStats();

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);

      for (int ☃ = 0; ☃ < 4; ☃++) {
         String ☃x = ITextComponent.Serializer.componentToJson(this.signText[☃]);
         ☃.setString("Text" + (☃ + 1), ☃x);
      }

      this.stats.writeStatsToNBT(☃);
      return ☃;
   }

   @Override
   protected void setWorldCreate(World var1) {
      this.setWorld(☃);
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      this.isEditable = false;
      super.readFromNBT(☃);
      ICommandSender ☃ = new ICommandSender() {
         @Override
         public String getName() {
            return "Sign";
         }

         @Override
         public boolean canUseCommand(int var1, String var2) {
            return true;
         }

         @Override
         public BlockPos getPosition() {
            return TileEntitySign.this.pos;
         }

         @Override
         public Vec3d getPositionVector() {
            return new Vec3d(TileEntitySign.this.pos.getX() + 0.5, TileEntitySign.this.pos.getY() + 0.5, TileEntitySign.this.pos.getZ() + 0.5);
         }

         @Override
         public World getEntityWorld() {
            return TileEntitySign.this.world;
         }

         @Override
         public MinecraftServer getServer() {
            return TileEntitySign.this.world.getMinecraftServer();
         }
      };

      for (int ☃x = 0; ☃x < 4; ☃x++) {
         String ☃xx = ☃.getString("Text" + (☃x + 1));
         ITextComponent ☃xxx = ITextComponent.Serializer.jsonToComponent(☃xx);

         try {
            this.signText[☃x] = TextComponentUtils.processComponent(☃, ☃xxx, null);
         } catch (CommandException var7) {
            this.signText[☃x] = ☃xxx;
         }
      }

      this.stats.readStatsFromNBT(☃);
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 9, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   @Override
   public boolean onlyOpsCanSetNbt() {
      return true;
   }

   public boolean getIsEditable() {
      return this.isEditable;
   }

   public void setEditable(boolean var1) {
      this.isEditable = ☃;
      if (!☃) {
         this.player = null;
      }
   }

   public void setPlayer(EntityPlayer var1) {
      this.player = ☃;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   public boolean executeCommand(final EntityPlayer var1) {
      ICommandSender ☃ = new ICommandSender() {
         @Override
         public String getName() {
            return ☃.getName();
         }

         @Override
         public ITextComponent getDisplayName() {
            return ☃.getDisplayName();
         }

         @Override
         public void sendMessage(ITextComponent var1x) {
         }

         @Override
         public boolean canUseCommand(int var1x, String var2) {
            return ☃ <= 2;
         }

         @Override
         public BlockPos getPosition() {
            return TileEntitySign.this.pos;
         }

         @Override
         public Vec3d getPositionVector() {
            return new Vec3d(TileEntitySign.this.pos.getX() + 0.5, TileEntitySign.this.pos.getY() + 0.5, TileEntitySign.this.pos.getZ() + 0.5);
         }

         @Override
         public World getEntityWorld() {
            return ☃.getEntityWorld();
         }

         @Override
         public Entity getCommandSenderEntity() {
            return ☃;
         }

         @Override
         public boolean sendCommandFeedback() {
            return false;
         }

         @Override
         public void setCommandStat(CommandResultStats.Type var1x, int var2) {
            if (TileEntitySign.this.world != null && !TileEntitySign.this.world.isRemote) {
               TileEntitySign.this.stats.setCommandStatForSender(TileEntitySign.this.world.getMinecraftServer(), this, ☃, ☃);
            }
         }

         @Override
         public MinecraftServer getServer() {
            return ☃.getServer();
         }
      };

      for (ITextComponent ☃x : this.signText) {
         Style ☃xx = ☃x == null ? null : ☃x.getStyle();
         if (☃xx != null && ☃xx.getClickEvent() != null) {
            ClickEvent ☃xxx = ☃xx.getClickEvent();
            if (☃xxx.getAction() == ClickEvent.Action.RUN_COMMAND) {
               ☃.getServer().getCommandManager().executeCommand(☃, ☃xxx.getValue());
            }
         }
      }

      return true;
   }

   public CommandResultStats getStats() {
      return this.stats;
   }
}
