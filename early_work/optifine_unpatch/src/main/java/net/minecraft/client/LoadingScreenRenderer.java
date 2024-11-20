/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Throwable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.util.IProgressUpdate
 *  net.minecraft.util.MinecraftError
 *  net.minecraftforge.fml.client.FMLClientHandler
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client;

import java.io.IOException;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class LoadingScreenRenderer
implements IProgressUpdate {
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
            GlStateManager.clear((int)256);
            GlStateManager.matrixMode((int)5889);
            GlStateManager.loadIdentity();
            if (OpenGlHelper.isFramebufferEnabled()) {
                int i = this.scaledResolution.getScaleFactor();
                GlStateManager.ortho((double)0.0, (double)(this.scaledResolution.getScaledWidth() * i), (double)(this.scaledResolution.getScaledHeight() * i), (double)0.0, (double)100.0, (double)300.0);
            } else {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                GlStateManager.ortho((double)0.0, (double)scaledresolution.getScaledWidth_double(), (double)scaledresolution.getScaledHeight_double(), (double)0.0, (double)100.0, (double)300.0);
            }
            GlStateManager.matrixMode((int)5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)-200.0f);
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
                    GlStateManager.clear((int)256);
                }
                this.framebuffer.bindFramebuffer(false);
                GlStateManager.matrixMode((int)5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho((double)0.0, (double)scaledresolution.getScaledWidth_double(), (double)scaledresolution.getScaledHeight_double(), (double)0.0, (double)100.0, (double)300.0);
                GlStateManager.matrixMode((int)5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate((float)0.0f, (float)0.0f, (float)-200.0f);
                if (!OpenGlHelper.isFramebufferEnabled()) {
                    GlStateManager.clear((int)16640);
                }
                try {
                    if (!FMLClientHandler.instance().handleLoadingScreen(scaledresolution)) {
                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder bufferbuilder = tessellator.getBuffer();
                        this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
                        float f = 32.0f;
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                        bufferbuilder.pos(0.0, (double)l, 0.0).tex(0.0, (double)((float)l / 32.0f)).color(64, 64, 64, 255).endVertex();
                        bufferbuilder.pos((double)k, (double)l, 0.0).tex((double)((float)k / 32.0f), (double)((float)l / 32.0f)).color(64, 64, 64, 255).endVertex();
                        bufferbuilder.pos((double)k, 0.0, 0.0).tex((double)((float)k / 32.0f), 0.0).color(64, 64, 64, 255).endVertex();
                        bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
                        tessellator.draw();
                        if (progress >= 0) {
                            int i1 = 100;
                            int j1 = 2;
                            int k1 = k / 2 - 50;
                            int l1 = l / 2 + 16;
                            GlStateManager.disableTexture2D();
                            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                            bufferbuilder.pos((double)k1, (double)l1, 0.0).color(128, 128, 128, 255).endVertex();
                            bufferbuilder.pos((double)k1, (double)(l1 + 2), 0.0).color(128, 128, 128, 255).endVertex();
                            bufferbuilder.pos((double)(k1 + 100), (double)(l1 + 2), 0.0).color(128, 128, 128, 255).endVertex();
                            bufferbuilder.pos((double)(k1 + 100), (double)l1, 0.0).color(128, 128, 128, 255).endVertex();
                            bufferbuilder.pos((double)k1, (double)l1, 0.0).color(128, 255, 128, 255).endVertex();
                            bufferbuilder.pos((double)k1, (double)(l1 + 2), 0.0).color(128, 255, 128, 255).endVertex();
                            bufferbuilder.pos((double)(k1 + progress), (double)(l1 + 2), 0.0).color(128, 255, 128, 255).endVertex();
                            bufferbuilder.pos((double)(k1 + progress), (double)l1, 0.0).color(128, 255, 128, 255).endVertex();
                            tessellator.draw();
                            GlStateManager.enableTexture2D();
                        }
                        GlStateManager.enableBlend();
                        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
                        this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (float)((k - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2), (float)(l / 2 - 4 - 16), 0xFFFFFF);
                        this.mc.fontRenderer.drawStringWithShadow(this.message, (float)((k - this.mc.fontRenderer.getStringWidth(this.message)) / 2), (float)(l / 2 - 4 + 8), 0xFFFFFF);
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException((Throwable)e);
                }
                this.framebuffer.unbindFramebuffer();
                if (OpenGlHelper.isFramebufferEnabled()) {
                    this.framebuffer.framebufferRender(k * j, l * j);
                }
                this.mc.updateDisplay();
                try {
                    Thread.yield();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    public void setDoneWorking() {
    }
}
