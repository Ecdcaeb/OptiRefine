package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.renderer.ImageBufferDownload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.image.BufferedImage;

@Mixin(ImageBufferDownload.class)
public abstract class MixinImageBufferDownload {
    @Shadow
    private int imageWidth;

    @Shadow
    private int imageHeight;

    @Redirect(method = "parseUserSkin", at = @At(value = "NEW", target = "(III)Ljava/awt/image/BufferedImage;"))
    public BufferedImage beforeBufferedImage(int nBits, int bOffs, int cs, @Local(argsOnly = true) BufferedImage image, @Share(namespace = "optirefine", value = "k") LocalIntRef intRef){
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        int k;
        for (k = 1; this.imageWidth < srcWidth || this.imageHeight < srcHeight; k *= 2) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
        }
        intRef.set(k);
        return new BufferedImage(this.imageWidth, this.imageHeight, 2);
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 32))
    public int const32(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 64))
    public int const64(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 20))
    public int const20(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 24))
    public int const24(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 28))
    public int const28(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 40))
    public int const40(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 44))
    public int const44(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 48))
    public int const48(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 52))
    public int const52(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 16))
    public int const16(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 36))
    public int const36(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 4))
    public int const4(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 8))
    public int const8(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 12))
    public int const12(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef kRef){
        return constant * kRef.get();
    }
}
