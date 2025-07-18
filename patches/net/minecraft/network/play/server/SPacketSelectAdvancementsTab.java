package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient> {
   @Nullable
   private ResourceLocation tab;

   public SPacketSelectAdvancementsTab() {
   }

   public SPacketSelectAdvancementsTab(@Nullable ResourceLocation var1) {
      this.tab = ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleSelectAdvancementsTab(this);
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      if (☃.readBoolean()) {
         this.tab = ☃.readResourceLocation();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeBoolean(this.tab != null);
      if (this.tab != null) {
         ☃.writeResourceLocation(this.tab);
      }
   }

   @Nullable
   public ResourceLocation getTab() {
      return this.tab;
   }
}
