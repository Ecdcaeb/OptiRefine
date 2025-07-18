package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class SPacketChat implements Packet<INetHandlerPlayClient> {
   private ITextComponent chatComponent;
   private ChatType type;

   public SPacketChat() {
   }

   public SPacketChat(ITextComponent var1) {
      this(☃, ChatType.SYSTEM);
   }

   public SPacketChat(ITextComponent var1, ChatType var2) {
      this.chatComponent = ☃;
      this.type = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.chatComponent = ☃.readTextComponent();
      this.type = ChatType.byId(☃.readByte());
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeTextComponent(this.chatComponent);
      ☃.writeByte(this.type.getId());
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleChat(this);
   }

   public ITextComponent getChatComponent() {
      return this.chatComponent;
   }

   public boolean isSystem() {
      return this.type == ChatType.SYSTEM || this.type == ChatType.GAME_INFO;
   }

   public ChatType getType() {
      return this.type;
   }
}
