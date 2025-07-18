package net.minecraft.block;

import net.minecraft.util.math.BlockPos;

public class BlockEventData {
   private final BlockPos position;
   private final Block blockType;
   private final int eventID;
   private final int eventParameter;

   public BlockEventData(BlockPos var1, Block var2, int var3, int var4) {
      this.position = ☃;
      this.eventID = ☃;
      this.eventParameter = ☃;
      this.blockType = ☃;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public int getEventID() {
      return this.eventID;
   }

   public int getEventParameter() {
      return this.eventParameter;
   }

   public Block getBlock() {
      return this.blockType;
   }

   @Override
   public boolean equals(Object var1) {
      if (!(☃ instanceof BlockEventData)) {
         return false;
      } else {
         BlockEventData ☃ = (BlockEventData)☃;
         return this.position.equals(☃.position) && this.eventID == ☃.eventID && this.eventParameter == ☃.eventParameter && this.blockType == ☃.blockType;
      }
   }

   @Override
   public String toString() {
      return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
   }
}
