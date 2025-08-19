package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.OptifineHelper;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.io.InputStream;

@Mixin(DefaultResourcePack.class)
public abstract class MixinDefaultResourcePack {
    @SuppressWarnings("unused")
    @Unique
    private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;


    @WrapMethod(method = "getResourceStream")
    public InputStream injectGetResourceStream(ResourceLocation location, Operation<InputStream> original){
        InputStream is = OptifineHelper.getOptifineResource("/assets/" + location.getNamespace() + "/" + location.getPath());
        if (is == null) return original.call(location);
        else return is;
    }


}
/*
--- net/minecraft/client/resources/DefaultResourcePack.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/resources/DefaultResourcePack.java	Tue Aug 19 14:59:58 2025
@@ -11,16 +11,19 @@
 import java.util.Set;
 import javax.annotation.Nullable;
 import net.minecraft.client.renderer.texture.TextureUtil;
 import net.minecraft.client.resources.data.IMetadataSection;
 import net.minecraft.client.resources.data.MetadataSerializer;
 import net.minecraft.util.ResourceLocation;
+import net.minecraft.util.Util;
+import net.optifine.reflect.ReflectorForge;

 public class DefaultResourcePack implements IResourcePack {
    public static final Set<String> DEFAULT_RESOURCE_DOMAINS = ImmutableSet.of("minecraft", "realms");
    private final ResourceIndex resourceIndex;
+   private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;

    public DefaultResourcePack(ResourceIndex var1) {
       this.resourceIndex = var1;
    }

    public InputStream getInputStream(ResourceLocation var1) throws IOException {
@@ -35,26 +38,30 @@
             throw new FileNotFoundException(var1.getPath());
          }
       }
    }

    @Nullable
-   public InputStream getInputStreamAssets(ResourceLocation var1) throws FileNotFoundException {
+   public InputStream getInputStreamAssets(ResourceLocation var1) throws IOException, FileNotFoundException {
       File var2 = this.resourceIndex.getFile(var1);
       return var2 != null && var2.isFile() ? new FileInputStream(var2) : null;
    }

    @Nullable
    private InputStream getResourceStream(ResourceLocation var1) {
       String var2 = "/assets/" + var1.getNamespace() + "/" + var1.getPath();
-
-      try {
-         URL var3 = DefaultResourcePack.class.getResource(var2);
-         return var3 != null && FolderResourcePack.validatePath(new File(var3.getFile()), var2) ? DefaultResourcePack.class.getResourceAsStream(var2) : null;
-      } catch (IOException var4) {
-         return DefaultResourcePack.class.getResourceAsStream(var2);
+      InputStream var3 = ReflectorForge.getOptiFineResourceStream(var2);
+      if (var3 != null) {
+         return var3;
+      } else {
+         try {
+            URL var4 = DefaultResourcePack.class.getResource(var2);
+            return var4 != null && this.validatePath(new File(var4.getFile()), var2) ? DefaultResourcePack.class.getResourceAsStream(var2) : null;
+         } catch (IOException var5) {
+            return DefaultResourcePack.class.getResourceAsStream(var2);
+         }
       }
    }

    public boolean resourceExists(ResourceLocation var1) {
       return this.getResourceStream(var1) != null || this.resourceIndex.isFileExisting(var1);
    }
@@ -78,8 +85,21 @@
    public BufferedImage getPackImage() throws IOException {
       return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getPath()));
    }

    public String getPackName() {
       return "Default";
+   }
+
+   private boolean validatePath(File var1, String var2) throws IOException {
+      String var3 = var1.getPath();
+      if (var3.startsWith("file:")) {
+         if (ON_WINDOWS) {
+            var3 = var3.replace("\\", "/");
+         }
+
+         return var3.endsWith(var2);
+      } else {
+         return FolderResourcePack.validatePath(var1, var2);
+      }
    }
 }
 */