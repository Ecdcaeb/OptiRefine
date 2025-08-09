package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.optifine.SmartAnimations;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(Tessellator.class)
public abstract class MixinTessellator {

    @Shadow @Final
    private BufferBuilder buffer;

    @Inject(method = "draw", at = @At("HEAD"))
    public void ijdraw(CallbackInfo ci) {
        if (BufferBuilder_animatedSprites_get(this.buffer) != null) {
            SmartAnimations.spritesRendered(BufferBuilder_animatedSprites_get(this.buffer));
        }
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.BufferBuilder animatedSprites Ljava.util.BitSet;")
    private static native BitSet BufferBuilder_animatedSprites_get(BufferBuilder buffer);
}
