package mods.Hileb.optirefine.mixin.defaults.minecraft.crash;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.optifine.CrashReporter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(CrashReport.class)
public abstract class MixinCrashReport {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field reported (in OF CrashReport, not in baseline)
    @Unique
    private boolean reported = false;

    @Shadow
    @Final
// [AUDIT-OK] baseline member systemDetailsCategory exists in target class
    private CrashReportCategory systemDetailsCategory;

    @Inject(method = "getCompleteReport", at = @At("HEAD"))
// [AUDIT-OK] HEAD reported-guard + CrashReporter.onCrashReport matches OF getCompleteReport head; CIR correct for non-void target
    public void injectGetCompleteReport(CallbackInfoReturnable<String> cir){
        if (!this.reported) {
            this.reported = true;
            CrashReporter.onCrashReport((CrashReport)(Object)this, this.systemDetailsCategory);
        }
    }

}
