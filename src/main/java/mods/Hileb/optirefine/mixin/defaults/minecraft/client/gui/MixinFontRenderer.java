package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.OptiRefine;
import mods.Hileb.optirefine.core.OptiRefineLog;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Implements;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
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
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Predicate;

@Implements(
        value = ISelectiveResourceReloadListener.class,
        removes = IResourceManagerReloadListener.class
)
@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer implements ISelectiveResourceReloadListener{

    @AccessTransformer(name = "field_111273_g", deobf = true)
    private ResourceLocation optirefine$acc$locationFontTexture;

    @Shadow @Final @Mutable
    protected ResourceLocation locationFontTexture;

    @Shadow @Final protected int[] charWidth;

    @SuppressWarnings("unused")
    @Shadow private float red;

    @Unique @Public
    public GameSettings gameSettings;
    @Unique
    public ResourceLocation locationFontTextureBase;
    @Unique
    public float offsetBold = 1.0F;
    @Unique
    private float[] charWidthFloat = new float[256];
    @Unique
    private boolean blend = false;
    @Unique
    private GlBlendState oldBlendState = new GlBlendState();

    @Inject(method = "<init>", at = @At(value = "CTOR_HEAD", unsafe = true))
    private void extraForInit(GameSettings p_i1035_1, ResourceLocation p_i1035_2, TextureManager p_i1035_3, boolean p_i1035_4, CallbackInfo ci){
        this.gameSettings = p_i1035_1;
        this.locationFontTextureBase = p_i1035_2;
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public void extraForInitNextPart(FontRenderer instance, ResourceLocation location, Operation<Void> original){
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        original.call(instance, this.locationFontTexture);
    }

    @Shadow @Final
    private static ResourceLocation[] UNICODE_PAGE_LOCATIONS;

    @WrapMethod(method = "onResourceManagerReload(Lnet/minecraft/client/resources/IResourceManager;)V")
    private void $onResourceManagerReload(IResourceManager resourceManager, Operation<Void> original){
        this.onResourceManagerReload(resourceManager, SelectiveReloadStateHandler.INSTANCE.get());
    }

    @Shadow
    private void readGlyphSizes(){}

    @Shadow
    private void readFontTexture(){}

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager, Predicate<IResourceType> predicate) {
        this.readGlyphSizes();
        if (predicate.test(VanillaResourceType.TEXTURES)){
            this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
            Arrays.fill(UNICODE_PAGE_LOCATIONS, null);
            this.readFontTexture();
        }
    }

    @Inject(method = "readFontTexture", at = @At("HEAD"))
    private void init$readFontTexture(CallbackInfo ci,
                                      @Share(namespace = "optirefine", value = "properties")LocalRef<Properties> propertiesLocalRef
    ){
        Properties var3 = FontUtils.readFontProperties(this.locationFontTexture);
        this.blend = FontUtils.readBoolean(var3, "blend", false);
        propertiesLocalRef.set(var3);
        Arrays.fill(this.charWidthFloat, -1f);
    }

    @Inject(method = "readFontTexture", at = @At("TAIL"))
    private void postinit$readFontTexture(CallbackInfo ci,
                                      @Share(namespace = "optirefine", value = "properties")LocalRef<Properties> propertiesLocalRef
    ){
        for (int i = 0; i < this.charWidthFloat.length; i++) {
            if (charWidthFloat[i] < 0) {
                this.charWidthFloat[i] = this.charWidth[i];
            }
        }
    }

    @WrapOperation(method = "readFontTexture", at = @At(value = "INVOKE", target = "Ljava/awt/image/BufferedImage;getRGB(IIII[III)[I"))
    private int[] readFontTexture$customRGB(BufferedImage instance, int data0_, int data0_1, int imgWidth, int imgHeight, int[] array, int data0_2, int data3, Operation<int[]> original, @Share(namespace = "optirefine", value = "properties")LocalRef<Properties> propertiesLocalRef){
        Arrays.fill(this.charWidth, -1);
        int charW = imgWidth / 16;
        int charH = imgHeight / 16;
        float kx = imgWidth / 128.0F;
        float boldScaleFactor = Config.limit(kx, 1.0F, 2.0F);
        this.offsetBold = 1.0F / boldScaleFactor;
        float offsetBoldConfig = FontUtils.readFloat(propertiesLocalRef.get(), "offsetBold", -1.0F);
        if (offsetBoldConfig >= 0.0F) {
            this.offsetBold = offsetBoldConfig;
        }

        array = original.call(instance, 0, 0, imgWidth, imgHeight, array, 0, imgWidth);

        for (int k = 0; k < 256; k++) {
            int cx = k % 16;
            int cy = k / 16;
            int px = 0;

            for (px = charW - 1; px >= 0; px--) {
                int x = cx * charW + px;
                boolean flag = true;

                for (int py = 0; py < charH; py++) {
                    int ypos = (cy * charH + py) * imgWidth;
                    int col = array[x + ypos];
                    int al = col >> 24 & 0xFF;
                    if (al > 16) {
                        flag = false;
                        break;
                    }
                }

                if (!flag) {
                    break;
                }
            }

            if (k == 32) {
                if (charW <= 8) {
                    px = (int)(2.0F * kx);
                } else {
                    px = (int)(1.5F * kx);
                }
            }

            this.charWidthFloat[k] = (px + 1) / kx + 1.0F;
        }

        FontUtils.readCustomCharWidths(propertiesLocalRef.get(), this.charWidthFloat);
        for (int i = 0; i < this.charWidth.length; i++) {
            this.charWidth[i] = Math.round(this.charWidthFloat[i]);
            OptiRefineLog.log.info("A_CharFloat {} : {}", i, charWidthFloat[i]);
        }
        return array;
    }

    @Definition(id = "charWidth", field = "Lnet/minecraft/client/gui/FontRenderer;charWidth:[I")
    @Expression("this.charWidth[?] = ?")
    @WrapOperation(method = "readFontTexture", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void customWight$catchArrayValue(int[] array, int index, int value, Operation<Void> original){
        OptiRefineLog.log.info("A_CharInt {} : {}", index, value);
        if (array[index] < 0 || this.charWidthFloat[index] < 0) {
            original.call(array, index, value);
        }
    }

    @ModifyReturnValue(method = "getColorCode", at = @At("RETURN"))
    public int injectGetColorCode(int cir, @Local(argsOnly = true) char character){
        if (Config.isCustomColors()) {
            return CustomColors.getTextColor("0123456789abcdef".indexOf(character), cir);
        } else return cir;
    }

    @Shadow
    private boolean unicodeFlag;

    @ModifyConstant(method = "renderChar", constant = @Constant(floatValue = 4.0F))
    public float floatWeightChar$renderChar(float constant, @Local(argsOnly = true) char ch){
        return !this.unicodeFlag ? this.charWidthFloat[ch] : 4.0F;
    }

    @Expression("? = @((float)? - 0.01)")
    @ModifyExpressionValue(method = "renderDefaultChar", at = @At("MIXINEXTRAS:EXPRESSION"))
    public float renderDefaultChar$WithFloatChar(float original, @Local(argsOnly = true) char ch){
        return this.charWidthFloat[ch] - 0.01f;
    }

    @WrapOperation(method = "getUnicodePageLocation", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    public String getUnicodePageLocation$UseOptiHelper$disableStringFormat(String format, Object[] args, Operation<String> original){
        return null;
    }

    @Redirect(method = "getUnicodePageLocation", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation getUnicodePageLocation$UseOptiHelper(String p_i1293_1, @Local(argsOnly = true) int page){
        return FontUtils.getHdFontLocation(UNICODE_PAGE_LOCATIONS[page]);
    }

    @Shadow
    private float alpha;

    @Definition(id = "colorCode", field = "Lnet/minecraft/client/gui/FontRenderer;colorCode:[I")
    @Expression("this.colorCode[?]")
    @WrapOperation(method = "renderStringAtPos", at = @At("MIXINEXTRAS:EXPRESSION"))
    public int renderStringAtPos$color(int[] array, int index, Operation<Integer> original){
        int value = original.call(array, index);
        if (Config.isCustomColors()) {
            value = CustomColors.getTextColor(index, value);
        }
        return value;
    }

    @WrapMethod(method = "drawString(Ljava/lang/String;FFIZ)I")
    public int drawString$blend(String text, float x, float y, int color, boolean dropShadow, Operation<Integer> original) {
        if (this.blend) {
            GlStateManager_getBlendState(this.oldBlendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            int returnValue = original.call(text, x, y, color, dropShadow);
            GlStateManager_setBlendState(this.oldBlendState);
            return returnValue;
        } else return original.call(text, x, y, color, dropShadow);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager setBlendState (Lnet.optifine.render.GlBlendState;)V")
    private static native void GlStateManager_setBlendState(GlBlendState glBlendState);

    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager getBlendState (Lnet.optifine.render.GlBlendState;)V")
    private static native void GlStateManager_getBlendState(GlBlendState glBlendState);

    @WrapMethod(method = "drawSplitString")
    public void drawSplitString$blend(String str, int x, int y, int wrapWidth, int textColor, Operation<Void> original) {
        if (this.blend) {
            GlStateManager_getBlendState(this.oldBlendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }

        original.call(str, x, y, wrapWidth, textColor);

        if (this.blend) {
            GlStateManager_setBlendState(this.oldBlendState);
        }
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
