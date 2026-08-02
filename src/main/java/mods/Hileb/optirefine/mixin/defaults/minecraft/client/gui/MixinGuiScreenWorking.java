package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Checked
@Mixin(GuiScreenWorking.class)
public abstract class MixinGuiScreenWorking extends GuiScreen {

    @Shadow private int progress;
    @Unique
    private CustomLoadingScreen optiRefine$customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreenWorking;drawDefaultBackground()V"))
    public void injectDrawScreen(GuiScreenWorking instance, Operation<Void> original){
        if (this.optiRefine$customLoadingScreen != null && this.mc.world == null) {
            this.optiRefine$customLoadingScreen.drawBackground(this.width, this.height);
        } else {
            original.call(instance);
        }
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreenWorking;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"))
    public boolean drawCenteredStringOnlyWhenProgressPositive(GuiScreenWorking instance, FontRenderer fontRenderer, String s, int i1, int i2, int i3){
        return this.progress > 0;
    }
}
