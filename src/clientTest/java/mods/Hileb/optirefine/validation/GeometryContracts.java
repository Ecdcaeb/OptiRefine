package mods.Hileb.optirefine.validation;

import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;
import java.util.Random;

final class GeometryContracts {
    private GeometryContracts() {}

    static void verify() {
        Random random = new Random(1337);
        for (int stride : new int[]{7, 14}) {
            for (int sample = 0; sample < 4096; sample++) {
                int[] data = new int[stride * 4];
                for (int i = 0; i < data.length; i++) data[i] = random.nextInt();
                for (int vertex = 0; vertex < 4; vertex++) {
                    for (int axis = 0; axis < 3; axis++) {
                        data[vertex * stride + axis] = Float.floatToRawIntBits(random.nextFloat() * 32.0F - 16.0F);
                    }
                }
                verify(data, stride);
            }
            // Winding ties, degenerate quads, signed zero, overflow and non-finite inputs.
            for (float[] positions : new float[][]{
                    {0,0,0, 1,0,0, 1,1,0, 0,1,0},
                    {0,1,0, 1,1,-1, 1,0,-1, 0,0,0},
                    {0,0,0, 0,0,0, 0,0,0, 0,0,0},
                    {-0.0F,0,0, 0,-0.0F,0, 0,0,-0.0F, 0,0,0},
                    {0,0,0, Float.MAX_VALUE,0,0, 0,Float.MAX_VALUE,0, 0,0,0},
                    {0,0,0, Float.MIN_VALUE,0,0, 0,Float.MIN_VALUE,0, 0,0,0},
                    {Float.NaN,0,0, 1,0,0, 0,1,0, 1,1,0},
                    {Float.POSITIVE_INFINITY,0,0, 1,0,0, 0,1,0, 1,1,0}
            }) {
                int[] data = new int[stride * 4];
                for (int i = 0; i < positions.length; i++) data[(i / 3) * stride + i % 3] = Float.floatToRawIntBits(positions[i]);
                verify(data, stride);
            }
        }
    }

    private static void verify(int[] data, int stride) {
        int[] original = data.clone();
        EnumFacing expectedFacing = facing(data, stride);
        if (FaceBakery.getFacingFromVertexData(data) != expectedFacing || !Arrays.equals(data, original)) {
            throw new AssertionError("Baked face orientation or source vertices changed at stride " + stride);
        }
        int[] expected = data.clone();
        int normal = normal(data, stride);
        for (int vertex = 0; vertex < 4; vertex++) expected[vertex * stride + 6] = normal;
        ForgeHooksClient.fillNormal(data, expectedFacing);
        if (!Arrays.equals(data, expected)) throw new AssertionError("Packed quad normal or unrelated vertex data changed at stride " + stride);
    }

    private static EnumFacing facing(int[] data, int stride) {
        Vector3f first = vertex(data, 0, stride), origin = vertex(data, 1, stride), second = vertex(data, 2, stride);
        Vector3f left = Vector3f.sub(first, origin, null), right = Vector3f.sub(second, origin, null);
        Vector3f normal = Vector3f.cross(right, left, null);
        float length = (float)Math.sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z);
        normal.x /= length; normal.y /= length; normal.z /= length;
        EnumFacing result = EnumFacing.UP;
        float best = 0.0F;
        for (EnumFacing facing : EnumFacing.values()) {
            float dot = Vector3f.dot(normal, new Vector3f(facing.getXOffset(), facing.getYOffset(), facing.getZOffset()));
            if (dot >= 0.0F && dot > best) { best = dot; result = facing; }
        }
        return result;
    }

    private static int normal(int[] data, int stride) {
        javax.vecmath.Vector3f first = forgeVertex(data, 3, stride), origin = forgeVertex(data, 1, stride), second = forgeVertex(data, 2, stride);
        first.sub(origin);
        origin.set(forgeVertex(data, 0, stride));
        second.sub(origin);
        first.cross(second, first);
        first.normalize();
        return ((byte)Math.round(first.x * 127) & 255)
                | (((byte)Math.round(first.y * 127) & 255) << 8)
                | (((byte)Math.round(first.z * 127) & 255) << 16);
    }

    private static Vector3f vertex(int[] data, int vertex, int stride) {
        int index = vertex * stride;
        return new Vector3f(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]), Float.intBitsToFloat(data[index + 2]));
    }

    private static javax.vecmath.Vector3f forgeVertex(int[] data, int vertex, int stride) {
        int index = vertex * stride;
        return new javax.vecmath.Vector3f(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]), Float.intBitsToFloat(data[index + 2]));
    }
}
