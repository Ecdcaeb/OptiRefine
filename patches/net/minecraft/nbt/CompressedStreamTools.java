package net.minecraft.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

public class CompressedStreamTools {
   public static NBTTagCompound readCompressed(InputStream var0) throws IOException {
      DataInputStream ☃ = new DataInputStream(new BufferedInputStream(new GZIPInputStream(☃)));

      NBTTagCompound var2;
      try {
         var2 = read(☃, NBTSizeTracker.INFINITE);
      } finally {
         ☃.close();
      }

      return var2;
   }

   public static void writeCompressed(NBTTagCompound var0, OutputStream var1) throws IOException {
      DataOutputStream ☃ = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(☃)));

      try {
         write(☃, ☃);
      } finally {
         ☃.close();
      }
   }

   public static void safeWrite(NBTTagCompound var0, File var1) throws IOException {
      File ☃ = new File(☃.getAbsolutePath() + "_tmp");
      if (☃.exists()) {
         ☃.delete();
      }

      write(☃, ☃);
      if (☃.exists()) {
         ☃.delete();
      }

      if (☃.exists()) {
         throw new IOException("Failed to delete " + ☃);
      } else {
         ☃.renameTo(☃);
      }
   }

   public static void write(NBTTagCompound var0, File var1) throws IOException {
      DataOutputStream ☃ = new DataOutputStream(new FileOutputStream(☃));

      try {
         write(☃, ☃);
      } finally {
         ☃.close();
      }
   }

   @Nullable
   public static NBTTagCompound read(File var0) throws IOException {
      if (!☃.exists()) {
         return null;
      } else {
         DataInputStream ☃ = new DataInputStream(new FileInputStream(☃));

         NBTTagCompound var2;
         try {
            var2 = read(☃, NBTSizeTracker.INFINITE);
         } finally {
            ☃.close();
         }

         return var2;
      }
   }

   public static NBTTagCompound read(DataInputStream var0) throws IOException {
      return read(☃, NBTSizeTracker.INFINITE);
   }

   public static NBTTagCompound read(DataInput var0, NBTSizeTracker var1) throws IOException {
      NBTBase ☃ = read(☃, 0, ☃);
      if (☃ instanceof NBTTagCompound) {
         return (NBTTagCompound)☃;
      } else {
         throw new IOException("Root tag must be a named compound tag");
      }
   }

   public static void write(NBTTagCompound var0, DataOutput var1) throws IOException {
      writeTag(☃, ☃);
   }

   private static void writeTag(NBTBase var0, DataOutput var1) throws IOException {
      ☃.writeByte(☃.getId());
      if (☃.getId() != 0) {
         ☃.writeUTF("");
         ☃.write(☃);
      }
   }

   private static NBTBase read(DataInput var0, int var1, NBTSizeTracker var2) throws IOException {
      byte ☃ = ☃.readByte();
      if (☃ == 0) {
         return new NBTTagEnd();
      } else {
         ☃.readUTF();
         NBTBase ☃x = NBTBase.create(☃);

         try {
            ☃x.read(☃, ☃, ☃);
            return ☃x;
         } catch (IOException var8) {
            CrashReport ☃xx = CrashReport.makeCrashReport(var8, "Loading NBT data");
            CrashReportCategory ☃xxx = ☃xx.makeCategory("NBT Tag");
            ☃xxx.addCrashSection("Tag type", ☃);
            throw new ReportedException(☃xx);
         }
      }
   }
}
