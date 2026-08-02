package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.Mipmaps;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * OptiFine additions to {@link TextureUtil}.
 *
 * <ul>
 *     <li>new static field {@code dataArray} reused by {@code uploadTextureImageSubImpl} (allocation avoidance)</li>
 *     <li>{@code blendColors} delegates to {@link Mipmaps#alphaBlend}</li>
 *     <li>{@code setTextureClamped}/{@code setTextureBlurMipmap} made public, GL_CLAMP -&gt; GL_CLAMP_TO_EDGE,
 *     mipmap type from {@link Config#getMipmapType()}</li>
 *     <li>null-safety for {@code readImageData}/{@code readBufferedImage}</li>
 * </ul>
 *
 * <p>Note: the {@code generateMipmapData} loop fix ({@code var2.length -&gt; var2[0].length}) and the
 * {@code synchronized} block in {@code allocateTextureImpl} are already applied by the Cleanroom patches,
 * so they are intentionally not re-implemented here.</p>
 */
@Mixin(TextureUtil.class)
public abstract class MixinTextureUtil {

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static int[] dataArray = new int[4194304];

    @SuppressWarnings("unused")
    @AccessTransformer(name = "setTextureClamped", access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    private static native void acc_setTextureClamped(boolean flag);

    @SuppressWarnings("unused")
    @AccessTransformer(name = "setTextureBlurMipmap", access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
    private static native void acc_setTextureBlurMipmap(boolean blur, boolean mipmap);

    @Shadow
    private static void setTextureBlurred(boolean flag) {
    }

    @Shadow
    private static void setTextureClamped(boolean flag) {
    }

    @Shadow
    private static void copyToBuffer(int[] data, int size) {
    }

    @ModifyConstant(method = "setTextureClamped", constant = @Constant(intValue = 10496))
    private static int optiRefine$clampToEdge(int original) {
        return 33071;
    }

    @ModifyConstant(method = "setTextureBlurMipmap", constant = @Constant(intValue = 9986))
    private static int optiRefine$mipmapType(int original) {
        return Config.getMipmapType();
    }

    /**
     * OptiFine: reuse the shared dataArray instead of allocating a fresh buffer on every upload.
     */
    @Inject(method = "uploadTextureImageSubImpl", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$reuseDataArray(BufferedImage image, int x, int y, boolean blur, boolean clamp, CallbackInfo ci) {
        int width = image.getWidth();
        int height = image.getHeight();
        int stride = 4194304 / width;
        int[] data = dataArray;
        setTextureBlurred(blur);
        setTextureClamped(clamp);

        for (int offset = 0; offset < width * height; offset += width * stride) {
            int row = offset / width;
            int rows = Math.min(stride, height - row);
            int count = width * rows;
            image.getRGB(0, row, width, rows, data, 0, width);
            copyToBuffer(data, count);
            net.minecraft.client.renderer.GlStateManager.glTexSubImage2D(3553, 0, x, y + row, width, rows, 32993, 33639, TextureUtil.DATA_BUFFER);
        }
        ci.cancel();
    }

    /**
     * OptiFine: delegate mipmap color blending to Mipmaps.
     */
    @Inject(method = "blendColors", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$blendColors(int a, int b, int c, int d, boolean flag, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Mipmaps.alphaBlend(a, b, c, d));
    }

    @Inject(method = "readBufferedImage", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$nullInputStream(InputStream input, CallbackInfoReturnable<BufferedImage> cir) {
        if (input == null) {
            cir.setReturnValue(null);
        }
    }

    /**
     * OptiFine: tolerate a null image (e.g. missing texture file) instead of crashing.
     */
    @Inject(method = "readImageData", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$nullImageData(IResourceManager manager, ResourceLocation location, CallbackInfoReturnable<int[]> cir) throws IOException {
        IResource resource = null;
        try {
            resource = manager.getResource(location);
            BufferedImage image = TextureUtil.readBufferedImage(resource.getInputStream());
            if (image == null) {
                cir.setReturnValue(null);
                return;
            }
            int width = image.getWidth();
            int height = image.getHeight();
            int[] data = new int[width * height];
            image.getRGB(0, 0, width, height, data, 0, width);
            cir.setReturnValue(data);
        } finally {
            IOUtils.closeQuietly(resource);
        }
    }
}
