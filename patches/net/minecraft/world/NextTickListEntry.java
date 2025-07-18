package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class NextTickListEntry implements Comparable<NextTickListEntry> {
   private static long nextTickEntryID;
   private final Block block;
   public final BlockPos position;
   public long scheduledTime;
   public int priority;
   private final long tickEntryID;

   public NextTickListEntry(BlockPos var1, Block var2) {
      this.tickEntryID = nextTickEntryID++;
      this.position = ☃.toImmutable();
      this.block = ☃;
   }

   @Override
   public boolean equals(Object var1) {
      if (!(☃ instanceof NextTickListEntry)) {
         return false;
      } else {
         NextTickListEntry ☃ = (NextTickListEntry)☃;
         return this.position.equals(☃.position) && Block.isEqualTo(this.block, ☃.block);
      }
   }

   @Override
   public int hashCode() {
      return this.position.hashCode();
   }

   public NextTickListEntry setScheduledTime(long var1) {
      this.scheduledTime = ☃;
      return this;
   }

   public void setPriority(int var1) {
      this.priority = ☃;
   }

   public int compareTo(NextTickListEntry var1) {
      if (this.scheduledTime < ☃.scheduledTime) {
         return -1;
      } else if (this.scheduledTime > ☃.scheduledTime) {
         return 1;
      } else if (this.priority != ☃.priority) {
         return this.priority - ☃.priority;
      } else if (this.tickEntryID < ☃.tickEntryID) {
         return -1;
      } else {
         return this.tickEntryID > ☃.tickEntryID ? 1 : 0;
      }
   }

   @Override
   public String toString() {
      return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
   }

   public Block getBlock() {
      return this.block;
   }
}
