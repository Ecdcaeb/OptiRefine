package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHandSide;

public class CPacketClientSettings implements Packet<INetHandlerPlayServer> {
   private String lang;
   private int view;
   private EntityPlayer.EnumChatVisibility chatVisibility;
   private boolean enableColors;
   private int modelPartFlags;
   private EnumHandSide mainHand;

   public CPacketClientSettings() {
   }

   public CPacketClientSettings(String var1, int var2, EntityPlayer.EnumChatVisibility var3, boolean var4, int var5, EnumHandSide var6) {
      this.lang = ☃;
      this.view = ☃;
      this.chatVisibility = ☃;
      this.enableColors = ☃;
      this.modelPartFlags = ☃;
      this.mainHand = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.lang = ☃.readString(16);
      this.view = ☃.readByte();
      this.chatVisibility = ☃.readEnumValue(EntityPlayer.EnumChatVisibility.class);
      this.enableColors = ☃.readBoolean();
      this.modelPartFlags = ☃.readUnsignedByte();
      this.mainHand = ☃.readEnumValue(EnumHandSide.class);
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeString(this.lang);
      ☃.writeByte(this.view);
      ☃.writeEnumValue(this.chatVisibility);
      ☃.writeBoolean(this.enableColors);
      ☃.writeByte(this.modelPartFlags);
      ☃.writeEnumValue(this.mainHand);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.processClientSettings(this);
   }

   public String getLang() {
      return this.lang;
   }

   public EntityPlayer.EnumChatVisibility getChatVisibility() {
      return this.chatVisibility;
   }

   public boolean isColorsEnabled() {
      return this.enableColors;
   }

   public int getModelPartFlags() {
      return this.modelPartFlags;
   }

   public EnumHandSide getMainHand() {
      return this.mainHand;
   }
}
