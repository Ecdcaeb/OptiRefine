package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerDemo extends WorldServer {
   private static final long DEMO_WORLD_SEED = "North Carolina".hashCode();
   public static final WorldSettings DEMO_WORLD_SETTINGS = new WorldSettings(DEMO_WORLD_SEED, GameType.SURVIVAL, true, false, WorldType.DEFAULT)
      .enableBonusChest();

   public WorldServerDemo(MinecraftServer var1, ISaveHandler var2, WorldInfo var3, int var4, Profiler var5) {
      super(☃, ☃, ☃, ☃, ☃);
      this.worldInfo.populateFromWorldSettings(DEMO_WORLD_SETTINGS);
   }
}
