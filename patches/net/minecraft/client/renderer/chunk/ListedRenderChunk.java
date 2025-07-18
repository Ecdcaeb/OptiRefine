package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;

public class ListedRenderChunk extends RenderChunk {
   private final int baseDisplayList = GLAllocation.generateDisplayLists(BlockRenderLayer.values().length);

   public ListedRenderChunk(World var1, RenderGlobal var2, int var3) {
      super(☃, ☃, ☃);
   }

   public int getDisplayList(BlockRenderLayer var1, CompiledChunk var2) {
      return !☃.isLayerEmpty(☃) ? this.baseDisplayList + ☃.ordinal() : -1;
   }

   @Override
   public void deleteGlResources() {
      super.deleteGlResources();
      GLAllocation.deleteDisplayLists(this.baseDisplayList, BlockRenderLayer.values().length);
   }
}
