/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.optifine.shaders.MultiTexID
 *  net.optifine.shaders.ShadersTex
 */
package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;

public abstract class AbstractTexture
implements ITextureObject {
    protected int glTextureId = -1;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean blurLast;
    protected boolean mipmapLast;
    public MultiTexID multiTex;

    public void setBlurMipmapDirect(boolean blurIn, boolean mipmapIn) {
        int j;
        int i;
        this.blur = blurIn;
        this.mipmap = mipmapIn;
        if (blurIn) {
            i = mipmapIn ? 9987 : 9729;
            j = 9729;
        } else {
            i = mipmapIn ? 9986 : 9728;
            j = 9728;
        }
        GlStateManager.bindTexture((int)this.getGlTextureId());
        GlStateManager.glTexParameteri((int)3553, (int)10241, (int)i);
        GlStateManager.glTexParameteri((int)3553, (int)10240, (int)j);
    }

    public void setBlurMipmap(boolean blurIn, boolean mipmapIn) {
        this.blurLast = this.blur;
        this.mipmapLast = this.mipmap;
        this.setBlurMipmapDirect(blurIn, mipmapIn);
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
        ShadersTex.deleteTextures((AbstractTexture)this, (int)this.glTextureId);
        if (this.glTextureId != -1) {
            TextureUtil.deleteTexture((int)this.glTextureId);
            this.glTextureId = -1;
        }
    }

    public MultiTexID getMultiTexID() {
        return ShadersTex.getMultiTexID((AbstractTexture)this);
    }
}
