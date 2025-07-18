package net.minecraft.util.text;

public enum ChatType {
   CHAT((byte)0),
   SYSTEM((byte)1),
   GAME_INFO((byte)2);

   private final byte id;

   private ChatType(byte var3) {
      this.id = ☃;
   }

   public byte getId() {
      return this.id;
   }

   public static ChatType byId(byte var0) {
      for (ChatType ☃ : values()) {
         if (☃ == ☃.id) {
            return ☃;
         }
      }

      return CHAT;
   }
}
