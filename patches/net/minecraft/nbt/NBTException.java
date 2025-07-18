package net.minecraft.nbt;

public class NBTException extends Exception {
   public NBTException(String var1, String var2, int var3) {
      super(☃ + " at: " + slice(☃, ☃));
   }

   private static String slice(String var0, int var1) {
      StringBuilder ☃ = new StringBuilder();
      int ☃x = Math.min(☃.length(), ☃);
      if (☃x > 35) {
         ☃.append("...");
      }

      ☃.append(☃.substring(Math.max(0, ☃x - 35), ☃x));
      ☃.append("<--[HERE]");
      return ☃.toString();
   }
}
