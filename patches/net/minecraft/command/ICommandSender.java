package net.minecraft.command;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public interface ICommandSender {
   String getName();

   default ITextComponent getDisplayName() {
      return new TextComponentString(this.getName());
   }

   default void sendMessage(ITextComponent var1) {
   }

   boolean canUseCommand(int var1, String var2);

   default BlockPos getPosition() {
      return BlockPos.ORIGIN;
   }

   default Vec3d getPositionVector() {
      return Vec3d.ZERO;
   }

   World getEntityWorld();

   @Nullable
   default Entity getCommandSenderEntity() {
      return null;
   }

   default boolean sendCommandFeedback() {
      return false;
   }

   default void setCommandStat(CommandResultStats.Type var1, int var2) {
   }

   @Nullable
   MinecraftServer getServer();
}
