package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class NBTTagString extends NBTBase {
   private String data;

   public NBTTagString() {
      this("");
   }

   public NBTTagString(String var1) {
      Objects.requireNonNull(☃, "Null string not allowed");
      this.data = ☃;
   }

   @Override
   void write(DataOutput var1) throws IOException {
      ☃.writeUTF(this.data);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(288L);
      this.data = ☃.readUTF();
      ☃.read(16 * this.data.length());
   }

   @Override
   public byte getId() {
      return 8;
   }

   @Override
   public String toString() {
      return quoteAndEscape(this.data);
   }

   public NBTTagString copy() {
      return new NBTTagString(this.data);
   }

   @Override
   public boolean isEmpty() {
      return this.data.isEmpty();
   }

   @Override
   public boolean equals(Object var1) {
      if (!super.equals(☃)) {
         return false;
      } else {
         NBTTagString ☃ = (NBTTagString)☃;
         return this.data == null && ☃.data == null || Objects.equals(this.data, ☃.data);
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.data.hashCode();
   }

   @Override
   public String getString() {
      return this.data;
   }

   public static String quoteAndEscape(String var0) {
      StringBuilder ☃ = new StringBuilder("\"");

      for (int ☃x = 0; ☃x < ☃.length(); ☃x++) {
         char ☃xx = ☃.charAt(☃x);
         if (☃xx == '\\' || ☃xx == '"') {
            ☃.append('\\');
         }

         ☃.append(☃xx);
      }

      return ☃.append('"').toString();
   }
}
