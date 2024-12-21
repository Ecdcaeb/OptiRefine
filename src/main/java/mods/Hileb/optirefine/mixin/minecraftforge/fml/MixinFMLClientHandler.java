package mods.Hileb.optirefine.mixin.minecraftforge.fml;

import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FMLClientHandler.class)
public class MixinFMLClientHandler {
    @Inject(method = "handleLoadingScreen", at = @At("RETURN"), remap = false, cancellable = true)
    public void inject$handleLoadingScreen(CallbackInfoReturnable<Boolean> returnable){
        if (!returnable.getReturnValueZ()) {
            CustomLoadingScreen scr = CustomLoadingScreens.getCustomLoadingScreen();
            if (scr != null) {
                scr.drawBackground(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
                returnable.setReturnValue(true);
            }
        }
    }
}
