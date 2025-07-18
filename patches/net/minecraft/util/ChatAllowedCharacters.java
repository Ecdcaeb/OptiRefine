package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

public class ChatAllowedCharacters {
   public static final Level NETTY_LEAK_DETECTION = Level.DISABLED;
   public static final char[] ILLEGAL_STRUCTURE_CHARACTERS = new char[]{'.', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"'};
   public static final char[] ILLEGAL_FILE_CHARACTERS = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};

   public static boolean isAllowedCharacter(char var0) {
      return ☃ != 167 && ☃ >= ' ' && ☃ != 127;
   }

   public static String filterAllowedCharacters(String var0) {
      StringBuilder ☃ = new StringBuilder();

      for (char ☃x : ☃.toCharArray()) {
         if (isAllowedCharacter(☃x)) {
            ☃.append(☃x);
         }
      }

      return ☃.toString();
   }

   static {
      ResourceLeakDetector.setLevel(NETTY_LEAK_DETECTION);
   }
}
