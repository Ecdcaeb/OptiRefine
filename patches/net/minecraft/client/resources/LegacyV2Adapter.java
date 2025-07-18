package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class LegacyV2Adapter implements IResourcePack {
   private final IResourcePack pack;

   public LegacyV2Adapter(IResourcePack var1) {
      this.pack = ☃;
   }

   @Override
   public InputStream getInputStream(ResourceLocation var1) throws IOException {
      return this.pack.getInputStream(this.fudgePath(☃));
   }

   private ResourceLocation fudgePath(ResourceLocation var1) {
      String ☃ = ☃.getPath();
      if (!"lang/swg_de.lang".equals(☃) && ☃.startsWith("lang/") && ☃.endsWith(".lang")) {
         int ☃x = ☃.indexOf(95);
         if (☃x != -1) {
            final String ☃xx = ☃.substring(0, ☃x + 1) + ☃.substring(☃x + 1, ☃.indexOf(46, ☃x)).toUpperCase() + ".lang";
            return new ResourceLocation(☃.getNamespace(), "") {
               @Override
               public String getPath() {
                  return ☃;
               }
            };
         }
      }

      return ☃;
   }

   @Override
   public boolean resourceExists(ResourceLocation var1) {
      return this.pack.resourceExists(this.fudgePath(☃));
   }

   @Override
   public Set<String> getResourceDomains() {
      return this.pack.getResourceDomains();
   }

   @Nullable
   @Override
   public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer var1, String var2) throws IOException {
      return this.pack.getPackMetadata(☃, ☃);
   }

   @Override
   public BufferedImage getPackImage() throws IOException {
      return this.pack.getPackImage();
   }

   @Override
   public String getPackName() {
      return this.pack.getPackName();
   }
}
