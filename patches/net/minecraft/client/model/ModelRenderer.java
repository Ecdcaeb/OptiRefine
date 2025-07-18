package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

public class ModelRenderer {
   public float textureWidth = 64.0F;
   public float textureHeight = 32.0F;
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

   public ModelRenderer(ModelBase var1, String var2) {
      this.baseModel = ☃;
      ☃.boxList.add(this);
      this.boxName = ☃;
      this.setTextureSize(☃.textureWidth, ☃.textureHeight);
   }

   public ModelRenderer(ModelBase var1) {
      this(☃, null);
   }

   public ModelRenderer(ModelBase var1, int var2, int var3) {
      this(☃);
      this.setTextureOffset(☃, ☃);
   }

   public void addChild(ModelRenderer var1) {
      if (this.childModels == null) {
         this.childModels = Lists.newArrayList();
      }

      this.childModels.add(☃);
   }

   public ModelRenderer setTextureOffset(int var1, int var2) {
      this.textureOffsetX = ☃;
      this.textureOffsetY = ☃;
      return this;
   }

   public ModelRenderer addBox(String var1, float var2, float var3, float var4, int var5, int var6, int var7) {
      ☃ = this.boxName + "." + ☃;
      TextureOffset ☃ = this.baseModel.getTextureOffset(☃);
      this.setTextureOffset(☃.textureOffsetX, ☃.textureOffsetY);
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, ☃, ☃, ☃, ☃, ☃, ☃, 0.0F).setBoxName(☃));
      return this;
   }

   public ModelRenderer addBox(float var1, float var2, float var3, int var4, int var5, int var6) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, ☃, ☃, ☃, ☃, ☃, ☃, 0.0F));
      return this;
   }

   public ModelRenderer addBox(float var1, float var2, float var3, int var4, int var5, int var6, boolean var7) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, ☃, ☃, ☃, ☃, ☃, ☃, 0.0F, ☃));
      return this;
   }

   public void addBox(float var1, float var2, float var3, int var4, int var5, int var6, float var7) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, ☃, ☃, ☃, ☃, ☃, ☃, ☃));
   }

   public void setRotationPoint(float var1, float var2, float var3) {
      this.rotationPointX = ☃;
      this.rotationPointY = ☃;
      this.rotationPointZ = ☃;
   }

   public void render(float var1) {
      if (!this.isHidden) {
         if (this.showModel) {
            if (!this.compiled) {
               this.compileDisplayList(☃);
            }

            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
            if (this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.rotationPointX * ☃, this.rotationPointY * ☃, this.rotationPointZ * ☃);
               if (this.rotateAngleZ != 0.0F) {
                  GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
               }

               if (this.rotateAngleY != 0.0F) {
                  GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
               }

               if (this.rotateAngleX != 0.0F) {
                  GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
               }

               GlStateManager.callList(this.displayList);
               if (this.childModels != null) {
                  for (int ☃ = 0; ☃ < this.childModels.size(); ☃++) {
                     this.childModels.get(☃).render(☃);
                  }
               }

               GlStateManager.popMatrix();
            } else if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
               GlStateManager.callList(this.displayList);
               if (this.childModels != null) {
                  for (int ☃ = 0; ☃ < this.childModels.size(); ☃++) {
                     this.childModels.get(☃).render(☃);
                  }
               }
            } else {
               GlStateManager.translate(this.rotationPointX * ☃, this.rotationPointY * ☃, this.rotationPointZ * ☃);
               GlStateManager.callList(this.displayList);
               if (this.childModels != null) {
                  for (int ☃ = 0; ☃ < this.childModels.size(); ☃++) {
                     this.childModels.get(☃).render(☃);
                  }
               }

               GlStateManager.translate(-this.rotationPointX * ☃, -this.rotationPointY * ☃, -this.rotationPointZ * ☃);
            }

            GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
         }
      }
   }

   public void renderWithRotation(float var1) {
      if (!this.isHidden) {
         if (this.showModel) {
            if (!this.compiled) {
               this.compileDisplayList(☃);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * ☃, this.rotationPointY * ☃, this.rotationPointZ * ☃);
            if (this.rotateAngleY != 0.0F) {
               GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
               GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            }

            if (this.rotateAngleZ != 0.0F) {
               GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
            }

            GlStateManager.callList(this.displayList);
            GlStateManager.popMatrix();
         }
      }
   }

   public void postRender(float var1) {
      if (!this.isHidden) {
         if (this.showModel) {
            if (!this.compiled) {
               this.compileDisplayList(☃);
            }

            if (this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F) {
               GlStateManager.translate(this.rotationPointX * ☃, this.rotationPointY * ☃, this.rotationPointZ * ☃);
               if (this.rotateAngleZ != 0.0F) {
                  GlStateManager.rotate(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
               }

               if (this.rotateAngleY != 0.0F) {
                  GlStateManager.rotate(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
               }

               if (this.rotateAngleX != 0.0F) {
                  GlStateManager.rotate(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
               }
            } else if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
               GlStateManager.translate(this.rotationPointX * ☃, this.rotationPointY * ☃, this.rotationPointZ * ☃);
            }
         }
      }
   }

   private void compileDisplayList(float var1) {
      this.displayList = GLAllocation.generateDisplayLists(1);
      GlStateManager.glNewList(this.displayList, 4864);
      BufferBuilder ☃ = Tessellator.getInstance().getBuffer();

      for (int ☃x = 0; ☃x < this.cubeList.size(); ☃x++) {
         this.cubeList.get(☃x).render(☃, ☃);
      }

      GlStateManager.glEndList();
      this.compiled = true;
   }

   public ModelRenderer setTextureSize(int var1, int var2) {
      this.textureWidth = ☃;
      this.textureHeight = ☃;
      return this;
   }
}
