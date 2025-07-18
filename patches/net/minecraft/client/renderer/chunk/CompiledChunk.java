package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

public class CompiledChunk {
   public static final CompiledChunk DUMMY = new CompiledChunk() {
      @Override
      protected void setLayerUsed(BlockRenderLayer var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setLayerStarted(BlockRenderLayer var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean isVisible(EnumFacing var1, EnumFacing var2) {
         return false;
      }
   };
   private final boolean[] layersUsed = new boolean[BlockRenderLayer.values().length];
   private final boolean[] layersStarted = new boolean[BlockRenderLayer.values().length];
   private boolean empty = true;
   private final List<TileEntity> tileEntities = Lists.newArrayList();
   private SetVisibility setVisibility = new SetVisibility();
   private BufferBuilder.State state;

   public boolean isEmpty() {
      return this.empty;
   }

   protected void setLayerUsed(BlockRenderLayer var1) {
      this.empty = false;
      this.layersUsed[☃.ordinal()] = true;
   }

   public boolean isLayerEmpty(BlockRenderLayer var1) {
      return !this.layersUsed[☃.ordinal()];
   }

   public void setLayerStarted(BlockRenderLayer var1) {
      this.layersStarted[☃.ordinal()] = true;
   }

   public boolean isLayerStarted(BlockRenderLayer var1) {
      return this.layersStarted[☃.ordinal()];
   }

   public List<TileEntity> getTileEntities() {
      return this.tileEntities;
   }

   public void addTileEntity(TileEntity var1) {
      this.tileEntities.add(☃);
   }

   public boolean isVisible(EnumFacing var1, EnumFacing var2) {
      return this.setVisibility.isVisible(☃, ☃);
   }

   public void setVisibility(SetVisibility var1) {
      this.setVisibility = ☃;
   }

   public BufferBuilder.State getState() {
      return this.state;
   }

   public void setState(BufferBuilder.State var1) {
      this.state = ☃;
   }
}
