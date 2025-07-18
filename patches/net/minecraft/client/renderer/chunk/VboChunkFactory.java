package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class VboChunkFactory implements IRenderChunkFactory {
   @Override
   public RenderChunk create(World var1, RenderGlobal var2, int var3) {
      return new RenderChunk(☃, ☃, ☃);
   }
}
