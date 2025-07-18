package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.math.MathHelper;

public class Stitcher {
   private final int mipmapLevelStitcher;
   private final Set<Stitcher.Holder> setStitchHolders = Sets.newHashSetWithExpectedSize(256);
   private final List<Stitcher.Slot> stitchSlots = Lists.newArrayListWithCapacity(256);
   private int currentWidth;
   private int currentHeight;
   private final int maxWidth;
   private final int maxHeight;
   private final int maxTileDimension;

   public Stitcher(int var1, int var2, int var3, int var4) {
      this.mipmapLevelStitcher = ☃;
      this.maxWidth = ☃;
      this.maxHeight = ☃;
      this.maxTileDimension = ☃;
   }

   public int getCurrentWidth() {
      return this.currentWidth;
   }

   public int getCurrentHeight() {
      return this.currentHeight;
   }

   public void addSprite(TextureAtlasSprite var1) {
      Stitcher.Holder ☃ = new Stitcher.Holder(☃, this.mipmapLevelStitcher);
      if (this.maxTileDimension > 0) {
         ☃.setNewDimension(this.maxTileDimension);
      }

      this.setStitchHolders.add(☃);
   }

   public void doStitch() {
      Stitcher.Holder[] ☃ = this.setStitchHolders.toArray(new Stitcher.Holder[this.setStitchHolders.size()]);
      Arrays.sort((Object[])☃);

      for (Stitcher.Holder ☃x : ☃) {
         if (!this.allocateSlot(☃x)) {
            String ☃xx = String.format(
               "Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?",
               ☃x.getAtlasSprite().getIconName(),
               ☃x.getAtlasSprite().getIconWidth(),
               ☃x.getAtlasSprite().getIconHeight()
            );
            throw new StitcherException(☃x, ☃xx);
         }
      }

      this.currentWidth = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth);
      this.currentHeight = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight);
   }

   public List<TextureAtlasSprite> getStichSlots() {
      List<Stitcher.Slot> ☃ = Lists.newArrayList();

      for (Stitcher.Slot ☃x : this.stitchSlots) {
         ☃x.getAllStitchSlots(☃);
      }

      List<TextureAtlasSprite> ☃x = Lists.newArrayList();

      for (Stitcher.Slot ☃xx : ☃) {
         Stitcher.Holder ☃xxx = ☃xx.getStitchHolder();
         TextureAtlasSprite ☃xxxx = ☃xxx.getAtlasSprite();
         ☃xxxx.initSprite(this.currentWidth, this.currentHeight, ☃xx.getOriginX(), ☃xx.getOriginY(), ☃xxx.isRotated());
         ☃x.add(☃xxxx);
      }

      return ☃x;
   }

   private static int getMipmapDimension(int var0, int var1) {
      return (☃ >> ☃) + ((☃ & (1 << ☃) - 1) == 0 ? 0 : 1) << ☃;
   }

   private boolean allocateSlot(Stitcher.Holder var1) {
      TextureAtlasSprite ☃ = ☃.getAtlasSprite();
      boolean ☃x = ☃.getIconWidth() != ☃.getIconHeight();

      for (int ☃xx = 0; ☃xx < this.stitchSlots.size(); ☃xx++) {
         if (this.stitchSlots.get(☃xx).addSlot(☃)) {
            return true;
         }

         if (☃x) {
            ☃.rotate();
            if (this.stitchSlots.get(☃xx).addSlot(☃)) {
               return true;
            }

            ☃.rotate();
         }
      }

      return this.expandAndAllocateSlot(☃);
   }

   private boolean expandAndAllocateSlot(Stitcher.Holder var1) {
      int ☃ = Math.min(☃.getWidth(), ☃.getHeight());
      int ☃x = Math.max(☃.getWidth(), ☃.getHeight());
      int ☃xx = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth);
      int ☃xxx = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight);
      int ☃xxxx = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth + ☃);
      int ☃xxxxx = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight + ☃);
      boolean ☃xxxxxx = ☃xxxx <= this.maxWidth;
      boolean ☃xxxxxxx = ☃xxxxx <= this.maxHeight;
      if (!☃xxxxxx && !☃xxxxxxx) {
         return false;
      } else {
         boolean ☃xxxxxxxx = ☃xxxxxx && ☃xx != ☃xxxx;
         boolean ☃xxxxxxxxx = ☃xxxxxxx && ☃xxx != ☃xxxxx;
         boolean ☃xxxxxxxxxx;
         if (☃xxxxxxxx ^ ☃xxxxxxxxx) {
            ☃xxxxxxxxxx = ☃xxxxxxxx;
         } else {
            ☃xxxxxxxxxx = ☃xxxxxx && ☃xx <= ☃xxx;
         }

         Stitcher.Slot ☃xxxxxxxxxxx;
         if (☃xxxxxxxxxx) {
            if (☃.getWidth() > ☃.getHeight()) {
               ☃.rotate();
            }

            if (this.currentHeight == 0) {
               this.currentHeight = ☃.getHeight();
            }

            ☃xxxxxxxxxxx = new Stitcher.Slot(this.currentWidth, 0, ☃.getWidth(), this.currentHeight);
            this.currentWidth = this.currentWidth + ☃.getWidth();
         } else {
            ☃xxxxxxxxxxx = new Stitcher.Slot(0, this.currentHeight, this.currentWidth, ☃.getHeight());
            this.currentHeight = this.currentHeight + ☃.getHeight();
         }

         ☃xxxxxxxxxxx.addSlot(☃);
         this.stitchSlots.add(☃xxxxxxxxxxx);
         return true;
      }
   }

   public static class Holder implements Comparable<Stitcher.Holder> {
      private final TextureAtlasSprite sprite;
      private final int width;
      private final int height;
      private final int mipmapLevelHolder;
      private boolean rotated;
      private float scaleFactor = 1.0F;

      public Holder(TextureAtlasSprite var1, int var2) {
         this.sprite = ☃;
         this.width = ☃.getIconWidth();
         this.height = ☃.getIconHeight();
         this.mipmapLevelHolder = ☃;
         this.rotated = Stitcher.getMipmapDimension(this.height, ☃) > Stitcher.getMipmapDimension(this.width, ☃);
      }

      public TextureAtlasSprite getAtlasSprite() {
         return this.sprite;
      }

      public int getWidth() {
         int ☃ = this.rotated ? this.height : this.width;
         return Stitcher.getMipmapDimension((int)(☃ * this.scaleFactor), this.mipmapLevelHolder);
      }

      public int getHeight() {
         int ☃ = this.rotated ? this.width : this.height;
         return Stitcher.getMipmapDimension((int)(☃ * this.scaleFactor), this.mipmapLevelHolder);
      }

      public void rotate() {
         this.rotated = !this.rotated;
      }

      public boolean isRotated() {
         return this.rotated;
      }

      public void setNewDimension(int var1) {
         if (this.width > ☃ && this.height > ☃) {
            this.scaleFactor = (float)☃ / Math.min(this.width, this.height);
         }
      }

      @Override
      public String toString() {
         return "Holder{width=" + this.width + ", height=" + this.height + '}';
      }

      public int compareTo(Stitcher.Holder var1) {
         int ☃;
         if (this.getHeight() == ☃.getHeight()) {
            if (this.getWidth() == ☃.getWidth()) {
               if (this.sprite.getIconName() == null) {
                  return ☃.sprite.getIconName() == null ? 0 : -1;
               }

               return this.sprite.getIconName().compareTo(☃.sprite.getIconName());
            }

            ☃ = this.getWidth() < ☃.getWidth() ? 1 : -1;
         } else {
            ☃ = this.getHeight() < ☃.getHeight() ? 1 : -1;
         }

         return ☃;
      }
   }

   public static class Slot {
      private final int originX;
      private final int originY;
      private final int width;
      private final int height;
      private List<Stitcher.Slot> subSlots;
      private Stitcher.Holder holder;

      public Slot(int var1, int var2, int var3, int var4) {
         this.originX = ☃;
         this.originY = ☃;
         this.width = ☃;
         this.height = ☃;
      }

      public Stitcher.Holder getStitchHolder() {
         return this.holder;
      }

      public int getOriginX() {
         return this.originX;
      }

      public int getOriginY() {
         return this.originY;
      }

      public boolean addSlot(Stitcher.Holder var1) {
         if (this.holder != null) {
            return false;
         } else {
            int ☃ = ☃.getWidth();
            int ☃x = ☃.getHeight();
            if (☃ <= this.width && ☃x <= this.height) {
               if (☃ == this.width && ☃x == this.height) {
                  this.holder = ☃;
                  return true;
               } else {
                  if (this.subSlots == null) {
                     this.subSlots = Lists.newArrayListWithCapacity(1);
                     this.subSlots.add(new Stitcher.Slot(this.originX, this.originY, ☃, ☃x));
                     int ☃xx = this.width - ☃;
                     int ☃xxx = this.height - ☃x;
                     if (☃xxx > 0 && ☃xx > 0) {
                        int ☃xxxx = Math.max(this.height, ☃xx);
                        int ☃xxxxx = Math.max(this.width, ☃xxx);
                        if (☃xxxx >= ☃xxxxx) {
                           this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + ☃x, ☃, ☃xxx));
                           this.subSlots.add(new Stitcher.Slot(this.originX + ☃, this.originY, ☃xx, this.height));
                        } else {
                           this.subSlots.add(new Stitcher.Slot(this.originX + ☃, this.originY, ☃xx, ☃x));
                           this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + ☃x, this.width, ☃xxx));
                        }
                     } else if (☃xx == 0) {
                        this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + ☃x, ☃, ☃xxx));
                     } else if (☃xxx == 0) {
                        this.subSlots.add(new Stitcher.Slot(this.originX + ☃, this.originY, ☃xx, ☃x));
                     }
                  }

                  for (Stitcher.Slot ☃xx : this.subSlots) {
                     if (☃xx.addSlot(☃)) {
                        return true;
                     }
                  }

                  return false;
               }
            } else {
               return false;
            }
         }
      }

      public void getAllStitchSlots(List<Stitcher.Slot> var1) {
         if (this.holder != null) {
            ☃.add(this);
         } else if (this.subSlots != null) {
            for (Stitcher.Slot ☃ : this.subSlots) {
               ☃.getAllStitchSlots(☃);
            }
         }
      }

      @Override
      public String toString() {
         return "Slot{originX="
            + this.originX
            + ", originY="
            + this.originY
            + ", width="
            + this.width
            + ", height="
            + this.height
            + ", texture="
            + this.holder
            + ", subSlots="
            + this.subSlots
            + '}';
      }
   }
}
