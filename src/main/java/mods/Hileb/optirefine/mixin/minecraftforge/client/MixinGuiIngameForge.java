package mods.Hileb.optirefine.mixin.minecraftforge.client;

import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiIngameForge.class)
public abstract class MixinGuiIngameForge extends GuiIngame {

}
