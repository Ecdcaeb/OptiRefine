package mods.Hileb.optirefine.mixin.defaults.minecraft.network.datasync;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.init.Biomes;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityDataManager.class)
public abstract class MixinEntityDataManager {
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    public Biome spawnBiome = Biomes.PLAINS;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    public BlockPos spawnPosition = BlockPos.ORIGIN;
}
/*
--- net/minecraft/network/datasync/EntityDataManager.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/network/datasync/EntityDataManager.java	Tue Aug 19 14:59:58 2025
@@ -11,26 +11,31 @@
 import java.util.concurrent.locks.ReadWriteLock;
 import java.util.concurrent.locks.ReentrantReadWriteLock;
 import javax.annotation.Nullable;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.CrashReportCategory;
 import net.minecraft.entity.Entity;
+import net.minecraft.init.Biomes;
 import net.minecraft.network.PacketBuffer;
 import net.minecraft.util.ReportedException;
+import net.minecraft.util.math.BlockPos;
+import net.minecraft.world.biome.Biome;
 import org.apache.commons.lang3.ObjectUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class EntityDataManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<Class<? extends Entity>, Integer> NEXT_ID_MAP = Maps.newHashMap();
    private final Entity entity;
    private final Map<Integer, EntityDataManager.DataEntry<?>> entries = Maps.newHashMap();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean empty = true;
    private boolean dirty;
+   public Biome spawnBiome = Biomes.PLAINS;
+   public BlockPos spawnPosition = BlockPos.ORIGIN;

    public EntityDataManager(Entity var1) {
       this.entity = var1;
    }

    public static <T> DataParameter<T> createKey(Class<? extends Entity> var0, DataSerializer<T> var1) {
@@ -293,10 +298,10 @@

       public void setDirty(boolean var1) {
          this.dirty = var1;
       }

       public EntityDataManager.DataEntry<T> copy() {
-         return new EntityDataManager.DataEntry<>(this.key, this.key.getSerializer().copyValue(this.value));
+         return new EntityDataManager.DataEntry<>(this.key, (T)this.key.getSerializer().copyValue(this.value));
       }
    }
 }
 */
