package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.optifine.gui.GuiButtonOF;
import net.optifine.gui.GuiScreenCapeOF;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiCustomizeSkin.class)
public abstract class MixinGuiCustomizeSkin extends GuiScreen {

    @Inject(method = "initGui", at = @At(value = "TAIL"))
    public void injectInitGui(CallbackInfo ci, @Local LocalIntRef lvt_1_1_){
        GuiButtonOF of = new GuiButtonOF(210, this.width / 2 - 100, this.height / 6 + 24 * (lvt_1_1_.get() >> 1), I18n.format("of.options.skinCustomisation.ofCape"));
        lvt_1_1_.set(lvt_1_1_.get() + 2);
        GuiButton done = this.buttonList.getLast();
        this.buttonList.add(this.buttonList.size() -1, of);
        done.y = this.height / 6 + 24 * (lvt_1_1_.get() >> 1);
        this.buttonList.add(done);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void injectActionPerformed(GuiButton button, CallbackInfo ci){
        if (button.enabled) {
            if (button.id == 210) {
                this.mc.displayGuiScreen(new GuiScreenCapeOF(this));
            }
        }
    }



}
