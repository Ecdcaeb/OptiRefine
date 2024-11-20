/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiCustomizeSkin$ButtonPart
 *  net.minecraft.client.gui.GuiOptionButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.optifine.gui.GuiButtonOF
 *  net.optifine.gui.GuiScreenCapeOF
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.optifine.gui.GuiButtonOF;
import net.optifine.gui.GuiScreenCapeOF;

/*
 * Exception performing whole class analysis ignored.
 */
public class GuiCustomizeSkin
extends GuiScreen {
    private final GuiScreen parentScreen;
    private String title;

    public GuiCustomizeSkin(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    public void initGui() {
        int i = 0;
        this.title = I18n.format((String)"options.skinCustomisation.title", (Object[])new Object[0]);
        for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
            this.buttonList.add((Object)new ButtonPart(this, enumplayermodelparts.getPartId(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts, null));
            ++i;
        }
        this.buttonList.add((Object)new GuiOptionButton(199, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), GameSettings.Options.MAIN_HAND, this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND)));
        if (++i % 2 == 1) {
            ++i;
        }
        this.buttonList.add((Object)new GuiButtonOF(210, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format((String)"of.options.skinCustomisation.ofCape", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * ((i += 2) >> 1), I18n.format((String)"gui.done", (Object[])new Object[0])));
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.gameSettings.saveOptions();
        }
        super.keyTyped(typedChar, keyCode);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 210) {
                this.mc.displayGuiScreen((GuiScreen)new GuiScreenCapeOF((GuiScreen)this));
            }
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == 199) {
                this.mc.gameSettings.setOptionValue(GameSettings.Options.MAIN_HAND, 1);
                button.displayString = this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND);
                this.mc.gameSettings.sendSettingsToServer();
            } else if (button instanceof ButtonPart) {
                EnumPlayerModelParts enumplayermodelparts = ButtonPart.access$100((ButtonPart)((ButtonPart)button));
                this.mc.gameSettings.switchModelPartEnabled(enumplayermodelparts);
                button.displayString = this.getMessage(enumplayermodelparts);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.a(this.fontRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String getMessage(EnumPlayerModelParts playerModelParts) {
        String s = this.mc.gameSettings.getModelParts().contains((Object)playerModelParts) ? I18n.format((String)"options.on", (Object[])new Object[0]) : I18n.format((String)"options.off", (Object[])new Object[0]);
        return playerModelParts.getName().getFormattedText() + ": " + s;
    }

    static /* synthetic */ String access$200(GuiCustomizeSkin x0, EnumPlayerModelParts x1) {
        return x0.getMessage(x1);
    }
}
