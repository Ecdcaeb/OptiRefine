package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;


import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
@Mixin(targets = "net.minecraft.client.renderer.block.model.BlockPart$Deserializer")
public class MixinBlockPartDeserializer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1
    @WrapMethod(method = "parseAngle")
    // [AUDIT-FIXED] restore +/-45/22.5 validation present in both OF and vanilla
    public float breakAngleLimit(JsonObject object, Operation<Float> original){
        float f = JsonUtils.getFloat(object, "angle");
        if (f != 0.0F && MathHelper.abs(f) != 45.0F && MathHelper.abs(f) != 22.5F) {
            throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
        }
        return f;
    }
}
