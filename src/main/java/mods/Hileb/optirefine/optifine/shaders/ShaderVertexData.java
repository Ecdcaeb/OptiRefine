package mods.Hileb.optirefine.optifine.shaders;

import java.nio.IntBuffer;

/** Writes the current shader entity to vertex data owned by the receiving buffer. */
public interface ShaderVertexData {
    void optiRefine$writeEntityData(IntBuffer destination, int start, int length);
}
