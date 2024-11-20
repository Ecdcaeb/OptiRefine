/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.resources.I18n
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiDownloadTerrain
extends GuiScreen {
    public void initGui() {
        this.buttonList.clear();
    }

    public void drawScreen(int n, int n2, float f) {
        this.drawBackground(0);
        this.a(this.fontRenderer, I18n.format((String)"multiplayer.downloadingTerrain", (Object[])new Object[0]), this.width / 2, this.height / 2 - 50, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
