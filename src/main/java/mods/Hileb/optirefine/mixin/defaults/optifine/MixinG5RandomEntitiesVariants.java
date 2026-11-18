package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.optifine.RandomEntities;
import net.optifine.RandomEntityProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = RandomEntities.class, remap = false)
public abstract class MixinG5RandomEntitiesVariants {
    @Shadow private static Map<String, RandomEntityProperties> mapProperties;

    @Shadow
    private static String getPathBase(String path) {
        throw new AssertionError();
    }

    @Shadow
    private static RandomEntityProperties makeProperties(ResourceLocation base, boolean mcpatcher) {
        throw new AssertionError();
    }

    @WrapOperation(method = "initialize", at = @At(value = "INVOKE",
            target = "Lnet/optifine/util/ResUtils;collectFiles([Ljava/lang/String;[Ljava/lang/String;)[Ljava/lang/String;"))
    private static String[] optiRefine$discoverDigitEndingBases(String[] prefixes, String[] suffixes,
                                                                 Operation<String[]> original) {
        String[] paths = original.call(prefixes, suffixes);
        for (String path : paths) {
            // Preserve the digit in a rule filename and in the base of a dot-separated variant.
            int end = path.lastIndexOf('.');
            if (path.endsWith(".png")) {
                int separator = path.lastIndexOf('.', end - 1);
                if (separator < 0 || separator + 1 == end) continue;
                int digit = separator + 1;
                while (digit < end && optiRefine$isDigit(path.charAt(digit))) ++digit;
                if (digit != end) continue;
                end = separator;
            }
            if (end <= 0 || !optiRefine$isDigit(path.charAt(end - 1))) continue;
            String basePath = getPathBase(path.substring(0, end) + ".png");
            if (basePath == null || mapProperties.containsKey(basePath)) continue;
            ResourceLocation base = new ResourceLocation(basePath);
            if (!Config.hasResource(base)) continue;
            RandomEntityProperties properties = makeProperties(base, false);
            if (properties == null) properties = makeProperties(base, true);
            if (properties != null) mapProperties.put(basePath, properties);
        }
        return paths;
    }

    @Inject(method = "getLocationIndexed", at = @At("RETURN"), cancellable = true)
    private static void optiRefine$useDigitSeparator(ResourceLocation location, int index,
                                                     CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation legacy = cir.getReturnValue();
        if (legacy == null) return;
        String path = location.getPath();
        int end = path.lastIndexOf('.');
        if (end <= 0 || !optiRefine$isDigit(path.charAt(end - 1))) return;
        ResourceLocation dotted = new ResourceLocation(location.getNamespace(),
                path.substring(0, end) + "." + index + path.substring(end));
        if (!Config.hasResource(dotted)) return;
        var legacyPack = Config.getDefiningResourcePack(legacy);
        if (legacyPack != null) {
            IResourcePack dottedPack = Config.getDefiningResourcePack(dotted);
            if (legacyPack != dottedPack) {
                // The separator is an alternate spelling for the same variant, so follow the existing pack stack.
                IResourcePack[] packs = Config.getResourcePacks();
                for (int i = packs.length - 1; i >= 0; --i) {
                    if (packs[i] == legacyPack) return;
                    if (packs[i] == dottedPack) break;
                }
            }
        }
        cir.setReturnValue(dotted);
    }

    @Unique
    private static boolean optiRefine$isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
