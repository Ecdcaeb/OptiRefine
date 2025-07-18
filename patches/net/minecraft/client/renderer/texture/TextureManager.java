package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureManager implements ITickable, IResourceManagerReloadListener {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
   private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
   private final List<ITickable> listTickables = Lists.newArrayList();
   private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
   private final IResourceManager resourceManager;

   public TextureManager(IResourceManager var1) {
      this.resourceManager = ☃;
   }

   public void bindTexture(ResourceLocation var1) {
      ITextureObject ☃ = this.mapTextureObjects.get(☃);
      if (☃ == null) {
         ☃ = new SimpleTexture(☃);
         this.loadTexture(☃, ☃);
      }

      TextureUtil.bindTexture(☃.getGlTextureId());
   }

   public boolean loadTickableTexture(ResourceLocation var1, ITickableTextureObject var2) {
      if (this.loadTexture(☃, ☃)) {
         this.listTickables.add(☃);
         return true;
      } else {
         return false;
      }
   }

   public boolean loadTexture(ResourceLocation var1, final ITextureObject var2) {
      boolean ☃ = true;

      try {
         ☃.loadTexture(this.resourceManager);
      } catch (IOException var8) {
         if (☃ != RESOURCE_LOCATION_EMPTY) {
            LOGGER.warn("Failed to load texture: {}", ☃, var8);
         }

         ☃ = TextureUtil.MISSING_TEXTURE;
         this.mapTextureObjects.put(☃, ☃);
         ☃ = false;
      } catch (Throwable var9) {
         CrashReport ☃x = CrashReport.makeCrashReport(var9, "Registering texture");
         CrashReportCategory ☃xx = ☃x.makeCategory("Resource location being registered");
         ☃xx.addCrashSection("Resource location", ☃);
         ☃xx.addDetail("Texture object class", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return ☃.getClass().getName();
            }
         });
         throw new ReportedException(☃x);
      }

      this.mapTextureObjects.put(☃, ☃);
      return ☃;
   }

   public ITextureObject getTexture(ResourceLocation var1) {
      return this.mapTextureObjects.get(☃);
   }

   public ResourceLocation getDynamicTextureLocation(String var1, DynamicTexture var2) {
      Integer ☃ = this.mapTextureCounters.get(☃);
      if (☃ == null) {
         ☃ = 1;
      } else {
         ☃ = ☃ + 1;
      }

      this.mapTextureCounters.put(☃, ☃);
      ResourceLocation ☃x = new ResourceLocation(String.format("dynamic/%s_%d", ☃, ☃));
      this.loadTexture(☃x, ☃);
      return ☃x;
   }

   @Override
   public void tick() {
      for (ITickable ☃ : this.listTickables) {
         ☃.tick();
      }
   }

   public void deleteTexture(ResourceLocation var1) {
      ITextureObject ☃ = this.getTexture(☃);
      if (☃ != null) {
         TextureUtil.deleteTexture(☃.getGlTextureId());
      }
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      Iterator<Entry<ResourceLocation, ITextureObject>> ☃ = this.mapTextureObjects.entrySet().iterator();

      while (☃.hasNext()) {
         Entry<ResourceLocation, ITextureObject> ☃x = ☃.next();
         ITextureObject ☃xx = ☃x.getValue();
         if (☃xx == TextureUtil.MISSING_TEXTURE) {
            ☃.remove();
         } else {
            this.loadTexture(☃x.getKey(), ☃xx);
         }
      }
   }
}
