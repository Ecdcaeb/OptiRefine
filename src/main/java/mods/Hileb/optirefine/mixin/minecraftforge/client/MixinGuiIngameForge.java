package mods.Hileb.optirefine.mixin.minecraftforge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("ALL")
@Mixin(GuiIngameForge.class)
public abstract class MixinGuiIngameForge extends GuiIngame {

    public MixinGuiIngameForge(Minecraft p_i46325_1_) {
        super(p_i46325_1_);
    }
}
