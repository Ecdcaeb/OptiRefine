package net.minecraft.world.storage;

import net.minecraft.util.math.MathHelper;

public class MapDecoration {
   private final MapDecoration.Type type;
   private byte x;
   private byte y;
   private byte rotation;

   public MapDecoration(MapDecoration.Type var1, byte var2, byte var3, byte var4) {
      this.type = ☃;
      this.x = ☃;
      this.y = ☃;
      this.rotation = ☃;
   }

   public byte getImage() {
      return this.type.getIcon();
   }

   public MapDecoration.Type getType() {
      return this.type;
   }

   public byte getX() {
      return this.x;
   }

   public byte getY() {
      return this.y;
   }

   public byte getRotation() {
      return this.rotation;
   }

   public boolean renderOnFrame() {
      return this.type.isRenderedOnFrame();
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof MapDecoration)) {
         return false;
      } else {
         MapDecoration ☃ = (MapDecoration)☃;
         if (this.type != ☃.type) {
            return false;
         } else if (this.rotation != ☃.rotation) {
            return false;
         } else {
            return this.x != ☃.x ? false : this.y == ☃.y;
         }
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.type.getIcon();
      ☃ = 31 * ☃ + this.x;
      ☃ = 31 * ☃ + this.y;
      return 31 * ☃ + this.rotation;
   }

   public static enum Type {
      PLAYER(false),
      FRAME(true),
      RED_MARKER(false),
      BLUE_MARKER(false),
      TARGET_X(true),
      TARGET_POINT(true),
      PLAYER_OFF_MAP(false),
      PLAYER_OFF_LIMITS(false),
      MANSION(true, 5393476),
      MONUMENT(true, 3830373);

      private final byte icon = (byte)this.ordinal();
      private final boolean renderedOnFrame;
      private final int mapColor;

      private Type(boolean var3) {
         this(☃, -1);
      }

      private Type(boolean var3, int var4) {
         this.renderedOnFrame = ☃;
         this.mapColor = ☃;
      }

      public byte getIcon() {
         return this.icon;
      }

      public boolean isRenderedOnFrame() {
         return this.renderedOnFrame;
      }

      public boolean hasMapColor() {
         return this.mapColor >= 0;
      }

      public int getMapColor() {
         return this.mapColor;
      }

      public static MapDecoration.Type byIcon(byte var0) {
         return values()[MathHelper.clamp(☃, 0, values().length - 1)];
      }
   }
}
