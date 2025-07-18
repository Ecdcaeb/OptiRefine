package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackRepository {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final FileFilter RESOURCE_PACK_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         boolean ☃ = ☃.isFile() && ☃.getName().endsWith(".zip");
         boolean ☃x = ☃.isDirectory() && new File(☃, "pack.mcmeta").isFile();
         return ☃ || ☃x;
      }
   };
   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
   private static final ResourceLocation UNKNOWN_PACK_TEXTURE = new ResourceLocation("textures/misc/unknown_pack.png");
   private final File dirResourcepacks;
   public final IResourcePack rprDefaultResourcePack;
   private final File dirServerResourcepacks;
   public final MetadataSerializer rprMetadataSerializer;
   private IResourcePack serverResourcePack;
   private final ReentrantLock lock = new ReentrantLock();
   private ListenableFuture<Object> downloadingPacks;
   private List<ResourcePackRepository.Entry> repositoryEntriesAll = Lists.newArrayList();
   private final List<ResourcePackRepository.Entry> repositoryEntries = Lists.newArrayList();

   public ResourcePackRepository(File var1, File var2, IResourcePack var3, MetadataSerializer var4, GameSettings var5) {
      this.dirResourcepacks = ☃;
      this.dirServerResourcepacks = ☃;
      this.rprDefaultResourcePack = ☃;
      this.rprMetadataSerializer = ☃;
      this.fixDirResourcepacks();
      this.updateRepositoryEntriesAll();
      Iterator<String> ☃ = ☃.resourcePacks.iterator();

      while (☃.hasNext()) {
         String ☃x = ☃.next();

         for (ResourcePackRepository.Entry ☃xx : this.repositoryEntriesAll) {
            if (☃xx.getResourcePackName().equals(☃x)) {
               if (☃xx.getPackFormat() == 3 || ☃.incompatibleResourcePacks.contains(☃xx.getResourcePackName())) {
                  this.repositoryEntries.add(☃xx);
                  break;
               }

               ☃.remove();
               LOGGER.warn("Removed selected resource pack {} because it's no longer compatible", ☃xx.getResourcePackName());
            }
         }
      }
   }

   public static Map<String, String> getDownloadHeaders() {
      Map<String, String> ☃ = Maps.newHashMap();
      ☃.put("X-Minecraft-Username", Minecraft.getMinecraft().getSession().getUsername());
      ☃.put("X-Minecraft-UUID", Minecraft.getMinecraft().getSession().getPlayerID());
      ☃.put("X-Minecraft-Version", "1.12.2");
      return ☃;
   }

   private void fixDirResourcepacks() {
      if (this.dirResourcepacks.exists()) {
         if (!this.dirResourcepacks.isDirectory() && (!this.dirResourcepacks.delete() || !this.dirResourcepacks.mkdirs())) {
            LOGGER.warn("Unable to recreate resourcepack folder, it exists but is not a directory: {}", this.dirResourcepacks);
         }
      } else if (!this.dirResourcepacks.mkdirs()) {
         LOGGER.warn("Unable to create resourcepack folder: {}", this.dirResourcepacks);
      }
   }

   private List<File> getResourcePackFiles() {
      return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(RESOURCE_PACK_FILTER)) : Collections.emptyList();
   }

   private IResourcePack getResourcePack(File var1) {
      IResourcePack ☃;
      if (☃.isDirectory()) {
         ☃ = new FolderResourcePack(☃);
      } else {
         ☃ = new FileResourcePack(☃);
      }

      try {
         PackMetadataSection ☃x = ☃.getPackMetadata(this.rprMetadataSerializer, "pack");
         if (☃x != null && ☃x.getPackFormat() == 2) {
            return new LegacyV2Adapter(☃);
         }
      } catch (Exception var4) {
      }

      return ☃;
   }

   public void updateRepositoryEntriesAll() {
      List<ResourcePackRepository.Entry> ☃ = Lists.newArrayList();

      for (File ☃x : this.getResourcePackFiles()) {
         ResourcePackRepository.Entry ☃xx = new ResourcePackRepository.Entry(☃x);
         if (this.repositoryEntriesAll.contains(☃xx)) {
            int ☃xxx = this.repositoryEntriesAll.indexOf(☃xx);
            if (☃xxx > -1 && ☃xxx < this.repositoryEntriesAll.size()) {
               ☃.add(this.repositoryEntriesAll.get(☃xxx));
            }
         } else {
            try {
               ☃xx.updateResourcePack();
               ☃.add(☃xx);
            } catch (Exception var6) {
               ☃.remove(☃xx);
            }
         }
      }

      this.repositoryEntriesAll.removeAll(☃);

      for (ResourcePackRepository.Entry ☃xx : this.repositoryEntriesAll) {
         ☃xx.closeResourcePack();
      }

      this.repositoryEntriesAll = ☃;
   }

   @Nullable
   public ResourcePackRepository.Entry getResourcePackEntry() {
      if (this.serverResourcePack != null) {
         ResourcePackRepository.Entry ☃ = new ResourcePackRepository.Entry(this.serverResourcePack);

         try {
            ☃.updateResourcePack();
            return ☃;
         } catch (IOException var3) {
         }
      }

      return null;
   }

   public List<ResourcePackRepository.Entry> getRepositoryEntriesAll() {
      return ImmutableList.copyOf(this.repositoryEntriesAll);
   }

   public List<ResourcePackRepository.Entry> getRepositoryEntries() {
      return ImmutableList.copyOf(this.repositoryEntries);
   }

   public void setRepositories(List<ResourcePackRepository.Entry> var1) {
      this.repositoryEntries.clear();
      this.repositoryEntries.addAll(☃);
   }

   public File getDirResourcepacks() {
      return this.dirResourcepacks;
   }

   public ListenableFuture<Object> downloadResourcePack(String var1, String var2) {
      String ☃ = DigestUtils.sha1Hex(☃);
      final String ☃x = SHA1.matcher(☃).matches() ? ☃ : "";
      final File ☃xx = new File(this.dirServerResourcepacks, ☃);
      this.lock.lock();

      try {
         this.clearResourcePack();
         if (☃xx.exists()) {
            if (this.checkHash(☃x, ☃xx)) {
               return this.setServerResourcePack(☃xx);
            }

            LOGGER.warn("Deleting file {}", ☃xx);
            FileUtils.deleteQuietly(☃xx);
         }

         this.deleteOldServerResourcesPacks();
         final GuiScreenWorking ☃xxx = new GuiScreenWorking();
         Map<String, String> ☃xxxx = getDownloadHeaders();
         final Minecraft ☃xxxxx = Minecraft.getMinecraft();
         Futures.getUnchecked(☃xxxxx.addScheduledTask(new Runnable() {
            @Override
            public void run() {
               ☃.displayGuiScreen(☃);
            }
         }));
         final SettableFuture<Object> ☃xxxxxx = SettableFuture.create();
         this.downloadingPacks = HttpUtil.downloadResourcePack(☃xx, ☃, ☃xxxx, 52428800, ☃xxx, ☃xxxxx.getProxy());
         Futures.addCallback(this.downloadingPacks, new FutureCallback<Object>() {
            public void onSuccess(@Nullable Object var1) {
               if (ResourcePackRepository.this.checkHash(☃, ☃)) {
                  ResourcePackRepository.this.setServerResourcePack(☃);
                  ☃.set(null);
               } else {
                  ResourcePackRepository.LOGGER.warn("Deleting file {}", ☃);
                  FileUtils.deleteQuietly(☃);
               }
            }

            public void onFailure(Throwable var1) {
               FileUtils.deleteQuietly(☃);
               ☃.setException(☃);
            }
         });
         return this.downloadingPacks;
      } finally {
         this.lock.unlock();
      }
   }

   private boolean checkHash(String var1, File var2) {
      try {
         String ☃ = DigestUtils.sha1Hex(new FileInputStream(☃));
         if (☃.isEmpty()) {
            LOGGER.info("Found file {} without verification hash", ☃);
            return true;
         }

         if (☃.toLowerCase(java.util.Locale.ROOT).equals(☃.toLowerCase(java.util.Locale.ROOT))) {
            LOGGER.info("Found file {} matching requested hash {}", ☃, ☃);
            return true;
         }

         LOGGER.warn("File {} had wrong hash (expected {}, found {}).", ☃, ☃, ☃);
      } catch (IOException var4) {
         LOGGER.warn("File {} couldn't be hashed.", ☃, var4);
      }

      return false;
   }

   private boolean validatePack(File var1) {
      ResourcePackRepository.Entry ☃ = new ResourcePackRepository.Entry(☃);

      try {
         ☃.updateResourcePack();
         return true;
      } catch (Exception var4) {
         LOGGER.warn("Server resourcepack is invalid, ignoring it", var4);
         return false;
      }
   }

   private void deleteOldServerResourcesPacks() {
      try {
         List<File> ☃ = Lists.newArrayList(FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, null));
         Collections.sort(☃, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
         int ☃x = 0;

         for (File ☃xx : ☃) {
            if (☃x++ >= 10) {
               LOGGER.info("Deleting old server resource pack {}", ☃xx.getName());
               FileUtils.deleteQuietly(☃xx);
            }
         }
      } catch (IllegalArgumentException var5) {
         LOGGER.error("Error while deleting old server resource pack : {}", var5.getMessage());
      }
   }

   public ListenableFuture<Object> setServerResourcePack(File var1) {
      if (!this.validatePack(☃)) {
         return Futures.immediateFailedFuture(new RuntimeException("Invalid resourcepack"));
      } else {
         this.serverResourcePack = new FileResourcePack(☃);
         return Minecraft.getMinecraft().scheduleResourcesRefresh();
      }
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
      } finally {
         this.lock.unlock();
      }
   }

   public class Entry {
      private final IResourcePack reResourcePack;
      private PackMetadataSection rePackMetadataSection;
      private ResourceLocation locationTexturePackIcon;

      private Entry(File var2) {
         this(ResourcePackRepository.this.getResourcePack(☃));
      }

      private Entry(IResourcePack var2) {
         this.reResourcePack = ☃;
      }

      public void updateResourcePack() throws IOException {
         this.rePackMetadataSection = this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
         this.closeResourcePack();
      }

      public void bindTexturePackIcon(TextureManager var1) {
         BufferedImage ☃ = null;

         try {
            ☃ = this.reResourcePack.getPackImage();
         } catch (IOException var5) {
         }

         if (☃ == null) {
            try {
               ☃ = TextureUtil.readBufferedImage(
                  Minecraft.getMinecraft().getResourceManager().getResource(ResourcePackRepository.UNKNOWN_PACK_TEXTURE).getInputStream()
               );
            } catch (IOException var4) {
               throw new Error("Couldn't bind resource pack icon", var4);
            }
         }

         if (this.locationTexturePackIcon == null) {
            this.locationTexturePackIcon = ☃.getDynamicTextureLocation("texturepackicon", new DynamicTexture(☃));
         }

         ☃.bindTexture(this.locationTexturePackIcon);
      }

      public void closeResourcePack() {
         if (this.reResourcePack instanceof Closeable) {
            IOUtils.closeQuietly((Closeable)this.reResourcePack);
         }
      }

      public IResourcePack getResourcePack() {
         return this.reResourcePack;
      }

      public String getResourcePackName() {
         return this.reResourcePack.getPackName();
      }

      public String getTexturePackDescription() {
         return this.rePackMetadataSection == null
            ? TextFormatting.RED + "Invalid pack.mcmeta (or missing 'pack' section)"
            : this.rePackMetadataSection.getPackDescription().getFormattedText();
      }

      public int getPackFormat() {
         return this.rePackMetadataSection == null ? 0 : this.rePackMetadataSection.getPackFormat();
      }

      @Override
      public boolean equals(Object var1) {
         if (this == ☃) {
            return true;
         } else {
            return ☃ instanceof ResourcePackRepository.Entry ? this.toString().equals(☃.toString()) : false;
         }
      }

      @Override
      public int hashCode() {
         return this.toString().hashCode();
      }

      @Override
      public String toString() {
         return String.format("%s:%s", this.reResourcePack.getPackName(), this.reResourcePack instanceof FolderResourcePack ? "folder" : "zip");
      }
   }
}
