package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.cem.CemBoxGeometry;
import mods.Hileb.optirefine.optifine.cem.CemHandAttachments;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.Vec3d;
import net.optifine.util.Json;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.optifine.player.PlayerItemParser", remap = false)
public abstract class MixinCemPartParser {
    @WrapOperation(method = "parseModelRenderer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/model/ModelRenderer;addBox([[IFFFFFFF)V", remap = false))
    private static void optiRefine$inflateFaceUvBox(ModelRenderer part, int[][] uvs, float x, float y, float z,
                                                   float width, float height, float depth, float delta, Operation<Void> original,
                                                   @Local(index = 20) JsonObject box) {
        original.call(part, uvs, x, y, z, width, height, depth, delta);
        optiRefine$inflateLastBox(part, box);
    }

    @WrapOperation(method = "parseModelRenderer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/model/ModelRenderer;addBox(FFFIIIF)V", remap = true))
    private static void optiRefine$inflateTextureOffsetBox(ModelRenderer part, float x, float y, float z,
                                                          int width, int height, int depth, float delta, Operation<Void> original,
                                                          @Local(index = 20) JsonObject box) {
        original.call(part, x, y, z, width, height, depth, delta);
        optiRefine$inflateLastBox(part, box);
    }

    @Unique
    private static void optiRefine$inflateLastBox(ModelRenderer part, JsonObject box) {
        float[] sizes = Json.parseFloatArray(box.get("sizesAdd"), 3);
        if (sizes != null) {
            CemBoxGeometry geometry = (CemBoxGeometry) part.cubeList.getLast();
            geometry.optiRefine$inflate(sizes[0], sizes[1], sizes[2], part.mirror);
        }
    }

    @Inject(method = "parseModelRenderer", at = @At("RETURN"))
    private static void optiRefine$readHandAttachments(JsonObject part, ModelBase model, int[] textureSize, String basePath,
                                                       CallbackInfoReturnable<ModelRenderer> cir,
                                                       @Local(index = 10) boolean invertX, @Local(index = 11) boolean invertY,
                                                       @Local(index = 12) boolean invertZ) {
        JsonObject attachments = part.getAsJsonObject("attachments");
        if (attachments == null) {
            return;
        }
        Vec3d left = optiRefine$readAnchor(attachments, "left_handheld_item", invertX, invertY, invertZ);
        Vec3d right = optiRefine$readAnchor(attachments, "right_handheld_item", invertX, invertY, invertZ);
        if (left != null || right != null) {
            ((CemHandAttachments.Part) cir.getReturnValue()).optiRefine$setHandAttachments(new CemHandAttachments(left, right));
        }
    }

    @Unique
    private static Vec3d optiRefine$readAnchor(JsonObject attachments, String name, boolean invertX, boolean invertY, boolean invertZ) {
        float[] offset = Json.parseFloatArray(attachments.get(name), 3);
        return offset == null ? null : new Vec3d(invertX ? -offset[0] : offset[0],
                invertY ? -offset[1] : offset[1], invertZ ? -offset[2] : offset[2]);
    }
}
