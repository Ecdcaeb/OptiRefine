package net.minecraft.client.resources;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements IReloadableResourceManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Joiner JOINER_RESOURCE_PACKS = Joiner.on(", ");
   private final Map<String, FallbackResourceManager> domainResourceManagers = Maps.newHashMap();
   private final List<IResourceManagerReloadListener> reloadListeners = Lists.newArrayList();
   private final Set<String> setResourceDomains = Sets.newLinkedHashSet();
   private final MetadataSerializer rmMetadataSerializer;

   public SimpleReloadableResourceManager(MetadataSerializer var1) {
      this.rmMetadataSerializer = ☃;
   }

   public void reloadResourcePack(IResourcePack var1) {
      for (String ☃ : ☃.getResourceDomains()) {
         this.setResourceDomains.add(☃);
         FallbackResourceManager ☃x = this.domainResourceManagers.get(☃);
         if (☃x == null) {
            ☃x = new FallbackResourceManager(this.rmMetadataSerializer);
            this.domainResourceManagers.put(☃, ☃x);
         }

         ☃x.addResourcePack(☃);
      }
   }

   @Override
   public Set<String> getResourceDomains() {
      return this.setResourceDomains;
   }

   @Override
   public IResource getResource(ResourceLocation var1) throws IOException {
      IResourceManager ☃ = this.domainResourceManagers.get(☃.getNamespace());
      if (☃ != null) {
         return ☃.getResource(☃);
      } else {
         throw new FileNotFoundException(☃.toString());
      }
   }

   @Override
   public List<IResource> getAllResources(ResourceLocation var1) throws IOException {
      IResourceManager ☃ = this.domainResourceManagers.get(☃.getNamespace());
      if (☃ != null) {
         return ☃.getAllResources(☃);
      } else {
         throw new FileNotFoundException(☃.toString());
      }
   }

   private void clearResources() {
      this.domainResourceManagers.clear();
      this.setResourceDomains.clear();
   }

   @Override
   public void reloadResources(List<IResourcePack> var1) {
      this.clearResources();
      LOGGER.info("Reloading ResourceManager: {}", JOINER_RESOURCE_PACKS.join(Iterables.transform(☃, new Function<IResourcePack, String>() {
         public String apply(@Nullable IResourcePack var1) {
            return ☃ == null ? "<NULL>" : ☃.getPackName();
         }
      })));

      for (IResourcePack ☃ : ☃) {
         this.reloadResourcePack(☃);
      }

      this.notifyReloadListeners();
   }

   @Override
   public void registerReloadListener(IResourceManagerReloadListener var1) {
      this.reloadListeners.add(☃);
      ☃.onResourceManagerReload(this);
   }

   private void notifyReloadListeners() {
      for (IResourceManagerReloadListener ☃ : this.reloadListeners) {
         ☃.onResourceManagerReload(this);
      }
   }
}
