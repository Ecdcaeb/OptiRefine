package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.culling;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.Frustum;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
@Mixin(Frustum.class)
public abstract class MixinFrustum {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @Shadow @Final
    // [AUDIT-OK] baseline members clippingHelper (private final) / x / y / z (deobf:10-13)
    private ClippingHelper clippingHelper;
    @Shadow
    private double x;
    @Shadow
    private double y;
    @Shadow
    private double z;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    // [AUDIT-OK] OF-added member (OF Frustum:33); delegate with x/y/z offset matches OF:34
    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        // [AUDIT-OK] ClippingHelper.isBoxInFrustumFully is OF-added (not in tsrg) — MCP name, no deobf correct; provided by MixinClippingHelper
        return ClippingHelper_isBoxInFrustumFully(clippingHelper, minX - this.x, minY - this.y, minZ - this.z, maxX - this.x, maxY - this.y, maxZ - this.z);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.culling.ClippingHelper isBoxInFrustumFully (DDDDDD)Z")
    private static native boolean ClippingHelper_isBoxInFrustumFully(ClippingHelper helper, double a0, double a1, double a2, double a3, double a4, double a5) ;
}
