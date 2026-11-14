package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

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
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0
    @Shadow
// [AUDIT-OK] baseline member imageWidth (SRG field_78436_b)
    private int imageWidth;

    @Shadow
// [AUDIT-OK] baseline member imageHeight (SRG field_78437_c)
    private int imageHeight;

    @Redirect(method = "parseUserSkin", at = @At(value = "NEW", target = "(III)Ljava/awt/image/BufferedImage;"))
    public BufferedImage optiRefine$createScaledSkinImage(int width, int height, int imageType, @Local(argsOnly = true) BufferedImage image, @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] target parseUserSkin(Ljava/awt/image/BufferedImage;)L...; (SRG func_78432_a) matches baseline; replicates OF upscale loop + sets k
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        int k;
        for (k = 1; this.imageWidth < srcWidth || this.imageHeight < srcHeight; k *= 2) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
        }
        skinScale.set(k);
        return new BufferedImage(this.imageWidth, this.imageHeight, 2);
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 32))
    public int optiRefine$scaleSkinHalfSize(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] kRef guard: constants before NEW (imageWidth/imageHeight=64) unscaled; post-NEW constants scaled by k — matches OF (all 32/64 in drawImage/fillRect/setAreaOpaque/height==32 scaled)
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 64))
    public int optiRefine$scaleSkinBaseSize(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] const64 applies to BIPUSH 64 occurrences; imageWidth=64/imageHeight=64 run before kRef set (0) so unscaled — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 20))
    public int optiRefine$scaleSkinCoordinate20(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 24))
    public int optiRefine$scaleSkinCoordinate24(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 28))
    public int optiRefine$scaleSkinCoordinate28(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 40))
    public int optiRefine$scaleSkinCoordinate40(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 44))
    public int optiRefine$scaleSkinCoordinate44(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 48))
    public int optiRefine$scaleSkinCoordinate48(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 52))
    public int optiRefine$scaleSkinCoordinate52(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 16))
    public int optiRefine$scaleSkinCoordinate16(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage/setAreaOpaque bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 36))
    public int optiRefine$scaleSkinCoordinate36(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 4))
    public int optiRefine$scaleSkinCoordinate4(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 8))
    public int optiRefine$scaleSkinCoordinate8(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 12))
    public int optiRefine$scaleSkinCoordinate12(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-OK] scales drawImage bounds by k — matches OF
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }

    @ModifyConstant(method = "parseUserSkin", constant = @Constant(intValue = 56))
    public int optiRefine$scaleSkinCoordinate56(int constant,  @Share(namespace = "optirefine", value = "k") LocalIntRef skinScale){
// [AUDIT-FIXED] 2026-08-05: OF:49 last drawImage source rect uses 56*k; without it HD skins
// (k>1) get an invalid mirrored head-side source rect (sx1>sx2).
        return skinScale.get() > 0 ? constant * skinScale.get() : constant;
    }
}
