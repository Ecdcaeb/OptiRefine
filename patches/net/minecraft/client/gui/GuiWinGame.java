package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiWinGame extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
   private static final ResourceLocation field_194401_g = new ResourceLocation("textures/gui/title/edition.png");
   private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
   private final boolean poem;
   private final Runnable onFinished;
   private float time;
   private List<String> lines;
   private int totalScrollLength;
   private float scrollSpeed = 0.5F;

   public GuiWinGame(boolean var1, Runnable var2) {
      this.poem = ☃;
      this.onFinished = ☃;
      if (!☃) {
         this.scrollSpeed = 0.75F;
      }
   }

   @Override
   public void updateScreen() {
      this.mc.getMusicTicker().update();
      this.mc.getSoundHandler().update();
      float ☃ = (this.totalScrollLength + this.height + this.height + 24) / this.scrollSpeed;
      if (this.time > ☃) {
         this.sendRespawnPacket();
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (☃ == 1) {
         this.sendRespawnPacket();
      }
   }

   private void sendRespawnPacket() {
      this.onFinished.run();
      this.mc.displayGuiScreen(null);
   }

   @Override
   public boolean doesGuiPauseGame() {
      return true;
   }

   @Override
   public void initGui() {
      if (this.lines == null) {
         this.lines = Lists.newArrayList();
         IResource ☃ = null;

         try {
            String ☃x = "" + TextFormatting.WHITE + TextFormatting.OBFUSCATED + TextFormatting.GREEN + TextFormatting.AQUA;
            int ☃xx = 274;
            if (this.poem) {
               ☃ = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt"));
               InputStream ☃xxx = ☃.getInputStream();
               BufferedReader ☃xxxx = new BufferedReader(new InputStreamReader(☃xxx, StandardCharsets.UTF_8));
               Random ☃xxxxx = new Random(8124371L);

               String ☃xxxxxx;
               while ((☃xxxxxx = ☃xxxx.readLine()) != null) {
                  ☃xxxxxx = ☃xxxxxx.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());

                  while (☃xxxxxx.contains(☃x)) {
                     int ☃xxxxxxx = ☃xxxxxx.indexOf(☃x);
                     String ☃xxxxxxxx = ☃xxxxxx.substring(0, ☃xxxxxxx);
                     String ☃xxxxxxxxx = ☃xxxxxx.substring(☃xxxxxxx + ☃x.length());
                     ☃xxxxxx = ☃xxxxxxxx + TextFormatting.WHITE + TextFormatting.OBFUSCATED + "XXXXXXXX".substring(0, ☃xxxxx.nextInt(4) + 3) + ☃xxxxxxxxx;
                  }

                  this.lines.addAll(this.mc.fontRenderer.listFormattedStringToWidth(☃xxxxxx, 274));
                  this.lines.add("");
               }

               ☃xxx.close();

               for (int ☃xxxxxxx = 0; ☃xxxxxxx < 8; ☃xxxxxxx++) {
                  this.lines.add("");
               }
            }

            InputStream ☃xxx = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
            BufferedReader ☃xxxx = new BufferedReader(new InputStreamReader(☃xxx, StandardCharsets.UTF_8));

            String ☃xxxxx;
            while ((☃xxxxx = ☃xxxx.readLine()) != null) {
               ☃xxxxx = ☃xxxxx.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
               ☃xxxxx = ☃xxxxx.replaceAll("\t", "    ");
               this.lines.addAll(this.mc.fontRenderer.listFormattedStringToWidth(☃xxxxx, 274));
               this.lines.add("");
            }

            ☃xxx.close();
            this.totalScrollLength = this.lines.size() * 12;
         } catch (Exception var14) {
            LOGGER.error("Couldn't load credits", var14);
         } finally {
            IOUtils.closeQuietly(☃);
         }
      }
   }

   private void drawWinGameScreen(int var1, int var2, float var3) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      int ☃xx = this.width;
      float ☃xxx = -this.time * 0.5F * this.scrollSpeed;
      float ☃xxxx = this.height - this.time * 0.5F * this.scrollSpeed;
      float ☃xxxxx = 0.015625F;
      float ☃xxxxxx = this.time * 0.02F;
      float ☃xxxxxxx = (this.totalScrollLength + this.height + this.height + 24) / this.scrollSpeed;
      float ☃xxxxxxxx = (☃xxxxxxx - 20.0F - this.time) * 0.005F;
      if (☃xxxxxxxx < ☃xxxxxx) {
         ☃xxxxxx = ☃xxxxxxxx;
      }

      if (☃xxxxxx > 1.0F) {
         ☃xxxxxx = 1.0F;
      }

      ☃xxxxxx *= ☃xxxxxx;
      ☃xxxxxx = ☃xxxxxx * 96.0F / 255.0F;
      ☃x.pos(0.0, this.height, this.zLevel).tex(0.0, ☃xxx * 0.015625F).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, 1.0F).endVertex();
      ☃x.pos(☃xx, this.height, this.zLevel).tex(☃xx * 0.015625F, ☃xxx * 0.015625F).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, 1.0F).endVertex();
      ☃x.pos(☃xx, 0.0, this.zLevel).tex(☃xx * 0.015625F, ☃xxxx * 0.015625F).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, 1.0F).endVertex();
      ☃x.pos(0.0, 0.0, this.zLevel).tex(0.0, ☃xxxx * 0.015625F).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, 1.0F).endVertex();
      ☃.draw();
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawWinGameScreen(☃, ☃, ☃);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      int ☃xx = 274;
      int ☃xxx = this.width / 2 - 137;
      int ☃xxxx = this.height + 50;
      this.time += ☃;
      float ☃xxxxx = -this.time * this.scrollSpeed;
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, ☃xxxxx, 0.0F);
      this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableAlpha();
      this.drawTexturedModalRect(☃xxx, ☃xxxx, 0, 0, 155, 44);
      this.drawTexturedModalRect(☃xxx + 155, ☃xxxx, 0, 45, 155, 44);
      this.mc.getTextureManager().bindTexture(field_194401_g);
      drawModalRectWithCustomSizedTexture(☃xxx + 88, ☃xxxx + 37, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
      GlStateManager.disableAlpha();
      int ☃xxxxxx = ☃xxxx + 100;

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < this.lines.size(); ☃xxxxxxx++) {
         if (☃xxxxxxx == this.lines.size() - 1) {
            float ☃xxxxxxxx = ☃xxxxxx + ☃xxxxx - (this.height / 2 - 6);
            if (☃xxxxxxxx < 0.0F) {
               GlStateManager.translate(0.0F, -☃xxxxxxxx, 0.0F);
            }
         }

         if (☃xxxxxx + ☃xxxxx + 12.0F + 8.0F > 0.0F && ☃xxxxxx + ☃xxxxx < this.height) {
            String ☃xxxxxxxx = this.lines.get(☃xxxxxxx);
            if (☃xxxxxxxx.startsWith("[C]")) {
               this.fontRenderer
                  .drawStringWithShadow(☃xxxxxxxx.substring(3), ☃xxx + (274 - this.fontRenderer.getStringWidth(☃xxxxxxxx.substring(3))) / 2, ☃xxxxxx, 16777215);
            } else {
               this.fontRenderer.fontRandom.setSeed((long)((float)(☃xxxxxxx * 4238972211L) + this.time / 4.0F));
               this.fontRenderer.drawStringWithShadow(☃xxxxxxxx, ☃xxx, ☃xxxxxx, 16777215);
            }
         }

         ☃xxxxxx += 12;
      }

      GlStateManager.popMatrix();
      this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
      int ☃xxxxxxx = this.width;
      int ☃xxxxxxxx = this.height;
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃x.pos(0.0, ☃xxxxxxxx, this.zLevel).tex(0.0, 1.0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃x.pos(☃xxxxxxx, ☃xxxxxxxx, this.zLevel).tex(1.0, 1.0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃x.pos(☃xxxxxxx, 0.0, this.zLevel).tex(1.0, 0.0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃x.pos(0.0, 0.0, this.zLevel).tex(0.0, 0.0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃.draw();
      GlStateManager.disableBlend();
      super.drawScreen(☃, ☃, ☃);
   }
}
