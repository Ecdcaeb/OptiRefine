/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.BlockRendererDispatcher
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$CullFace
 *  net.minecraft.client.renderer.entity.RenderMooshroom
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.entity.passive.EntityMooshroom
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class LayerMooshroomMushroom
implements LayerRenderer<EntityMooshroom> {
    private final RenderMooshroom mooshroomRenderer;
    private ModelRenderer modelRendererMushroom;
    private static final ResourceLocation LOCATION_MUSHROOM_RED = new ResourceLocation("textures/entity/cow/mushroom_red.png");
    private static boolean hasTextureMushroom = false;

    public static void update() {
        hasTextureMushroom = Config.hasResource((ResourceLocation)LOCATION_MUSHROOM_RED);
    }

    public LayerMooshroomMushroom(RenderMooshroom mooshroomRendererIn) {
        this.mooshroomRenderer = mooshroomRendererIn;
        this.modelRendererMushroom = new ModelRenderer(this.mooshroomRenderer.f);
        this.modelRendererMushroom.setTextureSize(16, 16);
        this.modelRendererMushroom.rotationPointX = -6.0f;
        this.modelRendererMushroom.rotationPointZ = -8.0f;
        this.modelRendererMushroom.rotateAngleY = MathHelper.PI / 4.0f;
        int[][] faceUvs = new int[6][];
        faceUvs[2] = new int[]{16, 16, 0, 0};
        faceUvs[3] = new int[]{16, 16, 0, 0};
        this.modelRendererMushroom.addBox((int[][])faceUvs, 0.0f, 0.0f, 10.0f, 20.0f, 16.0f, 0.0f, 0.0f);
        int[][] faceUvs2 = new int[6][];
        faceUvs2[4] = new int[]{16, 16, 0, 0};
        faceUvs2[5] = new int[]{16, 16, 0, 0};
        this.modelRendererMushroom.addBox((int[][])faceUvs2, 10.0f, 0.0f, 0.0f, 0.0f, 16.0f, 20.0f, 0.0f);
    }

    public void doRenderLayer(EntityMooshroom entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitylivingbaseIn.l_() && !entitylivingbaseIn.aX()) {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            if (hasTextureMushroom) {
                this.mooshroomRenderer.a(LOCATION_MUSHROOM_RED);
            } else {
                this.mooshroomRenderer.a(TextureMap.LOCATION_BLOCKS_TEXTURE);
            }
            GlStateManager.enableCull();
            GlStateManager.cullFace((GlStateManager.CullFace)GlStateManager.CullFace.FRONT);
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)1.0f, (float)-1.0f, (float)1.0f);
            GlStateManager.translate((float)0.2f, (float)0.35f, (float)0.5f);
            GlStateManager.rotate((float)42.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.5f);
            if (hasTextureMushroom) {
                this.modelRendererMushroom.render(0.0625f);
            } else {
                blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.t(), 1.0f);
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)0.1f, (float)0.0f, (float)-0.6f);
            GlStateManager.rotate((float)42.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.5f);
            if (hasTextureMushroom) {
                this.modelRendererMushroom.render(0.0625f);
            } else {
                blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.t(), 1.0f);
            }
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            this.mooshroomRenderer.getMainModel().a.postRender(0.0625f);
            GlStateManager.scale((float)1.0f, (float)-1.0f, (float)1.0f);
            GlStateManager.translate((float)0.0f, (float)0.7f, (float)-0.2f);
            GlStateManager.rotate((float)12.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.5f);
            if (hasTextureMushroom) {
                this.modelRendererMushroom.render(0.0625f);
            } else {
                blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.t(), 1.0f);
            }
            GlStateManager.popMatrix();
            GlStateManager.cullFace((GlStateManager.CullFace)GlStateManager.CullFace.BACK);
            GlStateManager.disableCull();
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
