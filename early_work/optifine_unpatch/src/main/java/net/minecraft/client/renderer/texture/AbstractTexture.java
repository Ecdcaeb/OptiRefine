/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureUtil
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;

public abstract class AbstractTexture
implements ITextureObject {
    protected int glTextureId = -1;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean blurLast;
    protected boolean mipmapLast;

    public void setBlurMipmapDirect(boolean bl, boolean bl22) {
        int \u26033;
        int \u26032;
        this.blur = bl;
        this.mipmap = bl22;
        if (bl) {
            \u26032 = bl22 ? 9987 : 9729;
            \u26033 = 9729;
        } else {
            boolean bl22;
            \u26032 = bl22 ? 9986 : 9728;
            \u26033 = 9728;
        }
        GlStateManager.glTexParameteri((int)3553, (int)10241, (int)\u26032);
        GlStateManager.glTexParameteri((int)3553, (int)10240, (int)\u26033);
    }

    public void setBlurMipmap(boolean bl, boolean bl2) {
        this.blurLast = this.blur;
        this.mipmapLast = this.mipmap;
        this.setBlurMipmapDirect(bl, bl2);
    }

    public void restoreLastBlurMipmap() {
        this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
    }

    public int getGlTextureId() {
        if (this.glTextureId == -1) {
            this.glTextureId = TextureUtil.glGenTextures();
        }
        return this.glTextureId;
    }

    public void deleteGlTexture() {
        if (this.glTextureId != -1) {
            TextureUtil.deleteTexture((int)this.glTextureId);
            this.glTextureId = -1;
        }
    }
}
