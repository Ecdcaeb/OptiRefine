package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess {
   protected int chunkX;
   protected int chunkZ;
   protected Chunk[][] chunkArray;
   protected boolean empty;
   protected World world;

   public ChunkCache(World var1, BlockPos var2, BlockPos var3, int var4) {
      this.world = ☃;
      this.chunkX = ☃.getX() - ☃ >> 4;
      this.chunkZ = ☃.getZ() - ☃ >> 4;
      int ☃ = ☃.getX() + ☃ >> 4;
      int ☃x = ☃.getZ() + ☃ >> 4;
      this.chunkArray = new Chunk[☃ - this.chunkX + 1][☃x - this.chunkZ + 1];
      this.empty = true;

      for (int ☃xx = this.chunkX; ☃xx <= ☃; ☃xx++) {
         for (int ☃xxx = this.chunkZ; ☃xxx <= ☃x; ☃xxx++) {
            this.chunkArray[☃xx - this.chunkX][☃xxx - this.chunkZ] = ☃.getChunk(☃xx, ☃xxx);
         }
      }

      for (int ☃xx = ☃.getX() >> 4; ☃xx <= ☃.getX() >> 4; ☃xx++) {
         for (int ☃xxx = ☃.getZ() >> 4; ☃xxx <= ☃.getZ() >> 4; ☃xxx++) {
            Chunk ☃xxxx = this.chunkArray[☃xx - this.chunkX][☃xxx - this.chunkZ];
            if (☃xxxx != null && !☃xxxx.isEmptyBetween(☃.getY(), ☃.getY())) {
               this.empty = false;
            }
         }
      }
   }

   public boolean isEmpty() {
      return this.empty;
   }

   @Nullable
   @Override
   public TileEntity getTileEntity(BlockPos var1) {
      return this.getTileEntity(☃, Chunk.EnumCreateEntityType.IMMEDIATE);
   }

   @Nullable
   public TileEntity getTileEntity(BlockPos var1, Chunk.EnumCreateEntityType var2) {
      int ☃ = (☃.getX() >> 4) - this.chunkX;
      int ☃x = (☃.getZ() >> 4) - this.chunkZ;
      return this.chunkArray[☃][☃x].getTileEntity(☃, ☃);
   }

   @Override
   public int getCombinedLight(BlockPos var1, int var2) {
      int ☃ = this.getLightForExt(EnumSkyBlock.SKY, ☃);
      int ☃x = this.getLightForExt(EnumSkyBlock.BLOCK, ☃);
      if (☃x < ☃) {
         ☃x = ☃;
      }

      return ☃ << 20 | ☃x << 4;
   }

   @Override
   public IBlockState getBlockState(BlockPos var1) {
      if (☃.getY() >= 0 && ☃.getY() < 256) {
         int ☃ = (☃.getX() >> 4) - this.chunkX;
         int ☃x = (☃.getZ() >> 4) - this.chunkZ;
         if (☃ >= 0 && ☃ < this.chunkArray.length && ☃x >= 0 && ☃x < this.chunkArray[☃].length) {
            Chunk ☃xx = this.chunkArray[☃][☃x];
            if (☃xx != null) {
               return ☃xx.getBlockState(☃);
            }
         }
      }

      return Blocks.AIR.getDefaultState();
   }

   @Override
   public Biome getBiome(BlockPos var1) {
      int ☃ = (☃.getX() >> 4) - this.chunkX;
      int ☃x = (☃.getZ() >> 4) - this.chunkZ;
      return this.chunkArray[☃][☃x].getBiome(☃, this.world.getBiomeProvider());
   }

   private int getLightForExt(EnumSkyBlock var1, BlockPos var2) {
      if (☃ == EnumSkyBlock.SKY && !this.world.provider.hasSkyLight()) {
         return 0;
      } else if (☃.getY() >= 0 && ☃.getY() < 256) {
         if (this.getBlockState(☃).useNeighborBrightness()) {
            int ☃ = 0;

            for (EnumFacing ☃x : EnumFacing.values()) {
               int ☃xx = this.getLightFor(☃, ☃.offset(☃x));
               if (☃xx > ☃) {
                  ☃ = ☃xx;
               }

               if (☃ >= 15) {
                  return ☃;
               }
            }

            return ☃;
         } else {
            int ☃ = (☃.getX() >> 4) - this.chunkX;
            int ☃x = (☃.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[☃][☃x].getLightFor(☃, ☃);
         }
      } else {
         return ☃.defaultLightValue;
      }
   }

   @Override
   public boolean isAirBlock(BlockPos var1) {
      return this.getBlockState(☃).getMaterial() == Material.AIR;
   }

   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      if (☃.getY() >= 0 && ☃.getY() < 256) {
         int ☃ = (☃.getX() >> 4) - this.chunkX;
         int ☃x = (☃.getZ() >> 4) - this.chunkZ;
         return this.chunkArray[☃][☃x].getLightFor(☃, ☃);
      } else {
         return ☃.defaultLightValue;
      }
   }

   @Override
   public int getStrongPower(BlockPos var1, EnumFacing var2) {
      return this.getBlockState(☃).getStrongPower(this, ☃, ☃);
   }

   @Override
   public WorldType getWorldType() {
      return this.world.getWorldType();
   }
}
