package mods.Hileb.optirefine.mixin.defaults.minecraftforge.client;

import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;

@Checked
@SuppressWarnings("ALL")
@Mixin(GuiIngameForge.class)
public abstract class MixinGuiIngameForge extends GuiIngame {

    public MixinGuiIngameForge(Minecraft p_i46325_1_) {
        super(p_i46325_1_);
    }
}
