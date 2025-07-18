package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureManager implements ITickable, IResourceManagerReloadListener {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
   private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
   private final List<ITickable> listTickables = Lists.newArrayList();
   private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
   private final IResourceManager resourceManager;
   private ITextureObject boundTexture;
   private ResourceLocation boundTextureLocation;

   public TextureManager(IResourceManager resourceManager) {
      this.resourceManager = resourceManager;
   }

   public void bindTexture(ResourceLocation resource) {
      if (Config.isRandomEntities()) {
         resource = RandomEntities.getTextureLocation(resource);
      }

      if (Config.isCustomGuis()) {
         resource = CustomGuis.getTextureLocation(resource);
      }

      ITextureObject itextureobject = this.mapTextureObjects.get(resource);
      if (EmissiveTextures.isActive()) {
         itextureobject = EmissiveTextures.getEmissiveTexture(itextureobject, this.mapTextureObjects);
      }

      if (itextureobject == null) {
         itextureobject = new SimpleTexture(resource);
         this.loadTexture(resource, itextureobject);
      }

      if (Config.isShaders()) {
         ShadersTex.bindTexture(itextureobject);
      } else {
         TextureUtil.bindTexture(itextureobject.getGlTextureId());
      }

      this.boundTexture = itextureobject;
      this.boundTextureLocation = resource;
   }

   public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
      if (this.loadTexture(textureLocation, textureObj)) {
         this.listTickables.add(textureObj);
         return true;
      } else {
         return false;
      }
   }

   public boolean loadTexture(ResourceLocation textureLocation, final ITextureObject textureObj) {
      boolean flag = true;

      try {
         textureObj.loadTexture(this.resourceManager);
      } catch (IOException var8) {
         if (textureLocation != RESOURCE_LOCATION_EMPTY) {
            LOGGER.warn("Failed to load texture: {}", textureLocation, var8);
         }

         textureObj = TextureUtil.MISSING_TEXTURE;
         this.mapTextureObjects.put(textureLocation, textureObj);
         flag = false;
      } catch (Throwable var9) {
         CrashReport crashreport = CrashReport.makeCrashReport(var9, "Registering texture");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
         crashreportcategory.addCrashSection("Resource location", textureLocation);
         crashreportcategory.addDetail("Texture object class", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return textureObj.getClass().getName();
            }
         });
         throw new ReportedException(crashreport);
      }

      this.mapTextureObjects.put(textureLocation, textureObj);
      return flag;
   }

   public ITextureObject getTexture(ResourceLocation textureLocation) {
      return this.mapTextureObjects.get(textureLocation);
   }

   public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
      if (name.equals("logo")) {
         texture = Config.getMojangLogoTexture(texture);
      }

      Integer integer = this.mapTextureCounters.get(name);
      if (integer == null) {
         integer = 1;
      } else {
         integer = integer + 1;
      }

      this.mapTextureCounters.put(name, integer);
      ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
      this.loadTexture(resourcelocation, texture);
      return resourcelocation;
   }

   public void tick() {
      for (ITickable itickable : this.listTickables) {
         itickable.tick();
      }
   }

   public void deleteTexture(ResourceLocation textureLocation) {
      ITextureObject itextureobject = this.getTexture(textureLocation);
      if (itextureobject != null) {
         this.mapTextureObjects.remove(textureLocation);
         TextureUtil.deleteTexture(itextureobject.getGlTextureId());
      }
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      Config.dbg("*** Reloading textures ***");
      Config.log("Resource packs: " + Config.getResourcePackNames());
      Iterator it = this.mapTextureObjects.keySet().iterator();

      while (it.hasNext()) {
         ResourceLocation loc = (ResourceLocation)it.next();
         String path = loc.getPath();
         if (path.startsWith("mcpatcher/") || path.startsWith("optifine/") || EmissiveTextures.isEmissive(loc)) {
            ITextureObject tex = this.mapTextureObjects.get(loc);
            if (tex instanceof AbstractTexture) {
               AbstractTexture at = (AbstractTexture)tex;
               at.deleteGlTexture();
            }

            it.remove();
         }
      }

      EmissiveTextures.update();
      Set<Entry<ResourceLocation, ITextureObject>> entries = new HashSet<>(this.mapTextureObjects.entrySet());
      Iterator<Entry<ResourceLocation, ITextureObject>> iterator = entries.iterator();

      while (iterator.hasNext()) {
         Entry<ResourceLocation, ITextureObject> entry = iterator.next();
         ITextureObject itextureobject = entry.getValue();
         if (itextureobject == TextureUtil.MISSING_TEXTURE) {
            iterator.remove();
         } else {
            this.loadTexture(entry.getKey(), itextureobject);
         }
      }
   }

   public void reloadBannerTextures() {
      for (Entry<ResourceLocation, ITextureObject> entry : new HashSet<>(this.mapTextureObjects.entrySet())) {
         ResourceLocation loc = entry.getKey();
         ITextureObject tex = entry.getValue();
         if (tex instanceof LayeredColorMaskTexture) {
            this.loadTexture(loc, tex);
         }
      }
   }

   public ITextureObject getBoundTexture() {
      return this.boundTexture;
   }

   public ResourceLocation getBoundTextureLocation() {
      return this.boundTextureLocation;
   }
}
