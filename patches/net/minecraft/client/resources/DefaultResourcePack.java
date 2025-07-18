package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class DefaultResourcePack implements IResourcePack {
   public static final Set<String> DEFAULT_RESOURCE_DOMAINS = ImmutableSet.of("minecraft", "realms");
   private final ResourceIndex resourceIndex;

   public DefaultResourcePack(ResourceIndex var1) {
      this.resourceIndex = ☃;
   }

   @Override
   public InputStream getInputStream(ResourceLocation var1) throws IOException {
      InputStream ☃ = this.getInputStreamAssets(☃);
      if (☃ != null) {
         return ☃;
      } else {
         InputStream ☃x = this.getResourceStream(☃);
         if (☃x != null) {
            return ☃x;
         } else {
            throw new FileNotFoundException(☃.getPath());
         }
      }
   }

   @Nullable
   public InputStream getInputStreamAssets(ResourceLocation var1) throws FileNotFoundException {
      File ☃ = this.resourceIndex.getFile(☃);
      return ☃ != null && ☃.isFile() ? new FileInputStream(☃) : null;
   }

   @Nullable
   private InputStream getResourceStream(ResourceLocation var1) {
      String ☃ = "/assets/" + ☃.getNamespace() + "/" + ☃.getPath();

      try {
         URL ☃x = DefaultResourcePack.class.getResource(☃);
         return ☃x != null && FolderResourcePack.validatePath(new File(☃x.getFile()), ☃) ? DefaultResourcePack.class.getResourceAsStream(☃) : null;
      } catch (IOException var4) {
         return DefaultResourcePack.class.getResourceAsStream(☃);
      }
   }

   @Override
   public boolean resourceExists(ResourceLocation var1) {
      return this.getResourceStream(☃) != null || this.resourceIndex.isFileExisting(☃);
   }

   @Override
   public Set<String> getResourceDomains() {
      return DEFAULT_RESOURCE_DOMAINS;
   }

   @Nullable
   @Override
   public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer var1, String var2) throws IOException {
      try {
         InputStream ☃ = new FileInputStream(this.resourceIndex.getPackMcmeta());
         return AbstractResourcePack.readMetadata(☃, ☃, ☃);
      } catch (RuntimeException var4) {
         return null;
      } catch (FileNotFoundException var5) {
         return null;
      }
   }

   @Override
   public BufferedImage getPackImage() throws IOException {
      return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getPath()));
   }

   @Override
   public String getPackName() {
      return "Default";
   }
}
