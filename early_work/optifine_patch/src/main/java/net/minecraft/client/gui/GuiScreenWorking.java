/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.IProgressUpdate
 *  net.optifine.CustomLoadingScreen
 *  net.optifine.CustomLoadingScreens
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IProgressUpdate;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;

public class GuiScreenWorking
extends GuiScreen
implements IProgressUpdate {
    private String title = "";
    private String stage = "";
    private int progress;
    private boolean doneWorking;
    private CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    public void displaySavingString(String message) {
        this.resetProgressAndMessage(message);
    }

    public void resetProgressAndMessage(String message) {
        this.title = message;
        this.displayLoadingString("Working...");
    }

    public void displayLoadingString(String message) {
        this.stage = message;
        this.setLoadingProgress(0);
    }

    public void setLoadingProgress(int progress) {
        this.progress = progress;
    }

    public void setDoneWorking() {
        this.doneWorking = true;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.doneWorking) {
            if (!this.mc.isConnectedToRealms()) {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        } else {
            if (this.customLoadingScreen != null && this.mc.world == null) {
                this.customLoadingScreen.drawBackground(this.width, this.height);
            } else {
                this.drawDefaultBackground();
            }
            if (this.progress > 0) {
                this.a(this.fontRenderer, this.title, this.width / 2, 70, 0xFFFFFF);
                this.a(this.fontRenderer, this.stage + " " + this.progress + "%", this.width / 2, 90, 0xFFFFFF);
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
