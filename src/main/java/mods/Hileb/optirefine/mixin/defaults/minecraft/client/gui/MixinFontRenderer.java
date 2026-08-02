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
@Implements(
        value = ISelectiveResourceReloadListener.class,
        removes = net.minecraft.client.resources.IResourceManagerReloadListener.class
)
@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer implements ISelectiveResourceReloadListener {


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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")
    )
    private void optiRefine$ctorBindHd(TextureManager self, ResourceLocation ignored, Operation<Void> original) {
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

    @Expression("(float)((int)?)")
    @ModifyExpressionValue(method = "renderStringAtPos", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float optiRefine$renderStringAtPosFloatStep(float truncated, @Local(ordinal = 1) float f) {
        return f;
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
