package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public interface IRenderChunkFactory {
   RenderChunk create(World var1, RenderGlobal var2, int var3);
}
