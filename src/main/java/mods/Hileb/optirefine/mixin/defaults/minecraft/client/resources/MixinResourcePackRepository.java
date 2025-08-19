package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ResourcePackRepository.class)
public abstract class MixinResourcePackRepository {
}
/*
--- net/minecraft/client/resources/ResourcePackRepository.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/resources/ResourcePackRepository.java	Tue Aug 19 14:59:58 2025
@@ -16,12 +16,13 @@
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
+import java.util.Locale;
 import java.util.Map;
 import java.util.concurrent.locks.ReentrantLock;
 import java.util.regex.Pattern;
 import javax.annotation.Nullable;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.GuiScreenWorking;
@@ -35,12 +36,13 @@
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.text.TextFormatting;
 import org.apache.commons.codec.digest.DigestUtils;
 import org.apache.commons.io.FileUtils;
 import org.apache.commons.io.IOUtils;
 import org.apache.commons.io.comparator.LastModifiedFileComparator;
+import org.apache.commons.io.filefilter.IOFileFilter;
 import org.apache.commons.io.filefilter.TrueFileFilter;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class ResourcePackRepository {
    private static final Logger LOGGER = LogManager.getLogger();
@@ -58,13 +60,13 @@
    private final File dirServerResourcepacks;
    public final MetadataSerializer rprMetadataSerializer;
    private IResourcePack serverResourcePack;
    private final ReentrantLock lock = new ReentrantLock();
    private ListenableFuture<Object> downloadingPacks;
    private List<ResourcePackRepository.Entry> repositoryEntriesAll = Lists.newArrayList();
-   private final List<ResourcePackRepository.Entry> repositoryEntries = Lists.newArrayList();
+   public final List<ResourcePackRepository.Entry> repositoryEntries = Lists.newArrayList();

    public ResourcePackRepository(File var1, File var2, IResourcePack var3, MetadataSerializer var4, GameSettings var5) {
       this.dirResourcepacks = var1;
       this.dirServerResourcepacks = var2;
       this.rprDefaultResourcePack = var3;
       this.rprMetadataSerializer = var4;
@@ -117,13 +119,13 @@
          var2 = new FolderResourcePack(var1);
       } else {
          var2 = new FileResourcePack(var1);
       }

       try {
-         PackMetadataSection var3 = ((IResourcePack)var2).getPackMetadata(this.rprMetadataSerializer, "pack");
+         PackMetadataSection var3 = (PackMetadataSection)var2.getPackMetadata(this.rprMetadataSerializer, "pack");
          if (var3 != null && var3.getPackFormat() == 2) {
             return new LegacyV2Adapter((IResourcePack)var2);
          }
       } catch (Exception var4) {
       }

@@ -239,30 +241,37 @@
       } finally {
          this.lock.unlock();
       }
    }

    private boolean checkHash(String var1, File var2) {
+      FileInputStream var3 = null;
+
+      boolean var5;
       try {
-         String var3 = DigestUtils.sha1Hex(new FileInputStream(var2));
+         String var4 = DigestUtils.sha1Hex(var3 = new FileInputStream(var2));
          if (var1.isEmpty()) {
             LOGGER.info("Found file {} without verification hash", var2);
             return true;
          }

-         if (var3.toLowerCase(java.util.Locale.ROOT).equals(var1.toLowerCase(java.util.Locale.ROOT))) {
-            LOGGER.info("Found file {} matching requested hash {}", var2, var1);
-            return true;
+         if (!var4.toLowerCase(Locale.ROOT).equals(var1.toLowerCase(Locale.ROOT))) {
+            LOGGER.warn("File {} had wrong hash (expected {}, found {}).", var2, var1, var4);
+            return false;
          }

-         LOGGER.warn("File {} had wrong hash (expected {}, found {}).", var2, var1, var3);
-      } catch (IOException var4) {
-         LOGGER.warn("File {} couldn't be hashed.", var2, var4);
+         LOGGER.info("Found file {} matching requested hash {}", var2, var1);
+         var5 = true;
+      } catch (IOException var9) {
+         LOGGER.warn("File {} couldn't be hashed.", var2, var9);
+         return false;
+      } finally {
+         IOUtils.closeQuietly(var3);
       }

-      return false;
+      return var5;
    }

    private boolean validatePack(File var1) {
       ResourcePackRepository.Entry var2 = new ResourcePackRepository.Entry(var1);

       try {
@@ -273,13 +282,13 @@
          return false;
       }
    }

    private void deleteOldServerResourcesPacks() {
       try {
-         ArrayList var1 = Lists.newArrayList(FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, null));
+         ArrayList var1 = Lists.newArrayList(FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, (IOFileFilter)null));
          Collections.sort(var1, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
          int var2 = 0;

          for (File var4 : var1) {
             if (var2++ >= 10) {
                LOGGER.info("Deleting old server resource pack {}", var4.getName());
@@ -334,31 +343,32 @@

       private Entry(IResourcePack var2) {
          this.reResourcePack = var2;
       }

       public void updateResourcePack() throws IOException {
-         this.rePackMetadataSection = this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
+         this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
          this.closeResourcePack();
       }

       public void bindTexturePackIcon(TextureManager var1) {
          BufferedImage var2 = null;
-
-         try {
-            var2 = this.reResourcePack.getPackImage();
-         } catch (IOException var5) {
-         }
-
-         if (var2 == null) {
+         if (this.locationTexturePackIcon == null) {
             try {
-               var2 = TextureUtil.readBufferedImage(
-                  Minecraft.getMinecraft().getResourceManager().getResource(ResourcePackRepository.UNKNOWN_PACK_TEXTURE).getInputStream()
-               );
-            } catch (IOException var4) {
-               throw new Error("Couldn't bind resource pack icon", var4);
+               var2 = this.reResourcePack.getPackImage();
+            } catch (IOException var5) {
+            }
+
+            if (var2 == null) {
+               try {
+                  var2 = TextureUtil.readBufferedImage(
+                     Minecraft.getMinecraft().getResourceManager().getResource(ResourcePackRepository.UNKNOWN_PACK_TEXTURE).getInputStream()
+                  );
+               } catch (IOException var4) {
+                  throw new Error("Couldn't bind resource pack icon", var4);
+               }
             }
          }

          if (this.locationTexturePackIcon == null) {
             this.locationTexturePackIcon = var1.getDynamicTextureLocation("texturepackicon", new DynamicTexture(var2));
          }
 */
