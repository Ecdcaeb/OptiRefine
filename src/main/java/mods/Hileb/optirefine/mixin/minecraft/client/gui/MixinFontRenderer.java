package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.util.FontUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @Shadow
    @Final
    protected ResourceLocation locationFontTexture;

    @Inject(method = "readFontTexture", at = @AT("RETURN"))
    public void injectReadFontTexture(CallbackInfo ci){
        Properties props = FontUtils.readFontProperties(this.locationFontTexture);
        //this.blend = FontUtils.readBoolean(props, "blend", false);
        FontUtils.readCustomCharWidths((Properties)props, (float[])this.charWidthFloat);
        for (int i = 0; i < this.charWidth.length; ++i) {
            this.charWidth[i] = Math.round((float)this.charWidthFloat[i]);
        }
    }
}
