package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.ResourceLocation;

public class CPacketSeenAdvancements implements Packet<INetHandlerPlayServer> {
   private CPacketSeenAdvancements.Action action;
   private ResourceLocation tab;

   public CPacketSeenAdvancements() {
   }

   public CPacketSeenAdvancements(CPacketSeenAdvancements.Action var1, @Nullable ResourceLocation var2) {
      this.action = ☃;
      this.tab = ☃;
   }

   public static CPacketSeenAdvancements openedTab(Advancement var0) {
      return new CPacketSeenAdvancements(CPacketSeenAdvancements.Action.OPENED_TAB, ☃.getId());
   }

   public static CPacketSeenAdvancements closedScreen() {
      return new CPacketSeenAdvancements(CPacketSeenAdvancements.Action.CLOSED_SCREEN, null);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.action = ☃.readEnumValue(CPacketSeenAdvancements.Action.class);
      if (this.action == CPacketSeenAdvancements.Action.OPENED_TAB) {
         this.tab = ☃.readResourceLocation();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.action);
      if (this.action == CPacketSeenAdvancements.Action.OPENED_TAB) {
         ☃.writeResourceLocation(this.tab);
      }
   }

   public void processPacket(INetHandlerPlayServer var1) {
      ☃.handleSeenAdvancements(this);
   }

   public CPacketSeenAdvancements.Action getAction() {
      return this.action;
   }

   public ResourceLocation getTab() {
      return this.tab;
   }

   public static enum Action {
      OPENED_TAB,
      CLOSED_SCREEN;
   }
}
