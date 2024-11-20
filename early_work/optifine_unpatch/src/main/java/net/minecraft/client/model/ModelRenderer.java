/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBox
 *  net.minecraft.client.model.TextureOffset
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public List<ModelBox> cubeList = Lists.newArrayList();
    public List<ModelRenderer> childModels;
    public final String boxName;
    private final ModelBase baseModel;
    public float offsetX;
    public float offsetY;
    public float offsetZ;

    public ModelRenderer(ModelBase model, String boxNameIn) {
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

    @SideOnly(value=Side.CLIENT)
    public void render(float scale) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            GlStateManager.translate((float)this.offsetX, (float)this.offsetY, (float)this.offsetZ);
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX == 0.0f && this.rotationPointY == 0.0f && this.rotationPointZ == 0.0f) {
                    GlStateManager.callList((int)this.displayList);
                    if (this.childModels != null) {
                        for (int k = 0; k < this.childModels.size(); ++k) {
                            ((ModelRenderer)this.childModels.get(k)).render(scale);
                        }
                    }
                } else {
                    GlStateManager.translate((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
                    GlStateManager.callList((int)this.displayList);
                    if (this.childModels != null) {
                        for (int j = 0; j < this.childModels.size(); ++j) {
                            ((ModelRenderer)this.childModels.get(j)).render(scale);
                        }
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
                GlStateManager.callList((int)this.displayList);
                if (this.childModels != null) {
                    for (int i = 0; i < this.childModels.size(); ++i) {
                        ((ModelRenderer)this.childModels.get(i)).render(scale);
                    }
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.translate((float)(-this.offsetX), (float)(-this.offsetY), (float)(-this.offsetZ));
        }
    }

    @SideOnly(value=Side.CLIENT)
    public void renderWithRotation(float scale) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
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
            GlStateManager.callList((int)this.displayList);
            GlStateManager.popMatrix();
        }
    }

    @SideOnly(value=Side.CLIENT)
    public void postRender(float scale) {
        if (!this.isHidden && this.showModel) {
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

    @SideOnly(value=Side.CLIENT)
    private void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists((int)1);
        GlStateManager.glNewList((int)this.displayList, (int)4864);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        for (int i = 0; i < this.cubeList.size(); ++i) {
            ((ModelBox)this.cubeList.get(i)).render(bufferbuilder, scale);
        }
        GlStateManager.glEndList();
        this.compiled = true;
    }

    public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        return this;
    }
}
