package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager implements IResourceManager {
   private static final Logger LOGGER = LogManager.getLogger();
   protected final List<IResourcePack> resourcePacks = Lists.newArrayList();
   private final MetadataSerializer frmMetadataSerializer;

   public FallbackResourceManager(MetadataSerializer var1) {
      this.frmMetadataSerializer = ☃;
   }

   public void addResourcePack(IResourcePack var1) {
      this.resourcePacks.add(☃);
   }

   @Override
   public Set<String> getResourceDomains() {
      return Collections.emptySet();
   }

   @Override
   public IResource getResource(ResourceLocation var1) throws IOException {
      this.checkResourcePath(☃);
      IResourcePack ☃ = null;
      ResourceLocation ☃x = getLocationMcmeta(☃);

      for (int ☃xx = this.resourcePacks.size() - 1; ☃xx >= 0; ☃xx--) {
         IResourcePack ☃xxx = this.resourcePacks.get(☃xx);
         if (☃ == null && ☃xxx.resourceExists(☃x)) {
            ☃ = ☃xxx;
         }

         if (☃xxx.resourceExists(☃)) {
            InputStream ☃xxxx = null;
            if (☃ != null) {
               ☃xxxx = this.getInputStream(☃x, ☃);
            }

            return new SimpleResource(☃xxx.getPackName(), ☃, this.getInputStream(☃, ☃xxx), ☃xxxx, this.frmMetadataSerializer);
         }
      }

      throw new FileNotFoundException(☃.toString());
   }

   protected InputStream getInputStream(ResourceLocation var1, IResourcePack var2) throws IOException {
      InputStream ☃ = ☃.getInputStream(☃);
      return (InputStream)(LOGGER.isDebugEnabled() ? new FallbackResourceManager.InputStreamLeakedResourceLogger(☃, ☃, ☃.getPackName()) : ☃);
   }

   private void checkResourcePath(ResourceLocation var1) throws IOException {
      if (☃.getPath().contains("..")) {
         throw new IOException("Invalid relative path to resource: " + ☃);
      }
   }

   @Override
   public List<IResource> getAllResources(ResourceLocation var1) throws IOException {
      this.checkResourcePath(☃);
      List<IResource> ☃ = Lists.newArrayList();
      ResourceLocation ☃x = getLocationMcmeta(☃);

      for (IResourcePack ☃xx : this.resourcePacks) {
         if (☃xx.resourceExists(☃)) {
            InputStream ☃xxx = ☃xx.resourceExists(☃x) ? this.getInputStream(☃x, ☃xx) : null;
            ☃.add(new SimpleResource(☃xx.getPackName(), ☃, this.getInputStream(☃, ☃xx), ☃xxx, this.frmMetadataSerializer));
         }
      }

      if (☃.isEmpty()) {
         throw new FileNotFoundException(☃.toString());
      } else {
         return ☃;
      }
   }

   static ResourceLocation getLocationMcmeta(ResourceLocation var0) {
      return new ResourceLocation(☃.getNamespace(), ☃.getPath() + ".mcmeta");
   }

   static class InputStreamLeakedResourceLogger extends InputStream {
      private final InputStream inputStream;
      private final String message;
      private boolean isClosed;

      public InputStreamLeakedResourceLogger(InputStream var1, ResourceLocation var2, String var3) {
         this.inputStream = ☃;
         ByteArrayOutputStream ☃ = new ByteArrayOutputStream();
         new Exception().printStackTrace(new PrintStream(☃));
         this.message = "Leaked resource: '" + ☃ + "' loaded from pack: '" + ☃ + "'\n" + ☃;
      }

      @Override
      public void close() throws IOException {
         this.inputStream.close();
         this.isClosed = true;
      }

      @Override
      protected void finalize() throws Throwable {
         if (!this.isClosed) {
            FallbackResourceManager.LOGGER.warn(this.message);
         }

         super.finalize();
      }

      @Override
      public int read() throws IOException {
         return this.inputStream.read();
      }
   }
}
