package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.ShadersTex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Mixin(targets = "net.optifine.shaders.ShadersTex", remap = false)
public abstract class MixinShadersTex {
    @WrapMethod(method = "loadNSMapFile", remap = false)
    private static boolean optiRefine$closeNSMapResource(IResourceManager resourceManager, ResourceLocation location,
                                                       int width, int height, int[] data, int offset,
                                                       Operation<Boolean> original) {
        if (location == null) {
            return false;
        }
        try (IResource resource = resourceManager.getResource(location);
             InputStream input = resource.getInputStream()) {
            if (input == null) {
                return false;
            }
            BufferedImage image = ImageIO.read(input);
            if (image == null || image.getWidth() != width || image.getHeight() != height) {
                return false;
            }
            image.getRGB(0, 0, width, height, data, offset, width);
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    @WrapMethod(method = "readImage", remap = false)
    private static BufferedImage optiRefine$closeAtlasMapResource(ResourceLocation location,
                                                                Operation<BufferedImage> original) {
        if (location == null) {
            return null;
        }
        try (IResource resource = mods.Hileb.optirefine.optifine.Config.getResourceManager().getResource(location);
             InputStream input = resource.getInputStream()) {
            if (input == null) {
                return null;
            }
            return ImageIO.read(input);
        } catch (IOException exception) {
            return null;
        }
    }

    @WrapOperation(method = "readImageAndMipmaps", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/ShadersTex;readImage(Lnet/minecraft/util/ResourceLocation;)Ljava/awt/image/BufferedImage;", remap = false), remap = false)
    private static BufferedImage optiRefine$checkSidecarDimensions(ResourceLocation location, Operation<BufferedImage> original,
                                                                  @Local(argsOnly = true, ordinal = 0) int width,
                                                                  @Local(argsOnly = true, ordinal = 1) int height,
                                                                  @Local(argsOnly = true) boolean border) {
        BufferedImage image = original.call(location);
        if (image != null) {
            int readHeight = border ? image.getWidth() : height;
            if (image.getWidth() + (border ? 16 : 0) != width || image.getHeight() < readHeight
                    || (long) image.getWidth() * readHeight > (long) width * height) {
                return null;
            }
        }
        return image;
    }

    @WrapOperation(method = "readImageAndMipmaps", at = @At(value = "INVOKE", target = "Ljava/awt/image/BufferedImage;getRGB(IIII[III)[I", remap = false), remap = false)
    private static int[] optiRefine$readRectangularSidecar(BufferedImage image, int x, int y, int width, int height,
                                                         int[] data, int offset, int stride, Operation<int[]> original,
                                                         @Local(argsOnly = true, ordinal = 1) int spriteHeight,
                                                         @Local(argsOnly = true) boolean border) {
        return original.call(image, x, y, width, border ? height : spriteHeight, data, offset, stride);
    }

    @WrapOperation(method = "readImageAndMipmaps", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/ShadersTex;genMipmapsSimple(II[[I)[[I", remap = false), remap = false)
    private static int[][] optiRefine$mipmapRectangularSidecar(int levels, int width, int[][] data, Operation<int[][]> original,
                                                             @Local(argsOnly = true, ordinal = 1) int height) {
        if (width == height) {
            return original.call(levels, width, data);
        }
        for (int level = 1; level <= levels; ++level) {
            if (data[level] != null) {
                continue;
            }
            int levelWidth = width >> level;
            int levelHeight = height >> level;
            int previousWidth = width >> (level - 1);
            int[] previous = data[level - 1];
            int[] current = new int[levelWidth * levelHeight];
            data[level] = current;
            for (int y = 0; y < levelHeight; ++y) {
                for (int x = 0; x < levelWidth; ++x) {
                    int index = y * 2 * previousWidth + x * 2;
                    current[y * levelWidth + x] = ShadersTex.blend4Simple(previous[index], previous[index + 1],
                            previous[index + previousWidth], previous[index + previousWidth + 1]);
                }
            }
        }
        return data;
    }
}
