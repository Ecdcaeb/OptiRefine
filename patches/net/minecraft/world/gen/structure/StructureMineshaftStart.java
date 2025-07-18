package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.world.World;

public class StructureMineshaftStart extends StructureStart {
   private MapGenMineshaft.Type mineShaftType;

   public StructureMineshaftStart() {
   }

   public StructureMineshaftStart(World var1, Random var2, int var3, int var4, MapGenMineshaft.Type var5) {
      super(☃, ☃);
      this.mineShaftType = ☃;
      StructureMineshaftPieces.Room ☃ = new StructureMineshaftPieces.Room(0, ☃, (☃ << 4) + 2, (☃ << 4) + 2, this.mineShaftType);
      this.components.add(☃);
      ☃.buildComponent(☃, this.components, ☃);
      this.updateBoundingBox();
      if (☃ == MapGenMineshaft.Type.MESA) {
         int ☃x = -5;
         int ☃xx = ☃.getSeaLevel() - this.boundingBox.maxY + this.boundingBox.getYSize() / 2 - -5;
         this.boundingBox.offset(0, ☃xx, 0);

         for (StructureComponent ☃xxx : this.components) {
            ☃xxx.offset(0, ☃xx, 0);
         }
      } else {
         this.markAvailableHeight(☃, ☃, 10);
      }
   }
}
