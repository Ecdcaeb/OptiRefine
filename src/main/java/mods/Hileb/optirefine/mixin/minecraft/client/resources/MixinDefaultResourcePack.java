package mods.Hileb.optirefine.mixin.minecraft.client.resources;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.OptifineHelper;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.io.InputStream;

@Mixin(DefaultResourcePack.class)
public abstract class MixinDefaultResourcePack {
    @Unique
    private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;


    @WrapMethod(method = "getResourceStream")
    public InputStream injectGetResourceStream(ResourceLocation location, Operation<InputStream> original){
        InputStream is = OptifineHelper.getOptifineResource("/assets/" + location.getNamespace() + "/" + location.getPath());
        if (is == null) return original.call(location);
        else return is;
    }


}
