package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class FlatLayerInfo {
   private final int version;
   private IBlockState layerMaterial;
   private int layerCount = 1;
   private int layerMinimumY;

   public FlatLayerInfo(int var1, Block var2) {
      this(3, ☃, ☃);
   }

   public FlatLayerInfo(int var1, int var2, Block var3) {
      this.version = ☃;
      this.layerCount = ☃;
      this.layerMaterial = ☃.getDefaultState();
   }

   public FlatLayerInfo(int var1, int var2, Block var3, int var4) {
      this(☃, ☃, ☃);
      this.layerMaterial = ☃.getStateFromMeta(☃);
   }

   public int getLayerCount() {
      return this.layerCount;
   }

   public IBlockState getLayerMaterial() {
      return this.layerMaterial;
   }

   private Block getLayerMaterialBlock() {
      return this.layerMaterial.getBlock();
   }

   private int getFillBlockMeta() {
      return this.layerMaterial.getBlock().getMetaFromState(this.layerMaterial);
   }

   public int getMinY() {
      return this.layerMinimumY;
   }

   public void setMinY(int var1) {
      this.layerMinimumY = ☃;
   }

   @Override
   public String toString() {
      String ☃;
      if (this.version >= 3) {
         ResourceLocation ☃x = Block.REGISTRY.getNameForObject(this.getLayerMaterialBlock());
         ☃ = ☃x == null ? "null" : ☃x.toString();
         if (this.layerCount > 1) {
            ☃ = this.layerCount + "*" + ☃;
         }
      } else {
         ☃ = Integer.toString(Block.getIdFromBlock(this.getLayerMaterialBlock()));
         if (this.layerCount > 1) {
            ☃ = this.layerCount + "x" + ☃;
         }
      }

      int ☃x = this.getFillBlockMeta();
      if (☃x > 0) {
         ☃ = ☃ + ":" + ☃x;
      }

      return ☃;
   }
}
