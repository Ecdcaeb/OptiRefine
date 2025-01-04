package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChatAccessor.class)
public interface GuiChatAccessor {
    @Accessor
    GuiTextField getInputField();
}
