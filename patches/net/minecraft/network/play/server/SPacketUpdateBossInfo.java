package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;

public class SPacketUpdateBossInfo implements Packet<INetHandlerPlayClient> {
   private UUID uniqueId;
   private SPacketUpdateBossInfo.Operation operation;
   private ITextComponent name;
   private float percent;
   private BossInfo.Color color;
   private BossInfo.Overlay overlay;
   private boolean darkenSky;
   private boolean playEndBossMusic;
   private boolean createFog;

   public SPacketUpdateBossInfo() {
   }

   public SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation var1, BossInfo var2) {
      this.operation = ☃;
      this.uniqueId = ☃.getUniqueId();
      this.name = ☃.getName();
      this.percent = ☃.getPercent();
      this.color = ☃.getColor();
      this.overlay = ☃.getOverlay();
      this.darkenSky = ☃.shouldDarkenSky();
      this.playEndBossMusic = ☃.shouldPlayEndBossMusic();
      this.createFog = ☃.shouldCreateFog();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.uniqueId = ☃.readUniqueId();
      this.operation = ☃.readEnumValue(SPacketUpdateBossInfo.Operation.class);
      switch (this.operation) {
         case ADD:
            this.name = ☃.readTextComponent();
            this.percent = ☃.readFloat();
            this.color = ☃.readEnumValue(BossInfo.Color.class);
            this.overlay = ☃.readEnumValue(BossInfo.Overlay.class);
            this.setFlags(☃.readUnsignedByte());
         case REMOVE:
         default:
            break;
         case UPDATE_PCT:
            this.percent = ☃.readFloat();
            break;
         case UPDATE_NAME:
            this.name = ☃.readTextComponent();
            break;
         case UPDATE_STYLE:
            this.color = ☃.readEnumValue(BossInfo.Color.class);
            this.overlay = ☃.readEnumValue(BossInfo.Overlay.class);
            break;
         case UPDATE_PROPERTIES:
            this.setFlags(☃.readUnsignedByte());
      }
   }

   private void setFlags(int var1) {
      this.darkenSky = (☃ & 1) > 0;
      this.playEndBossMusic = (☃ & 2) > 0;
      this.createFog = (☃ & 2) > 0;
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeUniqueId(this.uniqueId);
      ☃.writeEnumValue(this.operation);
      switch (this.operation) {
         case ADD:
            ☃.writeTextComponent(this.name);
            ☃.writeFloat(this.percent);
            ☃.writeEnumValue(this.color);
            ☃.writeEnumValue(this.overlay);
            ☃.writeByte(this.getFlags());
         case REMOVE:
         default:
            break;
         case UPDATE_PCT:
            ☃.writeFloat(this.percent);
            break;
         case UPDATE_NAME:
            ☃.writeTextComponent(this.name);
            break;
         case UPDATE_STYLE:
            ☃.writeEnumValue(this.color);
            ☃.writeEnumValue(this.overlay);
            break;
         case UPDATE_PROPERTIES:
            ☃.writeByte(this.getFlags());
      }
   }

   private int getFlags() {
      int ☃ = 0;
      if (this.darkenSky) {
         ☃ |= 1;
      }

      if (this.playEndBossMusic) {
         ☃ |= 2;
      }

      if (this.createFog) {
         ☃ |= 2;
      }

      return ☃;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleUpdateBossInfo(this);
   }

   public UUID getUniqueId() {
      return this.uniqueId;
   }

   public SPacketUpdateBossInfo.Operation getOperation() {
      return this.operation;
   }

   public ITextComponent getName() {
      return this.name;
   }

   public float getPercent() {
      return this.percent;
   }

   public BossInfo.Color getColor() {
      return this.color;
   }

   public BossInfo.Overlay getOverlay() {
      return this.overlay;
   }

   public boolean shouldDarkenSky() {
      return this.darkenSky;
   }

   public boolean shouldPlayEndBossMusic() {
      return this.playEndBossMusic;
   }

   public boolean shouldCreateFog() {
      return this.createFog;
   }

   public static enum Operation {
      ADD,
      REMOVE,
      UPDATE_PCT,
      UPDATE_NAME,
      UPDATE_STYLE,
      UPDATE_PROPERTIES;
   }
}
