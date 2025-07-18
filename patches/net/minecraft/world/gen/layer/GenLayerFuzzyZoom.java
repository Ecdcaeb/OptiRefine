package net.minecraft.world.gen.layer;

public class GenLayerFuzzyZoom extends GenLayerZoom {
   public GenLayerFuzzyZoom(long var1, GenLayer var3) {
      super(☃, ☃);
   }

   @Override
   protected int selectModeOrRandom(int var1, int var2, int var3, int var4) {
      return this.selectRandom(new int[]{☃, ☃, ☃, ☃});
   }
}
