package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import net.optifine.util.FontUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @Shadow @Final protected ResourceLocation locationFontTexture;

    @Shadow @Final protected int[] charWidth;

    @Inject(method = "readFontTexture", at = @AT("RETURN"))
    public void injectReadFontTexture(CallbackInfo ci){
        Properties props = FontUtils.readFontProperties(this.locationFontTexture);
        //this.blend = FontUtils.readBoolean(props, "blend", false);
        float[] charWidthFloat = new float[this.charWidth.length];
        FontUtils.readCustomCharWidths(props, charWidthFloat);
        for (int i = 0; i < this.charWidth.length; ++i) {
            this.charWidth[i] = Math.round(charWidthFloat[i]);
        }
    }

    @Inject(method = "getColorCode", at = @AT("RETURN"), cancellable = true)
    public void injectGetColorCode(char character, CallbackInfoReturnable<Integer> cir){
        if (Config.isCustomColors()) {
            cir.setReturnValue(CustomColors.getTextColor("0123456789abcdef".indexOf(character), cir.getReturnValue()));
        }
    }
}
