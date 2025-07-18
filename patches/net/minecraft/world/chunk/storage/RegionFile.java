package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;

public class RegionFile {
   private static final byte[] EMPTY_SECTOR = new byte[4096];
   private final File fileName;
   private RandomAccessFile dataFile;
   private final int[] offsets = new int[1024];
   private final int[] chunkTimestamps = new int[1024];
   private List<Boolean> sectorFree;
   private int sizeDelta;
   private long lastModified;

   public RegionFile(File var1) {
      this.fileName = ☃;
      this.sizeDelta = 0;

      try {
         if (☃.exists()) {
            this.lastModified = ☃.lastModified();
         }

         this.dataFile = new RandomAccessFile(☃, "rw");
         if (this.dataFile.length() < 4096L) {
            this.dataFile.write(EMPTY_SECTOR);
            this.dataFile.write(EMPTY_SECTOR);
            this.sizeDelta += 8192;
         }

         if ((this.dataFile.length() & 4095L) != 0L) {
            for (int ☃ = 0; ☃ < (this.dataFile.length() & 4095L); ☃++) {
               this.dataFile.write(0);
            }
         }

         int ☃ = (int)this.dataFile.length() / 4096;
         this.sectorFree = Lists.newArrayListWithCapacity(☃);

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            this.sectorFree.add(true);
         }

         this.sectorFree.set(0, false);
         this.sectorFree.set(1, false);
         this.dataFile.seek(0L);

         for (int ☃x = 0; ☃x < 1024; ☃x++) {
            int ☃xx = this.dataFile.readInt();
            this.offsets[☃x] = ☃xx;
            if (☃xx != 0 && (☃xx >> 8) + (☃xx & 0xFF) <= this.sectorFree.size()) {
               for (int ☃xxx = 0; ☃xxx < (☃xx & 0xFF); ☃xxx++) {
                  this.sectorFree.set((☃xx >> 8) + ☃xxx, false);
               }
            }
         }

         for (int ☃xx = 0; ☃xx < 1024; ☃xx++) {
            int ☃xxx = this.dataFile.readInt();
            this.chunkTimestamps[☃xx] = ☃xxx;
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   @Nullable
   public synchronized DataInputStream getChunkDataInputStream(int var1, int var2) {
      if (this.outOfBounds(☃, ☃)) {
         return null;
      } else {
         try {
            int ☃ = this.getOffset(☃, ☃);
            if (☃ == 0) {
               return null;
            } else {
               int ☃x = ☃ >> 8;
               int ☃xx = ☃ & 0xFF;
               if (☃x + ☃xx > this.sectorFree.size()) {
                  return null;
               } else {
                  this.dataFile.seek(☃x * 4096);
                  int ☃xxx = this.dataFile.readInt();
                  if (☃xxx > 4096 * ☃xx) {
                     return null;
                  } else if (☃xxx <= 0) {
                     return null;
                  } else {
                     byte ☃xxxx = this.dataFile.readByte();
                     if (☃xxxx == 1) {
                        byte[] ☃xxxxx = new byte[☃xxx - 1];
                        this.dataFile.read(☃xxxxx);
                        return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(☃xxxxx))));
                     } else if (☃xxxx == 2) {
                        byte[] ☃xxxxx = new byte[☃xxx - 1];
                        this.dataFile.read(☃xxxxx);
                        return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(☃xxxxx))));
                     } else {
                        return null;
                     }
                  }
               }
            }
         } catch (IOException var9) {
            return null;
         }
      }
   }

   @Nullable
   public DataOutputStream getChunkDataOutputStream(int var1, int var2) {
      return this.outOfBounds(☃, ☃) ? null : new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(new RegionFile.ChunkBuffer(☃, ☃))));
   }

   protected synchronized void write(int var1, int var2, byte[] var3, int var4) {
      try {
         int ☃ = this.getOffset(☃, ☃);
         int ☃x = ☃ >> 8;
         int ☃xx = ☃ & 0xFF;
         int ☃xxx = (☃ + 5) / 4096 + 1;
         if (☃xxx >= 256) {
            return;
         }

         if (☃x != 0 && ☃xx == ☃xxx) {
            this.write(☃x, ☃, ☃);
         } else {
            for (int ☃xxxx = 0; ☃xxxx < ☃xx; ☃xxxx++) {
               this.sectorFree.set(☃x + ☃xxxx, true);
            }

            int ☃xxxx = this.sectorFree.indexOf(true);
            int ☃xxxxx = 0;
            if (☃xxxx != -1) {
               for (int ☃xxxxxx = ☃xxxx; ☃xxxxxx < this.sectorFree.size(); ☃xxxxxx++) {
                  if (☃xxxxx != 0) {
                     if (this.sectorFree.get(☃xxxxxx)) {
                        ☃xxxxx++;
                     } else {
                        ☃xxxxx = 0;
                     }
                  } else if (this.sectorFree.get(☃xxxxxx)) {
                     ☃xxxx = ☃xxxxxx;
                     ☃xxxxx = 1;
                  }

                  if (☃xxxxx >= ☃xxx) {
                     break;
                  }
               }
            }

            if (☃xxxxx >= ☃xxx) {
               ☃x = ☃xxxx;
               this.setOffset(☃, ☃, ☃xxxx << 8 | ☃xxx);

               for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxx; ☃xxxxxx++) {
                  this.sectorFree.set(☃x + ☃xxxxxx, false);
               }

               this.write(☃x, ☃, ☃);
            } else {
               this.dataFile.seek(this.dataFile.length());
               ☃x = this.sectorFree.size();

               for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxx; ☃xxxxxx++) {
                  this.dataFile.write(EMPTY_SECTOR);
                  this.sectorFree.add(false);
               }

               this.sizeDelta += 4096 * ☃xxx;
               this.write(☃x, ☃, ☃);
               this.setOffset(☃, ☃, ☃x << 8 | ☃xxx);
            }
         }

         this.setChunkTimestamp(☃, ☃, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
      } catch (IOException var12) {
         var12.printStackTrace();
      }
   }

   private void write(int var1, byte[] var2, int var3) throws IOException {
      this.dataFile.seek(☃ * 4096);
      this.dataFile.writeInt(☃ + 1);
      this.dataFile.writeByte(2);
      this.dataFile.write(☃, 0, ☃);
   }

   private boolean outOfBounds(int var1, int var2) {
      return ☃ < 0 || ☃ >= 32 || ☃ < 0 || ☃ >= 32;
   }

   private int getOffset(int var1, int var2) {
      return this.offsets[☃ + ☃ * 32];
   }

   public boolean isChunkSaved(int var1, int var2) {
      return this.getOffset(☃, ☃) != 0;
   }

   private void setOffset(int var1, int var2, int var3) throws IOException {
      this.offsets[☃ + ☃ * 32] = ☃;
      this.dataFile.seek((☃ + ☃ * 32) * 4);
      this.dataFile.writeInt(☃);
   }

   private void setChunkTimestamp(int var1, int var2, int var3) throws IOException {
      this.chunkTimestamps[☃ + ☃ * 32] = ☃;
      this.dataFile.seek(4096 + (☃ + ☃ * 32) * 4);
      this.dataFile.writeInt(☃);
   }

   public void close() throws IOException {
      if (this.dataFile != null) {
         this.dataFile.close();
      }
   }

   class ChunkBuffer extends ByteArrayOutputStream {
      private final int chunkX;
      private final int chunkZ;

      public ChunkBuffer(int var2, int var3) {
         super(8096);
         this.chunkX = ☃;
         this.chunkZ = ☃;
      }

      @Override
      public void close() {
         RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
      }
   }
}
