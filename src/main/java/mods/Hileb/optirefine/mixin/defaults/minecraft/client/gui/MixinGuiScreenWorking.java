package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(GuiScreenWorking.class)
public abstract class MixinGuiScreenWorking extends GuiScreen {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] baseline member progress exists in target class
    @Shadow private int progress;
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private CustomLoadingScreen optiRefine$customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

// [AUDIT-OK] target drawScreen(III)V matches baseline; drawDefaultBackground invoke (inherited GuiScreen) matches; replicates OF custom-loading bg
    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreenWorking;drawDefaultBackground()V"))
    public void injectDrawScreen(GuiScreenWorking instance, Operation<Void> original){
        if (this.optiRefine$customLoadingScreen != null && this.mc.world == null) {
            this.optiRefine$customLoadingScreen.drawBackground(this.width, this.height);
        } else {
            original.call(instance);
        }
    }

// [AUDIT-OK] two drawCenteredString invokes in baseline drawScreen; both gated on progress>0 matching OF drawScreen
    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreenWorking;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"))
    public boolean drawCenteredStringOnlyWhenProgressPositive(GuiScreenWorking instance, FontRenderer fontRenderer, String s, int i1, int i2, int i3){
        return this.progress > 0;
    }
}
