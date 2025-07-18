package net.minecraft.client.shader;

import java.io.IOException;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.util.JsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderLinkHelper {
   private static final Logger LOGGER = LogManager.getLogger();
   private static ShaderLinkHelper staticShaderLinkHelper;

   public static void setNewStaticShaderLinkHelper() {
      staticShaderLinkHelper = new ShaderLinkHelper();
   }

   public static ShaderLinkHelper getStaticShaderLinkHelper() {
      return staticShaderLinkHelper;
   }

   private ShaderLinkHelper() {
   }

   public void deleteShader(ShaderManager var1) {
      ☃.getFragmentShaderLoader().deleteShader(☃);
      ☃.getVertexShaderLoader().deleteShader(☃);
      OpenGlHelper.glDeleteProgram(☃.getProgram());
   }

   public int createProgram() throws JsonException {
      int ☃ = OpenGlHelper.glCreateProgram();
      if (☃ <= 0) {
         throw new JsonException("Could not create shader program (returned program ID " + ☃ + ")");
      } else {
         return ☃;
      }
   }

   public void linkProgram(ShaderManager var1) throws IOException {
      ☃.getFragmentShaderLoader().attachShader(☃);
      ☃.getVertexShaderLoader().attachShader(☃);
      OpenGlHelper.glLinkProgram(☃.getProgram());
      int ☃ = OpenGlHelper.glGetProgrami(☃.getProgram(), OpenGlHelper.GL_LINK_STATUS);
      if (☃ == 0) {
         LOGGER.warn(
            "Error encountered when linking program containing VS {} and FS {}. Log output:",
            ☃.getVertexShaderLoader().getShaderFilename(),
            ☃.getFragmentShaderLoader().getShaderFilename()
         );
         LOGGER.warn(OpenGlHelper.glGetProgramInfoLog(☃.getProgram(), 32768));
      }
   }
}
