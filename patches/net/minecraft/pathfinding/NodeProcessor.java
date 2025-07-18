package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor {
   protected IBlockAccess blockaccess;
   protected EntityLiving entity;
   protected final IntHashMap<PathPoint> pointMap = new IntHashMap<>();
   protected int entitySizeX;
   protected int entitySizeY;
   protected int entitySizeZ;
   protected boolean canEnterDoors;
   protected boolean canOpenDoors;
   protected boolean canSwim;

   public void init(IBlockAccess var1, EntityLiving var2) {
      this.blockaccess = ☃;
      this.entity = ☃;
      this.pointMap.clearMap();
      this.entitySizeX = MathHelper.floor(☃.width + 1.0F);
      this.entitySizeY = MathHelper.floor(☃.height + 1.0F);
      this.entitySizeZ = MathHelper.floor(☃.width + 1.0F);
   }

   public void postProcess() {
      this.blockaccess = null;
      this.entity = null;
   }

   protected PathPoint openPoint(int var1, int var2, int var3) {
      int ☃ = PathPoint.makeHash(☃, ☃, ☃);
      PathPoint ☃x = this.pointMap.lookup(☃);
      if (☃x == null) {
         ☃x = new PathPoint(☃, ☃, ☃);
         this.pointMap.addKey(☃, ☃x);
      }

      return ☃x;
   }

   public abstract PathPoint getStart();

   public abstract PathPoint getPathPointToCoords(double var1, double var3, double var5);

   public abstract int findPathOptions(PathPoint[] var1, PathPoint var2, PathPoint var3, float var4);

   public abstract PathNodeType getPathNodeType(
      IBlockAccess var1, int var2, int var3, int var4, EntityLiving var5, int var6, int var7, int var8, boolean var9, boolean var10
   );

   public abstract PathNodeType getPathNodeType(IBlockAccess var1, int var2, int var3, int var4);

   public void setCanEnterDoors(boolean var1) {
      this.canEnterDoors = ☃;
   }

   public void setCanOpenDoors(boolean var1) {
      this.canOpenDoors = ☃;
   }

   public void setCanSwim(boolean var1) {
      this.canSwim = ☃;
   }

   public boolean getCanEnterDoors() {
      return this.canEnterDoors;
   }

   public boolean getCanOpenDoors() {
      return this.canOpenDoors;
   }

   public boolean getCanSwim() {
      return this.canSwim;
   }
}
