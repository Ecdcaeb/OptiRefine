package mods.Hileb.optirefine.mixin.defaults.minecraftforge.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import javax.vecmath.Vector3f;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Forge's {@code fillNormal} / {@code getVertexPos} hard-code the vanilla ITEM stride of 7 ints.
 * FaceBakery widens baked quads to 56 ints under shaders; writing {@code i * 7 + 6} then lands in
 * the next vertex's lightmap (and for vertex 2/3, into the following vertex's position). Two of
 * four corners become NaN/zero — the shader black-triangle artifact.
 *
 * OptiFine's own class transformer used to rewrite this; OptiRefine disables that transformer.
 */
@Mixin(ForgeHooksClient.class)
public abstract class MixinForgeHooksClient {

    @WrapMethod(method = "fillNormal")
    private static void optiRefine$fillNormal(int[] faceData, EnumFacing facing, Operation<Void> original) {
        int stride = faceData.length / 4;
        if (stride == 7) {
            original.call(faceData, facing);
            return;
        }
        Vector3f v1 = optiRefine$getVertexPos(faceData, 3, stride);
        Vector3f t = optiRefine$getVertexPos(faceData, 1, stride);
        Vector3f v2 = optiRefine$getVertexPos(faceData, 2, stride);
        v1.sub(t);
        t.set(optiRefine$getVertexPos(faceData, 0, stride));
        v2.sub(t);
        v1.cross(v2, v1);
        v1.normalize();
        int x = ((byte) Math.round(v1.x * 127)) & 0xFF;
        int y = ((byte) Math.round(v1.y * 127)) & 0xFF;
        int z = ((byte) Math.round(v1.z * 127)) & 0xFF;
        int normal = x | (y << 0x08) | (z << 0x10);
        for (int i = 0; i < 4; i++) {
            faceData[i * stride + 6] = normal;
        }
    }

    @Unique
    private static Vector3f optiRefine$getVertexPos(int[] data, int vertex, int stride) {
        int idx = vertex * stride;
        return new Vector3f(
                Float.intBitsToFloat(data[idx]),
                Float.intBitsToFloat(data[idx + 1]),
                Float.intBitsToFloat(data[idx + 2]));
    }
}
