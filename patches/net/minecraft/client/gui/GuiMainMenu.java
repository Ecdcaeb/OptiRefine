package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Random RANDOM = new Random();
   private final float minceraftRoll;
   private String splashText;
   private GuiButton buttonResetDemo;
   private float panoramaTimer;
   private DynamicTexture viewportTexture;
   private final Object threadLock = new Object();
   public static final String MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";
   private int openGLWarning2Width;
   private int openGLWarning1Width;
   private int openGLWarningX1;
   private int openGLWarningY1;
   private int openGLWarningX2;
   private int openGLWarningY2;
   private String openGLWarning1;
   private String openGLWarning2 = MORE_INFO_TEXT;
   private String openGLWarningLink;
   private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
   private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
   private static final ResourceLocation field_194400_H = new ResourceLocation("textures/gui/title/edition.png");
   private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[]{
      new ResourceLocation("textures/gui/title/background/panorama_0.png"),
      new ResourceLocation("textures/gui/title/background/panorama_1.png"),
      new ResourceLocation("textures/gui/title/background/panorama_2.png"),
      new ResourceLocation("textures/gui/title/background/panorama_3.png"),
      new ResourceLocation("textures/gui/title/background/panorama_4.png"),
      new ResourceLocation("textures/gui/title/background/panorama_5.png")
   };
   private ResourceLocation backgroundTexture;
   private GuiButton realmsButton;
   private boolean hasCheckedForRealmsNotification;
   private GuiScreen realmsNotification;
   private int widthCopyright;
   private int widthCopyrightRest;

   public GuiMainMenu() {
      this.splashText = "missingno";
      IResource ☃ = null;

      try {
         List<String> ☃x = Lists.newArrayList();
         ☃ = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
         BufferedReader ☃xx = new BufferedReader(new InputStreamReader(☃.getInputStream(), StandardCharsets.UTF_8));

         String ☃xxx;
         while ((☃xxx = ☃xx.readLine()) != null) {
            ☃xxx = ☃xxx.trim();
            if (!☃xxx.isEmpty()) {
               ☃x.add(☃xxx);
            }
         }

         if (!☃x.isEmpty()) {
            do {
               this.splashText = ☃x.get(RANDOM.nextInt(☃x.size()));
            } while (this.splashText.hashCode() == 125780783);
         }
      } catch (IOException var8) {
      } finally {
         IOUtils.closeQuietly(☃);
      }

      this.minceraftRoll = RANDOM.nextFloat();
      this.openGLWarning1 = "";
      if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
         this.openGLWarning1 = I18n.format("title.oldgl1");
         this.openGLWarning2 = I18n.format("title.oldgl2");
         this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
      }
   }

   private boolean areRealmsNotificationsEnabled() {
      return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.realmsNotification != null;
   }

   @Override
   public void updateScreen() {
      if (this.areRealmsNotificationsEnabled()) {
         this.realmsNotification.updateScreen();
      }
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   public void initGui() {
      this.viewportTexture = new DynamicTexture(256, 256);
      this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
      this.widthCopyright = this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!");
      this.widthCopyrightRest = this.width - this.widthCopyright - 2;
      Calendar ☃ = Calendar.getInstance();
      ☃.setTime(new Date());
      if (☃.get(2) + 1 == 12 && ☃.get(5) == 24) {
         this.splashText = "Merry X-mas!";
      } else if (☃.get(2) + 1 == 1 && ☃.get(5) == 1) {
         this.splashText = "Happy new year!";
      } else if (☃.get(2) + 1 == 10 && ☃.get(5) == 31) {
         this.splashText = "OOoooOOOoooo! Spooky!";
      }

      int ☃x = 24;
      int ☃xx = this.height / 4 + 48;
      if (this.mc.isDemo()) {
         this.addDemoButtons(☃xx, 24);
      } else {
         this.addSingleplayerMultiplayerButtons(☃xx, 24);
      }

      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, ☃xx + 72 + 12, 98, 20, I18n.format("menu.options")));
      this.buttonList.add(new GuiButton(4, this.width / 2 + 2, ☃xx + 72 + 12, 98, 20, I18n.format("menu.quit")));
      this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, ☃xx + 72 + 12));
      synchronized (this.threadLock) {
         this.openGLWarning1Width = this.fontRenderer.getStringWidth(this.openGLWarning1);
         this.openGLWarning2Width = this.fontRenderer.getStringWidth(this.openGLWarning2);
         int ☃xxx = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
         this.openGLWarningX1 = (this.width - ☃xxx) / 2;
         this.openGLWarningY1 = this.buttonList.get(0).y - 24;
         this.openGLWarningX2 = this.openGLWarningX1 + ☃xxx;
         this.openGLWarningY2 = this.openGLWarningY1 + 24;
      }

      this.mc.setConnectedToRealms(false);
      if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.hasCheckedForRealmsNotification) {
         RealmsBridge ☃xxx = new RealmsBridge();
         this.realmsNotification = ☃xxx.getNotificationScreen(this);
         this.hasCheckedForRealmsNotification = true;
      }

      if (this.areRealmsNotificationsEnabled()) {
         this.realmsNotification.setGuiSize(this.width, this.height);
         this.realmsNotification.initGui();
      }
   }

   private void addSingleplayerMultiplayerButtons(int var1, int var2) {
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, ☃, I18n.format("menu.singleplayer")));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, ☃ + ☃ * 1, I18n.format("menu.multiplayer")));
      this.realmsButton = this.addButton(new GuiButton(14, this.width / 2 - 100, ☃ + ☃ * 2, I18n.format("menu.online")));
   }

   private void addDemoButtons(int var1, int var2) {
      this.buttonList.add(new GuiButton(11, this.width / 2 - 100, ☃, I18n.format("menu.playdemo")));
      this.buttonResetDemo = this.addButton(new GuiButton(12, this.width / 2 - 100, ☃ + ☃ * 1, I18n.format("menu.resetdemo")));
      ISaveFormat ☃ = this.mc.getSaveLoader();
      WorldInfo ☃x = ☃.getWorldInfo("Demo_World");
      if (☃x == null) {
         this.buttonResetDemo.enabled = false;
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 0) {
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      }

      if (☃.id == 5) {
         this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
      }

      if (☃.id == 1) {
         this.mc.displayGuiScreen(new GuiWorldSelection(this));
      }

      if (☃.id == 2) {
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
      }

      if (☃.id == 14 && this.realmsButton.visible) {
         this.switchToRealms();
      }

      if (☃.id == 4) {
         this.mc.shutdown();
      }

      if (☃.id == 11) {
         this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
      }

      if (☃.id == 12) {
         ISaveFormat ☃ = this.mc.getSaveLoader();
         WorldInfo ☃x = ☃.getWorldInfo("Demo_World");
         if (☃x != null) {
            this.mc
               .displayGuiScreen(
                  new GuiYesNo(
                     this,
                     I18n.format("selectWorld.deleteQuestion"),
                     "'" + ☃x.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning"),
                     I18n.format("selectWorld.deleteButton"),
                     I18n.format("gui.cancel"),
                     12
                  )
               );
         }
      }
   }

   private void switchToRealms() {
      RealmsBridge ☃ = new RealmsBridge();
      ☃.switchToRealms(this);
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      if (☃ && ☃ == 12) {
         ISaveFormat ☃ = this.mc.getSaveLoader();
         ☃.flushCache();
         ☃.deleteWorldDirectory("Demo_World");
         this.mc.displayGuiScreen(this);
      } else if (☃ == 12) {
         this.mc.displayGuiScreen(this);
      } else if (☃ == 13) {
         if (☃) {
            try {
               Class<?> ☃ = Class.forName("java.awt.Desktop");
               Object ☃x = ☃.getMethod("getDesktop").invoke(null);
               ☃.getMethod("browse", URI.class).invoke(☃x, new URI(this.openGLWarningLink));
            } catch (Throwable var5) {
               LOGGER.error("Couldn't open link", var5);
            }
         }

         this.mc.displayGuiScreen(this);
      }
   }

   private void drawPanorama(int var1, int var2, float var3) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      GlStateManager.matrixMode(5889);
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.disableCull();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      int ☃xx = 8;

      for (int ☃xxx = 0; ☃xxx < 64; ☃xxx++) {
         GlStateManager.pushMatrix();
         float ☃xxxx = (☃xxx % 8 / 8.0F - 0.5F) / 64.0F;
         float ☃xxxxx = (☃xxx / 8 / 8.0F - 0.5F) / 64.0F;
         float ☃xxxxxx = 0.0F;
         GlStateManager.translate(☃xxxx, ☃xxxxx, 0.0F);
         GlStateManager.rotate(MathHelper.sin(this.panoramaTimer / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(-this.panoramaTimer * 0.1F, 0.0F, 1.0F, 0.0F);

         for (int ☃xxxxxxx = 0; ☃xxxxxxx < 6; ☃xxxxxxx++) {
            GlStateManager.pushMatrix();
            if (☃xxxxxxx == 1) {
               GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (☃xxxxxxx == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if (☃xxxxxxx == 3) {
               GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (☃xxxxxxx == 4) {
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (☃xxxxxxx == 5) {
               GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            this.mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[☃xxxxxxx]);
            ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            int ☃xxxxxxxx = 255 / (☃xxx + 1);
            float ☃xxxxxxxxx = 0.0F;
            ☃x.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, ☃xxxxxxxx).endVertex();
            ☃x.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, ☃xxxxxxxx).endVertex();
            ☃x.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, ☃xxxxxxxx).endVertex();
            ☃x.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, ☃xxxxxxxx).endVertex();
            ☃.draw();
            GlStateManager.popMatrix();
         }

         GlStateManager.popMatrix();
         GlStateManager.colorMask(true, true, true, false);
      }

      ☃x.setTranslation(0.0, 0.0, 0.0);
      GlStateManager.colorMask(true, true, true, true);
      GlStateManager.matrixMode(5889);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.enableDepth();
   }

   private void rotateAndBlurSkybox() {
      this.mc.getTextureManager().bindTexture(this.backgroundTexture);
      GlStateManager.glTexParameteri(3553, 10241, 9729);
      GlStateManager.glTexParameteri(3553, 10240, 9729);
      GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.colorMask(true, true, true, false);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      GlStateManager.disableAlpha();
      int ☃xx = 3;

      for (int ☃xxx = 0; ☃xxx < 3; ☃xxx++) {
         float ☃xxxx = 1.0F / (☃xxx + 1);
         int ☃xxxxx = this.width;
         int ☃xxxxxx = this.height;
         float ☃xxxxxxx = (☃xxx - 1) / 256.0F;
         ☃x.pos(☃xxxxx, ☃xxxxxx, this.zLevel).tex(0.0F + ☃xxxxxxx, 1.0).color(1.0F, 1.0F, 1.0F, ☃xxxx).endVertex();
         ☃x.pos(☃xxxxx, 0.0, this.zLevel).tex(1.0F + ☃xxxxxxx, 1.0).color(1.0F, 1.0F, 1.0F, ☃xxxx).endVertex();
         ☃x.pos(0.0, 0.0, this.zLevel).tex(1.0F + ☃xxxxxxx, 0.0).color(1.0F, 1.0F, 1.0F, ☃xxxx).endVertex();
         ☃x.pos(0.0, ☃xxxxxx, this.zLevel).tex(0.0F + ☃xxxxxxx, 0.0).color(1.0F, 1.0F, 1.0F, ☃xxxx).endVertex();
      }

      ☃.draw();
      GlStateManager.enableAlpha();
      GlStateManager.colorMask(true, true, true, true);
   }

   private void renderSkybox(int var1, int var2, float var3) {
      this.mc.getFramebuffer().unbindFramebuffer();
      GlStateManager.viewport(0, 0, 256, 256);
      this.drawPanorama(☃, ☃, ☃);
      this.rotateAndBlurSkybox();
      this.rotateAndBlurSkybox();
      this.rotateAndBlurSkybox();
      this.rotateAndBlurSkybox();
      this.rotateAndBlurSkybox();
      this.rotateAndBlurSkybox();
      this.rotateAndBlurSkybox();
      this.mc.getFramebuffer().bindFramebuffer(true);
      GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      float ☃ = 120.0F / (this.width > this.height ? this.width : this.height);
      float ☃x = this.height * ☃ / 256.0F;
      float ☃xx = this.width * ☃ / 256.0F;
      int ☃xxx = this.width;
      int ☃xxxx = this.height;
      Tessellator ☃xxxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxxx = ☃xxxxx.getBuffer();
      ☃xxxxxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃xxxxxx.pos(0.0, ☃xxxx, this.zLevel).tex(0.5F - ☃x, 0.5F + ☃xx).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃xxxxxx.pos(☃xxx, ☃xxxx, this.zLevel).tex(0.5F - ☃x, 0.5F - ☃xx).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃xxxxxx.pos(☃xxx, 0.0, this.zLevel).tex(0.5F + ☃x, 0.5F - ☃xx).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃xxxxxx.pos(0.0, 0.0, this.zLevel).tex(0.5F + ☃x, 0.5F + ☃xx).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      ☃xxxxx.draw();
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.panoramaTimer += ☃;
      GlStateManager.disableAlpha();
      this.renderSkybox(☃, ☃, ☃);
      GlStateManager.enableAlpha();
      int ☃ = 274;
      int ☃x = this.width / 2 - 137;
      int ☃xx = 30;
      this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
      this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
      this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.minceraftRoll < 1.0E-4) {
         this.drawTexturedModalRect(☃x + 0, 30, 0, 0, 99, 44);
         this.drawTexturedModalRect(☃x + 99, 30, 129, 0, 27, 44);
         this.drawTexturedModalRect(☃x + 99 + 26, 30, 126, 0, 3, 44);
         this.drawTexturedModalRect(☃x + 99 + 26 + 3, 30, 99, 0, 26, 44);
         this.drawTexturedModalRect(☃x + 155, 30, 0, 45, 155, 44);
      } else {
         this.drawTexturedModalRect(☃x + 0, 30, 0, 0, 155, 44);
         this.drawTexturedModalRect(☃x + 155, 30, 0, 45, 155, 44);
      }

      this.mc.getTextureManager().bindTexture(field_194400_H);
      drawModalRectWithCustomSizedTexture(☃x + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)(this.width / 2 + 90), 70.0F, 0.0F);
      GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
      float ☃xxx = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
      ☃xxx = ☃xxx * 100.0F / (this.fontRenderer.getStringWidth(this.splashText) + 32);
      GlStateManager.scale(☃xxx, ☃xxx, ☃xxx);
      this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
      GlStateManager.popMatrix();
      String ☃xxxx = "Minecraft 1.12.2";
      if (this.mc.isDemo()) {
         ☃xxxx = ☃xxxx + " Demo";
      } else {
         ☃xxxx = ☃xxxx + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType());
      }

      this.drawString(this.fontRenderer, ☃xxxx, 2, this.height - 10, -1);
      this.drawString(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, -1);
      if (☃ > this.widthCopyrightRest && ☃ < this.widthCopyrightRest + this.widthCopyright && ☃ > this.height - 10 && ☃ < this.height && Mouse.isInsideWindow()
         )
       {
         drawRect(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, -1);
      }

      if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty()) {
         drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
         this.drawString(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
         this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, this.buttonList.get(0).y - 12, -1);
      }

      super.drawScreen(☃, ☃, ☃);
      if (this.areRealmsNotificationsEnabled()) {
         this.realmsNotification.drawScreen(☃, ☃, ☃);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      synchronized (this.threadLock) {
         if (!this.openGLWarning1.isEmpty()
            && !StringUtils.isNullOrEmpty(this.openGLWarningLink)
            && ☃ >= this.openGLWarningX1
            && ☃ <= this.openGLWarningX2
            && ☃ >= this.openGLWarningY1
            && ☃ <= this.openGLWarningY2) {
            GuiConfirmOpenLink ☃ = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
            ☃.disableSecurityWarning();
            this.mc.displayGuiScreen(☃);
         }
      }

      if (this.areRealmsNotificationsEnabled()) {
         this.realmsNotification.mouseClicked(☃, ☃, ☃);
      }

      if (☃ > this.widthCopyrightRest && ☃ < this.widthCopyrightRest + this.widthCopyright && ☃ > this.height - 10 && ☃ < this.height) {
         this.mc.displayGuiScreen(new GuiWinGame(false, Runnables.doNothing()));
      }
   }

   @Override
   public void onGuiClosed() {
      if (this.realmsNotification != null) {
         this.realmsNotification.onGuiClosed();
      }
   }
}
