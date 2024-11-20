/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiListExtended
 *  net.minecraft.client.gui.GuiOptionsRowList
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class GuiVideoSettings
extends GuiScreen {
    private final GuiScreen parentGuiScreen;
    protected String screenTitle = "Video Settings";
    private final GameSettings guiGameSettings;
    private GuiListExtended optionsRowList;
    private static final GameSettings.Options[] VIDEO_OPTIONS = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.ANAGLYPH, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.ATTACK_INDICATOR, GameSettings.Options.GAMMA, GameSettings.Options.RENDER_CLOUDS, GameSettings.Options.PARTICLES, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.ENABLE_VSYNC, GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.USE_VBO, GameSettings.Options.ENTITY_SHADOWS};

    public GuiVideoSettings(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
        this.parentGuiScreen = parentScreenIn;
        this.guiGameSettings = gameSettingsIn;
    }

    public void initGui() {
        this.screenTitle = I18n.format((String)"options.videoTitle", (Object[])new Object[0]);
        this.buttonList.clear();
        this.buttonList.add((Object)new GuiButton(200, this.width / 2 - 100, this.height - 27, I18n.format((String)"gui.done", (Object[])new Object[0])));
        if (OpenGlHelper.vboSupported) {
            this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, VIDEO_OPTIONS);
        } else {
            GameSettings.Options[] agamesettings$options = new GameSettings.Options[VIDEO_OPTIONS.length - 1];
            int i = 0;
            for (GameSettings.Options gamesettings$options : VIDEO_OPTIONS) {
                if (gamesettings$options == GameSettings.Options.USE_VBO) break;
                agamesettings$options[i] = gamesettings$options;
                ++i;
            }
            this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, agamesettings$options);
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.optionsRowList.p();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.gameSettings.saveOptions();
        }
        super.keyTyped(typedChar, keyCode);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled && button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentGuiScreen);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int i = this.guiGameSettings.guiScale;
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.optionsRowList.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.guiGameSettings.guiScale != i) {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int j = scaledresolution.getScaledWidth();
            int k = scaledresolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, j, k);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        int i = this.guiGameSettings.guiScale;
        super.mouseReleased(mouseX, mouseY, state);
        this.optionsRowList.mouseReleased(mouseX, mouseY, state);
        if (this.guiGameSettings.guiScale != i) {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int j = scaledresolution.getScaledWidth();
            int k = scaledresolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, j, k);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.optionsRowList.a(mouseX, mouseY, partialTicks);
        this.a(this.fontRenderer, this.screenTitle, this.width / 2, 5, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        this.mc.gameSettings.onGuiClosed();
    }
}
