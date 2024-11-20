/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.IProgressUpdate
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking
extends GuiScreen
implements IProgressUpdate {
    private String title = "";
    private String stage = "";
    private int progress;
    private boolean doneWorking;

    public void displaySavingString(String string) {
        this.resetProgressAndMessage(string);
    }

    public void resetProgressAndMessage(String string) {
        this.title = string;
        this.displayLoadingString("Working...");
    }

    public void displayLoadingString(String string) {
        this.stage = string;
        this.setLoadingProgress(0);
    }

    public void setLoadingProgress(int n) {
        this.progress = n;
    }

    public void setDoneWorking() {
        this.doneWorking = true;
    }

    public void drawScreen(int n, int n2, float f) {
        if (this.doneWorking) {
            if (!this.mc.isConnectedToRealms()) {
                this.mc.displayGuiScreen(null);
            }
            return;
        }
        this.drawDefaultBackground();
        this.a(this.fontRenderer, this.title, this.width / 2, 70, 0xFFFFFF);
        this.a(this.fontRenderer, this.stage + " " + this.progress + "%", this.width / 2, 90, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }
}
