package net.minecraft.advancements;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancementList {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<ResourceLocation, Advancement> advancements = Maps.newHashMap();
   private final Set<Advancement> roots = Sets.newLinkedHashSet();
   private final Set<Advancement> nonRoots = Sets.newLinkedHashSet();
   private AdvancementList.Listener listener;

   private void remove(Advancement var1) {
      for (Advancement ☃ : ☃.getChildren()) {
         this.remove(☃);
      }

      LOGGER.info("Forgot about advancement " + ☃.getId());
      this.advancements.remove(☃.getId());
      if (☃.getParent() == null) {
         this.roots.remove(☃);
         if (this.listener != null) {
            this.listener.rootAdvancementRemoved(☃);
         }
      } else {
         this.nonRoots.remove(☃);
         if (this.listener != null) {
            this.listener.nonRootAdvancementRemoved(☃);
         }
      }
   }

   public void removeAll(Set<ResourceLocation> var1) {
      for (ResourceLocation ☃ : ☃) {
         Advancement ☃x = this.advancements.get(☃);
         if (☃x == null) {
            LOGGER.warn("Told to remove advancement " + ☃ + " but I don't know what that is");
         } else {
            this.remove(☃x);
         }
      }
   }

   public void loadAdvancements(Map<ResourceLocation, Advancement.Builder> var1) {
      Function<ResourceLocation, Advancement> ☃ = Functions.forMap(this.advancements, null);

      while (!☃.isEmpty()) {
         boolean ☃x = false;
         Iterator<Entry<ResourceLocation, Advancement.Builder>> ☃xx = ☃.entrySet().iterator();

         while (☃xx.hasNext()) {
            Entry<ResourceLocation, Advancement.Builder> ☃xxx = ☃xx.next();
            ResourceLocation ☃xxxx = ☃xxx.getKey();
            Advancement.Builder ☃xxxxx = ☃xxx.getValue();
            if (☃xxxxx.resolveParent(☃)) {
               Advancement ☃xxxxxx = ☃xxxxx.build(☃xxxx);
               this.advancements.put(☃xxxx, ☃xxxxxx);
               ☃x = true;
               ☃xx.remove();
               if (☃xxxxxx.getParent() == null) {
                  this.roots.add(☃xxxxxx);
                  if (this.listener != null) {
                     this.listener.rootAdvancementAdded(☃xxxxxx);
                  }
               } else {
                  this.nonRoots.add(☃xxxxxx);
                  if (this.listener != null) {
                     this.listener.nonRootAdvancementAdded(☃xxxxxx);
                  }
               }
            }
         }

         if (!☃x) {
            for (Entry<ResourceLocation, Advancement.Builder> ☃xxx : ☃.entrySet()) {
               LOGGER.error("Couldn't load advancement " + ☃xxx.getKey() + ": " + ☃xxx.getValue());
            }
            break;
         }
      }

      LOGGER.info("Loaded " + this.advancements.size() + " advancements");
   }

   public void clear() {
      this.advancements.clear();
      this.roots.clear();
      this.nonRoots.clear();
      if (this.listener != null) {
         this.listener.advancementsCleared();
      }
   }

   public Iterable<Advancement> getRoots() {
      return this.roots;
   }

   public Iterable<Advancement> getAdvancements() {
      return this.advancements.values();
   }

   @Nullable
   public Advancement getAdvancement(ResourceLocation var1) {
      return this.advancements.get(☃);
   }

   public void setListener(@Nullable AdvancementList.Listener var1) {
      this.listener = ☃;
      if (☃ != null) {
         for (Advancement ☃ : this.roots) {
            ☃.rootAdvancementAdded(☃);
         }

         for (Advancement ☃ : this.nonRoots) {
            ☃.nonRootAdvancementAdded(☃);
         }
      }
   }

   public interface Listener {
      void rootAdvancementAdded(Advancement var1);

      void rootAdvancementRemoved(Advancement var1);

      void nonRootAdvancementAdded(Advancement var1);

      void nonRootAdvancementRemoved(Advancement var1);

      void advancementsCleared();
   }
}
