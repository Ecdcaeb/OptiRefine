package net.minecraft.command;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandSenderWrapper implements ICommandSender {
   private final ICommandSender delegate;
   @Nullable
   private final Vec3d positionVector;
   @Nullable
   private final BlockPos position;
   @Nullable
   private final Integer permissionLevel;
   @Nullable
   private final Entity entity;
   @Nullable
   private final Boolean sendCommandFeedback;

   public CommandSenderWrapper(
      ICommandSender var1, @Nullable Vec3d var2, @Nullable BlockPos var3, @Nullable Integer var4, @Nullable Entity var5, @Nullable Boolean var6
   ) {
      this.delegate = ☃;
      this.positionVector = ☃;
      this.position = ☃;
      this.permissionLevel = ☃;
      this.entity = ☃;
      this.sendCommandFeedback = ☃;
   }

   public static CommandSenderWrapper create(ICommandSender var0) {
      return ☃ instanceof CommandSenderWrapper ? (CommandSenderWrapper)☃ : new CommandSenderWrapper(☃, null, null, null, null, null);
   }

   public CommandSenderWrapper withEntity(Entity var1, Vec3d var2) {
      return this.entity == ☃ && Objects.equals(this.positionVector, ☃)
         ? this
         : new CommandSenderWrapper(this.delegate, ☃, new BlockPos(☃), this.permissionLevel, ☃, this.sendCommandFeedback);
   }

   public CommandSenderWrapper withPermissionLevel(int var1) {
      return this.permissionLevel != null && this.permissionLevel <= ☃
         ? this
         : new CommandSenderWrapper(this.delegate, this.positionVector, this.position, ☃, this.entity, this.sendCommandFeedback);
   }

   public CommandSenderWrapper withSendCommandFeedback(boolean var1) {
      return this.sendCommandFeedback == null || this.sendCommandFeedback && !☃
         ? new CommandSenderWrapper(this.delegate, this.positionVector, this.position, this.permissionLevel, this.entity, ☃)
         : this;
   }

   public CommandSenderWrapper computePositionVector() {
      return this.positionVector != null
         ? this
         : new CommandSenderWrapper(this.delegate, this.getPositionVector(), this.getPosition(), this.permissionLevel, this.entity, this.sendCommandFeedback);
   }

   @Override
   public String getName() {
      return this.entity != null ? this.entity.getName() : this.delegate.getName();
   }

   @Override
   public ITextComponent getDisplayName() {
      return this.entity != null ? this.entity.getDisplayName() : this.delegate.getDisplayName();
   }

   @Override
   public void sendMessage(ITextComponent var1) {
      if (this.sendCommandFeedback == null || this.sendCommandFeedback) {
         this.delegate.sendMessage(☃);
      }
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      return this.permissionLevel != null && this.permissionLevel < ☃ ? false : this.delegate.canUseCommand(☃, ☃);
   }

   @Override
   public BlockPos getPosition() {
      if (this.position != null) {
         return this.position;
      } else {
         return this.entity != null ? this.entity.getPosition() : this.delegate.getPosition();
      }
   }

   @Override
   public Vec3d getPositionVector() {
      if (this.positionVector != null) {
         return this.positionVector;
      } else {
         return this.entity != null ? this.entity.getPositionVector() : this.delegate.getPositionVector();
      }
   }

   @Override
   public World getEntityWorld() {
      return this.entity != null ? this.entity.getEntityWorld() : this.delegate.getEntityWorld();
   }

   @Nullable
   @Override
   public Entity getCommandSenderEntity() {
      return this.entity != null ? this.entity.getCommandSenderEntity() : this.delegate.getCommandSenderEntity();
   }

   @Override
   public boolean sendCommandFeedback() {
      return this.sendCommandFeedback != null ? this.sendCommandFeedback : this.delegate.sendCommandFeedback();
   }

   @Override
   public void setCommandStat(CommandResultStats.Type var1, int var2) {
      if (this.entity != null) {
         this.entity.setCommandStat(☃, ☃);
      } else {
         this.delegate.setCommandStat(☃, ☃);
      }
   }

   @Nullable
   @Override
   public MinecraftServer getServer() {
      return this.delegate.getServer();
   }
}
