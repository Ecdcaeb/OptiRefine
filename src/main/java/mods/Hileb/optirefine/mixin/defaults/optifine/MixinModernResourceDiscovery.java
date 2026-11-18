package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.resources.ModernResourcePaths;
import net.minecraft.client.resources.IResourcePack;
import net.optifine.util.ResUtils;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ResUtils.class, remap = false)
public abstract class MixinModernResourceDiscovery {
    @WrapMethod(method = "collectFiles(Lnet/minecraft/client/resources/IResourcePack;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)[Ljava/lang/String;", remap = false)
    private static String[] optiRefine$collectModernFiles(IResourcePack pack, String[] prefixes, String[] suffixes,
                                                        String[] defaultPaths, Operation<String[]> original) {
        String[] expanded = ModernResourcePaths.discoveryPrefixes(prefixes);
        // G5 unwraps LegacyV2Adapter and scans the original AbstractResourcePack backing file,
        // including Forge's file and folder packs. Only the requested paths need translation.
        String[] files = original.call(pack, expanded, suffixes, defaultPaths);
        return expanded == prefixes ? files : ModernResourcePaths.discoveryFiles(files, prefixes);
    }
}
