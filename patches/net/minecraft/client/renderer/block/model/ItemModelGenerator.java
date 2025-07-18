package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

public class ItemModelGenerator {
   public static final List<String> LAYERS = Lists.newArrayList(new String[]{"layer0", "layer1", "layer2", "layer3", "layer4"});

   @Nullable
   public ModelBlock makeItemModel(TextureMap var1, ModelBlock var2) {
      Map<String, String> ☃ = Maps.newHashMap();
      List<BlockPart> ☃x = Lists.newArrayList();

      for (int ☃xx = 0; ☃xx < LAYERS.size(); ☃xx++) {
         String ☃xxx = LAYERS.get(☃xx);
         if (!☃.isTexturePresent(☃xxx)) {
            break;
         }

         String ☃xxxx = ☃.resolveTextureName(☃xxx);
         ☃.put(☃xxx, ☃xxxx);
         TextureAtlasSprite ☃xxxxx = ☃.getAtlasSprite(new ResourceLocation(☃xxxx).toString());
         ☃x.addAll(this.getBlockParts(☃xx, ☃xxx, ☃xxxxx));
      }

      if (☃x.isEmpty()) {
         return null;
      } else {
         ☃.put("particle", ☃.isTexturePresent("particle") ? ☃.resolveTextureName("particle") : ☃.get("layer0"));
         return new ModelBlock(null, ☃x, ☃, false, false, ☃.getAllTransforms(), ☃.getOverrides());
      }
   }

   private List<BlockPart> getBlockParts(int var1, String var2, TextureAtlasSprite var3) {
      Map<EnumFacing, BlockPartFace> ☃ = Maps.newHashMap();
      ☃.put(EnumFacing.SOUTH, new BlockPartFace(null, ☃, ☃, new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0)));
      ☃.put(EnumFacing.NORTH, new BlockPartFace(null, ☃, ☃, new BlockFaceUV(new float[]{16.0F, 0.0F, 0.0F, 16.0F}, 0)));
      List<BlockPart> ☃x = Lists.newArrayList();
      ☃x.add(new BlockPart(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), ☃, null, true));
      ☃x.addAll(this.getBlockParts(☃, ☃, ☃));
      return ☃x;
   }

   private List<BlockPart> getBlockParts(TextureAtlasSprite var1, String var2, int var3) {
      float ☃ = ☃.getIconWidth();
      float ☃x = ☃.getIconHeight();
      List<BlockPart> ☃xx = Lists.newArrayList();

      for (ItemModelGenerator.Span ☃xxx : this.getSpans(☃)) {
         float ☃xxxx = 0.0F;
         float ☃xxxxx = 0.0F;
         float ☃xxxxxx = 0.0F;
         float ☃xxxxxxx = 0.0F;
         float ☃xxxxxxxx = 0.0F;
         float ☃xxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxxx = 0.0F;
         float ☃xxxxxxxxxxxxxx = ☃xxx.getMin();
         float ☃xxxxxxxxxxxxxxx = ☃xxx.getMax();
         float ☃xxxxxxxxxxxxxxxx = ☃xxx.getAnchor();
         ItemModelGenerator.SpanFacing ☃xxxxxxxxxxxxxxxxx = ☃xxx.getFacing();
         switch (☃xxxxxxxxxxxxxxxxx) {
            case UP:
               ☃xxxxxxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxxxx = ☃xxxxxxxxx = ☃xxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxx = 16.0F / ☃;
               ☃xxxxxxxxxxxxx = 16.0F / (☃x - 1.0F);
               break;
            case DOWN:
               ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxxxx = ☃xxxxxxxxx = ☃xxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxx = ☃xxxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxxxx = ☃xxxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxxxxxxxxx = 16.0F / ☃;
               ☃xxxxxxxxxxxxx = 16.0F / (☃x - 1.0F);
               break;
            case LEFT:
               ☃xxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxxxxx = ☃xxxxxxxxxx = ☃xxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxxxxxxxxx = 16.0F / (☃ - 1.0F);
               ☃xxxxxxxxxxxxx = 16.0F / ☃x;
               break;
            case RIGHT:
               ☃xxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxxxxxx = ☃xxxxxxxxxxxxxxxx;
               ☃xxxx = ☃xxxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxxx = ☃xxxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxxxxxxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxxx = ☃xxxxxxxxxxxxxx;
               ☃xxxxxxx = ☃xxxxxxxxxx = ☃xxxxxxxxxxxxxxx + 1.0F;
               ☃xxxxxxxxxxxx = 16.0F / (☃ - 1.0F);
               ☃xxxxxxxxxxxxx = 16.0F / ☃x;
         }

         float ☃xxxx = 16.0F / ☃;
         float ☃xxxxx = 16.0F / ☃x;
         ☃xxxx *= ☃xxxx;
         ☃xxxxxx *= ☃xxxx;
         ☃xxxxx *= ☃xxxxx;
         ☃xxxxxxx *= ☃xxxxx;
         ☃xxxxx = 16.0F - ☃xxxxx;
         ☃xxxxxxx = 16.0F - ☃xxxxxxx;
         ☃xxxxxxxx *= ☃xxxxxxxxxxxx;
         ☃xxxxxxxxx *= ☃xxxxxxxxxxxx;
         ☃xxxxxxxxxx *= ☃xxxxxxxxxxxxx;
         ☃xxxxxxxxxxx *= ☃xxxxxxxxxxxxx;
         Map<EnumFacing, BlockPartFace> ☃xxxxxx = Maps.newHashMap();
         ☃xxxxxx.put(
            ☃xxxxxxxxxxxxxxxxx.getFacing(), new BlockPartFace(null, ☃, ☃, new BlockFaceUV(new float[]{☃xxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxxx}, 0))
         );
         switch (☃xxxxxxxxxxxxxxxxx) {
            case UP:
               ☃xx.add(new BlockPart(new Vector3f(☃xxxx, ☃xxxxx, 7.5F), new Vector3f(☃xxxxxx, ☃xxxxx, 8.5F), ☃xxxxxx, null, true));
               break;
            case DOWN:
               ☃xx.add(new BlockPart(new Vector3f(☃xxxx, ☃xxxxxxx, 7.5F), new Vector3f(☃xxxxxx, ☃xxxxxxx, 8.5F), ☃xxxxxx, null, true));
               break;
            case LEFT:
               ☃xx.add(new BlockPart(new Vector3f(☃xxxx, ☃xxxxx, 7.5F), new Vector3f(☃xxxx, ☃xxxxxxx, 8.5F), ☃xxxxxx, null, true));
               break;
            case RIGHT:
               ☃xx.add(new BlockPart(new Vector3f(☃xxxxxx, ☃xxxxx, 7.5F), new Vector3f(☃xxxxxx, ☃xxxxxxx, 8.5F), ☃xxxxxx, null, true));
         }
      }

      return ☃xx;
   }

   private List<ItemModelGenerator.Span> getSpans(TextureAtlasSprite var1) {
      int ☃ = ☃.getIconWidth();
      int ☃x = ☃.getIconHeight();
      List<ItemModelGenerator.Span> ☃xx = Lists.newArrayList();

      for (int ☃xxx = 0; ☃xxx < ☃.getFrameCount(); ☃xxx++) {
         int[] ☃xxxx = ☃.getFrameTextureData(☃xxx)[0];

         for (int ☃xxxxx = 0; ☃xxxxx < ☃x; ☃xxxxx++) {
            for (int ☃xxxxxx = 0; ☃xxxxxx < ☃; ☃xxxxxx++) {
               boolean ☃xxxxxxx = !this.isTransparent(☃xxxx, ☃xxxxxx, ☃xxxxx, ☃, ☃x);
               this.checkTransition(ItemModelGenerator.SpanFacing.UP, ☃xx, ☃xxxx, ☃xxxxxx, ☃xxxxx, ☃, ☃x, ☃xxxxxxx);
               this.checkTransition(ItemModelGenerator.SpanFacing.DOWN, ☃xx, ☃xxxx, ☃xxxxxx, ☃xxxxx, ☃, ☃x, ☃xxxxxxx);
               this.checkTransition(ItemModelGenerator.SpanFacing.LEFT, ☃xx, ☃xxxx, ☃xxxxxx, ☃xxxxx, ☃, ☃x, ☃xxxxxxx);
               this.checkTransition(ItemModelGenerator.SpanFacing.RIGHT, ☃xx, ☃xxxx, ☃xxxxxx, ☃xxxxx, ☃, ☃x, ☃xxxxxxx);
            }
         }
      }

      return ☃xx;
   }

   private void checkTransition(
      ItemModelGenerator.SpanFacing var1, List<ItemModelGenerator.Span> var2, int[] var3, int var4, int var5, int var6, int var7, boolean var8
   ) {
      boolean ☃ = this.isTransparent(☃, ☃ + ☃.getXOffset(), ☃ + ☃.getYOffset(), ☃, ☃) && ☃;
      if (☃) {
         this.createOrExpandSpan(☃, ☃, ☃, ☃);
      }
   }

   private void createOrExpandSpan(List<ItemModelGenerator.Span> var1, ItemModelGenerator.SpanFacing var2, int var3, int var4) {
      ItemModelGenerator.Span ☃ = null;

      for (ItemModelGenerator.Span ☃x : ☃) {
         if (☃x.getFacing() == ☃) {
            int ☃xx = ☃.isHorizontal() ? ☃ : ☃;
            if (☃x.getAnchor() == ☃xx) {
               ☃ = ☃x;
               break;
            }
         }
      }

      int ☃xx = ☃.isHorizontal() ? ☃ : ☃;
      int ☃xxx = ☃.isHorizontal() ? ☃ : ☃;
      if (☃ == null) {
         ☃.add(new ItemModelGenerator.Span(☃, ☃xxx, ☃xx));
      } else {
         ☃.expand(☃xxx);
      }
   }

   private boolean isTransparent(int[] var1, int var2, int var3, int var4, int var5) {
      return ☃ >= 0 && ☃ >= 0 && ☃ < ☃ && ☃ < ☃ ? (☃[☃ * ☃ + ☃] >> 24 & 0xFF) == 0 : true;
   }

   static class Span {
      private final ItemModelGenerator.SpanFacing spanFacing;
      private int min;
      private int max;
      private final int anchor;

      public Span(ItemModelGenerator.SpanFacing var1, int var2, int var3) {
         this.spanFacing = ☃;
         this.min = ☃;
         this.max = ☃;
         this.anchor = ☃;
      }

      public void expand(int var1) {
         if (☃ < this.min) {
            this.min = ☃;
         } else if (☃ > this.max) {
            this.max = ☃;
         }
      }

      public ItemModelGenerator.SpanFacing getFacing() {
         return this.spanFacing;
      }

      public int getMin() {
         return this.min;
      }

      public int getMax() {
         return this.max;
      }

      public int getAnchor() {
         return this.anchor;
      }
   }

   static enum SpanFacing {
      UP(EnumFacing.UP, 0, -1),
      DOWN(EnumFacing.DOWN, 0, 1),
      LEFT(EnumFacing.EAST, -1, 0),
      RIGHT(EnumFacing.WEST, 1, 0);

      private final EnumFacing facing;
      private final int xOffset;
      private final int yOffset;

      private SpanFacing(EnumFacing var3, int var4, int var5) {
         this.facing = ☃;
         this.xOffset = ☃;
         this.yOffset = ☃;
      }

      public EnumFacing getFacing() {
         return this.facing;
      }

      public int getXOffset() {
         return this.xOffset;
      }

      public int getYOffset() {
         return this.yOffset;
      }

      private boolean isHorizontal() {
         return this == DOWN || this == UP;
      }
   }
}
