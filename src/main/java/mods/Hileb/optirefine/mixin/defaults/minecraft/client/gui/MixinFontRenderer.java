package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Implements;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.SelectiveReloadStateHandler;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.optifine.CustomColors;
import net.optifine.render.GlBlendState;
import net.optifine.util.FontUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Predicate;


@SuppressWarnings("deprecation")
@Checked // TODO
@Implements(
        value = ISelectiveResourceReloadListener.class,
        removes = net.minecraft.client.resources.IResourceManagerReloadListener.class
)
@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer implements ISelectiveResourceReloadListener {

    @AccessTransformer(name = "field_111273_g", deobf = true)
    private ResourceLocation optirefine$acc$locationFontTexture;

    @Shadow
    @Final
    @Mutable
    protected ResourceLocation locationFontTexture;

    @Shadow
    @Final
    protected int[] charWidth;

    @Shadow
    @Final
    protected byte[] glyphWidth;

    @Shadow
    @Final
    private int[] colorCode;

    @Shadow
    protected float posX;

    @Shadow
    protected float posY;

    @Shadow
    private boolean unicodeFlag;

    @Shadow
    private boolean randomStyle;

    @Shadow
    private boolean boldStyle;

    @Shadow
    private boolean italicStyle;

    @Shadow
    private boolean underlineStyle;

    @Shadow
    private boolean strikethroughStyle;

    @Shadow
    private int textColor;

    @Shadow
    private float red;

    @Shadow
    private float blue;

    @Shadow
    private float green;

    @Shadow
    private float alpha;

    @Shadow
    @Final
    private TextureManager renderEngine;

    @Shadow
    @Final
    private static ResourceLocation[] UNICODE_PAGE_LOCATIONS;

    @Shadow
    protected abstract void resetStyles();

    @Shadow
    protected abstract float renderUnicodeChar(char ch, boolean italic);

    @Shadow
    protected abstract void setColor(float r, float g, float b, float a);

    @Shadow
    protected abstract void enableAlpha();

    @Shadow
    protected abstract void bindTexture(ResourceLocation location);

    @Shadow
    protected abstract IResource getResource(ResourceLocation location) throws IOException;

    @Unique
    @Public
    public GameSettings gameSettings;

    @Unique
    public ResourceLocation locationFontTextureBase;

    @Unique
    public float offsetBold = 1.0F;

    @Unique
    private final float[] charWidthFloat = new float[256];

    @Unique
    private boolean blend = false;

    @Unique
    private final GlBlendState oldBlendState = new GlBlendState();

    // ====== Charset mapping used by vanilla FontRenderer ======
    @Unique
    private static final String OPTIREFINE_CHARSET =
            "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000" +
                    "ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";


    @Inject(method = "<init>", at = @At(value = "CTOR_HEAD", unsafe = true))
    private void optiRefine$ctorHead(GameSettings gs, ResourceLocation base, TextureManager tm, boolean unicode, CallbackInfo ci) {
        this.gameSettings = gs;
        this.locationFontTextureBase = base;
        Arrays.fill(this.charWidthFloat, -1.0F);
    }

    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")
    )
    private void optiRefine$ctorBindHd(FontRenderer self, ResourceLocation ignored, Operation<Void> original) {
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        original.call(self, this.locationFontTexture);
    }


    @WrapMethod(method = "onResourceManagerReload(Lnet/minecraft/client/resources/IResourceManager;)V")
    private void optiRefine$onReloadBridge(IResourceManager rm, Operation<Void> original) {
        this.onResourceManagerReload(rm, SelectiveReloadStateHandler.INSTANCE.get());
    }

    @Shadow
    private void readGlyphSizes() {
    }

    @Shadow
    private void readFontTexture() {
    }

    @Override
    public void onResourceManagerReload(IResourceManager rm, Predicate<IResourceType> predicate) {
        this.readGlyphSizes();
        if (predicate.test(VanillaResourceType.TEXTURES)) {
            this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
            Arrays.fill(UNICODE_PAGE_LOCATIONS, null);
            this.readFontTexture();
        }
    }

    @Inject(method = "readFontTexture", at = @At("HEAD"))
    private void optiRefine$readFontTextureHead(
            CallbackInfo ci,
            @Share(namespace = "optirefine", value = "props") LocalRef<Properties> propsRef
    ) {
        Properties props = FontUtils.readFontProperties(this.locationFontTexture);
        this.blend = FontUtils.readBoolean(props, "blend", false);
        propsRef.set(props);

        Arrays.fill(this.charWidth, -1);
        Arrays.fill(this.charWidthFloat, -1.0F);
    }

    @Inject(method = "readFontTexture", at = @At("TAIL"))
    private void optiRefine$readFontTextureTail(
            CallbackInfo ci,
            @Share(namespace = "optirefine", value = "props") LocalRef<Properties> propsRef
    ) {
        for (int i = 0; i < this.charWidthFloat.length; i++) {
            if (this.charWidthFloat[i] < 0.0F) {
                this.charWidthFloat[i] = this.charWidth[i] < 0 ? 0.0F : this.charWidth[i];
            }
        }
    }

    @WrapOperation(
            method = "readFontTexture",
            at = @At(value = "INVOKE", target = "Ljava/awt/image/BufferedImage;getRGB(IIII[III)[I")
    )
    private int[] optiRefine$readFontTextureComputeWidths(
            BufferedImage img,
            int x, int y, int w, int h,
            int[] pixels, int offset, int scansize,
            Operation<int[]> original,
            @Share(namespace = "optirefine", value = "props") LocalRef<Properties> propsRef
    ) {
        int imgW = w;
        int imgH = h;

        int charW = imgW / 16;
        int charH = imgH / 16;

        float kx = imgW / 128.0F;
        float boldScale = Config.limit(kx, 1.0F, 2.0F);
        this.offsetBold = 1.0F / boldScale;

        float cfgOffsetBold = FontUtils.readFloat(propsRef.get(), "offsetBold", -1.0F);
        if (cfgOffsetBold >= 0.0F) this.offsetBold = cfgOffsetBold;

        // read pixels
        pixels = original.call(img, 0, 0, imgW, imgH, pixels, 0, imgW);

        for (int k = 0; k < 256; k++) {
            int cx = k % 16;
            int cy = k / 16;

            int px;
            for (px = charW - 1; px >= 0; px--) {
                int xx = cx * charW + px;
                boolean emptyCol = true;

                for (int py = 0; py < charH; py++) {
                    int row = (cy * charH + py) * imgW;
                    int col = pixels[xx + row];
                    int a = (col >>> 24) & 0xFF;
                    if (a > 16) { // OF threshold
                        emptyCol = false;
                        break;
                    }
                }
                if (!emptyCol) break;
            }

            if (k == 32) {
                px = (charW <= 8) ? (int) (2.0F * kx) : (int) (1.5F * kx);
            }

            this.charWidthFloat[k] = (px + 1) / kx + 1.0F;
        }

        FontUtils.readCustomCharWidths(propsRef.get(), this.charWidthFloat);

        for (int i = 0; i < this.charWidth.length; i++) {
            this.charWidth[i] = Math.round(this.charWidthFloat[i]);
        }

        return pixels;
    }

    @Definition(id = "charWidth", field = "Lnet/minecraft/client/gui/FontRenderer;charWidth:[I")
    @Expression("this.charWidth[?] = ?")
    @WrapOperation(method = "readFontTexture", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void optiRefine$guardCharWidthWrite(int[] array, int index, int value, Operation<Void> original) {
        if (this.charWidthFloat[index] < 0.0F) {
            original.call(array, index, value);
        }
    }



    @WrapOperation(
            method = "getUnicodePageLocation",
            at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;")
    )
    private ResourceLocation optiRefine$unicodePageHd(String path, Operation<ResourceLocation> original) {
        ResourceLocation rl = original.call(path);
        return FontUtils.getHdFontLocation(rl);
    }


    @Definition(id = "colorCode", field = "Lnet/minecraft/client/gui/FontRenderer;colorCode:[I")
    @Expression("this.colorCode[?]")
    @WrapOperation(method = "renderStringAtPos", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int optiRefine$colorCodeHook(int[] array, int idx, Operation<Integer> original) {
        int v = original.call(array, idx);
        if (Config.isCustomColors()) {
            v = CustomColors.getTextColor(idx, v);
        }
        return v;
    }

    @ModifyConstant(method = "renderChar", constant = @Constant(floatValue = 4.0F))
    private float optiRefine$renderCharSpaceWidth(float constant, char ch) {
        return (!this.unicodeFlag) ? this.charWidthFloat[ch] : 4.0F;
    }

    @Expression("? = @((float)? - 0.01)")
    @ModifyExpressionValue(method = "renderDefaultChar", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float optiRefine$renderDefaultCharUseFloat(float original, @Local(argsOnly = true) int ch) {
        return this.charWidthFloat[ch] - 0.01F;
    }

    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager setBlendState (Lnet.optifine.render.GlBlendState;)V")
    private static native void optiRefine$setBlendState(GlBlendState glBlendState);

    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager getBlendState (Lnet.optifine.render.GlBlendState;)V")
    private static native void optiRefine$getBlendState(GlBlendState glBlendState);

    @WrapMethod(method = "drawString(Ljava/lang/String;FFIZ)I")
    private int optiRefine$drawStringBlend(String text, float x, float y, int color, boolean dropShadow, Operation<Integer> original) {
        if (!this.blend) return original.call(text, x, y, color, dropShadow);

        optiRefine$getBlendState(this.oldBlendState);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        int ret = original.call(text, x, y, color, dropShadow);

        optiRefine$setBlendState(this.oldBlendState);
        return ret;
    }

    @WrapMethod(method = "drawSplitString")
    private void optiRefine$drawSplitStringBlend(String str, int x, int y, int wrapWidth, int textColor, Operation<Void> original) {
        if (this.blend) {
            optiRefine$getBlendState(this.oldBlendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }

        original.call(str, x, y, wrapWidth, textColor);

        if (this.blend) {
            optiRefine$setBlendState(this.oldBlendState);
        }
    }

    @WrapOperation(
            method = "wrapFormattedStringToWidth",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;sizeStringToWidth(Ljava/lang/String;I)I")
    )
    private int optiRefine$wrapWidthFast(FontRenderer self, String s, int width, Operation<Integer> original) {
        if (s.length() <= 1) return 1;
        return original.call(self, s, width);
    }

    @Unique
    private float optiRefine$getCharWidthFloat(char ch) {
        if (ch == 167) return -1.0F; // '§'
        if (ch == ' ' || ch == 160) return this.charWidthFloat[32];

        int idx = OPTIREFINE_CHARSET.indexOf(ch);
        if (ch > 0 && idx != -1 && !this.unicodeFlag) {
            return this.charWidthFloat[idx];
        }

        int gw = this.glyphWidth[ch] & 0xFF;
        if (gw != 0) {
            int left = gw >>> 4;
            int right = gw & 15;
            right++;
            return (right - left) / 2.0F + 1.0F;
        }

        return 0.0F;
    }

    @WrapMethod(method = "getCharWidth")
    private int optiRefine$getCharWidth(char ch, Operation<Integer> original) {
        return Math.round(this.optiRefine$getCharWidthFloat(ch));
    }

    @WrapMethod(method = "getStringWidth")
    private int optiRefine$getStringWidth(String text, Operation<Integer> original) {
        if (text == null) return 0;

        float w = 0.0F;
        boolean bold = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            float cw = this.optiRefine$getCharWidthFloat(c);

            if (cw < 0.0F && i < text.length() - 1) {
                c = text.charAt(++i);
                if (c == 'l' || c == 'L') bold = true;
                else if (c == 'r' || c == 'R') bold = false;
                cw = 0.0F;
            }

            w += cw;

            if (bold && cw > 0.0F) {
                w += this.unicodeFlag ? 1.0F : this.offsetBold;
            }
        }

        return Math.round(w);
    }

    @WrapMethod(method = "trimStringToWidth(Ljava/lang/String;IZ)Ljava/lang/String;")
    private String optiRefine$trimStringToWidth(String text, int width, boolean reverse, Operation<String> original) {
        if (text == null) return "";

        StringBuilder out = new StringBuilder();
        float w = 0.0F;

        int i = reverse ? text.length() - 1 : 0;
        int step = reverse ? -1 : 1;

        boolean expectingFormat = false;
        boolean bold = false;

        for (; i >= 0 && i < text.length() && w < width; i += step) {
            char c = text.charAt(i);
            float cw = this.optiRefine$getCharWidthFloat(c);

            if (expectingFormat) {
                expectingFormat = false;
                if (c == 'l' || c == 'L') bold = true;
                else if (c == 'r' || c == 'R') bold = false;
            } else if (cw < 0.0F) {
                expectingFormat = true;
            } else {
                w += cw;
                if (bold && cw > 0.0F) {
                    w += this.unicodeFlag ? 1.0F : this.offsetBold;
                }
            }

            if (w > width) break;

            if (reverse) out.insert(0, c);
            else out.append(c);
        }

        return out.toString();
    }

    @WrapMethod(method = "sizeStringToWidth")
    private int optiRefine$sizeStringToWidth(String str, int wrapWidth, Operation<Integer> original) {
        int len = str.length();
        float w = 0.0F;
        int i = 0;
        int lastSpace = -1;
        boolean bold = false;

        for (; i < len; ++i) {
            char c = str.charAt(i);

            switch (c) {
                case '\n':
                    --i;
                    break;
                case ' ':
                    lastSpace = i;
                case '§':
                    if (i < len - 1) {
                        ++i;
                        char f = str.charAt(i);
                        if (f == 'l' || f == 'L') {
                            bold = true;
                        } else if (f == 'r' || f == 'R'
                                || (f >= '0' && f <= '9')
                                || (f >= 'a' && f <= 'f')
                                || (f >= 'A' && f <= 'F')) {
                            bold = false;
                        }
                    }
                    break;
                default:
                    w += this.optiRefine$getCharWidthFloat(c);
                    if (bold) {
                        w += this.unicodeFlag ? 1.0F : this.offsetBold;
                    }
                    break;
            }

            if (c == '\n') {
                lastSpace = ++i;
                break;
            }

            if (Math.round(w) > wrapWidth) {
                break;
            }
        }

        return i != len && lastSpace != -1 && lastSpace < i ? lastSpace : i;
    }

    @WrapMethod(method = "renderChar")
    private float optiRefine$renderChar(char ch, boolean italic, Operation<Float> original) {
        if (ch == ' ' || ch == 160) {
            return !this.unicodeFlag ? this.charWidthFloat[32] : 4.0F;
        }
        return original.call(ch, italic);
    }

    @WrapMethod(method = "doDraw")
    private void optiRefine$doDrawFloat(float f, Operation<Void> original) {
        if (this.strikethroughStyle) {
            net.minecraft.client.renderer.Tessellator tess = net.minecraft.client.renderer.Tessellator.getInstance();
            net.minecraft.client.renderer.BufferBuilder buf = tess.getBuffer();
            GlStateManager.disableTexture2D();
            buf.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
            buf.pos(this.posX, this.posY + (float) (9 / 2), 0.0).endVertex();
            buf.pos(this.posX + f, this.posY + (float) (9 / 2), 0.0).endVertex();
            buf.pos(this.posX + f, this.posY + (float) (9 / 2) - 1.0F, 0.0).endVertex();
            buf.pos(this.posX, this.posY + (float) (9 / 2) - 1.0F, 0.0).endVertex();
            tess.draw();
            GlStateManager.enableTexture2D();
        }

        if (this.underlineStyle) {
            net.minecraft.client.renderer.Tessellator tess = net.minecraft.client.renderer.Tessellator.getInstance();
            net.minecraft.client.renderer.BufferBuilder buf = tess.getBuffer();
            GlStateManager.disableTexture2D();
            buf.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
            int l = this.underlineStyle ? -1 : 0;
            buf.pos(this.posX + (float) l, this.posY + 9.0F, 0.0).endVertex();
            buf.pos(this.posX + f, this.posY + 9.0F, 0.0).endVertex();
            buf.pos(this.posX + f, this.posY + 9.0F - 1.0F, 0.0).endVertex();
            buf.pos(this.posX + (float) l, this.posY + 9.0F - 1.0F, 0.0).endVertex();
            tess.draw();
            GlStateManager.enableTexture2D();
        }

        this.posX += f;
    }


    @WrapMethod(method = "getColorCode")
    private int optiRefine$getColorCode(char character, Operation<Integer> original) {
        int idx = "0123456789abcdef".indexOf(character);
        if (idx >= 0 && idx < this.colorCode.length) {
            int v = this.colorCode[idx];
            if (Config.isCustomColors()) {
                v = CustomColors.getTextColor(idx, v);
            }
            return v;
        }
        return 16777215;
    }
}

/*
+++ net/minecraft/client/gui/FontRenderer.java	Tue Aug 19 14:59:58 2025
@@ -5,12 +5,13 @@
 import com.ibm.icu.text.Bidi;
 import java.awt.image.BufferedImage;
 import java.io.IOException;
 import java.util.Arrays;
 import java.util.List;
 import java.util.Locale;
+import java.util.Properties;
 import java.util.Random;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.texture.TextureManager;
@@ -18,22 +19,25 @@
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.client.resources.IResource;
 import net.minecraft.client.resources.IResourceManager;
 import net.minecraft.client.resources.IResourceManagerReloadListener;
 import net.minecraft.client.settings.GameSettings;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.CustomColors;
+import net.optifine.render.GlBlendState;
+import net.optifine.util.FontUtils;
 import org.apache.commons.io.IOUtils;

 public class FontRenderer implements IResourceManagerReloadListener {
    private static final ResourceLocation[] UNICODE_PAGE_LOCATIONS = new ResourceLocation[256];
    private final int[] charWidth = new int[256];
    public int FONT_HEIGHT = 9;
    public Random fontRandom = new Random();
    private final byte[] glyphWidth = new byte[65536];
    private final int[] colorCode = new int[32];
-   private final ResourceLocation locationFontTexture;
+   private ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    private float posX;
    private float posY;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
@@ -43,18 +47,27 @@
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
+   public GameSettings gameSettings;
+   public ResourceLocation locationFontTextureBase;
+   public float offsetBold = 1.0F;
+   private float[] charWidthFloat = new float[256];
+   private boolean blend = false;
+   private GlBlendState oldBlendState = new GlBlendState();

    public FontRenderer(GameSettings var1, ResourceLocation var2, TextureManager var3, boolean var4) {
+      this.gameSettings = var1;
+      this.locationFontTextureBase = var2;
       this.locationFontTexture = var2;
       this.renderEngine = var3;
       this.unicodeFlag = var4;
-      var3.bindTexture(this.locationFontTexture);
+      this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
+      this.bindTexture(this.locationFontTexture);

       for (int var5 = 0; var5 < 32; var5++) {
          int var6 = (var5 >> 3 & 1) * 85;
          int var7 = (var5 >> 2 & 1) * 170 + var6;
          int var8 = (var5 >> 1 & 1) * 170 + var6;
          int var9 = (var5 >> 0 & 1) * 170 + var6;
@@ -81,96 +94,127 @@
       }

       this.readGlyphSizes();
    }

    public void onResourceManagerReload(IResourceManager var1) {
+      this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
+
+      for (int var2 = 0; var2 < UNICODE_PAGE_LOCATIONS.length; var2++) {
+         UNICODE_PAGE_LOCATIONS[var2] = null;
+      }
+
       this.readFontTexture();
       this.readGlyphSizes();
    }

    private void readFontTexture() {
-      IResource var2 = null;
+      IResource var1 = null;

-      BufferedImage var1;
+      BufferedImage var2;
       try {
-         var2 = Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture);
-         var1 = TextureUtil.readBufferedImage(var2.getInputStream());
-      } catch (IOException var20) {
-         throw new RuntimeException(var20);
+         var1 = this.getResource(this.locationFontTexture);
+         var2 = TextureUtil.readBufferedImage(var1.getInputStream());
+      } catch (IOException var24) {
+         throw new RuntimeException(var24);
       } finally {
-         IOUtils.closeQuietly(var2);
+         IOUtils.closeQuietly(var1);
       }

-      int var3 = var1.getWidth();
-      int var4 = var1.getHeight();
-      int[] var5 = new int[var3 * var4];
-      var1.getRGB(0, 0, var3, var4, var5, 0, var3);
+      Properties var3 = FontUtils.readFontProperties(this.locationFontTexture);
+      this.blend = FontUtils.readBoolean(var3, "blend", false);
+      int var4 = var2.getWidth();
+      int var5 = var2.getHeight();
       int var6 = var4 / 16;
-      int var7 = var3 / 16;
-      boolean var8 = true;
-      float var9 = 8.0F / var7;
-
-      for (int var10 = 0; var10 < 256; var10++) {
-         int var11 = var10 % 16;
-         int var12 = var10 / 16;
-         if (var10 == 32) {
-            this.charWidth[var10] = 4;
-         }
-
-         int var13;
-         for (var13 = var7 - 1; var13 >= 0; var13--) {
-            int var14 = var11 * var7 + var13;
-            boolean var15 = true;
-
-            for (int var16 = 0; var16 < var6 && var15; var16++) {
-               int var17 = (var12 * var7 + var16) * var3;
-               if ((var5[var14 + var17] >> 24 & 0xFF) != 0) {
-                  var15 = false;
+      int var7 = var5 / 16;
+      float var8 = var4 / 128.0F;
+      float var9 = Config.limit(var8, 1.0F, 2.0F);
+      this.offsetBold = 1.0F / var9;
+      float var10 = FontUtils.readFloat(var3, "offsetBold", -1.0F);
+      if (var10 >= 0.0F) {
+         this.offsetBold = var10;
+      }
+
+      int[] var11 = new int[var4 * var5];
+      var2.getRGB(0, 0, var4, var5, var11, 0, var4);
+
+      for (int var12 = 0; var12 < 256; var12++) {
+         int var13 = var12 % 16;
+         int var14 = var12 / 16;
+         int var15 = 0;
+
+         for (var15 = var6 - 1; var15 >= 0; var15--) {
+            int var16 = var13 * var6 + var15;
+            boolean var17 = true;
+
+            for (int var18 = 0; var18 < var7 && var17; var18++) {
+               int var19 = (var14 * var7 + var18) * var4;
+               int var20 = var11[var16 + var19];
+               int var21 = var20 >> 24 & 0xFF;
+               if (var21 > 16) {
+                  var17 = false;
                }
             }

-            if (!var15) {
+            if (!var17) {
                break;
             }
          }

-         this.charWidth[var10] = (int)(0.5 + ++var13 * var9) + 1;
+         if (var12 == 65) {
+            var12 = var12;
+         }
+
+         if (var12 == 32) {
+            if (var6 <= 8) {
+               var15 = (int)(2.0F * var8);
+            } else {
+               var15 = (int)(1.5F * var8);
+            }
+         }
+
+         this.charWidthFloat[var12] = (var15 + 1) / var8 + 1.0F;
+      }
+
+      FontUtils.readCustomCharWidths(var3, this.charWidthFloat);
+
+      for (int var26 = 0; var26 < this.charWidth.length; var26++) {
+         this.charWidth[var26] = Math.round(this.charWidthFloat[var26]);
       }
    }

    private void readGlyphSizes() {
       IResource var1 = null;

       try {
-         var1 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin"));
+         var1 = this.getResource(new ResourceLocation("font/glyph_sizes.bin"));
          var1.getInputStream().read(this.glyphWidth);
       } catch (IOException var6) {
          throw new RuntimeException(var6);
       } finally {
          IOUtils.closeQuietly(var1);
       }
    }

    private float renderChar(char var1, boolean var2) {
-      if (var1 == ' ') {
-         return 4.0F;
-      } else {
+      if (var1 != ' ' && var1 != 160) {
          int var3 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
             .indexOf(var1);
          return var3 != -1 && !this.unicodeFlag ? this.renderDefaultChar(var3, var2) : this.renderUnicodeChar(var1, var2);
+      } else {
+         return !this.unicodeFlag ? this.charWidthFloat[var1] : 4.0F;
       }
    }

    private float renderDefaultChar(int var1, boolean var2) {
       int var3 = var1 % 16 * 8;
       int var4 = var1 / 16 * 8;
       int var5 = var2 ? 1 : 0;
-      this.renderEngine.bindTexture(this.locationFontTexture);
-      int var6 = this.charWidth[var1];
-      float var7 = var6 - 0.01F;
+      this.bindTexture(this.locationFontTexture);
+      float var6 = this.charWidthFloat[var1];
+      float var7 = 7.99F;
       GlStateManager.glBegin(5);
       GlStateManager.glTexCoord2f(var3 / 128.0F, var4 / 128.0F);
       GlStateManager.glVertex3f(this.posX + var5, this.posY, 0.0F);
       GlStateManager.glTexCoord2f(var3 / 128.0F, (var4 + 7.99F) / 128.0F);
       GlStateManager.glVertex3f(this.posX - var5, this.posY + 7.99F, 0.0F);
       GlStateManager.glTexCoord2f((var3 + var7 - 1.0F) / 128.0F, var4 / 128.0F);
@@ -181,19 +225,20 @@
       return var6;
    }

    private ResourceLocation getUnicodePageLocation(int var1) {
       if (UNICODE_PAGE_LOCATIONS[var1] == null) {
          UNICODE_PAGE_LOCATIONS[var1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", var1));
+         UNICODE_PAGE_LOCATIONS[var1] = FontUtils.getHdFontLocation(UNICODE_PAGE_LOCATIONS[var1]);
       }

       return UNICODE_PAGE_LOCATIONS[var1];
    }

    private void loadGlyphTexture(int var1) {
-      this.renderEngine.bindTexture(this.getUnicodePageLocation(var1));
+      this.bindTexture(this.getUnicodePageLocation(var1));
    }

    private float renderUnicodeChar(char var1, boolean var2) {
       int var3 = this.glyphWidth[var1] & 255;
       if (var3 == 0) {
          return 0.0F;
@@ -228,22 +273,32 @@

    public int drawString(String var1, int var2, int var3, int var4) {
       return this.drawString(var1, var2, var3, var4, false);
    }

    public int drawString(String var1, float var2, float var3, int var4, boolean var5) {
-      GlStateManager.enableAlpha();
+      this.enableAlpha();
+      if (this.blend) {
+         GlStateManager.getBlendState(this.oldBlendState);
+         GlStateManager.enableBlend();
+         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
+      }
+
       this.resetStyles();
       int var7;
       if (var5) {
          var7 = this.renderString(var1, var2 + 1.0F, var3 + 1.0F, var4, true);
          var7 = Math.max(var7, this.renderString(var1, var2, var3, var4, false));
       } else {
          var7 = this.renderString(var1, var2, var3, var4, false);
       }

+      if (this.blend) {
+         GlStateManager.setBlendState(this.oldBlendState);
+      }
+
       return var7;
    }

    private String bidiReorder(String var1) {
       try {
          Bidi var2 = new Bidi(new ArabicShaping(8).shape(var1), 127);
@@ -263,47 +318,51 @@
    }

    private void renderStringAtPos(String var1, boolean var2) {
       for (int var3 = 0; var3 < var1.length(); var3++) {
          char var4 = var1.charAt(var3);
          if (var4 == 167 && var3 + 1 < var1.length()) {
-            int var12 = "0123456789abcdefklmnor".indexOf(String.valueOf(var1.charAt(var3 + 1)).toLowerCase(Locale.ROOT).charAt(0));
-            if (var12 < 16) {
+            int var9 = "0123456789abcdefklmnor".indexOf(String.valueOf(var1.charAt(var3 + 1)).toLowerCase(Locale.ROOT).charAt(0));
+            if (var9 < 16) {
                this.randomStyle = false;
                this.boldStyle = false;
                this.strikethroughStyle = false;
                this.underlineStyle = false;
                this.italicStyle = false;
-               if (var12 < 0 || var12 > 15) {
-                  var12 = 15;
+               if (var9 < 0 || var9 > 15) {
+                  var9 = 15;
                }

                if (var2) {
-                  var12 += 16;
+                  var9 += 16;
                }

-               int var14 = this.colorCode[var12];
-               this.textColor = var14;
-               GlStateManager.color((var14 >> 16) / 255.0F, (var14 >> 8 & 0xFF) / 255.0F, (var14 & 0xFF) / 255.0F, this.alpha);
-            } else if (var12 == 16) {
+               int var11 = this.colorCode[var9];
+               if (Config.isCustomColors()) {
+                  var11 = CustomColors.getTextColor(var9, var11);
+               }
+
+               this.textColor = var11;
+               this.setColor((var11 >> 16) / 255.0F, (var11 >> 8 & 0xFF) / 255.0F, (var11 & 0xFF) / 255.0F, this.alpha);
+            } else if (var9 == 16) {
                this.randomStyle = true;
-            } else if (var12 == 17) {
+            } else if (var9 == 17) {
                this.boldStyle = true;
-            } else if (var12 == 18) {
+            } else if (var9 == 18) {
                this.strikethroughStyle = true;
-            } else if (var12 == 19) {
+            } else if (var9 == 19) {
                this.underlineStyle = true;
-            } else if (var12 == 20) {
+            } else if (var9 == 20) {
                this.italicStyle = true;
-            } else if (var12 == 21) {
+            } else if (var9 == 21) {
                this.randomStyle = false;
                this.boldStyle = false;
                this.strikethroughStyle = false;
                this.underlineStyle = false;
                this.italicStyle = false;
-               GlStateManager.color(this.red, this.blue, this.green, this.alpha);
+               this.setColor(this.red, this.blue, this.green, this.alpha);
             }

             var3++;
          } else {
             int var5 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
                .indexOf(var4);
@@ -321,74 +380,78 @@
                      .charAt(var5);
                } while (var6 != this.getCharWidth(var7));

                var4 = var7;
             }

-            float var13 = this.unicodeFlag ? 0.5F : 1.0F;
-            boolean var15 = (var4 == 0 || var5 == -1 || this.unicodeFlag) && var2;
-            if (var15) {
-               this.posX -= var13;
-               this.posY -= var13;
+            float var10 = var5 != -1 && !this.unicodeFlag ? this.offsetBold : 0.5F;
+            boolean var12 = (var4 == 0 || var5 == -1 || this.unicodeFlag) && var2;
+            if (var12) {
+               this.posX -= var10;
+               this.posY -= var10;
             }

             float var8 = this.renderChar(var4, this.italicStyle);
-            if (var15) {
-               this.posX += var13;
-               this.posY += var13;
+            if (var12) {
+               this.posX += var10;
+               this.posY += var10;
             }

             if (this.boldStyle) {
-               this.posX += var13;
-               if (var15) {
-                  this.posX -= var13;
-                  this.posY -= var13;
+               this.posX += var10;
+               if (var12) {
+                  this.posX -= var10;
+                  this.posY -= var10;
                }

                this.renderChar(var4, this.italicStyle);
-               this.posX -= var13;
-               if (var15) {
-                  this.posX += var13;
-                  this.posY += var13;
+               this.posX -= var10;
+               if (var12) {
+                  this.posX += var10;
+                  this.posY += var10;
                }

-               var8++;
-            }
-
-            if (this.strikethroughStyle) {
-               Tessellator var9 = Tessellator.getInstance();
-               BufferBuilder var10 = var9.getBuffer();
-               GlStateManager.disableTexture2D();
-               var10.begin(7, DefaultVertexFormats.POSITION);
-               var10.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
-               var10.pos(this.posX + var8, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
-               var10.pos(this.posX + var8, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0).endVertex();
-               var10.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0).endVertex();
-               var9.draw();
-               GlStateManager.enableTexture2D();
-            }
-
-            if (this.underlineStyle) {
-               Tessellator var16 = Tessellator.getInstance();
-               BufferBuilder var17 = var16.getBuffer();
-               GlStateManager.disableTexture2D();
-               var17.begin(7, DefaultVertexFormats.POSITION);
-               int var11 = this.underlineStyle ? -1 : 0;
-               var17.pos(this.posX + var11, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
-               var17.pos(this.posX + var8, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
-               var17.pos(this.posX + var8, this.posY + this.FONT_HEIGHT - 1.0F, 0.0).endVertex();
-               var17.pos(this.posX + var11, this.posY + this.FONT_HEIGHT - 1.0F, 0.0).endVertex();
-               var16.draw();
-               GlStateManager.enableTexture2D();
+               var8 += var10;
             }

-            this.posX += (int)var8;
+            this.doDraw(var8);
          }
       }
    }

+   protected void doDraw(float var1) {
+      if (this.strikethroughStyle) {
+         Tessellator var2 = Tessellator.getInstance();
+         BufferBuilder var3 = var2.getBuffer();
+         GlStateManager.disableTexture2D();
+         var3.begin(7, DefaultVertexFormats.POSITION);
+         var3.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
+         var3.pos(this.posX + var1, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
+         var3.pos(this.posX + var1, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0).endVertex();
+         var3.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0).endVertex();
+         var2.draw();
+         GlStateManager.enableTexture2D();
+      }
+
+      if (this.underlineStyle) {
+         Tessellator var5 = Tessellator.getInstance();
+         BufferBuilder var6 = var5.getBuffer();
+         GlStateManager.disableTexture2D();
+         var6.begin(7, DefaultVertexFormats.POSITION);
+         int var4 = this.underlineStyle ? -1 : 0;
+         var6.pos(this.posX + var4, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
+         var6.pos(this.posX + var1, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
+         var6.pos(this.posX + var1, this.posY + this.FONT_HEIGHT - 1.0F, 0.0).endVertex();
+         var6.pos(this.posX + var4, this.posY + this.FONT_HEIGHT - 1.0F, 0.0).endVertex();
+         var5.draw();
+         GlStateManager.enableTexture2D();
+      }
+
+      this.posX += var1;
+   }
+
    private int renderStringAligned(String var1, int var2, int var3, int var4, int var5, boolean var6) {
       if (this.bidiFlag) {
          int var7 = this.getStringWidth(this.bidiReorder(var1));
          var2 = var2 + var4 - var7;
       }

@@ -412,96 +475,100 @@
          }

          this.red = (var4 >> 16 & 0xFF) / 255.0F;
          this.blue = (var4 >> 8 & 0xFF) / 255.0F;
          this.green = (var4 & 0xFF) / 255.0F;
          this.alpha = (var4 >> 24 & 0xFF) / 255.0F;
-         GlStateManager.color(this.red, this.blue, this.green, this.alpha);
+         this.setColor(this.red, this.blue, this.green, this.alpha);
          this.posX = var2;
          this.posY = var3;
          this.renderStringAtPos(var1, var5);
          return (int)this.posX;
       }
    }

    public int getStringWidth(String var1) {
       if (var1 == null) {
          return 0;
       } else {
-         int var2 = 0;
+         float var2 = 0.0F;
          boolean var3 = false;

          for (int var4 = 0; var4 < var1.length(); var4++) {
             char var5 = var1.charAt(var4);
-            int var6 = this.getCharWidth(var5);
-            if (var6 < 0 && var4 < var1.length() - 1) {
+            float var6 = this.getCharWidthFloat(var5);
+            if (var6 < 0.0F && var4 < var1.length() - 1) {
                var5 = var1.charAt(++var4);
                if (var5 == 'l' || var5 == 'L') {
                   var3 = true;
                } else if (var5 == 'r' || var5 == 'R') {
                   var3 = false;
                }

-               var6 = 0;
+               var6 = 0.0F;
             }

             var2 += var6;
-            if (var3 && var6 > 0) {
-               var2++;
+            if (var3 && var6 > 0.0F) {
+               var2 += this.unicodeFlag ? 1.0F : this.offsetBold;
             }
          }

-         return var2;
+         return Math.round(var2);
       }
    }

    public int getCharWidth(char var1) {
+      return Math.round(this.getCharWidthFloat(var1));
+   }
+
+   private float getCharWidthFloat(char var1) {
       if (var1 == 167) {
-         return -1;
-      } else if (var1 == ' ') {
-         return 4;
-      } else {
+         return -1.0F;
+      } else if (var1 != ' ' && var1 != 160) {
          int var2 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
             .indexOf(var1);
          if (var1 > 0 && var2 != -1 && !this.unicodeFlag) {
-            return this.charWidth[var2];
+            return this.charWidthFloat[var2];
          } else if (this.glyphWidth[var1] != 0) {
             int var3 = this.glyphWidth[var1] & 255;
             int var4 = var3 >>> 4;
             int var5 = var3 & 15;
             var5++;
             return (var5 - var4) / 2 + 1;
          } else {
-            return 0;
+            return 0.0F;
          }
+      } else {
+         return this.charWidthFloat[32];
       }
    }

    public String trimStringToWidth(String var1, int var2) {
       return this.trimStringToWidth(var1, var2, false);
    }

    public String trimStringToWidth(String var1, int var2, boolean var3) {
       StringBuilder var4 = new StringBuilder();
-      int var5 = 0;
+      float var5 = 0.0F;
       int var6 = var3 ? var1.length() - 1 : 0;
       int var7 = var3 ? -1 : 1;
       boolean var8 = false;
       boolean var9 = false;

       for (int var10 = var6; var10 >= 0 && var10 < var1.length() && var5 < var2; var10 += var7) {
          char var11 = var1.charAt(var10);
-         int var12 = this.getCharWidth(var11);
+         float var12 = this.getCharWidthFloat(var11);
          if (var8) {
             var8 = false;
             if (var11 == 'l' || var11 == 'L') {
                var9 = true;
             } else if (var11 == 'r' || var11 == 'R') {
                var9 = false;
             }
-         } else if (var12 < 0) {
+         } else if (var12 < 0.0F) {
             var8 = true;
          } else {
             var5 += var12;
             if (var9) {
                var5++;
             }
@@ -527,21 +594,30 @@
       }

       return var1;
    }

    public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
+      if (this.blend) {
+         GlStateManager.getBlendState(this.oldBlendState);
+         GlStateManager.enableBlend();
+         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
+      }
+
       this.resetStyles();
       this.textColor = var5;
       var1 = this.trimStringNewline(var1);
       this.renderSplitString(var1, var2, var3, var4, false);
+      if (this.blend) {
+         GlStateManager.setBlendState(this.oldBlendState);
+      }
    }

    private void renderSplitString(String var1, int var2, int var3, int var4, boolean var5) {
-      for (String var8 : this.listFormattedStringToWidth(var1, var4)) {
-         this.renderStringAligned(var8, var2, var3, var4, this.textColor, var5);
+      for (String var7 : this.listFormattedStringToWidth(var1, var4)) {
+         this.renderStringAligned(var7, var2, var3, var4, this.textColor, var5);
          var3 += this.FONT_HEIGHT;
       }
    }

    public int getWordWrappedHeight(String var1, int var2) {
       return this.FONT_HEIGHT * this.listFormattedStringToWidth(var1, var2).size();
@@ -561,40 +637,44 @@

    public List<String> listFormattedStringToWidth(String var1, int var2) {
       return Arrays.asList(this.wrapFormattedStringToWidth(var1, var2).split("\n"));
    }

    String wrapFormattedStringToWidth(String var1, int var2) {
-      int var3 = this.sizeStringToWidth(var1, var2);
-      if (var1.length() <= var3) {
+      if (var1.length() <= 1) {
          return var1;
       } else {
-         String var4 = var1.substring(0, var3);
-         char var5 = var1.charAt(var3);
-         boolean var6 = var5 == ' ' || var5 == '\n';
-         String var7 = getFormatFromString(var4) + var1.substring(var3 + (var6 ? 1 : 0));
-         return var4 + "\n" + this.wrapFormattedStringToWidth(var7, var2);
+         int var3 = this.sizeStringToWidth(var1, var2);
+         if (var1.length() <= var3) {
+            return var1;
+         } else {
+            String var4 = var1.substring(0, var3);
+            char var5 = var1.charAt(var3);
+            boolean var6 = var5 == ' ' || var5 == '\n';
+            String var7 = getFormatFromString(var4) + var1.substring(var3 + (var6 ? 1 : 0));
+            return var4 + "\n" + this.wrapFormattedStringToWidth(var7, var2);
+         }
       }
    }

    private int sizeStringToWidth(String var1, int var2) {
       int var3 = var1.length();
-      int var4 = 0;
+      float var4 = 0.0F;
       int var5 = 0;
       int var6 = -1;

       for (boolean var7 = false; var5 < var3; var5++) {
          char var8 = var1.charAt(var5);
          switch (var8) {
             case '\n':
                var5--;
                break;
             case ' ':
                var6 = var5;
             default:
-               var4 += this.getCharWidth(var8);
+               var4 += this.getCharWidthFloat(var8);
                if (var7) {
                   var4++;
                }
                break;
             case '§':
                if (var5 < var3 - 1) {
@@ -609,13 +689,13 @@

          if (var8 == '\n') {
             var6 = ++var5;
             break;
          }

-         if (var4 > var2) {
+         if (Math.round(var4) > var2) {
             break;
          }
       }

       return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }
@@ -650,9 +730,34 @@
    public boolean getBidiFlag() {
       return this.bidiFlag;
    }

    public int getColorCode(char var1) {
       int var2 = "0123456789abcdef".indexOf(var1);
-      return var2 >= 0 && var2 < this.colorCode.length ? this.colorCode[var2] : -1;
+      if (var2 >= 0 && var2 < this.colorCode.length) {
+         int var3 = this.colorCode[var2];
+         if (Config.isCustomColors()) {
+            var3 = CustomColors.getTextColor(var2, var3);
+         }
+
+         return var3;
+      } else {
+         return 16777215;
+      }
+   }
+
+   protected void setColor(float var1, float var2, float var3, float var4) {
+      GlStateManager.color(var1, var2, var3, var4);
+   }
+
+   protected void enableAlpha() {
+      GlStateManager.enableAlpha();
+   }
+
+   protected void bindTexture(ResourceLocation var1) {
+      this.renderEngine.bindTexture(var1);
+   }
+
+   protected IResource getResource(ResourceLocation var1) throws IOException {
+      return Minecraft.getMinecraft().getResourceManager().getResource(var1);
    }
 }
*/
