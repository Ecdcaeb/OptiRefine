package mods.Hileb.optirefine.mixin.optifine;

import optifine.OptiFineClassTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.net.*;

@Mixin(OptiFineClassTransformer.class)
public abstract class MixinOptiFineClassTransformer {

    @Redirect(method = "<init>()", remap = false, at = @At(value = "INVOKE", target = "Ljava/net/URL;toURI()Ljava/net/URI;"))
    private static URI url2uri(URL url, CallbackInfoReturnable<URI> returnable) throws IOException, URISyntaxException {
        URLConnection connection = url.openConnection();
        if (connection instanceof JarURLConnection jarURLConnection) {
            return jarURLConnection.getJarFileURL().toURI();
        } else  {
            return url.toURI();
        }
    }

}
