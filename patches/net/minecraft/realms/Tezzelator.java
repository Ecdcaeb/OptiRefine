package net.minecraft.realms;

import net.minecraft.client.renderer.Tessellator;

public class Tezzelator {
   public static Tessellator t = Tessellator.getInstance();
   public static final Tezzelator instance = new Tezzelator();

   public void end() {
      t.draw();
   }

   public Tezzelator vertex(double var1, double var3, double var5) {
      t.getBuffer().pos(☃, ☃, ☃);
      return this;
   }

   public void color(float var1, float var2, float var3, float var4) {
      t.getBuffer().color(☃, ☃, ☃, ☃);
   }

   public void tex2(short var1, short var2) {
      t.getBuffer().lightmap(☃, ☃);
   }

   public void normal(float var1, float var2, float var3) {
      t.getBuffer().normal(☃, ☃, ☃);
   }

   public void begin(int var1, RealmsVertexFormat var2) {
      t.getBuffer().begin(☃, ☃.getVertexFormat());
   }

   public void endVertex() {
      t.getBuffer().endVertex();
   }

   public void offset(double var1, double var3, double var5) {
      t.getBuffer().setTranslation(☃, ☃, ☃);
   }

   public RealmsBufferBuilder color(int var1, int var2, int var3, int var4) {
      return new RealmsBufferBuilder(t.getBuffer().color(☃, ☃, ☃, ☃));
   }

   public Tezzelator tex(double var1, double var3) {
      t.getBuffer().tex(☃, ☃);
      return this;
   }
}
