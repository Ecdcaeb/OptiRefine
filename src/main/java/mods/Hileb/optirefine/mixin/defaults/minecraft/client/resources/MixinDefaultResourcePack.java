package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.OptifineHelper;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
@Mixin(DefaultResourcePack.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinDefaultResourcePack {
    @SuppressWarnings("unused")
    @Unique
    // [AUDIT-OK] OF-added static field ON_WINDOWS (@Unique private static), matches OF
    private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;


    @WrapMethod(method = "getResourceStream")
    // [AUDIT-OK] target getResourceStream(ResourceLocation)LInputStream matches baseline; OptifineHelper == Forge port of ReflectorForge.getOptiFineResourceStream, matches OF
    public InputStream injectGetResourceStream(ResourceLocation location, Operation<InputStream> original){
        InputStream is = OptifineHelper.getOptifineResource("/assets/" + location.getNamespace() + "/" + location.getPath());
        if (is == null) return original.call(location);
        else return is;
    }

    @WrapOperation(method = "getResourceStream", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/FolderResourcePack;validatePath(Ljava/io/File;Ljava/lang/String;)Z"))
    // [AUDIT-OK] target FolderResourcePack.validatePath(File,String)Z (static, invoked from getResourceStream); body mirrors OF private validatePath (file: endsWith check, else delegate); instance handler legal per WrapOperation docs
    private boolean validatePath(File file, String string, Operation<Boolean> original) throws IOException {
        String var3 = file.getPath();
        if (var3.startsWith("file:")) {
            if (ON_WINDOWS) {
                var3 = var3.replace("\\", "/");
            }
            return var3.endsWith(string);
        } else {
            return original.call(file, string);
        }
    }

}
