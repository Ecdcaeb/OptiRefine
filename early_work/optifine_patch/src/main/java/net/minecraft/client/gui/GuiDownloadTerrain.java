/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.resources.I18n
 *  net.optifine.CustomLoadingScreen
 *  net.optifine.CustomLoadingScreens
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;

public class GuiDownloadTerrain
extends GuiScreen {
    private CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    public void initGui() {
        this.buttonList.clear();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.customLoadingScreen != null) {
            this.customLoadingScreen.drawBackground(this.width, this.height);
        } else {
            this.drawBackground(0);
        }
        this.a(this.fontRenderer, I18n.format((String)"multiplayer.downloadingTerrain", (Object[])new Object[0]), this.width / 2, this.height / 2 - 50, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
