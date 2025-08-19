package mods.Hileb.optirefine.mixin.defaults.minecraft.client;

import net.minecraft.client.LoadingScreenRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * see {@link mods.Hileb.optirefine.mixin.defaults.minecraftforge.fml.MixinFMLClientHandler}
 */
@Mixin(LoadingScreenRenderer.class)
public abstract class MixinLoadingScreenRenderer {
    //NO-OPS
}
/*
--- net/minecraft/client/LoadingScreenRenderer.java	Tue Aug 19 14:59:40 2025
+++ net/minecraft/client/LoadingScreenRenderer.java	Tue Aug 19 14:59:58 2025
@@ -7,12 +7,15 @@
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.client.shader.Framebuffer;
 import net.minecraft.util.IProgressUpdate;
 import net.minecraft.util.MinecraftError;
+import net.optifine.CustomLoadingScreen;
+import net.optifine.CustomLoadingScreens;
+import net.optifine.reflect.Reflector;

 public class LoadingScreenRenderer implements IProgressUpdate {
    private String message = "";
    private final Minecraft mc;
    private String currentlyDisplayedText = "";
    private long systemTime = Minecraft.getSystemTime();
@@ -101,64 +104,83 @@
             GlStateManager.loadIdentity();
             GlStateManager.translate(0.0F, 0.0F, -200.0F);
             if (!OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.clear(16640);
             }

-            Tessellator var8 = Tessellator.getInstance();
-            BufferBuilder var9 = var8.getBuffer();
-            this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
-            float var10 = 32.0F;
-            var9.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-            var9.pos(0.0, var7, 0.0).tex(0.0, var7 / 32.0F).color(64, 64, 64, 255).endVertex();
-            var9.pos(var6, var7, 0.0).tex(var6 / 32.0F, var7 / 32.0F).color(64, 64, 64, 255).endVertex();
-            var9.pos(var6, 0.0, 0.0).tex(var6 / 32.0F, 0.0).color(64, 64, 64, 255).endVertex();
-            var9.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
-            var8.draw();
-            if (var1 >= 0) {
-               byte var11 = 100;
-               byte var12 = 2;
-               int var13 = var6 / 2 - 50;
-               int var14 = var7 / 2 + 16;
-               GlStateManager.disableTexture2D();
-               var9.begin(7, DefaultVertexFormats.POSITION_COLOR);
-               var9.pos(var13, var14, 0.0).color(128, 128, 128, 255).endVertex();
-               var9.pos(var13, var14 + 2, 0.0).color(128, 128, 128, 255).endVertex();
-               var9.pos(var13 + 100, var14 + 2, 0.0).color(128, 128, 128, 255).endVertex();
-               var9.pos(var13 + 100, var14, 0.0).color(128, 128, 128, 255).endVertex();
-               var9.pos(var13, var14, 0.0).color(128, 255, 128, 255).endVertex();
-               var9.pos(var13, var14 + 2, 0.0).color(128, 255, 128, 255).endVertex();
-               var9.pos(var13 + var1, var14 + 2, 0.0).color(128, 255, 128, 255).endVertex();
-               var9.pos(var13 + var1, var14, 0.0).color(128, 255, 128, 255).endVertex();
-               var8.draw();
-               GlStateManager.enableTexture2D();
+            boolean var8 = true;
+            if (Reflector.FMLClientHandler_handleLoadingScreen.exists()) {
+               Object var9 = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
+               if (var9 != null) {
+                  var8 = !Reflector.callBoolean(var9, Reflector.FMLClientHandler_handleLoadingScreen, new Object[]{var4});
+               }
             }

-            GlStateManager.enableBlend();
-            GlStateManager.tryBlendFuncSeparate(
-               GlStateManager.SourceFactor.SRC_ALPHA,
-               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
-               GlStateManager.SourceFactor.ONE,
-               GlStateManager.DestFactor.ZERO
-            );
-            this.mc
-               .fontRenderer
-               .drawStringWithShadow(
-                  this.currentlyDisplayedText, (var6 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, var7 / 2 - 4 - 16, 16777215
+            if (var8) {
+               Tessellator var17 = Tessellator.getInstance();
+               BufferBuilder var10 = var17.getBuffer();
+               CustomLoadingScreen var11 = CustomLoadingScreens.getCustomLoadingScreen();
+               if (var11 != null) {
+                  var11.drawBackground(var4.getScaledWidth(), var4.getScaledHeight());
+               } else {
+                  this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
+                  float var12 = 32.0F;
+                  var10.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
+                  var10.pos(0.0, var7, 0.0).tex(0.0, var7 / 32.0F).color(64, 64, 64, 255).endVertex();
+                  var10.pos(var6, var7, 0.0).tex(var6 / 32.0F, var7 / 32.0F).color(64, 64, 64, 255).endVertex();
+                  var10.pos(var6, 0.0, 0.0).tex(var6 / 32.0F, 0.0).color(64, 64, 64, 255).endVertex();
+                  var10.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
+                  var17.draw();
+               }
+
+               if (var1 >= 0) {
+                  byte var18 = 100;
+                  byte var13 = 2;
+                  int var14 = var6 / 2 - 50;
+                  int var15 = var7 / 2 + 16;
+                  GlStateManager.disableTexture2D();
+                  var10.begin(7, DefaultVertexFormats.POSITION_COLOR);
+                  var10.pos(var14, var15, 0.0).color(128, 128, 128, 255).endVertex();
+                  var10.pos(var14, var15 + 2, 0.0).color(128, 128, 128, 255).endVertex();
+                  var10.pos(var14 + 100, var15 + 2, 0.0).color(128, 128, 128, 255).endVertex();
+                  var10.pos(var14 + 100, var15, 0.0).color(128, 128, 128, 255).endVertex();
+                  var10.pos(var14, var15, 0.0).color(128, 255, 128, 255).endVertex();
+                  var10.pos(var14, var15 + 2, 0.0).color(128, 255, 128, 255).endVertex();
+                  var10.pos(var14 + var1, var15 + 2, 0.0).color(128, 255, 128, 255).endVertex();
+                  var10.pos(var14 + var1, var15, 0.0).color(128, 255, 128, 255).endVertex();
+                  var17.draw();
+                  GlStateManager.enableTexture2D();
+               }
+
+               GlStateManager.enableBlend();
+               GlStateManager.tryBlendFuncSeparate(
+                  GlStateManager.SourceFactor.SRC_ALPHA,
+                  GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
+                  GlStateManager.SourceFactor.ONE,
+                  GlStateManager.DestFactor.ZERO
                );
-            this.mc.fontRenderer.drawStringWithShadow(this.message, (var6 - this.mc.fontRenderer.getStringWidth(this.message)) / 2, var7 / 2 - 4 + 8, 16777215);
+               this.mc
+                  .fontRenderer
+                  .drawStringWithShadow(
+                     this.currentlyDisplayedText, (var6 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, var7 / 2 - 4 - 16, 16777215
+                  );
+               this.mc
+                  .fontRenderer
+                  .drawStringWithShadow(this.message, (var6 - this.mc.fontRenderer.getStringWidth(this.message)) / 2, var7 / 2 - 4 + 8, 16777215);
+            }
+
             this.framebuffer.unbindFramebuffer();
             if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferRender(var6 * var5, var7 * var5);
             }

             this.mc.updateDisplay();

             try {
                Thread.yield();
-            } catch (Exception var15) {
+            } catch (Exception var16) {
             }
          }
       }
    }
 */
