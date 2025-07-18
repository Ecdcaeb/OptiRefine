package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class SPacketMaps implements Packet<INetHandlerPlayClient> {
   private int mapId;
   private byte mapScale;
   private boolean trackingPosition;
   private MapDecoration[] icons;
   private int minX;
   private int minZ;
   private int columns;
   private int rows;
   private byte[] mapDataBytes;

   public SPacketMaps() {
   }

   public SPacketMaps(int var1, byte var2, boolean var3, Collection<MapDecoration> var4, byte[] var5, int var6, int var7, int var8, int var9) {
      this.mapId = ☃;
      this.mapScale = ☃;
      this.trackingPosition = ☃;
      this.icons = ☃.toArray(new MapDecoration[☃.size()]);
      this.minX = ☃;
      this.minZ = ☃;
      this.columns = ☃;
      this.rows = ☃;
      this.mapDataBytes = new byte[☃ * ☃];

      for (int ☃ = 0; ☃ < ☃; ☃++) {
         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            this.mapDataBytes[☃ + ☃x * ☃] = ☃[☃ + ☃ + (☃ + ☃x) * 128];
         }
      }
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.mapId = ☃.readVarInt();
      this.mapScale = ☃.readByte();
      this.trackingPosition = ☃.readBoolean();
      this.icons = new MapDecoration[☃.readVarInt()];

      for (int ☃ = 0; ☃ < this.icons.length; ☃++) {
         short ☃x = ☃.readByte();
         this.icons[☃] = new MapDecoration(MapDecoration.Type.byIcon((byte)(☃x >> 4 & 15)), ☃.readByte(), ☃.readByte(), (byte)(☃x & 15));
      }

      this.columns = ☃.readUnsignedByte();
      if (this.columns > 0) {
         this.rows = ☃.readUnsignedByte();
         this.minX = ☃.readUnsignedByte();
         this.minZ = ☃.readUnsignedByte();
         this.mapDataBytes = ☃.readByteArray();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeVarInt(this.mapId);
      ☃.writeByte(this.mapScale);
      ☃.writeBoolean(this.trackingPosition);
      ☃.writeVarInt(this.icons.length);

      for (MapDecoration ☃ : this.icons) {
         ☃.writeByte((☃.getImage() & 15) << 4 | ☃.getRotation() & 15);
         ☃.writeByte(☃.getX());
         ☃.writeByte(☃.getY());
      }

      ☃.writeByte(this.columns);
      if (this.columns > 0) {
         ☃.writeByte(this.rows);
         ☃.writeByte(this.minX);
         ☃.writeByte(this.minZ);
         ☃.writeByteArray(this.mapDataBytes);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleMaps(this);
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapdataTo(MapData var1) {
      ☃.scale = this.mapScale;
      ☃.trackingPosition = this.trackingPosition;
      ☃.mapDecorations.clear();

      for (int ☃ = 0; ☃ < this.icons.length; ☃++) {
         MapDecoration ☃x = this.icons[☃];
         ☃.mapDecorations.put("icon-" + ☃, ☃x);
      }

      for (int ☃ = 0; ☃ < this.columns; ☃++) {
         for (int ☃x = 0; ☃x < this.rows; ☃x++) {
            ☃.colors[this.minX + ☃ + (this.minZ + ☃x) * 128] = this.mapDataBytes[☃ + ☃x * this.columns];
         }
      }
   }
}
