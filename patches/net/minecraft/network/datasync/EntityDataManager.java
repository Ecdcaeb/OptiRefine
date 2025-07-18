package net.minecraft.network.datasync;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ReportedException;
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

   public EntityDataManager(Entity var1) {
      this.entity = ☃;
   }

   public static <T> DataParameter<T> createKey(Class<? extends Entity> var0, DataSerializer<T> var1) {
      if (LOGGER.isDebugEnabled()) {
         try {
            Class<?> ☃ = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
            if (!☃.equals(☃)) {
               LOGGER.debug("defineId called for: {} from {}", ☃, ☃, new RuntimeException());
            }
         } catch (ClassNotFoundException var5) {
         }
      }

      int ☃;
      if (NEXT_ID_MAP.containsKey(☃)) {
         ☃ = NEXT_ID_MAP.get(☃) + 1;
      } else {
         int ☃x = 0;
         Class<?> ☃xx = ☃;

         while (☃xx != Entity.class) {
            ☃xx = ☃xx.getSuperclass();
            if (NEXT_ID_MAP.containsKey(☃xx)) {
               ☃x = NEXT_ID_MAP.get(☃xx) + 1;
               break;
            }
         }

         ☃ = ☃x;
      }

      if (☃ > 254) {
         throw new IllegalArgumentException("Data value id is too big with " + ☃ + "! (Max is " + 254 + ")");
      } else {
         NEXT_ID_MAP.put(☃, ☃);
         return ☃.createKey(☃);
      }
   }

   public <T> void register(DataParameter<T> var1, T var2) {
      int ☃ = ☃.getId();
      if (☃ > 254) {
         throw new IllegalArgumentException("Data value id is too big with " + ☃ + "! (Max is " + 254 + ")");
      } else if (this.entries.containsKey(☃)) {
         throw new IllegalArgumentException("Duplicate id value for " + ☃ + "!");
      } else if (DataSerializers.getSerializerId(☃.getSerializer()) < 0) {
         throw new IllegalArgumentException("Unregistered serializer " + ☃.getSerializer() + " for " + ☃ + "!");
      } else {
         this.setEntry(☃, ☃);
      }
   }

   private <T> void setEntry(DataParameter<T> var1, T var2) {
      EntityDataManager.DataEntry<T> ☃ = new EntityDataManager.DataEntry<>(☃, ☃);
      this.lock.writeLock().lock();
      this.entries.put(☃.getId(), ☃);
      this.empty = false;
      this.lock.writeLock().unlock();
   }

   private <T> EntityDataManager.DataEntry<T> getEntry(DataParameter<T> var1) {
      this.lock.readLock().lock();

      EntityDataManager.DataEntry<T> ☃;
      try {
         ☃ = (EntityDataManager.DataEntry<T>)this.entries.get(☃.getId());
      } catch (Throwable var6) {
         CrashReport ☃x = CrashReport.makeCrashReport(var6, "Getting synched entity data");
         CrashReportCategory ☃xx = ☃x.makeCategory("Synched entity data");
         ☃xx.addCrashSection("Data ID", ☃);
         throw new ReportedException(☃x);
      }

      this.lock.readLock().unlock();
      return ☃;
   }

   public <T> T get(DataParameter<T> var1) {
      return this.getEntry(☃).getValue();
   }

   public <T> void set(DataParameter<T> var1, T var2) {
      EntityDataManager.DataEntry<T> ☃ = this.getEntry(☃);
      if (ObjectUtils.notEqual(☃, ☃.getValue())) {
         ☃.setValue(☃);
         this.entity.notifyDataManagerChange(☃);
         ☃.setDirty(true);
         this.dirty = true;
      }
   }

   public <T> void setDirty(DataParameter<T> var1) {
      this.getEntry(☃).dirty = true;
      this.dirty = true;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public static void writeEntries(List<EntityDataManager.DataEntry<?>> var0, PacketBuffer var1) throws IOException {
      if (☃ != null) {
         int ☃ = 0;

         for (int ☃x = ☃.size(); ☃ < ☃x; ☃++) {
            EntityDataManager.DataEntry<?> ☃xx = ☃.get(☃);
            writeEntry(☃, ☃xx);
         }
      }

      ☃.writeByte(255);
   }

   @Nullable
   public List<EntityDataManager.DataEntry<?>> getDirty() {
      List<EntityDataManager.DataEntry<?>> ☃ = null;
      if (this.dirty) {
         this.lock.readLock().lock();

         for (EntityDataManager.DataEntry<?> ☃x : this.entries.values()) {
            if (☃x.isDirty()) {
               ☃x.setDirty(false);
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x.copy());
            }
         }

         this.lock.readLock().unlock();
      }

      this.dirty = false;
      return ☃;
   }

   public void writeEntries(PacketBuffer var1) throws IOException {
      this.lock.readLock().lock();

      for (EntityDataManager.DataEntry<?> ☃ : this.entries.values()) {
         writeEntry(☃, ☃);
      }

      this.lock.readLock().unlock();
      ☃.writeByte(255);
   }

   @Nullable
   public List<EntityDataManager.DataEntry<?>> getAll() {
      List<EntityDataManager.DataEntry<?>> ☃ = null;
      this.lock.readLock().lock();

      for (EntityDataManager.DataEntry<?> ☃x : this.entries.values()) {
         if (☃ == null) {
            ☃ = Lists.newArrayList();
         }

         ☃.add(☃x.copy());
      }

      this.lock.readLock().unlock();
      return ☃;
   }

   private static <T> void writeEntry(PacketBuffer var0, EntityDataManager.DataEntry<T> var1) throws IOException {
      DataParameter<T> ☃ = ☃.getKey();
      int ☃x = DataSerializers.getSerializerId(☃.getSerializer());
      if (☃x < 0) {
         throw new EncoderException("Unknown serializer type " + ☃.getSerializer());
      } else {
         ☃.writeByte(☃.getId());
         ☃.writeVarInt(☃x);
         ☃.getSerializer().write(☃, ☃.getValue());
      }
   }

   @Nullable
   public static List<EntityDataManager.DataEntry<?>> readEntries(PacketBuffer var0) throws IOException {
      List<EntityDataManager.DataEntry<?>> ☃ = null;

      int ☃x;
      while ((☃x = ☃.readUnsignedByte()) != 255) {
         if (☃ == null) {
            ☃ = Lists.newArrayList();
         }

         int ☃xx = ☃.readVarInt();
         DataSerializer<?> ☃xxx = DataSerializers.getSerializer(☃xx);
         if (☃xxx == null) {
            throw new DecoderException("Unknown serializer type " + ☃xx);
         }

         ☃.add(new EntityDataManager.DataEntry<>(☃xxx.createKey(☃x), ☃xxx.read(☃)));
      }

      return ☃;
   }

   public void setEntryValues(List<EntityDataManager.DataEntry<?>> var1) {
      this.lock.writeLock().lock();

      for (EntityDataManager.DataEntry<?> ☃ : ☃) {
         EntityDataManager.DataEntry<?> ☃x = this.entries.get(☃.getKey().getId());
         if (☃x != null) {
            this.setEntryValue(☃x, ☃);
            this.entity.notifyDataManagerChange(☃.getKey());
         }
      }

      this.lock.writeLock().unlock();
      this.dirty = true;
   }

   protected <T> void setEntryValue(EntityDataManager.DataEntry<T> var1, EntityDataManager.DataEntry<?> var2) {
      ☃.setValue((T)☃.getValue());
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public void setClean() {
      this.dirty = false;
      this.lock.readLock().lock();

      for (EntityDataManager.DataEntry<?> ☃ : this.entries.values()) {
         ☃.setDirty(false);
      }

      this.lock.readLock().unlock();
   }

   public static class DataEntry<T> {
      private final DataParameter<T> key;
      private T value;
      private boolean dirty;

      public DataEntry(DataParameter<T> var1, T var2) {
         this.key = ☃;
         this.value = ☃;
         this.dirty = true;
      }

      public DataParameter<T> getKey() {
         return this.key;
      }

      public void setValue(T var1) {
         this.value = ☃;
      }

      public T getValue() {
         return this.value;
      }

      public boolean isDirty() {
         return this.dirty;
      }

      public void setDirty(boolean var1) {
         this.dirty = ☃;
      }

      public EntityDataManager.DataEntry<T> copy() {
         return new EntityDataManager.DataEntry<>(this.key, this.key.getSerializer().copyValue(this.value));
      }
   }
}
