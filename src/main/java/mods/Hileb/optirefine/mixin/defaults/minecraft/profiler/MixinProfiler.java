package mods.Hileb.optirefine.mixin.defaults.minecraft.profiler;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.profiler.Profiler;
import net.optifine.Lagometer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
@Mixin(Profiler.class)
public abstract class MixinProfiler {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 2

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field profilerGlobalEnabled (in OF Profiler, not in baseline), MCP name matches
    public boolean profilerGlobalEnabled = true;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added field profilerLocalEnabled, matches OF init (snapshot of global)
    private boolean profilerLocalEnabled = this.profilerGlobalEnabled;
    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added constants SCHEDULED_EXECUTABLES/TICK/PRE_RENDER_ERRORS/RENDER/DISPLAY, match OF
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private static final String TICK = "tick";
    @SuppressWarnings("unused")
    @Unique
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private static final String RENDER = "render";
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private static final String DISPLAY = "display";
    @Unique
// [AUDIT-OK] OF-added hash constants, match OF
    private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
    @Unique
    private static final int HASH_TICK = "tick".hashCode();
    @Unique
    private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
    @Unique
    private static final int HASH_RENDER = "render".hashCode();
    @Unique
    private static final int HASH_DISPLAY = "display".hashCode();

    @Inject(method = "clearProfiling", at = @At("RETURN"))
// [AUDIT-OK] clearProfiling RETURN refresh (localEnabled = globalEnabled) matches OF clearProfiling tail
    public void injectClearProfiling(CallbackInfo ci){
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }

    @Inject(method = "startSection(Ljava/lang/String;)V", at = @At("HEAD"))
// [AUDIT-OK] Lagometer timing + fastRender clearEnabled logic matches OF startSection; note: OF also gates the vanilla body on profilerLocalEnabled (dormant divergence, see endSection issue)
    public void injectStartSection(String name, CallbackInfo ci){
        if (Lagometer.isActive()) {
            int hashName = name.hashCode();
            if (hashName == HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
                Lagometer.timerScheduledExecutables.start();
            } else if (hashName == HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            } else if (hashName == HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
                Lagometer.timerTick.end();
            }
        }

        if (Config.isFastRender()) {
            int hashName = name.hashCode();
            if (hashName == HASH_RENDER && name.equals("render")) {
                _set_GlStateManager_clearEnabled(false);
            } else if (hashName == HASH_DISPLAY && name.equals("display")) {
                _set_GlStateManager_clearEnabled(true);
            }
        }
    }


    @WrapMethod(method = "func_194340_a")
// [AUDIT-OK] startSection(Supplier) wrap matches OF (func_194340_a not in tsrg - same name both runtimes); localEnabled gate matches OF
    public void inject_func_194340_a(Supplier<String> p_194340_1_, Operation<Void> original){
        if (this.profilerLocalEnabled) {
            original.call(p_194340_1_);
        }
    }

    @WrapMethod(method = "endSection")
// [AUDIT-FIXED] gate on profilerLocalEnabled like startSection/func_194340_a (OF semantics)
    public void injectEndSection(Operation<Void> original){
        if (this.profilerLocalEnabled) original.call();
    }

    @WrapMethod(method = "getProfilingData")
// [AUDIT-ISSUE] (see endSection issue) extra profilerGlobalEnabled gate not present in OF getProfilingData (OF only checks profilingEnabled)
    public List<Profiler.Result> injectGetProfilingData(String p_76321_1_, Operation<List<Profiler.Result>> original){
// [AUDIT-FIXED] no extra gate (OF getProfilingData checks profilingEnabled only)
        return original.call(p_76321_1_);
    }

    @WrapMethod(method = "endStartSection")
// [AUDIT-OK] endStartSection wrap matches OF (localEnabled gate)
    public void injectEndStartSection(String p_76318_1_, Operation<Void> original){
        if (this.profilerLocalEnabled) {
            original.call(p_76318_1_);
        }
    }

    @WrapMethod(method = "func_194339_b")
// [AUDIT-OK] endStartSection(Supplier) wrap matches OF (localEnabled gate)
    public void inject_func_194339_b(Supplier<String> p_194339_1_, Operation<Void> original) {
        if (this.profilerLocalEnabled) {
            original.call(p_194339_1_);
        }
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net/minecraft/client/renderer/GlStateManager clearEnabled Z")
// [AUDIT-OK] OF member GlStateManager.clearEnabled (in OF, not in baseline; provided by MixinGlStateManager), MCP name OK
    private static void _set_GlStateManager_clearEnabled(boolean val){
        throw new AbstractMethodError();
    }
}
