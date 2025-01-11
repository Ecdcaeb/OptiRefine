package mods.Hileb.optirefine.mixin.minecraft.crash;

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
    @Unique
    private boolean reported = false;

    @Shadow
    @Final
    private CrashReportCategory systemDetailsCategory;



    @Inject(method = "getCompleteReport", at = @At("HEAD"))
    public void injectGetCompleteReport(CallbackInfoReturnable<String> cir){
        if (!this.reported) {
            this.reported = true;
            CrashReporter.onCrashReport((CrashReport)(Object)this, this.systemDetailsCategory);
        }
    }

}
