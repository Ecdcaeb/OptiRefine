package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonBlendingMode;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ShaderDefault DEFAULT_SHADER_UNIFORM = new ShaderDefault();
   private static ShaderManager staticShaderManager;
   private static int currentProgram = -1;
   private static boolean lastCull = true;
   private final Map<String, Object> shaderSamplers = Maps.newHashMap();
   private final List<String> samplerNames = Lists.newArrayList();
   private final List<Integer> shaderSamplerLocations = Lists.newArrayList();
   private final List<ShaderUniform> shaderUniforms = Lists.newArrayList();
   private final List<Integer> shaderUniformLocations = Lists.newArrayList();
   private final Map<String, ShaderUniform> mappedShaderUniforms = Maps.newHashMap();
   private final int program;
   private final String programFilename;
   private final boolean useFaceCulling;
   private boolean isDirty;
   private final JsonBlendingMode blendingMode;
   private final List<Integer> attribLocations;
   private final List<String> attributes;
   private final ShaderLoader vertexShaderLoader;
   private final ShaderLoader fragmentShaderLoader;

   public ShaderManager(IResourceManager var1, String var2) throws IOException {
      JsonParser ☃ = new JsonParser();
      ResourceLocation ☃x = new ResourceLocation("shaders/program/" + ☃ + ".json");
      this.programFilename = ☃;
      IResource ☃xx = null;

      try {
         ☃xx = ☃.getResource(☃x);
         JsonObject ☃xxx = ☃.parse(IOUtils.toString(☃xx.getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
         String ☃xxxx = JsonUtils.getString(☃xxx, "vertex");
         String ☃xxxxx = JsonUtils.getString(☃xxx, "fragment");
         JsonArray ☃xxxxxx = JsonUtils.getJsonArray(☃xxx, "samplers", null);
         if (☃xxxxxx != null) {
            int ☃xxxxxxx = 0;

            for (JsonElement ☃xxxxxxxx : ☃xxxxxx) {
               try {
                  this.parseSampler(☃xxxxxxxx);
               } catch (Exception var25) {
                  JsonException ☃xxxxxxxxx = JsonException.forException(var25);
                  ☃xxxxxxxxx.prependJsonKey("samplers[" + ☃xxxxxxx + "]");
                  throw ☃xxxxxxxxx;
               }

               ☃xxxxxxx++;
            }
         }

         JsonArray ☃xxxxxxx = JsonUtils.getJsonArray(☃xxx, "attributes", null);
         if (☃xxxxxxx != null) {
            int ☃xxxxxxxx = 0;
            this.attribLocations = Lists.newArrayListWithCapacity(☃xxxxxxx.size());
            this.attributes = Lists.newArrayListWithCapacity(☃xxxxxxx.size());

            for (JsonElement ☃xxxxxxxxx : ☃xxxxxxx) {
               try {
                  this.attributes.add(JsonUtils.getString(☃xxxxxxxxx, "attribute"));
               } catch (Exception var24) {
                  JsonException ☃xxxxxxxxxx = JsonException.forException(var24);
                  ☃xxxxxxxxxx.prependJsonKey("attributes[" + ☃xxxxxxxx + "]");
                  throw ☃xxxxxxxxxx;
               }

               ☃xxxxxxxx++;
            }
         } else {
            this.attribLocations = null;
            this.attributes = null;
         }

         JsonArray ☃xxxxxxxx = JsonUtils.getJsonArray(☃xxx, "uniforms", null);
         if (☃xxxxxxxx != null) {
            int ☃xxxxxxxxx = 0;

            for (JsonElement ☃xxxxxxxxxx : ☃xxxxxxxx) {
               try {
                  this.parseUniform(☃xxxxxxxxxx);
               } catch (Exception var23) {
                  JsonException ☃xxxxxxxxxxx = JsonException.forException(var23);
                  ☃xxxxxxxxxxx.prependJsonKey("uniforms[" + ☃xxxxxxxxx + "]");
                  throw ☃xxxxxxxxxxx;
               }

               ☃xxxxxxxxx++;
            }
         }

         this.blendingMode = JsonBlendingMode.parseBlendNode(JsonUtils.getJsonObject(☃xxx, "blend", null));
         this.useFaceCulling = JsonUtils.getBoolean(☃xxx, "cull", true);
         this.vertexShaderLoader = ShaderLoader.loadShader(☃, ShaderLoader.ShaderType.VERTEX, ☃xxxx);
         this.fragmentShaderLoader = ShaderLoader.loadShader(☃, ShaderLoader.ShaderType.FRAGMENT, ☃xxxxx);
         this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
         ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
         this.setupUniforms();
         if (this.attributes != null) {
            for (String ☃xxxxxxxxx : this.attributes) {
               int ☃xxxxxxxxxx = OpenGlHelper.glGetAttribLocation(this.program, ☃xxxxxxxxx);
               this.attribLocations.add(☃xxxxxxxxxx);
            }
         }
      } catch (Exception var26) {
         JsonException ☃xxxxxxxxx = JsonException.forException(var26);
         ☃xxxxxxxxx.setFilenameAndFlush(☃x.getPath());
         throw ☃xxxxxxxxx;
      } finally {
         IOUtils.closeQuietly(☃xx);
      }

      this.markDirty();
   }

   public void deleteShader() {
      ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
   }

   public void endShader() {
      OpenGlHelper.glUseProgram(0);
      currentProgram = -1;
      staticShaderManager = null;
      lastCull = true;

      for (int ☃ = 0; ☃ < this.shaderSamplerLocations.size(); ☃++) {
         if (this.shaderSamplers.get(this.samplerNames.get(☃)) != null) {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + ☃);
            GlStateManager.bindTexture(0);
         }
      }
   }

   public void useShader() {
      this.isDirty = false;
      staticShaderManager = this;
      this.blendingMode.apply();
      if (this.program != currentProgram) {
         OpenGlHelper.glUseProgram(this.program);
         currentProgram = this.program;
      }

      if (this.useFaceCulling) {
         GlStateManager.enableCull();
      } else {
         GlStateManager.disableCull();
      }

      for (int ☃ = 0; ☃ < this.shaderSamplerLocations.size(); ☃++) {
         if (this.shaderSamplers.get(this.samplerNames.get(☃)) != null) {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + ☃);
            GlStateManager.enableTexture2D();
            Object ☃x = this.shaderSamplers.get(this.samplerNames.get(☃));
            int ☃xx = -1;
            if (☃x instanceof Framebuffer) {
               ☃xx = ((Framebuffer)☃x).framebufferTexture;
            } else if (☃x instanceof ITextureObject) {
               ☃xx = ((ITextureObject)☃x).getGlTextureId();
            } else if (☃x instanceof Integer) {
               ☃xx = (Integer)☃x;
            }

            if (☃xx != -1) {
               GlStateManager.bindTexture(☃xx);
               OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, this.samplerNames.get(☃)), ☃);
            }
         }
      }

      for (ShaderUniform ☃xxx : this.shaderUniforms) {
         ☃xxx.upload();
      }
   }

   public void markDirty() {
      this.isDirty = true;
   }

   @Nullable
   public ShaderUniform getShaderUniform(String var1) {
      return this.mappedShaderUniforms.get(☃);
   }

   public ShaderUniform getShaderUniformOrDefault(String var1) {
      ShaderUniform ☃ = this.getShaderUniform(☃);
      return (ShaderUniform)(☃ == null ? DEFAULT_SHADER_UNIFORM : ☃);
   }

   private void setupUniforms() {
      int ☃ = 0;

      for (int ☃x = 0; ☃ < this.samplerNames.size(); ☃x++) {
         String ☃xx = this.samplerNames.get(☃);
         int ☃xxx = OpenGlHelper.glGetUniformLocation(this.program, ☃xx);
         if (☃xxx == -1) {
            LOGGER.warn("Shader {}could not find sampler named {} in the specified shader program.", this.programFilename, ☃xx);
            this.shaderSamplers.remove(☃xx);
            this.samplerNames.remove(☃x);
            ☃x--;
         } else {
            this.shaderSamplerLocations.add(☃xxx);
         }

         ☃++;
      }

      for (ShaderUniform ☃x : this.shaderUniforms) {
         String ☃xx = ☃x.getShaderName();
         int ☃xxx = OpenGlHelper.glGetUniformLocation(this.program, ☃xx);
         if (☃xxx == -1) {
            LOGGER.warn("Could not find uniform named {} in the specified shader program.", ☃xx);
         } else {
            this.shaderUniformLocations.add(☃xxx);
            ☃x.setUniformLocation(☃xxx);
            this.mappedShaderUniforms.put(☃xx, ☃x);
         }
      }
   }

   private void parseSampler(JsonElement var1) {
      JsonObject ☃ = JsonUtils.getJsonObject(☃, "sampler");
      String ☃x = JsonUtils.getString(☃, "name");
      if (!JsonUtils.isString(☃, "file")) {
         this.shaderSamplers.put(☃x, null);
         this.samplerNames.add(☃x);
      } else {
         this.samplerNames.add(☃x);
      }
   }

   public void addSamplerTexture(String var1, Object var2) {
      if (this.shaderSamplers.containsKey(☃)) {
         this.shaderSamplers.remove(☃);
      }

      this.shaderSamplers.put(☃, ☃);
      this.markDirty();
   }

   private void parseUniform(JsonElement var1) throws JsonException {
      JsonObject ☃ = JsonUtils.getJsonObject(☃, "uniform");
      String ☃x = JsonUtils.getString(☃, "name");
      int ☃xx = ShaderUniform.parseType(JsonUtils.getString(☃, "type"));
      int ☃xxx = JsonUtils.getInt(☃, "count");
      float[] ☃xxxx = new float[Math.max(☃xxx, 16)];
      JsonArray ☃xxxxx = JsonUtils.getJsonArray(☃, "values");
      if (☃xxxxx.size() != ☃xxx && ☃xxxxx.size() > 1) {
         throw new JsonException("Invalid amount of values specified (expected " + ☃xxx + ", found " + ☃xxxxx.size() + ")");
      } else {
         int ☃xxxxxx = 0;

         for (JsonElement ☃xxxxxxx : ☃xxxxx) {
            try {
               ☃xxxx[☃xxxxxx] = JsonUtils.getFloat(☃xxxxxxx, "value");
            } catch (Exception var13) {
               JsonException ☃xxxxxxxx = JsonException.forException(var13);
               ☃xxxxxxxx.prependJsonKey("values[" + ☃xxxxxx + "]");
               throw ☃xxxxxxxx;
            }

            ☃xxxxxx++;
         }

         if (☃xxx > 1 && ☃xxxxx.size() == 1) {
            while (☃xxxxxx < ☃xxx) {
               ☃xxxx[☃xxxxxx] = ☃xxxx[0];
               ☃xxxxxx++;
            }
         }

         int ☃xxxxxxx = ☃xxx > 1 && ☃xxx <= 4 && ☃xx < 8 ? ☃xxx - 1 : 0;
         ShaderUniform ☃xxxxxxxx = new ShaderUniform(☃x, ☃xx + ☃xxxxxxx, ☃xxx, this);
         if (☃xx <= 3) {
            ☃xxxxxxxx.set((int)☃xxxx[0], (int)☃xxxx[1], (int)☃xxxx[2], (int)☃xxxx[3]);
         } else if (☃xx <= 7) {
            ☃xxxxxxxx.setSafe(☃xxxx[0], ☃xxxx[1], ☃xxxx[2], ☃xxxx[3]);
         } else {
            ☃xxxxxxxx.set(☃xxxx);
         }

         this.shaderUniforms.add(☃xxxxxxxx);
      }
   }

   public ShaderLoader getVertexShaderLoader() {
      return this.vertexShaderLoader;
   }

   public ShaderLoader getFragmentShaderLoader() {
      return this.fragmentShaderLoader;
   }

   public int getProgram() {
      return this.program;
   }
}
