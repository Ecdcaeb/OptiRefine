package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.client.AtlasSpriteMipmaps;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.MathHelper;
import net.optifine.util.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * OptiFine changes to {@link Stitcher}.
 *
 * <p>In {@code expandAndAllocateSlot}, OptiFine rounds the current atlas height down to the
 * nearest power of two before deciding whether to expand vertically, and uses a {@code 2x} bound
 * against that rounded value. This produces better packing for non-power-of-two atlases than the
 * vanilla {@code k != i1} comparison. When expanding, OptiFine grows the atlas unconditionally
 * instead of re-checking slot fit.</p>
 *
 * <p>The mipmap dimension handling ({@code getMipmapDimension}), {@code Holder.setNewDimension}
 * and the {@code addSprite} flow are already identical between vanilla and OptiFine, so they are
 * not re-implemented here.</p>
 */
@Mixin(Stitcher.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinStitcher {

    @Shadow
    // [AUDIT-OK] baseline member currentWidth (field_94318_c) exists in Stitcher
    private int currentWidth;
    @Shadow
    // [AUDIT-OK] baseline member currentHeight (field_94315_d) exists in Stitcher
    private int currentHeight;
    @Shadow
    // [AUDIT-OK] baseline member maxWidth (field_94316_e) exists in Stitcher
    private int maxWidth;
    @Shadow
    // [AUDIT-OK] baseline member maxHeight (field_94313_f) exists in Stitcher
    private int maxHeight;
    @Shadow
    // [AUDIT-OK] baseline member stitchSlots (field_94317_b) exists in Stitcher
    private List<Stitcher.Slot> stitchSlots;

    @Unique private boolean optiRefine$hasUnrotatedRectangles;

    @WrapOperation(method = "addSprite", at = @At(value = "NEW", target = "(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;I)Lnet/minecraft/client/renderer/texture/Stitcher$Holder;"))
    private Stitcher.Holder optiRefine$createHolder(TextureAtlasSprite sprite, int mipLevel, Operation<Stitcher.Holder> original) {
        Stitcher.Holder holder = original.call(sprite, mipLevel);
        if (optiRefine$keepsOrientation(sprite)) {
            this.optiRefine$hasUnrotatedRectangles = true;
            if (holder.isRotated()) {
                holder.rotate();
            }
        }
        return holder;
    }

    @WrapOperation(method = "allocateSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/Stitcher$Holder;rotate()V"))
    private void optiRefine$rotateHolder(Stitcher.Holder holder, Operation<Void> original) {
        if (!optiRefine$keepsOrientation(holder.getAtlasSprite())) {
            original.call(holder);
        }
    }

    @Unique
    private static boolean optiRefine$keepsOrientation(TextureAtlasSprite sprite) {
        // Atlas UVs and pixel uploads use the sprite's original orientation.
        return sprite.getIconWidth() != sprite.getIconHeight() && ((AtlasSpriteMipmaps) sprite).optiRefine$hasAlignedResource();
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: rounded-down power-of-two expansion decision + unconditional growth
     */
    @Inject(method = "expandAndAllocateSlot", at = @At("HEAD"), cancellable = true)
    // [AUDIT-OK] target expandAndAllocateSlot (func_94311_c)Z non-void -> CIR; body mirrors OF (roundDownToPowerOfTwo + unconditional growth)
    private void optiRefine$expandAndAllocateSlot(Stitcher.Holder holder, CallbackInfoReturnable<Boolean> cir) {
        if (this.optiRefine$hasUnrotatedRectangles) {
            cir.setReturnValue(this.optiRefine$expandForRectangles(holder));
            return;
        }
        int i = Math.min(holder.getWidth(), holder.getHeight());
        int i1 = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth + i);
        int j1 = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight + i);
        boolean flag1 = i1 <= this.maxWidth;
        boolean flag2 = j1 <= this.maxHeight;
        if (!flag1 && !flag2) {
            cir.setReturnValue(false);
            return;
        }

        int k1 = MathUtils.roundDownToPowerOfTwo(this.currentHeight);
        boolean flag = flag1 && i1 <= 2 * k1;
        if (this.currentWidth == 0 && this.currentHeight == 0) {
            flag = true;
        }

        Stitcher.Slot slot;
        if (flag) {
            if (holder.getWidth() > holder.getHeight()) {
                holder.rotate();
            }
            if (this.currentHeight == 0) {
                this.currentHeight = holder.getHeight();
            }
            slot = new Stitcher.Slot(this.currentWidth, 0, holder.getWidth(), this.currentHeight);
            this.currentWidth = this.currentWidth + holder.getWidth();
        } else {
            slot = new Stitcher.Slot(0, this.currentHeight, this.currentWidth, holder.getHeight());
            this.currentHeight = this.currentHeight + holder.getHeight();
        }

        slot.addSlot(holder);
        this.stitchSlots.add(slot);
        cir.setReturnValue(true);
    }

    @Unique
    private boolean optiRefine$expandForRectangles(Stitcher.Holder holder) {
        int width = holder.getWidth();
        int height = holder.getHeight();
        int rightWidth = this.currentWidth + width;
        int rightHeight = Math.max(this.currentHeight, height);
        int bottomWidth = Math.max(this.currentWidth, width);
        int bottomHeight = this.currentHeight + height;
        long rightArea = this.optiRefine$allocatedArea(rightWidth, rightHeight);
        long bottomArea = this.optiRefine$allocatedArea(bottomWidth, bottomHeight);
        if (rightArea == Long.MAX_VALUE && bottomArea == Long.MAX_VALUE) {
            return false;
        }

        Stitcher.Slot slot;
        if (rightArea <= bottomArea) {
            slot = new Stitcher.Slot(this.currentWidth, 0, width, rightHeight);
            if (rightHeight > this.currentHeight && this.currentWidth > 0) {
                this.stitchSlots.add(new Stitcher.Slot(0, this.currentHeight, this.currentWidth, rightHeight - this.currentHeight));
            }
            this.currentWidth = rightWidth;
            this.currentHeight = rightHeight;
        } else {
            slot = new Stitcher.Slot(0, this.currentHeight, bottomWidth, height);
            if (bottomWidth > this.currentWidth && this.currentHeight > 0) {
                this.stitchSlots.add(new Stitcher.Slot(this.currentWidth, 0, bottomWidth - this.currentWidth, this.currentHeight));
            }
            this.currentWidth = bottomWidth;
            this.currentHeight = bottomHeight;
        }
        slot.addSlot(holder);
        this.stitchSlots.add(slot);
        return true;
    }

    @Unique
    private long optiRefine$allocatedArea(int width, int height) {
        int allocationWidth = MathHelper.smallestEncompassingPowerOfTwo(width);
        int allocationHeight = MathHelper.smallestEncompassingPowerOfTwo(height);
        return allocationWidth <= this.maxWidth && allocationHeight <= this.maxHeight
                ? (long) allocationWidth * allocationHeight : Long.MAX_VALUE;
    }
}
