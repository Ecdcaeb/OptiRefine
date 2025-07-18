package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage {
   private final int yBase;
   private int blockRefCount;
   private int tickRefCount;
   private final BlockStateContainer data;
   private NibbleArray blockLight;
   private NibbleArray skyLight;

   public ExtendedBlockStorage(int var1, boolean var2) {
      this.yBase = ☃;
      this.data = new BlockStateContainer();
      this.blockLight = new NibbleArray();
      if (☃) {
         this.skyLight = new NibbleArray();
      }
   }

   public IBlockState get(int var1, int var2, int var3) {
      return this.data.get(☃, ☃, ☃);
   }

   public void set(int var1, int var2, int var3, IBlockState var4) {
      IBlockState ☃ = this.get(☃, ☃, ☃);
      Block ☃x = ☃.getBlock();
      Block ☃xx = ☃.getBlock();
      if (☃x != Blocks.AIR) {
         this.blockRefCount--;
         if (☃x.getTickRandomly()) {
            this.tickRefCount--;
         }
      }

      if (☃xx != Blocks.AIR) {
         this.blockRefCount++;
         if (☃xx.getTickRandomly()) {
            this.tickRefCount++;
         }
      }

      this.data.set(☃, ☃, ☃, ☃);
   }

   public boolean isEmpty() {
      return this.blockRefCount == 0;
   }

   public boolean needsRandomTick() {
      return this.tickRefCount > 0;
   }

   public int getYLocation() {
      return this.yBase;
   }

   public void setSkyLight(int var1, int var2, int var3, int var4) {
      this.skyLight.set(☃, ☃, ☃, ☃);
   }

   public int getSkyLight(int var1, int var2, int var3) {
      return this.skyLight.get(☃, ☃, ☃);
   }

   public void setBlockLight(int var1, int var2, int var3, int var4) {
      this.blockLight.set(☃, ☃, ☃, ☃);
   }

   public int getBlockLight(int var1, int var2, int var3) {
      return this.blockLight.get(☃, ☃, ☃);
   }

   public void recalculateRefCounts() {
      this.blockRefCount = 0;
      this.tickRefCount = 0;

      for (int ☃ = 0; ☃ < 16; ☃++) {
         for (int ☃x = 0; ☃x < 16; ☃x++) {
            for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
               Block ☃xxx = this.get(☃, ☃x, ☃xx).getBlock();
               if (☃xxx != Blocks.AIR) {
                  this.blockRefCount++;
                  if (☃xxx.getTickRandomly()) {
                     this.tickRefCount++;
                  }
               }
            }
         }
      }
   }

   public BlockStateContainer getData() {
      return this.data;
   }

   public NibbleArray getBlockLight() {
      return this.blockLight;
   }

   public NibbleArray getSkyLight() {
      return this.skyLight;
   }

   public void setBlockLight(NibbleArray var1) {
      this.blockLight = ☃;
   }

   public void setSkyLight(NibbleArray var1) {
      this.skyLight = ☃;
   }
}
