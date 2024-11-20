/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiCustomizeSkin$ButtonPart
 *  net.minecraft.client.gui.GuiOptionButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraft.entity.player.EnumPlayerModelParts
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

/*
 * Exception performing whole class analysis ignored.
 */
public class GuiCustomizeSkin
extends GuiScreen {
    private final GuiScreen parentScreen;
    private String title;

    public GuiCustomizeSkin(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
    }

    public void initGui() {
        int n = 0;
        this.title = I18n.format((String)"options.skinCustomisation.title", (Object[])new Object[0]);
        for (EnumPlayerModelParts enumPlayerModelParts : EnumPlayerModelParts.values()) {
            this.buttonList.add((Object)new ButtonPart(this, enumPlayerModelParts.getPartId(), this.width / 2 - 155 + n % 2 * 160, this.height / 6 + 24 * (n >> 1), 150, 20, enumPlayerModelParts, null));
            ++n;
        }
        this.buttonList.add((Object)new GuiOptionButton(199, this.width / 2 - 155 + n % 2 * 160, this.height / 6 + 24 * (n >> 1), GameSettings.Options.MAIN_HAND, this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND)));
        if (++n % 2 == 1) {
            ++n;
        }
        this.buttonList.add((Object)new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * (n >> 1), I18n.format((String)"gui.done", (Object[])new Object[0])));
    }

    protected void keyTyped(char c, int n) {
        if (n == 1) {
            this.mc.gameSettings.saveOptions();
        }
        super.keyTyped(c, n);
    }

    protected void actionPerformed(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }
        if (guiButton.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (guiButton.id == 199) {
            this.mc.gameSettings.setOptionValue(GameSettings.Options.MAIN_HAND, 1);
            guiButton.displayString = this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND);
            this.mc.gameSettings.sendSettingsToServer();
        } else if (guiButton instanceof ButtonPart) {
            EnumPlayerModelParts enumPlayerModelParts = ButtonPart.access$100((ButtonPart)((ButtonPart)guiButton));
            this.mc.gameSettings.switchModelPartEnabled(enumPlayerModelParts);
            guiButton.displayString = this.getMessage(enumPlayerModelParts);
        }
    }

    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.a(this.fontRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    private String getMessage(EnumPlayerModelParts enumPlayerModelParts) {
        String string = this.mc.gameSettings.getModelParts().contains((Object)enumPlayerModelParts) ? I18n.format((String)"options.on", (Object[])new Object[0]) : I18n.format((String)"options.off", (Object[])new Object[0]);
        return enumPlayerModelParts.getName().getFormattedText() + ": " + string;
    }

    static /* synthetic */ String access$200(GuiCustomizeSkin guiCustomizeSkin, EnumPlayerModelParts enumPlayerModelParts) {
        return guiCustomizeSkin.getMessage(enumPlayerModelParts);
    }
}
