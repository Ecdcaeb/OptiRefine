/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiUtilRenderComponents
 *  net.minecraft.client.model.ModelSign
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.tileentity.TileEntitySign
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.text.ITextComponent
 *  net.optifine.CustomColors
 *  net.optifine.shaders.Shaders
 */
package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.optifine.CustomColors;
import net.optifine.shaders.Shaders;

public class TileEntitySignRenderer
extends TileEntitySpecialRenderer<TileEntitySign> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    private final ModelSign model = new ModelSign();
    private static double textRenderDistanceSq = 4096.0;

    public void render(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Block block = te.x();
        GlStateManager.pushMatrix();
        float f = 0.6666667f;
        if (block == Blocks.STANDING_SIGN) {
            GlStateManager.translate((float)((float)x + 0.5f), (float)((float)y + 0.5f), (float)((float)z + 0.5f));
            float f1 = (float)(te.v() * 360) / 16.0f;
            GlStateManager.rotate((float)(-f1), (float)0.0f, (float)1.0f, (float)0.0f);
            this.model.signStick.showModel = true;
        } else {
            int k = te.v();
            float f2 = 0.0f;
            if (k == 2) {
                f2 = 180.0f;
            }
            if (k == 4) {
                f2 = 90.0f;
            }
            if (k == 5) {
                f2 = -90.0f;
            }
            GlStateManager.translate((float)((float)x + 0.5f), (float)((float)y + 0.5f), (float)((float)z + 0.5f));
            GlStateManager.rotate((float)(-f2), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)0.0f, (float)-0.3125f, (float)-0.4375f);
            this.model.signStick.showModel = false;
        }
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode((int)5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)4.0f, (float)2.0f, (float)1.0f);
            GlStateManager.translate((float)0.0625f, (float)0.0625f, (float)0.0625f);
            GlStateManager.matrixMode((int)5888);
        } else {
            this.bindTexture(SIGN_TEXTURE);
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)0.6666667f, (float)-0.6666667f, (float)-0.6666667f);
        this.model.renderSign();
        GlStateManager.popMatrix();
        if (TileEntitySignRenderer.isRenderText(te)) {
            FontRenderer fontrenderer = this.getFontRenderer();
            float f3 = 0.010416667f;
            GlStateManager.translate((float)0.0f, (float)0.33333334f, (float)0.046666667f);
            GlStateManager.scale((float)0.010416667f, (float)-0.010416667f, (float)0.010416667f);
            GlStateManager.glNormal3f((float)0.0f, (float)0.0f, (float)-0.010416667f);
            GlStateManager.depthMask((boolean)false);
            int i = 0;
            if (Config.isCustomColors()) {
                i = CustomColors.getSignTextColor((int)i);
            }
            if (destroyStage < 0) {
                for (int j = 0; j < te.signText.length; ++j) {
                    String s;
                    if (te.signText[j] == null) continue;
                    ITextComponent itextcomponent = te.signText[j];
                    List list = GuiUtilRenderComponents.splitText((ITextComponent)itextcomponent, (int)90, (FontRenderer)fontrenderer, (boolean)false, (boolean)true);
                    String string = s = list != null && !list.isEmpty() ? ((ITextComponent)list.get(0)).getFormattedText() : "";
                    if (j == te.lineBeingEdited) {
                        s = "> " + s + " <";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                        continue;
                    }
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                }
            }
        }
        GlStateManager.depthMask((boolean)true);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode((int)5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5888);
        }
    }

    private static boolean isRenderText(TileEntitySign tileEntity) {
        if (Shaders.isShadowPass) {
            return false;
        }
        if (!Config.zoomMode && tileEntity.lineBeingEdited < 0) {
            Entity viewEntity = Config.getMinecraft().getRenderViewEntity();
            double distSq = tileEntity.a(viewEntity.posX, viewEntity.posY, viewEntity.posZ);
            if (distSq > textRenderDistanceSq) {
                return false;
            }
        }
        return true;
    }

    public static void updateTextRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit((float)mc.gameSettings.fovSetting, (float)1.0f, (float)120.0f);
        double textRenderDistance = Math.max((double)(1.5 * (double)mc.displayHeight / fov), (double)16.0);
        textRenderDistanceSq = textRenderDistance * textRenderDistance;
    }
}
