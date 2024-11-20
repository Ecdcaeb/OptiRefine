/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.Runnables
 *  java.io.BufferedReader
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.URI
 *  java.nio.charset.StandardCharsets
 *  java.util.ArrayList
 *  java.util.Calendar
 *  java.util.Date
 *  java.util.List
 *  java.util.Random
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiButtonLanguage
 *  net.minecraft.client.gui.GuiConfirmOpenLink
 *  net.minecraft.client.gui.GuiLanguage
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiWinGame
 *  net.minecraft.client.gui.GuiWorldSelection
 *  net.minecraft.client.gui.GuiYesNo
 *  net.minecraft.client.gui.GuiYesNoCallback
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.StringUtils
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.world.WorldServerDemo
 *  net.minecraft.world.storage.ISaveFormat
 *  net.minecraft.world.storage.WorldInfo
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.gui.NotificationModUpdateScreen
 *  net.minecraftforge.fml.client.GuiModList
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.util.glu.Project
 */
package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

@SideOnly(value=Side.CLIENT)
public class GuiMainMenu
extends GuiScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    private final float minceraftRoll;
    private String splashText;
    private GuiButton buttonResetDemo;
    private float panoramaTimer;
    private DynamicTexture viewportTexture;
    private final Object threadLock;
    public static final String MORE_INFO_TEXT = "Please click " + String.valueOf((Object)TextFormatting.UNDERLINE) + "here" + String.valueOf((Object)TextFormatting.RESET) + " for more information.";
    private int openGLWarning2Width;
    private int openGLWarning1Width;
    private int openGLWarningX1;
    private int openGLWarningY1;
    private int openGLWarningX2;
    private int openGLWarningY2;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation field_194400_H = new ResourceLocation("textures/gui/title/edition.png");
    private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private ResourceLocation backgroundTexture;
    private GuiButton realmsButton;
    private boolean hasCheckedForRealmsNotification;
    private GuiScreen realmsNotification;
    private int widthCopyright;
    private int widthCopyrightRest;
    private GuiButton modButton;
    private NotificationModUpdateScreen modUpdateNotification;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public GuiMainMenu() {
        IResource iresource;
        block6: {
            this.threadLock = new Object();
            this.openGLWarning2 = MORE_INFO_TEXT;
            this.splashText = "missingno";
            iresource = null;
            try {
                String s;
                ArrayList list = Lists.newArrayList();
                iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
                BufferedReader bufferedreader = new BufferedReader((Reader)new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
                while ((s = bufferedreader.readLine()) != null) {
                    if ((s = s.trim()).isEmpty()) continue;
                    list.add((Object)s);
                }
                if (list.isEmpty()) break block6;
                do {
                    this.splashText = (String)list.get(RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
            catch (IOException iOException) {
                IOUtils.closeQuietly(iresource);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(iresource);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Closeable)iresource);
        this.minceraftRoll = RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format((String)"title.oldgl1", (Object[])new Object[0]);
            this.openGLWarning2 = I18n.format((String)"title.oldgl2", (Object[])new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    private boolean areRealmsNotificationsEnabled() {
        return false;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        this.widthCopyright = this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!");
        this.widthCopyrightRest = this.width - this.widthCopyright - 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        int i = 24;
        int j = this.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }
        this.buttonList.add((Object)new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format((String)"menu.options", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format((String)"menu.quit", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));
        Object object = this.threadLock;
        synchronized (object) {
            this.openGLWarning1Width = this.fontRenderer.getStringWidth(this.openGLWarning1);
            this.openGLWarning2Width = this.fontRenderer.getStringWidth(this.openGLWarning2);
            int k = Math.max((int)this.openGLWarning1Width, (int)this.openGLWarning2Width);
            this.openGLWarningX1 = (this.width - k) / 2;
            this.openGLWarningY1 = ((GuiButton)this.buttonList.get((int)0)).y - 24;
            this.openGLWarningX2 = this.openGLWarningX1 + k;
            this.openGLWarningY2 = this.openGLWarningY1 + 24;
        }
        this.mc.setConnectedToRealms(false);
        this.modUpdateNotification = NotificationModUpdateScreen.init((GuiMainMenu)this, (GuiButton)this.modButton);
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add((Object)new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format((String)"menu.singleplayer", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format((String)"menu.multiplayer", (Object[])new Object[0])));
        this.modButton = new GuiButton(6, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format((String)"fml.menu.mods", (Object[])new Object[0]));
        this.buttonList.add((Object)this.modButton);
    }

    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        this.buttonList.add((Object)new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format((String)"menu.playdemo", (Object[])new Object[0])));
        this.buttonResetDemo = this.addButton(new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format((String)"menu.resetdemo", (Object[])new Object[0])));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        ISaveFormat isaveformat;
        WorldInfo worldinfo;
        if (button.id == 0) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 6) {
            this.mc.displayGuiScreen((GuiScreen)new GuiModList((GuiScreen)this));
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
        }
        if (button.id == 12 && (worldinfo = (isaveformat = this.mc.getSaveLoader()).getWorldInfo("Demo_World")) != null) {
            this.mc.displayGuiScreen((GuiScreen)new GuiYesNo((GuiYesNoCallback)this, I18n.format((String)"selectWorld.deleteQuestion", (Object[])new Object[0]), "'" + worldinfo.getWorldName() + "' " + I18n.format((String)"selectWorld.deleteWarning", (Object[])new Object[0]), I18n.format((String)"selectWorld.deleteButton", (Object[])new Object[0]), I18n.format((String)"gui.cancel", (Object[])new Object[0]), 12));
        }
    }

    private void switchToRealms() {
    }

    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen((GuiScreen)this);
        } else if (id == 12) {
            this.mc.displayGuiScreen((GuiScreen)this);
        } else if (id == 13) {
            if (result) {
                try {
                    Class oclass = Class.forName((String)"java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI(this.openGLWarningLink)});
                }
                catch (Throwable throwable) {
                    LOGGER.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen((GuiScreen)this);
        }
    }

    private void drawPanorama(int mouseX, int mouseY, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.matrixMode((int)5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GlStateManager.matrixMode((int)5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.rotate((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        int i = 8;
        for (int j = 0; j < 64; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float)(j % 8) / 8.0f - 0.5f) / 64.0f;
            float f1 = ((float)(j / 8) / 8.0f - 0.5f) / 64.0f;
            float f2 = 0.0f;
            GlStateManager.translate((float)f, (float)f1, (float)0.0f);
            GlStateManager.rotate((float)(MathHelper.sin((float)(this.panoramaTimer / 400.0f)) * 25.0f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(-this.panoramaTimer * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();
                if (k == 1) {
                    GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 2) {
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 3) {
                    GlStateManager.rotate((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (k == 4) {
                    GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (k == 5) {
                    GlStateManager.rotate((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                this.mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[k]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                float f3 = 0.0f;
                bufferbuilder.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        }
        bufferbuilder.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.matrixMode((int)5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox() {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GlStateManager.glTexParameteri((int)3553, (int)10241, (int)9729);
        GlStateManager.glTexParameteri((int)3553, (int)10240, (int)9729);
        GlStateManager.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)256, (int)256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;
        for (int j = 0; j < 3; ++j) {
            float f = 1.0f / (float)(j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float)(j - 1) / 256.0f;
            bufferbuilder.pos((double)k, (double)l, (double)this.e).tex((double)(0.0f + f1), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos((double)k, 0.0, (double)this.e).tex((double)(1.0f + f1), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos(0.0, 0.0, (double)this.e).tex((double)(1.0f + f1), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos(0.0, (double)l, (double)this.e).tex((double)(0.0f + f1), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
    }

    private void renderSkybox(int mouseX, int mouseY, float partialTicks) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport((int)0, (int)0, (int)256, (int)256);
        this.drawPanorama(mouseX, mouseY, partialTicks);
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
        float f = 120.0f / (float)(this.width > this.height ? this.width : this.height);
        float f1 = (float)this.height * f / 256.0f;
        float f2 = (float)this.width * f / 256.0f;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, (double)j, (double)this.e).tex((double)(0.5f - f1), (double)(0.5f + f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos((double)i, (double)j, (double)this.e).tex((double)(0.5f - f1), (double)(0.5f - f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos((double)i, 0.0, (double)this.e).tex((double)(0.5f + f1), (double)(0.5f - f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(0.0, 0.0, (double)this.e).tex((double)(0.5f + f1), (double)(0.5f + f2)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.panoramaTimer += partialTicks;
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        int i = 274;
        int j = this.width / 2 - 137;
        int k = 30;
        this.a(0, 0, this.width, this.height, -2130706433, 0xFFFFFF);
        this.a(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if ((double)this.minceraftRoll < 1.0E-4) {
            this.b(j + 0, 30, 0, 0, 99, 44);
            this.b(j + 99, 30, 129, 0, 27, 44);
            this.b(j + 99 + 26, 30, 126, 0, 3, 44);
            this.b(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
            this.b(j + 155, 30, 0, 45, 155, 44);
        } else {
            this.b(j + 0, 30, 0, 0, 155, 44);
            this.b(j + 155, 30, 0, 45, 155, 44);
        }
        this.mc.getTextureManager().bindTexture(field_194400_H);
        GuiMainMenu.a((int)(j + 88), (int)67, (float)0.0f, (float)0.0f, (int)98, (int)14, (float)128.0f, (float)16.0f);
        this.splashText = ForgeHooksClient.renderMainMenu((GuiMainMenu)this, (FontRenderer)this.fontRenderer, (int)this.width, (int)this.height, (String)this.splashText);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2 + 90), (float)70.0f, (float)0.0f);
        GlStateManager.rotate((float)-20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float f = 1.8f - MathHelper.abs((float)(MathHelper.sin((float)((float)(Minecraft.getSystemTime() % 1000L) / 1000.0f * ((float)Math.PI * 2))) * 0.1f));
        f = f * 100.0f / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
        GlStateManager.scale((float)f, (float)f, (float)f);
        this.a(this.fontRenderer, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        String s = "Minecraft 1.12.2";
        s = this.mc.isDemo() ? s + " Demo" : s + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType());
        List brandings = Lists.reverse((List)FMLCommonHandler.instance().getBrandings(true));
        for (int brdline = 0; brdline < brandings.size(); ++brdline) {
            String brd = (String)brandings.get(brdline);
            if (Strings.isNullOrEmpty((String)brd)) continue;
            this.c(this.fontRenderer, brd, 2, this.height - (10 + brdline * (this.fontRenderer.FONT_HEIGHT + 1)), 0xFFFFFF);
        }
        this.c(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, -1);
        if (mouseX > this.widthCopyrightRest && mouseX < this.widthCopyrightRest + this.widthCopyright && mouseY > this.height - 10 && mouseY < this.height && Mouse.isInsideWindow()) {
            GuiMainMenu.a((int)this.widthCopyrightRest, (int)(this.height - 1), (int)(this.widthCopyrightRest + this.widthCopyright), (int)this.height, (int)-1);
        }
        if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty()) {
            GuiMainMenu.a((int)(this.openGLWarningX1 - 2), (int)(this.openGLWarningY1 - 2), (int)(this.openGLWarningX2 + 2), (int)(this.openGLWarningY2 - 1), (int)0x55200000);
            this.c(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
            this.c(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, ((GuiButton)this.buttonList.get((int)0)).y - 12, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.modUpdateNotification.a(mouseX, mouseY, partialTicks);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Object object = this.threadLock;
        synchronized (object) {
            if (!this.openGLWarning1.isEmpty() && !StringUtils.isNullOrEmpty((String)this.openGLWarningLink) && mouseX >= this.openGLWarningX1 && mouseX <= this.openGLWarningX2 && mouseY >= this.openGLWarningY1 && mouseY <= this.openGLWarningY2) {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink((GuiYesNoCallback)this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen((GuiScreen)guiconfirmopenlink);
            }
        }
        if (mouseX > this.widthCopyrightRest && mouseX < this.widthCopyrightRest + this.widthCopyright && mouseY > this.height - 10 && mouseY < this.height) {
            this.mc.displayGuiScreen((GuiScreen)new GuiWinGame(false, Runnables.doNothing()));
        }
    }
}
