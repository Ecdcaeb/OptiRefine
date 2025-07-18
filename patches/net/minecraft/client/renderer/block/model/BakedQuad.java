package net.minecraft.client.renderer.block.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import net.optifine.model.QuadBounds;
import net.optifine.reflect.Reflector;

public class BakedQuad implements IVertexProducer {
   protected int[] vertexData;
   protected final int tintIndex;
   protected EnumFacing face;
   protected TextureAtlasSprite sprite;
   private int[] vertexDataSingle = null;
   protected boolean applyDiffuseLighting = Reflector.ForgeHooksClient_fillNormal.exists();
   protected VertexFormat format = DefaultVertexFormats.ITEM;
   private QuadBounds quadBounds;
   private boolean quadEmissiveChecked;
   private BakedQuad quadEmissive;

   public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn, boolean applyDiffuseLighting, VertexFormat format) {
      this.vertexData = vertexDataIn;
      this.tintIndex = tintIndexIn;
      this.face = faceIn;
      this.sprite = spriteIn;
      this.applyDiffuseLighting = applyDiffuseLighting;
      this.format = format;
      this.fixVertexData();
   }

   public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn) {
      this.vertexData = vertexDataIn;
      this.tintIndex = tintIndexIn;
      this.face = faceIn;
      this.sprite = spriteIn;
      this.fixVertexData();
   }

   public TextureAtlasSprite getSprite() {
      if (this.sprite == null) {
         this.sprite = getSpriteByUv(this.getVertexData());
      }

      return this.sprite;
   }

   public int[] getVertexData() {
      this.fixVertexData();
      return this.vertexData;
   }

   public boolean hasTintIndex() {
      return this.tintIndex != -1;
   }

   public int getTintIndex() {
      return this.tintIndex;
   }

   public EnumFacing getFace() {
      if (this.face == null) {
         this.face = FaceBakery.getFacingFromVertexData(this.getVertexData());
      }

      return this.face;
   }

   public int[] getVertexDataSingle() {
      if (this.vertexDataSingle == null) {
         this.vertexDataSingle = makeVertexDataSingle(this.getVertexData(), this.getSprite());
      }

      return this.vertexDataSingle;
   }

   private static int[] makeVertexDataSingle(int[] vd, TextureAtlasSprite sprite) {
      int[] vdSingle = (int[])vd.clone();
      int step = vdSingle.length / 4;

      for (int i = 0; i < 4; i++) {
         int pos = i * step;
         float tu = Float.intBitsToFloat(vdSingle[pos + 4]);
         float tv = Float.intBitsToFloat(vdSingle[pos + 4 + 1]);
         float u = sprite.toSingleU(tu);
         float v = sprite.toSingleV(tv);
         vdSingle[pos + 4] = Float.floatToRawIntBits(u);
         vdSingle[pos + 4 + 1] = Float.floatToRawIntBits(v);
      }

      return vdSingle;
   }

   public void pipe(IVertexConsumer consumer) {
      Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[]{consumer, this});
   }

   public VertexFormat getFormat() {
      return this.format;
   }

   public boolean shouldApplyDiffuseLighting() {
      return this.applyDiffuseLighting;
   }

   private static TextureAtlasSprite getSpriteByUv(int[] vertexData) {
      float uMin = 1.0F;
      float vMin = 1.0F;
      float uMax = 0.0F;
      float vMax = 0.0F;
      int step = vertexData.length / 4;

      for (int i = 0; i < 4; i++) {
         int pos = i * step;
         float tu = Float.intBitsToFloat(vertexData[pos + 4]);
         float tv = Float.intBitsToFloat(vertexData[pos + 4 + 1]);
         uMin = Math.min(uMin, tu);
         vMin = Math.min(vMin, tv);
         uMax = Math.max(uMax, tu);
         vMax = Math.max(vMax, tv);
      }

      float uMid = (uMin + uMax) / 2.0F;
      float vMid = (vMin + vMax) / 2.0F;
      return Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV(uMid, vMid);
   }

   protected void fixVertexData() {
      if (Config.isShaders()) {
         if (this.vertexData.length == 28) {
            this.vertexData = expandVertexData(this.vertexData);
         }
      } else if (this.vertexData.length == 56) {
         this.vertexData = compactVertexData(this.vertexData);
      }
   }

   private static int[] expandVertexData(int[] vd) {
      int step = vd.length / 4;
      int stepNew = step * 2;
      int[] vdNew = new int[stepNew * 4];

      for (int i = 0; i < 4; i++) {
         System.arraycopy(vd, i * step, vdNew, i * stepNew, step);
      }

      return vdNew;
   }

   private static int[] compactVertexData(int[] vd) {
      int step = vd.length / 4;
      int stepNew = step / 2;
      int[] vdNew = new int[stepNew * 4];

      for (int i = 0; i < 4; i++) {
         System.arraycopy(vd, i * step, vdNew, i * stepNew, stepNew);
      }

      return vdNew;
   }

   public QuadBounds getQuadBounds() {
      if (this.quadBounds == null) {
         this.quadBounds = new QuadBounds(this.getVertexData());
      }

      return this.quadBounds;
   }

   public float getMidX() {
      QuadBounds qb = this.getQuadBounds();
      return (qb.getMaxX() + qb.getMinX()) / 2.0F;
   }

   public double getMidY() {
      QuadBounds qb = this.getQuadBounds();
      return (qb.getMaxY() + qb.getMinY()) / 2.0F;
   }

   public double getMidZ() {
      QuadBounds qb = this.getQuadBounds();
      return (qb.getMaxZ() + qb.getMinZ()) / 2.0F;
   }

   public boolean isFaceQuad() {
      QuadBounds qb = this.getQuadBounds();
      return qb.isFaceQuad(this.face);
   }

   public boolean isFullQuad() {
      QuadBounds qb = this.getQuadBounds();
      return qb.isFullQuad(this.face);
   }

   public boolean isFullFaceQuad() {
      return this.isFullQuad() && this.isFaceQuad();
   }

   public BakedQuad getQuadEmissive() {
      if (this.quadEmissiveChecked) {
         return this.quadEmissive;
      } else {
         if (this.quadEmissive == null && this.sprite != null && this.sprite.spriteEmissive != null) {
            this.quadEmissive = new BakedQuadRetextured(this, this.sprite.spriteEmissive);
         }

         this.quadEmissiveChecked = true;
         return this.quadEmissive;
      }
   }

   @Override
   public String toString() {
      return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
   }
}
