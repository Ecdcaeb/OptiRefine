package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.regex.Pattern;

public class JsonToNBT {
   private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
   private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
   private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
   private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
   private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
   private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
   private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
   private final String string;
   private int cursor;

   public static NBTTagCompound getTagFromJson(String var0) throws NBTException {
      return new JsonToNBT(☃).readSingleStruct();
   }

   @VisibleForTesting
   NBTTagCompound readSingleStruct() throws NBTException {
      NBTTagCompound ☃ = this.readStruct();
      this.skipWhitespace();
      if (this.canRead()) {
         this.cursor++;
         throw this.exception("Trailing data found");
      } else {
         return ☃;
      }
   }

   @VisibleForTesting
   JsonToNBT(String var1) {
      this.string = ☃;
   }

   protected String readKey() throws NBTException {
      this.skipWhitespace();
      if (!this.canRead()) {
         throw this.exception("Expected key");
      } else {
         return this.peek() == '"' ? this.readQuotedString() : this.readString();
      }
   }

   private NBTException exception(String var1) {
      return new NBTException(☃, this.string, this.cursor);
   }

   protected NBTBase readTypedValue() throws NBTException {
      this.skipWhitespace();
      if (this.peek() == '"') {
         return new NBTTagString(this.readQuotedString());
      } else {
         String ☃ = this.readString();
         if (☃.isEmpty()) {
            throw this.exception("Expected value");
         } else {
            return this.type(☃);
         }
      }
   }

   private NBTBase type(String var1) {
      try {
         if (FLOAT_PATTERN.matcher(☃).matches()) {
            return new NBTTagFloat(Float.parseFloat(☃.substring(0, ☃.length() - 1)));
         }

         if (BYTE_PATTERN.matcher(☃).matches()) {
            return new NBTTagByte(Byte.parseByte(☃.substring(0, ☃.length() - 1)));
         }

         if (LONG_PATTERN.matcher(☃).matches()) {
            return new NBTTagLong(Long.parseLong(☃.substring(0, ☃.length() - 1)));
         }

         if (SHORT_PATTERN.matcher(☃).matches()) {
            return new NBTTagShort(Short.parseShort(☃.substring(0, ☃.length() - 1)));
         }

         if (INT_PATTERN.matcher(☃).matches()) {
            return new NBTTagInt(Integer.parseInt(☃));
         }

         if (DOUBLE_PATTERN.matcher(☃).matches()) {
            return new NBTTagDouble(Double.parseDouble(☃.substring(0, ☃.length() - 1)));
         }

         if (DOUBLE_PATTERN_NOSUFFIX.matcher(☃).matches()) {
            return new NBTTagDouble(Double.parseDouble(☃));
         }

         if ("true".equalsIgnoreCase(☃)) {
            return new NBTTagByte((byte)1);
         }

         if ("false".equalsIgnoreCase(☃)) {
            return new NBTTagByte((byte)0);
         }
      } catch (NumberFormatException var3) {
      }

      return new NBTTagString(☃);
   }

   private String readQuotedString() throws NBTException {
      int ☃ = ++this.cursor;
      StringBuilder ☃x = null;
      boolean ☃xx = false;

      while (this.canRead()) {
         char ☃xxx = this.pop();
         if (☃xx) {
            if (☃xxx != '\\' && ☃xxx != '"') {
               throw this.exception("Invalid escape of '" + ☃xxx + "'");
            }

            ☃xx = false;
         } else {
            if (☃xxx == '\\') {
               ☃xx = true;
               if (☃x == null) {
                  ☃x = new StringBuilder(this.string.substring(☃, this.cursor - 1));
               }
               continue;
            }

            if (☃xxx == '"') {
               return ☃x == null ? this.string.substring(☃, this.cursor - 1) : ☃x.toString();
            }
         }

         if (☃x != null) {
            ☃x.append(☃xxx);
         }
      }

      throw this.exception("Missing termination quote");
   }

   private String readString() {
      int ☃ = this.cursor;

      while (this.canRead() && this.isAllowedInKey(this.peek())) {
         this.cursor++;
      }

      return this.string.substring(☃, this.cursor);
   }

   protected NBTBase readValue() throws NBTException {
      this.skipWhitespace();
      if (!this.canRead()) {
         throw this.exception("Expected value");
      } else {
         char ☃ = this.peek();
         if (☃ == '{') {
            return this.readStruct();
         } else {
            return ☃ == '[' ? this.readList() : this.readTypedValue();
         }
      }
   }

   protected NBTBase readList() throws NBTException {
      return this.canRead(2) && this.peek(1) != '"' && this.peek(2) == ';' ? this.readArrayTag() : this.readListTag();
   }

   protected NBTTagCompound readStruct() throws NBTException {
      this.expect('{');
      NBTTagCompound ☃ = new NBTTagCompound();
      this.skipWhitespace();

      while (this.canRead() && this.peek() != '}') {
         String ☃x = this.readKey();
         if (☃x.isEmpty()) {
            throw this.exception("Expected non-empty key");
         }

         this.expect(':');
         ☃.setTag(☃x, this.readValue());
         if (!this.hasElementSeparator()) {
            break;
         }

         if (!this.canRead()) {
            throw this.exception("Expected key");
         }
      }

      this.expect('}');
      return ☃;
   }

   private NBTBase readListTag() throws NBTException {
      this.expect('[');
      this.skipWhitespace();
      if (!this.canRead()) {
         throw this.exception("Expected value");
      } else {
         NBTTagList ☃ = new NBTTagList();
         int ☃x = -1;

         while (this.peek() != ']') {
            NBTBase ☃xx = this.readValue();
            int ☃xxx = ☃xx.getId();
            if (☃x < 0) {
               ☃x = ☃xxx;
            } else if (☃xxx != ☃x) {
               throw this.exception("Unable to insert " + NBTBase.getTypeName(☃xxx) + " into ListTag of type " + NBTBase.getTypeName(☃x));
            }

            ☃.appendTag(☃xx);
            if (!this.hasElementSeparator()) {
               break;
            }

            if (!this.canRead()) {
               throw this.exception("Expected value");
            }
         }

         this.expect(']');
         return ☃;
      }
   }

   private NBTBase readArrayTag() throws NBTException {
      this.expect('[');
      char ☃ = this.pop();
      this.pop();
      this.skipWhitespace();
      if (!this.canRead()) {
         throw this.exception("Expected value");
      } else if (☃ == 'B') {
         return new NBTTagByteArray(this.readArray((byte)7, (byte)1));
      } else if (☃ == 'L') {
         return new NBTTagLongArray(this.readArray((byte)12, (byte)4));
      } else if (☃ == 'I') {
         return new NBTTagIntArray(this.readArray((byte)11, (byte)3));
      } else {
         throw this.exception("Invalid array type '" + ☃ + "' found");
      }
   }

   private <T extends Number> List<T> readArray(byte var1, byte var2) throws NBTException {
      List<T> ☃ = Lists.newArrayList();

      while (this.peek() != ']') {
         NBTBase ☃x = this.readValue();
         int ☃xx = ☃x.getId();
         if (☃xx != ☃) {
            throw this.exception("Unable to insert " + NBTBase.getTypeName(☃xx) + " into " + NBTBase.getTypeName(☃));
         }

         if (☃ == 1) {
            ☃.add((T)((NBTPrimitive)☃x).getByte());
         } else if (☃ == 4) {
            ☃.add((T)((NBTPrimitive)☃x).getLong());
         } else {
            ☃.add((T)((NBTPrimitive)☃x).getInt());
         }

         if (!this.hasElementSeparator()) {
            break;
         }

         if (!this.canRead()) {
            throw this.exception("Expected value");
         }
      }

      this.expect(']');
      return ☃;
   }

   private void skipWhitespace() {
      while (this.canRead() && Character.isWhitespace(this.peek())) {
         this.cursor++;
      }
   }

   private boolean hasElementSeparator() {
      this.skipWhitespace();
      if (this.canRead() && this.peek() == ',') {
         this.cursor++;
         this.skipWhitespace();
         return true;
      } else {
         return false;
      }
   }

   private void expect(char var1) throws NBTException {
      this.skipWhitespace();
      boolean ☃ = this.canRead();
      if (☃ && this.peek() == ☃) {
         this.cursor++;
      } else {
         throw new NBTException("Expected '" + ☃ + "' but got '" + (☃ ? this.peek() : "<EOF>") + "'", this.string, this.cursor + 1);
      }
   }

   protected boolean isAllowedInKey(char var1) {
      return ☃ >= '0' && ☃ <= '9' || ☃ >= 'A' && ☃ <= 'Z' || ☃ >= 'a' && ☃ <= 'z' || ☃ == '_' || ☃ == '-' || ☃ == '.' || ☃ == '+';
   }

   private boolean canRead(int var1) {
      return this.cursor + ☃ < this.string.length();
   }

   boolean canRead() {
      return this.canRead(0);
   }

   private char peek(int var1) {
      return this.string.charAt(this.cursor + ☃);
   }

   private char peek() {
      return this.peek(0);
   }

   private char pop() {
      return this.string.charAt(this.cursor++);
   }
}
