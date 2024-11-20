/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiUtilRenderComponents
 *  net.minecraft.client.model.ModelSign
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.init.Blocks
 *  net.minecraft.tileentity.TileEntitySign
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.text.ITextComponent
 */
package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TileEntitySignRenderer
extends TileEntitySpecialRenderer<TileEntitySign> {
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    private final ModelSign model = new ModelSign();

    public void render(TileEntitySign tileEntitySign2, double d, double d2, double d3, float f, int n2, float f2) {
        int n2;
        float \u26034;
        Block block = tileEntitySign2.x();
        GlStateManager.pushMatrix();
        float \u26032 = 0.6666667f;
        if (block == Blocks.STANDING_SIGN) {
            GlStateManager.translate((float)((float)d + 0.5f), (float)((float)d2 + 0.5f), (float)((float)d3 + 0.5f));
            float f3 = (float)(tileEntitySign2.v() * 360) / 16.0f;
            GlStateManager.rotate((float)(-f3), (float)0.0f, (float)1.0f, (float)0.0f);
            this.model.signStick.showModel = true;
        } else {
            TileEntitySign tileEntitySign2;
            int \u26033 = tileEntitySign2.v();
            \u26034 = 0.0f;
            if (\u26033 == 2) {
                \u26034 = 180.0f;
            }
            if (\u26033 == 4) {
                \u26034 = 90.0f;
            }
            if (\u26033 == 5) {
                \u26034 = -90.0f;
            }
            GlStateManager.translate((float)((float)d + 0.5f), (float)((float)d2 + 0.5f), (float)((float)d3 + 0.5f));
            GlStateManager.rotate((float)(-\u26034), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)0.0f, (float)-0.3125f, (float)-0.4375f);
            this.model.signStick.showModel = false;
        }
        if (n2 >= 0) {
            this.bindTexture(DESTROY_STAGES[n2]);
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
        FontRenderer fontRenderer = this.getFontRenderer();
        \u26034 = 0.010416667f;
        GlStateManager.translate((float)0.0f, (float)0.33333334f, (float)0.046666667f);
        GlStateManager.scale((float)0.010416667f, (float)-0.010416667f, (float)0.010416667f);
        GlStateManager.glNormal3f((float)0.0f, (float)0.0f, (float)-0.010416667f);
        GlStateManager.depthMask((boolean)false);
        boolean \u26035 = false;
        if (n2 < 0) {
            for (int i = 0; i < tileEntitySign2.signText.length; ++i) {
                if (tileEntitySign2.signText[i] == null) continue;
                ITextComponent iTextComponent = tileEntitySign2.signText[i];
                List \u26036 = GuiUtilRenderComponents.splitText((ITextComponent)iTextComponent, (int)90, (FontRenderer)fontRenderer, (boolean)false, (boolean)true);
                String string = string2 = \u26036 != null && !\u26036.isEmpty() ? ((ITextComponent)\u26036.get(0)).getFormattedText() : "";
                if (i == tileEntitySign2.lineBeingEdited) {
                    String string2 = "> " + string2 + " <";
                    fontRenderer.drawString(string2, -fontRenderer.getStringWidth(string2) / 2, i * 10 - tileEntitySign2.signText.length * 5, 0);
                    continue;
                }
                fontRenderer.drawString(string2, -fontRenderer.getStringWidth(string2) / 2, i * 10 - tileEntitySign2.signText.length * 5, 0);
            }
        }
        GlStateManager.depthMask((boolean)true);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
        if (n2 >= 0) {
            GlStateManager.matrixMode((int)5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5888);
        }
    }
}
