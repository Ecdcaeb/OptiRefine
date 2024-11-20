/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.util.concurrent.FutureCallback
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.SettableFuture
 *  java.io.File
 *  java.io.FileFilter
 *  java.io.FileInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.Proxy
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Locale
 *  java.util.Map
 *  java.util.concurrent.Future
 *  java.util.concurrent.locks.ReentrantLock
 *  java.util.regex.Pattern
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreenWorking
 *  net.minecraft.client.resources.FileResourcePack
 *  net.minecraft.client.resources.FolderResourcePack
 *  net.minecraft.client.resources.IResourcePack
 *  net.minecraft.client.resources.LegacyV2Adapter
 *  net.minecraft.client.resources.ResourcePackRepository$Entry
 *  net.minecraft.client.resources.data.MetadataSerializer
 *  net.minecraft.client.resources.data.PackMetadataSection
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.util.HttpUtil
 *  net.minecraft.util.IProgressUpdate
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.io.comparator.LastModifiedFileComparator
 *  org.apache.commons.io.filefilter.IOFileFilter
 *  org.apache.commons.io.filefilter.TrueFileFilter
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(value=Side.CLIENT)
public class ResourcePackRepository {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final FileFilter RESOURCE_PACK_FILTER = new /* Unavailable Anonymous Inner Class!! */;
    private static final Pattern SHA1 = Pattern.compile((String)"^[a-fA-F0-9]{40}$");
    private static final ResourceLocation UNKNOWN_PACK_TEXTURE = new ResourceLocation("textures/misc/unknown_pack.png");
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File dirServerResourcepacks;
    public final MetadataSerializer rprMetadataSerializer;
    private IResourcePack serverResourcePack;
    private final ReentrantLock lock = new ReentrantLock();
    private ListenableFuture<Object> downloadingPacks;
    private List<Entry> repositoryEntriesAll = Lists.newArrayList();
    private final List<Entry> repositoryEntries = Lists.newArrayList();

    public ResourcePackRepository(File dirResourcepacksIn, File dirServerResourcepacksIn, IResourcePack rprDefaultResourcePackIn, MetadataSerializer rprMetadataSerializerIn, GameSettings settings) {
        this.dirResourcepacks = dirResourcepacksIn;
        this.dirServerResourcepacks = dirServerResourcepacksIn;
        this.rprDefaultResourcePack = rprDefaultResourcePackIn;
        this.rprMetadataSerializer = rprMetadataSerializerIn;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        Iterator iterator = settings.resourcePacks.iterator();
        block0: while (iterator.hasNext()) {
            String s = (String)iterator.next();
            for (Entry resourcepackrepository$entry : this.repositoryEntriesAll) {
                if (!resourcepackrepository$entry.getResourcePackName().equals((Object)s)) continue;
                if (resourcepackrepository$entry.getPackFormat() == 3 || settings.incompatibleResourcePacks.contains((Object)resourcepackrepository$entry.getResourcePackName())) {
                    this.repositoryEntries.add((Object)resourcepackrepository$entry);
                    continue block0;
                }
                iterator.remove();
                LOGGER.warn("Removed selected resource pack {} because it's no longer compatible", (Object)resourcepackrepository$entry.getResourcePackName());
            }
        }
    }

    public static Map<String, String> getDownloadHeaders() {
        HashMap map = Maps.newHashMap();
        map.put((Object)"X-Minecraft-Username", (Object)Minecraft.getMinecraft().getSession().getUsername());
        map.put((Object)"X-Minecraft-UUID", (Object)Minecraft.getMinecraft().getSession().getPlayerID());
        map.put((Object)"X-Minecraft-Version", (Object)"1.12.2");
        return map;
    }

    private void fixDirResourcepacks() {
        if (this.dirResourcepacks.exists()) {
            if (!(this.dirResourcepacks.isDirectory() || this.dirResourcepacks.delete() && this.dirResourcepacks.mkdirs())) {
                LOGGER.warn("Unable to recreate resourcepack folder, it exists but is not a directory: {}", (Object)this.dirResourcepacks);
            }
        } else if (!this.dirResourcepacks.mkdirs()) {
            LOGGER.warn("Unable to create resourcepack folder: {}", (Object)this.dirResourcepacks);
        }
    }

    private List<File> getResourcePackFiles() {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList((Object[])this.dirResourcepacks.listFiles(RESOURCE_PACK_FILTER)) : Collections.emptyList();
    }

    private IResourcePack getResourcePack(File p_191399_1_) {
        Object iresourcepack = p_191399_1_.isDirectory() ? new FolderResourcePack(p_191399_1_) : new FileResourcePack(p_191399_1_);
        try {
            PackMetadataSection packmetadatasection = (PackMetadataSection)iresourcepack.getPackMetadata(this.rprMetadataSerializer, "pack");
            if (packmetadatasection != null && packmetadatasection.getPackFormat() == 2) {
                return new LegacyV2Adapter((IResourcePack)iresourcepack);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return iresourcepack;
    }

    public void updateRepositoryEntriesAll() {
        ArrayList list = Lists.newArrayList();
        for (File file1 : this.getResourcePackFiles()) {
            Entry resourcepackrepository$entry = new Entry(this, file1);
            if (this.repositoryEntriesAll.contains((Object)resourcepackrepository$entry)) {
                int i = this.repositoryEntriesAll.indexOf((Object)resourcepackrepository$entry);
                if (i <= -1 || i >= this.repositoryEntriesAll.size()) continue;
                list.add((Object)((Entry)this.repositoryEntriesAll.get(i)));
                continue;
            }
            try {
                resourcepackrepository$entry.updateResourcePack();
                list.add((Object)resourcepackrepository$entry);
            }
            catch (Exception var6) {
                list.remove((Object)resourcepackrepository$entry);
            }
        }
        this.repositoryEntriesAll.removeAll((Collection)list);
        for (Entry resourcepackrepository$entry1 : this.repositoryEntriesAll) {
            resourcepackrepository$entry1.closeResourcePack();
        }
        this.repositoryEntriesAll = list;
    }

    @Nullable
    public Entry getResourcePackEntry() {
        if (this.serverResourcePack != null) {
            Entry resourcepackrepository$entry = new Entry(this, this.serverResourcePack);
            try {
                resourcepackrepository$entry.updateResourcePack();
                return resourcepackrepository$entry;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return null;
    }

    public List<Entry> getRepositoryEntriesAll() {
        return ImmutableList.copyOf(this.repositoryEntriesAll);
    }

    public List<Entry> getRepositoryEntries() {
        return ImmutableList.copyOf(this.repositoryEntries);
    }

    public void setRepositories(List<Entry> repositories) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(repositories);
    }

    public File getDirResourcepacks() {
        return this.dirResourcepacks;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListenableFuture<Object> downloadResourcePack(String url, String hash) {
        String s = DigestUtils.sha1Hex((String)url);
        String s1 = SHA1.matcher((CharSequence)hash).matches() ? hash : "";
        File file1 = new File(this.dirServerResourcepacks, s);
        this.lock.lock();
        try {
            this.clearResourcePack();
            if (file1.exists()) {
                if (this.checkHash(s1, file1)) {
                    ListenableFuture<Object> listenablefuture1;
                    ListenableFuture<Object> listenableFuture = listenablefuture1 = this.setServerResourcePack(file1);
                    return listenableFuture;
                }
                LOGGER.warn("Deleting file {}", (Object)file1);
                FileUtils.deleteQuietly((File)file1);
            }
            this.deleteOldServerResourcesPacks();
            GuiScreenWorking guiscreenworking = new GuiScreenWorking();
            Map<String, String> map = ResourcePackRepository.getDownloadHeaders();
            Minecraft minecraft = Minecraft.getMinecraft();
            Futures.getUnchecked((Future)minecraft.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */));
            SettableFuture settablefuture = SettableFuture.create();
            this.downloadingPacks = HttpUtil.downloadResourcePack((File)file1, (String)url, map, (int)0x3200000, (IProgressUpdate)guiscreenworking, (Proxy)minecraft.getProxy());
            Futures.addCallback(this.downloadingPacks, (FutureCallback)new /* Unavailable Anonymous Inner Class!! */, Runnable::run);
            ListenableFuture<Object> listenableFuture = this.downloadingPacks;
            return listenableFuture;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean checkHash(String p_190113_1_, File p_190113_2_) {
        String s;
        FileInputStream is;
        block7: {
            block6: {
                is = null;
                is = new FileInputStream(p_190113_2_);
                s = DigestUtils.sha1Hex((InputStream)is);
                if (!p_190113_1_.isEmpty()) break block6;
                LOGGER.info("Found file {} without verification hash", (Object)p_190113_2_);
                boolean bl = true;
                IOUtils.closeQuietly((InputStream)is);
                return bl;
            }
            if (!s.toLowerCase(Locale.ROOT).equals((Object)p_190113_1_.toLowerCase(Locale.ROOT))) break block7;
            LOGGER.info("Found file {} matching requested hash {}", (Object)p_190113_2_, (Object)p_190113_1_);
            boolean bl = true;
            IOUtils.closeQuietly((InputStream)is);
            return bl;
        }
        try {
            LOGGER.warn("File {} had wrong hash (expected {}, found {}).", (Object)p_190113_2_, (Object)p_190113_1_, (Object)s);
        }
        catch (IOException ioexception) {
            try {
                LOGGER.warn("File {} couldn't be hashed.", (Object)p_190113_2_, (Object)ioexception);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(is);
                throw throwable;
            }
            IOUtils.closeQuietly((InputStream)is);
        }
        IOUtils.closeQuietly((InputStream)is);
        return false;
    }

    private boolean validatePack(File p_190112_1_) {
        Entry resourcepackrepository$entry = new Entry(this, p_190112_1_);
        try {
            resourcepackrepository$entry.updateResourcePack();
            return true;
        }
        catch (Exception exception) {
            LOGGER.warn("Server resourcepack is invalid, ignoring it", (Throwable)exception);
            return false;
        }
    }

    private void deleteOldServerResourcesPacks() {
        try {
            ArrayList list = Lists.newArrayList((Iterable)FileUtils.listFiles((File)this.dirServerResourcepacks, (IOFileFilter)TrueFileFilter.TRUE, (IOFileFilter)null));
            Collections.sort((List)list, (Comparator)LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            int i = 0;
            for (File file1 : list) {
                if (i++ < 10) continue;
                LOGGER.info("Deleting old server resource pack {}", (Object)file1.getName());
                FileUtils.deleteQuietly((File)file1);
            }
        }
        catch (IllegalArgumentException illegalargumentexception) {
            LOGGER.error("Error while deleting old server resource pack : {}", (Object)illegalargumentexception.getMessage());
        }
    }

    public ListenableFuture<Object> setServerResourcePack(File resourceFile) {
        if (!this.validatePack(resourceFile)) {
            return Futures.immediateFailedFuture((Throwable)new RuntimeException("Invalid resourcepack"));
        }
        this.serverResourcePack = new FileResourcePack(resourceFile);
        return Minecraft.getMinecraft().scheduleResourcesRefresh();
    }

    @Nullable
    public IResourcePack getServerResourcePack() {
        return this.serverResourcePack;
    }

    public void clearResourcePack() {
        this.lock.lock();
        try {
            if (this.downloadingPacks != null) {
                this.downloadingPacks.cancel(true);
            }
            this.downloadingPacks = null;
            if (this.serverResourcePack != null) {
                this.serverResourcePack = null;
                Minecraft.getMinecraft().scheduleResourcesRefresh();
            }
        }
        finally {
            this.lock.unlock();
        }
    }
}
