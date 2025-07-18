package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class FontRenderer implements IResourceManagerReloadListener {
   private static final ResourceLocation[] UNICODE_PAGE_LOCATIONS = new ResourceLocation[256];
   private final int[] charWidth = new int[256];
   public int FONT_HEIGHT = 9;
   public Random fontRandom = new Random();
   private final byte[] glyphWidth = new byte[65536];
   private final int[] colorCode = new int[32];
   private final ResourceLocation locationFontTexture;
   private final TextureManager renderEngine;
   private float posX;
   private float posY;
   private boolean unicodeFlag;
   private boolean bidiFlag;
   private float red;
   private float blue;
   private float green;
   private float alpha;
   private int textColor;
   private boolean randomStyle;
   private boolean boldStyle;
   private boolean italicStyle;
   private boolean underlineStyle;
   private boolean strikethroughStyle;

   public FontRenderer(GameSettings var1, ResourceLocation var2, TextureManager var3, boolean var4) {
      this.locationFontTexture = ☃;
      this.renderEngine = ☃;
      this.unicodeFlag = ☃;
      ☃.bindTexture(this.locationFontTexture);

      for (int ☃ = 0; ☃ < 32; ☃++) {
         int ☃x = (☃ >> 3 & 1) * 85;
         int ☃xx = (☃ >> 2 & 1) * 170 + ☃x;
         int ☃xxx = (☃ >> 1 & 1) * 170 + ☃x;
         int ☃xxxx = (☃ >> 0 & 1) * 170 + ☃x;
         if (☃ == 6) {
            ☃xx += 85;
         }

         if (☃.anaglyph) {
            int ☃xxxxx = (☃xx * 30 + ☃xxx * 59 + ☃xxxx * 11) / 100;
            int ☃xxxxxx = (☃xx * 30 + ☃xxx * 70) / 100;
            int ☃xxxxxxx = (☃xx * 30 + ☃xxxx * 70) / 100;
            ☃xx = ☃xxxxx;
            ☃xxx = ☃xxxxxx;
            ☃xxxx = ☃xxxxxxx;
         }

         if (☃ >= 16) {
            ☃xx /= 4;
            ☃xxx /= 4;
            ☃xxxx /= 4;
         }

         this.colorCode[☃] = (☃xx & 0xFF) << 16 | (☃xxx & 0xFF) << 8 | ☃xxxx & 0xFF;
      }

      this.readGlyphSizes();
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      this.readFontTexture();
      this.readGlyphSizes();
   }

   private void readFontTexture() {
      IResource ☃ = null;

      BufferedImage ☃x;
      try {
         ☃ = Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture);
         ☃x = TextureUtil.readBufferedImage(☃.getInputStream());
      } catch (IOException var20) {
         throw new RuntimeException(var20);
      } finally {
         IOUtils.closeQuietly(☃);
      }

      int var3 = ☃x.getWidth();
      int var4 = ☃x.getHeight();
      int[] var5 = new int[var3 * var4];
      ☃x.getRGB(0, 0, var3, var4, var5, 0, var3);
      int var6 = var4 / 16;
      int var7 = var3 / 16;
      boolean var8 = true;
      float var9 = 8.0F / var7;

      for (int var10 = 0; var10 < 256; var10++) {
         int ☃xx = var10 % 16;
         int ☃xxx = var10 / 16;
         if (var10 == 32) {
            this.charWidth[var10] = 4;
         }

         int ☃xxxx;
         for (☃xxxx = var7 - 1; ☃xxxx >= 0; ☃xxxx--) {
            int ☃xxxxx = ☃xx * var7 + ☃xxxx;
            boolean ☃xxxxxx = true;

            for (int ☃xxxxxxx = 0; ☃xxxxxxx < var6 && ☃xxxxxx; ☃xxxxxxx++) {
               int ☃xxxxxxxx = (☃xxx * var7 + ☃xxxxxxx) * var3;
               if ((var5[☃xxxxx + ☃xxxxxxxx] >> 24 & 0xFF) != 0) {
                  ☃xxxxxx = false;
               }
            }

            if (!☃xxxxxx) {
               break;
            }
         }

         this.charWidth[var10] = (int)(0.5 + ++☃xxxx * var9) + 1;
      }
   }

   private void readGlyphSizes() {
      IResource ☃ = null;

      try {
         ☃ = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin"));
         ☃.getInputStream().read(this.glyphWidth);
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      } finally {
         IOUtils.closeQuietly(☃);
      }
   }

   private float renderChar(char var1, boolean var2) {
      if (☃ == ' ') {
         return 4.0F;
      } else {
         int ☃ = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
            .indexOf(☃);
         return ☃ != -1 && !this.unicodeFlag ? this.renderDefaultChar(☃, ☃) : this.renderUnicodeChar(☃, ☃);
      }
   }

   private float renderDefaultChar(int var1, boolean var2) {
      int ☃ = ☃ % 16 * 8;
      int ☃x = ☃ / 16 * 8;
      int ☃xx = ☃ ? 1 : 0;
      this.renderEngine.bindTexture(this.locationFontTexture);
      int ☃xxx = this.charWidth[☃];
      float ☃xxxx = ☃xxx - 0.01F;
      GlStateManager.glBegin(5);
      GlStateManager.glTexCoord2f(☃ / 128.0F, ☃x / 128.0F);
      GlStateManager.glVertex3f(this.posX + ☃xx, this.posY, 0.0F);
      GlStateManager.glTexCoord2f(☃ / 128.0F, (☃x + 7.99F) / 128.0F);
      GlStateManager.glVertex3f(this.posX - ☃xx, this.posY + 7.99F, 0.0F);
      GlStateManager.glTexCoord2f((☃ + ☃xxxx - 1.0F) / 128.0F, ☃x / 128.0F);
      GlStateManager.glVertex3f(this.posX + ☃xxxx - 1.0F + ☃xx, this.posY, 0.0F);
      GlStateManager.glTexCoord2f((☃ + ☃xxxx - 1.0F) / 128.0F, (☃x + 7.99F) / 128.0F);
      GlStateManager.glVertex3f(this.posX + ☃xxxx - 1.0F - ☃xx, this.posY + 7.99F, 0.0F);
      GlStateManager.glEnd();
      return ☃xxx;
   }

   private ResourceLocation getUnicodePageLocation(int var1) {
      if (UNICODE_PAGE_LOCATIONS[☃] == null) {
         UNICODE_PAGE_LOCATIONS[☃] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", ☃));
      }

      return UNICODE_PAGE_LOCATIONS[☃];
   }

   private void loadGlyphTexture(int var1) {
      this.renderEngine.bindTexture(this.getUnicodePageLocation(☃));
   }

   private float renderUnicodeChar(char var1, boolean var2) {
      int ☃ = this.glyphWidth[☃] & 255;
      if (☃ == 0) {
         return 0.0F;
      } else {
         int ☃x = ☃ / 256;
         this.loadGlyphTexture(☃x);
         int ☃xx = ☃ >>> 4;
         int ☃xxx = ☃ & 15;
         float ☃xxxx = ☃xx;
         float ☃xxxxx = ☃xxx + 1;
         float ☃xxxxxx = ☃ % 16 * 16 + ☃xxxx;
         float ☃xxxxxxx = (☃ & 255) / 16 * 16;
         float ☃xxxxxxxx = ☃xxxxx - ☃xxxx - 0.02F;
         float ☃xxxxxxxxx = ☃ ? 1.0F : 0.0F;
         GlStateManager.glBegin(5);
         GlStateManager.glTexCoord2f(☃xxxxxx / 256.0F, ☃xxxxxxx / 256.0F);
         GlStateManager.glVertex3f(this.posX + ☃xxxxxxxxx, this.posY, 0.0F);
         GlStateManager.glTexCoord2f(☃xxxxxx / 256.0F, (☃xxxxxxx + 15.98F) / 256.0F);
         GlStateManager.glVertex3f(this.posX - ☃xxxxxxxxx, this.posY + 7.99F, 0.0F);
         GlStateManager.glTexCoord2f((☃xxxxxx + ☃xxxxxxxx) / 256.0F, ☃xxxxxxx / 256.0F);
         GlStateManager.glVertex3f(this.posX + ☃xxxxxxxx / 2.0F + ☃xxxxxxxxx, this.posY, 0.0F);
         GlStateManager.glTexCoord2f((☃xxxxxx + ☃xxxxxxxx) / 256.0F, (☃xxxxxxx + 15.98F) / 256.0F);
         GlStateManager.glVertex3f(this.posX + ☃xxxxxxxx / 2.0F - ☃xxxxxxxxx, this.posY + 7.99F, 0.0F);
         GlStateManager.glEnd();
         return (☃xxxxx - ☃xxxx) / 2.0F + 1.0F;
      }
   }

   public int drawStringWithShadow(String var1, float var2, float var3, int var4) {
      return this.drawString(☃, ☃, ☃, ☃, true);
   }

   public int drawString(String var1, int var2, int var3, int var4) {
      return this.drawString(☃, ☃, ☃, ☃, false);
   }

   public int drawString(String var1, float var2, float var3, int var4, boolean var5) {
      GlStateManager.enableAlpha();
      this.resetStyles();
      int ☃;
      if (☃) {
         ☃ = this.renderString(☃, ☃ + 1.0F, ☃ + 1.0F, ☃, true);
         ☃ = Math.max(☃, this.renderString(☃, ☃, ☃, ☃, false));
      } else {
         ☃ = this.renderString(☃, ☃, ☃, ☃, false);
      }

      return ☃;
   }

   private String bidiReorder(String var1) {
      try {
         Bidi ☃ = new Bidi(new ArabicShaping(8).shape(☃), 127);
         ☃.setReorderingMode(0);
         return ☃.writeReordered(2);
      } catch (ArabicShapingException var3) {
         return ☃;
      }
   }

   private void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   private void renderStringAtPos(String var1, boolean var2) {
      for (int ☃ = 0; ☃ < ☃.length(); ☃++) {
         char ☃x = ☃.charAt(☃);
         if (☃x == 167 && ☃ + 1 < ☃.length()) {
            int ☃xx = "0123456789abcdefklmnor".indexOf(String.valueOf(☃.charAt(☃ + 1)).toLowerCase(Locale.ROOT).charAt(0));
            if (☃xx < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (☃xx < 0 || ☃xx > 15) {
                  ☃xx = 15;
               }

               if (☃) {
                  ☃xx += 16;
               }

               int ☃xxx = this.colorCode[☃xx];
               this.textColor = ☃xxx;
               GlStateManager.color((☃xxx >> 16) / 255.0F, (☃xxx >> 8 & 0xFF) / 255.0F, (☃xxx & 0xFF) / 255.0F, this.alpha);
            } else if (☃xx == 16) {
               this.randomStyle = true;
            } else if (☃xx == 17) {
               this.boldStyle = true;
            } else if (☃xx == 18) {
               this.strikethroughStyle = true;
            } else if (☃xx == 19) {
               this.underlineStyle = true;
            } else if (☃xx == 20) {
               this.italicStyle = true;
            } else if (☃xx == 21) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               GlStateManager.color(this.red, this.blue, this.green, this.alpha);
            }

            ☃++;
         } else {
            int ☃xx = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
               .indexOf(☃x);
            if (this.randomStyle && ☃xx != -1) {
               int ☃xxx = this.getCharWidth(☃x);

               char ☃xxxx;
               do {
                  ☃xx = this.fontRandom
                     .nextInt(
                        "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
                           .length()
                     );
                  ☃xxxx = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
                     .charAt(☃xx);
               } while (☃xxx != this.getCharWidth(☃xxxx));

               ☃x = ☃xxxx;
            }

            float ☃xxx = this.unicodeFlag ? 0.5F : 1.0F;
            boolean ☃xxxx = (☃x == 0 || ☃xx == -1 || this.unicodeFlag) && ☃;
            if (☃xxxx) {
               this.posX -= ☃xxx;
               this.posY -= ☃xxx;
            }

            float ☃xxxxx = this.renderChar(☃x, this.italicStyle);
            if (☃xxxx) {
               this.posX += ☃xxx;
               this.posY += ☃xxx;
            }

            if (this.boldStyle) {
               this.posX += ☃xxx;
               if (☃xxxx) {
                  this.posX -= ☃xxx;
                  this.posY -= ☃xxx;
               }

               this.renderChar(☃x, this.italicStyle);
               this.posX -= ☃xxx;
               if (☃xxxx) {
                  this.posX += ☃xxx;
                  this.posY += ☃xxx;
               }

               ☃xxxxx++;
            }

            if (this.strikethroughStyle) {
               Tessellator ☃xxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxx = ☃xxxxxx.getBuffer();
               GlStateManager.disableTexture2D();
               ☃xxxxxxx.begin(7, DefaultVertexFormats.POSITION);
               ☃xxxxxxx.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
               ☃xxxxxxx.pos(this.posX + ☃xxxxx, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
               ☃xxxxxxx.pos(this.posX + ☃xxxxx, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0).endVertex();
               ☃xxxxxxx.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0).endVertex();
               ☃xxxxxx.draw();
               GlStateManager.enableTexture2D();
            }

            if (this.underlineStyle) {
               Tessellator ☃xxxxxx = Tessellator.getInstance();
               BufferBuilder ☃xxxxxxx = ☃xxxxxx.getBuffer();
               GlStateManager.disableTexture2D();
               ☃xxxxxxx.begin(7, DefaultVertexFormats.POSITION);
               int ☃xxxxxxxx = this.underlineStyle ? -1 : 0;
               ☃xxxxxxx.pos(this.posX + ☃xxxxxxxx, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
               ☃xxxxxxx.pos(this.posX + ☃xxxxx, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
               ☃xxxxxxx.pos(this.posX + ☃xxxxx, this.posY + this.FONT_HEIGHT - 1.0F, 0.0).endVertex();
               ☃xxxxxxx.pos(this.posX + ☃xxxxxxxx, this.posY + this.FONT_HEIGHT - 1.0F, 0.0).endVertex();
               ☃xxxxxx.draw();
               GlStateManager.enableTexture2D();
            }

            this.posX += (int)☃xxxxx;
         }
      }
   }

   private int renderStringAligned(String var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.bidiFlag) {
         int ☃ = this.getStringWidth(this.bidiReorder(☃));
         ☃ = ☃ + ☃ - ☃;
      }

      return this.renderString(☃, ☃, ☃, ☃, ☃);
   }

   private int renderString(String var1, float var2, float var3, int var4, boolean var5) {
      if (☃ == null) {
         return 0;
      } else {
         if (this.bidiFlag) {
            ☃ = this.bidiReorder(☃);
         }

         if ((☃ & -67108864) == 0) {
            ☃ |= -16777216;
         }

         if (☃) {
            ☃ = (☃ & 16579836) >> 2 | ☃ & 0xFF000000;
         }

         this.red = (☃ >> 16 & 0xFF) / 255.0F;
         this.blue = (☃ >> 8 & 0xFF) / 255.0F;
         this.green = (☃ & 0xFF) / 255.0F;
         this.alpha = (☃ >> 24 & 0xFF) / 255.0F;
         GlStateManager.color(this.red, this.blue, this.green, this.alpha);
         this.posX = ☃;
         this.posY = ☃;
         this.renderStringAtPos(☃, ☃);
         return (int)this.posX;
      }
   }

   public int getStringWidth(String var1) {
      if (☃ == null) {
         return 0;
      } else {
         int ☃ = 0;
         boolean ☃x = false;

         for (int ☃xx = 0; ☃xx < ☃.length(); ☃xx++) {
            char ☃xxx = ☃.charAt(☃xx);
            int ☃xxxx = this.getCharWidth(☃xxx);
            if (☃xxxx < 0 && ☃xx < ☃.length() - 1) {
               ☃xxx = ☃.charAt(++☃xx);
               if (☃xxx == 'l' || ☃xxx == 'L') {
                  ☃x = true;
               } else if (☃xxx == 'r' || ☃xxx == 'R') {
                  ☃x = false;
               }

               ☃xxxx = 0;
            }

            ☃ += ☃xxxx;
            if (☃x && ☃xxxx > 0) {
               ☃++;
            }
         }

         return ☃;
      }
   }

   public int getCharWidth(char var1) {
      if (☃ == 167) {
         return -1;
      } else if (☃ == ' ') {
         return 4;
      } else {
         int ☃ = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
            .indexOf(☃);
         if (☃ > 0 && ☃ != -1 && !this.unicodeFlag) {
            return this.charWidth[☃];
         } else if (this.glyphWidth[☃] != 0) {
            int ☃x = this.glyphWidth[☃] & 255;
            int ☃xx = ☃x >>> 4;
            int ☃xxx = ☃x & 15;
            ☃xxx++;
            return (☃xxx - ☃xx) / 2 + 1;
         } else {
            return 0;
         }
      }
   }

   public String trimStringToWidth(String var1, int var2) {
      return this.trimStringToWidth(☃, ☃, false);
   }

   public String trimStringToWidth(String var1, int var2, boolean var3) {
      StringBuilder ☃ = new StringBuilder();
      int ☃x = 0;
      int ☃xx = ☃ ? ☃.length() - 1 : 0;
      int ☃xxx = ☃ ? -1 : 1;
      boolean ☃xxxx = false;
      boolean ☃xxxxx = false;

      for (int ☃xxxxxx = ☃xx; ☃xxxxxx >= 0 && ☃xxxxxx < ☃.length() && ☃x < ☃; ☃xxxxxx += ☃xxx) {
         char ☃xxxxxxx = ☃.charAt(☃xxxxxx);
         int ☃xxxxxxxx = this.getCharWidth(☃xxxxxxx);
         if (☃xxxx) {
            ☃xxxx = false;
            if (☃xxxxxxx == 'l' || ☃xxxxxxx == 'L') {
               ☃xxxxx = true;
            } else if (☃xxxxxxx == 'r' || ☃xxxxxxx == 'R') {
               ☃xxxxx = false;
            }
         } else if (☃xxxxxxxx < 0) {
            ☃xxxx = true;
         } else {
            ☃x += ☃xxxxxxxx;
            if (☃xxxxx) {
               ☃x++;
            }
         }

         if (☃x > ☃) {
            break;
         }

         if (☃) {
            ☃.insert(0, ☃xxxxxxx);
         } else {
            ☃.append(☃xxxxxxx);
         }
      }

      return ☃.toString();
   }

   private String trimStringNewline(String var1) {
      while (☃ != null && ☃.endsWith("\n")) {
         ☃ = ☃.substring(0, ☃.length() - 1);
      }

      return ☃;
   }

   public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
      this.resetStyles();
      this.textColor = ☃;
      ☃ = this.trimStringNewline(☃);
      this.renderSplitString(☃, ☃, ☃, ☃, false);
   }

   private void renderSplitString(String var1, int var2, int var3, int var4, boolean var5) {
      for (String ☃ : this.listFormattedStringToWidth(☃, ☃)) {
         this.renderStringAligned(☃, ☃, ☃, ☃, this.textColor, ☃);
         ☃ += this.FONT_HEIGHT;
      }
   }

   public int getWordWrappedHeight(String var1, int var2) {
      return this.FONT_HEIGHT * this.listFormattedStringToWidth(☃, ☃).size();
   }

   public void setUnicodeFlag(boolean var1) {
      this.unicodeFlag = ☃;
   }

   public boolean getUnicodeFlag() {
      return this.unicodeFlag;
   }

   public void setBidiFlag(boolean var1) {
      this.bidiFlag = ☃;
   }

   public List<String> listFormattedStringToWidth(String var1, int var2) {
      return Arrays.asList(this.wrapFormattedStringToWidth(☃, ☃).split("\n"));
   }

   String wrapFormattedStringToWidth(String var1, int var2) {
      int ☃ = this.sizeStringToWidth(☃, ☃);
      if (☃.length() <= ☃) {
         return ☃;
      } else {
         String ☃x = ☃.substring(0, ☃);
         char ☃xx = ☃.charAt(☃);
         boolean ☃xxx = ☃xx == ' ' || ☃xx == '\n';
         String ☃xxxx = getFormatFromString(☃x) + ☃.substring(☃ + (☃xxx ? 1 : 0));
         return ☃x + "\n" + this.wrapFormattedStringToWidth(☃xxxx, ☃);
      }
   }

   private int sizeStringToWidth(String var1, int var2) {
      int ☃ = ☃.length();
      int ☃x = 0;
      int ☃xx = 0;
      int ☃xxx = -1;

      for (boolean ☃xxxx = false; ☃xx < ☃; ☃xx++) {
         char ☃xxxxx = ☃.charAt(☃xx);
         switch (☃xxxxx) {
            case '\n':
               ☃xx--;
               break;
            case ' ':
               ☃xxx = ☃xx;
            default:
               ☃x += this.getCharWidth(☃xxxxx);
               if (☃xxxx) {
                  ☃x++;
               }
               break;
            case '§':
               if (☃xx < ☃ - 1) {
                  char ☃xxxxxx = ☃.charAt(++☃xx);
                  if (☃xxxxxx == 'l' || ☃xxxxxx == 'L') {
                     ☃xxxx = true;
                  } else if (☃xxxxxx == 'r' || ☃xxxxxx == 'R' || isFormatColor(☃xxxxxx)) {
                     ☃xxxx = false;
                  }
               }
         }

         if (☃xxxxx == '\n') {
            ☃xxx = ++☃xx;
            break;
         }

         if (☃x > ☃) {
            break;
         }
      }

      return ☃xx != ☃ && ☃xxx != -1 && ☃xxx < ☃xx ? ☃xxx : ☃xx;
   }

   private static boolean isFormatColor(char var0) {
      return ☃ >= '0' && ☃ <= '9' || ☃ >= 'a' && ☃ <= 'f' || ☃ >= 'A' && ☃ <= 'F';
   }

   private static boolean isFormatSpecial(char var0) {
      return ☃ >= 'k' && ☃ <= 'o' || ☃ >= 'K' && ☃ <= 'O' || ☃ == 'r' || ☃ == 'R';
   }

   public static String getFormatFromString(String var0) {
      String ☃ = "";
      int ☃x = -1;
      int ☃xx = ☃.length();

      while ((☃x = ☃.indexOf(167, ☃x + 1)) != -1) {
         if (☃x < ☃xx - 1) {
            char ☃xxx = ☃.charAt(☃x + 1);
            if (isFormatColor(☃xxx)) {
               ☃ = "§" + ☃xxx;
            } else if (isFormatSpecial(☃xxx)) {
               ☃ = ☃ + "§" + ☃xxx;
            }
         }
      }

      return ☃;
   }

   public boolean getBidiFlag() {
      return this.bidiFlag;
   }

   public int getColorCode(char var1) {
      int ☃ = "0123456789abcdef".indexOf(☃);
      return ☃ >= 0 && ☃ < this.colorCode.length ? this.colorCode[☃] : -1;
   }
}
