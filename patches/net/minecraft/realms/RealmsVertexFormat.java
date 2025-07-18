package net.minecraft.realms;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class RealmsVertexFormat {
   private VertexFormat v;

   public RealmsVertexFormat(VertexFormat var1) {
      this.v = ☃;
   }

   public RealmsVertexFormat from(VertexFormat var1) {
      this.v = ☃;
      return this;
   }

   public VertexFormat getVertexFormat() {
      return this.v;
   }

   public void clear() {
      this.v.clear();
   }

   public int getUvOffset(int var1) {
      return this.v.getUvOffsetById(☃);
   }

   public int getElementCount() {
      return this.v.getElementCount();
   }

   public boolean hasColor() {
      return this.v.hasColor();
   }

   public boolean hasUv(int var1) {
      return this.v.hasUvOffset(☃);
   }

   public RealmsVertexFormatElement getElement(int var1) {
      return new RealmsVertexFormatElement(this.v.getElement(☃));
   }

   public RealmsVertexFormat addElement(RealmsVertexFormatElement var1) {
      return this.from(this.v.addElement(☃.getVertexFormatElement()));
   }

   public int getColorOffset() {
      return this.v.getColorOffset();
   }

   public List<RealmsVertexFormatElement> getElements() {
      List<RealmsVertexFormatElement> ☃ = Lists.newArrayList();

      for (VertexFormatElement ☃x : this.v.getElements()) {
         ☃.add(new RealmsVertexFormatElement(☃x));
      }

      return ☃;
   }

   public boolean hasNormal() {
      return this.v.hasNormal();
   }

   public int getVertexSize() {
      return this.v.getSize();
   }

   public int getOffset(int var1) {
      return this.v.getOffset(☃);
   }

   public int getNormalOffset() {
      return this.v.getNormalOffset();
   }

   public int getIntegerSize() {
      return this.v.getIntegerSize();
   }

   @Override
   public boolean equals(Object var1) {
      return this.v.equals(☃);
   }

   @Override
   public int hashCode() {
      return this.v.hashCode();
   }

   @Override
   public String toString() {
      return this.v.toString();
   }
}
