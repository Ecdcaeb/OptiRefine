package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

public class SPacketWorldBorder implements Packet<INetHandlerPlayClient> {
   private SPacketWorldBorder.Action action;
   private int size;
   private double centerX;
   private double centerZ;
   private double targetSize;
   private double diameter;
   private long timeUntilTarget;
   private int warningTime;
   private int warningDistance;

   public SPacketWorldBorder() {
   }

   public SPacketWorldBorder(WorldBorder var1, SPacketWorldBorder.Action var2) {
      this.action = ☃;
      this.centerX = ☃.getCenterX();
      this.centerZ = ☃.getCenterZ();
      this.diameter = ☃.getDiameter();
      this.targetSize = ☃.getTargetSize();
      this.timeUntilTarget = ☃.getTimeUntilTarget();
      this.size = ☃.getSize();
      this.warningDistance = ☃.getWarningDistance();
      this.warningTime = ☃.getWarningTime();
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.action = ☃.readEnumValue(SPacketWorldBorder.Action.class);
      switch (this.action) {
         case SET_SIZE:
            this.targetSize = ☃.readDouble();
            break;
         case LERP_SIZE:
            this.diameter = ☃.readDouble();
            this.targetSize = ☃.readDouble();
            this.timeUntilTarget = ☃.readVarLong();
            break;
         case SET_CENTER:
            this.centerX = ☃.readDouble();
            this.centerZ = ☃.readDouble();
            break;
         case SET_WARNING_BLOCKS:
            this.warningDistance = ☃.readVarInt();
            break;
         case SET_WARNING_TIME:
            this.warningTime = ☃.readVarInt();
            break;
         case INITIALIZE:
            this.centerX = ☃.readDouble();
            this.centerZ = ☃.readDouble();
            this.diameter = ☃.readDouble();
            this.targetSize = ☃.readDouble();
            this.timeUntilTarget = ☃.readVarLong();
            this.size = ☃.readVarInt();
            this.warningDistance = ☃.readVarInt();
            this.warningTime = ☃.readVarInt();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.action);
      switch (this.action) {
         case SET_SIZE:
            ☃.writeDouble(this.targetSize);
            break;
         case LERP_SIZE:
            ☃.writeDouble(this.diameter);
            ☃.writeDouble(this.targetSize);
            ☃.writeVarLong(this.timeUntilTarget);
            break;
         case SET_CENTER:
            ☃.writeDouble(this.centerX);
            ☃.writeDouble(this.centerZ);
            break;
         case SET_WARNING_BLOCKS:
            ☃.writeVarInt(this.warningDistance);
            break;
         case SET_WARNING_TIME:
            ☃.writeVarInt(this.warningTime);
            break;
         case INITIALIZE:
            ☃.writeDouble(this.centerX);
            ☃.writeDouble(this.centerZ);
            ☃.writeDouble(this.diameter);
            ☃.writeDouble(this.targetSize);
            ☃.writeVarLong(this.timeUntilTarget);
            ☃.writeVarInt(this.size);
            ☃.writeVarInt(this.warningDistance);
            ☃.writeVarInt(this.warningTime);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleWorldBorder(this);
   }

   public void apply(WorldBorder var1) {
      switch (this.action) {
         case SET_SIZE:
            ☃.setTransition(this.targetSize);
            break;
         case LERP_SIZE:
            ☃.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
            break;
         case SET_CENTER:
            ☃.setCenter(this.centerX, this.centerZ);
            break;
         case SET_WARNING_BLOCKS:
            ☃.setWarningDistance(this.warningDistance);
            break;
         case SET_WARNING_TIME:
            ☃.setWarningTime(this.warningTime);
            break;
         case INITIALIZE:
            ☃.setCenter(this.centerX, this.centerZ);
            if (this.timeUntilTarget > 0L) {
               ☃.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
            } else {
               ☃.setTransition(this.targetSize);
            }

            ☃.setSize(this.size);
            ☃.setWarningDistance(this.warningDistance);
            ☃.setWarningTime(this.warningTime);
      }
   }

   public static enum Action {
      SET_SIZE,
      LERP_SIZE,
      SET_CENTER,
      INITIALIZE,
      SET_WARNING_TIME,
      SET_WARNING_BLOCKS;
   }
}
