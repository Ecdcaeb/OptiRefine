package net.minecraft.client.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractResourcePack implements IResourcePack {
   private static final Logger LOGGER = LogManager.getLogger();
   protected final File resourcePackFile;

   public AbstractResourcePack(File var1) {
      this.resourcePackFile = ☃;
   }

   private static String locationToName(ResourceLocation var0) {
      return String.format("%s/%s/%s", "assets", ☃.getNamespace(), ☃.getPath());
   }

   protected static String getRelativeName(File var0, File var1) {
      return ☃.toURI().relativize(☃.toURI()).getPath();
   }

   @Override
   public InputStream getInputStream(ResourceLocation var1) throws IOException {
      return this.getInputStreamByName(locationToName(☃));
   }

   @Override
   public boolean resourceExists(ResourceLocation var1) {
      return this.hasResourceName(locationToName(☃));
   }

   protected abstract InputStream getInputStreamByName(String var1) throws IOException;

   protected abstract boolean hasResourceName(String var1);

   protected void logNameNotLowercase(String var1) {
      LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", ☃, this.resourcePackFile);
   }

   @Override
   public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer var1, String var2) throws IOException {
      return readMetadata(☃, this.getInputStreamByName("pack.mcmeta"), ☃);
   }

   static <T extends IMetadataSection> T readMetadata(MetadataSerializer var0, InputStream var1, String var2) {
      JsonObject ☃ = null;
      BufferedReader ☃x = null;

      try {
         ☃x = new BufferedReader(new InputStreamReader(☃, StandardCharsets.UTF_8));
         ☃ = new JsonParser().parse(☃x).getAsJsonObject();
      } catch (RuntimeException var9) {
         throw new JsonParseException(var9);
      } finally {
         IOUtils.closeQuietly(☃x);
      }

      return ☃.parseMetadataSection(☃, ☃);
   }

   @Override
   public BufferedImage getPackImage() throws IOException {
      return TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
   }

   @Override
   public String getPackName() {
      return this.resourcePackFile.getName();
   }
}
