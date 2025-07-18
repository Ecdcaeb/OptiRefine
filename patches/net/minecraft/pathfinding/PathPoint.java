package net.minecraft.pathfinding;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;

public class PathPoint {
   public final int x;
   public final int y;
   public final int z;
   private final int hash;
   public int index = -1;
   public float totalPathDistance;
   public float distanceToNext;
   public float distanceToTarget;
   public PathPoint previous;
   public boolean visited;
   public float distanceFromOrigin;
   public float cost;
   public float costMalus;
   public PathNodeType nodeType = PathNodeType.BLOCKED;

   public PathPoint(int var1, int var2, int var3) {
      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
      this.hash = makeHash(☃, ☃, ☃);
   }

   public PathPoint cloneMove(int var1, int var2, int var3) {
      PathPoint ☃ = new PathPoint(☃, ☃, ☃);
      ☃.index = this.index;
      ☃.totalPathDistance = this.totalPathDistance;
      ☃.distanceToNext = this.distanceToNext;
      ☃.distanceToTarget = this.distanceToTarget;
      ☃.previous = this.previous;
      ☃.visited = this.visited;
      ☃.distanceFromOrigin = this.distanceFromOrigin;
      ☃.cost = this.cost;
      ☃.costMalus = this.costMalus;
      ☃.nodeType = this.nodeType;
      return ☃;
   }

   public static int makeHash(int var0, int var1, int var2) {
      return ☃ & 0xFF | (☃ & 32767) << 8 | (☃ & 32767) << 24 | (☃ < 0 ? Integer.MIN_VALUE : 0) | (☃ < 0 ? 32768 : 0);
   }

   public float distanceTo(PathPoint var1) {
      float ☃ = ☃.x - this.x;
      float ☃x = ☃.y - this.y;
      float ☃xx = ☃.z - this.z;
      return MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
   }

   public float distanceToSquared(PathPoint var1) {
      float ☃ = ☃.x - this.x;
      float ☃x = ☃.y - this.y;
      float ☃xx = ☃.z - this.z;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public float distanceManhattan(PathPoint var1) {
      float ☃ = Math.abs(☃.x - this.x);
      float ☃x = Math.abs(☃.y - this.y);
      float ☃xx = Math.abs(☃.z - this.z);
      return ☃ + ☃x + ☃xx;
   }

   @Override
   public boolean equals(Object var1) {
      if (!(☃ instanceof PathPoint)) {
         return false;
      } else {
         PathPoint ☃ = (PathPoint)☃;
         return this.hash == ☃.hash && this.x == ☃.x && this.y == ☃.y && this.z == ☃.z;
      }
   }

   @Override
   public int hashCode() {
      return this.hash;
   }

   public boolean isAssigned() {
      return this.index >= 0;
   }

   @Override
   public String toString() {
      return this.x + ", " + this.y + ", " + this.z;
   }

   public static PathPoint createFromBuffer(PacketBuffer var0) {
      PathPoint ☃ = new PathPoint(☃.readInt(), ☃.readInt(), ☃.readInt());
      ☃.distanceFromOrigin = ☃.readFloat();
      ☃.cost = ☃.readFloat();
      ☃.costMalus = ☃.readFloat();
      ☃.visited = ☃.readBoolean();
      ☃.nodeType = PathNodeType.values()[☃.readInt()];
      ☃.distanceToTarget = ☃.readFloat();
      return ☃;
   }
}
