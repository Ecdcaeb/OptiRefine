package net.minecraft.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import net.optifine.reflect.Reflector;

public class LoadingScreenRenderer implements IProgressUpdate {
   private String message = "";
   private final Minecraft mc;
   private String currentlyDisplayedText = "";
   private long systemTime = Minecraft.getSystemTime();
   private boolean loadingSuccess;
   private final ScaledResolution scaledResolution;
   private final Framebuffer framebuffer;

   public LoadingScreenRenderer(Minecraft mcIn) {
      this.mc = mcIn;
      this.scaledResolution = new ScaledResolution(mcIn);
      this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
      this.framebuffer.setFramebufferFilter(9728);
   }

   public void resetProgressAndMessage(String message) {
      this.loadingSuccess = false;
      this.displayString(message);
   }

   public void displaySavingString(String message) {
      this.loadingSuccess = true;
      this.displayString(message);
   }

   private void displayString(String message) {
      this.currentlyDisplayedText = message;
      if (!this.mc.running) {
         if (!this.loadingSuccess) {
            throw new MinecraftError();
         }
      } else {
         GlStateManager.clear(256);
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         if (OpenGlHelper.isFramebufferEnabled()) {
            int i = this.scaledResolution.getScaleFactor();
            GlStateManager.ortho(0.0, this.scaledResolution.getScaledWidth() * i, this.scaledResolution.getScaledHeight() * i, 0.0, 100.0, 300.0);
         } else {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
         }

         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -200.0F);
      }
   }

   public void displayLoadingString(String message) {
      if (!this.mc.running) {
         if (!this.loadingSuccess) {
            throw new MinecraftError();
         }
      } else {
         this.systemTime = 0L;
         this.message = message;
         this.setLoadingProgress(-1);
         this.systemTime = 0L;
      }
   }

   public void setLoadingProgress(int progress) {
      if (!this.mc.running) {
         if (!this.loadingSuccess) {
            throw new MinecraftError();
         }
      } else {
         long i = Minecraft.getSystemTime();
         if (i - this.systemTime >= 100L) {
            this.systemTime = i;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int j = scaledresolution.getScaleFactor();
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            if (OpenGlHelper.isFramebufferEnabled()) {
               this.framebuffer.framebufferClear();
            } else {
               GlStateManager.clear(256);
            }

            this.framebuffer.bindFramebuffer(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);
            if (!OpenGlHelper.isFramebufferEnabled()) {
               GlStateManager.clear(16640);
            }

            boolean render = true;
            if (Reflector.FMLClientHandler_handleLoadingScreen.exists()) {
               Object fch = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
               if (fch != null) {
                  render = !Reflector.callBoolean(fch, Reflector.FMLClientHandler_handleLoadingScreen, new Object[]{scaledresolution});
               }
            }

            if (render) {
               Tessellator tessellator = Tessellator.getInstance();
               BufferBuilder bufferbuilder = tessellator.getBuffer();
               CustomLoadingScreen scr = CustomLoadingScreens.getCustomLoadingScreen();
               if (scr != null) {
                  scr.drawBackground(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
               } else {
                  this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
                  float f = 32.0F;
                  bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                  bufferbuilder.pos(0.0, l, 0.0).tex(0.0, l / 32.0F).color(64, 64, 64, 255).endVertex();
                  bufferbuilder.pos(k, l, 0.0).tex(k / 32.0F, l / 32.0F).color(64, 64, 64, 255).endVertex();
                  bufferbuilder.pos(k, 0.0, 0.0).tex(k / 32.0F, 0.0).color(64, 64, 64, 255).endVertex();
                  bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
                  tessellator.draw();
               }

               if (progress >= 0) {
                  int i1 = 100;
                  int j1 = 2;
                  int k1 = k / 2 - 50;
                  int l1 = l / 2 + 16;
                  GlStateManager.disableTexture2D();
                  bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                  bufferbuilder.pos(k1, l1, 0.0).color(128, 128, 128, 255).endVertex();
                  bufferbuilder.pos(k1, l1 + 2, 0.0).color(128, 128, 128, 255).endVertex();
                  bufferbuilder.pos(k1 + 100, l1 + 2, 0.0).color(128, 128, 128, 255).endVertex();
                  bufferbuilder.pos(k1 + 100, l1, 0.0).color(128, 128, 128, 255).endVertex();
                  bufferbuilder.pos(k1, l1, 0.0).color(128, 255, 128, 255).endVertex();
                  bufferbuilder.pos(k1, l1 + 2, 0.0).color(128, 255, 128, 255).endVertex();
                  bufferbuilder.pos(k1 + progress, l1 + 2, 0.0).color(128, 255, 128, 255).endVertex();
                  bufferbuilder.pos(k1 + progress, l1, 0.0).color(128, 255, 128, 255).endVertex();
                  tessellator.draw();
                  GlStateManager.enableTexture2D();
               }

               GlStateManager.enableBlend();
               GlStateManager.tryBlendFuncSeparate(
                  GlStateManager.SourceFactor.SRC_ALPHA,
                  GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                  GlStateManager.SourceFactor.ONE,
                  GlStateManager.DestFactor.ZERO
               );
               this.mc
                  .fontRenderer
                  .drawStringWithShadow(
                     this.currentlyDisplayedText, (k - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, l / 2 - 4 - 16, 16777215
                  );
               this.mc.fontRenderer.drawStringWithShadow(this.message, (k - this.mc.fontRenderer.getStringWidth(this.message)) / 2, l / 2 - 4 + 8, 16777215);
            }

            this.framebuffer.unbindFramebuffer();
            if (OpenGlHelper.isFramebufferEnabled()) {
               this.framebuffer.framebufferRender(k * j, l * j);
            }

            this.mc.updateDisplay();

            try {
               Thread.yield();
            } catch (Exception var16) {
            }
         }
      }
   }

   public void setDoneWorking() {
   }
}
