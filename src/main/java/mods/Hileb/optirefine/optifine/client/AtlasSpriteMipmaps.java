package mods.Hileb.optirefine.optifine.client;

import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;

import java.io.IOException;

/** Atlas loading hooks implemented by resource-backed TextureAtlasSprite instances. */
public interface AtlasSpriteMipmaps {
    void optiRefine$loadAtlasSprite(PngSizeInfo size, boolean animated, int alignment) throws IOException;

    boolean optiRefine$hasAlignedResource();

    void optiRefine$clearAlignedResource();

    void optiRefine$loadShaderSidecars(TextureMap atlas, IResourceManager resourceManager);

    void optiRefine$limitMipLevels(int levels);

    int[][] optiRefine$getInitialFrame();
}
