/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.awt.image.BufferedImage
 *  java.io.IOException
 *  java.lang.Object
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.resources.IResourceManager
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class DynamicTexture
extends AbstractTexture {
    private final int[] dynamicTextureData;
    private final int width;
    private final int height;

    public DynamicTexture(BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
        this.updateDynamicTexture();
    }

    public DynamicTexture(int n, int n2) {
        this.width = n;
        this.height = n2;
        this.dynamicTextureData = new int[n * n2];
        TextureUtil.allocateTexture((int)this.getGlTextureId(), (int)n, (int)n2);
    }

    public void loadTexture(IResourceManager iResourceManager) throws IOException {
    }

    public void updateDynamicTexture() {
        TextureUtil.uploadTexture((int)this.getGlTextureId(), (int[])this.dynamicTextureData, (int)this.width, (int)this.height);
    }

    public int[] getTextureData() {
        return this.dynamicTextureData;
    }
}
