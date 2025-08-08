package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import net.optifine.util.FontUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Properties;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Shadow @Final protected ResourceLocation locationFontTexture;

    @Shadow @Final protected int[] charWidth;

    @SuppressWarnings("unused")
    @Shadow private float red;

    //TODO
    @Inject(method = "readFontTexture", at = @At("RETURN"))
    public void injectReadFontTexture(CallbackInfo ci) {
        if (Config.isCustomFonts()) {
            Properties props = FontUtils.readFontProperties(this.locationFontTexture);
            //this.blend = FontUtils.readBoolean(props, "blend", false);
            float[] charWidthFloat = new float[this.charWidth.length];
            Arrays.fill(charWidthFloat, -1f);
            FontUtils.readCustomCharWidths(props, charWidthFloat);
            for (int i = 0; i < this.charWidth.length; ++i) {
                if (charWidthFloat[i] > 0) {
                    this.charWidth[i] = Math.round(charWidthFloat[i]);
                }
            }
        }
    }

    @ModifyReturnValue(method = "getColorCode", at = @At("RETURN"))
    public int injectGetColorCode(int cir, @Local(argsOnly = true) char character){
        if (Config.isCustomColors()) {
            return CustomColors.getTextColor("0123456789abcdef".indexOf(character), cir);
        } else return cir;
    }
}
