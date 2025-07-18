package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BitArray;
import net.minecraft.util.math.MathHelper;

public class BlockStateContainer implements IBlockStatePaletteResizer {
   private static final IBlockStatePalette REGISTRY_BASED_PALETTE = new BlockStatePaletteRegistry();
   protected static final IBlockState AIR_BLOCK_STATE = Blocks.AIR.getDefaultState();
   protected BitArray storage;
   protected IBlockStatePalette palette;
   private int bits;

   public BlockStateContainer() {
      this.setBits(4);
   }

   private static int getIndex(int var0, int var1, int var2) {
      return ☃ << 8 | ☃ << 4 | ☃;
   }

   private void setBits(int var1) {
      if (☃ != this.bits) {
         this.bits = ☃;
         if (this.bits <= 4) {
            this.bits = 4;
            this.palette = new BlockStatePaletteLinear(this.bits, this);
         } else if (this.bits <= 8) {
            this.palette = new BlockStatePaletteHashMap(this.bits, this);
         } else {
            this.palette = REGISTRY_BASED_PALETTE;
            this.bits = MathHelper.log2DeBruijn(Block.BLOCK_STATE_IDS.size());
         }

         this.palette.idFor(AIR_BLOCK_STATE);
         this.storage = new BitArray(this.bits, 4096);
      }
   }

   @Override
   public int onResize(int var1, IBlockState var2) {
      BitArray ☃ = this.storage;
      IBlockStatePalette ☃x = this.palette;
      this.setBits(☃);

      for (int ☃xx = 0; ☃xx < ☃.size(); ☃xx++) {
         IBlockState ☃xxx = ☃x.getBlockState(☃.getAt(☃xx));
         if (☃xxx != null) {
            this.set(☃xx, ☃xxx);
         }
      }

      return this.palette.idFor(☃);
   }

   public void set(int var1, int var2, int var3, IBlockState var4) {
      this.set(getIndex(☃, ☃, ☃), ☃);
   }

   protected void set(int var1, IBlockState var2) {
      int ☃ = this.palette.idFor(☃);
      this.storage.setAt(☃, ☃);
   }

   public IBlockState get(int var1, int var2, int var3) {
      return this.get(getIndex(☃, ☃, ☃));
   }

   protected IBlockState get(int var1) {
      IBlockState ☃ = this.palette.getBlockState(this.storage.getAt(☃));
      return ☃ == null ? AIR_BLOCK_STATE : ☃;
   }

   public void read(PacketBuffer var1) {
      int ☃ = ☃.readByte();
      if (this.bits != ☃) {
         this.setBits(☃);
      }

      this.palette.read(☃);
      ☃.readLongArray(this.storage.getBackingLongArray());
   }

   public void write(PacketBuffer var1) {
      ☃.writeByte(this.bits);
      this.palette.write(☃);
      ☃.writeLongArray(this.storage.getBackingLongArray());
   }

   @Nullable
   public NibbleArray getDataForNBT(byte[] var1, NibbleArray var2) {
      NibbleArray ☃ = null;

      for (int ☃x = 0; ☃x < 4096; ☃x++) {
         int ☃xx = Block.BLOCK_STATE_IDS.get(this.get(☃x));
         int ☃xxx = ☃x & 15;
         int ☃xxxx = ☃x >> 8 & 15;
         int ☃xxxxx = ☃x >> 4 & 15;
         if ((☃xx >> 12 & 15) != 0) {
            if (☃ == null) {
               ☃ = new NibbleArray();
            }

            ☃.set(☃xxx, ☃xxxx, ☃xxxxx, ☃xx >> 12 & 15);
         }

         ☃[☃x] = (byte)(☃xx >> 4 & 0xFF);
         ☃.set(☃xxx, ☃xxxx, ☃xxxxx, ☃xx & 15);
      }

      return ☃;
   }

   public void setDataFromNBT(byte[] var1, NibbleArray var2, @Nullable NibbleArray var3) {
      for (int ☃ = 0; ☃ < 4096; ☃++) {
         int ☃x = ☃ & 15;
         int ☃xx = ☃ >> 8 & 15;
         int ☃xxx = ☃ >> 4 & 15;
         int ☃xxxx = ☃ == null ? 0 : ☃.get(☃x, ☃xx, ☃xxx);
         int ☃xxxxx = ☃xxxx << 12 | (☃[☃] & 255) << 4 | ☃.get(☃x, ☃xx, ☃xxx);
         this.set(☃, Block.BLOCK_STATE_IDS.getByValue(☃xxxxx));
      }
   }

   public int getSerializedSize() {
      return 1 + this.palette.getSerializedSize() + PacketBuffer.getVarIntSize(this.storage.size()) + this.storage.getBackingLongArray().length * 8;
   }
}
