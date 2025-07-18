package net.minecraft.client.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.annotation.Nullable;

public class ImageBufferDownload implements IImageBuffer {
   private int[] imageData;
   private int imageWidth;
   private int imageHeight;

   @Nullable
   @Override
   public BufferedImage parseUserSkin(BufferedImage var1) {
      if (☃ == null) {
         return null;
      } else {
         this.imageWidth = 64;
         this.imageHeight = 64;
         BufferedImage ☃ = new BufferedImage(this.imageWidth, this.imageHeight, 2);
         Graphics ☃x = ☃.getGraphics();
         ☃x.drawImage(☃, 0, 0, null);
         boolean ☃xx = ☃.getHeight() == 32;
         if (☃xx) {
            ☃x.setColor(new Color(0, 0, 0, 0));
            ☃x.fillRect(0, 32, 64, 32);
            ☃x.drawImage(☃, 24, 48, 20, 52, 4, 16, 8, 20, null);
            ☃x.drawImage(☃, 28, 48, 24, 52, 8, 16, 12, 20, null);
            ☃x.drawImage(☃, 20, 52, 16, 64, 8, 20, 12, 32, null);
            ☃x.drawImage(☃, 24, 52, 20, 64, 4, 20, 8, 32, null);
            ☃x.drawImage(☃, 28, 52, 24, 64, 0, 20, 4, 32, null);
            ☃x.drawImage(☃, 32, 52, 28, 64, 12, 20, 16, 32, null);
            ☃x.drawImage(☃, 40, 48, 36, 52, 44, 16, 48, 20, null);
            ☃x.drawImage(☃, 44, 48, 40, 52, 48, 16, 52, 20, null);
            ☃x.drawImage(☃, 36, 52, 32, 64, 48, 20, 52, 32, null);
            ☃x.drawImage(☃, 40, 52, 36, 64, 44, 20, 48, 32, null);
            ☃x.drawImage(☃, 44, 52, 40, 64, 40, 20, 44, 32, null);
            ☃x.drawImage(☃, 48, 52, 44, 64, 52, 20, 56, 32, null);
         }

         ☃x.dispose();
         this.imageData = ((DataBufferInt)☃.getRaster().getDataBuffer()).getData();
         this.setAreaOpaque(0, 0, 32, 16);
         if (☃xx) {
            this.setAreaTransparent(32, 0, 64, 32);
         }

         this.setAreaOpaque(0, 16, 64, 32);
         this.setAreaOpaque(16, 48, 48, 64);
         return ☃;
      }
   }

   @Override
   public void skinAvailable() {
   }

   private void setAreaTransparent(int var1, int var2, int var3, int var4) {
      for (int ☃ = ☃; ☃ < ☃; ☃++) {
         for (int ☃x = ☃; ☃x < ☃; ☃x++) {
            int ☃xx = this.imageData[☃ + ☃x * this.imageWidth];
            if ((☃xx >> 24 & 0xFF) < 128) {
               return;
            }
         }
      }

      for (int ☃ = ☃; ☃ < ☃; ☃++) {
         for (int ☃xx = ☃; ☃xx < ☃; ☃xx++) {
            this.imageData[☃ + ☃xx * this.imageWidth] = this.imageData[☃ + ☃xx * this.imageWidth] & 16777215;
         }
      }
   }

   private void setAreaOpaque(int var1, int var2, int var3, int var4) {
      for (int ☃ = ☃; ☃ < ☃; ☃++) {
         for (int ☃x = ☃; ☃x < ☃; ☃x++) {
            this.imageData[☃ + ☃x * this.imageWidth] = this.imageData[☃ + ☃x * this.imageWidth] | 0xFF000000;
         }
      }
   }
}
