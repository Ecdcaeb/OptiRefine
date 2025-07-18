package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader {
   private final ShaderLoader.ShaderType shaderType;
   private final String shaderFilename;
   private final int shader;
   private int shaderAttachCount;

   private ShaderLoader(ShaderLoader.ShaderType var1, int var2, String var3) {
      this.shaderType = ☃;
      this.shader = ☃;
      this.shaderFilename = ☃;
   }

   public void attachShader(ShaderManager var1) {
      this.shaderAttachCount++;
      OpenGlHelper.glAttachShader(☃.getProgram(), this.shader);
   }

   public void deleteShader(ShaderManager var1) {
      this.shaderAttachCount--;
      if (this.shaderAttachCount <= 0) {
         OpenGlHelper.glDeleteShader(this.shader);
         this.shaderType.getLoadedShaders().remove(this.shaderFilename);
      }
   }

   public String getShaderFilename() {
      return this.shaderFilename;
   }

   public static ShaderLoader loadShader(IResourceManager var0, ShaderLoader.ShaderType var1, String var2) throws IOException {
      ShaderLoader ☃ = ☃.getLoadedShaders().get(☃);
      if (☃ == null) {
         ResourceLocation ☃x = new ResourceLocation("shaders/program/" + ☃ + ☃.getShaderExtension());
         IResource ☃xx = ☃.getResource(☃x);

         try {
            byte[] ☃xxx = IOUtils.toByteArray(new BufferedInputStream(☃xx.getInputStream()));
            ByteBuffer ☃xxxx = BufferUtils.createByteBuffer(☃xxx.length);
            ☃xxxx.put(☃xxx);
            ((Buffer)☃xxxx).position(0);
            int ☃xxxxx = OpenGlHelper.glCreateShader(☃.getShaderMode());
            OpenGlHelper.glShaderSource(☃xxxxx, ☃xxxx);
            OpenGlHelper.glCompileShader(☃xxxxx);
            if (OpenGlHelper.glGetShaderi(☃xxxxx, OpenGlHelper.GL_COMPILE_STATUS) == 0) {
               String ☃xxxxxx = StringUtils.trim(OpenGlHelper.glGetShaderInfoLog(☃xxxxx, 32768));
               JsonException ☃xxxxxxx = new JsonException("Couldn't compile " + ☃.getShaderName() + " program: " + ☃xxxxxx);
               ☃xxxxxxx.setFilenameAndFlush(☃x.getPath());
               throw ☃xxxxxxx;
            }

            ☃ = new ShaderLoader(☃, ☃xxxxx, ☃);
            ☃.getLoadedShaders().put(☃, ☃);
         } finally {
            IOUtils.closeQuietly(☃xx);
         }
      }

      return ☃;
   }

   public static enum ShaderType {
      VERTEX("vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER),
      FRAGMENT("fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);

      private final String shaderName;
      private final String shaderExtension;
      private final int shaderMode;
      private final Map<String, ShaderLoader> loadedShaders = Maps.newHashMap();

      private ShaderType(String var3, String var4, int var5) {
         this.shaderName = ☃;
         this.shaderExtension = ☃;
         this.shaderMode = ☃;
      }

      public String getShaderName() {
         return this.shaderName;
      }

      private String getShaderExtension() {
         return this.shaderExtension;
      }

      private int getShaderMode() {
         return this.shaderMode;
      }

      private Map<String, ShaderLoader> getLoadedShaders() {
         return this.loadedShaders;
      }
   }
}
