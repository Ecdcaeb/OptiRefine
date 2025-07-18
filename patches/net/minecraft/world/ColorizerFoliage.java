package net.minecraft.world;

public class ColorizerFoliage {
   private static int[] foliageBuffer = new int[65536];

   public static void setFoliageBiomeColorizer(int[] var0) {
      foliageBuffer = ☃;
   }

   public static int getFoliageColor(double var0, double var2) {
      ☃ *= ☃;
      int ☃ = (int)((1.0 - ☃) * 255.0);
      int ☃x = (int)((1.0 - ☃) * 255.0);
      return foliageBuffer[☃x << 8 | ☃];
   }

   public static int getFoliageColorPine() {
      return 6396257;
   }

   public static int getFoliageColorBirch() {
      return 8431445;
   }

   public static int getFoliageColorBasic() {
      return 4764952;
   }
}
