package net.minecraft.realms;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class RealmsBufferBuilder {
   private BufferBuilder b;

   public RealmsBufferBuilder(BufferBuilder var1) {
      this.b = ☃;
   }

   public RealmsBufferBuilder from(BufferBuilder var1) {
      this.b = ☃;
      return this;
   }

   public void sortQuads(float var1, float var2, float var3) {
      this.b.sortVertexData(☃, ☃, ☃);
   }

   public void fixupQuadColor(int var1) {
      this.b.putColor4(☃);
   }

   public ByteBuffer getBuffer() {
      return this.b.getByteBuffer();
   }

   public void postNormal(float var1, float var2, float var3) {
      this.b.putNormal(☃, ☃, ☃);
   }

   public int getDrawMode() {
      return this.b.getDrawMode();
   }

   public void offset(double var1, double var3, double var5) {
      this.b.setTranslation(☃, ☃, ☃);
   }

   public void restoreState(BufferBuilder.State var1) {
      this.b.setVertexState(☃);
   }

   public void endVertex() {
      this.b.endVertex();
   }

   public RealmsBufferBuilder normal(float var1, float var2, float var3) {
      return this.from(this.b.normal(☃, ☃, ☃));
   }

   public void end() {
      this.b.finishDrawing();
   }

   public void begin(int var1, VertexFormat var2) {
      this.b.begin(☃, ☃);
   }

   public RealmsBufferBuilder color(int var1, int var2, int var3, int var4) {
      return this.from(this.b.color(☃, ☃, ☃, ☃));
   }

   public void faceTex2(int var1, int var2, int var3, int var4) {
      this.b.putBrightness4(☃, ☃, ☃, ☃);
   }

   public void postProcessFacePosition(double var1, double var3, double var5) {
      this.b.putPosition(☃, ☃, ☃);
   }

   public void fixupVertexColor(float var1, float var2, float var3, int var4) {
      this.b.putColorRGB_F(☃, ☃, ☃, ☃);
   }

   public RealmsBufferBuilder color(float var1, float var2, float var3, float var4) {
      return this.from(this.b.color(☃, ☃, ☃, ☃));
   }

   public RealmsVertexFormat getVertexFormat() {
      return new RealmsVertexFormat(this.b.getVertexFormat());
   }

   public void faceTint(float var1, float var2, float var3, int var4) {
      this.b.putColorMultiplier(☃, ☃, ☃, ☃);
   }

   public RealmsBufferBuilder tex2(int var1, int var2) {
      return this.from(this.b.lightmap(☃, ☃));
   }

   public void putBulkData(int[] var1) {
      this.b.addVertexData(☃);
   }

   public RealmsBufferBuilder tex(double var1, double var3) {
      return this.from(this.b.tex(☃, ☃));
   }

   public int getVertexCount() {
      return this.b.getVertexCount();
   }

   public void clear() {
      this.b.reset();
   }

   public RealmsBufferBuilder vertex(double var1, double var3, double var5) {
      return this.from(this.b.pos(☃, ☃, ☃));
   }

   public void fixupQuadColor(float var1, float var2, float var3) {
      this.b.putColorRGB_F4(☃, ☃, ☃);
   }

   public void noColor() {
      this.b.noColor();
   }
}
