package net.minecraft.world;

public class ColorizerGrass {
   private static int[] grassBuffer = new int[65536];

   public static void setGrassBiomeColorizer(int[] var0) {
      grassBuffer = ☃;
   }

   public static int getGrassColor(double var0, double var2) {
      ☃ *= ☃;
      int ☃ = (int)((1.0 - ☃) * 255.0);
      int ☃x = (int)((1.0 - ☃) * 255.0);
      int ☃xx = ☃x << 8 | ☃;
      return ☃xx > grassBuffer.length ? -65281 : grassBuffer[☃xx];
   }
}
