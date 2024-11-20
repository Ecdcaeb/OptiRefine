/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  net.minecraft.client.resources.IResourceManager
 *  net.optifine.shaders.MultiTexID
 */
package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import net.optifine.shaders.MultiTexID;

public interface ITextureObject {
    public void setBlurMipmap(boolean var1, boolean var2);

    public void restoreLastBlurMipmap();

    public void loadTexture(IResourceManager var1) throws IOException;

    public int getGlTextureId();

    public MultiTexID getMultiTexID();
}
