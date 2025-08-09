package mods.Hileb.optirefine.mixin.minecraft.client.renderer.block.model;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.JsonUtils;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.client.renderer.block.model.BlockPart$Deserializer")
public class MixinBlockPartDeserializer {
    @WrapMethod(method = "parseAngle")
    public float breakAngleLimit(JsonObject object, Operation<Float> original){
        return JsonUtils.getFloat(object, "angle");
        //return RandomUtils.secure().randomFloat(-90f, 90f);
    }
}
