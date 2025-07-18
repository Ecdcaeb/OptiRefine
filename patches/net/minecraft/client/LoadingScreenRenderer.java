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

public class LoadingScreenRenderer implements IProgressUpdate {
   private String message = "";
   private final Minecraft mc;
   private String currentlyDisplayedText = "";
   private long systemTime = Minecraft.getSystemTime();
   private boolean loadingSuccess;
   private final ScaledResolution scaledResolution;
   private final Framebuffer framebuffer;

   public LoadingScreenRenderer(Minecraft var1) {
      this.mc = ☃;
      this.scaledResolution = new ScaledResolution(☃);
      this.framebuffer = new Framebuffer(☃.displayWidth, ☃.displayHeight, false);
      this.framebuffer.setFramebufferFilter(9728);
   }

   @Override
   public void resetProgressAndMessage(String var1) {
      this.loadingSuccess = false;
      this.displayString(☃);
   }

   @Override
   public void displaySavingString(String var1) {
      this.loadingSuccess = true;
      this.displayString(☃);
   }

   private void displayString(String var1) {
      this.currentlyDisplayedText = ☃;
      if (!this.mc.running) {
         if (!this.loadingSuccess) {
            throw new MinecraftError();
         }
      } else {
         GlStateManager.clear(256);
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         if (OpenGlHelper.isFramebufferEnabled()) {
            int ☃ = this.scaledResolution.getScaleFactor();
            GlStateManager.ortho(0.0, this.scaledResolution.getScaledWidth() * ☃, this.scaledResolution.getScaledHeight() * ☃, 0.0, 100.0, 300.0);
         } else {
            ScaledResolution ☃ = new ScaledResolution(this.mc);
            GlStateManager.ortho(0.0, ☃.getScaledWidth_double(), ☃.getScaledHeight_double(), 0.0, 100.0, 300.0);
         }

         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -200.0F);
      }
   }

   @Override
   public void displayLoadingString(String var1) {
      if (!this.mc.running) {
         if (!this.loadingSuccess) {
            throw new MinecraftError();
         }
      } else {
         this.systemTime = 0L;
         this.message = ☃;
         this.setLoadingProgress(-1);
         this.systemTime = 0L;
      }
   }

   @Override
   public void setLoadingProgress(int var1) {
      if (!this.mc.running) {
         if (!this.loadingSuccess) {
            throw new MinecraftError();
         }
      } else {
         long ☃ = Minecraft.getSystemTime();
         if (☃ - this.systemTime >= 100L) {
            this.systemTime = ☃;
            ScaledResolution ☃x = new ScaledResolution(this.mc);
            int ☃xx = ☃x.getScaleFactor();
            int ☃xxx = ☃x.getScaledWidth();
            int ☃xxxx = ☃x.getScaledHeight();
            if (OpenGlHelper.isFramebufferEnabled()) {
               this.framebuffer.framebufferClear();
            } else {
               GlStateManager.clear(256);
            }

            this.framebuffer.bindFramebuffer(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, ☃x.getScaledWidth_double(), ☃x.getScaledHeight_double(), 0.0, 100.0, 300.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);
            if (!OpenGlHelper.isFramebufferEnabled()) {
               GlStateManager.clear(16640);
            }

            Tessellator ☃xxxxx = Tessellator.getInstance();
            BufferBuilder ☃xxxxxx = ☃xxxxx.getBuffer();
            this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
            float ☃xxxxxxx = 32.0F;
            ☃xxxxxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            ☃xxxxxx.pos(0.0, ☃xxxx, 0.0).tex(0.0, ☃xxxx / 32.0F).color(64, 64, 64, 255).endVertex();
            ☃xxxxxx.pos(☃xxx, ☃xxxx, 0.0).tex(☃xxx / 32.0F, ☃xxxx / 32.0F).color(64, 64, 64, 255).endVertex();
            ☃xxxxxx.pos(☃xxx, 0.0, 0.0).tex(☃xxx / 32.0F, 0.0).color(64, 64, 64, 255).endVertex();
            ☃xxxxxx.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
            ☃xxxxx.draw();
            if (☃ >= 0) {
               int ☃xxxxxxxx = 100;
               int ☃xxxxxxxxx = 2;
               int ☃xxxxxxxxxx = ☃xxx / 2 - 50;
               int ☃xxxxxxxxxxx = ☃xxxx / 2 + 16;
               GlStateManager.disableTexture2D();
               ☃xxxxxx.begin(7, DefaultVertexFormats.POSITION_COLOR);
               ☃xxxxxx.pos(☃xxxxxxxxxx, ☃xxxxxxxxxxx, 0.0).color(128, 128, 128, 255).endVertex();
               ☃xxxxxx.pos(☃xxxxxxxxxx, ☃xxxxxxxxxxx + 2, 0.0).color(128, 128, 128, 255).endVertex();
               ☃xxxxxx.pos(☃xxxxxxxxxx + 100, ☃xxxxxxxxxxx + 2, 0.0).color(128, 128, 128, 255).endVertex();
               ☃xxxxxx.pos(☃xxxxxxxxxx + 100, ☃xxxxxxxxxxx, 0.0).color(128, 128, 128, 255).endVertex();
               ☃xxxxxx.pos(☃xxxxxxxxxx, ☃xxxxxxxxxxx, 0.0).color(128, 255, 128, 255).endVertex();
               ☃xxxxxx.pos(☃xxxxxxxxxx, ☃xxxxxxxxxxx + 2, 0.0).color(128, 255, 128, 255).endVertex();
               ☃xxxxxx.pos(☃xxxxxxxxxx + ☃, ☃xxxxxxxxxxx + 2, 0.0).color(128, 255, 128, 255).endVertex();
               ☃xxxxxx.pos(☃xxxxxxxxxx + ☃, ☃xxxxxxxxxxx, 0.0).color(128, 255, 128, 255).endVertex();
               ☃xxxxx.draw();
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
                  this.currentlyDisplayedText, (☃xxx - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, ☃xxxx / 2 - 4 - 16, 16777215
               );
            this.mc
               .fontRenderer
               .drawStringWithShadow(this.message, (☃xxx - this.mc.fontRenderer.getStringWidth(this.message)) / 2, ☃xxxx / 2 - 4 + 8, 16777215);
            this.framebuffer.unbindFramebuffer();
            if (OpenGlHelper.isFramebufferEnabled()) {
               this.framebuffer.framebufferRender(☃xxx * ☃xx, ☃xxxx * ☃xx);
            }

            this.mc.updateDisplay();

            try {
               Thread.yield();
            } catch (Exception var15) {
            }
         }
      }
   }

   @Override
   public void setDoneWorking() {
   }
}
