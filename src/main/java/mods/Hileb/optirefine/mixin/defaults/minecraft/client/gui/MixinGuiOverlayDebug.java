package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.renderer.texture.TextureMap;
import net.optifine.SmartAnimations;
import net.optifine.TextureAnimations;
import net.optifine.util.MemoryMonitor;
import net.optifine.util.NativeMemory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug extends Gui {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added member (OF GuiOverlayDebug private field), not in baseline
    @Unique
    private String debugOF;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private List<String> debugInfoLeft;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private List<String> debugInfoRight;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private long updateInfoLeftTimeMs;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added member, not in baseline
    @Unique
    private long updateInfoRightTimeMs;

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation.Reference(TextureMap.class)
// [AUDIT-OK] OF-added member (not in tsrg): MCP name getCountAnimationsActive correct
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountAnimationsActive ()I")
    private native static int _acc_TextureMap_getCountAnimationsActive_(TextureMap instance);

    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added member (not in tsrg): MCP name getCountAnimations correct
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountAnimations ()I")
    private native static int _acc_TextureMap_getCountAnimations_(TextureMap instance);

    @SuppressWarnings("unused")
    @Unique
// [AUDIT-FIXED] 2026-08-09: runtime GuiOverlayDebug (cleanroom 0.6.9) has NO bytesToMb(J)J — the 0.6.7-era
// deobf/cleanroom baseline has it, so the old @AccessibleOperation bridge threw NoSuchMethodError in-game.
// Implement locally (identical formula: /1024/1024) instead of linking the missing runtime member.
    private static long optiRefine$bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }

// [AUDIT-OK] target call() (List<String>) matches baseline; instance handler, List return type matches
    @WrapMethod(method = "call")
    public List<String> injectCall(Operation<List<String>> original){
        //noinspection StringEquality
        if (Minecraft.getMinecraft().debug != this.debugOF) {
            StringBuilder sb = new StringBuilder(Minecraft.getMinecraft().debug);
            int fpsMin = Config.getFpsMin();
            int posFps = Minecraft.getMinecraft().debug.indexOf(" fps ");
            if (posFps >= 0) {
                sb.insert(posFps, "/" + fpsMin);
            }

            if (Config.isSmoothFps()) {
                sb.append(" sf");
            }

            if (Config.isFastRender()) {
                sb.append(" fr");
            }

            if (Config.isAnisotropicFiltering()) {
                sb.append(" af");
            }

            if (Config.isAntialiasing()) {
                sb.append(" aa");
            }

            if (Config.isRenderRegions()) {
                sb.append(" reg");
            }

            if (Config.isShaders()) {
                sb.append(" sh");
            }

            Minecraft.getMinecraft().debug = sb.toString();
            this.debugOF = Minecraft.getMinecraft().debug;
        }

        StringBuilder sbx = new StringBuilder();
        TextureMap tm = Config.getTextureMap();
        sbx.append(", A: ");
        if (SmartAnimations.isActive()) {
            if (tm != null) sbx.append(_acc_TextureMap_getCountAnimationsActive_(tm) + TextureAnimations.getCountAnimationsActive());
            sbx.append("/");
        }

        if (tm != null) sbx.append(_acc_TextureMap_getCountAnimations_(tm) + TextureAnimations.getCountAnimations());
        String ofInfo = sbx.toString();

        // [AUDIT-FIXED] .toList() is immutable; Forge's GuiOverlayDebugForge.getLeft adds rows to the returned list -> UnsupportedOperationException
        return new java.util.ArrayList<>(original.call().stream().map((s) -> s.startsWith("P: ") ? s + ofInfo : s).toList());
    }

// [AUDIT-OK] target getDebugInfoRight()Ljava/util/List; declared in baseline; OF: add(4, "Native: X/YMB"), set(5, "GC: X MB/s"); 100ms cache via debugInfoRight/updateInfoRightTimeMs mirrors OF renderDebugInfoRight caching
    @WrapMethod(method = "getDebugInfoRight")
    public List<String> injectGetDebugInfoRight(Operation<List<String>> original) {
        List<String> list = this.debugInfoRight;
        if (list == null || System.currentTimeMillis() > this.updateInfoRightTimeMs) {
            list = original.call();
            list.add(4, "Native: " + optiRefine$bytesToMb(NativeMemory.getBufferAllocated()) + "/" + optiRefine$bytesToMb(NativeMemory.getBufferMaximum()) + "MB");
            list.set(5, "GC: " + MemoryMonitor.getAllocationRateMb() + "MB/s");
            this.debugInfoRight = list;
            this.updateInfoRightTimeMs = System.currentTimeMillis() + 100L;
        }
        return list;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.debugOF = null;
        this.debugInfoLeft = null;
        this.debugInfoRight = null;
        this.updateInfoLeftTimeMs = 0L;
        this.updateInfoRightTimeMs = 0L;
    }
}
