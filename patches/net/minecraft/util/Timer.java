package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer {
   public int elapsedTicks;
   public float renderPartialTicks;
   public float elapsedPartialTicks;
   private long lastSyncSysClock;
   private float tickLength;

   public Timer(float var1) {
      this.tickLength = 1000.0F / ☃;
      this.lastSyncSysClock = Minecraft.getSystemTime();
   }

   public void updateTimer() {
      long ☃ = Minecraft.getSystemTime();
      this.elapsedPartialTicks = (float)(☃ - this.lastSyncSysClock) / this.tickLength;
      this.lastSyncSysClock = ☃;
      this.renderPartialTicks = this.renderPartialTicks + this.elapsedPartialTicks;
      this.elapsedTicks = (int)this.renderPartialTicks;
      this.renderPartialTicks = this.renderPartialTicks - this.elapsedTicks;
   }
}
