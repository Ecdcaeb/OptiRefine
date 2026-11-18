package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.optifine.CustomItemProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Properties;

@Mixin(value = CustomItemProperties.class, remap = false)
public abstract class MixinG5CustomItemProperties {
    // Both keys use G5's existing parser for registry names and numeric ranges.
    @Redirect(method = "<init>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=enchantmentIDs")),
            at = @At(value = "INVOKE", target = "Ljava/util/Properties;getProperty(Ljava/lang/String;)Ljava/lang/String;", ordinal = 0))
    private String optiRefine$readEnchantmentSelector(Properties properties, String key) {
        String enchantments = properties.getProperty("enchantments");
        return enchantments != null ? enchantments : properties.getProperty(key);
    }
}
