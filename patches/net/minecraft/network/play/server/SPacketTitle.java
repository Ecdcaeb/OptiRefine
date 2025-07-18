package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketTitle implements Packet<INetHandlerPlayClient> {
   private SPacketTitle.Type type;
   private ITextComponent message;
   private int fadeInTime;
   private int displayTime;
   private int fadeOutTime;

   public SPacketTitle() {
   }

   public SPacketTitle(SPacketTitle.Type var1, ITextComponent var2) {
      this(☃, ☃, -1, -1, -1);
   }

   public SPacketTitle(int var1, int var2, int var3) {
      this(SPacketTitle.Type.TIMES, null, ☃, ☃, ☃);
   }

   public SPacketTitle(SPacketTitle.Type var1, @Nullable ITextComponent var2, int var3, int var4, int var5) {
      this.type = ☃;
      this.message = ☃;
      this.fadeInTime = ☃;
      this.displayTime = ☃;
      this.fadeOutTime = ☃;
   }

   @Override
   public void readPacketData(PacketBuffer var1) throws IOException {
      this.type = ☃.readEnumValue(SPacketTitle.Type.class);
      if (this.type == SPacketTitle.Type.TITLE || this.type == SPacketTitle.Type.SUBTITLE || this.type == SPacketTitle.Type.ACTIONBAR) {
         this.message = ☃.readTextComponent();
      }

      if (this.type == SPacketTitle.Type.TIMES) {
         this.fadeInTime = ☃.readInt();
         this.displayTime = ☃.readInt();
         this.fadeOutTime = ☃.readInt();
      }
   }

   @Override
   public void writePacketData(PacketBuffer var1) throws IOException {
      ☃.writeEnumValue(this.type);
      if (this.type == SPacketTitle.Type.TITLE || this.type == SPacketTitle.Type.SUBTITLE || this.type == SPacketTitle.Type.ACTIONBAR) {
         ☃.writeTextComponent(this.message);
      }

      if (this.type == SPacketTitle.Type.TIMES) {
         ☃.writeInt(this.fadeInTime);
         ☃.writeInt(this.displayTime);
         ☃.writeInt(this.fadeOutTime);
      }
   }

   public void processPacket(INetHandlerPlayClient var1) {
      ☃.handleTitle(this);
   }

   public SPacketTitle.Type getType() {
      return this.type;
   }

   public ITextComponent getMessage() {
      return this.message;
   }

   public int getFadeInTime() {
      return this.fadeInTime;
   }

   public int getDisplayTime() {
      return this.displayTime;
   }

   public int getFadeOutTime() {
      return this.fadeOutTime;
   }

   public static enum Type {
      TITLE,
      SUBTITLE,
      ACTIONBAR,
      TIMES,
      CLEAR,
      RESET;

      public static SPacketTitle.Type byName(String var0) {
         for (SPacketTitle.Type ☃ : values()) {
            if (☃.name().equalsIgnoreCase(☃)) {
               return ☃;
            }
         }

         return TITLE;
      }

      public static String[] getNames() {
         String[] ☃ = new String[values().length];
         int ☃x = 0;

         for (SPacketTitle.Type ☃xx : values()) {
            ☃[☃x++] = ☃xx.name().toLowerCase(Locale.ROOT);
         }

         return ☃;
      }
   }
}
