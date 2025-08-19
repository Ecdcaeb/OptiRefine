package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.optifine.entity.model.anim.ModelUpdater;
import net.optifine.model.ModelSprite;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;


@Mixin(ModelRenderer.class)
public abstract class MixinModelRenderer {
    @SuppressWarnings("unused")
    @Shadow
    public float textureWidth;
    @SuppressWarnings("unused")
    @Shadow
    public float textureHeight;
    @Shadow
    private int textureOffsetX;
    @Shadow
    private int textureOffsetY;
    @SuppressWarnings("unused")
    @Shadow
    public float rotationPointX;
    @SuppressWarnings("unused")
    @Shadow
    public float rotationPointY;
    @SuppressWarnings("unused")
    @Shadow
    public float rotationPointZ;
    @SuppressWarnings("unused")
    @Shadow
    public float rotateAngleX;
    @SuppressWarnings("unused")
    @Shadow
    public float rotateAngleY;
    @SuppressWarnings("unused")
    @Shadow
    public float rotateAngleZ;
    @Shadow
    private boolean compiled;
    @Shadow
    private int displayList;
    @Shadow
    public boolean mirror;
    @Shadow
    public boolean showModel;
    @Shadow
    public boolean isHidden;

    @Shadow
    public List<ModelBox> cubeList;
    @Shadow
    public List<ModelRenderer> childModels;
    @SuppressWarnings("unused")
    @Shadow @Final
    public String boxName;
    @SuppressWarnings("unused")
    @Shadow @Final
    private ModelBase baseModel;
    @SuppressWarnings("unused")
    @Shadow
    public float offsetX;
    @SuppressWarnings("unused")
    @Shadow
    public float offsetY;
    @SuppressWarnings("unused")
    @Shadow
    public float offsetZ;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    public List<ModelSprite> spriteList = new ArrayList<>();

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public boolean mirrorV = false;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public float scaleX = 1.0F;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public float scaleY = 1.0F;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public float scaleZ = 1.0F;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private int countResetDisplayList;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private ResourceLocation textureLocation = null;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private String id = null;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private ModelUpdater modelUpdater;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private RenderGlobal renderGlobal = Config.getRenderGlobal();

    @SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
    @Unique
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net/minecraft/client/renderer/RenderGlobal renderOverlayDamaged Z")
    private native static boolean _acc_RenderGlobal_renderOverlayDamaged_(RenderGlobal global);

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net/minecraft/client/renderer/RenderGlobal renderOverlayEyes Z")
    private static native boolean _acc_RenderGlobal_renderOverlayEyes_(RenderGlobal global);

    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net/minecraft/client/renderer/GlStateManager getBoundTexture ()I")
    private native static int _acc_GlStateManager_getBoundTexture_();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void injectPreRender(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();

            lastTextureId.set(0);
            if (this.textureLocation != null && !_acc_RenderGlobal_renderOverlayDamaged_(this.renderGlobal)) {
                if (_acc_RenderGlobal_renderOverlayEyes_(this.renderGlobal)) {
                    ci.cancel();
                }

                lastTextureId.set(_acc_GlStateManager_getBoundTexture_());
                Config.getTextureManager().bindTexture(this.textureLocation);
            }

            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void injectPostRender(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            if (lastTextureId.get() != 0) {
                GlStateManager.bindTexture(lastTextureId.get());
            }
        }
    }

    @Inject(method = "renderWithRotation", at = @At("HEAD"), cancellable = true)
    public void injectPreRenderWithRotation(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();

            lastTextureId.set(0);
            if (this.textureLocation != null && !_acc_RenderGlobal_renderOverlayDamaged_(this.renderGlobal)) {
                if (_acc_RenderGlobal_renderOverlayEyes_(this.renderGlobal)) {
                    ci.cancel();
                }

                lastTextureId.set(_acc_GlStateManager_getBoundTexture_());
                Config.getTextureManager().bindTexture(this.textureLocation);
            }

            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
        }
    }

    @Inject(method = "renderWithRotation", at = @At("RETURN"))
    public void injectPostRenderWithRotation(float p_78785_1_, CallbackInfo ci, @Share("lastTextureId") LocalIntRef lastTextureId){
        if (!this.isHidden && this.showModel) {
            if (lastTextureId.get() != 0) {
                GlStateManager.bindTexture(lastTextureId.get());
            }
        }
    }

    @Inject(method = "postRender", at = @At("HEAD"))
    public void injectPrePostRender(float p_78785_1_, CallbackInfo ci){
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();
        }
    }

    /**
     * @author
     * @reason
     */
    @SideOnly(Side.CLIENT)
    @Overwrite
    private void compileDisplayList(float scale) {
        if (this.displayList == 0) {
            this.displayList = GLAllocation.generateDisplayLists(1);
        }

        GlStateManager.glNewList(this.displayList, 4864);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

        for (ModelBox box : cubeList) {
            box.render(bufferbuilder, scale);
        }

        for (ModelSprite sprite : spriteList) {
            sprite.render(Tessellator.getInstance(), scale);
        }

        GlStateManager.glEndList();
        this.compiled = true;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void addSprite(float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd) {
        this.spriteList.add(new ModelSprite((ModelRenderer)(Object)this, this.textureOffsetX, this.textureOffsetY, posX, posY, posZ, sizeX, sizeY, sizeZ, sizeAdd));
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public boolean getCompiled() {
        return this.compiled;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public int getDisplayList() {
        return this.displayList;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private void checkResetDisplayList() {
        if (this.countResetDisplayList != Shaders.countResetDisplayLists) {
            this.compiled = false;
            this.countResetDisplayList = Shaders.countResetDisplayLists;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void setTextureLocation(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public String getId() {
        return this.id;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void setId(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net/minecraft/client/model/ModelBox (Lnet/minecraft/client/model/ModelRenderer;[[IFFFFFFFZ)V")
    private native static ModelBox _new_ModelBox(AccessibleOperation.Construction construction, ModelRenderer renderer, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void addBox(int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta) {
        this.cubeList.add(_new_ModelBox(AccessibleOperation.Construction.construction(), _cast_this(), faceUvs, x, y, z, dx, dy, dz, delta, this.mirror));
    }

    @Unique
    @AccessibleOperation
    public ModelRenderer _cast_this(){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "getId ()I")
    private static int _acc_ModelRenderer_getId(ModelRenderer renderer){
        throw new AbstractMethodError();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    public ModelRenderer getChild(String name) {
        if (name == null) {
            return null;
        } else {
            if (this.childModels != null) {
                for (int i = 0; i < this.childModels.size(); i++) {
                    ModelRenderer child = this.childModels.get(i);
                    //TODO
                    if (name.equals(_acc_ModelRenderer_getId(child))) {
                        return child;
                    }
                }
            }

            return null;
        }
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "getChildDeep (Ljava/lang/String;)Lnet/minecraft/client/model/ModelRenderer;")
    private static native ModelRenderer _acc_ModelRenderer_getChildDeep(ModelRenderer renderer, String a);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public ModelRenderer getChildDeep(String name) {
        if (name == null) {
            return null;
        } else {
            ModelRenderer mrChild = this.getChild(name);
            if (mrChild != null) {
                return mrChild;
            } else {
                if (this.childModels != null) {
                    for (int i = 0; i < this.childModels.size(); i++) {
                        ModelRenderer child = this.childModels.get(i);
                        ModelRenderer mr = _acc_ModelRenderer_getChildDeep(child, name);
                        if (mr != null) {
                            return mr;
                        }
                    }
                }

                return null;
            }
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void setModelUpdater(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
    }

    @Override
    public String toString() {
        return "id: " + this.id + ", boxes: " + (this.cubeList != null ? this.cubeList.size() : null) + ", submodels: " + (this.childModels != null ? this.childModels.size() : null);
    }

}
/*
+++ net/minecraft/client/model/ModelRenderer.java	Tue Aug 19 14:59:58 2025
@@ -1,48 +1,68 @@
 package net.minecraft.client.model;

 import com.google.common.collect.Lists;
+import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GLAllocation;
 import net.minecraft.client.renderer.GlStateManager;
+import net.minecraft.client.renderer.RenderGlobal;
 import net.minecraft.client.renderer.Tessellator;
+import net.minecraft.util.ResourceLocation;
+import net.optifine.entity.model.anim.ModelUpdater;
+import net.optifine.model.ModelSprite;
+import net.optifine.shaders.Shaders;

 public class ModelRenderer {
-   public float textureWidth = 64.0F;
-   public float textureHeight = 32.0F;
+   public float textureWidth;
+   public float textureHeight;
    private int textureOffsetX;
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean compiled;
    private int displayList;
    public boolean mirror;
-   public boolean showModel = true;
+   public boolean showModel;
    public boolean isHidden;
-   public List<ModelBox> cubeList = Lists.newArrayList();
+   public List<ModelBox> cubeList;
    public List<ModelRenderer> childModels;
    public final String boxName;
    private final ModelBase baseModel;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
+   public List spriteList = new ArrayList();
+   public boolean mirrorV = false;
+   public float scaleX = 1.0F;
+   public float scaleY = 1.0F;
+   public float scaleZ = 1.0F;
+   private int countResetDisplayList;
+   private ResourceLocation textureLocation = null;
+   private String id = null;
+   private ModelUpdater modelUpdater;
+   private RenderGlobal renderGlobal = Config.getRenderGlobal();

    public ModelRenderer(ModelBase var1, String var2) {
+      this.textureWidth = 64.0F;
+      this.textureHeight = 32.0F;
+      this.showModel = true;
+      this.cubeList = Lists.newArrayList();
       this.baseModel = var1;
       var1.boxList.add(this);
       this.boxName = var2;
       this.setTextureSize(var1.textureWidth, var1.textureHeight);
    }

    public ModelRenderer(ModelBase var1) {
-      this(var1, null);
+      this(var1, (String)null);
    }

    public ModelRenderer(ModelBase var1, int var2, int var3) {
       this(var1);
       this.setTextureOffset(var2, var3);
    }
@@ -87,133 +107,300 @@
       this.rotationPointX = var1;
       this.rotationPointY = var2;
       this.rotationPointZ = var3;
    }

    public void render(float var1) {
-      if (!this.isHidden) {
-         if (this.showModel) {
-            if (!this.compiled) {
-               this.compileDisplayList(var1);
+      if (!this.isHidden && this.showModel) {
+         this.checkResetDisplayList();
+         if (!this.compiled) {
+            this.compileDisplayList(var1);
+         }
+
+         int var2 = 0;
+         if (this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
+            if (this.renderGlobal.renderOverlayEyes) {
+               return;
             }

-            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
-            if (this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F) {
-               GlStateManager.pushMatrix();
-               GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
-               if (this.rotateAngleZ != 0.0F) {
-                  GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
-               }
+            var2 = GlStateManager.getBoundTexture();
+            Config.getTextureManager().bindTexture(this.textureLocation);
+         }

-               if (this.rotateAngleY != 0.0F) {
-                  GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
-               }
+         if (this.modelUpdater != null) {
+            this.modelUpdater.update();
+         }

-               if (this.rotateAngleX != 0.0F) {
-                  GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
-               }
+         boolean var3 = this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F;
+         GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
+         if (this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F) {
+            GlStateManager.pushMatrix();
+            GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
+            if (this.rotateAngleZ != 0.0F) {
+               GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
+            }

-               GlStateManager.callList(this.displayList);
-               if (this.childModels != null) {
-                  for (int var4 = 0; var4 < this.childModels.size(); var4++) {
-                     this.childModels.get(var4).render(var1);
-                  }
+            if (this.rotateAngleY != 0.0F) {
+               GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
+            }
+
+            if (this.rotateAngleX != 0.0F) {
+               GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
+            }
+
+            if (var3) {
+               GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
+            }
+
+            GlStateManager.callList(this.displayList);
+            if (this.childModels != null) {
+               for (int var6 = 0; var6 < this.childModels.size(); var6++) {
+                  this.childModels.get(var6).render(var1);
                }
+            }

-               GlStateManager.popMatrix();
-            } else if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
-               GlStateManager.callList(this.displayList);
-               if (this.childModels != null) {
-                  for (int var3 = 0; var3 < this.childModels.size(); var3++) {
-                     this.childModels.get(var3).render(var1);
-                  }
+            GlStateManager.popMatrix();
+         } else if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
+            if (var3) {
+               GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
+            }
+
+            GlStateManager.callList(this.displayList);
+            if (this.childModels != null) {
+               for (int var5 = 0; var5 < this.childModels.size(); var5++) {
+                  this.childModels.get(var5).render(var1);
                }
-            } else {
-               GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
-               GlStateManager.callList(this.displayList);
-               if (this.childModels != null) {
-                  for (int var2 = 0; var2 < this.childModels.size(); var2++) {
-                     this.childModels.get(var2).render(var1);
-                  }
+            }
+
+            if (var3) {
+               GlStateManager.scale(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
+            }
+         } else {
+            GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
+            if (var3) {
+               GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
+            }
+
+            GlStateManager.callList(this.displayList);
+            if (this.childModels != null) {
+               for (int var4 = 0; var4 < this.childModels.size(); var4++) {
+                  this.childModels.get(var4).render(var1);
                }
+            }

-               GlStateManager.translate(-this.rotationPointX * var1, -this.rotationPointY * var1, -this.rotationPointZ * var1);
+            if (var3) {
+               GlStateManager.scale(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
             }

-            GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
+            GlStateManager.translate(-this.rotationPointX * var1, -this.rotationPointY * var1, -this.rotationPointZ * var1);
+         }
+
+         GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
+         if (var2 != 0) {
+            GlStateManager.bindTexture(var2);
          }
       }
    }

    public void renderWithRotation(float var1) {
-      if (!this.isHidden) {
-         if (this.showModel) {
-            if (!this.compiled) {
-               this.compileDisplayList(var1);
-            }
+      if (!this.isHidden && this.showModel) {
+         this.checkResetDisplayList();
+         if (!this.compiled) {
+            this.compileDisplayList(var1);
+         }

-            GlStateManager.pushMatrix();
-            GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
-            if (this.rotateAngleY != 0.0F) {
-               GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
+         int var2 = 0;
+         if (this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
+            if (this.renderGlobal.renderOverlayEyes) {
+               return;
             }

-            if (this.rotateAngleX != 0.0F) {
-               GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
-            }
+            var2 = GlStateManager.getBoundTexture();
+            Config.getTextureManager().bindTexture(this.textureLocation);
+         }

-            if (this.rotateAngleZ != 0.0F) {
-               GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
+         if (this.modelUpdater != null) {
+            this.modelUpdater.update();
+         }
+
+         boolean var3 = this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F;
+         GlStateManager.pushMatrix();
+         GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
+         if (this.rotateAngleY != 0.0F) {
+            GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
+         }
+
+         if (this.rotateAngleX != 0.0F) {
+            GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
+         }
+
+         if (this.rotateAngleZ != 0.0F) {
+            GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
+         }
+
+         if (var3) {
+            GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
+         }
+
+         GlStateManager.callList(this.displayList);
+         if (this.childModels != null) {
+            for (int var4 = 0; var4 < this.childModels.size(); var4++) {
+               this.childModels.get(var4).render(var1);
             }
+         }

-            GlStateManager.callList(this.displayList);
-            GlStateManager.popMatrix();
+         GlStateManager.popMatrix();
+         if (var2 != 0) {
+            GlStateManager.bindTexture(var2);
          }
       }
    }

    public void postRender(float var1) {
-      if (!this.isHidden) {
-         if (this.showModel) {
-            if (!this.compiled) {
-               this.compileDisplayList(var1);
-            }
+      if (!this.isHidden && this.showModel) {
+         this.checkResetDisplayList();
+         if (!this.compiled) {
+            this.compileDisplayList(var1);
+         }

-            if (this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F) {
-               GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
-               if (this.rotateAngleZ != 0.0F) {
-                  GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
-               }
+         if (this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F) {
+            GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
+            if (this.rotateAngleZ != 0.0F) {
+               GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
+            }

-               if (this.rotateAngleY != 0.0F) {
-                  GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
-               }
+            if (this.rotateAngleY != 0.0F) {
+               GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
+            }

-               if (this.rotateAngleX != 0.0F) {
-                  GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
-               }
-            } else if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
-               GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
+            if (this.rotateAngleX != 0.0F) {
+               GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
             }
+         } else if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
+            GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
          }
       }
    }

    private void compileDisplayList(float var1) {
-      this.displayList = GLAllocation.generateDisplayLists(1);
+      if (this.displayList == 0) {
+         this.displayList = GLAllocation.generateDisplayLists(1);
+      }
+
       GlStateManager.glNewList(this.displayList, 4864);
       BufferBuilder var2 = Tessellator.getInstance().getBuffer();

       for (int var3 = 0; var3 < this.cubeList.size(); var3++) {
          this.cubeList.get(var3).render(var2, var1);
       }

+      for (int var5 = 0; var5 < this.spriteList.size(); var5++) {
+         ModelSprite var4 = (ModelSprite)this.spriteList.get(var5);
+         var4.render(Tessellator.getInstance(), var1);
+      }
+
       GlStateManager.glEndList();
       this.compiled = true;
    }

    public ModelRenderer setTextureSize(int var1, int var2) {
       this.textureWidth = var1;
       this.textureHeight = var2;
       return this;
+   }
+
+   public void addSprite(float var1, float var2, float var3, int var4, int var5, int var6, float var7) {
+      this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, var1, var2, var3, var4, var5, var6, var7));
+   }
+
+   public boolean getCompiled() {
+      return this.compiled;
+   }
+
+   public int getDisplayList() {
+      return this.displayList;
+   }
+
+   private void checkResetDisplayList() {
+      if (this.countResetDisplayList != Shaders.countResetDisplayLists) {
+         this.compiled = false;
+         this.countResetDisplayList = Shaders.countResetDisplayLists;
+      }
+   }
+
+   public ResourceLocation getTextureLocation() {
+      return this.textureLocation;
+   }
+
+   public void setTextureLocation(ResourceLocation var1) {
+      this.textureLocation = var1;
+   }
+
+   public String getId() {
+      return this.id;
+   }
+
+   public void setId(String var1) {
+      this.id = var1;
+   }
+
+   public void addBox(int[][] var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
+      this.cubeList.add(new ModelBox(this, var1, var2, var3, var4, var5, var6, var7, var8, this.mirror));
+   }
+
+   public ModelRenderer getChild(String var1) {
+      if (var1 == null) {
+         return null;
+      } else {
+         if (this.childModels != null) {
+            for (int var2 = 0; var2 < this.childModels.size(); var2++) {
+               ModelRenderer var3 = this.childModels.get(var2);
+               if (var1.equals(var3.getId())) {
+                  return var3;
+               }
+            }
+         }
+
+         return null;
+      }
+   }
+
+   public ModelRenderer getChildDeep(String var1) {
+      if (var1 == null) {
+         return null;
+      } else {
+         ModelRenderer var2 = this.getChild(var1);
+         if (var2 != null) {
+            return var2;
+         } else {
+            if (this.childModels != null) {
+               for (int var3 = 0; var3 < this.childModels.size(); var3++) {
+                  ModelRenderer var4 = this.childModels.get(var3);
+                  ModelRenderer var5 = var4.getChildDeep(var1);
+                  if (var5 != null) {
+                     return var5;
+                  }
+               }
+            }
+
+            return null;
+         }
+      }
+   }
+
+   public void setModelUpdater(ModelUpdater var1) {
+      this.modelUpdater = var1;
+   }
+
+   public String toString() {
+      StringBuffer var1 = new StringBuffer();
+      var1.append(
+         "id: "
+            + this.id
+            + ", boxes: "
+            + (this.cubeList != null ? this.cubeList.size() : null)
+            + ", submodels: "
+            + (this.childModels != null ? this.childModels.size() : null)
+      );
+      return var1.toString();
    }
 }
* */
