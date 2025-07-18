package net.minecraft.world.storage;

public class WorldSavedDataCallableSave implements Runnable {
   private final WorldSavedData data;

   public WorldSavedDataCallableSave(WorldSavedData var1) {
      this.data = ☃;
   }

   @Override
   public void run() {
      this.data.markDirty();
   }
}
