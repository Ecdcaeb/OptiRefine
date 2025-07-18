package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class SimpleResource implements IResource {
   private final Map<String, IMetadataSection> mapMetadataSections = Maps.newHashMap();
   private final String resourcePackName;
   private final ResourceLocation srResourceLocation;
   private final InputStream resourceInputStream;
   private final InputStream mcmetaInputStream;
   private final MetadataSerializer srMetadataSerializer;
   private boolean mcmetaJsonChecked;
   private JsonObject mcmetaJson;

   public SimpleResource(String var1, ResourceLocation var2, InputStream var3, InputStream var4, MetadataSerializer var5) {
      this.resourcePackName = ☃;
      this.srResourceLocation = ☃;
      this.resourceInputStream = ☃;
      this.mcmetaInputStream = ☃;
      this.srMetadataSerializer = ☃;
   }

   @Override
   public ResourceLocation getResourceLocation() {
      return this.srResourceLocation;
   }

   @Override
   public InputStream getInputStream() {
      return this.resourceInputStream;
   }

   @Override
   public boolean hasMetadata() {
      return this.mcmetaInputStream != null;
   }

   @Nullable
   @Override
   public <T extends IMetadataSection> T getMetadata(String var1) {
      if (!this.hasMetadata()) {
         return null;
      } else {
         if (this.mcmetaJson == null && !this.mcmetaJsonChecked) {
            this.mcmetaJsonChecked = true;
            BufferedReader ☃ = null;

            try {
               ☃ = new BufferedReader(new InputStreamReader(this.mcmetaInputStream, StandardCharsets.UTF_8));
               this.mcmetaJson = new JsonParser().parse(☃).getAsJsonObject();
            } finally {
               IOUtils.closeQuietly(☃);
            }
         }

         T ☃ = (T)this.mapMetadataSections.get(☃);
         if (☃ == null) {
            ☃ = this.srMetadataSerializer.parseMetadataSection(☃, this.mcmetaJson);
         }

         return ☃;
      }
   }

   @Override
   public String getResourcePackName() {
      return this.resourcePackName;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof SimpleResource)) {
         return false;
      } else {
         SimpleResource ☃ = (SimpleResource)☃;
         if (this.srResourceLocation != null ? this.srResourceLocation.equals(☃.srResourceLocation) : ☃.srResourceLocation == null) {
            return this.resourcePackName != null ? this.resourcePackName.equals(☃.resourcePackName) : ☃.resourcePackName == null;
         } else {
            return false;
         }
      }
   }

   @Override
   public int hashCode() {
      int ☃ = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
      return 31 * ☃ + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
   }

   @Override
   public void close() throws IOException {
      this.resourceInputStream.close();
      if (this.mcmetaInputStream != null) {
         this.mcmetaInputStream.close();
      }
   }
}
