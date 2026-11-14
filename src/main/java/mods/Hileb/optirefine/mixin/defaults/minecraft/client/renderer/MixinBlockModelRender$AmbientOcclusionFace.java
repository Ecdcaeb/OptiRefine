package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ShadowSuper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(targets = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace")
@SuppressWarnings("unused")
public abstract class MixinBlockModelRender$AmbientOcclusionFace {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 1

    @AccessTransformer(name = "<class>", access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
// [AUDIT-OK] <class> access transform -> AmbientOcclusionFace public+static, matches OF (public class)
    public abstract void access();

    @Mutable
    @Shadow @Final
// [AUDIT-OK] baseline member vertexColorMultiplier (SRG field_178206_b)
    private float[] vertexColorMultiplier;

    @Mutable
    @Shadow @Final
// [AUDIT-OK] baseline member vertexBrightness (SRG field_178207_c)
    private int[] vertexBrightness;


    @ShadowSuper("<init>")
    public void _Object() {}

    @NewConstructor
    public void AmbientOcclusionFace() {
// [AUDIT-FIXED] super() + explicit allocation: cursed-generated <init>()V does not run @Inject(<init>*) handlers
        _Object();
        // [AUDIT-FIXED] cursed-generated <init>()V lacks vanilla field initializers; replicate them
        this.vertexBrightness = new int[4];
        this.vertexColorMultiplier = new float[4];
        // The baseline update method retains and releases its five pooled corner positions.
    }



    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.BlockModelRenderer fixAoLightValue (F)F")
    private static native float BlockModelRenderer_fixAoLightValue(float value);

    @Redirect(method = "updateVertexBrightness", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getAmbientOcclusionLightValue()F"))
    public float optiRefine$fixAoLightValue(IBlockState state) {
        return BlockModelRenderer_fixAoLightValue(state.getAmbientOcclusionLightValue());
    }

    @Unique
// [AUDIT-OK] OF-added method, not in baseline (OF AmbientOcclusionFace.setMaxBlockLight)
    public void setMaxBlockLight() {
        int maxBlockLight = 240;
        this.vertexBrightness[0] = this.vertexBrightness[0] | maxBlockLight;
        this.vertexBrightness[1] = this.vertexBrightness[1] | maxBlockLight;
        this.vertexBrightness[2] = this.vertexBrightness[2] | maxBlockLight;
        this.vertexBrightness[3] = this.vertexBrightness[3] | maxBlockLight;
        this.vertexColorMultiplier[0] = 1.0F;
        this.vertexColorMultiplier[1] = 1.0F;
        this.vertexColorMultiplier[2] = 1.0F;
        this.vertexColorMultiplier[3] = 1.0F;
    }

}
