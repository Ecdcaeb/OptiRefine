package net.minecraft.client.renderer.vertex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormatElement {
   private static final Logger LOGGER = LogManager.getLogger();
   private final VertexFormatElement.EnumType type;
   private final VertexFormatElement.EnumUsage usage;
   private final int index;
   private final int elementCount;

   public VertexFormatElement(int var1, VertexFormatElement.EnumType var2, VertexFormatElement.EnumUsage var3, int var4) {
      if (this.isFirstOrUV(☃, ☃)) {
         this.usage = ☃;
      } else {
         LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
         this.usage = VertexFormatElement.EnumUsage.UV;
      }

      this.type = ☃;
      this.index = ☃;
      this.elementCount = ☃;
   }

   private final boolean isFirstOrUV(int var1, VertexFormatElement.EnumUsage var2) {
      return ☃ == 0 || ☃ == VertexFormatElement.EnumUsage.UV;
   }

   public final VertexFormatElement.EnumType getType() {
      return this.type;
   }

   public final VertexFormatElement.EnumUsage getUsage() {
      return this.usage;
   }

   public final int getElementCount() {
      return this.elementCount;
   }

   public final int getIndex() {
      return this.index;
   }

   @Override
   public String toString() {
      return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
   }

   public final int getSize() {
      return this.type.getSize() * this.elementCount;
   }

   public final boolean isPositionElement() {
      return this.usage == VertexFormatElement.EnumUsage.POSITION;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (☃ != null && this.getClass() == ☃.getClass()) {
         VertexFormatElement ☃ = (VertexFormatElement)☃;
         if (this.elementCount != ☃.elementCount) {
            return false;
         } else if (this.index != ☃.index) {
            return false;
         } else {
            return this.type != ☃.type ? false : this.usage == ☃.usage;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.type.hashCode();
      ☃ = 31 * ☃ + this.usage.hashCode();
      ☃ = 31 * ☃ + this.index;
      return 31 * ☃ + this.elementCount;
   }

   public static enum EnumType {
      FLOAT(4, "Float", 5126),
      UBYTE(1, "Unsigned Byte", 5121),
      BYTE(1, "Byte", 5120),
      USHORT(2, "Unsigned Short", 5123),
      SHORT(2, "Short", 5122),
      UINT(4, "Unsigned Int", 5125),
      INT(4, "Int", 5124);

      private final int size;
      private final String displayName;
      private final int glConstant;

      private EnumType(int var3, String var4, int var5) {
         this.size = ☃;
         this.displayName = ☃;
         this.glConstant = ☃;
      }

      public int getSize() {
         return this.size;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public int getGlConstant() {
         return this.glConstant;
      }
   }

   public static enum EnumUsage {
      POSITION("Position"),
      NORMAL("Normal"),
      COLOR("Vertex Color"),
      UV("UV"),
      MATRIX("Bone Matrix"),
      BLEND_WEIGHT("Blend Weight"),
      PADDING("Padding");

      private final String displayName;

      private EnumUsage(String var3) {
         this.displayName = ☃;
      }

      public String getDisplayName() {
         return this.displayName;
      }
   }
}
