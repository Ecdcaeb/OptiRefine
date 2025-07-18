package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerMulti extends WorldServer {
   private final WorldServer delegate;

   public WorldServerMulti(MinecraftServer var1, ISaveHandler var2, int var3, WorldServer var4, Profiler var5) {
      super(☃, ☃, new DerivedWorldInfo(☃.getWorldInfo()), ☃, ☃);
      this.delegate = ☃;
      ☃.getWorldBorder().addListener(new IBorderListener() {
         @Override
         public void onSizeChanged(WorldBorder var1, double var2x) {
            WorldServerMulti.this.getWorldBorder().setTransition(var2x);
         }

         @Override
         public void onTransitionStarted(WorldBorder var1, double var2x, double var4x, long var6) {
            WorldServerMulti.this.getWorldBorder().setTransition(var2x, var4x, ☃);
         }

         @Override
         public void onCenterChanged(WorldBorder var1, double var2x, double var4x) {
            WorldServerMulti.this.getWorldBorder().setCenter(var2x, var4x);
         }

         @Override
         public void onWarningTimeChanged(WorldBorder var1, int var2x) {
            WorldServerMulti.this.getWorldBorder().setWarningTime(var2x);
         }

         @Override
         public void onWarningDistanceChanged(WorldBorder var1, int var2x) {
            WorldServerMulti.this.getWorldBorder().setWarningDistance(var2x);
         }

         @Override
         public void onDamageAmountChanged(WorldBorder var1, double var2x) {
            WorldServerMulti.this.getWorldBorder().setDamageAmount(var2x);
         }

         @Override
         public void onDamageBufferChanged(WorldBorder var1, double var2x) {
            WorldServerMulti.this.getWorldBorder().setDamageBuffer(var2x);
         }
      });
   }

   @Override
   protected void saveLevel() {
   }

   @Override
   public World init() {
      this.mapStorage = this.delegate.getMapStorage();
      this.worldScoreboard = this.delegate.getScoreboard();
      this.lootTable = this.delegate.getLootTableManager();
      this.advancementManager = this.delegate.getAdvancementManager();
      String ☃ = VillageCollection.fileNameForProvider(this.provider);
      VillageCollection ☃x = (VillageCollection)this.mapStorage.getOrLoadData(VillageCollection.class, ☃);
      if (☃x == null) {
         this.villageCollection = new VillageCollection(this);
         this.mapStorage.setData(☃, this.villageCollection);
      } else {
         this.villageCollection = ☃x;
         this.villageCollection.setWorldsForAll(this);
      }

      return this;
   }

   public void saveAdditionalData() {
      this.provider.onWorldSave();
   }
}
