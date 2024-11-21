package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiScreen.class)
public interface GuiScreenAccessor {
    @Invoker("func_73733_a")
    void invokeDrawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor);
}
