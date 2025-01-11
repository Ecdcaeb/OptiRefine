package mods.Hileb.optirefine.mixin.minecraft.profiler;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.GlStateManager;
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
    @Unique @Public
    public boolean profilerGlobalEnabled = true;
    @Unique
    private boolean profilerLocalEnabled = this.profilerGlobalEnabled;
    @Unique
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    @Unique
    private static final String TICK = "tick";
    @Unique
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    @Unique
    private static final String RENDER = "render";
    @Unique
    private static final String DISPLAY = "display";
    @Unique
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
    public void injectClearProfiling(CallbackInfo ci){
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }

    @Inject(method = "startSection(Ljava/lang/String;)V", at = @At("HEAD"))
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
                GlStateManager.clearEnabled = false;
            } else if (hashName == HASH_DISPLAY && name.equals("display")) {
                _set_GlStateManager_clearEnabled(true);
            }
        }
    }


    @WrapMethod(method = "func_194340_a")
    public void inject_func_194340_a(Supplier<String> p_194340_1_, Operation<Void> original){
        if (this.profilerLocalEnabled) {
            original.call(p_194340_1_);
        }
    }

    @WrapMethod(method = "endSection")
    public void injectEndSection(Operation<Void> original){
        if (profilerGlobalEnabled) original.call();
    }

    @WrapMethod(method = "getProfilingData")
    public List<Profiler.Result> injectGetProfilingData(String p_76321_1_, Operation<List<Profiler.Result>> original){
        if (profilerGlobalEnabled) return original.call(p_76321_1_);
        else return Collections.emptyList();
    }

    @WrapMethod(method = "endStartSection")
    public void injectEndStartSection(String p_76318_1_, Operation<Void> original){
        if (this.profilerLocalEnabled) {
            original.call(p_76318_1_);
        }
    }

    @WrapMethod(method = "func_194339_b")
    public void inject_func_194339_b(Supplier<String> p_194339_1_, Operation<Void> original) {
        if (this.profilerLocalEnabled) {
            original.call(p_194339_1_);
        }
    }

    @Unique
    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net/minecraft/client/renderer/GlStateManager clearEnabled Z")
    private static void _set_GlStateManager_clearEnabled(boolean val){
        throw new AbstractMethodError();
    }
}
