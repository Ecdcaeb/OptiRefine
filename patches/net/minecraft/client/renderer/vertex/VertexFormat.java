package net.minecraft.client.renderer.vertex;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormat {
   private static final Logger LOGGER = LogManager.getLogger();
   private final List<VertexFormatElement> elements = Lists.newArrayList();
   private final List<Integer> offsets = Lists.newArrayList();
   private int vertexSize;
   private int colorElementOffset = -1;
   private final List<Integer> uvOffsetsById = Lists.newArrayList();
   private int normalElementOffset = -1;

   public VertexFormat(VertexFormat var1) {
      this();

      for (int ☃ = 0; ☃ < ☃.getElementCount(); ☃++) {
         this.addElement(☃.getElement(☃));
      }

      this.vertexSize = ☃.getSize();
   }

   public VertexFormat() {
   }

   public void clear() {
      this.elements.clear();
      this.offsets.clear();
      this.colorElementOffset = -1;
      this.uvOffsetsById.clear();
      this.normalElementOffset = -1;
      this.vertexSize = 0;
   }

   public VertexFormat addElement(VertexFormatElement var1) {
      if (☃.isPositionElement() && this.hasPosition()) {
         LOGGER.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
         return this;
      } else {
         this.elements.add(☃);
         this.offsets.add(this.vertexSize);
         switch (☃.getUsage()) {
            case NORMAL:
               this.normalElementOffset = this.vertexSize;
               break;
            case COLOR:
               this.colorElementOffset = this.vertexSize;
               break;
            case UV:
               this.uvOffsetsById.add(☃.getIndex(), this.vertexSize);
         }

         this.vertexSize = this.vertexSize + ☃.getSize();
         return this;
      }
   }

   public boolean hasNormal() {
      return this.normalElementOffset >= 0;
   }

   public int getNormalOffset() {
      return this.normalElementOffset;
   }

   public boolean hasColor() {
      return this.colorElementOffset >= 0;
   }

   public int getColorOffset() {
      return this.colorElementOffset;
   }

   public boolean hasUvOffset(int var1) {
      return this.uvOffsetsById.size() - 1 >= ☃;
   }

   public int getUvOffsetById(int var1) {
      return this.uvOffsetsById.get(☃);
   }

   @Override
   public String toString() {
      String ☃ = "format: " + this.elements.size() + " elements: ";

      for (int ☃x = 0; ☃x < this.elements.size(); ☃x++) {
         ☃ = ☃ + this.elements.get(☃x).toString();
         if (☃x != this.elements.size() - 1) {
            ☃ = ☃ + " ";
         }
      }

      return ☃;
   }

   private boolean hasPosition() {
      int ☃ = 0;

      for (int ☃x = this.elements.size(); ☃ < ☃x; ☃++) {
         VertexFormatElement ☃xx = this.elements.get(☃);
         if (☃xx.isPositionElement()) {
            return true;
         }
      }

      return false;
   }

   public int getIntegerSize() {
      return this.getSize() / 4;
   }

   public int getSize() {
      return this.vertexSize;
   }

   public List<VertexFormatElement> getElements() {
      return this.elements;
   }

   public int getElementCount() {
      return this.elements.size();
   }

   public VertexFormatElement getElement(int var1) {
      return this.elements.get(☃);
   }

   public int getOffset(int var1) {
      return this.offsets.get(☃);
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ != null && this.getClass() == ☃.getClass()) {
         VertexFormat ☃ = (VertexFormat)☃;
         if (this.vertexSize != ☃.vertexSize) {
            return false;
         } else {
            return !this.elements.equals(☃.elements) ? false : this.offsets.equals(☃.offsets);
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.elements.hashCode();
      ☃ = 31 * ☃ + this.offsets.hashCode();
      return 31 * ☃ + this.vertexSize;
   }
}
