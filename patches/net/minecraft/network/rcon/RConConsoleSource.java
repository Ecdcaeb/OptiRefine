package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender {
   private final StringBuffer buffer = new StringBuffer();
   private final MinecraftServer server;

   public RConConsoleSource(MinecraftServer var1) {
      this.server = ☃;
   }

   @Override
   public String getName() {
      return "Rcon";
   }

   @Override
   public void sendMessage(ITextComponent var1) {
      this.buffer.append(☃.getUnformattedText());
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      return true;
   }

   @Override
   public World getEntityWorld() {
      return this.server.getEntityWorld();
   }

   @Override
   public boolean sendCommandFeedback() {
      return true;
   }

   @Override
   public MinecraftServer getServer() {
      return this.server;
   }
}
