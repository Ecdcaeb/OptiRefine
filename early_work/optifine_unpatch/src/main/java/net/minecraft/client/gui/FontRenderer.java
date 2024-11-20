/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.cleanroommc.client.BreakIteratorHolder
 *  com.ibm.icu.text.ArabicShaping
 *  com.ibm.icu.text.ArabicShapingException
 *  com.ibm.icu.text.Bidi
 *  java.awt.image.BufferedImage
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.CharSequence
 *  java.lang.Deprecated
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.List
 *  java.util.Locale
 *  java.util.Random
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.io.IOUtils
 */
package net.minecraft.client.gui;

import com.cleanroommc.client.BreakIteratorHolder;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

@SideOnly(value=Side.CLIENT)
public class FontRenderer
implements IResourceManagerReloadListener {
    private static final ResourceLocation[] UNICODE_PAGE_LOCATIONS = new ResourceLocation[256];
    protected final int[] charWidth = new int[256];
    public int FONT_HEIGHT = 9;
    public Random fontRandom = new Random();
    protected final byte[] glyphWidth = new byte[65536];
    private final int[] colorCode = new int[32];
    protected final ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    protected float posX;
    protected float posY;
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

    public FontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        this.locationFontTexture = location;
        this.renderEngine = textureManagerIn;
        this.unicodeFlag = unicode;
        this.bindTexture(this.locationFontTexture);
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (gameSettingsIn.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
        this.readGlyphSizes();
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.readFontTexture();
        this.readGlyphSizes();
    }

    private void readFontTexture() {
        BufferedImage bufferedimage;
        IResource iresource = null;
        try {
            iresource = this.getResource(this.locationFontTexture);
            bufferedimage = TextureUtil.readBufferedImage((InputStream)iresource.getInputStream());
        }
        catch (IOException ioexception) {
            try {
                throw new RuntimeException((Throwable)ioexception);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(iresource);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Closeable)iresource);
        int lvt_3_2_ = bufferedimage.getWidth();
        int lvt_4_1_ = bufferedimage.getHeight();
        int[] lvt_5_1_ = new int[lvt_3_2_ * lvt_4_1_];
        bufferedimage.getRGB(0, 0, lvt_3_2_, lvt_4_1_, lvt_5_1_, 0, lvt_3_2_);
        int lvt_6_1_ = lvt_4_1_ / 16;
        int lvt_7_1_ = lvt_3_2_ / 16;
        boolean lvt_8_1_ = true;
        float lvt_9_1_ = 8.0f / (float)lvt_7_1_;
        for (int lvt_10_1_ = 0; lvt_10_1_ < 256; ++lvt_10_1_) {
            int l1;
            int j1 = lvt_10_1_ % 16;
            int k1 = lvt_10_1_ / 16;
            if (lvt_10_1_ == 32) {
                this.charWidth[lvt_10_1_] = 4;
            }
            for (l1 = lvt_7_1_ - 1; l1 >= 0; --l1) {
                int i2 = j1 * lvt_7_1_ + l1;
                boolean flag1 = true;
                for (int j2 = 0; j2 < lvt_6_1_ && flag1; ++j2) {
                    int k2 = (k1 * lvt_7_1_ + j2) * lvt_3_2_;
                    if ((lvt_5_1_[i2 + k2] >> 24 & 0xFF) == 0) continue;
                    flag1 = false;
                }
                if (!flag1) break;
            }
            this.charWidth[lvt_10_1_] = (int)(0.5 + (double)((float)(++l1) * lvt_9_1_)) + 1;
        }
    }

    private void readGlyphSizes() {
        IResource iresource = null;
        try {
            iresource = this.getResource(new ResourceLocation("font/glyph_sizes.bin"));
            iresource.getInputStream().read(this.glyphWidth);
        }
        catch (IOException ioexception) {
            try {
                throw new RuntimeException((Throwable)ioexception);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(iresource);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Closeable)iresource);
    }

    private float renderChar(char ch, boolean italic) {
        if (ch == '\u00a0') {
            return 4.0f;
        }
        if (ch == ' ') {
            return 4.0f;
        }
        int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf((int)ch);
        return i != -1 && !this.unicodeFlag ? this.renderDefaultChar(i, italic) : this.renderUnicodeChar(ch, italic);
    }

    protected float renderDefaultChar(int ch, boolean italic) {
        int i = ch % 16 * 8;
        int j = ch / 16 * 8;
        boolean k = italic;
        this.bindTexture(this.locationFontTexture);
        int l = this.charWidth[ch];
        float f = (float)l - 0.01f;
        GlStateManager.glBegin((int)5);
        GlStateManager.glTexCoord2f((float)((float)i / 128.0f), (float)((float)j / 128.0f));
        GlStateManager.glVertex3f((float)(this.posX + (float)k), (float)this.posY, (float)0.0f);
        GlStateManager.glTexCoord2f((float)((float)i / 128.0f), (float)(((float)j + 7.99f) / 128.0f));
        GlStateManager.glVertex3f((float)(this.posX - (float)k), (float)(this.posY + 7.99f), (float)0.0f);
        GlStateManager.glTexCoord2f((float)(((float)i + f - 1.0f) / 128.0f), (float)((float)j / 128.0f));
        GlStateManager.glVertex3f((float)(this.posX + f - 1.0f + (float)k), (float)this.posY, (float)0.0f);
        GlStateManager.glTexCoord2f((float)(((float)i + f - 1.0f) / 128.0f), (float)(((float)j + 7.99f) / 128.0f));
        GlStateManager.glVertex3f((float)(this.posX + f - 1.0f - (float)k), (float)(this.posY + 7.99f), (float)0.0f);
        GlStateManager.glEnd();
        return l;
    }

    private ResourceLocation getUnicodePageLocation(int page) {
        if (UNICODE_PAGE_LOCATIONS[page] == null) {
            FontRenderer.UNICODE_PAGE_LOCATIONS[page] = new ResourceLocation(String.format((String)"textures/font/unicode_page_%02x.png", (Object[])new Object[]{page}));
        }
        return UNICODE_PAGE_LOCATIONS[page];
    }

    private void loadGlyphTexture(int page) {
        this.bindTexture(this.getUnicodePageLocation(page));
    }

    protected float renderUnicodeChar(char ch, boolean italic) {
        int i = this.glyphWidth[ch] & 0xFF;
        if (i == 0) {
            return 0.0f;
        }
        int j = ch / 256;
        this.loadGlyphTexture(j);
        int k = i >>> 4;
        int l = i & 0xF;
        float f = k;
        float f1 = l + 1;
        float f2 = (float)(ch % 16 * 16) + f;
        float f3 = (ch & 0xFF) / 16 * 16;
        float f4 = f1 - f - 0.02f;
        float f5 = italic ? 1.0f : 0.0f;
        GlStateManager.glBegin((int)5);
        GlStateManager.glTexCoord2f((float)(f2 / 256.0f), (float)(f3 / 256.0f));
        GlStateManager.glVertex3f((float)(this.posX + f5), (float)this.posY, (float)0.0f);
        GlStateManager.glTexCoord2f((float)(f2 / 256.0f), (float)((f3 + 15.98f) / 256.0f));
        GlStateManager.glVertex3f((float)(this.posX - f5), (float)(this.posY + 7.99f), (float)0.0f);
        GlStateManager.glTexCoord2f((float)((f2 + f4) / 256.0f), (float)(f3 / 256.0f));
        GlStateManager.glVertex3f((float)(this.posX + f4 / 2.0f + f5), (float)this.posY, (float)0.0f);
        GlStateManager.glTexCoord2f((float)((f2 + f4) / 256.0f), (float)((f3 + 15.98f) / 256.0f));
        GlStateManager.glVertex3f((float)(this.posX + f4 / 2.0f - f5), (float)(this.posY + 7.99f), (float)0.0f);
        GlStateManager.glEnd();
        return (f1 - f) / 2.0f + 1.0f;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, true);
    }

    public int drawString(String text, int x, int y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        int i;
        this.enableAlpha();
        this.resetStyles();
        if (dropShadow) {
            i = this.renderString(text, x + 1.0f, y + 1.0f, color, true);
            i = Math.max((int)i, (int)this.renderString(text, x, y, color, false));
        } else {
            i = this.renderString(text, x, y, color, false);
        }
        return i;
    }

    private String bidiReorder(String text) {
        try {
            Bidi bidi = new Bidi(new ArabicShaping(8).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException arabicshapingexception) {
            return text;
        }
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderStringAtPos(String text, boolean shadow) {
        for (int i = 0; i < text.length(); ++i) {
            boolean flag;
            char c0 = text.charAt(i);
            if (c0 == '\u00a7' && i + 1 < text.length() && (FontRenderer.isFormatColor(text.charAt(i + 1)) || FontRenderer.isFormatSpecial(text.charAt(i + 1)))) {
                int i1 = "0123456789abcdefklmnor".indexOf((int)String.valueOf((char)text.charAt(i + 1)).toLowerCase(Locale.ROOT).charAt(0));
                if (i1 < 16) {
                    int j1;
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (shadow) {
                        i1 += 16;
                    }
                    this.textColor = j1 = this.colorCode[i1];
                    this.setColor((float)(j1 >> 16) / 255.0f, (float)(j1 >> 8 & 0xFF) / 255.0f, (float)(j1 & 0xFF) / 255.0f, this.alpha);
                } else if (i1 == 16) {
                    this.randomStyle = true;
                } else if (i1 == 17) {
                    this.boldStyle = true;
                } else if (i1 == 18) {
                    this.strikethroughStyle = true;
                } else if (i1 == 19) {
                    this.underlineStyle = true;
                } else if (i1 == 20) {
                    this.italicStyle = true;
                } else if (i1 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
                continue;
            }
            int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf((int)c0);
            if (this.randomStyle && j != -1) {
                char c1;
                int k = this.getCharWidth(c0);
                while (k != this.getCharWidth(c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length())))) {
                }
                c0 = c1;
            }
            float f1 = j == -1 || this.unicodeFlag ? 0.5f : 1.0f;
            boolean bl = flag = (c0 == '\u0000' || j == -1 || this.unicodeFlag) && shadow;
            if (flag) {
                this.posX -= f1;
                this.posY -= f1;
            }
            float f = this.renderChar(c0, this.italicStyle);
            if (flag) {
                this.posX += f1;
                this.posY += f1;
            }
            if (this.boldStyle) {
                this.posX += f1;
                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }
                this.renderChar(c0, this.italicStyle);
                this.posX -= f1;
                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }
                f += 1.0f;
            }
            this.doDraw(f);
        }
    }

    protected void doDraw(float f) {
        if (this.strikethroughStyle) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferbuilder.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0).endVertex();
            bufferbuilder.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0).endVertex();
            bufferbuilder.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f), 0.0).endVertex();
            bufferbuilder.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0f), 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        if (this.underlineStyle) {
            Tessellator tessellator1 = Tessellator.getInstance();
            BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
            GlStateManager.disableTexture2D();
            bufferbuilder1.begin(7, DefaultVertexFormats.POSITION);
            int l = this.underlineStyle ? -1 : 0;
            bufferbuilder1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0).endVertex();
            bufferbuilder1.pos((double)(this.posX + f), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0).endVertex();
            bufferbuilder1.pos((double)(this.posX + f), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0f), 0.0).endVertex();
            bufferbuilder1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0f), 0.0).endVertex();
            tessellator1.draw();
            GlStateManager.enableTexture2D();
        }
        this.posX += (float)((int)f);
    }

    private int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow) {
        if (this.bidiFlag) {
            int i = this.getStringWidth(this.bidiReorder(text));
            x = x + width - i;
        }
        return this.renderString(text, x, y, color, dropShadow);
    }

    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if (this.bidiFlag) {
            text = this.bidiReorder(text);
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        this.red = (float)(color >> 16 & 0xFF) / 255.0f;
        this.blue = (float)(color >> 8 & 0xFF) / 255.0f;
        this.green = (float)(color & 0xFF) / 255.0f;
        this.alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        this.setColor(this.red, this.blue, this.green, this.alpha);
        this.posX = x;
        this.posY = y;
        this.renderStringAtPos(text, dropShadow);
        return (int)this.posX;
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int i = 0;
        boolean flag = false;
        for (int j = 0; j < text.length(); ++j) {
            char c0 = text.charAt(j);
            int k = this.getCharWidth(c0);
            if (k < 0 && j < text.length() - 1) {
                if ((c0 = text.charAt(++j)) == 'l' || c0 == 'L') {
                    flag = true;
                } else if (c0 == 'r' || c0 == 'R') {
                    flag = false;
                }
                k = 0;
            }
            i += k;
            if (!flag || k <= 0) continue;
            ++i;
        }
        return i;
    }

    public int getCharWidth(char character) {
        if (character == '\u00a0') {
            return 4;
        }
        if (character == '\u00a7') {
            return -1;
        }
        if (character == ' ') {
            return 4;
        }
        int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf((int)character);
        if (character > '\u0000' && i != -1 && !this.unicodeFlag) {
            return this.charWidth[i];
        }
        if (this.glyphWidth[character] != 0) {
            int j = this.glyphWidth[character] & 0xFF;
            int k = j >>> 4;
            int l = j & 0xF;
            return (++l - k) / 2 + 1;
        }
        return 0;
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {
            char c0 = text.charAt(l);
            int i1 = this.getCharWidth(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0) {
                flag = true;
            } else {
                i += i1;
                if (flag1) {
                    ++i;
                }
            }
            if (i > width) break;
            if (reverse) {
                stringbuilder.insert(0, c0);
                continue;
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.toString();
    }

    private String trimStringNewline(String text) {
        while (text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, false);
    }

    private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
        for (String s : this.listFormattedStringToWidth(str, wrapWidth)) {
            this.renderStringAligned(s, x, y, wrapWidth, this.textColor, addShadow);
            y += this.FONT_HEIGHT;
        }
    }

    public int getWordWrappedHeight(String str, int maxLength) {
        return this.FONT_HEIGHT * this.listFormattedStringToWidth(str, maxLength).size();
    }

    public void setUnicodeFlag(boolean unicodeFlagIn) {
        this.unicodeFlag = unicodeFlagIn;
    }

    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }

    public void setBidiFlag(boolean bidiFlagIn) {
        this.bidiFlag = bidiFlagIn;
    }

    public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
        if (str.isEmpty()) {
            return Collections.singletonList((Object)"");
        }
        BreakIteratorHolder.BREAK_ITERATOR.setText(str);
        ArrayList list = new ArrayList();
        int lineWidth = 0;
        int fed = 0;
        int prevFormat = 0;
        StringBuilder format = new StringBuilder();
        int[] widths = new int[str.length()];
        String[] formats = new String[str.length()];
        StringBuilder line = new StringBuilder();
        char[] chars = str.toCharArray();
        boolean bold = false;
        block4: for (int i = 0; i < chars.length; ++i) {
            char current = chars[i];
            switch (current) {
                case '\n': {
                    list.add((Object)line.toString());
                    fed = i + 1;
                    line.delete(0, line.length()).append((CharSequence)format);
                    prevFormat = format.length();
                    widths[0] = lineWidth = 0;
                    formats[0] = format.toString();
                    continue block4;
                }
                case '\u00a7': {
                    char f;
                    boolean isC;
                    if (i + 1 < chars.length && ((isC = FontRenderer.isFormatColor(f = chars[i + 1])) || FontRenderer.isFormatSpecial(f))) {
                        if (f != 'l' && f != 'L') {
                            if (f == 'r' || f == 'R') {
                                bold = false;
                                format.delete(0, format.length());
                            } else if (isC) {
                                bold = false;
                            }
                        } else {
                            bold = true;
                        }
                        format.append('\u00a7').append(f);
                        line.append('\u00a7').append(f);
                        widths[i - fed] = lineWidth;
                        widths[i - fed + 1] = lineWidth;
                        formats[i - fed] = format.toString();
                        formats[i - fed + 1] = format.toString();
                        ++i;
                        continue block4;
                    }
                }
                default: {
                    line.append(current);
                    lineWidth += this.getCharWidth(current);
                    if (bold) {
                        // empty if block
                    }
                    widths[i - fed] = ++lineWidth;
                    formats[i - fed] = format.toString();
                    if (lineWidth <= wrapWidth) continue block4;
                    int icui = BreakIteratorHolder.BREAK_ITERATOR.isBoundary(i) ? i : BreakIteratorHolder.BREAK_ITERATOR.preceding(i);
                    if (icui <= fed + 1 || i == icui) {
                        list.add((Object)line.substring(0, line.length() - 1));
                        fed = i;
                        line.delete(0, line.length()).append((CharSequence)format).append(current);
                        prevFormat = format.length();
                        lineWidth = this.getCharWidth(current);
                        continue block4;
                    }
                    int d = icui - fed;
                    if (line.charAt(d + prevFormat - 2) == '\u00a7') {
                        ++d;
                    }
                    list.add((Object)line.substring(0, d + prevFormat));
                    String temp = line.substring(d + prevFormat);
                    fed += d;
                    line.delete(0, line.length()).append(formats[d]).append(temp);
                    prevFormat = formats[d].length();
                    lineWidth -= widths[d - 1];
                }
            }
        }
        list.add((Object)line.toString());
        return list;
    }

    @Deprecated
    String wrapFormattedStringToWidth(String str, int wrapWidth) {
        int i = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= i) {
            return str;
        }
        String s = str.substring(0, i);
        char c0 = str.charAt(i);
        boolean flag = c0 == ' ' || c0 == '\n';
        String s1 = FontRenderer.getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
        return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
    }

    @Deprecated
    private int sizeStringToWidth(String str, int wrapWidth) {
        int k;
        int i = str.length();
        int j = 0;
        int l = -1;
        boolean flag = false;
        for (k = 0; k < i; ++k) {
            char c0 = str.charAt(k);
            switch (c0) {
                case '\n': {
                    --k;
                    break;
                }
                case ' ': {
                    l = k;
                }
                default: {
                    j += this.getCharWidth(c0);
                    if (!flag) break;
                    ++j;
                    break;
                }
                case '\u00a7': {
                    char c1;
                    if (k >= i - 1) break;
                    if ((c1 = str.charAt(++k)) == 'l' || c1 == 'L') {
                        flag = true;
                        break;
                    }
                    if (c1 != 'r' && c1 != 'R' && !FontRenderer.isFormatColor(c1)) break;
                    flag = false;
                }
            }
            if (c0 == '\n') {
                l = ++k;
                break;
            }
            if (j > wrapWidth) break;
        }
        return k != i && l != -1 && l < k ? l : k;
    }

    private static boolean isFormatColor(char colorChar) {
        return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
    }

    private static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
    }

    public static String getFormatFromString(String text) {
        String s = "";
        int i = -1;
        int j = text.length();
        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i >= j - 1) continue;
            char c0 = text.charAt(i + 1);
            if (FontRenderer.isFormatColor(c0)) {
                s = "\u00a7" + c0;
                continue;
            }
            if (!FontRenderer.isFormatSpecial(c0)) continue;
            s = s + "\u00a7" + c0;
        }
        return s;
    }

    public boolean getBidiFlag() {
        return this.bidiFlag;
    }

    protected void setColor(float r, float g, float b, float a) {
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
    }

    protected void enableAlpha() {
        GlStateManager.enableAlpha();
    }

    protected void bindTexture(ResourceLocation location) {
        this.renderEngine.bindTexture(location);
    }

    protected IResource getResource(ResourceLocation location) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(location);
    }

    public int getColorCode(char character) {
        int i = "0123456789abcdef".indexOf((int)character);
        return i >= 0 && i < this.colorCode.length ? this.colorCode[i] : -1;
    }
}
