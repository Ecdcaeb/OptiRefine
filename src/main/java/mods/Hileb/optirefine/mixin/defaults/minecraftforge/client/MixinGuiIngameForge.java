package mods.Hileb.optirefine.mixin.defaults.minecraftforge.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
@SuppressWarnings("ALL")
@Mixin(GuiIngameForge.class)
public abstract class MixinGuiIngameForge extends GuiIngame {


    public MixinGuiIngameForge(Minecraft minecraft) {
        super(minecraft);
    }

    // Forge draws experience in its own method, bypassing GuiIngame.renderExpBar.
    @WrapOperation(method = "renderExperience", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", remap = true))
    private int optiRefine$experienceTextColor(FontRenderer renderer, String text, int x, int y, int color, Operation<Integer> original) {
        if (color != 0 && Config.isCustomColors()) {
            color = CustomColors.getExpBarTextColor(color);
        }
        return original.call(renderer, text, x, y, color);
    }
}
