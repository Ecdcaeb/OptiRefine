/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Lists
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.List
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBox
 *  net.minecraft.client.model.TextureOffset
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.entity.model.anim.ModelUpdater
 *  net.optifine.model.ModelSprite
 *  net.optifine.shaders.Shaders
 */
package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.anim.ModelUpdater;
import net.optifine.model.ModelSprite;
import net.optifine.shaders.Shaders;

public class ModelRenderer {
    public float textureWidth = 64.0f;
    public float textureHeight = 32.0f;
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
    public boolean showModel = true;
    public boolean isHidden;
    public List<ModelBox> cubeList;
    public List<ModelRenderer> childModels;
    public final String boxName;
    private final ModelBase baseModel;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public List spriteList = new ArrayList();
    public boolean mirrorV = false;
    public float scaleX = 1.0f;
    public float scaleY = 1.0f;
    public float scaleZ = 1.0f;
    private int countResetDisplayList;
    private ResourceLocation textureLocation = null;
    private String id = null;
    private ModelUpdater modelUpdater;
    private RenderGlobal renderGlobal = Config.getRenderGlobal();

    public ModelRenderer(ModelBase model, String boxNameIn) {
        this.cubeList = Lists.newArrayList();
        this.baseModel = model;
        model.boxList.add((Object)this);
        this.boxName = boxNameIn;
        this.setTextureSize(model.textureWidth, model.textureHeight);
    }

    public ModelRenderer(ModelBase model) {
        this(model, null);
    }

    public ModelRenderer(ModelBase model, int texOffX, int texOffY) {
        this(model);
        this.setTextureOffset(texOffX, texOffY);
    }

    public void addChild(ModelRenderer renderer) {
        if (this.childModels == null) {
            this.childModels = Lists.newArrayList();
        }
        this.childModels.add((Object)renderer);
    }

    public ModelRenderer setTextureOffset(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth) {
        partName = this.boxName + "." + partName;
        TextureOffset textureoffset = this.baseModel.getTextureOffset(partName);
        this.setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
        this.cubeList.add((Object)new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0f).setBoxName(partName));
        return this;
    }

    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
        this.cubeList.add((Object)new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0f));
        return this;
    }

    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored) {
        this.cubeList.add((Object)new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0f, mirrored));
        return this;
    }

    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor) {
        this.cubeList.add((Object)new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, scaleFactor));
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.rotationPointX = rotationPointXIn;
        this.rotationPointY = rotationPointYIn;
        this.rotationPointZ = rotationPointZIn;
    }

    public void render(float scale) {
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            int lastTextureId = 0;
            if (this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
                if (this.renderGlobal.renderOverlayEyes) {
                    return;
                }
                lastTextureId = GlStateManager.getBoundTexture();
                Config.getTextureManager().bindTexture(this.textureLocation);
            }
            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
            boolean scaleXYZ = this.scaleX != 1.0f || this.scaleY != 1.0f || this.scaleZ != 1.0f;
            GlStateManager.translate((float)this.offsetX, (float)this.offsetY, (float)this.offsetZ);
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX == 0.0f && this.rotationPointY == 0.0f && this.rotationPointZ == 0.0f) {
                    if (scaleXYZ) {
                        GlStateManager.scale((float)this.scaleX, (float)this.scaleY, (float)this.scaleZ);
                    }
                    GlStateManager.callList((int)this.displayList);
                    if (this.childModels != null) {
                        for (int k = 0; k < this.childModels.size(); ++k) {
                            ((ModelRenderer)this.childModels.get(k)).render(scale);
                        }
                    }
                    if (scaleXYZ) {
                        GlStateManager.scale((float)(1.0f / this.scaleX), (float)(1.0f / this.scaleY), (float)(1.0f / this.scaleZ));
                    }
                } else {
                    GlStateManager.translate((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
                    if (scaleXYZ) {
                        GlStateManager.scale((float)this.scaleX, (float)this.scaleY, (float)this.scaleZ);
                    }
                    GlStateManager.callList((int)this.displayList);
                    if (this.childModels != null) {
                        for (int j = 0; j < this.childModels.size(); ++j) {
                            ((ModelRenderer)this.childModels.get(j)).render(scale);
                        }
                    }
                    if (scaleXYZ) {
                        GlStateManager.scale((float)(1.0f / this.scaleX), (float)(1.0f / this.scaleY), (float)(1.0f / this.scaleZ));
                    }
                    GlStateManager.translate((float)(-this.rotationPointX * scale), (float)(-this.rotationPointY * scale), (float)(-this.rotationPointZ * scale));
                }
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
                if (this.rotateAngleZ != 0.0f) {
                    GlStateManager.rotate((float)(this.rotateAngleZ * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
                }
                if (this.rotateAngleY != 0.0f) {
                    GlStateManager.rotate((float)(this.rotateAngleY * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (this.rotateAngleX != 0.0f) {
                    GlStateManager.rotate((float)(this.rotateAngleX * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (scaleXYZ) {
                    GlStateManager.scale((float)this.scaleX, (float)this.scaleY, (float)this.scaleZ);
                }
                GlStateManager.callList((int)this.displayList);
                if (this.childModels != null) {
                    for (int i = 0; i < this.childModels.size(); ++i) {
                        ((ModelRenderer)this.childModels.get(i)).render(scale);
                    }
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.translate((float)(-this.offsetX), (float)(-this.offsetY), (float)(-this.offsetZ));
            if (lastTextureId != 0) {
                GlStateManager.bindTexture((int)lastTextureId);
            }
        }
    }

    public void renderWithRotation(float scale) {
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            int lastTextureId = 0;
            if (this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
                if (this.renderGlobal.renderOverlayEyes) {
                    return;
                }
                lastTextureId = GlStateManager.getBoundTexture();
                Config.getTextureManager().bindTexture(this.textureLocation);
            }
            if (this.modelUpdater != null) {
                this.modelUpdater.update();
            }
            boolean scaleXYZ = this.scaleX != 1.0f || this.scaleY != 1.0f || this.scaleZ != 1.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
            if (this.rotateAngleY != 0.0f) {
                GlStateManager.rotate((float)(this.rotateAngleY * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (this.rotateAngleX != 0.0f) {
                GlStateManager.rotate((float)(this.rotateAngleX * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (this.rotateAngleZ != 0.0f) {
                GlStateManager.rotate((float)(this.rotateAngleZ * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            if (scaleXYZ) {
                GlStateManager.scale((float)this.scaleX, (float)this.scaleY, (float)this.scaleZ);
            }
            GlStateManager.callList((int)this.displayList);
            if (this.childModels != null) {
                for (int i = 0; i < this.childModels.size(); ++i) {
                    ((ModelRenderer)this.childModels.get(i)).render(scale);
                }
            }
            GlStateManager.popMatrix();
            if (lastTextureId != 0) {
                GlStateManager.bindTexture((int)lastTextureId);
            }
        }
    }

    public void postRender(float scale) {
        if (!this.isHidden && this.showModel) {
            this.checkResetDisplayList();
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX != 0.0f || this.rotationPointY != 0.0f || this.rotationPointZ != 0.0f) {
                    GlStateManager.translate((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
                }
            } else {
                GlStateManager.translate((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
                if (this.rotateAngleZ != 0.0f) {
                    GlStateManager.rotate((float)(this.rotateAngleZ * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
                }
                if (this.rotateAngleY != 0.0f) {
                    GlStateManager.rotate((float)(this.rotateAngleY * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (this.rotateAngleX != 0.0f) {
                    GlStateManager.rotate((float)(this.rotateAngleX * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
            }
        }
    }

    private void compileDisplayList(float scale) {
        int i;
        if (this.displayList == 0) {
            this.displayList = GLAllocation.generateDisplayLists((int)1);
        }
        GlStateManager.glNewList((int)this.displayList, (int)4864);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        for (i = 0; i < this.cubeList.size(); ++i) {
            ((ModelBox)this.cubeList.get(i)).render(bufferbuilder, scale);
        }
        for (i = 0; i < this.spriteList.size(); ++i) {
            ModelSprite sprite = (ModelSprite)this.spriteList.get(i);
            sprite.render(Tessellator.getInstance(), scale);
        }
        GlStateManager.glEndList();
        this.compiled = true;
    }

    public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        return this;
    }

    public void addSprite(float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd) {
        this.spriteList.add((Object)new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, posX, posY, posZ, sizeX, sizeY, sizeZ, sizeAdd));
    }

    public boolean getCompiled() {
        return this.compiled;
    }

    public int getDisplayList() {
        return this.displayList;
    }

    private void checkResetDisplayList() {
        if (this.countResetDisplayList != Shaders.countResetDisplayLists) {
            this.compiled = false;
            this.countResetDisplayList = Shaders.countResetDisplayLists;
        }
    }

    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    public void setTextureLocation(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addBox(int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta) {
        this.cubeList.add((Object)new ModelBox(this, faceUvs, x, y, z, dx, dy, dz, delta, this.mirror));
    }

    public ModelRenderer getChild(String name) {
        if (name == null) {
            return null;
        }
        if (this.childModels != null) {
            for (int i = 0; i < this.childModels.size(); ++i) {
                ModelRenderer child = (ModelRenderer)this.childModels.get(i);
                if (!name.equals((Object)child.getId())) continue;
                return child;
            }
        }
        return null;
    }

    public ModelRenderer getChildDeep(String name) {
        if (name == null) {
            return null;
        }
        ModelRenderer mrChild = this.getChild(name);
        if (mrChild != null) {
            return mrChild;
        }
        if (this.childModels != null) {
            for (int i = 0; i < this.childModels.size(); ++i) {
                ModelRenderer child = (ModelRenderer)this.childModels.get(i);
                ModelRenderer mr = child.getChildDeep(name);
                if (mr == null) continue;
                return mr;
            }
        }
        return null;
    }

    public void setModelUpdater(ModelUpdater modelUpdater) {
        this.modelUpdater = modelUpdater;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id: " + this.id + ", boxes: " + (this.cubeList != null ? Integer.valueOf((int)this.cubeList.size()) : null) + ", submodels: " + (this.childModels != null ? Integer.valueOf((int)this.childModels.size()) : null));
        return sb.toString();
    }
}