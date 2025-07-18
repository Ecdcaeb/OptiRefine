package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketPlayerListHeaderFooter implements Packet<INetHandlerPlayClient> {
   private ITextComponent header;
   private ITextComponent footer;

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.header = ☃.readTextComponent();
      this.footer = ☃.readTextComponent();
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeTextComponent(this.header);
      ☃.writeTextComponent(this.footer);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handlePlayerListHeaderFooter(this);
   }

   public ITextComponent getHeader() {
      return this.header;
   }

   public ITextComponent getFooter() {
      return this.footer;
   }
}
