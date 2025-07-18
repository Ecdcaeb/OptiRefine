package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class MapItemRenderer {
   private static final ResourceLocation TEXTURE_MAP_ICONS = new ResourceLocation("textures/map/map_icons.png");
   private final TextureManager textureManager;
   private final Map<String, MapItemRenderer.Instance> loadedMaps = Maps.newHashMap();

   public MapItemRenderer(TextureManager var1) {
      this.textureManager = ☃;
   }

   public void updateMapTexture(MapData var1) {
      this.getMapRendererInstance(☃).updateMapTexture();
   }

   public void renderMap(MapData var1, boolean var2) {
      this.getMapRendererInstance(☃).render(☃);
   }

   private MapItemRenderer.Instance getMapRendererInstance(MapData var1) {
      MapItemRenderer.Instance ☃ = this.loadedMaps.get(☃.mapName);
      if (☃ == null) {
         ☃ = new MapItemRenderer.Instance(☃);
         this.loadedMaps.put(☃.mapName, ☃);
      }

      return ☃;
   }

   @Nullable
   public MapItemRenderer.Instance getMapInstanceIfExists(String var1) {
      return this.loadedMaps.get(☃);
   }

   public void clearLoadedMaps() {
      for (MapItemRenderer.Instance ☃ : this.loadedMaps.values()) {
         this.textureManager.deleteTexture(☃.location);
      }

      this.loadedMaps.clear();
   }

   @Nullable
   public MapData getData(@Nullable MapItemRenderer.Instance var1) {
      return ☃ != null ? ☃.mapData : null;
   }

   class Instance {
      private final MapData mapData;
      private final DynamicTexture mapTexture;
      private final ResourceLocation location;
      private final int[] mapTextureData;

      private Instance(MapData var2) {
         this.mapData = ☃;
         this.mapTexture = new DynamicTexture(128, 128);
         this.mapTextureData = this.mapTexture.getTextureData();
         this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + ☃.mapName, this.mapTexture);

         for (int ☃ = 0; ☃ < this.mapTextureData.length; ☃++) {
            this.mapTextureData[☃] = 0;
         }
      }

      private void updateMapTexture() {
         for (int ☃ = 0; ☃ < 16384; ☃++) {
            int ☃x = this.mapData.colors[☃] & 255;
            if (☃x / 4 == 0) {
               this.mapTextureData[☃] = (☃ + ☃ / 128 & 1) * 8 + 16 << 24;
            } else {
               this.mapTextureData[☃] = MapColor.COLORS[☃x / 4].getMapColor(☃x & 3);
            }
         }

         this.mapTexture.updateDynamicTexture();
      }

      private void render(boolean var1) {
         int ☃ = 0;
         int ☃x = 0;
         Tessellator ☃xx = Tessellator.getInstance();
         BufferBuilder ☃xxx = ☃xx.getBuffer();
         float ☃xxxx = 0.0F;
         MapItemRenderer.this.textureManager.bindTexture(this.location);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE
         );
         GlStateManager.disableAlpha();
         ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃xxx.pos(0.0, 128.0, -0.01F).tex(0.0, 1.0).endVertex();
         ☃xxx.pos(128.0, 128.0, -0.01F).tex(1.0, 1.0).endVertex();
         ☃xxx.pos(128.0, 0.0, -0.01F).tex(1.0, 0.0).endVertex();
         ☃xxx.pos(0.0, 0.0, -0.01F).tex(0.0, 0.0).endVertex();
         ☃xx.draw();
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
         MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.TEXTURE_MAP_ICONS);
         int ☃xxxxx = 0;

         for (MapDecoration ☃xxxxxx : this.mapData.mapDecorations.values()) {
            if (!☃ || ☃xxxxxx.renderOnFrame()) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(0.0F + ☃xxxxxx.getX() / 2.0F + 64.0F, 0.0F + ☃xxxxxx.getY() / 2.0F + 64.0F, -0.02F);
               GlStateManager.rotate(☃xxxxxx.getRotation() * 360 / 16.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.scale(4.0F, 4.0F, 3.0F);
               GlStateManager.translate(-0.125F, 0.125F, 0.0F);
               byte ☃xxxxxxx = ☃xxxxxx.getImage();
               float ☃xxxxxxxx = (☃xxxxxxx % 4 + 0) / 4.0F;
               float ☃xxxxxxxxx = (☃xxxxxxx / 4 + 0) / 4.0F;
               float ☃xxxxxxxxxx = (☃xxxxxxx % 4 + 1) / 4.0F;
               float ☃xxxxxxxxxxx = (☃xxxxxxx / 4 + 1) / 4.0F;
               ☃xxx.begin(7, DefaultVertexFormats.POSITION_TEX);
               float ☃xxxxxxxxxxxx = -0.001F;
               ☃xxx.pos(-1.0, 1.0, ☃xxxxx * -0.001F).tex(☃xxxxxxxx, ☃xxxxxxxxx).endVertex();
               ☃xxx.pos(1.0, 1.0, ☃xxxxx * -0.001F).tex(☃xxxxxxxxxx, ☃xxxxxxxxx).endVertex();
               ☃xxx.pos(1.0, -1.0, ☃xxxxx * -0.001F).tex(☃xxxxxxxxxx, ☃xxxxxxxxxxx).endVertex();
               ☃xxx.pos(-1.0, -1.0, ☃xxxxx * -0.001F).tex(☃xxxxxxxx, ☃xxxxxxxxxxx).endVertex();
               ☃xx.draw();
               GlStateManager.popMatrix();
               ☃xxxxx++;
            }
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, 0.0F, -0.04F);
         GlStateManager.scale(1.0F, 1.0F, 1.0F);
         GlStateManager.popMatrix();
      }
   }
}
