package mods.Hileb.optirefine.mixin.defaults.optifine;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeInt;
import net.optifine.config.RangeListInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = NbtTagValue.class, remap = false)
public abstract class MixinG5NbtTagValue {
    // Match the mode numbers in G5's matchesValue method.
    @Unique private static final int optiRefine$TEXT = 0;
    @Unique private static final int optiRefine$PATTERN = 1;
    @Unique private static final int optiRefine$IPATTERN = 2;
    @Unique private static final int optiRefine$REGEX = 3;
    @Unique private static final int optiRefine$IREGEX = 4;
    @Unique private static final Pattern optiRefine$RANGE = Pattern.compile(
            "(\\d+|\\(-\\d+\\))(?:-(\\d+|\\(-\\d+\\)))?");

    @Shadow private int type;
    @Shadow private String[] parents;
    @Shadow private String name;
    @Shadow public abstract boolean matchesValue(String value);

    @Shadow
    private static NBTBase getChildTag(NBTBase parent, String name) {
        throw new AssertionError();
    }

    @Unique private boolean optiRefine$raw;
    @Unique private String optiRefine$exists;
    @Unique private RangeListInt optiRefine$ranges;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lorg/apache/commons/lang3/StringEscapeUtils;unescapeJava(Ljava/lang/String;)Ljava/lang/String;"))
    private String optiRefine$readAdditionalPrefixes(String value) {
        // G5 has already handled negation and the four legacy prefixes at this point.
        if (type != optiRefine$TEXT) return value;
        if (value.startsWith("raw:")) {
            optiRefine$raw = true;
            value = value.substring("raw:".length());
        }
        if (value.startsWith("exists:")) {
            optiRefine$exists = value.substring("exists:".length());
        } else if (value.startsWith("range:")) {
            optiRefine$ranges = optiRefine$parseRanges(value.substring("range:".length()));
        } else if (optiRefine$raw) {
            if (value.startsWith("pattern:")) {
                type = optiRefine$PATTERN;
                return value.substring("pattern:".length());
            }
            if (value.startsWith("ipattern:")) {
                type = optiRefine$IPATTERN;
                return value.substring("ipattern:".length()).toLowerCase();
            }
            if (value.startsWith("regex:")) {
                type = optiRefine$REGEX;
                return value.substring("regex:".length());
            }
            if (value.startsWith("iregex:")) {
                type = optiRefine$IREGEX;
                return value.substring("iregex:".length()).toLowerCase();
            }
        }
        return value;
    }

    @Inject(method = "matchesCompound", at = @At("HEAD"), cancellable = true)
    private void optiRefine$matchExistence(NBTTagCompound root, CallbackInfoReturnable<Boolean> cir) {
        if (optiRefine$exists == null) return;
        NBTBase parent = root;
        for (String key : parents) {
            parent = getChildTag(parent, key);
            if (parent == null) break;
        }
        boolean present;
        if (name.equals("*")) {
            present = (parent instanceof NBTTagCompound compound && !compound.getKeySet().isEmpty())
                    || (parent instanceof NBTTagList list && list.tagCount() > 0);
        } else {
            present = getChildTag(parent, name) != null;
        }
        cir.setReturnValue(optiRefine$matchesExistence(present));
    }

    @Inject(method = "matchesBase", at = @At("HEAD"), cancellable = true)
    private void optiRefine$matchRawTag(NBTBase tag, CallbackInfoReturnable<Boolean> cir) {
        if (optiRefine$exists != null) {
            cir.setReturnValue(optiRefine$matchesExistence(tag != null));
        } else if (optiRefine$raw && tag != null) {
            cir.setReturnValue(matchesValue(tag.toString()));
        }
    }

    @Inject(method = "matchesValue", at = @At("HEAD"), cancellable = true)
    private void optiRefine$matchRangeOrExistence(String value, CallbackInfoReturnable<Boolean> cir) {
        if (optiRefine$exists != null) {
            cir.setReturnValue(optiRefine$matchesExistence(value != null));
        } else if (optiRefine$ranges != null && value != null) {
            cir.setReturnValue(optiRefine$matchesRange(value));
        }
    }

    @Unique
    private boolean optiRefine$matchesExistence(boolean present) {
        return (present ? "true" : "false").equals(optiRefine$exists);
    }

    @Unique
    private boolean optiRefine$matchesRange(String value) {
        try {
            return optiRefine$ranges.isInRange(Integer.parseInt(value));
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Unique
    private static RangeListInt optiRefine$parseRanges(String value) {
        RangeListInt ranges = new RangeListInt();
        for (String token : value.trim().split("[\\s,]+")) {
            Matcher matcher = optiRefine$RANGE.matcher(token);
            if (!matcher.matches()) return new RangeListInt();
            try {
                int first = optiRefine$parseEndpoint(matcher.group(1));
                String last = matcher.group(2);
                ranges.addRange(new RangeInt(first, last == null ? first : optiRefine$parseEndpoint(last)));
            } catch (NumberFormatException ignored) {
                return new RangeListInt();
            }
        }
        return ranges;
    }

    @Unique
    private static int optiRefine$parseEndpoint(String endpoint) {
        return Integer.parseInt(endpoint.charAt(0) == '('
                ? endpoint.substring(1, endpoint.length() - 1) : endpoint);
    }
}
