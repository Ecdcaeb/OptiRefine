package net.minecraft.tileentity;

import net.minecraft.util.EnumFacing;

public class TileEntityEndPortal extends TileEntity {
   public boolean shouldRenderFace(EnumFacing var1) {
      return ☃ == EnumFacing.UP;
   }
}
