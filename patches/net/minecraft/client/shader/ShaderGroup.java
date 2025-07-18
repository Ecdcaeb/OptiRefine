package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderGroup {
   private final Framebuffer mainFramebuffer;
   private final IResourceManager resourceManager;
   private final String shaderGroupName;
   private final List<Shader> listShaders = Lists.newArrayList();
   private final Map<String, Framebuffer> mapFramebuffers = Maps.newHashMap();
   private final List<Framebuffer> listFramebuffers = Lists.newArrayList();
   private Matrix4f projectionMatrix;
   private int mainFramebufferWidth;
   private int mainFramebufferHeight;
   private float time;
   private float lastStamp;

   public ShaderGroup(TextureManager var1, IResourceManager var2, Framebuffer var3, ResourceLocation var4) throws IOException, JsonSyntaxException {
      this.resourceManager = ☃;
      this.mainFramebuffer = ☃;
      this.time = 0.0F;
      this.lastStamp = 0.0F;
      this.mainFramebufferWidth = ☃.framebufferWidth;
      this.mainFramebufferHeight = ☃.framebufferHeight;
      this.shaderGroupName = ☃.toString();
      this.resetProjectionMatrix();
      this.parseGroup(☃, ☃);
   }

   public void parseGroup(TextureManager var1, ResourceLocation var2) throws IOException, JsonSyntaxException {
      JsonParser ☃ = new JsonParser();
      IResource ☃x = null;

      try {
         ☃x = this.resourceManager.getResource(☃);
         JsonObject ☃xx = ☃.parse(IOUtils.toString(☃x.getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
         if (JsonUtils.isJsonArray(☃xx, "targets")) {
            JsonArray ☃xxx = ☃xx.getAsJsonArray("targets");
            int ☃xxxx = 0;

            for (JsonElement ☃xxxxx : ☃xxx) {
               try {
                  this.initTarget(☃xxxxx);
               } catch (Exception var18) {
                  JsonException ☃xxxxxx = JsonException.forException(var18);
                  ☃xxxxxx.prependJsonKey("targets[" + ☃xxxx + "]");
                  throw ☃xxxxxx;
               }

               ☃xxxx++;
            }
         }

         if (JsonUtils.isJsonArray(☃xx, "passes")) {
            JsonArray ☃xxx = ☃xx.getAsJsonArray("passes");
            int ☃xxxx = 0;

            for (JsonElement ☃xxxxx : ☃xxx) {
               try {
                  this.parsePass(☃, ☃xxxxx);
               } catch (Exception var17) {
                  JsonException ☃xxxxxx = JsonException.forException(var17);
                  ☃xxxxxx.prependJsonKey("passes[" + ☃xxxx + "]");
                  throw ☃xxxxxx;
               }

               ☃xxxx++;
            }
         }
      } catch (Exception var19) {
         JsonException ☃xxx = JsonException.forException(var19);
         ☃xxx.setFilenameAndFlush(☃.getPath());
         throw ☃xxx;
      } finally {
         IOUtils.closeQuietly(☃x);
      }
   }

   private void initTarget(JsonElement var1) throws JsonException {
      if (JsonUtils.isString(☃)) {
         this.addFramebuffer(☃.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
      } else {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "target");
         String ☃x = JsonUtils.getString(☃, "name");
         int ☃xx = JsonUtils.getInt(☃, "width", this.mainFramebufferWidth);
         int ☃xxx = JsonUtils.getInt(☃, "height", this.mainFramebufferHeight);
         if (this.mapFramebuffers.containsKey(☃x)) {
            throw new JsonException(☃x + " is already defined");
         }

         this.addFramebuffer(☃x, ☃xx, ☃xxx);
      }
   }

   private void parsePass(TextureManager var1, JsonElement var2) throws IOException {
      JsonObject ☃ = JsonUtils.getJsonObject(☃, "pass");
      String ☃x = JsonUtils.getString(☃, "name");
      String ☃xx = JsonUtils.getString(☃, "intarget");
      String ☃xxx = JsonUtils.getString(☃, "outtarget");
      Framebuffer ☃xxxx = this.getFramebuffer(☃xx);
      Framebuffer ☃xxxxx = this.getFramebuffer(☃xxx);
      if (☃xxxx == null) {
         throw new JsonException("Input target '" + ☃xx + "' does not exist");
      } else if (☃xxxxx == null) {
         throw new JsonException("Output target '" + ☃xxx + "' does not exist");
      } else {
         Shader ☃xxxxxx = this.addShader(☃x, ☃xxxx, ☃xxxxx);
         JsonArray ☃xxxxxxx = JsonUtils.getJsonArray(☃, "auxtargets", null);
         if (☃xxxxxxx != null) {
            int ☃xxxxxxxx = 0;

            for (JsonElement ☃xxxxxxxxx : ☃xxxxxxx) {
               try {
                  JsonObject ☃xxxxxxxxxx = JsonUtils.getJsonObject(☃xxxxxxxxx, "auxtarget");
                  String ☃xxxxxxxxxxx = JsonUtils.getString(☃xxxxxxxxxx, "name");
                  String ☃xxxxxxxxxxxx = JsonUtils.getString(☃xxxxxxxxxx, "id");
                  Framebuffer ☃xxxxxxxxxxxxx = this.getFramebuffer(☃xxxxxxxxxxxx);
                  if (☃xxxxxxxxxxxxx == null) {
                     ResourceLocation ☃xxxxxxxxxxxxxx = new ResourceLocation("textures/effect/" + ☃xxxxxxxxxxxx + ".png");
                     IResource ☃xxxxxxxxxxxxxxx = null;

                     try {
                        ☃xxxxxxxxxxxxxxx = this.resourceManager.getResource(☃xxxxxxxxxxxxxx);
                     } catch (FileNotFoundException var29) {
                        throw new JsonException("Render target or texture '" + ☃xxxxxxxxxxxx + "' does not exist");
                     } finally {
                        IOUtils.closeQuietly(☃xxxxxxxxxxxxxxx);
                     }

                     ☃.bindTexture(☃xxxxxxxxxxxxxx);
                     ITextureObject var20 = ☃.getTexture(☃xxxxxxxxxxxxxx);
                     int var21 = JsonUtils.getInt(☃xxxxxxxxxx, "width");
                     int var22 = JsonUtils.getInt(☃xxxxxxxxxx, "height");
                     boolean ☃xxxxxxxxxxxxxxxx = JsonUtils.getBoolean(☃xxxxxxxxxx, "bilinear");
                     if (☃xxxxxxxxxxxxxxxx) {
                        GlStateManager.glTexParameteri(3553, 10241, 9729);
                        GlStateManager.glTexParameteri(3553, 10240, 9729);
                     } else {
                        GlStateManager.glTexParameteri(3553, 10241, 9728);
                        GlStateManager.glTexParameteri(3553, 10240, 9728);
                     }

                     ☃xxxxxx.addAuxFramebuffer(☃xxxxxxxxxxx, var20.getGlTextureId(), var21, var22);
                  } else {
                     ☃xxxxxx.addAuxFramebuffer(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxx.framebufferTextureWidth, ☃xxxxxxxxxxxxx.framebufferTextureHeight);
                  }
               } catch (Exception var31) {
                  JsonException ☃xxxxxxxxxxxxxx = JsonException.forException(var31);
                  ☃xxxxxxxxxxxxxx.prependJsonKey("auxtargets[" + ☃xxxxxxxx + "]");
                  throw ☃xxxxxxxxxxxxxx;
               }

               ☃xxxxxxxx++;
            }
         }

         JsonArray ☃xxxxxxxx = JsonUtils.getJsonArray(☃, "uniforms", null);
         if (☃xxxxxxxx != null) {
            int ☃xxxxxxxxx = 0;

            for (JsonElement ☃xxxxxxxxxx : ☃xxxxxxxx) {
               try {
                  this.initUniform(☃xxxxxxxxxx);
               } catch (Exception var28) {
                  JsonException ☃xxxxxxxxxxx = JsonException.forException(var28);
                  ☃xxxxxxxxxxx.prependJsonKey("uniforms[" + ☃xxxxxxxxx + "]");
                  throw ☃xxxxxxxxxxx;
               }

               ☃xxxxxxxxx++;
            }
         }
      }
   }

   private void initUniform(JsonElement var1) throws JsonException {
      JsonObject ☃ = JsonUtils.getJsonObject(☃, "uniform");
      String ☃x = JsonUtils.getString(☃, "name");
      ShaderUniform ☃xx = this.listShaders.get(this.listShaders.size() - 1).getShaderManager().getShaderUniform(☃x);
      if (☃xx == null) {
         throw new JsonException("Uniform '" + ☃x + "' does not exist");
      } else {
         float[] ☃xxx = new float[4];
         int ☃xxxx = 0;

         for (JsonElement ☃xxxxx : JsonUtils.getJsonArray(☃, "values")) {
            try {
               ☃xxx[☃xxxx] = JsonUtils.getFloat(☃xxxxx, "value");
            } catch (Exception var12) {
               JsonException ☃xxxxxx = JsonException.forException(var12);
               ☃xxxxxx.prependJsonKey("values[" + ☃xxxx + "]");
               throw ☃xxxxxx;
            }

            ☃xxxx++;
         }

         switch (☃xxxx) {
            case 0:
            default:
               break;
            case 1:
               ☃xx.set(☃xxx[0]);
               break;
            case 2:
               ☃xx.set(☃xxx[0], ☃xxx[1]);
               break;
            case 3:
               ☃xx.set(☃xxx[0], ☃xxx[1], ☃xxx[2]);
               break;
            case 4:
               ☃xx.set(☃xxx[0], ☃xxx[1], ☃xxx[2], ☃xxx[3]);
         }
      }
   }

   public Framebuffer getFramebufferRaw(String var1) {
      return this.mapFramebuffers.get(☃);
   }

   public void addFramebuffer(String var1, int var2, int var3) {
      Framebuffer ☃ = new Framebuffer(☃, ☃, true);
      ☃.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      this.mapFramebuffers.put(☃, ☃);
      if (☃ == this.mainFramebufferWidth && ☃ == this.mainFramebufferHeight) {
         this.listFramebuffers.add(☃);
      }
   }

   public void deleteShaderGroup() {
      for (Framebuffer ☃ : this.mapFramebuffers.values()) {
         ☃.deleteFramebuffer();
      }

      for (Shader ☃ : this.listShaders) {
         ☃.deleteShader();
      }

      this.listShaders.clear();
   }

   public Shader addShader(String var1, Framebuffer var2, Framebuffer var3) throws IOException {
      Shader ☃ = new Shader(this.resourceManager, ☃, ☃, ☃);
      this.listShaders.add(this.listShaders.size(), ☃);
      return ☃;
   }

   private void resetProjectionMatrix() {
      this.projectionMatrix = new Matrix4f();
      this.projectionMatrix.setIdentity();
      this.projectionMatrix.m00 = 2.0F / this.mainFramebuffer.framebufferTextureWidth;
      this.projectionMatrix.m11 = 2.0F / -this.mainFramebuffer.framebufferTextureHeight;
      this.projectionMatrix.m22 = -0.0020001999F;
      this.projectionMatrix.m33 = 1.0F;
      this.projectionMatrix.m03 = -1.0F;
      this.projectionMatrix.m13 = 1.0F;
      this.projectionMatrix.m23 = -1.0001999F;
   }

   public void createBindFramebuffers(int var1, int var2) {
      this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
      this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
      this.resetProjectionMatrix();

      for (Shader ☃ : this.listShaders) {
         ☃.setProjectionMatrix(this.projectionMatrix);
      }

      for (Framebuffer ☃ : this.listFramebuffers) {
         ☃.createBindFramebuffer(☃, ☃);
      }
   }

   public void render(float var1) {
      if (☃ < this.lastStamp) {
         this.time = this.time + (1.0F - this.lastStamp);
         this.time += ☃;
      } else {
         this.time = this.time + (☃ - this.lastStamp);
      }

      this.lastStamp = ☃;

      while (this.time > 20.0F) {
         this.time -= 20.0F;
      }

      for (Shader ☃ : this.listShaders) {
         ☃.render(this.time / 20.0F);
      }
   }

   public final String getShaderGroupName() {
      return this.shaderGroupName;
   }

   private Framebuffer getFramebuffer(String var1) {
      if (☃ == null) {
         return null;
      } else {
         return ☃.equals("minecraft:main") ? this.mainFramebuffer : this.mapFramebuffers.get(☃);
      }
   }
}
