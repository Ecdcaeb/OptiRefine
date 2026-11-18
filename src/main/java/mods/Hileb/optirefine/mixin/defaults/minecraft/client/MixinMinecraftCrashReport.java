package mods.Hileb.optirefine.mixin.defaults.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.ICrashReportDetail;
import org.lwjgl.opengl.GL;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Minecraft.class)
public abstract class MixinMinecraftCrashReport {
    @ModifyArg(method = "addGraphicsAndWorldToCrashReport",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/crash/CrashReportCategory;addDetail(Ljava/lang/String;Lnet/minecraft/crash/ICrashReportDetail;)V"),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=OpenGL"),
                    to = @At(value = "CONSTANT", args = "stringValue=GL Caps")), index = 1)
    private ICrashReportDetail<?> optiRefine$guardOpenGlCrashDetail(ICrashReportDetail<?> detail) {
        return () -> {
            // Chunk workers add these details synchronously without a current GL context.
            try {
                GL.getCapabilities();
            } catch (IllegalStateException noContext) {
                return "Unavailable (no OpenGL context on this thread)";
            }
            return detail.call();
        };
    }
}
