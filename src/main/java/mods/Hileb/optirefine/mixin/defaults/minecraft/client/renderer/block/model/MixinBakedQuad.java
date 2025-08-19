package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.optifine.model.QuadBounds;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BakedQuad.class)
public abstract class MixinBakedQuad {

    @Unique
    @AccessTransformer(name = "field_178215_a", deobf = true)
    protected int[] optirefine$acc_vertexData;

    @Unique
    @AccessTransformer(name = "field_178213_b", deobf = true)
    protected int optirefine$acc_tintIndex;
    @Unique
    @AccessTransformer(name = "field_178214_c", deobf = true)
    protected EnumFacing optirefine$acc_face;
    @Unique
    @AccessTransformer(name = "field_187509_d", deobf = true)
    protected TextureAtlasSprite optirefine$acc_sprite;

    @Unique
    @AccessTransformer(name = "format")
    protected VertexFormat  optirefine$acc_format;

    @Unique
    @AccessTransformer(name = "applyDiffuseLighting")
    protected boolean  optirefine$acc_applyDiffuseLighting;


    @Shadow @Final @Mutable
    protected int[] vertexData;
    @Shadow @Final @Mutable
    protected int tintIndex;
    @Shadow @Final @Mutable
    protected EnumFacing face;
    @Shadow @Final @Mutable
    protected TextureAtlasSprite sprite;

    @Unique
    private int[] vertexDataSingle = null;
    @Unique
    private QuadBounds quadBounds;
    @Unique
    private boolean quadEmissiveChecked;
    @Unique
    private BakedQuad quadEmissive;

    @Inject(method = "<init>([IILnet/minecraft/util/EnumFacing;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZLnet/minecraft/client/renderer/vertex/VertexFormat;)V", at = @At("RETURN"))
    public void init(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn, boolean applyDiffuseLighting, VertexFormat format, CallbackInfo ci){
        this.fixVertexData();
    }

    @ModifyReturnValue(method = "getSprite", at = @At("RETURN"))
    public TextureAtlasSprite $getSprite(TextureAtlasSprite orv) {
        if (orv == null) {
            return this.sprite = optiRefine$getSpriteByUv(this.getVertexData());
        }
        return orv;
    }

    @Inject(method = "getVertexData", at = @At("HEAD"))
    public void beforeGetVertexData(CallbackInfoReturnable<int[]> cir){
        this.fixVertexData();
    }

    @Shadow
    public abstract int[] getVertexData();

    @ModifyReturnValue(method = "getFace", at = @At("RETURN"))
    public EnumFacing $getSprite(EnumFacing original) {
        if (original == null) {
            return this.face = FaceBakery.getFacingFromVertexData(this.getVertexData());
        } else return original;
    }

    @Shadow
    public abstract TextureAtlasSprite getSprite();

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = optiRefine$makeVertexDataSingle(this.getVertexData(), this.getSprite());
        }

        return this.vertexDataSingle;
    }

    @Unique
    private static int[] optiRefine$makeVertexDataSingle(int[] vd, TextureAtlasSprite sprite) {
        int[] vdSingle = vd.clone();
        int step = vdSingle.length / 4;

        for (int i = 0; i < 4; i++) {
            int pos = i * step;
            float tu = Float.intBitsToFloat(vdSingle[pos + 4]);
            float tv = Float.intBitsToFloat(vdSingle[pos + 4 + 1]);
            float u = TextureAtlasSprite_toSingleU(sprite, tu);
            float v = TextureAtlasSprite_toSingleV(sprite, tv);
            vdSingle[pos + 4] = Float.floatToRawIntBits(u);
            vdSingle[pos + 4 + 1] = Float.floatToRawIntBits(v);
        }

        return vdSingle;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleU (F)F")
    private static native float TextureAtlasSprite_toSingleU(TextureAtlasSprite textureAtlasSprite, float arg1);

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite toSingleV (F)F")
    private static native float TextureAtlasSprite_toSingleV(TextureAtlasSprite textureAtlasSprite, float arg1);

    @Unique
    private static TextureAtlasSprite optiRefine$getSpriteByUv(int[] vertexData) {
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
        return TextureMap_getIconByUV(Minecraft.getMinecraft().getTextureMapBlocks(), uMid, vMid);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getIconByUV (FF)Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
    private static native TextureAtlasSprite TextureMap_getIconByUV(TextureMap textureMap, float u, float v);

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    protected void fixVertexData() {
        if (Config.isShaders()) {
            if (this.vertexData.length == 28) {
                this.vertexData = optiRefine$expandVertexData(this.vertexData);
            }
        } else if (this.vertexData.length == 56) {
            this.vertexData = optiRefine$compactVertexData(this.vertexData);
        }
    }

    @Unique
    private static int[] optiRefine$expandVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step * 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, step);
        }

        return vdNew;
    }

    @Unique
    private static int[] optiRefine$compactVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step / 2;
        int[] vdNew = new int[stepNew * 4];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, stepNew);
        }

        return vdNew;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public QuadBounds getQuadBounds() {
        if (this.quadBounds == null) {
            this.quadBounds = new QuadBounds(this.getVertexData());
        }

        return this.quadBounds;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public float getMidX() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxX() + qb.getMinX()) / 2.0F;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public double getMidY() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxY() + qb.getMinY()) / 2.0F;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public double getMidZ() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxZ() + qb.getMinZ()) / 2.0F;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean isFaceQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFaceQuad(this.face);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean isFullQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFullQuad(this.face);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean isFullFaceQuad() {
        return this.isFullQuad() && this.isFaceQuad();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public BakedQuad getQuadEmissive() {
        if (this.quadEmissiveChecked) {
            return this.quadEmissive;
        } else {
            if (this.quadEmissive == null && this.sprite != null && TextureAtlasSprite_spriteEmissive_get(this.sprite) != null) {
                this.quadEmissive = new BakedQuadRetextured(cast_BakedQuad(this), TextureAtlasSprite_spriteEmissive_get(this.sprite));
            }

            this.quadEmissiveChecked = true;
            return this.quadEmissive;
        }
    }

    @AccessibleOperation
    private static native BakedQuad cast_BakedQuad(Object obj);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.texture.TextureAtlasSprite spriteEmissive Lnet.minecraft.client.renderer.texture.TextureAtlasSprite;")
    private static native TextureAtlasSprite TextureAtlasSprite_spriteEmissive_get(TextureAtlasSprite textureAtlasSprite);

    @Override
    public String toString() {
        return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }
}
/*
+++ net/minecraft/client/renderer/block/model/BakedQuad.java	Tue Aug 19 14:59:58 2025
@@ -1,38 +1,218 @@
 package net.minecraft.client.renderer.block.model;

+import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;
+import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
+import net.minecraft.client.renderer.vertex.VertexFormat;
 import net.minecraft.util.EnumFacing;
+import net.minecraftforge.client.model.pipeline.IVertexConsumer;
+import net.minecraftforge.client.model.pipeline.IVertexProducer;
+import net.optifine.model.QuadBounds;
+import net.optifine.reflect.Reflector;

-public class BakedQuad {
-   protected final int[] vertexData;
+public class BakedQuad implements IVertexProducer {
+   protected int[] vertexData;
    protected final int tintIndex;
-   protected final EnumFacing face;
-   protected final TextureAtlasSprite sprite;
+   protected EnumFacing face;
+   protected TextureAtlasSprite sprite;
+   private int[] vertexDataSingle = null;
+   protected boolean applyDiffuseLighting = Reflector.ForgeHooksClient_fillNormal.exists();
+   protected VertexFormat format = DefaultVertexFormats.ITEM;
+   private QuadBounds quadBounds;
+   private boolean quadEmissiveChecked;
+   private BakedQuad quadEmissive;
+
+   public BakedQuad(int[] var1, int var2, EnumFacing var3, TextureAtlasSprite var4, boolean var5, VertexFormat var6) {
+      this.vertexData = var1;
+      this.tintIndex = var2;
+      this.face = var3;
+      this.sprite = var4;
+      this.applyDiffuseLighting = var5;
+      this.format = var6;
+      this.fixVertexData();
+   }

    public BakedQuad(int[] var1, int var2, EnumFacing var3, TextureAtlasSprite var4) {
       this.vertexData = var1;
       this.tintIndex = var2;
       this.face = var3;
       this.sprite = var4;
+      this.fixVertexData();
    }

    public TextureAtlasSprite getSprite() {
+      if (this.sprite == null) {
+         this.sprite = getSpriteByUv(this.getVertexData());
+      }
+
       return this.sprite;
    }

    public int[] getVertexData() {
+      this.fixVertexData();
       return this.vertexData;
    }

    public boolean hasTintIndex() {
       return this.tintIndex != -1;
    }

    public int getTintIndex() {
       return this.tintIndex;
    }

    public EnumFacing getFace() {
+      if (this.face == null) {
+         this.face = FaceBakery.getFacingFromVertexData(this.getVertexData());
+      }
+
       return this.face;
+   }
+
+   public int[] getVertexDataSingle() {
+      if (this.vertexDataSingle == null) {
+         this.vertexDataSingle = makeVertexDataSingle(this.getVertexData(), this.getSprite());
+      }
+
+      return this.vertexDataSingle;
+   }
+
+   private static int[] makeVertexDataSingle(int[] var0, TextureAtlasSprite var1) {
+      int[] var2 = (int[])var0.clone();
+      int var3 = var2.length / 4;
+
+      for (int var4 = 0; var4 < 4; var4++) {
+         int var5 = var4 * var3;
+         float var6 = Float.intBitsToFloat(var2[var5 + 4]);
+         float var7 = Float.intBitsToFloat(var2[var5 + 4 + 1]);
+         float var8 = var1.toSingleU(var6);
+         float var9 = var1.toSingleV(var7);
+         var2[var5 + 4] = Float.floatToRawIntBits(var8);
+         var2[var5 + 4 + 1] = Float.floatToRawIntBits(var9);
+      }
+
+      return var2;
+   }
+
+   public void pipe(IVertexConsumer var1) {
+      Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[]{var1, this});
+   }
+
+   public VertexFormat getFormat() {
+      return this.format;
+   }
+
+   public boolean shouldApplyDiffuseLighting() {
+      return this.applyDiffuseLighting;
+   }
+
+   private static TextureAtlasSprite getSpriteByUv(int[] var0) {
+      float var1 = 1.0F;
+      float var2 = 1.0F;
+      float var3 = 0.0F;
+      float var4 = 0.0F;
+      int var5 = var0.length / 4;
+
+      for (int var6 = 0; var6 < 4; var6++) {
+         int var7 = var6 * var5;
+         float var8 = Float.intBitsToFloat(var0[var7 + 4]);
+         float var9 = Float.intBitsToFloat(var0[var7 + 4 + 1]);
+         var1 = Math.min(var1, var8);
+         var2 = Math.min(var2, var9);
+         var3 = Math.max(var3, var8);
+         var4 = Math.max(var4, var9);
+      }
+
+      float var10 = (var1 + var3) / 2.0F;
+      float var11 = (var2 + var4) / 2.0F;
+      return Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV(var10, var11);
+   }
+
+   protected void fixVertexData() {
+      if (Config.isShaders()) {
+         if (this.vertexData.length == 28) {
+            this.vertexData = expandVertexData(this.vertexData);
+         }
+      } else if (this.vertexData.length == 56) {
+         this.vertexData = compactVertexData(this.vertexData);
+      }
+   }
+
+   private static int[] expandVertexData(int[] var0) {
+      int var1 = var0.length / 4;
+      int var2 = var1 * 2;
+      int[] var3 = new int[var2 * 4];
+
+      for (int var4 = 0; var4 < 4; var4++) {
+         System.arraycopy(var0, var4 * var1, var3, var4 * var2, var1);
+      }
+
+      return var3;
+   }
+
+   private static int[] compactVertexData(int[] var0) {
+      int var1 = var0.length / 4;
+      int var2 = var1 / 2;
+      int[] var3 = new int[var2 * 4];
+
+      for (int var4 = 0; var4 < 4; var4++) {
+         System.arraycopy(var0, var4 * var1, var3, var4 * var2, var2);
+      }
+
+      return var3;
+   }
+
+   public QuadBounds getQuadBounds() {
+      if (this.quadBounds == null) {
+         this.quadBounds = new QuadBounds(this.getVertexData());
+      }
+
+      return this.quadBounds;
+   }
+
+   public float getMidX() {
+      QuadBounds var1 = this.getQuadBounds();
+      return (var1.getMaxX() + var1.getMinX()) / 2.0F;
+   }
+
+   public double getMidY() {
+      QuadBounds var1 = this.getQuadBounds();
+      return (var1.getMaxY() + var1.getMinY()) / 2.0F;
+   }
+
+   public double getMidZ() {
+      QuadBounds var1 = this.getQuadBounds();
+      return (var1.getMaxZ() + var1.getMinZ()) / 2.0F;
+   }
+
+   public boolean isFaceQuad() {
+      QuadBounds var1 = this.getQuadBounds();
+      return var1.isFaceQuad(this.face);
+   }
+
+   public boolean isFullQuad() {
+      QuadBounds var1 = this.getQuadBounds();
+      return var1.isFullQuad(this.face);
+   }
+
+   public boolean isFullFaceQuad() {
+      return this.isFullQuad() && this.isFaceQuad();
+   }
+
+   public BakedQuad getQuadEmissive() {
+      if (this.quadEmissiveChecked) {
+         return this.quadEmissive;
+      } else {
+         if (this.quadEmissive == null && this.sprite != null && this.sprite.spriteEmissive != null) {
+            this.quadEmissive = new BakedQuadRetextured(this, this.sprite.spriteEmissive);
+         }
+
+         this.quadEmissiveChecked = true;
+         return this.quadEmissive;
+      }
+   }
+
+   public String toString() {
+      return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }
 }
 */
