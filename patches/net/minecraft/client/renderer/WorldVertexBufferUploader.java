package net.minecraft.client.renderer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class WorldVertexBufferUploader {
   public void draw(BufferBuilder var1) {
      if (☃.getVertexCount() > 0) {
         VertexFormat ☃ = ☃.getVertexFormat();
         int ☃x = ☃.getSize();
         ByteBuffer ☃xx = ☃.getByteBuffer();
         List<VertexFormatElement> ☃xxx = ☃.getElements();

         for (int ☃xxxx = 0; ☃xxxx < ☃xxx.size(); ☃xxxx++) {
            VertexFormatElement ☃xxxxx = ☃xxx.get(☃xxxx);
            VertexFormatElement.EnumUsage ☃xxxxxx = ☃xxxxx.getUsage();
            int ☃xxxxxxx = ☃xxxxx.getType().getGlConstant();
            int ☃xxxxxxxx = ☃xxxxx.getIndex();
            ((Buffer)☃xx).position(☃.getOffset(☃xxxx));
            switch (☃xxxxxx) {
               case POSITION:
                  GlStateManager.glVertexPointer(☃xxxxx.getElementCount(), ☃xxxxxxx, ☃x, ☃xx);
                  GlStateManager.glEnableClientState(32884);
                  break;
               case UV:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + ☃xxxxxxxx);
                  GlStateManager.glTexCoordPointer(☃xxxxx.getElementCount(), ☃xxxxxxx, ☃x, ☃xx);
                  GlStateManager.glEnableClientState(32888);
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case COLOR:
                  GlStateManager.glColorPointer(☃xxxxx.getElementCount(), ☃xxxxxxx, ☃x, ☃xx);
                  GlStateManager.glEnableClientState(32886);
                  break;
               case NORMAL:
                  GlStateManager.glNormalPointer(☃xxxxxxx, ☃x, ☃xx);
                  GlStateManager.glEnableClientState(32885);
            }
         }

         GlStateManager.glDrawArrays(☃.getDrawMode(), 0, ☃.getVertexCount());
         int ☃xxxx = 0;

         for (int ☃xxxxx = ☃xxx.size(); ☃xxxx < ☃xxxxx; ☃xxxx++) {
            VertexFormatElement ☃xxxxxx = ☃xxx.get(☃xxxx);
            VertexFormatElement.EnumUsage ☃xxxxxxx = ☃xxxxxx.getUsage();
            int ☃xxxxxxxx = ☃xxxxxx.getIndex();
            switch (☃xxxxxxx) {
               case POSITION:
                  GlStateManager.glDisableClientState(32884);
                  break;
               case UV:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + ☃xxxxxxxx);
                  GlStateManager.glDisableClientState(32888);
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case COLOR:
                  GlStateManager.glDisableClientState(32886);
                  GlStateManager.resetColor();
                  break;
               case NORMAL:
                  GlStateManager.glDisableClientState(32885);
            }
         }
      }

      ☃.reset();
   }
}
