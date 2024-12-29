package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    @Redirect(method = "renderExpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    public int redirectDrawString(FontRenderer instance, String p_78276_1_, int p_78276_2_, int p_78276_3_, int p_78276_4_){
        if (p_78276_4_ == 0) {
            return instance.drawString(p_78276_1_, p_78276_2_, p_78276_3_, p_78276_4_);
        } else {
            int col = p_78276_4_;
            if (Config.isCustomColors()) {
                 col = CustomColors.getExpBarTextColor(p_78276_4_);
            }
            return instance.drawString(p_78276_1_, p_78276_2_, p_78276_3_, col);
        }
    }
}
