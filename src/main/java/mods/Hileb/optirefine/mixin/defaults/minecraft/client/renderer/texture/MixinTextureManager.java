package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(TextureManager.class)
public abstract class MixinTextureManager {
    @Unique
    private ITextureObject boundTexture;
    @Unique
    private ResourceLocation boundTextureLocation;

    @WrapMethod(method = "bindTexture")
    public void beforeBindTexture(ResourceLocation resource, Operation<Void> original){
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation(resource);
        }

        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation(resource);
        }
        original.call(resource);
    }

    @WrapOperation(method = "bindTexture", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object processTexture(Map<ResourceLocation, ITextureObject> instance, Object o, Operation<ITextureObject> original){
        ITextureObject iTextureObject = original.call(instance, o);
        if (EmissiveTextures.isActive()) {
            iTextureObject = EmissiveTextures.getEmissiveTexture(iTextureObject, instance);
        }
        return iTextureObject;
    }

    @WrapOperation(method = "bindTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;bindTexture(I)V"))
    public void modifyBindTexture(int p_94277_0_, Operation<Void> original, @Local(ordinal = 0) ITextureObject iTextureObject, @Local(argsOnly = true) ResourceLocation resource){
        if (Config.isShaders()) {
            ShadersTex.bindTexture(iTextureObject);
        } else {
            original.call(p_94277_0_);
        }

        this.boundTexture = iTextureObject;
        this.boundTextureLocation = resource;
    }

    @WrapMethod(method = "getDynamicTextureLocation")
    public ResourceLocation mojangLogo(String name, DynamicTexture texture, Operation<ResourceLocation> original){
        if ("logo".equals(name)) {
            texture = Config.getMojangLogoTexture(texture);
        }
        return original.call(name, texture);
    }

    @Shadow @Final
    private Map<ResourceLocation, ITextureObject> mapTextureObjects;

    @Unique
    public void reloadBannerTextures() {
        for (Map.Entry<ResourceLocation, ITextureObject> entry : ImmutableSet.copyOf(this.mapTextureObjects.entrySet())) {
            ResourceLocation loc = entry.getKey();
            ITextureObject tex = entry.getValue();
            if (tex instanceof LayeredColorMaskTexture) {
                this.loadTexture(loc, tex);
            }
        }
    }

    @Shadow
    public abstract boolean loadTexture(ResourceLocation textureLocation, final ITextureObject textureObj);

    @Unique
    public ITextureObject getBoundTexture() {
        return this.boundTexture;
    }

    @Unique
    public ResourceLocation getBoundTextureLocation() {
        return this.boundTextureLocation;
    }

    /**
     * @author
     * @reason
     */
    @WrapMethod(method = "onResourceManagerReload")
    public void removeOptifineDynamicTexture(IResourceManager resourceManager, Operation<Void> original) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());

        final ProgressManager.ProgressBar bar = ProgressManager.push("Excluding Optifine Texture Manager", this.mapTextureObjects.size(), true);

        this.mapTextureObjects.entrySet().removeIf(
                (entry) -> {
                    var location = entry.getKey();
                    var tex = entry.getValue();
                    if (StringUtils.startsWithAny(location.getPath(), "mcpatcher/", "optifine/")
                            || EmissiveTextures.isEmissive(location)) {
                        if (tex instanceof AbstractTexture at) {
                            at.deleteGlTexture();
                        }
                        return true;
                    }
                    bar.step(location.toString());
                    return false;
                }
        );

        ProgressManager.pop(bar);

        EmissiveTextures.update();

        original.call(resourceManager);
    }
}

/*
--- net/minecraft/client/renderer/texture/TextureManager.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/texture/TextureManager.java	Tue Aug 19 14:59:58 2025
@@ -1,45 +1,71 @@
 package net.minecraft.client.renderer.texture;

 import com.google.common.collect.Lists;
 import com.google.common.collect.Maps;
 import java.io.IOException;
+import java.util.HashSet;
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
+import net.optifine.CustomGuis;
+import net.optifine.EmissiveTextures;
+import net.optifine.RandomEntities;
+import net.optifine.shaders.ShadersTex;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class TextureManager implements ITickable, IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickable> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private final IResourceManager resourceManager;
+   private ITextureObject boundTexture;
+   private ResourceLocation boundTextureLocation;

    public TextureManager(IResourceManager var1) {
       this.resourceManager = var1;
    }

    public void bindTexture(ResourceLocation var1) {
+      if (Config.isRandomEntities()) {
+         var1 = RandomEntities.getTextureLocation(var1);
+      }
+
+      if (Config.isCustomGuis()) {
+         var1 = CustomGuis.getTextureLocation(var1);
+      }
+
       Object var2 = this.mapTextureObjects.get(var1);
+      if (EmissiveTextures.isActive()) {
+         var2 = EmissiveTextures.getEmissiveTexture((ITextureObject)var2, this.mapTextureObjects);
+      }
+
       if (var2 == null) {
          var2 = new SimpleTexture(var1);
          this.loadTexture(var1, (ITextureObject)var2);
       }

-      TextureUtil.bindTexture(((ITextureObject)var2).getGlTextureId());
+      if (Config.isShaders()) {
+         ShadersTex.bindTexture((ITextureObject)var2);
+      } else {
+         TextureUtil.bindTexture(((ITextureObject)var2).getGlTextureId());
+      }
+
+      this.boundTexture = (ITextureObject)var2;
+      this.boundTextureLocation = var1;
    }

    public boolean loadTickableTexture(ResourceLocation var1, ITickableTextureObject var2) {
       if (this.loadTexture(var1, var2)) {
          this.listTickables.add(var2);
          return true;
@@ -59,32 +85,36 @@
          }

          var2 = TextureUtil.MISSING_TEXTURE;
          this.mapTextureObjects.put(var1, (ITextureObject)var2);
          var3 = false;
       } catch (Throwable var9) {
-         CrashReport var5 = CrashReport.makeCrashReport(var9, "Registering texture");
-         CrashReportCategory var6 = var5.makeCategory("Resource location being registered");
-         var6.addCrashSection("Resource location", var1);
-         var6.addDetail("Texture object class", new ICrashReportDetail<String>() {
+         CrashReport var6 = CrashReport.makeCrashReport(var9, "Registering texture");
+         CrashReportCategory var7 = var6.makeCategory("Resource location being registered");
+         var7.addCrashSection("Resource location", var1);
+         var7.addDetail("Texture object class", new ICrashReportDetail<String>() {
             public String call() throws Exception {
                return var2.getClass().getName();
             }
          });
-         throw new ReportedException(var5);
+         throw new ReportedException(var6);
       }

       this.mapTextureObjects.put(var1, (ITextureObject)var2);
       return var3;
    }

    public ITextureObject getTexture(ResourceLocation var1) {
       return this.mapTextureObjects.get(var1);
    }

    public ResourceLocation getDynamicTextureLocation(String var1, DynamicTexture var2) {
+      if (var1.equals("logo")) {
+         var2 = Config.getMojangLogoTexture(var2);
+      }
+
       Integer var3 = this.mapTextureCounters.get(var1);
       if (var3 == null) {
          var3 = 1;
       } else {
          var3 = var3 + 1;
       }
@@ -101,24 +131,63 @@
       }
    }

    public void deleteTexture(ResourceLocation var1) {
       ITextureObject var2 = this.getTexture(var1);
       if (var2 != null) {
+         this.mapTextureObjects.remove(var1);
          TextureUtil.deleteTexture(var2.getGlTextureId());
       }
    }

    public void onResourceManagerReload(IResourceManager var1) {
-      Iterator var2 = this.mapTextureObjects.entrySet().iterator();
+      Config.dbg("*** Reloading textures ***");
+      Config.log("Resource packs: " + Config.getResourcePackNames());
+      Iterator var2 = this.mapTextureObjects.keySet().iterator();

       while (var2.hasNext()) {
-         Entry var3 = (Entry)var2.next();
-         ITextureObject var4 = (ITextureObject)var3.getValue();
-         if (var4 == TextureUtil.MISSING_TEXTURE) {
+         ResourceLocation var3 = (ResourceLocation)var2.next();
+         String var4 = var3.getPath();
+         if (var4.startsWith("mcpatcher/") || var4.startsWith("optifine/") || EmissiveTextures.isEmissive(var3)) {
+            ITextureObject var5 = this.mapTextureObjects.get(var3);
+            if (var5 instanceof AbstractTexture) {
+               AbstractTexture var6 = (AbstractTexture)var5;
+               var6.deleteGlTexture();
+            }
+
             var2.remove();
+         }
+      }
+
+      EmissiveTextures.update();
+      HashSet var7 = new HashSet<>(this.mapTextureObjects.entrySet());
+      Iterator var8 = var7.iterator();
+
+      while (var8.hasNext()) {
+         Entry var9 = (Entry)var8.next();
+         ITextureObject var10 = (ITextureObject)var9.getValue();
+         if (var10 == TextureUtil.MISSING_TEXTURE) {
+            var8.remove();
          } else {
-            this.loadTexture((ResourceLocation)var3.getKey(), var4);
+            this.loadTexture((ResourceLocation)var9.getKey(), var10);
+         }
+      }
+   }
+
+   public void reloadBannerTextures() {
+      for (Entry var3 : new HashSet<>(this.mapTextureObjects.entrySet())) {
+         ResourceLocation var4 = (ResourceLocation)var3.getKey();
+         ITextureObject var5 = (ITextureObject)var3.getValue();
+         if (var5 instanceof LayeredColorMaskTexture) {
+            this.loadTexture(var4, var5);
          }
       }
+   }
+
+   public ITextureObject getBoundTexture() {
+      return this.boundTexture;
+   }
+
+   public ResourceLocation getBoundTextureLocation() {
+      return this.boundTextureLocation;
    }
 }
 */
