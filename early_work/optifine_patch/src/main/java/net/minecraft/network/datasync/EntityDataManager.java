/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  io.netty.handler.codec.DecoderException
 *  io.netty.handler.codec.EncoderException
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.locks.ReadWriteLock
 *  java.util.concurrent.locks.ReentrantReadWriteLock
 *  javax.annotation.Nullable
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Biomes
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.DataSerializer
 *  net.minecraft.network.datasync.DataSerializers
 *  net.minecraft.network.datasync.EntityDataManager$DataEntry
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.biome.Biome
 *  org.apache.commons.lang3.ObjectUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.network.datasync;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Exception performing whole class analysis ignored.
 */
public class EntityDataManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<Class<? extends Entity>, Integer> NEXT_ID_MAP = Maps.newHashMap();
    private final Entity entity;
    private final Map<Integer, DataEntry<?>> entries = Maps.newHashMap();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean empty = true;
    private boolean dirty;
    public Biome spawnBiome = Biomes.PLAINS;
    public BlockPos spawnPosition = BlockPos.ORIGIN;

    public EntityDataManager(Entity entityIn) {
        this.entity = entityIn;
    }

    public static <T> DataParameter<T> createKey(Class<? extends Entity> clazz, DataSerializer<T> serializer) {
        int j;
        if (LOGGER.isDebugEnabled()) {
            try {
                Class oclass = Class.forName((String)Thread.currentThread().getStackTrace()[2].getClassName());
                if (!oclass.equals(clazz)) {
                    LOGGER.debug("defineId called for: {} from {}", (Object)clazz, (Object)oclass, (Object)new RuntimeException());
                }
            }
            catch (ClassNotFoundException oclass) {
                // empty catch block
            }
        }
        if (NEXT_ID_MAP.containsKey(clazz)) {
            j = (Integer)NEXT_ID_MAP.get(clazz) + 1;
        } else {
            int i = 0;
            Class oclass1 = clazz;
            while (oclass1 != Entity.class) {
                if (!NEXT_ID_MAP.containsKey((Object)(oclass1 = oclass1.getSuperclass()))) continue;
                i = (Integer)NEXT_ID_MAP.get((Object)oclass1) + 1;
                break;
            }
            j = i;
        }
        if (j > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + j + "! (Max is " + 254 + ")");
        }
        NEXT_ID_MAP.put((Object)clazz, (Object)j);
        return serializer.createKey(j);
    }

    public <T> void register(DataParameter<T> key, T value) {
        int i = key.getId();
        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
        }
        if (this.entries.containsKey((Object)i)) {
            throw new IllegalArgumentException("Duplicate id value for " + i + "!");
        }
        if (DataSerializers.getSerializerId((DataSerializer)key.getSerializer()) < 0) {
            throw new IllegalArgumentException("Unregistered serializer " + key.getSerializer() + " for " + i + "!");
        }
        this.setEntry(key, value);
    }

    private <T> void setEntry(DataParameter<T> key, T value) {
        DataEntry dataentry = new DataEntry(key, value);
        this.lock.writeLock().lock();
        this.entries.put((Object)key.getId(), (Object)dataentry);
        this.empty = false;
        this.lock.writeLock().unlock();
    }

    private <T> DataEntry<T> getEntry(DataParameter<T> key) {
        DataEntry dataentry;
        this.lock.readLock().lock();
        try {
            dataentry = (DataEntry)this.entries.get((Object)key.getId());
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
            crashreportcategory.addCrashSection("Data ID", key);
            throw new ReportedException(crashreport);
        }
        this.lock.readLock().unlock();
        return dataentry;
    }

    public <T> T get(DataParameter<T> key) {
        return (T)this.getEntry(key).getValue();
    }

    public <T> void set(DataParameter<T> key, T value) {
        DataEntry<T> dataentry = this.getEntry(key);
        if (ObjectUtils.notEqual(value, (Object)dataentry.getValue())) {
            dataentry.setValue(value);
            this.entity.notifyDataManagerChange(key);
            dataentry.setDirty(true);
            this.dirty = true;
        }
    }

    public <T> void setDirty(DataParameter<T> key) {
        DataEntry.access$002(this.getEntry(key), (boolean)true);
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public static void writeEntries(List<DataEntry<?>> entriesIn, PacketBuffer buf) throws IOException {
        if (entriesIn != null) {
            int j = entriesIn.size();
            for (int i = 0; i < j; ++i) {
                DataEntry dataentry = (DataEntry)entriesIn.get(i);
                EntityDataManager.writeEntry(buf, dataentry);
            }
        }
        buf.writeByte(255);
    }

    @Nullable
    public List<DataEntry<?>> getDirty() {
        ArrayList list = null;
        if (this.dirty) {
            this.lock.readLock().lock();
            for (DataEntry dataentry : this.entries.values()) {
                if (!dataentry.isDirty()) continue;
                dataentry.setDirty(false);
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add((Object)dataentry.copy());
            }
            this.lock.readLock().unlock();
        }
        this.dirty = false;
        return list;
    }

    public void writeEntries(PacketBuffer buf) throws IOException {
        this.lock.readLock().lock();
        for (DataEntry dataentry : this.entries.values()) {
            EntityDataManager.writeEntry(buf, dataentry);
        }
        this.lock.readLock().unlock();
        buf.writeByte(255);
    }

    @Nullable
    public List<DataEntry<?>> getAll() {
        ArrayList list = null;
        this.lock.readLock().lock();
        for (DataEntry dataentry : this.entries.values()) {
            if (list == null) {
                list = Lists.newArrayList();
            }
            list.add((Object)dataentry.copy());
        }
        this.lock.readLock().unlock();
        return list;
    }

    private static <T> void writeEntry(PacketBuffer buf, DataEntry<T> entry) throws IOException {
        DataParameter dataparameter = entry.getKey();
        int i = DataSerializers.getSerializerId((DataSerializer)dataparameter.getSerializer());
        if (i < 0) {
            throw new EncoderException("Unknown serializer type " + dataparameter.getSerializer());
        }
        buf.writeByte(dataparameter.getId());
        buf.writeVarInt(i);
        dataparameter.getSerializer().write(buf, entry.getValue());
    }

    @Nullable
    public static List<DataEntry<?>> readEntries(PacketBuffer buf) throws IOException {
        short i;
        ArrayList list = null;
        while ((i = buf.readUnsignedByte()) != 255) {
            int j;
            DataSerializer dataserializer;
            if (list == null) {
                list = Lists.newArrayList();
            }
            if ((dataserializer = DataSerializers.getSerializer((int)(j = buf.readVarInt()))) == null) {
                throw new DecoderException("Unknown serializer type " + j);
            }
            list.add((Object)new DataEntry(dataserializer.createKey((int)i), dataserializer.read(buf)));
        }
        return list;
    }

    public void setEntryValues(List<DataEntry<?>> entriesIn) {
        this.lock.writeLock().lock();
        for (DataEntry dataentry : entriesIn) {
            DataEntry dataentry1 = (DataEntry)this.entries.get((Object)dataentry.getKey().getId());
            if (dataentry1 == null) continue;
            this.setEntryValue(dataentry1, dataentry);
            this.entity.notifyDataManagerChange(dataentry.getKey());
        }
        this.lock.writeLock().unlock();
        this.dirty = true;
    }

    protected <T> void setEntryValue(DataEntry<T> target, DataEntry<?> source) {
        target.setValue(source.getValue());
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void setClean() {
        this.dirty = false;
        this.lock.readLock().lock();
        for (DataEntry dataentry : this.entries.values()) {
            dataentry.setDirty(false);
        }
        this.lock.readLock().unlock();
    }
}
