/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumHandSide
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

public class ModelPlayer
extends ModelBiped {
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private final ModelRenderer bipedCape;
    private final ModelRenderer bipedDeadmau5Head;
    private final boolean smallArms;

    public ModelPlayer(float f, boolean bl) {
        super(f, 0.0f, 64, 64);
        this.smallArms = bl;
        this.bipedDeadmau5Head = new ModelRenderer((ModelBase)this, 24, 0);
        this.bipedDeadmau5Head.addBox(-3.0f, -6.0f, -1.0f, 6, 6, 1, f);
        this.bipedCape = new ModelRenderer((ModelBase)this, 0, 0);
        this.bipedCape.setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, f);
        if (bl) {
            this.bipedLeftArm = new ModelRenderer((ModelBase)this, 32, 48);
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, f);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.5f, 0.0f);
            this.bipedRightArm = new ModelRenderer((ModelBase)this, 40, 16);
            this.bipedRightArm.addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, f);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.5f, 0.0f);
            this.bipedLeftArmwear = new ModelRenderer((ModelBase)this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, f + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.5f, 0.0f);
            this.bipedRightArmwear = new ModelRenderer((ModelBase)this, 40, 32);
            this.bipedRightArmwear.addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, f + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.5f, 10.0f);
        } else {
            this.bipedLeftArm = new ModelRenderer((ModelBase)this, 32, 48);
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, f);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            this.bipedLeftArmwear = new ModelRenderer((ModelBase)this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, f + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.0f, 0.0f);
            this.bipedRightArmwear = new ModelRenderer((ModelBase)this, 40, 32);
            this.bipedRightArmwear.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, f + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.0f, 10.0f);
        }
        this.bipedLeftLeg = new ModelRenderer((ModelBase)this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.bipedLeftLegwear = new ModelRenderer((ModelBase)this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f + 0.25f);
        this.bipedLeftLegwear.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.bipedRightLegwear = new ModelRenderer((ModelBase)this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f + 0.25f);
        this.bipedRightLegwear.setRotationPoint(-1.9f, 12.0f, 0.0f);
        this.bipedBodyWear = new ModelRenderer((ModelBase)this, 16, 32);
        this.bipedBodyWear.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, f + 0.25f);
        this.bipedBodyWear.setRotationPoint(0.0f, 0.0f, 0.0f);
    }

    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        super.render(entity, f, f2, f3, f4, f5, f6);
        GlStateManager.pushMatrix();
        if (this.q) {
            \u2603 = 2.0f;
            GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
            GlStateManager.translate((float)0.0f, (float)(24.0f * f6), (float)0.0f);
            this.bipedLeftLegwear.render(f6);
            this.bipedRightLegwear.render(f6);
            this.bipedLeftArmwear.render(f6);
            this.bipedRightArmwear.render(f6);
            this.bipedBodyWear.render(f6);
        } else {
            if (entity.isSneaking()) {
                GlStateManager.translate((float)0.0f, (float)0.2f, (float)0.0f);
            }
            this.bipedLeftLegwear.render(f6);
            this.bipedRightLegwear.render(f6);
            this.bipedLeftArmwear.render(f6);
            this.bipedRightArmwear.render(f6);
            this.bipedBodyWear.render(f6);
        }
        GlStateManager.popMatrix();
    }

    public void renderDeadmau5Head(float f) {
        ModelPlayer.a((ModelRenderer)this.bipedHead, (ModelRenderer)this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0f;
        this.bipedDeadmau5Head.rotationPointY = 0.0f;
        this.bipedDeadmau5Head.render(f);
    }

    public void renderCape(float f) {
        this.bipedCape.render(f);
    }

    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        super.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        ModelPlayer.a((ModelRenderer)this.bipedLeftLeg, (ModelRenderer)this.bipedLeftLegwear);
        ModelPlayer.a((ModelRenderer)this.bipedRightLeg, (ModelRenderer)this.bipedRightLegwear);
        ModelPlayer.a((ModelRenderer)this.bipedLeftArm, (ModelRenderer)this.bipedLeftArmwear);
        ModelPlayer.a((ModelRenderer)this.bipedRightArm, (ModelRenderer)this.bipedRightArmwear);
        ModelPlayer.a((ModelRenderer)this.bipedBody, (ModelRenderer)this.bipedBodyWear);
        this.bipedCape.rotationPointY = entity.isSneaking() ? 2.0f : 0.0f;
    }

    public void setVisible(boolean bl) {
        super.setVisible(bl);
        this.bipedLeftArmwear.showModel = bl;
        this.bipedRightArmwear.showModel = bl;
        this.bipedLeftLegwear.showModel = bl;
        this.bipedRightLegwear.showModel = bl;
        this.bipedBodyWear.showModel = bl;
        this.bipedCape.showModel = bl;
        this.bipedDeadmau5Head.showModel = bl;
    }

    public void postRenderArm(float f, EnumHandSide enumHandSide) {
        ModelRenderer modelRenderer = this.getArmForSide(enumHandSide);
        if (this.smallArms) {
            float f2 = 0.5f * (float)(enumHandSide == EnumHandSide.RIGHT ? 1 : -1);
            modelRenderer.rotationPointX += f2;
            modelRenderer.postRender(f);
            modelRenderer.rotationPointX -= f2;
        } else {
            modelRenderer.postRender(f);
        }
    }
}
