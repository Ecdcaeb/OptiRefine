package mods.Hileb.optirefine.mixin.defaults.minecraftforge.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;

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
        int second = stride * 2;
        int third = stride * 3;
        float firstX = Float.intBitsToFloat(faceData[third]) - Float.intBitsToFloat(faceData[stride]);
        float firstY = Float.intBitsToFloat(faceData[third + 1]) - Float.intBitsToFloat(faceData[stride + 1]);
        float firstZ = Float.intBitsToFloat(faceData[third + 2]) - Float.intBitsToFloat(faceData[stride + 2]);
        float secondX = Float.intBitsToFloat(faceData[second]) - Float.intBitsToFloat(faceData[0]);
        float secondY = Float.intBitsToFloat(faceData[second + 1]) - Float.intBitsToFloat(faceData[1]);
        float secondZ = Float.intBitsToFloat(faceData[second + 2]) - Float.intBitsToFloat(faceData[2]);
        float normalX = secondY * firstZ - secondZ * firstY;
        float normalY = firstX * secondZ - firstZ * secondX;
        float normalZ = secondX * firstY - secondY * firstX;
        // Vecmath normalizes by a double reciprocal cast to float, then multiplies.
        float inverseLength = (float) (1.0 / Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ));
        int x = ((byte) Math.round(normalX * inverseLength * 127)) & 0xFF;
        int y = ((byte) Math.round(normalY * inverseLength * 127)) & 0xFF;
        int z = ((byte) Math.round(normalZ * inverseLength * 127)) & 0xFF;
        int normal = x | (y << 0x08) | (z << 0x10);
        for (int i = 0; i < 4; i++) {
            faceData[i * stride + 6] = normal;
        }
    }

}
