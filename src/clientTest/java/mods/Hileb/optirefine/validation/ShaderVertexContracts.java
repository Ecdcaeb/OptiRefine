package mods.Hileb.optirefine.validation;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.shaders.SVertexFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.Arrays;

final class ShaderVertexContracts {
    private ShaderVertexContracts() {}

    static void verify() throws ReflectiveOperationException {
        VertexFormat format = SVertexFormat.makeDefVertexFormatBlock();
        int stride = format.getIntegerSize();
        int[] quad = new int[stride * 4];
        for (int vertex = 0; vertex < 4; vertex++) {
            quad[vertex * stride + 12] = -1;
            quad[vertex * stride + 13] = -1;
        }
        int[] source = quad.clone();
        ByteBuffer bytes = BufferUtils.createByteBuffer(quad.length * Integer.BYTES);
        bytes.asIntBuffer().put(quad);
        ByteBuffer readOnly = bytes.asReadOnlyBuffer();
        BufferBuilder buffer = new BufferBuilder(16);
        SVertexBuilder vertexBuilder = (SVertexBuilder)BufferBuilder.class.getField("sVertexBuilder").get(buffer);
        buffer.begin(GL11.GL_QUADS, format);
        long firstTag = 0x13579BDF2468ACE0L;
        long secondTag = 0x2468ACE013579BDFL;
        vertexBuilder.pushEntity(firstTag);
        try {
            buffer.addVertexData(quad);
        } finally {
            vertexBuilder.popEntity();
        }
        vertexBuilder.pushEntity(secondTag);
        try {
            buffer.putBulkData(readOnly);
        } finally {
            vertexBuilder.popEntity();
        }
        buffer.finishDrawing();
        for (int i = 0; i < quad.length; i++) {
            if (bytes.asIntBuffer().get(i) != source[i]) throw new AssertionError("Bulk source vertex data was modified");
        }
        if (!Arrays.equals(quad, source)) throw new AssertionError("Baked vertex data was modified");
        for (int vertex = 0; vertex < 8; vertex++) {
            long tag = vertex < 4 ? firstTag : secondTag;
            int entityOffset = (vertex * stride + 12) * Integer.BYTES;
            if (buffer.getByteBuffer().getInt(entityOffset) != (int)tag
                    || buffer.getByteBuffer().getInt(entityOffset + Integer.BYTES) != (int)(tag >>> 32)) {
                throw new AssertionError("Shader entity tag missing or applied to the wrong quad at vertex " + vertex);
            }
        }
    }
}
