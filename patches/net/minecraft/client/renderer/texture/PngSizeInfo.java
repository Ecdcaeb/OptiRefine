package net.minecraft.client.renderer.texture;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.resources.IResource;
import org.apache.commons.io.IOUtils;

public class PngSizeInfo {
   public final int pngWidth;
   public final int pngHeight;

   public PngSizeInfo(InputStream var1) throws IOException {
      DataInputStream ☃ = new DataInputStream(☃);
      if (☃.readLong() != -8552249625308161526L) {
         throw new IOException("Bad PNG Signature");
      } else if (☃.readInt() != 13) {
         throw new IOException("Bad length for IHDR chunk!");
      } else if (☃.readInt() != 1229472850) {
         throw new IOException("Bad type for IHDR chunk!");
      } else {
         this.pngWidth = ☃.readInt();
         this.pngHeight = ☃.readInt();
         IOUtils.closeQuietly(☃);
      }
   }

   public static PngSizeInfo makeFromResource(IResource var0) throws IOException {
      PngSizeInfo var1;
      try {
         var1 = new PngSizeInfo(☃.getInputStream());
      } finally {
         IOUtils.closeQuietly(☃);
      }

      return var1;
   }
}
