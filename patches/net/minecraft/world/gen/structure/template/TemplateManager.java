package net.minecraft.world.gen.structure.template;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import org.apache.commons.io.IOUtils;

public class TemplateManager {
   private final Map<String, Template> templates = Maps.newHashMap();
   private final String baseFolder;
   private final DataFixer fixer;

   public TemplateManager(String var1, DataFixer var2) {
      this.baseFolder = ☃;
      this.fixer = ☃;
   }

   public Template getTemplate(@Nullable MinecraftServer var1, ResourceLocation var2) {
      Template ☃ = this.get(☃, ☃);
      if (☃ == null) {
         ☃ = new Template();
         this.templates.put(☃.getPath(), ☃);
      }

      return ☃;
   }

   @Nullable
   public Template get(@Nullable MinecraftServer var1, ResourceLocation var2) {
      String ☃ = ☃.getPath();
      if (this.templates.containsKey(☃)) {
         return this.templates.get(☃);
      } else {
         if (☃ == null) {
            this.readTemplateFromJar(☃);
         } else {
            this.readTemplate(☃);
         }

         return this.templates.containsKey(☃) ? this.templates.get(☃) : null;
      }
   }

   public boolean readTemplate(ResourceLocation var1) {
      String ☃ = ☃.getPath();
      File ☃x = new File(this.baseFolder, ☃ + ".nbt");
      if (!☃x.exists()) {
         return this.readTemplateFromJar(☃);
      } else {
         InputStream ☃xx = null;

         boolean var6;
         try {
            ☃xx = new FileInputStream(☃x);
            this.readTemplateFromStream(☃, ☃xx);
            return true;
         } catch (Throwable var10) {
            var6 = false;
         } finally {
            IOUtils.closeQuietly(☃xx);
         }

         return var6;
      }
   }

   private boolean readTemplateFromJar(ResourceLocation var1) {
      String ☃ = ☃.getNamespace();
      String ☃x = ☃.getPath();
      InputStream ☃xx = null;

      boolean var6;
      try {
         ☃xx = MinecraftServer.class.getResourceAsStream("/assets/" + ☃ + "/structures/" + ☃x + ".nbt");
         this.readTemplateFromStream(☃x, ☃xx);
         return true;
      } catch (Throwable var10) {
         var6 = false;
      } finally {
         IOUtils.closeQuietly(☃xx);
      }

      return var6;
   }

   private void readTemplateFromStream(String var1, InputStream var2) throws IOException {
      NBTTagCompound ☃ = CompressedStreamTools.readCompressed(☃);
      if (!☃.hasKey("DataVersion", 99)) {
         ☃.setInteger("DataVersion", 500);
      }

      Template ☃x = new Template();
      ☃x.read(this.fixer.process(FixTypes.STRUCTURE, ☃));
      this.templates.put(☃, ☃x);
   }

   public boolean writeTemplate(@Nullable MinecraftServer var1, ResourceLocation var2) {
      String ☃ = ☃.getPath();
      if (☃ != null && this.templates.containsKey(☃)) {
         File ☃x = new File(this.baseFolder);
         if (!☃x.exists()) {
            if (!☃x.mkdirs()) {
               return false;
            }
         } else if (!☃x.isDirectory()) {
            return false;
         }

         File ☃xx = new File(☃x, ☃ + ".nbt");
         Template ☃xxx = this.templates.get(☃);
         OutputStream ☃xxxx = null;

         boolean var9;
         try {
            NBTTagCompound ☃xxxxx = ☃xxx.writeToNBT(new NBTTagCompound());
            ☃xxxx = new FileOutputStream(☃xx);
            CompressedStreamTools.writeCompressed(☃xxxxx, ☃xxxx);
            return true;
         } catch (Throwable var13) {
            var9 = false;
         } finally {
            IOUtils.closeQuietly(☃xxxx);
         }

         return var9;
      } else {
         return false;
      }
   }

   public void remove(ResourceLocation var1) {
      this.templates.remove(☃.getPath());
   }
}
